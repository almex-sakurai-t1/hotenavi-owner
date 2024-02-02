package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagData;

public class MagazineRecipientPut
{
    private final String className     = "MagazineRecipientPut";
    private final String function      = "magazine";
    private final String functionSub   = "recipients";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;

    private String       recipient_id;
    private int          revision;

    public MagazineRecipientPut()
    {
        this.recipient_id = "";
        this.revision = 0;
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    public void setRecipientId(String recipientId)
    {
        this.recipient_id = recipientId;
    }

    public String getRecipientId()
    {
        return this.recipient_id;
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

    public void execute(String hotelId, int historyId)
    {
        LogLib log = new LogLib();
        DataMagData dmd = new DataMagData();
        if ( dmd.getData( hotelId, historyId ) )
        {
            ApiForMailmagazine am = new ApiForMailmagazine();
            try
            {
                am.execute( hotelId, "PUT", function + "/" + functionSub + "/" + dmd.getRecipientId(), setJson( dmd ), dmd.getApiKey() );
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
            setResponseCode( dmd.getResponseCode() );
        }
    }

    public String setJson(DataMagData dmd)
    {
        JSONObject jsonAll = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        for( int i = 0 ; i < dmd.getEmails().length ; i++ )
        {
            jsonArr.put( dmd.getEmails()[i] );
        }
        jsonAll.put( "emails", jsonArr );
        jsonAll.put( "revision", this.revision );
        return jsonAll.toString();
    }
}
