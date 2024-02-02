package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;

public class ProjectApiKeyGet
{
    private final String className     = "ProjectApiKeyGet";
    private final String function      = "projects";
    private final String functionSub   = "api-keys";
    private String       api_key_value;
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;

    public ProjectApiKeyGet()
    {
        this.api_key_value = "";
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    public String getApiKeyValue()
    {
        return this.api_key_value;
    }

    public void setApiKeyValue(String apiKeyValue)
    {
        this.api_key_value = apiKeyValue;
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

    public void execute(String hotelId, String apiKey)
    {
        String jsonOut = "";
        LogLib log = new LogLib();
        ApiForMailmagazine am = new ApiForMailmagazine();
        try
        {
            jsonOut = am.execute( hotelId, "GET", function + "/" + "hotenavi_" + hotelId + "/" + functionSub + "/" + apiKey, "" );
            setResponseCode( am.getResponseCode() );
            setErrorMessage( am.getErrorMessage() );
        }
        catch ( IOException e )
        {
            log.error( className + " Error=" + e.toString() );
            setResponseCode( am.getResponseCode() );
            setErrorMessage( className + " Error=" + e.toString() );
        }
        ObjectMapper objectMapper = new ObjectMapper();
        if ( this.response_code == NomalResponse )
        {
            try
            {
                JsonNode json = objectMapper.readTree( jsonOut );
                setApiKeyValue( json.get( "value" ).textValue() );
            }
            catch ( JsonProcessingException e )
            {
                log.error( className + " JsonProcessingException Error=" + e.toString() );
            }
            catch ( IOException e )
            {
                log.error( className + " IOException Error=" + e.toString() );
            }
        }
    }
}
