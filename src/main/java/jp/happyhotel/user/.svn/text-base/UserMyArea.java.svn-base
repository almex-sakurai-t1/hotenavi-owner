/*
 * @(#)UserMyArea.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザマイエリア取得・更新クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserMyArea;

/**
 * ユーザマイエリア取得・更新クラス。 ユーザのマイエリア情報を取得・更新する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/27
 */
public class UserMyArea implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -5297595393094543241L;

    private int               userMyAreaCount;
    private DataUserMyArea[]  userMyArea;

    /**
     * データを初期化します。
     */
    public UserMyArea()
    {
        userMyAreaCount = 0;
    }

    /** ユーザマイエリア情報件数取得 **/
    public int getCount()
    {
        return(userMyAreaCount);
    }

    /** ユーザマイエリア情報取得 **/
    public DataUserMyArea[] getMyArea()
    {
        return(userMyArea);
    }

    /**
     * ユーザマイホテルを取得する（IDから）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyAreaList(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_myarea";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
        }
        else
        {
            return(false);
        }
        query = query + " AND del_flag <> 1";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getMyAreaListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserMyArea.getMyAreaList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);

    }

    /**
     * ユーザマイエリアを取得する（端末番号から）
     * 
     * @param mobileTermno 端末番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyAreaListByTermno(String mobileTermno)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_myarea.* FROM hh_user_myarea,hh_user_basic";

        if ( mobileTermno.compareTo( "" ) != 0 )
        {
            query = query + " WHERE hh_user_basic.mobile_termno = ?";
            query = query + " AND hh_user_basic.regist_status = 9";
            query = query + " AND hh_user_basic.del_flag = 0";
            query = query + " AND hh_user_basic.user_id = hh_user_myarea.user_id";
            query = query + " AND hh_user_myarea.del_flag <> 1";
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

            ret = getMyAreaListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserMyArea.getMyAreaListByTermno] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ユーザマイホテルを取得する（IDから）（削除済みも含む）
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMyAreaListAll(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_myarea";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
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

            ret = getMyAreaListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserMyArea.getMyAreaListAll] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ユーザマイエリアのデータをセット
     * 
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMyAreaListSub(PreparedStatement prestate)
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
                    userMyAreaCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.userMyArea = new DataUserMyArea[this.userMyAreaCount];
                for( i = 0 ; i < userMyAreaCount ; i++ )
                {
                    userMyArea[i] = new DataUserMyArea();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ユーザ情報の取得
                    this.userMyArea[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserMyArea.getMyAreaListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userMyAreaCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ユーザマイエリア既存チェック処理
     * 
     * @param userId ユーザID
     * @param areaKind エリア区分
     * @param jisCode 市区町村コード
     * @param areaId エリアID
     * @return 処理結果(false:未登録,true:登録済み)
     */
    public boolean getMyAreaCheck(String userId, int areaKind, int jisCode, int areaId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_myarea WHERE user_id = ?";
        query = query + " AND area_kind = ?";
        query = query + " AND jis_code = ?";
        query = query + " AND area_id = ?";
        query = query + " AND del_flag <> 1";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setString( 1, userId );
                prestate.setInt( 2, areaKind );
                prestate.setInt( 3, jisCode );
                prestate.setInt( 4, areaId );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserMyArea.getMyAreaCheck] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザーマイエリア更新処理
     * 
     * @param userId
     * @param seq
     * @param pushFlag
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updataMyAreaData(String userId, int seq, int pushFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        int result = 0;
        PreparedStatement prestate = null;

        query = "UPDATE hh_user_myarea SET";
        query += " push_flag = ?";
        query += " WHERE user_id = ? AND seq = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pushFlag );
                prestate.setString( 2, userId );
                prestate.setInt( 3, seq );
                result = prestate.executeUpdate();
                if ( result > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserMyArea.updataMyAreaData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
