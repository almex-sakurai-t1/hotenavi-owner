package jp.happyhotel.data;

/*
 * @(#)DataUserPoint.java 1.00 2010/04/28
 * Copyright (C) ALMEX Inc. 2007
 * 地図スポットデータ情報取得クラス
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 地図スポットデータ情報取得
 * 
 * @author S.Tashiro
 * @version 1.00 2010/04/28
 */
public class DataMapSpot implements Serializable
{
    private static final long serialVersionUID = -6371601675690443781L;
    private int               spotId;
    private int               seq;
    private String            name;
    private int               prefId;
    private String            prefName;
    private String            address1;
    private String            address2;
    private String            lat;
    private String            lon;
    private int               scale;
    private String            info;
    private String            detailInfo;
    private int               recommendFlag;
    private int               startDate;
    private int               endDate;
    private int               dispIndex;
    private int               topDispFlag;

    public DataMapSpot()
    {
        spotId = 0;
        seq = 0;
        name = "";
        prefId = 0;
        prefName = "";
        address1 = "";
        address2 = "";
        lat = "";
        lon = "";
        scale = 0;
        info = "";
        detailInfo = "";
        recommendFlag = 0;
        startDate = 0;
        endDate = 0;
        dispIndex = 0;
        topDispFlag = 0;
    }

    /* getter */
    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public String getDetailInfo()
    {
        return detailInfo;
    }

    public int getDispIndex()
    {
        return dispIndex;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public String getInfo()
    {
        return info;
    }

    public String getLat()
    {
        return lat;
    }

    public String getLon()
    {
        return lon;
    }

    public String getName()
    {
        return name;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public int getRecommendFlag()
    {
        return recommendFlag;
    }

    public int getScale()
    {
        return scale;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getSpotId()
    {
        return spotId;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getTopDispFlag()
    {
        return topDispFlag;
    }

    /* setter */
    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public void setDetailInfo(String detailInfo)
    {
        this.detailInfo = detailInfo;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public void setLon(String lon)
    {
        this.lon = lon;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

    public void setRecommendFlag(int recommendFlag)
    {
        this.recommendFlag = recommendFlag;
    }

    public void setScale(int scale)
    {
        this.scale = scale;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setSpotId(int spotId)
    {
        this.spotId = spotId;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setTopDispFlag(int topDispFlag)
    {
        this.topDispFlag = topDispFlag;
    }

    /**
     * 地図スポットデータ取得
     * 
     * @param spotId スポットID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int spotId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_map_spot WHERE spot_id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, spotId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.spotId = result.getInt( "spot_id" );
                    this.seq = result.getInt( "seq" );
                    this.name = result.getString( "name" );
                    this.prefId = result.getInt( "pref_id" );
                    this.prefName = result.getString( "pref_name" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.lat = result.getString( "lat" );
                    this.lon = result.getString( "lon" );
                    this.scale = result.getInt( "scale" );
                    this.info = result.getString( "info" );
                    this.detailInfo = result.getString( "detail_info" );
                    this.recommendFlag = result.getInt( "recommend_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.topDispFlag = result.getInt( "top_disp_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMapSpot.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 地図スポットデータ設定
     * 
     * @param result 地図スポットデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.spotId = result.getInt( "spot_id" );
                this.seq = result.getInt( "seq" );
                this.name = result.getString( "name" );
                this.prefId = result.getInt( "pref_id" );
                this.prefName = result.getString( "pref_name" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.lat = result.getString( "lat" );
                this.lon = result.getString( "lon" );
                this.scale = result.getInt( "scale" );
                this.info = result.getString( "info" );
                this.detailInfo = result.getString( "detail_info" );
                this.recommendFlag = result.getInt( "recommend_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.dispIndex = result.getInt( "disp_index" );
                this.topDispFlag = result.getInt( "top_disp_flag" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMapSpot.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * 地図スポットデータ情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_map_spot SET ";
        query = query + " spot_id = ?,";
        query = query + " seq = ?,";
        query = query + " name = ?,";
        query = query + " pref_id = ?,";
        query = query + " pref_name = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " lat = ?,";
        query = query + " lon = ?,";
        query = query + " scale = ?,";
        query = query + " info = ?,";
        query = query + " detail_info = ?,";
        query = query + " recommend_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " disp_index = ?,";
        query = query + " top_disp_flag = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.spotId );
            prestate.setInt( 2, this.seq );
            prestate.setString( 3, this.name );
            prestate.setInt( 4, this.prefId );
            prestate.setString( 5, this.prefName );
            prestate.setString( 6, this.address1 );
            prestate.setString( 7, this.address2 );
            prestate.setString( 8, this.lat );
            prestate.setString( 9, this.lon );
            prestate.setInt( 10, this.scale );
            prestate.setString( 11, this.info );
            prestate.setString( 12, this.detailInfo );
            prestate.setInt( 13, this.recommendFlag );
            prestate.setInt( 14, this.startDate );
            prestate.setInt( 15, this.endDate );
            prestate.setInt( 16, this.dispIndex );
            prestate.setInt( 17, this.topDispFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMapSpot.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 地図スポットデータ情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param spotId スポットID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int spotId, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_map_spot SET ";
        query = query + " name = ?,";
        query = query + " pref_id = ?,";
        query = query + " pref_name = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " lat = ?,";
        query = query + " lon = ?,";
        query = query + " scale = ?,";
        query = query + " info = ?,";
        query = query + " detail_info = ?,";
        query = query + " recommend_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " disp_index = ?,";
        query = query + " top_disp_flag = ?";
        query = query + " WHERE spot_id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.name );
            prestate.setInt( 2, this.prefId );
            prestate.setString( 3, this.prefName );
            prestate.setString( 4, this.address1 );
            prestate.setString( 5, this.address2 );
            prestate.setString( 6, this.lat );
            prestate.setString( 7, this.lon );
            prestate.setInt( 8, this.scale );
            prestate.setString( 9, this.info );
            prestate.setString( 10, this.detailInfo );
            prestate.setInt( 11, this.recommendFlag );
            prestate.setInt( 12, this.startDate );
            prestate.setInt( 13, this.endDate );
            prestate.setInt( 14, this.dispIndex );
            prestate.setInt( 15, this.topDispFlag );
            prestate.setInt( 16, spotId );
            prestate.setInt( 17, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMapSpot.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

}
