package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Nov 28 11:38:50 JST 2014
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * Ap_auto_coupon_detailVo.
 * 
 * @author tashiro-s1
 * @version 1.0
 *          history
 *          Symbol Date Person Note
 *          [1] 2014/11/28 tashiro-s1 Generated.
 */
public class DataApAutoCouponDetail implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 6199759058884180191L;
    private int               id;
    private int               couponSeq;
    private int               planNo;
    private int               chargeMode;
    private String            planName;
    private String            chargeModeName;
    private int               discount;

    public DataApAutoCouponDetail()
    {
        this.id = 0;
        this.couponSeq = 0;
        this.planNo = 0;
        this.chargeMode = 0;
        this.planName = "";
        this.chargeModeName = "";
        this.discount = 0;

    }

    public int getId()
    {
        return id;
    }

    public int getCouponSeq()
    {
        return couponSeq;
    }

    public int getPlanNo()
    {
        return planNo;
    }

    public int getChargeMode()
    {
        return chargeMode;
    }

    public String getPlanName()
    {
        return planName;
    }

    public String getChargeModeName()
    {
        return chargeModeName;
    }

    public int getDiscount()
    {
        return discount;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCouponSeq(int couponSeq)
    {
        this.couponSeq = couponSeq;
    }

    public void setPlanNo(int planNo)
    {
        this.planNo = planNo;
    }

    public void setChargeMode(int chargeMode)
    {
        this.chargeMode = chargeMode;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public void setChargeModeName(String chargeModeName)
    {
        this.chargeModeName = chargeModeName;
    }

    public void setDiscount(int discount)
    {
        this.discount = discount;
    }

    /****
     * 自動発行クーポン管理(ap_auto_coupon)取得
     * 
     * @param id ハピホテホテルID
     * @param couponSeq クーポン連番（自動採番）
     * @return
     */
    public boolean getData(int id, int couponSeq, int planNo, int chargeMode)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_auto_coupon_detail WHERE id = ? AND coupon_seq = ? AND plan_no = ? AND charge_mode=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, couponSeq );
            prestate.setInt( 3, planNo );
            prestate.setInt( 4, chargeMode );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.couponSeq = result.getInt( "coupon_seq" );
                    this.planNo = result.getInt( "plan_no" );
                    this.chargeMode = result.getInt( "charge_mode" );
                    this.planName = result.getString( "plan_name" );
                    this.chargeModeName = result.getString( "charge_mode_name" );
                    this.discount = result.getInt( "discount" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAutoCouponDetail.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 自動発行クーポン管理(ap_auto_coupon)設定
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
                this.couponSeq = result.getInt( "coupon_seq" );
                this.planNo = result.getInt( "plan_no" );
                this.chargeMode = result.getInt( "charge_mode" );
                this.planName = result.getString( "plan_name" );
                this.chargeModeName = result.getString( "charge_mode_name" );
                this.discount = result.getInt( "discount" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAutoCouponDetail.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 自動発行クーポン管理(ap_auto_coupon)挿入
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

        query = "INSERT ap_auto_coupon_detail SET ";
        query += " id=?";
        query += ", coupon_seq=?";
        query += ", plan_no=?";
        query += ", charge_mode=?";
        query += ", plan_name=?";
        query += ", charge_mode_name=?";
        query += ", discount=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.couponSeq );
            prestate.setInt( i++, this.planNo );
            prestate.setInt( i++, this.chargeMode );
            prestate.setString( i++, this.planName );
            prestate.setString( i++, this.chargeModeName );
            prestate.setInt( i++, this.discount );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAutoCouponDetail.insertData] Exception=" + e.toString() );
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
     * 自動発行クーポン管理(ap_auto_coupon)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ハピホテホテルID
     * @param couponSeq クーポン連番（自動採番）
     * @return
     */
    public boolean updateData(int id, int couponSeq, int planNo, int chargeMode)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_auto_coupon_detail SET ";
        query += ", plan_name=?";
        query += ", charge_mode_name=?";
        query += ", discount=?";
        query += " WHERE id=? AND coupon_seq=? AND plan_no=? AND charge_mode=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.planName );
            prestate.setString( i++, this.chargeModeName );
            prestate.setInt( i++, this.discount );
            prestate.setInt( i++, id );
            prestate.setInt( i++, couponSeq );
            prestate.setInt( i++, planNo );
            prestate.setInt( i++, chargeMode );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApAutoCouponDetail.updateData] Exception=" + e.toString() );
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
