package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagData;

public class MagazineRecipientAdd
{
    private final String className        = "MagazineRecipientAdd";
    private final String function         = "magazine";
    private final String functionSub      = "recipients";
    private int          response_code;
    private String       error_message;
    private final int    CreatedResponse  = 201;                   // çÏê¨ê¨å˜
    private final int    AcceptedResponse = 202;                   // ñ¢èàóù

    private String       recipient_id;

    public MagazineRecipientAdd()
    {
        this.recipient_id = "";
        this.response_code = CreatedResponse;
        this.error_message = "";
    }

    public void setRecipientId(String recipientId)
    {
        this.recipient_id = recipientId;
    }

    public String getRecipientId()
    {
        return this.recipient_id;
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

    public void execute(String hotelId, int historyId)
    {
        LogLib log = new LogLib();
        DataMagData dmd = new DataMagData();
        String jsonOut = "";
        if ( dmd.getData( hotelId, historyId ) )
        {
            if ( dmd.getRecipientId().equals( "" ) )
            {
                if ( dmd.getMagDataList( hotelId, historyId ) )
                {
                    ApiForMailmagazine am = new ApiForMailmagazine();
                    try
                    {
                        jsonOut = am.execute( hotelId, "POST", function + "/" + functionSub, setJson( dmd ), dmd.getApiKey() );
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
                    setResponseCode( dmd.getResponseCode() );
                }
            }
            else
            {
                setResponseCode( AcceptedResponse ); // ìoò^çœÇ»ÇÃÇ≈ñ¢èàóù
            }
        }
        else
        {
            setResponseCode( dmd.getResponseCode() );
        }
        if ( response_code == CreatedResponse )
        {
            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                JsonNode json = objectMapper.readTree( jsonOut );
                setRecipientId( json.get( "id" ).textValue() );
                dmd.updateData( hotelId, historyId, "recipient_id", json.get( "id" ).textValue() );
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

    public String setJson(DataMagData dmd)
    {
        JSONObject jsonAll = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        for( int i = 0 ; i < dmd.getEmails().length ; i++ )
        {
            jsonArr.put( dmd.getEmails()[i] );
        }
        jsonAll.put( "emails", jsonArr );
        return jsonAll.toString();
    }

}
