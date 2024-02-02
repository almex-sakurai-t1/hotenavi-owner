package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * 予約一覧（ＰＣ）Formクラス
 */
public class FormOwnerRsvCreditList
{
    private int                selHotelID         = 0;
    private String             selHotenaviID      = "";
    private String             selHotelName       = "";
    private ArrayList<String>  reserveNoList      = new ArrayList<String>(); // 予約番号
    private ArrayList<String>  reserveDateList    = new ArrayList<String>(); // 予約日
    private ArrayList<Integer> reserveSeqList     = new ArrayList<Integer>(); // 売上データシーケンス番号
    private ArrayList<String>  reserveEsttimeList = new ArrayList<String>(); // 到着予定時刻
    private ArrayList<String>  spidList           = new ArrayList<String>(); // 対象SPID
    private ArrayList<String>  salesDateList      = new ArrayList<String>(); // 売上日付
    private ArrayList<Integer> generateDateList   = new ArrayList<Integer>(); // 発生日付
    private ArrayList<Integer> generateTimeList   = new ArrayList<Integer>(); // 発生時刻
    private ArrayList<String>  salesnameList      = new ArrayList<String>(); // 屋号(請求名称)
    private ArrayList<Integer> amountList         = new ArrayList<Integer>(); // 請求金額
    private ArrayList<Integer> approvenoList      = new ArrayList<Integer>(); // 承認番号
    private ArrayList<String>  forwardList        = new ArrayList<String>(); // 仕向先
    private ArrayList<String>  tranidList         = new ArrayList<String>(); // トランザクションID
    private ArrayList<String>  cancelTranidList   = new ArrayList<String>(); // 取消元トランザクションID
    private ArrayList<Integer> cancelflagList     = new ArrayList<Integer>(); // 取消フラグ
    private ArrayList<Integer> rowstatusList      = new ArrayList<Integer>(); // レコード種別
    private int                objKbn             = 0;                       // 抽出対象
    private String             dateFrom           = "";                      // 開始日付
    private String             dateTo             = "";                      // 終了日付
    private String             reserveNo          = "";                      // 予約番号
    private String             chk1Obj            = "";
    private String             chk2Obj            = "";
    private String             chk3Obj            = "";
    private String             errMsg             = "";                      //
    private int                pageMax            = 0;                       // 予約件数
    private int                pageAct            = 0;                       // 現在のページ数
    private int                pageSt             = 0;                       // 現在表示予約件数(先頭)
    private int                pageEd             = 0;                       // 現在表示予約件数(最後)
    private String             pageLink           = "";
    private int                recCnt             = 0;
    private boolean            cancelBtnViewFlag  = false;                   // 取消ボタン表示フラグ
    private String             pageRecords        = "";

    /**
     * 
     * getter
     * 
     */

    public String getDateFrom()
    {
        return this.dateFrom;
    }

    public String getDateTo()
    {
        return this.dateTo;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public int getObjKbn()
    {
        return this.objKbn;
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

    public String getReserveNo()
    {
        return this.reserveNo;
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

    public ArrayList<String> getSpidList()
    {
        return spidList;
    }

    public ArrayList<String> getSalesDateList()
    {
        return salesDateList;
    }

    public ArrayList<Integer> getGenerateDateList()
    {
        return generateDateList;
    }

    public ArrayList<Integer> getGenerateTimeList()
    {
        return generateTimeList;
    }

    public ArrayList<String> getSalesnameList()
    {
        return salesnameList;
    }

    public ArrayList<Integer> getAmountList()
    {
        return amountList;
    }

    public ArrayList<Integer> getApprovenoList()
    {
        return approvenoList;
    }

    public ArrayList<String> getForwardList()
    {
        return forwardList;
    }

    public ArrayList<String> getTranidList()
    {
        return tranidList;
    }

    public ArrayList<String> getCancelTranidList()
    {
        return cancelTranidList;
    }

    public ArrayList<Integer> getCancelflagList()
    {
        return cancelflagList;
    }

    public String getChk1Obj()
    {
        return chk1Obj;
    }

    public String getChk2Obj()
    {
        return chk2Obj;
    }

    public String getChk3Obj()
    {
        return chk3Obj;
    }

    public ArrayList<String> getReserveDateList()
    {
        return reserveDateList;
    }

    public ArrayList<String> getReserveEsttimeList()
    {
        return reserveEsttimeList;
    }

    public ArrayList<Integer> getRowstatusList()
    {
        return rowstatusList;
    }

    public ArrayList<Integer> getReserveSeqList()
    {
        return reserveSeqList;
    }

    public boolean isCancelBtnViewFlag()
    {
        return cancelBtnViewFlag;
    }

    /**
     * 
     * setter
     * 
     **/

    public void setDateFrom(String datefrom)
    {
        this.dateFrom = datefrom;
    }

    public void setDateTo(String dateto)
    {
        this.dateTo = dateto;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public void setObjKbn(int objkbn)
    {
        this.objKbn = objkbn;
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

    public void setReserveNo(String reserveno)
    {
        this.reserveNo = reserveno;
    }

    public void setReserveNoList(String reserveno)
    {
        this.reserveNoList.add( reserveno );
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public void setSelHotenaviID(String selHotenaviID)
    {
        this.selHotenaviID = selHotenaviID;
    }

    public void setSelHotelName(String selHotelName)
    {
        this.selHotelName = selHotelName;
    }

    public void setRecCnt(int reccnt)
    {
        this.recCnt = reccnt;
    }

    public void setSpidList(String spidList)
    {
        this.spidList.add( spidList );
    }

    public void setSalesDateList(String salesDateList)
    {
        this.salesDateList.add( salesDateList );
    }

    public void setGenerateDateList(Integer generateDateList)
    {
        this.generateDateList.add( generateDateList );
    }

    public void setGenerateTimeList(Integer generateTimeList)
    {
        this.generateTimeList.add( generateTimeList );
    }

    public void setSalesnameList(String salesnameList)
    {
        this.salesnameList.add( salesnameList );
    }

    public void setAmountList(Integer amountList)
    {
        this.amountList.add( amountList );
    }

    public void setApprovenoList(Integer approvenoList)
    {
        this.approvenoList.add( approvenoList );
    }

    public void setForwardList(String forwardList)
    {
        this.forwardList.add( forwardList );
    }

    public void setTranidList(String tranidList)
    {
        this.tranidList.add( tranidList );
    }

    public void setCancelTranidList(String cancelTranidList)
    {
        this.cancelTranidList.add( cancelTranidList );
    }

    public void setCancelflagList(Integer cancelflagList)
    {
        this.cancelflagList.add( cancelflagList );
    }

    public void setChk1Obj(String chk1Obj)
    {
        this.chk1Obj = chk1Obj;
    }

    public void setChk2Obj(String chk2Obj)
    {
        this.chk2Obj = chk2Obj;
    }

    public void setChk3Obj(String chk3Obj)
    {
        this.chk3Obj = chk3Obj;
    }

    public void setReserveDateList(String reserveDateList)
    {
        this.reserveDateList.add( reserveDateList );
    }

    public void setReserveEsttimeList(String reserveEsttimeList)
    {
        this.reserveEsttimeList.add( reserveEsttimeList );
    }

    public void setRowstatusList(Integer rowstatusList)
    {
        this.rowstatusList.add( rowstatusList );
    }

    public void setReserveSeqList(Integer reserveSeqList)
    {
        this.reserveSeqList.add( reserveSeqList );
    }

    public void setCancelBtnViewFlag(boolean cancelBtnViewFlag)
    {
        this.cancelBtnViewFlag = cancelBtnViewFlag;
    }

}
