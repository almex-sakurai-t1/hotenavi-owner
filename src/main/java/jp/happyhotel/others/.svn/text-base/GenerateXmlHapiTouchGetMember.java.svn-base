package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLカードレスメンバー情報
 */
public class GenerateXmlHapiTouchGetMember extends WebApiResultBase
{
    // タグ名
    private static final String TAG_GET_MEMBER  = "GetMember";
    private static final String TAG_RESULT      = "Result";
    private static final String TAG_ERROR_CODE  = "ErrorCode";
    private static final String TAG_KIND        = "Kind";
    private static final String TAG_REGIST_DATE = "RegistDate";
    private static final String TAG_USER_ID     = "UserId";

    private XmlTag              result;                        // 結果用タグ
    private XmlTag              errorCode;                     // エラーコード
    private XmlTag              kind;                          // 0:カードのみ(磁気カードのみ)、1:カードレス移行済(磁気カード+カードレス)、2:カードレス新規(カードレスのみ)
    private XmlTag              registDate;                    // カードレスメンバーの場合にセット
    private XmlTag              userId;                        // 登録されている場合にユーザIDをセット（ユーザ管理番号）

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_GET_MEMBER );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, kind );
        XmlTag.setParent( root, registDate );
        XmlTag.setParent( root, userId );
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

    public void setKind(int kind)
    {
        this.kind = XmlTag.createXmlTag( TAG_KIND, kind );
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = XmlTag.createXmlTag( TAG_REGIST_DATE, registDate );
    }

    public void setUserId(int userId)
    {
        this.userId = XmlTag.createXmlTag( TAG_USER_ID, userId );
    }
}
