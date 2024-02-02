package jp.happyhotel.touch;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.HotelIp;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.TcpClientEx;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataHotelRoomrank;
import jp.happyhotel.data.DataRsvReserve;
import jp.happyhotel.dto.DtoRsvList;
import jp.happyhotel.dto.DtoRsvListData;
import jp.happyhotel.others.HapiTouchRsvSub;
import jp.happyhotel.reserve.FormReserveSheetPC;
import jp.happyhotel.reserve.LogicReserveSheetPC;
import jp.happyhotel.user.UserBasicInfo;

import org.apache.commons.lang.StringUtils;

/**
 * ハピホテタッチ予約一覧情報処理クラス
 * 
 * @author T.Sakurai
 * @version 1.00 2017/03/27
 * @see リクエスト：1016電文<br>
 *      レスポンス：1017電文
 */
public class RsvList extends DtoRsvList implements Serializable
{

    private static final long serialVersionUID      = -8773588860845751555L;
    private static final int  TIMEOUT               = 10000;
    private static final int  HAPIHOTE_PORT_NO      = 7046;
    final int                 MAX_ROOM_LENGTH       = 60;
    final int                 HEADER_LENGTH         = 32;
    final int                 COMMAND_LENGTH        = 4;
    final String              COMMAND               = "1016";
    final String              REPLY_COMMAND         = "1017";
    String                    header;
    final int                 MAX_CHARGELIST_LENGTH = 30;

    public boolean getData(int hotelId, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        boolean ret = false;
        String paramKind;
        String paramStartDate = "";
        String paramEndDate = "";
        String paramDays = "2";
        String paramRsvNo = "";
        String paramIdentifyNo = "0";

        paramKind = request.getParameter( "kind" );
        paramStartDate = request.getParameter( "startDate" );
        paramEndDate = request.getParameter( "endDate" );
        paramRsvNo = request.getParameter( "rsvNo" );
        paramDays = request.getParameter( "days" );
        paramIdentifyNo = request.getParameter( "IdentifyNo" );

        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        // 5時以前の場合は前日とする
        if ( 50000 > nowTime )
        {
            nowTime = nowTime + 240000;
            nowDate = DateEdit.addDay( nowDate, -1 );
        }

        // 開始日のチェック
        if ( (paramStartDate == null) || (paramStartDate.equals( "" ) != false) || (CheckString.numCheck( paramStartDate ) == false) )
        {
            paramStartDate = "0";
        }

        // 終了日のチェック
        if ( (paramEndDate == null) || (paramEndDate.equals( "" ) != false) || (CheckString.numCheck( paramEndDate ) == false) )
        {
            paramEndDate = "0";
        }

        if ( (paramDays == null) || (paramDays.equals( "" ) != false) || (CheckString.numCheck( paramDays ) == false) )
        {
            if ( paramStartDate.equals( "0" ) )
            {
                paramDays = "2"; // 日付パラメータがない場合は2日分
            }
            else
            {
                paramDays = "1"; // 日付パラメータがある場合は当日分
            }
        }
        // 開始日がセットされていない場合は、開始日に現在予約管理日付をセットする。
        //
        if ( paramStartDate.equals( "0" ) )
        {
            paramStartDate = Integer.toString( nowDate );
        }

        // 終了日がセットされていない場合は、日数のパラメータから終了日付をセットする。
        //
        if ( paramEndDate.equals( "0" ) )
        {
            paramEndDate = paramDays.equals( "1" ) ? paramStartDate : Integer.toString( DateEdit.addDay( Integer.parseInt( paramStartDate ), Integer.parseInt( paramDays ) ) );
        }

        if ( (paramRsvNo == null) || (paramRsvNo.equals( "" ) != false) )
        {
            paramRsvNo = "";
        }

        if ( (paramKind == null) || (paramKind.equals( "" ) != false) || (CheckString.numCheck( paramKind ) == false) )
        {
            paramKind = "0";
        }

        if ( Integer.parseInt( paramKind ) <= 0 )
        {
            paramKind = "5";
        }

        if ( (paramIdentifyNo == null) || (paramIdentifyNo.equals( "" ) != false) || (CheckString.numCheck( paramIdentifyNo ) == false) )
        {
            paramIdentifyNo = "0";
        }
        setIdentifyNo( Integer.parseInt( paramIdentifyNo ) );

        ret = getData( hotelId, Integer.parseInt( paramStartDate ), Integer.parseInt( paramEndDate ), Integer.parseInt( paramDays ), paramRsvNo, paramIdentifyNo, 1 );
        return ret;
    }

    public boolean getData(int hotelId, int startDate, int endDate, int days, String rsvNo, int all_flag) throws Exception
    {
        return getData( hotelId, startDate, endDate, days, rsvNo, "0", all_flag );
    }

    /*
     * all_flag : 0 ･･･1件のみ， 1･･･すべて
     */

    public boolean getData(int hotelId, int startDate, int endDate, int days, String rsvNo, String identifyNo, int all_flag) throws Exception
    {
        Connection connection = null;
        connection = DBConnection.getConnection();
        boolean ret = false;
        try
        {
            ret = getData( connection, hotelId, startDate, endDate, days, rsvNo, identifyNo, all_flag );
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvList.getData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return ret;
    }

    public boolean getData(Connection connection, int hotelId, int startDate, int endDate, int days, String rsvNo, int all_flag) throws Exception
    {
        return getData( connection, hotelId, startDate, endDate, days, rsvNo, "0", all_flag );
    }

    public boolean getData(Connection connection, int hotelId, int startDate, int endDate, int days, String rsvNo, String identifyNo, int all_flag) throws Exception
    {
        boolean ret = false;
        String query = "";
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = -1;
        /* 部屋名称取得のため */
        DataHotelRoomMore dhrm = new DataHotelRoomMore();
        /* 部屋ランク名称取得のため */
        DataHotelRoomrank dhr = new DataHotelRoomrank();

        /* 候補部屋取得のため */
        String[] anotherRoomList = null;
        int anotherRoomCount = 0;

        /* 連泊時個別金額 */
        int[] chargeList = null;

        HapiTouchRsvSub htRsvSub = new HapiTouchRsvSub();
        /* オプションデータ取得のため */
        FormReserveSheetPC frm = new FormReserveSheetPC();
        LogicReserveSheetPC logic = new LogicReserveSheetPC();

        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        /* 部屋名称取得のため */
        DataRsvReserve drr = new DataRsvReserve();

        /* ラブインジャパンユーザ判断のため */
        UserBasicInfo ubi = new UserBasicInfo();

        // 5時以前の場合は前日とする
        if ( 50000 > nowTime )
        {
            nowTime = nowTime + 240000;
        }

        try
        {
            query = "SELECT";
            query += " hrr.status";
            query += ",hrr.ext_flag";
            query += ",hrr.user_id";
            query += ",hrr.reserve_no";
            query += ",hrr.reserve_sub_no";
            query += ",CASE WHEN rsvfirst.reserve_date IS NULL THEN hrr.reserve_date ELSE rsvfirst.reserve_date END AS reserve_date";
            query += ",hrr.est_time_arrival";
            query += ",hrp.plan_type";
            query += ",CASE WHEN hrr.room_hold = 0 THEN hrr.seq ELSE hrr.room_hold END AS roomNo";
            query += ",hrr.plan_id";
            query += ",hrr.plan_sub_id";
            query += ",hrp.plan_name";
            query += ",hrr.charge_total";
            query += ",hrr.payment";
            query += ",hrr.used_mile";
            query += ",hrr.ci_time_from";
            query += ",hrr.ci_time_to";
            query += ",hrr.co_kind";
            query += ",hrr.co_time";
            query += ",hrr.name_last";
            query += ",hrr.name_first";
            query += ",hrr.name_last_kana";
            query += ",hrr.name_first_kana";
            query += ",hrr.tel1";
            query += ",hrr.num_man";
            query += ",hrr.num_woman";
            query += ",hrr.num_child";
            query += ",hubf.country";
            query += ",hrr.charge_total_all";
            query += ",hrr.reserve_date_to";
            query += ",hrrb.parking as hotelParking";
            query += ",hrr.parking";
            query += ",hrr.parking_count";
            query += ",hrr.highroof_count";
            query += ",hrr.remarks";
            query += ",hrr.demands";
            query += ",hrp.question";
            query += ",hrp.room_select_kind";
            query += ",hrr.reserve_no_main";
            query += ",hrp.foreign_flag,hrp.consecutive_flag,hrp.plan_sub_name,hrp.max_stay_num,hrp.min_stay_num"; // ラブインジャパンプラン名生成用
            query += ",hrr.address1";
            query += ",hrr.room_hold";
            query += ",hrr.accept_date";
            query += ",hrp.local_payment_kind";
            query += ",CASE WHEN rsvfirst.ci_time_from IS NULL THEN hrr.ci_time_from ELSE rsvfirst.ci_time_from END AS first_ci_time_from";
            query += ",CASE WHEN rsvfirst.ci_time_to IS NULL THEN hrr.ci_time_to ELSE rsvfirst.ci_time_to END AS first_ci_time_to";
            query += ",CASE WHEN rsvfinal.co_time IS NULL THEN hrr.co_time ELSE rsvfinal.co_time END AS final_co_time";
            query += " FROM newRsvDB.hh_rsv_reserve hrr";
            query += " INNER JOIN newRsvDB.hh_rsv_plan hrp ON ( hrr.id = hrp.id AND hrr.plan_id = hrp.plan_id  AND hrr.plan_sub_id = hrp.plan_sub_id )";
            query += " INNER JOIN newRsvDB.hh_rsv_reserve_basic hrrb ON ( hrr.id = hrrb.id ) ";
            query += " LEFT JOIN  newRsvDB.hh_rsv_reserve rsvfirst ON hrr.id=rsvfirst.id AND SUBSTRING(rsvfirst.reserve_no,INSTR(rsvfirst.reserve_no,'-') + 1) = hrr.reserve_no_main AND rsvfirst.reserve_no_main != '' ";
            query += " LEFT JOIN  newRsvDB.hh_rsv_reserve rsvfinal ON hrr.id=rsvfinal.id AND hrr.reserve_no_main = rsvfinal.reserve_no_main AND hrr.reserve_date_to = rsvfinal.reserve_date AND rsvfinal.reserve_no_main != '' ";
            query += " LEFT JOIN hh_user_basic_foreign hubf ON ( hrr.user_id = hubf.user_id) ";
            query += " WHERE hrr.id = ?";
            if ( rsvNo.equals( "" ) == false && all_flag == 0 )
            {
                query += " AND hrr.reserve_no = ?";
            }
            else
            {
                if ( days == 2 )
                {
                    query += " AND CONCAT(hrr.reserve_date,hrr.est_time_arrival) BETWEEN ? AND ?";
                }
                else
                {
                    query += " AND hrr.reserve_date BETWEEN ? AND ?";
                }
                query += " AND (hrr.reserve_no_main = '' OR SUBSTRING(hrr.reserve_no,INSTR(hrr.reserve_no,'-') + 1) = hrr.reserve_no_main OR hrr.reserve_date = ?)"; // 連泊の場合は1件目のみを返す
            }
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            if ( rsvNo.equals( "" ) == false && all_flag == 0 )
            {
                prestate.setString( 2, rsvNo );
            }
            else
            {
                if ( days == 2 )
                {
                    prestate.setString( 2, Integer.toString( startDate ) + "050000" );
                    prestate.setString( 3, Integer.toString( endDate ) + String.format( "%06d", nowTime ) );
                }
                else
                {
                    prestate.setInt( 2, startDate );
                    prestate.setInt( 3, endDate );
                }
                prestate.setInt( 4, startDate );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    count = result.getRow();
                    // クラスの配列を用意し、初期化する。
                    dtoRsvListData = new DtoRsvListData[count];
                    ret = true;
                }
                result.beforeFirst();
                count = -1;
                while( result.next() != false )
                {
                    count++;

                    dtoRsvListData[count] = new DtoRsvListData();
                    dtoRsvListData[count].setStatus( result.getInt( "status" ) );
                    dtoRsvListData[count].setExtFlag( result.getInt( "ext_flag" ) );

                    if ( ubi.isLvjUser( result.getString( "user_id" ) ) != false )
                    {
                        dtoRsvListData[count].setUserId( "" );
                    }
                    else
                    {
                        dtoRsvListData[count].setUserId( result.getString( "user_id" ) );
                    }
                    dtoRsvListData[count].setRsvNo( result.getString( "reserve_no" ) );
                    dtoRsvListData[count].setDispRsvNo( result.getString( "reserve_no" ).substring( result.getString( "reserve_no" ).length() - 6 ) );
                    if ( result.getInt( "plan_type" ) == 1 || result.getInt( "plan_type" ) == 3 ) // 宿泊
                    {
                        if ( !result.getString( "reserve_no_main" ).equals( "" ) )
                        {
                            if ( drr.getData( hotelId, "A" + hotelId + "-" + result.getString( "reserve_no_main" ) ) != false )
                            {
                                dtoRsvListData[count].setRsvNo( drr.getReserveNo() );// 予約Noは親の予約No
                                dtoRsvListData[count].setDispRsvNo( result.getString( "reserve_no_main" ) );
                                dtoRsvListData[count].setArrivalDate( drr.getReserveDate() );
                                chargeList = htRsvSub.getChargeList( hotelId, result.getString( "reserve_no_main" ) );
                                dtoRsvListData[count].setCountChargeList( chargeList.length );
                                dtoRsvListData[count].setChargeList( chargeList );
                            }
                            else
                            {
                                dtoRsvListData[count].setArrivalDate( result.getInt( "reserve_date" ) );
                                dtoRsvListData[count].setCountChargeList( 1 );
                                chargeList = new int[1];
                                chargeList[0] = result.getInt( "charge_total" );
                                dtoRsvListData[count].setChargeList( chargeList );
                            }
                        }
                        else
                        {
                            dtoRsvListData[count].setArrivalDate( result.getInt( "reserve_date" ) );
                            dtoRsvListData[count].setCountChargeList( 1 );
                            chargeList = new int[1];
                            chargeList[0] = result.getInt( "charge_total" );
                            dtoRsvListData[count].setChargeList( chargeList );
                        }
                        dtoRsvListData[count].setPlanType( 01 );
                    }
                    else
                    {
                        dtoRsvListData[count].setArrivalDate( result.getInt( "reserve_date" ) );
                        dtoRsvListData[count].setCountChargeList( 0 );
                        if ( ReserveCommon.isSyncRest( hotelId ) != false ) // 休憩予約がはいって問題ない場合
                        {
                            dtoRsvListData[count].setPlanType( 02 );
                        }
                        else
                        {
                            dtoRsvListData[count].setPlanType( 01 );
                        }
                    }
                    dtoRsvListData[count].setArrivalTime( result.getInt( "est_time_arrival" ) );

                    String reserveRoom = "";
                    int roomRank = 0;
                    if ( dhrm.getData( hotelId, result.getInt( "roomNo" ) ) != false )
                    {
                        reserveRoom = dhrm.getRoomNameHost();
                        roomRank = dhrm.getRoomRank();
                    }
                    else
                    {
                        reserveRoom = Integer.toString( result.getInt( "roomNo" ) );
                    }
                    dtoRsvListData[count].setRoomName( reserveRoom );

                    anotherRoomList = htRsvSub.getAnotherReserveRoom( hotelId, result.getInt( "plan_id" ), result.getInt( "plan_sub_id" ), result.getInt( "reserve_date" ), reserveRoom, result.getString( "reserve_no" ) );
                    anotherRoomCount = anotherRoomList.length;
                    dtoRsvListData[count].setRoomNameListLength( anotherRoomCount );
                    dtoRsvListData[count].setRoomNameList( anotherRoomList );
                    if ( result.getInt( "foreign_flag" ) == 1 )
                    {
                        dtoRsvListData[count].setPlanNm( "LIJ " + OwnerRsvCommon.getFroeignPlanName( result.getString( "plan_name" ).toString(), result.getInt( "consecutive_flag" ), result.getInt( "min_stay_num" ), result.getInt( "max_stay_num" ),
                                result.getString( "plan_sub_name" ) ) );
                    }
                    else if ( result.getInt( "foreign_flag" ) == 2 )
                    {
                        dtoRsvListData[count].setPlanNm( "[E]" + result.getString( "plan_name" ) );
                    }
                    else
                    {
                        if ( result.getInt( "plan_type" ) == 2 || result.getInt( "plan_type" ) == 4 ) // 休憩
                        {
                            dtoRsvListData[count].setPlanNm( "[休]" + result.getString( "plan_name" ) );
                        }
                        else
                        {
                            dtoRsvListData[count].setPlanNm( result.getString( "plan_name" ) );
                        }
                    }
                    dtoRsvListData[count].setChargeTotal( result.getInt( "charge_total_all" ) == 0 ? result.getInt( "charge_total" ) : result.getInt( "charge_total_all" ) );
                    dtoRsvListData[count].setPayment( result.getInt( "payment" ) == 1 ? result.getInt( "charge_total" ) - result.getInt( "used_mile" ) : 0 );
                    dtoRsvListData[count].setUsedMile( result.getInt( "used_mile" ) );
                    dtoRsvListData[count].setPossibleTime( result.getInt( "co_kind" ) == 2 ? (result.getInt( "co_time" ) / 10000) * 60 + (result.getInt( "co_time" ) / 100 % 100) : 0 );
                    if ( result.getInt( "reserve_date_to" ) != 0 && result.getInt( "reserve_date" ) < result.getInt( "reserve_date_to" ) )
                    {
                        dtoRsvListData[count].setCiTimeFrom( result.getInt( "first_ci_time_from" ) / 100 );
                        dtoRsvListData[count].setCiTimeTo( result.getInt( "first_ci_time_to" ) / 100 );
                        dtoRsvListData[count].setCoTime( result.getInt( "final_co_time" ) / 100 );
                    }
                    else
                    {
                        dtoRsvListData[count].setCiTimeFrom( result.getInt( "ci_time_from" ) / 100 );
                        dtoRsvListData[count].setCiTimeTo( result.getInt( "ci_time_to" ) / 100 );
                        dtoRsvListData[count].setCoTime( result.getInt( "co_kind" ) == 1 ? result.getInt( "co_time" ) / 100 : 0 );
                    }
                    dtoRsvListData[count].setName( result.getString( "name_last" ) + " " + result.getString( "name_first" ) );
                    dtoRsvListData[count].setNameKana( result.getString( "name_last_kana" ) + " " + result.getString( "name_first_kana" ) );

                    String smsPhoneNo = "";
                    if ( result.getInt( "payment" ) == 2 // 現地払い
                            && result.getInt( "local_payment_kind" ) == 2 ) // SMS認証させる
                    {
                        smsPhoneNo = ReserveCommon.getSmsPhoneNo( connection, result.getString( "user_id" ), result.getInt( "accept_date" ) );
                    }
                    dtoRsvListData[count].setTel( smsPhoneNo.equals( "" ) ? result.getString( "tel1" ) : smsPhoneNo + "(S)" );
                    dtoRsvListData[count].setNumMan( result.getInt( "num_man" ) );
                    dtoRsvListData[count].setNumWoman( result.getInt( "num_woman" ) );
                    dtoRsvListData[count].setNumChild( result.getInt( "num_child" ) );

                    frm.setSelHotelId( hotelId );
                    frm.setRsvNo( result.getString( "reserve_no" ) );
                    frm.setRsvSubNo( result.getInt( "reserve_sub_no" ) );
                    frm.setRsvDate( result.getInt( "reserve_date" ) );
                    // 予約データ抽出
                    logic.setFrm( frm );
                    // 通常オプションデータ取得
                    logic.getRsvOptData( ReserveCommon.OPTION_USUAL, 2 );
                    // 必須オプションデータ取得
                    logic.getRsvOptData( ReserveCommon.OPTION_IMP, 2 );

                    String optTemp = "";
                    // 通常オプション
                    for( int i = 0 ; i < frm.getOptInpMaxQuantityList().size() ; i++ )
                    {
                        if ( optTemp.length() > 160 )
                        {
                            optTemp += "※画面よりオプションを確認してください\n";
                        }
                        else
                        {
                            optTemp += frm.getOptNmList().get( i ) + " ";
                            optTemp += frm.getOptUnitPriceViewList().get( i ) + "×";
                            optTemp += frm.getOptInpMaxQuantityList().get( i ) + "＝";
                            optTemp += frm.getOptChargeTotalList().get( i );
                            if ( !frm.getOptRemarksList().get( i ).equals( "" ) )
                            {
                                optTemp += "(" + frm.getOptRemarksList().get( i ) + ")";
                            }
                            optTemp += "\n";
                        }
                    }
                    dtoRsvListData[count].setOption0( optTemp );

                    optTemp = "";
                    boolean isNumber = false;
                    if ( frm.getOptNmImpList().size() > 1 )
                    {
                        if ( frm.getOptNumberList().get( frm.getOptNmImpList().size() - 1 ) > 1 )
                        {
                            isNumber = true;
                        }
                    }
                    int beforeNumber = -1;
                    // 必須オプションを取得
                    for( int i = 0 ; i < frm.getOptNmImpList().size() ; i++ )
                    {
                        if ( optTemp.length() > 160 )
                        {
                            optTemp += "※画面よりオプションを確認してください\n";
                        }
                        else
                        {
                            String optName = "";
                            if ( beforeNumber != frm.getOptNumberList().get( i ) && isNumber )
                            {
                                optName = "【" + frm.getOptNumberList().get( i ) + "人目】\n";
                            }
                            optName += frm.getOptNmImpList().get( i ) + "\n";
                            optTemp += optName;

                            String optSubName = frm.getOptSubNmImpList().get( i );

                            if ( frm.getOptQuantityImpList().get( i ) > 1 )
                            {
                                optSubName += "×" + frm.getOptQuantityImpList().get( i );
                            }
                            optTemp += optSubName + "\n";

                            beforeNumber = frm.getOptNumberList().get( i );
                        }
                    }

                    // lvj予約で連泊のとき、日付ごとの料金を出力
                    if ( StringUtils.isNotBlank( result.getString( "reserve_no_main" ) ) )
                    {
                        String[][] eachAmount = OwnerRsvCommon.getRsvEachAmountArr( hotelId, result.getString( "reserve_no_main" ) );
                        int len = eachAmount.length;
                        for( int index = 0 ; index < len ; index++ )
                        {
                            optTemp += eachAmount[index][0];
                            optTemp += "：";
                            optTemp += eachAmount[index][1];
                            optTemp += "\n";
                        }
                    }
                    dtoRsvListData[count].setOption1( optTemp );

                    String parkingTemp = "";
                    if ( result.getInt( "hotelParking" ) == ReserveCommon.PARKING_NO_INPUT )
                    {
                        parkingTemp = ReserveCommon.PARKING_NO_PARKNG;
                    }
                    else
                    {
                        if ( result.getInt( "parking" ) == ReserveCommon.PRKING_USED_USE )
                        {
                            // 利用する
                            parkingTemp = ReserveCommon.PARKING_USE;
                            if ( result.getInt( "parking_count" ) != 0 )
                            {
                                parkingTemp += ":" + result.getInt( "parking_count" ) + "台";
                                if ( result.getInt( "parking_count" ) != 0 )
                                {
                                    if ( result.getInt( "highroof_count" ) != 0 )
                                    {
                                        parkingTemp += "(ハイルーフ " + result.getInt( "highroof_count" ) + ")";
                                    }
                                }
                            }
                        }
                        else
                        {
                            // 利用しない
                            parkingTemp = ReserveCommon.PARKING_NOT_USE;
                        }
                    }
                    dtoRsvListData[count].setCar( parkingTemp );

                    String remarksTemp = "";
                    if ( result.getString( "question" ) != null )
                    {
                        remarksTemp = ConvertCharacterSet.convDb2Form( result.getString( "question" ).replace( "\r\n", "\n" ) );
                    }
                    if ( !result.getString( "remarks" ).equals( "" ) )
                    {
                        remarksTemp += "\n回答「" + ConvertCharacterSet.convDb2Form( result.getString( "remarks" ).toString().replace( "<br>", "\n" ).replace( "\r\n", "\n" ) ) + "」";
                    }
                    dtoRsvListData[count].setRemarks( remarksTemp );

                    dtoRsvListData[count].setDemands( ConvertCharacterSet.convDb2Form( result.getString( "demands" ).toString().replace( "<br>", "\n" ).replace( "\r\n", "\n" ) ) );

                    if ( result.getInt( "room_select_kind" ) == 1 || result.getInt( "room_select_kind" ) == 2 )
                    {
                        if ( dhr.getData( hotelId, roomRank ) != false )
                        {
                            dtoRsvListData[count].setRoomRank( dhr.getRankName() );
                        }
                    }
                    dtoRsvListData[count].setRsvDate( result.getInt( "reserve_date" ) );
                    dtoRsvListData[count].setCountry( result.getInt( "foreign_flag" ) == 1 ? result.getString( "country" ) == null ? result.getString( "address1" ) : result.getString( "country" ) : "" );
                    dtoRsvListData[count].setPaymentAll( result.getInt( "payment" ) == 1 ? result.getInt( "charge_total_all" ) == 0 ? result.getInt( "charge_total" ) - result.getInt( "used_mile" ) : result.getInt( "charge_total_all" ) : 0 );
                    int dateSpan = 0;
                    if ( result.getInt( "co_kind" ) == 1 )
                    {
                        if ( result.getInt( "reserve_date_to" ) != 0 && result.getInt( "reserve_date" ) < result.getInt( "reserve_date_to" ) )
                        {
                            if ( result.getInt( "ci_time_from" ) > result.getInt( "final_co_time" ) ) // 翌日
                            {
                                dateSpan = 1;
                            }
                        }
                        else
                        {
                            if ( result.getInt( "ci_time_from" ) > result.getInt( "co_time" ) ) // 翌日
                            {
                                dateSpan = 1;
                            }
                        }
                    }
                    dtoRsvListData[count].setReserveDateTo( result.getInt( "reserve_date_to" ) == 0 ? DateEdit.addDay( result.getInt( "reserve_date" ), dateSpan ) : DateEdit.addDay( result.getInt( "reserve_date_to" ), dateSpan ) );
                    dtoRsvListData[count].setIdentifyNo( Integer.parseInt( identifyNo ) );
                }

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvList.getData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return ret;
    }

    /****
     * 1016電文送信
     * 
     * @param hotelId ハピホテホテルID
     * @param sendKind 01:データ取得要求 02:当日予約通知
     * @return
     */

    public boolean sendToHost(int hotelId, String reserveNo)
    {
        final int sendKind = 2; // 当日予約
        return sendToHost( hotelId, sendKind, reserveNo );
    }

    public boolean sendToHost(int hotelId)
    {
        final int sendKind = 3; // 情報変更通知
        return sendToHost( hotelId, sendKind, "" );
    }

    public boolean sendToHost(int hotelId, int sendKind, String reserveNo)
    {

        String sendData = "";
        TcpClientEx tcpclient = null;
        String recvData = "";
        char[] charData = null;
        String data = "";
        int retryCount = 0;
        int sendCount = 0;
        String licenceKey = "";
        boolean ret = false;

        // ホスト側データ送信
        tcpclient = new TcpClientEx();
        // 指定のipアドレスに接続
        ret = tcpclient.connectServiceByAddr( HotelIp.getFrontIp( hotelId ), TIMEOUT, HAPIHOTE_PORT_NO );
        if ( ret != false )
        {
            licenceKey = getLicenceKeyForPMS( hotelId );
            for( int i = 0 ; i < dtoRsvListData.length ; i++ )
            {
                try
                {
                    sendData = COMMAND;
                    if ( sendKind == 2 && dtoRsvListData[i].getRsvNo().equals( reserveNo ) )
                    {
                        sendData += tcpclient.rightFitZeroFormat( Integer.toString( sendKind ), 2 );
                    }
                    else if ( sendKind == 1 )
                    {
                        sendData += tcpclient.rightFitZeroFormat( "01", 2 );
                    }
                    else
                    {
                        sendData += tcpclient.rightFitZeroFormat( "03", 2 );
                    }
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( i + 1 ), 4 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData.length ), 4 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getStatus() ), 2 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getExtFlag() ), 2 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getUserId(), 32 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getRsvNo(), 32 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getDispRsvNo(), 6 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getArrivalDate() ), 8 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getArrivalTime() ), 6 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getPlanType() ), 2 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getRoomName(), 8 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getRoomNameListLength() ), 2 );
                    // 要素数だけ繰り返す
                    for( int j = 0 ; j < (dtoRsvListData[i].getRoomNameListLength() > MAX_ROOM_LENGTH ? MAX_ROOM_LENGTH : dtoRsvListData[i].getRoomNameListLength()) ; j++ )
                    {
                        sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getRoomNameList()[j], 8 );
                    }
                    if ( dtoRsvListData[i].getRoomNameListLength() < MAX_ROOM_LENGTH )
                    {
                        sendData += tcpclient.leftFitFormat( "", (MAX_ROOM_LENGTH - dtoRsvListData[i].getRoomNameListLength()) * 8 );
                    }
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getPlanNm(), 64 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getChargeTotal() ), 9 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getPayment() ), 9 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getUsedMile() ), 9 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getCiTimeFrom() ), 4 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getCiTimeTo() ), 4 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getPossibleTime() ), 4 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getCoTime() ), 4 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getName(), 40 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getNameKana(), 40 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getTel(), 15 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getNumMan() ), 2 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getNumWoman() ), 2 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getNumChild() ), 2 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getOption1(), 200 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getOption0(), 200 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getRsvDate() ), 8 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getCar(), 40 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getRemarks(), 200 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getDemands(), 200 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getRoomRank(), 128 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getIdentifyNo() ), 4 );

                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getCountry(), 32 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getPaymentAll() ), 9 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getReserveDateTo() ), 8 );
                    sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getCountChargeList() ), 3 );
                    for( int j = 0 ; j < dtoRsvListData[i].getCountChargeList() ; j++ )
                    {
                        sendData += tcpclient.rightFitZeroFormat( Integer.toString( dtoRsvListData[i].getChargeList()[j] ), 9 );
                    }
                    if ( dtoRsvListData[i].getCountChargeList() < MAX_CHARGELIST_LENGTH )
                    {
                        sendData += tcpclient.rightFitZeroFormat( "0", (MAX_CHARGELIST_LENGTH - dtoRsvListData[i].getCountChargeList()) * 9 );
                    }
                    sendData += tcpclient.leftFitFormat( licenceKey, 6 );
                    sendData += tcpclient.leftFitFormat( dtoRsvListData[i].getReserve(), 857 );
                    dtoRsvListData[i].setHeader( tcpclient.getPacketHapihoteHeader( Integer.toString( hotelId ), sendData.getBytes( "Windows-31J" ).length ) );
                    sendData = dtoRsvListData[i].getHeader() + sendData;
                    int roop = 0;

                    while( true )
                    {
                        // 電文送信
                        tcpclient.send( sendData );

                        // 受信待機
                        recvData = tcpclient.recv();

                        roop++;
                        if ( recvData.indexOf( "exception" ) >= 0 )
                        {
                            Logging.error( "電文受信Exception " + recvData );
                        }
                        else
                        {
                            charData = new char[recvData.length()];
                            charData = recvData.toCharArray();

                            // コマンド取得
                            data = new String( charData, HEADER_LENGTH, COMMAND_LENGTH );

                            // 応答電文コマンドが1017なら正しい応答
                            if ( data.compareTo( REPLY_COMMAND ) == 0 )
                            {
                                // 返ってきた情報をセット
                                dtoRsvListData[i].setResult( Integer.valueOf( new String( charData, 36, 4 ) ).intValue() );
                                dtoRsvListData[i].setCount( Integer.valueOf( new String( charData, 40, 2 ) ).intValue() );
                                dtoRsvListData[i].setErrorCode( Integer.valueOf( new String( charData, 42, 4 ) ).intValue() );
                                dtoRsvListData[i].setIdentifyNo( Integer.valueOf( new String( charData, 46, 4 ) ).intValue() );
                                dtoRsvListData[i].setGuidRoomName( new String( charData, 50, 8 ) );
                                // 正常を返して成功とする
                                ret = true;
                                sendCount++;
                            }
                        }
                        if ( roop >= retryCount )
                        {
                            break;
                        }
                    }
                }
                catch ( SocketTimeoutException e )
                {
                    Logging.error( "[RsvList.sendToHost()]Exception:" + e.toString() );
                    // 送信エラーのときは、タイムアウト
                    dtoRsvListData[i].setErrorCode( 40001 );
                    ret = false;
                }
                catch ( Exception e )
                {
                    Logging.error( "[RsvList.sendToHost()]Exception:" + e.toString() );
                    // 送信エラーのときは、タイムアウト
                    dtoRsvListData[i].setErrorCode( 40002 );
                    ret = false;
                }
                finally
                {
                }
            }
        }
        setSendCount( sendCount );
        tcpclient.disconnectService();
        return ret;

    }

    /**
     * ホテル認証データ取得
     * 
     * @param hotelId ホテルId
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public String getLicenceKeyForPMS(int hotelId)
    {
        String licenceKey = "";
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT licence_key FROM hh_hotel_auth WHERE id = ? AND rsv_last_update <> 0";
        query += " ORDER BY rsv_last_update DESC,rsv_last_uptime DESC LIMIT 0,1";
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
                    licenceKey = result.getString( "licence_key" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[RsvList.getLicenceKeyForPMS] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(licenceKey);
    }
}
