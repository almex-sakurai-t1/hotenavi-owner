package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class SearchRsvPlanOption
        implements Serializable
{
    private static final long serialVersionUID = 1407152047936140839L;
    private int               m_optionAllCount;

    public SearchRsvPlanOption()
    {
        this.m_optionAllCount = 0;
    }

    public int getAllCount()
    {
        return this.m_optionAllCount;
    }

    /**
     * 必須オプションID取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     */
    public int[] getSearchMustOptionIdList(int hotelId, int planId)
            throws Exception
    {
        int[] arrOptionIdList = (int[])null;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        String query = "";
        int count = 0;

        query = "SELECT hh_rsv_option.option_id";
        query = query + " FROM hh_rsv_option, hh_rsv_rel_plan_option";
        query = query + " WHERE hh_rsv_option.id = ?";
        query = query + " AND hh_rsv_option.option_flag = 1";
        query = query + " AND hh_rsv_option.id = hh_rsv_rel_plan_option.id";
        query = query + " AND hh_rsv_option.option_id = hh_rsv_rel_plan_option.option_id";
        query = query + " AND hh_rsv_rel_plan_option.plan_id = ?";
        query = query + " GROUP BY hh_rsv_option.option_id";
        query = query + " ORDER BY option_flag DESC, disp_index";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() )
                {
                    this.m_optionAllCount = result.getRow();
                }

                arrOptionIdList = new int[this.m_optionAllCount];

                result.beforeFirst();
                while( result.next() )
                {
                    arrOptionIdList[(count++)] = result.getInt( "option_id" );
                }
            }

            int[] arrayOfInt1 = arrOptionIdList;
            return arrayOfInt1;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchRsvPlanOption.getSearchOptionIdList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 通常オプションID取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     */
    public int[] getSearchCommOptionIdList(int hotelId, int planId)
            throws Exception
    {
        int[] arrOptionIdList = (int[])null;

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        String query = "";
        int count = 0;

        query = "SELECT hh_rsv_option.option_id";
        query = query + " FROM hh_rsv_option, hh_rsv_rel_plan_option";
        query = query + " WHERE hh_rsv_option.id = ?";
        query = query + " AND hh_rsv_option.option_flag = 0";
        query = query + " AND hh_rsv_option.id = hh_rsv_rel_plan_option.id";
        query = query + " AND hh_rsv_option.option_id = hh_rsv_rel_plan_option.option_id";
        query = query + " AND hh_rsv_rel_plan_option.plan_id = ?";
        query = query + " GROUP BY hh_rsv_option.option_id";
        query = query + " ORDER BY option_flag DESC, disp_index";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() )
                {
                    this.m_optionAllCount = result.getRow();
                }

                arrOptionIdList = new int[this.m_optionAllCount];

                result.beforeFirst();
                while( result.next() )
                {
                    arrOptionIdList[(count++)] = result.getInt( "option_id" );
                }
            }

            int[] arrayOfInt1 = arrOptionIdList;
            return arrayOfInt1;
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchRsvPlanOption.getSearchOptionIdList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }
}
