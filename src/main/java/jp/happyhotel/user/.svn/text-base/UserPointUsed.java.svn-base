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
import java.util.HashMap;
import java.util.Properties;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.SendMail;
import jp.happyhotel.data.DataUserPointPay;

/**
 * ユーザー別にマイル仕訳計算を行う(hh_user_point_pay_used)に書き込む
 * 毎月1日の0:00より実行する
 * 
 * @author T.Sakurai
 */
public class UserPointUsed implements Serializable
{
    static Connection connection     = null;   // Database connection
    static String     DB_URL;                  // URL for database server
    static String     user;                    // DB user
    static String     password;                // DB password
    static int        beforeLastDate;          // 前月末日付
    static int        closingStartDate;        // 前月締開始日
    static int        closingDate;             // 前月締日
    static int        before2YDate;            // 2年前日付
    static int        before90Date;            // 90日前日付
    static String     errMsg         = "";
    static String     mailFrom       = "";
    static String     mailTo         = "";
    static String     mailSubject    = "";
    static final int  ADD_CODE       = 1000005;
    static final int  USE_CODE       = 1000006;
    static final int  USE_TITLE_CODE = 120;
    static final int  CHANGE_CODE    = 1000008;
    static final int  DEL_CODE       = 1000100;
    static final int  REVOCATE_CODE  = 1000102;

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( "/etc/happyhotel/dbconnect.conf" );
            prop = new Properties();
            // プロパティファイルを読み込む
            prop.load( propfile );

            DB_URL = prop.getProperty( "jdbc.url" );
            user = prop.getProperty( "jdbc.user" );
            password = prop.getProperty( "jdbc.password" );

            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "UserPointUsed Static Block Error=" + e.toString() );
        }
    }

    /**
     * メイン
     * 
     * @param args
     */

    public static void main(String[] args)
    {

        String strDate = "";
        int nowDate = 0;
        try
        {
            if ( args != null && args.length > 0 )
            {
                strDate = args[0];
                nowDate = Integer.parseInt( strDate );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointUsed] コマンドライン引数の処理失敗:" + e.toString() );
        }

        if ( nowDate == 0 )
        {
            nowDate = (Integer.parseInt( DateEdit.getDate( 2 ) ));
        }
        System.out.println( "nDate:" + nowDate );

        /*
         * 前月末日付の取得
         */
        beforeLastDate = DateEdit.getLastDayOfMonth( DateEdit.addMonth( nowDate, -1 ) );
        System.out.println( "beforeLastDate :" + beforeLastDate );

        /*
         * 前月締め日の取得
         */
        closingDate = (beforeLastDate / 100) * 100 + 10;
        System.out.println( "closingDate :" + closingDate );

        /*
         * 前月締めの開始日の取得
         */
        closingStartDate = DateEdit.addMonth( closingDate, -1 ) + 1;
        System.out.println( "closingStartDate :" + closingStartDate );

        /*
         * 2年前日付
         */
        before2YDate = (DateEdit.addYear( nowDate, -2 ) / 100) * 100 + 1;
        System.out.println( "before2YDate :" + before2YDate );

        /*
         * 90日前の日付
         */
        before90Date = DateEdit.addDay( nowDate, -90 );
        System.out.println( "before90Date  :" + before90Date );

        connection = makeConnection();

        HashMap<String, String> map = getSystemConf( connection, 9 ); // メール情報をhh_rsv_system_confから取得する。
        mailFrom = map.get( "mail.from" );
        mailTo = map.get( "mail.to" );
        mailSubject = closingDate / 10000 + "年" + closingDate / 100 + "月締め処理実行";

        System.out.println( "[UserPointUsed.main( )] Start" );

        try
        {
            // 1.退会者のマイル償却･･･有料会員の場合は退会して3ヶ月経過した会員についてマイナスデータを書き込み
            System.out.println( "[UserPointUsed.DeleteUser()] Start" );
            UserPointUsed.DeleteUser();

            // 2.ハピホテ予約のマイル使用取消分について、同じ予約Noでused_mileが0のものがあれば、双方ともにused_mile を償却する。
            // 2.ハピホテ予約のマイル使用取消分について、同じ予約Noでexpired_mileが0のものがあれば、双方ともにexpired_mile を償却する。
            System.out.println( "[UserPointUsed.RsvPointCancel()] Start" );
            UserPointUsed.RsvPointCancel( "used" );
            UserPointUsed.RsvPointCancel( "expired" );

            // 3.マイル使用取り消し分の仕訳用マイルについて、仕訳データを作成する。対象は締め時刻を考慮した前月の10日までのものとし、取消についてはすべてAマイルとする。
            System.out.println( "[UserPointUsed.DeletedPoint()] Start" );
            UserPointUsed.DeletedPoint();

            // 4.マイナスマイルについて先頭から償却する。対象は締め時刻を考慮した前月の10日までのものとする。予約については、チェックインしているもののみ対象とする。
            System.out.println( "[UserPointUsed.PointExpired()] Start" );
            UserPointUsed.PointExpired();

            // 5.2年経過マイル分について、前月末日付にマイナスマイルを書き込む。
            System.out.println( "[UserPointUsed.MinusPointBy2Year()] Start" );
            UserPointUsed.MinusPointBy2Year();

            // 6.マイナスマイルについて、加算マイルを優先して償却し、結果をhh_user_point_pay_used に書き込む。対象は締め時刻を考慮した前月の10日までのものとする。予約については、チェックインしているもののみ対象とする。
            System.out.println( "[UserPointUsed.CalABbyMinusMile()] Start" );
            UserPointUsed.CalABbyMinusMile();

            // 7.マイナスマイルユーザーについて、research_staff の mail2に"bl"がはいったユーザのメアドに送信する。
            System.out.println( "[UserPointUsed.MinusCheck()] Start" );
            UserPointUsed.MinusCheck();

            // 8.ホテル支払分と仕訳ユーザの残高について、差異があった場合、 research_staff のmail2に"bl"がはいったユーザのメアドに送信する。
            System.out.println( "[UserPointUsed.RecvCheck()] Start" );
            UserPointUsed.RecvCheck();

            // 9.エラーがなかった場合にresearch_staff のmail2に"bl"がはいったユーザのメアドに仕訳結果を送信する。
            System.out.println( "[UserPointUsed.BalanceMail()] Start" );
            UserPointUsed.BalanceMail();

        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointUsed.main( )]Exception:" + e.toString() );
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        finally
        {
            UserPointUsed.closeConnection();
        }

        if ( errMsg.equals( "" ) )
        {
            errMsg = "仕訳計算処理が終了しました";
        }
        SendMail.send( mailFrom, mailTo, mailSubject, errMsg );
        System.out.println( "[UserPointUsed.main( )] End\r\n" + errMsg );
    }

    /*
     * 1.退会者のマイル償却･･･有料会員の場合は退会して3ヶ月経過した会員についてマイナスデータを書き込み
     */
    public static void DeleteUser()
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        DataUserPointPay dupp;

        query = "SELECT basic.user_id";
        query += ",basic.del_date_pay";
        query += ",basic.del_date_pc";
        query += ",basic.del_date_mobile";
        query += ",SUM(p.point) AS sumPoint";
        query += " FROM hh_user_point_pay p";
        query += " INNER JOIN hh_user_basic basic ON basic.user_id = p.user_id";
        query += " WHERE basic.del_flag = 1";
        query += " AND p.get_date <= ?";
        query += " GROUP BY p.user_id";
        query += " HAVING sumPoint !=0";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, beforeLastDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    int del_date = 0;// 有料会員退会日
                    if ( result.getInt( "basic.del_date_pay" ) > del_date )
                        del_date = result.getInt( "basic.del_date_pay" );
                    if ( result.getInt( "basic.del_date_pc" ) > del_date )
                        del_date = result.getInt( "basic.del_date_pc" );
                    if ( result.getInt( "basic.del_date_mobile" ) > del_date )
                        del_date = result.getInt( "basic.del_date_mobile" );

                    int target_date = del_date == 0 ? beforeLastDate : DateEdit.addDay( DateEdit.addMonth( del_date, 4 ), -1 ); // 削除日付の3か月後の月末
                    if ( target_date > beforeLastDate )
                        target_date = beforeLastDate;
                    if ( result.getInt( "basic.del_date_mobile" ) >= result.getInt( "basic.del_date_pay" ) || result.getInt( "basic.del_date_pc" ) >= result.getInt( "basic.del_date_pay" )
                            || del_date < before90Date )
                    {
                        // DataUserPointPayを初期化
                        dupp = new DataUserPointPay();
                        dupp.setUserId( result.getString( "user_id" ) );
                        dupp.setCode( DEL_CODE );
                        dupp.setGetDate( target_date );
                        dupp.setGetTime( 999999 );
                        dupp.setPoint( 0 - result.getInt( "sumPoint" ) );
                        dupp.setPointKind( 100 );
                        dupp.insertData( connection );
                    }
                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.DeletedUser()] Exception:" + e.toString() );
            }
        }
    }

    /*
     * 2.ハピホテ予約のマイル使用取消分について、同じ予約Noでused_mileが0のものがあれば、双方ともにused_mile を償却する。
     * 2.ハピホテ予約のマイル使用取消分について、同じ予約Noでexpired_mileが0のものがあれば、双方ともにexpired_mile を償却する。
     */
    public static void RsvPointCancel(String kind)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;

        String query;
        query = "SELECT user_id,seq,point,ext_string FROM hh_user_point_pay WHERE code=? AND ext_string <> '' AND point > 0 AND " + kind + "_point=0";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_CODE );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    PreparedStatement pre_sub = null;
                    ResultSet ret_sub = null;
                    int seq = 0;
                    int point = 0;
                    query = "SELECT seq,point FROM hh_user_point_pay WHERE user_id=? AND code=? AND ext_string=? AND point=? AND " + kind + "_point=0";
                    pre_sub = connection.prepareStatement( query );
                    pre_sub.setString( 1, result.getString( "user_id" ) );
                    pre_sub.setInt( 2, USE_CODE );
                    pre_sub.setString( 3, result.getString( "ext_string" ) );
                    pre_sub.setInt( 4, 0 - result.getInt( "point" ) );
                    ret_sub = pre_sub.executeQuery();
                    if ( ret_sub != null )
                    {
                        if ( ret_sub.next() != false )
                        {
                            seq = ret_sub.getInt( "seq" );
                            point = ret_sub.getInt( "point" );
                        }
                    }
                    if ( point < (0 - result.getInt( "point" )) )
                    {
                        point = 0 - result.getInt( "point" );
                    }

                    ret_sub.close();
                    if ( seq != 0 )
                    {
                        query = "UPDATE hh_user_point_pay SET";
                        query += " " + kind + "_point = " + result.getInt( "point" );
                        query += " WHERE user_id = '" + result.getString( "user_id" ) + "'";
                        query += "   AND seq     =  " + result.getInt( "seq" );
                        pre_sub = connection.prepareStatement( query );
                        pre_sub.executeUpdate();

                        query = "UPDATE hh_user_point_pay SET";
                        query += " " + kind + "_point = " + point;
                        query += " WHERE user_id = '" + result.getString( "user_id" ) + "'";
                        query += "   AND seq     =  " + seq;
                        pre_sub = connection.prepareStatement( query );
                        pre_sub.executeUpdate();
                    }
                    pre_sub.close();
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.RsvPointCancel(" + kind + ")] Exception:" + e.toString() );
            }
        }
    }

    /*
     * 3.マイル使用取り消し分の仕訳用マイルについて、仕訳データを作成する。対象は締め時刻を考慮した前月の10日までのものとし、取消についてはすべてAマイルとする。
     */
    public static void DeletedPoint()
    {
        PreparedStatement prestate = null;
        ResultSet result = null;

        String query;
        query = "SELECT (pay.point - pay.expired_point) AS sumPoint,pay.user_id,pay.seq,pay.get_date,recv.usage_date,recv.usage_time FROM hh_user_point_pay pay";
        query += " INNER JOIN hh_hotel_ci ci ON  pay.ext_code = ci.id AND pay.user_id = ci.user_id AND pay.user_seq= ci.user_seq AND pay.visit_seq= ci.visit_seq AND ci.ci_status = 1";
        query += " INNER JOIN hh_bko_account_recv recv ON ci.id = recv.id AND ci.seq = recv.ci_seq AND recv.closing_kind = 2";
        query += " WHERE pay.point > 0";
        query += " AND pay.code=?";
        query += " AND pay.get_date <= ?";
        query += " AND pay.point <> pay.expired_point";
        query += " AND NOT EXISTS (SELECT 1 FROM hh_hotel_ci ci_sub WHERE ci.id = ci_sub.id AND ci.seq = ci_sub.seq AND ci.sub_seq < ci_sub.sub_seq)";
        query += " GROUP BY pay.user_id , pay.seq";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_CODE );
            prestate.setInt( 2, closingDate + 1 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    query = "INSERT INTO hh_user_point_pay_used SET";
                    query += " user_id = '" + result.getString( "pay.user_id" ) + "'";
                    query += ",seq     =  " + result.getInt( "pay.seq" );
                    query += ",get_date=  " + (result.getInt( "recv.usage_date" ) < closingStartDate ? closingDate : result.getInt( "recv.usage_date" ));
                    query += ",get_time=  " + (result.getInt( "recv.usage_date" ) < closingStartDate ? 999999 : result.getInt( "recv.usage_time" ));
                    query += ",point=" + result.getInt( "sumPoint" );
                    query += ",point_a=" + result.getInt( "sumPoint" );
                    prestate = connection.prepareStatement( query );
                    prestate.executeUpdate();
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.DeletedPoint()] Exception:" + e.toString() );
            }
        }
    }

    /*
     * 4.マイナスマイルについて先頭から償却する。対象は締め時刻を考慮した前月の10日までのものとする。
     */
    public static void PointExpired()
    {
        PreparedStatement prestate = null;
        ResultSet result = null;

        String query;

        // 通常マイル使用分
        query = "SELECT (pay.point - pay.expired_point) AS sumPoint, pay.user_id, pay.seq, pay.get_date,pay.point FROM hh_user_point_pay pay";
        query += " INNER JOIN hh_hotel_ci ci ON  pay.ext_code = ci.id AND pay.ext_code = ci.id AND pay.user_id = ci.user_id AND pay.user_seq= ci.user_seq AND pay.visit_seq= ci.visit_seq AND ci.ci_status = 1";
        query += " INNER JOIN hh_bko_account_recv recv ON ci.id = recv.id AND ci.seq = recv.ci_seq AND recv.closing_kind = 2";
        query += " WHERE pay.point < 0";
        query += " AND pay.code = ?";
        query += " AND pay.get_date <= ?";
        query += " AND pay.point <> pay.expired_point";
        query += " AND NOT EXISTS (SELECT 1 FROM hh_hotel_ci ci_sub WHERE ci.id = ci_sub.id AND ci.seq = ci_sub.seq AND ci.sub_seq < ci_sub.sub_seq)";
        query += " GROUP BY pay.user_id , pay.seq";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_CODE );
            prestate.setInt( 2, closingDate + 1 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    int sumPoint = 0 - result.getInt( "sumPoint" );
                    int amortize_point = AmortizePoint( connection, "expired", result.getString( "user_id" ), sumPoint, "" );
                    UpdatePoint( connection, "expired", result.getString( "user_id" ), result.getInt( "seq" ), result.getInt( "point" ) + amortize_point );
                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.PointExpired(1)] Exception:" + e.toString() );
            }
        }

        // 予約マイル使用でキャンセルされた分
        query = "SELECT (pay.point - pay.expired_point) AS sumPoint, pay.user_id, pay.seq, rsv.reserve_date,pay.point FROM hh_user_point_pay pay";
        query += " INNER JOIN newRsvDB.hh_rsv_reserve rsv ON  pay.ext_code = rsv.id AND pay.ext_string = rsv.reserve_no AND rsv.status=3";
        query += " WHERE pay.point < 0";
        query += " AND pay.code = ?";
        query += " AND rsv.reserve_date <= ?";
        query += " AND pay.point <> pay.expired_point";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_CODE );
            prestate.setInt( 2, closingDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    int sumPoint = 0 - result.getInt( "sumPoint" );
                    int amortize_point = AmortizePoint( connection, "expired", result.getString( "user_id" ), sumPoint, "" );
                    UpdatePoint( connection, "expired", result.getString( "user_id" ), result.getInt( "seq" ), result.getInt( "point" ) + amortize_point );
                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.PointExpired(2)] Exception:" + e.toString() );
            }
        }

        query = "SELECT (point - expired_point) AS sumPoint,user_id,seq,get_date,point FROM hh_user_point_pay";
        query += " WHERE point < 0";
        query += " AND code<>?";
        query += " AND get_date <= ?";
        query += " AND point <> expired_point";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_CODE );
            prestate.setInt( 2, closingDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    int sumPoint = 0 - result.getInt( "sumPoint" );
                    int amortize_point = AmortizePoint( connection, "expired", result.getString( "user_id" ), sumPoint, "" );
                    UpdatePoint( connection, "expired", result.getString( "user_id" ), result.getInt( "seq" ), result.getInt( "point" ) + amortize_point );
                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.PointExpired(3)] Exception:" + e.toString() );
            }
        }
    }

    public static int AmortizePoint(Connection connection, String kind, String user_id, int sumPoint, String plusQuery)
    {
        PreparedStatement prestate = null;
        ResultSet result = null;

        int amortize_point = sumPoint;
        try
        {
            String query = "SELECT seq,point," + kind + "_point FROM hh_user_point_pay";
            query += " WHERE user_id =?";
            if ( amortize_point < 0 )
            {
                query += " AND   point < " + kind + "_point";
                query += " AND   point < 0";
            }
            else
            {
                query += " AND   point > " + kind + "_point";
                query += " AND   point > 0";
            }
            query += plusQuery;
            query += " ORDER BY get_date,get_time";
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, user_id );
            result = prestate.executeQuery();
            while( result.next() )
            {

                if ( amortize_point == 0 )
                    break;
                if ( (amortize_point < 0 && amortize_point < (result.getInt( "point" ) - result.getInt( kind + "_point" ))) || (amortize_point > 0 && amortize_point > (result.getInt( "point" ) - result.getInt( kind + "_point" ))) )
                {
                    amortize_point = amortize_point - (result.getInt( "point" ) - result.getInt( kind + "_point" ));
                    UpdatePoint( connection, kind, user_id, result.getInt( "seq" ), result.getInt( "point" ) );

                }
                else
                {
                    UpdatePoint( connection, kind, user_id, result.getInt( "seq" ), result.getInt( kind + "_point" ) + amortize_point );
                    amortize_point = 0;
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString();
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
                Logging.error( "[UserPointUsed.AmortizePoint()] Exception:" + e.toString() );
            }
        }
        return amortize_point;
    }

    public static void UpdatePoint(Connection connection, String kind, String user_id, int seq, int point)
    {
        PreparedStatement prestate = null;

        String query;
        try
        {
            query = "UPDATE hh_user_point_pay SET " + kind + "_point = ?";
            query += " WHERE user_id = ? ";
            query += " AND seq = ? ";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, point );
            prestate.setString( 2, user_id );
            prestate.setInt( 3, seq );

            prestate.executeUpdate();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString();
        }
        finally
        {
            try
            {
                if ( prestate != null )
                    prestate.close();
            }
            catch ( Exception e )
            {
                Logging.error( "[UserPointUsed.UpdatePoint()] Exception:" + e.toString() );
            }
        }
    }

    /*
     * 5.2年経過マイル分について、前月末日付にマイナスマイルを書き込む。
     */
    public static void MinusPointBy2Year()
    {
        PreparedStatement prestate = null;
        ResultSet result = null;

        String query;
        DataUserPointPay dupp;

        query = "SELECT SUM(point - expired_point) AS sumPoint,user_id FROM hh_user_point_pay";
        query += " WHERE ( point > expired_point AND get_date < ? ) OR (point < expired_point)";
        query += " GROUP BY user_id";
        query += " HAVING sumPoint >0";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, before2YDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    dupp = new DataUserPointPay();
                    dupp.setUserId( result.getString( "user_id" ) );
                    dupp.setCode( REVOCATE_CODE );
                    dupp.setGetDate( beforeLastDate );
                    dupp.setGetTime( 999999 );
                    dupp.setPoint( 0 - result.getInt( "sumPoint" ) );
                    dupp.setExpiredPoint( 0 - result.getInt( "sumPoint" ) );
                    dupp.setPointKind( 102 );
                    if ( dupp.insertData( connection ) )
                    {
                        Update2YPoint( connection, result.getString( "user_id" ) );
                    }
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
            ;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.MinusPointBy2Year()] Exception:" + e.toString() );
            }
        }
    }

    public static void Update2YPoint(Connection connection, String user_id)
    {
        PreparedStatement prestate = null;

        String query;
        try
        {
            query = "UPDATE hh_user_point_pay SET expired_point = point";
            query += " WHERE point > expired_point";
            query += " AND user_id = ?";
            query += " AND get_date < ?";

            prestate = connection.prepareStatement( query );
            prestate.setString( 1, user_id );
            prestate.setInt( 2, before2YDate );
            prestate.executeUpdate();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString();
        }
        finally
        {
            try
            {
                if ( prestate != null )
                    prestate.close();
            }
            catch ( Exception e )
            {
                Logging.error( "[UserPointUsed.Update2YPoint()] Exception:" + e.toString() );
            }
        }
    }

    /*
     * 6.マイナスマイルについて、加算マイルを優先して償却し、結果をhh_user_point_pay_used に書き込む。対象は締め時刻を考慮した前月の10日までのものとする。予約については、チェックインしているもののみ対象とする。
     */
    public static void CalABbyMinusMile()
    {
        PreparedStatement prestate = null;
        ResultSet result = null;

        String query;

        // 通常マイル使用分
        query = "SELECT (pay.point - pay.used_point) AS sumPoint,pay.user_id,pay.seq,recv.usage_date,recv.usage_time,pay.point FROM hh_user_point_pay pay";
        query += " INNER JOIN hh_hotel_ci ci ON  pay.ext_code = ci.id AND pay.ext_code = ci.id AND pay.user_id = ci.user_id AND pay.user_seq= ci.user_seq AND pay.visit_seq= ci.visit_seq AND ci.ci_status = 1";
        query += " INNER JOIN hh_bko_account_recv recv ON ci.id = recv.id AND ci.seq = recv.ci_seq AND recv.closing_kind = 2";
        query += " WHERE pay.point < 0";
        query += " AND pay.code = ?";
        query += " AND pay.get_date <= ?";
        query += " AND pay.point <> pay.used_point";
        query += " AND NOT EXISTS (SELECT 1 FROM hh_hotel_ci ci_sub WHERE ci.id = ci_sub.id AND ci.seq = ci_sub.seq AND ci.sub_seq < ci_sub.sub_seq)";
        query += " GROUP BY pay.user_id , pay.seq";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_CODE );
            prestate.setInt( 2, closingDate + 2 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    int sumPoint = 0 - result.getInt( "sumPoint" );
                    int amortize_point = AmortizePoint( connection, "used", result.getString( "user_id" ), sumPoint, " AND code=" + ADD_CODE );
                    int point_b = sumPoint - amortize_point;
                    if ( amortize_point != 0 )
                    {
                        amortize_point = AmortizePoint( connection, "used", result.getString( "user_id" ), amortize_point, " AND code<>" + ADD_CODE );
                    }
                    int point_a = sumPoint - point_b - amortize_point;
                    UpdatePoint( connection, "used", result.getString( "user_id" ), result.getInt( "seq" ), result.getInt( "point" ) + amortize_point );

                    query = "INSERT INTO hh_user_point_pay_used SET";
                    query += " user_id = '" + result.getString( "user_id" ) + "'";
                    query += ",seq     =  " + result.getInt( "seq" );
                    query += ",get_date=  " + (result.getInt( "usage_date" ) < closingStartDate ? closingDate : result.getInt( "usage_date" ));
                    query += ",get_time=  " + (result.getInt( "usage_date" ) < closingStartDate ? 999999 : result.getInt( "usage_time" ));
                    query += ",point=" + (0 - sumPoint);
                    query += ",point_a=" + (0 - point_a);
                    query += ",point_b=" + (0 - point_b);
                    prestate = connection.prepareStatement( query );
                    prestate.executeUpdate();

                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.CalABbyMinusMile()] Exception:" + e.toString() );
            }
        }

        // 予約マイル使用でキャンセルされた分
        query = "SELECT pay.ext_code, pay.ext_string, (pay.point - pay.used_point) AS sumPoint, pay.user_id, pay.seq, rsv.reserve_date,rsv.est_time_arrival,pay.point,rsv.status,rsv.payment,rsv.noshow_flag FROM hh_user_point_pay pay";
        query += " INNER JOIN newRsvDB.hh_rsv_reserve rsv ON  pay.ext_code = rsv.id AND pay.ext_string = rsv.reserve_no AND rsv.status=3 ";
        query += " WHERE pay.point < 0";
        query += " AND pay.code = ?";
        query += " AND rsv.reserve_date <= ?";
        query += " AND pay.point <> pay.used_point";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_CODE );
            prestate.setInt( 2, closingDate + 2 );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    int sumPoint = 0 - result.getInt( "sumPoint" );
                    int amortize_point = AmortizePoint( connection, "used", result.getString( "user_id" ), sumPoint, " AND code=" + ADD_CODE );
                    int point_b = sumPoint - amortize_point;
                    if ( amortize_point != 0 )
                    {
                        amortize_point = AmortizePoint( connection, "used", result.getString( "user_id" ), amortize_point, " AND code<>" + ADD_CODE );
                    }
                    int point_a = sumPoint - point_b - amortize_point;
                    UpdatePoint( connection, "used", result.getString( "user_id" ), result.getInt( "seq" ), result.getInt( "point" ) + amortize_point );

                    query = "INSERT INTO hh_user_point_pay_used SET";
                    query += " user_id = '" + result.getString( "user_id" ) + "'";
                    query += ",seq     =  " + result.getInt( "seq" );
                    if ( result.getInt( "payment" ) == 2 && result.getInt( "noshow_flag" ) == 1 ) // 現地決済分キャンセルnoshowについては締め単位の集計にはいれない
                    {
                        query += ",get_date=0";
                        query += ",get_time=0";
                    }
                    else
                    {
                        query += ",get_date=  " + (result.getInt( "reserve_date" ) < closingStartDate ? closingDate : result.getInt( "reserve_date" ));
                        query += ",get_time=  " + (result.getInt( "reserve_date" ) < closingStartDate ? 999999 : result.getInt( "est_time_arrival" ));
                    }
                    query += ",point=" + (0 - sumPoint);
                    query += ",point_a=" + (0 - point_a);
                    query += ",point_b=" + (0 - point_b);
                    prestate = connection.prepareStatement( query );
                    prestate.executeUpdate();

                    if ( result.getInt( "status" ) == 3 && result.getInt( "noshow_flag" ) == 0 ) // 予約取消分については、＋の仕訳データを同時に書き込む
                    {
                        DataUserPointPay dupp = new DataUserPointPay();
                        if ( dupp.getData( connection, result.getString( "user_id" ), result.getInt( "ext_code" ), result.getString( "ext_string" ), 1 ) != false )
                        {
                            int seq = dupp.getSeq();
                            query = "INSERT INTO hh_user_point_pay_used SET";
                            query += " user_id = '" + result.getString( "user_id" ) + "'";
                            query += ",seq     =  " + seq;
                            query += ",get_date=  " + (result.getInt( "reserve_date" ) < closingStartDate ? closingDate : result.getInt( "reserve_date" ));
                            query += ",get_time=  " + (result.getInt( "reserve_date" ) < closingStartDate ? 999999 : result.getInt( "est_time_arrival" ));
                            query += ",point=" + sumPoint;
                            query += ",point_a=" + sumPoint;
                            query += ",point_b=0";
                            prestate = connection.prepareStatement( query );
                            prestate.executeUpdate();
                        }
                        else
                        { // +のデータがない場合には締め単位の集計にはいれない
                            query = "UPDATE hh_user_point_pay_used SET";
                            query += " get_date=0  ";
                            query += ",get_time=0  ";
                            query += " WHERE user_id = '" + result.getString( "user_id" ) + "'";
                            query += "   AND seq     =  " + result.getInt( "seq" );
                            prestate = connection.prepareStatement( query );
                            prestate.executeUpdate();

                        }
                    }
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.CalABbyMinusMile()] Exception:" + e.toString() );
            }
        }

        query = "SELECT (point - used_point) AS sumPoint,user_id,seq,get_date,get_time,point FROM hh_user_point_pay";
        query += " WHERE point < 0";
        query += " AND code <> ?";
        query += " AND get_date <= ?";
        query += " AND point <> used_point";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_CODE );
            prestate.setInt( 2, beforeLastDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    int sumPoint = 0 - result.getInt( "sumPoint" );
                    int amortize_point = AmortizePoint( connection, "used", result.getString( "user_id" ), sumPoint, " AND code=" + ADD_CODE );
                    int point_b = sumPoint - amortize_point;
                    if ( amortize_point != 0 )
                    {
                        amortize_point = AmortizePoint( connection, "used", result.getString( "user_id" ), amortize_point, " AND code<>" + ADD_CODE );
                    }
                    int point_a = sumPoint - point_b - amortize_point;
                    UpdatePoint( connection, "used", result.getString( "user_id" ), result.getInt( "seq" ), result.getInt( "point" ) + amortize_point );

                    query = "INSERT INTO hh_user_point_pay_used SET";
                    query += " user_id = '" + result.getString( "user_id" ) + "'";
                    query += ",seq     =  " + result.getInt( "seq" );
                    query += ",get_date=  " + result.getInt( "get_date" );
                    query += ",get_time=  " + result.getInt( "get_time" );
                    query += ",point=" + (0 - sumPoint);
                    query += ",point_a=" + (0 - point_a);
                    query += ",point_b=" + (0 - point_b);
                    prestate = connection.prepareStatement( query );
                    prestate.executeUpdate();
                    if ( prestate != null )
                        prestate.close();
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.CalABbyMinusMile()] Exception:" + e.toString() );
            }
        }
    }

    /*
     * 7.マイナスマイルユーザーについて、research_staff の mail2に"bl"がはいったユーザのメアドに送信する。
     */
    public static void MinusCheck()
    {
        PreparedStatement prestate = null;
        ResultSet result = null;

        String query;
        String minusUserMsg = "";

        try
        {
            query = "SELECT SUM(pay.point) AS sumPoint,pay.user_id FROM hh_user_point_pay pay";
            query += " INNER JOIN hh_user_basic basic ON pay.user_id = basic.user_id AND basic.login_flag = 0";
            query += " GROUP BY pay.user_id";
            query += " HAVING sumPoint < 0";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    minusUserMsg += "user_id=" + result.getString( "user_id" ) + ",残高=" + result.getInt( "sumPoint" ) + "\r\n";
                }
            }

            if ( !minusUserMsg.equals( "" ) )
            {
                System.out.println( "minusUserMsg :" + minusUserMsg );

                query = "SELECT mail1 FROM research_staff WHERE mail2 LIKE '%bl%'";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    while( result.next() != false )
                    {
                        SendMail.send( mailFrom, result.getString( "mail1" ), "マイナス残高ユーザー発生", "マイナス残高ユーザーが発生しました\r\n" + minusUserMsg );
                    }
                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.minusCheck()] Exception:" + e.toString() );
            }
        }
    }

    /*
     * 8.ホテル支払分と仕訳ユーザの残高について、差異があった場合、 research_staff のmail2に"bl"がはいったユーザのメアドに送信する。
     */
    public static void RecvCheck()
    {
        PreparedStatement prestate = null;
        ResultSet result = null;

        String query;
        String ngBalanceMsg = "";

        try
        {
            query = "SELECT bill.id,uu.sumPoint,bd.amount FROM hh_bko_bill bill";
            query += " INNER JOIN hh_bko_bill_detail bd ON bill.bill_slip_no = bd.bill_slip_no AND bd.account_title_cd =?";
            query += " LEFT JOIN";
            query += "   (SELECT pay.ext_code,sum(used.point) AS sumPoint FROM hh_user_point_pay_used used";
            query += "   INNER JOIN hh_user_basic ub ON  ub.user_id = used.user_id AND ub.login_flag = 0";
            query += "   INNER JOIN hh_user_point_pay pay ON pay.user_id = used.user_id AND pay.seq = used.seq";
            query += "   INNER JOIN hh_rsv_reserve_basic rb ON rb.id = pay.ext_code";
            query += "   WHERE  pay.code = ?";
            query += "   AND  ((used.get_date > ? AND used.get_date <= ?)";
            query += "   OR   (used.get_date = ? AND used.get_time >  rb.deadline_time)";
            query += "   OR   (used.get_date = ? AND used.get_time <= rb.deadline_time))";
            query += "   GROUP BY pay.ext_code) uu";
            query += " ON uu.ext_code = bill.id ";
            query += " AND bill.bill_date=? AND bill.id<100000000";
            query += " WHERE bill.bill_date=? AND bill.id<100000000";
            query += " HAVING (sumPoint IS NULL AND amount<> 0) OR sumPoint <> amount";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_TITLE_CODE );
            prestate.setInt( 2, USE_CODE );
            prestate.setInt( 3, closingStartDate );
            prestate.setInt( 4, closingDate );
            prestate.setInt( 5, closingStartDate );
            prestate.setInt( 6, closingDate + 1 );
            prestate.setInt( 7, closingDate / 100 );
            prestate.setInt( 8, closingDate / 100 );

            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    ngBalanceMsg += "id=" + result.getString( "id" ) + ",ユーザー合計=" + result.getInt( "sumPoint" ) + ",ホテル支払金額=" + result.getInt( "amount" ) + "\r\n";
                }
            }

            query = "SELECT pay.ext_code,sum(used.point) AS sumPoint,bd.amount FROM hh_user_point_pay_used used";
            query += " INNER JOIN hh_user_basic ub ON  ub.user_id = used.user_id AND ub.login_flag = 0";
            query += " INNER JOIN hh_user_point_pay pay ON pay.user_id = used.user_id AND pay.seq = used.seq";
            query += " INNER JOIN hh_rsv_reserve_basic rb ON rb.id = pay.ext_code";
            query += " LEFT JOIN hh_bko_bill bill ON bill.id = pay.ext_code AND bill.bill_date = ?";
            query += " LEFT JOIN hh_bko_bill_detail bd ON bill.bill_slip_no = bd.bill_slip_no AND bd.account_title_cd =?";
            query += " WHERE  pay.code = ?";
            query += " AND  ((used.get_date > ? AND used.get_date <= ?)";
            query += " OR   (used.get_date = ? AND used.get_time >  rb.deadline_time)";
            query += " OR   (used.get_date = ? AND used.get_time <= rb.deadline_time))";
            query += " GROUP BY pay.ext_code";
            query += " HAVING amount IS NULL";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, closingDate / 100 );
            prestate.setInt( 2, USE_TITLE_CODE );
            prestate.setInt( 3, USE_CODE );
            prestate.setInt( 4, closingStartDate );
            prestate.setInt( 5, closingDate );
            prestate.setInt( 6, closingStartDate );
            prestate.setInt( 7, closingDate + 1 );

            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    ngBalanceMsg += "id=" + result.getString( "ext_code" ) + ",ユーザー合計=" + result.getInt( "sumPoint" ) + ",ホテル支払金額=" + result.getInt( "amount" ) + "\r\n";
                }
            }

            if ( !ngBalanceMsg.equals( "" ) )
            {
                System.out.println( "ngBalanceMsg :" + ngBalanceMsg );

                query = "SELECT mail1 FROM research_staff WHERE mail2 LIKE '%bl%'";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    while( result.next() != false )
                    {
                        SendMail.send( mailFrom, result.getString( "mail1" ), "支払い金額とユーザー別合計に差異発生", "支払い金額とユーザー別合計に差異発生が発生しました\r\n" + ngBalanceMsg );
                    }
                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.RsvCheck()] Exception:" + e.toString() );
            }
        }
    }

    /*
     * 9.エラーがなかった場合にresearch_staff のmail2に"bl"がはいったユーザのメアドに仕訳結果を送信する。
     */
    public static void BalanceMail()
    {
        PreparedStatement prestate = null;
        ResultSet result = null;

        String query;
        String BalanceMail = "";
        int sumPoint = 0;
        int sumPointA = 0;
        int sumPointB = 0;
        int sumPointC = 0;
        try
        {
            query = "SELECT sum(used.point) AS sumPoint,sum(used.point_a) AS sumPointA,sum(used.point_b) AS sumPointB FROM hh_user_point_pay_used used";
            query += " INNER JOIN hh_user_basic ub ON  ub.user_id = used.user_id AND ub.login_flag = 0";
            query += " INNER JOIN hh_user_point_pay pay ON pay.user_id = used.user_id AND pay.seq = used.seq";
            query += " INNER JOIN hh_rsv_reserve_basic rb ON rb.id = pay.ext_code";
            query += " WHERE  pay.code = ?";
            query += " AND  ((used.get_date > ? AND used.get_date <= ?)";
            query += " OR   (used.get_date = ? AND used.get_time >  rb.deadline_time)";
            query += " OR   (used.get_date = ? AND used.get_time <= rb.deadline_time))";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, USE_CODE );
            prestate.setInt( 2, closingStartDate );
            prestate.setInt( 3, closingDate );
            prestate.setInt( 4, closingStartDate );
            prestate.setInt( 5, closingDate + 1 );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    sumPoint = result.getInt( "sumPoint" );
                    sumPointA = result.getInt( "sumPointA" );
                    sumPointB = result.getInt( "sumPointB" );
                }
            }
            query = "SELECT sum(used.point_b) AS sumPointC FROM hh_user_point_pay_used used ";
            query += " INNER JOIN hh_user_basic ub ON  ub.user_id = used.user_id AND ub.login_flag = 0";
            query += " INNER JOIN hh_user_point_pay pay ON pay.user_id = used.user_id AND pay.seq = used.seq";
            query += " WHERE  pay.code = ?";
            query += " AND  used.get_date >= ? AND used.get_date <= ?";
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, CHANGE_CODE );
            prestate.setInt( 2, closingStartDate );
            prestate.setInt( 3, closingDate );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    sumPointC = result.getInt( "sumPointC" );
                }
            }
            BalanceMail = "ハピホテマイル仕訳" + closingDate / 10000 + "年" + closingDate / 100 % 100 + "月\r\n";
            BalanceMail += "販売促進費（Aマイル 合計）:" + (sumPointA - sumPointC);
            BalanceMail += ",前受金（Bマイル 合計）:" + (sumPointB + sumPointC);
            BalanceMail += "／未払い金:" + sumPoint;
            System.out.println( "BalanceMail :" + BalanceMail );

            query = "SELECT mail1 FROM research_staff WHERE mail2 LIKE '%bl%'";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    SendMail.send( mailFrom, result.getString( "mail1" ), "ハピホテマイル仕訳（" + closingDate / 10000 + "年" + closingDate / 100 % 100 + "月）", BalanceMail );
                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            errMsg += e.toString() + "\r\n";
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
                Logging.error( "[UserPointUsed.BalanceMail()] Exception:" + e.toString() );
            }
        }
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
        PreparedStatement prestate = null;
        ResultSet result = null;
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
            Logging.error( "[UserPointUsed.hashMap()] Exception:" + e.toString() );
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
                Logging.error( "UserPointUsed.hashMap()] Exception:" + e.toString() );
            }
        }
        return(rtMap);
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
            connection = DriverManager.getConnection( DB_URL, user, password );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return connection;
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
            if ( connection != null )
                connection.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

}
