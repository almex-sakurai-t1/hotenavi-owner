package jp.happyhotel.dto;

public class DtoRsvListData
{
    String           header;

    // 送信電文
    private int      seq;
    private int      status;            // ステータス 01：来店待ち 02：来店 03：キャンセル hh_rsv_reserveより
    private int      extFlag;           // 予約種別 01:ハピホテ 02:OTA hh_rsv_reserveより
    private String   userId;            // ハピホテユーザーID hh_rsv_reserveより
    private String   rsvNo;             // 予約番号 hh_rsv_reserveより
    private String   dispRsvNo;         // 表示予約番号 hh_rsv_reserveより
    private int      arrivalDate;       // 到着予定日 hh_rsv_reserveより
    private int      arrivalTime;       // 到着予定時刻 hh_rsv_reserveより
    private int      planType;          // 休宿区分 hh_rsv_planより 01:宿泊 02:休憩
    private String   roomName;          // 予約番号 hh_rsv_reserveより hh_hotel_room_more 参照
    private int      roomNameListLength;
    private String[] roomNameList;
    private String   touchRoomName;
    private String   planNm;            // プラン名 hh_rsv_planより
    private int      chargeTotal;       // プラン料金 hh_rsv_reserveより
    private int      payment;           // 事前決済金額 hh_rsv_reserveより payment_status=1の場合は予約金額 - usedMile
    private int      usedMile;          // マイル決済済金額 hh_rsv_reserveより
    private int      ciTimeFrom;        // チェックイン開始時刻 hh_rsv_planより
    private int      ciTimeTo;          // チェックイン終了時刻 hh_rsv_planより
    private int      possibleTime;      // 滞在可能時間（分単位） 対応プランのhh_rsv_planより
    private int      coTime;            // チェックアウト時刻（分単位） 対応プランのhh_rsv_planより
    private String   name;              // ユーザ名 hh_rsv_reserveより
    private String   nameKana;          // ユーザ名(カナ) hh_rsv_reserveより
    private String   tel;               // ユーザ名 hh_rsv_reserveより
    private int      numMan;            // 男性人数 hh_rsv_reserveより
    private int      numWoman;          // 女性人数 hh_rsv_reserveより
    private int      numChild;          // 子供人数 hh_rsv_reserveより
    private String   option1;           // 必須オプション名称 対応プランのhh_rsv_optionより
    private String   option0;           // 任意オプション名称 対応プランのhh_rsv_optionより
    private String   car;               // 対応プランのhh_rsv_reserve.parkingより
    private String   remarks;           // hh_rsv_plan.questionとhh_sv_reserve.remarksより
    private String   demands;           // hh_rsv_reserve.demandsより
    private String   roomRank;          // hh_rsv_plan.room_select_kindが1(ランク指定)2(部屋指定)のときに、hh_hotel_roomrank のランク名をわたす

    private String   country;           // 居住地（国籍） hh_user_basic_foreignより
    private int      paymentAll;        // 事前決済合計金額 メイン予約Noのhh_rsv_credit,ない場合は予約Noに対応するhh_rsv_creditより
    private int      reserveDateTo;     // チェックアウト日付 hh_rsv_reserveより
    private int      rsvDate;           // 予約管理日付 hh_rsv_reserveより
    private int      identifyNo;        // 識別コード method=RsvListのパラメータより
    private int      countChargeList;   // 宿泊数
    private int[]    chargeList;

    private String   reserve;           // 予備

    // 受信電文
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
