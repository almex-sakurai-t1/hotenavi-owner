package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;

public class LogicOwnerRsvPlanChargeCalendar implements Serializable
{

    private static final long              serialVersionUID       = 8116958157065927881L;
    private FormOwnerRsvPlanChargeCalendar frm;
    public static final int                CALENDAR_MONDAY        = 0x01;
    public static final int                CALENDAR_TUESDAY       = 0x02;
    public static final int                CALENDAR_WEDNESDAY     = 0x04;
    public static final int                CALENDAR_THIRTHDAY     = 0x08;
    public static final int                CALENDAR_FRIDAY        = 0x10;
    public static final int                CALENDAR_SATURDAY      = 0x20;
    public static final int                CALENDAR_SUNDAY        = 0x40;
    public static final int                CALENDAR_HOLIDAY       = 0x80;
    public static final int                CALENDAR_BEFOREHOLIDAY = 0x100;

    /* フォームオブジェクト */
    public FormOwnerRsvPlanChargeCalendar getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvPlanChargeCalendar frm)
    {
        this.frm = frm;
    }

    /**
     * プランリスト取得
     * 
     * @param なし
     * @return なし
     */
    public void getPlanList() throws Exception
    {
        ArrayList<Integer> planIdList = new ArrayList<Integer>();
        ArrayList<String> planNmList = new ArrayList<String>();

        planIdList = getPlanIdList();
        planNmList = getPlanNmList();

        frm.setPlanIdList( planIdList );
        frm.setPlanNmList( planNmList );
    }

    /**
     * プランIDのリスト取得
     * 
     * @param なし
     * @return プランIDのリスト
     * @throws Exception
     */
    private ArrayList<Integer> getPlanIdList() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> planIdList = new ArrayList<Integer>();

        query = query + "SELECT plan_id FROM hh_rsv_plan ";
        query = query + "WHERE id = ? AND publishing_flag=1 ";
        query = query + " AND disp_end_date >= " + DateEdit.getDate( 2 );
        query = query + " ORDER BY disp_index, plan_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();

            // ホテル全体用のプランIDをセット
            // planIdList.add( OwnerRsvCommon.PLAN_CAL_HOTELID );

            while( result.next() )
            {
                planIdList.add( result.getInt( "plan_id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getPlanIdList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(planIdList);
    }

    /**
     * プラン名のリスト取得
     * 
     * @param なし
     * @return プラン名のリスト
     * @throws Exception
     */
    private ArrayList<String> getPlanNmList() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> planNmList = new ArrayList<String>();

        query = query + "SELECT plan_name FROM hh_rsv_plan ";
        query = query + "WHERE id = ? AND publishing_flag=1 ";
        query = query + " AND disp_end_date >= " + DateEdit.getDate( 2 );
        query = query + " ORDER BY disp_index, plan_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();

            // ホテル全体用のプラン名をセット
            // planNmList.add( OwnerRsvCommon.PLAN_CAL_HOTELNM );

            while( result.next() )
            {
                planNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getPlanNmList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(planNmList);
    }

    /**
     * プラン名取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @return プラン名
     * @throws Exception
     */
    public String getPlanNm(int hotelId, int planId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String planNm = "";

        query = query + "SELECT plan_name FROM hh_rsv_plan ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                planNm = ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getPlanNm] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(planNm);
    }

    /**
     * カレンダー・料金モード報取得
     * 
     * @param hotelId ホテルID
     * @param planId 指定されたプランID
     * @param targetYM 表示対象の年月
     * @param imediaFlag アイメディアフラグ
     * @return 指定された月の1か月分のカレンダー情報
     */
    public void getCalendar(int hotelId, int planId, int targetYM, int imediaFlag) throws Exception
    {
        String year = "";
        String month = "";
        LogicOwnerRsvManageCalendar logicCalendar = new LogicOwnerRsvManageCalendar();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        ArrayList<FormOwnerRsvManageCalendar> oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
        FormOwnerRsvPlanChargeCalendarSub frmSub = new FormOwnerRsvPlanChargeCalendarSub();
        FormOwnerRsvManageCalendar frmCalMng = new FormOwnerRsvManageCalendar();
        ArrayList<Integer> chargeModeIdList = new ArrayList<Integer>();
        ArrayList<String> chargeModeNmList = new ArrayList<String>();
        ArrayList<FormOwnerRsvPlanChargeCalendarSub> newOneWeekList;
        ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> newMonthlyList = new ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>>();
        ArrayList<FormOwnerRsvPlanChargeCalendarSub> chargeFrmList = new ArrayList<FormOwnerRsvPlanChargeCalendarSub>();
        FormOwnerRsvPlanChargeCalendarSub dayChargeFrmSub;
        ReserveCommon rsvCmm = new ReserveCommon();
        String reserveCharge = "";

        // カレンダー情報取得
        monthlyList = logicCalendar.getCalendarData( hotelId, planId, targetYM );

        // 日別料金情報取得
        chargeFrmList = getDayCharge( hotelId, planId, targetYM );

        // 料金モードID、料金モード名取得
        chargeModeIdList = getChargeModeIdList( hotelId, planId, imediaFlag );
        chargeModeNmList = getChargeModeNmList( hotelId, planId, imediaFlag );

        for( int i = 0 ; i < monthlyList.size() ; i++ )
        {
            // 1週間分のリスト取得
            oneWeekList = monthlyList.get( i );

            newOneWeekList = new ArrayList<FormOwnerRsvPlanChargeCalendarSub>();

            // カレンダー情報の1日分のデータ取得
            for( int j = 0 ; j < oneWeekList.size() ; j++ )
            {
                frmSub = new FormOwnerRsvPlanChargeCalendarSub();
                frmCalMng = oneWeekList.get( j );
                // 2013/06/04　基本的な大人2人、子供0人で料金を計算してセットする
                reserveCharge = "";
                reserveCharge = getRsvPlanChargeSpecified( hotelId, planId, 2, 0, frmCalMng.getCalDate() );

                // エラーメッセージ
                frmSub.setErrMsg( frmCalMng.getErrMsg() );

                // 対象年月取得
                year = Integer.toString( targetYM ).substring( 0, 4 );
                month = Integer.toString( targetYM ).substring( 4 );
                frmSub.setCurrentYear( year );
                frmSub.setCurrentMonth( month );

                // 日別料金情報データ取得
                for( int k = 0 ; k < chargeFrmList.size() ; k++ )
                {
                    dayChargeFrmSub = chargeFrmList.get( k );
                    frmSub.setCalDate( 0 );

                    if ( dayChargeFrmSub.getErrMsg().trim().length() != 0 )
                    {
                        // エラーあり時
                        if ( frmSub.getErrMsg().trim().length() == 0 )
                        {
                            frmSub.setErrMsg( dayChargeFrmSub.getErrMsg() );
                        }
                        frmSub.setChargeModeId( frmCalMng.getChargeModeId() );
                        frmSub.setChargeModeIdList( chargeModeIdList );
                        frmSub.setChargeModeNmList( chargeModeNmList );
                        newOneWeekList.add( frmSub );
                        newMonthlyList.add( newOneWeekList );
                        frm.setMonthlyList( newMonthlyList );
                        return;
                    }
                    if ( frmCalMng.getCalDate() == dayChargeFrmSub.getCalDate() )
                    {
                        frmSub.setCalDate( frmCalMng.getCalDate() );
                        break;
                    }
                }

                frmSub.setHotelId( hotelId );
                frmSub.setPlanId( planId );
                frmSub.setDate( frmCalMng.getDate() );
                frmSub.setChargeModeId( frmCalMng.getChargeModeId() );
                frmSub.setChargeModeIdList( chargeModeIdList );
                frmSub.setChargeModeNmList( chargeModeNmList );
                frmSub.setWeekId( frmCalMng.getWeekId() );
                frmSub.setHolidayKind( frmCalMng.getHolidayKind() );
                frmSub.setCurrentYear( frmCalMng.getCurrentYear() );
                frmSub.setCurrentMonth( frmCalMng.getCurrentMonth() );
                frmSub.setCurrentFlg( frmCalMng.getCurrentFlg() );
                frmSub.setReserveChargeFormat( reserveCharge );
                newOneWeekList.add( frmSub );
            }
            newMonthlyList.add( newOneWeekList );
        }
        frm.setMonthlyList( newMonthlyList );
    }

    /**
     * 日別料金情報取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param targetYM 対象年月
     * @return 対象年月の料金情報リスト
     * @throws Exception
     */
    public ArrayList<FormOwnerRsvPlanChargeCalendarSub> getDayCharge(int hotelId, int planId, int targetYM) throws Exception
    {
        String query = "";
        int count = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        FormOwnerRsvPlanChargeCalendarSub frmSub;
        ArrayList<FormOwnerRsvPlanChargeCalendarSub> frmSubList = new ArrayList<FormOwnerRsvPlanChargeCalendarSub>();

        query = query + "SELECT cal_date, charge_mode_id FROM hh_rsv_day_charge ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND cal_date LIKE concat(?, '%') ";
        query = query + "ORDER BY cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, targetYM );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frmSub = new FormOwnerRsvPlanChargeCalendarSub();
                frmSub.setCalDate( result.getInt( "cal_date" ) );
                frmSub.setChargeModeId( result.getInt( "charge_mode_id" ) );
                frmSubList.add( frmSub );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frmSub = new FormOwnerRsvPlanChargeCalendarSub();
                frmSub.setErrMsg( Message.getMessage( "erro.30001", "カレンダー情報" ) );
                frmSubList.add( frmSub );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getDayCharge] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(frmSubList);
    }

    /**
     * 料金モードIDのリスト取得
     * 
     * @param hotelId ホテルID
     * @param planId ID
     * @param imediaFlag アイメディアフラグ
     * @return 料金モードIDのリスト
     * @throws Exception
     */
    private ArrayList<Integer> getChargeModeIdList(int hotelId, int planId, int imediaFlag) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> chargeModeIdList = new ArrayList<Integer>();

        query = query + "SELECT cm.charge_mode_id ";
        query = query + "FROM hh_rsv_charge_mode cm ";
        query = query + "  INNER JOIN hh_rsv_plan_charge pc ON cm.charge_mode_id = pc.charge_mode_id ";
        query = query + "WHERE cm.id = ? ";
        query = query + "  AND pc.plan_id = ? ";
        query = query + "  AND (cm.charge_mode_name is not null and length(rtrim(ltrim(cm.charge_mode_name))) > 0) ";
        query = query + "ORDER BY cm.charge_mode_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            // ホテルでも販売停止をさせるために、コメント(2013/06/07)
            // if ( imediaFlag == 1 )
            // {
            chargeModeIdList.add( 0 );
            // }
            while( result.next() )
            {
                chargeModeIdList.add( result.getInt( "charge_mode_id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getChargeModeIdList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(chargeModeIdList);
    }

    /**
     * 料金モード名のリスト取得
     * 
     * @param hotelId ホテルID
     * @param planId ID
     * @param imediaFlag アイメディアフラグ
     * @return 料金モード名のリスト
     * @throws Exception
     */
    private ArrayList<String> getChargeModeNmList(int hotelId, int planId, int imediaFlag) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> chargeModeNmList = new ArrayList<String>();

        query = query + "SELECT cm.charge_mode_name ";
        query = query + "FROM hh_rsv_charge_mode cm ";
        query = query + "  INNER JOIN hh_rsv_plan_charge pc ON cm.charge_mode_id = pc.charge_mode_id ";
        query = query + "WHERE cm.id = ? ";
        query = query + "  AND pc.plan_id = ? ";
        query = query + "  AND (cm.charge_mode_name is not null and length(rtrim(ltrim(cm.charge_mode_name))) > 0) ";
        query = query + "ORDER BY cm.charge_mode_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            // ホテルでも販売停止をさせるために、コメント(2013/06/07)
            // if ( imediaFlag == 1 )
            // {
            chargeModeNmList.add( "販売停止" );
            // }
            while( result.next() )
            {
                chargeModeNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getChargeModeNmList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(chargeModeNmList);
    }

    /**
     * カレンダー更新処理
     * 
     * @param chargeModeIdList 画面で設定されている日別料金モードIDリスト
     * @param targetYM 対象年月
     * @return なし
     * @throws Exception
     */
    public void registPlanCalendar(ArrayList<Integer> chargeModeIdList, int targetYM) throws Exception
    {

        ArrayList<Integer> targetDateList = new ArrayList<Integer>();

        // 指定月の日付一覧取得
        targetDateList = getTargetDate( frm.getSelPlanId(), targetYM );

        // 日別料金モードの更新
        for( int i = 0 ; i < targetDateList.size() ; i++ )
        {
            updDayCharge( chargeModeIdList.get( i ), targetDateList.get( i ) );
        }
    }

    /**
     * ホテル全体のカレンダー更新処理
     * 
     * @param chargeModeIdList 画面で設定されている日別料金モードIDリスト
     * @param targetYM 対象年月
     * @return なし
     * @throws Exception
     */
    public void registHotelCalendar(ArrayList<Integer> chargeModeIdList, int targetYM) throws Exception
    {
        ArrayList<Integer> targetDateList = new ArrayList<Integer>();

        // 指定月の日付一覧取得
        targetDateList = getHotelTargetDate( targetYM );

        // 日別料金モードの更新
        for( int i = 0 ; i < targetDateList.size() ; i++ )
        {
            updHotelCalendarChargeMode( chargeModeIdList.get( i ), targetDateList.get( i ) );
        }
    }

    /**
     * 対象年月の日別料金データの日付取得
     * 
     * @param planId プランID
     * @param targetYM 対象年月
     * @return 日付のリスト
     * @throws Exception
     */
    private ArrayList<Integer> getTargetDate(int planId, int targetYM) throws Exception
    {

        ArrayList<Integer> targetDateList = new ArrayList<Integer>();

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT cal_date FROM hh_rsv_day_charge ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id  = ? ";
        query = query + "  AND cal_date LIKE concat(?, '%') ";
        query = query + "ORDER BY cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, targetYM );
            result = prestate.executeQuery();

            while( result.next() )
            {
                targetDateList.add( result.getInt( "cal_date" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getTargetDate] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(targetDateList);
    }

    /**
     * 対象年月のホテルカレンダー情報取得
     * 
     * @param targetYM 対象年月
     * @return 日付のリスト
     * @throws Exception
     */
    private ArrayList<Integer> getHotelTargetDate(int targetYM) throws Exception
    {

        ArrayList<Integer> targetDateList = new ArrayList<Integer>();

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT cal_date FROM hh_rsv_hotel_calendar ";
        query = query + "WHERE id = ? ";
        query = query + "  AND cal_date LIKE concat(?, '%') ";
        query = query + "ORDER BY cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, targetYM );
            result = prestate.executeQuery();

            while( result.next() )
            {
                targetDateList.add( result.getInt( "cal_date" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getHotelTargetDate] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(targetDateList);
    }

    /**
     * 日別料金モード更新
     * 
     * @param 料金モード
     * @param 対象日付
     * @return なし
     * @throws Exception
     */
    private boolean updDayCharge(int chargeModeId, int targetDate) throws Exception
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_day_charge SET ";
        query = query + " charge_mode_id = ?,";
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ? ";
        query = query + "   AND plan_id = ? ";
        query = query + "   AND cal_date = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, chargeModeId );
            prestate.setString( 2, frm.getOwnerHotelID() );
            prestate.setInt( 3, frm.getUserId() );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 5, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 6, frm.getSelHotelId() );
            prestate.setInt( 7, frm.getSelPlanId() );
            prestate.setInt( 8, targetDate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.updDayCharge] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvPlanChargeCalendar.updDayCharge] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ホテルカレンダー更新
     * 
     * @param 料金モード
     * @param 対象日付
     * @return なし
     * @throws Exception
     */
    private boolean updHotelCalendarChargeMode(int chargeModeId, int targetDate) throws Exception
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_hotel_calendar SET ";
        if ( chargeModeId > 0 )
        {
            query = query + " charge_mode_id = ?,";
            query = query + " sales_flag = 1, ";
        }
        else
        {
            query = query + " sales_flag = 0, ";
        }
        query = query + " hotel_id = ?,";
        query = query + " user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ? ";
        query = query + "   AND cal_date = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            if ( chargeModeId > 0 )
            {
                prestate.setInt( i++, chargeModeId );
            }
            prestate.setString( i++, frm.getOwnerHotelID() );
            prestate.setInt( i++, frm.getUserId() );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( i++, frm.getSelHotelId() );
            prestate.setInt( i++, targetDate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
            if ( ret != false )
            {
                Logging.error( "[LogicOwnerRsvPlanChargeCalendar.updHotelCalendarChargeMode] ret=" + ret );
                updRoomRemainder( frm.getSelHotelId(), targetDate, chargeModeId );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.updHotelCalendarChargeMode] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvPlanChargeCalendar.updHotelCalendarChargeMode] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ホテルカレンダー・料金モード報取得
     * 
     * @param hotelId ホテルID
     * @param targetYM 表示対象の年月
     * @param imediaFlag アイメディアフラグ
     * @return 指定された月の1か月分のカレンダー情報
     */
    public void getHotelCalendar(int hotelId, int targetYM, int imediaFlag) throws Exception
    {
        String year = "";
        String month = "";
        FormOwnerRsvPlanChargeCalendarSub frmSub = new FormOwnerRsvPlanChargeCalendarSub();
        FormOwnerRsvManageCalendar frmCalMng = new FormOwnerRsvManageCalendar();
        ArrayList<Integer> chargeModeIdList = new ArrayList<Integer>();
        ArrayList<String> chargeModeNmList = new ArrayList<String>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        ArrayList<FormOwnerRsvManageCalendar> oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> newMonthlyList = new ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>>();
        ArrayList<FormOwnerRsvPlanChargeCalendarSub> newOneWeekList;

        // 1か月分のホテルカレンダーリスト取得
        monthlyList = getHotelCalMonthliList( hotelId, targetYM );

        // 料金モードID、料金モード名取得
        chargeModeIdList = getHotelChargeModeIdList( hotelId, imediaFlag );
        chargeModeNmList = getHotelChargeModeNmList( hotelId, imediaFlag );

        for( int i = 0 ; i < monthlyList.size() ; i++ )
        {
            // 1週間分のリスト取得
            oneWeekList = monthlyList.get( i );
            newOneWeekList = new ArrayList<FormOwnerRsvPlanChargeCalendarSub>();

            // カレンダー情報の1日分のデータ取得
            for( int j = 0 ; j < oneWeekList.size() ; j++ )
            {
                frmSub = new FormOwnerRsvPlanChargeCalendarSub();
                frmCalMng = oneWeekList.get( j );

                // エラーメッセージ
                frmSub.setErrMsg( frmCalMng.getErrMsg() );

                // 対象年月取得
                year = Integer.toString( targetYM ).substring( 0, 4 );
                month = Integer.toString( targetYM ).substring( 4 );
                frmSub.setCurrentYear( year );
                frmSub.setCurrentMonth( month );

                frmSub.setHotelId( hotelId );
                frmSub.setPlanId( 0 );
                frmSub.setDate( frmCalMng.getDate() );
                frmSub.setChargeModeId( frmCalMng.getChargeModeId() );
                frmSub.setChargeModeIdList( chargeModeIdList );
                frmSub.setChargeModeNmList( chargeModeNmList );
                frmSub.setWeekId( frmCalMng.getWeekId() );
                frmSub.setHolidayKind( frmCalMng.getHolidayKind() );
                frmSub.setCurrentYear( frmCalMng.getCurrentYear() );
                frmSub.setCurrentMonth( frmCalMng.getCurrentMonth() );
                frmSub.setCurrentFlg( frmCalMng.getCurrentFlg() );
                frmSub.setCalDate( frmCalMng.getCalDate() );
                frmSub.setSalesFlag( frmCalMng.getSalesFlag() );
                newOneWeekList.add( frmSub );
            }
            newMonthlyList.add( newOneWeekList );
        }
        frm.setMonthlyList( newMonthlyList );
    }

    /**
     * ホテルカレンダー情報取得
     * 
     * @param frm FormOwnerRsvManageCalendarオブジェクト
     * @param hotelId ホテルID
     * @param targetYM 対象年月
     * @return ホテルカレンダー情報のArrayList
     */
    private ArrayList<FormOwnerRsvManageCalendar> getRsvHotelCalendar(FormOwnerRsvManageCalendar frm, int hotelId, int targetYM) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        String year = "";
        String month = "";
        String day = "";
        int today = 0;
        int count = 0;

        query = query + "SELECT ca.id, ca.cal_date, ca.charge_mode_id, ca.week, ca.holiday_kind, ca.sales_flag ";
        query = query + "FROM hh_rsv_hotel_calendar ca ";
        query = query + "WHERE ca.id = ? ";
        query = query + "AND ca.cal_date BETWEEN ? AND ? ";
        query = query + "ORDER BY ca.cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, Integer.toString( targetYM ) + "01" );
            prestate.setString( 3, Integer.toString( targetYM ) + "31" );
            result = prestate.executeQuery();

            // 当日取得
            Calendar calendar = Calendar.getInstance();
            year = Integer.toString( calendar.get( Calendar.YEAR ) );
            month = String.format( "%1$02d", calendar.get( Calendar.MONTH ) + 1 );
            day = String.format( "%1$02d", calendar.get( Calendar.DATE ) );
            today = Integer.parseInt( year + month + day );

            while( result.next() )
            {
                frm = new FormOwnerRsvManageCalendar();
                frm.setHotelId( result.getInt( "id" ) );
                frm.setCalDate( result.getInt( "cal_date" ) );
                frm.setDate( Integer.parseInt( result.getString( "cal_date" ).substring( 6 ) ) );
                frm.setChargeModeId( result.getInt( "charge_mode_id" ) );
                frm.setWeekId( result.getInt( "week" ) );
                frm.setHolidayKind( result.getInt( "holiday_kind" ) );
                frm.setSalesFlag( result.getInt( "sales_flag" ) );
                // 当日フラグのセット
                frm.setCurrentFlg( 0 );
                if ( today == (result.getInt( "cal_date" )) )
                {
                    // 当日
                    frm.setCurrentFlg( 1 );
                }
                frmList.add( frm );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm = new FormOwnerRsvManageCalendar();
                frm.setErrMsg( Message.getMessage( "erro.30001", "カレンダー情報" ) );
                frmList.add( frm );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.getRsvHotelCalendar] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(frmList);
    }

    /**
     * ホテルカレンダー1ヶ月分情報取得
     * 
     * @param hotelId ホテルID
     * @param targetYM 表示対象の年月
     * @return 指定された月の1か月分のカレンダー情報
     */
    private ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getHotelCalMonthliList(int hotelId, int targetYM) throws Exception
    {
        String year = "";
        String month = "";
        int weekId = 0;
        FormOwnerRsvManageCalendar frm;
        FormOwnerRsvManageCalendar frmEmpty;
        FormOwnerRsvManageCalendar frmMngCalendar = new FormOwnerRsvManageCalendar();
        ArrayList<FormOwnerRsvManageCalendar> hotelCalendarList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        ArrayList<FormOwnerRsvManageCalendar> oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();

        // ホテルカレンダー情報取得
        hotelCalendarList = getRsvHotelCalendar( frmMngCalendar, hotelId, targetYM );

        // 1か月分のカレンダーリスト生成ｓ
        for( int i = 0 ; i < hotelCalendarList.size() ; i++ )
        {
            frm = hotelCalendarList.get( i );
            weekId = frm.getWeekId();
            // 1日より前の日付分の空データ作成
            if ( (i == 0) && (weekId != 0) )
            {
                for( int j = 0 ; j < weekId ; j++ )
                {
                    frmEmpty = new FormOwnerRsvManageCalendar();
                    oneWeekList.add( frmEmpty );
                }
            }

            // 対象年月取得
            year = Integer.toString( targetYM ).substring( 0, 4 );
            month = Integer.toString( targetYM ).substring( 4 );
            frm.setCurrentYear( year );
            frm.setCurrentMonth( month );

            oneWeekList.add( frm );

            if ( frm.getWeekId() == 6 )
            {
                // 土曜の場合は、新しいリストを作成
                monthlyList.add( oneWeekList );
                oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
            }
        }
        // 最終日日以降の空データ作成
        monthlyList.add( oneWeekList );

        return(monthlyList);
    }

    /**
     * ホテル全体で使用する料金モードIDのリスト取得
     * 
     * @param hotelId ホテルID
     * @param imediaFlag アイメディアフラグ
     * @return 料金モードIDのリスト
     * @throws Exception
     */
    private ArrayList<Integer> getHotelChargeModeIdList(int hotelId, int imediaFlag) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> chargeModeIdList = new ArrayList<Integer>();

        query = query + "SELECT cm.charge_mode_id FROM hh_rsv_charge_mode cm ";
        query = query + "WHERE cm.id = ? ";
        query = query + "  AND (cm.charge_mode_name is not null and length(rtrim(ltrim(cm.charge_mode_name))) > 0) ";
        query = query + "ORDER BY cm.charge_mode_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( imediaFlag == 1 )
            {
                chargeModeIdList.add( 0 );
            }
            while( result.next() )
            {
                chargeModeIdList.add( result.getInt( "charge_mode_id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getHotelChargeModeIdList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(chargeModeIdList);
    }

    /**
     * ホテル全体で使用する料金モード名のリスト取得
     * 
     * @param hotelId ホテルID
     * @param imediaFlag アイメディアフラグ
     * @return 料金モード名のリスト
     * @throws Exception
     */
    private ArrayList<String> getHotelChargeModeNmList(int hotelId, int imediaFlag) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> chargeModeNmList = new ArrayList<String>();

        query = query + "SELECT cm.charge_mode_name FROM hh_rsv_charge_mode cm ";
        query = query + "WHERE cm.id = ? ";
        query = query + "  AND (cm.charge_mode_name is not null and length(rtrim(ltrim(cm.charge_mode_name))) > 0) ";
        query = query + "ORDER BY cm.charge_mode_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( imediaFlag == 1 )
            {
                chargeModeNmList.add( "販売停止" );
            }
            while( result.next() )
            {
                chargeModeNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.getHotelChargeModeNmList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(chargeModeNmList);
    }

    /**
     * 部屋残数データ更新
     * 
     * @param hotelId ホテルID
     * @param calDate 対象日付
     * @param chargeModeId 料金モード
     * @return true:処理成功、false:処理失敗
     */
    public boolean updRoomRemainder(int hotelId, int calDate, int chargeModeId) throws Exception
    {
        final int SALES_START = 1;
        final int SALES_STOP = 3;
        int result;
        int status;
        int condStatus;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_room_remainder SET ";
        query = query + " status = ? ";
        query = query + " WHERE id = ? ";
        query = query + "   AND cal_date = ? ";
        query = query + "   AND status = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            if ( chargeModeId > 0 )
            {
                status = SALES_START;
                condStatus = SALES_STOP;
            }
            else
            {
                status = SALES_STOP;
                condStatus = SALES_START;
            }
            // 更新対象の値をセットする
            prestate.setInt( 1, status );
            prestate.setInt( 2, hotelId );
            prestate.setInt( 3, calDate );
            prestate.setInt( 4, condStatus );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeCalendar.updRoomRemainder] Exception=" + e.toString() );
            ret = false;
            throw new Exception( "[LogicOwnerRsvPlanChargeCalendar.updRoomRemainder] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * プランの料金を取得（指定日）
     * 
     * @param id ホテルID
     * @param planId プランID
     * @param adult 大人の人数
     * @param child 子供の人数
     * @param date 指定日
     * @return
     */
    public String getRsvPlanChargeSpecified(int id, int planId, int adult, int child, int date)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int maxAdultNum = 0;
        int minAdultNum = 0;

        int price = 0;
        int searchAdult = 0;
        int searchChild = 0;
        NumberFormat objNum = NumberFormat.getCurrencyInstance();
        String returnPrice = "";

        if ( adult < 0 || child < 0 )
        {
            return(returnPrice);
        }

        // 料金、チェックイン、チェックアウトを取得するSQL
        query = "SELECT HRPC.*,HRP.max_num_adult, HRP.min_num_adult " +
                " FROM hh_rsv_plan_charge HRPC " +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRPC.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1 )" +
                "   INNER JOIN hh_rsv_plan HRP ON HRPC.id = HRP.id AND HRPC.plan_id = HRP.plan_id " + // AND HRP.sales_flag = 1" + 販売前のも確認できるようにするためにコメント
                "   INNER JOIN hh_rsv_day_charge HRDC ON HRPC.id = HRDC.id AND HRPC.plan_id = HRDC.plan_id AND HRPC.charge_mode_id = HRDC.charge_mode_id" +
                " WHERE HRPC.id = ?" +
                " AND HRPC.plan_id = ?" +
                " AND HRDC.cal_date = ?" +
                " ORDER BY HRP.disp_index, HRPC.plan_id, HRPC.charge_mode_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, date );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    price = 0;

                    searchAdult = adult;
                    searchChild = child;

                    if ( adult == 0 )
                    {
                        // 人数指定なしの時
                        maxAdultNum = result.getInt( "max_num_adult" );
                        minAdultNum = result.getInt( "min_num_adult" );
                        if ( maxAdultNum == 1 )
                        {
                            // 最大人数が1の時は大人1人の金額を表示する
                            price = result.getInt( "adult_one_charge" );
                            searchAdult = maxAdultNum;
                        }
                        else if ( minAdultNum > 2 )
                        {
                            // 最低人数が2以上の時は大人最低人数分の金額で表示
                            price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (minAdultNum - 2));
                            searchAdult = minAdultNum;
                        }
                        else
                        {
                            // 基本的には2人用の金額を表示
                            price = result.getInt( "adult_two_charge" );
                            searchAdult = 2;
                        }
                    }
                    else if ( adult == 1 )
                    {
                        // 大人1人料金 + 子供の追加料金 X 子供の人数
                        price = result.getInt( "adult_one_charge" ) + (result.getInt( "child_add_charge" ) * child);
                    }
                    else if ( adult == 2 )
                    {
                        // 大人2人料金 + 子供の追加料金 X 子供の人数
                        price = result.getInt( "adult_two_charge" ) + (result.getInt( "child_add_charge" ) * child);
                    }
                    else if ( adult > 2 )
                    {
                        // 大人2人料金 + 大人の追加料金 * (大人の人数-2) + 子供の追加料金 X 子供の人数
                        price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (adult - 2)) + (result.getInt( "child_add_charge" ) * child);
                    }
                    // 大人の人数が０人の時
                    else
                    {
                        // 子供の追加料金 X 子供の人数
                        price = (result.getInt( "child_add_charge" ) * child);
                    }
                    returnPrice = objNum.format( price );

                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[LogicOwnerRsvPlanChargeCalendar.getRsvPlanChargeSpecified()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return returnPrice;

    }
}
