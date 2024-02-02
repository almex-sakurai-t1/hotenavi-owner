/*
 * 基本カレンダークラス
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

public class DataRsvCalendar implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 2937392757582972696L;

    private int               calDate;
    private int               chargeModeId;
    private int               week;
    private int               holidayKind;
    private String            holidayName;
    private String            hotelId;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データの初期化
     */
    public DataRsvCalendar()
    {
        calDate = 0;
        chargeModeId = 0;
        week = 0;
        holidayKind = 0;
        holidayName = "";
        hotelId = "";
        userId = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    // getter
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

    public String getHotelId()
    {
        return this.hotelId;
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

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
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
     * 基本カレンダー情報取得
     *
     * @param calDate 日付
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int calDate)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT cal_date, charge_mode_id, week, holiday_kind, holiday_name, " +
                " hotel_id, user_id, last_update, last_uptime " +
                " FROM hh_rsv_calendar WHERE cal_date = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, calDate );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.calDate = result.getInt( "cal_date" );
                    this.chargeModeId = result.getInt( "charge_mode_id" );
                    this.week = result.getInt( "week" );
                    this.holidayKind = result.getInt( "holiday_kind" );
                    this.holidayName = result.getString( "holiday_name" );
                    this.hotelId = result.getString( "hotel_id" );
                    this.userId = result.getInt( "user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvCalendar.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 基本カレンダー情報取得
     *
     * @param result 基本カレンダー情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.calDate = result.getInt( "cal_date" );
                this.chargeModeId = result.getInt( "charge_mode_id" );
                this.week = result.getInt( "week" );
                this.holidayKind = result.getInt( "holiday_kind" );
                this.holidayName = result.getString( "holiday_name" );
                this.hotelId = result.getString( "hotel_id" );
                this.userId = result.getInt( "user_id" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserApply.setData] Exception=" + e.toString() );
        }
        return(true);
    }
}
