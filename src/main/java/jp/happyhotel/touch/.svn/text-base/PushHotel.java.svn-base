package jp.happyhotel.touch;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApPushHotel;

/**
 * Push通知情報
 * 
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class PushHotel implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -6199069863331936331L;

    // 端末情報データ
    DataApPushHotel           daph             = null;

    public PushHotel()
    {
        daph = new DataApPushHotel();
    }

    public DataApPushHotel getPushHotel()
    {
        return daph;
    }

    public void setPushHotel(DataApPushHotel pushHotel)
    {
        this.daph = pushHotel;
    }

    /**
     * PUSH通知情報一覧取得
     * 
     * @param userId
     * @return
     */
    public boolean getData(int id)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_push_hotel ";
        query += " WHERE type = 2";
        query += " AND del_flag = 0";
        query += " AND id IN( 0, ? )";
        query += " AND disp_from <= ?";
        query += " AND disp_to >= ?";
        query += " ORDER BY id DESC, seq DESC";
        query += " LIMIT 0,1";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
                result.beforeFirst();

                daph = new DataApPushHotel();
                while( result.next() != false )
                {
                    daph.setData( result );
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[PushHotel.getData()] Exception=" + e.toString() ,"PushHotel");
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

}
