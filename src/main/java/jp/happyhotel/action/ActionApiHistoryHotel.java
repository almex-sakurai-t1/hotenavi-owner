package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * 最近見たホテル検索クラス（API）
 * 
 * @author S.Tashirro
 * @version 1.0 2011/04/26
 * 
 */

public class ActionApiHistoryHotel extends BaseAction
{

    static int                pageRecords       = Constants.pageLimitRecordMobile; // 表示件数
    static int                maxRecords        = Constants.maxRecordsMobile;     // 最大件数
    static String             recordNotFound1   = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String             recordsNotFound2  = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String             recordsNotFound3  = Constants.errorRecordsNotFound3; // 最大件数を超えた場合のエラーメッセージ
    static String             recordsNotFound4  = Constants.ERROR_MSG_API5;       // 必須パラメータの不足
    public static final int   DISP_MAX          = 200;

    private RequestDispatcher requestDispatcher = null;

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
        String[] paramIdList = null;
        String errorMsg = "";
        String paramMethod = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchHotelDao_M2 searchHotelDao = null;
        UserLoginInfo uli;

        // XML出力
        boolean ret = false;
        GenerateXmlAd ad = new GenerateXmlAd();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramMethod = request.getParameter( "method" );
            paramIdList = request.getParameterValues( "hotel_id" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( paramIdList != null )
            {
                arrHotelIdList = new int[paramIdList.length];
                for( int i = 0 ; i < paramIdList.length ; i++ )
                {
                    arrHotelIdList[i] = Integer.parseInt( paramIdList[i] );
                }

                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {

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
                addHotel.addHotelInfo( arrDataSearchHotel[i], uli.isPaymemberFlag(), 0 );
                // 検索結果ノードにホテルノードを追加
                searchResult.addHotel( addHotel );
            }

            // 検索結果ヘッダ作成
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "最近見たホテル" );
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
            Logging.error( "[ActionApiHystoryHotel.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "最近見たホテル" );
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
                Logging.error( "[ActionApiHystoryHotel response]Exception:" + e.toString() );
            }
        }
        finally
        {
            arrDataSearchHotel = null;
        }
    }
}
