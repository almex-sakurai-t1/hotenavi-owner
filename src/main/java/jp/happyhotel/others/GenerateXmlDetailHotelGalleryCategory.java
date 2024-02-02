package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * �z�e���ڍ׃M�������[���xml�����N���X
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// �z�e���ڍ׃M�������[���
public class GenerateXmlDetailHotelGalleryCategory extends WebApiResultBase
{
    // �^�O��
    private static final String                                    TAG_CATEGORY       = "category";
    private static final String                                    TAG_CATEGORY_NAME  = "name";
    private static final String                                    TAG_CATEGORY_COUNT = "count";

    private XmlTag                                                 categoy;                                                                          // �J�e�S���[���i�[�^�O
    private XmlTag                                                 categoyName;                                                                      // �J�e�S���[����
    private XmlTag                                                 categoyCount;                                                                     // �摜��
    private ArrayList<GenerateXmlDetailHotelGalleryCategoryDetail> galleryCategory    = new ArrayList<GenerateXmlDetailHotelGalleryCategoryDetail>(); // �摜�M�������[�ڍ׏��

    @Override
    protected void initXmlNodeInfo()
    {
        categoy = createRootChild( TAG_CATEGORY );

        XmlTag.setParent( categoy, categoyName );
        XmlTag.setParent( categoy, categoyCount );

        if ( galleryCategory != null )
        {
            for( int i = 0 ; i < galleryCategory.size() ; i++ )
            {
                galleryCategory.get( i ).setRootNode( categoy );
                galleryCategory.get( i ).initXmlNodeInfo();
            }
        }

        return;
    }

    public void setName(String name)
    {
        if ( name != null )
        {
            name = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( name ) );
        }
        categoyName = XmlTag.createXmlTag( TAG_CATEGORY_NAME, name );
        return;
    }

    public void setCount(int count)
    {
        categoyCount = XmlTag.createXmlTag( TAG_CATEGORY_COUNT, count );
        return;
    }

    public void addDetail(GenerateXmlDetailHotelGalleryCategoryDetail detail)
    {
        galleryCategory.add( detail );
        return;
    }
}
