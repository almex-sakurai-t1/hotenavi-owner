package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * �\��ꗗ�i�o�b�jForm�N���X
 */
public class FormOwnerRsvOptionList
{
    private int                selHotelID            = 0;
    private String             selHotenaviID         = "";
    private String             selHotelName          = "";
    private ArrayList<Integer> idList                = new ArrayList<Integer>(); // �z�e��ID
    private ArrayList<String>  optionList            = new ArrayList<String>(); // �I�v�V����
    private ArrayList<Integer> optionCountList       = new ArrayList<Integer>(); // �I�v�V��������
    private ArrayList<String>  planNmList            = new ArrayList<String>(); // �v������
    private ArrayList<String>  reserveNoList         = new ArrayList<String>(); // �\��ԍ�
    private ArrayList<String>  rsvDateList           = new ArrayList<String>(); // �\���
    private ArrayList<Integer> rsvDateValList        = new ArrayList<Integer>(); // �\���
    private ArrayList<Integer> seqList               = new ArrayList<Integer>(); // �����ԍ�
    private ArrayList<String>  userIdList            = new ArrayList<String>(); // ���[�UID
    private ArrayList<String>  userNmList            = new ArrayList<String>(); // ���p�Җ�
    private ArrayList<String>  tel1List              = new ArrayList<String>(); // �A����
    private ArrayList<String>  estTimeArrivalList    = new ArrayList<String>(); // �����\�莞��
    private ArrayList<Integer> estTimeArrivalValList = new ArrayList<Integer>(); // �����\�莞��
    private ArrayList<Integer> dspList               = new ArrayList<Integer>(); // �񋟋敪
    private String             dateFrom              = "";                      // �J�n���t
    private int                dateFromVal           = 0;                       // �J�n���t���l
    private String             timeFromHour          = "";                      // �J�n����(��)
    private String             timeFromMin           = "";                      // �J�n����(��)
    private String             dateTo                = "";                      // �I�����t
    private int                dateToVal             = 0;                       // �I�����t���l
    private String             timeToHour            = "";                      // �I������(��)
    private String             timeToMin             = "";                      // �I������(��)
    private String             reserveNo             = "";                      // �\��ԍ�
    private String             errMsg                = "";                      //
    private int                pageMax               = 0;                       // �\�񌏐�
    private int                pageAct               = 0;                       // ���݂̃y�[�W��
    private int                pageSt                = 0;                       // ���ݕ\���\�񌏐�(�擪)
    private int                pageEd                = 0;                       // ���ݕ\���\�񌏐�(�Ō�)
    private String             pageLink              = "";
    private String             assignLink            = "";
    private String             chk1Obj               = "";
    private String             chk2Obj               = "";
    private String             chk3Obj               = "";
    private String             chk4Obj               = "";
    private String             chk5Obj               = "";
    private int                recCnt                = 0;
    private String             pageRecords           = "";

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

    public void setDateFromVal(int dateFromVal)
    {
        this.dateFromVal = dateFromVal;
    }

    public int getDateFromVal()
    {
        return dateFromVal;
    }

    public void setDateToVal(int dateToVal)
    {
        this.dateToVal = dateToVal;
    }

    public int getDateToVal()
    {
        return dateToVal;
    }

    public void setDateTo(String dateTo)
    {
        this.dateTo = dateTo;
    }

    public String getDateTo()
    {
        return dateTo;
    }

    public void setTimeFromHour(String timeFromHour)
    {
        this.timeFromHour = timeFromHour;
    }

    public String getTimeFromHour()
    {
        return timeFromHour;
    }

    public void setTimeFromMin(String timeFromMin)
    {
        this.timeFromMin = timeFromMin;
    }

    public String getTimeFromMin()
    {
        return timeFromMin;
    }

    public void setTimeToHour(String timeToHour)
    {
        this.timeToHour = timeToHour;
    }

    public String getTimeToHour()
    {
        return timeToHour;
    }

    public void setTimeToMin(String timeToMin)
    {
        this.timeToMin = timeToMin;
    }

    public String getTimeToMin()
    {
        return timeToMin;
    }

    public void setOptionCountList(Integer optionCountList)
    {
        this.optionCountList.add( optionCountList );
    }

    public ArrayList<Integer> getOptionCountList()
    {
        return optionCountList;
    }

    public void setAssignLink(String assignLink)
    {
        this.assignLink = assignLink;
    }

    public String getAssignLink()
    {
        return assignLink;
    }

}
