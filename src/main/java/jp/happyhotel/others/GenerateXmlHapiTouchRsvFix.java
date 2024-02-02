package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約確定情報
 */
public class GenerateXmlHapiTouchRsvFix extends WebApiResultBase
{
    // タグ名
    private static final String TAG_RSV_FIX        = "RsvFix";
    private static final String TAG_RESULT         = "Result";
    private static final String TAG_STATUS         = "Status";
    private static final String TAG_STATUS_VALUE   = "StatusValue";
    private static final String TAG_CIRESULT       = "CiResult";
    private static final String TAG_POINT          = "Point";
    private static final String TAG_MESSAGE        = "Message";
    private static final String TAG_REGIST_URL     = "RegistUrl";
    private static final String TAG_CICODE         = "CiCode";
    private static final String TAG_USERID         = "UserId";
    private static final String TAG_EMPLOYEE       = "Employee";
    private static final String TAG_CIDATE         = "CiDate";
    private static final String TAG_CITIME         = "CiTime";
    private static final String TAG_AMOUNT_RATE    = "AmountRate";
    private static final String TAG_ROOM_NO        = "RoomNo";
    private static final String TAG_ROOM_NAME      = "RoomName";
    private static final String TAG_USE_POINT      = "UsePoint";
    private static final String TAG_AMOUNT         = "Amount";
    private static final String TAG_SLIP_NO        = "SlipNo";
    private static final String TAG_RSV_NO         = "RsvNo";
    private static final String TAG_RSV_ROOM_NO    = "RsvRoomNo";
    private static final String TAG_USER_TYPE      = "UserType";
    private static final String TAG_PAID_CREDIT    = "PaidCredit";
    private static final String TAG_PAID_MILE      = "PaidMile";
    private static final String TAG_ERROR_CODE     = "ErrorCode";
    private static final String TAG_PMS_ALARM_FLAG = "PmsAlarmFlag";

    private String              tagName;
    private XmlTag              result;                             // 結果用タグ
    private XmlTag              status;                             // ステータス用タグ
    private XmlTag              statusValue;                        // ステータス用タグ
    private XmlTag              ciResult;                           // 来店ハピー結果用タグ（ハピー付与の場合のみ）
    private XmlTag              point;                              // ポイント用タグ
    private XmlTag              messsage;                           // 結果の応答メッセージ用タグ
    private XmlTag              registUrl;
    private XmlTag              ciCode;                             // チェックインコード
    private XmlTag              userId;                             // ユーザID(ホテル毎のユーザ管理番号)
    private XmlTag              ciDate;
    private XmlTag              ciTime;
    private XmlTag              amountRate;
    private XmlTag              roomNo;
    private XmlTag              roomName;
    private XmlTag              usePoint;
    private XmlTag              amount;
    private XmlTag              slipNo;
    private XmlTag              rsvNo;
    private XmlTag              rsvRoomNo;
    private XmlTag              userType;
    private XmlTag              paidCredit;
    private XmlTag              paidMile;
    private XmlTag              errorCode;
    private XmlTag              pmsAlarmFlag;

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_FIX );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, status );
        XmlTag.setParent( root, statusValue );
        XmlTag.setParent( root, ciResult );
        XmlTag.setParent( root, point );
        XmlTag.setParent( root, messsage );
        XmlTag.setParent( root, registUrl );
        XmlTag.setParent( root, ciCode );
        XmlTag.setParent( root, userId );
        XmlTag.setParent( root, ciDate );
        XmlTag.setParent( root, ciTime );
        XmlTag.setParent( root, amountRate );
        XmlTag.setParent( root, roomNo );
        XmlTag.setParent( root, roomName );
        XmlTag.setParent( root, usePoint );
        XmlTag.setParent( root, amount );
        XmlTag.setParent( root, slipNo );
        XmlTag.setParent( root, rsvNo );
        XmlTag.setParent( root, rsvRoomNo );
        XmlTag.setParent( root, userType );
        XmlTag.setParent( root, paidCredit );
        XmlTag.setParent( root, paidMile );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, pmsAlarmFlag );

        return;
    }

    public void setResult(String fixResult)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, fixResult );
    }

    public void setStatus(String status)
    {
        this.status = XmlTag.createXmlTag( TAG_STATUS, status );
    }

    public void setStatusValue(int statusValue)
    {
        this.statusValue = XmlTag.createXmlTag( TAG_STATUS_VALUE, statusValue );
    }

    public void setCiResult(String result)
    {
        this.ciResult = XmlTag.createXmlTag( TAG_CIRESULT, result );
    }

    public void setPoint(int point)
    {
        this.point = XmlTag.createXmlTag( TAG_POINT, point );
    }

    public void setMessage(String messsage)
    {
        this.messsage = XmlTag.createXmlTag( TAG_MESSAGE, messsage );
    }

    public void setRegistUrl(String registUrl)
    {
        this.registUrl = XmlTag.createXmlTag( TAG_REGIST_URL, registUrl );
    }

    public void setCiCode(int cicode)
    {
        this.ciCode = XmlTag.createXmlTag( TAG_CICODE, cicode );
    }

    public void setUserId(String userid)
    {
        this.userId = XmlTag.createXmlTag( TAG_USERID, userid );
    }

    public void setCiDate(int cidate)
    {
        this.ciDate = XmlTag.createXmlTag( TAG_CIDATE, cidate );
    }

    public void setCiTime(int citime)
    {
        this.ciTime = XmlTag.createXmlTag( TAG_CITIME, citime );
    }

    public void setAmountRate(double rate)
    {
        if ( rate == 0 )
        {
            rate = 1;
        }
        this.amountRate = XmlTag.createXmlTag( TAG_AMOUNT_RATE, rate );
    }

    public void setRoomNo(String roomno)
    {
        this.roomNo = XmlTag.createXmlTag( TAG_ROOM_NO, roomno );
    }

    public void setRoomName(String roomname)
    {
        this.roomName = XmlTag.createXmlTag( TAG_ROOM_NAME, roomname );
    }

    public void setUsePoint(double usePoint)
    {
        if ( usePoint > 0 )
        {
            this.usePoint = XmlTag.createXmlTag( TAG_USE_POINT, usePoint );
        }
    }

    public void setAmount(int amount)
    {
        if ( amount > 0 )
        {
            this.amount = XmlTag.createXmlTag( TAG_AMOUNT, amount );
        }
    }

    public void setSlipNo(int slipno)
    {
        if ( slipno > 0 )
        {
            this.slipNo = XmlTag.createXmlTag( TAG_SLIP_NO, slipno );
        }
    }

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = XmlTag.createXmlTag( TAG_RSV_NO, rsvNo );
    }

    public void setRsvRoomNo(String rsvRoomNo)
    {
        this.rsvRoomNo = XmlTag.createXmlTag( TAG_RSV_ROOM_NO, rsvRoomNo );
    }

    public void setUserType(int usertype)
    {
        this.userType = XmlTag.createXmlTag( TAG_USER_TYPE, usertype );
    }

    public void setPaidCredit(int credit)
    {
        this.paidCredit = XmlTag.createXmlTag( TAG_PAID_CREDIT, credit );
    }

    public void setPaidMile(int mile)
    {
        this.paidMile = XmlTag.createXmlTag( TAG_PAID_MILE, mile );
    }

    public void setErrorCode(int errCode)
    {
        if ( errCode != 0 )
        {
            this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
        }
    }

    public void setPmsAlarmFlag(String pmsAlarmFlag)
    {
        if ( !pmsAlarmFlag.equals( "" ) )
        {
            this.pmsAlarmFlag = XmlTag.createXmlTag( TAG_PMS_ALARM_FLAG, pmsAlarmFlag );
        }
    }

}
