package jp.happyhotel.touch;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApHotelTerminal;

/**
 * 端末マスタクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class HotelTerminal implements Serializable
{

    /**
     *
     */
    private static final long   serialVersionUID = -6199069863331936331L;
    // 端末情報データ
    private DataApHotelTerminal terminal         = new DataApHotelTerminal();

    //
    public DataApHotelTerminal getTerminal()
    {
        return terminal;
    }

    public void setTerminal(DataApHotelTerminal terminal)
    {
        this.terminal = terminal;
    }

    /***
     * シリアル番号からホテル端末データを取得
     * 
     * @param serialNo
     * @return
     */
    public boolean getHotelTerminal(String serialNo)
    {
        boolean ret = false;

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_hotel_terminal WHERE serial_no = ?";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, serialNo );
            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.next() != false )
                {
                    this.terminal.setData( result );

                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelTerminal.getHotelTerminal()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /***
     * ホテルIDと管理番号からホテル端末データを取得
     * 
     * @param id
     * @param seq
     * @return
     */
    public boolean getHotelTerminal(int id, int seq)
    {
        boolean ret = false;

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_hotel_terminal WHERE id = ? AND room_no = ?";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {

                if ( result.next() != false )
                {
                    this.terminal.setData( result );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelTerminal.getHotelTerminal()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

}
