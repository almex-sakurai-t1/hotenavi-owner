package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * ICルート詳細マスタ情XML作成クラス
 */
public class GenerateXmlMasterIc2RouteDetail extends WebApiResultBase
{
    // タグ名
    private static final String TAG_IC_DETAIL             = "detail";
    private static final String TAG_IC_DETAIL_ICID        = "icId";
    private static final String TAG_IC_DETAIL_ICNAME      = "icName";
    private static final String TAG_IC_DETAIL_HOTEL_COUNT = "count";

    private XmlTag              detail;                              // IC詳細タグ
    private XmlTag              detailIcId;                          // ICID
    private XmlTag              detailIcName;                        // IC名
    private XmlTag              detailHotelCount;                    // IC付近のホテル件数

    @Override
    protected void initXmlNodeInfo()
    {
        detail = createRootChild( TAG_IC_DETAIL );
        XmlTag.setParent( detail, detailIcId );
        XmlTag.setParent( detail, detailIcName );
        XmlTag.setParent( detail, detailHotelCount );
    }

    public void setId(String id)
    {
        detailIcId = XmlTag.createXmlTag( TAG_IC_DETAIL_ICID, id );
        return;
    }

    public void setName(String name)
    {
        detailIcName = XmlTag.createXmlTag( TAG_IC_DETAIL_ICNAME, name );
        return;
    }

    public void setCount(int count)
    {
        detail = XmlTag.createXmlTag( TAG_IC_DETAIL_HOTEL_COUNT, count );
        return;
    }

}
