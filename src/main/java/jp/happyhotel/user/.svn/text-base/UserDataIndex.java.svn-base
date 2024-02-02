/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserDataIndex;

/**
 * ユーザ
 * 
 * 
 * @author S.Tashiro
 * @version 1.00 2011/04/18
 */
public class UserDataIndex implements Serializable
{
    private static final long serialVersionUID = 7778439696083068167L;

    private final int         KIND_HAPPIE      = 1;
    private int               userDataIndexCount;
    private DataUserDataIndex userDataIndex;

    /**
     * データを初期化します。
     */
    public UserDataIndex()
    {
        userDataIndexCount = 0;
    }

    /** ユーザ基本情報件数取得 **/
    public int getCount()
    {
        return(userDataIndexCount);
    }

    /** ユーザ基本情報取得 **/
    public DataUserDataIndex getUserDataIndexInfo()
    {
        return(userDataIndex);
    }

    /**
     * ユーザ採番データ
     * 
     * @param userId
     * @param id
     * @return 処理結果（true、false）
     */
    public boolean getDataUserIndex(String userId, int id)
    {
        boolean ret = false;
        DataUserDataIndex dudi;

        try
        {
            dudi = new DataUserDataIndex();
            ret = dudi.getData( userId, id );
            if ( ret == false )
            {
                ret = this.registUserDataIndex( userId, id );
            }
            else
            {
                userDataIndex = dudi;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserDataIndex.getDataUserIndex()] Exception:" + e.toString() );
        }

        return(ret);
    }

    /**
     * ユーザ採番情報登録処理
     * 
     * @param userId
     * @param id
     * @return
     */
    public boolean registUserDataIndex(String userId, int id)
    {
        int nMaxSeq = 0;
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            // トランザクションの開始
            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            // システム採番データから最大値を取得
            nMaxSeq = this.getMaxSeq( KIND_HAPPIE, id, connection, prestate );
            if ( nMaxSeq == 0 )
            {
                // 新規追加
                ret = insertData( KIND_HAPPIE, id, connection, prestate );
            }
            else
            {
                // 更新
                ret = updateData( KIND_HAPPIE, id, nMaxSeq + 1, connection, prestate );
            }

            // トランザクションの終了（更新成功→COMMIT、更新失敗→ROLLBACK）
            if ( ret != false )
            {
                query = "COMMIT ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // ユーザ管理番号データに追加
                if ( userDataIndex == null )
                {
                    userDataIndex = new DataUserDataIndex();
                }
                userDataIndex.setUserId( userId );
                userDataIndex.setHotelId( id );
                userDataIndex.setUserSeq( nMaxSeq + 1 );
                userDataIndex.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                userDataIndex.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                userDataIndex.insertData();
            }
            else
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[UserDataIndex.registUserDataIndex()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);

    }

    /**
     * システム採番データ取得
     * 
     * @param kind 区分（0：ハピタッチ関連）
     * @param id ホテルID
     * @param conn DBコネクション（ トランザクションをしたコネクションを渡す ）
     * @param prestate ステートメント
     * @return 現在登録されている採番値（0：未登録）
     * @throws Exception
     */
    private int getMaxSeq(int kind, int id, Connection conn, PreparedStatement prestate) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int curSeqNum = 0;

        query = query + "SELECT max_seq FROM hh_system_data_index ";
        query = query + "WHERE kind=? AND id = ? FOR UPDATE";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, id );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() )
                {
                    curSeqNum = result.getInt( "max_seq" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserDataIndex.getMaxSeq()] Exception=" + e.toString() );
            throw new Exception( "[UserDataIndex.getMaxSeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(curSeqNum);
    }

    /***
     * システム採番データの新規作成
     * 
     * @param kind 区分
     * @param id ホテルID
     * @param conn DBコネクション
     * @param prestate ステートメント
     * @return 処理結果（true:成功、false:失敗）
     * @throws Exception
     */
    private static boolean insertData(int kind, int id, Connection conn, PreparedStatement prestate) throws Exception
    {
        String query = "";
        int result = 0;
        boolean ret = false;

        query = query + "INSERT hh_system_data_index SET ";
        query += " kind = ?,";
        query += " id = ?, ";
        query += " max_seq = ?,";
        query += " max_seq_sub1 = ?,";
        query += " max_seq_sub2 = ?,";
        query += " max_seq_sub3 = ?,";
        query += " update_date = ?,";
        query += " update_time = ?";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, id );
            prestate.setInt( 3, 1 );
            prestate.setInt( 4, 0 );
            prestate.setInt( 5, 0 );
            prestate.setInt( 6, 0 );
            prestate.setInt( 7, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 8, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserDataIndex.insertData()] Exception=" + e.toString() );
            throw new Exception( "[UserDataIndex.insertData()] Exception=" + e.toString() );
        }

        return(ret);
    }

    private static boolean updateData(int kind, int id, int maxSeq, Connection conn, PreparedStatement prestate) throws Exception
    {
        String query = "";
        int result = 0;
        boolean ret = false;

        query = "UPDATE hh_system_data_index SET ";
        query += " max_seq = ?,";
        query += " update_date = ?,";
        query += " update_time = ?";
        query += " WHERE kind = ? AND id = ?";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, maxSeq );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 4, kind );
            prestate.setInt( 5, id );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserDataIndex.updateData()] Exception=" + e.toString() );
            throw new Exception( "[UserDataIndex.updateData()] Exception=" + e.toString() );
        }

        return(ret);
    }
}
