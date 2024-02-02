package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * 高速道路詳細マスタ情報XML作成クラス
 */
public class GenerateXmlMasterRouteIcDetail extends WebApiResultBase
{
    // タグ名
    private static final String TAG_ROUTE_DETAIL = "detail";
    private static final String TAG_ROUTE_ID     = "id";
    private static final String TAG_ROUTE_NAME   = "name";

    private XmlTag              detail;                     // 詳細タグ
    private XmlTag              detailId;                   // ICルートID
    private XmlTag              detailName;                 // ICルート名

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
