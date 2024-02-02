package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * ホテル詳細部屋詳細情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// ホテル詳細部屋詳細情報
public class GenerateXmlDetailHotelRoomDetail extends WebApiResultBase
{
    // タグ名
    private static final String TAG_DETAIL          = "detail";
    private static final String TAG_DETAIL_NAME     = "name";
    private static final String TAG_DETAIL_IMAGE    = "image";
    private static final String TAG_DETAIL_TEXT     = "text";
    private static final String TAG_DETAIL_TEXT_URL = "textUrl";

    private XmlTag              detail;                         // 部屋詳細情報格納タグ
    private XmlTag              detailName;                     // 部屋名
    private XmlTag              detailImage;                    // 部屋画像URL
    private XmlTag              detailText;                     // 説明文
    private XmlTag              detailTextUrl;                  // 説明文URL

    @Override
    protected void initXmlNodeInfo()
    {
        detail = createRootChild( TAG_DETAIL );
        XmlTag.setParent( detail, detailName );
        XmlTag.setParent( detail, detailImage );
        XmlTag.setParent( detail, detailText );
        XmlTag.setParent( detail, detailTextUrl );

        return;
    }

    public void setName(String name)
    {
        if ( name != null )
        {
            name = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( name ) );
        }
        detailName = XmlTag.createXmlTag( TAG_DETAIL_NAME, name );
        return;
    }

    public void setImage(String image)
    {
        if ( image != null && image.equals( "" ) == false && image.indexOf( "http://" ) == -1 && image.indexOf( "https://" ) == -1 )
        {
            image = Url.getUrl() + image;
        }
        detailImage = XmlTag.createXmlTag( TAG_DETAIL_IMAGE, image );
        return;
    }

    public void setText(String text)
    {
        if ( text != null )
        {
            text = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( text ) );
        }
        detailText = XmlTag.createXmlTag( TAG_DETAIL_TEXT, text );
        return;
    }

    public void setTextUrl(String textUrl)
    {
        if ( textUrl != null )
        {
            textUrl = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( textUrl ) );
        }
        detailTextUrl = XmlTag.createXmlTag( TAG_DETAIL_TEXT_URL, textUrl );
        return;
    }
}
