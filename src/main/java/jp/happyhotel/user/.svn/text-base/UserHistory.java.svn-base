/*
 * @(#)UserHistory.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ユーザ履歴取得クラス
 */
package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserHistory;
import jp.happyhotel.data.DataUserPoint;
import jp.happyhotel.data.DataUserViewHotel;

/**
 * ユーザ履歴取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2007/09/05
 * @version 1.1 2007/11/26
 */
public class UserHistory implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID = -937819653723950778L;

    private final int           HOTEL_DETAIL     = 3;
    private int                 m_historyCount;
    private DataUserHistory[]   m_userHistory;
    private DataUserPoint[]     m_userPoint;
    private DataUserViewHotel[] m_userViewHotel;

    /**
     * データを初期化します。
     */
    public UserHistory()
    {
        m_historyCount = 0;
    }

    /** ユーザ履歴件数取得 **/
    public int getCount()
    {
        return(m_historyCount);
    }

    /** ユーザ履歴取得 **/
    public DataUserHistory[] getUserHistory()
    {
        return(m_userHistory);
    }

    /** ユーザ履歴取得 **/
    public DataUserPoint[] getUserPointHistory()
    {
        return(m_userPoint);
    }

    /** ユーザ履歴取得 **/
    public DataUserViewHotel[] getUserViewHotel()
    {
        return(m_userViewHotel);
    }

    /**
     * ユーザー履歴情報取得(ホテルランク順)
     * 
     * @param userId ユーザーID
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHistoryList(String userId, int countNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(true);
        }
        if ( userId.compareTo( "" ) == 0 )
        {
            return(true);
        }
        query = "SELECT  * FROM hh_user_history WHERE user_id = ? GROUP BY id ORDER BY seq DESC ";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + 0 + "," + countNum;
        }

        count = 0;
        m_historyCount = 0;

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
                    m_historyCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_userHistory = new DataUserHistory[this.m_historyCount];
                for( i = 0 ; i < m_historyCount ; i++ )
                {
                    m_userHistory[i] = new DataUserHistory();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ユーザ履歴の取得
                    this.m_userHistory[count++].setData( result );
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUserHistory] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル閲覧履歴追加
     * 
     * @param userId ユーザーID
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setHistory(String userId, int hotelId, HttpServletRequest request)
    {
        boolean ret;
        DataUserHistory duh;

        duh = new DataUserHistory();
        duh.setUserId( userId );
        duh.setId( hotelId );
        duh.setDispDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        duh.setDispTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        duh.setDispIp( request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr() );
        duh.setDispUserAgent( request.getHeader( "user-agent" ) );
        ret = duh.insertData();

        return(ret);
    }

    /**
     * ユーザー履歴情報取得(ホテルランク順)
     * 
     * @param userId ユーザーID
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getPointHistoryList(String userId, int countNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(true);
        }
        if ( userId.compareTo( "" ) == 0 )
        {
            return(true);
        }
        query = "SELECT hh_user_view_hotel.id,hh_user_view_hotel.user_id,MAX(regist_date*1000000+regist_time) AS seq";
        query = query + "  FROM hh_user_view_hotel,hh_hotel_basic";
        query = query + "  WHERE hh_hotel_basic.id = hh_user_view_hotel.id";
        query = query + "  AND   hh_user_view_hotel.user_id =?";
        query = query + "  AND   hh_hotel_basic.kind <=7  AND hh_hotel_basic.rank >= 1";
        query = query + "  GROUP BY hh_user_view_hotel.id";
        query = query + "  ORDER BY seq DESC";
        /*
         * query = "SELECT hh_user_point.*, MAX(hh_user_point.seq) AS max_seq FROM hh_hotel_basic,hh_user_point";
         * query = query + "  WHERE hh_hotel_basic.id = hh_user_point.ext_code";
         * query = query + "  AND   hh_user_point.user_id =?";
         * query = query + "  AND   hh_hotel_basic.kind <=7  AND hh_hotel_basic.rank >= 1";
         * query = query + "  AND   hh_user_point.point_kind = ?";
         * query = query + "  GROUP BY hh_user_point.ext_code";
         * query = query + "  ORDER BY max_seq DESC";
         */
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + 0 + "," + countNum;
        }

        count = 0;
        m_historyCount = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            // prestate.setInt( 2, HOTEL_DETAIL );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_historyCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                // this.m_userPoint = new DataUserPoint[this.m_historyCount];
                this.m_userViewHotel = new DataUserViewHotel[this.m_historyCount];
                for( i = 0 ; i < m_historyCount ; i++ )
                {
                    m_userViewHotel[i] = new DataUserViewHotel();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ユーザ履歴の取得
                    this.m_userViewHotel[count++].setViewHotelData( result );
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUserPointHistoryList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

}
