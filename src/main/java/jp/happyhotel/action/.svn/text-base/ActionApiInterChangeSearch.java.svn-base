package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapConvert;
import jp.happyhotel.data.DataMapPoint;
import jp.happyhotel.data.DataMapPoint_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelIc_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * IC検索（API）
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/07
 */

public class ActionApiInterChangeSearch extends BaseAction
{
    static int                pageRecords         = Constants.pageLimitRecords;     // 1ページで表示する件数
    static String             recordsNotFoundMsg2 = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    private RequestDispatcher requestDispatcher   = null;

    /**
     * IC検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     */

    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 5;
        final int DISP_FLAG = 2;
        int i = 0;
        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int hotelIdList[] = null;
        String paramLocalId = null;
        String paramPrefId = null;
        String paramIcId = null;
        String paramAndWord = null;
        String paramPage = null;
        String errorMsg = "";
        String paramMethod = null;
        String icName = "";
        DataMapPoint_M2 dMapPoint = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchHotelIc_M2 searchHotelIC = null;
        SearchHotelCommon searchHotelCommon = null;
        SearchHotelDao_M2 searchHotelDao = null;
        UserLoginInfo uli;

        // XML出力
        boolean ret = false;
        SponsorData_M2 sd;
        sd = new SponsorData_M2();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramLocalId = request.getParameter( "local_id" );
            paramPrefId = request.getParameter( "pref_id" );
            paramIcId = request.getParameter( "ic_id" );
            paramAndWord = request.getParameter( "andword" );
            paramPage = request.getParameter( "page" );
            paramMethod = request.getParameter( "method" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
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

            // CASE 1 :インターチェンジIDで要求
            if ( CheckString.isvalidString( paramIcId ) )
            {
                // 昭文社の地図コードだった場合はゼンリンのコードに変換（#15057対応）
                if ( paramIcId.matches( "(\\d+@\\d+,?)+" ) )
                {
                    DataMapConvert converter = new DataMapConvert();
                    if ( converter.getData( paramIcId ) )
                    {
                        paramIcId = converter.getConvertId();
                    }
                }

                pageNum = Integer.parseInt( paramPage );
                searchHotelIC = new SearchHotelIc_M2();

                // ルートIDでホテル情報を取得
                hotelIdList = searchHotelIC.getSearchIdList( paramIcId );

                if ( paramAndWord != null && (paramAndWord.trim().length() > 0) )
                {
                    // 現在のホテルIDリストをセット
                    searchHotelCommon = new SearchHotelCommon();
                    searchHotelCommon.setEquipHotelList( hotelIdList );
                    try
                    {
                        // paramAndWord = new String( paramAndWord.getBytes( "8859_1" ), "Windows-31J" );
                        // searchHotelFreeWord = new SearchHotelFreeword_M2();
                        // // 絞り込みフリーワードで検索
                        // hotelIdList = searchHotelFreeWord.getSearchIdList( paramAndWord, paramPrefId );
                        // searchHotelCommon.setResultHotelList( hotelIdList );
                        // // マージを行う
                        // hotelIdList = searchHotelCommon.getMargeHotel( pageRecords, pageNum );
                        //
                        // // 表示名称
                        // pageHeader = "「" + paramAndWord + "」で絞込み検索しました";

                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[ActionAPIInterChangeSearch.execute() ] UnsupportedEncodingException =" + e.toString() );
                        throw e;
                    }
                }

                // ホテル詳細情報の取得
                searchHotelDao = new SearchHotelDao_M2();
                searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );
                searchHotelIC.getMapPointInfo( paramIcId );

                // データのセット
                arrDataSearchHotel = searchHotelDao.getHotelInfo();
                dMapPoint = searchHotelIC.getIcInfo();
                hotelCount = searchHotelDao.getCount();
                hotelAllCount = searchHotelDao.getAllCount();
                icName = dMapPoint.getName();
            }
            else
            {
                hotelAllCount = 0;
                hotelCount = 0;
                errorMsg = Constants.errorRecordsNotFound2;
            }

            // 検索結果作成
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( errorMsg );
            searchResult.setResultCount( hotelAllCount );

            // 都道府県コードが入っていない場合を考慮してICIDから都道府県IDを取得する
            if ( Integer.parseInt( paramPrefId ) == 0 && paramIcId.equals( "" ) == false )
            {
                DataMapPoint dmap = new DataMapPoint();
                dmap.getData( paramIcId );
                paramPrefId = Integer.toString( dmap.getJisCode() / 1000 );
            }

            // ローテーションバナー取得
            ret = sd.getAdRandomData( Integer.parseInt( paramPrefId ), DISP_COUNT, DISP_FLAG );

            if ( ret != false )
            {

                for( i = 0 ; i < sd.getSponsor().length ; i++ )
                {
                    GenerateXmlAd ad = new GenerateXmlAd();
                    // スマートフォンの表示数を追加
                    sd.setImpressionCountForSmart( sd.getSponsor()[i].getSponsorCode() );

                    // 広告用のXMLを追加
                    ad.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                    ad.setAdInfo2( sd.getSponsor()[i], request );
                    searchResult.addAd( ad );

                }
            }

            // ホテルの情報をセット
            for( i = 0 ; i < hotelCount ; i++ )
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
            searchHeader.setName( icName );
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
            Logging.error( "[ActionApiInterChangeSearch.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "IC検索" );
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
                Logging.error( "[ActionApiInterChangeSearch response]Exception:" + e.toString() );
            }
        }
        finally
        {
            dMapPoint = null;
            arrDataSearchHotel = null;
            searchHotelIC = null;
            searchHotelCommon = null;
            searchHotelDao = null;
        }
    }
}
