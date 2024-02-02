/*
 * プランクラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.ConvertCharacterSet;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;

/*
 * ここにimportするクラスを追加
 */

public class DataRsvPlan implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -5017740779206486110L;

    private int               iD;
    private int               planId;
    private String            planName;
    private int               dispStartDate;
    private int               dispEndDate;
    private int               salesStartDay;
    private int               salesEndDay;
    private int               reseveStartDate;
    private int               reserveEndDate;
    private int               reserveStartTime;
    private int               reserveEndTime;
    private int               offerKind;
    private String            planPr;
    private int               maxQuantity;
    private int               planGroupId;
    private int               salesFlag;
    private String            imagePc;
    private String            imageGif;
    private String            imagePng;
    private int               givingPointKind;
    private int               givingPoint;
    private int               maxNumAdult;
    private int               maxNumChild;
    private int               minNumAdult;
    private int               minNumChild;
    private int               requestFlag;
    private String            question;
    private int               questionFlag;
    private String            remarks;
    private int               dispIndex;
    private int               longStayDay;
    private int               publishingFlag;
    private String            hotelId;
    private int               userId;
    private int               lastUpdate;
    private int               lastUptime;
    private int               salesStopWeekStatus;
    private int               maxNumMan;
    private int               minNumMan;
    private int               maxNumWoman;
    private int               minNumWoman;
    private int               manCountJudgeFlag;

    /**
     * データの初期化
     */
    public DataRsvPlan()
    {
        iD = 0;
        planId = 0;
        planName = "";
        dispStartDate = 0;
        dispEndDate = 0;
        salesStartDay = 0;
        salesEndDay = 0;
        reseveStartDate = 0;
        reserveEndDate = 0;
        reserveStartTime = 0;
        reserveEndTime = 0;
        offerKind = 0;
        planPr = "";
        maxQuantity = 0;
        planGroupId = 0;
        salesFlag = 0;
        imagePc = "";
        imageGif = "";
        imagePng = "";
        givingPointKind = 0;
        givingPoint = 0;
        maxNumAdult = 0;
        maxNumChild = 0;
        minNumChild = 0;
        minNumAdult = 0;
        setQuestionFlag( 0 );
        question = "";
        remarks = "";
        dispIndex = 0;
        longStayDay = 0;
        publishingFlag = 0;
        requestFlag = 0;
        hotelId = "";
        userId = 0;
        lastUpdate = 0;
        lastUptime = 0;
        setSalesStopWeekStatus( 0 );
        maxNumMan = 0;
        minNumMan = 0;
        maxNumWoman = 0;
        minNumWoman = 0;
        manCountJudgeFlag = 0;
    }

    // getter
    public int getID()
    {
        return this.iD;
    }

    public int getPlanId()
    {
        return this.planId;
    }

    public String getPlanName()
    {
        return this.planName;
    }

    public int getDispStartDate()
    {
        return this.dispStartDate;
    }

    public int getDispEndDate()
    {
        return this.dispEndDate;
    }

    public int getSalesStartDay()
    {
        return this.salesStartDay;
    }

    public int getSalesEndDay()
    {
        return this.salesEndDay;
    }

    public int getReseveStartDate()
    {
        return this.reseveStartDate;
    }

    public int getReserveEndDate()
    {
        return this.reserveEndDate;
    }

    public int getReserveStartTime()
    {
        return this.reserveStartTime;
    }

    public int getReserveEndTime()
    {
        return this.reserveEndTime;
    }

    public int getOfferKind()
    {
        return this.offerKind;
    }

    public String getPlanPr()
    {
        return this.planPr;
    }

    public int getMaxQuantity()
    {
        return this.maxQuantity;
    }

    public int getPlanGroupId()
    {
        return this.planGroupId;
    }

    public int getSalesFlag()
    {
        return this.salesFlag;
    }

    public String getImagePc()
    {
        return this.imagePc;
    }

    public String getImageGif()
    {
        return this.imageGif;
    }

    public String getImagePng()
    {
        return this.imagePng;
    }

    public int getGivingPointKind()
    {
        return this.givingPointKind;
    }

    public int getGivingPoint()
    {
        return this.givingPoint;
    }

    public int getMaxNumAdult()
    {
        return this.maxNumAdult;
    }

    public int getMaxNumChild()
    {
        return this.maxNumChild;
    }

    public String getQuestion()
    {
        return this.question;
    }

    public String getRemarks()
    {
        return this.remarks;
    }

    public int getDispIndex()
    {
        return this.dispIndex;
    }

    public String getHotelId()
    {
        return this.hotelId;
    }

    public int getUserId()
    {
        return this.userId;
    }

    public int getLastUpdate()
    {
        return this.lastUpdate;
    }

    public int getLastUptime()
    {
        return this.lastUptime;
    }

    public int getLongStayDay()
    {
        return longStayDay;
    }

    public int getPublishingFlag()
    {
        return publishingFlag;
    }

    public int getMinNumAdult()
    {
        return minNumAdult;
    }

    public int getMinNumChild()
    {
        return minNumChild;
    }

    public int getRequestFlag()
    {
        return requestFlag;
    }

    public int getQuestionFlag()
    {
        return questionFlag;
    }

    public int getSalesStopWeekStatus()
    {
        return salesStopWeekStatus;
    }

    public int getMinNumMan()
    {
        return minNumMan;
    }

    public int getMinNumWoman()
    {
        return minNumWoman;
    }

    public int getMaxNumMan()
    {
        return maxNumMan;
    }

    public int getMaxNumWoman()
    {
        return maxNumWoman;
    }

    public int getManCountJudgeFlag()
    {
        return manCountJudgeFlag;
    }

    /**
     * 
     * setter
     * 
     */
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public void setDispStartDate(int dispStartDate)
    {
        this.dispStartDate = dispStartDate;
    }

    public void setDispEndDate(int dispEndDate)
    {
        this.dispEndDate = dispEndDate;
    }

    public void setSalesStartDay(int salesStartDate)
    {
        this.salesStartDay = salesStartDate;
    }

    public void setSalesEndDay(int salesEndDate)
    {
        this.salesEndDay = salesEndDate;
    }

    public void setReseveStartDate(int reseveStartDate)
    {
        this.reseveStartDate = reseveStartDate;
    }

    public void setReserveEndDate(int reserveEndDate)
    {
        this.reserveEndDate = reserveEndDate;
    }

    public void setReserveStartTime(int reserveStartTime)
    {
        this.reserveStartTime = reserveStartTime;
    }

    public void setReserveEndTime(int reserveEndTime)
    {
        this.reserveEndTime = reserveEndTime;
    }

    public void setOfferKind(int offerKind)
    {
        this.offerKind = offerKind;
    }

    public void setPlanPr(String planPr)
    {
        this.planPr = planPr;
    }

    public void setMaxQuantity(int maxQuantity)
    {
        this.maxQuantity = maxQuantity;
    }

    public void setPlanGroupId(int planGroupId)
    {
        this.planGroupId = planGroupId;
    }

    public void setSalesFlag(int salesFlag)
    {
        this.salesFlag = salesFlag;
    }

    public void setImagePc(String imagePc)
    {
        this.imagePc = imagePc;
    }

    public void setImageGif(String imageGif)
    {
        this.imageGif = imageGif;
    }

    public void setImagePng(String imagePng)
    {
        this.imagePng = imagePng;
    }

    public void setGivingPointKind(int givingPointKind)
    {
        this.givingPointKind = givingPointKind;
    }

    public void setGivingPoint(int givingPoint)
    {
        this.givingPoint = givingPoint;
    }

    public void setMaxNumAdult(int maxNumAdult)
    {
        this.maxNumAdult = maxNumAdult;
    }

    public void setMaxNumChild(int maxNumChild)
    {
        this.maxNumChild = maxNumChild;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void steUserId(int userId)
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

    public void setLongStayDay(int longStayDay)
    {
        this.longStayDay = longStayDay;
    }

    public void setPublishingFlag(int publishingFlag)
    {
        this.publishingFlag = publishingFlag;
    }

    public void setMinNumAdult(int minNumAdult)
    {
        this.minNumAdult = minNumAdult;
    }

    public void setMinNumChild(int minNumChild)
    {
        this.minNumChild = minNumChild;
    }

    public void setRequestFlag(int requestFlag)
    {
        this.requestFlag = requestFlag;
    }

    public void setQuestionFlag(int questionFlag)
    {
        this.questionFlag = questionFlag;
    }

    public void setSalesStopWeekStatus(int salesStopWeekStatus)
    {
        this.salesStopWeekStatus = salesStopWeekStatus;
    }

    public void setMinNumMan(int minNumMan)
    {
        this.minNumMan = minNumMan;
    }

    public void setMinNumWoman(int minNumWoman)
    {
        this.minNumWoman = minNumWoman;
    }

    public void setMaxNumMan(int maxNumMan)
    {
        this.maxNumMan = maxNumMan;
    }

    public void setMaxNumWoman(int maxNumWoman)
    {
        this.maxNumWoman = maxNumWoman;
    }

    public void setManCountJudgeFlag(int manCountJudgeFlag)
    {
        this.manCountJudgeFlag = manCountJudgeFlag;
    }

    /**
     * プラン情報取得
     * 
     * @param iD ホテルID
     * @param planId プランID
     * @return 処理結果(True:正常,False:異常)
     */
    public boolean getData(int Id, int planId)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, plan_id, plan_name, disp_start_date, disp_end_date," +
                " sales_start_date, sales_end_date, reserve_start_day, reserve_end_day," +
                " reserve_start_time, reserve_end_time, offer_kind, plan_pr," +
                " max_quantity, plan_group_id, sales_flag, image_pc, image_gif, image_png," +
                " giving_point_kind, giving_point, max_num_adult, max_num_child, min_num_adult, min_num_child," +
                " max_num_man, max_num_woman, min_num_man, min_num_woman, man_count_judge_flag, " +
                " question, question_flag, sales_stop_week_status, remarks, disp_index, long_stay_day, publishing_flag, request_flag, hotel_id, user_id, last_update, last_uptime " +
                " FROM hh_rsv_plan WHERE id = ? AND plan_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, planId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.planId = result.getInt( "plan_id" );
                    this.planName = ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_name" ) ) );
                    this.dispStartDate = result.getInt( "disp_start_date" );
                    this.dispEndDate = result.getInt( "disp_end_date" );
                    this.salesStartDay = result.getInt( "sales_start_date" );
                    this.salesEndDay = result.getInt( "sales_end_date" );
                    this.reseveStartDate = result.getInt( "reserve_start_day" );
                    this.reserveEndDate = result.getInt( "reserve_end_day" );
                    this.reserveStartTime = result.getInt( "reserve_start_time" );
                    this.reserveEndTime = result.getInt( "reserve_end_time" );
                    this.offerKind = result.getInt( "offer_kind" );
                    this.planPr = ConvertCharacterSet.convDb2Form( ReplaceString.HTMLEscape( result.getString( "plan_pr" ) ) );
                    this.maxQuantity = result.getInt( "max_quantity" );
                    this.planGroupId = result.getInt( "plan_group_id" );
                    this.salesFlag = result.getInt( "sales_flag" );
                    this.imagePc = CheckString.checkStringForNull( result.getString( "image_pc" ) );
                    this.imageGif = CheckString.checkStringForNull( result.getString( "image_gif" ) );
                    this.imagePng = CheckString.checkStringForNull( result.getString( "image_png" ) );
                    this.givingPointKind = result.getInt( "giving_point_kind" );
                    this.givingPoint = result.getInt( "giving_point" );
                    this.maxNumAdult = result.getInt( "max_num_adult" );
                    this.maxNumChild = result.getInt( "max_num_child" );
                    this.minNumAdult = result.getInt( "min_num_adult" );
                    this.minNumChild = result.getInt( "min_num_child" );
                    this.minNumMan = result.getInt( "min_num_man" );
                    this.minNumWoman = result.getInt( "min_num_woman" );
                    this.maxNumMan = result.getInt( "max_num_man" );
                    this.maxNumWoman = result.getInt( "max_num_woman" );
                    this.manCountJudgeFlag = result.getInt( "man_count_judge_flag" );
                    this.question = ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( ReplaceString.HTMLEscape( result.getString( "question" ) ) ) );
                    this.questionFlag = result.getInt( "question_flag" );
                    this.remarks = ConvertCharacterSet.convDb2Form( CheckString.checkStringForNull( ReplaceString.HTMLEscape( result.getString( "remarks" ) ) ) );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.longStayDay = result.getInt( "long_stay_day" );
                    this.publishingFlag = result.getInt( "publishing_flag" );
                    this.requestFlag = result.getInt( "request_flag" );
                    this.hotelId = result.getString( "hotel_id" );
                    this.userId = result.getInt( "user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.salesStopWeekStatus = result.getInt( "sales_stop_week_status" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvPlan.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

}
