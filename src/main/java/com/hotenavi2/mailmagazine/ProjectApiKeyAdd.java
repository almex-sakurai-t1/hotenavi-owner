package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.MailSystemConf;
import com.hotenavi2.data.DataMagHotel;

public class ProjectApiKeyAdd
{
    private final String className        = "ProjectApiKeyAdd";
    private final String function         = "projects";
    private final String functionSub      = "api-keys";
    private String       projectId;
    private String       apiKeyId;
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse    = 200;
    private final int    CreatedResponse  = 201;               // çÏê¨ê¨å˜
    private final int    AcceptedResponse = 202;               // ñ¢èàóù

    public ProjectApiKeyAdd()
    {
        this.projectId = "";
        this.apiKeyId = "";
        this.response_code = CreatedResponse;
        this.error_message = "";
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public void setApiKeyId(String apiKeyId)
    {
        this.apiKeyId = apiKeyId;
    }

    public String getApiKeyId()
    {
        return this.apiKeyId;
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
        String jsonOut = "";
        if ( dmh.getData( hotelId ) )
        {
            if ( dmh.getApiKey().equals( "" ) )
            {
                setProjectId( "hotenavi_" + hotelId );
                ApiForMailmagazine am = new ApiForMailmagazine();
                try
                {
                    jsonOut = am.execute( hotelId, "POST", function + "/" + projectId + "/" + functionSub, "" );
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
                setResponseCode( AcceptedResponse );// ìoò^çœÇ»ÇÃÇ≈ñ¢èàóù
            }
        }
        else
        {
            setResponseCode( dmh.getResponseCode() );
        }
        if ( response_code == CreatedResponse )
        {
            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                JsonNode json = objectMapper.readTree( jsonOut );
                setApiKeyId( json.get( "id" ).textValue() );
                ProjectApiKeyGet projectApiKeyGet = new ProjectApiKeyGet();
                projectApiKeyGet.execute( hotelId, getApiKeyId() );
                if ( projectApiKeyGet.getResponseCode() == NomalResponse )
                {
                    dmh.updateData( hotelId, "api_key", MailSystemConf.encrypt( projectApiKeyGet.getApiKeyValue() ) );
                }
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
