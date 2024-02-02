package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * ì˙ï óàìXèÛãµämîFFormÉNÉâÉX
 */
public class FormOwnerBkoComingDay
{
    private int                selHotelID       = 0;
    private String             errMsg           = "";
    private int                selYear          = 0;
    private int                selMonth         = 0;
    private int                billFlg          = 0;

    private ArrayList<String>  dateStringList   = new ArrayList<String>(); // óòópì˙
    private ArrayList<Integer> dateList         = new ArrayList<Integer>();

    // óàìX
    private ArrayList<Integer> raitenCntList    = new ArrayList<Integer>(); // ëgêî
    private ArrayList<String>  raitenPointList  = new ArrayList<String>(); // â¡éZ

    // ê¥éZ
    private ArrayList<Integer> seisanCntList    = new ArrayList<Integer>(); // ëgêî
    private ArrayList<String>  seisanPointList  = new ArrayList<String>(); // â¡éZ
    private ArrayList<String>  seisanAmountList = new ArrayList<String>(); // ã‡äz
    private ArrayList<String>  genzanAmountList = new ArrayList<String>(); // å∏éZ

    // ó\ñÒ
    private ArrayList<Integer> rsvCntList       = new ArrayList<Integer>(); // ëgêî
    private ArrayList<String>  rsvChargeList    = new ArrayList<String>(); // ã‡äz
    private ArrayList<String>  rsvPointList     = new ArrayList<String>(); // â¡éZ
    private ArrayList<String>  rsvBonusList     = new ArrayList<String>(); // É{Å[ÉiÉXÉ}ÉCÉã
    private ArrayList<String>  rsvSiyouList     = new ArrayList<String>(); // égópÉ}ÉCÉã

    // Ç≤êøãÅ
    private ArrayList<String>  billAmountList   = new ArrayList<String>();

    // çáåv
    // óàìX
    private String             sumRaitenCnt     = "";                      // ëgêî
    private String             sumRaitenPoint   = "";                      // â¡éZ

    // ê∏éZ
    private String             sumSeisanCnt     = "";                      // ëgêî
    private String             sumSeisanPoint   = "";                      // ã‡äz
    private String             sumSeisanAmount  = "";                      // â¡éZ
    private String             sumGenzanAmount  = "";                      // å∏éZ

    // ó\ñÒ
    private String             sumRsvCnt        = "";                      // ëgêî
    private String             sumRsvPoint      = "";                      // ã‡äz
    private String             sumRsvBonus      = "";
    private String             sumRsvSiyou      = "";
    private String             sumRsvAmount     = "";                      // â¡éZ

    private String             sumChargePoint   = "";

    // Ç≤êøãÅ
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

    public int getSelMonth()
    {
        return selMonth;
    }

    public void setSelMonth(int selMonth)
    {
        this.selMonth = selMonth;
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

    public ArrayList<String> getRsvChargeList()
    {
        return rsvChargeList;
    }

    public void setRsvChargeList(ArrayList<String> rsvChargeList)
    {
        this.rsvChargeList = rsvChargeList;
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
