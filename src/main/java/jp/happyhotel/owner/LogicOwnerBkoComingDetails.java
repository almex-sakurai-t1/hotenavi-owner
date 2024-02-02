package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerBkoCommon;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataBkoAccountRecv;
import jp.happyhotel.data.DataBkoAccountRecvDetail;

/**
 * ���X���׃r�W�l�X���W�b�N
 */
public class LogicOwnerBkoComingDetails implements Serializable
{
    private static final long         serialVersionUID = -987298264984295770L;

    private FormOwnerBkoComingDetails frm;

    /* �t�H�[���I�u�W�F�N�g */
    public FormOwnerBkoComingDetails getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoComingDetails frm)
    {
        this.frm = frm;
    }

    /**
     * ���|�f�[�^�擾
     * 
     * @param �Ȃ�
     * @return �Ȃ�
     */
    public void getAccountRecv() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        String usageDate = "";
        String usageTime = "";
        String slipUpdate = "";
        int sumHapy = 0;
        int sumBill = 0;
        int seikyWaribiki = 0;
        int hWaribiki = 0;
        int sWaribiki = 0;
        NumberFormat formatCur = NumberFormat.getNumberInstance();

        query = query + "SELECT ";
        query = query + " h.id, hotel_id, usage_date, usage_time, slip_update, person_name, user_management_no, ";
        query = query + " ht_slip_no, ht_room_no, usage_charge, receive_charge, h.closing_kind, d.account_title_cd, ";
        query = query + " d.account_title_name, d.point, d.amount, happy_balance, rel.bill_slip_no ";
        query = query + "FROM hh_bko_account_recv h ";
        query = query + "    INNER JOIN hh_bko_account_recv_detail d ON d.accrecv_slip_no = h.accrecv_slip_no ";
        query = query + "    LEFT JOIN hh_bko_rel_bill_account_recv rel ON h.accrecv_slip_no = rel.accrecv_slip_no   ";
        query = query + "    INNER JOIN hh_hotel_newhappie nh ON h.id = nh.id AND h.usage_date >= nh.bko_date_start ";
        query = query + "WHERE h.accrecv_slip_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getAccrecvSlipNo() );

            result = prestate.executeQuery();

            while( result.next() )
            {
                frm.setBillSlipNo( result.getInt( "bill_slip_no" ) );
                frm.setSelHotelID( result.getInt( "id" ) );
                usageDate = result.getString( "usage_date" );
                usageTime = ConvertTime.convTimeHHMM( result.getInt( "usage_time" ), 0 );
                frm.setUsageDate( usageDate.substring( 0, 4 ) + "/" + usageDate.substring( 4, 6 ) + "/" + usageDate.substring( 6 ) + " " + usageTime );
                if ( result.getInt( "slip_update" ) == 0 )
                {
                    frm.setSlipUpdate( "0" );
                }
                else
                {
                    slipUpdate = result.getString( "slip_update" );
                    frm.setSlipUpdate( slipUpdate.substring( 0, 4 ) + "/" + slipUpdate.substring( 4, 6 ) + "/" + slipUpdate.substring( 6 ) );
                }
                frm.setPersonName( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "person_name" ) ) ) );
                frm.setUsrMngNo( result.getInt( "user_management_no" ) );
                frm.setHtSlipNo( result.getString( "ht_slip_no" ) );
                frm.setHtRoomNo( result.getString( "ht_room_no" ) );
                frm.setUsageCharge( result.getString( "usage_charge" ) );
                frm.setUsageChargeStr( formatCur.format( result.getInt( "usage_charge" ) ) );
                frm.setReceiveCharge( result.getString( "receive_charge" ) );
                frm.setReceiveChargeStr( formatCur.format( result.getInt( "receive_charge" ) ) );
                frm.sethZandaka( result.getInt( "happy_balance" ) );
                frm.setClosingKind( result.getInt( "closing_kind" ) );

                if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 == result.getInt( "d.account_title_cd" ) )
                {
                    // ���X
                    frm.sethRaiten( result.getInt( "point" ) ); // �}�C��
                    frm.setsRaiten( result.getInt( "d.amount" ) ); // ���x
                }
                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 == result.getInt( "d.account_title_cd" ) )
                {
                    // �t�^
                    frm.sethSeisan( result.getInt( "point" ) ); // �}�C��
                    frm.setsSeisan( Integer.toString( (result.getInt( "d.amount" ) * -1) ) ); // ���x
                }
                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 == result.getInt( "d.account_title_cd" ) )
                {
                    hWaribiki += result.getInt( "point" );
                    sWaribiki += result.getInt( "d.amount" );

                    // �g�p
                    frm.sethWaribiki( hWaribiki * -1 ); // �}�C��
                    frm.sethWaribiki_View( Integer.toString( hWaribiki ) );
                    // frm.sethWaribiki_Inp( Integer.toString( result.getInt( "d.amount" ) * -1 ) );

                    frm.setsWaribiki( Integer.toString( sWaribiki * -1 ) ); // ���x
                    frm.setsWaribikiInt( sWaribiki * -1 );
                    seikyWaribiki = sWaribiki;
                }
                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 == result.getInt( "d.account_title_cd" ) )
                {
                    // �}�C�� �\��
                    frm.sethYoyaku( result.getInt( "point" ) );
                }

                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_140 == result.getInt( "d.account_title_cd" ) || OwnerBkoCommon.ACCOUNT_TITLE_CD_141 == result.getInt( "d.account_title_cd" ) )
                {
                    // ���x �\��萔��
                    frm.setsYoyaku( Integer.toString( result.getInt( "d.amount" ) * -1 ) );
                }

                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_150 == result.getInt( "d.account_title_cd" ) )
                {
                    // �\��{�[�i�X�}�C��
                    frm.sethBonusMile( result.getInt( "point" ) );
                    // ���x�@�\��{�[�i�X�}�C��
                    frm.setsBonusMile( Integer.toString( result.getInt( "d.amount" ) * -1 ) );
                }

                else if ( OwnerBkoCommon.ACCOUNT_TITLE_CD_200 <= result.getInt( "d.account_title_cd" ) )
                {
                    // ���̑� ���x
                    frm.setSonota1( result.getString( "d.account_title_name" ) );
                    frm.setSonotaFlg( 1 ); // ���i�R�[�h200�ԑ�̂��߁A���̏��i�R�[�h�ƍ��݂��Ȃ��悤�ɍX�V�s�Ƃ���
                    frm.setSonota1Charge( Integer.toString( result.getInt( "d.amount" ) * -1 ) );
                }
            }

            // ���R�[�h�����擾
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // �Y���f�[�^���Ȃ��ꍇ
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "���X����" ) );
                return;
            }

            // �}�C���@���v
            sumHapy = frm.gethRaiten() + frm.gethSeisan() + (frm.gethWaribiki() * -1) + frm.gethYoyaku() + frm.gethBonusMile();
            frm.sethSum( Integer.toString( sumHapy ) );

            // ���x�@���v
            int yoyaku = 0;
            int seisan = 0;
            int bonusMile = 0;
            if ( frm.getsYoyaku().trim().length() == 0 )
            {
                yoyaku = 0;
            }
            else
            {
                yoyaku = Integer.parseInt( frm.getsYoyaku() );
            }
            if ( frm.getsBonusMile().trim().length() == 0 )
            {
                bonusMile = 0;
            }
            else
            {
                bonusMile = Integer.parseInt( frm.getsBonusMile() );
            }
            if ( frm.getsSeisan().trim().length() == 0 )
            {
                seisan = 0;
            }
            else
            {
                seisan = Integer.parseInt( frm.getsSeisan() );
            }
            sumBill = seisan + (seikyWaribiki * -1) + yoyaku + bonusMile;
            frm.setsSum( Integer.toString( sumBill ) );

            // �|�C���g�{���擾
            double bairitu = 0; // �t�^�{��
            bairitu = getAmountRate( frm.getSelHotelID(), frm.getAccrecvSlipNo() );
            frm.setBairitu( bairitu );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccountRecv] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * �|�C���g�{���擾
     * 
     * @param hotelId:�z�e��ID�AaccSlipNo:���|�`�[�ԍ�
     * @return �|�C���g�{��
     * @throws Exception
     */
    public double getAmountRate(int hotelid, int accSlipNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        double ret = 1;
        int idx = 0;

        query = query + "SELECT ci.amount_rate ";
        query = query + "FROM hh_bko_account_recv_detail dtl ";
        query = query + "  LEFT JOIN hh_user_point_pay_temp point ON dtl.user_id = point.user_id AND dtl.seq = point.seq ";
        query = query + "  LEFT JOIN hh_hotel_ci ci ON dtl.user_id = ci.user_id AND point.user_seq = ci.user_seq AND point.visit_seq = ci.visit_seq ";
        query = query + "WHERE dtl.accrecv_slip_no = ? ";
        query = query + "  AND dtl.account_title_cd = " + OwnerBkoCommon.ACCOUNT_TITLE_CD_110;
        query = query + " ORDER BY dtl.id, ci.seq, ci.sub_seq DESC ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( idx > 0 )
                {
                    break;
                }
                ret = result.getDouble( "amount_rate" );

                idx++;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAmountRate] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �����E���|�f�[�^���݃`�F�b�N
     * ���ʂ�Form�ɃZ�b�g����B
     * 
     * @param �Ȃ�
     * @return �Ȃ�
     */
    public void existsBillData() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_bko_rel_bill_account_recv ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getAccrecvSlipNo() );

            result = prestate.executeQuery();

            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }

            frm.setExistsSeikyu( false );
            if ( count > 0 )
            {
                frm.setExistsSeikyu( true );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.existsBillData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * �{���߃f�[�^�̔��|�f�[�^�쐬
     * 
     * @return true:���������Afalse:�������s
     * @throws Exception
     */
    public boolean registAccountRecvHon() throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;
        ResultSet result = null;
        Connection connection = null;
        boolean ret = false;
        int usageAmount = 0;
        int slipNo = 0;
        int akaSlipNo = 0;
        int newSlipNo = 0;

        try
        {
            // ���݂̔��|�`�[�ԍ��擾
            slipNo = frm.getAccrecvSlipNo();
            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            // ���ԓ`���R�[�h���쐬
            ret = insertAccountRecv( prestate, connection, slipNo, 1, 0, 0, OwnerBkoCommon.CLOSING_KBN_KARI, OwnerBkoCommon.REGIST_FLG_HONT );

            // �ԓ`�p���|�`�[No.�擾
            akaSlipNo = getAccrSlipNo( prestate, connection );
            if ( ret )
            {
                // �ԓ`���|����
                ret = copyDetail( connection, slipNo, akaSlipNo );
            }

            // �������z���z�i���p���z�j
            usageAmount = Integer.parseInt( frm.getUsageCharge() );

            // �����`�쐬
            // ���|�f�[�^�쐬
            if ( ret )
            {
                ret = insertAccountRecv( prestate, connection, slipNo, 2, usageAmount, Integer.parseInt( frm.getReceiveCharge() ), OwnerBkoCommon.CLOSING_KBN_KARI, OwnerBkoCommon.REGIST_FLG_HONT );
            }

            // �V�������|�`�[No�擾
            newSlipNo = getAccrSlipNo( prestate, connection );
            int slipDetailNo = 1;
            int accrecv_amount = 0;
            int amount = 0;
            int point = 0;
            DataBkoAccountRecvDetail dataAccDetail = null;

            // ���̔��|���׃f�[�^�擾
            int hotelId = getAccDetailNo( prestate, connection, akaSlipNo, 1 );
            int seq = getAccDetailNo( prestate, connection, akaSlipNo, 2 );
            String rsvNo = getAccDetailRsvno( prestate, connection, akaSlipNo, 1 );
            String userId = getAccDetailRsvno( prestate, connection, akaSlipNo, 2 );

            // ���Ƃ̔��|���׃f�[�^�������p��
            if ( ret )
            {
                ret = copyDetail( connection, slipNo, newSlipNo );
            }

            // ���ݓo�^����Ă��閾�הԍ��̎��̔ԍ����擾
            slipDetailNo = getNextSlipDetailNo( prestate, connection, newSlipNo );

            // // �\����z
            // if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140 ) == true )
            // {
            // // �Ώۂ̔��|�f�[�^�����݂���ꍇ�A�X�V
            // ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, Integer.valueOf( frm.getReceiveCharge() ) );
            //
            // }
            // else
            // {
            // // ���݂��Ȃ��ꍇ�͒ǉ�
            // if ( Integer.valueOf( frm.getReceiveCharge() ) > 0 )
            // {
            //
            // dataAccDetail = OwnerBkoCommon.GetDetailYoyaku( Integer.valueOf( frm.getReceiveCharge() ) );
            // dataAccDetail.setAccrecvSlipNo( newSlipNo );
            // dataAccDetail.setSlipDetailNo( slipDetailNo );
            //
            // if ( ret )
            // {
            // dataAccDetail.setId( hotelId );
            // dataAccDetail.setReserveNo( rsvNo );
            // dataAccDetail.setUserId( userId );
            // dataAccDetail.setSeq( seq );
            // dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );
            //
            // ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
            // slipDetailNo = slipDetailNo + 1;
            // accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
            // }
            // }
            // }

            // �n�s�[�����E�g�p
            if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_120 ) == true )
            {
                // �Ώۂ̔��|�f�[�^�����݂���ꍇ�A�X�V
                ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, Integer.valueOf( frm.gethWaribiki() ) );

            }
            else
            {

                if ( Integer.valueOf( frm.gethWaribiki() ) > 0 )
                {

                    dataAccDetail = OwnerBkoCommon.GetDetailWaribiki( Integer.valueOf( frm.gethWaribiki() ) );
                    dataAccDetail.setAccrecvSlipNo( newSlipNo );
                    dataAccDetail.setSlipDetailNo( slipDetailNo );

                    if ( ret )
                    {
                        amount = dataAccDetail.getAmount() * -1;
                        point = dataAccDetail.getPoint() * -1;

                        dataAccDetail.setAmount( amount );
                        dataAccDetail.setPoint( point );
                        dataAccDetail.setId( hotelId );
                        dataAccDetail.setReserveNo( rsvNo );
                        dataAccDetail.setUserId( userId );
                        dataAccDetail.setSeq( seq );
                        dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );

                        ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        slipDetailNo = slipDetailNo + 1;
                        accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                    }
                }
            }

            // �n�s�[�����E�t�^
            if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110 ) == true )
            {
                // �Ώۂ̔��|�f�[�^�����݂���ꍇ�A�X�V
                ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, Integer.valueOf( frm.gethSeisan() ) );

            }
            else
            {

                if ( Integer.valueOf( frm.gethWaribiki() ) > 0 )
                {

                    dataAccDetail = OwnerBkoCommon.GetDetailWaribiki( Integer.valueOf( frm.gethSeisan() ) );
                    dataAccDetail.setAccrecvSlipNo( newSlipNo );
                    dataAccDetail.setSlipDetailNo( slipDetailNo );

                    if ( ret )
                    {
                        amount = dataAccDetail.getAmount() * -1;
                        point = dataAccDetail.getPoint() * -1;

                        dataAccDetail.setAmount( amount );
                        dataAccDetail.setPoint( point );
                        dataAccDetail.setId( hotelId );
                        dataAccDetail.setReserveNo( rsvNo );
                        dataAccDetail.setUserId( userId );
                        dataAccDetail.setSeq( seq );
                        dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );

                        ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        slipDetailNo = slipDetailNo + 1;
                        accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                    }
                }
            }

            // ���̔��|�f�[�^�̔��|���Ɣ��|�c��V�����`�[���ׂ̋��z�ōX�V
            if ( ret )
            {
                ret = updAccountRecvUrikake( connection, prestate, newSlipNo );
            }

            // �������`�[�̐�����R�[�h�擾
            int billCd = 0;
            billCd = getOrgBillCd( connection, slipNo );

            // ����Ԃ̊Y��������`�[�ԍ��擾
            int billSlipNo = 0;
            billSlipNo = getNewBillSlipNo( connection, billCd );

            // �����E���|�f�[�^�쐬
            if ( ret )
            {
                ret = insRelBillAccountRecv( connection, prestate, newSlipNo, billSlipNo );
            }

            // �������׃f�[�^�X�V�i�쐬�j
            if ( ret )
            {
                billSlipNo = getBillSlipNo( prestate, connection, newSlipNo );

                if ( billSlipNo > 0 )
                {
                    ret = OwnerBkoCommon.RegistBillDetail( prestate, connection, billSlipNo );
                }
            }

            // �����f�[�^�̋��z�X�V
            if ( ret )
            {
                ret = updBillCharge( connection, prestate, billSlipNo );
            }

            // �����f�[�^�쐬
            if ( ret )
            {
                // �������׍폜(�ԓ`)
                OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), frm.getHotenaviID(), frm.getUserId(),
                        OwnerRsvCommon.ADJUST_EDIT_ID_BILL_DEL, akaSlipNo, OwnerRsvCommon.ADJUST_MEMO_BILL_DEL );
                // �������גǉ�(���`)
                OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), frm.getHotenaviID(), frm.getUserId(),
                        OwnerRsvCommon.ADJUST_EDIT_ID_BILL_ADD, newSlipNo, OwnerRsvCommon.ADJUST_MEMO_BILL_ADD );
            }

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
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            Logging.error( "[LogicOwnerBkoComingDetails.registAccountRecvHon] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * ���|���׃f�[�^���݃`�F�b�N
     * 
     * @param prestate PreparedStatement
     * @param connection Connection
     * @param accSlipNo ���|�`�[�ԍ�
     * @param accTitleCd �ȖڃR�[�h
     * @return true;���݂���Afalse:���݂��Ȃ�
     * @throws Exception
     */
    private boolean existsAccountRecv(PreparedStatement prestate, Connection connection, int accSlipNo, int accTitleCd) throws Exception
    {
        boolean ret = false;

        String query = "";
        ResultSet result = null;
        int cnt = 0;

        query = query + "SELECT COUNT(*) as CNT ";
        query = query + "FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";
        query = query + "  AND account_title_cd = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            prestate.setInt( 2, accTitleCd );
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
            Logging.error( "[LogicOwnerBkoComingDetails.existsAccountRecv] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * ���|���׃f�[�^�X�V����
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param billSlipNo ���|����No.
     * @param accTitleCd �ȖڃR�[�h
     * @param amount ���p���z
     * @return true:�X�V�����Afalse:�X�V���s
     */
    private boolean updAccountRecvDetail(Connection conn, PreparedStatement prestate, int billSlipNo, int accTitleCd, int amount)
    {

        String query = "";
        boolean ret = false;
        int detailAmount = 0;
        int point = 0;

        query = query + "UPDATE hh_bko_account_recv_detail SET ";
        query = query + "  amount = ?, ";
        query = query + "  point = ? ";
        query = query + "WHERE accrecv_slip_no = ? ";
        query = query + "  AND account_title_cd = ? ";

        if ( accTitleCd == OwnerBkoCommon.ACCOUNT_TITLE_CD_110 )
        {
            detailAmount = amount * 2;
            point = amount;

        }
        else if ( accTitleCd == OwnerBkoCommon.ACCOUNT_TITLE_CD_120 )
        {
            detailAmount = amount * -1;
            point = amount * -1;

        }
        else if ( accTitleCd == OwnerBkoCommon.ACCOUNT_TITLE_CD_140 )
        {
            detailAmount = (int)Math.floor( amount * OwnerBkoCommon.PERCENT_YOYAKU_AMOUNT / 100 );
            point = 0;
        }

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, detailAmount );
            prestate.setInt( 2, point );
            prestate.setInt( 3, billSlipNo );
            prestate.setInt( 4, accTitleCd );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerBkoComingDetails.updAccountRecvDetail] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ���|�f�[�^�X�V
     * 
     * @param hotelId �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean registAccountRecv() throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;
        ResultSet result = null;
        Connection connection = null;
        boolean ret = false;
        DataBkoAccountRecv data = new DataBkoAccountRecv();
        int usageAmount = 0;
        int slipNo = 0;
        int akaSlipNo = 0;
        int newSlipNo = 0;
        int registFlag = 0; // 0:�I�[�i�[ 1:������

        try
        {
            slipNo = frm.getAccrecvSlipNo();

            if ( frm.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA )
            {
                registFlag = OwnerBkoCommon.REGIST_FLG_HONT;
            }

            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            // ���݂̃��R�[�h�𖳌��ɍX�V
            data.getData( slipNo );
            data.setInvalidFlag( 1 );
            data.setSlipUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            data.setOwnerUserId( frm.getUserId() );
            data.setOwnerHotelId( frm.getHotenaviID() );
            data.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            data.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = data.updateData( connection );

            // �ԓ`���R�[�h���쐬
            if ( ret )
            {
                ret = insertAccountRecv( prestate, connection, slipNo, 1, 0, 0, OwnerBkoCommon.CLOSING_KBN_KARI, registFlag );
            }
            // �ԓ`�p���|�`�[No.�擾
            akaSlipNo = getAccrSlipNo( prestate, connection );

            if ( ret )
            {
                // �ԓ`���|����
                ret = copyDetail( connection, slipNo, akaSlipNo );
            }

            // �������z���z�i���p���z�j
            usageAmount = Integer.parseInt( frm.getUsageCharge() );

            // ���|�f�[�^�쐬
            if ( ret )
            {
                ret = insertAccountRecv( prestate, connection, slipNo, 2, usageAmount, Integer.parseInt( frm.getReceiveCharge() ), OwnerBkoCommon.CLOSING_KBN_KARI, registFlag );
            }

            // �V�������|�`�[No�擾
            newSlipNo = getAccrSlipNo( prestate, connection );
            int slipDetailNo = 0;
            int accrecv_amount = 0;
            int amount = 0;
            int point = 0;
            DataBkoAccountRecvDetail dataAccDetail = null;

            // ���̔��|���׃f�[�^�擾
            int hotelId = getAccDetailNo( prestate, connection, akaSlipNo, 1 );
            int seq = getAccDetailNo( prestate, connection, akaSlipNo, 2 );
            String rsvNo = getAccDetailRsvno( prestate, connection, akaSlipNo, 1 );
            String userId = getAccDetailRsvno( prestate, connection, akaSlipNo, 2 );

            // ���Ƃ̔��|���׃f�[�^�������p��
            if ( ret )
            {
                ret = copyDetail( connection, slipNo, newSlipNo );
            }

            // ���ݓo�^����Ă��閾�הԍ��̎��̔ԍ����擾
            slipDetailNo = getNextSlipDetailNo( prestate, connection, newSlipNo );

            // // �\����z
            // if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140 ) == true )
            // {
            // // �Ώۂ̔��|�f�[�^�����݂���ꍇ�A�X�V
            // ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, Integer.valueOf( frm.getReceiveCharge() ) );
            //
            // }
            // else
            // {
            //
            // if ( Integer.valueOf( frm.getReceiveCharge() ) > 0 )
            // {
            //
            // dataAccDetail = OwnerBkoCommon.GetDetailYoyaku( Integer.valueOf( frm.getReceiveCharge() ) );
            // dataAccDetail.setAccrecvSlipNo( newSlipNo );
            // dataAccDetail.setSlipDetailNo( slipDetailNo );
            //
            // if ( ret )
            // {
            // dataAccDetail.setId( hotelId );
            // dataAccDetail.setReserveNo( rsvNo );
            // dataAccDetail.setUserId( userId );
            // dataAccDetail.setSeq( seq );
            // dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );
            //
            // ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
            // slipDetailNo = slipDetailNo + 1;
            // accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
            // }
            // }
            // }

            // �n�s�[�����E�g�p
            if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_120 ) == true )
            {
                // �Ώۂ̔��|�f�[�^�����݂���ꍇ�A�X�V
                ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, Integer.valueOf( frm.gethWaribiki() ) );

            }
            else
            {
                if ( Integer.valueOf( frm.gethWaribiki() ) > 0 )
                {

                    dataAccDetail = OwnerBkoCommon.GetDetailWaribiki( Integer.valueOf( frm.gethWaribiki() ) );
                    dataAccDetail.setAccrecvSlipNo( newSlipNo );
                    dataAccDetail.setSlipDetailNo( slipDetailNo );

                    if ( ret )
                    {
                        amount = dataAccDetail.getAmount() * -1;
                        point = dataAccDetail.getPoint() * -1;

                        dataAccDetail.setAmount( amount );
                        dataAccDetail.setPoint( point );
                        dataAccDetail.setId( hotelId );
                        dataAccDetail.setReserveNo( rsvNo );
                        dataAccDetail.setUserId( userId );
                        dataAccDetail.setSeq( seq );
                        dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );

                        ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        slipDetailNo = slipDetailNo + 1;
                        accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                    }
                }
            }

            // �}�C���E�t�^
            if ( existsAccountRecv( prestate, connection, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110 ) == true )
            {
                // �Ώۂ̔��|�f�[�^�����݂���ꍇ�A�X�V
                ret = updAccountRecvDetail( connection, prestate, newSlipNo, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, Integer.valueOf( frm.gethSeisan() ) );

            }
            else
            {
                if ( Integer.valueOf( frm.gethSeisan() ) > 0 )
                {
                    dataAccDetail = OwnerBkoCommon.GetDetailHuyo( Integer.valueOf( frm.gethSeisan() ) );
                    dataAccDetail.setAccrecvSlipNo( newSlipNo );
                    dataAccDetail.setSlipDetailNo( slipDetailNo );

                    if ( ret )
                    {
                        dataAccDetail.setAmount( dataAccDetail.getAmount() );
                        dataAccDetail.setPoint( dataAccDetail.getPoint() );
                        dataAccDetail.setId( hotelId );
                        dataAccDetail.setReserveNo( rsvNo );
                        dataAccDetail.setUserId( userId );
                        dataAccDetail.setSeq( seq );
                        dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_KARI );

                        ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        slipDetailNo = slipDetailNo + 1;
                        accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                    }
                }
            }

            // ���̔��|�f�[�^�̔��|���Ɣ��|�c��V�����`�[���ׂ̋��z�ōX�V
            if ( ret )
            {
                ret = updAccountRecvUrikake( connection, prestate, newSlipNo );
            }

            // �����f�[�^�X�V
            if ( ret )
            {
                ret = updateBill( connection, slipNo, newSlipNo );
            }

            // �������׃f�[�^�X�V�i�쐬�j
            int billSlipNo = 0;
            if ( ret )
            {
                billSlipNo = getBillSlipNo( prestate, connection, newSlipNo );

                if ( billSlipNo > 0 )
                {
                    ret = OwnerBkoCommon.RegistBillDetail( prestate, connection, billSlipNo );
                }
            }

            // �����f�[�^�̋��z�X�V
            if ( ret )
            {
                ret = updBillCharge( connection, prestate, billSlipNo );
            }

            // �����f�[�^�쐬
            if ( ret )
            {
                // �������׍폜(�ԓ`)
                OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), frm.getHotenaviID(), frm.getUserId(),
                        OwnerRsvCommon.ADJUST_EDIT_ID_BILL_DEL, akaSlipNo, OwnerRsvCommon.ADJUST_MEMO_BILL_DEL );
                // �������גǉ�(���`)
                OwnerRsvCommon.addAdjustmentHistory( frm.getSelHotelID(), frm.getHotenaviID(), frm.getUserId(),
                        OwnerRsvCommon.ADJUST_EDIT_ID_BILL_ADD, newSlipNo, OwnerRsvCommon.ADJUST_MEMO_BILL_ADD );
            }

            // �V�������|�`�[�ԍ����Z�b�g
            frm.setNewAccSLipNo( newSlipNo );
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
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            Logging.error( "[LogicOwnerBkoComingDetails.RegistAccountRecv] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * �������ׂ����ԏ������ԍ��̃f�[�^�擾
     * 
     * @param prestate
     * @param connection
     * @param accSlipNo ���|�`�[�ԍ�
     * @param selKbn 1:�z�e��ID�A2�F�Ǘ��ԍ�
     * @return
     * @throws Exception
     */
    private int getAccDetailNo(PreparedStatement prestate, Connection connection, int accSlipNo, int selKbn) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int hotelId = 0;
        int seq = 0;
        int cnt = 0;
        int ret = 0;

        query = query + "SELECT id, seq ";
        query = query + "FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( cnt == 0 )
                {
                    hotelId = result.getInt( "id" );
                    seq = result.getInt( "seq" );
                }

                cnt = cnt + 1;
            }

            if ( selKbn == 1 )
            {
                ret = hotelId;
            }
            else
            {
                ret = seq;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccDetailNo] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * �������ׂ����ԏ������ԍ��̗\��ԍ��A�܂��̓��[�U�[ID�擾
     * 
     * @param prestate
     * @param connection
     * @param accSlipNo ���|�`�[�ԍ�
     * @param selKbn 1�F�\��ԍ��A2�F���[�U�[ID
     * @return
     * @throws Exception
     */
    private String getAccDetailRsvno(PreparedStatement prestate, Connection connection, int accSlipNo, int selKbn) throws Exception
    {
        String query = "";
        ResultSet result = null;
        String rsvNo = "";
        String ret = "";
        String userId = "";
        int cnt = 0;

        query = query + "SELECT reserve_no, user_id FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( cnt == 0 )
                {
                    rsvNo = result.getString( "reserve_no" );
                    userId = result.getString( "user_id" );
                }
                cnt = cnt + 1;
            }

            if ( selKbn == 1 )
            {
                ret = rsvNo;
            }
            else
            {
                ret = userId;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccDetailRsvno] Exception=" + e.toString() );
            throw new Exception( e );
        }
        return(ret);
    }

    /**
     * ���|���׃R�s�[
     * 
     * @param conn Connection
     * @param oldAccrecvSlipNo �ύX�O���|�`�[No
     * @param newAccrecvSlipNo �ύX�㔄�|�`�[No
     * @return �Ȃ�
     */
    private boolean copyDetail(Connection connection, int oldAccrecvSlipNo, int newAccrecvSlipNo) throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;

        query = query + "INSERT INTO hh_bko_account_recv_detail (";
        query = query + " accrecv_slip_no, ";
        query = query + " slip_detail_no, ";
        query = query + " slip_kind, ";
        query = query + " account_title_cd, ";
        query = query + " account_title_name, ";
        query = query + " amount, ";
        query = query + " point, ";
        query = query + " id, ";
        query = query + " reserve_no, ";
        query = query + " user_id, ";
        query = query + " seq, ";
        query = query + " closing_kind ) ";
        query = query + "SELECT ";
        query = query + " ?, ";
        query = query + " slip_detail_no, ";
        query = query + " slip_kind, ";
        query = query + " account_title_cd, ";
        query = query + " account_title_name, ";
        query = query + " amount, ";
        query = query + " point, ";
        query = query + " id, ";
        query = query + " reserve_no, ";
        query = query + " user_id, ";
        query = query + " seq, ";
        query = query + " ? ";
        query = query + "FROM hh_bko_account_recv_detail WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, newAccrecvSlipNo );
            prestate.setInt( 2, OwnerBkoCommon.CLOSING_KBN_KARI );
            prestate.setInt( 3, oldAccrecvSlipNo );

            if ( !(prestate.executeUpdate() > 0) )
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.CopyDetail] Exception=" + e.toString() );
            return false;

        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return true;
    }

    /**
     * ���|�f�[�^�쐬
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param slipNo int ���̔��|�f�[�^
     * @param insMode int 1;�ԓ`�쐬�A2:�ʏ�̔��|�f�[�^
     * @param usageAmount int ���p���z
     * @param rsvAmount int �\����z
     * @param closingKind int ���ߏ����t���O
     * @param registFlag int �o�^�t���O
     * @return true:����Afalse:���s
     */
    private boolean insertAccountRecv(PreparedStatement prestate, Connection conn, int slipNo, int insMode, int usageAmount, int rsvAmount, int closingKind, int registFlag) throws Exception
    {
        String query = "";
        int result;
        boolean ret = false;

        query = query + "INSERT INTO hh_bko_account_recv (";
        query = query + " hotel_id, ";
        query = query + " id, ";
        query = query + " add_up_date, ";
        query = query + " slip_update, ";
        query = query + " bill_cd, ";
        query = query + " bill_name, ";
        query = query + " div_name, ";
        query = query + " person_name, ";
        query = query + " user_management_no, ";
        query = query + " usage_date, ";
        query = query + " usage_time, ";
        query = query + " ht_slip_no, ";
        query = query + " ht_room_no, ";
        query = query + " happy_balance, ";
        query = query + " accrecv_amount, ";
        query = query + " reconcile_amount, ";
        query = query + " accrecv_balance, ";
        query = query + " remarks, ";
        query = query + " correction, ";
        query = query + " temp_slip_no, ";
        query = query + " first_accrecv_slip_no, ";
        query = query + " credit_note_flag, ";
        query = query + " invalid_flag, ";
        query = query + " regist_flag, ";
        query = query + " closing_kind, ";
        query = query + " owner_hotel_id, ";
        query = query + " owner_user_id, ";
        query = query + " last_update, ";
        query = query + " last_uptime, ";
        query = query + " usage_charge, ";
        query = query + " receive_charge ) ";
        query = query + "SELECT ";
        query = query + " hotel_id, ";
        query = query + " id, ";
        query = query + " add_up_date, ";
        query = query + " slip_update, ";
        query = query + " bill_cd, ";
        query = query + " bill_name, ";
        query = query + " div_name, ";
        query = query + " person_name, ";
        query = query + " user_management_no, ";
        query = query + " usage_date, ";
        query = query + " usage_time, ";
        query = query + " ht_slip_no, ";
        query = query + " ht_room_no, ";
        query = query + " happy_balance, ";
        query = query + " accrecv_amount, ";
        query = query + " reconcile_amount, ";
        query = query + " accrecv_balance, ";
        query = query + " remarks, ";
        query = query + " correction, ";
        query = query + " temp_slip_no, ";
        query = query + " first_accrecv_slip_no, ";
        query = query + " ?, "; // credit_note_flag
        query = query + " ?, "; // invalid_flag
        query = query + " ?, "; // regist_flag
        query = query + " ?, "; // closing_kind
        query = query + " owner_hotel_id, ";
        query = query + " owner_user_id, ";
        query = query + " last_update, ";
        query = query + " last_uptime, ";
        if ( insMode == 1 )
        {
            query = query + " usage_charge, ";
            query = query + " receive_charge ";
        }
        else
        {
            query = query + " ?, "; // usageAmount
            query = query + " ? "; // rsvAmount
        }
        query = query + "FROM hh_bko_account_recv ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            if ( insMode == 1 )
            {
                prestate.setInt( 1, 1 );
                prestate.setInt( 2, 1 );
                prestate.setInt( 3, registFlag );
                prestate.setInt( 4, closingKind );
                prestate.setInt( 5, slipNo );
            }
            else
            {
                prestate.setInt( 1, 0 );
                prestate.setInt( 2, 0 );
                prestate.setInt( 3, registFlag );
                prestate.setInt( 4, closingKind );
                prestate.setInt( 5, usageAmount );
                prestate.setInt( 6, rsvAmount );
                prestate.setInt( 7, slipNo );
            }

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetail.insertAccountRecv] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * �{�o�^���̌��`�[�̐�����R�[�h�擾
     * 
     * @param Connection�I�u�W�F�N�g
     * @param int oldAccrecvSlipNo �X�V�O���|�`�[No.
     * @return true:�X�V�����Afalse:�X�V���s
     */
    private int getOrgBillCd(Connection connection, int oldAccrecvSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        int billCd = 0;

        query = query + "SELECT h.bill_cd ";
        query = query + "FROM hh_bko_rel_bill_account_recv d ";
        query = query + "  INNER JOIN hh_bko_bill h ON d.bill_slip_no = h.bill_slip_no ";
        query = query + "WHERE d.accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, oldAccrecvSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                billCd = result.getInt( "bill_cd" );
            }

            return billCd;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getOrgBillCd] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * ���o�^����Ă��鐿���`�[No.�擾
     * 
     * @param connection Connection�I�u�W�F�N�g
     * @param billCd ������R�[�h
     * @return �����`�[No.
     * @throws Exception
     */
    private int getNewBillSlipNo(Connection connection, int billCd) throws Exception
    {
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        int billSlipNo = 0;

        query = query + "SELECT bill_slip_no FROM hh_bko_bill ";
        query = query + "WHERE bill_cd = ? ";
        query = query + "  AND closing_kind = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, billCd );
            prestate.setInt( 2, OwnerBkoCommon.CLOSING_KBN_KARI );
            result = prestate.executeQuery();

            while( result.next() )
            {
                billSlipNo = result.getInt( "bill_slip_no" );
            }

            return billSlipNo;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getNewBillSlipNo] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * �����f�[�^�X�V
     * 
     * @param Connection�I�u�W�F�N�g
     * @param int oldAccrecvSlipNo �X�V�O���|�`�[No.
     * @param int newAccrecvSlipNo �X�V�㔄�|�`�[No.
     * @return true:�X�V�����Afalse:�X�V���s
     */
    private boolean updateBill(Connection connection, int oldAccrecvSlipNo, int newAccrecvSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> billSlipNoList = new ArrayList<Integer>();
        ArrayList<Integer> slipDtlNoList = new ArrayList<Integer>();
        ArrayList<Integer> chargeIncTaxList = new ArrayList<Integer>();
        boolean ret = false;

        query = query + "SELECT d.*, h.charge_inc_tax ";
        query = query + "FROM hh_bko_rel_bill_account_recv d ";
        query = query + "  INNER JOIN hh_bko_bill h ON d.bill_slip_no = h.bill_slip_no ";
        query = query + "WHERE d.accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, oldAccrecvSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                billSlipNoList.add( result.getInt( "bill_slip_no" ) );
                slipDtlNoList.add( result.getInt( "slip_detail_no" ) );
                chargeIncTaxList.add( result.getInt( "charge_inc_tax" ) );
            }

            // �����E���|�f�[�^�쐬
            for( int i = 0 ; i < billSlipNoList.size() ; i++ )
            {
                ret = updRelBillAccountRecv( connection, prestate, newAccrecvSlipNo, oldAccrecvSlipNo, billSlipNoList.get( i ), slipDtlNoList.get( i ) );
                if ( ret == false )
                {
                    throw new Exception( "�����E���|�f�[�^�쐬���G���[�FupdBillDetail" );
                }

            }

            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.updateBill] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * �����E���|�f�[�^�ǉ�
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param newSlipNo int �V�������|�ԍ�
     * @param billSlipNo int �����ԍ�
     * @return slipNo int �o�^��̔��|�`�[No.
     */
    private boolean insRelBillAccountRecv(Connection conn, PreparedStatement prestate, int newSlipNo, int billSlipNo)
    {

        String query = "";
        boolean ret = false;
        int nextDtlNo = 0;

        query = query + "Insert INTO hh_bko_rel_bill_account_recv VALUES(?, ?, ?, ?); ";

        try
        {
            // ���̖��הԍ��擾
            nextDtlNo = getNextSlipDtlNo( billSlipNo );

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, billSlipNo );
            prestate.setInt( 2, nextDtlNo );
            prestate.setInt( 3, newSlipNo );
            prestate.setInt( 4, OwnerBkoCommon.CLOSING_KBN_KARI );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerBkoComingDetails.insRelBillAccountRecv] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * �����E���|�f�[�^�̍ő�̖��הԍ� + 1 �̒l���擾
     * 
     * @param int billSlipNo �����ԍ�
     * @return �o�^����Ă���ő�̖��הԍ� + 1
     */
    public int getNextSlipDtlNo(int billSlipNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;

        query = query + "SELECT max(slip_detail_no) as maxNo from hh_bko_rel_bill_account_recv ";
        query = query + "WHERE bill_slip_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, billSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                ret = result.getInt( "maxNo" ) + 1;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getNextSlipDtlNo] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �����E���|�f�[�^�X�V
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param newSlipNo int �V�������|�ԍ�
     * @param oldSlipNo int �Â����|�ԍ�
     * @param billSlipNo int �����ԍ�
     * @param slipDtlNo int �`�[���הԍ�
     * @return slipNo int �o�^��̔��|�`�[No.
     */
    private boolean updRelBillAccountRecv(Connection conn, PreparedStatement prestate, int newSlipNo, int oldSlipNo, int billSlipNo, int slipDtlNo)
    {

        String query = "";
        boolean ret = false;

        query = query + "UPDATE hh_bko_rel_bill_account_recv SET ";
        query = query + "  accrecv_slip_no = ? ";
        query = query + "WHERE bill_slip_no = ? ";
        query = query + "  AND slip_detail_no = ? ";
        query = query + "  AND accrecv_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, newSlipNo );
            prestate.setInt( 2, billSlipNo );
            prestate.setInt( 3, slipDtlNo );
            prestate.setInt( 4, oldSlipNo );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerBkoComingDetails.updRelBillAccountRecv] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * �������z�f�[�^�X�V
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param billSlipNo int �������ԍ�
     * @param chargeIncTax int �������z�̏����
     * @param balance int ���z
     * @return slipNo int �o�^��̔��|�`�[No.
     */
    private boolean updBillCharge(Connection conn, PreparedStatement prestate, int billSlipNo) throws Exception
    {

        String query = "";
        boolean ret = false;
        int charge100 = 0;
        int charge200 = 0;
        int tax200 = 0;

        query = query + "UPDATE hh_bko_bill SET ";
        query = query + "  charge_inc_tax = ?,";
        query = query + "  charge_not_inc_tax = ?,";
        query = query + "  tax = ?, ";
        query = query + "  last_update = ?, ";
        query = query + "  last_uptime = ? ";
        query = query + "WHERE bill_slip_no = ? ";

        try
        {
            // �ȖڃR�[�h�u100�v�ԑ�̐������z���擾(�ō�)
            charge100 = getSumCharge( prestate, conn, billSlipNo, 1 );

            // �ȖڃR�[�h�u200�v�ԑ�̐������z���擾(�ŕ�)
            charge200 = getSumCharge( prestate, conn, billSlipNo, 2 );
            tax200 = (int)Math.floor( charge200 * OwnerBkoCommon.TAX );

            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, charge100 + charge200 + tax200 );// �������z�i�ō��݁j
            prestate.setInt( 2, charge100 + charge200 ); // �������z�i�Ŕ����j
            prestate.setInt( 3, tax200 );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 6, billSlipNo );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.updBillCharge] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * �������z(�ō�)�擾
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param billSlipNo �����`�[No.
     * @param selKbn (1:�ȖڃR�[�h�u100�v�ԑ�A2:�ȖڃR�[�h�u200�v�ԑ�)
     * @return �ō��������z
     * @throws Exception
     */
    private int getSumCharge(PreparedStatement prestate, Connection conn, int billSlipNo, int selKbn) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int chargeInTax = 0;

        try
        {
            query = query + "SELECT SUM(ad.amount) AS amount ";
            query = query + "FROM hh_bko_bill bh ";
            query = query + "  INNER JOIN hh_bko_rel_bill_account_recv br ON br.bill_slip_no = bh.bill_slip_no ";
            query = query + "    INNER JOIN hh_bko_account_recv_detail ad ON ad.accrecv_slip_no = br.accrecv_slip_no ";
            query = query + "WHERE bh.bill_slip_no = ? ";
            query = query + "AND NOT (ad.slip_kind = 0 AND ad.account_title_cd = 100) ";// �|�C���g�̗��X�͐������Ȃ��̂ŏ���
            query = query + "AND NOT (ad.slip_kind = 0 AND ad.account_title_cd = 130) ";// �|�C���g�̗\��͐������Ȃ��̂ŏ���
            if ( selKbn == 1 )
            {
                query = query + "AND (ad.account_title_cd BETWEEN 100 AND 199) ";
            }
            else
            {
                query = query + "AND (ad.account_title_cd BETWEEN 200 AND 299) ";
            }

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, billSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                chargeInTax = result.getInt( "amount" );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[OwnerBkoCommon.getSumCharge] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(chargeInTax);
    }

    /**
     * �o�^�������|�`�[No.�擾
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return slipNo int �o�^��̔��|�`�[No.
     */
    private int getAccrSlipNo(PreparedStatement prestate, Connection connection) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int slipNo = 0;

        query = query + "SELECT LAST_INSERT_ID() AS IDX ";

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            while( result.next() )
            {
                slipNo = result.getInt( "IDX" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccrSlipNo] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(slipNo);
    }

    /**
     * ���|�f�[�^�ǉ��i���X�ǉ����͗p�j
     * 
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean addAccountRecv() throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;
        ResultSet result = null;
        Connection connection = null;
        boolean ret = false;
        DataBkoAccountRecvDetail dataAccDetail = null;

        try
        {
            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            int lastUpdate = Integer.parseInt( DateEdit.getDate( 2 ) );
            int lastUptime = Integer.parseInt( DateEdit.getTime( 1 ) );

            // ���|�f�[�^
            DataBkoAccountRecv dataAcc = new DataBkoAccountRecv();
            dataAcc.setHotelId( frm.getHotenaviID() ); // �z�e��ID�i�z�e�i�r�j
            dataAcc.setId( frm.getSelHotelID() ); // �z�e��ID�i�n�s�z�e�j
            dataAcc.setAddUpDate( lastUpdate ); // �v���
            dataAcc.setSlipUpdate( lastUpdate ); // �`�[�X�V��
            dataAcc.setBillCd( 0 ); // ������R�[�h
            dataAcc.setBillName( "" ); // �����於
            dataAcc.setPersonName( "" );// �S���Җ�
            dataAcc.setUserManagementNo( 0 );// ���[�U�Ǘ��ԍ�
            dataAcc.setUsageDate( frm.getInpUsageDate() );// ���p��
            dataAcc.setHtSlipNo( 0 );// �`�[No�i�n�s�^�b�`�j
            dataAcc.setHtRoomNo( frm.getInpHtRoomNo() );// �����ԍ��i�n�s�^�b�`�j
            dataAcc.setUsageCharge( Integer.valueOf( frm.getInpUsageCharge() ) );// ���p���z
            dataAcc.setReceiveCharge( Integer.valueOf( frm.getInpRsvCharge() ) );// �\����z
            // dataAcc.setAccrecvAmount( 0 ); // ���|���z
            // dataAcc.setAccrecvBalance( 0 );// ���|�c
            dataAcc.setHappyBalance( frm.gethZandaka() );// �n�s�[�c��
            dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_HON );// ���ߏ����敪
            dataAcc.setOwnerHotelId( frm.getHotenaviID() );// �I�[�i�[�z�e��ID
            dataAcc.setOwnerUserId( frm.getUserId() );// �I�[�i�[���[�UID
            dataAcc.setLastUpdate( lastUpdate );
            dataAcc.setLastUptime( lastUptime );

            ret = OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );

            // �����̔Ԃ��ꂽ���|�`�[No�擾
            int accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, connection );
            dataAcc.setAccrecvSlipNo( accrecvSlipNo );

            // �`�[����No
            int slipDetailNo = 1;

            // ���|���z
            int accrecv_amount = 0;

            // �\����z
            if ( Integer.valueOf( frm.getInpRsvCharge() ) > 0 )
            {

                dataAccDetail = OwnerBkoCommon.GetDetailYoyaku( Integer.valueOf( frm.getInpRsvCharge() ) );
                dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                dataAccDetail.setSlipDetailNo( slipDetailNo );

                if ( ret )
                {
                    ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                    slipDetailNo = slipDetailNo + 1;
                    accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                }
            }

            // �����n�s�[
            if ( Integer.valueOf( frm.getInpWaribiki() ) > 0 )
            {

                dataAccDetail = OwnerBkoCommon.GetDetailWaribiki( Integer.valueOf( frm.getInpWaribiki() ) );
                dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                dataAccDetail.setSlipDetailNo( slipDetailNo );

                if ( ret )
                {
                    ret = OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                    slipDetailNo = slipDetailNo + 1;
                    accrecv_amount = accrecv_amount + dataAccDetail.getAmount();
                }
            }

            // ���|���z�A���|�c�X�V
            if ( ret )
            {
                ret = OwnerBkoCommon.UpdateRecv( prestate, connection, accrecvSlipNo, accrecv_amount );
            }

            // �����f�[�^�̍X�V�̓L�[���킩��Ȃ��̂ŕۗ�
            // �����N���擾
            // int billDate = getBillDate(connection);

            // if (billDate == -1) {
            // frm.setErrMsg( Message.getMessage( "warn.30033" ) );
            // return false;
            // }

            // �����f�[�^�X�V
            // ret = getBill(connection ,frm.getSelHotelID() ,billDate,accrecvSlipNo ,accrecv_amount);

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
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            Logging.error( "[LogicOwnerBkoComingDetails.addAccountRecv] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * �����N���擾
     * 
     * @param �Ȃ�
     * @return �����ߒ��̔N��
     */
    public int getClosingCntrlDate() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;

        query = query + "SELECT max(closing_date) as closing_date from hh_bko_closing_control ";
        query = query + "WHERE closing_kind = " + OwnerBkoCommon.CLOSING_KBN_KARI;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            while( result.next() )
            {
                ret = result.getInt( "closing_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getClosingCntrlDate] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * ���|�`�[No.���琿���`�[No.�擾
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param accrecvSlipNo ���|�`�[No.
     * @return billSlipNo int �����`�[No.
     */
    private int getBillSlipNo(PreparedStatement prestate, Connection connection, int accrecvSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int slipNo = -1;

        query = query + "SELECT bill_slip_no FROM hh_bko_rel_bill_account_recv ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            result = prestate.executeQuery();
            while( result.next() )
            {
                slipNo = result.getInt( "bill_slip_no" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getBillSlipNo] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(slipNo);
    }

    /**
     * ���̔��|���ד`�[�ԍ��擾
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param accrecvSlipNo ���|�`�[No.
     * @return ���הԍ� int ���̔��|���הԍ�
     */
    private int getNextSlipDetailNo(PreparedStatement prestate, Connection connection, int accrecvSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int dtlNo = 0;

        query = query + "SELECT MAX(slip_detail_no) as dtlNo FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            result = prestate.executeQuery();
            while( result.next() )
            {
                dtlNo = result.getInt( "dtlNo" );
            }

            dtlNo = dtlNo + 1;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getNextSlipDetailNo] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(dtlNo);
    }

    /**
     * ���ߏ����e�[�u���Ɂu���v�̃f�[�^�����݂��Ă��邩
     * 
     * @param �Ȃ�
     * @return true:���f�[�^����Afalse:���f�[�^�Ȃ�
     */
    public boolean existsClosingCtrlKari() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;
        boolean ret = false;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_bko_closing_control ";
        query = query + "WHERE CLOSING_KIND = " + OwnerBkoCommon.CLOSING_KBN_KARI;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
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
            Logging.error( "[LogicOwnerBkoComingDetails.existsClosingCtrlKari] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * ���|�f�[�^�̔��|���z�A���|�c�X�V
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @param accSlipNo ���|�`�[No
     * @return true:���������Afalse:�������s
     */
    private boolean updAccountRecvUrikake(Connection conn, PreparedStatement prestate, int accSlipNo)
    {
        String query = "";
        boolean ret = false;
        int detailAmount = 0;

        query = query + "UPDATE hh_bko_account_recv SET ";
        query = query + "  accrecv_amount = ?, ";
        query = query + "  accrecv_balance = ? ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {

            // ���|���z�擾
            detailAmount = getAccrecvAmount( prestate, conn, accSlipNo );

            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, detailAmount );
            prestate.setInt( 2, detailAmount );
            prestate.setInt( 3, accSlipNo );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerBkoComingDetails.updAccountRecvUrikake] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ���|���ׂ̍��v���z�擾
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param accSlipNo ���|�`�[�ԍ�
     * @return ���v���z
     * @throws Exception
     */
    public int getAccrecvAmount(PreparedStatement prestate, Connection conn, int accSlipNo) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int amount = 0;

        query = query + "SELECT SUM(amount) AS sumAmount FROM hh_bko_account_recv_detail ";
        query = query + "WHERE accrecv_slip_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, accSlipNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                amount = result.getInt( "sumAmount" );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoComingDetails.getAccrecvAmount] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        return(amount);
    }
}
