package jp.happyhotel.others;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHhRsvPlan;
import jp.happyhotel.data.DataHhRsvRoomRemainder;
import jp.happyhotel.data.DataHotelRoomMore;

public class RsvStock
{
    /** 在庫ステータス - 販売可 */
    public static final int                    STOCK_STATUS_ON_SALE    = 0;
    /** 在庫ステータス - 売り切れ */
    public static final int                    STOCK_STATUS_SOLD_OUT   = 6; // warn.00006
    /** 在庫ステータス - 販売枠上限 */
    public static final int                    STOCK_STATUS_RSV_LIMIT  = 14; // warn.00014

    /** 在庫ステータス（内部用） - 売り切れ */
    private static final int                   _STOCK_STATUS_SOLD_OUT  = -1;
    /** 在庫ステータス（内部用） - 販売枠上限 */
    private static final int                   _STOCK_STATUS_RSV_LIMIT = -2;
    /** 内部用の在庫ステータスと返却用の在庫ステータスの紐づけ */
    private static final Map<Integer, Integer> _STOCK_STATUS_MAP;
    static
    {
        Map<Integer, Integer> tmp = new HashMap<Integer, Integer>();
        tmp.put( _STOCK_STATUS_SOLD_OUT, STOCK_STATUS_SOLD_OUT );
        tmp.put( _STOCK_STATUS_RSV_LIMIT, STOCK_STATUS_RSV_LIMIT );
        _STOCK_STATUS_MAP = Collections.unmodifiableMap( tmp );
    }

    /** 部屋選択区分 */
    public static final Map<Integer, String>   ROOM_SELECT_KIND;
    /** 部屋選択区分 - お客様に部屋ランクを選ばせる */
    public static final int                    ROOM_SELECT_KIND_RANK   = 1;
    /** 部屋選択区分 - お客様にお部屋を選ばせる */
    public static final int                    ROOM_SELECT_KIND_ROOM   = 2;
    /** 部屋選択区分 - ランク/お部屋の指定なし */
    public static final int                    ROOM_SELECT_KIND_NONE   = 3;
    static
    {
        Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
        tmp.put( 1, "お客様に部屋ランクを選ばせる" );
        tmp.put( 2, "お客様にお部屋を選ばせる" );
        tmp.put( 3, "ランク/お部屋の指定なし" );
        ROOM_SELECT_KIND = Collections.unmodifiableMap( tmp );
    }
    /** プラン種別 */
    public static final Map<Integer, String>   PLAN_TYPE;
    /** プラン種別 - 宿泊プラン */
    public static final int                    PLAN_TYPE_STAY          = 1;
    /** プラン種別 - 休憩・フリータイム */
    public static final int                    PLAN_TYPE_REST          = 2;
    /** プラン種別 - 当日限定（宿泊プラン） */
    public static final int                    PLAN_TYPE_TODAY_STAY    = 3;
    /** プラン種別 - 当日限定（休憩・フリータイム） */
    public static final int                    PLAN_TYPE_TODAY_REST    = 4;
    static
    {
        Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
        tmp.put( PLAN_TYPE_STAY, "宿泊プラン" );
        tmp.put( PLAN_TYPE_REST, "休憩・フリータイム" );
        tmp.put( PLAN_TYPE_TODAY_STAY, "当日限定（宿泊プラン）" );
        tmp.put( PLAN_TYPE_TODAY_REST, "当日限定（休憩・フリータイム）" );
        PLAN_TYPE = Collections.unmodifiableMap( tmp );
    }
    /** 部屋残数ステータス */
    public static final Map<Integer, String>   ROOM_STATUS;
    /** 部屋残数ステータス - 空き */
    public static final int                    ROOM_STATUS_VACANT      = 1;
    /** 部屋残数ステータス - 来店済み */
    public static final int                    ROOM_STATUS_COMING      = 2;
    /** 部屋残数ステータス - 売り止め */
    public static final int                    ROOM_STATUS_STOP        = 3;
    static
    {
        Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
        tmp.put( ROOM_STATUS_VACANT, "空き" );
        tmp.put( ROOM_STATUS_COMING, "来店済み" );
        tmp.put( ROOM_STATUS_STOP, "売り止め" );
        ROOM_STATUS = Collections.unmodifiableMap( tmp );
    }

    /**
     * 指定日、指定プランの在庫数を返す
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @return
     * @throws Exception
     */
    public static int getStockPlan(int hotelId, int planId, int planSubId, int calDate) throws Exception
    {
        return Math.max( 0, _getStockPlan( hotelId, planId, planSubId, calDate ) );
    }

    /**
     * 指定日、指定プラン、指定ランクの在庫数を返す
     * 部屋選択種別が「選択させない」の場合は部屋ランク無視される
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param roomRank
     * @return
     * @throws Exception
     */
    public static int getStockRank(int hotelId, int planId, int planSubId, int calDate, int roomRank) throws Exception
    {
        return Math.max( 0, _getStockRank( hotelId, planId, planSubId, calDate, roomRank ) );
    }

    /**
     * 指定日、指定プラン、指定部屋の在庫数を返す
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param seq
     * @return
     * @throws Exception
     */
    public static int getStockRoom(int hotelId, int planId, int planSubId, int calDate, int seq) throws Exception
    {
        return Math.max( 0, _getStockRoom( hotelId, planId, planSubId, calDate, seq ) );
    }

    /**
     * 指定日、指定プランの在庫ステータスを返す
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @return
     * @throws Exception
     */
    public static int getStockStatusPlan(int hotelId, int planId, int planSubId, int calDate) throws Exception
    {
        int stock = _getStockPlan( hotelId, planId, planSubId, calDate );
        if ( stock > 0 )
        {
            return STOCK_STATUS_ON_SALE;
        }
        return _STOCK_STATUS_MAP.get( stock );
    }

    /**
     * 指定日、指定プラン、指定ランクの在庫ステータスを返す
     * 部屋選択種別が「選択させない」の場合は部屋ランク無視される
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param roomRank
     * @return
     * @throws Exception
     */
    public static int getStockStatusRank(int hotelId, int planId, int planSubId, int calDate, int roomRank) throws Exception
    {
        int stock = _getStockRank( hotelId, planId, planSubId, calDate, roomRank );
        if ( stock > 0 )
        {
            return STOCK_STATUS_ON_SALE;
        }
        return _STOCK_STATUS_MAP.get( stock );
    }

    /**
     * 指定日、指定プラン、指定部屋の在庫ステータスを返す
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param seq
     * @return
     * @throws Exception
     */
    public static int getStockStatusRoom(int hotelId, int planId, int planSubId, int calDate, int seq) throws Exception
    {
        int stock = _getStockRoom( hotelId, planId, planSubId, calDate, seq );
        if ( stock > 0 )
        {
            return STOCK_STATUS_ON_SALE;
        }
        return _STOCK_STATUS_MAP.get( stock );
    }

    /**
     * プランの部屋選択種別を取得する
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @return
     * @throws Exception
     */
    private static int getRoomSelectKind(int hotelId, int planId, int planSubId) throws Exception
    {
        DataHhRsvPlan plan = new DataHhRsvPlan();
        if ( plan.getData( hotelId, planId, planSubId ) == false )
        {
            Logging.error( "[StockCommon.getRoomSelectKind]Plan not found.[id=" + hotelId + ", plan_id=" + planId + ", plan_sub_id=" + planSubId + "]" );
            throw new Exception();
        }
        return plan.getRoomSelectKind();
    }

    /**
     * 指定日、指定プランの在庫状況を返す
     * 0 < : 在庫数
     * 0 > : 内部の在庫ステータス
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param reserveTempNo
     * @return
     * @throws Exception
     */
    private static int _getStockPlan(int hotelId, int planId, int planSubId, int calDate) throws Exception
    {
        int roomSelectKind = getRoomSelectKind( hotelId, planId, planSubId );
        if ( roomSelectKind == ROOM_SELECT_KIND_NONE )
        {
            int stock = getStockRank( hotelId, planId, planSubId, calDate, 0 );
            return stock > 0 ? stock : _STOCK_STATUS_SOLD_OUT;
        }

        // ホテルの全部屋ランクの取得
        DataHotelRoomMore roomMore = new DataHotelRoomMore();
        List<Integer> roomRanks = roomMore.getRoomRanks( hotelId );
        if ( roomRanks == null )
        {
            throw new Exception();
        }

        // ホテルのランクごとの在庫数を合算
        int stock = 0;
        for( int roomRank : roomRanks )
        {
            stock += getStockRank( hotelId, planId, planSubId, calDate, roomRank );
        }

        return stock > 0 ? stock : _STOCK_STATUS_SOLD_OUT;
    }

    /**
     * 指定日、指定プラン、指定ランクの在庫数を返す
     * プランの部屋選択種別と部屋ランクの整合性チェックは行わない
     * 部屋選択種別が「選択させない」の場合は部屋ランクは「ゼロ」であるべきなど
     * 0 < : 在庫数
     * 0 > : 内部の在庫ステータス
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param roomRank
     * @param reserveTempNo
     * @return
     * @throws Exception
     */
    private static int _getStockRank(int hotelId, int planId, int planSubId, int calDate, int roomRank) throws Exception
    {
        // Logging.info( "START getStockRank  planId=" + planId );
        int roomSelectKind = getRoomSelectKind( hotelId, planId, planSubId );
        if ( roomSelectKind == ROOM_SELECT_KIND_NONE )
        {
            // プランの販売数
            int realSaleRoomsQuantity = getRealSaleRoomsQuantity( hotelId, planId, planSubId, calDate, 0 );
            // Logging.info( "hotelId=" + hotelId + ",planId=" + planId + ",planSubId=" + planSubId + ",calDate=" + calDate + ",realSaleRoomsQuantity=" + realSaleRoomsQuantity, "stockCommon" );
            // プランの販売状況
            int soldCountPlanRank = getSoldCountPlanRank( hotelId, planId, planSubId, calDate, 0 );
            // Logging.info( "hotelId=" + hotelId + ",planId=" + planId + ",planSubId=" + planSubId + ",calDate=" + calDate + ",soldCountPlanRank=" + soldCountPlanRank, "stockCommon" );
            if ( realSaleRoomsQuantity - soldCountPlanRank < 1 )
            {
                // プランで設定された販売数を売り切った
                return _STOCK_STATUS_RSV_LIMIT;
            }

            // ランクの販売状況
            int soldCountRank = getSoldCountRankPlan( hotelId, planId, planSubId, calDate, 0 );
            // Logging.info( "hotelId=" + hotelId + ",planId=" + planId + ",planSubId=" + planSubId + ",calDate=" + calDate + ",soldCountRank=" + soldCountRank, "stockCommon" );
            int soldCountRoomRank = getSalesCountRoomByRank( hotelId, planId, planSubId, roomRank );
            // Logging.info( "hotelId=" + hotelId + ",planId=" + planId + ",planSubId=" + planSubId + ",roomRank=" + roomRank + ",soldCountRoomRank=" + soldCountRoomRank, "stockCommon" );
            if ( soldCountRoomRank - soldCountRank < 1 )
            {
                // ランクに属する部屋がすべて販売済み
                return _STOCK_STATUS_SOLD_OUT;
            }
            // Logging.info( "hotelId=" + hotelId + ",planId=" + planId + ",roomSelectKind=" + roomSelectKind + ",realSaleRoomsQuantity=" + realSaleRoomsQuantity + ",soldCountRoomRank=" + soldCountRoomRank + ",soldCountRank=" + soldCountRank
            // + ",soldCountPlanRank=" + soldCountPlanRank );

            // ①realSaleRoomsQuantity:販売数
            // ②soldCountPlanRank:プラン割り振られている部屋のうち、同プランで販売した販売数
            // ③soldCountRoomRank:プランに紐づく部屋数
            // ④soldCountRank:プラン割り振られている部屋で販売された販売数

            return Math.min( realSaleRoomsQuantity - soldCountPlanRank, soldCountRoomRank - soldCountRank );
            // return Math.min( realSaleRoomsQuantity, realSaleRoomsQuantity - soldCountPlanRank );
            // return Math.min( realSaleRoomsQuantity, soldCountRoomRank - soldCountRank );
            // return Math.min( realSaleRoomsQuantity, realSaleRoomsQuantity - soldCountRank );
        }
        else
        {

            // プランの販売数
            int realSaleRoomsQuantity = getRealSaleRoomsQuantity( hotelId, planId, planSubId, calDate, roomRank );
            // Logging.info( "realSaleRoomsQuantity=" + realSaleRoomsQuantity );
            // プランの販売状況
            int soldCountPlanRank = getSoldCountPlanRank( hotelId, planId, planSubId, calDate, roomRank );
            // Logging.info( "soldCountPlanRank=" + soldCountPlanRank );
            if ( realSaleRoomsQuantity - soldCountPlanRank < 1 )
            {
                // プランで設定された販売数を売り切った
                return _STOCK_STATUS_RSV_LIMIT;
            }

            // ランクの販売状況
            int soldCountRank = getSoldCountRankPlan( hotelId, planId, planSubId, calDate, roomRank );
            // Logging.info( "soldCountRank=" + soldCountRank );
            int soldCountRoomRank = getSalesCountRoomByRank( hotelId, planId, planSubId, roomRank );
            // Logging.info( "soldCountRoomRank=" + soldCountRoomRank );
            if ( (soldCountRoomRank - soldCountRank) < 1 )
            {
                // ランクに属する部屋がすべて販売済み
                return _STOCK_STATUS_SOLD_OUT;
            }
            // Logging.info( "hotelId=" + hotelId + ",planId=" + planId + ",roomSelectKind=" + roomSelectKind + ",realSaleRoomsQuantity=" + realSaleRoomsQuantity + ",soldCountRoomRank=" + soldCountRoomRank + ",soldCountRank=" + soldCountRank
            // + ",soldCountPlanRank=" + soldCountPlanRank );

            return Math.min( realSaleRoomsQuantity - soldCountPlanRank, soldCountRoomRank - soldCountRank );
        }
    }

    /**
     * 指定日、指定プラン、指定部屋の在庫数を返す
     * 0 < : 在庫数
     * 0 > : 内部の在庫ステータス
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param seq
     * @param reserveTempNo
     * @return
     * @throws Exception
     */
    private static int _getStockRoom(int hotelId, int planId, int planSubId, int calDate, int seq) throws Exception
    {
        // 部屋の販売数
        int salesCountRoom = getSalesCountRoom( hotelId, planId, planSubId, calDate, seq );
        if ( salesCountRoom > 0 )
        {
            // すでに販売済みのため、在庫はゼロ
            return _STOCK_STATUS_SOLD_OUT;
        }

        // 部屋の属する部屋ランクの取得
        int roomRank = 0;
        {
            DataHotelRoomMore roomMore = new DataHotelRoomMore();
            if ( roomMore.getData( hotelId, seq ) == false )
            {
                throw new Exception();
            }
            roomRank = roomMore.getRoomRank();
        }

        // 部屋の属する部屋ランクの在庫数取得
        int stockRank = getStockRank( hotelId, planId, planSubId, calDate, roomRank );

        // ランクに在庫があれば 1（部屋の在庫） 、なければ 0
        return stockRank > 0 ? 1 : _STOCK_STATUS_RSV_LIMIT;
    }

    /**
     * 指定日、指定プラン、指定ランクの実販売数を返す（販売可能数ではない）
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param roomRank
     * @return
     * @throws Exception
     */
    private static int getRealSaleRoomsQuantity(int hotelId, int planId, int planSubId, int calDate, int roomRank) throws Exception
    {
        int saleRoomsQuantity = getSaleRoomsQuantity( hotelId, planId, planSubId, calDate, roomRank );
        int roomCount = getPlanRoomCount( hotelId, planId, planSubId, roomRank );
        return Math.min( saleRoomsQuantity, roomCount );
    }

    /**
     * 指定日、指定プラン、指定ランクの日別販売数を返す
     * 
     * @param type
     * @param mode
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param date
     * @param key
     * @return
     * @throws Exception
     */
    private static int getSaleRoomsQuantity(int hotelId, int planId, int planSubId, int calDate, int roomRank) throws Exception
    {
        StringBuilder query = new StringBuilder();
        query.append( " SELECT " );
        query.append( "   day_charge.sale_rooms_quantity " );
        // 日別販売数
        query.append( " FROM newRsvDB.hh_rsv_day_charge day_charge " );
        // ホテルカレンダーマスタ
        query.append( " INNER JOIN newRsvDB.hh_rsv_hotel_calendar hotel_calendar " );
        query.append( "   ON day_charge.id = hotel_calendar.id " );
        // 料金モード内訳
        query.append( " INNER JOIN newRsvDB.hh_rsv_charge_mode_breakdown breakdown " );
        query.append( "   ON day_charge.id = breakdown.id " );
        query.append( "   AND day_charge.plan_id = breakdown.plan_id " );
        query.append( "   AND day_charge.plan_sub_id = breakdown.plan_sub_id " );
        query.append( " WHERE day_charge.id = ? " );
        query.append( "   AND day_charge.plan_id = ? " );
        query.append( "   AND day_charge.plan_sub_id = ? " );
        query.append( "   AND day_charge.room_rank = ? " );
        query.append( "   AND day_charge.cal_date = ? " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            prestate.setInt( i++, roomRank );
            prestate.setInt( i++, calDate );
            result = prestate.executeQuery();

            if ( result.next() == false )
            {
                return 0;
            }
            return result.getInt( "sale_rooms_quantity" );

        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getSaleRoomsQuantity] Exception = " + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 指定プラン、指定ランクでプランに紐づいている部屋数を返す
     * roomRank に 0 を設定した場合はランクを考慮しない
     * プランの部屋選択種別との整合性はチェックしない
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param roomRank
     * @return
     * @throws Exception
     */
    private static int getPlanRoomCount(int hotelId, int planId, int planSubId, int roomRank) throws Exception
    {
        StringBuilder query = new StringBuilder();
        query.append( " SELECT count(*) room_count " );
        // プラン・部屋設定データ
        query.append( " FROM newRsvDB.hh_rsv_rel_plan_room plan_room " );
        // ホテル部屋
        query.append( " INNER JOIN hh_hotel_room_more room_more " );
        query.append( "   ON plan_room.id = room_more.id " );
        query.append( "   AND plan_room.seq = room_more.seq " );
        query.append( " WHERE plan_room.id = ? " );
        query.append( "   AND plan_room.plan_id = ? " );
        query.append( "   AND plan_room.plan_sub_id = ? " );
        if ( roomRank > 0 )
        {
            query.append( "   AND room_more.room_rank = ? " );
        }

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            if ( roomRank > 0 )
            {
                prestate.setInt( i++, roomRank );
            }
            result = prestate.executeQuery();

            if ( result.next() == false )
            {
                return 0;
            }
            return result.getInt( "room_count" );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getStock] Exception = " + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 指定プランに紐づいているランクの販売数を返す
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param roomRank
     * @param reserveTempNo
     * @return
     * @throws Exception
     * @see RsvStock#getSalesCountRank(int, int, int, int)
     */
    private static int getSoldCountRankPlan(int hotelId, int planId, int planSubId, int calDate, int roomRank) throws Exception
    {
        // プラン情報から宿泊/休憩区分を取得
        int planType = 0;
        {
            DataHhRsvPlan plan = new DataHhRsvPlan();
            if ( plan.getData( hotelId, planId, planSubId ) == false )
            {
                throw new Exception();
            }
            planType = plan.getPlanType();
        }

        StringBuilder query = new StringBuilder();
        query.append( " SELECT count(*) sold_count " );
        query.append( " FROM newRsvDB.hh_rsv_room_remainder remainder " ); // 部屋残数データ
        query.append( " INNER JOIN hh_hotel_room_more room_more " ); // ホテル部屋
        query.append( "   ON remainder.id = room_more.id " );
        query.append( "   AND remainder.seq = room_more.seq " );
        query.append( " INNER JOIN newRsvDB.hh_rsv_rel_plan_room plan_room " ); // プラン・部屋設定データ
        query.append( "   ON room_more.id = plan_room.id " );
        query.append( "   AND room_more.seq = plan_room.seq " );
        query.append( " WHERE ( " );
        if ( planType == PLAN_TYPE_STAY || planType == PLAN_TYPE_TODAY_STAY )
        {
            query.append( "   remainder.stay_status <> ? " );
            query.append( "   OR ( " );
            query.append( "     remainder.stay_reserve_no is not null " );
            query.append( "     AND remainder.stay_reserve_no <> '' " );
            query.append( "   ) " );
        }
        else if ( planType == PLAN_TYPE_REST || planType == PLAN_TYPE_TODAY_REST )
        {
            query.append( "   remainder.rest_status <> ? " );
            query.append( "   OR ( " );
            query.append( "     remainder.rest_reserve_no is not null " );
            query.append( "     AND remainder.rest_reserve_no <> '' " );
            query.append( "   ) " );
        }
        else
        {
            throw new Exception();
        }
        query.append( " ) AND remainder.id = ? " );
        query.append( " AND remainder.cal_date = ? " );
        query.append( " AND plan_room.plan_id = ? " );
        query.append( " AND plan_room.plan_sub_id = ? " );
        if ( roomRank > 0 )
        {
            query.append( " AND room_more.room_rank = ? " );
        }

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, ROOM_STATUS_VACANT );
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, calDate );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            if ( roomRank > 0 )
            {
                prestate.setInt( i++, roomRank );
            }
            result = prestate.executeQuery();

            if ( result.next() == false )
            {
                throw new Exception();
            }
            return result.getInt( "sold_count" );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getSalesCountRank] Exception = " + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 指定日、指定プラン、指定ランクの販売済数（売り止め、仮予約を含む）を返す
     * roomRank に 0 を設定した場合はランクを考慮しない
     * プランの部屋選択種別との整合性はチェックしない
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param reserveTempNo
     * @param date
     * @return
     * @throws Exception
     */
    private static int getSoldCountPlanRank(int hotelId, int planId, int planSubId, int calDate, int roomRank) throws Exception
    {
        StringBuilder query = new StringBuilder();
        query.append( " SELECT count(*) sales_count " );
        // 部屋残数データ
        query.append( " FROM newRsvDB.hh_rsv_room_remainder remainder " );
        // ホテル部屋
        query.append( " INNER JOIN hh_hotel_room_more room_more " );
        query.append( " ON remainder.id = room_more.id " );
        query.append( " AND remainder.seq = room_more.seq " );
        query.append( " WHERE " );
        query.append( "   remainder.id = ? " );
        query.append( "   AND remainder.cal_date = ? " );
        query.append( "   AND ( " );
        // 宿泊予約番号
        query.append( "     EXISTS ( " );
        query.append( "       SELECT * FROM newRsvDB.hh_rsv_reserve reserve " );
        query.append( "       WHERE reserve.id = remainder.id AND reserve.reserve_no = remainder.stay_reserve_no " );
        query.append( "         AND reserve.plan_id = ? " ); // プラン枝番は考慮不要
        query.append( "     ) " );
        // 休憩予約番号
        query.append( "     OR EXISTS ( " );
        query.append( "       SELECT * FROM newRsvDB.hh_rsv_reserve reserve " );
        query.append( "       WHERE reserve.id = remainder.id AND reserve.reserve_no = remainder.rest_reserve_no " );
        query.append( "         AND reserve.plan_id = ? " ); // プラン枝番は考慮不要
        query.append( "     ) " );
        // 売り止め
        query.append( "     OR remainder.stay_status = ? " );
        query.append( "     OR remainder.rest_status = ? " );
        query.append( "   ) " );

        if ( roomRank > 0 )
        {
            query.append( "   AND room_more.room_rank = ? " );
        }

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );

            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, calDate );
            // 宿泊予約番号
            prestate.setInt( i++, planId );
            // 休憩予約番号
            prestate.setInt( i++, planId );
            // 売り止め
            prestate.setInt( i++, ROOM_STATUS_STOP );
            prestate.setInt( i++, ROOM_STATUS_STOP );

            if ( roomRank > 0 )
            {
                prestate.setInt( i++, roomRank );
            }

            result = prestate.executeQuery();

            if ( result.next() == false )
            {
                throw new Exception();
            }
            return result.getInt( "sales_count" );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getStock] Exception = " + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

    }

    /**
     * 部屋の販売数を返す
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param seq
     * @param reserveTempNo
     * @return
     * @throws Exception
     * @see RsvStock#getSalesCountRoom(int, int, int, int)
     */
    private static int getSalesCountRoom(int hotelId, int planId, int planSubId, int calDate, int seq) throws Exception
    {
        // プラン情報から宿泊/休憩区分を取得
        int planType = 0;
        {
            DataHhRsvPlan plan = new DataHhRsvPlan();
            if ( plan.getData( hotelId, planId, planSubId ) == false )
            {
                throw new Exception();
            }
            planType = plan.getPlanType();
        }
        return getSalesCountRoom( hotelId, planType, calDate, seq );
    }

    /**
     * 部屋の販売数を返す
     * （1: 販売済み、0: 空き部屋）
     * 
     * @param planType
     * @param hotelId
     * @param calDate
     * @param seq
     * @param reserveTempNo
     * @return
     * @throws Exception
     */
    private static int getSalesCountRoom(int hotelId, int planType, int calDate, int seq) throws Exception
    {
        /*
         * 部屋残数の情報取得
         */
        int roomStatus = 0;
        String reserveNo = "";
        {
            DataHhRsvRoomRemainder remainder = new DataHhRsvRoomRemainder();
            if ( remainder.getData( hotelId, calDate, seq ) == false )
            {
                throw new Exception();
            }
            // 宿泊
            if ( planType == PLAN_TYPE_STAY || planType == PLAN_TYPE_TODAY_STAY )
            {
                roomStatus = remainder.getStayStatus();
                reserveNo = remainder.getStayReserveNo();
            }
            // 休憩
            else if ( planType == PLAN_TYPE_REST || planType == PLAN_TYPE_TODAY_REST )
            {
                roomStatus = remainder.getRestStatus();
                reserveNo = remainder.getRestReserveNo();
            }
            else
            {
                throw new Exception();
            }
        }

        // 「空き」でない場合は販売数 1
        if ( roomStatus != ROOM_STATUS_VACANT )
        {
            return 1;
        }

        // 予約番号が入っていたら販売数 1
        if ( !reserveNo.equals( "" ) )
        {
            return 1;
        }

        // 空いている部屋（＝販売数はゼロ）
        return 0;
    }

    /**
     * 部屋ランクの販売数を返す
     * 
     * @param hotelId
     * @param planId
     * @param roomRank
     * @return
     * @throws Exception
     * @see RsvStock#getSalesCountRoomByRank(int, int, int, int)
     */
    private static int getSalesCountRoomByRank(int hotelId, int planId, int planSubId, int roomRank) throws Exception
    {
        StringBuilder query = new StringBuilder();
        query.append( " SELECT count(*) sales_count " );
        // 部屋残数データ
        query.append( " FROM newRsvDB.hh_rsv_rel_plan_room rel " );
        // ホテル部屋
        query.append( " INNER JOIN hh_hotel_room_more room_more " );
        query.append( " ON rel.id = room_more.id " );
        query.append( " AND rel.seq = room_more.seq " );
        query.append( " WHERE " );
        query.append( "   rel.id = ? " );
        query.append( "   AND rel.plan_id = ? " );
        query.append( "   AND rel.plan_sub_id = ? " );
        if ( roomRank > 0 )
        {
            query.append( " AND room_more.room_rank = ? " );
        }

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );

            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            if ( roomRank > 0 )
            {
                prestate.setInt( i++, roomRank );
            }

            result = prestate.executeQuery();

            if ( result.next() == false )
            {
                throw new Exception();
            }
            return result.getInt( "sales_count" );
        }
        catch ( Exception e )
        {
            Logging.error( "[ReserveCommon.getSalesCountRoomByRank] Exception = " + e.toString(), e );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }
}
