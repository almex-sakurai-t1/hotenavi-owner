package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hotenavi2.common.LogLib;

public class ProjectRolesGet
{
    private final String className      = "ProjectRoleGet";
    private final String function       = "projects";
    private final String functionSub    = "roles";
    private String       role_id;
    private String       description;
    private int          response_code;
    private String       error_message;
    private final String MagazineCaller = "magazine.caller";
    private final int    NomalResponse  = 200;
    private final int    NoContent      = 204;

    public ProjectRolesGet()
    {
        role_id = "";
        description = "";
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    public String getRoleId()
    {
        return role_id;
    }

    public String getDescription()
    {
        return description;
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
            if ( jsonArray.length() == 0 )
            {
                setResponseCode( NoContent );
            }
            else
            {
                for( int i = 0 ; i < jsonArray.length() ; i++ )
                {
                    JSONObject obj = jsonArray.getJSONObject( i );
                    if ( obj.getString( "id" ).equals( MagazineCaller ) )
                    {
                        role_id = obj.getString( "id" );
                        description = obj.getString( "description" );
                        return;
                    }
                }
                if ( role_id.equals( "" ) )
                {
                    setResponseCode( NoContent );
                }
            }
        }
    }
}
