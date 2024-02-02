package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * IC���[�g�ڍ׃}�X�^��XML�쐬�N���X
 */
public class GenerateXmlMasterIcRouteDetail extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_IC_DETAIL             = "detail";
    private static final String TAG_IC_DETAIL_ID          = "id";
    private static final String TAG_IC_DETAIL_NAME        = "name";
    private static final String TAG_IC_DETAIL_HOTEL_COUNT = "count";

    private XmlTag              detail;                              // IC�ڍ׃^�O
    private XmlTag              detailId;                            // ICID
    private XmlTag              detailName;                          // IC��
    private XmlTag              detailHotelCount;                    // IC�t�߂̃z�e������

    @Override
    protected void initXmlNodeInfo()
    {
        detail = createRootChild( TAG_IC_DETAIL );
        XmlTag.setParent( detail, detailId );
        XmlTag.setParent( detail, detailName );
        XmlTag.setParent( detail, detailHotelCount );
    }

    public void setId(String id)
    {
        detailId = XmlTag.createXmlTag( TAG_IC_DETAIL_ID, id );
        return;
    }

    public void setName(String name)
    {
        detailName = XmlTag.createXmlTag( TAG_IC_DETAIL_NAME, name );
        return;
    }

    public void setPrefId(int id)
    {
        detail = XmlTag.createXmlTag( TAG_IC_DETAIL_ID, id );
        return;
    }

}
