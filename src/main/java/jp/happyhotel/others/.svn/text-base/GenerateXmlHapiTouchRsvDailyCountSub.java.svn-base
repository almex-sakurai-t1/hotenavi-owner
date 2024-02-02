package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約データデータ
 */
public class GenerateXmlHapiTouchRsvDailyCountSub extends WebApiResultBase
{
    // タグ名
    private static final String TAG_DATA           = "Data";
    private static final String TAG_RSV_DATE_VALUE = "RsvDateValue";
    private static final String TAG_RSV_COUNT      = "RsvCount";

    private XmlTag              data;
    private XmlTag              rsvDateValue;
    private XmlTag              rsvCount;

    @Override
    protected void initXmlNodeInfo()
    {
        data = createRootChild( TAG_DATA );
        XmlTag.setParent( data, rsvDateValue );
        XmlTag.setParent( data, rsvCount );

        return;
    }

    public void setRsvDateValue(int rsvDateValue)
    {
        this.rsvDateValue = XmlTag.createXmlTag( TAG_RSV_DATE_VALUE, rsvDateValue );
    }

    public void setRsvCount(int rsvCount)
    {
        this.rsvCount = XmlTag.createXmlTag( TAG_RSV_COUNT, rsvCount );
    }

}
