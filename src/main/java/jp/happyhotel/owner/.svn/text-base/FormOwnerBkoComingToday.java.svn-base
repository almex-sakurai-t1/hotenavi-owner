package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * ハピホテマイル履歴Formクラス
 */
public class FormOwnerBkoComingToday
{

    private int                selHotelID         = 0;
    private String             errMsg             = "";

    // 検索条件
    private int                yearFrom           = 0;
    private int                monthFrom          = 0;
    private int                dateFrom           = 0;
    private int                yearTo             = 0;
    private int                monthTo            = 0;
    private int                dateTo             = 0;
    private int                ciTimeFrom         = 0;
    private int                ciTimeTo           = 0;
    private int                selCustomerId      = 0;

    // ヘッダー情報
    private ArrayList<Integer> slipNoList         = new ArrayList<Integer>();
    private ArrayList<String>  usageDate          = new ArrayList<String>();
    private ArrayList<String>  usageTime          = new ArrayList<String>();
    private ArrayList<String>  personNm           = new ArrayList<String>();
    private ArrayList<String>  seq                = new ArrayList<String>();
    private ArrayList<Integer> htSlipNo           = new ArrayList<Integer>();
    private ArrayList<String>  accrecvAmount      = new ArrayList<String>();
    private ArrayList<Integer> customerId         = new ArrayList<Integer>();

    // 明細情報
    private ArrayList<String>  raitenList         = new ArrayList<String>();
    private ArrayList<String>  huyoList           = new ArrayList<String>();
    private ArrayList<String>  siyouList          = new ArrayList<String>();
    private ArrayList<String>  reserveList        = new ArrayList<String>();
    private ArrayList<String>  bonusList          = new ArrayList<String>();
    private ArrayList<String>  addAmountList      = new ArrayList<String>();
    private ArrayList<String>  subtractAmountList = new ArrayList<String>();
    private ArrayList<String>  closingList        = new ArrayList<String>();

    // 合計欄
    private String             sumUsageCharrge    = "";
    private String             sumAddAmount       = "";
    private String             sumSubtractAmount  = "";

    // ページャー
    private int                pageMax            = 0;                       // 予約件数
    private int                pageAct            = 0;                       // 現在のページ数
    private int                pageSt             = 0;                       // 現在表示予約件数(先頭)
    private int                pageEd             = 0;                       // 現在表示予約件数(最後)
    private String             pageLink           = "";
    private String             pageRecords        = "";
    private int                recCnt             = 0;
    private int                recMaxCnt          = 0;

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

    public int getCiTimeFrom()
    {
        return ciTimeFrom;
    }

    public void setCiTimeFrom(int ciTimeFrom)
    {
        this.ciTimeFrom = ciTimeFrom;
    }

    public int getCiTimeTo()
    {
        return ciTimeTo;
    }

    public void setCiTimeTo(int ciTimeTo)
    {
        this.ciTimeTo = ciTimeTo;
    }

    public ArrayList<String> getPersonNm()
    {
        return personNm;
    }

    public void setPersonNm(ArrayList<String> personNm)
    {
        this.personNm = personNm;
    }

    public ArrayList<String> getSeq()
    {
        return seq;
    }

    public void setSeq(ArrayList<String> seq)
    {
        this.seq = seq;
    }

    public ArrayList<String> getAccrecvAmount()
    {
        return accrecvAmount;
    }

    public void setAccrecvAmount(ArrayList<String> accrecvAmount)
    {
        this.accrecvAmount = accrecvAmount;
    }

    public ArrayList<Integer> getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(ArrayList<Integer> customerId)
    {
        this.customerId = customerId;
    }

    public int getYearFrom()
    {
        return yearFrom;
    }

    public void setYearFrom(int yearFrom)
    {
        this.yearFrom = yearFrom;
    }

    public int getMonthFrom()
    {
        return monthFrom;
    }

    public void setMonthFrom(int monthFrom)
    {
        this.monthFrom = monthFrom;
    }

    public int getDateFrom()
    {
        return dateFrom;
    }

    public void setDateFrom(int dateFrom)
    {
        this.dateFrom = dateFrom;
    }

    public int getYearTo()
    {
        return yearTo;
    }

    public void setYearTo(int yeraTo)
    {
        this.yearTo = yeraTo;
    }

    public int getMonthTo()
    {
        return monthTo;
    }

    public void setMonthTo(int monthTo)
    {
        this.monthTo = monthTo;
    }

    public int getDateTo()
    {
        return dateTo;
    }

    public void setDateTo(int dateTo)
    {
        this.dateTo = dateTo;
    }

    public ArrayList<String> getUsageDate()
    {
        return usageDate;
    }

    public void setUsageDate(ArrayList<String> usageDate)
    {
        this.usageDate = usageDate;
    }

    public ArrayList<String> getUsageTime()
    {
        return usageTime;
    }

    public void setUsageTime(ArrayList<String> usageTime)
    {
        this.usageTime = usageTime;
    }

    public ArrayList<Integer> getSlipNoList()
    {
        return slipNoList;
    }

    public void setSlipNoList(ArrayList<Integer> slipNoList_h)
    {
        this.slipNoList = slipNoList_h;
    }

    public ArrayList<String> getAddAmountList()
    {
        return addAmountList;
    }

    public void setAddAmountList(ArrayList<String> addAmountList)
    {
        this.addAmountList = addAmountList;
    }

    public ArrayList<String> getSubtractAmountList()
    {
        return subtractAmountList;
    }

    public void setSubtractAmountList(ArrayList<String> subtractAmountList)
    {
        this.subtractAmountList = subtractAmountList;
    }

    public String getSumUsageCharrge()
    {
        return sumUsageCharrge;
    }

    public void setSumUsageCharrge(String sumUsageCharrge)
    {
        this.sumUsageCharrge = sumUsageCharrge;
    }

    public String getSumAddAmount()
    {
        return sumAddAmount;
    }

    public void setSumAddAmount(String sumAddAmount)
    {
        this.sumAddAmount = sumAddAmount;
    }

    public String getSumSubtractAmount()
    {
        return sumSubtractAmount;
    }

    public void setSumSubtractAmount(String sumSubtractAmount)
    {
        this.sumSubtractAmount = sumSubtractAmount;
    }

    public ArrayList<Integer> getHtSlipNo()
    {
        return htSlipNo;
    }

    public void setHtSlipNo(ArrayList<Integer> htSlipNo)
    {
        this.htSlipNo = htSlipNo;
    }

    public int getPageAct()
    {
        return pageAct;
    }

    public void setPageAct(int pageAct)
    {
        this.pageAct = pageAct;
    }

    public int getPageSt()
    {
        return pageSt;
    }

    public void setPageSt(int pageSt)
    {
        this.pageSt = pageSt;
    }

    public int getPageEd()
    {
        return pageEd;
    }

    public void setPageEd(int pageEd)
    {
        this.pageEd = pageEd;
    }

    public int getRecCnt()
    {
        return recCnt;
    }

    public void setRecCnt(int recCnt)
    {
        this.recCnt = recCnt;
    }

    public String getPageRecords()
    {
        return pageRecords;
    }

    public void setPageRecords(String pageRecords)
    {
        this.pageRecords = pageRecords;
    }

    public int getPageMax()
    {
        return pageMax;
    }

    public void setPageMax(int pageMax)
    {
        this.pageMax = pageMax;
    }

    public String getPageLink()
    {
        return pageLink;
    }

    public void setPageLink(String pageLink)
    {
        this.pageLink = pageLink;
    }

    public int getRecMaxCnt()
    {
        return recMaxCnt;
    }

    public void setRecMaxCnt(int recMaxCnt)
    {
        this.recMaxCnt = recMaxCnt;
    }

    public int getSelCustomerId()
    {
        return selCustomerId;
    }

    public void setSelCustomerId(int selCustomerId)
    {
        this.selCustomerId = selCustomerId;
    }

    public ArrayList<String> getClosingList()
    {
        return closingList;
    }

    public void setClosingList(ArrayList<String> closingList)
    {
        this.closingList = closingList;
    }

    public ArrayList<String> getRaitenList()
    {
        return raitenList;
    }

    public void setRaitenList(ArrayList<String> raitenList)
    {
        this.raitenList = raitenList;
    }

    public ArrayList<String> getHuyoList()
    {
        return huyoList;
    }

    public void setHuyoList(ArrayList<String> huyoList)
    {
        this.huyoList = huyoList;
    }

    public ArrayList<String> getSiyouList()
    {
        return siyouList;
    }

    public void setSiyouList(ArrayList<String> siyouList)
    {
        this.siyouList = siyouList;
    }

    public ArrayList<String> getReserveList()
    {
        return reserveList;
    }

    public void setReserveList(ArrayList<String> reserveList)
    {
        this.reserveList = reserveList;
    }

    public ArrayList<String> getBonusList()
    {
        return bonusList;
    }

    public void setBonusList(ArrayList<String> bonusList)
    {
        this.bonusList = bonusList;
    }
}
