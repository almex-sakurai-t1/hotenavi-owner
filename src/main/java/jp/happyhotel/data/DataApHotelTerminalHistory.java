package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 端末マスタ更新履歴(ap_hotel_terminal_history)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApHotelTerminalHistory implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -5442979207546710609L;
    public static final String TABLE            = "ap_hotel_terminal_history";
    private int                id;                                            // ハピホテID
    private int                terminalId;                                    // ホテルごとの端末ID
    private int                seq;                                           // 連番
    private String             serialNo;                                      // 端末シリアル番号
    private int                roomNo;                                        // 部屋番号（設置場所が部屋でない場合は0）
    private int                terminalNo;                                    // ターミナル番号（部屋に設置する場合は0）
    private String             terminalName;                                  // ターミナル名称
    private int                terminalKind;                                  // 1:sstouch
    private int                startDate;                                     // 利用開始日付(YYYYMMDD)
    private int                startTime;                                     // 利用開始時刻(HHMMSS)
    private int                endDate;                                       // 利用終了日付(YYYYMMDD)
    private int                endTime;                                       // 利用終了時刻(HHMMSS)
    private int                registDate;                                    // 更新日付(YYYYMMDD)
    private int                registTime;                                    // 更新時刻(HHMMSS)
    private int                companyId;                                     // 会社ID:2
    private String             staffId;                                       // 社員ID

    /**
     * データを初期化します。
     */
    public DataApHotelTerminalHistory()
    {
        this.id = 0;
        this.terminalId = 0;
        this.seq = 0;
        this.serialNo = "";
        this.roomNo = 0;
        this.terminalNo = 0;
        this.terminalName = "";
        this.terminalKind = 0;
        this.startDate = 0;
        this.startTime = 0;
        this.endDate = 0;
        this.endTime = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.companyId = 0;
        this.staffId = "";
    }

    public int getId()
    {
        return id;
    }

    public int getTerminalId()
    {
        return terminalId;
    }

    public int getSeq()
    {
        return seq;
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

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public String getStaffId()
    {
        return staffId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTerminalId(int terminalId)
    {
        this.terminalId = terminalId;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
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

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }

    public void setStaffId(String staffId)
    {
        this.staffId = staffId;
    }

    /****
     * 端末マスタ更新履歴(ap_hotel_terminal_history)取得
     * 
     * @param id ハピホテID
     * @param terminalId ホテルごとの端末ID
     * @param seq 連番
     * @return
     */
    public boolean getData(int id, int terminalId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_hotel_terminal_history WHERE id = ? AND terminal_id = ? AND seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, terminalId );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.terminalId = result.getInt( "terminal_id" );
                    this.seq = result.getInt( "seq" );
                    this.serialNo = result.getString( "serial_no" );
                    this.roomNo = result.getInt( "room_no" );
                    this.terminalNo = result.getInt( "terminal_no" );
                    this.terminalName = result.getString( "terminal_name" );
                    this.terminalKind = result.getInt( "terminal_kind" );
                    this.startDate = result.getInt( "start_date" );
                    this.startTime = result.getInt( "start_time" );
                    this.endDate = result.getInt( "end_date" );
                    this.endTime = result.getInt( "end_time" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.companyId = result.getInt( "company_id" );
                    this.staffId = result.getString( "staff_id" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminalHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 端末マスタ更新履歴(ap_hotel_terminal_history)設定
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
                this.seq = result.getInt( "seq" );
                this.serialNo = result.getString( "serial_no" );
                this.roomNo = result.getInt( "room_no" );
                this.terminalNo = result.getInt( "terminal_no" );
                this.terminalName = result.getString( "terminal_name" );
                this.terminalKind = result.getInt( "terminal_kind" );
                this.startDate = result.getInt( "start_date" );
                this.startTime = result.getInt( "start_time" );
                this.endDate = result.getInt( "end_date" );
                this.endTime = result.getInt( "end_time" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.companyId = result.getInt( "company_id" );
                this.staffId = result.getString( "staff_id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminalHistory.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 端末マスタ更新履歴(ap_hotel_terminal_history)挿入
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

        query = "INSERT ap_hotel_terminal_history SET ";
        query += " id=?";
        query += ", terminal_id=?";
        query += ", seq=?";
        query += ", serial_no=?";
        query += ", room_no=?";
        query += ", terminal_no=?";
        query += ", terminal_name=?";
        query += ", terminal_kind=?";
        query += ", start_date=?";
        query += ", start_time=?";
        query += ", end_date=?";
        query += ", end_time=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.terminalId );
            prestate.setInt( i++, this.seq );
            prestate.setString( i++, this.serialNo );
            prestate.setInt( i++, this.roomNo );
            prestate.setInt( i++, this.terminalNo );
            prestate.setString( i++, this.terminalName );
            prestate.setInt( i++, this.terminalKind );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.startTime );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.endTime );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminalHistory.insertData] Exception=" + e.toString() );
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
     * 端末マスタ更新履歴(ap_hotel_terminal_history)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ハピホテID
     * @param terminalId ホテルごとの端末ID
     * @param seq 連番
     * @return
     */
    public boolean updateData(int id, int terminalId, int seq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_hotel_terminal_history SET ";
        query += " serial_no=?";
        query += ", room_no=?";
        query += ", terminal_no=?";
        query += ", terminal_name=?";
        query += ", terminal_kind=?";
        query += ", start_date=?";
        query += ", start_time=?";
        query += ", end_date=?";
        query += ", end_time=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", company_id=?";
        query += ", staff_id=?";
        query += " WHERE id=? AND terminal_id=? AND seq=?";

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
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.companyId );
            prestate.setString( i++, this.staffId );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.terminalId );
            prestate.setInt( i++, this.seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApHotelTerminalHistory.updateData] Exception=" + e.toString() );
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
