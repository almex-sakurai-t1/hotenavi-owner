package jp.happyhotel.others;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;

public class DbAccess
{
    private String                     driver;
    private String                     url;
    private String                     user;
    private String                     password;
    private String                     jdbcds;
    private Properties                 prop;
    private int                        result;
    private ResultSet                  resultset;
    private DataSource                 ds;
    private Connection                 connect;
    private java.sql.PreparedStatement state;

    /**
     * データベースへのアクセス環境設定
     *
     */
    public DbAccess(boolean demoflag)
    {

        try
        {
            String dbconnectPath = "/etc/happyhotel/dbconnect.conf";
            if ( demoflag )
            {
                dbconnectPath = "/etc/happyhotel/demo_dbconnect.conf";
            }
            File file = new File( dbconnectPath );
            FileInputStream propfile = new FileInputStream( file );
            prop = new Properties();
            // ﾌﾟﾛﾊﾟﾃｨﾌｧｲﾙからｷｰと値のﾘｽﾄを読み込みます
            prop.load( propfile );
            // "jdbc.driver"に設定されている値を取得します
            driver = prop.getProperty( "jdbc.driver" );
            // "jdbc.url"に設定されている値を取得します
            url = prop.getProperty( "jdbc.url" );
            // "jdbc.user"に設定されている値を取得します
            user = prop.getProperty( "jdbc.user" );
            // "jdbc.url"に設定されている値を取得します
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"に設定されている値を取得します
            jdbcds = prop.getProperty( "jdbc.datasource" );

            propfile.close();
        }
        catch ( Exception e )
        {
            System.out.println( e.toString() );
        }
    }

    /**
     * データベースへのアクセス環境設定
     *
     * @param driver_param JDBCドライバ名
     * @param url_param 接続先
     * @param user_param ユーザ名
     * @param password_param パスワード
     */
    public DbAccess(String driver_param, String url_param, String user_param, String password_param)
    {
        driver = driver_param;
        url = url_param;
        user = user_param;
        password = password_param;
    }

    /**
     * データベースへのアクセス環境設定
     *
     * @param filename 設定ファイル名
     */
    public DbAccess(String filename)
    {
        try
        {
            FileInputStream propfile = new FileInputStream( filename );

            prop = new Properties();
            // ﾌﾟﾛﾊﾟﾃｨﾌｧｲﾙからｷｰと値のﾘｽﾄを読み込みます
            prop.load( propfile );
            // "jdbc.driver"に設定されている値を取得します
            driver = prop.getProperty( "jdbc.driver" );
            // "jdbc.url"に設定されている値を取得します
            url = prop.getProperty( "jdbc.url" );
            // "jdbc.user"に設定されている値を取得します
            user = prop.getProperty( "jdbc.user" );
            // "jdbc.url"に設定されている値を取得します
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"に設定されている値を取得します
            jdbcds = prop.getProperty( "jdbc.datasource" );

            propfile.close();
        }
        catch ( Exception e )
        {
        }
    }

    /**
     * SQL文セット処理(SELECT/INSERT/UPDATE)
     *
     * @param query SQL文
     * @return 処理結果(True→セット成功,False→セット失敗)
     */
    public boolean setQuery(String query)
    {
        boolean ret = false;

        try
        {
        	Class.forName( driver );
            // DBへ接続する
            connect = DriverManager.getConnection( url, user, password );

            // ステートメントの作成
            state = connect.prepareStatement( query );

            ret = true;
        }
        catch ( Exception e )
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * パラメータセット処理
     *
     * @param no パラメータセット番号
     * @param value セット値(String形式)
     */
    public void setParameter(int no, String value)
    {
        try
        {
            state.setString( no, value );
        }
        catch ( Exception e )
        {

        }

        return;
    }

    /**
     * パラメータセット処理
     *
     * @param no パラメータセット番号
     * @param value セット値(Int形式)
     */
    public void setParameter(int no, int value)
    {
        try
        {
            state.setInt( no, value );
        }
        catch ( Exception e )
        {

        }
        return;
    }

    /**
     * SQL文発行処理(SELECT)
     *
     * @return 処理結果(失敗:null)
     */
    public ResultSet execQuery()
    {
        try
        {
            // SQLの実行
            resultset = state.executeQuery();
        }
        catch ( Exception e )
        {
            resultset = null;
        }

        return(resultset);
    }

    /**
     * SQL文発行処理(INSERT,UPDATE,DELETE)
     *
     * @return 処理結果(失敗:-1)
     */
    public int execUpdate()
    {
        try
        {
            // SQLの実行
            result = state.executeUpdate();
        }
        catch ( Exception e )
        {
            result = -1;
        }

        return(result);
    }

    /**
     * データベース終了処理
     *
     */
    public void close()
    {
        try
        {
            if ( resultset != null )
            {
                resultset.close();
            }
            if ( state != null )
            {
                state.close();
            }
            if ( connect != null )
            {
                connect.close();
            }
        }
        catch ( Exception e )
        {
        }
        finally
        {
            try
            {
                if ( resultset != null )
                {
                    resultset.close();
                }
                if ( state != null )
                {
                    state.close();
                }
                if ( connect != null )
                {
                    connect.close();
                }
            }
            catch ( Exception e )
            {
            }
        }
    }
}
