/*
 * @(#)OwnerLoginInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 オーナーログイン情報取得クラス
 */

package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotenaviHotel;
import jp.happyhotel.data.DataOwnerUser;
import jp.happyhotel.data.DataOwnerUserLock;
import jp.happyhotel.data.DataOwnerUserSecurity;

/**
 * オーナーログイン情報取得クラス。 オーナーログイン情報を取得する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.00 2007/12/03
 */
public class OwnerLoginInfo implements Serializable
{
    /**
	 *
	 */
    private static final long     serialVersionUID = 705628811172679729L;

    private int                   ownerUserCount;
    private DataOwnerUser         ownerUser;
    private DataOwnerUserSecurity ownerUserSecurity;
    private DataHotenaviHotel     hotenaviHotel;

    /**
     * データを初期化します。
     */
    public OwnerLoginInfo()
    {
        ownerUserCount = 0;
        ownerUser = new DataOwnerUser();
        ownerUserSecurity = new DataOwnerUserSecurity();
        hotenaviHotel = new DataHotenaviHotel();
    }

    /** オーナー基本情報件数取得 **/
    public int getCount()
    {
        return(ownerUserCount);
    }

    /** オーナー基本情報取得 **/
    public DataOwnerUser getUserInfo()
    {
        return(ownerUser);
    }

    /** オーナーアクセス権情報取得 **/
    public DataOwnerUserSecurity getUserSecurityInfo()
    {
        return(ownerUserSecurity);
    }

    /** ホテナビホテル情報取得 **/
    public DataHotenaviHotel getHotenaviHotel()
    {
        return(hotenaviHotel);
    }

    /**
     * オーナー基本情報を取得する（IDから）
     * 
     * @param netId ネットワークID
     * @param netPass ネットワークパスワード
     * @param userId ユーザID
     * @param userPass ユーザパスワード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getLogin(String netId, String netPass, String userId, String userPass)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT owner_user.* FROM owner_user,hotel";
        query = query + " WHERE hotel.owner_user = ? AND hotel.owner_password = ?";
        query = query + " AND owner_user.loginid = ? AND owner_user.passwd_pc = ?";
        query = query + " AND hotel.hotel_id = owner_user.hotelid";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, netId );
            prestate.setString( 2, netPass );
            prestate.setString( 3, userId );
            prestate.setString( 4, userPass );

            ret = getLoginSub( prestate );
            if ( ret != false )
            {
                // ホテナビホテル情報取得
                hotenaviHotel = new DataHotenaviHotel();
                hotenaviHotel.getData( this.ownerUser.getHotelId() );
                // セキュリティ情報取得
                ownerUserSecurity = new DataOwnerUserSecurity();
                ownerUserSecurity.getData( this.ownerUser.getHotelId(), this.ownerUser.getUserId() );
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[OwnerLoginInfo.getLogin] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * オーナー基本情報を取得する（クッキーから）
     * 
     * @param cookieValue クッキー値
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getLoginByCookie(String cookieValue)
    {
        boolean ret;
        String query;
        String hotelId;
        String userId;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( cookieValue == null )
        {
            return(false);
        }

        hotelId = cookieValue.substring( 0, cookieValue.indexOf( ":" ) );
        userId = cookieValue.substring( cookieValue.indexOf( ":" ) + 1 );

        query = "SELECT owner_user.* FROM owner_user";
        query = query + " WHERE owner_user.hotelid = ? AND owner_user.userid = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, hotelId );
            prestate.setString( 2, userId );

            ret = getLoginSub( prestate );
            if ( ret != false )
            {
                // ホテナビホテル情報取得
                hotenaviHotel = new DataHotenaviHotel();
                hotenaviHotel.getData( this.ownerUser.getHotelId() );
                // セキュリティ情報取得
                ownerUserSecurity = new DataOwnerUserSecurity();
                ownerUserSecurity.getData( this.ownerUser.getHotelId(), this.ownerUser.getUserId() );
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[OwnerLoginInfo.getLoginByCookie] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * オーナー基本情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getLoginSub(PreparedStatement prestate)
    {
        ResultSet result = null;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                ownerUser = new DataOwnerUser();

                if ( result.next() != false )
                {
                    ownerUserCount = 1;
                    // オーナー基本データ情報の設定
                    this.ownerUser.setData( result );
                }
                else
                {
                    ownerUserCount = 0;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[OwnerLoginInfo.getLoginSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( ownerUserCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 連続ログイン失敗チェック
     * 
     * @param userId 新ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean userLoginHistoryCheck(String hotelId, String loginId)
    {
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int userId = 0;
        try
        {
            query = "SELECT owner_user.userid FROM owner_user";
            query = query + " WHERE owner_user.hotelid = ? AND owner_user.loginid = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int i = 1;
            prestate.setString( i++, hotelId );
            prestate.setString( i++, loginId );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                userId = result.getInt( "userid" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerLoginInfo.userLoginHistoryCheck] Exception=" + e.toString(), e );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( userId == 0 )
        {
            return true;
            // loginId が存在しない。ロックとは関係ないので、とりあえずtrueでは返す
        }
        else
        {
            int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            DataOwnerUserLock lock = new DataOwnerUserLock();
            if ( lock.getData( hotelId, userId ) )
            {
                // アカウントがロックされている
                if ( lock.getLockStatus() == 1 && lock.getLockDate() >= nowDate && lock.getLockTime() >= nowTime )
                {
                    return false;
                }
                // 初期化
                lock.setLockStatus( 0 );
                lock.setLockDate( 0 );
                lock.setLockTime( 0 );
                lock.updateData( hotelId, userId );
            }

            int[] time = DateEdit.addSec( nowDate, nowTime, -900 );
            int nDate = time[0];
            int nTime = time[1];

            // 条件によってかわるのでfinalを付けません
            query = "SELECT * FROM owner_user_log"
                    + " WHERE hotelid = ?"
                    + " AND userid = ?"
                    + " AND log_level != 1";

            // 15分前は本日の場合
            if ( time[0] == nowDate )
            {
                query += " AND login_date = ?"
                        + " AND login_time BETWEEN ? AND ?";
            }
            // 15分前は昨日の場合
            else
            {
                query += " AND login_date >= ?"
                        + " AND (login_time  >= ? OR login_time <= ?)";
            }
            query += " ORDER BY login_date DESC,login_time DESC"
                    + " LIMIT 1";

            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                int i = 1;
                prestate.setString( i++, hotelId );
                prestate.setInt( i++, userId );
                prestate.setInt( i++, nDate );
                prestate.setInt( i++, nTime );
                prestate.setInt( i++, nowTime );
                result = prestate.executeQuery();

                if ( result.next() )
                {
                    int loginDate = Integer.parseInt( result.getString( "login_date" ).replace( "-", "" ) );
                    int loginTime = Integer.parseInt( result.getString( "login_time" ).replace( ":", "" ) );
                    return isFailOverFiveTimes( hotelId, userId, loginDate, loginTime, lock, connection );
                }
                return true;

            }
            catch ( Exception e )
            {
                Logging.error( "[OwnerLoginInfo.userLoginHistoryCheck] Exception=" + e.toString(), e );
                return false;
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }
    }

    /**
     * 連続ログイン5回チェック
     * 
     * @param userId ユーザID
     * @param loginDate 日付
     * @param loginTime 時間
     * @param lock DataUserLock
     * @param connection
     * @return 処理結果 TRUE:正常,FALSE:異常(5回以上失敗)
     */
    public boolean isFailOverFiveTimes(
            String hotelId, int userId, int loginDate, int loginTime, DataOwnerUserLock lock, Connection connection
            )
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        // 15分前の日付
        int[] time = DateEdit.addSec( loginDate, loginTime, -900 );
        int lDate = time[0];
        int lTime = time[1];

        // 条件によってかわるのでfinalを付けません
        String query = "SELECT * FROM owner_user_log WHERE hotelid = ? AND userid = ?";

        // 15分前は本日の場合
        if ( time[0] == nowDate )
        {
            query += " AND login_date = ?"
                    + " AND login_time BETWEEN ? AND ?";
        }
        // 15分前は昨日の場合
        else
        {
            query += " AND login_date >= ?"
                    + " AND (login_time >= ? OR login_time <= ?)";
        }
        query += " ORDER BY login_date DESC,login_time DESC"
                + " LIMIT 5 ";

        try
        {
            prestate = connection.prepareStatement( query );
            int i = 1;
            prestate.setString( i++, hotelId );
            prestate.setInt( i++, userId );
            prestate.setInt( i++, lDate );
            prestate.setInt( i++, lTime );
            prestate.setInt( i++, loginTime );
            result = prestate.executeQuery();
            int count = 0;
            while( result.next() )
            {
                if ( result.getInt( "log_level" ) != 1 && result.getInt( "log_level" ) < 100 )
                    count++;
            }
            // 連続失敗履歴5回の場合、記録
            if ( count == 5 )
            {
                int[] TimesAfterFifteenMin = DateEdit.addSec( nowDate, nowTime, 900 );
                lock.setLockStatus( 1 );
                lock.setLockDate( TimesAfterFifteenMin[0] );
                lock.setLockTime( TimesAfterFifteenMin[1] );
                lock.setMistakeCount( lock.getMistakeCount() + 1 );
                lock.setMistakeDate( nowDate );
                lock.setMistakeTime( nowTime );
                if ( !lock.updateData( hotelId, userId ) )
                {
                    lock.setHotelId( hotelId );
                    lock.setUserId( userId );
                    lock.insertData();
                }
                return false;
            }
            return true;

        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerLoginInfo.isFailOverFiveTimes] Exception=" + e.toString(), e );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }
}
