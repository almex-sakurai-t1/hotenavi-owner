package jp.happyhotel.owner;

import java.io.FileInputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;

import jp.happyhotel.common.*;

/**
 * 
 * 
 *
 */
public class ReserveTempComingRunner
{
    public static void main( String[] args )
    {
        String driver;
        String connurl;
        String user;
        String password;
        Properties prop;
        Connection connect = null;
        int elapseDays = 0;        //経過日数

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
            
            prop.load( new FileInputStream( "/etc/happyhotel/reserve.conf" ) );
            elapseDays = Integer.parseInt(prop.getProperty( "temp.coming.limit.range"));

            // MySQLへの接続を確立する
            Class.forName( driver );
            // MySQLへ接続する
            connect = DriverManager.getConnection( connurl, user, password );
            
            //処理実行
            LogicReserveTempComing rsvTempComing = new LogicReserveTempComing();
            rsvTempComing.execute(connect, elapseDays);

        }
        catch ( Exception e )
        {
            System.out.println("DB Init Error");
            Logging.error("ReserveTempComingRunner DB Init Error " + e.toString());
        }
        finally
        {
            DBConnection.releaseResources(connect);
        }
    }
}