/*
 * @(#)SearchHotelChain.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 チェーン店検索ホテル取得クラス
 */
package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterChain_M2;

/**
 * チェーン店検索ホテル取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/09/10
 * @version 1.10 2007/11/15
 */
public class SearchHotelChain_M2 implements Serializable
{
    private static final long serialVersionUID = -1608867817744034648L;
    // private int m_hotelCount;
    private int               m_hotelAllCount;
    private int[]             m_hotelIdList;
    private int               m_groupCount;
    private int[]             m_groupIdList;
    private String[]          m_groupNameList;
    private String[]          m_groupNameKanaList;
    private int[]             m_groupIdCount;

    // private DataSearchHotel_M2[] m_hotelInfo;

    /**
     * データを初期化します。
     */
    public SearchHotelChain_M2()
    {
        // m_hotelCount = 0;
        m_hotelAllCount = 0;
    }

    // public int getCount( ) { return( m_hotelCount ); }
    public int getAllCount()
    {
        return(m_hotelAllCount);
    }

    public int[] getHotelIdList()
    {
        return(m_hotelIdList);
    }

    // public DataSearchHotel_M2[] getHotelInfo( ) { return( m_hotelInfo ); }

    public int getGroupCount()
    {
        return(m_groupCount);
    }

    public int[] getGroupIdList()
    {
        return(m_groupIdList);
    }

    public String[] getGroupNameList()
    {
        return(m_groupNameList);
    }

    public String[] getGroupNameKanaList()
    {
        return(m_groupNameKanaList);
    }

    public int[] getGroupCountList()
    {
        return(m_groupIdCount);
    }

    /**
     * Acquires hotel Ids for given groupId
     * 
     * @param groupId
     * @return hotelIdList
     * @throws Exception
     */

    public int[] getHotelBasicIdList(String groupId) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        int count = 0;
        int totalHotel = 0;
        int[] hotelIdList = null;

        // ホテル総件数の取得

        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_chain,hh_hotel_basic WHERE"
                + " hh_hotel_chain.group_id = ?"
                + " AND hh_hotel_basic.id = hh_hotel_chain.id"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_chain.start_date <=" + Integer.parseInt( DateEdit.getDate( 2 ) )
                + " AND hh_hotel_chain.end_date >=" + Integer.parseInt( DateEdit.getDate( 2 ) )
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY hh_hotel_chain.disp_index, Ranking DESC, hh_hotel_basic.name_kana";

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, groupId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    totalHotel = result.getRow();
                }

                hotelIdList = new int[totalHotel];

                result.beforeFirst();
                while( result.next() != false )
                {
                    hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }

            }
            return(hotelIdList);

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelChain_M2.getHotelBasicIdList(String groupId = " + groupId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            hotelIdList = null;
        }
    }

    /**
     * Acquires Hotel Id for given groupId and prefId
     * 
     * @param groupId , prefId
     * @throws Exception
     */

    public int[] getHotelBasicIdListByPref(String groupId, String prefId) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        int count = 0;
        int totalHotel = 0;
        int[] hotelIdList = null;

        query = "SELECT hh_hotel_basic.id, (CASE WHEN hh_hotel_basic.rank=3 THEN 2 ELSE hh_hotel_basic.rank END) AS Ranking FROM hh_hotel_chain,hh_hotel_basic WHERE"
                + " hh_hotel_chain.group_id = ?"
                + " AND hh_hotel_basic.ad_pref_id = ?"
                + " AND hh_hotel_basic.id = hh_hotel_chain.id"
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_chain.start_date <=" + Integer.parseInt( DateEdit.getDate( 2 ) )
                + " AND hh_hotel_chain.end_date >=" + Integer.parseInt( DateEdit.getDate( 2 ) )
                + " GROUP BY hh_hotel_basic.id"
                + " ORDER BY hh_hotel_chain.disp_index, Ranking DESC, hh_hotel_basic.name_kana";

        count = 0;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, groupId );
            prestate.setString( 2, prefId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    totalHotel = result.getRow();
                }

                hotelIdList = new int[totalHotel];

                result.beforeFirst();
                while( result.next() != false )
                {
                    hotelIdList[count++] = result.getInt( "hh_hotel_basic.id" );
                }
            }
            return(hotelIdList);
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelChain.getHotelBasicIdList2(String groupId = " + groupId + " ,String prefId = " + prefId + " )] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            hotelIdList = null;
        }

    }

    /**
     * Fetches Chain Data
     * 
     * @param groupId
     * @return DataMasterChain_M2 object(set with Chain Data)
     * @throws Exception
     */
    public DataMasterChain_M2 getMasterChainData(String groupId) throws Exception
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataMasterChain_M2 dataMasterChain = null;
        query = "SELECT * FROM hh_master_chain WHERE group_id = ?";

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, groupId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    dataMasterChain = new DataMasterChain_M2();
                    dataMasterChain.setGroupId( result.getInt( "group_id" ) );
                    dataMasterChain.setGroupHotenavi( result.getString( "group_hotenavi" ) );
                    dataMasterChain.setName( result.getString( "name" ) );
                    dataMasterChain.setNameKana( result.getString( "name_kana" ) );
                    dataMasterChain.setPr( result.getString( "pr" ) );
                    dataMasterChain.setPictureJpg( result.getBytes( "picture_jpg" ) );
                    dataMasterChain.setPictureGif( result.getBytes( "picture_gif" ) );
                    dataMasterChain.setPicturePng( result.getBytes( "picture_png" ) );
                }
            }
            return(dataMasterChain);
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchHotelChain_M2.getMasterChainData(String groupId = " + groupId + ")] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            dataMasterChain = null;
        }

    }

    /**
     * チェーン店別チェーン数検索結果取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean getChainList() throws Exception
    {
        int count;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_master_chain.group_id,hh_master_chain.name,hh_master_chain.name_kana,count(*) as count"
                + " FROM hh_master_chain,hh_hotel_chain,hh_hotel_basic"
                + " WHERE hh_hotel_chain.group_id = hh_master_chain.group_id"
                + " AND hh_master_chain.disp_flag = 0"
                + " AND hh_hotel_chain.start_date <=" + Integer.parseInt( DateEdit.getDate( 2 ) )
                + " AND hh_hotel_chain.end_date >=" + Integer.parseInt( DateEdit.getDate( 2 ) )
                + " AND hh_hotel_basic.kind <= 7"
                + " AND hh_hotel_basic.id = hh_hotel_chain.id"
                + " GROUP BY hh_master_chain.group_id"
                + " ORDER BY count DESC";
        count = 0;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    m_groupCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.m_groupIdList = new int[this.m_groupCount];
                this.m_groupNameList = new String[this.m_groupCount];
                this.m_groupNameKanaList = new String[this.m_groupCount];
                this.m_groupIdCount = new int[this.m_groupCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_groupIdList[count] = result.getInt( "group_id" );
                    this.m_groupNameList[count] = result.getString( "name" );
                    this.m_groupNameKanaList[count] = result.getString( "name_kana" );
                    this.m_groupIdCount[count] = result.getInt( "count" );

                    count++;
                }
            }
            ret = true;
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[SearchHotelChain_M2.getChainList()] Exception=" + e.toString() );
            throw e;

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
