/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * �n�s�^�b�`����N���X
 */
package jp.happyhotel.others;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataHhRsvPlan;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelRoom;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataRsvReserveBasic;
import jp.happyhotel.data.DataUserFelica;
import jp.happyhotel.hotel.HotelCi;
import jp.happyhotel.owner.LogicOwnerRsvCheckIn;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserPointPay;
import jp.happyhotel.user.UserPointPayTemp;

/**
 * �n�s�^�b�`>
 * 
 * @author S.Tashiro
 * @version 1.00 2010/11/17
 */
public class HapiTouchRsvSub
{

    private static final int POINT_KIND_WARIBIKI = 23;     // ����
    private static final int POINT_KIND_YOYAKU   = 24;     // �\��
    private static final int RSV_POINT           = 1000007;

    /**
     * �L�����Z���{�^���N���b�N����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws
     */
    public FormReserveSheetPC execCancel(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            // �V�K�o�^���ȊO�����f�[�^�̃X�e�[�^�X�̃`�F�b�N
            if ( rsvCmm.checkStatus( frm.getSelHotelId(), frm.getRsvNo(), status ) == false )
            {
                errMsg = Message.getMessage( "warn.00019" );
                frm.setErrMsg( errMsg );
                return(frm);
            }

            // �\��L�����Z���`�F�b�N������Ă��邩
            if ( frm.getCancelCheck() == 0 )
            {
                // �o�^���s
                errMsg = Message.getMessage( "warn.00032" );

                // ��ʓ��e�Ď擾
                // �\��f�[�^���o
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( frm.getMode() );

                return(frm);
            }

            // ���[�N�e�[�u���ɓo�^����Ă���ʏ�I�v�V�������擾
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // �f�[�^�X�V�i�L�����Z�������j
            logic.setFrm( frm );

            Logging.info( "[FormReserveSheetPC execCancel]reserve_no=" + frm.getRsvNo() + ",calDate=" + frm.getOrgRsvDate() + ",frm.getAdultNum() " + frm.getAdultNum() );

            ret = logic.execRsvCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );

            if ( ret == false )
            {
                // �o�^���s
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();

                // ��ʓ��e�Ď擾
                // �\��f�[�^���o
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // �o�^�f�[�^�̎擾
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
            frm.setMode( "" );
            frm.setStatus( ReserveCommon.RSV_STATUS_CANCEL );

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
            throw new Exception( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * ���X�m�F�{�^���N���b�N����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws
     */
    public FormReserveSheetPC execRaiten(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        int addPoint = 0;
        int reflectDate = 0;
        int rsvDate = 0;
        int arrivalTime = 0;
        int addBonusMile = 0;
        LogicOwnerRsvCheckIn logicCheckIn = new LogicOwnerRsvCheckIn();
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();
        ReserveCommon rsvCmm = new ReserveCommon();

        try
        {
            Logging.info( "[HapiTouchRsvSub.exceRaiten Start]" );
            Logging.info( "[HapiTouchRsvSub.exceRaiten ] getSelHotelId()=" + frm.getSelHotelId() + ",getRsvDate()=" + frm.getRsvDate() + ",getSeq()=" + frm.getSeq() + ",getRsvNo()" + frm.getRsvNo() );

            // �V�K�o�^���ȊO�����f�[�^�̃X�e�[�^�X�̃`�F�b�N
            if ( rsvCmm.checkStatus( frm.getSelHotelId(), frm.getRsvNo(), status ) == false )
            {
                errMsg = Message.getMessage( "warn.00020" );
                frm.setErrMsg( errMsg );
                frm = getRaitenRegistData( frm );
                Logging.info( "[HapiTouchRsvSub.warn.00020 ] " + errMsg );
                return(frm);
            }

            // �����̖��I���`�F�b�N
            if ( frm.getSeq() == 0 )
            {
                errMsg = Message.getMessage( "warn.00002", "�`�F�b�N�C�����镔���ԍ�" );
                frm.setErrMsg( errMsg );
                frm = getRaitenRegistData( frm );
                Logging.info( "[HapiTouchRsvSub.warn.00002 ] " + errMsg );
                return(frm);
            }

            // �|�C���g�f�[�^�̐ݒ�
            addPoint = logicCheckIn.getRsvPointData( frm.getRsvNo(), 1 );
            rsvDate = logicCheckIn.getRsvPointData( frm.getRsvNo(), 2 );
            arrivalTime = logicCheckIn.getRsvPointData( frm.getRsvNo(), 3 );
            addBonusMile = logicCheckIn.getRsvPointData( frm.getRsvNo(), 4 );
            Logging.info( "[HapiTouchRsvSub.exceRaiten ] addPoint=" + addPoint + ",rsvDate=" + rsvDate + ",arrivalTime=" + arrivalTime + ",addBonusMile=" + addBonusMile );

            if ( frm.getExtFlag() == ReserveCommon.EXT_HAPIHOTE )
            {
                // �\��|�C���g�̓n�s�z�e�\��̂Ƃ��̂݃Z�b�g����
                frm.setAddPoint( addPoint );
            }
            frm.setAddBonusMile( addBonusMile );

            // ���f���̐ݒ�
            reflectDate = getReflectDate( rsvDate, arrivalTime );
            frm.setReflectDate( reflectDate );

            Logging.info( "[HapiTouchRsvSub.exceRaiten ] " + frm.getRsvNo() );

            // �f�[�^�X�V�i���X�m�F�j
            logicCheckIn.setFrm( frm );

            // �\��͐V�\��݂̂Ƃ��� 2015.12.16
            ret = logicCheckIn.execRaiten( frm.getSelHotelId(), frm.getRsvNo(), frm.getRsvDate(), frm.getRoomNo(), ReserveCommon.SCHEMA_NEWRSV );

            if ( ret == false )
            {
                // �o�^���s
                errMsg = frm.getErrMsg().trim();
                mode = frm.getMode();
                Logging.info( "[HapiTouchRsvSub.execRaiten() ] " + errMsg );

                // ��ʓ��e�Ď擾
                // �\��f�[�^���o
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );

                return(frm);
            }

            // �����������b�Z�[�W
            switch( status )
            {
                case 1:
                    // ��t
                    frm.setProcMsg( Message.getMessage( "warn.00024" ) );
                    break;
                case 2:
                    // ���p�ς�
                    if ( frm.getMode().equals( ReserveCommon.MODE_RAITEN ) )
                    {
                        // ���X�m�F����
                        frm.setProcMsg( Message.getMessage( "warn.00025" ) );
                    }
                    else
                    {
                        // ���p�������߂��Ă���ꍇ
                        frm.setProcMsg( Message.getMessage( "warn.00026" ) );
                    }
                    break;
            }
            // �o�^�f�[�^�̎擾
            frm = getRaitenRegistData( frm );
            Logging.info( "[HapiTouchRsvSub.exceRaiten End]" );

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.execRaiten() ] " + e.getMessage() );
            throw new Exception( "[HapiTouchRsvSub.execRaiten() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * �L�����[�U�|�C���g�ꎞ�f�[�^�̔��f���擾
     * 
     * @param int rsvDate �\���
     * @param int arrivalTime �����\�莞��
     * @return int ���f��
     * @throws Exception
     */
    public int getReflectDate(int rsvDate, int arrivalTime) throws Exception
    {
        int retDate = 0;
        int limitFlg = 0;
        int range = 0;
        String year = "";
        String month = "";
        String day = "";
        String rsvYear = "";
        String rsvMonth = "";
        String rsvDay = "";
        String rsvHour = "";
        String rsvMinutes = "";
        String rsvSecond = "";
        String arrivalTimeStr = "";
        Calendar calendar = Calendar.getInstance();

        // �|�C���g�Ǘ��}�X�^����f�[�^�擾
        limitFlg = OwnerRsvCommon.getInitHapyPoint( 3 );
        range = OwnerRsvCommon.getInitHapyPoint( 4 );

        // ���t�ݒ�
        rsvYear = Integer.toString( rsvDate ).substring( 0, 4 );
        rsvMonth = Integer.toString( rsvDate ).substring( 4, 6 );
        rsvDay = Integer.toString( rsvDate ).substring( 6, 8 );

        arrivalTimeStr = ConvertTime.convTimeStr( arrivalTime, 0 );
        rsvHour = arrivalTimeStr.substring( 0, 2 );
        rsvMinutes = arrivalTimeStr.substring( 2, 4 );
        rsvSecond = arrivalTimeStr.substring( 4 );
        calendar.set( Integer.parseInt( rsvYear ), Integer.parseInt( rsvMonth ) - 1, Integer.parseInt( rsvDay ),
                Integer.parseInt( rsvHour ), Integer.parseInt( rsvMinutes ), Integer.parseInt( rsvSecond ) );

        switch( limitFlg )
        {
            case OwnerRsvCommon.LIMIT_FLG_TIME:
                // ���ԉ��Z
                calendar.add( Calendar.HOUR, range );
                break;

            case OwnerRsvCommon.LIMIT_FLG_DAY:
                // ���t���Z
                calendar.add( Calendar.DATE, range );
                break;

            case OwnerRsvCommon.LIMIT_FLG_MONTH:
                // �����Z
                calendar.add( Calendar.MONTH, range );
                break;
        }

        year = Integer.toString( calendar.get( Calendar.YEAR ) );
        month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
        day = String.format( "%1$02d", calendar.get( Calendar.DATE ) );

        retDate = Integer.parseInt( year + month + day );

        return(retDate);
    }

    /**
     * ���X�m�F���̓o�^�f�[�^�擾����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws Exception
     */
    private FormReserveSheetPC getRaitenRegistData(FormReserveSheetPC frm) throws Exception
    {
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();

        // �o�^�f�[�^�̎擾
        logicSheet.setFrm( frm );
        logicSheet.getData( 2 );

        frm.setUserKbn( ReserveCommon.USER_KBN_OWNER );
        frm.setStatus( ReserveCommon.RSV_STATUS_ZUMI );
        frm.setMode( ReserveCommon.MODE_RAITEN );

        return(frm);
    }

    /**
     * �L�����Z���{�^���N���b�N����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws
     */
    public FormReserveSheetPC execUndoFix(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();

        try
        {

            // ���[�N�e�[�u���ɓo�^����Ă���ʏ�I�v�V�������擾
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // �f�[�^�X�V�i���X�m�F�����������j
            logic.setFrm( frm );
            ret = logic.execRsvUndoFix( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );

            if ( ret == false )
            {
                // �o�^���s
                errMsg = Integer.toString( HapiTouchErrorMessage.ERR_20904 );
                mode = frm.getMode();

                // ��ʓ��e�Ď擾
                // �\��f�[�^���o
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // �o�^�f�[�^�̎擾
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
            frm.setMode( "" );
            frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
            throw new Exception( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /**
     * �L�����Z���{�^���N���b�N����
     * 
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @param frm FormReserveSheetPC�I�u�W�F�N�g
     * @throws
     */
    public FormReserveSheetPC execUndoCancel(FormReserveSheetPC frm, int status) throws Exception
    {
        String errMsg = "";
        String mode = "";
        boolean ret = false;
        LogicOwnerRsvCheckIn logic;
        LogicReserveSheetPC logicSheet = new LogicReserveSheetPC();

        try
        {

            // ���[�N�e�[�u���ɓo�^����Ă���ʏ�I�v�V�������擾
            logic = new LogicOwnerRsvCheckIn();
            frm = logic.getOptionQuantity( frm );

            // �f�[�^�X�V�i�L�����Z�������������j
            logic.setFrm( frm );
            if ( frm.getAdultNum() == -1 ) // �V�\��
            {
                ret = logic.execRsvUndoCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_NEWRSV );
            }
            else
            {
                ret = logic.execRsvUndoCancel( ReserveCommon.TERM_KIND_PC, ReserveCommon.SCHEMA_OLDRSV );
            }
            if ( ret == false )
            {
                // �o�^���s
                errMsg = Integer.toString( HapiTouchErrorMessage.ERR_20704 );
                mode = frm.getMode();

                // ��ʓ��e�Ď擾
                // �\��f�[�^���o
                logicSheet.setFrm( frm );
                logicSheet.getData( 1 );
                frm = logicSheet.getFrm();
                frm.setErrMsg( errMsg );
                frm.setMode( mode );
                return(frm);
            }

            // �o�^�f�[�^�̎擾
            logicSheet = new LogicReserveSheetPC();
            logicSheet.setFrm( frm );
            logicSheet.getData( 1 );

            // �����������̓��[�h����ɂ���B(�ŏI�y�[�W�̂��߁A���[�h�͊֌W�Ȃ�����)
            frm.setMode( "" );
            frm.setStatus( ReserveCommon.RSV_STATUS_UKETUKE );

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
            throw new Exception( "[HapiTouchRsvSub.execCancel() ] " + e.getMessage() );
        }
        return(frm);
    }

    /****
     * �\��f�[�^�擾(�`�F�b�N�C���f�[�^�ɕR�t����j
     * 
     * @param userId
     * @param hotelId
     * @param hc
     * @return
     */
    public HotelCi getReserveData(String userId, int hotelId, HotelCi hc)
    {
        String roomNo = "";
        DataRsvReserveBasic drrb = new DataRsvReserveBasic();
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();

        // �t�H�[���ɃZ�b�g
        frm.setSelHotelId( hotelId );
        frm.setUserId( userId );

        drrb.getData( hotelId );
        // 5�����z���Ă��Ȃ��ꍇ�͐���Ƃ���
        if ( 50000 > Integer.parseInt( DateEdit.getTime( 1 ) ) )
        {
            frm.setRsvDate( DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 ) );
        }
        else
        {
            frm.setRsvDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }

        try
        {
            // �\��f�[�^���o
            logic.setFrm( frm );
            // �\��f�[�^�̎擾
            logic.getRsvData();
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getReserveData()] Exception:" + e.toString() );
        }

        // �\��f�[�^���擾���擾�ł��A�X�e�[�^�X�����X�҂��̏ꍇ�̂�
        if ( frm.getSeq() > 0 && frm.getStatus() == 1 )
        {
            DataHotelRoomMore dhrm = new DataHotelRoomMore();

            // �����ԍ������ɓ����Ă�����}�Ԓǉ��A�����Ă��Ȃ��ꍇ�͍X�V
            if ( hc.getHotelCi().getRoomNo().equals( "" ) == false )
            {
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
            }

            // �����ԍ����擾����
            if ( dhrm.getData( hotelId, frm.getSeq() ) != false )
            {
                roomNo = dhrm.getRoomNameHost();
            }
            else
            {
                DataHotelRoom dhr = new DataHotelRoom();
                if ( dhr.getData( hotelId, frm.getSeq() ) != false )
                {
                    roomNo = dhr.getRoomName();
                }
            }

            hc.getHotelCi().setRsvNo( frm.getRsvNo() );
            int extUserFlag = 0;
            UserBasicInfo ubi = new UserBasicInfo();
            if ( ubi.isLvjUser( userId ) )
            {
                extUserFlag = 1;
            }
            hc.getHotelCi().setExtUserFlag( extUserFlag );
            hc.getHotelCi().setUsePoint( frm.getUsedMile() );// �\�񎞂ɓ��͂����g�p�}�C�����Z�b�g 20150904

            // �����ԍ������ɓ����Ă�����}�Ԓǉ��A�����Ă��Ȃ��ꍇ�͍X�V
            if ( hc.getHotelCi().getRoomNo().equals( "" ) == false )
            {
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );

                hc.getHotelCi().insertData();
            }
            else
            {
                hc.getHotelCi().updateData( hotelId, hc.getHotelCi().getSeq(), hc.getHotelCi().getSubSeq() );
            }

            // ���߂�CI�f�[�^���擾
            hc.getCheckInBeforeData( hotelId, userId );
            hc.getHotelCi().setRoomNo( roomNo );
        }
        return hc;
    }

    /****
     * �\��f�[�^�擾(�`�F�b�N�C���f�[�^�ɕR�t����j
     * 
     * @param userId
     * @param hotelId
     * @param ciSeq
     * @return
     */
    public FormReserveSheetPC getReserveData(String userId, int hotelId, int ciSeq)
    {
        Logging.info( "HapiTouchRsvSub getReserveData(String userId, int hotelId, int ciSeq)" + userId + " " + hotelId + " " + ciSeq );
        DataRsvReserveBasic drrb = new DataRsvReserveBasic();
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();

        // �t�H�[���ɃZ�b�g
        frm.setSelHotelId( hotelId );
        frm.setUserId( userId );

        drrb.getData( hotelId );
        // 5�����z���Ă��Ȃ��ꍇ��1���O�̓��t�ɂ���
        if ( 50000 > Integer.parseInt( DateEdit.getTime( 1 ) ) )
        {
            frm.setRsvDate( DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 ) );
        }
        else
        {
            frm.setRsvDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        }

        frm.setCiSeq( ciSeq );

        try
        {
            // �\��f�[�^���o
            logic.setFrm( frm );
            // �\��f�[�^�̎擾
            logic.getRsvData();

            // �K�{�I�v�V�����f�[�^�擾
            logic.getRsvOptData( ReserveCommon.OPTION_IMP, 2 );

            // �ʏ�I�v�V�����f�[�^�擾
            logic.getRsvOptData( ReserveCommon.OPTION_USUAL, 2 );
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getReserveData()] Exception:" + e.toString() );
        }
        return frm;
    }

    /****
     * �\��f�[�^�擾(�`�F�b�N�C���f�[�^�ɕR�t����j
     * 
     * @param userId
     * @param hotelId
     * @param hc
     * @return
     */
    public FormReserveSheetPC getReserveData(String userId, int hotelId, String rsvNo)
    {
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();

        // �t�H�[���ɃZ�b�g
        frm.setSelHotelId( hotelId );
        frm.setUserId( userId );

        try
        {
            // �\��f�[�^���o
            logic.setFrm( frm );
            // �\��f�[�^�̎擾
            logic.getRsvData( rsvNo );

            // �K�{�I�v�V�����f�[�^�擾
            logic.getRsvOptData( ReserveCommon.OPTION_IMP, 2 );

            // �ʏ�I�v�V�����f�[�^�擾
            logic.getRsvOptData( ReserveCommon.OPTION_USUAL, 2 );
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getReserveData()] Exception:" + e.toString() );
        }
        return frm;
    }

    /****
     * ���X�L�����Z������
     * 
     * @param hotelId �z�e��ID
     * @param request ���N�G�X�g
     * @param kind ��ށi0:VisitCancel�A1:RsvUndoFix�j
     * @param response ���X�|���X
     */
    public HotelCi visitCancel(int hotelId, HotelCi hc, int kind)
    {
        int errorCode = 0;
        boolean ret;
        boolean retUse;
        boolean retData;
        String hotenaviId = "";
        DataUserFelica duf;
        DataHotelBasic dhb;
        UserPointPayTemp uppt;
        UserPointPay upp;

        duf = new DataUserFelica();
        uppt = new UserPointPayTemp();
        upp = new UserPointPay();
        dhb = new DataHotelBasic();
        ret = false;
        retUse = false;
        retData = false;

        // ���X�|���X���Z�b�g
        try
        {
            if ( hc.getHotelCi().getCiStatus() == 0 )
            {
                duf.getData( hc.getHotelCi().getUserId() );

                // �z�e�i�rID���擾����
                dhb.getData( hotelId );
                if ( dhb.getId() > 0 )
                {
                    hotenaviId = dhb.getHotenaviId();
                }

                // �|�C���g�������Ă�����}�C���g�p
                // if ( hc.getHotelCi().getUsePoint() > 0 && hc.getHotelCi().getRsvNo().equals( "" ) )
                if ( hc.getHotelCi().getUsePoint() > 0 )
                {
                    // ���Ɏg�p�}�C���f�[�^�����邩�ǂ������m�F����
                    retData = uppt.getUserPointHistory( hc.getHotelCi().getUserId(), hc.getHotelCi().getId(), POINT_KIND_WARIBIKI, hc.getHotelCi().getUserSeq(), hc.getHotelCi().getVisitSeq() );
                    if ( retData != false )
                    {
                        // �f�[�^�����邽�߁A�g�p�}�C�����X�V
                        retUse = uppt.cancelUsePoint( hc.getHotelCi(), POINT_KIND_WARIBIKI, hc.getHotelCi().getUseEmployeeCode() );
                        if ( retUse != false )
                        {
                            // hh_user_point_pay�Ɏg�p�}�C���̒ǉ�
                            retUse = upp.cancelUsePoint( hc.getHotelCi(), POINT_KIND_WARIBIKI, hc.getHotelCi().getUseEmployeeCode() );
                            if ( retUse == false )
                            {
                                // RsvUndoFix�G���[
                                if ( kind == 1 )
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_20908;
                                }
                                // VisitCancel�G���[
                                else
                                {
                                    errorCode = HapiTouchErrorMessage.ERR_10803;
                                }
                            }
                        }
                        else
                        {
                            // RsvUndoFix�G���[
                            if ( kind == 1 )
                            {
                                errorCode = HapiTouchErrorMessage.ERR_20908;
                            }
                            // VisitCancel�G���[
                            else
                            {
                                errorCode = HapiTouchErrorMessage.ERR_10803;
                            }
                        }
                    }
                    else
                    {
                        // �\��ԍ��������Ă��Ȃ��ꍇ
                        if ( hc.getHotelCi().getRsvNo().equals( "" ) != false )
                        {
                            // RsvUndoFix�G���[
                            if ( kind == 1 )
                            {
                                errorCode = HapiTouchErrorMessage.ERR_20907;
                            }
                            // VisitCancel�G���[
                            else
                            {
                                errorCode = HapiTouchErrorMessage.ERR_10804;
                            }
                        }
                    }
                    if ( errorCode > 0 )
                    {
                        retData = false;
                    }
                    else
                    {
                        retData = true;
                    }
                }
                else
                {
                    retData = true;
                }
            }
            else if ( hc.getHotelCi().getCiStatus() == 4 )// �n�s�z�e�ȊO�̗��X
            {
                retData = true;
            }

            if ( retData != false )
            {
                hc.getHotelCi().setUserId( hc.getHotelCi().getUserId() );
                hc.getHotelCi().setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                hc.getHotelCi().setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

                if ( retUse != false )
                {
                    // HotelCi��o�^����
                    // �|�C���g�̕����𔽓]�����ēo�^
                    if ( hc.getHotelCi().getUsePoint() >= 0 )
                    {
                        hc.getHotelCi().setUsePoint( Math.abs( hc.getHotelCi().getUsePoint() ) * -1 );
                    }
                    else
                    {
                        hc.getHotelCi().setUsePoint( Math.abs( hc.getHotelCi().getUsePoint() ) );
                    }
                    hc.getHotelCi().setUseDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    hc.getHotelCi().setUseTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    hc.getHotelCi().setUseHotenaviId( hotenaviId );
                    hc.getHotelCi().setUseEmployeeCode( hc.getHotelCi().getUseEmployeeCode() );
                }
                if ( hc.getHotelCi().getCiStatus() == 4 )// �O���̗\��f�[�^�͖����f�[�^�ɂ���B
                {
                    hc.getHotelCi().setCiStatus( 3 );

                }
                else
                {
                    hc.getHotelCi().setCiStatus( 2 );
                }
                hc.getHotelCi().setSubSeq( hc.getHotelCi().getSubSeq() + 1 );
                ret = hc.getHotelCi().insertData();
                if ( ret == false )
                {
                    // �`�F�b�N�C���f�[�^�ǉ��G���[
                    if ( kind == 1 )
                    {
                        errorCode = HapiTouchErrorMessage.ERR_20907;
                    }
                    else
                    {
                        errorCode = HapiTouchErrorMessage.ERR_10802;
                    }
                }
                else
                {
                    // �S�}�C���g�p���̓o�b�N�I�t�B�X�̃f�[�^�͒ǉ������Ȃ��i�S�}�C���g�p���͗����m�莞�ɏ�������ł��邽�߁j
                    if ( retUse != false && hc.getHotelCi().getAllUseFlag() == 0 )
                    {
                        // �o�b�N�I�t�B�X�̃f�[�^��ǉ�
                        // retData = bko.addBkoData( hc.getHotelCi().getUserId(), hotelId, POINT_KIND_WARIBIKI, hc.getHotelCi() );
                        // if ( retData == false )
                        // {
                        // errorCode = HapiTouchErrorMessage.ERR_10805;
                        // }
                    }
                }
            }
            else
            {

                if ( kind == 1 )
                {
                    errorCode = HapiTouchErrorMessage.ERR_20911;
                }
                else
                {
                    errorCode = HapiTouchErrorMessage.ERR_10806;
                }
            }
            hc.setErrorMsg( errorCode );
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub visitCancel]Exception:" + e.toString() );
        }
        return hc;
    }

    /**
     * ���̌�╔���擾
     * 
     * @param id�@�z�e��ID
     * @param planId�@�v����ID
     * @param date�@�Ώۓ�
     * @return
     */
    public String[] getAnotherRoom(int id, int planId, int planSubId, int date, String touchRoom, String reserveNo)
    {
        int i = 0;
        int count = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        String[] roomNoList = null;

        String query = "SELECT C.room_name_host FROM newRsvDB.hh_rsv_rel_plan_room A";
        query += " INNER JOIN newRsvDB.hh_rsv_room_remainder B";
        query += "    ON A.id = B.id AND A.seq = B.seq AND B.cal_date = ? AND (B.stay_reserve_no='' OR B.stay_reserve_no = ?)";
        query += " INNER JOIN hh_hotel_room_more C ";
        query += "    ON A.id = C.id AND A.seq = C.seq ";
        query += " WHERE A.id = ?";
        query += " AND A.plan_id = ?";
        query += " AND A.plan_sub_id = ?";
        if ( touchRoom.equals( "" ) == false ) // �t�����g�łȂ������ꍇ
        {
            query += " AND C.room_name_host ='" + touchRoom + "'";
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            prestate.setString( 2, reserveNo );
            prestate.setInt( 3, id );
            prestate.setInt( 4, planId );
            prestate.setInt( 5, planSubId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                    result.beforeFirst();
                }

                roomNoList = new String[count];
                while( result.next() != false )
                {
                    roomNoList[i] = result.getString( "room_name_host" );
                    i++;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getAnotherRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return roomNoList;
    }

    public String[] getAnotherReserveRoom(int id, int planId, int planSubId, int date, String reserveRoom, String reserveNo)
    {
        int room_select_kind = 3;
        DataHhRsvPlan plan = new DataHhRsvPlan();
        if ( plan.getData( id, planId, planSubId ) != false )
        {
            room_select_kind = plan.getRoomSelectKind();
        }
        int i = 0;
        int count = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        String[] roomNoList = null;

        String query = "SELECT C.room_name_host FROM newRsvDB.hh_rsv_rel_plan_room A";
        query += " INNER JOIN newRsvDB.hh_rsv_room_remainder B";
        query += "    ON A.id = B.id AND A.seq = B.seq AND B.cal_date = ? AND (B.stay_reserve_no='' OR B.stay_reserve_no = ?)";
        query += " INNER JOIN hh_hotel_room_more C ";
        query += "    ON A.id = C.id AND A.seq = C.seq ";
        query += " WHERE A.id = ?";
        query += " AND A.plan_id = ?";
        query += " AND A.plan_sub_id = ?";
        query += " AND C.room_name_host <>'" + reserveRoom + "'";
        if ( room_select_kind == 1 )
        {
            query += "  AND C.room_rank = (SELECT room_rank FROM hh_hotel_room_more M WHERE M.id= ? AND M.room_name_host='" + reserveRoom + "')";
        }
        else if ( room_select_kind == 2 )
        {
            query += "  AND C.room_name_host ='" + reserveRoom + "'";
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            prestate.setString( 2, reserveNo );
            prestate.setInt( 3, id );
            prestate.setInt( 4, planId );
            prestate.setInt( 5, planSubId );
            if ( room_select_kind == 1 )
            {
                prestate.setInt( 6, id );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                    result.beforeFirst();
                }

                roomNoList = new String[count];
                while( result.next() != false )
                {
                    roomNoList[i] = result.getString( "room_name_host" );
                    i++;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchRsvSub.getAnotherReserveRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return roomNoList;
    }

    public int setRsvPoint(HotelCi hc)
    {
        int errCode = 0;
        boolean ret = false;
        UserPointPayTemp uppt = new UserPointPayTemp();
        UserPointPay upp = new UserPointPay();
        HapiTouchBko bko = new HapiTouchBko();

        // �\��̃|�C���g������id=0�œo�^����邽��
        ret = uppt.getUserPointHistoryByRsvNo( hc.getHotelCi().getUserId(), hc.getHotelCi().getId(), POINT_KIND_YOYAKU, hc.getHotelCi().getRsvNo() );
        if ( ret != false )
        {
            // ���X�񐔁A���[�U�Ǘ��ԍ��Ȃǂ��Z�b�g����
            uppt.getUserPoint()[0].setIdm( hc.getHotelCi().getIdm() );
            uppt.getUserPoint()[0].setUserSeq( hc.getHotelCi().getUserSeq() );
            uppt.getUserPoint()[0].setVisitSeq( hc.getHotelCi().getVisitSeq() );
            uppt.getUserPoint()[0].setThenPoint( upp.getNowPoint( hc.getHotelCi().getUserId(), false ) );
            uppt.getUserPoint()[0].setHotenaviId( hc.getHotelCi().getVisitHotenaviId() );
            // �����ԍ��Ȃ�
            uppt.getUserPoint()[0].setSlipNo( hc.getHotelCi().getSlipNo() );
            uppt.getUserPoint()[0].setAmount( hc.getHotelCi().getAmount() );
            uppt.getUserPoint()[0].setRoomNo( hc.getHotelCi().getRoomNo() );
            uppt.getUserPoint()[0].setAddFlag( 1 );
            // update
            ret = uppt.getUserPoint()[0].updateData( hc.getHotelCi().getUserId(), uppt.getUserPoint()[0].getSeq() );
            if ( ret != false )
            {
                // �\��}�C���̃Z�b�g
                upp.setVisitPoint( hc.getHotelCi().getUserId(), RSV_POINT, upp.getNowPoint( hc.getHotelCi().getUserId(), false ), 0, hc.getHotelCi() );

                ret = bko.addBkoData( hc.getHotelCi().getUserId(), hc.getHotelCi().getId(), POINT_KIND_YOYAKU, hc.getHotelCi() );
                if ( ret == false )
                {
                    // �o�b�N�I�t�B�X�ǉ��G���[
                    errCode = HapiTouchErrorMessage.ERR_20406;
                }
            }
            else
            {
                // �|�C���g�������X�V�ł��Ȃ��������߃G���[
                errCode = HapiTouchErrorMessage.ERR_20405;
            }
        }
        else
        {
            // ���X�m�莞�ɒǉ����ꂽ�f�[�^���Ȃ����߃G���[
            errCode = HapiTouchErrorMessage.ERR_20404;
        }
        return errCode;
    }

    /****
     * �e�\��ԍ��擾
     * 
     * @param hotelId
     * @param rsvNo
     * @return
     */
    public String getMainReserveNo(int hotelId, String rsvNoMain)
    {
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String reserveNo = "";

        String query = "SELECT * FROM newRsvDB.hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ?"
                + " AND SUBSTRING(reserve_no,INSTR(reserve_no,'-') + 1) = ? ORDER BY reserve_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, rsvNoMain );
            prestate.setString( 3, rsvNoMain );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    reserveNo = result.getString( "reserve_no" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiToucnRsvSub.getMainReserveNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return reserveNo;
    }

    public int[] getChargeList(int hotelId, String rsvNoMain)
    {
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int[] chargeList = null;
        int i = -1;

        String query = "SELECT charge_total FROM newRsvDB.hh_rsv_reserve  WHERE id = ? AND reserve_no_main = ? ORDER BY reserve_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, rsvNoMain );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    // �������̋��z������������B
                    chargeList = new int[result.getRow()];
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    i++;
                    chargeList[i] = result.getInt( "charge_total" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiToucnRsvSub.getMainReserveNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return chargeList;
    }

}
