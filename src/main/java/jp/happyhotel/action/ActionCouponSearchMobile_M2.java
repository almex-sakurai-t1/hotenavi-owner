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
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterCoupon_M2;
import jp.happyhotel.data.DataSearchCoupon_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelCoupon_M2;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelGps_M2;

/**
 * クーポン検索クラス（携帯）
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/09/29
 * 
 */

public class ActionCouponSearchMobile_M2 extends BaseAction
{

    static int                pageRecords       = Constants.pageLimitRecordMobile; // 表示件数
    static int                maxRecords        = Constants.maxRecordsMobile;     // 最大件数
    static String             recordNotFound1   = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String             recordsNotFound2  = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String             recordsNotFound3  = Constants.errorRecordsNotFound3; // 最大件数を超えた場合のエラーメッセージ
    public static final int   dispFormat        = 2;

    private RequestDispatcher requestDispatcher = null;

    /**
     * クーポン検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      地方ID→search_coupon_pref_M2.jsp<br>
     *      地方ID、都道府県ID→search_result_coupon_M2.jsp<br>
     *      地方ID、GPSパラメータ→search_result_coupon_M2.jsp"
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        int registStatus = 0;
        int delFlag = 0;
        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int nType;
        int findCount;
        int[] arrHotelIdList = null;
        String paramPrefId = null;
        String paramLocalId = null;
        String paramPage;
        String paramLatPos = null;
        String paramLonPos = null;
        String paramScale = null;
        String queryString = "";
        String currentPageRecords;
        String pageLinks = "";
        String paramUidLink = null;
        String pageHeader = null;
        SearchHotelCoupon_M2 searchHotelCoupon;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataSearchCoupon_M2 dataSearchCouponDTO = null;
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelGps_M2 searchHotelGps = null;
        DataMasterCoupon_M2[] arrDataMasterCoupon = null;
        SearchHotelDao_M2 searchHotelDao = null;
        DataLoginInfo_M2 dataLoginInfo = null;
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
            paramPage = request.getParameter( "page" );
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramLatPos = request.getParameter( "lat" );
            paramLonPos = request.getParameter( "lon" );
            paramScale = request.getParameter( "scale" );

            // PCからのアクセスならPC用のページに飛ばす
            int userAgentType = UserAgent.getUserAgentType( request );
            if ( userAgentType == UserAgent.USERAGENT_PC )
            {
                response.sendRedirect( request.getContextPath() + Url.getPCUrl( request ) );
                return;
            }

            // パラメータがない場合はindex_M2に
            if ( paramPrefId == null && paramLocalId == null && paramLatPos == null && paramLonPos == null )
            {
                requestDispatcher = request.getRequestDispatcher( "index_M2.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }

            if ( !CheckString.numCheck( paramScale ) )
            {
                paramScale = "0";
            }
            searchHotelCoupon = new SearchHotelCoupon_M2();

            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
            }
            pageNum = Integer.parseInt( paramPage );
            dataLoginInfo = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            if ( dataLoginInfo != null )
            {
                delFlag = dataLoginInfo.getDelFlag();
                registStatus = dataLoginInfo.getRegistStatus();
            }

            if ( registStatus == 9 && delFlag == 0 )
            {
                nType = 1;
            }
            else
            {
                nType = 2;
            }

            findCount = 0;
            if ( paramLocalId == null )
            {
                if ( paramPrefId != null )
                {
                    if ( CheckString.numCheck( paramPrefId ) != false )
                    {
                        arrHotelIdList = searchHotelCoupon.getHotelIdListByPref( Integer.parseInt( paramPrefId ), nType, true );

                        searchHotelCommon = new SearchHotelCommon();
                        searchHotelCommon.setResultHotelList( arrHotelIdList );

                        queryString = "searchCouponMobile.act?pref_id=" + paramPrefId;

                        findCount++;

                    }
                }
                // GPS
                if ( paramLatPos != null && paramLonPos != null && paramLocalId == null )
                {
                    if ( (paramLatPos.compareTo( "" ) != 0) && (paramLonPos.compareTo( "" ) != 0) )
                    {
                        searchHotelGps = new SearchHotelGps_M2();

                        arrHotelIdList = searchHotelGps.getHotelIdList( paramLatPos, paramLonPos, Integer.parseInt( paramScale ) );

                        if ( findCount != 0 )
                        {
                            searchHotelCommon.setEquipHotelList( arrHotelIdList );

                            arrHotelIdList = searchHotelCommon.getMargeHotel( 0, 0, false );
                        }
                        else
                        {
                            if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                            {
                                arrHotelIdList = searchHotelCoupon.getHotelIdListGps( arrHotelIdList, nType, true );
                            }

                        }

                        queryString = "searchCouponMobile.act?lat=" + paramLatPos + "&lon=" + paramLonPos + "&scale=" + paramScale;
                    }

                }
                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {
                    if ( arrHotelIdList.length > 0 && arrHotelIdList.length <= 200 )
                    {
                        // ホテル詳細情報の取得
                        searchHotelDao = new SearchHotelDao_M2();
                        searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                        arrDataSearchHotel = searchHotelDao.getHotelInfo();
                        hotelCount = searchHotelDao.getCount();
                        hotelAllCount = searchHotelDao.getAllCount();

                        currentPageRecords = PagingDetails.getPageRecordsMobile( pageNum, pageRecords, hotelAllCount, hotelCount, dispFormat );

                        dataSearchResult = new DataSearchResult_M2();

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
                        dataSearchResult.setParamParameter1( paramLatPos );
                        dataSearchResult.setParamParameter2( paramLonPos );
                        dataSearchResult.setParamParameter3( paramScale );
                        dataSearchResult.setParamParameter4( queryString );
                        dataSearchResult.setParamParameter5( paramPage );
                        dataSearchResult.setParamParameter6( paramPrefId );

                    }
                    else if ( arrHotelIdList.length > 200 )
                    {
                        dataSearchResult = new DataSearchResult_M2();
                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordsNotFound3 );
                        dataSearchResult.setParamParameter1( paramLatPos );
                        dataSearchResult.setParamParameter2( paramLonPos );
                        dataSearchResult.setParamParameter3( paramScale );
                        dataSearchResult.setParamParameter4( queryString );
                        dataSearchResult.setParamParameter5( paramPage );
                        dataSearchResult.setParamParameter6( paramPrefId );
                    }
                    else if ( arrHotelIdList.length == 0 )
                    {
                        dataSearchResult = new DataSearchResult_M2();
                        dataSearchResult.setErrorOccurring( true );
                        dataSearchResult.setErrorMessage( recordsNotFound2 );
                        dataSearchResult.setParamParameter1( paramLatPos );
                        dataSearchResult.setParamParameter2( paramLonPos );
                        dataSearchResult.setParamParameter3( paramScale );
                        dataSearchResult.setParamParameter4( queryString );
                        dataSearchResult.setParamParameter5( paramPage );
                        dataSearchResult.setParamParameter6( paramPrefId );
                    }
                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordsNotFound2 );
                    dataSearchResult.setParamParameter1( paramLatPos );
                    dataSearchResult.setParamParameter2( paramLonPos );
                    dataSearchResult.setParamParameter3( paramScale );
                    dataSearchResult.setParamParameter4( queryString );
                    dataSearchResult.setParamParameter5( paramPage );
                    dataSearchResult.setParamParameter6( paramPrefId );
                }

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "search_result_coupon_M2.jsp" );
                requestDispatcher.forward( request, response );

            }
            else if ( paramLocalId != null && paramPrefId == null )
            {

                if ( paramLocalId.compareTo( "null" ) != 0 && CheckString.numCheck( paramLocalId ) != false )
                {

                    arrHotelIdList = searchHotelCoupon.getHotelIdList( paramLocalId, nType, true );

                    arrDataMasterCoupon = searchHotelCoupon.getHotelListByPref( arrHotelIdList, Integer.parseInt( paramLocalId ) );

                    int prefcount = searchHotelCoupon.getPrefCount();

                    dataSearchCouponDTO = new DataSearchCoupon_M2();

                    dataSearchCouponDTO.setDataMasterCoupon( arrDataMasterCoupon );
                    dataSearchCouponDTO.setPrefCount( prefcount );

                    request.setAttribute( "SEARCH-RESULT", dataSearchCouponDTO );
                    requestDispatcher = request.getRequestDispatcher( "search_coupon_pref_M2.jsp" );
                    requestDispatcher.forward( request, response );
                }
            }
            else
            {
                requestDispatcher = request.getRequestDispatcher( "index_M2.jsp" );
                requestDispatcher.forward( request, response );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionCouponSearchMobile.execute() ] Exception", exception );
            try
            {
                response.sendRedirect( "../../index.jsp" );
            }
            catch ( Exception subException )
            {
                Logging.error( "[ActionCouponSearchMobile.execute() ] Exception - Unable to dispatch....."
                        + subException.toString() );
            }
        }
        finally
        {
            searchHotelCoupon = null;
            arrDataSearchHotel = null;
            dataSearchResult = null;
            dataSearchCouponDTO = null;
            searchHotelDao = null;
        }
    }

}
