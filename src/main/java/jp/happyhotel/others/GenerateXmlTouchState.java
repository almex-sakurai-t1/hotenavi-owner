package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * タッチ状態XML作成クラス
 */
public class GenerateXmlTouchState extends WebApiResultBase
{
    // タグ名
    private static final String TAG_TOUCH_STATE   = "TouchState";
    private static final String TAG_ERROR_CODE    = "errorCode";
    private static final String TAG_ERROR_MESSAGE = "errorMessage";
    private static final String TAG_RESULT_COUNT  = "ResultCount";
    private static final String TAG_URL           = "url";

    private XmlTag              errorCode;
    private XmlTag              errorMessage;
    private XmlTag              resultCount;
    private XmlTag              touchState;
    private XmlTag              url;

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        XmlTag.setParent( root, resultCount );
        touchState = createRootChild( TAG_TOUCH_STATE );
        XmlTag.setParent( touchState, url );

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

    public void setUrl(String strUrl)
    {
        if ( strUrl.equals( "" ) == false )
        {
            url = XmlTag.createXmlTag( TAG_URL, strUrl );
        }
        return;
    }
}
