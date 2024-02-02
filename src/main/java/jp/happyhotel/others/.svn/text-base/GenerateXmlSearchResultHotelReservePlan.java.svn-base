package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// ホテル予約情報
public class GenerateXmlSearchResultHotelReservePlan extends WebApiResultBase
{
    // タグ名
    private static final String TAG_PLAN      = "plan";
    private static final String TAG_PLAN_NO   = "no";
    private static final String TAG_PLAN_NAME = "name";

    private XmlTag              plan;                  // 予約プラン情報格納タグ
    private XmlTag              planNo;                // 予約プラン番号
    private XmlTag              planName;              // 予約プラン名

    @Override
    protected void initXmlNodeInfo()
    {
        plan = createRootChild( TAG_PLAN );
        XmlTag.setParent( plan, planNo );
        XmlTag.setParent( plan, planName );
        return;
    }

    public void setNo(int no)
    {
        planNo = XmlTag.createXmlTag( TAG_PLAN_NO, no );
        return;
    }

    public void setName(String name)
    {
        planName = XmlTag.createXmlTag( TAG_PLAN_NAME, name );
        return;
    }
}
