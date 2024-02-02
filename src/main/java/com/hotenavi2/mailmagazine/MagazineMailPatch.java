package com.hotenavi2.mailmagazine;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagData;

public class MagazineMailPatch
{
    private final String className     = "MagazineMailPatch";
    private final String function      = "magazine";
    private final String functionSub   = "mails";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 200;

    private String       mail_id;
    private int          revision;

    public MagazineMailPatch()
    {
        this.mail_id = "";
        this.revision = 0;
        this.response_code = NomalResponse;
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
        if ( dmd.getData( hotelId, historyId ) )
        {
            ApiForMailmagazine am = new ApiForMailmagazine();
            try
            {
                am.execute( hotelId, "PATCH", function + "/" + functionSub, setJson( dmd ), dmd.getApiKey() );
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

    public String setJson(DataMagData dmd)
    {
        JSONObject jsonAll = new JSONObject();
        jsonAll.put( "title", dmd.getTitle() );
        jsonAll.put( "subject", dmd.getSubject() );
        jsonAll.put( "plain_contents", dmd.getPlainContents() );
        jsonAll.put( "sender_id", dmd.getSenderId() );
        jsonAll.put( "recipient_id", dmd.getRecipientId() );
        jsonAll.put( "unsubscribe_url", dmd.getUnsubscribeUrl() );
        jsonAll.put( "revision", this.revision );
        return jsonAll.toString();
    }
}
