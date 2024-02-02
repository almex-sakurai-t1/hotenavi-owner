package jp.happyhotel.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.ApiConstants;
import jp.happyhotel.common.BaseAction;
import jp.happyhotel.common.BaseApiAction;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.Logging;
import jp.happyhotel.others.GenerateXmlContents;
import jp.happyhotel.others.GenerateXmlHeader;
import jp.happyhotel.user.UserLoginInfo;

/**
 * 
 * 住所検索クラス（携帯）
 * 
 * @author S.Tashiro
 * @version 1.0 2011/04/06
 */

public class ActionApiWebService extends BaseAction
{
    final String              MASTER_VERSION    = "MasterVersion";
    final String              MASTER_ALL        = "MasterAll";
    final String              AD                = "Ad";
    final String              AD_RANDOM         = "AdRandom";
    final String              GPS               = "Gps";
    final String              AREA              = "Area";
    final String              STATION           = "St";
    final String              IC                = "Ic";
    final String              HOTEL_AREA        = "HotelArea";
    final String              KODAWARI          = "Kodawari";
    final String              HOTENAVI          = "Hotenavi";
    final String              COUPON            = "Coupon";
    final String              CHAIN_LIST        = "ChainList";
    final String              CHAIN             = "Chain";
    final String              FREEWORD          = "Freeword";
    final String              FREEWORD2         = "Freeword2";
    final String              HAPPIE            = "Happie";
    final String              RESERVE           = "Reserve";
    final String              PICKUP            = "Pickup";
    final String              NEW_OPEN          = "Newopen";
    final String              NEW_BUZZ          = "Newbuzz";
    final String              NEW_MESSAGE       = "Newmessage";
    final String              PV_RANKING        = "PvRanking";
    final String              FAVORITE          = "Favorite";
    final String              HISTORY           = "History";
    final String              DETAIL            = "Detail";
    final String              KUCHIKOMI         = "Kuchikomi";
    final String              CHECK_USER        = "CheckUser";
    final String              TOP_MENU          = "TopMenu";
    final String              SPECIAL_MENU      = "SpecialMenu";
    final String              MYHOTEL           = "MyHotel";
    final String              TOPICS            = "Topics";
    final String              MILE              = "Mile";
    final String              TOP_MESSAGE       = "TopMessage";
    final String              ISSUE_ID          = "IssueId";
    final String              DEL_MYHOTEL       = "DelMyHotel";
    final String              REG_DEVICEID      = "RegDeviceId";
    final String              PUSH_INFO         = "PushInfo";
    final String              PUSH_UNREAD       = "PushUnread";
    final String              UPD_PUSH_DATA     = "UpdPushData";
    final String              UUID_PUSH_CONFIG  = "UuidPushConfig";
    final String              RSV_DATA          = "RsvData";
    final String              TOUCH_STATE       = "TouchState";
    final String              ISSUE_UUID        = "IssueUuid";
    final String              CHECK_RECEIPT     = "CheckReceipt";
    final String              LVJ_HOTEL         = "lvjHotel";
    final String              userAgent         = "HappyHotel";
    final String              ipAddress         = "^125\\.63\\.42\\.(1(9[2-9])|2([0-1][0-9]|2[0-3]))$"; // 125.63.42.192/27（125.63.42.192 ～ 125.63.42.223）の正規表現
    private static ApiInfos   apiInfos;
    private RequestDispatcher requestDispatcher = null;

    /**
     * ハッピーホテルWEBAPI
     * 
     * @see
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    {
        String paramUserId;
        String paramPasswd;
        String paramMethod;
        UserLoginInfo uli;
        ActionApiAdSearch aaAdSearch;
        ActionApiAdRandomSearch aaAdRandomSearch;
        ActionApiAreaSearch aaAreaSearch;
        ActionApiChainSearch aaChainSearch;
        ActionApiCouponSearch aaCouponSearch;
        ActionApiFavoriteHotel aaFavoriteHotel;
        ActionApiFreewordSearch aaFreewordSearch;
        ActionApiGpsSearch aaGpsSearch;
        ActionApiHistoryHotel aaHistoryHotel;
        ActionApiHotelAreaSearch aaHotelAreaSearch;
        ActionApiHotelDetail aaHotelDetail;
        ActionApiHotelKuchikomi aaHotelKuchikomi;
        ActionApiHotelNewArrival aaHotelNewArrival;
        ActionApiHotelNewBuzz aaHotelNewBuzz;
        ActionApiHotelNewMessage aaHotelNewMessage;
        ActionApiHotelNewOpen aaHotelNewOpen;
        ActionApiHotenaviSearch aaHotenaviSearch;
        ActionApiHotelPvRank aaHotelPvRank;
        ActionApiInterChangeSearch aaICSearch;
        ActionApiKodawariSearch aaKodawariSearch;
        ActionApiMasterVersionCheck aaMasterVersionCheck;
        ActionApiMasterDataDeliver aaMasterDataDeliver;
        ActionApiStationSearch aaStationSearch;
        ActionApiReserveSearch aaReserveSearch;
        ActionApiHappieSearch aaHappieSearch;
        ActionApiLoginCheck aaLoginCheck;
        ActionApiMenu aaMenu;
        ActionApiMyHotel aaMyHotel;
        ActionApiTopics aaTopics;
        ActionApiMile aaMile;
        ActionApiTopMessage aaTopMessage;
        ActionApiIssueId aaIssueId;
        ActionApiMyHotelDelete aaMyhotelDelete;
        ActionApiRegDeviceId aaRegDevice;
        ActionApiPushInfo aaPushInfo;
        ActionApiPushUnread aaPushUnread;
        ActionApiUpdPushData aaUpdPushData;
        ActionApiUuidPushConfig aaUuidPushConfig;
        ActionApiRsvData aaRsvData;
        ActionApiTouchState aaTouchState;
        ActionApiIssueUuid aaIssueUuid;
        ActionApiCheckReceipt aaCheckReceipt;
        GenerateXmlHeader header = new GenerateXmlHeader();

        // Logging.info( "log:" + request.getRequestURI() + "?" + request.getQueryString() );

        paramUserId = request.getParameter( "user_id" );
        paramPasswd = request.getParameter( "password" );
        paramMethod = request.getParameter( "method" );
        uli = new UserLoginInfo();

        Logging.info( "method=" + paramMethod + ", user_id=" + paramUserId + ", password=" + paramPasswd );

        if ( paramUserId == null )
        {
            paramUserId = "";
        }
        if ( paramPasswd == null )
        {
            paramPasswd = "";
        }
        if ( paramMethod == null )
        {
            paramMethod = "";
        }

        // 不正アクセス防止
        try
        {
            if ( !paramMethod.equals( LVJ_HOTEL ) && request.getHeader( "user-agent" ).indexOf( userAgent ) == -1 )
            {
                // 不正アクセスの為画面遷移
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/index.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
            else if ( paramMethod.equals( LVJ_HOTEL ) && !(request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr()).equals( "172.25.21.61" ) &&
                    !(request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr()).matches( ipAddress ) )
            {
                // 不正アクセスの為画面遷移
                requestDispatcher = request.getRequestDispatcher( request.getContextPath() + "/index.jsp" );
                requestDispatcher.forward( request, response );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionApiWebService]Exception:" + e.toString() );
        }

        if ( paramMethod.equals( LVJ_HOTEL ) ) // ラブインジャパン用JSON
        {
            apiInfos = new ApiInfos();
            apiInfos.add( LVJ_HOTEL, ActionApiLvjHotel.class.getName() );

            try
            {

                @SuppressWarnings("rawtypes") Class myClass = Class.forName( apiInfos.getClassName( paramMethod ) );
                BaseApiAction action = (jp.happyhotel.common.BaseApiAction)myClass.newInstance();
                action.initialize( request );
                action.execute( request, response );
                return;
            }
            catch ( Throwable t )
            {
                Logging.error( "[ActionApiWebService method=" + paramMethod + "]Exception:" + t.toString(), t );
                BaseApiAction action = new BaseApiAction();
                action.initialize( request );
                action.outputErrorJon( response, ApiConstants.ERROR_CODE_API9, ApiConstants.ERROR_MSG_API9 );
            }

            return;

        }
        else
        // 他はXML
        {
            if ( paramUserId.equals( "" ) == false && paramPasswd.equals( "" ) == false )
            {
                // ユーザ認証を行う
                uli.getUserLoginInfo( paramUserId, paramPasswd );
            }
            // 結果をセット
            request.setAttribute( "USER_INFO", uli );

            // 会員情報チェック
            if ( paramMethod.equals( CHECK_USER ) != false )
            {
                aaLoginCheck = new ActionApiLoginCheck();
                aaLoginCheck.execute( request, response );
                return;
            }

            // いづれかの会員情報を取得したらparamMethodで判別する
            if ( uli.isMemberFlag() != false || uli.isNonmemberFlag() != false || uli.isPaymemberFlag() != false || uli.isPaymemberTempFlag() != false )
            {

                // 各メソッドに応じて振り分ける
                // マスターバージョンの確認
                if ( paramMethod.equals( MASTER_VERSION ) != false )
                {
                    aaMasterVersionCheck = new ActionApiMasterVersionCheck();
                    aaMasterVersionCheck.execute( request, response );
                    return;
                }
                // マスター情報取得
                else if ( paramMethod.equals( MASTER_ALL ) != false )
                {
                    aaMasterDataDeliver = new ActionApiMasterDataDeliver();
                    aaMasterDataDeliver.execute( request, response );
                    return;
                }
                // 広告情報取得
                else if ( paramMethod.equals( AD ) != false )
                {
                    aaAdSearch = new ActionApiAdSearch();
                    aaAdSearch.execute( request, response );
                    return;
                }
                // 広告ローテーション
                else if ( paramMethod.equals( AD_RANDOM ) != false )
                {
                    aaAdRandomSearch = new ActionApiAdRandomSearch();
                    aaAdRandomSearch.execute( request, response );
                    return;
                }

                // GPS情報取得
                else if ( paramMethod.equals( GPS ) != false )
                {
                    aaGpsSearch = new ActionApiGpsSearch();
                    aaGpsSearch.execute( request, response );
                    return;
                }
                // 住所検索
                else if ( paramMethod.equals( AREA ) != false )
                {
                    aaAreaSearch = new ActionApiAreaSearch();
                    aaAreaSearch.execute( request, response );
                    return;
                }
                // 駅検索
                else if ( paramMethod.equals( STATION ) != false )
                {
                    aaStationSearch = new ActionApiStationSearch();
                    aaStationSearch.execute( request, response );
                    return;
                }
                // インターチェンジ検索
                else if ( paramMethod.equals( IC ) != false )
                {
                    aaICSearch = new ActionApiInterChangeSearch();
                    aaICSearch.execute( request, response );
                    return;
                }
                // ホテルエリア検索
                else if ( paramMethod.equals( HOTEL_AREA ) != false )
                {
                    aaHotelAreaSearch = new ActionApiHotelAreaSearch();
                    aaHotelAreaSearch.execute( request, response );
                    return;
                }
                // こだわり検索
                else if ( paramMethod.equals( KODAWARI ) != false )
                {
                    aaKodawariSearch = new ActionApiKodawariSearch();
                    aaKodawariSearch.execute( request, response );
                    return;
                }
                // ホテナビ検索
                else if ( paramMethod.equals( HOTENAVI ) != false )
                {
                    aaHotenaviSearch = new ActionApiHotenaviSearch();
                    aaHotenaviSearch.execute( request, response );
                    return;
                }
                // クーポン検索
                else if ( paramMethod.equals( COUPON ) != false )
                {
                    aaCouponSearch = new ActionApiCouponSearch();
                    aaCouponSearch.execute( request, response );
                    return;
                }
                // チェーン店検索
                else if ( paramMethod.equals( CHAIN_LIST ) != false || paramMethod.equals( CHAIN ) != false )
                {
                    aaChainSearch = new ActionApiChainSearch();
                    aaChainSearch.execute( request, response );
                    return;
                }
                // フリーワード検索
                else if ( paramMethod.equals( FREEWORD ) != false || paramMethod.equals( FREEWORD2 ) != false )
                {
                    aaFreewordSearch = new ActionApiFreewordSearch();
                    aaFreewordSearch.execute( request, response );
                    return;
                }
                // ハピー加盟店検索
                else if ( paramMethod.equals( HAPPIE ) != false )
                {
                    aaHappieSearch = new ActionApiHappieSearch();
                    aaHappieSearch.execute( request, response );
                    return;
                }
                // 予約ハピー加盟店検索
                else if ( paramMethod.equals( RESERVE ) != false )
                {
                    aaReserveSearch = new ActionApiReserveSearch();
                    aaReserveSearch.execute( request, response );
                    return;
                }
                // 新着ホテル検索
                else if ( paramMethod.equals( PICKUP ) != false )
                {
                    aaHotelNewArrival = new ActionApiHotelNewArrival();
                    aaHotelNewArrival.execute( request, response );
                    return;
                }
                // ニューオープンホテル検索
                else if ( paramMethod.equals( NEW_OPEN ) != false )
                {
                    aaHotelNewOpen = new ActionApiHotelNewOpen();
                    aaHotelNewOpen.execute( request, response );
                    return;
                }
                // 新着クチコミ
                else if ( paramMethod.equals( NEW_BUZZ ) != false )
                {
                    aaHotelNewBuzz = new ActionApiHotelNewBuzz();
                    aaHotelNewBuzz.execute( request, response );
                    return;
                }
                // 新着メッセージ
                else if ( paramMethod.equals( NEW_MESSAGE ) != false )
                {
                    aaHotelNewMessage = new ActionApiHotelNewMessage();
                    aaHotelNewMessage.execute( request, response );
                    return;
                }
                // ホテルランキング
                else if ( paramMethod.equals( PV_RANKING ) != false )
                {
                    aaHotelPvRank = new ActionApiHotelPvRank();
                    aaHotelPvRank.execute( request, response );
                    return;
                }
                // お気に入りホテル
                else if ( paramMethod.equals( FAVORITE ) != false )
                {
                    aaFavoriteHotel = new ActionApiFavoriteHotel();
                    aaFavoriteHotel.execute( request, response );
                    return;
                }
                // 最近チェックしたホテル
                else if ( paramMethod.equals( HISTORY ) != false )
                {
                    aaHistoryHotel = new ActionApiHistoryHotel();
                    aaHistoryHotel.execute( request, response );
                    return;
                }
                // ホテル詳細
                else if ( paramMethod.equals( DETAIL ) != false )
                {
                    aaHotelDetail = new ActionApiHotelDetail();
                    aaHotelDetail.execute( request, response );
                    return;
                }
                // クチコミ
                else if ( paramMethod.equals( KUCHIKOMI ) != false )
                {
                    aaHotelKuchikomi = new ActionApiHotelKuchikomi();
                    aaHotelKuchikomi.execute( request, response );
                    return;
                }
                // XMLメニュー表示
                else if ( (paramMethod.equals( TOP_MENU ) != false) || (paramMethod.equals( SPECIAL_MENU ) != false) )
                {
                    aaMenu = new ActionApiMenu();
                    aaMenu.execute( request, response );
                    return;
                }
                // マイホテル
                else if ( paramMethod.equals( MYHOTEL ) != false )
                {
                    aaMyHotel = new ActionApiMyHotel();
                    aaMyHotel.execute( request, response );
                    return;
                }
                // トピックス
                else if ( paramMethod.equals( TOPICS ) != false )
                {
                    aaTopics = new ActionApiTopics();
                    aaTopics.execute( request, response );
                    return;
                }
                // ユーザマイル表示
                else if ( paramMethod.equals( MILE ) != false )
                {
                    aaMile = new ActionApiMile();
                    aaMile.execute( request, response );
                    return;
                }
                // トップメッセージ
                else if ( paramMethod.equals( TOP_MESSAGE ) != false )
                {
                    aaTopMessage = new ActionApiTopMessage();
                    aaTopMessage.execute( request, response );
                    return;
                }
                // アプリ会員仮登録
                else if ( paramMethod.equals( ISSUE_ID ) != false )
                {
                    aaIssueId = new ActionApiIssueId();
                    aaIssueId.execute( request, response );
                    return;
                }
                // マイホテル削除
                else if ( paramMethod.equals( DEL_MYHOTEL ) != false )
                {
                    aaMyhotelDelete = new ActionApiMyHotelDelete();
                    aaMyhotelDelete.execute( request, response );
                    return;

                }
                // PUSH通知用ID登録通知
                else if ( paramMethod.equals( REG_DEVICEID ) != false )
                {
                    aaRegDevice = new ActionApiRegDeviceId();
                    aaRegDevice.execute( request, response );
                    return;
                }
                // PUSH一覧取得
                else if ( paramMethod.equals( PUSH_INFO ) != false )
                {
                    aaPushInfo = new ActionApiPushInfo();
                    aaPushInfo.execute( request, response );
                    return;
                }
                // PUSH未読件数通知
                else if ( paramMethod.equals( PUSH_UNREAD ) != false )
                {
                    aaPushUnread = new ActionApiPushUnread();
                    aaPushUnread.execute( request, response );
                    return;
                }
                // PUSH未読件数通知
                else if ( paramMethod.equals( UPD_PUSH_DATA ) != false )
                {
                    aaUpdPushData = new ActionApiUpdPushData();
                    aaUpdPushData.execute( request, response );
                    return;
                }
                // ハピホテPUSH通知設定
                else if ( paramMethod.equals( UUID_PUSH_CONFIG ) != false )
                {
                    aaUuidPushConfig = new ActionApiUuidPushConfig();
                    aaUuidPushConfig.execute( request, response );
                    return;
                }
                // 予約一覧情報
                else if ( paramMethod.equals( RSV_DATA ) != false )
                {
                    aaRsvData = new ActionApiRsvData();
                    aaRsvData.execute( request, response );
                    return;
                }
                // タッチ受付状況
                else if ( paramMethod.equals( TOUCH_STATE ) != false )
                {
                    aaTouchState = new ActionApiTouchState();
                    aaTouchState.execute( request, response );
                    return;
                }
                // UUID発行
                else if ( paramMethod.equals( ISSUE_UUID ) != false )
                {
                    aaIssueUuid = new ActionApiIssueUuid();
                    aaIssueUuid.execute( request, response );
                    return;
                }
                // レシート検証要求
                else if ( paramMethod.equals( CHECK_RECEIPT ) != false )
                {
                    aaCheckReceipt = new ActionApiCheckReceipt();
                    aaCheckReceipt.execute( request, response );
                    return;
                }

                else
                {
                    GenerateXmlContents contents = new GenerateXmlContents();
                    contents.setError( Constants.ERROR_MSG_API4 );
                    contents.setErrorCode( Constants.ERROR_CODE_API4 );

                    // ルートノードをセット
                    header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                    header.setMethod( paramMethod );
                    header.setCount( 0 );
                    header.setContents( contents );

                }
            }
            else
            {
                GenerateXmlContents contents = new GenerateXmlContents();
                contents.setError( Constants.ERROR_MSG_API3 );
                contents.setErrorCode( Constants.ERROR_CODE_API3 );

                // ルートノードをセット
                header.setRootNode( Constants.ROOT_TAG_NAME_HAPPYHOTEL );
                header.setMethod( paramMethod );
                header.setCount( 0 );
                header.setContents( contents );
            }

            try
            {
                // 出力をヘッダーから
                String xmlOut = header.createXml();
                Logging.info( xmlOut );
                ServletOutputStream out = null;

                out = response.getOutputStream();
                response.setContentType( "text/xml; charset=UTF-8" );
                out.write( xmlOut.getBytes( "UTF-8" ) );
            }
            catch ( Exception e )
            {
                Logging.error( "[ActionApiWebService]Exception:" + e.toString() );
            }
        }
    }

    /**
     * API情報管理クラス
     * 
     */
    private class ApiInfos
    {
        Map<String, ApiInfo> map = new HashMap<String, ApiInfo>();

        /**
         * API情報を追加
         * 
         * @param method
         * @param className
         */
        public void add(String method, String className)
        {
            ApiInfo apiInfo = new ApiInfo( method, className, false );
            map.put( method, apiInfo );
        }

        /**
         * API情報を追加
         * 
         * @param method
         * @param className
         * @param needAuth
         */
        public void add(String method, String className, boolean needAuth)
        {
            ApiInfo apiInfo = new ApiInfo( method, className, needAuth );
            map.put( method, apiInfo );
        }

        /**
         * 有効なAPIかどうか
         * 
         * @param method
         * @return
         */
        public boolean exists(String method)
        {
            return getClassName( method ) != null;
        }

        /**
         * APIのクラス名を返す
         * 
         * @param method
         * @return
         */
        public String getClassName(String method)
        {
            ApiInfo apiInfo = map.get( method );
            if ( apiInfo == null )
            {
                return null;
            }
            return apiInfo.getClassName();
        }

        /**
         * 認証の要否を返す
         * 
         * @param method
         * @return
         */
        public boolean isNeedAuth(String method)
        {
            ApiInfo apiInfo = map.get( method );
            if ( apiInfo == null )
            {
                return true;
            }
            return apiInfo.isNeedAuth();
        }
    }

    /**
     * API情報
     * 
     */
    private class ApiInfo
    {
        String  method;
        String  className;
        boolean needAuth;

        public ApiInfo(String method, String className, boolean needAuth)
        {
            this.method = method;
            this.className = className;
            this.needAuth = needAuth;
        }

        @SuppressWarnings("unused")
        public String getMethod()
        {
            return method;
        }

        public String getClassName()
        {
            return className;
        }

        public boolean isNeedAuth()
        {
            return needAuth;
        }
    }
}
