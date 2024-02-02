package com.hotenavi2.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.hotenavi2.common.DBConnection;
import com.hotenavi2.common.DateEdit;
import com.hotenavi2.common.LogLib;
import com.hotenavi2.common.MailAddressEncrypt;
import com.hotenavi2.common.MailSystemConf;

public class DataMagData implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -1;
    private String            title;
    private String            subject;
    private String            plain_contents;
    private int               state;                /* 0:送信予約、1:送信済、2:取消 */
    private int               send_flag;            /* 1:予定配信、2:すぐに配信 */
    private String            sender_id;
    private String            send_at;
    private String            recipient_id;
    private String            mail_id;
    private String            api_key;
    private String            unsubscribe_url;
    private String[]          emails;
    private String            error_message;
    private int               response_code;

    public DataMagData()
    {
        this.title = "";
        this.subject = "";
        this.plain_contents = "";
        this.state = 0;
        this.send_flag = 0;
        this.sender_id = "";
        this.send_at = "";
        this.recipient_id = "";
        this.mail_id = "";
        this.api_key = "";
        this.unsubscribe_url = "";
        this.emails = null;
        this.error_message = "";
        this.response_code = 0;
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

    public int getState()
    {
        return this.state;
    }

    public int getSendFlag()
    {
        return this.send_flag;
    }

    public String getSenderId()
    {
        return this.sender_id;
    }

    public String getSendAt()
    {
        return this.send_at;
    }

    public String getRecipientId()
    {
        return this.recipient_id;
    }

    public String getMailId()
    {
        return this.mail_id;
    }

    public String getUnsubscribeUrl()
    {
        return this.unsubscribe_url;
    }

    public String getApiKey()
    {
        return this.api_key;
    }

    public String getEmails()[]
    {
        return this.emails;
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

    /**
     * mag_data データ取得
     * 
     * @param hotelId
     * @return boolean
     */
    public boolean getData(String hotelId, int historyId)
    {
        LogLib log = new LogLib();
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT";
        query += " CONCAT(mag_data.hotel_id,'[', mag_data.history_id, ']') AS title";
        query += ",mag_data.subject";
        query += ",mag_data.body";
        query += ",mag_data.state";
        query += ",mag_data.send_flag";
        query += ",mag_data.send_date";
        query += ",mag_data.send_time";
        query += ",mag_hotel.sender_id";
        query += ",mag_data.recipient_id";
        query += ",mag_data.mail_id";
        query += ",mag_hotel.api_key";
        query += ",mag_hotel.unsubscribe_url";
        query += " FROM hotenavi.mag_data ";
        query += " INNER JOIN hotenavi.mag_hotel ON mag_hotel.hotel_id = mag_data.hotel_id ";
        query += " WHERE mag_data.hotel_id = ? AND mag_data.history_id = ?";
        try
        {
            setErrorMessage( "[DataMagData.getData] no data" );
            setResponseCode( 404 );
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, historyId );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                this.title = result.getString( "title" );
                this.subject = result.getString( "subject" );
                this.plain_contents = result.getString( "body" );
                this.state = result.getInt( "state" );
                this.send_flag = result.getInt( "send_flag" );
                this.sender_id = result.getString( "sender_id" );
                NumberFormat nf = new DecimalFormat( "00" );
                int send_date = result.getInt( "send_date" );
                int send_time = result.getInt( "send_time" ) * 100;

                /* state:0 （送信予約）で send_flag:2（すぐに配信）の場合は、現在時刻の10分後にする。 */
                if ( this.state == 0 && this.send_flag == 2 )
                {
                    int send_datetime[] = DateEdit.addSec( send_date, send_time, 600 );
                    send_date = send_datetime[0];
                    send_time = send_datetime[1];
                }

                this.send_at = send_date / 10000 + "-" + nf.format( send_date / 100 % 100 ) + "-" + nf.format( send_date % 100 ) + "T"
                        + nf.format( send_time / 10000 ) + ":" + nf.format( send_time / 100 % 100 ) + ":" + nf.format( send_time % 100 ) + "+09:00";

                this.recipient_id = result.getString( "recipient_id" );
                this.mail_id = result.getString( "mail_id" );
                this.api_key = MailSystemConf.decrypt( result.getString( "api_key" ) );
                this.unsubscribe_url = result.getString( "unsubscribe_url" );
                setResponseCode( 200 );
                setErrorMessage( "" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            log.error( "[DataMagData.getData] Exception=" + e.toString() );
            setErrorMessage( "[DataMagData.getData] Exception=" + e.toString() );
            setResponseCode( 500 );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * mag_data_list データ取得
     * 
     * @param hotelId
     * @return boolean
     */
    public boolean getMagDataList(String hotelId, int historyId)
    {
        LogLib log = new LogLib();
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT";
        query += " address";
        query += " FROM hotenavi.mag_data_list ";
        query += " WHERE hotel_id = ? AND history_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, historyId );
            result = prestate.executeQuery();
            if ( result.last() )
            {
                this.emails = new String[result.getRow()];
            }
            result.beforeFirst();
            int i = -1;
            while( result.next() != false )
            {
                i++;
                this.emails[i] = MailAddressEncrypt.decrypt( result.getString( "address" ) );
            }
            if ( i > -1 )
            {
                setResponseCode( 200 );
                setErrorMessage( "" );
                ret = true;
            }
            else
            {
                setResponseCode( 202 );
                setErrorMessage( "[DataMagData.getMagDataList] no data" );
            }
        }
        catch ( Exception e )
        {
            log.error( "[DataMagData.getMagDataList] Excption=" + e.toString() );
            setErrorMessage( "[DataMagData.getMagDataList] Exception=" + e.toString() );
            setResponseCode( 500 );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * mag_hotel データ更新
     * 
     * @param hotelId
     * @param historyId
     * @return boolean
     */
    public boolean updateData(String hotelId, int historyId, String item, String value)
    {
        LogLib log = new LogLib();
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "UPDATE mag_data SET ";
        query += item + " = ? ";
        query += " WHERE hotel_id = ? AND history_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, value );
            prestate.setString( 2, hotelId );
            prestate.setInt( 3, historyId );

            if ( prestate.executeUpdate() > 0 )
            {
                setResponseCode( 200 );
                setErrorMessage( "" );
                ret = true;
            }
            else
            {
                setResponseCode( 202 );
                setErrorMessage( "[DataMagData.updateData] accepted" );
            }
        }
        catch ( Exception e )
        {
            log.error( "[DataMagData.updateData] Exception=" + e.toString() );
            setErrorMessage( "[DataMagData.updateData] Exception=" + e.toString() );
            setResponseCode( 500 );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * mag_hotel データ更新
     * 
     * @param hotelId
     * @return boolean
     */
    public boolean sentData(String hotelId, int historyId)
    {
        LogLib log = new LogLib();
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE mag_data SET ";
        query += " state  = 1 ";
        query += " WHERE hotel_id = ? AND history_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, hotelId );
            prestate.setInt( 2, historyId );

            if ( prestate.executeUpdate() > 0 )
            {
                setResponseCode( 200 );
                setErrorMessage( "" );
                ret = true;
            }
            else
            {
                setResponseCode( 202 );
                setErrorMessage( "[DataMagData.sentData mag_data] accepted" );
            }
        }
        catch ( Exception e )
        {
            log.error( "[DataMagData.sentData mag_data] Exception=" + e.toString() );
            setErrorMessage( "[DataMagData.sentData  mag_data] Exception=" + e.toString() );
            setResponseCode( 500 );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        if ( ret )
        {
            query = "UPDATE mag_data_list SET ";
            query += " send_flag  = 1 ";
            query += " WHERE hotel_id = ? AND history_id = ?";

            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );

                // 更新対象の値をセットする
                prestate.setString( 1, hotelId );
                prestate.setInt( 2, historyId );

                if ( prestate.executeUpdate() > 0 )
                {
                    setResponseCode( 200 );
                    setErrorMessage( "" );
                    ret = true;
                }
                else
                {
                    setResponseCode( 202 );
                    setErrorMessage( "[DataMagData.sentData mag_data_list] accepted" );
                    ret = false;
                }
            }
            catch ( Exception e )
            {
                log.error( "[DataMagData.sentData  mag_data_list] Exception=" + e.toString() );
                setErrorMessage( "[DataMagData.sentData  mag_data_list] Exception=" + e.toString() );
                setResponseCode( 500 );
                ret = false;
            }
            finally
            {
                DBConnection.releaseResources( prestate );
                DBConnection.releaseResources( connection );
            }
        }
        return(ret);
    }

}
