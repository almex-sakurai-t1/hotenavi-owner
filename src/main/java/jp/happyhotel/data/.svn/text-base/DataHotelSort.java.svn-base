package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Mar 29 18:56:30 JST 2013
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * DataHotelSort
 * 
 */
public class DataHotelSort implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 2039420613655006295L;
    private int               id;
    private int               collectDate;
    private int               allPoint;
    private int               uuPv;
    private int               uuPvPoint;
    private int               touch;
    private int               touchPoint;
    private int               uuTouch;
    private int               uuTouchPoint;
    private int               sponsor;
    private int               sponsorPoint;
    private int               myhotel;
    private int               myhotelPoint;
    private int               coupon;
    private int               couponPoint;
    private int               reserve;
    private int               reservePoint;
    private int               kuchikomi;
    private int               kuchikomiPoint;

    public DataHotelSort()
    {
        id = 0;
        collectDate = 0;
        allPoint = 0;
        uuPv = 0;
        uuPvPoint = 0;
        touch = 0;
        touchPoint = 0;
        uuTouch = 0;
        uuTouchPoint = 0;
        sponsor = 0;
        sponsorPoint = 0;
        myhotel = 0;
        myhotelPoint = 0;
        coupon = 0;
        couponPoint = 0;
        reserve = 0;
        reservePoint = 0;
        kuchikomi = 0;
        kuchikomiPoint = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getCollectDate()
    {
        return collectDate;
    }

    public int getAllPoint()
    {
        return allPoint;
    }

    public int getUuPv()
    {
        return uuPv;
    }

    public int getUuPvPoint()
    {
        return uuPvPoint;
    }

    public int getTouch()
    {
        return touch;
    }

    public int getTouchPoint()
    {
        return touchPoint;
    }

    public int getUuTouch()
    {
        return uuTouch;
    }

    public int getUuTouchPoint()
    {
        return uuTouchPoint;
    }

    public int getSponsor()
    {
        return sponsor;
    }

    public int getSponsorPoint()
    {
        return sponsorPoint;
    }

    public int getMyhotel()
    {
        return myhotel;
    }

    public int getMyhotelPoint()
    {
        return myhotelPoint;
    }

    public int getCoupon()
    {
        return coupon;
    }

    public int getCouponPoint()
    {
        return couponPoint;
    }

    public int getReserve()
    {
        return reserve;
    }

    public int getReservePoint()
    {
        return reservePoint;
    }

    public int getKuchikomi()
    {
        return kuchikomi;
    }

    public int getKuchikomiPoint()
    {
        return kuchikomiPoint;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCollectDate(int collectDate)
    {
        this.collectDate = collectDate;
    }

    public void setAllPoint(int allPoint)
    {
        this.allPoint = allPoint;
    }

    public void setUuPv(int uuPv)
    {
        this.uuPv = uuPv;
    }

    public void setUuPvPoint(int uuPvPoint)
    {
        this.uuPvPoint = uuPvPoint;
    }

    public void setTouch(int touch)
    {
        this.touch = touch;
    }

    public void setTouchPoint(int touchPoint)
    {
        this.touchPoint = touchPoint;
    }

    public void setUuTouch(int uuTouch)
    {
        this.uuTouch = uuTouch;
    }

    public void setUuTouchPoint(int uuTouchPoint)
    {
        this.uuTouchPoint = uuTouchPoint;
    }

    public void setSponsor(int sponsor)
    {
        this.sponsor = sponsor;
    }

    public void setSponsorPoint(int sponsorPoint)
    {
        this.sponsorPoint = sponsorPoint;
    }

    public void setMyhotel(int myhotel)
    {
        this.myhotel = myhotel;
    }

    public void setMyhotelPoint(int myhotelPoint)
    {
        this.myhotelPoint = myhotelPoint;
    }

    public void setCoupon(int coupon)
    {
        this.coupon = coupon;
    }

    public void setCouponPoint(int couponPoint)
    {
        this.couponPoint = couponPoint;
    }

    public void setReserve(int reserve)
    {
        this.reserve = reserve;
    }

    public void setReservePoint(int reservePoint)
    {
        this.reservePoint = reservePoint;
    }

    public void setKuchikomi(int kuchikomi)
    {
        this.kuchikomi = kuchikomi;
    }

    public void setKuchikomiPoint(int kuchikomiPoint)
    {
        this.kuchikomiPoint = kuchikomiPoint;
    }

    /****
     * マスターデータ取得
     * 
     * @param kind
     * @return
     */
    public boolean getData(int id, int collectDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_sort WHERE id= ? AND collect_date = ?";

        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, collectDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.collectDate = result.getInt( "collect_date" );
                    this.allPoint = result.getInt( "all_point" );
                    this.uuPv = result.getInt( "uu_pv" );
                    this.uuPvPoint = result.getInt( "uu_pv_point" );
                    this.touch = result.getInt( "touch" );
                    this.touchPoint = result.getInt( "touch_point" );
                    this.uuTouch = result.getInt( "uu_touch" );
                    this.uuTouchPoint = result.getInt( "uu_touch_point" );
                    this.sponsor = result.getInt( "sponsor" );
                    this.sponsorPoint = result.getInt( "sponsor_point" );
                    this.myhotel = result.getInt( "myhotel" );
                    this.myhotelPoint = result.getInt( "myhotel_point" );
                    this.coupon = result.getInt( "coupon" );
                    this.couponPoint = result.getInt( "coupon_point" );
                    this.reserve = result.getInt( "reserve" );
                    this.reservePoint = result.getInt( "reserve_point" );
                    this.kuchikomi = result.getInt( "kuchikomi" );
                    this.kuchikomiPoint = result.getInt( "kuchikomi_point" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSort.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * マスターデータ設定
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
                this.collectDate = result.getInt( "collect_date" );
                this.allPoint = result.getInt( "all_point" );
                this.uuPv = result.getInt( "uu_pv" );
                this.uuPvPoint = result.getInt( "uu_pv_point" );
                this.touch = result.getInt( "touch" );
                this.touchPoint = result.getInt( "touch_point" );
                this.uuTouch = result.getInt( "uu_touch" );
                this.uuTouchPoint = result.getInt( "uu_touch_point" );
                this.sponsor = result.getInt( "sponsor" );
                this.sponsorPoint = result.getInt( "sponsor_point" );
                this.myhotel = result.getInt( "myhotel" );
                this.myhotelPoint = result.getInt( "myhotel_point" );
                this.coupon = result.getInt( "coupon" );
                this.couponPoint = result.getInt( "coupon_point" );
                this.reserve = result.getInt( "reserve" );
                this.reservePoint = result.getInt( "reserve_point" );
                this.kuchikomi = result.getInt( "kuchikomi" );
                this.kuchikomiPoint = result.getInt( "kuchikomi_point" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSort.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * マスターデータ挿入
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

        query = "INSERT hh_hotel_sort SET ";
        query += " id = ?,";
        query += " collect_date = ?,";
        query += " all_point = ?,";
        query += " uu_pv = ?,";
        query += " uu_pv_point = ?, ";
        query += " touch = ?, ";
        query += " touch_point = ?, ";
        query += " uu_touch = ?, ";
        query += " uu_touch_point = ?, ";
        query += " sponsor = ?, ";
        query += " sponsor_point = ?, ";
        query += " myhotel = ?, ";
        query += " myhotel_point = ?, ";
        query += " coupon = ?, ";
        query += " coupon_point = ?, ";
        query += " reserve = ?, ";
        query += " reserve_point = ?, ";
        query += " kuchikomi = ?, ";
        query += " kuchikomi_point = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.collectDate );
            prestate.setInt( 3, this.allPoint );
            prestate.setInt( 4, this.uuPv );
            prestate.setInt( 5, this.uuPvPoint );
            prestate.setInt( 6, this.touch );
            prestate.setInt( 7, this.touchPoint );
            prestate.setInt( 8, this.uuTouch );
            prestate.setInt( 9, this.uuTouchPoint );
            prestate.setInt( 10, this.sponsor );
            prestate.setInt( 11, this.sponsorPoint );
            prestate.setInt( 12, this.myhotel );
            prestate.setInt( 13, this.myhotelPoint );
            prestate.setInt( 14, this.coupon );
            prestate.setInt( 15, this.couponPoint );
            prestate.setInt( 16, this.reserve );
            prestate.setInt( 17, this.reservePoint );
            prestate.setInt( 18, this.kuchikomi );
            prestate.setInt( 19, this.kuchikomiPoint );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSort.insertData] Exception=" + e.toString() );
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
     * マスターデータ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param kind 区分
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int kind)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_sort SET ";
        query += " all_point = ?,";
        query += " uu_pv = ?,";
        query += " uu_pv_point = ?, ";
        query += " touch = ?, ";
        query += " touch_point = ?, ";
        query += " uu_touch = ?, ";
        query += " uu_touch_point = ?, ";
        query += " sponsor = ?, ";
        query += " sponsor_point = ?, ";
        query += " myhotel = ?, ";
        query += " myhotel_point = ?, ";
        query += " coupon = ?, ";
        query += " coupon_point = ?, ";
        query += " reserve = ?, ";
        query += " reserve_point = ?, ";
        query += " kuchikomi = ?, ";
        query += " kuchikomi_point = ? ";
        query += " WHERE id = ?";
        query += " AND collect_date = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.allPoint );
            prestate.setInt( 2, this.uuPv );
            prestate.setInt( 3, this.uuPvPoint );
            prestate.setInt( 4, this.touch );
            prestate.setInt( 5, this.touchPoint );
            prestate.setInt( 6, this.uuTouch );
            prestate.setInt( 7, this.uuTouchPoint );
            prestate.setInt( 8, this.sponsor );
            prestate.setInt( 9, this.sponsorPoint );
            prestate.setInt( 10, this.myhotel );
            prestate.setInt( 11, this.myhotelPoint );
            prestate.setInt( 12, this.coupon );
            prestate.setInt( 13, this.couponPoint );
            prestate.setInt( 14, this.reserve );
            prestate.setInt( 15, this.reservePoint );
            prestate.setInt( 16, this.kuchikomi );
            prestate.setInt( 17, this.kuchikomiPoint );
            prestate.setInt( 18, this.id );
            prestate.setInt( 19, this.collectDate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSort.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /***
     * ホテルソート取得
     * 
     * @param con コネクション
     * @param id ホテルID
     * @param collectDate 集計日
     * @return
     */
    public boolean getData(Connection con, int id, int collectDate)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_sort WHERE id= ? AND collect_date = ?";

        try
        {
            prestate = con.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, collectDate );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.collectDate = result.getInt( "collect_date" );
                    this.allPoint = result.getInt( "all_point" );
                    this.uuPv = result.getInt( "uu_pv" );
                    this.uuPvPoint = result.getInt( "uu_pv_point" );
                    this.touch = result.getInt( "touch" );
                    this.touchPoint = result.getInt( "touch_point" );
                    this.uuTouch = result.getInt( "uu_touch" );
                    this.uuTouchPoint = result.getInt( "uu_touch_point" );
                    this.sponsor = result.getInt( "sponsor" );
                    this.sponsorPoint = result.getInt( "sponsor_point" );
                    this.myhotel = result.getInt( "myhotel" );
                    this.myhotelPoint = result.getInt( "myhotel_point" );
                    this.coupon = result.getInt( "coupon" );
                    this.couponPoint = result.getInt( "coupon_point" );
                    this.reserve = result.getInt( "reserve" );
                    this.reservePoint = result.getInt( "reserve_point" );
                    this.kuchikomi = result.getInt( "kuchikomi" );
                    this.kuchikomiPoint = result.getInt( "kuchikomi_point" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSort.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /***
     * マスターデータ挿入
     * 
     * @param con コネクション
     * @see "値のセット後(setXXX)に行うこと"
     * @return
     */
    public boolean insertData(Connection con)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_hotel_sort SET ";
        query += " id = ?,";
        query += " collect_date = ?,";
        query += " all_point = ?,";
        query += " uu_pv = ?,";
        query += " uu_pv_point = ?, ";
        query += " touch = ?, ";
        query += " touch_point = ?, ";
        query += " uu_touch = ?, ";
        query += " uu_touch_point = ?, ";
        query += " sponsor = ?, ";
        query += " sponsor_point = ?, ";
        query += " myhotel = ?, ";
        query += " myhotel_point = ?, ";
        query += " coupon = ?, ";
        query += " coupon_point = ?, ";
        query += " reserve = ?, ";
        query += " reserve_point = ?, ";
        query += " kuchikomi = ?, ";
        query += " kuchikomi_point = ? ";

        try
        {
            prestate = con.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.collectDate );
            prestate.setInt( 3, this.allPoint );
            prestate.setInt( 4, this.uuPv );
            prestate.setInt( 5, this.uuPvPoint );
            prestate.setInt( 6, this.touch );
            prestate.setInt( 7, this.touchPoint );
            prestate.setInt( 8, this.uuTouch );
            prestate.setInt( 9, this.uuTouchPoint );
            prestate.setInt( 10, this.sponsor );
            prestate.setInt( 11, this.sponsorPoint );
            prestate.setInt( 12, this.myhotel );
            prestate.setInt( 13, this.myhotelPoint );
            prestate.setInt( 14, this.coupon );
            prestate.setInt( 15, this.couponPoint );
            prestate.setInt( 16, this.reserve );
            prestate.setInt( 17, this.reservePoint );
            prestate.setInt( 18, this.kuchikomi );
            prestate.setInt( 19, this.kuchikomiPoint );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterSort.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /**
     * マスターデータ更新
     * 
     * @param con コネクション
     * @param id ホテルID
     * @param collectDate 集計日
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(Connection con, int id, int collectDate)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_sort SET ";
        query += " all_point = ?,";
        query += " uu_pv = ?,";
        query += " uu_pv_point = ?, ";
        query += " touch = ?, ";
        query += " touch_point = ?, ";
        query += " uu_touch = ?, ";
        query += " uu_touch_point = ?, ";
        query += " sponsor = ?, ";
        query += " sponsor_point = ?, ";
        query += " myhotel = ?, ";
        query += " myhotel_point = ?, ";
        query += " coupon = ?, ";
        query += " coupon_point = ?, ";
        query += " reserve = ?, ";
        query += " reserve_point = ?, ";
        query += " kuchikomi = ?, ";
        query += " kuchikomi_point = ? ";
        query += " WHERE id = ?";
        query += " AND collect_date = ?";

        try
        {
            prestate = con.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.allPoint );
            prestate.setInt( 2, this.uuPv );
            prestate.setInt( 3, this.uuPvPoint );
            prestate.setInt( 4, this.touch );
            prestate.setInt( 5, this.touchPoint );
            prestate.setInt( 6, this.uuTouch );
            prestate.setInt( 7, this.uuTouchPoint );
            prestate.setInt( 8, this.sponsor );
            prestate.setInt( 9, this.sponsorPoint );
            prestate.setInt( 10, this.myhotel );
            prestate.setInt( 11, this.myhotelPoint );
            prestate.setInt( 12, this.coupon );
            prestate.setInt( 13, this.couponPoint );
            prestate.setInt( 14, this.reserve );
            prestate.setInt( 15, this.reservePoint );
            prestate.setInt( 16, this.kuchikomi );
            prestate.setInt( 17, this.kuchikomiPoint );
            prestate.setInt( 18, id );
            prestate.setInt( 19, collectDate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelSort.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

}
