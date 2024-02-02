package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * カレンダー管理Formクラス
 */
public class FormOwnerRsvPlanChargeCalendar
{
    private int                                                     selHotelId   = 0;
    private String                                                  ownerHotelID = "";
    private int                                                     userId       = 0;
    private String                                                  errMsg       = "";
    private String                                                  inptErrMsg   = "";
    private String                                                  warnMsg      = "";
    private int                                                     selPlanId    = 0;
    private ArrayList<Integer>                                      planIdList   = new ArrayList<Integer>();
    private ArrayList<String>                                       planNmList   = new ArrayList<String>();
    private String                                                  selPlanNm    = "";
    private ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> monthlyList  = new ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>>();

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

    public void setMonthlyList(ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> monthlyList)
    {
        this.monthlyList = monthlyList;
    }

    public ArrayList<ArrayList<FormOwnerRsvPlanChargeCalendarSub>> getMonthlyList()
    {
        return monthlyList;
    }

    public ArrayList<Integer> getPlanIdList()
    {
        return planIdList;
    }

    public void setPlanIdList(ArrayList<Integer> planIdList)
    {
        this.planIdList = planIdList;
    }

    public ArrayList<String> getPlanNmList()
    {
        return planNmList;
    }

    public void setPlanNmList(ArrayList<String> planNmList)
    {
        this.planNmList = planNmList;
    }

    public int getSelPlanId()
    {
        return selPlanId;
    }

    public void setSelPlanId(int selPlanId)
    {
        this.selPlanId = selPlanId;
    }

    public String getSelPlanNm()
    {
        return selPlanNm;
    }

    public void setSelPlanNm(String selPlanNm)
    {
        this.selPlanNm = selPlanNm;
    }

    public String getOwnerHotelID()
    {
        return ownerHotelID;
    }

    public void setOwnerHotelID(String ownerHotelID)
    {
        this.ownerHotelID = ownerHotelID;
    }

    public String getInptErrMsg()
    {
        return inptErrMsg;
    }

    public void setInptErrMsg(String inptErrMsg)
    {
        this.inptErrMsg = inptErrMsg;
    }

    public String getWarnMsg() {
        return warnMsg;
    }

    public void setWarnMsg(String warnMsg) {
        this.warnMsg = warnMsg;
    }
}
