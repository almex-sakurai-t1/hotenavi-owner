/*
 * 部屋残数データクラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvRoomRemainder implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 3527059023335999124L;

    private int               iD;
    private int               calDate;
    private int               seq;
    private int               status;
    private String            reserveNo;
    private int               planType;

    /**
     * データの初期化
     */
    public DataRsvRoomRemainder()
    {
        iD = 0;
        calDate = 0;
        seq = 0;
        status = 0;
        reserveNo = "";
        planType = 0;
    }

    // getter
    public int getID()
    {
        return this.iD;
    }

    public int getCaldate()
    {
        return this.calDate;
    }

    public String getReserveNo()
    {
        return this.reserveNo;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public int getStatus()
    {
        return this.status;
    }

    public int getPlanType()
    {
        return this.planType;
    }

    // setter
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setCalDate(int calDate)
    {
        this.calDate = calDate;
    }

    public void setReserveNo(String rsvno)
    {
        this.reserveNo = rsvno;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setPlanType(int planType)
    {
        this.planType = planType;
    }

    /**
     * 部屋残数データ情報取得
     * 
     * @param iD ホテルID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getDate(int Id)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, cal_date, seq, status, reserve_no " +
                " FROM hh_rsv_room_remainder WHERE id = ? ";

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
                    this.calDate = result.getInt( "cal_date" );
                    this.seq = result.getInt( "seq" );
                    this.status = result.getInt( "status" );
                    this.reserveNo = result.getString( "reserve_no" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRoomRemainder.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 部屋残数データ情報取得
     * 
     * @param id ホテルID
     * @param calDate 予約日
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int id, int calDate, int seq)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, cal_date, seq, status, reserve_no " +
                " FROM hh_rsv_room_remainder WHERE id = ? " +
                " AND cal_date = ? AND seq = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, calDate );
            prestate.setInt( 3, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.calDate = result.getInt( "cal_date" );
                    this.seq = result.getInt( "seq" );
                    this.status = result.getInt( "status" );
                    this.reserveNo = result.getString( "reserve_no" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRoomRemainder.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 部屋残数取得 (部屋指定あり)
     * 
     * @param iD ホテルID
     * @param planId プランID
     * @param seq 管理番号
     * @param calDate 日付
     * @return 件数(int)
     */
    public int getRemainderCount(int Id, int planId, int seq, int calDate)
    {
        // 変数定義
        int retCnt; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        retCnt = 0;

        query = "SELECT COUNT(*) AS CNT FROM hh_rsv_room_remainder rrr  " +
                "INNER JOIN hh_rsv_rel_plan_room rrpr ON " +
                "     ( rrpr.id = rrr.id " +
                "   AND rrpr.seq = rrr.seq ) " +
                "INNER JOIN hh_rsv_room rr ON " +
                "     ( rrr.id = rr.id " +
                "   AND rrr.seq = rr.seq ) " +
                " WHERE rrpr.id = ? " +
                "   AND rrpr.plan_id = ? " +
                "   AND rrpr.seq = ? " +
                "   AND rrr.cal_date = ? " +
                "   AND rrr.status = 1 " +
                "   AND rr.sales_flag = 1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, seq );
            prestate.setInt( 4, calDate );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    retCnt = result.getInt( "CNT" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRoomRemainder.getRemainderCount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(retCnt);
    }

    /**
     * 部屋残数取得 (部屋指定無し)
     * 
     * @param iD ホテルID
     * @param planId プランID
     * @param calDate 日付
     * @return 件数(int)
     */
    public int getRemainderSumCount(int Id, int planId, int calDate)
    {
        // 変数定義
        int retCnt; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        retCnt = 0;

        query = "SELECT COUNT(*) AS CNT FROM hh_rsv_room_remainder rrr  " +
                "INNER JOIN hh_rsv_rel_plan_room rrpr ON " +
                "     ( rrpr.id = rrr.id " +
                "   AND rrpr.seq = rrr.seq ) " +
                "INNER JOIN hh_rsv_room rr ON " +
                "     ( rrr.id = rr.id " +
                "   AND rrr.seq = rr.seq ) " +
                " WHERE rrpr.id = ? " +
                "   AND rrpr.plan_id = ? " +
                "   AND rrr.cal_date = ? " +
                "   AND rrr.status = 1 " +
                "   AND rr.sales_flag = 1 ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, calDate );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    retCnt = result.getInt( "CNT" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRoomRemainder.getRemainderSumCount] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(retCnt);
    }

    /**
     * 部屋残数データ設定
     * 
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;

        try
        {
            if ( result != null )
            {
                this.iD = result.getInt( "id" );
                this.calDate = result.getInt( "cal_data" );
                this.seq = result.getInt( "seq" );
                this.status = result.getInt( "status" );
                this.reserveNo = result.getString( "reserve_no" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRoomRemainder.setData] Exception=" + e.toString() );
        }
        return(ret);

    }

    /**
     * 部屋残数データ登録
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

        query = "INSERT INTO hh_rsv_room_remainder SET " +
                "  id = ?" +
                ", cal_date = ?" +
                ", seq = ? " +
                ", status = ? " +
                ", reserve_no = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.iD );
            prestate.setInt( 2, this.calDate );
            prestate.setInt( 3, this.seq );
            prestate.setInt( 4, this.status );
            prestate.setString( 5, this.reserveNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRoomRemainder.insertData] Exception=" + e.toString() );
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
     * 部屋残数データ登録
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection conn)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_room_remainder SET " +
                "  id = ?" +
                ", cal_date = ?" +
                ", seq = ? " +
                ", status = ? " +
                ", reserve_no = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.iD );
            prestate.setInt( 2, this.calDate );
            prestate.setInt( 3, this.seq );
            prestate.setInt( 4, this.status );
            prestate.setString( 5, this.reserveNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRoomRemainder.insertData] Exception=" + e.toString() );
            ret = false;
        }
        return(ret);

    }

    /**
     * 部屋残数データ更新
     * 
     * @param iD ホテルID
     * @param calDate 日付
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int iD, int calDate, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_room_remainder SET " +
                "  status = ? ";
        query = query + " WHERE id = ? AND cal_date = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.status );

            prestate.setInt( 2, iD );
            prestate.setInt( 3, calDate );
            prestate.setInt( 4, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvRoomRemainder.updateData] Exception=" + e.toString() );
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
     * 部屋残数データ更新
     * 
     * @param conn DBConnection
     * @param iD ホテルID
     * @param calDate 日付
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    /*
     * public boolean updateData(Connection conn, int iD, int calDate, int seq)
     * {
     * int result;
     * boolean ret;
     * String query;
     * PreparedStatement prestate = null;
     * ret = false;
     * query = "UPDATE hh_rsv_room_remainder SET " +
     * "  status = ? , reserve_no = ? ";
     * query = query + " WHERE id = ? AND cal_date = ? AND seq = ?";
     * try
     * {
     * prestate = conn.prepareStatement( query );
     * // 更新対象の値をセットする
     * prestate.setInt( 1, this.status );
     * prestate.setString( 2, this.reserveNo );
     * prestate.setInt( 3, iD );
     * prestate.setInt( 4, calDate );
     * prestate.setInt( 5, seq );
     * result = prestate.executeUpdate();
     * if ( result > 0 )
     * {
     * ret = true;
     * }
     * }
     * catch ( Exception e )
     * {
     * Logging.error( "[DataRsvRoomRemainder.updateData] Exception=" + e.toString() );
     * ret = false;
     * }
     * return(ret);
     * }
     */

}
