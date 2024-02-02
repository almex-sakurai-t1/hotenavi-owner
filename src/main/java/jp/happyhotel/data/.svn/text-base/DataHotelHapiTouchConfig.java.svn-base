package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Sep 09 11:59:04 JST 2011
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ハピホテタッチ設定クラス
 * 
 * @author tashiro-s1
 */
public class DataHotelHapiTouchConfig implements Serializable
{

    private static final long serialVersionUID = -5489421141422985606L;
    private int               id;
    private int               lastUpdate;
    private int               lastUptime;
    private int               hotelInfoInterval;
    private int               slipNoDigit;
    private int               ciUpdate;
    private int               ciUptime;

    public DataHotelHapiTouchConfig()
    {
        id = 0;
        lastUpdate = 0;
        lastUptime = 0;
        hotelInfoInterval = 0;
        slipNoDigit = 0;
        ciUpdate = 0;
        ciUptime = 0;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getLastUpdate()
    {
        return this.lastUpdate;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public int getLastUptime()
    {
        return this.lastUptime;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public int getHotelInfoInterval()
    {
        return this.hotelInfoInterval;
    }

    public void setHotelInfoInterval(int hotelInfoInterval)
    {
        this.hotelInfoInterval = hotelInfoInterval;
    }

    public int getSlipNoDigit()
    {
        return this.slipNoDigit;
    }

    public void setSlipNoDigit(int slipNoDigit)
    {
        this.slipNoDigit = slipNoDigit;
    }

    public int getCiUpdate()
    {
        return this.ciUpdate;
    }

    public void setCiUpdate(int ciUpdate)
    {
        this.ciUpdate = ciUpdate;
    }

    public int getCiUptime()
    {
        return this.ciUptime;
    }

    public void setCiUptime(int ciUptime)
    {
        this.ciUptime = ciUptime;
    }

    /**
     * ハピホテタッチ設定データ取得
     * 
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_hapitouch_config WHERE id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.hotelInfoInterval = result.getInt( "hotel_info_interval" );
                    this.slipNoDigit = result.getInt( "slip_no_digit" );
                    this.ciUpdate = result.getInt( "ci_update" );
                    this.ciUptime = result.getInt( "ci_uptime" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHapiTouchConfig.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ハピホテタッチ設定データ取得
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.hotelInfoInterval = result.getInt( "hotel_info_interval" );
                this.slipNoDigit = result.getInt( "slip_no_digit" );
                this.ciUpdate = result.getInt( "ci_update" );
                this.ciUptime = result.getInt( "ci_uptime" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHapiTouchConfig.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ハピホテタッチ設定データ追加
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

        query = "INSERT hh_hotel_hapitouch_config SET ";
        query += " id = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?,";
        query += " hotel_info_interval = ?,";
        query += " slip_no_digit = ?,";
        query += " ci_update = ?,";
        query += " ci_uptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.lastUpdate );
            prestate.setInt( 3, this.lastUptime );
            prestate.setInt( 4, this.hotelInfoInterval );
            prestate.setInt( 5, this.slipNoDigit );
            prestate.setInt( 6, this.ciUpdate );
            prestate.setInt( 7, this.ciUptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHapiTouchConfig.insertData] Exception=" + e.toString() );
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
     * ハピホテタッチ設定データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_hapitouch_config SET ";
        query += " last_update = ?,";
        query += " last_uptime = ?,";
        query += " hotel_info_interval = ?,";
        query += " slip_no_digit = ?,";
        query += " ci_update = ?,";
        query += " ci_uptime = ?";
        query += " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.lastUpdate );
            prestate.setInt( 2, this.lastUptime );
            prestate.setInt( 3, this.hotelInfoInterval );
            prestate.setInt( 4, this.slipNoDigit );
            prestate.setInt( 5, this.ciUpdate );
            prestate.setInt( 6, this.ciUptime );
            prestate.setInt( 7, id );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelHapiTouchConfig.updateData] Exception=" + e.toString() );
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
