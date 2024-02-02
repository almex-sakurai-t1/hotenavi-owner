/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ���[�U��{���擾�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserBasic;

import org.apache.commons.lang.StringUtils;

/**
 * ���[�U��{���擾�N���X�B ���[�U�̊�{�����擾����@�\��񋟂���
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/26
 */
public class UserBasicInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -3924291538252750667L;

    private int               userBasicCount;
    private DataUserBasic     userBasic;
    private DataUserBasic[]   userBasicMulti;

    /**
     * �f�[�^�����������܂��B
     */
    public UserBasicInfo()
    {
        userBasicCount = 0;
    }

    /** ���[�U��{��񌏐��擾 **/
    public int getCount()
    {
        return(userBasicCount);
    }

    /** ���[�U��{���擾 **/
    public DataUserBasic getUserInfo()
    {
        return(userBasic);
    }

    /** ���[�U��{���擾 **/
    public DataUserBasic[] getUserInfoMulti()
    {
        return(userBasicMulti);
    }

    /**
     * ���[�U��{�����擾����iID����j
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasic(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
            query = query + " AND regist_status > 0";
            query = query + " AND del_flag = 0";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getUserBasicSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasic] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{�����擾����i�S�f�[�^���j
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByAll(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getUserBasicSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasic] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{�����擾����iID����j ���o�^�X�e�[�^�X����
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicNoCheck(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
            query = query + " AND del_flag = 0";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getUserBasicSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasicNoCheck] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{�����擾����iID����E�폜�ς̂��́j
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByDelete(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
            query = query + " AND del_flag = 1";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getUserBasicSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasicByDelete] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{�����擾����i�[���ԍ�����j
     * 
     * @param mobileTermno �[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByTermno(String mobileTermno)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( mobileTermno == null )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_basic";

        if ( mobileTermno.compareTo( "" ) != 0 )
        {
            query = query + " WHERE mobile_termno = ?";
            query = query + " AND regist_status > 0";
            query = query + " AND del_flag = 0";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( mobileTermno.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, mobileTermno );
            }

            ret = getUserBasicSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasicByTermno] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{�����擾����i�[���ԍ�����j ���o�^�X�e�[�^�X����
     * 
     * @param mobileTermno �[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByTermnoNoCheck(String mobileTermno)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( mobileTermno == null )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_basic";

        if ( mobileTermno.compareTo( "" ) != 0 )
        {
            query = query + " WHERE mobile_termno = ?";
            query = query + " AND del_flag = 0";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( mobileTermno.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, mobileTermno );
            }

            ret = getUserBasicSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasicByTermnoNoCheck] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{�����擾����i���[���A�h���X����j
     * 
     * @param mailAddr ���[���A�h���X
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByMailaddr(String mailAddr)
    {
        return getUserBasicByMailaddr( mailAddr, 0 );
    }

    /**
     * ���[�U��{�����擾����i���[���A�h���X����j
     * 
     * @param mailAddr ���[���A�h���X
     * @param registStatus �o�^�X�e�[�^�X
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByMailaddr(String mailAddr, int registStatus)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( CheckString.mailaddrCheck( mailAddr ) == false )
        {
            return false;
        }
        // PC�̃��[���A�h���X����f�[�^�擾
        query = "SELECT * FROM hh_user_basic";
        query += " WHERE mail_addr = ? ";
        query += " AND regist_status > ?";
        query += " AND del_flag = 0";
        // �g�т̃��[���A�h���X����f�[�^���擾
        query += " UNION SELECT * FROM hh_user_basic";
        query += " WHERE mail_addr_mobile = ? ";
        query += " AND regist_status > ?";
        query += " AND del_flag = 0";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int i = 1;
            if ( mailAddr.compareTo( "" ) != 0 )
            {
                prestate.setString( i++, mailAddr );
                prestate.setInt( i++, registStatus );
                prestate.setString( i++, mailAddr );
                prestate.setInt( i++, registStatus );
            }

            ret = getUserBasicSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasicByMailaddr] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{�����擾����i���[���A�h���X���畡�����[�U�擾�j
     * 
     * @param mailAddr ���[���A�h���X
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByMailaddrMulti(String mailAddr)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( CheckString.mailaddrCheck( mailAddr ) == false )
        {
            return false;
        }
        // PC�̃��[���A�h���X����f�[�^�擾
        query = "SELECT * FROM hh_user_basic";
        query += " WHERE mail_addr = ?";
        query += " AND regist_status > 0";
        query += " AND del_flag = 0";
        // �g�т̃��[���A�h���X����f�[�^�擾
        query += " UNION SELECT * FROM hh_user_basic";
        query += " WHERE mail_addr_mobile = ?";
        query += " AND regist_status > 0";
        query += " AND del_flag = 0";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( mailAddr.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, mailAddr );
                prestate.setString( 2, mailAddr );
            }

            ret = getUserBasicSubMulti( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasicByMailaddrMulti] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{���d���`�F�b�N�i���[�UID�A���[���A�h���X����j
     * 
     * @param userId ���[�UID
     * @param mailAddr ���[���A�h���X
     * @param mobileFlag �g�уt���O(true:�g�сAfalse:PC)
     * @return ��������(false:���o�^,true:�o�^�ς�)
     */
    public boolean getDuplicateCheckByMailaddr(String userId, String mailAddr, boolean mobileFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( mailAddr == null || userId == null )
        {
            return(false);
        }
        if ( CheckString.mailaddrCheck( mailAddr ) == false )
        {
            return false;
        }

        query = "SELECT * FROM hh_user_basic";

        if ( mailAddr.compareTo( "" ) != 0 )
        {
            if ( mobileFlag != false )
            {
                query = query + " WHERE mail_addr_mobile = ?";
            }
            else
            {
                query = query + " WHERE mail_addr = ?";
            }
            query = query + " AND user_id <> ?";
            query = query + " AND regist_status > 0";
            query = query + " AND del_flag = 0";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( mailAddr.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, mailAddr );
                prestate.setString( 2, userId );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasic] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{���d���`�F�b�N�i���[�UID�A���[���A�h���X����j
     * 
     * @param userId ���[�UID
     * @param mailAddr ���[���A�h���X
     * @param mobileFlag �g�уt���O(true:�g�сAfalse:PC)
     * @return ��������(false:���o�^,true:�o�^�ς�)
     */
    public boolean getDuplicateCheckByMailaddr(String userId, String mailAddr)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( mailAddr == null || userId == null )
        {
            return(false);
        }
        if ( CheckString.mailaddrCheck( mailAddr ) == false )
        {
            return false;
        }

        query = "SELECT * FROM hh_user_basic";

        if ( mailAddr.compareTo( "" ) != 0 )
        {
            query = query + " WHERE (mail_addr_mobile = ? OR mail_addr = ? )";
            query = query + " AND user_id <> ?";
            query = query + " AND regist_status > 0";
            query = query + " AND del_flag = 0";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( mailAddr.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, mailAddr );
                prestate.setString( 2, mailAddr );
                prestate.setString( 3, userId );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasic] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{�����擾����i���[���A�h���X����j
     * 
     * @param cookieValue �N�b�L�[�l
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByCookie(String cookieValue)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( cookieValue == null )
        {
            return(false);
        }
        if ( cookieValue.trim().compareTo( "" ) != 0 )
        {
            // ���[���A�h���X�̃n�b�V���l�Ŏ擾
            query = "SELECT * FROM hh_user_basic";
            query += " WHERE mail_addr_md5 = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
            // �g�у��[���A�h���X�n�b�V���l�Ŏ擾
            query += " UNION";
            query += " SELECT * FROM hh_user_basic";
            query += " WHERE mail_addr_mobile_md5 = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
            // ���[�UID�Ŏ擾
            query += " UNION";
            query += " SELECT * FROM hh_user_basic";
            query += " WHERE user_id = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( cookieValue.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, cookieValue );
                prestate.setString( 2, cookieValue );
                prestate.setString( 3, cookieValue );
            }

            ret = getUserBasicSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasic] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{���̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getUserBasicSub(PreparedStatement prestate)
    {
        ResultSet result = null;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                userBasic = new DataUserBasic();

                if ( result.next() != false )
                {
                    userBasicCount = 1;
                    // ���[�U��{�f�[�^���̐ݒ�
                    this.userBasic.setData( result );
                }
                else
                {
                    userBasicCount = 0;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUserBasicSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userBasicCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ���[�U��{���̃f�[�^���Z�b�g�i�������[�U�j
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getUserBasicSubMulti(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;

        count = 0;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    userBasicCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.userBasicMulti = new DataUserBasic[this.userBasicCount];
                for( i = 0 ; i < userBasicCount ; i++ )
                {
                    userBasicMulti[i] = new DataUserBasic();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����̎擾
                    this.userBasicMulti[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasicSubMulti] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userBasicCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ���[�U��{�����擾����iID����j
     * 
     * @param strMd5 ���[���A�h���X�̃n�b�V���l
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByMd5(String strMd5)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( strMd5 == null )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_basic";
        if ( strMd5.trim().compareTo( "" ) != 0 )
        {
            query += " WHERE mail_addr_md5 = ?";
            query += " AND regist_status = 9";
            query += " AND del_flag = 0";
            query += " AND mobile_termno <> '' ";
            query += " AND mobile_termno NOT LIKE '%DoCoMo%' ";
            query += " AND mobile_termno NOT LIKE '%J-PHONE%' ";
            query += " AND mobile_termno NOT LIKE '%Vodafone%' ";
            query += " AND mobile_termno NOT LIKE '%SoftBank%' ";
            query += " AND mobile_termno NOT LIKE '%KDDI%' ";
            // �g�у��[���A�h���X�̃n�b�V���l�Ŏ擾
            query += " UNION SELECT * FROM hh_user_basic";
            query += " WHERE  mail_addr_mobile_md5 = ?";
            query += " AND regist_status = 9";
            query += " AND del_flag = 0";
            query += " AND mobile_termno <> '' ";
            query += " AND mobile_termno NOT LIKE '%DoCoMo%' ";
            query += " AND mobile_termno NOT LIKE '%J-PHONE%' ";
            query += " AND mobile_termno NOT LIKE '%Vodafone%' ";
            query += " AND mobile_termno NOT LIKE '%SoftBank%' ";
            query += " AND mobile_termno NOT LIKE '%KDDI%' ";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( strMd5.trim().compareTo( "" ) != 0 )
            {
                prestate.setString( 1, strMd5 );
                prestate.setString( 2, strMd5 );
            }

            ret = getUserBasicSub( prestate );
            // �f�[�^���擾�ł����ꍇ�A�[���ԍ��������Ă��邩�ǂ���
            // ���[�U�[�G�[�W�F���g()�������Ă��Ȃ����`�F�b�N
            if ( ret != false )
            {
                if ( this.userBasic.getMobileTermNo().compareTo( "" ) != 0 )
                {
                    if ( this.userBasic.getMobileTermNo().indexOf( "DoCoMo" ) != -1 )
                    {

                    }

                }
                else
                {
                    ret = false;
                }

            }

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasicByMd5] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �o�^���������Ă��Ȃ��f�[�^�̍폜�t���O�𗧂Ă�i�[���ԍ�����j
     * 
     * @param mobileTermno �[���ԍ�
     * @param memo �����i�폜���R�j
     * @return ��������(TRUE:����܂��͊Y���f�[�^�Ȃ�,FALSE:�ُ�)
     */
    public boolean deleteOddDataByTermno(String mobileTermno, String memo)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( mobileTermno == null )
        {
            return(false);
        }
        if ( memo == null )
            memo = "";

        query = "SELECT * FROM hh_user_basic";
        if ( mobileTermno.trim().compareTo( "" ) != 0 )
        {
            query = query + " WHERE mobile_termno = ?";
            query = query + " AND regist_status >= 0";
            query = query + " AND del_flag = 0";
        }
        else
        {
            return(false);
        }
        try
        {
            ret = true;
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, mobileTermno );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.userBasicCount = result.getRow();
                }
                // �N���X�̔z���p�ӂ��A����������B
                this.userBasicMulti = new DataUserBasic[this.userBasicCount];
                for( i = 0 ; i < userBasicCount ; i++ )
                {
                    userBasicMulti[i] = new DataUserBasic();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����̎擾
                    this.userBasicMulti[count].setData( result );
                    count++;
                }
                for( i = 0 ; i < userBasicCount ; i++ )
                {
                    // userBasic.getData( userBasicMulti[i].getUserId() );
                    userBasicMulti[i].setDelFlag( 1 );
                    userBasicMulti[i].setMailAddr( "" );
                    userBasicMulti[i].setMailAddrMobile( "" );
                    userBasicMulti[i].setDelDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    userBasicMulti[i].setDelTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    userBasicMulti[i].updateData( userBasicMulti[i].getUserId(), memo );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserBasicInfo.deleteOddDataByTermno] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * �o�^���������Ă��Ȃ��f�[�^�̍폜�t���O�𗧂Ă�i���[�U�[ID�A�[���ԍ�����j
     * 
     * @param userId ���[�UID
     * @param mobileTermno �[���ԍ�
     * @param memo �����i�폜���R�j
     * @return ��������(TRUE:����܂��͊Y���f�[�^�Ȃ�,FALSE:�ُ�)
     */
    public boolean deleteExclusionDataByTermno(String userId, String mobileTermno, String memo)
    {
        int i;
        int count = 0;
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( userId == null || mobileTermno == null )
        {
            return(false);
        }
        if ( memo == null )
            memo = "";

        query = "SELECT * FROM hh_user_basic";
        if ( userId.compareTo( "" ) != 0 && mobileTermno.trim().compareTo( "" ) != 0 )
        {
            query = query + " WHERE mobile_termno = ?";
            query = query + " AND regist_status >= 0";
            query = query + " AND del_flag = 0";
            query = query + " AND user_id != ?";
        }
        else
        {
            return(false);
        }
        try
        {
            ret = true;
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, mobileTermno );
            prestate.setString( 2, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.userBasicCount = result.getRow();
                }
                // �N���X�̔z���p�ӂ��A����������B
                this.userBasicMulti = new DataUserBasic[this.userBasicCount];
                for( i = 0 ; i < userBasicCount ; i++ )
                {
                    userBasicMulti[i] = new DataUserBasic();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����̎擾
                    this.userBasicMulti[count].setData( result );
                    count++;
                }
                for( i = 0 ; i < userBasicCount ; i++ )
                {
                    // userBasic.getData( userBasicMulti[i].getUserId() );
                    userBasicMulti[i].setDelFlag( 1 );
                    userBasicMulti[i].setMailAddr( "" );
                    userBasicMulti[i].setMailAddrMobile( "" );
                    userBasicMulti[i].setDelDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    userBasicMulti[i].setDelTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    userBasicMulti[i].updateData( userBasicMulti[i].getUserId(), memo );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserBasicInfo.deleteExclusionDataByTermno] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * ���[�U��{�����擾����iID����j
     * 
     * @param strMd5 ���[���A�h���X�̃n�b�V���l
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserBasicByMd5NoCheck(String strMd5)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( strMd5 == null )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_basic";
        if ( strMd5.trim().compareTo( "" ) != 0 )
        {
            // ���[���A�h���X�̃n�b�V���l�Ŏ擾
            query += " WHERE mail_addr_md5 = ?";
            query += " AND del_flag = 0";
            query += " AND mobile_termno <> '' ";
            query += " AND mobile_termno NOT LIKE '%DoCoMo%' ";
            query += " AND mobile_termno NOT LIKE '%J-PHONE%' ";
            query += " AND mobile_termno NOT LIKE '%Vodafone%' ";
            query += " AND mobile_termno NOT LIKE '%SoftBank%' ";
            query += " AND mobile_termno NOT LIKE '%KDDI%' ";
            // �g�у��[���A�h���X�̃n�b�V���l�Ŏ擾
            query += " UNION SELECT * FROM hh_user_basic";
            query += " WHERE mail_addr_mobile_md5 = ?";
            query += " AND del_flag = 0";
            query += " AND mobile_termno <> '' ";
            query += " AND mobile_termno NOT LIKE '%DoCoMo%' ";
            query += " AND mobile_termno NOT LIKE '%J-PHONE%' ";
            query += " AND mobile_termno NOT LIKE '%Vodafone%' ";
            query += " AND mobile_termno NOT LIKE '%SoftBank%' ";
            query += " AND mobile_termno NOT LIKE '%KDDI%' ";

        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( strMd5.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, strMd5 );
                prestate.setString( 2, strMd5 );
            }

            ret = getUserBasicSub( prestate );
            // �f�[�^���擾�ł����ꍇ�A�[���ԍ��������Ă��邩�ǂ���
            // ���[�U�[�G�[�W�F���g()�������Ă��Ȃ����`�F�b�N
            if ( ret != false )
            {
                if ( this.userBasic.getMobileTermNo().compareTo( "" ) == 0 )
                {
                    ret = false;
                }

            }

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserBasicByMd5] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U��{�����擾����iID����j
     * 
     * @param strMd5 ���[���A�h���X�̃n�b�V���l
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean replaceMobieTermNo(String userId, String oldUserId)
    {
        final int DELFLAG = 1;
        int nResult = 0;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String mobileTermNo = "";

        if ( (userId == null) || (oldUserId == null) )
        {
            return(false);
        }
        if ( (userId.equals( "" ) != false) || (oldUserId.equals( "" ) != false) )
        {
            return(false);
        }

        ret = false;

        try
        {
            // �ȑO�̃��[�U����[���ԍ����擾
            query = "SELECT mobile_termno FROM hh_user_basic WHERE user_id = ? ";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, oldUserId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    mobileTermNo = result.getString( "mobile_termno" );
                    ret = true;
                }
            }
            DBConnection.releaseResources( result );

            if ( ret != false )
            {
                query = "UPDATE hh_user_basic SET mobile_termno=? WHERE user_id = ?";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, mobileTermNo );
                prestate.setString( 2, userId );
                nResult = prestate.executeUpdate();
                // �ύX�����܂���������
                if ( nResult >= 0 )
                {
                    query = "UPDATE hh_user_basic SET del_flag = ?, del_date_mobile = ?, del_time_mobile = ?";
                    query += " WHERE user_id = ?";
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, DELFLAG );
                    prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    prestate.setString( 4, oldUserId );
                    nResult = prestate.executeUpdate();
                }
            }
            if ( ret != false && nResult >= 0 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[replaceMobieTermNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[���A�h���X�̓o�^�����m�F����i���[�U�[ID����j
     * 
     * @param userId ���[�UID
     * @return ��������(-1:��O������,0:�o�^�Ȃ�,1:���o�C���p�݂̂���,2:PC�p�݂̂���,3:PC���o�C�����ɂ���)
     */
    public int checkMailAddressRegistered(String userId)
    {
        // �o�͗p�̒萔�̐ݒ�
        final int ERROR = -1; // ��O�������Ȃǃf�[�^�̎擾���ł��Ȃ������ꍇ
        final int NONE_ADDRESS = 0; // �o�^����Ă��郁�[���A�h���X�͂Ȃ�
        final int ONLY_MOBILE_ADDRESS = 1; // ���o�C���p�̂ݓo�^����
        final int ONLY_PC_ADDRESS = 2; // PC�p�̂ݓo�^����
        final int PC_AND_MOBILE_ADDRESS = 3; // PC�p�A���o�C���p���ɓo�^����

        // �����ُ킪�������ꍇ�Ȃǂ͏I��
        if ( getUserBasic( userId ) == false )
        {
            return ERROR;
        }

        // ���[���A�h���X�̎擾
        String pc_address = userBasic.getMailAddr();
        String mobile_address = userBasic.getMailAddrMobile();

        // �o�^���̊m�F
        if ( StringUtils.isNotBlank( pc_address ) && StringUtils.isNotBlank( mobile_address ) )
        {
            return PC_AND_MOBILE_ADDRESS;
        }
        else if ( StringUtils.isNotBlank( pc_address ) )
        {
            return ONLY_PC_ADDRESS;
        }
        else if ( StringUtils.isNotBlank( mobile_address ) )
        {
            return ONLY_MOBILE_ADDRESS;
        }
        else
        {
            return NONE_ADDRESS;
        }
    }

    /**
     * ���u�C���W���p�����[�U�[����
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����܂��͊Y���f�[�^�Ȃ�,FALSE:�ُ�)
     */
    public boolean isLvjUser(String userId)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }

        // ���[�U�[ID���ulj_�v����n�܂��Ă��Ȃ���΃��u�C���W���p�����[�U�[�ł͂Ȃ�
        if ( userId.indexOf( "lj_" ) != 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_user_basic";
        query = query + " WHERE temp_date_pc = 0"; // ���o�^���t�iPC�j
        query = query + " AND temp_date_mobile = 0"; // ���o�^���t�i�g�сj
        query = query + " AND regist_status = 9";// �o�^�X�e�[�^�X(9�F�o�^�ς�)
        query = query + " AND regist_date_pay = 0";// �L������o�^���t
        query = query + " AND smartphone_flag = 0";// �X�}�[�g�t�H���t���O(1�F�X�}�[�g�t�H���œo�^)
        query = query + " AND user_id = ?";

        try
        {
            ret = true;
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserBasicInfo.isLvjUser] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }
}
