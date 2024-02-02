package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * ログイン履歴（hh_user_login_history）取得クラス
 *
 * @author an-j1
 * @version 1.00 2019/04/23
 */
public class DataHhUserLoginHistory implements Serializable
{
    public static final String TABLE = "hh_user_login_history";
    private String           userId;                      // ユーザーID
    private int                loginDate;                // ログイン日
    private int                loginTime;                // ログイン時刻
    private int                loginKind;                 // ログイン区分（0：ハピホテログイン、1：ソーシャルログイン）
    private int                loginTerminal;           // ログイン端末（0：統合アプリ、1：スマホWEB、2：PCWEB、3：予約アプリ、4：検索アプリ）
    private String           userAgent;                // ユーザーエージェント

    /**
     * データを初期化します。
     */
    public DataHhUserLoginHistory()
    {
        this.userId = "";
        this.loginDate = 0;
        this.loginTime = 0;
        this.loginKind = 0;
        this.loginTerminal = 0;
        this.userAgent = "";
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(int loginDate)
    {
        this.loginDate = loginDate;
    }

    public int getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(int loginTime)
    {
        this.loginTime = loginTime;
    }

    public int getLoginKind()
    {
        return loginKind;
    }

    public void setLoginKind(int loginKind)
    {
        this.loginKind = loginKind;
    }

    public int getLoginTerminal()
    {
        return loginTerminal;
    }

    public void setLoginTerminal(int loginTerminal)
    {
        this.loginTerminal = loginTerminal;
    }

    public String getUserAgent()
    {
        return userAgent;
    }


    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }


    /****
     * ログイン履歴（hh_user_login_history）取得
     *
     * @param userId user_id
     * @return
     */
    public boolean getData(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM hh_user_login_history WHERE user_id = ? ";
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
                    this.loginDate = result.getInt( "login_date" );
                    this.loginTime = result.getInt( "login_time" );
                    this.loginKind = result.getInt( "login_kind" );
                    this.loginTerminal = result.getInt( "login_terminal" );
                    this.userAgent = result.getString( "user_agent" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhUserLoginHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * ログイン履歴（hh_user_login_history）取得
     *
     * @param userId user_id
     * @param loginDate ログイン日
     * @param loginTime ログイン時刻
     * @return
     */
    public boolean getData(String userId, int loginDate, int loginTime)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM hh_user_login_history ";
        query += " WHERE user_id = ? ";
        query += " AND login_date = ? ";
        query += " AND login_time = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, loginDate );
            prestate.setInt( 3, loginTime );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.loginDate = result.getInt( "login_date" );
                    this.loginTime = result.getInt( "login_time" );
                    this.loginKind = result.getInt( "login_kind" );
                    this.loginTerminal = result.getInt( "login_terminal" );
                    this.userAgent = result.getString( "user_agent" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhUserLoginHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ログイン履歴（hh_user_login_history）設定
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
                this.userId = result.getString( "user_id" );
                this.loginDate = result.getInt( "login_date" );
                this.loginTime = result.getInt( "login_time" );
                this.loginKind = result.getInt( "login_kind" );
                this.loginTerminal = result.getInt( "login_terminal" );
                this.userAgent = result.getString( "user_agent" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhUserLoginHistory.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ログイン履歴（hh_user_login_history）挿入
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

        query = "INSERT hh_user_login_history SET ";
        query += " user_id=?";
        query += ", login_date=?";
        query += ", login_time=?";
        query += ", login_kind=?";
        query += ", login_terminal=?";
        query += ", user_agent=?";
        query += " ON DUPLICATE KEY UPDATE";
        query += " login_kind = ?";
        query += ", login_terminal = ?";
        query += ", user_agent = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( i++, this.loginKind );
            prestate.setInt( i++, this.loginTerminal );
            prestate.setString( i++, this.userAgent );
            prestate.setInt( i++, this.loginKind );
            prestate.setInt( i++, this.loginTerminal );
            prestate.setString( i++, this.userAgent );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhUserLoginHistory.insertData] Exception=" + e.toString() );
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
     * ログイン履歴（hh_user_login_history）更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId user_id
     * @param loginDate ログイン日
     * @param loginTime ログイン時刻
     * @return
     */
    public boolean updateData(String userId, int loginDate, int loginTime)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE hh_user_login_history SET ";
        query += " login_kind=?";
        query += ", login_terminal=?";
        query += ", user_agent=?";
        query += " WHERE user_id=?";
        query += "  AND login_date=?";
        query += "  AND login_time=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.loginKind );
            prestate.setInt( i++, this.loginTerminal );
            prestate.setString( i++, this.userAgent );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.loginDate );
            prestate.setInt( i++, this.loginTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhUserLoginHistory.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    public static String getTable()
    {
        return TABLE;
    }

}
