package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagHotel;

public class MagazineSenderPatch
{
    private final String className     = "MagazineSenderPatch";
    private final String function      = "magazine";
    private final String functionSub   = "senders";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;

    private String       sender_id;
    private int          revision;

    public MagazineSenderPatch()
    {
        this.sender_id = "";
        this.revision = 0;
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    public void setSenderId(String senderId)
    {
        this.sender_id = senderId;
    }

    public String getSenderId()
    {
        return this.sender_id;
    }

    public void setRevision(int revision)
    {
        this.revision = revision;
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
        DataMagHotel dmh = new DataMagHotel();
        if ( dmh.getData( hotelId ) )
        {
            ApiForMailmagazine am = new ApiForMailmagazine();
            try
            {
                am.execute( hotelId, "PATCH", function + "/" + functionSub + "/" + dmh.getSenderId(), setJson( dmh ), dmh.getApiKey() );
                setResponseCode( am.getResponseCode() );
                setErrorMessage( am.getErrorMessage() );
            }
            catch ( IOException e )
            {
                log.error( className + " Error=" + e.toString() );
                setResponseCode( am.getResponseCode() );
                setErrorMessage( className + " Error=" + e.toString() );
            }
        }
        else
        {
            setResponseCode( dmh.getResponseCode() );
        }
    }

    public String setJson(DataMagHotel dmh)
    {
        JSONObject jsonAll = new JSONObject();
        JSONObject jsonFrom = new JSONObject();
        JSONObject jsonReplyTo = new JSONObject();
        jsonFrom.put( "email", dmh.getFromEmail() );
        jsonFrom.put( "name", dmh.getName() );
        jsonAll.put( "from", jsonFrom );
        jsonReplyTo.put( "email", dmh.getReplyToEmail() );
        jsonReplyTo.put( "name", dmh.getName() );
        jsonAll.put( "reply_to", jsonReplyTo );
        jsonAll.put( "address", dmh.getAddress() );
        jsonAll.put( "city", dmh.getCity() );
        jsonAll.put( "zip_code", dmh.getZipCode() );
        jsonAll.put( "country", "Japan" );
        jsonAll.put( "revision", this.revision );
        return jsonAll.toString();
    }
}
