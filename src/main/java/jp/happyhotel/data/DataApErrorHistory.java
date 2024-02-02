package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * エラー履歴(ap_error_history)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApErrorHistory implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -1435334082901647569L;
    public static final String TABLE            = "ap_error_history";
    private int                errorSeq;                                // エラー連番
    private int                errorCode;                               // エラーコード
    private int                errorSub;                                // エラーサブコード
    private int                id;                                      // ハピホテID
    private int                terminalId;                              // ホテルごとの端末ID
    private String             hotenaviId;                              // ホテナビID
    private int                roomNo;                                  // 部屋番号
    private int                terminalNo;                              // ターミナル番号
    private int                touchSeq;                                // ap_terminal_touch.touch_seq
    private String             userId;                                  // ユーザーID
    private int                registDate;                              // 登録日付(YYYYMMDD)
    private int                registTime;                              // 登録時刻(HHMMSS)
    private int                sendFlag;                                // 1:送信
    private String             hotelName;                               // ホテル名（入力）
    private String             roomName;                                // 部屋名称（入力）
    private int                entryDate;                               // 入室日付（入力 YYMMDD)
    private int                entryTime;                               // 入室時刻（入力 HHMM)
    private String             reserveNo;                               // 予約番号（予約来店に関する場合のみ）

    /**
     * データを初期化します。
     */
    public DataApErrorHistory()
    {
        this.errorSeq = 0;
        this.errorCode = 0;
        this.errorSub = 0;
        this.id = 0;
        this.terminalId = 0;
        this.hotenaviId = "";
        this.roomNo = 0;
        this.terminalNo = 0;
        this.touchSeq = 0;
        this.userId = "";
        this.registDate = 0;
        this.registTime = 0;
        this.sendFlag = 0;
        this.hotelName = "";
        this.roomName = "";
        this.entryDate = 0;
        this.entryTime = 0;
        this.reserveNo = "";
    }

    public int getErrorSeq()
    {
        return errorSeq;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public int getErrorSub()
    {
        return errorSub;
    }

    public int getId()
    {
        return id;
    }

    public int getTerminalId()
    {
        return terminalId;
    }

    public String getHotenaviId()
    {
        return hotenaviId;
    }

    public int getRoomNo()
    {
        return roomNo;
    }

    public int getTerminalNo()
    {
        return terminalNo;
    }

    public int getTouchSeq()
    {
        return touchSeq;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getSendFlag()
    {
        return sendFlag;
    }

    public String getHotelName()
    {
        return hotelName;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public int getEntryDate()
    {
        return entryDate;
    }

    public int getEntryTime()
    {
        return entryTime;
    }

    public String getReserveNo()
    {
        return reserveNo;
    }

    public void setErrorSeq(int errorSeq)
    {
        this.errorSeq = errorSeq;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public void setErrorSub(int errorSub)
    {
        this.errorSub = errorSub;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTerminalId(int terminalId)
    {
        this.terminalId = terminalId;
    }

    public void setHotenaviId(String hotenaviId)
    {
        this.hotenaviId = hotenaviId;
    }

    public void setRoomNo(int roomNo)
    {
        this.roomNo = roomNo;
    }

    public void setTerminalNo(int terminalNo)
    {
        this.terminalNo = terminalNo;
    }

    public void setTouchSeq(int touchSeq)
    {
        this.touchSeq = touchSeq;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setSendFlag(int sendFlag)
    {
        this.sendFlag = sendFlag;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setEntryDate(int entryDate)
    {
        this.entryDate = entryDate;
    }

    public void setEntryTime(int entryTime)
    {
        this.entryTime = entryTime;
    }

    public void setReserveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    /****
     * エラー履歴(ap_error_history)取得
     * 
     * @param errorSeq エラー連番
     * @return
     */
    public boolean getData(int errorSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_error_history WHERE error_seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, errorSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.errorSeq = result.getInt( "error_seq" );
                    this.errorCode = result.getInt( "error_code" );
                    this.errorSub = result.getInt( "error_sub" );
                    this.id = result.getInt( "id" );
                    this.terminalId = result.getInt( "terminal_id" );
                    this.hotenaviId = result.getString( "hotenavi_id" );
                    this.roomNo = result.getInt( "room_no" );
                    this.terminalNo = result.getInt( "terminal_no" );
                    this.touchSeq = result.getInt( "touch_seq" );
                    this.userId = result.getString( "user_id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.sendFlag = result.getInt( "send_flag" );
                    this.hotelName = result.getString( "hotel_name" );
                    this.roomName = result.getString( "room_name" );
                    this.entryDate = result.getInt( "entry_date" );
                    this.entryTime = result.getInt( "entry_time" );
                    this.reserveNo = result.getString( "reserve_no" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApErrorHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * エラー履歴(ap_error_history)設定
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
                this.errorSeq = result.getInt( "error_seq" );
                this.errorCode = result.getInt( "error_code" );
                this.errorSub = result.getInt( "error_sub" );
                this.id = result.getInt( "id" );
                this.terminalId = result.getInt( "terminal_id" );
                this.hotenaviId = result.getString( "hotenavi_id" );
                this.roomNo = result.getInt( "room_no" );
                this.terminalNo = result.getInt( "terminal_no" );
                this.touchSeq = result.getInt( "touch_seq" );
                this.userId = result.getString( "user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.sendFlag = result.getInt( "send_flag" );
                this.hotelName = result.getString( "hotel_name" );
                this.roomName = result.getString( "room_name" );
                this.entryDate = result.getInt( "entry_date" );
                this.entryTime = result.getInt( "entry_time" );
                this.reserveNo = result.getString( "reserve_no" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApErrorHistory.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * エラー履歴(ap_error_history)挿入
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

        query = "INSERT ap_error_history SET ";
        query += " error_seq=?";
        query += ", error_code=?";
        query += ", error_sub=?";
        query += ", id=?";
        query += ", terminal_id=?";
        query += ", hotenavi_id=?";
        query += ", room_no=?";
        query += ", terminal_no=?";
        query += ", touch_seq=?";
        query += ", user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", send_flag=?";
        query += ", hotel_name=?";
        query += ", room_name=?";
        query += ", entry_date=?";
        query += ", entry_time=?";
        query += ", reserve_no=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.errorSeq );
            prestate.setInt( i++, this.errorCode );
            prestate.setInt( i++, this.errorSub );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.terminalId );
            prestate.setString( i++, this.hotenaviId );
            prestate.setInt( i++, this.roomNo );
            prestate.setInt( i++, this.terminalNo );
            prestate.setInt( i++, this.touchSeq );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.sendFlag );
            prestate.setString( i++, this.hotelName );
            prestate.setString( i++, this.roomName );
            prestate.setInt( i++, this.entryDate );
            prestate.setInt( i++, this.entryTime );
            prestate.setString( i++, this.reserveNo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApErrorHistory.insertData] Exception=" + e.toString() );
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
     * エラー履歴(ap_error_history)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param errorSeq エラー連番
     * @return
     */
    public boolean updateData(int errorSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_error_history SET ";
        query += " error_code=?";
        query += ", error_sub=?";
        query += ", id=?";
        query += ", terminal_id=?";
        query += ", hotenavi_id=?";
        query += ", room_no=?";
        query += ", terminal_no=?";
        query += ", touch_seq=?";
        query += ", user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", send_flag=?";
        query += ", hotel_name=?";
        query += ", room_name=?";
        query += ", entry_date=?";
        query += ", entry_time=?";
        query += ", reserve_no=?";
        query += " WHERE error_seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.errorCode );
            prestate.setInt( i++, this.errorSub );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.terminalId );
            prestate.setString( i++, this.hotenaviId );
            prestate.setInt( i++, this.roomNo );
            prestate.setInt( i++, this.terminalNo );
            prestate.setInt( i++, this.touchSeq );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.sendFlag );
            prestate.setString( i++, this.hotelName );
            prestate.setString( i++, this.roomName );
            prestate.setInt( i++, this.entryDate );
            prestate.setInt( i++, this.entryTime );
            prestate.setInt( i++, this.errorSeq );
            prestate.setString( i++, this.reserveNo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApErrorHistory.updateData] Exception=" + e.toString() );
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
