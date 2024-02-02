package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * ホテル詳細外部サイト情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// ホテル詳細外部サイト情報
public class GenerateXmlDetailHotelSite extends WebApiResultBase
{
    // タグ名
    private static final String TAG_SITE      = "site";
    private static final String TAG_SITE_URL  = "url";
    private static final String TAG_SITE_TEXT = "text";

    private XmlTag              site;                  // 外部サイト情報格納タグ
    private XmlTag              siteUrl;               // ギャラリー画像名
    private XmlTag              siteText;              // ギャラリー画像URL

    @Override
    protected void initXmlNodeInfo()
    {
        site = createRootChild( TAG_SITE );
        XmlTag.setParent( site, siteUrl );
        XmlTag.setParent( site, siteText );

        return;
    }

    public void setUrl(String url)
    {
        if ( url != null )
        {
            url = ReplaceString.replaceApiSpecial( url );
        }
        siteUrl = XmlTag.createXmlTag( TAG_SITE_URL, url );
        return;
    }

    public void setImage(String text)
    {
        if ( text != null )
        {
            text = ReplaceString.replaceApiSpecial( text );
        }
        siteText = XmlTag.createXmlTag( TAG_SITE_TEXT, text );
        return;
    }

}
