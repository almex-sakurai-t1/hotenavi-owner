/*
 * @(#)MasterQuestionData.java 1.00 2008/05/14 Copyright (C) ALMEX Inc. 2007 アンケート質問取得クラス
 */

package jp.happyhotel.others;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMapSpot;
import jp.happyhotel.data.DataMasterSpot;
import jp.happyhotel.search.SearchEngineBasic;

/**
 * 地図スポット取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2010/04/28
 */
public class MapSpot implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -3308342896691422306L;
    int                       spotCount;
    int                       spotAllCount;
    int[]                     localIdList;
    int[]                     prefIdList;
    int[]                     spotIdList;
    DataMasterSpot            masterSpot;
    DataMapSpot[]             mapSpot;

    /**
     * データを初期化します。
     */
    public MapSpot()
    {
        spotCount = 0;
        spotAllCount = 0;
        localIdList = null;
        prefIdList = null;
        spotIdList = null;
    }

    /** スポット件数取得 **/
    public int getCount()
    {
        return(spotCount);
    }

    /** スポット件数取得 **/
    public int getAllCount()
    {
        return(spotAllCount);
    }

    /** スポットマスタ取得 **/
    public DataMasterSpot getMasterSpot()
    {
        return(masterSpot);
    }

    /** 地図スポット取得 **/
    public DataMapSpot[] getMapSpotInfo()
    {
        return(mapSpot);
    }

    public int[] getLocalIdList()
    {
        return(localIdList);
    }

    public int[] getPrefIdList()
    {
        return(prefIdList);
    }

    public int[] getSpotIdList()
    {
        return(spotIdList);
    }

    /**
     * 地図スポットを取得する
     * 
     * @param spotId スポットID
     * @param prefId 都道府県ID(0:全都道府県取得)
     * @param recommendFlag おすすめフラグ(1:おすすめのみ取得)
     * @param countNum 取得件数
     * @param pageNum ページ番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMapSpot(int spotId, int prefId, int recommendFlag, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( spotId < 0 )
        {
            return(false);
        }

        /* 任意のページ、任意の件数を取得するSQL */
        query = "SELECT * FROM hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        // 都道府県の指定がある場合
        if ( prefId > 0 )
        {
            query = query + " AND hh_map_spot.pref_id = ?";
        }
        // おすすめ表示
        if ( recommendFlag > 0 )
        {
            query = query + " AND hh_map_spot.recommend_flag >= 1";
        }
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        // おすすめ表示の場合はrecommend_flagでソート
        if ( recommendFlag > 0 )
        {
            query = query + " ORDER BY hh_map_spot.recommend_flag";
        }
        else
        {
            query = query + " ORDER BY hh_map_spot.disp_index";
        }
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            if ( prefId > 0 )
            {
                prestate.setInt( 2, prefId );
            }
            ret = getMapSpotSub( prestate );
            if ( ret != false )
            {
                this.masterSpot = new DataMasterSpot();
                this.masterSpot.getDataByLimit( spotId );
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpot()] Exception=" + e.toString() );
        }
        finally
        {
            prestate = null;
        }

        /* 全件数を取得するSQL */
        query = "SELECT * FROM hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        // 都道府県の指定がある場合
        if ( prefId > 0 )
        {
            query = query + " AND hh_map_spot.pref_id = ?";
        }
        // おすすめのみ表示する
        if ( recommendFlag > 0 )
        {
            query = query + " AND hh_map_spot.recommend_flag >= 1";
        }
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            if ( prefId > 0 )
            {
                prestate.setInt( 2, prefId );
            }
            // 全件数を取得
            ret = getMapSpotCountSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.error( "Exception in getHotelList =" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 地図スポットのデータをセット
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMapSpotSub(PreparedStatement prestate)
    {
        int count;
        ResultSet result = null;
        try
        {
            result = prestate.executeQuery();
            count = 0;

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.spotCount = result.getRow();
                }
                this.mapSpot = new DataMapSpot[this.spotCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.mapSpot[count] = new DataMapSpot();
                    this.mapSpot[count].setData( result );
                    count++;
                }
            }
            else
            {
                this.spotCount = 0;
            }
            Logging.info( "count:" + this.spotCount );
        }
        catch ( Exception e )
        {
            Logging.info( "[MapSpot.getMapSpotSub()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * 地図スポットの全件数をセット
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    private boolean getMapSpotCountSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.spotAllCount = result.getRow();
                }
            }
            else
            {
                this.spotAllCount = 0;
            }
            Logging.info( "count:" + this.spotCount );
        }
        catch ( Exception e )
        {
            Logging.info( "[MapSpot.getMapSpotCountSub()] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result );
        }
        return(true);
    }

    /**
     * 地図スポットを取得する
     * 
     * @param spotId スポットID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getMapSpotBySeq(int spotId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( spotId < 0 )
        {
            return(false);
        }

        query = "SELECT * FROM hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.seq = ?";
        query = query + " ORDER BY hh_map_spot.disp_index";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            prestate.setInt( 2, seq );
            ret = getMapSpotSub( prestate );
            if ( ret != false )
            {
                this.masterSpot = new DataMasterSpot();
                if ( this.mapSpot.length > 0 )
                {
                    this.masterSpot.getData( this.mapSpot[0].getSpotId() );
                }
            }
            // 全件数に件数を代入
            this.spotAllCount = this.spotCount;
            if ( this.spotAllCount == 0 )
            {
                ret = false;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpotBySeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 地方IDごとに地図スポットの件数を取得
     * 
     * @param spotId スポットID
     * @param localId 地方ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSpotIdListLocal(int spotId, int localId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        SearchEngineBasic seb;

        //
        seb = new SearchEngineBasic();
        seb.getLocalList( localId, 0 );
        if ( seb.getMasterLocalCount() > 0 )
        {
            this.localIdList = new int[seb.getMasterLocalCount()];
            this.spotIdList = new int[seb.getMasterLocalCount()];
            for( int i = 0 ; i < seb.getMasterLocalCount() ; i++ )
            {
                this.localIdList[i] = seb.getMasterLocal()[i].getLocalId();
                // 初期化して0を代入
                this.spotIdList[i] = 0;
            }
        }
        seb = null;

        if ( spotId < 0 )
        {
            return(false);
        }

        query = "SELECT hh_master_pref.local_id, COUNT(*) AS count FROM hh_master_pref, hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.pref_id = hh_master_pref.pref_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        if ( localId > 0 )
        {
            query = query + " AND hh_master_pref.local_id = ?";
        }
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " GROUP BY hh_master_pref.local_id";
        query = query + " ORDER BY hh_master_pref.local_id";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            if ( localId > 0 )
            {
                prestate.setInt( 2, localId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    for( int i = 0 ; i < this.localIdList.length ; i++ )
                    {
                        // 一致した地方のデータ件数をセット
                        if ( this.localIdList[i] == result.getInt( "local_id" ) )
                        {
                            this.spotIdList[i] = result.getInt( "count" );
                            break;
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpotBySeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        if ( this.localIdList.length > 0 )
        {
            ret = true;
        }

        return(ret);
    }

    /**
     * 都道府県IDごとに地図スポットの件数を取得
     * 
     * @param spotId スポットID
     * @param localId 地方ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSpotIdListPref(int spotId, int localId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        SearchEngineBasic seb;

        if ( (spotId < 0) || (localId <= 0) )
        {
            return(false);
        }

        //
        seb = new SearchEngineBasic();
        seb.getPrefListByLocal( localId, 0 );
        if ( seb.getMasterPrefCount() > 0 )
        {
            this.prefIdList = new int[seb.getMasterPrefCount()];
            this.spotIdList = new int[seb.getMasterPrefCount()];
            for( int i = 0 ; i < seb.getMasterPrefCount() ; i++ )
            {
                this.prefIdList[i] = seb.getMasterPref()[i].getPrefId();
                // 初期化して0を代入
                this.spotIdList[i] = 0;
            }
        }
        seb = null;

        query = "SELECT hh_master_pref.pref_id, COUNT(*) AS count FROM hh_master_pref, hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.pref_id = hh_master_pref.pref_id";
        query = query + " AND hh_map_spot.spot_id = ?";
        if ( localId > 0 )
        {
            query = query + " AND hh_master_pref.local_id = ?";
        }
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " GROUP BY hh_master_pref.pref_id";
        query = query + " ORDER BY hh_master_pref.local_id, hh_master_pref.pref_id";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            if ( localId > 0 )
            {
                prestate.setInt( 2, localId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    for( int i = 0 ; i < this.prefIdList.length ; i++ )
                    {
                        // 一致した都道府県のデータ件数をセット
                        if ( this.prefIdList[i] == result.getInt( "pref_id" ) )
                        {
                            this.spotIdList[i] = result.getInt( "count" );
                            break;
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpotBySeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        if ( this.prefIdList != null && this.prefIdList.length > 0 )
        {
            ret = true;
        }
        return(ret);
    }

    /**
     * TOPページに表示する地図スポットを取得
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getSpotDataByTopDisp()
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        // hh_map_spot.top_disp_flagl=1のデータを取得
        query = "SELECT * FROM hh_map_spot, hh_master_spot";
        query = query + " WHERE hh_map_spot.spot_id = hh_master_spot.spot_id";
        query = query + " AND hh_map_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.start_date <= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_master_spot.end_date >= " + Integer.parseInt( DateEdit.getDate( 2 ) );
        query = query + " AND hh_map_spot.top_disp_flag >= 1";
        query = query + " ORDER BY hh_map_spot.top_disp_flag ";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            ret = getMapSpotSub( prestate );
            if ( ret != false )
            {
                this.masterSpot = new DataMasterSpot();
                if ( this.mapSpot.length > 0 )
                {
                    this.masterSpot.getData( this.mapSpot[0].getSpotId() );
                }
            }
            // 全件数に件数を代入
            this.spotAllCount = this.spotCount;
            if ( this.spotAllCount == 0 )
            {
                ret = false;
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.info( "[MapSpot.getMapSpotBySeq()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }
}
