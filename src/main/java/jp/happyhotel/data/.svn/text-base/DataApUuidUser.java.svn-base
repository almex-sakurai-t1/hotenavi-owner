package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataApUuidUser implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -7928146577233822562L;
    private String            uuid;
    private String            userId;
    private int               appStatus;
    private int               registDate;
    private int               registTime;
    private int               updateDate;
    private int               updateTime;

    public DataApUuidUser()
    {
        this.uuid = "";
        this.userId = "";
        appStatus = 0;
        this.registDate = 0;
        this.registTime = 0;
        this.updateDate = 0;
        this.updateTime = 0;
    }

    public String getUuid()
    {
        return this.uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUserId()
    {
        return this.userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getAppStatus()
    {
        return this.appStatus;
    }

    public void setAppStatus(int appStatus)
    {
        this.appStatus = appStatus;
    }

    public int getRegistDate()
    {
        return this.registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return this.registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public int getUpdateDate()
    {
        return this.updateDate;
    }

    public void setUpdateDate(int updateDate)
    {
        this.updateDate = updateDate;
    }

    public int getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(int updateTime)
    {
        this.updateTime = updateTime;
    }

    public boolean getData(String uuid)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_uuid_user WHERE uuid = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, uuid );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.uuid = result.getString( "uuid" );
                    this.userId = result.getString( "user_id" );
                    this.appStatus = result.getInt( "app_status" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidUser.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    public boolean getAppData(String user_id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_uuid_user WHERE user_id = ?";
        query += " ORDER BY update_date DESC,update_time DESC";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, user_id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.uuid = result.getString( "uuid" );
                    this.userId = result.getString( "user_id" );
                    this.appStatus = result.getInt( "app_status" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidUser.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    public boolean getData(String uuid, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_uuid_user WHERE uuid = ? AND user_id=? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, uuid );
            prestate.setString( 2, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.uuid = result.getString( "uuid" );
                    this.userId = result.getString( "user_id" );
                    this.appStatus = result.getInt( "app_status" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.updateDate = result.getInt( "update_date" );
                    this.updateTime = result.getInt( "update_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidUser.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.uuid = result.getString( "uuid" );
                this.userId = result.getString( "user_id" );
                this.appStatus = result.getInt( "app_status" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.updateDate = result.getInt( "update_date" );
                this.updateTime = result.getInt( "update_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidUser.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataApUuidUserHistory dauuh;
        ret = false;

        query = "INSERT ap_uuid_user SET ";
        query += " uuid=?";
        query += ", user_id=?";
        query += ", app_status=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.uuid );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.appStatus );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidUser.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        /* DB登録後、履歴に追加する */
        if ( ret != false )
        {
            try
            {
                dauuh = new DataApUuidUserHistory();
                dauuh.setUuid( uuid );
                dauuh.setUserId( userId );
                dauuh.insertData();
            }
            catch ( Exception e )
            {
                Logging.error( "[DataApUuidUserHistory.insertData] Exception=" + e.toString() );
                ret = false;
            }
        }
        return(ret);
    }

    public boolean updateData(String uuid, String userId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        DataApUuidUserHistory dauuh;
        ret = false;
        query = "UPDATE ap_uuid_user SET ";
        query += " user_id=?";
        query += ", app_status=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        query += " WHERE uuid=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, userId );
            prestate.setInt( i++, this.appStatus );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setString( i++, uuid );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidUser.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        /* DB登録後、履歴に追加する */
        if ( ret != false )
        {
            try
            {
                dauuh = new DataApUuidUserHistory();
                dauuh.setUuid( uuid );
                dauuh.setUserId( userId );
                dauuh.insertData();
            }
            catch ( Exception e )
            {
                Logging.error( "[DataApUuidUserHistory.insertData] Exception=" + e.toString() );
                ret = false;
            }
        }
        return(ret);
    }

}
