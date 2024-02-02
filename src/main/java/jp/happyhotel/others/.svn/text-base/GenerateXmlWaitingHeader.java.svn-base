package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// 検索結果ヘッダ
public class GenerateXmlWaitingHeader extends WebApiResultBase
{
    // タグ名
    private static final String TAG_HEADER        = "header";
    private static final String TAG_HEADER_CMD    = "cmd";
    private static final String TAG_HEADER_RESULT = "result";

    private XmlTag              header;
    private XmlTag              headerCmd;
    private XmlTag              headerResult;

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, headerCmd );
        XmlTag.setParent( root, headerResult );

        return;
    }

    public void setCmd(String cmd)
    {
        headerCmd = XmlTag.createXmlTag( TAG_HEADER_CMD, cmd );
        return;
    }

    public void setResult(int result)
    {
        headerResult = XmlTag.createXmlTag( TAG_HEADER_RESULT, result );
        return;
    }

}
