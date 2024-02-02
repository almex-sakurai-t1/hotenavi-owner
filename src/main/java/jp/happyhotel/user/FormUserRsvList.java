package jp.happyhotel.user;

import java.util.ArrayList;

/*
 * �\��ꗗForm�N���X
 */
public class FormUserRsvList
{
    private String             userId             = "";
    private ArrayList<Integer> idList             = new ArrayList<Integer>(); // �z�e��ID
    private ArrayList<String>  nmList             = new ArrayList<String>(); // �z�e�������X�g
    private ArrayList<String>  addressList        = new ArrayList<String>(); // �Z�����X�g
    private ArrayList<String>  optionList         = new ArrayList<String>(); // �I�v�V����
    private ArrayList<String>  planNmList         = new ArrayList<String>(); // �v������
    private ArrayList<String>  reserveNoList      = new ArrayList<String>(); // �\��ԍ�
    private ArrayList<String>  rsvDateList        = new ArrayList<String>(); // �\���
    private ArrayList<Integer> rsvDateValList     = new ArrayList<Integer>(); // �\����i�l�j
    private ArrayList<Integer> seqList            = new ArrayList<Integer>(); // �����ԍ�
    private ArrayList<String>  userIdList         = new ArrayList<String>(); // ���[�UID
    private ArrayList<String>  userNmList         = new ArrayList<String>(); // ���p�Җ�
    private ArrayList<String>  tel1List           = new ArrayList<String>(); // �A����
    private ArrayList<String>  estTimeArrivalList = new ArrayList<String>(); // �����\�莞��
    private ArrayList<String>  statusList         = new ArrayList<String>(); // �X�e�[�^�X
    private ArrayList<Integer> dspList            = new ArrayList<Integer>(); // �񋟋敪
    private ArrayList<Integer> statusValList      = new ArrayList<Integer>(); // �X�e�[�^�X
    private ArrayList<Integer> noshowflagList     = new ArrayList<Integer>(); // �m�[�V���[
    private ArrayList<Integer> paymentList        = new ArrayList<Integer>(); // ���ϕ��@
    private ArrayList<Integer> paymentStatusList  = new ArrayList<Integer>(); // ���σX�e�[�^�X
    private int                objKbn             = 0;                       // ���o�Ώ�
    private int                dateFrom           = 0;                       // �J�n���t
    private int                dateTo             = 0;                       // �I�����t
    private String             reserveNo          = "";                      // �\��ԍ�
    private String             errMsg             = "";                      //
    private int                pageMax            = 0;                       // �\�񌏐�
    private int                pageAct            = 0;                       // ���݂̃y�[�W��
    private int                pageSt             = 0;                       // ���ݕ\���\�񌏐�(�擪)
    private int                pageEd             = 0;                       // ���ݕ\���\�񌏐�(�Ō�)
    private String             pageLink           = "";
    private String             chk1Obj            = "";
    private String             chk2Obj            = "";
    private String             chk3Obj            = "";
    private String             chk4Obj            = "";
    private String             chk5Obj            = "";
    private int                recCnt             = 0;
    private String             pageRecords        = "";

    /**
     *
     * getter
     *
     */
    public ArrayList<String> getAddressList()
    {
        return this.addressList;
    }

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

    public int getDateFrom()
    {
        return this.dateFrom;
    }

    public int getDateTo()
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

    public String getErrMsg()
    {
        return errMsg;
    }

    public ArrayList<Integer> getIdList()
    {
        return idList;
    }

    public ArrayList<String> getNmList()
    {
        return this.nmList;
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

    public String getUserId()
    {
        return this.userId;
    }
    public ArrayList<Integer> getNoshowFlagList()
    {
        return noshowflagList;
    }
    public ArrayList<Integer> getPaymentList()
    {
        return paymentList;
    }
    public ArrayList<Integer> getPaymentStatusList()
    {
        return paymentStatusList;
    }


    /**
     *
     * setter
     *
     **/
    public void setAddressList(String address)
    {
        this.addressList.add( address );
    }

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

    public void setDateFrom(int datefrom)
    {
        this.dateFrom = datefrom;
    }

    public void setDateTo(int dateto)
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

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public void setIdList(Integer id)
    {
        this.idList.add( id );
    }

    public void setNmList(String nmList)
    {
        this.nmList.add( nmList );
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

    public void setUserIdList(String useridlist)
    {
        this.userIdList.add( useridlist );
    }

    public void setUserId(String userid)
    {
        this.userId = userid;
    }

    public void setRecCnt(int reccnt)
    {
        this.recCnt = reccnt;
    }
    public void setNoshowFlagList(int noshowflag)
    {
        this.noshowflagList.add( noshowflag );
    }
    public void setPaymentList(int payment)
    {
        this.paymentList.add( payment );
    }
    public void setPaymentStatusList(int paymentstatus)
    {
        this.paymentStatusList.add( paymentstatus );
    }

}
