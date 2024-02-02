package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TimeCommon implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1812896216440070785L;

    private int               plan_id;                                /* プランID */
    private int               plan_sub_id;                            /* プランサブID */
    private int               ci_time_from;                           /* チェックイン開始時刻 */
    private int               ci_time_to;                             /* チェックイン終了時刻 */
    private int               co_time;                                /* チェックアウト時刻 */
    private int               est_time_arrival;                       /* 到着予定時刻 */
    private int               interval;                               /* 予約時間間隔 */
    private int               last_update;                            /* プランの最終更新時間 */
    private int               room_select_kind;                       /*
                                                                        * 部屋選択区分　(1:部屋ランクを選ばせる,2:部屋指定,3:ランク・部屋指定なし）
                                                                        * 部屋指定の場合は、すべての部屋で選べる時間帯にしなければならない
                                                                        */

    private int               plan_type;                              /* プラン種別(1:宿泊、2:休憩、3:当日宿泊、4:当日休憩） */
    private int               co_kind;                                /* チェックアウト区分(1:～時まで、2:INから～時間） */
    private int               coming_flag;                            /* 1:30分以内の来店必須 */
    private int               room_no;                                /* 対象部屋 */
    private int               ext_flag;                               /* 0:ハピホテ,1:ラブインジャパン,2:OTA */
    private int[]             roomNoArray;
    private int[]             ciTimeFromArray;
    private int[]             ciTimeToArray;
    private int[]             coTimeArray;
    private String[]          reserveNoArray;

    private final String      PLAN_TYPE_STAY   = "stay";
    private final String      PLAN_TYPE_REST   = "rest";

    public TimeCommon()
    {
        plan_id = 0;
        plan_sub_id = 0;
        ci_time_from = 0;
        ci_time_to = 999999;
        co_time = 0;
        est_time_arrival = 0;
        room_select_kind = 0;
        plan_type = 0;
        co_kind = 0;
        interval = 10000;
        last_update = 0;
        room_no = 0;
        coming_flag = 0;
        ext_flag = 0;
    }

    public void setCiTimeFrom(int value)
    {
        ci_time_from = value;
    }

    public void setCiTimeTo(int value)
    {
        ci_time_to = value;
    }

    public void setCoTime(int value)
    {
        co_time = value;
    }

    public int getCiTimeFrom()
    {
        return ci_time_from;
    }

    public int getCiTimeTo()
    {
        return ci_time_to;
    }

    public int getCoTime()
    {
        return co_time;
    }

    public int getRoomNo()
    {
        return room_no;
    }

    public int getInterval()
    {
        return interval;
    }

    public int getCoKind()
    {
        return co_kind;
    }

    public int getComingFlag()
    {
        return coming_flag;
    }

    public int getLastUpdate()
    {
        return last_update;
    }

    public int getPlanType()
    {
        return plan_type;
    }

    /**
     * 予約済み予約番号から、チェックイン開始時刻・終了時刻を取得する
     * 
     * @param hotelId
     * @param calDate
     * @param reserveNo
     * @return ret
     * @throws Exception
     */
    public boolean getRsvTime(int hotelId, int calDate, String reserveNo)
    {
        boolean ret = false;
        getReserveData( hotelId, reserveNo );
        ret = getRsvTime( hotelId, plan_id, calDate, room_no, null, reserveNo, 0 );
        return ret;
    }

    /**
     * 予約済み予約番号について、部屋変更可能か？
     * 
     * @param hotelId
     * @param calDate
     * @param reserveNo
     * @param roomNo //変更先部屋No
     * @return ret
     */
    public boolean isRsvChangeRoom(int hotelId, int calDate, String reserveNo, int roomNo)
    {
        boolean ret = false;
        getReserveData( hotelId, reserveNo );

        if ( plan_id != 0 )
        {
            if ( getRsvTime( hotelId, plan_id, calDate, roomNo ) != false )
            {
                if ( est_time_arrival >= ci_time_from &&
                        est_time_arrival <= ci_time_to )
                    ret = true;
            }
        }
        return ret;
    }

    /**
     * 予約Noから現状入力されている来店予定時刻を取得
     * 
     * @param hotelId
     * @param reserveNo
     * @throws Exception
     */
    private void getReserveData(int hotelId, String reserveNo)
    {

        est_time_arrival = 0;
        plan_id = 0;
        room_no = 0;
        StringBuilder query = new StringBuilder();
        query.append( " SELECT " );
        query.append( "   est_time_arrival,plan_id,seq " );
        query.append( " FROM newRsvDB.hh_rsv_reserve " );
        query.append( " WHERE id = ? AND reserve_no = ?" );
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setString( i++, reserveNo );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                est_time_arrival = result.getInt( "est_time_arrival" );
                plan_id = result.getInt( "plan_id" );
                room_no = result.getInt( "seq" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TimeCommon.getEstTimeArrival] Exception = " + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 仮予約Noと到着予定時刻からその予約可否を判断する
     * 
     * @param hotelId
     * @param calDate
     * @param reserveNoTemp
     * @param estTimeArrival
     * @return ret
     */

    public boolean isRsvTimeArrival(int hotelId, int calDate, long reserveTempNo, int estTimeArrival)
    {
        boolean ret = false;

        getPlanIdFromTemp( hotelId, reserveTempNo );

        if ( room_no != 0 )
        {
            if ( getRsvTime( hotelId, plan_id, calDate, room_no, reserveTempNo ) )
            {
                Logging.debug( "[TimeCommon.isRsvTimeArrival]hotelId=" + hotelId + ",planId=" + plan_id + ",estTimeArrival=" + estTimeArrival + ",calDate=" + calDate + ",roomNo=" + room_no + ",reserveTempNo=" + reserveTempNo + ",ci_time_from="
                        + ci_time_from + ",ci_time_to=" + ci_time_to );

                if ( estTimeArrival >= ci_time_from &&
                        estTimeArrival <= ci_time_to )
                    ret = true;
            }
        }
        else
        {
            if ( getRsvTime( hotelId, plan_id, calDate, reserveTempNo ) != false )
            {
                Logging.debug( "[TimeCommon.isRsvTimeArrival]hotelId=" + hotelId + ",planId=" + plan_id + ",estTimeArrival=" + estTimeArrival + ",calDate=" + calDate + ",roomNo=" + room_no + ",reserveTempNo=" + reserveTempNo + ",ci_time_from="
                        + ci_time_from + ",ci_time_to=" + ci_time_to );

                if ( estTimeArrival >= ci_time_from &&
                        estTimeArrival <= ci_time_to )
                    ret = true;
            }
        }
        return ret;
    }

    /**
     * 仮予約NoからプランIDと部屋番号を取得
     * 
     * @param hotelId
     * @param reserveTempNo
     * @throws Exception
     */
    private void getPlanIdFromTemp(int hotelId, long reserveTempNo)
    {

        plan_id = 0;
        room_no = 0;
        StringBuilder query = new StringBuilder();
        query.append( " SELECT " );
        query.append( "   plan_id,seq " );
        query.append( " FROM newRsvDB.hh_rsv_reserve_temp " );
        query.append( " WHERE id = ? AND reserve_temp_no = ?" );
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            prestate.setLong( i++, reserveTempNo );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                plan_id = result.getInt( "plan_id" );
                room_no = result.getInt( "seq" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TimeCommon.getPlanIdFromTemp] Exception = " + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 対象プランIDの対象日のチェックイン可能時間範囲を求める
     * 
     * @param hotelId
     * @param planId
     * @param calDate
     * @return ret
     * @throws Exception
     */
    public boolean getRsvTime(int hotelId, int planId, int calDate)
    {
        return getRsvTime( hotelId, planId, calDate, null, null, "", 0 );
    }

    public boolean getRsvTime(int hotelId, int planId, int calDate, long reserveTempNo)
    {
        return getRsvTime( hotelId, planId, calDate, null, null, "", reserveTempNo );
    }

    /**
     * 対象プランIDの対象日のチェックイン可能時間範囲を求める（部屋指定）
     * 
     * @param hotelId
     * @param planId
     * @param calDate
     * @param roomNo
     * @return ret
     * @throws Exception
     */
    public boolean getRsvTime(int hotelId, int planId, int calDate, Integer roomNo)
    {
        return getRsvTime( hotelId, planId, calDate, roomNo, null, "", 0 );
    }

    public boolean getRsvTime(int hotelId, int planId, int calDate, Integer roomNo, long reserveTempNo)
    {
        return getRsvTime( hotelId, planId, calDate, roomNo, null, "", reserveTempNo );
    }

    /**
     * 対象プランIDの対象日のチェックイン可能時間範囲を求める（ルームランク指定）
     * 
     * @param hotelId
     * @param planId
     * @param calDate
     * @param roomNo(null)
     * @param roomRank
     * @return ret
     * @throws Exception
     */
    public boolean getRsvTime(int hotelId, int planId, int calDate, Integer roomNo, Integer roomRank)
    {
        return getRsvTime( hotelId, planId, calDate, null, roomRank, "", 0 );
    }

    /**
     * 対象プランIDの対象日のチェックイン可能時間範囲を求める
     * 
     * @param hotelId
     * @param planId
     * @param calDate
     * @param roomNo
     * @param reserveNo
     * @return ret
     * @throws Exception
     */
    public boolean getRsvTime(int hotelId, int planId, int calDate, Integer roomNo, Integer roomRank, String reserveNo, long reserveTempNo)
    {
        boolean ret = true;

        int prevDate = DateEdit.addDay( calDate, -1 ); // 前日
        int nextDate = DateEdit.addDay( calDate, 1 ); // 翌日

        /* 予約間の猶予時刻の取得 */
        if ( getRsvInterval( hotelId ) == false )
        {
        }

        /* 対象プランIDの対象日のチェックイン可能時間範囲,plan_sub_idなどを求める */
        if ( getRsvTimeInitial( hotelId, planId, calDate ) == false )
        {
        }
        else
        {
            if ( co_time < ci_time_from && co_kind == 1 )
            {
                co_time = co_time + 240000;
            }
        }
        Logging.debug( "[TimeCommon.getRsvTime]hotelId=" + hotelId + ",planId=" + planId + ",plan_sub_id=" + plan_sub_id + ",calDate=" + calDate + ",roomNo=" + roomNo + ",reserveNo=" + reserveNo + ",plan_type=" + plan_type + ",room_select_kind="
                + room_select_kind + ",roomrank=" + roomRank + ",reserveTempNo=" + reserveTempNo + ",planTypeString=" + (plan_type % 2 == 1 ? PLAN_TYPE_STAY : PLAN_TYPE_REST) );

        /*
         * プランの対象部屋で予約が入っていない部屋が1件でもあれば、その日はすべての時刻範囲暫定OK
         * 当日の対象部屋についてすでに予約ははいっていれば、チェックイン時刻とチェックアウト時刻を調整する。
         */
        ret = isRsv( hotelId, planId, calDate, roomNo, roomRank, reserveNo, reserveTempNo, plan_type % 2 == 1 ? PLAN_TYPE_STAY : PLAN_TYPE_REST );

        Logging.debug( "[TimeCommon.getRsvTime]isRsv=" + ret );

        if ( ret )
            ret = adjustCoTime( hotelId, nextDate );
        Logging.debug( "[TimeCommon.getRsvTime]adjustCoTime=" + ret );

        if ( ret )
            ret = adjustCiTimeFrom( hotelId, prevDate );
        Logging.debug( "[TimeCommon.getRsvTime]adjustCiTimeFrom=" + ret );

        if ( ret )
        {
            int ci_time_from_temp = 999999;
            int ci_time_to_temp = 999999;
            int co_time_temp = 999999;
            int room_no_temp = 0;
            for( int i = 0 ; i < roomNoArray.length ; i++ )
            {
                if ( roomNo != null || room_select_kind == 2 ) // 部屋を選択の場合は最も最悪の範囲を返す。
                {
                    if ( ci_time_from < ciTimeFromArray[i] )
                    {
                        ci_time_from = ciTimeFromArray[i];
                    }
                    if ( ci_time_to > ciTimeToArray[i] )
                    {
                        ci_time_to = ciTimeToArray[i];
                    }
                    if ( co_time > coTimeArray[i] )
                    {
                        co_time = coTimeArray[i];
                    }
                }
                else
                {
                    if ( ci_time_from == ciTimeFromArray[i] && ci_time_to == ciTimeToArray[i] )
                    {
                        if ( room_no == 0 )
                        {
                            room_no = roomNoArray[i];
                        }
                    }
                    else
                    {
                        if ( ci_time_from_temp > ciTimeFromArray[i] )
                        {
                            ci_time_from_temp = ciTimeFromArray[i];
                            room_no_temp = roomNoArray[i];
                            if ( ci_time_to_temp > ciTimeToArray[i] )
                            {
                                ci_time_to_temp = ciTimeToArray[i];
                            }
                            if ( co_time_temp > coTimeArray[i] )
                            {
                                co_time_temp = coTimeArray[i];
                            }
                        }
                        else if ( ci_time_to_temp > ciTimeToArray[i] )
                        {
                            ci_time_to_temp = ciTimeToArray[i];
                            room_no_temp = roomNoArray[i];
                            if ( co_time_temp > coTimeArray[i] )
                            {
                                co_time_temp = coTimeArray[i];
                            }
                        }
                        else if ( co_time_temp > coTimeArray[i] )
                        {
                            co_time_temp = coTimeArray[i];
                            room_no_temp = roomNoArray[i];
                        }
                    }
                }

                Logging.debug( "[TimeCommon.getRsvTime]room_select_kind =" + room_select_kind + ",ci_time_from " + ci_time_from + ",ciTimeFromArray[" + i + "] :" + ciTimeFromArray[i] + ",ciTimeToArray[" + i + "] :" + ciTimeToArray[i] + ",roomNoArray[" + i
                        + "] :" + roomNoArray[i] + ",room_no=" + room_no
                        + ",room_no_temp=" + room_no_temp + ",ci_time_from_temp=" + ci_time_from_temp + ",ci_time_to_temp=" + ci_time_to_temp + ",getRoomNo=" + getRoomNo() );

            }
            if ( room_select_kind != 2 )
            {
                if ( room_no == 0 )
                {
                    ci_time_from = ci_time_from_temp;
                    ci_time_to = ci_time_to_temp;
                    co_time = co_time_temp;
                    room_no = room_no_temp;
                }
            }
        }
        return ret;
    }

    /**
     * 対象ホテルの予約intervalを求める
     * 
     * @param hotelId
     * @return ret
     * @throws Exception
     */
    private boolean getRsvInterval(int hotelId)
    {
        boolean ret = false;
        StringBuilder query = new StringBuilder();
        query.append( " SELECT " );
        query.append( "   `interval` " );
        query.append( " FROM newRsvDB.hh_rsv_reserve_basic " );
        query.append( " WHERE id = ? " );
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, hotelId );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                interval = result.getInt( "interval" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TimeCommon.getRsvInterval] Exception = " + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * 対象プランIDの対象日のチェックイン可能時間範囲を求める
     * 
     * @param hotelId
     * @param planId
     * @param calDate
     * @return ret
     * @throws Exception
     */
    private boolean getRsvTimeInitial(int hotelId, int planId, int calDate)
    {
        boolean ret = false;
        StringBuilder query = new StringBuilder();
        query.append( " SELECT " );
        query.append( "  plan.plan_sub_id,plan.plan_type,plan.room_select_kind,plan.coming_flag,plan.last_update,charge.ci_time_from,charge.ci_time_to,charge.co_time,charge.co_kind " );
        // プラン
        query.append( " FROM newRsvDB.hh_rsv_plan plan " );
        // ホテルカレンダーマスタ
        query.append( " INNER JOIN newRsvDB.hh_rsv_hotel_calendar calendar " );
        query.append( "   ON calendar.id = plan.id " );
        query.append( "   AND calendar.cal_date = ? " );
        // 料金モード内訳
        query.append( " INNER JOIN newRsvDB.hh_rsv_charge_mode_breakdown breakdown " );
        query.append( "   ON  plan.id = breakdown.id " );
        query.append( "   AND plan.plan_id = breakdown.plan_id " );
        query.append( "   AND plan.plan_sub_id = breakdown.plan_sub_id " );
        query.append( "   AND  calendar.charge_mode_id = breakdown.charge_mode_id " );
        // プラン料金モード別
        query.append( " INNER JOIN newRsvDB.hh_rsv_plan_charge charge " );
        query.append( "   ON  plan.id =  charge.id " );
        query.append( "   AND plan.plan_id = charge.plan_id  " );
        query.append( "   AND plan.plan_sub_id = charge.plan_sub_id " );
        query.append( "   AND charge.plan_charge_mode_id = breakdown.plan_charge_mode_id " );
        query.append( " WHERE plan.id = ? " );
        query.append( "   AND plan.plan_id = ? " );
        query.append( "   AND plan.latest_flag = ? " );
        query.append( "   AND plan.publishing_flag = ? " );

        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            prestate.setInt( i++, calDate );
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, Constants.LATEST_FLAG_LATEST );
            prestate.setInt( i++, Constants.PUBLISHING_FLAG_PUBLISH );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                plan_sub_id = result.getInt( "plan_sub_id" );
                plan_type = result.getInt( "plan_type" );
                room_select_kind = result.getInt( "room_select_kind" );
                coming_flag = result.getInt( "coming_flag" );
                ci_time_from = result.getInt( "ci_time_from" );
                ci_time_to = result.getInt( "ci_time_to" );
                co_time = result.getInt( "co_time" );
                co_kind = result.getInt( "co_kind" );
                last_update = result.getInt( "last_update" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TimeCommon.getRsvTimeInitial] Exception = " + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * 対象日対象プランIDで予約対象部屋があるかどうかを求める
     * 
     * @param hotelId
     * @param planId
     * @param planSubId
     * @param calDate
     * @param roomNo
     * @param reserveNo
     * @param planTypeString
     * @return ret
     * @throws Exception
     */
    private boolean isRsv(int hotelId, int planId, int calDate, Integer roomNo, Integer roomRank, String reserveNo, long reserveTempNo, String planTypeString)
    {
        boolean ret = false;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        try
        {
            StringBuilder query = new StringBuilder();
            query.append( " SELECT " );
            query.append( " remainder.seq,stay_reserve_no,rest_reserve_no FROM newRsvDB.hh_rsv_room_remainder remainder " );
            // プラン別対象部屋
            query.append( " INNER JOIN newRsvDB.hh_rsv_rel_plan_room proom " );
            query.append( "   ON remainder.id = proom.id " );
            query.append( "   AND remainder.seq = proom.seq " );
            if ( roomRank != null )
            {
                query.append( " INNER JOIN hh_hotel_room_more room" );
                query.append( "   ON remainder.id = room.id " );
                query.append( "   AND remainder.seq = room.seq " );
                query.append( "   AND room.room_rank = ? " );
            }
            query.append( " WHERE remainder.id = ? " );
            query.append( "   AND remainder.cal_date = ? " );
            query.append( "   AND remainder.stay_status <> 3 " ); // 販売停止部屋以外
            if ( roomNo != null )
                query.append( "   AND remainder.seq = ? " );
            query.append( "   AND proom.plan_id = ? " );
            query.append( "   AND proom.plan_sub_id = ? " );
            query.append( "   AND (" );
            query.append( "       remainder." + planTypeString + "_reserve_no = ''" );
            query.append( "     AND (remainder." + planTypeString + "_reserve_temp_no = 0 " );
            if ( reserveTempNo != 0 )
                query.append( "     OR remainder." + planTypeString + "_reserve_temp_no = " + reserveTempNo );
            query.append( "          OR  remainder." + planTypeString + "_reserve_limit_day < ? " );
            query.append( "          OR ( " );
            query.append( "              remainder." + planTypeString + "_reserve_limit_day = ? " );
            query.append( "          AND remainder." + planTypeString + "_reserve_limit_time < ? " );
            query.append( "             ) " );
            query.append( "          ) " );
            if ( !reserveNo.equals( "" ) )
                query.append( "   OR remainder." + planTypeString + "_reserve_no = ? " );
            query.append( "       ) " );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query.toString() );
            int i = 1;
            if ( roomRank != null )
                prestate.setInt( i++, roomRank );
            prestate.setInt( i++, hotelId );
            prestate.setInt( i++, calDate );
            if ( roomNo != null )
                prestate.setInt( i++, roomNo );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, plan_sub_id );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowTime );
            if ( !reserveNo.equals( "" ) )
                prestate.setString( i++, reserveNo );

            result = prestate.executeQuery();

            int roomCount = 0;
            if ( result.last() != false )
            {
                roomCount = result.getRow();
                roomNoArray = new int[roomCount];
                ciTimeFromArray = new int[roomCount];
                ciTimeToArray = new int[roomCount];
                coTimeArray = new int[roomCount];
                reserveNoArray = new String[roomCount];
            }

            result.beforeFirst();
            i = -1;
            while( result.next() != false )
            {
                i++;
                roomNoArray[i] = result.getInt( "seq" );
                ciTimeFromArray[i] = ci_time_from;
                ciTimeToArray[i] = ci_time_to;
                coTimeArray[i] = co_kind == 1 ? co_time : ci_time_to + co_time;
                reserveNoArray[i] = result.getString( (planTypeString.equals( "stay" ) ? "rest" : "stay") + "_reserve_no" );
            }
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
            if ( reserveNoArray == null )
            {
                ret = false;
            }
            else
            {
                for( i = 0 ; i < reserveNoArray.length ; i++ )
                {
                    if ( !reserveNoArray[i].equals( "" ) )
                    {
                        query = new StringBuilder();
                        query.append( " SELECT " );
                        query.append( " ci_time_from,ci_time_to,co_time,co_kind,est_time_arrival,ext_flag FROM newRsvDB.hh_rsv_reserve" );
                        query.append( "   WHERE id = ? AND reserve_no = ?" );
                        prestate = connection.prepareStatement( query.toString() );
                        prestate.setInt( 1, hotelId );
                        prestate.setString( 2, reserveNoArray[i] );
                        result = prestate.executeQuery();
                        if ( result.next() != false )
                        {
                            if ( planTypeString.equals( "rest" ) && result.getInt( "ext_flag" ) == ReserveCommon.EXT_OTA ) // 休憩予約時、OTAからの予約がはいっている部屋には予約をいれさせない
                            {
                                ciTimeFromArray[i] = 0;
                                ciTimeToArray[i] = 0;
                                coTimeArray[i] = 0;
                            }
                            else
                            {
                                int coTime = result.getInt( "co_kind" ) == 1 ? result.getInt( "co_time" ) : result.getInt( "est_time_arrival" ) + result.getInt( "co_time" );
                                if ( coTime < result.getInt( "est_time_arrival" ) )
                                {
                                    coTime = coTime + 240000;
                                }
                                coTime = coTime + interval;
                                int estTimeArrival = result.getInt( "est_time_arrival" ) - interval;
                                Logging.info( "[TimeCommon.isRsv] coTime=" + coTime + ",estTimeArrival=" + estTimeArrival + ",ciTimeFromArray[" + i + "]=" + ciTimeFromArray[i] + ",ciTimeToArray[" + i + "]=" + ciTimeToArray[i] + ",coTimeArray[" + i + "]="
                                        + coTimeArray[i] + ",coKind=" + co_kind );

                                if ( (ciTimeFromArray[i] >= estTimeArrival && ciTimeFromArray[i] <= coTime)
                                        || (ciTimeFromArray[i] <= estTimeArrival && coTimeArray[i] >= coTime)
                                        || (ciTimeFromArray[i] >= estTimeArrival && coTimeArray[i] <= coTime) )
                                {
                                    ciTimeFromArray[i] = coTime;
                                }

                                if ( coTimeArray[i] <= coTime && coTimeArray[i] >= estTimeArrival )
                                {
                                    if ( co_kind == 1 )
                                    {
                                        if ( coTimeArray[i] > estTimeArrival )
                                        {
                                            coTimeArray[i] = ciTimeFromArray[i]; // 到着予定がチェックアウト時刻より前なので対象外
                                        }
                                    }
                                    else
                                    {
                                        ciTimeToArray[i] = estTimeArrival - (coTimeArray[i] - ciTimeToArray[i]);
                                        coTimeArray[i] = estTimeArrival;
                                    }
                                }
                                if ( ciTimeFromArray[i] <= estTimeArrival && ciTimeToArray[i] >= estTimeArrival )
                                {
                                    ciTimeToArray[i] = estTimeArrival;
                                    if ( ciTimeToArray[i] == coTimeArray[i] )
                                    {
                                        ciTimeToArray[i] = coTimeArray[i] - interval;
                                    }
                                }
                            }
                        }
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TimeCommon.isRsv] Exception = " + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        if ( roomNoArray == null )
        {
            ret = false;
        }
        else
        {
            for( int i = 0 ; i < roomNoArray.length ; i++ )
            {
                Logging.debug( "[TimeCommon.isRsv] ciTimeFromArray[i] < coTimeArray[i] && ciTimeToArray[i] < coTimeArray[i]:" + ciTimeFromArray[i] + " <" + ciTimeToArray[i] + " && " + ciTimeToArray[i] + "<" + coTimeArray[i] );

                if ( ciTimeFromArray[i] < ciTimeToArray[i] && ciTimeToArray[i] < coTimeArray[i] )
                {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * 対象日対象プランIDで予約対象部屋があるかどうかを求める
     * 
     * @param hotelId
     * @param calDate
     * @param roomArray
     * @return ret
     * @throws Exception
     */
    private boolean adjustCoTime(int hotelId, int calDate)
    {
        boolean ret = false;
        StringBuilder query;
        String stay_reserve_no = "";
        String rest_reserve_no = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            for( int i = 0 ; i < roomNoArray.length ; i++ )
            {
                query = new StringBuilder();
                query.append( " SELECT " );
                query.append( " stay_reserve_no,rest_reserve_no FROM newRsvDB.hh_rsv_room_remainder remainder " );
                query.append( " WHERE remainder.id = ? " );
                query.append( "   AND remainder.cal_date = ? " );
                query.append( "   AND remainder.seq = ? " );
                query.append( "   LIMIT 0,1" );

                prestate = connection.prepareStatement( query.toString() );
                prestate.setInt( 1, hotelId );
                prestate.setInt( 2, calDate );
                prestate.setInt( 3, roomNoArray[i] );
                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    stay_reserve_no = result.getString( "stay_reserve_no" );
                    rest_reserve_no = result.getString( "rest_reserve_no" );
                }
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
                if ( !stay_reserve_no.equals( "" ) || !rest_reserve_no.equals( "" ) )
                {
                    query = new StringBuilder();
                    query.append( " SELECT " );
                    query.append( " ci_time_from,ci_time_to,co_time,co_kind,est_time_arrival FROM newRsvDB.hh_rsv_reserve" );
                    query.append( "   WHERE id = ? AND (reserve_no = ? OR reserve_no = ?)" );
                    prestate = connection.prepareStatement( query.toString() );
                    prestate.setInt( 1, hotelId );
                    prestate.setString( 2, stay_reserve_no );
                    prestate.setString( 3, rest_reserve_no );
                    result = prestate.executeQuery();
                    while( result.next() != false )
                    {
                        if ( coTimeArray[i] >= result.getInt( "est_time_arrival" ) + 240000 - interval )
                        {
                            coTimeArray[i] = result.getInt( "est_time_arrival" ) + 240000 - interval;
                        }
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TimeCommon.adjustCoTime] Exception = " + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        for( int i = 0 ; i < roomNoArray.length ; i++ )
        {
            if ( ciTimeFromArray[i] <= ciTimeToArray[i] )
            {
                ret = true;
                break;
            }
        }
        return ret;
    }

    private boolean adjustCiTimeFrom(int hotelId, int calDate)
    {
        boolean ret = false;
        StringBuilder query;
        String stay_reserve_no = "";
        String rest_reserve_no = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            for( int i = 0 ; i < roomNoArray.length ; i++ )
            {
                query = new StringBuilder();
                query.append( " SELECT " );
                query.append( " stay_reserve_no,rest_reserve_no FROM newRsvDB.hh_rsv_room_remainder remainder " );
                query.append( " WHERE remainder.id = ? " );
                query.append( "   AND remainder.cal_date = ? " );
                query.append( "   AND remainder.seq = ? " );
                query.append( "   LIMIT 0,1" );

                prestate = connection.prepareStatement( query.toString() );
                prestate.setInt( 1, hotelId );
                prestate.setInt( 2, calDate );
                prestate.setInt( 3, roomNoArray[i] );
                result = prestate.executeQuery();
                if ( result.next() != false )
                {
                    stay_reserve_no = result.getString( "stay_reserve_no" );
                    rest_reserve_no = result.getString( "rest_reserve_no" );
                }
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
                if ( !stay_reserve_no.equals( "" ) || !rest_reserve_no.equals( "" ) )
                {
                    query = new StringBuilder();
                    query.append( " SELECT " );
                    query.append( " ci_time_from,ci_time_to,co_time,est_time_arrival,co_kind FROM newRsvDB.hh_rsv_reserve" );
                    query.append( "   WHERE id = ? AND (reserve_no = ? OR reserve_no = ?) AND reserve_no<>''" );
                    prestate = connection.prepareStatement( query.toString() );
                    prestate.setInt( 1, hotelId );
                    prestate.setString( 2, stay_reserve_no );
                    prestate.setString( 3, rest_reserve_no );
                    result = prestate.executeQuery();
                    while( result.next() != false )
                    {
                        int coTime = result.getInt( "co_kind" ) == 1 ? result.getInt( "co_time" ) : result.getInt( "est_time_arrival" ) + result.getInt( "co_time" );
                        if ( coTime > 240000 )
                        {
                            coTime = coTime - 240000;
                            if ( ciTimeFromArray[i] <= coTime + interval )
                            {
                                ciTimeFromArray[i] = coTime + interval;
                            }
                        }
                    }
                    DBConnection.releaseResources( result );
                    DBConnection.releaseResources( prestate );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[TimeCommon.adjustCiTimeFrom] Exception = " + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        for( int i = 0 ; i < roomNoArray.length ; i++ )
        {
            Logging.info( "[TimeCommon.adjustCiTimeFrom]  ciTimeFromArray[i] <= ciTimeToArray[i] =" + ciTimeFromArray[i] + "<=" + ciTimeToArray[i] );
            if ( ciTimeFromArray[i] <= ciTimeToArray[i] )
            {
                ret = true;
                break;
            }
        }
        return ret;
    }
}
