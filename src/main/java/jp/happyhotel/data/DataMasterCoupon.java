/*
 * @(#)DataMasterCoupon.java 1.00 2008/03/06 Copyright (C) ALMEX Inc. 2008 クーポンマスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * クーポンマスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/03/06
 */
public class DataMasterCoupon implements Serializable
{

    /**
	 *
	 */
    private static final long serialVersionUID = -2647086753291385848L;

    int                       id;
    int                       seq;
    int                       serviceFlag;
    int                       couponNo;
    int                       available;
    int                       useCount;
    int                       startDay;
    int                       endDay;

    /**
     * データを初期化します。
     */
    public DataMasterCoupon()
    {
        id = 0;
        seq = 0;
        serviceFlag = 0;
        couponNo = 0;
        available = 0;
        startDay = 0;
        endDay = 0;
    }

    public int getAvailable()
    {
        return available;
    }

    public int getCouponNo()
    {
        return couponNo;
    }

    public int getEndDay()
    {
        return endDay;
    }

    public int getId()
    {
        return id;
    }

    public int getServiceFlag()
    {
        return serviceFlag;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStartDay()
    {
        return startDay;
    }

    public int getUseCount()
    {
        return useCount;
    }

    public void setAvailable(int available)
    {
        this.available = available;
    }

    public void setCouponNo(int couponNo)
    {
        this.couponNo = couponNo;
    }

    public void setEndDay(int endDay)
    {
        this.endDay = endDay;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setServiceFlag(int serviceFlag)
    {
        this.serviceFlag = serviceFlag;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStartDay(int startDay)
    {
        this.startDay = startDay;
    }

    public void setUseCount(int useCount)
    {
        this.useCount = useCount;
    }

    /**
     * クーポンマスタデータ取得(主キー全てから取得)
     * 
     * @param id ホテルID
     * @param couponNo クーポン番号(hh_hotel_coupon.seq)
     * @param available 利用フラグ（0：制御なし、1：メンバー、2：ビジター）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int serviceFlag, int couponNo, int seq)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_coupon WHERE id = ? AND service_flag=?";
        query = query + " AND coupon_no = ?";
        query = query + " AND seq = ?";
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, serviceFlag );
            prestate.setInt( 3, couponNo );
            prestate.setInt( 4, seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.serviceFlag = result.getInt( "service_flag" );
                    this.couponNo = result.getInt( "coupon_no" );
                    this.available = result.getInt( "available" );
                    this.useCount = result.getInt( "use_count" );
                    this.startDay = result.getInt( "start_day" );
                    this.endDay = result.getInt( "end_day" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterCoupon.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * クーポンマスタデータ取得
     * 
     * @param id ホテルID
     * @param couponNo クーポン番号(hh_hotel_coupon.seq)
     * @param available 利用フラグ（0：制御なし、1：メンバー、2：ビジター）
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int couponNo, int available)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_coupon WHERE id = ? AND coupon_no = ?";
        query = query + " AND ( available = 0";
        if ( available > 0 )
        {
            query = query + " OR available = ?";
        }
        query = query + " )";
        query = query + " AND ( service_flag = 0 OR service_flag = 1 )";
        query = query + " ORDER BY seq DESC";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, couponNo );
            if ( available > 0 )
            {
                prestate.setInt( 3, available );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.serviceFlag = result.getInt( "service_flag" );
                    this.couponNo = result.getInt( "coupon_no" );
                    this.available = result.getInt( "available" );
                    this.useCount = result.getInt( "use_count" );
                    this.startDay = result.getInt( "start_day" );
                    this.endDay = result.getInt( "end_day" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterCoupon.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * クーポンマスタデータ設定
     * 
     * @param result クーポンマスタデータレコード
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
                this.serviceFlag = result.getInt( "service_flag" );
                this.couponNo = result.getInt( "coupon_no" );
                this.available = result.getInt( "available" );
                this.useCount = result.getInt( "use_count" );
                this.startDay = result.getInt( "start_day" );
                this.endDay = result.getInt( "end_day" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterCoupon.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * クーポンマスタデータ設定
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

        query = "INSERT hh_master_coupon SET";
        query = query + " id = ?,";
        query = query + " seq = 0,";
        query = query + " service_flag = ?,";
        query = query + " coupon_no = ?,";
        query = query + " available = ?,";
        query = query + " use_count = ?,";
        query = query + " start_day = ?,";
        query = query + " end_day = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.serviceFlag );
            prestate.setInt( 3, this.couponNo );
            prestate.setInt( 4, this.available );
            prestate.setInt( 5, this.useCount );
            prestate.setInt( 6, this.startDay );
            prestate.setInt( 7, this.endDay );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterCoupon.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * クーポンマスタデータ設定
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int couponNo)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_master_coupon SET";
        query = query + " service_flag = ?,";
        query = query + " seq = ?,";
        query = query + " available = ?,";
        query = query + " use_count = ?,";
        query = query + " start_day = ?,";
        query = query + " end_day = ?";
        query = query + " WHERE id = ?";
        query = query + " AND coupon_no = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.serviceFlag );
            prestate.setInt( 2, this.seq );
            prestate.setInt( 3, this.available );
            prestate.setInt( 4, this.useCount );
            prestate.setInt( 5, this.startDay );
            prestate.setInt( 6, this.endDay );
            prestate.setInt( 7, id );
            prestate.setInt( 8, couponNo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterCoupon.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

}
