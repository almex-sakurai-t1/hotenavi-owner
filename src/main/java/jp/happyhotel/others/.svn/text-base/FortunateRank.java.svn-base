/* @(#)FortunateRank.java  1.00 2008/12/02
 *
 * Copyright (C) ALMEX Inc. 2007
 *
 * ユーザ基本情報取得クラス
 */

package jp.happyhotel.others;

import java.io.*;
import java.sql.*;

import jp.happyhotel.data.*;
import jp.happyhotel.common.*;

/**
 * 占いランキング取得クラス。
 *   占いのランキング情報を取得する機能を提供する
 * @author  S.Tashiro
 * @version 1.00 2008/04/22
 */
public class FortunateRank implements Serializable
{
    private static final long serialVersionUID = 2647659960821498268L;

    private int                   masterCount;
    private DataFortunateRank[]   m_FortunateRank;
    private DataMasterFortune[]   m_Fortune;
    private DataMasterFortune[]   m_FortuneItem;
    private DataMasterFortune[]   m_FortuneHotel;
    private final int            Fortune = 0;
    private final int            FortuneItem = 1;
    private final int            FortuneHotel = 2;

    /**
     * データを初期化します。
     */
    public FortunateRank( )
    {
        masterCount = 0;
    }

    /** 占いランク情報件数取得 **/
    public int getCount( ) { return( masterCount ); }
    /** 占いランク情報取得 **/
    public DataFortunateRank[] getFortunateRankInfo( ) { return( m_FortunateRank ); }
    /** 占いマスタ情報取得（星座） **/
    public DataMasterFortune[] getMasterFortuneInfo() { return( m_Fortune ); }
    /** 占いマスタ情報取得（ラッキーアイテム） **/
    public DataMasterFortune[] getMasterFortuneItemInfo() { return( m_FortuneItem ); }
    /** 占いマスタ情報取得（ラッキーホテル） **/
    public DataMasterFortune[] getMasterFortuneHotelInfo() { return( m_FortuneHotel ); }



    /**
     * 占いデータを取得する
     *
     * @param today 今日の日付
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getFortunateRank( int today )
    {
        boolean              ret;
        String               query;
        Connection connection = null;
        PreparedStatement    prestate = null;

        if( today < 0 )
        {
            return( false );
        }
        query = "SELECT * FROM hh_fortunate_rank";
        query = query + " WHERE disp_date = ?";
        query = query + " ORDER BY disp_date, disp_rank ";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setInt( 1, today );
            ret = geFortuneSub(prestate );
        }
        catch( Exception e )
        {
            Logging.info( "[getFortunateRank] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }

        return( ret );
    }


    /**
     * プレゼント情報のデータをセット
     *
     * @param prestate プリペアドステートメント
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean geFortuneSub( PreparedStatement prestate )
    {
        ResultSet    result = null;
        int          count;
        int          i;

        i     = 0;
        count = 0;
        try
        {
            result = prestate.executeQuery();
            if( result != null )
            {
                if( result.last() != false )
                {
                    masterCount = result.getRow();
                }
                this.m_FortunateRank = new DataFortunateRank[ this.masterCount ];
                this.m_Fortune = new DataMasterFortune[ this.masterCount ];
                this.m_FortuneItem = new DataMasterFortune[ this.masterCount ];
                this.m_FortuneHotel = new DataMasterFortune[ this.masterCount ];

                for( i = 0; i < masterCount; i++ )
                {
                    this.m_FortunateRank[ i ] = new DataFortunateRank();
                    this.m_Fortune[ i ] = new DataMasterFortune();
                    this.m_FortuneItem[ i ] = new DataMasterFortune();
                    this.m_FortuneHotel[ i ] = new DataMasterFortune();

                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // 占いランキング情報の設定
                    this.m_FortunateRank[ count ].setData( result );
                    // 占いマスタ(星座)情報の設定
                    this.m_Fortune[ count ].getData( Fortune, result.getInt("seq") );
                    // 占いマスタ(ラッキーアイテム)情報の設定
                    this.m_FortuneItem[ count ].getData( FortuneItem, result.getInt("item_seq") );
                    // 占いマスタ(ラッキーアイテム)情報の設定
                    this.m_FortuneHotel[ count ].getData( FortuneHotel, result.getInt("hotel_seq") );

                    count++;
                }
            }
        }
        catch( Exception e )
        {
            Logging.info( "[geFortuneSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources(result);
        }

        if( masterCount != 0 )
        {
            return( true );
        }
        else
        {
            return( false );
        }
    }

}
