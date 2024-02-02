package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチメンバー再利用通知情報
 */
public class GenerateXmlHapiTouchHtMemberReuse extends WebApiResultBase
{
    // タグ名
    private static final String TAG_MEMBER_REUSE = "HtMemberReuse";
    private static final String TAG_RESULT       = "Result";
    private static final String TAG_ERROR_CODE   = "ErrorCode";

    private XmlTag              result;                            // 結果用タグ
    private XmlTag              errorCode;                         // エラーコード

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_MEMBER_REUSE );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, errorCode );
        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setErrorCode(int error)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, error );
    }

}
