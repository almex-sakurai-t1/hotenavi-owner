/*
 * @(#)DataUserPoint.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザポイントデータ情報取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ユーザポイントデータ情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/27
 */
public class DataUserPoint implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -3525343607394356528L;

    private String            userId;
    private int               seq;
    private int               getDate;
    private int               getTime;
    private int               code;
    private int               point;
    private int               pointKind;
    private int               extCode;
    private String            extString;
    private String            personCode;
    private String            appendReason;

    /**
     * データを初期化します。
     */
    public DataUserPoint()
    {
        userId = "";
        seq = 0;
        getDate = 0;
        getTime = 0;
        code = 0;
        point = 0;
        pointKind = 0;
        extCode = 0;
        extString = "";
        personCode = "";
        appendReason = "";
    }

    public String getAppendReason()
    {
        return appendReason;
    }

    public int getCode()
    {
        return code;
    }

    public int getExtCode()
    {
        return extCode;
    }

    public String getExtString()
    {
        return extString;
    }

    public int getGetDate()
    {
        return getDate;
    }

    public int getGetTime()
    {
        return getTime;
    }

    public String getPersonCode()
    {
        return personCode;
    }

    public int getPoint()
    {
        return point;
    }

    public int getPointKind()
    {
        return pointKind;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setAppendReason(String appendReason)
    {
        this.appendReason = appendReason;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setExtCode(int extCode)
    {
        this.extCode = extCode;
    }

    public void setExtString(String extString)
    {
        this.extString = extString;
    }

    public void setGetDate(int getDate)
    {
        this.getDate = getDate;
    }

    public void setGetTime(int getTime)
    {
        this.getTime = getTime;
    }

    public void setPersonCode(String personCode)
    {
        this.personCode = personCode;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setPointKind(int pointKind)
    {
        this.pointKind = pointKind;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * ユーザポイントデータ取得
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

        query = "SELECT * FROM hh_user_point WHERE user_id = ? AND seq = ?";

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
                    this.getDate = result.getInt( "get_date" );
                    this.getTime = result.getInt( "get_time" );
                    this.code = result.getInt( "code" );
                    this.point = result.getInt( "point" );
                    this.pointKind = result.getInt( "point_kind" );
                    this.extCode = result.getInt( "ext_code" );
                    this.extString = result.getString( "ext_string" );
                    this.personCode = result.getString( "person_code" );
                    this.appendReason = result.getString( "append_reason" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPoint.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザポイントデータ設定
     * 
     * @param result ユーザポイントデータレコード
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
                this.getDate = result.getInt( "get_date" );
                this.getTime = result.getInt( "get_time" );
                this.code = result.getInt( "code" );
                this.point = result.getInt( "point" );
                this.pointKind = result.getInt( "point_kind" );
                this.extCode = result.getInt( "ext_code" );
                this.extString = result.getString( "ext_string" );
                this.personCode = result.getString( "person_code" );
                this.appendReason = result.getString( "append_reason" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPoint.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザポイントデータ情報データ追加
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

        query = "INSERT hh_user_point SET ";
        query = query + " user_id = ?,";
        query = query + " seq = 0,";
        query = query + " get_date = ?,";
        query = query + " get_time = ?,";
        query = query + " code = ?,";
        query = query + " point = ?,";
        query = query + " point_kind = ?,";
        query = query + " ext_code = ?,";
        query = query + " ext_string = ?,";
        query = query + " person_code = ?,";
        query = query + " append_reason = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.getDate );
            prestate.setInt( 3, this.getTime );
            prestate.setInt( 4, this.code );
            prestate.setInt( 5, this.point );
            prestate.setInt( 6, this.pointKind );
            prestate.setInt( 7, this.extCode );
            prestate.setString( 8, this.extString );
            prestate.setString( 9, this.personCode );
            prestate.setString( 10, this.appendReason );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPoint.insertData] Exception=" + e.toString() );
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

        query = "UPDATE hh_user_point SET ";
        query = query + " get_date = ?,";
        query = query + " get_time = ?,";
        query = query + " code = ?,";
        query = query + " point = ?,";
        query = query + " point_kind = ?,";
        query = query + " ext_code = ?,";
        query = query + " ext_string = ?,";
        query = query + " person_code = ?,";
        query = query + " append_reason = ?";
        query = query + " WHERE user_id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.getDate );
            prestate.setInt( 2, this.getTime );
            prestate.setInt( 3, this.code );
            prestate.setInt( 4, this.point );
            prestate.setInt( 5, this.pointKind );
            prestate.setInt( 6, this.extCode );
            prestate.setString( 7, this.extString );
            prestate.setString( 8, this.personCode );
            prestate.setString( 9, this.appendReason );
            prestate.setString( 10, userId );
            prestate.setInt( 11, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPoint.updateData] Exception=" + e.toString() );
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
