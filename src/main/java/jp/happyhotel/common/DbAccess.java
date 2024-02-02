/*
 * @(#)DbAccess.java 1.00 2007/07/13 Copyright (C) ALMEX Inc. 2007 データベースアクセスクラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * <p>
 * データベースへの接続、処理結果の取得を行います。 設定ファイルは/etc/happyhotel/dbconnect.confに設定する。<br>
 * jdbc.datasource=java:comp/env/jdbc/HAPPYHOTEL
 * </p>
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/13
 */
public class DbAccess implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 239312761897519395L;

    private int               result;
    private ResultSet         resultset        = null;
    private Connection        connect          = null;
    private Statement         state;
    private LogLib            log;
    private PreparedStatement prestate         = null;

    /**
     * データベースへのアクセス環境設定
     * 
     */
    public DbAccess()
    {
        log = new LogLib();

        try
        {
        }
        catch ( Exception e )
        {
            log.error( "DbAccess Error(0):" + e.toString() );
        }
    }

    /**
     * データベースへのアクセス環境設定
     * 
     * @param filename 設定ファイル名
     */
    public DbAccess(String filename)
    {
        try
        {
        }
        catch ( Exception e )
        {
            log.error( "DbAccess Error(1):" + e.toString() );
        }
    }

    /**
     * プリペアドステートメント作成処理
     * 
     * @param query SQL文
     * @return 処理結果(失敗:null)
     */
    public PreparedStatement createPrepared(String query)
    {
        try
        {
            // DBへ接続する
            connect = DBConnection.getConnection();
            // プリペアドステートメントの作成
            prestate = connect.prepareStatement( query );
        }
        catch ( Exception e )
        {
            log.error( "createPrepared:" + e.toString() );
            resultset = null;
        }

        return(prestate);
    }

    /**
     * SQL文発行処理(SELECT)
     * 
     * @param query SQL文
     * @return 処理結果(失敗:null)
     */
    public ResultSet execQuery(String query)
    {
        try
        {
            // DBへ接続する
            connect = DBConnection.getConnection();
            // ステートメントの作成
            prestate = connect.prepareStatement( query );

            // SQLの実行
            resultset = prestate.executeQuery();
        }
        catch ( Exception e )
        {
            log.error( "execQuery:" + e.toString() );
            resultset = null;
        }

        return(resultset);
    }

    /**
     * SQL文発行処理(SELECT)
     * 
     * @param query SQL文
     * @param object パラメータ
     * @return 処理結果(失敗:null)
     */
    @SuppressWarnings("unchecked")
    public ResultSet execQuery(String query, Object object)
    {
        try
        {
            // DBへ接続する
            connect = DBConnection.getConnection();
            // ステートメントの作成
            prestate = connect.prepareStatement( query );

            String type = getType( object );

            if ( type.indexOf( "Integer" ) != -1 )
            {
                prestate.setInt( 1, (Integer)object );
            }
            else if ( type.indexOf( "String" ) != -1 )
            {
                prestate.setString( 1, (String)object );
            }
            else if ( type.indexOf( "ArrayList" ) != -1 )
            {
                prestate = setPrestate( prestate, (ArrayList<Object>)object );
            }
            // SQLの実行
            resultset = prestate.executeQuery();
        }
        catch ( Exception e )
        {
            log.error( "execQuery:" + e.toString() );
            resultset = null;
        }

        return(resultset);
    }

    /**
     * SQL文発行処理(SELECT)
     * 
     * @param prestate
     * @param list パラメータリスト
     * @return 処理結果(失敗:null)
     */
    public PreparedStatement setPrestate(PreparedStatement prestate, ArrayList<Object> list)
    {
        int size = list.size();
        try
        {
            if ( size > 0 )
            {
                for( int a = 0 ; a < list.size() ; a++ )
                {
                    String type = getType( list.get( a ) );
                    if ( type.indexOf( "Integer" ) != -1 )
                    {
                        prestate.setInt( a + 1, (Integer)list.get( a ) );
                    }
                    else if ( type.indexOf( "String" ) != -1 )
                    {
                        prestate.setString( a + 1, (String)list.get( a ) );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            log.error( "setPrestate:" + e.toString() );
            resultset = null;
        }
        return prestate;
    }

    /**
     * プリペアドステートメント実行処理(SELECT)
     * 
     * @param state SQL文
     * @return 処理結果(失敗:null)
     */
    public ResultSet execQuery(PreparedStatement state)
    {
        try
        {
            // SQLの実行
            resultset = state.executeQuery();
        }
        catch ( Exception e )
        {
            log.error( "execPreparedQuery:" + e.toString() );
            resultset = null;
        }

        return(resultset);
    }

    /**
     * SQL文発行処理(INSERT,UPDATE,DELETE)
     * 
     * @param query SQL文
     * @return 処理結果(失敗:-1)
     */
    public int execUpdate(String query)
    {
        try
        {
            // DBへ接続する
            connect = DBConnection.getConnection();
            // ステートメントの作成
            prestate = connect.prepareStatement( query );

            // SQLの実行
            result = prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            log.error( "execUpdate:" + e.toString() );
            result = -1;
        }

        return(result);
    }

    /**
     * SQL文発行処理(INSERT,UPDATE,DELETE)
     * 
     * @param query SQL文
     * @param object パラメータ
     * @return 処理結果(失敗:-1)
     */
    @SuppressWarnings("unchecked")
    public int execUpdate(String query, Object object)
    {
        try
        {
            // DBへ接続する
            connect = DBConnection.getConnection();
            // ステートメントの作成
            prestate = connect.prepareStatement( query );

            String type = getType( object );

            if ( type.indexOf( "Integer" ) != -1 )
            {
                prestate.setInt( 1, (Integer)object );
            }
            else if ( type.indexOf( "String" ) != -1 )
            {
                prestate.setString( 1, (String)object );
            }
            else if ( type.indexOf( "ArrayList" ) != -1 )
            {
                prestate = setPrestate( prestate, (ArrayList<Object>)object );
            }
            // SQLの実行
            result = prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            log.error( "execUpdate:" + e.toString() );
            result = -1;
        }

        return(result);
    }

    /**
     * プリペアドステートメント実行処理(INSERT,UPDATE,DELETE)
     * 
     * @param state SQL文
     * @return 処理結果(失敗:-1)
     */
    public int execUpdate(PreparedStatement state)
    {
        try
        {
            // SQLの実行
            result = state.executeUpdate();
        }
        catch ( Exception e )
        {
            log.error( "execUpdate:" + e.toString() );
            result = -1;
        }

        return(result);
    }

    /**
     * データベース終了処理
     * 
     */
    public void close()
    {
        try
        {
            DBConnection.releaseResources( resultset );
            DBConnection.releaseResources( state );
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connect );
        }
        catch ( Exception e )
        {
            log.error( "close:" + e.toString() );
        }
    }

    public String getType(Object o)
    {
        String param = o.getClass().toString();
        return param;
    }
}
