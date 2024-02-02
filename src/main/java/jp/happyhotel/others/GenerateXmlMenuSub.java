package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * topメニューXML作成クラス
 */
public class GenerateXmlMenuSub extends WebApiResultBase
{
    // タグ名
    private static final String TAG_MENU  = "menu";
    private static final String TAG_KIND  = "kind";
    private static final String TAG_TITLE = "title";
    private static final String TAG_URL   = "url";
    private static final String TAG_IMG   = "img";

    private XmlTag              menu;               // メニュータブ
    private XmlTag              kind;               // 都道府県
    private XmlTag              title;              // タイトル
    private XmlTag              url;                // 遷移先URL
    private XmlTag              img;                // 画像

    @Override
    protected void initXmlNodeInfo()
    {
        menu = createRootChild( TAG_MENU );
        XmlTag.setParent( menu, kind );
        XmlTag.setParent( menu, title );
        XmlTag.setParent( menu, url );
        XmlTag.setParent( menu, img );
        return;
    }

    public void setKind(int mkind)
    {
        kind = XmlTag.createXmlTag( TAG_KIND, mkind );
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

    public void setImg(String mImg)
    {
        img = XmlTag.createXmlTag( TAG_IMG, mImg );
        return;
    }
}
