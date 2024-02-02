package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

import org.apache.commons.lang.StringUtils;

/**
 * 取得クラス
 * 
 * @author Techno
 * @version 1.00 2015/3/3
 */
public class DataHhRsvRoomRemainder implements Serializable
{
    public static final String TABLE = "hh_rsv_room_remainder";
    /** ホテルID */
    private int                id;
    /** 日付 */
    private int                calDate;
    /** 管理番号: 部屋の識別コード */
    private int                seq;
    /**
     * 部屋残数ステータス（宿泊）
     * 1：空き、2：予約、3：売り止め
     */
    private int                stayStatus;
    /**
     * 宿泊予約番号: 部屋を予約した時の予約番号
     * (ステータスが2：予約になった時に設定される
     */
    private String             stayReserveNo;
    /** 宿泊予約番号(仮) */
    private long               stayReserveTempNo;
    /** 宿泊予約有効期限 */
    private int                stayReserveLimitDay;
    /** 宿泊予約有効期限時刻 */
    private int                stayReserveLimitTime;
    /**
     * 部屋残数ステータス（休憩）
     * 1：空き、2：予約、3：売り止め
     */
    private int                restStatus;
    /**
     * 休憩予約番号: 部屋を予約した時の予約番号
     * (ステータスが""2：予約""になった時に設定される）"
     */
    private String             restReserveNo;
    /** 休憩予約番号(仮) */
    private long               restReserveTempNo;
    /** 休憩予約有効期限 */
    private int                restReserveLimitDay;
    /** 休憩予約有効期限時刻 */
    private int                restReserveLimitTime;

    /**
     * データを初期化します。
     */
    public DataHhRsvRoomRemainder()
    {
        this.id = 0;
        this.calDate = 0;
        this.seq = 0;
        this.stayStatus = 0;
        this.stayReserveNo = "";
        this.stayReserveTempNo = 0L;
        this.stayReserveLimitDay = 0;
        this.stayReserveLimitTime = 0;
        this.restStatus = 0;
        this.restReserveNo = "";
        this.restReserveTempNo = 0L;
        this.restReserveLimitDay = 0;
        this.restReserveLimitTime = 0;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getCalDate()
    {
        return calDate;
    }

    public void setCalDate(int calDate)
    {
        this.calDate = calDate;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public int getStayStatus()
    {
        return stayStatus;
    }

    public void setStayStatus(int stayStatus)
    {
        this.stayStatus = stayStatus;
    }

    public String getStayReserveNo()
    {
        return stayReserveNo;
    }

    public void setStayReserveNo(String stayReserveNo)
    {
        this.stayReserveNo = stayReserveNo;
    }

    public long getStayReserveTempNo()
    {
        return stayReserveTempNo;
    }

    public void setStayReserveTempNo(long stayReserveTempNo)
    {
        this.stayReserveTempNo = stayReserveTempNo;
    }

    public int getStayReserveLimitDay()
    {
        return stayReserveLimitDay;
    }

    public void setStayReserveLimitDay(int stayReserveLimitDay)
    {
        this.stayReserveLimitDay = stayReserveLimitDay;
    }

    public int getStayReserveLimitTime()
    {
        return stayReserveLimitTime;
    }

    public void setStayReserveLimitTime(int stayReserveLimitTime)
    {
        this.stayReserveLimitTime = stayReserveLimitTime;
    }

    public int getRestStatus()
    {
        return restStatus;
    }

    public void setRestStatus(int restStatus)
    {
        this.restStatus = restStatus;
    }

    public String getRestReserveNo()
    {
        return restReserveNo;
    }

    public void setRestReserveNo(String restReserveNo)
    {
        this.restReserveNo = restReserveNo;
    }

    public long getRestReserveTempNo()
    {
        return restReserveTempNo;
    }

    public void setRestReserveTempNo(long restReserveTempNo)
    {
        this.restReserveTempNo = restReserveTempNo;
    }

    public int getRestReserveLimitDay()
    {
        return restReserveLimitDay;
    }

    public void setRestReserveLimitDay(int restReserveLimitDay)
    {
        this.restReserveLimitDay = restReserveLimitDay;
    }

    public int getRestReserveLimitTime()
    {
        return restReserveLimitTime;
    }

    public void setRestReserveLimitTime(int restReserveLimitTime)
    {
        this.restReserveLimitTime = restReserveLimitTime;
    }

    /****
     * 取得
     * 
     * @param id ホテルID
     * @param calDate 日付
     * @param seq 管理番号: 部屋の識別コード
     * @return
     */
    public boolean getData(int id, int calDate, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT * FROM newRsvDB.hh_rsv_room_remainder WHERE id = ? AND cal_date = ? AND seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, calDate );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }
            return setData( result );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 仮予約番号から有効な部屋残数データを取得する
     * 
     * @param connection
     * @param planType
     * @param id
     * @param calDate
     * @param reserveTempNo
     * @return
     */
    public boolean getAvailableDataByReserveTempNo(Connection connection, int planType, int id, int calDate, long reserveTempNo)
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query = "";
        query += " SELECT * FROM newRsvDB.hh_rsv_room_remainder ";
        query += " WHERE id = ? ";
        query += " AND cal_date = ? ";
        query += " AND [type]_reserve_temp_no = ? ";
        query += " AND ( ";
        query += "   [type]_reserve_limit_day > ? ";
        query += "   OR ( ";
        query += "     [type]_reserve_limit_day = ? ";
        query += "     AND [type]_reserve_limit_time >= ? ";
        query += "   ) ";
        query += " ) ";
        query += " ORDER BY [type]_reserve_limit_day DESC, [type]_reserve_limit_time DESC ";

        String replacement = "";
        switch( planType )
        {
            case 1:
            case 2:
                replacement = "stay";
                break;
            case 3:
            case 4:
                replacement = "rest";
                break;
        }
        query = StringUtils.replace( query, "[type]", replacement );

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, calDate );
            prestate.setLong( 3, reserveTempNo );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 6, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }
            return setData( result );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.getAvailableDataByReserveTempNo] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 仮予約番号から部屋残数データを取得する
     * 有効期限は考慮しない（別途チェックが必要）
     * 
     * @param connection
     * @param planType
     * @param id
     * @param calDate
     * @param reserveTempNo
     * @return
     */
    public boolean getDataByReserveTempNo(int planType, int id, int calDate, long reserveTempNo)
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        Connection connection = null;
        String query = "";
        query += " SELECT * FROM newRsvDB.hh_rsv_room_remainder ";
        query += " WHERE id = ? ";
        query += " AND cal_date = ? ";
        query += " AND [type]_reserve_temp_no = ? ";
        query += " ORDER BY [type]_reserve_limit_day DESC, [type]_reserve_limit_time DESC ";

        String replacement = "";
        switch( planType )
        {
            case 1:
            case 2:
                replacement = "stay";
                break;
            case 3:
            case 4:
                replacement = "rest";
                break;
        }
        query = StringUtils.replace( query, "[type]", replacement );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, calDate );
            prestate.setLong( 3, reserveTempNo );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }
            return setData( result );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.getDataByReserveTempNo] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            this.id = result.getInt( "id" );
            this.calDate = result.getInt( "cal_date" );
            this.seq = result.getInt( "seq" );
            this.stayStatus = result.getInt( "stay_status" );
            this.stayReserveNo = result.getString( "stay_reserve_no" );
            this.stayReserveTempNo = result.getLong( "stay_reserve_temp_no" );
            this.stayReserveLimitDay = result.getInt( "stay_reserve_limit_day" );
            this.stayReserveLimitTime = result.getInt( "stay_reserve_limit_time" );
            this.restStatus = result.getInt( "rest_status" );
            this.restReserveNo = result.getString( "rest_reserve_no" );
            this.restReserveTempNo = result.getLong( "rest_reserve_temp_no" );
            this.restReserveLimitDay = result.getInt( "rest_reserve_limit_day" );
            this.restReserveLimitTime = result.getInt( "rest_reserve_limit_time" );
            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.setData] Exception=" + e.toString() );
            return false;
        }
    }

    /**
     * 挿入
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

        query = "INSERT newRsvDB.hh_rsv_room_remainder SET ";
        query += " id=?";
        query += ", cal_date=?";
        query += ", seq=?";
        query += ", stay_status=?";
        query += ", stay_reserve_no=?";
        query += ", stay_reserve_temp_no=?";
        query += ", stay_reserve_limit_day=?";
        query += ", stay_reserve_limit_time=?";
        query += ", rest_status=?";
        query += ", rest_reserve_no=?";
        query += ", rest_reserve_temp_no=?";
        query += ", rest_reserve_limit_day=?";
        query += ", rest_reserve_limit_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.calDate );
            prestate.setInt( i++, this.seq );
            prestate.setInt( i++, this.stayStatus );
            prestate.setString( i++, this.stayReserveNo );
            prestate.setLong( i++, this.stayReserveTempNo );
            prestate.setInt( i++, this.stayReserveLimitDay );
            prestate.setInt( i++, this.stayReserveLimitTime );
            prestate.setInt( i++, this.restStatus );
            prestate.setString( i++, this.restReserveNo );
            prestate.setLong( i++, this.restReserveTempNo );
            prestate.setInt( i++, this.restReserveLimitDay );
            prestate.setInt( i++, this.restReserveLimitTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.insertData] Exception=" + e.toString() );
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
     * 更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return
     */
    public boolean updateData()
    {
        boolean ret = false;
        ;
        Connection connection = null;
        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            updateData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /*
     * ここ以降は3/19以降に新しく作成しました
     */

    /**
     * calDate以降の日付の部屋状態を空きにする(updateの前にかけてください)
     * 仮予約状態は更新しない
     * 
     * @param id ホテルID
     * @param calDate 日付
     * @param seq 管理番号: 部屋の識別コード
     * @return
     */
    public boolean resetData(int id, int calDate, Connection connection)
    {
        StringBuilder query = new StringBuilder();

        query.append( " UPDATE newRsvDB.hh_rsv_room_remainder " );
        query.append( " SET " );
        query.append( "   stay_status=1, " );
        query.append( "   rest_status=1 " );
        query.append( " WHERE " );
        query.append( "   id=? " );
        query.append( "   AND cal_date>=? " );
        query.append( "   AND ( stay_status=3 OR rest_status=3 ) " );
        // query.append( "   AND ( stay_reserve_temp_no = 0 " );
        // // query.append( "     OR stay_reserve_temp_no = '' " );
        // query.append( "     OR ( " );
        // query.append( "       stay_reserve_limit_day < ? " );
        // query.append( "       OR ( " );
        // query.append( "         stay_reserve_limit_day = ? " );
        // query.append( "         AND stay_reserve_limit_time < ? " );
        // query.append( "       ) " );
        // query.append( "     ) " );
        // query.append( "   ) " );
        // query.append( "   AND ( rest_reserve_temp_no = 0 " );
        // // query.append( "     OR rest_reserve_temp_no = '' " );
        // query.append( "     OR ( " );
        // query.append( "       rest_reserve_limit_day < ? " );
        // query.append( "       OR ( " );
        // query.append( "         rest_reserve_limit_day = ? " );
        // query.append( "         AND rest_reserve_limit_time < ? " );
        // query.append( "       ) " );
        // query.append( "     ) " );
        // query.append( "   ) " );

        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query.toString() );
            // 更新対象の値をセットする
            int i = 1;
            prestate.setInt( i++, id );
            prestate.setInt( i++, calDate );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            int result = prestate.executeUpdate();
            if ( result > 0 )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.updateData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
    }

    /**
     * 更新(複数のテーブル更新用)
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param connection DBコネクション
     * @return
     */
    public boolean updateData(Connection connection)
    {
        StringBuilder query = new StringBuilder();
        query.append( " UPDATE newRsvDB.hh_rsv_room_remainder " );
        query.append( " SET " );
        query.append( "   stay_status=? " );
        // query.append( ",  stay_reserve_no=? " );
        // query.append( ",  stay_reserve_temp_no=? " );
        // query.append( ",  stay_reserve_limit_day=? " );
        // query.append( ",  stay_reserve_limit_time=? " );
        query.append( ",  rest_status=? " );
        // query.append( ",  rest_reserve_no=? " );
        // query.append( ",  rest_reserve_temp_no=? " );
        // query.append( ",  rest_reserve_limit_day=? " );
        // query.append( ",  rest_reserve_limit_time=? " );
        query.append( " WHERE id=? AND cal_date=? AND seq=? " );
        // query.append( "   AND ( stay_reserve_temp_no = 0 " );
        // // query.append( "     OR stay_reserve_temp_no = '' " );
        // query.append( "     OR ( " );
        // query.append( "       stay_reserve_limit_day < ? " );
        // query.append( "       OR ( " );
        // query.append( "         stay_reserve_limit_day = ? " );
        // query.append( "         AND stay_reserve_limit_time < ? " );
        // query.append( "       ) " );
        // query.append( "     ) " );
        // query.append( "   ) " );
        // query.append( "   AND ( rest_reserve_temp_no = 0 " );
        // // query.append( "     OR rest_reserve_temp_no = '' " );
        // query.append( "     OR ( " );
        // query.append( "       rest_reserve_limit_day < ? " );
        // query.append( "       OR ( " );
        // query.append( "         rest_reserve_limit_day = ? " );
        // query.append( "         AND rest_reserve_limit_time < ? " );
        // query.append( "       ) " );
        // query.append( "     ) " );
        // query.append( "   ) " );

        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query.toString() );
            // 更新対象の値をセットする
            int i = 1;
            prestate.setInt( i++, this.stayStatus );
            // prestate.setString( i++, this.stayReserveNo );
            // prestate.setLong( i++, this.stayReserveTempNo );
            // prestate.setInt( i++, this.stayReserveLimitDay );
            // prestate.setInt( i++, this.stayReserveLimitTime );
            prestate.setInt( i++, this.restStatus );
            // prestate.setString( i++, this.restReserveNo );
            // prestate.setLong( i++, this.restReserveTempNo );
            // prestate.setInt( i++, this.restReserveLimitDay );
            // prestate.setInt( i++, this.restReserveLimitTime );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.calDate );
            prestate.setInt( i++, this.seq );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            int result = prestate.executeUpdate();
            if ( result > 0 )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.updateData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
    }

    public boolean updateData(Connection connection, int start, int end)
    {
        StringBuilder query = new StringBuilder();

        query.append( "UPDATE newRsvDB.hh_rsv_room_remainder SET " );
        query.append( " stay_status=? " );
        // query.append( ", stay_reserve_no=? " );
        query.append( ", rest_status=? " );
        // query.append( ", rest_reserve_no=? " );
        query.append( " WHERE id=? AND ( cal_date BETWEEN ? AND ? ) AND seq=? " );
        // query.append( "   AND ( stay_reserve_temp_no = 0 " );
        // // query.append( "     OR stay_reserve_temp_no = '' " );
        // query.append( "     OR ( " );
        // query.append( "       stay_reserve_limit_day < ? " );
        // query.append( "       OR ( " );
        // query.append( "         stay_reserve_limit_day = ? " );
        // query.append( "         AND stay_reserve_limit_time < ? " );
        // query.append( "       ) " );
        // query.append( "     ) " );
        // query.append( "   ) " );
        // query.append( "   AND ( rest_reserve_temp_no = 0 " );
        // // query.append( "     OR rest_reserve_temp_no = '' " );
        // query.append( "     OR ( " );
        // query.append( "       rest_reserve_limit_day < ? " );
        // query.append( "       OR ( " );
        // query.append( "         rest_reserve_limit_day = ? " );
        // query.append( "         AND rest_reserve_limit_time < ? " );
        // query.append( "       ) " );
        // query.append( "     ) " );
        // query.append( "   ) " );

        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query.toString() );
            // 更新対象の値をセットする
            int i = 1;
            prestate.setInt( i++, this.stayStatus );
            // prestate.setString( i++, this.stayReserveNo );
            prestate.setInt( i++, this.restStatus );
            // prestate.setString( i++, this.restReserveNo );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, start );
            prestate.setInt( i++, end );
            prestate.setInt( i++, this.seq );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            // prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );

            int result = prestate.executeUpdate();
            if ( result > 0 )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.updateData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
    }

    public List<Integer> checkStopableRoom(Connection connection, int start, int end) throws Exception
    {
        StringBuilder query = new StringBuilder();

        query.append( " SELECT cal_date " );
        query.append( " FROM newRsvDB.hh_rsv_room_remainder " );
        query.append( " WHERE " );
        query.append( "   id = ? AND " );
        query.append( "   seq = ? AND " );
        query.append( "   cal_date BETWEEN ? AND ? AND " );
        query.append( "   ( stay_reserve_temp_no <> 0 AND ( stay_reserve_limit_day > ? OR stay_reserve_limit_day = ? AND stay_reserve_limit_time > ? ) OR " );
        query.append( "     rest_reserve_temp_no <> 0 AND ( rest_reserve_limit_day > ? OR rest_reserve_limit_day = ? AND rest_reserve_limit_time > ? ) OR " );
        query.append( "     cal_date = ? AND ( stay_status = 2 OR rest_status = 2 )" );
        query.append( "   ) " );

        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            prestate = connection.prepareStatement( query.toString() );
            int today = Integer.parseInt( DateEdit.getDate( 2 ) );
            int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            int i = 1;
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.seq );
            prestate.setInt( i++, start );
            prestate.setInt( i++, end );
            prestate.setInt( i++, today );
            prestate.setInt( i++, today );
            prestate.setInt( i++, nowTime );
            prestate.setInt( i++, today );
            prestate.setInt( i++, today );
            prestate.setInt( i++, nowTime );
            prestate.setInt( i++, today );

            result = prestate.executeQuery();
            List<Integer> disableList = new ArrayList<Integer>();
            while( result.next() != false )
            {
                disableList.add( result.getInt( "cal_date" ) );
            }
            return disableList;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvRoomRemainder.checkStopableRoom] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

    }

}
