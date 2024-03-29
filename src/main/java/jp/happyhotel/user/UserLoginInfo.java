/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Constants;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApUuidUser;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserFelica;

/**
 * ユーザ基本情報取得クラス。 ユーザの基本情報を取得する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/26
 */
public class UserLoginInfo implements Serializable
{
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

    /**
     * データを初期化します。
     */
    public UserLoginInfo()
    {
        userBasicCount = 0;
        nonmemberFlag = false;
        memberFlag = false;
        paymemberFlag = false;
        paymemberTempFlag = false;
        errorMessage = "";
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

    public void setUserBasic(DataUserBasic userBasic)
    {
        this.userBasic = userBasic;
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

    /**
     * ユーザ基本情報を取得する（ID,passから）
     * 
     * @param userId ユーザID
     * @param passwd パスワード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public void getUserLoginInfo(String userId, String passwd)
    {
        int kind = 0;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return;
        }

        // 非会員のチェック
        ret = this.checkNonMember( userId, passwd );
        if ( ret != false )
        {
            nonmemberFlag = true;
            return;
        }

        query = "SELECT * FROM hh_user_basic";
        // メールアドレスチェックを行う
        if ( CheckString.mailaddrCheck( userId ) != false )
        {
            kind = 1;
            query += " WHERE mail_addr = ? ";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
            // 携帯のメールアドレスからデータを取得
            query += " UNION SELECT * FROM hh_user_basic";
            query += " WHERE mail_addr_mobile = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";

        }
        else if ( userId.compareTo( "" ) != 0 )
        {
            kind = 2;
            query += " WHERE user_id = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
        }
        else
        {
            errorMessage = Constants.ERROR_MSG_API1;
            return;
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( kind == 1 )
            {
                prestate.setString( 1, userId );
                prestate.setString( 2, userId );
            }
            else if ( kind == 2 )
            {
                prestate.setString( 1, userId );
            }

            ret = getUserLoginInfoSub( prestate );
            if ( ret != false )
            {
                // メールアドレスで受けているかもしれないので、userIdを取得しなおす。
                userId = this.userBasic.getUserId();

                // パラメータを暗号化したパスワードと、DBのパスワードを比較
                if ( passwd.equals( UserLogin.decrypt( this.userBasic.getPasswd() ) ) != false )
                {
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

                        ResultSet result = prestate.executeQuery();

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
                else
                {
                    memberFlag = false;
                    paymemberTempFlag = false;
                    paymemberFlag = false;
                    errorMessage = Constants.ERROR_MSG_API2;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserLoginInfo.getUserLoginInfo()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return;
    }

    /**
     * ユーザ基本情報を取得する（userIdから）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public void getUserLoginInfo(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return;
        }

        query = "SELECT * FROM hh_user_basic";
        if ( userId.compareTo( "" ) != 0 )
        {
            query += " WHERE user_id = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
        }
        else
        {
            errorMessage = Constants.ERROR_MSG_API1;
            return;
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );

            ret = getUserLoginInfoSub( prestate );
            if ( ret != false )
            {
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

                    ResultSet result = prestate.executeQuery();

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
            else
            {
                memberFlag = false;
                paymemberTempFlag = false;
                paymemberFlag = false;
                errorMessage = Constants.ERROR_MSG_API2;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserLoginInfo.getUserLoginInfo()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return;
    }

    public static boolean checkMileUser(String userId)
    {
        return checkMileUser( userId, 0 );
    }

    public static boolean checkMileUser(String userId, int memberNo)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( userId != null )
        {
            query = "SELECT user_id FROM hh_user_basic";
            query += " WHERE user_id = ?";
            query += " AND regist_status > 0";
            try
            {
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
                Logging.info( "[UserLoginInfo.checkMileUser()] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
            if ( userId.indexOf( "ip_" ) == -1 && userId.indexOf( "ad_" ) == -1 )
            {
            }
            else if ( ret )
            {

                // 自動発行ユーザの判断をなくす。自動発行ユーザでもタッチを受け付ける。2015.06.02
                int appStatus = 1; // 0:未ログイン状態,1:ログイン状態
                DataApUuidUser dauu = new DataApUuidUser();
                if ( dauu.getAppData( userId ) != false )
                {
                    appStatus = dauu.getAppStatus();
                }
                if ( appStatus == 0 )
                {
                    ret = false;
                }
                Logging.info( "user_id=" + userId + ",appStatus=" + appStatus, "UserLoginInfo.checkMileUser()" );

                if ( ret == false ) // 未ログインユーザでもfelicaに紐づいていればOKとする
                {
                    DataUserFelica duf = new DataUserFelica();
                    if ( duf.getData( userId ) != false )
                    {
                        ret = true;
                    }
                    Logging.info( "user_id=" + userId + ",felica=" + ret, "UserLoginInfo.checkMileUser()" );
                }

                if ( ret == false ) // 未ログインユーザでも有料会員だったりメアドが登録されていればOKとする
                {
                    DataUserBasic dub = new DataUserBasic();
                    if ( dub.getData( userId ) != false )
                    {
                        if ( dub.getRegistStatusPay() == 9 )// 有料会員
                        {
                            ret = true;
                        }
                        else if ( !dub.getMailAddr().equals( "" ) )
                        {
                            ret = true;
                        }
                        else if ( !dub.getMailAddrMobile().equals( "" ) )
                        {
                            ret = true;
                        }
                    }
                    Logging.info( "user_id=" + userId + ",UserBasic=" + ret, "UserLoginInfo.checkMileUser()" );
                }

            }
        }
        return ret;
    }

    /**
     * ユーザ基本情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getUserLoginInfoSub(PreparedStatement prestate)
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
            Logging.info( "[UserLoginInfo.getUserLoginInfoSub()] Exception=" + e.toString() );
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
     * ユーザ基本情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getAppPurchaseInfo(PreparedStatement prestate)
    {
        ResultSet result = null;
        int flag = 0;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    flag = 1;
                }
                else
                {
                    flag = 0;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserLoginInfo.getUserLoginInfoSub()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( flag != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
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
     * MD5変換処理
     * 
     * @param passwd パスワード
     * @return 処理結果(null：MD5変換失敗)
     **/
    private String getPasswdMd5(String passwd)
    {
        byte[] bytePass;
        String strReturn = null;

        try
        {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            bytePass = passwd.getBytes();
            md.update( bytePass );
            bytePass = md.digest();

            StringBuffer buff = new StringBuffer();
            for( int i = 0 ; i < bytePass.length ; i++ )
            {
                int val = bytePass[i] & 0xff;
                if ( val < 16 )
                {
                    buff.append( "0" );
                }
                buff.append( Integer.toString( val, 16 ) );
            }
            strReturn = buff.toString();
        }
        catch ( Exception e )
        {
            Logging.error( "[getPasswdMd5]Exception:" + e.toString() );
        }

        return(strReturn);
    }
}
