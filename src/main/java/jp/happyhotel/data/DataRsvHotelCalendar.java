/*
 * ホテルカレンダクラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvHotelCalendar implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 2329233224148618038L;

    private int               iD;
    private int               calDate;
    private int               chargeModeId;
    private int               week;
    private int               holidayKind;
    private String            holidayName;
    private int               sales_flag;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データの初期化
     */
    public DataRsvHotelCalendar()
    {
        iD = 0;
        calDate = 0;
        chargeModeId = 0;
        week = 0;
        holidayKind = 0;
        holidayName = "";
        userId = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    // getter
    public int getId()
    {
        return this.iD;
    }

    public int getCalDate()
    {
        return this.calDate;
    }

    public int getChargeModeId()
    {
        return this.chargeModeId;
    }

    public int getWeek()
    {
        return this.week;
    }

    public int getHolidayKind()
    {
        return this.holidayKind;
    }

    public String getHolidayName()
    {
        return this.holidayName;
    }

    public int getUserId()
    {
        return this.userId;
    }

    public int getLastUpdate()
    {
        return this.lastUpdate;
    }

    public int getLastUptime()
    {
        return this.lastUptime;
    }

    // setter
    public void setId(int iD)
    {
        this.iD = iD;
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

    public void setHolidayName(String holidayName)
    {
        this.holidayName = holidayName;
    }

    public void steUserId(int userId)
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

    /**
     * ホテルカレンダー情報取得
     * 
     * @param iD ホテルID
     * @param calDate 日付
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int calDate)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, cal_date, charge_mode_id, week, holiday_kind, " +
                " holiday_name, user_id, last_update, last_uptime, sales_flag " +
                " FROM hh_rsv_hotel_calendar WHERE id = ? AND cal_date = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, calDate );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.calDate = result.getInt( "cal_date" );
                    this.chargeModeId = result.getInt( "charge_mode_id" );
                    this.week = result.getInt( "week" );
                    this.holidayKind = result.getInt( "holiday_kind" );
                    this.holidayName = result.getString( "holiday_name" );
                    this.userId = result.getInt( "user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.sales_flag = result.getInt( "sales_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvHotelCalendar.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルカレンダー情報取得
     * 
     * @param iD ホテルID
     * @param calDate 日付
     * @param chargeModeId 料金モードID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int calDate, int chargeModeId)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, cal_date, charge_mode_id, week, holiday_kind, " +
                " holiday_name, user_id, last_update, last_uptime, sales_flag " +
                " FROM hh_rsv_hotel_calendar WHERE id = ? AND cal_date = ? AND price_mode_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, calDate );
            prestate.setInt( 3, chargeModeId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.calDate = result.getInt( "cal_date" );
                    this.chargeModeId = result.getInt( "charge_mode_id" );
                    this.week = result.getInt( "week" );
                    this.holidayKind = result.getInt( "holiday_kind" );
                    this.holidayName = result.getString( "holiday_name" );
                    this.userId = result.getInt( "user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.sales_flag = result.getInt( "sales_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvHotelCalendar.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    public void setSales_flag(int sales_flag)
    {
        this.sales_flag = sales_flag;
    }

    public int getSales_flag()
    {
        return sales_flag;
    }

}
