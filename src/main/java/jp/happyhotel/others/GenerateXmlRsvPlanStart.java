package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML PMS予約連携 予約プラン開始
 */
public class GenerateXmlRsvPlanStart extends WebApiResultBase
{
    // タグ名
    private static final String TAG_RSV_PLAN_START = "RsvPlanStart";
    private static final String TAG_RESULT         = "Result";
    private static final String TAG_ERROR_CODE     = "ErrorCode";

    private XmlTag              result;                             // 結果用タグ
    private XmlTag              errorCode;                          // エラーコード用タグ

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_PLAN_START );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, errorCode );

        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }

}
