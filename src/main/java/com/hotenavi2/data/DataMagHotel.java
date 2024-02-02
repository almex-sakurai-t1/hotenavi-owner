package com.hotenavi2.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hotenavi2.common.DBConnection;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.MailSystemConf;

public class DataMagHotel implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -1;
    private String            name;
    private String            email;
    private String            api_key;
    private String            from_email;
    private String            reply_to_email;
    private String            address;
    private String            city;
    private String            zip_code;
    private String            sender_id;
    private String            error_message;
    private int               response_code;

    public DataMagHotel()
    {
        this.email = "";
        this.name = "";
        this.api_key = "";
        this.from_email = "";
        this.reply_to_email = "";
        this.address = "";
        this.city = "";
        this.zip_code = "";
        this.sender_id = "";
        this.error_message = "";
        this.response_code = 0;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getName()
    {
        return this.name;
    }

    public String getApiKey()
    {
        return this.api_key;
    }

    public String getFromEmail()
    {
        return this.from_email;
    }

    public String getReplyToEmail()
    {
        return this.reply_to_email;
    }

    public String getAddress()
    {
        return this.address;
    }

    public String getCity()
    {
        return this.city;
    }

    public String getZipCode()
    {
        return this.zip_code;
    }

    public String getSenderId()
    {
        return this.sender_id;
    }

    public int getResponseCode()
    {
        return this.response_code;
    }

    public void setResponseCode(int responsCode)
    {
        this.response_code = responsCode;
    }

    public String getErrorMessage()
    {
        return this.error_message;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.error_message = errorMessage;
    }

    /**
     * mag_hotel データ取得
     * 
     * @param hotelId
     * @return boolean
     */
    public boolean getData(String hotelId)
    {
        LogLib log = new LogLib();
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT";
        query += " hotel.name";
        query += ",mag_hotel.address AS email";
        query += ",mag_hotel.mag_address AS from_email";
        query += ",mag_hotel.reply_to_address AS reply_to_email";
        query += ",mag_hotel.api_key";
        query += ",CASE WHEN hh_hotel_basic.id IS NULL THEN hh_master_chain.zip_code ELSE hh_hotel_basic.zip_code END zip_code";
        query += ",CASE WHEN hh_hotel_basic.id IS NULL THEN CONCAT(hh_master_chain.pref_name,hh_master_chain.address1) ELSE CONCAT(hh_hotel_basic.pref_name,hh_hotel_basic.address1) END city";
        query += ",CASE WHEN hh_hotel_basic.id IS NULL THEN hh_master_chain.address2 ELSE hh_hotel_basic.address2 END address";
        query += ",mag_hotel.sender_id";
        query += " FROM mag_hotel";
        query += " INNER JOIN hotel ON mag_hotel.hotel_id = hotel.hotel_id";
        query += " LEFT JOIN hh_hotel_basic ON hh_hotel_basic.hotenavi_id = mag_hotel.hotel_id";
        query += " LEFT JOIN hh_master_chain ON hh_master_chain.group_hotenavi = mag_hotel.hotel_id";
        query += " WHERE mag_hotel.mag_address <> ''";
        query += " AND mag_hotel.hotel_id = ? ";
        query += " HAVING zip_code IS NOT NULL AND zip_code <> ''";
        try
        {
            setErrorMessage( "[DataMagHotel.getData] no data" );
            setResponseCode( 404 );
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                this.name = result.getString( "name" );
                this.email = result.getString( "email" );
                this.from_email = result.getString( "from_email" );
                this.reply_to_email = result.getString( "reply_to_email" );
                this.api_key = MailSystemConf.decrypt( result.getString( "api_key" ) );
                this.zip_code = result.getString( "zip_code" );
                this.city = result.getString( "city" );
                this.address = result.getString( "address" );
                this.sender_id = result.getString( "sender_id" );
                setResponseCode( 200 );
                setErrorMessage( "" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            log.error( "[DataMagHotel.getData] Exception=" + e.toString() );
            setErrorMessage( "[DataMagHotel.getData] Exception=" + e.toString() );
            setResponseCode( 500 );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * mag_hotel データ更新
     * 
     * @param hotelId
     * @return boolean
     */
    public boolean updateData(String hotelId, String item, String value)
    {
        LogLib log = new LogLib();
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "UPDATE mag_hotel SET ";
        query += item + " = ? ";
        query += " WHERE hotel_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, value );
            prestate.setString( 2, hotelId );

            if ( prestate.executeUpdate() > 0 )
            {
                setResponseCode( 200 );
                setErrorMessage( "" );
                ret = true;
            }
            else
            {
                setResponseCode( 202 );
                setErrorMessage( "" );
            }
        }
        catch ( Exception e )
        {
            log.error( "[DataMagHotel.updateData] Exception=" + e.toString() );
            setErrorMessage( "[DataMagHotel.updateData] Exception=" + e.toString() );
            setResponseCode( 500 );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
