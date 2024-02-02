package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;

public class ProjectRoleGet
{
    private final String className     = "ProjectRoleGet";
    private final String function      = "projects";
    private final String functionSub   = "roles";
    private String       description;
    private final String role_id       = "magazine.caller";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;

    public ProjectRoleGet()
    {
        description = "";
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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
        String jsonOut = "";
        ApiForMailmagazine am = new ApiForMailmagazine();
        try
        {
            jsonOut = am.execute( hotelId, "GET", function + "/" + "hotenavi_" + hotelId + "/" + functionSub + "/" + role_id, "" );
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
            ObjectMapper objectMapper = new ObjectMapper();
            if ( this.response_code == NomalResponse )
            {
                try
                {
                    JsonNode json = objectMapper.readTree( jsonOut );
                    setDescription( json.get( "description" ).textValue() );
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
}
