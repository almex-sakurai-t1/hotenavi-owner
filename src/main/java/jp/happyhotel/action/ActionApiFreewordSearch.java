package jp.happyhotel.action;

import java.net.URLDecoder;

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
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * フリーワード検索クラス（API）
 * 
 * @author S.Tashirro
 * @version 1.0 2011/04/21
 * 
 */

public class ActionApiFreewordSearch extends BaseAction
{

    static int              pageRecords      = Constants.pageLimitRecords;     // 表示件数
    static int              maxRecords       = Constants.maxRecordsMobile;     // 最大件数
    static String           recordNotFound1  = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String           recordsNotFound2 = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String           recordsNotFound3 = Constants.errorRecordsNotFound3; // 最大件数を超えた場合のエラーメッセージ
    public static final int DISP_MAX         = 200;

    /**
     * フリーワード検索（API）
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 1;
        final int DISP_FLAG = 2;
        int hotelCount = 0;
        int hotelAllCount = 0;
        int pageNum = 0;
        int[] arrHotelIdList = null;
        String freewordName = "";
        String paramPrefId = null;
        String paramLocalId = null;
        String paramFreeword = null;
        String paramPage;
        String paramAndWord;
        String errorMsg = "";
        String paramMethod = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchHotelFreeword_M2 searchHotelFreeWord = null;
        SearchHotelDao_M2 searchHotelDao = null;
        UserLoginInfo uli;

        // XML出力
        boolean ret = false;
        SponsorData_M2 sd;
        sd = new SponsorData_M2();
        GenerateXmlAd ad = new GenerateXmlAd();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramLocalId = request.getParameter( "local_id" );
            paramPrefId = request.getParameter( "pref_id" );
            paramPage = request.getParameter( "page" );
            paramAndWord = request.getParameter( "andword" );
            paramMethod = request.getParameter( "method" );
            paramFreeword = request.getParameter( "freeword" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( paramFreeword == null )
            {
                paramFreeword = "";
            }
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

            // フリーワードが空白以外だったら調べる
            if ( paramFreeword.equals( "" ) == false )
            {
                searchHotelFreeWord = new SearchHotelFreeword_M2();
                try
                {
                    paramFreeword = new String( URLDecoder.decode( paramFreeword, "8859_1" ).getBytes( "8859_1" ), "utf-8" );
                }
                catch ( Exception e )
                {
                    Logging.error( "[ActionApiFreeWordSearch.execute() ] Exception=" + e.toString() );
                }

                if ( Integer.parseInt( paramPrefId ) > 0 )
                {
                    arrHotelIdList = searchHotelFreeWord.getSearchIdList( paramFreeword, paramPrefId );
                }
                else
                {
                    arrHotelIdList = searchHotelFreeWord.getSearchIdList( paramFreeword );
                }
                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {
                    // ホテル詳細情報の取得
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, pageRecords, Integer.parseInt( paramPage ) );

                    // 共通にするためデータをセットする
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();

                    if ( arrHotelIdList != null && arrHotelIdList.length > 200 )
                    {
                        // 共通にするためデータをセットする
                        hotelAllCount = DISP_MAX;
                        // errorMsg = recordsNotFound3;
                    }
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
                hotelAllCount = 0;
                hotelCount = 0;
                errorMsg = recordsNotFound2;
            }

            /** 検索結果をセット **/
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
            searchHeader.setName( freewordName );
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
            Logging.error( "[ActionApiFreewordSearch.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "フリーワード検索" );
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
                Logging.error( "[ActionApiFreewordSearch response]Exception:" + e.toString() );
            }
        }
        finally
        {
            arrDataSearchHotel = null;
        }
    }
}
