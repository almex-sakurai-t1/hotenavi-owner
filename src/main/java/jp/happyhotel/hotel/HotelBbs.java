/*
 * @(#)HotelBbs.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテルクチコミ情報取得・更新クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.data.DataHotelBbs;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserBbsVote;

/**
 * ホテルクチコミ情報取得・更新クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 */
public class HotelBbs implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID   = 5929966149311195690L;

    private static final int  HOTELBBS_POINT_MAX = 5;

    private int               m_bbsCount;
    private DataHotelBbs[]    m_hotelBbs;
    private int               m_bbsAllCount;
    private DataUserBasic     m_userBasic;
    private boolean           m_memberFlag;

    /**
     * データを初期化します。
     */
    public HotelBbs()
    {
        m_bbsCount = 0;
        m_bbsAllCount = 0;
        m_memberFlag = false;
    }

    /** ホテルクチコミ情報件数取得 **/
    public int getBbsCount()
    {
        return(m_bbsCount);
    }

    /** ホテルクチコミ情報総件数取得 **/
    public int getBbsAllCount()
    {
        return(m_bbsAllCount);
    }

    /** ホテルクチコミ情報取得 **/
    public DataHotelBbs[] getHotelBbs()
    {
        return(m_hotelBbs);
    }

    /** ユーザ基本情報取得 **/
    public DataUserBasic getUserBasic()
    {
        return(m_userBasic);
    }

    /** ユーザ基本情報取得結果 **/
    public boolean isMember()
    {
        return(m_memberFlag);
    }

    /**
     * ホテルクチコミ情報取得
     * 
     * @param hotelId ホテルID(0: 全件)
     * @param type 表示方法( 0:掲載判断なし,1:返信なし,2:返信なし(オーナー),3:すべて表示(オーナー), 　4:未読(オーナー),5:既読(オーナー),6:返信承認待,7:返信保留,8:すべて表示,9:保留, 10:掲載不可,11:新着クチコミ用, 12:社内保留 )
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param kindFlag 0:すべて1:クチコミのみ2:ご意見ご要望
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getOwnerBbsList(int hotelId, int type, int countNum, int pageNum, int kindFlag)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // ホテルが許可したもののみ表示
        query = "SELECT * FROM hh_hotel_bbs";
        if ( type == 0 )
        {
            query = query + " WHERE thread_status = 0";
        }
        else if ( type == 1 )
        {
            query = query + " WHERE still_flag < 2";
        }
        else if ( type == 2 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag < 2";
        }
        else if ( type == 3 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 OR thread_status = 5)";
        }
        else if ( type == 4 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag = 0";
        }
        else if ( type == 5 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag = 1";
        }
        else if ( type == 6 )
        {
            query = query + " WHERE ( thread_status = 2 OR thread_status = 3 ) AND still_flag = 3 ";
        }
        else if ( type == 7 )
        {
            query = query + " WHERE ( thread_status = 2 OR thread_status = 3 ) AND still_flag = 4 ";
        }
        else if ( type == 9 )
        {
            query = query + " WHERE thread_status = 3";
        }
        else if ( type == 10 )
        {
            query = query + " WHERE ( thread_status = 4 OR thread_status = 5 )";
        }
        else if ( type == 11 )
        {
            query = query + " WHERE (thread_status = 1 OR thread_status = 2)";
        }
        else if ( type == 12 )
        {
            query = query + " WHERE thread_status = 6";
        }

        if ( hotelId > 0 )
        {
            if ( type >= 0 && type <= 7 )
                query = query + " AND id= ?";
            else if ( type >= 9 && type <= 12 )
                query = query + " AND id= ?";
            else
                query = query + " WHERE id= ?";
        }
        if ( kindFlag == 1 )
        {
            if ( (type >= 0 && type <= 7) || hotelId > 0 )
            {
                query = query + " AND kind_flag=0";
            }
            else if ( (type >= 9 && type <= 12) || hotelId > 0 )
            {
                query = query + " AND kind_flag=0";
            }
            else
            {
                query = query + " WHERE kind_flag=0";
            }
        }
        else if ( kindFlag == 2 )
        {
            if ( (type >= 0 && type <= 7) || hotelId > 0 )
            {
                query = query + " AND kind_flag=1";
            }
            else if ( (type >= 9 && type <= 12) || hotelId > 0 )
            {
                query = query + " AND kind_flag=1";
            }
            else
            {
                query = query + " WHERE kind_flag=1";
            }
        }

        if ( type == 0 || type == 1 || type == 6 || type == 7 || type == 9 )
        {
            query = query + " ORDER BY contribute_date, contribute_time";
        }
        else
        {
            query = query + " ORDER BY contribute_date DESC, contribute_time DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( hotelId > 0 )
            {
                prestate.setInt( 1, hotelId );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_bbsCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelBbs = new DataHotelBbs[this.m_bbsCount];
                for( i = 0 ; i < m_bbsCount ; i++ )
                {
                    m_hotelBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUnPostBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテルクチコミ総件数の取得
        query = "SELECT COUNT(*) FROM hh_hotel_bbs";
        if ( type == 0 )
        {
            query = query + " WHERE thread_status = 0";
        }
        else if ( type == 1 )
        {
            query = query + " WHERE still_flag < 2";
        }
        else if ( type == 2 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag < 2";
        }
        else if ( type == 3 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 OR thread_status = 5)";
        }
        else if ( type == 4 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag = 0";
        }
        else if ( type == 5 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag = 1";
        }
        else if ( type == 6 )
        {
            query = query + " WHERE ( thread_status=2 OR thread_status=3 ) AND still_flag=3 ";
        }
        else if ( type == 7 )
        {
            query = query + " WHERE ( thread_status=2 OR thread_status=3 ) AND still_flag=4 ";
        }
        else if ( type == 9 )
        {
            query = query + " WHERE thread_status=3";
        }
        else if ( type == 10 )
        {
            query = query + " WHERE ( thread_status=4 OR thread_status=5 )";
        }
        else if ( type == 11 )
        {
            query = query + " WHERE (thread_status=1 OR thread_status=2)";
        }
        else if ( type == 12 )
        {
            query = query + " WHERE thread_status = 6";
        }

        if ( hotelId > 0 )
        {
            if ( type >= 0 && type <= 7 )
                query = query + " AND id= ?";
            else if ( type >= 9 && type <= 12 )
                query = query + " AND id= ?";
            else
                query = query + " WHERE id= ?";
        }
        if ( kindFlag == 1 )
        {
            if ( (type >= 0 && type <= 7) || hotelId > 0 )
            {
                query = query + " AND kind_flag=0";
            }
            else if ( (type >= 9 && type <= 12) || hotelId > 0 )
            {
                query = query + " AND kind_flag=0";
            }
            else
            {
                query = query + " WHERE kind_flag=0";
            }
        }
        else if ( kindFlag == 2 )
        {
            if ( (type >= 0 && type <= 7) || hotelId > 0 )
            {
                query = query + " AND kind_flag=1";
            }
            else if ( (type >= 9 && type <= 12) || hotelId > 0 )
            {
                query = query + " AND kind_flag=1";
            }
            else
            {
                query = query + " WHERE kind_flag=1";
            }
        }

        try
        {
            prestate = connection.prepareStatement( query );
            if ( hotelId > 0 )
            {
                prestate.setInt( 1, hotelId );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 総件数の取得
                    this.m_bbsAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUnPostBbsHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 都道府県別新着クチコミ情報取得
     * 
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param prefId 都道府県ID(0:全件)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getOwnerBbsListByPref(int countNum, int pageNum, int prefId)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_bbs.* FROM hh_hotel_bbs,hh_hotel_basic";
        query = query + " WHERE ( hh_hotel_bbs.thread_status = 1 OR hh_hotel_bbs.thread_status = 2)";
        query = query + " AND hh_hotel_bbs.kind_flag=0";
        query = query + " AND hh_hotel_basic.id = hh_hotel_bbs.id";
        if ( prefId != 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = " + prefId;
        }
        query = query + " ORDER BY hh_hotel_bbs.contribute_date DESC, hh_hotel_bbs.contribute_time DESC";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_bbsCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelBbs = new DataHotelBbs[this.m_bbsCount];
                for( i = 0 ; i < m_bbsCount ; i++ )
                {
                    m_hotelBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUnPostBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテルクチコミ総件数の取得
        query = "SELECT COUNT(*) FROM hh_hotel_bbs,hh_hotel_basic";
        query = query + " WHERE (thread_status=1 OR thread_status=2)";
        query = query + " AND kind_flag=0";
        query = query + " AND hh_hotel_basic.id = hh_hotel_bbs.id";
        if ( prefId != 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = " + prefId;
        }

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 総件数の取得
                    this.m_bbsAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUnPostBbsHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテルクチコミ一覧情報取得(日付順)
     * 
     * @param hotelId ホテルID
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param kindFlag 0:すべて1:クチコミのみ2:ご意見ご要望
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getBbsList(int hotelId, int countNum, int pageNum, int kindFlag)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // ホテルが許可したもののみ表示
        query = "SELECT * FROM hh_hotel_bbs WHERE id = ? AND (thread_status=1 OR thread_status=2)";
        if ( kindFlag == 1 )
        {
            query = query + " AND kind_flag=0";
        }
        else if ( kindFlag == 2 )
        {
            query = query + " AND kind_flag=1";
        }

        query = query + " ORDER BY seq DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_bbsCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelBbs = new DataHotelBbs[this.m_bbsCount];
                for( i = 0 ; i < m_bbsCount ; i++ )
                {
                    m_hotelBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // ホテルクチコミ総件数の取得
        query = "SELECT COUNT(*) FROM hh_hotel_bbs WHERE id = ? AND (thread_status=1 OR thread_status=2)";
        if ( kindFlag == 1 )
        {
            query = query + " AND kind_flag=0";
        }
        else if ( kindFlag == 2 )
        {
            query = query + " AND kind_flag=1";
        }

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 総件数の取得
                    this.m_bbsAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテルクチコミ平均点情報取得
     * 
     * @param hotelId ホテルID
     * @return クチコミ平均点(×100) ※エラー時:-1
     */
    public int getPointAverage(int hotelId)
    {
        int avg;
        int point;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // ホテルが許可したもののみカウントする
        query = "SELECT SUM(point*100),COUNT(*) FROM hh_hotel_bbs WHERE id = ? AND (thread_status=1 OR thread_status=2)";
        query = query + " AND kind_flag =0";
        avg = 0;
        point = 0;
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 合計点数と投稿件数の取得
                    point = result.getInt( 1 );
                    count = result.getInt( 2 );
                    // 平均点計算
                    if ( count != 0 )
                    {
                        avg = point / count;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getPointAverage] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(avg);
    }

    /**
     * ホテルクチコミ平均点情報取得
     * 
     * @param hotelId ホテルID
     * @return クチコミ平均点 ※エラー時:-1
     */
    public BigDecimal getPointAverageByDecimal(int hotelId)
    {
        int avg;
        int point;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        BigDecimal decimalAvg;

        // ホテルが許可したもののみカウントする
        query = "SELECT SUM(point*100),COUNT(*) FROM hh_hotel_bbs WHERE id = ? AND (thread_status=1 OR thread_status=2)";
        query = query + " AND kind_flag = 0";

        avg = 0;
        point = 0;
        count = 0;
        decimalAvg = new BigDecimal( 1.2 );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 合計点数と投稿件数の取得
                    point = result.getInt( 1 );
                    count = result.getInt( 2 );
                    // 平均点計算
                    if ( count != 0 )
                    {
                        avg = point / count;
                        decimalAvg = new BigDecimal( Integer.toString( avg ) );
                        decimalAvg = decimalAvg.movePointLeft( 2 );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getPointAverage] Exception=" + e.toString() );
            return(new BigDecimal( "-1" ));
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(decimalAvg);
    }

    /**
     * ホテルクチコミ投稿処理
     * 
     * @param hotelId ホテルID
     * @param userId ユーザID
     * @param point クチコミ評価(0～5)
     * @param cleanness きれいさ評価(0～5)
     * @param width 広さ評価(0～5)
     * @param equip 設備評価(0～5)
     * @param service サービス評価(0～5)
     * @param cost コストパフォーマンス評価(0～5)
     * @param inputData 入力データ
     * @param kindFlag 0:クチコミ投稿、1:ご意見ご要望投稿
     * @param request HTTPリクエスト
     * @return 処理結果(0:正常,1:パラメタ異常,2:DB異常,3:NGワードあり,4:その他)
     */
    public int setBbsData(int hotelId, String userId, int point, int cleanness, int width, int equip, int service, int cost, String inputData, int kindFlag, HttpServletRequest request)
    {
        boolean ret;
        String userName;
        DataHotelBbs dhb;
        DataUserBasic dub;

        // 入力値チェック
        if ( hotelId <= 0 || userId.compareTo( "" ) == 0 || point > HOTELBBS_POINT_MAX || inputData.compareTo( "" ) == 0 )
        {
            return(1);
        }
        // 名前の取得
        dub = new DataUserBasic();
        ret = dub.getData( userId );
        if ( ret != false )
        {
            userName = dub.getHandleName();
        }
        else
        {
            userName = userId;
        }

        // NGワードチェック

        // ユーザ情報を取得する
        // データをセットする
        dhb = new DataHotelBbs();
        dhb.setId( hotelId );
        dhb.setUserId( userId );
        dhb.setUserName( userName );
        dhb.setPoint( point );
        dhb.setMessage( inputData );
        dhb.setReturnOwnerId( "" );
        dhb.setReturnOwnerName( "" );
        dhb.setReturnMessage( "" );
        dhb.setStillFlag( 0 );
        dhb.setThreadStatus( 0 );
        dhb.setOwnerMail( 0 );
        dhb.setUserMail( 0 );
        dhb.setContributeDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dhb.setContributeTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        dhb.setContributeIp( request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr() );
        dhb.setContributeUserAgent( request.getHeader( "user-agent" ) );
        dhb.setReturnDate( 0 );
        dhb.setReturnTime( 0 );
        dhb.setReturnIp( "" );
        dhb.setReturnUserAgent( "" );
        dhb.setCleannessPoint( cleanness );
        dhb.setWidthPoint( width );
        dhb.setEquipPoint( equip );
        dhb.setServicePoint( service );
        dhb.setCostPoint( cost );
        dhb.setKindFlag( kindFlag );
        ret = dhb.insertData();
        if ( ret != false )
        {
            return(0);
        }
        else
        {
            return(2);
        }
    }

    /**
     * ユーザ基本情報取得
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserData(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_basic WHERE user_id = ? ";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    m_userBasic = new DataUserBasic();

                    // ユーザ基本情報の取得
                    this.m_userBasic.setData( result );
                    if ( userId.compareTo( m_userBasic.getUserId() ) == 0 )
                        ret = true;
                    else
                        ret = false;
                }
            }
            else
            {
                ret = false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUserData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * クチコミ投票済みチェック処理
     * 
     * @param id ホテルID
     * @param seq 管理番号
     * @param userId ユーザID
     * @return 処理結果(TRUE:投票不可,FALSE:投票可)
     */
    public boolean checkVote(int id, int seq, String userId)
    {
        boolean ret;
        DataUserBbsVote dubv;

        dubv = new DataUserBbsVote();

        // 対象データが投票済みかチェックする
        ret = dubv.getData( userId, id, seq );

        return(ret);
    }

    /**
     * クチコミ投票処理
     * 
     * @param id ホテルID
     * @param seq 管理番号
     * @param userId ユーザID
     * @param status 投票状況
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateVote(int id, int seq, String userId, int status)
    {
        boolean ret;
        DataHotelBbs dhb;
        DataUserBasic dub;
        DataUserBbsVote dubv;

        dubv = new DataUserBbsVote();
        dub = new DataUserBasic();
        dhb = new DataHotelBbs();

        // ユーザクチコミ投票履歴データの追加
        dubv.setUserId( userId );
        dubv.setHotelId( id );
        dubv.setSeq( seq );
        dubv.setVoteDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dubv.setVoteTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        dubv.setVoteStatus( status );
        ret = dubv.insertData();

        // ホテルクチコミデータの投票数更新
        ret = dhb.getData( id, seq );
        if ( ret != false )
        {
            dhb.setVoteCount( dhb.getVoteCount() + 1 );
            if ( status == 0 )
            {
                dhb.setVoteYes( dhb.getVoteYes() + 1 );
            }
            dhb.updateData( id, seq );

            // クチコミ投稿者のクチコミポイント数（＋評価）加算処理
            ret = dub.getData( dhb.getUserId() );
            if ( ret != false )
            {
                // データが取得されていなくてもtrueで返ってくるため、ユーザデータのチェックを行う
                if ( dub.getUserId().compareTo( dhb.getUserId() ) == 0 )
                {
                    if ( status == 0 )
                    {
                        dub.setBuzzPointPlus( dub.getBuzzPointPlus() + 1 );
                    }
                    else
                    {
                        dub.setBuzzPointMinus( dub.getBuzzPointMinus() + 1 );
                    }
                    ret = dub.updateData( dhb.getUserId() );
                }
            }
        }

        return(true);
    }

    /**
     * クチコミ投稿数取得（指定日付）
     * 
     * @param userId ユーザーID
     * @param today 取得日付
     * @param kindFlag 0:クチコミのみ1:ご意見ご要望
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getTodayBbsList(String userId, int today, int kindFlag)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( userId == null || today < 0 || kindFlag < 0 )
        {
            return(-1);
        }

        // ホテルが許可したもののみ表示
        query = "SELECT COUNT(*) FROM hh_hotel_bbs WHERE user_id = ? ";
        query = query + " AND contribute_date = ?";
        query = query + " AND kind_flag = ?";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, today );
            prestate.setInt( 3, kindFlag );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getBbsList] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * ユーザー別クチコミデータ取得
     * 
     * @param userId ユーザID
     * @param userName ニックネーム
     * @param countNum 取得件数
     * @param pageNum ページ番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getBbsListByUserName(String userId, String userName, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( pageNum < 0 )
        {
            pageNum = 0;
        }

        // ユーザ基本情報を取得する
        this.m_memberFlag = this.getUserData( userId );

        // ニックネームでクチコミのデータを取得する
        query = "SELECT hh_hotel_bbs.* FROM hh_hotel_bbs,hh_hotel_basic"
                + " WHERE hh_hotel_bbs.id = hh_hotel_basic.id"
                + " AND hh_hotel_basic.rank = 2"
                + " AND hh_hotel_bbs.user_id = ?"
                + " AND ( hh_hotel_bbs.user_name = ? OR hh_hotel_bbs.user_name = ? )"
                + " AND hh_hotel_bbs.kind_flag = 0"
                + " AND hh_hotel_bbs.thread_status BETWEEN 1 AND 2"
                + " ORDER BY hh_hotel_bbs.contribute_date DESC, hh_hotel_bbs.contribute_time DESC";
        if ( countNum > 0 )
        {
            query += " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            // クロスサイトスクリプティング対策でエスケープされる文字があるため両方でチェックする
            prestate.setString( 2, ReplaceString.DBEscape( userName ) );
            prestate.setString( 3, ReplaceString.HTMLEscape( userName ) );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_bbsCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_hotelBbs = new DataHotelBbs[this.m_bbsCount];
                for( i = 0 ; i < m_bbsCount ; i++ )
                {
                    m_hotelBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル情報の取得
                    this.m_hotelBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        query = "SELECT COUNT(*) FROM hh_hotel_bbs,hh_hotel_basic"
                + " WHERE hh_hotel_bbs.id = hh_hotel_basic.id"
                + " AND hh_hotel_basic.rank = 2"
                + " AND hh_hotel_bbs.user_id = ?"
                + " AND ( hh_hotel_bbs.user_name = ? OR hh_hotel_bbs.user_name = ? )"
                + " AND hh_hotel_bbs.kind_flag = 0"
                + " AND hh_hotel_bbs.thread_status BETWEEN 1 AND 2"
                + " ORDER BY hh_hotel_bbs.contribute_date DESC, hh_hotel_bbs.contribute_time DESC";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setString( 2, ReplaceString.DBEscape( userName ) );
            prestate.setString( 3, ReplaceString.HTMLEscape( userName ) );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // 総件数の取得
                    this.m_bbsAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }
}
