package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHhRsvReserveCount;

/**
 * 予約件数集計バッチクラス
 * 
 * @author K.Mitsuhashi
 * 
 */

public class ExecReserveCount
{
    /** ハピホテからの予約 */
    public static final int    EXT_FLAG_HAPIHOTE            = 0;
    /** LVJからの予約 */
    public static final int    EXT_FLAG_LVJ                 = 1;
    /** OTAからの予約 */
    public static final int    EXT_FLAG_OTA                 = 2;

    public static final String PC_COUNT                     = "pcCount";
    public static final String SMART_COUNT                  = "smartCount";
    public static final String LIJ_COUNT                    = "lijCount";
    public static final String OTA_COUNT                    = "otaCount";
    public static final String RSV_APPLI_IOS_COUNT          = "rsvAppliIosCount";
    public static final String RSV_APPLI_ADR_COUNT          = "rsvAppliAdrCount";
    public static final String HAPPY_APPLI_IOS_COUNT        = "happyAppliIosCount";
    public static final String HAPPY_APPLI_ADR_COUNT        = "happyAppliAdrCount";
    public static final String PC_CANCEL_COUNT              = "pcCancelCount";
    public static final String SMART_CANCEL_COUNT           = "smartCancelCount";
    public static final String LIJ_CANCEL_COUNT             = "lijCancelCount";
    public static final String OTA_CANCEL_COUNT             = "otaCancelCount";
    public static final String RSV_APPLI_IOS_CANCEL_COUNT   = "rsvAppliIosCancelCount";
    public static final String RSV_APPLI_ADR_CANCEL_COUNT   = "rsvAppliAdrCancelCount";
    public static final String HAPPY_APPLI_IOS_CANCEL_COUNT = "happyAppliIosCancelCount";
    public static final String HAPPY_APPLI_ADR_CANCEL_COUNT = "happyAppliAdrCancelCount";

    static Connection          connection                   = null;
    static PreparedStatement   prestate                     = null;
    static ResultSet           result                       = null;

    static String              DB_URL;                                                   // URL for database server
    static String              user;                                                     // DB user
    static String              password;                                                 // DB password
    static String              driver;                                                   // DB driver
    static String              jdbcds;                                                   // DB jdbcds

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
            System.out.println( "ExecReserveCount Static Block Error=" + e.toString() );
        }
    }

    /**
     * メイン
     */

    public static void main(String[] args)
    {
        System.out.println( "[ExecReserveCount] Start" );
        int targetDate = 0;
        if ( args.length != 0 )
        {
            targetDate = Integer.parseInt( args[0] );
        }
        else
        {
            // 前日
            targetDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );
        }

        // accept_date（予約申込日）を基準にして集計
        if ( !dataAggregation( 0, targetDate ) )
        {
            System.out.println( "accept_date Aggregation Err" );
        }
        // reserve_date（予約日）を基準にして集計
        if ( !dataAggregation( 1, targetDate ) )
        {
            System.out.println( "reserve_date Aggregation Err" );
        }
        System.out.println( "[ExecReserveCount] End" );
    }

    /**
     * デバイス別予約件数データ集計
     * 
     * @param rsvFlag 基準フラグ 0：予約申込み日基準、1：予約日基準
     * @param targetDate 基準日
     * @return
     */
    private static boolean dataAggregation(int rsvFlag, int targetDate)
    {
        String query = "";
        // ハピホテ予約
        query = "SELECT ";
        query += (rsvFlag == 0) ? " his.accept_date AS rsvDate," : " rsv.reserve_date AS rsvDate,";
        query += " CASE WHEN (rsv.status=3) THEN 1 ELSE 0 END AS delFlag, ";
        query += " rsv.ext_flag, ";
        query += " rsv.user_agent, ";
        query += " count(*) AS count";
        query += " FROM newRsvDB.hh_rsv_reserve rsv";
        query += (rsvFlag == 0) ? " INNER JOIN newRsvDB.hh_rsv_reserve_history his ON rsv.id=his.id AND rsv.reserve_no=his.reserve_no AND his.reserve_sub_no = 0" : "";

        query += " WHERE  " + ((rsvFlag == 0) ? " his.accept_date" : " rsv.reserve_date") + "  >= ? ";
        query += " AND rsv.ext_flag = ? ";
        query += " GROUP BY rsvDate, rsv.ext_flag, delFlag, user_agent";
        query += " UNION";
        // LVJ予約・OTA予約
        query += " SELECT";
        query += (rsvFlag == 0) ? " his.accept_date AS rsvDate," : " rsv.reserve_date AS rsvDate,";
        query += " CASE WHEN (rsv.status=3) THEN 1 ELSE 0 END AS delFlag, ";
        query += " rsv.ext_flag, ";
        query += " '' AS user_agent, ";
        query += " count(*) AS count";
        query += " FROM newRsvDB.hh_rsv_reserve rsv";
        query += (rsvFlag == 0) ? " INNER JOIN newRsvDB.hh_rsv_reserve_history his ON rsv.id=his.id AND rsv.reserve_no=his.reserve_no AND his.reserve_sub_no = 0" : "";
        query += " WHERE  " + ((rsvFlag == 0) ? " his.accept_date" : " rsv.reserve_date") + "  >= ? ";
        query += " AND rsv.ext_flag != ? ";
        query += " GROUP BY rsvDate, rsv.ext_flag, delFlag ";

        query += " ORDER BY rsvDate, ext_flag;";

        try
        {
            Map<String, Integer> rsvCount = new HashMap<String, Integer>();
            // カウント初期化
            rsvCount = mapIni();

            int userAgent = 0;
            int rsvDate = 0;
            int rsvDatePre = 0;
            int delFlag = 0;
            int count = 0;

            connection = makeConnection();
            prestate = connection.prepareStatement( query );
            int i = 1;
            prestate.setInt( i++, targetDate );
            prestate.setInt( i++, EXT_FLAG_HAPIHOTE );
            prestate.setInt( i++, targetDate );
            prestate.setInt( i++, EXT_FLAG_HAPIHOTE );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {

                    // 予約日またはステータスがブレイクしたらデータ書き出し
                    rsvDate = result.getInt( "rsvDate" );
                    if ( (rsvDatePre != 0 && rsvDatePre != rsvDate) )
                    {
                        // データ書き出し
                        if ( !createData( rsvDatePre, rsvFlag, rsvCount, connection ) )
                        {
                            System.out.println( "[ExecReserveCount] createData Error rsvDate:" + rsvDate + " rsvFlag:" + rsvFlag );
                        }

                        // カウント初期化
                        rsvCount = mapIni();
                    }

                    // 集計
                    delFlag = result.getInt( "delFlag" );
                    count = result.getInt( "count" );
                    if ( result.getInt( "ext_flag" ) == EXT_FLAG_LVJ )
                    {
                        if ( delFlag == 0 )
                        {
                            rsvCount.put( LIJ_COUNT, rsvCount.get( LIJ_COUNT ) + count );
                        }
                        else
                        {
                            rsvCount.put( LIJ_CANCEL_COUNT, rsvCount.get( LIJ_CANCEL_COUNT ) + count );
                        }
                    }
                    else if ( result.getInt( "ext_flag" ) == EXT_FLAG_OTA )
                    {
                        if ( delFlag == 0 )
                        {
                            rsvCount.put( OTA_COUNT, rsvCount.get( OTA_COUNT ) + count );
                        }
                        else
                        {
                            rsvCount.put( OTA_CANCEL_COUNT, rsvCount.get( OTA_CANCEL_COUNT ) + count );
                        }
                    }
                    else if ( result.getInt( "ext_flag" ) == EXT_FLAG_HAPIHOTE )
                    {
                        userAgent = UserAgent.getDeviceFromUserAgentDetail( result.getString( "user_agent" ) );
                        switch( userAgent )
                        {
                            case UserAgent.DEVICE_RSV_APP_IOS:
                                if ( delFlag == 0 )
                                {
                                    rsvCount.put( RSV_APPLI_IOS_COUNT, rsvCount.get( RSV_APPLI_IOS_COUNT ) + count );
                                }
                                else
                                {
                                    rsvCount.put( RSV_APPLI_IOS_CANCEL_COUNT, rsvCount.get( RSV_APPLI_IOS_CANCEL_COUNT ) + count );
                                }
                                break;
                            case UserAgent.DEVICE_RSV_APP_ANDROID:
                                if ( delFlag == 0 )
                                {
                                    rsvCount.put( RSV_APPLI_ADR_COUNT, rsvCount.get( RSV_APPLI_ADR_COUNT ) + count );
                                }
                                else
                                {
                                    rsvCount.put( RSV_APPLI_ADR_CANCEL_COUNT, rsvCount.get( RSV_APPLI_ADR_CANCEL_COUNT ) + count );
                                }
                                break;
                            case UserAgent.DEVICE_APP_NEW_IOS:
                                if ( delFlag == 0 )
                                {
                                    rsvCount.put( HAPPY_APPLI_IOS_COUNT, rsvCount.get( HAPPY_APPLI_IOS_COUNT ) + count );
                                }
                                else
                                {
                                    rsvCount.put( HAPPY_APPLI_IOS_CANCEL_COUNT, rsvCount.get( HAPPY_APPLI_IOS_CANCEL_COUNT ) + count );
                                }
                                break;
                            case UserAgent.DEVICE_APP_NEW_ANDROID:
                                if ( delFlag == 0 )
                                {
                                    rsvCount.put( HAPPY_APPLI_ADR_COUNT, rsvCount.get( HAPPY_APPLI_ADR_COUNT ) + count );
                                }
                                else
                                {
                                    rsvCount.put( HAPPY_APPLI_ADR_CANCEL_COUNT, rsvCount.get( HAPPY_APPLI_ADR_CANCEL_COUNT ) + count );
                                }
                                break;
                            case UserAgent.DEVICE_SMARTPHONE_WEB:
                                if ( delFlag == 0 )
                                {
                                    rsvCount.put( SMART_COUNT, rsvCount.get( SMART_COUNT ) + count );
                                }
                                else
                                {
                                    rsvCount.put( SMART_CANCEL_COUNT, rsvCount.get( SMART_CANCEL_COUNT ) + count );
                                }
                                break;
                            case UserAgent.DEVICE_PC:
                                if ( delFlag == 0 )
                                {
                                    rsvCount.put( PC_COUNT, rsvCount.get( PC_COUNT ) + count );
                                }
                                else
                                {
                                    rsvCount.put( PC_CANCEL_COUNT, rsvCount.get( PC_CANCEL_COUNT ) + count );
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    // ブレイク値保存
                    rsvDatePre = rsvDate;
                }
                // 最後の一件出力
                if ( !createData( rsvDatePre, rsvFlag, rsvCount, connection ) )
                {
                    System.out.println( "[ExecReserveCount] createData Error rsvDate:" + rsvDate + " rsvFlag:" + rsvFlag );
                }
            }

        }
        catch ( Exception e )
        {
            System.out.println( "[ExecReserveCount] Exception e=" + e );
            e.printStackTrace();
            return(false);
        }
        finally
        {
            releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * デバイス別予約件数データ書き出し
     * 
     * @param rsvDate 予約日
     * @param rsvFlag 基準フラグ　 0：予約申込み日基準、1：予約日基準
     * @param delFlag キャンセルフラグ　 0：未キャンセル、1：キャンセル済み
     * @return
     */
    private static boolean createData(int rsvDate, int rsvFlag, Map<String, Integer> rsvCount, Connection connection)
    {

        DataHhRsvReserveCount reserveCount = new DataHhRsvReserveCount();
        reserveCount = new DataHhRsvReserveCount();
        reserveCount.setRsvDate( rsvDate );
        reserveCount.setRsvFlag( rsvFlag );
        reserveCount.setPcCount( rsvCount.get( PC_COUNT ) );
        reserveCount.setSmartCount( rsvCount.get( SMART_COUNT ) );
        reserveCount.setRsvAppliAdrCount( rsvCount.get( RSV_APPLI_ADR_COUNT ) );
        reserveCount.setRsvAppliIosCount( rsvCount.get( RSV_APPLI_IOS_COUNT ) );
        reserveCount.setHappyAppliAdrCount( rsvCount.get( HAPPY_APPLI_ADR_COUNT ) );
        reserveCount.setHappyAppliIosCount( rsvCount.get( HAPPY_APPLI_IOS_COUNT ) );
        reserveCount.setLijCount( rsvCount.get( LIJ_COUNT ) );
        reserveCount.setotaCount( rsvCount.get( OTA_COUNT ) );
        reserveCount.setPcCancelCount( rsvCount.get( PC_CANCEL_COUNT ) );
        reserveCount.setSmartCancelCount( rsvCount.get( SMART_CANCEL_COUNT ) );
        reserveCount.setRsvAppliAdrCancelCount( rsvCount.get( RSV_APPLI_ADR_CANCEL_COUNT ) );
        reserveCount.setRsvAppliIosCancelCount( rsvCount.get( RSV_APPLI_IOS_CANCEL_COUNT ) );
        reserveCount.setHappyAppliAdrCancelCount( rsvCount.get( HAPPY_APPLI_ADR_CANCEL_COUNT ) );
        reserveCount.setHappyAppliIosCancelCount( rsvCount.get( HAPPY_APPLI_IOS_CANCEL_COUNT ) );
        reserveCount.setLijCancelCount( rsvCount.get( LIJ_CANCEL_COUNT ) );
        reserveCount.setotaCancelCount( rsvCount.get( OTA_CANCEL_COUNT ) );
        reserveCount.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        reserveCount.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        if ( reserveCount.existsData( rsvDate, rsvFlag, connection ) )
        {
            // データが存在するときは更新
            // System.out.println( "[createData]UPDATE rsvDate=" + rsvDate + " rsvFlag=" + rsvFlag );
            return reserveCount.updateData( connection );
        }
        else
        {
            // データが存在しないときはINSERT
            // System.out.println( "[createData]INSERT" );
            return reserveCount.insertData( connection );
        }
    }

    /**
     * マップデータ初期化
     * 
     * @param rsvCount 集計用Map
     * @return
     */
    public static Map<String, Integer> mapIni()
    {
        Map<String, Integer> rsvCount = new HashMap<String, Integer>();
        rsvCount.put( LIJ_COUNT, 0 );
        rsvCount.put( OTA_COUNT, 0 );
        rsvCount.put( RSV_APPLI_IOS_COUNT, 0 );
        rsvCount.put( RSV_APPLI_ADR_COUNT, 0 );
        rsvCount.put( HAPPY_APPLI_IOS_COUNT, 0 );
        rsvCount.put( HAPPY_APPLI_ADR_COUNT, 0 );
        rsvCount.put( SMART_COUNT, 0 );
        rsvCount.put( PC_COUNT, 0 );
        rsvCount.put( LIJ_CANCEL_COUNT, 0 );
        rsvCount.put( OTA_CANCEL_COUNT, 0 );
        rsvCount.put( RSV_APPLI_IOS_CANCEL_COUNT, 0 );
        rsvCount.put( RSV_APPLI_ADR_CANCEL_COUNT, 0 );
        rsvCount.put( HAPPY_APPLI_IOS_CANCEL_COUNT, 0 );
        rsvCount.put( HAPPY_APPLI_ADR_CANCEL_COUNT, 0 );
        rsvCount.put( SMART_CANCEL_COUNT, 0 );
        rsvCount.put( PC_CANCEL_COUNT, 0 );

        return rsvCount;
    }

    /**
     * DBコネクション作成クラス
     * 
     * @return
     */
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

    /**
     * リソースを閉じる
     * 
     * @param resultset
     * @param statement
     * @param connection
     */
    public static void releaseResources(ResultSet resultset,
            Statement statement, Connection connection)
    {
        try
        {
            if ( resultset != null )
            {
                resultset.close();
                resultset = null;
            }
            if ( statement != null )
            {
                statement.close();
                statement = null;
            }
            if ( connection != null )
            {
                connection.close();
                connection = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the connection resources" + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the connection resources" + ex.toString() );
        }
    }

    /**
     * コネクションを閉じる
     * 
     * @param connection
     */
    public static void releaseResources(Connection connection)
    {
        try
        {
            if ( connection != null )
            {
                connection.close();
                connection = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the connection resources" + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the connection resources" + ex.toString() );
        }
    }

    /**
     * ResultSetオブジェクトを閉じる
     * 
     * @param resultset
     */
    public static void releaseResources(ResultSet resultset)
    {
        try
        {
            if ( resultset != null )
            {
                resultset.close();
                resultset = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the resultset " + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the resultset " + ex.toString() );
        }
    }

    /**
     * statementオブジェクトを閉じる
     * 
     * @param statement
     */
    public static void releaseResources(Statement statement)
    {
        try
        {
            if ( statement != null )
            {
                statement.close();
                statement = null;
            }

        }
        catch ( SQLException se )
        {
            Logging.error( "Error while closing the statement " + se.toString() );
        }
        catch ( Exception ex )
        {
            Logging.error( "Error while closing the statement " + ex.toString() );
        }
    }

}
