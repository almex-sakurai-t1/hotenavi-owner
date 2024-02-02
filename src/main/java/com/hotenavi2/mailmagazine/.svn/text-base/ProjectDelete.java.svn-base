package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagHotel;

public class ProjectDelete
{
    private final String className     = "ProjectDelete";
    private final String function      = "projects";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 204;

    public ProjectDelete()
    {
        this.response_code = NomalResponse;
        this.error_message = "";
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

    public void execute(String hotelId)
    {
        LogLib log = new LogLib();
        ApiForMailmagazine am = new ApiForMailmagazine();
        DataMagHotel dmh = new DataMagHotel();
        try
        {
            am.execute( hotelId, "DELETE", function + "/" + "hotenavi_" + hotelId, "" );
            setResponseCode( am.getResponseCode() );
            setErrorMessage( am.getErrorMessage() );
            dmh.updateData( hotelId, "api-key", "" );
            dmh.updateData( hotelId, "sender_id", "" );
        }
        catch ( IOException e )
        {
            log.error( className + " Error=" + e.toString() );
            setResponseCode( am.getResponseCode() );
            setErrorMessage( className + " Error=" + e.toString() );
        }
    }
}
