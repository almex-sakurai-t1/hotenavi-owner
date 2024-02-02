package jp.happyhotel.touch;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApErrorHistory;
import jp.happyhotel.data.DataApErrorMaster;

/**
 * ハピホテタッチエラーｊ￥
 * 
 * @author S.Tashiro
 * @version 1.00 2014/11/04
 */
public class TouchErrorHistory implements Serializable
{
    /**
     * 
     */
    private static final long  serialVersionUID = -1522078183049614703L;
    private DataApErrorHistory daeh;
    private DataApErrorMaster  daem;

    public TouchErrorHistory()
    {
        this.daeh = new DataApErrorHistory();
        this.daem = new DataApErrorMaster();
    }

    public DataApErrorHistory getErrorHistory()
    {
        return daeh;
    }

    public DataApErrorMaster getErrorMaster()
    {
        return daem;
    }

    public void setErrorHistory(DataApErrorHistory daeh)
    {
        this.daeh = daeh;
    }

    public void setErrorMaster(DataApErrorMaster daem)
    {
        this.daem = daem;
    }

    public boolean insertError(int errorCode, int errorSub, int id, int termId, String hotenaviId,
            int roomNo, int termNo, int seq, String userId, String hotelName, String roomName, int entryTime, String rsvNo)
    {
        boolean ret = false;
        if ( this.daeh == null )
        {
            this.daeh = new DataApErrorHistory();
        }
        this.daeh.setErrorCode( errorCode );
        this.daeh.setErrorSub( errorSub );
        this.daeh.setId( id );
        this.daeh.setTerminalId( termId );
        this.daeh.setHotenaviId( hotenaviId );
        this.daeh.setRoomNo( roomNo );
        this.daeh.setTerminalNo( termNo );
        this.daeh.setTouchSeq( seq );
        this.daeh.setUserId( userId );
        this.daeh.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        this.daeh.setRegistDate( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        this.daeh.setHotelName( hotelName );
        this.daeh.setRoomName( roomName );
        this.daeh.setEntryTime( entryTime );
        this.daeh.setReserveNo( rsvNo );
        this.daeh.insertData();
        return ret;
    }

    public boolean getErrorData(int errorSeq)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_error_history A WHERE error_seq = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, errorSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchErrorHistory.getErrorData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    public static boolean getErrorData(int id, int touch_seq)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_error_history A";
        query += " INNER JOIN ap_error_master B ON A.error_code=B.error_code AND A.error_sub=B.error_sub";
        query += " WHERE A.id = ? AND A.touch_seq = ? AND B.disp_flag=1";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, touch_seq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TouchErrorHistory.getErrorData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

}
