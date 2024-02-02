/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
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
 * ユーザログインクラス
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
    // キー
    private static byte[]       key         = null;
    // 暗号ベクター（Initialization Vector：初期化ベクトル）
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

    /** 未ログインユーザであることを示すユーザID */
    public static final String NON_MEMBER_USER_ID = "tFA7EFMXMFbY4bTjbuArTUARkSPUBsFN3tsWQi2E2Tebam38WmuNDce8u4LSQXpf";
    /** 未ログインユーザであることを示すユーザのパスワード */
    public static final String NON_MEMBER_PASSWD  = "R6rZ7aNcPhUEtDTJwWsSPtmw8WjZWWeYNVMTegktYXzEjr9JKrTx5m75iTh97XQY";

    /**
     *
     */
    private static final long  serialVersionUID   = -3924291538252750667L;

    private int                userBasicCount;
    private DataUserBasic      userBasic;
    private boolean            nonmemberFlag;                                                                          // 非会員（アプリからアクセスされたユーザ）
    private boolean            memberFlag;                                                                             // 会員(途中会員も含む)
    private boolean            paymemberFlag;                                                                          // 有料会員
    private boolean            paymemberTempFlag;                                                                      // 有料途中会員
    private String             errorMessage;
    private String             cookieValue;

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( CONF_PATH );
            prop = new Properties();
            // プロパティファイルを読み込む
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
     * データを初期化します。
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

    /** ユーザ基本情報件数取得 **/
    public int getCount()
    {
        return(userBasicCount);
    }

    /** ユーザ基本情報取得 **/
    public DataUserBasic getUserInfo()
    {
        return(userBasic);
    }

    /** 非会員フラグ **/
    public boolean isNonmemberFlag()
    {
        return(nonmemberFlag);
    }

    /** 会員フラグ **/
    public boolean isMemberFlag()
    {
        return(memberFlag);
    }

    /** 有料会員フラグ **/
    public boolean isPaymemberFlag()
    {
        return(paymemberFlag);
    }

    /** 有料途中会員フラグ **/
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
     * @param:入力値
     * 
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )による暗号化文字列
     */

    public static String encrypt(String inputStr) // セキュリティキーを暗号化する
    {
        // 暗号化
        String encrypt;
        encrypt = EncodeData.encodeString( key, ivBytes, inputStr );
        return(encrypt);
    }

    /**
     * @param:入力値
     * @inputPass :パスワード 暗号化にはホテル契約先のFTPパスワードを使う
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )による暗号化文字列
     */

    public static String encrypt(String inputStr, String inputKey) // セキュリティキーを暗号化する
    {

        // 暗号化
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
     * @param:暗号化文字列
     * 
     * @return DecodeData.decodeString( key, ivBytes, encrypt )による暗号化文字列
     */

    public static String decrypt(String encrypt) // パスワードを復号化
    {
        // 複合化
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
     * @param:暗号化文字列
     * 
     * @inputPass :パスワード 暗号化にはホテル契約先のFTPパスワードを使う
     * @return DecodeData.decodeString( key, ivBytes, encrypt )による暗号化文字列
     */

    public static String decrypt(String encrypt, String inputKey) // パスワードを復号化
    {
        // 複合化
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
     * ユーザログイン（Cookieから）
     * 
     * @param loginId ユーザID （Cookieからの場合には、ユーザーIDと同じものがはいる）
     * @param inputPasswd パスワード
     * @param isFromCookie どちらからの認証か？
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
            if ( userAgent.indexOf( "HappyHotel" ) == -1 ) // アプリからの場合はログイン認証済フラグをみない
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
                                ret = false; // Cookieをもとにしたログインだが、いつもと違った端末からなのでいったんログアウトさせる。アプリ内の場合はユーザーエジェントの判断はしない
                                ret = true; // 最終利用ユーザーエージェントが変わってもログアウトはしないことになった。#30516
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
            // 会員情報を取得します
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
     * ユーザログイン（ID,passから）
     * 
     * @param loginId ログインID
     * @param inputPasswd パスワード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean userLogin(String loginId, String inputPasswd, HttpServletRequest request)
    {
        return userLogin( loginId, inputPasswd, request, false );
    }

    /**
     * ユーザログイン（ID,passから）
     * 
     * @param loginId ログインID
     * @param inputPasswd パスワード
     * @param isApli アプリ内InAppBrowserからのアクセス
     * @return 処理結果(TRUE:正常,FALSE:異常)
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

        // 非会員のチェック
        ret = this.checkNonMember( loginId, inputPasswd );
        if ( ret != false )
        {
            nonmemberFlag = true;
            return false;
        }

        // InApp以外の場合、パスワードハッシュ化
        if ( !isInApp )
        {
            inputPasswd = MD5.convert( inputPasswd );
        }

        // ユーザ認証
        UserLoginInfo uli;
        uli = new UserLoginInfo();
        uli.getUserLoginInfo( loginId, inputPasswd );
        if ( uli.isMemberFlag() && uli.getUserInfo().getRegistStatus() == 9 )
        {
            // hh_user_loginデータが存在しなければ作成
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
            // 会員情報を取得します
            ret = getUserBasic( connection, userId );

            if ( userBasic.getRegistStatus() != 9 || userBasic.getDelFlag() == 1 )
            {
                ret = false;
            }
            else
            {
                dataPasswd = userBasic.getPasswd();// 暗号化されたパスワードを取得
                ret = inputPasswd.equals( decrypt( dataPasswd ) );// 入力パスワードを暗号化ものと照合
            }
        }

        if ( ret == false )
        {
            memberFlag = false;
            paymemberTempFlag = false;
            paymemberFlag = false;
            errorMessage = Constants.ERROR_MSG_API22;
        }

        // 新たにログインしたのでセキュリティキーを更新する
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
     * タッチによるユーザログイン（request.getParameter( "t" ) ）
     * 
     * @param HttpServletRequest request
     * @return 処理結果(TRUE:正常,FALSE:異常)
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

        // ユーザ認証
        UserLoginInfo uli;
        uli = new UserLoginInfo();
        uli.getUserLoginInfo( userId );
        if ( uli.isMemberFlag() && uli.getUserInfo().getRegistStatus() == 9 && uli.getUserInfo().getDelFlag() == 0 )
        {
            // hh_user_loginデータが存在しなければ作成
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

        // 新たにログインしたのでセキュリティキーを更新する
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
            // 携帯メールアドレスのハッシュ値で取得
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
            // データが取得できた場合、端末番号が入っているかどうか
            // ユーザーエージェント()が入っていないかチェック
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

            // 一致したのでメンバーは確定
            memberFlag = true;
            // 有料途中会員チェック
            if ( this.userBasic.getRegistStatusPay() == 1 )
            {
                paymemberTempFlag = true;
                paymemberFlag = false;
            }
            // 有料会員チェック
            if ( this.userBasic.getRegistStatusPay() == 9 )
            {
                paymemberTempFlag = false;
                paymemberFlag = true;
            }
            else
            {
                // アプリ内課金チェック

                query = "SELECT A.user_id, C.regist_status_pay FROM hh_user_basic A";
                query += " INNER JOIN ap_uuid_user B ON A.user_id = B.user_id";
                query += " INNER JOIN ap_uuid C ON B.uuid = C.uuid AND C.regist_status_pay = 2";
                query += " WHERE A.user_id = ?";

                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userId );

                result = prestate.executeQuery();

                if ( result.next() )
                {
                    // 有料会員(アプリ内課金)
                    paymemberTempFlag = false;
                    paymemberFlag = true;
                }
                else
                {
                    // 無料会員
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
     * ユーザ非会員チェック
     * 
     * @param userId ユーザID
     * @param passwd パスワード
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ユーザ基本情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
                    // ユーザ基本データ情報の設定
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
     * ユーザセキュリティキーを検査する
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:レコードがないのでOK,FALSE:レコード登録済)
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
     * ユーザログインデータがあるかどうかを検査する
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:レコード登録済,FALSE:レコードなし)
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
     * 旧ユーザーIDのtermNoを新しいユーザIDに書き込み、旧ユーザIDは退会とする。
     * 
     * @param userId 新ユーザID
     * @param oldUserId 旧ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean replaceMobieTermNo(String userId, String oldUserId)
    {
        UserBasicInfo userLogin = new UserBasicInfo();
        return userLogin.replaceMobieTermNo( userId, oldUserId );
    }

    /**
     * configファイル読み込み
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
            // confファイルからキーと値のリストを読み込む
            conffile = new FileInputStream( CONF_PATH );
            prop = new Properties();
            prop.load( conffile );

            // 設定値の取得
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
            // confファイルのクローズ
            prop = null;
            conffile.close();
        }
    }

}
