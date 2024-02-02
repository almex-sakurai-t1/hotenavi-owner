package jp.happyhotel.owner;

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
import jp.happyhotel.common.Message;
import jp.happyhotel.common.ReplaceString;

/**
 * プラン管理画面ビジネスロジッククラス
 */
public class LogicOwnerRsvPlanManage implements Serializable
{
    private static final long      serialVersionUID = -2217112628878640483L;
    private FormOwnerRsvPlanManage frm;
    private static final String    dispView_ON      = "掲載";
    private static final String    dispView_OFF     = "非掲載";

    /* フォームオブジェクト */
    public FormOwnerRsvPlanManage getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvPlanManage frm)
    {
        this.frm = frm;
    }

    private int maxnumAdult = 0;
    private int minnumAdult = 0;
    private int maxnumChild = 0;
    private int minnumChild = 0;

    /**
     * プラン情報取得
     * 
     * @param viewKbn 0:非掲載も表示する、1:非掲載は表示しない
     * @return なし
     */
    public void getPlan(int viewKbn) throws Exception
    {
        int planId = 0;
        int relPlanId = 0;
        String optionNm = "";
        ArrayList<String> optNmList = new ArrayList<String>();

        // プラン・部屋情報取得
        getPlanRoom( viewKbn );

        // 料金情報取得
        getPlanCharge();

        // データマージ
        ArrayList<Integer> expPlanIdList = new ArrayList<Integer>();
        ArrayList<String> expList = new ArrayList<String>();
        ArrayList<String> ciInfoList = new ArrayList<String>();
        ArrayList<String> coInfoList = new ArrayList<String>();
        ArrayList<String> expInfoList = new ArrayList<String>();
        ArrayList<String> maxnumAdultList = new ArrayList<String>();
        ArrayList<String> maxnumChildList = new ArrayList<String>();
        ArrayList<String> minnumAdultList = new ArrayList<String>();
        ArrayList<String> minnumChildList = new ArrayList<String>();
        ArrayList<String> dispStatusMessage = new ArrayList<String>();
        boolean setFlg = false;
        for( int i = 0 ; i < this.frm.getPlanId().size() ; i++ )
        {
            planId = this.frm.getPlanId().get( i );
            setFlg = false;

            for( int j = 0 ; j < this.frm.getExPlanId().size() ; j++ )
            {
                relPlanId = this.frm.getExPlanId().get( j );
                if ( planId == relPlanId )
                {
                    setFlg = true;
                    expPlanIdList.add( relPlanId );
                    expList.add( this.frm.getExpense().get( j ) );
                    ciInfoList.add( this.frm.getCiInfo().get( j ) );
                    coInfoList.add( this.frm.getCoInfo().get( j ) );
                    expInfoList.add( this.frm.getExpenseInfo().get( j ) );
                    maxnumAdultList.add( this.frm.getMaxNumAdult().get( j ) );
                    maxnumChildList.add( this.frm.getMaxNumChild().get( j ) );
                    minnumAdultList.add( this.frm.getMinNumAdult().get( j ) );
                    minnumChildList.add( this.frm.getMinNumChild().get( j ) );
                    dispStatusMessage.add( this.getDayCharge( relPlanId ) );
                    break;
                }
            }
            if ( setFlg == false )
            {
                expPlanIdList.add( null );
                maxnumAdultList.add( null );
                maxnumChildList.add( null );
                minnumAdultList.add( null );
                minnumChildList.add( null );
                expList.add( "" );
                ciInfoList.add( "" );
                coInfoList.add( "" );
                expInfoList.add( "料金情報が登録されていません。" );
                dispStatusMessage.add( "料金情報が登録されていません。" );
            }
        }

        for( int i = 0 ; i < this.frm.getPlanId().size() ; i++ )
        {
            optionNm = "";
            optionNm = getOption( this.frm.getPlanId().get( i ) );
            optNmList.add( optionNm );
        }

        frm.setExPlanId( expPlanIdList );
        frm.setExpense( expList );
        frm.setCiInfo( ciInfoList );
        frm.setCoInfo( coInfoList );
        frm.setExpenseInfo( expInfoList );
        frm.setMaxNumAdult( maxnumAdultList );
        frm.setMaxNumChild( maxnumChildList );
        frm.setMinNumAdult( minnumAdultList );
        frm.setMinNumChild( minnumChildList );
        frm.setOption( optNmList );
        frm.setDispStatus( dispStatusMessage );
    }

    /**
     * プラン・部屋情報取得
     * 
     * @param viewKbn 0:非掲載も表示する、1:非掲載は表示しない
     * @return なし
     */
    public void getPlanRoom(int viewKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String roomSeq = "";
        String dispIdx = "";
        int orgPlanId = 0;
        int newPlanId = 0;
        ArrayList<Integer> planIdList = new ArrayList<Integer>();
        ArrayList<String> planNmList = new ArrayList<String>();
        ArrayList<String> dispIdxList = new ArrayList<String>();
        ArrayList<String> dispViewList = new ArrayList<String>();
        // ArrayList<Integer> seqList = new ArrayList<Integer>();
        ArrayList<String> seqList = new ArrayList<String>();
        ArrayList<String> roomSeqList = new ArrayList<String>();
        ArrayList<Integer> salesFlgList = new ArrayList<Integer>();
        ArrayList<Integer> newPlanIdList = new ArrayList<Integer>();
        ArrayList<String> newPlanNmList = new ArrayList<String>();
        ArrayList<String> newDispIdxList = new ArrayList<String>();
        ArrayList<String> newDispViewList = new ArrayList<String>();
        ArrayList<Integer> newSalesFlgList = new ArrayList<Integer>();
        ArrayList<String> lastUpdateList = new ArrayList<String>();
        ArrayList<String> newLastUpdateList = new ArrayList<String>();
        ArrayList<Integer> salesStartDateList = new ArrayList<Integer>();
        ArrayList<Integer> salesEndDateList = new ArrayList<Integer>();
        ArrayList<Integer> dispStartDateList = new ArrayList<Integer>();
        ArrayList<Integer> dispEndDateList = new ArrayList<Integer>();

        ArrayList<Integer> newSalesStartDateList = new ArrayList<Integer>();
        ArrayList<Integer> newSalesEndDateList = new ArrayList<Integer>();
        ArrayList<Integer> newDispStartDateList = new ArrayList<Integer>();
        ArrayList<Integer> newDispEndDateList = new ArrayList<Integer>();

        query = query + "SELECT ";
        query = query + "pl.plan_id,pl.plan_name,room.seq, pl.disp_index, pl.publishing_flag, pl.sales_flag, ";
        query = query + "pl.disp_start_date, pl.disp_end_date,pl.last_update, pl.last_uptime, pl.sales_start_date, pl.sales_end_date ";
        query = query + "FROM hh_rsv_plan pl ";
        query = query + "  LEFT JOIN hh_rsv_rel_plan_room room ON pl.id = room.id AND pl.plan_id = room.plan_id ";
        query = query + "WHERE pl.id = ? ";
        if ( viewKbn == 1 )
        {
            // 非掲載は表示しない
            query = query + " AND pl.publishing_flag = 1 ";
            // 掲載期間が終了したものは表示しない
            query = query + " AND pl.disp_end_date >= " + DateEdit.getDate( 2 );
        }
        else
        {
            // 削除分は表示しない
            query = query + " AND pl.publishing_flag <>  -1 ";
        }

        query = query + " ORDER BY pl.disp_index, pl.plan_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();
            while( result.next() )
            {
                planIdList.add( result.getInt( "plan_id" ) );
                planNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
                lastUpdateList.add( result.getString( "last_update" ).substring( 0, 4 ) + "/" + result.getString( "last_update" ).substring( 4, 6 ) + "/" + result.getString( "last_update" ).substring( 6 ) +
                        " " + ConvertTime.convTimeHH( result.getInt( "last_uptime" ) ) + ":" + ConvertTime.convTimeMM( result.getInt( "last_uptime" ) ) );
                dispIdx = result.getString( "disp_index" );
                dispIdxList.add( dispIdx );
                if ( result.getInt( "publishing_flag" ) == 0 )
                {
                    dispViewList.add( dispView_OFF );
                }
                else
                {
                    dispViewList.add( dispView_ON );
                }
                if ( result.getString( "seq" ) == null )
                {
                    seqList.add( "" );
                }
                else
                {
                    seqList.add( result.getString( "seq" ) );
                }

                salesFlgList.add( result.getInt( "sales_flag" ) );
                salesStartDateList.add( result.getInt( "sales_start_date" ) );
                salesEndDateList.add( result.getInt( "sales_end_date" ) );
                dispStartDateList.add( result.getInt( "disp_start_date" ) );
                dispEndDateList.add( result.getInt( "disp_end_date" ) );

            }

            if ( planIdList.size() == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "プラン情報" ) );
                return;
            }
            // プラン情報の整形
            newPlanId = planIdList.get( 0 );
            newPlanIdList.add( newPlanId );
            newPlanNmList.add( planNmList.get( 0 ) );
            newDispIdxList.add( dispIdxList.get( 0 ) );
            newDispViewList.add( dispViewList.get( 0 ) );
            newSalesFlgList.add( salesFlgList.get( 0 ) );
            newLastUpdateList.add( lastUpdateList.get( 0 ) );
            newSalesStartDateList.add( salesStartDateList.get( 0 ) );
            newSalesEndDateList.add( salesEndDateList.get( 0 ) );
            newDispStartDateList.add( dispStartDateList.get( 0 ) );
            newDispEndDateList.add( dispEndDateList.get( 0 ) );

            for( int i = 0 ; i < seqList.size() ; i++ )
            {
                orgPlanId = planIdList.get( i );

                if ( orgPlanId == newPlanId )
                {
                    if ( roomSeq.trim().length() > 0 )
                    {
                        roomSeq = roomSeq + " ";
                    }
                    roomSeq = roomSeq + seqList.get( i );

                }
                else
                {
                    newPlanIdList.add( orgPlanId );
                    newPlanNmList.add( planNmList.get( i ) );
                    newDispIdxList.add( dispIdxList.get( i ) );
                    newDispViewList.add( dispViewList.get( i ) );
                    newSalesFlgList.add( salesFlgList.get( i ) );
                    newLastUpdateList.add( lastUpdateList.get( i ) );
                    roomSeqList.add( roomSeq );
                    roomSeq = "";
                    roomSeq = roomSeq + seqList.get( i );
                    newSalesStartDateList.add( salesStartDateList.get( i ) );
                    newSalesEndDateList.add( salesEndDateList.get( i ) );
                    newDispStartDateList.add( dispStartDateList.get( i ) );
                    newDispEndDateList.add( dispEndDateList.get( i ) );
                }
                newPlanId = planIdList.get( i );
            }
            roomSeqList.add( roomSeq );

            // Formにセット
            frm.setPlanId( newPlanIdList );
            frm.setPlanNm( newPlanNmList );
            frm.setDispIdx( newDispIdxList );
            frm.setDispNm( newDispViewList );
            frm.setRoomSeqList( roomSeqList );
            frm.setSalesFlgList( newSalesFlgList );
            frm.setLastUpdateTime( newLastUpdateList );
            frm.setSalesStartDate( newSalesStartDateList );
            frm.setSalesEndDate( newSalesEndDateList );
            frm.setDispStartDate( newDispStartDateList );
            frm.setDispEndDate( newDispEndDateList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanManage.getPlanRoom] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 大人・子供の最大・最少人数の取得
     * 
     * @param planid プランID
     * @return なし
     */
    private void getMaxMinNumAdultChild(int planid) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            query = "select max_num_adult, max_num_child, min_num_adult, min_num_child from hh_rsv_plan where " +
                    " id = ? AND plan_id = ?";
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, planid );
            result = prestate.executeQuery();

            if ( result.next() != false )
            {
                this.maxnumAdult = result.getInt( "max_num_adult" );
                this.maxnumChild = result.getInt( "max_num_child" );
                this.minnumAdult = result.getInt( "min_num_adult" );
                this.minnumChild = result.getInt( "min_num_child" );
            }
            else
            {
                this.maxnumAdult = 0;
                this.maxnumChild = 0;
                this.minnumAdult = 0;
                this.minnumChild = 0;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanManage.getMaxMinNumAdultChild] Exception=" + e.toString() );
            throw new Exception( e );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
            DBConnection.releaseResources( result );
        }

        return;
    }

    /**
     * 料金情報取得
     * 
     * @param なし
     * @return なし
     */
    public void getPlanCharge() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
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
        int orgPlanId = 0;
        int newPlanId = 0;
        int roopCnt = 0;
        ArrayList<Integer> planIdList = new ArrayList<Integer>();
        ArrayList<String> expList = new ArrayList<String>();
        ArrayList<String> ciInfoList = new ArrayList<String>();
        ArrayList<String> coInfoList = new ArrayList<String>();
        ArrayList<String> expInfoList = new ArrayList<String>();
        ArrayList<String> maxnumAdultList = new ArrayList<String>();
        ArrayList<String> maxnumChildList = new ArrayList<String>();
        ArrayList<String> minnumAdultList = new ArrayList<String>();
        ArrayList<String> minnumChildList = new ArrayList<String>();
        NumberFormat formatCur = NumberFormat.getCurrencyInstance();
        ArrayList<FormOwnerRsvPlanChargeSub> frmPlanChargeSubList = new ArrayList<FormOwnerRsvPlanChargeSub>();

        query = query + "SELECT ";
        query = query + "ch.plan_id, cm.charge_mode_name, ch.ci_time_from, ch.ci_time_to, ch.co_time, ch.adult_two_charge, ";
        query = query + "ch.adult_one_charge, ch.adult_add_charge, ch.child_add_charge, ch.co_kind ";
        query = query + "FROM hh_rsv_plan_charge ch ";
        query = query + "  LEFT JOIN hh_rsv_charge_mode cm ON ch.id = cm.id AND ch.charge_mode_id = cm.charge_mode_id ";
        query = query + "WHERE ch.id = ? ";
        query = query + "ORDER BY ch.plan_id, cm.disp_index";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                FormOwnerRsvPlanChargeSub frmSub = new FormOwnerRsvPlanChargeSub();
                orgPlanId = result.getInt( "plan_id" );

                if ( roopCnt == 0 )
                {
                    newPlanId = result.getInt( "plan_id" );
                }

                if ( orgPlanId != newPlanId )
                {
                    // 対象プランの大人子供の最大最少人数取得
                    maxnumAdultList.add( String.valueOf( this.maxnumAdult ) );
                    maxnumChildList.add( String.valueOf( this.maxnumChild ) );
                    minnumAdultList.add( String.valueOf( this.minnumAdult ) );
                    minnumChildList.add( String.valueOf( this.minnumChild ) );
                    // リストにセット
                    planIdList.add( newPlanId );
                    expList.add( chargeInfo );
                    ciInfoList.add( ciInfo );
                    coInfoList.add( coInfo );
                    if ( chargeInfo.trim().length() == 0 )
                    {
                        expInfoList.add( Message.getMessage( "erro.30001", "料金情報" ) );
                    }
                    else
                    {
                        expInfoList.add( "" );
                    }

                    // 値の初期化
                    modeInfo = "";
                    chargeInfo = "";
                    ciInfo = "";
                    coInfo = "";
                }
                // 対象プランの大人子供の最大最少人数取得
                getMaxMinNumAdultChild( orgPlanId );

                // 日付情報
                modeInfo = "";
                modeInfo = modeInfo + " (" + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "charge_mode_name" ) ) ) + ") ";

                // 料金
                if ( this.maxnumAdult < 2 )
                {
                    // 大人1人用の金額 + 子供金額 * 最低子供人数
                    frmSub.setAdultTwo( formatCur.format( (result.getLong( "adult_one_charge" ) + result.getLong( "child_add_charge" ) * this.minnumChild) ) );
                    charge = formatCur.format( (result.getLong( "adult_one_charge" ) + result.getLong( "child_add_charge" ) * this.minnumChild) );
                }
                else if ( this.minnumAdult > 2 )
                {
                    // 大人2人用の金額 + 大人追加金額 * オーバー人数 + 子供金額 * 最低子供人数
                    frmSub.setAdultTwo( formatCur.format( result.getLong( "adult_two_charge" ) + result.getLong( "adult_add_charge" ) * (this.minnumAdult - 2)
                            + result.getLong( "child_add_charge" ) * this.minnumChild ) );
                    charge = formatCur.format( result.getLong( "adult_two_charge" ) + result.getLong( "adult_add_charge" ) * (this.minnumAdult - 2) + result.getLong( "child_add_charge" )
                            * this.minnumChild );
                }
                else
                {
                    // 大人2人用の金額 + 子供金額 * 最低子供人数
                    frmSub.setAdultTwo( formatCur.format( result.getLong( "adult_two_charge" ) + result.getLong( "child_add_charge" ) * this.minnumChild ) );
                    charge = formatCur.format( result.getLong( "adult_two_charge" ) + result.getLong( "child_add_charge" ) * this.minnumChild );
                }
                if ( chargeInfo.trim().length() != 0 )
                {
                    chargeInfo = chargeInfo + " / ";
                }
                chargeInfo = chargeInfo + modeInfo + charge;

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
                ciInfoTo = ciTimeTo.substring( 0, 2 ) + ":" + ciTimeTo.substring( 2, 4 );
                ciInfo = ciInfo + modeInfo + ciInfoFrom + " ～ " + ciInfoTo;

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
                roopCnt += 1;
                newPlanId = result.getInt( "plan_id" );

                frmPlanChargeSubList.add( frmSub );
            }

            planIdList.add( orgPlanId );
            expList.add( chargeInfo );
            ciInfoList.add( ciInfo );
            coInfoList.add( coInfo );
            maxnumAdultList.add( String.valueOf( this.maxnumAdult ) );
            maxnumChildList.add( String.valueOf( this.maxnumChild ) );
            minnumAdultList.add( String.valueOf( this.minnumAdult ) );
            minnumChildList.add( String.valueOf( this.minnumChild ) );
            if ( chargeInfo.trim().length() == 0 )
            {
                expInfoList.add( Message.getMessage( "erro.30001", "料金情報" ) );
            }
            else
            {
                expInfoList.add( "" );
            }

            // フォームに設定
            frm.setExPlanId( planIdList );
            frm.setExpense( expList );
            frm.setCiInfo( ciInfoList );
            frm.setCoInfo( coInfoList );
            frm.setExpenseInfo( expInfoList );
            frm.setMaxNumAdult( maxnumAdultList );
            frm.setMaxNumChild( maxnumChildList );
            frm.setMinNumAdult( minnumAdultList );
            frm.setMinNumChild( minnumChildList );

            frm.setFrmPlanChargeSubList( frmPlanChargeSubList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlan.getPlanCharge] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
            DBConnection.releaseResources( result );
        }
    }

    /**
     * 表示順更新
     * 
     * @param hotelId ホテルID
     * @param planId 対象のプランID
     * @param dispIdx 表示順
     * @param userId ユーザID
     * @return true:正常、false:失敗
     */
    public boolean changeDispIdx(int hotelId, int planId, int dispIdx, int userId) throws Exception
    {

        String query = "";
        int result = 0;
        Connection connection = null;
        PreparedStatement prestate = null;
        boolean ret = false;

        query = query + "UPDATE hh_rsv_plan SET ";
        query = query + "disp_index = ?, ";
        query = query + "user_id = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ?  ";
        query = query + "WHERE id = ? ";
        query = query + "  AND plan_id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, dispIdx );
            prestate.setInt( 2, userId );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( 4, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( 5, hotelId );
            prestate.setInt( 6, planId );
            result = prestate.executeUpdate();

            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanManage.changeDispIdx] Exception=" + e.toString() );
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
     * オプション情報取得
     * 
     * @param planId プランID
     * @return なし
     */
    private String getOption(int planId) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String optionNm = "";

        query = query + "SELECT DISTINCT op.option_name ";
        query = query + "FROM hh_rsv_rel_plan_option rel ";
        query = query + "  LEFT JOIN hh_rsv_option op ON rel.id = op.id AND rel.option_id = op.option_id AND rel.option_sub_id = op.option_sub_id ";
        query = query + "WHERE rel.id = ? ";
        query = query + " AND rel.plan_id = ? ";
        query = query + "ORDER BY op.option_flag desc, rel.option_id, rel.option_sub_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();
            while( result.next() )
            {
                if ( optionNm.trim().length() != 0 )
                {
                    optionNm = optionNm + "、";
                }
                optionNm = optionNm + ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "option_name" ) ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanManage.getOption] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(optionNm);
    }

    private String getDayCharge(int planId) throws Exception
    {
        boolean retDayCharge = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String returnMsg = "";

        query = query + "SELECT DISTINCT day.*";
        query = query + "FROM hh_rsv_day_charge day ";
        query = query + "  LEFT JOIN hh_rsv_plan plan ON plan.plan_id = day.plan_id ";
        query = query + "WHERE day.id = ? ";
        query = query + " AND day.plan_id = ? ";
        // 開始日は今日以降
        query = query + " AND day.cal_date >= ? ";
        // 一致するプランIDの販売終了日より

        query = query + " AND day.cal_date <= plan.sales_end_date";
        query = query + " ORDER BY day.plan_id, day.cal_date ";
        query = query + " LIMIT 0,1";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    retDayCharge = true;
                }
            }
            if ( retDayCharge != false )
            {
                returnMsg = "";
            }
            else
            {
                returnMsg = "日別料金反映待ち";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanManage.getOption] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(returnMsg);
    }

}
