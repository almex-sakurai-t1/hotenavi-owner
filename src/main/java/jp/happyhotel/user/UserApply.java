/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
 */

package jp.happyhotel.user;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * 当選ユーザ取得クラス。 ユーザ応募状況のデータを取得する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/26
 */
public class UserApply implements Serializable
{
    private static final long serialVersionUID = -7658827367902506182L;

    private int               UserApplyCount;
    private DataUserApply[]   m_UserApply;

    /**
     * データを初期化します。
     */
    public UserApply()
    {
        UserApplyCount = 0;
    }

    /** ユーザ応募情報件数取得 **/
    public int getCount()
    {
        return(UserApplyCount);
    }

    /** ユーザ応募情報取得 **/
    public DataUserApply[] getUserApply()
    {
        return(m_UserApply);
    }

    /**
     * 応募情報を取得する（IDから）
     * 
     * @param userId カテゴリ
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserApply(String userId, int formId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        if ( formId < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_apply";
        query = query + " WHERE user_id = ?";
        query = query + " AND form_id = ?";
        query = query + " ORDER BY form_id DESC, input_date DESC, input_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, formId );

            ret = getUserApplySub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserApply] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 応募情報を取得する（端末IDから）
     * 
     * @param termNo カテゴリ
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserApplyByTermNo(String termNo, int formId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( termNo == null )
        {
            return(false);
        }
        if ( formId < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_apply";
        query = query + " WHERE term_no = ?";
        query = query + " AND form_id = ?";
        query = query + " ORDER BY form_id DESC, input_date DESC, input_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, termNo );
            prestate.setInt( 2, formId );

            ret = getUserApplySub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserApply] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 応募情報を取得する（ID,有効期限から）
     * 
     * @param userId カテゴリ
     * @param form_id 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getUserApply(String userId, int startDate, int endDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        if ( startDate < 0 || endDate < 0 )
        {
            return(false);
        }
        query = "SELECT * FROM hh_user_apply";
        query = query + " WHERE user_id = ?";
        query = query + " AND input_date >= ?";
        query = query + " AND input_date <= ?";
        query = query + " AND status_flag >= 3";
        query = query + " ORDER BY input_date DESC, input_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, startDate );
            prestate.setInt( 3, endDate );

            ret = getUserApplySub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserApply] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 応募情報を取得する
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getUserApplySub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        UserApplyCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    UserApplyCount = result.getRow();
                }
                this.m_UserApply = new DataUserApply[this.UserApplyCount];
                for( i = 0 ; i < UserApplyCount ; i++ )
                {
                    m_UserApply[i] = new DataUserApply();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 当選データ情報の設定
                    this.m_UserApply[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserApplySub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( UserApplyCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 当選者データを取得する
     * 
     * @param form_id 管理番号
     * @return 処理結果(-1:取得エラー)
     */
    public boolean getUserApplyWinner(int form_id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( form_id < 0 )
            return(false);

        query = "SELECT * FROM hh_user_elect";
        query = query + " WHERE form_id = ?";
        query = query + " AND status_flag = 5";
        query = query + " GROUP BY user_id";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, form_id );

            ret = getUserApplySub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserApplyWinner] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        if ( ret == false )
        {
            this.UserApplyCount = 0;
        }
        return(ret);
    }
}
