/*
 * @(#)DataHotelDistance.java
 * 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007
 * ホテル距離情報データ取得クラス
 */

package jp.happyhotel.data;

/**
 * ホテル距離、緯度経度取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/22
 */

public class DataHotelDistance implements Comparable<Object>
{
    private int    hotelId;
    private int    distance;
    private String hotelLat;
    private String hotelLon;

    /**
     * データを初期化します。
     */
    public DataHotelDistance()
    {
        hotelId = 0;
        distance = 0;
    }

    /**
     * @return 距離
     */
    public int getDistance()
    {
        return distance;
    }

    /**
     * @return ホテルの緯度
     */
    public String getHotelLat()
    {
        return hotelLat;
    }

    /**
     * @return ホテルの経度
     */
    public String getHotelLon()
    {
        return hotelLon;
    }

    /**
     * @return ホテルID
     */
    public int getId()
    {
        return hotelId;
    }

    /**
     * @param 距離をセット
     */
    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    /**
     * @param ホテルの緯度をセット
     */
    public void setHotelLat(String hotelLat)
    {
        this.hotelLat = hotelLat;
    }

    /**
     * @param ホテルの経度をセット
     */
    public void setHotelLon(String hotelLon)
    {
        this.hotelLon = hotelLon;
    }

    /**
     * @param ホテルIDをセット
     */
    public void setId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    /**
     * オブジェクト比較処理
     * 
     * @param obj オブジェクト
     * @return 処理結果(-1:小さい,0:等しい,1:大きい)
     * @see DataHotelDistanceクラスのdistanceを比較する
     * @see java.util.Arrays.sort()を用いることでソート可能
     */
    public int compareTo(Object obj)
    {
        double distance1 = this.distance;
        double distance2 = ((DataHotelDistance)obj).distance;

        if ( distance1 == distance2 )
        {
            return 0;
        }
        else if ( distance1 > distance2 )
        {
            return 1;
        }
        else
        {
            return -1;
        }

    }
}
