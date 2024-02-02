/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
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
 * ユーザ基本情報取得クラス。 ユーザの基本情報を取得する機能を提供する
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
     * データを初期化します。
     */
    public UserBasicInfo()
    {
        userBasicCount = 0;
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

    /** ユーザ基本情報取得 **/
    public DataUserBasic[] getUserInfoMulti()
    {
        return(userBasicMulti);
    }

    /**
     * ユーザ基本情報を取得する（IDから）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ユーザ基本情報を取得する（全データより）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ユーザ基本情報を取得する（IDから） ※登録ステータス無視
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ユーザ基本情報を取得する（IDから・削除済のもの）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ユーザ基本情報を取得する（端末番号から）
     * 
     * @param mobileTermno 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ユーザ基本情報を取得する（端末番号から） ※登録ステータス無視
     * 
     * @param mobileTermno 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ユーザ基本情報を取得する（メールアドレスから）
     * 
     * @param mailAddr メールアドレス
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserBasicByMailaddr(String mailAddr)
    {
        return getUserBasicByMailaddr( mailAddr, 0 );
    }

    /**
     * ユーザ基本情報を取得する（メールアドレスから）
     * 
     * @param mailAddr メールアドレス
     * @param registStatus 登録ステータス
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
        // PCのメールアドレスからデータ取得
        query = "SELECT * FROM hh_user_basic";
        query += " WHERE mail_addr = ? ";
        query += " AND regist_status > ?";
        query += " AND del_flag = 0";
        // 携帯のメールアドレスからデータを取得
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
     * ユーザ基本情報を取得する（メールアドレスから複数ユーザ取得）
     * 
     * @param mailAddr メールアドレス
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
        // PCのメールアドレスからデータ取得
        query = "SELECT * FROM hh_user_basic";
        query += " WHERE mail_addr = ?";
        query += " AND regist_status > 0";
        query += " AND del_flag = 0";
        // 携帯のメールアドレスからデータ取得
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
     * ユーザ基本情報重複チェック（ユーザID、メールアドレスから）
     * 
     * @param userId ユーザID
     * @param mailAddr メールアドレス
     * @param mobileFlag 携帯フラグ(true:携帯、false:PC)
     * @return 処理結果(false:未登録,true:登録済み)
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
     * ユーザ基本情報重複チェック（ユーザID、メールアドレスから）
     * 
     * @param userId ユーザID
     * @param mailAddr メールアドレス
     * @param mobileFlag 携帯フラグ(true:携帯、false:PC)
     * @return 処理結果(false:未登録,true:登録済み)
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
     * ユーザ基本情報を取得する（メールアドレスから）
     * 
     * @param cookieValue クッキー値
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
            // メールアドレスのハッシュ値で取得
            query = "SELECT * FROM hh_user_basic";
            query += " WHERE mail_addr_md5 = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
            // 携帯メールアドレスハッシュ値で取得
            query += " UNION";
            query += " SELECT * FROM hh_user_basic";
            query += " WHERE mail_addr_mobile_md5 = ?";
            query += " AND regist_status > 0";
            query += " AND del_flag = 0";
            // ユーザIDで取得
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
     * ユーザ基本情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * ユーザ基本情報のデータをセット（複数ユーザ）
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
                // レコード件数取得
                if ( result.last() != false )
                {
                    userBasicCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userBasicMulti = new DataUserBasic[this.userBasicCount];
                for( i = 0 ; i < userBasicCount ; i++ )
                {
                    userBasicMulti[i] = new DataUserBasic();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
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
     * ユーザ基本情報を取得する（IDから）
     * 
     * @param strMd5 メールアドレスのハッシュ値
     * @return 処理結果(TRUE:正常,FALSE:異常)
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

            ret = getUserBasicSub( prestate );
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
     * 登録が完了していないデータの削除フラグを立てる（端末番号から）
     * 
     * @param mobileTermno 端末番号
     * @param memo メモ（削除理由）
     * @return 処理結果(TRUE:正常または該当データなし,FALSE:異常)
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
                // クラスの配列を用意し、初期化する。
                this.userBasicMulti = new DataUserBasic[this.userBasicCount];
                for( i = 0 ; i < userBasicCount ; i++ )
                {
                    userBasicMulti[i] = new DataUserBasic();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
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
     * 登録が完了していないデータの削除フラグを立てる（ユーザーID、端末番号から）
     * 
     * @param userId ユーザID
     * @param mobileTermno 端末番号
     * @param memo メモ（削除理由）
     * @return 処理結果(TRUE:正常または該当データなし,FALSE:異常)
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
                // クラスの配列を用意し、初期化する。
                this.userBasicMulti = new DataUserBasic[this.userBasicCount];
                for( i = 0 ; i < userBasicCount ; i++ )
                {
                    userBasicMulti[i] = new DataUserBasic();
                }
                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
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
     * ユーザ基本情報を取得する（IDから）
     * 
     * @param strMd5 メールアドレスのハッシュ値
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
            // メールアドレスのハッシュ値で取得
            query += " WHERE mail_addr_md5 = ?";
            query += " AND del_flag = 0";
            query += " AND mobile_termno <> '' ";
            query += " AND mobile_termno NOT LIKE '%DoCoMo%' ";
            query += " AND mobile_termno NOT LIKE '%J-PHONE%' ";
            query += " AND mobile_termno NOT LIKE '%Vodafone%' ";
            query += " AND mobile_termno NOT LIKE '%SoftBank%' ";
            query += " AND mobile_termno NOT LIKE '%KDDI%' ";
            // 携帯メールアドレスのハッシュ値で取得
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
            // データが取得できた場合、端末番号が入っているかどうか
            // ユーザーエージェント()が入っていないかチェック
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
     * ユーザ基本情報を取得する（IDから）
     * 
     * @param strMd5 メールアドレスのハッシュ値
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
            // 以前のユーザから端末番号を取得
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
                // 変更がうまくいったら
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
     * メールアドレスの登録情報を確認する（ユーザーIDから）
     * 
     * @param userId ユーザID
     * @return 処理結果(-1:例外発生時,0:登録なし,1:モバイル用のみあり,2:PC用のみあり,3:PCモバイル共にあり)
     */
    public int checkMailAddressRegistered(String userId)
    {
        // 出力用の定数の設定
        final int ERROR = -1; // 例外発生時などデータの取得ができなかった場合
        final int NONE_ADDRESS = 0; // 登録されているメールアドレスはない
        final int ONLY_MOBILE_ADDRESS = 1; // モバイル用のみ登録あり
        final int ONLY_PC_ADDRESS = 2; // PC用のみ登録あり
        final int PC_AND_MOBILE_ADDRESS = 3; // PC用、モバイル用共に登録あり

        // 何か異常があった場合などは終了
        if ( getUserBasic( userId ) == false )
        {
            return ERROR;
        }

        // メールアドレスの取得
        String pc_address = userBasic.getMailAddr();
        String mobile_address = userBasic.getMailAddrMobile();

        // 登録情報の確認
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
     * ラブインジャパンユーザー判定
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常または該当データなし,FALSE:異常)
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

        // ユーザーIDが「lj_」から始まっていなければラブインジャパンユーザーではない
        if ( userId.indexOf( "lj_" ) != 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_user_basic";
        query = query + " WHERE temp_date_pc = 0"; // 仮登録日付（PC）
        query = query + " AND temp_date_mobile = 0"; // 仮登録日付（携帯）
        query = query + " AND regist_status = 9";// 登録ステータス(9：登録済み)
        query = query + " AND regist_date_pay = 0";// 有料会員登録日付
        query = query + " AND smartphone_flag = 0";// スマートフォンフラグ(1：スマートフォンで登録)
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
