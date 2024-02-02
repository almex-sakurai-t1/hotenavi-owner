package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// �������ʃw�b�_
public class GenerateXmlChain extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_CHAIN       = "chain";
    private static final String TAG_CHAIN_ID    = "id";
    private static final String TAG_CHAIN_NAME  = "name";
    private static final String TAG_CHAIN_COUNT = "count";

    private XmlTag              chain;                    // �`�F�[�����^�O
    private XmlTag              id;                       // �`�F�[��ID
    private XmlTag              name;                     // ����
    private XmlTag              count;                    // ����

    @Override
    protected void initXmlNodeInfo()
    {
        chain = createRootChild( TAG_CHAIN );

        XmlTag.setParent( chain, id );
        XmlTag.setParent( chain, name );
        XmlTag.setParent( chain, count );
        return;
    }

    public void setId(int groupId)
    {
        this.id = XmlTag.createXmlTag( TAG_CHAIN_ID, groupId );
        return;
    }

    public void setName(String groupName)
    {
        this.name = XmlTag.createXmlTag( TAG_CHAIN_NAME, groupName );
        return;
    }

    public void setCount(int groupCount)
    {
        this.count = XmlTag.createXmlTag( TAG_CHAIN_COUNT, groupCount );
    }

}
