package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML�`�F�b�N�C���G���[�f�[�^���z���f
 */
public class GenerateXmlReflectCi extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_REFLECT_CI = "ReflectCi";
    private static final String TAG_RESULT     = "Result";

    private XmlTag              result;                      // ���ʗp�^�O

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_REFLECT_CI );
        XmlTag.setParent( root, result );
        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

}
