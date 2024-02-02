package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * ���[�U�X�}�z�ۋ����擾�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2012/09/27
 */

public class DataUserSp implements Serializable
{
    // ��ۋ�
    public static final int   CHARGEFLAG_FREE     = 0;
    // �ۋ�
    public static final int   CHARGEFLAG_PAY      = 1;
    // ����ۋ�
    public static final int   CHARGEFLAG_FIRSTPAY = 2;
    // �ۋ�NG���
    public static final int   CHARGEFLAG_NGMEMBER = 3;
    // �މ�t���O�Ȃ�
    public static final int   DELETEFLAG_FALSE    = 0;
    // �މ�t���O����
    public static final int   DELETEFLAG_TRUE     = 1;

    // �h�R��
    public static final int   DOCOMO              = 1;
    // au
    public static final int   AU                  = 2;
    // �\�t�g�o���N
    public static final int   SOFTBANK            = 3;
    //
    public static final int   YAHOO_WALLET        = 4;
    /**
     *
     */
    private static final long serialVersionUID    = -3426514111887152342L;
    private String            userId;
    private String            openId;
    private int               carrierKind;
    private int               registDate;
    private int               registTime;
    private int               delDate;
    private int               delTime;
    private int               registDatePay;
    private int               registTimePay;
    private int               delDatePay;
    private int               delTimePay;
    private int               freeMymenu;
    private int               chargeFlag;
    private int               delFlag;
    private String            token;
    private String            orderNo;

    public DataUserSp()
    {
        userId = "";
        openId = "";
        carrierKind = 0;
        registDate = 0;
        registTime = 0;
        delDate = 0;
        delTime = 0;
        registDatePay = 0;
        registTimePay = 0;
        delDatePay = 0;
        delTimePay = 0;
        freeMymenu = 0;
        chargeFlag = 0;
        delFlag = 0;
        token = "";
        orderNo = "";
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public int getCarrierKind()
    {
        return carrierKind;
    }

    public void setCarrierKind(int carrierKind)
    {
        this.carrierKind = carrierKind;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public int getDelDate()
    {
        return delDate;
    }

    public void setDelDate(int delDate)
    {
        this.delDate = delDate;
    }

    public int getDelTime()
    {
        return delTime;
    }

    public void setDelTime(int delTime)
    {
        this.delTime = delTime;
    }

    public int getRegistDatePay()
    {
        return registDatePay;
    }

    public void setRegistDatePay(int registDatePay)
    {
        this.registDatePay = registDatePay;
    }

    public int getRegistTimePay()
    {
        return registTimePay;
    }

    public void setRegistTimePay(int registTimePay)
    {
        this.registTimePay = registTimePay;
    }

    public int getDelDatePay()
    {
        return delDatePay;
    }

    public void setDelDatePay(int delDatePay)
    {
        this.delDatePay = delDatePay;
    }

    public int getDelTimePay()
    {
        return delTimePay;
    }

    public void setDelTimePay(int delTimePay)
    {
        this.delTimePay = delTimePay;
    }

    public int getFreeMymenu()
    {
        return freeMymenu;
    }

    public void setFreeMymenu(int freeMymenu)
    {
        this.freeMymenu = freeMymenu;
    }

    public int getChargeFlag()
    {
        return chargeFlag;
    }

    public void setChargeFlag(int chargeFlag)
    {
        this.chargeFlag = chargeFlag;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    /**
     * ���[�U�X�}�z�ۋ����擾
     * 
     * @param userId ���[�U���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_sp WHERE user_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.openId = result.getString( "open_id" );
                    this.carrierKind = result.getInt( "carrier_kind" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.delDate = result.getInt( "del_date" );
                    this.delTime = result.getInt( "del_time" );
                    this.registDatePay = result.getInt( "regist_date_pay" );
                    this.registTimePay = result.getInt( "regist_time_pay" );
                    this.delDatePay = result.getInt( "del_date_pay" );
                    this.delTimePay = result.getInt( "del_time_pay" );
                    this.freeMymenu = result.getInt( "free_mymenu" );
                    this.chargeFlag = result.getInt( "charge_flag" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.token = result.getString( "token" );
                    this.orderNo = result.getString( "order_no" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ���[�U�X�}�z�ۋ����擾
     * 
     * @param userId ���[�U���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getDataBySuid(String suid)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_sp WHERE open_id = ? AND del_flag = 0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, suid );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.openId = result.getString( "open_id" );
                    this.carrierKind = result.getInt( "carrier_kind" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.delDate = result.getInt( "del_date" );
                    this.delTime = result.getInt( "del_time" );
                    this.registDatePay = result.getInt( "regist_date_pay" );
                    this.registTimePay = result.getInt( "regist_time_pay" );
                    this.delDatePay = result.getInt( "del_date_pay" );
                    this.delTimePay = result.getInt( "del_time_pay" );
                    this.freeMymenu = result.getInt( "free_mymenu" );
                    this.chargeFlag = result.getInt( "charge_flag" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.token = result.getString( "token" );
                    this.orderNo = result.getString( "order_no" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ���[�U�X�}�z�ۋ����擾
     * 
     * @param userId ���[�U���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getDataByToken(String token)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_sp WHERE token = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, token );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.openId = result.getString( "open_id" );
                    this.carrierKind = result.getInt( "carrier_kind" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.delDate = result.getInt( "del_date" );
                    this.delTime = result.getInt( "del_time" );
                    this.registDatePay = result.getInt( "regist_date_pay" );
                    this.registTimePay = result.getInt( "regist_time_pay" );
                    this.delDatePay = result.getInt( "del_date_pay" );
                    this.delTimePay = result.getInt( "del_time_pay" );
                    this.freeMymenu = result.getInt( "free_mymenu" );
                    this.chargeFlag = result.getInt( "charge_flag" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.token = result.getString( "token" );
                    this.orderNo = result.getString( "order_no" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ���[�U�X�}�z�ۋ��f�[�^�ݒ�
     * 
     * @param result ���[�U��{�f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.openId = result.getString( "open_id" );
                this.carrierKind = result.getInt( "carrier_kind" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.delDate = result.getInt( "del_date" );
                this.delTime = result.getInt( "del_time" );
                this.registDatePay = result.getInt( "regist_date_pay" );
                this.registTimePay = result.getInt( "regist_time_pay" );
                this.delDatePay = result.getInt( "del_date_pay" );
                this.delTimePay = result.getInt( "del_time_pay" );
                this.freeMymenu = result.getInt( "free_mymenu" );
                this.chargeFlag = result.getInt( "charge_flag" );
                this.delFlag = result.getInt( "del_flag" );
                this.token = result.getString( "token" );
                this.orderNo = result.getString( "order_no" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ���[�U�X�}�z�ۋ��f�[�^�ǉ�
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_user_sp SET ";
        query = query + " user_id = ?,";
        query = query + " open_id = ?,";
        query = query + " carrier_kind = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " del_date = ?,";
        query = query + " del_time = ?,";
        query = query + " regist_date_pay = ?,";
        query = query + " regist_time_pay = ?,";
        query = query + " del_date_pay = ?,";
        query = query + " del_time_pay = ?,";
        query = query + " free_mymenu = ?,";
        query = query + " charge_flag = ?,";
        query = query + " del_flag = ?,";
        query = query + " token = ?,";
        query = query + " order_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.openId );
            prestate.setInt( 3, this.carrierKind );
            prestate.setInt( 4, this.registDate );
            prestate.setInt( 5, this.registTime );
            prestate.setInt( 6, this.delDate );
            prestate.setInt( 7, this.delTime );
            prestate.setInt( 8, this.registDatePay );
            prestate.setInt( 9, this.registTimePay );
            prestate.setInt( 10, this.delDatePay );
            prestate.setInt( 11, this.delTimePay );
            prestate.setInt( 12, this.freeMymenu );
            prestate.setInt( 13, this.chargeFlag );
            prestate.setInt( 14, this.delDate );
            prestate.setString( 15, this.token );
            prestate.setString( 16, this.orderNo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        if ( ret != false )
        {
            ret = this.insertUserSpHistory( this.userId );
        }
        return(ret);
    }

    /**
     * ���[�U�X�}�z�ۋ��f�[�^�ύX
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_sp SET ";
        query = query + " user_id = ?,";
        query = query + " open_id = ?,";
        query = query + " carrier_kind = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " del_date = ?,";
        query = query + " del_time = ?,";
        query = query + " regist_date_pay = ?,";
        query = query + " regist_time_pay = ?,";
        query = query + " del_date_pay = ?,";
        query = query + " del_time_pay = ?,";
        query = query + " free_mymenu = ?,";
        query = query + " charge_flag = ?,";
        query = query + " del_flag = ?,";
        query = query + " token = ?,";
        query = query + " order_no = ?";
        query = query + " WHERE user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.openId );
            prestate.setInt( 3, this.carrierKind );
            prestate.setInt( 4, this.registDate );
            prestate.setInt( 5, this.registTime );
            prestate.setInt( 6, this.delDate );
            prestate.setInt( 7, this.delTime );
            prestate.setInt( 8, this.registDatePay );
            prestate.setInt( 9, this.registTimePay );
            prestate.setInt( 10, this.delDatePay );
            prestate.setInt( 11, this.delTimePay );
            prestate.setInt( 12, this.freeMymenu );
            prestate.setInt( 13, this.chargeFlag );
            prestate.setInt( 14, this.delFlag );
            prestate.setString( 15, this.token );
            prestate.setString( 16, this.orderNo );
            prestate.setString( 17, userId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        if ( ret != false )
        {
            ret = this.insertUserSpHistory( this.userId );
        }
        return(ret);
    }

    /**
     * ���[�USP�����̃C���T�[�g����
     * 
     * @param userId
     * @return
     */
    public boolean insertUserSpHistory(String userId)
    {
        boolean ret = false;
        DataUserSpHistory dush = new DataUserSpHistory();

        try
        {
            dush.setUserId( userId );
            dush.setOpenId( this.openId );
            dush.setCarrierKind( this.carrierKind );
            dush.setRegistDate( this.registDate );
            dush.setRegistTime( this.registTime );
            dush.setDelDate( this.delDate );
            dush.setDelTime( this.delTime );
            dush.setRegistDatePay( this.registDatePay );
            dush.setRegistTimePay( this.registTimePay );
            dush.setDelDatePay( this.delDatePay );
            dush.setDelTimePay( this.delTimePay );
            dush.setFreeMymenu( this.freeMymenu );
            dush.setChargeFlag( this.chargeFlag );
            dush.setDelFlag( this.delFlag );
            dush.setToken( this.token );
            dush.setOrderNo( this.orderNo );
            dush.setAddDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dush.setAddTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = dush.insertData();
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.insertUserSpHistory()] Exception:" + e.toString() );
        }
        return(ret);
    }

    /**
     * ���[�USP�̍폜����
     * 
     * @param userId
     * @return
     */
    public boolean deleteData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "DELETE FROM hh_user_sp ";
        query = query + " WHERE user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( 1, userId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.updateData] Exception=" + e.toString() );
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
