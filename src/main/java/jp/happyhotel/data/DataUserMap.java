/*
 * @(#)DataUserMap.java
 * 1.00 2009/07/21 Copyright (C) ALMEX Inc. 2007
 * ユーザー地図情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザー地図情報データ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/07/21
 */
public class DataUserMap implements Serializable
{
    private static final long serialVersionUID = -4585645439526206154L;

    private String            userId;
    private String            coordinate;
    private String            coordinateUL;
    private String            coordinateUR;
    private String            coordinateDL;
    private String            coordinateDR;
    private String            scale;
    private String            line;
    private String            point;
    private String            position;
    private String            contentType;
    private String            address;
    private byte[]            image;
    private byte[]            image2;

    /**
     * データを初期化します。
     */
    public DataUserMap()
    {
        userId = "";
        coordinate = "";
        coordinateUL = "";
        coordinateUR = "";
        coordinateDL = "";
        coordinateDR = "";
        scale = "";
        line = "";
        point = "";
        position = "";
        contentType = "";
        address = "";
        image = null;
        image2 = null;
    }

    /**
     * @return ユーザーID
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @return 中心地の座標
     */
    public String getCoordinate()
    {
        return coordinate;
    }

    /**
     * @return 左上の座標
     */
    public String getCoordinateUL()
    {
        return coordinateUL;
    }

    /**
     * @return 右上の座標
     */
    public String getCoordinateUR()
    {
        return coordinateUR;
    }

    /**
     * @return 左下の座標
     */
    public String getCoordinateDL()
    {
        return coordinateDL;
    }

    /**
     * @return 右下の座標
     */
    public String getCoordinateDR()
    {
        return coordinateDR;
    }

    /**
     * @return 縮尺
     */
    public String getScale()
    {
        return scale;
    }

    /**
     * @return 表示するルート
     */
    public String getLine()
    {
        return line;
    }

    /**
     * @return ルートの終始点
     */
    public String getPoint()
    {
        return point;
    }

    /**
     * @return 描画コンテンツ
     */
    public String getPosition()
    {
        return position;
    }

    /**
     * @return コンテントタイプ
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * @return 住所
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @return 画像
     */
    public byte[] getImage()
    {
        return image;
    }

    /**
     * @return 画像2
     */
    public byte[] getImage2()
    {
        return image2;
    }

    /**
     * @param ユーザーID
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * @param 中心地の座標
     */
    public void setCoordinate(String coordinate)
    {
        this.coordinate = coordinate;
    }

    /**
     * @param 左上の座標
     */
    public void setCoordinateUL(String coordinateUL)
    {
        this.coordinateUL = coordinateUL;
    }

    /**
     * @param 右上の座標
     */
    public void setCoordinateUR(String coordinateUR)
    {
        this.coordinateUR = coordinateUR;
    }

    /**
     * @param 左下の座標
     */
    public void setCoordinateDL(String coordinateDL)
    {
        this.coordinateDL = coordinateDL;
    }

    /**
     * @param 右下の座標
     */
    public void setCoordinateDR(String coordinateDR)
    {
        this.coordinateDR = coordinateDR;
    }

    /**
     * @param 縮尺
     */
    public void setScale(String scale)
    {
        this.scale = scale;
    }

    /**
     * @param 表示するルート
     */
    public void setLine(String line)
    {
        this.line = line;
    }

    /**
     * @param ルートの終始点
     */
    public void setPoint(String point)
    {
        this.point = point;
    }

    /**
     * @param 描画コンテンツ
     */
    public void setPosition(String position)
    {
        this.position = position;
    }

    /**
     * @param コンテントタイプ
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    /**
     * @param 住所
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * @param 画像
     */
    public void setImage(byte[] image)
    {
        this.image = image;
    }

    /**
     * @param 画像1
     */
    public void setImage2(byte[] image2)
    {
        this.image2 = image2;
    }

    /**
     * ユーザー地図情報取得
     * 
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_map WHERE user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.coordinate = result.getString( "coordinate" );
                    this.coordinateUL = result.getString( "coordinate_ul" );
                    this.coordinateUR = result.getString( "coordinate_ur" );
                    this.coordinateDL = result.getString( "coordinate_dl" );
                    this.coordinateDR = result.getString( "coordinate_dr" );
                    this.scale = result.getString( "scale" );
                    this.line = result.getString( "line" );
                    this.point = result.getString( "point" );
                    this.position = result.getString( "position" );
                    this.image = result.getBytes( "image" );
                    this.image2 = result.getBytes( "image2" );
                    this.contentType = result.getString( "content_type" );
                    this.address = result.getString( "address" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMap.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( userId.compareTo( this.userId ) == 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ユーザー地図情報取得
     * 
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataWithoutImage(String userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT user_id, coordinate, coordinate_ul, coordinate_ur, coordinate_dl, coordinate_dr, scale, line, point, position, content_type, address" +
                " FROM hh_user_map WHERE user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.coordinate = result.getString( "coordinate" );
                    this.coordinateUL = result.getString( "coordinate_ul" );
                    this.coordinateUR = result.getString( "coordinate_ur" );
                    this.coordinateDL = result.getString( "coordinate_dl" );
                    this.coordinateDR = result.getString( "coordinate_dr" );
                    this.scale = result.getString( "scale" );
                    this.line = result.getString( "line" );
                    this.point = result.getString( "point" );
                    this.position = result.getString( "position" );
                    this.image = result.getBytes( "image" );
                    this.image2 = result.getBytes( "image2" );
                    this.contentType = result.getString( "content_type" );
                    this.address = result.getString( "address" );

                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMap.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( userId.compareTo( this.userId ) == 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ユーザー地図情報取得
     * 
     * @param result ユーザー地図情報取得データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.userId = result.getString( "user_id" );
                this.coordinate = result.getString( "coordinate" );
                this.coordinateUL = result.getString( "coordinate_ul" );
                this.coordinateUR = result.getString( "coordinate_ur" );
                this.coordinateDL = result.getString( "coordinate_dl" );
                this.coordinateDR = result.getString( "coordinate_dr" );
                this.scale = result.getString( "scale" );
                this.line = result.getString( "line" );
                this.point = result.getString( "point" );
                this.position = result.getString( "position" );
                this.image = result.getBytes( "image" );
                this.image2 = result.getBytes( "image2" );
                this.contentType = result.getString( "content_type" );
                this.address = result.getString( "address" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMap.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザー地図情報取得
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

        query = "INSERT hh_user_map SET ";
        query = query + " user_id = ?,";
        query = query + " coordinate = ?,";
        query = query + " coordinate_ul = ?,";
        query = query + " coordinate_ur = ?,";
        query = query + " coordinate_dl = ?,";
        query = query + " coordinate_dr = ?,";
        query = query + " scale = ?,";
        query = query + " line = ?,";
        query = query + " point = ?,";
        query = query + " position = ?,";
        query = query + " image = ?,";
        query = query + " image2 = ?,";
        query = query + " content_type = ?,";
        query = query + " address = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.coordinate );
            prestate.setString( 3, this.coordinateUL );
            prestate.setString( 4, this.coordinateUR );
            prestate.setString( 5, this.coordinateDL );
            prestate.setString( 6, this.coordinateDR );
            prestate.setString( 7, this.scale );
            prestate.setString( 8, this.line );
            prestate.setString( 9, this.point );
            prestate.setString( 10, this.position );
            prestate.setBytes( 11, this.image );
            prestate.setBytes( 12, this.image2 );
            prestate.setString( 13, this.contentType );
            prestate.setString( 14, this.address );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMap.insertData] Exception=" + e.toString() );
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
     * ユーザー地図情報取得
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_user_map SET ";
        query = query + " coordinate = ?,";
        query = query + " coordinate_ul = ?,";
        query = query + " coordinate_ur = ?,";
        query = query + " coordinate_dl = ?,";
        query = query + " coordinate_dr = ?,";
        query = query + " scale = ?,";
        query = query + " line = ?,";
        query = query + " point = ?,";
        query = query + " position = ?,";
        query = query + " image = ?,";
        query = query + " image2 = ?,";
        query = query + " content_type = ?,";
        query = query + " address = ?";
        query = query + " WHERE user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.coordinate );
            prestate.setString( 2, this.coordinateUL );
            prestate.setString( 3, this.coordinateUR );
            prestate.setString( 4, this.coordinateDL );
            prestate.setString( 5, this.coordinateDR );
            prestate.setString( 6, this.scale );
            prestate.setString( 7, this.line );
            prestate.setString( 8, this.point );
            prestate.setString( 9, this.position );
            prestate.setBytes( 10, this.image );
            prestate.setBytes( 11, this.image2 );
            prestate.setString( 12, this.contentType );
            prestate.setString( 13, this.address );
            prestate.setString( 14, userId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserMap.updateData] Exception=" + e.toString() );
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
