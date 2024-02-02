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
 * �����ύX���W�b�N
 */
public class LogicOwnerRsvRoomChange implements Serializable
{

    /**
     *
     */
    private static final long  serialVersionUID = -2606785100683062613L;
    private FormReserveSheetPC frm;

    /* �t�H�[���I�u�W�F�N�g */
    public FormReserveSheetPC getFrm()
    {
        return frm;
    }

    public void setFrm(FormReserveSheetPC frm)
    {
        this.frm = frm;
    }

    /**
     * �\��X�V�����i�\��ڍ�(PC��)��p)
     * 
     * @param �Ȃ�
     * @return �Ȃ�
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

            // �V�\��݂̂Ƃ���
            Schema = ReserveCommon.SCHEMA_NEWRSV;

            oldSeq = frm.getOrgRsvSeq();
            newSeq = frm.getSeq();
            planId = frm.getSelPlanId();
            // �\��ԍ�
            newRsvNo = frm.getRsvNo();
            // �擾�����\��ԍ��}�Ԃ�1�����Z����
            subNo = getReserveSubNo( frm.getSelHotelId(), newRsvNo );
            frm.setRsvSubNo( subNo + 1 );
            Logging.info( "[updReserve]:rsvSubNo=" + frm.getRsvSubNo() );

            try
            {
                // �ύX�\��̓��e�Ń��b�N
                if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                {
                    Logging.info( "[updReserve] LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ):warn.00030" );
                    // ���̐l�����b�N���Ă����ꍇ
                    frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                    return;
                }
                lockId.add( frm.getSelHotelId() );
                lockReserveDate.add( frm.getRsvDate() );
                lockSeq.add( frm.getSeq() );

                // �ύX�O�ƕύX��̗\����A�܂��͕����ԍ����Ⴄ�ꍇ�͕ύX�O�����b�N
                // if ( frm.getSeq() != frm.getOrgRsvSeq() )
                // {
                // / if ( LockReserve.Lock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq() ) == false )
                // {
                // Logging.info( "[updReserve] LockReserve.Lock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq()):warn.00030" );
                // ���̐l�����b�N���Ă����ꍇ
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
                    // �����̒l�̕ۑ�
                    String SvRsvNo = frm.getRsvNo();
                    int SvRsvSubNo = frm.getRsvSubNo();
                    int SvSeq = frm.getSeq();
                    int SvRsvDate = frm.getRsvDate();
                    int SvOrgRsvDate = frm.getOrgRsvDate();
                    int SvSelPlanId = frm.getSelPlanId();
                    int SvOrgRsvSeq = frm.getOrgRsvSeq();

                    // �V�\��ŁA�A���\�񎞁A�A�����̗\������X�V
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
                                    // ���͒l�̃Z�b�g
                                    frm.setRsvNo( resultR.getString( "reserve_no" ) );
                                    frm.setRsvSubNo( resultR.getInt( "reserve_sub_no" ) );
                                    frm.setSeq( newSeq );
                                    frm.setRsvDate( resultR.getInt( "reserve_date" ) );
                                    frm.setOrgRsvDate( resultR.getInt( "reserve_date" ) );

                                    // �ύX�\��̓��e�Ń��b�N
                                    if ( LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ) == false )
                                    {
                                        Logging.info( "[updReserve] LockReserve.Lock( frm.getSelHotelId(), frm.getRsvDate(), frm.getSeq() ):warn.00030" );
                                        // ���̐l�����b�N���Ă����ꍇ
                                        frm.setErrMsg( Message.getMessage( "warn.00030" ) );
                                        return;
                                    }
                                    lockId.add( frm.getSelHotelId() );
                                    lockReserveDate.add( frm.getRsvDate() );
                                    lockSeq.add( frm.getSeq() );

                                    // �ύX�O�ƕύX��̗\����A�܂��͕����ԍ����Ⴄ�ꍇ�͕ύX�O�����b�N
                                    // if ( frm.getSeq() != frm.getOrgRsvSeq() )
                                    // {
                                    // if ( LockReserve.Lock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq() ) == false )
                                    // {
                                    // Logging.info( "[updReserve] LockReserve.Lock( frm.getSelHotelId(), frm.getOrgRsvDate(), frm.getOrgRsvSeq()):warn.00030" );
                                    // ���̐l�����b�N���Ă����ꍇ
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

                    // frm�̓��e�������̏h�����̃f�[�^�ɖ߂�
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
                    // �v�������̕����ύX�͑ΏۊO�A�����ύX�����ꕔ���I�������ꍇ�ΏۊO
                    if ( (planId == frm.getSelPlanId() && frm.getOfferKind() == 1) == false && (planId == frm.getSelPlanId() && frm.getOfferKind() == 2 && newSeq == oldSeq) == false )
                    {
                        // ���[�����M�˗��f�[�^�쐬
                        sendMail( connection, newRsvNo, frm.getReminder(), ReserveCommon.TERM_KIND_PC, ReserveCommon.MAIL_UPD, Schema );
                    }
                }
                else
                {
                    Logging.info( "[updReserve] :createRsvOptionHistory" );
                }

                if ( blnRet )
                {
                    // �\�񃆁[�U�[��{�f�[�^�쐬
                    // ���[�U�[�f�[�^�����邩
                    blnRet = existsRsvUserBasic( frm.getUserId() );
                    if ( blnRet == true )
                    {
                        // ���݂���ꍇ�A�\�񃆁[�U�[��{�f�[�^�X�V
                        blnRet = updateRsvUserBasic( connection, prestate, 1, frm.getUserId() );
                    }
                    else
                    {
                        // ���݂��Ȃ��ꍇ�A�\�񃆁[�U�[��{�f�[�^�쐬
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
                    frm.setErrMsg( Message.getMessage( "erro.30002", "�\����̓o�^" ) );
                }

                frm.setRsvNo( newRsvNo );
                Logging.info( "[updReserve]:End" );

                // COMMIT�������AOTA�̍݌ɍX�V���s��
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
            // ���b�N�̉���
            for( int i = 0 ; i < lockId.size() ; i++ )
            {
                LockReserve.UnLock( lockId.get( i ), lockReserveDate.get( i ), lockSeq.get( i ) );
            }

            // �ύX�O�̓��e�Ń��b�N����
            for( int i = 0 ; i < lockOrgId.size() ; i++ )
            {
                LockReserve.UnLock( lockOrgId.get( i ), lockOrgReserveDate.get( i ), lockOrgSeq.get( i ) );
            }
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * �\��X�V�����i�\��ڍ�(PC��)��p)
     * 
     * @param �Ȃ�
     * @return �Ȃ�
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

        // �\��ԍ�
        newRsvNo = frm.getRsvNo();
        // �擾�����\��ԍ��}�Ԃ�1�����Z����
        subNo = getReserveSubNo( frm.getSelHotelId(), newRsvNo );
        frm.setRsvSubNo( subNo + 1 );
        Logging.info( "[updReserveSub]:newRsvNo=" + newRsvNo + ",rsvSubNo=" + frm.getRsvSubNo() );

        PreparedStatement prestate = null;

        try
        {

            // �����˗��̃��}�C���_�[���[�����R�[�h�𖳌��ɂ���B
            blnRet = setCancelMail( connection, prestate, frm.getSelHotelId(), frm.getRsvNo(), Schema );

            int planType = ReserveCommon.getPlanType( connection, frm.getSelHotelId(), frm.getRsvNo() );
            // �ߋ��̕����f�[�^���Z�b�g���邽�߁A�����ԍ����Z�b�g
            frm.setSeq( oldSeq );
            // �����c���f�[�^�쐬(�O��̓o�^���������̃L�����Z��)
            Logging.info( "[updReserveSub] :createRoomRemaindar( connection, '', ReserveCommon.ROOM_STATUS_EMPTY," + frm.getRsvDate() + " )" );
            blnRet = createRoomRemaindar( connection, "", ReserveCommon.ROOM_STATUS_EMPTY, frm.getRsvDate(), planType );

            // �V���������ԍ����Z�b�g
            frm.setSeq( newSeq );
            // ���\��f�[�^�o�^
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
                if ( frm.getStatus() == 1 ) // ���X�҂�
                {
                    // �����c���f�[�^�쐬
                    blnRet = createRoomRemaindar( connection, newRsvNo, ReserveCommon.ROOM_STATUS_EMPTY, frm.getRsvDate(), planType );
                }
                else
                {
                    // �����c���f�[�^�쐬
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
                // �\�񗚗��f�[�^�쐬
                blnRet = createRsvHistory( connection, ReserveCommon.UPDKBN_UPDATE, newRsvNo, Schema );
            }
            else
            {
                Logging.info( "[updReserve] :createRoomRemaindar( connection, newRsvNo, ReserveCommon.ROOM_STATUS_RSV, frm.getRsvDate() )" );
            }
            if ( blnRet )
            {
                // �\��E�I�v�V���������f�[�^�쐬
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
     * �����\��ԍ��Ŗ��˗��̃��R�[�h�𖳌��ɕύX����
     * 
     * @param connection Connection�I�u�W�F�N�g
     * @param prestate PreparedStatement�I�u�W�F�N�g
     * @param hotelId �z�e��ID
     * @param rsvNo �\��ԍ�
     * @return true:����Afalse:�ُ�
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
     * �\��f�[�^�쐬
     * 
     * @param connection Connection�I�u�W�F�N�g
     * @param prestate PreparedStatement�I�u�W�F�N�g
     * @param newRsvNo �\��ԍ�
     * @param seq �����ԍ�
     * @return true:���������Afalse:�������s
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
     * �����c���f�[�^�쐬
     * 
     * @param connection Connection�I�u�W�F�N�g
     * @param newRsvNo �\��ԍ�
     * @param status �X�e�[�^�X
     * @param rsvDate �Ώۂ̗\���
     * @return true:���������Afalse:�������s
     */
    private boolean createRoomRemaindar(Connection connection, String newRsvNo, int status, int rsvDate, int planType)
    {
        boolean ret = false;
        Logging.info( "LogicOwnerRsvRoomRemainder[createRoomRemaindar]( connection," + frm.getSelHotelId() + "," + rsvDate + "," + frm.getSeq() + "," + newRsvNo + "," + status + " )" );
        ret = ReserveCommon.updateRemainder( connection, frm.getSelHotelId(), rsvDate, frm.getSeq(), newRsvNo, status, planType );
        return(ret);
    }

    /**
     * �\�񗚗��f�[�^�쐬
     * 
     * @param connection Connection�I�u�W�F�N�g
     * @param updKind
     * @param rsvNo �\��ԍ�
     * @return true:���������Afalse:�������s
     */
    public boolean createRsvHistory(Connection connection, int updKind, String rsvNo, String Schema)
    {

        boolean ret = false;
        DataRsvReserve ddr = new DataRsvReserve();
        DataRsvReserveHistory drrh = new DataRsvReserveHistory();

        // �\��f�[�^�擾
        ddr.getData( connection, frm.getSelHotelId(), rsvNo );
        Logging.info( "[updReserve]:rsvSubNo=" + frm.getRsvSubNo() );
        Logging.info( "[updReserve]:rsvSubNo=" + ddr.getReserveNoSub() );

        // �\�񗚗��f�[�^
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
     * ���[�����M�˗��f�[�^�쐬
     * 
     * @param connection Connection
     * @param newRsvNo �\��ԍ�
     * @param remainderFlg ���}�C���_�[�t���O
     * @param regTermKind �o�^�[���敪
     * @return �Ȃ�
     */
    public void sendMail(Connection connection, String newRsvNo, int remainderFlg, int regTermKind, int mailKind, String Schema)
    {
        if ( remainderFlg == 1 )
        {
            DataRsvMailRequest data = new DataRsvMailRequest();
            // ���}�C���_���[���ő��M�ς݂̂��̂��Ȃ�������������
            if ( !data.getData( connection, frm.getSelHotelId(), newRsvNo, ReserveCommon.MAIL_REQ_REMINDAR ) )
            {
                String language = "";
                if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
                {
                    // �������\��ԍ��̃f�[�^���猾����擾����
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

                // ���M�ς݂ł͂Ȃ��̂ŁA�ǉ�����B
                data.setId( frm.getSelHotelId() );
                data.setReserveNo( newRsvNo );
                data.setReserveSubNo( frm.getRsvSubNo() );
                data.setRequestFlag( 0 );
                data.setRegistTermKind( frm.getTermKind() );
                data.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                data.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                data.setLanguage( language );

                data.setRequestMailKind( ReserveCommon.MAIL_REQ_REMINDAR ); // �˗����[���敪
                data.insertData( connection, Schema );
            }
        }
    }

    /**
     * �\��E�I�v�V���������f�[�^�쐬
     * 
     * @param connection Connection�I�u�W�F�N�g
     * @param prestate PreparedStatement�I�u�W�F�N�g
     * @param newRsvNo �\��ԍ�
     * @return true:���������Afalse:�������s
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
     * �\�񃆁[�U�[��{�f�[�^���݃`�F�b�N
     * 
     * @param userId ���[�U�[ID
     * @return boolean true:���݂���Afalse:���݂��Ȃ�
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
     * �\�񃆁[�U�[��{�f�[�^�쐬
     * 
     * @param connection Connection�I�u�W�F�N�g
     * @param prestate PreparedStatement�I�u�W�F�N�g
     * @param userId ���[�U�[ID
     * @param rsvDate �\���
     * @return true:���������Afalse:�������s
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
     * �\�񃆁[�U�[��{�f�[�^�X�V
     * 
     * @param connection Connection�I�u�W�F�N�g
     * @param prestate PreparedStatement�I�u�W�F�N�g
     * @param updKbn �X�V�敪(1:�V�K�\��E�C���A2:���X�m�F�A3:�L�����Z��)
     * @return true:���������Afalse:�������s
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
                // �V�K�\��E�C��
                query = query + "reserve_count = reserve_count + 1, ";
                break;

            case 2:
                // ���X�m�F
                query = query + "checkin_count = checkin_count + 1, ";
                break;

            case 3:
                // �L�����Z��
                query = query + "cancel_count = cancel_count + 1, ";
                if ( frm.getNoShow() == ReserveCommon.NO_SHOW_ON && frm.getPayment() != 1 )
                {
                    // No-Show�̏ꍇ
                    query = query + "noshow_count = noshow_count + 1, ";
                    query = query + "limitation_flag = 1, ";
                    query = query + "limitation_start_date = ?, ";
                    query = query + "limitation_end_date = ?, ";
                    query = query + "limitation_reserve_no = ?, ";
                    idx = 3;

                    // �\��@�\�����I�����̎擾
                    calendar.set( Integer.parseInt( Integer.toString( frm.getRsvDate() ).substring( 0, 4 ) ),
                            Integer.parseInt( Integer.toString( frm.getRsvDate() ).substring( 4, 6 ) ) - 1,
                            Integer.parseInt( Integer.toString( frm.getRsvDate() ).substring( 6, 8 ) ) );

                    // �v�����C���[�W�t�@�C���i�[��擾
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
                // �L�����Z��������
                query = query + "cancel_count = cancel_count -1, ";
                if ( frm.getNoShow() == ReserveCommon.NO_SHOW_ON && frm.getPayment() != 1 && frm.getRsvNo().equals( limitation_reserve_no ) )
                {
                    query = query + "noshow_count = noshow_count - 1, ";
                    // �L�����Z������̂ŁA�������O��
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
                // No-show�L�����Z���̏ꍇ
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
     * �\��}�ԍ��擾
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
     * OTA�݌ɍX�VAPI�ďo��<br/>
     * 
     * OTA�\������Ă���z�e���ŁA���A<br/>
     * �ύX�O�̕�����OTA�v�����Ŏg�p����Ă��鎞</br>
     * �܂��́A�ύX��̕�����OTA�v�����Ŏg�p����Ă��鎞�AOTA�̍݌ɍX�VAPI���Ă�
     * 
     * @return
     * @throws Exception
     */
    private void callOtaStock()
    {
        // OTA�݌ɍX�VAPI�ďo�����f
        boolean callApiFlag = false;
        int rankOld = 0;// �ύX�O�̕��������N
        int rankNew = 0;// �ύX��̕��������N
        boolean otaRankOldSeq = false;// �ύX�O�����ԍ���OTA�p�v�����Ɏg�p����Ă��邩
        boolean otaRankNewSeq = false;// �ύX�㕔���ԍ���OTA�p�v�����Ɏg�p����Ă��邩

        try
        {
            // OTA�\������Ă���z�e���̂Ƃ�
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
                    // �ύX�O�����ƕύX�㕔���̃����N�������ŁA�ǂ��炩�����OTA�Ŏg�p���Ă��镔���̂Ƃ��i�����g�p���Ă��镔���̂Ƃ��������j
                    // �ύX�O�����ƕύX�㕔���̃����N���قȂ�A�ǂ��炩�����OTA�Ŏg�p���Ă��镔���̂Ƃ�
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
