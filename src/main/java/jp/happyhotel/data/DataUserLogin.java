/*
 * @(#)DataUserLogin.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ユーザログイン情報情報取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザログイン情報情報取得クラス
 * 
 * @author T.Sakurai
 * @version 1.00 2019/08/27
 * @version 1.1 2007/11/26
 * @version
 */
public class DataUserLogin implements Serializable
{
    private String loginId;
    private String userId;
    private String securityKey;
    private int    registFlag;
    private int    delFlag;
    private String userAgentPc;
    private String userAgentMobile;
    private int    registDate;
    private int    registTime;
    private int    latestDate;
    private int    latestTime;

    /**
     * データを初期化します。
     */
    public DataUserLogin()
    {

        loginId = "";
        userId = "";
        securityKey = "";
        registFlag = 0;
        delFlag = 0;
        userAgentPc = "";
        userAgentMobile = "";
        registDate = 0;
        registTime = 0;
        latestDate = 0;
        latestTime = 0;
    }

    public String getLoginId()
    {
        return loginId;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getSecurityKey()
    {
        return securityKey;
    }

    public int getRegistFlag()
    {
        return registFlag;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public String getUserAgentPc()
    {
        return userAgentPc;
    }

    public String getUserAgentMobile()
    {
        return userAgentMobile;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getLatestDate()
    {
        return latestDate;
    }

    public int getLatestTime()
    {
        return latestTime;
    }

    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setSecurityKey(String securityKey)
    {
        this.securityKey = securityKey;
    }

    public void setRegistFlag(int registFlag)
    {
        this.registFlag = registFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setUserAgentPc(String userAgentPc)
    {
        this.userAgentPc = userAgentPc;
    }

    public void setUserAgentMobile(String userAgentMobile)
    {
        this.userAgentMobile = userAgentMobile;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setLatestDate(int latestDate)
    {
        this.latestDate = latestDate;
    }

    public void setLatestTime(int latestTime)
    {
        this.latestTime = latestTime;
    }

    /**
     * ユーザログイン情報データ取得
     * 
     * @param userId ユーザログイン情報データ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String loginId)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_login WHERE login_id = ? AND del_flag != 1";
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
                    setData( result );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserLogin.getData] Exception=" + e.toString() );
            return ret;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * ユーザログイン情報データ設定
     * 
     * @param result ユーザログイン情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.loginId = result.getString( "login_id" );
                this.userId = result.getString( "user_id" );
                this.securityKey = result.getString( "security_key" );
                this.registFlag = result.getInt( "regist_flag" );
                this.delFlag = result.getInt( "del_flag" );
                this.userAgentPc = result.getString( "user_agent_pc" );
                this.userAgentMobile = result.getString( "user_agent_mobile" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.latestDate = result.getInt( "latest_date" );
                this.latestTime = result.getInt( "latest_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserLogin.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザログイン情報情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT INTO hh_user_login SET ";
        query += " login_id = ? ";
        query += ",user_id = ? ";
        query += ",security_key = ? ";
        query += ",regist_flag = ?";
        query += ",del_flag = ?";
        query += ",user_agent_pc = ?";
        query += ",user_agent_mobile = ?";
        query += ",regist_date = ?";
        query += ",regist_time = ?";
        query += ",latest_date = ?";
        query += ",latest_time = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.loginId );
            prestate.setString( 2, this.userId );
            prestate.setString( 3, this.securityKey );
            prestate.setInt( 4, this.registFlag );
            prestate.setInt( 5, this.delFlag );
            prestate.setString( 6, this.userAgentPc );
            prestate.setString( 7, this.userAgentMobile );
            prestate.setInt( 8, this.registDate );
            prestate.setInt( 9, this.registTime );
            prestate.setInt( 10, this.latestDate );
            prestate.setInt( 11, this.latestTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserLogin.insertData] Exception=" + e.toString() );
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
     * ユーザログイン情報情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String loginId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "UPDATE hh_user_login SET ";
        query += " user_id = ? ";
        query += ",security_key = ? ";
        query += ",regist_flag = ?";
        query += ",del_flag = ?";
        query += ",user_agent_pc = ?";
        query += ",user_agent_mobile = ?";
        query += ",regist_date = ?";
        query += ",regist_time = ?";
        query += ",latest_date = ?";
        query += ",latest_time = ?";
        query += " WHERE  login_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.securityKey );
            prestate.setInt( 3, this.registFlag );
            prestate.setInt( 4, this.delFlag );
            prestate.setString( 5, this.userAgentPc );
            prestate.setString( 6, this.userAgentMobile );
            prestate.setInt( 7, this.registDate );
            prestate.setInt( 8, this.registTime );
            prestate.setInt( 9, this.latestDate );
            prestate.setInt( 10, this.latestTime );
            prestate.setString( 11, this.loginId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserLogin.updateData] Exception=" + e.toString() );
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
     * ユーザログイン情報情報データ存在チェック
     * 
     * @param loginId
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean existsData(String loginId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT count(*) as cnt FROM hh_user_login WHERE login_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int i = 1;
            prestate.setString( i++, loginId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( result.getInt( "cnt" ) > 0 )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhUserLogin.existsData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
