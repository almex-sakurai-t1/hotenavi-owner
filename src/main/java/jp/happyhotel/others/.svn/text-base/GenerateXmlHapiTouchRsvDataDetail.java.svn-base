package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約データ取得
 */
public class GenerateXmlHapiTouchRsvDataDetail extends WebApiResultBase
{
    // タグ名
    private static final String                                          TAG_RSV_DATA_DETAIL    = "RsvDataDetail";
    private static final String                                          TAG_RSV_NO             = "RsvNo";
    private static final String                                          TAG_HOTEL_ID           = "HotelId";
    private static final String                                          TAG_HOTEL_NAME         = "HotelName";
    private static final String                                          TAG_HOTEL_ADDR         = "HotelAddr";
    private static final String                                          TAG_HOTEL_TEL          = "HotelTel";
    private static final String                                          TAG_PLAN_NAME          = "PlanName";
    private static final String                                          TAG_PLAN_PR            = "PlanPr";
    private static final String                                          TAG_RSV_DATE           = "RsvDate";
    private static final String                                          TAG_RSV_DATE_VALUE     = "RsvDateValue";
    private static final String                                          TAG_ROOM_NO            = "RoomNo";
    private static final String                                          TAG_ROOM_NAME          = "RoomName";
    private static final String                                          TAG_RANK_NAME          = "RankName";
    private static final String                                          TAG_ARRIVAL_DATE_VALUE = "ArrivalDateValue";
    private static final String                                          TAG_ARRIVAL_TIME       = "ArrivalTime";
    private static final String                                          TAG_ARRIVAL_TIME_VALUE = "ArrivalTimeValue";
    private static final String                                          TAG_PRICE              = "Price";
    private static final String                                          TAG_ROOM_PRICE         = "RoomPrice";
    private static final String                                          TAG_ADULT_NUM          = "AdultNum";
    private static final String                                          TAG_CHILD_NUM          = "ChildNum";

    private static final String                                          TAG_OPTION_PRICE       = "OptionPrice";
    private static final String                                          TAG_TOTAL_PRICE        = "TotalPrice";
    private static final String                                          TAG_IMPERATIVE_OPTION  = "ImperativeOption";
    private static final String                                          TAG_CI_TIME_FROM       = "CiTimeFrom";
    private static final String                                          TAG_CI_TIME_FROM_VALUE = "CiTimeFromValue";
    private static final String                                          TAG_CI_TIME_TO         = "CiTimeTo";
    private static final String                                          TAG_CI_TIME_TO_VALUE   = "CiTimeToValue";
    private static final String                                          TAG_CO_TIME            = "CoTime";
    private static final String                                          TAG_STATUS             = "Status";
    private static final String                                          TAG_STATUS_VALUE       = "StatusValue";
    private static final String                                          TAG_CANCEL_KIND        = "CancelKind";
    private static final String                                          TAG_CAR                = "Car";
    private static final String                                          TAG_USER_ID            = "UserId";
    private static final String                                          TAG_MAIL_ADDR          = "MailAddr";
    private static final String                                          TAG_MAIL_REMINDER      = "MailReminder";
    private static final String                                          TAG_USER_NAME          = "UserName";
    private static final String                                          TAG_USER_NAME_KANA     = "UserNameKana";
    private static final String                                          TAG_ZIP_CODE           = "ZipCode";
    private static final String                                          TAG_ADDRESS            = "Address";
    private static final String                                          TAG_TEL                = "Tel";
    private static final String                                          TAG_DEMANDS            = "Demands";
    private static final String                                          TAG_REMARKS            = "Remarks";
    private static final String                                          TAG_CANCEL_POLICY      = "CancelPolicy";
    private static final String                                          TAG_CHARGE_FLAG        = "ChargeFlag";
    private static final String                                          TAG_PAID_CREDIT        = "PaidCredit";
    private static final String                                          TAG_PAID_MILE          = "PaidMile";
    private static final String                                          TAG_ERROR_CODE         = "ErrorCode";
    private static final String                                          TAG_CI_CODE            = "CiCode";

    private XmlTag                                                       rsvDataDetail;
    private XmlTag                                                       rsvNo;
    private XmlTag                                                       hotelId;
    private XmlTag                                                       hotelName;
    private XmlTag                                                       hotelAddr;
    private XmlTag                                                       hotelTel;
    private XmlTag                                                       planName;
    private XmlTag                                                       planPr;
    private XmlTag                                                       rsvDate;
    private XmlTag                                                       rsvDateValue;
    private XmlTag                                                       roomNo;
    private XmlTag                                                       roomName;
    private XmlTag                                                       rankName;
    private XmlTag                                                       arrivalDateValue;
    private XmlTag                                                       arrivalTime;
    private XmlTag                                                       arrivalTimeValue;
    private XmlTag                                                       roomPrice;
    private XmlTag                                                       adultNum;
    private XmlTag                                                       childNum;
    private XmlTag                                                       totalPrice;
    private XmlTag                                                       ciDateValue;
    private XmlTag                                                       ciTimeFrom;
    private XmlTag                                                       ciTimeFromValue;
    private XmlTag                                                       ciTimeTo;
    private XmlTag                                                       ciTimeToValue;
    private XmlTag                                                       coTime;
    private XmlTag                                                       status;
    private XmlTag                                                       statusValue;
    private XmlTag                                                       cancelKind;
    private XmlTag                                                       car;
    private XmlTag                                                       userId;
    private XmlTag                                                       mailAddr;
    private XmlTag                                                       mailReminder;
    private XmlTag                                                       userName;
    private XmlTag                                                       userNameKana;
    private XmlTag                                                       zipCode;
    private XmlTag                                                       address;
    private XmlTag                                                       tel;
    private XmlTag                                                       demands;
    private XmlTag                                                       remarks;
    private XmlTag                                                       cancelPolicy;
    private XmlTag                                                       chargeFlag;
    private XmlTag                                                       paidCredit;
    private XmlTag                                                       paidMile;
    private XmlTag                                                       errorCode;
    private XmlTag                                                       ciCode;

    private ArrayList<GenerateXmlHapiTouchRsvDataDetailOptionPrice>      optionPriceList        = new ArrayList<GenerateXmlHapiTouchRsvDataDetailOptionPrice>();
    private ArrayList<GenerateXmlHapiTouchRsvDataDetailImperativeOption> imperativeOptionList   = new ArrayList<GenerateXmlHapiTouchRsvDataDetailImperativeOption>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_DATA_DETAIL );
        XmlTag.setParent( root, rsvNo );
        XmlTag.setParent( root, hotelId );
        XmlTag.setParent( root, hotelName );
        XmlTag.setParent( root, hotelAddr );
        XmlTag.setParent( root, hotelTel );
        XmlTag.setParent( root, planName );
        XmlTag.setParent( root, planPr );
        XmlTag.setParent( root, rsvDate );
        XmlTag.setParent( root, rsvDateValue );
        XmlTag.setParent( root, roomNo );
        XmlTag.setParent( root, roomName );
        XmlTag.setParent( root, rankName );
        XmlTag.setParent( root, arrivalDateValue );
        XmlTag.setParent( root, arrivalTime );
        XmlTag.setParent( root, arrivalTimeValue );
        XmlTag.setParent( root, roomPrice );
        XmlTag.setParent( root, adultNum );
        XmlTag.setParent( root, childNum );
        for( int i = 0 ; i < optionPriceList.size() ; i++ )
        {
            this.optionPriceList.get( i ).setRootNode( root );
            this.optionPriceList.get( i ).initXmlNodeInfo();
        }
        XmlTag.setParent( root, totalPrice );
        for( int i = 0 ; i < imperativeOptionList.size() ; i++ )
        {
            this.imperativeOptionList.get( i ).setRootNode( root );
            this.imperativeOptionList.get( i ).initXmlNodeInfo();
        }
        // チェックイン開始時間
        XmlTag.setParent( root, ciTimeFrom );
        XmlTag.setParent( root, ciTimeFromValue );
        // チェックイン終了時間
        XmlTag.setParent( root, ciTimeTo );
        XmlTag.setParent( root, ciTimeToValue );
        XmlTag.setParent( root, coTime );
        XmlTag.setParent( root, status );
        XmlTag.setParent( root, statusValue );
        XmlTag.setParent( root, cancelKind );
        XmlTag.setParent( root, car );
        XmlTag.setParent( root, userId );
        XmlTag.setParent( root, mailAddr );
        XmlTag.setParent( root, mailReminder );
        XmlTag.setParent( root, userName );
        XmlTag.setParent( root, userNameKana );
        XmlTag.setParent( root, zipCode );
        XmlTag.setParent( root, address );
        XmlTag.setParent( root, tel );
        XmlTag.setParent( root, demands );
        XmlTag.setParent( root, remarks );
        XmlTag.setParent( root, cancelPolicy );
        XmlTag.setParent( root, chargeFlag );
        XmlTag.setParent( root, paidCredit );
        XmlTag.setParent( root, paidMile );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, ciCode );

        return;
    }

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = XmlTag.createXmlTag( TAG_RSV_NO, rsvNo );
    }

    public void setHotelId(int hotelID)
    {
        this.hotelId = XmlTag.createXmlTag( TAG_HOTEL_ID, hotelID );
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = XmlTag.createXmlTag( TAG_HOTEL_NAME, hotelName );
    }

    public void setHotelAddr(String hotelAddr)
    {
        this.hotelAddr = XmlTag.createXmlTag( TAG_HOTEL_ADDR, hotelAddr );
    }

    public void setHotelTel(String hotelTel)
    {
        this.hotelTel = XmlTag.createXmlTag( TAG_HOTEL_TEL, hotelTel );
    }

    public void setPlanName(String planName)
    {
        this.planName = XmlTag.createXmlTag( TAG_PLAN_NAME, planName );
    }

    public void setPlanPr(String planPr)
    {
        this.planPr = XmlTag.createXmlTag( TAG_PLAN_PR, planPr );
    }

    public void setRsvDate(String rsvDate)
    {
        this.rsvDate = XmlTag.createXmlTag( TAG_RSV_DATE, rsvDate );
    }

    public void setRsvDateValue(int rsvDateValue)
    {
        this.rsvDateValue = XmlTag.createXmlTag( TAG_RSV_DATE_VALUE, rsvDateValue );
    }

    public void setRoomNo(int roomNo)
    {
        this.roomNo = XmlTag.createXmlTag( TAG_ROOM_NO, roomNo );
    }

    public void setRoomName(String roomName)
    {
        this.roomName = XmlTag.createXmlTag( TAG_ROOM_NAME, roomName );
    }

    public void setRankName(String rankName)
    {
        this.rankName = XmlTag.createXmlTag( TAG_RANK_NAME, rankName );
    }

    public void setArrivalDateValue(int arrDateVal)
    {
        this.arrivalDateValue = XmlTag.createXmlTag( TAG_ARRIVAL_DATE_VALUE, arrDateVal );
    }

    public void setArrivalTime(String arrTime)
    {
        this.arrivalTime = XmlTag.createXmlTag( TAG_ARRIVAL_TIME, arrTime );
    }

    public void setArrivalTimeValue(int arrTimeVal)
    {
        this.arrivalTimeValue = XmlTag.createXmlTag( TAG_ARRIVAL_TIME_VALUE, arrTimeVal );
    }

    public void setRoomPrice(String roomPrice)
    {
        this.roomPrice = XmlTag.createXmlTag( TAG_ROOM_PRICE, roomPrice );
    }

    public void setAdultNum(String adultNum)
    {
        this.adultNum = XmlTag.createXmlTag( TAG_ADULT_NUM, adultNum );
    }

    public void setChildNum(String childNum)
    {
        this.childNum = XmlTag.createXmlTag( TAG_CHILD_NUM, childNum );
    }

    public void setOptionPrice(GenerateXmlHapiTouchRsvDataDetailOptionPrice optionPrice)
    {
        this.optionPriceList.add( optionPrice );
    }

    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = XmlTag.createXmlTag( TAG_TOTAL_PRICE, totalPrice );
    }

    public void setImperativeOption(GenerateXmlHapiTouchRsvDataDetailImperativeOption imperativeOption)
    {
        this.imperativeOptionList.add( imperativeOption );
    }

    public void setCiTimeFrom(String ciTime)
    {
        this.ciTimeFrom = XmlTag.createXmlTag( TAG_CI_TIME_FROM, ciTime );
    }

    public void setCiTimeFromValue(int ciTime)
    {
        this.ciTimeFromValue = XmlTag.createXmlTag( TAG_CI_TIME_FROM_VALUE, ciTime );
    }

    public void setCiTimeTo(String ciTime)
    {
        this.ciTimeTo = XmlTag.createXmlTag( TAG_CI_TIME_TO, ciTime );
    }

    public void setCiTimeToValue(int ciTime)
    {
        this.ciTimeToValue = XmlTag.createXmlTag( TAG_CI_TIME_TO_VALUE, ciTime );
    }

    public void setCoTime(String coTime)
    {
        this.coTime = XmlTag.createXmlTag( TAG_CO_TIME, coTime );
    }

    public void setStatus(String status)
    {
        this.status = XmlTag.createXmlTag( TAG_STATUS, status );
    }

    public void setStatusValue(int statusValue)
    {
        this.statusValue = XmlTag.createXmlTag( TAG_STATUS_VALUE, statusValue );
    }

    public void setCancelKind(int cancelkind)
    {
        this.cancelKind = XmlTag.createXmlTag( TAG_CANCEL_KIND, cancelkind );
    }

    public void setCar(String car)
    {
        this.car = XmlTag.createXmlTag( TAG_CAR, car );
    }

    public void setUserId(String userId)
    {
        this.userId = XmlTag.createXmlTag( TAG_USER_ID, userId );
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = XmlTag.createXmlTag( TAG_MAIL_ADDR, mailAddr );
    }

    public void setMailReminder(String mailReminder)
    {
        this.mailReminder = XmlTag.createXmlTag( TAG_MAIL_REMINDER, mailReminder );
    }

    public void setUserName(String userName)
    {
        this.userName = XmlTag.createXmlTag( TAG_USER_NAME, userName );
    }

    public void setUserNameKana(String userNameKana)
    {
        this.userNameKana = XmlTag.createXmlTag( TAG_USER_NAME_KANA, userNameKana );
    }

    public void setZipCode(String zip)
    {
        this.zipCode = XmlTag.createXmlTag( TAG_ZIP_CODE, zip );
    }

    public void setAddress(String address)
    {
        this.address = XmlTag.createXmlTag( TAG_ADDRESS, address );
    }

    public void setTel(String tel)
    {
        this.tel = XmlTag.createXmlTag( TAG_TEL, tel );
    }

    public void setDemands(String demands)
    {
        this.demands = XmlTag.createXmlTag( TAG_DEMANDS, demands );
    }

    public void setRemarks(String remarks)
    {
        this.remarks = XmlTag.createXmlTag( TAG_REMARKS, remarks );
    }

    public void setCancelPolicy(String cancelPolicy)
    {
        this.cancelPolicy = XmlTag.createXmlTag( TAG_CANCEL_POLICY, cancelPolicy );
    }

    public void setChargeFlag(int charge)
    {
        this.chargeFlag = XmlTag.createXmlTag( TAG_CHARGE_FLAG, charge );
    }

    public void setPaidCredit(int credit)
    {
        this.paidCredit = XmlTag.createXmlTag( TAG_PAID_CREDIT, credit );
    }

    public void setPaidMile(int mile)
    {
        this.paidMile = XmlTag.createXmlTag( TAG_PAID_MILE, mile );
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errorCode );
    }

    public void setCiCode(int ciCode)
    {
        this.ciCode = XmlTag.createXmlTag( TAG_CI_CODE, ciCode );
    }

}
