package com.hotenavi2.mailmagazine;

import java.io.IOException;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagData;

public class MagazineMailDelete
{
    private final String className     = "MagazineMailDelete";
    private final String function      = "magazine";
    private final String functionSub   = "mails";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 204;

    public MagazineMailDelete()
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
        ApiForMailmagazine am = new ApiForMailmagazine();
        try
        {
            DataMagData dmd = new DataMagData();

            if ( dmd.getData( hotelId, historyId ) )
            {
                System.out.println( className + " projectId=" + "hotenavi_" + hotelId );
                am.execute( hotelId, "DELETE", function + "/" + functionSub + "/" + dmd.getMailId(), "", dmd.getApiKey() );
                setResponseCode( am.getResponseCode() );
                setErrorMessage( am.getErrorMessage() );
                if ( am.getResponseCode() == NomalResponse )
                {
                    dmd.updateData( hotelId, historyId, "mail_id", "" );
                }
            }
            else
            {
                setResponseCode( dmd.getResponseCode() );
            }
        }
        catch ( IOException e )
        {
            log.error( className + " Error=" + e.toString() );
            setResponseCode( am.getResponseCode() );
            setErrorMessage( className + " Error=" + e.toString() );
        }
    }
}
