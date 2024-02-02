package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;

/**
 * �\��Ǘ��r�W�l�X���W�b�N
 */
public class LogicOwnerRsvManage implements Serializable
{

    private static final long  serialVersionUID = -7252952933704381947L;
    private FormOwnerRsvManage frm;

    /* �t�H�[���I�u�W�F�N�g */
    public FormOwnerRsvManage getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvManage frm)
    {
        this.frm = frm;
    }

    /**
     * �\��Ǘ����擾
     * 
     * @param �Ȃ�
     * @return
     */
    public void getRsvManage() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = query + "SELECT sales_flag FROM hh_rsv_reserve_basic ";
        query = query + "WHERE id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();
            while( result.next() )
            {
                frm.setHotelSalesFlg( result.getInt( "sales_flag" ) );
            }

            // ���R�[�h�����擾
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // �Y���f�[�^���Ȃ��ꍇ
            if ( count == 0 )
            {
                frm.setSelHotelErrMsg( Message.getMessage( "erro.30001", "�z�e���\���{���" ) );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.getRsvManage] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * �J�����_�[�̔̔��t���O�X�V
     * 
     * @param hotelId �z�e��ID
     * @param calDate �Ώۓ��t
     * @param salesFlg �̔��t���O
     * @param userId ���[�U�[ID
     * @return true:���������Afalse:�������s
     */
    public boolean updCalendarSalesFlg(int hotelId, int calDate, int salesFlg, int userId) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_hotel_calendar SET ";
        query = query + " sales_flag = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ? ";
        query = query + "   AND cal_date = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, salesFlg );
            prestate.setInt( 2, userId );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 5, hotelId );
            prestate.setInt( 6, calDate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.updCalendarSalesFlg] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvManage.updCalendarSalesFlg] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �����c���f�[�^�X�V
     * 
     * @param hotelId �z�e��ID
     * @param calDate �Ώۓ��t
     * @param status �X�e�[�^�X�̍X�V�l
     * @param condStatus �X�V�����Ŏg�p����X�e�[�^�X
     * @return true:���������Afalse:�������s
     */
    public boolean updRoomRemainder(int hotelId, int calDate, int status, int condStatus) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_room_remainder SET ";
        query = query + " status = ? ";
        query = query + " WHERE id = ? ";
        query = query + "   AND cal_date = ? ";
        query = query + "   AND status = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, status );
            prestate.setInt( 2, hotelId );
            prestate.setInt( 3, calDate );
            prestate.setInt( 4, condStatus );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.updRoomRemainder] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvManage.updRoomRemainder] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �����̔̔��t���O�X�V
     * 
     * @param hotelId �z�e��ID
     * @param seq �Ǘ��ԍ�
     * @param salesFlg �̔��t���O
     * @param userId ���[�U�[ID
     * @return true:���������Afalse:�������s
     */
    public boolean updRoomSalesFlg(int hotelId, int seq, int salesFlg, int userId) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_room SET ";
        query = query + " sales_flag = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ? ";
        query = query + "   AND seq = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, salesFlg );
            prestate.setInt( 2, userId );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 5, hotelId );
            prestate.setInt( 6, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.updRoomSalesFlg] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvManage.updRoomSalesFlg] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �\�񕔉��}�X�^�ǉ�
     * 
     * @param hotelId �z�e��ID
     * @param seq �Ǘ��ԍ�
     * @param salesFlg �̔��t���O
     * @param userId ���[�U�[ID
     * @return �Ȃ�
     * @throws Exception
     */
    public boolean execInsRsvRoom(int hotelId, int seq, int salesFlg, int userId) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_rsv_room SET ";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " room_name = ?,";
        query = query + " room_pr = ?,";
        query = query + " remarks = ?,";
        query = query + " sales_flag = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            prestate.setString( 3, "" );
            prestate.setString( 4, "" );
            prestate.setString( 5, "" );
            prestate.setInt( 6, salesFlg );
            prestate.setInt( 7, userId );
            prestate.setInt( 8, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 9, Integer.parseInt( DateEdit.getTime( 1 ) ) );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.execInsRsvRoom] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvManage.execInsRsvRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �\�񕔉��}�X�^�ǉ�
     * 
     * @param hotelId �z�e��ID
     * @param seq �Ǘ��ԍ�
     * @param salesFlg �̔��t���O
     * @param userId ���[�U�[ID
     * @return �Ȃ�
     * @throws Exception
     */
    public boolean execInsRsvRoom(int hotelId, int seq, int salesFlg, int userId, String hotenaviId) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_rsv_room SET ";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " room_name = ?,";
        query = query + " room_pr = ?,";
        query = query + " remarks = ?,";
        query = query + " sales_flag = ?,";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            prestate.setString( 3, "" );
            prestate.setString( 4, "" );
            prestate.setString( 5, "" );
            prestate.setInt( 6, salesFlg );
            prestate.setString( 7, hotenaviId );
            prestate.setInt( 8, userId );
            prestate.setInt( 9, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 10, Integer.parseInt( DateEdit.getTime( 1 ) ) );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.execInsRsvRoom] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvManage.execInsRsvRoom] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �v�����̔̔��t���O�X�V
     * 
     * @param hotelId �z�e��ID
     * @param planId �v����ID
     * @param salesFlg �̔��t���O
     * @param userId ���[�U�[ID
     * @return true:���������Afalse:�������s
     */
    public boolean updPlanSalesFlg(int hotelId, int planId, int salesFlg, int userId) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_plan SET ";
        query = query + " sales_flag = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ? ";
        query = query + "   AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, salesFlg );
            prestate.setInt( 2, userId );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 5, hotelId );
            prestate.setInt( 6, planId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.updPlanSalesFlg] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvManage.updPlanSalesFlg] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �v�����̔̔��t���O�X�V
     * 
     * @param hotelId �z�e��ID
     * @param salesFlg �̔��t���O
     * @param userId ���[�U�[ID
     * @return true:���������Afalse:�������s
     */
    public boolean updHotelSalesFlg(int hotelId, int salesFlg, int userId) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_reserve_basic SET ";
        query = query + " sales_flag = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, salesFlg );
            prestate.setInt( 2, userId );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 5, hotelId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.updHotelSalesFlg] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvManage.updPlanSalesFlg] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �J�����_�[�̔̔��t���O�X�V
     * 
     * @param hotelId �z�e��ID
     * @param calDate �Ώۓ��t
     * @param planId �v����ID
     * @param userId ���[�U�[ID
     * @return true:���������Afalse:�������s
     */
    public boolean updChargeMode(int hotelId, int calDate, int planId, int chargeModeId, int userId) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_day_charge SET ";
        query = query + " charge_mode_id = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ? ";
        query = query + " AND cal_date = ? ";
        query = query + " AND plan_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, chargeModeId );
            prestate.setInt( 2, userId );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 5, hotelId );
            prestate.setInt( 6, calDate );
            prestate.setInt( 7, planId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.updCalendarSalesFlg] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvManage.updCalendarSalesFlg] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
