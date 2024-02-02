package com.hotenavi2.mailmagazine;

import com.hotenavi2.data.DataMagData;

public class LogicMagazineSend
{
    private final String className        = "LogicMagazineSend";
    private String       error_message;
    private int          response_code;
    private final int    NomalResponse    = 200;
    private final int    CreatedResponse  = 201;                // çÏê¨ê¨å˜
    private final int    AcceptedResponse = 202;                // ñ¢èàóù
    private final int    DeleteResponse   = 204;                // çÌèúê¨å˜

    public LogicMagazineSend()
    {
        this.error_message = "";
        this.response_code = 0;
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

    public boolean execute(String hotelId, int historyId, boolean isReExtraction)
    {
        DataMagData dmd = new DataMagData();
        if ( dmd.getData( hotelId, historyId ) )
        {
            if ( dmd.getRecipientId().equals( "" ) )
            {
                /*
                 * recipient_id Ç™ìoò^Ç≥ÇÍÇƒÇ¢Ç»ÇØÇÍÇŒêVãKÇ…ëóêMêÊÇçÏê¨
                 */
                MagazineRecipientAdd magazineRecipientAdd = new MagazineRecipientAdd();
                magazineRecipientAdd.execute( hotelId, historyId );
                if ( magazineRecipientAdd.getResponseCode() != CreatedResponse && magazineRecipientAdd.getResponseCode() != AcceptedResponse )
                {
                    setResponseCode( magazineRecipientAdd.getResponseCode() );
                    setErrorMessage( className + " magazine recipient add error" );
                    return false;
                }
            }
            else if ( isReExtraction ) // à∂êÊÇÃçƒíäèo
            {
                MagazineRecipientGet magazineRecipientGet = new MagazineRecipientGet();
                magazineRecipientGet.execute( hotelId, historyId );
                if ( magazineRecipientGet.getResponseCode() != NomalResponse )
                {
                    MagazineRecipientPut magazineRecipientPut = new MagazineRecipientPut();
                    magazineRecipientPut.setRevision( magazineRecipientGet.getRevision() );
                    magazineRecipientPut.execute( hotelId, historyId );
                    if ( magazineRecipientPut.getResponseCode() != NomalResponse )
                    {
                        setResponseCode( magazineRecipientPut.getResponseCode() );
                        setErrorMessage( className + " magazine recipient put error" );
                        return false;
                    }
                }
            }
            if ( dmd.getMailId().equals( "" ) )
            {
                /*
                 * mail_id Ç™ìoò^Ç≥ÇÍÇƒÇ¢Ç»ÇØÇÍÇŒêVãKÇ…ÉÅÉãÉ}ÉKÇçÏê¨
                 */
                MagazineMailAdd magazineMailAdd = new MagazineMailAdd();
                magazineMailAdd.execute( hotelId, historyId );
                if ( magazineMailAdd.getResponseCode() != CreatedResponse && magazineMailAdd.getResponseCode() != AcceptedResponse )
                {
                    setResponseCode( magazineMailAdd.getResponseCode() );
                    setErrorMessage( className + " magazine mail add error" );
                    return false;
                }
                if ( dmd.getSendFlag() == 2 )
                {
                    /* Ç∑ÇÆÇ…ëóêMÇ∑ÇÈ */
                    MagazineMailScheduleSend magazineMailScheduleSend = new MagazineMailScheduleSend();
                    magazineMailScheduleSend.execute( hotelId, historyId );
                    if ( magazineMailScheduleSend.getResponseCode() != AcceptedResponse )
                    {
                        setResponseCode( magazineMailScheduleSend.getResponseCode() );
                        setErrorMessage( className + " magazine mail schedule send error" );
                        return false;
                    }
                }
            }
            else
            {
                MagazineMailGet magazineMailGet = new MagazineMailGet();
                magazineMailGet.execute( hotelId, historyId );
                if ( !magazineMailGet.getSubject().equals( dmd.getSubject() ) || !magazineMailGet.getPlainContents().equals( dmd.getPlainContents() ) )
                {
                    MagazineMailDelete magazineMailDelete = new MagazineMailDelete();
                    magazineMailDelete.execute( hotelId, historyId );
                    if ( magazineMailDelete.getResponseCode() != DeleteResponse )
                    {
                        setResponseCode( magazineMailDelete.getResponseCode() );
                        setErrorMessage( className + " magazine mail delete  error" );
                        return false;
                    }
                    MagazineMailAdd magazineMailAdd = new MagazineMailAdd();
                    magazineMailAdd.execute( hotelId, historyId );
                    if ( magazineMailAdd.getResponseCode() != CreatedResponse && magazineMailAdd.getResponseCode() != AcceptedResponse )
                    {
                        setResponseCode( magazineMailAdd.getResponseCode() );
                        setErrorMessage( className + " magazine mail add error" );
                        return false;
                    }
                    /*
                     * MagazineMailPatch magazineMailPatch = new MagazineMailPatch();
                     * magazineMailPatch.setRevision( magazineMailGet.getRevision() );
                     * magazineMailPatch.execute( hotelId, historyId );
                     * if ( magazineMailPatch.getResponseCode() != NomalResponse )
                     * {
                     * setResponseCode( magazineMailPatch.getResponseCode() );
                     * setErrorMessage( className + " magazine mail patch error" );
                     * return false;
                     * }
                     */
                }
                if ( dmd.getState() == 0 && dmd.getSendFlag() == 2 && magazineMailGet.getStatus() != 3 )
                {
                    /* Ç∑ÇÆÇ…ëóêMÇ∑ÇÈ */
                    MagazineMailScheduleSend magazineMailScheduleSend = new MagazineMailScheduleSend();
                    magazineMailScheduleSend.execute( hotelId, historyId );
                    if ( magazineMailScheduleSend.getResponseCode() != AcceptedResponse )
                    {
                        setResponseCode( magazineMailScheduleSend.getResponseCode() );
                        setErrorMessage( className + " magazine mail schedule send error" );
                        return false;
                    }
                }
                else if ( !magazineMailGet.getSendAt().equals( dmd.getSendAt() ) )
                {
                    MagazineMailSchedulePut magazineMailSchedulePut = new MagazineMailSchedulePut();
                    magazineMailSchedulePut.setRevision( magazineMailGet.getRevision() );
                    magazineMailSchedulePut.execute( hotelId, historyId );
                    if ( magazineMailSchedulePut.getResponseCode() != NomalResponse )
                    {
                        setResponseCode( magazineMailSchedulePut.getResponseCode() );
                        setErrorMessage( className + " magazine mail schedule put  error" );
                        return false;
                    }
                }
            }
        }
        else
        {
            setResponseCode( dmd.getResponseCode() );
            setErrorMessage( dmd.getErrorMessage() );
        }
        return true;
    }

    public static boolean check(String hotelId, int historyId)
    {
        MagazineMailGet magazineMailGet = new MagazineMailGet();
        magazineMailGet.execute( hotelId, historyId );
        if ( magazineMailGet.getStatus() == 3 )// ëóêMçœ
        {
            DataMagData dataMagData = new DataMagData();
            return dataMagData.sentData( hotelId, historyId ); // ëóêMçœÇ…çXêVÇ≥ÇÍÇÈÇ∆trueÇ™ï‘ÇÈ
        }
        else
        {
            return false;
        }
    }

    public boolean delete(String hotelId, int historyId)
    {
        DataMagData dmd = new DataMagData();
        if ( dmd.getData( hotelId, historyId ) )
        {
            if ( !dmd.getMailId().equals( "" ) )
            {
                MagazineMailScheduleDelete magazineMailScheduleDelete = new MagazineMailScheduleDelete();
                magazineMailScheduleDelete.execute( hotelId, historyId );
                if ( magazineMailScheduleDelete.getResponseCode() != DeleteResponse )
                {
                    setResponseCode( magazineMailScheduleDelete.getResponseCode() );
                    setErrorMessage( className + " magazine mail schedule delete error:" + magazineMailScheduleDelete.getErrorMessage() );
                    return false;
                }
                MagazineMailDelete magazineMailDelete = new MagazineMailDelete();
                magazineMailDelete.execute( hotelId, historyId );
                if ( magazineMailDelete.getResponseCode() != DeleteResponse )
                {
                    setResponseCode( magazineMailDelete.getResponseCode() );
                    setErrorMessage( className + " magazine mail delete error:" + magazineMailDelete.getErrorMessage() );
                    return false;
                }
            }
            if ( !dmd.getRecipientId().equals( "" ) )
            {
                MagazineRecipientDelete magazineRecipientDelete = new MagazineRecipientDelete();
                magazineRecipientDelete.execute( hotelId, historyId );
                if ( magazineRecipientDelete.getResponseCode() != DeleteResponse )
                {
                    setResponseCode( magazineRecipientDelete.getResponseCode() );
                    setErrorMessage( className + " magazine recipient delete error:" + magazineRecipientDelete.getErrorMessage() );
                    return false;
                }
            }
        }
        return true;
    }
}
