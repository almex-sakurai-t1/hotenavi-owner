package jp.happyhotel.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.PagingDetails;
import jp.happyhotel.data.DataLoginInfo_M2;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.search.SearchHotelGps_M2;

/**
 * GPS検索クラス（携帯）
 * 
 * @author HCL Technologies Ltd.
 * @version 2.0 2008/10/06
 */

public class ActionGps_M2 extends BaseAction
{

    static int                pageRecords       = Constants.pageLimitRecordMobile;
    static String             recordsNotFound2  = Constants.errorRecordsNotFound2;
    private RequestDispatcher requestDispatcher = null;
    public static final int   dispFormat        = 1;

    /**
     * GPS検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "非会員または、登録ステータスが9より小さい→index.jsp<br>
     *      それ以外→search_result_gps_M2.jsp"
     * 
     **/
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {

        boolean memberFlag = false;
        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int carrierFlag = 1;
        int registStatus = 0;
        int scale = 0;
        int[] arrHotelIdList = null;
        String paramUidLink = null;
        String paramScale;
        String paramPage;
        String paramAndWord;
        String paramAndWordEnc;
        String currentPageRecords;
        String pageLinks = "";
        String LatPos = "";
        String LonPos = "";
        String strPos = "";
        String pageHeader = null;
        String queryString;
        SearchHotelGps_M2 searchHotelGps = null;
        SearchHotelCommon searchHotelCommon;
        SearchHotelFreeword_M2 searchHotelFreeWord;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchResult_M2 dataSearchResult = null;
        DataLoginInfo_M2 dataLoginInfo = null;
        SearchHotelDao_M2 searchHotelDao = null;

        try
        {
            paramUidLink = (String)request.getAttribute( "UID-LINK" );
            paramScale = request.getParameter( "scale" );
            paramPage = request.getParameter( "page" );
            paramAndWord = request.getParameter( "andword" );
            paramAndWordEnc = "";
            LatPos = request.getParameter( "lat" );
            LonPos = request.getParameter( "lon" );

            if ( !CheckString.numCheck( paramPage ) )
            {
                paramPage = "0";
            }
            pageNum = Integer.parseInt( paramPage );
            if ( !CheckString.numCheck( paramScale ) )
            {
                paramScale = "0";
            }
            scale = Integer.parseInt( paramScale );
            dataLoginInfo = (DataLoginInfo_M2)request.getAttribute( "LOGIN_INFO" );

            if ( dataLoginInfo != null )
            {
                memberFlag = dataLoginInfo.isMemberFlag();
                registStatus = dataLoginInfo.getRegistStatus();
                carrierFlag = dataLoginInfo.getCarrierFlag();
            }
            if ( (memberFlag != false) && (registStatus == 9) )
            {
                searchHotelGps = new SearchHotelGps_M2();
                switch( carrierFlag )
                {
                    case DataMasterUseragent.CARRIER_DOCOMO:
                        LatPos = request.getParameter( "lat" );
                        LonPos = request.getParameter( "lon" );
                        if ( (LatPos == null) || (LonPos == null) )
                        {
                            LatPos = "";
                            LonPos = "";
                        }
                        break;
                    case DataMasterUseragent.CARRIER_AU:
                        LatPos = request.getParameter( "lat" );
                        LonPos = request.getParameter( "lon" );
                        if ( (LatPos == null) || (LonPos == null) )
                        {
                            LatPos = "";
                            LonPos = "";
                        }
                        break;
                    case DataMasterUseragent.CARRIER_SOFTBANK:
                        strPos = request.getParameter( "pos" );
                        if ( (strPos != null) && (strPos.compareTo( "" ) != 0) )
                        {
                            int i = strPos.indexOf( "E" );
                            if ( i != -1 )
                            {
                                LatPos = strPos.substring( 1, i );
                                LonPos = strPos.substring( i + 1, strPos.length() );
                            }
                            else
                            {
                                i = strPos.indexOf( "W" );
                                if ( i != -1 )
                                {
                                    LatPos = strPos.substring( 1, i );
                                    LonPos = strPos.substring( i + 1, strPos.length() );
                                }
                                else
                                {
                                    LatPos = "";
                                    LonPos = "";
                                }
                            }
                        }
                        else
                        {
                            LatPos = request.getParameter( "lat" );
                            LonPos = request.getParameter( "lon" );
                        }
                        break;
                }
                if ( (LatPos.compareTo( "" ) != 0) && (LonPos.compareTo( "" ) != 0) )
                {
                    arrHotelIdList = searchHotelGps.getHotelIdList( LatPos, LonPos, scale );
                }
                queryString = "searchGps.act?lat=" + LatPos + "&lon=" + LonPos + "&scale=" + paramScale;
                // ホテルIDリストをセット
                searchHotelCommon = new SearchHotelCommon();
                searchHotelCommon.setEquipHotelList( arrHotelIdList );

                // 絞り込みフリーワードがあり、ホテルIDリストが0件以上の場合
                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {
                    if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                    {
                        try
                        {
                            paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );
                            paramAndWordEnc = URLEncoder.encode( paramAndWord, "Shift_JIS" );

                            searchHotelFreeWord = new SearchHotelFreeword_M2();
                            // 絞り込みフリーワードで検索したホテルIDリストをセット
                            arrHotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord );
                            // ホテルIDリストをセット
                            searchHotelCommon.setResultHotelList( arrHotelIdList );
                            arrHotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );
                            pageHeader = "「" + paramAndWord + "」で絞込み検索しました";
                            queryString = "searchGps.act?lat=" + LatPos + "&lon=" + LonPos + "&scale=" + paramScale + "&andword=" + paramAndWordEnc;

                        }
                        catch ( UnsupportedEncodingException e )
                        {
                            Logging.error( "[ActionGpsSearchMobile.execute() : AndWord = " + paramAndWord + " ] Exception=" + e.toString() );
                            throw e;
                        }
                    }
                }
                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {
                    // ホテル詳細を取得
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

                }
                else
                {
                    dataSearchResult = new DataSearchResult_M2();
                    dataSearchResult.setErrorOccurring( true );
                    dataSearchResult.setErrorMessage( recordsNotFound2 );
                    dataSearchResult.setHotelCount( 0 );
                    dataSearchResult.setHotelAllCount( 0 );
                }
                dataSearchResult.setParamParameter1( LatPos );
                dataSearchResult.setParamParameter2( LonPos );
                dataSearchResult.setParamParameter3( paramScale );
                dataSearchResult.setParamParameter4( paramAndWord );

                request.setAttribute( "SEARCH-RESULT", dataSearchResult );
                requestDispatcher = request.getRequestDispatcher( "search_result_gps_M2.jsp" );
                requestDispatcher.forward( request, response );
            }
            else
            {
                response.sendRedirect( "index.jsp?" + paramUidLink );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Logging.error( "[ActionGpsSearch_M2.execute() ] Exception=" + e.toString(), e );
            try
            {
                response.sendRedirect( "../../index.jsp" );
            }
            catch ( Exception exp )
            {
                Logging.error( "unable to dispatch for GPS.....=" + exp.toString(), exp );
            }
        }
        finally
        {
            searchHotelGps = null;
            searchHotelFreeWord = null;
            searchHotelDao = null;
        }
    }
}
