package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSystemMyHotel;

/**
 * マイホテル登録数集計クラス
 * 
 * @author S.Tashiro
 * 
 */

public class CollectMyHotelRanking
{
    static final int                   FIRST = 1;
    static final int                   ZERO  = 0;
    static Connection                  con   = null; // Database connection
    static PreparedStatement           stmt  = null;
    static ResultSet                   rs    = null;
    private static String              driver;
    static String                      DB_URL;      // URL for database server
    static String                      user;        // DB user
    static String                      password;    // DB password

    private static DataSystemMyHotel[] myHotel;
    private static int                 myHotelCount;

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

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            System.out.println( "CollectMyHotelRanking Static Block Error=" + e.toString() );
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
        nCount = 0;
        String strDate;

        strDate = "";
        try
        {
            Logging.info( "[CollectMyHotelRanking] Start" );
            for( int i = 0 ; i < args.length ; ++i )
            {
                strDate += args[i];
            }
            strDate = CollectMyHotelRanking.CollectDateCheck( strDate );
        }
        catch ( Exception e )
        {
            Logging.error( "[CollectMyHotelRanking] コマンドライン引数の処理失敗:" + e.toString() );
        }

        try
        {
            // 開始日付で登録されているデータを削除（同じデータが存在する可能性があるため）
            CollectMyHotelRanking.deleteData( Integer.parseInt( strDate ) );

            // インサート対象のデータを取得
            nCount = CollectMyHotelRanking.collectData( Integer.parseInt( strDate ) );
            if ( nCount > 0 )
            {
                // 全国のランキングを作成し、登録数の降順ソート
                myHotel = CollectMyHotelRanking.createRanking( myHotel );

                nCount = myHotel.length;
                if ( nCount > 0 )
                {
                    // ホテル毎に、累計登録数を取得する
                    myHotel = CollectMyHotelRanking.getTotalCount( myHotel );
                    nCount = myHotel.length;
                    if ( nCount > 0 )
                    {
                        // ランキングデータをインサートする
                        nCount = CollectMyHotelRanking.insertData( myHotel );
                    }
                    else
                    {
                        nCount = -1;
                    }
                }
                else
                {
                    nCount = -1;
                }
            }

            // インサートされた件数を元にログ出力
            if ( nCount > 0 )
            {
                Logging.info( "[CollectMyHotelRanking] insert:" + nCount + " records" );
            }
            else if ( nCount == 0 )
            {
                Logging.info( "[CollectMyHotelRanking] insert:no_record" );
            }
            else
            {
                Logging.info( "[CollectMyHotelRanking] insert:error" );
            }

        }
        catch ( Exception e )
        {
            Logging.info( e.toString() );
            e.printStackTrace();
        }
        finally
        {
            CollectMyHotelRanking.closeConnection();
        }
        Logging.info( "[CollectMyHotelRanking] End" );
    }

    /**
     * 入力日付のチェック
     * 
     * @param strCheck 入力文字列
     * @return 0または、日付
     */
    private static String CollectDateCheck(String strCheck)
    {

        if ( CheckString.numCheck( strCheck ) != false )
        {
            // 日付のフォーマットに足りなかったら0を追加する(YYYYMM00)
            if ( strCheck.length() < 8 )
            {
                while( strCheck.length() < 8 )
                {
                    strCheck += "0";
                }
            }
            else
            {
                try
                {
                    strCheck = strCheck.substring( 0, 8 );
                    strCheck = Integer.toString( Integer.parseInt( strCheck ) / 100 * 100 );
                }
                catch ( Exception e )
                {
                    strCheck = "0";
                }
            }
        }
        else
        {
            strCheck = "0";
        }

        // 文字列をint型に変換。NumberFormatExceptionの場合、"0"に置き換える
        try
        {
            Integer.parseInt( strCheck );
        }
        catch ( NumberFormatException e )
        {
            strCheck = "0";
        }
        return strCheck;
    }

    /**
     * マイホテル登録データ削除処理
     * 
     * @see 任意の月のマイホテルデータを削除する。
     * @return 処理結果
     * @throws SQLException
     */
    public static void deleteData(int startMonth) throws SQLException
    {
        String query;
        int result;

        result = -1;
        if ( startMonth == 0 )
        {
            startMonth = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );
            startMonth = startMonth / 100 * 100;
        }

        query = "DELETE FROM hh_system_myhotel WHERE collect_date = ?";

        try
        {
            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, startMonth );
            result = stmt.executeUpdate();
            if ( result > 0 )
            {
                Logging.info( "[DataSystemMyHotel.deleteData] Delete:" + result + "records" );
            }
            else
            {
                Logging.info( "[DataSystemMyHotel.deleteData] Delete:no records" );
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[DataSystemMyHotel.deleteData] Exception=" + e.toString() );
            e.printStackTrace();
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            CollectMyHotelRanking.closeStatement();
        }

    }

    /**
     * マイホテル登録データ取得
     * 
     * @see 任意の月のマイホテル登録データを作成する。指定がない場合は先月
     * @return 処理結果
     * @throws SQLException
     */
    public static int collectData(int startMonth) throws SQLException
    {
        String query;
        int count;
        boolean ret;
        int endMonth;
        int nowRank;
        int nowPref;
        int nextRank;

        ret = false;
        count = 0;

        if ( startMonth == 0 )
        {
            startMonth = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );
            startMonth = startMonth / 100 * 100;
        }
        endMonth = startMonth + 99;

        query = "SELECT hh_hotel_basic.pref_id, COUNT( hh_user_myhotel.id ) AS COUNT, hh_user_myhotel.id"
                + " FROM hh_user_myhotel, hh_hotel_basic"
                + " WHERE hh_user_myhotel.del_flag = 0"
                + " AND hh_user_myhotel.append_date > ?"
                + " AND hh_user_myhotel.append_date < ?"
                + " AND hh_user_myhotel.id = hh_hotel_basic.id"
                + " GROUP BY hh_user_myhotel.id"
                + " ORDER BY hh_hotel_basic.pref_id, COUNT DESC";

        try
        {
            Logging.info( "[CollectMyHotelRanking.collectData] getMonth:" + startMonth + " to " + endMonth );

            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }
            stmt = con.prepareStatement( query );
            stmt.setInt( 1, startMonth );
            stmt.setInt( 2, endMonth );
            rs = stmt.executeQuery();
            if ( rs != null )
            {
                // レコード件数取得
                if ( rs.last() != false )
                {
                    myHotelCount = rs.getRow();
                }

                // クラスの配列を用意し、初期化する。
                myHotel = new DataSystemMyHotel[myHotelCount];

                rs.beforeFirst();

                nowRank = FIRST;
                nowPref = FIRST;
                nextRank = FIRST;
                while( rs.next() != false )
                {
                    myHotel[count] = new DataSystemMyHotel();
                    myHotel[count].setCollectDate( startMonth );
                    myHotel[count].setId( rs.getInt( "id" ) );
                    myHotel[count].setCount( rs.getInt( "COUNT" ) );
                    if ( count > 0 )
                    {
                        // 都道府県が同じ場合は次にセットする順位を増やす。
                        if ( nowPref == rs.getInt( "pref_id" ) )
                        {
                            nextRank++;
                            // 取得したマイホテルの登録数が前のデータよりも小さかったら、行番号をランクにセット
                            if ( myHotel[count].getCount() < myHotel[count - 1].getCount() )
                            {
                                nowRank = nextRank;
                            }
                        }
                        // 違う場合は都道府県IDを保持し、次にセットする順位をリセット、順位も1位にする
                        else
                        {
                            nextRank = FIRST;
                            nowRank = FIRST;
                            nowPref = rs.getInt( "pref_id" );
                        }
                    }
                    myHotel[count].setPrefRank( nowRank );
                    count++;
                }
                ret = true;
            }
        }
        catch ( SQLException e )
        {
            Logging.error( "[DataSystemMyHotel.insertData] Exception=" + e.toString() );
            e.printStackTrace();
            ret = false;
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            CollectMyHotelRanking.closeStatement();
        }

        if ( ret != false )
        {
            count = myHotelCount;
        }
        else
        {
            count = 0;
        }
        return(count);
    }

    /**
     * マイホテル全国ランキングの作成
     * 
     * @param arryMyHotel マイホテルデータ
     * @return 処理結果（null:失敗）
     * @throws SQLException
     */
    private static DataSystemMyHotel[] createRanking(DataSystemMyHotel[] arryMyHotel)
    {
        int nowRank;
        int nextRank;
        SortMyHotelByRegistration ssmh;
        String out = "";

        ssmh = new SortMyHotelByRegistration();
        try
        {
            // 登録数の多い順にソート
            Arrays.sort( arryMyHotel, ssmh );

            nowRank = FIRST;
            nextRank = ZERO;
            for( int k = 0 ; k < arryMyHotel.length ; k++ )
            {
                nextRank++;
                if ( k > 0 )
                {
                    if ( arryMyHotel[k].getCount() < arryMyHotel[k - 1].getCount() )
                    {
                        nowRank = nextRank;
                    }
                }
                arryMyHotel[k].setRank( nowRank );

                out += "id:" + arryMyHotel[k].getId() + ", regist_count:" + arryMyHotel[k].getCount() + ", rank:"
                        + arryMyHotel[k].getRank() + ", pref_rank:" + arryMyHotel[k].getPrefRank() + "\r\n";
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[DataSystemMyHotel.SortSystemMyHotel] Exception=" + e.toString() );
            arryMyHotel = null;
        }
        ssmh = null;
        return(arryMyHotel);
    }

    /**
     * ホテルごとの累計登録数を追加
     * 
     * @param arryMyHotel マイホテルデータ
     * @return 処理結果（null:失敗）
     * @throws SQLException
     */
    private static DataSystemMyHotel[] getTotalCount(DataSystemMyHotel[] arryMyHotel) throws SQLException
    {
        SortMyHotelById smi;
        String strHotelId;
        String query;
        int i;
        int count;

        strHotelId = "";
        smi = new SortMyHotelById();
        try
        {
            // ホテルID順にソート
            Arrays.sort( arryMyHotel, smi );
        }
        catch ( Exception e )
        {
            Logging.info( "getTotalCount Exception:" + e.toString() );
        }

        for( i = 0 ; i < arryMyHotel.length ; i++ )
        {
            if ( i > 0 )
            {
                strHotelId += ", ";
            }
            strHotelId += arryMyHotel[i].getId();
        }
        query = "SELECT id, COUNT( id ) AS total_count FROM hh_user_myhotel "
                + " WHERE id IN(" + strHotelId + ")"
                + " AND del_flag=0"
                + " GROUP BY id"
                + " ORDER BY id";

        try
        {
            count = 0;

            stmt = con.prepareStatement( query );
            rs = stmt.executeQuery();

            if ( rs != null )
            {
                // レコード件数取得
                if ( rs.last() != false )
                {
                    myHotelCount = rs.getRow();
                }
                rs.beforeFirst();
                while( rs.next() != false )
                {
                    // IDが同じ場合にレコードをセット
                    if ( myHotel[count].getId() == rs.getInt( "id" ) )
                    {
                        myHotel[count].setTotalCount( rs.getInt( "total_count" ) );
                        count++;
                    }
                }
            }
        }
        catch ( SQLException e )
        {
            arryMyHotel = null;
            Logging.error( "[DataSystemMyHotel.insertData] Exception=" + e.toString() );
            e.printStackTrace();
            throw e;
        }
        finally
        {
            CollectMyHotelRanking.closeStatement();
        }

        return(arryMyHotel);
    }

    /**
     * ホテルごとの累計登録数を追加
     * 
     * @param arryMyHotel マイホテルデータ
     * @return インサート処理結果（失敗:-1、成功:0より大きい数字）
     * @throws SQLException
     */
    private static int insertData(DataSystemMyHotel[] arryMyHotel)
    {
        int i;
        int count;
        int result;
        String query;

        count = 0;
        result = 0;

        query = "INSERT hh_system_myhotel SET ";
        query = query + " collect_date = ?,";
        query = query + " id = ?,";
        query = query + " rank = ?,";
        query = query + " pref_rank = ?,";
        query = query + " count = ?,";
        query = query + " total_count = ?";

        if ( arryMyHotel.length > 0 )
        {
            for( i = 0 ; i < arryMyHotel.length ; i++ )
            {
                try
                {
                    result = 0;
                    stmt = con.prepareStatement( query );
                    stmt.setInt( 1, myHotel[i].getCollectDate() );
                    stmt.setInt( 2, myHotel[i].getId() );
                    stmt.setInt( 3, myHotel[i].getRank() );
                    stmt.setInt( 4, myHotel[i].getPrefRank() );
                    stmt.setInt( 5, myHotel[i].getCount() );
                    stmt.setInt( 6, myHotel[i].getTotalCount() );
                    result = stmt.executeUpdate();
                }
                catch ( SQLException e )
                {
                    result = -1;
                    Logging.error( "[DataSystemMyHotel.InsertMyHotelRanking] Exception=" + e.toString() );
                }
                finally
                {
                    CollectMyHotelRanking.closeStatement();
                }
                // 成功したらカウントを増やす
                if ( result >= 0 )
                {
                    count++;
                }
            }
            return(count);
        }
        else
        {
            return(-1);
        }
    }

    /**
     * DBコネクション作成クラス
     * 
     * @return
     */
    private static Connection makeConnection()
    {
        try
        {
            Class.forName( driver );
            con = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * DBコネクション開放クラス
     * 
     * @return
     */
    private static void closeConnection()
    {
        try
        {
            // それぞれを閉じる
            if ( rs != null )
            {
                rs.close();
            }
            if ( stmt != null )
            {
                stmt.close();
            }
            if ( con != null )
            {
                con.close();
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * DBステートメント、リザルトセット開放クラス
     * 
     * @return
     */
    private static void closeStatement()
    {
        try
        {
            // それぞれを閉じる
            if ( rs != null )
            {
                rs.close();
            }
            if ( stmt != null )
            {
                stmt.close();
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

}
