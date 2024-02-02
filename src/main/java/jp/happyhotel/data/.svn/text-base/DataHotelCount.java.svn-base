package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DBSync;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * hh_hotel_count（ホテル検索件数表示用）取得クラス
 * 
 * @author sakurai-t1
 * @version 1.00 2018/1/24
 */
public class DataHotelCount implements Serializable
{
    /**
     * 
     */
    private static final long  serialVersionUID = 7560411690377716768L;
    public static final String TABLE            = "hh_hotel_count";
    private int                id;                                     // ホテルID (hh_hotel_basic id<100000000,pref_id<>0,kind<=7 のホテル)
    private int                sortKey;                                // hh_hotel_sort : collect_date=0 のall_point 集計後に更新
    private int                prefId;                                 // hh_hotel_basic : pref_id <>0 より更新
    private int                mileFlag;                               // 1:マイル加盟店 (hh_hotel_newhappieにおいて date_start<=本日 AND date_start !=0)
    private int                couponFlag;                             // 1:クーポン有 (hh_hotel_coupon に　data_start>=本日 AND end_date <=本日のデータあり）
    private int                reserveFlag;                            // 1:予約有(hh_rsv_reserve_basicにおいて　rsv_date_start<=本日 AND sales_flag=1)
    private int                stationFlag;                            // 1:駅検索対象(hh_map_point のclass_code LIKE "5%" の地点が、hh_hotel_mapに登録あり)
    private int                icFlag;                                 // 1:IC検索対象(hh_map_point のclass_code LIKE "4%" の地点が、hh_hotel_mapに登録あり)
    private int                areaFlag;                               // 1:エリア検索対象(hh_hotel_area に　1件以上登録あり)
    private String             hotelImg;                               // あればcommon/images/HB/(id)n.jpg。もしくはあればcommon/images/HB/(id)jpg.jpg。両方なければブランク
    private int                hotenaviFlag;                           // 1:ホテナビ検索対象(hh_hotel_basic.urlが登録されていた場合)
    private int                cardlessFlag;                           // 1:カードレス対象(ap_hotel_settingのcustom_flagが1,及びhh_hotel_happie.date_start>=当日)
    private int                lastUpdate;                             // 最終更新日(YYYYMMDD)
    private int                lastUptime;                             // 最終更新時刻(HHMMSS)

    /**
     * データを初期化します。
     */
    public DataHotelCount()
    {
        this.id = 0;
        this.sortKey = 0;
        this.prefId = 0;
        this.mileFlag = 0;
        this.couponFlag = 0;
        this.reserveFlag = 0;
        this.stationFlag = 0;
        this.icFlag = 0;
        this.areaFlag = 0;
        this.hotelImg = "";
        this.hotenaviFlag = 0;
        this.cardlessFlag = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getSortKey()
    {
        return sortKey;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public int getMileFlag()
    {
        return mileFlag;
    }

    public int getCouponFlag()
    {
        return couponFlag;
    }

    public int getReserveFlag()
    {
        return reserveFlag;
    }

    public int getStationFlag()
    {
        return stationFlag;
    }

    public int getIcFlag()
    {
        return icFlag;
    }

    public int getAreaFlag()
    {
        return areaFlag;
    }

    public String getHotelImg()
    {
        return hotelImg;
    }

    public int getHotenaviFlag()
    {
        return hotenaviFlag;
    }

    public int getCardlessFlag()
    {
        return cardlessFlag;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSortKey(int sortKey)
    {
        this.sortKey = sortKey;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setMileFlag(int mileFlag)
    {
        this.mileFlag = mileFlag;
    }

    public void setCouponFlag(int couponFlag)
    {
        this.couponFlag = couponFlag;
    }

    public void setReserveFlag(int reserveFlag)
    {
        this.reserveFlag = reserveFlag;
    }

    public void setStationFlag(int stationFlag)
    {
        this.stationFlag = stationFlag;
    }

    public void setIcFlag(int icFlag)
    {
        this.icFlag = icFlag;
    }

    public void setAreaFlag(int areaFlag)
    {
        this.areaFlag = areaFlag;
    }

    public void setHotelImg(String hotelImg)
    {
        this.hotelImg = hotelImg;
    }

    public void setHotenaviFlag(int hotenaviFlag)
    {
        this.hotenaviFlag = hotenaviFlag;
    }

    public void setCardlessFlag(int cardlessFlag)
    {
        this.cardlessFlag = cardlessFlag;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /****
     * hh_hotel_count（ホテル検索件数表示用）取得
     * 
     * @param id ホテルID (hh_hotel_basic id<100000000,pref_id<>0,kind<=7 のホテル)
     * @return
     */
    public boolean getData(Connection connection, int id)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM hotenavi.hh_hotel_count WHERE id = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCount.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /****
     * hh_hotel_count（ホテル検索件数表示用）取得
     * 
     * @param id ホテルID (hh_hotel_basic id<100000000,pref_id<>0,kind<=7 のホテル)
     * @return
     */
    public boolean getData(int id)
    {
        boolean ret;
        Connection connection = null;
        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            ret = getData( connection, id );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCount.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * hh_hotel_count（ホテル検索件数表示用）設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.sortKey = result.getInt( "sort_key" );
                this.prefId = result.getInt( "pref_id" );
                this.mileFlag = result.getInt( "mile_flag" );
                this.couponFlag = result.getInt( "coupon_flag" );
                this.reserveFlag = result.getInt( "reserve_flag" );
                this.stationFlag = result.getInt( "station_flag" );
                this.icFlag = result.getInt( "ic_flag" );
                this.areaFlag = result.getInt( "area_flag" );
                this.hotelImg = result.getString( "hotel_img" );
                this.hotenaviFlag = result.getInt( "hotenavi_flag" );
                this.cardlessFlag = result.getInt( "cardless_flag" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCount.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * hh_hotel_count（ホテル検索件数表示用）挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT hotenavi.hh_hotel_count SET ";
        query += " id=?";
        // query += ", sort_key=?";
        query += ", pref_id=?";
        query += ", mile_flag=?";
        query += ", reserve_flag=?";
        // query += ", station_flag=?";
        // query += ", ic_flag=?";
        // query += ", area_flag=?";
        // query += ", hotel_img=?";
        query += ", hotenavi_flag=?";
        query += ", cardless_flag=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            // prestate.setInt( i++, this.sortKey );
            prestate.setInt( i++, this.prefId );
            prestate.setInt( i++, this.mileFlag );
            prestate.setInt( i++, this.reserveFlag );
            // prestate.setInt( i++, this.stationFlag );
            // prestate.setInt( i++, this.icFlag );
            // prestate.setInt( i++, this.areaFlag );
            // prestate.setString( i++, this.hotelImg );
            prestate.setInt( i++, this.hotenaviFlag );
            prestate.setInt( i++, this.cardlessFlag );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                Logging.info( "[DataHotelCount.insertDatal]=" + prestate.toString() );
                DBSync.publish( prestate.toString().split( ":", 2 )[1] );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCount.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * hh_hotel_count（ホテル検索件数表示用）挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        boolean ret;
        Connection connection = null;
        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            ret = insertData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCount.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * hh_hotel_count（ホテル検索件数表示用）更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID (hh_hotel_basic id<100000000,pref_id<>0,kind<=7 のホテル)
     * @return
     */
    public boolean updateData(Connection connection, int id)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE hotenavi.hh_hotel_count SET ";
        // query += " sort_key=?";
        // query += ", pref_id=?";
        query += " pref_id=?";
        query += ", mile_flag=?";
        query += ", reserve_flag=?";
        // query += ", station_flag=?";
        // query += ", ic_flag=?";
        // query += ", area_flag=?";
        // query += ", hotel_img=?";
        query += ", hotenavi_flag=?";
        query += ", cardless_flag=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE id=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            // prestate.setInt( i++, this.sortKey );
            prestate.setInt( i++, this.prefId );
            prestate.setInt( i++, this.mileFlag );
            prestate.setInt( i++, this.reserveFlag );
            // prestate.setInt( i++, this.stationFlag );
            // prestate.setInt( i++, this.icFlag );
            // prestate.setInt( i++, this.areaFlag );
            // prestate.setString( i++, this.hotelImg );
            prestate.setInt( i++, this.hotenaviFlag );
            prestate.setInt( i++, this.cardlessFlag );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( i++, this.id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
                DBSync.publish( prestate.toString().split( ":", 2 )[1] );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCount.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * hh_hotel_count（ホテル検索件数表示用）更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID (hh_hotel_basic id<100000000,pref_id<>0,kind<=7 のホテル)
     * @return
     */
    public boolean updateData(int id)
    {
        boolean ret;
        Connection connection = null;
        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            ret = updateData( connection, id );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCount.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /****
     * hh_hotel_count（ホテル検索件数表示用）取得
     * 
     * @param id ホテルID (hh_hotel_basic id<100000000,pref_id<>0,kind<=7 のホテル)
     * @return
     */
    public boolean deleteData(Connection connection, int id)
    {
        boolean ret;
        String query;
        int result = -1;
        PreparedStatement prestate = null;
        ret = false;
        query = "DELETE FROM hotenavi.hh_hotel_count WHERE id = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                DBSync.publish( prestate.toString().split( ":", 2 )[1] );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCount.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /****
     * hh_hotel_count（ホテル検索件数表示用）取得
     * 
     * @param id ホテルID (hh_hotel_basic id<100000000,pref_id<>0,kind<=7 のホテル)
     * @return
     */
    public boolean deleteData(int id)
    {
        boolean ret;
        Connection connection = null;
        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            ret = deleteData( connection, id );

        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelCount.deleteData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
