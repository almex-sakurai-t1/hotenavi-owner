/*
 * @(#)HotelBasicInfo_M2.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテル情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテル情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/15
 */
public class HotelBasicInfo_M2 implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -4801063650044967621L;

    // private int m_hotelCount;
    // private DataHotelMaster m_hotelMaster;
    // private DataHotelBasic m_hotelInfo;
    // private DataHotelMessage m_hotelMessage;
    // private int m_hotelRemarksCount;
    // private DataHotelRemarks[] m_hotelRemarks;
    // private int m_hotelPriceCount;
    // private DataHotelPrice[] m_hotelPrice;
    // private int m_hotelEquipCount;
    // private DataHotelEquip[] m_hotelEquip;
    // private DataMasterEquip[] m_equipName;
    // private int m_hotelMapCount;
    // private DataHotelMap[] m_hotelMap;
    // private int m_hotelStatusCount;
    // private DataHotelStatus m_hotelStatus;
    private int[]             m_hotelIdList;

    // /** ホテル基本情報件数取得 **/
    // public int getCount( ) { return( m_hotelCount ); }
    // /** ホテル基本情報取得 **/
    // public DataHotelBasic getHotelInfo( ) { return( m_hotelInfo ); }
    // /** ホテル最新情報取得 **/
    // public DataHotelMessage getHotelMessage( ) { return( m_hotelMessage ); }
    // /** ホテル料金情報件数取得 **/
    // public int getPriceCount( ) { return( m_hotelPriceCount ); }
    // /** ホテル料金情報取得 **/
    // public DataHotelPrice[] getHotelPrice( ) { return( m_hotelPrice ); }
    // /** ホテル備考情報件数取得 **/
    // public int getRemarksCount( ) { return( m_hotelRemarksCount ); }
    // /** ホテル料金情報取得 **/
    // public DataHotelRemarks[] getHotelRemarks( ) { return( m_hotelRemarks ); }
    // /** ホテル設備情報件数取得 **/
    // public int getEquipCount( ) { return( m_hotelEquipCount ); }
    // /** ホテル設備情報取得 **/
    // public DataHotelEquip[] getHotelEquip( ) { return( m_hotelEquip ); }
    // /** ホテル設備情報取得 **/
    // public DataMasterEquip[] getEquipName( ) { return( m_equipName ); }
    // /** ホテル地図関連情報件数取得 **/
    // public int getMapCount( ) { return( m_hotelMapCount ); }
    // /** ホテル地図関連情報取得 **/
    // public DataHotelMap[] getHotelMap( ) { return( m_hotelMap ); }
    // /** ホテルステータス関連情報取得 **/
    // public int getStatusCount( ) { return( m_hotelStatusCount ); }
    // /** ホテルステータス関連情報取得 **/
    // public DataHotelStatus getHotelStatus( ) { return( m_hotelStatus ); }
    // /** ホテル設定情報取得取得 **/
    // public DataHotelMaster getHotelMaster( ) { return( m_hotelMaster ); }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    /**
     * ホテル件数取得（都道府県IDから）
     * 
     * @param prefId 都道府県ID
     * @return ホテル件数
     */
    public int getHotelCountByPref(int[] prefId) throws Exception
    {
        int count;
        int hotelCount;
        int prefCount = prefId.length;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        count = 0;
        hotelCount = 0;

        query = "SELECT id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_basic WHERE id <> 0";

        if ( prefCount >= 1 && prefId[0] != 0 )
        {
            query = query + " AND pref_id in ( ";
            for( int i = 0 ; i < prefCount ; i++ )
            {
                if ( i == 0 )
                    query = query + "?";
                else
                    query = query + ", " + "?";
            }
            query = query + ") ";
        }

        query = query + " AND hh_hotel_basic.kind <= 7"
                + " ORDER BY Ranking DESC, hh_hotel_basic.name_kana";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            if ( prefCount >= 1 && prefId[0] != 0 )
            {
                for( int i = 0 ; i < prefCount ; i++ )
                {
                    prestate.setInt( (i + 1), prefId[i] );
                }
            }

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    hotelCount = result.getRow();
                }

                m_hotelIdList = new int[hotelCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    m_hotelIdList[count++] = result.getInt( "id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[HotelBasiCInfo_M2.getHotelCountByPref(" + prefId + ")] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(hotelCount);
    }

}
