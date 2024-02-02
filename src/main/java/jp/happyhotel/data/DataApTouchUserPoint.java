package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ���[�U�[�^�b�`�|�C���g�f�[�^(ap_touch_user_point)�擾�N���X
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApTouchUserPoint implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -6791241249806979423L;
    public static final String TABLE            = "ap_touch_user_point";
    private String             userId;                                  // ���[�U�[ID
    private int                id;                                      // �z�e��ID
    private int                ciSeq;                                   // �`�F�b�N�C���R�[�h(ap_touch_ci.seq)
    private int                code;                                    // �|�C���g�R�[�h(hh_master_point.code)
    private int                paySeq;                                  // hh_user_point_pay.seq
    private int                tempSeq;                                 // hh_user_point_pay_temp.seq
    private int                registDate;                              // �o�^���t(YYYYMMDD)
    private int                registTime;                              // �o�^����(HHMMSS)

    /**
     * �f�[�^�����������܂��B
     */
    public DataApTouchUserPoint()
    {
        this.userId = "";
        this.id = 0;
        this.ciSeq = 0;
        this.code = 0;
        this.paySeq = 0;
        this.tempSeq = 0;
        this.registDate = 0;
        this.registTime = 0;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getId()
    {
        return id;
    }
    
    public int getCiSeq()
    {
        return ciSeq;
    }

    public int getCode()
    {
        return code;
    }

    public int getPaySeq()
    {
        return paySeq;
    }

    public int getTempSeq()
    {
        return tempSeq;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    
    public void setCiSeq(int ciSeq)
    {
        this.ciSeq = ciSeq;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setPaySeq(int paySeq)
    {
        this.paySeq = paySeq;
    }

    public void setTempSeq(int tempSeq)
    {
        this.tempSeq = tempSeq;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    /****
     * ���[�U�[�^�b�`�|�C���g�f�[�^(ap_touch_user_point)�擾
     * 
     * @param userId ���[�U�[ID
     * @param ciSeq �`�F�b�N�C���R�[�h(ap_touch_ci.seq)
     * @param code �|�C���g�R�[�h(hh_master_point.code)
     * @return
     */
    public boolean getData(String userId, int id, int ciSeq, int code)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_touch_user_point WHERE user_id = ? AND id = ? AND ci_seq = ? AND code = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, id );
            prestate.setInt( 3, ciSeq );
            prestate.setInt( 4, code );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.id = result.getInt( "id" );
                    this.ciSeq = result.getInt( "ci_seq" );
                    this.code = result.getInt( "code" );
                    this.paySeq = result.getInt( "pay_seq" );
                    this.tempSeq = result.getInt( "temp_seq" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchUserPoint.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ���[�U�[�^�b�`�|�C���g�f�[�^(ap_touch_user_point)�ݒ�
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
                this.userId = result.getString( "user_id" );
                this.id = result.getInt( "id" );
                this.ciSeq = result.getInt( "ci_seq" );
                this.code = result.getInt( "code" );
                this.paySeq = result.getInt( "pay_seq" );
                this.tempSeq = result.getInt( "temp_seq" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchUserPoint.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ���[�U�[�^�b�`�|�C���g�f�[�^(ap_touch_user_point)�}��
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

        query = "INSERT ap_touch_user_point SET ";
        query += " user_id=?";
        query += ", id=?";
        query += ", ci_seq=?";
        query += ", code=?";
        query += ", pay_seq=?";
        query += ", temp_seq=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.ciSeq );
            prestate.setInt( i++, this.code );
            prestate.setInt( i++, this.paySeq );
            prestate.setInt( i++, this.tempSeq );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchUserPoint.insertData] Exception=" + e.toString() );
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
     * ���[�U�[�^�b�`�|�C���g�f�[�^(ap_touch_user_point)�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param userId ���[�U�[ID
     * @param id �z�e��ID
     * @param ciSeq �`�F�b�N�C���R�[�h(ap_touch_ci.seq)
     * @param code �|�C���g�R�[�h(hh_master_point.code)
     * @return
     */
    public boolean updateData(String userId, int id, int ciSeq, int code)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_touch_user_point SET ";
        query += " pay_seq=?";
        query += ", temp_seq=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += " WHERE user_id=? AND id=? AND ci_seq=? AND code=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.paySeq );
            prestate.setInt( i++, this.tempSeq );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.ciSeq );
            prestate.setInt( i++, this.code );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchUserPoint.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
