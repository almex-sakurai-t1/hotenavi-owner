package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * topメニューXML作成クラス
 */
public class GenerateXmlTopics extends WebApiResultBase
{
    // タグ名
    private static final String             TAG_TOPICS = "topics";
    private static final String             TAG_COUNT  = "count";

    private XmlTag                          topics;                                            // トップメニュータブ
    private XmlTag                          count;                                             // 都道府県
    private ArrayList<GenerateXmlTopicsSub> topicsSub  = new ArrayList<GenerateXmlTopicsSub>(); // トピックスサブ

    @Override
    protected void initXmlNodeInfo()
    {
        topics = createRootChild( TAG_TOPICS );
        XmlTag.setParent( topics, count );
        if ( topicsSub != null )
        {
            for( int i = 0 ; i < topicsSub.size() ; i++ )
            {
                topicsSub.get( i ).setRootNode( topics );
                topicsSub.get( i ).initXmlNodeInfo();
            }
        }
        return;
    }

    public void setCount(int cnt)
    {
        count = XmlTag.createXmlTag( TAG_COUNT, cnt );
        return;
    }

    public void setTopicsSub(GenerateXmlTopicsSub sub)
    {
        topicsSub.add( sub );
        return;
    }

}
