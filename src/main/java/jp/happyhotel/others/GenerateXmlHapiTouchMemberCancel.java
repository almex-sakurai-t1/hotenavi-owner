package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLカードレスメンバー情報
 */
public class GenerateXmlHapiTouchMemberCancel extends WebApiResultBase
{
    // タグ名
    private static final String TAG_MEMBER_CANCEL = "MemberCancel";
    private static final String TAG_RESULT        = "Result";
    private static final String TAG_ERROR_CODE    = "ErrorCode";

    private XmlTag              result;                            // 結果用タグ
    private XmlTag              errorCode;                         // エラーコード

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_MEMBER_CANCEL );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, errorCode );
        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errorCode );
    }

}
