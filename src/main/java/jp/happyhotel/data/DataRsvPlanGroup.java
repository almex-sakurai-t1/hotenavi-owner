/*
 * プラングループクラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvPlanGroup implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 6192864641774491927L;

    private int               iD;
    private int               planGroupId;
    private String            planGroupName;
    private int               dispIndex;
    private String            hotelId;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データの初期化
     */
    public DataRsvPlanGroup()
    {
        iD = 0;
        planGroupId = 0;
        planGroupName = "";
        dispIndex = 0;
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

    public int getPlanGroupId()
    {
        return this.planGroupId;
    }

    public String getPlanGroupName()
    {
        return this.planGroupName;
    }

    public int getDispIndex()
    {
        return this.dispIndex;
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

    /**
     *
     * setter
     *
     */
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setPlanGroupId(int planGroupId)
    {
        this.planGroupId = planGroupId;
    }

    public void setPlanGroupName(String planGroupName)
    {
        this.planGroupName = planGroupName;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
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
     * プラングループ情報取得
     *
     * @param iD ホテルID
     * @param planGroupId プラングループID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int planGroupId)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, plan_group_id, plan_group_name, disp_index," +
                " hotel_id, user_id, last_update, last_uptime " +
                " FROM hh_rsv_plan_charge WHERE id = ? AND plan_group_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, planGroupId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.planGroupId = result.getInt( "plan_group_id" );
                    this.planGroupName = result.getString( "plan_group_name" );
                    this.dispIndex = result.getInt( "disp_index" );
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
            Logging.error( "[DataRsvPlanGroup.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
