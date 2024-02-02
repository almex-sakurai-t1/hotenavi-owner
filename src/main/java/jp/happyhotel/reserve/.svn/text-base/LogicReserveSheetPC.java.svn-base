package jp.happyhotel.reserve;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;

import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.OwnerRsvCommon;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.TimeCommon;

/**
 * 
 * 予約詳細画面 business Logic
 */
public class LogicReserveSheetPC implements Serializable
{
    private static final long   serialVersionUID = -545780924614467092L;
    private static final String labelUsr         = "名様";

    private FormReserveSheetPC  frm;

    /* フォームオブジェクト */
    public FormReserveSheetPC getFrm()
    {
        return frm;
    }

    public void setFrm(FormReserveSheetPC frm)
    {
        this.frm = frm;
    }

    /**
     * 予約情報取得
     * 
     * @param regKbn 1:実績データは参照しない、2:実績データを参照する
     * @return なし
     * @throws Exception
     */
    public void getData(int regKbn) throws Exception
    {
        // Logging.info( "LogicReserveSheetPC.getData getRsvData start", "LogicReserveSheetPC.getData" );
        // 予約データ取得
        getRsvData( regKbn );

        // Logging.info( "LogicReserveSheetPC.getData getRsvOptData start", "LogicReserveSheetPC.getData" );
        // 必須オプションデータ取得
        getRsvOptData( ReserveCommon.OPTION_IMP, regKbn );

        // 通常オプションデータ取得
        getRsvOptData( ReserveCommon.OPTION_USUAL, regKbn );

        // Logging.info( "LogicReserveSheetPC.getData getSeqList start", "LogicReserveSheetPC.getData" );
        // ホテルにある部屋全てを抽出
        getSeqList();

        // ノーショウクレジット請求かどうかを取得する
        frm.setNoshowCreditFlag( ReserveCommon.checkNoShowCreditHotel( frm.getSelHotelId() ) );

        // Logging.info( "LogicReserveSheetPC.getData setChargeStartTime start", "LogicReserveSheetPC.getData" );
        // 請求開始時刻を取得
        frm.setChargeStartTime( ReserveCommon.getChargeStartTime() );
    }

    /**
     * 予約登録データ取得
     * 
     * @param regKbn 1:実績データは参照しない、2:実績データを参照する、99:履歴データを参照する
     * @return なし
     */
    public void getRsvData(int regKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String convDate = "";
        String weekName = "";
        String estTime = "";
        int ciFrom = 0;
        int ciTo = 0;
        int co = 0;
        int coKind = 0;
        String cnvMoney;
        String ciTimeFrom = "";
        String ciTimeTo = "";
        String coTime = "";
        String checkInDate = "";
        String checkInTime = "";
        String checkInSeq = "";

        try
        {
            if ( regKbn == 99 )
            {
                // 履歴情報取得
                query = query + " SELECT hhb.name AS hotelName, hhb.pref_name AS prefName, hhb.address1 AS hotelAddr1, hhb.address2 AS hotelAddr2, ";
                query = query + " hhb.address3 AS hotelAddr3, hhb.tel1 AS hotelTel, hrp.plan_name, hrp.plan_pr, hrp.precaution as planRemarks, hrp.plan_type, hrr.ci_time_from, hrr.ci_time_to, hrr.co_time, ";
                query = query + " hrr.reserve_date, hrr.reserve_no, CASE WHEN hrr.room_hold=0 THEN hrr.seq ELSE hrr.room_hold END AS seq, hrr.basic_charge_total, hrr.charge_total,0 AS used_mile,0 AS payment, hrr.status, ";
                query = query + " 0 AS num_adult, hrr.num_child, hrr.num_man, hrr.num_woman, hrr.parking_count, hrr.highroof_count AS parking_high_roof_count, hrr.est_time_arrival, hrp.plan_id, 0 AS request_flag,";
                query = query + " hrr.user_id, hrr.name_last, hrr.name_first, hrr.name_last_kana, hrr.name_first_kana, ";
                query = query + " hrr.zip_code, hrr.address1, hrr.address2, hrr.address3, hrr.tel1, hrr.reminder_flag, hrr.parking, ";
                query = query + " hrr.mail_addr, hrrb.parking as hotelParking, hrrb.cancel_policy, hrr.demands, hrr.id, hrr.remarks, 0 AS offer_kind, hrp.question, 0 AS question_flag, hrr.noshow_flag, ";
                query = query + " hrr.co_kind, hrr.cancel_type AS cancel_kind, hrr.mail_addr1, hrr.mail_addr2, ";
                query = query + " hrr.payment, hrr.used_mile, hrr.reserve_date_to,";
                query = query + " hrp.foreign_flag,hrp.consecutive_flag,hrp.plan_sub_name,hrp.max_stay_num,hrp.min_stay_num, ";
                query = query + " hrr.basic_charge_total_all,hrr.charge_total_all, ";
                query = query + " rsvfinal.co_time AS final_co_time, rsvfinal.co_kind AS final_co_kind ";
                query = query + " FROM newRsvDB.hh_rsv_reserve_history hrr ";
                query = query + "   INNER JOIN hh_hotel_basic hhb ON ( hrr.id = hhb.id ) ";
                query = query + "   INNER JOIN newRsvDB.hh_rsv_plan hrp ON ( hrr.id = hrp.id AND hrr.plan_id = hrp.plan_id AND hrr.plan_sub_id=hrp.plan_sub_id) ";
                query = query + "   INNER JOIN hh_rsv_reserve_basic hrrb ON ( hrr.id = hrrb.id ) ";
                // 過去のデータを見る場合は、料金モードを参照しない（予約時と変わっている場合があるため）
                // query = query + "   INNER JOIN hh_rsv_day_charge hrdc ON (hrr.id = hrdc.id AND hrr.plan_id = hrdc.plan_id AND hrr.reserve_date = hrdc.cal_date) ";
                // query = query + "   INNER JOIN hh_rsv_plan_charge hrpc ON ( hrpc.id = hrdc.id AND hrpc.plan_id = hrdc.plan_id AND hrpc.charge_mode_id = hrdc.charge_mode_id ) ";
                query = query
                        + "   LEFT JOIN newRsvDB.hh_rsv_reserve rsvfinal ON hrr.id=rsvfinal.id AND hrr.reserve_no_main = rsvfinal.reserve_no_main AND hrr.reserve_date_to = rsvfinal.reserve_date AND rsvfinal.reserve_no_main != ''  ";
                query = query + " WHERE hrr.id = ? AND hrr.reserve_no = ? AND hrr.reserve_sub_no = ? ";
            }
            else if ( regKbn == 2 )
            {
                query = query + " SELECT hhb.name AS hotelName, hhb.pref_name AS prefName, hhb.address1 AS hotelAddr1, hhb.address2 AS hotelAddr2, ";
                query = query + " hhb.address3 AS hotelAddr3, hhb.tel1 AS hotelTel, hrp.plan_name, hrp.plan_pr, hrp.precaution as planRemarks, hrp.plan_type, hrr.ci_time_from, hrr.ci_time_to, hrr.co_time, ";
                query = query + " hrr.reserve_date, hrr.reserve_no, CASE WHEN hrr.room_hold=0 THEN hrr.seq ELSE hrr.room_hold END AS seq, hrr.basic_charge_total, hrr.charge_total, hrr.status, ";
                query = query + " hrr.num_adult, hrr.num_child, hrr.num_man, hrr.num_woman, hrr.parking_count, hrr.highroof_count AS parking_high_roof_count, hrr.est_time_arrival, hrp.plan_id, 0 AS request_flag,";
                query = query + " hrr.user_id, hrr.name_last, hrr.name_first, hrr.name_last_kana, hrr.name_first_kana, ";
                query = query + " hrr.zip_code, hrr.address1, hrr.address2, hrr.address3, hrr.tel1, hrr.reminder_flag, hrr.parking, ";
                query = query + " hrr.mail_addr, hrrb.parking as hotelParking, hrrb.cancel_policy, hrr.demands, hrr.id, hrr.remarks, 0 AS offer_kind, hrp.question, 0 AS question_flag, hrr.noshow_flag,  ";
                query = query + " hrr.co_kind, hrr.cancel_type AS cancel_kind, hrr.mail_addr1, hrr.mail_addr2,";
                query = query + " hrr.payment, hrr.used_mile, hrr.reserve_date_to, ";
                query = query + " hrp.foreign_flag,hrp.consecutive_flag,hrp.plan_sub_name,hrp.max_stay_num,hrp.min_stay_num,hrp.local_payment_kind,";
                query = query + " hrr.basic_charge_total_all,hrr.charge_total_all, ";
                // 区分が2の時だけ
                query = query + " hrr.accept_date,hresult.ci_date, hresult.regist_time, hresult.seq as checkinSeq, hrr.reserve_no_main, hrr.ext_flag, ";
                query = query + " rsvfinal.co_time AS final_co_time, rsvfinal.co_kind AS final_co_kind ";
                query = query + " FROM newRsvDB.hh_rsv_reserve hrr ";
                query = query + "   INNER JOIN hh_hotel_basic hhb ON ( hrr.id = hhb.id ) ";
                // 区分が2の時だけ
                query = query + "   LEFT JOIN hh_rsv_result hresult ON ( hrr.id = hresult.id AND hrr.reserve_no = hresult.reserve_no AND hresult.use_kind IN(1,3))";
                query = query + "   INNER JOIN newRsvDB.hh_rsv_plan hrp ON ( hrr.id = hrp.id AND hrr.plan_id = hrp.plan_id  AND hrr.plan_sub_id = hrp.plan_sub_id ) ";
                query = query + "   INNER JOIN newRsvDB.hh_rsv_reserve_basic hrrb ON ( hrr.id = hrrb.id ) ";
                query = query
                        + "   LEFT JOIN newRsvDB.hh_rsv_reserve rsvfinal ON hrr.id=rsvfinal.id AND hrr.reserve_no_main = rsvfinal.reserve_no_main AND hrr.reserve_date_to = rsvfinal.reserve_date AND rsvfinal.reserve_no_main != ''  ";
                query = query + " WHERE hrr.id = ? AND hrr.reserve_no = ? ";
            }
            else
            {
                query = query + " SELECT hhb.name AS hotelName, hhb.pref_name AS prefName, hhb.address1 AS hotelAddr1, hrp.plan_type, hhb.address2 AS hotelAddr2, ";
                query = query + " hhb.address3 AS hotelAddr3, hhb.tel1 AS hotelTel, hrp.plan_name, hrp.plan_pr, hrp.precaution as planRemarks, hrr.ci_time_from, hrr.ci_time_to, hrr.co_time, ";
                query = query + " hrr.reserve_date, hrr.reserve_no, CASE WHEN hrr.room_hold=0 THEN hrr.seq ELSE hrr.room_hold END AS seq, hrr.basic_charge_total, hrr.charge_total, hrr.status, ";
                query = query + " hrr.num_adult, hrr.num_child, hrr.num_man, hrr.num_woman, hrr.parking_count, hrr.highroof_count AS parking_high_roof_count, hrr.est_time_arrival, hrp.plan_id, 0 AS request_flag,";
                query = query + " hrr.user_id, hrr.name_last, hrr.name_first, hrr.name_last_kana, hrr.name_first_kana, ";
                query = query + " hrr.zip_code, hrr.address1, hrr.address2, hrr.address3, hrr.tel1, hrr.reminder_flag, hrr.parking, ";
                query = query + " hrr.mail_addr, hrrb.parking as hotelParking, hrrb.cancel_policy, hrr.demands, hrr.id, hrr.remarks, 0 AS offer_kind, hrp.question, 0 AS question_flag, hrr.noshow_flag, ";
                query = query + " hrr.co_kind,  hrr.cancel_type AS cancel_kind, hrr.mail_addr1, hrr.mail_addr2, ";
                query = query + " hrr.payment, hrr.used_mile, hrr.reserve_date_to, ";
                query = query + " hrp.foreign_flag,hrp.consecutive_flag,hrp.plan_sub_name,hrp.max_stay_num,hrp.min_stay_num, ";
                query = query + " hrr.basic_charge_total_all,hrr.charge_total_all, ";
                query = query + " rsvfinal.co_time AS final_co_time, rsvfinal.co_kind AS final_co_kind ";
                query = query + " FROM newRsvDB.hh_rsv_reserve hrr ";
                query = query + "   INNER JOIN hh_hotel_basic hhb ON ( hrr.id = hhb.id ) ";
                query = query + "   INNER JOIN newRsvDB.hh_rsv_plan hrp ON ( hrr.id = hrp.id AND hrr.plan_id = hrp.plan_id  AND hrr.plan_sub_id = hrp.plan_sub_id ) ";
                query = query + "   INNER JOIN newRsvDB.hh_rsv_reserve_basic hrrb ON ( hrr.id = hrrb.id ) ";
                query = query
                        + "   LEFT JOIN newRsvDB.hh_rsv_reserve rsvfinal ON hrr.id=rsvfinal.id AND hrr.reserve_no_main = rsvfinal.reserve_no_main AND hrr.reserve_date_to = rsvfinal.reserve_date AND rsvfinal.reserve_no_main != ''  ";
                query = query + " WHERE hrr.id = ? AND hrr.reserve_no = ? ";
            }
            // Logging.info( "LogicReserveSheetPC.getRsvData query=" + query );

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( regKbn == 99 )
            {
                prestate.setInt( 1, frm.getSelHotelId() );
                prestate.setString( 2, frm.getRsvNo() );
                prestate.setInt( 3, frm.getRsvSubNo() );
                // Logging.info( "LogicReserveSheetPC.getRsvData set=" + frm.getSelHotelId() + "," + frm.getRsvNo() + "," + frm.getRsvSubNo() );
            }
            else
            {
                prestate.setInt( 1, frm.getSelHotelId() );
                prestate.setString( 2, frm.getRsvNo() );
                // Logging.info( "LogicReserveSheetPC.getRsvData set=" + frm.getSelHotelId() + "," + frm.getRsvNo() );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {

                // 全件抽出
                if ( result.next() != false )
                {
                    frm.setSelHotelId( frm.getSelHotelId() );
                    frm.setSelPlanId( result.getInt( "plan_id" ) );
                    frm.setRsvNo( result.getString( "reserve_no" ) );
                    frm.setHotelNm( ConvertCharacterSet.convDb2Form( result.getString( "hotelName" ) ) );
                    frm.setHotelAddr( ConvertCharacterSet.convDb2Form( result.getString( "prefName" ).toString() ) +
                            ConvertCharacterSet.convDb2Form( result.getString( "hotelAddr1" ).toString() )
                            + ConvertCharacterSet.convDb2Form( result.getString( "hotelAddr2" ).toString() )
                            + ConvertCharacterSet.convDb2Form( result.getString( "hotelAddr3" ).toString() ) );
                    frm.setHotelTel( result.getString( "hotelTel" ) );
                    if ( result.getInt( "foreign_flag" ) == 1 )
                    {
                        frm.setPlanNm( "LIJ " + OwnerRsvCommon.getFroeignPlanName( result.getString( "plan_name" ).toString(), result.getInt( "consecutive_flag" ), result.getInt( "min_stay_num" ), result.getInt( "max_stay_num" ),
                                result.getString( "plan_sub_name" ) ) );
                    }
                    else if ( result.getInt( "foreign_flag" ) == 2 )
                    {
                        frm.setPlanNm( "[E]" + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ).toString() ) ) );
                    }
                    else if ( result.getInt( "plan_type" ) == 2 || result.getInt( "plan_type" ) == 4 )
                    {
                        frm.setPlanNm( "[休]" + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ).toString() ) ) );
                    }
                    else
                    {
                        frm.setPlanNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ).toString() ) ) );
                    }
                    frm.setPlanType( result.getInt( "plan_type" ) );
                    frm.setPlanPr( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_pr" ).toString() ) ) );
                    frm.setDispRemarks( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "planRemarks" ).toString() ) ) );
                    frm.setDispRequestFlag( result.getInt( "request_flag" ) );
                    convDate = String.valueOf( result.getInt( "reserve_date" ) );
                    weekName = DateEdit.getWeekName( result.getInt( "reserve_date" ) );
                    frm.setRsvDateView( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 )
                            + "/" + convDate.substring( 6, 8 ) + "(" + weekName + ")" );
                    frm.setRsvDate( result.getInt( "reserve_date" ) );
                    frm.setSeq( result.getInt( "seq" ) );
                    NumberFormat objNum = NumberFormat.getCurrencyInstance();
                    if ( result.getInt( "basic_charge_total_all" ) == 0 )
                    {
                        cnvMoney = objNum.format( result.getInt( "basic_charge_total" ) );
                        frm.setBasicTotal( result.getInt( "basic_charge_total" ) );
                    }
                    else
                    {
                        cnvMoney = objNum.format( result.getInt( "basic_charge_total_all" ) );
                        frm.setBasicTotal( result.getInt( "basic_charge_total_all" ) );
                    }
                    frm.setBasicTotalView( cnvMoney );
                    frm.setAdultNumView( String.valueOf( result.getInt( "num_adult" ) ) + labelUsr );
                    frm.setAdultNum( result.getInt( "num_adult" ) );
                    frm.setChildNumView( String.valueOf( result.getInt( "num_child" ) ) + labelUsr );
                    frm.setChildNum( result.getInt( "num_child" ) );
                    frm.setManNum( result.getInt( "num_man" ) );
                    frm.setManNumView( String.valueOf( result.getInt( "num_man" ) ) + labelUsr );
                    frm.setWomanNum( result.getInt( "num_woman" ) );
                    frm.setWomanNumView( String.valueOf( result.getInt( "num_woman" ) ) + labelUsr );
                    if ( result.getInt( "charge_total_all" ) == 0 )
                    {
                        cnvMoney = objNum.format( result.getInt( "charge_total" ) );
                        frm.setChargeTotalView( cnvMoney );
                        frm.setChargeTotal( result.getInt( "charge_total" ) );
                    }
                    else
                    {
                        cnvMoney = objNum.format( result.getInt( "charge_total_all" ) );
                        frm.setChargeTotalView( cnvMoney );
                        frm.setChargeTotal( result.getInt( "charge_total_all" ) );
                    }

                    // チェックイン開始可能時間、チェックイン終了可能時間を取得
                    ciFrom = result.getInt( "ci_time_from" );
                    ciTo = result.getInt( "ci_time_to" );
                    TimeCommon timeCommon = new TimeCommon();
                    boolean ret = timeCommon.getRsvTime( frm.getSelHotelId(), frm.getRsvDate(), frm.getRsvNo() );
                    if ( ret )
                    {
                        ciFrom = timeCommon.getCiTimeFrom();
                        ciTo = timeCommon.getCiTimeTo();
                    }

                    if ( result.getInt( "reserve_date_to" ) != 0 && result.getInt( "reserve_date" ) < result.getInt( "reserve_date_to" ) )
                    {
                        // 連泊のとき
                        co = result.getInt( "final_co_time" );
                        coKind = result.getInt( "final_co_kind" );
                    }
                    else
                    {
                        co = result.getInt( "co_time" );
                        coKind = result.getInt( "co_kind" );
                    }

                    ciTimeFrom = ConvertTime.convTimeStr( ciFrom, 3 );
                    ciTimeTo = ConvertTime.convTimeStr( ciTo, 3 );
                    if ( coKind == 1 )
                    {
                        int dateSpan = 0;
                        if ( ciFrom > co ) // 翌日
                        {
                            dateSpan = 1;
                        }
                        coTime = DateEdit.formatDate( 5, (result.getInt( "reserve_date_to" ) == 0 ? DateEdit.addDay( result.getInt( "reserve_date" ), dateSpan ) : DateEdit.addDay( result.getInt( "reserve_date_to" ), dateSpan )) ) + " "
                                + ConvertTime.convTimeStr( co, 3 );
                    }
                    else
                    {
                        coTime = "チェックインから" + DateEdit.formatTime( 6, co );
                    }
                    Logging.info( "coKind" + coKind + ",coTime" + coTime );

                    frm.setCiTime( ciTimeFrom );
                    frm.setCiTimeToView( ciTimeTo );
                    frm.setCiTimeFrom( ciFrom );
                    frm.setCiTimeTo( ciTo );
                    frm.setCoTimeView( coTime );

                    if ( result.getInt( "hotelParking" ) == ReserveCommon.PARKING_NO_INPUT )
                    {
                        frm.setParkingUsed( ReserveCommon.PARKING_NO_PARKNG );
                    }
                    else
                    {
                        if ( result.getInt( "parking" ) == ReserveCommon.PRKING_USED_USE )
                        {
                            // 利用する
                            frm.setParkingUsed( ReserveCommon.PARKING_USE );
                        }
                        else
                        {
                            // 利用しない
                            frm.setParkingUsed( ReserveCommon.PARKING_NOT_USE );
                        }
                    }
                    frm.setParkingCnt( result.getInt( "parking_count" ) );
                    frm.setHiRoofCnt( result.getInt( "parking_high_roof_count" ) );
                    estTime = ReserveCommon.getArrivalTimeView( result.getInt( "est_time_arrival" ) );
                    frm.setEstTimeArrivalView( estTime );
                    frm.setEstTimeArrival( result.getInt( "est_time_arrival" ) );
                    frm.setUserId( result.getString( "user_id" ) );
                    frm.setMail( "" );
                    if ( result.getInt( "foreign_flag" ) == 1 )
                    {
                        frm.setName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_last" ) ) )
                                + " "
                                + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_first" ) ) )
                                + "（" + ConvertCharacterSet.convDb2Form( result.getString( "address1" ).toString() )
                                + "）" );
                    }
                    else
                    {
                        frm.setName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_last" ) ) )
                                + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_first" ) ) )
                                + "（" + ConvertCharacterSet.convDb2Form( result.getString( "name_last_kana" ).toString() )
                                + ConvertCharacterSet.convDb2Form( result.getString( "name_first_kana" ).toString() ) + "）" );
                    }
                    // Logging.info( "LogicReserveSheetPC.getRsvData zip_code=" + result.getString( "zip_code" ) );

                    if ( result.getString( "zip_code" ).length() >= 7 )
                    {
                        frm.setZip( result.getString( "zip_code" ).substring( 0, 3 ) + " - " + result.getString( "zip_code" ).substring( 3 ) );
                    }
                    else
                    {
                        frm.setZip( result.getString( "zip_code" ) );
                    }
                    // else
                    // {
                    // frm.setZip( "" );
                    // }
                    frm.setAddress( ConvertCharacterSet.convDb2Form( result.getString( "address1" ).toString() )
                            + ConvertCharacterSet.convDb2Form( result.getString( "address2" ).toString() )
                            + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "address3" ) ) ) );

                    if ( regKbn == 2 )
                    {
                        String smsPhoneNo = "";
                        if ( result.getInt( "payment" ) == 2 // 現地払い
                                && result.getInt( "local_payment_kind" ) == 2 ) // SMS認証させる
                        {
                            smsPhoneNo = ReserveCommon.getSmsPhoneNo( connection, result.getString( "user_id" ), result.getInt( "accept_date" ) );
                        }
                        frm.setTel( smsPhoneNo.equals( "" ) ? result.getString( "tel1" ) : smsPhoneNo + "(S)" );
                        frm.setRsvNoMain( result.getString( "reserve_no_main" ) );
                        frm.setExtFlag( result.getInt( "ext_flag" ) );
                    }
                    else
                    {
                        frm.setTel( result.getString( "tel1" ) );
                    }
                    frm.setReminder( result.getInt( "reminder_flag" ) );
                    if ( result.getInt( "reminder_flag" ) == 1 )
                    {
                        frm.setReminderView( ReserveCommon.REMINDER_ON );
                    }
                    else
                    {
                        frm.setReminderView( ReserveCommon.REMINDER_OFF );
                    }
                    // #3182 20011-04-20
                    if ( result.getInt( "reminder_flag" ) == 1 && result.getString( "mail_addr" ).trim().length() > 0 )
                    {
                        frm.setRemainderMail( ReserveCommon.REMINDER_MAIL + result.getString( "mail_addr" ).trim() );
                    }
                    if ( result.getString( "demands" ) != null )
                    {
                        frm.setDemands( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "demands" ).toString() ) ) );
                        frm.setDemandsView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "demands" ).toString() ) ) );
                    }
                    frm.setDispRequestFlag( result.getInt( "request_flag" ) );
                    if ( result.getString( "cancel_policy" ) != null )
                    {
                        frm.setCahcelPolicy( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "cancel_policy" ).toString() ) ) );
                    }
                    frm.setOfferKind( result.getInt( "offer_kind" ) );
                    frm.setStatus( result.getInt( "status" ) );
                    if ( regKbn == 2 )
                    {
                        if ( result.getString( "ci_date" ) != null )
                        {
                            convDate = result.getString( "ci_date" );
                            weekName = DateEdit.getWeekName( result.getInt( "ci_date" ) );
                            checkInDate = convDate.substring( 0, 4 ) + "/"
                                    + convDate.substring( 4, 6 ) + "/"
                                    + convDate.substring( 6, 8 ) + "(" + weekName + ")";
                            checkInSeq = result.getString( "checkinSeq" );
                        }
                        if ( result.getString( "regist_time" ) != null )
                        {
                            checkInTime = ConvertTime.convTimeStr( result.getInt( "regist_time" ), 3 );
                        }
                        frm.setCheckInDate( checkInDate );
                        frm.setCheckInSeq( checkInSeq );
                        frm.setCheckInTime( checkInTime );
                    }
                    frm.setNoShow( result.getInt( "noshow_flag" ) );
                    if ( result.getString( "question" ) != null )
                    {
                        frm.setQuestion( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "question" ) ) ) );
                    }
                    frm.setQuestionFlg( result.getInt( "question_flag" ) );
                    if ( result.getString( "remarks" ) != null )
                    {
                        frm.setRemarks( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ).toString() ) ) );
                        frm.setRemarksView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ).toString() ) ) );
                    }
                    frm.setCancelKind( result.getInt( "cancel_kind" ) );
                    frm.setMailAddr1( result.getString( "mail_addr1" ) );
                    frm.setMailAddr2( result.getString( "mail_addr2" ) );
                    frm.setPayment( result.getInt( "payment" ) );
                    frm.setUsedMile( result.getInt( "used_mile" ) );
                    cnvMoney = objNum.format( result.getInt( "used_mile" ) );
                    frm.setUsedMileView( cnvMoney );

                    int payCredit = 0;
                    if ( result.getInt( "payment" ) == ReserveCommon.PAYMENT_CREDIT )
                    {
                        if ( result.getInt( "charge_total_all" ) == 0 )
                        {
                            payCredit = result.getInt( "charge_total" ) - result.getInt( "used_mile" );
                        }
                        else
                        {
                            payCredit = result.getInt( "charge_total_all" ) - result.getInt( "used_mile" );
                        }
                    }
                    frm.setPayCredit( payCredit );
                    cnvMoney = objNum.format( payCredit );
                    frm.setPayCreditView( cnvMoney );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveSheetPC.getRsvData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * ホテルに存在する部屋を取得
     * 
     * @param なし
     * @return なし
     */
    private void getSeqList()
    {
        String seqTbl = "";
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        ArrayList<Integer> seqList = new ArrayList<Integer>();

        try
        {
            // すでに利用済みの部屋番号を実績データから取得する。
            query = "SELECT seq FROM hh_rsv_result WHERE id = ? AND ci_date = ? AND use_kind IN(1,3)";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, frm.getRsvDate() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                seqTbl += " AND seq NOT IN (" + String.valueOf( result.getInt( "seq" ) ) + ")";
            }

            result.close();
            prestate.clearParameters();
            prestate.close();

            // 利用済みの部屋番号以外の部屋を取得する。
            query = "SELECT seq FROM hh_hotel_room_more WHERE id = ? AND disp_flag = 1 AND seq <>0";
            if ( !seqTbl.equals( "" ) )
            {
                query += seqTbl;
            }
            query = query + " ORDER BY seq";

            // Logging.info( query );
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );

            result = prestate.executeQuery();
            while( result.next() )
            {
                seqList.add( result.getInt( "seq" ) );
            }
            frm.setSeqList( seqList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveSheetPC.getSeqList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 予約・オプションデータ取得
     * 
     * @param optionFlg オプションフラグ
     * @param regKbn regKbn 1:実績データは参照しない、2:実績データを参照する、99:履歴データを参照する
     * @return FormReserveSheetPCオブジェクト
     */
    public void getRsvOptData(int optionFlg, int regKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> optNmList = new ArrayList<String>();
        ArrayList<String> optSubNmList = new ArrayList<String>();
        ArrayList<Integer> quantityList = new ArrayList<Integer>();
        ArrayList<Integer> unitPriceList = new ArrayList<Integer>();
        ArrayList<String> unitPriceViewList = new ArrayList<String>();
        ArrayList<String> chargeTotalList = new ArrayList<String>();
        ArrayList<String> remarksList = new ArrayList<String>();
        ArrayList<Integer> numberList = new ArrayList<Integer>();
        NumberFormat objNum = NumberFormat.getCurrencyInstance();

        try
        {
            if ( regKbn == 99 )
            {
                query = query + "SELECT ";
                query = query + " op.option_id, op.option_sub_id, mst.option_name, mst.option_sub_name, op.quantity, op.unit_price, op.charge_total, op.remarks, 0 AS number ";
                query = query + "FROM hh_rsv_rel_reserve_option_history op ";
                query = query + "   LEFT JOIN hh_rsv_option mst ON op.id = mst.id AND op.option_id = mst.option_id AND op.option_sub_id = mst.option_sub_id ";
                query = query + " WHERE op.id = ? ";
                query = query + "   AND op.reserve_no = ? ";
                query = query + "   AND mst.option_flag = ? ";
                query = query + "   AND op.reserve_sub_no = ? ";
                query = query + "UNION SELECT ";
                query = query + " op.option_id, op.option_sub_id, mst.option_name, mst.option_sub_name, op.quantity, op.unit_price, op.charge_total, op.remarks, op.number ";
                query = query + "FROM newRsvDB.hh_rsv_rel_reserve_option_history op ";
                query = query + "   LEFT JOIN newRsvDB.hh_rsv_option mst ON op.id = mst.id AND op.option_id = mst.option_id AND op.option_sub_id = mst.option_sub_id ";
                query = query + " WHERE op.id = ? ";
                query = query + "   AND op.reserve_no = ? ";
                query = query + "   AND mst.option_flag = ? ";
                query = query + "   AND op.reserve_sub_no = ? ";
                query = query + "   AND op.quantity <> 0 ";
                query = query + "ORDER BY option_id, number, option_sub_id";
            }
            else
            {
                query = query + "SELECT ";
                query = query + " op.option_id, op.option_sub_id, mst.option_name, mst.option_sub_name, op.quantity, op.unit_price, op.charge_total, op.remarks, 0 AS number ";
                query = query + "FROM hh_rsv_rel_reserve_option op ";
                query = query + "   LEFT JOIN hh_rsv_option mst ON op.id = mst.id AND op.option_id = mst.option_id AND op.option_sub_id = mst.option_sub_id ";
                query = query + " WHERE op.id = ? ";
                query = query + "   AND op.reserve_no = ? ";
                query = query + "   AND mst.option_flag = ? ";
                query = query + "UNION SELECT ";
                query = query + " op.option_id, op.option_sub_id, mst.option_name, mst.option_sub_name, op.quantity, op.unit_price, op.charge_total, op.remarks, op.number ";
                query = query + "FROM newRsvDB.hh_rsv_rel_reserve_option op ";
                query = query + "   LEFT JOIN newRsvDB.hh_rsv_option mst ON op.id = mst.id AND op.option_id = mst.option_id AND op.option_sub_id = mst.option_sub_id ";
                query = query + " WHERE op.id = ? ";
                query = query + "   AND op.reserve_no = ? ";
                query = query + "   AND mst.option_flag = ? ";
                query = query + "   AND op.quantity <> 0 ";
                query = query + "ORDER BY option_id, number, option_sub_id";
            }
            // Logging.info( "[LogicReserveSheetPC.getRsvOptData] query=" + query + ",id=" + frm.getSelHotelId() + ",reserve_no=" + frm.getRsvNo() + ",optionFlg=" + optionFlg );

            connection = DBConnection.getConnection();
            if ( regKbn == 99 )
            {
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, frm.getSelHotelId() );
                prestate.setString( 2, frm.getRsvNo() );
                prestate.setInt( 3, optionFlg );
                prestate.setInt( 4, frm.getRsvSubNo() );
                prestate.setInt( 5, frm.getSelHotelId() );
                prestate.setString( 6, frm.getRsvNo() );
                prestate.setInt( 7, optionFlg );
                prestate.setInt( 8, frm.getRsvSubNo() );
            }
            else
            {
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, frm.getSelHotelId() );
                prestate.setString( 2, frm.getRsvNo() );
                prestate.setInt( 3, optionFlg );
                prestate.setInt( 4, frm.getSelHotelId() );
                prestate.setString( 5, frm.getRsvNo() );
                prestate.setInt( 6, optionFlg );
            }
            result = prestate.executeQuery();

            while( result.next() )
            {
                optNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                optSubNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_sub_name" ) ) ) );
                quantityList.add( result.getInt( "quantity" ) );
                unitPriceList.add( result.getInt( "unit_price" ) );
                unitPriceViewList.add( objNum.format( result.getInt( "unit_price" ) ) );
                chargeTotalList.add( objNum.format( result.getInt( "charge_total" ) ) );
                remarksList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ) ) ) );
                numberList.add( result.getInt( "number" ) );
            }

            if ( optionFlg == ReserveCommon.OPTION_IMP )
            {
                frm.setOptNmImpList( optNmList );
                frm.setOptSubNmImpList( optSubNmList );
                frm.setOptQuantityImpList( quantityList );
                frm.setOptNumberList( numberList );
            }
            else
            {
                frm.setOptNmList( optNmList );
                frm.setOptSubNmList( optSubNmList );
                frm.setOptInpMaxQuantityList( quantityList );
                frm.setOptUnitPriceList( unitPriceList );
                frm.setOptUnitPriceViewList( unitPriceViewList );
                frm.setOptChargeTotalList( chargeTotalList );
                frm.setOptRemarksList( remarksList );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveSheetPC.getRsvOptData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 予約番号からホテルIDを取得
     * 
     * @param rsvNo 予約番号
     * @return ホテルID
     */
    public int getHotelId(String rsvNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int hotelId = 0;

        try
        {
            query = query + "SELECT id FROM hh_rsv_reserve ";
            query = query + " WHERE reserve_no = ? ";
            query = query + "UNION SELECT id FROM newRsvDB.hh_rsv_reserve ";
            query = query + " WHERE reserve_no = ? ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, rsvNo );
            result = prestate.executeQuery();

            while( result.next() )
            {
                hotelId = result.getInt( "id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveSheetPC.getRsvData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(hotelId);
    }

    /**
     * 予約登録データ取得
     * 
     * @param なし
     * @return なし
     */
    public void getRsvData() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        try
        {
            query = query + " SELECT atc.seq AS ci_seq ,atc.sub_seq AS ci_sub_seq ,atc.ci_status AS ci_status ,hhb.name AS hotelName, hhb.pref_name AS prefName, hhb.address1 AS hotelAddr1, hhb.address2 AS hotelAddr2, ";
            query = query + " hhb.address3 AS hotelAddr3, hhb.tel1 AS hotelTel, hrp.plan_name, hrp.plan_pr, hrr.ci_time_from, hrr.co_time, ";
            query = query + " hrr.reserve_date, hrr.reserve_no, CASE WHEN hrr.room_hold=0 THEN hrr.seq ELSE hrr.room_hold END AS seq, hrr.basic_charge_total, hrr.charge_total, hrr.status, ";
            query = query + " hrr.num_adult, hrr.num_child, hrr.num_man, hrr.num_woman, hrr.parking_count, hrr.highroof_count AS parking_high_roof_count, hrr.est_time_arrival, hrp.plan_id,hrp.plan_sub_id, 0 AS request_flag, ";
            query = query + " hrr.user_id, hrr.name_last, hrr.name_first, hrr.name_last_kana, hrr.name_first_kana, ";
            query = query + " hrr.zip_code, hrr.address1, hrr.address2, hrr.address3, hrr.tel1, hrr.reminder_flag, hrr.parking, ";
            query = query + " hrr.mail_addr, hrrb.parking as hotelParking, hrrb.cancel_policy, hrr.demands, hrr.id, hrr.remarks, 0 AS offer_kind, hrr.noshow_flag, hrr.co_kind  ";
            query = query + " ,hresult.ci_date, hresult.regist_time, hresult.seq as checkinSeq ";
            query = query + " ,hrr.payment,hrr.payment_status,hrr.consumer_demands,hrr.add_bonus_mile,hrr.used_mile ";
            query = query + " ,hrr.charge_total_all,hrr.reserve_date_to, hrr.ext_flag, hrr.reserve_no_main ";
            query = query + " FROM newRsvDB.hh_rsv_reserve hrr ";
            query = query + "   INNER JOIN hh_hotel_basic hhb ON ( hrr.id = hhb.id ) ";
            query = query + "   LEFT JOIN hh_rsv_result hresult ON ( hrr.id = hresult.id AND hrr.reserve_no = hresult.reserve_no AND hresult.use_kind IN(1,3))";
            query = query + "   INNER JOIN newRsvDB.hh_rsv_plan hrp ON ( hrr.id = hrp.id AND hrr.plan_id = hrp.plan_id AND hrr.plan_sub_id = hrp.plan_sub_id) ";
            query = query + "   INNER JOIN newRsvDB.hh_rsv_reserve_basic hrrb ON ( hrr.id = hrrb.id ) ";
            query = query + "   INNER JOIN hh_hotel_ci atc ON hrr.id = atc.id  AND hrr.reserve_no = atc.rsv_no AND atc.ci_status = 0 AND atc.seq = ? ";
            query = query + " WHERE hrr.id = ? AND hrr.user_id = ?";
            query = query + " AND hrr.status = 2";
            query = query + " UNION SELECT 0 AS ci_seq ,0 AS ci_sub_seq ,0 AS ci_status ,hhb.name AS hotelName, hhb.pref_name AS prefName, hhb.address1 AS hotelAddr1, hhb.address2 AS hotelAddr2, ";
            query = query + " hhb.address3 AS hotelAddr3, hhb.tel1 AS hotelTel, hrp.plan_name, hrp.plan_pr, hrr.ci_time_from, hrr.co_time, ";
            query = query + " hrr.reserve_date, hrr.reserve_no, CASE WHEN hrr.room_hold=0 THEN hrr.seq ELSE hrr.room_hold END AS seq, hrr.basic_charge_total, hrr.charge_total, hrr.status, ";
            query = query + " hrr.num_adult, hrr.num_child, hrr.num_man, hrr.num_woman, hrr.parking_count, hrr.highroof_count AS parking_high_roof_count, hrr.est_time_arrival, hrp.plan_id, hrp.plan_sub_id, 0 AS request_flag, ";
            query = query + " hrr.user_id, hrr.name_last, hrr.name_first, hrr.name_last_kana, hrr.name_first_kana, ";
            query = query + " hrr.zip_code, hrr.address1, hrr.address2, hrr.address3, hrr.tel1, hrr.reminder_flag, hrr.parking, ";
            query = query + " hrr.mail_addr, hrrb.parking as hotelParking, hrrb.cancel_policy, hrr.demands, hrr.id, hrr.remarks, 0 AS offer_kind, hrr.noshow_flag, hrr.co_kind  ";
            query = query + " ,hresult.ci_date, hresult.regist_time, hresult.seq as checkinSeq ";
            query = query + " ,hrr.payment,hrr.payment_status,hrr.consumer_demands,hrr.add_bonus_mile,hrr.used_mile ";
            query = query + " ,hrr.charge_total_all,hrr.reserve_date_to, hrr.ext_flag, hrr.reserve_no_main ";
            query = query + " FROM newRsvDB.hh_rsv_reserve hrr ";
            query = query + "   INNER JOIN hh_hotel_basic hhb ON ( hrr.id = hhb.id ) ";
            query = query + "   LEFT JOIN hh_rsv_result hresult ON ( hrr.id = hresult.id AND hrr.reserve_no = hresult.reserve_no AND hresult.use_kind IN(1,3))";
            query = query + "   INNER JOIN newRsvDB.hh_rsv_plan hrp ON ( hrr.id = hrp.id AND hrr.plan_id = hrp.plan_id AND hrr.plan_sub_id = hrp.plan_sub_id) ";
            query = query + "   INNER JOIN newRsvDB.hh_rsv_reserve_basic hrrb ON ( hrr.id = hrrb.id ) ";
            query = query + " WHERE hrr.id = ? AND hrr.user_id = ?";
            query = query + " AND hrr.status = 1";
            if ( frm.getRsvDate() > 0 )
            {
                query = query + " AND hrr.reserve_date = ?";
            }

            query = query + " ORDER BY ci_seq DESC, ci_sub_seq DESC LIMIT 0,1";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int i = 1;
            prestate.setInt( i++, frm.getCiSeq() );
            prestate.setInt( i++, frm.getSelHotelId() );
            prestate.setString( i++, frm.getUserId() );

            prestate.setInt( i++, frm.getSelHotelId() );
            prestate.setString( i++, frm.getUserId() );

            if ( frm.getRsvDate() > 0 )
            {
                prestate.setInt( i++, frm.getRsvDate() );
            }
            result = prestate.executeQuery();

            setData( result );

            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );

            query = " SELECT country FROM hh_user_basic_foreign WHERE user_id = ?";
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, frm.getUserId() );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    frm.setCountry( result.getString( "country" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveSheetPC.getRsvData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 予約登録データ取得(予約番号で取得)
     * 
     * @param rsvNo
     * @return なし
     */
    public void getRsvData(String rsvNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = query + " SELECT 0 AS ci_seq ,0 AS ci_sub_seq ,0 AS ci_status ,hhb.name AS hotelName, hhb.pref_name AS prefName, hhb.address1 AS hotelAddr1, hhb.address2 AS hotelAddr2, ";
            query = query + " hhb.address3 AS hotelAddr3, hhb.tel1 AS hotelTel, hrp.plan_name, hrp.plan_pr, hrr.ci_time_from, hrr.co_time, ";
            query = query + " hrr.reserve_date, hrr.reserve_no,  CASE WHEN hrr.room_hold=0 THEN hrr.seq ELSE hrr.room_hold END AS seq, hrr.basic_charge_total, hrr.charge_total, hrr.status, ";
            query = query + " hrr.num_adult, hrr.num_child, hrr.num_man, hrr.num_woman, hrr.parking_count, hrr.highroof_count AS parking_high_roof_count, hrr.est_time_arrival, hrp.plan_id, hrp.plan_sub_id, 0 AS request_flag, ";
            query = query + " hrr.user_id, hrr.name_last, hrr.name_first, hrr.name_last_kana, hrr.name_first_kana, ";
            query = query + " hrr.zip_code, hrr.address1, hrr.address2, hrr.address3, hrr.tel1, hrr.reminder_flag, hrr.parking, ";
            query = query + " hrr.mail_addr, hrrb.parking as hotelParking, hrrb.cancel_policy, hrr.demands, hrr.id, hrr.remarks, 0 AS offer_kind, hrr.noshow_flag, hrr.co_kind  ";
            query = query + " ,hresult.ci_date, hresult.regist_time, hresult.seq as checkinSeq ";
            query = query + " ,hrr.payment,hrr.payment_status,hrr.consumer_demands,hrr.add_bonus_mile,hrr.used_mile ";
            query = query + " ,hrr.charge_total_all,hrr.reserve_date_to, hrr.ext_flag, hrr.reserve_no_main ";
            query = query + " FROM newRsvDB.hh_rsv_reserve hrr ";
            query = query + "   INNER JOIN hh_hotel_basic hhb ON ( hrr.id = hhb.id ) ";
            query = query + "   LEFT JOIN hh_rsv_result hresult ON ( hrr.id = hresult.id AND hrr.reserve_no = hresult.reserve_no AND hresult.use_kind IN(1,3))";
            query = query + "   INNER JOIN newRsvDB.hh_rsv_plan hrp ON ( hrr.id = hrp.id AND hrr.plan_id = hrp.plan_id AND hrr.plan_sub_id = hrp.plan_sub_id) ";
            query = query + "   INNER JOIN newRsvDB.hh_rsv_reserve_basic hrrb ON ( hrr.id = hrrb.id ) ";
            query = query + " WHERE hrr.id = ? AND hrr.user_id = ? AND hrr.reserve_no = ? ";
            query = query + " AND hrr.status = 1";

            query = query + " UNION SELECT atc.seq AS ci_seq ,atc.sub_seq AS ci_sub_seq ,atc.ci_status AS ci_status ,hhb.name AS hotelName, hhb.pref_name AS prefName, hhb.address1 AS hotelAddr1, hhb.address2 AS hotelAddr2, ";
            query = query + " hhb.address3 AS hotelAddr3, hhb.tel1 AS hotelTel, hrp.plan_name, hrp.plan_pr, hrr.ci_time_from, hrr.co_time, ";
            query = query + " hrr.reserve_date, hrr.reserve_no,CASE WHEN hrr.room_hold=0 THEN hrr.seq ELSE hrr.room_hold END AS seq, hrr.basic_charge_total, hrr.charge_total, hrr.status, ";
            query = query + " hrr.num_adult, hrr.num_child, hrr.num_man, hrr.num_woman, hrr.parking_count, hrr.highroof_count AS parking_high_roof_count, hrr.est_time_arrival, hrp.plan_id,hrp.plan_sub_id, 0 AS request_flag, ";
            query = query + " hrr.user_id, hrr.name_last, hrr.name_first, hrr.name_last_kana, hrr.name_first_kana, ";
            query = query + " hrr.zip_code, hrr.address1, hrr.address2, hrr.address3, hrr.tel1, hrr.reminder_flag, hrr.parking, ";
            query = query + " hrr.mail_addr, hrrb.parking as hotelParking, hrrb.cancel_policy, hrr.demands, hrr.id, hrr.remarks, 0 AS offer_kind, hrr.noshow_flag, hrr.co_kind  ";
            query = query + " ,hresult.ci_date, hresult.regist_time, hresult.seq as checkinSeq ";
            query = query + " ,hrr.payment,hrr.payment_status,hrr.consumer_demands,hrr.add_bonus_mile,hrr.used_mile ";
            query = query + " ,hrr.charge_total_all,hrr.reserve_date_to, hrr.ext_flag, hrr.reserve_no_main ";
            query = query + " FROM newRsvDB.hh_rsv_reserve hrr ";
            query = query + "   INNER JOIN hh_hotel_basic hhb ON ( hrr.id = hhb.id ) ";
            query = query + "   LEFT JOIN hh_rsv_result hresult ON ( hrr.id = hresult.id AND hrr.reserve_no = hresult.reserve_no AND hresult.use_kind IN(1,3))";
            query = query + "   INNER JOIN newRsvDB.hh_rsv_plan hrp ON ( hrr.id = hrp.id AND hrr.plan_id = hrp.plan_id AND hrr.plan_sub_id = hrp.plan_sub_id) ";
            query = query + "   INNER JOIN newRsvDB.hh_rsv_reserve_basic hrrb ON ( hrr.id = hrrb.id ) ";
            query = query + "   INNER JOIN hh_hotel_ci atc ON hrr.id = atc.id  AND hrr.reserve_no = atc.rsv_no";
            query = query + " WHERE hrr.id = ? AND hrr.user_id = ? AND hrr.reserve_no = ? ";
            query = query + " AND hrr.status = 2";

            query = query + " ORDER BY ci_seq DESC, ci_sub_seq DESC LIMIT 0,1";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setString( 2, frm.getUserId() );
            prestate.setString( 3, rsvNo );
            prestate.setInt( 4, frm.getSelHotelId() );
            prestate.setString( 5, frm.getUserId() );
            prestate.setString( 6, rsvNo );

            result = prestate.executeQuery();

            setData( result );

            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );

            query = " SELECT country FROM hh_user_basic_foreign WHERE user_id = ?";
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, frm.getUserId() );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    frm.setCountry( result.getString( "country" ) );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReserveSheetPC.getRsvData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * ホテルチェックインデータ取得
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     * @throws Exception
     */
    public boolean setData(ResultSet result) throws Exception
    {
        boolean ret;
        ret = false;

        String convDate = "";
        String weekName = "";
        String estTime = "";
        int ci = 0;
        int co = 0;
        int coKind = 0;
        String cnvMoney;
        String ciTime = "";
        String coTime = "";
        String checkInDate = "";
        String checkInTime = "";
        String checkInSeq = "";

        try
        {
            if ( result != null )
            {
                // 全件抽出
                if ( result.next() != false )
                {
                    if ( result.getInt( "ci_status" ) == 0 )
                    {
                        frm.setSelHotelId( frm.getSelHotelId() );
                        frm.setSelPlanId( result.getInt( "plan_id" ) );
                        frm.setSelPlanSubId( result.getInt( "plan_sub_id" ) );
                        frm.setRsvNo( result.getString( "reserve_no" ) );
                        frm.setHotelNm( ConvertCharacterSet.convDb2Form( result.getString( "hotelName" ) ) );
                        frm.setHotelAddr( ConvertCharacterSet.convDb2Form( result.getString( "prefName" ).toString() ) +
                                ConvertCharacterSet.convDb2Form( result.getString( "hotelAddr1" ).toString() )
                                + ConvertCharacterSet.convDb2Form( result.getString( "hotelAddr2" ).toString() )
                                + ConvertCharacterSet.convDb2Form( result.getString( "hotelAddr3" ).toString() ) );
                        frm.setHotelTel( result.getString( "hotelTel" ) );
                        frm.setPlanNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ).toString() ) ) );
                        frm.setPlanPr( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_pr" ).toString() ) ) );
                        convDate = String.valueOf( result.getInt( "reserve_date" ) );
                        weekName = DateEdit.getWeekName( result.getInt( "reserve_date" ) );
                        frm.setRsvDateView( convDate.substring( 0, 4 ) + "/" + convDate.substring( 4, 6 )
                                + "/" + convDate.substring( 6, 8 ) + "(" + weekName + ")" );
                        frm.setRsvDate( result.getInt( "reserve_date" ) );
                        frm.setSeq( result.getInt( "seq" ) );
                        NumberFormat objNum = NumberFormat.getCurrencyInstance();
                        cnvMoney = objNum.format( result.getInt( "basic_charge_total" ) );
                        frm.setBasicTotal( result.getInt( "basic_charge_total" ) );
                        frm.setBasicTotalView( cnvMoney );
                        if ( result.getInt( "num_adult" ) == -1 ) // newRsvDB は -1 がdefault値でセットされているので男性＋女性とする
                        {
                            frm.setAdultNumView( String.valueOf( result.getInt( "num_man" ) + result.getInt( "num_woman" ) ) + labelUsr );
                            frm.setAdultNum( result.getInt( "num_man" ) + result.getInt( "num_woman" ) );
                        }
                        else
                        {
                            frm.setAdultNumView( String.valueOf( result.getInt( "num_adult" ) ) + labelUsr );
                            frm.setAdultNum( result.getInt( "num_adult" ) );
                        }
                        frm.setChildNumView( String.valueOf( result.getInt( "num_child" ) ) + labelUsr );
                        frm.setChildNum( result.getInt( "num_child" ) );
                        frm.setManNum( result.getInt( "num_man" ) );
                        frm.setManNumView( String.valueOf( result.getInt( "num_man" ) ) + labelUsr );
                        frm.setWomanNum( result.getInt( "num_woman" ) );
                        frm.setWomanNumView( String.valueOf( result.getInt( "num_woman" ) ) + labelUsr );

                        cnvMoney = objNum.format( result.getInt( "charge_total" ) );
                        frm.setChargeTotalView( cnvMoney );
                        frm.setChargeTotal( result.getInt( "charge_total" ) );
                        frm.setCoKind( result.getInt( "co_kind" ) );
                        frm.setCoTime( result.getInt( "co_time" ) );
                        ci = result.getInt( "ci_time_from" );
                        co = result.getInt( "co_time" );
                        coKind = result.getInt( "co_kind" );
                        ciTime = ConvertTime.convTimeStr( ci, 3 );
                        if ( coKind == 1 )
                        {
                            coTime = ConvertTime.convTimeStr( co, 3 );
                        }
                        else
                        {
                            coTime = "チェックインから" + DateEdit.formatTime( 6, co );
                        }
                        frm.setCiTime( ciTime );
                        frm.setCoTimeView( coTime );
                        if ( result.getInt( "hotelParking" ) == ReserveCommon.PARKING_NO_INPUT )
                        {
                            frm.setParkingUsed( ReserveCommon.PARKING_NO_PARKNG );
                        }
                        else
                        {
                            if ( result.getInt( "parking" ) == ReserveCommon.PRKING_USED_USE )
                            {
                                // 利用する
                                if ( result.getInt( "parking_count" ) != 0 )
                                {
                                    // 台数あり
                                    frm.setParkingUsed( ReserveCommon.PARKING_USE + "：" + String.valueOf( result.getInt( "parking_count" ) ) + ReserveCommon.PARKING_CNT );
                                }
                                else
                                {
                                    // 台数無し
                                    frm.setParkingUsed( ReserveCommon.PARKING_USE );
                                }
                            }
                            else
                            {
                                // 利用しない
                                frm.setParkingUsed( ReserveCommon.PARKING_NOT_USE );
                            }
                        }
                        frm.setParkingCnt( result.getInt( "parking_count" ) );
                        frm.setHiRoofCnt( result.getInt( "parking_high_roof_count" ) );
                        estTime = ReserveCommon.getArrivalTimeView( result.getInt( "est_time_arrival" ) );
                        frm.setEstTimeArrivalView( estTime );
                        frm.setEstTimeArrival( result.getInt( "est_time_arrival" ) );
                        frm.setUserId( result.getString( "user_id" ) );
                        frm.setMail( "" );
                        frm.setName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_last" ) ) )
                                + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_first" ) ) )
                                + "（" + ConvertCharacterSet.convDb2Form( result.getString( "name_last_kana" ).toString() )
                                + ConvertCharacterSet.convDb2Form( result.getString( "name_first_kana" ).toString() ) + "）" );
                        frm.setLastNmKana( ConvertCharacterSet.convDb2Form( result.getString( "name_last_kana" ).toString() ) );
                        frm.setFirstNmKana( ConvertCharacterSet.convDb2Form( result.getString( "name_first_kana" ).toString() ) );

                        if ( result.getString( "zip_code" ).length() >= 7 )
                        {
                            frm.setZip( result.getString( "zip_code" ).substring( 0, 3 ) + " - " + result.getString( "zip_code" ).substring( 3 ) );
                        }
                        else
                        {
                            frm.setZip( result.getString( "zip_code" ) );
                        }
                        frm.setAddress( ConvertCharacterSet.convDb2Form( result.getString( "address1" ).toString() )
                                + ConvertCharacterSet.convDb2Form( result.getString( "address2" ).toString() )
                                + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "address3" ) ) ) );
                        frm.setTel( result.getString( "tel1" ) );
                        frm.setReminder( result.getInt( "reminder_flag" ) );
                        if ( result.getInt( "reminder_flag" ) == 1 )
                        {
                            frm.setReminderView( ReserveCommon.REMINDER_ON );
                        }
                        else
                        {
                            frm.setReminderView( ReserveCommon.REMINDER_OFF );
                        }
                        // #3182 20011-04-20
                        if ( result.getInt( "reminder_flag" ) == 1 && result.getString( "mail_addr" ).trim().length() > 0 )
                        {
                            frm.setRemainderMail( ReserveCommon.REMINDER_MAIL + result.getString( "mail_addr" ).trim() );
                        }
                        frm.setDemands( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "demands" ).toString() ) ) );
                        frm.setDemandsView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "demands" ).toString() ) ) );
                        frm.setCahcelPolicy( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "cancel_policy" ).toString() ) ) );
                        frm.setDispRequestFlag( result.getInt( "request_flag" ) );
                        frm.setOfferKind( result.getInt( "offer_kind" ) );
                        frm.setStatus( result.getInt( "status" ) );
                        if ( result.getString( "ci_date" ) != null )
                        {
                            convDate = result.getString( "ci_date" );
                            weekName = DateEdit.getWeekName( result.getInt( "ci_date" ) );
                            checkInDate = convDate.substring( 0, 4 ) + "/"
                                    + convDate.substring( 4, 6 ) + "/"
                                    + convDate.substring( 6, 8 ) + "(" + weekName + ")";
                            checkInSeq = result.getString( "checkinSeq" );
                        }
                        if ( result.getString( "regist_time" ) != null )
                        {
                            checkInTime = ConvertTime.convTimeStr( result.getInt( "regist_time" ), 3 );
                        }
                        frm.setCheckInDate( checkInDate );
                        frm.setCheckInSeq( checkInSeq );
                        frm.setCheckInTime( checkInTime );
                        frm.setNoShow( result.getInt( "noshow_flag" ) );
                        frm.setRemarks( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ).toString() ) ) );
                        frm.setRemarksView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ).toString() ) ) );
                        frm.setPayment( result.getInt( "payment" ) );
                        frm.setPaymentStatus( result.getInt( "payment_status" ) );
                        frm.setConsumerDemands( result.getString( "consumer_demands" ) );
                        frm.setAddBonusMile( result.getInt( "add_bonus_mile" ) );
                        frm.setUsedMile( result.getInt( "used_mile" ) );
                        frm.setChargeTotalAll( result.getInt( "charge_total_all" ) );
                        frm.setReserveDateTo( result.getInt( "reserve_date_to" ) );
                        frm.setExtFlag( result.getInt( "ext_flag" ) );
                        frm.setRsvNoMain( result.getString( "reserve_no_main" ) );
                    }
                }
            }
            ret = true;

        }
        catch ( Exception e )
        {
            Logging.error( "[[LogicReserveSheetPC.setData] Exception=" + e.toString() );
            throw e;
        }

        return(ret);
    }
}
