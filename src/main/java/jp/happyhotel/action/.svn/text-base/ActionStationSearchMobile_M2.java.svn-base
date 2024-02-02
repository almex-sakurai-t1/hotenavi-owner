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
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.data.DataSearchStation_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.search.SearchHotelStation_M2;

/**
 * 駅検索クラス（携帯）
 * 
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/23
 */
public class ActionStationSearchMobile_M2 extends BaseAction
{

    private RequestDispatcher requestDispatcher   = null;

    static int                pageRecords         = Constants.pageLimitRecordMobile; // 1ページに表示する件数
    static int                maxRecords          = Constants.maxRecords;           // 最大件数
    static String             recordsNotFoundMsg1 = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String             limitFreewordMsg    = Constants.errorLimitFreeword;   // 最大件数を超えた場合のエラーメッセージ

    /**
     * 駅検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      地方ID、都道府県ID→st_01_M2.jsp<br>
     *      駅名→st_01_2_M2.jsp<br>
     *      都道府県ID、ルートID→st_01_2_M2.jsp<br>
     *      ルートID、searchパラメータ→st_result_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramPrefId = null;
        String paramRouteId = null;
        String paramSearch = null;
        String paramPage = null;
        String paramUidLink = null;
        String paramName = null;
        String queryString = null;
        String paramAndWord = null;
        String paramLocalId = null;
        String currentPageRecords = null;
        String pageLinks = null;
        String pageHeader = null;
        String paramPos; // ソフトバンクでGPS取得したときに返す位置情報のパラメータ
        String paramLat; // 位置情報を扱う扱うパラメータ
        String paramLon; // 位置情報を扱う扱うパラメータ
        String paramKind;
        int i;
        int dispFormat = 1;
        int pageNum = 0;
        int hotelCount;
        int hotelAllCount;
        int carrierFlag;
        int[] hotelIdList = null;
        DataSearchStation_M2 stationSearchDTO = null;
        DataSearchResult_M2 dataSearchResultDTO = null;
        DataMapPoint_M2 dataMapPointDTO = null;
        SearchHotelStation_M2 searchHotelStation = null;
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelFreeword_M2 searchHotelFreeWord = null;
        DataSearchHotel_M2[] dataSearchHotel = null;
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

            paramPrefId = request.getParameter( "pref_id" );
            paramRouteId = request.getParameter( "route_id" );
            paramName = request.getParameter( "name" );
            paramSearch = request.getParameter( "search" );
            paramPage = request.getParameter( "page" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramAndWord = request.getParameter( "andword" );
            paramLocalId = request.getParameter( "local_id" );
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

            // PCからのアクセスならPC用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getPCUrl( request ) );
                return;
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
                stationSearchDTO = new DataSearchStation_M2();

                // 駅情報をセットする
                this.getSearchStationListByGps( paramLat, paramLon, Integer.parseInt( paramKind ), stationSearchDTO );

                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
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
                requestDispatcher = request.getRequestDispatcher( "st_01_2_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( CheckString.numCheck( paramPrefId ) && CheckString.numCheck( paramLocalId )
                    && paramRouteId == null )
            {
                int ParamPrefId = Integer.parseInt( paramPrefId );
                stationSearchDTO = new DataSearchStation_M2();
                stationSearchDTO.setParamPrefId( ParamPrefId );
                stationSearchDTO.setParamLocalID( Integer.parseInt( paramLocalId ) );

                // stationSearchDTOにデータをセット
                getSearchStationRoute( ParamPrefId, stationSearchDTO );
                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
                requestDispatcher = request.getRequestDispatcher( "st_01_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( paramName != null && paramName.compareTo( "null" ) != 0 )
            {
                stationSearchDTO = new DataSearchStation_M2();
                searchHotelStation = new SearchHotelStation_M2();
                boolean isStationFound = false;
                paramName = new String( paramName.getBytes( "8859_1" ), "Windows-31J" );
                stationSearchDTO.setParamName( paramName );

                if ( paramName.compareTo( "" ) != 0 )
                {
                    isStationFound = searchHotelStation
                            .getRailwayStationListByName( paramName );
                    // stationSearchDTO.setReturn(isStationFound);

                    if ( isStationFound )
                    {
                        stationSearchDTO.setMapPointCount( searchHotelStation.getMapPointCount() );
                        stationSearchDTO.setMapPointHotelCount( searchHotelStation.getMapPointHotelCount() );
                        stationSearchDTO.setMapPoint( searchHotelStation.getMapPoint() );
                    }
                }
                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
                requestDispatcher = request.getRequestDispatcher( "st_01_2_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( CheckString.isvalidString( paramRouteId )
                    && CheckString.numCheck( paramPrefId )
                    && paramSearch == null )
            {
                int ParamPrefId = Integer.parseInt( paramPrefId );
                stationSearchDTO = new DataSearchStation_M2();
                stationSearchDTO.setParamPrefId( ParamPrefId );

                getSearchStationList( paramRouteId, paramPrefId, stationSearchDTO );
                request.setAttribute( "SEARCH-RESULT", stationSearchDTO );
                requestDispatcher = request.getRequestDispatcher( "st_01_2_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( CheckString.isvalidString( paramSearch )
                    && CheckString.isvalidString( paramRouteId ) )
            {
                searchHotelStation = new SearchHotelStation_M2();
                searchHotelCommon = new SearchHotelCommon();
                dataSearchResultDTO = new DataSearchResult_M2();

                if ( paramPage != null )
                {
                    pageNum = Integer.parseInt( paramPage );
                }

                // ホテルIDリストをセット
                hotelIdList = searchHotelStation.getHotelIdList( paramRouteId );
                searchHotelCommon.setEquipHotelList( hotelIdList );

                queryString = "searchStationMobile.act?search=2&route_id=" + paramRouteId;

                if ( CheckString.isvalidString( paramAndWord ) )
                {
                    try
                    {
                        paramAndWord = new String( paramAndWord
                                .getBytes( "8859_1" ), "Windows-31J" );
                        searchHotelFreeWord = new SearchHotelFreeword_M2();
                        // 絞り込みフリーワードでホテルIDリストを取得
                        hotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );
                        // ホテルIDリストをセット
                        searchHotelCommon.setResultHotelList( hotelIdList );

                        hotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );

                        // 表示名称
                        pageHeader = "「" + paramAndWord + "」で絞込み検索しました";

                        // ページリンク
                        queryString = "searchStationMobile.act?search=2&route_id=" + paramRouteId + "&andword=" + paramAndWord;

                    }
                    catch ( UnsupportedEncodingException e )
                    {
                        Logging.error( "[ActionStationSearchMobile_M2.execute() ] Exception=" + e.toString() );
                    }
                }
                else
                {
                    if ( searchHotelStation.getDataMapPoint( paramRouteId ) )
                    {
                        dataMapPointDTO = searchHotelStation.getStationInfo();
                        pageHeader = dataMapPointDTO.getName();
                    }
                }

                // ホテルIDリストからホテル詳細情報を取得
                searchHotelDao = new SearchHotelDao_M2();
                searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );

                // 共通にするためデータをセットする
                dataSearchHotel = searchHotelDao.getHotelInfo();
                hotelCount = searchHotelDao.getCount();
                hotelAllCount = searchHotelDao.getAllCount();
                currentPageRecords = PagingDetails.getPageRecordsMobile(
                        pageNum, pageRecords, hotelAllCount, hotelCount,
                        dispFormat );

                if ( hotelAllCount > pageRecords )
                {
                    pageLinks = PagingDetails.getPagenationLinkMobile(
                            pageNum, pageRecords, hotelAllCount, queryString,
                            paramUidLink );
                    dataSearchResultDTO.setPageLink( pageLinks );
                }

                dataSearchResultDTO.setRecordsOnPage( currentPageRecords );
                dataSearchResultDTO.setHotelCount( hotelCount );
                dataSearchResultDTO.setHotelAllCount( hotelAllCount );
                dataSearchResultDTO.setPageHeader( pageHeader );
                dataSearchResultDTO.setDataSearchHotel( dataSearchHotel );
                dataSearchResultDTO.setParamParameter1( paramRouteId );
                // dataSearchResultDTO.setParamParameter2();
                // dataSearchResultDTO.setParamParameter3(cityName);

                request.setAttribute( "SEARCH-RESULT", dataSearchResultDTO );
                requestDispatcher = request.getRequestDispatcher( "st_result_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else
            {
                if ( paramUidLink != null )
                {
                    requestDispatcher = request.getRequestDispatcher( "index_M2.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( "index_M2.jsp" );
                    requestDispatcher.forward( request, response );
                    return;
                }
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearchMobile_M2.execute()] Exception",
                    exception );
            try
            {
                response.sendRedirect( "../../index.jsp" );
            }
            catch ( Exception subException )
            {
                Logging.error( "[[ActionStationSearchMobile_M2.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            stationSearchDTO = null;
            dataSearchResultDTO = null;
            dataMapPointDTO = null;
            searchHotelCommon = null;
            searchHotelStation = null;
            searchHotelDao = null;
        }
    }

    /**
     * ルートリスト取得
     * 
     * @param paramPrefId 都道府県ID
     * @param stationSearchDTO DataSearchStation_M2クラス
     * @throws Exception
     */
    public void getSearchStationRoute(int paramPrefId, DataSearchStation_M2 stationSearchDTO) throws Exception
    {
        boolean isRailwayRouteFound = false;
        SearchHotelStation_M2 searchHotelStation = null;
        try
        {
            searchHotelStation = new SearchHotelStation_M2();
            isRailwayRouteFound = searchHotelStation.getRailwayRouteList( paramPrefId );
            stationSearchDTO.setReturn( isRailwayRouteFound );

            if ( isRailwayRouteFound )
            {
                stationSearchDTO.setName( searchHotelStation.getPrefName() );
                stationSearchDTO.setMapRouteCount( searchHotelStation.getMapRouteCount() );
                stationSearchDTO.setMapRoute( searchHotelStation.getMapRoute() );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearchMobile_M2.getSearchStationRoute()] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelStation = null;
        }
    }

    /**
     * 駅リスト取得
     * 
     * @param paramRouteId ルートID
     * @param paramPrefId 都道府県ID
     * @param stationSearchDTO DataSearchStation_M2クラス
     * @throws Exception
     */
    public void getSearchStationList(String paramRouteId, String paramPrefId,
            DataSearchStation_M2 stationSearchDTO) throws Exception
    {
        boolean isStationListFound = false;
        SearchHotelStation_M2 searchHotelStation = null;
        try
        {
            searchHotelStation = new SearchHotelStation_M2();
            isStationListFound = searchHotelStation.getRailwayStationList( paramRouteId, Integer
                    .parseInt( paramPrefId ) );
            stationSearchDTO.setReturn( isStationListFound );

            if ( isStationListFound )
            {
                stationSearchDTO.setMapPointCount( searchHotelStation.getMapPointCount() );
                stationSearchDTO.setMapPointHotelCount( searchHotelStation
                        .getMapPointHotelCount() );
                stationSearchDTO.setMapPoint( searchHotelStation.getMapPoint() );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearchMobile_M2.getSearchStationList()] "
                    + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelStation = null;
        }
    }

    /**
     * 駅リスト取得(GPS)
     * 
     * @param paramRouteId ルートID
     * @param paramPrefId 都道府県ID
     * @param stationSearchDTO DataSearchStation_M2クラス
     * @throws Exception
     */
    public void getSearchStationListByGps(String lat, String lon, int kind, DataSearchStation_M2 stationSearchDTO)
            throws Exception
    {
        boolean isStationListFound = false;
        SearchHotelStation_M2 searchHotelStation = null;
        try
        {
            searchHotelStation = new SearchHotelStation_M2();
            isStationListFound = searchHotelStation.getRailwayStationListByGps( lat, lon, kind );
            stationSearchDTO.setReturn( isStationListFound );

            if ( isStationListFound )
            {
                stationSearchDTO.setMapPointCount( searchHotelStation.getMapPointCount() );
                stationSearchDTO.setMapPointHotelCount( searchHotelStation.getMapPointHotelCount() );
                stationSearchDTO.setMapPoint( searchHotelStation.getMapPoint() );
            }
        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionStationSearchMobile_M2.getSearchStationListByGps()] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelStation = null;
        }
    }

}
