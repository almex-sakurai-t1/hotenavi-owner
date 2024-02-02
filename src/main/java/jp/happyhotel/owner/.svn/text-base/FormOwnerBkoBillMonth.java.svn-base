package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * 請求明細Formクラス
 */
public class FormOwnerBkoBillMonth
{
    private int                selHotelID            = 0;
    private String             selHotelName          = "";
    private int                selYearFrom           = 0;
    private int                selMonthFrom          = 0;
    private int                selYearTo             = 0;
    private int                selMonthTo            = 0;
    private String             errMsg                = "";
    private int                rsvKind               = 0;

    private ArrayList<Integer> billDateIntList       = new ArrayList<Integer>();
    private ArrayList<String>  billDateStrList       = new ArrayList<String>();

    // ハピホテタッチ
    private ArrayList<Integer> seisanCntList         = new ArrayList<Integer>(); // 組数
    private ArrayList<String>  seisanAmountList      = new ArrayList<String>(); // 金額
    private ArrayList<String>  seisanSeikyuList      = new ArrayList<String>(); // 請求

    // ハピホテ予約
    private ArrayList<Integer> rsvCntList            = new ArrayList<Integer>(); // 組数
    private ArrayList<String>  rsvAmountList         = new ArrayList<String>(); // 金額
    private ArrayList<String>  rsvSeikyuList         = new ArrayList<String>(); // 請求
    private ArrayList<String>  rsvBonusList          = new ArrayList<String>(); // 予約ボーナス

    // その他
    private ArrayList<Integer> otherCntList          = new ArrayList<Integer>(); // 組数
    private ArrayList<String>  otherSeikyuList       = new ArrayList<String>(); // 請求

    // ハピホテタッチ＋予約＋その他
    private ArrayList<String>  totalSeikyuIncomeList = new ArrayList<String>();
    private ArrayList<String>  seikyuIncomeList      = new ArrayList<String>();

    // マイル使用
    private ArrayList<String>  seisanPayList         = new ArrayList<String>(); // 支払い
    private ArrayList<String>  seikyuPayList         = new ArrayList<String>();

    // ご請求（合算）
    private ArrayList<String>  totalSeikyuList       = new ArrayList<String>();
    // 請求金額（合算）
    private ArrayList<String>  seikyuList            = new ArrayList<String>();
    // 入金金額（合算）
    private ArrayList<String>  inputChargeList       = new ArrayList<String>();
    // 入金日
    private ArrayList<String>  inputDateList         = new ArrayList<String>();
    // 入金状況
    private ArrayList<String>  inputStatusList       = new ArrayList<String>();

    // 合計
    private int                sumSeisanCnt          = 0;
    private String             sumSeisanAmount       = "";
    private String             sumSeisanSeikyu       = "";
    private String             sumSeisanPay          = "";
    private int                sumRsvCnt             = 0;
    private String             sumRsvAmount          = "";
    private String             sumRsvSeikyu          = "";
    private String             sumRsvBonus           = "";
    private int                sumOtherCnt           = 0;
    private String             sumOtherSeikyu        = "";
    private String             sumTotalSeikyu        = "";

    private String             sumTotalSeikyuIncome  = "";
    private boolean            blankCloseFlg         = false;

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public String getSelHotelName()
    {
        return selHotelName;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getSelYearFrom()
    {
        return selYearFrom;
    }

    public void setSelYearFrom(int selYear)
    {
        this.selYearFrom = selYear;
    }

    public int getRsvKind()
    {
        return rsvKind;
    }

    public void setRsvKind(int rsvKind)
    {
        this.rsvKind = rsvKind;
    }

    public ArrayList<Integer> getBillDateIntList()
    {
        return billDateIntList;
    }

    public void setBillDateIntList(ArrayList<Integer> billDateIntList)
    {
        this.billDateIntList = billDateIntList;
    }

    public ArrayList<String> getBillDateStrList()
    {
        return billDateStrList;
    }

    public void setBillDateStrList(ArrayList<String> billDateStrList)
    {
        this.billDateStrList = billDateStrList;
    }

    public ArrayList<Integer> getSeisanCntList()
    {
        return seisanCntList;
    }

    public void setSeisanCntList(ArrayList<Integer> seisanCntList)
    {
        this.seisanCntList = seisanCntList;
    }

    public ArrayList<String> getSeisanAmountList()
    {
        return seisanAmountList;
    }

    public void setSeisanAmountList(ArrayList<String> seisanAmountList)
    {
        this.seisanAmountList = seisanAmountList;
    }

    public ArrayList<String> getSeisanSeikyuList()
    {
        return seisanSeikyuList;
    }

    public void setSeisanSeikyuList(ArrayList<String> seisanSeikyuList)
    {
        this.seisanSeikyuList = seisanSeikyuList;
    }

    public ArrayList<String> getSeisanPayList()
    {
        return seisanPayList;
    }

    public void setSeisanPayList(ArrayList<String> seisanPayList)
    {
        this.seisanPayList = seisanPayList;
    }

    public ArrayList<Integer> getRsvCntList()
    {
        return rsvCntList;
    }

    public void setRsvCntList(ArrayList<Integer> rsvCntList)
    {
        this.rsvCntList = rsvCntList;
    }

    public ArrayList<String> getRsvAmountList()
    {
        return rsvAmountList;
    }

    public void setRsvAmountList(ArrayList<String> rsvAmountList)
    {
        this.rsvAmountList = rsvAmountList;
    }

    public ArrayList<String> getRsvSeikyuList()
    {
        return rsvSeikyuList;
    }

    public void setRsvSeikyuList(ArrayList<String> rsvSeikyuList)
    {
        this.rsvSeikyuList = rsvSeikyuList;
    }

    public ArrayList<String> getRsvBonusList()
    {
        return rsvBonusList;
    }

    public void setRsvBonusList(ArrayList<String> rsvBonusList)
    {
        this.rsvBonusList = rsvBonusList;
    }

    public ArrayList<Integer> getOtherCntList()
    {
        return otherCntList;
    }

    public void setOtherCntList(ArrayList<Integer> otherCntList)
    {
        this.otherCntList = otherCntList;
    }

    public ArrayList<String> getOtherSeikyuList()
    {
        return otherSeikyuList;
    }

    public void setOtherSeikyuList(ArrayList<String> otherSeikyuList)
    {
        this.otherSeikyuList = otherSeikyuList;
    }

    public ArrayList<String> getTotalSeikyuList()
    {
        return totalSeikyuList;
    }

    public void setTotalSeikyuList(ArrayList<String> totalSeikyuList)
    {
        this.totalSeikyuList = totalSeikyuList;
    }

    public int getSumSeisanCnt()
    {
        return sumSeisanCnt;
    }

    public void setSumSeisanCnt(int sumSeisanCnt)
    {
        this.sumSeisanCnt = sumSeisanCnt;
    }

    public String getSumSeisanAmount()
    {
        return sumSeisanAmount;
    }

    public void setSumSeisanAmount(String sumSeisanAmount)
    {
        this.sumSeisanAmount = sumSeisanAmount;
    }

    public String getSumSeisanSeikyu()
    {
        return sumSeisanSeikyu;
    }

    public void setSumSeisanSeikyu(String sumSeisanSeikyu)
    {
        this.sumSeisanSeikyu = sumSeisanSeikyu;
    }

    public String getSumSeisanPay()
    {
        return sumSeisanPay;
    }

    public void setSumSeisanPay(String sumSeisanPay)
    {
        this.sumSeisanPay = sumSeisanPay;
    }

    public int getSumRsvCnt()
    {
        return sumRsvCnt;
    }

    public void setSumRsvCnt(int sumRsvCnt)
    {
        this.sumRsvCnt = sumRsvCnt;
    }

    public String getSumRsvAmount()
    {
        return sumRsvAmount;
    }

    public void setSumRsvAmount(String sumRsvAmount)
    {
        this.sumRsvAmount = sumRsvAmount;
    }

    public String getSumRsvSeikyu()
    {
        return sumRsvSeikyu;
    }

    public void setSumRsvSeikyu(String sumRsvSeikyu)
    {
        this.sumRsvSeikyu = sumRsvSeikyu;
    }

    public String getSumRsvBonus()
    {
        return sumRsvBonus;
    }

    public void setSumRsvBonus(String sumRsvBonus)
    {
        this.sumRsvBonus = sumRsvBonus;
    }

    public int getSumOtherCnt()
    {
        return sumOtherCnt;
    }

    public void setSumOtherCnt(int sumOtherCnt)
    {
        this.sumOtherCnt = sumOtherCnt;
    }

    public String getSumOtherSeikyu()
    {
        return sumOtherSeikyu;
    }

    public void setSumOtherSeikyu(String sumOtherSeikyu)
    {
        this.sumOtherSeikyu = sumOtherSeikyu;
    }

    public String getSumTotalSeikyu()
    {
        return sumTotalSeikyu;
    }

    public void setSumTotalSeikyu(String sumTotalSeikyu)
    {
        this.sumTotalSeikyu = sumTotalSeikyu;
    }

    public void setSelMonthFrom(int selMonthFrom)
    {
        this.selMonthFrom = selMonthFrom;
    }

    public int getSelMonthFrom()
    {
        return selMonthFrom;
    }

    public void setSelYearTo(int selYearTo)
    {
        this.selYearTo = selYearTo;
    }

    public int getSelYearTo()
    {
        return selYearTo;
    }

    public void setSelMonthTo(int selMonthTo)
    {
        this.selMonthTo = selMonthTo;
    }

    public int getSelMonthTo()
    {
        return selMonthTo;
    }

    public void setInputChargeList(ArrayList<String> inputChargeList)
    {
        this.inputChargeList = inputChargeList;
    }

    public ArrayList<String> getInputChargeList()
    {
        return inputChargeList;
    }

    public void setInputStatusList(ArrayList<String> inputStatusList)
    {
        this.inputStatusList = inputStatusList;
    }

    public ArrayList<String> getInputStatusList()
    {
        return inputStatusList;
    }

    public void setSeikyuList(ArrayList<String> seikyuList)
    {
        this.seikyuList = seikyuList;
    }

    public ArrayList<String> getSeikyuList()
    {
        return seikyuList;
    }

    public void setInputDateList(ArrayList<String> inputDateList)
    {
        this.inputDateList = inputDateList;
    }

    public ArrayList<String> getInputDateList()
    {
        return inputDateList;
    }

    public void setTotalSeikyuIncomeList(ArrayList<String> totalSeikyuIncomeList)
    {
        this.totalSeikyuIncomeList = totalSeikyuIncomeList;
    }

    public ArrayList<String> getTotalSeikyuIncomeList()
    {
        return totalSeikyuIncomeList;
    }

    public void setSeikyuIncomeList(ArrayList<String> seikyuIncomeList)
    {
        this.seikyuIncomeList = seikyuIncomeList;
    }

    public ArrayList<String> getSeikyuIncomeList()
    {
        return seikyuIncomeList;
    }

    public void setSeikyuPayList(ArrayList<String> seikyuPayList)
    {
        this.seikyuPayList = seikyuPayList;
    }

    public ArrayList<String> getSeikyuPayList()
    {
        return seikyuPayList;
    }

    public String getSumTotalSeikyuIncome()
    {
        return sumTotalSeikyuIncome;
    }

    public void setSumTotalSeikyuIncome(String sumTotalSeikyuIncome)
    {
        this.sumTotalSeikyuIncome = sumTotalSeikyuIncome;
    }

    /**
     * BlankFlgを設定します。
     * 
     * @return BlankFlg
     */
    public void setBlankCloseFlg(boolean blankCloseFlg)
    {
        this.blankCloseFlg = blankCloseFlg;
    }

    /**
     * BlankFlgを取得します。
     * 
     * @return BlankFlg
     */
    public boolean isBlankCloseFlg()
    {
        return blankCloseFlg;
    }
}
