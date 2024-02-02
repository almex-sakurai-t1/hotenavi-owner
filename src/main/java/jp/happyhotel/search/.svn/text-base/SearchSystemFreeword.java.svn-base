/*
 * @(#)SearchSystemInfo.java 1.00 2009/01/07 Copyright (C) ALMEX Inc. 2009 検索ワード情報データ取得クラス
 */
package jp.happyhotel.search;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * 検索ワード情報データ取得クラス
 * 
 * @author N.Ide
 * @version 1.00 2009/01/07
 */
public class SearchSystemFreeword implements Serializable
{
    /**
     *
     */
    private static final long    serialVersionUID = -4608809242232034748L;

    private int                  dataCount;
    private int                  maxSearchCount;
    private int                  minSearchCount;
    private DataSystemFreeword[] systemFreeword;

    /**
     * データを初期化します。
     */
    public SearchSystemFreeword()
    {
        dataCount = 0;
        maxSearchCount = 0;
        minSearchCount = 0;
        systemFreeword = new DataSystemFreeword[0];
    }

    public int getDataCount()
    {
        return dataCount;
    }

    public int getMaxSearchCount()
    {
        return maxSearchCount;
    }

    public int getMinSearchCount()
    {
        return minSearchCount;
    }

    public DataSystemFreeword[] getSystemFreeword()
    {
        return systemFreeword;
    }

    /**
     * フリーワード履歴管理データ一覧取得(日付指定、件数指定)
     * 
     * @param date 日付
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param randomFlag ランダムフラグ(TRUE:ランダム順,FALSE:検索回数順)
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataListByDate(int date, int countNum, int pageNum, boolean randomFlag)
    {
        int i;
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String subquery;
        String[] searchWordList;
        int ngwordCount;
        String[] ngwordList;

        ret = false;

        // NGワード一覧の取得
        query = "SELECT ng_word FROM hh_master_ngword";
        subquery = "";
        try
        {
            count = 0;
            ngwordCount = 0;
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        ngwordCount = result.getRow();
                    }

                    // Stringの配列を用意し、初期化する。
                    ngwordList = new String[ngwordCount];
                    for( i = 0 ; i < ngwordCount ; i++ )
                    {
                        ngwordList[i] = new String();
                    }
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // NGワード情報の取得
                        ngwordList[count] = result.getString( "ng_word" );
                        subquery = subquery + " AND freeword <> '" + ngwordList[count] + "'";
                        count++;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemFreeword.getDataListByDate(NGword)] Exception=" + e.toString() );
            return(ret);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // NGワードがはじかれた検索ワード一覧を取得
        query = "SELECT * FROM hh_system_freeword";
        query = query + " WHERE date = ?";
        query = query + " AND freeword != ''";

        if ( subquery.compareTo( "" ) != 0 )
        {
            query = query + subquery;
        }
        query = query + " ORDER BY count DESC";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }
        try
        {
            count = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        dataCount = result.getRow();
                    }

                    // Stringの配列を用意し、初期化する。
                    searchWordList = new String[dataCount];
                    for( i = 0 ; i < dataCount ; i++ )
                    {
                        searchWordList[i] = new String();
                    }
                    result.beforeFirst();
                    subquery = " AND freeword IN (";
                    while( result.next() != false )
                    {
                        // NGワードがはじかれた検索ワード情報の取得
                        searchWordList[count] = result.getString( "freeword" );
                        if ( count < dataCount - 1 )
                        {
                            subquery = subquery + "'" + searchWordList[count] + "',";
                            // 検索回数の最大値の取得
                            if ( count == 0 )
                            {
                                maxSearchCount = result.getInt( "count" );
                            }
                        }
                        else
                        {
                            subquery = subquery + "'" + searchWordList[count] + "')";
                            minSearchCount = result.getInt( "count" );
                        }
                        count++;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemFreeword.getDataListByDate(Keyword)] Exception=" + e.toString() );
            return(ret);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // 検索ワード一覧のデータをランダムで取得
        query = "SELECT * FROM hh_system_freeword";
        query = query + " WHERE date = ?";
        if ( subquery.compareTo( "" ) != 0 )
        {
            query = query + subquery;
        }

        if ( randomFlag != false )
        {
            query = query + " ORDER BY RAND()";
        }
        else
        {
            query = query + " ORDER BY count DESC";
        }

        try
        {
            count = 0;
            dataCount = 0;
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, date );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // レコード件数取得
                    if ( result.last() != false )
                    {
                        dataCount = result.getRow();
                    }

                    // クラスの配列を用意し、初期化する。
                    this.systemFreeword = new DataSystemFreeword[this.dataCount];
                    for( i = 0 ; i < dataCount ; i++ )
                    {
                        systemFreeword[i] = new DataSystemFreeword();
                    }
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ホテル情報の取得
                        this.systemFreeword[count].setData( result );
                        count++;
                    }

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchSystemFreeword.getDataListByDate] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
