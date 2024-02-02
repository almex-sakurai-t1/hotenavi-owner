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

    // �|�C���g�敪
    private static final int    POINT_KIND_RAITEN   = 21;                               // ���X
    private static final int    POINT_KIND_RIYOU    = 22;                               // ���p
    private static final int    POINT_KIND_WARIBIKI = 23;                               // ����
    private static final int    POINT_KIND_YOYAKU   = 24;                               // �\��
    private static final int    POINT_KIND_BONUS    = 29;                               // �\��{�[�i�X

    // �ŏI�X�V��
    private int                 lastUpdate          = 0;

    // �ŏI�X�V����
    private int                 lastUptime          = 0;

    private static final String CONF_FILE           = "/etc/happyhotel/backoffice.conf";
    private static final String MAIL_ERRORTITLE     = "mail.errortitle";
    private static final String MAIL_SUCCESSTITLE   = "mail.successtitle";

    public boolean execInsert(String userId, DataUserPointPayTemp duppt, int ciSeq)
    {
        boolean ret = true;

        // ����̃f�[�^���V�K���A�ǉ����𔻒f����
        BkoAccountRecv bko;
        bko = new BkoAccountRecv();
        ret = bko.getData( duppt );
        if ( ret != false )
        {
            // hh_bko_account_recv_detail�݂̂�ǉ�
            ret = this.insertData( userId, duppt, bko, true, ciSeq );
        }
        else
        {
            // hh_bko_account_recv��hh_bko_account_recv_detail���쐬
            ret = this.insertData( userId, duppt, bko, false, ciSeq );
        }

        return ret;
    }

    public boolean execUpdate(String userId, DataUserPointPayTemp duppt)
    {
        boolean ret = true;

        // ����|���f�[�^�A����|�����׃f�[�^���擾
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

        // �z�e��ID�ƃ`�F�b�N�C���R�[�h���甄�|�f�[�^���o�^����Ă��邩�ǂ�������������B
        additionFlag = dataAcc.getData( dhc.getId(), dhc.getSeq() );
        if ( additionFlag != false )
        {
            slipDetailNo = dataAccDetail.getSlipDetailNo( dataAcc.getAccrecvSlipNo() );
            // ���ׂ��폜����B
            dataAccDetail.deleteData( dataAcc.getAccrecvSlipNo() );
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            try
            {
                // �������J�n
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // ���|�f�[�^�쐬
                // �ΏۂƂȂ郆�[�U�|�C���g�ꎞ�f�[�^�擾
                query = "SELECT ";
                query = query + "point.user_id, ";
                query = query + "point.seq, ";
                query = query + "point.point, ";
                query = query + "point.then_point, ";
                query = query + "point.amount, "; // �����̋��z(point_kind=23)or���p���z(point_kind=22)
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

                query = query + "FROM hh_hotel_basic hotelbasic "; // �z�e����{�f�[�^
                query = query + "INNER JOIN hh_rsv_reserve_basic reservebasic ON reservebasic.id = hotelbasic.id ";

                // query = query + "INNER JOIN hh_user_point_pay_temp point ON point.hotenavi_id = hotelbasic.hotenavi_id ";
                // ��L��URL�������z�e�i�rID�ŕ����̃z�e��ID�������Ă����ꍇ�ɁA���܂������Ȃ����߉��L�̌��������ɏC���@2015/01/16�@Tashiro
                query = query + "INNER JOIN hh_user_point_pay_temp point ON point.ext_code = hotelbasic.id ";
                query = query + "INNER JOIN hh_hotel_ci ci ON ci.id = hotelbasic.id  AND ci.user_id = point.user_id AND ci.user_seq=point.user_seq AND ci.visit_seq=point.visit_seq ";
                query = query + "INNER JOIN hh_bko_rel_billing_hotel billingHotel ON billingHotel.id = hotelbasic.id ";
                query = query + "WHERE ci.id = ? AND ci.seq = ? AND ci_status=1 ";
                query = query + "AND point.point_kind IN (21,22,23,24,29) ";
                query = query + "GROUP BY hotelbasic.id ,point.visit_seq ,point.point_kind ";
                query = query + "ORDER BY hotelbasic.id ,point.visit_seq ,point.point_kind ";

                // ���|�f�[�^�쐬
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, dhc.getId() );
                prestate.setInt( 2, dhc.getSeq() );
                result = prestate.executeQuery();
                Logging.info( "hdc.getId():" + dhc.getId() + ",dhc.getSeq():" + dhc.getSeq() + ",query:" + query, "STEP14.LogicOwnerBkoHapiTouch" );

                if ( result != null )
                {
                    int accrecvSlipNo = -1;
                    int accrecvAmount = 0; // ���|���z
                    while( result.next() != false )
                    {

                        // �ǉ��t���O=false�i�V�K�Ńf�[�^�ǉ��j
                        if ( additionFlag == false )
                        {
                            // �����|�f�[�^�ǉ�
                            dataAcc = new DataBkoAccountRecv();
                            dataAcc.setHotelId( result.getString( "hotelbasic.hotenavi_id" ) ); // �z�e��ID�i�z�e�i�r�j
                            dataAcc.setId( result.getInt( "hotelbasic.id" ) ); // �z�e��ID�i�n�s�z�e�j
                            dataAcc.setAddUpDate( lastUpdate ); // �v���
                            dataAcc.setBillCd( result.getInt( "billingHotel.bill_cd" ) );// ������R�[�h
                            dataAcc.setBillName( "" ); // �����於

                            dataAcc.setPersonName( OwnerBkoCommon.GetPersonName( prestate, connection, result.getString( "hotelbasic.hotenavi_id" ), result.getInt( "point.employee_code" ) ) );// �S���Җ�
                            dataAcc.setUserManagementNo( result.getInt( "point.user_seq" ) );// ���[�U�Ǘ��ԍ�
                            if ( result.getInt( "point.ci_date" ) > 0 )
                            {
                                dataAcc.setUsageDate( result.getInt( "point.ci_date" ) );// ���p��
                            }
                            else
                            {
                                dataAcc.setUsageDate( result.getInt( "point.get_date" ) );// ���p��
                            }

                            if ( result.getInt( "point.ci_time" ) > 0 )
                            {
                                dataAcc.setUsageTime( result.getInt( "point.ci_time" ) );// ���p����
                            }
                            else
                            {
                                dataAcc.setUsageTime( result.getInt( "point.get_time" ) );// ���p����
                            }
                            dataAcc.setHtSlipNo( result.getInt( "point.slip_no" ) );// �`�[No�i�n�s�^�b�`�j
                            dataAcc.setHtRoomNo( result.getString( "point.room_no" ) );// �����ԍ��i�n�s�^�b�`�j
                            dataAcc.setUsageCharge( result.getInt( "point.amount" ) );// ���p���z

                            if ( POINT_KIND_RIYOU == result.getInt( "point.point_kind" ) )
                            {
                                // �t�^
                                accrecvAmount = result.getInt( "point.point" ) * 2;
                            }
                            else if ( POINT_KIND_YOYAKU == result.getInt( "point.point_kind" ) )
                            {
                                // �\��
                                accrecvAmount = result.getInt( "point.amount" );
                            }
                            else if ( POINT_KIND_WARIBIKI == result.getInt( "point.point_kind" ) )
                            {
                                // �g�p
                                accrecvAmount = result.getInt( "point.point" );
                            }
                            else if ( POINT_KIND_BONUS == result.getInt( "point.point_kind" ) )
                            {
                                // �\��{�[�i�X
                                accrecvAmount = result.getInt( "point.point" );
                            }
                            // if ( !result.getString( "ci.rsv_no" ).equals( "" ) )
                            // {
                            // if ( drr.getData( result.getInt( "hotelbasic.id" ), result.getString( "ci.rsv_no" ) ) )
                            // {
                            // dataAcc.setReceiveCharge( drr.getChargeTotal() ); // �\����z
                            // }
                            // }
                            dataAcc.setAccrecvAmount( accrecvAmount ); // ���|���z
                            dataAcc.setAccrecvBalance( accrecvAmount );// ���|�c
                            dataAcc.setHappyBalance( result.getInt( "point.then_point" ) );// �n�s�[�c��
                            dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// ���ߏ����敪
                            dataAcc.setOwnerHotelId( result.getString( "reservebasic.hotel_id" ) );// �I�[�i�[�z�e��ID
                            dataAcc.setOwnerUserId( result.getInt( "reservebasic.user_id" ) );// �I�[�i�[���[�UID
                            dataAcc.setCiSeq( result.getInt( "ci.seq" ) ); // �z�e��ID�i�n�s�z�e�j

                            // ret = OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );
                            OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );
                            Logging.info( "STEP16", "ret:" + ret );

                            // �����̔Ԃ��ꂽ���|�`�[No�擾
                            accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, connection );
                            Logging.info( "STEP17", "accrecvSlipNo:" + accrecvSlipNo );
                            dataAcc.setAccrecvSlipNo( accrecvSlipNo );

                            // �`�[����No������
                            slipDetailNo = 0;
                            additionFlag = true; // �ǉ������̂�
                        }
                        else
                        {
                            // �����|�f�[�^�X�V
                            // dataAcc = new DataBkoAccountRecv();
                            // if ( dataAcc.getData( result.getInt( "hotelbasic.id" ), result.getInt( "ci.seq" ) ) != false )
                            // {
                            if ( result.getInt( "point.amount" ) >= 0 )
                            {
                                dataAcc.setUsageCharge( result.getInt( "point.amount" ) );// ���p���z
                            }

                            // �擾���Ă���No��+1������
                            accrecvSlipNo = dataAcc.getAccrecvSlipNo();

                            if ( POINT_KIND_RIYOU == result.getInt( "point.point_kind" ) )
                            {
                                // �t�^
                                accrecvAmount += result.getInt( "point.point" ) * 2;
                            }
                            else if ( POINT_KIND_YOYAKU == result.getInt( "point.point_kind" ) )
                            {
                                // �\��
                                accrecvAmount += result.getInt( "point.amount" );
                            }
                            else if ( POINT_KIND_WARIBIKI == result.getInt( "point.point_kind" ) )
                            {
                                // �g�p
                                accrecvAmount += result.getInt( "point.point" );
                            }
                            else if ( POINT_KIND_BONUS == result.getInt( "point.point_kind" ) )
                            {
                                // �\��{�[�i�X
                                accrecvAmount += result.getInt( "point.point" );
                            }

                            if ( result.getInt( "hotelbasic.id" ) > 0 )
                            {
                                dataAcc.setId( result.getInt( "hotelbasic.id" ) );// �����ԍ��i�n�s�^�b�`�j
                            }
                            if ( result.getString( "point.room_no" ).compareTo( "" ) != 0 )
                            {
                                dataAcc.setHtRoomNo( result.getString( "point.room_no" ) );// �����ԍ��i�n�s�^�b�`�j
                            }
                            // �`�[�ԍ����ǉ�����Ă�����o�^
                            if ( result.getInt( "point.slip_no" ) > 0 )
                            {
                                dataAcc.setHtSlipNo( result.getInt( "point.slip_no" ) );
                            }

                            dataAcc.setAccrecvAmount( accrecvAmount ); // ���|���z
                            dataAcc.setAccrecvBalance( accrecvAmount ); // ���|�c
                            dataAcc.setHappyBalance( result.getInt( "point.then_point" ) );// �n�s�[�c��
                            Logging.info( query, "STEP15-2" + ",id=" + dhc.getId() + ",Seq=" + dhc.getSeq() );

                            updateRecv( prestate, connection, dataAcc );
                            // }
                        }

                        switch( result.getInt( "point.point_kind" ) )
                        {
                            case POINT_KIND_RAITEN:

                                // ���X�n�s�[
                                dataAccDetail.setSlipKind( 0 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_100 );
                                dataAccDetail.setAmount( 0 );
                                break;

                            case POINT_KIND_RIYOU:

                                // �t�^
                                dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_110 );
                                dataAccDetail.setAmount( result.getInt( "point.point" ) * 2 );
                                break;

                            case POINT_KIND_WARIBIKI:

                                // �g�p
                                dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_120 );
                                dataAccDetail.setAmount( result.getInt( "point.point" ) );
                                break;

                            case POINT_KIND_YOYAKU:

                                // �\��n�s�[
                                dataAccDetail.setSlipKind( 0 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_130 );
                                dataAccDetail.setAmount( 0 );
                                break;

                            case POINT_KIND_BONUS:

                                // �\��{�[�i�X
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

                            // ���|���׃f�[�^�ǉ�
                            OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );
                        }
                    }
                }

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
            }
            catch ( Exception e )
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                Logging.error( "LogicOwnerBkoHapiTouch.insertData] Exception:" + e.toString(), "ROLLBACK" );

                System.out.println( e.toString() );

                // �G���[���[�����M
                sendErrorMail( Message.getMessage( "erro.30002", "���ߏ���" ) + e.toString() );

                return false;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerHapiTouch.excute] Exception=" + e.toString() );
            Logging.error( "LogicOwnerHapiTouch.insertData] Exception:" + e.toString(), "" );

            // �G���[���[�����M
            sendErrorMail( Message.getMessage( "erro.30002", "���ߏ���" ) + e.toString() );

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

        // ����|���f�[�^�A����|�����׃f�[�^���擾
        BkoAccountRecv bko;
        bko = new BkoAccountRecv();
        ret = bko.getDetailData( duppt );

        if ( ret != false )
        {
            ret = this.cancelData( userId, duppt, bko );
        }
        else
        {
            ret = true; // ���|�f�[�^���Ȃ������̂ōX�V���Ȃ������B
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
                // �������J�n
                controlChangeFlg = true;
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // ���|�f�[�^�쐬
                // �ΏۂƂȂ郆�[�U�|�C���g�ꎞ�f�[�^�擾
                query = "SELECT ";
                query = query + "point.user_id, ";
                query = query + "point.seq, ";
                query = query + "point.point, ";
                query = query + "point.then_point, ";
                query = query + "point.amount, "; // �����̋��z(point_kind=23)or���p���z(point_kind=22)
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

                query = query + "FROM hh_hotel_basic hotelbasic "; // �z�e����{�f�[�^
                query = query + "INNER JOIN hh_rsv_reserve_basic reservebasic ON reservebasic.id = hotelbasic.id ";

                // query = query + "INNER JOIN hh_user_point_pay_temp point ON point.hotenavi_id = hotelbasic.hotenavi_id ";
                // ��L��URL�������z�e�i�rID�ŕ����̃z�e��ID�������Ă����ꍇ�ɁA���܂������Ȃ����߉��L�̌��������ɏC���@2015/01/16�@Tashiro
                query = query + "INNER JOIN hh_user_point_pay_temp point ON point.ext_code = hotelbasic.id ";

                query = query + "INNER JOIN hh_bko_rel_billing_hotel billingHotel ON billingHotel.id = hotelbasic.id ";

                query = query + "WHERE point.user_id = ? AND point.seq = ? ";
                query = query + "AND (point.point_kind = 21 OR point.point_kind = 22 OR point.point_kind = 23 OR point.point_kind = 24) ";
                query = query + "ORDER BY hotelbasic.id ,visit_seq ,point.point_kind";

                // ���|�f�[�^�쐬
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userId );
                prestate.setInt( 2, duppt.getSeq() );
                result = prestate.executeQuery();

                if ( result != null )
                {
                    int wkVisitSeq = -1; // �Ǘ��ԍ�
                    int accrecvSlipNo = -1;
                    int slipDetailNo = -1;

                    // ���|�f�[�^���X�V���Ă���
                    // �@���|�f�[�^�ǉ����A���|���גǉ����u���C�N���ĂȂ���·B���|�f�[�^�X�V���C���|���׍X�V
                    // �@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@���u���C�N���Ă�����@���|�f�[�^�ǉ����E�E�E
                    while( result.next() )
                    {
                        int accrecvAmount = 0; // ���|���z

                        // �ǉ��t���O=false�i�V�K�Ńf�[�^�ǉ��j
                        if ( additionFlag == false )
                        {
                            // �����|�f�[�^�ǉ�
                            dataAcc = new DataBkoAccountRecv();
                            dataAcc.setHotelId( result.getString( "hotelbasic.hotenavi_id" ) ); // �z�e��ID�i�z�e�i�r�j
                            dataAcc.setId( result.getInt( "id" ) ); // �z�e��ID�i�n�s�z�e�j
                            dataAcc.setAddUpDate( lastUpdate ); // �v���
                            dataAcc.setBillCd( result.getInt( "billingHotel.bill_cd" ) );// ������R�[�h
                            dataAcc.setBillName( "" ); // �����於

                            dataAcc.setPersonName( OwnerBkoCommon.GetPersonName( prestate, connection, result.getString( "hotelbasic.hotenavi_id" ), result.getInt( "point.employee_code" ) ) );// �S���Җ�
                            dataAcc.setUserManagementNo( result.getInt( "user_seq" ) );// ���[�U�Ǘ��ԍ�
                            if ( result.getInt( "point.ci_date" ) > 0 )
                            {
                                dataAcc.setUsageDate( result.getInt( "point.ci_date" ) );// ���p��
                            }
                            else
                            {
                                dataAcc.setUsageDate( result.getInt( "point.get_date" ) );// ���p��
                            }

                            if ( result.getInt( "point.ci_time" ) > 0 )
                            {
                                dataAcc.setUsageTime( result.getInt( "point.ci_time" ) );// ���p����
                            }
                            else
                            {
                                dataAcc.setUsageTime( result.getInt( "point.get_time" ) );// ���p����
                            }
                            dataAcc.setHtSlipNo( result.getInt( "slip_no" ) );// �`�[No�i�n�s�^�b�`�j
                            dataAcc.setHtRoomNo( result.getString( "room_no" ) );// �����ԍ��i�n�s�^�b�`�j
                            dataAcc.setUsageCharge( result.getInt( "amount" ) );// ���p���z

                            if ( POINT_KIND_RIYOU == result.getInt( "point_kind" ) )
                            {
                                // �t�^
                                accrecvAmount = result.getInt( "point" ) * 2;
                            }
                            else if ( POINT_KIND_YOYAKU == result.getInt( "point_kind" ) )
                            {
                                // �\��
                                accrecvAmount = result.getInt( "amount" );
                            }
                            else if ( POINT_KIND_WARIBIKI == result.getInt( "point_kind" ) )
                            {
                                // �g�p
                                accrecvAmount = result.getInt( "point" );
                            }
                            else if ( POINT_KIND_BONUS == result.getInt( "point_kind" ) )
                            {
                                // �\��{�[�i�X
                                accrecvAmount = result.getInt( "point" );
                            }
                            // if ( !result.getString( "point.ext_string" ).equals( "" ) )
                            // {
                            // if ( drr.getData( result.getInt( "hotelbasic.id" ), result.getString( "point.ext_string" ) ) )
                            // {
                            // dataAcc.setReceiveCharge( drr.getChargeTotal() ); // �\����z
                            // }
                            // }
                            dataAcc.setAccrecvAmount( accrecvAmount ); // ���|���z
                            dataAcc.setAccrecvBalance( accrecvAmount );// ���|�c
                            dataAcc.setHappyBalance( result.getInt( "then_point" ) );// �n�s�[�c��
                            dataAcc.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );// ���ߏ����敪
                            dataAcc.setOwnerHotelId( result.getString( "reservebasic.hotel_id" ) );// �I�[�i�[�z�e��ID
                            dataAcc.setOwnerUserId( result.getInt( "reservebasic.user_id" ) );// �I�[�i�[���[�UID
                            dataAcc.setCiSeq( ciSeq );// �`�F�b�N�C���R�[�h

                            // ret=OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );
                            OwnerBkoCommon.RegistRecv( prestate, connection, dataAcc );

                            // �����̔Ԃ��ꂽ���|�`�[No�擾
                            accrecvSlipNo = OwnerBkoCommon.GetInsertedAccrecvSlipNo( prestate, connection );
                            dataAcc.setAccrecvSlipNo( accrecvSlipNo );

                            // �`�[����No������
                            slipDetailNo = 1;
                        }
                        else
                        {
                            // �����|�f�[�^�X�V

                            // �������|�̏ꍇ�A�`�[����No + 1
                            dataAcc = bko.getBkoAccountRecv();
                            if ( result.getInt( "amount" ) > 0 )
                            {
                                dataAcc.setUsageCharge( result.getInt( "amount" ) );// ���p���z
                            }

                            // �擾���Ă���No��+1������
                            accrecvSlipNo = bko.getBkoAccountRecv().getAccrecvSlipNo();
                            slipDetailNo = bko.getSlipDetailNo() + 1;

                            if ( POINT_KIND_RIYOU == result.getInt( "point_kind" ) )
                            {
                                // �t�^
                                accrecvAmount = result.getInt( "point" ) * 2;
                            }
                            else if ( POINT_KIND_YOYAKU == result.getInt( "point_kind" ) )
                            {
                                // �\��
                                accrecvAmount = result.getInt( "amount" );
                            }
                            else if ( POINT_KIND_WARIBIKI == result.getInt( "point_kind" ) )
                            {
                                // �g�p
                                accrecvAmount = result.getInt( "point" );
                            }
                            else if ( POINT_KIND_BONUS == result.getInt( "point_kind" ) )
                            {
                                // �\��{�[�i�X
                                accrecvAmount = result.getInt( "point" );
                            }

                            if ( result.getInt( "id" ) > 0 )
                            {
                                dataAcc.setId( result.getInt( "id" ) );// �����ԍ��i�n�s�^�b�`�j
                            }
                            if ( result.getString( "room_no" ).compareTo( "" ) != 0 )
                            {
                                dataAcc.setHtRoomNo( result.getString( "room_no" ) );// �����ԍ��i�n�s�^�b�`�j
                            }
                            // �`�[�ԍ����ǉ�����Ă�����o�^
                            if ( result.getInt( "slip_no" ) > 0 )
                            {
                                dataAcc.setHtSlipNo( result.getInt( "slip_no" ) );
                            }

                            dataAcc.setAccrecvAmount( dataAcc.getAccrecvAmount() + accrecvAmount ); // ���|���z
                            dataAcc.setAccrecvBalance( dataAcc.getAccrecvBalance() + accrecvAmount ); // ���|�c
                            dataAcc.setHappyBalance( result.getInt( "then_point" ) );// �n�s�[�c��

                            updateRecv( prestate, connection, dataAcc );
                        }

                        // ���|���׃f�[�^�@���ʍ��ڐݒ�
                        DataBkoAccountRecvDetail dataAccDetail = new DataBkoAccountRecvDetail();
                        dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                        dataAccDetail.setSlipDetailNo( slipDetailNo );
                        dataAccDetail.setPoint( result.getInt( "point" ) );
                        dataAccDetail.setId( result.getInt( "id" ) );
                        dataAccDetail.setReserveNo( result.getString( "point.ext_string" ) );
                        dataAccDetail.setUserId( result.getString( "user_id" ) );
                        dataAccDetail.setSeq( result.getInt( "seq" ) );
                        dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );

                        // ���|���׃f�[�^�o�^
                        switch( result.getInt( "point_kind" ) )
                        {
                            case POINT_KIND_RAITEN:

                                // ���X�n�s�[
                                dataAccDetail.setSlipKind( 0 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_100 );
                                dataAccDetail.setAmount( 0 );
                                break;

                            case POINT_KIND_RIYOU:

                                // �t�^
                                dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_110 );
                                dataAccDetail.setAmount( result.getInt( "point" ) * 2 );
                                break;

                            case POINT_KIND_WARIBIKI:

                                // �g�p
                                dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_120 );
                                dataAccDetail.setAmount( result.getInt( "point" ) );
                                break;

                            case POINT_KIND_YOYAKU:

                                // �\��n�s�[
                                dataAccDetail.setSlipKind( 0 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_130 );
                                dataAccDetail.setAmount( 0 );
                                break;

                            case POINT_KIND_BONUS:

                                // �\��{�[�i�X
                                dataAccDetail.setSlipKind( 0 );
                                dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_150 );
                                dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_150 );
                                dataAccDetail.setAmount( 0 );
                                break;

                            default:
                                break;
                        }

                        OwnerBkoCommon.RegistRecvDetail( prestate, connection, dataAccDetail );

                        wkVisitSeq = result.getInt( "visit_seq" ); // ���X�Ǘ��ԍ�
                    }
                }

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
                System.out.println( "[LogicOwnerBkoHapiTouth.insertData] closingYYYYMM = " + closingDate + ", closingKbn = " + closingKbn );
            }
            catch ( Exception e )
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                Logging.error( "LogicOwnerBkoClosing.insertData] Exception:" + e.toString(), "ROLLBACK" );

                System.out.println( e.toString() );

                // �G���[���[�����M
                sendErrorMail( Message.getMessage( "erro.30002", "���ߏ���" ) + e.toString() );

                return false;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.excute] Exception=" + e.toString() );
            Logging.error( "LogicOwnerBkoClosing.insertData] Exception:" + e.toString(), "" );

            // �G���[���[�����M
            sendErrorMail( Message.getMessage( "erro.30002", "���ߏ���" ) + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }

    /***
     * ����|���f�[�^�X�V
     * 
     * @param userId ���[�UID
     * @param duppt ���[�U�L���|�C���g�ꎞ�f�[�^�iDataUserPointPayTemp�j
     * @param bko ����|���f�[�^(BkoAccountRec)
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
        int wkVisitSeq = -1; // �Ǘ��ԍ�
        int accrecvSlipNo = -1;
        int slipDetailNo = -1;
        int accrecvAmount = 0; // ���|���z
        int accrecvAmountOld = 0; // ���|���z(�ȑO)

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            try
            {
                // �������J�n
                controlChangeFlg = true;
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // �����|�f�[�^�X�V

                // �������|�̏ꍇ�A�`�[����No + 1
                dataAcc = bko.getBkoAccountRecv();
                dataAccDetail = bko.getBkoAccountRecvDetail();

                // ���z = �]���̋��z - �ߋ��o�^���̋��z + ������̋��z
                // dataAcc.setUsageCharge( dataAcc.getUsageCharge() - dataAccDetail.getAmount() + duppt.getAmount() );// ���p���z
                dataAcc.setUsageCharge( duppt.getAmount() );// ���p���z
                //
                accrecvSlipNo = bko.getBkoAccountRecv().getAccrecvSlipNo();
                slipDetailNo = bko.getSlipDetailNo();

                if ( POINT_KIND_RIYOU == duppt.getPointKind() )
                {
                    // �t�^
                    accrecvAmount = duppt.getPoint() * 2;
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_YOYAKU == duppt.getPointKind() )
                {
                    // �\��
                    accrecvAmount = duppt.getAmount();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_WARIBIKI == duppt.getPointKind() )
                {
                    // �g�p
                    accrecvAmount = duppt.getPoint();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_BONUS == duppt.getPointKind() )
                {
                    // �g�p
                    accrecvAmount = duppt.getPoint();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }

                if ( duppt.getRoomNo().compareTo( "" ) != 0 )
                {
                    dataAcc.setHtRoomNo( duppt.getRoomNo() );// �����ԍ��i�n�s�^�b�`�j
                }
                if ( duppt.getSlipNo() > 0 )
                {
                    dataAcc.setHtSlipNo( duppt.getSlipNo() );// �����ԍ��i�n�s�^�b�`�j
                }
                dataAcc.setHappyBalance( duppt.getThenPoint() );// �n�s�[�c��

                // ����|�����z = �]���̋��z - �ߋ��o�^���̋��z + ������̋��z
                dataAcc.setAccrecvAmount( dataAcc.getAccrecvAmount() - accrecvAmountOld + accrecvAmount ); // ���|���z
                dataAcc.setAccrecvBalance( dataAcc.getAccrecvBalance() - accrecvAmountOld + accrecvAmount ); // ���|�c

                // ����|���f�[�^�̍X�V
                updateRecv( prestate, connection, dataAcc );

                // ���|���׃f�[�^�@���ʍ��ڐݒ�
                dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                dataAccDetail.setSlipDetailNo( slipDetailNo );
                dataAccDetail.setPoint( duppt.getPoint() );
                // dataAccDetail.setId( result.getInt( "id" ) );
                // dataAccDetail.setReserveNo( result.getString( "point.ext_string" ) );
                dataAccDetail.setUserId( duppt.getUserId() );
                dataAccDetail.setSeq( duppt.getSeq() );
                dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );

                // ���|���׃f�[�^�o�^
                switch( duppt.getPointKind() )
                {
                    case POINT_KIND_RAITEN:

                        // ���X�n�s�[
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_100 );
                        dataAccDetail.setAmount( 0 );
                        break;

                    case POINT_KIND_RIYOU:

                        // �t�^
                        dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_110 );
                        dataAccDetail.setAmount( duppt.getPoint() * 2 );
                        break;

                    case POINT_KIND_WARIBIKI:

                        // �g�p
                        dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_120 );
                        dataAccDetail.setAmount( duppt.getPoint() );
                        break;

                    case POINT_KIND_YOYAKU:

                        // �\��n�s�[
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_130 );
                        dataAccDetail.setAmount( 0 );
                        break;

                    case POINT_KIND_BONUS:

                        // �\��{�[�i�X
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_150 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_150 );
                        dataAccDetail.setAmount( 0 );
                        break;

                    default:
                        break;
                }
                // ����|�����ׂ̍X�V
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

                // �G���[���[�����M
                sendErrorMail( Message.getMessage( "erro.30002", "���ߏ���" ) + e.toString() );

                return false;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.excute] Exception=" + e.toString() );

            // �G���[���[�����M
            sendErrorMail( Message.getMessage( "erro.30002", "���ߏ���" ) + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

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

        // ���|���׃f�[�^
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
     * ���|�f�[�^�X�V
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param dataAccount DataBkoAccountRecv
     * @return true:����Afalse:���s
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
     * ����|���f�[�^�X�V
     * 
     * @param userId ���[�UID
     * @param duppt ���[�U�L���|�C���g�ꎞ�f�[�^�iDataUserPointPayTemp�j
     * @param bko ����|���f�[�^(BkoAccountRec)
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
        int wkVisitSeq = -1; // �Ǘ��ԍ�
        int accrecvSlipNo = -1;
        int slipDetailNo = -1;
        int accrecvAmount = 0; // ���|���z
        int accrecvAmountOld = 0; // ���|���z(�ȑO)

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            try
            {
                // �������J�n
                controlChangeFlg = true;
                query = "START TRANSACTION ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // �����|�f�[�^�X�V

                // �������|�̏ꍇ�A�`�[����No + 1
                dataAcc = bko.getBkoAccountRecv();
                dataAccDetail = bko.getBkoAccountRecvDetail();

                // ���z = �]���̋��z - �ߋ��o�^���̋��z + ������̋��z
                // dataAcc.setUsageCharge( dataAcc.getUsageCharge() - dataAccDetail.getAmount() + duppt.getAmount() );// ���p���z
                dataAcc.setUsageCharge( duppt.getAmount() );// ���p���z
                //
                accrecvSlipNo = bko.getBkoAccountRecv().getAccrecvSlipNo();
                slipDetailNo = bko.getSlipDetailNo();

                if ( POINT_KIND_RIYOU == duppt.getPointKind() )
                {
                    // �t�^
                    accrecvAmount = duppt.getPoint() * 2;
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_YOYAKU == duppt.getPointKind() )
                {
                    // �\��
                    accrecvAmount = duppt.getAmount();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_WARIBIKI == duppt.getPointKind() )
                {
                    // �g�p
                    accrecvAmount = duppt.getPoint();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }
                else if ( POINT_KIND_BONUS == duppt.getPointKind() )
                {
                    // �\��{�[�i�X
                    accrecvAmount = duppt.getPoint();
                    accrecvAmountOld = dataAccDetail.getAmount();
                }

                if ( duppt.getRoomNo().compareTo( "" ) != 0 )
                {
                    dataAcc.setHtRoomNo( duppt.getRoomNo() );// �����ԍ��i�n�s�^�b�`�j
                }
                if ( duppt.getSlipNo() > 0 )
                {
                    dataAcc.setHtSlipNo( duppt.getSlipNo() );// �����ԍ��i�n�s�^�b�`�j
                }
                dataAcc.setHappyBalance( duppt.getThenPoint() );// �n�s�[�c��

                // ����|�����z = �L�����Z���Ȃ̂Ŏ擾�����f�[�^�����̂܂܃Z�b�g
                dataAcc.setAccrecvAmount( accrecvAmount ); // ���|���z
                dataAcc.setAccrecvBalance( accrecvAmount ); // ���|�c

                // ����|���f�[�^�̍X�V
                updateRecv( prestate, connection, dataAcc );

                // ���|���׃f�[�^�@���ʍ��ڐݒ�
                dataAccDetail.setAccrecvSlipNo( accrecvSlipNo );
                dataAccDetail.setSlipDetailNo( slipDetailNo );
                dataAccDetail.setPoint( duppt.getPoint() );
                // dataAccDetail.setId( result.getInt( "id" ) );
                // dataAccDetail.setReserveNo( "" );
                dataAccDetail.setUserId( duppt.getUserId() );
                dataAccDetail.setSeq( duppt.getSeq() );
                dataAccDetail.setClosingKind( OwnerBkoCommon.CLOSING_KBN_MISYORI );

                // ���|���׃f�[�^�o�^
                switch( duppt.getPointKind() )
                {
                    case POINT_KIND_RAITEN:

                        // ���X�n�s�[
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_100 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_100 );
                        dataAccDetail.setAmount( 0 );
                        break;

                    case POINT_KIND_RIYOU:

                        // �t�^
                        dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_110 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_110 );
                        dataAccDetail.setAmount( duppt.getPoint() * 2 );
                        break;

                    case POINT_KIND_WARIBIKI:

                        // �g�p
                        dataAccDetail.setSlipKind( OwnerBkoCommon.SLIP_KIND_SEISAN );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_120 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_120 );
                        dataAccDetail.setAmount( duppt.getPoint() );
                        break;

                    case POINT_KIND_YOYAKU:

                        // �\��n�s�[
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_130 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_130 );
                        dataAccDetail.setAmount( 0 );
                        dataAccDetail.setPoint( 0 );
                        break;

                    case POINT_KIND_BONUS:

                        // �\��{�[�i�X
                        dataAccDetail.setSlipKind( 0 );
                        dataAccDetail.setAccountTitleCd( OwnerBkoCommon.ACCOUNT_TITLE_CD_150 );
                        dataAccDetail.setAccountTitleName( OwnerBkoCommon.ACCOUNT_TITLE_NAME_150 );
                        dataAccDetail.setAmount( 0 );
                        dataAccDetail.setPoint( 0 );
                        break;

                    default:
                        break;
                }
                // ����|�����ׂ̍X�V
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

                // �G���[���[�����M
                sendErrorMail( Message.getMessage( "erro.30002", "���ߏ���" ) + e.toString() );

                return false;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[LogicOwnerBkoClosing.excute] Exception=" + e.toString() );

            // �G���[���[�����M
            sendErrorMail( Message.getMessage( "erro.30002", "���ߏ���" ) + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
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

        Logging.info( "[LogincOwnerBkoHapiTouch] " + getMessage( MAIL_SUCCESSTITLE ) + ":" + msg );
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

        Logging.error( "[LogincOwnerBkoHapiTouch] " + getMessage( MAIL_ERRORTITLE ) + ":" + msg );
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
