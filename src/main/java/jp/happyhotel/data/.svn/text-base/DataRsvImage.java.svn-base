package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataRsvImage implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID   = 7819590617918577187L;
    private int               id                 = 0;
    private int               image_id           = 0;
    private String            original_file_name = "";
    private String            upload_file_name   = "";
    private int               original_file_size = 0;
    private int               original_width     = 0;
    private int               original_height    = 0;
    private String            release_file_name  = "";
    private int               release_file_size  = 0;
    private int               release_width      = 0;
    private int               release_height     = 0;
    private String            upload_hotelid     = "";
    private int               upload_user_id     = 0;
    private int               upload_date        = 0;
    private int               upload_time        = 0;
    private String            message            = "";
    private int               status             = 1;
    private int               user_id            = 0;
    private int               last_update        = 0;
    private int               last_uptime        = 0;
    private String            ownerHotelId       = "";

    /**
     * 画像データ登録
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection conn)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_image SET " +
                "id = ?, " +
                "original_file_name = ?, " +
                "upload_file_name = ?, " +
                "original_file_size = ?, " +
                "original_width = ?, " +
                "original_height = ?, " +
                "release_file_name = ?, " +
                "release_file_size = ?, " +
                "release_width = ?, " +
                "release_height = ?, " +
                "upload_hotelid = ?, " +
                "upload_user_id = ?, " +
                "upload_date = CURDATE()+0, " +
                "upload_time = CURTIME()+0, " +
                "message = ?, " +
                "status = ?, " +
                "hotel_id = ?, " +
                "user_id = ?, " +
                "last_update = CURDATE()+0, last_uptime = CURTIME()+0";

        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.original_file_name );
            prestate.setString( 3, this.upload_file_name );
            prestate.setInt( 4, this.original_file_size );
            prestate.setInt( 5, this.original_width );
            prestate.setInt( 6, this.original_height );
            prestate.setString( 7, this.release_file_name );
            prestate.setInt( 8, this.release_file_size );
            prestate.setInt( 9, this.release_width );
            prestate.setInt( 10, this.release_height );
            prestate.setString( 11, this.upload_hotelid );
            prestate.setInt( 12, this.upload_user_id );
            prestate.setString( 13, this.message );
            prestate.setInt( 14, this.status );
            prestate.setString( 15, this.ownerHotelId );
            prestate.setInt( 16, this.user_id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 画像データ検索
     * 
     * @param id ホテルID
     * @param uploadfilename アップロードファイル名
     * 
     */
    public void getData(int id, String uploadfilename)
    {
        String query;
        PreparedStatement prestate = null;
        ResultSet resultSet = null;
        Connection conn = null;

        query = "select id, image_id, original_file_name, upload_file_name, original_file_size, original_width, original_height, release_file_name, release_file_size, " +
                "release_width, release_height, upload_hotelid, upload_user_id, upload_date, upload_time, message, status, user_id, last_update, last_uptime, hotel_id from hh_rsv_image " +
                "where id = ? AND upload_file_name = ?";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, uploadfilename );
            resultSet = prestate.executeQuery();
            if ( resultSet != null && resultSet.next() )
            {
                this.id = resultSet.getInt( "id" );
                this.image_id = resultSet.getInt( "image_id" );
                this.original_file_name = resultSet.getString( "original_file_name" );
                this.upload_file_name = resultSet.getString( "upload_file_name" );
                this.original_file_size = resultSet.getInt( "original_file_size" );
                this.original_width = resultSet.getInt( "original_width" );
                this.original_height = resultSet.getInt( "original_height" );
                this.release_file_name = resultSet.getString( "release_file_name" );
                this.release_file_size = resultSet.getInt( "release_file_size" );
                this.release_width = resultSet.getInt( "release_width" );
                this.release_height = resultSet.getInt( "release_height" );
                this.upload_hotelid = resultSet.getString( "upload_hotelid" );
                this.upload_user_id = resultSet.getInt( "upload_user_id" );
                this.upload_date = resultSet.getInt( "upload_date" );
                this.upload_time = resultSet.getInt( "upload_time" );
                this.message = resultSet.getString( "message" );
                this.status = resultSet.getInt( "status" );
                this.user_id = resultSet.getInt( "user_id" );
                this.last_update = resultSet.getInt( "last_update" );
                this.last_uptime = resultSet.getInt( "last_uptime" );
                this.ownerHotelId = resultSet.getString( "hotel_id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( resultSet );
        }

    }

    /**
     * 画像データ検索
     * 
     * @param id ホテルID
     * @param imageid 画像ID
     * 
     */
    public void getData(Connection conn, int id, int imageid)
    {
        String query;
        PreparedStatement prestate = null;
        ResultSet resultSet = null;

        query = "select id, image_id, original_file_name, upload_file_name, original_file_size, original_width, original_height, release_file_name, release_file_size, " +
                "release_width, release_height, upload_hotelid, upload_user_id, upload_date, upload_time, message, status, user_id, last_update, last_uptime, hotel_id from hh_rsv_image " +
                "where id = ? AND image_id = ?";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, imageid );
            resultSet = prestate.executeQuery();
            if ( resultSet != null && resultSet.next() )
            {
                this.id = resultSet.getInt( "id" );
                this.image_id = resultSet.getInt( "image_id" );
                this.original_file_name = resultSet.getString( "original_file_name" );
                this.upload_file_name = resultSet.getString( "upload_file_name" );
                this.original_file_size = resultSet.getInt( "original_file_size" );
                this.original_width = resultSet.getInt( "original_width" );
                this.original_height = resultSet.getInt( "original_height" );
                this.release_file_name = resultSet.getString( "release_file_name" );
                this.release_file_size = resultSet.getInt( "release_file_size" );
                this.release_width = resultSet.getInt( "release_width" );
                this.release_height = resultSet.getInt( "release_height" );
                this.upload_hotelid = resultSet.getString( "upload_hotelid" );
                this.upload_user_id = resultSet.getInt( "upload_user_id" );
                this.upload_date = resultSet.getInt( "upload_date" );
                this.upload_time = resultSet.getInt( "upload_time" );
                this.message = resultSet.getString( "message" );
                this.status = resultSet.getInt( "status" );
                this.user_id = resultSet.getInt( "user_id" );
                this.last_update = resultSet.getInt( "last_update" );
                this.last_uptime = resultSet.getInt( "last_uptime" );
                this.ownerHotelId = resultSet.getString( "hotel_id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( resultSet );
        }

        return;
    }

    /**
     * 画像データ　ステータス更新(全要素)
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateStatusAll(Connection conn)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_image SET " +
                "original_file_name = ?, upload_file_name = ?, original_file_size = ?, original_width = ?, original_height = ?, release_file_name = ?, " +
                "release_file_size = ?, release_width = ?, release_height = ?, upload_hotelid = ?, upload_user_id = ?, upload_date = ?, upload_time = ?, " +
                "message = ?, status = ?, user_id = ?, last_update = CURDATE()+0, last_uptime = CURTIME()+0, hotel_id = ? " +
                "WHERE id = ? AND image_id = ?";
        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.original_file_name );
            prestate.setString( 2, this.upload_file_name );
            prestate.setInt( 3, this.original_file_size );
            prestate.setInt( 4, this.original_width );
            prestate.setInt( 5, this.original_height );
            prestate.setString( 6, this.release_file_name );
            prestate.setInt( 7, this.release_file_size );
            prestate.setInt( 8, this.release_width );
            prestate.setInt( 9, this.release_height );
            prestate.setString( 10, this.upload_hotelid );
            prestate.setInt( 11, this.upload_user_id );
            prestate.setInt( 12, this.upload_date );
            prestate.setInt( 13, this.upload_time );
            prestate.setString( 14, this.message );
            prestate.setInt( 15, this.status );
            prestate.setInt( 16, this.user_id );
            prestate.setString( 17, this.ownerHotelId );
            prestate.setInt( 18, this.id );
            prestate.setInt( 19, this.image_id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.updateStatusAll] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /**
     * 画像データ　ステータス更新
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateStatus(Connection conn)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_image SET " +
                "status = ?, hotel_id = ?, user_id = ?, last_update = CURDATE()+0, last_uptime = CURTIME()+0 " +
                "WHERE id = ? AND image_id = ?";
        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.status );
            prestate.setString( 2, this.ownerHotelId );
            prestate.setInt( 3, this.user_id );
            prestate.setInt( 4, this.id );
            prestate.setInt( 5, this.image_id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.updateStatus] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getImage_id()
    {
        return image_id;
    }

    public void setImage_id(int image_id)
    {
        this.image_id = image_id;
    }

    public String getOriginal_file_name()
    {
        return original_file_name;
    }

    public void setOriginal_file_name(String original_file_name)
    {
        this.original_file_name = original_file_name;
    }

    public String getUpload_file_name()
    {
        return upload_file_name;
    }

    public void setUpload_file_name(String upload_file_name)
    {
        this.upload_file_name = upload_file_name;
    }

    public int getOriginal_file_size()
    {
        return original_file_size;
    }

    public void setOriginal_file_size(int original_file_size)
    {
        this.original_file_size = original_file_size;
    }

    public int getOriginal_width()
    {
        return original_width;
    }

    public void setOriginal_width(int original_width)
    {
        this.original_width = original_width;
    }

    public int getOriginal_height()
    {
        return original_height;
    }

    public void setOriginal_height(int original_height)
    {
        this.original_height = original_height;
    }

    public String getRelease_file_name()
    {
        return release_file_name;
    }

    public void setRelease_file_name(String release_file_name)
    {
        this.release_file_name = release_file_name;
    }

    public int getRelease_file_size()
    {
        return release_file_size;
    }

    public void setRelease_file_size(int release_file_size)
    {
        this.release_file_size = release_file_size;
    }

    public int getRelease_width()
    {
        return release_width;
    }

    public void setRelease_width(int release_width)
    {
        this.release_width = release_width;
    }

    public int getRelease_height()
    {
        return release_height;
    }

    public void setRelease_height(int release_height)
    {
        this.release_height = release_height;
    }

    public String getUpload_hotelid()
    {
        return upload_hotelid;
    }

    public void setUpload_hotelid(String upload_hotelid)
    {
        this.upload_hotelid = upload_hotelid;
    }

    public int getUpload_user_id()
    {
        return upload_user_id;
    }

    public void setUpload_user_id(int upload_user_id)
    {
        this.upload_user_id = upload_user_id;
    }

    public int getUpload_date()
    {
        return upload_date;
    }

    public void setUpload_date(int upload_date)
    {
        this.upload_date = upload_date;
    }

    public int getUpload_time()
    {
        return upload_time;
    }

    public void setUpload_time(int upload_time)
    {
        this.upload_time = upload_time;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    public int getLast_update()
    {
        return last_update;
    }

    public void setLast_update(int last_update)
    {
        this.last_update = last_update;
    }

    public int getLast_uptime()
    {
        return last_uptime;
    }

    public void setLast_uptime(int last_uptime)
    {
        this.last_uptime = last_uptime;
    }

    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

}
