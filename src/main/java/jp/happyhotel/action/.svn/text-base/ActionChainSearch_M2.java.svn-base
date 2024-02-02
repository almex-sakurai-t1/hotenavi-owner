package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.data.DataChainResult_M2;
import jp.happyhotel.data.DataMasterChain_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchMasterEquip_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelChain_M2;
import jp.happyhotel.search.SearchHotelDao_M2;

/**
 * チェーン店検索クラス
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/18
 * 
 */

public class ActionChainSearch_M2 extends BaseAction
{
    static int                pageRecords       = Constants.pageLimitRecords;     // 1ページに表示する最大件数
    static String             recordNotFound2   = Constants.errorRecordsNotFound2; // エラーメッセージ
    private RequestDispatcher requestDispatcher = null;

    /**
     * チェーン店検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→search_chain_01_M2.jsp<br>
     *      チェーンIDあり→search_result_chain_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount; // 1ページに表示するホテル件数
        int hotelAllCount; // 検索結果のホテル総数
        int pageNum; // ページ番号
        int masterEquipCount = 0;
        int[] arrHotelIdList = null;
        String paramPage;
        String queryString = "";
        String actionURL = "searchChain.act?";
        String currentPageRecords;
        String pageLinks = "";
        String paramGroupId = null;
        String paramPrefId = null;
        DataMasterChain_M2 dataMasterChainObj = null;
        DataChainResult_M2 dataChainResult = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataMasterChain_M2[] arrDataMasterChain = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchMasterEquip_M2[] arrDataSearchMasterEquip = null;
        SearchHotelChain_M2 searchHotelChain = null;
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
            paramGroupId = request.getParameter( "group_id" );
            paramPrefId = request.getParameter( "pref_id" );
            paramPage = request.getParameter( "page" );

            if ( CheckString.isvalidString( paramGroupId ) )
            {
                dataSearchResult = new DataSearchResult_M2();

                if ( !CheckString.numCheck( paramPage ) )
                {
                    paramPage = "0";
                }
                if ( CheckString.numCheck( paramGroupId ) && CheckString.numCheck( paramPage ) )
                {
                    searchHotelChain = new SearchHotelChain_M2();
                    pageNum = Integer.parseInt( paramPage );

                    // 設備情報の取得
                    boolean isEquipListFound = false;
                    searchEngineBasic = new SearchEngineBasic_M2();
                    isEquipListFound = searchEngineBasic.getEquipList( false );
                    if ( isEquipListFound )
                    {
                        masterEquipCount = searchEngineBasic.getMasterEquipCount();
                        arrDataSearchMasterEquip = searchEngineBasic.getMasterEquip();
                    }
                    // チェーン店データの取得
                    dataMasterChainObj = searchHotelChain.getMasterChainData( paramGroupId );

                    dataSearchResult.setGroupName( dataMasterChainObj.getName() );
                    if ( paramPrefId != null )
                    {
                        if ( CheckString.numCheck( paramPrefId ) )
                        {
                            // ホテルIDのリスト取得
                            arrHotelIdList = searchHotelChain.getHotelBasicIdListByPref( paramGroupId, paramPrefId );
                        }
                    }
                    else
                    {
                        // ホテルIDのリスト取得
                        arrHotelIdList = searchHotelChain.getHotelBasicIdList( paramGroupId );
                    }
                    // ホテル詳細情報
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );
                    // 共通にするためデータをセットする
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();

                    // 表示するデータをセット
                    currentPageRecords = PagingDetails.getPageRecords( pageNum, pageRecords, hotelAllCount, hotelCount );
                    queryString = "group_id=" + paramGroupId;
                    actionURL = actionURL + queryString;

                    if ( hotelAllCount == 0 )
                    {

                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordNotFound2 );

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

                    }
                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordNotFound2 );

                }

                dataSearchResult.setParamParameter1( paramGroupId );
                dataSearchResult.setMasterEquipCount( masterEquipCount );
                dataSearchResult.setDataSearchMasterEquip( arrDataSearchMasterEquip );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_chain_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_chain_M2.jsp" );
                }
                requestDispatcher.forward( request, response );
                return;
            }
            else
            {
                boolean hasHotels;
                int groupCount = 0;
                dataChainResult = new DataChainResult_M2();
                searchHotelChain = new SearchHotelChain_M2();

                // チェーン店のリストを取得
                hasHotels = searchHotelChain.getChainList();
                // チェーン店の件数を取得
                groupCount = searchHotelChain.getGroupCount();
                if ( hasHotels )
                {
                    arrDataMasterChain = new DataMasterChain_M2[groupCount];
                    for( int i = 0 ; i < groupCount ; i++ )
                    {
                        arrDataMasterChain[i] = new DataMasterChain_M2();
                        arrDataMasterChain[i].setGroupId( searchHotelChain.getGroupIdList()[i] );
                        arrDataMasterChain[i].setName( searchHotelChain.getGroupNameList()[i] );
                        arrDataMasterChain[i].setGroupCountList( searchHotelChain.getGroupCountList()[i] );
                    }
                    dataChainResult.setPageRecords( pageRecords );
                    dataChainResult.setChainData( arrDataMasterChain );
                    dataChainResult.setErrorMessage( recordNotFound2 );

                }
                else
                {
                    dataChainResult.setErrorMessage( recordNotFound2 );
                }

                request.setAttribute( "CHAIN_RESULT", dataChainResult );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_chain_01_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_chain_01_M2.jsp" );
                }
                requestDispatcher.forward( request, response );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionChainSearch_M2.execute() ] Exception = " + e.toString(), e );
            try
            {
                dataSearchResult = new DataSearchResult_M2();

                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( recordNotFound2 );
                dataSearchResult.setParamParameter1( paramGroupId );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                if ( request.getRequestURL().indexOf( "_debug_" ) != -1 )
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/_debug_/search/search_result_chain_M2.jsp" );
                }
                else
                {
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/search/search_result_chain_M2.jsp" );
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
            arrDataSearchHotel = null;
            dataMasterChainObj = null;
            arrDataMasterChain = null;
            arrDataSearchMasterEquip = null;
            dataSearchResult = null;
            dataChainResult = null;
            searchHotelChain = null;
            searchEngineBasic = null;
            searchHotelDao = null;
        }
    }
}
