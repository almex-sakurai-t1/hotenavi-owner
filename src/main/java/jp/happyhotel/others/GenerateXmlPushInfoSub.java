package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * PUSH通知一覧XML作成クラス
 */
public class GenerateXmlPushInfoSub extends WebApiResultBase
{
    // タグ名
    private static final String TAG_PUSH_INFO = "pushInfo";
    private static final String TAG_DATA      = "data";
    private static final String TAG_SEQ       = "seq";
    private static final String TAG_DATE      = "date";
    private static final String TAG_TIME      = "time";
    private static final String TAG_TITLE     = "title";
    private static final String TAG_DETAIL    = "detail";

    private XmlTag              pushInfo;                  //
    private XmlTag              data;                      //
    private XmlTag              seq;                       //
    private XmlTag              date;                      //
    private XmlTag              time;                      //
    private XmlTag              title;                     //
    private XmlTag              detail;                    //

    @Override
    protected void initXmlNodeInfo()
    {
        pushInfo = createRootChild( TAG_PUSH_INFO );
        data = createRootChild( TAG_DATA );
        XmlTag.setParent( data, seq );
        XmlTag.setParent( data, date );
        XmlTag.setParent( data, time );
        XmlTag.setParent( data, title );
        XmlTag.setParent( data, detail );
        return;
    }

    public void setSeq(int seq1)
    {
        seq = XmlTag.createXmlTag( TAG_SEQ, seq1 );
        return;
    }

    public void setDate(String date1)
    {
        date = XmlTag.createXmlTag( TAG_DATE, date1 );
        return;
    }

    public void setTime(String time1)
    {
        time = XmlTag.createXmlTag( TAG_TIME, time1 );
        return;
    }

    public void setTitle(String title1)
    {
        title = XmlTag.createXmlTag( TAG_TITLE, title1 );
        return;
    }

    public void setDetail(String detail1)
    {
        detail = XmlTag.createXmlTag( TAG_DETAIL, detail1 );
        return;
    }

}
