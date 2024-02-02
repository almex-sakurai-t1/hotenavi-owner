package com.hotenavi2.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hotenavi2.common.DBConnection;
import com.hotenavi2.common.LogLib;

public class DataHotel implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -1;
    private String            name;
    private String            error_message;
    private int               response_code;

    public DataHotel()
    {
        this.name = "";
        this.error_message = "";
        this.response_code = 0;
    }

    public String getName()
    {
        return this.name;
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
     * addressŽæ“¾
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
        query = "SELECT name FROM hotel WHERE hotel_id = ? ";
        try
        {
            setErrorMessage( "[DataHotel.getData] no data" );
            setResponseCode( 404 );
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.name = result.getString( "name" );
                    ret = true;
                    setResponseCode( 200 );
                    setErrorMessage( "" );
                }
            }
        }
        catch ( Exception e )
        {
            log.error( "[DataHotel.getData] Exception=" + e.toString() );
            setErrorMessage( "[DataHotel.getData] Exception=" + e.toString() );
            setResponseCode( 500 );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
