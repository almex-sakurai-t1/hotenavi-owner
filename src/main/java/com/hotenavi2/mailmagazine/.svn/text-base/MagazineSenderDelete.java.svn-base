package com.hotenavi2.mailmagazine;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import com.hotenavi2.common.LogLib;
import com.hotenavi2.data.DataMagHotel;

public class MagazineSenderDelete
{
    private final String className     = "MagazineSenderDelete";
    private final String function      = "magazine";
    private final String functionSub   = "senders";
    private int          response_code;
    private String       error_message;
    private final int    NomalResponse = 204;

    public MagazineSenderDelete()
    {
        try
        {
            String p[] = SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
            System.out.println( p );
        }
        catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
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

    public void execute(String hotelId)
    {
        LogLib log = new LogLib();
        ApiForMailmagazine am = new ApiForMailmagazine();
        try
        {
            DataMagHotel dmh = new DataMagHotel();
            if ( dmh.getData( hotelId ) )
            {
                am.execute( hotelId, "DELETE", function + "/" + functionSub + "/" + dmh.getSenderId(), "", dmh.getApiKey() );
                setResponseCode( am.getResponseCode() );
                setErrorMessage( am.getErrorMessage() );
                if ( am.getResponseCode() == NomalResponse )
                {
                    dmh.updateData( hotelId, "sender_id", "" );
                }
            }
            else
            {
                setResponseCode( dmh.getResponseCode() );
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
