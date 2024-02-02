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
import jp.happyhotel.data.DataChainResult_M2;
import jp.happyhotel.data.DataMasterChain_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchHotelChain_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;

/**
 * 
 * チェーン店検索クラス（携帯）
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/30
 */

public class ActionChainSearchMobile_M2 extends BaseAction
{
    static int                pageRecords       = Constants.pageLimitRecordMobile; // 1ページに表示する最大件数
    private RequestDispatcher requestDispatcher = null;
    static String             recordNotFound2   = Constants.errorRecordsNotFound2;

    /**
     * チェーン店検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      チェーン店ID→chain_result_M2.jsp"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount; // 1ページに表示するホテル件数
        int hotelAllCount; // 検索結果のホテル総数
        int pageNum = 0; // ページ番号
        int hotelIdList[] = null;
        int dispFormat = 1;
        String paramGroupId = null;
        String paramPrefId = null;
        String paramAndWord = null;
        String paramUidLink = null;
        String paramPage = null;
        String pageHeader = null;
        String queryString = null;
        String currentPageRecords = null;
        String pageLinks = "";
        DataChainResult_M2 dataChainResult = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataMasterChain_M2 dataMasterChain = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataMasterChain_M2[] arrDataMasterChain = null;
        SearchHotelChain_M2 searchHotelChain = null;
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
            paramGroupId = request.getParameter( "group_id" );
            paramPrefId = request.getParameter( "pref_id" );
            paramAndWord = request.getParameter( "andword" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );

            // PCからのアクセスならPC用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getPCUrl( request ) );
                return;
            }

            if ( paramGroupId != null )
            {
                paramPage = request.getParameter( "page" );

                if ( CheckString.isvalidString( paramGroupId ) )
                {
                    if ( !CheckString.numCheck( paramPage ) )
                    {
                        paramPage = "0";
                    }
                    pageNum = Integer.parseInt( paramPage );

                    searchHotelChain = new SearchHotelChain_M2();
                    dataSearchResult = new DataSearchResult_M2();
                    // チェーン店データ取得
                    dataMasterChain = searchHotelChain.getMasterChainData( paramGroupId );
                    dataSearchResult.setGroupName( dataMasterChain.getName() );

                    if ( paramPrefId != null )
                    {
                        if ( CheckString.numCheck( paramPrefId ) )
                        {

                            // 都道府県、チェーン店IDからホテルIDリストを取得
                            hotelIdList = searchHotelChain.getHotelBasicIdListByPref( paramGroupId, paramPrefId );

                        }
                    }
                    else
                    {

                        // チェーン店IDからホテルIDリストを取得
                        hotelIdList = searchHotelChain.getHotelBasicIdList( paramGroupId );

                    }

                    queryString = "searchChainMobile.act?group_id=" + paramGroupId;

                    if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                    {

                        // ホテルIDリストをセット
                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setEquipHotelList( hotelIdList );
                        try
                        {
                            paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );
                            searchHotelFreeWord = new SearchHotelFreeword_M2();
                            // フリーワードからホテルIDリストを取得
                            hotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord, paramPrefId );

                            searchHotelCommon.setResultHotelList( hotelIdList );
                            // ホテルIDリストと、フリーワードから取得したホテルIDリストをマージ
                            hotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );

                            // 表示名称
                            if ( paramPrefId != null )
                            {
                                pageHeader = "「" + paramAndWord + "」を検索しました";
                            }
                            else
                            {
                                pageHeader = "「" + paramAndWord + "」で絞込み検索しました";
                            }

                            // ページリンク
                            queryString = "searchChainMobile.act?group_id=" + paramGroupId + "&andword=" + paramAndWord;
                        }
                        catch ( UnsupportedEncodingException e )
                        {
                            Logging.error( "[ActionChainSearchMobile.execute() ] UnsupportedEncodingException =" + e.toString() );
                            throw e;
                        }
                    }

                    // ホテル詳細情報をセット
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );

                    // 共通にするためデータをセットする
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();

                    // 表示するデータをセット
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
                    dataSearchResult.setParamParameter1( paramGroupId );
                    dataSearchResult.setParamParameter2( paramPage );
                    dataSearchResult.setParamParameter3( paramAndWord );
                    dataSearchResult.setParamParameter4( paramPrefId );

                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordNotFound2 );
                }

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "chain_result_M2.jsp" );
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

                if ( hasHotels && (groupCount > 0) )
                {
                    arrDataMasterChain = new DataMasterChain_M2[groupCount];
                    for( int i = 0 ; i < groupCount ; i++ )
                    {
                        arrDataMasterChain[i] = new DataMasterChain_M2();
                        arrDataMasterChain[i].setGroupId( searchHotelChain.getGroupIdList()[i] );
                        arrDataMasterChain[i].setName( searchHotelChain.getGroupNameList()[i] );
                        arrDataMasterChain[i].setNameKana( searchHotelChain.getGroupNameKanaList()[i] );
                    }

                    dataChainResult.setChainData( arrDataMasterChain );

                }
                else
                {
                    dataChainResult.setErrorMessage( recordNotFound2 );
                    requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "index_M2.jsp" );
                    requestDispatcher.forward( request, response );
                    return;

                }

                request.setAttribute( "CHAIN-MOBILE-RESULT-GROUPLIST", dataChainResult );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "index_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionChainSearchMobile.execute() ] Exception=" + e.toString(), e );
            try
            {
                dataSearchResult = new DataSearchResult_M2();

                dataSearchResult.setErrorOccurring( true );
                dataSearchResult.setErrorMessage( recordNotFound2 );
                dataSearchResult.setParamParameter1( paramGroupId );
                dataSearchResult.setParamParameter2( paramPage );
                dataSearchResult.setParamParameter3( paramAndWord );
                dataSearchResult.setParamParameter4( paramPrefId );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "chain_result_M2.jsp" );
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
            dataMasterChain = null;
            arrDataMasterChain = null;
            dataChainResult = null;
            dataSearchResult = null;
            searchHotelChain = null;
            searchHotelCommon = null;
            searchHotelFreeWord = null;
            searchHotelDao = null;
        }

    } // End of Execute function

}// End of Class

