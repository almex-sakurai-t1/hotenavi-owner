package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 市区町村取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMasterCityList implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -2478206465407108838L;

    /**
     * 市区町村一覧データ取得
     * 
     * @param pref 都道府県ID
     * @return ArrayList<DataMasterCity> 市区町村マスタのリスト
     */
    public ArrayList<DataMasterCity> getCityList(int Pref)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataMasterCity dmc;
        ArrayList<DataMasterCity> array = null;

        query = "SELECT * FROM hh_master_city WHERE pref_id = ?";
        query += " ORDER BY jis_code";

        try
        {
            array = new ArrayList<DataMasterCity>();
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Pref );
            result = prestate.executeQuery();
            if ( result != null )
            {
                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    dmc = new DataMasterCity();
                    dmc.setData( result );
                    array.add( dmc );
                    dmc = null;
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getCityList()] Exception:" + e.toString() );

        }
        return(array);
    }
}
