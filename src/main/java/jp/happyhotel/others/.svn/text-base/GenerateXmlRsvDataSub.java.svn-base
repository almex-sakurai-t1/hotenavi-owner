package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * PUSH通知一覧XML作成クラス
 */
public class GenerateXmlRsvDataSub extends WebApiResultBase
{
    // タグ名
    private static final String TAG_RSV_DATA = "RsvData";
    private static final String TAG_DATE     = "date";
    private static final String TAG_NAME     = "name";
    private static final String TAG_RSV_NO   = "rsvNo";

    private XmlTag              rsvData;                 //
    private XmlTag              data;                    //
    private XmlTag              date;                    //
    private XmlTag              name;                    //
    private XmlTag              rsvNo;                   //

    @Override
    protected void initXmlNodeInfo()
    {
        rsvData = createRootChild( TAG_RSV_DATA );
        XmlTag.setParent( rsvData, date );
        XmlTag.setParent( rsvData, name );
        XmlTag.setParent( rsvData, rsvNo );
        return;
    }

    public void setDate(int date1)
    {
        date = XmlTag.createXmlTag( TAG_DATE, date1 );
        return;
    }

    public void setName(String name1)
    {
        name = XmlTag.createXmlTag( TAG_NAME, name1 );
        return;
    }

    public void setRsvNo(String rsvno)
    {
        rsvNo = XmlTag.createXmlTag( TAG_RSV_NO, rsvno );
        return;
    }

}
