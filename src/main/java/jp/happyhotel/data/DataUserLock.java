package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataUserLock implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 6675605603241371219L;
    public final static int   KIND_MINUTES     = 4;
    public final static int   AVAILABLE_MINUTE = 5;
    public final static int   LOCK_COUNT       = 10;
    public final static int   LOCK             = 1;
    public final static int   RELEASE          = 0;
    private String            userId;
    private int               lockStatus;
    private int               lockDate;
    private int               lockTime;
    private int               mistakeCount;
    private int               mistakeDate;
    private int               mistakeTime;

    public DataUserLock()
    {
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
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
     * ユーザスマホ課金情報取得
     * 
     * @param userId ユーザユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_lock WHERE user_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.lockStatus = result.getInt( "lock_status" );
                    this.lockDate = result.getInt( "lock_date" );
                    this.lockTime = result.getInt( "lock_time" );
                    this.mistakeCount = result.getInt( "mistake_count" );
                    this.mistakeDate = result.getInt( "mistake_date" );
                    this.mistakeTime = result.getInt( "mistake_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserLock.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザスマホ課金データ設定
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
                this.userId = result.getString( "user_id" );
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
            Logging.error( "[DataUserLock.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザスマホ課金データ追加
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

        query = "INSERT hh_user_lock SET ";
        query = query + " user_id = ?,";
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
            prestate.setString( 1, userId );
            prestate.setInt( 2, lockStatus );
            prestate.setInt( 3, lockDate );
            prestate.setInt( 4, lockTime );
            prestate.setInt( 5, mistakeCount );
            prestate.setInt( 6, mistakeDate );
            prestate.setInt( 7, mistakeTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserLock.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        if ( ret != false )
        {
        }
        return(ret);
    }

    /**
     * ユーザスマホ課金データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_lock SET ";
        query = query + " lock_status = ?,";
        query = query + " lock_date = ?,";
        query = query + " lock_time = ?,";
        query = query + " mistake_count = ?,";
        query = query + " mistake_date = ?,";
        query = query + " mistake_time = ?";
        query = query + " WHERE user_id = ?";

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
            prestate.setString( 7, userId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserLock.updateData] Exception=" + e.toString() );
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
