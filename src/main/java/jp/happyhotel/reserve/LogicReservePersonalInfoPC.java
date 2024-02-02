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
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;

/**
 * 
 * 個人情報入力画面（PC版） business logic
 */

public class LogicReservePersonalInfoPC implements Serializable
{
    private static final long         serialVersionUID = 4066584694901283027L;
    private FormReservePersonalInfoPC frm;
    private FormReserveSheetPC        frmSheet;

    /* フォームオブジェクト */
    public FormReservePersonalInfoPC getFrm()
    {
        return frm;
    }

    public void setFrm(FormReservePersonalInfoPC frm)
    {
        this.frm = frm;
    }

    public FormReserveSheetPC getFrmSheet()
    {
        return frmSheet;
    }

    public void setFrmSheet(FormReserveSheetPC frmSheet)
    {
        this.frmSheet = frmSheet;
    }

    /**
     * 予約データ仮テーブルに登録
     * 
     * @param なし
     */
    public void insReserveWork() throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        int workID = 0;
        int tempComingFlg = 0;

        // 予約データの仮来店フラグ取得
        tempComingFlg = getTempComingFlg();

        query = query + "INSERT INTO hh_rsv_reserve_work SET ";
        query = query + "  id = ? ";
        query = query + ", reserve_no = ? ";
        query = query + ", reserve_sub_no = ?  ";
        query = query + ", plan_id = ?  ";
        query = query + ", user_id = ?  ";
        query = query + ", reserve_date = ?  ";
        query = query + ", seq = ?  ";
        query = query + ", est_time_arrival = ?  ";
        query = query + ", num_adult = ?  ";
        query = query + ", num_child = ?  ";
        query = query + ", name_last = ?  ";
        query = query + ", name_first = ?  ";
        query = query + ", name_last_kana = ?  ";
        query = query + ", name_first_kana = ?  ";
        query = query + ", zip_code = ?  ";
        query = query + ", pref_code = ?  ";
        query = query + ", jis_code = ?  ";
        query = query + ", address1 = ?  ";
        query = query + ", address2 = ?  ";
        query = query + ", address3 = ?  ";
        query = query + ", tel1 = ?  ";
        query = query + ", tel2 = ?  ";
        query = query + ", reminder_flag = ?  ";
        query = query + ", mail_addr = ?  ";
        query = query + ", demands = ?  ";
        query = query + ", remarks = ?  ";
        query = query + ", accept_date = ?  ";
        query = query + ", accept_time = ?  ";
        query = query + ", status = ?  ";
        query = query + ", basic_charge_total = ?  ";
        query = query + ", option_charge_total = ?  ";
        query = query + ", charge_total = ?  ";
        query = query + ", add_point = ?  ";
        query = query + ", coming_flag = ?  ";
        query = query + ", hotel_name = ?  ";
        query = query + ", noshow_flag = ?  ";
        query = query + ", parking = ?  ";
        query = query + ", parking_count = ?  ";
        query = query + ", parking_high_roof_count = ?  ";
        query = query + ", ci_time_from = ?  ";
        query = query + ", ci_time_to = ?  ";
        query = query + ", co_time = ? ";
        query = query + ", temp_coming_flag = ? ";
        query = query + ", num_man = ? ";
        query = query + ", num_woman = ? ";
        query = query + ", co_kind = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setString( 2, frm.getReserveNo() );
            prestate.setInt( 3, 0 );
            prestate.setInt( 4, frm.getSelPlanID() );
            prestate.setString( 5, frm.getLoginUserId() );
            prestate.setInt( 6, frm.getSelRsvDate() );
            prestate.setInt( 7, frm.getSelSeq() );
            prestate.setInt( 8, frm.getSelEstTimeArrival() );
            prestate.setInt( 9, frm.getSelNumAdult() );
            prestate.setInt( 10, frm.getSelNumChild() );
            prestate.setString( 11, ConvertCharacterSet.convForm2Db( frm.getLastName() ) );
            prestate.setString( 12, ConvertCharacterSet.convForm2Db( frm.getFirstName() ) );
            prestate.setString( 13, frm.getLastNameKana() );
            prestate.setString( 14, frm.getFirstNameKana() );
            prestate.setString( 15, frm.getZipCd3() + frm.getZipCd4() );
            prestate.setInt( 16, frm.getSelPrefId() );
            prestate.setInt( 17, frm.getSelJisCd() );
            prestate.setString( 18, frm.getAddress1() );
            prestate.setString( 19, frm.getAddress2() );
            prestate.setString( 20, ConvertCharacterSet.convForm2Db( frm.getAddress3() ) );
            prestate.setString( 21, frm.getTel() );
            prestate.setString( 22, frm.getLoginTel() );
            prestate.setInt( 23, frm.getRemainder() );
            prestate.setString( 24, frm.getRemainderMailAddr() );
            prestate.setString( 25, ConvertCharacterSet.convForm2Db( frm.getDemands() ) );
            prestate.setString( 26, ConvertCharacterSet.convForm2Db( frm.getRemarks() ) );
            prestate.setInt( 27, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 28, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 29, 1 ); // 1:受付
            prestate.setInt( 30, frm.getBaseChargeTotal() );
            prestate.setInt( 31, frm.getOptionCharge() );
            prestate.setInt( 32, frm.getChargeTotal() );
            prestate.setInt( 33, frm.getHapyPoint() );
            prestate.setInt( 34, 0 );
            prestate.setString( 35, frm.getHotelName() );
            prestate.setInt( 36, 0 );
            prestate.setInt( 37, frm.getSelParkingUsedKbn() );
            prestate.setInt( 38, frm.getSelParkingCount() );
            prestate.setInt( 39, frm.getSelHiRoofCount() );
            prestate.setInt( 40, frm.getCiTimeFrom() );
            prestate.setInt( 41, frm.getCiTimeTo() );
            prestate.setInt( 42, frm.getCoTime() );
            prestate.setInt( 43, tempComingFlg );
            prestate.setInt( 44, frm.getSelNumMan() );
            prestate.setInt( 45, frm.getSelNumWoman() );
            prestate.setInt( 46, frm.getCoKind() );

            prestate.executeUpdate();

            // ワークID取得
            workID = getWorkRsvID( conn, prestate );

            frm.setWorkId( workID );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.insReserveWork] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * 予約データの仮来店フラグ取得
     * 
     * @param なし
     * @return int 仮来店フラグ
     */
    private int getTempComingFlg() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;

        try
        {
            query = query + "SELECT temp_coming_flag FROM hh_rsv_reserve ";
            query = query + " WHERE id = ? AND reserve_no = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setString( 2, frm.getReserveNo() );

            result = prestate.executeQuery();

            while( result.next() )
            {
                ret = result.getInt( "temp_coming_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getTempComingFlg] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 予約仮データを更新
     * 
     * @param なし
     */
    public void updReserveWork() throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;

        query = query + "UPDATE hh_rsv_reserve_work SET ";
        query = query + "  reserve_date = ? ";
        query = query + ", seq = ? ";
        query = query + ", est_time_arrival = ? ";
        query = query + ", num_adult = ? ";
        query = query + ", num_child = ? ";
        query = query + ", name_last = ? ";
        query = query + ", name_first = ? ";
        query = query + ", name_last_kana = ? ";
        query = query + ", name_first_kana = ? ";
        query = query + ", zip_code = ? ";
        query = query + ", pref_code = ? ";
        query = query + ", jis_code = ? ";
        query = query + ", address1 = ? ";
        query = query + ", address2 = ? ";
        query = query + ", address3 = ? ";
        query = query + ", tel1 = ? ";
        query = query + ", tel2 = ? ";
        query = query + ", reminder_flag = ? ";
        query = query + ", mail_addr = ? ";
        query = query + ", demands = ? ";
        query = query + ", accept_date = ? ";
        query = query + ", accept_time = ? ";
        query = query + ", basic_charge_total = ? ";
        query = query + ", option_charge_total = ? ";
        query = query + ", charge_total = ? ";
        query = query + ", add_point = ? ";
        query = query + ", noshow_flag = ? ";
        query = query + ", parking = ?  ";
        query = query + ", parking_count = ?  ";
        query = query + ", parking_high_roof_count = ?  ";
        query = query + ", ci_time_from = ?  ";
        query = query + ", ci_time_to = ?  ";
        query = query + ", co_time = ?  ";
        query = query + ", remarks = ? ";
        query = query + ", num_man = ? ";
        query = query + ", num_woman = ? ";
        query = query + ", co_kind = ? ";
        query = query + " WHERE work_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, frm.getSelRsvDate() );
            prestate.setInt( 2, frm.getSelSeq() );
            prestate.setInt( 3, frm.getSelEstTimeArrival() );
            prestate.setInt( 4, frm.getSelNumAdult() );
            prestate.setInt( 5, frm.getSelNumChild() );
            prestate.setString( 6, ConvertCharacterSet.convForm2Db( frm.getLastName() ) );
            prestate.setString( 7, ConvertCharacterSet.convForm2Db( frm.getFirstName() ) );
            prestate.setString( 8, frm.getLastNameKana() );
            prestate.setString( 9, frm.getFirstNameKana() );
            prestate.setString( 10, frm.getZipCd3() + frm.getZipCd4() );
            prestate.setInt( 11, frm.getSelPrefId() );
            prestate.setInt( 12, frm.getSelJisCd() );
            prestate.setString( 13, frm.getAddress1() );
            prestate.setString( 14, frm.getAddress2() );
            prestate.setString( 15, ConvertCharacterSet.convForm2Db( frm.getAddress3() ) );
            prestate.setString( 16, frm.getTel() );
            prestate.setString( 17, frm.getLoginTel() );
            prestate.setInt( 18, frm.getRemainder() );
            prestate.setString( 19, frm.getRemainderMailAddr() );
            prestate.setString( 20, ConvertCharacterSet.convForm2Db( frm.getDemands() ) );
            prestate.setInt( 21, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 22, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 23, frm.getBaseChargeTotal() );
            prestate.setInt( 24, frm.getOptionCharge() );
            prestate.setInt( 25, frm.getChargeTotal() );
            prestate.setInt( 26, frm.getHapyPoint() );
            prestate.setInt( 27, 0 );
            prestate.setInt( 28, frm.getSelParkingUsedKbn() );
            prestate.setInt( 29, frm.getSelParkingCount() );
            prestate.setInt( 30, frm.getSelHiRoofCount() );
            prestate.setInt( 31, frm.getCiTimeFrom() );
            prestate.setInt( 32, frm.getCiTimeTo() );
            prestate.setInt( 33, frm.getCoTime() );
            prestate.setString( 34, ConvertCharacterSet.convForm2Db( frm.getRemarks() ) );
            prestate.setInt( 35, frm.getSelNumMan() );
            prestate.setInt( 36, frm.getSelNumWoman() );
            prestate.setInt( 37, frm.getCoKind() );
            prestate.setInt( 38, frm.getWorkId() );

            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.updReserveWork] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * 登録したワークID取得
     * 
     * @param conn Connection
     * @param prestate PreparedStatement
     * @return true:正常、false:失敗
     */
    public int getWorkRsvID(Connection conn, PreparedStatement prestate) throws Exception
    {

        String query = "";
        ResultSet result = null;
        int workId = 0;

        query = query + "SELECT LAST_INSERT_ID() AS IDX ";

        try
        {
            prestate = conn.prepareStatement( query );
            result = prestate.executeQuery();
            while( result.next() )
            {
                workId = result.getInt( "IDX" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getWorkRsvID] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(workId);
    }

    /**
     * 予約確認後の予約仮データを取得
     * 
     * @param workId 仮ID
     * @return なし
     */
    public void getReserveWorkData(int workId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String rsvDate = "";
        String zip = "";
        String arrivalTimeStr = "";
        NumberFormat objNum = NumberFormat.getCurrencyInstance();

        try
        {
            query = query + "SELECT ";
            query = query + "wk.*, hb.tel1 as hotelTel, hb.pref_name as hotelPrefNm, hb.address1 as hotelAddress1, hb.address2 as hotelAddress2, hb.address3 as hotelAddress3, ";
            query = query + "rb.parking as hotelParking, pl.plan_name, pl.plan_pr, pl.remarks as planRemarks, rb.cancel_policy, pl.question, pl.question_flag, pl.request_flag ";
            query = query + "FROM hh_rsv_reserve_work wk";
            query = query + "   LEFT JOIN hh_hotel_basic hb ON wk.id = hb.id ";
            query = query + "   LEFT JOIN hh_rsv_plan pl ON wk.id = pl.id AND wk.plan_id = pl.plan_id ";
            query = query + "   LEFT JOIN hh_rsv_reserve_basic rb ON wk.id = rb.id ";
            query = query + " WHERE work_id = ? ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, workId );

            result = prestate.executeQuery();

            while( result.next() )
            {
                frmSheet.setRsvNo( result.getString( "reserve_no" ) );
                frmSheet.setSelHotelId( result.getInt( "id" ) );
                frmSheet.setSelPlanId( result.getInt( "plan_id" ) );
                frmSheet.setHotelNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "hotel_name" ) ) ) );
                frmSheet.setHotelAddr( result.getString( "hotelPrefNm" ) + result.getString( "hotelAddress1" ) + result.getString( "hotelAddress2" ) + result.getString( "hotelAddress3" ) );
                frmSheet.setHotelTel( result.getString( "hotelTel" ) );
                frmSheet.setPlanNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
                frmSheet.setPlanPr( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_pr" ) ) ) );
                if ( (result.getString( "reserve_date" ) != null) && (result.getString( "reserve_date" ).trim().length() != 0) )
                {
                    rsvDate = (result.getString( "reserve_date" ).substring( 0, 4 )) + "年"
                            + (result.getString( "reserve_date" ).substring( 4, 6 )) + "月"
                            + (result.getString( "reserve_date" ).substring( 6 )) + "日(" + DateEdit.getWeekName( result.getInt( "reserve_date" ) ) + ")";
                }
                frmSheet.setRsvDateView( rsvDate );
                frmSheet.setRsvDate( result.getInt( "reserve_date" ) );
                frmSheet.setSeq( result.getInt( "seq" ) );
                frmSheet.setBasicTotalView( objNum.format( result.getInt( "basic_charge_total" ) ) );
                frmSheet.setBasicTotal( result.getInt( "basic_charge_total" ) );
                frmSheet.setAdultNum( result.getInt( "num_adult" ) );
                frmSheet.setChildNum( result.getInt( "num_child" ) );
                frmSheet.setManNum( result.getInt( "num_man" ) );
                frmSheet.setWomanNum( result.getInt( "num_woman" ) );
                frmSheet.setOptionChargeTotal( result.getInt( "option_charge_total" ) );
                frmSheet.setChargeTotalView( objNum.format( result.getInt( "charge_total" ) ) );
                frmSheet.setChargeTotal( result.getInt( "charge_total" ) );
                frmSheet.setCiTime( ConvertTime.convTimeStr( result.getInt( "ci_time_from" ), 3 ) );
                frmSheet.setCiTimeToView( ConvertTime.convTimeStr( result.getInt( "ci_time_to" ), 3 ) );
                if ( result.getInt( "co_kind" ) == 1 )
                {
                    frmSheet.setCoTimeView( "チェックインから" + DateEdit.formatTime( 6, result.getInt( "co_time" ) ) );
                }
                else
                {
                    frmSheet.setCoTimeView( ConvertTime.convTimeStr( result.getInt( "co_time" ), 3 ) );
                }
                frmSheet.setCoTime( result.getInt( "co_time" ) );
                if ( result.getInt( "hotelParking" ) == ReserveCommon.PARKING_NO_INPUT )
                {
                    // 駐車場なし
                    frmSheet.setParkingUsed( ReserveCommon.PARKING_NO_PARKNG );
                }
                else
                {
                    // 利用区分
                    if ( result.getInt( "parking" ) == ReserveCommon.PRKING_USED_USE )
                    {
                        // 利用する
                        if ( result.getInt( "hotelParking" ) == ReserveCommon.PARKING_INPUT_COUNT )
                        {
                            // 台数あり
                            frmSheet.setParkingUsed( ReserveCommon.PARKING_USE + " " + result.getString( "parking_count" ) + ReserveCommon.PARKING_CNT );
                        }
                        else
                        {
                            // 台数なし
                            frmSheet.setParkingUsed( ReserveCommon.PARKING_USE );
                        }
                    }
                    else
                    {
                        // 利用しない
                        frmSheet.setParkingUsed( ReserveCommon.PARKING_NOT_USE );
                    }
                }
                frmSheet.setParking( result.getInt( "parking" ) );
                frmSheet.setParkingCnt( result.getInt( "parking_count" ) );
                frmSheet.setHiRoofCnt( result.getInt( "parking_high_roof_count" ) );
                arrivalTimeStr = ReserveCommon.getArrivalTimeView( result.getInt( "est_time_arrival" ) );
                frmSheet.setEstTimeArrivalView( arrivalTimeStr );
                frmSheet.setEstTimeArrival( result.getInt( "est_time_arrival" ) );
                frmSheet.setUserId( result.getString( "user_id" ) );
                frmSheet.setName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_last" ) ) ) + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_first" ) ) ) +
                        "(" + (result.getString( "name_last_kana" )) + (result.getString( "name_first_kana" )) + ")" );
                if ( (result.getString( "zip_code" ) != null) && (result.getString( "zip_code" ).trim().length() != 0) )
                {
                    zip = (result.getString( "zip_code" ).substring( 0, 3 )) + "-" + (result.getString( "zip_code" ).substring( 3 ));
                }
                frmSheet.setLastName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_last" ) ) ) );
                frmSheet.setFirstName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_first" ) ) ) );
                frmSheet.setLastNmKana( (result.getString( "name_last_kana" )) );
                frmSheet.setFirstNmKana( (result.getString( "name_first_kana" )) );
                frmSheet.setZip( zip );
                frmSheet.setJisCd( result.getInt( "jis_code" ) );
                frmSheet.setAddress( (result.getString( "address1" )) + (result.getString( "address2" )) + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "address3" ) ) ) );
                frmSheet.setAddress1( (result.getString( "address1" )) );
                frmSheet.setAddress2( (result.getString( "address2" )) );
                frmSheet.setAddress3( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "address3" ) ) ) );
                frmSheet.setTel( result.getString( "tel1" ) );
                frmSheet.setReminder( result.getInt( "reminder_flag" ) );
                if ( result.getInt( "reminder_flag" ) == 0 )
                {
                    frmSheet.setReminderView( ReserveCommon.REMINDER_OFF );
                }
                else
                {
                    frmSheet.setReminderView( ReserveCommon.REMINDER_ON );
                }
                if ( result.getString( "mail_addr" ) != null )
                {
                    frmSheet.setRemainderMail( result.getString( "mail_addr" ) );
                }
                // nullだったらセットしない
                else
                {
                    frmSheet.setRemainderMail( "" );
                }
                frmSheet.setDemands( ConvertCharacterSet.convDb2Form( result.getString( "demands" ) ) );
                frmSheet.setDemandsView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "demands" ) ) ) );
                frmSheet.setCahcelPolicy( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "cancel_policy" ) ) ) );
                frmSheet.setPrefId( result.getInt( "pref_code" ) );
                frmSheet.setRemarksView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ) ) ) );
                frmSheet.setRemarks( ConvertCharacterSet.convDb2Form( result.getString( "remarks" ) ) );
                frmSheet.setDispRemarks( ConvertCharacterSet.convDb2Form( result.getString( "planRemarks" ) ) );
                frmSheet.setDispRequestFlag( result.getInt( "request_flag" ) );
                frmSheet.setQuestionFlg( result.getInt( "question_flag" ) );
                frmSheet.setQuestion( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "question" ) ) ) );
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getReserveWorkData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 予約・オプション仮テーブルデータ取得
     * 
     * @para frmSheet FormReserveSheetPCオブジェクト
     * @param workParentId 親のworkID
     * @param optionFlag オプションフラグ
     * @return FormReserveSheetPCオブジェクト
     */
    public FormReserveSheetPC getRsvWorkOptData(FormReserveSheetPC frmSheet, int workParentId, int optionFlag) throws Exception
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
        NumberFormat objNum = NumberFormat.getCurrencyInstance();

        try
        {
            query = query + "SELECT ";
            query = query + " opt.option_name, opt.option_sub_name, wk.quantity, wk.unit_price, wk.charge_total, wk.remarks ";
            query = query + "FROM hh_rsv_rel_reserve_option_work wk ";
            query = query + "   LEFT JOIN hh_rsv_option opt ON wk.id = opt.id AND wk.option_id = opt.option_id AND wk.option_sub_id = opt.option_sub_id ";
            query = query + " WHERE wk.id = ? ";
            query = query + "   AND wk.work_parent_id = ? ";
            query = query + "   AND opt.option_flag = ? ";
            query = query + "ORDER BY wk.option_id, wk.option_sub_id";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frmSheet.getSelHotelId() );
            prestate.setInt( 2, workParentId );
            prestate.setInt( 3, optionFlag );

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
            }

            if ( optionFlag == ReserveCommon.OPTION_IMP )
            {
                frmSheet.setOptNmImpList( optNmList );
                frmSheet.setOptSubNmImpList( optSubNmList );
            }
            else
            {
                frmSheet.setOptNmList( optNmList );
                frmSheet.setOptSubNmList( optSubNmList );
                frmSheet.setOptInpMaxQuantityList( quantityList );
                frmSheet.setOptUnitPriceList( unitPriceList );
                frmSheet.setOptChargeTotalList( chargeTotalList );
                frmSheet.setOptRemarksList( remarksList );
                frmSheet.setOptUnitPriceViewList( unitPriceViewList );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getRsvWorkOptData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frmSheet);
    }

    /**
     * 戻るボタン押下時の予約仮データを取得
     * 
     * @param workId 仮ID
     * @return なし
     */
    public void getReserveWorkData_Back(int workId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String rsvDate = "";

        try
        {
            query = query + "SELECT * FROM hh_rsv_reserve_work ";
            query = query + " WHERE work_id = ? ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, workId );

            result = prestate.executeQuery();

            while( result.next() )
            {
                frm.setReserveNo( result.getString( "reserve_no" ) );
                frm.setSelHotelID( result.getInt( "id" ) );
                frm.setSelPlanID( result.getInt( "plan_id" ) );
                frm.setSelRsvDate( result.getInt( "reserve_date" ) );
                frm.setOrgReserveDate( result.getInt( "reserve_date" ) );
                frm.setSelSeq( result.getInt( "seq" ) );
                rsvDate = (result.getString( "reserve_date" ).substring( 0, 4 )) + "年"
                        + (result.getString( "reserve_date" ).substring( 4, 6 )) + "月"
                        + (result.getString( "reserve_date" ).substring( 6 )) + "日(" + DateEdit.getWeekName( result.getInt( "reserve_date" ) ) + ")";
                frm.setReserveDateFormat( rsvDate );
                frm.setSelNumAdult( result.getInt( "num_adult" ) );
                frm.setSelNumChild( result.getInt( "num_child" ) );
                frm.setSelNumMan( result.getInt( "num_man" ) );
                frm.setSelNumWoman( result.getInt( "num_woman" ) );
                frm.setSelParkingUsedKbn( result.getInt( "parking" ) );
                frm.setParkingUsedKbnInit( result.getInt( "parking" ) );
                frm.setSelParkingCount( result.getInt( "parking_count" ) );
                frm.setSelHiRoofCount( result.getInt( "parking_high_roof_count" ) );
                frm.setSelEstTimeArrival( result.getInt( "est_time_arrival" ) );
                frm.setLastName( ConvertCharacterSet.convDb2Form( result.getString( "name_last" ) ) );
                frm.setFirstName( ConvertCharacterSet.convDb2Form( result.getString( "name_first" ) ) );
                frm.setLastNameKana( result.getString( "name_last_kana" ) );
                frm.setFirstNameKana( result.getString( "name_first_kana" ) );
                frm.setZipCd3( result.getString( "zip_code" ).substring( 0, 3 ) );
                frm.setZipCd4( result.getString( "zip_code" ).substring( 3 ) );
                frm.setSelPrefId( result.getInt( "pref_code" ) );
                frm.setSelJisCd( result.getInt( "jis_code" ) );
                frm.setAddress3( ConvertCharacterSet.convDb2Form( result.getString( "address3" ) ) );
                frm.setTel( result.getString( "tel1" ) );
                frm.setRemainder( result.getInt( "reminder_flag" ) );
                frm.setRemainderMailAddr( result.getString( "mail_addr" ) );
                frm.setDemands( ConvertCharacterSet.convDb2Form( result.getString( "demands" ) ) );
                frm.setRemarks( ConvertCharacterSet.convDb2Form( result.getString( "remarks" ) ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getReserveWorkData_Back] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 登録済み予約データ取得
     * 
     * @param なし
     * @return なし
     */
    public void getReserveData() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String rsvDate = "";

        try
        {
            query = query + "SELECT * FROM hh_rsv_reserve ";
            query = query + " WHERE id = ? AND reserve_no = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setString( 2, frm.getReserveNo() );

            result = prestate.executeQuery();

            while( result.next() )
            {
                frm.setReserveNo( result.getString( "reserve_no" ) );
                frm.setSelHotelID( result.getInt( "id" ) );
                frm.setSelPlanID( result.getInt( "plan_id" ) );
                frm.setSelRsvDate( result.getInt( "reserve_date" ) );
                frm.setOrgReserveDate( result.getInt( "reserve_date" ) );
                frm.setSelSeq( result.getInt( "seq" ) );
                frm.setSeq( result.getInt( "seq" ) );
                rsvDate = (result.getString( "reserve_date" ).substring( 0, 4 )) + "年"
                        + (result.getString( "reserve_date" ).substring( 4, 6 )) + "月"
                        + (result.getString( "reserve_date" ).substring( 6 )) + "日(" + DateEdit.getWeekName( result.getInt( "reserve_date" ) ) + ")";
                frm.setReserveDateFormat( rsvDate );
                frm.setSelNumAdult( result.getInt( "num_adult" ) );
                frm.setSelNumChild( result.getInt( "num_child" ) );
                frm.setSelNumMan( result.getInt( "num_man" ) );
                frm.setSelNumWoman( result.getInt( "num_woman" ) );
                frm.setSelParkingUsedKbn( result.getInt( "parking" ) );
                frm.setParkingUsedKbnInit( result.getInt( "parking" ) );
                frm.setSelParkingCount( result.getInt( "parking_count" ) );
                frm.setSelHiRoofCount( result.getInt( "parking_high_roof_count" ) );
                frm.setSelEstTimeArrival( result.getInt( "est_time_arrival" ) );
                frm.setLastName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_last" ) ) ) );
                frm.setFirstName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "name_first" ) ) ) );
                frm.setLastNameKana( result.getString( "name_last_kana" ) );
                frm.setFirstNameKana( result.getString( "name_first_kana" ) );
                frm.setZipCd3( result.getString( "zip_code" ).substring( 0, 3 ) );
                frm.setZipCd4( result.getString( "zip_code" ).substring( 3 ) );
                frm.setSelPrefId( result.getInt( "pref_code" ) );
                frm.setSelJisCd( result.getInt( "jis_code" ) );
                frm.setAddress3( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "address3" ) ) ) );
                frm.setTel( result.getString( "tel1" ) );
                frm.setRemainder( result.getInt( "reminder_flag" ) );
                frm.setRemainderMailAddr( result.getString( "mail_addr" ) );
                frm.setDemands( ConvertCharacterSet.convDb2Form( result.getString( "demands" ) ) );
                frm.setStatus( result.getInt( "status" ) );
                frm.setRemarks( ConvertCharacterSet.convDb2Form( result.getString( "remarks" ) ) );
                frm.setLoginUserId( result.getString( "user_id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getReserveWorkData_Back] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 予約仮データ存在チェック
     * 
     * @param workId 仮ID
     * @return true:存在する、false:存在しない
     */
    public boolean isExistsRsvWork(int workId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;
        boolean ret = false;

        try
        {
            query = "SELECT COUNT(*) AS CNT FROM hh_rsv_reserve_work WHERE work_id = ? ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, workId );

            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.isExistsRsvWork] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * 予約仮データ削除
     * 
     * @param workId ワークID
     * @return なし
     */
    public void deleteRsvWork(int workId) throws Exception
    {
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;

        query = query + "DELETE FROM hh_rsv_reserve_work ";
        query = query + "WHERE work_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, workId );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.deleteRsvWork] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

    }

    /**
     * 予約・オプション仮データ削除
     * 
     * @param workParentId 親のワークID
     * @return なし
     */
    public void deleteRsvOptionWork(int workParentId) throws Exception
    {
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;

        query = query + "DELETE FROM hh_rsv_rel_reserve_option_work ";
        query = query + "WHERE work_parent_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, workParentId );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.deleteRsvOptionWork] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * プランに登録されているオプション情報取得
     * 
     * @param hotelId ホテルID
     * @param planId プランID
     * @param optFlg オプションフラグ
     * @return オプションIDのリスト
     */
    public ArrayList<Integer> getPlanOption(int hotelId, int planId, int optFlg) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> optionIdList = new ArrayList<Integer>();

        try
        {
            query = query + "SELECT DISTINCT rel.option_id ";
            query = query + "FROM hh_rsv_rel_plan_option rel ";
            query = query + "  INNER JOIN hh_rsv_option opt ON rel.id = opt.id AND rel.option_id = opt.option_id ";
            query = query + " WHERE rel.id = ? ";
            query = query + "   AND rel.plan_id = ? ";
            query = query + "   AND opt.option_flag = ? ";
            query = query + "ORDER BY opt.disp_index ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, optFlg );

            result = prestate.executeQuery();

            while( result.next() )
            {
                optionIdList.add( result.getInt( "option_id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getPlanOption] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(optionIdList);
    }

    /**
     * 予約・オプション仮テーブルにデータを登録
     * 
     * @param optionId オプションID
     * @param optSubId オプションサブID
     * @param quantity 数量
     * @param unitPrice 単価
     * @param chargeTotal 総合計
     * @param remarks 備考
     * @param quantityFlg 数量のフラグ
     * @return workId
     */
    public int insRsvOptionWork(int optionId, int optSubId, int quantity, int unitPrice, int chargeTotal, String remarks, int quantityFlg) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;
        int workID = 0;

        query = query + "INSERT INTO hh_rsv_rel_reserve_option_work SET ";
        query = query + "  work_parent_id = ? ";
        query = query + ", id = ? ";
        query = query + ", reserve_no = ?  ";
        query = query + ", reserve_sub_no = ?  ";
        query = query + ", option_id = ?  ";
        query = query + ", option_sub_id = ?  ";
        query = query + ", quantity = ?  ";
        query = query + ", unit_price = ?  ";
        query = query + ", charge_total = ?  ";
        query = query + ", remarks = ?  ";
        query = query + ", quantity_Flag = ?  ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, frm.getWorkId() );
            prestate.setInt( 2, frm.getSelHotelID() );
            prestate.setString( 3, frm.getReserveNo() );
            prestate.setInt( 4, 0 );
            prestate.setInt( 5, optionId );
            prestate.setInt( 6, optSubId );
            prestate.setInt( 7, quantity );
            prestate.setInt( 8, unitPrice );
            prestate.setInt( 9, chargeTotal );
            prestate.setString( 10, remarks );
            prestate.setInt( 11, quantityFlg );

            prestate.executeUpdate();

            // ワークID取得
            workID = getWorkRsvID( conn, prestate );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.insRsvOptionWork] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }

        return(workID);
    }

    /**
     * 必須オプション用 予約・オプション仮テーブルデータ取得
     * 
     * @param workId ワークID
     * @return 選択済みオプションサブIDリストの取得
     */
    public ArrayList<Integer> getSelOptImpSubIdList(int workId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> optionSubIdList = new ArrayList<Integer>();

        try
        {
            query = query + "SELECT rel.option_sub_id ";
            query = query + "FROM hh_rsv_rel_reserve_option_work rel ";
            query = query + "  INNER  JOIN hh_rsv_option opt ON rel.id = opt.id AND rel.option_id = opt.option_id AND rel.option_sub_id = opt.option_sub_id ";
            query = query + "WHERE rel.work_parent_id = ? ";
            query = query + "  AND opt.option_flag = 1 ";
            query = query + "ORDER BY rel.option_id, rel.option_sub_id ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, workId );

            result = prestate.executeQuery();

            while( result.next() )
            {
                optionSubIdList.add( result.getInt( "option_sub_id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getSelOptImpSubIdList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(optionSubIdList);
    }

    /**
     * 通常オプション用 予約・オプション仮テーブルデータ取得
     * 
     * @param workId ワークID
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    public FormReservePersonalInfoPC getSelOptSubList(int workId, FormReservePersonalInfoPC frm) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> optionIdList = new ArrayList<Integer>();
        ArrayList<Integer> quantityList = new ArrayList<Integer>();
        ArrayList<Integer> unitPriceList = new ArrayList<Integer>();
        ArrayList<String> remarksList = new ArrayList<String>();
        ArrayList<Integer> quantityFlgList = new ArrayList<Integer>();

        try
        {
            query = query + "SELECT rel.option_id, rel.quantity, rel.unit_price, rel.remarks, rel.quantity_flag ";
            query = query + "FROM hh_rsv_rel_reserve_option_work rel ";
            query = query + "  INNER  JOIN hh_rsv_option opt ON rel.id = opt.id AND rel.option_id = opt.option_id AND rel.option_sub_id = opt.option_sub_id ";
            query = query + "WHERE rel.work_parent_id = ? ";
            query = query + "  AND opt.option_flag = 0 ";
            query = query + "ORDER BY opt.disp_index ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, workId );

            result = prestate.executeQuery();

            while( result.next() )
            {
                optionIdList.add( result.getInt( "option_id" ) );
                quantityList.add( result.getInt( "quantity" ) );
                unitPriceList.add( result.getInt( "unit_price" ) );
                remarksList.add( result.getString( "remarks" ) );
                quantityFlgList.add( result.getInt( "quantity_flag" ) );
            }
            frm.setSelOptSubIdList( optionIdList );
            frm.setSelOptSubNumList( quantityList );
            frm.setSelOptSubUnitPriceList( unitPriceList );
            frm.setSelOptSubRemarksList( remarksList );
            frm.setSelQuantityFlgList( quantityFlgList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getSelOptSubList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frm);
    }

    /**
     * 予約・オプションデータ取得
     * 
     * @param hotelId ホテルID
     * @param rsvNo 予約番号
     * @return 選択済みオプションサブIDリストの取得
     */
    public ArrayList<Integer> getRsvSelOptImpSubIdList(int hotelId, String rsvNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> optionSubIdList = new ArrayList<Integer>();

        try
        {
            query = query + "SELECT rel.option_sub_id ";
            query = query + "FROM hh_rsv_rel_reserve_option rel ";
            query = query + "   LEFT JOIN hh_rsv_option opt ON rel.id = opt.id AND rel.option_id = opt.option_id AND rel.option_sub_id = opt.option_sub_id ";
            query = query + " WHERE rel.id = ? ";
            query = query + "   AND rel.reserve_no = ? ";
            query = query + "   AND opt.option_flag = 1 ";
            query = query + "ORDER BY rel.option_id, rel.option_sub_id ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, rsvNo );

            result = prestate.executeQuery();

            while( result.next() )
            {
                optionSubIdList.add( result.getInt( "option_sub_id" ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getRsvSelOptImpSubIdList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(optionSubIdList);
    }

    /**
     * 通常オプション用 予約・オプションデータ取得
     * 
     * @param workId ワークID
     * @param frm FormReservePersonalInfoPCオブジェクト
     * @return FormReservePersonalInfoPCオブジェクト
     */
    public FormReservePersonalInfoPC getRsvSelOptSubIdList(int hotelId, String rsvNo, FormReservePersonalInfoPC frm) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> optionIdList = new ArrayList<Integer>();
        ArrayList<Integer> optionSubIdList = new ArrayList<Integer>();
        ArrayList<Integer> quantityList = new ArrayList<Integer>();
        ArrayList<Integer> unitPriceList = new ArrayList<Integer>();
        ArrayList<String> remarksList = new ArrayList<String>();

        try
        {
            query = query + "SELECT rel.option_id, rel.option_sub_id, rel.quantity, rel.unit_price, rel.remarks ";
            query = query + "FROM hh_rsv_rel_reserve_option rel ";
            query = query + "  INNER  JOIN hh_rsv_option opt ON rel.id = opt.id AND rel.option_id = opt.option_id AND rel.option_sub_id = opt.option_sub_id ";
            query = query + "WHERE rel.id = ? ";
            query = query + "  AND rel.reserve_no = ? ";
            query = query + "  AND opt.option_flag = 0 ";
            query = query + "ORDER BY rel.option_id, rel.option_sub_id ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, rsvNo );

            result = prestate.executeQuery();

            while( result.next() )
            {
                optionIdList.add( result.getInt( "option_id" ) );
                optionSubIdList.add( result.getInt( "option_sub_id" ) );
                quantityList.add( result.getInt( "quantity" ) );
                unitPriceList.add( result.getInt( "unit_price" ) );
                remarksList.add( result.getString( "remarks" ) );
            }

            frm.setSelOptSubIdList( optionIdList );
            frm.setSelOptSubNumList( quantityList );
            frm.setSelOptSubUnitPriceList( unitPriceList );
            frm.setSelOptSubRemarksList( remarksList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getRsvSelOptSubIdList] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(frm);
    }

    /**
     * 予約時のオプション数量取得
     * 
     * @param hotelId ホテルID
     * @param optionId オプションID
     * @param rsvNo 予約番号
     * @return オプション須量
     */
    public int getRsvOptionQuantity(int hotelId, int optionId, String rsvNo) throws Exception
    {
        String query = "";
        int quantity = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = query + "SELECT quantity FROM hh_rsv_rel_reserve_option ";
            query = query + "WHERE id = ? ";
            query = query + "  AND option_id = ? ";
            query = query + "  AND reserve_no = ? ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, optionId );
            prestate.setString( 3, rsvNo );

            result = prestate.executeQuery();

            while( result.next() )
            {
                quantity = result.getInt( "quantity" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getRsvOptionQuantity] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(quantity);
    }

    /**
     * 通常オプション合計金額取得
     * 
     * @param int 親のworkID
     * @return int 通常オプション合計金額
     */
    public int getOptionChargeTotal(int workParentId) throws Exception
    {
        String query = "";
        int chargeTotal = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = query + "SELECT SUM(wk.charge_total) as optionChargeTotal ";
            query = query + "FROM hh_rsv_rel_reserve_option_work wk ";
            query = query + "   LEFT JOIN hh_rsv_option opt ON wk.id = opt.id AND wk.option_id = opt.option_id AND wk.option_sub_id = opt.option_sub_id ";
            query = query + " WHERE wk.work_parent_id = ? ";
            query = query + "   AND opt.option_flag = 0 ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, workParentId );

            result = prestate.executeQuery();

            while( result.next() )
            {
                chargeTotal = result.getInt( "optionChargeTotal" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getOptionChargeTotal] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(chargeTotal);
    }

    /**
     * 基本料金取得
     * 
     * @param int 親のworkID
     * @return int 通常オプション合計金額
     */
    public int getBasicChargeTotal(int workId) throws Exception
    {
        String query = "";
        int basicChargeTotal = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = query + "SELECT basic_charge_total FROM hh_rsv_reserve_work ";
            query = query + " WHERE work_id = ? ";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, workId );

            result = prestate.executeQuery();

            while( result.next() )
            {
                basicChargeTotal = result.getInt( "basic_charge_total" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getBasicChargeTotal] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(basicChargeTotal);
    }

    /**
     * 予約仮データのオプション合計、総合計を更新
     * 
     * @param workId ワークID
     * @param optChargeTotal オプション金額合計
     * @param chargeTotal 総合計
     * @return なし
     */
    public void updRsvWorkChargeTotal(int workId, int optChargeTotal, int chargeTotal) throws Exception
    {
        String query = "";
        Connection conn = null;
        PreparedStatement prestate = null;

        query = query + "UPDATE hh_rsv_reserve_work SET ";
        query = query + "  option_charge_total = ? ";
        query = query + ", charge_total = ? ";
        query = query + " WHERE work_id = ? ";

        try
        {
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, optChargeTotal );
            prestate.setInt( 2, chargeTotal );
            prestate.setInt( 3, workId );

            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.updRsvWorkChargeTotal] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( conn );
        }
    }

    /**
     * 予約の加算ポイント取得
     * 
     * @param int hotelId ホテルID
     * @param String rsvNo 予約番号
     * @return int 加算ポイント
     */
    public int getAddPoint(int hotelId, String rsvNo) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ret = 0;

        try
        {
            query = query + "SELECT add_point FROM hh_rsv_reserve ";
            query = query + " WHERE id = ? AND reserve_no = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setString( 2, rsvNo );

            result = prestate.executeQuery();

            while( result.next() )
            {
                ret = result.getInt( "add_point" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicReservePersonalInfoPC.getAddPoint] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }
}
