package jp.happyhotel.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataSameUUIDData;
import jp.happyhotel.user.UserPointPay;

public class SearchSameUUIDData
{

    private String             user_id;

    private DataSameUUIDData[] sameUUIDDatas;

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public DataSameUUIDData[] getSameUUIDDatas()
    {
        return sameUUIDDatas;
    }

    /**
     * 検索ユーザと同じuuidのユーザ情報をDataSameUUIDData配列に格納
     * 
     * @return
     * @throws Exception
     */
    public boolean setSameUUIDDatas() throws Exception
    {

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        StringBuilder querySb = new StringBuilder();

        ArrayList<DataSameUUIDData> uuidDataList = null;
        DataSameUUIDData uuidData = null;

        UserPointPay upp = null;

        boolean ret = true;

        String currentUserID = null;

        try
        {

            querySb = new StringBuilder();

            querySb.append( "SELECT" );
            querySb.append( "  DISTINCT(a.user_id) user_id" );
            querySb.append( " FROM" );
            querySb.append( "  ap_uuid_user_history a" );
            querySb.append( " INNER JOIN" );
            querySb.append( "  (" );
            querySb.append( "   SELECT" );
            querySb.append( "    uuid" );
            querySb.append( "   FROM" );
            querySb.append( "    ap_uuid_user" );
            querySb.append( "   WHERE" );
            querySb.append( "    user_id = ?" );
            querySb.append( "  ) b" );
            querySb.append( " ON a.uuid = b.uuid" );
            querySb.append( " ORDER BY user_id" );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, user_id );
            result = prestate.executeQuery();

            uuidDataList = new ArrayList<DataSameUUIDData>();

            upp = new UserPointPay();

            while( result.next() )
            {

                currentUserID = result.getString( "user_id" );
                int pointSum = upp.getNowPoint( currentUserID, false );

                if (pointSum > 0)
                {
                    uuidData = new DataSameUUIDData();

                    uuidData.setUser_id( currentUserID );
                    uuidData.setPoint_sum( upp.getNowPoint( currentUserID, false ) );
                    uuidDataList.add( uuidData );
                }

            }

            sameUUIDDatas = uuidDataList.toArray( new DataSameUUIDData[uuidDataList.size()] );

        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "SearchUUIDData.setSameUUIDDatas Exceptionが発生", e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;

    }

}
