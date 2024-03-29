package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelGps_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * GPS検索クラス（API）
 * 
 * @author S.Tashirro
 * @version 1.0 2011/04/26
 * 
 */

public class ActionApiGpsSearch extends BaseAction
{

    static int              pageRecords      = Constants.pageLimitRecords;     // 表示件数
    static int              maxRecords       = Constants.maxRecordsMobile;     // 最大件数
    static String           recordNotFound1  = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String           recordsNotFound2 = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String           recordsNotFound3 = Constants.errorRecordsNotFound3; // 最大件数を超えた場合のエラーメッセージ
    static String           recordsNotFound4 = Constants.ERROR_MSG_API5;       // 必須パラメータの不足
    public static final int DISP_MAX         = 200;

    /**
     * フリーワード検索（API）
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        int hotelCount = 0;
        int hotelAllCount = 0;
        int[] arrHotelIdList = null;
        int[] arrDistanceList = null;
        String paramLat = null;
        String paramLon = null;
        String paramZoom = null;
        String paramPage;
        String paramAndWord;
        String errorMsg = "";
        String gpsName = "";
        String paramMethod = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchHotelGps_M2 searchHotelGps = null;
        SearchHotelDao_M2 searchHotelDao = null;
        UserLoginInfo uli;

        // XML出力
        GenerateXmlAd ad = new GenerateXmlAd();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramZoom = request.getParameter( "zoom" );
            paramLon = request.getParameter( "lon" );
            paramLat = request.getParameter( "lat" );
            paramPage = request.getParameter( "page" );
            paramAndWord = request.getParameter( "andword" );
            paramMethod = request.getParameter( "method" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( (paramZoom == null) || paramZoom.equals( "" ) != false || CheckString.numCheck( paramZoom ) == false )
            {
                paramZoom = "14";
            }
            if ( paramPage == null || paramPage.equals( "" ) != false || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }
            if ( paramLon == null || paramLon.equals( "" ) != false )
            {
                paramLon = "0";
            }
            if ( paramLat == null || paramLat.equals( "" ) != false )
            {
                paramLat = "0";
            }

            if ( paramLat.equals( "0" ) == false && paramLon.equals( "0" ) == false )
            {
                searchHotelGps = new SearchHotelGps_M2();
                arrHotelIdList = searchHotelGps.getHotelIdListForGoogleMap( paramLat, paramLon, Integer.parseInt( paramZoom ) );
                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {
                    // ホテルの距離を取得
                    arrDistanceList = searchHotelGps.getHotelDistance();

                    // ホテル詳細情報の取得
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, 0, 0 );

                    // 共通にするためデータをセットする
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();
                }
                else
                {
                    hotelAllCount = 0;
                    hotelCount = 0;
                    errorMsg = recordsNotFound2;
                }
            }
            else
            {
                // 必須パラメータのエラー
                hotelAllCount = 0;
                hotelCount = 0;
                errorMsg = recordsNotFound4;
            }

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
                addHotel.addHotelInfo( arrDataSearchHotel[i], uli.isPaymemberFlag(), arrDistanceList[i] );
                // 検索結果ノードにホテルノードを追加
                searchResult.addHotel( addHotel );
            }

            // 検索結果ヘッダ作成
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( gpsName );
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
            Logging.error( "[ActionApiGpsSearch.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "GPS検索" );
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
                Logging.error( "[ActionApiGpsSearch response]Exception:" + e.toString() );
            }
        }
        finally
        {
            arrDataSearchHotel = null;
        }
    }
}
