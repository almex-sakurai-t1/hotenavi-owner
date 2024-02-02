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
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelCity_M2;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataSearchArea_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchArea_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;

/**
 * 
 * 住所検索クラス（携帯）
 * 
 * @author HCL Technologies Ltd.
 * @version 1.0 2008/09/19
 */

public class ActionAreaSearchMobile_M2 extends BaseAction
{

    static int                pageRecords       = Constants.pageLimitRecordMobile;
    static int                maxRecords        = Constants.maxRecordsMobile;
    static String             recordNotFound2   = Constants.errorRecordsNotFound2;
    public static final int   dispFormat        = 1;
    private RequestDispatcher requestDispatcher = null;

    /**
     * 住所検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      都道府県ID、地方ID→area_01_M2.jsp<br>
     *      市区町村ID→search_result_area_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int jisCode;
        int prefId;
        int[] arrHotelIdList = null;
        String cityName;
        String paramJisCode = null;
        String paramPrefId = null;
        String paramLocalId = null;
        String paramPage;
        String queryString;
        String paramAndWord;
        String currentPageRecords;
        String pageLinks = "";
        String paramUidLink = null;
        String pageHeader = null;
        SearchArea_M2 searchArea;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataSearchArea_M2 dataSearchAreaDTO = null;
        SearchHotelCommon shcom = null;
        SearchHotelFreeword_M2 searchHotelFreeWord = null;
        SearchHotelDao_M2 searchHotelDao = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 && UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_SMARTPHONE )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }
            paramPrefId = request.getParameter( "pref_id" );
            paramLocalId = request.getParameter( "local_id" );
            paramJisCode = request.getParameter( "jis_code" );
            paramPage = request.getParameter( "page" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramAndWord = request.getParameter( "andword" );

            searchArea = new SearchArea_M2();

            // スマホからのアクセスならスマホ用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getPCUrl( request ) );
                return;
            }

            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
            }

            if ( CheckString.numCheck( paramJisCode ) && CheckString.numCheck( paramPage ) )
            {
                pageNum = Integer.parseInt( paramPage );
                jisCode = Integer.parseInt( paramJisCode );

                // 市区町村データの取得
                searchArea.getCityDetail( jisCode );
                cityName = searchArea.getCityName();
                prefId = searchArea.getPrefId();

                // 市区町村IDからホテルIDの取得
                arrHotelIdList = searchArea.getSearchIdList( jisCode );

                shcom = new SearchHotelCommon();
                shcom.setPrefHotelList( arrHotelIdList );
                queryString = "searchAreaMobile.act?jis_code=" + paramJisCode;

                if ( CheckString.isvalidString( paramAndWord ) )
                {
                    try
                    {
                        paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );

                        searchHotelFreeWord = new SearchHotelFreeword_M2();

                        arrHotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );

                        shcom.setResultHotelList( arrHotelIdList );

                        arrHotelIdList = shcom.getMargeHotel( pageRecords, pageNum );

                        // 表示名称
                        pageHeader = "「" + paramAndWord + "」を検索しました";

                        // ページリンクの作成
                        queryString = "searchAreaMobile.act?jis_code=" + paramJisCode + "&andword=" + paramAndWord;

                    }
                    catch ( UnsupportedEncodingException e )
                    {
                        Logging.error( "[ActionFreeWordSearchMobile.execute(): [AndWord= " + paramAndWord + " ]] Exception=" + e.toString() );
                        throw e;
                    }
                }
                dataSearchResult = new DataSearchResult_M2();
                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {
                    // ホテル詳細情報の取得
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                    // 共通にするためデータをセットする
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();

                    currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount, hotelCount, dispFormat );

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
                }
                else
                {
                    dataSearchResult.setHotelCount( 0 );
                    dataSearchResult.setHotelAllCount( 0 );
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordNotFound2 );

                }
                Logging.info( "arrHotelIdList:" + arrHotelIdList.length );
                dataSearchResult.setParamParameter1( Integer.toString( prefId ) );
                dataSearchResult.setParamParameter2( paramJisCode );
                dataSearchResult.setParamParameter3( cityName );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "search_result_area_M2.jsp" );
                requestDispatcher.forward( request, response );

            }
            else if ( CheckString.numCheck( paramPrefId ) && paramJisCode == null && paramLocalId != null )
            {
                dataSearchAreaDTO = new DataSearchArea_M2();
                int prefID = Integer.parseInt( paramPrefId );

                // 市区町村データを取得
                getSearchCity( prefID, dataSearchAreaDTO );

                // 地方IDをセット
                dataSearchAreaDTO.setParameter1( paramLocalId );

                request.setAttribute( "SEARCH-RESULT", dataSearchAreaDTO );
                requestDispatcher = request.getRequestDispatcher( "area_01_M2.jsp" );
                requestDispatcher.forward( request, response );

            }
            else
            {

                requestDispatcher = request.getRequestDispatcher( "index_M2.jsp" );
                requestDispatcher.forward( request, response );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearchMobile.execute() ][paramJisCode = "
                    + paramJisCode + ",paramPrefId = " + paramPrefId + ",paramLocalId = " + paramLocalId + "] Exception", exception );
            try
            {
                response.sendRedirect( "../../index.jsp" );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionAreaSearchMobile.execute() ] - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            searchArea = null;
            arrDataSearchHotel = null;
            dataSearchResult = null;
        }
    }

    /**
     * 市区町村データ取得（都道府県IDで取得）
     * 
     * @param PrefId 都道府県ID
     * @param dataSearchAreaDTO DataSearchArea_M2クラス
     * @throws Exception
     */
    public void getSearchCity(int PrefId, DataSearchArea_M2 dataSearchAreaDTO) throws Exception
    {
        SearchArea_M2 searchArea = null;
        DataHotelCity_M2[] arrDataHotelCity = null;
        DataMasterPref dataMasterPref = null;
        boolean isMasterPrefFound = false;
        try
        {
            searchArea = new SearchArea_M2();
            arrDataHotelCity = searchArea.getCityListByPref( PrefId );

            if ( arrDataHotelCity != null )
            {
                dataSearchAreaDTO.setDataHotelCity( arrDataHotelCity );
                dataSearchAreaDTO.setPrefName( arrDataHotelCity[0].getPrefName() );
                dataSearchAreaDTO.setCityCount( searchArea.getCityCount() );
            }
            else
            {
                dataMasterPref = new DataMasterPref();

                isMasterPrefFound = dataMasterPref.getData( PrefId );
                if ( isMasterPrefFound )
                {
                    dataSearchAreaDTO.setPrefName( dataMasterPref.getName() );
                }
                dataSearchAreaDTO.setCityCount( 0 );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionAreaSearchMobile_M2.getSearchCity(): PrefId = " + PrefId + " ] " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchArea = null;
            arrDataHotelCity = null;
            dataMasterPref = null;
        }
    }

}
