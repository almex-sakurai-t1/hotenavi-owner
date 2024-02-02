package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagData;

public class MagazineMailGet
{
    private final String className     = "MagazineMailGet";
    private final String function      = "magazine";
    private final String functionSub   = "mails";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;

    private String       title;
    private String       subject;
    private String       plain_contents;
    private String       sender_id;
    private String       recipient_id;
    private String       unsubscribe_url;
    private String       send_at;
    private int          status;
    private int          revision;

    public MagazineMailGet()
    {
        this.title = "";
        this.subject = "";
        this.plain_contents = "";
        this.sender_id = "";
        this.recipient_id = "";
        this.unsubscribe_url = "";
        this.send_at = "";
        this.status = 0;
        this.revision = 0;
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getSubject()
    {
        return this.subject;
    }

    public String getPlainContents()
    {
        return this.plain_contents;
    }

    public String getSenderId()
    {
        return this.sender_id;
    }

    public String getRecipientId()
    {
        return this.recipient_id;
    }

    public String getUnsubscribeUrl()
    {
        return this.unsubscribe_url;
    }

    public String getSendAt()
    {
        return this.send_at;
    }

    public int getStatus()
    {
        return this.status;
    }

    public int getRevision()
    {
        return this.revision;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public void setPlainContents(String plainContents)
    {
        this.plain_contents = plainContents;
    }

    public void setSenderId(String senderId)
    {
        this.sender_id = senderId;
    }

    public void setRecipientId(String recipientId)
    {
        this.recipient_id = recipientId;
    }

    public void setUnsubscribeUrl(String unsubscribeUrl)
    {
        this.unsubscribe_url = unsubscribeUrl;
    }

    public void setSendAt(String sendAt)
    {
        this.send_at = sendAt;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setRevision(int revision)
    {
        this.revision = revision;
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
        ApiForMailmagazine am = new ApiForMailmagazine();
        if ( dmd.getData( hotelId, historyId ) )
        {
            try
            {
                jsonOut = am.execute( hotelId, "GET", function + "/" + functionSub + "/" + dmd.getMailId(), "", dmd.getApiKey() );
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
        if ( response_code == NomalResponse )
        {
            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                JsonNode json = objectMapper.readTree( jsonOut );
                setTitle( json.get( "title" ).textValue() );
                setSubject( json.get( "subject" ).textValue() );
                setPlainContents( json.get( "plain_contents" ).textValue() );
                setSenderId( json.get( "sender_id" ).textValue() );
                setRecipientId( json.get( "recipient_id" ).textValue() );
                setUnsubscribeUrl( json.get( "unsubscribe_url" ) == null ? "https://www.hotenavi.com/" + hotelId + "/mailmagazine/cancel/" : json.get( "unsubscribe_url" ).textValue() );
                if ( json.get( "schedule" ) != null )
                {
                    if ( json.get( "schedule" ).get( "send_at" ) != null )
                    {
                        setSendAt( json.get( "schedule" ).get( "send_at" ).textValue() );
                    }
                }
                setStatus( json.get( "status" ).intValue() );
                setRevision( json.get( "revision" ).intValue() );
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
