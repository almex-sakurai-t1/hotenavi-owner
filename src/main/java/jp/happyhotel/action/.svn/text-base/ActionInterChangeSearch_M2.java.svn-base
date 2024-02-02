package jp.happyhotel.action;

import java.io.PrintWriter;

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
import jp.happyhotel.data.DataInterChange_M2;
import jp.happyhotel.data.DataMapPoint_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelIc_M2;
import jp.happyhotel.sponsor.SponsorData_M2;

/**
 * IC検索クラス
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/04
 */

public class ActionInterChangeSearch_M2 extends BaseAction
{
    static int                pageRecords         = Constants.pageLimitRecords;     // 1ページに表示する件数
    static int                maxRecords          = Constants.maxRecords;           // 最大件数
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String             limitRecords        = Constants.errorLimitRecords;    // 最大件数を超えた場合のエラーメッセージ
    private RequestDispatcher requestDispatcher   = null;

    /**
     * IC検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→search_ic_01_M2.jsp<br>
     *      インターチェンジ名→search_ic_02_M2.jsp<br>
     *      インターチェンジID→search_result_ic_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount;
        int hotelAllCount;
        int pageNum;
        int masterEquipCount = 0;
        int hotelIdList[] = null;
        boolean isEquipDataExist = false;
        String paramIcId = null; // インターチェンジID
        String paramIcName = null; // インターチェンジ名
        String errorMessage = "";
        String paramPage;
        String queryString = "";
        String queryString2 = "kind=3";
        String isSubmitValue;
        String isViaIcName;
        String actionURL = "searchInterChange.act?";
        String currentPageRecords;
        String pageLinks = "";
        DataSearchResult_M2 dataSearchResult = null;
        DataMapPoint_M2 dataMapPoint = null;
        DataInterChange_M2 dataInterChange = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchMasterEquip_M2[] arrDataSearchMasterEquip = null;
        SponsorData_M2 sponsorData = null;
        SearchHotelIc_M2 searchHotelIc = null;
        SearchEngineBasic_M2 searchEngineBasic = null;
        SearchHotelDao_M2 searchHotelDao = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 )
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
            searchEngineBasic = new SearchEngineBasic_M2();
            paramIcId = request.getParameter( "ic_id" );
            paramIcName = request.getParameter( "ic_name" );
            isSubmitValue = request.getParameter( "isSubmit" );
            isViaIcName = request.getParameter( "viaIcName" );
            if ( paramIcName == null )
            {
                paramIcName = "";
            }

            if ( "GET".equals( request.getMethod() ) ) // GETでアクセスされた場合はパラメータをデコードする必要あり
                paramIcName = new String( paramIcName.getBytes( "8859_1" ), "Windows-31J" );

            // スマホからのアクセスならスマホ用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType != UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getSmartUrl( request ) );
                return;
            }

            if ( isSubmitValue != null )
            {
                String paramLocalId = request.getParameter( "local_id" );
                String paramRoutelId = request.getParameter( "route_id" );

                if ( paramLocalId != null )
                {
                    queryString2 = queryString2 + "&local_id=" + paramLocalId;
                    if ( paramRoutelId != null )
                    {
                        queryString2 = queryString2 + "&route_id=" + paramRoutelId;
                        if ( paramIcId != null )
                        {
                            queryString2 = queryString2 + "&ic_id=" + paramIcId;
                        }
                    }
                }
                request.setAttribute( "QUERY_STRING", queryString2 );

            }
            else
            {
                if ( isViaIcName != null )
                {
                    queryString2 = queryString2 + "&ic_id=" + paramIcId;
                }
                request.setAttribute( "QUERY_STRING", queryString2 );
            }

            if ( CheckString.isvalidString( paramIcId ) )
            {
                paramPage = request.getParameter( "page" );
                searchHotelIc = new SearchHotelIc_M2();
                if ( paramPage == null )
                {
                    paramPage = "0";
                }
                if ( CheckString.numCheck( paramPage ) )
                {
                    pageNum = Integer.parseInt( paramPage );
                    dataSearchResult = new DataSearchResult_M2();

                    // エリア広告を取得する
                    sponsorData = searchEngineBasic.getSponsorDataForIC( paramIcId );
                    if ( sponsorData != null )
                    {
                        // エリア広告のセット
                        dataSearchResult.setSponsorData( sponsorData );
                        dataSearchResult.setSponsorDataStatus( true );
                    }

                    // エリア広告（ローテーション）を取得する
                    sponsorData = searchEngineBasic.getRandomSponsorDataForIC( paramIcId );
                    if ( sponsorData != null )
                    {
                        // エリア広告（ローテーション）をセット
                        dataSearchResult.setRandomSponsorData( sponsorData );
                        dataSearchResult.setRandomSponsorDataStatus( true );
                    }

                    // インターチェンジIDからホテルIDリストを取得
                    hotelIdList = searchHotelIc.getSearchIdList( paramIcId );

                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );
                    searchHotelIc.getMapPointInfo( paramIcId );

                    // 共通にするためデータをセットする
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    dataMapPoint = searchHotelIc.getIcInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();

                    // 設備情報を取得する
                    isEquipDataExist = searchEngineBasic.getEquipList( false );
                    if ( isEquipDataExist )
                    {
                        masterEquipCount = searchEngineBasic.getMasterEquipCount();
                        arrDataSearchMasterEquip = searchEngineBasic.getMasterEquip();
                    }

                    // 現在のページに表示するホテル情報をセット
                    currentPageRecords = PagingDetails.getPageRecords( pageNum, pageRecords, hotelAllCount, hotelCount );
                    queryString = "ic_id=" + paramIcId;
                    actionURL = actionURL + queryString;

                    if ( hotelAllCount == 0 )
                    {
                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                        dataSearchResult.setParamParameter1( paramIcId );
                        dataSearchResult.setMasterEquipCount( masterEquipCount );
                        dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                    }
                    else
                    {
                        if ( hotelAllCount > pageRecords )
                        {
                            pageLinks = PagingDetails.getPagenationLink( pageNum, pageRecords, hotelAllCount, actionURL );
                            dataSearchResult.setPageLink( pageLinks );
                        }
                        dataSearchResult.setRecordsOnPage( currentPageRecords );
                        dataSearchResult.setHotelCount( hotelCount );
                        dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                        dataSearchResult.setDmap( dataMapPoint );
                        dataSearchResult.setParamParameter1( paramIcId );
                        dataSearchResult.setMasterEquipCount( masterEquipCount );
                        dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                    }

                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                    dataSearchResult.setParamParameter1( paramIcId );
                    dataSearchResult.setMasterEquipCount( masterEquipCount );
                    dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );

                }
            }
            else
            {
                String isIcName = request.getParameter( "isIcName" );

                if ( isIcName != null )
                {
                    dataInterChange = new DataInterChange_M2();
                    if ( paramIcName != null && (paramIcName.trim().length() > 0) )
                    {
                        int count = 0;
                        searchHotelIc = new SearchHotelIc_M2();
                        // IC名からホテル情報を取得
                        searchHotelIc.getHotelResultList( paramIcName );

                        dataInterChange.setIcName( paramIcName );
                        dataInterChange.setDataMapPoint( searchHotelIc.getMapPoint() );
                        errorMessage = searchHotelIc.getErrorMessage();
                        if ( CheckString.checkStringForNull( errorMessage ).length() != 0 )
                        {
                            dataInterChange.setErrorMessage( searchHotelIc.getErrorMessage() );
                        }
                        else
                        {
                            count = searchHotelIc.getMapPointCount();
                            if ( count < maxRecords )
                            {
                                dataInterChange.setPageRecords( pageRecords );
                                dataInterChange.setMapPointCount( count );
                                dataInterChange.setMapPointHotelCount( searchHotelIc.getMapPointHotelCount() );
                            }
                            else
                            {
                                dataInterChange.setErrorMessage( limitRecords );
                            }
                        }

                    }
                    else
                    {
                        dataInterChange.setErrorMessage( recordsNotFoundMsg2 );
                    }
                    request.setAttribute( "INTERCHANGE-RESULT", dataInterChange );
                    if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                    {
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_ic_02_M2.jsp" );
                    }
                    else
                    {
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_ic_02_M2.jsp" );
                    }
                    requestDispatcher.forward( request, response );

                    return;
                }
                else
                {

                    if ( isSubmitValue != null )
                    {
                        if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                        {
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_ic_M2.jsp" );
                        }
                        else
                        {
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_ic_M2.jsp" );
                        }
                        requestDispatcher.forward( request, response );
                        return;
                    }
                    else
                    {
                        if ( request.getParameter( "ajaxFind" ) != null && request.getParameter( "ajaxFind" ).equals( "local" ) )
                        {
                            // ローカルIDから取得
                            findLocal( request, response );
                            return;
                        }
                        else if ( request.getParameter( "ajaxFind" ) != null && request.getParameter( "ajaxFind" ).equals( "route" ) )
                        {
                            // ルートから取得
                            findRoute( request, response );
                            return;
                        }
                        else if ( request.getParameter( "ajaxFind" ) != null && request.getParameter( "ajaxFind" ).equals( "ic" ) )
                        {
                            // ICから取得
                            findIc( request, response );
                            return;
                        }
                        else
                        {
                            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                            {
                                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_ic_01_M2.jsp" );
                            }
                            else
                            {
                                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_ic_01_M2.jsp" );
                            }
                            requestDispatcher.forward( request, response );
                            return;
                        }
                    }
                }
            }
            request.setAttribute( "SEARCH-RESULT", dataSearchResult );
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_ic_M2.jsp" );
            }
            else
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_ic_M2.jsp" );
            }
            requestDispatcher.forward( request, response );
            return;
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionInterChangeSearch_M2.execute() ] Exception = " + e.toString(), e );
            try
            {
                dataSearchResult = new DataSearchResult_M2();

                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                dataSearchResult.setParamParameter1( paramIcId );
                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_ic_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_ic_M2.jsp" );
                }
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
            sponsorData = null;
            dataMapPoint = null;
            searchHotelIc = null;
            searchEngineBasic = null;
            arrDataSearchHotel = null;
            arrDataSearchMasterEquip = null;
            dataInterChange = null;
            dataSearchResult = null;
            searchHotelDao = null;
        }
    }

    /**
     * ローカルIDから検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @throws Exception
     */
    private void findLocal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        boolean isLocalExist;
        SearchEngineBasic_M2 searchEngineBasic = null;
        String paramReferer = request.getHeader( "Referer" );

        if ( paramReferer == null )
        {
            return;
        }
        // ((ServletRequest)response).setCharacterEncoding( "UTF-8" );
        response.setContentType( "text/html; charset=UTF-8" );
        PrintWriter out = response.getWriter();
        out.println( "{\"local\":[" );

        searchEngineBasic = new SearchEngineBasic_M2();
        searchEngineBasic.setHotelCountFlag( false );
        isLocalExist = searchEngineBasic.getLocalList( 0, 0 );
        if ( isLocalExist )
        {
            for( int i = 0 ; i < searchEngineBasic.getMasterLocalCount() ; i++ )
            {
                out.println( "{localId : \"" + searchEngineBasic.getMasterLocal()[i].getLocalId() + "\"," +
                        "localName : \"" + searchEngineBasic.getMasterLocal()[i].getName() + "\"}" );

                if ( i < searchEngineBasic.getMasterLocalCount() - 1 )
                {
                    out.println( "," );
                }
            }
        }
        searchEngineBasic = null;
        out.println( "]}" );
        out.flush();
        out.close();
    }

    /**
     * ルートから検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @throws Exception
     */
    private void findRoute(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // リファラーが無い場合はNGとする
        String paramReferer = request.getHeader( "Referer" );
        if ( paramReferer == null )
        {
            return;
        }
        // ((ServletRequest)response).setCharacterEncoding( "UTF-8" );
        response.setContentType( "text/html; charset=UTF-8" );
        PrintWriter out = response.getWriter();
        out.println( "{\"route\": [" );
        boolean isRouteExist;
        String paramLocal;
        SearchEngineBasic_M2 searchEngineBasic = null;

        paramLocal = request.getParameter( "local_id" );
        if ( paramLocal != null )
        {
            if ( CheckString.numCheck( paramLocal ) )
            {
                searchEngineBasic = new SearchEngineBasic_M2();
                searchEngineBasic.setHotelCountFlag( false );
                isRouteExist = searchEngineBasic.getHighwayListByLocal( Integer.parseInt( paramLocal ) );

                if ( isRouteExist )
                {
                    for( int i = 0 ; i < searchEngineBasic.getMapRouteCount() ; i++ )
                    {
                        out.println( "{routeId : \"" + searchEngineBasic.getMapRoute()[i].getRouteId() + "\"," +
                                "routeName : \"" + searchEngineBasic.getMapRoute()[i].getName() + "\"}" );

                        if ( i < searchEngineBasic.getMapRouteCount() - 1 )
                        {
                            out.println( "," );
                        }
                    }
                }
            }
            searchEngineBasic = null;
        }
        out.println( "]}" );
        out.flush();
        out.close();
    }

    /**
     * ICから検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @throws Exception
     */
    private void findIc(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // リファラーが無い場合はNGとする
        boolean isIcExist;
        String paramLocal;
        String paramRoute;
        SearchEngineBasic_M2 searchEngineBasic = null;
        String paramReferer = request.getHeader( "Referer" );

        if ( paramReferer == null )
        {
            return;
        }

        // ((ServletRequest)response).setCharacterEncoding( "UTF-8" );
        response.setContentType( "text/html; charset=UTF-8" );
        PrintWriter out = response.getWriter();

        out.println( "{\"ic\": [" );
        paramLocal = request.getParameter( "local_id" );
        paramRoute = request.getParameter( "route_id" );
        if ( paramLocal != null && paramRoute != null )
        {
            if ( CheckString.numCheck( paramLocal ) )
            {
                searchEngineBasic = new SearchEngineBasic_M2();
                isIcExist = searchEngineBasic.getIcList( paramRoute );
                if ( isIcExist )
                {
                    for( int i = 0 ; i < searchEngineBasic.getMapPointCount() ; i++ )
                    {
                        if ( searchEngineBasic.getMapPointHotelCount()[i] != 0 )
                        {
                            out.println( "{icId : \"" + searchEngineBasic.getMapPoint()[i].getOption4() + "\"," +
                                    "icName : \"" + searchEngineBasic.getMapPoint()[i].getName() + "\"," +
                                    "hotelCount : \"" + searchEngineBasic.getMapPointHotelCount()[i] + "\"}" );
                            if ( i < searchEngineBasic.getMapPointCount() - 1 )
                            {
                                out.println( "," );
                            }
                        }
                    }
                }
                searchEngineBasic = null;
            }
        }

        out.println( "]}" );
        out.flush();
        out.close();
    }

}
