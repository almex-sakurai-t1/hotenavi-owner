package jp.happyhotel.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataOwnerUserLock
{
    private String hotelId;
    private int    userId;
    private int    lockStatus;
    private int    lockDate;
    private int    lockTime;
    private int    mistakeCount;
    private int    mistakeDate;
    private int    mistakeTime;

    public String getHotelId()
    {
        return hotelId;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getLockStatus()
    {
        return lockStatus;
    }

    public void setLockStatus(int lockStatus)
    {
        this.lockStatus = lockStatus;
    }

    public int getLockDate()
    {
        return lockDate;
    }

    public void setLockDate(int lockDate)
    {
        this.lockDate = lockDate;
    }

    public int getLockTime()
    {
        return lockTime;
    }

    public void setLockTime(int lockTime)
    {
        this.lockTime = lockTime;
    }

    public int getMistakeCount()
    {
        return mistakeCount;
    }

    public void setMistakeCount(int mistakeCount)
    {
        this.mistakeCount = mistakeCount;
    }

    public int getMistakeDate()
    {
        return mistakeDate;
    }

    public void setMistakeDate(int mistakeDate)
    {
        this.mistakeDate = mistakeDate;
    }

    public int getMistakeTime()
    {
        return mistakeTime;
    }

    public void setMistakeTime(int mistakeTime)
    {
        this.mistakeTime = mistakeTime;
    }

    /**
     * オーナーサイトログインロック情報取得
     * 
     * @param hotelId ホテルID
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String hotelId, int userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM owner_user_lock WHERE hotelid = ? and userid = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                return setData( result );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOwnerUserLock.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return false;
    }

    /**
     * オーナーサイトログインロック情報設定
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.hotelId = result.getString( "hotelid" );
                this.userId = result.getInt( "userid" );
                this.lockStatus = result.getInt( "lock_status" );
                this.lockDate = result.getInt( "lock_date" );
                this.lockTime = result.getInt( "lock_time" );
                this.mistakeCount = result.getInt( "mistake_count" );
                this.mistakeDate = result.getInt( "mistake_date" );
                this.mistakeTime = result.getInt( "mistake_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOwnerUserLock.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * オーナーサイトログインロックデータ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
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

        query = "INSERT owner_user_lock SET ";
        query = query + " hotelid = ?,";
        query = query + " userid = ?,";
        query = query + " lock_status = ?,";
        query = query + " lock_date = ?,";
        query = query + " lock_time = ?,";
        query = query + " mistake_count = ?,";
        query = query + " mistake_date = ?,";
        query = query + " mistake_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            prestate.setInt( 3, lockStatus );
            prestate.setInt( 4, lockDate );
            prestate.setInt( 5, lockTime );
            prestate.setInt( 6, mistakeCount );
            prestate.setInt( 7, mistakeDate );
            prestate.setInt( 8, mistakeTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOwnerUserLock.insertData] Exception=" + e.toString() );
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
     * オーナーサイトログインロックデータ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param hotelId ホテルID
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String hotelId, int userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE owner_user_lock SET ";
        query = query + " lock_status = ?,";
        query = query + " lock_date = ?,";
        query = query + " lock_time = ?,";
        query = query + " mistake_count = ?,";
        query = query + " mistake_date = ?,";
        query = query + " mistake_time = ?";
        query = query + " WHERE hotelid = ?";
        query = query + " AND userid = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, lockStatus );
            prestate.setInt( 2, lockDate );
            prestate.setInt( 3, lockTime );
            prestate.setInt( 4, mistakeCount );
            prestate.setInt( 5, mistakeDate );
            prestate.setInt( 6, mistakeTime );
            prestate.setString( 7, hotelId );
            prestate.setInt( 8, userId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOwnerUserLock.updateData] Exception=" + e.toString() );
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
