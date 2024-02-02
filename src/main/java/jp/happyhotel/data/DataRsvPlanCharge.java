/*
 * プラン別料金クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvPlanCharge implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -6308118082952585142L;

    private int               iD;
    private int               planId;
    private int               chargeModeId;
    private int               ciTimeFrom;
    private int               ciTimeTo;
    private int               coTime;
    private String            coRemarks;
    private int               adultTwoCharge;
    private int               adultOneCharge;
    private int               adultAddCharge;
    private int               childAddCharge;
    private String            remarks;
    private String            hotelId;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;
    private int               weekstatus;
    private int               coKind;

    /**
     * データの初期化
     */
    public DataRsvPlanCharge()
    {
        iD = 0;
        planId = 0;
        chargeModeId = 0;
        ciTimeFrom = 0;
        ciTimeTo = 0;
        coTime = 0;
        coRemarks = "";
        adultTwoCharge = 0;
        adultOneCharge = 0;
        adultAddCharge = 0;
        childAddCharge = 0;
        remarks = "";
        hotelId = "";
        userId = 0;
        lastUpdate = 0;
        lastUptime = 0;
        setWeekstatus( 0 );
        coKind = 0;
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

    public int getChargeModeId()
    {
        return this.chargeModeId;
    }

    public int getCiTimeFrom()
    {
        return this.ciTimeFrom;
    }

    public int getCiTimeTo()
    {
        return this.ciTimeTo;
    }

    public int getCoTime()
    {
        return this.coTime;
    }

    public String getCoRemarks()
    {
        return this.coRemarks;
    }

    public int getAdultTwoCharge()
    {
        return this.adultTwoCharge;
    }

    public int getAdultOneCharge()
    {
        return this.adultOneCharge;
    }

    public int getAdultAddCharge()
    {
        return this.adultAddCharge;
    }

    public int getChildAddCharge()
    {
        return this.childAddCharge;
    }

    public String getRemarks()
    {
        return this.remarks;
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

    public int getWeekstatus()
    {
        return weekstatus;
    }

    public int getCoKind()
    {
        return coKind;
    }

    /**
     * 
     * setter
     * 
     */
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public void setChargeModeId(int chargeModeId)
    {
        this.chargeModeId = chargeModeId;
    }

    public void setCiTimeFrom(int ciTimeFrom)
    {
        this.ciTimeFrom = ciTimeFrom;
    }

    public void setCiTimeTo(int ciTimeTo)
    {
        this.ciTimeTo = ciTimeTo;
    }

    public void setCoTime(int coTime)
    {
        this.coTime = coTime;
    }

    public void setCoRemarks(String coRemarks)
    {
        this.coRemarks = coRemarks;
    }

    public void setAdultTwoCharge(int adultTwoCharge)
    {
        this.adultTwoCharge = adultTwoCharge;
    }

    public void setAdultOneCharge(int adultOneCharge)
    {
        this.adultOneCharge = adultOneCharge;
    }

    public void setAdultAddCharge(int adultAddCharge)
    {
        this.adultAddCharge = adultAddCharge;
    }

    public void setChildAddCharge(int childAddCharge)
    {
        this.childAddCharge = childAddCharge;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
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

    public void setWeekstatus(int weekstatus)
    {
        this.weekstatus = weekstatus;
    }

    public void setCoKind(int coKind)
    {
        this.coKind = coKind;
    }

    /**
     * プラン別料金情報取得
     * 
     * @param iD ホテルID
     * @param planId プランID
     * @param chargeModeId 料金モードID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int planId, int chargeModeId)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, plan_id, charge_mode_id, ci_time_from, ci_time_to," +
                " co_time, co_remarks, adult_two_charge, adult_one_charge," +
                " adult_add_charge, child_add_charge, remarks, hotel_id, user_id, last_update, last_uptime, week_status, co_kind " +
                " FROM hh_rsv_plan_charge WHERE id = ? AND plan_id = ? AND charge_mode_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, chargeModeId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.planId = result.getInt( "plan_id" );
                    this.chargeModeId = result.getInt( "charge_mode_id" );
                    this.ciTimeFrom = result.getInt( "ci_time_from" );
                    this.ciTimeTo = result.getInt( "ci_time_to" );
                    this.coTime = result.getInt( "co_time" );
                    this.coRemarks = CheckString.checkStringForNull( result.getString( "co_remarks" ) );
                    this.adultTwoCharge = result.getInt( "adult_two_charge" );
                    this.adultOneCharge = result.getInt( "adult_one_charge" );
                    this.adultAddCharge = result.getInt( "adult_add_charge" );
                    this.childAddCharge = result.getInt( "child_add_charge" );
                    this.remarks = CheckString.checkStringForNull( result.getString( "remarks" ) );
                    this.hotelId = result.getString( "hotel_id" );
                    this.userId = result.getInt( "user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.weekstatus = result.getInt( "week_status" );
                    this.coKind = result.getInt( "co_kind" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvPlanCharge.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * プラン別料金情報取得
     * 
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_plan_charge SET " +
                "  id = ?" +
                ", plan_id = ?" +
                ", charge_mode_id = ?" +
                ", ci_time_from = ? " +
                ", ci_time_to = ? " +
                ", co_time = ? " +
                ", co_remarks = ? " +
                ", adult_two_charge = ? " +
                ", adult_one_charge = ? " +
                ", adult_add_charge = ? " +
                ", child_add_charge = ? " +
                ", remarks = ? " +
                ", hotel_id = ? " +
                ", user_id = ? " +
                ", last_update = ? " +
                ", last_uptime = ? " +
                ", week_status = ? " +
                ", co_kind = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.iD );
            prestate.setInt( 2, this.planId );
            prestate.setInt( 3, this.chargeModeId );
            prestate.setInt( 4, this.ciTimeFrom );
            prestate.setInt( 5, this.ciTimeTo );
            prestate.setInt( 6, this.coTime );
            prestate.setString( 7, this.coRemarks );
            prestate.setInt( 8, this.adultTwoCharge );
            prestate.setInt( 9, this.adultOneCharge );
            prestate.setInt( 10, this.adultAddCharge );
            prestate.setInt( 11, this.childAddCharge );
            prestate.setString( 12, this.remarks );
            prestate.setString( 13, this.hotelId );
            prestate.setInt( 14, this.userId );
            prestate.setInt( 15, this.lastUpdate );
            prestate.setInt( 16, this.lastUptime );
            prestate.setInt( 17, this.weekstatus );
            prestate.setInt( 18, this.coKind );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRelPlanOption.insertData] Exception=" + e.toString() );
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
