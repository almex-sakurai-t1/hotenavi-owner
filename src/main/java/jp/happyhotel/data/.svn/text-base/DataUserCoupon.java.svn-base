/*
 * @(#)DataMasterCoupon.java 1.00 2008/03/06 Copyright (C) ALMEX Inc. 2008 クーポンシリアル番号取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * クーポンシリアル番号取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/03/06
 */
public class DataUserCoupon implements Serializable
{
    private static final long serialVersionUID = -3214679901890506089L;
    int                       id;
    int                       seq;
    String                    userId;
    int                       couponNo;
    String                    userStatus;
    int                       startDate;
    int                       endDate;
    String                    mailAddrMd5;
    int                       printDate;
    int                       printTime;
    int                       usedFlag;

    /**
     * データを初期化します。
     */
    public DataUserCoupon()
    {
        id = 0;
        seq = 0;
        userId = "";
        couponNo = 0;
        userStatus = "";
        startDate = 0;
        endDate = 0;
        mailAddrMd5 = "";
        printDate = 0;
        printTime = 0;
        usedFlag = 0;
    }

    public int getCouponNo()
    {
        return couponNo;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getId()
    {
        return id;
    }

    public String getMailAddrMd5()
    {
        return mailAddrMd5;
    }

    public int getPrintDate()
    {
        return printDate;
    }

    public int getPrintTime()
    {
        return printTime;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getUsedFlag()
    {
        return usedFlag;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserStatus()
    {
        return userStatus;
    }

    public void setCouponNo(int couponNo)
    {
        this.couponNo = couponNo;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMailAddrMd5(String mailAddrMd5)
    {
        this.mailAddrMd5 = mailAddrMd5;
    }

    public void setPrintDate(int printDate)
    {
        this.printDate = printDate;
    }

    public void setPrintTime(int printTime)
    {
        this.printTime = printTime;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setUsedFlag(int usedFlag)
    {
        this.usedFlag = usedFlag;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserStatus(String userStatus)
    {
        this.userStatus = userStatus;
    }

    /**
     * クーポンシリアル番号データ取得
     * 
     * @param code ポイントコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int couponNo)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_coupon WHERE id = ? AND coupon_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, couponNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.userId = result.getString( "user_id" );
                    this.couponNo = result.getInt( "coupon_no" );
                    this.userStatus = result.getString( "user_status" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.mailAddrMd5 = result.getString( "mail_addr_md5" );
                    this.printDate = result.getInt( "print_date" );
                    this.printTime = result.getInt( "print_time" );
                    this.usedFlag = result.getInt( "used_flag" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserCoupon.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * クーポンシリアル番号データ設定
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
                this.userId = result.getString( "user_id" );
                this.couponNo = result.getInt( "coupon_no" );
                this.userStatus = result.getString( "user_status" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.mailAddrMd5 = result.getString( "mail_addr_md5" );
                this.printDate = result.getInt( "print_date" );
                this.printTime = result.getInt( "print_time" );
                this.usedFlag = result.getInt( "used_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserCoupon.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * クーポンシリアル番号データ設定
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

        query = "INSERT hh_user_coupon SET";
        query = query + " id = ?,";
        query = query + " seq = ?,";
        query = query + " user_id = ?";
        query = query + " coupon_no = 0,";
        query = query + " user_status = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " mail_addr_md5 = ?,";
        query = query + " print_date = ?,";
        query = query + " print_time = ?,";
        query = query + " used_flag = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.seq );
            prestate.setString( 3, this.userId );
            prestate.setString( 4, this.userStatus );
            prestate.setInt( 5, this.startDate );
            prestate.setInt( 6, this.endDate );
            prestate.setString( 7, this.mailAddrMd5 );
            prestate.setInt( 8, this.printDate );
            prestate.setInt( 9, this.printTime );
            prestate.setInt( 10, this.usedFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserCoupon.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * クーポンシリアル番号データ設定
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

        query = "UPDATE hh_user_coupon SET";
        query = query + " seq = ?,";
        query = query + " user_id = ?,";
        query = query + " user_status = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " mail_addr_md5 = ?,";
        query = query + " print_date = ?,";
        query = query + " print_time = ?,";
        query = query + " used_flag = ?";
        query = query + " WHERE id = ?";
        query = query + " AND coupon_no = ?";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, this.seq );
            prestate.setString( 2, this.userId );
            prestate.setString( 3, this.userStatus );
            prestate.setInt( 4, this.startDate );
            prestate.setInt( 5, this.endDate );
            prestate.setString( 6, this.mailAddrMd5 );
            prestate.setInt( 7, this.printDate );
            prestate.setInt( 8, this.printTime );
            prestate.setInt( 9, this.usedFlag );
            prestate.setInt( 10, id );
            prestate.setInt( 11, couponNo );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserCoupon.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
