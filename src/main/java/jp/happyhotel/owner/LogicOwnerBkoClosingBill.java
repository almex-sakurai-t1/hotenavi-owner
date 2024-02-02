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

    // �����E�x���i�\��j�@�p�[�Z���e�[�W(%)
    private static final int    PERCENT_RESERVE         = 5;
    private static final int    PERCENT_NEW_RESERVE     = 8;
    private static final int    PERCENT_NEW_RESERVE_LVJ = 10;

    // �ŏI�X�V��
    private int                 lastUpdate              = 0;

    // �ŏI�X�V����
    private int                 lastUptime              = 0;

    // �x��������
    private int                 paymentDate             = 0;

    private static final String CONF_FILE               = "/etc/happyhotel/backoffice.conf";
    private static final String MAIL_TO                 = "mail.to";
    private static final String MAIL_FROM               = "mail.from";
    private static final String MAIL_ERRORTITLE         = "mail.errortitle";
    private static final String MAIL_SUCCESSTITLE       = "mail.successtitle";

    // �������ԍ��擪����
    private static final String BILL_NO_HEADER          = "641";

    // 20110808 �폜���鐿���f�[�^�̐����`�[�ԍ���ێ� START
    Hashtable<String, Integer>  hashBill                = new Hashtable<String, Integer>();
    int                         monthStartDay           = 0;                                // ���ߔN���J�n��
    int                         monthEndDay             = 0;                                // ���ߔN���I����

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
            // �����`�F�b�N
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

            // 2:������ or 3:�{���� �ȊO�̏ꍇ�̓G���[
            if ( !(OwnerBkoCommon.CLOSING_KBN_KARI == closingKbn || OwnerBkoCommon.CLOSING_KBN_HON == closingKbn) )
            {
                Logging.info( "[LogicOwnerBkoClogingBill] Parameter closing_kind Error" );
                return false;
            }

            // ���ߔN�������݂���N�����`�F�b�N
            if ( !DateEdit.checkDate( String.valueOf( closingDate ).substring( 0, 4 ) + "/" + String.valueOf( closingDate ).substring( 4, 6 ) + "/01" ) )
            {
                Logging.info( "[LogicOwnerBkoClogingBill] Parameter closing_date Error" + closingDate );
                return false;
            }

            // �����߂̏ꍇ�́A�ΏۊJ�n���A�ΏۏI�����̑Ó����`�F�b�N�����{
            if ( OwnerBkoCommon.CLOSING_KBN_KARI == closingKbn )
            {
                // �ΏۊJ�n�������݂���N�������`�F�b�N
                if ( !DateEdit.checkDate( String.valueOf( startDay ).substring( 0, 4 ) + "/" + String.valueOf( startDay ).substring( 4, 6 ) + "/" + String.valueOf( startDay ).substring( 6, 8 ) ) )
                {
                    Logging.info( "[LogicOwnerBkoClogingBill] Parameter startDay Error" + startDay );
                    return false;
                }

                // �ΏۏI���������݂���N�������`�F�b�N
                if ( !DateEdit.checkDate( String.valueOf( endDay ).substring( 0, 4 ) + "/" + String.valueOf( endDay ).substring( 4, 6 ) + "/" + String.valueOf( endDay ).substring( 6, 8 ) ) )
                {
                    Logging.info( "[LogicOwnerBkoClogingBill] Parameter endDay Error" + endDay );
                    return false;
                }

                // �ΏۊJ�n�����ΏۏI�����ł��邩�`�F�b�N
                if ( startDay >= endDay )
                {
                    Logging.info( "[LogicOwnerBkoClogingBill] Parameter startDay,endDay Error" + startDay + "," + endDay );
                    return false;
                }
            }

            lastUpdate = Integer.parseInt( DateEdit.getDate( 2 ) );
            lastUptime = Integer.parseInt( DateEdit.getTime( 1 ) );

            // ALMEX���Œǉ�
            // 20110808
            if ( Integer.parseInt( DateEdit.getDate( 2 ) ) % 100 > OwnerBkoCommon.SIME_DATE + 2 )
            {
                // 20110913 12���𒴂��Ă���ꍇ�A���ߔN���� +1 �ɂ���
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

                // �������̎擾
                paymentDate = getPaymentDate( prestate, connection );
                // System.out.println("paymentDate:" + paymentDate);
                if ( paymentDate == -1 )
                {
                    Logging.error( "[LogicOwnerBkoClogingBill] �x���������̎擾���ł��܂���" );
                    return false;
                }

                controlChangeFlg = true;

                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );

                // ���ߔN���J�n�E�I�����Z�b�g
                setMonthStartDate( closingDate );

                // ������
                if ( OwnerBkoCommon.CLOSING_KBN_KARI == closingKbn )
                {
                    query = "SELECT ";
                    query = query + "bill_slip_no, "; // �����`�[No
                    query = query + "bill_cd, "; // ������R�[�h
                    query = query + "id "; // �z�e��ID
                    query = query + "FROM hh_bko_bill ";
                    query = query + "WHERE closing_kind BETWEEN 1 AND 2 "; // ������
                    query = query + "AND bill_date = ? ";

                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        while( result.next() )
                        {
                            // �l�̊i�[
                            hashBill.put( String.valueOf( result.getInt( "bill_cd" ) ) + "-" + String.valueOf( result.getInt( "id" ) ), result.getInt( "bill_slip_no" ) );
                        }
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    // �������׃f�[�^�폜
                    query = "DELETE hh_bko_bill_detail.* ";
                    query = query + "FROM hh_bko_bill_detail ";
                    query = query + "INNER JOIN hh_bko_bill ";
                    query = query + "ON hh_bko_bill.bill_slip_no = hh_bko_bill_detail.bill_slip_no ";
                    query = query + "WHERE hh_bko_bill.bill_date = ? ";
                    query = query + "AND hh_bko_bill_detail.closing_kind BETWEEN 1 AND 2 "; // ������

                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    prestate.executeUpdate();
                    DBConnection.releaseResources( prestate );

                    // �����E���|�f�[�^�폜
                    query = "DELETE hh_bko_rel_bill_account_recv ";
                    query = query + "FROM hh_bko_rel_bill_account_recv ";
                    query = query + "INNER JOIN hh_bko_bill ";
                    query = query + "ON hh_bko_bill.bill_slip_no = hh_bko_rel_bill_account_recv.bill_slip_no ";
                    query = query + "WHERE hh_bko_bill.bill_date = ? ";
                    query = query + "AND hh_bko_rel_bill_account_recv.closing_kind BETWEEN 1 AND 2 "; // ������

                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    prestate.executeUpdate();
                    DBConnection.releaseResources( prestate );

                    // �����f�[�^�폜�i�������ׁA�����E���|�f�[�^�������Ă���Ō�ɏ����j
                    query = "DELETE FROM hh_bko_bill ";
                    query = query + "WHERE closing_kind BETWEEN 1 AND 2 "; // ������
                    query = query + "AND bill_date = ? ";
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, closingDate );
                    prestate.executeUpdate();
                    DBConnection.releaseResources( prestate );

                    // ���ߐ���f�[�^(hh_bko_closing_control)����last_update���擾
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

                    // �����߂̂Ƃ�
                    if ( closingKind <= 2 && hbccLastUpdate > 0 )
                    {

                        // ���|�f�[�^���׍폜
                        query = "DELETE hh_bko_account_recv_detail FROM hh_bko_account_recv_detail ";
                        query = query + "INNER JOIN  hh_bko_account_recv  ";
                        query = query + "ON hh_bko_account_recv.accrecv_slip_no = hh_bko_account_recv_detail.accrecv_slip_no ";
                        query = query + "WHERE hh_bko_account_recv.add_up_date = ? ";
                        query = query + "AND hh_bko_account_recv.closing_kind BETWEEN 1 AND 2 "; // ������
                        prestate = connection.prepareStatement( query );
                        prestate.setInt( 1, hbccLastUpdate );
                        prestate.executeUpdate();
                        DBConnection.releaseResources( prestate );

                        // ���|�f�[�^�폜
                        query = "DELETE FROM hh_bko_account_recv ";
                        query = query + "WHERE hh_bko_account_recv.add_up_date = ? ";
                        query = query + "AND hh_bko_account_recv.closing_kind BETWEEN 1 AND 2 "; // ������
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

                    // �\��i�����q�萔�� �|�C���g�͏�ō쐬�ς݁j
                    query = "SELECT ";
                    query = query + "hotelbasic.hotenavi_id, "; // �z�e�i�rID
                    query = query + "reserve.user_id, "; // ���[�UID
                    query = query + "reserve.charge_total, "; // �����v
                    query = query + "reserve.add_bonus_mile, "; // �{�[�i�X�}�C��
                    query = query + "result.seq, "; // �Ǘ��ԍ�
                    query = query + "hotelbasic.id, "; // �z�e��ID
                    query = query + "result.reserve_no, "; // �\��ԍ�
                    query = query + "reserve.reserve_no_main, "; // �e�\��ԍ�
                    query = query + "result.regist_date, "; // �o�^���t�i���p���j
                    query = query + "result.regist_time, "; // �o�^���ԁi���p�����j
                    query = query + "result.ci_date, "; // �\���
                    query = query + "basic.hotel_id, "; // �I�[�i�[�z�e��ID
                    query = query + "basic.user_id, "; // ���[�UID
                    query = query + "billingHotel.bill_cd, "; // ������R�[�h
                    query = query + "reserve.ext_flag, "; // �V�\��(ext_flag=0�̂Ƃ��n�s�z�e�\�� ext_flag=1�̂Ƃ����u�C���W���p���\��)
                    query = query + "reserve.used_mile, "; // �g�p�}�C��
                    query = query + "reserve.noshow_flag, ";
                    query = query + "reserve.payment, ";
                    query = query + "reserve.cancel_charge, "; // �L�����Z����
                    query = query + "reserve.status, "; // �X�e�[�^�X
                    query = query + "CASE WHEN userDataIndex.user_seq IS NULL THEN 0 ELSE userDataIndex.user_seq END AS user_seq ";
                    query = query + "FROM hh_rsv_result result ";
                    query = query + "INNER JOIN newRsvDB.hh_rsv_reserve reserve ";
                    query = query + "ON result.id = reserve.id ";
                    query = query + "AND result.reserve_no = reserve.reserve_no ";
                    query = query + "AND ( reserve.status = 2 OR ( reserve.payment=1 AND reserve.noshow_flag=1 ) OR (reserve.status = 3 AND reserve.ext_flag >= 1) ) "; // �`�F�b�N�C���܂��̓L�����Z���܂��̓m�[�V���[���A�N���W�b�g�����グ�ς�
                    query = query + "INNER JOIN hh_rsv_reserve_basic basic ";
                    query = query + "ON result.id = basic.id ";
                    query = query + "INNER JOIN hh_hotel_basic hotelbasic "; // �z�e����{�f�[�^
                    query = query + "ON hotelbasic.id = basic.id ";

                    query = query + "INNER JOIN hh_bko_rel_billing_hotel billingHotel ";// ������E�z�e��
                    query = query + "ON billingHotel.id = hotelbasic.id ";
                    query = query + "LEFT JOIN hh_user_data_index userDataIndex ";
                    query = query + "ON userDataIndex.user_id = reserve.user_id ";
                    query = query + "AND userDataIndex.id = reserve.id ";
                    query = query + "WHERE result.regist_date * 1000000 + result.regist_time >=  ? * 1000000 + basic.deadline_time ";
                    query = query + "AND result.regist_date * 1000000 + result.regist_time < ? * 1000000 + basic.deadline_time ";
                    // ���|�f�[�^�쐬�i�\�񕪁i�����q�萔���i�|�C���g�͏�ł��j�j�j
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, startDay );
                    prestate.setInt( 2, endDay );

                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        int accrecvSlipNo = -1;
                        UserBasicInfo ubi = new UserBasicInfo();

                        // �\��i���q�萔���j
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

                            // ���|�f�[�^�o�^
                            dataAcc = new DataBkoAccountRecv();
                            dataAcc.setHotelId( result.getString( "hotenavi_id" ) ); // �z�e��ID�i�z�e�i�r�j
                            dataAcc.setId( result.getInt( "id" ) ); // �z�e��ID�i�n�s�z�e�j
                            dataAcc.setAddUpDate( lastUpdate ); // �v���
                            dataAcc.setBillCd( result.getInt( "bill_cd" ) );
                            dataAcc.setPersonName( "" ); // ������R�[�h
                            if ( ubi.isLvjUser( result.getString( "reserve.user_id" ) ) )
                            {
                                dataAcc.setUserManagementNo( 0 ); // ���[�U�Ǘ��ԍ�
                            }
                            else
                            {
                                dataAcc.setUserManagementNo( result.getInt( "user_seq" ) ); // ���[�U�Ǘ��ԍ�
                            }
                            String rsvNo = result.getString( "reserve_no" );
                            String revNoMain = result.getString( "reserve_no_main" );
                            dataAcc.setUsageDate( result.getInt( "ci_date" ) ); // ���p��
                            if ( !StringUtils.isBlank( revNoMain ) && !rsvNo.substring( rsvNo.indexOf( "-" ) + 1 ).equals( revNoMain ) )
                            {
                                // �A���\��̓r���̏h����
                                dataAcc.setUsageTime( 999999 ); // ���p����
                            }
                            else
                            {
                                // �ꔑ��
                                dataAcc.setUsageTime( result.getInt( "regist_time" ) ); // ���p����
                                if ( result.getInt( "ci_date" ) < result.getInt( "regist_date" ) )
                                {
                                    dataAcc.setUsageTime( result.getInt( "regist_time" ) + 240000 ); // ���p����
                                }
                            }
                            dataAcc.setHtSlipNo( 0 ); // �`�[No�i�n�s�^�b�`�j
                            dataAcc.setHtRoomNo( Integer.toString( result.getInt( "seq" ) ) ); // �����ԍ��i�n�s�^�b�`�j
                            dataAcc.setUsageCharge( 0 ); // ���p���z
                            dataAcc.setReceiveCharge( result.getInt( "charge_total" ) ); // �\����z
                            if ( result.getInt( "used_mile" ) > 0 && result.getInt( "noshow_flag" ) == 1 && result.getInt( "payment" ) == 1 )
                            {
                                dataAcc.setAccrecvAmount( amount - result.getInt( "used_mile" ) ); // ���|���z
                                dataAcc.setAccrecvBalance( amount - result.getInt( "used_mile" ) ); // ���|�c
                            }
                            else
                            {
                                dataAcc.setAccrecvAmount( amount ); // ���|���z
                                dataAcc.setAccrecvBalance( amount ); // ���|�c
                            }

                            dataAcc.setHappyBalance( 0 ); // �n�s�[�c��
                            dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// ���ߏ����敪
                            dataAcc.setLastUpdate( lastUpdate ); // �ŏI�X�V��
                            dataAcc.setLastUptime( lastUptime ); // �ŏI�X�V����
                            OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );

                            // �����̔Ԃ��ꂽ���|�`�[No�擾
                            accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, connection );

                            // ���|���׃f�[�^�o�^
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
                            dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// ���ߏ����敪
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

                    // �����f�[�^�쐬�O�ɃR�~�b�g
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

                    // �����f�[�^�쐬
                    // �ΏۂƂȂ锄�|�f�[�^�擾
                    query = "SELECT DISTINCT header.accrecv_slip_no, ";
                    query = query + "header.hotel_id, ";
                    query = query + "header.id, ";
                    query = query + "header.bill_cd, ";
                    query = query + "header.accrecv_amount, "; // ���|���z
                    query = query + "header.credit_note_flag, "; // �ԓ`�t���O
                    query = query + "header.owner_hotel_id, "; // �I�[�i�[�z�e��ID
                    query = query + "header.owner_user_id, "; // �I�[�i�[���[�UID
                    query = query + "detail.slip_detail_no, "; // �`�[����No
                    query = query + "detail.account_title_cd, "; // ���|�R�[�h
                    query = query + "ifnull(basic.name, '') as name ";

                    query = query + "FROM hh_bko_account_recv header ";

                    query = query + "INNER JOIN hh_bko_account_recv_detail detail ";
                    query = query + "ON header.accrecv_slip_no = detail.accrecv_slip_no ";

                    query = query + "LEFT JOIN hh_hotel_basic basic ";
                    query = query + "ON basic.id = header.id ";

                    query = query + "INNER JOIN hh_rsv_reserve_basic reservebasic ";
                    query = query + "ON header.id = reservebasic.id ";

                    query = query + "WHERE header.closing_kind <> 3 ";

                    // ALMEX�ǉ��@�`�F�b�N�C�����t��24���Ԉȏ�O�̃f�[�^���X�V�Ώ�
                    query = query + " AND header.usage_date * 1000000 + header.usage_time <   ? * 1000000 + reservebasic.deadline_time ";

                    // ���z��0�̏ꍇ�͒��ߑΏۂƂ��Ȃ�
                    query = query + " AND header.accrecv_amount <> 0";

                    query = query + " ORDER BY header.id,header.hotel_id,header.accrecv_slip_no,detail.slip_detail_no ";

                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, monthEndDay );

                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        // �����f�[�^��o�^���Ă���
                        String wkHotelId = "-1";
                        int wkId = -1;
                        int detailNo = 0;
                        int sumCharge = 0; // �ō������v
                        int sumChargeNotInc = 0; // �Ŕ������v
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

                                    // �z�e���������Ȃ�X�V
                                    // �����f�[�^�X�V
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
                                // �z�e�����ς������u���C�N
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
                                // �����f�[�^�o�^
                                registBill( prestate, connection, result.getInt( "id" ), result.getString( "name" ), result.getInt( "bill_cd" ), closingDate, sumCharge, sumChargeNotInc, result.getString( "owner_hotel_id" ),
                                        result.getInt( "owner_user_id" ), OwnerBkoCommon.CLOSING_KBN_MISYORI );

                                if ( hashBill.containsKey( String.valueOf( result.getInt( "bill_cd" ) ) + "-" + String.valueOf( result.getInt( "id" ) ) ) )
                                {
                                    billSlipNo = hashBill.get( String.valueOf( result.getInt( "bill_cd" ) ) + "-" + String.valueOf( result.getInt( "id" ) ) );
                                }
                                else
                                {
                                    // �����̔Ԃ��ꂽ�����`�[No�擾
                                    billSlipNo = getInsertedBillSlipNo( prestate, connection );
                                }

                                // �������ԍ��X�V
                                updateBillNo( prestate, connection, billSlipNo );

                                detailNo = 1;
                            }

                            // if ( result.getInt( "slip_detail_no" ) == 1 )
                            if ( wkSlipNo != result.getInt( "accrecv_slip_no" ) )
                            {

                                // �����E���|�f�[�^�o�^
                                OwnerBkoCommon.RegistRelBillAccountRecv( prestate, connection, billSlipNo, detailNo, result.getInt( "accrecv_slip_no" ), OwnerBkoCommon.CLOSING_KBN_MISYORI );

                            }
                            wkId = result.getInt( "id" );
                            wkHotelId = result.getString( "hotel_id" );
                            wkSlipNo = result.getInt( "accrecv_slip_no" );
                        }
                    }

                    // �������׃f�[�^�쐬�O�ɃR�~�b�g
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

                    // �������׃f�[�^�쐬
                    // ret = OwnerBkoCommon.RegistBillDetail( prestate, connection, 0 );
                    OwnerBkoCommon.RegistBillDetail( prestate, connection, 0 );

                    if ( ret )
                    {
                        // �������߁i���ߏ����敪�̍X�V�j
                        ret = updateClosingKind( prestate, connection, OwnerBkoCommon.CLOSING_KBN_MISYORI, OwnerBkoCommon.CLOSING_KBN_KARI, closingDate );
                    }
                }
                else
                {
                    // ���{���߁i���ߏ����敪�̍X�V�j
                    ret = updateClosingKind( prestate, connection, OwnerBkoCommon.CLOSING_KBN_KARI, OwnerBkoCommon.CLOSING_KBN_HON, closingDate );
                    Logging.info( "�{����:" + ret, "[LogicOwnerBkoClogingBill]" );
                }

                if ( ret )
                {
                    System.out.println( "COMMIT4" );
                    query = "COMMIT ";
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );

                    String msg = Message.getMessage( "info.00004", "���ߏ���" );
                    if ( OwnerBkoCommon.CLOSING_KBN_HON == closingKbn )
                    {
                        msg = msg + "�i�{���߁j";
                    }
                    else
                    {
                        msg = msg + "�i�����߁j";
                    }

                    // �������[�����M
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
                // �������r���ŃG���[�ɂȂ����ꍇ�A���ߐ���f�[�^�͖߂�����߂�
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
                        // �{����
                        // ���ߐ���f�[�^���u��~�v�ɖ߂�
                        query = "UPDATE hh_bko_closing_control ";
                        query = query + "SET exec_flag = 0, "; // 0:��~
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
                        // ������
                        // ���ߐ���f�[�^�Ɂu�������F���s���v�f�[�^���폜
                        query = "DELETE FROM hh_bko_closing_control ";
                        query = query + "WHERE closing_date = ? ";
                        query = query + "AND closing_kind = 1 "; // 1:������
                        query = query + "AND exec_flag = 1 "; // 1:���s��

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

                    // ���ߐ���f�[�^�X�V���R�~�b�g
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
                // ���ɃG���[�Ȃ̂ł����ł͉������Ȃ�
            }

            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * ���ߔN���J�n���E�I�����Z�b�g
     * 
     * @param closingDate ���ߔN��
     * @return true:����Afalse:���s
     */
    private boolean setMonthStartDate(int closingDate)
    {
        // �������͈̔͂��v�Z �i�O���̒��ߓ��̗����`�����̒��ߓ��̗����j
        Calendar cal = Calendar.getInstance();

        cal.set( Calendar.YEAR, (closingDate / 100) );
        cal.set( Calendar.MONTH, (closingDate % 100) - 1 );
        cal.set( Calendar.DATE, OwnerBkoCommon.SIME_DATE ); // ��:20110410

        // �����Ώۓ��͒��ߓ��̗���
        cal.add( Calendar.DATE, 1 ); // ��:20110411

        // �ΏۏI����
        monthEndDay = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

        // �ΏۊJ�n��
        cal.add( Calendar.MONTH, -1 ); // ��:20110311
        monthStartDay = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

        return true;
    }

    /**
     * ���ߏ����敪�X�V
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param whereClosingKind �C���Ώے��ߏ����敪
     * @param setClosingKind �X�V����ߏ����敪
     * @param closingDate ���ߏ����N��
     * @return true:����Afalse:���s
     */
    private boolean updateClosingKind(PreparedStatement prestate, Connection connection, int whereClosingKind, int setClosingKind, int closingDate)
            throws Exception
    {

        String query = "";
        boolean ret = true;

        // ���|�f�[�^
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
        // 20110913 �����f�[�^�̐����N���̂��̂����X�V���� END

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
        // 20110913 �����f�[�^�̐����N���̂��̂����X�V���� END

        prestate.executeUpdate();
        DBConnection.releaseResources( prestate );

        // �����f�[�^
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

        // �����E���|�f�[�^
        query = "UPDATE hh_bko_rel_bill_account_recv ";
        query = query + "SET closing_kind = ? ";
        query = query + "WHERE closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        prestate.setInt( 2, whereClosingKind );

        prestate.executeUpdate();
        DBConnection.releaseResources( prestate );

        // �������׃f�[�^
        query = "UPDATE hh_bko_bill_detail ";
        query = query + "SET closing_kind = ? ";
        query = query + "WHERE closing_kind = ? ";

        prestate = connection.prepareStatement( query );
        prestate.setInt( 1, setClosingKind );
        prestate.setInt( 2, whereClosingKind );

        prestate.executeUpdate();
        DBConnection.releaseResources( prestate );

        // ���ߐ���f�[�^
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
     * �����f�[�^�o�^
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param id �z�e��ID
     * @param hotelName �z�e����
     * @param billCd ������R�[�h
     * @param billDate �����N��
     * @param billKin �������z�i�ō��݁j
     * @param billKinNotInc �������z�i�Ŕ����j
     * @param ownerHotelId �z�e��ID
     * @param ownerUserId ���[�UID
     * @param closingKind ���ߏ����敪
     * @return true:����Afalse:���s
     */
    private boolean registBill(PreparedStatement prestate, Connection conn, int id, String hotelName, int billCd, int billDate, int billKin, int billKinNotInc, String ownerHotelId, int ownerUserId, int closingKind) throws Exception
    {
        // 20110808 ������R�[�h�A�z�e��ID�ŁA�����`�[No���̔ԍς݂��`�F�b�N
        int billSlipNo = 0;
        if ( hashBill.containsKey( String.valueOf( billCd ) + "-" + String.valueOf( id ) ) )
        {
            billSlipNo = hashBill.get( String.valueOf( billCd ) + "-" + String.valueOf( id ) );
        }

        String query = "";
        query = query + "INSERT hh_bko_bill ( ";
        // 20110808 ���ɍ̔ԍς݂̐����`�[No�����݂��Ă���ꍇ�́A���̐����`�[No�œo�^
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
            query = query + String.valueOf( billSlipNo ) + ","; // �����`�[No
        }
        query = query + "?, ";// ������R�[�h
        query = query + "bill_name, ";// �����於
        query = query + "bill_name_kana, ";// �����於�i�J�i�j
        query = query + "?, ";// �����N��
        query = query + "?, ";// �������z�i�ō��݁j
        query = query + "?, ";// �������z�i�Ŕ����j
        query = query + "?, ";// �����
        query = query + "?, ";// ���������s��
        query = query + "?, ";// �x��������
        query = query + "?, ";// �����\���
        query = query + "?, ";// �z�e��ID
        query = query + "?, ";// �z�e����
        query = query + "0, ";// ���s�ς݃t���O
        query = query + "0, ";// �Ĕ��s�ς݃t���O
        query = query + "bill_zip_code, ";// �������X�֔ԍ�
        query = query + "bill_pref_code, ";// ������s���{���R�[�h
        query = query + "bill_jis_code, ";// ������s�撬���R�[�h
        query = query + "bill_address1, ";// ������Z���P
        query = query + "bill_address2, ";// ������Z���Q
        query = query + "bill_address3, ";// ������Z���R
        query = query + "bill_tel, ";// ������d�b�ԍ�
        query = query + "bill_div_name, ";// �����敔����
        query = query + "bill_position_title, ";// �������E��
        query = query + "bill_person_name, ";// ������S���Җ�
        query = query + "?, ";// ���ߏ����敪
        query = query + "'', ";// �I�[�i�[�z�e��ID
        query = query + "0, ";// �I�[�i�[���[�UID
        query = query + "?, ";// �ŏI�X�V��
        query = query + "? ";// �ŏI�X�V����

        query = query + "FROM hh_bko_billing "; // ������}�X�^
        query = query + "WHERE bill_cd = ? ";

        try
        {
            int notIncTax = (int)Math.floor( billKinNotInc * OwnerBkoCommon.TAX ); // �Ŕ������̏����

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, billCd ); // ������R�[�h
            prestate.setInt( 2, billDate ); // �����N��
            prestate.setInt( 3, billKin + billKinNotInc + notIncTax ); // �������z�i�ō��݁j
            prestate.setInt( 4, billKin + billKinNotInc ); // �������z�i�Ŕ����j
            prestate.setInt( 5, notIncTax ); // �����
            prestate.setInt( 6, 0 ); // ���������s��
            prestate.setInt( 7, paymentDate ); // �x��������
            prestate.setInt( 8, paymentDate );// �����\���
            prestate.setInt( 9, id );// �z�e��ID
            prestate.setString( 10, hotelName );// �z�e����
            prestate.setInt( 11, closingKind );// ���ߏ����敪
            prestate.setInt( 12, lastUpdate );
            prestate.setInt( 13, lastUptime );
            prestate.setInt( 14, billCd );// ������R�[�h

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
     * �x���������v�Z
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return �x��������
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

            int date2month = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), 2 ); // �Q������̓��t
            int date = (date2month / 10000) * 10000 + ((date2month / 100 % 100)) * 100 + 1; // �Q������̌��̈��
            date = DateEdit.addDay( date, -1 ); // ���̑O���i�����������j
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
     * �ǉ����������`�[No
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return �����`�[No
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
     * �������ԍ��X�V
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param billSlipNo �����`�[No
     * @return true:����Afalse:���s
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
     * �������[�����M
     * 
     * @param msg ���[���{��
     * @return �Ȃ�
     */
    private void sendSuccessMail(String msg)
    {

        // ������������̂ōŌ�ɋ󔒒ǉ�
        msg = msg + " ";
        DataHhRsvSystemConfList sysConfList = new DataHhRsvSystemConfList();
        HashMap<String, String> map = sysConfList.getSystemConfMap( 9 ); // ���[������hh_rsv_system_conf����擾����B

        SendMail.send( map.get( MAIL_FROM ), map.get( MAIL_TO ), map.get( MAIL_SUCCESSTITLE ), msg );
    }

    /**
     * �G���[���[�����M
     * 
     * @param msg ���[���{��
     * @return �Ȃ�
     */
    private void sendErrorMail(String msg)
    {

        // ������������̂ōŌ�ɋ󔒒ǉ�
        msg = msg + " ";
        DataHhRsvSystemConfList sysConfList = new DataHhRsvSystemConfList();
        HashMap<String, String> map = sysConfList.getSystemConfMap( 9 ); // ���[������hh_rsv_system_conf����擾����B

        SendMail.send( map.get( MAIL_FROM ), map.get( MAIL_TO ), map.get( MAIL_ERRORTITLE ), msg );
    }

    /**
     * conf�t�@�C������A�w�肵�����b�Z�[�WID�̃��b�Z�[�W���擾����
     * 
     * @param messageID ���b�Z�[�WID
     * @return �擾�������b�Z�[�W�i���b�Z�[�WID�����o�^�̏ꍇ�́ANull��Ԃ��j
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
