package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataMasterHotelArea_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelArea_M2;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.sponsor.SponsorData_M2;

/**
 * 
 * ホテルエリア検索クラス
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/04
 */

public class ActionHotelAreaSearch_M2 extends BaseAction
{

    private RequestDispatcher requestDispatcher;

    static int                pageRecords         = Constants.pageLimitRecords;     // 1ページに表示する件数
    static int                maxRecords          = Constants.maxRecords;           // 最大件数
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String             limitFreewordMsg    = Constants.errorLimitFreeword;   // 最大件数を超えた場合のエラーメッセージ
    private final boolean     DISP_PC             = false;
    private final int         DISP_COUNT          = 2;

    /**
     * ホテルエリア検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→search_hotelarea_01_M2.jsp<br>
     *      ホテルエリアID→search_result_hotelarea_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int areaId;
        int prefId;
        int masterEquipCount = 0;
        int[] arrHotelIdList = null;
        String paramAreaId = null;
        String paramPage = null;
        String areaName;
        String queryString;
        String currentPageRecords;
        String pageLinks = "";
        DataMasterHotelArea_M2 arrDataMasterHotelArea[] = null;
        SearchEngineBasic_M2 searchEngineBasic = null;
        SearchHotelArea_M2 searchHotelArea;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataSearchMasterEquip_M2[] arrDataSearchMasterEquip = null;
        SearchHotelDao_M2 searchHotelDao = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }
            paramAreaId = request.getParameter( "area_id" );
            paramPage = request.getParameter( "page" );
            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
            }

            // スマホからのアクセスならスマホ用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType != UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getSmartUrl( request ) );
                return;
            }

            if ( CheckString.numCheck( paramAreaId ) && CheckString.numCheck( paramPage ) )
            {
                try
                {
                    searchEngineBasic = new SearchEngineBasic_M2();
                    boolean isEquipListFound = false;
                    // 設備情報を取得する
                    isEquipListFound = searchEngineBasic.getEquipList( false );
                    if ( isEquipListFound != false )
                    {
                        masterEquipCount = searchEngineBasic.getMasterEquipCount();
                        arrDataSearchMasterEquip = searchEngineBasic.getMasterEquip();
                    }

                    searchHotelArea = new SearchHotelArea_M2();
                    dataSearchResult = new DataSearchResult_M2();

                    pageNum = Integer.parseInt( paramPage );
                    areaId = Integer.parseInt( paramAreaId );

                    // ホテルエリアIDから詳細情報を取得
                    searchHotelArea.getHotelAreaDetail( areaId );
                    areaName = searchHotelArea.getAreaName();
                    prefId = searchHotelArea.getPrefId();

                    // 都道府県に該当するエリア広告を取得
                    getSearchResultSponserArea( areaId, prefId, dataSearchResult );
                    // 都道府県に該当するエリア広告（ローテーション）を取得
                    getSearchResultRandomSponserArea( prefId, dataSearchResult );

                    // ホテルエリアからホテルIDリストを取得
                    arrHotelIdList = searchHotelArea.getSearchIdList( areaId );

                    if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                    {

                        searchHotelDao = new SearchHotelDao_M2();
                        searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                        // 共通にするためデータをセットする
                        arrDataSearchHotel = searchHotelDao.getHotelInfo();
                        hotelCount = searchHotelDao.getCount();
                        hotelAllCount = searchHotelDao.getAllCount();

                        queryString = "searchHotelArea.act?area_id=" + paramAreaId;
                        currentPageRecords = PagingDetails.getPageRecords( pageNum, pageRecords, hotelAllCount, hotelCount );

                        if ( hotelAllCount > pageRecords )
                        {
                            pageLinks = PagingDetails.getPagenationLink( pageNum, pageRecords, hotelAllCount, queryString );
                            dataSearchResult.setPageLink( pageLinks );
                        }
                        dataSearchResult.setRecordsOnPage( currentPageRecords );
                        dataSearchResult.setHotelCount( hotelCount );
                        dataSearchResult.setDataSearchHotel( arrDataSearchHotel );

                    }
                    else
                    {
                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                    }
                    dataSearchResult.setMasterEquipCount( masterEquipCount );
                    dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                    dataSearchResult.setParamParameter1( areaName );
                    dataSearchResult.setParamParameter2( paramAreaId );
                    request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                    if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                    {
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_hotelarea_M2.jsp" );
                    }
                    else
                    {
                        requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_hotelarea_M2.jsp" );
                    }
                    requestDispatcher.forward( request, response );

                }
                catch ( Exception e )
                {
                    Logging.error( "[ActionHotelAreaSearch_M2.execute() : AreaId = " + paramAreaId + " ] Exception=" + e.toString(), e );
                    try
                    {
                        dataSearchResult = new DataSearchResult_M2();
                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                        request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                        if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                        {
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_hotelarea_M2.jsp" );
                        }
                        else
                        {
                            requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_hotelarea_M2.jsp" );
                        }
                        requestDispatcher.forward( request, response );
                    }
                    catch ( Exception exp )
                    {
                        Logging.error( "unable to dispatch.....=" + e.toString(), exp );
                    }
                }
                finally
                {
                    searchHotelArea = null;
                    arrDataSearchHotel = null;
                    dataSearchResult = null;
                }
            }
            else
            {
                searchHotelArea = new SearchHotelArea_M2();
                arrDataMasterHotelArea = searchHotelArea.getHotelAreaListByLocal( 0 );
                request.setAttribute( "AREA-LIST", arrDataMasterHotelArea );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_hotelarea_01_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_hotelarea_01_M2.jsp" );
                }
                requestDispatcher.forward( request, response );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHotelAreaSearch_M2.execute()] Exception", e );
        }
        finally
        {

            searchHotelArea = null;
            searchHotelDao = null;
        }
    }

    /**
     * ホテルエリアIDまたは都道府県IDでエリア広告を取得
     * 
     * @param areaId ホテルエリアID
     * @param prefId 都道府県ID
     * @param dataSearchResultDTO DataSearchResult_M2クラス
     * @throws Exception
     */
    public void getSearchResultSponserArea(int areaId, int prefId, DataSearchResult_M2 dataSearchResultDTO) throws Exception
    {
        boolean ret = false;
        SponsorData_M2 sponsorData = null;
        try
        {
            sponsorData = new SponsorData_M2();
            ret = sponsorData.getSponsorByArea( areaId );
            if ( sponsorData.getSponsorCount() == 0 )
            {
                ret = sponsorData.getSponsorByPref( prefId );
            }
            dataSearchResultDTO.setSponsorDataStatus( ret );
            dataSearchResultDTO.setSponsorData( sponsorData );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHotelAreaSearch_M2.getSearchResultSponserArea() : AreaId = "
                    + areaId + ",PrefId = " + prefId + "] " + e.toString() );
            throw e;
        }
        finally
        {
            sponsorData = null;
        }
    }

    /**
     * 都道府県IDでエリア広告（ローテーション）を取得
     * 
     * @param prefId 都道府県ID
     * @param dataSearchResultDTO DataSearchResult_M2クラス
     * @throws Exception
     */
    public void getSearchResultRandomSponserArea(int prefId, DataSearchResult_M2 dataSearchResultDTO) throws Exception
    {
        boolean ret = false;
        SponsorData_M2 sponsorData = null;
        try
        {
            sponsorData = new SponsorData_M2();
            ret = sponsorData.getRandomSponsorByPref( prefId, DISP_COUNT, DISP_PC );
            dataSearchResultDTO.setRandomSponsorDataStatus( ret );
            dataSearchResultDTO.setRandomSponsorData( sponsorData );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHotelAreaSearch_M2.getSearchResultRandomSponserArea() : PrefId = " + prefId + "] " + e.toString() );
            throw e;
        }
        finally
        {
            sponsorData = null;
        }
    }
}
