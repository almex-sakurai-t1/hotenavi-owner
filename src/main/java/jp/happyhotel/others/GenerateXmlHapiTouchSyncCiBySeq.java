package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchSyncCiBySeq extends WebApiResultBase
{
    // タグ名
    private static final String                              TAG_SYNC_CI_BY_DATA = "SyncCiBySeq";
    private static final String                              TAG_CICODE          = "CiCode";
    private static final String                              TAG_CIDATE          = "CiDate";
    private static final String                              TAG_CITIME          = "CiTime";
    private static final String                              TAG_USERID          = "UserId";
    private static final String                              TAG_POINT           = "Point";
    private static final String                              TAG_AMOUNT_RATE     = "AmountRate";
    private static final String                              TAG_ROOM_NO         = "RoomNo";
    private static final String                              TAG_USE_POINT       = "UsePoint";
    private static final String                              TAG_AMOUNT          = "Amount";
    private static final String                              TAG_SLIP_NO         = "SlipNo";
    private static final String                              TAG_CI_STATUS       = "CiStatus";
    private static final String                              TAG_USER_TYPE       = "UserType";
    private static final String                              TAG_DEL_FLAG        = "DelFlag";
    private static final String                              TAG_RSV_NO          = "RsvNo";
    private static final String                              TAG_USE_TEMP_FLAG   = "UseTempFlag";
    private static final String                              TAG_EMPLOYEE        = "Employee";
    private static final String                              TAG_APP_STATUS      = "AppStatus";

    private String                                           tagName;
    private XmlTag                                           syncCiBySeq;
    private XmlTag                                           ciCode;                                                                      // チェックインコード
    private XmlTag                                           ciDate;
    private XmlTag                                           ciTime;
    private XmlTag                                           userId;                                                                      // ユーザID(ホテル毎のユーザ管理番号)
    private XmlTag                                           point;                                                                       // ポイント用タグ
    private XmlTag                                           amountRate;
    private XmlTag                                           roomNo;
    private XmlTag                                           usePoint;
    private XmlTag                                           amount;
    private XmlTag                                           slipNo;
    private XmlTag                                           ciStatus;
    private XmlTag                                           userType;
    private XmlTag                                           rsvNo;
    private XmlTag                                           useTempFlag;
    private XmlTag                                           delFlag;
    private XmlTag                                           appStatus;

    private ArrayList<GenerateXmlHapiTouchHotelInfoEmployee> employeeList        = new ArrayList<GenerateXmlHapiTouchHotelInfoEmployee>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_SYNC_CI_BY_DATA );
        XmlTag.setParent( root, ciCode );
        XmlTag.setParent( root, ciDate );
        XmlTag.setParent( root, ciTime );
        XmlTag.setParent( root, userId );
        XmlTag.setParent( root, point );
        XmlTag.setParent( root, amountRate );
        XmlTag.setParent( root, roomNo );
        XmlTag.setParent( root, usePoint );
        XmlTag.setParent( root, amount );
        XmlTag.setParent( root, slipNo );
        XmlTag.setParent( root, ciStatus );
        XmlTag.setParent( root, userType );
        XmlTag.setParent( root, delFlag );
        XmlTag.setParent( root, rsvNo );
        XmlTag.setParent( root, useTempFlag );
        XmlTag.setParent( root, appStatus );
        if ( employeeList != null )
        {
            for( int i = 0 ; i < employeeList.size() ; i++ )
            {
                this.employeeList.get( i ).setRootNode( root );
                this.employeeList.get( i ).initXmlNodeInfo();
            }
        }
        return;
    }

    public void setPoint(int point)
    {
        this.point = XmlTag.createXmlTag( TAG_POINT, point );
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
        if ( roomno.equals( "" ) == false )
        {
            this.roomNo = XmlTag.createXmlTagNoCheck( TAG_ROOM_NO, roomno );
        }
    }

    public void setUsePoint(int usePoint)
    {
        // 使用したかどうかを明確にするため
        if ( usePoint > 0 )
        {
            this.usePoint = XmlTag.createXmlTag( TAG_USE_POINT, usePoint );
        }
    }

    public void setAmount(int amount)
    {
        if ( amount >= 0 )
        {
            this.amount = XmlTag.createXmlTag( TAG_AMOUNT, amount );
        }
    }

    public void setSlipNo(int slipno)
    {
        if ( slipno >= 0 )
        {
            this.slipNo = XmlTag.createXmlTag( TAG_SLIP_NO, slipno );
        }
    }

    public void setCiStatus(int status)
    {
        if ( status >= 0 )
        {
            this.ciStatus = XmlTag.createXmlTag( TAG_CI_STATUS, status );
        }
    }

    public void setUserType(int userType)
    {
        this.userType = XmlTag.createXmlTag( TAG_USER_TYPE, userType );
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = XmlTag.createXmlTag( TAG_DEL_FLAG, delFlag );
    }

    public void setRsvNo(String rsvno)
    {
        this.rsvNo = XmlTag.createXmlTag( TAG_RSV_NO, rsvno );
    }

    public void setUseTempFlag(int useTempFlag)
    {
        this.useTempFlag = XmlTag.createXmlTag( TAG_USE_TEMP_FLAG, useTempFlag );
    }

    public void setAppStatus(int appStatus)
    {
        this.appStatus = XmlTag.createXmlTag( TAG_APP_STATUS, appStatus );
    }
}
