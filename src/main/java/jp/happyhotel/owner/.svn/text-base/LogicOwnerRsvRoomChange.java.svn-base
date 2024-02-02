package jp.happyhotel.owner;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.LockReserve;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataRsvMailRequest;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.data.DataRsvReserveHistory;
import jp.happyhotel.data.DataRsvUserBasic;
import jp.happyhotel.reserve.FormReserveSheetPC;

import org.apache.commons.lang.StringUtils;

/**
 * 部屋変更ロジック
 */
public class LogicOwnerRsvRoomChange implements Serializable
{

    /**
     *
     */
    private static final long  serialVersionUID = -2606785100683062613L;
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
     * 予約更新処理（予約詳細(PC版)専用)
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    public void updReserve() throws Exception
    {
        boolean blnRet = true;
        int subNo = 1;
        int oldSeq = 0;
        int newSeq = 0;
        String query;
        String newRsvNo = "";
        int planId = 0;
        String Schema = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ArrayList<Integer> lockId = new ArrayList<Integer>();
        ArrayList<Integer> lockReserveDate = new ArrayList<Integer>();
        ArrayList<Integer> lockSeq = new ArrayList<Integer>();
        ArrayList<Integer> lockOrgId = new ArrayList<Integer>();
        ArrayList<Integer> lockOrgReserveDate = new ArrayList<Integer>();
        ArrayList<Integer> lockOrgSeq = new ArrayList<Integer>();

        try
        {
            Logging.info( "[updReserve]:start" );

            // 新予約のみとする
            Schema = ReserveCommon.SCHEMA_NEWRSV;

            oldSeq = frm.getOrgRsvSeq();
            newSeq = frm.getSeq();
            planId = frm.getSelPlanId();
            // 予約番号
            newRsvNo = frm.getRsvNo();
            // 取得した予約番号枝番に1を加算する
            subNo = getReserveSubNo( frm.getSelHotelId(), newRsvNo );
            frm.setRsvSubNo( subNo + 1 );
            Logging.info( "[updReserve]:rsvSubNo=" + frm.getRsvSubNo() );

            try
            {
                // 変更予定の内容でロック
                if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                {
                    Logging.info( "[updReserve] LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ):warn.00030" );
                    // 他の人がロックしていた場合
                    frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                    return;
                }
                lockId.add( frm.getSelHotelId() );
                lockReserveDate.add( frm.getRsvDate() );
                lockSeq.add( frm.getSeq() );

                // 変更前と変更後の予約日、または部屋番号が違う場合は変更前をロック
                // if ( frm.getSeq() != frm.getOrgRsvSeq() )
                // {
                // / if ( LockReserve.Lock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq() ) == false )
                // {
                // Logging.info( "[updReserve] LockReserve.Lock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq()):warn.00030" );
                // 他の人がロックしていた場合
                // frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                // return;
                // }
                // lockOrgId.add( frm.getSelHotelId() );
                // lockOrgReserveDate.add( frm.getOrgRsvDate() );
                // lockOrgSeq.add( frm.getOrgRsvSeq() );
                // }
                connection = DBConnection.getConnection( false );
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                blnRet = updReserveSub( connection, Schema );

                if ( blnRet != false )
                {
                    // 初日の値の保存
                    String SvRsvNo = frm.getRsvNo();
                    int SvRsvSubNo = frm.getRsvSubNo();
                    int SvSeq = frm.getSeq();
                    int SvRsvDate = frm.getRsvDate();
                    int SvOrgRsvDate = frm.getOrgRsvDate();
                    int SvSelPlanId = frm.getSelPlanId();
                    int SvOrgRsvSeq = frm.getOrgRsvSeq();

                    // 新予約で、連泊予約時、連泊分の予約情報を更新
                    if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) && StringUtils.isNotBlank( frm.getRsvNoMain() ) )
                    {
                        String que = "SELECT * FROM " + Schema + ".hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                                + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) != reserve_no_main ORDER BY reserve_date";
                        ResultSet resultR = null;
                        PreparedStatement prestateR = connection.prepareStatement( que );
                        prestateR.setInt( 1, frm.getSelHotelId() );
                        prestateR.setString( 2, frm.getRsvNoMain() );
                        try
                        {
                            resultR = prestateR.executeQuery();

                            if ( resultR != null )
                            {
                                while( resultR.next() != false )
                                {
                                    // 入力値のセット
                                    frm.setRsvNo( resultR.getString( "reserve_no" ) );
                                    frm.setRsvSubNo( resultR.getInt( "reserve_sub_no" ) );
                                    frm.setSeq( newSeq );
                                    frm.setRsvDate( resultR.getInt( "reserve_date" ) );
                                    frm.setOrgRsvDate( resultR.getInt( "reserve_date" ) );

                                    // 変更予定の内容でロック
                                    if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                                    {
                                        Logging.info( "[updReserve] LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ):warn.00030" );
                                        // 他の人がロックしていた場合
                                        frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                                        return;
                                    }
                                    lockId.add( frm.getSelHotelId() );
                                    lockReserveDate.add( frm.getRsvDate() );
                                    lockSeq.add( frm.getSeq() );

                                    // 変更前と変更後の予約日、または部屋番号が違う場合は変更前をロック
                                    // if ( frm.getSeq() != frm.getOrgRsvSeq() )
                                    // {
                                    // if ( LockReserve.Lock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq() ) == false )
                                    // {
                                    // Logging.info( "[updReserve] LockReserve.Lock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq()):warn.00030" );
                                    // 他の人がロックしていた場合
                                    // frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                                    // return;
                                    // }
                                    // lockOrgId.add( frm.getSelHotelId() );
                                    // lockOrgReserveDate.add( frm.getOrgRsvDate() );
                                    // lockOrgSeq.add( frm.getOrgRsvSeq() );
                                    // }

                                    blnRet = updReserveSub( connection, Schema );
                                    if ( blnRet == false )
                                    {
                                        break;
                                    }
                                }
                            }
                        }
                        catch ( Exception e )
                        {
                            Logging.error( "[LogicOwnerRsvRoomChange.updReserve] Exception=" + e.toString() );
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
                    frm.setSeq( SvSeq );
                    frm.setRsvSubNo( SvRsvSubNo );
                    frm.setOrgRsvDate( SvOrgRsvDate );
                    frm.setSelPlanId( SvSelPlanId );
                    frm.setOrgRsvSeq( SvOrgRsvSeq );
                }

                if ( blnRet )
                {
                    // プラン売の部屋変更は対象外、部屋変更も同一部屋選択した場合対象外
                    if ( (planId == frm.getSelPlanId() && frm.getOfferKind() == 1) == false && (planId == frm.getSelPlanId() && frm.getOfferKind() == 2 && newSeq == oldSeq) == false )
                    {
                        // メール送信依頼データ作成
                        sendMail( connection, newRsvNo, frm.getReminder(), ReserveCommon.TERM_KIND_PC, ReserveCommon.MAIL_UPD, Schema );
                    }
                }
                else
                {
                    Logging.info( "[updReserve] :createRsvOptionHistory" );
                }

                if ( blnRet )
                {
                    // 予約ユーザー基本データ作成
                    // ユーザーデータがあるか
                    blnRet = existsRsvUserBasic( frm.getUserId() );
                    if ( blnRet == true )
                    {
                        // 存在する場合、予約ユーザー基本データ更新
                        blnRet = updateRsvUserBasic( connection, prestate, 1, frm.getUserId() );
                    }
                    else
                    {
                        // 存在しない場合、予約ユーザー基本データ作成
                        blnRet = createRsvUserBasic( connection, prestate, frm.getUserId(), frm.getRsvDate() );
                    }
                    Logging.info( "[LogicOwnerRsvRoomChange.createRsvUserBasic]:" + blnRet );
                }

                if ( blnRet )
                {
                    query = "COMMIT ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                }
                else
                {
                    Logging.info( "[updReserve] :updateRsvUserBasic" );
                    query = "ROLLBACK";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    frm.setErrMsg( Message.getMessage( "erro.30002", "予約情報の登録" ) );
                }

                frm.setRsvNo( newRsvNo );
                Logging.info( "[updReserve]:End" );

                // COMMIT成功時、OTAの在庫更新を行う
                if ( blnRet )
                {
                    callOtaStock();
                }

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

            Logging.error( "[LogicOwnerRsvRoomChange.updReserve] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            // ロックの解除
            for( int i = 0 ; i < lockId.size() ; i++ )
            {
                LockReserve.UnLock( lockId.get( i ), lockReserveDate.get( i ), lockSeq.get( i ) );
            }

            // 変更前の内容でロック解除
            for( int i = 0 ; i < lockOrgId.size() ; i++ )
            {
                LockReserve.UnLock( lockOrgId.get( i ), lockOrgReserveDate.get( i ), lockOrgSeq.get( i ) );
            }
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 予約更新処理（予約詳細(PC版)専用)
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    public boolean updReserveSub(Connection connection, String Schema) throws Exception
    {
        boolean blnRet = false;
        int subNo = 0;
        int newSeq = 0;
        int oldSeq = 0;

        String newRsvNo = "";

        oldSeq = frm.getOrgRsvSeq();
        newSeq = frm.getSeq();

        // 予約番号
        newRsvNo = frm.getRsvNo();
        // 取得した予約番号枝番に1を加算する
        subNo = getReserveSubNo( frm.getSelHotelId(), newRsvNo );
        frm.setRsvSubNo( subNo + 1 );
        Logging.info( "[updReserveSub]:newRsvNo=" + newRsvNo + ",rsvSubNo=" + frm.getRsvSubNo() );

        PreparedStatement prestate = null;

        try
        {

            // ▼未依頼のリマインダーメールレコードを無効にする。
            blnRet = setCancelMail( connection, prestate, frm.getSelHotelId(), frm.getRsvNo(), Schema );

            int planType = ReserveCommon.getPlanType( connection, frm.getSelHotelId(), frm.getRsvNo() );
            // 過去の部屋データをセットするため、部屋番号をセット
            frm.setSeq( oldSeq );
            // 部屋残数データ作成(前回の登録した部屋のキャンセル)
            Logging.info( "[updReserveSub] :createRoomRemaindar( connection, '', ReserveCommon.ROOM_STATUS_EMPTY," + frm.getRsvDate() + " )" );
            blnRet = createRoomRemaindar( connection, "", ReserveCommon.ROOM_STATUS_EMPTY, frm.getRsvDate(), planType );

            // 新しい部屋番号をセット
            frm.setSeq( newSeq );
            // ▼予約データ登録
            if ( blnRet )
            {
                blnRet = updRsvData( connection, prestate, Schema );
            }
            else
            {
                Logging.info( "[updReserve] :createRoomRemaindar( connection, newRsvNo, ReserveCommon.ROOM_STATUS_EMPTY, frm.getRsvDate() )" );
            }

            if ( blnRet )
            {
                if ( frm.getStatus() == 1 ) // 来店待ち
                {
                    // 部屋残数データ作成
                    blnRet = createRoomRemaindar( connection, newRsvNo, ReserveCommon.ROOM_STATUS_EMPTY, frm.getRsvDate(), planType );
                }
                else
                {
                    // 部屋残数データ作成
                    Logging.info( "[updReserveSub] :createRoomRemaindar( connection, " + newRsvNo + ", ReserveCommon.ROOM_STATUS_RSV," + frm.getRsvDate() + " )" );
                    blnRet = createRoomRemaindar( connection, newRsvNo, ReserveCommon.ROOM_STATUS_RSV, frm.getRsvDate(), planType );
                }
            }
            else
            {
                Logging.info( "[updReserve] :updRsvData( connection, prestate )" );
            }
            if ( blnRet )
            {
                // 予約履歴データ作成
                blnRet = createRsvHistory( connection, ReserveCommon.UPDKBN_UPDATE, newRsvNo, Schema );
            }
            else
            {
                Logging.info( "[updReserve] :createRoomRemaindar( connection, newRsvNo, ReserveCommon.ROOM_STATUS_RSV, frm.getRsvDate() )" );
            }
            if ( blnRet )
            {
                // 予約・オプション履歴データ作成
                blnRet = createRsvOptionHistory( connection, prestate, newRsvNo, Schema );
            }
            else
            {
                Logging.info( "[updReserve] :createRsvHistory( connection, ReserveCommon.UPDKBN_UPDATE, newRsvNo )" );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoomChange.updReserveSub] Exception=" + e.toString(), "LogicOwnerRsvRoomChange.updReserveSub" );
            throw e;
        }

        return blnRet;
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

        query = query + "UPDATE " + Schema + ".hh_rsv_mail_request SET ";
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
            Logging.error( "[LogicOwnerRsvRoomChange.setCancelMail] Exception=" + e.toString() + "," + query );
            throw e;
        }
        return(ret);
    }

    /**
     * 予約データ作成
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param newRsvNo 予約番号
     * @param seq 部屋番号
     * @return true:処理成功、false:処理失敗
     */
    private boolean updRsvData(Connection connection, PreparedStatement prestate, String Schema) throws Exception
    {

        boolean ret = false;
        String query = "";
        int retCnt = 0;

        query = query + "UPDATE " + Schema + ".hh_rsv_reserve ";
        query = query + " SET reserve_sub_no=?,";
        query = query + " seq=? ";
        query = query + " ,room_hold=? ";
        query = query + " WHERE id = ? AND reserve_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getRsvSubNo() );
            prestate.setInt( 2, frm.getSeq() );
            prestate.setInt( 3, frm.getRoomHold() );
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
            Logging.error( "[LogicOwnerRsvRoomChange.updRsvData()] Exception=" + e.toString() + "," + query );
            throw e;
        }

        return(ret);
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
    private boolean createRoomRemaindar(Connection connection, String newRsvNo, int status, int rsvDate, int planType)
    {
        boolean ret = false;
        Logging.info( "LogicOwnerRsvRoomRemainder[createRoomRemaindar]( connection," + frm.getSelHotelId() + "," + rsvDate + "," + frm.getSeq() + "," + newRsvNo + "," + status + " )" );
        ret = ReserveCommon.updateRemainder( connection, frm.getSelHotelId(), rsvDate, frm.getSeq(), newRsvNo, status, planType );
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
        Logging.info( "[updReserve]:rsvSubNo=" + frm.getRsvSubNo() );
        Logging.info( "[updReserve]:rsvSubNo=" + ddr.getReserveNoSub() );

        // 予約履歴データ
        drrh = new DataRsvReserveHistory();
        drrh.setId( ddr.getID() );
        drrh.setReserveNo( rsvNo );
        drrh.setReserveSubNo( ddr.getReserveNoSub() );
        drrh.setUpdateKind( updKind );
        drrh.setPlanId( ddr.getPlanId() );
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
        drrh.setMailAddr1( ddr.getMailAddr1() );
        drrh.setMailAddr2( ddr.getMailAddr2() );

        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {
            drrh.setPlanSubId( ddr.getPlanSubId() );
            drrh.setPayment( ddr.getPayment() );
            drrh.setPaymentStatus( ddr.getPaymentStatus() );
            drrh.setAddBonusMile( ddr.getAddBonusMile() );
            drrh.setUsedMile( ddr.getUsedMile() );
            drrh.setOtaCd( ddr.getOtaCd() );
            drrh.setOtaBookingCode( ddr.getOtaBookingCode() );
            drrh.setOtaTotalAmountAfterTaxes( ddr.getOtaTotalAmountAfterTaxes() );
            drrh.setOtaTotalAmountOfTaxes( ddr.getOtaTotalAmountOfTaxes() );
            drrh.setOtaCurrency( ddr.getOtaCurrency() );
        }
        drrh.setConsumerDemands( ddr.getConsumerDemands() );
        drrh.setReserveNoMain( ddr.getReserveNoMain() );
        drrh.setReserveDateTo( ddr.getReserveDateTo() );
        drrh.setExtFlag( ddr.getExtFlag() );
        drrh.setCancelCharge( ddr.getCancelCharge() );
        drrh.setBasicChargeTotalAll( ddr.getBasicChargeTotalAll() );
        drrh.setOptionChargeTotalAll( ddr.getOptionChargeTotalAll() );
        drrh.setChargeTotalAll( ddr.getChargeTotalAll() );
        drrh.setCancelDate( ddr.getCancelDate() );
        drrh.setCancelCreditStatus( ddr.getCancelCreditStatus() );
        drrh.setRoomHold( ddr.getRoomHold() );

        ret = drrh.insertData( connection, Schema );

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
        if ( remainderFlg == 1 )
        {
            DataRsvMailRequest data = new DataRsvMailRequest();
            // リマインダメールで送信済みのものがないかを検査する
            if ( !data.getData( connection, frm.getSelHotelId(), newRsvNo, ReserveCommon.MAIL_REQ_REMINDAR ) )
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
                        Logging.error( "[LogicOwnerRsvRoomChange.sendMail] Exception=" + e.toString() );
                    }
                    finally
                    {
                        DBConnection.releaseResources( result );
                        DBConnection.releaseResources( prestate );
                    }
                }

                // 送信済みではないので、追加する。
                data.setId( frm.getSelHotelId() );
                data.setReserveNo( newRsvNo );
                data.setReserveSubNo( frm.getRsvSubNo() );
                data.setRequestFlag( 0 );
                data.setRegistTermKind( frm.getTermKind() );
                data.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                data.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                data.setLanguage( language );

                data.setRequestMailKind( ReserveCommon.MAIL_REQ_REMINDAR ); // 依頼メール区分
                data.insertData( connection, Schema );
            }
        }
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
        String query = "";

        query = query + "INSERT INTO " + Schema + ".hh_rsv_rel_reserve_option_history ( ";
        query = query + "  id ";
        query = query + " ,reserve_no ";
        query = query + " ,reserve_sub_no ";
        query = query + " ,option_id ";
        query = query + " ,option_sub_id ";
        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {
            query = query + " ,number ";
        }
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
        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {
            query = query + " ,number ";
        }
        query = query + " ,quantity ";
        query = query + " ,unit_price ";
        query = query + " ,charge_total ";
        query = query + " ,remarks ";
        query = query + "FROM " + Schema + ".hh_rsv_rel_reserve_option ";
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
            Logging.error( "[LogicOwnerRsvRoomChange.createRsvOptionHistory] Exception=" + e.toString() );
            ret = false;
            throw e;
        }

        return(ret);
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
            Logging.error( "[LogicOwnerRsvRoomChange.existsRsvUserBasic] Exception=" + e.toString() );
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
            Logging.error( "[LogicOwnerRsvRoomChange.createRsvUserBasic] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }

    /**
     * 予約ユーザー基本データ更新
     * 
     * @param connection Connectionオブジェクト
     * @param prestate PreparedStatementオブジェクト
     * @param updKbn 更新区分(1:新規予約・修正、2:来店確認、3:キャンセル)
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
                if ( frm.getNoShow() == ReserveCommon.NO_SHOW_ON && frm.getPayment() != 1 )
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
                    propfile = new FileInputStream( ReserveCommon.RSV_LIMIT_CONF );
                    config = new Properties();
                    config.load( propfile );

                    range = Integer.parseInt( config.getProperty( ReserveCommon.LIMIT_KEY ) );
                    propfile.close();
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
            Logging.error( "[LogicOwnerRsvRoomChange.updateRsvUserBasic] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }

    /*****
     * 予約枝番号取得
     * 
     * @param id
     * @param rsvNo
     * @return
     */
    public int getReserveSubNo(int id, String rsvNo)
    {
        int subNo = 0;
        DataRsvReserve drr;
        drr = new DataRsvReserve();
        drr.getData( id, rsvNo );
        subNo = drr.getReserveNoSub();
        drr = null;
        return(subNo);
    }

    /*****
     * OTA在庫更新API呼出し<br/>
     * 
     * OTA予約をしているホテルで、かつ、<br/>
     * 変更前の部屋がOTAプランで使用されている時</br>
     * または、変更後の部屋がOTAプランで使用されている時、OTAの在庫更新APIを呼ぶ
     * 
     * @return
     * @throws Exception
     */
    private void callOtaStock()
    {
        // OTA在庫更新API呼出し判断
        boolean callApiFlag = false;
        int rankOld = 0;// 変更前の部屋ランク
        int rankNew = 0;// 変更後の部屋ランク
        boolean otaRankOldSeq = false;// 変更前部屋番号がOTA用プランに使用されているか
        boolean otaRankNewSeq = false;// 変更後部屋番号がOTA用プランに使用されているか

        try
        {
            // OTA予約をしているホテルのとき
            if ( OwnerRsvCommon.checkOtaHotel( frm.getSelHotelId() ) )
            {
                DataHotelRoomMore room = new DataHotelRoomMore();
                if ( room.getData( frm.getSelHotelId(), frm.getOrgRsvSeq() ) )
                {
                    rankOld = room.getRoomRank();
                }
                if ( room.getData( frm.getSelHotelId(), frm.getSeq() ) )
                {
                    rankNew = room.getRoomRank();
                }
                otaRankOldSeq = OwnerRsvCommon.chkOtaPlanSeq( frm.getSelHotelId(), rankOld, frm.getOrgRsvSeq() );
                otaRankNewSeq = OwnerRsvCommon.chkOtaPlanSeq( frm.getSelHotelId(), rankNew, frm.getSeq() );
                if ( ((rankOld == rankNew) && (otaRankOldSeq != otaRankNewSeq)) ||
                        ((rankOld != rankNew) && (otaRankOldSeq || otaRankNewSeq)) )
                    // 変更前部屋と変更後部屋のランクが同じで、どちらか一方がOTAで使用している部屋のとき（両方使用している部屋のときを除く）
                    // 変更前部屋と変更後部屋のランクが異なり、どちらか一方がOTAで使用している部屋のとき
                    callApiFlag = true;
            }

            if ( callApiFlag )
            {
                String api = "stock";
                int modeFlag = 1;
                int dateTo = frm.getReserveDateTo();
                if ( frm.getReserveDateTo() == 0 )
                {
                    dateTo = frm.getRsvDate();
                }
                String param = "json={" +
                        "id:" + frm.getSelHotelId() + "," +
                        "ranks:[";
                if ( otaRankOldSeq )
                {
                    param += "{room_rank:" + rankOld + "," +
                            "dates:[" +
                            "{date: " + frm.getRsvDate() + "," +
                            "date_to:" + dateTo + "}" +
                            "]" +
                            "}";
                }
                if ( otaRankNewSeq )
                {
                    param += (otaRankOldSeq) ? "," : "";
                    param += "{room_rank:" + rankNew + "," +
                            "dates:[" +
                            "{date: " + frm.getRsvDate() + "," +
                            "date_to:" + dateTo + "}" +
                            "]" +
                            "}";
                }
                param += "]," +
                        "mode_flag:" + modeFlag +
                        "}";
                OwnerRsvCommon.callOtaApiThread( api, param );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvRoomChange.callOtaStock] Exception=" + e.toString() );
        }
    }
}
