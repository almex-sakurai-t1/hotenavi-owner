package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelSort;
import jp.happyhotel.data.DataMasterSort;
import jp.happyhotel.hotel.HotelCount;

/**
 * マイホテル登録数集計クラス
 * 
 * @author S.Tashiro
 * 
 */

public class CollectHotelSort
{
    // ハピホテランク
    static final int         RANK_LIGHT       = 1;
    static final int         RANK_STANDARD    = 2;
    static final int         RANK_MILE        = 3;

    static final int         UUPV             = 1;
    static final int         TOUCH            = 2;
    static final int         UU_TOUCH         = 3;
    static final int         SPONSOR          = 4;
    static final int         MYHOTEL          = 5;
    static final int         COUPON           = 6;
    static final int         RESERVE          = 7;
    static final int         KUCHIKOMI        = 8;
    static final int         YESTERDAY        = -1;
    static final int         LASTWEEK         = -7;
    static final int         SPONSOR_MULTIPLE = 1000;

    static final int         FIRST            = 1;
    static final int         ZERO             = 0;
    static Connection        con              = null; // Database connection
    static PreparedStatement stmt             = null;
    static ResultSet         rs               = null;

    static String            DB_URL;                 // URL for database server
    static String            user;                   // DB user
    static String            password;               // DB password
    static String            driver;                 // DB driver
    static String            jdbcds;                 // DB jdbcds

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
            System.out.println( "CollectHotelSort Static Block Error=" + e.toString() );
        }
    }

    /**
     * メイン
     * 
     * @param args 任意の日付(YYYYMMまたはYYYYMM00)
     */

    public static void main(String[] args)
    {
        int nCount;
        int[] idList;
        int[] idListStandard;
        int[] idListMile;
        nCount = 0;
        String strDate;
        boolean auto = false;
        int nDate = 0;
        strDate = "";
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
            Logging.error( "[CollectHotelSort] コマンドライン引数の処理失敗:" + e.toString() );
        }

        try
        {
            if ( checkPv( nDate ) != false || auto == false )// PV集計が終了している
            {
                if ( checkRanking( nDate ) != false )// ランキング集計が終了していない
                {
                    // 集計条件は、kind<=7かつrank>0
                    idList = collectHotelId( RANK_LIGHT );
                    idListStandard = collectHotelId( RANK_STANDARD );
                    idListMile = collectHotelId( RANK_MILE );

                    // タッチ数を集計して反映、ユニークタッチも集計
                    collectTouch( idListMile, nDate );

                    // 広告の集計
                    collectSponsor( nDate );

                    // グループ広告の集計
                    collectSponsorGroup( nDate );

                    // マイホテル登録
                    collectMyHotel( idList, nDate );
                    // クーポン登録
                    // collectCoupon( idListStandard, nDate );
                    // ハピホテ予約
                    collectReserve( idListMile, nDate );
                    // クチコミ数
                    collectKuchikomi( idListStandard, nDate );

                    // ユニークPVを集計して反映
                    collectUuPv( idList, nDate );

                    // 全ポイント集計
                    collectTotalPoint( idList, nDate, auto );

                    HotelCount.setData();
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
        Logging.info( "[CollectHotelSort] End" );
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
            Logging.error( "[CollectHotelSort.checkPV] Exception=" + e.toString() );
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
     * ランキング集計判断
     * 
     * @return
     * @throws SQLException
     */
    public static boolean checkRanking(int collectDate) throws SQLException
    {
        String query;
        boolean boolRanking = true;

        // 前日比取得用の日付を計算
        // collectDate = DateEdit.addDay( collectDate, YESTERDAY );

        query = "SELECT COUNT(HB.id) AS COUNTPV,SUM(PV1.uu_pv) AS TOTALPV,SUM(PV1.all_point - PV2.all_point) AS ALLPOINT FROM hh_hotel_basic HB";
        query += " INNER JOIN hh_hotel_sort PV1 ON HB.id = PV1.id AND PV1.collect_date = ?";
        query += " INNER JOIN hh_hotel_sort PV2 ON HB.id = PV2.id AND PV2.collect_date = 0";
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
                    if ( rs.getInt( "COUNTPV" ) != 0 && rs.getInt( "TOTALPV" ) != 0 && rs.getInt( "ALLPOINT" ) == 0 )
                    {
                        boolRanking = false; // 集計処理終了
                    }
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelSort.checkRanking] Exception=" + e.toString() );
            e.printStackTrace();
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }

        return(boolRanking);
    }

    /**
     * ホテルID取得
     * 
     * @return
     * @throws SQLException
     */
    public static int[] collectHotelId(int rank) throws SQLException
    {
        String query;
        int[] idlist = null;
        int count = 0;
        int i;

        query = "SELECT id FROM hh_hotel_basic";
        query += " WHERE rank >= ?";
        query += " AND kind <= 7";
        query += " ORDER BY id, rank DESC ";

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
            Logging.error( "[CollectHotelSort.collectHotelId] Exception=" + e.toString() );
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
     * ホテルID取得
     * 
     * @return
     * @throws SQLException
     */
    public static int[] collectGroupId(int groupId, int startDate, int endDate) throws SQLException
    {
        String query;
        int[] idlist = null;
        int count = 0;
        int i;

        query = "SELECT HB.id, HB.rank FROM hh_hotel_basic HB";
        query += " INNER JOIN  hh_hotel_chain HC ON HC.id = HB.id";
        query += " WHERE HC.group_id = ?";
        query += " AND HC.start_date <= ?";
        query += " AND HC.end_date >= ?";
        query += " AND HB.kind <= 7";
        query += " ORDER BY HB.rank, HB.id DESC ";

        try
        {

            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, groupId );
            stmt.setInt( 2, endDate );
            stmt.setInt( 3, startDate );
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
                    if ( rs.getInt( "rank" ) > 0 )
                    {
                        idlist[i] = rs.getInt( "id" );
                    }
                    else
                    {
                        idlist[i] = 0;
                    }
                    i++;
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelSort.collectGroupId] Exception=" + e.toString() );
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
     * ソートマスター取得
     * 
     * @param kind 区分
     * @return DataMasaterSort マスターソートデータ
     * @throws SQLException
     */
    public static DataMasterSort masterSort(int kind) throws SQLException
    {
        String query;
        DataMasterSort master = null;

        query = "SELECT * FROM hh_master_sort";
        query += " WHERE kind = ?";

        try
        {

            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, kind );

            rs = stmt.executeQuery();
            if ( rs != null )
            {
                if ( rs.next() != false )
                {
                    master = new DataMasterSort();
                    master.setData( rs );
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[DataSystemMyHotel.masterSort] Exception=" + e.toString() );
            e.printStackTrace();
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }

        return(master);
    }

    /**
     * UUPVの集計
     * 
     * @param idList ホテルIDリスト
     * @param startDate 開始日付
     * @return 処理結果件数
     * @throws SQLException
     */
    public static int collectUuPv(int[] idList, int collectDate) throws SQLException
    {
        String query;
        int count;
        boolean ret;
        int i = 0;
        int resultCount = 0;
        ret = false;
        count = 0;
        DataMasterSort dms = new DataMasterSort();
        DataHotelSort dhs = new DataHotelSort();
        Connection dbcon = null;

        if ( collectDate == 0 )
        {
            collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );
        }
        Logging.info( "[CollectHotelSort.collectUuPv] Start, " + collectDate );
        dms = masterSort( UUPV );

        query = "SELECT total_uu_pv FROM hh_hotel_pv WHERE collect_date = ? ";
        query += " AND id = ?";

        try
        {

            dbcon = makeConnection();
            for( i = 0 ; i < idList.length ; i++ )
            {
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
                        count = rs.getInt( "total_uu_pv" );
                    }
                }
                releaseResources( rs );
                releaseResources( stmt );

                // 取得した回数をセット
                if ( dhs != null )
                {
                    dhs = null;
                    dhs = new DataHotelSort();
                }
                dhs.getData( dbcon, idList[i], collectDate );
                dhs.setId( idList[i] );
                dhs.setCollectDate( collectDate );
                // ユニークPV数のセット
                dhs.setUuPv( count );
                if ( dhs.getUuPv() > dms.getLimitValue() )
                {
                    dhs.setUuPv( dms.getLimitValue() );
                }
                dhs.setUuPvPoint( dhs.getUuPv() * dms.getCoefficient() );

                ret = updateHotelSort( dhs, dbcon );
                if ( ret != false )
                {
                    resultCount++;
                }

            }

        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelSort.collectUuPv] Exception=" + e.toString() );
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
        Logging.info( "[CollectHotelSort.collectUuPv] End" );

        return(resultCount);
    }

    /**
     * タッチの集計
     * 
     * @param idList ホテルIDリスト
     * @param endDate 終了日付
     * @return 処理結果件数
     * @throws SQLException
     */
    public static int collectTouch(int[] idList, int collectDate) throws SQLException
    {
        String query;
        String query2;
        int count;
        int count2;
        int resultCount = 0;
        boolean ret;
        int i = 0;
        int startDate = 0;
        ret = false;
        count = 0;
        DataMasterSort dms = new DataMasterSort();
        DataMasterSort dms2 = new DataMasterSort();
        DataHotelSort dhs = new DataHotelSort();
        Connection dbcon = null;

        if ( collectDate == 0 )
        {
            collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), YESTERDAY );
        }
        startDate = DateEdit.addDay( collectDate, LASTWEEK );
        Logging.info( "[CollectHotelSort.collectTouch] Start," + startDate + "-" + collectDate );
        dms = masterSort( TOUCH );
        dms2 = masterSort( UU_TOUCH );

        // タッチ数
        query = "SELECT id FROM hh_hotel_ci WHERE ci_status=1 ";
        query += " AND ci_date BETWEEN ? AND ?";
        query += " AND id = ?";
        query += " GROUP BY id, seq";

        // ユニークタッチ数
        query2 = "SELECT id FROM hh_hotel_ci WHERE ci_status=1 ";
        query2 += " AND ci_date BETWEEN ? AND ?";
        query2 += " AND id = ?";
        query2 += " GROUP BY id, user_id";

        try
        {
            dbcon = makeConnection();
            for( i = 0 ; i < idList.length ; i++ )
            {
                count = 0;
                count2 = 0;
                // コネクションを作る
                if ( con == null )
                {
                    con = makeConnection();
                }

                // タッチ数を取得
                stmt = con.prepareStatement( query );
                stmt.setInt( 1, startDate );
                stmt.setInt( 2, collectDate );
                stmt.setInt( 3, idList[i] );
                rs = stmt.executeQuery();

                if ( rs != null )
                {
                    // レコード件数取得
                    if ( rs.last() != false )
                    {
                        count = rs.getRow();
                    }
                }
                releaseResources( rs );
                releaseResources( stmt );

                // ユニークタッチ数を取得
                stmt = con.prepareStatement( query2 );
                stmt.setInt( 1, startDate );
                stmt.setInt( 2, collectDate );
                stmt.setInt( 3, idList[i] );
                rs = stmt.executeQuery();
                if ( rs != null )
                {
                    // レコード件数取得
                    if ( rs.last() != false )
                    {
                        count2 = rs.getRow();
                    }
                }
                releaseResources( rs );
                releaseResources( stmt );

                // 取得した回数をセット
                if ( dhs != null )
                {
                    dhs = null;
                    dhs = new DataHotelSort();
                }
                dhs.getData( dbcon, idList[i], collectDate );
                dhs.setId( idList[i] );
                dhs.setCollectDate( collectDate );
                // タッチ数のセット
                dhs.setTouch( count );
                if ( dhs.getTouch() > dms.getLimitValue() )
                {
                    dhs.setTouch( dms.getLimitValue() );
                }
                dhs.setTouchPoint( dhs.getTouch() * dms.getCoefficient() );

                // ユニークタッチ数のセット
                dhs.setUuTouch( count2 );
                if ( dhs.getUuTouch() > dms2.getLimitValue() )
                {
                    dhs.setUuTouch( dms2.getLimitValue() );
                }
                dhs.setUuTouchPoint( dhs.getUuTouch() * dms2.getCoefficient() );

                ret = updateHotelSort( dhs, dbcon );
                if ( ret != false )
                {
                    resultCount++;
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelSort.collectTouch] Exception=" + e.toString() );
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
        Logging.info( "[CollectHotelSort.collectTouch] End" );

        return(resultCount);
    }

    /***
     * スポンサー広告の集計
     * 
     * @param endDate
     * @return
     */
    public static int collectSponsor(int collectDate)
    {
        String query = "";
        int count;
        int resultCount = 0;
        int startDate = 0;
        int endDate = 0;
        int collectMonth = 0;
        boolean ret;
        int i = 0;
        ret = false;
        count = 0;
        DataMasterSort dms = new DataMasterSort();
        DataHotelSort dhs = new DataHotelSort();
        Connection dbcon = null;
        int[] hotelIdList = null;
        int[] countList = null;

        try
        {

            if ( collectDate == 0 )
            {
                collectMonth = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), YESTERDAY );
            }
            else
            {
                collectMonth = collectDate;
            }

            collectMonth = collectMonth / 100 * 100;

            startDate = collectMonth + 1;
            endDate = collectMonth + 31;

            Logging.info( "[CollectHotelSort.collectSponsor] Start, " + startDate + "-" + endDate );

            dms = masterSort( SPONSOR );

            query = "SELECT hotel_id, COUNT(hotel_id) AS cnt FROM hh_master_sponsor  ";
            query += " WHERE hotel_id <> 0 AND hotel_id <> 99999999 ";
            query += " AND hotel_id > 100000 ";
            query += " AND start_date <= ? AND end_date >= ? ";
            query += " GROUP BY hotel_id ";
            query += " ORDER BY hotel_id";

            dbcon = makeConnection();
            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            // タッチ数を取得
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, endDate );
            stmt.setInt( 2, startDate );
            rs = stmt.executeQuery();

            if ( rs != null )
            {
                // レコード件数取得
                if ( rs.last() != false )
                {
                    count = rs.getRow();
                }

                rs.beforeFirst();
                hotelIdList = new int[count];
                countList = new int[count];

                while( rs.next() != false )
                {
                    hotelIdList[i] = rs.getInt( "hotel_id" );
                    countList[i] = rs.getInt( "cnt" );

                    // 取得した回数をセット
                    if ( dhs != null )
                    {
                        dhs = null;
                        dhs = new DataHotelSort();
                    }
                    dhs.getData( dbcon, hotelIdList[i], collectDate );
                    dhs.setId( hotelIdList[i] );
                    dhs.setCollectDate( collectDate );
                    // 広告数×1000をセット
                    dhs.setSponsor( countList[i] * SPONSOR_MULTIPLE );
                    if ( dhs.getSponsor() > dms.getLimitValue() )
                    {
                        dhs.setSponsor( dms.getLimitValue() );
                    }
                    dhs.setSponsorPoint( dhs.getSponsor() * dms.getCoefficient() );

                    ret = updateHotelSort( dhs, dbcon );
                    if ( ret != false )
                    {
                        resultCount++;
                    }
                    i++;
                }
            }
            releaseResources( rs );
            releaseResources( stmt );
        }
        catch ( Exception e )
        {
        }
        finally
        {
            releaseResources( dbcon );
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }
        Logging.info( "[CollectHotelSort.collectSponsor] End" );

        return(resultCount);
    }

    /***
     * スポンサー広告の集計
     * 
     * @param endDate
     * @return
     */
    public static int collectSponsorGroup(int collectDate)
    {
        String query = "";
        int count;
        int resultCount = 0;
        int startDate = 0;
        int endDate = 0;
        int collectMonth = 0;
        boolean ret;
        int i = 0;
        ret = false;
        count = 0;
        DataMasterSort dms = new DataMasterSort();
        DataHotelSort dhs = new DataHotelSort();
        Connection dbcon = null;
        int[] hotelIdList = null;
        int[] groupIdList = null;
        int[] countList = null;

        try
        {

            if ( collectDate == 0 )
            {
                collectMonth = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), YESTERDAY );
            }
            else
            {
                collectMonth = collectDate;
            }
            collectMonth = collectMonth / 100 * 100;

            startDate = collectMonth + 1;
            endDate = collectMonth + 31;
            Logging.info( "[CollectHotelSort.collectSponsorGroup] Start, " + startDate + "-" + endDate );

            dms = masterSort( SPONSOR );

            query = "SELECT hotel_id, COUNT(hotel_id) AS cnt FROM hh_master_sponsor  ";
            query += " WHERE hotel_id <> 0 AND hotel_id <> 99999999 ";
            query += " AND hotel_id < 100000 ";
            query += " AND start_date <= ? AND end_date >= ? ";
            query += " GROUP BY hotel_id";
            query += " ORDER BY hotel_id";

            dbcon = makeConnection();
            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            // タッチ数を取得
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, endDate );
            stmt.setInt( 2, startDate );
            rs = stmt.executeQuery();

            if ( rs != null )
            {
                // レコード件数取得
                if ( rs.last() != false )
                {
                    count = rs.getRow();
                }

                rs.beforeFirst();
                hotelIdList = new int[count];
                countList = new int[count];

                while( rs.next() != false )
                {
                    hotelIdList[i] = rs.getInt( "hotel_id" );
                    countList[i] = rs.getInt( "cnt" );
                    i++;
                }
            }
            releaseResources( rs );
            releaseResources( stmt );

            for( i = 0 ; i < hotelIdList.length ; i++ )
            {
                groupIdList = collectGroupId( hotelIdList[i], startDate, endDate );
                if ( groupIdList != null )
                {
                    for( int k = 0 ; k < groupIdList.length ; k++ )
                    {
                        if ( groupIdList[k] != 0 )
                        {

                            // 取得した回数をセット
                            if ( dhs != null )
                            {
                                dhs = null;
                                dhs = new DataHotelSort();
                            }
                            dhs.getData( dbcon, groupIdList[k], collectDate );
                            dhs.setId( groupIdList[k] );
                            dhs.setCollectDate( collectDate );
                            // ホテルIDのスポンサーと、GROUPIDでのスポンサーデータを足しこむ
                            dhs.setSponsor( dhs.getSponsor() + (countList[i] * SPONSOR_MULTIPLE / groupIdList.length) );
                            if ( dhs.getSponsor() > dms.getLimitValue() )
                            {
                                dhs.setSponsor( dms.getLimitValue() );
                            }
                            dhs.setSponsorPoint( dhs.getSponsor() * dms.getCoefficient() );

                            ret = updateHotelSort( dhs, dbcon );
                            if ( ret != false )
                            {
                                resultCount++;
                            }
                        }
                    }
                }
                groupIdList = null;
            }

        }
        catch ( Exception e )
        {
        }
        finally
        {
            releaseResources( dbcon );
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }
        Logging.info( "[CollectHotelSort.collectSponsorGroup] End " );
        return(resultCount);
    }

    /**
     * マイホテルの取得
     * 
     * @param idList ホテルIDリスト
     * @param collectMonth 集計月
     * @return 処理結果
     * @throws SQLException
     */
    public static int collectMyHotel(int[] idList, int collectDate) throws SQLException
    {
        String query;
        int count;
        int resultCount = 0;
        int collectMonth = 0;
        boolean ret;
        int i = 0;
        ret = false;
        count = 0;
        DataMasterSort dms = new DataMasterSort();
        DataHotelSort dhs = new DataHotelSort();
        Connection dbcon = null;

        if ( collectDate == 0 )
        {
            collectMonth = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), YESTERDAY );
        }
        else
        {
            collectMonth = DateEdit.addMonth( collectDate, YESTERDAY );
        }

        collectMonth = collectMonth / 100 * 100;

        Logging.info( "[CollectHotelSort.collectMyHotel] Start, " + collectMonth );
        dms = masterSort( MYHOTEL );

        // タッチ数
        query = "SELECT count FROM hh_system_myhotel WHERE id = ? ";
        query += " AND collect_date = ?";
        try
        {
            dbcon = makeConnection();
            for( i = 0 ; i < idList.length ; i++ )
            {
                count = 0;
                // コネクションを作る
                if ( con == null )
                {
                    con = makeConnection();
                }

                // タッチ数を取得
                stmt = con.prepareStatement( query );
                stmt.setInt( 1, idList[i] );
                stmt.setInt( 2, collectMonth );
                rs = stmt.executeQuery();

                if ( rs != null )
                {
                    // レコード件数取得
                    if ( rs.next() != false )
                    {
                        count = rs.getInt( "count" );
                    }
                }
                releaseResources( rs );
                releaseResources( stmt );

                // 取得した回数をセット
                if ( dhs != null )
                {
                    dhs = null;
                    dhs = new DataHotelSort();
                }
                dhs.getData( dbcon, idList[i], collectDate );
                dhs.setId( idList[i] );
                dhs.setCollectDate( collectDate );
                // マイホテル数のセット
                dhs.setMyhotel( count );
                if ( dhs.getMyhotel() > dms.getLimitValue() )
                {
                    dhs.setMyhotel( dms.getLimitValue() );
                }
                dhs.setMyhotelPoint( dhs.getMyhotel() * dms.getCoefficient() );

                ret = updateHotelSort( dhs, dbcon );
                if ( ret != false )
                {
                    resultCount++;
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelSort.collectMyHotel] Exception=" + e.toString() );
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
        Logging.info( "[CollectHotelSort.collectMyHotel] End" );
        return(resultCount);
    }

    /**
     * クーポンの取得
     * 
     * @param idList ホテルIDリスト
     * @param endDate 集計月
     * @return 処理結果
     * @throws SQLException
     */
    public static int collectCoupon(int[] idList, int collectDate) throws SQLException
    {
        String query;
        int count;
        int resultCount = 0;
        int startDate = 0;
        boolean ret;
        int i = 0;
        ret = false;
        count = 0;
        DataMasterSort dms = new DataMasterSort();
        DataHotelSort dhs = new DataHotelSort();
        Connection dbcon = null;

        if ( collectDate == 0 )
        {
            collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), YESTERDAY );
        }

        startDate = DateEdit.addDay( collectDate, LASTWEEK );

        Logging.info( "[CollectHotelSort.collectCoupon] Start, " + startDate + "-" + collectDate );
        dms = masterSort( COUPON );

        // タッチ数
        query = "SELECT COUNT(*) FROM hh_user_coupon WHERE id = ? ";
        query += " AND print_date BETWEEN ? AND ?";
        try
        {
            dbcon = makeConnection();
            for( i = 0 ; i < idList.length ; i++ )
            {
                count = 0;
                // コネクションを作る
                if ( con == null )
                {
                    con = makeConnection();
                }

                // タッチ数を取得
                stmt = con.prepareStatement( query );
                stmt.setInt( 1, idList[i] );
                stmt.setInt( 2, startDate );
                stmt.setInt( 3, collectDate );
                rs = stmt.executeQuery();

                if ( rs != null )
                {
                    // レコード件数取得
                    if ( rs.next() != false )
                    {
                        count = rs.getInt( 1 );
                    }
                }
                releaseResources( rs );
                releaseResources( stmt );

                // 取得した回数をセット
                if ( dhs != null )
                {
                    dhs = null;
                    dhs = new DataHotelSort();
                }
                dhs.getData( dbcon, idList[i], collectDate );
                dhs.setId( idList[i] );
                dhs.setCollectDate( collectDate );
                // マイホテル数のセット
                dhs.setCoupon( count );
                if ( dhs.getCoupon() > dms.getLimitValue() )
                {
                    dhs.setCoupon( dms.getLimitValue() );
                }
                dhs.setCouponPoint( dhs.getCoupon() * dms.getCoefficient() );

                ret = updateHotelSort( dhs, dbcon );
                if ( ret != false )
                {
                    resultCount++;
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelSort.collectCoupon] Exception=" + e.toString() );
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
        Logging.info( "[CollectHotelSort.collectCoupon] End" );
        return(resultCount);
    }

    /**
     * 予約の取得
     * 
     * @param idList ホテルIDリスト
     * @param endDate 終了日
     * @return 処理結果
     * @throws SQLException
     */
    public static int collectReserve(int[] idList, int collectDate) throws SQLException
    {
        String query;
        int count;
        int resultCount = 0;
        int startDate = 0;
        boolean ret;
        int i = 0;
        ret = false;
        count = 0;
        DataMasterSort dms = new DataMasterSort();
        DataHotelSort dhs = new DataHotelSort();
        Connection dbcon = null;

        if ( collectDate == 0 )
        {
            collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), YESTERDAY );
        }
        startDate = DateEdit.addDay( collectDate, LASTWEEK );

        Logging.info( "[CollectHotelSort.collectReserve] Start, " + startDate + "-" + collectDate );
        dms = masterSort( RESERVE );

        // タッチ数
        // query = "SELECT COUNT(*) FROM hh_rsv_reserve WHERE status = 2 ";
        // query += " AND id = ?";
        // query += " AND reserve_date BETWEEN ? AND ?";

        // 予約について、新スキーマ分も合わせてカウントをとる
        query = "SELECT COUNT(t.id) FROM ";
        query += "(SELECT id FROM hh_rsv_reserve WHERE status = 2 ";
        query += " AND id = ?";
        query += " AND reserve_date BETWEEN ? AND ?";
        query += " UNION ALL SELECT id FROM newRsvDB.hh_rsv_reserve WHERE status = 2 ";
        query += " AND id = ?";
        query += " AND reserve_date BETWEEN ? AND ?)t GROUP BY id";
        try
        {
            dbcon = makeConnection();
            for( i = 0 ; i < idList.length ; i++ )
            {
                count = 0;
                // コネクションを作る
                if ( con == null )
                {
                    con = makeConnection();
                }

                // タッチ数を取得
                stmt = con.prepareStatement( query );
                stmt.setInt( 1, idList[i] );
                stmt.setInt( 2, startDate );
                stmt.setInt( 3, collectDate );
                stmt.setInt( 4, idList[i] );
                stmt.setInt( 5, startDate );
                stmt.setInt( 6, collectDate );
                rs = stmt.executeQuery();

                if ( rs != null )
                {
                    // レコード件数取得
                    if ( rs.next() != false )
                    {
                        count = rs.getInt( 1 );
                    }
                }
                releaseResources( rs );
                releaseResources( stmt );

                // 取得した回数をセット
                if ( dhs != null )
                {
                    dhs = null;
                    dhs = new DataHotelSort();
                }
                dhs.getData( dbcon, idList[i], collectDate );
                dhs.setId( idList[i] );
                dhs.setCollectDate( collectDate );
                // マイホテル数のセット
                dhs.setReserve( count );
                if ( dhs.getReserve() > dms.getLimitValue() )
                {
                    dhs.setReserve( dms.getLimitValue() );
                }
                dhs.setReservePoint( dhs.getReserve() * dms.getCoefficient() );

                ret = updateHotelSort( dhs, dbcon );
                if ( ret != false )
                {
                    resultCount++;
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelSort.collectCoupon] Exception=" + e.toString() );
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
        Logging.info( "[CollectHotelSort.collectReserve] End" );
        return(resultCount);
    }

    /**
     * クチコミの取得
     * 
     * @param idList ホテルIDリスト
     * @param endDate 終了日
     * @return 処理結果
     * @throws SQLException
     */
    public static int collectKuchikomi(int[] idList, int collectDate) throws SQLException
    {
        String query;
        int count;
        int resultCount = 0;
        int startDate = 0;
        boolean ret;
        int i = 0;
        ret = false;
        count = 0;
        DataMasterSort dms = new DataMasterSort();
        DataHotelSort dhs = new DataHotelSort();
        Connection dbcon = null;

        if ( collectDate == 0 )
        {
            collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), YESTERDAY );
        }

        startDate = DateEdit.addDay( collectDate, LASTWEEK );

        Logging.info( "[CollectHotelSort.collectKuchikomi] Start, " + startDate + "-" + collectDate );
        dms = masterSort( KUCHIKOMI );

        // タッチ数
        query = "SELECT COUNT(*) FROM hh_hotel_bbs WHERE thread_status BETWEEN 1 AND 2 ";
        query += " AND id = ?";
        query += " AND contribute_date BETWEEN ? AND ?";
        try
        {
            dbcon = makeConnection();
            for( i = 0 ; i < idList.length ; i++ )
            {
                count = 0;
                // コネクションを作る
                if ( con == null )
                {
                    con = makeConnection();
                }

                // タッチ数を取得
                stmt = con.prepareStatement( query );
                stmt.setInt( 1, idList[i] );
                stmt.setInt( 2, startDate );
                stmt.setInt( 3, collectDate );
                rs = stmt.executeQuery();

                if ( rs != null )
                {
                    // レコード件数取得
                    if ( rs.next() != false )
                    {
                        count = rs.getInt( 1 );
                    }
                }
                releaseResources( rs );
                releaseResources( stmt );

                // 取得した回数をセット
                if ( dhs != null )
                {
                    dhs = null;
                    dhs = new DataHotelSort();
                }
                dhs.getData( dbcon, idList[i], collectDate );
                dhs.setId( idList[i] );
                dhs.setCollectDate( collectDate );
                // マイホテル数のセット
                dhs.setKuchikomi( count );
                if ( dhs.getKuchikomi() > dms.getLimitValue() )
                {
                    dhs.setKuchikomi( dms.getLimitValue() );
                }
                dhs.setKuchikomiPoint( dhs.getKuchikomi() * dms.getCoefficient() );

                ret = updateHotelSort( dhs, dbcon );
                if ( ret != false )
                {
                    resultCount++;
                }
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[CollectHotelSort.collectCoupon] Exception=" + e.toString() );
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
        Logging.info( "[CollectHotelSort.collectKuchikomi] End" );
        return(resultCount);
    }

    /****
     * 総合ポイント集計
     * 
     * @param idList
     * @return
     */
    public static int collectTotalPoint(int[] idList, int collectDate, boolean auto)
    {
        DataHotelSort dhs = new DataHotelSort();
        boolean ret = false;
        int i = 0;
        int resultCount = 0;
        int point = 0;

        Logging.info( "[CollectHotelSort.collectTotalPoint] Start, " + collectDate + ", collectdate0update:" + auto );
        try
        {
            for( i = 0 ; i < idList.length ; i++ )
            {
                point = 0;
                // コネクションを作る
                if ( con == null )
                {
                    con = makeConnection();
                }

                // 取得した回数をセット
                if ( dhs != null )
                {
                    dhs = null;
                    dhs = new DataHotelSort();
                }
                dhs.getData( con, idList[i], collectDate );
                dhs.setId( idList[i] );
                dhs.setCollectDate( collectDate );
                point = dhs.getUuPvPoint() + dhs.getTouchPoint() + dhs.getUuTouchPoint() + dhs.getSponsorPoint() + dhs.getMyhotelPoint()
                        + dhs.getCouponPoint() + dhs.getReservePoint() + dhs.getKuchikomiPoint();

                dhs.setAllPoint( point );
                ret = updateHotelSort( dhs, con );
                if ( ret != false )
                {
                    resultCount++;
                }

                if ( auto != false )
                {
                    // 集計日を変更してインサートまたはアップデート
                    dhs.setCollectDate( 0 );
                    ret = updateHotelSort( dhs, con );
                }
            }
        }
        catch ( Exception e )
        {

        }
        Logging.info( "[CollectHotelSort.collectTotalPoint] End" );
        return resultCount;

    }

    /**
     * ホテルソートデータ更新
     * 
     * @return
     * @throws SQLException
     */
    public static boolean updateHotelSort(DataHotelSort hotelSort, Connection dbcon) throws SQLException
    {
        boolean ret = false;
        String query;
        int[] idlist = null;
        int count = 0;

        try
        {
            // コネクションを作る
            ret = hotelSort.updateData( dbcon, hotelSort.getId(), hotelSort.getCollectDate() );
            if ( ret == false )
            {
                ret = hotelSort.insertData( dbcon );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "[CollectHotelSort.updateHotelSort] Exception=" + e.toString() );
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
