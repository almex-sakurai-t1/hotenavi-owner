package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchVisit extends WebApiResultBase
{
    // タグ名
    private static final String                              TAG_VISIT       = "Visit";
    private static final String                              TAG_RESULT      = "Result";
    private static final String                              TAG_CIRESULT    = "CiResult";
    private static final String                              TAG_POINT       = "Point";
    private static final String                              TAG_MESSAGE     = "Message";
    private static final String                              TAG_REGIST_URL  = "RegistUrl";
    private static final String                              TAG_CICODE      = "CiCode";
    private static final String                              TAG_USERID      = "UserId";
    private static final String                              TAG_EMPLOYEE    = "Employee";
    private static final String                              TAG_CIDATE      = "CiDate";
    private static final String                              TAG_CITIME      = "CiTime";
    private static final String                              TAG_AMOUNT_RATE = "AmountRate";
    private static final String                              TAG_ROOM_NO     = "RoomNo";
    private static final String                              TAG_USE_POINT   = "UsePoint";
    private static final String                              TAG_AMOUNT      = "Amount";
    private static final String                              TAG_ADD_POINT   = "AddPoint";
    private static final String                              TAG_SLIP_NO     = "SlipNo";
    private static final String                              TAG_RSV_NO      = "RsvNo";
    private static final String                              TAG_USER_TYPE   = "UserType";
    private static final String                              TAG_ACCESSKEY1  = "AccessKey1";
    private static final String                              TAG_ACCESSKEY2  = "AccessKey2";

    private String                                           tagName;
    private XmlTag                                           result;                                                                  // 結果用タグ
    private XmlTag                                           ciResult;                                                                // 来店ハピー結果用タグ（ハピー付与の場合のみ）
    private XmlTag                                           point;                                                                   // ポイント用タグ
    private XmlTag                                           messsage;                                                                // 結果の応答メッセージ用タグ
    private XmlTag                                           registUrl;
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
    private XmlTag                                           userType;                                                                // 0:有料会員、1:無料会員
    private XmlTag                                           accessKey1;
    private XmlTag                                           accessKey2;

    private ArrayList<GenerateXmlHapiTouchHotelInfoEmployee> employeeList    = new ArrayList<GenerateXmlHapiTouchHotelInfoEmployee>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_VISIT );
        XmlTag.setParent( root, result );
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
        XmlTag.setParent( root, usePoint );
        XmlTag.setParent( root, amount );
        XmlTag.setParent( root, slipNo );
        XmlTag.setParent( root, rsvNo );
        XmlTag.setParent( root, userType );
        XmlTag.setParent( root, accessKey1 );
        XmlTag.setParent( root, accessKey2 );

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

    public void setCiResult(String ciResult)
    {
        this.ciResult = XmlTag.createXmlTag( TAG_CIRESULT, ciResult );
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
        if ( rate < 0 )
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
        this.userType = XmlTag.createXmlTag( TAG_USER_TYPE, userType );
    }

    public void setAccessKey1(String accesskey1)
    {
        this.accessKey1 = XmlTag.createXmlTag( TAG_ACCESSKEY1, accesskey1 );
    }

    public void setAccessKey2(String accesskey2)
    {
        this.accessKey2 = XmlTag.createXmlTag( TAG_ACCESSKEY2, accesskey2 );
    }

    public void setEmployee(GenerateXmlHapiTouchHotelInfoEmployee employee)
    {
        this.employeeList.add( employee );
        return;
    }

}
