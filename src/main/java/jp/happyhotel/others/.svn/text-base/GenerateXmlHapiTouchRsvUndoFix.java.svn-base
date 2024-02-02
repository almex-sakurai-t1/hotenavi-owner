package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約来店確定取消情報
 */
public class GenerateXmlHapiTouchRsvUndoFix extends WebApiResultBase
{
    // タグ名
    private static final String TAG_RSV_UNDO_FIX = "RsvUndoFix";
    private static final String TAG_RESULT       = "Result";
    private static final String TAG_STATUS       = "Status";
    private static final String TAG_ERROR_CODE   = "ErrorCode";

    private String              tagName;
    private XmlTag              result;                         // 結果用タグ
    private XmlTag              status;                         // ステータス用タグ
    private XmlTag              errorCode;                      // エラーコード用タグ

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_UNDO_FIX );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, status );
        XmlTag.setParent( root, errorCode );

        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setStatus(int status)
    {
        this.status = XmlTag.createXmlTag( TAG_STATUS, status );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }

}
