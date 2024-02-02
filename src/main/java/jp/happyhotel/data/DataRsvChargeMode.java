/*
 * 料金モードクラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvChargeMode implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 558195860178631239L;

    private int               iD;
    private int               chargeModeId;
    private String            chargeModeName;
    private String            remarks;
    private int               editFlag;
    private String            hotelId;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データの初期化
     */
    public DataRsvChargeMode()
    {
        iD = 0;
        chargeModeId = 0;
        chargeModeName = "";
        remarks = "";
        editFlag = 0;
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

    public int getChargeModeId()
    {
        return this.chargeModeId;
    }

    public String getChargeModeName()
    {
        return this.chargeModeName;
    }

    public String getRemarks()
    {
        return this.remarks;
    }

    public int getEditFlag()
    {
        return this.editFlag;
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

    public void setChargeModeId(int chargeModeId)
    {
        this.chargeModeId = chargeModeId;
    }

    public void setChargeModeName(String chargeModeName)
    {
        this.chargeModeName = chargeModeName;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public void setEditFlag(int editFlag)
    {
        this.editFlag = editFlag;
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
     * プラン別料金情報取得
     *
     * @param iD ホテルID
     * @param chargeModeId 料金モードID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int chargeModeId)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, charge_mode_id, charge_mode_name, remarks, edit_flag," +
                " hotel_id, user_id, last_update, last_uptime " +
                " FROM hh_rsv_charge_mode WHERE id = ? AND charge_mode_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, chargeModeId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.chargeModeId = result.getInt( "charge_mode_id" );
                    this.chargeModeName = ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "charge_mode_name" ) ) );
                    this.remarks = ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ) ) );
                    this.editFlag = result.getInt( "edit_flag" );
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
            Logging.error( "[DataRsvChargeMode.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
