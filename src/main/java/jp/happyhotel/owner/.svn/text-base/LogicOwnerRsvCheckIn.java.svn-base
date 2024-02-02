package jp.happyhotel.owner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.LockReserve;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.NumberingRsvNo;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataRsvMailRequest;
import jp.happyhotel.data.DataRsvPlan;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveHistory;
import jp.happyhotel.data.DataRsvResult;
import jp.happyhotel.data.DataRsvUserBasic;
import jp.happyhotel.data.DataUserPointPay;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;
import jp.happyhotel.reserve.FormReserveSheetPC;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 予約処理 business Logic
 */
public class LogicOwnerRsvCheckIn implements Serializable
{

    private static final long  serialVersionUID = 2418194078977326261L;
    private FormReserveSheetPC frm;

    /* フォームオブジェクト */
    public FormReserveSheetPC getFrm()
    {
        return frm;
    }

    public void setFrm(FormReserveSheetPC frm)
    {
        this.frm = frm;
    }

    /**
     * 
     * 実績データ存在確認
     * 
     * @param hotelId ホテルID
     * @param reserveDate 予約日
     * @param seq 部屋番号
     * @return true:存在しない、false:存在する
     * @throws Exception
     */
    public boolean existsRsvResult(int id, int rsvDate, int seq) throws Exception
    {
        boolean isResult = false;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        String query = "";
        int cnt = 0;

        query = "SELECT COUNT(*) AS CNT  FROM hh_rsv_result WHERE id = ? AND ci_date = ? AND seq = ? AND use_kind = 1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, rsvDate );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    cnt = result.getInt( "CNT" );
                }
            }

            if ( cnt > 0 )
            {
                isResult = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.existsRsvResult] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return isResult;

    }

    /**
     * 
     * 予約者来店処理
     * 
     * @param hotelId ホテルID
     * @param reserveNo 予約番号
     * @param reserveDate 予約日
     * @param seq 部屋番号
     * @return true:正常、false:異常
     * @throws Exception
     */
    public boolean execRaiten(int hotelId, String reserveNo, int reserveDate, int seq, String Schema) throws Exception
    {
        boolean isResult = false;
        boolean blnRet = false;
        int subNo = 0;
        String query = "";
        int newSeq = 0;
        int remainderFlg = 0;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ArrayList<Integer> lockId = new ArrayList<Integer>();
        ArrayList<Integer> lockReserveDate = new ArrayList<Integer>();
        ArrayList<Integer> lockSeq = new ArrayList<Integer>();

        try
        {
            // 新しい部屋番号取得
            newSeq = frm.getSeq();

            // 予約番号枝番取得
            getRsvSubNo( Schema );

            // リマインダーフラグ取得
            DataRsvReserve dataRsv = new DataRsvReserve();
            dataRsv.getData( hotelId, reserveNo );
            remainderFlg = dataRsv.getReminderFlag();

            int chargeTotalAll = 0;

            try
            {
                // 変更予定の内容でロック
                if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                {
                    // 他の人がロックしていた場合
                    frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                    return(blnRet);
                }

                lockId.add( frm.getSelHotelId() );
                lockReserveDate.add( frm.getRsvDate() );
                lockSeq.add( frm.getSeq() );

                connection = DBConnection.getConnection( false );
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                blnRet = execRaitenSub( connection, frm.getSelHotelId(), frm.getRsvNo(), frm.getRsvDate(), frm.getSeq(), Schema );
                chargeTotalAll = chargeTotalAll + frm.getChargeTotal();

                if ( blnRet )
                {
                    // 初日の値の保存
                    String SvRsvNo = frm.getRsvNo();
                    int SvRsvSubNo = frm.getRsvSubNo();
                    int SvRsvDate = frm.getRsvDate();
                    int SvSeq = frm.getSeq();
                    int SvOrgRsvDate = frm.getOrgRsvDate();
                    int SvSelPlanId = frm.getSelPlanId();

                    // 新予約で、連泊予約時、連泊分の予約情報を更新
                    if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) && StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                    {
                        String que = "SELECT * FROM " + Schema + ".hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                                + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";
                        ResultSet resultR = null;
                        PreparedStatement prestateR = null;
                        try
                        {
                            prestateR = connection.prepareStatement( que );
                            prestateR.setInt( 1, frm.getSelHotelId() );
                            prestateR.setString( 2, frm.getRsvNoMain() );
                            resultR = prestateR.executeQuery();

                            if ( resultR != null )
                            {
                                while( resultR.next() != false )
                                {
                                    // 入力値のセット
                                    frm.setRsvNo( resultR.getString( "reserve_no" ) );
                                    frm.setRsvSubNo( resultR.getInt( "reserve_sub_no" ) );
                                    frm.setRsvDate( resultR.getInt( "reserve_date" ) );
                                    frm.setSeq( resultR.getInt( "seq" ) );

                                    // 変更予定の内容でロック
                                    if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                                    {
                                        // 他の人がロックしていた場合
                                        frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                                        blnRet = false;
                                        return(blnRet);
                                    }

                                    lockId.add( resultR.getInt( "id" ) );
                                    lockReserveDate.add( resultR.getInt( "reserve_date" ) );
                                    lockSeq.add( resultR.getInt( "seq" ) );

                                    blnRet = execRaitenSub( connection, frm.getSelHotelId(), frm.getRsvNo(), frm.getRsvDate(), frm.getSeq(), Schema );
                                    chargeTotalAll = chargeTotalAll + resultR.getInt( "charge_total" );
                                    if ( blnRet == false )
                                    {
                                        break;
                                    }
                                }
                            }
                        }
                        catch ( Exception e )
                        {
                            Logging.error( "[LogicOwnerRsvCheckIn.execRaiten] Exception=" + e.toString() );
                            blnRet = false;
                        }
                        finally
                        {
                            DBConnection.releaseResources( prestateR );
                            DBConnection.releaseResources( resultR );
                        }
                    }

                    // frmの内容を初日の宿泊日のデータに戻す
                    frm.setRsvNo( SvRsvNo );
                    frm.setRsvDate( SvRsvDate );
                    frm.setRsvSubNo( SvRsvSubNo );
                    frm.setSeq( SvSeq );
                    frm.setOrgRsvDate( SvOrgRsvDate );
                    frm.setSelPlanId( SvSelPlanId );

                    // 連泊の場合は金額をトータルにする
                    frm.setChargeTotal( chargeTotalAll );
                }

                if ( blnRet )
                {
                    if ( remainderFlg == 1 )
                    {
                        // リマインダーがONの場合、サンキューメールを送信
                        sendMail( connection, reserveNo, 0, ReserveCommon.TERM_KIND_PC, ReserveCommon.MAIL_RAITEN, Schema );
                    }
                }

                // 有料ユーザポイント一時データ作成
                if ( frm.getAddPoint() != 0 )
                {
                    if ( blnRet )
                    {
                        blnRet = createUserPointPayTemp( connection, prestate );
                        Logging.info( "reserveNo:" + reserveNo + ",createUserPointPayTemp:" + blnRet );
                    }
                }

                // ボーナスマイルデータ作成
                if ( frm.getAddBonusMile() != 0 )
                {
                    if ( blnRet )
                    {
                        blnRet = createBonusMilePayTemp( connection, prestate );
                        Logging.info( "reserveNo:" + reserveNo + ",creatBonusMilePayPayTemp:" + blnRet );
                    }
                }

                if ( blnRet )
                {
                    // 予約ユーザー基本データ更新
                    blnRet = updateRsvUserBasic( connection, prestate, 2, frm.getUserId() );
                    Logging.info( "reserveNo:" + reserveNo + ",updateRsvUserBasic:" + blnRet );
                }

                if ( blnRet )
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
                isResult = true;
            }
            catch ( Exception e )
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                throw e;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.dataUpdate] Exception=" + e.toString(), "LogicOwnerRsvCheckIn.execRaiten" );
            throw e;
        }
        finally
        {
            // ロックの解除
            for( int i = 0 ; i < lockId.size() ; i++ )
            {
                LockReserve.UnLock( lockId.get( i ), lockReserveDate.get( i ), lockSeq.get( i ) );
            }

            DBConnection.releaseResources( result, prestate, connection );
        }
        return isResult;
    }

    /**
     * 
     * 予約者来店処理
     * 
     * @param hotelId ホテルID
     * @param reserveNo 予約番号
     * @param reserveDate 予約日
     * @param seq 部屋番号
     * @return true:正常、false:異常
     * @throws Exception
     */
    public boolean execRaitenSub(Connection connection, int hotelId, String reserveNo, int reserveDate, int seq, String Schema) throws Exception
    {
        boolean blnRet = false;
        int subNo = 0;
        int newSeq = 0;

        PreparedStatement prestate = null;

        try
        {
            // 新しい部屋番号取得
            newSeq = frm.getSeq();

            // 予約番号枝番取得
            getRsvSubNo( Schema );

            // 取得した予約番号枝番に1を加算する
            subNo = frm.getRsvSubNo() + 1;
            frm.setRsvSubNo( subNo );

            try
            {

                // 予約データを利用済み、来店フラグをONにする
                blnRet = updRaitenRsvData( connection, prestate, frm, Schema );

                Logging.info( "reserveNo:" + reserveNo + ",updRaitenRsvData:" + blnRet );

                // 実績データの登録
                if ( blnRet )
                {
                    blnRet = registRsvResult( connection, ReserveCommon.RESULT_KIND_ZUMI, frm, newSeq );
                    Logging.info( "reserveNo:" + reserveNo + ",registRsvResult:" + blnRet );
                }

                // 部屋残数データの更新
                if ( blnRet )
                {
                    // 画面で選択された部屋番号でチェックイン
                    blnRet = createRoomRemaindarRaiten( connection, reserveNo, frm.getRsvDate(), newSeq, Schema );
                    Logging.info( "reserveNo:" + reserveNo + ",createRoomRemaindarRaiten:" + blnRet );

                    if ( newSeq != frm.getSeq() )
                    {
                        // 予約時と来店確認時の部屋番号が違う
                        if ( checkResult( hotelId, frm.getOrgRsvDate(), newSeq, reserveNo ) )
                        {
                            // 部屋残数情報の存在チェック
                            if ( isExistsRoomReminder( hotelId, frm.getOrgRsvDate(), frm.getSeq(), reserveNo, Schema ) == true )
                            {
                                // 予約時に登録した部屋番号の部屋残数をクリアする。
                                blnRet = createRoomRemaindar( connection, "", ReserveCommon.ROOM_STATUS_EMPTY, frm.getOrgRsvDate(), Schema );
                                Logging.info( "reserveNo:" + reserveNo + ",createRoomRemaindar:" + blnRet );
                            }
                        }
                    }
                }

                // 予約履歴の作成
                if ( blnRet )
                {
                    blnRet = createRsvHistory( connection, ReserveCommon.UPDKBN_UPDATE, reserveNo, Schema );
                    Logging.info( "reserveNo:" + reserveNo + ",createRsvHistory:" + blnRet );
                }

                if ( blnRet )
                {
                    // 予約・オプション履歴データ作成
                    blnRet = createRsvOptionHistory( connection, prestate, reserveNo, Schema );
                    Logging.info( "reserveNo:" + reserveNo + ",createRsvOptionHistory:" + blnRet );
                }

            }
            catch ( Exception e )
            {

                throw e;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.dataUpdate] Exception=" + e.toString(), "LogicOwnerRsvCheckIn.execRaiten" );
            throw e;
        }

        return blnRet;
    }

    /**
     * 実績データ確認
     * 
     * 利用済みデータがあるか
     * 
     * @param id
     * @param rsvDate
     * @param oldSeq
     * @param rsvNo
     * @return
     * @throws Exception
     */
    private boolean checkResult(int id, int rsvDate, int oldSeq, String rsvNo) throws Exception
    {
        boolean blnRet;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String rsRsvNo = "";
        blnRet = false;

        try
        {
            query = "SELECT reserve_no FROM hh_rsv_result " +
                    " WHERE id = ? and ci_date = ? and seq = ? and use_kind = 1";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, rsvDate );
            prestate.setInt( 3, oldSeq );

            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.next() != false )
                {
                    rsRsvNo = result.getString( "reserve_no" );
                }
                else
                {
                    rsRsvNo = "";
                }
            }
            else
            {
                // not Found
                blnRet = true;
            }
            if ( rsRsvNo.equals( "" ) )
            {
                // 存在しない場合はOKとする
                blnRet = true;
            }
            else if ( rsRsvNo.equals( rsvNo ) )
            {
                // 予約と同じ
                blnRet = true;
            }
            else
            {
                // 別の予約番号の場合
                blnRet = false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.checkResult] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return blnRet;
    }

    /**
     * 
     * 予約登録処理
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    public void insReserve(String userAgent, String Schema) throws Exception
    {
        boolean blnRet = false;
        String query;
        String newRsvNo = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ReserveCommon rsvCmm = new ReserveCommon();
        int offerKind = 0;

        try
        {
            // プランの提供区分取得
            DataRsvPlan data = new DataRsvPlan();
            data.getData( frm.getSelHotelId(), frm.getSelPlanId() );
            offerKind = data.getOfferKind();

            // 予約番号取得
            newRsvNo = NumberingRsvNo.getRsvNo( frm.getSelHotelId() );

            // 部屋番号取得
            if ( offerKind == ReserveCommon.OFFER_KIND_PLAN )
            {
                // プランで提供の場合、部屋番号取得
                frm = getSeq( frm );
            }

            // ポイント情報取得
            frm = getPoingData( frm );

            try
            {

                // ロックの設定
                if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                {
                    // 他の人がロックしていた場合
                    frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                    return;
                }

                // データチェック
                frm = rsvCmm.chkDspMaster( frm );
                if ( frm.getErrMsg().trim().length() != 0 )
                {
                    // ロックの解除
                    LockReserve.UnLock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );
                    return;
                }

                connection = DBConnection.getConnection( false );
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // 予約データ
                frm.setRsvSubNo( 0 );
                blnRet = createReserve( connection, prestate, newRsvNo, userAgent );

                // 予約・オプションデータ
                if ( blnRet )
                {
                    blnRet = createRsvOptionData( connection, prestate, newRsvNo, frm.getWorkId() );
                    Logging.info( "[LogicRsvCheckIn.createRsvOptionData]:" + blnRet );
                }

                if ( blnRet )
                {
                    // 部屋残数データ
                    blnRet = createRoomRemaindarUndo( connection, newRsvNo, frm.getRsvDate(), frm.getSeq(), Schema );
                    Logging.info( "[LogicRsvCheckIn.createRoomRemaindar]:" + blnRet );
                }

                if ( blnRet )
                {
                    // 予約履歴データ作成
                    blnRet = createRsvHistory( connection, ReserveCommon.UPDKBN_INSERT, newRsvNo, Schema );
                    Logging.info( "[LogicRsvCheckIn.createRsvHistory]:" + blnRet );
                }

                if ( blnRet )
                {
                    // 予約・オプション履歴データ作成
                    blnRet = createRsvOptionHistory( connection, prestate, newRsvNo, Schema );
                    Logging.info( "[LogicRsvCheckIn.createRsvOptionHistory]:" + blnRet );
                }

                if ( blnRet )
                {
                    // 予約ユーザー基本データ作成
                    // ユーザーデータがあるか
                    blnRet = existsRsvUserBasic( frm.getLoginUserId() );
                    if ( blnRet == true )
                    {
                        // 存在する場合、予約ユーザー基本データ更新
                        blnRet = updateRsvUserBasic( connection, prestate, 1, frm.getLoginUserId() );
                    }
                    else
                    {
                        // 存在しない場合、予約ユーザー基本データ作成
                        blnRet = createRsvUserBasic( connection, prestate, frm.getLoginUserId(), frm.getRsvDate() );
                    }
                    Logging.info( "[LogicRsvCheckIn.createRsvUserBasic]:" + blnRet );
                }

                if ( blnRet )
                {
                    // メール送信依頼データ作成
                    sendMail( connection, newRsvNo, frm.getReminder(), ReserveCommon.TERM_KIND_PC, ReserveCommon.MAIL_NEW, Schema );

                    query = "COMMIT ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                }
                else
                {
                    query = "ROLLBACK";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    frm.setErrMsg( Message.getMessage( "erro.30002", "予約情報の登録" ) );
                }

                // ロックの解除
                LockReserve.UnLock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );

                frm.setRsvNo( newRsvNo );

            }
            catch ( Exception e )
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                throw e;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicRsvCheckIn.dataInsert] Exception=" + e.toString() );
            LockReserve.UnLock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 部屋指定なしの場合、部屋番号取得
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @return FormReserveSheetPCオブジェクト
     */
    private FormReserveSheetPC getSeq(FormReserveSheetPC frm) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = query + "SELECT rrpr.seq ";
        query = query + "FROM hh_rsv_room_remainder rrr ";
        query = query + "  INNER JOIN hh_rsv_rel_plan_room rrpr ON ( rrpr.id = rrr.id  AND rrpr.seq = rrr.seq ) ";
        // query = query + "  INNER JOIN hh_rsv_room rr ON ( rr.id = rrr.id AND rr.seq = rrr.seq ) ";
        query = query + " WHERE rrpr.id = ? ";
        query = query + "    AND rrpr.plan_id = ? ";
        query = query + "    AND rrr.cal_date = ? ";
        query = query + "    AND rrr.status = 1 ";
        // query = query + "    AND rr.sales_flag = 1 ";
        query = query + " order by rrpr.seq LIMIT 1 ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelPlanId() );
            prestate.setInt( 3, frm.getRsvDate() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frm.setSeq( result.getInt( "seq" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.getSeq] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }

        return(frm);
    }

    /**
     * プラン情報からポイント情報取得
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @return FormReserveSheetPCオブジェクト
     */
    private FormReserveSheetPC getPoingData(FormReserveSheetPC frm)
    {
        DataRsvPlan dataPlan = new DataRsvPlan();

        dataPlan.getData( frm.getSelHotelId(), frm.getSelPlanId() );

        // ハピー付与区分
        frm.setGivingPointKind( dataPlan.getGivingPointKind() );

        // ハピー付与ポイント
        frm.setGivingPoint( dataPlan.getGivingPoint() );

        return(frm);
    }

    /**
     * 予約データ作成
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param newRsvNo 予約番号
     * @return true:処理成功、false:処理失敗
     */
    private boolean createReserve(Connection connection, PreparedStatement prestate, String newRsvNo, String userAgent) throws Exception
    {

        boolean ret = false;
        String query = "";
        int retCnt = 0;
        String mail1 = "";
        String mail2 = "";

        if ( frm.getMailList().size() > 0 )
        {
            mail1 = frm.getMailList().get( 0 );
        }
        if ( frm.getMailList().size() > 1 )
        {
            mail2 = frm.getMailList().get( 1 );
        }

        query = query + "INSERT INTO hh_rsv_reserve ( ";
        query = query + "id, ";
        query = query + "reserve_no, ";
        query = query + "reserve_sub_no, ";
        query = query + "plan_id, ";
        query = query + "user_id, ";
        query = query + "reserve_date, ";
        query = query + "seq, ";
        query = query + "est_time_arrival, ";
        query = query + "num_adult, ";
        query = query + "num_child, ";
        query = query + "name_last, ";
        query = query + "name_first, ";
        query = query + "name_last_kana, ";
        query = query + "name_first_kana, ";
        query = query + "zip_code, ";
        query = query + "pref_code, ";
        query = query + "jis_code, ";
        query = query + "address1, ";
        query = query + "address2, ";
        query = query + "address3, ";
        query = query + "tel1, ";
        query = query + "tel2, ";
        query = query + "reminder_flag, ";
        query = query + "mail_addr, ";
        query = query + "demands, ";
        query = query + "remarks, ";
        query = query + "accept_date, ";
        query = query + "accept_time, ";
        query = query + "status, ";
        query = query + "basic_charge_total, ";
        query = query + "option_charge_total, ";
        query = query + "charge_total, ";
        query = query + "add_point, ";
        query = query + "coming_flag, ";
        query = query + "hotel_name, ";
        query = query + "noshow_flag, ";
        query = query + "parking, ";
        query = query + "parking_count, ";
        query = query + "ci_time_from, ";
        query = query + "ci_time_to, ";
        query = query + "co_time, ";
        query = query + "temp_coming_flag, ";
        query = query + "parking_high_roof_count, ";
        query = query + "num_man, ";
        query = query + "num_woman, ";
        query = query + "co_kind, ";
        query = query + "mail_addr1, ";
        query = query + "mail_addr2, ";
        query = query + "user_agent ";
        query = query + ") ";
        query = query + "SELECT  ";
        query = query + "id, ";
        query = query + "?, ";
        query = query + "?, ";
        query = query + "plan_id, ";
        query = query + "user_id, ";
        query = query + "reserve_date, ";
        query = query + "?, ";
        query = query + "est_time_arrival, ";
        query = query + "num_adult, ";
        query = query + "num_child, ";
        query = query + "name_last, ";
        query = query + "name_first, ";
        query = query + "name_last_kana, ";
        query = query + "name_first_kana, ";
        query = query + "zip_code, ";
        query = query + "pref_code, ";
        query = query + "jis_code, ";
        query = query + "address1, ";
        query = query + "address2, ";
        query = query + "address3, ";
        query = query + "tel1, ";
        query = query + "tel2, ";
        query = query + "reminder_flag, ";
        query = query + "mail_addr, ";
        query = query + "demands, ";
        query = query + "remarks, ";
        query = query + "accept_date, ";
        query = query + "accept_time, ";
        query = query + "status, ";
        query = query + "basic_charge_total, ";
        query = query + "option_charge_total, ";
        query = query + "charge_total, ";
        query = query + "add_point, ";
        query = query + "coming_flag, ";
        query = query + "hotel_name, ";
        query = query + "noshow_flag, ";
        query = query + "parking, ";
        query = query + "parking_count, ";
        query = query + "ci_time_from, ";
        query = query + "ci_time_to, ";
        query = query + "co_time, ";
        query = query + "temp_coming_flag, ";
        query = query + "parking_high_roof_count, ";
        query = query + "num_man, ";
        query = query + "num_woman, ";
        query = query + "co_kind, ";
        query = query + "?, ";
        query = query + "?, ";
        query = query + "? ";
        query = query + "FROM hh_rsv_reserve_work ";
        query = query + "WHERE work_id = ? ";

        try
        {

            prestate = connection.prepareStatement( query );
            prestate.setString( 1, newRsvNo );
            prestate.setInt( 2, frm.getRsvSubNo() );
            prestate.setInt( 3, frm.getSeq() );
            prestate.setString( 4, mail1 );
            prestate.setString( 5, mail2 );
            prestate.setString( 6, userAgent );
            prestate.setInt( 7, frm.getWorkId() );
            retCnt = prestate.executeUpdate();
            // Logging.info( query );
            if ( retCnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.createReserve] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }

    /**
     * 予約・オプションデータ作成
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param newRsvNo 予約番号
     * @param workParentId ワークID
     * @return true:処理成功、false:処理失敗
     */
    private boolean createRsvOptionData(Connection connection, PreparedStatement prestate, String newRsvNo, int workParentId) throws Exception
    {

        boolean ret = true;
        String query = "";

        query = query + "INSERT INTO hh_rsv_rel_reserve_option ( ";
        query = query + "  id ";
        query = query + " ,reserve_no ";
        query = query + " ,option_id ";
        query = query + " ,option_sub_id ";
        query = query + " ,quantity ";
        query = query + " ,unit_price ";
        query = query + " ,charge_total ";
        query = query + " ,remarks ";
        query = query + ") ";
        query = query + "SELECT  ";
        query = query + "  id ";
        query = query + " ,? ";
        query = query + " ,option_id ";
        query = query + " ,option_sub_id ";
        query = query + " ,if( quantity = -1, 0, quantity) ";
        query = query + " ,unit_price ";
        query = query + " ,charge_total ";
        query = query + " ,remarks ";
        query = query + "FROM hh_rsv_rel_reserve_option_work ";
        query = query + "WHERE work_parent_id = ? ";
        query = query + "  AND quantity <> 0 ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, newRsvNo );
            prestate.setInt( 2, workParentId );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.createRsvOptionData] Exception=" + e.toString() );
            ret = false;
            throw e;
        }

        return(ret);
    }

    /**
     * 予約・オプション履歴データ作成
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param newRsvNo 予約番号
     * @return true:処理成功、false:処理失敗
     */
    public boolean createRsvOptionHistory(Connection connection, PreparedStatement prestate, String newRsvNo, String Schema) throws Exception
    {

        boolean ret = true;
        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {

            String query = "";

            query = query + "INSERT INTO " + ReserveCommon.SCHEMA_NEWRSV + ".hh_rsv_rel_reserve_option_history ( ";
            query = query + "  id ";
            query = query + " ,reserve_no ";
            query = query + " ,reserve_sub_no ";
            query = query + " ,option_id ";
            query = query + " ,option_sub_id ";
            query = query + " ,number ";
            query = query + " ,quantity ";
            query = query + " ,unit_price ";
            query = query + " ,charge_total ";
            query = query + " ,remarks ";
            query = query + ") ";
            query = query + "SELECT  ";
            query = query + "  id ";
            query = query + " ,reserve_no ";
            query = query + " ,? ";
            query = query + " ,option_id ";
            query = query + " ,option_sub_id ";
            query = query + " ,number ";
            query = query + " ,quantity ";
            query = query + " ,unit_price ";
            query = query + " ,charge_total ";
            query = query + " ,remarks ";
            query = query + "FROM  " + ReserveCommon.SCHEMA_NEWRSV + ".hh_rsv_rel_reserve_option ";
            query = query + "WHERE id = ? ";
            query = query + "  AND reserve_no = ? ";

            try
            {
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, frm.getRsvSubNo() );
                prestate.setInt( 2, frm.getSelHotelId() );
                prestate.setString( 3, newRsvNo );
                prestate.executeUpdate();

            }
            catch ( Exception e )
            {
                Logging.error( "[LogicOwnerRsvCheckIn.createRsvOptionHistory] Exception=" + e.toString() );
                ret = false;
                throw e;
            }
        }
        else
        {
            ret = createRsvOptionHistory( connection, prestate, newRsvNo );
        }
        return(ret);
    }

    /**
     * 予約・オプション履歴データ作成
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param newRsvNo 予約番号
     * @return true:処理成功、false:処理失敗
     */
    public boolean createRsvOptionHistory(Connection connection, PreparedStatement prestate, String newRsvNo) throws Exception
    {

        boolean ret = true;
        String query = "";

        query = query + "INSERT INTO hh_rsv_rel_reserve_option_history ( ";
        query = query + "  id ";
        query = query + " ,reserve_no ";
        query = query + " ,reserve_sub_no ";
        query = query + " ,option_id ";
        query = query + " ,option_sub_id ";
        query = query + " ,quantity ";
        query = query + " ,unit_price ";
        query = query + " ,charge_total ";
        query = query + " ,remarks ";
        query = query + ") ";
        query = query + "SELECT  ";
        query = query + "  id ";
        query = query + " ,reserve_no ";
        query = query + " ,? ";
        query = query + " ,option_id ";
        query = query + " ,option_sub_id ";
        query = query + " ,quantity ";
        query = query + " ,unit_price ";
        query = query + " ,charge_total ";
        query = query + " ,remarks ";
        query = query + "FROM hh_rsv_rel_reserve_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND reserve_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getRsvSubNo() );
            prestate.setInt( 2, frm.getSelHotelId() );
            prestate.setString( 3, newRsvNo );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.createRsvOptionHistory] Exception=" + e.toString() );
            ret = false;
            throw e;
        }

        return(ret);
    }

    public boolean releaseRoomRemaindar(int selHotelId, String roomNo, String newRsvNo, int rsvDate) throws Exception
    {
        frm = new FormReserveSheetPC();
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;
        frm.setSelHotelId( selHotelId );
        DataHotelRoomMore room = new DataHotelRoomMore();

        if ( room.getData( selHotelId, roomNo ) != false )
        {
            frm.setSeq( room.getSeq() );
        }

        try
        {
            connection = DBConnection.getConnection( false );
            prestate = connection.prepareStatement( "START TRANSACTION " );
            result = prestate.executeQuery();
            ret = createRoomRemaindar( connection, newRsvNo, ReserveCommon.ROOM_STATUS_EMPTY, rsvDate, ReserveCommon.SCHEMA_NEWRSV );
            if ( ret )
            {
                prestate = connection.prepareStatement( "COMMIT" );
                result = prestate.executeQuery();
            }
            else
            {
                prestate = connection.prepareStatement( "ROLLBACK" );
                result = prestate.executeQuery();
                frm.setErrMsg( Message.getMessage( "erro.30002", "予約情報の登録" ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[releaseRoomRemaindar] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * 部屋残数データ作成
     * 
     * @param connection Connectionオブジェクト
     * @param newRsvNo 予約番号
     * @param status ステータス
     * @param rsvDate 対象の予約日
     * @return true:処理成功、false:処理失敗
     */
    private boolean createRoomRemaindar(Connection connection, String newRsvNo, int status, int rsvDate, String Schema)
    {
        boolean ret = false;
        int planType = ReserveCommon.getPlanType( connection, frm.getSelHotelId(), newRsvNo );
        ret = ReserveCommon.updateRemainder( connection, frm.getSelHotelId(), rsvDate, frm.getSeq(), status == ReserveCommon.ROOM_STATUS_EMPTY ? "" : newRsvNo, status, planType );
        return(ret);
    }

    /**
     * 部屋残数データ作成
     * 
     * 来店確認時に、部屋番号が変わった場合、新しい部屋番号でデータを更新する。
     * 
     * @param connection Connectionオブジェクト
     * @param newRsvNo 予約番号
     * @param status ステータス
     * @param rsvDate 対象の予約日
     * @param seq 対象の部屋番号
     * @return true:処理成功、false:処理失敗
     */
    public boolean createRoomRemaindarRaiten(Connection connection, String newRsvNo, int rsvDate, int seq, String Schema)
    {
        boolean ret = false;
        int planType = ReserveCommon.getPlanType( connection, frm.getSelHotelId(), newRsvNo );
        ret = ReserveCommon.updateRemainder( connection, frm.getSelHotelId(), rsvDate, frm.getSeq(), newRsvNo, ReserveCommon.ROOM_STATUS_RSV, planType );
        return(ret);
    }

    public boolean createRoomRemaindarRaiten(Connection connection, String newRsvNo, int rsvDate, int seq)
    {
        boolean ret = false;
        int planType = ReserveCommon.getPlanType( connection, frm.getSelHotelId(), newRsvNo );
        ret = ReserveCommon.updateRemainder( connection, frm.getSelHotelId(), rsvDate, frm.getSeq(), newRsvNo, ReserveCommon.ROOM_STATUS_RSV, planType );
        return(ret);
    }

    public boolean createRoomRemaindarUndo(Connection connection, String newRsvNo, int rsvDate, int seq, String Schema)
    {
        boolean ret = false;
        int planType = ReserveCommon.getPlanType( connection, frm.getSelHotelId(), newRsvNo );
        ret = ReserveCommon.updateRemainder( connection, frm.getSelHotelId(), rsvDate, frm.getSeq(), newRsvNo, ReserveCommon.ROOM_STATUS_EMPTY, planType );
        return(ret);
    }

    public boolean createRoomRemaindarUndo(Connection connection, String newRsvNo, int rsvDate, int seq)
    {
        boolean ret = false;
        int planType = ReserveCommon.getPlanType( connection, frm.getSelHotelId(), newRsvNo );
        ret = ReserveCommon.updateRemainder( connection, frm.getSelHotelId(), rsvDate, frm.getSeq(), newRsvNo, ReserveCommon.ROOM_STATUS_EMPTY, planType );
        return(ret);
    }

    /**
     * 予約履歴データ作成
     * 
     * @param connection Connectionオブジェクト
     * @param updKind
     * @param rsvNo 予約番号
     * @return true:処理成功、false:処理失敗
     */
    public boolean createRsvHistory(Connection connection, int updKind, String rsvNo, String Schema)
    {

        boolean ret = false;
        DataRsvReserve ddr = new DataRsvReserve();
        DataRsvReserveHistory drrh = new DataRsvReserveHistory();

        // 予約データ取得
        ddr.getData( connection, frm.getSelHotelId(), rsvNo );

        // 予約履歴データ
        drrh = new DataRsvReserveHistory();
        drrh.setId( ddr.getID() );
        drrh.setReserveNo( rsvNo );
        drrh.setReserveSubNo( ddr.getReserveNoSub() );
        drrh.setUpdateKind( updKind );
        drrh.setPlanId( ddr.getPlanId() );
        drrh.setPlanSubId( ddr.getPlanSubId() );
        drrh.steUserId( ddr.getUserId() );
        drrh.setReserveDate( ddr.getReserveDate() );
        drrh.setSeq( ddr.getSeq() );
        drrh.setEstTimeArrival( ddr.getEstTimeArrival() );
        drrh.setNumAdult( ddr.getNumAdult() );
        drrh.setNumChild( ddr.getNumChild() );
        drrh.setNameLast( ddr.getNameLast() );
        drrh.setNameFirst( ddr.getNameFirst() );
        drrh.setNameLastKana( ddr.getNameLastKana() );
        drrh.setNameFirstKana( ddr.getNameFirstKana() );
        drrh.setZipCd( ddr.getZipCd() );
        drrh.setPrefCode( ddr.getPrefCode() );
        drrh.setJisCode( ddr.getJisCode() );
        drrh.setAddress1( ddr.getAddress1() );
        drrh.setAddress2( ddr.getAddress2() );
        drrh.setAddress3( ddr.getAddress3() );
        drrh.setTel1( ddr.getTel1() );
        drrh.setTel2( ddr.getTel2() );
        drrh.setRemaindFlag( ddr.getReminderFlag() );
        drrh.setMailAddr( ddr.getMailAddr() );
        drrh.setDemands( ddr.getDemands() );
        drrh.setRemarks( ddr.getRemarks() );
        drrh.setAcceptDate( ddr.getAcceptDate() );
        drrh.setAcceptTime( ddr.getAcceptTime() );
        drrh.setStatus( ddr.getStatus() );
        drrh.setBasicChargeTotal( ddr.getBasicChargeTotal() );
        drrh.setOptionChargeTotal( ddr.getOptionChargeTotal() );
        drrh.setChargeTotal( ddr.getChargeTotal() );
        drrh.setAddPoint( ddr.getAddPoint() );
        drrh.setComingFlag( ddr.getComingFlag() );
        drrh.setHotelName( ddr.getHotelName() );
        drrh.setNowShowFlag( ddr.getNowShowFlag() );
        drrh.setParking( ddr.getParking() );
        drrh.setParkingCount( ddr.getParkingCount() );
        drrh.setHiRoofCount( ddr.getParkingHiRoofCount() );
        drrh.setCiTimeFrom( ddr.getCiTimeFrom() );
        drrh.setCiTimeTo( ddr.getCiTimeTo() );
        drrh.setCoTime( ddr.getCoTime() );
        drrh.setTempComingFlag( ddr.getTempComingFlag() );
        drrh.setNumMan( ddr.getNumMan() );
        drrh.setNumWoman( ddr.getNumWoman() );
        drrh.setCoKind( ddr.getCoKind() );
        drrh.setCancelKind( ddr.getCancelKind() );
        drrh.setMailAddr1( ddr.getMailAddr1() );
        drrh.setMailAddr2( ddr.getMailAddr2() );
        drrh.setPayment( ddr.getPayment() );
        drrh.setPaymentStatus( ddr.getPaymentStatus() );
        drrh.setConsumerDemands( ddr.getConsumerDemands() );
        drrh.setAddBonusMile( ddr.getAddBonusMile() );
        drrh.setUsedMile( ddr.getUsedMile() );
        drrh.setReserveNoMain( ddr.getReserveNoMain() );
        drrh.setReserveDateTo( ddr.getReserveDateTo() );
        drrh.setExtFlag( ddr.getExtFlag() );
        drrh.setCancelCharge( ddr.getCancelCharge() );
        drrh.setBasicChargeTotalAll( ddr.getBasicChargeTotalAll() );
        drrh.setOptionChargeTotalAll( ddr.getOptionChargeTotalAll() );
        drrh.setChargeTotalAll( ddr.getChargeTotalAll() );
        drrh.setCancelDate( ddr.getCancelDate() );
        drrh.setCancelCreditStatus( ddr.getCancelCreditStatus() );

        ret = drrh.insertData( connection, Schema );

        return(ret);
    }

    /**
     * 予約履歴データ作成
     * 
     * @param connection Connectionオブジェクト
     * @param updKind
     * @param rsvNo 予約番号
     * @return true:処理成功、false:処理失敗
     */
    public boolean createRsvHistory(Connection connection, int updKind, String rsvNo)
    {

        boolean ret = false;
        DataRsvReserve ddr = new DataRsvReserve();
        DataRsvReserveHistory drrh = new DataRsvReserveHistory();

        // 予約データ取得
        ddr.getData( connection, frm.getSelHotelId(), rsvNo );

        // 予約履歴データ
        drrh = new DataRsvReserveHistory();
        drrh.setId( ddr.getID() );
        drrh.setReserveNo( rsvNo );
        drrh.setReserveSubNo( ddr.getReserveNoSub() );
        drrh.setUpdateKind( updKind );
        drrh.setPlanId( ddr.getPlanId() );
        drrh.setPlanSubId( ddr.getPlanSubId() );
        drrh.steUserId( ddr.getUserId() );
        drrh.setReserveDate( ddr.getReserveDate() );
        drrh.setSeq( ddr.getSeq() );
        drrh.setEstTimeArrival( ddr.getEstTimeArrival() );
        drrh.setNumAdult( ddr.getNumAdult() );
        drrh.setNumChild( ddr.getNumChild() );
        drrh.setNameLast( ddr.getNameLast() );
        drrh.setNameFirst( ddr.getNameFirst() );
        drrh.setNameLastKana( ddr.getNameLastKana() );
        drrh.setNameFirstKana( ddr.getNameFirstKana() );
        drrh.setZipCd( ddr.getZipCd() );
        drrh.setPrefCode( ddr.getPrefCode() );
        drrh.setJisCode( ddr.getJisCode() );
        drrh.setAddress1( ddr.getAddress1() );
        drrh.setAddress2( ddr.getAddress2() );
        drrh.setAddress3( ddr.getAddress3() );
        drrh.setTel1( ddr.getTel1() );
        drrh.setTel2( ddr.getTel2() );
        drrh.setRemaindFlag( ddr.getReminderFlag() );
        drrh.setMailAddr( ddr.getMailAddr() );
        drrh.setDemands( ddr.getDemands() );
        drrh.setRemarks( ddr.getRemarks() );
        drrh.setAcceptDate( ddr.getAcceptDate() );
        drrh.setAcceptTime( ddr.getAcceptTime() );
        drrh.setStatus( ddr.getStatus() );
        drrh.setBasicChargeTotal( ddr.getBasicChargeTotal() );
        drrh.setOptionChargeTotal( ddr.getOptionChargeTotal() );
        drrh.setChargeTotal( ddr.getChargeTotal() );
        drrh.setAddPoint( ddr.getAddPoint() );
        drrh.setComingFlag( ddr.getComingFlag() );
        drrh.setHotelName( ddr.getHotelName() );
        drrh.setNowShowFlag( ddr.getNowShowFlag() );
        drrh.setParking( ddr.getParking() );
        drrh.setParkingCount( ddr.getParkingCount() );
        drrh.setHiRoofCount( ddr.getParkingHiRoofCount() );
        drrh.setCiTimeFrom( ddr.getCiTimeFrom() );
        drrh.setCiTimeTo( ddr.getCiTimeTo() );
        drrh.setCoTime( ddr.getCoTime() );
        drrh.setTempComingFlag( ddr.getTempComingFlag() );
        drrh.setNumMan( ddr.getNumMan() );
        drrh.setNumWoman( ddr.getNumWoman() );
        drrh.setCoKind( ddr.getCoKind() );
        drrh.setCancelKind( ddr.getCancelKind() );
        drrh.setMailAddr1( ddr.getMailAddr1() );
        drrh.setMailAddr2( ddr.getMailAddr2() );
        drrh.setPayment( ddr.getPayment() );
        drrh.setPaymentStatus( ddr.getPaymentStatus() );
        drrh.setConsumerDemands( ddr.getConsumerDemands() );
        drrh.setAddBonusMile( ddr.getAddBonusMile() );
        drrh.setUsedMile( ddr.getUsedMile() );
        ret = drrh.insertData( connection );

        return(ret);
    }

    /**
     * メール送信依頼データ作成
     * 
     * @param connection Connection
     * @param newRsvNo 予約番号
     * @param remainderFlg リマインダーフラグ
     * @param regTermKind 登録端末区分
     * @return なし
     */
    public void sendMail(Connection connection, String newRsvNo, int remainderFlg, int regTermKind, int mailKind, String Schema)
    {
        String language = "";
        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {
            // ▼同じ予約番号のデータから言語を取得する
            PreparedStatement prestate = null;
            ResultSet result = null;
            String query = "SELECT * FROM newRsvDB.hh_rsv_mail_request WHERE id = ? AND reserve_no = ? ";
            try
            {
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, frm.getSelHotelId() );
                prestate.setString( 2, newRsvNo );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        language = result.getString( "language" );
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[LogicOwnerRsvCheckIn.updSendMail] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( prestate );
                DBConnection.releaseResources( result );
            }
        }

        DataRsvMailRequest data = new DataRsvMailRequest();
        int hotelmailkind = 0;

        data.setId( frm.getSelHotelId() );
        data.setReserveNo( newRsvNo );
        data.setReserveSubNo( frm.getRsvSubNo() );
        data.setRequestMailKind( mailKind );
        data.setRequestFlag( 0 );
        data.setRegistTermKind( frm.getTermKind() );
        data.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        data.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        data.setLanguage( language );

        data.insertData( connection, Schema );

        if ( mailKind == ReserveCommon.MAIL_NEW )
        {
            hotelmailkind = ReserveCommon.MAIL_REQ_HOTEL_NewRSV;
        }
        else if ( mailKind == ReserveCommon.MAIL_UPD )
        {
            hotelmailkind = ReserveCommon.MAIL_REQ_HOTEL_UpdateRSV;
        }
        else if ( mailKind == ReserveCommon.MAIL_DEL )
        {
            hotelmailkind = ReserveCommon.MAIL_REQ_HOTEL_CANCELRSV;
        }

        if ( hotelmailkind > 0 )
        {
            // ホテル向け
            data.setRequestMailKind( hotelmailkind ); // 依頼メール区分
            data.insertData( connection, Schema );
        }
        // リマインダが"ON"の場合、さらに１件追加する
        if ( remainderFlg == 1 )
        {
            data.setRequestMailKind( ReserveCommon.MAIL_REQ_REMINDAR ); // 依頼メール区分
            data.insertData( connection, Schema );
        }
    }

    /**
     * メール送信依頼データ作成
     * 
     * @param connection Connection
     * @param newRsvNo 予約番号
     * @param remainderFlg リマインダーフラグ
     * @param regTermKind 登録端末区分
     * @return なし
     */
    public void sendMail(Connection connection, String newRsvNo, int remainderFlg, int regTermKind, int mailKind)
    {
        DataRsvMailRequest data = new DataRsvMailRequest();
        int hotelmailkind = 0;

        data.setId( frm.getSelHotelId() );
        data.setReserveNo( newRsvNo );
        data.setReserveSubNo( frm.getRsvSubNo() );
        data.setRequestMailKind( mailKind );
        data.setRequestFlag( 0 );
        data.setRegistTermKind( frm.getTermKind() );
        data.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        data.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

        data.insertData( connection );

        if ( mailKind == ReserveCommon.MAIL_NEW )
        {
            hotelmailkind = ReserveCommon.MAIL_REQ_HOTEL_NewRSV;
        }
        else if ( mailKind == ReserveCommon.MAIL_UPD )
        {
            hotelmailkind = ReserveCommon.MAIL_REQ_HOTEL_UpdateRSV;
        }
        else if ( mailKind == ReserveCommon.MAIL_DEL )
        {
            hotelmailkind = ReserveCommon.MAIL_REQ_HOTEL_CANCELRSV;
        }

        if ( hotelmailkind > 0 )
        {
            // ホテル向け
            data.setRequestMailKind( hotelmailkind ); // 依頼メール区分
            data.insertData( connection );
        }
        // リマインダが"ON"の場合、さらに１件追加する
        if ( remainderFlg == 1 )
        {
            data.setRequestMailKind( ReserveCommon.MAIL_REQ_REMINDAR ); // 依頼メール区分
            data.insertData( connection );
        }
    }

    /**
     * 予約更新処理（予約詳細(PC版)専用)
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    public void updReserve(String userAgent, String Schema) throws Exception
    {
        boolean blnRet = true;
        int subNo = 1;
        String query;
        String newRsvNo = "";
        String oldRsvNo = "";
        int roomSeq = 0;
        int planId = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            newRsvNo = frm.getRsvNo();
            oldRsvNo = frm.getRsvNo();

            roomSeq = frm.getSeq();
            planId = frm.getSelPlanId();

            // 更新前予約情報の取得
            getRsvSubNo( Schema );

            // 取得した予約番号枝番に1を加算する
            subNo = frm.getRsvSubNo() + 1;
            frm.setRsvSubNo( subNo );

            try
            {
                // 変更予定の内容でロック
                if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                {
                    // 他の人がロックしていた場合
                    frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                    return;
                }

                // 変更前と変更後の予約日、または部屋番号が違う場合は変更前をロック
                if ( (frm.getRsvDate() != frm.getOrgRsvDate()) || (frm.getSeq() != frm.getOrgRsvSeq()) )
                {
                    if ( LockReserve.Lock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq() ) == false )
                    {
                        // 他の人がロックしていた場合
                        frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                        return;
                    }
                }

                // データチェック
                frm = rsvCmm.chkDspMaster( frm );
                if ( frm.getErrMsg().trim().length() != 0 )
                {
                    // ロックの解除
                    LockReserve.UnLock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );
                    if ( (frm.getRsvDate() != frm.getOrgRsvDate()) || (frm.getSeq() != frm.getOrgRsvSeq()) )
                    {
                        // 変更前の内容でロック解除
                        LockReserve.UnLock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq() );
                    }
                    return;
                }

                connection = DBConnection.getConnection( false );
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // ▼未依頼のリマインダーメールレコードを無効にする。
                blnRet = setCancelMail( connection, prestate, frm.getSelHotelId(), frm.getRsvNo(), Schema );

                // ▼既存の予約データをキャンセル扱いにする
                if ( frm.getOrgRsvDate() != frm.getRsvDate() )
                {
                    // 日付が変わったら予約データのキャンセル
                    blnRet = cancelNowReserve( connection, prestate, frm.getRsvNo(), Schema );
                }

                if ( blnRet )
                {
                    // 予約番号取得
                    if ( frm.getOrgRsvDate() != frm.getRsvDate() )
                    {
                        // 日付が変わったら予約番号再取得
                        newRsvNo = NumberingRsvNo.getRsvNo( frm.getSelHotelId() );
                        frm.setRsvSubNo( 0 );
                    }

                    if ( (frm.getSeq() == 0) && (frm.getOrgRsvSeq() != frm.getSeq()) )
                    {
                        // 部屋指定されていない場合
                        frm = getSeq( frm );
                    }

                    // プランマスタを取得
                    frm = getPoingData( frm );
                }

                // ▼予約データ登録
                if ( blnRet )
                {
                    if ( frm.getOrgRsvDate() != frm.getRsvDate() )
                    {
                        // 日付が変わった場合は新規に作成
                        blnRet = createReserve( connection, prestate, newRsvNo, userAgent );
                    }
                    else
                    {
                        // 日付以外の項目が変更した場合は更新
                        blnRet = updRsvData( connection, prestate, userAgent );
                    }
                }

                if ( blnRet )
                {
                    // オプションデータ登録
                    deleteRsvOption( connection, prestate, frm.getSelHotelId(), newRsvNo );
                    blnRet = createRsvOptionData( connection, prestate, newRsvNo, frm.getWorkId() );
                }

                if ( blnRet )
                {
                    // 部屋残数データ作成
                    blnRet = createRoomRemaindarUndo( connection, newRsvNo, frm.getRsvDate(), frm.getSeq(), Schema );
                }

                if ( blnRet )
                {
                    // 予約履歴データ作成
                    blnRet = createRsvHistory( connection, ReserveCommon.UPDKBN_UPDATE, newRsvNo, Schema );
                }

                if ( blnRet )
                {
                    // 予約・オプション履歴データ作成
                    blnRet = createRsvOptionHistory( connection, prestate, newRsvNo );
                }

                if ( blnRet )
                {
                    // プラン売で部屋番号が違った場合の変更はメール通知は行なわない
                    if ( (oldRsvNo.equals( newRsvNo ) && planId == frm.getSelPlanId() && frm.getOfferKind() == 1 && roomSeq != frm.getSeq()) == false )
                    {
                        // メール送信依頼データ作成
                        sendMail( connection, newRsvNo, frm.getReminder(), ReserveCommon.TERM_KIND_PC, ReserveCommon.MAIL_UPD, Schema );
                    }
                }

                if ( blnRet )
                {
                    // 予約ユーザー基本データ更新
                    blnRet = updateRsvUserBasic( connection, prestate, 1, frm.getUserId() );
                }

                if ( blnRet )
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
                    frm.setErrMsg( Message.getMessage( "erro.30002", "予約情報の登録" ) );
                }

                // ロックの解除
                LockReserve.UnLock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );
                if ( (frm.getRsvDate() != frm.getOrgRsvDate()) || (frm.getSeq() != frm.getOrgRsvSeq()) )
                {
                    // 変更前の内容でロック解除
                    LockReserve.UnLock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq() );
                }

                frm.setRsvNo( newRsvNo );

            }
            catch ( Exception e )
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                throw e;
            }
        }
        catch ( Exception e )
        {
            LockReserve.UnLock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );
            if ( (frm.getRsvDate() != frm.getOrgRsvDate()) || (frm.getSeq() != frm.getOrgRsvSeq()) )
            {
                // 変更前の内容でロック解除
                LockReserve.UnLock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq() );
            }
            Logging.error( "[LogicOwnerRsvCheckIn.dataUpdate] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 現在の予約情報をキャンセルする
     * 
     * @param conn Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param rsvNo 予約番号
     * @return true:正常、false:異常
     */
    private boolean cancelNowReserve(Connection conn, PreparedStatement prestate, String rsvNo, String Schema) throws Exception
    {
        boolean ret = true;

        // 予約データのキャンセル
        ret = setReserveStatus( conn, prestate, frm, ReserveCommon.RSV_STATUS_CANCEL, Schema );
        if ( ret )
        {
            // 部屋残数データ更新
            ret = createRoomRemaindar( conn, rsvNo, ReserveCommon.ROOM_STATUS_EMPTY, frm.getOrgRsvDate(), Schema );
        }

        if ( ret )
        {
            // キャンセルの予約履歴データ作成
            ret = createRsvHistory( conn, ReserveCommon.UPDKBN_CANCEL, rsvNo, Schema );
        }

        if ( ret )
        {
            // メール送信依頼データ
            // 同じ予約番号で未依頼のレコードを無効に変更する
            ret = setCancelMail( conn, prestate, frm.getSelHotelId(), rsvNo, Schema );

            // 無効メール送信依頼作成
            sendMail( conn, rsvNo, 0, frm.getTermKind(), ReserveCommon.MAIL_DEL, Schema );
        }
        return(ret);
    }

    /**
     * 予約データ更新
     * 予約データを削除し、ワークテーブルに格納されているデータで登録する
     * 
     * @param conn Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @return true:正常、false:異常
     */
    private boolean updRsvData(Connection conn, PreparedStatement prestate, String userAgent) throws Exception
    {
        boolean ret = false;

        // 予約データ削除
        ret = deleteReserve( conn, prestate );

        if ( ret == false )
        {
            return(ret);
        }

        // 予約データ登録
        ret = createReserve( conn, prestate, frm.getRsvNo(), userAgent );

        return(ret);
    }

    /**
     * プラン・部屋データ削除
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @return true:正常、false:失敗
     */
    private boolean deleteReserve(Connection conn, PreparedStatement prestate) throws Exception
    {
        String query = "";
        int result;
        boolean ret = false;

        query = query + "DELETE FROM hh_rsv_reserve ";
        query = query + "WHERE id = ? ";
        query = query + "  AND reserve_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setString( 2, frm.getRsvNo() );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.deleteReserve] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 同じ予約番号で未依頼のレコードを無効に変更する
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param hotelId ホテルID
     * @param rsvNo 予約番号
     * @return true:正常、false:異常
     */
    private boolean setCancelMail(Connection connection, PreparedStatement prestate, int hotelId, String rsvNo, String Schema) throws Exception
    {
        String query = "";
        boolean ret = false;

        query = query + "UPDATE" + Schema + ".hh_rsv_mail_request SET ";
        query = query + "  request_flag = 2 ";
        query = query + " ,regist_term_kind = 1 ";
        query = query + " ,regist_date = ? ";
        query = query + " ,regist_time = ? ";
        query = query + " WHERE id = ? AND reserve_no = ? ";
        query = query + " AND request_mail_kind = 5 AND request_flag = 0 ";

        try
        {
            // DBConnection.releaseResources( prestate );

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 3, hotelId );
            prestate.setString( 4, rsvNo );
            prestate.executeUpdate();

            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.updRsvData] Exception=" + e.toString() );
            throw e;
        }
        return(ret);
    }

    /**
     * 予約データ更新
     * 
     * @param conn Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @param status ステータス
     * @return true:正常、false:異常
     */
    private boolean setReserveStatus(Connection conn, PreparedStatement prestate, FormReserveSheetPC frm, int status, String Schema) throws Exception
    {
        String query = "";
        int retCnt = 0;
        boolean ret = false;

        query = query + "UPDATE " + Schema + ".hh_rsv_reserve SET ";
        query = query + "  reserve_sub_no = ? ";
        query = query + " ,status = ? ";
        query = query + " ,accept_date = ? ";
        query = query + " ,accept_time = ? ";
        if ( frm.getNoShow() == ReserveCommon.NO_SHOW_ON )
        {
            query = query + " ,noshow_flag = 1 ";
        }
        else
        {
            query = query + " ,noshow_flag = 0 ";
        }
        if ( status == ReserveCommon.RSV_STATUS_CANCEL )
        {
            // 区分がオーナーだったら、ホテルキャンセル。ユーザであればユーザキャンセル
            if ( frm.getUserKbn().equals( ReserveCommon.USER_KBN_OWNER ) )
            {
                if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
                {
                    query += ", cancel_type = " + ReserveCommon.CANCEL_HOTEL;
                }
                else
                {
                    query += ", cancel_kind = " + ReserveCommon.CANCEL_HOTEL;
                }
            }
            else
            {
                if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
                {
                    query += ", cancel_type = " + ReserveCommon.CANCEL_USER;
                }
                else
                {
                    query += ", cancel_kind = " + ReserveCommon.CANCEL_USER;
                }
            }
        }
        query += " , cancel_date = ? ";
        query += " , cancel_credit_status = ? ";
        query = query + " WHERE id = ? AND reserve_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            int i = 1;
            prestate.setInt( i++, frm.getRsvSubNo() );
            prestate.setInt( i++, status );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            if ( status == ReserveCommon.RSV_STATUS_CANCEL )
            {
                prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                prestate.setInt( i++, 2 );
            }
            else
            {
                prestate.setInt( i++, 0 );
                prestate.setInt( i++, 0 );
            }
            prestate.setInt( i++, frm.getSelHotelId() );
            prestate.setString( i++, frm.getRsvNo() );

            retCnt = prestate.executeUpdate();

            if ( retCnt > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.setCancelReserve] Exception=" + e.toString() );
            throw e;
        }
        return(ret);
    }

    /**
     * 来店確認時の予約データ更新
     * 
     * @param conn Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param frm FormReserveSheetPCオブジェクト
     * @return true:正常、false:異常
     */
    private boolean updRaitenRsvData(Connection conn, PreparedStatement prestate, FormReserveSheetPC frm, String Schema) throws Exception
    {
        String query = "";
        int retCnt = 0;
        boolean ret = false;

        query = query + "UPDATE " + Schema + ".hh_rsv_reserve SET ";
        query = query + "  reserve_sub_no = ? ";
        query = query + " ,status = 2 ";
        query = query + " ,coming_flag = 1 ";
        query = query + " ,accept_date = ? ";
        query = query + " ,accept_time = ? ";
        query = query + " ,temp_coming_flag = 0 ";
        query = query + " WHERE id = ? AND reserve_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getRsvSubNo() );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 4, frm.getSelHotelId() );
            prestate.setString( 5, frm.getRsvNo() );

            retCnt = prestate.executeUpdate();

            if ( retCnt > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.updRaitenRsvData] Exception=" + e.toString() );
            throw e;
        }
        return(ret);
    }

    /**
     * 更新前予約情報取得
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @return frm FormReserveSheetPCオブジェクト
     */
    private void getRsvSubNo(String Schema) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = query + "SELECT reserve_sub_no, plan_id, reserve_date, seq, reminder_flag ";
        query = query + " FROM " + Schema + ".hh_rsv_reserve WHERE id = ? AND reserve_no = ?";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setString( 2, frm.getRsvNo() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frm.setRsvSubNo( result.getInt( "reserve_sub_no" ) );
                frm.setSeq( result.getInt( "seq" ) );
                frm.setOrgRsvDate( result.getInt( "reserve_date" ) );
                frm.setSelPlanId( result.getInt( "plan_id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.getRsvSubNo] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }
    }

    /**
     * 
     * 予約データの更新（予約キャンセル）
     * 
     * @param hotelId // ホテルID
     * @param reserveNo // 予約番号
     * @param reserveDate // 予約日
     * @param seq // 部屋番号
     * @param kind // 機種
     * @param noShow // noShowﾌﾗｸﾞ
     * @return
     * @throws Exception
     */
    public boolean execRsvCancel(int kind, String Schema) throws Exception
    {
        boolean blnRet = true;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ReserveCommon rsvCmm = new ReserveCommon();

        ArrayList<Integer> lockId = new ArrayList<Integer>();
        ArrayList<Integer> lockReserveDate = new ArrayList<Integer>();
        ArrayList<Integer> lockSeq = new ArrayList<Integer>();

        try
        {

            // 変更予定の内容でロック
            if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
            {
                // 他の人がロックしていた場合
                frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                blnRet = false;
                return(blnRet);
            }

            lockId.add( frm.getSelHotelId() );
            lockReserveDate.add( frm.getRsvDate() );
            lockSeq.add( frm.getSeq() );

            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            blnRet = execRsvCancelSub( connection, kind, Schema );

            // データチェック
            // frm = rsvCmm.chkDspMaster( frm );
            // if ( frm.getErrMsg().trim().length() != 0 )
            // {
            // // ロックの解除
            // LockReserve.UnLock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );
            // blnRet = false;
            // return(blnRet);
            // }

            if ( blnRet )
            {
                // 初日の値の保存
                String SvRsvNo = frm.getRsvNo();
                int SvRsvSubNo = frm.getRsvSubNo();
                int SvRsvDate = frm.getRsvDate();
                int SvSeq = frm.getSeq();
                int SvOrgRsvDate = frm.getOrgRsvDate();
                int SvSelPlanId = frm.getSelPlanId();

                // 新予約で、連泊予約時、連泊分の予約情報を更新
                if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) && StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                {
                    String que = "SELECT * FROM " + Schema + ".hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                            + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";
                    ResultSet resultR = null;
                    PreparedStatement prestateR = null;
                    try
                    {
                        prestateR = connection.prepareStatement( que );
                        prestateR.setInt( 1, frm.getSelHotelId() );
                        prestateR.setString( 2, frm.getRsvNoMain() );
                        resultR = prestateR.executeQuery();

                        if ( resultR != null )
                        {
                            while( resultR.next() != false )
                            {
                                // 入力値のセット
                                frm.setRsvNo( resultR.getString( "reserve_no" ) );
                                frm.setRsvSubNo( resultR.getInt( "reserve_sub_no" ) );
                                frm.setRsvDate( resultR.getInt( "reserve_date" ) );
                                frm.setSeq( resultR.getInt( "seq" ) );

                                // 変更予定の内容でロック
                                if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                                {
                                    // 他の人がロックしていた場合
                                    frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                                    Logging.info( "[HapiTouchRsvSub.warn.00030 ] " );
                                    blnRet = false;
                                    return(blnRet);
                                }

                                lockId.add( resultR.getInt( "id" ) );
                                lockReserveDate.add( resultR.getInt( "reserve_date" ) );
                                lockSeq.add( resultR.getInt( "seq" ) );

                                blnRet = execRsvCancelSub( connection, kind, Schema );
                                if ( blnRet == false )
                                {
                                    break;
                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[LogicOwnerRsvCheckIn.execRsvCancel] Exception=" + e.toString() );
                        blnRet = false;
                    }
                    finally
                    {
                        DBConnection.releaseResources( prestateR );
                        DBConnection.releaseResources( resultR );
                    }
                }

                // frmの内容を初日の宿泊日のデータに戻す
                frm.setRsvNo( SvRsvNo );
                frm.setRsvDate( SvRsvDate );
                frm.setRsvSubNo( SvRsvSubNo );
                frm.setSeq( SvSeq );
                frm.setOrgRsvDate( SvOrgRsvDate );
                frm.setSelPlanId( SvSelPlanId );
            }

            // no-show ONのときは、実績データを作成する。
            if ( frm.getNoShow() == 1 )
            {
                blnRet = registRsvResult( connection, ReserveCommon.RESULT_KIND_NOSHOW, frm, frm.getSeq() );
                Logging.info( "[LogicOwnerRsvCheckIn.execRsvCancel]registRsvResult=" + blnRet );
            }
            else
            // no-show でないとき使用マイルを入力していた場合は、マイルを戻す
            {
                blnRet = cancelUseMile( frm.getUserId(), frm.getSelHotelId(), frm.getRsvNo(), -1 );
                Logging.info( "[LogicOwnerRsvCheckIn.execRsvCancel]cancelUseMile=" + blnRet );
            }

            if ( blnRet )
            {
                // 予約ユーザー基本データ更新
                blnRet = updateRsvUserBasic( connection, prestate, 3, frm.getUserId() );
            }
            Logging.info( "[LogicOwnerRsvCheckIn.execRsvCancel]updateRsvUserBasic=" + blnRet );

            if ( blnRet )
            {
                // メール送信依頼データ
                // 同じ予約番号で未依頼のレコードを無効に変更する
                blnRet = updSendMail( connection, prestate, kind, Schema );
            }
            Logging.info( "[LogicOwnerRsvCheckIn.execRsvCancelSub]updSendMail=" + blnRet );

            if ( blnRet )
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
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.execRsvCancel] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            // ロックの解除
            for( int i = 0 ; i < lockId.size() ; i++ )
            {
                LockReserve.UnLock( lockId.get( i ), lockReserveDate.get( i ), lockSeq.get( i ) );
            }
            DBConnection.releaseResources( result, prestate, connection );
        }

        // 戻り値
        return(blnRet);
    }

    /**
     * 
     * 予約データの更新（予約キャンセル）
     * 
     * @param hotelId // ホテルID
     * @param reserveNo // 予約番号
     * @param reserveDate // 予約日
     * @param seq // 部屋番号
     * @param kind // 機種
     * @param noShow // noShowﾌﾗｸﾞ
     * @return
     * @throws Exception
     */
    public boolean execRsvCancelSub(Connection connection, int kind, String Schema) throws Exception
    {

        boolean blnRet = false;
        int subNo = 0;

        PreparedStatement prestate = null;

        // 予約番号枝番取得
        getRsvSubNo( Schema );
        subNo = frm.getRsvSubNo() + 1;
        frm.setRsvSubNo( subNo );

        try
        {

            // 予約データをキャンセルする
            blnRet = setReserveStatus( connection, prestate, frm, ReserveCommon.RSV_STATUS_CANCEL, Schema );
            Logging.info( "[LogicOwnerRsvCheckIn.execRsvCancelSub]setReserveStatus=" + blnRet );

            if ( blnRet )
            {
                // 部屋残数データ
                blnRet = createRoomRemaindar( connection, frm.getRsvNo(), ReserveCommon.ROOM_STATUS_EMPTY, frm.getRsvDate(), Schema );
            }
            Logging.info( "[LogicOwnerRsvCheckIn.execRsvCancelSub]createRoomRemaindar=" + blnRet );

            if ( blnRet )
            {
                // 予約履歴データ
                blnRet = createRsvHistory( connection, ReserveCommon.UPDKBN_CANCEL, frm.getRsvNo(), Schema );
            }
            Logging.info( "[LogicOwnerRsvCheckIn.execRsvCancelSub]createRsvHistory=" + blnRet );

            if ( blnRet )
            {
                // 予約・オプション履歴データ作成
                blnRet = createRsvOptionHistory( connection, prestate, frm.getRsvNo(), Schema );
            }
            Logging.info( "[LogicOwnerRsvCheckIn.execRsvCancelSub]createRsvOptionHistory=" + blnRet );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.execRsvCancelSub] Exception=" + e.toString(), "LogicOwnerRsvCheckIn.execRsvCancelSub" );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return blnRet;

    }

    /**
     * 
     * 予約データの更新（キャンセル取消）
     * 
     * @param hotelId // ホテルID
     * @param reserveNo // 予約番号
     * @param reserveDate // 予約日
     * @param seq // 部屋番号
     * @param kind // 機種
     * @param noShow // noShowﾌﾗｸﾞ
     * @return
     * @throws Exception
     */
    public boolean execRsvUndoCancel(int kind, String Schema) throws Exception
    {
        boolean blnRet = true;
        int subNo = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ReserveCommon rsvCmm = new ReserveCommon();

        ArrayList<Integer> lockId = new ArrayList<Integer>();
        ArrayList<Integer> lockReserveDate = new ArrayList<Integer>();
        ArrayList<Integer> lockSeq = new ArrayList<Integer>();

        try
        {

            // 変更予定の内容でロック
            if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
            {
                // 他の人がロックしていた場合
                frm.setErrMsg( Message.getMessage( "warn.00039" ) );
                blnRet = false;
                return(blnRet);
            }

            lockId.add( frm.getSelHotelId() );
            lockReserveDate.add( frm.getRsvDate() );
            lockSeq.add( frm.getSeq() );

            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            blnRet = execRsvUndoCancelSub( connection, Schema );

            if ( blnRet )
            {
                // 初日の値の保存
                String SvRsvNo = frm.getRsvNo();
                int SvRsvSubNo = frm.getRsvSubNo();
                int SvRsvDate = frm.getRsvDate();
                int SvSeq = frm.getSeq();
                int SvOrgRsvDate = frm.getOrgRsvDate();
                int SvSelPlanId = frm.getSelPlanId();
                int SvNoShow = frm.getNoShow();
                int SvCancelCheck = frm.getCancelCheck();

                // 新予約で、連泊予約時、連泊分の予約情報を更新
                if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) && StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                {
                    String que = "SELECT * FROM " + Schema + ".hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                            + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";
                    ResultSet resultR = null;
                    PreparedStatement prestateR = null;
                    try
                    {
                        prestateR = connection.prepareStatement( que );
                        prestateR.setInt( 1, frm.getSelHotelId() );
                        prestateR.setString( 2, frm.getRsvNoMain() );
                        resultR = prestateR.executeQuery();

                        if ( resultR != null )
                        {
                            while( resultR.next() != false )
                            {
                                // 入力値のセット
                                frm.setRsvNo( resultR.getString( "reserve_no" ) );
                                frm.setRsvSubNo( resultR.getInt( "reserve_sub_no" ) );
                                frm.setRsvDate( resultR.getInt( "reserve_date" ) );
                                frm.setSeq( resultR.getInt( "seq" ) );

                                // 変更予定の内容でロック
                                if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                                {
                                    // 他の人がロックしていた場合
                                    frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                                    Logging.info( "[HapiTouchRsvSub.warn.00030 ] ", "execRaiten" );
                                    blnRet = false;
                                    return(blnRet);
                                }

                                lockId.add( resultR.getInt( "id" ) );
                                lockReserveDate.add( resultR.getInt( "reserve_date" ) );
                                lockSeq.add( resultR.getInt( "seq" ) );

                                blnRet = execRsvUndoCancelSub( connection, Schema );
                                if ( blnRet == false )
                                {
                                    break;
                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[LogicOwnerRsvCheckIn.execRsvUndoCancel] Exception=" + e.toString() );
                        blnRet = false;
                    }
                    finally
                    {
                        DBConnection.releaseResources( prestateR );
                        DBConnection.releaseResources( resultR );
                    }
                }

                // frmの内容を初日の宿泊日のデータに戻す
                frm.setRsvNo( SvRsvNo );
                frm.setRsvDate( SvRsvDate );
                frm.setRsvSubNo( SvRsvSubNo );
                frm.setSeq( SvSeq );
                frm.setOrgRsvDate( SvOrgRsvDate );
                frm.setSelPlanId( SvSelPlanId );
                frm.setNoShow( SvNoShow );
                frm.setCancelCheck( SvCancelCheck );
            }

            if ( blnRet )
            {
                // 予約ユーザー基本データ更新
                blnRet = updateRsvUserBasic( connection, prestate, 4, frm.getUserId() );
            }

            if ( frm.getNoShow() != 1 && blnRet )
            {
                // no-show でないとき使用マイルが取り消されていた場合は元に戻す
                blnRet = cancelUseMile( frm.getUserId(), frm.getSelHotelId(), frm.getRsvNo(), 1 );
                Logging.info( "[LogicOwnerRsvCheckIn.execRsvUndoCancel]cancelUseMile=" + blnRet );
            }

            if ( blnRet )
            {
                // 実績データを削除する
                blnRet = deleteRsvResult( connection, frm );
            }

            if ( blnRet )
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
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.execRsvUndoCancel] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            // ロックの解除
            for( int i = 0 ; i < lockId.size() ; i++ )
            {
                LockReserve.UnLock( lockId.get( i ), lockReserveDate.get( i ), lockSeq.get( i ) );
            }
            DBConnection.releaseResources( result, prestate, connection );
        }

        // 戻り値
        return(blnRet);
    }

    /**
     * 
     * 予約データの更新（キャンセル取消）
     * 
     * @param hotelId // ホテルID
     * @param reserveNo // 予約番号
     * @param reserveDate // 予約日
     * @param seq // 部屋番号
     * @param kind // 機種
     * @param noShow // noShowﾌﾗｸﾞ
     * @return
     * @throws Exception
     */
    public boolean execRsvUndoCancelSub(Connection connection, String Schema) throws Exception
    {
        boolean blnRet = false;
        int subNo = 0;
        int svNoShow = 0;
        PreparedStatement prestate = null;

        // 予約番号枝番取得
        getRsvSubNo( Schema );
        subNo = frm.getRsvSubNo() + 1;
        frm.setRsvSubNo( subNo );

        try
        {
            // ノーショウフラグを一時保存
            svNoShow = frm.getNoShow();
            // ノーショウフラグを消す
            frm.setNoShow( 0 );
            // キャンセルフラグを消す
            frm.setCancelCheck( 0 );

            // 予約データを受け付けに戻す
            blnRet = setReserveStatus( connection, prestate, frm, ReserveCommon.RSV_STATUS_UKETUKE, Schema );

            Logging.info( "[LogicOwnerRsvCheckIn.execRsvUndoCancel]setReserveStatus=" + blnRet );

            if ( blnRet )
            {
                // 部屋残数データ
                blnRet = createRoomRemaindarUndo( connection, frm.getRsvNo(), frm.getRsvDate(), frm.getSeq(), Schema );
            }
            Logging.info( "[LogicOwnerRsvCheckIn.execRsvUndoCancel]createRoomRemaindar=" + blnRet );

            if ( blnRet )
            {
                // 予約履歴データ
                blnRet = createRsvHistory( connection, ReserveCommon.UPDKBN_UPDATE, frm.getRsvNo(), Schema );
            }
            Logging.info( "[LogicOwnerRsvCheckIn.execRsvUndoCancel]createRsvHistory=" + blnRet );

            if ( blnRet )
            {
                // 予約・オプション履歴データ作成
                blnRet = createRsvOptionHistory( connection, prestate, frm.getRsvNo(), Schema );
            }
            Logging.info( "[LogicOwnerRsvCheckIn.execRsvUndoCancel]createRsvOptionHistory=" + blnRet );

            // 保存しておりたノーショウフラグを戻す
            frm.setNoShow( svNoShow );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.execRsvUndoCancelSub] Exception=" + e.toString(), "LogicOwnerRsvCheckIn.execRsvUndoCancelSub" );
            throw e;
        }

        return blnRet;
    }

    /**
     * 
     * 予約データの更新（来店取消）
     * 
     * @param hotelId // ホテルID
     * @param reserveNo // 予約番号
     * @param reserveDate // 予約日
     * @param seq // 部屋番号
     * @param kind // 機種
     * @param noShow // noShowﾌﾗｸﾞ
     * @return
     * @throws Exception
     */
    public boolean execRsvUndoFix(int kind, String Schema) throws Exception
    {
        boolean blnRet = true;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ReserveCommon rsvCmm = new ReserveCommon();

        ArrayList<Integer> lockId = new ArrayList<Integer>();
        ArrayList<Integer> lockReserveDate = new ArrayList<Integer>();
        ArrayList<Integer> lockSeq = new ArrayList<Integer>();

        try
        {

            // 変更予定の内容でロック
            if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
            {
                // 他の人がロックしていた場合
                frm.setErrMsg( Message.getMessage( "warn.00039" ) );
                blnRet = false;
                return(blnRet);
            }

            lockId.add( frm.getSelHotelId() );
            lockReserveDate.add( frm.getRsvDate() );
            lockSeq.add( frm.getSeq() );

            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            blnRet = execRsvUndoFixSub( connection, Schema );

            if ( blnRet )
            {
                // 初日の値の保存
                String SvRsvNo = frm.getRsvNo();
                int SvRsvSubNo = frm.getRsvSubNo();
                int SvRsvDate = frm.getRsvDate();
                int SvSeq = frm.getSeq();
                int SvOrgRsvDate = frm.getOrgRsvDate();
                int SvSelPlanId = frm.getSelPlanId();

                // 新予約で、連泊予約時、連泊分の予約情報を更新
                if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) && StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                {
                    String que = "SELECT * FROM " + Schema + ".hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                            + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";
                    ResultSet resultR = null;
                    PreparedStatement prestateR = null;
                    try
                    {
                        prestateR = connection.prepareStatement( que );
                        prestateR.setInt( 1, frm.getSelHotelId() );
                        prestateR.setString( 2, frm.getRsvNoMain() );
                        resultR = prestateR.executeQuery();

                        if ( resultR != null )
                        {
                            while( resultR.next() != false )
                            {
                                // 入力値のセット
                                frm.setRsvNo( resultR.getString( "reserve_no" ) );
                                frm.setRsvSubNo( resultR.getInt( "reserve_sub_no" ) );
                                frm.setRsvDate( resultR.getInt( "reserve_date" ) );
                                frm.setSeq( resultR.getInt( "seq" ) );

                                // 変更予定の内容でロック
                                if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                                {
                                    // 他の人がロックしていた場合
                                    frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                                    Logging.info( "[HapiTouchRsvSub.warn.00030 ] ", "execRaiten" );
                                    blnRet = false;
                                    return(blnRet);
                                }

                                lockId.add( resultR.getInt( "id" ) );
                                lockReserveDate.add( resultR.getInt( "reserve_date" ) );
                                lockSeq.add( resultR.getInt( "seq" ) );

                                blnRet = execRsvUndoFixSub( connection, Schema );
                                if ( blnRet == false )
                                {
                                    break;
                                }
                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        Logging.error( "[LogicOwnerRsvCheckIn.execRsvUndoFix] Exception=" + e.toString() );
                        blnRet = false;
                    }
                    finally
                    {
                        DBConnection.releaseResources( prestateR );
                        DBConnection.releaseResources( resultR );
                    }
                }

                // frmの内容を初日の宿泊日のデータに戻す
                frm.setRsvNo( SvRsvNo );
                frm.setRsvDate( SvRsvDate );
                frm.setRsvSubNo( SvRsvSubNo );
                frm.setSeq( SvSeq );
                frm.setOrgRsvDate( SvOrgRsvDate );
                frm.setSelPlanId( SvSelPlanId );
            }
            // データチェック
            // frm = rsvCmm.chkDspMaster( frm );
            // if ( frm.getErrMsg().trim().length() != 0 )
            // {
            // // ロックの解除
            // LockReserve.UnLock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() );
            // blnRet = false;
            // return(blnRet);
            // }

            if ( blnRet )
            {
                // 予約ユーザー基本データ更新
                blnRet = updateRsvUserBasic( connection, prestate, 5, frm.getUserId() );
            }

            if ( blnRet )
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
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            Logging.error( "[LogicOwnerRsvCheckIn.execRsvUndoFix] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            // ロックの解除
            for( int i = 0 ; i < lockId.size() ; i++ )
            {
                LockReserve.UnLock( lockId.get( i ), lockReserveDate.get( i ), lockSeq.get( i ) );
            }
            DBConnection.releaseResources( result, prestate, connection );
        }

        // 戻り値
        return(blnRet);
    }

    /**
     * 
     * 予約データの更新（来店取消）
     * 
     * @param hotelId // ホテルID
     * @param reserveNo // 予約番号
     * @param reserveDate // 予約日
     * @param seq // 部屋番号
     * @param kind // 機種
     * @param noShow // noShowﾌﾗｸﾞ
     * @return
     * @throws Exception
     */
    public boolean execRsvUndoFixSub(Connection connection, String Schema) throws Exception
    {

        boolean blnRet = false;
        int subNo = 0;

        PreparedStatement prestate = null;

        try
        {

            // 予約番号枝番取得
            getRsvSubNo( Schema );
            subNo = frm.getRsvSubNo() + 1;
            frm.setRsvSubNo( subNo );

            // 予約データを来店から来店待ち（受け付け）に戻す
            blnRet = setReserveStatus( connection, prestate, frm, ReserveCommon.RSV_STATUS_UKETUKE, Schema );

            if ( blnRet )
            {
                // 実績データを削除する
                blnRet = deleteRsvResult( connection, frm );
            }

            if ( blnRet )
            {
                // 部屋残数データ
                blnRet = createRoomRemaindarUndo( connection, frm.getRsvNo(), frm.getOrgRsvDate(), frm.getSeq(), Schema );
            }

            if ( blnRet )
            {
                // 予約履歴データ
                blnRet = createRsvHistory( connection, ReserveCommon.UPDKBN_UPDATE, frm.getRsvNo(), Schema );
            }

            if ( blnRet )
            {
                // 予約・オプション履歴データ作成
                blnRet = createRsvOptionHistory( connection, prestate, frm.getRsvNo() );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.dataUpdate] Exception=" + e.toString(), "LogicOwnerRsvCheckIn.execRaiten" );
            throw e;
        }

        return blnRet;
    }

    /**
     * 
     * メール送信依頼の更新
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param kind 登録種別
     * @return true:処理成功、false：処理失敗
     * @throws Exception
     */
    private boolean updSendMail(Connection conn, PreparedStatement prestate, int kind, String Schema) throws Exception
    {

        boolean ret = true;
        String query = "";
        DataRsvMailRequest dataMailReq = new DataRsvMailRequest();
        String language = "";

        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {
            // ▼同じ予約番号のデータから言語を取得する
            ResultSet result = null;
            query = "SELECT * FROM newRsvDB.hh_rsv_mail_request WHERE id = ? AND reserve_no = ? ";
            try
            {
                prestate = conn.prepareStatement( query );
                prestate.setInt( 1, frm.getSelHotelId() );
                prestate.setString( 2, frm.getRsvNo() );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        language = result.getString( "language" );
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[LogicOwnerRsvCheckIn.updSendMail] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result );
            }
        }

        // ▼同じ予約番号で未依頼のレコードを無効に変更する
        query = "UPDATE " + Schema + ".hh_rsv_mail_request SET ";
        query = query + "  request_flag = 2 ";
        query = query + " ,regist_term_kind = ? ";
        query = query + " ,regist_date = ? ";
        query = query + " ,regist_time = ? ";
        query = query + "WHERE id = ? ";
        query = query + "  AND reserve_no = ? ";
        query = query + "  AND request_mail_kind = ? ";
        query = query + "  AND request_flag = 0 ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 4, frm.getSelHotelId() );
            prestate.setString( 5, frm.getRsvNo() );
            prestate.setInt( 6, ReserveCommon.MAIL_REQ_REMINDAR );

            prestate.executeUpdate();

            // ▼メール送信依頼データを作成する
            dataMailReq.setId( frm.getSelHotelId() );
            dataMailReq.setReserveNo( frm.getRsvNo() );
            dataMailReq.setReserveSubNo( frm.getRsvSubNo() );
            if ( frm.getNoShow() == 1 )
            {
                // no=Showﾌﾗｸﾞが"ON"の場合
                dataMailReq.setRequestMailKind( ReserveCommon.MAIL_REQ_NOSHOW ); // 依頼メール区分
            }
            else
            {
                // no=Showﾌﾗｸﾞが"OFF"の場合
                dataMailReq.setRequestMailKind( ReserveCommon.MAIL_REQ_CANCELRSV ); // 依頼メール区分
            }
            dataMailReq.setRegistTermKind( kind ); // 登録端末区分
            dataMailReq.setRequestFlag( 0 ); // 依頼フラグ
            dataMailReq.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) ); // 登録日付
            dataMailReq.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) ); // 登録時刻
            dataMailReq.setLanguage( language ); // 言語

            ret = dataMailReq.insertData( conn, Schema );

            // ホテル向けのメール作成
            if ( frm.getNoShow() == 1 )
            {
                // no=Showﾌﾗｸﾞが"ON"の場合
                dataMailReq.setRequestMailKind( ReserveCommon.MAIL_REQ_HOTEL_NOSHOW ); // 依頼メール区分
            }
            else
            {
                // no=Showﾌﾗｸﾞが"OFF"の場合
                dataMailReq.setRequestMailKind( ReserveCommon.MAIL_REQ_HOTEL_CANCELRSV ); // 依頼メール区分
            }

            ret = dataMailReq.insertData( conn, Schema );

            // リマインダメール(受付済みの場合にのみ送信する)
            // if ( frm.getReminder() == 1 )
            // {
            // dataMailReq.setRequestMailKind( ReserveCommon.MAIL_REQ_REMINDAR );
            // ret = dataMailReq.insertData( conn );
            // }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.updSendMail] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }

    /**
     * 
     * 予約データの更新
     * 
     * @param connection Connection
     * @param kind 利用区分
     * @param seq
     * @return true:処理成功、false:処理失敗
     * @throws Exception
     */
    public boolean registRsvResult(Connection connection, int kind, FormReserveSheetPC frm, int seq)
    {

        boolean ret = false;
        DataRsvResult drrs = new DataRsvResult();

        drrs.setId( frm.getSelHotelId() );
        drrs.setRsvNo( frm.getRsvNo() );
        drrs.setSeq( seq );
        drrs.setCiDate( frm.getRsvDate() );
        drrs.setUseKind( kind );
        drrs.setTotalFlag( 0 );
        drrs.setTotalDate( 0 );
        drrs.setTotalTime( 0 );
        drrs.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        drrs.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

        ret = drrs.insertData( connection );

        return(ret);
    }

    /**
     * 仮テーブルのリマインダーフラグ取得
     * 
     * @param workId ワークID
     * @return リマインダーフラグ
     */
    public int getWorkReminderFlg(int workId) throws Exception
    {
        int retReminderFlg = 0;
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = query + "SELECT reminder_flag FROM hh_rsv_reserve_work ";
        query = query + " WHERE work_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, workId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                retReminderFlg = result.getInt( "reminder_flag" );
            }

            return(retReminderFlg);
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.getWorkReminderFlg] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }
    }

    /**
     * 予約仮テーブルへ、各項目を設定
     * ホテル名、ハピーポイント、チェックイン、チェックアウト時間
     * 
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return なし
     */
    public void setRsvWorkData(FormReservePersonalInfoPC frm) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;

        query = query + "UPDATE hh_rsv_reserve_work SET ";
        query = query + "  hotel_name = ? ";
        query = query + " ,add_point = ? ";
        query = query + " ,ci_time_from = ? ";
        query = query + " ,ci_time_to = ? ";
        query = query + " ,co_time = ? ";
        query = query + " WHERE work_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, frm.getHotelName() );
            prestate.setInt( 2, frm.getHapyPoint() );
            prestate.setInt( 3, frm.getCiTimeFrom() );
            prestate.setInt( 4, frm.getCiTimeTo() );
            prestate.setInt( 5, frm.getCoTime() );
            prestate.setInt( 6, frm.getWorkId() );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.setRsvWorkCicoTime] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * 仮テーブルの基本料金取得
     * 
     * @param workId ワークID
     * @return 基本料金
     */
    public int getWorkBasicCharge(int workId) throws Exception
    {
        int retBasicCharge = 0;
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = query + "SELECT basic_charge_total FROM hh_rsv_reserve_work ";
        query = query + " WHERE work_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, workId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                retBasicCharge = result.getInt( "basic_charge_total" );
            }

            return(retBasicCharge);
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.getWorkBasicCharge] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }
    }

    /**
     * 予約・オプションデータ削除
     * 
     * @param conn Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param hotelId ホテルID
     * @param reserveNo 予約番号
     * @return なし
     */
    private void deleteRsvOption(Connection conn, PreparedStatement prestate, int hotelId, String reserveNo) throws Exception
    {
        String query = "";

        query = query + "DELETE FROM hh_rsv_rel_reserve_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND reserve_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, reserveNo );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.deleteRsvOption] Exception=" + e.toString() );
            throw new Exception( e );
        }
    }

    /**
     * 来店確認時に、対象の予約番号の部屋残数データ存在チェック
     * 
     * @param hotelId ホテルID
     * @param calDate 対象日付
     * @param seq 部屋番号
     * @param rsvNo 予約番号
     * @return true:存在する、false:存在しない
     */
    private boolean isExistsRoomReminder(int hotelId, int calDate, int seq, String rsvNo, String Schema) throws Exception
    {
        boolean ret = false;
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        int cnt = 0;

        query = query + "SELECT COUNT(*) AS CNT FROM " + Schema + ".hh_rsv_room_remainder ";
        query = query + " WHERE id = ? ";
        query = query + "    AND cal_date = ? ";
        query = query + "    AND seq = ? ";
        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {
            query = query + "    AND (stay_reserve_no = ?  OR rest_reserve_no = ?)";
        }
        else
        {
            query = query + "    AND reserve_no = ? ";
        }
        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, calDate );
            prestate.setInt( 3, seq );
            prestate.setString( 4, rsvNo );
            if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
            {
                prestate.setString( 5, rsvNo );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt != 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.isExistsRoomReminder] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }

        return(ret);
    }

    /**
     * オプション仮テーブルの通常オプション取得
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @return FormReserveSheetPCオブジェクト
     */
    public FormReserveSheetPC getWorkOptionQuantity(FormReserveSheetPC frm) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<Integer> quantityList = new ArrayList<Integer>();
        ArrayList<String> optNmList = new ArrayList<String>();

        query = query + "SELECT wk.option_id, wk.quantity, opt.option_name ";
        query = query + "FROM hh_rsv_rel_reserve_option_work wk ";
        query = query + "  LEFT JOIN hh_rsv_option opt ON wk.id = opt.id AND wk.option_id = opt.option_id AND wk.option_sub_id = opt.option_sub_id ";
        query = query + " WHERE wk.work_parent_id = ? ";
        query = query + "   AND wk.quantity_flag = 0 ";
        query = query + "   AND wk.quantity > 0 ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getWorkId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                optIdList.add( result.getInt( "option_id" ) );
                quantityList.add( result.getInt( "quantity" ) );
                optNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
            }

            frm.setCheckOptIdList( optIdList );
            frm.setCheckOptNmList( optNmList );
            frm.setCheckQuantityList( quantityList );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.getWorkOptionQuantity] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return(frm);
    }

    /**
     * オプションテーブルの通常オプション取得
     * 
     * @param frm FormReserveSheetPCオブジェクト
     * @return FormReserveSheetPCオブジェクト
     */
    public FormReserveSheetPC getOptionQuantity(FormReserveSheetPC frm) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        ArrayList<Integer> optIdList = new ArrayList<Integer>();
        ArrayList<Integer> quantityList = new ArrayList<Integer>();
        ArrayList<String> optNmList = new ArrayList<String>();

        query = query + "SELECT rsv.option_id, rsv.quantity, opt.option_name ";
        query = query + "FROM hh_rsv_rel_reserve_option rsv ";
        query = query + "  LEFT JOIN hh_rsv_option opt ON rsv.id = opt.id AND rsv.option_id = opt.option_id AND rsv.option_sub_id = opt.option_sub_id ";
        query = query + " WHERE rsv.reserve_no = ? ";
        query = query + "   AND opt.option_flag = 0 ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, frm.getRsvNo() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                optIdList.add( result.getInt( "option_id" ) );
                quantityList.add( result.getInt( "quantity" ) );
                optNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
            }

            frm.setCheckOptIdList( optIdList );
            frm.setCheckOptNmList( optNmList );
            frm.setCheckQuantityList( quantityList );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.getWorkOptionQuantity] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return(frm);
    }

    /**
     * 予約・オプション履歴データ作成
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param newRsvNo 予約番号
     * @return true:処理成功、false:処理失敗
     */
    public boolean createUserPointPayTemp(Connection connection, PreparedStatement prestate) throws Exception
    {

        boolean ret = true;
        String query = "";
        int pointCode = 0;

        query = query + "INSERT INTO hh_user_point_pay_temp SET ";
        query = query + "  user_id = ? ";
        query = query + " ,get_date = ? ";
        query = query + " ,get_time = ? ";
        query = query + " ,code = ? ";
        query = query + " ,point = ? ";
        query = query + " ,point_kind = ? ";
        query = query + " ,ext_code = ? ";
        query = query + " ,ext_string = ? ";
        query = query + " ,person_code = ? ";
        query = query + " ,append_reason = ? ";
        query = query + " ,memo = ? ";
        query = query + " ,reflect_date = ? ";
        query = query + " ,add_flag = ? ";
        query = query + " ,amount = ? ";

        try
        {
            // ポイントコード取得
            pointCode = OwnerRsvCommon.getInitHapyPoint( connection, 2 );

            prestate = connection.prepareStatement( query );
            prestate.setString( 1, frm.getLoginUserId() );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 4, pointCode );
            prestate.setInt( 5, frm.getAddPoint() );
            prestate.setInt( 6, OwnerRsvCommon.HAPYPOINT_24 );
            prestate.setInt( 7, frm.getSelHotelId() );
            prestate.setString( 8, frm.getRsvNo() );
            prestate.setString( 9, "" );
            prestate.setString( 10, "" );
            prestate.setString( 11, "" );
            prestate.setInt( 12, frm.getReflectDate() );
            prestate.setInt( 13, 0 );
            prestate.setInt( 14, frm.getChargeTotal() );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.createUserPointPayTemp] Exception=" + e.toString() );
            ret = false;
            throw e;
        }

        return(ret);
    }

    /**
     * 予約ボーナスマイルデータ作成
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param newRsvNo 予約番号
     * @return true:処理成功、false:処理失敗
     */
    public boolean createBonusMilePayTemp(Connection connection, PreparedStatement prestate) throws Exception
    {

        boolean ret = true;
        String query = "";

        query = query + "INSERT INTO hh_user_point_pay_temp SET ";
        query = query + "  user_id = ? ";
        query = query + " ,get_date = ? ";
        query = query + " ,get_time = ? ";
        query = query + " ,code = ? ";
        query = query + " ,point = ? ";
        query = query + " ,point_kind = ? ";
        query = query + " ,ext_code = ? ";
        query = query + " ,ext_string = ? ";
        query = query + " ,person_code = ? ";
        query = query + " ,append_reason = ? ";
        query = query + " ,memo = ? ";
        query = query + " ,reflect_date = ? ";
        query = query + " ,add_flag = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, frm.getLoginUserId() );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 4, OwnerRsvCommon.RSV_BONUS_CODE );
            prestate.setInt( 5, frm.getAddBonusMile() );
            prestate.setInt( 6, OwnerRsvCommon.HAPYPOINT_29 );
            prestate.setInt( 7, frm.getSelHotelId() );
            prestate.setString( 8, frm.getRsvNo() );
            prestate.setString( 9, "" );
            prestate.setString( 10, "" );
            prestate.setString( 11, "" );
            prestate.setInt( 12, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 13, 1 );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.createBonusMilePayTemp] Exception=" + e.toString() );
            ret = false;
            throw e;
        }

        return(ret);
    }

    /**
     * ポイント算出で必要な情報を取得する
     * 
     * @param reserveNo 予約番号
     * @param int selKbn (1:加算ポイント、2:予約日, 3:予約時間,4:ボーナスマイル )
     * @return ポイント
     */
    public int getRsvPointData(String reserveNo, int selKbn) throws Exception
    {
        int ret = 0;
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = query + "SELECT add_point, reserve_date, est_time_arrival,0 AS add_bonus_mile FROM hh_rsv_reserve ";
        query = query + " WHERE reserve_no = ? ";
        query = query + " UNION SELECT add_point, reserve_date, est_time_arrival,add_bonus_mile FROM newRsvDB.hh_rsv_reserve ";
        query = query + " WHERE reserve_no = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, reserveNo );
            prestate.setString( 2, reserveNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( selKbn == 1 )
                {
                    ret = result.getInt( "add_point" );
                }
                else if ( selKbn == 2 )
                {
                    ret = result.getInt( "reserve_date" );
                }
                else if ( selKbn == 3 )
                {
                    ret = result.getInt( "est_time_arrival" );
                }
                else if ( selKbn == 4 )
                {
                    ret = result.getInt( "add_bonus_mile" );
                }
            }

            return(ret);
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.getRsvPointData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }
    }

    /**
     * 予約ユーザー基本データ存在チェック
     * 
     * @param userId ユーザーID
     * @return boolean true:存在する、false:存在しない
     */
    public boolean existsRsvUserBasic(String userId) throws Exception
    {
        boolean ret = false;
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_rsv_user_basic ";
        query = query + " WHERE user_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( result.getInt( "CNT" ) > 0 )
                {
                    ret = true;
                }
            }

            return(ret);
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.existsRsvUserBasic] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }
    }

    /**
     * 予約ユーザー基本データ作成
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param userId ユーザーID
     * @param rsvDate 予約日
     * @return true:処理成功、false:処理失敗
     */
    private boolean createRsvUserBasic(Connection connection, PreparedStatement prestate, String userId, int rsvDate) throws Exception
    {

        boolean ret = false;
        String query = "";
        int retCnt = 0;

        query = query + "INSERT INTO hh_rsv_user_basic SET ";
        query = query + "user_id = ?, ";
        query = query + "reserve_count = 1, ";
        query = query + "cancel_count = 0, ";
        query = query + "noshow_count = 0, ";
        query = query + "checkin_count = 0, ";
        query = query + "last_reserve_date = ?, ";
        query = query + "limitation_flag = 0, ";
        query = query + "limitation_start_date = 0, ";
        query = query + "limitation_end_date = 0, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, rsvDate );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            retCnt = prestate.executeUpdate();

            if ( retCnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.createRsvUserBasic] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }

    /**
     * 予約ユーザー基本データ更新
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param updKbn 更新区分(1:新規予約・修正、2:来店確認、3:キャンセル、4:キャンセルの取消、5:来店確認取消)
     * @return true:処理成功、false:処理失敗
     */
    public boolean updateRsvUserBasic(Connection connection, PreparedStatement prestate, int updKbn, String userId) throws Exception
    {

        boolean ret = false;
        String query = "";
        int retCnt = 0;
        int idx = 1;
        int rsvLimitEndDate = 0;
        int range = 0;
        Calendar calendar = Calendar.getInstance();
        Properties config = new Properties();
        FileInputStream propfile = null;
        String limitation_reserve_no = "";
        if ( updKbn == 4 )
        {
            DataRsvUserBasic drub = new DataRsvUserBasic();
            if ( drub.getData( userId ) != false )
            {
                limitation_reserve_no = drub.getLimitationReserveNo();
            }
        }

        query = query + "UPDATE hh_rsv_user_basic SET ";
        switch( updKbn )
        {
            case 1:
                // 新規予約・修正
                query = query + "reserve_count = reserve_count + 1, ";
                break;

            case 2:
                // 来店確認
                query = query + "checkin_count = checkin_count + 1, ";
                break;

            case 3:
                // キャンセル
                query = query + "cancel_count = cancel_count + 1, ";
                if ( frm.getNoShow() == ReserveCommon.NO_SHOW_ON && frm.getPayment() != 1 ) // 事前決済はペナルティをつけない
                {
                    // No-Showの場合
                    query = query + "noshow_count = noshow_count + 1, ";
                    query = query + "limitation_flag = 1, ";
                    query = query + "limitation_start_date = ?, ";
                    query = query + "limitation_end_date = ?, ";
                    query = query + "limitation_reserve_no = ?, ";
                    idx = 3;

                    // 予約機能制限終了日の取得
                    calendar.set( Integer.parseInt( Integer.toString( frm.getRsvDate() ).substring( 0, 4 ) ),
                            Integer.parseInt( Integer.toString( frm.getRsvDate() ).substring( 4, 6 ) ) - 1,
                            Integer.parseInt( Integer.toString( frm.getRsvDate() ).substring( 6, 8 ) ) );

                    // プランイメージファイル格納先取得
                    try
                    {
                        propfile = new FileInputStream( ReserveCommon.RSV_LIMIT_CONF );
                        config = new Properties();
                        config.load( propfile );

                        range = Integer.parseInt( config.getProperty( ReserveCommon.LIMIT_KEY ) );
                        propfile.close();
                    }
                    catch ( FileNotFoundException e )
                    {
                        range = 60;
                    }

                    calendar.add( Calendar.DATE, range );

                    rsvLimitEndDate = Integer.parseInt( calendar.get( Calendar.YEAR ) +
                            String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 ) +
                            String.format( "%1$02d", calendar.get( Calendar.DATE ) ) );
                }
                break;

            case 4:
                // キャンセル取り消し
                query = query + "cancel_count = cancel_count -1, ";
                if ( frm.getNoShow() == ReserveCommon.NO_SHOW_ON && frm.getPayment() != 1 && frm.getRsvNo().equals( limitation_reserve_no ) )
                {
                    query = query + "noshow_count = noshow_count - 1, ";
                    // キャンセルするので、制限を外す
                    query = query + "limitation_flag = 0, ";
                    query = query + "limitation_start_date = 0, ";
                    query = query + "limitation_end_date = 0, ";
                    query = query + "limitation_reserve_no = '', ";
                }
                break;

            case 5:
                // 来店確認の取消
                query = query + "checkin_count = checkin_count - 1, ";
                break;
        }
        query = query + "last_reserve_date = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ? ";
        query = query + " WHERE user_id = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            if ( idx == 3 )
            {
                prestate.setInt( 1, frm.getRsvDate() );
                prestate.setInt( 2, rsvLimitEndDate );
                prestate.setString( 3, frm.getRsvNo() );
                idx = idx + 1;
            }

            prestate.setInt( idx, frm.getRsvDate() );
            prestate.setInt( idx + 1, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( idx + 2, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setString( idx + 3, userId );
            if ( frm.getNoShow() == ReserveCommon.NO_SHOW_ON )
            {
                // No-showキャンセルの場合
            }
            retCnt = prestate.executeUpdate();

            if ( retCnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvCheckIn.updateRsvUserBasic] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }

    /**
     * 
     * 予約実績データの削除
     * 
     * @param connection Connection
     * @param frm FormReserveSheetPC
     * @return true:処理成功、false:処理失敗
     * @throws Exception
     */
    public boolean deleteRsvResult(Connection connection, FormReserveSheetPC frm)
    {

        boolean ret = false;
        DataRsvResult drrs = new DataRsvResult();

        ret = drrs.getData( frm.getSelHotelId(), frm.getRsvNo() );
        if ( ret != false )
        {
            ret = drrs.deleteData( connection );
        }
        // データがない場合もOKとする
        else
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * 
     * 予約実績データの削除
     * 
     * @param connection Connection
     * @param frm FormReserveSheetPC
     * @return true:処理成功、false:処理失敗
     * @throws Exception
     */
    public boolean cancelUseMile(String userId, int paramId, String rsvNo, int flag)
    {
        DataUserPointPay dupp = new DataUserPointPay();
        boolean ret = true;

        if ( dupp.getData( userId, paramId, rsvNo, flag ) != false )
        {
            dupp.setPoint( dupp.getPoint() * -1 );
            dupp.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dupp.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = dupp.insertData();
        }
        return(ret);
    }
}
