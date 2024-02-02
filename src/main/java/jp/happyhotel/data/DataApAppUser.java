package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * アプリユーザー(ap_app_user)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/19
 */
public class DataApAppUser implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -8088933554255704294L;
    public static final String TABLE            = "ap_app_user";
    private String             appUserId;                               // SstouchUserId（アプリインストールごとに書き換わる）
    private String             userId;                                  // ユーザーID
    private int                registDate;                              // 登録日付（YYYYMMDD)
    private int                registTime;                              // 登録時刻（HHMMSS)
    private int                updateDate;                              // 変更日付（YYYYMMDD)
    private int                updateTime;                              // 変更時刻（HHMMSS)

    /**
     * データを初期化します。
     */
    public DataApAppUser()
    {
        this.appUserId = "";
        this.userId = "";
        this.registDate = 0;
        this.registTime = 0;
        this.updateDate = 0;
        this.updateTime = 0;
    }

    public String getAppUserId()
    {
        return appUserId;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getUpdateDate()
    {
        return updateDate;
    }

    public int getUpdateTime()
    {
        return updateTime;
    }

    public void setAppUserId(String appUserId)
    {
        this.appUserId = appUserId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setUpdateDate(int updateDate)
    {
        this.updateDate = updateDate;
    }

    public void setUpdateTime(int updateTime)
    {
        this.updateTime = updateTime;
    }

    /****
     * アプリユーザー(ap_app_user)取得
     * 
     * @param appUserId SstouchUserId（アプリインストールごとに書き換わる）
     * @param userId ユーザーID
     * @return
     */
    public boolean getData(String appUserId, String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_app_user WHERE app_user_id = ? AND user_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, appUserId );
            prestate.setString( 2, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.appUserId = result.getString( "app_user_id" );
                    this.userId = result.getString( "user_id" );
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
            Logging.error( "[DataApAppUser.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * アプリユーザー(ap_app_user)取得
     * 
     * @param appUserId SstouchUserId（アプリインストールごとに書き換わる）
     * @param userId ユーザーID
     * @return
     */
    public boolean getData(String appUserId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_app_user WHERE app_user_id = ? ORDER BY update_date DESC, update_time DESC ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, appUserId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.appUserId = result.getString( "app_user_id" );
                    this.userId = result.getString( "user_id" );
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
            Logging.error( "[DataApAppUser.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * アプリユーザー(ap_app_user)取得
     * 
     * @param appUserId SstouchUserId（アプリインストールごとに書き換わる）
     * @param userId ユーザーID
     * @return
     */
    public boolean getUserData(String appUserId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT ap_app_user.* FROM ap_app_user" +
                " INNER JOIN hh_user_basic ON ap_app_user.user_id = hh_user_basic.user_id  " +
                " AND hh_user_basic.regist_status=9 AND hh_user_basic.del_flag = 0" +
                " WHERE ap_app_user.app_user_id = ? ORDER BY ap_app_user.update_date DESC, ap_app_user.update_time DESC ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, appUserId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.appUserId = result.getString( "app_user_id" );
                    this.userId = result.getString( "user_id" );
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
            Logging.error( "[DataApAppUser.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * アプリユーザー(ap_app_user)設定
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
                this.appUserId = result.getString( "app_user_id" );
                this.userId = result.getString( "user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.updateDate = result.getInt( "update_date" );
                this.updateTime = result.getInt( "update_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAppUser.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * アプリユーザー(ap_app_user)挿入
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

        query = "INSERT ap_app_user SET ";
        query += " app_user_id=?";
        query += ", user_id=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.appUserId );
            prestate.setString( i++, this.userId );
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
            Logging.error( "[DataApAppUser.insertData] Exception=" + e.toString() );
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
     * アプリユーザー(ap_app_user)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param appUserId SstouchUserId（アプリインストールごとに書き換わる）
     * @param userId ユーザーID
     * @return
     */
    public boolean updateData(String appUserId, String userId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_app_user SET ";
        query += " regist_date=?";
        query += ", regist_time=?";
        query += ", update_date=?";
        query += ", update_time=?";
        query += " WHERE app_user_id=? AND user_id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.updateDate );
            prestate.setInt( i++, this.updateTime );
            prestate.setString( i++, this.appUserId );
            prestate.setString( i++, this.userId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAppUser.updateData] Exception=" + e.toString() );
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
