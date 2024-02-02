package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * ΚXσ΅mFFormNX
 */
public class FormOwnerBkoComingMonth
{
    private int                selHotelID       = 0;
    private String             errMsg           = "";
    private int                selYear          = 0;
    private int                billFlg          = 0;

    private ArrayList<String>  dateStringList   = new ArrayList<String>(); // pϊ
    private ArrayList<Integer> dateList         = new ArrayList<Integer>();

    // X
    private ArrayList<Integer> raitenCntList    = new ArrayList<Integer>(); // g
    private ArrayList<String>  raitenPointList  = new ArrayList<String>(); // ΑZ

    // ΈZ
    private ArrayList<Integer> seisanCntList    = new ArrayList<Integer>(); // g
    private ArrayList<String>  seisanPointList  = new ArrayList<String>(); // ΑZ
    private ArrayList<String>  seisanAmountList = new ArrayList<String>(); // ΰz
    private ArrayList<String>  genzanAmountList = new ArrayList<String>(); // ΈZ

    // \ρ
    private ArrayList<Integer> rsvCntList       = new ArrayList<Integer>(); // g
    private ArrayList<String>  rscAmountList    = new ArrayList<String>(); // ΰz
    private ArrayList<String>  rsvPointList     = new ArrayList<String>(); // ΑZ
    private ArrayList<String>  rsvBonusList     = new ArrayList<String>(); // {[iX}C
    private ArrayList<String>  rsvSiyouList     = new ArrayList<String>(); // gp}C
    private ArrayList<Integer> rsvAmountNumList = new ArrayList<Integer>(); // ΰz

    // ²Ώ
    private ArrayList<String>  billAmountList   = new ArrayList<String>();

    // v
    private String             sumRaitenCnt     = "";
    private String             sumRaitenPoint   = "";
    private String             sumSeisanCnt     = "";
    private String             sumSeisanPoint   = "";
    private String             sumSeisanAmount  = "";
    private String             sumGenzanAmount  = "";
    private String             sumRsvCnt        = "";
    private String             sumRsvPoint      = "";
    private String             sumRsvBonus      = "";
    private String             sumRsvSiyou      = "";
    private String             sumRsvAmount     = "";
    private String             sumChargePoint   = "";
    private String             sumBillAmount    = "";

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
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

    public ArrayList<String> getDateStringList()
    {
        return dateStringList;
    }

    public void setDateStringList(ArrayList<String> dateStringList)
    {
        this.dateStringList = dateStringList;
    }

    public ArrayList<Integer> getDateList()
    {
        return dateList;
    }

    public void setDateList(ArrayList<Integer> dateList)
    {
        this.dateList = dateList;
    }

    public ArrayList<Integer> getRaitenCntList()
    {
        return raitenCntList;
    }

    public void setRaitenCntList(ArrayList<Integer> raitenCntList)
    {
        this.raitenCntList = raitenCntList;
    }

    public ArrayList<String> getRaitenPointList()
    {
        return raitenPointList;
    }

    public void setRaitenPointList(ArrayList<String> raitenPointList)
    {
        this.raitenPointList = raitenPointList;
    }

    public ArrayList<Integer> getSeisanCntList()
    {
        return seisanCntList;
    }

    public void setSeisanCntList(ArrayList<Integer> seisanCntList)
    {
        this.seisanCntList = seisanCntList;
    }

    public ArrayList<String> getSeisanPointList()
    {
        return seisanPointList;
    }

    public void setSeisanPointList(ArrayList<String> seisanPointList)
    {
        this.seisanPointList = seisanPointList;
    }

    public ArrayList<String> getSeisanAmountList()
    {
        return seisanAmountList;
    }

    public void setSeisanAmountList(ArrayList<String> seisanAmountList)
    {
        this.seisanAmountList = seisanAmountList;
    }

    public ArrayList<String> getGenzanAmountList()
    {
        return genzanAmountList;
    }

    public void setGenzanAmountList(ArrayList<String> genzanAmountList)
    {
        this.genzanAmountList = genzanAmountList;
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
        return rscAmountList;
    }

    public void setRsvAmountList(ArrayList<String> amountList)
    {
        this.rscAmountList = amountList;
    }

    public ArrayList<String> getRsvPointList()
    {
        return rsvPointList;
    }

    public void setRsvPointList(ArrayList<String> rsvPointList)
    {
        this.rsvPointList = rsvPointList;
    }

    public ArrayList<String> getRsvBonusList()
    {
        return rsvBonusList;
    }

    public void setRsvBonusList(ArrayList<String> rsvBonusList)
    {
        this.rsvBonusList = rsvBonusList;
    }

    public ArrayList<String> getRsvSiyouList()
    {
        return rsvSiyouList;
    }

    public void setRsvSiyouList(ArrayList<String> rsvSiyouList)
    {
        this.rsvSiyouList = rsvSiyouList;
    }

    public ArrayList<String> getBillAmountList()
    {
        return billAmountList;
    }

    public void setBillAmountList(ArrayList<String> billAmountList)
    {
        this.billAmountList = billAmountList;
    }

    public String getSumRaitenCnt()
    {
        return sumRaitenCnt;
    }

    public void setSumRaitenCnt(String sumRaitenCnt)
    {
        this.sumRaitenCnt = sumRaitenCnt;
    }

    public String getSumRaitenPoint()
    {
        return sumRaitenPoint;
    }

    public void setSumRaitenPoint(String sumRaitenPoint)
    {
        this.sumRaitenPoint = sumRaitenPoint;
    }

    public String getSumSeisanCnt()
    {
        return sumSeisanCnt;
    }

    public void setSumSeisanCnt(String sumSeisanCnt)
    {
        this.sumSeisanCnt = sumSeisanCnt;
    }

    public String getSumSeisanPoint()
    {
        return sumSeisanPoint;
    }

    public void setSumSeisanPoint(String sumSeisanPoint)
    {
        this.sumSeisanPoint = sumSeisanPoint;
    }

    public String getSumSeisanAmount()
    {
        return sumSeisanAmount;
    }

    public void setSumSeisanAmount(String sumSeisanAmount)
    {
        this.sumSeisanAmount = sumSeisanAmount;
    }

    public String getSumGenzanAmount()
    {
        return sumGenzanAmount;
    }

    public void setSumGenzanAmount(String sumGenzanAmount)
    {
        this.sumGenzanAmount = sumGenzanAmount;
    }

    public String getSumRsvCnt()
    {
        return sumRsvCnt;
    }

    public void setSumRsvCnt(String sumRsvCnt)
    {
        this.sumRsvCnt = sumRsvCnt;
    }

    public String getSumRsvPoint()
    {
        return sumRsvPoint;
    }

    public void setSumRsvPoint(String sumRsvPoint)
    {
        this.sumRsvPoint = sumRsvPoint;
    }

    public String getSumRsvBonus()
    {
        return sumRsvBonus;
    }

    public void setSumRsvBonus(String sumRsvBonus)
    {
        this.sumRsvBonus = sumRsvBonus;
    }

    public String getSumRsvSiyou()
    {
        return sumRsvSiyou;
    }

    public void setSumRsvSiyou(String sumRsvSiyou)
    {
        this.sumRsvSiyou = sumRsvSiyou;
    }

    public String getSumRsvAmount()
    {
        return sumRsvAmount;
    }

    public void setSumRsvAmount(String sumRsvAmount)
    {
        this.sumRsvAmount = sumRsvAmount;
    }

    public String getSumChargePoint()
    {
        return sumChargePoint;
    }

    public void setSumChargePoint(String sumChargePoint)
    {
        this.sumChargePoint = sumChargePoint;
    }

    public String getSumBillAmount()
    {
        return sumBillAmount;
    }

    public void setSumBillAmount(String sumBillAmount)
    {
        this.sumBillAmount = sumBillAmount;
    }

    public ArrayList<String> getRscAmountList()
    {
        return rscAmountList;
    }

    public void setRscAmountList(ArrayList<String> rscAmountList)
    {
        this.rscAmountList = rscAmountList;
    }

    public ArrayList<Integer> getRsvAmountNumList()
    {
        return rsvAmountNumList;
    }

    public void setRsvAmountNumList(ArrayList<Integer> rsvAmountNumList)
    {
        this.rsvAmountNumList = rsvAmountNumList;
    }

    public int getBillFlg()
    {
        return billFlg;
    }

    public void setBillFlg(int billFlg)
    {
        this.billFlg = billFlg;
    }
}
