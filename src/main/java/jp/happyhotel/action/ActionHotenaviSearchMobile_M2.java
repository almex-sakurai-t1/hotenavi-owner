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
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelHotenavi_M2;

/**
 * ホテナビ検索クラス（携帯）
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/10/03
 */
public class ActionHotenaviSearchMobile_M2 extends BaseAction
{

    static int                pageRecords         = Constants.pageLimitRecordMobile;
    static int                maxRecords          = Constants.maxRecords;
    static String             recordsNotFoundMsg1 = Constants.errorRecordsNotFound1;
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2;
    static String             limitFreewordMsg    = Constants.errorLimitFreeword;
    public static final int   dispFormat          = 2;
    private String            actionURL           = "searchHotenaviMobile.act?pref_id=";
    private RequestDispatcher requestDispatcher   = null;

    /**
     * ホテナビ検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      地方IDあり→search_hotenavi_pref_M2.jsp<br>
     *      都道府県IDあり→search_result_hotenavi_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int[][] pref_ID = { { 1, 2, 3, 4, 5, 6, 7 },
                { 8, 9, 10, 11, 12, 13, 14 }, { 15, 19, 20 },
                { 21, 22, 23, 24 }, { 16, 17, 18 }, { 25, 26, 27, 28, 29, 30 },
                { 31, 32, 33, 34, 35 }, { 36, 37, 38, 39 },
                { 40, 41, 42, 43, 44, 45, 46, 47 }
        };

        int pageNum = 0;
        int hotelCount = 0;
        int hotelAllCount = 0;
        int[] hotelIdList = null;
        String paramLocalId = null;
        String paramPage = null;
        String paramPrefId = null;
        String paramUidLink = null;
        String queryString = null;
        String currentPageRecords = null;
        String pageLinks = null;
        DataSearchHotel_M2[] dataSearchHotelInfo = null;
        SearchHotelHotenavi_M2 searchHotelHotenavi = null;
        SearchHotelCommon searchHotelCommon = null;
        DataSearchHotel_M2 dataSearchHotelDTO = null;
        DataSearchResult_M2 dataSearchResultDTO = null;
        SearchHotelDao_M2 searchHotelDao = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 && UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_SMARTPHONE )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }
            paramLocalId = request.getParameter( "local_id" );
            paramPage = request.getParameter( "page" );
            paramPrefId = request.getParameter( "pref_id" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            // PCからのアクセスならPC用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getPCUrl( request ) );
                return;
            }

            if ( paramLocalId != null
                    && CheckString.numCheck( paramLocalId ) != false )
            {
                searchHotelHotenavi = new SearchHotelHotenavi_M2();
                dataSearchHotelDTO = new DataSearchHotel_M2();
                int LocalId = Integer.parseInt( paramLocalId );
                dataSearchHotelDTO.setHotelCount( searchHotelHotenavi
                        .getHotelCountMobile( pref_ID[LocalId - 1] ) );
                request.setAttribute( "HOTENAVI-HOTELS", dataSearchHotelDTO );
                requestDispatcher = request
                        .getRequestDispatcher( "search_hotenavi_pref_M2.jsp" );
                requestDispatcher.forward( request, response );

            }
            else if ( paramPrefId != null
                    && CheckString.numCheck( paramPrefId ) != false )
            {
                searchHotelCommon = new SearchHotelCommon();
                searchHotelHotenavi = new SearchHotelHotenavi_M2();
                dataSearchResultDTO = new DataSearchResult_M2();

                if ( paramPage != null )
                {
                    pageNum = Integer.parseInt( paramPage );
                }
                // ホテルIDリスト
                hotelIdList = searchHotelHotenavi.getHotelIdList( Integer.parseInt( paramPrefId ) );

                // ホテルIDリストをセット
                searchHotelCommon.setEquipHotelList( hotelIdList );
                // URL作成
                queryString = actionURL + paramPrefId;

                // ホテルIDリストからホテル詳細情報を取得
                searchHotelDao = new SearchHotelDao_M2();
                searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );

                // 共通にするためデータをセットする
                dataSearchHotelInfo = searchHotelDao.getHotelInfo();
                hotelCount = searchHotelDao.getCount();
                hotelAllCount = searchHotelDao.getAllCount();

                // 現在のページにホテル情報をセットする
                currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount, hotelCount, dispFormat );

                if ( hotelAllCount > pageRecords )
                {
                    pageLinks = PagingDetails.getPagenationLinkMobile( pageNum, pageRecords, hotelAllCount, queryString, paramUidLink );
                    dataSearchResultDTO.setPageLink( pageLinks );
                }
                dataSearchResultDTO.setRecordsOnPage( currentPageRecords );
                dataSearchResultDTO.setHotelCount( hotelCount );
                dataSearchResultDTO.setHotelAllCount( hotelAllCount );
                dataSearchResultDTO.setPageHeader( " " );
                dataSearchResultDTO.setDataSearchHotel( dataSearchHotelInfo );
                dataSearchResultDTO.setParamParameter1( paramPrefId );
                // dataSearchResult.setParamParameter2();
                // dataSearchResult.setParamParameter3(cityName);

                request.setAttribute( "SEARCH-RESULT", dataSearchResultDTO );

                requestDispatcher = request.getRequestDispatcher( "search_result_hotenavi_M2.jsp" );
                requestDispatcher.forward( request, response );
            }
            else
            {

                requestDispatcher = request.getRequestDispatcher( "index_M2.jsp?" + paramUidLink );
                requestDispatcher.forward( request, response );
            }
        }
        catch ( Exception exception )
        {
            Logging.error(
                    "[ActionHotenaviSearchMobile_M2 - execute()] Exception" + exception.toString() );
            try
            {
                response.sendRedirect( "../../index.jsp" );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionHotenaviSearchMobile_M2 - execute()] Exception - unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            searchHotelHotenavi = null;
            searchHotelCommon = null;
            dataSearchHotelDTO = null;
            dataSearchResultDTO = null;
            dataSearchHotelInfo = null;
            searchHotelDao = null;
        }

    }

}
