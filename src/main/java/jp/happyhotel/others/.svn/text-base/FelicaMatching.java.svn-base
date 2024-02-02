/*
 * @(#)FelicaMatching.java 1.00 2009/08/02
 * Copyright (C) ALMEX Inc. 2012
 * フェリカ紐付けクラス
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.RandomNumber;
import jp.happyhotel.data.DataSystemFelicaMatching;

;

/**
 * フェリカ紐付けデータ
 * フェリカIDを紐付けするためのキー情報を管理する
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/12
 */
public class FelicaMatching implements Serializable
{
    private static final long        serialVersionUID = -5551234346012294101L;
    private int                      idmCount;
    private DataSystemFelicaMatching felicaData;

    /**
     * データを初期化します。
     */
    public FelicaMatching()
    {
        idmCount = 0;
    }

    /** ユーザ基本情報件数取得 **/
    public int getCount()
    {
        return(idmCount);
    }

    /** ユーザ基本情報取得 **/
    public DataSystemFelicaMatching getFelicaDataInfo()
    {
        return(felicaData);
    }

    /**
     * IDMに紐付くデータを取得するデータ
     * 
     * @param
     * @return
     */
    public boolean getIdm(String idm)
    {
        boolean result;
        result = false;
        try
        {
            // nullチェック
            if ( (idm == null) || (idm.equals( "" ) != false) || (idm.length() < 16) )
            {
                return result;
            }

            // IDMからデータを取得する
            felicaData = this.getUnregistIdm( idm );

            // IDMデータがない場合はアクセスキーを発行する
            if ( felicaData == null )
            {
                result = this.registFelica( idm );
            }
            else
            {
                felicaData.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                felicaData.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                felicaData.updateData( felicaData.getKey1(), felicaData.getKey2() );
                result = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[FelicaMatching.getIdm()] Exceptioon:" + e.toString() );
        }
        return(result);
    }

    /**
     * ユーザ採番情報登録処理
     * 
     * @param idm
     * @return
     */
    public DataSystemFelicaMatching getUnregistIdm(String idm)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataSystemFelicaMatching dsfm = new DataSystemFelicaMatching();

        query = "SELECT * FROM hh_system_felica_matching WHERE idm = ? and regist_status = 0";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, idm );
            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.next() != false )
                {
                    dsfm.setData( result );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[FelicaMatching.checkIdm()] Exception=" + e.toString() );
            dsfm = null;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        // 1件もなかったらnullを返す
        if ( count <= 0 )
        {
            dsfm = null;
        }
        return(dsfm);

    }

    /**
     * フェリカ登録処理
     * 
     * @param userId
     * @param id
     * @return
     */
    public boolean registFelica(String idm)
    {
        int i = 0;
        String key1 = "";
        String key2 = "";
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataSystemFelicaMatching dsfm = new DataSystemFelicaMatching();
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        try
        {
            // 重複した場合に備えて10回ほど作成する
            for( i = 0 ; i < 10 ; i++ )
            {
                key1 = String.format( "%04d", RandomNumber.getRandomNumber( 4 ) );
                key2 = String.format( "%04d", RandomNumber.getRandomNumber( 4 ) );

                // データを取得する
                ret = dsfm.getData( key1, key2 );
                if ( ret == false )
                {
                    break;
                }
            }

            // トランザクションの開始
            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            // フェリカデータの作成
            if ( ret == false )
            {
                dsfm.setKey1( key1 );
                dsfm.setKey2( key2 );
                dsfm.setIdm( idm );
                dsfm.setRegistStatus( 0 );
                dsfm.setRegistDate( nowDate );
                dsfm.setRegistTime( nowTime );
                dsfm.setLastUpdate( nowDate );
                dsfm.setLastUptime( nowTime );
                ret = dsfm.insertData();
            }

            // トランザクションの終了（更新成功→COMMIT、更新失敗→ROLLBACK）
            if ( ret != false )
            {
                felicaData = dsfm;
                query = "COMMIT ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
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
}
