package jp.happyhotel.owner;

/**
 * 
 * カレンダー Formクラス
 */
public class FormOwnerRsvManageCalendar
{
    private int     hotelId              = 0;
    private int     planId               = 0;
    private int     date                 = 0;    // 日のみ
    private int     calDate              = 0;    // 対象年月日(YYYYMMDD)
    private int     chargeModeId         = 0;
    private String  chargeModeNm         = "";
    private int     weekId               = 0;
    private int     allRoomNum           = 0;
    private int     vacancyRoomNum       = 0;
    private int     adultTwoCharge       = 0;
    private int     holidayKind          = 0;
    private String  currentYear          = "";   // 表示対象の年
    private String  currentMonth         = "";   // 表示対象の月
    private int     currentFlg           = 0;    // 当日フラグ
    private String  rsvJotai             = "";   // 受付状態
    private String  errMsg               = "";
    private String  adultTwoChargeFormat = "";
    private String  reserveChargeFormat  = "";
    private String  rsvJotaiMark         = "";   // 受付状態マーク
    private int     rsvJotaiFlg          = 0;    // 受付状態フラグ(0:受付終了、1:受付中)
    private boolean rsvPremiumOnlyFlg    = false; // プレミアム会員限定有効日
    private int     salesFlag            = 0;

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

    public String getChargeModeNm()
    {
        return chargeModeNm;
    }

    public void setChargeModeNm(String chargeModeNm)
    {
        this.chargeModeNm = chargeModeNm;
    }

    public int getWeekId()
    {
        return weekId;
    }

    public void setWeekId(int weekId)
    {
        this.weekId = weekId;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getAdultTwoCharge()
    {
        return adultTwoCharge;
    }

    public void setAdultTwoCharge(int adultTwoCharge)
    {
        this.adultTwoCharge = adultTwoCharge;
    }

    public String getAdultTwoChargeFormat()
    {
        return adultTwoChargeFormat;
    }

    public void setAdultTwoChargeFormat(String adultTwoChargeFormat)
    {
        this.adultTwoChargeFormat = adultTwoChargeFormat;
    }

    public int getDate()
    {
        return date;
    }

    public void setDate(int date)
    {
        this.date = date;
    }

    public int getAllRoomNum()
    {
        return allRoomNum;
    }

    public void setAllRoomNum(int allRoomNum)
    {
        this.allRoomNum = allRoomNum;
    }

    public int getVacancyRoomNum()
    {
        return vacancyRoomNum;
    }

    public void setVacancyRoomNum(int vacancyRoomNum)
    {
        this.vacancyRoomNum = vacancyRoomNum;
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

    public String getRsvJotai()
    {
        return rsvJotai;
    }

    public void setRsvJotai(String rsvJotai)
    {
        this.rsvJotai = rsvJotai;
    }

    public String getRsvJotaiMark()
    {
        return rsvJotaiMark;
    }

    public void setRsvJotaiMark(String rsvJotaiMark)
    {
        this.rsvJotaiMark = rsvJotaiMark;
    }

    public int getRsvJotaiFlg()
    {
        return rsvJotaiFlg;
    }

    public void setRsvJotaiFlg(int rsvJotaiFlg)
    {
        this.rsvJotaiFlg = rsvJotaiFlg;
    }

    public int getSalesFlag()
    {
        return salesFlag;
    }

    public void setSalesFlag(int salesFlag)
    {
        this.salesFlag = salesFlag;
    }

    public void setRsvPremiumOnlyFlg(boolean rsvPremiumOnlyFlg)
    {
        this.rsvPremiumOnlyFlg = rsvPremiumOnlyFlg;
    }

    public boolean getRsvPremiumOnlyFlg()
    {
        return rsvPremiumOnlyFlg;
    }

    public void setReserveChargeFormat(String reserveChargeFormat)
    {
        this.reserveChargeFormat = reserveChargeFormat;
    }

    public String getReserveChargeFormat()
    {
        return reserveChargeFormat;
    }

}
