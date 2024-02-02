package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Tue Nov 16 13:50:58 JST 2010
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * DataUserFelica
 * 
 * @author tashiro-s1
 * @version 1.0
 */
public class DataUserFelica implements Serializable
{

    private String userId;
    private String idm;
    private int    registDate;
    private int    registTime;
    private int    id;
    private int    delFlag;

    public DataUserFelica()
    {
        userId = "";
        idm = "";
        registDate = 0;
        registTime = 0;
        id = 0;
        delFlag = 0;
    }

    public String getUserId()
    {
        return this.userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getIdm()
    {
        return this.idm;
    }

    public void setIdm(String idm)
    {
        this.idm = idm;
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

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getDelFlag()
    {
        return this.delFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    /**
     * ユーザフェリカID情報取得
     * 
     * @param userId ユーザーID
     * @param idm フェリカID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_felica WHERE user_id = ?";

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
                    this.idm = result.getString( "idm" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.id = result.getInt( "id" );
                    this.delFlag = result.getInt( "del_flag" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserFelica.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( userId.compareTo( this.userId ) == 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ユーザフェリカID情報取得
     * 
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserData(String idm)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_felica.* FROM hh_user_felica, hh_user_basic WHERE hh_user_basic.user_id = hh_user_felica.user_id" +
                " AND hh_user_basic.regist_status=9 AND hh_user_basic.del_flag = 0 " +
                " AND hh_user_felica.del_flag=0 AND hh_user_felica.idm = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, idm );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.idm = result.getString( "idm" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.id = result.getInt( "id" );
                    this.delFlag = result.getInt( "del_flag" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserFelica.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( userId.compareTo( "" ) != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ユーザフェリカID情報取得
     * 
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserDataNoCheck(String idm)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_felica WHERE  hh_user_felica.idm = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, idm );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.idm = result.getString( "idm" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.id = result.getInt( "id" );
                    this.delFlag = result.getInt( "del_flag" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserFelica.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( userId.compareTo( "" ) != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ユーザフェリカID情報取得
     * 
     * @param result ユーザフェリカID情報取得データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.idm = result.getString( "idm" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.id = result.getInt( "id" );
                this.delFlag = result.getInt( "del_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserFelica.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザフェリカID情報取得
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

        query = "INSERT hh_user_felica SET ";
        query += " user_id = ?,";
        query += " idm = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " id = ?,";
        query += " del_flag = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.idm );
            prestate.setInt( 3, this.registDate );
            prestate.setInt( 4, this.registTime );
            prestate.setInt( 5, this.id );
            prestate.setInt( 6, this.delFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserFelica.insertData] Exception=" + e.toString() );
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
     * ユーザフェリカID情報取得
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザーID
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

        query = "UPDATE hh_user_felica SET ";
        query += " user_id = ?,";
        query += " idm = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " id = ?,";
        query += " del_flag = ?";
        query += " WHERE user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.idm );
            prestate.setInt( 3, this.registDate );
            prestate.setInt( 4, this.registTime );
            prestate.setInt( 5, this.id );
            prestate.setInt( 6, this.delFlag );
            prestate.setString( 7, userId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserFelica.updateData] Exception=" + e.toString() );
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
     *ユーザフェリカID情報削除
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        if ( userId != null )
        {
            if ( userId.compareTo( "" ) != 0 )
            {
                query = "DELETE FROM hh_user_felica WHERE user_id = ?";

                try
                {
                    connection = DBConnection.getConnection();
                    prestate = connection.prepareStatement( query );
                    // 更新対象の値をセットする
                    prestate.setString( 1, userId );
                    result = prestate.executeUpdate();
                    if ( result > 0 )
                    {
                        ret = true;
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[DataUserFelica.deleteData] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( prestate );
                    DBConnection.releaseResources( connection );
                }

            }
        }
        return(ret);
    }

    /**
     *ユーザフェリカID情報削除
     * 
     * @param idm フェリカID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean deleteDataByIdm(String idm)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        if ( idm != null )
        {
            if ( idm.compareTo( "" ) != 0 )
            {
                query = "DELETE FROM hh_user_felica WHERE idm = ?";

                try
                {
                    connection = DBConnection.getConnection();
                    prestate = connection.prepareStatement( query );
                    // 更新対象の値をセットする
                    prestate.setString( 1, idm );
                    result = prestate.executeUpdate();
                    if ( result > 0 )
                    {
                        ret = true;
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[DataUserFelica.deleteDataByIdm] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( prestate );
                    DBConnection.releaseResources( connection );
                }

            }
        }
        return(ret);
    }

}
