package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約キャンセル情報
 */
public class GenerateXmlHapiTouchRsvModifyArrivalTime extends WebApiResultBase
{
    // タグ名
    private static final String TAG_RSV_MODIFY_ARRIVAL_TIME = "RsvModifyArrivalTime";
    private static final String TAG_RESULT                  = "Result";
    private static final String TAG_RSV_DATE_VALUE          = "RsvDateValue";
    private static final String TAG_ARRIVAL_DATE_VALUE      = "ArrivalDateValue";
    private static final String TAG_ARRIVAL_TIME            = "ArrivalTime";
    private static final String TAG_ARRIVAL_TIME_VALUE      = "ArrivalTimeValue";
    private static final String TAG_ERROR_CODE              = "ErrorCode";

    private String              tagName;
    private XmlTag              result;                                              // 結果用タグ
    private XmlTag              rsvDateValue;                                        // 予約日用タグ

    private XmlTag              arrivalDateValue;                                    // 到着予定日用タグ
    private XmlTag              arrivalTime;                                         // 到着予定時刻用タグ
    private XmlTag              arrivalTimeValue;                                    // 到着予定時刻用タグ
    private XmlTag              errorCode;                                           // エラーコード用タグ

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_MODIFY_ARRIVAL_TIME );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, rsvDateValue );
        XmlTag.setParent( root, arrivalDateValue );
        XmlTag.setParent( root, arrivalTime );
        XmlTag.setParent( root, arrivalTimeValue );
        XmlTag.setParent( root, errorCode );

        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setRsvDateValue(int rsvdateValue)
    {
        this.rsvDateValue = XmlTag.createXmlTag( TAG_RSV_DATE_VALUE, rsvdateValue );
    }

    public void setArraivalDateValue(int arrivaldateValue)
    {
        this.arrivalDateValue = XmlTag.createXmlTag( TAG_ARRIVAL_DATE_VALUE, arrivaldateValue );
    }

    public void setArraivalTime(String arrivaltime)
    {
        this.arrivalTime = XmlTag.createXmlTag( TAG_ARRIVAL_TIME, arrivaltime );
    }

    public void setArraivalTimeValue(int arrivaltimeValue)
    {
        this.arrivalTimeValue = XmlTag.createXmlTag( TAG_ARRIVAL_TIME_VALUE, arrivaltimeValue );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }

}
