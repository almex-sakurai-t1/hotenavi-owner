package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagHotel;

public class MagazineSenderAdd
{
    private final String className        = "MagazineSenderAdd";
    private final String function         = "magazine";
    private final String functionSub      = "senders";
    private int          response_code;
    private String       error_message;
    private final int    CreatedResponse  = 201;                // çÏê¨ê¨å˜
    private final int    AcceptedResponse = 202;                // ñ¢èàóù

    private String       sender_id;

    public MagazineSenderAdd()
    {
        this.sender_id = "";
        this.response_code = CreatedResponse;
        this.error_message = "";
    }

    public void setSenderId(String senderId)
    {
        this.sender_id = senderId;
    }

    public String getSenderId()
    {
        return this.sender_id;
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
            if ( dmh.getSenderId().equals( "" ) )
            {
                ApiForMailmagazine am = new ApiForMailmagazine();
                try
                {
                    jsonOut = am.execute( hotelId, "POST", function + "/" + functionSub, setJson( dmh ), dmh.getApiKey() );
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
                setSenderId( json.get( "id" ).textValue() );
                dmh.updateData( hotelId, "sender_id", json.get( "id" ).textValue() );
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

    public String setJson(DataMagHotel dmh)
    {
        JSONObject jsonAll = new JSONObject();
        JSONObject jsonFrom = new JSONObject();
        JSONObject jsonReplyTo = new JSONObject();
        jsonFrom.put( "email", dmh.getFromEmail() );
        jsonFrom.put( "name", dmh.getName() );
        jsonAll.put( "from", jsonFrom );
        jsonReplyTo.put( "email", dmh.getReplyToEmail() );
        jsonReplyTo.put( "name", dmh.getName() );
        jsonAll.put( "reply_to", jsonReplyTo );
        jsonAll.put( "address", dmh.getAddress() );
        jsonAll.put( "city", dmh.getCity() );
        jsonAll.put( "zip_code", dmh.getZipCode() );
        jsonAll.put( "country", "Japan" );
        System.out.println( className + " json=" + jsonAll );
        return jsonAll.toString();
    }
}
