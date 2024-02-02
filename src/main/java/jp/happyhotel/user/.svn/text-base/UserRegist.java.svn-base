/*
 * @(#)UserRegist.java 1.00 2007/08/12 Copyright (C) ALMEX Inc. 2007 ユーザマイメニュー登録・更新クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.CheckMailAddr;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.RandomString;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserLogin;

/**
 * ユーザマイメニュー登録・更新クラス ユーザのマイメニュー登録する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/12
 * @version 1.1 2007/11/27
 */
public class UserRegist implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 1534187427216353797L;

    private String            userId;
    private String            passwd;
    private int               registStatus;
    private String            handleName;
    private String            mailAddr;
    private String            mailAddrMd5;
    private String            mailAddrMobile;
    private String            mailAddrMobileMd5;
    private int               tempDatePc;
    private int               tempTimePc;
    private int               tempDateMobile;
    private int               tempTimeMobile;
    private int               registDatePc;
    private int               registTimePc;
    private int               registDateMobile;
    private int               registTimeMobile;
    private int               smartPhoneFlag;
    private String            oldMailAddr;
    private int               tempDate;
    private int               tempTime;

    /**
     * データを初期化します。
     */
    public UserRegist()
    {
        userId = "";
        passwd = "";
        registStatus = 0;
        handleName = "";

        mailAddr = "";
        mailAddrMd5 = "";
        mailAddrMobile = "";
        mailAddrMobileMd5 = "";

        tempDatePc = 0;
        tempTimePc = 0;
        tempDateMobile = 0;
        tempTimeMobile = 0;
        registDatePc = 0;
        registTimePc = 0;
        registDateMobile = 0;
        registTimeMobile = 0;
        smartPhoneFlag = 0;

        oldMailAddr = "";
        tempDate = 0;
        tempTime = 0;
    }

    public int getSmartPhoneFlag()
    {
        return smartPhoneFlag;
    }

    public void setSmartPhoneFlag(int smartPhoneFlag)
    {
        this.smartPhoneFlag = smartPhoneFlag;
    }

    public String getHandleName()
    {
        if ( handleName.compareTo( "" ) == 0 )
        {
            return userId;
        }
        else
        {
            return handleName;
        }
    }

    public String getHandleNameOnly()
    {
        return handleName;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public String getMailAddrMd5()
    {
        return mailAddrMd5;
    }

    public String getMailAddrMobile()
    {
        return mailAddrMobile;
    }

    public String getMailAddrMobileMd5()
    {
        return mailAddrMobileMd5;
    }

    public String getPasswd()
    {
        return passwd;
    }

    public int getRegistDateMobile()
    {
        return registDateMobile;
    }

    public int getRegistDatePc()
    {
        return registDatePc;
    }

    public int getRegistStatus()
    {
        return registStatus;
    }

    public int getRegistTimeMobile()
    {
        return registTimeMobile;
    }

    public int getRegistTimePc()
    {
        return registTimePc;
    }

    public int getTempDateMobile()
    {
        return tempDateMobile;
    }

    public int getTempDatePc()
    {
        return tempDatePc;
    }

    public int getTempTimeMobile()
    {
        return tempTimeMobile;
    }

    public int getTempTimePc()
    {
        return tempTimePc;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getOldMailAddr()
    {
        return oldMailAddr;
    }

    public int getTempDate()
    {
        return tempDate;
    }

    public int getTempTime()
    {
        return tempTime;
    }

    public void setHandleName(String handleName)
    {
        this.handleName = handleName;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setMailAddrMd5(String mailAddrMd5)
    {
        this.mailAddrMd5 = mailAddrMd5;
    }

    public void setMailAddrMobile(String mailAddrMobile)
    {
        this.mailAddrMobile = mailAddrMobile;
    }

    public void setMailAddrMobileMd5(String mailAddrMobileMd5)
    {
        this.mailAddrMobileMd5 = mailAddrMobileMd5;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }

    public void setRegistDateMobile(int registDateMobile)
    {
        this.registDateMobile = registDateMobile;
    }

    public void setRegistDatePc(int registDatePc)
    {
        this.registDatePc = registDatePc;
    }

    public void setRegistStatus(int registStatus)
    {
        this.registStatus = registStatus;
    }

    public void setRegistTimeMobile(int registTimeMobile)
    {
        this.registTimeMobile = registTimeMobile;
    }

    public void setRegistTimePc(int registTimePc)
    {
        this.registTimePc = registTimePc;
    }

    public void setTempDateMobile(int tempDateMobile)
    {
        this.tempDateMobile = tempDateMobile;
    }

    public void setTempDatePc(int tempDatePc)
    {
        this.tempDatePc = tempDatePc;
    }

    public void setTempTimeMobile(int tempTimeMobile)
    {
        this.tempTimeMobile = tempTimeMobile;
    }

    public void setTempTimePc(int tempTimePc)
    {
        this.tempTimePc = tempTimePc;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setOldMailAddr(String oldMailAddr)
    {
        this.oldMailAddr = oldMailAddr;
    }

    /**
     * ユーザ端末番号を設定する(DoCoMo用)
     * 
     * @param request HTTPリクエスト
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setTermInfoDoCoMo(HttpServletRequest request)
    {
        boolean ret;
        String paramUid;
        String paramAct;
        final String memo = "端末番号が一致するユーザ退会のため";
        UserBasicInfo ubi;

        // 端末番号の取得
        paramUid = request.getParameter( "uid" );
        if ( paramUid == null )
        {
            return(false);
        }
        if ( paramUid.compareTo( "" ) == 0 )
        {
            return(false);
        }

        // アクションの取得
        paramAct = request.getParameter( "act" );
        if ( paramAct == null )
        {
            return(false);
        }
        if ( paramAct.compareTo( "" ) == 0 )
        {
            return(false);
        }

        ret = false;

        // マイメニュー登録処理
        if ( paramAct.compareTo( "reg" ) == 0 )
        {
            // マイメニュー登録時はｽﾃｰﾀｽを1にしてhh_user_basicに書き込み
            try
            {
                ubi = new UserBasicInfo();

                // ユーザ端末番号仮領域既存データチェック
                ret = ubi.getUserBasicByTermno( paramUid );
                if ( ret == false )
                {
                    ubi.getUserInfo().getRegistStatus();
                    ubi.getUserInfo().setUserId( paramUid );
                    ubi.getUserInfo().setMobileTermNo( paramUid );
                    ubi.getUserInfo().setRegistStatus( 1 );
                    ubi.getUserInfo().setRegistDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    ubi.getUserInfo().setRegistTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ubi.getUserInfo().setDocomoFlag( 1 );
                    ret = ubi.getUserInfo().insertData();
                    System.out.println( "insert結果:" + ret );
                }
                else
                {
                    ubi.getUserInfo().setRegistStatus( 1 );
                    ubi.getUserInfo().setRegistDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    ubi.getUserInfo().setRegistTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ubi.getUserInfo().setDocomoFlag( 1 );
                    ret = ubi.getUserInfo().updateData( paramUid );
                    System.out.println( "update結果:" + ret );
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegist.setTermInfoDoCoMo] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        // マイメニュー削除処理
        else if ( paramAct.compareTo( "rel" ) == 0 )
        {
            try
            {
                ubi = new UserBasicInfo();

                // ユーザ端末番号既存データチェック
                ret = ubi.getUserBasicByTermno( paramUid );
                if ( ret != false )
                {
                    if ( ubi.getUserInfo().getRegistStatus() == 1 )
                    {
                        ret = ubi.getUserInfo().deleteData( ubi.getUserInfo().getUserId() );
                    }
                    else
                    {
                        // ユーザ基本情報の削除フラグに1を立てる
                        ubi.getUserInfo().setDelFlag( 1 );
                        ubi.getUserInfo().setMailAddr( "" );
                        ubi.getUserInfo().setMailAddrMobile( "" );
                        ubi.getUserInfo().setDelDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        ubi.getUserInfo().setDelTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        ret = ubi.getUserInfo().updateData( ubi.getUserInfo().getUserId() );
                    }
                    if ( ret == false )
                    {
                        Logging.info( "マイメニュー削除失敗" +
                                "user_id=" + ubi.getUserInfo().getUserId() +
                                "regist_status=" + ubi.getUserInfo().getRegistStatus() +
                                "term_no=" + ubi.getUserInfo().getMobileTermNo() +
                                "temp_date_mobile=" + ubi.getUserInfo().getTempDateMobile() +
                                "temp_time_mobile=" + ubi.getUserInfo().getTempTimeMobile() +
                                "regist_date_mobile=" + ubi.getUserInfo().getRegistDateMobile() +
                                "regist_time_mobile=" + ubi.getUserInfo().getRegistTimeMobile() +
                                "del_reason=" + ubi.getUserInfo().getDelReason() );
                    }
                    else
                    {
                        // 登録未完了の半端なデータの削除フラグを立てる
                        ubi.deleteOddDataByTermno( paramUid, memo );
                    }
                }
                // 削除の場合は常にOKとする
                ret = true;
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegist.setTermInfoDoCoMo] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * ユーザ端末番号を設定する(SoftBank用)
     * 
     * @param request HTTPリクエスト
     * @param appendFlag 登録フラグ(true:登録,false:解除)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setTermInfoSoftBank(HttpServletRequest request, boolean appendFlag)
    {
        boolean ret;
        String paramUid;
        DataMasterUseragent dmu;
        DataUserBasic dub;
        UserBasicInfo ubi;

        // UAのチェック
        dmu = new DataMasterUseragent();
        dmu.getData( request );
        if ( dmu.getCarrierFlag() != DataMasterUseragent.CARRIER_SOFTBANK )
        {
            return(false);
        }

        // 端末番号の取得
        paramUid = request.getHeader( "x-jphone-uid" );
        if ( paramUid == null )
        {
            return(false);
        }
        if ( paramUid.compareTo( "" ) == 0 )
        {
            return(false);
        }
        if ( paramUid.compareTo( "NULL" ) == 0 )
        {
            return(false);
        }

        ret = false;

        // マイメニュー登録処理
        if ( appendFlag != false )
        {
            // マイメニュー登録時は仮領域DBに書き込み
            try
            {
                ubi = new UserBasicInfo();

                // ユーザ端末番号仮領域既存データチェック
                ret = ubi.getUserBasicByTermno( paramUid );
                if ( ret == false )
                {
                    ubi.getUserInfo().setUserId( paramUid );
                    ubi.getUserInfo().setMobileTermNo( paramUid );
                    ubi.getUserInfo().setRegistStatus( 1 );
                    ubi.getUserInfo().setRegistDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    ubi.getUserInfo().setRegistTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = ubi.getUserInfo().insertData();
                }
                else
                {
                    ubi.getUserInfo().setRegistStatus( 1 );
                    ubi.getUserInfo().setRegistDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    ubi.getUserInfo().setRegistTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = ubi.getUserInfo().updateData( paramUid );
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegist.setTermInfoDoCoMo] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        // マイメニュー削除処理
        else
        {
            try
            {
                dub = new DataUserBasic();
                ubi = new UserBasicInfo();

                // ユーザ端末番号既存データチェック
                ret = ubi.getUserBasicByTermno( paramUid );
                if ( ret != false )
                {
                    // ユーザ基本情報の削除フラグに1を立てる
                    dub.setDelFlag( 1 );
                    dub.setDelDateMobile( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dub.setDelTimeMobile( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    ret = dub.updateData( ubi.getUserInfo().getUserId() );
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserRegist.setTermInfoDoCoMo] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    public boolean getUser(String mail_hash)
    {
        boolean ret = false;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        String query = "SELECT * FROM hh_user_basic  WHERE (mail_addr_md5 = ? OR mail_addr_mobile_md5 = ?) AND del_flag=0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, mail_hash );
            prestate.setString( 2, mail_hash );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.passwd = result.getString( "passwd" );
                    this.registStatus = result.getInt( "regist_status" );
                    this.handleName = result.getString( "handle_name" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.mailAddrMd5 = result.getString( "mail_addr_md5" );
                    this.mailAddrMobile = result.getString( "mail_addr_mobile" );
                    this.mailAddrMobileMd5 = result.getString( "mail_addr_mobile_md5" );
                    this.tempDatePc = result.getInt( "temp_date_pc" );
                    this.tempTimePc = result.getInt( "temp_time_pc" );
                    this.tempDateMobile = result.getInt( "temp_date_mobile" );
                    this.tempTimeMobile = result.getInt( "temp_time_mobile" );
                    this.registDatePc = result.getInt( "regist_date_pc" );
                    this.registTimePc = result.getInt( "regist_time_pc" );
                    this.registDateMobile = result.getInt( "regist_date_mobile" );
                    this.registTimeMobile = result.getInt( "regist_time_mobile" );
                    this.smartPhoneFlag = result.getInt( "smartphone_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserRegist.getUser] Exception=" + e.toString() );
            return(ret);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    public boolean getUserMailChange(String mail_hash)
    {
        boolean ret = false;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "SELECT * FROM hh_user_basic";
        query += " INNER JOIN hh_user_mail_change ON hh_user_basic.user_id = hh_user_mail_change.user_id";
        query += " WHERE hh_user_mail_change.mail_hash = ? AND hh_user_basic.del_flag=0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, mail_hash );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "hh_user_basic.user_id" );
                    this.passwd = result.getString( "hh_user_basic.passwd" );
                    this.registStatus = result.getInt( "hh_user_basic.regist_status" );
                    this.handleName = result.getString( "hh_user_basic.handle_name" );
                    this.mailAddr = result.getString( "hh_user_mail_change.mail_addr" );
                    this.oldMailAddr = result.getString( "hh_user_mail_change.old_mail_addr" );
                    this.tempDate = result.getInt( "hh_user_mail_change.temp_date" );
                    this.tempTime = result.getInt( "hh_user_mail_change.temp_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserRegist.getUserMailChange] Exception=" + e.toString() );
            return(ret);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * メール認証後会員を登録する
     * 
     * @param userId ユーザーID
     * @param mailAddr メールアドレス
     * @param request HTTPリクエスト
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean regist(String userId, String mailAddr, HttpServletRequest request)
    {
        boolean ret = false;
        int mailKind = CheckMailAddr.checkMailKind( mailAddr );
        int userAgentKind = UserAgent.getUserAgentType( request ) == UserAgent.USERAGENT_PC ? 1 : 2;
        String userAgent = UserAgent.getUserAgent( request );

        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        DataUserBasic dub = new DataUserBasic();
        DataUserLogin dul = new DataUserLogin();

        if ( dub.getData( userId ) != false )
        {
            dub.setRegistStatus( 9 );
            if ( mailKind == 1 )
            {
                dub.setRegistDatePc( nowDate );
                dub.setRegistTimePc( nowTime );
            }
            else
            {
                dub.setRegistDateMobile( nowDate );
                dub.setRegistTimeMobile( nowTime );
            }
            ret = dub.updateData( userId );
        }

        boolean isExist = false;

        if ( ret )
        {
            isExist = dul.getData( mailAddr );
            dul.setLoginId( mailAddr );
            dul.setUserId( userId );
            dul.setSecurityKey( RandomString.getSecurityKey() );

            if ( userAgentKind == 1 )
            {
                dul.setUserAgentPc( userAgent );
            }
            else
            {
                dul.setUserAgentMobile( userAgent );
            }
            if ( isExist )
            {
                ret = dul.updateData( mailAddr );
            }
            else
            {
                ret = dul.insertData();
            }

            dul = new DataUserLogin();
            isExist = dul.getData( userId );
            dul.setLoginId( userId );
            dul.setUserId( userId );
            dul.setSecurityKey( RandomString.getSecurityKey() );
            if ( userAgentKind == 1 )
            {
                dul.setUserAgentPc( userAgent );
            }
            else
            {
                dul.setUserAgentMobile( userAgent );
            }
            if ( isExist )
            {
                dul.updateData( userId );
            }
            else
            {
                dul.insertData();
            }
        }
        return(ret);
    }

    /**
     * パスワードの変更
     * 
     * @param userId ユーザID
     * @param passWd
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean changePassWd(String userId)
    {
        boolean ret = false;
        DataUserLogin dul = new DataUserLogin();
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "SELECT * FROM hh_user_login";
        query += " WHERE user_id = ? AND del_flag = 0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    dul = new DataUserLogin();
                    if ( dul.getData( result.getString( "login_id" ) ) != false )
                    {
                        dul.setSecurityKey( RandomString.getSecurityKey() );
                        dul.setLatestDate( nowDate );
                        dul.setLatestTime( nowTime );
                        dul.updateData( result.getString( "login_id" ) );
                    }
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserRegist.getUserMailChange] Exception=" + e.toString() );
            return(ret);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    public boolean changeMailAddr(String userId, String mailAddr, String oldMailAddr)
    {

        String query = "";
        boolean isExist = false;
        mailAddrMd5 = ReplaceString.replaceMd5( mailAddr );

        boolean ret = false;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        try
        {
            connection = DBConnection.getConnection();
            query = "SELECT 1 FROM hh_user_mail_change WHERE mail_hash = ?";
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, mailAddrMd5 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                isExist = result.next();
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[changeMailAddr] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        try
        {
            if ( isExist )
            {
                query = "UPDATE hh_user_mail_change SET ";
                query = query + " user_id = ? ";
                query = query + ",temp_date = ?";
                query = query + ",temp_time = ? ";
                query = query + ",mail_addr = ? ";
                query = query + ",old_mail_addr = ? ";
                query = query + " WHERE mail_hash =? ";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, userId );
                prestate.setInt( 2, nowDate );
                prestate.setInt( 3, nowTime );
                prestate.setString( 4, mailAddr );
                prestate.setString( 5, oldMailAddr );
                prestate.setString( 6, mailAddrMd5 );
            }
            else
            {
                query = "INSERT hh_user_mail_change SET ";
                query = query + "mail_hash = ? ";
                query = query + ",user_id = ?  ";
                query = query + ",temp_date = ? ";
                query = query + ",temp_time = ? ";
                query = query + ",mail_addr = ? ";
                query = query + ",old_mail_addr = ? ";
                prestate = connection.prepareStatement( query );
                prestate.setString( 1, mailAddrMd5 );
                prestate.setString( 2, userId );
                prestate.setInt( 3, nowDate );
                prestate.setInt( 4, nowTime );
                prestate.setString( 5, mailAddr );
                prestate.setString( 6, oldMailAddr );
            }
            if ( prestate.executeUpdate() == 1 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[changeMailAddr] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * メーアドレスの変更
     * 
     * @param userId ユーザーID
     * @param mailAddr 変更メールアドレス
     * @param oldMailAddr 変更前メールアドレス
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean changeMailAddrRegist(String userId, String mailAddr, String oldMailAddr)
    {
        int mailKind = CheckMailAddr.checkMailKind( mailAddr );
        boolean ret = false;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        DataUserBasic dub = new DataUserBasic();
        DataUserLogin dul = new DataUserLogin();

        // *認証が通ったら、hh_user_basic のメールアドレスを変更します。
        if ( dub.getData( userId ) != false )
        {
            dub.setRegistStatus( 9 );
            if ( mailKind == 1 )
            {
                dub.setMailAddr( mailAddr );
                dub.setMailAddrMd5( ReplaceString.replaceMd5( mailAddr ) );
                dub.setMailAddrMobile( "" );
                dub.setMailAddrMobileMd5( "" );
                dub.setMailAddrUnknown( 0 );
            }
            else
            {
                dub.setMailAddr( "" );
                dub.setMailAddrMd5( "" );
                dub.setMailAddrMobile( mailAddr );
                dub.setMailAddrMobileMd5( ReplaceString.replaceMd5( mailAddr ) );
                dub.setMailAddrMobileUnknown( 0 );
            }
            ret = dub.updateData( userId );
        }

        // *旧メールアドレスはログインで使えなくします。
        if ( ret != false )
        {
            if ( dul.getData( oldMailAddr ) != false )
            {
                dul.setUserId( userId );
                dul.setSecurityKey( RandomString.getSecurityKey() );
                dul.setRegistFlag( 0 );
                dul.setDelFlag( 2 );
                dul.setLatestDate( nowDate );
                dul.setLatestTime( nowTime );
                ret = dul.updateData( oldMailAddr );
            }
            else
            {
                dul = new DataUserLogin();
                dul.setLoginId( oldMailAddr );
                dul.setUserId( userId );
                dul.setSecurityKey( RandomString.getSecurityKey() );
                dul.setRegistFlag( 0 );
                dul.setDelFlag( 2 );
                dul.setRegistDate( nowDate );
                dul.setRegistTime( nowTime );
                dul.setLatestDate( nowDate );
                dul.setLatestTime( nowTime );
                ret = dul.insertData();
            }
        }

        // *新メールアドレスはログインで使えるようにします。
        if ( ret != false )
        {
            if ( dul.getData( mailAddr ) != false )
            {
                dul.setUserId( userId );
                dul.setSecurityKey( RandomString.getSecurityKey() );
                dul.setRegistFlag( 1 );
                dul.setDelFlag( 0 );
                dul.setLatestDate( nowDate );
                dul.setLatestTime( nowTime );
                ret = dul.updateData( mailAddr );
            }
            else
            {
                dul = new DataUserLogin();
                dul.setLoginId( mailAddr );
                dul.setUserId( userId );
                dul.setSecurityKey( RandomString.getSecurityKey() );
                dul.setRegistFlag( 1 );
                dul.setRegistDate( nowDate );
                dul.setRegistTime( nowTime );
                dul.setLatestDate( nowDate );
                dul.setLatestTime( nowTime );
                ret = dul.insertData();
            }
        }
        return ret;
    }

}
