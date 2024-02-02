package jp.happyhotel.owner;

/*
 * プラン設定オプションSubFormクラス
 */
public class FormOwnerRsvPlanOptionSub
{
    private int    check       = 0;
    private int    planID      = 0;
    private int    optionID    = 0;
//    private int    optionSubID = 0;
    private String optionNm    = "";
    private String optionSubNm = "";
    private int    optionFlg   = 0; // 0:通常、1:必須
    private String errMsg      = "";

    public int getCheck()
    {
        return check;
    }

    public void setCheck(int check)
    {
        this.check = check;
    }

    public int getPlanID()
    {
        return planID;
    }

    public void setPlanID(int planID)
    {
        this.planID = planID;
    }

    public int getOptionID()
    {
        return optionID;
    }

    public void setOptionID(int optionID)
    {
        this.optionID = optionID;
    }

    public int getOptionFlg()
    {
        return optionFlg;
    }

    public void setOptionFlg(int optionFlg)
    {
        this.optionFlg = optionFlg;
    }

    public String getOptionNm()
    {
        return optionNm;
    }

    public void setOptionNm(String optionNm)
    {
        this.optionNm = optionNm;
    }

    public String getOptionSubNm()
    {
        return optionSubNm;
    }

    public void setOptionSubNm(String optionSubNm)
    {
        this.optionSubNm = optionSubNm;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

}
