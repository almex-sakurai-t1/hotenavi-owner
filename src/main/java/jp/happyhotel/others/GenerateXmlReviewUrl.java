package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * AppleStore・GooglePlayレビューURLXML作成クラス
 * 
 * @author Koshiba
 */
public class GenerateXmlReviewUrl extends WebApiResultBase
{
    // タグ名
    private static final String APPLESTORE = "appStore";
    private static final String GOOGLEPLAY = "googlePlay";

    /** AppleStoreレビューURL */
    private XmlTag              appleStore;
    /** GooglePlayレビューURL */
    private XmlTag              googlePlay;

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, appleStore );
        XmlTag.setParent( root, googlePlay );
    }

    /** AppleStoreレビューURLをセット */
    public void setAppStore(String url)
    {
        if ( url != null )
        {
            url = ReplaceString.replaceApiSpecial( url ); // URLは"&"を含む可能性があるので置換処理を行う必要あり
        }
        appleStore = XmlTag.createXmlTag( APPLESTORE, url );
        return;
    }

    /** GooglePlayレビューURLをセット */
    public void setGooglePlay(String url)
    {
        if ( url != null )
        {
            url = ReplaceString.replaceApiSpecial( url ); // URLは"&"を含む可能性があるので置換処理を行う必要あり
        }
        googlePlay = XmlTag.createXmlTag( GOOGLEPLAY, url );
        return;
    }
}
