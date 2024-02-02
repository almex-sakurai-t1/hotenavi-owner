/*
 * @(#)DataUserMyArea.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザマイエリア情報取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザマイエリア情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/27
 */
public class DataUserMyArea implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 7552386622418742255L;

    private String            userId;
    private int               seq;
    private int               areaKind;
    private int               jisCode;
    private int               areaId;
    private int               appendDate;
    private int               appendTime;
    private int               delFlag;
    private int               pushFlag;

    /**
     * データを初期化します。
     */
    public DataUserMyArea()
    {
        userId = "";
        seq = 0;
        areaKind = 0;
        jisCode = 0;
        areaId = 0;
        appendDate = 0;
        appendTime = 0;
        delFlag = 0;
        pushFlag = 1;
    }

    public int getAppendDate()
    {
        return appendDate;
    }

    public int getAppendTime()
    {
        return appendTime;
    }

    public int getAreaId()
    {
        return areaId;
    }

    public int getAreaKind()
    {
        return areaKind;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getPushFlag()
    {
        return pushFlag;
    }

    public void setAppendDate(int appendDate)
    {
        this.appendDate = appendDate;
    }

    public void setAppendTime(int appendTime)
    {
        this.appendTime = appendTime;
    }

    public void setAreaId(int areaId)
    {
        this.areaId = areaId;
    }

    public void setAreaKind(int areaKind)
    {
        this.areaKind = areaKind;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setPushFlag(int pushFlag)
    {
        this.pushFlag = pushFlag;
    }

    /**
     * ユーザマイエリアデータ取得
     * 
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_myarea WHERE user_id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.seq = result.getInt( "seq" );
                    this.areaKind = result.getInt( "area_kind" );
                    this.jisCode = result.getInt( "jis_code" );
                    this.areaId = result.getInt( "area_id" );
                    this.appendDate = result.getInt( "append_date" );
                    this.appendTime = result.getInt( "append_time" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.pushFlag = result.getInt( "push_flag" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMyArea.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザマイエリアデータ設定
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
                this.userId = result.getString( "user_id" );
                this.seq = result.getInt( "seq" );
                this.areaKind = result.getInt( "area_kind" );
                this.jisCode = result.getInt( "jis_code" );
                this.areaId = result.getInt( "area_id" );
                this.appendDate = result.getInt( "append_date" );
                this.appendTime = result.getInt( "append_time" );
                this.delFlag = result.getInt( "del_flag" );
                this.pushFlag = result.getInt( "push_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMyArea.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザマイエリア情報データ追加
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

        query = "INSERT hh_user_myarea SET ";
        query = query + " user_id = ?,";
        query = query + " seq = 0,";
        query = query + " area_kind = ?,";
        query = query + " jis_code = ?,";
        query = query + " area_id = ?,";
        query = query + " append_date = ?,";
        query = query + " append_time = ?,";
        query = query + " del_flag = ?,";
        query = query + " push_flag = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.areaKind );
            prestate.setInt( 3, this.jisCode );
            prestate.setInt( 4, this.areaId );
            prestate.setInt( 5, this.appendDate );
            prestate.setInt( 6, this.appendTime );
            prestate.setInt( 7, this.delFlag );
            prestate.setInt( 8, this.pushFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMyArea.insertData] Exception=" + e.toString() );
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
     * ユーザマイエリア情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE  hh_user_myarea SET ";
        query = query + " area_kind = ?,";
        query = query + " jis_code = ?,";
        query = query + " area_id = ?,";
        query = query + " append_date = ?,";
        query = query + " append_time = ?,";
        query = query + " del_flag = ?,";
        query = query + " push_flag = ?";
        query = query + " WHERE user_id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.areaKind );
            prestate.setInt( 2, this.jisCode );
            prestate.setInt( 3, this.areaId );
            prestate.setInt( 4, this.appendDate );
            prestate.setInt( 5, this.appendTime );
            prestate.setInt( 6, this.delFlag );
            prestate.setInt( 7, this.pushFlag );
            prestate.setString( 8, userId );
            prestate.setInt( 9, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMyArea.updateData] Exception=" + e.toString() );
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
