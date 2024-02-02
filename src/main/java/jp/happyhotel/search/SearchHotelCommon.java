/*
 * @(#)SearchHotelCommon.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテル検索共通取得クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;

/**
 * ホテル検索共通取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/15
 */
public class SearchHotelCommon implements Serializable
{

    private static final long serialVersionUID = 5377516176123625349L;

    private static final int  RESULTHOTEL_MAX  = 10000;
    private int[]             prefHotelList;
    private int[]             equipHotelList;
    private int[]             priceHotelList;
    private int[]             resultHotelList;

    private int               m_hotelCount;
    private int               m_hotelAllCount;
    private DataHotelBasic[]  m_hotelInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelCommon()
    {
        prefHotelList = null;
        equipHotelList = null;
        priceHotelList = null;
        resultHotelList = null;

        m_hotelCount = 0;
        m_hotelAllCount = 0;
        m_hotelInfo = new DataHotelBasic[0];
    }

    /** ホテル基本情報件数取得 **/
    public int getCount()
    {
        return(m_hotelCount);
    }

    /** ホテル基本情報件数取得 **/
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public void setEquipHotelList(int[] equipHotelList)
    {
        this.equipHotelList = equipHotelList;
    }

    public void setPrefHotelList(int[] prefHotelList)
    {
        this.prefHotelList = prefHotelList;
    }

    public void setPriceHotelList(int[] priceHotelList)
    {
        this.priceHotelList = priceHotelList;
    }

    public void setResultHotelList(int[] resultHotelList)
    {
        this.resultHotelList = resultHotelList;
    }

    public DataHotelBasic[] getHotelInfo()
    {
        return(m_hotelInfo);
    }

    /**
     * 検索結果マージ処理. set???HotelListにて検索結果のホテルリストをセット後呼び出す。
     * 
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @return マージ結果後ホテルリスト
     */
    public int[] getMargeHotel(int countNum, int pageNum)
    {
        int[] resultList;

        resultList = new int[0];

        resultList = getMargeHotelSub( countNum, pageNum, true );

        return(resultList);
    }

    /**
     * 検索結果マージ処理. set???HotelListにて検索結果のホテルリストをセット後呼び出す。
     * 
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param getHotel ホテルデータ取得(true:取得する)
     * @return マージ結果後ホテルリスト
     */
    public int[] getMargeHotel(int countNum, int pageNum, boolean getHotel)
    {
        int[] resultList;
        resultList = new int[0];
        resultList = getMargeHotelSub( countNum, pageNum, getHotel );
        return(resultList);
    }

    /**
     * 検索結果マージ処理. set???HotelListにて検索結果のホテルリストをセット後呼び出す。
     * 
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     * @param getHotel ホテルデータ取得(true:取得する)
     * @return マージ結果後ホテルリスト
     */
    private int[] getMargeHotelSub(int countNum, int pageNum, boolean getHotel)
    {
        int i;
        int j;
        int k;
        int count;
        int[] margeList;
        int[] margeResult;
        int[] resultList;

        margeList = new int[RESULTHOTEL_MAX];
        margeResult = new int[RESULTHOTEL_MAX];
        resultList = new int[0];

        count = 0;
        // 設備一覧よりマージ
        if ( equipHotelList != null )
        {
            for( i = 0 ; i < equipHotelList.length ; i++ )
            {
                if ( equipHotelList[i] == 0 )
                {
                    break;
                }

                if ( priceHotelList != null )
                {
                    for( j = 0 ; j < priceHotelList.length ; j++ )
                    {
                        if ( priceHotelList[j] == 0 )
                        {
                            break;
                        }

                        if ( equipHotelList[i] == priceHotelList[j] )
                        {
                            if ( resultHotelList != null )
                            {
                                for( k = 0 ; k < resultHotelList.length ; k++ )
                                {
                                    if ( resultHotelList[k] == 0 )
                                    {
                                        break;
                                    }

                                    if ( equipHotelList[i] == resultHotelList[k] )
                                    {
                                        margeList[count++] = equipHotelList[i];
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                margeList[count++] = equipHotelList[i];
                                break;
                            }
                        }
                    }
                }
                else if ( resultHotelList != null )
                {
                    for( k = 0 ; k < resultHotelList.length ; k++ )
                    {
                        if ( resultHotelList[k] == 0 )
                        {
                            break;
                        }

                        if ( equipHotelList[i] == resultHotelList[k] )
                        {
                            margeList[count++] = equipHotelList[i];
                            break;
                        }
                    }
                }
                else
                {
                    margeList[count++] = equipHotelList[i];
                    break;
                }
            }
        }
        else if ( priceHotelList != null )
        {
            if ( resultHotelList != null )
            {
                for( j = 0 ; j < priceHotelList.length ; j++ )
                {
                    if ( priceHotelList[j] == 0 )
                    {
                        break;
                    }

                    for( k = 0 ; k < resultHotelList.length ; k++ )
                    {
                        if ( resultHotelList[k] == 0 )
                        {
                            break;
                        }

                        if ( priceHotelList[j] == resultHotelList[k] )
                        {
                            margeList[count++] = priceHotelList[j];
                            break;
                        }
                    }
                }
            }
            else
            {
                margeList = priceHotelList;
                count = priceHotelList.length;
            }
        }
        else if ( resultHotelList != null )
        {
            margeList = resultHotelList;
            count = resultHotelList.length;
        }

        if ( prefHotelList != null )
        {
            count = 0;

            // 都道府県のホテルリストはOR条件
            // margeListからの絞込み
            for( i = 0 ; i < prefHotelList.length ; i++ )
            {
                if ( prefHotelList[i] == 0 )
                {
                    break;
                }

                for( j = 0 ; j < margeList.length ; j++ )
                {
                    if ( margeList[j] == 0 )
                    {
                        break;
                    }

                    if ( prefHotelList[i] == margeList[j] )
                    {
                        margeResult[count] = margeList[j];

                        count++;
                        break;
                    }
                }
            }
        }
        else
        {
            margeResult = margeList;
        }

        resultList = new int[count];
        this.m_hotelInfo = new DataHotelBasic[count];
        this.m_hotelAllCount = count;
        this.m_hotelCount = 0;

        for( i = 0 ; i < count ; i++ )
        {
            resultList[i] = margeResult[i];
        }

        if ( getHotel != false )
        {
            count = 0;
            for( i = pageNum * countNum ; i < this.m_hotelAllCount ; i++ )
            {

                this.m_hotelInfo[count] = new DataHotelBasic();
                this.m_hotelInfo[count].getData( resultList[i] );
                count++;
                if ( count >= countNum && countNum != 0 )
                {
                    break;
                }
            }
            this.m_hotelCount = count;
        }
        else
        {
            this.m_hotelCount = this.m_hotelAllCount;
        }

        if ( count > 0 )
        {
            // ID結果をランク順に並べ替え
            resultList = sortHotelRank( resultList );
        }

        return(resultList);
    }

    /**
     * 検索結果ランク別並べ替え
     * 
     * @param resultList ホテルIDリスト
     * @return ランク別並べ替え結果後ホテルリスト
     */
    public int[] sortHotelRank(int[] resultList)
    {
        int i;
        int count;
        int[] idList;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        idList = resultList;

        if ( resultList != null )
        {
            query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic,hh_hotel_sort WHERE ";
            query = query + " hh_hotel_basic.kind <= 7";

            if ( resultList.length > 0 )
            {
                query = query + " AND hh_hotel_basic.id IN(";
                for( i = 0 ; i < resultList.length ; i++ )
                {
                    if ( resultList[i] == 0 )
                    {
                        break;
                    }
                    query = query + resultList[i];

                    if ( i + 1 < resultList.length )
                    {
                        if ( resultList[i + 1] != 0 )
                        {
                            query = query + ",";
                        }
                    }
                }
                query = query + ")";
            }
            query = query + " AND hh_hotel_basic.id = hh_hotel_sort.id";
            query = query + " AND hh_hotel_sort.collect_date = 0";
            query = query + " GROUP BY hh_hotel_basic.id";
            query = query + " ORDER BY Ranking DESC, hh_hotel_sort.all_point DESC, hh_hotel_basic.name_kana";

            try
            {
                connection = DBConnection.getConnectionRO();
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();

                // レコード件数取得
                if ( result.last() != false )
                {
                    idList = new int[result.getRow()];
                }
                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    idList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[sortHotelRank] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }

        return(idList);
    }
}
