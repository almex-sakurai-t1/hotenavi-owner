package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * \ñêiobjFormNX
 */
public class FormOwnerRsvList
{
    private int                selHotelID            = 0;
    private String             selHotenaviID         = "";
    private String             selHotelName          = "";
    private ArrayList<Integer> idList                = new ArrayList<Integer>(); // zeID
    private ArrayList<String>  optionList            = new ArrayList<String>(); // IvV
    private ArrayList<String>  planNmList            = new ArrayList<String>(); // v¼
    private ArrayList<String>  reserveNoList         = new ArrayList<String>(); // \ñÔ
    private ArrayList<Integer> reserveSubNoList      = new ArrayList<Integer>(); // \ñ}Ô
    private ArrayList<String>  acceptDateList        = new ArrayList<String>(); // ótúXg
    private ArrayList<String>  acceptTimeList        = new ArrayList<String>(); // ótXg
    private ArrayList<String>  rsvDateList           = new ArrayList<String>(); // \ñú
    private ArrayList<Integer> rsvDateValList        = new ArrayList<Integer>(); // \ñú
    private ArrayList<Integer> seqList               = new ArrayList<Integer>(); // ®Ô
    private ArrayList<String>  userIdList            = new ArrayList<String>(); // [UID
    private ArrayList<String>  userNmList            = new ArrayList<String>(); // pÒ¼
    private ArrayList<String>  tel1List              = new ArrayList<String>(); // Aæ
    private ArrayList<String>  estTimeArrivalList    = new ArrayList<String>(); // \è
    private ArrayList<Integer> estTimeArrivalValList = new ArrayList<Integer>(); // \è
    private ArrayList<String>  statusList            = new ArrayList<String>(); // Xe[^X
    private ArrayList<Boolean> historyFlg            = new ArrayList<Boolean>(); // ðtO
    private ArrayList<Integer> dspList               = new ArrayList<Integer>(); // ñæª
    private ArrayList<Integer> statusValList         = new ArrayList<Integer>(); // Xe[^X
    private ArrayList<Integer> noShowList            = new ArrayList<Integer>(); // m[VEi¿ÎÛj
    private ArrayList<Integer> paymentList           = new ArrayList<Integer>(); // Ïû@
    private ArrayList<Integer> paymentStatusList     = new ArrayList<Integer>(); // ÏXe[^X
    private ArrayList<String>  chargeTotalList       = new ArrayList<String>(); // \ñàz

    private int                objKbn                = 0;                       // oÎÛ
    private String             dateFrom              = "";                      // Jnút
    private String             dateTo                = "";                      // I¹út
    private String             reserveNo             = "";                      // \ñÔ
    private String             errMsg                = "";                      //
    private int                pageMax               = 0;                       // \ñ
    private int                pageAct               = 0;                       // »ÝÌy[W
    private int                pageSt                = 0;                       // »Ý\¦\ñ(æª)
    private int                pageEd                = 0;                       // »Ý\¦\ñ(Åã)
    private String             pageLink              = "";
    private String             chk1Obj               = "";
    private String             chk2Obj               = "";
    private String             chk3Obj               = "";
    private String             chk4Obj               = "";
    private String             chk5Obj               = "";
    private int                recCnt                = 0;
    private String             pageRecords           = "";
    private ArrayList<Integer> rsvCountList          = new ArrayList<Integer>(); // \ñXg

    /**
     * 
     * getter
     * 
     */
    public String getChk1Obj()
    {
        return this.chk1Obj;
    }

    public String getChk2Obj()
    {
        return this.chk2Obj;
    }

    public String getChk3Obj()
    {
        return this.chk3Obj;
    }

    public String getChk4Obj()
    {
        return this.chk4Obj;
    }

    public String getChk5Obj()
    {
        return this.chk5Obj;
    }

    public String getDateFrom()
    {
        return this.dateFrom;
    }

    public String getDateTo()
    {
        return this.dateTo;
    }

    public ArrayList<Integer> getDspList()
    {
        return this.dspList;
    }

    public ArrayList<String> getEstTimeArrivalList()
    {
        return estTimeArrivalList;
    }

    public ArrayList<Integer> getEstTimeArrivalValList()
    {
        return estTimeArrivalValList;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public ArrayList<Integer> getIdList()
    {
        return idList;
    }

    public ArrayList<Integer> getNoSHowList()
    {
        return noShowList;
    }

    public int getObjKbn()
    {
        return this.objKbn;
    }

    public ArrayList<String> getOptionList()
    {
        return optionList;
    }

    public String getPageRecords()
    {
        return this.pageRecords;
    }

    public ArrayList<String> getPlanNmList()
    {
        return planNmList;
    }

    public ArrayList<String> getRsvDateList()
    {
        return rsvDateList;
    }

    public ArrayList<Integer> getRsvDateValList()
    {
        return rsvDateValList;
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

    public ArrayList<Integer> getSeqList()
    {
        return seqList;
    }

    public ArrayList<String> getStatusList()
    {
        return this.statusList;
    }

    public ArrayList<Integer> getStatusValList()
    {
        return this.statusValList;
    }

    public ArrayList<String> getUserIdList()
    {
        return userIdList;
    }

    public ArrayList<String> getUserNmList()
    {
        return userNmList;
    }

    public ArrayList<String> getTel1List()
    {
        return tel1List;
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

    public ArrayList<Boolean> getHistoryFlg()
    {
        return historyFlg;
    }

    public ArrayList<Integer> getReserveSubNoList()
    {
        return reserveSubNoList;
    }

    public ArrayList<String> getAcceptDateList()
    {
        return acceptDateList;
    }

    public ArrayList<String> getAcceptTimeList()
    {
        return acceptTimeList;
    }

    public ArrayList<Integer> getRsvCount()
    {
        return rsvCountList;
    }

    public ArrayList<Integer> getPaymentList()
    {
        return paymentList;
    }

    public ArrayList<Integer> getPaymentStatusList()
    {
        return paymentStatusList;
    }

    public ArrayList<String> getChargeTotalList()
    {
        return chargeTotalList;
    }

    /**
     * 
     * setter
     * 
     **/
    public void setChk1Obj(String chk1obj)
    {
        this.chk1Obj = chk1obj;
    }

    public void setChk2Obj(String chk2obj)
    {
        this.chk2Obj = chk2obj;
    }

    public void setChk3Obj(String chk3obj)
    {
        this.chk3Obj = chk3obj;
    }

    public void setChk4Obj(String chk4obj)
    {
        this.chk4Obj = chk4obj;
    }

    public void setChk5Obj(String chk5obj)
    {
        this.chk5Obj = chk5obj;
    }

    public void setDateFrom(String datefrom)
    {
        this.dateFrom = datefrom;
    }

    public void setDateTo(String dateto)
    {
        this.dateTo = dateto;
    }

    public void setDspList(int dspval)
    {
        this.dspList.add( dspval );
    }

    public void setEstTimeArrivalList(String esttimearrivallist)
    {
        this.estTimeArrivalList.add( esttimearrivallist );
    }

    public void setEstTimeArrivalValList(int esttimearrivalvalist)
    {
        this.estTimeArrivalValList.add( esttimearrivalvalist );
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public void setIdList(Integer id)
    {
        this.idList.add( id );
    }

    public void setNoShowList(Integer noshow)
    {
        this.noShowList.add( noshow );
    }

    public void setObjKbn(int objkbn)
    {
        this.objKbn = objkbn;
    }

    public void setOptionList(String option)
    {
        this.optionList.add( option );
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

    public void setPlanNmList(String plannm)
    {
        this.planNmList.add( plannm );
    }

    public void setReserveNo(String reserveno)
    {
        this.reserveNo = reserveno;
    }

    public void setReserveNoList(String reserveno)
    {
        this.reserveNoList.add( reserveno );
    }

    public void setRsvDateList(String rsvdate)
    {
        this.rsvDateList.add( rsvdate );
    }

    public void setRsvDateValList(int rsvdateVal)
    {
        this.rsvDateValList.add( rsvdateVal );
    }

    public void setSeqList(Integer seq)
    {
        this.seqList.add( seq );
    }

    public void setStatusValList(int status)
    {
        this.statusValList.add( status );
    }

    public void setStatusList(String status)
    {
        this.statusList.add( status );
    }

    public void setUserNmList(String usernm)
    {
        this.userNmList.add( usernm );
    }

    public void setTel1List(String tel1)
    {
        this.tel1List.add( tel1 );
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

    public void setUserIdList(String useridlist)
    {
        this.userIdList.add( useridlist );
    }

    public void setRecCnt(int reccnt)
    {
        this.recCnt = reccnt;
    }

    public void setHistoryFlg(Boolean historyFlg)
    {
        this.historyFlg.add( historyFlg );
    }

    public void setReserveSubNoList(Integer reserveSubNoList)
    {
        this.reserveSubNoList.add( reserveSubNoList );
    }

    public void setAcceptDateList(String acceptDateList)
    {
        this.acceptDateList.add( acceptDateList );
    }

    public void setAcceptTimeList(String acceptTimeList)
    {
        this.acceptTimeList.add( acceptTimeList );
    }

    public void setRsvCount(int rsvCount)
    {
        this.rsvCountList.add( rsvCount );
    }

    public void setPaymentList(Integer payment)
    {
        this.paymentList.add( payment );
    }

    public void setPaymentStatusList(Integer paymentStatus)
    {
        this.paymentStatusList.add( paymentStatus );
    }

    public void setChargeTotalList(String chargeTotal)
    {
        this.chargeTotalList.add( chargeTotal );
    }

}
