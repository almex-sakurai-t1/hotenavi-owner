package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchHotelInfo extends WebApiResultBase
{
    // タグ名
    private static final String                              TAG_HOTEL_INFO             = "HotelInfo";
    private static final String                              TAG_RESULT                 = "Result";
    private static final String                              TAG_MESSAGE                = "Message";
    private static final String                              TAG_FILE32                 = "File32";
    private static final String                              TAG_FILE64                 = "File64";
    private static final String                              TAG_DATE                   = "Date";
    private static final String                              TAG_TIME                   = "Time";
    private static final String                              TAG_EMPLOYEE               = "Employee";
    private static final String                              TAG_SYNC_CI_UPDATE         = "SyncCiUpdate";
    private static final String                              TAG_SYNC_CI_UPTIME         = "SyncCiUptime";
    private static final String                              TAG_HOTEL_INFO_INTERVAL    = "HotelInfoInterval";
    private static final String                              TAG_NO_SHOW_CREDIT         = "NoShowCredit";
    private static final String                              TAG_CHARGE_START_TIME      = "ChargeStartTime";
    private static final String                              TAG_RSV_COUNT              = "RsvCount";
    private static final String                              TAG_NEW_TOUCH              = "NewTouch";
    private static final String                              TAG_FRONT_TOUCH_STATE      = "FrontTouchState";
    private static final String                              TAG_FRONT_TOUCH_LIMIT_DATE = "FrontTouchLimitDate";
    private static final String                              TAG_FRONT_TOUCH_LIMIT_TIME = "FrontTouchLimitTime";
    private static final String                              TAG_ERROR_CODE             = "ErrorCode";

    private String                                           tagName;
    private XmlTag                                           result;                                                                             // 結果用タグ
    private XmlTag                                           visitResult;                                                                        // 来店ハピー結果用タグ（ハピー付与の場合のみ）
    private XmlTag                                           messsage;                                                                           // 結果の応答メッセージ用タグ
    private XmlTag                                           registUrl;
    private XmlTag                                           file32;                                                                             // 32ビット用タグ
    private XmlTag                                           file64;                                                                             // 64ビット用タグ
    private XmlTag                                           date;                                                                               // サーバー日付用タグ
    private XmlTag                                           time;                                                                               // サーバー時刻用タグ
    private XmlTag                                           syncCiUpdate;                                                                       // チェックインデータ更新日付
    private XmlTag                                           syncCiUptime;                                                                       // チェックインデータ更新時刻
    private XmlTag                                           hotelInfoInterval;                                                                  // ホテル情報取得間隔
    private XmlTag                                           noShowCredit;                                                                       // クレジット請求フラグ
    private XmlTag                                           chargeStartTime;                                                                    // クレジット請求開始時刻
    private XmlTag                                           rsvCount;
    private XmlTag                                           newTouch;
    private XmlTag                                           frontTouchState;
    private XmlTag                                           frontTouchLimitDate;
    private XmlTag                                           frontTouchLimitTime;
    private XmlTag                                           errorCode;
    private ArrayList<GenerateXmlHapiTouchHotelInfoEmployee> employeeList               = new ArrayList<GenerateXmlHapiTouchHotelInfoEmployee>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_HOTEL_INFO );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, messsage );
        XmlTag.setParent( root, date );
        XmlTag.setParent( root, time );
        XmlTag.setParent( root, file32 );
        XmlTag.setParent( root, file64 );
        XmlTag.setParent( root, syncCiUpdate );
        XmlTag.setParent( root, syncCiUptime );
        XmlTag.setParent( root, hotelInfoInterval );
        XmlTag.setParent( root, noShowCredit );
        XmlTag.setParent( root, chargeStartTime );
        XmlTag.setParent( root, rsvCount );
        XmlTag.setParent( root, newTouch );
        XmlTag.setParent( root, frontTouchState );
        XmlTag.setParent( root, frontTouchLimitDate );
        XmlTag.setParent( root, frontTouchLimitTime );
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

    public void setMessage(String messsage)
    {
        this.messsage = XmlTag.createXmlTag( TAG_MESSAGE, messsage );
    }

    public void setDate(String date)
    {
        this.date = XmlTag.createXmlTag( TAG_DATE, date );
    }

    public void setTime(String time)
    {
        this.time = XmlTag.createXmlTag( TAG_TIME, time );
    }

    public void setFile32(String file32)
    {
        this.file32 = XmlTag.createXmlTag( TAG_FILE32, file32 );
    }

    public void setFile64(String file64)
    {
        this.file64 = XmlTag.createXmlTag( TAG_FILE64, file64 );
    }

    public void setSyncCiUpdate(int ciUpdate)
    {
        this.syncCiUpdate = XmlTag.createXmlTag( TAG_SYNC_CI_UPDATE, ciUpdate );
    }

    public void setSyncCiUptime(int ciUptime)
    {
        this.syncCiUptime = XmlTag.createXmlTag( TAG_SYNC_CI_UPTIME, ciUptime );
    }

    public void setHotelInfoInterval(int hotelInfoInterval)
    {
        this.hotelInfoInterval = XmlTag.createXmlTag( TAG_HOTEL_INFO_INTERVAL, hotelInfoInterval );
    }

    public void setNoShowCredit(int noShowCredit)
    {
        this.noShowCredit = XmlTag.createXmlTag( TAG_NO_SHOW_CREDIT, noShowCredit );
    }

    public void setChargeStartTime(int chargeStartTime)
    {
        this.chargeStartTime = XmlTag.createXmlTag( TAG_CHARGE_START_TIME, chargeStartTime );
    }

    public void setRsvCount(int rsvcount)
    {
        this.rsvCount = XmlTag.createXmlTag( TAG_RSV_COUNT, rsvcount );
    }

    public void setNewTouch(int newTouch)
    {
        this.newTouch = XmlTag.createXmlTag( TAG_NEW_TOUCH, newTouch );
    }

    public void setFrontTouchState(int frontTouchState)
    {
        this.frontTouchState = XmlTag.createXmlTag( TAG_FRONT_TOUCH_STATE, frontTouchState );
    }

    public void setFrontTouchLimitDate(int frontTouchLimitDate)
    {
        this.frontTouchLimitDate = XmlTag.createXmlTag( TAG_FRONT_TOUCH_LIMIT_DATE, frontTouchLimitDate );
    }

    public void setFrontTouchLimitTime(int frontTouchLimitTime)
    {
        this.frontTouchLimitTime = XmlTag.createXmlTag( TAG_FRONT_TOUCH_LIMIT_TIME, frontTouchLimitTime );
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errorCode );
    }

    public void setEmployee(GenerateXmlHapiTouchHotelInfoEmployee employee)
    {
        this.employeeList.add( employee );
        return;
    }
}
