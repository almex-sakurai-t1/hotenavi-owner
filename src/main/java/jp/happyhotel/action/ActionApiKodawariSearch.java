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
import jp.happyhotel.search.SearchKodawari_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * フリーワード検索クラス（API）
 * 
 * @author S.Tashirro
 * @version 1.0 2011/04/21
 * 
 */

public class ActionApiKodawariSearch extends BaseAction
{

    static int              pageRecords      = Constants.pageLimitRecords;     // 表示件数
    static int              maxRecords       = Constants.maxRecordsMobile;     // 最大件数
    static String           recordNotFound1  = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String           recordsNotFound2 = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String           recordsNotFound3 = Constants.errorRecordsNotFound3; // 最大件数を超えた場合のエラーメッセージ
    public static final int dispFormat       = 2;
    public static final int DISP_MAX         = 200;

    /**
     * こだわり検索（API）
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
        int i;
        int searchKind = 0;
        String cityName = "";
        String paramPrefId = null;
        String paramLocalId = null;
        String paramPage;
        String paramAndWord;
        String errorMsg = "";
        String paramMethod = null;
        String paramKind = null;
        String[] arrayPrefId = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        SearchKodawari_M2 searchKodawari;
        UserLoginInfo uli;

        // XML出力
        boolean ret = false;
        SponsorData_M2 sd;
        sd = new SponsorData_M2();
        GenerateXmlAd ad = new GenerateXmlAd();

        uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
        paramLocalId = request.getParameter( "local_id" );
        paramPrefId = request.getParameter( "pref_id" );
        paramPage = request.getParameter( "page" );
        paramAndWord = request.getParameter( "andword" );
        paramMethod = request.getParameter( "method" );
        paramKind = request.getParameter( "kind" );
        arrayPrefId = request.getParameterValues( "pref_id" );

        searchKodawari = new SearchKodawari_M2();
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
        if ( paramKind == null || paramKind.equals( "" ) != false || CheckString.numCheck( paramKind ) == false )
        {
            paramKind = "0";
        }
        if ( arrayPrefId != null )
        {
            if ( arrayPrefId.length == 1 )
            {
                searchKind = 0;
            }
            else if ( arrayPrefId.length > 1 )
            {
                searchKind = 1;
            }
        }
        else
        {
            searchKind = 0;
        }

        try
        {

            if ( searchKind == 0 )
            {
                // 都道府県が指定されている
                if ( Integer.parseInt( paramPrefId ) > 0 )
                {
                    searchKodawari.searchHotelListMobile( request, Integer.parseInt( paramPage ), pageRecords, Integer.parseInt( paramPrefId ) );

                    arrDataSearchHotel = searchKodawari.getHotelInfo();
                    hotelCount = searchKodawari.getCount();
                    hotelAllCount = searchKodawari.getAllCount();

                    // 200件以上の場合はhotelAllCountを200件で返す
                    if ( arrDataSearchHotel != null && hotelAllCount > DISP_MAX )
                    {
                        hotelAllCount = DISP_MAX;
                    }
                    else if ( arrDataSearchHotel == null || hotelAllCount == 0 )
                    {
                        hotelAllCount = 0;
                        hotelCount = 0;
                        errorMsg = recordsNotFound2;
                    }
                }
                else
                {
                    // 共通にするためデータをセットする
                    arrDataSearchHotel = null;
                    hotelCount = 0;
                    hotelAllCount = 0;
                    errorMsg = "都道府県を選択してください。";
                }
            }
            else if ( searchKind == 1 )
            {
                searchKodawari.searchHotelList( request, Integer.parseInt( paramPage ), pageRecords );
                arrDataSearchHotel = searchKodawari.getHotelInfo();
                hotelCount = searchKodawari.getCount();
                hotelAllCount = searchKodawari.getAllCount();

                // 200件超えたらエラーとする
                if ( hotelAllCount > DISP_MAX )
                {
                    errorMsg = recordsNotFound3;
                }

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
            // searchResult.setAd( ad );

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
            Logging.error( "[ActionApiKodawariSearch.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "こだわり検索" );
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
                Logging.error( "[ActionApiKodawariSearch response]Exception:" + e.toString() );
            }
        }
    }
}
