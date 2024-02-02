package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchVisitCancel extends WebApiResultBase
{
    // タグ名
    private static final String                              TAG_VISIT       = "VisitCancel";
    private static final String                              TAG_RESULT      = "Result";
    private static final String                              TAG_CISTATUS    = "CiStatus";
    private static final String                              TAG_ERROR_CODE  = "ErrorCode";
    private static final String                              TAG_CICODE      = "CiCode";
    private static final String                              TAG_USERID      = "UserId";
    private static final String                              TAG_CIDATE      = "CiDate";
    private static final String                              TAG_CITIME      = "CiTime";
    private static final String                              TAG_AMOUNT_RATE = "AmountRate";
    private static final String                              TAG_ROOM_NO     = "RoomNo";
    private static final String                              TAG_USE_POINT   = "UsePoint";
    private static final String                              TAG_AMOUNT      = "Amount";
    private static final String                              TAG_SLIP_NO     = "SlipNo";
    private static final String                              TAG_RSV_NO      = "RsvNo";
    private static final String                              TAG_USER_TYPE   = "UserType";

    private String                                           tagName;
    private XmlTag                                           result;                                                                  // 結果用タグ
    private XmlTag                                           ciStatus;                                                                // チェックインコード
    private XmlTag                                           errorCode;                                                               // エラーコード
    private XmlTag                                           ciCode;                                                                  // チェックインコード
    private XmlTag                                           userId;                                                                  // ユーザID(ホテル毎のユーザ管理番号)
    private XmlTag                                           ciDate;
    private XmlTag                                           ciTime;
    private XmlTag                                           amountRate;
    private XmlTag                                           roomNo;
    private XmlTag                                           usePoint;
    private XmlTag                                           amount;
    private XmlTag                                           slipNo;
    private XmlTag                                           rsvNo;
    private XmlTag                                           userType;
    private ArrayList<GenerateXmlHapiTouchHotelInfoEmployee> employeeList    = new ArrayList<GenerateXmlHapiTouchHotelInfoEmployee>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_VISIT );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, ciStatus );
        XmlTag.setParent( root, ciCode );
        XmlTag.setParent( root, userId );
        XmlTag.setParent( root, ciDate );
        XmlTag.setParent( root, ciTime );
        XmlTag.setParent( root, amountRate );
        XmlTag.setParent( root, roomNo );
        XmlTag.setParent( root, usePoint );
        XmlTag.setParent( root, amount );
        XmlTag.setParent( root, slipNo );
        XmlTag.setParent( root, rsvNo );
        XmlTag.setParent( root, userType );
        XmlTag.setParent( root, errorCode );

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

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setCiStatus(int ciStatus)
    {
        this.ciStatus = XmlTag.createXmlTag( TAG_CISTATUS, ciStatus );
    }

    public void setCiCode(int cicode)
    {
        this.ciCode = XmlTag.createXmlTag( TAG_CICODE, cicode );
    }

    public void setUserId(int userid)
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

    public void setUsePoint(int usePoint)
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

    public void setRsvNo(String rsvno)
    {
        this.rsvNo = XmlTag.createXmlTag( TAG_RSV_NO, rsvno );
    }

    public void setUserType(int userType)
    {
        this.rsvNo = XmlTag.createXmlTag( TAG_USER_TYPE, userType );
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errorCode );
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errorCode );
    }

    public void setEmployee(GenerateXmlHapiTouchHotelInfoEmployee employee)
    {
        this.employeeList.add( employee );
        return;
    }

}
