package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約キャンセル情報
 */
public class GenerateXmlHapiTouchPointCancel extends WebApiResultBase
{
    // タグ名
    private static final String TAG_POINT_CANCEL = "PointCancel";
    private static final String TAG_RESULT       = "Result";
    private static final String TAG_ERROR_CODE   = "ErrorCode";

    private String              tagName;
    private XmlTag              result;                          // 結果用タグ
    private XmlTag              status;                          // ステータス用タグ
    private XmlTag              statusValue;                     // ステータス用タグ
    private XmlTag              rsvNo;                           // 予約番号用タグ
    private XmlTag              errorCode;                       // エラーコード用タグ

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_POINT_CANCEL );
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
