package com.hotenavi2.mailmagazine;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;

public class ProjectRoleDelete
{
    private final String className     = "ProjectRoleDelete";
    private final String function      = "projects";
    private final String functionSub   = "roles";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 204;

    public ProjectRoleDelete()
    {
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    class Roles
    {
        public String id;
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

            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                List<Roles> roles = Arrays.asList( objectMapper.readValue( jsonOut, Roles[].class ) );
                for( Roles role : roles )
                {
                    am.execute( hotelId, "DELETE", function + "/" + "hotenavi_" + hotelId + "/" + functionSub + "/" + role.id, "" );
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
