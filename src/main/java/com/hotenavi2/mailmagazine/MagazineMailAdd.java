package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagData;

public class MagazineMailAdd
{
    private final String className        = "MagazineMailAdd";
    private final String function         = "magazine";
    private final String functionSub      = "mails";
    private int          response_code;
    private String       error_message;
    private final int    CreatedResponse  = 201;              // çÏê¨ê¨å˜
    private final int    AcceptedResponse = 202;              // ñ¢èàóù

    private String       mail_id;

    public MagazineMailAdd()
    {
        this.mail_id = "";
        this.response_code = CreatedResponse;
        this.error_message = "";
    }

    public void setMailId(String mailId)
    {
        this.mail_id = mailId;
    }

    public String getMailId()
    {
        return this.mail_id;
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
            if ( dmd.getMailId().equals( "" ) )
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
                setResponseCode( AcceptedResponse );// ìoò^çœÇ»ÇÃÇ≈ñ¢èàóù
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
                setMailId( json.get( "id" ).textValue() );
                dmd.updateData( hotelId, historyId, "mail_id", json.get( "id" ).textValue() );
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
        JSONObject jsonSchedule = new JSONObject();
        jsonAll.put( "title", dmd.getTitle() );
        jsonAll.put( "subject", dmd.getSubject() );
        jsonAll.put( "plain_contents", dmd.getPlainContents() );
        jsonAll.put( "sender_id", dmd.getSenderId() );
        jsonAll.put( "recipient_id", dmd.getRecipientId() );
        jsonAll.put( "unsubscribe_url", dmd.getUnsubscribeUrl() );
        jsonSchedule.put( "send_at", dmd.getSendAt() );
        jsonAll.put( "schedule", jsonSchedule );
        return jsonAll.toString();
    }
}
