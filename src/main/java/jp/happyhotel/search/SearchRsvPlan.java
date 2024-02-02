package jp.happyhotel.search;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;

/**
 * プラン検索クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2011/02/16
 */
public class SearchRsvPlan implements Serializable
{
    public final int          SEARCH_ADULT_MAX_NUM = 4;
    public final int          SEARCH_CHILD_MAX_NUM = 3;
    private static final long serialVersionUID     = -1878770032303514444L;
    private static final int  ADULT_ONE            = 1;
    private static final int  ADULT_TWO            = 2;
    private int               m_planAllCount;
    private int               m_hotelId;
    private int               m_startDate;
    private int               m_endDate;

    /**
     * データを初期化します。
     */
    public SearchRsvPlan()
    {
        this.m_planAllCount = 0;
        this.m_hotelId = 0;
        this.m_startDate = 0;
        this.m_endDate = 0;
    }

    /** プラン件数取得 **/
    public int getAllCount()
    {
        return(this.m_planAllCount);
    }

    /** ホテルID取得 **/
    public int getHotelId()
    {
        return(this.m_hotelId);
    }

    /** 開始日取得 **/
    public int getStartDate()
    {
        return this.m_startDate;
    }

    /** 終了日取得 **/
    public int getEndDate()
    {
        return this.m_endDate;
    }

    /***
     * プラン検索クラス
     * 
     * @param hotelId ホテルID
     * @param date 日付
     * @param adult 大人人数
     * @param child 子供人数
     * @param fromCharge 料金下限
     * @param toCharge 料金上限
     * @return planIdのリスト
     */
    public int[] getSearchPlanIdList(int hotelId, int date, int adult, int child, int fromCharge, int toCharge) throws Exception
    {

        int[] arrPlanIdList = null;
        int count;
        int startDate;
        int endDate;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ReserveCommon rsvcomm = new ReserveCommon();
        int minuscount = 0;
        int today = 0;

        query = "";
        count = 0;

        startDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), 1 );
        endDate = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), 2 );
        today = Integer.parseInt( DateEdit.getDate( 2 ) );

        if ( date > 0 && DateEdit.checkDate( date / 10000, date % 10000 / 100, date % 100 ) != false )
        {
            startDate = date;
            endDate = date;
        }
        else
        {
            // 日付指定がない場合や日付がカレンダーに存在しない場合は、翌日～１か月後を期間とする
            startDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), 1 );
            endDate = DateEdit.addMonth( Integer.parseInt( DateEdit.getDate( 2 ) ), 2 );
        }
        this.m_startDate = startDate;
        this.m_endDate = endDate;

        if ( adult < 0 || child < 0 || fromCharge < 0 || toCharge < 0 )
        {
            return null;
        }

        if ( startDate == endDate )
        {
            // 選択された日付の料金モードで計算、選択された部屋の空きがあるものプランを取得する
            query = " SELECT DISTINCT HRP.* FROM hh_rsv_plan HRP" +
                    "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRP.id = HRRB.id AND (HRRB.sales_flag = 1 OR HRRB.pre_open_flag=1)" +
                    "   INNER JOIN hh_rsv_plan_charge HRPC ON HRP.id = HRPC.id AND HRP.plan_id = HRPC.plan_id" +
                    "   INNER JOIN hh_rsv_day_charge HRDC ON HRP.id = HRDC.id AND HRDC.plan_id = HRP.plan_id AND HRDC.charge_mode_id= HRPC.charge_mode_id" +
                    "   INNER JOIN hh_rsv_room HRR ON HRR.id = HRP.id AND HRR.sales_flag=1" +
                    "   INNER JOIN hh_rsv_room_remainder HRRR ON HRRR.id = HRP.id AND HRRR.seq = HRR.seq AND HRRR.status = 1" +
                    " WHERE HRP.id = ?" +
                    " AND HRP.sales_flag = 1" +
                    " AND HRP.publishing_flag = 1" +
                    " AND HRDC.cal_date =" + startDate +
                    " AND HRRR.cal_date =" + startDate +
                    " AND HRP.sales_start_date <=" + startDate +
                    " AND HRP.sales_end_date >=" + endDate +
                    " AND HRP.disp_start_date <=" + today +
                    " AND HRP.disp_end_date >=" + today;

            // 検索人数指定時はセットする
            if ( adult > 0 && adult < SEARCH_ADULT_MAX_NUM )
            {
                query += " AND HRP.min_num_adult <= " + adult + " AND HRP.max_num_adult >= " + adult;
            }
            // 大人の指定人数4人以上なら
            if ( adult == SEARCH_ADULT_MAX_NUM )
            {
                // 指定人数以上固定で扱う
                query += " AND HRP.max_num_adult >= " + adult;
            }
            // 子供の指定人数3人以上なら
            if ( child == SEARCH_CHILD_MAX_NUM )
            {
                // 指定人数以上固定で扱う
                query += " AND HRP.max_num_child >= " + child;
            }
            else
            {
                query += " AND HRP.min_num_child <= " + child + " AND HRP.max_num_child >= " + child;
            }
        }
        else
        {
            // 予約開始日が検索終了日よりも早く、終了日が検索開始日よりも遅いものを取得
            query = " SELECT DISTINCT HRP.* FROM hh_rsv_plan HRP" +
                    "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRP.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag)" +
                    "   INNER JOIN hh_rsv_plan_charge HRPC ON HRP.id = HRPC.id AND HRP.plan_id = HRPC.plan_id" +
                    " WHERE HRP.id = ?" +
                    " AND HRP.sales_flag = 1" +
                    " AND HRP.publishing_flag = 1" +
                    " AND HRP.sales_start_date <=" + endDate +
                    " AND HRP.sales_end_date >=" + startDate +
                    " AND HRP.disp_start_date <=" + today +
                    " AND HRP.disp_end_date >=" + today;

            // 検索人数指定時はセットする
            if ( adult > 0 && adult < SEARCH_ADULT_MAX_NUM )
            {
                query += " AND HRP.min_num_adult <= " + adult + " AND HRP.max_num_adult >= " + adult;
            }
            // 大人の指定人数4人以上なら
            if ( adult == SEARCH_ADULT_MAX_NUM )
            {
                // 指定人数以上固定で扱う
                query += " AND HRP.max_num_adult >= " + adult;
            }
            // 子供の指定人数3人以上なら
            if ( child == SEARCH_CHILD_MAX_NUM )
            {
                // 指定人数以上固定で扱う
                query += " AND HRP.max_num_child >= " + child;
            }
            else
            {
                query += " AND HRP.min_num_child <= " + child + " AND HRP.max_num_child >= " + child;
            }
        }
        if ( fromCharge >= 0 && toCharge > 0 && fromCharge <= toCharge )
        {
            if ( adult == 0 )
            {
                if ( child == SEARCH_CHILD_MAX_NUM )
                {
                    query += " AND (";
                    query += "(HRP.max_num_adult = 1 AND (HRPC.adult_one_charge + HRPC.child_add_charge * HRP.min_num_child) >= " + fromCharge + " AND (HRPC.adult_one_charge + HRPC.child_add_charge * HRP.min_num_child) <= " + toCharge + ") OR ";
                    query += "(HRP.min_num_adult > 2 AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult - " + ADULT_TWO + ") + HRPC.child_add_charge * HRP.min_num_child) >= " + fromCharge + " AND" +
                            " (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult - " + ADULT_TWO + ") + HRPC.child_add_charge * HRP.min_num_child) <= " + toCharge + ") OR ";
                    query += "(HRP.max_num_adult >= 2 AND HRP.min_num_adult <= 2 AND (HRPC.adult_two_charge + HRPC.child_add_charge * HRP.min_num_child) >=" + fromCharge + " AND " +
                            "(HRPC.adult_two_charge + HRPC.child_add_charge * HRP.min_num_child) <=" + toCharge + ")";
                    query += ")";
                }
                else
                {
                    query += " AND (";
                    query += "(HRP.max_num_adult = 1 AND (HRPC.adult_one_charge + HRPC.child_add_charge * " + child + ") >= " + fromCharge + " AND (HRPC.adult_one_charge + HRPC.child_add_charge * " + child + ") <= " + toCharge + ") OR ";
                    query += "(HRP.min_num_adult > 2 AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult - " + ADULT_TWO + ") + HRPC.child_add_charge * " + child + ") >= " + fromCharge + " AND" +
                            " (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult - " + ADULT_TWO + ") + HRPC.child_add_charge * " + child + ") <= " + toCharge + ") OR ";
                    query += "(HRP.max_num_adult >= 2 AND HRP.min_num_adult <= 2 AND (HRPC.adult_two_charge + HRPC.child_add_charge * " + child + ") >=" + fromCharge + " AND " +
                            "(HRPC.adult_two_charge + HRPC.child_add_charge * " + child + ") <=" + toCharge + ")";
                    query += ")";
                }
            }
            else if ( adult == 1 )
            {
                if ( child == SEARCH_CHILD_MAX_NUM )
                {
                    // 3人以上の場合は最低人数が3人より多い場合はその人数で取得する
                    query += " AND (((HRP.min_num_child > " + SEARCH_CHILD_MAX_NUM + " ) AND (HRPC.adult_one_charge + HRPC.child_add_charge * HRP.min_num_child) >=" + fromCharge + ") OR " +
                            " ((HRP.min_num_child <= " + SEARCH_CHILD_MAX_NUM + ") AND (HRPC.adult_one_charge + HRPC.child_add_charge * HRP.min_num_child) >= " + fromCharge + ")) " +
                            " AND (((HRP.min_num_child > " + SEARCH_CHILD_MAX_NUM + " ) AND (HRPC.adult_one_charge + HRPC.child_add_charge * HRP.min_num_child) <=" + toCharge + ") OR " +
                            " ((HRP.min_num_child <= " + SEARCH_CHILD_MAX_NUM + ") AND (HRPC.adult_one_charge + HRPC.child_add_charge * HRP.min_num_child) <= " + toCharge + ")) ";
                }
                else
                {
                    query += " AND (HRPC.adult_one_charge + HRPC.child_add_charge * " + child + ") >=" + fromCharge
                            + " AND (HRPC.adult_one_charge + HRPC.child_add_charge * " + child + ") <=" + toCharge;
                }
            }
            else if ( adult == 2 )
            {
                if ( child == SEARCH_CHILD_MAX_NUM )
                {
                    // 3人以上の場合は最低人数が3人より多い場合はその人数で取得する
                    query += " AND (((HRP.min_num_child > " + SEARCH_CHILD_MAX_NUM + " ) AND (HRPC.adult_two_charge + HRPC.child_add_charge * HRP.min_num_child) >=" + fromCharge + ") OR " +
                            " ((HRP.min_num_child <= " + SEARCH_CHILD_MAX_NUM + ") AND (HRPC.adult_two_charge + HRPC.child_add_charge * HRP.min_num_child) >= " + fromCharge + ")) " +
                            " AND (((HRP.min_num_child > " + SEARCH_CHILD_MAX_NUM + " ) AND (HRPC.adult_two_charge + HRPC.child_add_charge * HRP.min_num_child) <=" + toCharge + ") OR " +
                            " ((HRP.min_num_child <= " + SEARCH_CHILD_MAX_NUM + ") AND (HRPC.adult_two_charge + HRPC.child_add_charge * HRP.min_num_child) <= " + toCharge + ")) ";
                }
                else
                {
                    query += " AND (HRPC.adult_two_charge + HRPC.child_add_charge * " + child + ") >=" + fromCharge
                            + " AND (HRPC.adult_two_charge + HRPC.child_add_charge * " + child + ") <=" + toCharge;
                }
            }
            else if ( adult > 2 && adult < SEARCH_ADULT_MAX_NUM )
            {
                if ( child == SEARCH_CHILD_MAX_NUM )
                {
                    // 3人以上の場合は最低人数が3人より多い場合はその人数で取得する
                    query += " AND (((HRP.min_num_child > " + SEARCH_CHILD_MAX_NUM + " ) AND (HRPC.adult_two_charge + HRPC.adult_add_charge * " + (adult - ADULT_TWO) + " HRPC.child_add_charge * HRP.min_num_child) >=" + fromCharge + ") OR " +
                            " ((HRP.min_num_child <= " + SEARCH_CHILD_MAX_NUM + ") AND (HRPC.adult_two_charge + HRPC.adult_add_charge * " + (adult - ADULT_TWO) + " HRPC.child_add_charge * HRP.min_num_child) >= " + fromCharge + ")) " +
                            " AND (((HRP.min_num_child > " + SEARCH_CHILD_MAX_NUM + " ) AND (HRPC.adult_two_charge + HRPC.adult_add_charge * " + (adult - ADULT_TWO) + " HRPC.child_add_charge * HRP.min_num_child) <=" + toCharge + ") OR " +
                            " ((HRP.min_num_child <= " + SEARCH_CHILD_MAX_NUM + ") AND (HRPC.adult_two_charge + HRPC.adult_add_charge * " + (adult - ADULT_TWO) + " HRPC.child_add_charge * HRP.min_num_child) <= " + toCharge + ")) ";
                }
                else
                {
                    query += " AND (HRPC.adult_two_charge + HRPC.adult_add_charge * " + (adult - ADULT_TWO) + " + HRPC.child_add_charge * " + child + ") >=" + fromCharge
                            + " AND (HRPC.adult_two_charge + HRPC.adult_add_charge * " + (adult - ADULT_TWO) + "  + HRPC.child_add_charge * " + child + ") <=" + toCharge;
                }
            }
            else if ( adult > 2 && adult >= SEARCH_ADULT_MAX_NUM )
            {
                if ( child == SEARCH_CHILD_MAX_NUM )
                {
                    query += " AND (((HRP.min_num_adult > " + SEARCH_ADULT_MAX_NUM + " ) AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult -" + ADULT_TWO + ") + HRPC.child_add_charge * HRP.min_num_child) >=" + fromCharge + ") OR " +
                            " ((HRP.min_num_adult <= " + SEARCH_ADULT_MAX_NUM + ") AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult -" + ADULT_TWO + ") + HRPC.child_add_charge * HRP.min_num_child) >= " + fromCharge + ")) " +
                            " AND (((HRP.min_num_adult > " + SEARCH_ADULT_MAX_NUM + " ) AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult -" + ADULT_TWO + ") + HRPC.child_add_charge * HRP.min_num_child) <=" + toCharge + ") OR " +
                            " ((HRP.min_num_adult <= " + SEARCH_ADULT_MAX_NUM + ") AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult -" + ADULT_TWO + ") + HRPC.child_add_charge * HRP.min_num_child) <= " + toCharge + ")) ";
                }
                else
                {
                    query += " AND (((HRP.min_num_adult > " + SEARCH_ADULT_MAX_NUM + " ) AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult -" + ADULT_TWO + ") + HRPC.child_add_charge * " + child + ") >=" + fromCharge + ") OR " +
                            " ((HRP.min_num_adult <= " + SEARCH_ADULT_MAX_NUM + ") AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult -" + ADULT_TWO + ") + HRPC.child_add_charge * " + child + ") >= " + fromCharge + ")) " +
                            " AND (((HRP.min_num_adult > " + SEARCH_ADULT_MAX_NUM + " ) AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult -" + ADULT_TWO + ") + HRPC.child_add_charge * " + child + ") <=" + toCharge + ") OR " +
                            " ((HRP.min_num_adult <= " + SEARCH_ADULT_MAX_NUM + ") AND (HRPC.adult_two_charge + HRPC.adult_add_charge * (HRP.min_num_adult -" + ADULT_TWO + ") + HRPC.child_add_charge * " + child + ") <= " + toCharge + ")) ";
                }
            }
        }
        else if ( fromCharge > toCharge )
        {
            return null;
        }

        query += " ORDER BY HRP.disp_index, HRP.plan_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                // 予約上限数の確認
                while( result.next() != false )
                {
                    if ( startDate == endDate &&
                            (rsvcomm.checkMaxRsvRommCnt( hotelId, result.getInt( "plan_id" ), startDate, 0 ) == false
                            || this.getEmptyRoom( hotelId, startDate, result.getInt( "plan_id" ) ) == 0) )
                    {
                        minuscount++;
                    }
                }

                // レコード件数取得
                if ( result.last() != false )
                {
                    // 総件数の取得
                    this.m_planAllCount = result.getRow();
                    this.m_planAllCount -= minuscount;
                }
                // 件数が0になっていたら追加しない
                if ( this.m_planAllCount > 0 )
                {
                    arrPlanIdList = new int[this.m_planAllCount];

                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // 対象プランが予約上限にいってるか確認
                        if ( startDate != endDate ||
                                (rsvcomm.checkMaxRsvRommCnt( hotelId, result.getInt( "plan_id" ), startDate, 0 ) == true &&
                                this.getEmptyRoom( hotelId, startDate, result.getInt( "plan_id" ) ) > 0) )

                        {
                            arrPlanIdList[count++] = result.getInt( "plan_id" );
                        }
                    }
                }
            }

            return arrPlanIdList;
        }
        catch ( Exception e )
        {
            Logging.error( query );
            Logging.error( "[SearchPlanId.getSearchPlanIdList1 : hotelId = " + hotelId + "] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 空き部屋を求める処理
     * 
     * @param hotelId
     * @param date
     * @return
     */
    public int getEmptyRoom(int hotelId, int date)
    {
        int remainderCount = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT COUNT(HRRR.seq)  FROM hh_rsv_rel_plan_room HRRPR" +
                "  INNER JOIN hh_rsv_reserve_basic HRRB ON HRRPR.id = HRRB.id AND (HRRB.sales_flag = 1 OR HRRB.pre_open_flag=1)" +
                "  INNER JOIN hh_rsv_plan HRP ON HRRPR.id = HRP.id AND HRRPR.plan_id = HRP.plan_id AND HRP.sales_flag=1" +
                "  INNER JOIN hh_rsv_room HRR ON HRRPR.id = HRR.id AND HRR.seq = HRRPR.seq AND HRR.sales_flag=1" +
                "  INNER JOIN hh_rsv_room_remainder HRRR ON HRRPR.id = HRRR.id AND HRRPR.seq = HRRR.seq AND HRRR.status = 1" +
                " WHERE HRRPR.id = ?" +
                " AND HRRPR.sales_start_date <=" + date +
                " AND HRRPR.sales_end_date >=" + date +
                " AND HRRR.cal_date =" + date +
                " GROUP BY HRRR.id, HRRR.cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    remainderCount = result.getInt( 1 );
                }
            }
            else
            {
                remainderCount = 0;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchPlanId.getSearchPlanIdList2 : hotelId = " + hotelId + "] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(remainderCount);
    }

    /**
     * 空き部屋を求める処理
     * 
     * @param hotelId
     * @param date
     * @return
     */
    public int getEmptyRoom(int hotelId, int date, int planId)
    {
        int remainderCount = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT COUNT(HRRR.seq)  FROM hh_rsv_rel_plan_room HRRPR" +
                "  INNER JOIN hh_rsv_reserve_basic HRRB ON HRRPR.id = HRRB.id AND (HRRB.sales_flag = 1 OR HRRB.pre_open_flag=1)" +
                "  INNER JOIN hh_rsv_plan HRP ON HRRPR.id = HRP.id AND HRRPR.plan_id = HRP.plan_id AND HRP.sales_flag=1" +
                "  INNER JOIN hh_rsv_room HRR ON HRRPR.id = HRR.id AND HRR.seq = HRRPR.seq AND HRR.sales_flag=1" +
                "  INNER JOIN hh_rsv_room_remainder HRRR ON HRRPR.id = HRRR.id AND HRRPR.seq = HRRR.seq AND HRRR.status = 1" +
                " WHERE HRRPR.id = ?" +
                " AND HRP.plan_id = ?" +
                " AND HRRPR.sales_start_date <=" + date +
                " AND HRRPR.sales_end_date >=" + date +
                " AND HRRR.cal_date =" + date +
                " GROUP BY HRRR.id, HRRR.cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    remainderCount = result.getInt( 1 );
                }
            }
            else
            {
                remainderCount = 0;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[SearchPlanId.getSearchPlanIdList2 : hotelId = " + hotelId + "] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(remainderCount);
    }

}
