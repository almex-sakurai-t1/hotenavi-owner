package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * 路線詳細マスタ情報XML作成クラス
 */
public class GenerateXmlMasterRouteStationDetail extends WebApiResultBase
{
    // タグ名
    private static final String TAG_ROUTEST_DETAIL      = "detail";
    private static final String TAG_ROUTEST_DETAIL_ID   = "id";
    private static final String TAG_ROUTEST_DETAIL_NAME = "name";

    private XmlTag              detail;                            // 駅路線タグ
    private XmlTag              routeId;                           // 路線ID
    private XmlTag              routeName;                         // 路線名

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
