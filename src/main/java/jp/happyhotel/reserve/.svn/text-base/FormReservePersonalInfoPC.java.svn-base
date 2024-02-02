package jp.happyhotel.reserve;

import java.util.ArrayList;

import jp.happyhotel.owner.FormOwnerRsvManageCalendar;

public class FormReservePersonalInfoPC
{
    // 画面間パラメータ
    private int                                              selHotelID                 = 0;
    private int                                              selPlanID                  = 0;
    private int                                              selSeq                     = 0;
    private int                                              selRsvDate                 = 0;
    private String                                           mode                       = "";                                                    // 処理モード

    // 画面内容
    private String                                           errMsg                     = "";
    private String                                           reserveNo                  = "";
    private String                                           hotelName                  = "";
    private String                                           hotelAddr                  = "";
    private String                                           planName                   = "";
    private String                                           planPR                     = "";
    private String                                           reserveDateFormat          = "";
    private int                                              reserveDateDay             = 0;                                                     // 予約日付
    private int                                              orgReserveDate             = 0;                                                     // 画面起動時の予約希望日
    private ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList                = new ArrayList<ArrayList<FormOwnerRsvManageCalendar>>(); // カレンダー情報
    private int                                              currentDate                = 0;
    private int                                              offerKind                  = 0;                                                     // 提供区分(1:プランでの提供、2:部屋での提供)
    private String                                           roomImgPath                = "";
    private ArrayList<Integer>                               equipIdList                = new ArrayList<Integer>();                              // 設備IDリスト
    private ArrayList<String>                                equipNmList                = new ArrayList<String>();                               // 設備名称リスト
    private ArrayList<String>                                equipBranchNameList        = new ArrayList<String>();                               // 選択肢名称リスト
    private ArrayList<String>                                equipPassList              = new ArrayList<String>();                               // 設備イメージファイルパスリスト
    private ArrayList<Integer>                               equipSortList              = new ArrayList<Integer>();                              // 設備表示順リスト
    private String                                           roomRemarks                = "";
    private String                                           roomPr                     = "";
    private String                                           adulTwoCharge              = "";
    private String                                           lowestCharge               = "";                                                    // 最低料金
    private String                                           maxCharge                  = "";                                                    // 最高料金
    private int                                              baseChargeTotal            = 0;                                                     // 基本料金
    private int                                              optionCharge               = 0;                                                     // オプション料金
    private int                                              chargeTotal                = 0;                                                     // 総合計
    private ArrayList<String>                                chargeModeNameList         = new ArrayList<String>();                               // 料金ﾓｰﾄﾞ名称リスト
    private ArrayList<String>                                ciTimeList                 = new ArrayList<String>();                               // ﾁｪｯｸｲﾝ開始時刻リスト
    private ArrayList<String>                                ciTimeToList               = new ArrayList<String>();                               // ﾁｪｯｸｲﾝ終了時刻リスト
    private ArrayList<String>                                coTimeList                 = new ArrayList<String>();                               // ﾁｪｯｸｱｳﾄ時刻リスト
    private String                                           ciTimeFromView             = "";
    private String                                           ciTimeToView               = "";
    private int                                              ciTimeFrom                 = 0;
    private int                                              ciTimeTo                   = 0;
    private String                                           coTimeView                 = "";
    private int                                              coTime                     = 0;
    private int                                              coKind                     = 0;
    private int                                              selNumAdult                = 0;                                                     // 選択された大人人数
    private ArrayList<Integer>                               numAdultList               = new ArrayList<Integer>();                              // 大人人数リスト
    private int                                              selNumChild                = 0;                                                     // 選択された子供人数
    private ArrayList<Integer>                               numChildList               = new ArrayList<Integer>();                              // こども人数リスト
    private ArrayList<Integer>                               numManList                 = new ArrayList<Integer>();                              // 男性リスト
    private ArrayList<Integer>                               numWomanList               = new ArrayList<Integer>();                              // 女性リスト
    private int                                              selNumMan                  = 0;                                                     // 男性人数
    private int                                              selNumWoman                = 0;                                                     // 女性人数
    private int                                              manCountJudgeFlag          = 0;                                                     // 男性多数許可フラグ
    private int                                              hotelParking               = 0;                                                     // ホテルの駐車場区分
    private int                                              selParkingUsedKbn          = 0;                                                     // 選択された駐車場利用区分
    private int                                              parkingUsedKbnInit         = 0;                                                     // 駐車場利用区分の初期値
    private ArrayList<Integer>                               parkingUsedKbnList         = new ArrayList<Integer>();                              // 駐車場利用IDリスト
    private ArrayList<String>                                parkingUsedNmList          = new ArrayList<String>();                               // 駐車場利用名リスト
    private int                                              selParkingCount            = 0;                                                     // 選択された台数
    private int                                              selHiRoofCount             = 0;                                                     // ハイルーフ車台数
    private int                                              selEstTimeArrival          = 0;                                                     // 選択された到着予定時刻
    private ArrayList<Integer>                               estTimeArrivalIDList       = new ArrayList<Integer>();                              // 到着予定時刻IDリスト
    private ArrayList<String>                                estTimeArrivalValList      = new ArrayList<String>();                               // 到着予定時刻の値リスト
    private String                                           loginUserId                = "";
    private String                                           loginMailAddr              = "";
    private ArrayList<String>                                loginMailAddrList          = new ArrayList<String>();                               //
    private String                                           loginTel                   = "";
    private String                                           loginUserKbn               = "";
    private String                                           lastName                   = "";
    private String                                           firstName                  = "";
    private String                                           lastNameKana               = "";
    private String                                           firstNameKana              = "";
    private String                                           zipCd3                     = "";
    private String                                           zipCd4                     = "";
    private int                                              selPrefId                  = 0;                                                     // 選択された都道府県
    private ArrayList<Integer>                               prefIdList                 = new ArrayList<Integer>();                              // 都道府県コードリスト
    private ArrayList<String>                                prefNmList                 = new ArrayList<String>();                               // 都道府県名リスト
    private int                                              selJisCd                   = 0;                                                     // 選択された市区町村
    private ArrayList<Integer>                               jisCdList                  = new ArrayList<Integer>();                              // 市区町村コードリスト
    private ArrayList<String>                                jisNmList                  = new ArrayList<String>();                               // 市区町村名リスト
    private String                                           address1                   = "";
    private String                                           address2                   = "";
    private String                                           address3                   = "";
    private String                                           tel                        = "";                                                    // 画面の連絡先電話番号
    private int                                              remainder                  = 0;                                                     // メールリマインダー
    private String                                           remainderMailAddr          = "";
    private String                                           demands                    = "";                                                    // お客様容貌
    private ArrayList<Integer>                               equipTypeList              = new ArrayList<Integer>();                              // 設備タイプリスト
    private int                                              rsvStartDate               = 0;                                                     // プレミアム・無料問わず予約開始加算日
    private int                                              rsvStartDatePremium        = 0;                                                     // プレミアム会員予約開始加算日
    private int                                              rsvStartDateFree           = 0;                                                     // 無料会員予約開始加算日
    private int                                              rsvStartTime               = 0;                                                     // 予約開始時間
    private int                                              rsvEndDate                 = 0;                                                     // プレミアム・無料問わず予約終了加算日
    private int                                              rsvEndDatePremium          = 0;                                                     // プレミアム予約終了加算日
    private int                                              rsvEndDateFree             = 0;                                                     // 無料予約終了加算日
    private int                                              rsvEndTime                 = 0;                                                     // 予約終了時間
    private String                                           rsvPremiumStartDayStr      = "";                                                    // プレミアム予約開始日(yyyy年mm月dd日)
    private String                                           rsvPremiumEndDayStr        = "";                                                    // プレミアム予約終了日(yyyy年mm月dd日)
    private int                                              rsvPremiumStartDay         = 0;                                                     // プレミアム予約開始日(値)
    private int                                              rsvPremiumEndDay           = 0;                                                     // プレミアム予約終了日(値)
    private String                                           rsvStartDayStr             = "";                                                    // 予約開始日(yyyy年mm月dd日)
    private String                                           rsvEndDayStr               = "";                                                    // 予約終了日(yyyy年mm月dd日)
    private int                                              rsvStartDayInt             = 0;                                                     // 予約開始日
    private int                                              rsvEndDayInt               = 0;                                                     // 予約終了日
    private int                                              salesStartDay              = 0;                                                     // 販売開始日
    private int                                              salesEndDay                = 0;                                                     // 販売終了日
    private int                                              hapyPoint                  = 0;                                                     // ハピーポイント
    private int                                              selCalDate                 = 0;                                                     // カレンダーで選択された日付
    private String                                           question                   = "";
    private int                                              questionFlg                = 0;                                                     // 予約者へ質問必須フラグ
    private String                                           remarks                    = "";                                                    // 予約のホテルからの質問のユーザの回答
    private String                                           dispRemarks                = "";                                                    // 備考の表示
    private int                                              dispRequestFlg             = 0;                                                     // 要望の表示フラグ

    private int                                              workId                     = 0;
    private int                                              chargeModeId               = 0;
    private int                                              roomZanSuu                 = 0;
    private String                                           baseChargeView             = "";
    private int                                              mobileCheckErrKbn          = 0;                                                     // 1:入力チェックエラー、2:復帰不可エラー
    private String                                           planImagePc                = "";
    private int                                              lastMonth                  = 0;
    private int                                              nextMonth                  = 0;
    private int                                              selCalYm                   = 0;
    private String                                           cardno                     = "";                                                    // カード番号
    private String                                           dispcardno                 = "";                                                    // 表示用カード番号
    private int                                              expiremonth                = 0;                                                     // 有効期限月
    private int                                              expireyear                 = 0;                                                     // 有効期限年

    // 更新画面のみで使用
    private int                                              seq                        = 0;
    private String                                           chargeTotalView            = "";
    private int                                              status                     = 0;

    // 通常オプション
    private FormReserveOptionSub                             frmOptSub                  = new FormReserveOptionSub();                            // 通常オプション
    private ArrayList<Integer>                               selOptSubIdList            = new ArrayList<Integer>();
    private ArrayList<Integer>                               selOptSubNumList           = new ArrayList<Integer>();
    private ArrayList<String>                                selOptSubRemarksList       = new ArrayList<String>();
    private ArrayList<Integer>                               selOptSubUnitPriceList     = new ArrayList<Integer>();
    private ArrayList<String>                                selOptSubUnitPriceViewList = new ArrayList<String>();
    private ArrayList<String>                                selOptSubChargeTotalList   = new ArrayList<String>();
    private ArrayList<String>                                selOptSubNmList            = new ArrayList<String>();
    private ArrayList<Integer>                               selOptNumList              = new ArrayList<Integer>();
    private ArrayList<Integer>                               selQuantityFlgList         = new ArrayList<Integer>();

    // 必須オプション
    private ArrayList<FormReserveOptionSubImp>               frmOptSubImpList           = new ArrayList<FormReserveOptionSubImp>();              // 必須オプションのリスト
    private ArrayList<Integer>                               selOptionImpSubIdList      = new ArrayList<Integer>();                              // 選択されている必須オプションのIDリスト
    private int                                              optImpWorkId               = 0;
    private boolean                                          isPaymemberFlg             = false;

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public int getSelPlanID()
    {
        return selPlanID;
    }

    public void setSelPlanID(int selPlanID)
    {
        this.selPlanID = selPlanID;
    }

    public int getSelSeq()
    {
        return selSeq;
    }

    public void setSelSeq(int selSeq)
    {
        this.selSeq = selSeq;
    }

    public int getSelRsvDate()
    {
        return selRsvDate;
    }

    public void setSelRsvDate(int selRsvDate)
    {
        this.selRsvDate = selRsvDate;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public String getReserveNo()
    {
        return reserveNo;
    }

    public void setReserveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    public String getHotelName()
    {
        return hotelName;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public String getHotelAddr()
    {
        return hotelAddr;
    }

    public void setHotelAddr(String hotelAddr)
    {
        this.hotelAddr = hotelAddr;
    }

    public String getPlanName()
    {
        return planName;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public String getPlanPR()
    {
        return planPR;
    }

    public void setPlanPR(String planPR)
    {
        this.planPR = planPR;
    }

    public String getReserveDateFormat()
    {
        return reserveDateFormat;
    }

    public void setReserveDateFormat(String reserveDateFormat)
    {
        this.reserveDateFormat = reserveDateFormat;
    }

    public int getOrgReserveDate()
    {
        return orgReserveDate;
    }

    public void setOrgReserveDate(int orgReserveDate)
    {
        this.orgReserveDate = orgReserveDate;
    }

    public ArrayList<ArrayList<FormOwnerRsvManageCalendar>> getMonthlyList()
    {
        return monthlyList;
    }

    public void setMonthlyList(ArrayList<ArrayList<FormOwnerRsvManageCalendar>> monthlyList)
    {
        this.monthlyList = monthlyList;
    }

    public int getCurrentDate()
    {
        return currentDate;
    }

    public void setCurrentDate(int currentDate)
    {
        this.currentDate = currentDate;
    }

    public int getOfferKind()
    {
        return offerKind;
    }

    public void setOfferKind(int offerKind)
    {
        this.offerKind = offerKind;
    }

    public String getRoomImgPath()
    {
        return roomImgPath;
    }

    public void setRoomImgPath(String roomImgPath)
    {
        this.roomImgPath = roomImgPath;
    }

    public ArrayList<Integer> getEquipIdList()
    {
        return equipIdList;
    }

    public void setEquipIdList(ArrayList<Integer> equipIdList)
    {
        this.equipIdList = equipIdList;
    }

    public ArrayList<String> getEquipNmList()
    {
        return equipNmList;
    }

    public void setEquipNmList(ArrayList<String> equipNmList)
    {
        this.equipNmList = equipNmList;
    }

    public ArrayList<String> getEquipPassList()
    {
        return equipPassList;
    }

    public void setEquipPassList(ArrayList<String> equipPassList)
    {
        this.equipPassList = equipPassList;
    }

    public String getRoomRemarks()
    {
        return roomRemarks;
    }

    public void setRoomRemarks(String roomRemarks)
    {
        this.roomRemarks = roomRemarks;
    }

    public String getAdulTwoCharge()
    {
        return adulTwoCharge;
    }

    public void setAdulTwoCharge(String adulTwoCharge)
    {
        this.adulTwoCharge = adulTwoCharge;
    }

    public int getSelNumAdult()
    {
        return selNumAdult;
    }

    public void setSelNumAdult(int selNumAdult)
    {
        this.selNumAdult = selNumAdult;
    }

    public ArrayList<Integer> getNumAdultList()
    {
        return numAdultList;
    }

    public void setNumAdultList(ArrayList<Integer> numAdultList)
    {
        this.numAdultList = numAdultList;
    }

    public int getSelNumChild()
    {
        return selNumChild;
    }

    public void setSelNumChild(int selNumChild)
    {
        this.selNumChild = selNumChild;
    }

    public ArrayList<Integer> getNumChildList()
    {
        return numChildList;
    }

    public void setNumChildList(ArrayList<Integer> numChildList)
    {
        this.numChildList = numChildList;
    }

    public int getSelParkingUsedKbn()
    {
        return selParkingUsedKbn;
    }

    public void setSelParkingUsedKbn(int selParkingUsedKbn)
    {
        this.selParkingUsedKbn = selParkingUsedKbn;
    }

    public ArrayList<Integer> getParkingUsedKbnList()
    {
        return parkingUsedKbnList;
    }

    public void setParkingUsedKbnList(ArrayList<Integer> parkingUsedKbnList)
    {
        this.parkingUsedKbnList = parkingUsedKbnList;
    }

    public ArrayList<String> getParkingUsedNmList()
    {
        return parkingUsedNmList;
    }

    public void setParkingUsedNmList(ArrayList<String> parkingUsedNmList)
    {
        this.parkingUsedNmList = parkingUsedNmList;
    }

    public int getSelParkingCount()
    {
        return selParkingCount;
    }

    public void setSelParkingCount(int selParkingCount)
    {
        this.selParkingCount = selParkingCount;
    }

    public int getSelEstTimeArrival()
    {
        return selEstTimeArrival;
    }

    public void setSelEstTimeArrival(int selEstTimeArrival)
    {
        this.selEstTimeArrival = selEstTimeArrival;
    }

    public ArrayList<Integer> getEstTimeArrivalIDList()
    {
        return estTimeArrivalIDList;
    }

    public void setEstTimeArrivalIDList(ArrayList<Integer> estTimeArrivalIDList)
    {
        this.estTimeArrivalIDList = estTimeArrivalIDList;
    }

    public ArrayList<String> getEstTimeArrivalValList()
    {
        return estTimeArrivalValList;
    }

    public void setEstTimeArrivalValList(ArrayList<String> estTimeArrivalValList)
    {
        this.estTimeArrivalValList = estTimeArrivalValList;
    }

    public String getLoginUserId()
    {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId)
    {
        this.loginUserId = loginUserId;
    }

    public String getLoginMailAddr()
    {
        return loginMailAddr;
    }

    public void setLoginMailAddr(String loginMailAddr)
    {
        this.loginMailAddr = loginMailAddr;
    }

    public ArrayList<String> getLoginMailAddrList()
    {
        return loginMailAddrList;
    }

    public void setLoginMailAddrList(ArrayList<String> loginMailAddrList)
    {
        this.loginMailAddrList = loginMailAddrList;
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

    public String getLastNameKana()
    {
        return lastNameKana;
    }

    public void setLastNameKana(String lastNameKana)
    {
        this.lastNameKana = lastNameKana;
    }

    public String getFirstNameKana()
    {
        return firstNameKana;
    }

    public void setFirstNameKana(String firstNameKana)
    {
        this.firstNameKana = firstNameKana;
    }

    public String getZipCd3()
    {
        return zipCd3;
    }

    public void setZipCd3(String zipCd3)
    {
        this.zipCd3 = zipCd3;
    }

    public String getZipCd4()
    {
        return zipCd4;
    }

    public void setZipCd4(String zipCd4)
    {
        this.zipCd4 = zipCd4;
    }

    public int getSelPrefId()
    {
        return selPrefId;
    }

    public void setSelPrefId(int selPrefId)
    {
        this.selPrefId = selPrefId;
    }

    public ArrayList<Integer> getPrefIdList()
    {
        return prefIdList;
    }

    public void setPrefIdList(ArrayList<Integer> prefIdList)
    {
        this.prefIdList = prefIdList;
    }

    public ArrayList<String> getPrefNmList()
    {
        return prefNmList;
    }

    public void setPrefNmList(ArrayList<String> prefNmList)
    {
        this.prefNmList = prefNmList;
    }

    public int getSelJisCd()
    {
        return selJisCd;
    }

    public void setSelJisCd(int selJisCd)
    {
        this.selJisCd = selJisCd;
    }

    public ArrayList<Integer> getJisCdList()
    {
        return jisCdList;
    }

    public void setJisCdList(ArrayList<Integer> jisCdList)
    {
        this.jisCdList = jisCdList;
    }

    public ArrayList<String> getJisNmList()
    {
        return jisNmList;
    }

    public void setJisNmList(ArrayList<String> jisNmList)
    {
        this.jisNmList = jisNmList;
    }

    public String getAddress3()
    {
        return address3;
    }

    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public int getRemainder()
    {
        return remainder;
    }

    public void setRemainder(int remainder)
    {
        this.remainder = remainder;
    }

    public String getRemainderMailAddr()
    {
        return remainderMailAddr;
    }

    public void setRemainderMailAddr(String remainderMailAddr)
    {
        this.remainderMailAddr = remainderMailAddr;
    }

    public String getDemands()
    {
        return demands;
    }

    public void setDemands(String demands)
    {
        this.demands = demands;
    }

    public ArrayList<Integer> getEquipTypeList()
    {
        return equipTypeList;
    }

    public void setEquipTypeList(ArrayList<Integer> equipTypeList)
    {
        this.equipTypeList = equipTypeList;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }

    public int getBaseChargeTotal()
    {
        return baseChargeTotal;
    }

    public void setBaseChargeTotal(int baseChargeTotal)
    {
        this.baseChargeTotal = baseChargeTotal;
    }

    public int getRsvStartDate()
    {
        return rsvStartDate;
    }

    public void setRsvStartDate(int rsvStartDate)
    {
        this.rsvStartDate = rsvStartDate;
    }

    public int getRsvStartTime()
    {
        return rsvStartTime;
    }

    public void setRsvStartTime(int rsvStartTime)
    {
        this.rsvStartTime = rsvStartTime;
    }

    public int getRsvEndDate()
    {
        return rsvEndDate;
    }

    public void setRsvEndDate(int rsvEndDate)
    {
        this.rsvEndDate = rsvEndDate;
    }

    public int getRsvEndTime()
    {
        return rsvEndTime;
    }

    public void setRsvEndTime(int rsvEndTime)
    {
        this.rsvEndTime = rsvEndTime;
    }

    public int getSalesStartDay()
    {
        return salesStartDay;
    }

    public void setSalesStartDay(int salesStartDay)
    {
        this.salesStartDay = salesStartDay;
    }

    public int getSalesEndDay()
    {
        return salesEndDay;
    }

    public void setSalesEndDay(int salesEndDay)
    {
        this.salesEndDay = salesEndDay;
    }

    public String getLoginTel()
    {
        return loginTel;
    }

    public void setLoginTel(String loginTel)
    {
        this.loginTel = loginTel;
    }

    public int getOptionCharge()
    {
        return optionCharge;
    }

    public void setOptionCharge(int optionCharge)
    {
        this.optionCharge = optionCharge;
    }

    public int getChargeTotal()
    {
        return chargeTotal;
    }

    public void setChargeTotal(int chargeTotal)
    {
        this.chargeTotal = chargeTotal;
    }

    public int getHapyPoint()
    {
        return hapyPoint;
    }

    public void setHapyPoint(int hapyPoint)
    {
        this.hapyPoint = hapyPoint;
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

    public int getSelCalDate()
    {
        return selCalDate;
    }

    public void setSelCalDate(int selCalDate)
    {
        this.selCalDate = selCalDate;
    }

    public int getParkingUsedKbnInit()
    {
        return parkingUsedKbnInit;
    }

    public void setParkingUsedKbnInit(int parkingUsedKbnInit)
    {
        this.parkingUsedKbnInit = parkingUsedKbnInit;
    }

    public String getCiTimeFromView()
    {
        return ciTimeFromView;
    }

    public void setCiTimeFromView(String ciTimeFromView)
    {
        this.ciTimeFromView = ciTimeFromView;
    }

    public String getCoTimeView()
    {
        return coTimeView;
    }

    public void setCoTimeView(String coTimeView)
    {
        this.coTimeView = coTimeView;
    }

    public void setCiTimeFrom(int ciTimeFrom)
    {
        this.ciTimeFrom = ciTimeFrom;
    }

    public void setCiTimeTo(int ciTimeTo)
    {
        this.ciTimeTo = ciTimeTo;
    }

    public void setCoTime(int coTime)
    {
        this.coTime = coTime;
    }

    public void setCoKind(int coKind)
    {
        this.coKind = coKind;
    }

    public int getCiTimeFrom()
    {
        return ciTimeFrom;
    }

    public int getCiTimeTo()
    {
        return ciTimeTo;
    }

    public int getCoTime()
    {
        return coTime;
    }

    public int getCoKind()
    {
        return coKind;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public String getChargeTotalView()
    {
        return chargeTotalView;
    }

    public void setChargeTotalView(String chargeTotalView)
    {
        this.chargeTotalView = chargeTotalView;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getWorkId()
    {
        return workId;
    }

    public void setWorkId(int workId)
    {
        this.workId = workId;
    }

    public int getChargeModeId()
    {
        return chargeModeId;
    }

    public void setChargeModeId(int chargeModeId)
    {
        this.chargeModeId = chargeModeId;
    }

    public int getRoomZanSuu()
    {
        return roomZanSuu;
    }

    public void setRoomZanSuu(int roomZanSuu)
    {
        this.roomZanSuu = roomZanSuu;
    }

    public String getBaseChargeView()
    {
        return baseChargeView;
    }

    public void setBaseChargeView(String baseChargeView)
    {
        this.baseChargeView = baseChargeView;
    }

    public int getMobileCheckErrKbn()
    {
        return mobileCheckErrKbn;
    }

    public void setMobileCheckErrKbn(int mobileCheckErrKbn)
    {
        this.mobileCheckErrKbn = mobileCheckErrKbn;
    }

    public ArrayList<Integer> getEquipSortList()
    {
        return equipSortList;
    }

    public void setEquipSortList(ArrayList<Integer> equipSortList)
    {
        this.equipSortList = equipSortList;
    }

    public String getPlanImagePc()
    {
        return planImagePc;
    }

    public void setPlanImagePc(String planImagePc)
    {
        this.planImagePc = planImagePc;
    }

    public int getHotelParking()
    {
        return hotelParking;
    }

    public void setHotelParking(int hotelParking)
    {
        this.hotelParking = hotelParking;
    }

    public ArrayList<FormReserveOptionSubImp> getFrmOptSubImpList()
    {
        return frmOptSubImpList;
    }

    public void setFrmOptSubImpList(ArrayList<FormReserveOptionSubImp> frmOptSubImpList)
    {
        this.frmOptSubImpList = frmOptSubImpList;
    }

    public ArrayList<Integer> getSelOptionImpSubIdList()
    {
        return selOptionImpSubIdList;
    }

    public void setSelOptionImpSubIdList(ArrayList<Integer> selOptionImpSubIdList)
    {
        this.selOptionImpSubIdList = selOptionImpSubIdList;
    }

    public int getOptImpWorkId()
    {
        return optImpWorkId;
    }

    public void setOptImpWorkId(int optImpWorkId)
    {
        this.optImpWorkId = optImpWorkId;
    }

    public String getLoginUserKbn()
    {
        return loginUserKbn;
    }

    public void setLoginUserKbn(String loginUserKbn)
    {
        this.loginUserKbn = loginUserKbn;
    }

    public int getLastMonth()
    {
        return lastMonth;
    }

    public void setLastMonth(int lastMonth)
    {
        this.lastMonth = lastMonth;
    }

    public int getNextMonth()
    {
        return nextMonth;
    }

    public void setNextMonth(int nextMonth)
    {
        this.nextMonth = nextMonth;
    }

    public int getSelCalYm()
    {
        return selCalYm;
    }

    public void setSelCalYm(int selCalYm)
    {
        this.selCalYm = selCalYm;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public FormReserveOptionSub getFrmOptSub()
    {
        return frmOptSub;
    }

    public void setDispRemarks(String dispRemarks)
    {
        this.dispRemarks = dispRemarks;
    }

    public void setFrmOptSub(FormReserveOptionSub frmOptSub)
    {
        this.frmOptSub = frmOptSub;
    }

    public ArrayList<Integer> getSelOptSubIdList()
    {
        return selOptSubIdList;
    }

    public void setSelOptSubIdList(ArrayList<Integer> selOptSubIdList)
    {
        this.selOptSubIdList = selOptSubIdList;
    }

    public void setCiTimeToView(String ciTimeToView)
    {
        this.ciTimeToView = ciTimeToView;
    }

    public ArrayList<Integer> getSelOptSubNumList()
    {
        return selOptSubNumList;
    }

    public void setSelOptSubNumList(ArrayList<Integer> selOptSubNumList)
    {
        this.selOptSubNumList = selOptSubNumList;
    }

    public ArrayList<String> getSelOptSubRemarksList()
    {
        return selOptSubRemarksList;
    }

    public void setSelOptSubRemarksList(ArrayList<String> selOptSubRemarksList)
    {
        this.selOptSubRemarksList = selOptSubRemarksList;
    }

    public ArrayList<Integer> getSelOptSubUnitPriceList()
    {
        return selOptSubUnitPriceList;
    }

    public void setSelOptSubUnitPriceList(ArrayList<Integer> selOptSubUnitPriceList)
    {
        this.selOptSubUnitPriceList = selOptSubUnitPriceList;
    }

    public ArrayList<String> getSelOptSubNmList()
    {
        return selOptSubNmList;
    }

    public void setSelOptSubNmList(ArrayList<String> selOptSubNmList)
    {
        this.selOptSubNmList = selOptSubNmList;
    }

    public ArrayList<String> getSelOptSubChargeTotalList()
    {
        return selOptSubChargeTotalList;
    }

    public void setSelOptSubChargeTotalList(ArrayList<String> selOptSubChargeTotalList)
    {
        this.selOptSubChargeTotalList = selOptSubChargeTotalList;
    }

    public ArrayList<String> getSelOptSubUnitPriceViewList()
    {
        return selOptSubUnitPriceViewList;
    }

    public void setSelOptSubUnitPriceViewList(ArrayList<String> selOptSubUnitPriceViewList)
    {
        this.selOptSubUnitPriceViewList = selOptSubUnitPriceViewList;
    }

    public ArrayList<Integer> getSelOptNumList()
    {
        return selOptNumList;
    }

    public void setSelOptNumList(ArrayList<Integer> selOptNumList)
    {
        this.selOptNumList = selOptNumList;
    }

    public ArrayList<Integer> getSelQuantityFlgList()
    {
        return selQuantityFlgList;
    }

    public void setSelQuantityFlgList(ArrayList<Integer> selQuantityFlgList)
    {
        this.selQuantityFlgList = selQuantityFlgList;
    }

    public boolean isPaymemberFlg()
    {
        return isPaymemberFlg;
    }

    public void setPaymemberFlg(boolean isPaymemberFlg)
    {
        this.isPaymemberFlg = isPaymemberFlg;
    }

    public String getLowestCharge()
    {
        return lowestCharge;
    }

    public void setLowestCharge(String lowestCharge)
    {
        this.lowestCharge = lowestCharge;
    }

    public void setReserveDateDay(int reserveDateDay)
    {
        this.reserveDateDay = reserveDateDay;
    }

    public int getReserveDateDay()
    {
        return reserveDateDay;
    }

    public void setRsvStartDatePremium(int rsvStartDatePremium)
    {
        this.rsvStartDatePremium = rsvStartDatePremium;
    }

    public int getRsvStartDatePremium()
    {
        return rsvStartDatePremium;
    }

    public void setRsvStartDateFree(int rsvStartDateFree)
    {
        this.rsvStartDateFree = rsvStartDateFree;
    }

    public int getRsvStartDateFree()
    {
        return rsvStartDateFree;
    }

    public void setRsvEndDatePremium(int rsvEndDatePremium)
    {
        this.rsvEndDatePremium = rsvEndDatePremium;
    }

    public int getRsvEndDatePremium()
    {
        return rsvEndDatePremium;
    }

    public void setRsvEndDateFree(int rsvEndDateFree)
    {
        this.rsvEndDateFree = rsvEndDateFree;
    }

    public int getRsvEndDateFree()
    {
        return rsvEndDateFree;
    }

    public void setRsvPremiumStartDayStr(String rsvPremiumStartDayStr)
    {
        this.rsvPremiumStartDayStr = rsvPremiumStartDayStr;
    }

    public String getRsvPremiumStartDayStr()
    {
        return rsvPremiumStartDayStr;
    }

    public void setRsvPremiumEndDayStr(String rsvPremiumEndDayStr)
    {
        this.rsvPremiumEndDayStr = rsvPremiumEndDayStr;
    }

    public String getRsvPremiumEndDayStr()
    {
        return rsvPremiumEndDayStr;
    }

    public void setRsvPremiumStartDay(int rsvPremiumStartDay)
    {
        this.rsvPremiumStartDay = rsvPremiumStartDay;
    }

    public int getRsvPremiumStartDay()
    {
        return rsvPremiumStartDay;
    }

    public void setRsvPremiumEndDay(int rsvPremiumEndDay)
    {
        this.rsvPremiumEndDay = rsvPremiumEndDay;
    }

    public int getRsvPremiumEndDay()
    {
        return rsvPremiumEndDay;
    }

    public String getDispRemarks()
    {
        return dispRemarks;
    }

    public void setRsvStartDayStr(String rsvStartDayStr)
    {
        this.rsvStartDayStr = rsvStartDayStr;
    }

    public String getRsvStartDayStr()
    {
        return rsvStartDayStr;
    }

    public void setRsvEndDayStr(String rsvEndDayStr)
    {
        this.rsvEndDayStr = rsvEndDayStr;
    }

    public String getRsvEndDayStr()
    {
        return rsvEndDayStr;
    }

    public void setExpiremonth(int expiremonth)
    {
        this.expiremonth = expiremonth;
    }

    public int getExpiremonth()
    {
        return expiremonth;
    }

    public void setExpireyear(int expireyear)
    {
        this.expireyear = expireyear;
    }

    public int getExpireyear()
    {
        return expireyear;
    }

    public void setCardno(String cardno)
    {
        this.cardno = cardno;
    }

    public String getCardno()
    {
        return cardno;
    }

    public void setDispcardno(String dispcardno)
    {
        this.dispcardno = dispcardno;
    }

    public String getDispcardno()
    {
        return dispcardno;
    }

    public void setRoomPr(String roomPr)
    {
        this.roomPr = roomPr;
    }

    public String getRoomPr()
    {
        return roomPr;
    }

    public void setDispRequestFlg(int dispRequestFlg)
    {
        this.dispRequestFlg = dispRequestFlg;
    }

    public int getDispRequestFlg()
    {
        return dispRequestFlg;
    }

    public String getCiTimeToView()
    {
        return ciTimeToView;
    }

    public void setRsvStartDayInt(int rsvStartDayInt)
    {
        this.rsvStartDayInt = rsvStartDayInt;
    }

    public int getRsvStartDayInt()
    {
        return rsvStartDayInt;
    }

    public void setRsvEndDayInt(int rsvEndDayInt)
    {
        this.rsvEndDayInt = rsvEndDayInt;
    }

    public int getRsvEndDayInt()
    {
        return rsvEndDayInt;
    }

    public void setQuestionFlg(int questionFlg)
    {
        this.questionFlg = questionFlg;
    }

    public int getQuestionFlg()
    {
        return questionFlg;
    }

    public void setEquipBranchNameList(ArrayList<String> equipBranchNameList)
    {
        this.equipBranchNameList = equipBranchNameList;
    }

    public ArrayList<String> getEquipBranchNameList()
    {
        return equipBranchNameList;
    }

    public void setChargeModeNameList(ArrayList<String> chargeModeNameList)
    {
        this.chargeModeNameList = chargeModeNameList;
    }

    public ArrayList<String> getChargeModeNameList()
    {
        return chargeModeNameList;
    }

    public void setCiTimeList(ArrayList<String> ciTimeList)
    {
        this.ciTimeList = ciTimeList;
    }

    public ArrayList<String> getCiTimeList()
    {
        return ciTimeList;
    }

    public void setCiTimeToList(ArrayList<String> ciTimeToList)
    {
        this.ciTimeToList = ciTimeToList;
    }

    public ArrayList<String> getCiTimeToList()
    {
        return ciTimeToList;
    }

    public void setCoTimeList(ArrayList<String> coTimeList)
    {
        this.coTimeList = coTimeList;
    }

    public ArrayList<String> getCoTimeList()
    {
        return coTimeList;
    }

    public void setSelHiRoofCount(int selHiRoofCount)
    {
        this.selHiRoofCount = selHiRoofCount;
    }

    public int getSelHiRoofCount()
    {
        return selHiRoofCount;
    }

    public void setNumManList(ArrayList<Integer> numManList)
    {
        this.numManList = numManList;
    }

    public ArrayList<Integer> getNumManList()
    {
        return numManList;
    }

    public void setNumWomanList(ArrayList<Integer> numWomanList)
    {
        this.numWomanList = numWomanList;
    }

    public ArrayList<Integer> getNumWomanList()
    {
        return numWomanList;
    }

    public void setSelNumMan(int selNumMan)
    {
        this.selNumMan = selNumMan;
    }

    public int getSelNumMan()
    {
        return selNumMan;
    }

    public void setSelNumWoman(int selNumWoman)
    {
        this.selNumWoman = selNumWoman;
    }

    public int getSelNumWoman()
    {
        return selNumWoman;
    }

    public void setManCountJudgeFlag(int manCountJudgeFlag)
    {
        this.manCountJudgeFlag = manCountJudgeFlag;
    }

    public int getManCountJudgeFlag()
    {
        return manCountJudgeFlag;
    }

    public String getMaxCharge()
    {
        return maxCharge;
    }

    public void setMaxCharge(String maxCharge)
    {
        this.maxCharge = maxCharge;
    }

}
