package jp.happyhotel.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.ws.rs.core.MediaType;

import jp.happyhotel.data.DataHhRsvSystemConfList;
import jp.happyhotel.data.DataHotelAdjustmentHistory;
import jp.happyhotel.owner.FormOwnerBkoMenu;
import jp.happyhotel.owner.LogicOwnerBkoMenu;
import jp.happyhotel.owner.OwnerLoginInfo;
import jp.happyhotel.util.ReserveGuestMailToken;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

/**
 * オーナーサイトメニュー共通クラス
 * 
 * @author H.Takanami
 * @version 1.00 2010/12/02
 * @see
 */
public class OwnerRsvCommon implements Serializable
{
    private static final long  serialVersionUID                  = 5406052699924130708L;

    // ▼ハピー付与区分
    public static final int    POINT_KIND_ROOM                   = 1;                   // 室料での付与
    public static final int    POINT_KIND_FIX                    = 2;                   // 固定ポイントでの付与

    // ▼オーナー権限
    public static final int    USER_AUTH_OWNER                   = 1;                   // オーナー
    public static final int    USER_AUTH_FRONT                   = 2;                   // フロント
    public static final int    USER_AUTH_DEMO                    = 3;                   // 支店デモ
    public static final int    USER_AUTH_CALLCENTER              = 4;                   // コールセンター

    // ▼imediaFlg
    public static final int    IMEDIAFLG_IMEDIA                  = 1;                   // アルメックス社員
    public static final int    IMEDIAFLG_OWNER                   = 0;                   // オーナー

    // ▼ポイント制限フラグ
    public static final int    LIMIT_FLG_TIME                    = 3;                   // 時間
    public static final int    LIMIT_FLG_DAY                     = 4;                   // 日
    public static final int    LIMIT_FLG_MONTH                   = 5;                   // 月

    // ▼カレンダー曜日ステータス
    public static final int    CALENDAR_MONDAY                   = 0x01;
    public static final int    CALENDAR_TUESDAY                  = 0x02;
    public static final int    CALENDAR_WEDNESDAY                = 0x04;
    public static final int    CALENDAR_THIRTHDAY                = 0x08;
    public static final int    CALENDAR_FRIDAY                   = 0x10;
    public static final int    CALENDAR_SATURDAY                 = 0x20;
    public static final int    CALENDAR_SUNDAY                   = 0x40;
    public static final int    CALENDAR_HOLIDAY                  = 0x80;
    public static final int    CALENDAR_BEFOREHOLIDAY            = 0x100;

    // ▼ハピーポイント取得区分
    public static final int    HAPYPOINT_24                      = 24;                  // 予約マイル
    public static final int    HAPYPOINT_29                      = 29;                  // 予約ボーナスマイル取引区分
    public static final int    RSV_BONUS_CODE                    = 1000017;             // 予約ボーナスマイルコード

    // ▼プラン別カレンダー管理で使用
    public static final int    PLAN_CAL_HOTELID                  = 0;
    public static final String PLAN_CAL_HOTELNM                  = "ホテル全体";

    // ▼プラン登録で使用するボタンの区分値
    public static final int    BTN_SALES                         = 1;                   // 一時的に停止するボタン
    public static final int    BTN_REGIST                        = 2;                   // プラン設定更新
    public static final int    BTN_DRAFT                         = 3;                   // 下書き保存ボタン
    public static final int    BTN_VIEW                          = 4;                   // 表示ボタン
    public static final int    BTN_DEL                           = 5;                   // 下書き削除ボタン
    public static final int    BTN_DRAFTUPD                      = 6;                   // 下書き　プラン設定更新ボタン
    public static final int    BTN_PREVIEW                       = 7;                   // プレビュー
    public static final int    BTN_BACK                          = 8;                   // 戻るボタン
    public static final int    BTN_PREVIEW_DETAIL                = 9;                   // プレビュー詳細
    public static final int    BTN_COPYREGIST                    = 10;                  // コピーして保存ボタン
    public static final int    BTN_COPY                          = 11;                  // コピーボタン

    // プラン掲載フラグ
    public static final int    PLAN_VIEW_ALL                     = 0;                   // 未掲載プランも表示する
    public static final int    PLAN_VIEW_PART                    = 1;                   // 未掲載プランは表示しない

    // ホテル修正履歴
    public static final int    ADJUST_EDIT_ID_RSV                = 200;                 // 予約開始・停止
    public static final int    ADJUST_EDIT_ID_RSV_DAY            = 201;                 // 予約開始・停止（日付単位）
    public static final int    ADJUST_EDIT_ID_RSV_PLAN           = 202;                 // 予約開始・停止（プラン単位）
    public static final int    ADJUST_EDIT_ID_HOTEL_BASIC        = 210;                 // 施設基本情報
    public static final int    ADJUST_EDIT_ID_RSV_BASIC          = 220;                 // 予約基本情報
    public static final int    ADJUST_EDIT_ID_PLAN_ADD           = 230;                 // プラン設定追加
    public static final int    ADJUST_EDIT_ID_PLAN_UPDWN         = 231;                 // 掲載・非掲載
    public static final int    ADJUST_EDIT_ID_PLAN_UPD           = 232;                 // 変更
    public static final int    ADJUST_EDIT_ID_CHARGE_ADD         = 240;                 // 料金モード追加
    public static final int    ADJUST_EDIT_ID_CHARGE_DEL         = 241;                 // 削除
    public static final int    ADJUST_EDIT_ID_CHARGE_UPD         = 242;                 // 更新
    public static final int    ADJUST_EDIT_ID_PLAN_CHARGE_ADDUPD = 250;                 // プラン別料金追加・更新
    public static final int    ADJUST_EDIT_ID_PLAN_CHARGE_DEL    = 251;                 // 削除
    public static final int    ADJUST_EDIT_ID_ROOM               = 270;                 // 部屋情報設定
    public static final int    ADJUST_EDIT_ID_OPTION_ADD         = 280;                 // オプション設定追加
    public static final int    ADJUST_EDIT_ID_OPTION_DEL         = 281;                 // 削除
    public static final int    ADJUST_EDIT_ID_OPTION_UPD         = 282;                 // 更新
    public static final int    ADJUST_EDIT_ID_CALENDAR           = 300;                 // カレンダー設定
    public static final int    ADJUST_EDIT_ID_BILL_ADD           = 500;                 // 請求明細追加
    public static final int    ADJUST_EDIT_ID_BILL_DEL           = 501;                 // 請求明細削除

    public static final String ADJUST_MEMO_RSV_START             = "予約開始";
    public static final String ADJUST_MEMO_RSV_STOP              = "予約停止";
    public static final String ADJUST_MEMO_RSV_START_DAY         = "予約開始（日付単位）";
    public static final String ADJUST_MEMO_RSV_STOP_DAY          = "予約停止（日付単位）";
    public static final String ADJUST_MEMO_RSV_START_PLAN        = "予約開始（プラン単位）";
    public static final String ADJUST_MEMO_RSV_STOP_PLAN         = "予約停止（プラン単位）";
    public static final String ADJUST_MEMO_HOTEL_BASIC           = "施設基本情報修正";
    public static final String ADJUST_MEMO_RSV_BASIC             = "予約基本情報修正";
    public static final String ADJUST_MEMO_PLAN_ADD              = "プラン設定追加";
    public static final String ADJUST_MEMO_PLAN_UP               = "プラン設定掲載";
    public static final String ADJUST_MEMO_PLAN_DWN              = "プラン設定非掲載";
    public static final String ADJUST_MEMO_PLAN_UPD              = "プラン設定変更";
    public static final String ADJUST_MEMO_CHAGE_ADD             = "料金モード追加";
    public static final String ADJUST_MEMO_CHAGE_DEL             = "料金モード削除";
    public static final String ADJUST_MEMO_CHAGE_UPD             = "料金モード変更";
    public static final String ADJUST_MEMO_PLAN_CHARGE_ADDUPD    = "プラン別料金追加・更新";
    public static final String ADJUST_MEMO_PLAN_CHARGE_DEL       = "プラン別料金削除";
    public static final String ADJUST_MEMO_ROOM                  = "部屋情報設定";
    public static final String ADJUST_MEMO_OPTION_ADD            = "オプション設定追加";
    public static final String ADJUST_MEMO_OPTION_DEL            = "オプション設定削除";
    public static final String ADJUST_MEMO_OPTION_UPD            = "オプション設定変更";
    public static final String ADJUST_MEMO_CALENDAR              = "カレンダー設定";
    public static final String ADJUST_MEMO_BILL_ADD              = "請求明細追加";
    public static final String ADJUST_MEMO_BILL_DEL              = "請求明細削除";

    // ▼予約システム設定
    public static final int    CTRL_ID1_PREMIUM_GOAHEAD_DAYS     = 1;
    public static final int    CTRL_ID2_PREMIUM_GOAHEAD_DAYS     = 1;

    // 予約区分
    public static final int    EXT_HAPIHOTE                      = 0;                   // ハピホテからの予約
    public static final int    EXT_LVJ                           = 1;                   // ラブインジャパンからの予約
    public static final int    EXT_OTA                           = 2;                   // OTAからの予約

    // プラン販売中
    public static final int    PLAN_SALES_STATUS_SALE            = 1;

    // 掲載フラグ - 掲載
    public static final int    PUBLISHING_FLAG_PUBLISH           = 1;

    /***
     * メニュー設定
     * 
     * @param frm FormOwnerRsvMenuオブジェクト
     * @param hotelID ホテルID
     * @param modeFlg
     * @param userID ログインユーザID
     * @return なし
     **/
    public static void setMenu(FormOwnerBkoMenu frm, int hotelID, int modeFlg, Cookie[] cookies) throws Exception
    {
        int userAuth = 0;
        String hotenaviId = "";
        String loginHotelId = "";
        int loginUserId = 0;
        int hapihoteFlag = 0;
        int imediaFlg = 0;
        int adminFlg = 0;
        int billFlg = 0;
        Cookie hhCookie = null;
        LogicOwnerBkoMenu logic = new LogicOwnerBkoMenu();
        ArrayList<Integer> hotelIdList = new ArrayList<Integer>();
        ArrayList<String> hotelNmList = new ArrayList<String>();
        OwnerLoginInfo loginUser = new OwnerLoginInfo();
        String cookie_value = "";

        frm.setModeFlg( modeFlg );
        frm.setSelHotelID( hotelID );

        try
        {
            logic.setFrm( frm );

            // Cookie情報取得
            if ( cookies != null )
            {
                for( int i = 0 ; i < cookies.length ; i++ )
                {
                    if ( cookies[i].getName().compareTo( "hhownuid" ) == 0 )
                    {
                        hhCookie = cookies[i];
                        break;
                    }
                }
            }
            cookie_value = hhCookie.getValue();

            // ログイン情報取得
            if ( cookie_value.compareTo( "" ) != 0 )
            {
                loginUser.getLoginByCookie( cookie_value );
            }
            loginHotelId = loginUser.getUserInfo().getHotelId();
            loginUserId = loginUser.getUserInfo().getUserId();
            if ( loginHotelId.compareTo( "happyhotel" ) == 0 )
            {
                hapihoteFlag = 1;
            }

            // ホテナビID取得
            hotenaviId = getCookieLoginHotenavi( cookies );

            // ユーザーの権限取得
            userAuth = logic.getUserAuth( hotenaviId, loginUserId );

            // imediaFlg取得(1:アルメックス社員、0:アルメックス社員以外)
            imediaFlg = getImediaFlag( loginHotelId, loginUserId );

            // 管理者フラグ取得
            adminFlg = logic.getAdminFlg( loginHotelId, loginUserId );

            // 請求閲覧可能フラグ取得
            billFlg = OwnerBkoCommon.getBillOwnFlg( hotenaviId, loginUserId );

            // ホテル情報取得
            if ( hapihoteFlag == 1 && imediaFlg == 1 && adminFlg == 1 )
            {
                // 管理者
                hotelIdList = logic.getAdminHotelIDList();
                hotelNmList = logic.getAdminHotelNmList();
            }
            else
            {
                if ( userAuth == OwnerRsvCommon.USER_AUTH_CALLCENTER )
                {
                    // コールセンターのみマイル加盟店全ホテル
                    hotelIdList = logic.getMileHotelIDList();
                    hotelNmList = logic.getMileHotelNmList();
                }
                else
                {
                    // 管理者以外
                    hotelIdList = logic.getHotelIDList( loginHotelId, loginUserId );
                    hotelNmList = logic.getHotelNmList( loginHotelId, loginUserId );
                }
            }

            frm.setHotelIDList( hotelIdList );
            frm.setHotelNmList( hotelNmList );
            frm.setUserId( loginUserId );
            frm.setUserAuth( userAuth );
            frm.setImediaFlg( imediaFlg );
            frm.setBillFlg( billFlg );
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.setMenu() ] " + e.getMessage() );
            throw new Exception( "[OwnerRsvCommon.setMenu() ] " + e.getMessage() );
        }
    }

    /***
     * 文字列中の文字数を半角文字基準でチェックする
     * 
     * @param input チェック対象の文字列
     * @param length 入力可能長さ
     * @return チェック結果。0:未入力、1:チェックOK、99：チェックNG
     **/
    public static int LengthCheck(String input, int length) throws Exception
    {
        int ret = -1;
        int valueLeng;
        try
        {
            valueLeng = input.getBytes( "Shift_JIS" ).length;

            if ( valueLeng == 0 )
            {
                // 未入力の場合
                ret = 0;
            }
            else if ( valueLeng > length )
            {
                // 桁数Over
                ret = 1;
            }

            return ret;
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new Exception( "[ActionOwnerU10301.LengthCheck() ]" + e.toString() );
        }
    }

    /**
     * 販売停止曜日設定表示用文字列取得
     * 
     * @param salesStopWeekStatus 販売停止曜日ステータス
     * @return 販売停止曜日設定表示用文字列
     */
    public static String createSalesStopWeek(int salesStopWeekStatus)
    {
        String ret = "";

        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_MONDAY) == OwnerRsvCommon.CALENDAR_MONDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "月";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_TUESDAY) == OwnerRsvCommon.CALENDAR_TUESDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "火";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_WEDNESDAY) == OwnerRsvCommon.CALENDAR_WEDNESDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "水";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_THIRTHDAY) == OwnerRsvCommon.CALENDAR_THIRTHDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "木";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_FRIDAY) == OwnerRsvCommon.CALENDAR_FRIDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "金";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_SATURDAY) == OwnerRsvCommon.CALENDAR_SATURDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "土";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_SUNDAY) == OwnerRsvCommon.CALENDAR_SUNDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "日";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_HOLIDAY) == OwnerRsvCommon.CALENDAR_HOLIDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "祝日";
        }
        if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY) == OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY )
        {
            if ( ret.equals( "" ) != true )
            {
                ret += ",";
            }
            ret += "祝前日";
        }
        if ( ret.equals( "" ) == true )
        {
            ret = "なし";
        }

        return(ret);
    }

    /**
     * 選択済み設備IDと全ての設備IDのマッチング
     * 全ての設備IDのうち、選択されている設備IDと一致したら1をセットする。
     * 
     * @param allEquipIdList ホテルに紐づく設備IDのリスト
     * @param eqIdList 選択されている設備IDのリスト
     * @return マッチング処理が行われた後のArrayList
     */
    public static ArrayList<Integer> setSelEqList(ArrayList<Integer> allEquipIdList, ArrayList<Integer> eqIdList)
    {
        int selEqID = 0;
        ArrayList<Integer> selEquipIdList = new ArrayList<Integer>();

        // 選択済み設備IDの設定
        for( int i = 0 ; i <= allEquipIdList.size() ; i++ )
        {
            selEquipIdList.add( 0 );
        }

        for( int i = 0 ; i <= eqIdList.size() - 1 ; i++ )
        {
            selEqID = eqIdList.get( i );

            for( int j = 0 ; j <= allEquipIdList.size() - 1 ; j++ )
            {
                if ( selEqID == allEquipIdList.get( j ) )
                {
                    selEquipIdList.set( j, 1 );
                    break;
                }
            }
        }

        return selEquipIdList;
    }

    /**
     * CookieからホテナビIDを取得する
     * 
     * @param cookies クッキー
     * @return 指定された区分に該当する値
     */
    public static String getCookieLoginHotenavi(Cookie[] cookies)
    {
        String cookValue = "";
        String value = "";
        int sepalateIdx = 0;

        for( int i = 0 ; cookies != null && i < cookies.length ; i++ )
        {
            // ログインユーザID取得
            if ( cookies[i].getName().equals( "hhownuid" ) )
            {
                cookValue = cookies[i].getValue();
                // 区切り文字のIndexを取得
                sepalateIdx = cookValue.indexOf( ":" );
                value = cookValue.substring( 0, sepalateIdx );
                break;
            }
        }
        return value;
    }

    /**
     * 半角数字チェック処理
     * マイナスは許容しない。
     * 
     * @param orgNum チェック対象文字列
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean numCheck(String orgNum)
    {
        char cutData;
        if ( orgNum != null && orgNum.trim().length() > 0 )
        {
            for( int i = 0 ; i < orgNum.trim().length() ; i++ )
            {
                cutData = orgNum.charAt( i );
                if ( (cutData < '0' || cutData > '9') )
                {
                    return(false);
                }
            }
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 対象プランが予約済みか
     * 
     * @param hotelID ホテルID
     * @param planID プランID
     * @return true:予約済み、False:予約されていない
     */
    public static boolean isReservePlan(int hotelID, int planID) throws Exception
    {
        int cnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND status = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            prestate.setInt( 3, 1 );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.isReservePlan] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 指定期間内に予約済みデータがあるか
     * 
     * @param hotelID ホテルID
     * @param planID プランID
     * @param fromDate 対象期間開始日
     * @param toDate 対象期間終了日
     * @return true:予約済み、False:予約されていない
     */
    public static boolean isReservePlanTargetDate(int hotelID, int planID, int fromDate, int toDate) throws Exception
    {
        int cnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND reserve_date BETWEEN ? AND ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            prestate.setInt( 3, fromDate );
            prestate.setInt( 4, toDate );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.isReservePlanTargetDate] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * アルメックス社員かどうかの情報を返す
     * 
     * @param hotelID ホテルID
     * @param userID ユーザID
     * @return 1:アルメックス社員、0:
     * @throws Exception
     */
    public static int getImediaFlag(Connection connection, String hotelID, int userID)
    {
        int imediaFlag = 0;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            String sql = "SELECT imedia_user FROM owner_user WHERE hotelid = ? AND userid = ?";
            prestate = connection.prepareStatement( sql );
            prestate.setString( 1, hotelID );
            prestate.setInt( 2, userID );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                imediaFlag = result.getInt( "imedia_user" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getImediaFlag] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(imediaFlag);
    }

    /**
     * プレミアム先行予約日数の取得
     * 
     * @return プレミアム先行予約日数
     * @throws Exception
     */
    public static String getPremiumGoAheadDays()
    {
        String premiumDays = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            String sql = "SELECT val1 FROM hh_rsv_system_conf WHERE ctrl_id1 = ? AND ctrl_id2 = ?";
            prestate = connection.prepareStatement( sql );
            prestate.setInt( 1, CTRL_ID1_PREMIUM_GOAHEAD_DAYS );
            prestate.setInt( 2, CTRL_ID2_PREMIUM_GOAHEAD_DAYS );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                premiumDays = result.getString( "val1" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getPremiumGoAheadDays] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(premiumDays);
    }

    /**
     * アルメックス社員かどうかの情報を返す
     * Connectionはメソッドの中でClose
     * 
     * @param hotelID ホテルID
     * @param userID ユーザID
     * @return 1:アルメックス社員、0:
     * @throws Exception
     */
    public static int getImediaFlag(String hotelID, int userID)
    {
        int imediaFlag = 0;
        ResultSet result = null;
        PreparedStatement prestate = null;
        Connection connection = null;

        try
        {
            String sql = "SELECT imedia_user FROM owner_user WHERE hotelid = ? AND userid = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( sql );
            prestate.setString( 1, hotelID );
            prestate.setInt( 2, userID );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                imediaFlag = result.getInt( "imedia_user" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getImediaFlag] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(imediaFlag);
    }

    /**
     * 最新予約枝番取得処理
     * 
     * @param id ホテルID
     * @param reserveNo 予約番号
     * @return 予約枝番(最高値)
     */
    public static int getMaxRsvSubNo(int id, String reserveNo) throws Exception
    {
        int ret = 0;

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "select reserve_sub_no from hh_rsv_reserve_history where id = ? and reserve_no = ? order by reserve_no desc";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserveNo );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                ret = result.getInt( "reserve_sub_no" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getMaxRsvSubNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 指定日指定プランに予約があるか
     * 
     * @param hotelID ホテルID
     * @param planID プランID
     * @param caldate チェック対象日
     * @return true:予約あり、False:予約なし
     */
    public static boolean isExistsRsvPlanByDay(int hotelID, int planID, int caldate) throws Exception
    {
        int cnt = 0;
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND status = 1 ";
        query = query + "  AND reserve_date = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            prestate.setInt( 3, caldate );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.isExistsRsvPlanByDay] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 指定プランに予約があるか
     * 
     * @param hotelID ホテルID
     * @param planID プランID
     * @return true:予約あり、False:予約なし
     */
    public static boolean isExistsRsvPlan(int hotelID, int planID) throws Exception
    {
        int cnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND status = 1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.iisExistsRsvPlan] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * オーナーユーザー情報を取得
     * 
     * @param int userId ユーザーID
     * @param cookie[] cookies
     * @return オーナーユーザー情報が格納されているHashTable
     * @throws Exception
     */
    public static Hashtable<String, String> getOwnerUserData(int userId, Cookie[] cookies)
    {
        String hotenaviId = "";
        String mailAddr = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        Hashtable<String, String> retHash = new Hashtable<String, String>();

        try
        {
            // ホテナビID取得
            hotenaviId = getCookieLoginHotenavi( cookies );

            String sql = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( sql );
            prestate.setString( 1, hotenaviId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( result.getString( "mailaddr_pc" ) != null )
                {
                    mailAddr = result.getString( "mailaddr_pc" );
                }

                retHash.put( "mailAddr", mailAddr );
                retHash.put( "tel1", "" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getOwnerUserData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(retHash);
    }

    /**
     * ポイント初期値取得
     * 
     * @param int 検索区分(1:ポイント取得、2:ポイントコード取得、3:ポイント制限フラグ、4:範囲)
     * @return なし
     * @throws Exception
     */
    public static int getInitHapyPoint(int selKbn) throws Exception
    {
        int ret = 0;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            ret = getInitHapyPoint( connection, selKbn );
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getInitHapyPoint] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public static int getInitHapyPoint(Connection connection, int selKbn) throws Exception
    {
        String query = "";
        int ret = 0;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT add_point, code, limit_flag, available_range FROM hh_master_point WHERE kind = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, HAPYPOINT_24 );
            result = prestate.executeQuery();

            while( result.next() )
            {
                switch( selKbn )
                {
                    case 1:
                        // ポイント取得
                        ret = result.getInt( "add_point" );
                        break;

                    case 2:
                        // ポイントコード取得
                        ret = result.getInt( "code" );
                        break;

                    case 3:
                        // ポイント制限フラグ取得
                        ret = result.getInt( "limit_flag" );
                        break;

                    case 4:
                        // 範囲取得
                        ret = result.getInt( "available_range" );
                        break;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getInitHapyPoint] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * ホテル修正履歴データを追加する
     * 
     * @param id ：ホテルID
     * @param hotelId ：オーナーホテルID
     * @param userId ：ユーザID
     * @param editId ：修正項目
     * @param editSub ：修正項目（サブ）
     * @param memo ：メモ
     * @return
     * @throws Exception
     */
    public static boolean addAdjustmentHistory(int id, String hotelId, int userId, int editId, int editSub, String memo) throws Exception
    {
        boolean ret = false;
        DataHotelAdjustmentHistory dataAdjust;

        dataAdjust = new DataHotelAdjustmentHistory();
        dataAdjust.setId( id );
        dataAdjust.setHotelId( hotelId );
        dataAdjust.setUserId( userId );
        dataAdjust.setEditId( editId );
        dataAdjust.setEditSub( editSub );
        dataAdjust.setMemo( memo );
        dataAdjust.setInputDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dataAdjust.setInputTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

        // 履歴に追加する
        if ( dataAdjust.insertData() )
        {
            ret = true;
        }
        else
        {
            Logging.error( "[addAdjustmentHistory] Error dataAdjust" );
        }
        return(ret);
    }

    /**
     * CookieからログインユーザIDを取得する
     * 
     * @param cookies クッキー
     * @return ログインユーザID
     */
    public static int getCookieLoginUserId(Cookie[] cookies)
    {
        int ret = 0;
        String cookValue = "";
        String value = "";
        int sepalateIdx = 0;

        for( int i = 0 ; cookies != null && i < cookies.length ; i++ )
        {
            // ログインユーザID取得
            if ( cookies[i].getName().equals( "hhownuid" ) )
            {
                cookValue = cookies[i].getValue();
                // 区切り文字のIndexを取得
                sepalateIdx = cookValue.indexOf( ":" );
                // ユーザID取得
                value = cookValue.substring( sepalateIdx + 1, cookValue.trim().length() );
                ret = Integer.parseInt( value );
                break;
            }
        }
        return ret;
    }

    /**
     * 対象料金モードに予約受付中のデータが存在するか
     * 
     * @param hotelID ホテルID
     * @param planID プランID
     * @param chargeModeId 料金モードID
     * @return true:データあり、False:データなし
     */
    public static boolean existsRsvChargeMode(int hotelID, int planID, int chargeModeId) throws Exception
    {
        int cnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_rsv_reserve rsv ";
        query = query + "  INNER JOIN  hh_rsv_plan_charge pc ON rsv.id = pc.id AND rsv.plan_id = pc.plan_id ";
        query = query + "WHERE rsv.id = ? ";
        query = query + "  AND rsv.plan_id = ? ";
        query = query + "  AND pc.charge_mode_id = ? ";
        query = query + "  AND rsv.status = 1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, planID );
            prestate.setInt( 3, chargeModeId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.isExistsRsvChargeMode] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * チェックイン、チェックアウト時間の整合性チェック
     * チェックイン時間が24時を超えた場合のみチェック
     * 
     * @param int ciTime チェックイン時間
     * @param int coTime チェックアウト時間
     * @return true:チェックOK、false:チェックNG
     */
    public static boolean checkCiCoTime(int ciTime, int coTime)
    {
        boolean ret = true;
        int ciTimeh = 0;

        if ( ciTime >= 2400 )
        {

            ciTimeh = ciTime - 2400;
            // チェックイン時間、チェックアウト時間の比較
            if ( ciTimeh > coTime )
            {
                ret = false;
            }
        }

        return(ret);
    }

    /**
     * 締時刻取得
     * 
     * @param hotelId ホテルID
     * @return 締時刻
     */
    public static int getDeadLineTime(int hotelId)
    {
        int ret = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";

        try
        {
            query = "select deadline_time from hh_rsv_reserve_basic where id = ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            while( result.next() != false )
            {
                ret = result.getInt( "deadline_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getDeadLineTime] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 管理ホテルのチェック
     * 
     * @param hotelId ホテルID(ホテナビID)
     * @param userId ユーザID
     * @param id ホテルID(ハピホテ)
     * @return true:管理ホテル、false:管理外のホテル
     */
    public static boolean checkHotelID(String hotelId, int userId, int id) throws Exception
    {
        boolean ret = false;

        // 事務局かどうか
        if ( OwnerRsvCommon.getImediaFlag( hotelId, userId ) == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
        {
            // 事務局の場合
            ret = true;
            return(ret);
        }

        // 事務局以外の場合、担当ホテルのチェック
        if ( getHotelIDList( hotelId, userId, id ) == true )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * 管理者以外の場合、選択したホテルIDが管理ホテルかどうか
     * 
     * @param hotelId ホテルID(ホテナビID)
     * @param userId ユーザID
     * @param id ホテルID(ハピホテ)
     * @return true:管理ホテル、false:管理外のホテル
     */
    private static boolean getHotelIDList(String hotelId, int userId, int id) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;
        boolean ret = false;

        query = query + "SELECT COUNT(*) AS CNT ";
        query = query + "FROM hh_hotel_basic hb ";
        query = query + "   INNER JOIN owner_user_hotel uh ON hb.hotenavi_id = uh.accept_hotelid ";
        query = query + "WHERE uh.hotelid = ? ";
        query = query + "  AND uh.userid = ? ";
        query = query + "  AND hb.id = ? ";
        query = query + "  AND hb.rank >= 2 ";
        query = query + "ORDER BY hb.hotenavi_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            prestate.setInt( 3, id );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvMenu.getHotelIDList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 外国人向けプラン英語名取得
     * 
     * @param planName プラン名
     * @param consecutiveFlag 連泊フラグ
     * @param minStayNum 最小人数
     * @param maxStayNum 最大人数
     * @return
     * @throws Exception
     */
    public static String getFroeignPlanName(String planName, int consecutiveFlag, int minStayNum, int maxStayNum) throws Exception
    {
        return getFroeignPlanName( planName, consecutiveFlag, minStayNum, maxStayNum, "" );
    }

    /**
     * 外国人向けプラン英語名取得
     * 
     * @param planName プラン名
     * @param consecutiveFlag 連泊フラグ
     * @param minStayNum 最小人数
     * @param maxStayNum 最大人数
     * @return
     * @throws Exception
     */
    public static String getFroeignPlanName(String planName, int consecutiveFlag, int minStayNum, int maxStayNum, String planSubName) throws Exception
    {
        String planNameEn = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";

        // プランサブタイトル追加
        if ( !planSubName.equals( "" ) )
        {
            planNameEn = planSubName + "/";
        }

        // タイトル取得
        if ( StringUtils.isNotBlank( planName ) )
        {
            String[] id = planName.split( "," );
            connection = DBConnection.getConnection();

            try
            {
                for( int i = 0 ; i < id.length ; i++ )
                {
                    query = "SELECT  val2 nameEn";
                    query += " FROM hh_rsv_system_conf ";
                    query += " WHERE ctrl_id1 = 7 AND  ctrl_id2 = " + id[i];

                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    if ( result.next() != false )
                    {

                        if ( result.getString( "nameEn" ) != null )
                        {
                            planNameEn = planNameEn + result.getString( "nameEn" ) + ",";
                        }
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }
            }
            catch ( Exception e )
            {
                Logging.info( "foward Exception e=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }

        // 最後のカンマを削除
        if ( planNameEn.length() > 0 )
        {
            if ( planNameEn.substring( planNameEn.length() - 1 ).equals( "," ) )
            {
                planNameEn = planNameEn.substring( 0, planNameEn.length() - 1 );
            }
        }
        return planNameEn;
    }

    /**
     * 連泊予約の日付ごとの料金
     * 
     * @param id ホテルID
     * @param reserveNo 予約番号
     * @return
     * @throws Exception
     */
    public static String[][] getRsvEachAmountArr(int id, String reserve_no) throws Exception
    {

        ArrayList<String> ArrDate = new ArrayList<String>();
        ArrayList<String> ArrChargeTotal = new ArrayList<String>();

        String reserveNoMain = "";
        int chargeTotal = 0;
        int chargeTotalPre = 0;

        int rsvDateFrom = 0;
        int rsvDateTo = 0;
        int cnt = 0;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        reserveNoMain = reserve_no.substring( reserve_no.indexOf( "-" ) + 1 );

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";

        query += "SELECT * FROM newRsvDB.hh_rsv_reserve  ";
        query += " WHERE id = ? ";
        query += " AND reserve_no_main = ? ";
        query += " ORDER BY reserve_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserveNoMain );
            result = prestate.executeQuery();

            while( result.next() )
            {
                chargeTotal = result.getInt( "charge_total" );

                if ( chargeTotalPre == 0 || (chargeTotal == chargeTotalPre) )
                {
                    if ( cnt == 0 )
                    {
                        rsvDateFrom = result.getInt( "reserve_date" );
                    }
                    else
                    {
                        rsvDateTo = result.getInt( "reserve_date" );
                    }
                    cnt++;
                }
                else
                {
                    // 出力
                    if ( cnt == 1 )
                    {
                        ArrDate.add( DateEdit.formatDate( 7, rsvDateFrom ) );
                        ArrChargeTotal.add( currencyFormat.format( chargeTotalPre ) );
                    }
                    else
                    {
                        ArrDate.add( DateEdit.formatDate( 7, rsvDateFrom ) + "〜" + DateEdit.formatDate( 7, rsvDateTo ) );
                        ArrChargeTotal.add( currencyFormat.format( chargeTotalPre ) + "×" + cnt );
                    }
                    // セット
                    rsvDateFrom = result.getInt( "reserve_date" );
                    rsvDateTo = 0;
                    cnt = 1;
                }

                chargeTotalPre = chargeTotal;
            }

            // 出力
            if ( rsvDateFrom != 0 )
            {

                if ( cnt == 1 )
                {
                    ArrDate.add( DateEdit.formatDate( 7, rsvDateFrom ) );
                    ArrChargeTotal.add( currencyFormat.format( chargeTotalPre ) );
                }
                else
                {
                    ArrDate.add( DateEdit.formatDate( 7, rsvDateFrom ) + "〜" + DateEdit.formatDate( 7, rsvDateTo ) );
                    ArrChargeTotal.add( currencyFormat.format( chargeTotalPre ) + "×" + cnt );
                }
            }
            if ( ArrDate.isEmpty() )
            {
                return new String[0][0];
            }
            int len = ArrDate.size();
            String[][] eachAmount = new String[len][2];
            for( int index = 0 ; index < len ; index++ )
            {
                eachAmount[index][0] = ArrDate.get( index );
                eachAmount[index][1] = ArrChargeTotal.get( index );
            }
            return eachAmount;

        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getRsvEachAmountArr] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

    }

    /**
     * 連泊予約の日付ごとの料金
     * 
     * @param id ホテルID
     * @param reserveNo 予約番号
     * @return
     * @throws Exception
     */
    public static String getRsvEachAmount(int id, String reserve_no) throws Exception
    {
        String msg = "";
        String[][] eachAmount = getRsvEachAmountArr( id, reserve_no );
        int len = eachAmount.length;
        for( int index = 0 ; index < len ; index++ )
        {
            if ( index != 0 )
            {
                msg += "\n";
            }
            msg += eachAmount[index][0] + " " + eachAmount[index][1];
        }
        return msg;
    }

    /**
     * OTA用API呼出し
     * 
     * @param api
     * @param param
     * @throws Exception
     */
    public static void callOtaApiThread(final String api, final String param) throws Exception
    {

        ExecutorService service = Executors.newCachedThreadPool();

        try
        {
            service.submit( new Runnable(){

                @Override
                public void run()
                {
                    try
                    {
                        callOtaApi( api, param );
                    }
                    catch ( Exception e )
                    {
                        // TODO 自動生成された catch ブロック
                        e.printStackTrace();
                    }
                }
            } );
        }
        catch ( Exception e )
        {
            Logging.error( "message : " + e.getMessage(), e );
            throw e;
        }
        finally
        {
            service.shutdown();
        }

    }

    public static JSONObject callOtaApi(final String api, final String param) throws Exception
    {
        Logging.info( "[callOtaApi] api=" + api + " param=" + param, "OwnerRsvCommon" );

        String url = Url.getSiteconApilUrl() + api;
        ClientResponse response =
                getClient()
                        .resource( url )
                        .header( "User-Agent", "PMS-HappyHotel" )
                        .type( MediaType.APPLICATION_FORM_URLENCODED )
                        .post( ClientResponse.class, param );

        Logging.info( "[callOtaApi] Reason Phrase=" + response.getStatusInfo().getReasonPhrase() );
        Logging.info( "[callOtaApi] status=" + response.getStatus() );

        String s = response.getEntity( String.class );
        Logging.info( "[callOtaApi] entity=" + s );

        JSONObject data = JSONObject.fromObject( s );
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader( new InputStreamReader( response.getEntityInputStream() ) );
            Logging.info( "[callOtaApi] reader=" + reader );
            String detailedMessage = data.getString( "detailedMessage" );
            Logging.info( "callOtaApi sucsess:" + data.getBoolean( "success" ) + " message:" + data.getString( "message" ) );
            if ( !data.getBoolean( "success" ) )
            {
                System.out.println( "message : " + data.getString( "message" ) );
            }
        }
        catch ( Exception e )
        {
            data = JSONObject.fromString( "{success : false, msg : \"パラメータが正常に取得できませんでした。\"}" );
        }
        finally
        {
            if ( reader != null )
                reader.close();
        }

        return data;
    }

    /**
     * SSL通信用Clientを取得
     * 
     * @return
     */
    private static Client getClient()
    {
        DefaultClientConfig config = new DefaultClientConfig();
        config.getProperties().put(
                HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                new HTTPSProperties(
                        new HostnameVerifier()
                        {

                            @Override
                            public boolean verify(String hostname, SSLSession session)
                            {
                                // TODO 自動生成されたメソッド・スタブ
                                return true;
                            }
                        },
                        SSLUtil.context ) );
        return Client.create( config );
    }

    private static class SSLUtil
    {

        static SSLContext context;

        static
        {
            context = getSSLContext();
        }

        private static SSLContext getSSLContext()
        {

            SSLContext context = null;

            try
            {
                // 証明書情報　全て空を返す
                TrustManager[] tm = { new X509TrustManager()
                {

                    @Override
                    public X509Certificate[] getAcceptedIssuers()
                    {
                        // TODO 自動生成されたメソッド・スタブ
                        return null;
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                    {
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                    {
                    }
                } };

                context = SSLContext.getInstance( "SSL" );
                context.init( null, tm, null );

                HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){

                    @Override
                    public boolean verify(String arg0, SSLSession arg1)
                    {
                        return true;
                    }
                } );
            }
            catch ( Exception e )
            {
                Logging.error( "" );
            }

            return context;
        }
    }

    /**
     * 部屋番号がOTA用プランに使用されているかチェックを行う
     * 
     * @param hotelId
     * @param roomRank
     * @param roomSeq
     * @return boolean
     * @throws Exception
     */
    public static boolean chkOtaPlanSeq(int hotelId, int roomRank, int roomSeq) throws Exception
    {
        int count = 0;
        StringBuilder query = new StringBuilder();
        query.append( " SELECT count(*)  as count " );
        // プランデータ
        query.append( " FROM newRsvDB.hh_rsv_plan plan  " );
        // プラン部屋
        query.append( " INNER JOIN newRsvDB.hh_rsv_rel_plan_room plan_room " );
        query.append( "   ON plan.id=plan_room.id  " );
        query.append( "   AND plan.plan_id=plan_room.plan_id " );
        query.append( "   AND plan.plan_sub_id=plan_room.plan_sub_id " );

        query.append( " WHERE plan.id = ? " );
        query.append( "  AND plan.foreign_flag= ? " );
        query.append( "  AND plan.latest_flag = ? " );
        query.append( "  AND plan.plan_sales_status = ? " );
        query.append( "  AND plan.publishing_flag = ? " );
        query.append( "  AND plan.sales_end_date >= ? " );
        query.append( "  AND plan_room.seq = ? " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, EXT_OTA );
            prestate.setInt( i++, Constants.LATEST_FLAG_LATEST );
            prestate.setInt( i++, PLAN_SALES_STATUS_SALE );
            prestate.setInt( i++, PUBLISHING_FLAG_PUBLISH );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, roomSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( "count" );
                }
            }
            return count > 0;

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.chkOtaPlanSeq] Exception = " + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * OTA予約をしているホテルか
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean checkOtaHotel(int hotelId)
    {
        boolean ret = false;
        String query = "SELECT * FROM newRsvDB.hh_rsv_reserve_basic WHERE id = ? ";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( (result.getInt( "rsvota_date_due" ) != 0 || result.getInt( "rsvota_date_start" ) != 0) )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkOtaHotel] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ゲスト予約 メール送信
     * 
     * @param id ホテルID
     * @param reserveTempNo 仮予約ID
     * @param mailAddr メールアドレス
     * @param userId ユーザーID
     * @param carAf Lineセッション情報
     * @param mode 1:メールからreserve_guest_payment_go.jspへ遷移 2:メールからreserve_mobile_guest_payment_go.jspへ遷移
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean sendRsvGuestMail(int id, long reserveTempNo, String mailAddr, String userId, String carAf)
    {
        return sendRsvGuestMail( id, reserveTempNo, mailAddr, userId, carAf, 1 );
    }

    public static boolean sendRsvGuestMail(int id, long reserveTempNo, String mailAddr, String userId, String carAf, int mode)
    {
        boolean rtn = true;

        DataHhRsvSystemConfList sysConfList = new DataHhRsvSystemConfList();
        HashMap<String, String> map = sysConfList.getSystemConfMap( 13 ); // メール情報をhh_rsv_system_confから取得する。

        String MAIL_FROM = "mail.from";
        String MAIL_SUBJECT = "mail.subject";

        try
        {
            String encdata = URLEncoder.encode( "お問い合わせ", "Shift_JIS" );

            String url = "";
            if ( mode == 1 )
            {
                url = Url.getRsvUrl() + "/others/reserve_guest_payment_go.jsp?";
            }
            else
            {
                url = Url.getRsvUrl() + "/others/reserve_mobile_guest_payment_go.jsp?";
            }
            url += "token=" + ReserveGuestMailToken.issue( id, reserveTempNo );// パラメータをトークン化
            if ( CheckString.isvalidString( carAf ) )
            {
                url += "&_car-af=" + carAf;
            }
            Logging.info( "url=" + url, "OwnerRsvCommon.sendRsvGuestMail" );

            String body = "";
            body = "----------------------------------------------------------------------------\r\n";
            body += "このメールは、入力メールアドレス宛に自動的にお送りしています。\r\n";
            body += "---------------------------------------------------------------------------\r\n";
            body += "まだ予約は確定していません。 下記のURLをクリックして支払画面にお進みください。\r\n";
            body += url;
            body += "\r\n\r\nご注意\r\n";
            body += "■本メールのURLの有効期限は10分以内となります。" + "\r\n";
            body += "■本メールにお心当たりのない場合は、本メールの破棄をお願いいたします。" + "\r\n";
            body += "\r\n";
            body += "お問い合わせ" + "\r\n";
            body += "mailto:" + map.get( MAIL_FROM ) + "?subject=" + encdata + "\r\n";
            body += "\r\n";
            body += "ハッピー・ホテルはUSEN-NEXTグループの株式会社アルメックスが運営する" + "\r\n";
            body += "レジャーホテル検索サイトです。" + "\r\n";
            body += "\r\n";
            body += "ハッピー・ホテルURL" + "\r\n";
            body += Url.getUrl() + "" + "\r\n";

            // body = new String( body.getBytes( "Shift_JIS" ) );
            // 文字化けするので最後に空白追加
            body += " ";

            // メール送信を行う
            SendMail.send( map.get( MAIL_FROM ), mailAddr, map.get( MAIL_SUBJECT ), body );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.sendRsvGuestMail] Exception=" + e.toString(), e );
            rtn = false;
        }

        return rtn;

    }
}
