package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSearchArea_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchArea_M2;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelFreeword_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * 
 * 住所検索クラス（API）
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionApiAreaSearch extends BaseAction
{

    static int              pageRecords     = Constants.pageLimitRecords;
    static int              maxRecords      = Constants.maxRecordsMobile;
    static String           recordNotFound2 = Constants.errorRecordsNotFound2;
    public static final int dispFormat      = 1;

    /**
     * 住所検索
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      都道府県ID、地方ID→area_01_M2.jsp<br>
     *      市区町村ID→search_result_area_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 5;
        final int DISP_FLAG = 2;
        int hotelCount;
        int hotelAllCount;
        int pageNum = 0;
        int jisCode;
        int prefId;
        int[] arrHotelIdList = null;
        String cityName = "";
        String paramJisCode = null;
        String paramPrefId = null;
        String paramLocalId = null;
        String paramPage;
        String paramAndWord;
        String errorMsg = "";
        String paramMethod = null;
        SearchArea_M2 searchArea;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataSearchArea_M2 dataSearchAreaDTO = null;
        SearchHotelCommon shcom = null;
        SearchHotelFreeword_M2 searchHotelFreeWord = null;
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
            paramJisCode = request.getParameter( "jis_code" );
            paramPage = request.getParameter( "page" );
            paramAndWord = request.getParameter( "andword" );
            paramMethod = request.getParameter( "method" );

            searchArea = new SearchArea_M2();
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

            // 検索結果の取得
            if ( CheckString.numCheck( paramJisCode ) && CheckString.numCheck( paramPage ) )
            {
                pageNum = Integer.parseInt( paramPage );
                jisCode = Integer.parseInt( paramJisCode );

                // 市区町村データの取得
                searchArea.getCityDetail( jisCode );
                cityName = searchArea.getCityName();
                prefId = searchArea.getPrefId();

                // 市区町村IDからホテルIDの取得
                arrHotelIdList = searchArea.getSearchIdList( jisCode );

                shcom = new SearchHotelCommon();
                shcom.setPrefHotelList( arrHotelIdList );

                if ( arrHotelIdList != null && arrHotelIdList.length > 0 )
                {
                    // ホテル詳細情報の取得
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );

                    // 共通にするためデータをセットする
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();
                }
                else
                {
                    hotelAllCount = 0;
                    hotelCount = 0;
                    errorMsg = recordNotFound2;
                }

            }
            else
            {
                hotelAllCount = 0;
                hotelCount = 0;
                errorMsg = recordNotFound2;
            }

            // 検索結果作成
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( errorMsg );
            searchResult.setResultCount( hotelAllCount );

            // ローテーションバナー取得
            ret = sd.getAdRandomData( Integer.parseInt( paramPrefId ), DISP_COUNT, DISP_FLAG );

            if ( ret != false )
            {

                for( int i = 0 ; i < sd.getSponsor().length ; i++ )
                {
                    GenerateXmlAd ad = new GenerateXmlAd();
                    // スマートフォンの表示数を追加
                    sd.setImpressionCountForSmart( sd.getSponsor()[i].getSponsorCode() );

                    // 広告用のXMLを追加
                    ad.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                    ad.setAdInfo2( sd.getSponsor()[i] ,request);
                    searchResult.addAd( ad );

                }
            }

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
            searchHeader.setName( cityName );
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
            Logging.error( "[ActionApiAreaSearch.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "住所検索" );
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
                Logging.error( "[ActionApiAreaSearch response]Exception:" + e.toString() );
            }
        }
        finally
        {
            searchArea = null;
            arrDataSearchHotel = null;
        }
    }
}
