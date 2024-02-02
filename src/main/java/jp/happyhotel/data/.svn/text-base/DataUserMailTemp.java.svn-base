/*
 * @(#)DataUserMailTemp.java 1.00 2007/08/12 Copyright (C) ALMEX Inc. 2007 ユーザ端末情報仮領域データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * メール情報仮領域データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/12
 * @version 1.1 2007/11/27
 */
public class DataUserMailTemp implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -9087687522985823622L;

    private String            mailAddr;
    private int               mailStatus;
    private String            mailHash;
    private int               mailUnknownFlag;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データを初期化します。
     */
    public DataUserMailTemp()
    {
        mailAddr = "";
        mailStatus = 0;
        mailHash = "";
        mailUnknownFlag = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public String getMailHash()
    {
        return mailHash;
    }

    public int getMailStatus()
    {
        return mailStatus;
    }

    public int getMailUnknownFlag()
    {
        return mailUnknownFlag;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setMailHash(String mailHash)
    {
        this.mailHash = mailHash;
    }

    public void setMailStatus(int mailStatus)
    {
        this.mailStatus = mailStatus;
    }

    public void setMailUnknownFlag(int mailUnknownFlag)
    {
        this.mailUnknownFlag = mailUnknownFlag;
    }

    /**
     * メール情報仮領域データ取得
     * 
     * @param mailHash メールハッシュ値
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String mailHash)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_mailtemp WHERE mail_hash = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, mailHash );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.mailAddr = result.getString( "mail_addr" );
                    this.mailStatus = result.getInt( "mail_status" );
                    this.mailHash = result.getString( "mail_hash" );
                    this.mailUnknownFlag = result.getInt( "unknown_flag" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMailTemp.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザ端末情報仮領域データ設定
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.mailAddr = result.getString( "mail_addr" );
                this.mailStatus = result.getInt( "mail_status" );
                this.mailHash = result.getString( "mail_hash" );
                this.mailUnknownFlag = result.getInt( "unknown_flag" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMailTemp.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザ端末情報仮領域データ追加
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

        query = "INSERT hh_user_mailtemp SET ";
        query = query + " mail_addr = ?,";
        query = query + " mail_status = ?,";
        query = query + " mail_hash = ?,";
        query = query + " unknown_flag = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.mailAddr );
            prestate.setInt( 2, this.mailStatus );
            prestate.setString( 3, this.mailHash );
            prestate.setInt( 4, this.mailUnknownFlag );
            prestate.setInt( 5, this.lastUpdate );
            prestate.setInt( 6, this.lastUptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMailTemp.insertData] Exception=" + e.toString() );
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
     * ユーザ端末情報仮領域データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param mobileTermno 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String mobileTermno)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_mailtemp SET ";
        query = query + " mail_status = ?,";
        query = query + " mail_hash = ?,";
        query = query + " unknown_flag = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE mail_addr = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.mailStatus );
            prestate.setString( 2, this.mailHash );
            prestate.setInt( 3, this.mailUnknownFlag );
            prestate.setInt( 4, this.lastUpdate );
            prestate.setInt( 5, this.lastUptime );
            prestate.setString( 6, this.mailAddr );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMailTemp.updateData] Exception=" + e.toString() );
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
