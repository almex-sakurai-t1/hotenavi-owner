package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 予約排他制御
 * 予約の新規登録時に、ダブルブッキング防止用に、排他テーブルを制御する。
 * 
 * @author H.Takanami
 * @version 1.00 2011/01/18
 * @see
 */
public class LockReserve implements Serializable
{
    private static final long serialVersionUID = 9023198498622848194L;

    /**
     * ロック情報作成
     * 
     * @param hotelID ホテルID
     * @param targetDate 対象日付
     * @param seq 部屋番号
     * @return true:正常、false:異常
     */
    public static boolean Lock(int hotelID, int targetDate, int seq)
    {
        boolean ret = false;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            ret = Lock( connection, hotelID, targetDate, seq );
        }
        catch ( Exception e )
        {
            Logging.error( "[LockReserve.Lock] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public static boolean Lock(Connection connection, int hotelID, int targetDate, int seq)
    {
        int result;
        boolean ret = false;
        String query = "";
        PreparedStatement prestate = null;

        query = query + "INSERT hh_rsv_reserve_lock SET ";
        query = query + " id = ?,";
        query = query + " reserve_date = ?,";
        query = query + " seq = ?, ";
        query = query + " last_update = ?, ";
        query = query + " last_uptime = ? ";

        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, targetDate );
            prestate.setInt( 3, seq );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getTime( 1 ) ) );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LockReserve.Lock] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * ロック解除
     * 
     * @param hotelID ホテルID
     * @param targetDate 対象日付
     * @param seq 部屋番号
     * @return なし
     */
    public static void UnLock(int hotelID, int targetDate, int seq)
    {
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            UnLock( connection, hotelID, targetDate, seq );
        }
        catch ( Exception e )
        {
            Logging.error( "[LockReserve.UnLock] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    public static void UnLock(Connection connection, int hotelID, int targetDate, int seq)
    {
        String query = "";
        PreparedStatement prestate = null;

        query = query + "DELETE FROM hh_rsv_reserve_lock ";
        query = query + "WHERE id = ? ";
        query = query + "  AND reserve_date = ? ";
        query = query + "  AND seq = ? ";

        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, hotelID );
            prestate.setInt( 2, targetDate );
            prestate.setInt( 3, seq );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LockReserve.UnLock] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
    }
}
