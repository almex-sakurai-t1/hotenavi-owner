package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagHotel;

public class ProjectRoleAdd
{
    private final String className       = "ProjectRoleAdd";
    private final String function        = "projects";
    private final String functionSub     = "roles";
    private String       project_id;
    private String       role_id         = "magazine.caller";
    private int          response_code;
    private String       error_message;
    private final int    CreatedResponse = 201;              // çÏê¨ê¨å˜

    public ProjectRoleAdd()
    {
        this.project_id = "";
        this.role_id = "magazine.caller";
        this.response_code = CreatedResponse;
        this.error_message = "";
    }

    public void setProjectId(String projectId)
    {
        this.project_id = projectId;
    }

    public void setRoleId(String roleId)
    {
        this.role_id = roleId;
    }

    public String getRoleId()
    {
        return this.role_id;
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
            ApiForMailmagazine am = new ApiForMailmagazine();
            try
            {
                jsonOut = am.execute( hotelId, "POST", function + "/" + project_id + "/" + functionSub + "/" + role_id, "" );
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
                setRoleId( json.get( "id" ).textValue() );
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
