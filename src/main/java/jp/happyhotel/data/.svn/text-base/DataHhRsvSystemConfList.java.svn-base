package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 取得クラス
 * 
 * @author K.Mitsuhashi
 * @version 1.00 2017/06/06
 */
public class DataHhRsvSystemConfList implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -2478206465407108838L;

    /**
     * データ取得
     * 
     * @return ArrayList<DataHhRsvSystemCont> マスタのリスト
     */
    public ArrayList<DataHhRsvSystemConf> getList(int ctrl_id1)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataHhRsvSystemConf dmp;
        ArrayList<DataHhRsvSystemConf> array = null;

        query = "SELECT * FROM hh_rsv_system_conf WHERE ctrl_id1 = ?  ";
        if ( ctrl_id1 == 7 )
        {
            query += " AND ( ctrl_id2 < 6 OR ctrl_id2 > 9 ) ";
        }
        query += " ORDER BY ctrl_id2";

        try
        {
            array = new ArrayList<DataHhRsvSystemConf>();
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, ctrl_id1 );
            result = prestate.executeQuery();

            if ( result != null )
            {
                count = 0;
                result.beforeFirst();
                while( result.next() != false )
                {
                    dmp = new DataHhRsvSystemConf();
                    dmp.setData( result );
                    array.add( dmp );
                    dmp = null;
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getList()] Exception:" + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(array);
    }

    /**
     * hh_rsv_system_confテーブルの情報をMapに変換して取得
     * 
     * @param iCtrlId ctrl_id1の値
     * @return HashMap(val1, val2) ctrl_id1に紐づくデータ
     */
    public HashMap<String, String> getSystemConfMap(int iCtrlId)
    {
        HashMap<String, String> rtMap = new HashMap<String, String>();
        try
        {
            ArrayList<DataHhRsvSystemConf> list = getList( iCtrlId );
            for( int i = 0 ; i < list.size() ; i++ )
            {
                rtMap.put( list.get( i ).getVal1(), list.get( i ).getVal2() );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvSystemConfList.getSystemConfMap()] Exception=" + e.toString() + ", Ctrl_id1:" + iCtrlId );
        }
        return rtMap;
    }
}
