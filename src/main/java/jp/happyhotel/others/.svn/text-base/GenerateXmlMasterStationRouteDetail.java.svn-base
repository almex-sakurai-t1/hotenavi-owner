package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * 駅ルート詳細マスタ情報XML作成クラス
 */
public class GenerateXmlMasterStationRouteDetail extends WebApiResultBase
{
    // タグ名
    private static final String TAG_ST_DETAIL       = "detail";
    private static final String TAG_ST_DETAIL_ID    = "id";
    private static final String TAG_ST_DETAIL_NAME  = "name";
    private static final String TAG_ST_DETAIL_COUNT = "count";

    private XmlTag              detail;                        // 駅詳細タグ
    private XmlTag              detailId;                      // 駅ID
    private XmlTag              detailName;                    // 駅名
    private XmlTag              detailHotelCount;              // 駅付近のホテル件数

    @Override
    protected void initXmlNodeInfo()
    {
        detail = createRootChild( TAG_ST_DETAIL );
        XmlTag.setParent( detail, detailId );
        XmlTag.setParent( detail, detailName );
        XmlTag.setParent( detail, detailHotelCount );
    }

    public void setId(String id)
    {
        detailId = XmlTag.createXmlTag( TAG_ST_DETAIL_ID, id );
        return;
    }

    public void setName(String message)
    {
        detailName = XmlTag.createXmlTag( TAG_ST_DETAIL_NAME, message );
        return;
    }

    public void setHotelCount(int count)
    {
        detailHotelCount = XmlTag.createXmlTag( TAG_ST_DETAIL_COUNT, count );
        return;
    }

}
