/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * ハピタッチ制御クラス
 */
package jp.happyhotel.others;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.data.DataHhRsvHotelCalendar;
import jp.happyhotel.data.DataHhRsvPlan;
import jp.happyhotel.data.DataHotelBasic;

/**
 * プラン販売開始（予約PMS連携用）
 * 
 * @author T.Sakurai
 * @version 1.00 2017/04/13
 */
public class RsvPlanStart
{
    private static final String              RESULT_OK                   = "OK";
    private static final String              RESULT_NG                   = "NG";
    private static final String              RESULT_DENY                 = "DENY";
    private static final String              CONTENT_TYPE                = "text/xml; charset=UTF-8";
    private static final String              ENCODE                      = "UTF-8";

    /** プラン種別 */
    public static final Map<Integer, String> PLAN_TYPE;
    /** プラン種別 - 宿泊プラン */
    public static final int                  PLAN_TYPE_STAY              = 1;
    /** プラン種別 - 休憩・フリータイム */
    public static final int                  PLAN_TYPE_REST              = 2;
    /** プラン種別 - 当日限定（宿泊プラン） */
    public static final int                  PLAN_TYPE_TODAY_STAY        = 3;
    /** プラン種別 - 当日限定（休憩・フリータイム） */
    public static final int                  PLAN_TYPE_TODAY_REST        = 4;

    static
    {
        Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
        tmp.put( PLAN_TYPE_STAY, "宿泊プラン" );
        tmp.put( PLAN_TYPE_REST, "休憩・フリータイム" );
        tmp.put( PLAN_TYPE_TODAY_STAY, "当日限定（宿泊プラン）" );
        tmp.put( PLAN_TYPE_TODAY_REST, "当日限定（休憩・フリータイム）" );
        PLAN_TYPE = Collections.unmodifiableMap( tmp );
    }
    /** プラン販売ステータス */
    public static final Map<Integer, String> PLAN_SALES_STATUS;
    /** プラン販売ステータス - 販売中 */
    public static final int                  PLAN_SALES_STATUS_SALE      = 1;
    /** プラン販売ステータス - 販売停止中 */
    public static final int                  PLAN_SALES_STATUS_STOP      = 2;
    /** プラン販売ステータス - 下書き保存中 */
    public static final int                  PLAN_SALES_STATUS_DRAFT     = 3;
    /** プラン販売ステータス - 販売確認待ち */
    public static final int                  PLAN_SALES_STATUS_CONFIRM   = 4;
    /** プラン販売ステータス - 販売終了（事前予約） */
    public static final int                  PLAN_SALES_STATUS_END_STAY  = 5;
    /** プラン販売ステータス - 販売終了（当日予約） */
    public static final int                  PLAN_SALES_STATUS_END_TODAT = 6;
    static
    {
        Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
        tmp.put( PLAN_SALES_STATUS_SALE, "販売中" );
        tmp.put( PLAN_SALES_STATUS_STOP, "販売停止中" );
        tmp.put( PLAN_SALES_STATUS_DRAFT, "下書き保存中" );
        tmp.put( PLAN_SALES_STATUS_CONFIRM, "販売確認待ち" );
        tmp.put( PLAN_SALES_STATUS_END_STAY, "販売期間終了（事前予約）" );
        tmp.put( PLAN_SALES_STATUS_END_TODAT, "販売期間終了（当日予約）" );
        PLAN_SALES_STATUS = Collections.unmodifiableMap( tmp );
    }

    /***
     * エラーメッセージ出力処理
     * 
     * @param root ルートノードネーム
     * @param message エラーメッセージ
     * @param response レスポンス
     */
    public void errorData(String root, String message, HttpServletResponse response)
    {
        GenerateXmlHapiTouchHotelInfo gxTouch;
        ServletOutputStream stream = null;

        try
        {
            stream = response.getOutputStream();

            gxTouch = new GenerateXmlHapiTouchHotelInfo();

            // xml出力クラスにノードをセット

            gxTouch.setResult( RESULT_DENY );
            gxTouch.setMessage( message );

            // XMLの出力
            String xmlOut = gxTouch.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            out.write( xmlOut.getBytes( ENCODE ) );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[ActionHapiTouch errorData]Exception:" + e.toString() );
                }
            }
        }
    }

    /****
     * 予約プランの販売を開始する
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void rsvPlanStart(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        GenerateXmlRsvPlanStart gxRsvPlanStart;

        gxRsvPlanStart = new GenerateXmlRsvPlanStart();
        String paramAction;
        String paramPlanId;
        paramPlanId = request.getParameter( "planId" );
        if ( (paramPlanId == null) || (paramPlanId.equals( "" ) != false) || (CheckString.numCheck( paramPlanId ) == false) )
        {
            paramPlanId = "0";
        }
        paramAction = request.getParameter( "action" );
        if ( paramAction == null )
        {
            paramAction = "";
        }
        int planId = Integer.parseInt( paramPlanId );
        // レスポンスをセット
        try
        {
            boolean ret = true;
            DataHhRsvPlan plan = new DataHhRsvPlan();
            if ( paramAction.equals( "" ) )
            {
                ret = false;
            }
            else
            {
                ret = plan.getLatestData( hotelId, planId );
                Logging.info( "[RsvPlanStart.rsvPlanStart]getLatestData hotelid:" + hotelId + ",planId:" + planId + ",ret=" + ret );
            }
            if ( ret != false )
            {

                if ( plan.getPlanType() != PLAN_TYPE_TODAY_STAY && plan.getPlanType() != PLAN_TYPE_TODAY_REST )
                {
                    ret = false; // 当日予約でない
                }
                else if ( plan.getPlanSalesStatus() != PLAN_SALES_STATUS_SALE && plan.getPlanSalesStatus() != PLAN_SALES_STATUS_STOP )
                {
                    ret = false; // 販売中・販売停止でないので開始できない
                }
                Logging.info( "[RsvPlanStart.rsvPlanStart]ret=" + ret + ",hotelid:" + hotelId + ",planId:" + planId + ",plan.getPlanType()=" + plan.getPlanType() + ",plan.getPlanSalesStatus()=" + plan.getPlanSalesStatus() + ",action=" + paramAction );
            }
            if ( ret != false )
            {
                ret = planRegistration( plan, paramAction );
            }

            stream = response.getOutputStream();

            if ( ret != false )
            {
                gxRsvPlanStart.setResult( RESULT_OK );
            }
            else
            {
                gxRsvPlanStart.setResult( RESULT_NG );
            }
            // XMLの出力
            String xmlOut = gxRsvPlanStart.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            response.setContentLength( xmlOut.getBytes().length );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanStart.rsvPlanStart]Exception:" + e.toString() + " [id=" + hotelId + ", plan_id=" + planId + "]" );
        }
        finally
        {
            if ( stream != null )
            {
                try
                {
                    stream.close();
                }
                catch ( IOException e )
                {
                    Logging.error( "[RsvPlanStart.rsvPlanStart]Exception:" + e.toString() + " [id=" + hotelId + ", plan_id=" + planId + "]" );
                }
            }

        }
    }

    public boolean planRegistration(DataHhRsvPlan plan, String paramAction) throws Exception
    {
        DataHotelBasic hotel = new DataHotelBasic();
        int hotelId = plan.getId();
        int planId = plan.getPlanId();
        int nowPlanSubId = plan.getPlanSubId();
        int planSubId = plan.getSubId( hotelId, planId );
        boolean isInsert = false;
        boolean ret = true;
        String hotenaviId = "";
        if ( hotel.getData( hotelId ) != false )
        {
            hotenaviId = hotel.getHotenaviId();
        }

        Connection connection = DBConnection.getConnection( false );
        try
        {
            // plan.getData( connection, hotelId, planId, nowPlanSubId );
            if ( paramAction.equals( "start" ) )
            {
                int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
                int today = Integer.parseInt( DateEdit.getDate( 2 ) );
                int weekIndex = DateEdit.getWeekIndex( today );
                if ( nowTime < 50000 )
                {
                    today = DateEdit.addDay( today, -1 );
                }
                DataHhRsvHotelCalendar hrhc = new DataHhRsvHotelCalendar();
                // 当日のカレンダーデータがない場合、当日分のみ作成
                if ( !hrhc.getData( connection, hotelId, today ) )
                {
                    hrhc.setId( hotelId );
                    hrhc.setCalDate( today );
                    hrhc.setChargeModeId( weekIndex );
                    hrhc.setWeek( weekIndex );
                    hrhc.setHolidayKind( 0 );
                    hrhc.setHotelId( plan.getHotelId() );
                    hrhc.setUserId( plan.getUserId() );
                    hrhc.setLastUpdate( today );
                    hrhc.setLastUptime( nowTime );
                    this.registHotelCalendarToday( connection, hrhc, plan.getHotelId() );
                }
                // 新subIdのlatest_flagを書き込む
                planSubId = plan.getSubId( connection, hotelId, planId );
                plan.setPlanSubId( planSubId );
                plan.setPlanSalesStatus( PLAN_SALES_STATUS_SALE );

                plan.setSalesStartDate( today );
                plan.setSalesEndDate( today );
                plan.setLatestFlag( 1 );
                plan.setHotelId( hotenaviId );
                plan.setUserId( 0 );
                plan.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                plan.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                ret = plan.insertData( connection );
                if ( !ret )
                {
                    planSubId++;
                    plan.setPlanSubId( planSubId );
                    ret = plan.insertData( connection );
                }
                if ( ret != false )
                {
                    // 追加したplanSubId 以外のlatest_flagを0に変更
                    plan.setLatestFlag( 0 );
                    plan.updateFlag( connection, hotelId, planId, planSubId );
                }

                // Logging.info( "[RsvPlanStart.planRegistration insert]hotelid:" + paramAction + "," + hotelId + ",planId:" + planId + ",PlanSubId=" + planSubId + ",plan.getPlanSalesStatus()=" + plan.getPlanSalesStatus() + ",action=" + paramAction );
            }
            else if ( paramAction.equals( "stop" ) )
            {
                plan.setPlanSalesStatus( PLAN_SALES_STATUS_STOP );
                plan.setHotelId( hotenaviId );
                plan.setUserId( 0 );
                plan.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                plan.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                ret = plan.updateData( connection, hotelId, planId, nowPlanSubId );
                // Logging.info( "[RsvPlanStart.planRegistration]ret:" + ret + ",hotelid:" + hotelId + ",planId:" + planId + ",PlanSubId=" + planSubId + ",plan.getPlanSalesStatus()=" + plan.getPlanSalesStatus() + ",action=" + paramAction );
            }

            if ( ret && paramAction.equals( "start" ) )
            {

                // プラン部屋設定
                this.saveRelPlanRoom( connection, plan, nowPlanSubId );

                // プランランク設定
                this.savePlanRoomrank( connection, plan, nowPlanSubId );

                // 料金モード内訳
                this.saveChargeModeBreakdown( connection, plan );

                // プラン別料金マスタ
                this.savePlanCharge( connection, plan, nowPlanSubId );

                // 日別販売数
                this.saveDayCharge( connection, plan, nowPlanSubId );

            }
            // ホテル修正履歴
            OwnerRsvCommon.addAdjustmentHistory(
                    plan.getId(),
                    plan.getHotelId(),
                    plan.getUserId(),
                    OwnerRsvCommon.ADJUST_EDIT_ID_PLAN_ADD,
                    plan.getPlanId() * 10 + 1,
                    OwnerRsvCommon.ADJUST_MEMO_PLAN_ADD
                    );

            connection.commit();

            return ret;
        }
        catch ( Exception e )
        {
            /*
             * プラン新規登録時にエラーがあった場合に
             * 登録されていないプランID枝番がセットさせないために、
             * エラー時はプランID枝番を前の値に戻す
             */
            if ( isInsert )
            {
                plan.setPlanSubId( nowPlanSubId );
            }

            try
            {
                if ( connection != null )
                {
                    connection.rollback();
                }
            }
            catch ( SQLException e1 )
            {
                Logging.error( "[RsvPlanStart.planRegistration] Exception=" + e1.toString() );
            }
            Logging.error( "[RsvPlanStart.planRegistration] Exception=" + e.toString(), e );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * プラン・部屋設定データ保存
     * 
     * @param connection
     * @throws Exception
     */
    protected void saveRelPlanRoom(Connection connection, DataHhRsvPlan plan, int nowPlanSubId) throws Exception
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        PreparedStatement insPrestate = null;

        StringBuilder query = new StringBuilder();
        query.append( "SELECT plan_room.* FROM newRsvDB.hh_rsv_rel_plan_room plan_room" );
        query.append( " INNER JOIN hh_hotel_room_more room_more ON plan_room.id = room_more.id AND plan_room.seq = room_more.seq" );
        query.append( " WHERE plan_room.id = ?" );
        query.append( " AND plan_room.plan_id = ?" );
        query.append( " AND plan_room.plan_sub_id = ?" );

        StringBuilder insertQuery = new StringBuilder();
        insertQuery.append( "INSERT INTO newRsvDB.hh_rsv_rel_plan_room ( " );
        insertQuery.append( "   id" );
        insertQuery.append( " , plan_id" );
        insertQuery.append( " , plan_sub_id" );
        insertQuery.append( " , seq" );
        insertQuery.append( " , image_file_name" );
        insertQuery.append( " , hotel_id" );
        insertQuery.append( " , user_id" );
        insertQuery.append( " , last_update" );
        insertQuery.append( " , last_uptime" );
        insertQuery.append( " ) VALUES ( " );
        insertQuery.append( "   ?, ?, ?, ?, ? " );
        insertQuery.append( " , ?, ?, ?, ? " );
        insertQuery.append( " ) " );
        prestate = connection.prepareStatement( query.toString() );

        try
        {

            prestate.setInt( 1, plan.getId() );
            prestate.setInt( 2, plan.getPlanId() );
            prestate.setInt( 3, nowPlanSubId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    insPrestate = connection.prepareStatement( insertQuery.toString() );
                    int i = 1;
                    insPrestate.setInt( i++, plan.getId() );
                    insPrestate.setInt( i++, plan.getPlanId() );
                    insPrestate.setInt( i++, plan.getPlanSubId() );
                    insPrestate.setInt( i++, result.getInt( "plan_room.seq" ) );
                    insPrestate.setString( i++, "" );
                    insPrestate.setString( i++, plan.getHotelId() );
                    insPrestate.setInt( i++, plan.getUserId() );
                    insPrestate.setInt( i++, plan.getLastUpdate() );
                    insPrestate.setInt( i++, plan.getLastUptime() );
                    insPrestate.executeUpdate();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanStart.saveRelPlanRoom] [id=" + plan.getId() + ", plan_id=" + plan.getPlanId() + ", plan_sub_id=" + plan.getPlanSubId() + "] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( insPrestate );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * プラン・ランク設定保存
     * 
     * @param connection
     * @throws Exception
     */
    protected void savePlanRoomrank(Connection connection, DataHhRsvPlan plan, int nowPlanSubId) throws Exception
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        PreparedStatement insPrestate = null;

        StringBuilder query = new StringBuilder();
        query.append( "SELECT planrank.* FROM newRsvDB.hh_rsv_plan_roomrank planrank" );
        query.append( " WHERE planrank.id = ?" );
        query.append( " AND planrank.plan_id = ?" );
        query.append( " AND planrank.plan_sub_id = ?" );

        StringBuilder insertQuery = new StringBuilder();
        insertQuery.append( "INSERT INTO newRsvDB.hh_rsv_plan_roomrank ( " );
        insertQuery.append( "  id" );
        insertQuery.append( " , plan_id" );
        insertQuery.append( " , plan_sub_id" );
        insertQuery.append( " , room_rank" );
        insertQuery.append( " , image" );
        insertQuery.append( " , max_quantity" );
        insertQuery.append( " , hotel_id" );
        insertQuery.append( " , user_id" );
        insertQuery.append( " , last_update" );
        insertQuery.append( " , last_uptime" );
        insertQuery.append( " ) VALUES ( " );
        insertQuery.append( "   ?, ?, ?, ?, ? " );
        insertQuery.append( " , ?, ?, ?, ?, ? " );
        insertQuery.append( " ) " );
        prestate = connection.prepareStatement( query.toString() );

        try
        {

            prestate.setInt( 1, plan.getId() );
            prestate.setInt( 2, plan.getPlanId() );
            prestate.setInt( 3, nowPlanSubId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    insPrestate = connection.prepareStatement( insertQuery.toString() );
                    int i = 1;
                    insPrestate.setInt( i++, plan.getId() );
                    insPrestate.setInt( i++, plan.getPlanId() );
                    insPrestate.setInt( i++, plan.getPlanSubId() );
                    insPrestate.setInt( i++, result.getInt( "planrank.room_rank" ) );
                    insPrestate.setString( i++, null );
                    insPrestate.setInt( i++, 999 );
                    insPrestate.setString( i++, plan.getHotelId() );
                    insPrestate.setInt( i++, plan.getUserId() );
                    insPrestate.setInt( i++, plan.getLastUpdate() );
                    insPrestate.setInt( i++, plan.getLastUptime() );
                    insPrestate.executeUpdate();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanStart.savePlanRoomrank] [id=" + plan.getId() + ", plan_id=" + plan.getPlanId() + ", plan_sub_id=" + plan.getPlanSubId() + "] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( insPrestate );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 料金モード内訳保存
     * 
     * @param connection
     * @throws Exception
     */
    protected void saveChargeModeBreakdown(Connection connection, DataHhRsvPlan plan) throws Exception
    {
        PreparedStatement insPrestate = null;

        StringBuilder insertQuery = new StringBuilder();
        insertQuery.append( "INSERT INTO newRsvDB.hh_rsv_charge_mode_breakdown ( " );
        insertQuery.append( "  id" );
        insertQuery.append( " , plan_id" );
        insertQuery.append( " , plan_sub_id" );
        insertQuery.append( " , plan_charge_mode_id" );
        insertQuery.append( " , charge_mode_id" );
        insertQuery.append( " ) VALUES ( " );
        insertQuery.append( "   ?, ?, ?, ?, ? " );
        insertQuery.append( " ) " );

        try
        {
            DataHhRsvHotelCalendar dataHotelCalendar = new DataHhRsvHotelCalendar();
            if ( dataHotelCalendar.getData( connection, plan.getId(), plan.getSalesStartDate() ) == false )
            {
                throw new Exception( "ホテルカレンダーの取得に失敗しました。" );
            }

            insPrestate = connection.prepareStatement( insertQuery.toString() );
            int i = 1;
            insPrestate.setInt( i++, plan.getId() );
            insPrestate.setInt( i++, plan.getPlanId() );
            insPrestate.setInt( i++, plan.getPlanSubId() );
            insPrestate.setInt( i++, 1 ); // 当日は1固定
            insPrestate.setInt( i++, dataHotelCalendar.getChargeModeId() );
            insPrestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanStart.saveChargeModeBreakdown] [id=" + plan.getId() + ", plan_id=" + plan.getPlanId() + ", plan_sub_id=" + plan.getPlanSubId() + "] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( insPrestate );
        }
    }

    /**
     * プラン別料金マスタ保存
     * 
     * @param connection
     * @throws Exception
     */
    protected void savePlanCharge(Connection connection, DataHhRsvPlan plan, int nowPlanSubId) throws Exception
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        PreparedStatement insPrestate = null;

        StringBuilder query = new StringBuilder();
        query.append( "SELECT plancharge.* FROM newRsvDB.hh_rsv_plan_charge plancharge" );
        query.append( " WHERE plancharge.id = ?" );
        query.append( " AND plancharge.plan_id = ?" );
        query.append( " AND plancharge.plan_sub_id = ?" );

        StringBuilder insertQuery = new StringBuilder();
        insertQuery.append( "INSERT INTO newRsvDB.hh_rsv_plan_charge ( " );
        insertQuery.append( "   id" );
        insertQuery.append( " , plan_id" );
        insertQuery.append( " , plan_sub_id" );
        insertQuery.append( " , plan_charge_mode_id" );
        insertQuery.append( " , room_rank" );
        insertQuery.append( " , ci_time_from" );
        insertQuery.append( " , ci_time_to" );
        insertQuery.append( " , co_time" );
        insertQuery.append( " , co_kind" );
        insertQuery.append( " , plan_charge" );
        insertQuery.append( " , hotel_id" );
        insertQuery.append( " , user_id" );
        insertQuery.append( " , last_update" );
        insertQuery.append( " , last_uptime" );
        insertQuery.append( " ) VALUES ( " );
        insertQuery.append( "   ?, ?, ?, ?, ? " );
        insertQuery.append( " , ?, ?, ?, ?, ? " );
        insertQuery.append( " , ?, ?, ?, ? " );
        insertQuery.append( " ) " );
        prestate = connection.prepareStatement( query.toString() );

        try
        {

            prestate.setInt( 1, plan.getId() );
            prestate.setInt( 2, plan.getPlanId() );
            prestate.setInt( 3, nowPlanSubId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    insPrestate = connection.prepareStatement( insertQuery.toString() );
                    int i = 1;
                    insPrestate.setInt( i++, plan.getId() );
                    insPrestate.setInt( i++, plan.getPlanId() );
                    insPrestate.setInt( i++, plan.getPlanSubId() );
                    insPrestate.setInt( i++, 1 );
                    insPrestate.setInt( i++, result.getInt( "plancharge.room_rank" ) );
                    insPrestate.setInt( i++, result.getInt( "plancharge.ci_time_from" ) );
                    insPrestate.setInt( i++, result.getInt( "plancharge.ci_time_to" ) );
                    insPrestate.setInt( i++, result.getInt( "plancharge.co_time" ) );
                    insPrestate.setInt( i++, result.getInt( "plancharge.co_kind" ) );
                    insPrestate.setInt( i++, result.getInt( "plancharge.plan_charge" ) );
                    insPrestate.setString( i++, plan.getHotelId() );
                    insPrestate.setInt( i++, plan.getUserId() );
                    insPrestate.setInt( i++, plan.getLastUpdate() );
                    insPrestate.setInt( i++, plan.getLastUptime() );
                    insPrestate.executeUpdate();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanToday.savePlanCharge] [id=" + plan.getId() + ", plan_id=" + plan.getPlanId() + ", plan_sub_id=" + plan.getPlanSubId() + "] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( insPrestate );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 日別販売数
     * 
     * @param connection
     * @throws Exception
     */
    protected void saveDayCharge(Connection connection, DataHhRsvPlan plan, int nowPlanSubId) throws Exception
    {
        ResultSet result = null;
        PreparedStatement prestate = null;
        PreparedStatement insPrestate = null;

        StringBuilder query = new StringBuilder();
        query.append( "SELECT daycharge.* FROM newRsvDB.hh_rsv_day_charge daycharge" );
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan plan" );
        query.append( " ON plan.id = daycharge.id AND plan.plan_id = daycharge.plan_id" );
        query.append( " WHERE daycharge.id = ?" );
        query.append( " AND daycharge.plan_id = ?" );
        query.append( " AND plan.plan_sub_id = ?" );
        query.append( " AND daycharge.plan_sub_id = (SELECT IFNULL(MAX(plan_sub_id), 0) FROM newRsvDB.hh_rsv_day_charge WHERE id = daycharge.id AND plan_id = daycharge.plan_id ) " );
        query.append( " AND plan.plan_sub_id >= daycharge.plan_sub_id" );

        StringBuilder insertQuery = new StringBuilder();
        insertQuery.append( "INSERT INTO newRsvDB.hh_rsv_day_charge ( " );
        insertQuery.append( "   id" );
        insertQuery.append( " , plan_id" );
        insertQuery.append( " , plan_sub_id" );
        insertQuery.append( " , cal_date" );
        insertQuery.append( " , room_rank" );
        insertQuery.append( " , sale_rooms_quantity" );
        insertQuery.append( " , hotel_id" );
        insertQuery.append( " , user_id" );
        insertQuery.append( " , last_update" );
        insertQuery.append( " , last_uptime" );
        insertQuery.append( " ) VALUES ( " );
        insertQuery.append( "   ?, ?, ?, ?, ? " );
        insertQuery.append( " , ?, ?, ?, ?, ? " );
        insertQuery.append( " ) " );
        prestate = connection.prepareStatement( query.toString() );

        try
        {

            prestate.setInt( 1, plan.getId() );
            prestate.setInt( 2, plan.getPlanId() );
            prestate.setInt( 3, nowPlanSubId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    insPrestate = connection.prepareStatement( insertQuery.toString() );
                    int i = 1;
                    insPrestate.setInt( i++, plan.getId() );
                    insPrestate.setInt( i++, plan.getPlanId() );
                    insPrestate.setInt( i++, plan.getPlanSubId() );
                    insPrestate.setInt( i++, plan.getSalesStartDate() );
                    insPrestate.setInt( i++, result.getInt( "daycharge.room_rank" ) );
                    insPrestate.setInt( i++, result.getInt( "daycharge.sale_rooms_quantity" ) );// 販売開始時の販売数は販売終了時の数と同じとする
                    insPrestate.setString( i++, plan.getHotelId() );
                    insPrestate.setInt( i++, plan.getUserId() );
                    insPrestate.setInt( i++, plan.getLastUpdate() );
                    insPrestate.setInt( i++, plan.getLastUptime() );
                    insPrestate.executeUpdate();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanStart.savePlanCharge] [id=" + plan.getId() + ", plan_id=" + plan.getPlanId() + ", plan_sub_id=" + plan.getPlanSubId() + "] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( insPrestate );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * ホテルカレンダーの登録(本日のみ)
     * ・ホテルカレンダー(hh_rsv_hotel_calendar)
     * ・日別販売数（hh_rsv_day_chrage)は、INSERTのみ
     * ・部屋残数（hh_rsv_room_remainder)は、INSERTのみ
     * 
     * @param hrhc
     * @param hotenaviId
     * @return
     * @throws Exception
     */
    public boolean registHotelCalendarToday(Connection connection, DataHhRsvHotelCalendar hrhc, String hotenaviId) throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;
        int seq = 1;
        boolean ret = false;

        try
        {
            // 本日のみカレンダー設定
            hrhc.insertData( connection );
            // 日別販売数の登録
            this.insDayCharge( connection, hrhc.getId(), hrhc.getCalDate() / 100, hotenaviId, hrhc.getUserId() );

            // 部屋残数
            query = "INSERT INTO newRsvDB.hh_rsv_room_remainder " +
                    "(id,cal_date,seq,stay_status,stay_reserve_no,stay_reserve_temp_no," +
                    "stay_reserve_limit_day,stay_reserve_limit_time,rest_status,rest_reserve_no,rest_reserve_temp_no," +
                    "rest_reserve_limit_day,rest_reserve_limit_time) " +
                    "SELECT p.id, c.cal_date, p.seq, 1, '', 0, 0, 0, 1, '', 0, 0, 0 " +
                    // ホテル部屋
                    "FROM hh_hotel_room_more p " +
                    // カレンダーと結合
                    "INNER JOIN newRsvDB.hh_rsv_calendar c ON c.cal_date > (? * 100) AND c.cal_date < (? * 100 + 99) " +
                    "WHERE p.id = ? " +
                    // 部屋残数に未登録
                    "AND NOT EXISTS " +
                    "(SELECT * FROM newRsvDB.hh_rsv_room_remainder WHERE id = p.id " +
                    " AND cal_date = c.cal_date AND seq = p.seq " +
                    ")";

            prestate = connection.prepareStatement( query );

            prestate.setInt( seq++, hrhc.getCalDate() / 100 );
            prestate.setInt( seq++, hrhc.getCalDate() / 100 );
            prestate.setInt( seq++, hrhc.getId() );
            prestate.execute();

        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanStart.registHotelCalendarToday] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return ret;
    }

    /**
     * 日別販売数の登録
     * ・プランに対応する日別販売数を１ヶ月単位で登録する（INSERTのみ）
     * ・プランの条件は、掲載、予約可能期間（宿泊日）で絞る
     * ・同一プランの中では、最新プランよりplan_sub_idが大きければ対象とする
     * ・販売ステータスは考慮しない
     * 2015-05-01
     * ・紐付きが無くなったデータで最新フラグが1のデータは削除する
     * 
     * @param connection
     * @param id
     * @param yearMonth
     * @param hotenaviId
     * @param userId
     * @throws Exception
     */
    public void insDayCharge(Connection connection, int id, int yearMonth, String hotenaviId, int userId) throws Exception
    {
        String query = "";
        String queryMain = "";
        PreparedStatement prestate = null;
        int seq = 1;

        // 日別販売数のデータを得る
        queryMain =
                // プラン
                "FROM newRsvDB.hh_rsv_plan p " +
                        // プラン別料金マスタ（部屋ランク番号）と 料金モード内訳（料金モードIDでカレンダーと結合）
                        "INNER JOIN " +
                        "(SELECT DISTINCT p.id, p.plan_id, p.plan_sub_id, p.room_rank, cmbd.charge_mode_id " +
                        " FROM newRsvDB.hh_rsv_plan_charge p" +
                        " INNER JOIN newRsvDB.hh_rsv_charge_mode_breakdown cmbd ON cmbd.id=p.id AND cmbd.plan_id=p.plan_id AND cmbd.plan_sub_id=p.plan_sub_id " +
                        " AND cmbd.id = ? " +
                        ") pc ON pc.id=p.id AND pc.plan_id=p.plan_id AND pc.plan_sub_id=p.plan_sub_id " +
                        // カレンダー
                        "INNER JOIN newRsvDB.hh_rsv_hotel_calendar hc ON hc.id=pc.id AND hc.charge_mode_id=pc.charge_mode_id " +
                        // ルームランク
                        "INNER JOIN newRsvDB.hh_rsv_plan_roomrank pr ON pr.id=pc.id AND pr.plan_id=pc.plan_id AND pr.plan_sub_id=pc.plan_sub_id AND pr.room_rank = pc.room_rank " +
                        // プランの条件は、掲載、最新以上、予約可能期間（宿泊日）
                        "WHERE p.publishing_flag != 0 " + // 掲載
                        " AND p.id = ? " +
                        // カレンダーの日付け範囲
                        "AND hc.cal_date < (? * 100 + 99) AND hc.cal_date > (? * 100) " +
                        // 予約可能期間は、作成する月に入っているか
                        "AND p.sales_start_date <= hc.cal_date AND p.sales_end_date >= hc.cal_date ";

        query = "INSERT INTO newRsvDB.hh_rsv_day_charge " +
                "(id,plan_id,plan_sub_id,cal_date,room_rank,sale_rooms_quantity,hotel_id,user_id,last_update,last_uptime) " +
                "SELECT p.id, p.plan_id, p.plan_sub_id, hc.cal_date, pc.room_rank, pr.max_quantity " +
                ", ?, ?, CURDATE()+0, CURTIME()+0 " +
                queryMain +
                // 同一プランの中では、最新プランよりplan_sub_idが大きければ対象とする
                "AND p.plan_sub_id >= (SELECT IFNULL(MAX(plan_sub_id), 0) FROM newRsvDB.hh_rsv_plan WHERE id = p.id AND plan_id = p.plan_id AND latest_flag = 1) " +
                "AND p.id = ? " +
                // 日別販売数に未登録
                "AND NOT EXISTS " +
                "(SELECT * FROM newRsvDB.hh_rsv_day_charge WHERE id = p.id AND plan_id = p.plan_id AND plan_sub_id = p.plan_sub_id " +
                " AND cal_date = hc.cal_date AND room_rank = pc.room_rank " +
                ")";

        try
        {
            prestate = connection.prepareStatement( query );

            prestate.setString( seq++, hotenaviId ); //
            prestate.setInt( seq++, userId ); //
            prestate.setInt( seq++, id );
            prestate.setInt( seq++, id );
            prestate.setInt( seq++, yearMonth ); // 年月
            prestate.setInt( seq++, yearMonth );
            prestate.setInt( seq++, id );

            prestate.execute();

            Logging.debug( "[RsvPlanStart.insDayCharge] sql " );
            Logging.debug( query );

            // 2015-05-01 紐付きが無くなったデータを削除する
            query = "DELETE d FROM newRsvDB.hh_rsv_day_charge d " +
                    "LEFT JOIN newRsvDB.hh_rsv_plan p ON p.id = d.id AND p.plan_id = d.plan_id AND p.plan_sub_id = d.plan_sub_id " +
                    "LEFT JOIN " +
                    "(SELECT p.id, p.plan_id, p.plan_sub_id, hc.cal_date, pc.room_rank " +
                    queryMain +
                    " AND p.latest_flag = 1 " +
                    ") c ON c.id = d.id AND c.plan_id = d.plan_id AND c.plan_sub_id = d.plan_sub_id " +
                    " AND c.room_rank = d.room_rank AND c.cal_date = d.cal_date " +
                    // ホテルIDと日付けで範囲設定
                    "WHERE d.id = ? AND d.cal_date < (? * 100 + 99) AND d.cal_date > (? * 100) " +
                    // 紐付きが無くなったデータ
                    " AND c.id IS NULL " +
                    // 最新フラグのデータだけを対象にする
                    " AND p.latest_flag = 1 ";

            prestate.close();
            prestate = connection.prepareStatement( query );
            seq = 1;
            prestate.setInt( seq++, id ); //
            prestate.setInt( seq++, id ); //
            prestate.setInt( seq++, yearMonth ); // 年月
            prestate.setInt( seq++, yearMonth );
            prestate.setInt( seq++, id ); //
            prestate.setInt( seq++, yearMonth ); // 年月
            prestate.setInt( seq++, yearMonth );

            prestate.execute();

            Logging.debug( "[RsvPlanStart.insDayCharge] sql " );
            Logging.debug( query );
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanStart.insDayCharge] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

    }
}
