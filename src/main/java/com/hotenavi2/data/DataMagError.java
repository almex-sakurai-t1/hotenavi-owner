package com.hotenavi2.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hotenavi2.common.DBConnection;
import com.hotenavi2.common.LogLib;

public class DataMagError implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -1;
    private String            hotel_id;
    private String            method;
    private String            function;
    private String            json;
    private int               response_code;
    private String            message;

    public DataMagError()
    {
        this.hotel_id = "";
        this.method = "";
        this.function = "";
        this.json = "";
        this.message = "";
        this.response_code = 0;
    }

    public String getHotelId()
    {
        return this.hotel_id;
    }

    public String getMethod()
    {
        return this.method;
    }

    public String getFunction()
    {
        return this.function;
    }

    public String getJson()
    {
        return this.json;
    }

    public String getMessage()
    {
        return this.message;
    }

    public int getResponseCode()
    {
        return this.response_code;
    }

    public void setHotelId(String hotelId)
    {
        this.hotel_id = hotelId;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public void setFunction(String function)
    {
        this.function = function;
    }

    public void setJson(String json)
    {
        this.json = json;
    }

    public void setResponseCode(int responsCode)
    {
        this.response_code = responsCode;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * mag_error データ作成
     * 
     * @param hotelId
     * @return boolean
     */
    public boolean insertData()
    {
        LogLib log = new LogLib();
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        int i = 1;
        query = "INSERT mag_error SET ";
        query += " hotel_id = ?";
        query += ", method = ?";
        query += ", function = ?";
        query += ", json = ?";
        query += ", response_code = ?";
        query += ", message = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.hotel_id );
            prestate.setString( i++, this.method );
            prestate.setString( i++, this.function );
            prestate.setString( i++, this.json );
            prestate.setInt( i++, this.response_code );
            prestate.setString( i++, this.message );

            ret = (prestate.executeUpdate() > 0);

        }
        catch ( Exception e )
        {
            log.error( "[DataMagError.insertData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
