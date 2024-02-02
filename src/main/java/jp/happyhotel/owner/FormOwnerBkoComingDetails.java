package jp.happyhotel.owner;

/*
 * ���X����Form�N���X
 */
public class FormOwnerBkoComingDetails
{
    //
    private int     selHotelID       = 0;
    private String  selHotenaviID    = "";
    private String  selHotelName     = "";
    private String  hotenaviID       = "";
    private int     userId           = 0;
    private int     imediaFlg        = 0;    // ������
    private int     billOwnFlg       = 0;    // �����{���\�S����
    private int     closingKind      = 0;    // ���ߏ����敪
    private int     modeFlg          = 0;    // �Ăяo�������ID
    private int     sonotaFlg        = 0;    // ���i�R�[�h��200�ԑ�̖��ׂ��𔻒f����i0�F���̑ΊO 1�F���̑��j
    private boolean existsSeikyu     = false; // �����E���|�f�[�^�����݂��邩

    private int     billSlipNo       = 0;    // �����`�[No
    private int     accrecvSlipNo    = 0;    // ���|�`�[No
    private int     newAccSLipNo     = 0;    // �V�������|�`�[No.
    private String  usageDate        = "";   // ���p���t
    private String  slipUpdate       = "";   // �X�V���t
    private String  personName       = "";   // �S����
    private int     usrMngNo         = 0;    // ���[�U�Ǘ��ԍ�
    private String  htSlipNo         = "";   // �`�[No.
    private String  htRoomNo         = "";   // ����
    private String  usageCharge      = "";   // ���p���z
    private String  usageChargeStr   = "";
    private double  bairitu          = 0;    // �t�^�{��
    private String  receiveCharge    = "";   // �\����z
    private String  receiveChargeStr = "";
    private int     hRaiten          = 0;    // �n�b�s�[�����@���X
    private int     hSeisan          = 0;    // �n�b�s�[�����@���Z �v�Z�p
    private String  hSeisan_View     = "";   // �n�b�s�[�����@���Z �\���p
    private String  hSeisan_Inp      = "";   // �n�b�s�[�����@���Z ���͗p
    private int     hWaribiki        = 0;    // �n�b�s�[�����@���� �v�Z�p
    private String  hWaribiki_View   = "";   // �n�b�s�[�����@���� �\���p
    private String  hWaribiki_Inp    = "";   // �n�b�s�[�����@���� ���͗p
    private int     hYoyaku          = 0;    // �n�b�s�[�����@�\��
    private int     hBonusMile       = 0;    // �\��{�[�i�X�}�C��
    private String  hSum             = "";   // �n�b�s�[�����@���v
    private int     hZandaka         = 0;    // �n�b�s�[�c���@����
    private int     sRaiten          = 0;    // �����E�x���@���X
    private String  sSeisan          = "";   // �����E�x���@���Z
    private String  sWaribiki        = "";   // �����E�x���@����
    private int     sWaribikiInt     = 0;
    private String  sYoyaku          = "";   // �����E�x���@�\��
    private String  sBonusMile       = "";   // �\��{�[�i�X�}�C��
    private String  sSum             = "";   // �����E�x���@���v
    private String  sonota1          = "";   // ���̑��x��1
    private String  sonota1Charge    = "";
    private String  sonota2          = "";   // ���̑������E�x��2
    private String  sonota2Charget   = "";
    private String  errMsg           = "";   //

    // ���X�ǉ�
    private String  selYear          = "";   // ���p�N
    private String  selMonth         = "";   // ���p��
    private String  selDate          = "";   // ���p��

    private int     inpUsageDate     = 0;    // ���p�N����
    private String  inpPersonNm      = "";   // �S���Җ�
    private String  inpHtRoomNo      = "";   // ����
    private String  inpUsageCharge   = "";   // ���p���z
    private String  inpRsvCharge     = "";   // �\����z
    private String  inpWaribiki      = "";   // ����

    private String  selYearFrom      = "";
    private String  selMonthFrom     = "";
    private String  selDateFrom      = "";
    private String  selYearTo        = "";
    private String  selMonthTo       = "";
    private String  selDateTo        = "";
    private int     page             = 0;

    private int     callPage         = 0;    // �Ăяo�����y�[�W(1:�n�s�z�e�}�C�������A2:���x����)

    /**
     * selHotelID���擾���܂��B
     * 
     * @return selHotelID
     */
    public int getSelHotelID()
    {
        return selHotelID;
    }

    /**
     * selHotelID��ݒ肵�܂��B
     * 
     * @param selHotelID selHotelID
     */
    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    /**
     * selHotenaviID���擾���܂��B
     * 
     * @return selHotenaviID
     */
    public String getSelHotenaviID()
    {
        return selHotenaviID;
    }

    /**
     * selHotenaviID��ݒ肵�܂��B
     * 
     * @param selHotenaviID selHotenaviID
     */
    public void setSelHotenaviID(String selHotenaviID)
    {
        this.selHotenaviID = selHotenaviID;
    }

    /**
     * selHotelName���擾���܂��B
     * 
     * @return selHotelName
     */
    public String getSelHotelName()
    {
        return selHotelName;
    }

    /**
     * selHotelName��ݒ肵�܂��B
     * 
     * @param selHotelName selHotelName
     */
    public void setSelHotelName(String selHotelName)
    {
        this.selHotelName = selHotelName;
    }

    /**
     * accrecvSlipNo���擾���܂��B
     * 
     * @return accrecvSlipNo
     */
    public int getAccrecvSlipNo()
    {
        return accrecvSlipNo;
    }

    /**
     * accrecvSlipNo��ݒ肵�܂��B
     * 
     * @param accrecvSlipNo accrecvSlipNo
     */
    public void setAccrecvSlipNo(int accrecvSlipNo)
    {
        this.accrecvSlipNo = accrecvSlipNo;
    }

    /**
     * usageDate���擾���܂��B
     * 
     * @return usageDate
     */
    public String getUsageDate()
    {
        return usageDate;
    }

    /**
     * usageDate��ݒ肵�܂��B
     * 
     * @param usageDate usageDate
     */
    public void setUsageDate(String usageDate)
    {
        this.usageDate = usageDate;
    }

    /**
     * slipUpdate���擾���܂��B
     * 
     * @return slipUpdate
     */
    public String getSlipUpdate()
    {
        return slipUpdate;
    }

    /**
     * slipUpdate��ݒ肵�܂��B
     * 
     * @param slipUpdate slipUpdate
     */
    public void setSlipUpdate(String slipUpdate)
    {
        this.slipUpdate = slipUpdate;
    }

    /**
     * personName���擾���܂��B
     * 
     * @return personName
     */
    public String getPersonName()
    {
        return personName;
    }

    /**
     * personName��ݒ肵�܂��B
     * 
     * @param personName personName
     */
    public void setPersonName(String personName)
    {
        this.personName = personName;
    }

    /**
     * htSlipNo���擾���܂��B
     * 
     * @return htSlipNo
     */
    public String getHtSlipNo()
    {
        return htSlipNo;
    }

    /**
     * htSlipNo��ݒ肵�܂��B
     * 
     * @param htSlipNo htSlipNo
     */
    public void setHtSlipNo(String htSlipNo)
    {
        this.htSlipNo = htSlipNo;
    }

    /**
     * htRoomNo���擾���܂��B
     * 
     * @return htRoomNo
     */
    public String getHtRoomNo()
    {
        return htRoomNo;
    }

    /**
     * htRoomNo��ݒ肵�܂��B
     * 
     * @param htRoomNo htRoomNo
     */
    public void setHtRoomNo(String htRoomNo)
    {
        this.htRoomNo = htRoomNo;
    }

    /**
     * usageCharge���擾���܂��B
     * 
     * @return usageCharge
     */
    public String getUsageCharge()
    {
        return usageCharge;
    }

    /**
     * usageCharge��ݒ肵�܂��B
     * 
     * @param usageCharge usageCharge
     */
    public void setUsageCharge(String usageCharge)
    {
        this.usageCharge = usageCharge;
    }

    /**
     * receiveCharge���擾���܂��B
     * 
     * @return receiveCharge
     */
    public String getReceiveCharge()
    {
        return receiveCharge;
    }

    /**
     * receiveCharge��ݒ肵�܂��B
     * 
     * @param receiveCharge receiveCharge
     */
    public void setReceiveCharge(String receiveCharge)
    {
        this.receiveCharge = receiveCharge;
    }

    /**
     * hRaiten���擾���܂��B
     * 
     * @return hRaiten
     */
    public int gethRaiten()
    {
        return hRaiten;
    }

    /**
     * hRaiten��ݒ肵�܂��B
     * 
     * @param hRaiten hRaiten
     */
    public void sethRaiten(int hRaiten)
    {
        this.hRaiten = hRaiten;
    }

    /**
     * hSeisan���擾���܂��B
     * 
     * @return hSeisan
     */
    public int gethSeisan()
    {
        return hSeisan;
    }

    /**
     * hSeisan��ݒ肵�܂��B
     * 
     * @param hSeisan hSeisan
     */
    public void sethSeisan(int hSeisan)
    {
        this.hSeisan = hSeisan;
    }

    /**
     * hYoyaku���擾���܂��B
     * 
     * @return hYoyaku
     */
    public int gethYoyaku()
    {
        return hYoyaku;
    }

    /**
     * hYoyaku��ݒ肵�܂��B
     * 
     * @param hYoyaku hYoyaku
     */
    public void sethYoyaku(int hYoyaku)
    {
        this.hYoyaku = hYoyaku;
    }

    /**
     * hBonusMile���擾���܂��B
     * 
     * @return hBonusMile
     */
    public int gethBonusMile()
    {
        return hBonusMile;
    }

    /**
     * hBonusMile��ݒ肵�܂��B
     * 
     * @param hBonusMile hBonusMile
     */
    public void sethBonusMile(int hBonusMile)
    {
        this.hBonusMile = hBonusMile;
    }

    /**
     * hSum���擾���܂��B
     * 
     * @return hSum
     */
    public String gethSum()
    {
        return hSum;
    }

    /**
     * hSum��ݒ肵�܂��B
     * 
     * @param hSum hSum
     */
    public void sethSum(String hSum)
    {
        this.hSum = hSum;
    }

    /**
     * hZandaka���擾���܂��B
     * 
     * @return hZandaka
     */
    public int gethZandaka()
    {
        return hZandaka;
    }

    /**
     * hZandaka��ݒ肵�܂��B
     * 
     * @param hZandaka hZandaka
     */
    public void sethZandaka(int hZandaka)
    {
        this.hZandaka = hZandaka;
    }

    /**
     * sRaiten���擾���܂��B
     * 
     * @return sRaiten
     */
    public int getsRaiten()
    {
        return sRaiten;
    }

    /**
     * sRaiten��ݒ肵�܂��B
     * 
     * @param sRaiten sRaiten
     */
    public void setsRaiten(int sRaiten)
    {
        this.sRaiten = sRaiten;
    }

    /**
     * sSeisan���擾���܂��B
     * 
     * @return sSeisan
     */
    public String getsSeisan()
    {
        return sSeisan;
    }

    /**
     * sSeisan��ݒ肵�܂��B
     * 
     * @param sSeisan sSeisan
     */
    public void setsSeisan(String sSeisan)
    {
        this.sSeisan = sSeisan;
    }

    /**
     * sWaribiki���擾���܂��B
     * 
     * @return sWaribiki
     */
    public String getsWaribiki()
    {
        return sWaribiki;
    }

    /**
     * sWaribiki��ݒ肵�܂��B
     * 
     * @param sWaribiki sWaribiki
     */
    public void setsWaribiki(String sWaribiki)
    {
        this.sWaribiki = sWaribiki;
    }

    /**
     * sYoyaku���擾���܂��B
     * 
     * @return sYoyaku
     */
    public String getsYoyaku()
    {
        return sYoyaku;
    }

    /**
     * sYoyaku��ݒ肵�܂��B
     * 
     * @param sYoyaku sYoyaku
     */
    public void setsYoyaku(String sYoyaku)
    {
        this.sYoyaku = sYoyaku;
    }

    /**
     * sBonusMile���擾���܂��B
     * 
     * @return sBonusMile
     */
    public String getsBonusMile()
    {
        return sBonusMile;
    }

    /**
     * sBonusMile��ݒ肵�܂��B
     * 
     * @param sBonusMile sBonusMile
     */
    public void setsBonusMile(String sBonusMile)
    {
        this.sBonusMile = sBonusMile;
    }

    /**
     * sSum���擾���܂��B
     * 
     * @return sSum
     */
    public String getsSum()
    {
        return sSum;
    }

    /**
     * sSum��ݒ肵�܂��B
     * 
     * @param sSum sSum
     */
    public void setsSum(String sSum)
    {
        this.sSum = sSum;
    }

    /**
     * errMsg���擾���܂��B
     * 
     * @return errMsg
     */
    public String getErrMsg()
    {
        return errMsg;
    }

    /**
     * errMsg��ݒ肵�܂��B
     * 
     * @param errMsg errMsg
     */
    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getUsrMngNo()
    {
        return usrMngNo;
    }

    public void setUsrMngNo(int usrMngNo)
    {
        this.usrMngNo = usrMngNo;
    }

    public String getUsageChargeStr()
    {
        return usageChargeStr;
    }

    public void setUsageChargeStr(String usageChargeStr)
    {
        this.usageChargeStr = usageChargeStr;
    }

    public String getReceiveChargeStr()
    {
        return receiveChargeStr;
    }

    public void setReceiveChargeStr(String receiveChargeStr)
    {
        this.receiveChargeStr = receiveChargeStr;
    }

    public String gethWaribiki_View()
    {
        return hWaribiki_View;
    }

    public void sethWaribiki_View(String hWaribiki_View)
    {
        this.hWaribiki_View = hWaribiki_View;
    }

    public void sethWaribiki(int hWaribiki)
    {
        this.hWaribiki = hWaribiki;
    }

    public int gethWaribiki()
    {
        return hWaribiki;
    }

    public String gethWaribiki_Inp()
    {
        return hWaribiki_Inp;
    }

    public void sethWaribiki_Inp(String hWaribiki_Inp)
    {
        this.hWaribiki_Inp = hWaribiki_Inp;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getBillOwnFlg()
    {
        return billOwnFlg;
    }

    public void setBillOwnFlg(int billOwnFlg)
    {
        this.billOwnFlg = billOwnFlg;
    }

    public int getImediaFlg()
    {
        return imediaFlg;
    }

    public void setImediaFlg(int imediaFlg)
    {
        this.imediaFlg = imediaFlg;
    }

    public String getSelYear()
    {
        return selYear;
    }

    public void setSelYear(String selYear)
    {
        this.selYear = selYear;
    }

    public String getSelMonth()
    {
        return selMonth;
    }

    public void setSelMonth(String selMonth)
    {
        this.selMonth = selMonth;
    }

    public String getSelDate()
    {
        return selDate;
    }

    public void setSelDate(String selDate)
    {
        this.selDate = selDate;
    }

    public int getInpUsageDate()
    {
        return inpUsageDate;
    }

    public void setInpUsageDate(int inpUsageDate)
    {
        this.inpUsageDate = inpUsageDate;
    }

    public String getInpPersonNm()
    {
        return inpPersonNm;
    }

    public void setInpPersonNm(String inpPersonNm)
    {
        this.inpPersonNm = inpPersonNm;
    }

    public String getInpHtRoomNo()
    {
        return inpHtRoomNo;
    }

    public void setInpHtRoomNo(String inpHtRoomNo)
    {
        this.inpHtRoomNo = inpHtRoomNo;
    }

    public String getInpUsageCharge()
    {
        return inpUsageCharge;
    }

    public void setInpUsageCharge(String inpUsageCharge)
    {
        this.inpUsageCharge = inpUsageCharge;
    }

    public String getInpRsvCharge()
    {
        return inpRsvCharge;
    }

    public void setInpRsvCharge(String inpRsvCharge)
    {
        this.inpRsvCharge = inpRsvCharge;
    }

    public String getInpWaribiki()
    {
        return inpWaribiki;
    }

    public void setInpWaribiki(String inpWaribiki)
    {
        this.inpWaribiki = inpWaribiki;
    }

    public String getSonota1Charge()
    {
        return sonota1Charge;
    }

    public void setSonota1Charge(String sonota1Charge)
    {
        this.sonota1Charge = sonota1Charge;
    }

    public String getSonota2Charget()
    {
        return sonota2Charget;
    }

    public void setSonota2Charget(String sonota2Charget)
    {
        this.sonota2Charget = sonota2Charget;
    }

    public void setSonota1(String sonota1)
    {
        this.sonota1 = sonota1;
    }

    public void setSonota2(String sonota2)
    {
        this.sonota2 = sonota2;
    }

    public String getSonota1()
    {
        return sonota1;
    }

    public String getSonota2()
    {
        return sonota2;
    }

    public int getClosingKind()
    {
        return closingKind;
    }

    public void setClosingKind(int closingKind)
    {
        this.closingKind = closingKind;
    }

    public int getsWaribikiInt()
    {
        return sWaribikiInt;
    }

    public void setsWaribikiInt(int sWaribikiInt)
    {
        this.sWaribikiInt = sWaribikiInt;
    }

    public String getHotenaviID()
    {
        return hotenaviID;
    }

    public void setHotenaviID(String hotenaviID)
    {
        this.hotenaviID = hotenaviID;
    }

    public String gethSeisan_View()
    {
        return hSeisan_View;
    }

    public void sethSeisan_View(String hSeisan_View)
    {
        this.hSeisan_View = hSeisan_View;
    }

    public String gethSeisan_Inp()
    {
        return hSeisan_Inp;
    }

    public void sethSeisan_Inp(String hSeisan_Inp)
    {
        this.hSeisan_Inp = hSeisan_Inp;
    }

    public int getModeFlg()
    {
        return modeFlg;
    }

    public void setModeFlg(int modeFlg)
    {
        this.modeFlg = modeFlg;
    }

    public int getSonotaFlg()
    {
        return sonotaFlg;
    }

    public void setSonotaFlg(int sonotaFlg)
    {
        this.sonotaFlg = sonotaFlg;
    }

    public boolean isExistsSeikyu()
    {
        return existsSeikyu;
    }

    public void setExistsSeikyu(boolean existsSeikyu)
    {
        this.existsSeikyu = existsSeikyu;
    }

    public int getBillSlipNo()
    {
        return billSlipNo;
    }

    public void setBillSlipNo(int billSlipNo)
    {
        this.billSlipNo = billSlipNo;
    }

    public double getBairitu()
    {
        return bairitu;
    }

    public void setBairitu(double bairitu)
    {
        this.bairitu = bairitu;
    }

    public int getCallPage()
    {
        return callPage;
    }

    public void setCallPage(int callPage)
    {
        this.callPage = callPage;
    }

    public String getSelYearFrom()
    {
        return selYearFrom;
    }

    public void setSelYearFrom(String selYearFrom)
    {
        this.selYearFrom = selYearFrom;
    }

    public String getSelMonthFrom()
    {
        return selMonthFrom;
    }

    public void setSelMonthFrom(String selMonthFrom)
    {
        this.selMonthFrom = selMonthFrom;
    }

    public String getSelDateFrom()
    {
        return selDateFrom;
    }

    public void setSelDateFrom(String selDateFrom)
    {
        this.selDateFrom = selDateFrom;
    }

    public String getSelYearTo()
    {
        return selYearTo;
    }

    public void setSelYearTo(String selYearTo)
    {
        this.selYearTo = selYearTo;
    }

    public String getSelMonthTo()
    {
        return selMonthTo;
    }

    public void setSelMonthTo(String selMonthTo)
    {
        this.selMonthTo = selMonthTo;
    }

    public String getSelDateTo()
    {
        return selDateTo;
    }

    public void setSelDateTo(String selDateTo)
    {
        this.selDateTo = selDateTo;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getNewAccSLipNo()
    {
        return newAccSLipNo;
    }

    public void setNewAccSLipNo(int newAccSLipNo)
    {
        this.newAccSLipNo = newAccSLipNo;
    }
}
