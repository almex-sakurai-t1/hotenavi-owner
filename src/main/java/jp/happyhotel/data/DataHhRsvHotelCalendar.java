package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテルカレンダーマスタ取得クラス
 * 
 * @author Techno
 * @version 1.00 2015/3/10
 */
public class DataHhRsvHotelCalendar implements Serializable
{
    /**
     * 
     */
    private static final long  serialVersionUID = -4562675214816088545L;
    public static final String TABLE            = "hh_rsv_hotel_calendar";
    /** ホテルID */
    private int                id;
    /** 日付 */
    private int                calDate;
    /** 料金モードID */
    private int                chargeModeId;
    /** 曜日: 0：日曜～6：土曜 */
    private int                week;
    /** 休日区分（祝祭日）: 0：通常、1：祝祭日 */
    private int                holidayKind;
    /** オーナーホテルID: (owner_user)ホテルID */
    private String             hotelId;
    /** ユーザID: (owner_user)ユーザーID */
    private int                userId;
    /** 最終更新日: YYYYMMDD */
    private int                lastUpdate;
    /** 最終更新時刻: HHMMSS */
    private int                lastUptime;

    /**
     * データを初期化します。
     */
    public DataHhRsvHotelCalendar()
    {
        this.id = 0;
        this.calDate = 0;
        this.chargeModeId = 0;
        this.week = 0;
        this.holidayKind = 0;
        this.hotelId = "";
        this.userId = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getCalDate()
    {
        return calDate;
    }

    public int getChargeModeId()
    {
        return chargeModeId;
    }

    public int getWeek()
    {
        return week;
    }

    public int getHolidayKind()
    {
        return holidayKind;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public int getUserId()
    {
        return userId;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCalDate(int calDate)
    {
        this.calDate = calDate;
    }

    public void setChargeModeId(int chargeModeId)
    {
        this.chargeModeId = chargeModeId;
    }

    public void setWeek(int week)
    {
        this.week = week;
    }

    public void setHolidayKind(int holidayKind)
    {
        this.holidayKind = holidayKind;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /****
     * ホテルカレンダーマスタ取得
     * 
     * @param id ホテルID
     * @param calDate 日付
     * @return
     */
    public boolean getData(Connection connection, int id, int calDate)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM newRsvDB.hh_rsv_hotel_calendar WHERE id = ? AND cal_date = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, calDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvHotelCalendar.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * データの存在チェック
     * 
     * @param connection
     * @param id
     * @param calDate
     * @return
     */
    public boolean isExistData(Connection connection, int id, int calDate)
    {
        boolean ret = false;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query;
        query = "SELECT COUNT(*) AS cnt FROM newRsvDB.hh_rsv_hotel_calendar WHERE id = ? AND cal_date = ? ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, calDate );
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
            Logging.error( "[DataHhRsvHotelCalendar.isExistData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);

    }

    /**
     * ホテルカレンダーマスタ設定
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
                this.id = result.getInt( "id" );
                this.calDate = result.getInt( "cal_date" );
                this.chargeModeId = result.getInt( "charge_mode_id" );
                this.week = result.getInt( "week" );
                this.holidayKind = result.getInt( "holiday_kind" );
                this.hotelId = result.getString( "hotel_id" );
                this.userId = result.getInt( "user_id" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvHotelCalendar.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ホテルカレンダーマスタ挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        boolean ret;
        Connection connection = null;
        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            ret = insertData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvHotelCalendar.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public boolean insertData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT newRsvDB.hh_rsv_hotel_calendar SET ";
        query += " id=?";
        query += ", cal_date=?";
        query += ", charge_mode_id=?";
        query += ", week=?";
        query += ", holiday_kind=?";
        query += ", hotel_id=?";
        query += ", user_id=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.calDate );
            prestate.setInt( i++, this.chargeModeId );
            prestate.setInt( i++, this.week );
            prestate.setInt( i++, this.holidayKind );
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.userId );
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
            Logging.error( "[DataHhRsvHotelCalendar.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * ホテルカレンダーマスタ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param calDate 日付
     * @return
     */
    public boolean updateData(int id, int calDate)
    {
        boolean ret;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            ret = updateData( connection, id, calDate );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvHotelCalendar.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public boolean updateData(Connection connection, int id, int calDate)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_hotel_calendar SET ";
        query += " charge_mode_id=?";
        query += ", week=?";
        query += ", holiday_kind=?";
        query += ", hotel_id=?";
        query += ", user_id=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE id=? AND cal_date=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.chargeModeId );
            prestate.setInt( i++, this.week );
            prestate.setInt( i++, this.holidayKind );
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.userId );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.calDate );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvHotelCalendar.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * ホテルで登録されているカレンダーの最大値を取得
     * 
     * @param connection
     * @param id
     * @return
     */
    public int getMaxCalDate(Connection connection, int id)
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query;
        query = "SELECT MAX(cal_date) AS max_cal_date FROM newRsvDB.hh_rsv_hotel_calendar WHERE id = ?  ";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null && result.next() )
            {
                return result.getInt( "max_cal_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvHotelCalendar.isExistData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return 0;
    }

    /**
     * 指定された料金モードIDのカレンダーを取得
     * 
     * @param connection
     * @param id
     * @return
     */
    public boolean getCalCharge(Connection connection, ArrayList<Integer> chargeModeIdList)
    {
        boolean ret = false;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query;
        String cond = "";
        int index = 1;

        for( int i = 0 ; i < chargeModeIdList.size() ; i++ )
        {
            if ( i != 0 )
            {
                cond = " ,";
            }
            cond = " ? ";
        }

        query = "SELECT cal_date, charge_mode_id FROM newRsvDB.hh_rsv_hotel_calendar WHERE id = ? charge_mode_id in (" + cond + ")";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( index, id );
            for( int i = 0 ; i < chargeModeIdList.size() ; i++ )
            {
                prestate.setInt( index, chargeModeIdList.get( i ) );
                index++;
            }

            result = prestate.executeQuery();
            if ( result != null && result.next() )
            {
                // return result.getInt( "max_cal_date" );
            }

            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvHotelCalendar.isExistData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return ret;
    }
}
