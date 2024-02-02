package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * ホテルエリア詳細情報XML作成クラス
 */
public class GenerateXmlMasterHotelAreaDetail extends WebApiResultBase
{
    // タグ名
    private static final String TAG_HOTELAREA_DETAIL = "detail";
    private static final String TAG_HOTELAREA_ID     = "id";
    private static final String TAG_HOTELAREA_NAME   = "name";
    private static final String TAG_HOTELAREA_COUNT  = "count";

    private XmlTag              detail;                         // ホテルエリア詳細タグ
    private XmlTag              hotelAreaId;                    // ホテルエリアID
    private XmlTag              name;                           // ホテルエリア名
    private XmlTag              hotelCount;                     // ホテルエリア内のホテル件数

    @Override
    protected void initXmlNodeInfo()
    {
        detail = createRootChild( TAG_HOTELAREA_DETAIL );
        XmlTag.setParent( detail, hotelAreaId );
        XmlTag.setParent( detail, name );
        XmlTag.setParent( detail, hotelCount );
    }

    public void setId(int id)
    {
        hotelAreaId = XmlTag.createXmlTag( TAG_HOTELAREA_ID, id );
        return;
    }

    public void setName(String message)
    {
        name = XmlTag.createXmlTag( TAG_HOTELAREA_NAME, message );
        return;
    }

    public void setCount(int count)
    {
        hotelCount = XmlTag.createXmlTag( TAG_HOTELAREA_ID, count );

        return;
    }

}
