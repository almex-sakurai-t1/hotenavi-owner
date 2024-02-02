/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ���[�U��{���擾�N���X
 */

package jp.happyhotel.user;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.DecodeData;
import jp.happyhotel.common.EncodeData;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.RandomString;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserLogin;
import jp.happyhotel.touch.UserAuth;
import jp.happyhotel.util.MD5;

/**
 * ���[�U���O�C���N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/26
 */
public class UserLogin implements Serializable
{
    private final static String CONF_PATH   = "/etc/happyhotel/userlogin.conf";
    private final static String KEY_KEY     = "key";
    private final static String IVBYTES_KEY = "ivBytes";
    // �L�[
    private static byte[]       key         = null;
    // �Í��x�N�^�[�iInitialization Vector�F�������x�N�g���j
    private static byte[]       ivBytes     = null;
    private static int          date        = 0;
    private static int          time        = 0;

    public int getDate()
    {
        return(date);
    }

    public int getTime()
    {
        return(time);
    }

    /** �����O�C�����[�U�ł��邱�Ƃ��������[�UID */
    public static final String NON_MEMBER_USER_ID = "tFA7EFMXMFbY4bTjbuArTUARkSPUBsFN3tsWQi2E2Tebam38WmuNDce8u4LSQXpf";
    /** �����O�C�����[�U�ł��邱�Ƃ��������[�U�̃p�X���[�h */
    public static final String NON_MEMBER_PASSWD  = "R6rZ7aNcPhUEtDTJwWsSPtmw8WjZWWeYNVMTegktYXzEjr9JKrTx5m75iTh97XQY";

    /**
     *
     */
    private static final long  serialVersionUID   = -3924291538252750667L;

    private int                userBasicCount;
    private DataUserBasic      userBasic;
    private boolean            nonmemberFlag;                                                                          // �����i�A�v������A�N�Z�X���ꂽ���[�U�j
    private boolean            memberFlag;                                                                             // ���(�r��������܂�)
    private boolean            paymemberFlag;                                                                          // �L�����
    private boolean            paymemberTempFlag;                                                                      // �L���r�����
    private String             errorMessage;
    private String             cookieValue;

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( CONF_PATH );
            prop = new Properties();
            // �v���p�e�B�t�@�C����ǂݍ���
            prop.load( propfile );

            String confKey = prop.getProperty( KEY_KEY );
            String confIbytes = prop.getProperty( IVBYTES_KEY );
            key = confKey.getBytes();
            ivBytes = confIbytes.getBytes();
            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "UserLogin Static Block Error=" + e.toString() );
        }
    }

    /**
     * �f�[�^�����������܂��B
     */
    public UserLogin()
    {
        userBasicCount = 0;
        nonmemberFlag = false;
        memberFlag = false;
        paymemberFlag = false;
        paymemberTempFlag = false;
        errorMessage = "";
        cookieValue = "";
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

    /** �����t���O **/
    public boolean isNonmemberFlag()
    {
        return(nonmemberFlag);
    }

    /** ����t���O **/
    public boolean isMemberFlag()
    {
        return(memberFlag);
    }

    /** �L������t���O **/
    public boolean isPaymemberFlag()
    {
        return(paymemberFlag);
    }

    /** �L���r������t���O **/
    public boolean isPaymemberTempFlag()
    {
        return(paymemberTempFlag);
    }

    public String getErrorMessage()
    {
        return(errorMessage);
    }

    public String getCookieValue()
    {
        return(cookieValue);
    }

    /**
     * @param:���͒l
     * 
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )�ɂ��Í���������
     */

    public static String encrypt(String inputStr) // �Z�L�����e�B�L�[���Í�������
    {
        // �Í���
        String encrypt;
        encrypt = EncodeData.encodeString( key, ivBytes, inputStr );
        return(encrypt);
    }

    /**
     * @param:���͒l
     * @inputPass :�p�X���[�h �Í����ɂ̓z�e���_����FTP�p�X���[�h���g��
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )�ɂ��Í���������
     */

    public static String encrypt(String inputStr, String inputKey) // �Z�L�����e�B�L�[���Í�������
    {

        // �Í���
        String encrypt;
        if ( inputKey.equals( "" ) )
        {
            encrypt = EncodeData.encodeString( key, ivBytes, inputStr );
        }
        else
        {
            String temp_str = inputKey + inputKey;
            if ( temp_str.length() < 16 )
            {
                temp_str = String.format( "%-16s", temp_str );
            }
            if ( temp_str.length() > 16 )
            {
                temp_str = temp_str.substring( 0, 16 );
            }
            encrypt = EncodeData.encodeString( key, (temp_str).getBytes(), inputStr );
        }
        return(encrypt);
    }

    /**
     * @param:�Í���������
     * 
     * @return DecodeData.decodeString( key, ivBytes, encrypt )�ɂ��Í���������
     */

    public static String decrypt(String encrypt) // �p�X���[�h�𕜍���
    {
        // ������
        String decrypt = "";
        try
        {
            decrypt = DecodeData.decodeString( key, ivBytes, encrypt );
        }
        catch ( Exception e )
        {
            Logging.error( "[LoginChecck.decrypt] Exception:" + e.toString() );
        }
        return(decrypt);
    }

    /**
     * @param:�Í���������
     * 
     * @inputPass :�p�X���[�h �Í����ɂ̓z�e���_����FTP�p�X���[�h���g��
     * @return DecodeData.decodeString( key, ivBytes, encrypt )�ɂ��Í���������
     */

    public static String decrypt(String encrypt, String inputKey) // �p�X���[�h�𕜍���
    {
        // ������
        String decrypt = "";
        try
        {
            if ( inputKey.equals( "" ) )
            {
                decrypt = DecodeData.decodeString( key, ivBytes, new String( encrypt ) );
            }
            else
            {
                String temp_str = inputKey + inputKey;
                if ( temp_str.length() < 16 )
                {
                    temp_str = String.format( "%-16s", temp_str );
                }
                if ( temp_str.length() > 16 )
                {
                    temp_str = temp_str.substring( 0, 16 );
                }
                decrypt = DecodeData.decodeString( key, temp_str.getBytes(), new String( encrypt ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LoginChecck.decrypt] Exception:" + e.toString() );
        }
        return(decrypt);
    }

    /**
     * ���[�U���O�C���iCookie����j
     * 
     * @param loginId ���[�UID �iCookie����̏ꍇ�ɂ́A���[�U�[ID�Ɠ������̂��͂���j
     * @param inputPasswd �p�X���[�h
     * @param isFromCookie �ǂ��炩��̔F�؂��H
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */

    public boolean userLogin(String securityKey, HttpServletRequest request)
    {
        String userAgentKind = UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_PC ? "user_agent_pc" : "user_agent_mobile";
        String userAgent = UserAgent.getUserAgent( request );
        String dataUserAgent = "";

        String userId = "";
        String decryptSecurityKey = "";

        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        decryptSecurityKey = decrypt( securityKey );
        if ( !decryptSecurityKey.equals( "" ) )
        {
            query = "SELECT login.user_id,login." + userAgentKind + " FROM hh_user_login login";
            query += " WHERE login.security_key = ?";
            if ( userAgent.indexOf( "HappyHotel" ) == -1 ) // �A�v������̏ꍇ�̓��O�C���F�؍σt���O���݂Ȃ�
            {
                query += " AND login.regist_flag = 1";
            }
            query += " AND login.del_flag = 0";
            ret = false;

            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, decryptSecurityKey );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        userId = result.getString( "login.user_id" );
                        dataUserAgent = result.getString( "login." + userAgentKind );
                        if ( userAgent.indexOf( "HappyHotel" ) == -1 )
                        {
                            if ( !dataUserAgent.equals( "" ) && !dataUserAgent.equals( userAgent ) )
                            {
                                ret = false; // Cookie�����Ƃɂ������O�C�������A�����ƈ�����[������Ȃ̂ł������񃍃O�A�E�g������B�A�v�����̏ꍇ�̓��[�U�[�G�W�F���g�̔��f�͂��Ȃ�
                                ret = true; // �ŏI���p���[�U�[�G�[�W�F���g���ς���Ă����O�A�E�g�͂��Ȃ����ƂɂȂ����B#30516
                            }
                            else
                            {
                                ret = true;
                            }
                        }
                        else
                        {
                            ret = true;
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[UserLogin.userLogin(String securityKey, HttpServletRequest request)] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
            }
        }
        else
        {
            ret = false;
        }

        if ( ret != false )
        {
            // ��������擾���܂�
            ret = getUserBasic( connection, userId );
            if ( userBasic.getRegistStatus() == 0 || userBasic.getDelFlag() == 1 )
            {
                ret = false;
            }
        }

        if ( ret == false )
        {
            memberFlag = false;
            paymemberTempFlag = false;
            paymemberFlag = false;
            errorMessage = Constants.ERROR_MSG_API22;
        }

        try
        {
            if ( ret != false )
            {
                query = "UPDATE hh_user_login SET ";
                query = query + userAgentKind + " = ?, ";
                query = query + "latest_date = ?,";
                query = query + "latest_time = ? ";
                query = query + "WHERE security_key = ? ";

                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userAgent );
                prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                prestate.setString( 4, decryptSecurityKey );
                prestate.executeUpdate();

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.userLogin(String securityKey, HttpServletRequest request)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return ret;
    }

    /**
     * ���[�U���O�C���iID,pass����j
     * 
     * @param loginId ���O�C��ID
     * @param inputPasswd �p�X���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */

    public boolean userLogin(String loginId, String inputPasswd, HttpServletRequest request)
    {
        return userLogin( loginId, inputPasswd, request, false );
    }

    /**
     * ���[�U���O�C���iID,pass����j
     * 
     * @param loginId ���O�C��ID
     * @param inputPasswd �p�X���[�h
     * @param isApli �A�v����InAppBrowser����̃A�N�Z�X
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */

    public boolean userLogin(String loginId, String inputPasswd, HttpServletRequest request, boolean isInApp)
    {
        String userAgentKind = UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_PC ? "user_agent_pc" : "user_agent_mobile";
        String userAgent = UserAgent.getUserAgent( request );

        String userId = "";
        String dataPasswd = "";
        int regist_flag = 0;
        String securityKey = "";
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( loginId == null || inputPasswd == null )
        {
            return false;
        }
        if ( loginId.equals( "" ) || inputPasswd.equals( "" ) )
        {
            return false;
        }

        // �����̃`�F�b�N
        ret = this.checkNonMember( loginId, inputPasswd );
        if ( ret != false )
        {
            nonmemberFlag = true;
            return false;
        }

        // InApp�ȊO�̏ꍇ�A�p�X���[�h�n�b�V����
        if ( !isInApp )
        {
            inputPasswd = MD5.convert( inputPasswd );
        }

        // ���[�U�F��
        UserLoginInfo uli;
        uli = new UserLoginInfo();
        uli.getUserLoginInfo( loginId, inputPasswd );
        if ( uli.isMemberFlag() && uli.getUserInfo().getRegistStatus() == 9 )
        {
            // hh_user_login�f�[�^�����݂��Ȃ���΍쐬
            DataUserLogin userlogin = new DataUserLogin();
            if ( !userlogin.existsData( loginId ) )
            {
                userlogin.setLoginId( loginId );
                userlogin.setUserId( uli.getUserInfo().getUserId() );
                userlogin.setSecurityKey( RandomString.getSecurityKey() );
                userlogin.insertData();
            }
            if ( !loginId.equals( uli.getUserInfo().getUserId() ) )
            {
                if ( !userlogin.existsData( uli.getUserInfo().getUserId() ) )
                {
                    userlogin.setLoginId( uli.getUserInfo().getUserId() );
                    userlogin.setUserId( uli.getUserInfo().getUserId() );
                    userlogin.setSecurityKey( RandomString.getSecurityKey() );
                    userlogin.insertData();
                }
            }
            if ( !loginId.equals( uli.getUserInfo().getMailAddr() ) && !uli.getUserInfo().getMailAddr().equals( "" ) )
            {
                if ( !userlogin.existsData( uli.getUserInfo().getMailAddr() ) )
                {
                    userlogin.setLoginId( uli.getUserInfo().getMailAddr() );
                    userlogin.setUserId( uli.getUserInfo().getUserId() );
                    userlogin.setSecurityKey( RandomString.getSecurityKey() );
                    userlogin.insertData();
                }
            }
            if ( !loginId.equals( uli.getUserInfo().getMailAddrMobile() ) && !uli.getUserInfo().getMailAddrMobile().equals( "" ) )
            {
                if ( !userlogin.existsData( uli.getUserInfo().getMailAddrMobile() ) )
                {
                    userlogin.setLoginId( uli.getUserInfo().getMailAddrMobile() );
                    userlogin.setUserId( uli.getUserInfo().getUserId() );
                    userlogin.setSecurityKey( RandomString.getSecurityKey() );
                    userlogin.insertData();
                }
            }
        }

        query = "SELECT login.user_id,login.regist_flag,login.security_key,login." + userAgentKind + " FROM hh_user_login login";
        query += " WHERE login.login_id = ?";
        query += " AND login.del_flag = 0";
        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, loginId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    userId = result.getString( "login.user_id" );
                    securityKey = result.getString( "login.security_key" );
                    regist_flag = result.getInt( "login.regist_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.userLogin(String loginId, String inputPasswd, HttpServletRequest request)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        if ( ret != false )
        {
            // ��������擾���܂�
            ret = getUserBasic( connection, userId );

            if ( userBasic.getRegistStatus() != 9 || userBasic.getDelFlag() == 1 )
            {
                ret = false;
            }
            else
            {
                dataPasswd = userBasic.getPasswd();// �Í������ꂽ�p�X���[�h���擾
                ret = inputPasswd.equals( decrypt( dataPasswd ) );// ���̓p�X���[�h���Í������̂Əƍ�
            }
        }

        if ( ret == false )
        {
            memberFlag = false;
            paymemberTempFlag = false;
            paymemberFlag = false;
            errorMessage = Constants.ERROR_MSG_API22;
        }

        // �V���Ƀ��O�C�������̂ŃZ�L�����e�B�L�[���X�V����
        securityKey = RandomString.getSecurityKey();
        try
        {
            if ( ret != false )
            {
                query = "UPDATE hh_user_login SET ";
                if ( isInApp )
                {
                    query += userAgentKind + " = ? ";
                }
                else
                {
                    query += " regist_flag = 1 ";
                    query += "," + userAgentKind + " = ? ";
                }
                if ( regist_flag == 0 )
                {
                    query += ",security_key = ? ";
                    query += ",regist_date = ?";
                    query += ",regist_time = ?";
                }
                query += ",latest_date = ?";
                query += ",latest_time = ? ";
                query += " WHERE login_id = ? ";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userAgent );
                if ( regist_flag == 0 && !isInApp )
                {
                    prestate.setString( 2, securityKey );
                    prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    prestate.setInt( 6, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    prestate.setString( 7, loginId );
                }
                else
                {
                    prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    prestate.setString( 4, loginId );
                }
                prestate.executeUpdate();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.userLogin(String loginId, String inputPasswd, HttpServletRequest request)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return ret;
    }

    /**
     * �^�b�`�ɂ�郆�[�U���O�C���irequest.getParameter( "t" ) �j
     * 
     * @param HttpServletRequest request
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */

    public boolean userLoginbyTouch(HttpServletRequest request)
    {
        String userAgentKind = UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_PC ? "user_agent_pc" : "user_agent_mobile";
        String userAgent = UserAgent.getUserAgent( request );
        String userId = UserAuth.getUserId( request.getParameter( "t" ) );
        int regist_flag = 0;
        String securityKey = "";
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        // ���[�U�F��
        UserLoginInfo uli;
        uli = new UserLoginInfo();
        uli.getUserLoginInfo( userId );
        if ( uli.isMemberFlag() && uli.getUserInfo().getRegistStatus() == 9 && uli.getUserInfo().getDelFlag() == 0 )
        {
            // hh_user_login�f�[�^�����݂��Ȃ���΍쐬
            DataUserLogin userlogin = new DataUserLogin();
            if ( !userlogin.existsData( userId ) )
            {
                userlogin.setLoginId( userId );
                userlogin.setUserId( uli.getUserInfo().getUserId() );
                userlogin.setSecurityKey( RandomString.getSecurityKey() );
                userlogin.insertData();
            }
        }

        query = "SELECT login.user_id,login.regist_flag,login.security_key,login." + userAgentKind + " FROM hh_user_login login";
        query += " WHERE login.login_id = ?";
        query += " AND login.del_flag = 0";
        ret = false;

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
                    userId = result.getString( "login.user_id" );
                    securityKey = result.getString( "login.security_key" );
                    regist_flag = result.getInt( "login.regist_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.userLogin( HttpServletRequest request)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        if ( ret == false )
        {
            memberFlag = false;
            paymemberTempFlag = false;
            paymemberFlag = false;
            errorMessage = Constants.ERROR_MSG_API22;
        }

        // �V���Ƀ��O�C�������̂ŃZ�L�����e�B�L�[���X�V����
        securityKey = RandomString.getSecurityKey();
        try
        {
            if ( ret != false )
            {
                query = "UPDATE hh_user_login SET ";
                query += userAgentKind + " = ? ";
                if ( regist_flag == 0 )
                {
                    query += ",security_key = ? ";
                    query += ",regist_date = ?";
                    query += ",regist_time = ?";
                }
                query += ",latest_date = ?";
                query += ",latest_time = ? ";
                query += " WHERE login_id = ? ";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userAgent );
                if ( regist_flag == 0 )
                {
                    prestate.setString( 2, securityKey );
                    prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    prestate.setInt( 6, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    prestate.setString( 7, userId );
                }
                else
                {
                    prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    prestate.setString( 4, userId );
                }
                prestate.executeUpdate();
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.userLogin( HttpServletRequest request)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return ret;
    }

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

            ret = getUserInfo( prestate );
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
            Logging.error( "[getUserBasicByMd5] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    public boolean getUserBasicByTermNo(String mobileTermno)
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

            ret = getUserInfo( prestate );

        }
        catch ( Exception e )
        {
            Logging.error( "[getUserBasicByTermno] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    private boolean getUserBasic(Connection connection, String userId)
    {
        boolean ret = false;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query = "";
        try
        {
            query = "SELECT * FROM hh_user_basic WHERE user_id=?";
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            ret = getUserInfo( prestate );

            // ��v�����̂Ń����o�[�͊m��
            memberFlag = true;
            // �L���r������`�F�b�N
            if ( this.userBasic.getRegistStatusPay() == 1 )
            {
                paymemberTempFlag = true;
                paymemberFlag = false;
            }
            // �L������`�F�b�N
            if ( this.userBasic.getRegistStatusPay() == 9 )
            {
                paymemberTempFlag = false;
                paymemberFlag = true;
            }
            else
            {
                // �A�v�����ۋ��`�F�b�N

                query = "SELECT A.user_id, C.regist_status_pay FROM hh_user_basic A";
                query += " INNER JOIN ap_uuid_user B ON A.user_id = B.user_id";
                query += " INNER JOIN ap_uuid C ON B.uuid = C.uuid AND C.regist_status_pay = 2";
                query += " WHERE A.user_id = ?";

                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userId );

                result = prestate.executeQuery();

                if ( result.next() )
                {
                    // �L�����(�A�v�����ۋ�)
                    paymemberTempFlag = false;
                    paymemberFlag = true;
                }
                else
                {
                    // �������
                    paymemberTempFlag = false;
                    paymemberFlag = false;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.getUserBasic(String userId)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return ret;
    }

    public boolean makeCookieValue(String loginId)
    {
        boolean ret = false;
        String query;
        String securityKey = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT security_key FROM hh_user_login WHERE login_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, loginId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    securityKey = result.getString( "security_key" );
                    this.cookieValue = encrypt( securityKey ).replace( "\r", "" ).replace( "\n", "" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.makeCookieValue(String loginId)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * ���[�U�����`�F�b�N
     * 
     * @param userId ���[�UID
     * @param passwd �p�X���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     **/
    private boolean checkNonMember(String userId, String passwd)
    {
        if ( (NON_MEMBER_USER_ID.equals( userId ) != false) && (NON_MEMBER_PASSWD.equals( passwd ) != false) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * ���[�U��{���̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getUserInfo(PreparedStatement prestate)
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
            Logging.error( "[UserLogin.getUserInfo(PreparedStatement prestate)] Exception=" + e.toString() );
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
     * ���[�U�Z�L�����e�B�L�[����������
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:���R�[�h���Ȃ��̂�OK,FALSE:���R�[�h�o�^��)
     */
    public boolean isSecurityKey(String securityKey)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( securityKey == null )
        {
            return(false);
        }
        query = "SELECT 1 FROM hh_user_login";

        if ( securityKey.compareTo( "" ) != 0 )
        {
            query = query + " WHERE security_key = ?";
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
            if ( securityKey.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, securityKey );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = false;
                }
                else
                {
                    ret = true;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.isSecurityKey(String securityKey)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return ret;
    }

    /**
     * ���[�U���O�C���f�[�^�����邩�ǂ�������������
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:���R�[�h�o�^��,FALSE:���R�[�h�Ȃ�)
     */
    public boolean isUserLogin(String loginId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( loginId == null )
        {
            return(false);
        }
        query = "SELECT 1 FROM hh_user_login";

        if ( loginId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE login_id = ?";
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
            if ( loginId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, loginId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true;
                }
                else
                {
                    ret = false;
                }
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.isUserLogin(String loginId)] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return ret;
    }

    /**
     * �����[�U�[ID��termNo��V�������[�UID�ɏ������݁A�����[�UID�͑މ�Ƃ���B
     * 
     * @param userId �V���[�UID
     * @param oldUserId �����[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean replaceMobieTermNo(String userId, String oldUserId)
    {
        UserBasicInfo userLogin = new UserBasicInfo();
        return userLogin.replaceMobieTermNo( userId, oldUserId );
    }

    /**
     * config�t�@�C���ǂݍ���
     * 
     * @param
     * @return
     */
    private static void readConfFile() throws Exception
    {
        FileInputStream conffile = null;
        Properties prop = null;

        try
        {
            // conf�t�@�C������L�[�ƒl�̃��X�g��ǂݍ���
            conffile = new FileInputStream( CONF_PATH );
            prop = new Properties();
            prop.load( conffile );

            // �ݒ�l�̎擾
            String confKey = prop.getProperty( KEY_KEY );
            String confIbytes = prop.getProperty( IVBYTES_KEY );
            key = confKey.getBytes();
            ivBytes = confIbytes.getBytes();

        }
        catch ( Exception e )
        {
            Logging.error( "[UserLogin.readConfFile() ]Exception:" + e.toString() );
            throw e;
        }
        finally
        {
            // conf�t�@�C���̃N���[�Y
            prop = null;
            conffile.close();
        }
    }

}
