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
import jp.happyhotel.data.DataRsvPlan;

/**
 * カレンダービジネスロジック
 */
public class LogicOwnerRsvManageCalendar implements Serializable
{

    private static final long   serialVersionUID    = -2912059728003741855L;
    private static final String RSV_ON              = "受付中";
    private static final String RSV_OFF             = "受付終了";
    private static final String RSV_STOP            = "停止中";
    private static final String RSV_ON_MARK         = "○";
    private static final String RSV_OFF_MARK        = "×";
    private static final String RSV_IMPOSSIBLE_MARK = "－";

    /**
     * カレンダー・残室数情報取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param targetYM 対象年月
     * @return 指定された月の1か月分のカレンダー情報
     */
    public ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getCalendarData(int hotelId, int planId, int targetYM) throws Exception
    {
        int cnt = 0;
        int weekId = 0;
        String year = "";
        String month = "";
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        FormOwnerRsvManageCalendar frm = new FormOwnerRsvManageCalendar();
        FormOwnerRsvManageCalendar frmEmpty;

        // カレンダー情報取得
        frmList = getCalendar( frm, hotelId, planId, targetYM );

        // 部屋残数情報取得
        for( int i = 0 ; i < frmList.size() ; i++ )
        {
            frm = frmList.get( i );
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

            if ( planId == -1 )
            {
                // プランID未指定の場合
                // 全部屋残数取得
                cnt = getRoomRemainder( hotelId, planId, 1, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // 空き部屋残数
                cnt = 0;
                cnt = getRoomRemainder( hotelId, planId, 2, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );

                if ( (frm.getRsvJotaiFlg() == 1) && (frm.getSalesFlag() == 0) )
                {
                    frm.setRsvJotai( RSV_STOP );
                }
            }
            else
            {
                // プランID指定の場合
                // 全部屋残数取得
                cnt = getRoomRemainderPlan( hotelId, planId, 1, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // 空き部屋残数
                cnt = 0;
                cnt = getRoomRemainderPlan( hotelId, planId, 2, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );
                if ( frm.getRsvJotaiMark().equals( RSV_ON_MARK ) )
                {
                    if ( cnt == 0 )
                    {
                        frm.setRsvJotai( RSV_OFF );
                        frm.setRsvJotaiMark( RSV_OFF_MARK );
                    }
                    else
                    {
                        // 予約上限数確認
                        if ( checkMaxRsvRommCnt( hotelId, planId, frm.getCalDate() ) == false )
                        {
                            frm.setRsvJotai( RSV_OFF );
                            frm.setRsvJotaiMark( RSV_OFF_MARK );
                        }
                    }
                }
            }

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

    //
    /**
     * カレンダー・残室数情報取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param seq 管理番号
     * @param targetYM 対象年月
     * @return 指定された月の1か月分のカレンダー情報
     */
    public ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getCalendarData(int hotelId, int planId, int seq, int targetYM) throws Exception
    {
        int cnt = 0;
        int weekId = 0;
        String year = "";
        String month = "";
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        FormOwnerRsvManageCalendar frm = new FormOwnerRsvManageCalendar();
        FormOwnerRsvManageCalendar frmEmpty;

        // カレンダー情報取得
        frmList = getCalendar( frm, hotelId, planId, targetYM );

        // 部屋残数情報取得
        for( int i = 0 ; i < frmList.size() ; i++ )
        {
            frm = frmList.get( i );
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

            if ( planId == -1 )
            {
                // プランID未指定の場合
                // 全部屋残数取得
                cnt = getRoomRemainder( hotelId, planId, 1, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // 予約済み部屋数
                cnt = 0;
                cnt = getRoomRemainder( hotelId, planId, 2, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );
            }
            else
            {
                // プランID指定の場合
                // 全部屋残数取得
                cnt = getRoomRemainderPlan( hotelId, planId, 1, seq, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // 予約済み部屋数
                cnt = 0;
                cnt = getRoomRemainderPlan( hotelId, planId, 2, seq, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );
                if ( frm.getRsvJotaiMark().equals( RSV_ON_MARK ) )
                {
                    if ( cnt == 0 )
                    {
                        frm.setRsvJotai( RSV_OFF );
                        frm.setRsvJotaiMark( RSV_OFF_MARK );
                    }
                    else
                    {
                        // 予約上限数確認
                        if ( checkMaxRsvRommCnt( hotelId, planId, frm.getCalDate() ) == false )
                        {
                            frm.setRsvJotai( RSV_OFF );
                            frm.setRsvJotaiMark( RSV_OFF_MARK );
                        }
                    }
                }
            }

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
     * カレンダー情報取得
     * 
     * @param frm FormOwnerRsvManageCalendarオブジェクト
     * @param hotelId ホテルID
     * @param planId プランID(未指定の場合は-1を指定する)
     * @param targetYM 検索対象年月(YYYYMM)
     * @return FormOwnerRsvManageCalendarのArrayList
     * @throws Exception
     */
    private ArrayList<FormOwnerRsvManageCalendar> getCalendar(FormOwnerRsvManageCalendar frm, int hotelId, int planId, int targetYM) throws Exception
    {
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();

        if ( planId != -1 )
        {
            frmList = getPlanIdCalendar( frm, hotelId, planId, targetYM );
        }
        else
        {
            // プランID未指定時
            frmList = getNoPlanIdCalendar( frm, hotelId, targetYM );
        }

        return(frmList);
    }

    /**
     * プランID指定時のカレンダー情報取得
     * 
     * @param frm FormOwnerRsvManageCalendarオブジェクト
     * @param hotelId ホテルID
     * @param planId プランID
     * @param targetYM 検索対象年月(YYYYMM)
     * @return FormOwnerRsvManageCalendarのArrayList
     * @throws Exception
     */
    private ArrayList<FormOwnerRsvManageCalendar> getPlanIdCalendar(FormOwnerRsvManageCalendar frm, int hotelId, int planId, int targetYM) throws Exception
    {
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> hotelCalendarList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> dayChargeList = new ArrayList<FormOwnerRsvManageCalendar>();
        FormOwnerRsvManageCalendar hotelCalendarFrm;
        FormOwnerRsvManageCalendar dayChargeFrm;
        FormOwnerRsvManageCalendar newFrm;
        boolean setFlg = false;

        // ホテルカレンダー情報取得
        hotelCalendarList = getHotelCalendar( frm, hotelId, targetYM );

        // 日別料金マスタ取得
        dayChargeList = getDayCharge( frm, hotelId, planId, targetYM );

        // ホテルカレンダーと日別料金マスタ情報をマージ
        for( int i = 0 ; i < hotelCalendarList.size() ; i++ )
        {
            hotelCalendarFrm = hotelCalendarList.get( i );
            setFlg = false;

            for( int j = 0 ; j < dayChargeList.size() ; j++ )
            {
                dayChargeFrm = dayChargeList.get( j );

                if ( hotelCalendarFrm.getCalDate() == dayChargeFrm.getCalDate() )
                {
                    // 日別料金情報のデータで上書き
                    newFrm = new FormOwnerRsvManageCalendar();
                    newFrm.setHotelId( hotelId );
                    newFrm.setCalDate( hotelCalendarFrm.getCalDate() );
                    newFrm.setDate( hotelCalendarFrm.getDate() );
                    newFrm.setChargeModeId( dayChargeFrm.getChargeModeId() );
                    newFrm.setChargeModeNm( dayChargeFrm.getChargeModeNm() );
                    newFrm.setWeekId( hotelCalendarFrm.getWeekId() );
                    newFrm.setHolidayKind( hotelCalendarFrm.getHolidayKind() );
                    newFrm.setSalesFlag( hotelCalendarFrm.getSalesFlag() );
                    newFrm.setPlanId( planId );
                    newFrm.setAdultTwoCharge( dayChargeFrm.getAdultTwoCharge() );
                    newFrm.setAdultTwoChargeFormat( dayChargeFrm.getAdultTwoChargeFormat() );
                    newFrm.setCurrentFlg( hotelCalendarFrm.getCurrentFlg() );
                    newFrm.setRsvJotai( hotelCalendarFrm.getRsvJotai() );
                    newFrm.setRsvJotaiMark( hotelCalendarFrm.getRsvJotaiMark() );
                    newFrm.setRsvJotaiFlg( hotelCalendarFrm.getRsvJotaiFlg() );
                    newFrm.setErrMsg( hotelCalendarFrm.getErrMsg() );

                    frmList.add( newFrm );
                    setFlg = true;
                    break;
                }
            }

            if ( setFlg == false )
            {
                // カレンダー情報を正とする
                newFrm = new FormOwnerRsvManageCalendar();
                newFrm.setHotelId( hotelId );
                newFrm.setCalDate( hotelCalendarFrm.getCalDate() );
                newFrm.setDate( hotelCalendarFrm.getDate() );
                newFrm.setChargeModeId( hotelCalendarFrm.getChargeModeId() );
                newFrm.setChargeModeNm( hotelCalendarFrm.getChargeModeNm() );
                newFrm.setWeekId( hotelCalendarFrm.getWeekId() );
                newFrm.setHolidayKind( hotelCalendarFrm.getHolidayKind() );
                newFrm.setSalesFlag( hotelCalendarFrm.getSalesFlag() );
                newFrm.setPlanId( planId );
                newFrm.setAdultTwoCharge( 0 );
                newFrm.setAdultTwoChargeFormat( "0" );
                newFrm.setCurrentFlg( hotelCalendarFrm.getCurrentFlg() );
                newFrm.setRsvJotai( hotelCalendarFrm.getRsvJotai() );
                newFrm.setRsvJotaiMark( hotelCalendarFrm.getRsvJotaiMark() );
                newFrm.setRsvJotaiFlg( hotelCalendarFrm.getRsvJotaiFlg() );
                newFrm.setErrMsg( hotelCalendarFrm.getErrMsg() );

                frmList.add( newFrm );
            }

        }

        return(frmList);
    }

    /**
     * 日別料金マスタ情報取得
     * 
     * @param frm FormOwnerRsvManageCalendarオブジェクト
     * @param hotelId ホテルID
     * @param planId プランID
     * @param targetYM 対象年月
     * @return 日別料金マスタのArrayList
     */
    private ArrayList<FormOwnerRsvManageCalendar> getDayCharge(FormOwnerRsvManageCalendar frm, int hotelId, int planId, int targetYM) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        NumberFormat objNum = NumberFormat.getCurrencyInstance();

        query = query + "SELECT dc.id, dc.plan_id, dc.cal_date, dc.charge_mode_id, cm.charge_mode_name, ";
        query = query + "CASE pc.adult_two_charge IS null WHEN 1 THEN 0 ELSE pc.adult_two_charge END adult_two_charge ";
        query = query + "FROM hh_rsv_day_charge dc ";
        query = query + "    LEFT JOIN hh_rsv_charge_mode cm ON dc.id = cm.id AND dc.charge_mode_id = cm.charge_mode_id ";
        query = query + "    LEFT JOIN hh_rsv_plan_charge pc ON dc.id = pc.id AND dc.plan_id = pc.plan_id AND dc.charge_mode_id = pc.charge_mode_id ";
        query = query + "WHERE dc.id = ? ";
        query = query + "  AND dc.cal_date BETWEEN ? AND ? ";
        query = query + "  AND dc.plan_id = ? ";
        query = query + "ORDER BY dc.cal_date";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, Integer.toString( targetYM ) + "01" );
            prestate.setString( 3, Integer.toString( targetYM ) + "31" );
            prestate.setInt( 4, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frm = new FormOwnerRsvManageCalendar();
                frm.setHotelId( result.getInt( "id" ) );
                frm.setPlanId( result.getInt( "plan_id" ) );
                frm.setCalDate( result.getInt( "cal_date" ) );
                frm.setDate( Integer.parseInt( result.getString( "cal_date" ).substring( 6 ) ) );
                frm.setChargeModeId( result.getInt( "charge_mode_id" ) );
                frm.setChargeModeNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) ) );
                frm.setAdultTwoCharge( result.getInt( "adult_two_charge" ) );
                if ( result.getInt( "adult_two_charge" ) > 0 )
                {
                    frm.setAdultTwoChargeFormat( objNum.format( result.getInt( "adult_two_charge" ) ) );
                }
                else
                {
                    frm.setAdultTwoChargeFormat( "" );
                }
                frmList.add( frm );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.getDayCharge] Exception=" + e.toString() );
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
     * ホテルカレンダー情報取得
     * 
     * @param frm FormOwnerRsvManageCalendarオブジェクト
     * @param hotelId ホテルID
     * @param targetYM 対象年月
     * @return ホテルカレンダー情報のArrayList
     */
    private ArrayList<FormOwnerRsvManageCalendar> getHotelCalendar(FormOwnerRsvManageCalendar frm, int hotelId, int targetYM) throws Exception
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

        query = query + "SELECT ca.id, ca.cal_date, ca.charge_mode_id, ca.week, ca.holiday_kind, ca.sales_flag, cm.charge_mode_name ";
        query = query + "FROM hh_rsv_hotel_calendar ca ";
        query = query + "    LEFT JOIN hh_rsv_charge_mode cm ON ca.id = cm.id AND ca.charge_mode_id = cm.charge_mode_id ";
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
                frm.setChargeModeNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) ) );
                frm.setWeekId( result.getInt( "week" ) );
                frm.setHolidayKind( result.getInt( "holiday_kind" ) );
                frm.setSalesFlag( result.getInt( "sales_flag" ) );
                // 当日フラグのセット
                if ( today == (result.getInt( "cal_date" )) )
                {
                    // 当日
                    frm.setCurrentFlg( 1 );
                }
                else
                {
                    // 当日以外
                    frm.setCurrentFlg( 0 );
                }
                // 受付フラグのセット
                if ( (result.getInt( "cal_date" )) < today )
                {
                    // 当日より前は受付終了
                    frm.setRsvJotai( RSV_OFF );
                    frm.setRsvJotaiMark( RSV_OFF_MARK ); // ×マーク
                    frm.setRsvJotaiFlg( 0 );
                }
                else
                {
                    // 受付中
                    frm.setRsvJotai( RSV_ON );
                    frm.setRsvJotaiMark( RSV_ON_MARK ); // ○マーク
                    frm.setRsvJotaiFlg( 1 );
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
            Logging.error( "[LogicOwnerRsvManage.getHotelCalendar] Exception=" + e.toString() );
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
     * プランID未指定時のカレンダー情報取得
     * 
     * @param frm FormOwnerRsvManageCalendarオブジェクト
     * @param hotelId ホテルID
     * @param targetYM 検索対象年月(YYYYMM)
     * @return FormOwnerRsvManageCalendarのArrayList
     * @throws Exception
     */
    private ArrayList<FormOwnerRsvManageCalendar> getNoPlanIdCalendar(FormOwnerRsvManageCalendar frm, int hotelId, int targetYM) throws Exception
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
        int roomCnt = 0;

        query = query + "SELECT ca.id, ca.cal_date, ca.charge_mode_id, cm.charge_mode_name, ca.week, ca.holiday_kind, ca.sales_flag ";
        query = query + "FROM hh_rsv_hotel_calendar ca ";
        query = query + "    LEFT JOIN hh_rsv_charge_mode cm ON ca.id = cm.id AND ca.charge_mode_id = cm.charge_mode_id ";
        query = query + "WHERE ca.id = ? ";
        query = query + "AND cal_date BETWEEN ? AND ? ";
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
                frm.setChargeModeNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) ) );
                frm.setWeekId( result.getInt( "week" ) );
                frm.setHolidayKind( result.getInt( "holiday_kind" ) );
                frm.setSalesFlag( result.getInt( "sales_flag" ) );
                // 当日フラグのセット
                if ( today == (result.getInt( "cal_date" )) )
                {
                    // 当日
                    frm.setCurrentFlg( 1 );
                }
                else
                {
                    // 当日以外
                    frm.setCurrentFlg( 0 );
                }
                // 受付フラグのセット
                if ( (result.getInt( "cal_date" )) < today )
                {
                    // 当日より前は受付終了
                    frm.setRsvJotai( RSV_OFF );
                    frm.setRsvJotaiMark( RSV_OFF_MARK ); // ×マーク
                    frm.setRsvJotaiFlg( 0 );
                }
                else
                {
                    // 受付中
                    frm.setRsvJotai( RSV_ON );
                    frm.setRsvJotaiMark( RSV_ON_MARK ); // ○マーク
                    frm.setRsvJotaiFlg( 1 );
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
                return(frmList);
            }

            // 部屋残数データがない場合
            roomCnt = getRoomRemainderCnt( hotelId, targetYM );
            if ( roomCnt == 0 )
            {
                frmList = new ArrayList<FormOwnerRsvManageCalendar>();
                frm = new FormOwnerRsvManageCalendar();
                frm.setErrMsg( Message.getMessage( "erro.30001", "カレンダー情報" ) );
                frmList.add( frm );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManage.getNoPlanIdCalendar] Exception=" + e.toString() );
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
     * 対象年月の部屋残数取得
     * 
     * @param hotelId ホテルID
     * @param targetYM 検索対象年月(YYYYMM)
     * @return 取得した部屋残数
     * @throws Exception
     */
    private int getRoomRemainderCnt(int hotelId, int targetYM) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = query + "SELECT COUNT(*) AS CNT ";
        query = query + "FROM hh_rsv_room_remainder ";
        query = query + "WHERE id = ? ";
        query = query + "  AND cal_date LIKE concat(?, '%') ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, targetYM );
            result = prestate.executeQuery();

            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.getRoomRemainderCnt] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(count);
    }

    /**
     * 部屋残数取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID(-1指定時は、プランを絞って検索は行わない)
     * @param selKbn 検索区分(1:全残室数、2:空き部屋数)
     * @param targetDate 検索対象年月日(YYYYMMDD)
     * @return なし
     * @throws Exception
     */
    private int getRoomRemainder(int hotelId, int planId, int selKbn, int targetDate) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = query + "SELECT DISTINCT count(cal_date) AS CNT ";
        query = query + "FROM hh_rsv_room_remainder ";
        query = query + "WHERE id = ? ";
        query = query + "  AND cal_date = ? ";
        if ( planId != -1 )
        {
            query = query + "  AND plan_id = ? ";
        }
        if ( selKbn == 2 )
        {
            query = query + "  AND status = 2 ";
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, targetDate );
            if ( planId != -1 )
            {
                prestate.setInt( 3, planId );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.getRoomRemainder] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(count);
    }

    /**
     * 部屋残数取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID(-1指定時は、プランを絞って検索は行わない)
     * @param selKbn 検索区分(1:全残室数、2:空き部屋数、3:予約部屋数)
     * @param targetDate 検索対象年月日(YYYYMMDD)
     * @return なし
     * @throws Exception
     */
    private int getRoomRemainderPlan(int hotelId, int planId, int selKbn, int targetDate) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = "SELECT COUNT(*) AS CNT FROM hh_rsv_room_remainder rrr  " +
                "INNER JOIN hh_rsv_rel_plan_room rrpr ON " +
                "     ( rrpr.id = rrr.id " +
                "   AND rrpr.seq = rrr.seq ) " +
                "INNER JOIN hh_rsv_room rr ON " +
                "     ( rrr.id = rr.id " +
                "   AND rrr.seq = rr.seq ) " +
                " WHERE rrpr.id = ? " +
                "   AND rrr.cal_date = ? " +
                "   AND rrpr.plan_id = ? " +
                "   AND rr.sales_flag = 1 ";
        if ( selKbn == 2 )
        {
            query = query + "  AND rrr.status = 1 ";
        }
        else if ( selKbn == 3 )
        {
            query = query + "  AND rrr.status = 2 ";
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, targetDate );
            prestate.setInt( 3, planId );
            result = prestate.executeQuery();
            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.getRoomRemainder] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(count);
    }

    /**
     * 予約上限値確認処理
     * 予約データのうち受付済のﾚｺｰﾄﾞ件数とプランマスタの予約上限数とを比較する
     * 
     * @param id ホテルID
     * @param planid プランID
     * @param rsvDate 予約日
     * @return false: 存在する true: 存在しない
     * @throws Exception
     */
    private boolean checkMaxRsvRommCnt(int id, int planid, int rsvDate) throws Exception
    {
        boolean blnRet = false;
        DataRsvPlan dataPlan = new DataRsvPlan();
        int intMaxQuantity = 0;
        int rsvCnt = 0;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            // プランマスタより上限値を取得
            if ( dataPlan.getData( id, planid ) == true )
            {
                // 予約上限数の取得
                intMaxQuantity = dataPlan.getMaxQuantity();
            }

            if ( intMaxQuantity != 0 )
            {
                // ﾎﾃﾙID、ﾌﾟﾗﾝID、予約日を元に受付済の予約データの件数を取得する
                query = "SELECT COUNT(*) AS CNT FROM hh_rsv_reserve " +
                        " WHERE id = ? AND plan_id = ? AND reserve_date = ? AND status = 1 ";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, id );
                prestate.setInt( 2, planid );
                prestate.setInt( 3, rsvDate );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        rsvCnt = result.getInt( "CNT" );
                    }
                }

                // 部屋指定の場合
                if ( intMaxQuantity > rsvCnt )
                {
                    // 予約データの件数が上限数異なる場合はＯＫ
                    blnRet = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.checkMaxRsvRommCnt] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            dataPlan = null;
            DBConnection.releaseResources( result, prestate, connection );
        }
        return blnRet;
    }

    /**
     * 部屋残数取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID(-1指定時は、プランを絞って検索は行わない)
     * @param selKbn 検索区分(1:全残室数、2:空き部屋数)
     * @param seq 管理番号
     * @param targetDate 検索対象年月日(YYYYMMDD)
     * @return なし
     * @throws Exception
     */
    private int getRoomRemainderPlan(int hotelId, int planId, int selKbn, int seq, int targetDate) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;

        query = "SELECT COUNT(*) AS CNT FROM hh_rsv_room_remainder rrr  " +
                "INNER JOIN hh_rsv_rel_plan_room rrpr ON " +
                "     ( rrpr.id = rrr.id " +
                "   AND rrpr.seq = rrr.seq ) " +
                "INNER JOIN hh_rsv_room rr ON " +
                "     ( rrr.id = rr.id " +
                "   AND rrr.seq = rr.seq ) " +
                " WHERE rrpr.id = ? " +
                "   AND rrr.cal_date = ? " +
                "   AND rrpr.plan_id = ? " +
                "   AND rrr.seq = ? " +
                "   AND rr.sales_flag = 1 ";
        if ( selKbn == 2 )
        {
            query = query + "  AND rrr.status = 1 ";
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, targetDate );
            prestate.setInt( 3, planId );
            prestate.setInt( 4, seq );
            result = prestate.executeQuery();

            while( result.next() )
            {
                count = result.getInt( "CNT" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvManageCalendar.getRoomRemainder] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(count);
    }

    /**
     * カレンダー・残室数情報取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param targetYM 対象年月
     * @return 指定された月の1か月分のカレンダー情報
     */
    public ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getPlanCalendarData(int hotelId, int planId, int targetYM) throws Exception
    {
        int cnt = 0;
        int weekId = 0;
        String year = "";
        String month = "";
        ArrayList<FormOwnerRsvManageCalendar> frmList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<FormOwnerRsvManageCalendar> oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
        ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>();
        FormOwnerRsvManageCalendar frm = new FormOwnerRsvManageCalendar();
        FormOwnerRsvManageCalendar frmEmpty;

        // カレンダー情報取得
        frmList = getCalendar( frm, hotelId, planId, targetYM );

        // 部屋残数情報取得
        for( int i = 0 ; i < frmList.size() ; i++ )
        {
            frm = frmList.get( i );
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

            if ( planId == -1 )
            {
                // プランID未指定の場合
                // 全部屋残数取得
                cnt = getRoomRemainder( hotelId, planId, 1, frm.getCalDate() );
                frm.setAllRoomNum( cnt );

                // 空き部屋残数
                cnt = 0;
                cnt = getRoomRemainder( hotelId, planId, 2, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );

                if ( (frm.getRsvJotaiFlg() == 1) && (frm.getSalesFlag() == 0) )
                {
                    frm.setRsvJotai( RSV_STOP );
                }
            }
            else
            {
                // プランID指定の場合
                // 全部屋残数取得
                cnt = getRoomRemainderPlan( hotelId, planId, 1, frm.getCalDate() );

                if ( cnt > ReserveCommon.getMaxQuantityPlan( hotelId, planId ) )
                {
                    cnt = ReserveCommon.getMaxQuantityPlan( hotelId, planId );
                }
                frm.setAllRoomNum( cnt );

                // 予約済みの部屋数
                cnt = 0;
                // cnt = getRoomRemainderPlan( hotelId, planId, 3, frm.getCalDate() );
                // 予約されたカウント数を表示する
                cnt = ReserveCommon.getReserveCount( hotelId, planId, frm.getCalDate() );
                frm.setVacancyRoomNum( cnt );

                // ユーザサイトと同じ表示をするための空き部屋数を取得する（2013/07/18）
                cnt = getRoomRemainderPlan( hotelId, planId, 2, frm.getCalDate() );
                // 上で取得した空き部屋数で判断する
                if ( frm.getRsvJotaiMark().equals( RSV_ON_MARK ) )
                {
                    // if ( frm.getAllRoomNum() <= cnt )
                    if ( cnt == 0 )
                    {
                        frm.setRsvJotai( RSV_OFF );
                        frm.setRsvJotaiMark( RSV_OFF_MARK );
                    }
                    else
                    {
                        // 予約上限数確認
                        if ( checkMaxRsvRommCnt( hotelId, planId, frm.getCalDate() ) == false )
                        {
                            frm.setRsvJotai( RSV_OFF );
                            frm.setRsvJotaiMark( RSV_OFF_MARK );
                        }
                    }
                }

                // 料金モードが0だったら販売しない
                if ( frm.getChargeModeId() == 0 )
                {
                    frm.setRsvJotai( RSV_STOP );
                    frm.setRsvJotaiMark( RSV_IMPOSSIBLE_MARK );
                    frm.setChargeModeNm( "販売停止" );

                }
                if ( frm.getCalDate() < Integer.parseInt( DateEdit.getDate( 2 ) ) )
                {
                    frm.setRsvJotaiMark( RSV_IMPOSSIBLE_MARK );
                }
            }
            oneWeekList.add( frm );

            if ( frm.getWeekId() == 6 )
            {
                // 土曜の場合は、新しいリストを作成
                monthlyList.add( oneWeekList );
                oneWeekList = new ArrayList<FormOwnerRsvManageCalendar>();
            }
        }
        // 最終日以降の空データ作成
        monthlyList.add( oneWeekList );

        return(monthlyList);

    }
}
