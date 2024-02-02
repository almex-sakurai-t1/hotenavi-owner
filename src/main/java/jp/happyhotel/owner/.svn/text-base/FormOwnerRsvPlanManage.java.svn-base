package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * プラン管理画面Formクラス
 */
public class FormOwnerRsvPlanManage
{
    private int                                  selHotelId           = 0;
    private int                                  userId               = 0;
    private String                               errMsg               = "";
    private ArrayList<Integer>                   planId               = new ArrayList<Integer>();
    private ArrayList<String>                    planNm               = new ArrayList<String>();
    private ArrayList<String>                    dispIdx              = new ArrayList<String>();
    private ArrayList<String>                    dispNm               = new ArrayList<String>();
    private ArrayList<String>                    option               = new ArrayList<String>();
    private ArrayList<String>                    roomSeqList          = new ArrayList<String>();
    private String                               roomSeq              = "";
    private ArrayList<Integer>                   exPlanId             = new ArrayList<Integer>();
    private ArrayList<String>                    expense              = new ArrayList<String>();
    private ArrayList<String>                    ciInfo               = new ArrayList<String>();
    private ArrayList<String>                    coInfo               = new ArrayList<String>();
    private ArrayList<String>                    expenseInfo          = new ArrayList<String>();
    private ArrayList<Integer>                   salesFlgList         = new ArrayList<Integer>();
    private int                                  viewMode             = 0;
    private ArrayList<String>                    dispStatus           = new ArrayList<String>();                   // 状況表示
    private ArrayList<FormOwnerRsvPlanChargeSub> frmPlanChargeSubList = new ArrayList<FormOwnerRsvPlanChargeSub>(); // プラン別料金リスト
    private ArrayList<String>                    maxNumAdult          = new ArrayList<String>();                   // 最大人数大人
    private ArrayList<String>                    maxNumChild          = new ArrayList<String>();                   // 最大人数子供
    private ArrayList<String>                    minNumAdult          = new ArrayList<String>();                   // 最少人数大人
    private ArrayList<String>                    minNumChild          = new ArrayList<String>();                   // 最少人数子供
    private ArrayList<String>                    lastUpdateTime       = new ArrayList<String>();                   // 最終更新日時
    private ArrayList<Integer>                   salesStartDate       = new ArrayList<Integer>();                  // 販売開始日
    private ArrayList<Integer>                   salesEndDate         = new ArrayList<Integer>();                  // 販売終了日
    private ArrayList<Integer>                   dispStartDate        = new ArrayList<Integer>();                  // 販売開始日
    private ArrayList<Integer>                   dispEndDate          = new ArrayList<Integer>();                  // 販売終了日

    public int getSelHotelId()
    {
        return selHotelId;
    }

    public void setSelHotelId(int selHotelId)
    {
        this.selHotelId = selHotelId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public ArrayList<Integer> getPlanId()
    {
        return planId;
    }

    public void setPlanId(ArrayList<Integer> planId)
    {
        this.planId = planId;
    }

    public ArrayList<String> getPlanNm()
    {
        return planNm;
    }

    public void setPlanNm(ArrayList<String> planNm)
    {
        this.planNm = planNm;
    }

    public ArrayList<String> getDispIdx()
    {
        return dispIdx;
    }

    public void setDispIdx(ArrayList<String> dispIdx)
    {
        this.dispIdx = dispIdx;
    }

    public ArrayList<String> getDispNm()
    {
        return dispNm;
    }

    public void setDispNm(ArrayList<String> dispNm)
    {
        this.dispNm = dispNm;
    }

    public String getRoomSeq()
    {
        return roomSeq;
    }

    public void setRoomSeq(String roomSeq)
    {
        this.roomSeq = roomSeq;
    }

    public ArrayList<String> getRoomSeqList()
    {
        return roomSeqList;
    }

    public void setRoomSeqList(ArrayList<String> roomSeqList)
    {
        this.roomSeqList = roomSeqList;
    }

    public ArrayList<String> getExpense()
    {
        return expense;
    }

    public void setExpense(ArrayList<String> expense)
    {
        this.expense = expense;
    }

    public ArrayList<String> getCiInfo()
    {
        return ciInfo;
    }

    public void setCiInfo(ArrayList<String> ciInfo)
    {
        this.ciInfo = ciInfo;
    }

    public ArrayList<String> getCoInfo()
    {
        return coInfo;
    }

    public void setCoInfo(ArrayList<String> coInfo)
    {
        this.coInfo = coInfo;
    }

    public ArrayList<String> getExpenseInfo()
    {
        return expenseInfo;
    }

    public void setExpenseInfo(ArrayList<String> expenseInfo)
    {
        this.expenseInfo = expenseInfo;
    }

    public ArrayList<Integer> getExPlanId()
    {
        return exPlanId;
    }

    public void setExPlanId(ArrayList<Integer> exPlanId)
    {
        this.exPlanId = exPlanId;
    }

    public ArrayList<Integer> getSalesFlgList()
    {
        return salesFlgList;
    }

    public void setSalesFlgList(ArrayList<Integer> salesFlgList)
    {
        this.salesFlgList = salesFlgList;
    }

    public ArrayList<String> getOption()
    {
        return option;
    }

    public void setOption(ArrayList<String> option)
    {
        this.option = option;
    }

    public int getViewMode()
    {
        return viewMode;
    }

    public void setViewMode(int viewMode)
    {
        this.viewMode = viewMode;
    }

    public ArrayList<String> getDispStatus()
    {
        return dispStatus;
    }

    public void setDispStatus(ArrayList<String> dispStatus)
    {
        this.dispStatus = dispStatus;
    }

    public void setFrmPlanChargeSubList(ArrayList<FormOwnerRsvPlanChargeSub> frmPlanChargeSubList)
    {
        this.frmPlanChargeSubList = frmPlanChargeSubList;
    }

    public ArrayList<FormOwnerRsvPlanChargeSub> getFrmPlanChargeSubList()
    {
        return frmPlanChargeSubList;
    }

    public void setMaxNumAdult(ArrayList<String> maxNumAdult)
    {
        this.maxNumAdult = maxNumAdult;
    }

    public ArrayList<String> getMaxNumAdult()
    {
        return maxNumAdult;
    }

    public void setMaxNumChild(ArrayList<String> maxNumChild)
    {
        this.maxNumChild = maxNumChild;
    }

    public ArrayList<String> getMaxNumChild()
    {
        return maxNumChild;
    }

    public void setMinNumAdult(ArrayList<String> minNumAdult)
    {
        this.minNumAdult = minNumAdult;
    }

    public ArrayList<String> getMinNumAdult()
    {
        return minNumAdult;
    }

    public void setMinNumChild(ArrayList<String> minNumChild)
    {
        this.minNumChild = minNumChild;
    }

    public ArrayList<String> getMinNumChild()
    {
        return minNumChild;
    }

    public ArrayList<String> getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(ArrayList<String> lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    public ArrayList<Integer> getSalesStartDate()
    {
        return salesStartDate;
    }

    public void setSalesStartDate(ArrayList<Integer> salesStartDate)
    {
        this.salesStartDate = salesStartDate;
    }

    public ArrayList<Integer> getSalesEndDate()
    {
        return salesEndDate;
    }

    public void setSalesEndDate(ArrayList<Integer> salesEndDate)
    {
        this.salesEndDate = salesEndDate;
    }

    public ArrayList<Integer> getDispStartDate()
    {
        return dispStartDate;
    }

    public void setDispStartDate(ArrayList<Integer> dispStartDate)
    {
        this.dispStartDate = dispStartDate;
    }

    public ArrayList<Integer> getDispEndDate()
    {
        return dispEndDate;
    }

    public void setDispEndDate(ArrayList<Integer> dispEndDate)
    {
        this.dispEndDate = dispEndDate;
    }

}
