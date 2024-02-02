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
import jp.happyhotel.data.DataHotelArea_M2;
import jp.happyhotel.data.DataSearchArea_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchHotelArea_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;

/**
 * ホテルエリア検索クラス（携帯）
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/23
 */

public class ActionHotelAreaSearchMobile_M2 extends BaseAction
{

    static int                pageRecords       = Constants.pageLimitRecordMobile;
    static int                maxRecords        = Constants.maxRecordsMobile;
    static String             recordNotFound2   = Constants.errorRecordsNotFound2;
    private RequestDispatcher requestDispatcher = null;
    public static final int   dispFormat        = 1;

    /**
     * ホテルエリア検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      都道府県ID、ホテルエリアID→spot_02_M2.jsp<br>
     *      ホテルエリアID→search_result_hotelarea_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int areaId;
        int prefId = 1;
        int[] arrHotelIdList = null;
        String areaName;
        String paramAreaId = null;
        String paramPrefId = null;
        String paramLocalId = null;
        String paramPage;
        String queryString;
        String paramAndWord;
        String currentPageRecords;
        String pageLinks = "";
        String paramUidLink = null;
        String pageHeader = null;
        SearchHotelArea_M2 searchHotelArea;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataSearchArea_M2 dataSearchAreaDTO = null;
        SearchHotelCommon searchHotelCommon = null;
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
            paramAreaId = request.getParameter( "area_id" );
            paramLocalId = request.getParameter( "local_id" );
            paramPage = request.getParameter( "page" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramAndWord = request.getParameter( "andword" );
            searchHotelArea = new SearchHotelArea_M2();

            // PCからのアクセスならPC用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                String queryStr = Url.getPCUrl( request );
                response.sendRedirect( request.getContextPath() + queryStr );
                return;
            }

            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
            }
            if ( CheckString.numCheck( paramAreaId )
                    && CheckString.numCheck( paramPage ) != false )
            {
                pageNum = Integer.parseInt( paramPage );
                areaId = Integer.parseInt( paramAreaId );

                searchHotelArea.getHotelAreaDetail( areaId );
                prefId = searchHotelArea.getPrefId();
                areaName = searchHotelArea.getAreaName();

                // ホテルエリアIDからデータ取得
                arrHotelIdList = searchHotelArea.getSearchIdList( areaId );

                searchHotelCommon = new SearchHotelCommon();
                searchHotelCommon.setEquipHotelList( arrHotelIdList );

                if ( CheckString.isvalidString( paramAndWord ) )
                {
                    try
                    {
                        paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );
                        searchHotelFreeWord = new SearchHotelFreeword_M2();
                        arrHotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );
                        searchHotelCommon.setResultHotelList( arrHotelIdList );
                        arrHotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );

                        // 表示名称
                        pageHeader = "「" + paramAndWord + "」で絞込み検索しました";

                        // ページリンク
                        queryString = "searchHotelAreaMobile.act?area_id=" + areaId + "&andword=" + paramAndWord;
                    }
                    catch ( UnsupportedEncodingException e )
                    {
                        Logging.error( "[ActionHotelAreaSearchMobile_M2.execute():paramAndWord =" + paramAndWord + " ] Exception=" + e.toString() );
                        throw e;
                    }
                }

                dataSearchResult = new DataSearchResult_M2();

                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {
                    // ホテル詳細情報を取得
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                    // 共通にするためデータをセットする
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();

                    queryString = "searchHotelAreaMobile.act?area_id=" + areaId;
                    if ( paramAndWord != null && paramAndWord.compareTo( "" ) != 0 )
                    {
                        queryString = queryString + "&andword=" + paramAndWord;
                    }
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
                dataSearchResult.setParamParameter1( Integer.toString( prefId ) );
                dataSearchResult.setParamParameter2( paramAreaId );
                dataSearchResult.setParamParameter3( areaName );
                dataSearchResult.setParamParameter4( paramAndWord );
                dataSearchResult.setParamParameter5( paramPage );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "search_result_hotelarea_M2.jsp" );
                requestDispatcher.forward( request, response );
            }
            else if ( CheckString.numCheck( paramPrefId ) && paramAreaId == null
                    && CheckString.numCheck( paramPage ) != false )
            {
                dataSearchAreaDTO = new DataSearchArea_M2();
                int prefID = Integer.parseInt( paramPrefId );
                dataSearchAreaDTO.setParameter1( paramLocalId );
                dataSearchAreaDTO.setPrefName( searchHotelArea.getPrefInfo( prefID ) );

                // Fetch Area Details
                getSearchHotelArea( prefID, dataSearchAreaDTO );

                request.setAttribute( "SEARCH-RESULT", dataSearchAreaDTO );
                requestDispatcher = request.getRequestDispatcher( "spot_02_M2.jsp" );
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
            Logging.error( "[ActionHotelAreaSearch_M2.execute() ] Exception=" + exception.toString(), exception );
            try
            {
                response.sendRedirect( "../../index.jsp" );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionHotelAreaSearch_M2.execute() ] Exception : Unable to dispatch.....="
                        + subException.toString() );
            }
        }
        finally
        {
            dataSearchAreaDTO = null;
            arrDataSearchHotel = null;
            dataSearchResult = null;
            searchHotelDao = null;
        }
    }

    /**
     * ホテルエリア一覧取得（都道府県ID）
     * 
     * @param prefId 都道府県ID
     * @param dataSearchAreaDTO DataSearchArea_M2クラス
     * @throws Exception
     */
    public void getSearchHotelArea(int prefId, DataSearchArea_M2 dataSearchAreaDTO) throws Exception
    {

        SearchHotelArea_M2 searchHotelArea = null;
        DataHotelArea_M2[] arrDataHotelArea = null;
        try
        {
            searchHotelArea = new SearchHotelArea_M2();
            arrDataHotelArea = searchHotelArea.getAreaListByPref( prefId );

            if ( arrDataHotelArea != null )
            {
                dataSearchAreaDTO.setDataHotelArea( arrDataHotelArea );
                dataSearchAreaDTO.setHotelAreaCount( searchHotelArea.getAreaCount() );
            }
            else
            {
                dataSearchAreaDTO.setHotelAreaCount( 0 );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionHotelAreaSearch_M2.getSearchHotelArea() :PrefId=" + prefId + " ]  " + exception.toString() );
            throw exception;
        }
        finally
        {
            searchHotelArea = null;
            arrDataHotelArea = null;

        }
    }
}
