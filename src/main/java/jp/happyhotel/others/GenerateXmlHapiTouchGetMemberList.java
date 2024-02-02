package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML �z�X�g�A�g �J�[�h���X�����o�[�擾�v��
 */
public class GenerateXmlHapiTouchGetMemberList extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_GET_MEMBER_LIST = "GetMemberList";
    private static final String TAG_RESULT          = "Result";
    private static final String TAG_MEMBER_COUNT    = "MemberCount";
    private static final String TAG_ERROR_CODE      = "ErrorCode";
    private static final String TAG_ERROR_COUNT     = "ErrorCount";
    private static final String TAG_IDENTIFY_NO     = "IdentifyNo";

    private XmlTag              result;                               // ���ʗp�^�O
    private XmlTag              memberCount;                          // �����M��
    private XmlTag              errorCode;                            // �G���[�R�[�h�p�^�O
    private XmlTag              errorCount;                           // ���M���s����
    private XmlTag              identifyNo;                           // ���ʃR�[�h

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_GET_MEMBER_LIST );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, memberCount );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorCount );
        XmlTag.setParent( root, identifyNo );

        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setMemberCount(int memberCount)
    {
        this.memberCount = XmlTag.createXmlTag( TAG_MEMBER_COUNT, memberCount );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }

    public void setErrorCount(int errCount)
    {
        this.errorCount = XmlTag.createXmlTag( TAG_ERROR_COUNT, errCount );
    }

    public void setIdentifyNo(int identifyNo)
    {
        this.identifyNo = XmlTag.createXmlTag( TAG_IDENTIFY_NO, identifyNo );
    }

}
