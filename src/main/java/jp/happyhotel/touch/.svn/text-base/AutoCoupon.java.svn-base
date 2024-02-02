package jp.happyhotel.touch;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApAutoCoupon;
import jp.happyhotel.data.DataApAutoCouponDetail;

public class AutoCoupon implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -8220319338729678991L;
    int                       couponKind;
    int                       couponCount;
    int                       couponDetailCount;
    DataApAutoCoupon          autoCoupon;
    DataApAutoCouponDetail[]  autoCouponDetail;

    public int getCouponKind()
    {
        return couponKind;
    }

    public int getCouponCount()
    {
        return couponCount;
    }

    public int getCouponDetailCount()
    {
        return couponDetailCount;
    }

    public DataApAutoCoupon getAutoCoupon()
    {
        return autoCoupon;
    }

    public DataApAutoCouponDetail[] getAutoCouponDetail()
    {
        return autoCouponDetail;
    }

    public void setCouponKind(int couponKind)
    {
        this.couponKind = couponKind;
    }

    public void setCouponCount(int couponCount)
    {
        this.couponCount = couponCount;
    }

    public void setCouponDetailCount(int couponDetailCount)
    {
        this.couponDetailCount = couponDetailCount;
    }

    public void setAutoCoupon(DataApAutoCoupon autoCoupon)
    {
        this.autoCoupon = autoCoupon;
    }

    public void setAutoCouponDetail(DataApAutoCouponDetail[] autoCouponDetail)
    {
        this.autoCouponDetail = autoCouponDetail;
    }

    /***
     * クーポンのデータを取得する
     * 
     */
    public boolean getAutoCoupon(int id)
    {
        boolean ret = false;
        boolean memberFlag;
        int count = 0;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        String query = "";
        int today = Integer.parseInt( DateEdit.getDate( 2 ) );
        int thisTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        Long now = (long)today * 1000000 + (long)thisTime;
        try
        {
            query = "SELECT * FROM ap_auto_coupon";
            query += " WHERE id = ?";
            query += " AND start_date * 1000000 + start_time <= ?";
            query += " AND end_date * 1000000 + end_time >= ?";
            query += " AND period >= ?";
            query += " AND del_flag=0";
            query += " ORDER BY coupon_seq DESC";
            query += " LIMIT 0,1 ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setLong( 2, now );
            prestate.setLong( 3, now );
            prestate.setInt( 4, today );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.couponCount = result.getRow();
                }
                result.beforeFirst();

                // 1件取得
                if ( result.next() != false )
                {
                    this.autoCoupon = new DataApAutoCoupon();
                    this.autoCoupon.setData( result );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.getAutoCoupon]Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /***
     * クーポンのデータを取得する
     * 
     * @param DataHotelBasic ホテル情報
     */
    public boolean getAutoCouponDetail(Connection connection, int id, int couponSeq)
    {
        boolean ret = false;
        boolean memberFlag;
        int count = 0;

        ResultSet result = null;
        PreparedStatement prestate = null;

        String query = "";

        try
        {
            query = "SELECT * FROM ap_auto_coupon_detail";
            query += " WHERE id = ?";
            query += " AND coupon_seq = ? ";

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, couponSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.couponDetailCount = result.getRow();
                }
                result.beforeFirst();

                this.autoCouponDetail = new DataApAutoCouponDetail[this.couponDetailCount];
                // 1件取得
                while( result.next() != false )
                {
                    this.autoCouponDetail[count] = new DataApAutoCouponDetail();
                    this.autoCouponDetail[count].setData( result );
                    count++;
                }
                ret = true;

            }

        }
        catch ( Exception e )
        {
            Logging.error( "[HotelDetail.getAutoCoupon]Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }
}
