/*
 * 予約基本データクラス
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

public class DataRsvReserveBasic implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1301982339239965148L;

    private int               iD;
    private String            reserve_pr;
    private String            child_charge;
    private int               deadline_time;
    private int               cashDeposit;
    private int               parking;
    private int               parkingCount;
    private String            cancelPolicy;
    private int               salesFlag;
    private String            hotelId;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;
    private int               noshow_credit_flag;
    private int               preOpenFlag;
    private int               equipDispFlag;

    /**
     * データの初期化
     */
    public DataRsvReserveBasic()
    {
        iD = 0;
        reserve_pr = "";
        child_charge = "";
        cashDeposit = 0;
        parking = 0;
        parkingCount = 0;
        cancelPolicy = null;
        salesFlag = 0;
        hotelId = "";
        userId = 0;
        lastUpdate = 0;
        lastUptime = 0;
        noshow_credit_flag = 0;
        preOpenFlag = 0;
        equipDispFlag = 0;
    }

    // getter
    public int getID()
    {
        return this.iD;
    }

    public int getDeadline_time()
    {
        return deadline_time;
    }

    public String getReservePr()
    {
        return this.reserve_pr;
    }

    public int getCashDeposit()
    {
        return this.cashDeposit;
    }

    public int getParking()
    {
        return this.parking;
    }

    public int getParkingCount()
    {
        return this.parkingCount;
    }

    public String getCancelPolicy()
    {
        return this.cancelPolicy;
    }

    public int getSalesFlag()
    {
        return this.salesFlag;
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

    public String getChild_charge()
    {
        return child_charge;
    }

    public int getNoshow_credit_flag()
    {
        return noshow_credit_flag;
    }

    public int getPreOpenFlag()
    {
        return preOpenFlag;
    }

    public int getEquipDispFlag()
    {
        return equipDispFlag;
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

    public void setDeadline_time(int deadline_time)
    {
        this.deadline_time = deadline_time;
    }

    public void setReservePr(String reservePr)
    {
        this.reserve_pr = reservePr;
    }

    public void setCashDeposit(int cashDeposit)
    {
        this.cashDeposit = cashDeposit;
    }

    public void setParking(int parking)
    {
        this.parking = parking;
    }

    public void setParkingCount(int parkingCount)
    {
        this.parkingCount = parkingCount;
    }

    public void setCancelPolicy(String cancelPolicy)
    {
        this.cancelPolicy = cancelPolicy;
    }

    public void setSalesFlag(int salesFlag)
    {
        this.salesFlag = salesFlag;
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

    public void setChild_charge(String child_charge)
    {
        this.child_charge = child_charge;
    }

    public void setNoshow_credit_flag(int noshow_credit_flag)
    {
        this.noshow_credit_flag = noshow_credit_flag;
    }

    public int setPreOpenFlag(int preOpenFlag)
    {
        return preOpenFlag;
    }

    public void setEquipDispFlag(int equipDispFlag)
    {
        this.equipDispFlag = equipDispFlag;
    }

    /**
     * 予約基本データ情報取得
     * 
     * @param iD ホテルID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, deadline_time, reserve_pr, cash_deposit, parking, parking_count, " +
                " cancel_policy, sales_flag, hotel_id, user_id, child_charge_info, noshow_credit_flag, last_update, last_uptime, pre_open_flag, equip_disp_flag " +
                " FROM hh_rsv_reserve_basic WHERE id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.deadline_time = result.getInt( "deadline_time" );
                    this.reserve_pr = CheckString.checkStringForNull( result.getString( "reserve_pr" ) );
                    this.cashDeposit = result.getInt( "cash_deposit" );
                    this.parking = result.getInt( "parking" );
                    this.parkingCount = result.getInt( "parking_count" );
                    this.cancelPolicy = CheckString.checkStringForNull( result.getString( "cancel_policy" ) );
                    this.salesFlag = result.getInt( "sales_flag" );
                    this.hotelId = result.getString( "hotel_id" );
                    this.userId = result.getInt( "user_id" );
                    this.child_charge = result.getString( "child_charge_info" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.equipDispFlag = result.getInt( "equip_disp_flag" );
                    this.noshow_credit_flag = result.getInt( "noshow_credit_flag" );
                    this.preOpenFlag = result.getInt( "pre_open_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserveBasic.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 予約基本データ情報取得
     * 
     * @param result 予約基本情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.iD = result.getInt( "id" );
                this.deadline_time = result.getInt( "deadline_time" );
                this.reserve_pr = CheckString.checkStringForNull( result.getString( "reserve_pr" ) );
                this.cashDeposit = result.getInt( "cash_deposit" );
                this.parking = result.getInt( "parking" );
                this.parkingCount = result.getInt( "parking_count" );
                this.cancelPolicy = CheckString.checkStringForNull( result.getString( "cancel_policy" ) );
                this.salesFlag = result.getInt( "sales_flag" );
                this.hotelId = result.getString( "hotel_id" );
                this.userId = result.getInt( "user_id" );
                this.child_charge = result.getString( "child_charge_info" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.equipDispFlag = result.getInt( "equip_disp_flag" );
                this.noshow_credit_flag = result.getInt( "noshow_credit_flag" );
                this.preOpenFlag = result.getInt( "pre_open_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserveBasic.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 予約基本データ登録
     * 
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

        query = "INSERT INTO hh_rsv_reserve_basic SET " +
                "  id = ?" +
                ", deadline_time = ?" +
                ", reserve_pr = ?" +
                ", ci_info = ? " +
                ", co_info = ? " +
                ", cash_deposit = ? " +
                ", parking = ? " +
                ", parking_count = ? " +
                ", cancel_policy = ? " +
                ", sales_flag = ? " +
                ", hotel_id = ? " +
                ", user_id = ? " +
                ", child_charge_info = ? " +
                ", last_update = ? " +
                ", last_uptime = ? " +
                ", noshow_credit_flag = ? " +
                ", pre_open_flag = ? " +
                ", equip_disp_flag = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.iD );
            prestate.setInt( 2, this.deadline_time );
            prestate.setString( 3, this.reserve_pr );
            prestate.setString( 4, "" );
            prestate.setString( 5, "" );
            prestate.setInt( 6, this.cashDeposit );
            prestate.setInt( 7, this.parking );
            prestate.setInt( 8, this.parkingCount );
            prestate.setString( 9, this.cancelPolicy );
            prestate.setInt( 10, this.salesFlag );
            prestate.setString( 11, this.hotelId );
            prestate.setInt( 12, this.userId );
            prestate.setString( 13, this.child_charge );
            prestate.setInt( 14, this.lastUpdate );
            prestate.setInt( 15, this.lastUptime );
            prestate.setInt( 16, this.noshow_credit_flag );
            prestate.setInt( 17, this.preOpenFlag );
            prestate.setInt( 18, this.equipDispFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserveBasic.insertData] Exception=" + e.toString() );
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
