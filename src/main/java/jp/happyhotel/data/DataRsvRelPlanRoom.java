/*
 * プラン・部屋設定データ
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvRelPlanRoom implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 4688756010219452032L;

    private int               iD;
    private int               planId;
    private int               seq;
    private int               salesStartDate;
    private int               salesEndDate;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データの初期化
     */
    public DataRsvRelPlanRoom()
    {
        iD = 0;
        planId = 0;
        seq = 0;
        salesStartDate = 0;
        salesEndDate = 0;
        userId = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    // getter
    public int getID()
    {
        return this.iD;
    }

    public int getPlanId()
    {
        return this.planId;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public int getSalesStartDate()
    {
        return this.salesStartDate;
    }

    public int getSalesEndDate()
    {
        return this.salesEndDate;
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

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setSalesStartDate(int salesStartDate)
    {
        this.salesStartDate = salesStartDate;
    }

    public void setSalesEndDate(int salesEndDate)
    {
        this.salesEndDate = salesEndDate;
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

    /**
     * プラン・部屋設定データ情報取得
     *
     * @param iD ホテルID
     * @param planId プランID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int planId, int seq)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, plan_id, seq, sales_start_date, " +
                " sales_end_date, user_id, last_update, last_uptime " +
                " FROM hh_rsv_rel_plan_room WHERE id = ? AND plan_id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.planId = result.getInt( "plan_id" );
                    this.seq = result.getInt( "seq" );
                    this.salesStartDate = result.getInt( "sales_start_date" );
                    this.salesEndDate = result.getInt( "sales_end_date" );
                    this.userId = result.getInt( "user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRelPlanRoom.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * プラン・部屋設定データ登録
     *
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection conn)
    {
        int result;
        boolean ret;
        String query;
        // Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_rel_plan_room SET " +
                "  id = ?" +
                ", plan_id = ?" +
                ", seq = ?" +
                ", sales_start_date = ? " +
                ", sales_end_date = ? " +
                ", user_id = ? " +
                ", last_update = ? " +
                ", last_uptime = ? ";

        try
        {
            // connection = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.iD );
            prestate.setInt( 2, this.planId );
            prestate.setInt( 3, this.seq );
            prestate.setInt( 4, this.salesStartDate );
            prestate.setInt( 5, this.salesEndDate );
            prestate.setInt( 6, this.userId );
            prestate.setInt( 7, this.lastUpdate );
            prestate.setInt( 8, this.lastUptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRelPlanRoom.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
        return(ret);
    }
}
