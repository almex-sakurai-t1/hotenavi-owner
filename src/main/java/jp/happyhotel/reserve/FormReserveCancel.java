package jp.happyhotel.reserve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormReserveCancel
{
    /*
     * 予約時受取パラメータ
     */
    /** ホテルID */
    private int                                       id                     = 0;
    /** プランID */
    private int                                       planId                 = 0;
    /** プランID枝番 */
    private int                                       planSubId              = 0;
    /** 部屋番号 */
    private int                                       seq                    = 0;
    /** 部屋ランク番号 */
    private int                                       roomRank               = 0;
    /** 予約日時 */
    private int                                       reserveDate            = 0;

    /*
     * 画面内容
     */
    /** 処理モード */
    private String                                    mode                   = "";
    /** エラーメッセージ */
    private List<String>                              errorMessages          = new ArrayList<String>();
    /** 予約番号 */
    private String                                    reserveNo              = "";
    /** 予約番号枝番 */
    private int                                       reserveSubNo           = 0;
    /** 仮予約番号 */
    private long                                      reserveTempNo          = 0;

    /*
     * パンくずリンク
     */
    /** ホテル名称 */
    private String                                    hotelName              = "";
    /** 都道府県ID */
    private int                                       hotelPrefId            = 0;
    /** 都道府県名 */
    private String                                    hotelPrefName          = "";
    /** 地域コード */
    private int                                       hotelJisCode           = 0;
    /** ホテル住所1 */
    private String                                    hotelAddress1          = "";
    /** ホテル名称（フリガナ） */
    private String                                    hotelNameKana          = "";

    /*
     * プラン情報とお客様情報入力 / お客様情報 / 画面入力項目
     */
    /** ご予約者氏名（姓） */
    private String                                    nameLast               = "";
    /** ご予約者氏名（名） */
    private String                                    nameFirst              = "";
    /** ご予約者氏名（フリガナ）（姓） */
    private String                                    nameLastKana           = "";
    /** ご予約者氏名（フリガナ）（名） */
    private String                                    nameFirstKana          = "";
    /** 郵便番号（3桁） */
    private String                                    zipCd3                 = "";
    /** 郵便番号（4桁） */
    private String                                    zipCd4                 = "";
    /** 選択された都道府県 */
    private int                                       prefCode               = 0;
    /** 選択された市区町村 */
    private int                                       jisCode                = 0;
    /** 住所３: 丁目番地、アパート名等 */
    private String                                    address3               = "";
    /** 連絡先電話番号 */
    private String                                    tel1                   = "";
    /** メールリマインダー */
    private int                                       reminderFlag           = 0;
    /** メールリマインダー用のアドレス */
    private String                                    mailAddr               = "";

    /*
     * プラン情報とお客様情報入力 / お客様情報 / 画面表示項目
     */
    /** ハピホテユーザーID */
    private String                                    userId                 = "";
    /** 都道府県コードリスト */
    private List<Map<Integer, String>>                prefList               = new ArrayList<Map<Integer, String>>();
    /** 市区町村コードリスト */
    private List<Integer>                             jisCdList              = new ArrayList<Integer>();
    /** 市区町村名リスト */
    private List<String>                              jisNmList              = new ArrayList<String>();
    /** メールアドレス */
    private List<String>                              mailAddrList           = new ArrayList<String>();

    /*
     * プラン情報とお客様情報入力 / お申込みプラン情報 / 画面入力項目
     */
    /** 到着予定時刻 */
    private int                                       estTimeArrival         = -1;
    /** 男性人数 */
    private int                                       numMan                 = 1;
    /** 女性人数 */
    private int                                       numWoman               = 1;
    /** 子供人数 */
    private int                                       numChild               = 0;
    /** 駐車場利用区分 */
    private int                                       parking                = 0;
    /** 駐車場利用台数 */
    private int                                       parkingCount           = 0;
    /** ハイルーフ車台数 */
    private int                                       highroofCount          = 0;
    /** 必須選択事項 */
    private Map<Integer, Map<Integer, List<Integer>>> customerOptionValues   = new HashMap<Integer, Map<Integer, List<Integer>>>();
    /** 必須選択事項 */
    private Map<Integer, Integer>                     optionalOptionValues   = new HashMap<Integer, Integer>();
    /** 要望事項 */
    private String                                    demands                = "";
    /** 備考 */
    private String                                    remarks                = "";

    /*
     * プラン情報とお客様情報入力 / お申込みプラン情報 / 画面表示項目
     */
    /** 予約受付開始日 */
    private int                                       reserveStartDate       = 0;
    /** 予約受付終了日 */
    private int                                       reserveEndDate         = 0;
    /** プラン名 */
    private String                                    planName               = "";
    /** プラン紹介文 */
    private String                                    planPr                 = "";
    /** 注意事項 */
    private String                                    precaution             = "";
    /** 部屋選択区分 */
    private int                                       roomSelectKind         = 0;
    /** 部屋名称 */
    private String                                    roomName               = "";
    /** ランク名称 */
    private String                                    rankName               = "";
    /** 実大人追加料金 */
    private int                                       adultAddChargeValue    = 0;
    /** 実子供追加料金 */
    private int                                       childAddChargeValue    = 0;
    /** チェックイン開始時間 */
    private int                                       ciTimeFrom             = 0;
    /** チェックイン終了時間 */
    private int                                       ciTimeTo               = 0;
    /** C/O: HHMMSS */
    private int                                       coTime                 = 0;
    /** C/O区分 */
    private int                                       coKind                 = 0;
    /** 基本料金 */
    private int                                       planCharge             = 0;
    /** 基本料金(人数反映) */
    private int                                       planNumCharge          = 0;
    /** 最大宿泊人数（合計） */
    private int                                       maxStayNum             = 0;
    /** 最小宿泊人数（合計） */
    private int                                       minStayNum             = 0;
    /** 最大宿泊人数（男） */
    private int                                       maxStayNumMan          = 0;
    /** 最大宿泊人数（女） */
    private int                                       maxStayNumWoman        = 0;
    /** 最小宿泊人数（男） */
    private int                                       minStayNumMan          = 0;
    /** 最小宿泊人数（女） */
    private int                                       minStayNumWoman        = 0;
    /** 最大宿泊人数（子供） */
    private int                                       maxStayNumChild        = 0;
    /** ホテル基本情報の駐車場区分 */
    private int                                       hotelParking           = 0;
    /** 予約基本情報の駐車場区分 */
    private int                                       reserveParking         = 0;
    /** プレミアムメンバー */
    private boolean                                   paymember              = false;
    /** 有料会員マイル */
    private int                                       addMilePremium         = 0;
    /** 無料会員マイル */
    private int                                       addMileFree            = 0;
    /** ボーナスマイル */
    private int                                       bonusMile              = 0;
    /** 予約者への質問 */
    private String                                    question               = "";
    /** お客様要望事項表示区分 */
    private int                                       consumerDemandsKind    = 0;

    /*
     * プラン情報とお客様情報入力 / キャンセル / 画面表示項目
     */
    private String                                    cancelPolicy           = "";

    /*
     * お支払方法の選択 / 料金 / 画面入力項目
     */
    /** 使用マイル */
    private String                                    usedMile               = "";

    /*
     * お支払方法の選択 / 料金 / 画面表示項目
     */
    /** 料金（税込） */
    private int                                       chargeTotal            = 0;
    /** 保有マイル */
    private int                                       holdMile               = 0;

    /*
     * お支払方法の選択 / 支払方法 / 画面入力項目
     */
    /** お支払方法 */
    private int                                       payment                = 0;
    /** カード番号 */
    private String                                    cardNo                 = "";
    /** カード有効期限（月） */
    private int                                       expireMonth            = 0;
    /** カード有効期限（年） */
    private int                                       expireYear             = 0;
    /** セキュリティコード */
    private String                                    securityCode           = "";
    /** SMS電話番号 */
    private String                                    smsPhoneNo             = "";

    /*
     * お支払方法の選択 / 支払方法 / 画面表示項目
     */
    /** 事前決済方法 */
    private int                                       paymentKind            = 0;
    /** 現地支払区分 */
    private int                                       localPaymentKind       = 0;
    /** クレジットカード会社ビット演算値 */
    private int                                       cardCompany            = 0;
    /** クレジットカード請求名 */
    private String                                    salesName              = "";

    /*
     * 予約申込確認 / 画面入力項目
     */
    /** 認証パスワード */
    private String                                    smsPasscode            = "";
    /** 個人情報の利用目的、利用規約、キャンセル規定に同意する */
    private int                                       agree                  = 0;

    /*
     * 予約申込確認 / 画面表示項目
     */
    /** 住所１: 都道府県名 */
    private String                                    address1               = "";
    /** 住所２: 市区町村名 */
    private String                                    address2               = "";

    /*
     * 内部処理で利用
     */
    /** プラン種別 */
    private int                                       planType               = 0;
    /** 予約受付開始日数（プレミアム会員） */
    private int                                       reserveStartDayPremium = 0;
    /** 予約受付開始日数（無料会員） */
    private int                                       reserveStartDayFree    = 0;
    /** 予約受付開始日数 */
    private int                                       reserveStartDay        = 0;
    /** 予約受付開始時間 */
    private int                                       reserveStartTime       = 0;
    /** 予約受付終了日数 */
    private int                                       reserveEndDay          = 0;
    /** 予約受付終了時間 */
    private int                                       reserveEndTime         = 0;
    /** 予約受付終了日（プレミアム会員） */
    private int                                       reserveEndDatePremium  = 0;
    /** 予約受付終了日（無料会員） */
    private int                                       reserveEndDateFree     = 0;
    /** 予約可能期間開始 */
    private int                                       salesStartDate         = 0;
    /** 予約可能期間終了 */
    private int                                       salesEndDate           = 0;
    /** 大人追加料金区分 */
    private int                                       adultAddChargeKind     = 0;
    /** 大人追加料金 */
    private int                                       adultAddCharge         = 0;
    /** 子供追加料金区分 */
    private int                                       childAddChargeKind     = 0;
    /** 子供追加料金 */
    private int                                       childAddCharge         = 0;
    /** 男性多数許可フラグ */
    private int                                       menOkFlag              = 0;
    /** ユーザー情報の電話番号 */
    private String                                    tel2                   = "";
    /** 基本料金合計 */
    private int                                       basicChargeTotal       = 0;
    /** オプション合計 */
    private int                                       optionChargeTotal      = 0;
    /** オーソリ結果 */
    private boolean                                   successCreditAuthority = false;
    /** トランザクションID */
    private String                                    tranid                 = "";
    /** ユーザーエージェント */
    private String                                    userAgent              = "";
    /** hotenavi ID */
    private String                                    hotenaviId             = "";
    /** 来店必須フラグ */
    private int                                       comingFlag             = 0;
    /** 予約親番号 */
    private String                                    reserveNoMain          = "";
    /** 言語 */
    private String                                    language               = "";

    /**
     * エラーメッセージの追加
     * 
     * @param errorMessage
     */
    public void addErrorMessage(String errorMessage)
    {
        if ( this.getErrorMessages() == null )
        {
            this.errorMessages = new ArrayList<String>();
        }
        this.errorMessages.add( errorMessage );
    }

    /**
     * フォームのデータのエラー有無
     * 
     * @return
     */
    public boolean isValid()
    {
        if ( this.errorMessages == null )
        {
            return true;
        }
        if ( this.errorMessages.size() == 0 )
        {
            return true;
        }
        return false;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPlanId()
    {
        return planId;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public int getPlanSubId()
    {
        return planSubId;
    }

    public void setPlanSubId(int planSubId)
    {
        this.planSubId = planSubId;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public int getRoomRank()
    {
        return roomRank;
    }

    public void setRoomRank(int roomRank)
    {
        this.roomRank = roomRank;
    }

    public int getReserveDate()
    {
        return reserveDate;
    }

    public void setReserveDate(int reserveDate)
    {
        this.reserveDate = reserveDate;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }

    public List<String> getErrorMessages()
    {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages)
    {
        this.errorMessages = errorMessages;
    }

    public String getReserveNo()
    {
        return reserveNo;
    }

    public void setReserveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    public int getReserveSubNo()
    {
        return reserveSubNo;
    }

    public void setReserveSubNo(int reserveSubNo)
    {
        this.reserveSubNo = reserveSubNo;
    }

    public long getReserveTempNo()
    {
        return reserveTempNo;
    }

    public void setReserveTempNo(long reserveTempNo)
    {
        this.reserveTempNo = reserveTempNo;
    }

    public String getHotelName()
    {
        return hotelName;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public int getHotelPrefId()
    {
        return hotelPrefId;
    }

    public void setHotelPrefId(int hotelPrefId)
    {
        this.hotelPrefId = hotelPrefId;
    }

    public String getHotelPrefName()
    {
        return hotelPrefName;
    }

    public void setHotelPrefName(String hotelPrefName)
    {
        this.hotelPrefName = hotelPrefName;
    }

    public int getHotelJisCode()
    {
        return hotelJisCode;
    }

    public void setHotelJisCode(int hotelJisCode)
    {
        this.hotelJisCode = hotelJisCode;
    }

    public String getHotelAddress1()
    {
        return hotelAddress1;
    }

    public void setHotelAddress1(String hotelAddress1)
    {
        this.hotelAddress1 = hotelAddress1;
    }

    public String getHotelNameKana()
    {
        return hotelNameKana;
    }

    public void setHotelNameKana(String hotelNameKana)
    {
        this.hotelNameKana = hotelNameKana;
    }

    public String getNameLast()
    {
        return nameLast;
    }

    public void setNameLast(String nameLast)
    {
        this.nameLast = nameLast;
    }

    public String getNameFirst()
    {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst)
    {
        this.nameFirst = nameFirst;
    }

    public String getNameLastKana()
    {
        return nameLastKana;
    }

    public void setNameLastKana(String nameLastKana)
    {
        this.nameLastKana = nameLastKana;
    }

    public String getNameFirstKana()
    {
        return nameFirstKana;
    }

    public void setNameFirstKana(String nameFirstKana)
    {
        this.nameFirstKana = nameFirstKana;
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

    public int getPrefCode()
    {
        return prefCode;
    }

    public void setPrefCode(int prefCode)
    {
        this.prefCode = prefCode;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public String getAddress3()
    {
        return address3;
    }

    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    public String getTel1()
    {
        return tel1;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public int getReminderFlag()
    {
        return reminderFlag;
    }

    public void setReminderFlag(int reminderFlag)
    {
        this.reminderFlag = reminderFlag;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public List<Map<Integer, String>> getPrefList()
    {
        return prefList;
    }

    public void setPrefList(List<Map<Integer, String>> prefList)
    {
        this.prefList = prefList;
    }

    public List<Integer> getJisCdList()
    {
        return jisCdList;
    }

    public void setJisCdList(List<Integer> jisCdList)
    {
        this.jisCdList = jisCdList;
    }

    public List<String> getJisNmList()
    {
        return jisNmList;
    }

    public void setJisNmList(List<String> jisNmList)
    {
        this.jisNmList = jisNmList;
    }

    public List<String> getMailAddrList()
    {
        return mailAddrList;
    }

    public void setMailAddrList(List<String> mailAddrList)
    {
        this.mailAddrList = mailAddrList;
    }

    public int getEstTimeArrival()
    {
        return estTimeArrival;
    }

    public void setEstTimeArrival(int estTimeArrival)
    {
        this.estTimeArrival = estTimeArrival;
    }

    public int getNumMan()
    {
        return numMan;
    }

    public void setNumMan(int numMan)
    {
        this.numMan = numMan;
    }

    public int getNumWoman()
    {
        return numWoman;
    }

    public void setNumWoman(int numWoman)
    {
        this.numWoman = numWoman;
    }

    public int getNumChild()
    {
        return numChild;
    }

    public void setNumChild(int numChild)
    {
        this.numChild = numChild;
    }

    public int getParking()
    {
        return parking;
    }

    public void setParking(int parking)
    {
        this.parking = parking;
    }

    public int getParkingCount()
    {
        return parkingCount;
    }

    public void setParkingCount(int parkingCount)
    {
        this.parkingCount = parkingCount;
    }

    public int getHighroofCount()
    {
        return highroofCount;
    }

    public void setHighroofCount(int highroofCount)
    {
        this.highroofCount = highroofCount;
    }

    public Map<Integer, Map<Integer, List<Integer>>> getCustomerOptionValues()
    {
        return customerOptionValues;
    }

    public void setCustomerOptionValues(Map<Integer, Map<Integer, List<Integer>>> customerOptionValues)
    {
        this.customerOptionValues = customerOptionValues;
    }

    public Map<Integer, Integer> getOptionalOptionValues()
    {
        return optionalOptionValues;
    }

    public void setOptionalOptionValues(Map<Integer, Integer> optionalOptionValues)
    {
        this.optionalOptionValues = optionalOptionValues;
    }

    public String getDemands()
    {
        return demands;
    }

    public void setDemands(String demands)
    {
        this.demands = demands;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public int getReserveStartDate()
    {
        return reserveStartDate;
    }

    public void setReserveStartDate(int reserveStartDate)
    {
        this.reserveStartDate = reserveStartDate;
    }

    public int getReserveEndDate()
    {
        return reserveEndDate;
    }

    public void setReserveEndDate(int reserveEndDate)
    {
        this.reserveEndDate = reserveEndDate;
    }

    public String getPlanName()
    {
        return planName;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public String getPlanPr()
    {
        return planPr;
    }

    public void setPlanPr(String planPr)
    {
        this.planPr = planPr;
    }

    public String getPrecaution()
    {
        return precaution;
    }

    public void setPrecaution(String precaution)
    {
        this.precaution = precaution;
    }

    public int getRoomSelectKind()
    {
        return roomSelectKind;
    }

    public void setRoomSelectKind(int roomSelectKind)
    {
        this.roomSelectKind = roomSelectKind;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public String getRankName()
    {
        return rankName;
    }

    public void setRankName(String rankName)
    {
        this.rankName = rankName;
    }

    public int getAdultAddChargeValue()
    {
        return adultAddChargeValue;
    }

    public void setAdultAddChargeValue(int adultAddChargeValue)
    {
        this.adultAddChargeValue = adultAddChargeValue;
    }

    public int getChildAddChargeValue()
    {
        return childAddChargeValue;
    }

    public void setChildAddChargeValue(int childAddChargeValue)
    {
        this.childAddChargeValue = childAddChargeValue;
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

    public int getPlanCharge()
    {
        return planCharge;
    }

    public void setPlanCharge(int planCharge)
    {
        this.planCharge = planCharge;
    }

    public int getPlanNumCharge()
    {
        return planNumCharge;
    }

    public void setPlanNumCharge(int planNumCharge)
    {
        this.planNumCharge = planNumCharge;
    }

    public int getMaxStayNum()
    {
        return maxStayNum;
    }

    public void setMaxStayNum(int maxStayNum)
    {
        this.maxStayNum = maxStayNum;
    }

    public int getMinStayNum()
    {
        return minStayNum;
    }

    public void setMinStayNum(int minStayNum)
    {
        this.minStayNum = minStayNum;
    }

    public int getMaxStayNumMan()
    {
        return maxStayNumMan;
    }

    public void setMaxStayNumMan(int maxStayNumMan)
    {
        this.maxStayNumMan = maxStayNumMan;
    }

    public int getMaxStayNumWoman()
    {
        return maxStayNumWoman;
    }

    public void setMaxStayNumWoman(int maxStayNumWoman)
    {
        this.maxStayNumWoman = maxStayNumWoman;
    }

    public int getMinStayNumMan()
    {
        return minStayNumMan;
    }

    public void setMinStayNumMan(int minStayNumMan)
    {
        this.minStayNumMan = minStayNumMan;
    }

    public int getMinStayNumWoman()
    {
        return minStayNumWoman;
    }

    public void setMinStayNumWoman(int minStayNumWoman)
    {
        this.minStayNumWoman = minStayNumWoman;
    }

    public int getMaxStayNumChild()
    {
        return maxStayNumChild;
    }

    public void setMaxStayNumChild(int maxStayNumChild)
    {
        this.maxStayNumChild = maxStayNumChild;
    }

    public int getHotelParking()
    {
        return hotelParking;
    }

    public void setHotelParking(int hotelParking)
    {
        this.hotelParking = hotelParking;
    }

    public int getReserveParking()
    {
        return reserveParking;
    }

    public void setReserveParking(int reserveParking)
    {
        this.reserveParking = reserveParking;
    }

    public boolean isPaymember()
    {
        return paymember;
    }

    public void setPaymember(boolean paymember)
    {
        this.paymember = paymember;
    }

    public int getAddMilePremium()
    {
        return addMilePremium;
    }

    public void setAddMilePremium(int addMilePremium)
    {
        this.addMilePremium = addMilePremium;
    }

    public int getAddMileFree()
    {
        return addMileFree;
    }

    public void setAddMileFree(int addMileFree)
    {
        this.addMileFree = addMileFree;
    }

    public int getBonusMile()
    {
        return bonusMile;
    }

    public void setBonusMile(int bonusMile)
    {
        this.bonusMile = bonusMile;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public int getConsumerDemandsKind()
    {
        return consumerDemandsKind;
    }

    public void setConsumerDemandsKind(int consumerDemandsKind)
    {
        this.consumerDemandsKind = consumerDemandsKind;
    }

    public String getCancelPolicy()
    {
        return cancelPolicy;
    }

    public void setCancelPolicy(String cancelPolicy)
    {
        this.cancelPolicy = cancelPolicy;
    }

    public String getUsedMile()
    {
        return usedMile;
    }

    public void setUsedMile(String usedMile)
    {
        this.usedMile = usedMile;
    }

    public int getChargeTotal()
    {
        return chargeTotal;
    }

    public void setChargeTotal(int chargeTotal)
    {
        this.chargeTotal = chargeTotal;
    }

    public int getHoldMile()
    {
        return holdMile;
    }

    public void setHoldMile(int holdMile)
    {
        this.holdMile = holdMile;
    }

    public int getPayment()
    {
        return payment;
    }

    public void setPayment(int payment)
    {
        this.payment = payment;
    }

    public String getCardNo()
    {
        return cardNo;
    }

    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    public int getExpireMonth()
    {
        return expireMonth;
    }

    public void setExpireMonth(int expireMonth)
    {
        this.expireMonth = expireMonth;
    }

    public int getExpireYear()
    {
        return expireYear;
    }

    public void setExpireYear(int expireYear)
    {
        this.expireYear = expireYear;
    }

    public String getSecurityCode()
    {
        return securityCode;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }

    public String getSmsPhoneNo()
    {
        return smsPhoneNo;
    }

    public void setSmsPhoneNo(String smsPhoneNo)
    {
        this.smsPhoneNo = smsPhoneNo;
    }

    public int getPaymentKind()
    {
        return paymentKind;
    }

    public void setPaymentKind(int paymentKind)
    {
        this.paymentKind = paymentKind;
    }

    public int getLocalPaymentKind()
    {
        return localPaymentKind;
    }

    public void setLocalPaymentKind(int localPaymentKind)
    {
        this.localPaymentKind = localPaymentKind;
    }

    public int getCardCompany()
    {
        return cardCompany;
    }

    public void setCardCompany(int cardCompany)
    {
        this.cardCompany = cardCompany;
    }

    public String getSalesName()
    {
        return salesName;
    }

    public void setSalesName(String salesName)
    {
        this.salesName = salesName;
    }

    public String getSmsPasscode()
    {
        return smsPasscode;
    }

    public void setSmsPasscode(String smsPasscode)
    {
        this.smsPasscode = smsPasscode;
    }

    public int getAgree()
    {
        return agree;
    }

    public void setAgree(int agree)
    {
        this.agree = agree;
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

    public int getPlanType()
    {
        return planType;
    }

    public void setPlanType(int planType)
    {
        this.planType = planType;
    }

    public int getReserveStartDayPremium()
    {
        return reserveStartDayPremium;
    }

    public void setReserveStartDayPremium(int reserveStartDayPremium)
    {
        this.reserveStartDayPremium = reserveStartDayPremium;
    }

    public int getReserveStartDayFree()
    {
        return reserveStartDayFree;
    }

    public void setReserveStartDayFree(int reserveStartDayFree)
    {
        this.reserveStartDayFree = reserveStartDayFree;
    }

    public int getReserveStartDay()
    {
        return reserveStartDay;
    }

    public void setReserveStartDay(int reserveStartDay)
    {
        this.reserveStartDay = reserveStartDay;
    }

    public int getReserveStartTime()
    {
        return reserveStartTime;
    }

    public void setReserveStartTime(int reserveStartTime)
    {
        this.reserveStartTime = reserveStartTime;
    }

    public int getReserveEndDay()
    {
        return reserveEndDay;
    }

    public void setReserveEndDay(int reserveEndDay)
    {
        this.reserveEndDay = reserveEndDay;
    }

    public int getReserveEndTime()
    {
        return reserveEndTime;
    }

    public void setReserveEndTime(int reserveEndTime)
    {
        this.reserveEndTime = reserveEndTime;
    }

    public int getReserveEndDatePremium()
    {
        return reserveEndDatePremium;
    }

    public void setReserveEndDatePremium(int reserveEndDatePremium)
    {
        this.reserveEndDatePremium = reserveEndDatePremium;
    }

    public int getReserveEndDateFree()
    {
        return reserveEndDateFree;
    }

    public void setReserveEndDateFree(int reserveEndDateFree)
    {
        this.reserveEndDateFree = reserveEndDateFree;
    }

    public int getSalesStartDate()
    {
        return salesStartDate;
    }

    public void setSalesStartDate(int salesStartDate)
    {
        this.salesStartDate = salesStartDate;
    }

    public int getSalesEndDate()
    {
        return salesEndDate;
    }

    public void setSalesEndDate(int salesEndDate)
    {
        this.salesEndDate = salesEndDate;
    }

    public int getAdultAddChargeKind()
    {
        return adultAddChargeKind;
    }

    public void setAdultAddChargeKind(int adultAddChargeKind)
    {
        this.adultAddChargeKind = adultAddChargeKind;
    }

    public int getAdultAddCharge()
    {
        return adultAddCharge;
    }

    public void setAdultAddCharge(int adultAddCharge)
    {
        this.adultAddCharge = adultAddCharge;
    }

    public int getChildAddChargeKind()
    {
        return childAddChargeKind;
    }

    public void setChildAddChargeKind(int childAddChargeKind)
    {
        this.childAddChargeKind = childAddChargeKind;
    }

    public int getChildAddCharge()
    {
        return childAddCharge;
    }

    public void setChildAddCharge(int childAddCharge)
    {
        this.childAddCharge = childAddCharge;
    }

    public int getMenOkFlag()
    {
        return menOkFlag;
    }

    public void setMenOkFlag(int menOkFlag)
    {
        this.menOkFlag = menOkFlag;
    }

    public String getTel2()
    {
        return tel2;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public int getBasicChargeTotal()
    {
        return basicChargeTotal;
    }

    public void setBasicChargeTotal(int basicChargeTotal)
    {
        this.basicChargeTotal = basicChargeTotal;
    }

    public int getOptionChargeTotal()
    {
        return optionChargeTotal;
    }

    public void setOptionChargeTotal(int optionChargeTotal)
    {
        this.optionChargeTotal = optionChargeTotal;
    }

    public boolean isSuccessCreditAuthority()
    {
        return successCreditAuthority;
    }

    public void setSuccessCreditAuthority(boolean successCreditAuthority)
    {
        this.successCreditAuthority = successCreditAuthority;
    }

    public String getTranid()
    {
        return tranid;
    }

    public void setTranid(String tranid)
    {
        this.tranid = tranid;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public String getHotenaviId()
    {
        return hotenaviId;
    }

    public void setHotenaviId(String hotenaviId)
    {
        this.hotenaviId = hotenaviId;
    }

    public int getComingFlag()
    {
        return comingFlag;
    }

    public void setComingFlag(int comingFlag)
    {
        this.comingFlag = comingFlag;
    }

    public String getReserveNoMain()
    {
        return reserveNoMain;
    }

    public void setReserveNoMain(String reserveNoMain)
    {
        this.reserveNoMain = reserveNoMain;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

}
