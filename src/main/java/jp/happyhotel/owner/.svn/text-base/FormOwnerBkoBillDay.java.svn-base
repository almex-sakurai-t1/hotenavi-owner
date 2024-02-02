package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * 日別請求明細画面Formクラス
 */
public class FormOwnerBkoBillDay
{
    private int                selHotelID       = 0;
    private String             selHotelName     = "";
    private String             errMsg           = "";
    private int                selYear          = 0;
    private int                selMonth         = 0;
    private String             simeKikan        = "";
    private int                rsvKind          = 0;

    private ArrayList<Integer> billDateList     = new ArrayList<Integer>(); // 請求年月
    private ArrayList<String>  usageDateStrList = new ArrayList<String>(); // 利用日
    private ArrayList<Integer> usageDateIntList = new ArrayList<Integer>();

    // 精算
    private ArrayList<Integer> seisanCntList    = new ArrayList<Integer>(); // 組数
    private ArrayList<String>  seisanAmountList = new ArrayList<String>(); // 金額
    private ArrayList<String>  seisanSeikyuList = new ArrayList<String>(); // 請求
    private ArrayList<Integer> seisanPayCntList = new ArrayList<Integer>(); // 支払い
    private ArrayList<String>  seisanPayList    = new ArrayList<String>(); // 支払い

    // 予約
    private ArrayList<Integer> rsvCntList       = new ArrayList<Integer>(); // 組数
    private ArrayList<String>  rsvAmountList    = new ArrayList<String>(); // 金額
    private ArrayList<String>  rsvSeikyuList    = new ArrayList<String>(); // 請求
    private ArrayList<String>  rsvBonusList     = new ArrayList<String>(); // 予約ボーナス

    // その他
    private ArrayList<Integer> otherCntList     = new ArrayList<Integer>(); // 組数
    private ArrayList<String>  otherSeikyuList  = new ArrayList<String>(); // 請求

    // ご請求
    private ArrayList<String>  totalSeikyuList  = new ArrayList<String>();

    // 合計
    private int                sumSeisanCnt     = 0;
    private String             sumSeisanAmount  = "";
    private String             sumSeisanSeikyu  = "";
    private int                sumSeisanPayCnt  = 0;
    private String             sumSeisanPay     = "";
    private int                sumRsvCnt        = 0;
    private String             sumRsvAmount     = "";
    private String             sumRsvSeikyu     = "";
    private String             sumRsvBonus      = "";
    private int                sumOtherCnt      = 0;
    private String             sumOtherSeikyu   = "";
    private String             sumTotalSeikyu   = "";
    private boolean            blankCloseFlg    = false;

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

    public int getSelYear()
    {
        return selYear;
    }

    public void setSelYear(int selYear)
    {
        this.selYear = selYear;
    }

    public int getSelMonth()
    {
        return selMonth;
    }

    public void setSelMonth(int selMonth)
    {
        this.selMonth = selMonth;
    }

    public String getSimeKikan()
    {
        return simeKikan;
    }

    public void setSimeKikan(String simeKikan)
    {
        this.simeKikan = simeKikan;
    }

    public ArrayList<String> getUsageDateStrList()
    {
        return usageDateStrList;
    }

    public void setUsageDateStrList(ArrayList<String> usageDateStrList)
    {
        this.usageDateStrList = usageDateStrList;
    }

    public ArrayList<Integer> getUsageDateIntList()
    {
        return usageDateIntList;
    }

    public void setUsageDateIntList(ArrayList<Integer> usageDateIntList)
    {
        this.usageDateIntList = usageDateIntList;
    }

    public ArrayList<Integer> getSeisanCntList()
    {
        return seisanCntList;
    }

    public void setSeisanCntList(ArrayList<Integer> seisanCntList)
    {
        this.seisanCntList = seisanCntList;
    }

    public ArrayList<Integer> getSeisanPayCntList()
    {
        return seisanPayCntList;
    }

    public void setSeisanPayCntList(ArrayList<Integer> seisanPayCntList)
    {
        this.seisanPayCntList = seisanPayCntList;
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

    public int getSumSeisanPayCnt()
    {
        return sumSeisanPayCnt;
    }

    public void setSumSeisanPayCnt(int sumSeisanPayCnt)
    {
        this.sumSeisanPayCnt = sumSeisanPayCnt;
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

    public ArrayList<Integer> getBillDateList()
    {
        return billDateList;
    }

    public void setBillDateList(ArrayList<Integer> billDateList)
    {
        this.billDateList = billDateList;
    }

    public int getRsvKind()
    {
        return rsvKind;
    }

    public void setRsvKind(int rsvKind)
    {
        this.rsvKind = rsvKind;
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
