package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagData;

public class MagazineRecipientDelete
{
    private final String className     = "MagazineRecipientDelete";
    private final String function      = "magazine";
    private final String functionSub   = "recipients";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 204;

    public MagazineRecipientDelete()
    {
        this.response_code = NomalResponse;
        this.error_message = "";
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
                am.execute( hotelId, "DELETE", function + "/" + functionSub + "/" + dmd.getRecipientId(), "", dmd.getApiKey() );
                setResponseCode( am.getResponseCode() );
                setErrorMessage( am.getErrorMessage() );
                if ( am.getResponseCode() == NomalResponse )
                {
                    dmd.updateData( hotelId, historyId, "recipient_id", "" );
                }
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
}
