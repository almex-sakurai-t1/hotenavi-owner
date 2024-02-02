package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
 * プラン設定画面ビジネスロジッククラス
 */
public class LogicOwnerRsvPlan implements Serializable
{
    private static final long                    serialVersionUID = 5881012125830538206L;
    private static final String                  BTN_STOP         = "一時的に停止する";
    private static final String                  BTN_ON           = "販売中にする";

    // 登録モード
    private static final int                     MODE_NEW         = 2;                   // 新規
    private static final int                     MODE_EDIT        = 4;                   // 更新

    // 下書き区分
    private static final int                     DRAFT_OFF        = 0;                   // 通常

    private FormOwnerRsvPlan                     frm;
    private FormOwnerRsvPlanSub                  frmSub;
    private FormOwnerRsvPlanOptionSub            frmMustOptionSub;
    private FormOwnerRsvPlanOptionSub            frmCommOptionSub;
    private ArrayList<FormOwnerRsvPlanSub>       subFormList;
    private ArrayList<FormOwnerRsvPlanOptionSub> subMustOptionFormList;
    private ArrayList<FormOwnerRsvPlanOptionSub> subCommOptionFormList;

    /* フォームオブジェクト */
    public FormOwnerRsvPlan getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvPlan frm)
    {
        this.frm = frm;
    }

    public FormOwnerRsvPlanSub getFrmSub()
    {
        return frmSub;
    }

    public void setFrmSub(FormOwnerRsvPlanSub frmSub)
    {
        this.frmSub = frmSub;
    }

    public FormOwnerRsvPlanOptionSub getFrmMustOptionSub()
    {
        return frmMustOptionSub;
    }

    public void setFrmMustOptionSub(FormOwnerRsvPlanOptionSub frmMustOptionSub)
    {
        this.frmMustOptionSub = frmMustOptionSub;
    }

    public FormOwnerRsvPlanOptionSub getFrmCommOptionSub()
    {
        return frmCommOptionSub;
    }

    public void setFrmCommOptionSub(FormOwnerRsvPlanOptionSub frmCommOptionSub)
    {
        this.frmCommOptionSub = frmCommOptionSub;
    }

    public ArrayList<FormOwnerRsvPlanSub> getSubFormList()
    {
        return subFormList;
    }

    public void setSubFormList(ArrayList<FormOwnerRsvPlanSub> subFormList)
    {
        this.subFormList = subFormList;
    }

    public ArrayList<FormOwnerRsvPlanOptionSub> getSubMustOptionFormList()
    {
        return subMustOptionFormList;
    }

    public void setSubMustOptionFormList(ArrayList<FormOwnerRsvPlanOptionSub> subMustOptionFormList)
    {
        this.subMustOptionFormList = subMustOptionFormList;
    }

    public ArrayList<FormOwnerRsvPlanOptionSub> getSubCommOptionFormList()
    {
        return subCommOptionFormList;
    }

    public void setSubCommOptionFormList(ArrayList<FormOwnerRsvPlanOptionSub> subCommOptionFormList)
    {
        this.subCommOptionFormList = subCommOptionFormList;
    }

    /*
     * コンストラクタ
     */
    public LogicOwnerRsvPlan()
    {
        subFormList = new ArrayList<FormOwnerRsvPlanSub>();
        subMustOptionFormList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        subCommOptionFormList = new ArrayList<FormOwnerRsvPlanOptionSub>();
    }

    /**
     * プラン・部屋設定新規登録時のデータ取得
     * 
     * @param なし
     * @return なし
     */
    public void getNewPlanRoom() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = query + "SELECT seq, room_name ";
        query = query + "FROM hh_hotel_room_more ";
        query = query + "WHERE id = ? ";
        query = query + "  AND (disp_flag = 0 or disp_flag = 1) ";
        query = query + "ORDER BY seq";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                frmSub = new FormOwnerRsvPlanSub();
                frmSub.setSeq( result.getInt( "seq" ) );
                frmSub.setRoomNm( result.getString( "room_name" ) );
                frmSub.setCheck( 0 );
                frmSub.setTekiyoDateFrom( "" );
                frmSub.setTekiyoDateTo( "" );
                subFormList.add( frmSub );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getNewPlanRoom] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 新規必須オプション情報取得
     * 
     * @param optionFlg オプションフラグ
     * @return なし
     */
    public void getNewOption(int optionFlg) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        int orgOptId = 0;
        int newOptId = 0;
        String subOptNm = "";
        String newOptNm = "";
        FormOwnerRsvPlanOptionSub frmMustSub;
        ArrayList<FormOwnerRsvPlanOptionSub> mustOptSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();

        query = query + "SELECT ";
        query = query + "option_id, option_sub_id, option_name, option_sub_name ";
        query = query + "FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_flag = ? ";
        query = query + "ORDER BY option_id, option_sub_id";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, optionFlg );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( optionFlg == 0 )
                {
                    // 通常オプション
                    frmCommOptionSub = new FormOwnerRsvPlanOptionSub();
                    frmCommOptionSub.setCheck( 0 );
                    frmCommOptionSub.setOptionID( result.getInt( "option_id" ) );
                    frmCommOptionSub.setOptionNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                    frmCommOptionSub.setOptionFlg( 0 );
                    subCommOptionFormList.add( frmCommOptionSub );
                }
                else
                {
                    // 必須オプション
                    frmMustOptionSub = new FormOwnerRsvPlanOptionSub();
                    frmMustOptionSub.setCheck( 0 );
                    frmMustOptionSub.setOptionID( result.getInt( "option_id" ) );
                    frmMustOptionSub.setOptionNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                    frmMustOptionSub.setOptionSubNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_sub_name" ) ) ) );
                    frmMustOptionSub.setOptionFlg( 1 );
                    mustOptSubList.add( frmMustOptionSub );
                }
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                if ( optionFlg == 0 )
                {
                    // 通常オプション
                    frmCommOptionSub = new FormOwnerRsvPlanOptionSub();
                    frmCommOptionSub.setErrMsg( Message.getMessage( "erro.30001", "オプション可能項目" ) );
                    subCommOptionFormList.add( frmCommOptionSub );
                }
                else
                {
                    // 必須オプション
                    frmMustOptionSub = new FormOwnerRsvPlanOptionSub();
                    frmMustOptionSub.setErrMsg( Message.getMessage( "erro.30001", "必須選択項目" ) );
                    subMustOptionFormList.add( frmMustOptionSub );
                }
                return;
            }

            // 必須オプションのデータを整形する
            if ( optionFlg == 1 )
            {
                for( int i = 0 ; i < mustOptSubList.size() ; i++ )
                {
                    FormOwnerRsvPlanOptionSub sub = mustOptSubList.get( i );
                    frmMustSub = new FormOwnerRsvPlanOptionSub();
                    orgOptId = sub.getOptionID();
                    if ( i == 0 )
                    {
                        newOptId = sub.getOptionID();
                        newOptNm = sub.getOptionNm();
                    }

                    if ( orgOptId == newOptId )
                    {
                        if ( subOptNm.trim().length() != 0 )
                        {
                            subOptNm = subOptNm + ",";
                        }
                        subOptNm = subOptNm + sub.getOptionSubNm();
                    }
                    else
                    {
                        frmMustSub.setCheck( 0 );
                        frmMustSub.setOptionID( newOptId );
                        frmMustSub.setOptionNm( newOptNm );
                        frmMustSub.setOptionSubNm( subOptNm );
                        frmMustSub.setOptionFlg( 1 );
                        subMustOptionFormList.add( frmMustSub );
                        subOptNm = "";
                        subOptNm = subOptNm + sub.getOptionSubNm();
                    }
                    newOptId = sub.getOptionID();
                    newOptNm = sub.getOptionNm();
                }
                frmMustSub = new FormOwnerRsvPlanOptionSub();
                frmMustSub.setCheck( 0 );
                frmMustSub.setOptionID( orgOptId );
                frmMustSub.setOptionNm( newOptNm );
                frmMustSub.setOptionSubNm( subOptNm );
                frmMustSub.setOptionFlg( 1 );
                subMustOptionFormList.add( frmMustSub );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getNewOption] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 登録済みオプション情報取得
     * 
     * @param optionFlg オプションフラグ(0:通常、1:必須)
     * @param draftKbn 0:通常、1:下書き
     * @return なし
     */
    public void getOption(int optionFlg, int draftKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int orgOptId = 0;
        int newOptId = 0;
        String subOptNm = "";
        String newOptNm = "";
        FormOwnerRsvPlanOptionSub frmMustSub;
        ArrayList<FormOwnerRsvPlanOptionSub> mustOptSubList = new ArrayList<FormOwnerRsvPlanOptionSub>();
        // int count = 0;

        query = query + "SELECT DISTINCT rel.option_id, op.option_name, op.option_sub_name ";
        if ( draftKbn == DRAFT_OFF )
        {
            query = query + "FROM hh_rsv_rel_plan_option rel ";
        }
        else
        {
            query = query + "FROM hh_rsv_rel_plan_option_draft rel ";
        }
        query = query + "   LEFT JOIN hh_rsv_option op ON rel.id = op.id AND rel.option_id = op.option_id AND rel.option_sub_id = op.option_sub_id ";
        query = query + "WHERE rel.id = ? ";
        query = query + "  AND rel.plan_id = ? ";
        query = query + "  AND op.option_flag = ? ";
        query = query + "ORDER BY rel.option_id, rel.option_sub_id";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSelPlanID() );
            prestate.setInt( 3, optionFlg );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( optionFlg == 0 )
                {
                    // 通常オプション
                    frmCommOptionSub = new FormOwnerRsvPlanOptionSub();
                    frmCommOptionSub.setCheck( 1 );
                    frmCommOptionSub.setOptionID( result.getInt( "option_id" ) );
                    frmCommOptionSub.setOptionNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                    frmCommOptionSub.setOptionSubNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_sub_name" ) ) ) );
                    subCommOptionFormList.add( frmCommOptionSub );
                }
                else
                {
                    // 必須オプション
                    frmMustOptionSub = new FormOwnerRsvPlanOptionSub();
                    frmMustOptionSub.setCheck( 1 );
                    frmMustOptionSub.setOptionID( result.getInt( "option_id" ) );
                    frmMustOptionSub.setOptionNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) ) );
                    frmMustOptionSub.setOptionSubNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_sub_name" ) ) ) );
                    mustOptSubList.add( frmMustOptionSub );
                }
            }

            // 必須オプションのデータを整形する
            if ( optionFlg == 1 )
            {
                for( int i = 0 ; i < mustOptSubList.size() ; i++ )
                {
                    FormOwnerRsvPlanOptionSub sub = mustOptSubList.get( i );
                    frmMustSub = new FormOwnerRsvPlanOptionSub();
                    orgOptId = sub.getOptionID();
                    if ( i == 0 )
                    {
                        newOptId = sub.getOptionID();
                        newOptNm = sub.getOptionNm();
                    }

                    if ( orgOptId == newOptId )
                    {
                        if ( subOptNm.trim().length() != 0 )
                        {
                            subOptNm = subOptNm + ",";
                        }
                        subOptNm = subOptNm + sub.getOptionSubNm();
                    }
                    else
                    {
                        frmMustSub.setCheck( 0 );
                        frmMustSub.setOptionID( newOptId );
                        frmMustSub.setOptionNm( newOptNm );
                        frmMustSub.setOptionSubNm( subOptNm );
                        frmMustSub.setOptionFlg( 1 );
                        subMustOptionFormList.add( frmMustSub );
                        subOptNm = "";
                        subOptNm = subOptNm + sub.getOptionSubNm();
                    }
                    newOptId = sub.getOptionID();
                    newOptNm = sub.getOptionNm();
                }
                if ( mustOptSubList.size() != 0 )
                {
                    frmMustSub = new FormOwnerRsvPlanOptionSub();
                    frmMustSub.setCheck( 0 );
                    frmMustSub.setOptionID( orgOptId );
                    frmMustSub.setOptionNm( newOptNm );
                    frmMustSub.setOptionSubNm( subOptNm );
                    frmMustSub.setOptionFlg( 1 );
                    subMustOptionFormList.add( frmMustSub );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getOption] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * プランID指定時のプランデータ取得
     * 
     * @param planId プランID
     * @param draftKbn 0:通常、1:下書き
     * @return なし
     */
    public void getPlan_PlanId(int planId, int draftKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String dispStartDate = "";
        String dispEndDate = "";
        String salesStartDate = "";
        String hapyPoint = "";
        String tekiyoRoom = "";
        String tekiyoStartDate = "";
        String tekiyoEndDate = "";

        query = query + "SELECT ";
        query = query + "pl.id, pl.plan_id, pl.plan_name, pl.disp_start_date, pl.disp_end_date, pl.sales_start_date, pl.sales_end_date, pl.reserve_start_day, pl.last_update, pl.last_uptime, ";
        query = query + "pl.reserve_end_day, pl.reserve_start_time, pl.reserve_end_time, pl.offer_kind, pl.max_quantity, pl.sales_flag, pl.giving_point_kind, pl.sales_stop_week_status, ";
        query = query + "pl.giving_point, pl.max_num_adult, pl.max_num_child, pl.min_num_adult, pl.min_num_child, pl.max_num_man, pl.max_num_woman, pl.min_num_man, pl.min_num_woman, ";
        query = query + " pl.man_count_judge_flag, pl.question, pl.question_flag, pl.remarks, pl.disp_index, pl.long_stay_day, pl.publishing_flag, ";
        query = query + "request_flag, pl.plan_pr, pl.image_pc, rel.seq, rel.sales_start_date AS seq_salesStartdate, rel.sales_end_date AS seq_salesEndDate, hotel.room_name ";
        if ( draftKbn == DRAFT_OFF )
        {
            query = query + "FROM hh_rsv_plan pl ";
            query = query + "  LEFT JOIN hh_rsv_rel_plan_room rel ON pl.id = rel.id AND pl.plan_id = rel.plan_id ";
        }
        else
        {
            query = query + "FROM hh_rsv_plan_draft pl ";
            query = query + "  LEFT JOIN hh_rsv_rel_plan_room_draft rel ON pl.id = rel.id AND pl.plan_id = rel.plan_id ";
        }
        query = query + "    INNER JOIN hh_hotel_room_more hotel ON rel.id = hotel.id AND rel.seq = hotel.seq ";
        query = query + "WHERE pl.id = ? ";
        query = query + "  AND pl.plan_id = ? ";
        query = query + "ORDER BY pl.plan_id, rel.seq ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                // 更新日時
                frm.setLastUpdateTime( result.getString( "last_update" ).substring( 0, 4 ) + "/" + result.getString( "last_update" ).substring( 4, 6 ) + "/" + result.getString( "last_update" ).substring( 6 ) +
                        " " + ConvertTime.convTimeHH( result.getInt( "last_uptime" ) ) + ":" + ConvertTime.convTimeMM( result.getInt( "last_uptime" ) ) );
                // 表示順
                frm.setDispIndex( Integer.toString( result.getInt( "disp_index" ) ) );
                // プラン名
                // frm.setPlanNm( ConvertCharacterSet.convDb2Form( result.getString( "plan_name" ) ) );
                frm.setPlanNm( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
                frm.setPlanNmView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
                // 表示期間
                dispStartDate = result.getString( "disp_start_date" );
                dispEndDate = result.getString( "disp_end_date" );
                frm.setDispStartDateView( dispStartDate.substring( 0, 4 ) + "年" + dispStartDate.substring( 4, 6 ) + "月" + dispStartDate.substring( 6 ) + "日 ～ " );
                frm.setDispStartDate( dispStartDate.substring( 0, 4 ) + "/" + dispStartDate.substring( 4, 6 ) + "/" + dispStartDate.substring( 6 ) );
                frm.setDispEndDateView( dispEndDate.substring( 0, 4 ) + "年" + dispEndDate.substring( 4, 6 ) + "月" + dispEndDate.substring( 6 ) + "日" );
                frm.setDispEndDate( dispEndDate.substring( 0, 4 ) + "/" + dispEndDate.substring( 4, 6 ) + "/" + dispEndDate.substring( 6 ) );
                frm.setOrgDispStartDate( dispStartDate );
                frm.setOrgDispEndDate( dispEndDate );
                // 販売期間
                salesStartDate = result.getString( "sales_start_date" );
                frm.setSalesStartDateView( salesStartDate.substring( 0, 4 ) + "年" + salesStartDate.substring( 4, 6 ) + "月" + salesStartDate.substring( 6 ) + "日 ～ " );
                frm.setSalesStartDate( salesStartDate.substring( 0, 4 ) + "/" + salesStartDate.substring( 4, 6 ) + "/" + salesStartDate.substring( 6 ) );
                frm.setSalesEndDateView( dispEndDate.substring( 0, 4 ) + "年" + dispEndDate.substring( 4, 6 ) + "月" + dispEndDate.substring( 6 ) + "日" );
                frm.setOrgSalesStartDate( salesStartDate );
                // 予約受付開始
                frm.setRsvEndDay( result.getString( "reserve_end_day" ) );
                frm.setRsvEndTimeHH( ConvertTime.convTimeHH( result.getInt( "reserve_end_time" ) ) );
                frm.setRsvEndTimeMM( ConvertTime.convTimeMM( result.getInt( "reserve_end_time" ) ) );
                frm.setRsvEndDayView( result.getString( "reserve_end_day" ) + "日前の"
                        + ConvertTime.convTimeHH( result.getInt( "reserve_end_time" ) ) + ":"
                        + ConvertTime.convTimeMM( result.getInt( "reserve_end_time" ) ) );
                frm.setRsvStartDay( result.getString( "reserve_start_day" ) );
                frm.setRsvStartTimeHH( ConvertTime.convTimeHH( result.getInt( "reserve_start_time" ) ) );
                frm.setRsvStartTimeMM( ConvertTime.convTimeMM( result.getInt( "reserve_start_time" ) ) );
                frm.setRsvStartDayView( result.getString( "reserve_start_day" ) + "日前の"
                        + ConvertTime.convTimeHH( result.getInt( "reserve_start_time" ) ) + ":"
                        + ConvertTime.convTimeMM( result.getInt( "reserve_start_time" ) ) );
                // 最大人数
                frm.setMaxNumAdultView( result.getInt( "max_num_adult" ) );
                frm.setMaxNumAdult( result.getString( "max_num_adult" ) );
                frm.setMaxNumChildView( result.getInt( "max_num_child" ) );
                frm.setMaxNumChild( result.getString( "max_num_child" ) );
                // 最少人数
                frm.setMinNumAdultView( result.getInt( "min_num_adult" ) );
                frm.setMinNumAdult( result.getString( "min_num_adult" ) );
                frm.setMinNumChildView( result.getInt( "min_num_child" ) );
                frm.setMinNumChild( result.getString( "min_num_child" ) );
                // 男女範囲設定
                frm.setMaxNumManView( result.getInt( "max_num_man" ) );
                frm.setMaxNumMan( result.getString( "max_num_man" ) );
                frm.setMaxNumWomanView( result.getInt( "max_num_woman" ) );
                frm.setMaxNumWoman( result.getString( "max_num_woman" ) );
                frm.setMinNumManView( result.getInt( "min_num_man" ) );
                frm.setMinNumMan( result.getString( "min_num_man" ) );
                frm.setMinNumWomanView( result.getInt( "min_num_woman" ) );
                frm.setMinNumWoman( result.getString( "min_num_woman" ) );
                frm.setManCountJudgeFlg( result.getInt( "man_count_judge_flag" ) );
                // 販売停止曜日
                frm.setSalesStopWeekStatus( result.getInt( "sales_stop_week_status" ) );

                // 連泊可能数
                frm.setRenpakuNumView( result.getInt( "long_stay_day" ) );
                frm.setRenpakuNum( result.getString( "long_stay_day" ) );
                // ハピー付与
                if ( result.getInt( "giving_point_kind" ) == 1 )
                {
                    // 室料
                    hapyPoint = "室料の" + result.getString( "giving_point" ) + "%";
                    frm.setPointRoom( result.getString( "giving_point" ) );
                    frm.setPointFix( "" );
                }
                else
                {
                    // 固定ポイント
                    hapyPoint = result.getString( "giving_point" );
                    frm.setPointRoom( "" );
                    frm.setPointFix( result.getString( "giving_point" ) );
                }
                frm.setHapyPointView( hapyPoint );
                frm.setPointKbn( result.getInt( "giving_point_kind" ) );
                // 予約者へ質問必須フラグ
                frm.setQuestionFlag( result.getInt( "question_flag" ) );
                // 予約者へ質問
                frm.setQuestion( ConvertCharacterSet.convDb2Form( result.getString( "question" ) ) );
                frm.setQuestionView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "question" ) ) ) );
                // プラン紹介
                frm.setPlanInfo( ConvertCharacterSet.convDb2Form( result.getString( "plan_pr" ) ) );
                frm.setPlanInfoView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_pr" ) ) ) );
                // 備考
                frm.setRemarks( ConvertCharacterSet.convDb2Form( result.getString( "remarks" ) ) );
                frm.setRemarksView( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ) ) ) );

                // プラン画像
                frm.setPlanImg( result.getString( "image_pc" ) );
                frm.setPlanImgView( result.getString( "image_pc" ) );
                // 最大予約受付数
                frm.setMaxQuantity( result.getString( "max_quantity" ) );
                frm.setMaxQuantityView( result.getInt( "max_quantity" ) );
                // 部屋指定
                frm.setOfferKbn( result.getInt( "offer_kind" ) );
                // プラン掲載
                frm.setPublishingFlg( result.getInt( "publishing_flag" ) );
                // 要望入力表示フラグ
                frm.setUserRequestFlg( result.getInt( "request_flag" ) );
                // ボタンの値
                frm.setSalesBtnValue( BTN_STOP );
                if ( result.getInt( "sales_flag" ) == 0 )
                {
                    // 一時的に停止する。
                    frm.setSalesBtnValue( BTN_ON );
                }

                // 適用部屋
                tekiyoStartDate = result.getString( "seq_salesStartdate" ).substring( 0, 4 ) + "/"
                        + result.getString( "seq_salesStartdate" ).substring( 4, 6 ) + "/"
                        + result.getString( "seq_salesStartdate" ).substring( 6 );

                tekiyoEndDate = result.getString( "seq_salesEndDate" ).substring( 0, 4 ) + "/"
                        + result.getString( "seq_salesEndDate" ).substring( 4, 6 ) + "/"
                        + result.getString( "seq_salesEndDate" ).substring( 6 );
                // tekiyoRoom = tekiyoRoom + result.getString( "seq" ) + "(" + tekiyoStartDate + " ～ " + tekiyoEndDate + ")" + "<br />";
                tekiyoRoom = tekiyoRoom + result.getString( "seq" ) + "<br />";
                frm.setTekiyoRoom( tekiyoRoom );
                frmSub = new FormOwnerRsvPlanSub();
                frmSub.setCheck( 1 );
                frmSub.setSeq( result.getInt( "seq" ) );
                frmSub.setRoomNm( result.getString( "room_name" ) );
                frmSub.setTekiyoDateFrom( tekiyoStartDate );
                frmSub.setTekiyoDateTo( tekiyoEndDate );
                subFormList.add( frmSub );
            }
            frm.setFrmSubList( subFormList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getPlan_PlanId] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * プラン・部屋データ登録
     * 
     * @param regKbn 登録区分(1:新規登録、2:更新)
     * @param draftKbn 下書き区分(0:通常、1:下書き)
     * @param btnKbn ボタン区分(2:プラン設定更新、7:プレビュー)
     * @return なし
     */
    public void registPlan(int regKbn, int draftKbn, int btnKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;
        int fromDate = 0;
        int toDate = 0;
        String fromDateStr = "";
        String toDateStr = "";
        String newFromDateStr = "";
        String newToDateStr = "";
        Calendar cal;

        try
        {
            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            // プランデータ登録
            ret = execRegistPlan( prestate, connection, regKbn, draftKbn, btnKbn );

            // プラン・部屋データ登録
            if ( ret == true )
            {
                ret = execRegistPlanRoom( prestate, connection, regKbn, draftKbn, btnKbn );
            }

            // オプションデータ登録
            if ( ret == true )
            {
                ret = execRegistOption( prestate, connection, regKbn, draftKbn, btnKbn );
            }

            // プラン設定更新の場合、販売期間終了の日別料金情報削除
            if ( (btnKbn == OwnerRsvCommon.BTN_REGIST) && ((draftKbn == DRAFT_OFF) && (regKbn == MODE_EDIT)) )
            {
                if ( ret == true )
                {
                    fromDateStr = frm.getDispEndDate().replace( "/", "" );
                    toDate = Integer.parseInt( frm.getOrgDispEndDate() );

                    // 1日後取得
                    cal = Calendar.getInstance();
                    cal.set( Integer.parseInt( fromDateStr.substring( 0, 4 ) ), Integer.parseInt( fromDateStr.substring( 4, 6 ) ) - 1, Integer.parseInt( fromDateStr.substring( 6 ) ) );
                    Date dt = cal.getTime();
                    cal.setTime( dt );
                    cal.add( Calendar.DATE, 1 );

                    newFromDateStr = Integer.toString( cal.get( Calendar.YEAR ) ) + (String.format( "%02d", (cal.get( Calendar.MONTH )) + 1 )) + (String.format( "%02d", (cal.get( Calendar.DATE )) ));
                    fromDate = Integer.parseInt( newFromDateStr );

                    if ( fromDate <= toDate )
                    {
                        ret = execRegistDayCharge( prestate, connection, fromDate, toDate );
                    }
                }

                // 販売期間開始の日別料金情報削除
                if ( ret == true )
                {
                    fromDate = Integer.parseInt( frm.getOrgSalesStartDate() );
                    toDateStr = frm.getSalesStartDate().replace( "/", "" );

                    // 1日前取得
                    cal = Calendar.getInstance();
                    cal.set( Integer.parseInt( toDateStr.substring( 0, 4 ) ), Integer.parseInt( toDateStr.substring( 4, 6 ) ) - 1, Integer.parseInt( toDateStr.substring( 6 ) ) );
                    Date dt = cal.getTime();
                    cal.setTime( dt );
                    cal.add( Calendar.DATE, -1 );
                    newToDateStr = Integer.toString( cal.get( Calendar.YEAR ) ) + (String.format( "%02d", (cal.get( Calendar.MONTH )) + 1 )) + (String.format( "%02d", (cal.get( Calendar.DATE )) ));
                    toDate = Integer.parseInt( newToDateStr );

                    if ( fromDate <= toDate )
                    {
                        ret = execRegistDayCharge( prestate, connection, fromDate, toDate );
                    }
                }
            }

            if ( ret )
            {
                query = "COMMIT ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }
            else
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }
        }
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            Logging.error( "[LogicOwnerRsvPlan.registPlan] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * プランデータ登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param regKbn 2:新規登録、4;更新
     * @param draftKbn 0:通常、1:下書き
     * @param btnKbn 2:プラン設定更新、7:プレビュー
     * @return true:正常、false:失敗
     */
    public boolean execRegistPlan(PreparedStatement prestate, Connection conn, int regKbn, int draftKbn, int btnKbn) throws Exception
    {
        String dispStartDate = "";
        String dispEndDate = "";
        String salesStartDate = "";
        int startTime = 0;
        int endTime = 0;
        String query = "";
        int result;
        boolean ret = false;

        if ( regKbn == MODE_NEW )
        {
            query = query + "INSERT ";
        }
        else
        {
            query = query + "UPDATE ";
        }

        if ( draftKbn == DRAFT_OFF )
        {
            // 通常
            if ( btnKbn == OwnerRsvCommon.BTN_PREVIEW )
            {
                // プレビューの場合
                query = query + "hh_rsv_plan_preview SET ";
            }
            else
            {
                query = query + "hh_rsv_plan SET ";
            }
        }
        else
        {
            // 下書き
            query = query + "hh_rsv_plan_draft SET ";
        }
        query = query + "plan_name = ?, ";
        query = query + "disp_start_date = ?, ";
        query = query + "disp_end_date = ?, ";
        query = query + "sales_start_date = ?, ";
        query = query + "sales_end_date = ?, ";
        query = query + "reserve_start_day = ?, ";
        query = query + "reserve_end_day = ?, ";
        query = query + "reserve_start_time = ?, ";
        query = query + "reserve_end_time = ?, ";
        query = query + "offer_kind = ?, ";
        query = query + "plan_pr = ?, ";
        query = query + "max_quantity = ?, ";
        query = query + "plan_group_id = ?, ";
        query = query + "image_pc = ?, ";
        query = query + "image_gif = ?, ";
        query = query + "image_png = ?, ";
        query = query + "max_num_adult = ?, ";
        query = query + "max_num_child = ?, ";
        query = query + "min_num_adult = ?, ";
        query = query + "min_num_child = ?, ";
        query = query + "question = ?, ";
        query = query + "question_flag = ?, ";
        query = query + "remarks = ?, ";
        query = query + "disp_index = ?, ";
        query = query + "long_stay_day = ?, ";
        query = query + "publishing_flag = ?, ";
        query = query + "request_flag = ?, ";
        query = query + "hotel_id = ?,";
        query = query + "user_id = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ?, ";
        query = query + "max_num_man = ?, ";
        query = query + "max_num_woman = ?, ";
        query = query + "min_num_man = ?, ";
        query = query + "min_num_woman = ?, ";
        query = query + "man_count_judge_flag = ?,";
        query = query + "sales_stop_week_status = ?";

        if ( (regKbn == MODE_NEW) || (frm.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA) )
        {
            // 新規登録の場合は、事務局ユーザ、ホテルのオーナーユーザに関わらず、ハピー付与ポイントを設定する
            // ホテルのオーナーユーザの場合は、ハピー付与ポイントの更新権限がないため、更新時にはハピー付与ポイントは設定なし
            query = query + ", giving_point_kind = ? ";
            query = query + ", giving_point = ? ";
        }
        if ( regKbn == MODE_NEW )
        {
            query = query + ", sales_flag = ? ";
            query = query + ", id = ? ";
        }
        if ( regKbn == MODE_EDIT )
        {
            query = query + " WHERE id = ? ";
            query = query + "  AND plan_id = ? ";
        }

        try
        {
            dispStartDate = frm.getDispStartDate().substring( 0, 4 ) + frm.getDispStartDate().substring( 5, 7 ) + frm.getDispStartDate().substring( 8 );
            dispEndDate = frm.getDispEndDate().substring( 0, 4 ) + frm.getDispEndDate().substring( 5, 7 ) + frm.getDispEndDate().substring( 8 );
            if ( Integer.parseInt( dispEndDate ) >= 20150901 )
            {
                dispEndDate = "20150831";
            }

            salesStartDate = frm.getSalesStartDate().substring( 0, 4 ) + frm.getSalesStartDate().substring( 5, 7 ) + frm.getSalesStartDate().substring( 8 );
            startTime = ConvertTime.convTimeSS( Integer.parseInt( frm.getRsvStartTimeHH() ), Integer.parseInt( frm.getRsvStartTimeMM() ), 2 );
            endTime = ConvertTime.convTimeSS( Integer.parseInt( frm.getRsvEndTimeHH() ), Integer.parseInt( frm.getRsvEndTimeMM() ), 2 );

            prestate = conn.prepareStatement( query );
            prestate.setString( 1, ConvertCharacterSet.convForm2Db( frm.getPlanNm() ) );
            prestate.setInt( 2, Integer.parseInt( dispStartDate ) );
            prestate.setInt( 3, Integer.parseInt( dispEndDate ) );
            prestate.setInt( 4, Integer.parseInt( salesStartDate ) );
            prestate.setInt( 5, Integer.parseInt( dispEndDate ) );
            prestate.setInt( 6, Integer.parseInt( frm.getRsvStartDay() ) );
            prestate.setInt( 7, Integer.parseInt( frm.getRsvEndDay() ) );
            prestate.setInt( 8, startTime );
            prestate.setInt( 9, endTime );
            prestate.setInt( 10, frm.getOfferKbn() );
            prestate.setString( 11, ConvertCharacterSet.convForm2Db( frm.getPlanInfo() ) );
            prestate.setInt( 12, Integer.parseInt( frm.getMaxQuantity() ) );
            prestate.setInt( 13, 0 );
            prestate.setString( 14, frm.getPlanImg() );
            prestate.setString( 15, "" );
            prestate.setString( 16, "" );
            prestate.setInt( 17, Integer.parseInt( frm.getMaxNumAdult() ) );
            prestate.setInt( 18, Integer.parseInt( frm.getMaxNumChild() ) );
            prestate.setInt( 19, Integer.parseInt( frm.getMinNumAdult() ) );
            prestate.setInt( 20, Integer.parseInt( frm.getMinNumChild() ) );
            prestate.setString( 21, ConvertCharacterSet.convForm2Db( frm.getQuestion() ) );
            prestate.setInt( 22, frm.getQuestionFlag() );
            prestate.setString( 23, ConvertCharacterSet.convForm2Db( frm.getRemarks() ) );
            prestate.setInt( 24, Integer.parseInt( frm.getDispIndex() ) );
            prestate.setInt( 25, Integer.parseInt( frm.getRenpakuNum() ) );
            prestate.setInt( 26, frm.getPublishingFlg() );
            prestate.setInt( 27, frm.getUserRequestFlg() );
            prestate.setString( 28, frm.getOwnerHotelID() );
            prestate.setInt( 29, frm.getUserId() );
            prestate.setInt( 30, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 31, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 32, Integer.parseInt( frm.getMaxNumMan() ) );
            prestate.setInt( 33, Integer.parseInt( frm.getMaxNumWoman() ) );
            prestate.setInt( 34, Integer.parseInt( frm.getMinNumMan() ) );
            prestate.setInt( 35, Integer.parseInt( frm.getMinNumWoman() ) );
            prestate.setInt( 36, frm.getManCountJudgeFlg() );
            prestate.setInt( 37, frm.getSalesStopWeekStatus() );

            if ( (regKbn == MODE_NEW) || (frm.getImediaFlg() == OwnerRsvCommon.IMEDIAFLG_IMEDIA) )
            {
                // 新規登録の場合は、事務局ユーザ、ホテルのオーナーユーザに関わらず、ハピー付与ポイントを設定する
                prestate.setInt( 38, frm.getPointKbn() );
                if ( frm.getPointKbn() == 1 )
                {
                    // 室料
                    prestate.setInt( 39, Integer.parseInt( frm.getPointRoom() ) );
                }
                else
                {
                    // 固定
                    prestate.setInt( 39, Integer.parseInt( frm.getPointFix() ) );
                }
                if ( regKbn == MODE_NEW )
                {
                    prestate.setInt( 40, frm.getSalesFlag() );
                    prestate.setInt( 41, frm.getSelHotelID() );

                }
                else if ( regKbn == MODE_EDIT )
                {
                    prestate.setInt( 40, frm.getSelHotelID() );
                    prestate.setInt( 41, frm.getSelPlanID() );
                }
            }
            else
            {
                // ホテルのオーナーユーザの場合は、ハピー付与ポイントの更新権限が無く、更新時にはハピー付与ポイントは更新されない
                // そのため、ＳＱＬのパラメータインデックスの指定に注意すること
                if ( regKbn == MODE_NEW )
                {
                    prestate.setInt( 38, frm.getSalesFlag() );
                    prestate.setInt( 39, frm.getSelHotelID() );

                }
                else if ( regKbn == MODE_EDIT )
                {
                    prestate.setInt( 38, frm.getSelHotelID() );
                    prestate.setInt( 39, frm.getSelPlanID() );
                }
            }
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.execRegistPlan] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * プラン・部屋データ登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param regKbn 2:新規登録、4:更新
     * @param draftKbn 0:通常、1：下書き
     * @param btnKbn 2:プラン設定更新、7:プレビュー
     * @return true:正常、false:失敗
     */
    public boolean execRegistPlanRoom(PreparedStatement prestate, Connection conn, int regKbn, int draftKbn, int btnKbn) throws Exception
    {
        int planId;
        boolean ret = false;
        ArrayList<FormOwnerRsvPlanSub> frmSubList = new ArrayList<FormOwnerRsvPlanSub>();
        FormOwnerRsvPlanSub frmSub;

        try
        {
            // FormOwnerRsvPlanSubのリスト取得
            frmSubList = frm.getFrmSubList();

            // プランID取得
            if ( regKbn == MODE_NEW )
            {
                planId = getPlanId( prestate, conn );
                // 修正履歴用
                frm.setSelPlanID( planId );
            }
            else
            {
                planId = frm.getSelPlanID();
            }

            ret = true;
            // データ削除
            deletePlanRoom( prestate, conn, planId, draftKbn, btnKbn );

            // 新規登録
            for( int i = 0 ; i < frmSubList.size() ; i++ )
            {
                frmSub = frmSubList.get( i );

                if ( registPlanRoom( prestate, conn, frmSub, planId, draftKbn, btnKbn ) == false )
                {
                    return(ret);
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.execRegistPlanRoom] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 日別料金マスタ登録
     * 指定日付の期間が変更された場合、対象の日付を削除する。
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param fromDate 削除開始日
     * @param toDate 削除終了日
     * @return true:正常、false:失敗
     */
    private boolean execRegistDayCharge(PreparedStatement prestate, Connection conn, int fromDate, int toDate) throws Exception
    {
        String query = "";
        boolean ret = true;

        query = query + "DELETE FROM hh_rsv_day_charge ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";
        query = query + "  AND cal_date BETWEEN ? AND ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSelPlanID() );
            prestate.setInt( 3, fromDate );
            prestate.setInt( 4, toDate );
            prestate.executeUpdate();

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.execRegistDayCharge] Exception=" + e.toString() );
            ret = false;
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * オプション登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param regKbn 2:新規登録、4:更新
     * @param draftKbn 0:通常、1:下書き
     * @param btnKbn 2:プラン設定更新、7:プレビュー
     * @return true:正常、false:失敗
     */
    private boolean execRegistOption(PreparedStatement prestate, Connection conn, int regKbn, int draftKbn, int btnKbn) throws Exception
    {
        int planId;
        boolean ret = false;
        int optId = 0;
        ArrayList<Integer> mustOptIDList = new ArrayList<Integer>();
        ArrayList<Integer> commOptIDList = new ArrayList<Integer>();
        ArrayList<Integer> subOptIdList = new ArrayList<Integer>();

        try
        {
            // プランID取得
            if ( regKbn == MODE_NEW )
            {
                planId = getPlanId( prestate, conn );
            }
            else
            {
                planId = frm.getSelPlanID();
            }

            ret = true;
            // データ削除
            deleteOption( prestate, conn, planId, draftKbn, btnKbn );

            // 必須オプション登録
            mustOptIDList = frm.getMustOptIdList();
            for( int i = 0 ; i < mustOptIDList.size() ; i++ )
            {
                optId = mustOptIDList.get( i );

                // サブオプションIDの取得
                subOptIdList = getSubOptionIdList( optId );

                for( int j = 0 ; j < subOptIdList.size() ; j++ )
                {
                    if ( registOption( prestate, conn, planId, optId, subOptIdList.get( j ), draftKbn, btnKbn ) == false )
                    {
                        return(ret);
                    }
                }
            }

            // 通常オプション登録
            commOptIDList = frm.getComOptIdList();
            for( int i = 0 ; i < commOptIDList.size() ; i++ )
            {
                optId = commOptIDList.get( i );
                if ( registOption( prestate, conn, planId, optId, 1, draftKbn, btnKbn ) == false )
                {
                    return(ret);
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.execRegistOption] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 登録したプランID取得
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @return true:正常、false:失敗
     */
    private int getPlanId(PreparedStatement prestate, Connection connection) throws Exception
    {
        String query = "";
        ResultSet result = null;
        int planId = 0;

        query = query + "SELECT LAST_INSERT_ID() AS IDX ";

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            while( result.next() )
            {
                planId = result.getInt( "IDX" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getPlanId] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(planId);
    }

    /**
     * プラン・部屋データ削除
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param planId プランID
     * @param draftKbn 0:通常、1:下書き
     * @param btnKbn 2:プラン設定更新、7:プレビュー
     * @return true:正常、false:失敗
     */
    private boolean deletePlanRoom(PreparedStatement prestate, Connection conn, int planId, int draftKbn, int btnKbn) throws Exception
    {
        String query = "";
        int result;
        boolean ret = false;

        query = query + "DELETE FROM ";
        if ( draftKbn == DRAFT_OFF )
        {
            // 通常
            if ( btnKbn == OwnerRsvCommon.BTN_PREVIEW )
            {
                // プレビュー
                query = query + "hh_rsv_rel_plan_room_preview ";
            }
            else
            {
                query = query + "hh_rsv_rel_plan_room ";
            }
        }
        else
        {
            // 下書き
            query = query + "hh_rsv_rel_plan_room_draft ";
        }
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, planId );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.deletePlanRoom] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * オプションデータ削除
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param planId プランID
     * @param draftKbn 0:通常、1:下書き
     * @return true:正常、false:失敗
     */
    private boolean deleteOption(PreparedStatement prestate, Connection conn, int planId, int draftKbn, int btnKbn) throws Exception
    {
        String query = "";
        int result;
        boolean ret = false;

        query = query + "DELETE FROM ";
        if ( draftKbn == DRAFT_OFF )
        {
            // 通常
            if ( btnKbn == OwnerRsvCommon.BTN_PREVIEW )
            {
                // プレビュー
                query = query + "hh_rsv_rel_plan_option_preview ";
            }
            else
            {
                query = query + "hh_rsv_rel_plan_option ";
            }
        }
        else
        {
            // 下書き
            query = query + "hh_rsv_rel_plan_option_draft ";
        }
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, planId );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.deleteOption] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * プラン・部屋データ登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param paraFrmSub FormOwnerRsvPlanSubオブジェクト
     * @param planId プランID
     * @param draftKbn 0:通常、1:下書き
     * @param btnKbn 2:プラン設定更新、7:プレビュー
     * @return true:正常、false:失敗
     */
    private boolean registPlanRoom(PreparedStatement prestate, Connection conn, FormOwnerRsvPlanSub paraFrmSub, int planId, int draftKbn, int btnKbn) throws Exception
    {
        String salesStartDate = "";
        String salesEndDate = "";
        String query = "";
        int result;
        boolean ret = false;

        query = query + "INSERT ";
        if ( draftKbn == DRAFT_OFF )
        {
            // 通常
            if ( btnKbn == OwnerRsvCommon.BTN_PREVIEW )
            {
                // プレビュー
                query = query + "hh_rsv_rel_plan_room_preview SET ";
            }
            else
            {
                query = query + "hh_rsv_rel_plan_room SET ";
            }
        }
        else
        {
            // 下書き
            query = query + "hh_rsv_rel_plan_room_draft SET ";
        }
        query = query + "id = ?, ";
        query = query + "plan_id = ?, ";
        query = query + "seq = ?, ";
        query = query + "sales_start_date = ?, ";
        query = query + "sales_end_date = ?, ";
        query = query + "hotel_id = ?, ";
        query = query + "user_id = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ? ";

        try
        {
            if ( (paraFrmSub.getTekiyoDateFrom() == null) || (paraFrmSub.getTekiyoDateFrom().trim().length() == 0) )
            {
                // 販売開始日を取得
                salesStartDate = getSalesDate( conn, 1, frm.getSelHotelID(), planId, draftKbn );
            }
            else
            {
                salesStartDate = paraFrmSub.getTekiyoDateFrom().substring( 0, 4 ) + paraFrmSub.getTekiyoDateFrom().substring( 5, 7 ) + paraFrmSub.getTekiyoDateFrom().substring( 8 );
            }

            if ( (paraFrmSub.getTekiyoDateTo() == null) || (paraFrmSub.getTekiyoDateTo().trim().length() == 0) )
            {
                // 販売終了日を取得
                salesEndDate = getSalesDate( conn, 2, frm.getSelHotelID(), planId, draftKbn );
            }
            else
            {
                salesEndDate = paraFrmSub.getTekiyoDateTo().substring( 0, 4 ) + paraFrmSub.getTekiyoDateTo().substring( 5, 7 ) + paraFrmSub.getTekiyoDateTo().substring( 8 );
            }
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, paraFrmSub.getSeq() );
            prestate.setInt( 4, Integer.parseInt( salesStartDate ) );
            prestate.setInt( 5, Integer.parseInt( salesEndDate ) );
            prestate.setString( 6, frm.getOwnerHotelID() );
            prestate.setInt( 7, frm.getUserId() );
            prestate.setInt( 8, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 9, Integer.parseInt( DateEdit.getTime( 1 ) ) );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.registPlanRoom] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * オプションデータ登録
     * 
     * @param prestate PreparedStatement
     * @param conn Connection
     * @param planId プランID
     * @param optId オプションID
     * @param optionFlg オプションフラグ(0:通常、1:必須)
     * @param draftKbn 0:通常、1：下書き
     * @param btnKbn 2:プラン設定更新、7:プレビュー
     * @return true:正常、false:失敗
     */
    private boolean registOption(PreparedStatement prestate, Connection conn, int planId, int optId, int subOptId, int draftKbn, int btnKbn) throws Exception
    {
        String query = "";
        int result;
        boolean ret = false;

        if ( draftKbn == DRAFT_OFF )
        {
            // 通常
            if ( btnKbn == OwnerRsvCommon.BTN_PREVIEW )
            {
                // プレビュー
                query = query + "INSERT hh_rsv_rel_plan_option_preview SET ";
            }
            else
            {
                query = query + "INSERT hh_rsv_rel_plan_option SET ";
            }
        }
        else
        {
            // 下書き
            query = query + "INSERT hh_rsv_rel_plan_option_draft SET ";
        }
        query = query + "id = ?, ";
        query = query + "plan_id = ?, ";
        query = query + "option_id = ?, ";
        query = query + "option_sub_id = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, optId );
            prestate.setInt( 4, subOptId );

            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.registOption] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 販売開始、終了日の取得
     * 
     * @param conn Connection
     * @param int selKbn 1:販売開始日を取得、2;販売終了日を取得
     * @param hotelId ホテルID
     * @param planId プランID
     * @param draftKbn 下書き区分(0:通常、1:下書き)
     * @return 販売開始、または終了日
     */
    public String getSalesDate(Connection conn, int selKbn, int hotelId, int planId, int draftKbn) throws Exception
    {
        String query = "";
        PreparedStatement prestate = null;
        ResultSet result = null;
        String salesDate = "";

        query = query + "SELECT sales_start_date, sales_end_date FROM ";
        if ( draftKbn == 0 )
        {
            query = query + " hh_rsv_plan ";
        }
        else
        {
            query = query + " hh_rsv_plan_draft ";
        }
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                if ( selKbn == 1 )
                {
                    // 販売開始日を取得
                    salesDate = result.getString( "sales_start_date" );

                }
                else
                {
                    // 販売終了日を取得
                    salesDate = result.getString( "sales_end_date" );
                }
            }

            return(salesDate);
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getSalesDate] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * サブオプションID取得
     * 
     * @param optId オプションID
     * @return サブオプションIDのリスト
     */
    public ArrayList<Integer> getSubOptionIdList(int optId) throws Exception
    {
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        ArrayList<Integer> subOptIdList = new ArrayList<Integer>();

        query = query + "SELECT option_sub_id FROM hh_rsv_option ";
        query = query + "WHERE id = ? ";
        query = query + "  AND option_id = ? ";
        query = query + "  AND option_flag = 1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, optId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                subOptIdList.add( result.getInt( "option_sub_id" ) );
            }
            return subOptIdList;
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getSubOptionIdList] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 販売フラグ取得
     * 
     * @param なし
     * @return 販売フラグの値
     */
    public int getSalesFlg() throws Exception
    {
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        int salesFlg = -1;

        query = query + "SELECT sales_flag FROM hh_rsv_plan ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSelPlanID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                salesFlg = result.getInt( "sales_flag" );
            }

            return salesFlg;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getSalesFlg] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 日別料金マスタ削除
     * 
     * @param salesFlg　販売フラグ
     * @return true:正常、false:失敗
     */
    public boolean setSalesFlg(int salesFlg) throws Exception
    {

        String query = "";
        int result = 0;
        Connection connection = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "UPDATE hh_rsv_plan SET sales_flag = ? ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, salesFlg );
            prestate.setInt( 2, frm.getSelHotelID() );
            prestate.setInt( 3, frm.getSelPlanID() );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.setSalesFlg] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * プランに紐付いている料金ﾓｰﾄﾞのﾁｪｯｸｲﾝ・ﾁｪｯｸｱｳﾄ時刻の取得
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @return なし
     */
    public void getPlanChargeDetail(FormOwnerRsvPlan frm) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<Integer> ciTimeFromList = new ArrayList<Integer>();
        ArrayList<Integer> ciTimeToList = new ArrayList<Integer>();
        ArrayList<Integer> coTimeList = new ArrayList<Integer>();

        query = "SELECT ci_time_from, ci_time_to , co_time FROM hh_rsv_plan_charge PC" +
                " LEFT JOIN hh_rsv_charge_mode CM ON ( CM.id = PC.id " +
                " AND CM.charge_mode_id = PC.charge_mode_id ) " +
                " WHERE PC.id = ? AND PC.plan_id = ? ";

        try
        {
            query += " AND PC.charge_mode_id IN (";
            for( int i = 0 ; i < frm.getChargeModeIdList().size() ; i++ )
            {
                if ( i != 0 )
                {
                    query += ",";
                }
                query += "?";
            }
            query += ") ORDER BY CM.disp_index";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSelPlanID() );
            for( int i = 0 ; i < frm.getChargeModeIdList().size() ; i++ )
            {
                prestate.setInt( i + 3, frm.getChargeModeIdList().get( i ) );
            }
            result = prestate.executeQuery();
            while( result.next() != false )
            {
                ciTimeFromList.add( result.getInt( "ci_time_from" ) );
                ciTimeToList.add( result.getInt( "ci_time_to" ) );
                coTimeList.add( result.getInt( "co_time" ) );
            }
            frm.setCiFromList( ciTimeFromList );
            frm.setCiToList( ciTimeToList );
            frm.setCoList( coTimeList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getPlanChargeDetail] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return;
    }

    /**
     * プランに紐付いている料金ﾓｰﾄﾞ名称取得
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @return なし
     */
    public void getPlanChargeModeName(FormOwnerRsvPlan frm) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> chargeModeNameList = new ArrayList<String>();
        ArrayList<Integer> chargeModeIdList = new ArrayList<Integer>();

        query = "SELECT charge_mode_name, hh_rsv_plan_charge.charge_mode_id FROM hh_rsv_charge_mode " +
                " LEFT JOIN hh_rsv_plan_charge ON hh_rsv_charge_mode.id = hh_rsv_plan_charge.id " +
                " AND hh_rsv_charge_mode.charge_mode_id = hh_rsv_plan_charge.charge_mode_id " +
                " WHERE hh_rsv_plan_charge.id = ? AND hh_rsv_plan_charge.plan_id = ? ORDER BY hh_rsv_charge_mode.disp_index";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSelPlanID() );
            result = prestate.executeQuery();
            while( result.next() )
            {
                chargeModeNameList.add( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) );
                chargeModeIdList.add( result.getInt( "charge_mode_id" ) );
            }
            frm.setChargeModeNameList( chargeModeNameList );
            frm.setChargeModeIdList( chargeModeIdList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getDefaultChargeModeName] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return;
    }

    /**
     * 料金情報取得
     * 
     * @param frm FormOwnerRsvPlanオブジェクト
     * @return なし
     */
    public void getPlanCharge(FormOwnerRsvPlan frm) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        String modeInfo = "";
        String chargeInfo = "";
        String charge = "";
        String ciInfoFrom = "";
        String ciInfoTo = "";
        String ciTimeFrom = "";
        String ciTimeTo = "";
        String ciInfo = "";
        String coTime = "";
        String coTimeInfo = "";
        String coInfo = "";
        int lowCharge = 0;
        NumberFormat formatCur = NumberFormat.getCurrencyInstance();
        ArrayList<FormOwnerRsvPlanChargeSub> frmPlanChargeSubList = new ArrayList<FormOwnerRsvPlanChargeSub>();

        query = query + "SELECT ";
        query = query + "ch.id, ch.plan_id, ch.charge_mode_id, cm.charge_mode_name, ch.ci_time_from, ch.ci_time_to, ch.co_time, ";
        query = query + "ch.adult_two_charge, ch.adult_one_charge, ch.adult_add_charge, ch.child_add_charge, ch.co_kind ";
        query = query + "FROM hh_rsv_plan_charge ch ";
        query = query + "  LEFT JOIN hh_rsv_charge_mode cm ON ch.id = cm.id AND ch.charge_mode_id = cm.charge_mode_id ";
        query = query + "WHERE ch.id = ? ";
        query = query + "  AND ch.plan_id = ? ";
        query += " ORDER BY cm.disp_index";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            prestate.setInt( 2, frm.getSelPlanID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                FormOwnerRsvPlanChargeSub frmSub = new FormOwnerRsvPlanChargeSub();
                // 日付情報
                modeInfo = "";
                modeInfo = modeInfo + " (" + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "charge_mode_name" ) ) ) + ") ";

                // 料金
                if ( frm.getMaxNumAdult().equals( "" ) || frm.getMinNumAdult().equals( "" ) || frm.getMinNumChild().equals( "" ) )
                {
                    charge = formatCur.format( result.getLong( "adult_two_charge" ) );
                    lowCharge = 0;
                }
                else
                {
                    if ( Integer.parseInt( frm.getMaxNumAdult() ) < 2 )
                    {
                        // 大人1人用の金額 + 子供金額 * 最低子供人数
                        frmSub.setAdultTwo( formatCur.format( (result.getLong( "adult_one_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() )) ) );
                        charge = formatCur.format( (result.getLong( "adult_one_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() )) );
                        if ( lowCharge == 0 || ((result.getLong( "adult_one_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() )) > 0 &&
                                (result.getLong( "adult_one_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() )) < lowCharge) )
                        {
                            lowCharge = (int)(result.getLong( "adult_one_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() ));
                        }
                    }
                    else if ( Integer.parseInt( frm.getMinNumAdult() ) > 2 )
                    {
                        // 大人2人用の金額 + 大人追加金額 * オーバー人数 + 子供金額 * 最低子供人数
                        frmSub.setAdultTwo( formatCur.format( result.getLong( "adult_two_charge" ) + result.getLong( "adult_add_charge" ) * (Integer.parseInt( frm.getMinNumAdult() ) - 2) + result.getLong( "child_add_charge" )
                                * Integer.parseInt( frm.getMinNumChild() ) ) );
                        charge = formatCur.format( result.getLong( "adult_two_charge" ) + result.getLong( "adult_add_charge" ) * (Integer.parseInt( frm.getMinNumAdult() ) - 2) + result.getLong( "child_add_charge" )
                                * Integer.parseInt( frm.getMinNumChild() ) );
                        if ( lowCharge == 0 || ((result.getLong( "adult_two_charge" ) + result.getLong( "adult_add_charge" ) * (Integer.parseInt( frm.getMinNumAdult() ) - 2) > 0 &&
                                (result.getLong( "adult_two_charge" ) + result.getLong( "adult_add_charge" ) * (Integer.parseInt( frm.getMinNumAdult() ) - 2) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() )) < lowCharge)) )
                        {
                            lowCharge = (int)(result.getLong( "adult_two_charge" ) + result.getLong( "adult_add_charge" ) * (Integer.parseInt( frm.getMinNumAdult() ) - 2) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() ));
                        }
                    }
                    else
                    {
                        // 大人2人用の金額 + 子供金額 * 最低子供人数
                        frmSub.setAdultTwo( formatCur.format( result.getLong( "adult_two_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() ) ) );
                        charge = formatCur.format( result.getLong( "adult_two_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() ) );
                        if ( lowCharge == 0 ||
                                ((result.getLong( "adult_two_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() )) > 0 &&
                                (result.getLong( "adult_two_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() )) < lowCharge) )
                        {
                            lowCharge = (int)(result.getLong( "adult_two_charge" ) + result.getLong( "child_add_charge" ) * Integer.parseInt( frm.getMinNumChild() ));
                        }
                    }
                }

                if ( chargeInfo.trim().length() != 0 )
                {
                    chargeInfo = chargeInfo + " / ";
                }
                chargeInfo = chargeInfo + modeInfo + charge;
                frm.setChargeInfo( chargeInfo );

                // チェックイン
                if ( ciInfo.trim().length() != 0 )
                {
                    ciInfo = ciInfo + " / ";
                }
                ciTimeFrom = (String.format( "%1$06d", result.getInt( "ci_time_from" ) ));
                ciTimeTo = (String.format( "%1$06d", result.getInt( "ci_time_to" ) ));
                ciInfoFrom = ciTimeFrom.substring( 0, 2 ) + ":" + ciTimeFrom.substring( 2, 4 );
                frmSub.setCiTimeFromHH( ciTimeFrom.substring( 0, 2 ) );
                frmSub.setCiTimeFromMM( ciTimeFrom.substring( 2, 4 ) );
                frmSub.setCiTimeToHH( ciTimeTo.substring( 0, 2 ) );
                frmSub.setCiTimeToMM( ciTimeTo.substring( 2, 4 ) );
                ciInfoTo = ciTimeTo.substring( 0, 2 ) + ":" + ciTimeTo.substring( 2, 4 );
                ciInfo = ciInfo + modeInfo + ciInfoFrom + " ～ " + ciInfoTo;
                frm.setCiTime( ciInfo );

                // チェックアウト
                if ( coInfo.trim().length() != 0 )
                {
                    coInfo = coInfo + " / ";
                }
                coTime = (String.format( "%1$06d", result.getInt( "co_time" ) ));
                if ( result.getInt( "co_kind" ) == 0 )
                {
                    coTimeInfo = coTime.substring( 0, 2 ) + ":" + coTime.substring( 2, 4 );
                }
                else
                {
                    coTimeInfo = "INから" + DateEdit.formatTime( 6, result.getInt( "co_time" ) );
                }
                frmSub.setCoTimeHH( coTime.substring( 0, 2 ) );
                frmSub.setCoTimeMM( coTime.substring( 2, 4 ) );
                coInfo = coInfo + modeInfo + coTimeInfo;
                frm.setCoTime( coInfo );

                frmSub.setChargeModeId( result.getInt( "charge_mode_id" ) );
                frmPlanChargeSubList.add( frmSub );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setChargeMsg( Message.getMessage( "erro.30001", "料金情報" ) );
                return;
            }
            frm.setLowAdultTwoCharge( formatCur.format( lowCharge ) );
            frm.setFrmPlanChargeSubList( frmPlanChargeSubList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getPlanCharge] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 下書きプランIDのリスト取得
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    public void getDraftPlanIdList() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        ArrayList<Integer> planIdList = new ArrayList<Integer>();

        query = query + "SELECT plan_id FROM hh_rsv_plan_draft ";
        query = query + "WHERE id = ? ";
        query = query + "ORDER BY disp_index, plan_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                planIdList.add( result.getInt( "plan_id" ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setDraftErrMsg( Message.getMessage( "erro.30001", "登録されている下書きプラン" ) );
                return;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getDraftPlanIdList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        frm.setDraftPlanIdList( planIdList );
    }

    /**
     * 下書きプラン名のリスト取得
     * 
     * @param なし
     * @return なし
     * @throws Exception
     */
    public void getDraftPlanNmList() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ArrayList<String> planNmList = new ArrayList<String>();

        query = query + "SELECT plan_name FROM hh_rsv_plan_draft ";
        query = query + "WHERE id = ? ";
        query = query + "ORDER BY disp_index, plan_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelID() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                planNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getDraftPlanNmList] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        frm.setDraftPlanNmList( planNmList );
    }

    /**
     * 下書きプラン名取得
     * 
     * @param id ホテルID
     * @param planId プランID
     * @return 該当するプラン名
     */
    public String getDraftPlanNm(int id, int planId) throws Exception
    {
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String planNm = "";

        query = query + "SELECT plan_name FROM hh_rsv_plan_draft ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                planNm = result.getString( "plan_name" );
            }

            return planNm;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getDraftPlanNm] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 下書きプラン削除
     * 
     * @param id ホテルID
     * @param planId プランID
     * @return true:正常、false:失敗
     */
    public void execDelDraftPlan(int id, int planId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        try
        {
            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            // 下書きプランデータ削除
            ret = delDraftPlan( prestate, connection, id, planId );

            // 下書きプラン・部屋データ削除
            if ( ret == true )
            {
                ret = delDraftPlanRoom( prestate, connection, id, planId );
            }

            // 下書きオプションデータ登録
            if ( ret == true )
            {
                ret = delDraftPlanOption( prestate, connection, id, planId );
            }

            if ( ret )
            {
                query = "COMMIT ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }
            else
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }
        }
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            Logging.error( "[LogicOwnerRsvPlan.execDelDraftPlan] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 下書きプラン削除
     * 
     * @param prestate PreparedStatementオブジェクト
     * @param conn Connectionオブジェクト
     * @param id ホテルID
     * @param planId プランID
     * @return true:正常、false:失敗
     */
    private boolean delDraftPlan(PreparedStatement prestate, Connection conn, int hotelId, int planId) throws Exception
    {
        String query = "";
        boolean ret = false;

        query = query + "DELETE FROM hh_rsv_plan_draft ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            prestate.executeUpdate();

            ret = true;
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerRsvPlan.delDraftPlan] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 下書きプラン部屋削除
     * 
     * @param prestate PreparedStatementオブジェクト
     * @param conn Connectionオブジェクト
     * @param id ホテルID
     * @param planId プランID
     * @return true:正常、false:失敗
     */
    private boolean delDraftPlanRoom(PreparedStatement prestate, Connection conn, int hotelId, int planId) throws Exception
    {
        String query = "";
        boolean ret = false;

        query = query + "DELETE FROM hh_rsv_rel_plan_room_draft ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            prestate.executeUpdate();

            ret = true;
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerRsvPlan.delDraftPlanRoom] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 下書きオプションデータ削除
     * 
     * @param prestate PreparedStatementオブジェクト
     * @param conn Connectionオブジェクト
     * @param id ホテルID
     * @param planId プランID
     * @return true:正常、false:失敗
     */
    private boolean delDraftPlanOption(PreparedStatement prestate, Connection conn, int hotelId, int planId) throws Exception
    {
        String query = "";
        boolean ret = false;

        query = query + "DELETE FROM hh_rsv_rel_plan_option_draft ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, planId );
            prestate.executeUpdate();

            ret = true;
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[LogicOwnerRsvPlan.delDraftPlanOption] Exception=" + e.toString() );
            throw new Exception( e );
        }

        return(ret);
    }

    /**
     * 下書きからプラン設定更新の場合のプラン・部屋データ登録処理
     * 
     * @param なし
     * @return なし
     */
    public void registDraftPlan() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        try
        {
            connection = DBConnection.getConnection( false );
            query = "START TRANSACTION ";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            // プランデータ登録
            ret = execRegistPlan( prestate, connection, 2, 0, OwnerRsvCommon.BTN_REGIST );

            // プラン・部屋データ登録
            if ( ret == true )
            {
                ret = execRegistPlanRoom( prestate, connection, 2, 0, OwnerRsvCommon.BTN_REGIST );
            }

            // オプションデータ登録
            if ( ret == true )
            {
                ret = execRegistOption( prestate, connection, 2, 0, OwnerRsvCommon.BTN_REGIST );
            }

            if ( ret )
            {
                query = "COMMIT ";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }
            else
            {
                query = "ROLLBACK";
                prestate = connection.prepareStatement( query );
                result = prestate.executeQuery();
            }
        }
        catch ( Exception e )
        {
            query = "ROLLBACK";
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();
            Logging.error( "[LogicOwnerRsvPlan.registDraftPlan] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 予約データのチェック
     * 
     * @param id
     * @param planId
     * @return
     */
    public boolean existReserve(int id, int planId)
    {
        boolean ret = false;
        int count = 0;
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = query + "SELECT count(id) FROM hh_rsv_reserve ";
        query = query + " WHERE status = 1 AND id = ? ";
        query = query + " AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            while( result.next() )
            {
                count = result.getInt( 1 );
            }

            if ( count > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.existReserve] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;

    }

    /**
     * 予約データの非掲載
     * 
     * @param id
     * @param planId
     * @return
     */
    public boolean noPublishPlan(int id, int planId)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        int result = 0;

        query = query + "UPDATE hh_rsv_plan ";
        query = query + " SET publishing_flag = 0 ";
        query = query + " WHERE id = ? ";
        query = query + " AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            result = prestate.executeUpdate();

            if ( result >= 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.noPublishPlan] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return ret;

    }

    /**
     * 予約データの削除
     * 
     * @param id
     * @param planId
     * @return
     */
    public boolean delPublishPlan(int id, int planId)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;
        int result = 0;

        query = query + "UPDATE hh_rsv_plan ";
        query = query + " SET publishing_flag = -1 ";
        query = query + " WHERE id = ? ";
        query = query + " AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            result = prestate.executeUpdate();

            if ( result >= 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.delPublishPlan] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return ret;

    }

}
