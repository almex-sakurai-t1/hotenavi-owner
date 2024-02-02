package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテル情報未反映デ有無取得クラス
 * 
 * @author T.Sakurai
 */
public class CheckHotelReflect implements Serializable
{
    /**
     * ホテル情報未反映デ有無取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:対象データあり,FALSE:対象データなし)
     */
    public static boolean check(int hotelId)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT 1 FROM hh_hotel_basic basic";
        query += " INNER JOIN (SELECT id,MAX(seq) maxSeq FROM hh_hotel_adjustment_history WHERE edit_id BETWEEN 1 AND 199 AND edit_id != 45 AND input_date>=20200217 GROUP BY id) historyMax ON basic.id=historyMax.id";
        query += " INNER JOIN (SELECT id,seq,input_date,input_time,memo FROM hh_hotel_adjustment_history WHERE edit_id BETWEEN 1 AND 199 AND edit_id != 45 AND input_date>=20200217) history  ON history.id=historyMax.id AND history.seq = historyMax.maxSeq ";
        query += " WHERE basic.last_update*1000000+basic.last_uptime < history.input_date*1000000+history.input_time AND basic.id = ?";
        query += " UNION SELECT 1 FROM hh_hotel_basic basic";
        query += " INNER JOIN (SELECT id,MAX(seq) maxSeq FROM research_hotel_history WHERE input_date>=20200217 AND data_level = 4 GROUP BY id) historyMax ON basic.id=historyMax.id";
        query += " INNER JOIN (SELECT id,seq,input_date,input_time,memo FROM research_hotel_history WHERE input_date>=20200217 AND data_level = 4) history ON history.id=historyMax.id AND history.seq = historyMax.maxSeq";
        query += " WHERE basic.last_update*1000000+basic.last_uptime < history.input_date*1000000+history.input_time  AND basic.id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = true; /* 反映しなければならないデータがある */
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CheckHotelReflect.check] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
