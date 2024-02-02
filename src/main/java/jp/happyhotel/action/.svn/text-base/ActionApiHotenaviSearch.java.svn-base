package jp.happyhotel.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.data.DataSearchResult_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.search.SearchHotelHotenavi_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * ホテナビ検索クラス（API）
 * 
 * @author S.Tashiro.
 * @version 1.0 2011/04/08
 */
public class ActionApiHotenaviSearch extends BaseAction
{

    static int    pageRecords         = Constants.pageLimitRecords;
    static int    maxRecords          = Constants.maxRecords;
    static String recordsNotFoundMsg1 = Constants.errorRecordsNotFound1;
    static String recordsNotFoundMsg2 = Constants.errorRecordsNotFound2;
    static String limitFreewordMsg    = Constants.errorLimitFreeword;

    /**
     * ホテナビ検索（携帯）
     * 
     * @param request リクエスト
     * @param response レスポンス
     * @see "パラメータなし→index_M2.jsp<br>
     *      地方IDあり→search_hotenavi_pref_M2.jsp<br>
     *      都道府県IDあり→search_result_hotenavi_M2.jsp"
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        final int DISP_COUNT = 1;
        final int DISP_FLAG = 2;

        int pageNum = 0;
        int hotelCount = 0;
        int hotelAllCount = 0;
        int[] hotelIdList = null;
        String paramLocalId = null;
        String paramPage = null;
        String paramPrefId = null;
        String paramAndWord;
        String errorMsg = "";
        String paramMethod = null;
        String prefName = "";
        DataSearchHotel_M2[] dataSearchHotelInfo = null;
        SearchHotelHotenavi_M2 searchHotelHotenavi = null;
        SearchHotelCommon searchHotelCommon = null;
        DataSearchHotel_M2 dataSearchHotelDTO = null;
        DataSearchResult_M2 dataSearchResultDTO = null;
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
            paramLocalId = request.getParameter( "local_id" );
            paramPage = request.getParameter( "page" );
            paramPrefId = request.getParameter( "pref_id" );
            paramMethod = request.getParameter( "method" );
            paramAndWord = request.getParameter( "andword" );

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

            if ( paramPrefId != null && CheckString.numCheck( paramPrefId ) != false )
            {
                searchHotelCommon = new SearchHotelCommon();
                searchHotelHotenavi = new SearchHotelHotenavi_M2();
                dataSearchResultDTO = new DataSearchResult_M2();

                if ( paramPage != null )
                {
                    pageNum = Integer.parseInt( paramPage );
                }
                dmp = new DataMasterPref();
                dmp.getData( Integer.parseInt( paramPrefId ) );
                prefName = dmp.getName();

                // ホテルIDリスト
                hotelIdList = searchHotelHotenavi.getHotelIdList( Integer.parseInt( paramPrefId ) );

                // ホテルIDリストをセット
                searchHotelCommon.setEquipHotelList( hotelIdList );

                // ホテルIDリストからホテル詳細情報を取得
                searchHotelDao = new SearchHotelDao_M2();
                searchHotelDao.getHotelList( hotelIdList, pageRecords, pageNum );

                // 共通にするためデータをセットする
                dataSearchHotelInfo = searchHotelDao.getHotelInfo();
                hotelCount = searchHotelDao.getCount();
                hotelAllCount = searchHotelDao.getAllCount();
                if ( hotelCount == 0 )
                {
                    errorMsg = recordsNotFoundMsg2;
                }
            }
            else
            {
                hotelCount = 0;
                hotelAllCount = 0;
                errorMsg = recordsNotFoundMsg2;
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
                addHotel.addHotelInfo( dataSearchHotelInfo[i], uli.isPaymemberFlag(), 0 );
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
            Logging.error( "[ActionApiHotenaviSearch.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "ホテナビ検索" );
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
                Logging.error( "[ActionApiHotenaviSearch response]Exception:" + e.toString() );
            }

        }
        finally
        {
            searchHotelHotenavi = null;
            searchHotelCommon = null;
            dataSearchHotelDTO = null;
            dataSearchResultDTO = null;
            dataSearchHotelInfo = null;
            searchHotelDao = null;
        }

    }
}
