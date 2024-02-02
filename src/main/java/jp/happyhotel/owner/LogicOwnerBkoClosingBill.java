package jp.happyhotel.owner;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.data.DataBkoAccountRecv;
import jp.happyhotel.data.DataBkoAccountRecvDetail;
import jp.happyhotel.data.DataHhRsvSystemConfList;
import jp.happyhotel.user.UserBasicInfo;

import org.apache.commons.lang.StringUtils;

public class LogicOwnerBkoClosingBill implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID        = -1694452370598598055L;

    // 請求・支払（予約）　パーセンテージ(%)
    private static final int    PERCENT_RESERVE         = 5;
    private static final int    PERCENT_NEW_RESERVE     = 8;
    private static final int    PERCENT_NEW_RESERVE_LVJ = 10;

    // 最終更新日
    private int                 lastUpdate              = 0;

    // 最終更新日時
    private int                 lastUptime              = 0;

    // 支払期限日
    private int                 paymentDate             = 0;

    private static final String CONF_FILE               = "/etc/happyhotel/backoffice.conf";
    private static final String MAIL_TO                 = "mail.to";
    private static final String MAIL_FROM               = "mail.from";
    private static final String MAIL_ERRORTITLE         = "mail.errortitle";
    private static final String MAIL_SUCCESSTITLE       = "mail.successtitle";

    // 請求書番号先頭文字
    private static final String BILL_NO_HEADER          = "641";

    // 20110808 削除する請求データの請求伝票番号を保持 START
    Hashtable<String, Integer>  hashBill                = new Hashtable<String, Integer>();
    int                         monthStartDay           = 0;                                // 締め年月開始日
    int                         monthEndDay             = 0;                                // 締め年月終了日

    public boolean execute(Connection connection, String[] args)
    {
        // boolean returnSts = true;
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = true;
        int startDay = 0;
        int endDay = 0;
        DataBkoAccountRecv dataAcc = null;
        int closingDate = 0;
        int closingKbn = 0;
        boolean controlChangeFlg = false;

        try
        {
            // 引数チェック
            if ( args.length != 4 )
            {
                Logging.info( "[LogicOwnerBkoClogingBill]Parameter Count Error" + args.length );
                return false;
            }

            try
            {
                closingDate = Integer.parseInt( args[0] );
                closingKbn = Integer.parseInt( args[1] );
                startDay = Integer.parseInt( args[2] );
                endDay = Integer.parseInt( args[3] );
            }
            catch ( Exception e )
            {
                Logging.info( "[LogicOwnerBkoClogingBill] Parameter Int Error" );
                return false;
            }

            // 2:仮締め or 3:本締め 以外の場合はエラー
            if ( !(OwnerBkoCommon.CLOSING_KBN_KARI == closingKbn || OwnerBkoCommon.CLOSING_KBN_HON == closingKbn) )
            {
                Logging.info( "[LogicOwnerBkoClogingBill] Parameter closing_kind Error" );
                return false;
            }

            // 締め年月が存在する年月かチェック
            if ( !DateEdit.checkDate( String.valueOf( closingDate ).substring( 0, 4 ) + "/" + String.valueOf( closingDate ).substring( 4, 6 ) + "/01" ) )
            {
                Logging.info( "[LogicOwnerBkoClogingBill] Parameter closing_date Error" + closingDate );
                return false;
            }

            // 仮締めの場合は、対象開始日、対象終了日の妥当性チェックを実施
            if ( OwnerBkoCommon.CLOSING_KBN_KARI == closingKbn )
            {
                // 対象開始日が存在する年月日かチェック
                if ( !DateEdit.checkDate( String.valueOf( startDay ).substring( 0, 4 ) + "/" + String.valueOf( startDay ).substring( 4, 6 ) + "/" + String.valueOf( startDay ).substring( 6, 8 ) ) )
                {
                    Logging.info( "[LogicOwnerBkoClogingBill] Parameter startDay Error" + startDay );
                    return false;
                }

                // 対象終了日が存在する年月日かチェック
                if ( !DateEdit.checkDate( String.valueOf( endDay ).substring( 0, 4 ) + "/" + String.valueOf( endDay ).substring( 4, 6 ) + "/" + String.valueOf( endDay ).substring( 6, 8 ) ) )
                {
                    Logging.info( "[LogicOwnerBkoClogingBill] Parameter endDay Error" + endDay );
                    return false;
                }

                // 対象開始日＜対象終了日であるかチェック
                if ( startDay >= endDay )
                {
                    Logging.info( "[LogicOwnerBkoClogingBill] Parameter startDay,endDay Error" + startDay + "," + endDay );
                    return false;
                }
            }

            lastUpdate = Integer.parseInt( DateEdit.getDate( 2 ) );
            lastUptime = Integer.parseInt( DateEdit.getTime( 1 ) );

            // ALMEX側で追加
            // 20110808
            if ( Integer.parseInt( DateEdit.getDate( 2 ) ) % 100 > OwnerBkoCommon.SIME_DATE + 2 )
            {
                // 20110913 12日を超えている場合、締め年月は +1 にする
                // closingDate = Integer.parseInt( DateEdit.getDate( 2 ) ) / 100;
                // closingDate = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), 1 ) / 100;
            }

            try
            {
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                Logging.info( "[LogicOwnerBkoClogingBill]" + "START TRANSACTION ", "[LogicOwnerBkoClogingBill]" );

                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );

                // 期限日の取得
                paymentDate = getPaymentDate( prestate, connection );
                // System.out.println("paymentDate:" + paymentDate);
                if ( paymentDate == -1 )
                {
                    Logging.error( "[LogicOwnerBkoClogingBill] 支払期限日の取得ができません" );
                    return false;
                }

                controlChangeFlg = true;

                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );

                // 締め年月開始・終了をセット
                setMonthStartDate( closingDate );

                // 仮締め
                if ( OwnerBkoCommon.CLOSING_KBN_KARI == closingKbn )
                {
                    query = "SELECT ";
                    query = query + "bill_slip_no, "; // 請求伝票No
                    query = query + "bill_cd, "; // 請求先コード
                    query = query + "id "; // ホテルID
                    query = query + "FROM hh_bko_bill ";
                    query = query + "WHERE closing_kind BETWEEN 1 AND 2 "; // 未処理
                    query = query + "AND bill_date = ? ";

                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        while( result.next() )
                        {
                            // 値の格納
                            hashBill.put( String.valueOf( result.getInt( "bill_cd" ) ) + "-" + String.valueOf( result.getInt( "id" ) ), result.getInt( "bill_slip_no" ) );
                        }
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    // 請求明細データ削除
                    query = "DELETE hh_bko_bill_detail.* ";
                    query = query + "FROM hh_bko_bill_detail ";
                    query = query + "INNER JOIN hh_bko_bill ";
                    query = query + "ON hh_bko_bill.bill_slip_no = hh_bko_bill_detail.bill_slip_no ";
                    query = query + "WHERE hh_bko_bill.bill_date = ? ";
                    query = query + "AND hh_bko_bill_detail.closing_kind BETWEEN 1 AND 2 "; // 未処理

                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    prestate.executeUpdate();
                    DBConnection.releaseResources( prestate );

                    // 請求・売掛データ削除
                    query = "DELETE hh_bko_rel_bill_account_recv ";
                    query = query + "FROM hh_bko_rel_bill_account_recv ";
                    query = query + "INNER JOIN hh_bko_bill ";
                    query = query + "ON hh_bko_bill.bill_slip_no = hh_bko_rel_bill_account_recv.bill_slip_no ";
                    query = query + "WHERE hh_bko_bill.bill_date = ? ";
                    query = query + "AND hh_bko_rel_bill_account_recv.closing_kind BETWEEN 1 AND 2 "; // 未処理

                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    prestate.executeUpdate();
                    DBConnection.releaseResources( prestate );

                    // 請求データ削除（請求明細、請求・売掛データを消してから最後に消す）
                    query = "DELETE FROM hh_bko_bill ";
                    query = query + "WHERE closing_kind BETWEEN 1 AND 2 "; // 未処理
                    query = query + "AND bill_date = ? ";
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    prestate.executeUpdate();
                    DBConnection.releaseResources( prestate );

                    // 締め制御データ(hh_bko_closing_control)からlast_updateを取得
                    int closingKind = 0;
                    int hbccLastUpdate = 0;
                    query = "SELECT * FROM hh_bko_closing_control where closing_date= ? ";
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        if ( result.next() != false )
                        {
                            closingKind = result.getInt( "closing_kind" );
                            hbccLastUpdate = result.getInt( "last_update" );
                        }
                    }

                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    // 仮締めのとき
                    if ( closingKind <= 2 && hbccLastUpdate > 0 )
                    {

                        // 売掛データ明細削除
                        query = "DELETE hh_bko_account_recv_detail FROM hh_bko_account_recv_detail ";
                        query = query + "INNER JOIN  hh_bko_account_recv  ";
                        query = query + "ON hh_bko_account_recv.accrecv_slip_no = hh_bko_account_recv_detail.accrecv_slip_no ";
                        query = query + "WHERE hh_bko_account_recv.add_up_date = ? ";
                        query = query + "AND hh_bko_account_recv.closing_kind BETWEEN 1 AND 2 "; // 未処理
                        prestate = connection.prepareStatement( query );
                        prestate.setInt( 1, hbccLastUpdate );
                        prestate.executeUpdate();
                        DBConnection.releaseResources( prestate );

                        // 売掛データ削除
                        query = "DELETE FROM hh_bko_account_recv ";
                        query = query + "WHERE hh_bko_account_recv.add_up_date = ? ";
                        query = query + "AND hh_bko_account_recv.closing_kind BETWEEN 1 AND 2 "; // 未処理
                        prestate = connection.prepareStatement( query );
                        prestate.setInt( 1, hbccLastUpdate );
                        prestate.executeUpdate();
                        DBConnection.releaseResources( prestate );

                    }

                    query = "COMMIT ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    query = "START TRANSACTION ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    // 予約（＝送客手数料 ポイントは上で作成済み）
                    query = "SELECT ";
                    query = query + "hotelbasic.hotenavi_id, "; // ホテナビID
                    query = query + "reserve.user_id, "; // ユーザID
                    query = query + "reserve.charge_total, "; // 総合計
                    query = query + "reserve.add_bonus_mile, "; // ボーナスマイル
                    query = query + "result.seq, "; // 管理番号
                    query = query + "hotelbasic.id, "; // ホテルID
                    query = query + "result.reserve_no, "; // 予約番号
                    query = query + "reserve.reserve_no_main, "; // 親予約番号
                    query = query + "result.regist_date, "; // 登録日付（利用日）
                    query = query + "result.regist_time, "; // 登録時間（利用時刻）
                    query = query + "result.ci_date, "; // 予約日
                    query = query + "basic.hotel_id, "; // オーナーホテルID
                    query = query + "basic.user_id, "; // ユーザID
                    query = query + "billingHotel.bill_cd, "; // 請求先コード
                    query = query + "reserve.ext_flag, "; // 新予約(ext_flag=0のときハピホテ予約 ext_flag=1のときラブインジャパン予約)
                    query = query + "reserve.used_mile, "; // 使用マイル
                    query = query + "reserve.noshow_flag, ";
                    query = query + "reserve.payment, ";
                    query = query + "reserve.cancel_charge, "; // キャンセル料
                    query = query + "reserve.status, "; // ステータス
                    query = query + "CASE WHEN userDataIndex.user_seq IS NULL THEN 0 ELSE userDataIndex.user_seq END AS user_seq ";
                    query = query + "FROM hh_rsv_result result ";
                    query = query + "INNER JOIN newRsvDB.hh_rsv_reserve reserve ";
                    query = query + "ON result.id = reserve.id ";
                    query = query + "AND result.reserve_no = reserve.reserve_no ";
                    query = query + "AND ( reserve.status = 2 OR ( reserve.payment=1 AND reserve.noshow_flag=1 ) OR (reserve.status = 3 AND reserve.ext_flag >= 1) ) "; // チェックインまたはキャンセルまたはノーショーかつ、クレジット実売上げ済み
                    query = query + "INNER JOIN hh_rsv_reserve_basic basic ";
                    query = query + "ON result.id = basic.id ";
                    query = query + "INNER JOIN hh_hotel_basic hotelbasic "; // ホテル基本データ
                    query = query + "ON hotelbasic.id = basic.id ";

                    query = query + "INNER JOIN hh_bko_rel_billing_hotel billingHotel ";// 請求先・ホテル
                    query = query + "ON billingHotel.id = hotelbasic.id ";
                    query = query + "LEFT JOIN hh_user_data_index userDataIndex ";
                    query = query + "ON userDataIndex.user_id = reserve.user_id ";
                    query = query + "AND userDataIndex.id = reserve.id ";
                    query = query + "WHERE result.regist_date * 1000000 + result.regist_time >=  ? * 1000000 + basic.deadline_time ";
                    query = query + "AND result.regist_date * 1000000 + result.regist_time < ? * 1000000 + basic.deadline_time ";
                    // 売掛データ作成（予約分（＝送客手数料（ポイントは上でやる）））
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, startDay );
                    prestate.setInt( 2, endDay );

                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        int accrecvSlipNo = -1;
                        UserBasicInfo ubi = new UserBasicInfo();

                        // 予約（送客手数料）
                        while( result.next() )
                        {
                            int amount = 0;
                            if ( result.getInt( "ext_flag" ) == 0 )
                            {
                                amount = (int)Math.floor( result.getInt( "charge_total" ) * PERCENT_NEW_RESERVE / 100 );
                            }
                            else if ( result.getInt( "ext_flag" ) == 1 )
                            {
                                amount = (int)Math.floor( result.getInt( "charge_total" ) * PERCENT_NEW_RESERVE_LVJ / 100 );
                            }
                            else
                            {
                                amount = (int)Math.floor( result.getInt( "charge_total" ) * PERCENT_NEW_RESERVE_LVJ / 100 );
                            }

                            if ( result.getInt( "noshow_flag" ) == 1 && result.getInt( "payment" ) == 1 )
                            {
                                if ( result.getInt( "ext_flag" ) == 0 )
                                {
                                    amount = (int)Math.floor( result.getInt( "charge_total" ) * PERCENT_NEW_RESERVE / 100 );
                                }
                                else
                                {
                                    amount = (int)Math.floor( result.getInt( "charge_total" ) * PERCENT_NEW_RESERVE_LVJ / 100 );
                                }
                            }
                            else if ( result.getInt( "ext_flag" ) >= 1 && result.getInt( "status" ) == 3 )
                            {
                                amount = (int)Math.floor( result.getInt( "cancel_charge" ) * PERCENT_NEW_RESERVE_LVJ / 100 );
                            }

                            // 売掛データ登録
                            dataAcc = new DataBkoAccountRecv();
                            dataAcc.setHotelId( result.getString( "hotenavi_id" ) ); // ホテルID（ホテナビ）
                            dataAcc.setId( result.getInt( "id" ) ); // ホテルID（ハピホテ）
                            dataAcc.setAddUpDate( lastUpdate ); // 計上日
                            dataAcc.setBillCd( result.getInt( "bill_cd" ) );
                            dataAcc.setPersonName( "" ); // 請求先コード
                            if ( ubi.isLvjUser( result.getString( "reserve.user_id" ) ) )
                            {
                                dataAcc.setUserManagementNo( 0 ); // ユーザ管理番号
                            }
                            else
                            {
                                dataAcc.setUserManagementNo( result.getInt( "user_seq" ) ); // ユーザ管理番号
                            }
                            String rsvNo = result.getString( "reserve_no" );
                            String revNoMain = result.getString( "reserve_no_main" );
                            dataAcc.setUsageDate( result.getInt( "ci_date" ) ); // 利用日
                            if ( !StringUtils.isBlank( revNoMain ) && !rsvNo.substring( rsvNo.indexOf( "-" ) + 1 ).equals( revNoMain ) )
                            {
                                // 連泊予約の途中の宿泊日
                                dataAcc.setUsageTime( 999999 ); // 利用時刻
                            }
                            else
                            {
                                // 一泊目
                                dataAcc.setUsageTime( result.getInt( "regist_time" ) ); // 利用時刻
                                if ( result.getInt( "ci_date" ) < result.getInt( "regist_date" ) )
                                {
                                    dataAcc.setUsageTime( result.getInt( "regist_time" ) + 240000 ); // 利用時刻
                                }
                            }
                            dataAcc.setHtSlipNo( 0 ); // 伝票No（ハピタッチ）
                            dataAcc.setHtRoomNo( Integer.toString( result.getInt( "seq" ) ) ); // 部屋番号（ハピタッチ）
                            dataAcc.setUsageCharge( 0 ); // 利用金額
                            dataAcc.setReceiveCharge( result.getInt( "charge_total" ) ); // 予約金額
                            if ( result.getInt( "used_mile" ) > 0 && result.getInt( "noshow_flag" ) == 1 && result.getInt( "payment" ) == 1 )
                            {
                                dataAcc.setAccrecvAmount( amount - result.getInt( "used_mile" ) ); // 売掛金額
                                dataAcc.setAccrecvBalance( amount - result.getInt( "used_mile" ) ); // 売掛残
                            }
                            else
                            {
                                dataAcc.setAccrecvAmount( amount ); // 売掛金額
                                dataAcc.setAccrecvBalance( amount ); // 売掛残
                            }

                            dataAcc.setHappyBalance( 0 ); // ハピー残高
                            dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// 締め処理区分
                            dataAcc.setLastUpdate( lastUpdate ); // 最終更新日
                            dataAcc.setLastUptime( lastUptime ); // 最終更新時刻
                            OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );

                            // 自動採番された売掛伝票No取得
                            accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, connection );

                            // 売掛明細データ登録
                            DataBkoAccountRecvDetail dataAccDetail = OwnerBkoCommon.GetDetailYoyaku( result.getInt( "charge_total" ) );
                            dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                            dataAccDetail.setSlipDetailNo( 1 );
                            dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_RESERVE );
                            if ( result.getInt( "ext_flag" ) == 0 )
                            {
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_140 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_140 );
                            }
                            else if ( result.getInt( "ext_flag" ) == 1 )
                            {
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_141 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_141 );
                            }
                            else if ( result.getInt( "ext_flag" ) == 2 )
                            {
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_142 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_142 );
                            }

                            dataAccDetail.setAmount( amount );
                            dataAccDetail.setPoint( 0 );
                            dataAccDetail.setId( result.getInt( "id" ) );
                            dataAccDetail.setReserveNo( result.getString( "reserve_no" ) );
                            dataAccDetail.setSeq( 0 );
                            dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// 締め処理区分
                            dataAccDetail.setUserId( result.getString( "reserve.user_id" ) );
                            OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );

                            if ( result.getInt( "used_mile" ) > 0 && result.getInt( "noshow_flag" ) == 1 && result.getInt( "payment" ) == 1 )
                            {
                                dataAccDetail.setSlipDetailNo( 2 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_120 );
                                dataAccDetail.setAmount( 0 - result.getInt( "used_mile" ) );
                                dataAccDetail.setPoint( 0 - result.getInt( "used_mile" ) );
                                OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                            }
                        }
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    // 請求データ作成前にコミット
                    if ( ret )
                    {
                        query = "COMMIT ";
                        prestate = connection.prepareStatement( query );
                        result = prestate.executeQuery();
                    }
                    else
                    {
                        query = "ROLLBACK";
                        prestate = connection.prepareStatement( query );
                        result = prestate.executeQuery();

                        return false;
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    query = "START TRANSACTION ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();

                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    // 請求データ作成
                    // 対象となる売掛データ取得
                    query = "SELECT DISTINCT header.accrecv_slip_no, ";
                    query = query + "header.hotel_id, ";
                    query = query + "header.id, ";
                    query = query + "header.bill_cd, ";
                    query = query + "header.accrecv_amount, "; // 売掛金額
                    query = query + "header.credit_note_flag, "; // 赤伝フラグ
                    query = query + "header.owner_hotel_id, "; // オーナーホテルID
                    query = query + "header.owner_user_id, "; // オーナーユーザID
                    query = query + "detail.slip_detail_no, "; // 伝票明細No
                    query = query + "detail.account_title_cd, "; // 売掛コード
                    query = query + "ifnull(basic.name, '') as name ";

                    query = query + "FROM hh_bko_account_recv header ";

                    query = query + "INNER JOIN hh_bko_account_recv_detail detail ";
                    query = query + "ON header.accrecv_slip_no = detail.accrecv_slip_no ";

                    query = query + "LEFT JOIN hh_hotel_basic basic ";
                    query = query + "ON basic.id = header.id ";

                    query = query + "INNER JOIN hh_rsv_reserve_basic reservebasic ";
                    query = query + "ON header.id = reservebasic.id ";

                    query = query + "WHERE header.closing_kind <> 3 ";

                    // ALMEX追加　チェックイン日付か24時間以上前のデータが更新対象
                    query = query + " AND header.usage_date * 1000000 + header.usage_time <   ? * 1000000 + reservebasic.deadline_time ";

                    // 金額が0の場合は締め対象としない
                    query = query + " AND header.accrecv_amount <> 0";

                    query = query + " ORDER BY header.id,header.hotel_id,header.accrecv_slip_no,detail.slip_detail_no ";

                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, monthEndDay );

                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        // 請求データを登録していく
                        String wkHotelId = "-1";
                        int wkId = -1;
                        int detailNo = 0;
                        int sumCharge = 0; // 税込分合計
                        int sumChargeNotInc = 0; // 税抜分合計
                        int billSlipNo = 0;
                        int accrecvAmount = 0;
                        int wkSlipNo = -1;

                        while( result.next() )
                        {
                            if ( result.getInt( "header.credit_note_flag" ) == 1 )
                            {
                                accrecvAmount = result.getInt( "accrecv_amount" ) * (-1);
                            }
                            else
                            {
                                accrecvAmount = result.getInt( "accrecv_amount" );
                            }

                            if ( wkId == result.getInt( "id" ) && wkHotelId.equals( result.getString( "hotel_id" ) ) )
                            {
                                if ( wkSlipNo != result.getInt( "accrecv_slip_no" ) )
                                {

                                    // ホテルが同じなら更新
                                    // 請求データ更新
                                    if ( result.getInt( "account_title_cd" ) >= 200 && result.getInt( "account_title_cd" ) <= 299 )
                                    {
                                        sumChargeNotInc = sumChargeNotInc + accrecvAmount;
                                    }
                                    else
                                    {
                                        sumCharge = sumCharge + accrecvAmount;
                                    }

                                    OwnerBkoCommon.UpdateBill( prestate, connection, billSlipNo, sumCharge, sumChargeNotInc );

                                    detailNo += 1;

                                }
                            }
                            else
                            {
                                // ホテルが変わったらブレイク
                                if ( result.getInt( "account_title_cd" ) >= 200 && result.getInt( "account_title_cd" ) <= 299 )
                                {
                                    sumCharge = 0;
                                    sumChargeNotInc = accrecvAmount;
                                }
                                else
                                {
                                    sumCharge = accrecvAmount;
                                    sumChargeNotInc = 0;
                                }
                                // 請求データ登録
                                registBill( prestate, connection, result.getInt( "id" ), result.getString( "name" ), result.getInt( "bill_cd" ), closingDate, sumCharge, sumChargeNotInc, result.getString( "owner_hotel_id" ),
                                        result.getInt( "owner_user_id" ), OwnerBkoCommon.CLOSING_KBN_MISYORI );

                                if ( hashBill.containsKey( String.valueOf( result.getInt( "bill_cd" ) ) + "-" + String.valueOf( result.getInt( "id" ) ) ) )
                                {
                                    billSlipNo = hashBill.get( String.valueOf( result.getInt( "bill_cd" ) ) + "-" + String.valueOf( result.getInt( "id" ) ) );
                                }
                                else
                                {
                                    // 自動採番された請求伝票No取得
                                    billSlipNo = getInsertedBillSlipNo( prestate, connection );
                                }

                                // 請求書番号更新
                                updateBillNo( prestate, connection, billSlipNo );

                                detailNo = 1;
                            }

                            // if ( result.getInt( "slip_detail_no" ) == 1 )
                            if ( wkSlipNo != result.getInt( "accrecv_slip_no" ) )
                            {

                                // 請求・売掛データ登録
                                OwnerBkoCommon.RegistRelBillAccountRecv( prestate, connection, billSlipNo, detailNo, result.getInt( "accrecv_slip_no" ), OwnerBkoCommon.CLOSING_KBN_MISYORI );

                            }
                            wkId = result.getInt( "id" );
                            wkHotelId = result.getString( "hotel_id" );
                            wkSlipNo = result.getInt( "accrecv_slip_no" );
                        }
                    }

                    // 請求明細データ作成前にコミット
                    if ( ret )
                    {
                        query = "COMMIT ";
                        prestate = connection.prepareStatement( query );
                        result = prestate.executeQuery();
                    }
                    else
                    {
                        query = "ROLLBACK";
                        prestate = connection.prepareStatement( query );
                        result = prestate.executeQuery();

                        return false;
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    query = "START TRANSACTION ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();

                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    // 請求明細データ作成
                    // ret = OwnerBkoCommon.RegistBillDetail( prestate, connection, 0 );
                    OwnerBkoCommon.RegistBillDetail( prestate, connection, 0 );

                    if ( ret )
                    {
                        // ■仮締め（締め処理区分の更新）
                        ret = updateClosingKind( prestate, connection, OwnerBkoCommon.CLOSING_KBN_MISYORI, OwnerBkoCommon.CLOSING_KBN_KARI, closingDate );
                    }
                }
                else
                {
                    // ■本締め（締め処理区分の更新）
                    ret = updateClosingKind( prestate, connection, OwnerBkoCommon.CLOSING_KBN_KARI, OwnerBkoCommon.CLOSING_KBN_HON, closingDate );
                    Logging.info( "本締め:" + ret, "[LogicOwnerBkoClogingBill]" );
                }

                if ( ret )
                {
                    System.out.println( "COMMIT4" );
                    query = "COMMIT ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    String msg = Message.getMessage( "info.00004", "締め処理" );
                    if ( OwnerBkoCommon.CLOSING_KBN_HON == closingKbn )
                    {
                        msg = msg + "（本締め）";
                    }
                    else
                    {
                        msg = msg + "（仮締め）";
                    }

                    // 成功メール送信
                    sendSuccessMail( msg );
                }
                else
                {
                    System.out.println( "ROLLBACK" );
                    query = "ROLLBACK";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }

                System.out.println( "[LogicOwnerBkoClosing.excute] closingYYYYMM = " + closingDate + ", closingKbn = " + closingKbn + ", startDay = " + startDay + ", endDay = " + endDay );
            }
            catch ( Exception e )
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
                Logging.error( "[LogicOwnerBkoClosing.excute] Exception=" + e.toString() );

                return false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoClosing.excute] Exception=" + e.toString() );

        }
        finally
        {
            try
            {
                // 処理が途中でエラーになった場合、締め制御データは戻せたら戻す
                if ( controlChangeFlg && !ret )
                {
                    System.out.println( "closing_control recovery" );
                    System.out.println( controlChangeFlg );
                    System.out.println( ret );

                    if ( connection.isClosed() )
                    {
                        connection = DBConnection.getConnection( false );
                    }

                    query = "START TRANSACTION ";
                    prestate = connection.prepareStatement( query );

                    result = prestate.executeQuery();
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    ret = false;

                    if ( OwnerBkoCommon.CLOSING_KBN_HON == closingKbn )
                    {
                        // 本締め
                        // 締め制御データを「停止」に戻す
                        query = "UPDATE hh_bko_closing_control ";
                        query = query + "SET exec_flag = 0, "; // 0:停止
                        query = query + "last_update = ?, ";
                        query = query + "last_uptime = ? ";
                        query = query + "WHERE closing_date = ? ";

                        prestate = connection.prepareStatement( query );
                        prestate.setInt( 1, lastUpdate );
                        prestate.setInt( 2, lastUptime );
                        prestate.setInt( 3, closingDate );

                        if ( prestate.executeUpdate() > 0 )
                        {
                            ret = true;
                        }
                        else
                        {
                            ret = false;
                        }
                    }
                    else
                    {
                        // 仮締め
                        // 締め制御データに「未処理：実行中」データを削除
                        query = "DELETE FROM hh_bko_closing_control ";
                        query = query + "WHERE closing_date = ? ";
                        query = query + "AND closing_kind = 1 "; // 1:未処理
                        query = query + "AND exec_flag = 1 "; // 1:実行中

                        prestate = connection.prepareStatement( query );
                        prestate.setInt( 1, closingDate );
                        if ( prestate.executeUpdate() > 0 )
                        {
                            ret = true;
                        }
                        else
                        {
                            ret = false;
                        }
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    // 締め制御データ更新をコミット
                    if ( ret )
                    {
                        query = "COMMIT ";
                        prestate = connection.prepareStatement( query );
                        result = prestate.executeQuery();
                    }
                    else
                    {
                        query = "ROLLBACK";
                        prestate = connection.prepareStatement( query );
                        result = prestate.executeQuery();
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }
            }
            catch ( Exception e )
            {
                // System.out.println( e.toString() );
                // 既にエラーなのでここでは何もしない
            }

            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * 締め年月開始日・終了日セット
     * 
     * @param closingDate 締め年月
     * @return true:正常、false:失敗
     */
    private boolean setMonthStartDate(int closingDate)
    {
        // 処理日の範囲を計算 （前月の締め日の翌日～当月の締め日の翌日）
        Calendar cal = Calendar.getInstance();

        cal.set( Calendar.YEAR, (closingDate / 100) );
        cal.set( Calendar.MONTH, (closingDate % 100) - 1 );
        cal.set( Calendar.DATE, OwnerBkoCommon.SIME_DATE ); // 例:20110410

        // 処理対象日は締め日の翌日
        cal.add( Calendar.DATE, 1 ); // 例:20110411

        // 対象終了日
        monthEndDay = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

        // 対象開始日
        cal.add( Calendar.MONTH, -1 ); // 例:20110311
        monthStartDay = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

        return true;
    }

    /**
     * 締め処理区分更新
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param whereClosingKind 修正対象締め処理区分
     * @param setClosingKind 更新後締め処理区分
     * @param closingDate 締め処理年月
     * @return true:正常、false:失敗
     */
    private boolean updateClosingKind(PreparedStatement prestate, Connection connection, int whereClosingKind, int setClosingKind, int closingDate)
            throws Exception
    {

        String query = "";
        boolean ret = true;

        // 売掛データ
        query = "UPDATE hh_bko_account_recv ah ";

        query = query + "INNER JOIN hh_bko_rel_bill_account_recv bd ";
        query = query + "ON bd.accrecv_slip_no = ah.accrecv_slip_no ";

        query = query + "INNER JOIN hh_bko_bill bh ";
        query = query + "ON bh.bill_slip_no = bd.bill_slip_no ";
        query = query + "AND bh.bill_date = ? ";

        query = query + "SET ah.closing_kind = ?, ";
        query = query + "ah.last_update = ?, ";
        query = query + "ah.last_uptime = ? ";
        query = query + "WHERE ah.closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, closingDate );
        prestate.setInt( 2, setClosingKind );
        prestate.setInt( 3, lastUpdate );
        prestate.setInt( 4, lastUptime );
        prestate.setInt( 5, whereClosingKind );
        // 20110913 請求データの請求年月のものだけ更新する END

        prestate.executeUpdate();
        DBConnection.releaseResources( prestate );

        query = "UPDATE hh_bko_account_recv_detail ad ";
        query = query + "INNER JOIN hh_bko_rel_bill_account_recv bd ";
        query = query + "ON bd.accrecv_slip_no = ad.accrecv_slip_no ";

        query = query + "INNER JOIN hh_bko_bill bh ";
        query = query + "ON bh.bill_slip_no = bd.bill_slip_no ";
        query = query + "AND bh.bill_date = ? ";

        query = query + "SET ad.closing_kind = ? ";
        query = query + "WHERE ad.closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, closingDate );
        prestate.setInt( 2, setClosingKind );
        prestate.setInt( 3, whereClosingKind );
        // 20110913 請求データの請求年月のものだけ更新する END

        prestate.executeUpdate();
        DBConnection.releaseResources( prestate );

        // 請求データ
        query = "UPDATE hh_bko_bill ";
        query = query + "SET closing_kind = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ? ";
        query = query + "WHERE closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        prestate.setInt( 2, lastUpdate );
        prestate.setInt( 3, lastUptime );
        prestate.setInt( 4, whereClosingKind );

        prestate.executeUpdate();
        DBConnection.releaseResources( prestate );

        // 請求・売掛データ
        query = "UPDATE hh_bko_rel_bill_account_recv ";
        query = query + "SET closing_kind = ? ";
        query = query + "WHERE closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        prestate.setInt( 2, whereClosingKind );

        prestate.executeUpdate();
        DBConnection.releaseResources( prestate );

        // 請求明細データ
        query = "UPDATE hh_bko_bill_detail ";
        query = query + "SET closing_kind = ? ";
        query = query + "WHERE closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        prestate.setInt( 2, whereClosingKind );

        prestate.executeUpdate();
        DBConnection.releaseResources( prestate );

        // 締め制御データ
        query = "UPDATE hh_bko_closing_control ";
        query = query + "SET closing_kind = ?, ";
        query = query + "exec_flag = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ? ";
        query = query + "WHERE closing_date = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        prestate.setInt( 2, 0 );
        prestate.setInt( 3, lastUpdate );
        prestate.setInt( 4, lastUptime );
        prestate.setInt( 5, closingDate );

        if ( prestate.executeUpdate() > 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        DBConnection.releaseResources( prestate );

        return ret;
    }

    /**
     * 請求データ登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param id ホテルID
     * @param hotelName ホテル名
     * @param billCd 請求先コード
     * @param billDate 請求年月
     * @param billKin 請求金額（税込み）
     * @param billKinNotInc 請求金額（税抜き）
     * @param ownerHotelId ホテルID
     * @param ownerUserId ユーザID
     * @param closingKind 締め処理区分
     * @return true:正常、false:失敗
     */
    private boolean registBill(PreparedStatement prestate, Connection conn, int id, String hotelName, int billCd, int billDate, int billKin, int billKinNotInc, String ownerHotelId, int ownerUserId, int closingKind) throws Exception
    {
        // 20110808 請求先コード、ホテルIDで、請求伝票Noが採番済みかチェック
        int billSlipNo = 0;
        if ( hashBill.containsKey( String.valueOf( billCd ) + "-" + String.valueOf( id ) ) )
        {
            billSlipNo = hashBill.get( String.valueOf( billCd ) + "-" + String.valueOf( id ) );
        }

        String query = "";
        query = query + "INSERT hh_bko_bill ( ";
        // 20110808 既に採番済みの請求伝票Noが存在している場合は、その請求伝票Noで登録
        if ( billSlipNo > 0 )
        {
            query = query + "bill_slip_no, ";
        }
        query = query + "bill_cd, ";
        query = query + "bill_name, ";
        query = query + "bill_name_kana, ";
        query = query + "bill_date, ";
        query = query + "charge_inc_tax, ";
        query = query + "charge_not_inc_tax, ";
        query = query + "tax, ";
        query = query + "bill_issue_date, ";
        query = query + "payment_date, ";
        query = query + "deposit_date, ";
        query = query + "id, ";
        query = query + "hotel_name, ";
        query = query + "issue_flag, ";
        query = query + "reissue_flag, ";
        query = query + "bill_zip_code, ";
        query = query + "bill_pref_code, ";
        query = query + "bill_jis_code, ";
        query = query + "bill_address1, ";
        query = query + "bill_address2, ";
        query = query + "bill_address3, ";
        query = query + "bill_tel, ";
        query = query + "bill_div_name, ";
        query = query + "bill_position_title, ";
        query = query + "bill_person_name, ";
        query = query + "closing_kind, ";
        query = query + "owner_hotel_id, ";
        query = query + "owner_user_id, ";
        query = query + "last_update, ";
        query = query + "last_uptime ";
        query = query + ") ";
        query = query + "SELECT ";
        if ( billSlipNo > 0 )
        {
            query = query + String.valueOf( billSlipNo ) + ","; // 請求伝票No
        }
        query = query + "?, ";// 請求先コード
        query = query + "bill_name, ";// 請求先名
        query = query + "bill_name_kana, ";// 請求先名（カナ）
        query = query + "?, ";// 請求年月
        query = query + "?, ";// 請求金額（税込み）
        query = query + "?, ";// 請求金額（税抜き）
        query = query + "?, ";// 消費税
        query = query + "?, ";// 請求書発行日
        query = query + "?, ";// 支払期限日
        query = query + "?, ";// 入金予定日
        query = query + "?, ";// ホテルID
        query = query + "?, ";// ホテル名
        query = query + "0, ";// 発行済みフラグ
        query = query + "0, ";// 再発行済みフラグ
        query = query + "bill_zip_code, ";// 請求書郵便番号
        query = query + "bill_pref_code, ";// 請求先都道府県コード
        query = query + "bill_jis_code, ";// 請求先市区町村コード
        query = query + "bill_address1, ";// 請求先住所１
        query = query + "bill_address2, ";// 請求先住所２
        query = query + "bill_address3, ";// 請求先住所３
        query = query + "bill_tel, ";// 請求先電話番号
        query = query + "bill_div_name, ";// 請求先部署名
        query = query + "bill_position_title, ";// 請求先役職名
        query = query + "bill_person_name, ";// 請求先担当者名
        query = query + "?, ";// 締め処理区分
        query = query + "'', ";// オーナーホテルID
        query = query + "0, ";// オーナーユーザID
        query = query + "?, ";// 最終更新日
        query = query + "? ";// 最終更新日時

        query = query + "FROM hh_bko_billing "; // 請求先マスタ
        query = query + "WHERE bill_cd = ? ";

        try
        {
            int notIncTax = (int)Math.floor( billKinNotInc * OwnerBkoCommon.TAX ); // 税抜き分の消費税

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, billCd ); // 請求先コード
            prestate.setInt( 2, billDate ); // 請求年月
            prestate.setInt( 3, billKin + billKinNotInc + notIncTax ); // 請求金額（税込み）
            prestate.setInt( 4, billKin + billKinNotInc ); // 請求金額（税抜き）
            prestate.setInt( 5, notIncTax ); // 消費税
            prestate.setInt( 6, 0 ); // 請求書発行日
            prestate.setInt( 7, paymentDate ); // 支払期限日
            prestate.setInt( 8, paymentDate );// 入金予定日
            prestate.setInt( 9, id );// ホテルID
            prestate.setString( 10, hotelName );// ホテル名
            prestate.setInt( 11, closingKind );// 締め処理区分
            prestate.setInt( 12, lastUpdate );
            prestate.setInt( 13, lastUptime );
            prestate.setInt( 14, billCd );// 請求先コード

            if ( prestate.executeUpdate() <= 0 )
            {
                throw new Exception( "[LogicOwnerBkoClosing.registBill] registBill error" );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.registBill] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * 支払期限日計算
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return 支払期限日
     */
    private int getPaymentDate(PreparedStatement prestate, Connection conn)
    {
        String query;
        ResultSet result = null;

        query = "SELECT cal_date FROM hh_rsv_calendar ";
        query = query + "WHERE cal_date <= ? ";
        query = query + "AND (1 <= week AND week <= 5) ";
        query = query + "AND holiday_kind = 0 ";
        query = query + "ORDER BY cal_date DESC ";

        try
        {
            prestate = conn.prepareStatement( query );

            int date2month = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), 2 ); // ２ヵ月後の日付
            int date = (date2month / 10000) * 10000 + ((date2month / 100 % 100)) * 100 + 1; // ２ヵ月後の月の一日
            date = DateEdit.addDay( date, -1 ); // その前日（＝翌月末日）
            prestate.setInt( 1, date );

            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    return result.getInt( "cal_date" );
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.getPaymentDate] Exception=" + e.toString() );
            return -1;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return -1;
    }

    /**
     * 追加した請求伝票No
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return 請求伝票No
     */
    private int getInsertedBillSlipNo(PreparedStatement prestate, Connection conn)
    {
        String query;
        ResultSet result = null;

        query = "SELECT LAST_INSERT_ID() AS bill_slip_no";

        try
        {
            prestate = conn.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    return result.getInt( "bill_slip_no" );
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.getInsertedBillSlipNo] Exception=" + e.toString() );
            return -1;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return -1;
    }

    /**
     * 請求書番号更新
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param billSlipNo 請求伝票No
     * @return true:正常、false:失敗
     */
    private boolean updateBillNo(PreparedStatement prestate, Connection conn, int billSlipNo) throws Exception
    {
        String query = "";
        query = query + "UPDATE hh_bko_bill SET ";
        query = query + "bill_no = ? ";
        query = query + "WHERE bill_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, BILL_NO_HEADER + String.format( "%07d", billSlipNo ) );
            prestate.setInt( 2, billSlipNo );

            if ( prestate.executeUpdate() <= 0 )
            {
                throw new Exception( "[LogicOwnerBkoClosing.updateBillNo] updateBillNo error" );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.updateBillNo] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * 成功メール送信
     * 
     * @param msg メール本文
     * @return なし
     */
    private void sendSuccessMail(String msg)
    {

        // 文字化けするので最後に空白追加
        msg = msg + " ";
        DataHhRsvSystemConfList sysConfList = new DataHhRsvSystemConfList();
        HashMap<String, String> map = sysConfList.getSystemConfMap( 9 ); // メール情報をhh_rsv_system_confから取得する。

        SendMail.send( map.get( MAIL_FROM ), map.get( MAIL_TO ), map.get( MAIL_SUCCESSTITLE ), msg );
    }

    /**
     * エラーメール送信
     * 
     * @param msg メール本文
     * @return なし
     */
    private void sendErrorMail(String msg)
    {

        // 文字化けするので最後に空白追加
        msg = msg + " ";
        DataHhRsvSystemConfList sysConfList = new DataHhRsvSystemConfList();
        HashMap<String, String> map = sysConfList.getSystemConfMap( 9 ); // メール情報をhh_rsv_system_confから取得する。

        SendMail.send( map.get( MAIL_FROM ), map.get( MAIL_TO ), map.get( MAIL_ERRORTITLE ), msg );
    }

    /**
     * confファイルから、指定したメッセージIDのメッセージを取得する
     * 
     * @param messageID メッセージID
     * @return 取得したメッセージ（メッセージIDが未登録の場合は、Nullを返す）
     * 
     */
    public static String getMessage(String messageID)
    {
        String messageStr = null;
        FileInputStream propfile = null;
        Properties config = new Properties();

        try
        {
            propfile = new FileInputStream( CONF_FILE );
            config = new Properties();
            config.load( propfile );

            messageStr = config.getProperty( messageID );
            messageStr = new String( messageStr.getBytes( "ISO-8859-1" ), "Windows-31J" );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoClosing.getMessage()] Exception=" + e.toString() + ", messageID:" + messageID );
        }

        return messageStr;
    }
}
