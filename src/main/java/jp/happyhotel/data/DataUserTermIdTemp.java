/*
 * @(#)DataUserTermIdTemp.java 1.00 2007/08/12 Copyright (C) ALMEX Inc. 2007 ユーザ端末情報仮領域データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ユーザ端末情報仮領域データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/12
 * @version 1.1 2007/11/27
 */
public class DataUserTermIdTemp implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -5271822083421316016L;

    private String            mobileTermNo;
    private int               termnoStatus;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データを初期化します。
     */
    public DataUserTermIdTemp()
    {
        mobileTermNo = "";
        termnoStatus = 0;
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

    public String getMobileTermNo()
    {
        return mobileTermNo;
    }

    public int getTermnoStatus()
    {
        return termnoStatus;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setMobileTermNo(String mobileTermNo)
    {
        this.mobileTermNo = mobileTermNo;
    }

    public void setTermnoStatus(int termnoStatus)
    {
        this.termnoStatus = termnoStatus;
    }

    /**
     * ユーザ端末情報仮領域データ取得
     * 
     * @param mobileTermNo ユーザ端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String mobileTermNo)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_idtemp WHERE mobile_termno = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, mobileTermNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.mobileTermNo = result.getString( "mobile_termno" );
                    this.termnoStatus = result.getInt( "termno_status" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserTermIdTemp.getData] Exception=" + e.toString() );
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
                this.mobileTermNo = result.getString( "mobile_termno" );
                this.termnoStatus = result.getInt( "termno_status" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserTermIdTemp.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_user_idtemp SET ";
        query = query + " mobile_termno = ?,";
        query = query + " termno_status = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.mobileTermNo );
            prestate.setInt( 2, this.termnoStatus );
            prestate.setInt( 3, this.lastUpdate );
            prestate.setInt( 4, this.lastUptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserTermIdTemp.insertData] Exception=" + e.toString() );
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

        query = "UPDATE hh_user_idtemp SET ";
        query = query + " termno_status = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE mobile_termno = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.termnoStatus );
            prestate.setInt( 2, this.lastUpdate );
            prestate.setInt( 3, this.lastUptime );
            prestate.setString( 4, mobileTermno );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserTermIdTemp.updateData] Exception=" + e.toString() );
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
