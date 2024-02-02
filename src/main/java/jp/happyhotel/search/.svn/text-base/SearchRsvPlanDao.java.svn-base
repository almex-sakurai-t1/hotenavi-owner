/*
 * @(#)SearchPlanDao.java 2011/02/17
 */

package jp.happyhotel.search;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.data.DataSearchPlan;
import jp.happyhotel.reserve.FormReservePersonalInfoPC;

/**
 * 
 * サーチプランDAOクラス
 * 
 * @author S.Tashiro
 * @version 1.0 2011/02/16
 */
public class SearchRsvPlanDao
{
    private final static int  KIND_WEEKDAY         = 1;
    private final static int  KIND_WEEKEND_BEFORE  = 2;
    private final static int  KIND_WEEKEND         = 3;
    private final static int  ADULT_TWO            = 2;
    private int               m_planCount;
    private int               m_planAllCount;
    private int               m_maxCharge;                // 最高料金
    private int               m_lowestCharge;             // 最低料金
    private int               m_charge;                   // 指定日　　　料金
    private String            m_ci;                       // 指定日　　　チェックイン
    private String            m_ciTo;                     // 指定日　チェックインTO
    private String            m_co;                       // 指定日　　　チェックアウト
    private ArrayList<String> m_chargeModeNameList = null; // 指定日なし料金ﾓｰﾄﾞ名称
    private ArrayList<String> m_ciList             = null; // 指定日なしﾁｪｯｸｲﾝFROM時刻リスト
    private ArrayList<String> m_ciToList           = null; // 指定日なしﾁｪｯｸｲﾝTO時刻リスト
    private ArrayList<String> m_coList             = null; // 指定日なしﾁｪｯｸｱｳﾄ時刻リスト
    private int               m_search_adult;
    private int               m_search_child;
    private int               m_roomAllCount;
    private int               m_roomCount;
    private String[]          m_roomName           = null;
    private int[]             m_roomSeq            = null;
    private String[]          m_roomText           = null;
    private String[]          m_roomEquip          = null;
    private String[]          m_roomPr             = null;
    private String[]          m_roomRemarks        = null;
    private String[]          m_roomImagePc        = null;
    private String[]          m_roomImageGif       = null;
    private String[]          m_roomImagePng       = null;
    private DataSearchPlan[]  m_planInfo           = null;

    /**
     * コンストラクタ
     */
    public SearchRsvPlanDao()
    {
        this.m_planCount = 0;
        this.m_planAllCount = 0;
        this.m_lowestCharge = 999999;
        this.m_maxCharge = 0;
        this.m_charge = 0;
        this.m_ci = "";
        this.m_ciTo = "";
        this.m_ciList = new ArrayList<String>();
        this.m_ciToList = new ArrayList<String>();
        this.m_coList = new ArrayList<String>();
        this.m_chargeModeNameList = new ArrayList<String>();
        this.m_co = "";
        this.m_search_adult = 0;
        this.m_search_child = 0;
    }

    /**
     * プラン別料金用変数初期化
     */
    public void clearRsvPlanChargeData()
    {
        this.m_lowestCharge = 999999;
        this.m_maxCharge = 0;
        this.m_charge = 0;
        this.m_ci = "";
        this.m_ciTo = "";
        this.m_co = "";
        this.m_search_adult = 0;
        this.m_search_child = 0;
        this.m_ciList = new ArrayList<String>();
        this.m_ciToList = new ArrayList<String>();
        this.m_coList = new ArrayList<String>();
        this.m_chargeModeNameList = new ArrayList<String>();
    }

    /**
     * ホテル部屋用変数初期化
     */
    public void clearRsvPlanRoomData()
    {
        this.m_roomAllCount = 0;
        this.m_roomCount = 0;
        this.m_roomName = null;
        this.m_roomSeq = null;
        this.m_roomText = null;
        this.m_roomEquip = null;
        this.m_roomPr = null;
        this.m_roomRemarks = null;
        this.m_ciList = new ArrayList<String>();
        this.m_ciToList = new ArrayList<String>();
        this.m_coList = new ArrayList<String>();
        this.m_chargeModeNameList = new ArrayList<String>();
        this.m_roomImagePc = null;
        this.m_roomImageGif = null;
        this.m_roomImagePng = null;
    }

    /** プラン情報件数取得 **/
    public int getCount()
    {
        return(m_planCount);
    }

    /** プラン情報件数取得 **/
    public int getAllCount()
    {
        return(m_planAllCount);
    }

    public int getRoomCount()
    {
        return m_roomCount;
    }

    public int getRoomAllCount()
    {
        return m_roomAllCount;
    }

    /** 料金ﾓｰﾄﾞ名称リスト **/
    public ArrayList<String> getChargeModeNameList()
    {
        return m_chargeModeNameList;
    }

    /** ﾁｪｯｸｲﾝFROM時刻リスト **/
    public ArrayList<String> getCiList()
    {
        return m_ciList;
    }

    /** ﾁｪｯｸｲﾝTO時刻リスト **/
    public ArrayList<String> getCiToList()
    {
        return m_ciToList;
    }

    /** ﾁｪｯｸｱｳﾄ時刻リスト **/
    public ArrayList<String> getCoList()
    {
        return m_coList;
    }

    /** プラン情報取得 **/
    public DataSearchPlan[] getPlanInfo()
    {
        return(m_planInfo);
    }

    /** 最低料金 **/
    public int getLowstCharge()
    {
        return(m_lowestCharge);
    }

    /** 最高料金 **/
    public int getMaxCharge()
    {
        return(m_maxCharge);
    }

    /** 料金 **/
    public int getCharge()
    {
        return(m_charge);
    }

    /** チェックイン時刻 **/
    public String getCheckin()
    {
        return(m_ci);
    }

    /** チェックイン時刻TO **/
    public String getCheckinTo()
    {
        return(m_ciTo);
    }

    /** チェックアウト時刻 **/
    public String getCheckout()
    {
        return(m_co);
    }

    /**
     * 予約ハピー加盟店のホテルIDリストを取得
     * 
     * @param id ホテルID
     * @param planIdList ホテルIDリスト
     * @param adult 大人の人数
     * @param child 子どもの人数
     * @param startDate 開始日付
     * @param endDate 終了日付
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     */
    public void getPlanList(int id, int[] planIdList, int adult, int child, int startDate, int endDate, int countNum, int pageNum) throws Exception
    {
        int loop;
        int count;

        if ( planIdList != null )
        {
            m_planAllCount = planIdList.length;
            if ( m_planAllCount > 0 )
            {
                count = 0;
                // 任意のページ分のプランIDをセットする
                for( loop = countNum * pageNum ; loop < m_planAllCount ; loop++ )
                {
                    count++;
                    if ( count >= countNum && countNum != 0 )
                    {
                        break;
                    }
                }

                int hotelIds[] = new int[count];
                int start = countNum * pageNum;
                for( int k = 0 ; k < count ; k++ )
                {
                    hotelIds[k] = planIdList[start];
                    start++;
                }

                // 予約ハピー加盟店データ取得
                m_planInfo = this.getPlanListFromDataBase( id, hotelIds, adult, child, startDate, endDate );
            }
        }
    }

    /**
     * プランIDから部屋一覧を取得
     * 
     * @param id ホテルID
     * @param planIdList ホテルIDリスト
     * @param adult 大人の人数
     * @param child 子どもの人数
     * @param startDate 開始日付
     * @param endDate 終了日付
     * @param countNum 取得件数（0：全件 ※pageNum無視）
     * @param pageNum ページ番号（0～）
     */
    public void getRoomList(int id, int planId, int adult, int child, int startDate, int endDate, int countNum, int pageNum) throws Exception
    {
        int loop;
        int count;
        int start;

        int[] planIdList;
        if ( planId > 0 )
        {
            planIdList = new int[1];
            planIdList[0] = planId;

            // 予約ハピー加盟店データ取得
            this.m_planInfo = this.getPlanListFromDataBase( id, planIdList, adult, child, startDate, endDate );

            if ( this.m_planInfo[0] != null )
            {
                // プラン数は最大で1
                this.m_planCount = this.m_planInfo.length;
                this.m_planAllCount = this.m_planInfo.length;
                // 部屋数の取得
                this.m_roomAllCount = this.m_planInfo[0].getRoomAllCount();

                count = 0;
                // 任意のページ分の部屋情報をセットする
                for( loop = countNum * pageNum ; loop < this.m_roomAllCount ; loop++ )
                {
                    count++;
                    if ( count >= countNum && countNum != 0 )
                    {
                        break;
                    }
                }
                // 表示する部屋数をセット
                this.m_roomCount = count;

                // 表示する件数を取得
                // データの一時保管場所
                int[] seq = new int[count];
                String[] name = new String[count];
                String[] text = new String[count];
                String[] equip = new String[count];
                String[] pr = new String[count];
                String[] remarks = new String[count];
                String[] imagePc = new String[count];
                String[] imageGif = new String[count];
                String[] imagePng = new String[count];

                start = countNum * pageNum;
                // 必要部分だけ抜き出す。
                for( int k = 0 ; k < count ; k++ )
                {
                    name[k] = this.m_roomName[start];
                    seq[k] = this.m_roomSeq[start];
                    text[k] = this.m_roomText[start];
                    equip[k] = this.m_roomEquip[start];
                    pr[k] = this.m_roomPr[start];
                    remarks[k] = this.m_roomRemarks[start];
                    imagePc[k] = this.m_roomImagePc[start];
                    imageGif[k] = this.m_roomImageGif[start];
                    imagePng[k] = this.m_roomImagePng[start];
                    start++;
                }

                // 表示するデータのみをセット
                this.m_planInfo[0].setRoomName( name );
                this.m_planInfo[0].setRoomSeq( seq );
                this.m_planInfo[0].setRoomText( text );
                this.m_planInfo[0].setRoomEquip( equip );
                this.m_planInfo[0].setRoomRemarks( remarks );
                this.m_planInfo[0].setRoomPr( pr );
                this.m_planInfo[0].setRoomImagePc( imagePc );
                this.m_planInfo[0].setRoomImageGif( imageGif );
                this.m_planInfo[0].setRoomImagePng( imagePng );
                this.m_planInfo[0].setRoomCount( this.m_roomCount );
                this.m_planInfo[0].setRoomAllCount( this.m_roomAllCount );
            }
        }
    }

    /**
     * プランIDと管理番号から部屋の情報を取得
     * 
     * @param id ホテルID
     * @param planIdList ホテルIDリスト
     * @param adult 大人の人数
     * @param child 子どもの人数
     * @param startDate 開始日付
     * @param endDate 終了日付
     * @param seq 部屋管理番号
     */
    public void getRoomDetails(int id, int planId, int adult, int child, int startDate, int endDate, int seq) throws Exception
    {
        if ( planId > 0 )
        {
            m_planInfo = this.getRoomFromDataBase( id, planId, adult, child, startDate, endDate, seq );
        }
    }

    /***
     * プラン情報取得
     * 
     * @param id ホテルID
     * @param planIdList プランIDリスト
     * @param adult 大人の利用人数
     * @param child 子供の利用人数
     * @param startDate 開始日付
     * @param endDate 終了日付
     * @return
     * @throws Exception
     */
    public DataSearchPlan[] getPlanListFromDataBase(int id, int[] planIdList, int adult, int child, int startDate, int endDate) throws Exception
    {
        boolean ret = false;

        String strPlanIdList = "";
        String query;
        int planCount = planIdList.length;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        FormReservePersonalInfoPC frmInfoPC = null;
        ReserveCommon rsvcomm = null;
        SearchRsvPlan rsvplan = null;

        DataSearchPlan dataSearchPlan[] = new DataSearchPlan[planCount];
        rsvplan = new SearchRsvPlan();

        for( int i = 0 ; i < planCount ; i++ )
        {
            if ( i == 0 )
            {
                strPlanIdList = strPlanIdList + "?";
            }
            else
            {
                strPlanIdList = strPlanIdList + ", " + "?";
            }
        }

        // プラン名、プランPRを取得するSQL
        query = " SELECT HRP.id, HRP.plan_id, HRP.plan_name, HRP.plan_pr, HRP.remarks, HRP.offer_kind, HRP.image_pc, HRP.image_gif, HRP.image_png" +
                " FROM hh_rsv_plan HRP" +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRP.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1 )" +
                " WHERE HRP.id = ?" +
                " AND HRP.sales_flag = 1" +
                " AND HRP.publishing_flag = 1" +
                " AND HRP.plan_id IN(" + strPlanIdList + ") ";
        // プラン人数指定あり時は上限下限指定
        if ( adult > 0 && adult < rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            query += " AND HRP.min_num_adult <= " + adult + " AND HRP.max_num_adult >= " + adult;
        }
        // 大人の指定人数4人以上なら
        if ( adult == rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            // 指定人数以上固定で扱う
            query += " AND HRP.max_num_adult >= " + adult;
        }
        else if ( adult > rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            query += " AND HRP.max_num_adult >= " + adult;
        }
        // 子供の指定人数3人以上なら
        if ( child == rsvplan.SEARCH_CHILD_MAX_NUM )
        {
            // 指定人数以上固定で扱う
            query += " AND HRP.max_num_child >= " + child;
        }
        else
        {
            query += " AND HRP.min_num_child <= " + child + " AND HRP.max_num_child >= " + child;
        }

        query += " ORDER BY HRP.disp_index, HRP.plan_id DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, id );
            for( int i = 0 ; i < planCount ; i++ )
            {
                prestate.setInt( (i + 2), planIdList[i] );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.m_planCount = result.getRow();
                    result.beforeFirst();
                }

                for( int j = 0 ; result.next() ; j++ )
                {
                    dataSearchPlan[j] = new DataSearchPlan();
                    // プランデータをセット
                    dataSearchPlan[j].setId( result.getInt( "id" ) );
                    dataSearchPlan[j].setPlanId( result.getInt( "plan_id" ) );
                    dataSearchPlan[j].setPlanName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
                    dataSearchPlan[j].setPlanPr( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_pr" ) ) ) );
                    dataSearchPlan[j].setRemarks( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ) ) ) );
                    dataSearchPlan[j].setOfferkind( result.getInt( "offer_kind" ) );
                    dataSearchPlan[j].setPlanImagePc( result.getString( "image_pc" ) );
                    dataSearchPlan[j].setPlanImageGif( result.getString( "image_gif" ) );
                    dataSearchPlan[j].setPlanImagePng( result.getString( "image_png" ) );
                    // 予約の必要情報取得
                    frmInfoPC = new FormReservePersonalInfoPC();
                    frmInfoPC.setMode( ReserveCommon.MODE_INS );
                    frmInfoPC.setSelHotelID( result.getInt( "id" ) );
                    frmInfoPC.setSelPlanID( result.getInt( "plan_id" ) );
                    frmInfoPC.setOrgReserveDate( 0 );
                    frmInfoPC.setSelRsvDate( 0 );
                    frmInfoPC.setSeq( 0 );
                    frmInfoPC.setReserveDateFormat( "" );
                    frmInfoPC.setParkingUsedKbnInit( 0 );
                    frmInfoPC.setPaymemberFlg( true );
                    rsvcomm = new ReserveCommon();
                    frmInfoPC = rsvcomm.getPlanData( frmInfoPC );
                    dataSearchPlan[j].setReserveForm( frmInfoPC );
                    if ( startDate == endDate )
                    {
                        // 指定日の料金モードから料金情報を取得
                        ret = this.getRsvPlanChargeSpecified( result.getInt( "id" ), result.getInt( "plan_id" ), adult, child, startDate );
                        // 指定日
                        dataSearchPlan[j].setCharge( this.m_charge );
                        dataSearchPlan[j].setCiTime( this.m_ci );
                        dataSearchPlan[j].setCiTimeTo( this.m_ciTo );
                        dataSearchPlan[j].setCoTime( this.m_co );
                        dataSearchPlan[j].setLowestCharge( this.m_charge );
                        dataSearchPlan[j].setSearchAdult( this.m_search_adult );
                        dataSearchPlan[j].setSearchChild( this.m_search_child );
                    }
                    else
                    {
                        // プランの料金、チェックイン、チェックアウトを取得
                        ret = this.getRsvPlanCharge( result.getInt( "id" ), result.getInt( "plan_id" ), adult, child );
                        if ( ret != false )
                        {
                            dataSearchPlan[j].setChargeModeNameList( this.m_chargeModeNameList );
                            dataSearchPlan[j].setCiTimeList( this.m_ciList );
                            dataSearchPlan[j].setCiTimeToList( this.m_ciToList );
                            dataSearchPlan[j].setCoTimeList( this.m_coList );
                            // 最低料金
                            dataSearchPlan[j].setLowestCharge( this.m_lowestCharge );
                            // 最高料金
                            dataSearchPlan[j].setMaxCharge( this.m_maxCharge );
                            dataSearchPlan[j].setSearchAdult( this.m_search_adult );
                            dataSearchPlan[j].setSearchChild( this.m_search_child );
                        }
                    }
                    // 部屋のデータを取得する
                    if ( result.getInt( "offer_kind" ) == 2 )
                    {
                        if ( startDate == endDate )
                        {
                            ret = this.getRsvPlanEmptyRoom( result.getInt( "id" ), result.getInt( "plan_id" ), startDate, 0 );
                        }
                        else
                        {
                            ret = this.getRsvPlanRoom( result.getInt( "id" ), result.getInt( "plan_id" ), startDate, endDate, 0 );
                        }
                        if ( ret != false )
                        {
                            dataSearchPlan[j].setRoomAllCount( this.m_roomAllCount );
                            dataSearchPlan[j].setRoomCount( this.m_roomCount );
                            dataSearchPlan[j].setRoomSeq( this.m_roomSeq );
                            dataSearchPlan[j].setRoomName( this.m_roomName );
                            dataSearchPlan[j].setRoomText( this.m_roomText );
                            dataSearchPlan[j].setRoomImagePc( this.m_roomImagePc );
                            dataSearchPlan[j].setRoomImageGif( this.m_roomImageGif );
                            dataSearchPlan[j].setRoomImagePng( this.m_roomImagePng );
                            dataSearchPlan[j].setRoomEquip( this.m_roomEquip );
                            dataSearchPlan[j].setRoomPr( this.m_roomPr );
                            dataSearchPlan[j].setRoomRemarks( this.m_roomRemarks );
                        }
                    }
                }
            }
            else
            {
                // 検索結果が合わなければクリアする
                dataSearchPlan = null;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchPlanDao.getPlanListFromDataBase()] Exception" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(dataSearchPlan);
    }

    /**
     * プランの料金を取得
     * 
     * @param id ホテルID
     * @param planId プランID
     * @param adult 大人の人数
     * @param child 子供の人数
     * @return
     */
    public boolean getRsvPlanCharge(int id, int planId, int adult, int child)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        SearchRsvPlan rsvplan = null;
        int maxAdultNum = 0;
        int minAdultNum = 0;
        int minChildNum = 0;

        int price = 0;
        int ciTime = 0;
        int ciTimeTo = 0;
        int coTime = 0;
        int coKind = 0;

        if ( adult < 0 || child < 0 )
        {
            return false;
        }

        // 料金、チェックイン、チェックアウトを取得するSQL
        query = "SELECT HRPC.*,HRP.max_num_adult, HRP.min_num_adult,HRP.min_num_child, HRCM.charge_mode_name FROM hh_rsv_plan_charge HRPC" +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRPC.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1)" +
                "   INNER JOIN hh_rsv_plan HRP ON HRPC.id = HRP.id AND HRPC.plan_id = HRP.plan_id AND HRP.sales_flag=1" +
                "   INNER JOIN hh_rsv_charge_mode HRCM ON HRPC.id = HRCM.id AND HRPC.charge_mode_id = HRCM.charge_mode_id" +
                " WHERE HRPC.id = ?" +
                " AND HRPC.plan_id = ?" +
                " ORDER BY HRP.disp_index, HRPC.plan_id, HRCM.disp_index";

        rsvplan = new SearchRsvPlan();
        // グローバル変数を初期化
        this.clearRsvPlanChargeData();

        this.m_chargeModeNameList = new ArrayList<String>();
        this.m_ciList = new ArrayList<String>();
        this.m_ciToList = new ArrayList<String>();
        this.m_coList = new ArrayList<String>();

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                while( result.next() != false )
                {
                    price = 0;
                    ciTime = 0;
                    coTime = 0;

                    // 最少最大人数取得
                    maxAdultNum = result.getInt( "max_num_adult" );
                    minAdultNum = result.getInt( "min_num_adult" );
                    minChildNum = result.getInt( "min_num_child" );

                    if ( adult == 0 )
                    {
                        if ( maxAdultNum == 1 )
                        {
                            if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                            {
                                // 子供が3人以上の場合は最少人数を確認してセット
                                price = result.getInt( "adult_one_charge" ) + (result.getInt( "child_add_charge" ) * minChildNum);
                                this.m_search_adult = maxAdultNum;
                                this.m_search_child = minChildNum;
                            }
                            else
                            {
                                price = result.getInt( "adult_one_charge" ) + (result.getInt( "child_add_charge" ) * child);
                                this.m_search_adult = maxAdultNum;
                                this.m_search_child = child;
                            }
                        }
                        else if ( minAdultNum > 2 )
                        {
                            // 最低人数が2以上の時は大人最低人数分の金額で表示
                            if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                            {
                                price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (minAdultNum - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * minChildNum);
                                this.m_search_adult = minAdultNum;
                                this.m_search_child = minChildNum;
                            }
                            else
                            {
                                price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (minAdultNum - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * child);
                                this.m_search_adult = minAdultNum;
                                this.m_search_child = child;
                            }
                        }
                        else
                        {
                            // 基本的には2人用の金額を表示
                            if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                            {
                                // 子供が3人以上の場合は最少人数を確認してセット
                                price = result.getInt( "adult_two_charge" ) + (result.getInt( "child_add_charge" ) * minChildNum);
                                this.m_search_adult = 2;
                                this.m_search_child = minChildNum;
                            }
                            else
                            {
                                price = result.getInt( "adult_two_charge" ) + (result.getInt( "child_add_charge" ) * child);
                                this.m_search_adult = 2;
                                this.m_search_child = child;
                            }
                        }
                    }
                    else if ( adult == 1 )
                    {
                        this.m_search_adult = adult;
                        // 大人1人料金 + 子供の追加料金 X 子供の人数
                        if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                        {
                            price = result.getInt( "adult_one_charge" ) + (result.getInt( "child_add_charge" ) * minChildNum);
                            this.m_search_child = minChildNum;
                        }
                        else
                        {
                            price = result.getInt( "adult_one_charge" ) + (result.getInt( "child_add_charge" ) * child);
                            this.m_search_child = child;
                        }
                    }
                    else if ( adult == 2 )
                    {
                        this.m_search_adult = adult;
                        // 大人2人料金 + 子供の追加料金 X 子供の人数
                        if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                        {
                            price = result.getInt( "adult_two_charge" ) + (result.getInt( "child_add_charge" ) * minChildNum);
                            this.m_search_child = minChildNum;
                        }
                        else
                        {
                            price = result.getInt( "adult_two_charge" ) + (result.getInt( "child_add_charge" ) * child);
                            this.m_search_child = child;
                        }
                    }
                    else if ( adult > 2 && adult < rsvplan.SEARCH_ADULT_MAX_NUM )
                    {
                        // 大人2人料金 + 大人の追加料金 * (大人の人数-2) + 子供の追加料金 X 子供の人数
                        this.m_search_adult = adult;
                        if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                        {
                            price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (adult - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * minChildNum);
                            this.m_search_child = minChildNum;
                        }
                        else
                        {
                            price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (adult - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * child);
                            this.m_search_child = child;
                        }
                    }
                    else if ( adult > 2 && adult >= rsvplan.SEARCH_ADULT_MAX_NUM )
                    {
                        // 4人以上の場合は最低人数を確認する
                        if ( minAdultNum > rsvplan.SEARCH_ADULT_MAX_NUM )
                        {
                            this.m_search_adult = minAdultNum;
                            if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                            {
                                price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (minAdultNum - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * minChildNum);
                                this.m_search_child = minChildNum;
                            }
                            else
                            {
                                price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (minAdultNum - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * child);
                                this.m_search_child = child;
                            }
                        }
                        else
                        {
                            this.m_search_adult = adult;
                            if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                            {
                                price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (adult - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * minChildNum);
                                this.m_search_child = minChildNum;
                            }
                            else
                            {
                                price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (adult - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * child);
                                this.m_search_child = child;
                            }
                        }
                    }
                    ciTime = result.getInt( "ci_time_from" );
                    ciTimeTo = result.getInt( "ci_time_to" );
                    coTime = result.getInt( "co_time" );
                    coKind = result.getInt( "co_kind" );

                    if ( price < this.m_lowestCharge && price != 0 )
                    {
                        this.m_lowestCharge = price;
                    }
                    if ( price > this.m_maxCharge && price != 0 )
                    {
                        this.m_maxCharge = price;
                    }
                    m_chargeModeNameList.add( ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( result.getString( "charge_mode_name" ) ) ) );
                    m_ciList.add( String.format( "%02d:%02d", ciTime / 10000, ciTime % 10000 / 100 ) );
                    m_ciToList.add( String.format( "%02d:%02d", ciTimeTo / 10000, ciTimeTo % 10000 / 100 ) );
                    if ( coKind == 1 )
                    {
                        m_coList.add( "チェックインから" + DateEdit.formatTime( 6, coTime ) );
                    }
                    else
                    {
                        m_coList.add( String.format( "%02d:%02d", coTime / 10000, coTime % 10000 / 100 ) );
                    }
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchPlanDao.getRsvPlanCharge()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;

    }

    /**
     * プランの料金を取得（指定日）
     * 
     * @param id ホテルID
     * @param planId プランID
     * @param adult 大人の人数
     * @param child 子供の人数
     * @param date 指定日
     * @return
     */
    public boolean getRsvPlanChargeSpecified(int id, int planId, int adult, int child, int date)
    {
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int maxAdultNum = 0;
        int minAdultNum = 0;

        int price = 0;
        int ciTime = 0;
        int ciTimeTo = 0;
        int coTime = 0;
        int coKind = 0;

        if ( adult < 0 || child < 0 )
        {
            return false;
        }

        // 料金、チェックイン、チェックアウトを取得するSQL
        query = "SELECT HRPC.*,HRP.max_num_adult, HRP.min_num_adult " +
                " FROM hh_rsv_plan_charge HRPC " +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRPC.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1 )" +
                "   INNER JOIN hh_rsv_plan HRP ON HRPC.id = HRP.id AND HRPC.plan_id = HRP.plan_id AND HRP.sales_flag = 1" +
                "   INNER JOIN hh_rsv_day_charge HRDC ON HRPC.id = HRDC.id AND HRPC.plan_id = HRDC.plan_id AND HRPC.charge_mode_id = HRDC.charge_mode_id" +
                " WHERE HRPC.id = ?" +
                " AND HRPC.plan_id = ?" +
                " AND HRDC.cal_date = ?" +
                " ORDER BY HRP.disp_index, HRPC.plan_id, HRPC.charge_mode_id";

        // グローバル変数を初期化
        this.clearRsvPlanChargeData();

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, date );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    price = 0;
                    ciTime = 0;
                    coTime = 0;

                    this.m_search_adult = adult;
                    this.m_search_child = child;

                    if ( adult == 0 )
                    {
                        // 人数指定なしの時
                        maxAdultNum = result.getInt( "max_num_adult" );
                        minAdultNum = result.getInt( "min_num_adult" );
                        if ( maxAdultNum == 1 )
                        {
                            // 最大人数が1の時は大人1人の金額を表示する
                            price = result.getInt( "adult_one_charge" );
                            this.m_search_adult = maxAdultNum;
                        }
                        else if ( minAdultNum > 2 )
                        {
                            // 最低人数が2以上の時は大人最低人数分の金額で表示
                            price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (minAdultNum - ADULT_TWO));
                            this.m_search_adult = minAdultNum;
                        }
                        else
                        {
                            // 基本的には2人用の金額を表示
                            price = result.getInt( "adult_two_charge" );
                            this.m_search_adult = 2;
                        }
                    }
                    else if ( adult == 1 )
                    {
                        // 大人1人料金 + 子供の追加料金 X 子供の人数
                        price = result.getInt( "adult_one_charge" ) + (result.getInt( "child_add_charge" ) * child);
                    }
                    else if ( adult == 2 )
                    {
                        // 大人2人料金 + 子供の追加料金 X 子供の人数
                        price = result.getInt( "adult_two_charge" ) + (result.getInt( "child_add_charge" ) * child);
                    }
                    else if ( adult > 2 )
                    {
                        // 大人2人料金 + 大人の追加料金 * (大人の人数-2) + 子供の追加料金 X 子供の人数
                        price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (adult - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * child);
                    }
                    // 大人の人数が０人の時
                    else
                    {
                        // 子供の追加料金 X 子供の人数
                        price = (result.getInt( "child_add_charge" ) * child);
                    }
                    ciTime = result.getInt( "ci_time_from" );
                    ciTimeTo = result.getInt( "ci_time_to" );
                    coTime = result.getInt( "co_time" );
                    coKind = result.getInt( "co_kind" );

                    this.m_charge = price;
                    // 時刻にあわせてフォーマットさせる
                    this.m_ci = String.format( "%02d:%02d", ciTime / 10000, ciTime % 10000 / 100 );
                    this.m_ciTo = String.format( "%02d:%02d", ciTimeTo / 10000, ciTimeTo % 10000 / 100 );
                    if ( coKind == 1 )
                    {
                        this.m_co = "チェックインから" + DateEdit.formatTime( 6, coTime );
                    }
                    else
                    {
                        this.m_co = String.format( "%02d:%02d", coTime / 10000, coTime % 10000 / 100 );
                    }
                }
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[SearchPlanDao.getRsvPlanChargeSpecified()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;

    }

    /**
     * プランの部屋情報を取得
     * 
     * @param id ホテルID
     * @param planId プランID
     * @param startDate 開始日
     * @param endDate 終了日
     * @return
     */
    public boolean getRsvPlanRoom(int id, int planId, int startDate, int endDate, int seq)
    {
        int dataCount = 0;
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 部屋の管理番号、部屋名、参照部屋名称、部屋番号を取得するSQL
        query = "SELECT HRRPR.id, HRRPR.plan_id, HRRPR.seq, HHRM.room_name, HHRM.room_text, HHRM.refer_name, HB.hotenavi_id";
        if ( seq > 0 )
        {
            query += ",HRR.room_pr, HRR.remarks";
        }
        query += " FROM hh_rsv_rel_plan_room HRRPR" +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRRPR.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1 )" +
                "   INNER JOIN hh_rsv_plan HRP ON HRRPR.id = HRP.id AND HRRPR.plan_id = HRP.plan_id AND HRP.sales_flag = 1" +
                "   INNER JOIN hh_hotel_room_more HHRM ON HRRPR.id = HHRM.id AND HRRPR.seq = HHRM.seq AND HHRM.disp_flag = 1";
        if ( seq > 0 )
        {
            query += "   INNER JOIN hh_rsv_room HRR ON HRRPR.id = HRR.id AND HRRPR.seq = HRR.seq AND HRR.sales_flag = 1";
        }
        query += "   INNER JOIN hh_hotel_basic HB ON HRRPR.id = HB.id" +
                " WHERE HRRB.id = ?" +
                " AND HRRPR.plan_id = ?" +
                " AND HRRPR.sales_start_date <=" + endDate +
                " AND HRRPR.sales_end_date >=" + startDate;

        if ( seq > 0 )
        {
            query += " AND HRRPR.seq =" + seq;
        }
        query += " ORDER BY HRP.disp_index, HRP.plan_id, HRRPR.seq";

        try
        {
            connection = DBConnection.getConnection();

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );

            Logging.info( "test:" + query );
            result = prestate.executeQuery();
            dataCount = 0;
            // 部屋用変数の初期化
            this.clearRsvPlanRoomData();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.m_roomCount = result.getRow();
                    this.m_roomAllCount = this.m_roomCount;
                }

                this.m_roomSeq = new int[this.m_roomCount];
                this.m_roomText = new String[this.m_roomCount];
                this.m_roomName = new String[this.m_roomCount];
                this.m_roomImagePc = new String[this.m_roomCount];
                this.m_roomImageGif = new String[this.m_roomCount];
                this.m_roomImagePng = new String[this.m_roomCount];
                this.m_roomEquip = new String[this.m_roomCount];
                this.m_roomPr = new String[this.m_roomCount];
                this.m_roomRemarks = new String[this.m_roomCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_roomName[dataCount] = new String();
                    this.m_roomText[dataCount] = new String();
                    this.m_roomImagePc[dataCount] = new String();
                    this.m_roomImageGif[dataCount] = new String();
                    this.m_roomImagePng[dataCount] = new String();
                    this.m_roomEquip[dataCount] = new String();

                    // データをセット
                    this.m_roomSeq[dataCount] = result.getInt( "seq" );
                    this.m_roomName[dataCount] = result.getString( "room_name" );
                    this.m_roomText[dataCount] = result.getString( "room_text" );
                    // 画像のパスはそれぞれホテル部屋詳細ページを参考に取得
                    this.m_roomImagePc[dataCount] = this.existFile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ) );
                    this.m_roomImageGif[dataCount] = this.existFileMobile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ), DataMasterUseragent.CARRIER_DOCOMO );
                    this.m_roomImagePng[dataCount] = this.existFileMobile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ), DataMasterUseragent.CARRIER_AU );

                    // 設備を文字列として取得
                    this.m_roomEquip[dataCount] = this.getRoomEquip( id, result.getInt( "seq" ) );
                    if ( seq > 0 )
                    {
                        this.m_roomPr[dataCount] = ConvertCharacterSet.convDb2Form( result.getString( "room_pr" ) );
                        this.m_roomRemarks[dataCount] = ConvertCharacterSet.convDb2Form( result.getString( "remarks" ) );
                    }
                    dataCount++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchPlanDao.getRsvPlanRoom()] Exception:" + e.toString() );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            if ( dataCount > 0 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
        }

        return ret;
    }

    /**
     * プランで空いている部屋を取得
     * 
     * @param id ホテルID
     * @param planId プランID
     * @param date 指定日付
     * @param seq 管理番号
     * @return
     */
    public boolean getRsvPlanEmptyRoom(int id, int planId, int date, int seq)
    {
        int dataCount = 0;
        boolean ret = false;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 部屋の管理番号、部屋名、参照部屋名称、部屋番号を取得するSQL
        query = "SELECT HHRM.id, HRP.plan_id, HHRM.seq, HHRM.room_name, HHRM.refer_name, HB.hotenavi_id, HHRM.room_text";
        if ( seq > 0 )
        {
            query += ",HRR.room_pr, HRR.remarks";
        }
        query += " FROM hh_rsv_rel_plan_room HRRPR " +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRRPR.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1 )" +
                "   INNER JOIN hh_rsv_plan HRP ON HRRB.id = HRP.id AND HRRPR.plan_id = HRP.plan_id AND HRP.sales_flag=1" +
                "   INNER JOIN hh_hotel_room_more HHRM ON HRRPR.id = HHRM.id AND HRRPR.seq = HHRM.seq AND HHRM.disp_flag = 1" +
                "   INNER JOIN hh_hotel_basic HB ON HRRPR.id = HB.id";
        if ( seq > 0 )
        {
            // query += "   INNER JOIN hh_rsv_room HRR ON HRRPR.id = HRR.id AND HRRPR.seq = HRR.seq AND HRR.sales_flag = 1";
        }
        query += "   INNER JOIN hh_rsv_room HRR ON HRRPR.id = HRR.id AND HRRPR.seq = HRR.seq AND HRR.sales_flag = 1" +
                "   INNER JOIN hh_rsv_room_remainder HRRR ON HRRPR.id = HRRR.id AND HRRPR.seq = HRRR.seq AND HRRR.status = 1" +
                " WHERE HRRPR.id = ?" +
                " AND HRRPR.plan_id = ?" +
                " AND HRRPR.sales_start_date <=" + date +
                " AND HRRPR.sales_end_date >=" + date +
                " AND HRRR.cal_date = " + date;
        if ( seq > 0 )
        {
            query += " AND HRRR.seq = " + seq;
        }
        query += " ORDER BY HRP.disp_index, HRP.plan_id, HRRPR.seq";
        try
        {
            connection = DBConnection.getConnection();

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );

            result = prestate.executeQuery();
            dataCount = 0;
            // 部屋用変数の初期化
            this.clearRsvPlanRoomData();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.m_roomCount = result.getRow();
                    this.m_roomAllCount = this.m_roomCount;
                }

                this.m_roomSeq = new int[this.m_roomCount];
                this.m_roomName = new String[this.m_roomCount];
                this.m_roomText = new String[this.m_roomCount];
                this.m_roomEquip = new String[this.m_roomCount];
                this.m_roomImagePc = new String[this.m_roomCount];
                this.m_roomImageGif = new String[this.m_roomCount];
                this.m_roomImagePng = new String[this.m_roomCount];
                this.m_roomEquip = new String[this.m_roomCount];
                this.m_roomPr = new String[this.m_roomCount];
                this.m_roomRemarks = new String[this.m_roomCount];

                result.beforeFirst();
                while( result.next() != false )
                {
                    this.m_roomName[dataCount] = new String();
                    this.m_roomText[dataCount] = new String();
                    this.m_roomEquip[dataCount] = new String();
                    this.m_roomImagePc[dataCount] = new String();
                    this.m_roomImageGif[dataCount] = new String();
                    this.m_roomImagePng[dataCount] = new String();
                    this.m_roomEquip[dataCount] = new String();

                    // データをセット
                    this.m_roomSeq[dataCount] = result.getInt( "seq" );
                    this.m_roomName[dataCount] = result.getString( "room_name" );
                    this.m_roomText[dataCount] = result.getString( "room_text" );
                    // 画像のパスはそれぞれホテル部屋詳細ページを参考に取得
                    this.m_roomImagePc[dataCount] = this.existFile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ) );
                    this.m_roomImageGif[dataCount] = this.existFileMobile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ), DataMasterUseragent.CARRIER_DOCOMO );
                    this.m_roomImagePng[dataCount] = this.existFileMobile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ), DataMasterUseragent.CARRIER_AU );

                    // 設備を文字列として取得
                    this.m_roomEquip[dataCount] = this.getRoomEquip( id, result.getInt( "seq" ) );
                    if ( seq > 0 )
                    {
                        this.m_roomPr[dataCount] = ConvertCharacterSet.convDb2Form( result.getString( "room_pr" ) );
                        this.m_roomRemarks[dataCount] = ConvertCharacterSet.convDb2Form( result.getString( "remarks" ) );
                    }
                    dataCount++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchPlanDao.getRsvPlanEmptyRoom()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
            if ( dataCount > 0 )
            {
                ret = true;
            }
            else
            {
                ret = false;
            }
        }

        return ret;
    }

    /***
     * ファイル確認処理
     * 
     * @param hotenaviId ホテナビID
     * @param referName 参照ファイル名
     * @return
     */
    public String existFile(String hotenaviId, String referName)
    {
        String filePath = "";
        File file;

        if ( referName.equals( "" ) == false )
        {
            filePath = "/hotenavi/" + hotenaviId + "/image/r" + referName + ".jpg";
            file = new File( "/happyhotel" + filePath );

            // 画像がなかったらnoImage画像を表示する
            if ( file.exists() == false )
            {
                filePath = "/common/images/noimage.jpg";
            }
        }
        else
        {
            filePath = "/common/images/noimage.jpg";
        }
        return(filePath);
    }

    /***
     * ファイル確認処理
     * 
     * @param hotenaviId ホテナビID
     * @param referName 参照ファイル名
     * @param carrierFlag キャリアタイプ
     * @return
     */
    public String existFileMobile(String hotenaviId, String referName, int carrierFlag)
    {
        String filePath = "";
        String carrierHotenavi = "";
        String fileType = "";
        File file;

        if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
        {
            carrierHotenavi = "i";
            fileType = "gif";
        }
        else
        {
            carrierHotenavi = "ez";
            fileType = "png";
        }

        if ( referName.equals( "" ) == false )
        {
            filePath = "/hotenavi/" + hotenaviId + "/" + carrierHotenavi + "/image/r" + referName + "l." + fileType;
            file = new File( "/happyhotel" + filePath );
            // ファイルがなかったらnoImage画像を表示
            if ( file.exists() == false )
            {
                filePath = "/common/images/noimage.jpg";
            }
        }
        else
        {
            filePath = "/common/images/noimage.jpg";
        }
        return filePath;
    }

    /**
     * 部屋の設備情報を取得する
     * 
     * @param id ホテルID
     * @param seq 管理番号
     * @return
     */
    public String getRoomEquip(int id, int seq)
    {
        int count;
        String equipName;
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // 部屋の管理番号、部屋名、参照部屋名称、部屋番号を取得するSQL
        query = "SELECT HME.name " +
                " FROM hh_master_equip HME" +
                " INNER JOIN hh_rsv_rel_room_equip HRRE ON HME.equip_id = HRRE.equip_id" +
                " WHERE HRRE.id = ?" +
                " AND HRRE.seq = ?" +
                " ORDER BY HME.equip_id";

        equipName = "";
        try
        {
            connection = DBConnection.getConnection();

            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, seq );

            result = prestate.executeQuery();

            count = 0;

            if ( result != null )
            {
                while( result.next() )
                {
                    if ( count > 0 )
                    {
                        equipName += "/";
                    }
                    equipName += result.getString( "name" );
                    count++;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchPlanDao.getRoomEquip()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return equipName;
    }

    /***
     * プラン情報取得
     * 
     * @param id ホテルID
     * @param planIdList プランIDリスト
     * @param adult 大人の利用人数
     * @param child 子供の利用人数
     * @param startDate 開始日付
     * @param endDate 終了日付
     * @return
     * @throws Exception
     */
    public DataSearchPlan[] getRoomFromDataBase(int id, int planId, int adult, int child, int startDate, int endDate, int seq) throws Exception
    {
        boolean ret = false;

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        DataSearchPlan dataSearchPlan[] = new DataSearchPlan[0];
        FormReservePersonalInfoPC frmInfoPC = null;
        ReserveCommon rsvcomm = null;
        SearchRsvPlan rsvplan = null;

        rsvplan = new SearchRsvPlan();
        // プラン名、プランPRを取得するSQL
        query = " SELECT HRP.id, HRP.plan_id, HRP.plan_name, HRP.plan_pr, HRP.remarks, HRP.offer_kind, HRP.image_pc, HRP.image_gif, HRP.image_png" +
                " FROM hh_rsv_plan HRP" +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRP.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1 )" +
                " WHERE HRP.id = ?" +
                " AND HRP.sales_flag=1" +
                " AND HRP.plan_id  = ?";
        // 検索人数指定時はセットする
        if ( adult > 0 && adult < rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            query += " AND HRP.min_num_adult <= " + adult + " AND HRP.max_num_adult >= " + adult;
        }
        // 大人の指定人数4人以上なら
        if ( adult == rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            // 指定人数以上固定で扱う
            query += " AND HRP.max_num_adult >= " + adult;
        }
        // 子供の指定人数3人以上なら
        if ( child == rsvplan.SEARCH_CHILD_MAX_NUM )
        {
            // 指定人数以上固定で扱う
            query += " AND HRP.max_num_child >= " + child;
        }
        else
        {
            query += " AND HRP.min_num_child <= " + child + " AND HRP.max_num_child >= " + child;
        }
        query += " ORDER BY HRP.disp_index, HRP.plan_id DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    this.m_planAllCount = result.getRow();
                    this.m_planCount = this.m_planAllCount;
                }

                dataSearchPlan = new DataSearchPlan[this.m_planCount];
                result.beforeFirst();
                for( int j = 0 ; result.next() ; j++ )
                {
                    dataSearchPlan[j] = new DataSearchPlan();
                    // プランデータをセット
                    dataSearchPlan[j].setId( result.getInt( "id" ) );
                    dataSearchPlan[j].setPlanId( result.getInt( "plan_id" ) );
                    dataSearchPlan[j].setPlanName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
                    dataSearchPlan[j].setPlanPr( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_pr" ) ) ) );
                    dataSearchPlan[j].setRemarks( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ) ) ) );
                    dataSearchPlan[j].setOfferkind( result.getInt( "offer_kind" ) );
                    dataSearchPlan[j].setPlanImagePc( result.getString( "image_pc" ) );
                    dataSearchPlan[j].setPlanImageGif( result.getString( "image_gif" ) );
                    dataSearchPlan[j].setPlanImagePng( result.getString( "image_png" ) );
                    // 予約の必要情報取得
                    frmInfoPC = new FormReservePersonalInfoPC();
                    frmInfoPC.setMode( ReserveCommon.MODE_INS );
                    frmInfoPC.setSelHotelID( result.getInt( "id" ) );
                    frmInfoPC.setSelPlanID( result.getInt( "plan_id" ) );
                    frmInfoPC.setOrgReserveDate( 0 );
                    frmInfoPC.setSelRsvDate( 0 );
                    frmInfoPC.setSeq( 0 );
                    frmInfoPC.setReserveDateFormat( "" );
                    frmInfoPC.setParkingUsedKbnInit( 0 );
                    frmInfoPC.setPaymemberFlg( true );
                    rsvcomm = new ReserveCommon();
                    frmInfoPC = rsvcomm.getPlanData( frmInfoPC );
                    dataSearchPlan[j].setReserveForm( frmInfoPC );
                    if ( startDate == endDate )
                    {
                        // 指定日の料金モードから料金情報を取得
                        ret = this.getRsvPlanChargeSpecified( result.getInt( "id" ), result.getInt( "plan_id" ), adult, child, startDate );
                        // 指定日
                        dataSearchPlan[j].setCharge( this.m_charge );
                        dataSearchPlan[j].setCiTime( this.m_ci );
                        dataSearchPlan[j].setCiTimeTo( this.m_ciTo );
                        dataSearchPlan[j].setCoTime( this.m_co );
                        dataSearchPlan[j].setSearchAdult( this.m_search_adult );
                        dataSearchPlan[j].setSearchChild( this.m_search_child );
                    }
                    else
                    {
                        // プランの料金、チェックイン、チェックアウトを取得
                        ret = this.getRsvPlanCharge( result.getInt( "id" ), result.getInt( "plan_id" ), adult, child );
                        if ( ret != false )
                        {
                            dataSearchPlan[j].setChargeModeNameList( this.m_chargeModeNameList );
                            dataSearchPlan[j].setCiTimeList( this.m_ciList );
                            dataSearchPlan[j].setCiTimeToList( this.m_ciToList );
                            dataSearchPlan[j].setCoTimeList( this.m_coList );

                            // 最低料金をセット
                            dataSearchPlan[j].setLowestCharge( this.m_lowestCharge );
                            // 最高料金をセット
                            dataSearchPlan[j].setMaxCharge( this.m_maxCharge );
                            dataSearchPlan[j].setSearchAdult( this.m_search_adult );
                            dataSearchPlan[j].setSearchChild( this.m_search_child );
                        }
                    }
                    // 部屋のデータを取得する
                    if ( result.getInt( "offer_kind" ) == 2 )
                    {
                        if ( startDate == endDate )
                        {
                            ret = this.getRsvPlanEmptyRoom( result.getInt( "id" ), result.getInt( "plan_id" ), startDate, seq );
                        }
                        else
                        {
                            ret = this.getRsvPlanRoom( result.getInt( "id" ), result.getInt( "plan_id" ), startDate, endDate, seq );
                        }
                        if ( ret != false )
                        {
                            dataSearchPlan[j].setRoomAllCount( this.m_roomAllCount );
                            dataSearchPlan[j].setRoomCount( this.m_roomCount );
                            dataSearchPlan[j].setRoomSeq( this.m_roomSeq );
                            dataSearchPlan[j].setRoomName( this.m_roomName );
                            dataSearchPlan[j].setRoomText( this.m_roomText );
                            dataSearchPlan[j].setRoomImagePc( this.m_roomImagePc );
                            dataSearchPlan[j].setRoomImageGif( this.m_roomImageGif );
                            dataSearchPlan[j].setRoomImagePng( this.m_roomImagePng );
                            dataSearchPlan[j].setRoomEquip( this.m_roomEquip );
                            dataSearchPlan[j].setRoomPr( this.m_roomPr );
                            dataSearchPlan[j].setRoomRemarks( this.m_roomRemarks );
                        }
                        else
                        {
                            dataSearchPlan[j].setRoomAllCount( 0 );
                            dataSearchPlan[j].setRoomCount( 0 );
                            dataSearchPlan[j].setRoomText( null );
                            dataSearchPlan[j].setRoomSeq( null );
                            dataSearchPlan[j].setRoomName( null );
                            dataSearchPlan[j].setRoomImagePc( null );
                            dataSearchPlan[j].setRoomImageGif( null );
                            dataSearchPlan[j].setRoomImagePng( null );
                            dataSearchPlan[j].setRoomEquip( null );
                            dataSearchPlan[j].setRoomPr( null );
                            dataSearchPlan[j].setRoomRemarks( null );
                        }
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[SearchPlanDao.getPlanListFromDataBase()] Exception" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(dataSearchPlan);
    }
}
