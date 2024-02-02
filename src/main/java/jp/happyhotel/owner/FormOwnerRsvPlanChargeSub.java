package jp.happyhotel.owner;

/*
 * ÉvÉâÉìï óøã‡ê›íËSubFormÉNÉâÉX
 */
public class FormOwnerRsvPlanChargeSub
{
    private int    check        = 0;
    private int    planId       = 0;
    private int    chargeModeId = 0;
    private String chargeModeNm = "";
    private String remarks      = "";
    private String ciTimeFromHH = "";
    private String ciTimeFromMM = "";
    private String ciTimeToHH   = "";
    private String ciTimeToMM   = "";
    private String coTimeHH     = "";
    private String coTimeMM     = "";
    private String adultTwo     = "";
    private String adultOne     = "";
    private String adultAdd     = "";
    private String childAdd     = "";
    private String coRemarks    = "";
    private int    registStatus = 0; // ÉvÉâÉìï óøã‡”∞ƒﬁìoò^èÛë‘(0Å®ñ¢ìoò^,1Å®ìoò^çœ)
    private int    weekStatus   = 0; // ÉJÉåÉìÉ_Å[ójì˙ÉXÉeÅ[É^ÉX
    private int    coKind       = 0;

    public int getCheck()
    {
        return check;
    }

    public void setCheck(int check)
    {
        this.check = check;
    }

    public int getPlanId()
    {
        return planId;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
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

    public String getCiTimeFromHH()
    {
        return ciTimeFromHH;
    }

    public void setCiTimeFromHH(String ciTimeFromHH)
    {
        this.ciTimeFromHH = ciTimeFromHH;
    }

    public String getCiTimeFromMM()
    {
        return ciTimeFromMM;
    }

    public void setCiTimeFromMM(String ciTimeFromMM)
    {
        this.ciTimeFromMM = ciTimeFromMM;
    }

    public String getCiTimeToHH()
    {
        return ciTimeToHH;
    }

    public void setCiTimeToHH(String ciTimeToHH)
    {
        this.ciTimeToHH = ciTimeToHH;
    }

    public String getCiTimeToMM()
    {
        return ciTimeToMM;
    }

    public void setCiTimeToMM(String ciTimeToMM)
    {
        this.ciTimeToMM = ciTimeToMM;
    }

    public String getCoTimeHH()
    {
        return coTimeHH;
    }

    public void setCoTimeHH(String coTimeHH)
    {
        this.coTimeHH = coTimeHH;
    }

    public String getCoTimeMM()
    {
        return coTimeMM;
    }

    public void setCoTimeMM(String coTimeMM)
    {
        this.coTimeMM = coTimeMM;
    }

    public String getAdultTwo()
    {
        return adultTwo;
    }

    public void setAdultTwo(String adultTwo)
    {
        this.adultTwo = adultTwo;
    }

    public String getAdultOne()
    {
        return adultOne;
    }

    public void setAdultOne(String adultOne)
    {
        this.adultOne = adultOne;
    }

    public String getAdultAdd()
    {
        return adultAdd;
    }

    public void setAdultAdd(String adultAdd)
    {
        this.adultAdd = adultAdd;
    }

    public String getChildAdd()
    {
        return childAdd;
    }

    public void setChildAdd(String childAdd)
    {
        this.childAdd = childAdd;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getCoRemarks()
    {
        return coRemarks;
    }

    public void setCoRemarks(String coRemarks)
    {
        this.coRemarks = coRemarks;
    }

    public void setRegistStatus(int registStatus)
    {
        this.registStatus = registStatus;
    }

    public int getRegistStatus()
    {
        return registStatus;
    }

    public void setWeekStatus(int weekStatus)
    {
        this.weekStatus = weekStatus;
    }

    public int getWeekStatus()
    {
        return weekStatus;
    }

    public int getCoKind()
    {
        return coKind;
    }

    public void setCoKind(int coKind)
    {
        this.coKind = coKind;
    }

}
