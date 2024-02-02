package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Mon Jun 03 19:36:29 JST 2013
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 
 * @author tashiro-s1
 * 
 */
public class DataRsvMonthlyConfirm implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -2249998385112004350L;
    private int               id;
    private int               planId;
    private int               calDate;
    private String            hotelId;
    private int               userId;
    private int               dispDate;
    private int               dispTime;
    private int               confirmDate;
    private int               confirmTime;

    /**
     * Constractor
     */
    public DataRsvMonthlyConfirm()
    {
        id = 0;
        planId = 0;
        calDate = 0;
        hotelId = "";
        userId = 0;
        dispDate = 0;
        dispTime = 0;
        confirmDate = 0;
        confirmTime = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getPlanId()
    {
        return planId;
    }

    public int getCalDate()
    {
        return calDate;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public int getUserId()
    {
        return userId;
    }

    public int getDispDate()
    {
        return dispDate;
    }

    public int getDispTime()
    {
        return dispTime;
    }

    public int getConfirmDate()
    {
        return confirmDate;
    }

    public int getConfirmTime()
    {
        return confirmTime;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public void setCalDate(int calDate)
    {
        this.calDate = calDate;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public void setDispDate(int dispDate)
    {
        this.dispDate = dispDate;
    }

    public void setDispTime(int dispTime)
    {
        this.dispTime = dispTime;
    }

    public void setConfirmDate(int confirmDate)
    {
        this.confirmDate = confirmDate;
    }

    public void setConfirmTime(int confirmTime)
    {
        this.confirmTime = confirmTime;
    }

    /**
     * プラン情報取得
     * 
     * @param id ホテルID
     * @param planId プランID
     * @return 処理結果(True:正常,False:異常)
     */
    public boolean getData(int Id, int planId, int calDate, String hotelId, int userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_rsv_monthly_confirm WHERE id = ? AND plan_id = ? AND cal_date = ?" +
                " AND hotel_id = ? AND user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, calDate );
            prestate.setString( 4, hotelId );
            prestate.setInt( 5, userId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.planId = result.getInt( "plan_id" );
                    this.calDate = result.getInt( "cal_date" );
                    this.hotelId = result.getString( "hotel_id" );
                    this.userId = result.getInt( "user_id" );
                    this.dispDate = result.getInt( "disp_date" );
                    this.dispTime = result.getInt( "disp_time" );
                    this.confirmDate = result.getInt( "confirm_date" );
                    this.confirmTime = result.getInt( "confirm_time" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvMonthlyConfirm.getData] Exception=" + e.toString() );
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
                this.planId = result.getInt( "plan_id" );
                this.calDate = result.getInt( "cal_date" );
                this.hotelId = result.getString( "hotel_id" );
                this.userId = result.getInt( "user_id" );
                this.dispDate = result.getInt( "disp_date" );
                this.dispTime = result.getInt( "disp_time" );
                this.confirmDate = result.getInt( "check_date" );
                this.confirmTime = result.getInt( "check_time" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvMonthlyConfirm.setData] Exception=" + e.toString() );
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

        query = "INSERT hh_rsv_monthly_confirm SET ";
        query += " id = ?,";
        query += " plan_id = ?,";
        query += " cal_date = ?,";
        query += " hotel_id = ?,";
        query += " user_id = ?,";
        query += " disp_date = ?,";
        query += " disp_time = ?,";
        query += " confirm_date = ?,";
        query += " confirm_time = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.planId );
            prestate.setInt( 3, this.calDate );
            prestate.setString( 4, this.hotelId );
            prestate.setInt( 5, this.userId );
            prestate.setInt( 6, this.dispDate );
            prestate.setInt( 7, this.dispTime );
            prestate.setInt( 8, this.confirmDate );
            prestate.setInt( 9, this.confirmTime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvMonthlyConfirm.insertData] Exception=" + e.toString() );
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
    public boolean updateData(int Id, int planId, int calDate, String hotelId, int userId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_monthly_confirm SET ";
        query += " disp_date = ?,";
        query += " disp_time = ?,";
        query += " confirm_date = ?,";
        query += " confirm_time = ?";
        query += " WHERE id = ?";
        query += " AND plan_id = ?";
        query += " AND cal_date = ?";
        query += " AND hotel_id = ?";
        query += " AND user_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.dispDate );
            prestate.setInt( 2, this.dispTime );
            prestate.setInt( 3, this.confirmDate );
            prestate.setInt( 4, this.confirmTime );
            prestate.setInt( 5, Id );
            prestate.setInt( 6, planId );
            prestate.setInt( 7, calDate );
            prestate.setString( 8, hotelId );
            prestate.setInt( 9, userId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvMonthlyConfirm.updateData] Exception=" + e.toString() );
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
