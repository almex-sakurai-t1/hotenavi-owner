package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// �z�e���ڍו����ڍ׏��
public class GenerateXmlDetailHotelGalleryCategoryDetail extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_DETAIL          = "detail";
    private static final String TAG_DETAIL_NAME     = "name";
    private static final String TAG_DETAIL_IMAGE    = "image";
    private static final String TAG_DETAIL_TEXT     = "text";
    private static final String TAG_DETAIL_TEXT_URL = "textUrl";

    private XmlTag              detail;                         // �����ڍ׏��i�[�^�O
    private XmlTag              detailName;                     // ������
    private XmlTag              detailImage;                    // �����摜URL
    private XmlTag              detailText;                     // ����������
    private XmlTag              detailTextUrl;                  // ����������URL

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

    public void setTextUrl(String textURL)
    {
        if ( textURL != null )
        {
            textURL = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( textURL ) );
        }
        detailTextUrl = XmlTag.createXmlTag( TAG_DETAIL_TEXT_URL, textURL );
        return;
    }
}
