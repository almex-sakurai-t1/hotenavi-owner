package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelCoupon_M2;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * クーポン検索クラス（API）
 * 
 * @author S.Tashirro
 * @version 1.0 2011/04/08
 * 
 */

public class ActionApiCouponSearch extends BaseAction
{

    static int                pageRecords       = Constants.pageLimitRecords;     // 表示件数
    static int                maxRecords        = Constants.maxRecordsMobile;     // 最大件数
    static String             recordNotFound1   = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String             recordsNotFound2  = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String             recordsNotFound3  = Constants.errorRecordsNotFound3; // 最大件数を超えた場合のエラーメッセージ
    public static final int   dispFormat        = 2;

    private RequestDispatcher requestDispatcher = null;

    /**
     * クーポン検索（API）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "地方ID、都道府県ID→search_result_coupon_M2.jsp<br>
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 1;
        final int DISP_FLAG = 2;
        int hotelCount = 0;
        int hotelAllCount = 0;
        int pageNum = 0;
        int nType;
        int findCount;
        int[] arrHotelIdList = null;
        String paramPrefId = null;
        String paramLocalId = null;
        String paramPage;
        String errorMsg = "";
        String paramMethod = "";
        String prefName = "";
        String paramAndWord = "";
        SearchHotelCoupon_M2 searchHotelCoupon;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelDao_M2 searchHotelDao = null;
        UserLoginInfo uli;
        DataMasterPref dmp;

        // XML出力
        boolean ret = false;
        SponsorData_M2 sd;
        sd = new SponsorData_M2();
        GenerateXmlAd ad = new GenerateXmlAd();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramPrefId = request.getParameter( "pref_id" );
            paramLocalId = request.getParameter( "local_id" );
            paramPage = request.getParameter( "page" );
            paramMethod = request.getParameter( "method" );

            // パラメータがない場合はindex_M2に
            if ( paramPrefId == null && paramLocalId == null )
            {
                paramPrefId = "0";
            }

            searchHotelCoupon = new SearchHotelCoupon_M2();

            if ( paramPage == null || paramPage.equals( "" ) != false || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }
            if ( paramLocalId == null || paramLocalId.equals( "" ) != false || CheckString.numCheck( paramLocalId ) == false )
            {
                paramLocalId = "0";
            }
            if ( paramPrefId == null || paramPrefId.equals( "" ) != false || CheckString.numCheck( paramPrefId ) == false )
            {
                paramPrefId = "0";
            }
            pageNum = Integer.parseInt( paramPage );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            // 会員だったら会員向けのクーポンを作成する
            if ( uli.isMemberFlag() != false )
            {
                nType = 1;
            }
            else
            {
                nType = 2;
            }

            findCount = 0;
            if ( paramPrefId != null )
            {
                if ( CheckString.numCheck( paramPrefId ) != false )
                {
                    arrHotelIdList = searchHotelCoupon.getHotelIdListByPref( Integer.parseInt( paramPrefId ), nType, true );
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setResultHotelList( arrHotelIdList );
                    findCount++;
                    dmp = new DataMasterPref();
                    dmp.getData( Integer.parseInt( paramPrefId ) );
                    prefName = dmp.getName();
                }
            }
            if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
            {
                if ( arrHotelIdList.length > 0 )
                {
                    // ホテル詳細情報の取得
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();

                    if ( arrHotelIdList.length > 200 )
                    {
                        hotelAllCount = 200;
                        // errorMsg = recordsNotFound3;
                    }
                }
                else if ( arrHotelIdList.length == 0 )
                {
                    hotelCount = 0;
                    hotelAllCount = 0;
                    errorMsg = recordsNotFound2;
                }
            }
            else
            {
                hotelCount = 0;
                hotelAllCount = 0;
                errorMsg = recordsNotFound2;
            }

            // ret = sd.getAdData( Integer.parseInt( paramPrefId ), DISP_COUNT, DISP_FLAG );
            // if ( ret != false )
            // {
            // sd.setImpressionCountForSmart( sd.getSponsor()[0].getSponsorCode() );
            // // 広告用のXMLを追加
            // ad.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            // ad.setAdInfo( sd.getSponsor()[0] );
            // }

            // 検索結果作成
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( errorMsg );
            searchResult.setResultCount( hotelAllCount );
            // 広告をセット
            searchResult.setAd( ad );

            // ホテルの情報をセット
            for( int i = 0 ; i < hotelCount ; i++ )
            {
                GenerateXmlSearchResultHotel addHotel = new GenerateXmlSearchResultHotel();
                // ホテル情報をセット
                addHotel.addHotelInfo( arrDataSearchHotel[i], uli.isPaymemberFlag(), 0 );
                // 検索結果ノードにホテルノードを追加
                searchResult.addHotel( addHotel );
            }

            // 検索結果ヘッダ作成
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( prefName );
            searchHeader.setAndword( paramAndWord );
            searchHeader.setCount( hotelCount );
            // 検索結果ノードを検索結果ヘッダーノードに追加
            searchHeader.setSearchResult( searchResult );

            String xmlOut = searchHeader.createXml();
            ServletOutputStream out = null;

            out = response.getOutputStream();
            response.setContentType( "text/xml; charset=UTF-8" );
            out.write( xmlOut.getBytes( "UTF-8" ) );

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiCouponSearch.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "クーポン検索" );
            searchHeader.setCount( 0 );
            // ホテル詳細を追加
            searchHeader.setSearchResult( searchResult );

            String xmlOut = searchHeader.createXml();
            ServletOutputStream out = null;

            try
            {
                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiCouponSearch response]Exception:" + e.toString() );
            }
        }
        finally
        {
            searchHotelCoupon = null;
            arrDataSearchHotel = null;
            searchHotelDao = null;
        }
    }
}
