package jp.happyhotel.owner;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.data.DataBkoAccountRecv;
import jp.happyhotel.data.DataBkoAccountRecvDetail;

public class LogicOwnerBkoClosing implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID  = -1694452370598598055L;

    // ポイント区分
    private static final int    POINT_KIND_RAITEN = 21;                               // 来店
    private static final int    POINT_KIND_FUYO   = 22;                               // 付与
    private static final int    POINT_KIND_RIYOU  = 23;                               // 使用
    private static final int    POINT_KIND_YOYAKU = 24;                               // 予約

    // 請求・支払（予約）　パーセンテージ(%)
    private static final int    PERCENT_RESERVE   = 5;

    // 最終更新日
    private int                 lastUpdate        = 0;

    // 最終更新日時
    private int                 lastUptime        = 0;

    // 支払期限日
    private int                 paymentDate       = 0;

    private static final String CONF_FILE         = "/etc/happyhotel/backoffice.conf";
    private static final String MAIL_TO           = "mail.to";
    private static final String MAIL_FROM         = "mail.from";
    private static final String MAIL_ERRORTITLE   = "mail.errortitle";
    private static final String MAIL_SUCCESSTITLE = "mail.successtitle";

    // 請求書番号先頭文字
    private static final String BILL_NO_HEADER    = "641";

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
            if ( args.length != 2 )
            {
                System.out.println( "Parameter Count Error" + args.length );

                // エラーメール送信
                sendErrorMail( Message.getMessage( "warn.30025" ) );
                return false;
            }

            try
            {
                closingDate = Integer.parseInt( args[0] );
                closingKbn = Integer.parseInt( args[1] );
            }
            catch ( Exception e )
            {
                System.out.println( "Parameter Int Error" );

                // エラーメール送信
                sendErrorMail( Message.getMessage( "warn.30026" ) );
                return false;
            }

            lastUpdate = Integer.parseInt( DateEdit.getDate( 2 ) );
            lastUptime = Integer.parseInt( DateEdit.getTime( 1 ) );

            // 締め年月が存在する年月かチェック
            if ( !DateEdit.checkDate( String.valueOf( closingDate ).substring( 0, 4 ) + "/" + String.valueOf( closingDate ).substring( 4, 6 ) + "/01" ) )
            {
                System.out.println( "Parameter closing_date Error" + closingDate );

                // エラーメール送信
                sendErrorMail( Message.getMessage( "warn.30027" ) );

                return false;
            }

            // 2:仮締め or 3:本締め 以外の場合はエラー
            if ( !(OwnerBkoCommon.CLOSING_KBN_KARI == closingKbn || OwnerBkoCommon.CLOSING_KBN_HON == closingKbn) )
            {
                System.out.println( "Parameter closing_kind Error" );
                // エラーメール送信
                sendErrorMail( Message.getMessage( "warn.30028" ) );
                return false;
            }

            // 実行した日が締め日でなければエラー
            if ( ((lastUpdate / 10000 % 100) < OwnerBkoCommon.SIME_DATE + 1) )
            {
                // エラーメール送信
                sendErrorMail( Message.getMessage( "warn.30035" ) );
                return false;
            }

            try
            {
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // システム日付＝締め日の場合、締め時刻が過ぎているかチェック
                if ( (lastUpdate % 100) == OwnerBkoCommon.SIME_DATE + 1 )
                {

                    int deadlineTime = getDeadlineTime( prestate, connection );
                    if ( deadlineTime != 0 && deadlineTime > lastUptime )
                    {
                        // エラーメール送信
                        sendErrorMail( Message.getMessage( "warn.30036" ) );
                        return false;
                    }

                }

                paymentDate = getPaymentDate( prestate, connection );
                // System.out.println("paymentDate:" + paymentDate);
                if ( paymentDate == -1 )
                {

                    // エラーメール送信
                    sendErrorMail( Message.getMessage( "erro.30002", "支払期限日の取得" ) );
                    return false;
                }

                // 締め制御データチェック
                if ( !checkClosingControl( prestate, connection, closingDate, closingKbn ) )
                {
                    System.out.println( "checkClosingControl Error:" + closingDate + ":" + closingKbn );
                    return false;
                }

                // ■処理開始

                if ( OwnerBkoCommon.CLOSING_KBN_HON == closingKbn )
                {
                    // 本締め
                    // 締め制御データを「実行中」に更新
                    query = "UPDATE hh_bko_closing_control SET ";
                    query = query + "exec_flag = 1, "; // 1:実行中
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
                    // 締め制御データに「未処理：実行中」データを登録
                    query = "INSERT hh_bko_closing_control SET ";
                    query = query + "closing_date = ?,";
                    query = query + "closing_kind = 1,"; // 1:未処理
                    query = query + "exec_flag = 1, "; // 1:実行中
                    query = query + "last_update = ?, ";
                    query = query + "last_uptime = ? ";

                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    prestate.setInt( 2, lastUpdate );
                    prestate.setInt( 3, lastUptime );

                    if ( prestate.executeUpdate() > 0 )
                    {
                        ret = true;
                    }
                    else
                    {
                        ret = false;
                    }
                }

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

                    return false;
                }

                controlChangeFlg = true;

                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // 処理日の範囲を計算 （前月の締め日の翌日～当月の締め日の翌日）
                Calendar cal = Calendar.getInstance();

                cal.set( Calendar.YEAR, (closingDate / 100) );
                cal.set( Calendar.MONTH, (closingDate % 100) - 1 );
                cal.set( Calendar.DATE, OwnerBkoCommon.SIME_DATE ); // 例:20110410

                // 処理対象日は締め日の翌日
                cal.add( Calendar.DATE, 1 ); // 例:20110411

                // 対象終了日
                endDay = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

                // 対象開始日
                cal.add( Calendar.MONTH, -1 ); // 例:20110310
                startDay = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

                // 仮締め
                if ( OwnerBkoCommon.CLOSING_KBN_KARI == closingKbn )
                {
                    // 売掛データ削除
                    query = "DELETE FROM hh_bko_account_recv ";
                    query = query + "WHERE closing_kind = 1 "; // 未処理
                    prestate = connection.prepareStatement( query );
                    prestate.executeUpdate();

                    // 売掛明細データ削除
                    query = "DELETE FROM hh_bko_account_recv_detail ";
                    query = query + "WHERE closing_kind = 1 "; // 未処理
                    prestate = connection.prepareStatement( query );
                    prestate.executeUpdate();

                    // 請求データ削除
                    query = "DELETE FROM hh_bko_bill ";
                    query = query + "WHERE closing_kind = 1 "; // 未処理
                    prestate = connection.prepareStatement( query );
                    prestate.executeUpdate();

                    // 請求明細データ削除
                    query = "DELETE FROM hh_bko_bill_detail ";
                    query = query + "WHERE closing_kind = 1 "; // 未処理
                    prestate = connection.prepareStatement( query );
                    prestate.executeUpdate();

                    // 請求・売掛データ削除
                    query = "DELETE FROM hh_bko_rel_bill_account_recv ";
                    query = query + "WHERE closing_kind = 1 "; // 未処理
                    prestate = connection.prepareStatement( query );
                    prestate.executeUpdate();

                    query = "COMMIT ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();

                    query = "START TRANSACTION ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();

                    // 売掛データ作成
                    // 対象となるユーザポイント一時データ取得
                    query = "SELECT ";
                    query = query + "point.user_id, ";
                    query = query + "point.seq, ";
                    query = query + "point.point, ";
                    query = query + "point.then_point, ";
                    query = query + "point.amount, "; // 使用金額(point_kind=23)or付与金額(point_kind=22)
                    query = query + "point.point_kind, ";
                    query = query + "point.get_date, ";
                    query = query + "point.get_time, ";
                    query = query + "point.employee_code, ";
                    query = query + "hotelbasic.id, ";
                    query = query + "hotelbasic.hotenavi_id, ";
                    query = query + "reservebasic.hotel_id, ";
                    query = query + "reservebasic.user_id, ";
                    query = query + "0 as regist_date, ";
                    query = query + "0 as regist_time, ";
                    query = query + "visit_seq, ";
                    query = query + "user_seq, ";
                    query = query + "slip_no, ";
                    query = query + "room_no, ";
                    query = query + "billingHotel.bill_cd ";

                    query = query + "FROM hh_hotel_basic hotelbasic "; // ホテル基本データ

                    query = query + "INNER JOIN hh_rsv_reserve_basic reservebasic ";
                    query = query + "ON reservebasic.id = hotelbasic.id ";

                    query = query + "INNER JOIN hh_user_point_pay_temp point ";
                    query = query + "ON point.hotenavi_id = hotelbasic.hotenavi_id ";

                    query = query + "INNER JOIN hh_bko_rel_billing_hotel billingHotel ";// 請求先・ホテル
                    query = query + "ON billingHotel.id = hotelbasic.id ";

                    query = query + "WHERE point.get_date * 1000000 + point.get_time >=  ? * 1000000 + reservebasic.deadline_time ";
                    query = query + "AND point.get_date * 1000000 + point.get_time < ? * 1000000 + reservebasic.deadline_time ";
                    query = query + "AND (point.point_kind = 21 OR point.point_kind = 22 OR point.point_kind = 23 OR point.point_kind = 24) ";
                    query = query + "AND hotelbasic.rank >= 2 ";
                    query = query + "AND hotelbasic.kind <= 7 ";
                    query = query + "ORDER BY hotelbasic.id ,user_seq, visit_seq ,point.point_kind";

                    // 売掛データ作成
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, startDay );
                    prestate.setInt( 2, endDay );
                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        int wkHotelID = -1; // ホテルID
                        int wkUserSeq = -1; // ユーザーID
                        int wkVisitSeq = -1; // 来店管理番号
                        int accrecvSlipNo = -1;
                        int slipDetailNo = -1;

                        // 売掛データを更新していく
                        // ①売掛データ追加→②売掛明細追加→ブレイクしてなければ③売掛データ更新→④売掛明細更新
                        // 　　　　　　　　　　　　　　　　→ブレイクしていたら①売掛データ追加→・・・
                        while( result.next() )
                        {
                            int accrecvAmount = 0; // 売掛金額

                            // 売掛がブレイクしたら売掛データ登録
                            if ( ( wkHotelID != result.getInt( "hotelbasic.id" ) ) || ( wkUserSeq != result.getInt( "user_seq" ) ) || ( wkVisitSeq != result.getInt( "visit_seq" ) ) )
                            {
                                // ■売掛データ追加
                                dataAcc = new DataBkoAccountRecv();
                                dataAcc.setHotelId( result.getString( "hotelbasic.hotenavi_id" ) ); // ホテルID（ホテナビ）
                                dataAcc.setId( result.getInt( "id" ) ); // ホテルID（ハピホテ）
                                dataAcc.setAddUpDate( lastUpdate ); // 計上日
                                dataAcc.setBillCd( result.getInt( "billingHotel.bill_cd" ) );// 請求先コード
                                dataAcc.setBillName( "" ); // 請求先名

                                dataAcc.setPersonName( OwnerBkoCommon.GetPersonName( prestate, connection, result.getString( "hotelbasic.hotenavi_id" ), result.getInt( "point.employee_code" ) ) );// 担当者名
                                dataAcc.setUserManagementNo( result.getInt( "user_seq" ) );// ユーザ管理番号
                                dataAcc.setUsageDate( result.getInt( "point.get_date" ) );// 利用日
                                dataAcc.setUsageTime( result.getInt( "point.get_time" ) );// 利用時刻
                                dataAcc.setHtSlipNo( result.getInt( "slip_no" ) );// 伝票No（ハピタッチ）
                                dataAcc.setHtRoomNo( result.getString( "room_no" ) );// 部屋番号（ハピタッチ）
                                dataAcc.setUsageCharge( result.getInt( "amount" ) );// 利用金額

                                if ( POINT_KIND_FUYO == result.getInt( "point_kind" ) )
                                {
                                    // 付与
                                    accrecvAmount = result.getInt( "point" ) * 2;
                                }
                                else if ( POINT_KIND_YOYAKU == result.getInt( "point_kind" ) )
                                {
                                    // 予約
                                    accrecvAmount = result.getInt( "amount" );
                                }
                                else if ( POINT_KIND_RIYOU == result.getInt( "point_kind" ) )
                                {
                                    // 使用
                                    accrecvAmount = result.getInt( "point" );
                                }
                                dataAcc.setAccrecvAmount( accrecvAmount ); // 売掛金額
                                dataAcc.setAccrecvBalance( accrecvAmount );// 売掛残
                                dataAcc.setHappyBalance( result.getInt( "then_point" ) );// ハピー残高
                                dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// 締め処理区分
                                dataAcc.setOwnerHotelId( result.getString( "reservebasic.hotel_id" ) );// オーナーホテルID
                                dataAcc.setOwnerUserId( result.getInt( "reservebasic.user_id" ) );// オーナーユーザID

                                // ret = OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );
                                OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );

                                // 自動採番された売掛伝票No取得
                                accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, connection );
                                dataAcc.setAccrecvSlipNo( accrecvSlipNo );

                                // 伝票明細No初期化
                                slipDetailNo = 1;
                            }
                            else
                            {
                                // ■売掛データ更新

                                // 同じ売掛の場合、伝票明細No + 1
                                slipDetailNo += 1;

                                dataAcc.setUsageCharge( dataAcc.getUsageCharge() + result.getInt( "amount" ) );// 利用金額

                                if ( POINT_KIND_FUYO == result.getInt( "point_kind" ) )
                                {
                                    // 付与
                                    accrecvAmount = result.getInt( "point" ) * 2;
                                }
                                else if ( POINT_KIND_YOYAKU == result.getInt( "point_kind" ) )
                                {
                                    // 予約
                                    accrecvAmount = result.getInt( "amount" );
                                }
                                else if ( POINT_KIND_RIYOU == result.getInt( "point_kind" ) )
                                {
                                    // 使用
                                    accrecvAmount = result.getInt( "point" );
                                }
                                dataAcc.setAccrecvAmount( dataAcc.getAccrecvAmount() + accrecvAmount ); // 売掛金額
                                dataAcc.setAccrecvBalance( dataAcc.getAccrecvBalance() + accrecvAmount ); // 売掛残

                                updateRecv( prestate, connection, dataAcc );
                            }

                            // 売掛明細データ　共通項目設定
                            DataBkoAccountRecvDetail dataAccDetail = new DataBkoAccountRecvDetail();
                            dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                            dataAccDetail.setSlipDetailNo( slipDetailNo );
                            dataAccDetail.setPoint( result.getInt( "point" ) );
                            // dataAccDetail.setId( result.getInt( "id" ) );
                            dataAccDetail.setReserveNo( "" );
                            dataAccDetail.setUserId( result.getString( "user_id" ) );
                            dataAccDetail.setSeq( result.getInt( "seq" ) );
                            dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );

                            // 売掛明細データ登録
                            switch( result.getInt( "point_kind" ) )
                            {
                                case POINT_KIND_RAITEN:

                                    // 来店ハピー
                                    dataAccDetail.setSlipKind( 0 );
                                    dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 );
                                    dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_100 );
                                    dataAccDetail.setAmount( 0 );
                                    break;

                                case POINT_KIND_FUYO:

                                    // 付与
                                    dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                                    dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
                                    dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_110 );
                                    dataAccDetail.setAmount( result.getInt( "point" ) * 2 );
                                    break;

                                case POINT_KIND_RIYOU:

                                    // 使用
                                    dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                                    dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 );
                                    dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_120 );
                                    dataAccDetail.setAmount( result.getInt( "point" ) );
                                    break;

                                case POINT_KIND_YOYAKU:

                                    // 予約ハピー
                                    dataAccDetail.setSlipKind( 0 );
                                    dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 );
                                    dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_130 );
                                    dataAccDetail.setAmount( 0 );
                                    break;

                                default:
                                    break;
                            }

                            OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );

                            wkHotelID = result.getInt( "hotelbasic.id" ); // ホテルID
                            wkUserSeq = result.getInt( "user_seq" ); // ユーザーID
                            wkVisitSeq = result.getInt( "visit_seq" ); // 来店管理番号
                        }
                    }

                    // 予約（＝送客手数料 ポイントは上で作成済み）
                    query = "SELECT ";
                    query = query + "hotelbasic.hotenavi_id, "; // ホテナビID
                    query = query + "reserve.user_id, "; // ユーザID
                    query = query + "reserve.charge_total, "; // 総合計
                    query = query + "result.seq, "; // 管理番号
                    query = query + "hotelbasic.id, "; // ホテルID
                    query = query + "result.reserve_no, "; // 予約番号
                    query = query + "result.regist_date, "; // 登録日付（利用日）
                    query = query + "result.regist_time, "; // 登録時間（利用時刻）
                    query = query + "basic.hotel_id, "; // オーナーホテルID
                    query = query + "basic.user_id, "; // ユーザID
                    query = query + "billingHotel.bill_cd "; // 請求先コード

                    query = query + "FROM hh_rsv_result result ";
                    query = query + "INNER JOIN hh_rsv_reserve reserve ";
                    query = query + "ON result.id = reserve.id ";
                    query = query + "AND result.reserve_no = reserve.reserve_no ";
                    query = query + "AND reserve.status = 2 "; // チェックイン
                    query = query + "INNER JOIN hh_rsv_reserve_basic basic ";
                    query = query + "ON result.id = basic.id ";
                    query = query + "INNER JOIN hh_hotel_basic hotelbasic "; // ホテル基本データ
                    query = query + "ON hotelbasic.id = basic.id ";
                    query = query + "AND hotelbasic.rank >= 2 ";

                    query = query + "INNER JOIN hh_bko_rel_billing_hotel billingHotel ";// 請求先・ホテル
                    query = query + "ON billingHotel.id = hotelbasic.id ";

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

                        // 予約（送客手数料）
                        while( result.next() )
                        {
                            int amount = (int)Math.floor( result.getInt( "reserve.charge_total" ) * PERCENT_RESERVE / 100 );

                            // 売掛データ登録
                            dataAcc = new DataBkoAccountRecv();
                            dataAcc.setHotelId( result.getString( "hotelbasic.hotenavi_id" ) ); // ホテルID（ホテナビ）
                            dataAcc.setId( result.getInt( "id" ) ); // ホテルID（ハピホテ）
                            dataAcc.setAddUpDate( lastUpdate ); // 計上日
                            dataAcc.setBillCd( result.getInt( "billingHotel.bill_cd" ) );
                            dataAcc.setPersonName( "" ); // 請求先コード
                            dataAcc.setUserManagementNo( 0 ); // ユーザ管理番号
                            dataAcc.setUsageDate( result.getInt( "result.regist_date" ) ); // 利用日
                            dataAcc.setUsageTime( result.getInt( "result.regist_time" ) ); // 利用時刻
                            dataAcc.setHtSlipNo( 0 ); // 伝票No（ハピタッチ）
                            dataAcc.setHtRoomNo( "" ); // 部屋番号（ハピタッチ）
                            dataAcc.setUsageCharge( 0 ); // 利用金額
                            dataAcc.setReceiveCharge( result.getInt( "reserve.charge_total" ) ); // 予約金額
                            dataAcc.setAccrecvAmount( amount ); // 売掛金額
                            dataAcc.setAccrecvBalance( amount ); // 売掛残
                            dataAcc.setHappyBalance( 0 ); // ハピー残高
                            dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// 締め処理区分
                            // dataAcc.setOwnerHotelId( result.getString( "basic.hotel_id" ) );// オーナーホテルID
                            // dataAcc.setOwnerUserId( result.getInt( "basic.user_id" ) ); // オーナーユーザID
                            dataAcc.setLastUpdate( lastUpdate ); // 最終更新日
                            dataAcc.setLastUptime( lastUptime ); // 最終更新時刻

                            OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );

                            // 自動採番された売掛伝票No取得
                            accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, connection );

                            // 売掛明細データ登録
                            DataBkoAccountRecvDetail dataAccDetail = OwnerBkoCommon.GetDetailYoyaku( result.getInt( "reserve.charge_total" ) );
                            dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                            dataAccDetail.setSlipDetailNo( 1 );
                            dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_RESERVE );
                            dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_140 );
                            dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_140 );
                            dataAccDetail.setAmount( amount );
                            dataAccDetail.setPoint( 0 );
                            dataAccDetail.setId( result.getInt( "id" ) );
                            dataAccDetail.setReserveNo( result.getString( "reserve_no" ) );
                            // dataAccDetail.setUserId( result.getString( "user_id" ) );
                            dataAccDetail.setSeq( 0 );
                            dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// 締め処理区分
                            OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        }
                    }

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

                    query = "START TRANSACTION ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();

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

                    query = query + "WHERE header.closing_kind <> 3 ";
                    query = query + "ORDER BY header.hotel_id,header.accrecv_slip_no,detail.slip_detail_no ";

                    prestate = connection.prepareStatement( query );

                    result = prestate.executeQuery();

                    if ( result != null )
                    {

                        // 請求データを登録していく

                        String wkHotelId = "-1";
                        int detailNo = 0;
                        int sumCharge = 0; // 税込分合計
                        int sumChargeNotInc = 0; // 税抜分合計
                        int billSlipNo = 0;
                        int accrecvAmount = 0;

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

                            if ( wkHotelId.equals( result.getString( "hotel_id" ) ) )
                            {
                                if ( result.getInt( "slip_detail_no" ) == 1 )
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

                                    // ret = OwnerBkoCommon.UpdateBill( prestate, connection, billSlipNo, sumCharge );
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

                                // 自動採番された請求伝票No取得
                                billSlipNo = getInsertedBillSlipNo( prestate, connection );

                                // 請求書番号更新
                                updateBillNo( prestate, connection, billSlipNo );

                                detailNo = 1;
                            }

                            if ( result.getInt( "slip_detail_no" ) == 1 )
                            {

                                // 請求・売掛データ登録
                                OwnerBkoCommon.RegistRelBillAccountRecv( prestate, connection, billSlipNo, detailNo, result.getInt( "accrecv_slip_no" ), OwnerBkoCommon.CLOSING_KBN_MISYORI );

                            }
                            wkHotelId = result.getString( "hotel_id" );
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

                    query = "START TRANSACTION ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();

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
                }

                if ( ret )
                {
                    System.out.println( "COMMIT" );
                    query = "COMMIT ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();

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
                }

                System.out.println( "[LogicOwnerBkoClosing.excute] closingYYYYMM = " + closingDate + ", closingKbn = " + closingKbn );
            }
            catch ( Exception e )
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                System.out.println( e.toString() );

                // エラーメール送信
                sendErrorMail( Message.getMessage( "erro.30002", "締め処理" ) + e.toString() );

                return false;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.excute] Exception=" + e.toString() );

            // エラーメール送信
            sendErrorMail( Message.getMessage( "erro.30002", "締め処理" ) + e.toString() );

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
        query = "UPDATE hh_bko_account_recv ";
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

        // 売掛明細データ
        query = "UPDATE hh_bko_account_recv_detail ";
        query = query + "SET closing_kind = ? ";
        // query = query + "last_update = ?, ";
        // query = query + "last_uptime = ? ";
        query = query + "WHERE closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        prestate.setInt( 2, whereClosingKind );

        prestate.executeUpdate();

        // 請求データ
        query = "UPDATE hh_bko_bill ";
        query = query + "SET closing_kind = ?, ";
        query = query + "bill_issue_date = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ? ";
        query = query + "WHERE closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        if ( OwnerBkoCommon.CLOSING_KBN_HON == setClosingKind )
        {
            Calendar cal = Calendar.getInstance();
            int year = cal.get( Calendar.YEAR );
            int month = cal.get( Calendar.MONTH ) + 1;
            int day = 17; // cal.get( Calendar.DATE );
            int now = (year * 10000 + month * 100 + day);

            prestate.setInt( 2, now );
        }
        else
        {
            prestate.setInt( 2, 0 );
        }
        prestate.setInt( 3, lastUpdate );
        prestate.setInt( 4, lastUptime );
        prestate.setInt( 5, whereClosingKind );

        prestate.executeUpdate();

        // 請求・売掛データ
        query = "UPDATE hh_bko_rel_bill_account_recv ";
        query = query + "SET closing_kind = ? ";
        query = query + "WHERE closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        prestate.setInt( 2, whereClosingKind );

        prestate.executeUpdate();

        // 請求明細データ
        query = "UPDATE hh_bko_bill_detail ";
        query = query + "SET closing_kind = ? ";
        query = query + "WHERE closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        prestate.setInt( 2, whereClosingKind );

        prestate.executeUpdate();

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

        return ret;
    }

    /**
     * 売掛データ更新
     *
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param dataAccount DataBkoAccountRecv
     * @return true:正常、false:失敗
     */
    private boolean updateRecv(PreparedStatement prestate, Connection conn, DataBkoAccountRecv dataAccount) throws Exception
    {
        String query = "";
        query = query + "UPDATE hh_bko_account_recv SET ";
        query = query + "usage_charge = ?, ";
        query = query + "accrecv_amount = ?, ";
        query = query + "accrecv_balance = ? ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, dataAccount.getUsageCharge() );
            prestate.setInt( 2, dataAccount.getAccrecvAmount() );
            prestate.setInt( 3, dataAccount.getAccrecvBalance() );
            prestate.setInt( 4, dataAccount.getAccrecvSlipNo() );

            if ( prestate.executeUpdate() <= 0 )
            {
                throw new Exception( "[LogicOwnerBkoClosing.updateRecv] updateRecv" );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.updateRecv] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return true;
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
        String query = "";
        query = query + "INSERT hh_bko_bill ( ";
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
            // DBConnection.releaseResources( result, prestate, connection );
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
            // DBConnection.releaseResources( result, prestate, connection );
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
     * 締め時刻の取得
     *
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return 最大締め時刻
     */
    private int getDeadlineTime(PreparedStatement prestate, Connection conn)
    {
        String query;
        ResultSet result = null;

        query = "SELECT MAX(deadline_time) AS deadline_time FROM hh_rsv_reserve_basic";

        try
        {
            prestate = conn.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    return result.getInt( "deadline_time" );
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.getDeadlineTime] Exception=" + e.toString() );
            return -1;
        }
        finally
        {
            // DBConnection.releaseResources( result, prestate, connection );
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return -1;
    }

    /**
     * 締め制御データのチェック
     *
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param closingDate 締め処理年月
     * @param closingKind 締め処理区分
     * @return 判別結果(true:正しい,false：正しくない)
     */
    private boolean checkClosingControl(PreparedStatement prestate, Connection connection, int closingDate, int closingKind)
    {
        String query;
        ResultSet result = null;

        try
        {
            int maxClosingDate = 0;
            int maxClosingKind = 0;

            // 共通　実行中か
            query = "SELECT * FROM hh_bko_closing_control WHERE exec_flag = 1";

            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // エラーメール送信
                    sendErrorMail( Message.getMessage( "warn.30029" ) );

                    return false;
                }
            }

            // 最終年月を取得
            query = "select max(closing_date) as closing_date from hh_bko_closing_control";

            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    maxClosingDate = result.getInt( "closing_date" );
                }
                else
                {
                    // エラーメール送信
                    sendErrorMail( Message.getMessage( "warn.30030" ) );

                    return false;
                }
            }
            else
            {
                return false;
            }

            // 最終年月の状態を取得
            query = "select closing_kind from hh_bko_closing_control where closing_date = ?";
            // System.out.println( query);

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, maxClosingDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    maxClosingKind = result.getInt( "closing_kind" );
                }
                else
                {
                    // エラーメール送信
                    sendErrorMail( Message.getMessage( "warn.30030" ) );
                    return false;
                }
            }
            else
            {
                return false;
            }

            // 仮締めの場合
            if ( OwnerBkoCommon.CLOSING_KBN_KARI == closingKind )
            {

                int lastMonthYYYYMMDD = DateEdit.addMonth( (closingDate * 100 + 1), -1 );
                int lastMonthYYYYMM = lastMonthYYYYMMDD / 100;

                // 先月が本か
                if ( !(maxClosingDate == lastMonthYYYYMM && maxClosingKind == OwnerBkoCommon.CLOSING_KBN_HON) )
                {
                    // System.out.println( maxClosingDate );
                    // System.out.println( lastMonthYYYYMM );
                    // System.out.println( maxClosingKind );

                    // エラーメール送信
                    sendErrorMail( Message.getMessage( "warn.30031" ) );

                    return false;
                }

            }
            else if ( OwnerBkoCommon.CLOSING_KBN_HON == closingKind )
            {

                // 本締めの場合、今月が仮締めか
                if ( !(maxClosingDate == closingDate && maxClosingKind == OwnerBkoCommon.CLOSING_KBN_KARI) )
                {
                    // エラーメール送信
                    sendErrorMail( Message.getMessage( "warn.30032" ) );

                    return false;
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.checkClosingControl] Exception=" + e.toString() );
            return false;
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

        SendMail.send( getMessage( MAIL_FROM ), getMessage( MAIL_TO ), getMessage( MAIL_SUCCESSTITLE ), msg );
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

        SendMail.send( getMessage( MAIL_FROM ), getMessage( MAIL_TO ), getMessage( MAIL_ERRORTITLE ), msg );
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
