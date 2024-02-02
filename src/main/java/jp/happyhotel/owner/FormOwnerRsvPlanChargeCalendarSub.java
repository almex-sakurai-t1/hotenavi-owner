package jp.happyhotel.owner;

import java.util.ArrayList;

/**
 * 
 * カレンダー管理 Formクラス
 */
public class FormOwnerRsvPlanChargeCalendarSub
{
    private int                hotelId             = 0;
    private int                planId              = 0;
    private int                date                = 0;                       // 日のみ
    private int                calDate             = 0;                       // 対象年月日(YYYYMMDD)
    private int                chargeModeId        = 0;
    private int                weekId              = 0;
    private int                holidayKind         = 0;
    private String             currentYear         = "";                      // 表示対象の年
    private String             currentMonth        = "";                      // 表示対象の月
    private int                currentFlg          = 0;                       // 当日フラグ
    private int                salesFlag           = 0;                       // 販売フラグ
    private ArrayList<Integer> chargeModeIdList    = new ArrayList<Integer>();
    private ArrayList<String>  chargeModeNmList    = new ArrayList<String>();
    private String             errMsg              = "";
    private String             reserveChargeFormat = "";

    public int getHotelId()
    {
        return hotelId;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    public int getPlanId()
    {
        return planId;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public int getDate()
    {
        return date;
    }

    public void setDate(int date)
    {
        this.date = date;
    }

    public int getCalDate()
    {
        return calDate;
    }

    public void setCalDate(int calDate)
    {
        this.calDate = calDate;
    }

    public int getChargeModeId()
    {
        return chargeModeId;
    }

    public void setChargeModeId(int chargeModeId)
    {
        this.chargeModeId = chargeModeId;
    }

    public int getWeekId()
    {
        return weekId;
    }

    public void setWeekId(int weekId)
    {
        this.weekId = weekId;
    }

    public int getHolidayKind()
    {
        return holidayKind;
    }

    public void setHolidayKind(int holidayKind)
    {
        this.holidayKind = holidayKind;
    }

    public String getCurrentYear()
    {
        return currentYear;
    }

    public void setCurrentYear(String currentYear)
    {
        this.currentYear = currentYear;
    }

    public String getCurrentMonth()
    {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth)
    {
        this.currentMonth = currentMonth;
    }

    public int getCurrentFlg()
    {
        return currentFlg;
    }

    public void setCurrentFlg(int currentFlg)
    {
        this.currentFlg = currentFlg;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public ArrayList<Integer> getChargeModeIdList()
    {
        return chargeModeIdList;
    }

    public void setChargeModeIdList(ArrayList<Integer> chargeModeIdList)
    {
        this.chargeModeIdList = chargeModeIdList;
    }

    public ArrayList<String> getChargeModeNmList()
    {
        return chargeModeNmList;
    }

    public void setChargeModeNmList(ArrayList<String> chargeModeNmList)
    {
        this.chargeModeNmList = chargeModeNmList;
    }

    public int getSalesFlag()
    {
        return salesFlag;
    }

    public void setSalesFlag(int salesFlag)
    {
        this.salesFlag = salesFlag;
    }

    public String getReserveChargeFormat()
    {
        return reserveChargeFormat;
    }

    public void setReserveChargeFormat(String reserveChargeFormat)
    {
        this.reserveChargeFormat = reserveChargeFormat;
    }

}
