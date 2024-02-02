package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagHotel;

public class MagazineSenderGet
{
    private final String className     = "MagazineSenderGet";
    private final String function      = "magazine";
    private final String functionSub   = "senders";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;
    private final int    NoContent     = 204;

    private String       nickname;
    private String       from_email;
    private String       from_name;
    private String       reply_to_email;
    private String       reply_to_name;
    private String       address;
    private String       city;
    private String       zip_code;
    private int          revision;

    public MagazineSenderGet()
    {
        this.nickname = "";
        this.from_email = "";
        this.reply_to_email = "";
        this.address = "";
        this.city = "";
        this.zip_code = "";
        this.revision = 0;
        this.response_code = NomalResponse;
        this.error_message = "";
    }

    public String getNickname()
    {
        return this.nickname;
    }

    public String getFromEmail()
    {
        return this.from_email;
    }

    public String getFromName()
    {
        return this.from_name;
    }

    public String getReplyToEmail()
    {
        return this.reply_to_email;
    }

    public String getReplyToName()
    {
        return this.reply_to_name;
    }

    public String getAddress()
    {
        return this.address;
    }

    public String getCity()
    {
        return this.city;
    }

    public String getZipCode()
    {
        return this.zip_code;
    }

    public int getRevision()
    {
        return this.revision;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public void setFromEmail(String fromEmail)
    {
        this.from_email = fromEmail;
    }

    public void setFromName(String fromName)
    {
        this.from_name = fromName;
    }

    public void setReplyToEmail(String replyToEmail)
    {
        this.reply_to_email = replyToEmail;
    }

    public void setReplyToName(String replyToName)
    {
        this.reply_to_name = replyToName;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setZipCode(String zipCode)
    {
        this.zip_code = zipCode;
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

    public void execute(String hotelId)
    {
        LogLib log = new LogLib();
        DataMagHotel dmh = new DataMagHotel();
        String jsonOut = "";
        ApiForMailmagazine am = new ApiForMailmagazine();
        if ( dmh.getData( hotelId ) )
        {
            if ( dmh.getSenderId().equals( "" ) )
            {
                setResponseCode( NoContent );
            }
            else
            {
                try
                {
                    jsonOut = am.execute( hotelId, "GET", function + "/" + functionSub + "/" + dmh.getSenderId(), "", dmh.getApiKey() );
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
        }
        else
        {
            setResponseCode( dmh.getResponseCode() );
        }
        if ( response_code == NomalResponse )
        {
            ObjectMapper objectMapper = new ObjectMapper();
            try
            {
                JsonNode json = objectMapper.readTree( jsonOut );
                setNickname( json.get( "nickname" ) == null ? "" : json.get( "nickname" ).textValue() );
                setFromEmail( json.get( "from" ).get( "email" ).textValue() );
                setFromName( json.get( "from" ).get( "name" ).textValue() );
                setReplyToEmail( json.get( "reply_to" ).get( "email" ).textValue() );
                setReplyToName( json.get( "reply_to" ).get( "name" ).textValue() );
                setAddress( json.get( "address" ).textValue() );
                setCity( json.get( "city" ).textValue() );
                setZipCode( json.get( "zip" ).textValue() );
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
