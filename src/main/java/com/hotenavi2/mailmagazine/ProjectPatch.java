package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagHotel;

public class ProjectPatch
{
    private final String className     = "ProjectPatch";
    private final String function      = "projects";
    private String       projectId;
    private String       name;
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;

    public ProjectPatch()
    {
        this.projectId = "";
        this.name = "";
        this.response_code = NomalResponse;
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
        if ( dmh.getData( hotelId ) )
        {
            setProjectId( "hotenavi_" + hotelId );
            setName( dmh.getName() );
            ApiForMailmagazine am = new ApiForMailmagazine();
            try
            {
                am.execute( hotelId, "PATCH", function + "/" + projectId, setJson() );
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
    }

    public String setJson()
    {
        JSONObject json = new JSONObject();
        json.put( "name", this.name );
        return json.toString();
    }
}
