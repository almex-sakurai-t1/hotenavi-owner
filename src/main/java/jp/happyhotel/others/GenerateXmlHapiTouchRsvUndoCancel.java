package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約キャンセル取消情報
 */
public class GenerateXmlHapiTouchRsvUndoCancel extends WebApiResultBase
{
    // タグ名
    private static final String TAG_RSV_UNDO_CANCEL = "RsvUndoCancel";
    private static final String TAG_RESULT          = "Result";
    private static final String TAG_STATUS          = "Status";
    private static final String TAG_STATUS_VALUE    = "StatusValue";
    private static final String TAG_RSV_NO          = "RsvNo";
    private static final String TAG_ERROR_CODE      = "ErrorCode";

    private String              tagName;
    private XmlTag              result;                               // 結果用タグ
    private XmlTag              status;                               // ステータス用タグ
    private XmlTag              statusValue;                          // ステータス用タグ
    private XmlTag              rsvNo;                                // 予約番号用タグ
    private XmlTag              errorCode;                            // エラーコード用タグ

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_UNDO_CANCEL );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, status );
        XmlTag.setParent( root, statusValue );
        XmlTag.setParent( root, rsvNo );
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

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = XmlTag.createXmlTag( TAG_RSV_NO, rsvNo );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }

}
