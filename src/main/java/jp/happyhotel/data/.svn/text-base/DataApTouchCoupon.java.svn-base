package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * タッチクーポン管理（ap_touch_coupon）取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2015/1/13
 */
public class DataApTouchCoupon implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -2250287402963086045L;
    public static final String TABLE            = "ap_touch_coupon";
    private int                id;                                      // ホテルID
    private int                seq;                                     // ハピホテチェックインコード
    private int                kind;                                    // クーポン区分（0：旧クーポン、1：割引クーポン、2：印字クーポン）
    private int                couponNo;                                // クーポン番号（hh_user_coupon.coupon_no）
    private int                userSeq;                                 // ユーザクーポン管理番号（hh_user_coupon.seq）
    private int                hotelSeq;                                // ホテルクーポン管理番号（hh_user_coupon.seq）

    /**
     * データを初期化します。
     */
    public DataApTouchCoupon()
    {
        this.id = 0;
        this.seq = 0;
        this.kind = 0;
        this.couponNo = 0;
        this.userSeq = 0;
        this.hotelSeq = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getKind()
    {
        return kind;
    }

    public int getCouponNo()
    {
        return couponNo;
    }

    public int getUserSeq()
    {
        return userSeq;
    }

    public int getHotelSeq()
    {
        return hotelSeq;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setCouponNo(int couponNo)
    {
        this.couponNo = couponNo;
    }

    public void setUserSeq(int userSeq)
    {
        this.userSeq = userSeq;
    }

    public void setHotelSeq(int hotelSeq)
    {
        this.hotelSeq = hotelSeq;
    }

    /****
     * タッチクーポン管理（ap_touch_coupon）取得
     * 
     * @param id ホテルID
     * @param seq ハピホテチェックインコード
     * @return
     */
    public boolean getData(int id, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_touch_coupon WHERE id = ? AND seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.kind = result.getInt( "kind" );
                    this.couponNo = result.getInt( "coupon_no" );
                    this.userSeq = result.getInt( "user_seq" );
                    this.hotelSeq = result.getInt( "hotel_seq" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchCoupon.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * タッチクーポン管理（ap_touch_coupon）設定
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
                this.seq = result.getInt( "seq" );
                this.kind = result.getInt( "kind" );
                this.couponNo = result.getInt( "coupon_no" );
                this.userSeq = result.getInt( "user_seq" );
                this.hotelSeq = result.getInt( "hotel_seq" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchCoupon.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * タッチクーポン管理（ap_touch_coupon）挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT ap_touch_coupon SET ";
        query += " id=?";
        query += ", seq=?";
        query += ", kind=?";
        query += ", coupon_no=?";
        query += ", user_seq=?";
        query += ", hotel_seq=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.seq );
            prestate.setInt( i++, this.kind );
            prestate.setInt( i++, this.couponNo );
            prestate.setInt( i++, this.userSeq );
            prestate.setInt( i++, this.hotelSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchCoupon.insertData] Exception=" + e.toString() );
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
     * タッチクーポン管理（ap_touch_coupon）更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param seq ハピホテチェックインコード
     * @return
     */
    public boolean updateData(int id, int seq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_touch_coupon SET ";
        query += " kind=?";
        query += ", coupon_no=?";
        query += ", user_seq=?";
        query += ", hotel_seq=?";
        query += " WHERE id=? AND seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.kind );
            prestate.setInt( i++, this.couponNo );
            prestate.setInt( i++, this.userSeq );
            prestate.setInt( i++, this.hotelSeq );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.seq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApTouchCoupon.updateData] Exception=" + e.toString() );
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
