package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * クレジット明細（オーナーサイト）Formクラス
 */
public class FormOwnerBkoCreditDetail
{
    private int                selHotelID        = 0;
    private String             selHotenaviID     = "";
    private String             selHotelName      = "";
    private int                selYearFrom       = 0;
    private int                selMonthFrom      = 0;
    private int                selYearTo         = 0;
    private int                selMonthTo        = 0;
    private int                amountTotal       = 0;
    private int                creditFeeTotal    = 0;
    private int                amountSum         = 0;
    // フォームからの年月範囲
    private ArrayList<Integer> creditDateIntList = new ArrayList<Integer>();
    private ArrayList<String>  creditDateStrList = new ArrayList<String>();

    // newRsvDB.hh_rsv_creditよりセット
    private ArrayList<String>  reserveNoList     = new ArrayList<String>(); // 予約番号
    private ArrayList<String>  tranidList        = new ArrayList<String>(); // トランザクションID
    private ArrayList<String>  approvedList      = new ArrayList<String>(); // 承認番号
    private ArrayList<String>  forwardedList     = new ArrayList<String>(); // 仕向先
    private ArrayList<Integer> amountList        = new ArrayList<Integer>(); // 請求金額
    private ArrayList<Integer> creditFeeList     = new ArrayList<Integer>(); // クレジット手数料金額
    private ArrayList<Integer> salesFlagList     = new ArrayList<Integer>(); // 売上フラグ
    private ArrayList<Integer> salesDateList     = new ArrayList<Integer>(); // 売上日付
    private ArrayList<Integer> salesTimeList     = new ArrayList<Integer>(); // 売上時刻
    private ArrayList<Integer> delFlagList       = new ArrayList<Integer>(); // 削除フラグ
    private ArrayList<Integer> delDateList       = new ArrayList<Integer>(); // 削除日付
    private ArrayList<Integer> delTimeList       = new ArrayList<Integer>(); // 削除時刻

    // newRsvDB.hh_rsv_reserveよりセット
    private ArrayList<Integer> reserveSubNoList  = new ArrayList<Integer>(); // 予約サブNo
    private ArrayList<Integer> reserveDateList   = new ArrayList<Integer>(); // 予約日
    private ArrayList<String>  nameList          = new ArrayList<String>(); // 予約者名（name_last&" "&name_first)
    private ArrayList<Integer> statusList        = new ArrayList<Integer>(); // ステータス
    private ArrayList<Integer> noshowList        = new ArrayList<Integer>(); // ノーショー
    private ArrayList<Integer> extFlagList       = new ArrayList<Integer>(); // 予約の区別(0:ハピホテからの予約1:lvjからの予約2:OTAからの予約)

    // hh_user_data_indexより
    private ArrayList<Integer> userSeqList       = new ArrayList<Integer>(); // ホテルごとのユーザーID

    private String             errMsg            = "";                      //
    private int                pageMax           = 0;                       // クレジット件数
    private int                pageAct           = 0;                       // 現在のページ数
    private int                pageSt            = 0;                       // 現在表示件数(先頭)
    private int                pageEd            = 0;                       // 現在表示件数(最後)
    private String             pageLink          = "";
    private int                recCnt            = 0;
    private String             pageRecords       = "";
    private boolean            blankCloseFlg     = false;
    private int                rsvKind           = 0;

    public int getSelYearFrom()
    {
        return selYearFrom;
    }

    public int getSelMonthFrom()
    {
        return selMonthFrom;
    }

    public int getSelYearTo()
    {
        return selYearTo;
    }

    public int getSelMonthTo()
    {
        return selMonthTo;
    }

    public void setSelYearFrom(int selYear)
    {
        this.selYearFrom = selYear;
    }

    public void setSelMonthFrom(int selMonthFrom)
    {
        this.selMonthFrom = selMonthFrom;
    }

    public void setSelYearTo(int selYearTo)
    {
        this.selYearTo = selYearTo;
    }

    public void setSelMonthTo(int selMonthTo)
    {
        this.selMonthTo = selMonthTo;
    }

    public ArrayList<Integer> getCreditDateIntList()
    {
        return creditDateIntList;
    }

    public void setCreditDateIntList(ArrayList<Integer> creditDateIntList)
    {
        this.creditDateIntList = creditDateIntList;
    }

    public ArrayList<String> getCreditDateStrList()
    {
        return creditDateStrList;
    }

    public void setCreditDateStrList(ArrayList<String> creditDateStrList)
    {
        this.creditDateStrList = creditDateStrList;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public String getPageRecords()
    {
        return this.pageRecords;
    }

    public ArrayList<String> getReserveNoList()
    {
        return reserveNoList;
    }

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public String getSelHotenaviID()
    {
        return selHotenaviID;
    }

    public String getSelHotelName()
    {
        return selHotelName;
    }

    public int getPageMax()
    {
        return pageMax;
    }

    public int getPageAct()
    {
        return pageAct;
    }

    public int getPageSt()
    {
        return this.pageSt;
    }

    public int getPageEd()
    {
        return this.pageEd;
    }

    public String getPageLink()
    {
        return pageLink;
    }

    public int getRecCnt()
    {
        return this.recCnt;
    }

    public ArrayList<Integer> getSalesDateList()
    {
        return salesDateList;
    }

    public ArrayList<Integer> getSalesTimeList()
    {
        return salesTimeList;
    }

    public ArrayList<Integer> getUserSeqList()
    {
        return userSeqList;
    }

    public ArrayList<String> getNameList()
    {
        return nameList;
    }

    public ArrayList<Integer> getAmountList()
    {
        return amountList;
    }

    public ArrayList<String> getApprovedList()
    {
        return approvedList;
    }

    public ArrayList<String> getForwardedList()
    {
        return forwardedList;
    }

    public ArrayList<String> getTranidList()
    {
        return tranidList;
    }

    public ArrayList<Integer> getReserveDateList()
    {
        return reserveDateList;
    }

    public ArrayList<Integer> getCreditFeeList()
    {
        return creditFeeList;
    }

    public ArrayList<Integer> getReserveSubNoList()
    {
        return reserveSubNoList;
    }

    public ArrayList<Integer> getStatusList()
    {
        return statusList;
    }

    public ArrayList<Integer> getNoshowList()
    {
        return noshowList;
    }

    public ArrayList<Integer> getExtFlagList()
    {
        return extFlagList;
    }

    public ArrayList<Integer> getSalesFlagList()
    {
        return salesFlagList;
    }

    public ArrayList<Integer> getDelDateList()
    {
        return delDateList;
    }

    public ArrayList<Integer> getDelTimeList()
    {
        return delTimeList;
    }

    public ArrayList<Integer> getDelFlagList()
    {
        return delFlagList;
    }

    /**
     * 
     * setter
     * 
     **/

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public void setPageAct(int pageact)
    {
        this.pageAct = pageact;
    }

    public void setPageLink(String pageLink)
    {
        this.pageLink = pageLink;
    }

    public void setPageMax(int pagemax)
    {
        this.pageMax = pagemax;
    }

    public void setPageSt(int pagest)
    {
        this.pageSt = pagest;
    }

    public void setPageEd(int pageed)
    {
        this.pageEd = pageed;
    }

    public void setPageRecords(String pagerecords)
    {
        this.pageRecords = pagerecords;
    }

    public void setReserveNoList(ArrayList<String> reserveNoList)
    {
        this.reserveNoList = reserveNoList;
    }

    public void setRecCnt(int reccnt)
    {
        this.recCnt = reccnt;
    }

    public void setNameList(ArrayList<String> nameList)
    {
        this.nameList = nameList;
    }

    public void setSalesDateList(ArrayList<Integer> salesDateList)
    {
        this.salesDateList = salesDateList;
    }

    public void setSalesTimeList(ArrayList<Integer> salesTimeList)
    {
        this.salesTimeList = salesTimeList;
    }

    public void setUserSeqList(ArrayList<Integer> userSeqList)
    {
        this.userSeqList = userSeqList;
    }

    public void setAmountList(ArrayList<Integer> amountList)
    {
        this.amountList = amountList;
    }

    public void setApprovedList(ArrayList<String> approvedList)
    {
        this.approvedList = approvedList;
    }

    public void setForwardedList(ArrayList<String> forwardedList)
    {
        this.forwardedList = forwardedList;
    }

    public void setTranidList(ArrayList<String> tranidList)
    {
        this.tranidList = tranidList;
    }

    public void setReserveDateList(ArrayList<Integer> reserveDateList)
    {
        this.reserveDateList = reserveDateList;
    }

    public void setCreditFeeList(ArrayList<Integer> creditFeeList)
    {
        this.creditFeeList = creditFeeList;
    }

    public void setReserveSubNoList(ArrayList<Integer> reserveSubNoList)
    {
        this.reserveSubNoList = reserveSubNoList;
    }

    public void setStatusList(ArrayList<Integer> statusList)
    {
        this.statusList = statusList;
    }

    public void setNoshowList(ArrayList<Integer> noshowList)
    {
        this.noshowList = noshowList;
    }

    public void setExtFlagList(ArrayList<Integer> extFlagList)
    {
        this.extFlagList = extFlagList;
    }

    public void setSalesFlagList(ArrayList<Integer> salesFlagList)
    {
        this.salesFlagList = salesFlagList;
    }

    public void setDelDateList(ArrayList<Integer> delDateList)
    {
        this.delDateList = delDateList;
    }

    public void setDelTimeList(ArrayList<Integer> delTimeList)
    {
        this.delTimeList = delTimeList;
    }

    public void setDelFlagList(ArrayList<Integer> delFlagList)
    {
        this.delFlagList = delFlagList;
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

    public void setAmountTotal(int amountTotal)
    {
        this.amountTotal = amountTotal;
    }

    public void setCreditFeeTotal(int creditFeeTotal)
    {
        this.creditFeeTotal = creditFeeTotal;
    }

    public int getAmountTotal()
    {
        return amountTotal;
    }

    public int getCreditFeeTotal()
    {
        return creditFeeTotal;
    }

    public void setAmountSum(int amountSum)
    {
        this.amountSum = amountSum;
    }

    public int getAmountSum()
    {
        return amountSum;
    }

    public int getRsvKind()
    {
        return rsvKind;
    }

    public void setRsvKind(int rsvKind)
    {
        this.rsvKind = rsvKind;
    }
}
