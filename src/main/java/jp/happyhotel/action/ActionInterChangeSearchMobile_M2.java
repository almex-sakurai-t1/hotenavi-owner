package jp.happyhotel.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.QueryStrChecker;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataMapPoint_M2;
import jp.happyhotel.data.DataMapRoute_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.search.SearchHotelIc_M2;

/**
 * IC検索（携帯）
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/22
 */

public class ActionInterChangeSearchMobile_M2 extends BaseAction
{
    static int                pageRecords         = Constants.pageLimitRecordMobile; // 1ページで表示する件数
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    private RequestDispatcher requestDispatcher   = null;

    /**
     * IC検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      地方ID→ic_index_M2.jsp<br>
     *      都道府県ID→ic_01_M2.jsp<br>
     *      インターチェンジ名→ic_01_2_M2.jsp<br>
     *      ルートID→ic_01_2_M2.jsp<br>
     *      インターチェンジID→search_result_ic_M2.jsp"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int i = 0;
        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int hotelIdList[] = null;
        int dispFormat = 1;
        int carrierFlag;
        String paramLocalId = null;
        String paramPrefId = null;
        String paramRouteId = null;
        String paramIcId = null;
        String paramName = null;
        String paramAndWord = null;
        String paramUidLink = null;
        String paramPage = null;
        String pageHeader = null;
        String queryString = null;
        String currentPageRecords;
        String pageLinks = "";
        String paramPos; // ソフトバンクでGPS取得したときに返す位置情報のパラメータ
        String paramLat; // 位置情報を扱う扱うパラメータ
        String paramLon; // 位置情報を扱う扱うパラメータ
        String paramKind;
        DataMapPoint_M2 dMapPoint = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataMapRoute_M2[] arrDataMapRoute = null;
        DataMapPoint_M2[] arrDataMapPoint = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchHotelIc_M2 searchHotelIC = null;
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelFreeword_M2 searchHotelFreeWord = null;
        SearchEngineBasic_M2 searchEngineBasic = null;
        SearchHotelDao_M2 searchHotelDao = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 && UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_SMARTPHONE )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }
            // 昭文社の地図コードをパラメータで受けた場合はゼンリンのコードに変換しリダイレクト（#15040対応）
            QueryStrChecker qsc = new QueryStrChecker();
            int redirection_flag = 0; // 1：リダイレクト、0：スルー、-1：エラー（トップページへリダイレクト）
            String query_str = request.getQueryString();
            if ( query_str == null )
            {
                query_str = "";
            }
            query_str = query_str.replaceAll( "&amp;", "&" ); // "&"が置換されている場合があるので元に戻す（リダイレクトするため）
            redirection_flag = qsc.checkIncludingShobunshaCode( query_str );
            if ( redirection_flag == 1 )
            {
                response.sendRedirect( request.getRequestURI() + "?" + qsc.getConvertedQueryStr() );
                return;
            }
            else if ( redirection_flag == -1 )
            {
                response.sendRedirect( Url.getUrl() );
                return;
            }

            // スマホからのアクセスならスマホ用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getPCUrl( request ) );
                return;
            }

            paramLocalId = request.getParameter( "local_id" );
            paramPrefId = request.getParameter( "pref_id" );
            paramRouteId = request.getParameter( "route_id" );
            paramIcId = request.getParameter( "ic_id" );
            paramName = request.getParameter( "name" );
            paramAndWord = request.getParameter( "andword" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramPage = request.getParameter( "page" );
            paramLat = request.getParameter( "lat" );
            paramLon = request.getParameter( "lon" );
            paramPos = request.getParameter( "pos" );
            paramKind = request.getParameter( "kind" );
            carrierFlag = UserAgent.getUserAgentType( request );

            if ( paramLat == null )
            {
                paramLat = "";
            }
            if ( paramLon == null )
            {
                paramLon = "";
            }
            if ( paramPos == null )
            {
                paramPos = "";
            }
            if ( (paramKind == null) || (paramKind.equals( "" ) != false) || (CheckString.numCheck( paramKind ) == false) )
            {
                paramKind = "0";
            }

            // 位置情報を取得するデータがある場合（GPSの結果が返ってきた場合：世界測地系）
            if ( (paramLat.compareTo( "" ) != 0 && paramLon.compareTo( "" ) != 0) || paramPos.compareTo( "" ) != 0 )
            {
                // ソフトバンクはposから位置情報を分割する
                if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
                {
                    if ( paramPos.compareTo( "" ) != 0 )
                    {
                        i = paramPos.indexOf( "E" );
                        if ( i != -1 )
                        {
                            paramLat = paramPos.substring( 1, i );
                            paramLon = paramPos.substring( i + 1, paramPos.length() );
                        }
                        else
                        {
                            i = paramPos.indexOf( "W" );
                            if ( i != -1 )
                            {
                                paramLat = paramPos.substring( 1, i );
                                paramLon = paramPos.substring( i + 1, paramPos.length() );
                            }
                            else
                            {
                                paramLat = "";
                                paramLon = "";
                            }
                        }
                    }
                }
                boolean isIcListExist = false;
                searchEngineBasic = new SearchEngineBasic_M2();
                searchEngineBasic.setHotelCountFlag( true );

                isIcListExist = searchEngineBasic.getIcListByGps( paramLat, paramLon, Integer.parseInt( paramKind ) );

                // ルートIDまたはIC名で
                if ( isIcListExist )
                {
                    int totalIC = searchEngineBasic.getMapPointCount();
                    String paramOpt3 = null;
                    String paramOpt4 = null;
                    String paramOptName = null;
                    arrDataMapPoint = new DataMapPoint_M2[totalIC];
                    for( i = 0 ; i < totalIC ; i++ )
                    {
                        arrDataMapPoint[i] = new DataMapPoint_M2();
                        arrDataMapPoint[i].setMapPointHotelCount( searchEngineBasic.getMapPointHotelCount()[i] );

                        paramOpt3 = searchEngineBasic.getMapPoint()[i].getOption3();
                        if ( paramOpt3 == null )
                            paramOpt3 = "";
                        arrDataMapPoint[i].setOption3( paramOpt3 );

                        paramOpt4 = searchEngineBasic.getMapPoint()[i].getOption4();
                        if ( paramOpt4 == null )
                            paramOpt4 = "";
                        arrDataMapPoint[i].setOption4( paramOpt4 );

                        paramOptName = searchEngineBasic.getMapPoint()[i].getName();
                        if ( paramOptName == null )
                            paramOptName = "";
                        arrDataMapPoint[i].setName( paramOptName );
                    }

                    if ( arrDataMapPoint != null )
                    {
                        request.setAttribute( "INTERCHANGE-MOBILE-RESULT-ICLIST", arrDataMapPoint );
                        request.setAttribute( "GPS", "1" );
                        request.setAttribute( "KIND", paramKind );
                        // キャリアに応じてセットするパラメータを変える
                        if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
                        {
                            request.setAttribute( "POS", paramPos );
                        }
                        else
                        {
                            request.setAttribute( "LON", paramLon );
                            request.setAttribute( "LAT", paramLat );
                        }
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "ic_01_2_M2.jsp" );
                        requestDispatcher.forward( request, response );
                        return;
                    }

                }

            }

            // CASE 1 :インターチェンジIDで要求
            if ( CheckString.isvalidString( paramIcId ) && (paramIcId.equals( "true" )) && CheckString.isvalidString( paramRouteId ) )
            {
                if ( (paramPage == null) || (CheckString.numCheck( paramPage ) == false) )
                {
                    paramPage = "0";
                }
                pageNum = Integer.parseInt( paramPage );
                searchHotelIC = new SearchHotelIc_M2();

                // ルートIDでホテル情報を取得
                hotelIdList = searchHotelIC.getSearchIdList( paramRouteId );

                queryString = "searchInterChangeMobile.act?ic_id=true&route_id=" + paramRouteId;
                if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                {
                    // 現在のホテルIDリストをセット
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setEquipHotelList( hotelIdList );
                    try
                    {
                        paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );
                        searchHotelFreeWord = new SearchHotelFreeword_M2();
                        // 絞り込みフリーワードで検索
                        hotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord, paramPrefId );
                        searchHotelCommon.setResultHotelList( hotelIdList );
                        // マージを行う
                        hotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );

                        // 表示名称
                        pageHeader = "「" + paramAndWord + "」で絞込み検索しました";

                        // ページリンク
                        queryString = "searchInterChangeMobile.act?ic_id=true&route_id=" + paramRouteId + "&andword=" + paramAndWord;
                    }
                    catch ( UnsupportedEncodingException e )
                    {
                        Logging.error( "[ActionInterChangeSearchMobile.execute() ] UnsupportedEncodingException =" + e.toString() );
                        throw e;
                    }
                }

                // ホテル詳細情報の取得
                searchHotelDao = new SearchHotelDao_M2();
                searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );
                searchHotelIC.getMapPointInfo( paramRouteId );

                // データのセット
                arrDataSearchHotel = searchHotelDao.getHotelInfo();
                dMapPoint = searchHotelIC.getIcInfo();
                hotelCount = searchHotelDao.getCount();
                hotelAllCount = searchHotelDao.getAllCount();

                // 現在のページのホテル情報をセット
                currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount, hotelCount, dispFormat );

                dataSearchResult = new DataSearchResult_M2();
                if ( hotelAllCount > pageRecords )
                {
                    pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, hotelAllCount, queryString, paramUidLink );

                    dataSearchResult.setPageLink( pageLinks );
                }
                dataSearchResult.setRecordsOnPage( currentPageRecords );
                dataSearchResult.setHotelCount( hotelCount );
                dataSearchResult.setHotelAllCount( hotelAllCount );
                dataSearchResult.setPageHeader( pageHeader );
                dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                dataSearchResult.setParamParameter1( paramRouteId );
                dataSearchResult.setParamParameter3( dMapPoint.getName() );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "search_result_ic_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;

            }
            else if ( (CheckString.isvalidString( paramRouteId )) || ((paramName != null) && (paramName.compareTo( "" ) != 0)) )
            {
                boolean isIcListExist = false;
                searchEngineBasic = new SearchEngineBasic_M2();
                searchEngineBasic.setHotelCountFlag( true );

                // CASE 2 : ルートIDで要求
                if ( paramRouteId != null )
                {
                    isIcListExist = searchEngineBasic.getIcListByRouteAndPref( paramRouteId, Integer.parseInt( paramPrefId ) );
                }
                // CASE 3 : インターチェンジ名で要求
                else
                {
                    paramName = new String( paramName.getBytes( "8859_1" ), "Windows-31J" );
                    isIcListExist = searchEngineBasic.getIcListByName( paramName );
                }

                // ルートIDまたはIC名で
                if ( isIcListExist )
                {
                    int totalIC = searchEngineBasic.getMapPointCount();
                    String paramOpt3 = null;
                    String paramOpt4 = null;
                    String paramOptName = null;
                    arrDataMapPoint = new DataMapPoint_M2[totalIC];
                    for( i = 0 ; i < totalIC ; i++ )
                    {
                        arrDataMapPoint[i] = new DataMapPoint_M2();
                        arrDataMapPoint[i].setMapPointHotelCount( searchEngineBasic.getMapPointHotelCount()[i] );

                        paramOpt3 = searchEngineBasic.getMapPoint()[0].getOption3();
                        if ( paramOpt3 == null )
                            paramOpt3 = "";
                        arrDataMapPoint[i].setOption3( paramOpt3 );

                        paramOpt4 = searchEngineBasic.getMapPoint()[i].getOption4();
                        if ( paramOpt4 == null )
                            paramOpt4 = "";
                        arrDataMapPoint[i].setOption4( paramOpt4 );

                        paramOptName = searchEngineBasic.getMapPoint()[i].getName();
                        if ( paramOptName == null )
                            paramOptName = "";
                        arrDataMapPoint[i].setName( paramOptName );
                    }

                    if ( arrDataMapPoint != null )
                    {
                        request.setAttribute( "INTERCHANGE-MOBILE-RESULT-ICLIST", arrDataMapPoint );
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "ic_01_2_M2.jsp" );
                        requestDispatcher.forward( request, response );
                    }
                    else
                    {
                        response.sendRedirect( "index.jsp?" + paramUidLink );
                    }
                    return;
                }
            }
            // CASE 4 : 都道府県IDで要求
            else if ( CheckString.isvalidString( paramPrefId ) && CheckString.numCheck( paramPrefId ) )
            {
                String paramPrefName = null;
                boolean isRouteExist = false;
                searchEngineBasic = new SearchEngineBasic_M2();
                searchEngineBasic.setHotelCountFlag( false );
                // ルートリスト
                isRouteExist = searchEngineBasic.getHighwayList( Integer.parseInt( paramPrefId ) );
                if ( isRouteExist )
                {
                    boolean isPrefExist = false;
                    int totalRoutes = searchEngineBasic.getMapRouteCount();
                    arrDataMapRoute = new DataMapRoute_M2[totalRoutes];
                    // 都道府県情報
                    isPrefExist = searchEngineBasic.getPrefInfo( Integer.parseInt( paramPrefId ) );
                    if ( isPrefExist )
                    {
                        paramPrefName = searchEngineBasic.getMasterPref().getName();
                    }
                    for( i = 0 ; i < totalRoutes ; i++ )
                    {
                        arrDataMapRoute[i] = new DataMapRoute_M2();
                        arrDataMapRoute[i].setPrefName( paramPrefName );
                        arrDataMapRoute[i].setName( searchEngineBasic.getMapRoute()[i].getName() );
                        arrDataMapRoute[i].setRouteId( searchEngineBasic.getMapRoute()[i].getRouteId() );
                    }
                    if ( arrDataMapRoute != null )
                    {
                        request.setAttribute( "INTERCHANGE-MOBILE-RESULT-ROUTELIST", arrDataMapRoute );
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "ic_01_M2.jsp" );
                        requestDispatcher.forward( request, response );
                    }
                    else
                    {
                        response.sendRedirect( "index.jsp?" + paramUidLink );
                    }
                    return;
                }
            }
            // CASE 5 : 地方IDで要求
            else if ( CheckString.isvalidString( paramLocalId ) && CheckString.numCheck( paramLocalId ) )
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "ic_index_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            // CASE 6 : パラメータなし
            else
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "index_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionInterChangeSearchMobile.execute() ] Exception=" + e.toString(), e );
            try
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                dataSearchResult.setParamParameter1( paramRouteId );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "search_result_ic_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            catch ( Exception exp )
            {
                Logging.error( "unable to dispatch.....=" + exp.toString(), exp );
            }
        }
        finally
        {
            dMapPoint = null;
            dataSearchResult = null;
            arrDataMapRoute = null;
            arrDataMapPoint = null;
            arrDataSearchHotel = null;
            searchEngineBasic = null;
            searchHotelIC = null;
            searchHotelCommon = null;
            searchHotelFreeWord = null;
            searchHotelDao = null;
        }
    }
}
