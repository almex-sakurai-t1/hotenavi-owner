package jp.happyhotel.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.servlet.http.Cookie;

import jp.happyhotel.data.DataHhRsvCancelpattern;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataMasterCity;
import jp.happyhotel.data.DataMasterPref;
import jp.happyhotel.data.DataMasterZip;
import jp.happyhotel.data.DataRsvDayCharge;
import jp.happyhotel.data.DataRsvHotelCalendar;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvPlanCharge;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataRsvReserveWork;
import jp.happyhotel.data.DataRsvRoom;
import jp.happyhotel.data.DataRsvRoomRemainder;
import jp.happyhotel.reserve.FormReserveOptionSub;
import jp.happyhotel.reserve.FormReserveOptionSubImp;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.search.SearchRsvPlanDao;
import jp.happyhotel.user.UserRsvBasicInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 予約情報共通クラス
 */
public class ReserveCommon implements Serializable
{

    private static final long  serialVersionUID                    = -7283272162016948586L;

    // ▼ 予約スキーマ
    public static final String SCHEMA_NEWRSV                       = "newRsvDB";
    public static final String SCHEMA_OLDRSV                       = "hotenavi";

    // ▼ ユーザー区分
    public static final String USER_KBN_USER                       = "u";                             // ユーザー
    public static final String USER_KBN_OWNER                      = "o";                             // オーナー

    // ▼ 遷移状態
    public static final String MODE_INS                            = "I";                             // 新規
    public static final String MODE_UPD                            = "U";                             // 修正
    public static final String MODE_DETL                           = "D";                             // 詳細
    public static final String MODE_CANCEL                         = "C";                             // キャンセル
    public static final String MODE_RAITEN                         = "R";                             // 来店確認
    public static final String MODE_VIEW                           = "V";                             // プレビュー
    public static final String MODE_HISTORY                        = "H";                             // 履歴
    public static final String MODE_UNDO_CANCEL                    = "UC";                            // キャンセルの取り消し
    public static final String MODE_USAGE_DETL                     = "UD";                            // 利用明細

    // ▼予約状況ステータス
    public static final int    RSV_STATUS_UKETUKE                  = 1;                               // 1:受付
    public static final int    RSV_STATUS_ZUMI                     = 2;                               // 2:利用済み
    public static final int    RSV_STATUS_CANCEL                   = 3;                               // 3:キャンセル

    // ▼予約決済方法
    public static final int    PAYMENT_CREDIT                      = 1;                               // 1:クレジットカード払い（オンライン）
    public static final int    PAYMENT_ONSITE                      = 2;                               // 2:ホテル現地払い

    // ▼決済ステータス
    public static final int    PAYMENT_STATUS_SETTLED              = 1;                               // 1:決済済み
    public static final int    PAYMENT_STATUS_UNSETTLED            = 2;                               // 2:未決済

    // ▼ノーショー申告有無
    public static final int    NOSHOW_FALSE                        = 0;                               // 0:No-Show 申告なし
    public static final int    NOSHOW_TRUE                         = 1;                               // 1:No-Show 申告あり

    // ▼受付状況マーク
    public static final String RSV_ON_MARK                         = "○";
    public static final String RSV_OFF_MARK                        = "×";
    public static final String RSV_IMPOSSIBLE_MARK                 = "－";
    public static final String RSV_PREMIUM_MARK                    = "☆";

    // ▼依頼メール区分
    public static final int    MAIL_REQ_CANCELRSV                  = 3;                               // 3:予約取消
    public static final int    MAIL_REQ_NOSHOW                     = 4;                               // 4:No-Show
    public static final int    MAIL_REQ_REMINDAR                   = 5;                               // 5:メールリマインダー
    public static final int    MAIL_REQ_HOTEL_NewRSV               = 11;                              // 13:ホテル側新規予約
    public static final int    MAIL_REQ_HOTEL_UpdateRSV            = 12;                              // 13:ホテル側予約変更
    public static final int    MAIL_REQ_HOTEL_CANCELRSV            = 13;                              // 13:ホテル側予約取消
    public static final int    MAIL_REQ_HOTEL_NOSHOW               = 14;                              // 14:ホテル側No-Show

    // ▼端末区分
    public static final int    TERM_KIND_PC                        = 1;                               // PC
    public static final int    TERM_KIND_MOBILE                    = 2;                               // 携帯

    // ▼入力チェック用区分
    public static final int    INP_CHECK_PC                        = 1;                               // 1:PC版の入力チェック
    public static final int    INP_CHECK_PLAN_M                    = 2;                               // 2:プラン申込(携帯)
    public static final int    INP_CHECK_MOBILE1                   = 3;                               // 3:個人入力(携帯)
    public static final int    INP_CHECK_MOBILE2                   = 4;                               // 4:個人入力2(携帯)

    // ▼実績データ登録区分
    public static final int    RESULT_KIND_ZUMI                    = 1;                               // 1:利用済み
    public static final int    RESULT_KIND_NOSHOW                  = 2;                               // 2:no-show
    public static final int    RESULT_KIND_LIMIT                   = 3;                               // 3:期限切れ

    // ▼部屋残数ステータス
    public static final int    ROOM_STATUS_EMPTY                   = 1;                               // 空き
    public static final int    ROOM_STATUS_RSV                     = 2;                               // 予約

    // ▼依頼メール依頼区分
    public static final int    MAIL_NEW                            = 1;                               // 新規予約
    public static final int    MAIL_UPD                            = 2;                               // 予約変更
    public static final int    MAIL_DEL                            = 3;                               // 予約取消し
    public static final int    MAIL_RAITEN                         = 6;                               // 来店確認

    // ▼更新区分
    public static final int    UPDKBN_INSERT                       = 1;                               // 追加
    public static final int    UPDKBN_UPDATE                       = 2;                               // 更新
    public static final int    UPDKBN_CANCEL                       = 3;                               // キャンセル

    // ▼提供区分
    public static final int    OFFER_KIND_PLAN                     = 1;                               // プランでの提供
    public static final int    OFFER_KIND_ROOM                     = 2;                               // 部屋での提供

    // ▼ホテル駐車車場利用区分
    public static final int    PARKING_INPUT_COUNT                 = 1;                               // 入力有(台数あり)
    public static final int    PARKING_INPUT_NOCOUNT               = 2;                               // 入力有(台数指定なし)
    public static final int    PARKING_NO_INPUT                    = 3;                               // 入力無し
    public static final int    PARKING_INPUT_COUNT_HIROOF          = 4;                               // 入力有(台数ありハイルーフあり)
    public static final int    PARKING_INPUT_NOCOUNT_HIROOF        = 5;                               // 入力有(台数指定なしハイルーフあり)
    public static final int    PARKING_INPUT_COUNT_NOHIROOF        = 6;                               // 入力有(台数ありハイルーフ不可)
    public static final int    PARKING_INPUT_NOCOUNT_NOHIROOF      = 7;                               // 入力有(台数指定なしハイルーフ不可)

    // ▼駐車場利用区分
    public static final int    PRKING_USED_USE                     = 1;                               // 利用する

    // ▼imageファイルパス
    public static final String PLAN_IMAGE_CONF                     = "/etc/happyhotel/planimage.conf";
    public static final String IMAGE_KEY                           = "release.image.url";

    // ▼No-Showフラグ
    public static final int    NO_SHOW_ON                          = 1;                               // No-Show申告あり

    // ▼オプションフラグ
    public static final int    OPTION_USUAL                        = 0;                               // 通常オプション
    public static final int    OPTION_IMP                          = 1;                               // 必須オプション

    // ▼数量フラグ
    public static final int    QUANTITY_NEED                       = 0;                               // 必要
    public static final int    QUANTITY_NEED_NO                    = 1;                               // 不要

    // ▼予約システム設定フラグ
    public static final int    RESERVE_SYSTEMCONF_CHILDCHARGE      = 2;
    public static final int    RESERVE_SYSTEMCONF_CHAGE_START_TIME = 3;
    public static final int    RESERVE_SYSTEMCONF_CANCEL_POLICY    = 4;

    // ▼カード会社ビット値
    public static final int    CARDCOMPANY_VISA                    = 1;
    public static final int    CARDCOMPANY_MASTERCARD              = 2;
    public static final int    CARDCOMPANY_JCB                     = 4;
    public static final int    CARDCOMPANY_AMEX                    = 8;
    public static final int    CARDCOMPANY_DYNERS                  = 16;

    // ▼タッチ画像ビット値
    public static final int    TOUCH_FRONT                         = 1;
    public static final int    TOUCH_PANEL                         = 2;
    public static final int    TOUCH_FRONT_TEX                     = 4;
    public static final int    TOUCH_ROOM_TEX                      = 8;

    // ▼予約機能制限値設定ファイルパス
    public static final String RSV_LIMIT_CONF                      = "/etc/happyhotel/reserve.conf";
    public static final String LIMIT_KEY                           = "reserve.limitation.range";

    // ▼各種文言
    // 駐車場
    public static final String PARKING_NO_PARKNG                   = "駐車場の確保は承っておりません";
    public static final String PARKING_USE                         = "利用する";
    public static final String PARKING_NOT_USE                     = "利用しない";
    public static final String PARKING_CNT                         = "台";
    // 駐車情リスト
    public static final String PARKING_LIST_DEF                    = "▼選択ください";
    public static final String PARKING_LIST_ON                     = "利用する";
    public static final String PARKING_LIST_OFF                    = "利用しない";

    // メールリマインダー
    public static final String REMINDER_ON                         = "有";
    public static final String REMINDER_OFF                        = "無";
    public static final String REMINDER_MAIL                       = "ご登録メールアドレス以外のメールアドレス：";

    // オプションの数量
    public static final String OPT_SUB_NON                         = "不要";
    public static final String OPT_SUB_ZERO                        = "在庫なし";
    public static final String OPT_SUB_FIN                         = "受付終了しました";

    // 予約処理
    public static final String OPT_RSV_INS                         = "申込";
    public static final String OPT_RSV_UPD                         = "変更";
    public static final String OPT_RSV_DEL                         = "解除";

    // キャンセル区分
    public static final int    CANCEL_HOTEL                        = 0;                               // ホテルキャンセル
    public static final int    CANCEL_USER                         = 1;                               // ユーザキャンセル
    public static final int    CANCEL_ADMIN                        = 2;                               // 事務局キャンセル

    // クレジット料率 （%）
    public static final double CREDIT_RATES                        = 5.0;

    // 予約締め時刻
    public static final int    RSV_DEADLINE_TIME                   = 50000;

    // 予約区分
    public static final int    EXT_HAPIHOTE                        = 0;                               // ハピホテからの予約
    public static final int    EXT_LVJ                             = 1;                               // ラブインジャパンからの予約
    public static final int    EXT_OTA                             = 2;                               // OTAからの予約

    /**
     * rsvNoに6桁しかはいらないときのために、予約Noを変換する
     * 
     * @param hotelId ホテルID
     * @param rsvNo 予約番号
     * @return 変換後予約番号
     */
    public static String AdjustRsvNo(int hotelId, String rsvNo)
    {
        String adjustRsvNo = rsvNo;
        try
        {
            if ( rsvNo.length() == 6 )
            {
                adjustRsvNo = "A" + hotelId + "-" + rsvNo;
            }
        }
        catch ( Exception e )
        {
            adjustRsvNo = rsvNo;
        }

        return(adjustRsvNo);
    }

    /**
     * 都道府県IDのList取得
     * 
     * @param なし
     * @return 都道府県IDのArrayList
     */
    public ArrayList<Integer> getPrefIdList() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> prefIdList = new ArrayList<Integer>();

        query = "SELECT pref_id FROM hh_master_pref ORDER BY pref_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result == null )
            {
                throw new Exception( "都道府県情報 NotFound" );
            }

            while( result.next() )
            {
                prefIdList.add( result.getInt( "pref_id" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getPrefIdList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(prefIdList);
    }

    /**
     * ホテルに紐づく部屋数取得
     * 
     * @param iD ホテルID
     * @return 件数
     */
    public int getRoomCnt(int Id) throws Exception
    {
        // 変数定義
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT COUNT(*) AS COUNT ";
        query = query + "FROM hh_hotel_roomrank hrr ";
        query = query + "  LEFT JOIN hh_hotel_room_more hr ON hrr.id = hr.id AND hrr.room_rank = hr.room_rank ";
        query = query + "     LEFT JOIN hh_rsv_room rsvroom ON hr.id = rsvroom.id and hr.seq = rsvroom.seq ";
        query = query + "WHERE hrr.id = ? ";
        query = query + "  AND (hr.disp_flag = 0 or hr.disp_flag = 1) ";
        query = query + "  AND hrr.room_rank <> 0 ";
        query = query + "  AND (hrr.disp_index BETWEEN 1 AND 98) ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() == true )
                {
                    ret = result.getInt( "Count" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getRoomCnt] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 都道府県名のリスト取得
     * 
     * @param なし
     * @return 都道府県名のArrayList
     */
    public ArrayList<String> getPrefNmList() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> prefNmList = new ArrayList<String>();

        query = "SELECT name FROM hh_master_pref ORDER BY pref_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result == null )
            {
                throw new Exception( "都道府県情報 NotFound" );
            }

            while( result.next() )
            {
                prefNmList.add( result.getString( "name" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getPrefNmList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(prefNmList);
    }

    /**
     * 市区町村IDのリスト取得
     * 
     * @param なし
     * @return 市区町村IDのArrayList
     */
    public ArrayList<Integer> getJisCdList(int prefId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> jisCdList = new ArrayList<Integer>();

        query = "SELECT jis_code FROM hh_master_city WHERE pref_id = ? ORDER BY jis_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefId );
            result = prestate.executeQuery();
            if ( result == null )
            {
                throw new Exception( "市区町村情報 NotFound" );
            }

            while( result.next() )
            {
                jisCdList.add( result.getInt( "jis_code" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getJisCdList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(jisCdList);
    }

    /**
     * 市区町村名取得
     * 
     * @param なし
     * @return 市区町村名のArrayList
     */
    public ArrayList<String> getJisNmList(int prefId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> jisNmList = new ArrayList<String>();

        query = "SELECT name FROM hh_master_city WHERE pref_id = ? ORDER BY jis_code";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefId );
            result = prestate.executeQuery();
            if ( result == null )
            {
                throw new Exception( "市区町村情報 NotFound" );
            }

            while( result.next() )
            {
                jisNmList.add( result.getString( "name" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveInfo.setJisNmList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(jisNmList);
    }

    /**
     * カード番号数値有効判定処理
     * 
     * @param cardNo カード番号
     * @return 処理結果(true→正常、false→異常)
     */
    public boolean CheckCardNo(String cardNo)
    {
        boolean ret = false;

        try
        {
            // 桁数が合っていれば数値のチェック
            if ( cardNo.length() >= 10 && cardNo.length() <= 16 )
            {
                ret = CheckString.numCheck( cardNo );
            }
        }
        catch ( Exception e )
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * カード有効期限数値有効判定処理
     * 
     * @param limitYear 期限(年)
     * @param limitMonth 期限(月)
     * @return 処理結果(true→正常、false→異常)
     */
    public boolean CheckCardLimit(String limitYear, String limitMonth)
    {
        boolean ret = false;
        int yaer = 0;
        int month = 0;

        try
        {
            // 両方の数値チェック
            if ( CheckString.numCheck( limitYear ) && CheckString.numCheck( limitMonth ) )
            {
                yaer = Integer.valueOf( limitYear );
                month = Integer.valueOf( limitMonth );
                // 月が存在するか判定
                if ( month > 0 && month <= 12 )
                {
                    // 期限が未来か判定
                    if ( (20 * 10000 + yaer * 100 + month) >= Integer.valueOf( DateEdit.getDate( 2 ) ) / 100 )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * 
     * 入力チェック
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @param mode 1:PC版の入力チェック、2:プラン申込(携帯)、3:個人入力(携帯)、4:個人入力2(携帯)
     * @return true:正常、false:異常
     */
    public boolean isInputCheck(FormReservePersonalInfoPC frm, int mode) throws Exception
    {
        boolean isResult = false;
        String errMsgPlan = "";
        String errMsgInfo1 = "";
        String errMsgInfo2 = "";
        String newErrMsg = "";

        // PC版予約入力、プラン申込画面の入力チェック
        if ( (mode == INP_CHECK_PC) || (mode == INP_CHECK_PLAN_M) )
        {
            errMsgPlan = inputCheckPlan( frm );
        }

        // PC版予約入力、個人情報入力1画面の入力チェック
        if ( (mode == INP_CHECK_PC) || (mode == INP_CHECK_MOBILE1) )
        {
            errMsgInfo1 = inputCheckPersonalInfoM1( frm );
        }

        // PC版予約入力、個人情報入力2画面の入力チェック
        if ( (mode == INP_CHECK_PC) || (mode == INP_CHECK_MOBILE2) )
        {
            errMsgInfo2 = inputCheckPersonalInfoM2( frm );
        }
        newErrMsg = newErrMsg + errMsgPlan + errMsgInfo1 + errMsgInfo2;

        frm.setErrMsg( newErrMsg );

        if ( newErrMsg.trim().length() == 0 )
        {
            isResult = true;
        }

        return(isResult);
    }

    /***
     * 子供料金定義設定取得
     * 
     * @param hotelid
     * @return 子供料金定義
     **/
    public String getChildChargeInfo(String hotelid)
    {
        String ret = "";

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT child_charge_info FROM hh_rsv_reserve_basic WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelid );
            result = prestate.executeQuery();
            if ( result != null && result.next() == true )
            {
                if ( result.getString( "child_charge_info" ) != null )
                {
                    ret = ConvertCharacterSet.convDb2Form( (CheckString.checkStringForNull( result.getString( "child_charge_info" ) )).trim() );
                }
                else
                {
                    ret = getDefaultChildChargeInfo();
                }
            }
            else
            {
                ret = getDefaultChildChargeInfo();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getDefaultChildChargeInfo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * 子供料金定義デフォルト設定取得
     * 
     * @return 子供料金定義デフォルト値
     **/
    private String getDefaultChildChargeInfo()
    {
        String ret = "";

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT val1 FROM hh_rsv_system_conf WHERE ctrl_id1 = ? and ctrl_id2 = 1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, RESERVE_SYSTEMCONF_CHILDCHARGE );
            result = prestate.executeQuery();
            if ( result != null && result.next() == true )
            {
                ret = ConvertCharacterSet.convDb2Form( (CheckString.checkStringForNull( result.getString( "val1" ) )).trim() );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getDefaultChildChargeInfo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * キャンセルポリシー設定取得
     * 
     * @return キャンセルポリシーデフォルト値
     **/
    public String getDefaultCancelPolicy()
    {
        String ret = "";

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT val3 FROM hh_rsv_system_conf WHERE ctrl_id1 = ? and ctrl_id2 = 1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, RESERVE_SYSTEMCONF_CANCEL_POLICY );
            result = prestate.executeQuery();
            if ( result != null && result.next() == true )
            {
                ret = CheckString.checkStringForNull( result.getString( "val3" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getDefaultCancelPolicy] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * 4号営業ホテル判定処理
     * 
     * @param hotelid ホテルID
     * @return 4号営業判定結果(true→4号営業ホテル,false→旅館業法)
     **/
    public boolean checkLoveHotelFlag(int hotelid)
    {
        boolean ret = false;

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "select company_type from hh_hotel_basic where id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelid );
            result = prestate.executeQuery();
            if ( result != null && result.next() == true )
            {
                int type = result.getInt( "company_type" );
                if ( type == DataHotelBasic.COMPANY_TYPE_LOVEHOTEL_FIX || type == DataHotelBasic.COMPANY_TYPE_LOVEHOTEL_NOTIFICATION_UNFIX ||
                        type == DataHotelBasic.COMPANY_TYPE_LOVEHOTEL_REPORT_UNFIX || type == DataHotelBasic.COMPANY_TYPE_LOVEHOTEL_UNFIX )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkLoveHotelFlag] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * オプション名称取得処理
     * 
     * @param id ホテルID
     * @param optionid オプションID
     * @return
     */
    public static String getOptionName(int id, int optionid)
    {
        String ret = "";
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "select option_name from hh_rsv_option where id = ? AND option_id = ? group by option_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, optionid );
            result = prestate.executeQuery();
            if ( result != null && result.next() == true )
            {
                ret = result.getString( "option_name" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getOptionName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * 部屋名称取得処理
     * 
     * @param id ホテルID
     * @param seq 管理番号
     * @return
     */
    public static String getRoomName(int id, int seq)
    {
        String ret = "";
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "select room_name from hh_hotel_room_more where id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null && result.next() == true )
            {
                ret = result.getString( "room_name" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getRoomName] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * 部屋画像パス取得処理
     * 
     * @param id ホテルID
     * @param seq 管理番号
     * @return
     */
    public static String getRoomImagePc(int id, int seq)
    {
        String ret = "";
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "select hotenavi_id, refer_name from hh_hotel_basic inner join hh_hotel_room_more on hh_hotel_basic.id = hh_hotel_room_more.id where hh_hotel_room_more.id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null && result.next() == true )
            {
                ret = existFile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getRoomImageList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * ファイル確認処理
     * 
     * @param hotenaviId ホテナビID
     * @param referName 参照ファイル名
     * @return
     */
    private static String existFile(String hotenaviId, String referName)
    {
        String filePath = "";
        File file;

        if ( referName.equals( "" ) == false )
        {
            filePath = "/hotenavi/" + hotenaviId + "/image/r" + referName + ".jpg";
            file = new File( "/happyhotel" + filePath );

            // 画像がなかったらnoImage画像を表示する
            if ( file.exists() == false )
            {
                filePath = "/common/images/noimage.jpg";
            }
        }
        else
        {
            filePath = "/common/images/noimage.jpg";
        }
        return(filePath);
    }

    /***
     * ノーショークレジット有効ホテル判定処理
     * 
     * @param hotelid ホテルID
     * @return 判定結果(true→有効,false→無効)
     **/
    public static boolean checkNoShowCreditHotel(int hotelid)
    {
        boolean ret = false;

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "select noshow_credit_flag from hh_rsv_reserve_basic where id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelid );
            result = prestate.executeQuery();
            if ( result != null && result.next() == true )
            {
                if ( result.getInt( "noshow_credit_flag" ) == 1 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkNoShowCreditHotel] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * PC予約入力、プラン申込画面の入力チェック
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return エラーメッセージ
     **/
    private String inputCheckPlan(FormReservePersonalInfoPC frm)
    {
        String errMsg = "";
        DataRsvReserveBasic data;
        int parking = 0;

        // 大人人数
        if ( frm.getSelNumAdult() == 0 )
        {
            // 大人人数が"0"の場合
            errMsg = errMsg + Message.getMessage( "warn.00002", "大人人数" ) + "<br />";
        }
        // 3人以上の時の人数判定
        if ( frm.getSelNumAdult() > 2 && (frm.getNumManList().size() > 0 || frm.getNumWomanList().size() > 0) && frm.getSelNumAdult() != (frm.getSelNumMan() + frm.getSelNumWoman()) )
        {
            // 人数が不一致
            errMsg = errMsg + Message.getMessage( "warn.00043" ) + "<br />";
        }
        else if ( frm.getManCountJudgeFlag() != 1 && (frm.getSelNumMan() > frm.getSelNumWoman()) )
        {
            // 男性比率が多い警告
            errMsg = errMsg + Message.getMessage( "warn.00044" ) + "<br />";
        }

        // 駐車場利用区分
        data = new DataRsvReserveBasic();
        data.getData( frm.getSelHotelID() );
        parking = data.getParking();
        switch( frm.getSelParkingUsedKbn() )
        {
            case 0:
                // 未選択の場合
                if ( parking != ReserveCommon.PARKING_NO_INPUT )
                {
                    errMsg = errMsg + Message.getMessage( "warn.00002", "お車のご利用" ) + "<br />";
                }
                break;

            case 1:
                // 利用する選択時
                if ( parking == ReserveCommon.PARKING_INPUT_COUNT || parking == ReserveCommon.PARKING_INPUT_COUNT_HIROOF || parking == ReserveCommon.PARKING_INPUT_COUNT_NOHIROOF )
                {
                    // 予約基本情報で「入力有(台数あり)」の場合
                    if ( frm.getSelParkingCount() == 0 )
                    {
                        // 利用台数が0台の場合はエラー
                        errMsg = errMsg + Message.getMessage( "warn.00016" ) + "<br />";
                    }
                    if ( frm.getSelHiRoofCount() > frm.getSelParkingCount() )
                    {
                        // ハイルーフ車数が全体の台数を上回ったら警告
                        errMsg = errMsg + Message.getMessage( "warn.00042" ) + "<br />";
                    }
                }
                break;

            default:
                // 利用しない選択時
                if ( frm.getSelParkingCount() != 0 )
                {
                    // 利用台数が0以外の場合はエラー
                    errMsg = errMsg + Message.getMessage( "warn.00017" ) + "<br />";
                }
                break;
        }

        // 到着予定時刻の編集
        if ( frm.getSelEstTimeArrival() == -1 )
        {
            errMsg = errMsg + Message.getMessage( "warn.00002", "到着予定時間" ) + "<br />";
        }

        // オプションチェック

        return(errMsg);
    }

    /***
     * PC版予約入力、個人情報入力1(携帯)の入力値チェック
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return エラーメッセージ
     **/
    private String inputCheckPersonalInfoM1(FormReservePersonalInfoPC frm) throws Exception
    {
        String errMsg = "";
        int ret = 0;
        boolean blnRetZIP = false;
        DataMasterPref dmp;
        DataMasterZip dmz = new DataMasterZip();

        // 予約者名(姓)
        if ( CheckString.onlySpaceCheck( frm.getLastName() ) )
        {
            // 予約者名(姓)が未入力の場合はエラー
            errMsg = errMsg + Message.getMessage( "warn.00001", "ご予約者氏名(姓)" ) + "<br />";
        }
        else
        {
            ret = LengthCheck( frm.getLastName().trim(), 64 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                errMsg = errMsg + Message.getMessage( "warn.00003", "ご予約者氏名(姓)", "32", "64" ) + "<br />";
            }
            if ( CheckNgWord.ngWordCheck( frm.getLastName().toString() ) )
            {
                // 予約者名(姓)に禁則文字がある場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00029", "ご予約者氏名(姓)" ) + "<br />";
            }
        }

        // 予約者名(名)
        if ( CheckString.onlySpaceCheck( frm.getFirstName() ) )
        {
            // 予約者名(名)が未入力の場合はエラー
            errMsg = errMsg + Message.getMessage( "warn.00001", "ご予約者氏名(名)" ) + "<br />";
        }
        else
        {
            ret = LengthCheck( frm.getFirstName().trim(), 64 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                errMsg = errMsg + Message.getMessage( "warn.00003", "ご予約者氏名(名)", "32", "64" ) + "<br />";
            }
            if ( CheckNgWord.ngWordCheck( frm.getFirstName().toString() ) )
            {
                // 予約者名(名)に禁則文字がある場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00029", "ご予約者氏名(名)" ) + "<br />";
            }
        }

        // 予約者名カナ(姓)
        if ( CheckString.onlySpaceCheck( frm.getLastNameKana() ) )
        {
            // 予約者名カナ(姓)が未入力の場合はエラー
            errMsg = errMsg + Message.getMessage( "warn.00001", "氏名フリガナ(姓)" ) + "<br />";
        }
        else
        {
            ret = LengthCheck( frm.getLastNameKana().trim(), 64 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                errMsg = errMsg + Message.getMessage( "warn.00003", "氏名フリガナ(姓)", "32", "64" ) + "<br />";
            }
            if ( !CheckString.katakanaCheck( frm.getLastNameKana() ) )
            {
                // 予約者名カナ(姓)が全角カナ以外の場合
                errMsg = errMsg + Message.getMessage( "warn.00008", "氏名フリガナ(姓)" ) + "<br />";
            }
            if ( CheckNgWord.ngWordCheck( frm.getLastNameKana().toString() ) )
            {
                // 予約者名(名)に禁則文字がある場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00029", "氏名フリガナ(姓)" ) + "<br />";
            }
        }

        // 予約者名カナ(名)
        if ( CheckString.onlySpaceCheck( frm.getFirstNameKana() ) )
        {
            // 予約者名カナ(名)が未入力の場合はエラー
            errMsg = errMsg + Message.getMessage( "warn.00001", "氏名フリガナ(名)" ) + "<br />";
        }
        else
        {
            ret = LengthCheck( frm.getFirstNameKana().trim(), 64 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                errMsg = errMsg + Message.getMessage( "warn.00003", "氏名フリガナ(名)", "32", "64" ) + "<br />";
            }
            if ( !CheckString.katakanaCheck( frm.getFirstNameKana() ) )
            {
                // 予約者名カナ(名)が全角カナ以外の場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00008", "氏名フリガナ(名)" ) + "<br />";
            }
            if ( CheckNgWord.ngWordCheck( frm.getFirstNameKana().toString() ) )
            {
                // 予約者名(名)に禁則文字がある場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00029", "氏名フリガナ(名)" ) + "<br />";
            }
        }

        // 郵便番号(3桁)
        if ( CheckString.onlySpaceCheck( frm.getZipCd3() ) )
        {
            // 郵便番号(3桁)が未入力の場合はエラー
            errMsg = errMsg + Message.getMessage( "warn.00001", "郵便番号(3桁)" ) + "<br />";
        }
        else
        {
            if ( !CheckString.numCheck( frm.getZipCd3() ) )
            {
                // 郵便番号(3桁)が数字でない場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00004", "郵便番号(3桁)" ) + "<br />";
            }
        }

        // 郵便番号(4桁)
        if ( CheckString.onlySpaceCheck( frm.getZipCd4() ) )
        {
            // 郵便番号(4桁)が未入力の場合はエラー
            errMsg = errMsg + Message.getMessage( "warn.00001", "郵便番号(4桁)" ) + "<br />";
        }
        else
        {
            if ( !CheckString.numCheck( frm.getZipCd4() ) )
            {
                // 郵便番号(4桁)が全角カナの場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00004", "郵便番号(4桁)" ) + "<br />";
            }
        }

        // 郵便番号の存在チェック
        if ( (!CheckString.onlySpaceCheck( frm.getZipCd3() )) && (!CheckString.onlySpaceCheck( frm.getZipCd4() )) )
        {
            blnRetZIP = dmz.getData( frm.getZipCd3() + frm.getZipCd4() );

            if ( !blnRetZIP )
            {
                // 存在しない郵便番号
                errMsg = errMsg + Message.getMessage( "warn.00007" ) + "<br />";
            }
        }

        // 都道府県
        if ( frm.getSelPrefId() == 0 )
        {
            errMsg = errMsg + Message.getMessage( "warn.00002", "都道府県名" ) + "<br />";
        }
        else
        {
            dmp = new DataMasterPref();
            dmp.getData( frm.getSelPrefId() );

            if ( !(dmz.getPrefName()).equals( dmp.getName() ) )
            {
                // 郵便番号からの都道府県名と選択した都道府県名が一致しない場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00005", "入力された郵便番号", "都道府県名" ) + "<br />";
            }
        }

        return(errMsg);
    }

    /***
     * PC版予約入力、個人情報入力2(携帯)の入力値チェック
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return エラーメッセージ
     **/
    private String inputCheckPersonalInfoM2(FormReservePersonalInfoPC frm) throws Exception
    {
        DataMasterZip dmz = new DataMasterZip();
        String errMsg = "";
        int ret = 0;

        // 住所２、３
        if ( frm.getSelJisCd() != 0 )
        {

            dmz.getData( frm.getZipCd3() + frm.getZipCd4() );

            if ( frm.getSelJisCd() != dmz.getJisCode() )
            {
                errMsg = errMsg + Message.getMessage( "warn.00005", "選択された市区町村", "郵便番号" ) + "<br />";
            }
            if ( CheckString.onlySpaceCheck( frm.getAddress3() ) )
            {
                // 住所２がNULLで住所３がNULLでない場合
                errMsg = errMsg + Message.getMessage( "warn.00001", "番地・建物名・部屋番号" ) + "<br />";
            }
            else
            {
                ret = LengthCheck( frm.getAddress3().trim(), 128 );
                if ( ret == 1 )
                {
                    // 桁数Overの場合
                    errMsg = errMsg + Message.getMessage( "warn.00003", "番地・建物名・部屋番号", "64", "128" ) + "<br />";
                }
                if ( CheckNgWord.ngWordCheck( frm.getAddress3().toString() ) )
                {
                    // 住所３に禁則文字がある場合はエラー
                    errMsg = errMsg + Message.getMessage( "warn.00029", "番地・建物名・部屋番号" ) + "<br />";
                }
            }
        }
        else
        {
            errMsg = errMsg + Message.getMessage( "warn.00002", "市区町村" ) + "<br />";
        }

        // 連絡先電話番号
        if ( CheckString.onlySpaceCheck( frm.getTel() ) )
        {
            errMsg = errMsg + Message.getMessage( "warn.00001", "連絡先電話番号" ) + "<br />";
        }
        else
        {
            ret = LengthCheck( frm.getTel().trim(), 255 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                errMsg = errMsg + Message.getMessage( "warn.00003", "連絡先電話番号", "32", "32" ) + "<br />";
            }
            ret = MinimumLengthCheck( frm.getTel().trim(), 10 );
            if ( ret == 1 )
            {
                // 桁数足りないの場合
                errMsg = errMsg + Message.getMessage( "warn.00037", "連絡先電話番号", "10" ) + "<br />";
            }
            if ( !CheckString.numCheck( frm.getTel() ) )
            {
                // 未入力でなく且つ数字でない場合
                errMsg = errMsg + Message.getMessage( "warn.00004", "連絡先電話番号" ) + "<br />";
            }
        }

        // その他メールアドレス
        if ( (frm.getRemainderMailAddr() != null) && (frm.getRemainderMailAddr().toString().length() != 0) )
        {
            ret = LengthCheck( frm.getRemainderMailAddr().trim(), 255 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                errMsg = errMsg + Message.getMessage( "warn.00003", "メールリマインダーのメールアドレス", "127", "255" ) + "<br />";
            }
            if ( !CheckString.mailaddrCheck( frm.getRemainderMailAddr() ) )
            {
                // メールアドレスとして正しくない場合
                errMsg = errMsg + Message.getMessage( "warn.00009", "メールリマインダーのメールアドレス" ) + "<br />";
            }
            if ( CheckNgWord.ngWordCheck( frm.getRemainderMailAddr().toString() ) )
            {
                // メールアドレスに禁則文字がある場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00029", "メールリマインダーのメールアドレス" ) + "<br />";
            }
        }

        // お客様要望事項
        if ( (frm.getDemands() != null) && (frm.getDemands().toString().length() != 0) )
        {
            ret = LengthCheck( frm.getDemands().trim(), 255 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                errMsg = errMsg + Message.getMessage( "warn.00003", "お客様要望事項", "127", "255" ) + "<br />";
            }
            if ( CheckNgWord.ngWordCheck( frm.getDemands().toString() ) )
            {
                // お客様要望事項に禁則文字がある場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00029", "お客様要望事項" ) + "<br />";
            }
        }

        // お客様への質問
        if ( CheckString.onlySpaceCheck( frm.getRemarks() ) && (frm.getRemarks().toString().length() != 0) )
        {
            errMsg = errMsg + Message.getMessage( "warn.00001", "ホテルからの質問" ) + "<br />";
        }
        else
        {
            if ( frm.getQuestionFlg() == 1 && CheckString.onlySpaceCheck( frm.getRemarks() ) && (frm.getRemarks().toString().length() == 0) )
            {
                errMsg = errMsg + Message.getMessage( "warn.00001", "ホテルからの質問" ) + "<br />";
            }
            ret = LengthCheck( frm.getRemarks().trim(), 255 );
            if ( ret == 1 )
            {
                // 桁数Overの場合
                errMsg = errMsg + Message.getMessage( "warn.00003", "ホテルからの質問", "127", "255" ) + "<br />";
            }
            if ( CheckNgWord.ngWordCheck( frm.getRemarks().toString() ) )
            {
                // お客様要望事項に禁則文字がある場合はエラー
                errMsg = errMsg + Message.getMessage( "warn.00029", "ホテルからの質問" ) + "<br />";
            }
        }

        return(errMsg);
    }

    /***
     * 文字列中の文字数を半角文字基準でチェックする
     * 
     * @param input チェック対象の文字列
     * @param length 入力可能長さ
     * @return チェック結果。0:未入力、1:チェックOK、99：チェックNG
     **/
    private int LengthCheck(String input, int length) throws Exception
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

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.LengthCheck] Exception=" + e.toString() );
            throw new Exception( e );
        }
        return ret;
    }

    /***
     * 文字列中の文字数を半角文字基準でチェックする（最小値）
     * 
     * @param input チェック対象の文字列
     * @param length 最低限必要な長さ
     * @return チェック結果。0:未入力、1:チェックOK、99：チェックNG
     **/
    private int MinimumLengthCheck(String input, int length) throws Exception
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
            else if ( valueLeng < length )
            {
                // 桁数が足りない
                ret = 1;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.MinimumLengthCheck] Exception=" + e.toString() );
            throw new Exception( e );
        }
        return ret;
    }

    /**
     * 対象ホテルID有効クレジットカード会社ビット演算値取得
     * 
     * @param hotelid ホテルID
     * @return カード会社ビット演算値
     */
    public int getCreditCardCompany(int hotelid)
    {
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "select card_company from hh_rsv_spid where id = ? and del_flag = 0";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelid );
            result = prestate.executeQuery();

            if ( result.next() != false )
            {
                ret = result.getInt( "card_company" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getCreditCardCompany] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 対象ホテルIDのSPID取得
     * 
     * @param hotelid ホテルID
     * @return SPID
     */
    public String getSpid(int hotelid)
    {
        String ret = "";
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "select spid from hh_rsv_spid where id = ? and del_flag = 0";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelid );
            result = prestate.executeQuery();

            if ( result.next() != false )
            {
                ret = result.getString( "spid" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getSpid] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 予約登録／変更共通チェック
     * 
     * @param rsvDate
     * @param rsvNo
     * @param seq
     * @param mode
     * @param userid
     * @return FormReserveSheetPCオブジェクト
     * @throws Exception
     */
    public FormReserveSheetPC chkDspMaster(FormReserveSheetPC frm) throws Exception
    {
        boolean blnRet;
        String errMsg = "";
        int offerkind = 2; // プランマスタ提供区分(初期値 2:部屋の提供)
        DataRsvPlan dataPlan = new DataRsvPlan();
        DataRsvReserve dataRsv;
        DataRsvReserveWork dataWork;
        UserRsvBasicInfo loginRsvUser = new UserRsvBasicInfo();
        int hotelId = 0;
        int planId = 0;
        String rsvNo = "";
        int seq = 0;
        String mode = "";
        String userId = "";
        int rsvDate = 0;
        int wkQuantity = 0;
        int remaindQuantity = 0;
        Calendar rsvCal = Calendar.getInstance();
        String nowDate = "";
        String optRsvMode = "";
        int workId = 0;

        hotelId = frm.getSelHotelId();
        planId = frm.getSelPlanId();
        rsvNo = frm.getRsvNo();
        seq = frm.getSeq();
        mode = frm.getMode();
        rsvDate = frm.getRsvDate();
        userId = frm.getUserId();
        workId = frm.getWorkId();

        // 予約停止ユーザかチェック
        if ( loginRsvUser.getRsvUserBasic( userId ) == true )
        {
            if ( loginRsvUser.checkRsvStopUser() == true )
            {
                errMsg = Message.getMessage( "warn.00045" ) + "<br />";
                frm.setErrMsg( errMsg );
                return(frm);
            }
        }

        // プラン情報の取得
        blnRet = dataPlan.getData( hotelId, planId );
        if ( blnRet != false )
        {
            // 提供区分の取得
            offerkind = dataPlan.getOfferKind();
        }

        // 残数チェック
        // 最新の残数を取得し予約可能か判断する
        if ( mode.equals( ReserveCommon.MODE_INS ) )
        {
            if ( offerkind == ReserveCommon.OFFER_KIND_ROOM )
            {
                if ( seq != 0 )
                {
                    // 部屋での提供の場合
                    if ( getRoomZan( hotelId, planId, seq, rsvDate ) == false )
                    {
                        // 残数なし
                        errMsg = Message.getMessage( "warn.00006" ) + "<br />";
                        frm.setErrMsg( errMsg );
                        return(frm);
                    }
                    // プランマスタ販売状況確認チェック
                    if ( checkRoomSalesFlag( hotelId, seq ) == false )
                    {
                        // 販売対象外の場合はエラー
                        frm.setErrMsg( errMsg );
                        errMsg = Message.getMessage( "warn.00013" ) + "<br />";
                        return(frm);
                    }
                }
            }
            else
            {
                if ( getRoomZan_NoRoom( hotelId, planId, rsvDate ) == false )
                {
                    // 残数なし
                    errMsg = Message.getMessage( "warn.00006" ) + "<br />";
                    frm.setErrMsg( errMsg );
                    return(frm);
                }
            }

            // 予約上限チェック
            if ( checkMaxRsvRommCnt( hotelId, planId, rsvDate, seq ) == false )
            {
                // 存在する場合はエラー
                errMsg = Message.getMessage( "warn.00014" ) + "<br />";
                frm.setErrMsg( errMsg );
                return(frm);
            }
        }

        // ユーザIDとカレンダーとの重複チェック
        if ( mode.equals( ReserveCommon.MODE_INS ) )
        {
            if ( checkReserveUserDup( userId, rsvDate ) == false )
            {
                // 存在する場合はエラー
                errMsg = Message.getMessage( "warn.00018" ) + "<br />";
                frm.setErrMsg( errMsg );
                return(frm);
            }
        }

        // 最新の残数を取得し予約可能か判断する
        if ( mode.equals( ReserveCommon.MODE_INS ) )
        {

            // 予約販売状況確認チェック
            if ( checkReserveBasicSalesFlag( hotelId ) == false )
            {
                // 存在する場合はエラー
                errMsg = Message.getMessage( "warn.00013" ) + "<br />";
                frm.setErrMsg( errMsg );
                return(frm);
            }

            // プランマスタ販売状況確認チェック
            if ( checkPlanSalesFlag( hotelId, planId ) == false )
            {
                // 存在する場合はエラー
                errMsg = Message.getMessage( "warn.00013" ) + "<br />";
                frm.setErrMsg( errMsg );
                return(frm);
            }
            // ホテルカレンダ販売状況確認チェック
            if ( checkHotelCalendarSalesFlag( hotelId, rsvDate ) == false )
            {
                // 販売中止はエラー
                errMsg = Message.getMessage( "warn.00013" ) + "<br />";
                frm.setErrMsg( errMsg );
                return(frm);
            }
        }

        if ( !mode.equals( ReserveCommon.MODE_INS ) )
        {
            // 変更時のみキャンセルチェック
            dataRsv = new DataRsvReserve();
            if ( dataRsv.getData( hotelId, rsvNo ) )
            {
                if ( dataRsv.getStatus() == 3 )
                {
                    // ステータスがキャンセルの場合はエラー
                    errMsg = Message.getMessage( "warn.00015" ) + "<br />";
                    frm.setErrMsg( errMsg );
                    return(frm);
                }
                else if ( dataRsv.getStatus() == 2 )
                {
                    // ステータスが来店確認済みの場合はエラー
                    errMsg = Message.getMessage( "warn.00028" ) + "<br />";
                    frm.setErrMsg( errMsg );
                    return(frm);
                }
            }
        }

        // 画面の戻るや進むでworkIdが死んでる場合
        if ( frm.getWorkId() > 0 && (mode.equals( ReserveCommon.MODE_INS ) || mode.equals( ReserveCommon.MODE_UPD )) )
        {
            dataWork = new DataRsvReserveWork();
            if ( dataWork.getReserveWork( hotelId, workId ) == false )
            {
                errMsg = Message.getMessage( "warn.00041", "予約入力情報" ) + "<br />";
                frm.setErrMsg( errMsg );
                return(frm);
            }
        }

        // 通常オプション残数チェック
        int rsvQuantity = 0;
        if ( !mode.equals( MODE_CANCEL ) )
        {
            for( int i = 0 ; i < frm.getCheckOptIdList().size() ; i++ )
            {

                // 画面で選択されている数量の取得
                wkQuantity = frm.getCheckQuantityList().get( i );

                // 予約データに登録してある数量の取得
                rsvQuantity = getRsvQuantity( frm.getSelHotelId(), frm.getRsvNo(), frm.getCheckOptIdList().get( i ) );

                // 現在のオプション残数取得
                remaindQuantity = getRemaindOption( frm.getSelHotelId(), frm.getRsvDate(), frm.getCheckOptIdList().get( i ) );
                if ( remaindQuantity == -1 )
                {
                    // 予約無し時は、1日の最大入力可能数を取得
                    remaindQuantity = getInpMaxQuantity( frm.getSelHotelId(), frm.getCheckOptIdList().get( i ) );
                }

                if ( (rsvQuantity + remaindQuantity) < wkQuantity )
                {
                    // 残数Overの場合はエラー
                    errMsg = Message.getMessage( "warn.00033", frm.getCheckOptNmList().get( i ) ) + "<br />";
                    frm.setErrMsg( errMsg );
                    return(frm);
                }
            }
        }

        // 更新、キャンセル、新規の場合、通常オプションの手仕舞い日チェック
        wkQuantity = 0;
        rsvQuantity = 0;

        // 現在日付を取得
        nowDate = DateEdit.getDate( 2 ) + DateEdit.getTime( 1 );
        boolean checkLimit = false;
        if ( frm.getUserKbn().equals( USER_KBN_USER ) )
        {
            // ユーザーの場合のみチェック
            if ( (mode.equals( MODE_INS )) || (mode.equals( MODE_UPD )) || (mode.equals( MODE_CANCEL )) )
            {
                for( int i = 0 ; i < frm.getCheckOptIdList().size() ; i++ )
                {
                    // 画面で選択されている数量の取得
                    wkQuantity = frm.getCheckQuantityList().get( i );

                    // 予約データに登録してある数量の取得
                    rsvQuantity = getRsvQuantity( frm.getSelHotelId(), frm.getRsvNo(), frm.getCheckOptIdList().get( i ) );

                    if ( mode.equals( MODE_INS ) )
                    {
                        // 新規追加
                        checkLimit = checkLimitDate( hotelId, frm, rsvCal, nowDate, frm.getCheckOptIdList().get( i ), wkQuantity );
                        optRsvMode = OPT_RSV_INS;
                    }
                    else if ( mode.equals( MODE_UPD ) )
                    {
                        // 更新時
                        checkLimit = checkLimitDate( hotelId, frm, rsvCal, nowDate, frm.getCheckOptIdList().get( i ), wkQuantity );
                        optRsvMode = OPT_RSV_UPD;
                    }
                    else if ( (mode.equals( MODE_CANCEL )) )
                    {
                        // キャンセル時
                        checkLimit = checkCancelLimitDate( hotelId, frm, rsvCal, nowDate, frm.getCheckOptIdList().get( i ) );
                        optRsvMode = OPT_RSV_DEL;
                    }

                    if ( checkLimit == false )
                    {
                        errMsg = Message.getMessage( "warn.00034", frm.getCheckOptNmList().get( i ), optRsvMode ) + "<br />";
                        frm.setErrMsg( errMsg );
                        return(frm);
                    }
                }
            }
        }

        frm.setErrMsg( errMsg );
        return frm;
    }

    /**
     * 新規追加、更新時の手仕舞い日チェック
     * 
     * @param hotelId ホテルID
     * @param frm FormReserveSheetPC オブジェクト
     * @param rsvCal 予約日
     * @param nowDate 本日
     * @return true:チェックOK、false:チェックNG
     */
    private boolean checkLimitDate(int hotelId, FormReserveSheetPC frm, Calendar rsvCal, String nowDate, int checkOptId, int wkQuantity) throws Exception
    {
        boolean ret = false;
        int rsvQuantity = 0;
        String limitDay = "";
        String limitDate = "";
        String limitTime = "";
        String rsvYear = "";
        String rsvMonth = "";
        String rsvDay = "";

        // 予約日をCalendarに変換
        rsvYear = Integer.toString( frm.getRsvDate() ).substring( 0, 4 );
        rsvMonth = Integer.toString( frm.getRsvDate() ).substring( 4, 6 );
        rsvDay = Integer.toString( frm.getRsvDate() ).substring( 6 );

        // 予約データに登録してある数量の取得
        rsvQuantity = getRsvQuantity( frm.getSelHotelId(), frm.getRsvNo(), checkOptId );

        if ( wkQuantity != rsvQuantity )
        {
            // 数量が変更されている場合
            // 対象オプションの手仕舞い日(期限日)取得
            rsvCal.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ), 0, 0, 0 );
            limitDay = getCancelLimitDate( hotelId, checkOptId, rsvCal );

            // 手仕舞い日の時間を取得
            limitTime = getCancelLimitTime( hotelId, checkOptId );
            limitDate = limitDay + limitTime;
            // 手仕舞い日と現在の日付を比較
            if ( nowDate.compareTo( limitDate ) > 0 )
            {
                return(ret);
            }
        }

        ret = true;
        return(ret);
    }

    /**
     * 手仕舞い日チェック
     * 
     * @param hotelId ホテルID
     * @param rsvDate 予約日
     * @param nowDate 本日
     * @param checkOptId オプションID
     * @return true:チェックOK、false:チェックNG
     */
    private boolean checkLimitDataNew(int hotelId, int rsvDate, String nowDate, int checkOptId) throws Exception
    {
        boolean ret = false;
        Calendar cal = Calendar.getInstance();
        String rsvYear = "";
        String rsvMonth = "";
        String rsvDay = "";
        String limitDay = "";
        String limitDate = "";
        String limitTime = "";

        if ( rsvDate > 0 )
        {
            rsvYear = Integer.toString( rsvDate ).substring( 0, 4 );
            rsvMonth = Integer.toString( rsvDate ).substring( 4, 6 );
            rsvDay = Integer.toString( rsvDate ).substring( 6 );

            // 対象オプションの手仕舞い日(期限日)取得
            cal.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ), 0, 0, 0 );
            limitDay = getCancelLimitDate( hotelId, checkOptId, cal );
            // 手仕舞い日の時間を取得
            limitTime = getCancelLimitTime( hotelId, checkOptId );
            limitDate = limitDay + limitTime;
            // 手仕舞い日と現在の日付を比較
            if ( nowDate.compareTo( limitDate ) > 0 )
            {
                return(ret);
            }
        }

        ret = true;
        return(ret);
    }

    /**
     * キャンセル時の手仕舞い日チェック
     * 
     * @param hotelId ホテルID
     * @param frm FormReserveSheetPC オブジェクト
     * @param rsvCal 予約日
     * @param nowDate 本日
     * @return true:チェックOK、false:チェックNG
     */
    private boolean checkCancelLimitDate(int hotelId, FormReserveSheetPC frm, Calendar rsvCal, String nowDate, int checkOptId) throws Exception
    {
        boolean ret = false;
        String limitDay = "";
        String limitDate = "";
        String limitTime = "";
        String rsvYear = "";
        String rsvMonth = "";
        String rsvDay = "";

        // 予約日をCalendarに変換
        rsvYear = Integer.toString( frm.getRsvDate() ).substring( 0, 4 );
        rsvMonth = Integer.toString( frm.getRsvDate() ).substring( 4, 6 );
        rsvDay = Integer.toString( frm.getRsvDate() ).substring( 6 );

        // 対象オプションの手仕舞い日(期限日)取得
        rsvCal.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ), 0, 0, 0 );
        limitDay = getCancelLimitDate( hotelId, checkOptId, rsvCal );

        // 手仕舞い日の時間を取得
        limitTime = getCancelLimitTime( hotelId, checkOptId );
        limitDate = limitDay + limitTime;

        // 手仕舞い日と現在の日付を比較
        if ( nowDate.compareTo( limitDate ) > 0 )
        {
            return(ret);
        }

        ret = true;
        return(ret);
    }

    /**
     * 部屋残数取得処理(部屋指定あり)
     * 
     * @param id ホテルID
     * @param planid プランID
     * @param seq 部屋番号
     * @param rsvdate 予約日
     * @return false:残数0、true:残数1
     */
    private boolean getRoomZan(int id, int planid, int seq, int rsvDate)
    {
        int intRet = 0;
        boolean blnRet = false;
        DataRsvRoomRemainder dataRoomRemainder = new DataRsvRoomRemainder();
        intRet = dataRoomRemainder.getRemainderCount( id, planid, seq, rsvDate );

        if ( intRet > 0 )
        {
            blnRet = true;
        }
        return blnRet;
    }

    /**
     * 部屋残数取得 (部屋指定無し)
     * 
     * @param id ホテルID
     * @param planid プランID
     * @param rsvdate 予約日
     * @return false:残数0、true:残数1
     */
    private boolean getRoomZan_NoRoom(int id, int planid, int rsvDate)
    {
        int intRet = 0;
        boolean blnRet = false;
        DataRsvRoomRemainder dataRemainder = new DataRsvRoomRemainder();
        intRet = dataRemainder.getRemainderSumCount( id, planid, rsvDate );

        if ( intRet > 0 )
        {
            blnRet = true;
        }
        return blnRet;
    }

    /**
     * 予約データの重複チェック
     * ユーザIDと予約日を元に受付済のレコードの存在チェック（予約登録確認画面用)
     * 
     * @param userId ユーザID
     * @param rsvDate 予約日
     * @return false: 存在する true: 存在しない
     * @throws Exception
     */
    private boolean checkReserveUserDup(String userId, int rsvDate) throws Exception
    {
        boolean blnRet = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT COUNT(*) AS CNT FROM hh_rsv_reserve WHERE user_id = ? AND reserve_date = ?" +
                " AND status = 1 ";

        blnRet = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, rsvDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( result.getInt( "CNT" ) == 0 )
                    {
                        blnRet = true;
                    }
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkReserveDup] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return blnRet;
    }

    /**
     * 予約販売状況確認処理
     * 予約基本データの「販売フラグ」の値を元にチェックする
     * 
     * @param id ホテルID
     * @return false: 販売中止 true: 販売中
     * @throws Exception
     */
    private boolean checkReserveBasicSalesFlag(int id) throws Exception
    {
        boolean blnRet = false;
        DataRsvReserveBasic dataRsvBasic = new DataRsvReserveBasic();

        try
        {
            if ( dataRsvBasic.getData( id ) )
            {
                // データが存在する場合
                if ( dataRsvBasic.getSalesFlag() == 1 || dataRsvBasic.getPreOpenFlag() == 1 )
                {
                    // 販売中
                    blnRet = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkReserveBasicSalesFlag] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            dataRsvBasic = null;
        }
        return blnRet;
    }

    /**
     * ホテルカレンダ販売状況確認処理
     * 
     * @param id ホテルID
     * @param caldate yyyymmdd
     * @return false: 販売中止
     * @throws Exception
     */
    private boolean checkHotelCalendarSalesFlag(int id, int caldate) throws Exception
    {
        boolean blnRet = false;
        DataRsvHotelCalendar dataCal = new DataRsvHotelCalendar();

        try
        {
            if ( dataCal.getData( id, caldate ) )
            {
                if ( dataCal.getSales_flag() == 1 )
                {
                    blnRet = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkHotelCalendarSalesFlag] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            dataCal = null;
        }

        return blnRet;
    }

    /**
     * プランマスタ販売状況確認処理
     * プランマスタの「販売フラグ」および「掲載フラグ」の値を元にチェックする
     * 
     * @param id ホテルID
     * @param planid プランID
     * @return false: 販売中止若しくは未掲載 true: 販売中且つ掲載
     * @throws Exception
     */
    private boolean checkPlanSalesFlag(int id, int planid) throws Exception
    {
        boolean blnRet = false;
        DataRsvPlan dataPlan = new DataRsvPlan();

        try
        {
            if ( dataPlan.getData( id, planid ) )
            {
                // データが存在する場合
                if ( (dataPlan.getSalesFlag() == 1) && (dataPlan.getPublishingFlag() == 1) )
                {
                    // 販売中且つ掲載
                    blnRet = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkPlanSalesFlag] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            dataPlan = null;
        }
        return blnRet;
    }

    /**
     * 予約部屋マスタ販売状況確認処理
     * 予約部屋マスタの「販売フラグ」の値を元にチェックする
     * 
     * @param id ホテルID
     * @param seq 部屋番号
     * @return false: 販売中止 true: 販売中
     * @throws Exception
     */
    private boolean checkRoomSalesFlag(int id, int seq) throws Exception
    {
        boolean blnRet = false;
        DataRsvRoom dataRoom = new DataRsvRoom();
        blnRet = false;
        try
        {
            if ( dataRoom.getData( id, seq ) )
            {
                // データが存在する場合
                if ( dataRoom.getSalesFlag() == 1 )
                {
                    // 販売中
                    blnRet = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkRoomSalesFlag] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            dataRoom = null;
        }
        return blnRet;

    }

    /**
     * 予約上限数取得処理
     * 
     * @param id ホテルID
     * @param planid プランID
     * @return 予約上限数
     * @throws Exception
     */
    public static int getMaxQuantityPlan(int id, int planid) throws Exception
    {
        int ret = 0;
        DataRsvPlan dataPlan = new DataRsvPlan();

        try
        {
            // プランマスタより上限値を取得
            if ( dataPlan.getData( id, planid ) == true )
            {
                // 予約上限数の取得
                ret = dataPlan.getMaxQuantity();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getMaxQuantityPlan] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            dataPlan = null;
        }

        return(ret);
    }

    /**
     * 予約上限値確認処理
     * 予約データのうち受付済のﾚｺｰﾄﾞ件数とプランマスタの予約上限数とを比較する
     * 
     * @param id ホテルID
     * @param planid プランID
     * @param rsvDate 予約日
     * @param seq 部屋番号
     * @return false: 存在する true: 存在しない
     * @throws Exception
     */
    public boolean checkMaxRsvRommCnt(int id, int planid, int rsvDate, int seq) throws Exception
    {
        boolean blnRet = false;
        DataRsvPlan dataPlan = new DataRsvPlan();
        int intMaxQuantity = 0;
        int rsvCnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            // プランマスタより上限値を取得
            if ( dataPlan.getData( id, planid ) == true )
            {
                // 予約上限数の取得
                intMaxQuantity = dataPlan.getMaxQuantity();
            }

            if ( intMaxQuantity != 0 )
            {
                // ﾎﾃﾙID、ﾌﾟﾗﾝID、予約日を元に受付済の予約データの件数を取得する
                query = "SELECT COUNT(*) AS CNT FROM hh_rsv_reserve " +
                        " WHERE id = ? AND plan_id = ? AND reserve_date = ? AND status = 1 ";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, id );
                prestate.setInt( 2, planid );
                prestate.setInt( 3, rsvDate );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        rsvCnt = result.getInt( "CNT" );
                    }
                }

                // 部屋指定の場合
                if ( intMaxQuantity > rsvCnt )
                {
                    // 予約データの件数が上限数異なる場合はＯＫ
                    blnRet = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkMaxRsvRommCnt] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            dataPlan = null;
            DBConnection.releaseResources( result, prestate, connection );
        }
        return blnRet;
    }

    /**
     * 予約データステータスチェック
     * 
     * @param hotelId ホテルID
     * @param rsvNo 予約番号
     * @param oldSts 比較用ステータス
     * @return false:異なる true:同じ
     */
    public boolean checkStatus(int hotelId, String rsvNo, int oldSts)
    {
        boolean blnRet = false;
        DataRsvReserve dataRsv = new DataRsvReserve();

        if ( dataRsv.getData( hotelId, rsvNo ) )
        {
            if ( dataRsv.getStatus() == 1 )
            {
                // 受付の場合のみOK
                blnRet = true;
            }
        }

        dataRsv = null;
        return blnRet;
    }

    /**
     * 
     * ホテル情報取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    public FormReservePersonalInfoPC getHotelData(FormReservePersonalInfoPC frm)
    {
        String hotelName = "";
        String hotelAddr = "";
        String roomImg = "";
        DataHotelBasic data = new DataHotelBasic();

        data.getData( frm.getSelHotelID() );

        hotelName = ConvertCharacterSet.convDb2Form( data.getName() );
        hotelAddr = ConvertCharacterSet.convDb2Form( data.getPrefName().toString() ) +
                ConvertCharacterSet.convDb2Form( data.getAddress1().toString() ) +
                ConvertCharacterSet.convDb2Form( data.getAddress2().toString() ) +
                ConvertCharacterSet.convDb2Form( data.getAddress3().toString() );
        roomImg = data.getHotenaviId().toString();

        // フォームにセット
        frm.setHotelName( hotelName );
        frm.setHotelAddr( hotelAddr );
        frm.setRoomImgPath( roomImg );

        return(frm);
    }

    /**
     * 
     * ホテルの駐車場情報取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    public FormReservePersonalInfoPC getParking(FormReservePersonalInfoPC frm)
    {

        DataRsvReserveBasic data = new DataRsvReserveBasic();

        // 予約基本より駐車場情報を取得
        data.getData( frm.getSelHotelID() );

        frm.setHotelParking( data.getParking() );
        if ( frm.getSelParkingUsedKbn() <= 0 )
        {
            frm.setSelParkingUsedKbn( data.getParking() );
        }
        return(frm);
    }

    /**
     * 
     * 料金モードID取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    public FormReservePersonalInfoPC getDayChargeMode(FormReservePersonalInfoPC frm)
    {
        DataRsvDayCharge data = new DataRsvDayCharge();

        // 日別料金マスタより料金モードIDを取得
        data.getData( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelRsvDate() );
        frm.setChargeModeId( data.getChargeModeId() );

        return(frm);
    }

    /**
     * 
     * プラン情報取得
     * プラン名、プランPR、人数リスト、駐車場リストの作成
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    public FormReservePersonalInfoPC getPlanData(FormReservePersonalInfoPC frm) throws Exception
    {
        boolean ret = false;
        int maxAdult = 0;
        int maxChild = 0;
        int minAdult = 0;
        int minChild = 0;
        int maxMan = 0;
        int maxWoman = 0;
        int minMan = 0;
        int minWoman = 0;
        int manCountJudgeFlag = 0;
        int offerKind = 0;
        int stDay = 0;
        int edDay = 0;
        int minusStartDay = 0;
        int premiumStDay = 0;
        int freeStDay = 0;
        int premiumEdDay = 0;
        int freeEdDay = 0;
        int stTime = 0;
        int edTime = 0;
        int planStDay = 0;
        int planEdDay = 0;
        int requestFlag = 0;
        String planNm = "";
        String planPr = "";
        String remarks = "";
        String planImgPc = "";
        String planImgPath = "";
        String question = "";
        int questionFlag = 0;
        Properties config = new Properties();
        FileInputStream propfile = null;
        ArrayList<Integer> numAdultList = new ArrayList<Integer>();
        ArrayList<Integer> numChildList = new ArrayList<Integer>();
        ArrayList<Integer> numManList = new ArrayList<Integer>();
        ArrayList<Integer> numWomanList = new ArrayList<Integer>();
        ArrayList<Integer> parkingUsedKbnList = new ArrayList<Integer>();
        ArrayList<String> parkingUsedNmList = new ArrayList<String>();
        DataRsvPlan data = new DataRsvPlan();
        DataRsvRoomRemainder dataRoomRemainder = new DataRsvRoomRemainder();

        try
        {
            // プレミアム先行日数取得
            minusStartDay = Integer.parseInt( OwnerRsvCommon.getPremiumGoAheadDays() );
        }
        catch ( Exception e )
        {
        }

        // プランマスタより最大人数大人、最大人数子供を取得
        ret = data.getData( frm.getSelHotelID(), frm.getSelPlanID() );
        if ( ret )
        {
            maxAdult = data.getMaxNumAdult();
            maxChild = data.getMaxNumChild();
            minAdult = data.getMinNumAdult();
            minChild = data.getMinNumChild();
            minMan = data.getMinNumMan();
            maxMan = data.getMaxNumMan();
            minWoman = data.getMinNumWoman();
            maxWoman = data.getMaxNumWoman();
            manCountJudgeFlag = data.getManCountJudgeFlag();
            planNm = data.getPlanName();
            planPr = data.getPlanPr();
            remarks = data.getRemarks();
            offerKind = data.getOfferKind();
            planImgPc = data.getImagePc();
            stDay = data.getReseveStartDate();
            stTime = data.getReserveStartTime();
            edDay = data.getReserveEndDate();
            requestFlag = data.getRequestFlag();
            // プレミアムの値は保持
            premiumStDay = stDay;
            // 無料もプレミアムもスタート日は一緒なので同じで保持
            premiumEdDay = edDay;
            freeEdDay = edDay;
            // 無料会員は先行日数分削る(予約開始日の開始日が期限より前の場合は実際より1マイナスする)
            int minuscheckDay = 0;
            if ( Integer.parseInt( DateEdit.getTime( 1 ) ) < stTime )
            {
                minuscheckDay = 1;
            }
            if ( (stDay - minuscheckDay) - minusStartDay < minusStartDay && (stDay - minuscheckDay) - minusStartDay > 0 )
            {
                freeStDay = minusStartDay;
            }
            else if ( (stDay - minuscheckDay) - minusStartDay < minusStartDay && (stDay - minuscheckDay) - minusStartDay <= 0 )
            {
                // 最低限の日数のまま
                freeStDay = stDay;
            }
            else
            {
                freeStDay = stDay - minusStartDay;
            }
            // 無料会員は実際の開始日も無料ベース
            if ( !frm.isPaymemberFlg() )
            {
                stDay = freeStDay;
            }

            edTime = data.getReserveEndTime();
            planStDay = data.getSalesStartDay();
            planEdDay = data.getSalesEndDay();
            question = data.getQuestion();
            questionFlag = data.getQuestionFlag();
        }

        // 大人人数のリスト作成
        if ( maxAdult != 0 )
        {
            for( int j = minAdult ; j <= maxAdult ; j++ )
            {
                numAdultList.add( j );
            }
        }

        // 子供人数のリスト作成
        if ( maxChild != 0 )
        {
            for( int j = minChild ; j <= maxChild ; j++ )
            {
                numChildList.add( j );
            }
        }

        // 男女範囲
        if ( maxMan != 0 )
        {
            for( int j = minMan ; j <= maxMan ; j++ )
            {
                numManList.add( j );
            }

        }

        if ( maxWoman != 0 )
        {
            for( int j = minWoman ; j <= maxWoman ; j++ )
            {
                numWomanList.add( j );
            }

        }

        // 駐車場利用区分リスト作成
        for( int j = 0 ; j <= 2 ; j++ )
        {
            parkingUsedKbnList.add( j );
            switch( j )
            {
                case 0:
                    parkingUsedNmList.add( ReserveCommon.PARKING_LIST_DEF );
                    break;
                case 1:
                    parkingUsedNmList.add( ReserveCommon.PARKING_LIST_ON );
                    break;
                case 2:
                    parkingUsedNmList.add( ReserveCommon.PARKING_LIST_OFF );
                    break;
            }
        }
        frm.setRsvStartDatePremium( premiumStDay );
        frm.setRsvStartDateFree( freeStDay );
        frm.setPlanName( planNm );
        frm.setPlanPR( planPr );
        frm.setDispRemarks( remarks );
        frm.setDispRequestFlg( requestFlag );
        frm.setNumAdultList( numAdultList );
        frm.setNumChildList( numChildList );
        frm.setNumManList( numManList );
        frm.setNumWomanList( numWomanList );
        frm.setManCountJudgeFlag( manCountJudgeFlag );
        frm.setParkingUsedKbnList( parkingUsedKbnList );
        frm.setParkingUsedNmList( parkingUsedNmList );
        frm.setOfferKind( offerKind );
        frm.setRsvStartDate( stDay );
        frm.setRsvEndDate( edDay );
        frm.setRsvEndDateFree( freeEdDay );
        frm.setRsvEndDatePremium( premiumEdDay );
        frm.setRsvStartTime( stTime );
        frm.setRsvEndTime( edTime );
        frm.setSalesStartDay( planStDay );
        frm.setSalesEndDay( planEdDay );
        frm.setQuestionFlg( questionFlag );
        frm.setQuestion( question );
        // 部屋残数取得
        if ( frm.getSelSeq() == 0 )
        {
            frm.setRoomZanSuu( dataRoomRemainder.getRemainderSumCount( frm.getSelHotelID(), frm.getSelPlanID(), frm.getOrgReserveDate() ) );
        }
        else
        {
            frm.setRoomZanSuu( dataRoomRemainder.getRemainderCount( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSelSeq(), frm.getSelRsvDate() ) );
        }
        // プランイメージファイル格納先取得
        propfile = new FileInputStream( PLAN_IMAGE_CONF );
        config = new Properties();
        config.load( propfile );

        planImgPath = config.getProperty( IMAGE_KEY );
        propfile.close();
        frm.setPlanImagePc( planImgPath + frm.getSelHotelID() + "/" + planImgPc );

        return(frm);
    }

    /**
     * 
     * 部屋残数取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    public FormReservePersonalInfoPC getRoomZanSuu(FormReservePersonalInfoPC frm)
    {

        int zanSuu = 0;
        DataRsvRoomRemainder data = new DataRsvRoomRemainder();

        // 部屋残数より予約残数を取得
        if ( frm.getSeq() == 0 )
        {
            zanSuu = data.getRemainderSumCount( frm.getSelHotelID(), frm.getSelPlanID(), frm.getOrgReserveDate() );
        }
        else
        {
            zanSuu = data.getRemainderCount( frm.getSelHotelID(), frm.getSelPlanID(), frm.getSeq(), frm.getOrgReserveDate() );
        }

        frm.setRoomZanSuu( zanSuu );

        return(frm);
    }

    /**
     * 
     * 駐車場利用区分用コンボボックス設定
     * 
     */
    public FormReservePersonalInfoPC setParkingUsed(FormReservePersonalInfoPC frm)
    {
        ArrayList<Integer> usedKbnList = new ArrayList<Integer>();
        ArrayList<String> usedNmList = new ArrayList<String>();

        for( int j = 0 ; j <= 2 ; j++ )
        {
            usedKbnList.add( j );
            switch( j )
            {
                case 0:
                    usedNmList.add( ReserveCommon.PARKING_LIST_DEF );
                    break;
                case 1:
                    usedNmList.add( ReserveCommon.PARKING_LIST_ON );
                    break;
                case 2:
                    usedNmList.add( ReserveCommon.PARKING_LIST_OFF );
                    break;
            }
        }

        // フォームにセット
        frm.setParkingUsedKbnList( usedKbnList );
        frm.setParkingUsedNmList( usedNmList );

        return(frm);
    }

    /**
     * 
     * プラン別料金データからチェックイン、チェックアウト時間の取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @param adult 大人の人数
     * @param child 子供の人数
     * @param rsvDate 予約日
     * @return FormReservePersonalInfoPCオブジェクト
     */
    public FormReservePersonalInfoPC getCiCoTime(FormReservePersonalInfoPC frm, int adult, int child, int rsvDate)
    {
        SearchRsvPlanDao searchRsvPlan = new SearchRsvPlanDao();
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        NumberFormat nf2 = new DecimalFormat( "00" );
        DataRsvPlanCharge drpc = new DataRsvPlanCharge();
        boolean blnRet = false;
        String ciFrom = "";
        int roopCnt = 0;

        if ( rsvDate > 0 )
        {
            // 指定日あり
            frm.setSelRsvDate( rsvDate );
            if ( searchRsvPlan.getRsvPlanChargeSpecified( frm.getSelHotelID(), frm.getSelPlanID(), adult, child, rsvDate ) )
            {
                frm.setAdulTwoCharge( objNum.format( searchRsvPlan.getCharge() ) );
                frm.setCiTimeFromView( searchRsvPlan.getCheckin() );
                frm.setCiTimeToView( searchRsvPlan.getCheckinTo() );
                frm.setCoTimeView( searchRsvPlan.getCheckout() );
                frm.setLowestCharge( objNum.format( searchRsvPlan.getLowstCharge() ) );
                frm.setSelNumAdult( adult );
                frm.setSelNumChild( child );
                // 日別料金マスタより料金モードIDを取得
                frm = getDayChargeMode( frm );
                // プラン別料金データ取得
                if ( drpc.getData( frm.getSelHotelID(), frm.getSelPlanID(), frm.getChargeModeId() ) )
                {
                    int basePrice = drpc.getAdultTwoCharge();
                    int ci_from = drpc.getCiTimeFrom();
                    int disp_ci_from = ci_from;
                    int ci_to = drpc.getCiTimeTo();
                    int co = drpc.getCoTime();
                    int ciFromDate = 0;
                    int ciToDate = 0;
                    Calendar calendar = Calendar.getInstance();
                    Calendar checkInFrom = Calendar.getInstance();
                    Calendar checkInTO = Calendar.getInstance();
                    ArrayList<Integer> estTimeArrivalIdList = new ArrayList<Integer>();
                    ArrayList<String> estTimeArrivalValList = new ArrayList<String>();

                    if ( ci_from >= 240000 )
                    {
                        // チェックイン時間FROM
                        checkInFrom.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ) + 1, getTime( ci_from, 1 ), getTime( ci_from, 2 ) );
                    }
                    else
                    {
                        // チェックイン時間FROM
                        checkInFrom.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), getTime( ci_from, 1 ), getTime( ci_from, 2 ) );
                    }
                    // チェックイン時間TO
                    if ( ci_to >= 240000 )
                    {
                        checkInTO.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ) + 1, getTime( ci_to, 1 ), getTime( ci_to, 2 ) );
                    }
                    else
                    {
                        checkInTO.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), getTime( ci_to, 1 ), getTime( ci_to, 2 ) );
                    }

                    ciToDate = Integer.parseInt( new SimpleDateFormat( "yyyyMMdd" ).format( checkInTO.getTime() ) );

                    // チェックインTOが次の日か
                    boolean isNextDay = false;
                    ciFromDate = Integer.parseInt( new SimpleDateFormat( "yyyyMMdd" ).format( checkInFrom.getTime() ) );
                    if ( ciFromDate < ciToDate )
                    {
                        isNextDay = true;
                    }

                    estTimeArrivalIdList.add( -1 );
                    estTimeArrivalValList.add( PARKING_LIST_DEF );
                    do
                    {
                        ciFrom = "";
                        ciFromDate = Integer.parseInt( new SimpleDateFormat( "yyyyMMdd" ).format( checkInFrom.getTime() ) );
                        if ( (ciFromDate == ciToDate) && (isNextDay == true) )
                        {
                            // 翌日
                            ciFrom = (new SimpleDateFormat( "HHmm" ).format( checkInFrom.getTime() )) + "00";
                            disp_ci_from = 240000 + Integer.parseInt( ciFrom );
                            estTimeArrivalIdList.add( disp_ci_from );
                            // estTimeArrivalValList.add( "翌" + (new SimpleDateFormat( "HH:mm" ).format( checkInFrom.getTime() )) );
                            estTimeArrivalValList.add( nf2.format( disp_ci_from / 10000 ) + ":" + nf2.format( disp_ci_from / 100 % 100 ) );

                        }
                        else
                        {
                            ciFrom = (new SimpleDateFormat( "HHmm" ).format( checkInFrom.getTime() )) + "00";
                            disp_ci_from = Integer.parseInt( ciFrom );
                            if ( ci_from >= 240000 )
                            {
                                disp_ci_from = 240000 + disp_ci_from;
                                estTimeArrivalIdList.add( disp_ci_from );
                                estTimeArrivalValList.add( nf2.format( disp_ci_from / 10000 ) + ":" + nf2.format( disp_ci_from / 100 % 100 ) );
                            }
                            else
                            {
                                estTimeArrivalIdList.add( disp_ci_from );
                                estTimeArrivalValList.add( new SimpleDateFormat( "HH:mm" ).format( checkInFrom.getTime() ) );
                            }
                        }

                        checkInFrom.add( Calendar.MINUTE, 30 );

                        roopCnt++;
                    }
                    while( checkInFrom.compareTo( checkInTO ) <= 0 );

                    frm.setEstTimeArrivalIDList( estTimeArrivalIdList );
                    frm.setEstTimeArrivalValList( estTimeArrivalValList );
                    frm.setCoKind( drpc.getCoKind() );
                }
            }
        }
        else
        {
            // 指定日なし
            if ( searchRsvPlan.getRsvPlanCharge( frm.getSelHotelID(), frm.getSelPlanID(), adult, child ) )
            {
                frm.setAdulTwoCharge( objNum.format( searchRsvPlan.getLowstCharge() ) );
                frm.setChargeModeNameList( searchRsvPlan.getChargeModeNameList() );
                frm.setCiTimeList( searchRsvPlan.getCiList() );
                frm.setCiTimeToList( searchRsvPlan.getCiToList() );
                frm.setCoTimeList( searchRsvPlan.getCoList() );
                frm.setLowestCharge( objNum.format( searchRsvPlan.getLowstCharge() ) );
                frm.setMaxCharge( objNum.format( searchRsvPlan.getMaxCharge() ) );
                frm.setSelNumAdult( adult );
                frm.setSelNumChild( child );
                frm.setCoKind( 0 );

            }
        }

        return(frm);
    }

    /**
     * 
     * 指定プラン指定日の指定人数時料金
     * 
     * @param hotelid ホテルID
     * @param planid プランID
     * @param adult 大人の人数
     * @param child 子供の人数
     * @param rsvDate 予約日
     * @return 料金
     */
    public String getReserveCharge(int hotelid, int planid, int adult, int child, int rsvDate)
    {
        String ret = "";
        SearchRsvPlanDao searchRsvPlan = new SearchRsvPlanDao();
        NumberFormat objNum = NumberFormat.getCurrencyInstance();

        if ( rsvDate > 0 )
        {
            if ( searchRsvPlan.getRsvPlanChargeSpecified( hotelid, planid, adult, child, rsvDate ) )
            {
                ret = objNum.format( searchRsvPlan.getCharge() );
            }
        }

        return(ret);
    }

    /**
     * 
     * プラン別料金データからチェックイン、チェックアウト時間の取得
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    public FormReservePersonalInfoPC getCiCoTime(FormReservePersonalInfoPC frm)
    {
        boolean blnRet = false;
        int roopCnt = 0;
        int ci_from = 0;
        int ci_to = 0;
        int basePrice = 0;
        int co = 0;
        int coKind = 0;
        String ciFrom = "";
        int ciFromDate = 0;
        int ciToDate = 0;
        int adultTwoCharge = 0;
        Calendar calendar = Calendar.getInstance();
        Calendar checkInFrom = Calendar.getInstance();
        Calendar checkInTO = Calendar.getInstance();
        DataRsvPlanCharge drpc = new DataRsvPlanCharge();
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        ArrayList<Integer> estTimeArrivalIdList = new ArrayList<Integer>();
        ArrayList<String> estTimeArrivalValList = new ArrayList<String>();

        // 日別料金マスタより料金モードIDを取得
        frm = getDayChargeMode( frm );

        // プラン別料金データ取得
        blnRet = drpc.getData( frm.getSelHotelID(), frm.getSelPlanID(), frm.getChargeModeId() );
        adultTwoCharge = drpc.getAdultTwoCharge();

        if ( blnRet == false )
        {
            // フォームにセット
            frm.setAdulTwoCharge( objNum.format( adultTwoCharge ) );
            frm.setBaseChargeTotal( basePrice );
            frm.setCiTimeFromView( ConvertTime.convTimeStr( ci_from, 3 ) );
            frm.setCiTimeToView( ConvertTime.convTimeStr( ci_to, 3 ) );
            frm.setCiTimeFrom( ci_from );
            frm.setCiTimeTo( ci_to );
            frm.setCoTimeView( ConvertTime.convTimeStr( co, 3 ) );
            frm.setCoTime( co );
            frm.setEstTimeArrivalIDList( estTimeArrivalIdList );
            frm.setEstTimeArrivalValList( estTimeArrivalValList );

            return(frm);
        }

        basePrice = drpc.getAdultTwoCharge();
        ci_from = drpc.getCiTimeFrom();
        ci_to = drpc.getCiTimeTo();
        co = drpc.getCoTime();
        coKind = drpc.getCoKind();

        // チェックイン時間FROM
        checkInFrom.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), getTime( ci_from, 1 ), getTime( ci_from, 2 ) );

        // チェックイン時間TO
        if ( ci_to >= 240000 )
        {
            checkInTO.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ) + 1, getTime( ci_to, 1 ), getTime( ci_to, 2 ) );
        }
        else
        {
            checkInTO.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), getTime( ci_to, 1 ), getTime( ci_to, 2 ) );
        }

        ciToDate = Integer.parseInt( new SimpleDateFormat( "yyyyMMdd" ).format( checkInTO.getTime() ) );

        // チェックインTOが次の日か
        boolean isNextDay = false;
        ciFromDate = Integer.parseInt( new SimpleDateFormat( "yyyyMMdd" ).format( checkInFrom.getTime() ) );
        if ( ciFromDate < ciToDate )
        {
            isNextDay = true;
        }

        estTimeArrivalIdList.add( -1 );
        estTimeArrivalValList.add( PARKING_LIST_DEF );
        int estTimeArrival = 0;
        do
        {
            ciFrom = "";
            ciFromDate = Integer.parseInt( new SimpleDateFormat( "yyyyMMdd" ).format( checkInFrom.getTime() ) );
            if ( (ciFromDate == ciToDate) && (isNextDay == true) )
            {
                // 翌日
                ciFrom = (new SimpleDateFormat( "HHmm" ).format( checkInFrom.getTime() )) + "00";
                estTimeArrival = 240000 + Integer.parseInt( ciFrom );
                estTimeArrivalIdList.add( estTimeArrival );
                estTimeArrivalValList.add( "翌" + (new SimpleDateFormat( "HH:mm" ).format( checkInFrom.getTime() )) );

            }
            else
            {
                ciFrom = (new SimpleDateFormat( "HHmm" ).format( checkInFrom.getTime() )) + "00";
                estTimeArrival = Integer.parseInt( ciFrom );
                estTimeArrivalIdList.add( estTimeArrival );
                estTimeArrivalValList.add( new SimpleDateFormat( "HH:mm" ).format( checkInFrom.getTime() ) );
            }

            checkInFrom.add( Calendar.MINUTE, 30 );

            roopCnt++;
        }
        while( checkInFrom.compareTo( checkInTO ) <= 0 );

        // フォームにセット
        frm.setAdulTwoCharge( objNum.format( adultTwoCharge ) );
        frm.setBaseChargeTotal( basePrice );
        frm.setCiTimeFromView( ConvertTime.convTimeStr( ci_from, 3 ) );
        frm.setCiTimeToView( ConvertTime.convTimeStr( ci_to, 3 ) );
        frm.setCiTimeFrom( ci_from );
        frm.setCiTimeTo( ci_to );
        if ( coKind == 1 )
        {
            frm.setCoTimeView( "チェックインから" + DateEdit.formatTime( 6, co ) );
        }
        else
        {
            frm.setCoTimeView( ConvertTime.convTimeStr( co, 3 ) );
        }
        frm.setCoTime( co );
        frm.setEstTimeArrivalIDList( estTimeArrivalIdList );
        frm.setEstTimeArrivalValList( estTimeArrivalValList );
        frm.setCoKind( coKind );

        return(frm);
    }

    /**
     * 時間、分を取得
     * 
     * @param target 対象の時間(hhmmss)
     * @param selKbn 取得区分(1:時間、2：分)
     * @return 結果
     */
    private int getTime(int target, int selKbn)
    {
        int ret = 0;
        int hh = 0;
        String timeVal = "";

        timeVal = String.format( "%1$06d", target );

        if ( selKbn == 1 )
        {
            // 時間の取得
            hh = Integer.parseInt( timeVal.substring( 0, 2 ) );
            if ( hh >= 24 )
            {
                ret = hh - 24;
            }
            else
            {
                ret = hh;
            }

            return(ret);
        }

        // 分の取得
        ret = Integer.parseInt( timeVal.substring( 2, 4 ) );

        return(ret);
    }

    /**
     * 
     * FormReserveSheetPCに、FormReservePersonalInfoPCの内容をマージ
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @return FormReserveSheetPCオブジェクト
     */
    public FormReserveSheetPC margeFormSheetPC(FormReserveSheetPC frmSheet, FormReservePersonalInfoPC frmInfo)
    {
        frmSheet.setSelHotelId( frmInfo.getSelHotelID() );
        frmSheet.setSelPlanId( frmInfo.getSelPlanID() );
        frmSheet.setRsvNo( frmInfo.getReserveNo() );
        frmSheet.setSeq( frmInfo.getSelSeq() );
        frmSheet.setMode( frmInfo.getMode() );
        frmSheet.setRsvDate( frmInfo.getOrgReserveDate() );
        frmSheet.setUserId( frmInfo.getLoginUserId() );
        frmSheet.setRsvDate( frmInfo.getSelRsvDate() );

        return(frmSheet);
    }

    /**
     * 
     * 必須オプションのリスト取得
     * 
     * @param hotelId ホテルID
     * @param optionid オプションID
     * @return FormReserveOptionSubImpのArrayList
     */
    public static ArrayList<FormReserveOptionSubImp> getOptionSubList(int hotelId, int optionid)
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int orgOptId = 0;
        int newOptId = 0;
        FormReserveOptionSubImp frm;
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<String> optNmList = new ArrayList<String>();
        ArrayList<Integer> optSubIdList = new ArrayList<Integer>();
        ArrayList<String> optSubNmList = new ArrayList<String>();
        ArrayList<FormReserveOptionSubImp> frmOptSubList = new ArrayList<FormReserveOptionSubImp>();

        query = query + "SELECT option_id, option_sub_id, option_name, option_sub_name ";
        query = query + "FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";
        query = query + "  AND option_flag = 1 ";
        query = query + " ORDER BY option_sub_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, optionid );
            result = prestate.executeQuery();

            frm = new FormReserveOptionSubImp();
            while( result.next() )
            {
                optIdList.add( result.getInt( "option_id" ) );
                optNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                optSubIdList.add( result.getInt( "option_sub_id" ) );
                optSubNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_sub_name" ) ) ) );

                newOptId = result.getInt( "option_id" );
            }

            frm.setOptIdList( optIdList );
            frm.setOptNmList( optNmList );
            frm.setOptSubIdList( optSubIdList );
            frm.setOptSubNmList( optSubNmList );
            frmOptSubList.add( frm );

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getOptionSubList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return frmOptSubList;
    }

    /**
     * 
     * 必須オプションのリスト取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @return FormReserveOptionSubImpのArrayList
     */
    public ArrayList<FormReserveOptionSubImp> getOptionSubImp(int hotelId, int planId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int orgOptId = 0;
        int newOptId = 0;
        FormReserveOptionSubImp frm;
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<String> optNmList = new ArrayList<String>();
        ArrayList<Integer> optSubIdList = new ArrayList<Integer>();
        ArrayList<String> optSubNmList = new ArrayList<String>();
        ArrayList<FormReserveOptionSubImp> frmOptSubList = new ArrayList<FormReserveOptionSubImp>();

        query = query + "SELECT rel.option_id, rel.option_sub_id, opt.option_name, opt.option_sub_name ";
        query = query + "FROM hh_rsv_rel_plan_option rel ";
        query = query + " LEFT JOIN hh_rsv_option opt ON rel.id = opt.id AND rel.option_id = opt.option_id AND rel.option_sub_id = opt.option_sub_id ";
        query = query + "WHERE rel.id = ? ";
        query = query + "  AND rel.plan_id = ? ";
        query = query + "  AND opt.option_flag = 1 ";
        query = query + "ORDER BY disp_index ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            frm = new FormReserveOptionSubImp();
            while( result.next() )
            {
                orgOptId = result.getInt( "option_id" );

                if ( orgOptId == newOptId )
                {
                    // 同じオプションの場合、サブオプションのみ格納
                    optSubIdList.add( result.getInt( "option_sub_id" ) );
                    optSubNmList.add( result.getString( "option_sub_name" ) );
                    continue;
                }
                else if ( (orgOptId != newOptId) && (newOptId != 0) )
                {
                    // フォームにセット
                    frm.setOptIdList( optIdList );
                    frm.setOptNmList( optNmList );
                    frm.setOptSubIdList( optSubIdList );
                    frm.setOptSubNmList( optSubNmList );

                    frmOptSubList.add( frm );

                    optIdList = new ArrayList<Integer>();
                    optNmList = new ArrayList<String>();
                    optSubIdList = new ArrayList<Integer>();
                    optSubNmList = new ArrayList<String>();
                    frm = new FormReserveOptionSubImp();
                }

                optIdList.add( result.getInt( "option_id" ) );
                optNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                optSubIdList.add( result.getInt( "option_sub_id" ) );
                optSubNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_sub_name" ) ) ) );

                newOptId = result.getInt( "option_id" );
            }

            frm.setOptIdList( optIdList );
            frm.setOptNmList( optNmList );
            frm.setOptSubIdList( optSubIdList );
            frm.setOptSubNmList( optSubNmList );
            frmOptSubList.add( frm );

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getOptionSub] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frmOptSubList);
    }

    /**
     * 
     * 通常オプションのリスト取得
     * 
     * @param hotelId ホテルID
     * @param optionId オプションID
     * @return FormReserveOptionSubオブジェクト
     */
    public static FormReserveOptionSub getOptionSubList2(int hotelId, int optionId) throws Exception
    {
        String query = "";
        String optNm = "";
        String optCharge = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        FormReserveOptionSub frm = new FormReserveOptionSub();
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<String> optNmList = new ArrayList<String>();
        ArrayList<Integer> maxQuantityList = new ArrayList<Integer>();
        ArrayList<String> optRemarksList = new ArrayList<String>();
        ArrayList<Integer> unitPriceList = new ArrayList<Integer>();
        ArrayList<Integer> optStatusList = new ArrayList<Integer>();

        query = query + "SELECT option_id, option_name, input_max_quantity, option_charge ";
        query = query + "FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";
        query = query + "  AND option_flag = 0 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, optionId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frm = new FormReserveOptionSub();

                optIdList.add( result.getInt( "option_id" ) );
                optNm = ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) );
                optCharge = objNum.format( result.getInt( "option_charge" ) );
                optNm = optNm + "  (+" + optCharge + ")";
                optNmList.add( optNm );
                maxQuantityList.add( result.getInt( "input_max_quantity" ) );
                optRemarksList.add( "" );
                unitPriceList.add( result.getInt( "option_charge" ) );
            }

            frm.setOptIdList( optIdList );
            frm.setOptNmList( optNmList );
            frm.setOptRemarksList( optRemarksList );
            frm.setUnitPriceList( unitPriceList );
            frm.setOptStatusList( optStatusList );
            frm.setMaxQuantityList( maxQuantityList );

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getOptionSubList2] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frm);
    }

    /**
     * 
     * 通常オプションのリスト取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param rsvDate 予約日
     * @return FormReserveOptionSubオブジェクト
     */
    public FormReserveOptionSub getOptionSub(int hotelId, int planId, int rsvDate) throws Exception
    {
        int remaindOption = 0;
        String query = "";
        String optNm = "";
        String optCharge = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        FormReserveOptionSub frm = new FormReserveOptionSub();
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<String> optNmList = new ArrayList<String>();
        ArrayList<Integer> maxQuantityList = new ArrayList<Integer>();
        ArrayList<String> optRemarksList = new ArrayList<String>();
        ArrayList<Integer> unitPriceList = new ArrayList<Integer>();
        ArrayList<Integer> newMaxQuantityList = new ArrayList<Integer>();
        ArrayList<Integer> optStatusList = new ArrayList<Integer>();

        query = query + "SELECT rel.option_id, opt.option_name, opt.input_max_quantity, opt.option_charge ";
        query = query + "FROM hh_rsv_rel_plan_option rel ";
        query = query + " LEFT JOIN hh_rsv_option opt ON rel.id = opt.id AND rel.option_id = opt.option_id AND rel.option_sub_id = opt.option_sub_id ";
        query = query + "WHERE rel.id = ? ";
        query = query + "  AND rel.plan_id = ? ";
        query = query + "  AND opt.option_flag = 0 ";
        query = query + "ORDER BY disp_index ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frm = new FormReserveOptionSub();

                optIdList.add( result.getInt( "option_id" ) );
                optNm = ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) );
                optCharge = objNum.format( result.getInt( "option_charge" ) );
                optNm = optNm + "  (+" + optCharge + ")";
                optNmList.add( optNm );
                maxQuantityList.add( result.getInt( "input_max_quantity" ) );
                optRemarksList.add( "" );
                unitPriceList.add( result.getInt( "option_charge" ) );
                // 手仕舞い日チェック
                if ( checkLimitDataNew( hotelId, rsvDate, DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ), result.getInt( "option_id" ) ) )
                {
                    optStatusList.add( 0 );
                }
                else
                {
                    // 手仕舞い日切れは受付終了と表示
                    optStatusList.add( 1 );
                }
            }

            frm.setOptIdList( optIdList );
            frm.setOptNmList( optNmList );
            frm.setOptRemarksList( optRemarksList );
            frm.setUnitPriceList( unitPriceList );
            frm.setOptStatusList( optStatusList );

            // 通常オプションの再設定
            for( int i = 0 ; i < optIdList.size() ; i++ )
            {
                remaindOption = getRemaindOption( hotelId, rsvDate, optIdList.get( i ) );
                if ( remaindOption == -1 )
                {
                    newMaxQuantityList.add( maxQuantityList.get( i ) );
                }
                else
                {
                    newMaxQuantityList.add( remaindOption );
                }
            }
            frm.setMaxQuantityList( newMaxQuantityList );

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getOptionSub] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frm);
    }

    /**
     * 
     * 通常オプションのリスト取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param rsvDate 予約日
     * @return FormReserveOptionSubオブジェクト
     */
    public int getInpMaxQuantity(int hotelId, int optionId) throws Exception
    {
        int inpMaxQuantity = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT input_max_quantity FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";
        query = query + "  AND option_flag = 0 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, optionId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                inpMaxQuantity = result.getInt( "input_max_quantity" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getInpMaxQuantity] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(inpMaxQuantity);
    }

    /**
     * 
     * 予約時の数量取得
     * 
     * @param hotelId ホテルID
     * @param rsvNo 予約番号
     * @param optId オプションID
     * @return 予約時のオプション数量
     */
    public int getRsvQuantity(int hotelId, String rsvNo, int optId) throws Exception
    {
        int retQuantity = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT quantity FROM hh_rsv_rel_reserve_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND reserve_no = ? ";
        query = query + "  AND option_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, rsvNo );
            prestate.setInt( 3, optId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                retQuantity = result.getInt( "quantity" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getRsvQuantity] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(retQuantity);
    }

    /**
     * 
     * ホテル詳細駐車場あるなし
     * 
     * @param hotelId ホテルID
     * @return ホテル駐車場ステータス
     */
    public static int getHotelDetailParking(int hotelId) throws Exception
    {
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "select parking from hh_hotel_basic where id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                ret = result.getInt( "parking" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getHotelDetailParking] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 
     * 通常オプションの数量を再設定する。
     * 
     * @param hotelId ホテルID
     * @param rsvDate 予約日
     * @param frmOptId オプションID
     * @return 対象オプションの残数。予約データなしの場合は「-1」を返す。
     */
    public int getRemaindOption(int hotelId, int rsvDate, int frmOptId) throws Exception
    {
        int optId = 0;
        int quantitySum = 0;
        int maxQuantity = 0;
        int remaindsQuantity = 0;
        int retQuantity = -1;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<Integer> optNumList = new ArrayList<Integer>();
        ArrayList<Integer> maxQuantityList = new ArrayList<Integer>();
        ArrayList<Integer> inpMaxQuantityList = new ArrayList<Integer>();

        query = query + "SELECT rsvOpt.option_id, opt.max_quantity, opt.input_max_quantity, SUM(rsvOpt.quantity) as quantity ";
        query = query + "FROM hh_rsv_reserve rsv ";
        query = query + " LEFT JOIN hh_rsv_rel_reserve_option rsvOpt on rsv.id = rsvOpt.id and rsv.reserve_no = rsvOpt.reserve_no ";
        query = query + "    INNER JOIN hh_rsv_option opt on rsvOpt.id = opt.id and rsvOpt.option_id = opt.option_id and rsvOpt.option_sub_id = opt.option_sub_id ";
        query = query + "WHERE rsv.id = ? ";
        query = query + "  AND rsv.reserve_date = ? ";
        query = query + "  AND rsvOpt.option_id = ? ";
        query = query + "  AND opt.option_flag = 0 ";
        query = query + "  AND rsv.status = ? ";
        query = query + "GROUP BY rsvOpt.option_id, opt.max_quantity, opt.input_max_quantity ";
        query = query + "ORDER BY rsvOpt.option_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, rsvDate );
            prestate.setInt( 3, frmOptId );
            prestate.setInt( 4, RSV_STATUS_UKETUKE );
            result = prestate.executeQuery();

            while( result.next() )
            {
                optIdList.add( result.getInt( "option_id" ) );
                optNumList.add( result.getInt( "quantity" ) );
                maxQuantityList.add( result.getInt( "max_quantity" ) );
                inpMaxQuantityList.add( result.getInt( "input_max_quantity" ) );
            }

            if ( optIdList.size() == 0 )
            {
                // 予約無し
                return(retQuantity);

            }

            retQuantity = 0;
            for( int i = 0 ; i < optIdList.size() ; i++ )
            {
                optId = optIdList.get( i );

                if ( frmOptId != optId )
                {
                    continue;
                }

                // 1日あたりの上限数を超えているか
                quantitySum = optNumList.get( i );
                maxQuantity = maxQuantityList.get( i );

                if ( maxQuantity <= quantitySum )
                {
                    // 超えている場合は0
                    return(retQuantity);
                }

                // 超えていない場合、設定可能な数量を算出
                remaindsQuantity = maxQuantity - quantitySum;

                // 残数と1回の上限数との比較
                if ( remaindsQuantity > inpMaxQuantityList.get( i ) )
                {
                    // 1回の上限数のほうが少ない場合
                    retQuantity = inpMaxQuantityList.get( i );
                }
                else
                {
                    retQuantity = remaindsQuantity;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getRemaindOption] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(retQuantity);
    }

    /**
     * 
     * Ajaxで入力された郵便番号に該当する住所を取得
     * 
     * @param zip3 郵便番号3桁
     * @param zip4 郵便番号4桁
     * @return JSONArray
     */
    public JSONArray getZipAddress(String zip3, String zip4)
    {

        int selPrefId = 0;
        int selJisCd = 0;
        String selName = "";
        boolean setFlg = true;

        JSONObject jsnObj;
        JSONObject jsnAll = new JSONObject();
        JSONArray jsonPrefArray = new JSONArray();
        JSONArray jsonJisArray = new JSONArray();
        JSONArray jsonAllArray = new JSONArray();
        ArrayList<Integer> prefIdList = new ArrayList<Integer>();
        ArrayList<String> prefNmList = new ArrayList<String>();
        DataMasterZip dataZip = new DataMasterZip();
        DataMasterCity dataCity = new DataMasterCity();
        ArrayList<Integer> jisCdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            // 入力された郵便番号を取得
            if ( (zip3.trim().length() == 0) || (zip4.trim().length() == 0) )
            {
                // 郵便番号が未入力の場合、検索前の値を保持
                setFlg = false;
            }

            // 郵便番号存在チェック
            if ( existsZip( zip3.trim() + zip4.trim() ) == false )
            {
                // 存在しない場合、検索前の値を保持
                setFlg = false;
            }

            if ( setFlg == true )
            {
                // 対象の市区町村ID取得
                dataZip.getData( zip3.trim() + zip4.trim() );
                selJisCd = dataZip.getJisCode();

                // 都道府県ID取得
                dataCity.getData( selJisCd );
                selPrefId = dataCity.getPrefId();

                // 町名
                selName = dataZip.getAddress2Name();
            }

            // 都道府県リスト取得
            prefIdList = rsvCmm.getPrefIdList();
            prefNmList = rsvCmm.getPrefNmList();
            for( int i = 0 ; i < prefIdList.size() ; i++ )
            {
                jsnObj = new JSONObject();
                if ( i == 0 )
                {
                    jsnObj.put( "prefId", 0 );
                    jsnObj.put( "prefNm", ReserveCommon.PARKING_LIST_DEF );
                    jsnObj.put( "selPrefId", selPrefId );
                    jsonPrefArray.put( jsnObj );
                    jsnObj = new JSONObject();
                    jsnObj.put( "prefId", prefIdList.get( i ) );
                    jsnObj.put( "prefNm", prefNmList.get( i ) );
                }
                else
                {
                    jsnObj.put( "prefId", prefIdList.get( i ) );
                    jsnObj.put( "prefNm", prefNmList.get( i ) );
                }
                jsnObj.put( "selPrefId", selPrefId );
                jsonPrefArray.put( jsnObj );
            }
            jsnAll.put( "prefList", jsonPrefArray );
            jsonAllArray.put( jsnAll );

            // 市区町村リスト取得
            jisCdList = rsvCmm.getJisCdList( selPrefId );
            jisNMList = rsvCmm.getJisNmList( selPrefId );
            for( int i = 0 ; i < jisCdList.size() ; i++ )
            {
                jsnObj = new JSONObject();
                if ( i == 0 )
                {
                    jsnObj.put( "jisCd", 0 );
                    jsnObj.put( "jisNm", ReserveCommon.PARKING_LIST_DEF );
                    jsnObj.put( "selJisCd", selJisCd );
                    jsonPrefArray.put( jsnObj );
                    jsnObj = new JSONObject();
                    jsnObj.put( "jisCd", jisCdList.get( i ) );
                    jsnObj.put( "jisNm", jisNMList.get( i ) );
                }
                else
                {
                    jsnObj.put( "jisCd", jisCdList.get( i ) );
                    jsnObj.put( "jisNm", jisNMList.get( i ) );
                }
                jsnObj.put( "selJisCd", selJisCd );
                jsonJisArray.put( jsnObj );
            }
            jsnAll.put( "jisList", jsonJisArray );
            jsonAllArray.put( jsnAll );
            jsonAllArray.put( selName );
        }
        catch ( Exception e )
        {
            Logging.error( "Error ActionReserveAddressSearch.getZipAddress = " + e.toString() );
        }

        return(jsonAllArray);
    }

    /**
     * 
     * Ajaxで選択された都道府県コードに該当する市区町村取得処理
     * 
     * @param prefId 都道府県コード
     * @param jisCd 市区町村コード
     * @return JSONArray
     */
    public JSONArray getPrefAddress(int prefId, int jisCd)
    {

        JSONObject jsnObj;
        JSONObject jsnAll = new JSONObject();
        JSONArray jsonPrefArray = new JSONArray();
        JSONArray jsonJisArray = new JSONArray();
        JSONArray jsonAllArray = new JSONArray();
        ArrayList<Integer> prefIdList = new ArrayList<Integer>();
        ArrayList<String> prefNmList = new ArrayList<String>();
        ArrayList<Integer> jisCdList = new ArrayList<Integer>();
        ArrayList<String> jisNMList = new ArrayList<String>();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            // 都道府県リスト取得
            prefIdList = rsvCmm.getPrefIdList();
            prefNmList = rsvCmm.getPrefNmList();

            // jsnObj = new JSONObject();
            // jsnObj.put( "prefId", 0 );
            // jsnObj.put( "prefNm", ReserveCommon.PARKING_LIST_DEF );
            // jsnObj.put( "selPrefId", prefId );
            // jsonJisArray.put( jsnObj );

            for( int i = 0 ; i < prefIdList.size() ; i++ )
            {
                jsnObj = new JSONObject();
                if ( i == 0 )
                {
                    jsnObj.put( "prefId", 0 );
                    jsnObj.put( "prefNm", ReserveCommon.PARKING_LIST_DEF );
                    jsnObj.put( "selPrefId", prefId );
                    jsonPrefArray.put( jsnObj );
                    jsnObj = new JSONObject();
                    jsnObj.put( "prefId", prefIdList.get( i ) );
                    jsnObj.put( "prefNm", prefNmList.get( i ) );
                }
                else
                {
                    jsnObj.put( "prefId", prefIdList.get( i ) );
                    jsnObj.put( "prefNm", prefNmList.get( i ) );
                }
                jsnObj.put( "selPrefId", prefId );
                jsonPrefArray.put( jsnObj );
            }
            jsnAll.put( "prefList", jsonPrefArray );
            jsonAllArray.put( jsnAll );

            // 市区町村リスト取得
            jisCdList = rsvCmm.getJisCdList( prefId );
            jisNMList = rsvCmm.getJisNmList( prefId );
            // Logging.error("★2 = " + jisCdList.size() + ":" + prefId);

            // jsnObj = new JSONObject();
            // jsnObj.put( "jisCd", 0 );
            // jsnObj.put( "jisNm", ReserveCommon.PARKING_LIST_DEF );
            // jsnObj.put( "selJisCd", jisCd );
            // jsonJisArray.put( jsnObj );

            for( int i = 0 ; i < jisCdList.size() ; i++ )
            {
                jsnObj = new JSONObject();
                if ( i == 0 )
                {
                    jsnObj.put( "jisCd", 0 );
                    jsnObj.put( "jisNm", ReserveCommon.PARKING_LIST_DEF );
                    jsnObj.put( "selJisCd", jisCd );
                    jsonJisArray.put( jsnObj );
                    jsnObj = new JSONObject();
                    jsnObj.put( "jisCd", jisCdList.get( i ) );
                    jsnObj.put( "jisNm", jisNMList.get( i ) );
                }
                else
                {
                    jsnObj.put( "jisCd", jisCdList.get( i ) );
                    jsnObj.put( "jisNm", jisNMList.get( i ) );
                }
                jsnObj.put( "selJisCd", jisCd );
                jsonJisArray.put( jsnObj );
            }

            jsnAll.put( "jisList", jsonJisArray );
            jsonAllArray.put( jsnAll );
        }
        catch ( Exception e )
        {
            Logging.error( "Error ActionReserveAddressSearch.getPrefAddress = " + e.toString() );
        }

        return(jsonAllArray);
    }

    /**
     * 
     * 郵便番号存在チェック
     * 
     * @param zip 郵便番号
     * @return true:正常、false:異常
     */
    private boolean existsZip(String zip) throws Exception
    {
        boolean isResult = true;
        boolean blnRetZIP = false;
        String errMsg = "";
        DataMasterZip dmz = new DataMasterZip();

        try
        {
            // 郵便番号の存在チェック
            blnRetZIP = dmz.getData( zip );

            if ( blnRetZIP == false )
            {
                // 存在しない郵便番号
                isResult = false;
                errMsg = errMsg + Message.getMessage( "warn.00007" ) + "<br />";
            }
            return isResult;
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionReserveAddressSearch.existsZip] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            dmz = null;
        }
    }

    /**
     * 対象オプションの手仕舞い日取得
     * 
     * @param hotelId ホテルID
     * @param optionId オプションID
     * @param rsvDate 予約日
     * @return 手仕舞い日
     * @throws Exception
     */
    private String getCancelLimitDate(int hotelId, int optionId, Calendar rsvDate) throws Exception
    {
        int limitYear = 0;
        int limitMonth = 0;
        int limitDay = 0;
        String retLimitDate = "";
        int cancelLimitDate = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT cancel_limit_date, cancel_limit_time FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, optionId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cancelLimitDate = result.getInt( "cancel_limit_date" );
            }

            // 手仕舞い日を算出
            rsvDate.add( Calendar.DATE, -1 * cancelLimitDate );
            limitYear = rsvDate.get( Calendar.YEAR );
            limitMonth = rsvDate.get( Calendar.MONTH ) + 1;
            limitDay = rsvDate.get( Calendar.DATE );
            retLimitDate = Integer.toString( limitYear ) + String.format( "%1$02d", limitMonth ) + String.format( "%1$02d", limitDay );

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getCancelLimitDate] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(retLimitDate);
    }

    /**
     * 対象オプションの手仕舞い日の時刻取得
     * 
     * @param hotelId ホテルID
     * @param optionId オプションID
     * @return 手仕舞い日の時間
     * @throws Exception
     */
    private String getCancelLimitTime(int hotelId, int optionId) throws Exception
    {
        String cancelLimitTime = "";
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT cancel_limit_date, cancel_limit_time FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, optionId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cancelLimitTime = result.getString( "cancel_limit_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getCancelLimitTime] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(cancelLimitTime);
    }

    /**
     * 予約を作ったユーザーID取得
     * 
     * @param rsvNo 予約No
     * @return 予約を作成したユーザーID
     * @throws Exception
     */
    public String getRsvUserId(String rsvNo) throws Exception
    {
        String userId = "";
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT user_id FROM hh_rsv_reserve ";
        query = query + "WHERE reserve_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, rsvNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                userId = result.getString( "user_id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getRsvUserId] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(userId);
    }

    /**
     * CookieからログインユーザIDを取得する
     * 
     * @param cookies クッキー
     * @return ログインユーザID
     */
    public static String getCookieLoginUserId(Cookie[] cookies)
    {
        String ret = "";
        String cookValue = "";

        for( int i = 0 ; cookies != null && i < cookies.length ; i++ )
        {
            // ログインユーザID取得
            if ( cookies[i].getName().equals( "hhuid" ) )
            {
                cookValue = cookies[i].getValue();
                ret = cookValue;
                break;
            }
        }
        return ret;
    }

    /**
     * 対象ホテルが存在するか
     * 
     * @param hotelId ホテルID
     * @return true:存在する、false:存在しない
     */
    public static boolean existsHotel(int hotelId) throws Exception
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;

        query = query + "SELECT COUNT(*) CNT ";
        query = query + "FROM hh_hotel_basic hb ";
        query = query + "   INNER JOIN hh_rsv_reserve_basic rb ON hb.id = rb.id ";
        query = query + "WHERE hb.id = ? ";
        query = query + "  AND hb.rank >= 2 ";
        query = query + "  AND hb.kind <=7 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
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
            Logging.error( "[OwnerRsvCommon.existsHotel] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 対象のホテルIDにプランが存在するかチェック
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @return true:存在する、false:存在しない
     */
    public static boolean existsPlan(Integer hotelId, Integer date, Integer checkInTime) throws Exception
    {

        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        int calDate = nowDate;
        int calTime = nowTime;
        if ( checkInTime != null )
        {
            calTime = checkInTime * 100; // 30分後
        }
        else
        {
            calTime = nowTime;
        }
        if ( date != null )
        {
            if ( nowDate == date && nowTime < 50000 )
            {
                calDate = DateEdit.addDay( nowDate, -1 );
                calTime = calTime + 240000;
                calDate = 99999999; // 当日で5時前は強制的に検索させないんだって
            }
            else
            {
                calDate = date;
                if ( calDate < nowDate && calTime < 50000 )
                {
                    calTime = calTime + 240000;
                }
            }
        }

        StringBuilder query = new StringBuilder();
        query.append( " SELECT plan.plan_id, plan.plan_sub_id, plan.plan_type, plan.reserve_end_day, plan.reserve_end_time,plan.sales_end_date,plan.reserve_end_notset_flag " );
        // プランマスタ
        query.append( " FROM newRsvDB.hh_rsv_plan plan " );
        // プラン・ランク設定
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan_roomrank plan_rank " );
        query.append( "   ON plan.id = plan_rank.id  " );
        query.append( "   AND plan.plan_id = plan_rank.plan_id " );
        query.append( "   AND plan.plan_sub_id = plan_rank.plan_sub_id " );
        // プラン別料金マスタ
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan_charge plan_charge " );
        query.append( "   ON plan_rank.id = plan_charge.id  " );
        query.append( "   AND plan_rank.plan_id = plan_charge.plan_id " );
        query.append( "   AND plan_rank.plan_sub_id = plan_charge.plan_sub_id " );
        query.append( "   AND plan_rank.room_rank = plan_charge.room_rank " );
        if ( date != null )
        {
            // 部屋残数データ
            query.append( " INNER JOIN newRsvDB.hh_rsv_room_remainder remainder" );
            query.append( "   ON plan.id = remainder.id " );
            // プラン・部屋設定データ
            query.append( " INNER JOIN newRsvDB.hh_rsv_rel_plan_room plan_room " );
            query.append( "   ON plan.id = plan_room.id " );
            query.append( "   AND plan.plan_id = plan_room.plan_id " );
            query.append( "   AND plan.plan_sub_id = plan_room.plan_sub_id " );
            query.append( "   AND remainder.seq = plan_room.seq " );
            // 料金モード内訳
            query.append( " INNER JOIN newRsvDB.hh_rsv_charge_mode_breakdown breakdown " );
            query.append( "   ON plan_charge.id = breakdown.id " );
            query.append( "   AND plan_charge.plan_id = breakdown.plan_id " );
            query.append( "   AND plan_charge.plan_sub_id = breakdown.plan_sub_id " );
            query.append( "   AND plan_charge.plan_charge_mode_id = breakdown.plan_charge_mode_id " );
            // ホテルカレンダーマスタ
            query.append( " INNER JOIN newRsvDB.hh_rsv_hotel_calendar calendar " );
            query.append( "   ON breakdown.id = calendar.id " );
            query.append( "   AND breakdown.charge_mode_id = calendar.charge_mode_id " );
            query.append( "   AND remainder.cal_date = calendar.cal_date " );
        }
        query.append( " WHERE plan.latest_flag = ? " );
        query.append( "   AND plan.plan_sales_status = ? " );
        query.append( "   AND plan.id = ? " );
        query.append( "   AND plan.sales_start_date <= ? " );
        query.append( "   AND (plan.sales_end_date >= ? " );
        query.append( "   OR plan.reserve_end_notset_flag = ?) " );
        if ( date != null )
        {
            if ( date <= nowDate )
            {
                query.append( "   AND plan.plan_type IN (3,4) " ); // 当日限定
            }
            else
            {
                query.append( "   AND plan.plan_type IN (1,2) " );// 当日限定プラン以外
            }
            query.append( "   AND remainder.cal_date = ? " );
            query.append( "   AND plan.reserve_end_day <= ? " );
            if ( date <= nowDate )
            {
                query.append( "   AND plan_charge.ci_time_to >= ? " );
                query.append( "   AND ((plan_charge.ci_time_from <= ? AND plan.coming_flag = 1) " );// 30分以内来店
                query.append( "   OR plan.coming_flag=0)" );
            }
        }
        else
        {
            query.append( "   AND plan_charge.ci_time_to > CASE WHEN plan.plan_type IN (3,4) THEN " + nowTime + " ELSE 0 END" );
        }

        query.append( " GROUP BY plan.plan_id, plan.plan_sub_id " );
        query.append( " ORDER BY plan.disp_index, plan.plan_id " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, 1 );
            prestate.setInt( i++, 1 );
            prestate.setInt( i++, hotelId );
            if ( date != null )
            {
                // 日付指定ありの場合のみ開始日付をみる
                prestate.setInt( i++, calDate );
                prestate.setInt( i++, calDate );
            }
            else
            {
                // 過去のプランは検索対象外
                if ( nowTime < 50000 ) // 日付指定なしの場合は、終了日付の範囲を前日以降にする必要あり
                {
                    prestate.setInt( i++, 99999999 );
                    prestate.setInt( i++, DateEdit.addDay( calDate, -1 ) );
                }
                else
                {
                    prestate.setInt( i++, 99999999 );
                    prestate.setInt( i++, calDate );
                }
            }
            prestate.setInt( i++, 1 );
            if ( date != null )
            {
                prestate.setInt( i++, calDate );
                prestate.setInt( i++, calDate );
                if ( date <= nowDate )
                {
                    prestate.setInt( i++, calTime );
                    prestate.setInt( i++, calTime );
                }
            }
            result = prestate.executeQuery();

            ArrayList<Integer> planId = new ArrayList<Integer>();
            ArrayList<Integer> plansubId = new ArrayList<Integer>();
            result.beforeFirst();
            while( result.next() )
            {
                // 事前予約締切日チェック
                boolean chk = true;
                if ( result.getInt( "plan.plan_type" ) == 1 || result.getInt( "plan.plan_type" ) == 2 )
                {
                    if ( date == null )
                    {
                        chk = checkRsvDate_sed( result.getInt( "plan.sales_end_date" ), result.getInt( "plan.reserve_end_notset_flag" ), nowDate, result.getInt( "plan.reserve_end_day" ) );
                    }
                    else
                    {
                        chk = checkRsvDate( calDate, nowDate, nowTime, result.getInt( "plan.reserve_end_day" ), result.getInt( "reserve_end_time" ) );
                    }
                }
                if ( chk )
                {
                    planId.add( result.getInt( "plan_id" ) );
                    plansubId.add( result.getInt( "plan_sub_id" ) );
                }
            }

            if ( planId.isEmpty() )
            {
                return false;
            }
            else
            {
                return true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicApiPlan.searchPlans] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

    }

    /**
     * 事前予約受付締切時刻と現在日時の整合性チェック(検索日がの場合)
     * 
     * @param sales_end_date 予約可能期間終了 YYYYMMDD
     * @param reserve_end_notset_flag 予約可能期間終了日未設定フラグ
     * @param nowDate 現在日 YYYYMMDD
     * @param reserve_end_day 事前予約締切日
     * @return true：予約可能日付、false:予約不可日付
     */
    private static boolean checkRsvDate_sed(int sales_end_date, int reserve_end_notset_flag, int nowDate, int reserve_end_day)
    {
        // 検索日がの場合は予約可能期間終了日と事前予約締切日と当日日付のチェック
        boolean chk = false;
        int targetDate = DateEdit.addDay( nowDate, reserve_end_day );
        if ( sales_end_date >= targetDate || reserve_end_notset_flag == 1 )
        {
            chk = true;
        }

        return chk;
    }

    /**
     * 事前予約受付締切時刻と現在日時の整合性チェック
     * 
     * @param calDate 検索対象日 YYYYMMDD
     * @param nowDate 現在日 YYYYMMDD
     * @param nowTime 現在時刻 HHMMSS
     * @param reserve_end_day 事前予約締切日
     * @param reserve_end_time 事前予約締切日 時刻 HHMMSS
     * @return true：予約可能日付、false:予約不可日付
     */
    private static boolean checkRsvDate(int calDate, int nowDate, int nowTime, int reserve_end_day, int reserve_end_time)
    {
        // 検索日がでない場合は検索対象日と事前予約締切日と当日日付のチェック
        boolean chk = false;
        int targetDate = DateEdit.addDay( nowDate, reserve_end_day );

        if ( calDate > targetDate )
        {
            chk = true;
        }
        else if ( calDate == targetDate )
        {
            // 時刻チェック
            if ( nowTime <= reserve_end_time )
            {
                chk = true;
            }
        }
        return chk;
    }

    /**
     * 対象のホテルIDにプランが存在するかチェック
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @return true:存在する、false:存在しない
     */
    public static boolean existsPlan(int hotelId, int planId) throws Exception
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;

        query = query + "SELECT COUNT(*) CNT FROM hh_rsv_plan ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
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
            Logging.error( "[OwnerRsvCommon.existsPlan] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 予約日と現在日付の整合性チェック
     * 
     * @param rsvDate 予約日
     * @return true：予約可能日付、false:予約不可日付
     */
    public static boolean checkRsvDate(int rsvDate)
    {
        boolean ret = true;
        int today = 0;
        Calendar cal = Calendar.getInstance();
        String year = "";
        String month = "";
        String day = "";

        // 当日を取得
        year = Integer.toString( cal.get( Calendar.YEAR ) );
        month = String.format( "%1$02d", cal.get( Calendar.MONTH ) + 1 );
        day = String.format( "%1$02d", cal.get( Calendar.DATE ) );
        today = Integer.parseInt( year + month + day );

        if ( rsvDate <= today && rsvDate != 0 )
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * プランに対象の部屋が存在するかチェック
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param seq 部屋番号
     * @param mode 処理モード
     * @return true:存在する、false:存在しない
     */
    public static boolean existsPlanSeq(int hotelId, int planId, int seq, String mode) throws Exception
    {
        boolean ret = false;
        int offerKind = 0;

        // 提供区分取得
        offerKind = getOfferKind( hotelId, planId );

        if ( mode.equals( ReserveCommon.MODE_INS ) )
        {
            // ▼新規の場合
            if ( offerKind == OFFER_KIND_PLAN )
            {
                // プラン指定の場合、部屋番号が0ならOK、0以外ならエラー
                if ( seq == 0 )
                {
                    ret = true;
                }
            }
            else
            {
                // 部屋指定の場合、部屋の存在チェック
                ret = existsSeq( hotelId, planId, seq );
            }
        }
        else if ( mode.equals( ReserveCommon.MODE_UPD ) )
        {
            // ▼更新
            // 部屋の存在チェック
            ret = existsSeq( hotelId, planId, seq );
        }

        return(ret);
    }

    /**
     * プラン提供区分取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @return 提供区分
     * @throws Exception
     */
    private static int getOfferKind(int hotelId, int planId) throws Exception
    {
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int offerKind = 0;

        // プランの提供区分を取得
        query = query + "SELECT offer_kind FROM hh_rsv_plan ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                offerKind = result.getInt( "offer_kind" );
            }

            ret = offerKind;

        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getOfferKind] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 指定プランに部屋が存在するかチェック
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param seq 部屋番号
     * @return true:存在する、false:存在しない
     * @throws Exception
     */
    private static boolean existsSeq(int hotelId, int planId, int seq) throws Exception
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;

        // プランの提供区分を取得
        query = query + "SELECT COUNT(*) AS CNT FROM hh_rsv_rel_plan_room ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND seq = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, seq );
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
            Logging.error( "[OwnerRsvCommon.existsSeq] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * プランが「掲載」になっているか
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @return true:掲載する、false:掲載しない
     * @throws Exception
     */
    public static boolean isPlanKeisai(int hotelId, int planId) throws Exception
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int publishingFlg = 0;

        query = query + "SELECT publishing_flag FROM hh_rsv_plan ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                publishingFlg = result.getInt( "publishing_flag" );
            }

            if ( publishingFlg == 1 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.isPlanKeisai] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 日別料金データが存在するか
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param rsvDate 予約日
     * @return true:掲載する、false:掲載しない
     * @throws Exception
     */
    public static boolean existsDayCharge(int hotelId, int planId, int rsvDate) throws Exception
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;

        query = query + "SELECT COUNT(*) AS CNT ";
        query = query + "FROM hh_rsv_plan_charge pl ";
        query = query + "   LEFT JOIN hh_rsv_day_charge dc ON pl.id = dc.id AND pl.plan_id = dc.plan_id AND pl.charge_mode_id = dc.charge_mode_id ";
        query = query + "WHERE pl.id = ? ";
        query = query + "  AND pl.plan_id = ? ";
        query = query + "  AND dc.cal_date = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, rsvDate );
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
            Logging.error( "[OwnerRsvCommon.existsDayCharge] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 販売期間、予約受付日のチェック
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param rsvDate 予約日
     * @return true:掲載する、false:掲載しない
     * @throws Exception
     */
    public static boolean checkSalesDate(int hotelId, int planId, int rsvDate) throws Exception
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int salesStartDate = 0;
        int salesEndDate = 0;
        int rsvStartDay = 0;
        int rsvEndDay = 0;
        String year = "";
        String month = "";
        String date = "";
        String rsvYear = "";
        String rsvMonth = "";
        String rsvDay = "";
        String rsvStartTime = "";
        String rsvEndTime = "";
        int today = 0;
        String rsvStartDateStr = "";
        String rsvEndDateStr = "";
        long rsvStartDateTime = 0;
        long rsvEndDateTime = 0;
        long todayTime = 0;

        query = query + "SELECT sales_start_date, sales_end_date, reserve_start_day, reserve_end_day, reserve_start_time, reserve_end_time ";
        query = query + "FROM hh_rsv_plan ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        // 現在を取得
        Calendar calendar = Calendar.getInstance();
        year = Integer.toString( calendar.get( Calendar.YEAR ) );
        month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
        date = String.format( "%1$02d", calendar.get( Calendar.DATE ) );
        today = Integer.parseInt( year + month + date );
        todayTime = Long.parseLong( Integer.toString( today )
                + String.format( "%1$02d", calendar.get( Calendar.HOUR_OF_DAY ) ) + String.format( "%1$02d", calendar.get( Calendar.MINUTE ) ) + "00" );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                salesStartDate = result.getInt( "sales_start_date" );
                salesEndDate = result.getInt( "sales_end_date" );
                rsvStartDay = result.getInt( "reserve_start_day" );
                rsvEndDay = result.getInt( "reserve_end_day" );
                rsvStartTime = result.getString( "reserve_start_time" );
                rsvEndTime = result.getString( "reserve_end_time" );
            }

            // 予約日が販売期間外の場合
            if ( (salesStartDate > rsvDate) || (salesEndDate < rsvDate) )
            {
                return(ret);
            }

            // ▼予約受付開始期間の算出
            // カレンダーに予約日を設定
            rsvYear = Integer.toString( rsvDate ).substring( 0, 4 );
            rsvMonth = Integer.toString( rsvDate ).substring( 4, 6 );
            rsvDay = Integer.toString( rsvDate ).substring( 6, 8 );
            calendar.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ) );

            // 予約受付開始期間の算出
            calendar.add( Calendar.DATE, rsvStartDay * -1 );
            rsvStartDateStr = new SimpleDateFormat( "yyyyMMdd" ).format( calendar.getTime() );
            rsvStartDateTime = Long.parseLong( rsvStartDateStr + rsvStartTime );

            // ▼予約受付終了機関の算出
            // カレンダーに予約日を設定
            rsvYear = Integer.toString( rsvDate ).substring( 0, 4 );
            rsvMonth = Integer.toString( rsvDate ).substring( 4, 6 );
            rsvDay = Integer.toString( rsvDate ).substring( 6, 8 );
            calendar.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ) );

            // 予約受付終了機関の算出
            calendar.add( Calendar.DATE, rsvEndDay * -1 );
            rsvEndDateStr = new SimpleDateFormat( "yyyyMMdd" ).format( calendar.getTime() );
            rsvEndDateTime = Long.parseLong( rsvEndDateStr + rsvEndTime );

            // 今日が予約開始、終了日内か
            if ( (Integer.parseInt( rsvStartDateStr ) > today) || (Integer.parseInt( rsvEndDateStr ) < today) )
            {
                return(ret);
            }

            // 今日が開始日、終了日の場合、時間をチェック
            if ( (Integer.parseInt( rsvStartDateStr ) == today) || (Integer.parseInt( rsvEndDateStr ) == today) )
            {
                if ( (todayTime < rsvStartDateTime) || (rsvEndDateTime < todayTime) )
                {
                    return(ret);
                }

            }

            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.checkSalesDate] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 予約申し込み人数のチェック
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param adult 大人人数
     * @param child 子供人数
     * @return エラーメッセージ
     */
    public String checkAdultChildNum(int hotelId, int planId, int adult, int child)
    {
        String errMsg = "";
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "select max_num_adult, min_num_adult, max_num_child, min_num_child from hh_rsv_plan where id = ? and plan_id = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            result.next();
            // 大人人数の確認
            if ( adult < result.getInt( "min_num_adult" ) || adult > result.getInt( "max_num_adult" ) )
            {
                errMsg += Message.getMessage( "erro.30014", "大人人数" );
            }
            // 子供人数の確認
            if ( child < result.getInt( "min_num_child" ) || child > result.getInt( "max_num_child" ) )
            {
                errMsg += Message.getMessage( "erro.30014", "子供人数" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.checkAdultChildNum] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(errMsg);
    }

    /**
     * 画面間パラメータの整合性チェック
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param seq 部屋番号
     * @param rsvDate 予約日
     * @param usrKbn ユーザ区分（u：ユーザ、o：オーナー）
     * @param mode 処理モード
     * @return エラーメッセージ
     * @throws Exception
     */
    public String checkParam(int hotelId, int planId, int seq, int rsvDate, String usrKbn, String mode) throws Exception
    {
        String errMsg = "";

        // ホテルが存在するか
        if ( existsHotel( hotelId ) == false )
        {
            // 存在しない場合
            errMsg = Message.getMessage( "erro.30010" );
            return(errMsg);
        }

        // ホテルにプランが存在するか
        if ( existsPlan( hotelId, planId ) == false )
        {
            // 存在しない場合
            errMsg = Message.getMessage( "erro.30001", "指定されたホテルに該当プラン" );
            return(errMsg);
        }

        // 部屋番号存在チェック
        if ( existsPlanSeq( hotelId, planId, seq, mode ) == false )
        {
            // 存在しない場合
            errMsg = Message.getMessage( "erro.30001", "指定されたプランに該当の部屋" );
            return(errMsg);
        }

        // 日付未選択の場合はここでチェックしない
        if ( rsvDate != 0 )
        {
            // 日別料金が存在しているか
            if ( existsDayCharge( hotelId, planId, rsvDate ) == false )
            {
                errMsg = Message.getMessage( "warn.00035" );
                return(errMsg);
            }
        }

        // ユーザの場合は、プランの掲載、予約日と販売期間、受付期間の日付関連のチェックを実施する
        // オーナーの場合は、掲載/非掲載、日付の状態に関係なく更新可能とする
        if ( usrKbn.equals( ReserveCommon.USER_KBN_USER ) )
        {
            // プランが「掲載」か
            if ( isPlanKeisai( hotelId, planId ) == false )
            {
                errMsg = Message.getMessage( "erro.30001", "指定されたホテルに該当プラン" );
                return(errMsg);
            }

            // 日付未選択の場合はここでチェックしない
            if ( rsvDate != 0 )
            {
                // 予約日が未来日かチェック
                if ( checkRsvDate( rsvDate ) == false )
                {
                    // 当日以前の場合
                    errMsg = Message.getMessage( "erro.30006" );
                    return(errMsg);
                }

                // 予約日が販売期間、予約受付期間の範囲内か
                if ( checkSalesDate( hotelId, planId, rsvDate ) == false )
                {
                    errMsg = Message.getMessage( "warn.00036" );
                    return(errMsg);
                }
            }
        }

        return(errMsg);
    }

    /**
     * 表示用到着予定時刻取得
     * 
     * @param target 到着予定時刻
     * @return hh:mmの時間
     */
    public static String getArrivalTimeView(int target)
    {
        String ret = "";
        // int arrivalTime = 0;

        // if ( target >= 240000 )
        // {
        // arrivalTime = target - 240000;
        // ret = "翌" + ConvertTime.convTimeStr( arrivalTime, 3 );
        // }
        // else
        // {
        ret = ConvertTime.convTimeStr( target, 3 );
        // }

        return(ret);
    }

    /***
     * 請求開始時刻取得処理
     * 
     * @return 請求開始時刻値
     **/
    public static int getChargeStartTime()
    {
        int startTime = 0;
        String ret = "";
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT val1 FROM hh_rsv_system_conf WHERE ctrl_id1 = ? and ctrl_id2 = 1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, RESERVE_SYSTEMCONF_CHAGE_START_TIME );
            result = prestate.executeQuery();
            if ( result != null && result.next() == true )
            {
                ret = result.getString( "val1" );
            }
            // 数値変換できるかどうか
            if ( (ret != null) && (ret.equals( "" ) == false) && (CheckString.numCheck( ret ) != false) )
            {
                startTime = Integer.parseInt( ret );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getChargeStartTime()] Exception=" + e.toString() );
            startTime = 0;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(startTime);
    }

    public String checkReserveDuplicate(String userId, int rsvDate)
    {
        String errMsg = "";
        try
        {
            if ( checkReserveUserDup( userId, rsvDate ) == false )
            {
                // 存在する場合はエラー
                errMsg = Message.getMessage( "warn.00018" ) + "<br />";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkReserveDuplicate()] Exception:" + e.toString() );
            errMsg = "サーバーエラーが発生致しました。";
        }

        return errMsg;
    }

    /***
     * 設備情報表示チェック処理
     * 
     * @param id ホテルID
     * 
     * @return true→設備情報表示
     **/
    public static boolean checkEquipDispFlag(int id)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "select equip_disp_flag from hh_rsv_reserve_basic where id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null && result.next() != false )
            {
                if ( result.getInt( "equip_disp_flag" ) > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkEquipDispFlag()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * 請求開始時刻取得処理
     * 
     * @param id ホテルID
     * @param planid プランID
     * 
     * @return 指定プラン部屋リスト
     **/
    public static ArrayList<Integer> getRoomSeqList(int id, int planid)
    {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "select seq from hh_rsv_rel_plan_room where id = ? and plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planid );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    ret.add( result.getInt( "seq" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getRoomSeqList()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * 部屋リスト機器設定情報存在確認処理
     * 
     * @param id ホテルID
     * @param equipid 機器ID
     * @param roomSeqList 部屋リスト
     * @param equipType チェック元機器種別(1:全室 2:一部 3:レンタル 4:販売(ここだけの種別))
     * 
     * @return 機器種別(0→未入力,1→全室,2→一部)
     **/
    public static int checkRoomEquipList(int id, int equipid, ArrayList<Integer> roomSeqList, int equipType)
    {
        int ret = 0;
        int truecount = 0;
        int falsecount = 0;

        try
        {
            for( int i = 0 ; i < roomSeqList.size() ; i++ )
            {
                if ( checkRoomEquip( id, equipid, roomSeqList.get( i ) ) == true )
                {
                    truecount++;
                }
                else
                {
                    falsecount++;
                }
            }

            if ( truecount > 0 )
            {
                if ( falsecount > 0 )
                {

                    if ( equipType == 1 || equipType == 2 )
                    {
                        // 全室および一部の場合はない部屋があれば一部として返す
                        ret = 2;
                    }
                    else
                    {
                        // 販売・レンタルはすべて対応して初めて対象の区分なので0で返す
                        ret = 0;
                    }
                }
                else
                {
                    if ( equipType == 1 || equipType == 2 || equipType == 4 )
                    {
                        // 全室および一部および販売の機器種別のパターンは失敗なければ全室(販売)となる
                        ret = 1;
                    }
                    else if ( equipType == 3 )
                    {
                        // レンタルの場合はそのままの値で返す
                        ret = 3;
                    }
                }
            }
            else
            {
                // 存在確認がひとつも取れなければ0
                ret = 0;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkRoomEquipList()] Exception:" + e.toString() );
        }
        finally
        {
        }

        return(ret);
    }

    /***
     * 部屋機器設定情報存在確認処理
     * 
     * @param id ホテルID
     * @param equipid 機器ID
     * @param roomseq 部屋
     * 
     * @return true→存在
     **/
    public static boolean checkRoomEquip(int id, int equipid, int roomseq)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "select seq from hh_rsv_rel_room_equip where id = ? and equip_id = ? and seq = ?";

        try
        {
            // 対象の部屋に機種設定があるか確認する
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, equipid );
            prestate.setInt( 3, roomseq );

            result = prestate.executeQuery();

            if ( result != null && result.next() != false )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.checkRoomEquip()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * 予約日
     * 
     * @param id ホテルID
     * @param planId プランID
     * @param rsvDate 予約日
     * 
     * @return true→存在
     **/
    public static int getReserveCount(int id, int planId, int rsvDate)
    {
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT COUNT(*) AS CNT FROM hh_rsv_reserve WHERE id = ? AND plan_id = ? AND reserve_date = ? AND status = 1";

        try
        {
            // 対象の部屋に機種設定があるか確認する
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, rsvDate );

            result = prestate.executeQuery();

            if ( result != null && result.next() != false )
            {
                ret = result.getInt( "CNT" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon. getReserveCount()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /***
     * 新予約プラン有無の判断
     * 
     * @param id ホテルID
     * 
     * @return true→存在
     **/
    public static boolean isNewReserve(Connection connection, int id)
    {
        boolean ret = false;
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        /*
         * query = "SELECT count(*) FROM newRsvDB.hh_rsv_plan RP";
         * query += " LEFT JOIN newRsvDB.hh_rsv_rel_plan_room RPR ON RP.id = RPR.id AND RP.plan_id = RPR.plan_id AND RP.plan_sub_id = RPR.plan_sub_id";
         * query += " INNER JOIN hh_hotel_room_more RM ON RPR.id = RM.id AND RPR.seq = RM.seq";
         * query += " INNER JOIN hh_hotel_roomrank HR ON RM.id = HR.id AND RM.room_rank = HR.room_rank";
         * query += " WHERE RP.id = ? ";
         * query += " AND RP.latest_flag = 1 AND RP.plan_sales_status = 1 AND RP.publishing_flag = 1 AND ? <= RP.sales_end_date";
         */
        query = "SELECT count(RB.id) FROM newRsvDB.hh_rsv_reserve_basic RB";
        query += "  INNER JOIN hh_hotel_basic HB ON RB.id = HB.id";
        query += "  AND HB.rank >= 3";
        query += "  AND HB.kind <= 7";
        query += " WHERE RB.id = ? ";
        query += " AND RB.sales_flag = 1";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );

            result = prestate.executeQuery();

            if ( result != null && result.next() != false )
            {
                if ( result.getInt( 1 ) != 0 )
                    ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.isNewReserve()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /***
     * 新予約プラン有無の判断
     * 
     * @param id ホテルID
     * 
     * @return true→存在
     **/
    public static boolean isNewReserve(int id)
    {
        boolean ret = false;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            ret = isNewReserve( connection, id );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.isNewReserve()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * キャンセルパターンからキャンセルポリシー取得
     * 
     * @param cancel_id
     * @return
     * @throws Exception
     */
    public static String getCancelPolicyFromCancelpattern(int cancel_id) throws Exception
    {

        String cancelPolicy = "";

        StringBuilder query = new StringBuilder();
        query.append( " SELECT * " );
        query.append( " FROM newRsvDB.hh_rsv_cancelpattern " );
        query.append( " WHERE cancel_id = ? " );
        query.append( " ORDER BY seq, day_from " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, cancel_id );
            result = prestate.executeQuery();

            ArrayList<DataHhRsvCancelpattern> cancelPattern = new ArrayList<DataHhRsvCancelpattern>();
            while( result.next() )
            {
                DataHhRsvCancelpattern data = new DataHhRsvCancelpattern();
                data.setData( result );
                cancelPattern.add( data );
            }
            cancelPolicy = getCancelPolicy( cancelPattern );

            return cancelPolicy;
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getCancelPolicyFromCancelpattern] Exception=" + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * ホテルキャンセルパターンからキャンセルポリシー取得
     * 
     * @param id ホテルID
     * @return
     * @throws Exception
     */
    public static String getCancelPolicyFromHotelCancelpattern(int id) throws Exception
    {

        String cancelPolicy = "";

        StringBuilder query = new StringBuilder();
        query.append( " SELECT * " );
        query.append( " FROM newRsvDB.hh_rsv_hotel_cancelpattern " );
        query.append( " WHERE id = ? " );
        query.append( " ORDER BY cancel_id, seq, day_from " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, id );
            result = prestate.executeQuery();
            ArrayList<DataHhRsvCancelpattern> cancelPattern = new ArrayList<DataHhRsvCancelpattern>();
            while( result.next() )
            {
                DataHhRsvCancelpattern data = new DataHhRsvCancelpattern();
                data.setData( result );
                cancelPattern.add( data );
            }
            cancelPolicy = getCancelPolicy( cancelPattern );

            return cancelPolicy;
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getCancelPolicyFromHotelCancelpattern] Exception=" + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * キャンセルポリシー取得
     * 
     * @param ArrayList<DataHhRsvCancelpattern>
     * @return
     * @throws Exception
     */
    public static String getCancelPolicy(ArrayList<DataHhRsvCancelpattern> cancelPattern) throws Exception
    {

        String cancelPolicy = "";
        int dayFrom = 0;
        int dayTo = 0;
        int per = 0;

        DataHhRsvCancelpattern data = new DataHhRsvCancelpattern();

        try
        {
            for( int i = 0 ; i < cancelPattern.size() ; i++ )
            {
                data = cancelPattern.get( i );
                dayFrom = data.getDayFrom();
                dayTo = data.getDayTo();
                per = data.getPer();

                if ( dayFrom == 0 && dayTo == 0 )
                {
                    cancelPolicy += ReplaceString.HTMLEscape( "当日" + per + "%、" );
                }
                if ( dayFrom == 1 && dayTo == 1 )
                {
                    cancelPolicy += "前日" + per + "%、";
                }
                if ( dayFrom != dayTo )
                {
                    String dayF = "";
                    if ( dayFrom == 0 )
                    {
                        dayF = "当日";

                    }
                    else if ( dayFrom == 1 )
                    {
                        dayF = "前日";
                    }
                    else
                    {
                        dayF = String.valueOf( dayFrom );
                    }
                    cancelPolicy += dayF + "～" + dayTo + "日前" + per + "%、";
                }

            }
            // 最後のカンマ削除
            int index = cancelPolicy.lastIndexOf( "、" );
            if ( index >= 0 )
            {
                cancelPolicy = cancelPolicy.substring( 0, index );
            }

            return cancelPolicy.toString();
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getCancelPolicy] Exception=" + e.toString(), e );
            throw e;
        }

    }

    /***
     * 予約連動物件の判断
     * 
     * @param id ホテルID
     * @return true→存在
     **/
    public static boolean isSyncReserve(int id)
    {
        boolean ret = false;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            ret = isSyncReserve( connection, id, 0 );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.isSyncReserve()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /***
     * 予約休憩連動物件の判断
     * 
     * @param id ホテルID
     * @return true→存在
     **/
    public static boolean isSyncRest(int id)
    {
        boolean ret = false;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            ret = isSyncReserve( connection, id, 1 );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.isSyncRest()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public static boolean isSyncReserve(Connection connection, int id, int judge)
    {
        boolean ret = false;
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT rsv_version,rsv_last_update FROM hh_hotel_auth";
        query += " WHERE id = ? ";
        query += " ORDER BY rsv_last_update DESC";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );

            result = prestate.executeQuery();

            if ( result != null && result.next() != false )
            {
                if ( judge == 0 ) // 予約ホスト連携をやっている判断
                {
                    if ( result.getInt( "rsv_last_update" ) >= DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -2 ) )
                        ret = true;
                }
                else if ( judge == 1 ) // 予約ホスト連携をやっており休憩対応をしていない判断
                {
                    if ( result.getInt( "rsv_last_update" ) > 0 && result.getInt( "rsv_version" ) == 0 )
                    {
                        ret = false;
                    }
                    else
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.isSyncReserve()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /***
     * sms認証に使った電話番号の取得
     * 
     * @param userId ユーザーID
     * @return sms_phone_no→電話番号（ない場合は空白で返る）
     **/
    public static String getSmsPhoneNo(String userId, int acceptDate)
    {
        String sms_phone_no = "";
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            sms_phone_no = getSmsPhoneNo( connection, userId, acceptDate );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getSmsPhoneNo()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(sms_phone_no);
    }

    public static String getSmsPhoneNo(Connection connection, String userId, int acceptDate)
    {
        String sms_phone_no = "";
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT sms_phone_no FROM newRsvDB.sms_auth WHERE user_id = ? ";
        query += " AND sms_auth.sms_status = 1";
        query += " AND sms_auth.last_update >= ?";
        query += " ORDER BY last_update DESC,last_uptime DESC";
        query += " LIMIT 0,1";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, acceptDate );// 入力日の1日前

            result = prestate.executeQuery();

            if ( result != null && result.next() != false )
            {
                sms_phone_no = result.getString( "sms_phone_no" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getSmsPhoneNo()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(sms_phone_no);
    }

    /***
     * 予約Noからプランタイプを取得する
     * 
     * @param hotelId ホテルID
     * @param reserveNo 予約No
     * @return planType→プランタイプ
     **/
    public static int getPlanType(int hotelId, String reserveNo)
    {
        int plan_type = 0;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            plan_type = getPlanType( connection, hotelId, reserveNo );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getPlanType()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(plan_type);
    }

    public static int getPlanType(Connection connection, int hotelId, String reserveNo)
    {
        int plan_type = 0;
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT plan.plan_type FROM newRsvDB.hh_rsv_plan plan";
        query += " INNER JOIN newRsvDB.hh_rsv_reserve reserve ON plan.id = reserve.id ";
        query += " AND plan.plan_id = reserve.plan_id AND plan.plan_sub_id = reserve.plan_sub_id ";
        query += " WHERE reserve.id = ?";
        query += " AND reserve.reserve_no = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, reserveNo );

            result = prestate.executeQuery();

            if ( result != null && result.next() != false )
            {
                plan_type = result.getInt( "plan_type" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getPlanType()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return(plan_type);
    }

    /**
     * 部屋残数データ更新
     * 
     * @param conn DBConnection
     * @param iD ホテルID
     * @param calDate 日付
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public static boolean updateRemainder(Connection connection, int hotelId, int calDate, int seq, String reserveNo, int status, int planType)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        String statusName = "[type_]status";
        String reserveNoName = "[type_]reserve_no";

        String type = "";
        Logging.debug( "[updateRemainder] hotelId=" + hotelId + "reserveNo=" + reserveNo + ",getPlanType=" + planType );

        if ( planType == 1 || planType == 3 )
        {
            type = "stay_";

        }
        else if ( planType == 2 || planType == 4 )
        {
            type = "rest_";
        }

        statusName = statusName.replace( "[type_]", type );
        reserveNoName = reserveNoName.replace( "[type_]", type );

        query = "UPDATE newRsvDB.hh_rsv_room_remainder SET " +
                statusName + " = ? , " + reserveNoName + " = ? ";
        query = query + " WHERE id = ? AND cal_date = ? AND seq = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, status );

            prestate.setString( 2, reserveNo );
            prestate.setInt( 3, hotelId );
            prestate.setInt( 4, calDate );
            prestate.setInt( 5, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[updateRemainder] Exception=" + e.toString() );
            ret = false;
        }
        return(ret);
    }

}
