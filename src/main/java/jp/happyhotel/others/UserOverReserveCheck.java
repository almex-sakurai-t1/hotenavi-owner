package jp.happyhotel.others;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DBConnectionBatch;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.SendMail;

public class UserOverReserveCheck
{
    public static void userReserveCheck(Connection connection)
    {
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        String messageBody = "";
        String messageSubject = "不正利用対策（連続予約)";
        int threeDaysAgo = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -3 );
        int fourDaysAgo = DateEdit.addDay( threeDaysAgo, -1 );
        boolean ret = false;

        query = "SELECT reserve.user_id,count(*) reserveCount";
        query += ",CASE WHEN pre.preCount IS NULL THEN 0 ELSE pre.preCount END pastReserveCount ";
        query += " FROM newRsvDB.hh_rsv_reserve reserve";
        query += " LEFT JOIN (SELECT user_id,count(*) preCount FROM newRsvDB.hh_rsv_reserve WHERE reserve_date <= ?  AND `status` IN (1,2) AND  payment = 1 GROUP BY user_id) pre";
        query += " ON reserve.user_id = pre.user_id ";
        query += " WHERE reserve.reserve_date >= ? AND reserve.`status` IN (1,2) AND reserve.payment = 1";
        query += " GROUP BY reserve.user_id ";
        query += " HAVING reserveCount >= 3";
        query += " ORDER BY reserveCount DESC";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, threeDaysAgo );
            prestate.setInt( 2, fourDaysAgo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                messageBody = "ハピホテ予約で同一アカウント（会員）から連続して3日間で3回予約が入ったユーザーを抽出しました。\r\n";
                messageBody += "\r\n";
                while( result.next() )
                {
                    messageBody += "【ユーザーID】                        :" + result.getString( "user_id" ) + "\r\n";
                    messageBody += "【三日前以降のクレジット決済予約件数】:" + result.getInt( "reserveCount" ) + "\r\n";
                    messageBody += "【過去の予約件数(三日前以前)】        :" + result.getInt( "pastReserveCount" ) + "\r\n";
                    if ( !result.isLast() )
                    {
                        messageBody += "---------------------------------------------" + "\r\n";
                    }
                    else
                    {
                        messageBody += "\r\n";
                    }
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            messageBody = "ハピホテ予約で同一アカウント（会員）から連続して3日間で3回予約が入ったユーザー抽出時、エラーが発生しました。\r\n";
            messageBody += e.toString();
            System.out.println( "[UserOverReserveCheck.userReserveCheck] Exception=" + e.toString() );
            ret = true;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            if ( ret )
            {
                // メールを送信する
                SendMail.send( "reserve@happyhotel.jp", "service@happyhotel.jp", messageSubject, messageBody );
                System.out.println( "SendMail ok" );
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        userReserveCheck( DBConnectionBatch.makeConnection() );
    }
}
