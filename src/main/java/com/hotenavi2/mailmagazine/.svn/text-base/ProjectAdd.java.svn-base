package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagHotel;

public class ProjectAdd
{
    private final String className       = "ProjectAdd";
    private final String function        = "projects";
    private String       projectId;
    private String       name;
    private String       email;
    private int          response_code;
    private String       error_message;
    private final int    CreatedResponse = 201;

    public ProjectAdd()
    {
        this.projectId = "";
        this.name = "";
        this.email = "";
        this.response_code = CreatedResponse;
        this.error_message = "";
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public String getProjctId()
    {
        return this.projectId;
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
        LogLib log = new LogLib();
        DataMagHotel dmh = new DataMagHotel();
        String jsonOut = "";
        if ( dmh.getData( hotelId ) )
        {
            setProjectId( "hotenavi_" + hotelId );
            setName( dmh.getName() );
            setEmail( dmh.getEmail() );
            ApiForMailmagazine am = new ApiForMailmagazine();
            try
            {
                jsonOut = am.execute( hotelId, "POST", function + "/" + projectId, setJson() );
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
        if ( response_code == CreatedResponse )
        {
            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                JsonNode json = objectMapper.readTree( jsonOut );
                setProjectId( json.get( "id" ).textValue() );
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

    public String setJson()
    {
        JSONObject json = new JSONObject();
        json.put( "name", this.name );
        json.put( "email", this.email );
        return json.toString();
    }
}
