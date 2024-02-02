package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * クチコミ情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// クチコミ情報
public class GenerateXmlKuchikomiKuchikomi extends WebApiResultBase
{
    // タグ名
    private static final String                            TAG_KUCHIKOMI         = "kuchikomi";
    private static final String                            TAG_KUCHIKOMI_COUNT   = "count";
    private static final String                            TAG_KUCHIKOMI_AVERAGE = "average";

    private XmlTag                                         kuchikomi;                                                                   // クチコミ情報格納タグ
    private XmlTag                                         kuchikomiCount;                                                              // クチコミ件数
    private XmlTag                                         kuchikomiAverage;                                                            // クチコミ平均点

    private ArrayList<GenerateXmlKuchikomiKuchikomiDetail> kuchikomiDetail       = new ArrayList<GenerateXmlKuchikomiKuchikomiDetail>(); // クチコミ詳細情報

    @Override
    protected void initXmlNodeInfo()
    {
        kuchikomi = createRootChild( TAG_KUCHIKOMI );

        XmlTag.setParent( kuchikomi, kuchikomiCount );
        XmlTag.setParent( kuchikomi, kuchikomiAverage );
        if ( kuchikomiDetail != null )
        {
            for( int i = 0 ; i < kuchikomiDetail.size() ; i++ )
            {
                kuchikomiDetail.get( i ).setRootNode( kuchikomi );
                kuchikomiDetail.get( i ).initXmlNodeInfo();
            }
        }

        return;
    }

    public void setCount(int count)
    {
        kuchikomiCount = XmlTag.createXmlTag( TAG_KUCHIKOMI_COUNT, count );
        return;
    }

    public void setAverage(String average)
    {
        kuchikomiAverage = XmlTag.createXmlTag( TAG_KUCHIKOMI_AVERAGE, average );
        return;
    }

    public void addKuchikomiDetail(GenerateXmlKuchikomiKuchikomiDetail addKuchikomiDetail)
    {
        kuchikomiDetail.add( addKuchikomiDetail );
        return;
    }
}
