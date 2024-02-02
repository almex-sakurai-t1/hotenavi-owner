package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * topメニューXML作成クラス
 */
public class GenerateXmlTopicsSub extends WebApiResultBase
{
    // タグ名
    private static final String TAG_MENU  = "menu";
    private static final String TAG_DATE  = "date";
    private static final String TAG_TITLE = "title";
    private static final String TAG_URL   = "url";

    private XmlTag              menu;               // メニュータブ
    private XmlTag              date;               //
    private XmlTag              title;              // タイトル
    private XmlTag              url;                // 遷移先URL

    @Override
    protected void initXmlNodeInfo()
    {
        menu = createRootChild( TAG_MENU );
        XmlTag.setParent( menu, date );
        XmlTag.setParent( menu, title );
        XmlTag.setParent( menu, url );
        return;
    }

    public void setDate(int mdate)
    {
        date = XmlTag.createXmlTag( TAG_DATE, mdate );
        return;
    }

    public void setTitle(String mtitle)
    {
        title = XmlTag.createXmlTag( TAG_TITLE, mtitle );
        return;
    }

    public void setUrl(String mUrl)
    {
        url = XmlTag.createXmlTag( TAG_URL, mUrl );
        return;
    }

}
