package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.data.DataMasterLocal_M2;
import jp.happyhotel.data.DataMasterPref_M2;
import jp.happyhotel.data.DataSystemFreeword;

/**
 * 
 * This class handles the core freeword search logic and the data fetching from database.
 * 
 * @author HCL Technologies Ltd.
 * 
 */

public class SearchHotelFreeword_M2 implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -5524226612470789020L;
    // private int m_hotelCount;
    private int               m_hotelAllCount;
    private int               m_localCount;
    private int               m_prefCount;

    // private DataSearchHotel_M2[] m_hotelInfo ;

    public SearchHotelFreeword_M2()
    {
        // m_hotelCount = 0;
        m_hotelAllCount = 0;
        m_localCount = 0;
        m_prefCount = 0;
    }

    // public int getCount( ) { return( m_hotelCount ); }
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int getLocalCount()
    {
        return(m_localCount);
    }

    public int getPrefCount()
    {
        return(m_prefCount);
    }

    // public DataSearchHotel_M2[] getHotelInfo( ) { return( m_hotelInfo ); }

    /**
     * Gives Hotel list for given freeword
     * 
     * @param freeWord
     * @return Hotel Id list corresponding to the word searched
     * @throws Exception
     */
    public int[] getSearchIdList(String freeWord) throws Exception
    {

        int[] arrHotelIdList = null;
        String[] cutWord;

        freeWord = ReplaceString.DBEscape( freeWord );
        // 検索ワードを分割する
        cutWord = cutSearchWord( freeWord );

        // ホテルのIDリスト取得
        arrHotelIdList = getHotelBasicIdList( cutWord );

        return arrHotelIdList;
    }

    /**
     * Gives Hotel list for given freeword (フリーワードカウントを取らない)
     * 
     * @param freeWord
     * @return Hotel Id list corresponding to the word searched
     * @throws Exception
     */
    public int[] getSearchIdListNoCount(String freeWord) throws Exception
    {

        int[] arrHotelIdList = null;
        String[] cutWord;

        freeWord = ReplaceString.DBEscape( freeWord );
        // 検索ワードを分割する
        cutWord = cutSearchWord( freeWord );

        // ホテルのIDリスト取得
        arrHotelIdList = getHotelBasicIdListNoCount( cutWord );

        return arrHotelIdList;
    }

    /**
     * Gives Hotel list for given freeword and prefectureId
     * 
     * @param freeWord
     * @param prefId
     * @return Hotel Id list corresponding to the word searched
     * @throws Exception
     */
    public int[] getSearchIdList(String freeWord, String prefId) throws Exception
    {

        int[] arrHotelIdList = null;
        String[] cutWord;

        freeWord = ReplaceString.DBEscape( freeWord );
        // ホテルのIDリスト取得
        if ( prefId != null )
        {
            if ( CheckString.numCheck( prefId ) != false )
            {
                // Gets Hotel Ids
                arrHotelIdList = getHotelBasicIdListByPref( freeWord, Integer.parseInt( prefId ) );
            }
        }
        else
        {
            // 検索ワードを分割する
            cutWord = cutSearchWord( freeWord );
            // Gets Hotel Ids
            arrHotelIdList = getHotelBasicIdList( cutWord );
        }
        return arrHotelIdList;
    }

    /**
     * ホテル一覧取得(jiscode指定)
     * 
     * @param freeWord
     * @param jiscode
     * @return ホテルIDリスト(null:失敗)
     * @throws Exception
     */
    public int[] getSearchIdListByJiscode(String freeWord, String jiscode) throws Exception
    {

        int[] arrHotelIdList = null;
        String[] cutWord;

        freeWord = ReplaceString.DBEscape( freeWord );
        // ホテルのIDリスト取得
        if ( jiscode != null )
        {
            if ( CheckString.numCheck( jiscode ) != false )
            {
                // ホテルID一覧取得
                arrHotelIdList = getHotelBasicIdListByJiscode( freeWord, Integer.parseInt( jiscode ) );
            }
        }
        else
        {
            // 検索ワードを分割する
            cutWord = cutSearchWord( freeWord );
            // ホテルID一覧取得
            arrHotelIdList = getHotelBasicIdList( cutWord );
        }

        return arrHotelIdList;

    }

    /**
     * Gives hotel data by locals
     * 
     * @param hotelIds
     * @param zeroResultDispflag
     * @return
     * @throws Exception
     */
    public DataMasterLocal_M2[] getHotelListByLocal(int[] hotelIds, boolean zeroResultDispflag) throws Exception
    {
        String query = null;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataMasterLocal_M2 arrDataMasterLocal[] = new DataMasterLocal_M2[9];

        if ( zeroResultDispflag != false )
        {
            query = "SELECT hh_master_local.local_id,hh_master_local.name,count(hh_hotel_basic.id) as count FROM hh_master_local"
                    + " LEFT JOIN hh_master_pref ON (hh_master_local.local_id = hh_master_pref.local_id )"
                    + " LEFT JOIN hh_hotel_basic ON (hh_hotel_basic.pref_id = hh_master_pref.pref_id "
                    + " AND hh_hotel_basic.id IN(";
            for( int i = 0 ; i < hotelIds.length ; i++ )
            {
                if ( i == 0 )
                    query = query + "?";
                else
                    query = query + ", " + "?";
            }
            query = query + " ))"
                    + " GROUP BY hh_master_pref.local_id";

        }
        else
        {
            query = "SELECT hh_master_local.local_id,hh_master_local.name,count(*) as count FROM hh_master_local,hh_master_pref,hh_hotel_basic"
                    + " WHERE hh_hotel_basic.id IN(";
            for( int i = 0 ; i < hotelIds.length ; i++ )
            {
                if ( i == 0 )
                    query = query + "?";
                else
                    query = query + ", " + "?";
            }
            query = query + " )"
                    + " AND hh_master_local.local_id=hh_master_pref.local_id and hh_master_pref.pref_id = hh_hotel_basic.pref_id"
                    + " GROUP BY hh_master_pref.local_id";
        }

        try
        {
            connection = DBConnection.getConnection();

            prestate = connection.prepareStatement( query );
            for( int i = 0 ; i < hotelIds.length ; i++ )
            {
                prestate.setInt( (i + 1), hotelIds[i] );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.last() != false )
                {
                    m_localCount = result.getRow();
                }

                result.beforeFirst();
                for( int count = 0 ; result.next() ; count++ )
                {
                    arrDataMasterLocal[count] = new DataMasterLocal_M2();
                    arrDataMasterLocal[count].setLocalId( result.getInt( "local_id" ) );
                    arrDataMasterLocal[count].setName( result.getString( "name" ) );
                    arrDataMasterLocal[count].setHotelCount( result.getInt( "count" ) );
                }
            }
            return arrDataMasterLocal;
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchHotelFreeword_M2.getHotelListByLocal(" + hotelIds + "," + zeroResultDispflag + ")] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrDataMasterLocal = null;
        }
    }

    /**
     * Gives hotel data by prefectures
     * 
     * @param hotelIds
     * @param localId
     * @param zeroResultDispflag
     * @return
     * @throws Exception
     */
    public DataMasterPref_M2[] getHotelListByPref(int[] hotelIds, int localId, boolean zeroResultDispflag) throws Exception
    {
        int i;
        int count;
        String query = null;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataMasterPref_M2 arrDataMasterPref[] = null;

        if ( zeroResultDispflag != false )
        {
            query = "SELECT hh_master_pref.pref_id,hh_master_pref.name,count(hh_hotel_basic.id) as count FROM hh_master_pref"
                    + " LEFT JOIN hh_hotel_basic ON ( hh_master_pref.pref_id = hh_hotel_basic.pref_id "
                    + " AND hh_hotel_basic.id IN(";
            for( i = 0 ; i < hotelIds.length ; i++ )
            {
                query = query + hotelIds[i];
                if ( i < hotelIds.length - 1 )
                {
                    query = query + ",";
                }
            }
            query = query + " ))"
                    + " WHERE hh_master_pref.local_id =?"
                    + " GROUP BY hh_master_pref.pref_id";

        }
        else
        {

            query = "SELECT hh_master_pref.pref_id,hh_master_pref.name,count(*) as count FROM hh_master_pref,hh_hotel_basic"
                    + " WHERE hh_hotel_basic.id IN(";
            for( i = 0 ; i < hotelIds.length ; i++ )
            {
                query = query + hotelIds[i];
                if ( i < hotelIds.length - 1 )
                {
                    query = query + ",";
                }
            }
            query = query + " )"
                    + " AND hh_master_pref.local_id =?"
                    + " AND hh_hotel_basic.pref_id = hh_master_pref.pref_id "
                    + " GROUP BY hh_master_pref.pref_id";
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, localId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    m_prefCount = result.getRow();
                }

                result.beforeFirst();
                arrDataMasterPref = new DataMasterPref_M2[m_prefCount];
                for( count = 0 ; result.next() ; count++ )
                {
                    arrDataMasterPref[count] = new DataMasterPref_M2();
                    arrDataMasterPref[count].setPrefId( result.getInt( "pref_id" ) );
                    arrDataMasterPref[count].setName( result.getString( "name" ) );
                    arrDataMasterPref[count].setHotelCount( result.getInt( "count" ) );
                }
            }
            return arrDataMasterPref;
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchHotelFreeword_M2.getHotelListByPref(" + hotelIds + " ," + localId + " ," + zeroResultDispflag + ")] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            arrDataMasterPref = null;
        }
    }

    /**
     * Gets list of Hotel Ids for given freeword and prefecture Id
     * 
     * @param freeWord
     * @param prefId
     * @return array of hotel Ids
     * @throws Exception
     */

    public int[] getHotelBasicIdListByPref(String freeWord, int prefId) throws Exception
    {
        int i;
        int count;
        int[][] idList;
        int[] idListResult;
        String cutWord[] = null;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        freeWord = ReplaceString.DBEscape( freeWord );
        cutWord = cutSearchWord( freeWord );

        idList = new int[cutWord.length][0];

        try
        {
            connection = DBConnection.getConnectionRO();

            // 検索ワードクエリーの結合
            for( i = 0 ; i < cutWord.length ; i++ )
            {
                count = 0;

                query = "SELECT hh_hotel_search.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_search,hh_hotel_basic,hh_hotel_sort"
                        + " WHERE hh_hotel_search.id <> 0"
                        + " AND hh_hotel_search.word LIKE ?"
                        + " AND hh_hotel_basic.pref_id = ?"
                        + " AND hh_hotel_search.id=hh_hotel_basic.id"
                        + " AND hh_hotel_basic.kind <= 7"
                        + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                        + " AND hh_hotel_sort.collect_date = 0"
                        + " GROUP BY hh_hotel_basic.id"
                        + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";
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
                }
                catch ( Exception e )
                {
                    Logging.error( "[SearchHotelFreeword_M2.getHotelBasicIdListByPref( String freeWord = " + freeWord + " , int prefId = " + prefId + " )] Exception=" + e.toString() );
                    throw e;
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
            Logging.error( "SearchHotelFreeword_M2.getHotelBasicIdListByPref( String freeWord = " + freeWord + " , int prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
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
     * ホテルID一覧取得（jiscode指定）
     * 
     * @param freeWord
     * @param jiscode
     * @return ホテルIDリスト
     * @throws Exception
     */

    public int[] getHotelBasicIdListByJiscode(String freeWord, int jiscode) throws Exception
    {
        int i;
        int count;
        int[][] idList;
        int[] idListResult;
        String cutWord[] = null;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        freeWord = ReplaceString.DBEscape( freeWord );
        cutWord = cutSearchWord( freeWord );

        idList = new int[cutWord.length][0];

        try
        {
            connection = DBConnection.getConnectionRO();

            // 検索ワードクエリーの結合
            for( i = 0 ; i < cutWord.length ; i++ )
            {
                count = 0;

                query = "SELECT hh_hotel_search.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_search,hh_hotel_basic,hh_hotel_sort"
                        + " WHERE hh_hotel_search.id <> 0"
                        + " AND hh_hotel_search.word LIKE ?"
                        + " AND hh_hotel_basic.jis_code = ?"
                        + " AND hh_hotel_search.id=hh_hotel_basic.id"
                        + " AND hh_hotel_basic.kind <= 7"
                        + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                        + " AND hh_hotel_sort.collect_date = 0"
                        + " GROUP BY hh_hotel_basic.id"
                        + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";
                try
                {
                    prestate = connection.prepareStatement( query );
                    prestate.setString( 1, "%" + cutWord[i] + "%" );
                    prestate.setInt( 2, jiscode );
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
                }
                catch ( Exception e )
                {
                    Logging.error( "[SearchHotelFreeword_M2.getHotelBasicIdListByJiscode( String freeWord = " + freeWord + " , int prefId = " + jiscode + " )] Exception=" + e.toString() );
                    throw e;
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
            Logging.error( "SearchHotelFreeword_M2.getHotelBasicIdListByJiscode( String freeWord = " + freeWord + " , int prefId = " + jiscode + " )] Exception=" + e.toString() );
            throw e;
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
     * Search the hotel ids for entered keywords String array
     * 
     * @param cutWord 検索キーワード
     * 
     * @return ホテルIDリスト(null:失敗)
     */
    private int[] getHotelBasicIdList(String[] cutWord) throws Exception
    {

        int loop;
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
            connection = DBConnection.getConnectionRO();

            // 検索ワードクエリーの結合
            for( loop = 0 ; loop < cutWord.length ; loop++ )
            {
                count = 0;

                query = "SELECT hh_hotel_search.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_search,hh_hotel_basic,hh_hotel_sort"
                        + " WHERE hh_hotel_search.id <> 0"
                        + " AND hh_hotel_search.word LIKE ?"
                        + " AND hh_hotel_search.id=hh_hotel_basic.id"
                        + " AND hh_hotel_basic.kind <= 7"
                        + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                        + " AND hh_hotel_sort.collect_date = 0"
                        + " GROUP BY hh_hotel_basic.id"
                        + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

                try
                {
                    prestate = connection.prepareStatement( query );

                    prestate.setString( 1, "%" + cutWord[loop] + "%" );
                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        // レコード件数取得
                        if ( result.last() != false )
                        {
                            idList[loop] = new int[result.getRow()];

                        }

                        result.beforeFirst();
                        while( result.next() != false )
                        {
                            // ホテル情報の取得
                            idList[loop][count++] = result.getInt( "hh_hotel_search.id" );

                        }
                    }
                    if ( count > 0 )
                    {
                        // 検索ワードで一致するものがあった場合
                        freewordCountUpdate( cutWord[loop] );

                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[SearchHotelFreeword_M2.getHotelBasicIdList (" + cutWord + ") -[word " + loop + "th " + cutWord[loop] + "]] Exception=" + e.toString() );
                    throw e;
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
            Logging.error( "[SearchHotelFreeword_M2.getHotelBasicIdList (" + cutWord + ")] Exception=" + e.toString() );
            throw e;
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
     * Search the hotel ids for entered keywords String array (フリーワードカウントを取らない)
     * 
     * @param cutWord 検索キーワード
     * 
     * @return ホテルIDリスト(null:失敗)
     */
    private int[] getHotelBasicIdListNoCount(String[] cutWord) throws Exception
    {

        int loop;
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
            connection = DBConnection.getConnectionRO();

            // 検索ワードクエリーの結合
            for( loop = 0 ; loop < cutWord.length ; loop++ )
            {
                count = 0;

                query = "SELECT hh_hotel_search.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_search,hh_hotel_basic,hh_hotel_sort"
                        + " WHERE hh_hotel_search.id <> 0"
                        + " AND hh_hotel_search.word LIKE ?"
                        + " AND hh_hotel_search.id=hh_hotel_basic.id"
                        + " AND hh_hotel_basic.kind <= 7"
                        + " AND hh_hotel_basic.id = hh_hotel_sort.id"
                        + " AND hh_hotel_sort.collect_date = 0"
                        + " GROUP BY hh_hotel_basic.id"
                        + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

                try
                {
                    prestate = connection.prepareStatement( query );

                    prestate.setString( 1, "%" + cutWord[loop] + "%" );
                    result = prestate.executeQuery();

                    if ( result != null )
                    {
                        // レコード件数取得
                        if ( result.last() != false )
                        {
                            idList[loop] = new int[result.getRow()];

                        }

                        result.beforeFirst();
                        while( result.next() != false )
                        {
                            // ホテル情報の取得
                            idList[loop][count++] = result.getInt( "hh_hotel_search.id" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[SearchHotelFreeword_M2.getHotelBasicIdList (" + cutWord + ") -[word " + loop + "th " + cutWord[loop] + "]] Exception=" + e.toString() );
                    throw e;
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
            Logging.error( "[SearchHotelFreeword_M2.getHotelBasicIdList (" + cutWord + ")] Exception=" + e.toString() );
            throw e;
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
     * It spilts the keyword string to String array
     * 
     * @param word キーワード
     * @return 分割後ワード
     */
    private static String[] cutSearchWord(String word)
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
     * It finds and returns only the matching hotel that are common for each keyword
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
     * It update the keyword search count in database.
     * 
     * @param freeword フリーワード
     */
    private void freewordCountUpdate(String freeword) throws Exception
    {
        boolean ret;
        DataSystemFreeword dataSystemFreeword;

        // 重複登録防止のため文字列を変換
        freeword = ReplaceString.replaceSearchWord( freeword );

        dataSystemFreeword = new DataSystemFreeword();
        ret = dataSystemFreeword.getData( freeword );
        if ( ret != false )
        {
            dataSystemFreeword.updateData( freeword );
        }
        else
        {
            dataSystemFreeword.setFreeword( freeword );
            dataSystemFreeword.setCount( 1 );
            dataSystemFreeword.insertData();
        }
        dataSystemFreeword = null;
    }

}
