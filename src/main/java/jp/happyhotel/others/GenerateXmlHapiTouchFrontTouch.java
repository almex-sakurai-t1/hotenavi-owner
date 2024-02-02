package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * �t�����g�^�b�`XML�쐬�N���X
 */
public class GenerateXmlHapiTouchFrontTouch extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_FRONT_TOUCH = "HtFrontTouch";
    private static final String TAG_RESULT      = "Result";
    private static final String TAG_ERROR_CODE  = "ErrorCode";
    private static final String TAG_LIMIT_DATE  = "LimitDate";
    private static final String TAG_LIMIT_TIME  = "LimitTime";

    private XmlTag              result;                          // ���ʗp�^�O
    private XmlTag              errorCode;                       // �G���[�R�[�h
    private XmlTag              limitDate;                       //
    private XmlTag              limitTime;                       //

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_FRONT_TOUCH );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, limitDate );
        XmlTag.setParent( root, limitTime );
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

    public void setLimitDate(int limitDate)
    {
        this.limitDate = XmlTag.createXmlTag( TAG_LIMIT_DATE, limitDate );
    }

    public void setLimitTime(int limitTime)
    {
        this.limitTime = XmlTag.createXmlTag( TAG_LIMIT_TIME, limitTime );
    }

}
