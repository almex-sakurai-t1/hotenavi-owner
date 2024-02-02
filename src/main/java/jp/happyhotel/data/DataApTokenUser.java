package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �g�[�N�����[�U�[(ap_token_user)�擾�N���X
 *
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApTokenUser implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -6914763244365809860L;
    public static final String TABLE            = "ap_token_user";
    private String             token;                                   // iOS:device token,Android:registration id�i�g�[�N���͏��������Ƃ�����j
    private String             userId;                                  // ���[�U�[ID
    private int                osType;                                  // 0:iPhone�A1;Android
    private int                registDate;                              // �o�^���t�iYYYYMMDD)
    private int                registTime;                              // �o�^�����iHHMMSS)
    private int                updateDate;                              // �ύX���t�iYYYYMMDD)
    private int                updateTime;                              // �ύX�����iHHMMSS)
    private int                errorFlag;                              // �G���[�t���O(0:���� 2:���M�G���[ 3:�g�[�N���s��)

    /**
     * �f�[�^�����������܂��B
     */
    public DataApTokenUser()
    {
        this.token = "";
        this.userId = "";
        this.osType = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.updateDate = 0;
        this.updateTime = 0;
        this.errorFlag = 0;
    }

    public String getToken()
    {
        return token;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getOsType()
    {
        return osType;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getUpdateDate()
    {
        return updateDate;
    }

    public int getUpdateTime()
    {
        return updateTime;
    }

    public int getErrorFlag()
    {
        return errorFlag;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setOsType(int osType)
    {
        this.osType = osType;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setUpdateDate(int updateDate)
    {
        this.updateDate = updateDate;
    }

    public void setUpdateTime(int updateTime)
    {
        this.updateTime = updateTime;
    }

    public void setErrorFlag(int errorFlag)
    {
        this.errorFlag = errorFlag;
    }

    /****
     * �g�[�N�����[�U�[(ap_token_user)�擾
     *
     * @param token iOS:device token,Android:registration id�i�g�[�N���͏��������Ƃ�����j
     * @param userId ���[�U�[ID
     * @return
     */
    public boolean getData(String token, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_token_user WHERE token = ? AND user_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, token );
            prestate.setString( 2, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.token = result.getString( "token" );
                    this.userId = result.getString( "user_id" );
                    this.osType = result.getInt( "os_type" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    this.errorFlag = result.getInt( "error_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �g�[�N�����[�U�[(ap_token_user)�擾
     *
     * @param connection �R�l�N�V����
     * @param token iOS:device token,Android:registration id�i�g�[�N���͏��������Ƃ�����j
     * @param userId ���[�U�[ID
     * @return
     */
    public boolean getData(Connection connection, String token, String userId)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_token_user WHERE token = ? AND user_id = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, token );
            prestate.setString( 2, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.token = result.getString( "token" );
                    this.userId = result.getString( "user_id" );
                    this.osType = result.getInt( "os_type" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    this.errorFlag = result.getInt( "error_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }


    /****
     * �g�[�N�����[�U�[(ap_token_user)�擾
     *
     * @param userId ���[�U�[ID
     * @return
     */
    public boolean getDataByUserId(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_token_user WHERE user_id = ? ";
        query += " ORDER BY regist_date DESC, regist_time DESC";
        query += " LIMIT 1";
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
                    this.token = result.getString( "token" );
                    this.userId = result.getString( "user_id" );
                    this.osType = result.getInt( "os_type" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    this.errorFlag = result.getInt( "error_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.getDataByUserId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �Ō�Ƀ��O�C�������g�[�N�����[�U�[(ap_token_user)�擾
     *
     * @param userId ���[�U�[ID
     * @return
     */
    public boolean getDataLastLoginByUserId(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_token_user WHERE user_id = ? ";
        query += " ORDER BY update_date DESC, update_time DESC";
        query += " LIMIT 1";
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
                    this.token = result.getString( "token" );
                    this.userId = result.getString( "user_id" );
                    this.osType = result.getInt( "os_type" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    this.errorFlag = result.getInt( "error_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.getDataByUserId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
    /****
     * �g�[�N�����[�U�[(ap_token_user)�擾
     *
     * @param token iOS:device token,Android:registration id�i�g�[�N���͏��������Ƃ�����j
     * @return
     */
    public boolean getDataByToken(String token)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_token_user WHERE token = ? ";
        query += " ORDER BY regist_date DESC, regist_time DESC";
        query += " LIMIT 1";
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
                    this.token = result.getString( "token" );
                    this.userId = result.getString( "user_id" );
                    this.osType = result.getInt( "os_type" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    this.errorFlag = result.getInt( "error_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.getDataByToken] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �g�[�N�����[�U�[(ap_token_user)�ݒ�
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
                this.token = result.getString( "token" );
                this.userId = result.getString( "user_id" );
                this.osType = result.getInt( "os_type" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.updateDate = result.getInt( "update_date" );
                this.updateTime = result.getInt( "update_time" );
                this.errorFlag = result.getInt( "error_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �g�[�N�����[�U�[(ap_token_user)�}��
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

        query = "INSERT ap_token_user SET ";
        query += " token=?";
        query += ", user_id=?";
        query += ", os_type=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        query += ", error_flag=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.token );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.osType );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.errorFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.insertData] Exception=" + e.toString() );
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
     * �g�[�N�����[�U�[(ap_token_user)�X�V
     *
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param token iOS:device token,Android:registration id�i�g�[�N���͏��������Ƃ�����j
     * @param userId ���[�U�[ID
     * @return
     */
    public boolean updateData(String token, String userId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_token_user SET ";
        query += "  token=?";
        query += ", user_id=?";
        query += ", os_type=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        query += ", error_flag=?";
        query += " WHERE token=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.token );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.osType );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.errorFlag );
            prestate.setString( i++, token );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.updateData] Exception=" + e.toString() + ",newtoken=" + this.token + ",oldToken=" + token + ",newUserid=" + this.userId + ",oldUserId=" + userId );
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
     * �g�[�N�����[�U�[(ap_token_user)�X�V
     *
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param connection �R�l�N�V����
     * @param token iOS:device token,Android:registration id�i�g�[�N���͏��������Ƃ�����j
     * @param userId ���[�U�[ID
     * @return
     */
    public boolean updateData(Connection connection, String token, String userId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_token_user SET ";
        query += "  token=?";
        query += ", user_id=?";
        query += ", os_type=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        query += ", error_flag=?";
        query += " WHERE token=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.token );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.osType );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.errorFlag );
            prestate.setString( i++, token );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.updateData] Exception=" + e.toString() + ",newtoken=" + this.token + ",oldToken=" + token + ",newUserid=" + this.userId + ",oldUserId=" + userId );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }


    /**
     * �g�[�N�����[�U�[(ap_token_user)�X�V
     *
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param token iOS:device token,Android:registration id�i�g�[�N���͏��������Ƃ�����j
     * @param userId ���[�U�[ID
     * @return
     */
    public boolean updateData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_token_user SET ";
        query += " user_id=?";
        query += ", os_type=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        query += ", error_flag=?";
        query += " WHERE token=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.osType );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setInt( i++, this.errorFlag );
            prestate.setString( i++, this.token );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUser.updateData] Exception=" + e.toString() + ",newtoken=" + this.token + ",newUserid=" + this.userId );
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
