/*
 * 日別料金クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvDayCharge implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 6437297376936061681L;

    private int               iD;
    private int               planId;
    private int               calDate;
    private int               chargeModeId;
    private String            hotelId;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データの初期化
     */
    public DataRsvDayCharge()
    {
        iD = 0;
        planId = 0;
        calDate = 0;
        chargeModeId = 0;
        hotelId = "";
        userId = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    // getter
    public int getId()
    {
        return this.iD;
    }

    public int getPlanId()
    {
        return this.planId;
    }

    public int getCalDate()
    {
        return this.calDate;
    }

    public int getChargeModeId()
    {
        return this.chargeModeId;
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
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public void setCalDate(int calDate)
    {
        this.calDate = calDate;
    }

    public void setChargeModeId(int chargeModeId)
    {
        this.chargeModeId = chargeModeId;
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
     * 日別料金情報取得
     *
     * @param iD ホテルID
     * @param planId プランID
     * @param calDate 日付
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int planId, int calDate)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, plan_id, cal_date, charge_mode_id, " +
                " hotel_id, user_id, last_update, last_uptime " +
                " FROM hh_rsv_day_charge WHERE id = ? AND plan_id = ? AND cal_date = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, calDate );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.planId = result.getInt( "plan_id" );
                    this.calDate = result.getInt( "cal_date" );
                    this.chargeModeId = result.getInt( "charge_mode_id" );
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
            Logging.error( "[DataRsvDayCharge.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
