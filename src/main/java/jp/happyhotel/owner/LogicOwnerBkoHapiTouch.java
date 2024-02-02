package jp.happyhotel.owner;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
import jp.happyhotel.data.DataBkoAccountRecv;
import jp.happyhotel.data.DataBkoAccountRecvDetail;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataUserPointPayTemp;

public class LogicOwnerBkoHapiTouch implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID    = -1694452370598598055L;

    // ポイント区分
    private static final int    POINT_KIND_RAITEN   = 21;                               // 来店
    private static final int    POINT_KIND_RIYOU    = 22;                               // 利用
    private static final int    POINT_KIND_WARIBIKI = 23;                               // 割引
    private static final int    POINT_KIND_YOYAKU   = 24;                               // 予約
    private static final int    POINT_KIND_BONUS    = 29;                               // 予約ボーナス

    // 最終更新日
    private int                 lastUpdate          = 0;

    // 最終更新日時
    private int                 lastUptime          = 0;

    private static final String CONF_FILE           = "/etc/happyhotel/backoffice.conf";
    private static final String MAIL_ERRORTITLE     = "mail.errortitle";
    private static final String MAIL_SUCCESSTITLE   = "mail.successtitle";

    public boolean execInsert(String userId, DataUserPointPayTemp duppt, int ciSeq)
    {
        boolean ret = true;

        // 今回のデータが新規か、追加かを判断する
        BkoAccountRecv bko;
        bko = new BkoAccountRecv();
        ret = bko.getData( duppt );
        if ( ret != false )
        {
            // hh_bko_account_recv_detailのみを追加
            ret = this.insertData( userId, duppt, bko, true, ciSeq );
        }
        else
        {
            // hh_bko_account_recvとhh_bko_account_recv_detailを作成
            ret = this.insertData( userId, duppt, bko, false, ciSeq );
        }

        return ret;
    }

    public boolean execUpdate(String userId, DataUserPointPayTemp duppt)
    {
        boolean ret = true;

        // 売り掛けデータ、売り掛け明細データを取得
        BkoAccountRecv bko;
        bko = new BkoAccountRecv();
        ret = bko.getDetailData( duppt );

        if ( ret != false )
        {
            ret = this.updateData( userId, duppt, bko );
        }

        return ret;
    }

    public boolean execUpdate(String userId, DataHotelCi dhc)
    {
        // boolean returnSts = true;
        Connection connection = null;

        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataBkoAccountRecv dataAcc = null;
        boolean ret = true;
        boolean additionFlag = false;
        dataAcc = new DataBkoAccountRecv();
        DataBkoAccountRecvDetail dataAccDetail = new DataBkoAccountRecvDetail();
        DataRsvReserve drr = new DataRsvReserve();
        int slipDetailNo = 0;

        // ホテルIDとチェックインコードから売掛データが登録されているかどうかを検査する。
        additionFlag = dataAcc.getData( dhc.getId(), dhc.getSeq() );
        if ( additionFlag != false )
        {
            slipDetailNo = dataAccDetail.getSlipDetailNo( dataAcc.getAccrecvSlipNo() );
            // 明細を削除する。
            dataAccDetail.deleteData( dataAcc.getAccrecvSlipNo() );
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            try
            {
                // ■処理開始
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
                query = query + "point.amount, "; // 割引の金額(point_kind=23)or利用金額(point_kind=22)
                query = query + "point.point_kind, ";
                query = query + "point.get_date, ";
                query = query + "point.get_time, ";
                query = query + "point.employee_code, ";
                query = query + "point.ci_date, ";
                query = query + "point.ci_time, ";
                query = query + "hotelbasic.id, ";
                query = query + "hotelbasic.hotenavi_id, ";
                query = query + "reservebasic.hotel_id, ";
                query = query + "reservebasic.user_id, ";
                query = query + "0 as regist_date, ";
                query = query + "0 as regist_time, ";
                query = query + "point.visit_seq, ";
                query = query + "point.user_seq, ";
                query = query + "point.slip_no, ";
                query = query + "point.room_no, ";
                query = query + "billingHotel.bill_cd, ";
                query = query + "ci.seq, ";
                query = query + "ci.rsv_no ";

                query = query + "FROM hh_hotel_basic hotelbasic "; // ホテル基本データ
                query = query + "INNER JOIN hh_rsv_reserve_basic reservebasic ON reservebasic.id = hotelbasic.id ";

                // query = query + "INNER JOIN hh_user_point_pay_temp point ON point.hotenavi_id = hotelbasic.hotenavi_id ";
                // 上記のURLが同じホテナビIDで複数のホテルIDを持っていた場合に、うまくいかないため下記の結合条件に修正　2015/01/16　Tashiro
                query = query + "INNER JOIN hh_user_point_pay_temp point ON point.ext_code = hotelbasic.id ";
                query = query + "INNER JOIN hh_hotel_ci ci ON ci.id = hotelbasic.id  AND ci.user_id = point.user_id AND ci.user_seq=point.user_seq AND ci.visit_seq=point.visit_seq ";
                query = query + "INNER JOIN hh_bko_rel_billing_hotel billingHotel ON billingHotel.id = hotelbasic.id ";
                query = query + "WHERE ci.id = ? AND ci.seq = ? AND ci_status=1 ";
                query = query + "AND point.point_kind IN (21,22,23,24,29) ";
                query = query + "GROUP BY hotelbasic.id ,point.visit_seq ,point.point_kind ";
                query = query + "ORDER BY hotelbasic.id ,point.visit_seq ,point.point_kind ";

                // 売掛データ作成
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, dhc.getId() );
                prestate.setInt( 2, dhc.getSeq() );
                result = prestate.executeQuery();
                Logging.info( "hdc.getId():" + dhc.getId() + ",dhc.getSeq():" + dhc.getSeq() + ",query:" + query, "STEP14.LogicOwnerBkoHapiTouch" );

                if ( result != null )
                {
                    int accrecvSlipNo = -1;
                    int accrecvAmount = 0; // 売掛金額
                    while( result.next() != false )
                    {

                        // 追加フラグ=false（新規でデータ追加）
                        if ( additionFlag == false )
                        {
                            // ■売掛データ追加
                            dataAcc = new DataBkoAccountRecv();
                            dataAcc.setHotelId( result.getString( "hotelbasic.hotenavi_id" ) ); // ホテルID（ホテナビ）
                            dataAcc.setId( result.getInt( "hotelbasic.id" ) ); // ホテルID（ハピホテ）
                            dataAcc.setAddUpDate( lastUpdate ); // 計上日
                            dataAcc.setBillCd( result.getInt( "billingHotel.bill_cd" ) );// 請求先コード
                            dataAcc.setBillName( "" ); // 請求先名

                            dataAcc.setPersonName( OwnerBkoCommon.GetPersonName( prestate, connection, result.getString( "hotelbasic.hotenavi_id" ), result.getInt( "point.employee_code" ) ) );// 担当者名
                            dataAcc.setUserManagementNo( result.getInt( "point.user_seq" ) );// ユーザ管理番号
                            if ( result.getInt( "point.ci_date" ) > 0 )
                            {
                                dataAcc.setUsageDate( result.getInt( "point.ci_date" ) );// 利用日
                            }
                            else
                            {
                                dataAcc.setUsageDate( result.getInt( "point.get_date" ) );// 利用日
                            }

                            if ( result.getInt( "point.ci_time" ) > 0 )
                            {
                                dataAcc.setUsageTime( result.getInt( "point.ci_time" ) );// 利用時刻
                            }
                            else
                            {
                                dataAcc.setUsageTime( result.getInt( "point.get_time" ) );// 利用時刻
                            }
                            dataAcc.setHtSlipNo( result.getInt( "point.slip_no" ) );// 伝票No（ハピタッチ）
                            dataAcc.setHtRoomNo( result.getString( "point.room_no" ) );// 部屋番号（ハピタッチ）
                            dataAcc.setUsageCharge( result.getInt( "point.amount" ) );// 利用金額

                            if ( POINT_KIND_RIYOU == result.getInt( "point.point_kind" ) )
                            {
                                // 付与
                                accrecvAmount = result.getInt( "point.point" ) * 2;
                            }
                            else if ( POINT_KIND_YOYAKU == result.getInt( "point.point_kind" ) )
                            {
                                // 予約
                                accrecvAmount = result.getInt( "point.amount" );
                            }
                            else if ( POINT_KIND_WARIBIKI == result.getInt( "point.point_kind" ) )
                            {
                                // 使用
                                accrecvAmount = result.getInt( "point.point" );
                            }
                            else if ( POINT_KIND_BONUS == result.getInt( "point.point_kind" ) )
                            {
                                // 予約ボーナス
                                accrecvAmount = result.getInt( "point.point" );
                            }
                            // if ( !result.getString( "ci.rsv_no" ).equals( "" ) )
                            // {
                            // if ( drr.getData( result.getInt( "hotelbasic.id" ), result.getString( "ci.rsv_no" ) ) )
                            // {
                            // dataAcc.setReceiveCharge( drr.getChargeTotal() ); // 予約金額
                            // }
                            // }
                            dataAcc.setAccrecvAmount( accrecvAmount ); // 売掛金額
                            dataAcc.setAccrecvBalance( accrecvAmount );// 売掛残
                            dataAcc.setHappyBalance( result.getInt( "point.then_point" ) );// ハピー残高
                            dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// 締め処理区分
                            dataAcc.setOwnerHotelId( result.getString( "reservebasic.hotel_id" ) );// オーナーホテルID
                            dataAcc.setOwnerUserId( result.getInt( "reservebasic.user_id" ) );// オーナーユーザID
                            dataAcc.setCiSeq( result.getInt( "ci.seq" ) ); // ホテルID（ハピホテ）

                            // ret = OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );
                            OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );
                            Logging.info( "STEP16", "ret:" + ret );

                            // 自動採番された売掛伝票No取得
                            accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, connection );
                            Logging.info( "STEP17", "accrecvSlipNo:" + accrecvSlipNo );
                            dataAcc.setAccrecvSlipNo( accrecvSlipNo );

                            // 伝票明細No初期化
                            slipDetailNo = 0;
                            additionFlag = true; // 追加したので
                        }
                        else
                        {
                            // ■売掛データ更新
                            // dataAcc = new DataBkoAccountRecv();
                            // if ( dataAcc.getData( result.getInt( "hotelbasic.id" ), result.getInt( "ci.seq" ) ) != false )
                            // {
                            if ( result.getInt( "point.amount" ) >= 0 )
                            {
                                dataAcc.setUsageCharge( result.getInt( "point.amount" ) );// 利用金額
                            }

                            // 取得していたNoに+1をする
                            accrecvSlipNo = dataAcc.getAccrecvSlipNo();

                            if ( POINT_KIND_RIYOU == result.getInt( "point.point_kind" ) )
                            {
                                // 付与
                                accrecvAmount += result.getInt( "point.point" ) * 2;
                            }
                            else if ( POINT_KIND_YOYAKU == result.getInt( "point.point_kind" ) )
                            {
                                // 予約
                                accrecvAmount += result.getInt( "point.amount" );
                            }
                            else if ( POINT_KIND_WARIBIKI == result.getInt( "point.point_kind" ) )
                            {
                                // 使用
                                accrecvAmount += result.getInt( "point.point" );
                            }
                            else if ( POINT_KIND_BONUS == result.getInt( "point.point_kind" ) )
                            {
                                // 予約ボーナス
                                accrecvAmount += result.getInt( "point.point" );
                            }

                            if ( result.getInt( "hotelbasic.id" ) > 0 )
                            {
                                dataAcc.setId( result.getInt( "hotelbasic.id" ) );// 部屋番号（ハピタッチ）
                            }
                            if ( result.getString( "point.room_no" ).compareTo( "" ) != 0 )
                            {
                                dataAcc.setHtRoomNo( result.getString( "point.room_no" ) );// 部屋番号（ハピタッチ）
                            }
                            // 伝票番号が追加されていたら登録
                            if ( result.getInt( "point.slip_no" ) > 0 )
                            {
                                dataAcc.setHtSlipNo( result.getInt( "point.slip_no" ) );
                            }

                            dataAcc.setAccrecvAmount( accrecvAmount ); // 売掛金額
                            dataAcc.setAccrecvBalance( accrecvAmount ); // 売掛残
                            dataAcc.setHappyBalance( result.getInt( "point.then_point" ) );// ハピー残高
                            Logging.info( query, "STEP15-2" + ",id=" + dhc.getId() + ",Seq=" + dhc.getSeq() );

                            updateRecv( prestate, connection, dataAcc );
                            // }
                        }

                        switch( result.getInt( "point.point_kind" ) )
                        {
                            case POINT_KIND_RAITEN:

                                // 来店ハピー
                                dataAccDetail.setSlipKind( 0 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_100 );
                                dataAccDetail.setAmount( 0 );
                                break;

                            case POINT_KIND_RIYOU:

                                // 付与
                                dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_110 );
                                dataAccDetail.setAmount( result.getInt( "point.point" ) * 2 );
                                break;

                            case POINT_KIND_WARIBIKI:

                                // 使用
                                dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_120 );
                                dataAccDetail.setAmount( result.getInt( "point.point" ) );
                                break;

                            case POINT_KIND_YOYAKU:

                                // 予約ハピー
                                dataAccDetail.setSlipKind( 0 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_130 );
                                dataAccDetail.setAmount( 0 );
                                break;

                            case POINT_KIND_BONUS:

                                // 予約ボーナス
                                dataAccDetail.setSlipKind( 0 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_150 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_150 );
                                dataAccDetail.setAmount( result.getInt( "point.point" ) );
                                break;

                            default:
                                break;
                        }
                        Logging.info( "STEP18", "accrecvSlipNo:" + accrecvSlipNo + ",TitleCd=" + dataAccDetail.getAccountTitleCd() );

                        if ( dataAccDetail.getDataByTitleCd( accrecvSlipNo, dataAccDetail.getAccountTitleCd() ) == false )
                        {
                            slipDetailNo++;
                            Logging.info( "STEP19", "accrecvSlipNo:" + accrecvSlipNo + ",slipDetailNo=" + slipDetailNo );
                            dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                            dataAccDetail.setSlipDetailNo( slipDetailNo );
                            dataAccDetail.setPoint( result.getInt( "point.point" ) );
                            dataAccDetail.setId( result.getInt( "hotelbasic.id" ) );
                            dataAccDetail.setReserveNo( result.getString( "ci.rsv_no" ) );
                            dataAccDetail.setUserId( result.getString( "point.user_id" ) );
                            dataAccDetail.setSeq( result.getInt( "point.seq" ) );
                            dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );

                            // 売掛明細データ追加
                            OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        }
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
            }
            catch ( Exception e )
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                Logging.error( "LogicOwnerBkoHapiTouch.insertData] Exception:" + e.toString(), "ROLLBACK" );

                System.out.println( e.toString() );

                // エラーメール送信
                sendErrorMail( Message.getMessage( "erro.30002", "締め処理" ) + e.toString() );

                return false;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerHapiTouch.excute] Exception=" + e.toString() );
            Logging.error( "LogicOwnerHapiTouch.insertData] Exception:" + e.toString(), "" );

            // エラーメール送信
            sendErrorMail( Message.getMessage( "erro.30002", "締め処理" ) + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }

    public boolean execCancel(String userId, DataUserPointPayTemp duppt)
    {
        boolean ret = true;

        // 売り掛けデータ、売り掛け明細データを取得
        BkoAccountRecv bko;
        bko = new BkoAccountRecv();
        ret = bko.getDetailData( duppt );

        if ( ret != false )
        {
            ret = this.cancelData( userId, duppt, bko );
        }
        else
        {
            ret = true; // 売掛データがなかったので更新しなかった。
        }

        return ret;
    }

    public boolean insertData(String userId, DataUserPointPayTemp duppt, BkoAccountRecv bko, boolean additionFlag, int ciSeq)
    {
        // boolean returnSts = true;
        Connection connection = null;

        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = true;
        DataBkoAccountRecv dataAcc = null;
        int closingDate = 0;
        int closingKbn = 0;
        boolean controlChangeFlg = false;
        DataRsvReserve drr = new DataRsvReserve();

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            try
            {
                // ■処理開始
                controlChangeFlg = true;
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
                query = query + "point.amount, "; // 割引の金額(point_kind=23)or利用金額(point_kind=22)
                query = query + "point.point_kind, ";
                query = query + "point.get_date, ";
                query = query + "point.get_time, ";
                query = query + "point.ext_string, ";
                query = query + "point.employee_code, ";
                query = query + "point.ci_date, ";
                query = query + "point.ci_time, ";
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
                query = query + "INNER JOIN hh_rsv_reserve_basic reservebasic ON reservebasic.id = hotelbasic.id ";

                // query = query + "INNER JOIN hh_user_point_pay_temp point ON point.hotenavi_id = hotelbasic.hotenavi_id ";
                // 上記のURLが同じホテナビIDで複数のホテルIDを持っていた場合に、うまくいかないため下記の結合条件に修正　2015/01/16　Tashiro
                query = query + "INNER JOIN hh_user_point_pay_temp point ON point.ext_code = hotelbasic.id ";

                query = query + "INNER JOIN hh_bko_rel_billing_hotel billingHotel ON billingHotel.id = hotelbasic.id ";

                query = query + "WHERE point.user_id = ? AND point.seq = ? ";
                query = query + "AND (point.point_kind = 21 OR point.point_kind = 22 OR point.point_kind = 23 OR point.point_kind = 24) ";
                query = query + "ORDER BY hotelbasic.id ,visit_seq ,point.point_kind";

                // 売掛データ作成
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userId );
                prestate.setInt( 2, duppt.getSeq() );
                result = prestate.executeQuery();

                if ( result != null )
                {
                    int wkVisitSeq = -1; // 管理番号
                    int accrecvSlipNo = -1;
                    int slipDetailNo = -1;

                    // 売掛データを更新していく
                    // ①売掛データ追加→②売掛明細追加→ブレイクしてなければ③売掛データ更新→④売掛明細更新
                    // 　　　　　　　　　　　　　　　　→ブレイクしていたら①売掛データ追加→・・・
                    while( result.next() )
                    {
                        int accrecvAmount = 0; // 売掛金額

                        // 追加フラグ=false（新規でデータ追加）
                        if ( additionFlag == false )
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
                            if ( result.getInt( "point.ci_date" ) > 0 )
                            {
                                dataAcc.setUsageDate( result.getInt( "point.ci_date" ) );// 利用日
                            }
                            else
                            {
                                dataAcc.setUsageDate( result.getInt( "point.get_date" ) );// 利用日
                            }

                            if ( result.getInt( "point.ci_time" ) > 0 )
                            {
                                dataAcc.setUsageTime( result.getInt( "point.ci_time" ) );// 利用時刻
                            }
                            else
                            {
                                dataAcc.setUsageTime( result.getInt( "point.get_time" ) );// 利用時刻
                            }
                            dataAcc.setHtSlipNo( result.getInt( "slip_no" ) );// 伝票No（ハピタッチ）
                            dataAcc.setHtRoomNo( result.getString( "room_no" ) );// 部屋番号（ハピタッチ）
                            dataAcc.setUsageCharge( result.getInt( "amount" ) );// 利用金額

                            if ( POINT_KIND_RIYOU == result.getInt( "point_kind" ) )
                            {
                                // 付与
                                accrecvAmount = result.getInt( "point" ) * 2;
                            }
                            else if ( POINT_KIND_YOYAKU == result.getInt( "point_kind" ) )
                            {
                                // 予約
                                accrecvAmount = result.getInt( "amount" );
                            }
                            else if ( POINT_KIND_WARIBIKI == result.getInt( "point_kind" ) )
                            {
                                // 使用
                                accrecvAmount = result.getInt( "point" );
                            }
                            else if ( POINT_KIND_BONUS == result.getInt( "point_kind" ) )
                            {
                                // 予約ボーナス
                                accrecvAmount = result.getInt( "point" );
                            }
                            // if ( !result.getString( "point.ext_string" ).equals( "" ) )
                            // {
                            // if ( drr.getData( result.getInt( "hotelbasic.id" ), result.getString( "point.ext_string" ) ) )
                            // {
                            // dataAcc.setReceiveCharge( drr.getChargeTotal() ); // 予約金額
                            // }
                            // }
                            dataAcc.setAccrecvAmount( accrecvAmount ); // 売掛金額
                            dataAcc.setAccrecvBalance( accrecvAmount );// 売掛残
                            dataAcc.setHappyBalance( result.getInt( "then_point" ) );// ハピー残高
                            dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// 締め処理区分
                            dataAcc.setOwnerHotelId( result.getString( "reservebasic.hotel_id" ) );// オーナーホテルID
                            dataAcc.setOwnerUserId( result.getInt( "reservebasic.user_id" ) );// オーナーユーザID
                            dataAcc.setCiSeq( ciSeq );// チェックインコード

                            // ret=OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );
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
                            dataAcc = bko.getBkoAccountRecv();
                            if ( result.getInt( "amount" ) > 0 )
                            {
                                dataAcc.setUsageCharge( result.getInt( "amount" ) );// 利用金額
                            }

                            // 取得していたNoに+1をする
                            accrecvSlipNo = bko.getBkoAccountRecv().getAccrecvSlipNo();
                            slipDetailNo = bko.getSlipDetailNo() + 1;

                            if ( POINT_KIND_RIYOU == result.getInt( "point_kind" ) )
                            {
                                // 付与
                                accrecvAmount = result.getInt( "point" ) * 2;
                            }
                            else if ( POINT_KIND_YOYAKU == result.getInt( "point_kind" ) )
                            {
                                // 予約
                                accrecvAmount = result.getInt( "amount" );
                            }
                            else if ( POINT_KIND_WARIBIKI == result.getInt( "point_kind" ) )
                            {
                                // 使用
                                accrecvAmount = result.getInt( "point" );
                            }
                            else if ( POINT_KIND_BONUS == result.getInt( "point_kind" ) )
                            {
                                // 予約ボーナス
                                accrecvAmount = result.getInt( "point" );
                            }

                            if ( result.getInt( "id" ) > 0 )
                            {
                                dataAcc.setId( result.getInt( "id" ) );// 部屋番号（ハピタッチ）
                            }
                            if ( result.getString( "room_no" ).compareTo( "" ) != 0 )
                            {
                                dataAcc.setHtRoomNo( result.getString( "room_no" ) );// 部屋番号（ハピタッチ）
                            }
                            // 伝票番号が追加されていたら登録
                            if ( result.getInt( "slip_no" ) > 0 )
                            {
                                dataAcc.setHtSlipNo( result.getInt( "slip_no" ) );
                            }

                            dataAcc.setAccrecvAmount( dataAcc.getAccrecvAmount() + accrecvAmount ); // 売掛金額
                            dataAcc.setAccrecvBalance( dataAcc.getAccrecvBalance() + accrecvAmount ); // 売掛残
                            dataAcc.setHappyBalance( result.getInt( "then_point" ) );// ハピー残高

                            updateRecv( prestate, connection, dataAcc );
                        }

                        // 売掛明細データ　共通項目設定
                        DataBkoAccountRecvDetail dataAccDetail = new DataBkoAccountRecvDetail();
                        dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                        dataAccDetail.setSlipDetailNo( slipDetailNo );
                        dataAccDetail.setPoint( result.getInt( "point" ) );
                        dataAccDetail.setId( result.getInt( "id" ) );
                        dataAccDetail.setReserveNo( result.getString( "point.ext_string" ) );
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

                            case POINT_KIND_RIYOU:

                                // 付与
                                dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_110 );
                                dataAccDetail.setAmount( result.getInt( "point" ) * 2 );
                                break;

                            case POINT_KIND_WARIBIKI:

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

                            case POINT_KIND_BONUS:

                                // 予約ボーナス
                                dataAccDetail.setSlipKind( 0 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_150 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_150 );
                                dataAccDetail.setAmount( 0 );
                                break;

                            default:
                                break;
                        }

                        OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );

                        wkVisitSeq = result.getInt( "visit_seq" ); // 来店管理番号
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
                System.out.println( "[LogicOwnerBkoHapiTouth.insertData] closingYYYYMM = " + closingDate + ", closingKbn = " + closingKbn );
            }
            catch ( Exception e )
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                Logging.error( "LogicOwnerBkoClosing.insertData] Exception:" + e.toString(), "ROLLBACK" );

                System.out.println( e.toString() );

                // エラーメール送信
                sendErrorMail( Message.getMessage( "erro.30002", "締め処理" ) + e.toString() );

                return false;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.excute] Exception=" + e.toString() );
            Logging.error( "LogicOwnerBkoClosing.insertData] Exception:" + e.toString(), "" );

            // エラーメール送信
            sendErrorMail( Message.getMessage( "erro.30002", "締め処理" ) + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }

    /***
     * 売り掛けデータ更新
     * 
     * @param userId ユーザID
     * @param duppt ユーザ有料ポイント一時データ（DataUserPointPayTemp）
     * @param bko 売り掛けデータ(BkoAccountRec)
     * @return
     */
    public boolean updateData(String userId, DataUserPointPayTemp duppt, BkoAccountRecv bko)
    {
        // boolean returnSts = true;
        Connection connection = null;

        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = true;
        DataBkoAccountRecv dataAcc = null;
        DataBkoAccountRecvDetail dataAccDetail = new DataBkoAccountRecvDetail();
        int closingDate = 0;
        int closingKbn = 0;
        boolean controlChangeFlg = false;
        int wkVisitSeq = -1; // 管理番号
        int accrecvSlipNo = -1;
        int slipDetailNo = -1;
        int accrecvAmount = 0; // 売掛金額
        int accrecvAmountOld = 0; // 売掛金額(以前)

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            try
            {
                // ■処理開始
                controlChangeFlg = true;
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // ■売掛データ更新

                // 同じ売掛の場合、伝票明細No + 1
                dataAcc = bko.getBkoAccountRecv();
                dataAccDetail = bko.getBkoAccountRecvDetail();

                // 金額 = 従来の金額 - 過去登録時の金額 + 訂正後の金額
                // dataAcc.setUsageCharge( dataAcc.getUsageCharge() - dataAccDetail.getAmount() + duppt.getAmount() );// 利用金額
                dataAcc.setUsageCharge( duppt.getAmount() );// 利用金額
                //
                accrecvSlipNo = bko.getBkoAccountRecv().getAccrecvSlipNo();
                slipDetailNo = bko.getSlipDetailNo();

                if ( POINT_KIND_RIYOU == duppt.getPointKind() )
                {
                    // 付与
                    accrecvAmount = duppt.getPoint() * 2;
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_YOYAKU == duppt.getPointKind() )
                {
                    // 予約
                    accrecvAmount = duppt.getAmount();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_WARIBIKI == duppt.getPointKind() )
                {
                    // 使用
                    accrecvAmount = duppt.getPoint();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_BONUS == duppt.getPointKind() )
                {
                    // 使用
                    accrecvAmount = duppt.getPoint();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }

                if ( duppt.getRoomNo().compareTo( "" ) != 0 )
                {
                    dataAcc.setHtRoomNo( duppt.getRoomNo() );// 部屋番号（ハピタッチ）
                }
                if ( duppt.getSlipNo() > 0 )
                {
                    dataAcc.setHtSlipNo( duppt.getSlipNo() );// 部屋番号（ハピタッチ）
                }
                dataAcc.setHappyBalance( duppt.getThenPoint() );// ハピー残高

                // 売り掛け金額 = 従来の金額 - 過去登録時の金額 + 訂正後の金額
                dataAcc.setAccrecvAmount( dataAcc.getAccrecvAmount() - accrecvAmountOld + accrecvAmount ); // 売掛金額
                dataAcc.setAccrecvBalance( dataAcc.getAccrecvBalance() - accrecvAmountOld + accrecvAmount ); // 売掛残

                // 売り掛けデータの更新
                updateRecv( prestate, connection, dataAcc );

                // 売掛明細データ　共通項目設定
                dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                dataAccDetail.setSlipDetailNo( slipDetailNo );
                dataAccDetail.setPoint( duppt.getPoint() );
                // dataAccDetail.setId( result.getInt( "id" ) );
                // dataAccDetail.setReserveNo( result.getString( "point.ext_string" ) );
                dataAccDetail.setUserId( duppt.getUserId() );
                dataAccDetail.setSeq( duppt.getSeq() );
                dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );

                // 売掛明細データ登録
                switch( duppt.getPointKind() )
                {
                    case POINT_KIND_RAITEN:

                        // 来店ハピー
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_100 );
                        dataAccDetail.setAmount( 0 );
                        break;

                    case POINT_KIND_RIYOU:

                        // 付与
                        dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_110 );
                        dataAccDetail.setAmount( duppt.getPoint() * 2 );
                        break;

                    case POINT_KIND_WARIBIKI:

                        // 使用
                        dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_120 );
                        dataAccDetail.setAmount( duppt.getPoint() );
                        break;

                    case POINT_KIND_YOYAKU:

                        // 予約ハピー
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_130 );
                        dataAccDetail.setAmount( 0 );
                        break;

                    case POINT_KIND_BONUS:

                        // 予約ボーナス
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_150 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_150 );
                        dataAccDetail.setAmount( 0 );
                        break;

                    default:
                        break;
                }
                // 売り掛け明細の更新
                ret = dataAccDetail.updateData();

                // OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );

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
            DBConnection.releaseResources( result, prestate, connection );
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
        query = query + "accrecv_balance = ?, ";
        query = query + "happy_balance = ?, ";
        query = query + "ht_room_no = ?, ";
        query = query + "ht_slip_no = ? ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, dataAccount.getUsageCharge() );
            prestate.setInt( 2, dataAccount.getAccrecvAmount() );
            prestate.setInt( 3, dataAccount.getAccrecvBalance() );
            prestate.setInt( 4, dataAccount.getHappyBalance() );
            prestate.setString( 5, dataAccount.getHtRoomNo() );
            prestate.setInt( 6, dataAccount.getHtSlipNo() );
            prestate.setInt( 7, dataAccount.getAccrecvSlipNo() );

            Logging.info( "" + dataAccount.getUsageCharge() + "," + dataAccount.getAccrecvAmount() + "," + dataAccount.getAccrecvBalance() + "," + dataAccount.getHappyBalance() + "," +
                    dataAccount.getHtRoomNo() + "," + dataAccount.getHtSlipNo() + "," + dataAccount.getAccrecvSlipNo(), "" );
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

    /***
     * 売り掛けデータ更新
     * 
     * @param userId ユーザID
     * @param duppt ユーザ有料ポイント一時データ（DataUserPointPayTemp）
     * @param bko 売り掛けデータ(BkoAccountRec)
     * @return
     */
    public boolean cancelData(String userId, DataUserPointPayTemp duppt, BkoAccountRecv bko)
    {
        // boolean returnSts = true;
        Connection connection = null;

        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = true;
        DataBkoAccountRecv dataAcc = null;
        DataBkoAccountRecvDetail dataAccDetail = new DataBkoAccountRecvDetail();
        int closingDate = 0;
        int closingKbn = 0;
        boolean controlChangeFlg = false;
        int wkVisitSeq = -1; // 管理番号
        int accrecvSlipNo = -1;
        int slipDetailNo = -1;
        int accrecvAmount = 0; // 売掛金額
        int accrecvAmountOld = 0; // 売掛金額(以前)

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            try
            {
                // ■処理開始
                controlChangeFlg = true;
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // ■売掛データ更新

                // 同じ売掛の場合、伝票明細No + 1
                dataAcc = bko.getBkoAccountRecv();
                dataAccDetail = bko.getBkoAccountRecvDetail();

                // 金額 = 従来の金額 - 過去登録時の金額 + 訂正後の金額
                // dataAcc.setUsageCharge( dataAcc.getUsageCharge() - dataAccDetail.getAmount() + duppt.getAmount() );// 利用金額
                dataAcc.setUsageCharge( duppt.getAmount() );// 利用金額
                //
                accrecvSlipNo = bko.getBkoAccountRecv().getAccrecvSlipNo();
                slipDetailNo = bko.getSlipDetailNo();

                if ( POINT_KIND_RIYOU == duppt.getPointKind() )
                {
                    // 付与
                    accrecvAmount = duppt.getPoint() * 2;
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_YOYAKU == duppt.getPointKind() )
                {
                    // 予約
                    accrecvAmount = duppt.getAmount();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_WARIBIKI == duppt.getPointKind() )
                {
                    // 使用
                    accrecvAmount = duppt.getPoint();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_BONUS == duppt.getPointKind() )
                {
                    // 予約ボーナス
                    accrecvAmount = duppt.getPoint();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }

                if ( duppt.getRoomNo().compareTo( "" ) != 0 )
                {
                    dataAcc.setHtRoomNo( duppt.getRoomNo() );// 部屋番号（ハピタッチ）
                }
                if ( duppt.getSlipNo() > 0 )
                {
                    dataAcc.setHtSlipNo( duppt.getSlipNo() );// 部屋番号（ハピタッチ）
                }
                dataAcc.setHappyBalance( duppt.getThenPoint() );// ハピー残高

                // 売り掛け金額 = キャンセルなので取得したデータをそのままセット
                dataAcc.setAccrecvAmount( accrecvAmount ); // 売掛金額
                dataAcc.setAccrecvBalance( accrecvAmount ); // 売掛残

                // 売り掛けデータの更新
                updateRecv( prestate, connection, dataAcc );

                // 売掛明細データ　共通項目設定
                dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                dataAccDetail.setSlipDetailNo( slipDetailNo );
                dataAccDetail.setPoint( duppt.getPoint() );
                // dataAccDetail.setId( result.getInt( "id" ) );
                // dataAccDetail.setReserveNo( "" );
                dataAccDetail.setUserId( duppt.getUserId() );
                dataAccDetail.setSeq( duppt.getSeq() );
                dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );

                // 売掛明細データ登録
                switch( duppt.getPointKind() )
                {
                    case POINT_KIND_RAITEN:

                        // 来店ハピー
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_100 );
                        dataAccDetail.setAmount( 0 );
                        break;

                    case POINT_KIND_RIYOU:

                        // 付与
                        dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_110 );
                        dataAccDetail.setAmount( duppt.getPoint() * 2 );
                        break;

                    case POINT_KIND_WARIBIKI:

                        // 使用
                        dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_120 );
                        dataAccDetail.setAmount( duppt.getPoint() );
                        break;

                    case POINT_KIND_YOYAKU:

                        // 予約ハピー
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_130 );
                        dataAccDetail.setAmount( 0 );
                        dataAccDetail.setPoint( 0 );
                        break;

                    case POINT_KIND_BONUS:

                        // 予約ボーナス
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_150 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_150 );
                        dataAccDetail.setAmount( 0 );
                        dataAccDetail.setPoint( 0 );
                        break;

                    default:
                        break;
                }
                // 売り掛け明細の更新
                ret = dataAccDetail.updateData();

                // OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );

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
            DBConnection.releaseResources( result, prestate, connection );
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

        Logging.info( "[LogincOwnerBkoHapiTouch] " + getMessage( MAIL_SUCCESSTITLE ) + ":" + msg );
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

        Logging.error( "[LogincOwnerBkoHapiTouch] " + getMessage( MAIL_ERRORTITLE ) + ":" + msg );
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
