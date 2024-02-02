package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML�n�s�^�b�`�����o�[�ύX�ʒm���
 */
public class GenerateXmlHapiTouchHtMemberChange extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_MEMBER_CHANGE = "HtMemberChange";
    private static final String TAG_RESULT        = "Result";
    private static final String TAG_ERROR_CODE    = "ErrorCode";

    private XmlTag              result;                              // ���ʗp�^�O
    private XmlTag              errorCode;                           // �G���[�R�[�h

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_MEMBER_CHANGE );
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