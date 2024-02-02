/*
 * 予約部屋クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvRoom implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 5698540492016118074L;

    private int               iD;
    private int               seq;
    private String            roomName;
    private String            roomPr;
    private String            remarks;
    private int               salesFlag;
    private String            hotelId;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データの初期化
     */
    public DataRsvRoom()
    {
        iD = 0;
        seq = 0;
        roomName = "";
        roomPr = "";
        remarks = "";
        salesFlag = 0;
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

    public int getSeq()
    {
        return this.seq;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRoomPr()
    {
        return this.roomPr;
    }

    public String getRemarks()
    {
        return this.remarks;
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

    // setter
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setRoomPr(String roomPr)
    {
        this.roomPr = roomPr;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public void setSalesFlag(int salesFlag)
    {
        this.salesFlag = salesFlag;
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
     * 予約部屋情報取得
     * 
     * @param iD ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, int seq)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, seq, room_name, room_pr, remarks, " +
                " sales_flag, hotel_id, user_id, last_update, last_uptime " +
                " FROM hh_rsv_room WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.roomName = result.getString( "room_name" );
                    this.roomPr = ConvertCharacterSet.convDb2Form( (CheckString.checkStringForNull( result.getString( "room_pr" ) )).trim() );
                    this.remarks = ConvertCharacterSet.convDb2Form( (CheckString.checkStringForNull( result.getString( "remarks" ) )).trim() );
                    this.salesFlag = result.getInt( "sales_flag" );
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
            Logging.error( "[DataRsvRoom.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
