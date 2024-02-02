package jp.happyhotel.action;

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
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelHotenavi_M2;
import jp.happyhotel.sponsor.SponsorData_M2;

/**
 * ホテナビ検索クラス
 * 
 * @author HCL Technologies Ltd.
 * @version 2008/09/17
 */

public class ActionHotenaviSearch_M2 extends BaseAction
{

    static int                pageRecords         = Constants.pageLimitRecords;     // 1ページの表示件数
    static int                maxRecords          = Constants.maxRecordsHotenavi;   // 最大件数
    static String             recordsNotFoundMsg1 = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String             recordsNotFoundMsg4 = Constants.errorRecordsNotFound4; // 件数なしの場合のエラーメッセージ
    static String             limitFreewordMsg    = Constants.errorLimitFreeword;   // 最大件数を超えた場合のエラーメッセージ
    private String            actionURL           = "searchHotenavi.act";
    private RequestDispatcher requestDispatcher   = null;
    private final boolean     DISP_PC             = false;
    private final int         DISP_COUNT          = 2;

    /**
     * ホテナビ検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→search_hotenavi_01_M2.jsp<br>
     *      都道府県IDあり→search_result_hotenavi_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramPrefId = "";
        // SearchHotelHotenavi_M2 searchHotelHotenavi = null;
        // DataSearchHotel_M2[] arrDataSearchHotel = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }
            paramPrefId = request.getParameter( "pref_id" );

            // スマホからのアクセスならスマホ用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType != UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getSmartUrl( request ) );
                return;
            }

            if ( CheckString.numCheck( paramPrefId ) )
            {
                searchHotels( request, response );
            }
            else
            {
                // searchHotelHotenavi = new SearchHotelHotenavi_M2( );
                // arrDataSearchHotel = searchHotelHotenavi.getHotelList();

                // request.setAttribute("HOTENAVI-HOTELS", arrDataSearchHotel);
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_hotenavi_01_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_hotenavi_01_M2.jsp" );
                }

                requestDispatcher.forward( request, response );

                return;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHotenaviSearch_M2.execute() ] Exception ", e );
        }

    }

    /**
     * ホテナビリンクのあるホテルを検索する
     * 
     * @param request
     * @param response
     */
    private void searchHotels(HttpServletRequest request, HttpServletResponse response)
    {
        boolean isEquipListFound;
        boolean sponsorStatus = false;
        boolean randomSponsorStatus = false;
        int hotelCount;
        int hotelAllCount;
        int pageNum;
        int masterEquipCount = 0;
        int prefId;
        int[] arrHotelIdList;
        String paramPage;
        String queryString;
        String currentPageRecords;
        String pageLinks = "";
        String paramPrefId = "";
        String prefName = "";
        SponsorData_M2 sponsorData = null;
        SponsorData_M2 randomSponsorData = null;
        SearchHotelHotenavi_M2 searchHotelHotenavi;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataSearchMasterEquip_M2[] arrDataSearchMasterEquip = null;
        SearchEngineBasic_M2 searchEngineBasic;
        SearchHotelDao_M2 searchHotelDao = null;

        try
        {
            paramPrefId = request.getParameter( "pref_id" );
            paramPage = request.getParameter( "page" );

            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
            }
            prefId = Integer.parseInt( paramPrefId );

            searchEngineBasic = new SearchEngineBasic_M2();
            if ( searchEngineBasic.getPrefInfo( prefId ) )
                prefName = searchEngineBasic.getMasterPref().getName();

            searchHotelHotenavi = new SearchHotelHotenavi_M2();

            // 設備情報を取得
            isEquipListFound = searchEngineBasic.getEquipList( false );
            if ( isEquipListFound != false )
            {
                masterEquipCount = searchEngineBasic.getMasterEquipCount();
                arrDataSearchMasterEquip = searchEngineBasic.getMasterEquip();
            }
            // エリア広告の取得
            sponsorData = new SponsorData_M2();
            sponsorStatus = sponsorData.getSponsorByPref( Integer.parseInt( paramPrefId ) );
            randomSponsorData = new SponsorData_M2();
            randomSponsorStatus = randomSponsorData.getRandomSponsorByPref( Integer.parseInt( paramPrefId ), DISP_COUNT, DISP_PC );

            if ( CheckString.numCheck( paramPage ) != false )
            {
                pageNum = Integer.parseInt( paramPage );

                // ホテルIDリストを取得
                arrHotelIdList = searchHotelHotenavi.getHotelIdList( prefId );
                // ホテル情報を取得
                searchHotelDao = new SearchHotelDao_M2();
                searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                // 共通にするためデータをセットする
                arrDataSearchHotel = searchHotelDao.getHotelInfo();
                hotelCount = searchHotelDao.getCount();
                hotelAllCount = searchHotelDao.getAllCount();

                currentPageRecords = PagingDetails.getPageRecords( pageNum, pageRecords, hotelAllCount, hotelCount );
                queryString = "pref_id=" + paramPrefId;
                actionURL = actionURL + "?" + queryString;

                if ( hotelAllCount < maxRecords )
                {
                    if ( hotelAllCount == 0 )
                    {
                        dataSearchResult = new DataSearchResult_M2();

                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                        dataSearchResult.setParamParameter1( prefName );
                        dataSearchResult.setMasterEquipCount( masterEquipCount );
                        dataSearchResult.setSponsorData( sponsorData );
                        dataSearchResult.setSponsorDataStatus( sponsorStatus );
                        dataSearchResult.setRandomSponsorData( randomSponsorData );
                        dataSearchResult.setRandomSponsorDataStatus( randomSponsorStatus );
                        dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                    }
                    else
                    {
                        dataSearchResult = new DataSearchResult_M2();
                        if ( hotelAllCount > pageRecords )
                        {
                            pageLinks = PagingDetails.getPagenationLink( pageNum, pageRecords, hotelAllCount, actionURL );
                            dataSearchResult.setPageLink( pageLinks );
                        }
                        dataSearchResult.setParamParameter1( prefName );
                        dataSearchResult.setRecordsOnPage( currentPageRecords );
                        dataSearchResult.setHotelCount( hotelCount );
                        dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                        dataSearchResult.setMasterEquipCount( masterEquipCount );
                        dataSearchResult.setSponsorData( sponsorData );
                        dataSearchResult.setSponsorDataStatus( sponsorStatus );
                        dataSearchResult.setRandomSponsorData( randomSponsorData );
                        dataSearchResult.setRandomSponsorDataStatus( randomSponsorStatus );
                        dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                    }
                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordsNotFoundMsg4 );
                    dataSearchResult.setParamParameter1( prefName );
                    dataSearchResult.setMasterEquipCount( masterEquipCount );
                    dataSearchResult.setSponsorData( sponsorData );
                    dataSearchResult.setSponsorDataStatus( sponsorStatus );
                    dataSearchResult.setRandomSponsorData( randomSponsorData );
                    dataSearchResult.setRandomSponsorDataStatus( randomSponsorStatus );
                    dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                }
            }
            else
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setParamParameter1( prefName );
                dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                dataSearchResult.setMasterEquipCount( masterEquipCount );
                dataSearchResult.setSponsorData( sponsorData );
                dataSearchResult.setSponsorDataStatus( sponsorStatus );
                dataSearchResult.setRandomSponsorData( randomSponsorData );
                dataSearchResult.setRandomSponsorDataStatus( randomSponsorStatus );
                dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );

            }

            request.setAttribute( "SEARCH-RESULT", dataSearchResult );
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_hotenavi_M2.jsp" );
            }
            else
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_hotenavi_M2.jsp" );
            }
            requestDispatcher.forward( request, response );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHotenaviSearch_M2.execute() ] Exception ", e );
            try
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setParamParameter1( prefName );
                dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                dataSearchResult.setSponsorData( sponsorData );
                dataSearchResult.setSponsorDataStatus( sponsorStatus );
                request.setAttribute( "SEARCH-RESULT", dataSearchResult );

                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_hotenavi_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_hotenavi_M2.jsp" );
                }

                requestDispatcher.forward( request, response );
            }
            catch ( Exception exp )
            {
                Logging.error( "unable to dispatch.....=" + exp.toString(), exp );
            }
        }
        finally
        {
            searchHotelHotenavi = null;
            arrDataSearchHotel = null;
            dataSearchResult = null;
            searchHotelDao = null;
        }
    }
}
