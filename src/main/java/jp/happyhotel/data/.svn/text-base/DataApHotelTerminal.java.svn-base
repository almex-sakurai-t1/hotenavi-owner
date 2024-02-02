package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 端末マスタ(ap_hotel_terminal)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApHotelTerminal implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = 9149112312515150783L;
    public static final String TABLE            = "ap_hotel_terminal";
    private int                id;                                     // ハピホテID
    private int                terminalId;                             // ホテルごとの端末ID
    private String             serialNo;                               // 端末シリアル番号
    private int                roomNo;                                 // 部屋番号（設置場所が部屋でない場合は0）
    private int                terminalNo;                             // ターミナル番号（部屋に設置する場合は0）
    private String             terminalName;                           // ターミナル名称
    private int                terminalKind;                           // 1:sstouch
    private int                startDate;                              // 利用開始日付(YYYYMMDD)
    private int                startTime;                              // 利用開始時刻(HHMMSS)
    private int                endDate;                                // 利用終了日付(YYYYMMDD)
    private int                endTime;                                // 利用終了時刻(HHMMSS)
    private long               rtcTimer;                               // RTCタイマー
    private int                lastUpdate;                             // 最終更新日
    private int                lastUptime;                             // 最終更新時刻
    private int                frontTouchState;                        // フロントタッチ受付許可状態（0:受付不可、1:受付許可）:int(10)
    private int                limitDate;                              // フロントタッチ受付制限日:int(10)
    private int                limitTime;                              // フロントタッチ受付制限時刻:int(10)
    private int                autoCheckinFlag;                        // タッチ時来店処理自動フラグ(1(default):タッチ時自動来店 0:タッチ時自動来店OFF)
    private String             ipAddress;                              // メニュー盤のIPアドレス

    /**
     * データを初期化します。
     */
    public DataApHotelTerminal()
    {
        this.id = 0;
        this.terminalId = 0;
        this.serialNo = "";
        this.roomNo = 0;
        this.terminalNo = 0;
        this.terminalName = "";
        this.terminalKind = 0;
        this.startDate = 0;
        this.startTime = 0;
        this.endDate = 0;
        this.endTime = 0;
        this.rtcTimer = 0l;
        this.lastUpdate = 0;
        this.lastUptime = 0;
        this.frontTouchState = 0;
        this.limitDate = 0;
        this.limitTime = 0;
        this.autoCheckinFlag = 1;
        this.ipAddress = "";
    }

    public int getId()
    {
        return id;
    }

    public int getTerminalId()
    {
        return terminalId;
    }

    public String getSerialNo()
    {
        return serialNo;
    }

    public int getRoomNo()
    {
        return roomNo;
    }

    public int getTerminalNo()
    {
        return terminalNo;
    }

    public String getTerminalName()
    {
        return terminalName;
    }

    public int getTerminalKind()
    {
        return terminalKind;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getStartTime()
    {
        return startTime;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getEndTime()
    {
        return endTime;
    }

    public long getRtcTimer()
    {
        return rtcTimer;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getFrontTouchState()
    {
        return frontTouchState;
    }

    public int getLimitDate()
    {
        return limitDate;
    }

    public int getLimitTime()
    {
        return limitTime;
    }

    public int getAutoCheckinFlag()
    {
        return autoCheckinFlag;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTerminalId(int terminalId)
    {
        this.terminalId = terminalId;
    }

    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }

    public void setRoomNo(int roomNo)
    {
        this.roomNo = roomNo;
    }

    public void setTerminalNo(int terminalNo)
    {
        this.terminalNo = terminalNo;
    }

    public void setTerminalName(String terminalName)
    {
        this.terminalName = terminalName;
    }

    public void setTerminalKind(int terminalKind)
    {
        this.terminalKind = terminalKind;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setStartTime(int startTime)
    {
        this.startTime = startTime;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setEndTime(int endTime)
    {
        this.endTime = endTime;
    }

    public void setRtcTimer(long rtcTimer)
    {
        this.rtcTimer = rtcTimer;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setFrontTouchState(int frontTouchState)
    {
        this.frontTouchState = frontTouchState;
    }

    public void setLimitDate(int limitDate)
    {
        this.limitDate = limitDate;
    }

    public void setLimitTime(int limitTime)
    {
        this.limitTime = limitTime;
    }

    public void setAutoCheckinFlag(int autoCheckinFlag)
    {
        this.autoCheckinFlag = autoCheckinFlag;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    /****
     * 端末マスタ(ap_hotel_terminal)取得
     * 
     * @param id ハピホテID
     * @param terminalId ホテルごとの端末ID
     * @return
     */
    public boolean getData(int id, int terminalId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_hotel_terminal WHERE id = ? AND terminal_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, terminalId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.terminalId = result.getInt( "terminal_id" );
                    this.serialNo = result.getString( "serial_no" );
                    this.roomNo = result.getInt( "room_no" );
                    this.terminalNo = result.getInt( "terminal_no" );
                    this.terminalName = result.getString( "terminal_name" );
                    this.terminalKind = result.getInt( "terminal_kind" );
                    this.startDate = result.getInt( "start_date" );
                    this.startTime = result.getInt( "start_time" );
                    this.endDate = result.getInt( "end_date" );
                    this.endTime = result.getInt( "end_time" );
                    this.rtcTimer = result.getLong( "rtc_timer" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.frontTouchState = result.getInt( "front_touch_state" );
                    this.limitDate = result.getInt( "limit_date" );
                    this.limitTime = result.getInt( "limit_time" );
                    this.autoCheckinFlag = result.getInt( "auto_checkin_flag" );
                    this.ipAddress = result.getString( "ip_address" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminal.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * 端末マスタ(ap_hotel_terminal)取得
     * 
     * @param id ハピホテID
     * @param 部屋番号 部屋番号
     * @return
     */
    public boolean getDataByRoomNo(int id, int roomNo)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_hotel_terminal WHERE id = ? AND room_no = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, roomNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.terminalId = result.getInt( "terminal_id" );
                    this.serialNo = result.getString( "serial_no" );
                    this.roomNo = result.getInt( "room_no" );
                    this.terminalNo = result.getInt( "terminal_no" );
                    this.terminalName = result.getString( "terminal_name" );
                    this.terminalKind = result.getInt( "terminal_kind" );
                    this.startDate = result.getInt( "start_date" );
                    this.startTime = result.getInt( "start_time" );
                    this.endDate = result.getInt( "end_date" );
                    this.endTime = result.getInt( "end_time" );
                    this.rtcTimer = result.getLong( "rtc_timer" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.frontTouchState = result.getInt( "front_touch_state" );
                    this.limitDate = result.getInt( "limit_date" );
                    this.limitTime = result.getInt( "limit_time" );
                    this.autoCheckinFlag = result.getInt( "auto_checkin_flag" );
                    this.ipAddress = result.getString( "ip_address" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminal.getDataByRoomNo] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 端末マスタ(ap_hotel_terminal)設定
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
                this.terminalId = result.getInt( "terminal_id" );
                this.serialNo = result.getString( "serial_no" );
                this.roomNo = result.getInt( "room_no" );
                this.terminalNo = result.getInt( "terminal_no" );
                this.terminalName = result.getString( "terminal_name" );
                this.terminalKind = result.getInt( "terminal_kind" );
                this.startDate = result.getInt( "start_date" );
                this.startTime = result.getInt( "start_time" );
                this.endDate = result.getInt( "end_date" );
                this.endTime = result.getInt( "end_time" );
                this.rtcTimer = result.getLong( "rtc_timer" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.frontTouchState = result.getInt( "front_touch_state" );
                this.limitDate = result.getInt( "limit_date" );
                this.limitTime = result.getInt( "limit_time" );
                this.autoCheckinFlag = result.getInt( "auto_checkin_flag" );
                this.ipAddress = result.getString( "ip_address" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminal.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 端末マスタ(ap_hotel_terminal)挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT ap_hotel_terminal SET ";
        query += " id=?";
        query += ", terminal_id=?";
        query += ", serial_no=?";
        query += ", room_no=?";
        query += ", terminal_no=?";
        query += ", terminal_name=?";
        query += ", terminal_kind=?";
        query += ", start_date=?";
        query += ", start_time=?";
        query += ", end_date=?";
        query += ", end_time=?";
        query += ", rtc_timer=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", front_touch_state=?";
        query += ", limit_date=?";
        query += ", limit_time=?";
        query += ", auto_checkin_flag=?";
        query += ", ip_address=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.terminalId );
            prestate.setString( i++, this.serialNo );
            prestate.setInt( i++, this.roomNo );
            prestate.setInt( i++, this.terminalNo );
            prestate.setString( i++, this.terminalName );
            prestate.setInt( i++, this.terminalKind );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.startTime );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.endTime );
            prestate.setLong( i++, this.rtcTimer );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.frontTouchState );
            prestate.setInt( i++, this.limitDate );
            prestate.setInt( i++, this.limitTime );
            prestate.setInt( i++, this.autoCheckinFlag );
            prestate.setString( i++, this.ipAddress );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminal.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 端末マスタ(ap_hotel_terminal)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ハピホテID
     * @param terminalId ホテルごとの端末ID
     * @return
     */
    public boolean updateData(int id, int terminalId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_hotel_terminal SET ";
        query += " serial_no=?";
        query += ", room_no=?";
        query += ", terminal_no=?";
        query += ", terminal_name=?";
        query += ", terminal_kind=?";
        query += ", start_date=?";
        query += ", start_time=?";
        query += ", end_date=?";
        query += ", end_time=?";
        query += ", rtc_timer=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", front_touch_state=?";
        query += ", limit_date=?";
        query += ", limit_time=?";
        query += ", auto_checkin_flag=?";
        query += ", ip_address=?";
        query += " WHERE id=? AND terminal_id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.serialNo );
            prestate.setInt( i++, this.roomNo );
            prestate.setInt( i++, this.terminalNo );
            prestate.setString( i++, this.terminalName );
            prestate.setInt( i++, this.terminalKind );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.startTime );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.endTime );
            prestate.setLong( i++, this.rtcTimer );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.frontTouchState );
            prestate.setInt( i++, this.limitDate );
            prestate.setInt( i++, this.limitTime );
            prestate.setInt( i++, this.autoCheckinFlag );
            prestate.setString( i++, this.ipAddress );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.terminalId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminal.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 端末マスタ(ap_hotel_terminal)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ハピホテID
     * @param terminalId ホテルごとの端末ID
     * @return
     */
    public boolean updateFrontData(int id)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_hotel_terminal SET ";
        query += " serial_no=?";
        query += ", room_no=?";
        query += ", terminal_no=?";
        query += ", terminal_name=?";
        query += ", terminal_kind=?";
        query += ", start_date=?";
        query += ", start_time=?";
        query += ", end_date=?";
        query += ", end_time=?";
        query += ", rtc_timer=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", front_touch_state=?";
        query += ", limit_date=?";
        query += ", limit_time=?";
        query += ", auto_checkin_flag=?";
        query += ", ip_address=?";
        query += " WHERE id=? AND room_no=0 AND terminal_no > 0";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.serialNo );
            prestate.setInt( i++, this.roomNo );
            prestate.setInt( i++, this.terminalNo );
            prestate.setString( i++, this.terminalName );
            prestate.setInt( i++, this.terminalKind );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.startTime );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.endTime );
            prestate.setLong( i++, this.rtcTimer );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.frontTouchState );
            prestate.setInt( i++, this.limitDate );
            prestate.setInt( i++, this.limitTime );
            prestate.setInt( i++, this.autoCheckinFlag );
            prestate.setString( i++, this.ipAddress );
            prestate.setInt( i++, this.id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminal.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * フロントの端末データを取得
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ハピホテID
     * @return
     */
    public boolean getFrontTerminal(int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        ret = false;
        query = "SELECT * FROM ap_hotel_terminal ";
        query += " WHERE id=? AND room_no=0 AND terminal_no > 0";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = this.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminal.getFrontTerminal] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
