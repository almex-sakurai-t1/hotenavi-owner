/*
 * @(#)UserMap.java 1.00
 * 2007/07/31 Copyright (C) ALMEX Inc. 2007
 * ユーザーマップ取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.data.DataUserPointPay;
import jp.happyhotel.data.DataUserPointPayTemp;

/**
 * ユーザーに表示する地図を取得する。
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/30
 */
public class UserPointPayReflect implements Serializable
{
    private static final long serialVersionUID = -9077082078242062976L;
    static Connection         con              = null;                 // Database connection
    static Statement          stmt             = null;
    static ResultSet          rs               = null;
    static String             DB_URL;                                  // URL for database server
    static String             user;                                    // DB user
    static String             password;                                // DB password
    private static int        userCount;
    private static String     mailTo;
    private static String     mailFrom;
    private static String     mailSubject;
    private String            errMsg           = "";

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/user_point_mediate.conf" );
            prop = new Properties();
            // プロパティファイルを読み込む
            prop.load( propfile );

            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );
            mailTo = prop.getProperty( "mail.to" );
            mailFrom = prop.getProperty( "mail.from" );
            mailSubject = prop.getProperty( "mail.subject" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "UserPointPayReflect Static Block Error=" + e.toString() );
        }
    }

    /**
     * メイン
     * 
     * @param args
     */

    public static void main(String[] args)
    {
        boolean ret;
        ret = false;

        Logging.info( "[UserPointPayReflect.main( )] Start" );
        try
        {
            ret = UserPointPayReflect.findAdditionalData( Integer.parseInt( DateEdit.getDate( 2 ) ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointPayReflect.main( )]Exception:" + e.toString() );
            e.printStackTrace();
        }
        finally
        {
            UserPointPayReflect.closeConnection();
        }
        Logging.info( "[UserPointPayReflect.main( )] End" );
    }

    /***
     * 日付から追加するデータを探す、
     * 
     * @param Date 日付(YYYYMMDD)
     * @return
     */
    public static boolean findAdditionalData(int Date)
    {
        final int ADD_FINISHED = 1;
        int i;
        int count;
        int errCount;
        int result;
        boolean ret;
        boolean boolRegist;
        String query;
        String errMsg = "";
        DataUserPointPayTemp duppt;

        ret = false;
        boolRegist = false;
        count = 0;
        errCount = 0;

        query = "SELECT * FROM hh_user_point_pay_temp"
                + " WHERE add_flag=0"
                + " AND reflect_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            // コネクションを作る
            con = makeConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery( query );
            if ( rs != null )
            {
                // レコード件数取得
                if ( rs.last() != false )
                {
                    userCount = rs.getRow();
                }

                count = 0;
                errCount = 0;
                rs.beforeFirst();
                while( rs.next() != false )
                {
                    // DataUserPointPayTempを初期化
                    duppt = new DataUserPointPayTemp();
                    duppt.setData( rs );

                    Logging.info( "userId:" + duppt.getUserId() );

                    // ポイントを反映
                    boolRegist = UserPointPayReflect.insertUserPointPay( con, duppt );
                    if ( boolRegist != false )
                    {
                        // 追加済みフラグをセットし、更新する
                        duppt.setAddFlag( ADD_FINISHED );
                        ret = duppt.updateData( con, duppt.getUserId(), duppt.getSeq() );

                        if ( ret != false )
                        {
                            count++;
                        }
                        else
                        {
                            Logging.error( "[hh_user_poin_pay_temp 更新エラー]user_id:" + duppt.getUserId() + ", seq:" + duppt.getSeq() + "\r\n" );
                            errMsg += "[hh_user_poin_pay_temp 更新エラー]user_id:" + duppt.getUserId() + ", seq:" + duppt.getSeq() + "\r\n";
                            errCount++;
                        }
                    }
                    else
                    {
                        Logging.error( "[hh_user_poin_pay 追加エラー]user_id:" + duppt.getUserId() + ", seq:" + duppt.getSeq() + "\r\n" );
                        errMsg += "[hh_user_poin_pay 追加エラー]user_id:" + duppt.getUserId() + ", seq:" + duppt.getSeq() + "\r\n";
                        errCount++;
                    }

                    if ( duppt != null )
                    {
                        duppt = null;
                    }
                }

                errMsg = "total:" + userCount + ", success:" + count + ", failed:" + errCount + "\r\n" + errMsg;
                if ( errCount > 0 )
                {
                    HashMap<String, String> map = getSystemConf( con, 10 ); // メール情報をhh_rsv_system_confから取得する。

                    SendMail.send( map.get( "mail.from" ), map.get( "mail.to" ), map.get( "mail.subject" ), errMsg );
                }
                Logging.info( "[UserPointPayReflect] total:" + userCount + ", success:" + count + ", failed:" + errCount );

                ret = true;
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            ret = false;
        }
        catch ( Exception e )
        {
            HashMap<String, String> map = getSystemConf( con, 10 ); // メール情報をhh_rsv_system_confから取得する。

            SendMail.send( map.get( "mail.from" ), map.get( "mail.to" ), map.get( "mail.subject" ), e.toString() );
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    /**
     * hh_rsv_system_confテーブルの情報をMapに変換して取得
     *
     * @param con DBコネクション
     * @param ctrl_id1 hh_rsv_system_conf.ctrl_id1
     * @return ctrl_id1に紐づくHashMap(val1, val2)の値
     */
    public static HashMap<String, String> getSystemConf(Connection con, int ctrl_id1)
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        HashMap<String, String> rtMap = new HashMap<String, String>();

        query = "SELECT * FROM hh_rsv_system_conf WHERE ctrl_id1 = ?  ";
        query += " ORDER BY ctrl_id2";

        try
        {
            prestate = con.prepareStatement( query );
            prestate.setInt( 1, ctrl_id1 );
            result = prestate.executeQuery();

            if ( result != null )
            {
                result.beforeFirst();
                while( result.next() != false )
                {
                    rtMap.put( result.getString( "val1" ), result.getString( "val2" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMailInfo()] Exception:" + e.toString() );
        }
        finally
        {
            try
            {
                // それぞれを閉じる
                if ( result != null )
                    result.close();
                if ( prestate != null )
                    prestate.close();
            }
            catch ( Exception e )
            {
                Logging.info( "[getMailInfo()] Exception:" + e.toString() );
            }
        }
        return(rtMap);
    }

    /**
     * 有料ポイントデータセットクラス
     * 
     * @param DataUserPointPayTemp 有料ポイント一時データ
     * @return
     */
    public static boolean insertUserPointPay(Connection con, DataUserPointPayTemp dupt)
    {
        DataUserPointPay dup;
        boolean ret = false;

        if ( dupt == null )
        {
            ret = false;
            return ret;
        }

        dup = new DataUserPointPay();
        // DataUserPointPayにDataUserPointPayTempをセットする
        dup.setUserId( dupt.getUserId() );
        // インサートするためseqは0でセット
        dup.setSeq( 0 );
        dup.setGetDate( dupt.getGetDate() );
        dup.setGetTime( dupt.getGetTime() );
        dup.setCode( dupt.getCode() );
        dup.setPoint( dupt.getPoint() );
        dup.setPointKind( dupt.getPointKind() );
        dup.setExtCode( dupt.getExtCode() );
        dup.setExtString( dupt.getExtString() );
        dup.setPersonCode( dupt.getPersonCode() );
        dup.setAppendReason( dupt.getAppendReason() );
        dup.setMemo( dupt.getMemo() );
        dup.setIdm( dupt.getIdm() );
        dup.setUserSeq( dupt.getUserSeq() );
        dup.setVisitSeq( dupt.getVisitSeq() );
        dup.setSlipNo( dupt.getSlipNo() );
        dup.setRoomNo( dupt.getRoomNo() );
        dup.setAmount( dupt.getAmount() );
        dup.setThenPoint( dupt.getThenPoint() );
        dup.setHotenaviId( dupt.getHotenaviId() );
        dup.setEmployeeCode( dupt.getEmployeeCode() );
        dup.setUsedPoint( dupt.getUsedPoint() );

        ret = dup.insertData( con );
        return ret;
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
            Class.forName( "com.mysql.jdbc.Driver" );
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
                rs.close();
            if ( stmt != null )
                stmt.close();
            if ( con != null )
                con.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

}
