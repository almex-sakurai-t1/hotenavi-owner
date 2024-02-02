package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約データデータ
 */
public class GenerateXmlHapiTouchRsvDataSub extends WebApiResultBase
{
    // タグ名
    private static final String                         TAG_DATA               = "Data";
    private static final String                         TAG_HOTEL_ID           = "HotelId";
    private static final String                         TAG_RSV_DATE           = "RsvDate";
    private static final String                         TAG_RSV_DATE_VALUE     = "RsvDateValue";
    private static final String                         TAG_RSV_NO             = "RsvNo";
    private static final String                         TAG_PLAN_NO            = "PlanNo";
    private static final String                         TAG_PLAN_NAME          = "PlanName";
    private static final String                         TAG_ARRIVAL_DATE_VALUE = "ArrivalDateValue";
    private static final String                         TAG_ARRIVAL_TIME       = "ArrivalTime";
    private static final String                         TAG_ARRIVAL_TIME_VALUE = "ArrivalTimeValue";
    private static final String                         TAG_ROOM_NO            = "RoomNo";
    private static final String                         TAG_ROOM_NAME          = "RoomName";
    private static final String                         TAG_OPTION             = "Option";
    private static final String                         TAG_USER_ID            = "UserId";
    private static final String                         TAG_USER_NAME          = "UserName";
    private static final String                         TAG_HANDLE_NAME        = "HandleName";
    private static final String                         TAG_TEL                = "Tel";
    private static final String                         TAG_STATUS             = "Status";
    private static final String                         TAG_STATUS_VALUE       = "StatusValue";
    private static final String                         TAG_DISP_STATUS        = "DispStatus";
    private static final String                         TAG_FELICA_FLAG        = "FelicaFlag";
    private static final String                         TAG_CHARGE_FLAG        = "ChargeFlag";
    private static final String                         TAG_PAID_FLAG          = "PaidFlag";
    private static final String                         TAG_TOTAL_PRICE        = "TotalPrice";

    private XmlTag                                      data;
    private XmlTag                                      hotelId;
    private XmlTag                                      rsvDate;
    private XmlTag                                      rsvDateValue;
    private XmlTag                                      rsvNo;
    private XmlTag                                      planName;
    private XmlTag                                      arrivalDateValue;
    private XmlTag                                      arrivalTime;
    private XmlTag                                      arrivalTimeValue;
    private XmlTag                                      roomNo;
    private XmlTag                                      roomName;
    private XmlTag                                      option;
    private XmlTag                                      userId;
    private XmlTag                                      userName;
    private XmlTag                                      handleName;
    private XmlTag                                      tel;
    private XmlTag                                      status;
    private XmlTag                                      statusValue;
    private XmlTag                                      dispStatus;
    private XmlTag                                      felicaFlag;
    private XmlTag                                      chargeFlag;
    private XmlTag                                      paidFlag;
    private XmlTag                                      totalPrice;

    private ArrayList<GenerateXmlHapiTouchGetRsvRoomNo> roomNoList             = new ArrayList<GenerateXmlHapiTouchGetRsvRoomNo>();

    @Override
    protected void initXmlNodeInfo()
    {
        data = createRootChild( TAG_DATA );
        XmlTag.setParent( data, hotelId );
        XmlTag.setParent( data, rsvDate );
        XmlTag.setParent( data, rsvDateValue );
        XmlTag.setParent( data, rsvNo );
        XmlTag.setParent( data, planName );
        XmlTag.setParent( data, arrivalDateValue );
        XmlTag.setParent( data, arrivalTime );
        XmlTag.setParent( data, arrivalTimeValue );
        XmlTag.setParent( data, roomNo );
        XmlTag.setParent( data, roomName );
        XmlTag.setParent( data, option );
        XmlTag.setParent( data, userId );
        XmlTag.setParent( data, userName );
        XmlTag.setParent( data, handleName );
        XmlTag.setParent( data, tel );
        XmlTag.setParent( data, status );
        XmlTag.setParent( data, statusValue );
        XmlTag.setParent( data, dispStatus );
        XmlTag.setParent( data, felicaFlag );
        XmlTag.setParent( data, chargeFlag );
        XmlTag.setParent( data, paidFlag );
        XmlTag.setParent( data, totalPrice );
        return;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = XmlTag.createXmlTag( TAG_HOTEL_ID, hotelId );
    }

    public void setRsvDate(String rsvDate)
    {
        this.rsvDate = XmlTag.createXmlTag( TAG_RSV_DATE, rsvDate );
    }

    public void setRsvDateValue(int rsvDateValue)
    {
        this.rsvDateValue = XmlTag.createXmlTag( TAG_RSV_DATE_VALUE, rsvDateValue );
    }

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = XmlTag.createXmlTag( TAG_RSV_NO, rsvNo );
    }

    public void setPlanName(String planName)
    {
        this.planName = XmlTag.createXmlTag( TAG_PLAN_NAME, planName );
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

    public void setRoomNo(int roomNo)
    {
        this.roomNo = XmlTag.createXmlTag( TAG_ROOM_NO, roomNo );
    }

    public void setRoomName(String roomName)
    {
        this.roomName = XmlTag.createXmlTag( TAG_ROOM_NAME, roomName );
    }

    public void setOption(String option)
    {
        this.option = XmlTag.createXmlTag( TAG_OPTION, option );
    }

    public void setUserId(String userId)
    {
        this.userId = XmlTag.createXmlTag( TAG_USER_ID, userId );
    }

    public void setUserName(String userName)
    {
        this.userName = XmlTag.createXmlTag( TAG_USER_NAME, userName );
    }

    public void setHandleName(String handleName)
    {
        this.handleName = XmlTag.createXmlTag( TAG_HANDLE_NAME, handleName );
    }

    public void setTel(String tel)
    {
        this.tel = XmlTag.createXmlTag( TAG_TEL, tel );
    }

    public void setStatus(String status)
    {
        this.status = XmlTag.createXmlTag( TAG_STATUS, status );
    }

    public void setStatusValue(Integer statusValue)
    {
        this.statusValue = XmlTag.createXmlTag( TAG_STATUS_VALUE, statusValue );
    }

    public void setDispStatus(int dispStatus)
    {
        this.dispStatus = XmlTag.createXmlTag( TAG_DISP_STATUS, dispStatus );
    }

    public void setFelicaFlag(int felica)
    {
        this.felicaFlag = XmlTag.createXmlTag( TAG_FELICA_FLAG, felica );
    }

    public void setCharegeFlag(int charge)
    {
        this.chargeFlag = XmlTag.createXmlTag( TAG_CHARGE_FLAG, charge );
    }

    public void setPaidFlag(int paid)
    {
        this.paidFlag = XmlTag.createXmlTag( TAG_PAID_FLAG, paid );
    }

    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = XmlTag.createXmlTag( TAG_TOTAL_PRICE, totalPrice );
    }

}
