package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApCiError;

/**
 * タッチエラープッシュ実行クラス
 * 
 * @author Keion.Park
 * 
 */
public class TouchErrorMessage
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
        String strDate = "0";
        int nDate = 0;
        try
        {
            if ( args != null && args.length > 0 )
            {
                strDate = args[0];
                nDate = Integer.parseInt( strDate );
            }
            Logging.info( "[TouchErrorMessage] :" + nDate );
            if ( nDate == 0 )
            {
                nDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -2 );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchErrorMessage] コマンドライン引数の処理失敗:" + e.toString() );
        }

        updateErrorMessage( makeConnection(), nDate );
    }

    public static boolean updateErrorMessage(Connection connection, int dayBeforeYesterday) throws Exception
    {
        boolean ret = false;
        String query = "";
        String selectSql = "";
        String insertSql = "";
        String updateSql = "";
        boolean isHost;
        ResultSet result = null;
        ResultSet resultMsg = null;
        DataApCiError dace = new DataApCiError();
        PreparedStatement prestate = null;

        final String reason = "原因不明";

        // チェックイン日付が2日前のもので金額がはいっていない分を抽出してap_ci_errorに書き込む。
        query = "SELECT ci_main.id,ci_main.seq,hm.front_ip,ci_main.ci_date,ci_main.ci_time,ci_main.room_no,ci_main.user_id,ci_main.rsv_no,";
        query += " 0 AS kind,'' AS reason,ci_main.amount,ci_main.use_point,0 AS reflect_flag,0 AS error_code FROM hh_hotel_ci ci_main";
        query += " INNER JOIN hh_hotel_basic basic ON basic.id = ci_main.id ";
        query += " INNER JOIN hh_hotel_master hm ON hm.id = ci_main.id AND hm.front_ip <> '' AND hm.touch_sync_flag = 1";
        query += " INNER JOIN hh_user_basic `user` ON `user`.user_id = ci_main.user_id AND `user`.login_flag=0 ";
        query += " LEFT JOIN hh_bko_account_recv recv ON ci_main.id =  recv.id AND ci_main.seq = recv.ci_seq ";
        query += " WHERE ci_main.id < 100000000 AND NOT EXISTS";
        query += " (SELECT 1 FROM hh_hotel_ci ci_sub WHERE ci_main.id = ci_sub.id AND ci_main.seq = ci_sub.seq AND ci_main.sub_seq < ci_sub.sub_seq)";
        query += "AND ci_main.ci_date = ? AND ci_main.amount = 0 AND ci_main.ci_status IN (0,1)";
        query += "AND  (ci_seq IS NULL OR rsv_no<>'' AND usage_charge = 0)";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, dayBeforeYesterday );
            result = prestate.executeQuery();
            while( result.next() != false )
            {
                dace.setData( result );
                selectSql = " SELECT * FROM ap_ci_error WHERE id = ? AND seq = ? ";
                insertSql = " INSERT INTO ap_ci_error (id, seq, front_ip,ci_date,ci_time,room_no,user_id, ";
                insertSql += " rsv_no,kind,reason,amount,use_point,reflect_flag) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                updateSql = " UPDATE ap_ci_error SET front_ip = ?,  ci_date = ?, ci_time = ?,room_no = ?,user_id = ?,";
                updateSql += " rsv_no = ?, kind = ?,reason = ?,amount = ?,use_point = ?, reflect_flag = ? WHERE id = ? AND seq = ?";
                prestate = connection.prepareStatement( selectSql );
                prestate.setInt( 1, dace.getId() );
                prestate.setInt( 2, dace.getSeq() );
                resultMsg = prestate.executeQuery();
                // データが入っていれば更新、入ってなかったら書き込む。
                if ( resultMsg.next() != false )
                {
                    prestate = connection.prepareStatement( updateSql );
                    prestate.setString( 1, dace.getFrontIp() );
                    prestate.setInt( 2, dace.getCiDate() );
                    prestate.setInt( 3, dace.getCiTime() );
                    prestate.setString( 4, dace.getRoomNo() );
                    prestate.setString( 5, dace.getUserId() );
                    prestate.setString( 6, dace.getRsvNo() );
                    prestate.setInt( 7, dace.getKind() );
                    prestate.setString( 8, reason );
                    prestate.setInt( 9, dace.getAmount() );
                    prestate.setInt( 10, dace.getUsePoint() );
                    prestate.setInt( 11, dace.getReflectFlag() );
                    prestate.setInt( 12, dace.getId() );
                    prestate.setInt( 13, dace.getSeq() );
                    prestate.executeUpdate();
                }
                else
                {
                    prestate = connection.prepareStatement( insertSql );
                    prestate.setInt( 1, dace.getId() );
                    prestate.setInt( 2, dace.getSeq() );
                    prestate.setString( 3, dace.getFrontIp() );
                    prestate.setInt( 4, dace.getCiDate() );
                    prestate.setInt( 5, dace.getCiTime() );
                    prestate.setString( 6, dace.getRoomNo() );
                    prestate.setString( 7, dace.getUserId() );
                    prestate.setString( 8, dace.getRsvNo() );
                    prestate.setInt( 9, dace.getKind() );
                    prestate.setString( 10, reason );
                    prestate.setInt( 11, dace.getAmount() );
                    prestate.setInt( 12, dace.getUsePoint() );
                    prestate.setInt( 13, dace.getReflectFlag() );
                    prestate.executeUpdate();
                }

                // 抽出データで同ホテル同日時同部屋同ユーザのチェックインデータがあるのなら、無効データに変更する。また、その旨理由ap_ci_errorに書き込む
                query = "SELECT 1 FROM hh_hotel_ci ci_main";
                query += " WHERE ci_main.id =? AND ci_main.seq <>? AND ci_main.ci_date=? ";
                query += " AND ci_main.ci_time = ? AND ci_main.room_no=?  ";
                query += " AND ci_main.ci_status =1";
                query += " AND NOT EXISTS ";
                query += " (SELECT 1 FROM hh_hotel_ci ci_sub WHERE ci_main.id = ci_sub.id AND ci_main.seq = ci_sub.seq AND ci_main.sub_seq < ci_sub.sub_seq) ";
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, dace.getId() );
                prestate.setInt( 2, dace.getSeq() );
                prestate.setInt( 3, dace.getCiDate() );
                prestate.setInt( 4, dace.getCiTime() );
                prestate.setString( 5, dace.getRoomNo() );
                resultMsg = prestate.executeQuery();
                if ( resultMsg.next() != false )
                {
                    updateSql = "UPDATE hh_hotel_ci SET ci_status = 3 WHERE id=? AND seq = ?";
                    prestate = connection.prepareStatement( updateSql );
                    prestate.setInt( 1, dace.getId() );
                    prestate.setInt( 2, dace.getSeq() );
                    prestate.executeUpdate();

                    updateSql = "UPDATE ap_ci_error SET kind = 4,reason = '無効データ' WHERE id = ? AND seq = ?";
                    prestate = connection.prepareStatement( updateSql );
                    prestate.setInt( 1, dace.getId() );
                    prestate.setInt( 2, dace.getSeq() );
                    prestate.executeUpdate();
                    continue;
                }

                // 利用金額がマイナスで受信されたら、その旨、理由を書き込む。！！！
                query = "SELECT 1 FROM `hotenavi`.`ap_touch_history` WHERE id=? AND ci_seq=? ";
                query += " AND detail LIKE '%method=ModifyCi%' AND detail LIKE '%price=-%'";
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, dace.getId() );
                prestate.setInt( 2, dace.getSeq() );
                resultMsg = prestate.executeQuery();
                if ( resultMsg.next() != false )
                {
                    updateSql = "UPDATE ap_ci_error SET kind = 6,reflect_flag=-1,reason = '利用金額がマイナスで受信されました'  WHERE id = ? AND seq = ?";
                    prestate = connection.prepareStatement( updateSql );
                    prestate.setInt( 1, dace.getId() );
                    prestate.setInt( 2, dace.getSeq() );
                    prestate.executeUpdate();
                    continue;
                }

                isHost = true;
                // 予約番号がない場合、HtHomeの電文があるか否かをチェック、なければチェックイン電文を送っていない旨、理由を書き込む。
                if ( dace.getRsvNo() == null || dace.getRsvNo().equals( "" ) )
                {
                    query = "SELECT 1 FROM `hotenavi`.`ap_touch_history` WHERE id=? AND ci_seq=? AND detail LIKE '%method=HtHome%'";
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, dace.getId() );
                    prestate.setInt( 2, dace.getSeq() );
                    resultMsg = prestate.executeQuery();
                    if ( resultMsg.next() == false )
                    {
                        updateSql = "UPDATE ap_ci_error SET kind = 2,reason = 'チェックイン電文を送っていません' WHERE id = ? AND seq = ?";
                        prestate = connection.prepareStatement( updateSql );
                        prestate.setInt( 1, dace.getId() );
                        prestate.setInt( 2, dace.getSeq() );
                        prestate.executeUpdate();
                    }
                    else
                    {
                        isHost = false;
                    }
                }
                else
                {
                    // 予約番号がある場合、ModifyCi電文がある場合はホテルが運用を間違っている可能性が大きい旨、理由を書き込む。
                    query = "SELECT 1 FROM `hotenavi`.`ap_touch_history` WHERE id=? ";
                    query += " AND ci_seq=? AND detail LIKE '%method=ModifyCi%' AND detail LIKE '%price=0%'";
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, dace.getId() );
                    prestate.setInt( 2, dace.getSeq() );
                    resultMsg = prestate.executeQuery();
                    if ( resultMsg.next() != false )
                    {
                        updateSql = "UPDATE ap_ci_error SET kind = 5,reflect_flag=-1,reason = 'ホテルの運用間違いです' WHERE id = ? AND seq = ?";
                        prestate = connection.prepareStatement( updateSql );
                        prestate.setInt( 1, dace.getId() );
                        prestate.setInt( 2, dace.getSeq() );
                        prestate.executeUpdate();
                        continue;
                    }

                    // 予約番号がある場合、ap_error_historyを見て、ホスト経由でない場合はタッチPCの設定が間違っている旨、理由を書き込む。
                    query = "SELECT 1 FROM ap_error_history WHERE error_code=20407 AND id=?  AND reserve_no=?";
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, dace.getId() );
                    prestate.setString( 2, dace.getRsvNo() );
                    resultMsg = prestate.executeQuery();
                    if ( resultMsg.next() != false )
                    {
                        updateSql = "UPDATE ap_ci_error SET kind = 3,reason = 'タッチPCの設定が間違えています' WHERE id = ? AND seq = ?";
                        prestate = connection.prepareStatement( updateSql );
                        prestate.setInt( 1, dace.getId() );
                        prestate.setInt( 2, dace.getSeq() );
                        prestate.executeUpdate();
                    }
                }
                if ( !isHost )
                {
                    // 前回のModifyCi電文の日時を取得していつから通信できていないかをチェック。理由に書き込む。
                    query = "SELECT regist_date,regist_time FROM `hotenavi`.`ap_touch_history` WHERE id=? ";
                    query += " AND (detail LIKE 'http://172.25.32.26:8080/hapiTouch.act?method=ModifyCi%' ";
                    query += " OR detail LIKE 'http://172.25.5.19:8080/hapiTouch.act?method=ModifyCi%') ORDER BY `seq` DESC LIMIT 0,1";
                    prestate = connection.prepareStatement( query );
                    prestate.setInt( 1, dace.getId() );
                    resultMsg = prestate.executeQuery();
                    if ( resultMsg.next() != false )
                    {
                        int registDate = resultMsg.getInt( "regist_date" );
                        int registTime = resultMsg.getInt( "regist_time" );
                        updateSql = "UPDATE ap_ci_error SET kind=4 , reason ='" + DateEdit.getDate( 1, registDate ) + "の" + DateEdit.getTime( 0, registTime ) + "から";
                        updateSql += "利用金額が送られてきていません。ホスト側設定を確認してください。' WHERE id = ? AND seq = ?";
                        prestate = connection.prepareStatement( updateSql );
                        prestate.setInt( 1, dace.getId() );
                        prestate.setInt( 2, dace.getSeq() );
                        prestate.executeUpdate();
                    }
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchErrorMessage.updateErrorMessage] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }
}
