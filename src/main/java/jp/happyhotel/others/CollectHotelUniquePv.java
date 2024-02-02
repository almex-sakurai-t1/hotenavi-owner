package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelUniquePv;

/**
 * ユニークPV集計クラス
 * 
 * @author S.Tashiro
 * 
 */

public class CollectHotelUniquePv
{
    // ハピホテランク
    static final int         RANK_LIGHT    = 1;
    static final int         RANK_STANDARD = 2;
    static final int         RANK_MILE     = 3;

    static final int         TOMORROW      = 1;
    static final int         YESTERDAY     = -1;
    static final int         LASTWEEK      = -7;

    static final int         FIRST         = 1;
    static final int         ZERO          = 0;
    static Connection        con           = null; // Database connection
    static PreparedStatement stmt          = null;
    static ResultSet         rs            = null;

    static String            DB_URL;              // URL for database server
    static String            user;                // DB user
    static String            password;            // DB password
    static String            driver;              // DB driver
    static String            jdbcds;              // DB jdbcds

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

    /**
     * メイン
     * 
     * @param args 任意の日付(YYYYMMまたはYYYYMM00)
     */

    public static void main(String[] args)
    {
        int[] idList = null;
        String strDate;
        boolean auto = false;
        int nDate = 0;
        strDate = "";
        int prevMonth = 0;
        con = null;

        try
        {
            if ( args != null && args.length > 0 )
            {
                strDate = args[0];
                nDate = Integer.parseInt( strDate );
            }
            if ( nDate == 0 )
            {
                nDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), YESTERDAY );
                auto = true;
            }

            System.out.println( "nDate:" + nDate );
        }
        catch ( Exception e )
        {
            Logging.error( "[CollectHotelUniquePv] コマンドライン引数の処理失敗:" + e.toString() );
        }

        try
        {
            if ( checkPv( nDate ) != false || auto == false )// PV集計が終了している
            {
                if ( checkUniquePv( nDate ) != false )// ユーザーユニークアクセス集計が終了していない
                {
                    // 集計条件は、kind<=7かつrank>0
                    idList = collectHotelId( RANK_LIGHT, nDate );
                    // ユニークPVを集計して反映
                    collectUuPv( idList, nDate, auto );
                }

                int today = Integer.parseInt( DateEdit.getDate( 2 ) );
                Calendar cal = Calendar.getInstance();
                cal.set( today / 10000, (today / 100 % 100) - 1, 1 );
                cal.add( Calendar.MONTH, -1 );
                // 前月
                prevMonth = cal.get( Calendar.YEAR ) * 100 + (cal.get( Calendar.MONTH ) + 1);

                // 当日が１日であれば
                if ( today % 100 == 1 )
                {
                    collectUuMonthPv( prevMonth );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( e.toString() );
            e.printStackTrace();
        }
        finally
        {
            releaseResources( rs, stmt, con );
        }
        Logging.info( "[CollectHotelUniquePv] End" );
    }

    /**
     * PV集計処理判断
     * 
     * @return
     * @throws SQLException
     */
    public static boolean checkPv(int collectDate) throws SQLException
    {
        String query;
        boolean boolPv = false;

        // 前日比取得用の日付を計算
        // collectDate = DateEdit.addDay( collectDate, YESTERDAY );

        query = "SELECT COUNT(HB.id) AS COUNTPV,SUM(PV1.total_pv) AS TOTALPV,SUM(PV1.prev_day_ratio - PV2.prev_day_ratio) AS RATIO FROM hh_hotel_basic HB";
        query += " INNER JOIN hh_hotel_pv PV1 ON HB.id = PV1.id AND PV1.collect_date = ?";
        query += " INNER JOIN hh_hotel_pv PV2 ON HB.id = PV2.id AND PV2.collect_date = 0";
        query += " WHERE HB.rank >= 1";
        query += " AND HB.kind <= 7";
        try
        {

            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, collectDate );
            rs = stmt.executeQuery();
            if ( rs != null )
            {
                if ( rs.next() != false )
                {
                    if ( rs.getInt( "COUNTPV" ) != 0 && rs.getInt( "TOTALPV" ) != 0 && rs.getInt( "RATIO" ) == 0 )
                    {
                        boolPv = true;
                    }
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelUniquePv.checkPv] Exception=" + e.toString() );
            e.printStackTrace();
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }

        return(boolPv);
    }

    /**
     * ユニークアクセス集計処理判断
     * 
     * @return
     * @throws SQLException
     */
    public static boolean checkUniquePv(int collectDate) throws SQLException
    {
        String query;
        boolean boolUniquePv = true;

        // 前日比取得用の日付を計算
        // collectDate = DateEdit.addDay( collectDate, YESTERDAY );

        query = "SELECT COUNT(HB.id) AS COUNTPV,SUM(PV1.total_uu_pv) AS TOTALPV,SUM(PV1.prev_day_ratio - PV2.prev_day_ratio) AS RATIO FROM hh_hotel_basic HB";
        query += " INNER JOIN hh_hotel_unique_pv PV1 ON HB.id = PV1.id AND PV1.collect_date = ?";
        query += " INNER JOIN hh_hotel_unique_pv PV2 ON HB.id = PV2.id AND PV2.collect_date = 0";
        query += " WHERE HB.rank >= 1";
        query += " AND HB.kind <= 7";
        try
        {

            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, collectDate );
            rs = stmt.executeQuery();
            if ( rs != null )
            {
                if ( rs.next() != false )
                {
                    if ( rs.getInt( "COUNTPV" ) != 0 && rs.getInt( "TOTALPV" ) != 0 && rs.getInt( "RATIO" ) == 0 )
                    {
                        boolUniquePv = false; // 集計処理終了
                    }
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelUniquePv.checkUniquePv] Exception=" + e.toString() );
            e.printStackTrace();
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }

        return(boolUniquePv);
    }

    /**
     * ホテルID取得
     * 
     * @return
     * @throws SQLException
     */
    public static int[] collectHotelId(int rank, int collectDate) throws SQLException
    {
        String query;
        int[] idlist = null;
        int count = 0;
        int i;

        // 前日比取得用の日付を計算
        // collectDate = DateEdit.addDay( collectDate, YESTERDAY );

        query = "SELECT HB.id, COUNT(HB.id) AS COUNTPV FROM hh_hotel_basic HB";
        query += " INNER JOIN hh_hotel_pv PV ON HB.id = PV.id AND PV.collect_date = " + collectDate;
        query += " LEFT JOIN hh_user_view_hotel UVH ON HB.id = UVH.id AND UVH.regist_date = " + collectDate;
        query += " WHERE HB.rank >= ?";
        query += " AND HB.kind <= 7";
        query += " GROUP BY HB.id ";
        query += " ORDER BY COUNTPV DESC, PV.total_uu_pv DESC, HB.rank DESC, HB.id ";

        try
        {

            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, rank );
            rs = stmt.executeQuery();
            if ( rs != null )
            {
                // レコード件数取得
                if ( rs.last() != false )
                {
                    count = rs.getRow();
                }

                rs.beforeFirst();

                idlist = new int[count];
                i = 0;
                while( rs.next() != false )
                {
                    idlist[i] = rs.getInt( "id" );

                    i++;
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelUniquePv.collectHotelId] Exception=" + e.toString() );
            e.printStackTrace();
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }

        return(idlist);
    }

    /**
     * UUPVの集計
     * 
     * @param idList ホテルIDリスト
     * @param startDate 開始日付
     * @return 処理結果件数
     * @throws SQLException
     */
    public static int collectUuPv(int[] idList, int collectDate, boolean auto) throws SQLException
    {
        String query;
        String query2;
        int nTotalUuPv;
        int nPrevDayRatio;
        int nPrevRank = 0;
        boolean ret;
        int i = 0;
        int resultCount = 0;
        int collectDateBefore = 0;
        ret = false;
        nTotalUuPv = 0;
        nPrevDayRatio = 0;
        DataHotelUniquePv dhup = new DataHotelUniquePv();

        Connection dbcon = null;

        if ( collectDate == 0 )
        {
            collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), YESTERDAY );
        }
        // 前日比取得用の日付を計算
        collectDateBefore = DateEdit.addDay( collectDate, YESTERDAY );

        Logging.info( "[CollectHotelUniquePv.collectUuPv] Start, " + collectDate );

        query = "SELECT COUNT(user_id) as total_uu_pv FROM hh_user_view_hotel WHERE regist_date = ? ";
        query += " AND id = ?";

        query2 = "SELECT id,collect_date,rank FROM hh_hotel_unique_pv WHERE collect_date = ?";
        query2 += " AND id = ? ORDER BY total_uu_pv DESC";

        try
        {

            System.out.println( "UUPV:" + collectDate + ", rank:" + collectDateBefore );
            dbcon = makeConnection();
            for( i = 0 ; i < idList.length ; i++ )
            {

                nTotalUuPv = 0;
                nPrevDayRatio = 999999;
                nPrevRank = 0;
                // コネクションを作る
                if ( con == null )
                {
                    con = makeConnection();
                }
                stmt = con.prepareStatement( query );
                stmt.setInt( 1, collectDate );
                stmt.setInt( 2, idList[i] );
                rs = stmt.executeQuery();
                if ( rs != null )
                {
                    if ( rs.next() != false )
                    {
                        nTotalUuPv = rs.getInt( "total_uu_pv" );
                    }
                }
                releaseResources( rs );
                releaseResources( stmt );

                // 前日分のデータ取得
                stmt = con.prepareStatement( query2 );
                stmt.setInt( 1, collectDateBefore );
                stmt.setInt( 2, idList[i] );
                rs = stmt.executeQuery();
                if ( rs != null )
                {
                    // データがあった時だけ前回の順位から今回の順位を引いた値を前日比にセット
                    if ( rs.next() != false )
                    {
                        nPrevRank = rs.getInt( "rank" );
                        nPrevDayRatio = nPrevRank - (i + 1);
                    }
                }

                releaseResources( rs );
                releaseResources( stmt );

                // 取得した回数をセット
                if ( dhup != null )
                {
                    dhup = null;
                    dhup = new DataHotelUniquePv();
                }
                dhup.getData( dbcon, idList[i], collectDate );
                dhup.setId( idList[i] );
                dhup.setCollectDate( collectDate );
                // ユニークPV数のセット
                dhup.setTotalUuPv( nTotalUuPv );
                dhup.setPrevDayRatio( nPrevDayRatio );
                dhup.setRank( i + 1 );
                dhup.setPrevRank( nPrevRank );

                ret = updateHotelPv( dhup, dbcon );
                if ( ret != false )
                {
                    if ( auto != false )
                    {
                        dhup.setCollectDate( 0 );
                        ret = updateHotelPv( dhup, dbcon );
                    }
                    resultCount++;
                }

            }

        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelUniquePv.collectUuPv] Exception=" + e.toString() );
            e.printStackTrace();
            ret = false;
            throw e;
        }
        finally
        {
            releaseResources( dbcon );
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }
        Logging.info( "[CollectHotelUniquePv.collectUuPv] End" );

        return(resultCount);
    }

    /**
     * ホテルユニークPVデータ更新
     * 
     * @return
     * @throws SQLException
     */
    public static boolean updateHotelPv(DataHotelUniquePv hotelUuPv, Connection dbcon) throws SQLException
    {
        boolean ret = false;

        try
        {
            // コネクションを作る
            ret = hotelUuPv.updateData( dbcon, hotelUuPv.getId(), hotelUuPv.getCollectDate() );
            if ( ret == false )
            {
                ret = hotelUuPv.insertData( dbcon );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[CollectHotelUniquePv.updateHotelSort] Exception=" + e.toString() );
            e.printStackTrace();

            try
            {
                throw e;
            }
            catch ( Exception e1 )
            {
                // TODO 自動生成された catch ブロック
                e1.printStackTrace();
            }
        }
        finally
        {
        }

        return(ret);
    }

    /**
     * ホテルユニークPV月別集計
     * 
     * @return
     * @throws SQLException
     */
    public static int collectUuMonthPv(int collectMonth) throws SQLException
    {
        int count = -1;
        try
        {
            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }

            int[] id = null;
            int[] pv = null;

            int fromDate = collectMonth * 100 + 1;
            int toDate = DateEdit.getLastDayOfMonth( collectMonth * 100 );

            String query = "SELECT";
            query += " HB.id, SUM(UVH.total_uu_pv) AS COUNTPV FROM hh_hotel_basic HB";
            query += " INNER JOIN hh_hotel_pv PV ON HB.id = PV.id AND  PV.collect_date = 0";
            query += " INNER JOIN hh_hotel_unique_pv UVH ON HB.id = UVH.id AND UVH.collect_date BETWEEN " + fromDate + " AND " + toDate + " AND UVH.total_uu_pv > 0";
            query += " WHERE HB.rank >= 1 AND HB.kind <= 7";
            query += " GROUP BY HB.id";
            query += " ORDER BY COUNTPV DESC, PV.total_uu_pv DESC, HB.rank DESC, HB.id";

            stmt = con.prepareStatement( query );
            rs = stmt.executeQuery();

            if ( rs.last() != false )
            {
                count = rs.getRow();
            }

            id = new int[count];
            pv = new int[count];
            rs.beforeFirst();
            int i = -1;

            while( rs.next() != false )
            {
                i++;
                id[i] = rs.getInt( "HB.id" );
                pv[i] = rs.getInt( "COUNTPV" );
            }

            DBConnection.releaseResources( rs );
            DBConnection.releaseResources( stmt );

            for( i = 0 ; i < count ; i++ )
            {
                query = "REPLACE INTO hh_hotel_unique_pv SET";
                query += " id = " + id[i];
                query += ",collect_date = " + collectMonth + "00";
                query += ",total_uu_pv = " + pv[i];

                stmt = con.prepareStatement( query );
                stmt.executeUpdate();
                DBConnection.releaseResources( stmt );
            }

        }
        catch ( SQLException e )
        {
            Logging.info( "collectUuMonthPv exception:" + e.toString(), "CollectHotelUniquePv" );
        }
        finally
        {
            releaseResources( rs );
            releaseResources( stmt );
        }
        return count;
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
