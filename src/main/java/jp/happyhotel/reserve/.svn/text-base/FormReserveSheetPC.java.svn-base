package jp.happyhotel.reserve;

import java.util.ArrayList;

/*
 * 予約詳細画面 Formクラス
 */
public class FormReserveSheetPC
{
    private String             rsvNo                 = "";
    private int                rsvSubNo              = 0;
    private int                selHotelId            = 0;
    private String             hotelNm               = "";
    private String             hotelAddr             = "";
    private String             hotelTel              = "";
    private int                selPlanId             = 0;
    private int                selPlanSubId          = 0;
    private String             planNm                = "";
    private String             planPr                = "";
    private String             dispRemarks           = "";
    private int                dispRequestFlag       = 0;
    private int                rsvDate               = 0;
    private String             rsvDateView           = "";
    private String             basicTotalView        = "";
    private int                basicTotal            = 0;
    private int                adultNum              = 0;
    private String             adultNumView          = "";
    private int                childNum              = 0;
    private String             childNumView          = "";
    private int                manNum                = 0;
    private String             manNumView            = "";
    private int                womanNum              = 0;
    private String             womanNumView          = "";
    private String             chargeTotalView       = "";
    private int                chargeTotal           = 0;
    private int                optionChargeTotal     = 0;
    private String             ciTime                = "";
    private String             ciTimeToView          = "";
    private String             coTimeView            = "";
    private String             parkingUsed           = "";
    private int                parking               = 0;
    private int                parkingCnt            = 0;
    private int                hiRoofCnt             = 0;
    private String             estTimeArrivalView    = "";
    private int                estTimeArrival        = 0;
    private String             userId                = "";
    private String             mail                  = "";
    private ArrayList<String>  mailList              = new ArrayList<String>();
    private String             mailAddr1             = "";                      // 2014/01/29 退会後もメールアドレスを保持するために追加
    private String             mailAddr2             = "";                      // 2014/01/29 退会後もメールアドレスを保持するために追加
    private String             userAgent             = "";                      // 2014/07/31 ユーザーエージェント取得
    private String             name                  = "";
    private String             lastName              = "";
    private String             firstName             = "";
    private String             lastNmKana            = "";
    private String             firstNmKana           = "";
    private String             zip                   = "";
    private String             address               = "";
    private String             tel                   = "";
    private String             reminderView          = "";
    private int                reminder              = 0;
    private String             remainderMail         = "";
    private String             demands               = "";
    private String             demandsView           = "";
    private int                status                = 0;
    private int                seq                   = 0;
    private String             cahcelPolicy          = "";
    private String             mode                  = "";
    private String             errMsg                = "";
    private boolean            rsvFixViewFlag        = false;
    private String             rsvNoMain             = "";
    private int                extFlag               = 0;

    private int                noShow                = 0;
    private String             userKbn               = "";
    private int                roomNo                = 0;
    private String             procMsg               = "";
    private int                agree                 = 0;
    private int                prefId                = 0;
    private int                jisCd                 = 0;
    private String             address1              = "";
    private String             address2              = "";
    private String             address3              = "";
    private int                givingPointKind       = 0;
    private int                givingPoint           = 0;
    private int                ciTimeFrom            = 0;
    private int                ciTimeTo              = 0;
    private int                coTime                = 0;
    private int                coKind                = 0;
    private int                orgRsvDate            = 0;
    private int                orgRsvSeq             = 0;
    private int                offerKind             = 0;
    private String             checkInDate           = "";
    private String             checkInSeq            = "";
    private String             checkInTime           = "";
    private int                workId                = 0;
    private int                roomZansuu            = 0;
    private int                mobileCheckErrKbn     = 0;
    private int                termKind              = 0;                       // 登録端末区分
    private int                cancelCheck           = 0;
    private String             remarks               = "";
    private String             remarksView           = "";
    private String             question              = "";
    private int                questionFlg           = 0;
    private String             loginUserId           = "";
    private String             loginUserTel          = "";
    private String             loginUserMail         = "";
    private String             dispcardno            = "";
    private String             cardno                = "";
    private int                expireMonth           = 0;
    private int                expireYear            = 0;
    private boolean            creditAuthorityNgFlag = false;
    private boolean            creditUpdateFlag      = false;
    private boolean            noshowCreditFlag      = false;
    private int                chargeStartTime       = 0;
    private int                cancelKind            = 0;
    private int                roomHold              = 0;                       // 確保部屋
    private int                planType              = 0;                       // プラン種別

    // 必須オプション
    private ArrayList<String>  optNmImpList          = new ArrayList<String>();
    private ArrayList<String>  optSubNmImpList       = new ArrayList<String>();

    // 通常オプション
    private ArrayList<String>  optNmList             = new ArrayList<String>();
    private ArrayList<String>  optSubNmList          = new ArrayList<String>();
    private ArrayList<Integer> optInpMaxQuantityList = new ArrayList<Integer>();
    private ArrayList<Integer> optQuantityImpList    = new ArrayList<Integer>();
    private ArrayList<Integer> optNumberList         = new ArrayList<Integer>();
    private ArrayList<Integer> optUnitPriceList      = new ArrayList<Integer>();
    private ArrayList<String>  optUnitPriceViewList  = new ArrayList<String>();
    private ArrayList<String>  optChargeTotalList    = new ArrayList<String>();
    private ArrayList<String>  optRemarksList        = new ArrayList<String>();
    private ArrayList<String>  checkOptNmList        = new ArrayList<String>(); // チェック処理で使用するオプション名
    private ArrayList<Integer> checkOptIdList        = new ArrayList<Integer>(); // チェック処理で使用するオプションID
    private ArrayList<Integer> checkQuantityList     = new ArrayList<Integer>(); // チェック処理で使用する数量

    private ArrayList<Integer> seqList               = new ArrayList<Integer>();
    private int                addPoint              = 0;
    private int                reflectDate           = 0;

    private int                payment               = 0;
    private int                paymentStatus         = 0;
    private String             consumerDemands       = "";
    private int                addBonusMile          = 0;
    private String             usedMileView          = "";
    private int                usedMile              = 0;
    private String             payCreditView         = "";
    private int                payCredit             = 0;

    private int                chargeTotalAll        = 0;
    private int                reserveDateTo         = 0;
    private String             country               = "";
    private int                ciSeq                 = 0;

    public String getRsvNo()
    {
        return rsvNo;
    }

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = rsvNo;
    }

    public int getSelHotelId()
    {
        return selHotelId;
    }

    public void setSelHotelId(int selHotelId)
    {
        this.selHotelId = selHotelId;
    }

    public String getHotelNm()
    {
        return hotelNm;
    }

    public void setHotelNm(String hotelNm)
    {
        this.hotelNm = hotelNm;
    }

    public String getHotelAddr()
    {
        return hotelAddr;
    }

    public void setHotelAddr(String hotelAddr)
    {
        this.hotelAddr = hotelAddr;
    }

    public String getHotelTel()
    {
        return hotelTel;
    }

    public void setHotelTel(String hotelTel)
    {
        this.hotelTel = hotelTel;
    }

    public int getSelPlanId()
    {
        return selPlanId;
    }

    public void setSelPlanId(int selPlanId)
    {
        this.selPlanId = selPlanId;
    }

    public int getSelPlanSubId()
    {
        return selPlanSubId;
    }

    public void setSelPlanSubId(int selPlanSubId)
    {
        this.selPlanSubId = selPlanSubId;
    }

    public String getPlanNm()
    {
        return planNm;
    }

    public void setPlanNm(String planNm)
    {
        this.planNm = planNm;
    }

    public String getPlanPr()
    {
        return planPr;
    }

    public void setPlanPr(String planPr)
    {
        this.planPr = planPr;
    }

    public String getRsvDateView()
    {
        return rsvDateView;
    }

    public void setRsvDateView(String rsvDateView)
    {
        this.rsvDateView = rsvDateView;
    }

    public int getAdultNum()
    {
        return adultNum;
    }

    public void setAdultNum(int adultNum)
    {
        this.adultNum = adultNum;
    }

    public int getChildNum()
    {
        return childNum;
    }

    public void setChildNum(int childNum)
    {
        this.childNum = childNum;
    }

    public int getChargeTotal()
    {
        return chargeTotal;
    }

    public void setChargeTotal(int chargeTotal)
    {
        this.chargeTotal = chargeTotal;
    }

    public String getCiTime()
    {
        return ciTime;
    }

    public void setCiTime(String ciTime)
    {
        this.ciTime = ciTime;
    }

    public int getCoTime()
    {
        return coTime;
    }

    public void setCoTime(int coTime)
    {
        this.coTime = coTime;
    }

    public int getCoKind()
    {
        return coKind;
    }

    public void setCoKind(int coKind)
    {
        this.coKind = coKind;
    }

    public String getParkingUsed()
    {
        return parkingUsed;
    }

    public void setParkingUsed(String parkingUsed)
    {
        this.parkingUsed = parkingUsed;
    }

    public int getParkingCnt()
    {
        return parkingCnt;
    }

    public void setParkingCnt(int parkingCnt)
    {
        this.parkingCnt = parkingCnt;
    }

    public void setEstTimeArrival(int estTimeArrival)
    {
        this.estTimeArrival = estTimeArrival;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public ArrayList<String> getMailList()
    {
        return mailList;
    }

    public void setMailList(ArrayList<String> mailList)
    {
        this.mailList = mailList;
    }

    public String getMailAddr1()
    {
        return mailAddr1;
    }

    public void setMailAddr1(String mailAddr1)
    {
        this.mailAddr1 = mailAddr1;
    }

    public String getMailAddr2()
    {
        return mailAddr2;
    }

    public void setMailAddr2(String mailAddr2)
    {
        this.mailAddr2 = mailAddr2;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public int getReminder()
    {
        return reminder;
    }

    public void setReminder(int reminder)
    {
        this.reminder = reminder;
    }

    public String getRemainderMail()
    {
        return remainderMail;
    }

    public void setRemainderMail(String remainderMail)
    {
        this.remainderMail = remainderMail;
    }

    public String getDemands()
    {
        return demands;
    }

    public void setDemands(String demands)
    {
        this.demands = demands;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public String getCahcelPolicy()
    {
        return cahcelPolicy;
    }

    public void setCahcelPolicy(String cahcelPolicy)
    {
        this.cahcelPolicy = cahcelPolicy;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getRsvDate()
    {
        return rsvDate;
    }

    public void setRsvDate(int rsvDate)
    {
        this.rsvDate = rsvDate;
    }

    public int getNoShow()
    {
        return noShow;
    }

    public void setNoShow(int noShow)
    {
        this.noShow = noShow;
    }

    public String getUserKbn()
    {
        return userKbn;
    }

    public void setUserKbn(String userKbn)
    {
        this.userKbn = userKbn;
    }

    public int getRoomNo()
    {
        return roomNo;
    }

    public void setRoomNo(int roomNo)
    {
        this.roomNo = roomNo;
    }

    public String getProcMsg()
    {
        return procMsg;
    }

    public void setProcMsg(String procMsg)
    {
        this.procMsg = procMsg;
    }

    public int getAgree()
    {
        return agree;
    }

    public void setAgree(int agree)
    {
        this.agree = agree;
    }

    public int getRsvSubNo()
    {
        return rsvSubNo;
    }

    public void setRsvSubNo(int rsvSubNo)
    {
        this.rsvSubNo = rsvSubNo;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastNmKana()
    {
        return lastNmKana;
    }

    public void setLastNmKana(String lastNmKana)
    {
        this.lastNmKana = lastNmKana;
    }

    public String getFirstNmKana()
    {
        return firstNmKana;
    }

    public void setFirstNmKana(String firstNmKana)
    {
        this.firstNmKana = firstNmKana;
    }

    public String getEstTimeArrivalView()
    {
        return estTimeArrivalView;
    }

    public void setEstTimeArrivalView(String estTimeArrivalView)
    {
        this.estTimeArrivalView = estTimeArrivalView;
    }

    public int getEstTimeArrival()
    {
        return estTimeArrival;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public int getJisCd()
    {
        return jisCd;
    }

    public void setJisCd(int jisCd)
    {
        this.jisCd = jisCd;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getAddress3()
    {
        return address3;
    }

    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    public String getLoginUserTel()
    {
        return loginUserTel;
    }

    public void setLoginUserTel(String loginUserTel)
    {
        this.loginUserTel = loginUserTel;
    }

    public String getBasicTotalView()
    {
        return basicTotalView;
    }

    public void setBasicTotalView(String basicTotalView)
    {
        this.basicTotalView = basicTotalView;
    }

    public int getBasicTotal()
    {
        return basicTotal;
    }

    public void setBasicTotal(int basicTotal)
    {
        this.basicTotal = basicTotal;
    }

    public String getChargeTotalView()
    {
        return chargeTotalView;
    }

    public void setChargeTotalView(String chargeTotalView)
    {
        this.chargeTotalView = chargeTotalView;
    }

    public int getGivingPointKind()
    {
        return givingPointKind;
    }

    public void setGivingPointKind(int givingPointKind)
    {
        this.givingPointKind = givingPointKind;
    }

    public int getGivingPoint()
    {
        return givingPoint;
    }

    public void setGivingPoint(int givingPoint)
    {
        this.givingPoint = givingPoint;
    }

    public int getParking()
    {
        return parking;
    }

    public void setParking(int parking)
    {
        this.parking = parking;
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

    public String getCiTimeToView()
    {
        return ciTimeToView;
    }

    public void setCiTimeToView(String ciTimeView)
    {
        this.ciTimeToView = ciTimeView;
    }

    public String getCoTimeView()
    {
        return coTimeView;
    }

    public void setCoTimeView(String coTimeView)
    {
        this.coTimeView = coTimeView;
    }

    public String getReminderView()
    {
        return reminderView;
    }

    public void setReminderView(String reminderView)
    {
        this.reminderView = reminderView;
    }

    public int getOptionChargeTotal()
    {
        return optionChargeTotal;
    }

    public void setOptionChargeTotal(int optionChargeTotal)
    {
        this.optionChargeTotal = optionChargeTotal;
    }

    public int getOrgRsvDate()
    {
        return orgRsvDate;
    }

    public void setOrgRsvDate(int orgRsvDate)
    {
        this.orgRsvDate = orgRsvDate;
    }

    public int getOrgRsvSeq()
    {
        return orgRsvSeq;
    }

    public void setOrgRsvSeq(int orgRsvSeq)
    {
        this.orgRsvSeq = orgRsvSeq;
    }

    public String getAdultNumView()
    {
        return adultNumView;
    }

    public void setAdultNumView(String adultNumView)
    {
        this.adultNumView = adultNumView;
    }

    public String getChildNumView()
    {
        return childNumView;
    }

    public void setChildNumView(String childNumView)
    {
        this.childNumView = childNumView;
    }

    public int getOfferKind()
    {
        return offerKind;
    }

    public void setOfferKind(int offerKind)
    {
        this.offerKind = offerKind;
    }

    public String getCheckInDate()
    {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate)
    {
        this.checkInDate = checkInDate;
    }

    public String getCheckInSeq()
    {
        return checkInSeq;
    }

    public void setCheckInSeq(String checkInSeq)
    {
        this.checkInSeq = checkInSeq;
    }

    public String getCheckInTime()
    {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime)
    {
        this.checkInTime = checkInTime;
    }

    public ArrayList<Integer> getSeqList()
    {
        return seqList;
    }

    public void setSeqList(ArrayList<Integer> seqList)
    {
        this.seqList = seqList;
    }

    public int getWorkId()
    {
        return workId;
    }

    public void setWorkId(int workId)
    {
        this.workId = workId;
    }

    public int getRoomZansuu()
    {
        return roomZansuu;
    }

    public void setRoomZansuu(int roomZansuu)
    {
        this.roomZansuu = roomZansuu;
    }

    public int getMobileCheckErrKbn()
    {
        return mobileCheckErrKbn;
    }

    public void setMobileCheckErrKbn(int mobileCheckErrKbn)
    {
        this.mobileCheckErrKbn = mobileCheckErrKbn;
    }

    public int getTermKind()
    {
        return termKind;
    }

    public void setTermKind(int termKind)
    {
        this.termKind = termKind;
    }

    public String getDemandsView()
    {
        return demandsView;
    }

    public void setDemandsView(String demandsView)
    {
        this.demandsView = demandsView;
    }

    public ArrayList<String> getOptNmList()
    {
        return optNmList;
    }

    public void setOptNmList(ArrayList<String> optNmList)
    {
        this.optNmList = optNmList;
    }

    public ArrayList<String> getOptSubNmList()
    {
        return optSubNmList;
    }

    public String getDispRemarks()
    {
        return dispRemarks;
    }

    public void setOptSubNmList(ArrayList<String> optSubNmList)
    {
        this.optSubNmList = optSubNmList;
    }

    public int getCancelCheck()
    {
        return cancelCheck;
    }

    public void setCancelCheck(int cancelCheck)
    {
        this.cancelCheck = cancelCheck;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getQuestion()
    {
        return question;
    }

    public int getExpireMonth()
    {
        return expireMonth;
    }

    public int getExpireYear()
    {
        return expireYear;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getRemarksView()
    {
        return remarksView;
    }

    public void setRemarksView(String remarksView)
    {
        this.remarksView = remarksView;
    }

    public ArrayList<Integer> getOptInpMaxQuantityList()
    {
        return optInpMaxQuantityList;
    }

    public void setOptInpMaxQuantityList(ArrayList<Integer> optInpMaxQuantityList)
    {
        this.optInpMaxQuantityList = optInpMaxQuantityList;
    }

    public ArrayList<Integer> getOptQuantityImpList()
    {
        return optQuantityImpList;
    }

    public void setOptQuantityImpList(ArrayList<Integer> optQuantityImpList)
    {
        this.optQuantityImpList = optQuantityImpList;
    }

    public ArrayList<Integer> getOptNumberList()
    {
        return optNumberList;
    }

    public void setOptNumberList(ArrayList<Integer> optNumberList)
    {
        this.optNumberList = optNumberList;
    }

    public ArrayList<Integer> getOptUnitPriceList()
    {
        return optUnitPriceList;
    }

    public void setOptUnitPriceList(ArrayList<Integer> optUnitPriceList)
    {
        this.optUnitPriceList = optUnitPriceList;
    }

    public ArrayList<String> getOptChargeTotalList()
    {
        return optChargeTotalList;
    }

    public void setOptChargeTotalList(ArrayList<String> optChargeTotalList)
    {
        this.optChargeTotalList = optChargeTotalList;
    }

    public ArrayList<String> getOptRemarksList()
    {
        return optRemarksList;
    }

    public void setOptRemarksList(ArrayList<String> optRemarksList)
    {
        this.optRemarksList = optRemarksList;
    }

    public ArrayList<String> getOptNmImpList()
    {
        return optNmImpList;
    }

    public void setOptNmImpList(ArrayList<String> optNmImpList)
    {
        this.optNmImpList = optNmImpList;
    }

    public ArrayList<String> getOptSubNmImpList()
    {
        return optSubNmImpList;
    }

    public void setOptSubNmImpList(ArrayList<String> optSubNmImpList)
    {
        this.optSubNmImpList = optSubNmImpList;
    }

    public ArrayList<String> getOptUnitPriceViewList()
    {
        return optUnitPriceViewList;
    }

    public void setOptUnitPriceViewList(ArrayList<String> optUnitPriceViewList)
    {
        this.optUnitPriceViewList = optUnitPriceViewList;
    }

    public ArrayList<String> getCheckOptNmList()
    {
        return checkOptNmList;
    }

    public void setCheckOptNmList(ArrayList<String> checkOptNmList)
    {
        this.checkOptNmList = checkOptNmList;
    }

    public ArrayList<Integer> getCheckOptIdList()
    {
        return checkOptIdList;
    }

    public void setCheckOptIdList(ArrayList<Integer> checkOptIdList)
    {
        this.checkOptIdList = checkOptIdList;
    }

    public ArrayList<Integer> getCheckQuantityList()
    {
        return checkQuantityList;
    }

    public void setCheckQuantityList(ArrayList<Integer> checkQuantityList)
    {
        this.checkQuantityList = checkQuantityList;
    }

    public String getLoginUserId()
    {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId)
    {
        this.loginUserId = loginUserId;
    }

    public String getLoginUserMail()
    {
        return loginUserMail;
    }

    public void setLoginUserMail(String loginUserMail)
    {
        this.loginUserMail = loginUserMail;
    }

    public int getAddPoint()
    {
        return addPoint;
    }

    public void setAddPoint(int addPoint)
    {
        this.addPoint = addPoint;
    }

    public int getReflectDate()
    {
        return reflectDate;
    }

    public void setReflectDate(int reflectDate)
    {
        this.reflectDate = reflectDate;
    }

    public void setDispRemarks(String dispRemarks)
    {
        this.dispRemarks = dispRemarks;
    }

    public void setRsvFixViewFlag(boolean rsvFixViewFlag)
    {
        this.rsvFixViewFlag = rsvFixViewFlag;
    }

    public boolean isRsvFixViewFlag()
    {
        return rsvFixViewFlag;
    }

    public void setRsvNoMain(String rsvNoMain)
    {
        this.rsvNoMain = rsvNoMain;
    }

    public String getRsvNoMain()
    {
        return rsvNoMain;
    }

    public void setExtFlag(int extFlag)
    {
        this.extFlag = extFlag;
    }

    public int getExtFlag()
    {
        return extFlag;
    }

    public void setExpireMonth(int expireMonth)
    {
        this.expireMonth = expireMonth;
    }

    public void setExpireYear(int expireYear)
    {
        this.expireYear = expireYear;
    }

    public void setCreditAuthorityNgFlag(boolean creditAuthorityNgFlag)
    {
        this.creditAuthorityNgFlag = creditAuthorityNgFlag;
    }

    public boolean isCreditAuthorityNgFlag()
    {
        return creditAuthorityNgFlag;
    }

    public void setCreditUpdateFlag(boolean creditUpdateFlag)
    {
        this.creditUpdateFlag = creditUpdateFlag;
    }

    public boolean isCreditUpdateFlag()
    {
        return creditUpdateFlag;
    }

    public void setCardno(String cardno)
    {
        this.cardno = cardno;
    }

    public String getCardno()
    {
        return cardno;
    }

    public boolean isNoshowCreditFlag()
    {
        return noshowCreditFlag;
    }

    public void setNoshowCreditFlag(boolean noshowCreditFlag)
    {
        this.noshowCreditFlag = noshowCreditFlag;
    }

    public int getChargeStartTime()
    {
        return chargeStartTime;
    }

    public void setChargeStartTime(int chargeStartTime)
    {
        this.chargeStartTime = chargeStartTime;
    }

    public void setDispcardno(String dispcardno)
    {
        this.dispcardno = dispcardno;
    }

    public String getDispcardno()
    {
        return dispcardno;
    }

    public void setDispRequestFlag(int dispRequestFlag)
    {
        this.dispRequestFlag = dispRequestFlag;
    }

    public int getDispRequestFlag()
    {
        return dispRequestFlag;
    }

    public void setQuestionFlg(int questionFlg)
    {
        this.questionFlg = questionFlg;
    }

    public int getQuestionFlg()
    {
        return questionFlg;
    }

    public void setHiRoofCnt(int hiRoofCnt)
    {
        this.hiRoofCnt = hiRoofCnt;
    }

    public int getHiRoofCnt()
    {
        return hiRoofCnt;
    }

    public void setManNum(int manNum)
    {
        this.manNum = manNum;
    }

    public int getManNum()
    {
        return manNum;
    }

    public void setManNumView(String manNumView)
    {
        this.manNumView = manNumView;
    }

    public String getManNumView()
    {
        return manNumView;
    }

    public void setWomanNum(int womanNum)
    {
        this.womanNum = womanNum;
    }

    public int getWomanNum()
    {
        return womanNum;
    }

    public void setWomanNumView(String womanNumView)
    {
        this.womanNumView = womanNumView;
    }

    public String getWomanNumView()
    {
        return womanNumView;
    }

    public int getCancelKind()
    {
        return cancelKind;
    }

    public void setCancelKind(int cancelKind)
    {
        this.cancelKind = cancelKind;
    }

    public int getPayment()
    {
        return payment;
    }

    public int getPaymentStatus()
    {
        return paymentStatus;
    }

    public String getConsumerDemands()
    {
        return consumerDemands;
    }

    public int getAddBonusMile()
    {
        return addBonusMile;
    }

    public int getUsedMile()
    {
        return usedMile;
    }

    public void setPayment(int payment)
    {
        this.payment = payment;
    }

    public void setPaymentStatus(int paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    public void setConsumerDemands(String consumerDemands)
    {
        this.consumerDemands = consumerDemands;
    }

    public void setAddBonusMile(int addBonusMile)
    {
        this.addBonusMile = addBonusMile;
    }

    public void setUsedMile(int usedMile)
    {
        this.usedMile = usedMile;
    }

    public int getPayCredit()
    {
        return payCredit;
    }

    public void setPayCredit(int payCredit)
    {
        this.payCredit = payCredit;
    }

    public String getPayCreditView()
    {
        return payCreditView;
    }

    public void setPayCreditView(String payCreditView)
    {
        this.payCreditView = payCreditView;
    }

    public String getUsedMileView()
    {
        return usedMileView;
    }

    public void setUsedMileView(String usedMileView)
    {
        this.usedMileView = usedMileView;
    }

    public int getChargeTotalAll()
    {
        return chargeTotalAll;
    }

    public void setChargeTotalAll(int chargeTotalAll)
    {
        this.chargeTotalAll = chargeTotalAll;
    }

    public int getReserveDateTo()
    {
        return reserveDateTo;
    }

    public void setReserveDateTo(int reserveDateTo)
    {
        this.reserveDateTo = reserveDateTo;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public int getCiSeq()
    {
        return ciSeq;
    }

    public void setCiSeq(int ciSeq)
    {
        this.ciSeq = ciSeq;
    }

    public int getRoomHold()
    {
        return roomHold;
    }

    public void setRoomHold(int roomHold)
    {
        this.roomHold = roomHold;
    }

    public int getPlanType()
    {
        return planType;
    }

    public void setPlanType(int planType)
    {
        this.planType = planType;
    }
}
