/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 クーポンシリアル番号取得クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterCoupon;
import jp.happyhotel.data.DataUserCoupon;
import jp.happyhotel.hotel.HotelCoupon;

/**
 * クーポン取得クラス。 ユーザのクーポンを取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2008/03/07
 */
public class UserCoupon implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -3924291538252750667L;

    private int               userCouponCount;
    private DataUserCoupon    userCoupon;
    private DataUserCoupon[]  userCouponHistory;

    /**
     * データを初期化します。
     */
    public UserCoupon()
    {
        userCouponCount = 0;
    }

    /** クーポン基本情報件数取得 **/
    public int getCount()
    {
        return(userCouponCount);
    }

    /** クーポン基本情報取得 **/
    public DataUserCoupon getCouponInfo()
    {
        return(userCoupon);
    }

    /** クーポン基本情報取得 **/
    public DataUserCoupon[] getCouponHistoryInfo()
    {
        return(userCouponHistory);
    }

    /**
     * ユーザークーポン情報を取得する（IDから）
     * 
     * @param id ホテルID
     * @param userId ユーザーID
     * @param printDate 発行日
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserCoupon(int id, String userId, int printDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        userCouponCount = 0;

        if ( (id < 0) || (userId == null) || (userId.equals( "" ) != false) )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_coupon";
        query = query + " WHERE id = ?";
        query = query + " AND user_id = ?";
        if ( printDate > 0 )
        {
            query = query + " AND print_date = ?";
        }
        query = query + " ORDER BY print_date DESC, print_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, userId );
            if ( printDate > 0 )
            {
                prestate.setInt( 3, printDate );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    userCouponCount = result.getRow();
                }
                this.userCoupon = new DataUserCoupon();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // クーポン情報の設定
                    this.userCoupon.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserCoupon] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        if ( userCouponCount != 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * クーポンシリアル番号を登録する
     * 
     * @param hotelId ホテルID
     * @param available 利用フラグ（0:制御なし、1:メンバー、2:ビジター）
     * @param userStatus 会員識別子（UC：メンバーPC、UM：メンバー携帯、NC：ビジターPC、NM：ビジター携帯）
     * @param mailAddrMd5 ハッシュ値
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setUserCoupon(int hotelId, int available, String userId, String userStatus, String mailAddrMd5)
    {
        int startDate;
        int endDate;
        int startDay;
        int endDay;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        HotelCoupon hc;
        DataMasterCoupon dmc;
        Calendar cal;

        if ( hotelId < 0 || userId == null || (userId.equals( "" ) != false) || userStatus == null )
        {
            return(false);
        }

        hc = new HotelCoupon();
        dmc = new DataMasterCoupon();
        startDate = 0;
        endDate = 0;
        ret = hc.getCouponData( hotelId, available );
        if ( ret != false )
        {
            Logging.info( "[setUserCoupon] seq=" + hc.getHotelCoupon().getSeq() );
            ret = dmc.getData( hotelId, hc.getHotelCoupon().getSeq(), available );
            Logging.info( "[setUserCoupon] seq=" + dmc.getSeq() );
        }
        else
            ret = false;

        if ( ret != false )
        {
            // 今日の日付を取得する
            startDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            endDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            // hh_master_coupon有効期限を取得
            startDay = dmc.getStartDay();
            endDay = dmc.getEndDay();
            if ( startDay > 0 )
            {
                // 開始日付をカレンダー型に格納する
                cal = new GregorianCalendar( startDate / 10000, startDate / 100 % 100 - 1, startDate % 100 % 100 );
                cal.add( Calendar.DATE, startDay );
                startDate = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );
            }
            if ( endDay > 0 )
            {
                // 終了日付をカレンダー型に格納する
                cal = new GregorianCalendar( endDate / 10000, (endDate / 100 % 100) - 1, endDate % 100 % 100 );
                cal.add( Calendar.DATE, endDay );
                endDate = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );
                if ( endDate > hc.getHotelCoupon().getPeriod() )
                {
                    endDate = hc.getHotelCoupon().getPeriod();
                }
            }
            else
            {
                endDate = hc.getHotelCoupon().getPeriod();
            }
        }
        query = " INSERT hh_user_coupon SET";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " coupon_no = 0,";
        query = query + " user_id = ?,";
        query = query + " user_status = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " mail_addr_md5 = ?,";
        query = query + " print_date = ?,";
        query = query + " print_time = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, dmc.getSeq() );
            prestate.setString( 3, userId );
            prestate.setString( 4, userStatus );
            prestate.setInt( 5, startDate );
            prestate.setInt( 6, endDate );
            prestate.setString( 7, mailAddrMd5 );
            prestate.setInt( 8, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 9, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
            DBConnection.releaseResources( prestate );

            getCouponInfo().setId( hotelId );
            getCouponInfo().setSeq( dmc.getSeq() );
            getCouponInfo().setUserId( userId );
            getCouponInfo().setUserStatus( userStatus );
            getCouponInfo().setStartDate( startDate );
            getCouponInfo().setEndDate( endDate );
            getCouponInfo().setMailAddrMd5( mailAddrMd5 );
            getCouponInfo().setPrintDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            getCouponInfo().setPrintTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            getCouponInfo().setUsedFlag( 0 );

            if ( ret != false )
            {
                try
                {
                    query = "SELECT @LAST_INSERT_ID AS coupon_no";// ←書き込まれた番号を取得
                    prestate = connection.prepareStatement( query );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        if ( result.next() != false )
                        {
                            getCouponInfo().setCouponNo( result.getInt( "coupon_no" ) );
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[setUserCoupon] Exception=" + e.toString() );
                }
                finally
                {
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[setUserCoupon] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * ユーザークーポン履歴を取得する
     * 
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserCouponHistory(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null || (userId.equals( "" ) != false) )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_coupon";
        query = query + " WHERE user_id = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );

            ret = getUserCouponHistorySub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserCouponHistory] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ユーザークーポン履歴情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getUserCouponHistorySub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        userCouponCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    userCouponCount = result.getRow();
                }
                this.userCouponHistory = new DataUserCoupon[this.userCouponCount];
                for( i = 0 ; i < userCouponCount ; i++ )
                {
                    userCouponHistory[i] = new DataUserCoupon();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ユーザークーポン情報の設定
                    this.userCouponHistory[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserCouponHistorySub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userCouponCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ホテル別、日付別クーポン発行履歴を取得する
     * 
     * @param class クラスID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelCouponHistory(int hotelId, int printDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( hotelId < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_coupon";
        query = query + " WHERE id = ?";
        if ( printDate > 0 )
        {
            query = query + " AND print_date = ?";
        }

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            if ( printDate > 0 )
            {
                prestate.setInt( 2, printDate );
            }

            ret = getHotelCouponHistorySub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelCouponHistory] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ホテル別、日付別クーポン発行情報のデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getHotelCouponHistorySub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        userCouponCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    userCouponCount = result.getRow();
                }
                this.userCouponHistory = new DataUserCoupon[this.userCouponCount];
                for( i = 0 ; i < userCouponCount ; i++ )
                {
                    userCouponHistory[i] = new DataUserCoupon();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ユーザークーポン情報の設定
                    this.userCouponHistory[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelCouponHistorySub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userCouponCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * クーポンシリアル番号を取得する(ID、seq、発行日から)
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号(hh_master_coupon.seq)
     * @param userId ユーザーID
     * @param printDate 発行日
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserCoupon(int id, int seq, String userId, int printDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        userCouponCount = 0;
        if ( (id < 0) || (userId == null) || (userId.equals( "" ) != false) )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_coupon";
        query = query + " WHERE id = ?";
        query = query + " AND seq = ?";
        query = query + " AND user_id = ?";
        if ( printDate > 0 )
        {
            query = query + " AND print_date = ?";
        }
        query = query + " ORDER BY print_date DESC, print_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            prestate.setString( 3, userId );
            if ( printDate > 0 )
            {
                prestate.setInt( 4, printDate );
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    userCouponCount = result.getRow();
                }
                this.userCoupon = new DataUserCoupon();

                result.beforeFirst();
                if ( result.next() != false )
                {
                    // クーポン情報の設定
                    this.userCoupon.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserCoupon] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        if ( userCouponCount != 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * 使用済みクーポンのデータ数を取得する(ID、seqから)
     * 
     * @param hotelId ホテルID
     * @param seq 管理番号(hh_master_coupon.seq)
     * @param userId ユーザーID
     * @return 処理結果(0:使用済みなし、それ以外：○回使用済み)
     */
    public int getUsedCouponCount(int id, int seq, String userId)
    {
        String query;
        int count;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( (id < 0) || (userId == null) || (userId.equals( "" ) != false) )
        {
            return(0);
        }
        count = 0;
        query = "SELECT count(*) FROM hh_user_coupon";
        query = query + " WHERE id = ?";
        query = query + " AND seq = ?";
        query = query + " AND user_id = ?";
        query = query + " AND used_flag > 0";
        query = query + " ORDER BY print_date DESC, print_time DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            prestate.setString( 3, userId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUsedCouponCount] Exception=" + e.toString() );
            System.out.println( e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(count);
    }
}
