package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataUserViewHotel implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -3353951682763583321L;
    private int               registDate;
    private int               id;
    private String            userId;
    private int               registTime;

    public DataUserViewHotel()
    {
        this.registDate = 0;
        this.id = 0;
        this.userId = "";
        this.registTime = 0;
    }

    public int getRegistDate()
    {
        return this.registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return this.userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getRegistTime()
    {
        return this.registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    /**
     * ホテル閲覧履歴データ
     * 
     * @param registDate
     * @param userId
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int registDate, int id, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_view_hotel WHERE regist_date = ? AND id = ? AND user_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, registDate );
            prestate.setInt( 2, id );
            prestate.setString( 3, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.registDate = result.getInt( "regist_date" );
                    this.id = result.getInt( "id" );
                    this.userId = result.getString( "user_id" );
                    this.registTime = result.getInt( "regist_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserViewHotel.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテル閲覧履歴データ
     * 
     * @param registDate
     * @param userId
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int registDate, int id, String userId, Connection connection)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_view_hotel WHERE regist_date = ? AND id = ? AND user_id = ?";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, registDate );
            prestate.setInt( 2, id );
            prestate.setString( 3, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.registDate = result.getInt( "regist_date" );
                    this.id = result.getInt( "id" );
                    this.userId = result.getString( "user_id" );
                    this.registTime = result.getInt( "regist_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserViewHotel.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( result );
        }
        return(ret);
    }

    /**
     * ホテル閲覧履歴データ
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.registDate = result.getInt( "regist_date" );
                this.id = result.getInt( "id" );
                this.userId = result.getString( "user_id" );
                this.registTime = result.getInt( "regist_time" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserViewHotel.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    public boolean setViewHotelData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.userId = result.getString( "user_id" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserViewHotel.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ホテル認証データ追加
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

        query = "INSERT hh_user_view_hotel SET ";
        query += " regist_date = ?,";
        query += " id = ?,";
        query += " user_id = ?,";
        query += " regist_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.registDate );
            prestate.setInt( 2, this.id );
            prestate.setString( 3, this.userId );
            prestate.setInt( 4, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserViewHotel.insertData] Exception=" + e.toString() );
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
     * ホテル認証データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection connection)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_user_view_hotel SET ";
        query += " regist_date = ?,";
        query += " id = ?,";
        query += " user_id = ?,";
        query += " regist_time = ?";

        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.registDate );
            prestate.setInt( 2, this.id );
            prestate.setString( 3, this.userId );
            prestate.setInt( 4, this.registTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserViewHotel.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /**
     * ホテル認証データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param licencekey ライセンスキー
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int registDate, int id, String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_auth SET ";
        query += " regist_time = ?";
        query += " WHERE  regist_date = ?";
        query += " AND id = ?";
        query += " AND user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, registDate );
            prestate.setInt( 2, id );
            prestate.setString( 3, userId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserViewHotel.updateData] Exception=" + e.toString() );
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
