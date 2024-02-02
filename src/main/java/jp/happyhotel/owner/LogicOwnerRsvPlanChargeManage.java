package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.ConvertTime;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;

/**
 * プラン別料金管理画面ビジネスロジッククラス
 */
public class LogicOwnerRsvPlanChargeManage implements Serializable
{

    private static final long            serialVersionUID = 7682135060820907314L;
    private static final String          PUBLISHING_ON    = "掲載";
    private static final String          PUBLISHING_OFF   = "未掲載";
    private static final String          SALES_ON         = "予約受付中";
    private static final String          SALES_OFF        = "予約停止中";
    private FormOwnerRsvPlanChargeManage frm;

    /* フォームオブジェクト */
    public FormOwnerRsvPlanChargeManage getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerRsvPlanChargeManage frm)
    {
        this.frm = frm;
    }

    /**
     * 料金モードデータが存在するか
     * 
     * @param id ホテルID
     * @return true：存在する、False：存在しない
     */
    public boolean existsChargeMode(int id) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int cnt = 0;
        boolean ret = false;

        query = query + "SELECT COUNT(*) AS CNT FROM hh_rsv_charge_mode ";
        query = query + "WHERE id = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            result = prestate.executeQuery();

            while( result.next() )
            {
                cnt = result.getInt( "CNT" );
            }

            if ( cnt == 0 )
            {
                return ret;
            }

            ret = true;
            return ret;

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeManage.existsChargeMode] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * プラン別料金データ取得
     * 
     * @param なし
     * @return なし
     */
    public void getPlanChargeData(int viewKbn) throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String adultTwoCharge = "";
        String adultOneCharge = "";
        String adultAddCharge = "";
        String childAddCharge = "";
        int publishingFlag = 0;
        int salesFlag = 0;
        ArrayList<Integer> planIdList = new ArrayList<Integer>();
        ArrayList<String> planNmList = new ArrayList<String>();
        ArrayList<String> publishingNmList = new ArrayList<String>();
        ArrayList<String> salesNmList = new ArrayList<String>();
        ArrayList<Integer> chargeModeIdList = new ArrayList<Integer>();
        ArrayList<String> chargeModeNmList = new ArrayList<String>();
        ArrayList<String> ciTimeFromList = new ArrayList<String>();
        ArrayList<String> ciTimeToList = new ArrayList<String>();
        ArrayList<String> coTimeList = new ArrayList<String>();
        ArrayList<String> coRemarksList = new ArrayList<String>();
        ArrayList<String> adultTwoList = new ArrayList<String>();
        ArrayList<String> adultOneList = new ArrayList<String>();
        ArrayList<String> adultAddList = new ArrayList<String>();
        ArrayList<String> childList = new ArrayList<String>();
        ArrayList<String> remarksList = new ArrayList<String>();
        ArrayList<Integer> dispStartDateList = new ArrayList<Integer>();
        ArrayList<Integer> dispEndDateList = new ArrayList<Integer>();
        ArrayList<Integer> salesStartDateList = new ArrayList<Integer>();
        ArrayList<Integer> salesEndDateList = new ArrayList<Integer>();
        NumberFormat formatCur = NumberFormat.getCurrencyInstance();
        String coTime = "";

        query = query + "SELECT ";
        query = query + "pl.plan_id, pl.plan_name, pl.publishing_flag,pl.sales_flag,";
        query = query + "pl.disp_start_date, pl.disp_end_date, pl.sales_start_date,pl.sales_end_date,";
        query = query + "cm.charge_mode_id, cm.charge_mode_name, ";
        query = query + "pc.ci_time_from, pc.ci_time_to, pc.co_time, pc.co_remarks, pc.adult_two_charge, ";
        query = query + "pc.adult_one_charge, pc.adult_add_charge, pc.child_add_charge, pc.remarks, pc.co_kind ";
        query = query + "FROM hh_rsv_plan pl ";
        query = query + "  LEFT JOIN hh_rsv_plan_charge pc ON pl.id = pc.id AND pl.plan_id = pc.plan_id ";
        query = query + "    LEFT JOIN hh_rsv_charge_mode cm ON pc.id = cm.id AND pc.charge_mode_id = cm.charge_mode_id ";
        query = query + "WHERE pl.id = ?";
        if ( viewKbn == 1 )
        {
            // 非掲載は表示しない
            query = query + " AND pl.publishing_flag = 1 ";
            // 掲載期間が終了したものは表示しない
            query = query + " AND pl.disp_end_date >= " + DateEdit.getDate( 2 );
        }
        else
        {
            query = query + " AND pl.publishing_flag = 1 ";
        }
        query = query + " ORDER BY pl.disp_index, pl.plan_id, cm.disp_index";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getSelHotelId() );
            result = prestate.executeQuery();

            while( result.next() )
            {
                adultTwoCharge = formatCur.format( result.getLong( "adult_two_charge" ) );
                adultOneCharge = formatCur.format( result.getLong( "adult_one_charge" ) );
                adultAddCharge = formatCur.format( result.getLong( "adult_add_charge" ) );
                childAddCharge = formatCur.format( result.getLong( "child_add_charge" ) );
                planIdList.add( result.getInt( "plan_id" ) );
                planNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
                publishingFlag = result.getInt( "publishing_flag" );
                salesFlag = result.getInt( "sales_flag" );
                if ( publishingFlag == 1 )
                {
                    publishingNmList.add( PUBLISHING_ON );
                }
                else
                {
                    publishingNmList.add( PUBLISHING_OFF );
                }
                if ( salesFlag == 1 )
                {
                    salesNmList.add( SALES_ON );
                }
                else
                {
                    salesNmList.add( SALES_OFF );
                }
                dispStartDateList.add( result.getInt( "disp_start_date" ) );
                dispEndDateList.add( result.getInt( "disp_end_date" ) );
                salesStartDateList.add( result.getInt( "sales_start_date" ) );
                salesEndDateList.add( result.getInt( "sales_end_date" ) );

                chargeModeIdList.add( result.getInt( "charge_mode_id" ) );
                if ( result.getString( "charge_mode_name" ) == null )
                {
                    chargeModeNmList.add( "料金プランが登録されていません。" );
                }
                else
                {
                    chargeModeNmList.add( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "charge_mode_name" ) ) ) );
                }
                ciTimeFromList.add( ConvertTime.convTimeHHMM( result.getInt( "ci_time_from" ), 0 ) );
                ciTimeToList.add( ConvertTime.convTimeHHMM( result.getInt( "ci_time_to" ), 0 ) );
                if ( result.getInt( "co_kind" ) == 0 )
                {
                    coTime = ConvertTime.convTimeHHMM( result.getInt( "co_time" ), 0 );
                    coTimeList.add( coTime );
                }
                else
                {
                    coTime = "INから" + DateEdit.formatTime( 6, result.getInt( "co_time" ) );
                    coTimeList.add( coTime );
                }
                coRemarksList.add( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "co_remarks" ) ) ) );
                adultTwoList.add( adultTwoCharge );
                adultOneList.add( adultOneCharge );
                adultAddList.add( adultAddCharge );
                childList.add( childAddCharge );
                remarksList.add( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "remarks" ) ) ) );
            }
            // フォームにセット
            frm.setPlanIdList( planIdList );
            frm.setPlanNmList( planNmList );
            frm.setPublishingNmList( publishingNmList );
            frm.setSalesNmList( salesNmList );
            frm.setChargeModeIdList( chargeModeIdList );
            frm.setChargeModeNmList( chargeModeNmList );
            frm.setCiTimeFromList( ciTimeFromList );
            frm.setCiTimeToList( ciTimeToList );
            frm.setCoTimeList( coTimeList );
            frm.setCoRemarksList( coRemarksList );
            frm.setAdultTwoList( adultTwoList );
            frm.setAdultOneList( adultOneList );
            frm.setAdultAddList( adultAddList );
            frm.setChildList( childList );
            frm.setRemarksList( remarksList );
            frm.setSalesStartDateList( salesStartDateList );
            frm.setSalesEndDateList( salesEndDateList );
            frm.setDispStartDateList( dispStartDateList );
            frm.setDispEndDateList( dispEndDateList );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerRsvPlanChargeManage.getPlanChargeData] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }
}
