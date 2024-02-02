/*
 * @(#)HapiTouch.java
 * 1.00 2011/01/12 Copyright (C) ALMEX Inc. 2007
 * ハピタッチ制御クラス
 */
package jp.happyhotel.others;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterPoint;

/**
 * プランデータ一覧取得（予約PMS連携用）
 * 
 * @author T.Sakurai
 * @version 1.00 2017/04/12
 */
public class RsvPlanData
{
    // ポイント区分
    private static final String              RESULT_DENY                = "DENY";
    private static final String              CONTENT_TYPE               = "text/xml; charset=UTF-8";
    private static final String              ENCODE                     = "UTF-8";

    /** チェックアウト区分 */
    public static final Map<Integer, String> CO_KIND;
    /** チェックアウト区分 - ～時まで */
    public static final int                  CO_KIND_TO_TIME            = 1;
    /** チェックアウト区分 - INから */
    public static final int                  CO_KIND_FROM_CHECKIN       = 2;
    static
    {
        Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
        tmp.put( CO_KIND_TO_TIME, "～時まで" );
        tmp.put( CO_KIND_FROM_CHECKIN, "INから" );
        CO_KIND = Collections.unmodifiableMap( tmp );
    }
    /** 追加料金 */
    public static final Map<Integer, String> ADD_CHARGE_KIND;
    /** 追加料金 - 受け付けない */
    public static final int                  ADD_CHARGE_KIND_NOT_ACCEPT = 1;
    /** 追加料金 - %増し */
    public static final int                  ADD_CHARGE_KIND_PERCENT    = 2;
    /** 追加料金 - 円増し */
    public static final int                  ADD_CHARGE_KIND_YEN        = 3;
    static
    {
        Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
        tmp.put( ADD_CHARGE_KIND_NOT_ACCEPT, "追加料金なし" );
        tmp.put( ADD_CHARGE_KIND_PERCENT, "%増し" );
        tmp.put( ADD_CHARGE_KIND_YEN, "円増し" );
        ADD_CHARGE_KIND = Collections.unmodifiableMap( tmp );
    }

    /** 部屋選択区分 */
    public static final Map<Integer, String> ROOM_SELECT_KIND;
    /** 部屋選択区分 - お客様に部屋ランクを選ばせる */
    public static final int                  ROOM_SELECT_KIND_RANK      = 1;
    /** 部屋選択区分 - お客様にお部屋を選ばせる */
    public static final int                  ROOM_SELECT_KIND_ROOM      = 2;
    /** 部屋選択区分 - ランク/お部屋の指定なし */
    public static final int                  ROOM_SELECT_KIND_NONE      = 3;
    static
    {
        Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
        tmp.put( 1, "お客様に部屋ランクを選ばせる" );
        tmp.put( 2, "お客様にお部屋を選ばせる" );
        tmp.put( 3, "ランク/お部屋の指定なし" );
        ROOM_SELECT_KIND = Collections.unmodifiableMap( tmp );
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
     * 予約プランの一覧を取得する
     * 
     * @param hotelId ホテルID
     * @param request リクエスト
     * @param response レスポンス
     */
    public void rsvPlanData(int hotelId, HttpServletRequest request, HttpServletResponse response)
    {
        ServletOutputStream stream = null;
        GenerateXmlRsvPlanData gxRsvPlanData;
        GenerateXmlRsvPlanDataSub gxRsvPlanDataSub;

        gxRsvPlanData = new GenerateXmlRsvPlanData();
        String paramKind;
        paramKind = request.getParameter( "kind" );
        if ( (paramKind == null) || (paramKind.equals( "" ) != false) || (CheckString.numCheck( paramKind ) == false) )
        {
            paramKind = "0";
        }

        if ( Integer.parseInt( paramKind ) <= 0 )
        {
            paramKind = "5";
        }

        // レスポンスをセット
        try
        {
            stream = response.getOutputStream();

            int[][] planIds = searchPlans( hotelId, 0 );
            // gxRsvPlanData.setPlanCount( Integer.toString( planIds.length / 2 ) );
            // for( int i = 0 ; i < planIds.length / 2 ; i++ )
            gxRsvPlanData.setPlanCount( Integer.toString( planIds.length ) );
            for( int i = 0 ; i < planIds.length ; i++ )
            {
                gxRsvPlanDataSub = new GenerateXmlRsvPlanDataSub();
                getPlanDetail( gxRsvPlanDataSub, hotelId, planIds[i][0], planIds[i][1] );
                gxRsvPlanData.addData( gxRsvPlanDataSub );
            }

            // XMLの出力
            String xmlOut = gxRsvPlanData.createXml();
            Logging.info( xmlOut );
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType( CONTENT_TYPE );
            response.setContentLength( xmlOut.getBytes().length );
            out.write( xmlOut.getBytes( ENCODE ) );

        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch rsvPlanData]Exception:" + e.toString() );
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
                    Logging.error( "[ActionHapiTouch rsvPlanData]Exception:" + e.toString() );
                }
            }

        }
    }

    /**
     * 条件に一致するプランIDのリストを取得する
     * 
     * @param hotelId
     * @param status
     * @return
     * @throws Exception
     */
    public static int[][] searchPlans(int hotelId, int sales_status) throws Exception
    {
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        int calDate = nowDate;
        int calTime = nowTime;
        if ( nowTime < 50000 )
        {
            calDate = DateEdit.addDay( nowDate, -1 );
            calTime = calTime + 240000;
        }

        StringBuilder query = new StringBuilder();
        query.append( " SELECT plan.plan_id, plan.plan_sub_id " );
        // プランマスタ
        query.append( " FROM newRsvDB.hh_rsv_plan plan " );
        // ホテル基本情報
        query.append( " INNER JOIN newRsvDB.hh_rsv_reserve_basic basic " );
        query.append( "   ON plan.id = basic.id  " );
        query.append( "   AND basic.hotel_sales_flag = 0 " );
        // プラン・ランク設定
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan_roomrank plan_rank " );
        query.append( "   ON plan.id = plan_rank.id  " );
        query.append( "   AND plan.plan_id = plan_rank.plan_id " );
        query.append( "   AND plan.plan_sub_id = plan_rank.plan_sub_id " );
        // プラン別料金マスタ
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan_charge plan_charge " );
        query.append( "   ON plan_rank.id = plan_charge.id  " );
        query.append( "   AND plan_rank.plan_id = plan_charge.plan_id " );
        query.append( "   AND plan_rank.plan_sub_id = plan_charge.plan_sub_id " );
        query.append( "   AND plan_rank.room_rank = plan_charge.room_rank " );
        // 部屋残数データ
        query.append( " INNER JOIN newRsvDB.hh_rsv_room_remainder remainder " );
        query.append( "   ON plan.id = remainder.id " );
        // プラン・部屋設定データ
        query.append( " INNER JOIN newRsvDB.hh_rsv_rel_plan_room plan_room " );
        query.append( "   ON plan.id = plan_room.id " );
        query.append( "   AND plan.plan_id = plan_room.plan_id " );
        query.append( "   AND plan.plan_sub_id = plan_room.plan_sub_id " );
        query.append( "   AND remainder.seq = plan_room.seq " );
        // 料金モード内訳
        query.append( " INNER JOIN newRsvDB.hh_rsv_charge_mode_breakdown breakdown " );
        query.append( "   ON plan_charge.id = breakdown.id " );
        query.append( "   AND plan_charge.plan_id = breakdown.plan_id " );
        query.append( "   AND plan_charge.plan_sub_id = breakdown.plan_sub_id " );
        query.append( "   AND plan_charge.plan_charge_mode_id = breakdown.plan_charge_mode_id " );
        if ( sales_status == 1 )// 販売中のみの絞り込み
        {
            // 部屋販売数データ
            query.append( " INNER JOIN newRsvDB.hh_rsv_day_charge day_charge " );
            query.append( "   ON plan.id = day_charge.id " );
            query.append( "   AND plan.plan_id = day_charge.plan_id " );
            query.append( "   AND plan.plan_sub_id = day_charge.plan_sub_id " );
            query.append( "   AND day_charge.cal_date = ? " );
            // ホテルカレンダーマスタ
            query.append( " INNER JOIN newRsvDB.hh_rsv_hotel_calendar calendar " );
            query.append( "   ON breakdown.id = calendar.id " );
            query.append( "   AND remainder.cal_date = calendar.cal_date " );
        }
        query.append( " WHERE plan.latest_flag = 1 " );
        query.append( "   AND plan.publishing_flag = 1  " );
        query.append( "   AND plan.plan_type IN (3,4) " );// 当日プランのみ
        query.append( "   AND plan.plan_sales_status IN (1,2) " );
        query.append( "   AND plan.id = ? " );
        /* 販売中のみの絞り込み */
        if ( sales_status == 1 )// 販売中のみの絞り込み
        {
            query.append( "   AND plan.plan_sales_status = 1 " );
            query.append( "   AND plan.sales_start_date = ? " );
        }
        else if ( sales_status == 2 )// 販売停止中のみの絞り込み
        {
            query.append( "   AND (plan.plan_sales_status = 2 " );
            query.append( "   OR plan.sales_start_date < ? )" );
        }
        query.append( "   AND remainder.cal_date = ? " );
        query.append( "   AND plan_charge.ci_time_to >= ? " );

        query.append( " GROUP BY plan.plan_id, plan.plan_sub_id " );
        query.append( " ORDER BY plan.disp_index, plan.plan_id " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            if ( sales_status == 1 )
            {
                prestate.setInt( i++, calDate );
            }
            prestate.setInt( i++, hotelId );
            if ( sales_status != 0 )
            {
                prestate.setInt( i++, calDate );
            }
            prestate.setInt( i++, calDate );
            prestate.setInt( i++, calTime );
            result = prestate.executeQuery();

            if ( result.last() == false )
            {
                return new int[0][0];
            }
            ArrayList<Integer> planId = new ArrayList<Integer>();
            ArrayList<Integer> plansubId = new ArrayList<Integer>();
            result.beforeFirst();
            while( result.next() )
            {
                planId.add( result.getInt( "plan_id" ) );
                plansubId.add( result.getInt( "plan_sub_id" ) );
            }
            if ( planId.isEmpty() )
            {
                return new int[0][0];
            }
            int len = planId.size();
            int[][] planIds = new int[len][2];
            for( int index = 0 ; index < len ; index++ )
            {
                planIds[index][0] = planId.get( index );
                planIds[index][1] = plansubId.get( index );
            }
            return planIds;

        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanData.searchPlans] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * プラン情報の取得
     * 
     * @param planIds
     * @param hotelId
     * @param date
     * @param request
     * @return
     * @throws Exception
     */
    public static void getPlanDetail(GenerateXmlRsvPlanDataSub gxRsvPlanDataSub, int hotelId, int planId, int planSubId) throws Exception
    {
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        int calDate = nowDate;
        int calTime = nowTime;
        if ( nowTime < 50000 )
        {
            calDate = DateEdit.addDay( nowDate, -1 );
            calTime = calTime + 240000;
        }
        int premiumMile = 50;
        int normalMile = 10;

        try
        {
            DataMasterPoint dmp = new DataMasterPoint();
            if ( dmp.getData( 1000007 ) )// 予約マイルのデータ
            {
                premiumMile = dmp.getAddPoint();
                normalMile = (int)(dmp.getAddPoint() * dmp.getFreeMultiple());
            }

            StringBuilder query = new StringBuilder();
            query.append( " SELECT DISTINCT " );
            query.append( "   plan.* " );
            query.append( "   , plan_charge.* " );
            // プランマスタ
            query.append( " FROM newRsvDB.hh_rsv_plan plan " );
            // プラン別料金マスタ
            query.append( " INNER JOIN newRsvDB.hh_rsv_plan_charge plan_charge " );
            query.append( "   ON plan.id = plan_charge.id " );
            query.append( "   AND plan.plan_id = plan_charge.plan_id " );
            query.append( "   AND plan.plan_sub_id = plan_charge.plan_sub_id " );
            // 料金モード内訳
            query.append( " INNER JOIN newRsvDB.hh_rsv_charge_mode_breakdown breakdown " );
            query.append( "   ON plan_charge.id = breakdown.id " );
            query.append( "   AND plan_charge.plan_id = breakdown.plan_id " );
            query.append( "   AND plan_charge.plan_sub_id = breakdown.plan_sub_id " );
            query.append( "   AND plan_charge.plan_charge_mode_id = breakdown.plan_charge_mode_id " );
            query.append( " WHERE " );
            query.append( " plan.id = ? " );
            query.append( " AND plan.plan_id = ? " );
            query.append( " AND plan.plan_sub_id = ? " );
            connection = DBConnection.getConnectionRO();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            result = prestate.executeQuery();

            if ( result.next() != false )
            {
                gxRsvPlanDataSub.setPlanId( Integer.toString( planId ) );
                gxRsvPlanDataSub.setPlanName( result.getString( "plan_name" ) );
                if ( result.getInt( "plan_type" ) == 1 || result.getInt( "plan_type" ) == 3 )
                {
                    gxRsvPlanDataSub.setPlanType( "宿泊" );
                }
                else if ( result.getInt( "plan_type" ) == 2 || result.getInt( "plan_type" ) == 4 )
                {
                    gxRsvPlanDataSub.setPlanType( "休憩" );
                }
                int stockValue = RsvStock.getStockPlan( hotelId, planId, planSubId, calDate );
                gxRsvPlanDataSub.setStockValue( Integer.toString( stockValue ) );
                if ( stockValue > 0 )
                {
                    gxRsvPlanDataSub.setStockText( "あと" + stockValue + "室" );
                }
                else
                {
                    gxRsvPlanDataSub.setStockText( "販売終了" );
                }
                gxRsvPlanDataSub.setBonusMileValue( Integer.toString( result.getInt( "bonus_mile" ) ) );
                gxRsvPlanDataSub.setBonusMileText( Integer.toString( result.getInt( "bonus_mile" ) ) );
                gxRsvPlanDataSub.setPremiumMile( Integer.toString( premiumMile ) );
                gxRsvPlanDataSub.setNormalMile( Integer.toString( normalMile ) );
                gxRsvPlanDataSub.setNote( result.getString( "precaution" ) );
                gxRsvPlanDataSub.setPlanPr( result.getString( "plan_pr" ) );

                /* チェックイン時間、チェックアウト時間 */
                int ciTimeFrom = result.getInt( "ci_time_from" );
                int ciTimeTo = result.getInt( "ci_time_to" );
                int comingFlag = result.getInt( "coming_flag" );
                int lastUptime = result.getInt( "last_uptime" );
                gxRsvPlanDataSub.setFromTime( Integer.toString( ciTimeFrom ) );
                gxRsvPlanDataSub.setToTime( Integer.toString( ciTimeTo ) );
                String checkInText = getCheckInText( calTime, nowTime, ciTimeFrom, ciTimeTo, lastUptime );
                gxRsvPlanDataSub.setCheckInText( checkInText );
                gxRsvPlanDataSub.setComingSoon( Integer.toString( comingFlag ) );
                int coTime = result.getInt( "co_time" );
                int coKind = result.getInt( "co_kind" );
                String checkOutText = getCheckOutText( nowTime, coTime, coKind );
                gxRsvPlanDataSub.setCheckOutText( checkOutText );

                // 人数
                int minStay = result.getInt( "min_stay_num" );
                int maxStay = result.getInt( "max_stay_num" );
                if ( minStay == maxStay )
                {
                    gxRsvPlanDataSub.setPeopleText( minStay + "名" );
                }
                else
                {
                    gxRsvPlanDataSub.setPeopleText( Math.max( minStay, 1 ) + "名～" + maxStay + "名" );
                }

                /* 追加料金テキスト */
                int adultAddChargeKind = result.getInt( "adult_add_charge_kind" );
                int adultAddCharge = result.getInt( "adult_add_charge" );
                int childAddChargeKind = result.getInt( "child_add_charge_kind" );
                int childAddCharge = result.getInt( "child_add_charge" );
                int maxStayNumChild = result.getInt( "max_stay_num_child" );
                String additionalChargesText = getAdditionalChargeText( minStay, maxStay, adultAddChargeKind, adultAddCharge, childAddChargeKind, childAddCharge, maxStayNumChild );
                gxRsvPlanDataSub.setAdditionalChargeText( additionalChargesText );

                gxRsvPlanDataSub.setPrePay( Integer.toString( result.getInt( "payment_kind" ) ) );
                gxRsvPlanDataSub.setPayAtHotel( Integer.toString( result.getInt( "local_payment_kind" ) ) );

                GenerateXmlRsvPlanDataPeopleOption gxPeopleOption;
                // 男性
                {
                    gxPeopleOption = new GenerateXmlRsvPlanDataPeopleOption();
                    gxPeopleOption.setLabel( "男性" );
                    gxPeopleOption.setNumberMin( Integer.toString( result.getInt( "min_stay_num_man" ) ) );
                    gxPeopleOption.setNumberMax( Integer.toString( result.getInt( "max_stay_num_man" ) ) );
                    gxRsvPlanDataSub.addPeopleOption( gxPeopleOption );

                }
                // 女性
                {
                    gxPeopleOption = new GenerateXmlRsvPlanDataPeopleOption();
                    gxPeopleOption.setLabel( "女性" );
                    gxPeopleOption.setNumberMin( Integer.toString( result.getInt( "min_stay_num_woman" ) ) );
                    gxPeopleOption.setNumberMax( Integer.toString( result.getInt( "max_stay_num_woman" ) ) );
                    gxRsvPlanDataSub.addPeopleOption( gxPeopleOption );
                }
                // 子供
                {
                    gxPeopleOption = new GenerateXmlRsvPlanDataPeopleOption();
                    gxPeopleOption.setLabel( "子供" );
                    gxPeopleOption.setNumberMin( "0" );
                    gxPeopleOption.setNumberMax( Integer.toString( result.getInt( "max_stay_num_child" ) ) );
                    gxRsvPlanDataSub.addPeopleOption( gxPeopleOption );
                }

            }

            /* 料金 */
            int roomSelectKind = result.getInt( "room_select_kind" );
            switch( roomSelectKind )
            {
                case ROOM_SELECT_KIND_RANK:
                    setChargeRank( connection, gxRsvPlanDataSub, hotelId, planId, planSubId, calDate );
                    break;
                case ROOM_SELECT_KIND_ROOM:
                    setChargeRoom( connection, gxRsvPlanDataSub, hotelId, planId, planSubId, calDate );
                    break;
                case ROOM_SELECT_KIND_NONE:
                    setChargeNoSelect( connection, gxRsvPlanDataSub, hotelId, planId, planSubId, calDate );
                    break;
                default:
                    throw new Exception( "部屋選択種別が不正です。" );
            }
            if ( result.getInt( "sales_start_date" ) < calDate )
            {
                gxRsvPlanDataSub.setSalesStatus( "2" );
            }
            else
            {
                gxRsvPlanDataSub.setSalesStatus( Integer.toString( result.getInt( "plan_sales_status" ) ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanData.getPlanDetail] Exception = " + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 料金の設定 - お客様に部屋ランクを選ばせる
     * 
     * @param connection
     * @param hotelId
     * @param plan
     * @param planSubId
     * @param date
     * @param planImageProp
     * @param baseUrl
     * @throws Exception
     */
    private static void setChargeRank(Connection connection, GenerateXmlRsvPlanDataSub gxSub, int hotelId, int planId, int planSubId, int calDate) throws Exception
    {
        Logging.info( "[RsvPlanData]setChargeRank hotelid:" + hotelId + ",planId:" + planId + ",planSubId=" + planSubId + ",calDate=" + calDate );

        GenerateXmlRsvPlanDataRoomRanks gxRoomRanks = new GenerateXmlRsvPlanDataRoomRanks();
        NumberFormat nFrmt = NumberFormat.getCurrencyInstance( Locale.JAPAN );

        StringBuilder query = new StringBuilder();
        query.append( " SELECT plan_charge.room_rank, plan_charge.plan_charge " );
        query.append( "   , roomrank.rank_name " );
        query.append( "   , plan.max_stay_num " );
        query.append( "   , plan.min_stay_num " );
        query.append( "   , plan.adult_add_charge_kind " );
        query.append( "   , plan.adult_add_charge " );
        query.append( "   , plan.child_add_charge_kind " );
        query.append( "   , plan.child_add_charge " );
        // プラン別料金マスタ
        query.append( " FROM newRsvDB.hh_rsv_plan_charge plan_charge " );
        // 料金モード内訳
        query.append( " INNER JOIN newRsvDB.hh_rsv_charge_mode_breakdown breakdown " );
        query.append( "   ON plan_charge.id = breakdown.id " );
        query.append( "   AND plan_charge.plan_id = breakdown.plan_id " );
        query.append( "   AND plan_charge.plan_sub_id = breakdown.plan_sub_id " );
        query.append( "   AND plan_charge.plan_charge_mode_id = breakdown.plan_charge_mode_id " );
        // プラン
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan plan " );
        query.append( "   ON plan_charge.id = plan.id " );
        query.append( "   AND plan_charge.plan_id = plan.plan_id " );
        query.append( "   AND plan_charge.plan_sub_id = plan.plan_sub_id " );
        // ホテルカレンダーマスタ
        query.append( " INNER JOIN newRsvDB.hh_rsv_hotel_calendar calendar " );
        query.append( "   ON breakdown.id = calendar.id " );
        // ホテル部屋ランク
        query.append( " INNER JOIN hh_hotel_roomrank roomrank " );
        query.append( "   ON plan_charge.id = roomrank.id " );
        query.append( "   AND plan_charge.room_rank = roomrank.room_rank " );
        // ホテル部屋
        query.append( " INNER JOIN hh_hotel_room_more roommore " );
        query.append( "   ON plan_charge.id = roommore.id " );
        query.append( "   AND plan_charge.room_rank = roommore.room_rank " );
        // プラン・ランク設定
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan_roomrank plan_rank " );
        query.append( "   ON plan_charge.id = plan_rank.id " );
        query.append( "   AND plan_charge.plan_id = plan_rank.plan_id " );
        query.append( "   AND plan_charge.plan_sub_id = plan_rank.plan_sub_id " );
        query.append( "   AND plan_charge.room_rank = plan_rank.room_rank " );
        query.append( " WHERE plan_charge.id = ? " );
        query.append( "   AND plan_charge.plan_id = ? " );
        query.append( "   AND plan_charge.plan_sub_id = ? " );
        query.append( "   AND calendar.cal_date = ? " );
        query.append( " GROUP BY plan_charge.room_rank, plan_charge.plan_charge " );
        query.append( "   , roomrank.rank_name " );
        query.append( " ORDER BY roomrank.disp_index, roomrank.room_rank " );

        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            prestate.setInt( i++, calDate );
            result = prestate.executeQuery();

            int maxCharge = Integer.MIN_VALUE;
            int minCharge = Integer.MAX_VALUE;
            if ( result != null )
            {
                while( result.next() )
                {
                    int rankNo = result.getInt( "room_rank" );

                    gxRoomRanks = new GenerateXmlRsvPlanDataRoomRanks();
                    gxRoomRanks.setRankId( String.valueOf( rankNo ) );
                    gxRoomRanks.setRankName( result.getString( "rank_name" ) );
                    gxRoomRanks.setCharge( Integer.toString( result.getInt( "plan_charge" ) ) );

                    int stock = RsvStock.getStockRank( hotelId, planId, planSubId, calDate, rankNo );
                    if ( stock <= 0 )
                    {
                        gxRoomRanks.setChargeText( "販売終了" );
                    }
                    else
                    {
                        gxRoomRanks.setChargeText( nFrmt.format( result.getInt( "plan_charge" ) ) );
                    }
                    gxSub.addRoomRanks( gxRoomRanks );
                    maxCharge = Math.max(
                            maxCharge,
                            result.getInt( "plan_charge" )
                                    + calcAddCharge( result.getInt( "plan_charge" ), result.getInt( "adult_add_charge_kind" ), result.getInt( "adult_add_charge" ), result.getInt( "max_stay_num" ), result.getInt( "min_stay_num" ) ) );
                    minCharge = Math.min(
                            minCharge,
                            result.getInt( "plan_charge" )
                                    + calcAddCharge( result.getInt( "plan_charge" ), result.getInt( "adult_add_charge_kind" ), result.getInt( "adult_add_charge" ), result.getInt( "max_stay_num" ), result.getInt( "min_stay_num" ) ) );
                }
                if ( maxCharge != Integer.MIN_VALUE )
                {
                    if ( maxCharge == minCharge )
                    {
                        gxSub.setChargeText( nFrmt.format( maxCharge ) );
                    }
                    else
                    {
                        gxSub.setChargeText( nFrmt.format( minCharge ) + "～" + nFrmt.format( maxCharge ) );
                    }

                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanData.setChargeRank] Exception = " + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 料金の設定 - お客様にお部屋を選ばせる
     * 
     * @param connection
     * @param hotelId
     * @param plan
     * @param planSubId
     * @param date
     * @param planImageProp
     * @param baseUrl
     * @param hotelImageProp
     * @throws Exception
     */
    private static void setChargeRoom(Connection connection, GenerateXmlRsvPlanDataSub gxSub, int hotelId, int planId,
            int planSubId, int calDate) throws Exception
    {
        Logging.info( "[RsvPlanData]setChargeRoom hotelid:" + hotelId + ",planId:" + planId + ",planSubId=" + planSubId + ",calDate=" + calDate );

        GenerateXmlRsvPlanDataRooms gxRooms = new GenerateXmlRsvPlanDataRooms();
        NumberFormat nFrmt = NumberFormat.getCurrencyInstance( Locale.JAPAN );

        StringBuilder query = new StringBuilder();
        query.append( " SELECT plan_charge.plan_charge, plan_charge.room_rank " );
        query.append( "   , room_more.seq , room_more.room_name, room_more.room_picture_pc " );
        query.append( "   , plan_room.image_file_name " );
        query.append( "   , plan.max_stay_num " );
        query.append( "   , plan.min_stay_num " );
        query.append( "   , plan.adult_add_charge_kind " );
        query.append( "   , plan.adult_add_charge " );
        query.append( "   , plan.child_add_charge_kind " );
        query.append( "   , plan.child_add_charge " );
        // プラン・部屋設定データ
        query.append( " FROM newRsvDB.hh_rsv_rel_plan_room plan_room " );
        // ホテル部屋
        query.append( " INNER JOIN hh_hotel_room_more room_more " );
        query.append( "   ON plan_room.id = room_more.id " );
        query.append( "   AND plan_room.seq = room_more.seq " );
        // ホテル部屋ランク
        query.append( " INNER JOIN hh_hotel_roomrank roomrank " );
        query.append( "   ON room_more.id = roomrank.id " );
        query.append( "   AND room_more.room_rank = roomrank.room_rank " );
        // プラン別料金マスタ
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan_charge plan_charge " );
        query.append( "   ON plan_room.id = plan_charge.id " );
        query.append( "   AND plan_room.plan_id = plan_charge.plan_id " );
        query.append( "   AND plan_room.plan_sub_id = plan_charge.plan_sub_id " );
        query.append( "   AND roomrank.room_rank = plan_charge.room_rank " );
        // プラン
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan plan " );
        query.append( "   ON plan_charge.id = plan.id " );
        query.append( "   AND plan_charge.plan_id = plan.plan_id " );
        query.append( "   AND plan_charge.plan_sub_id = plan.plan_sub_id " );
        // 料金モード内訳
        query.append( " INNER JOIN newRsvDB.hh_rsv_charge_mode_breakdown breakdown " );
        query.append( "   ON plan_charge.id = breakdown.id " );
        query.append( "   AND plan_charge.plan_id = breakdown.plan_id " );
        query.append( "   AND plan_charge.plan_sub_id = breakdown.plan_sub_id " );
        query.append( "   AND plan_charge.plan_charge_mode_id = breakdown.plan_charge_mode_id " );
        // ホテルカレンダーマスタ
        query.append( " INNER JOIN newRsvDB.hh_rsv_hotel_calendar calendar " );
        query.append( "   ON breakdown.id = calendar.id " );
        query.append( " WHERE plan_charge.id = ? " );
        query.append( "   AND plan_charge.plan_id = ? " );
        query.append( "   AND plan_charge.plan_sub_id = ? " );
        query.append( "   AND calendar.cal_date = ? " );
        query.append( " ORDER BY roomrank.disp_index, roomrank.room_rank, room_more.seq " );

        // Logging.debug( query.toString() );

        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            prestate.setInt( i++, calDate );
            result = prestate.executeQuery();

            int maxCharge = Integer.MIN_VALUE;
            int minCharge = Integer.MAX_VALUE;
            if ( result != null )
            {
                while( result.next() )
                {
                    int seq = result.getInt( "seq" );
                    gxRooms = new GenerateXmlRsvPlanDataRooms();
                    gxRooms.setRoomNo( String.valueOf( seq ) );
                    gxRooms.setRoomName( result.getString( "room_name" ) );
                    gxRooms.setCharge( Integer.toString( result.getInt( "plan_charge" ) ) );
                    int stock = RsvStock.getStockStatusRoom( hotelId, planId, planSubId, calDate, seq );
                    if ( stock <= 0 )
                    {
                        gxRooms.setChargeText( "売り切れ" );
                    }
                    else
                    {
                        gxRooms.setChargeText( nFrmt.format( result.getInt( "plan_charge" ) ) );
                    }
                    gxSub.addRooms( gxRooms );
                    maxCharge = Math.max(
                            maxCharge,
                            result.getInt( "plan_charge" )
                                    + calcAddCharge( result.getInt( "plan_charge" ), result.getInt( "adult_add_charge_kind" ), result.getInt( "adult_add_charge" ), result.getInt( "max_stay_num" ), result.getInt( "min_stay_num" ) ) );
                    minCharge = Math.min(
                            minCharge,
                            result.getInt( "plan_charge" )
                                    + calcAddCharge( result.getInt( "plan_charge" ), result.getInt( "adult_add_charge_kind" ), result.getInt( "adult_add_charge" ), result.getInt( "max_stay_num" ), result.getInt( "min_stay_num" ) ) );

                }
                if ( maxCharge != Integer.MIN_VALUE )
                {

                    if ( maxCharge == minCharge )
                    {
                        gxSub.setChargeText( nFrmt.format( maxCharge ) );
                    }
                    else
                    {
                        gxSub.setChargeText( nFrmt.format( minCharge ) + "～" + nFrmt.format( maxCharge ) );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvPlanData.setChargeRoom] Exception = " + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 料金の設定 - ランク/お部屋の指定なし
     * 
     * @param connection
     * @param hotelId
     * @param plan
     * @param planSubId
     * @param date
     * @throws Exception
     */
    private static void setChargeNoSelect(Connection connection, GenerateXmlRsvPlanDataSub gxSub, int hotelId, int planId,
            int planSubId, int calDate) throws Exception
    {
        Logging.info( "[RsvPlanData]setChargeNoSelect hotelid:" + hotelId + ",planId:" + planId + ",planSubId=" + planSubId + ",calDate=" + calDate );
        NumberFormat nFrmt = NumberFormat.getCurrencyInstance( Locale.JAPAN );

        StringBuilder query = new StringBuilder();
        query.append( " SELECT plan_charge.plan_charge " );
        query.append( "   , plan.max_stay_num " );
        query.append( "   , plan.min_stay_num " );
        query.append( "   , plan.adult_add_charge_kind " );
        query.append( "   , plan.adult_add_charge " );
        query.append( "   , plan.child_add_charge_kind " );
        query.append( "   , plan.child_add_charge " );
        // プラン別料金マスタ
        query.append( " FROM newRsvDB.hh_rsv_plan_charge plan_charge " );
        // 料金モード内訳
        query.append( " INNER JOIN newRsvDB.hh_rsv_charge_mode_breakdown breakdown " );
        query.append( "   ON plan_charge.id = breakdown.id " );
        query.append( "   AND plan_charge.plan_id = breakdown.plan_id " );
        query.append( "   AND plan_charge.plan_sub_id = breakdown.plan_sub_id " );
        query.append( "   AND plan_charge.plan_charge_mode_id = breakdown.plan_charge_mode_id " );
        // プラン
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan plan " );
        query.append( "   ON plan_charge.id = plan.id " );
        query.append( "   AND plan_charge.plan_id = plan.plan_id " );
        query.append( "   AND plan_charge.plan_sub_id = plan.plan_sub_id " );
        // ホテルカレンダーマスタ
        query.append( " INNER JOIN newRsvDB.hh_rsv_hotel_calendar calendar " );
        query.append( "   ON breakdown.id = calendar.id " );
        query.append( " WHERE plan_charge.id = ? " );
        query.append( "   AND plan_charge.plan_id = ? " );
        query.append( "   AND plan_charge.plan_sub_id = ? " );
        query.append( "   AND calendar.cal_date = ? " );

        // Logging.debug( query.toString() );

        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            // prestate.setInt( i++, 0 ); // room_rank
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            prestate.setInt( i++, calDate );
            result = prestate.executeQuery();

            int maxCharge = Integer.MIN_VALUE;
            int minCharge = Integer.MAX_VALUE;
            if ( result != null )
            {
                while( result.next() )
                {
                    maxCharge = Math.max(
                            maxCharge,
                            result.getInt( "plan_charge" )
                                    + calcAddCharge( result.getInt( "plan_charge" ), result.getInt( "adult_add_charge_kind" ), result.getInt( "adult_add_charge" ), result.getInt( "max_stay_num" ), result.getInt( "min_stay_num" ) ) );
                    minCharge = Math.min(
                            minCharge,
                            result.getInt( "plan_charge" )
                                    + calcAddCharge( result.getInt( "plan_charge" ), result.getInt( "adult_add_charge_kind" ), result.getInt( "adult_add_charge" ), result.getInt( "max_stay_num" ), result.getInt( "min_stay_num" ) ) );
                }
                if ( maxCharge != Integer.MIN_VALUE )
                {
                    if ( maxCharge == minCharge )
                    {
                        gxSub.setChargeText( nFrmt.format( maxCharge ) );
                    }
                    else
                    {
                        gxSub.setChargeText( nFrmt.format( minCharge ) + "～" + nFrmt.format( maxCharge ) );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicApiPlan.setChargeNoSelect] Exception = " + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 表示人数用追加料金の計算
     * 
     * @param charge
     * @param addChargeKind
     * @param addCharge
     * @param maxNum
     * @param minNum
     * @return calCharge 追加金額
     * @throws Exception
     */
    public static int calcAddCharge(int charge, int addChargeKind, int addCharge, int maxNum, int minNum) throws Exception
    {
        int calCharge = 0;
        calCharge = calcAddCharge( charge, addChargeKind, addCharge );
        if ( maxNum == 1 )
        {
            calCharge = calCharge * (-1);
        }
        else if ( minNum > 2 )
        {
            calCharge = calCharge * (minNum - 2);
        }
        else
        {
            calCharge = 0;
        }
        return calCharge;
    }

    /**
     * 追加料金の計算
     * 
     * @param planCharge
     * @param addChangeKind
     * @param addCharge
     * @return
     * @throws Exception
     */
    public static int calcAddCharge(int planCharge, int addChangeKind, int addCharge) throws Exception
    {

        if ( addChangeKind == ADD_CHARGE_KIND_NOT_ACCEPT )
        {
            return 0;
        }
        if ( addChangeKind == ADD_CHARGE_KIND_YEN )
        {
            return addCharge;
        }
        if ( addChangeKind != ADD_CHARGE_KIND_PERCENT )
        {
            throw new Exception( "料金追加区分未登録" );
        }

        BigDecimal percent = new BigDecimal( addCharge ).divide( new BigDecimal( 100 ) );
        BigDecimal charge = new BigDecimal( planCharge )
                .multiply( percent ) // 割合を掛ける(1234.5)
                .divide( new BigDecimal( 10 ) ) // 10で割る(123.45)
                .setScale( 0, BigDecimal.ROUND_UP ) // 小数第1位で切り上げ(124)
                .multiply( new BigDecimal( 10 ) ); // 10を掛ける(1240)

        return charge.intValue();
    }

    /**
     * チェックイン時刻テキストの取得
     */
    private static String getCheckInText(int calTime, int nowTime, int ciTimeFrom, int ciTimeTo, int lastUptime)
    {
        // 到着時刻From
        String ciFrom;
        // 到着時刻To
        String ciTo;

        ciFrom = ConvertTime.convTimeStr( ciTimeFrom, 3 );
        ciTo = ConvertTime.convTimeStr( ciTimeTo, 3 );

        return(ciFrom + "～" + ciTo);
    }

    /**
     * チェックアウト時刻テキストの取得
     */
    private static String getCheckOutText(int nowTime, int coTime, int coKind)
    {
        String checkOutText = "";
        int coTime12 = coTime; // 12時間表記用
        if ( coTime > 240000 && nowTime <= 50000 )
        {
            coTime12 = coTime % 240000;
        }

        // チェックアウト時刻テキスト
        if ( coKind == CO_KIND_TO_TIME )
        {
            if ( ConvertTime.convTimeMM( coTime12 ).equals( "00" ) )
            {
                checkOutText = ConvertTime.convTimeHH( coTime12 ) + "時まで";
            }
            else
            {
                checkOutText = ConvertTime.convTimeHH( coTime12 ) + "時" + ConvertTime.convTimeMM( coTime12 ) + "分まで";
            }
        }
        else if ( coKind == CO_KIND_FROM_CHECKIN )
        {
            checkOutText = "チェックインから" + DateEdit.formatTime( 6, coTime ) + "後";
        }
        return checkOutText;
    }

    /**
     * 追加料金テキストの取得
     * 
     * @param id
     * @param seq
     * @return
     */
    private static String getAdditionalChargeText(int minStay, int maxStay, int adultAddChargeKind, int adultAddCharge, int childAddChargeKind, int childAddCharge, int maxStayNumChild)
    {
        // 基本人数
        int basicNum = 2;
        if ( maxStay == 1 )
        {
            basicNum = 1;
        }
        else
        {
            basicNum = Math.max( minStay, 2 );
        }

        StringBuilder additionalChargesText = new StringBuilder();
        additionalChargesText.append( "" );
        if ( (adultAddChargeKind != ADD_CHARGE_KIND_NOT_ACCEPT || childAddChargeKind != ADD_CHARGE_KIND_NOT_ACCEPT) && minStay != maxStay )
        {
            additionalChargesText.append( "【人数追加の割増料金】\n" );
            switch( adultAddChargeKind )
            {
                case ADD_CHARGE_KIND_PERCENT:
                    additionalChargesText.append( "大人１名追加：" );
                    additionalChargesText.append( NumberFormat.getNumberInstance().format( (int)(Math.ceil( adultAddCharge * (100 / (100 + ((double)basicNum - 2) * (double)adultAddCharge)) )) ) );
                    additionalChargesText.append( "％増し\n" );
                    break;
                case ADD_CHARGE_KIND_YEN:
                    additionalChargesText.append( "大人１名追加：" );
                    additionalChargesText.append( NumberFormat.getNumberInstance().format( adultAddCharge ) );
                    additionalChargesText.append( "円増し\n" );
                    break;
                case ADD_CHARGE_KIND_NOT_ACCEPT:
                default:
                    additionalChargesText.append( "大人追加：受け付けない\n" );
                    break;
            }
            if ( maxStayNumChild > 0 )
            {
                switch( childAddChargeKind )
                {
                    case ADD_CHARGE_KIND_PERCENT:
                        additionalChargesText.append( "子供１名追加：" );
                        additionalChargesText.append( NumberFormat.getNumberInstance().format( childAddCharge * (100 / (100 + (basicNum - 2) * childAddCharge)) ) );
                        additionalChargesText.append( "％増し\n" );
                        break;
                    case ADD_CHARGE_KIND_YEN:
                        additionalChargesText.append( "子供１名追加：" );
                        additionalChargesText.append( NumberFormat.getNumberInstance().format( childAddCharge ) );
                        additionalChargesText.append( "円増し\n" );
                        break;
                    case ADD_CHARGE_KIND_NOT_ACCEPT:
                    default:
                        additionalChargesText.append( "子供追加：受け付けない" );
                        break;
                }
            }
        }
        return additionalChargesText.toString();
    }

}
