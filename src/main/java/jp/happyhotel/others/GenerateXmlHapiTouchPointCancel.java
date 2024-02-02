package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML�n�s�^�b�`�\��L�����Z�����
 */
public class GenerateXmlHapiTouchPointCancel extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_POINT_CANCEL = "PointCancel";
    private static final String TAG_RESULT       = "Result";
    private static final String TAG_ERROR_CODE   = "ErrorCode";

    private String              tagName;
    private XmlTag              result;                          // ���ʗp�^�O
    private XmlTag              status;                          // �X�e�[�^�X�p�^�O
    private XmlTag              statusValue;                     // �X�e�[�^�X�p�^�O
    private XmlTag              rsvNo;                           // �\��ԍ��p�^�O
    private XmlTag              errorCode;                       // �G���[�R�[�h�p�^�O

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
