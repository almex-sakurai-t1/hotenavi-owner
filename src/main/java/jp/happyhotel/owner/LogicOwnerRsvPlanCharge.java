package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;

/**
 * プラン別料金設定ビジネスロジック
 */
public class LogicOwnerRsvPlanCharge implements Serializable
{
    private static final long                    serialVersionUID = 2282025787630209565L;
    private FormOwnerRsvPlanCharge               frm;
    private FormOwnerRsvPlanChargeSub            frmSub;
    private ArrayList<FormOwnerRsvPlanChargeSub> subFormList;

    /* フォームオブジェクト */
    public FormOwnerRsvPlanCharge getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvPlanCharge frm)
    {
        this.frm = frm;
    }

    public FormOwnerRsvPlanChargeSub getFrmSub()
    {
        return frmSub;
    }

    public void setFrmSub(FormOwnerRsvPlanChargeSub frmSub)
    {
        this.frmSub = frmSub;
    }

    public ArrayList<FormOwnerRsvPlanChargeSub> getSubFormList()
    {
        return subFormList;
    }

    public void setSubFormList(ArrayList<FormOwnerRsvPlanChargeSub> subFormList)
    {
        this.subFormList = subFormList;
    }

    /*
     * コンストラクタ
     */
    public LogicOwnerRsvPlanCharge()
    {
        subFormList = new ArrayList<FormOwnerRsvPlanChargeSub>();
    }

    /**
     * 料金データ未登録の料金モード取得
     * 
     * @param なし
     * @return
     */
    public boolean existsChargeMode() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;
        boolean ret = false;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_rsv_charge_mode ";
        query = query + "WHERE id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt == 0 )
            {
                return ret;
            }

            ret = true;
            return ret;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.existsChargeMode()] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 全料金ﾓｰﾄﾞの取得
     * 
     * @return
     */
    public void getPlanCharge() throws Exception
    {
        try
        {
            // 登録済料金ﾓｰﾄﾞの取得
            getRegistPlanCharge();
            // 非登録料金ﾓｰﾄﾞの取得
            getUnRegistPlan();

            frm.setFrmSubList( subFormList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.getPlanCharge()] Exception=" + e.toString() );
            throw new Exception( e );
        }
    }

    /**
     * 登録済料金ﾓｰﾄﾞの取得
     * 
     * @return
     */
    private boolean getRegistPlanCharge() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;
        boolean ret = false;
        String planNm = "";

        query = query + "SELECT cm.id, cm.charge_mode_id, cm.charge_mode_name, pc.remarks, pc.week_status,";
        query = query + "pc.plan_id, pc.charge_mode_id, pc.ci_time_from, pc.ci_time_to, pc.co_time, pc.co_remarks, ";
        query = query + "pc.adult_two_charge, pc.adult_one_charge, pc.adult_add_charge, pc.child_add_charge, pc.co_kind ";
        query = query + "FROM hh_rsv_charge_mode cm ";
        query = query + "   LEFT JOIN hh_rsv_plan_charge pc ON cm.id = pc.id AND cm.charge_mode_id = pc.charge_mode_id ";
        query = query + "WHERE cm.id = ? ";
        query = query + "  AND (cm.charge_mode_name is not null and length(rtrim(ltrim(cm.charge_mode_name))) > 0) ";
        query = query + "  AND pc.plan_id = ? ORDER BY cm.disp_index";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelPlanId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frmSub = new FormOwnerRsvPlanChargeSub();
                frmSub.setCheck( 1 );
                frmSub.setPlanId( result.getInt( "plan_id" ) );
                frmSub.setChargeModeId( result.getInt( "charge_mode_id" ) );
                frmSub.setChargeModeNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "charge_mode_name" ) ) ) );
                frmSub.setRemarks( CheckString.checkStringForNull( ConvertCharacterSet.convDb2Form( result.getString( "remarks" ) ) ) );
                frmSub.setWeekStatus( result.getInt( "week_status" ) );
                frmSub.setCiTimeFromHH( ConvertTime.convTimeHH( result.getInt( "ci_time_from" ) ) );
                frmSub.setCiTimeFromMM( ConvertTime.convTimeMM( result.getInt( "ci_time_from" ) ) );
                frmSub.setCiTimeToHH( ConvertTime.convTimeHH( result.getInt( "ci_time_to" ) ) );
                frmSub.setCiTimeToMM( ConvertTime.convTimeMM( result.getInt( "ci_time_to" ) ) );
                frmSub.setCoTimeHH( ConvertTime.convTimeHH( result.getInt( "co_time" ) ) );
                frmSub.setCoTimeMM( ConvertTime.convTimeMM( result.getInt( "co_time" ) ) );
                frmSub.setAdultTwo( Integer.toString( result.getInt( "adult_two_charge" ) ) );
                frmSub.setAdultOne( Integer.toString( result.getInt( "adult_one_charge" ) ) );
                frmSub.setAdultAdd( Integer.toString( result.getInt( "adult_add_charge" ) ) );
                frmSub.setChildAdd( Integer.toString( result.getInt( "child_add_charge" ) ) );
                frmSub.setCoRemarks( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "co_remarks" ) ) ) );
                frmSub.setRegistStatus( 1 );
                frmSub.setCoKind( result.getInt( "co_kind" ) );
                subFormList.add( frmSub );
            }

            if ( result.last() != false )
            {
                cnt = result.getRow();
            }

            // 該当データがない場合
            if ( cnt == 0 )
            {
                // プラン名称の取得
                planNm = getPlanNm( frm.getSelPlanId() );
                frm.setErrMsg( Message.getMessage( "warn.30020", planNm ) + "</br>" + frm.getErrMsg() );
                return ret;
            }

            ret = true;
            return ret;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.getRegistPlanCharge()] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 未登録料金ﾓｰﾄﾞの取得
     * 
     * @return
     */
    private boolean getUnRegistPlan() throws Exception
    {
        boolean ret = false;
        String query = "";
        String sqlCond = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        // 登録済み料金データ条件用SQLの取得
        sqlCond = createChargeModeCond();

        query = query + "SELECT charge_mode_id, charge_mode_name ";
        query = query + " FROM hh_rsv_charge_mode WHERE id = ? AND (charge_mode_name is not null and length(rtrim(ltrim(charge_mode_name))) > 0) ";
        query = query + sqlCond + " ORDER BY disp_index";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();

            while( result.next() != false )
            {
                frmSub = new FormOwnerRsvPlanChargeSub();
                frmSub.setCheck( 0 );
                frmSub.setPlanId( frm.getSelPlanId() );
                frmSub.setChargeModeId( result.getInt( "charge_mode_id" ) );
                frmSub.setChargeModeNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "charge_mode_name" ) ) ) );
                frmSub.setRemarks( "" );
                frmSub.setCiTimeFromHH( "00" );
                frmSub.setCiTimeFromMM( "00" );
                frmSub.setCiTimeToHH( "00" );
                frmSub.setCiTimeToMM( "00" );
                frmSub.setCoTimeHH( "00" );
                frmSub.setCoTimeMM( "00" );
                frmSub.setAdultTwo( "0" );
                frmSub.setAdultOne( "0" );
                frmSub.setAdultAdd( "0" );
                frmSub.setChildAdd( "0" );
                frmSub.setCoRemarks( "" );
                frmSub.setRegistStatus( 0 );
                frmSub.setCoKind( 0 );
                subFormList.add( frmSub );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.getUnRegistPlan()] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * プラン名取得
     * 
     * @param int planId
     * @return プラン名
     */
    private String getPlanNm(int planId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String planNm = "";

        query = query + "SELECT plan_name FROM hh_rsv_plan ";
        query = query + "WHERE plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                planNm = ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.getPlanNm()] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return planNm;
    }

    /**
     * 
     * プランカレンダー作成処理
     * 
     * @param salesStopWeekStatus 販売停止曜日ステータス
     * 
     */
    public void addPlanCalendar(int salesStopWeekStatus)
    {
        int daysadd = 120;
        Calendar cal = Calendar.getInstance();
        int addYYYYMMDD = 0;
        int nowYYYYMMDD = 0;
        int caldate = 0;
        int chargemodeid = 0;
        int week = 0;
        int holidaykind = 0;
        int beforeHolidaykind = 0;
        int nowstatus = 0;
        int getchargemodeid = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> salesstopweeklist = null;

        try
        {
            // 現在日を取得する
            nowYYYYMMDD = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

            // 2ｶ月後の日を取得する
            cal = Calendar.getInstance();
            cal.add( Calendar.DATE, daysadd );
            addYYYYMMDD = cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE );

            // 始まりは必ず1日からにする
            nowYYYYMMDD = nowYYYYMMDD / 100 * 100 + 1;
            // ホテルカレンダー取得
            query = " Select B.id, M.plan_id, C.cal_date, C.charge_mode_id, C.week, C.holiday_kind FROM hh_hotel_basic B ";
            query += " INNER JOIN hh_rsv_plan M ON M.id = B.id ";
            query += " INNER JOIN hh_rsv_hotel_calendar C ON C.id = B.id ";
            query += " LEFT JOIN hh_rsv_day_charge R ";
            query += " ON R.id = B.id AND R.plan_id = M.plan_id AND R.cal_date = C.cal_date ";
            query += " WHERE B.rank >= 2 AND B.kind <= 7 ";
            // 販売前のプランでも作成したいので、下記の条件を外す
            // query += " AND M.sales_flag = 1 AND M.publishing_flag = 1 ";
            query += " AND R.cal_date IS NULL ";
            query += " AND C.cal_date >= ?";
            query += " AND C.cal_date < ?";
            query += " AND C.cal_date >= M.sales_start_date AND C.cal_date <= M.sales_end_date";
            query += " AND B.id = ?";
            query += " AND M.plan_id = ?";
            query += " ORDER BY B.id, M.plan_id, C.cal_date";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, nowYYYYMMDD );
            prestate.setInt( 2, addYYYYMMDD );
            prestate.setInt( 3, frm.getSelHotelId() );
            prestate.setInt( 4, frm.getSelPlanId() );
            result = prestate.executeQuery();
            Logging.info( "createCalendar:" + nowYYYYMMDD + "～" + addYYYYMMDD );

            // 対象の日数分プラン別カレンダーのINSERT
            while( result.next() != false )
            {
                beforeHolidaykind = 0;
                caldate = result.getInt( "cal_date" );
                chargemodeid = result.getInt( "charge_mode_id" );
                week = result.getInt( "week" );
                holidaykind = result.getInt( "holiday_kind" );
                // 販売停止曜日リスト取得
                salesstopweeklist = getSalesStopWeekList( salesStopWeekStatus );

                // 祝前日フラグのチェック
                if ( checkBeforeHoliday( caldate ) == true )
                {
                    beforeHolidaykind = 1;
                }
                else
                {
                    beforeHolidaykind = 0;
                }

                // その日の適用料金モードをセット
                nowstatus = getTodayChargeMode( week, holidaykind, beforeHolidaykind );

                // ステータスに応じた料金ﾓｰﾄﾞを取得する
                getchargemodeid = getChargeModeId( nowstatus );
                if ( getchargemodeid > 0 )
                {
                    chargemodeid = getchargemodeid;
                }
                // 販売停止の曜日の場合はchargemodeを0にする
                if ( checkSalesStop( nowstatus, salesstopweeklist ) == true )
                {
                    chargemodeid = 0;
                }

                // カラム追加処理
                insertReserveDayCharge( caldate, chargemodeid );
            }
        }
        catch ( Exception ex )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.addPlanCalendar()] Exception=" + ex.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return;
    }

    /**
     * 
     * プラン日別カレンダー追加
     * 
     * @param caldate カレンダ日付
     * @param chargemodeid 料金ﾓｰﾄﾞ
     * 
     */
    private void insertReserveDayCharge(int caldate, int chargemodeid)
    {
        String query = "";
        Connection connection = null;
        Calendar cal = Calendar.getInstance();
        PreparedStatement prestate = null;

        try
        {
            query = "insert into hh_rsv_day_charge set id = ?, plan_id = ?, cal_date = ?, charge_mode_id = ?, hotel_id = '', user_id = 0, last_update = ?, last_uptime = ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelPlanId() );
            prestate.setInt( 3, caldate );
            prestate.setInt( 4, chargemodeid );
            prestate.setInt( 5, cal.get( Calendar.YEAR ) * 10000 + (cal.get( Calendar.MONTH ) + 1) * 100 + cal.get( Calendar.DATE ) );
            prestate.setInt( 6, cal.get( Calendar.HOUR_OF_DAY ) * 10000 + cal.get( Calendar.MINUTE ) * 100 + cal.get( Calendar.SECOND ) );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "reservePlanChargeRunner.insertReserveDayCharge():" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return;
    }

    /**
     * 
     * 販売停止チェック処理
     * 
     * @param status チェック対象曜日ステータス
     * @param statusList 販売停止曜日リスト
     * 
     */
    private boolean checkSalesStop(int status, ArrayList<Integer> statusList)
    {
        boolean ret = false;

        try
        {
            for( int i = 0 ; i < statusList.size() ; i++ )
            {
                if ( status == statusList.get( i ) )
                {
                    ret = true;
                    break;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.checkSalesStop()] Exception=" + e.toString() );
        }
        finally
        {

        }

        return(ret);
    }

    /**
     * 
     * 料金ﾓｰﾄﾞID取得処理
     * 
     * @param status 確認カレンダー曜日
     * @return 料金モードID
     * 
     */
    private int getChargeModeId(int status)
    {
        int ret = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "select charge_mode_id, week_status from hh_rsv_plan_charge where id = ? and plan_id = ? ";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelPlanId() );
            result = prestate.executeQuery();
            while( result.next() != false )
            {
                // 対象の料金モードを返す
                if ( (result.getInt( "week_status" ) & status) == status )
                {
                    ret = result.getInt( "charge_mode_id" );
                    break;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.getChargeModeId] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 翌日祝日チェック処理
     * 
     * @param caldate チェック日付
     * @return true→翌日祝日
     */
    private boolean checkBeforeHoliday(int caldate)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "select holiday_kind from hh_rsv_hotel_calendar where id = ? AND cal_date > ? order by cal_date limit 1";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, caldate );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                // 翌日が祝日の場合結果True
                if ( result.getInt( "holiday_kind" ) > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.checkBeforeHoliday()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 販売停止曜日リスト取得
     * 
     * @param int salesStopWeekStatus 売上停止曜日ステータス
     * @return 販売停止曜日リスト
     */
    private ArrayList<Integer> getSalesStopWeekList(int salesStopWeekStatus)
    {
        ArrayList<Integer> ret = new ArrayList<Integer>();

        try
        {
            if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_MONDAY) == OwnerRsvCommon.CALENDAR_MONDAY )
            {
                ret.add( OwnerRsvCommon.CALENDAR_MONDAY );
            }
            if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_TUESDAY) == OwnerRsvCommon.CALENDAR_TUESDAY )
            {
                ret.add( OwnerRsvCommon.CALENDAR_TUESDAY );
            }
            if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_WEDNESDAY) == OwnerRsvCommon.CALENDAR_WEDNESDAY )
            {
                ret.add( OwnerRsvCommon.CALENDAR_WEDNESDAY );
            }
            if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_THIRTHDAY) == OwnerRsvCommon.CALENDAR_THIRTHDAY )
            {
                ret.add( OwnerRsvCommon.CALENDAR_THIRTHDAY );
            }
            if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_FRIDAY) == OwnerRsvCommon.CALENDAR_FRIDAY )
            {
                ret.add( OwnerRsvCommon.CALENDAR_FRIDAY );
            }
            if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_SATURDAY) == OwnerRsvCommon.CALENDAR_SATURDAY )
            {
                ret.add( OwnerRsvCommon.CALENDAR_SATURDAY );
            }
            if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_SUNDAY) == OwnerRsvCommon.CALENDAR_SUNDAY )
            {
                ret.add( OwnerRsvCommon.CALENDAR_SUNDAY );
            }
            if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_HOLIDAY) == OwnerRsvCommon.CALENDAR_HOLIDAY )
            {
                ret.add( OwnerRsvCommon.CALENDAR_HOLIDAY );
            }
            if ( (salesStopWeekStatus & OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY) == OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY )
            {
                ret.add( OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.getSalesStopWeekList()] Exception=" + e.toString() );
        }
        finally
        {
        }

        return(ret);
    }

    /**
     * 登録済み料金データ条件用SQLの取得
     * 
     * @param なし
     * @return SQLの条件文
     */
    public String createChargeModeCond() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String sql = "";

        // 登録済み料金データ条件用SQLの取得
        query = query + "SELECT charge_mode_id FROM hh_rsv_plan_charge ";
        query = query + "WHERE id = ? AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelPlanId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( sql.trim().length() == 0 )
                {
                    sql = sql + " AND (";
                }
                else
                {
                    sql = sql + " AND ";
                }
                sql = sql + "(charge_mode_id <> " + result.getInt( "charge_mode_id" ) + ")";
            }
            if ( sql.trim().length() != 0 )
            {
                sql = sql + ")";
            }

            return sql;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.createChargeModeCond()] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 最低予約金額取得
     * 
     * @param なし
     * @return
     */
    public int getChashDeposit(int hotelId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int chashDeposit = 0;

        query = query + "SELECT cash_deposit ";
        query = query + "FROM hh_rsv_reserve_basic ";
        query = query + "WHERE id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                // frm.setCashDeposit( result.getInt( "cash_deposit" ) );
                chashDeposit = result.getInt( "cash_deposit" );

            }

            return chashDeposit;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.getChashDeposit()] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * プラン別料金マスタ追加更新処理
     * 
     * @throws Exception
     */
    public boolean addPlanCharge() throws Exception
    {
        boolean ret = true;

        ArrayList<FormOwnerRsvPlanChargeSub> frmSubList = new ArrayList<FormOwnerRsvPlanChargeSub>();
        frmSubList = frm.getFrmSubList();

        for( int i = 0 ; i < frmSubList.size() ; i++ )
        {
            FormOwnerRsvPlanChargeSub frmSub = frmSubList.get( i );
            if ( frmSub.getRegistStatus() == 0 )
            {
                // 新規登録
                if ( execInsPlanCharge( frmSub ) == false )
                {
                    return false;
                }
            }
            else if ( frmSub.getRegistStatus() == 1 )
            {
                // 更新
                if ( execUpdPlanCharge( frmSub ) == false )
                {
                    return false;
                }
            }
        }

        // 販売停止曜日更新
        execUpdSalesStopWeekStatus();

        return(ret);
    }

    /**
     * プラン別料金マスタ追加実処理
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    private boolean execInsPlanCharge(FormOwnerRsvPlanChargeSub frmSub) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_rsv_plan_charge SET ";
        query = query + " id = ?,";
        query = query + " plan_id = ?,";
        query = query + " charge_mode_id = ?,";
        query = query + " ci_time_from = ?,";
        query = query + " ci_time_to = ?,";
        query = query + " co_time = ?,";
        query = query + " co_remarks = ?,";
        query = query + " adult_two_charge = ?,";
        query = query + " adult_one_charge = ?,";
        query = query + " adult_add_charge = ?,";
        query = query + " child_add_charge = ?,";
        query = query + " remarks = ?,";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " week_status = ?,";
        query = query + " co_kind = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getSelPlanId() );
            prestate.setInt( 3, frmSub.getChargeModeId() );
            prestate.setInt( 4, ConvertTime.convTimeSS( Integer.parseInt( frmSub.getCiTimeFromHH() ), Integer.parseInt( frmSub.getCiTimeFromMM() ), 2 ) );
            prestate.setInt( 5, ConvertTime.convTimeSS( Integer.parseInt( frmSub.getCiTimeToHH() ), Integer.parseInt( frmSub.getCiTimeToMM() ), 2 ) );
            prestate.setInt( 6, ConvertTime.convTimeSS( Integer.parseInt( frmSub.getCoTimeHH() ), Integer.parseInt( frmSub.getCoTimeMM() ), 2 ) );
            prestate.setString( 7, ConvertCharacterSet.convForm2Db( frmSub.getCoRemarks() ) );
            prestate.setInt( 8, Integer.parseInt( frmSub.getAdultTwo() ) );
            prestate.setInt( 9, Integer.parseInt( frmSub.getAdultOne() ) );
            prestate.setInt( 10, Integer.parseInt( frmSub.getAdultAdd() ) );
            if ( frmSub.getChildAdd() == null )
            {
                prestate.setInt( 11, 0 );
            }
            else
            {
                prestate.setInt( 11, Integer.parseInt( frmSub.getChildAdd() ) );
            }
            prestate.setString( 12, ConvertCharacterSet.convForm2Db( frmSub.getRemarks() ) );
            prestate.setString( 13, frm.getOwnerHotelID() );
            prestate.setInt( 14, frm.getUserId() );
            prestate.setInt( 15, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 16, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 17, frmSub.getWeekStatus() );
            prestate.setInt( 18, frmSub.getCoKind() );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerPlanCharge.execInsPlanCharge()] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerPlanCharge.execInsPlanCharge()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 販売停止曜日ステータス更新実処理
     * 
     * @return boolean true:処理成功、false:処理失敗
     * @throws Exception
     */
    public boolean execUpdSalesStopWeekStatus() throws Exception
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "UPDATE hh_rsv_plan SET ";
        query = query + " sales_stop_week_status = ? ";
        query = query + " WHERE id = ?";
        query = query + "   AND plan_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSalesStopWeekStatus() );
            prestate.setInt( 2, frm.getSelHotelId() );
            prestate.setInt( 3, frm.getSelPlanId() );

            if ( prestate.executeUpdate() > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerPlanCharge.execUpdSalesStopWeekStatus()] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerPlanCharge.execUpdSalesStopWeekStatus()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * プラン別料金マスタ更新実処理
     * 
     * @param frmSub FormOwnerRsvPlanChargeSubオブジェクト
     * @return boolean true:処理成功、false:処理失敗
     * @throws Exception
     */
    public boolean execUpdPlanCharge(FormOwnerRsvPlanChargeSub frmSub) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_plan_charge SET ";
        query = query + " ci_time_from = ?,";
        query = query + " ci_time_to = ?,";
        query = query + " co_time = ?,";
        query = query + " co_remarks = ?,";
        query = query + " adult_two_charge = ?,";
        query = query + " adult_one_charge = ?,";
        query = query + " adult_add_charge = ?,";
        query = query + " child_add_charge = ?,";
        query = query + " remarks = ?,";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " week_status = ?,";
        query = query + " co_kind = ?";
        query = query + " WHERE id = ?";
        query = query + "   AND plan_id = ?";
        query = query + "   AND charge_mode_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, ConvertTime.convTimeSS( Integer.parseInt( frmSub.getCiTimeFromHH() ), Integer.parseInt( frmSub.getCiTimeFromMM() ), 2 ) );
            prestate.setInt( 2, ConvertTime.convTimeSS( Integer.parseInt( frmSub.getCiTimeToHH() ), Integer.parseInt( frmSub.getCiTimeToMM() ), 2 ) );
            prestate.setInt( 3, ConvertTime.convTimeSS( Integer.parseInt( frmSub.getCoTimeHH() ), Integer.parseInt( frmSub.getCoTimeMM() ), 2 ) );
            prestate.setString( 4, ConvertCharacterSet.convForm2Db( frmSub.getCoRemarks() ) );
            prestate.setInt( 5, Integer.parseInt( frmSub.getAdultTwo() ) );
            prestate.setInt( 6, Integer.parseInt( frmSub.getAdultOne() ) );
            prestate.setInt( 7, Integer.parseInt( frmSub.getAdultAdd() ) );
            // 子供追加料金がなければ0をセット
            if ( frmSub.getChildAdd() == null )
            {
                prestate.setInt( 8, 0 );
            }
            else
            {
                prestate.setInt( 8, Integer.parseInt( frmSub.getChildAdd() ) );
            }
            prestate.setString( 9, ConvertCharacterSet.convForm2Db( frmSub.getRemarks() ) );
            prestate.setString( 10, frm.getOwnerHotelID() );
            prestate.setInt( 11, frm.getUserId() );
            prestate.setInt( 12, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 13, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 14, frmSub.getWeekStatus() );
            prestate.setInt( 15, frmSub.getCoKind() );
            prestate.setInt( 16, frm.getSelHotelId() );
            prestate.setInt( 17, frm.getSelPlanId() );
            prestate.setInt( 18, frmSub.getChargeModeId() );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerPlanCharge.execUpdPlanCharge()] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerPlanCharge.execUpdPlanCharge()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 
     * 
     * @param int hotelId
     * @param int planId
     * @param int chargeModeId
     * @return true:false:
     * @throws Exception
     */
    public void execDelChargeMode(int hotelId, int planId, int chargeModeId) throws Exception
    {
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "DELETE FROM hh_rsv_plan_charge ";
        query = query + " WHERE  id = ? ";
        query = query + "   AND plan_id = ? ";
        query = query + "   AND charge_mode_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, chargeModeId );

            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.execDelChargeMode()] Exception=" + e.toString() );
            throw new Exception( "[LogicOwnerRsvPlanCharge.execDelChargeMode()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /***
     * 料金モード取得
     * 
     * @param id "ホテルID"
     * @param planId "プランID"
     * @param calDate "カレンダー日付"
     * @return 料金モード
     */
    public int getChargeMode(int id, int planId, int calDate)
    {
        int daysadd = 60;
        Calendar cal = Calendar.getInstance();
        int addYYYYMMDD = 0;
        int nowYYYYMMDD = 0;
        int caldate = 0;
        int chargemodeid = 0;
        int week = 0;
        int holidaykind = 0;
        int beforeHolidaykind = 0;
        int nowstatus = 0;
        int getchargemodeid = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> salesstopweeklist = null;

        try
        {
            // ホテルカレンダー取得
            query = " Select B.id, M.plan_id, C.cal_date, C.charge_mode_id, C.week, C.holiday_kind FROM hh_hotel_basic B ";
            query += " INNER JOIN hh_rsv_plan M ON M.id = B.id ";
            query += " INNER JOIN hh_rsv_hotel_calendar C ON C.id = B.id ";
            query += " LEFT JOIN hh_rsv_day_charge R ";
            query += " ON R.id = B.id AND R.plan_id = M.plan_id AND R.cal_date = C.cal_date ";
            query += " WHERE B.rank >= 2 AND B.kind <= 7 ";
            query += " AND M.sales_flag = 1 AND M.publishing_flag = 1 ";
            query += " AND C.cal_date = ?";
            query += " AND B.id = ?";
            query += " AND M.plan_id = ?";
            query += " ORDER BY B.id, M.plan_id, C.cal_date";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, calDate );
            prestate.setInt( 2, id );
            prestate.setInt( 3, planId );
            result = prestate.executeQuery();

            // 対象の日数分プラン別カレンダーのINSERT
            while( result.next() != false )
            {
                caldate = result.getInt( "cal_date" );
                chargemodeid = result.getInt( "charge_mode_id" );
                week = result.getInt( "week" );
                holidaykind = result.getInt( "holiday_kind" );

                // 祝前日フラグのチェック
                if ( checkBeforeHoliday( caldate ) == true )
                {
                    beforeHolidaykind = 1;
                }
                else
                {
                    beforeHolidaykind = 0;
                }

                // その日の適用料金モードをセット
                nowstatus = getTodayChargeMode( week, holidaykind, beforeHolidaykind );

                // ステータスに応じた料金ﾓｰﾄﾞを取得する
                getchargemodeid = getChargeModeId( nowstatus );
                if ( getchargemodeid > 0 )
                {
                    chargemodeid = getchargemodeid;
                }
            }
        }
        catch ( Exception ex )
        {
            Logging.error( "[LogicOwnerRsvPlanCharge.addPlanCalendar()] Exception=" + ex.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return chargemodeid;
    }

    /*****
     * 適用料金モードチェック
     * 
     * @param week
     * @param holidayKind
     * @param beforeHolidayKind
     * @return
     */
    private static int getTodayChargeMode(int week, int holidayKind, int beforeHolidayKind)
    {
        int nowstatus = 0;

        switch( week )
        {

        // 日曜
            case 0:
                // 日曜は祝日でも日曜料金を優先
                nowstatus = OwnerRsvCommon.CALENDAR_SUNDAY;

                // 月曜が祝日の場合のみ祝前日料金とする
                if ( beforeHolidayKind == 1 )
                {
                    nowstatus = OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
                }
                break;

            // 金曜
            case 5:
                // 土曜祝日の場合は金曜日料金とする
                nowstatus = OwnerRsvCommon.CALENDAR_FRIDAY;

                // 金曜日が祝日の場合は祝前日料金とする
                if ( holidayKind > 0 )
                {
                    nowstatus = OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
                }
                break;

            // 土曜日
            case 6:
                // 土曜日が祝日、日曜日が祝日でも必ず土曜日料金とする
                nowstatus = OwnerRsvCommon.CALENDAR_SATURDAY;
                break;

            // それ以外の平日
            default:
                // 祝前日を最優先
                if ( beforeHolidayKind == 1 )
                {
                    nowstatus = OwnerRsvCommon.CALENDAR_BEFOREHOLIDAY;
                }
                // 祝日が第2優先
                else if ( holidayKind > 0 )
                {
                    nowstatus = OwnerRsvCommon.CALENDAR_HOLIDAY;
                }
                else
                {
                    // 他の日はそのまま曜日をセット
                    if ( week == 1 )
                    {
                        nowstatus = OwnerRsvCommon.CALENDAR_MONDAY;
                    }
                    else if ( week == 2 )
                    {
                        nowstatus = OwnerRsvCommon.CALENDAR_TUESDAY;
                    }
                    else if ( week == 3 )
                    {
                        nowstatus = OwnerRsvCommon.CALENDAR_WEDNESDAY;
                    }
                    else if ( week == 4 )
                    {
                        nowstatus = OwnerRsvCommon.CALENDAR_THIRTHDAY;
                    }
                }
                break;
        }
        return nowstatus;
    }

}
