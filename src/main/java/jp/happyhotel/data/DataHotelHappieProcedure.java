package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri May 27 11:21:46 JST 2011
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ハピー手続きデータ
 * 
 */
public class DataHotelHappieProcedure implements Serializable
{
    private static final long serialVersionUID = -6541367843571347450L;

    private int               id;
    private int               seq;
    private int               registStatus;
    private String            userId;
    private int               userSeq;
    private int               registDate;
    private int               registTime;

    public DataHotelHappieProcedure()
    {
        id = 0;
        seq = 0;
        registStatus = 0;
        userId = "";
        userSeq = 0;
        registDate = 0;
        registTime = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getRegistStatus()
    {
        return registStatus;
    }

    public String getUserId()
    {
        return userId;
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

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setRegistStatus(int registStatus)
    {
        this.registStatus = registStatus;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
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
    public boolean getData(int hotelId, int seq)
    {

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_happie_procedure WHERE id = ? AND seq = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.registStatus = result.getInt( "regist_status" );
                    this.userId = result.getString( "user_id" );
                    this.userSeq = result.getInt( "user_seq" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            Logging.error( "[DataHotelHappieProcedure.getData] count=" + count );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHappieProcedure.getData] Exception=" + e.toString() );
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
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.registStatus = result.getInt( "regist_status" );
                this.userId = result.getString( "user_id" );
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

        query = "INSERT hh_hotel_happie_procedure SET ";
        query += " id = ?,";
        query += " seq = 0,";
        query += " regist_status = ?,";
        query += " user_id = ?,";
        query += " user_seq = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.registStatus );
            prestate.setString( 3, this.userId );
            prestate.setInt( 4, this.userSeq );
            prestate.setInt( 5, this.registDate );
            prestate.setInt( 6, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                this.seq = getMaxSeq( connection );
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

        query = "UPDATE hh_hotel_happie_procedure SET ";
        query += " regist_status = ?,";
        query += " user_id = ?,";
        query += " user_seq = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query = query + " WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.registStatus );
            prestate.setString( 2, this.userId );
            prestate.setInt( 3, this.userSeq );
            prestate.setInt( 4, this.registDate );
            prestate.setInt( 5, this.registTime );
            prestate.setInt( 6, id );
            prestate.setInt( 7, seq );

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

    /****
     * 最大の問い合わせ暗号を取得する（ユーザごと）
     * 
     * @param kind
     * @param id
     * @return
     */
    public int getMaxSeq(int hotelId, String userId)
    {
        int maxSeq;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT MAX(seq) FROM hh_hotel_happie_procedure WHERE id = ? AND user_id = ?";

        maxSeq = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, userId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    maxSeq = result.getInt( 1 );
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHappieProcedure.getData] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(maxSeq);
    }

    /****
     * 最大の問い合わせ番号を取得する
     * 
     * @return maxSeq
     */
    public int getMaxSeq(Connection connection)
    {
        int maxSeq;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT @LAST_INSERT_ID";
        maxSeq = 0;
        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    maxSeq = result.getInt( 1 );
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHappieProcedure.getMaxSeq] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(maxSeq);
    }

}
