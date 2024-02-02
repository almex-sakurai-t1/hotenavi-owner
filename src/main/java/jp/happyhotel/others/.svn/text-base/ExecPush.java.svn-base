package jp.happyhotel.others;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApPush;
import jp.happyhotel.data.DataApPushData;
import jp.happyhotel.data.DataApPushDataList;
import jp.happyhotel.data.DataApPushDataListExtend;
import jp.happyhotel.data.DataApTokenUser;
import jp.happyhotel.data.DataApUserCampaign;
import jp.happyhotel.touch.GooglePushMapper;
import jp.happyhotel.touch.Push2Android;
import jp.happyhotel.touch.Push2Iphone;

/**
 * プッシュ通知実行クラス
 * 
 * @author K.Mitsuhashi
 * 
 */

public class ExecPush
{

    static final int                           ANDROID              = 2;
    static final int                           PUSH                 = 1;
    static final int                           MAXPUSH_a            = 100;                                      // 1回にPUSHするトークン数(Android用)
    static final int                           MAXPUSH_i            = 100;                                      // 1回にPUSHするトークン数(iOS用)
    static final int                           STATUS_IN_PROCECCING = 9;
    static final int                           STATUS_ERROR         = 11;
    static final int                           STATUS_BLOKKING_PUSH = 4;
    static final String                        configFilePath       = "//etc//happyhotel//push.conf";
    static final int                           apdlRowCount         = 1000;                                     // 1回に処理するホテルプッシュ配信データ(ap_push_hotel)件数

    static DataApPushData                      dataApPushData       = new DataApPushData();
    static DataApPushDataListExtend[]          dataApPushDataList   = null;
    static ArrayList<DataApPush>               arrPush_a            = new ArrayList<DataApPush>();              // 1回分毎の送信トークンをセット(100件）
    static ArrayList<DataApPush>               arrPush_i            = new ArrayList<DataApPush>();              // 1回分毎の送信トークンをセット(100件）
    static ArrayList<DataApPushDataListExtend> arrPush_x            = new ArrayList<DataApPushDataListExtend>(); // OSタイプ不明で「不正トークン」にするトークン全件
    static String                              campaignMsg          = "";

    static Connection                          con                  = null;                                     // Database connection
    static PreparedStatement                   stmt                 = null;
    static ResultSet                           rs                   = null;

    static String                              DB_URL;                                                          // URL for database server
    static String                              user;                                                            // DB user
    static String                              password;                                                        // DB password
    static String                              driver;                                                          // DB driver
    static String                              jdbcds;                                                          // DB jdbcds

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

    /*
     * public ExecPush()
     * {
     * this.dataApPushDataList = null;
     * }
     */

    /**
     * メイン
     * 
     */

    public static void main(String[] args)
    {

        // 本日の日付取得
        int now_time = Integer.parseInt( DateEdit.getTime( 1 ) );
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            // CampaignMessage campaignMessage = new CampaignMessage();
            // campaignMessage.sendCampaignMessage(now_date, now_time);
            sendCampaignMessage( now_date, now_time );

        }
        catch ( Exception e )
        {
            Logging.error( "[ExecPush] ERROR:" + e.toString() );
        }
    }

    /***
     * プッシュ配信データを取得
     * 
     * @param date 配信希望日付
     * @param time 配信希望時刻
     * @return
     */
    public static boolean getApPushData(int date, int time, Connection dbcon) throws Exception
    {

        boolean ret = false;
        String query;
        dataApPushData = new DataApPushData();

        // PUSH配信希望日時のデータを取得
        query = "SELECT *, ACM.campaign_id, ACM.detail  FROM ap_push_data APD";
        query += " INNER JOIN ap_campaign_master ACM ON APD.campaign_id = ACM.campaign_id";
        query += " WHERE APD.status = 2 "; // 配信承認
        query += "  AND APD.del_flag = 0 ";
        query += "  AND ACM.del_flag = 0 ";
        query += "  AND ( APD.desired_date < ? OR ";
        query += "  ( APD.desired_date = ?";
        query += "  AND APD.desired_time <= ? ))";
        query += "  ORDER BY APD.push_seq";
        query += "  LIMIT 1";

        try
        {
            stmt = dbcon.prepareStatement( query );
            stmt.setInt( 1, date );
            stmt.setInt( 2, date );
            stmt.setInt( 3, time );
            rs = stmt.executeQuery();

            if ( rs != null )
            {
                if ( rs.next() != false )
                {
                    if ( dataApPushData.setData( rs ) )
                    {
                        campaignMsg = rs.getString( "detail" );
                        campaignMsg = campaignMsg.replace( "\\", "￥" );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CampaignMessage.getApPushData()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }
        return(ret);
    }

    /***
     * キャンペーン用プッシュ配信データリストを取得
     * 
     * @param push_seq プッシュ配信連番
     * @return
     */
    public static boolean getApPushDataList(int push_seq, int apli_kind, Connection dbcon) throws Exception
    {

        boolean ret = false;
        int count = 0;
        String query = "";

        // 初期化
        dataApPushDataList = null;

        if ( apli_kind == 1 )
        {
            // ハピホテアプリに配信のとき
            query = "SELECT apdl.*";
            query += "  ,ifnull(atu.os_type, -1) as os_type ";
            query += "  ,ifnull(aupc.push_flag, 1) as push_flag ";
            query += "  ,ifnull(aupc.co_flag,1) as co_flag ";
            query += "  ,ifnull(aupc.campaign_flag, 1) as campaign_flag";
            query += " FROM ap_push_data_list as apdl";
            query += " LEFT JOIN ap_token_user AS atu";
            query += "   ON apdl.user_id = atu.user_id";
            query += "   AND apdl.token = atu.token";
            query += " LEFT JOIN ap_user_push_config AS aupc";
            query += "   ON apdl.user_id = aupc.user_id";
            query += " WHERE apdl.type = 1 "; // キャンペーン
            query += "  AND apdl.status = 0 ";// 未送信
            query += "  AND apdl.push_seq = ? ";
            query += "  ORDER BY atu.os_type, apdl.user_id DESC";
            query += "  LIMIT 0," + apdlRowCount;
        }
        else if ( apli_kind == 10 )
        {
            // 予約アプリに配信のとき
            /*
             * query = "SELECT apdl.*";
             * query += "  ,ifnull(tkn.os_type, -1) as os_type ";
             * query += "  ,ifnull(config.push_flag, 1) as push_flag ";
             * query += "  ,ifnull(config.co_flag, 1) as co_flag ";
             * query += "  ,ifnull(config.campaign_flag, 1) as campaign_flag";
             * query += " FROM ap_push_data_list AS apdl ";
             * query += " LEFT JOIN ( SELECT token, device_type AS os_type FROM newRsvDB.ap_rsv_token_uuid AS artu ";
             * query += "              INNER JOIN newRsvDB.ap_rsv_uuid AS aru";
             * query += "               ON artu.uuid = aru.uuid ) AS tkn";
             * query += " ON apdl.token = tkn.token";
             * query += " LEFT JOIN  ";
             * query += "( SELECT artu.token AS token,aruu.user_id AS user_id, arupc.push_flag AS push_flag, arupc.campaign_flag AS campaign_flag, 0 AS co_flag FROM newRsvDB.ap_rsv_uuid_user AS aruu ";
             * query += " INNER JOIN  newRsvDB.ap_rsv_user_push_config AS arupc ON aruu.uuid = arupc.uuid ";
             * query += " INNER JOIN newRsvDB.ap_rsv_token_uuid AS artu ON aruu.uuid = artu.uuid";
             * query += " ) AS config";
             * query += " ON config.user_id = apdl.user_id";
             * query += " AND config.token = apdl.token";
             * query += " WHERE apdl.type = 1 "; // キャンペーン
             * query += "  AND apdl.status = 0 ";// 未送信
             * query += "  AND apdl.push_seq = ? ";
             * query += "  ORDER BY tkn.os_type, apdl.user_id DESC";
             * query += "  LIMIT 0," + apdlRowCount;
             */
            // スロークエリにとなり処理に異常な時間を要するので以下のクエリで実行
            query = "SELECT apdl.*";
            query += "  ,ifnull(config.os_type, -1) as os_type ";
            query += "  ,ifnull(config.push_flag, 1) as push_flag ";
            query += "  ,CASE WHEN config.co_flag IS NULL THEN 1 ELSE 0 END as co_flag ";
            query += "  ,ifnull(config.campaign_flag, 1) as campaign_flag";
            query += " FROM ap_push_data_list AS apdl ";
            query += " LEFT JOIN ( SELECT device_type AS os_type,artu.token AS token,aruu.user_id AS user_id, arupc.push_flag AS push_flag, arupc.campaign_flag AS campaign_flag, 0 AS co_flag FROM newRsvDB.ap_rsv_uuid_user AS aruu ";
            query += "          INNER JOIN newRsvDB.ap_rsv_user_push_config AS arupc ON aruu.uuid = arupc.uuid  ";
            query += "          INNER JOIN newRsvDB.ap_rsv_token_uuid AS artu ON aruu.uuid = artu.uuid";
            query += "          INNER JOIN newRsvDB.ap_rsv_uuid AS aru ON  artu.uuid = aru.uuid ";
            query += "          INNER JOIN ap_push_data_list AS apdl ON apdl.token = artu.token";
            query += "          AND apdl.type = 1  ";
            query += "          AND apdl.status = 0 ";
            query += "          AND apdl.push_seq = ?";
            query += " ) AS config";
            query += " ON config.user_id = apdl.user_id";
            query += " AND config.token = apdl.token";
            query += " WHERE apdl.type = 1 "; // キャンペーン
            query += "  AND apdl.status = 0 ";// 未送信
            query += "  AND apdl.push_seq = ? ";
            query += "  LIMIT 0," + apdlRowCount;

        }
        else if ( apli_kind == 2 )
        {
            // 新ハピホテアプリに配信のとき
            query = "SELECT apdl.*";
            query += "  ,ifnull(config.os_type, -1) as os_type ";
            query += "  ,ifnull(config.push_flag, 1) as push_flag ";
            query += "  ,CASE WHEN config.co_flag IS NULL THEN 1 ELSE 0 END as co_flag ";
            query += "  ,ifnull(config.campaign_flag, 1) as campaign_flag";
            query += " FROM ap_push_data_list AS apdl ";
            query += " LEFT JOIN ( SELECT os_type AS os_type,artu.token AS token,aruu.user_id AS user_id, arupc.push_flag AS push_flag, arupc.campaign_flag AS campaign_flag, 0 AS co_flag FROM ap_uuid_user AS aruu ";
            query += "          INNER JOIN ap_uuid_push_config AS arupc ON aruu.uuid = arupc.uuid  ";
            query += "          INNER JOIN ap_token_uuid AS artu ON aruu.uuid = artu.uuid";
            query += "          INNER JOIN ap_push_data_list AS apdl ON apdl.token = artu.token";
            query += "          AND apdl.type = 1  ";
            query += "          AND apdl.status = 0 ";
            query += "          AND apdl.push_seq = ?";
            query += " ) AS config";
            query += " ON config.user_id = apdl.user_id";
            query += " AND config.token = apdl.token";
            query += " WHERE apdl.type = 1 "; // キャンペーン
            query += "  AND apdl.status = 0 ";// 未送信
            query += "  AND apdl.push_seq = ? ";
            query += "  LIMIT 0," + apdlRowCount;
        }
        try
        {

            stmt = dbcon.prepareStatement( query );
            if ( apli_kind == 1 )
            {
                stmt.setInt( 1, push_seq );
            }
            else
            {
                stmt.setInt( 1, push_seq );
                stmt.setInt( 2, push_seq );
            }
            rs = stmt.executeQuery();

            if ( rs != null )
            {

                // レコード件数取得
                if ( rs.last() != false )
                {
                    // Logging.info( "[ExecPush.getApPushDataList()] rs.getRow()=" + rs.getRow() );
                    count = rs.getRow();
                    // クラスの配列を用意し、初期化する。
                    dataApPushDataList = new DataApPushDataListExtend[count];
                    ret = true;
                }
                rs.beforeFirst();
                count = 0;
                while( rs.next() != false )
                {
                    dataApPushDataList[count] = new DataApPushDataListExtend();
                    dataApPushDataList[count].setData( rs );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CampaignMessage.getApPushDataList()] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( rs );
            releaseResources( stmt );
        }
        // Logging.info( "[ExecPush.getApPushDataList()] ret=" + ret );
        return(ret);
    }

    /***
     * キャンペーン用PUSH通知
     * 
     * @param userId
     * @param id
     * @return
     */
    public static boolean sendCampaignMessage(int date, int time)
    {
        boolean ret = true;
        int target_PushSeq = 0;
        int target_CampaignId = 0;
        int target_ApliKind = 0;

        // URL情報を取得
        FileInputStream propfile = null;
        Properties config;
        String Url = "";
        String UrlRsv = "";
        try
        {
            propfile = new FileInputStream( configFilePath );
            config = new Properties();
            config.load( propfile );
            Url = String.valueOf( config.getProperty( "url" ) );
            UrlRsv = String.valueOf( config.getProperty( "urlrsv" ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[CampaignMessage]conf file failed Exception:" + e.toString() );
            return false;
        }

        try
        {
            // コネクションを作る
            if ( con == null )
            {
                con = makeConnection();
            }

            // PUSH通知するトークンリスト取得
            if ( getApPushData( date, time, con ) )
            {
                // 処理対象情報セット
                target_PushSeq = dataApPushData.getPushSeq();
                target_CampaignId = dataApPushData.getCampaignId();
                target_ApliKind = dataApPushData.getApliKind();

                // プッシュ配信データ(ap_push_data)配信日時更新
                if ( !updatePushData( target_PushSeq, STATUS_IN_PROCECCING, con ) )
                {
                    Logging.error( "CampaignMessage[this.updatePushData()]" );
                    throw new Exception();
                }
                // Logging.info( "ap_push_data date update, target_PushSeq=" + target_PushSeq );

                // URL作成（jspを開いたときにap_push_data_listのconfirmdate等を更新するため、キー項目を送る）
                String param = "";
                param = "?cpid=" + +target_CampaignId;
                param += "&type=1";// 常に1（キャンペーン）
                param += "&push_seq=" + target_PushSeq;
                param += "&apli_kind=" + target_ApliKind;
                String url = "";
                if ( target_ApliKind == 1 || target_ApliKind == 2 )
                {
                    url = Url + param;
                }
                else
                {
                    url = UrlRsv + param;
                }

                // プッシュ配信データリストを取得し、OSごとに配信用トークン配列(arrPush_a,arrPush_i,arrPush_x)を作成する
                makePushArr( target_PushSeq, target_ApliKind, con );
                // Logging.info( "arrPush_a.size()=" + arrPush_a.size() + " arrPush_i.size()=" + arrPush_i.size() + "arrPush_x.size()=" + arrPush_x.size() );

                // android送信＆データ更新
                pushAndroid( target_CampaignId, target_ApliKind, url, con );

                // iPhone送信＆データ更新
                pushiPhone( target_CampaignId, target_ApliKind, url, con );

                // OSタイプ不明のプッシュ配信データリスト(ap_push_data_list)更新
                updatePushDataList( arrPush_x, con );

                // プッシュ配信データ(ap_push_data)Status更新
                // Logging.info( "push (ap_push_data)Status update:" + DateEdit.getDate( 1 ) + DateEdit.getTime( 0 ) );
                updatePushDataStatus( target_PushSeq, con );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Logging.error( "[CampaignMessage.sendCampaignMessage()] Exception=" + e.toString() );

            // プッシュ配信データ(ap_push_data)配信エラーにする
            if ( !updatePushData( target_PushSeq, STATUS_ERROR, con ) )
            {
                Logging.error( "CampaignMessage[this.updatePushData()]status=10" );
            }
            ret = false;

        }
        finally
        {
            // コネクションを閉じる
            releaseResources( rs );
            releaseResources( stmt );
            releaseResources( con );
        }

        return ret;
    }

    /****
     * PUSH配信用配列作成
     * 
     * @param target_PushSeq
     * @param target_ApliKind
     * @param Connection
     * @return
     */
    public static boolean makePushArr(int target_PushSeq, int target_ApliKind, Connection dbcon)
    {
        boolean rtn = true;
        arrPush_a.clear();
        arrPush_i.clear();
        arrPush_x.clear();
        try
        {
            if ( getApPushDataList( target_PushSeq, target_ApliKind, con ) )
            {
                if ( dataApPushDataList != null )
                {
                    // 変数初期化
                    int cnt_i = 0;
                    int cnt_a = 0;
                    String tmp_i = "";
                    String tmp_a = "";
                    ArrayList<DataApPushDataListExtend> listDt_a = new ArrayList<DataApPushDataListExtend>();
                    ArrayList<DataApPushDataListExtend> listDt_i = new ArrayList<DataApPushDataListExtend>();

                    for( int j = 0 ; j < dataApPushDataList.length ; j++ )
                    {
                        if ( j == 0 )
                        {
                            Logging.info( "list start" );
                        }
                        // iOS
                        if ( dataApPushDataList[j].getOsType() == 1 )
                        {
                            if ( dataApPushDataList[j].getPushFlag() == 1 && dataApPushDataList[j].getCampaignFlag() == 1 )
                            {
                                tmp_i += "," + dataApPushDataList[j].getToken();
                            }
                            listDt_i.add( dataApPushDataList[j] );
                            cnt_i++;
                            if ( cnt_i >= MAXPUSH_i )
                            {
                                DataApPush dp = new DataApPush();
                                if ( tmp_i.length() > 0 )
                                {
                                    dp.setTokens( tmp_i.substring( 1, tmp_i.length() ) );
                                }
                                dp.setListPl( listDt_i );
                                arrPush_i.add( dp );
                                // 変数クリア
                                cnt_i = 0;
                                tmp_i = "";
                                listDt_i = new ArrayList<DataApPushDataListExtend>();
                            }
                        }
                        // android
                        else if ( dataApPushDataList[j].getOsType() == 2 )
                        {
                            if ( dataApPushDataList[j].getPushFlag() == 1 && dataApPushDataList[j].getCampaignFlag() == 1 )
                            {
                                tmp_a += "," + dataApPushDataList[j].getToken();
                            }
                            listDt_a.add( dataApPushDataList[j] );
                            cnt_a++;
                            if ( cnt_a >= MAXPUSH_a )
                            {
                                DataApPush dp = new DataApPush();
                                if ( tmp_a.length() > 0 )
                                {
                                    dp.setTokens( tmp_a.substring( 1, tmp_a.length() ) );
                                }
                                dp.setListPl( listDt_a );
                                arrPush_a.add( dp );
                                // 変数クリア
                                cnt_a = 0;
                                tmp_a = "";
                                listDt_a = new ArrayList<DataApPushDataListExtend>();
                            }
                        }
                        // トークンユーザー(ap_token_user)またはアプリ(ap_rsv_uuid)が存在しないとき
                        else
                        {
                            arrPush_x.add( dataApPushDataList[j] );
                        }
                    }

                    // MAXPUSH未満のデータをセット
                    DataApPush dpi = new DataApPush();
                    if ( tmp_i.length() > 0 )
                    {
                        dpi.setTokens( tmp_i.substring( 1, tmp_i.length() ) );
                    }
                    if ( listDt_i.size() > 0 )
                    {
                        dpi.setListPl( listDt_i );
                        arrPush_i.add( dpi );
                    }
                    DataApPush dpa = new DataApPush();
                    if ( tmp_a.length() > 0 )
                    {
                        dpa.setTokens( tmp_a.substring( 1, tmp_a.length() ) );
                    }
                    if ( listDt_a.size() > 0 )
                    {
                        dpa.setListPl( listDt_a );
                        arrPush_a.add( dpa );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ExecPush] Exception=" + e.toString() );
            e.printStackTrace();
            rtn = false;
        }
        return rtn;
    }

    /****
     * PUSH配信とデータ更新(Android)
     * 
     * @param target_PushSeq
     * @param target_ApliKind
     * @param Connection
     * @return
     */
    public static boolean pushAndroid(int target_CampaignId, int target_ApliKind, String url, Connection dbcon)
    {
        // Logging.info( "android Send:" + DateEdit.getDate( 1 ) + DateEdit.getTime( 0 ) + ",this.arrPush_a.size()=" + arrPush_a.size() );

        boolean rtn = true;
        Push2Android p2a = new Push2Android();

        for( int j = 0 ; j < arrPush_a.size() ; j++ )
        {
            if ( j == 0 )
            {
                Logging.info( "android Send START" );
            }
            p2a = new Push2Android();
            GooglePushMapper gpm = new GooglePushMapper();
            gpm.addRegId( arrPush_a.get( j ).getTokens() );
            gpm.createData( campaignMsg, url );
            p2a.setApliKind( target_ApliKind );
            if ( p2a.push( gpm ) )
            {
                // Logging.info( "push result Android:" + arrPush_a.get( j ).getTokens() );

                // 送信エラートークン配列取得
                ArrayList<String> inactiveTokenTmp = new ArrayList<String>();
                inactiveTokenTmp = p2a.getInactiveToken();
                // 不正エラートークン配列取得
                ArrayList<String> invalidTokenTmp = new ArrayList<String>();
                invalidTokenTmp = p2a.getInvalidToken();

                // 大文字変換
                ArrayList<String> inactiveToken = new ArrayList<String>();
                if ( inactiveTokenTmp != null )
                {
                    for( int m = 0 ; m < inactiveTokenTmp.size() ; m++ )
                    {
                        inactiveToken.add( inactiveTokenTmp.get( m ).toUpperCase() );
                    }
                }
                ArrayList<String> invalidToken = new ArrayList<String>();
                if ( invalidTokenTmp != null )
                {
                    for( int m = 0 ; m < invalidTokenTmp.size() ; m++ )
                    {
                        invalidToken.add( invalidTokenTmp.get( m ).toUpperCase() );
                    }
                }
                Connection conSub = null;
                conSub = makeConnection();

                // ap_push_data_list更新 (更新失敗データが存在しても処理続行)
                updatePushDataList( arrPush_a.get( j ), inactiveToken, invalidToken, conSub );

                // ap_user_campaignデータ作成（または更新）
                updateUserCampaign( arrPush_a.get( j ), target_CampaignId, target_ApliKind, conSub );

                // ap_token_user またはap_rsv_token_uuidのエラーフラグ更新
                updateTokenErrorFlag( arrPush_a.get( j ), inactiveToken, invalidToken, target_ApliKind, conSub );

                releaseResources( conSub );
            }
            else
            {
                rtn = false;
            }
        }
        return rtn;
    }

    /****
     * PUSH配信とデータ更新(iPhone)
     * 
     * @param target_PushSeq
     * @param target_ApliKind
     * @param Connection
     * @return
     */
    public static boolean pushiPhone(int target_CampaignId, int target_ApliKind, String url, Connection dbcon)
    {
        // Logging.info( "iPhone Send:" + DateEdit.getDate( 1 ) + DateEdit.getTime( 0 ) + " this.arrPush_i.size()=" + arrPush_i.size() );

        boolean rtn = true;
        Push2Iphone p2i = new Push2Iphone();

        for( int j = 0 ; j < arrPush_i.size() ; j++ )
        {
            if ( j == 0 )
            {
                Logging.info( "iPhone send START" );
                // Logging.info( "this.arrPush_i.get(j).getTokens()=" + arrPush_i.get( j ).getTokens() );
            }
            p2i = new Push2Iphone();
            p2i.setApliKind( target_ApliKind );
            if ( p2i.push( arrPush_i.get( j ).getTokens(), campaignMsg, url ) )
            {
                // Logging.info( "push result (iOS）" + arrPush_i.get( j ).getTokens() );

                // 送信エラートークン配列取得
                ArrayList<String> inactiveTokenTmp = new ArrayList<String>();
                inactiveTokenTmp = p2i.getInactiveToken();
                // 不正エラートークン配列取得
                ArrayList<String> invalidTokenTmp = new ArrayList<String>();
                invalidTokenTmp = p2i.getInvalidToken();

                // 大文字変換
                ArrayList<String> inactiveToken = new ArrayList<String>();
                if ( inactiveTokenTmp != null )
                {
                    for( int m = 0 ; m < inactiveTokenTmp.size() ; m++ )
                    {
                        inactiveToken.add( inactiveTokenTmp.get( m ).toUpperCase() );
                    }
                }
                ArrayList<String> invalidToken = new ArrayList<String>();
                if ( invalidTokenTmp != null )
                {
                    for( int m = 0 ; m < invalidTokenTmp.size() ; m++ )
                    {
                        invalidToken.add( invalidTokenTmp.get( m ).toUpperCase() );
                    }
                }

                // Logging.info( "inactiveToken.size()=" + inactiveToken.size() );
                Connection conSub = null;
                conSub = makeConnection();
                // ap_push_data_list更新
                updatePushDataList( arrPush_i.get( j ), inactiveToken, invalidToken, conSub );

                // ap_user_campaignデータ作成（または更新）
                updateUserCampaign( arrPush_i.get( j ), target_CampaignId, target_ApliKind, conSub );

                // ap_token_user またはap_rsv_token_uuidのエラーフラグ更新
                updateTokenErrorFlag( arrPush_i.get( j ), inactiveToken, invalidToken, target_ApliKind, conSub );
                releaseResources( conSub );

            }
            else
            {
                rtn = false;
            }
        }
        return rtn;
    }

    /****
     * PUSH配信データリスト更新
     * 
     * @param id
     * @param pushSeq
     * @param userId
     * @param token
     * @return
     */
    public static boolean updatePushDataList(DataApPush dp, ArrayList<String> inactiveToken, ArrayList<String> invalidToken, Connection dbcon)
    {
        // Logging.error( "[CampaignMessage.updatePushDataList()] dp.getListPl().size()="+dp.getListPl().size(),"CampaignMessage" );
        boolean ret = true;

        for( int i = 0 ; i < dp.getListPl().size() ; i++ )
        {
            DataApPushDataListExtend listPl = new DataApPushDataListExtend();
            listPl = dp.getListPl().get( i );
            DataApPushDataList dapdl = new DataApPushDataList();
            if ( dapdl.getData( dbcon, listPl.getType(), listPl.getId(), listPl.getPushSeq(), listPl.getUserId(), listPl.getToken() ) )
            {
                if ( listPl.getPushFlag() == 1 && listPl.getCampaignFlag() == 1 )
                {

                    // トークンの状態を取得
                    int status = chkTokenStatus( listPl.getToken(), inactiveToken, invalidToken );

                    if ( status == 0 )
                    {
                        dapdl.setStatus( 1 );// トークンが正常のとき送信済みにする
                    }
                    else
                    {
                        dapdl.setStatus( status );// 送信エラーまたは、トークン不正
                    }

                }
                else
                {
                    dapdl.setStatus( STATUS_BLOKKING_PUSH );// PUSH通知拒否データはステータスを着信拒否にする
                }
                dapdl.setSendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dapdl.setSendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( !dapdl.updateData( dbcon, listPl.getType(), listPl.getId(), listPl.getPushSeq(), listPl.getUserId(), listPl.getToken() ) )
                {
                    ret = false;
                }
            }
            else
            {
                ret = false;
            }
        }
        return ret;
    }

    /****
     * PUSH配信データリスト更新(OSタイプ不明時)
     * 
     * @param id
     * @param pushSeq
     * @param userId
     * @param token
     * @return
     */
    public static boolean updatePushDataList(ArrayList<DataApPushDataListExtend> listPd, Connection dbcon)
    {
        // Logging.info( "[updatePushDataList] listPd.size()=" + listPd.size() );
        boolean ret = true;
        Connection conSub = null;
        conSub = makeConnection();

        for( int i = 0 ; i < listPd.size() ; i++ )
        {
            DataApPushDataListExtend listPl = new DataApPushDataListExtend();
            listPl = listPd.get( i );
            DataApPushDataList dapdl = new DataApPushDataList();
            // Logging.info( "[updatePushDataList] listPl.getUserId()=" + listPl.getUserId() + " listPl.getToken()=" + listPl.getToken() );

            if ( dapdl.getData( conSub, listPl.getType(), listPl.getId(), listPl.getPushSeq(), listPl.getUserId(), listPl.getToken() ) )
            {

                dapdl.setStatus( 3 );// トークン不正
                dapdl.setSendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dapdl.setSendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( !dapdl.updateData( conSub, listPl.getType(), listPl.getId(), listPl.getPushSeq(), listPl.getUserId(), listPl.getToken() ) )
                {
                    ret = false;
                }
            }
            else
            {
                ret = false;
            }
        }
        releaseResources( conSub );
        return ret;
    }

    /****
     * PUSH配信データ更新（配信日時）
     * 
     * @param pushSeq
     * @return
     */
    public static boolean updatePushData(int pushSeq, int status, Connection dbcon)
    {
        boolean ret = false;
        DataApPushData dapd = new DataApPushData();

        if ( dapd.getData( dbcon, pushSeq ) )
        {
            dapd.setStatus( status );
            if ( status == 9 )
            {
                // 配信中のとき
                dapd.setPushDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dapd.setPushTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            }
            dapd.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dapd.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = dapd.updateData( dbcon, pushSeq );
        }

        return ret;
    }

    /****
     * PUSH配信データ更新(Status, Count)
     * 
     * @param pushSeq
     * @param count
     * @return
     */
    public static boolean updatePushDataStatus(int pushSeq, Connection dbcon)
    {
        boolean ret = false;
        int count = 0;
        DataApPushDataList dapdl = new DataApPushDataList();
        Connection conSub = null;
        conSub = makeConnection();

        // 処理済み件数取得
        if ( dapdl.getPushDataListExecCount( conSub, 1, pushSeq ) )
        {
            count = dapdl.getExecCount();
        }

        DataApPushData dapd = new DataApPushData();
        if ( dapd.getData( conSub, pushSeq ) )
        {
            int listCnt = 0;
            if ( dapdl.getPushDataListCount( conSub, 1, pushSeq, 0 ) )
            {
                listCnt = dapdl.getPushCount();
            }
            if ( listCnt == 0 )
            {
                dapd.setStatus( 3 );// 配信済
            }
            else
            {
                dapd.setStatus( 2 );// 残りを配信
            }
            dapd.setPushCount( count );
            dapd.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dapd.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = dapd.updateData( conSub, pushSeq );
            // Logging.info( "[updatePushDataStatus] count=" + count + " listCnt=" + listCnt + " ret=" + ret );
        }
        releaseResources( conSub );

        return ret;
    }

    /****
     * apUserCampaign更新
     * 
     * @param userId
     * @param campaign_id
     * @return
     */
    public static boolean updateUserCampaign(DataApPush dp, int campaign_id, int apli_kind, Connection dbcon)
    {
        boolean ret = true;

        for( int i = 0 ; i < dp.getListPl().size() ; i++ )
        {
            DataApPushDataListExtend listPl = new DataApPushDataListExtend();
            listPl = dp.getListPl().get( i );
            DataApUserCampaign dauc = new DataApUserCampaign();

            if ( dauc.getData( dbcon, listPl.getUserId(), campaign_id, apli_kind ) )
            {
                // update
                dauc.setPushDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dauc.setPushTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( !dauc.updateData( dbcon, listPl.getUserId(), campaign_id, apli_kind ) )
                {
                    ret = false;
                }
            }
            else
            {
                // insert
                dauc.setUserId( listPl.getUserId() );
                dauc.setCampaignId( campaign_id );
                dauc.setApliKind( apli_kind );
                dauc.setPushDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dauc.setPushTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                if ( listPl.getUserId().equals( "" ) )
                {
                    // 未ログイン者用のリストは既読にしておく
                    dauc.setDispDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dauc.setDispTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                if ( !dauc.insertData( dbcon ) )
                {
                    ret = false;
                }
            }
        }
        return ret;
    }

    /****
     * error_flag更新
     * 
     * @param DataApPush
     * @param inactiveToken
     * @param invalidToken
     * @param apli_kind
     * @return
     */
    public static boolean updateTokenErrorFlag(DataApPush dp, ArrayList<String> inactiveToken, ArrayList<String> invalidToken, int apli_kind, Connection dbcon)
    {
        boolean ret = true;
        DataApTokenUser datu = new DataApTokenUser();

        for( int i = 0 ; i < dp.getListPl().size() ; i++ )
        {
            DataApPushDataListExtend listPl = new DataApPushDataListExtend();
            listPl = dp.getListPl().get( i );
            DataApPushDataList dapdl = new DataApPushDataList();
            String userId = listPl.getUserId();
            String token = listPl.getToken();
            int status = 0;

            // トークンの状態を取得
            status = chkTokenStatus( listPl.getToken(), inactiveToken, invalidToken );

            if ( apli_kind == 1 )
            {
                // ハピホテアプリ用トークンのときap_user_tokenのerror_flag更新
                if ( datu.getData( dbcon, token, userId ) )
                {
                    if ( datu.getErrorFlag() != status )
                    {
                        datu.setErrorFlag( status );
                        datu.setUpdateDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        datu.setUpdateTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        if ( !datu.updateData( dbcon, token, userId ) )
                        {
                            ret = false;
                        }
                    }
                }
                else
                {
                    ret = false;
                }
            }
            else if ( apli_kind == 10 )
            {
                // 予約アプリのときap_rsv_token_uuidのerror_flag更新
                if ( !updateApRsvTokenUuid( token, status, dbcon ) )
                {
                    ret = false;
                }
            }
            else
            {
                // 新ハピホテアプリのときap_token_uuidのerror_flag更新
                if ( !updateApTokenUuid( token, status, dbcon ) )
                {
                    ret = false;
                }

            }
        }

        return ret;
    }

    /****
     * トークンの状態取得
     * 
     * @param token
     * @param inactiveToken
     * @param invalidToken
     * @return
     */
    public static int chkTokenStatus(String token, ArrayList<String> inactiveToken, ArrayList<String> invalidToken)
    {

        int status = 0;// 正常

        // トークンの状態を取得
        if ( inactiveToken.indexOf( token.toUpperCase() ) != -1 )
        {
            status = 2;// 送信エラー
        }
        if ( invalidToken.indexOf( token.toUpperCase() ) != -1 )
        {
            status = 3;// トークン不正
        }

        return status;
    }

    /**
     * トークンユーザー(ap_rsv_token_uuid)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param token iOS:device token,Android:registration id（トークンは書き換わるときあり）
     * @param status ステータス(0: 正常 2: 送信エラー 3: トークン不正 )
     * @param dbcon コネクション
     * @return
     */
    public static boolean updateApRsvTokenUuid(String token, int status, Connection dbcon)
    {
        int i = 1;
        int result;
        ResultSet resultset = null;
        int errorFlag = -1;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        try
        {

            // エラーフラグ取得
            query = "SELECT * FROM newRsvDB.ap_rsv_token_uuid WHERE token = ?  ";
            prestate = dbcon.prepareStatement( query );
            prestate.setString( i++, token );
            resultset = prestate.executeQuery();
            if ( resultset != null )
            {
                if ( resultset.next() != false )
                {
                    errorFlag = resultset.getInt( "error_flag" );
                    ret = true;
                }
            }

            i = 1;

            // エラーフラグに変更があるなら、更新
            if ( ret && errorFlag != status )
            {
                query = "UPDATE newRsvDB.ap_rsv_token_uuid SET ";
                query += "  update_date=?";
                query += ", update_time=?";
                query += ", error_flag=?";
                query += " WHERE token=?";

                prestate = dbcon.prepareStatement( query );
                // 更新対象の値をセットする
                prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                prestate.setInt( i++, status );
                prestate.setString( i++, token );
                result = prestate.executeUpdate();
                if ( result > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApRsvTokenUuid.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( resultset );
            releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * トークンユーザー(ap_token_uuid)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param token iOS:device token,Android:registration id（トークンは書き換わるときあり）
     * @param status ステータス(0: 正常 2: 送信エラー 3: トークン不正 )
     * @param dbcon コネクション
     * @return
     */
    public static boolean updateApTokenUuid(String token, int status, Connection dbcon)
    {
        int i = 1;
        int result;
        ResultSet resultset = null;
        int errorFlag = -1;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        try
        {

            // エラーフラグ取得
            query = "SELECT * FROM ap_token_uuid WHERE token = ?  ";
            prestate = dbcon.prepareStatement( query );
            prestate.setString( i++, token );
            resultset = prestate.executeQuery();
            if ( resultset != null )
            {
                if ( resultset.next() != false )
                {
                    errorFlag = resultset.getInt( "error_flag" );
                    ret = true;
                }
            }

            i = 1;

            // エラーフラグに変更があるなら、更新
            if ( ret && errorFlag != status )
            {
                query = "UPDATE ap_token_uuid SET ";
                query += "  update_date=?";
                query += ", update_time=?";
                query += ", error_flag=?";
                query += " WHERE token=?";

                prestate = dbcon.prepareStatement( query );
                // 更新対象の値をセットする
                prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
                prestate.setInt( i++, status );
                prestate.setString( i++, token );
                result = prestate.executeUpdate();
                if ( result > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTokenUuid.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            // コネクション以外を閉じる
            releaseResources( resultset );
            releaseResources( prestate );
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
