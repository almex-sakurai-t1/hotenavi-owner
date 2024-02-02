package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapConvert;
import jp.happyhotel.data.DataMapPoint_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelStation_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * 駅検索クラス（API）
 * 
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/07
 */
public class ActionApiStationSearch extends BaseAction
{

    static int    pageRecords         = Constants.pageLimitRecords;     // 1ページに表示する件数
    static int    maxRecords          = Constants.maxRecords;           // 最大件数
    static String recordsNotFoundMsg1 = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String limitFreewordMsg    = Constants.errorLimitFreeword;   // 最大件数を超えた場合のエラーメッセージ

    /**
     * 駅検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      地方ID、都道府県ID→st_01_M2.jsp<br>
     *      駅名→st_01_2_M2.jsp<br>
     *      都道府県ID、ルートID→st_01_2_M2.jsp<br>
     *      ルートID、searchパラメータ→st_result_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 5;
        final int DISP_FLAG = 2;
        String paramPrefId = null;
        String paramStId = null;
        String paramPage = null;
        String paramAndWord = null;
        String paramLocalId = null;
        String stName = null;
        String errorMsg = "";
        String paramMethod = null;
        int i;
        int pageNum = 0;
        int hotelCount;
        int hotelAllCount;
        int[] hotelIdList = null;
        DataMapPoint_M2 dataMapPointDTO = null;
        SearchHotelStation_M2 searchHotelStation = null;
        SearchHotelCommon searchHotelCommon = null;
        DataSearchHotel_M2[] dataSearchHotel = null;
        SearchHotelDao_M2 searchHotelDao = null;
        UserLoginInfo uli;

        // XML出力関連
        boolean ret = false;
        SponsorData_M2 sd;
        sd = new SponsorData_M2();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramPrefId = request.getParameter( "pref_id" );
            paramStId = request.getParameter( "st_id" );
            paramPage = request.getParameter( "page" );
            paramAndWord = request.getParameter( "andword" );
            paramLocalId = request.getParameter( "local_id" );
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

            if ( CheckString.isvalidString( paramStId ) )
            {
                paramStId = new String( paramStId.getBytes( "8859_1" ), "Windows-31J" );

                // 昭文社の地図コードだった場合はゼンリンのコードに変換（#15057対応）
                if ( paramStId.matches( "(\\d+@\\d+,?)+" ) )
                {
                    DataMapConvert converter = new DataMapConvert();
                    if ( converter.getData( paramStId ) )
                    {
                        paramStId = converter.getConvertId();
                    }
                }

                searchHotelStation = new SearchHotelStation_M2();
                searchHotelCommon = new SearchHotelCommon();

                if ( paramPage != null )
                {
                    pageNum = Integer.parseInt( paramPage );
                }

                // ホテルIDリストをセット
                hotelIdList = searchHotelStation.getHotelIdList( paramStId );
                searchHotelCommon.setEquipHotelList( hotelIdList );

                if ( searchHotelStation.getDataMapPoint( paramStId ) )
                {
                    dataMapPointDTO = searchHotelStation.getStationInfo();
                    stName = dataMapPointDTO.getName();
                }

                // ホテルIDリストからホテル詳細情報を取得
                searchHotelDao = new SearchHotelDao_M2();
                searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );

                // 共通にするためデータをセットする
                dataSearchHotel = searchHotelDao.getHotelInfo();
                hotelCount = searchHotelDao.getCount();
                hotelAllCount = searchHotelDao.getAllCount();

            }
            // jis_code
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
                addHotel.addHotelInfo( dataSearchHotel[i], uli.isPaymemberFlag(), 0 );
                // 検索結果ノードにホテルノードを追加
                searchResult.addHotel( addHotel );
            }

            // 検索結果ヘッダ作成
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( stName );
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
            Logging.error( "[ActionApiStationSearch.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "駅検索" );
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
                Logging.error( "[ActionApiStationSearch response]Exception:" + e.toString() );
            }
        }
        finally
        {
            dataMapPointDTO = null;
            searchHotelCommon = null;
            searchHotelStation = null;
            searchHotelDao = null;
        }
    }
}
