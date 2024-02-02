/*
 * @(#)SearchHotelFreeWord.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 フリーワード検索ホテル取得クラス
 */
package jp.happyhotel.search;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * フリーワード検索ホテル取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/20
 */
public class SearchHotelFreeWord implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -5524226612470789020L;

    private int               m_hotelCount;
    private int               m_hotelAllCount;
    private int[]             m_hotelIdList;
    private DataHotelBasic[]  m_hotelInfo;
    private DataMasterLocal[] m_masterLocal;
    private int               m_masterLocalCount;
    private int[]             m_masterLocalHotelCount;
    private DataMasterPref[]  m_masterPref;
    private int               m_masterPrefCount;
    private int[]             m_masterPrefHotelCount;

    /**
     * 詳細表示可能最大数
     */
    public static final int   DETAILDISP_MAX   = 50;

    /**
     * データを初期化します。
     */
    public SearchHotelFreeWord()
    {
        m_hotelCount = 0;
        m_hotelAllCount = 0;
    }

    /**
     * ホテル基本情報件数取得
     */
    public int getCount()
    {
        return(m_hotelCount);
    }

    /**
     * ホテル基本情報件数取得
     */
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    public DataHotelBasic[] getHotelInfo()
    {
        return(m_hotelInfo);
    }

    public DataMasterLocal[] getMasterLocal()
    {
        return(m_masterLocal);
    }

    public int getMasterLocalCount()
    {
        return(m_masterLocalCount);
    }

    public int[] getMasterLocalHotelCount()
    {
        return(m_masterLocalHotelCount);
    }

    public DataMasterPref[] getMasterPref()
    {
        return(m_masterPref);
    }

    public int getMasterPrefCount()
    {
        return(m_masterPrefCount);
    }

    public int[] getMasterPrefHotelCount()
    {
        return(m_masterPrefHotelCount);
    }

    /**
     * ホテル件数情報取得
     * 
     * @param freeWord 検索ワード
     * @return ホテル件数
     */
    public int getHotelCount(String freeWord)
    {
        int[] hotelBasicList;
        String[] cutWord;

        // 検索ワードを分割する
        cutWord = cutSearchWord( freeWord );

        // ホテルのIDリスト取得
        hotelBasicList = getHotelBasicIdList( cutWord );

        m_hotelIdList = hotelBasicList;

        return(hotelBasicList.length);
    }

    /**
     * ホテル一覧情報取得(ホテルランク順)
     * 
     * @param freeWord 検索ワード
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelList(String freeWord, int countNum, int pageNum)
    {
        int i;
        int count;
        int[] hotelBasicList;
        String[] cutWord;

        // 検索ワードを分割する
        cutWord = cutSearchWord( freeWord );

        // ホテルのIDリスト取得
        hotelBasicList = getHotelBasicIdList( cutWord );

        m_hotelAllCount = hotelBasicList.length;
        m_hotelIdList = hotelBasicList;

        count = 0;

        // クラスの配列を用意し、初期化する。
        if ( countNum == 0 )
        {
            this.m_hotelInfo = new DataHotelBasic[m_hotelAllCount];
            for( i = 0 ; i < m_hotelAllCount ; i++ )
            {
                m_hotelInfo[i] = new DataHotelBasic();
            }
        }
        else
        {
            this.m_hotelInfo = new DataHotelBasic[countNum];
            for( i = 0 ; i < countNum ; i++ )
            {
                m_hotelInfo[i] = new DataHotelBasic();
            }
        }

        // ホテル基本情報取得
        for( i = countNum * pageNum ; i < m_hotelAllCount ; i++ )
        {
            m_hotelInfo[count++].getData( hotelBasicList[i] );
            if ( count >= countNum && countNum != 0 )
            {
                Logging.error( "[getHotelBasicIdList] count=" + count );
                Logging.error( "[getHotelBasicIdList] countNum=" + countNum );
                break;
            }
        }
        m_hotelCount = count;

        return(true);
    }

    /**
     * 都道府県別ホテル一覧情報取得(ホテルランク順)
     * 
     * @param freeWord 検索ワード
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelListByPref(String freeWord, int prefId, int countNum, int pageNum)
    {
        int i;
        int count;
        int[] hotelBasicList;
        String[] cutWord;

        // 検索ワードを分割する
        cutWord = cutSearchWord( freeWord );

        // ホテルのIDリスト取得
        hotelBasicList = getHotelBasicIdListByPref( cutWord, prefId );

        m_hotelAllCount = hotelBasicList.length;
        m_hotelIdList = hotelBasicList;

        count = 0;

        // クラスの配列を用意し、初期化する。
        if ( countNum == 0 )
        {
            this.m_hotelInfo = new DataHotelBasic[m_hotelAllCount];
            for( i = 0 ; i < m_hotelAllCount ; i++ )
            {
                m_hotelInfo[i] = new DataHotelBasic();
            }
        }
        else
        {
            this.m_hotelInfo = new DataHotelBasic[countNum];
            for( i = 0 ; i < countNum ; i++ )
            {
                m_hotelInfo[i] = new DataHotelBasic();
            }
        }

        // DETAILDISP_MAX以下の場合はホテル基本情報取得
        for( i = countNum * pageNum ; i < m_hotelAllCount ; i++ )
        {
            m_hotelInfo[count++].getData( hotelBasicList[i] );
            if ( count >= countNum && countNum != 0 )
            {
                break;
            }
        }
        m_hotelCount = count;

        return(true);
    }

    /**
     * 地域別ホテル一覧情報取得
     * 
     * @param freeWord 検索ワード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelCountByLocal(String freeWord)
    {
        boolean ret;
        String[] cutWord;

        // 検索ワードを分割する
        cutWord = cutSearchWord( freeWord );

        // 一覧を取得する
        ret = getHotelBasicCountByLocal( cutWord );

        return(ret);
    }

    /**
     * 地域別ホテル一覧情報取得
     * 
     * @param freeWord 検索ワード
     * @param localId 地域ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelCountByPref(String freeWord, int localId)
    {
        boolean ret;
        String[] cutWord;

        // 検索ワードを分割する
        cutWord = cutSearchWord( freeWord );

        // 一覧を取得する
        ret = getHotelBasicCountByPref( cutWord, localId );

        return(ret);
    }

    /**
     * ホテル基本情報検索結果一覧取得
     * 
     * @param cutWord 検索キーワード
     * @return ホテルIDリスト(null:失敗)
     */
    private int[] getHotelBasicIdList(String[] cutWord)
    {
        int i;
        int count;
        int[][] idList;
        int[] idListResult;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        idList = new int[cutWord.length][0];
        try
        {
            connection = DBConnection.getConnection();

            // 検索ワードクエリーの結合
            for( i = 0 ; i < cutWord.length ; i++ )
            {
                count = 0;

                query = "SELECT hh_hotel_search.id FROM hh_hotel_search,hh_hotel_basic,hh_hotel_pv";
                query = query + " WHERE hh_hotel_search.id <> 0";
                query = query + " AND hh_hotel_search.word LIKE ?";
                query = query + " AND hh_hotel_search.id=hh_hotel_basic.id";
                query = query + " AND hh_hotel_basic.kind <= 7";
                query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
                query = query + " AND hh_hotel_pv.collect_date = 0";
                query = query + " GROUP BY hh_hotel_basic.id";
                query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_pv.total_uu_pv DESC, hh_hotel_basic.name_kana";

                try
                {
                    prestate = connection.prepareStatement( query );
                    prestate.setString( 1, "%" + cutWord[i] + "%" );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        // レコード件数取得
                        if ( result.last() != false )
                        {
                            idList[i] = new int[result.getRow()];
                        }

                        result.beforeFirst();
                        while( result.next() != false )
                        {
                            // ホテル情報の取得
                            idList[i][count++] = result.getInt( "hh_hotel_search.id" );
                        }
                    }
                    if ( count > 0 )
                    {
                        // 検索ワードで一致するものがあった場合
                        freewordCountUpdate( cutWord[i] );
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[getHotelBasicIdList] Exception=" + e.toString() );
                }
                finally
                {
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelBasicIdList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }

        idListResult = new int[0];

        // 複数条件指定の場合、全てに一致するもののみ抽出
        if ( cutWord.length > 1 )
        {
            idListResult = getMatchList( idList );
        }
        else
        {
            idListResult = idList[0];
        }
        return(idListResult);
    }

    /**
     * ホテル基本情報検索結果一覧取得
     * 
     * @param cutWord 検索キーワード
     * @param prefId 都道府県ID
     * @return ホテルIDリスト(null:失敗)
     */
    private int[] getHotelBasicIdListByPref(String[] cutWord, int prefId)
    {
        int i;
        int count;
        int[][] idList;
        int[] idListResult;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        idList = new int[cutWord.length][0];

        try
        {
            connection = DBConnection.getConnection();

            // 検索ワードクエリーの結合
            for( i = 0 ; i < cutWord.length ; i++ )
            {
                count = 0;

                query = "SELECT hh_hotel_search.id FROM hh_hotel_search,hh_hotel_basic,hh_hotel_pv";
                query = query + " WHERE hh_hotel_search.id <> 0";
                query = query + " AND hh_hotel_search.word LIKE ?";
                query = query + " AND hh_hotel_basic.pref_id = ?";
                query = query + " AND hh_hotel_search.id=hh_hotel_basic.id";
                query = query + " AND hh_hotel_basic.kind <= 7";
                query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
                query = query + " AND hh_hotel_pv.collect_date = 0";
                query = query + " GROUP BY hh_hotel_basic.id";
                query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_pv.total_uu_pv DESC, hh_hotel_basic.name_kana";
                try
                {
                    prestate = connection.prepareStatement( query );
                    prestate.setString( 1, "%" + cutWord[i] + "%" );
                    prestate.setInt( 2, prefId );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        // レコード件数取得
                        if ( result.last() != false )
                        {
                            idList[i] = new int[result.getRow()];
                        }

                        result.beforeFirst();
                        while( result.next() != false )
                        {
                            // ホテル情報の取得
                            idList[i][count++] = result.getInt( "hh_hotel_search.id" );
                        }
                    }
                    if ( count > 0 )
                    {
                        // 検索ワードで一致するものがあった場合
                        freewordCountUpdate( cutWord[i] );
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[getHotelBasicIdList] Exception=" + e.toString() );
                }
                finally
                {
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelBasicIdList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }

        count = 0;
        idListResult = new int[0];

        // 複数条件指定の場合、全てに一致するもののみ抽出
        if ( cutWord.length > 1 )
        {
            idListResult = getMatchList( idList );
        }
        else
        {
            idListResult = idList[0];
        }

        return(idListResult);
    }

    /**
     * 地方の一覧を取得する（検索ワード別）
     * 
     * @param freeWord 検索ワード
     * @param localId 地域ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getHotelBasicCountByLocal(String[] cutWord)
    {
        int i;
        int count;
        int[][] idList;
        int[] idListResult;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        SearchEngineBasic seb;

        ret = false;

        // 地方一覧取得
        seb = new SearchEngineBasic();
        seb.getLocalList( 0, 1 );

        // ホテル件数配列を用意し、初期化する。
        this.m_masterLocal = seb.getMasterLocal();
        this.m_masterLocalCount = seb.getMasterLocalCount();
        this.m_masterLocalHotelCount = new int[this.m_masterLocalCount];
        idList = new int[cutWord.length][0];
        try
        {
            connection = DBConnection.getConnection();

            for( i = 0 ; i < cutWord.length ; i++ )
            {
                // 候補の取得
                query = "SELECT hh_hotel_basic.id FROM hh_hotel_search,hh_hotel_basic,hh_master_pref";
                query = query + " WHERE hh_hotel_search.id <> 0";
                query = query + " AND hh_hotel_search.word LIKE ?";
                query = query + " AND hh_hotel_search.id=hh_hotel_basic.id";
                query = query + " AND hh_hotel_basic.pref_id=hh_master_pref.pref_id";
                query = query + " GROUP BY hh_hotel_basic.id";

                count = 0;

                try
                {
                    prestate = connection.prepareStatement( query );
                    prestate.setString( 1, "%" + cutWord[i] + "%" );
                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        // レコード件数取得
                        if ( result.last() != false )
                        {
                            idList[i] = new int[result.getRow()];
                        }

                        result.beforeFirst();
                        while( result.next() != false )
                        {
                            idList[i][count++] = result.getInt( "hh_hotel_basic.id" );
                        }
                    }

                    if ( count > 0 )
                    {
                        // 検索ワードで一致するものがあった場合
                        freewordCountUpdate( cutWord[i] );
                    }

                    ret = true;
                }
                catch ( Exception e )
                {
                    Logging.info( "[getHotelBasicCountByLocal] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    result = null;
                    prestate = null;
                }
            }

            idListResult = new int[0];

            // 複数条件指定の場合、全てに一致するもののみ抽出
            if ( cutWord.length > 1 )
            {
                idListResult = getMatchList( idList );
            }
            else
            {
                idListResult = idList[0];
            }

            // 候補を絞った後、地方別に振り分け
            query = "SELECT hh_master_pref.local_id,COUNT(*) FROM hh_hotel_basic,hh_master_pref";
            query = query + " WHERE hh_hotel_basic.id IN(";
            for( i = 0 ; i < idListResult.length ; i++ )
            {
                query = query + idListResult[i];
                if ( i < idListResult.length - 1 )
                {
                    query = query + ",";
                }
            }
            query = query + " )";
            query = query + " AND hh_master_pref.pref_id=hh_hotel_basic.pref_id";
            query = query + " GROUP BY hh_master_pref.local_id";

            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                count = 0;

                result.beforeFirst();
                while( result.next() != false )
                {
                    for( i = 0 ; i < m_masterLocalCount ; i++ )
                    {
                        if ( m_masterLocal[i].getLocalId() == result.getInt( "local_id" ) )
                        {
                            this.m_masterLocalHotelCount[i] = result.getInt( 2 );
                            break;
                        }
                    }
                }
            }

            ret = true;
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelBasicCountByLocal] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 都道府県の一覧を取得する（検索ワード別）
     * 
     * @param freeWord 検索ワード
     * @param localId 地域ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getHotelBasicCountByPref(String[] cutWord, int localId)
    {
        int i;
        int count;
        int[][] idList;
        int[] idListResult;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        SearchEngineBasic seb;

        ret = false;

        // 都道府県一覧取得
        seb = new SearchEngineBasic();
        seb.getPrefListByLocal( localId, 1 );

        // ホテル件数配列を用意し、初期化する。
        this.m_masterPref = seb.getMasterPref();
        this.m_masterPrefCount = seb.getMasterPrefCount();
        this.m_masterPrefHotelCount = new int[this.m_masterPrefCount];
        idList = new int[cutWord.length][0];

        try
        {
            connection = DBConnection.getConnection();

            for( i = 0 ; i < cutWord.length ; i++ )
            {
                // 候補の取得
                query = "SELECT hh_hotel_basic.id FROM hh_hotel_search,hh_hotel_basic,hh_master_pref";
                query = query + " WHERE hh_hotel_search.id <> 0";
                query = query + " AND hh_hotel_search.word LIKE ?";
                query = query + " AND hh_master_pref.local_id = ?";
                query = query + " AND hh_hotel_search.id=hh_hotel_basic.id";
                query = query + " AND hh_hotel_basic.pref_id=hh_master_pref.pref_id";
                query = query + " GROUP BY hh_hotel_search.id";

                count = 0;
                try
                {
                    prestate = connection.prepareStatement( query );
                    prestate.setString( 1, "%" + cutWord[i] + "%" );
                    prestate.setInt( 2, localId );
                    result = prestate.executeQuery();
                    if ( result != null )
                    {
                        // レコード件数取得
                        if ( result.last() != false )
                        {
                            idList[i] = new int[result.getRow()];
                        }

                        result.beforeFirst();
                        while( result.next() != false )
                        {
                            idList[i][count++] = result.getInt( "hh_hotel_basic.id" );
                        }
                    }

                    if ( count > 0 )
                    {
                        // 検索ワードで一致するものがあった場合
                        freewordCountUpdate( cutWord[i] );
                    }
                    ret = true;
                }
                catch ( Exception e )
                {
                    Logging.info( "[getHotelBasicCountByPref] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    result = null;
                    prestate = null;
                }
            }

            idListResult = new int[0];

            // 複数条件指定の場合、全てに一致するもののみ抽出
            if ( cutWord.length > 1 )
            {
                idListResult = getMatchList( idList );
            }
            else
            {
                idListResult = idList[0];
            }

            // 候補を絞った後、都道府県別に振り分け
            query = "SELECT hh_hotel_basic.pref_id, COUNT(*) FROM hh_hotel_basic";
            query = query + " WHERE hh_hotel_basic.id IN(";
            for( i = 0 ; i < idListResult.length ; i++ )
            {
                query = query + idListResult[i];
                if ( i < idListResult.length - 1 )
                {
                    query = query + ",";
                }
            }
            query = query + " )";
            query = query + " GROUP BY hh_hotel_basic.pref_id";

            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result != null )
            {
                count = 0;

                result.beforeFirst();
                while( result.next() != false )
                {
                    for( i = 0 ; i < m_masterPrefCount ; i++ )
                    {
                        if ( m_masterPref[i].getPrefId() == result.getInt( "pref_id" ) )
                        {
                            this.m_masterPrefHotelCount[i] = result.getInt( 2 );
                            break;
                        }
                    }
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.info( "[getHotelBasicCountByPref] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 検索ワード分割処理
     * 
     * @param word キーワード
     * @return 分割後ワード
     */
    private String[] cutSearchWord(String word)
    {
        int i;
        int count = 0;
        int spCount = 0;
        char charBuff;
        String strBuff;
        String cutWord[];
        StringBuffer wordBuff;

        // 全角スペースを半角スペースに置き換える
        wordBuff = new StringBuffer( word.replace( '　', ' ' ).trim() );

        for( i = 0 ; i < wordBuff.length() ; i++ )
        {
            charBuff = wordBuff.charAt( i );
            if ( charBuff == ' ' )
            {
                if ( spCount > 0 )
                {
                    count++;
                    spCount = 0;
                }
            }
            else if ( charBuff == '　' )
            {
                if ( spCount > 0 )
                {
                    count++;
                    spCount = 0;
                }
            }
            else
            {
                spCount++;
            }
        }

        if ( count > 0 )
        {
            cutWord = new String[count + 1];

            count = 0;
            spCount = 0;
            strBuff = "";

            for( i = 0 ; i < wordBuff.length() ; i++ )
            {
                charBuff = wordBuff.charAt( i );
                if ( charBuff == ' ' )
                {
                    if ( spCount > 0 )
                    {
                        cutWord[count] = strBuff;

                        strBuff = "";
                        count++;
                        spCount = 0;
                    }
                }
                else if ( charBuff == '　' )
                {
                    if ( spCount > 0 )
                    {
                        cutWord[count] = strBuff;

                        strBuff = "";
                        count++;
                        spCount = 0;
                    }
                }
                else
                {
                    strBuff = strBuff + Character.toString( charBuff );
                    spCount++;
                }
            }

            if ( strBuff.compareTo( "" ) != 0 )
            {
                cutWord[count] = strBuff;
            }
        }
        else
        {
            cutWord = new String[1];
            cutWord[0] = wordBuff.toString();
        }

        return(cutWord);
    }

    /**
     * IDマッチ処理
     * 
     * @param idList ID一覧
     * @return マッチ後ID一覧
     */
    private int[] getMatchList(int[][] idList)
    {
        int i;
        int j;
        int k;
        int count;
        int matchCount;
        int[] idListResult;

        count = 0;
        idListResult = new int[0];

        // 検索結果のホテル一覧をまとめる（全てに入っているもののみ）
        for( i = 0 ; i < idList[0].length ; i++ )
        {
            matchCount = 0;

            for( j = 1 ; j < idList.length ; j++ )
            {
                for( k = 0 ; k < idList[j].length ; k++ )
                {
                    if ( idList[0][i] == idList[j][k] )
                    {
                        matchCount++;
                        break;
                    }
                }
            }
            if ( matchCount == idList.length - 1 )
            {
                count++;
            }
        }
        if ( count > 0 )
        {
            idListResult = new int[count];
            count = 0;

            for( i = 0 ; i < idList[0].length ; i++ )
            {
                matchCount = 0;

                for( j = 1 ; j < idList.length ; j++ )
                {
                    for( k = 0 ; k < idList[j].length ; k++ )
                    {
                        if ( idList[0][i] == idList[j][k] )
                        {
                            matchCount++;
                            break;
                        }
                    }
                }
                if ( matchCount == idList.length - 1 )
                {
                    idListResult[count++] = idList[0][i];
                }
            }
        }

        return(idListResult);
    }

    /**
     * フリーワード検索件数更新処理
     * 
     * @param freeword フリーワード
     */
    private void freewordCountUpdate(String freeword)
    {
        boolean ret;
        DataSystemFreeword dsf;

        dsf = new DataSystemFreeword();
        ret = dsf.getData( freeword );
        if ( ret != false )
        {
            dsf.updateData( freeword );
        }
        else
        {
            dsf.setFreeword( freeword );
            dsf.setCount( 1 );
            dsf.insertData();
        }
    }
}
