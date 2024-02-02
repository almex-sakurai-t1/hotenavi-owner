package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * �H���ڍ׃}�X�^���XML�쐬�N���X
 */
public class GenerateXmlMasterRouteStationDetail extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_ROUTEST_DETAIL      = "detail";
    private static final String TAG_ROUTEST_DETAIL_ID   = "id";
    private static final String TAG_ROUTEST_DETAIL_NAME = "name";

    private XmlTag              detail;                            // �w�H���^�O
    private XmlTag              routeId;                           // �H��ID
    private XmlTag              routeName;                         // �H����

    @Override
    protected void initXmlNodeInfo()
    {
        detail = createRootChild( TAG_ROUTEST_DETAIL );
        XmlTag.setParent( detail, routeId );
        XmlTag.setParent( detail, routeName );
        return;
    }

    public void setId(String id)
    {
        routeId = XmlTag.createXmlTag( TAG_ROUTEST_DETAIL_ID, id );
        return;
    }

    public void setName(String message)
    {
        routeName = XmlTag.createXmlTag( TAG_ROUTEST_DETAIL_NAME, message );
        return;
    }

}
