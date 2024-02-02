/*
 * @(#)DataUserR18Check.java 1.00
 * 2007/08/12 Copyright (C) ALMEX Inc. 2007
 * ユーザ18禁確認データ取得クラス
 */

package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Dec 24 16:21:27 JST 2010
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザ18禁確認データ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.0
 */
public class DataUserR18Check implements Serializable
{
    private static final long serialVersionUID = -2610511873932083846L;
    private String            mobileTermNo;
    private int               registDate;
    private int               registTime;

    public DataUserR18Check()
    {
        mobileTermNo = "";
        registDate = 0;
        registTime = 0;
    }

    public String getMobileTermno()
    {
        return this.mobileTermNo;
    }

    public int getRegistDate()
    {
        return this.registDate;
    }

    public int getRegistTime()
    {
        return this.registTime;
    }

    public void setMobileTermno(String mobileTermno)
    {
        this.mobileTermNo = mobileTermno;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    /**
     * ユーザ18禁確認データ取得
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

        query = "SELECT * FROM hh_user_r18_check WHERE mobile_termno = ?";
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
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserR18Check.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザ18禁確認データ取得
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.mobileTermNo = result.getString( "mobile_termno" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserR18Check.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ユーザ18禁確認データ追加
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

        query = "INSERT hh_user_r18_check SET ";
        query = query + " mobile_termno = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.mobileTermNo );
            prestate.setInt( 2, this.registDate );
            prestate.setInt( 3, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserR18Check.insertData] Exception=" + e.toString() );
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
     * ユーザ18禁確認データ変更
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

        query = "UPDATE hh_user_r18_check SET ";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?";
        query = query + " WHERE mobile_termno = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.registDate );
            prestate.setInt( 2, this.registTime );
            prestate.setString( 3, mobileTermno );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserR18Check.updateData] Exception=" + e.toString() );
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
