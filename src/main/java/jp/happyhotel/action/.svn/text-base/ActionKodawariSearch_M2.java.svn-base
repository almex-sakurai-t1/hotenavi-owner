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
import jp.happyhotel.data.DataEquipListResult_M2;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchKodawari_M2;

/**
 * 
 * こだわり検索クラス
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/16
 */

public class ActionKodawariSearch_M2 extends BaseAction
{

    static int                pageRecords         = Constants.pageLimitRecords;     // maximum records to be displayed on a page
    static int                maxRecords          = Constants.maxRecords;           // maximum number of records that can be fetched
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // Error message, no match found
    static String             limitRecordsMsg     = Constants.errorLimitRecords;    // Error message, Records found are moe than limits
    private RequestDispatcher requestDispatcher   = null;
    private String            actionURL           = "searchKodawari.act";

    /**
     * こだわり検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "非会員→search_kodawari_01_nonmember_M2.jsp<br>
     *      パラメータなし→search_kodawari_01_M2.jsp<br>
     *      こだわり条件あり→search_result_kodawari_M2.jsp"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        boolean ret;
        int equipCount;
        int[] amenityId;
        boolean memberFlag = false;
        String[] paramAmenity;
        SearchEngineBasic_M2 searchEngineBasic;
        DataEquipListResult_M2 dataEquipListResult = null;
        DataLoginInfo_M2 dataLoginInfo = null;

        try
        {
            String requestURL = new String( request.getRequestURL() );
            if ( requestURL.indexOf( "http://" ) != -1 )
            {
                response.sendRedirect( requestURL.replace( "http://", "https://" ) + (request.getQueryString() == null ? "" : "?" + request.getQueryString().replace( "&amp;", "&" )) );
                return;
            }

            // スマホからのアクセスならスマホ用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType != UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getSmartUrl( request ) );
                return;
            }

            if ( request.getParameter( "search" ) != null )
            {
                searchHotels( request, response );
                return;
            }

            dataLoginInfo = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            if ( dataLoginInfo != null )
                memberFlag = dataLoginInfo.isMemberFlag();

            amenityId = new int[0];

            paramAmenity = request.getParameterValues( "amenity" );
            if ( paramAmenity != null )
            {
                amenityId = new int[paramAmenity.length];
                for( int i = 0 ; i < paramAmenity.length ; i++ )
                {
                    if ( CheckString.numCheck( paramAmenity[i] ) != false )
                    {
                        amenityId[i] = Integer.parseInt( paramAmenity[i] );
                    }
                    else
                    {
                        amenityId[i] = 0;
                    }

                }
            }

            searchEngineBasic = new SearchEngineBasic_M2();
            ret = searchEngineBasic.getEquipList( false );

            dataEquipListResult = new DataEquipListResult_M2();

            if ( ret != false )
            {
                equipCount = searchEngineBasic.getMasterEquip().length;
                if ( equipCount > 0 )
                {
                    dataEquipListResult.setDataEquipListDto( searchEngineBasic.getMasterEquip() );
                    dataEquipListResult.setAmenityId( amenityId );
                }
                else
                    dataEquipListResult.setErrorMessage( recordsNotFoundMsg2 );
            }
            else
            {
                dataEquipListResult.setErrorMessage( recordsNotFoundMsg2 );
            }

            request.setAttribute( "EQUIP-LIST", dataEquipListResult );
            if ( memberFlag )
            {
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_kodawari_01_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_kodawari_01_M2.jsp" );
                }
            }
            else
            {
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_kodawari_01_nonmember_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_kodawari_01_nonmember_M2.jsp" );
                }
            }

            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionKodawariSearch_M2.execute(REQ,RESP) ] Exception ", e );
        }
        finally
        {
            searchEngineBasic = null;
            dataEquipListResult = null;
            dataLoginInfo = null;
        }

    }

    /**
     * ホテル検索ロジック
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    private void searchHotels(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount;
        int hotelAllCount;
        int totalPages;

        int masterEquipCount = 0;
        int pageNum = 0;
        boolean ret;

        String paramPage;
        String currentPageRecords;
        String pageLinks = "";
        String queryString = "";
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataSearchMasterEquip_M2[] arrDataSearchMasterEquip = null;
        SearchEngineBasic_M2 searchEngineBasic;
        SearchKodawari_M2 searchKodawari;

        try
        {
            paramPage = request.getParameter( "page" );

            if ( paramPage == null )
            {
                paramPage = "0";
                totalPages = 0;
            }

            if ( CheckString.numCheck( paramPage ) != false )
            {
                pageNum = Integer.parseInt( paramPage );
            }
            searchKodawari = new SearchKodawari_M2();
            searchKodawari.searchHotelList( request, pageNum, pageRecords );

            // 共通にするためデータをセットする
            arrDataSearchHotel = searchKodawari.getHotelInfo();
            hotelCount = searchKodawari.getCount();
            hotelAllCount = searchKodawari.getAllCount();
            queryString = searchKodawari.getQueryString();
            actionURL = actionURL + "?search=true&" + queryString;

            // 設備情報を取得
            searchEngineBasic = new SearchEngineBasic_M2();
            ret = searchEngineBasic.getEquipList( false );
            if ( ret != false )
            {
                masterEquipCount = searchEngineBasic.getMasterEquipCount();
                arrDataSearchMasterEquip = searchEngineBasic.getMasterEquip();
            }

            totalPages = (hotelAllCount / pageRecords) - 1;
            if ( (hotelAllCount % pageRecords) != 0 )
                totalPages = totalPages + 1;

            currentPageRecords = PagingDetails.getPageRecords( pageNum, pageRecords, hotelAllCount, hotelCount );

            if ( hotelAllCount < maxRecords )
            {
                if ( hotelAllCount == 0 )
                {
                    dataSearchResult = new DataSearchResult_M2();

                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                    dataSearchResult.setMasterEquipCount( masterEquipCount );
                    dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    if ( hotelAllCount > pageRecords )
                    {
                        pageLinks = PagingDetails.getPagenationLinkSorted( pageNum, pageRecords, totalPages, hotelAllCount, actionURL, searchKodawari.getSortParams()[0] );
                        dataSearchResult.setPageLink( pageLinks );
                    }
                    dataSearchResult.setQueryString( queryString );
                    dataSearchResult.setRecordsOnPage( currentPageRecords );
                    dataSearchResult.setHotelCount( hotelCount );
                    dataSearchResult.setDataSearchHotel( arrDataSearchHotel );
                    dataSearchResult.setMasterEquipCount( masterEquipCount );
                    dataSearchResult.setParamParameter1( searchKodawari.getSortParams()[0] );
                    dataSearchResult.setParamParameter2( searchKodawari.getSortParams()[1] );
                    dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
                }
            }
            else
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( limitRecordsMsg );
                dataSearchResult.setMasterEquipCount( masterEquipCount );
                dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );
            }

            request.setAttribute( "SEARCH-RESULT", dataSearchResult );
            if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_kodawari_M2.jsp" );
            }
            else
            {
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_kodawari_M2.jsp" );
            }
            requestDispatcher.forward( request, response );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionKodawariSearch_M2.searchHotels(REQ,RESP) ] Exception ", e );
            try
            {
                dataSearchResult = new DataSearchResult_M2();
                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( recordsNotFoundMsg2 );
                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_kodawari_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_kodawari_M2.jsp" );
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
            searchEngineBasic = null;
            searchKodawari = null;
            arrDataSearchHotel = null;
            dataSearchResult = null;
        }
    }
}
