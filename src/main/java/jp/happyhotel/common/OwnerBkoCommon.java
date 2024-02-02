package jp.happyhotel.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.data.DataBkoAccountRecv;
import jp.happyhotel.data.DataBkoAccountRecvDetail;

/**
 * バックオフィス用オーナーサイトメニュー共通クラス
 * 
 * @author H.Takanami
 * @version 1.00 2011/04/15
 * @see
 */
public class OwnerBkoCommon implements Serializable
{
    private static final long  serialVersionUID            = -1781688967127512378L;

    // ▼科目コード
    public static final int    ACCOUNT_TITLE_CD_100        = 100;                       // 来店
    public static final int    ACCOUNT_TITLE_CD_110        = 110;                       // 付与
    public static final int    ACCOUNT_TITLE_CD_120        = 120;                       // 割引
    public static final int    ACCOUNT_TITLE_CD_130        = 130;                       // 予約
    public static final int    ACCOUNT_TITLE_CD_140        = 140;                       // 送客手数料
    public static final int    ACCOUNT_TITLE_CD_141        = 141;                       // 送客手数料(LVJ)
    public static final int    ACCOUNT_TITLE_CD_142        = 142;                       // 送客手数料(OTA)
    public static final int    ACCOUNT_TITLE_CD_150        = 150;                       // ボーナスマイル

    public static final int    ACCOUNT_TITLE_CD_200        = 200;                       // スタンダードコース
    public static final int    ACCOUNT_TITLE_CD_210        = 210;                       // 広告料
    public static final int    ACCOUNT_TITLE_CD_201        = 201;                       // Aスタンダード
    public static final int    ACCOUNT_TITLE_CD_211        = 211;                       // B広告TOP
    public static final int    ACCOUNT_TITLE_CD_212        = 212;                       // C広告Y!地域
    public static final int    ACCOUNT_TITLE_CD_213        = 213;                       // D広告企画
    public static final int    ACCOUNT_TITLE_CD_214        = 214;                       // E広告ターゲティングメール
    public static final int    ACCOUNT_TITLE_CD_215        = 215;                       // F広告都道府県

    // ▼伝票区分
    public static final int    SLIP_KIND_DEFAULT           = 0;                         // 来店、予約
    public static final int    SLIP_KIND_SEISAN            = 1;
    public static final int    SLIP_KIND_RESERVE           = 2;
    public static final int    SLIP_KIND_OTHER             = 3;

    // ▼締日(10日)
    public static final int    SIME_DATE                   = 10;

    // ▼消費税
    public static final double TAX                         = 0.08;

    // ▼パーセンテージ(%)
    public static final int    PERCENT_SEISAN_AMOUNT       = 2;                         // 利用金額
    public static final int    PERCENT_SEISAN_POINT        = 1;                         // 利用ポイント
    public static final int    PERCENT_YOYAKU_AMOUNT       = 5;                         // 予約金額
    public static final int    PERCENT_YOYAKU_POINT        = 1;                         // 予約ポイント

    // ▼科目名
    public static final String ACCOUNT_TITLE_NAME_100      = "来店";
    public static final String ACCOUNT_TITLE_NAME_110      = "付与";
    public static final String ACCOUNT_TITLE_NAME_120      = "使用";
    public static final String ACCOUNT_TITLE_NAME_130      = "予約";
    public static final String ACCOUNT_TITLE_NAME_140      = "送客手数料";
    public static final String ACCOUNT_TITLE_NAME_141      = "送客手数料(LVJ)";
    public static final String ACCOUNT_TITLE_NAME_142      = "送客手数料(OTA)";
    public static final String ACCOUNT_TITLE_NAME_150      = "ボーナスマイル";
    public static final String ACCOUNT_TITLE_NAME_160      = "クレジット";
    public static final String ACCOUNT_TITLE_NAME_170      = "クレジット手数料";
    public static final String ACCOUNT_TITLE_NAME_200      = "スタンダードコース";
    public static final String ACCOUNT_TITLE_NAME_210      = "広告料";

    public static final String ACCOUNT_TITLE_NAME_201      = "Aスタンダード";
    public static final String ACCOUNT_TITLE_NAME_211      = "B広告TOP";
    public static final String ACCOUNT_TITLE_NAME_212      = "C広告Y!地域";
    public static final String ACCOUNT_TITLE_NAME_213      = "D広告企画";
    public static final String ACCOUNT_TITLE_NAME_214      = "E広告ターゲティングメール";
    public static final String ACCOUNT_TITLE_NAME_215      = "F広告都道府県";

    // ▼科目名（請求明細用）
    public static final String BILL_ACCOUNT_TITLE_NAME_100 = "来店";
    public static final String BILL_ACCOUNT_TITLE_NAME_110 = "加盟店送客手数料（消費税込）";
    public static final String BILL_ACCOUNT_TITLE_NAME_120 = "プレミアム会員マイル使用分（消費税込）";
    public static final String BILL_ACCOUNT_TITLE_NAME_130 = "予約";
    public static final String BILL_ACCOUNT_TITLE_NAME_140 = "予約送客手数料（消費税込）";
    public static final String BILL_ACCOUNT_TITLE_NAME_141 = "予約送客手数料（LVJ・消費税込）";
    public static final String BILL_ACCOUNT_TITLE_NAME_142 = "予約送客手数料（OTA・消費税込）";
    public static final String BILL_ACCOUNT_TITLE_NAME_150 = "ボーナスマイル";
    public static final String BILL_ACCOUNT_TITLE_NAME_200 = "スタンダードコース";
    public static final String BILL_ACCOUNT_TITLE_NAME_210 = "広告料";

    public static final String BILL_ACCOUNT_TITLE_NAME_201 = "スタンダードコース掲載料{0}月分";
    public static final String BILL_ACCOUNT_TITLE_NAME_211 = "ハッピー・ホテル　TOPバナー広告{0}月分";
    public static final String BILL_ACCOUNT_TITLE_NAME_212 = "YAHOO!地域情報{0}月分";
    public static final String BILL_ACCOUNT_TITLE_NAME_213 = "ハッピー・ホテル　企画広告{0}月分";
    public static final String BILL_ACCOUNT_TITLE_NAME_214 = "ハッピー・ホテル　ターゲティングメール{0}月分";
    public static final String BILL_ACCOUNT_TITLE_NAME_215 = "ハッピー・ホテル　エリア広告{0}月分";

    // ▼伝票区分
    public static final int    POINT_RAITEN                = 1;                         // 来店ポイント

    // ▼締め区分
    public static final int    CLOSING_KBN_MISYORI         = 1;                         // 未処理
    public static final int    CLOSING_KBN_KARI            = 2;                         // 仮締め
    public static final int    CLOSING_KBN_HON             = 3;                         // 本締め
    public static final String CLOSING_NM_KARI             = "仮";

    // ▼登録状態
    public static final int    REGIST_FLG_KARI             = 0;
    public static final int    REGIST_FLG_HONT             = 1;
    public static final String REGIST_NM_KARI              = "仮";

    /**
     * 売掛明細データクラス取得（来店）
     * 
     * @return DataBkoAccountRecvDetail
     */
    public static DataBkoAccountRecvDetail GetDetailRaiten()
    {
        DataBkoAccountRecvDetail detail = new DataBkoAccountRecvDetail();
        detail.setSlipKind( SLIP_KIND_DEFAULT );
        detail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 );
        detail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_100 );
        detail.setAmount( 0 );
        detail.setPoint( POINT_RAITEN );
        return detail;
    }

    /**
     * 売掛明細データクラス取得（利用金額）
     * 
     * @param amount 利用金額
     * @return DataBkoAccountRecvDetail
     */
    public static DataBkoAccountRecvDetail GetDetailRiyou(int amount)
    {
        DataBkoAccountRecvDetail detail = new DataBkoAccountRecvDetail();
        detail.setSlipKind( SLIP_KIND_SEISAN );
        detail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
        detail.setAccountTitleName( ACCOUNT_TITLE_NAME_110 );
        detail.setAmount( (int)Math.floor( amount * PERCENT_SEISAN_AMOUNT / 100 ) );
        detail.setPoint( (int)Math.floor( amount * PERCENT_SEISAN_POINT / 100 ) );
        return detail;
    }

    /**
     * 売掛明細データクラス取得（予約金額）（※来店明細画面の予約金額→送客手数料）
     * 
     * @param amount 予約金額
     * @return DataBkoAccountRecvDetail
     */
    public static DataBkoAccountRecvDetail GetDetailYoyaku(int amount)
    {
        DataBkoAccountRecvDetail detail = new DataBkoAccountRecvDetail();
        detail.setSlipKind( SLIP_KIND_RESERVE );
        detail.setAccountTitleCd( ACCOUNT_TITLE_CD_140 );
        detail.setAccountTitleName( ACCOUNT_TITLE_NAME_140 );
        detail.setAmount( (int)Math.floor( amount * PERCENT_YOYAKU_AMOUNT / 100 ) );
        detail.setPoint( 0 );
        return detail;
    }

    /**
     * 売掛明細データクラス取得（予約金額）（※来店明細画面のボーナスマイル）
     * 
     * @param amount 予約金額
     * @return DataBkoAccountRecvDetail
     */
    public static DataBkoAccountRecvDetail GetDetailBonusMile(int bonus_mile)
    {
        DataBkoAccountRecvDetail detail = new DataBkoAccountRecvDetail();
        detail.setSlipKind( SLIP_KIND_RESERVE );
        detail.setAccountTitleCd( ACCOUNT_TITLE_CD_150 );
        detail.setAccountTitleName( ACCOUNT_TITLE_NAME_150 );
        detail.setAmount( bonus_mile );
        detail.setPoint( 0 );
        return detail;
    }

    /**
     * 売掛明細データクラス取得（割引ポイント）
     * 
     * @param point 割引ポイント
     * @return DataBkoAccountRecvDetail
     */
    public static DataBkoAccountRecvDetail GetDetailWaribiki(int point)
    {
        DataBkoAccountRecvDetail detail = new DataBkoAccountRecvDetail();
        detail.setSlipKind( SLIP_KIND_SEISAN );
        detail.setAccountTitleCd( ACCOUNT_TITLE_CD_120 );
        detail.setAccountTitleName( ACCOUNT_TITLE_NAME_120 );
        detail.setAmount( point );
        detail.setPoint( point );
        return detail;
    }

    /**
     * 売掛明細データクラス取得（スタンダードコース）
     * 
     * @param amount スタンダードコース料金
     * @return DataBkoAccountRecvDetail
     */
    public static DataBkoAccountRecvDetail GetDetailStandard(int amount)
    {
        DataBkoAccountRecvDetail detail = new DataBkoAccountRecvDetail();
        detail.setSlipKind( SLIP_KIND_OTHER );
        detail.setAccountTitleCd( ACCOUNT_TITLE_CD_200 );
        detail.setAccountTitleName( ACCOUNT_TITLE_NAME_200 );
        detail.setAmount( amount );
        detail.setPoint( 0 );
        return detail;
    }

    /**
     * 売掛明細データクラス取得（広告料）
     * 
     * @param amount 広告料
     * @return DataBkoAccountRecvDetail
     */
    public static DataBkoAccountRecvDetail GetDetailKoukoku(int amount)
    {
        DataBkoAccountRecvDetail detail = new DataBkoAccountRecvDetail();
        detail.setSlipKind( SLIP_KIND_OTHER );
        detail.setAccountTitleCd( ACCOUNT_TITLE_CD_210 );
        detail.setAccountTitleName( ACCOUNT_TITLE_NAME_210 );
        detail.setAmount( amount );
        detail.setPoint( 0 );
        return detail;
    }

    /**
     * 売掛明細データクラス取得（付与ポイント）
     * 
     * @param point 付与ポイント
     * @return DataBkoAccountRecvDetail
     */
    public static DataBkoAccountRecvDetail GetDetailHuyo(int point)
    {
        DataBkoAccountRecvDetail detail = new DataBkoAccountRecvDetail();
        detail.setSlipKind( SLIP_KIND_SEISAN );
        detail.setAccountTitleCd( ACCOUNT_TITLE_CD_110 );
        detail.setAccountTitleName( ACCOUNT_TITLE_NAME_110 );
        detail.setAmount( point * 2 );
        detail.setPoint( point );
        return detail;
    }

    /**
     * 売掛データ登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param dataAccount DataBkoAccountRecv
     * @return true:正常、false:失敗
     */
    public static boolean RegistRecv(PreparedStatement prestate, Connection conn, DataBkoAccountRecv dataAccount) throws Exception
    {
        String query = "";
        query = query + "INSERT hh_bko_account_recv ( ";
        query = query + "hotel_id, ";
        query = query + "id, ";
        query = query + "add_up_date, ";
        query = query + "slip_update, ";
        query = query + "bill_cd, ";
        query = query + "bill_name, ";
        query = query + "div_name, ";
        query = query + "person_name, ";
        query = query + "user_management_no, ";
        query = query + "usage_date, ";
        query = query + "usage_time, ";
        query = query + "ht_slip_no, ";
        query = query + "ht_room_no, ";
        query = query + "usage_charge, ";
        query = query + "receive_charge, ";
        query = query + "happy_balance, ";
        query = query + "accrecv_amount, ";
        query = query + "reconcile_amount, ";
        query = query + "accrecv_balance, ";
        query = query + "remarks, ";
        query = query + "correction, ";
        query = query + "temp_slip_no, ";
        query = query + "first_accrecv_slip_no, ";
        query = query + "credit_note_flag, ";
        query = query + "invalid_flag, ";
        query = query + "regist_flag, ";
        query = query + "closing_kind, ";
        query = query + "owner_hotel_id, ";
        query = query + "owner_user_id, ";
        query = query + "last_update, ";
        query = query + "last_uptime, ";
        query = query + "ci_seq ";
        query = query + ") ";
        query = query + "SELECT ";
        query = query + "  ? "; // ホテルID（ホテナビ）
        query = query + " ,? "; // ホテルID（ハピホテ）
        query = query + " ,? "; // 計上日
        query = query + " ,? "; // 伝票更新日
        query = query + " ,? "; // 請求先コード
        query = query + " ,bill_name "; // 請求先名
        query = query + " ,bill_div_name "; // 部署名
        query = query + " ,? "; // 担当者名
        query = query + " ,? "; // ユーザ管理番号
        query = query + " ,? "; // 利用日
        query = query + " ,? "; // 利用時刻
        query = query + " ,? "; // 伝票No（ハピタッチ）
        query = query + " ,? "; // 部屋番号（ハピタッチ）
        query = query + " ,? "; // 利用金額
        query = query + " ,? "; // 予約金額
        query = query + " ,? "; // ハピー残高
        query = query + " ,? "; // 売掛金額
        query = query + " ,0 "; // 消込額
        query = query + " ,? "; // 売掛残
        query = query + " ,'' "; // 摘要
        query = query + " ,'' "; // 訂正理由
        query = query + " ,0 "; // 仮伝票番号
        query = query + " ,0 "; // 初回売掛伝票No
        query = query + " ,0 "; // 赤伝フラグ
        query = query + " ,0 "; // 無効フラグ
        query = query + " ,1 "; // 登録フラグ
        query = query + " ,? "; // 締め処理区分
        query = query + " ,'' "; // オーナーホテルID
        query = query + " ,0 "; // オーナーユーザID
        query = query + " ,? "; // 最終更新日
        query = query + " ,? "; // 最終更新時刻
        query = query + " ,? "; // チェックインコード

        query = query + "FROM hh_bko_billing "; // 請求先マスタ
        query = query + "WHERE bill_cd = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, dataAccount.getHotelId() ); // ホテルID（ホテナビ）
            prestate.setInt( 2, dataAccount.getId() ); // ホテルID（ハピホテ）
            prestate.setInt( 3, dataAccount.getAddUpDate() ); // 計上日
            prestate.setInt( 4, dataAccount.getSlipUpdate() ); // 伝票更新日
            prestate.setInt( 5, dataAccount.getBillCd() ); // 請求先コード
            prestate.setString( 6, dataAccount.getPersonName() ); // 担当者名
            prestate.setInt( 7, dataAccount.getUserManagementNo() );// ユーザ管理番号
            prestate.setInt( 8, dataAccount.getUsageDate() ); // 利用日
            prestate.setInt( 9, dataAccount.getUsageTime() ); // 利用時刻
            prestate.setInt( 10, dataAccount.getHtSlipNo() ); // 伝票No（ハピタッチ）
            prestate.setString( 11, dataAccount.getHtRoomNo() ); // 部屋番号（ハピタッチ）
            prestate.setInt( 12, dataAccount.getUsageCharge() ); // 利用金額
            prestate.setInt( 13, dataAccount.getReceiveCharge() ); // 予約金額
            prestate.setInt( 14, dataAccount.getHappyBalance() ); // ハピー残高
            prestate.setInt( 15, dataAccount.getAccrecvAmount() ); // 売掛金額
            prestate.setInt( 16, dataAccount.getAccrecvBalance() ); // 売掛残
            prestate.setInt( 17, dataAccount.getClosingKind() ); // 締め処理区分
            // prestate.setString( 18, dataAccount.getOwnerHotelId() );// オーナーホテルID
            // prestate.setInt( 19, dataAccount.getOwnerUserId() ); // オーナーユーザID
            prestate.setInt( 18, dataAccount.getLastUpdate() ); // 最終更新日
            prestate.setInt( 19, dataAccount.getLastUptime() ); // 最終更新時刻
            prestate.setInt( 20, dataAccount.getCiSeq() ); // チェックインコード
            prestate.setInt( 21, dataAccount.getBillCd() ); // 請求者コード

            // System.out.println("■" + dataAccount.getBillCd());
            if ( prestate.executeUpdate() <= 0 )
            {
                Logging.error( "OwnerBkoCommon.RegistRecv] Exception:", "" );
                throw new Exception( "[OwnerBkoCommon.RegistRecv] insert error" );
            }

            // 初回売掛伝票Noの更新
            int accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, conn );
            // System.out.println("■" + accrecvSlipNo);

            if ( accrecvSlipNo == -1 )
            {
                throw new Exception( "[OwnerBkoCommon.RegistRecv] GetInsertedAccrecvSlipNo error" );
            }

            query = "UPDATE hh_bko_account_recv ";
            query = query + "SET first_accrecv_slip_no = ? ";
            query = query + "WHERE accrecv_slip_no = ? ";

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            prestate.setInt( 2, accrecvSlipNo );
            // System.out.println("■3" + accrecvSlipNo);
            if ( prestate.executeUpdate() <= 0 )
            {
                throw new Exception( "[OwnerBkoCommon.RegistRecv] UPDATE error" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "OwnerBkoCommon.RegistRecv] Exception:" + e.toString(), "RegistRecv" );
            System.out.println( "[OwnerBkoCommon.RegistRecv] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * 担当者名取得
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param hotelId ホテルID（ホテナビ）
     * @param userId 従業員コード
     * @return 担当者名
     */
    public static String GetPersonName(PreparedStatement prestate, Connection conn, String hotelId, int userId)
    {
        String query;
        ResultSet result = null;

        query = "SELECT name FROM owner_user ";
        query = query + "WHERE hotelid = ? ";
        query = query + "AND userid = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    return result.getString( "name" );
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.GetPersonName] Exception=" + e.toString() );
            return "";
        }

        return "";
    }

    /**
     * 追加した売掛伝票No
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return 売掛伝票No
     */
    public static int GetInsertedAccrecvSlipNo(PreparedStatement prestate, Connection conn)
    {
        String query;
        ResultSet result = null;

        query = "SELECT LAST_INSERT_ID() AS accrecv_slip_no";

        try
        {
            prestate = conn.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    return result.getInt( "accrecv_slip_no" );
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.GetInsertedAccrecvSlipNo] Exception=" + e.toString() );
            return -1;
        }

        return -1;
    }

    /**
     * 売掛明細データ登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param dataDetail DataBkoAccountRecvDetailNo
     * @return true:正常、false:失敗
     */
    public static boolean RegistRecvDetail(PreparedStatement prestate, Connection conn, DataBkoAccountRecvDetail dataDetail)
            throws Exception
    {
        String query = "";
        query = query + "INSERT hh_bko_account_recv_detail SET ";
        query = query + "accrecv_slip_no = ?, ";
        query = query + "slip_detail_no = ?, ";
        query = query + "slip_kind = ?, ";
        query = query + "account_title_cd = ?, ";
        query = query + "account_title_name = ?, ";
        query = query + "amount = ?, ";
        query = query + "point = ?, ";
        query = query + "id = ?, ";
        query = query + "reserve_no = ?, ";
        query = query + "user_id = ?, ";
        query = query + "seq = ?, ";
        query = query + "closing_kind = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, dataDetail.getAccrecvSlipNo() );
            prestate.setInt( 2, dataDetail.getSlipDetailNo() );
            prestate.setInt( 3, dataDetail.getSlipKind() );
            prestate.setInt( 4, dataDetail.getAccountTitleCd() );
            prestate.setString( 5, dataDetail.getAccountTitleName() );
            prestate.setInt( 6, dataDetail.getAmount() );
            prestate.setInt( 7, dataDetail.getPoint() );
            prestate.setInt( 8, dataDetail.getId() );
            prestate.setString( 9, dataDetail.getReserveNo() );
            prestate.setString( 10, dataDetail.getUserId() );
            prestate.setInt( 11, dataDetail.getSeq() );
            prestate.setInt( 12, dataDetail.getClosingKind() );

            if ( prestate.executeUpdate() <= 0 )
            {
                throw new Exception( "[OwnerBkoCommon.RegistRecvDetail] RegistRecvDetail error" );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.RegistRecvDetail] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * 売掛データ更新
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param accrecv_slip_no 売掛伝票No
     * @param accrecv_amount 売掛金額（売掛残）
     * @return true:正常、false:失敗
     */
    public static boolean UpdateRecv(PreparedStatement prestate, Connection conn, int accrecv_slip_no, int accrecv_amount) throws Exception
    {
        String query = "";
        query = query + "UPDATE hh_bko_account_recv SET ";
        query = query + "accrecv_amount = ?, ";
        query = query + "accrecv_balance = ? ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, accrecv_amount );
            prestate.setInt( 2, accrecv_amount );
            prestate.setInt( 3, accrecv_slip_no );

            if ( prestate.executeUpdate() > 0 )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.UpdateRecv] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return false;
    }

    /**
     * 請求・売掛データ登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param billSlipNo 請求伝票No
     * @param slipDetailNo 伝票明細No
     * @param accrecvSlipNo 売掛伝票No
     * @param closingKind 締め処理区分
     * @return true:正常、false:失敗
     */
    public static boolean RegistRelBillAccountRecv(PreparedStatement prestate, Connection conn, int billSlipNo, int slipDetailNo, int accrecvSlipNo, int closingKind) throws Exception
    {
        String query = "";
        query = query + "INSERT hh_bko_rel_bill_account_recv SET ";
        query = query + "bill_slip_no = ?, ";
        query = query + "slip_detail_no = ?, ";
        query = query + "accrecv_slip_no = ?,";
        query = query + "closing_kind = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, billSlipNo );
            prestate.setInt( 2, slipDetailNo );
            prestate.setInt( 3, accrecvSlipNo );
            prestate.setInt( 4, closingKind );

            if ( prestate.executeUpdate() <= 0 )
            {
                throw new Exception( "[OwnerBkoCommon.RegistRelBillAccountRecv] RegistRelBillAccountRecv error" );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.RegistRelBillAccountRecv] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * 請求データ更新
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param billSlipNo 請求伝票No
     * @param billKin 請求金額（税込み分）
     * @param billKinNotInc 請求金額（税抜き分）
     * @return true:正常、false:失敗
     */
    public static boolean UpdateBill(PreparedStatement prestate, Connection conn, int billSlipNo, int billKin, int billKinNotInc) throws Exception
    {
        String query = "";
        query = query + "UPDATE hh_bko_bill SET ";
        query = query + "charge_inc_tax=?, ";
        query = query + "charge_not_inc_tax=?, ";
        query = query + "tax=? ";
        query = query + "WHERE bill_slip_no = ? ";

        try
        {
            int notIncTax = (int)Math.floor( billKinNotInc * OwnerBkoCommon.TAX ); // 税抜き分の消費税

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, billKin + billKinNotInc + notIncTax ); // 請求金額（税込み）
            prestate.setInt( 2, billKin + billKinNotInc ); // 請求金額（税抜き）
            prestate.setInt( 3, notIncTax ); // 消費税
            prestate.setInt( 4, billSlipNo );

            if ( prestate.executeUpdate() <= 0 )
            {
                throw new Exception( "[OwnerBkoCommon.UpdateBill] UpdateBill error" );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.UpdateBill] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * 請求明細データ登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param billSlipNo 請求伝票No
     * @param lineNo 項目No
     * @param brandName 商品名及び仕様
     * @param quantity 数量
     * @param unitPrice 単価
     * @param amount 金額
     * @param closingKind 締め処理区分
     * @return true:正常、false:失敗
     */
    public static boolean RegistBillDetail(PreparedStatement prestate, Connection conn, int billSlipNo, int lineNo, int account_title_cd, String brandName, int quantity, int unitPrice, int amount, int closingKind) throws Exception
    {
        String query = "";
        query = query + "INSERT hh_bko_bill_detail SET ";
        query = query + "bill_slip_no = ?, ";
        query = query + "line_no = ?, ";
        query = query + "account_title_cd = ?,";
        query = query + "brand_name = ?,";
        query = query + "quantity = ?, ";
        query = query + "unit_price = ?, ";
        query = query + "amount = ?,";
        query = query + "closing_kind = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, billSlipNo );
            prestate.setInt( 2, lineNo );
            prestate.setInt( 3, account_title_cd );
            prestate.setString( 4, brandName );
            prestate.setInt( 5, quantity );
            prestate.setInt( 6, unitPrice );
            prestate.setInt( 7, amount );
            prestate.setInt( 8, closingKind );

            if ( prestate.executeUpdate() > 0 )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.RegistBillDetail] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return false;
    }

    /**
     * 科目名取得
     * 
     * @param accountTitileCd 科目コード
     * @return 科目名
     */
    public static String GetAccountTitleName(int accountTitileCd)
    {
        switch( accountTitileCd )
        {
            case ACCOUNT_TITLE_CD_100:
                return ACCOUNT_TITLE_NAME_100;
            case ACCOUNT_TITLE_CD_110:
                return ACCOUNT_TITLE_NAME_110;
            case ACCOUNT_TITLE_CD_120:
                return ACCOUNT_TITLE_NAME_120;
            case ACCOUNT_TITLE_CD_130:
                return ACCOUNT_TITLE_NAME_130;
            case ACCOUNT_TITLE_CD_140:
                return ACCOUNT_TITLE_NAME_140;
            case ACCOUNT_TITLE_CD_141:
                return ACCOUNT_TITLE_NAME_141;
            case ACCOUNT_TITLE_CD_142:
                return ACCOUNT_TITLE_NAME_142;
            case ACCOUNT_TITLE_CD_150:
                return ACCOUNT_TITLE_NAME_150;
            case ACCOUNT_TITLE_CD_201:
                return ACCOUNT_TITLE_NAME_201;
            case ACCOUNT_TITLE_CD_211:
                return ACCOUNT_TITLE_NAME_211;
            case ACCOUNT_TITLE_CD_212:
                return ACCOUNT_TITLE_NAME_212;
            case ACCOUNT_TITLE_CD_213:
                return ACCOUNT_TITLE_NAME_213;
            case ACCOUNT_TITLE_CD_214:
                return ACCOUNT_TITLE_NAME_214;
            case ACCOUNT_TITLE_CD_215:
                return ACCOUNT_TITLE_NAME_215;
        }
        return "";
    }

    /**
     * 科目名取得（請求明細用）
     * 
     * @param accountTitileCd 科目コード
     * @return 科目名
     */
    public static String GetBillAccountTitleName(int accountTitileCd)
    {
        switch( accountTitileCd )
        {
            case ACCOUNT_TITLE_CD_100:
                return BILL_ACCOUNT_TITLE_NAME_100;
            case ACCOUNT_TITLE_CD_110:
                return BILL_ACCOUNT_TITLE_NAME_110;
            case ACCOUNT_TITLE_CD_120:
                return BILL_ACCOUNT_TITLE_NAME_120;
            case ACCOUNT_TITLE_CD_130:
                return BILL_ACCOUNT_TITLE_NAME_130;
            case ACCOUNT_TITLE_CD_140:
                return BILL_ACCOUNT_TITLE_NAME_140;
            case ACCOUNT_TITLE_CD_141:
                return BILL_ACCOUNT_TITLE_NAME_141;
            case ACCOUNT_TITLE_CD_142:
                return BILL_ACCOUNT_TITLE_NAME_142;
            case ACCOUNT_TITLE_CD_150:
                return BILL_ACCOUNT_TITLE_NAME_150;
            case ACCOUNT_TITLE_CD_201:
                return BILL_ACCOUNT_TITLE_NAME_201;
            case ACCOUNT_TITLE_CD_211:
                return BILL_ACCOUNT_TITLE_NAME_211;
            case ACCOUNT_TITLE_CD_212:
                return BILL_ACCOUNT_TITLE_NAME_212;
            case ACCOUNT_TITLE_CD_213:
                return BILL_ACCOUNT_TITLE_NAME_213;
            case ACCOUNT_TITLE_CD_214:
                return BILL_ACCOUNT_TITLE_NAME_214;
            case ACCOUNT_TITLE_CD_215:
                return BILL_ACCOUNT_TITLE_NAME_215;
        }
        return "";
    }

    /**
     * 請求明細データ作成
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param billSlipNo 請求伝票No
     * @return true:正常、false:失敗
     */
    public static boolean RegistBillDetail(PreparedStatement prestate, Connection conn, int billSlipNo) throws Exception
    {
        boolean ret = true;
        String query = "";
        ResultSet result = null;

        try
        {
            if ( billSlipNo > 0 )
            {
                query = query + "DELETE FROM hh_bko_bill_detail ";
                query = query + "WHERE bill_slip_no = ? ";

                prestate = conn.prepareStatement( query );
                if ( billSlipNo > 0 )
                {
                    prestate.setInt( 1, billSlipNo );
                }

                prestate.executeUpdate(); // 新規時、削除は0件
            }

            // 請求明細データ作成
            // 対象となる売掛データ取得
            query = "SELECT bh.bill_slip_no ,bh.bill_date ,ad.account_title_cd ,SUM(CASE WHEN ah.credit_note_flag = 1 THEN -ad.amount ELSE ad.amount END) AS amount ";
            query = query + "FROM hh_bko_bill bh ";

            query = query + "INNER JOIN hh_bko_rel_bill_account_recv br ";
            query = query + "ON br.bill_slip_no = bh.bill_slip_no ";

            query = query + "INNER JOIN hh_bko_account_recv ah ";
            query = query + "ON ah.accrecv_slip_no = br.accrecv_slip_no ";

            query = query + "INNER JOIN hh_bko_account_recv_detail ad ";
            query = query + "ON ad.accrecv_slip_no = br.accrecv_slip_no ";

            if ( billSlipNo > 0 )
            {
                query = query + "WHERE bh.bill_slip_no = ? ";
            }
            else
            {
                query = query + "WHERE bh.closing_kind = 1 ";
            }

            query = query + "AND NOT (ad.slip_kind = 0 AND ad.account_title_cd = 100) ";// ポイントの来店は請求しないので除く
            query = query + "AND NOT (ad.slip_kind = 0 AND ad.account_title_cd = 130) ";// ポイントの予約は請求しないので除く

            query = query + "GROUP BY bh.bill_slip_no ,ad.account_title_cd ";
            query = query + "ORDER BY bh.bill_slip_no ,ad.account_title_cd ";

            prestate = conn.prepareStatement( query );
            if ( billSlipNo > 0 )
            {
                prestate.setInt( 1, billSlipNo );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                // 請求明細データを登録していく

                int wkBillSlipNo = -1;
                int lineNo = 0;

                while( result.next() )
                {

                    if ( wkBillSlipNo != result.getInt( "bh.bill_slip_no" ) )
                    {
                        lineNo = 0;
                        wkBillSlipNo = result.getInt( "bh.bill_slip_no" );
                    }

                    lineNo = lineNo + 1;

                    // 請求明細データ登録
                    if ( ret )
                    {
                        String brandName = OwnerBkoCommon.GetBillAccountTitleName( result.getInt( "ad.account_title_cd" ) );
                        brandName = brandName.replace( "{0}", String.valueOf( Integer.parseInt( result.getString( "bh.bill_date" ).substring( 4, 6 ) ) ) );
                        brandName = brandName + "[641-6-" + result.getInt( "bh.bill_slip_no" ) + "-" + String.format( "%02d", lineNo ) + "]";

                        if ( billSlipNo > 0 )
                        {
                            ret = OwnerBkoCommon.RegistBillDetail( prestate, conn, result.getInt( "bh.bill_slip_no" ), lineNo, result.getInt( "ad.account_title_cd" ), brandName, 1, result.getInt( "amount" ),
                                    result.getInt( "amount" ), OwnerBkoCommon.CLOSING_KBN_KARI );
                        }
                        else
                        {
                            ret = OwnerBkoCommon.RegistBillDetail( prestate, conn, result.getInt( "bh.bill_slip_no" ), lineNo, result.getInt( "ad.account_title_cd" ), brandName, 1, result.getInt( "amount" ),
                                    result.getInt( "amount" ), OwnerBkoCommon.CLOSING_KBN_MISYORI );
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.RegistBillDetail] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return ret;
    }

    /**
     * 請求閲覧可能担当者フラグをセットする。
     * 
     * @param hotelID ホテルID
     * @param userID ユーザID
     * @return 1:請求閲覧可能担当者、0:閲覧不可
     */
    public static int getBillOwnFlg(String hotelID, int userID) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;

        query = query + "SELECT sec_level20 FROM owner_user_security ";
        query = query + "WHERE hotelid = ? AND userid = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelID );
            prestate.setInt( 2, userID );
            result = prestate.executeQuery();

            while( result.next() )
            {
                ret = result.getInt( "sec_level20" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerBkoCommon.getBillOwnFlg] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 締年月取得
     * 
     * @param hotelId ホテルID
     * @return 締め年月(YYYYMM)
     * 
     */
    public static int getClosingMonth()
    {
        int ret = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";

        try
        {
            query = "select closing_date from hh_bko_closing_control WHERE closing_kind >= ? ORDER BY closing_date DESC";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, CLOSING_KBN_KARI );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                ret = result.getInt( "closing_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerBkoCommon.getClosingMonth] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 消費税率取得
     * 
     * @param 締め年月(YYYYMM)
     * @return 消費税率
     * 
     */
    public static int getTax(int closingDate)
    {
        int ret = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";

        try
        {
            query = "select tax from hh_bko_closing_control WHERE closing_date = ? ";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, closingDate );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                ret = result.getInt( "tax" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerBkoCommon.getClosingMonth] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 消費税再計算
     * 
     * @param 再計算前金額
     * @param 消費税
     * @return 再計算後金額
     * 
     */
    public static int reCalctTax(int amount, int tax)
    {
        int ret = 0;
        double dtax = 0;
        BigDecimal bd_temp;
        double d_temp;
        try
        {
            dtax = ((double)tax / 1000);
            dtax = 1 + dtax;
            bd_temp = new BigDecimal( String.valueOf( amount / dtax ) );
            bd_temp = bd_temp.setScale( 0, BigDecimal.ROUND_HALF_UP ); // 1円未満四捨五入
            d_temp = bd_temp.doubleValue() * dtax; // さらに税率(1.08)を掛ける
            bd_temp = new BigDecimal( String.valueOf( d_temp ) );
            ret = bd_temp.setScale( 0, BigDecimal.ROUND_HALF_UP ).intValue(); // 1円未満四捨五入
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerBkoCommon.getClosingMonth] Exception=" + e.toString() );
        }
        return(ret);
    }

    /**
     * 売掛明細情報取得
     * 
     * @param int billDate 請求年月
     * @param int accTitleCd 科目コード
     *        (その他の場合、-1を設定する)
     * @param int selKbn (1:利用金額取得、2：予約金額取得、3:明細の金額, 4:件数)
     * @return int 金額、または件数
     */
    public static int getAccountRecvDetail(int id, int billDate, int accTitleCd, int selKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;

        query = query + "SELECT ";
        query = query + "COUNT(*) as CNT, SUM(detail.amount) AS amount, SUM(rcv.usage_charge) AS usgCharge, SUM(rcv.receive_charge) AS rcvCharge ";
        query = query + "FROM hh_bko_bill bill ";
        query = query + " INNER JOIN hh_bko_rel_bill_account_recv bdt ON bill.bill_slip_no = bdt.bill_slip_no ";
        query = query + "   LEFT JOIN hh_bko_account_recv rcv ON bdt.accrecv_slip_no = rcv.accrecv_slip_no ";
        query = query + "     LEFT JOIN hh_bko_account_recv_detail detail ON rcv.accrecv_slip_no = detail.accrecv_slip_no ";
        // 10日締めなので基準日を毎月10日とする
        query = query + " INNER JOIN hh_hotel_newhappie happie ON bill.id = happie.id AND bill.bill_date * 100 + 10 >= happie.bko_date_start ";
        query = query + "WHERE bill.id = ? ";
        query = query + "  AND bill.bill_date = ? ";
        query = query + "  AND rcv.invalid_flag = ? ";

        if ( accTitleCd != -1 )
        {
            query = query + "  AND detail.account_title_cd = ? ";
        }
        else
        {
            query = query + "  AND (detail.account_title_cd >= " + OwnerBkoCommon.ACCOUNT_TITLE_CD_200 + ")";
        }
        query = query + " GROUP BY bill.bill_date ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, billDate );
            prestate.setInt( 3, 0 );
            if ( accTitleCd != -1 )
            {
                prestate.setInt( 4, accTitleCd );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( selKbn == 1 )
                {
                    ret = result.getInt( "usgCharge" );

                }
                else if ( selKbn == 2 )
                {
                    ret = result.getInt( "rcvCharge" );

                }
                else if ( selKbn == 3 )
                {
                    ret = result.getInt( "amount" );
                }
                else if ( selKbn == 4 )
                {
                    ret = result.getInt( "CNT" );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoGroupBill.getAccountRecvDetail] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

}
