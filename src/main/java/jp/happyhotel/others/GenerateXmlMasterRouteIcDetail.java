package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * �������H�ڍ׃}�X�^���XML�쐬�N���X
 */
public class GenerateXmlMasterRouteIcDetail extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_ROUTE_DETAIL = "detail";
    private static final String TAG_ROUTE_ID     = "id";
    private static final String TAG_ROUTE_NAME   = "name";

    private XmlTag              detail;                     // �ڍ׃^�O
    private XmlTag              detailId;                   // IC���[�gID
    private XmlTag              detailName;                 // IC���[�g��

    @Override
    protected void initXmlNodeInfo()
    {
        detail = createRootChild( TAG_ROUTE_DETAIL );
        XmlTag.setParent( detail, detailId );
        XmlTag.setParent( detail, detailName );
    }

    public void setId(String id)
    {
        detailId = XmlTag.createXmlTag( TAG_ROUTE_ID, id );
        return;
    }

    public void setName(String message)
    {
        detailName = XmlTag.createXmlTag( TAG_ROUTE_NAME, message );
        return;
    }

}
