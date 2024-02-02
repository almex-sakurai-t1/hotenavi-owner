package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 端末タッチ履歴(ap_terminal_touch)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApTerminalTouch implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -1476220433326116449L;
    public static final String TABLE            = "ap_terminal_touch";
    private int                id;                                      // ハピホテホテルID（タッチ時ap_hotel_terminalから取得）
    private int                terminalId;                              // ターミナルID（タッチ時ap_hotel_terminalから取得）
    private int                touchSeq;                                // タッチ連番
    private String             userId;                                  // ユーザーID
    private String             idm;                                     // IDMもしくはデバイストークン
    private int                ciSeq;                                   // チェックインコード（ap_touch_ci.seq )
    private int                roomNo;                                  // 部屋番号
    private int                terminalNo;                              // ターミナル番号
    private int                touchDate;                               // タッチ日付（YYYYMMDD)
    private int                touchTime;                               // タッチ時刻（HHMMSS）
    private int                registDate;                              // 紐付日付（YYYYMMDD)
    private int                registTime;                              // 紐付時刻（HHMMSS)
    private String             touchIp;                                 // IPアドレス
    private String             touchUseragent;                          // ユーザーエージェント

    /**
     * データを初期化します。
     */
    public DataApTerminalTouch()
    {
        this.id = 0;
        this.terminalId = 0;
        this.touchSeq = 0;
        this.userId = "";
        this.idm = "";
        this.ciSeq = 0;
        this.roomNo = 0;
        this.terminalNo = 0;
        this.touchDate = 0;
        this.touchTime = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.touchIp = "";
        this.touchUseragent = "";
    }

    public int getId()
    {
        return id;
    }

    public int getTerminalId()
    {
        return terminalId;
    }

    public int getTouchSeq()
    {
        return touchSeq;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getIdm()
    {
        return idm;
    }

    public int getCiSeq()
    {
        return ciSeq;
    }

    public int getRoomNo()
    {
        return roomNo;
    }

    public int getTerminalNo()
    {
        return terminalNo;
    }

    public int getTouchDate()
    {
        return touchDate;
    }

    public int getTouchTime()
    {
        return touchTime;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public String getTouchIp()
    {
        return touchIp;
    }

    public String getTouchUseragent()
    {
        return touchUseragent;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTerminalId(int terminalId)
    {
        this.terminalId = terminalId;
    }

    public void setTouchSeq(int touchSeq)
    {
        this.touchSeq = touchSeq;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setIdm(String idm)
    {
        this.idm = idm;
    }

    public void setCiSeq(int ciSeq)
    {
        this.ciSeq = ciSeq;
    }

    public void setRoomNo(int roomNo)
    {
        this.roomNo = roomNo;
    }

    public void setTerminalNo(int terminalNo)
    {
        this.terminalNo = terminalNo;
    }

    public void setTouchDate(int touchDate)
    {
        this.touchDate = touchDate;
    }

    public void setTouchTime(int touchTime)
    {
        this.touchTime = touchTime;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setTouchIp(String touchIp)
    {
        this.touchIp = touchIp;
    }

    public void setTouchUseragent(String touchUseragent)
    {
        this.touchUseragent = touchUseragent;
    }

    /****
     * 端末タッチ履歴(ap_terminal_touch)取得
     * 
     * @param id ハピホテホテルID（タッチ時ap_hotel_terminalから取得）
     * @param terminalId ターミナルID（タッチ時ap_hotel_terminalから取得）
     * @param touchSeq タッチ連番
     * @return
     */
    public boolean getData(int id, int terminalId, int touchSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_terminal_touch WHERE id = ? AND terminal_id = ? AND touch_seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, terminalId );
            prestate.setInt( 3, touchSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.terminalId = result.getInt( "terminal_id" );
                    this.touchSeq = result.getInt( "touch_seq" );
                    this.userId = result.getString( "user_id" );
                    this.idm = result.getString( "idm" );
                    this.ciSeq = result.getInt( "ci_seq" );
                    this.roomNo = result.getInt( "room_no" );
                    this.terminalNo = result.getInt( "terminal_no" );
                    this.touchDate = result.getInt( "touch_date" );
                    this.touchTime = result.getInt( "touch_time" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.touchIp = result.getString( "touch_ip" );
                    this.touchUseragent = result.getString( "touch_useragent" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTerminalTouch.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 端末タッチ履歴(ap_terminal_touch)設定
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
                this.touchSeq = result.getInt( "touch_seq" );
                this.userId = result.getString( "user_id" );
                this.idm = result.getString( "idm" );
                this.ciSeq = result.getInt( "ci_seq" );
                this.roomNo = result.getInt( "room_no" );
                this.terminalNo = result.getInt( "terminal_no" );
                this.touchDate = result.getInt( "touch_date" );
                this.touchTime = result.getInt( "touch_time" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.touchIp = result.getString( "touch_ip" );
                this.touchUseragent = result.getString( "touch_useragent" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTerminalTouch.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 端末タッチ履歴(ap_terminal_touch)挿入
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

        query = "INSERT ap_terminal_touch SET ";
        query += " id=?";
        query += ", terminal_id=?";
        query += ", touch_seq=?";
        query += ", user_id=?";
        query += ", idm=?";
        query += ", ci_seq=?";
        query += ", room_no=?";
        query += ", terminal_no=?";
        query += ", touch_date=?";
        query += ", touch_time=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", touch_ip=?";
        query += ", touch_useragent=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.terminalId );
            prestate.setInt( i++, this.touchSeq );
            prestate.setString( i++, this.userId );
            prestate.setString( i++, this.idm );
            prestate.setInt( i++, this.ciSeq );
            prestate.setInt( i++, this.roomNo );
            prestate.setInt( i++, this.terminalNo );
            prestate.setInt( i++, this.touchDate );
            prestate.setInt( i++, this.touchTime );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setString( i++, this.touchIp );
            prestate.setString( i++, this.touchUseragent );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTerminalTouch.insertData] Exception=" + e.toString() );
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
     * 端末タッチ履歴(ap_terminal_touch)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ハピホテホテルID（タッチ時ap_hotel_terminalから取得）
     * @param terminalId ターミナルID（タッチ時ap_hotel_terminalから取得）
     * @param touchSeq タッチ連番
     * @return
     */
    public boolean updateData(int id, int terminalId, int touchSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_terminal_touch SET ";
        query += " user_id=?";
        query += ", idm=?";
        query += ", ci_seq=?";
        query += ", room_no=?";
        query += ", terminal_no=?";
        query += ", touch_date=?";
        query += ", touch_time=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", touch_ip=?";
        query += ", touch_useragent=?";
        query += " WHERE id=? AND terminal_id=? AND touch_seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.userId );
            prestate.setString( i++, this.idm );
            prestate.setInt( i++, this.ciSeq );
            prestate.setInt( i++, this.roomNo );
            prestate.setInt( i++, this.terminalNo );
            prestate.setInt( i++, this.touchDate );
            prestate.setInt( i++, this.touchTime );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setString( i++, this.touchIp );
            prestate.setString( i++, this.touchUseragent );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.terminalId );
            prestate.setInt( i++, this.touchSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTerminalTouch.updateData] Exception=" + e.toString() );
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
