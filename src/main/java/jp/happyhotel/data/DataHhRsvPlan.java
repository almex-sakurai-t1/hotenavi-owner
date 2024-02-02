package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �v�����}�X�^�擾�N���X
 * 
 * @author Techno
 * @version 1.00 2015/3/20
 */
public class DataHhRsvPlan implements Serializable
{
    public static final String TABLE = "hh_rsv_plan";
    private int                id;                   // �z�e��ID
    private int                planId;               // �v����ID
    private int                planSubId;            // �v����ID�}��
    private int                latestFlag;           // �ŐV�t���O
    private String             planName;             // �v�������́i�����j
    private int                salesStartDate;       // �̔��J�n��
    private int                salesEndDate;         // �̔��I����
    private int                reserveStartDay;      // �\����͊��ԁi�J�n�j
    private int                reserveEndDay;        // �\����͊��ԁi�I���j
    private int                reserveStartTime;     // �\����͎����i�J�n�j
    private int                reserveEndTime;       // �\����͎����i�I���j
    private String             planPr;               // �v�����Љ�
    private int                dispIndex;            // �\����
    private int                publishingFlag;       // �f�ڃt���O
    private int                maxStayNumMan;        // �ő�h���l���i�j�j
    private int                maxStayNumWoman;      // �ő�h���l���i���j
    private int                minStayNumMan;        // �ŏ��h���l���i�j�j
    private int                minStayNumWoman;      // �ŏ��h���l���i���j
    private int                maxStayNumChild;      // �ő�h���l���i�q���j
                                                      // private int currcvLimit; // �����\���t���؎���
                                                      // private int currcvFlag; // �����\���t�t���O
    private int                reserveEndNotsetFlag; // �\��\���ԏI�������ݒ�t���O
    private int                paymentKind;          // ���O���ϕ��@
    private int                localPaymentKind;     // ���n�x���敪
    private int                adultAddChargeKind;   // ��l�ǉ������敪
    private int                adultAddCharge;       // ��l�ǉ�����
    private int                childAddChargeKind;   // �q���ǉ������敪
    private int                childAddCharge;       // �q���ǉ�����
    private int                consumerDemandsKind;  // ���q�l�v�]�����\���敪
    private int                planSalesStatus;      // �v�����̔��X�e�[�^�X
    private int                roomSelectKind;       // �����I���敪
    private String             imagePcMain;          // �v�����摜PC�����N�惁�C��
    private String             imagePc1;             // �v�����摜PC�����N��1
    private String             imagePc2;             // �v�����摜PC�����N��2
    private String             imagePc3;             // �v�����摜PC�����N��3
    private int                bonusMile;            // �{�[�i�X�}�C��
    private String             question;             // �\��҂ւ̎���
    private int                planType;             // �v�������
    private String             precaution;           // ���ӎ���
    private int                comingFlag;           // ���X�K�{�t���O
    private int                userId;               // ���[�UID
    private String             hotelId;              // �z�e�i�rID
    private int                lastUpdate;           // �ŏI�X�V��
    private int                lastUptime;           // �ŏI�X�V����
    private int                maxStayNum;           // �ő�h���l��
    private int                minStayNum;           // �ŏ��h���l��
    private int                simpleModeFlag;       // �v�����o�^���[�h
    private int                foreignFlag;          // �O���l�����t���O
    private int                consecutiveFlag;      // �A���t���O

    private String             imageNameMain;        // hidden�p�摜�t�@�C�����imain�j
    private String             imageNamePc1;         // hidden�p�摜�t�@�C�����ipc1�j
    private String             imageNamePc2;         // hidden�p�摜�t�@�C�����ipc2�j
    private String             imageNamePc3;         // hidden�p�摜�t�@�C�����ipc3�j

    /**
     * �f�[�^�����������܂��B
     */
    public DataHhRsvPlan()
    {
        this.id = 0;
        this.planId = 0;
        this.planSubId = 0;
        this.latestFlag = 0;
        this.planName = "";
        this.salesStartDate = 0;
        this.salesEndDate = 0;
        this.reserveStartDay = 0;
        this.reserveEndDay = 0;
        this.reserveStartTime = 0;
        this.reserveEndTime = 0;
        this.planPr = "";
        this.dispIndex = 0;
        this.publishingFlag = 0;
        this.maxStayNumMan = 0;
        this.maxStayNumWoman = 0;
        this.minStayNumMan = 0;
        this.minStayNumWoman = 0;
        this.maxStayNumChild = 0;
        this.reserveEndNotsetFlag = 0;
        this.paymentKind = 0;
        this.localPaymentKind = 0;
        this.adultAddChargeKind = 1;
        this.adultAddCharge = 0;
        this.childAddChargeKind = 1;
        this.childAddCharge = 0;
        this.consumerDemandsKind = 0;
        this.planSalesStatus = 0;
        this.roomSelectKind = 0;
        this.imagePcMain = "";
        this.imagePc1 = "";
        this.imagePc2 = "";
        this.imagePc3 = "";
        this.bonusMile = 0;
        this.question = "";
        this.planType = 0;
        this.precaution = "";
        this.comingFlag = 0;
        this.userId = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
        this.maxStayNum = 0;
        this.minStayNum = 0;
        this.simpleModeFlag = 0;
        this.foreignFlag = 0;
        this.consecutiveFlag = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getPlanId()
    {
        return planId;
    }

    public int getPlanSubId()
    {
        return planSubId;
    }

    public int getLatestFlag()
    {
        return latestFlag;
    }

    public String getPlanName()
    {
        return planName;
    }

    public int getSalesStartDate()
    {
        return salesStartDate;
    }

    public int getSalesEndDate()
    {
        return salesEndDate;
    }

    public int getReserveStartDay()
    {
        return reserveStartDay;
    }

    public int getReserveEndDay()
    {
        return reserveEndDay;
    }

    public int getReserveStartTime()
    {
        return reserveStartTime;
    }

    public int getReserveEndTime()
    {
        return reserveEndTime;
    }

    public String getPlanPr()
    {
        return planPr;
    }

    public int getDispIndex()
    {
        return dispIndex;
    }

    public int getPublishingFlag()
    {
        return publishingFlag;
    }

    public int getMaxStayNumMan()
    {
        return maxStayNumMan;
    }

    public int getMaxStayNumWoman()
    {
        return maxStayNumWoman;
    }

    public int getMinStayNumMan()
    {
        return minStayNumMan;
    }

    public int getMinStayNumWoman()
    {
        return minStayNumWoman;
    }

    public int getMaxStayNumChild()
    {
        return maxStayNumChild;
    }

    // public int getCurrcvLimit()
    // {
    // return currcvLimit;
    // }
    //
    // public int getCurrcvFlag()
    // {
    // return currcvFlag;
    // }

    public int getReserveEndNotsetFlag()
    {
        return reserveEndNotsetFlag;
    }

    public int getPaymentKind()
    {
        return paymentKind;
    }

    public int getLocalPaymentKind()
    {
        return localPaymentKind;
    }

    public int getAdultAddChargeKind()
    {
        return adultAddChargeKind;
    }

    public int getAdultAddCharge()
    {
        return adultAddCharge;
    }

    public int getChildAddChargeKind()
    {
        return childAddChargeKind;
    }

    public int getChildAddCharge()
    {
        return childAddCharge;
    }

    public int getConsumerDemandsKind()
    {
        return consumerDemandsKind;
    }

    public int getPlanSalesStatus()
    {
        return planSalesStatus;
    }

    public int getRoomSelectKind()
    {
        return roomSelectKind;
    }

    public String getImagePcMain()
    {
        return imagePcMain;
    }

    public String getImagePc1()
    {
        return imagePc1;
    }

    public String getImagePc2()
    {
        return imagePc2;
    }

    public String getImagePc3()
    {
        return imagePc3;
    }

    public int getBonusMile()
    {
        return bonusMile;
    }

    public String getQuestion()
    {
        return question;
    }

    public int getPlanType()
    {
        return planType;
    }

    public String getPrecaution()
    {
        return precaution;
    }

    public int getComingFlag()
    {
        return comingFlag;
    }

    public int getUserId()
    {
        return userId;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getMaxStayNum()
    {
        return maxStayNum;
    }

    public int getMinStayNum()
    {
        return minStayNum;
    }

    public int getSimpleModeFlag()
    {
        return simpleModeFlag;
    }

    public int getForeignFlag()
    {
        return foreignFlag;
    }

    public int getConsecutiveFlag()
    {
        return consecutiveFlag;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public void setPlanSubId(int planSubId)
    {
        this.planSubId = planSubId;
    }

    public void setLatestFlag(int latestFlag)
    {
        this.latestFlag = latestFlag;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public void setSalesStartDate(int salesStartDate)
    {
        this.salesStartDate = salesStartDate;
    }

    public void setSalesEndDate(int salesEndDate)
    {
        this.salesEndDate = salesEndDate;
    }

    public void setReserveStartDay(int reserveStartDay)
    {
        this.reserveStartDay = reserveStartDay;
    }

    public void setReserveEndDay(int reserveEndDay)
    {
        this.reserveEndDay = reserveEndDay;
    }

    public void setReserveStartTime(int reserveStartTime)
    {
        this.reserveStartTime = reserveStartTime;
    }

    public void setReserveEndTime(int reserveEndTime)
    {
        this.reserveEndTime = reserveEndTime;
    }

    public void setPlanPr(String planPr)
    {
        this.planPr = planPr;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public void setPublishingFlag(int publishingFlag)
    {
        this.publishingFlag = publishingFlag;
    }

    public void setMaxStayNumMan(int maxStayNumMan)
    {
        this.maxStayNumMan = maxStayNumMan;
    }

    public void setMaxStayNumWoman(int maxStayNumWoman)
    {
        this.maxStayNumWoman = maxStayNumWoman;
    }

    public void setMinStayNumMan(int minStayNumMan)
    {
        this.minStayNumMan = minStayNumMan;
    }

    public void setMinStayNumWoman(int minStayNumWoman)
    {
        this.minStayNumWoman = minStayNumWoman;
    }

    public void setMaxStayNumChild(int maxStayNumChild)
    {
        this.maxStayNumChild = maxStayNumChild;
    }

    // public void setCurrcvLimit(int currcvLimit)
    // {
    // this.currcvLimit = currcvLimit;
    // }
    //
    // public void setCurrcvFlag(int currcvFlag)
    // {
    // this.currcvFlag = currcvFlag;
    // }

    public void setReserveEndNotsetFlag(int reserveEndNotsetFlag)
    {
        this.reserveEndNotsetFlag = reserveEndNotsetFlag;
    }

    public void setPaymentKind(int paymentKind)
    {
        this.paymentKind = paymentKind;
    }

    public void setLocalPaymentKind(int localPaymentKind)
    {
        this.localPaymentKind = localPaymentKind;
    }

    public void setAdultAddChargeKind(int adultAddChargeKind)
    {
        this.adultAddChargeKind = adultAddChargeKind;
    }

    public void setAdultAddCharge(int adultAddCharge)
    {
        this.adultAddCharge = adultAddCharge;
    }

    public void setChildAddChargeKind(int childAddChargeKind)
    {
        this.childAddChargeKind = childAddChargeKind;
    }

    public void setChildAddCharge(int childAddCharge)
    {
        this.childAddCharge = childAddCharge;
    }

    public void setConsumerDemandsKind(int consumerDemandsKind)
    {
        this.consumerDemandsKind = consumerDemandsKind;
    }

    public void setPlanSalesStatus(int planSalesStatus)
    {
        this.planSalesStatus = planSalesStatus;
    }

    public void setRoomSelectKind(int roomSelectKind)
    {
        this.roomSelectKind = roomSelectKind;
    }

    public void setImagePcMain(String imagePcMain)
    {
        this.imagePcMain = imagePcMain;
    }

    public void setImagePc1(String imagePc1)
    {
        this.imagePc1 = imagePc1;
    }

    public void setImagePc2(String imagePc2)
    {
        this.imagePc2 = imagePc2;
    }

    public void setImagePc3(String imagePc3)
    {
        this.imagePc3 = imagePc3;
    }

    public void setBonusMile(int bonusMile)
    {
        this.bonusMile = bonusMile;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public void setPlanType(int planType)
    {
        this.planType = planType;
    }

    public void setPrecaution(String precaution)
    {
        this.precaution = precaution;
    }

    public void setComingFlag(int comingFlag)
    {
        this.comingFlag = comingFlag;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setMaxStayNum(int maxStayNum)
    {
        this.maxStayNum = maxStayNum;
    }

    public void setMinStayNum(int minStayNum)
    {
        this.minStayNum = minStayNum;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    /****
     * �v�����}�X�^�擾
     * 
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param planSubId �v����ID�}��
     * @return
     */
    public boolean getData(int id, int planId, int planSubId)
    {
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            return this.getData( connection, id, planId, planSubId );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    /****
     * �ŐV�̃v�������擾
     * 
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param planSubId �v����ID�}��
     * @return
     */
    public boolean getLatestData(int id, int planId)
    {
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            return this.getLatestData( connection, id, planId );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.getLatestData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    /****
     * �ŐV�̃v�������擾
     * 
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param planSubId �v����ID�}��
     * @return
     */
    public int getSubId(int id, int planId)
    {
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            return this.getSubId( connection, id, planId );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.getSubId] Exception=" + e.toString() );
            return 0;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
    }

    public boolean getData(Connection connection, int id, int planId, int planSubId)
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT * FROM newRsvDB.hh_rsv_plan WHERE id = ? AND plan_id = ? AND plan_sub_id = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            prestate.setInt( 3, planSubId );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }

            setData( result );
            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.getData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    public boolean getLatestData(Connection connection, int id, int planId)
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT * FROM newRsvDB.hh_rsv_plan WHERE id = ? AND plan_id = ? AND latest_flag = 1 ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }

            setData( result );
            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.getLatestData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    public int getSubId(Connection connection, int id, int planId)
    {
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        query = "SELECT plan_sub_id FROM newRsvDB.hh_rsv_plan WHERE id = ? AND plan_id = ? ORDER BY plan_sub_id DESC";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return 0;
            }
            else
            {
                return result.getInt( "plan_sub_id" ) + 1;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.getSubId] Exception=" + e.toString() );
            return 0;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * �v�����}�X�^�ݒ�
     * 
     * @param result �}�X�^�[�\�[�g���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {

        if ( result == null )
        {
            return false;
        }

        try
        {
            this.id = result.getInt( "id" );
            this.planId = result.getInt( "plan_id" );
            this.planSubId = result.getInt( "plan_sub_id" );
            this.latestFlag = result.getInt( "latest_flag" );
            this.planName = result.getString( "plan_name" );
            this.salesStartDate = result.getInt( "sales_start_date" );
            this.salesEndDate = result.getInt( "sales_end_date" );
            this.reserveStartDay = result.getInt( "reserve_start_day" );
            this.reserveEndDay = result.getInt( "reserve_end_day" );
            this.reserveStartTime = result.getInt( "reserve_start_time" );
            this.reserveEndTime = result.getInt( "reserve_end_time" );
            this.planPr = result.getString( "plan_pr" );
            this.dispIndex = result.getInt( "disp_index" );
            this.publishingFlag = result.getInt( "publishing_flag" );
            this.maxStayNumMan = result.getInt( "max_stay_num_man" );
            this.maxStayNumWoman = result.getInt( "max_stay_num_woman" );
            this.minStayNumMan = result.getInt( "min_stay_num_man" );
            this.minStayNumWoman = result.getInt( "min_stay_num_woman" );
            this.maxStayNumChild = result.getInt( "max_stay_num_child" );
            this.reserveEndNotsetFlag = result.getInt( "reserve_end_notset_flag" );
            this.paymentKind = result.getInt( "payment_kind" );
            this.localPaymentKind = result.getInt( "local_payment_kind" );
            this.adultAddChargeKind = result.getInt( "adult_add_charge_kind" );
            this.adultAddCharge = result.getInt( "adult_add_charge" );
            this.childAddChargeKind = result.getInt( "child_add_charge_kind" );
            this.childAddCharge = result.getInt( "child_add_charge" );
            this.consumerDemandsKind = result.getInt( "consumer_demands_kind" );
            this.planSalesStatus = result.getInt( "plan_sales_status" );
            this.roomSelectKind = result.getInt( "room_select_kind" );
            this.imagePcMain = result.getString( "image_pc_main" );
            // this.imagePcMain = "/servlet/HotelPicture?id=" + this.getId();
            this.imagePc1 = result.getString( "image_pc_1" );
            this.imagePc2 = result.getString( "image_pc_2" );
            this.imagePc3 = result.getString( "image_pc_3" );
            this.bonusMile = result.getInt( "bonus_mile" );
            this.question = result.getString( "question" );
            this.planType = result.getInt( "plan_type" );
            this.precaution = result.getString( "precaution" );
            this.comingFlag = result.getInt( "coming_flag" );
            this.hotelId = result.getString( "hotel_id" );
            this.userId = result.getInt( "user_id" );
            this.lastUpdate = result.getInt( "last_update" );
            this.lastUptime = result.getInt( "last_uptime" );
            this.imageNameMain = result.getString( "image_pc_main" );
            this.imageNamePc1 = result.getString( "image_pc_1" );
            this.imageNamePc2 = result.getString( "image_pc_2" );
            this.imageNamePc3 = result.getString( "image_pc_3" );
            this.maxStayNum = result.getInt( "max_stay_num" );
            this.minStayNum = result.getInt( "min_stay_num" );
            this.simpleModeFlag = result.getInt( "simple_mode_flag" );
            this.foreignFlag = result.getInt( "foreign_flag" );
            this.consecutiveFlag = result.getInt( "consecutive_flag" );

            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.setData] Exception=" + e.toString() );
            return false;
        }
    }

    /**
     * �v�����}�X�^�}��
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean insertData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT newRsvDB.hh_rsv_plan SET ";
        query += " id=?";
        query += ", plan_id=?";
        query += ", plan_sub_id=?";
        query += ", latest_flag=?";
        query += ", plan_name=?";
        query += ", sales_start_date=?";
        query += ", sales_end_date=?";
        query += ", reserve_start_day=?";
        query += ", reserve_end_day=?";
        query += ", reserve_start_time=?";
        query += ", reserve_end_time=?";
        query += ", plan_pr=?";
        query += ", disp_index=?";
        query += ", publishing_flag=?";
        query += ", max_stay_num_man=?";
        query += ", max_stay_num_woman=?";
        query += ", min_stay_num_man=?";
        query += ", min_stay_num_woman=?";
        query += ", max_stay_num_child=?";
        query += ", currcv_limit=?";
        query += ", currcv_flag=?";
        query += ", reserve_end_notset_flag=?";
        query += ", payment_kind=?";
        query += ", local_payment_kind=?";
        query += ", adult_add_charge_kind=?";
        query += ", adult_add_charge=?";
        query += ", child_add_charge_kind=?";
        query += ", child_add_charge=?";
        query += ", consumer_demands_kind=?";
        query += ", plan_sales_status=?";
        query += ", room_select_kind=?";
        query += ", image_pc_main=?";
        query += ", image_pc_1=?";
        query += ", image_pc_2=?";
        query += ", image_pc_3=?";
        query += ", bonus_mile=?";
        query += ", question=?";
        query += ", plan_type=?";
        query += ", precaution=?";
        query += ", coming_flag=?";
        query += ", hotel_id=?";
        query += ", user_id=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", max_stay_num=?";
        query += ", min_stay_num=?";
        query += ", simple_mode_flag=?";
        query += ", foreign_flag=?";
        query += ", consecutive_flag=?";
        try
        {
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.planId );
            prestate.setInt( i++, this.planSubId );
            prestate.setInt( i++, this.latestFlag );
            prestate.setString( i++, this.planName );
            prestate.setInt( i++, this.salesStartDate );
            prestate.setInt( i++, this.salesEndDate );
            prestate.setInt( i++, this.reserveStartDay );
            prestate.setInt( i++, this.reserveEndDay );
            prestate.setInt( i++, this.reserveStartTime );
            prestate.setInt( i++, this.reserveEndTime );
            prestate.setString( i++, this.planPr );
            prestate.setInt( i++, this.dispIndex );
            prestate.setInt( i++, this.publishingFlag );
            prestate.setInt( i++, this.maxStayNumMan );
            prestate.setInt( i++, this.maxStayNumWoman );
            prestate.setInt( i++, this.minStayNumMan );
            prestate.setInt( i++, this.minStayNumWoman );
            prestate.setInt( i++, this.maxStayNumChild );
            prestate.setInt( i++, 0 );
            prestate.setInt( i++, 0 );
            prestate.setInt( i++, this.reserveEndNotsetFlag );
            prestate.setInt( i++, this.paymentKind );
            prestate.setInt( i++, this.localPaymentKind );
            prestate.setInt( i++, this.adultAddChargeKind );
            prestate.setInt( i++, this.adultAddCharge );
            prestate.setInt( i++, this.childAddChargeKind );
            prestate.setInt( i++, this.childAddCharge );
            prestate.setInt( i++, this.consumerDemandsKind );
            prestate.setInt( i++, this.planSalesStatus );
            prestate.setInt( i++, this.roomSelectKind );
            prestate.setString( i++, this.imagePcMain );
            prestate.setString( i++, this.imagePc1 );
            prestate.setString( i++, this.imagePc2 );
            prestate.setString( i++, this.imagePc3 );
            prestate.setInt( i++, this.bonusMile );
            prestate.setString( i++, this.question );
            prestate.setInt( i++, this.planType );
            prestate.setString( i++, this.precaution );
            prestate.setInt( i++, this.comingFlag );
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.userId );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.maxStayNum );
            prestate.setInt( i++, this.minStayNum );
            prestate.setInt( i++, this.simpleModeFlag );
            prestate.setInt( i++, this.foreignFlag );
            prestate.setInt( i++, this.consecutiveFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    public boolean insertData()
    {
        Connection connection = null;
        boolean ret = false;
        try
        {
            connection = DBConnection.getConnection();
            ret = insertData( connection );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �v�����}�X�^�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param planSubId �v����ID�}��
     * @return
     */
    public boolean updateData(Connection connection, int id, int planId, int planSubId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_plan SET ";
        query += " latest_flag=?";
        query += ", plan_name=?";
        query += ", sales_start_date=?";
        query += ", sales_end_date=?";
        query += ", reserve_start_day=?";
        query += ", reserve_end_day=?";
        query += ", reserve_start_time=?";
        query += ", reserve_end_time=?";
        query += ", plan_pr=?";
        query += ", disp_index=?";
        query += ", publishing_flag=?";
        query += ", max_stay_num_man=?";
        query += ", max_stay_num_woman=?";
        query += ", min_stay_num_man=?";
        query += ", min_stay_num_woman=?";
        query += ", max_stay_num_child=?";
        query += ", currcv_limit=?";
        query += ", currcv_flag=?";
        query += ", reserve_end_notset_flag=?";
        query += ", payment_kind=?";
        query += ", local_payment_kind=?";
        query += ", adult_add_charge_kind=?";
        query += ", adult_add_charge=?";
        query += ", child_add_charge_kind=?";
        query += ", child_add_charge=?";
        query += ", consumer_demands_kind=?";
        query += ", plan_sales_status=?";
        query += ", room_select_kind=?";
        query += ", image_pc_main=?";
        query += ", image_pc_1=?";
        query += ", image_pc_2=?";
        query += ", image_pc_3=?";
        query += ", bonus_mile=?";
        query += ", question=?";
        query += ", plan_type=?";
        query += ", precaution=?";
        query += ", coming_flag=?";
        query += ", hotel_id=?";
        query += ", user_id=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", max_stay_num=?";
        query += ", min_stay_num=?";
        query += ", simple_mode_flag=?";
        query += ", foreign_flag=?";
        query += ", consecutive_flag=?";
        query += " WHERE id=? AND plan_id=? AND plan_sub_id=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.latestFlag );
            prestate.setString( i++, this.planName );
            prestate.setInt( i++, this.salesStartDate );
            prestate.setInt( i++, this.salesEndDate );
            prestate.setInt( i++, this.reserveStartDay );
            prestate.setInt( i++, this.reserveEndDay );
            prestate.setInt( i++, this.reserveStartTime );
            prestate.setInt( i++, this.reserveEndTime );
            prestate.setString( i++, this.planPr );
            prestate.setInt( i++, this.dispIndex );
            prestate.setInt( i++, this.publishingFlag );
            prestate.setInt( i++, this.maxStayNumMan );
            prestate.setInt( i++, this.maxStayNumWoman );
            prestate.setInt( i++, this.minStayNumMan );
            prestate.setInt( i++, this.minStayNumWoman );
            prestate.setInt( i++, this.maxStayNumChild );
            prestate.setInt( i++, 0 );
            prestate.setInt( i++, 0 );
            prestate.setInt( i++, this.reserveEndNotsetFlag );
            prestate.setInt( i++, this.paymentKind );
            prestate.setInt( i++, this.localPaymentKind );
            prestate.setInt( i++, this.adultAddChargeKind );
            prestate.setInt( i++, this.adultAddCharge );
            prestate.setInt( i++, this.childAddChargeKind );
            prestate.setInt( i++, this.childAddCharge );
            prestate.setInt( i++, this.consumerDemandsKind );
            prestate.setInt( i++, this.planSalesStatus );
            prestate.setInt( i++, this.roomSelectKind );
            prestate.setString( i++, this.imagePcMain );
            prestate.setString( i++, this.imagePc1 );
            prestate.setString( i++, this.imagePc2 );
            prestate.setString( i++, this.imagePc3 );
            prestate.setInt( i++, this.bonusMile );
            prestate.setString( i++, this.question );
            prestate.setInt( i++, this.planType );
            prestate.setString( i++, this.precaution );
            prestate.setInt( i++, this.comingFlag );
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.userId );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.maxStayNum );
            prestate.setInt( i++, this.minStayNum );
            prestate.setInt( i++, this.simpleModeFlag );
            prestate.setInt( i++, this.foreignFlag );
            prestate.setInt( i++, this.consecutiveFlag );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.planId );
            prestate.setInt( i++, this.planSubId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �v�����}�X�^�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param planSubId �v����ID�}��
     * @return
     */
    public boolean updateData(int id, int planId, int planSubId)
    {
        boolean ret;
        Connection connection = null;
        try
        {
            connection = DBConnection.getConnection();
            ret = updateData( connection, id, planId, planSubId );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �v�����}�X�^�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param id �z�e��ID
     * @param planId �v����ID
     * @param planSubId �v����ID�}��
     * @return
     */
    public boolean updateFlag(Connection connection, int id, int planId, int planSubId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_plan SET ";
        query += " latest_flag=?";
        query += " WHERE id=? AND plan_id=? AND plan_sub_id<>?";

        try
        {
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.latestFlag );
            prestate.setInt( i++, id );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.updateFlag] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �v�����̍폜
     * 
     * @param connection
     * @param id
     * @param planId
     * @param planSubId
     * @return
     */
    public boolean deleteData(Connection connection, int id, int planId, int planSubId)
    {
        String query;
        query = "DELETE FROM newRsvDB.hh_rsv_plan WHERE id = ? AND plan_id = ? AND plan_sub_id = ? ";

        PreparedStatement prestate = null;
        try
        {
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            int i = 1;
            prestate.setInt( i++, id );
            prestate.setInt( i++, planId );
            prestate.setInt( i++, planSubId );
            prestate.executeUpdate();
            return true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvPlan.deleteData] Exception=" + e.toString() );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
    }

    public String getImageNameMain()
    {
        return imageNameMain;
    }

    public void setImageNameMain(String imageNameMain)
    {
        this.imageNameMain = imageNameMain;
    }

    public String getImageNamePc1()
    {
        return imageNamePc1;
    }

    public void setImageNamePc1(String imageNamePc1)
    {
        this.imageNamePc1 = imageNamePc1;
    }

    public String getImageNamePc2()
    {
        return imageNamePc2;
    }

    public void setImageNamePc2(String imageNamePc2)
    {
        this.imageNamePc2 = imageNamePc2;
    }

    public String getImageNamePc3()
    {
        return imageNamePc3;
    }

    public void setImageNamePc3(String imageNamePc3)
    {
        this.imageNamePc3 = imageNamePc3;
    }

    public void setSimpleModeFlag(int simpleModeFlag)
    {
        this.simpleModeFlag = simpleModeFlag;
    }

    public void setForeignFlag(int foreignFlag)
    {
        this.foreignFlag = foreignFlag;
    }

    public void setConsecutiveFlag(int consecutiveFlag)
    {
        this.consecutiveFlag = consecutiveFlag;
    }
}
