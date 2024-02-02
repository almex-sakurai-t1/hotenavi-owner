/*
 * @(#)UserMyBbs.java 1.00 2007/08/02 Copyright (C) ALMEX Inc. 2007 ユーザマイクチコミ取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * ユーザマイクチコミ取得・更新クラス。 ユーザのマイクチコミ情報を取得・更新する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/02
 * @version 1.1 2007/11/27
 */
public class UserMyBbs implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 1170777016941473403L;

    private int               userMyBbsCount;
    private int               userMyBbsReturnCount;
    private int               m_mybbsAllCount;
    private DataHotelBbs[]    userMyBbs;

    /**
     * データを初期化します。
     */
    public UserMyBbs()
    {
        userMyBbsCount = 0;
        userMyBbsReturnCount = 0;
        m_mybbsAllCount = 0;
    }

    /** ユーザマイクチコミ情報件数取得 **/
    public int getCount()
    {
        return(userMyBbsCount);
    }

    public int getReturnCount()
    {
        return(userMyBbsReturnCount);
    }

    public int getAllCount()
    {
        return(m_mybbsAllCount);
    }

    /** ユーザマイクチコミ情報取得 **/
    public DataHotelBbs[] getMyBbs()
    {
        return(userMyBbs);
    }

    /**
     * ユーザマイクチコミを取得する（IDから）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyBbsList(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_bbs";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
            query = query + " ORDER BY contribute_date DESC, contribute_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getMyBbsListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getMyBbsList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ホテルクチコミ一覧情報取得(日付順)
     * 
     * @param userId ユーザーID
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyBbsList(String userId, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT * FROM hh_hotel_bbs WHERE user_id = ?";
        query = query + " ORDER BY contribute_date DESC, contribute_time DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    userMyBbsCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userMyBbs = new DataHotelBbs[this.userMyBbsCount];
                for( i = 0 ; i < userMyBbsCount ; i++ )
                {
                    userMyBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.userMyBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getMyBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテルクチコミ総件数の取得
        query = "SELECT COUNT(*) FROM hh_hotel_bbs WHERE user_id = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 総件数の取得
                    this.m_mybbsAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getAllBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ユーザマイクチコミを取得する（端末番号から）
     * 
     * @param mobileTermno 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyBbsListByTermno(String mobileTermno)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_bbs.* FROM hh_hotel_bbs,hh_user_basic";

        if ( mobileTermno.compareTo( "" ) != 0 )
        {
            query = query + " WHERE hh_user_basic.mobile_termno = ?";
            query = query + " AND hh_user_basic.regist_status = 9";
            query = query + " AND hh_user_basic.del_flag = 0";
            query = query + " AND hh_user_basic.user_id = hh_hotel_bbs.user_id";
            query = query + " ORDER BY contribute_date DESC, contribute_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( mobileTermno.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, mobileTermno );
            }

            ret = getMyBbsListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMyAreaListByTermno] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 未読返信有ユーザクチコミ一覧情報取得(日付順)
     * 
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyBbsUnreadReturnList(String userId)
    {
        int i;
        int count;
        String query = null;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT * FROM hh_hotel_bbs";
        query = query + " WHERE user_id = ?";
        query = query + " AND still_flag = 2";
        query = query + " AND thread_status = 2";
        query = query + " AND return_still_flag = 1";
        query = query + " ORDER BY contribute_date DESC, contribute_time DESC";

        count = 0;

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    userMyBbsReturnCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userMyBbs = new DataHotelBbs[this.userMyBbsReturnCount];
                for( i = 0 ; i < userMyBbsReturnCount ; i++ )
                {
                    userMyBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.userMyBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getMyBbsUnreadReturnList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 未読返信ユーザクチコミを既読に変更
     * 
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateReturnStillFlag(String userId)
    {
        String query = null;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_hotel_bbs SET return_still_flag = 2";
        query = query + " WHERE user_id = ?";
        query = query + " AND still_flag = 2";
        query = query + " AND thread_status = 2";
        query = query + " AND ( return_still_flag = 0 OR return_still_flag = 1 )";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[updateReturnStillFlag] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(true);
    }

    /**
     * ユーザマイクチコミのデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMyBbsListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;

        count = 0;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    userMyBbsCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userMyBbs = new DataHotelBbs[this.userMyBbsCount];
                for( i = 0 ; i < userMyBbsCount ; i++ )
                {
                    userMyBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ユーザ情報の取得
                    this.userMyBbs[count++].setData( result );
                }

            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getMyBbsListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userMyBbsCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

}
