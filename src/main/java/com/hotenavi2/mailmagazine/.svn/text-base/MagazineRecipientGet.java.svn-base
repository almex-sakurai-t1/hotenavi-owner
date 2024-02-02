package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagData;

public class MagazineRecipientGet
{
    private final String className     = "MagazineRecipientGet";
    private final String function      = "magazine";
    private final String functionSub   = "recipients";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;

    private String[]     emails;
    private int          revision;

    public MagazineRecipientGet()
    {
        this.emails = null;
        this.revision = 0;
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    public void setRevision(int revision)
    {
        this.revision = revision;
    }

    public int getRevision()
    {
        return this.revision;
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

    @SuppressWarnings("deprecation")
    public void execute(String hotelId, int historyId)
    {
        LogLib log = new LogLib();
        DataMagData dmd = new DataMagData();
        String jsonOut = "";
        ApiForMailmagazine am = new ApiForMailmagazine();
        if ( dmd.getData( hotelId, historyId ) )
        {
            try
            {
                jsonOut = am.execute( hotelId, "GET", function + "/" + functionSub + "/" + dmd.getRecipientId(), "", dmd.getApiKey() );
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
        if ( response_code == NomalResponse )
        {
            JSONObject jsonObj = new JSONObject( jsonOut.toString() );
            JSONArray emails = jsonObj.getJSONArray( "emails" );
            for( int i = 0 ; i < emails.length() ; i++ )
            {
                String email = emails.getJSONObject( i ).toString();
                this.emails[i] = email;
            }
            setRevision( (Integer)jsonObj.get( "revision" ) );
        }
    }
}
