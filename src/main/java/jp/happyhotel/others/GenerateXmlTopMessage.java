package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * マイルXML作成クラス
 */
public class GenerateXmlTopMessage extends WebApiResultBase
{
    // タグ名
    private static final String TAG_TOP_MESSAGE = "topMessage";
    private static final String TAG_COUNT       = "count";
    private static final String TAG_MESSAGE     = "message";

    private XmlTag              topMessage;                    // トップメッセージタブ
    private XmlTag              count;                         // 件数
    private XmlTag              message;                       // メッセージ

    @Override
    protected void initXmlNodeInfo()
    {
        topMessage = createRootChild( TAG_TOP_MESSAGE );
        XmlTag.setParent( topMessage, count );
        XmlTag.setParent( topMessage, message );
        return;
    }

    public void setCount(int cnt)
    {
        count = XmlTag.createXmlTag( TAG_COUNT, cnt );
        return;
    }

    public void setMessage(String msg)
    {
        message = XmlTag.createXmlTag( TAG_MESSAGE, msg );
        return;
    }
}
