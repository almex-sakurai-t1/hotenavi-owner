package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML PMS予約連携 予約票取得要求
 */
public class GenerateXmlRsvList extends WebApiResultBase
{
    // タグ名
    private static final String TAG_RSV_LIST    = "RsvList";
    private static final String TAG_RESULT      = "Result";
    private static final String TAG_RSV_COUNT   = "RsvCount";
    private static final String TAG_ERROR_CODE  = "ErrorCode";
    private static final String TAG_ERROR_COUNT = "ErrorCount";
    private static final String TAG_IDENTIFY_NO = "IdentifyNo";

    private XmlTag              result;                        // 結果用タグ
    private XmlTag              rsvCount;                      // 総送信数
    private XmlTag              errorCode;                     // エラーコード用タグ
    private XmlTag              errorCount;                    // 送信失敗件数
    private XmlTag              identifyNo;                    // 識別コード

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_LIST );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, rsvCount );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorCount );
        XmlTag.setParent( root, identifyNo );

        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setRsvCount(int rsvCount)
    {
        this.rsvCount = XmlTag.createXmlTag( TAG_RSV_COUNT, rsvCount );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }

    public void setErrorCount(int errCount)
    {
        this.errorCount = XmlTag.createXmlTag( TAG_ERROR_COUNT, errCount );
    }

    public void setIdentifyNo(int identifyNo)
    {
        this.identifyNo = XmlTag.createXmlTag( TAG_IDENTIFY_NO, identifyNo );
    }

}
