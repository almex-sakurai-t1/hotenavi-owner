package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.MailSystemConf;
import com.hotenavi2.data.DataMagHotel;

public class ProjectApiKeysGet
{
    private final String className     = "ProjectApiKeyGet";
    private final String function      = "projects";
    private final String functionSub   = "api-keys";
    private String[]     apiKeyId;
    private String[]     apiKeyValue;
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;
    private final int    NoContent     = 204;

    public ProjectApiKeysGet()
    {
        this.apiKeyId = null;
        this.apiKeyValue = null;
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    public String getApiKeyId()[]
    {
        return this.apiKeyId;
    }

    public String getApiKeyValue()[]
    {
        return this.apiKeyValue;
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
    public void execute(String hotelId)
    {
        LogLib log = new LogLib();
        String jsonOut = "";
        ApiForMailmagazine am = new ApiForMailmagazine();
        try
        {
            jsonOut = am.execute( hotelId, "GET", function + "/" + "hotenavi_" + hotelId + "/" + functionSub, "" );
            setResponseCode( am.getResponseCode() );
            setErrorMessage( am.getErrorMessage() );
        }
        catch ( IOException e )
        {
            log.error( className + " Error=" + e.toString() );
            setResponseCode( am.getResponseCode() );
            setErrorMessage( className + " Error=" + e.toString() );
        }
        if ( response_code == NomalResponse )
        {
            /* JSONObject は { から始まらないとエラーになるので、先頭を { にし、またgetJSONArrayのために配列に'array'の名称を付加した。 */
            jsonOut = "{ 'array':" + jsonOut + "}";
            JSONObject json = new JSONObject( jsonOut );
            JSONArray jsonArray = json.getJSONArray( "array" );
            System.out.println( "jsonArray.length:" + jsonArray.length() );
            if ( jsonArray.length() == 0 )
            {
                setResponseCode( NoContent );
            }
            else
            {
                apiKeyId = new String[jsonArray.length()];
                apiKeyValue = new String[jsonArray.length()];
                for( int i = 0 ; i < jsonArray.length() ; i++ )
                {
                    JSONObject obj = jsonArray.getJSONObject( i );
                    apiKeyId[i] = obj.getString( "id" );
                    apiKeyValue[i] = obj.getString( "value" );
                }
                DataMagHotel dmh = new DataMagHotel();
                dmh.updateData( hotelId, "api_key", MailSystemConf.encrypt( apiKeyValue[0] ) );
            }
        }
    }
}
