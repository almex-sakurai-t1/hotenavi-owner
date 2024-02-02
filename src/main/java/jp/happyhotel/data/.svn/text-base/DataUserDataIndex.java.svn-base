package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Apr 15 14:22:39 JST 2011
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザ管理番号
 * 
 * @author tashiro-s1
 * @version 1.0
 */
public class DataUserDataIndex implements Serializable
{

    private static final long serialVersionUID = 7474273818706973415L;
    private String            userId;
    private int               id;
    private int               userSeq;
    private int               registDate;
    private int               registTime;

    public DataUserDataIndex()
    {
        userId = "";
        id = 0;
        userSeq = 0;
        registDate = 0;
        registTime = 0;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getHotelId()
    {
        return id;
    }

    public int getUserSeq()
    {
        return userSeq;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setHotelId(int hotelId)
    {
        this.id = hotelId;
    }

    public void setUserSeq(int userSeq)
    {
        this.userSeq = userSeq;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    /****
     * ユーザ管理番号データ取得
     * 
     * @param kind
     * @param id
     * @return
     */
    public boolean getData(String userId, int id)
    {

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_data_index WHERE user_id = ? AND id = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.id = result.getInt( "id" );
                    this.userSeq = result.getInt( "user_seq" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            Logging.info( "[DataUserDataIndex.getData] count=" + count );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDataIndex.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ユーザ管理番号データ設定
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
                this.id = result.getInt( "id" );
                this.userSeq = result.getInt( "user_seq" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDataIndex.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザ管理番号データ挿入
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

        query = "INSERT hh_user_data_index SET ";
        query += " user_id = ?,";
        query += " id = ?,";
        query += " user_seq = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.id );
            prestate.setInt( 3, this.userSeq );
            prestate.setInt( 4, this.registDate );
            prestate.setInt( 5, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDataIndex.insertData] Exception=" + e.toString() );
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
     * ユーザ管理番号データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param kind　区分
     * @param id　ホテルID
     * @return
     */
    public boolean updateData(int userId, int id)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_data_index SET ";
        query += " user_seq = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query = query + " WHERE user_id = ? AND id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.userSeq );
            prestate.setInt( 2, this.registDate );
            prestate.setInt( 3, this.registTime );
            prestate.setInt( 4, userId );
            prestate.setInt( 5, id );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserDataIndex.updateData] Exception=" + e.toString() );
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
