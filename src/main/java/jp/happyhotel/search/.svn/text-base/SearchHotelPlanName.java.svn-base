package jp.happyhotel.search;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;

public class SearchHotelPlanName
{

    static String DB_URL;  // URL for database server
    static String user;    // DB user
    static String password; // DB password
    static String driver;  // DB driver
    static String jdbcds;  // DB jdbcds

    static
    {
        try
        {
            Properties prop = new Properties();
            // Linux環境
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
            // windows環境
            // FileInputStream propfile = new FileInputStream( "C:\\ALMEX\\WWW\\WEB-INF\\dbconnect.conf" );
            prop = new Properties();
            // プロパティファイルを読み込む
            prop.load( propfile );

            // "jdbc.driver"に設定されている値を取得します
            driver = prop.getProperty( "jdbc.driver" );

            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );
            // "jdbc.datasource"に設定されている値を取得します
            jdbcds = prop.getProperty( "jdbc.datasource" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            System.out.println( "CollectHotelUniquePv Static Block Error=" + e.toString() );
        }
    }

    private static Connection makeConnection()
    {
        Connection conn = null;
        try
        {
            Class.forName( driver );
            conn = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) throws Exception
    {
        updatePlanName( makeConnection() );
    }

    private final static int startKind = 301;

    public static boolean updatePlanName(Connection connection) throws Exception
    {
        boolean ret = false;
        String query = "";
        String deleteSql = " DELETE FROM hotenavi.hh_hotel_search WHERE hotenavi.hh_hotel_search.kind BETWEEN 302 AND 399";
        ResultSet result = null;
        PreparedStatement prestate = null;
        final String dateTomorrow = "CAST(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY),'%Y%m%d') as DECIMAL)";

        query = "SELECT plan.id,plan.plan_name ";
        query += "FROM newRsvDB.hh_rsv_plan plan ";
        query += " WHERE plan.latest_flag = 1 "; /* 最新のプランデータである */
        query += " AND plan.plan_sales_status = 1 "; /* 販売中 */
        query += " AND plan.sales_start_date <=" + dateTomorrow; /* 翌日以前から販売開始している */
        query += " AND (plan.sales_end_date >=" + dateTomorrow + " OR plan.reserve_end_notset_flag = 1) ";/* 翌日以降も販売している */
        query += " AND plan.plan_type IN (1,2) "; /* 事前予約 */
        query += "AND plan.foreign_flag = 0 "; /* 海外向け予約ではない */
        query += "ORDER BY plan.id ";

        try
        {
            connection = makeConnection();
            prestate = connection.prepareStatement( deleteSql );
            prestate.executeUpdate();

            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            Map<Integer, List<String>> map = new LinkedHashMap<Integer, List<String>>();
            List<String> strList = null;

            while( result.next() != false )
            {

                int id = result.getInt( "id" );
                String plan_name = result.getString( "plan_name" );
                if ( map.containsKey( id ) )
                {
                    strList = map.get( id );
                    int lastIndex = strList.size() - 1;
                    String lastStr = strList.get( lastIndex );
                    if ( lastStr.length() + plan_name.length() + 1 > 255 )
                    {
                        strList.add( plan_name );
                    }
                    else
                    {
                        strList.set( lastIndex, lastStr + " " + plan_name );
                    }
                }
                else
                {
                    strList = new ArrayList<String>();
                    strList.add( plan_name );
                }
                map.put( id, strList );
            }

            ret = map.size() > 0;

            String selectSql = "SELECT * FROM hh_hotel_search WHERE id = ? AND kind = ?";
            String insertSql = "INSERT INTO hh_hotel_search (word, id, kind) VALUES (?, ? ,?)";
            String updateSql = "UPDATE hh_hotel_search SET word = ? WHERE id = ? AND kind = ?";
            for( Iterator<Integer> keyIt = map.keySet().iterator() ; keyIt.hasNext() ; )
            {
                int hotelID = keyIt.next();
                strList = map.get( hotelID );
                for( int i = 0 ; i < strList.size() ; i++ )
                {
                    int kind = (startKind + i);
                    prestate = connection.prepareStatement( selectSql );
                    prestate.setInt( 1, hotelID );
                    prestate.setInt( 2, kind );
                    result = prestate.executeQuery();
                    String sql = "";
                    if ( result.next() )
                    {
                        sql = updateSql;
                    }
                    else
                    {
                        sql = insertSql;
                    }

                    prestate = connection.prepareStatement( sql );
                    prestate.setString( 1, strList.get( i ) );
                    prestate.setInt( 2, hotelID );
                    prestate.setInt( 3, kind );
                    prestate.executeUpdate();

                }
            }

        }
        catch ( Exception e )
        {
            System.out.println( "[SearchHotelPlanName.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

}
