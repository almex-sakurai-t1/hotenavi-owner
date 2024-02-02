/*
 * @(#)HotenaviRoomPrice.java 1.00 2011/05/19 Copyright (C) ALMEX Inc. 2011 ホテナビ部屋料金
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * ホテル詳細クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2012/11/05
 */
public class HotenaviRoomPrice implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 6792007065691281320L;

    public HotenaviRoomPrice()
    {

    }

    /**
     * ランク別料金があるかどうかのチェック
     */
    public boolean checkRoomPrice(String hotenavi_id)
    {
        boolean ret = false;

        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) );
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        try
        {
            connection = DBConnection.getConnection();
            String query = "";
            query = "SELECT roomrank.system FROM roomrank";
            query = query + " INNER JOIN hotel ON roomrank.hotelid = hotel.hotel_id";
            query = query + " AND hotel.plan IN (1,3,4)";
            query = query + " WHERE roomrank.hotelid='" + hotenavi_id + "'";
            query = query + " AND roomrank.disp_flg=1";
            query = query + " AND roomrank.start_date<=" + now_date;
            query = query + " AND roomrank.end_date>=" + now_date;
            query = query + " ORDER BY roomrank.id DESC";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                if ( result.getString( "roomrank.system" ).length() > 1 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[RoomPriceCheck] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }
}
