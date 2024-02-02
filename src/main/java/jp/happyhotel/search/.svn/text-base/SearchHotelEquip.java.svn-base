/*
 * @(#)SearchHotelEquip.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 設備別ホテル取得クラス
 */
package jp.happyhotel.search;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * 設備別ホテル取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/19
 */
public class SearchHotelEquip implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 4146033710422271672L;

    private int               m_hotelCount;
    private int               m_hotelAllCount;
    private int[]             m_hotelIdList;
    private DataHotelBasic[]  m_hotelInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelEquip()
    {

        m_hotelCount = 0;
        m_hotelAllCount = 0;
    }

    public int getCount()
    {
        return(m_hotelCount);
    }

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

    /**
     * ホテル一覧情報取得(ホテルランク順)
     * 
     * @param equipList 設備コード一覧
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getHotelList(int equipList[], int countNum, int pageNum)
    {
        int i;
        int count;
        int[] hotelBasicList;

        // ホテルのIDリスト取得
        hotelBasicList = getHotelBasicIdList( equipList );

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
            if ( count + 1 >= countNum && countNum != 0 )
            {
                break;
            }
        }
        m_hotelCount = count;

        return(true);
    }

    /**
     * ホテル基本情報検索結果一覧取得
     * 
     * @param cutWord 検索キーワード
     * @return ホテルIDリスト(null:失敗)
     */
    private int[] getHotelBasicIdList(int[] equipList)
    {
        int i;
        int j;
        int k;
        int count;
        int matchCount;
        int[][] idList;
        int[] idListResult;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        idList = new int[equipList.length][0];

        try
        {
            connection = DBConnection.getConnection();

            // 検索ワードクエリーの結合
            for( i = 0 ; i < equipList.length ; i++ )
            {
                count = 0;

                query = "SELECT hh_hotel_basic.id FROM hh_hotel_basic,hh_hotel_equip,hh_hotel_pv WHERE";
                query = query + " hh_hotel_equip.equip_id = " + equipList[i];
                query = query + " AND (hh_hotel_equip.equip_type = 1 OR hh_hotel_equip.equip_type = 2 OR hh_hotel_equip.equip_type = 3)";
                query = query + " AND hh_hotel_basic.id = hh_hotel_equip.id";
                query = query + " AND hh_hotel_basic.kind <= 7";
                query = query + " AND hh_hotel_basic.id = hh_hotel_pv.id";
                query = query + " AND hh_hotel_pv.collect_date = 0";
                query = query + " GROUP BY hh_hotel_basic.id";
                query = query + " ORDER BY hh_hotel_basic.rank DESC, hh_hotel_pv.total_uu_pv DESC, hh_hotel_basic.name_kana";

                try
                {
                    prestate = connection.prepareStatement( query );
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
                            idList[i][count++] = result.getInt( "id" );
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.error( "[getHotelList] Exception=" + e.toString() );
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
            Logging.error( "[getHotelList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }

        count = 0;
        idListResult = new int[0];

        // 複数条件指定の場合、全てに一致するもののみ抽出
        if ( equipList.length > 1 )
        {
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
        }
        else
        {
            idListResult = idList[0];
        }

        return(idListResult);
    }
}
