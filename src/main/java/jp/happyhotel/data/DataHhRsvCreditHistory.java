package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �\��N���W�b�g�����擾�N���X
 * 
 * @author takeshi-sakurai
 * @version 1.00 2015/5/22
 */
public class DataHhRsvCreditHistory implements Serializable
{
    /**
	 *
	 */
    private static final long  serialVersionUID = -8809144985532341051L;
    public static final String TABLE            = "hh_rsv_credit_history";
    private String             orderId;                                   // �I�[�_�[ID
    private int                jobSeq;                                    // �����A��
    private String             jobCd;                                     // �����敪�FGMO�d����JobCd�@�eCHECK�e:�L�����`�F�b�N,�fAUTH�f:������,�eSALES�e:������
    private String             tranid;                                    // �g�����U�N�V����ID
    private String             approved;                                  // ���F�ԍ�
    private String             forwarded;                                 // �d����R�[�h
    private String             reserveNo;                                 // �\��No
    private int                amount;                                    // ���z
    private int                registDate;                                // �������t�@YYYYMMDD
    private int                registTime;                                // ���������@HHMMSS
    private int                errorFlag;                                 // 1:�G���[����

    /**
     * �f�[�^�����������܂��B
     */
    public DataHhRsvCreditHistory()
    {
        this.orderId = "";
        this.jobSeq = 0;
        this.jobCd = "";
        this.tranid = "";
        this.approved = "";
        this.forwarded = "";
        this.reserveNo = "";
        this.amount = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.errorFlag = 0;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public int getJobSeq()
    {
        return jobSeq;
    }

    public String getJobCd()
    {
        return jobCd;
    }

    public String getTranid()
    {
        return tranid;
    }

    public String getApproved()
    {
        return approved;
    }

    public String getForwarded()
    {
        return forwarded;
    }

    public String getResearveNo()
    {
        return reserveNo;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getErrorFlag()
    {
        return errorFlag;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public void setJobSeq(int jobSeq)
    {
        this.jobSeq = jobSeq;
    }

    public void setJobCd(String jobCd)
    {
        this.jobCd = jobCd;
    }

    public void setTranid(String tranid)
    {
        this.tranid = tranid;
    }

    public void setApproved(String approved)
    {
        this.approved = approved;
    }

    public void setForwarded(String forwarded)
    {
        this.forwarded = forwarded;
    }

    public void setResearveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setErrorFlag(int errorFlag)
    {
        this.errorFlag = errorFlag;
    }

    /****
     * �\��N���W�b�g�����擾
     * 
     * @param orderId �I�[�_�[ID
     * @param jobSeq �����A��
     * @return
     */
    public boolean getData(String orderId, int jobSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM newRsvDB.hh_rsv_credit_history WHERE order_id = ? AND job_seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, orderId );
            prestate.setInt( 2, jobSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.orderId = result.getString( "order_id" );
                    this.jobSeq = result.getInt( "job_seq" );
                    this.jobCd = result.getString( "job_cd" );
                    this.tranid = result.getString( "tranid" );
                    this.approved = result.getString( "approved" );
                    this.forwarded = result.getString( "forwarded" );
                    this.reserveNo = result.getString( "reserve_no" );
                    this.amount = result.getInt( "amount" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.errorFlag = result.getInt( "error_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �\��N���W�b�g�����f�[�^
     * 
     * @param orderId �I�[�_�[ID
     * @return
     */
    public boolean getData(String orderId)
    {
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            return getData( connection, orderId );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditHistory.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }

    }

    /****
     * �\��N���W�b�g�����f�[�^
     * 
     * @param orderId �I�[�_�[ID
     * @return
     */
    public boolean getData(Connection connection, String orderId)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM newRsvDB.hh_rsv_credit_history WHERE order_id = ? ORDER BY job_seq DESC";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, orderId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.orderId = result.getString( "order_id" );
                    this.jobSeq = result.getInt( "job_seq" );
                    this.jobCd = result.getString( "job_cd" );
                    this.tranid = result.getString( "tranid" );
                    this.approved = result.getString( "approved" );
                    this.forwarded = result.getString( "forwarded" );
                    this.reserveNo = result.getString( "reserve_no" );
                    this.amount = result.getInt( "amount" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.errorFlag = result.getInt( "error_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �\��N���W�b�g����ݒ�
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
                this.orderId = result.getString( "order_id" );
                this.jobSeq = result.getInt( "job_seq" );
                this.jobCd = result.getString( "job_cd" );
                this.tranid = result.getString( "tranid" );
                this.approved = result.getString( "approved" );
                this.forwarded = result.getString( "forwarded" );
                this.reserveNo = result.getString( "reserve_no" );
                this.amount = result.getInt( "amount" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.errorFlag = result.getInt( "error_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditHistory.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �\��N���W�b�g����}��
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

        query = "INSERT newRsvDB.hh_rsv_credit_history SET ";
        query += " order_id=?";
        query += ", job_seq=?";
        query += ", job_cd=?";
        query += ", tranid=?";
        query += ", approved=?";
        query += ", forwarded=?";
        query += ", reserve_no=?";
        query += ", amount=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", error_flag=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.orderId );
            prestate.setInt( i++, this.jobSeq );
            prestate.setString( i++, this.jobCd );
            prestate.setString( i++, this.tranid );
            prestate.setString( i++, this.approved );
            prestate.setString( i++, this.forwarded );
            prestate.setString( i++, this.reserveNo );
            prestate.setInt( i++, this.amount );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.errorFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditHistory.insertData] Exception=" + e.toString(), "DataHhRsvCreditHistory.insertData" );
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
     * �\��N���W�b�g����}��
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */

    public boolean insertData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT newRsvDB.hh_rsv_credit_history SET ";
        query += " order_id=?";
        query += ", job_seq=?";
        query += ", job_cd=?";
        query += ", tranid=?";
        query += ", approved=?";
        query += ", forwarded=?";
        query += ", reserve_no=?";
        query += ", amount=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", error_flag=?";
        try
        {
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.orderId );
            prestate.setInt( i++, this.jobSeq );
            prestate.setString( i++, this.jobCd );
            prestate.setString( i++, this.tranid );
            prestate.setString( i++, this.approved );
            prestate.setString( i++, this.forwarded );
            prestate.setString( i++, this.reserveNo );
            prestate.setInt( i++, this.amount );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.errorFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditHistory.insertData] Exception=" + e.toString(), "DataHhRsvCreditHistory.insertData" );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �\��N���W�b�g�����X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param orderId �I�[�_�[ID
     * @param jobSeq �����A��
     * @return
     */
    public boolean updateData(String orderId, int jobSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_credit_history SET ";
        query += " job_cd=?";
        query += ", tranid=?";
        query += ", approved=?";
        query += ", forwarded=?";
        query += ", reserve_no=?";
        query += ", amount=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", error_flag=?";
        query += " WHERE order_id=? AND job_seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.jobCd );
            prestate.setString( i++, this.tranid );
            prestate.setString( i++, this.approved );
            prestate.setString( i++, this.forwarded );
            prestate.setString( i++, this.reserveNo );
            prestate.setInt( i++, this.amount );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.errorFlag );
            prestate.setString( i++, this.orderId );
            prestate.setInt( i++, this.jobSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCreditHistory.updateData] Exception=" + e.toString() );
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
