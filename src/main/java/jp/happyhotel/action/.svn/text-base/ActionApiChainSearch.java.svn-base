package jp.happyhotel.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.data.DataMasterChain_M2;
import jp.happyhotel.data.DataSearchHotel_M2;
import jp.happyhotel.others.GenerateXmlAd;
import jp.happyhotel.others.GenerateXmlChain;
import jp.happyhotel.others.GenerateXmlChainList;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.others.GenerateXmlSearchResult;
import jp.happyhotel.others.GenerateXmlSearchResultHotel;
import jp.happyhotel.search.SearchEngineBasic_M2;
import jp.happyhotel.search.SearchHotelChain_M2;
import jp.happyhotel.search.SearchHotelDao_M2;
import jp.happyhotel.sponsor.SponsorData_M2;
import jp.happyhotel.user.UserLoginInfo;

/**
 * チェーン店検索クラス（API）
 * 
 * @author S.Tashirro
 * @version 1.0 2011/04/25
 * 
 */
public class ActionApiChainSearch extends BaseAction
{

    static int                 pageRecords       = Constants.pageLimitRecords;     // 表示件数
    static int                 maxRecords        = Constants.maxRecordsMobile;     // 最大件数
    static String              recordNotFound1   = Constants.errorRecordsNotFound1; // 件数なしの場合のエラーメッセージ
    static String              recordsNotFound2  = Constants.errorRecordsNotFound2; // 件数なしの場合のエラーメッセージ
    static String              recordsNotFound3  = Constants.errorRecordsNotFound3; // 最大件数を超えた場合のエラーメッセージ
    public static final int    dispFormat        = 2;
    public static final int    DISP_MAX          = 200;
    public static final String CHAIN_LIST        = "ChainList";
    public static final String CHAIN             = "Chain";

    private RequestDispatcher  requestDispatcher = null;

    /**
     * チェーン検索（API）
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
        int masterEquipCount;
        int[] arrHotelIdList = null;
        String paramPrefId;
        String paramChainId;
        String paramPage;
        String paramAndWord;
        String chainName = "";
        String errorMsg = "";
        String paramMethod = null;
        DataSearchHotel_M2[] arrDataSearchHotel = null;
        DataMasterChain_M2 dataMasterChainObj = null;
        SearchHotelChain_M2 searchHotelChain = null;
        SearchHotelDao_M2 searchHotelDao = null;
        UserLoginInfo uli;
        String paramSponsorCd;

        SearchEngineBasic_M2 searchEngineBasic;
        // XML出力
        boolean ret = false;
        SponsorData_M2 sd;
        sd = new SponsorData_M2();
        GenerateXmlAd ad = new GenerateXmlAd();

        try
        {
            uli = (UserLoginInfo)request.getAttribute( "USER_INFO" );
            paramPrefId = request.getParameter( "prefId" );
            paramChainId = request.getParameter( "chainId" );
            paramPage = request.getParameter( "page" );
            paramAndWord = request.getParameter( "andword" );
            paramMethod = request.getParameter( "method" );
            paramSponsorCd = request.getParameter( "sponsor" );

            if ( uli == null )
            {
                uli = new UserLoginInfo();
            }

            if ( paramPrefId == null || paramPrefId.equals( "" ) != false || CheckString.numCheck( paramPrefId ) == false )
            {
                paramPrefId = "0";
            }
            if ( paramPage == null || paramPage.equals( "" ) != false || CheckString.numCheck( paramPage ) == false )
            {
                paramPage = "0";
            }
            if ( paramChainId == null || paramChainId.equals( "" ) != false || CheckString.numCheck( paramChainId ) == false )
            {
                paramChainId = "0";
            }
            if ( paramSponsorCd != null && paramSponsorCd.equals( "" ) == false )
            {
                sd.setClickCountForSmart( Integer.parseInt( paramSponsorCd ) );
            }

            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            // チェーン店を出力する
            if ( paramMethod.equals( CHAIN_LIST ) != false )
            {
                boolean hasHotels;
                int groupCount = 0;
                GenerateXmlChainList chainList = new GenerateXmlChainList();

                searchHotelChain = new SearchHotelChain_M2();
                // チェーン店のリストを取得
                hasHotels = searchHotelChain.getChainList();
                // チェーン店の件数を取得
                groupCount = searchHotelChain.getGroupCount();

                if ( hasHotels && (groupCount > 0) )
                {
                    chainList.setErrorCode( "0" );
                    chainList.setChainCount( groupCount );
                    // 情報をセットして出力する
                    for( int i = 0 ; i < groupCount ; i++ )
                    {
                        GenerateXmlChain chain = new GenerateXmlChain();
                        chain.setId( searchHotelChain.getGroupIdList()[i] );
                        chain.setName( ReplaceString.replaceApiSpecial( searchHotelChain.getGroupNameList()[i] ) );
                        chain.setCount( searchHotelChain.getGroupCountList()[i] );
                        chainList.setChain( chain );
                    }
                }
                else
                {
                    // エラーページを表示させる
                    errorMsg = recordsNotFound2;
                    chainList.setErrorCode( "3" );
                    chainList.setErrorMessage( recordsNotFound2 );
                }

                // 検索結果ヘッダ作成
                searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                searchHeader.setMethod( paramMethod );
                searchHeader.setName( "チェーン店検索" );
                searchHeader.setAndword( paramAndWord );
                searchHeader.setCount( groupCount );
                // 検索結果ノードを検索結果ヘッダーノードに追加
                searchHeader.setChain( chainList );
            }
            // チェーン店検索結果を出力する
            else if ( paramMethod.equals( CHAIN ) != false )
            {
                if ( Integer.parseInt( paramChainId ) > 0 )
                {
                    searchHotelChain = new SearchHotelChain_M2();
                    pageNum = Integer.parseInt( paramPage );

                    // チェーン店データの取得
                    dataMasterChainObj = searchHotelChain.getMasterChainData( paramChainId );
                    if ( dataMasterChainObj != null )
                    {
                        chainName = dataMasterChainObj.getName();
                    }

                    if ( Integer.parseInt( paramPrefId ) > 0 )
                    {
                        if ( CheckString.numCheck( paramPrefId ) )
                        {
                            // ホテルIDのリスト取得
                            arrHotelIdList = searchHotelChain.getHotelBasicIdListByPref( paramChainId, paramPrefId );
                        }
                    }
                    else
                    {
                        // ホテルIDのリスト取得
                        arrHotelIdList = searchHotelChain.getHotelBasicIdList( paramChainId );
                    }

                    // ホテル詳細情報
                    searchHotelDao = new SearchHotelDao_M2();
                    searchHotelDao.getHotelList( arrHotelIdList, pageRecords, pageNum );
                    // 共通にするためデータをセットする
                    arrDataSearchHotel = searchHotelDao.getHotelInfo();
                    hotelCount = searchHotelDao.getCount();
                    hotelAllCount = searchHotelDao.getAllCount();

                    if ( hotelAllCount == 0 )
                    {
                        errorMsg = recordsNotFound2;
                    }
                }
                else
                {
                    hotelCount = 0;
                    hotelAllCount = 0;
                    errorMsg = recordsNotFound2;
                }

                // 検索結果をセット
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
                searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                searchHeader.setMethod( paramMethod );
                searchHeader.setName( ReplaceString.replaceApiSpecial( chainName ) );
                searchHeader.setAndword( paramAndWord );
                searchHeader.setCount( hotelCount );
                // 検索結果ノードを検索結果ヘッダーノードに追加
                searchHeader.setSearchResult( searchResult );

            }

            if ( searchHeader != null )
            {
                String xmlOut = searchHeader.createXml();
                ServletOutputStream out = null;

                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }

        }
        catch ( Exception exception )
        {
            Logging.error( "[ActionApiChainSearch.execute() ] Exception:", exception );

            // エラーを出力
            GenerateXmlHeader searchHeader = new GenerateXmlHeader();
            GenerateXmlSearchResult searchResult = new GenerateXmlSearchResult();
            searchResult.setError( Constants.ERROR_MSG_API10 );
            searchResult.setResultCount( 0 );

            // 検索結果ヘッダ作成
            searchHeader.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
            searchHeader.setMethod( paramMethod );
            searchHeader.setName( "チェーン店検索" );
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
                Logging.error( "[ActionApiChainSearch response]Exception:" + e.toString() );
            }
        }
        finally
        {
            arrDataSearchHotel = null;
        }
    }
}
