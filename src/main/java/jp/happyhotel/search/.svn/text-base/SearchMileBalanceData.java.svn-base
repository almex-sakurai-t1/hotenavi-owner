package jp.happyhotel.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMileBalanceData;

public class SearchMileBalanceData
{

    private String                user_id;

    private DataMileBalanceData[] mileBalanceDatas;

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public DataMileBalanceData[] getMileBalanceDatas()
    {
        return mileBalanceDatas;
    }

    /**
     * 検索ユーザのマイル残高情報をDBから抽出しDataMileBalanceData配列に格納
     * 
     * @return
     * @throws Exception
     */
    public boolean ｓetMileBalanceDatas() throws Exception
    {

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        StringBuilder querySb = new StringBuilder();

        ArrayList<DataMileBalanceData> mileBalanceDataList = null;
        DataMileBalanceData mileBalanceData = null;

        boolean ret = true;

        try
        {

            querySb = new StringBuilder();
            querySb.append( "SELECT" );
            querySb.append( " *" );
            querySb.append( " FROM hh_user_point_pay" );
            querySb.append( " WHERE user_id = ?" );
            querySb.append( " ORDER by seq" );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( querySb.toString() );
            prestate.setString( 1, user_id );
            result = prestate.executeQuery();

            mileBalanceDataList = new ArrayList<DataMileBalanceData>();

            while( result.next() )
            {
                mileBalanceData = new DataMileBalanceData();
                mileBalanceData.setUser_id( result.getString( "user_id" ) );
                mileBalanceData.setSeq( result.getInt( "seq" ) );
                mileBalanceData.setCode( result.getInt( "code" ) );
                mileBalanceData.setGet_date( result.getInt( "get_date" ) );
                mileBalanceData.setGet_time( result.getInt( "get_time" ) );
                mileBalanceData.setPoint( result.getInt( "point" ) );
                mileBalanceData.setPoint_kind( result.getInt( "point_kind" ) );
                mileBalanceData.setExt_code( result.getInt( "ext_code" ) );
                mileBalanceData.setExt_string( result.getString( "ext_string" ) );
                mileBalanceData.setUsed_point( result.getInt( "used_point" ) );
                mileBalanceData.setExpired_point( result.getInt( "expired_point" ) );
                mileBalanceDataList.add( mileBalanceData );
            }

            mileBalanceDatas = mileBalanceDataList.toArray( new DataMileBalanceData[mileBalanceDataList.size()] );

        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "SearchMileBalanceData.ｓetMileBalanceDatas Exceptionが発生", e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;

    }

}
