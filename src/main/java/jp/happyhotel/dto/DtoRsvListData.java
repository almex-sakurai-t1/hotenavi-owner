package jp.happyhotel.dto;

public class DtoRsvListData
{
    String           header;

    // ���M�d��
    private int      seq;
    private int      status;            // �X�e�[�^�X 01�F���X�҂� 02�F���X 03�F�L�����Z�� hh_rsv_reserve���
    private int      extFlag;           // �\���� 01:�n�s�z�e 02:OTA hh_rsv_reserve���
    private String   userId;            // �n�s�z�e���[�U�[ID hh_rsv_reserve���
    private String   rsvNo;             // �\��ԍ� hh_rsv_reserve���
    private String   dispRsvNo;         // �\���\��ԍ� hh_rsv_reserve���
    private int      arrivalDate;       // �����\��� hh_rsv_reserve���
    private int      arrivalTime;       // �����\�莞�� hh_rsv_reserve���
    private int      planType;          // �x�h�敪 hh_rsv_plan��� 01:�h�� 02:�x�e
    private String   roomName;          // �\��ԍ� hh_rsv_reserve��� hh_hotel_room_more �Q��
    private int      roomNameListLength;
    private String[] roomNameList;
    private String   touchRoomName;
    private String   planNm;            // �v������ hh_rsv_plan���
    private int      chargeTotal;       // �v�������� hh_rsv_reserve���
    private int      payment;           // ���O���ϋ��z hh_rsv_reserve��� payment_status=1�̏ꍇ�͗\����z - usedMile
    private int      usedMile;          // �}�C�����ύϋ��z hh_rsv_reserve���
    private int      ciTimeFrom;        // �`�F�b�N�C���J�n���� hh_rsv_plan���
    private int      ciTimeTo;          // �`�F�b�N�C���I������ hh_rsv_plan���
    private int      possibleTime;      // �؍݉\���ԁi���P�ʁj �Ή��v������hh_rsv_plan���
    private int      coTime;            // �`�F�b�N�A�E�g�����i���P�ʁj �Ή��v������hh_rsv_plan���
    private String   name;              // ���[�U�� hh_rsv_reserve���
    private String   nameKana;          // ���[�U��(�J�i) hh_rsv_reserve���
    private String   tel;               // ���[�U�� hh_rsv_reserve���
    private int      numMan;            // �j���l�� hh_rsv_reserve���
    private int      numWoman;          // �����l�� hh_rsv_reserve���
    private int      numChild;          // �q���l�� hh_rsv_reserve���
    private String   option1;           // �K�{�I�v�V�������� �Ή��v������hh_rsv_option���
    private String   option0;           // �C�ӃI�v�V�������� �Ή��v������hh_rsv_option���
    private String   car;               // �Ή��v������hh_rsv_reserve.parking���
    private String   remarks;           // hh_rsv_plan.question��hh_sv_reserve.remarks���
    private String   demands;           // hh_rsv_reserve.demands���
    private String   roomRank;          // hh_rsv_plan.room_select_kind��1(�����N�w��)2(�����w��)�̂Ƃ��ɁAhh_hotel_roomrank �̃����N�����킽��

    private String   country;           // ���Z�n�i���Ёj hh_user_basic_foreign���
    private int      paymentAll;        // ���O���ύ��v���z ���C���\��No��hh_rsv_credit,�Ȃ��ꍇ�͗\��No�ɑΉ�����hh_rsv_credit���
    private int      reserveDateTo;     // �`�F�b�N�A�E�g���t hh_rsv_reserve���
    private int      rsvDate;           // �\��Ǘ����t hh_rsv_reserve���
    private int      identifyNo;        // ���ʃR�[�h method=RsvList�̃p�����[�^���
    private int      countChargeList;   // �h����
    private int[]    chargeList;

    private String   reserve;           // �\��

    // ��M�d��
    private int      count;
    private int      result;
    private int      errorCode;
    private String   guidRoomName;

    public DtoRsvListData()
    {
        seq = 0;
        status = 0;
        extFlag = 0;
        userId = "";
        roomName = "";
        rsvNo = "";
        dispRsvNo = "";
        arrivalDate = 0;
        arrivalTime = 0;
        planType = 0;
        roomNameListLength = 0;
        roomNameList = null;
        touchRoomName = "";
        planNm = "";
        chargeTotal = 0;
        payment = 0;
        usedMile = 0;
        ciTimeFrom = 0;
        ciTimeTo = 0;
        possibleTime = 0;
        coTime = 0;
        name = "";
        nameKana = "";
        tel = "";
        numMan = 0;
        numWoman = 0;
        numChild = 0;
        option1 = "";
        option0 = "";
        car = "";
        remarks = "";
        demands = "";
        roomRank = "";
        country = "";
        paymentAll = 0;
        reserveDateTo = 0;
        rsvDate = 0;
        identifyNo = 0;

        reserve = "";
        result = 0;
        count = 0;
        errorCode = 0;
        guidRoomName = "";
        countChargeList = 0;
        chargeList = null;
    }

    public String getHeader()
    {
        return header;
    }

    public int getSeq()
    {
        return seq;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRsvNo()
    {
        return rsvNo;
    }

    public String getDispRsvNo()
    {
        return dispRsvNo;
    }

    public int getRoomNameListLength()
    {
        return roomNameListLength;
    }

    public String[] getRoomNameList()
    {
        return roomNameList;
    }

    public String getTouchRoomName()
    {
        return touchRoomName;
    }

    public String getReserve()
    {
        return reserve;
    }

    public int getCount()
    {
        return count;
    }

    public int getResult()
    {
        return result;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getGuidRoomName()
    {
        return guidRoomName;
    }

    public int getPayment()
    {
        return payment;
    }

    public int getUsedMile()
    {
        return usedMile;
    }

    public int getPossibleTime()
    {
        return possibleTime;
    }

    public int getCoTime()
    {
        return coTime;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public int getNumMan()
    {
        return numMan;
    }

    public int getNumWoman()
    {
        return numWoman;
    }

    public int getNumChild()
    {
        return numChild;
    }

    public String getOption1()
    {
        return option1;
    }

    public String getOption0()
    {
        return option0;
    }

    public String getCar()
    {
        return car;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public String getDemands()
    {
        return demands;
    }

    public String getRoomRank()
    {
        return roomRank;
    }

    public String getCountry()
    {
        return country;
    }

    public int getPaymentAll()
    {
        return paymentAll;
    }

    public int getReserveDateTo()
    {
        return reserveDateTo;
    }

    public int getStatus()
    {
        return status;
    }

    public int getExtFlag()
    {
        return extFlag;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getArrivalDate()
    {
        return arrivalDate;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }

    public int getPlanType()
    {
        return planType;
    }

    public String getPlanNm()
    {
        return planNm;
    }

    public int getChargeTotal()
    {
        return chargeTotal;
    }

    public int getCiTimeFrom()
    {
        return ciTimeFrom;
    }

    public int getCiTimeTo()
    {
        return ciTimeTo;
    }

    public String getName()
    {
        return name;
    }

    public int getRsvDate()
    {
        return rsvDate;
    }

    public String getTel()
    {
        return tel;
    }

    public int getIdentifyNo()
    {
        return identifyNo;
    }

    public int getCountChargeList()
    {
        return countChargeList;
    }

    public int[] getChargeList()
    {
        return chargeList;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = rsvNo;
    }

    public void setDispRsvNo(String dispRsvNo)
    {
        this.dispRsvNo = dispRsvNo;
    }

    public void setRoomNameListLength(int roomNameListLength)
    {
        this.roomNameListLength = roomNameListLength;
    }

    public void setRoomNameList(String[] roomNameList)
    {
        this.roomNameList = roomNameList;
    }

    public void setTouchRoomName(String touchRoomName)
    {
        this.touchRoomName = touchRoomName;
    }

    public void setReserve(String reserve)
    {
        this.reserve = reserve;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public void setGuidRoomName(String guidRoomName)
    {
        this.guidRoomName = guidRoomName;
    }

    public void setPayment(int payment)
    {
        this.payment = payment;
    }

    public void setUsedMile(int usedMile)
    {
        this.usedMile = usedMile;
    }

    public void setPossibleTime(int possibleTime)
    {
        this.possibleTime = possibleTime;
    }

    public void setCoTime(int coTime)
    {
        this.coTime = coTime;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setNumMan(int numMan)
    {
        this.numMan = numMan;
    }

    public void setNumWoman(int numWoman)
    {
        this.numWoman = numWoman;
    }

    public void setNumChild(int numChild)
    {
        this.numChild = numChild;
    }

    public void setOption1(String option1)
    {
        this.option1 = option1;
    }

    public void setOption0(String option0)
    {
        this.option0 = option0;
    }

    public void setCar(String car)
    {
        this.car = car;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public void setDemands(String demands)
    {
        this.demands = demands;
    }

    public void setRoomRank(String roomRank)
    {
        this.roomRank = roomRank;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setPaymentAll(int paymentAll)
    {
        this.paymentAll = paymentAll;
    }

    public void setReserveDateTo(int reserveDateTo)
    {
        this.reserveDateTo = reserveDateTo;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setExtFlag(int extFlag)
    {
        this.extFlag = extFlag;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setArrivalDate(int arrivalDate)
    {
        this.arrivalDate = arrivalDate;
    }

    public void setArrivalTime(int arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public void setPlanType(int planType)
    {
        this.planType = planType;
    }

    public void setPlanNm(String planNm)
    {
        this.planNm = planNm;
    }

    public void setChargeTotal(int chargeTotal)
    {
        this.chargeTotal = chargeTotal;
    }

    public void setCiTimeFrom(int ciTimeFrom)
    {
        this.ciTimeFrom = ciTimeFrom;
    }

    public void setCiTimeTo(int ciTimeTo)
    {
        this.ciTimeTo = ciTimeTo;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public void setRsvDate(int rsvDate)
    {
        this.rsvDate = rsvDate;
    }

    public void setIdentifyNo(int identifyNo)
    {
        this.identifyNo = identifyNo;
    }

    public void setCountChargeList(int countChargeList)
    {
        this.countChargeList = countChargeList;
    }

    public void setChargeList(int[] chargeList)
    {
        this.chargeList = chargeList;
    }
}
