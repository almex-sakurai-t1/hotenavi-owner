package jp.happyhotel.owner;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 *
 *
 *
 */
public class BkoClosingRunnerBill
{
    public static boolean main(String[] args)
    {
        String driver;
        String connurl;
        String user;
        String password;
        Properties prop;
        Connection connect = null;
        boolean rtn = false;

        // 引数＝対象月、仮締め・本締め区分

        try
        {
            // データベースの初期化
            prop = new Properties();
            // ﾌﾟﾛﾊﾟﾃｨﾌｧｲﾙからｷｰと値のﾘｽﾄを読み込みます
            prop.load( new FileInputStream( "/etc/happyhotel/dbconnect.conf" ) );
            // "jdbc.driver"に設定されている値を取得します
            driver = prop.getProperty( "jdbc.driver" );
            // "jdbc.url"に設定されている値を取得します
            connurl = prop.getProperty( "jdbc.url" );
            // "jdbc.user"に設定されている値を取得します
            user = prop.getProperty( "jdbc.user" );
            // "jdbc.url"に設定されている値を取得します
            password = prop.getProperty( "jdbc.password" );
            prop.clear();

            // prop.load( new FileInputStream( "/etc/happyhotel/reserve.conf" ) );
            // elapseDays = Integer.parseInt(prop.getProperty( "temp.coming.limit.range"));

            // MySQLへの接続を確立する
            Class.forName( driver );
            // MySQLへ接続する
            connect = DriverManager.getConnection( connurl, user, password );
            connect.setAutoCommit( false );

            // 処理実行
            LogicOwnerBkoClosingBill bkoClosingBill = new LogicOwnerBkoClosingBill();
            rtn = bkoClosingBill.execute( connect, args );

        }
        catch ( Exception e )
        {
            System.out.println( "DB Init Error" );
            Logging.error( "BkoClosingRunner DB Init Error " + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connect );
        }
        return rtn;
    }
}
