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
 * �T�[�`�v����DAO�N���X
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
    private int               m_maxCharge;                // �ō�����
    private int               m_lowestCharge;             // �Œᗿ��
    private int               m_charge;                   // �w����@�@�@����
    private String            m_ci;                       // �w����@�@�@�`�F�b�N�C��
    private String            m_ciTo;                     // �w����@�`�F�b�N�C��TO
    private String            m_co;                       // �w����@�@�@�`�F�b�N�A�E�g
    private ArrayList<String> m_chargeModeNameList = null; // �w����Ȃ�����Ӱ�ޖ���
    private ArrayList<String> m_ciList             = null; // �w����Ȃ�������FROM�������X�g
    private ArrayList<String> m_ciToList           = null; // �w����Ȃ�������TO�������X�g
    private ArrayList<String> m_coList             = null; // �w����Ȃ�������Ď������X�g
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
     * �R���X�g���N�^
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
     * �v�����ʗ����p�ϐ�������
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
     * �z�e�������p�ϐ�������
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

    /** �v������񌏐��擾 **/
    public int getCount()
    {
        return(m_planCount);
    }

    /** �v������񌏐��擾 **/
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

    /** ����Ӱ�ޖ��̃��X�g **/
    public ArrayList<String> getChargeModeNameList()
    {
        return m_chargeModeNameList;
    }

    /** ������FROM�������X�g **/
    public ArrayList<String> getCiList()
    {
        return m_ciList;
    }

    /** ������TO�������X�g **/
    public ArrayList<String> getCiToList()
    {
        return m_ciToList;
    }

    /** ������Ď������X�g **/
    public ArrayList<String> getCoList()
    {
        return m_coList;
    }

    /** �v�������擾 **/
    public DataSearchPlan[] getPlanInfo()
    {
        return(m_planInfo);
    }

    /** �Œᗿ�� **/
    public int getLowstCharge()
    {
        return(m_lowestCharge);
    }

    /** �ō����� **/
    public int getMaxCharge()
    {
        return(m_maxCharge);
    }

    /** ���� **/
    public int getCharge()
    {
        return(m_charge);
    }

    /** �`�F�b�N�C������ **/
    public String getCheckin()
    {
        return(m_ci);
    }

    /** �`�F�b�N�C������TO **/
    public String getCheckinTo()
    {
        return(m_ciTo);
    }

    /** �`�F�b�N�A�E�g���� **/
    public String getCheckout()
    {
        return(m_co);
    }

    /**
     * �\��n�s�[�����X�̃z�e��ID���X�g���擾
     * 
     * @param id �z�e��ID
     * @param planIdList �z�e��ID���X�g
     * @param adult ��l�̐l��
     * @param child �q�ǂ��̐l��
     * @param startDate �J�n���t
     * @param endDate �I�����t
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
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
                // �C�ӂ̃y�[�W���̃v����ID���Z�b�g����
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

                // �\��n�s�[�����X�f�[�^�擾
                m_planInfo = this.getPlanListFromDataBase( id, hotelIds, adult, child, startDate, endDate );
            }
        }
    }

    /**
     * �v����ID���畔���ꗗ���擾
     * 
     * @param id �z�e��ID
     * @param planIdList �z�e��ID���X�g
     * @param adult ��l�̐l��
     * @param child �q�ǂ��̐l��
     * @param startDate �J�n���t
     * @param endDate �I�����t
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
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

            // �\��n�s�[�����X�f�[�^�擾
            this.m_planInfo = this.getPlanListFromDataBase( id, planIdList, adult, child, startDate, endDate );

            if ( this.m_planInfo[0] != null )
            {
                // �v�������͍ő��1
                this.m_planCount = this.m_planInfo.length;
                this.m_planAllCount = this.m_planInfo.length;
                // �������̎擾
                this.m_roomAllCount = this.m_planInfo[0].getRoomAllCount();

                count = 0;
                // �C�ӂ̃y�[�W���̕��������Z�b�g����
                for( loop = countNum * pageNum ; loop < this.m_roomAllCount ; loop++ )
                {
                    count++;
                    if ( count >= countNum && countNum != 0 )
                    {
                        break;
                    }
                }
                // �\�����镔�������Z�b�g
                this.m_roomCount = count;

                // �\�����錏�����擾
                // �f�[�^�̈ꎞ�ۊǏꏊ
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
                // �K�v�������������o���B
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

                // �\������f�[�^�݂̂��Z�b�g
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
     * �v����ID�ƊǗ��ԍ����畔���̏����擾
     * 
     * @param id �z�e��ID
     * @param planIdList �z�e��ID���X�g
     * @param adult ��l�̐l��
     * @param child �q�ǂ��̐l��
     * @param startDate �J�n���t
     * @param endDate �I�����t
     * @param seq �����Ǘ��ԍ�
     */
    public void getRoomDetails(int id, int planId, int adult, int child, int startDate, int endDate, int seq) throws Exception
    {
        if ( planId > 0 )
        {
            m_planInfo = this.getRoomFromDataBase( id, planId, adult, child, startDate, endDate, seq );
        }
    }

    /***
     * �v�������擾
     * 
     * @param id �z�e��ID
     * @param planIdList �v����ID���X�g
     * @param adult ��l�̗��p�l��
     * @param child �q���̗��p�l��
     * @param startDate �J�n���t
     * @param endDate �I�����t
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

        // �v�������A�v����PR���擾����SQL
        query = " SELECT HRP.id, HRP.plan_id, HRP.plan_name, HRP.plan_pr, HRP.remarks, HRP.offer_kind, HRP.image_pc, HRP.image_gif, HRP.image_png" +
                " FROM hh_rsv_plan HRP" +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRP.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1 )" +
                " WHERE HRP.id = ?" +
                " AND HRP.sales_flag = 1" +
                " AND HRP.publishing_flag = 1" +
                " AND HRP.plan_id IN(" + strPlanIdList + ") ";
        // �v�����l���w�肠�莞�͏�������w��
        if ( adult > 0 && adult < rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            query += " AND HRP.min_num_adult <= " + adult + " AND HRP.max_num_adult >= " + adult;
        }
        // ��l�̎w��l��4�l�ȏ�Ȃ�
        if ( adult == rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            // �w��l���ȏ�Œ�ň���
            query += " AND HRP.max_num_adult >= " + adult;
        }
        else if ( adult > rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            query += " AND HRP.max_num_adult >= " + adult;
        }
        // �q���̎w��l��3�l�ȏ�Ȃ�
        if ( child == rsvplan.SEARCH_CHILD_MAX_NUM )
        {
            // �w��l���ȏ�Œ�ň���
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
                    // �v�����f�[�^���Z�b�g
                    dataSearchPlan[j].setId( result.getInt( "id" ) );
                    dataSearchPlan[j].setPlanId( result.getInt( "plan_id" ) );
                    dataSearchPlan[j].setPlanName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
                    dataSearchPlan[j].setPlanPr( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_pr" ) ) ) );
                    dataSearchPlan[j].setRemarks( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ) ) ) );
                    dataSearchPlan[j].setOfferkind( result.getInt( "offer_kind" ) );
                    dataSearchPlan[j].setPlanImagePc( result.getString( "image_pc" ) );
                    dataSearchPlan[j].setPlanImageGif( result.getString( "image_gif" ) );
                    dataSearchPlan[j].setPlanImagePng( result.getString( "image_png" ) );
                    // �\��̕K�v���擾
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
                        // �w����̗������[�h���痿�������擾
                        ret = this.getRsvPlanChargeSpecified( result.getInt( "id" ), result.getInt( "plan_id" ), adult, child, startDate );
                        // �w���
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
                        // �v�����̗����A�`�F�b�N�C���A�`�F�b�N�A�E�g���擾
                        ret = this.getRsvPlanCharge( result.getInt( "id" ), result.getInt( "plan_id" ), adult, child );
                        if ( ret != false )
                        {
                            dataSearchPlan[j].setChargeModeNameList( this.m_chargeModeNameList );
                            dataSearchPlan[j].setCiTimeList( this.m_ciList );
                            dataSearchPlan[j].setCiTimeToList( this.m_ciToList );
                            dataSearchPlan[j].setCoTimeList( this.m_coList );
                            // �Œᗿ��
                            dataSearchPlan[j].setLowestCharge( this.m_lowestCharge );
                            // �ō�����
                            dataSearchPlan[j].setMaxCharge( this.m_maxCharge );
                            dataSearchPlan[j].setSearchAdult( this.m_search_adult );
                            dataSearchPlan[j].setSearchChild( this.m_search_child );
                        }
                    }
                    // �����̃f�[�^���擾����
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
                // �������ʂ�����Ȃ���΃N���A����
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
     * �v�����̗������擾
     * 
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param adult ��l�̐l��
     * @param child �q���̐l��
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

        // �����A�`�F�b�N�C���A�`�F�b�N�A�E�g���擾����SQL
        query = "SELECT HRPC.*,HRP.max_num_adult, HRP.min_num_adult,HRP.min_num_child, HRCM.charge_mode_name FROM hh_rsv_plan_charge HRPC" +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRPC.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1)" +
                "   INNER JOIN hh_rsv_plan HRP ON HRPC.id = HRP.id AND HRPC.plan_id = HRP.plan_id AND HRP.sales_flag=1" +
                "   INNER JOIN hh_rsv_charge_mode HRCM ON HRPC.id = HRCM.id AND HRPC.charge_mode_id = HRCM.charge_mode_id" +
                " WHERE HRPC.id = ?" +
                " AND HRPC.plan_id = ?" +
                " ORDER BY HRP.disp_index, HRPC.plan_id, HRCM.disp_index";

        rsvplan = new SearchRsvPlan();
        // �O���[�o���ϐ���������
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

                    // �ŏ��ő�l���擾
                    maxAdultNum = result.getInt( "max_num_adult" );
                    minAdultNum = result.getInt( "min_num_adult" );
                    minChildNum = result.getInt( "min_num_child" );

                    if ( adult == 0 )
                    {
                        if ( maxAdultNum == 1 )
                        {
                            if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                            {
                                // �q����3�l�ȏ�̏ꍇ�͍ŏ��l�����m�F���ăZ�b�g
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
                            // �Œ�l����2�ȏ�̎��͑�l�Œ�l�����̋��z�ŕ\��
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
                            // ��{�I�ɂ�2�l�p�̋��z��\��
                            if ( child == rsvplan.SEARCH_CHILD_MAX_NUM && minChildNum > rsvplan.SEARCH_CHILD_MAX_NUM )
                            {
                                // �q����3�l�ȏ�̏ꍇ�͍ŏ��l�����m�F���ăZ�b�g
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
                        // ��l1�l���� + �q���̒ǉ����� X �q���̐l��
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
                        // ��l2�l���� + �q���̒ǉ����� X �q���̐l��
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
                        // ��l2�l���� + ��l�̒ǉ����� * (��l�̐l��-2) + �q���̒ǉ����� X �q���̐l��
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
                        // 4�l�ȏ�̏ꍇ�͍Œ�l�����m�F����
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
                        m_coList.add( "�`�F�b�N�C������" + DateEdit.formatTime( 6, coTime ) );
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
     * �v�����̗������擾�i�w����j
     * 
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param adult ��l�̐l��
     * @param child �q���̐l��
     * @param date �w���
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

        // �����A�`�F�b�N�C���A�`�F�b�N�A�E�g���擾����SQL
        query = "SELECT HRPC.*,HRP.max_num_adult, HRP.min_num_adult " +
                " FROM hh_rsv_plan_charge HRPC " +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRPC.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1 )" +
                "   INNER JOIN hh_rsv_plan HRP ON HRPC.id = HRP.id AND HRPC.plan_id = HRP.plan_id AND HRP.sales_flag = 1" +
                "   INNER JOIN hh_rsv_day_charge HRDC ON HRPC.id = HRDC.id AND HRPC.plan_id = HRDC.plan_id AND HRPC.charge_mode_id = HRDC.charge_mode_id" +
                " WHERE HRPC.id = ?" +
                " AND HRPC.plan_id = ?" +
                " AND HRDC.cal_date = ?" +
                " ORDER BY HRP.disp_index, HRPC.plan_id, HRPC.charge_mode_id";

        // �O���[�o���ϐ���������
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
                        // �l���w��Ȃ��̎�
                        maxAdultNum = result.getInt( "max_num_adult" );
                        minAdultNum = result.getInt( "min_num_adult" );
                        if ( maxAdultNum == 1 )
                        {
                            // �ő�l����1�̎��͑�l1�l�̋��z��\������
                            price = result.getInt( "adult_one_charge" );
                            this.m_search_adult = maxAdultNum;
                        }
                        else if ( minAdultNum > 2 )
                        {
                            // �Œ�l����2�ȏ�̎��͑�l�Œ�l�����̋��z�ŕ\��
                            price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (minAdultNum - ADULT_TWO));
                            this.m_search_adult = minAdultNum;
                        }
                        else
                        {
                            // ��{�I�ɂ�2�l�p�̋��z��\��
                            price = result.getInt( "adult_two_charge" );
                            this.m_search_adult = 2;
                        }
                    }
                    else if ( adult == 1 )
                    {
                        // ��l1�l���� + �q���̒ǉ����� X �q���̐l��
                        price = result.getInt( "adult_one_charge" ) + (result.getInt( "child_add_charge" ) * child);
                    }
                    else if ( adult == 2 )
                    {
                        // ��l2�l���� + �q���̒ǉ����� X �q���̐l��
                        price = result.getInt( "adult_two_charge" ) + (result.getInt( "child_add_charge" ) * child);
                    }
                    else if ( adult > 2 )
                    {
                        // ��l2�l���� + ��l�̒ǉ����� * (��l�̐l��-2) + �q���̒ǉ����� X �q���̐l��
                        price = result.getInt( "adult_two_charge" ) + (result.getInt( "adult_add_charge" ) * (adult - ADULT_TWO)) + (result.getInt( "child_add_charge" ) * child);
                    }
                    // ��l�̐l�����O�l�̎�
                    else
                    {
                        // �q���̒ǉ����� X �q���̐l��
                        price = (result.getInt( "child_add_charge" ) * child);
                    }
                    ciTime = result.getInt( "ci_time_from" );
                    ciTimeTo = result.getInt( "ci_time_to" );
                    coTime = result.getInt( "co_time" );
                    coKind = result.getInt( "co_kind" );

                    this.m_charge = price;
                    // �����ɂ��킹�ăt�H�[�}�b�g������
                    this.m_ci = String.format( "%02d:%02d", ciTime / 10000, ciTime % 10000 / 100 );
                    this.m_ciTo = String.format( "%02d:%02d", ciTimeTo / 10000, ciTimeTo % 10000 / 100 );
                    if ( coKind == 1 )
                    {
                        this.m_co = "�`�F�b�N�C������" + DateEdit.formatTime( 6, coTime );
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
     * �v�����̕��������擾
     * 
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param startDate �J�n��
     * @param endDate �I����
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

        // �����̊Ǘ��ԍ��A�������A�Q�ƕ������́A�����ԍ����擾����SQL
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
            // �����p�ϐ��̏�����
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

                    // �f�[�^���Z�b�g
                    this.m_roomSeq[dataCount] = result.getInt( "seq" );
                    this.m_roomName[dataCount] = result.getString( "room_name" );
                    this.m_roomText[dataCount] = result.getString( "room_text" );
                    // �摜�̃p�X�͂��ꂼ��z�e�������ڍ׃y�[�W���Q�l�Ɏ擾
                    this.m_roomImagePc[dataCount] = this.existFile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ) );
                    this.m_roomImageGif[dataCount] = this.existFileMobile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ), DataMasterUseragent.CARRIER_DOCOMO );
                    this.m_roomImagePng[dataCount] = this.existFileMobile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ), DataMasterUseragent.CARRIER_AU );

                    // �ݔ��𕶎���Ƃ��Ď擾
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
     * �v�����ŋ󂢂Ă��镔�����擾
     * 
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param date �w����t
     * @param seq �Ǘ��ԍ�
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

        // �����̊Ǘ��ԍ��A�������A�Q�ƕ������́A�����ԍ����擾����SQL
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
            // �����p�ϐ��̏�����
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

                    // �f�[�^���Z�b�g
                    this.m_roomSeq[dataCount] = result.getInt( "seq" );
                    this.m_roomName[dataCount] = result.getString( "room_name" );
                    this.m_roomText[dataCount] = result.getString( "room_text" );
                    // �摜�̃p�X�͂��ꂼ��z�e�������ڍ׃y�[�W���Q�l�Ɏ擾
                    this.m_roomImagePc[dataCount] = this.existFile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ) );
                    this.m_roomImageGif[dataCount] = this.existFileMobile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ), DataMasterUseragent.CARRIER_DOCOMO );
                    this.m_roomImagePng[dataCount] = this.existFileMobile( result.getString( "hotenavi_id" ), result.getString( "refer_name" ), DataMasterUseragent.CARRIER_AU );

                    // �ݔ��𕶎���Ƃ��Ď擾
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
     * �t�@�C���m�F����
     * 
     * @param hotenaviId �z�e�i�rID
     * @param referName �Q�ƃt�@�C����
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

            // �摜���Ȃ�������noImage�摜��\������
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
     * �t�@�C���m�F����
     * 
     * @param hotenaviId �z�e�i�rID
     * @param referName �Q�ƃt�@�C����
     * @param carrierFlag �L�����A�^�C�v
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
            // �t�@�C�����Ȃ�������noImage�摜��\��
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
     * �����̐ݔ������擾����
     * 
     * @param id �z�e��ID
     * @param seq �Ǘ��ԍ�
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

        // �����̊Ǘ��ԍ��A�������A�Q�ƕ������́A�����ԍ����擾����SQL
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
     * �v�������擾
     * 
     * @param id �z�e��ID
     * @param planIdList �v����ID���X�g
     * @param adult ��l�̗��p�l��
     * @param child �q���̗��p�l��
     * @param startDate �J�n���t
     * @param endDate �I�����t
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
        // �v�������A�v����PR���擾����SQL
        query = " SELECT HRP.id, HRP.plan_id, HRP.plan_name, HRP.plan_pr, HRP.remarks, HRP.offer_kind, HRP.image_pc, HRP.image_gif, HRP.image_png" +
                " FROM hh_rsv_plan HRP" +
                "   INNER JOIN hh_rsv_reserve_basic HRRB ON HRP.id = HRRB.id AND ( HRRB.sales_flag = 1 OR HRRB.pre_open_flag = 1 )" +
                " WHERE HRP.id = ?" +
                " AND HRP.sales_flag=1" +
                " AND HRP.plan_id  = ?";
        // �����l���w�莞�̓Z�b�g����
        if ( adult > 0 && adult < rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            query += " AND HRP.min_num_adult <= " + adult + " AND HRP.max_num_adult >= " + adult;
        }
        // ��l�̎w��l��4�l�ȏ�Ȃ�
        if ( adult == rsvplan.SEARCH_ADULT_MAX_NUM )
        {
            // �w��l���ȏ�Œ�ň���
            query += " AND HRP.max_num_adult >= " + adult;
        }
        // �q���̎w��l��3�l�ȏ�Ȃ�
        if ( child == rsvplan.SEARCH_CHILD_MAX_NUM )
        {
            // �w��l���ȏ�Œ�ň���
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
                    // �v�����f�[�^���Z�b�g
                    dataSearchPlan[j].setId( result.getInt( "id" ) );
                    dataSearchPlan[j].setPlanId( result.getInt( "plan_id" ) );
                    dataSearchPlan[j].setPlanName( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) ) );
                    dataSearchPlan[j].setPlanPr( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_pr" ) ) ) );
                    dataSearchPlan[j].setRemarks( ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "remarks" ) ) ) );
                    dataSearchPlan[j].setOfferkind( result.getInt( "offer_kind" ) );
                    dataSearchPlan[j].setPlanImagePc( result.getString( "image_pc" ) );
                    dataSearchPlan[j].setPlanImageGif( result.getString( "image_gif" ) );
                    dataSearchPlan[j].setPlanImagePng( result.getString( "image_png" ) );
                    // �\��̕K�v���擾
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
                        // �w����̗������[�h���痿�������擾
                        ret = this.getRsvPlanChargeSpecified( result.getInt( "id" ), result.getInt( "plan_id" ), adult, child, startDate );
                        // �w���
                        dataSearchPlan[j].setCharge( this.m_charge );
                        dataSearchPlan[j].setCiTime( this.m_ci );
                        dataSearchPlan[j].setCiTimeTo( this.m_ciTo );
                        dataSearchPlan[j].setCoTime( this.m_co );
                        dataSearchPlan[j].setSearchAdult( this.m_search_adult );
                        dataSearchPlan[j].setSearchChild( this.m_search_child );
                    }
                    else
                    {
                        // �v�����̗����A�`�F�b�N�C���A�`�F�b�N�A�E�g���擾
                        ret = this.getRsvPlanCharge( result.getInt( "id" ), result.getInt( "plan_id" ), adult, child );
                        if ( ret != false )
                        {
                            dataSearchPlan[j].setChargeModeNameList( this.m_chargeModeNameList );
                            dataSearchPlan[j].setCiTimeList( this.m_ciList );
                            dataSearchPlan[j].setCiTimeToList( this.m_ciToList );
                            dataSearchPlan[j].setCoTimeList( this.m_coList );

                            // �Œᗿ�����Z�b�g
                            dataSearchPlan[j].setLowestCharge( this.m_lowestCharge );
                            // �ō��������Z�b�g
                            dataSearchPlan[j].setMaxCharge( this.m_maxCharge );
                            dataSearchPlan[j].setSearchAdult( this.m_search_adult );
                            dataSearchPlan[j].setSearchChild( this.m_search_child );
                        }
                    }
                    // �����̃f�[�^���擾����
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
