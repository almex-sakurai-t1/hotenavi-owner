package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * クチコミ情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/27
 */

// コンテンツ共通
public class GenerateXmlKuchikomi extends WebApiResultBase
{
    // タグ名
    private static final String           TAG_ERROR_CODE    = "errorCode";
    private static final String           TAG_ERROR_MESSAGE = "errorMessage";
    private static final String           TAG_RESULT_COUNT  = "ResultCount";

    private XmlTag                        errorCode;
    private XmlTag                        errorMessage;
    private XmlTag                        resultCount;
    private GenerateXmlKuchikomiKuchikomi kuchikomi;

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        XmlTag.setParent( root, resultCount );

        if ( kuchikomi != null )
        {
            kuchikomi.setRootNode( root );
            kuchikomi.initXmlNodeInfo();
        }

        return;
    }

    public void setErrorCode(int code)
    {
        errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, code );
        return;
    }

    public void setError(String message)
    {
        errorMessage = XmlTag.createXmlTag( TAG_ERROR_MESSAGE, message );
        return;
    }

    public void setResultCount(int count)
    {
        resultCount = XmlTag.createXmlTag( TAG_RESULT_COUNT, count );
        return;
    }

    public void addKuchikomi(GenerateXmlKuchikomiKuchikomi addKuchikomi)
    {
        kuchikomi = addKuchikomi;
        return;
    }
}
