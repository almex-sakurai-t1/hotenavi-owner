package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 取得クラス
 * 
 * @author Mitsuhashi
 * @version 1.00 2017/4/12
 */
public class DataHhRsvCancelpattern implements Serializable
{
    public static final String TABLE = "hh_rsv_cancelpattern";
    private int                cancelId;                      // キャンセルID
    private int                seq;                           // キャンセルID明細
    private int                dayFrom;                       // 日from
    private int                dayTo;                         // 日to
    private int                per;                           // 割合
    private String             hotelId;                       // オーナーホテルID: (owner_user)ホテルID
    private int                userId;                        // ユーザID: (owner_user)ユーザーID
    private int                registDate;                    // 作成日: YYYYMMDD
    private int                registTime;                    // 作成時刻: HHMMSS
    private int                lastUpdate;                    // 最終更新日: YYYYMMDD
    private int                lastUptime;                    // 最終更新時刻: HHMMSS

    /**
     * データを初期化します。
     */
    public DataHhRsvCancelpattern()
    {
        this.cancelId = 0;
        this.seq = 0;
        this.dayFrom = 0;
        this.dayTo = 0;
        this.per = 0;
        this.hotelId = "";
        this.userId = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;

    }

    public int getCancelId()
    {
        return cancelId;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getDayFrom()
    {
        return dayFrom;
    }

    public int getDayTo()
    {
        return dayTo;
    }

    public int getPer()
    {
        return per;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public int getUserId()
    {
        return userId;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getregistTime()
    {
        return registTime;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setCancelId(int cancelId)
    {
        this.cancelId = cancelId;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setDayFrom(int dayFrom)
    {
        this.dayFrom = dayFrom;
    }

    public void setDayTo(int dayTo)
    {
        this.dayTo = dayTo;
    }

    public void setPer(int per)
    {
        this.per = per;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public void setRegistdate(int registDdate)
    {
        this.registDate = registDdate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /**
     * 取得
     * 
     * @param cancelId キャンセルID
     * @param seq キャンセルID明細
     * @return
     */
    public boolean getData(int cancelId, int seq)
    {
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            return this.getData( connection, cancelId, seq );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCancelpattern.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 取得
     * 
     * @param cancelId キャンセルID
     * @param seq キャンセルID明細
     * @return
     */
    public boolean getData(Connection connection, int cancelId, int seq)
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "SELECT * FROM newRsvDB.hh_rsv_cancelpattern WHERE cancel_id = ? AND seq = ?";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, cancelId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }
            return this.setData( result );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCancelpattern.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, null );
        }
    }

    /**
     * 設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.cancelId = result.getInt( "cancel_id" );
                this.seq = result.getInt( "seq" );
                this.dayFrom = result.getInt( "day_from" );
                this.dayTo = result.getInt( "day_to" );
                this.per = result.getInt( "per" );
                this.hotelId = result.getString( "hotel_id" );
                this.userId = result.getInt( "user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCancelpattern.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setDataPolicy(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.cancelId = result.getInt( "cancel_id" );
                this.seq = result.getInt( "seq" );
                this.dayFrom = result.getInt( "day_from" );
                this.dayTo = result.getInt( "day_to" );
                this.per = result.getInt( "per" );
                this.hotelId = result.getString( "hotel_id" );
                this.userId = result.getInt( "user_id" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCancelpattern.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        boolean ret = false;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            ret = insertData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCancelpattern.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return
     */
    public boolean updateData()
    {
        boolean ret = false;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            ret = updateData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCancelpattern.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 更新(複数テーブル用)
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return
     */

    public boolean updateData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_cancelpattern SET ";
        query += ", day_from=?";
        query += ", day_to=?";
        query += ", per=?";
        query += ", hotel_id=?";
        query += ", user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE cancel_id=?";
        query += "  AND seq=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.dayFrom );
            prestate.setInt( i++, this.dayTo );
            prestate.setInt( i++, this.per );
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.userId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.cancelId );
            prestate.setInt( i++, this.seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCancelpattern.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param Connection connection
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "INSERT newRsvDB.hh_rsv_cancelpattern SET ";
        query += "  cancel_id=?";
        query += "  seq=?";
        query += ", day_from=?";
        query += ", day_to=?";
        query += ", per=?";
        query += ", hotel_id=?";
        query += ", user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE cancel_id=?";

        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.cancelId );
            prestate.setInt( i++, this.seq );
            prestate.setInt( i++, this.dayFrom );
            prestate.setInt( i++, this.dayTo );
            prestate.setInt( i++, this.per );
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.userId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvCancelpattern.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * キャンセルパターン一覧を取得する
     * 
     * @return
     * @throws Exception
     */
    public static ArrayList<Integer> getCancelPatterns() throws Exception
    {
        ArrayList<Integer> cancelId = new ArrayList<Integer>();

        StringBuilder query = new StringBuilder();
        query.append( " SELECT cancel_id " );
        query.append( " FROM newRsvDB.hh_rsv_cancelpattern " );
        query.append( " GROUP BY cancel_id  " );
        query.append( " ORDER BY cancel_id " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cancelId.add( result.getInt( 1 ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerRsvCommon.getCancelPatterns] Exception=" + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return cancelId;

    }
}
