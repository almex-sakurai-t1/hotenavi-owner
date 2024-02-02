package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;

public class ProjectGet
{
    private final String className     = "ProjectGet";
    private final String function      = "projects";
    private String       name;
    private String       email;
    private String       error_message;
    private int          response_code;
    private final int    NomalResponse = 200;

    public ProjectGet()
    {
        this.name = "";
        this.email = "";
        this.error_message = "";
        this.response_code = NomalResponse;
    }

    public String getName()
    {
        return this.name;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmail(String email)
    {
        this.email = email;
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
        String jsonOut = "";
        LogLib log = new LogLib();
        ApiForMailmagazine am = new ApiForMailmagazine();
        try
        {
            jsonOut = am.execute( hotelId, "GET", function + "/" + "hotenavi_" + hotelId, "" );
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
                setName( json.get( "name" ).textValue() );
                setEmail( json.get( "email" ).textValue() );
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
