/*
 * @(#)DataHotelMap.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテル地図関連情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテル地図関連情報データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/16
 */
public class DataHotelMap implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -8096601015797370638L;

    private int               id;
    private int               seq;
    private String            mapId;
    private int               timeCar;
    private int               timeWalk;
    private int               distance;
    private int               dispFlag;
    private int               nearestFlag;
    private String            nearestId;

    /**
     * データを初期化します。
     */
    public DataHotelMap()
    {
        id = 0;
        seq = 0;
        mapId = "";
        timeCar = 0;
        timeWalk = 0;
        distance = 0;
        dispFlag = 0;
        nearestFlag = 0;
        nearestId = "";
    }

    public int getDispFlag()
    {
        return dispFlag;
    }

    public int getDistance()
    {
        return distance;
    }

    public int getNearestFlag()
    {
        return nearestFlag;
    }

    public String getNearestId()
    {
        return nearestId;
    }

    public int getTimeCar()
    {
        return timeCar;
    }

    public int getTimeWalk()
    {
        return timeWalk;
    }

    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public void setNearestFlag(int nearestFlag)
    {
        this.nearestFlag = nearestFlag;
    }

    public void setNearestId(String nearestId)
    {
        this.nearestId = nearestId;
    }

    public void setTimeCar(int timeCar)
    {
        this.timeCar = timeCar;
    }

    public void setTimeWalk(int timeWalk)
    {
        this.timeWalk = timeWalk;
    }

    public int getId()
    {
        return id;
    }

    public String getMapId()
    {
        return mapId;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMapId(String mapId)
    {
        this.mapId = mapId;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    /**
     * ホテル地図関連情報データ取得
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_map WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.mapId = result.getString( "map_id" );
                    this.timeCar = result.getInt( "time_car" );
                    this.timeWalk = result.getInt( "time_walk" );
                    this.distance = result.getInt( "distance" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.nearestFlag = result.getInt( "nearest_flag" );
                    this.nearestId = result.getString( "nearest_id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMap.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル地図関連情報データ取得
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, String mapId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_map WHERE id = ? AND map_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, mapId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.mapId = result.getString( "map_id" );
                    this.timeCar = result.getInt( "time_car" );
                    this.timeWalk = result.getInt( "time_walk" );
                    this.distance = result.getInt( "distance" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.nearestFlag = result.getInt( "nearest_flag" );
                    this.nearestId = result.getString( "nearest_id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMap.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテル地図関連情報データ設定
     * 
     * @param result ホテル地図関連情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.mapId = result.getString( "map_id" );
                this.timeCar = result.getInt( "time_car" );
                this.timeWalk = result.getInt( "time_walk" );
                this.distance = result.getInt( "distance" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.nearestFlag = result.getInt( "nearest_flag" );
                this.nearestId = result.getString( "nearest_id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMap.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ホテル地図関連情報データ追加
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

        query = "INSERT hh_hotel_map SET ";
        query = query + " id = ?,";
        query = query + " seq = 0,";
        query = query + " map_id = ?,";
        query = query + " time_car = ?,";
        query = query + " time_walk = ?,";
        query = query + " distance = ?,";
        query = query + " disp_flag = ?,";
        query = query + " nearest_flag = ?,";
        query = query + " nearest_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.mapId );
            prestate.setInt( 3, this.timeCar );
            prestate.setInt( 4, this.timeWalk );
            prestate.setInt( 5, this.distance );
            prestate.setInt( 6, this.dispFlag );
            prestate.setInt( 7, this.nearestFlag );
            prestate.setString( 8, this.nearestId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMap.insertData] Exception=" + e.toString() );
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
     * ホテル地図関連情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_map SET ";
        query = query + " map_id = ?,";
        query = query + " time_car = ?,";
        query = query + " time_walk = ?,";
        query = query + " distance = ?,";
        query = query + " disp_flag = ?,";
        query = query + " nearest_flag = ?,";
        query = query + " nearest_id = ?";
        query = query + " WHERE id = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.mapId );
            prestate.setInt( 2, this.timeCar );
            prestate.setInt( 3, this.timeWalk );
            prestate.setInt( 4, this.distance );
            prestate.setInt( 5, this.dispFlag );
            prestate.setInt( 6, this.nearestFlag );
            prestate.setString( 7, this.nearestId );
            prestate.setInt( 8, id );
            prestate.setInt( 9, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMap.updateData] Exception=" + e.toString() );
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
