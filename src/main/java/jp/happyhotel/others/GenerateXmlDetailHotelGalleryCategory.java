package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * ホテル詳細ギャラリー情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// ホテル詳細ギャラリー情報
public class GenerateXmlDetailHotelGalleryCategory extends WebApiResultBase
{
    // タグ名
    private static final String                                    TAG_CATEGORY       = "category";
    private static final String                                    TAG_CATEGORY_NAME  = "name";
    private static final String                                    TAG_CATEGORY_COUNT = "count";

    private XmlTag                                                 categoy;                                                                          // カテゴリー情報格納タグ
    private XmlTag                                                 categoyName;                                                                      // カテゴリー名称
    private XmlTag                                                 categoyCount;                                                                     // 画像数
    private ArrayList<GenerateXmlDetailHotelGalleryCategoryDetail> galleryCategory    = new ArrayList<GenerateXmlDetailHotelGalleryCategoryDetail>(); // 画像ギャラリー詳細情報

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
