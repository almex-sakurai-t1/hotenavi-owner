package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)�擾�N���X
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApPushDataList implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -5722164210085681271L;
    public static final String TABLE            = "ap_push_data_list";
    private int                type;                                    // 1:campaign,2:checkout
    private int                id;                                      //
    private int                pushSeq;                                 // �v�b�V���z�M�A��
    private String             userId;                                  // ���[�U�[ID
    private String             token;                                   // iOS:device token,Android:registration id
    private int                status;                                  // 0:�����M,1:���M��,9:�폜
    private int                sendDate;                                // ���M���t(YYYYMMDD)
    private int                sendTime;                                // ���M����(HHMMSS)
    private int                confirmDate;                             // �m�F���t(YYYYMMDD)
    private int                confirmTime;                             // �m�F����(HHMMSS)
    private int                hotelId;                                 //
    private int                hotelSeq;                                //

    private int                pushCount;                               // �z�M�ς݂̌���
    private int                execCount;                               // �����ς݂̌���

    /**
     * �f�[�^�����������܂��B
     */
    public DataApPushDataList()
    {
        this.type = 0;
        this.id = 0;
        this.pushSeq = 0;
        this.userId = "";
        this.token = "";
        this.status = 0;
        this.sendDate = 0;
        this.sendTime = 0;
        this.confirmDate = 0;
        this.confirmTime = 0;
        this.hotelId = 0;
        this.hotelSeq = 0;
        this.pushCount = 0;
        this.execCount = 0;
    }

    public int getType()
    {
        return type;
    }

    public int getId()
    {
        return id;
    }

    public int getPushSeq()
    {
        return pushSeq;
    }

    public int gethotelId()
    {
        return hotelId;
    }

    public int getHotelSeq()
    {
        return hotelSeq;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getToken()
    {
        return token;
    }

    public int getStatus()
    {
        return status;
    }

    public int getSendDate()
    {
        return sendDate;
    }

    public int getSendTime()
    {
        return sendTime;
    }

    public int getConfirmDate()
    {
        return confirmDate;
    }

    public int getConfirmTime()
    {
        return confirmTime;
    }

    public int getPushCount()
    {
        return pushCount;
    }

    public int getExecCount()
    {
        return execCount;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setPushSeq(int pushSeq)
    {
        this.pushSeq = pushSeq;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setHotelSeq(int hotelSeq)
    {
        this.hotelSeq = hotelSeq;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setSendDate(int sendDate)
    {
        this.sendDate = sendDate;
    }

    public void setSendTime(int sendTime)
    {
        this.sendTime = sendTime;
    }

    public void setConfirmDate(int confirmDate)
    {
        this.confirmDate = confirmDate;
    }

    public void setConfirmTime(int confirmTime)
    {
        this.confirmTime = confirmTime;
    }

    public void setPushCount(int pushCount)
    {
        this.pushCount = pushCount;
    }

    public void setExecCount(int execCount)
    {
        this.execCount = execCount;
    }

    /****
     * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)�擾
     * 
     * @param pushSeq �v�b�V���z�M�A��
     * @param userId ���[�U�[ID
     * @param token iOS:device token,Android:registration id
     * @return
     */
    public boolean getData(int type, int id, int pushSeq, String userId, String token)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_push_data_list WHERE type = ? AND id = ? AND  push_seq = ? AND user_id = ? AND token = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, type );
            prestate.setInt( 2, id );
            prestate.setInt( 3, pushSeq );
            prestate.setString( 4, userId );
            prestate.setString( 5, token );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.type = result.getInt( "type" );
                    this.id = result.getInt( "id" );
                    this.pushSeq = result.getInt( "push_seq" );
                    this.userId = result.getString( "user_id" );
                    this.token = result.getString( "token" );
                    this.status = result.getInt( "status" );
                    this.sendDate = result.getInt( "send_date" );
                    this.sendTime = result.getInt( "send_time" );
                    this.confirmDate = result.getInt( "confirm_date" );
                    this.confirmTime = result.getInt( "confirm_time" );
                    this.hotelId = result.getInt( "hotel_id" );
                    this.hotelSeq = result.getInt( "hotel_seq" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataList.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)�擾
     * 
     * @param connection �R�l�N�V����
     * @param pushSeq �v�b�V���z�M�A��
     * @param userId ���[�U�[ID
     * @param token iOS:device token,Android:registration id
     * @return
     */
    public boolean getData(Connection connection, int type, int id, int pushSeq, String userId, String token)
    {
        boolean ret;
        String query;
        // Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_push_data_list WHERE type = ? AND id = ? AND  push_seq = ? AND user_id = ? AND token = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, type );
            prestate.setInt( 2, id );
            prestate.setInt( 3, pushSeq );
            prestate.setString( 4, userId );
            prestate.setString( 5, token );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.type = result.getInt( "type" );
                    this.id = result.getInt( "id" );
                    this.pushSeq = result.getInt( "push_seq" );
                    this.userId = result.getString( "user_id" );
                    this.token = result.getString( "token" );
                    this.status = result.getInt( "status" );
                    this.sendDate = result.getInt( "send_date" );
                    this.sendTime = result.getInt( "send_time" );
                    this.confirmDate = result.getInt( "confirm_date" );
                    this.confirmTime = result.getInt( "confirm_time" );
                    this.hotelId = result.getInt( "hotel_id" );
                    this.hotelSeq = result.getInt( "hotel_seq" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataList.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)�ݒ�
     * 
     * @param result �}�X�^�[�\�[�g���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.type = result.getInt( "type" );
                this.id = result.getInt( "id" );
                this.pushSeq = result.getInt( "push_seq" );
                this.userId = result.getString( "user_id" );
                this.token = result.getString( "token" );
                this.status = result.getInt( "status" );
                this.sendDate = result.getInt( "send_date" );
                this.sendTime = result.getInt( "send_time" );
                this.confirmDate = result.getInt( "confirm_date" );
                this.confirmTime = result.getInt( "confirm_time" );
                this.hotelId = result.getInt( "hotel_id" );
                this.hotelSeq = result.getInt( "hotel_seq" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataList.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)�}��
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT ap_push_data_list SET ";
        query += " type=?";
        query += ", id=?";
        query += ", push_seq=?";
        query += ", user_id=?";
        query += ", token=?";
        query += ", status=?";
        query += ", send_date=?";
        query += ", send_time=?";
        query += ", confirm_date=?";
        query += ", confirm_time=?";
        query += ", hotel_id=?";
        query += ", hotel_seq=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.type );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.pushSeq );
            prestate.setString( i++, this.userId );
            prestate.setString( i++, this.token );
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.sendDate );
            prestate.setInt( i++, this.sendTime );
            prestate.setInt( i++, this.confirmDate );
            prestate.setInt( i++, this.confirmTime );
            prestate.setInt( i++, this.hotelId );
            prestate.setInt( i++, this.hotelSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataList.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param pushSeq �v�b�V���z�M�A��
     * @param userId ���[�U�[ID
     * @param token iOS:device token,Android:registration id
     * @return
     */
    public boolean updateData(int type, int id, int pushSeq, String userId, String token)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_push_data_list SET ";
        query += " status=?";
        query += ", send_date=?";
        query += ", send_time=?";
        query += ", confirm_date=?";
        query += ", confirm_time=?";
        query += ", hotel_id=?";
        query += ", hotel_seq=?";
        query += " WHERE type = ? AND id = ? AND push_seq=? AND user_id=? AND token=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.sendDate );
            prestate.setInt( i++, this.sendTime );
            prestate.setInt( i++, this.confirmDate );
            prestate.setInt( i++, this.confirmTime );
            prestate.setInt( i++, this.hotelId );
            prestate.setInt( i++, this.hotelSeq );
            prestate.setInt( i++, this.type );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.pushSeq );
            prestate.setString( i++, this.userId );
            prestate.setString( i++, this.token );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataList.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param connection �R�l�N�V����
     * @param pushSeq �v�b�V���z�M�A��
     * @param userId ���[�U�[ID
     * @param token iOS:device token,Android:registration id
     * @return
     */
    public boolean updateData(Connection connection, int type, int id, int pushSeq, String userId, String token)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_push_data_list SET ";
        query += " status=?";
        query += ", send_date=?";
        query += ", send_time=?";
        query += ", confirm_date=?";
        query += ", confirm_time=?";
        query += ", hotel_id=?";
        query += ", hotel_seq=?";
        query += " WHERE type = ? AND id = ? AND push_seq=? AND user_id=? AND token=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.status );
            prestate.setInt( i++, this.sendDate );
            prestate.setInt( i++, this.sendTime );
            prestate.setInt( i++, this.confirmDate );
            prestate.setInt( i++, this.confirmTime );
            prestate.setInt( i++, this.hotelId );
            prestate.setInt( i++, this.hotelSeq );
            prestate.setInt( i++, this.type );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.pushSeq );
            prestate.setString( i++, this.userId );
            prestate.setString( i++, this.token );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataList.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /****
     * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)�����擾
     * 
     * @param type �^�C�v
     * @param pushSeq �v�b�V���z�M�A��
     * @param status �X�e�[�^�X
     * @return
     */
    public boolean getPushDataListCount(int type, int pushSeq, int status)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT count(*) AS CNT FROM ap_push_data_list WHERE type = ? AND push_seq = ? AND status = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, type );
            prestate.setInt( 2, pushSeq );
            prestate.setInt( 3, status );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.pushCount = result.getInt( "CNT" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataList.getPushCount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)�����擾
     * 
     * @param connection �R�l�N�V����
     * @param type �^�C�v
     * @param pushSeq �v�b�V���z�M�A��
     * @param status �X�e�[�^�X
     * @return
     */
    public boolean getPushDataListCount(Connection connection, int type, int pushSeq, int status)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT count(*) AS CNT FROM ap_push_data_list WHERE type = ? AND push_seq = ? AND status = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, type );
            prestate.setInt( 2, pushSeq );
            prestate.setInt( 3, status );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.pushCount = result.getInt( "CNT" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataList.getPushCount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /****
     * �v�b�V���z�M�f�[�^���X�g(ap_push_data_list)���������擾
     * 
     * @param connection �R�l�N�V����
     * @param type �^�C�v
     * @param pushSeq �v�b�V���z�M�A��
     * @return
     */
    public boolean getPushDataListExecCount(Connection connection, int type, int pushSeq)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT count(*) AS CNT FROM ap_push_data_list WHERE type = ? AND push_seq = ? AND status != 0 ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, type );
            prestate.setInt( 2, pushSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.execCount = result.getInt( "CNT" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataList.getPushCount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }
}
