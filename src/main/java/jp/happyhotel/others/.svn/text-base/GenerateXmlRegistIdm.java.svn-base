package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

public class GenerateXmlRegistIdm extends WebApiResultBase
{
    // À¸Þ–¼
    private static final String TAG_HEADER        = "header";
    private static final String TAG_HEADER_METHOD = "method";
    private static final String TAG_HEADER_NAME   = "name";
    private static final String TAG_HEADER_COUNT  = "count";
    private static final String TAG_ERRORCODE     = "errorCode";
    private static final String TAG_ERRORMESSAGE  = "errorMessage";
    private static final String TAG_REGISTIDM     = "registIdm";
    private static final String TAG_MESSAGE       = "message";
    private static final String TAG_MESSAGEURL    = "messageUrl";
    private static final String TAG_MEMO          = "memo";
    private static final String TAG_MEMOURL       = "memoUrl";

    private XmlTag              header;
    private XmlTag              headerMethod;
    private XmlTag              headerName;
    private XmlTag              headerCount;
    private XmlTag              errorCode;
    private XmlTag              errorMessage;
    private XmlTag              registIdm;
    private XmlTag              registIdmMessage;
    private XmlTag              registIdmMessageUrl;
    private XmlTag              registIdmMemo;
    private XmlTag              registIdmMemoUrl;

    @Override
    protected void initXmlNodeInfo()
    {
        header = createRootChild( TAG_HEADER );
        XmlTag.setParent( header, headerMethod );
        XmlTag.setParent( header, headerName );
        XmlTag.setParent( header, headerCount );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        registIdm = createRootChild( TAG_REGISTIDM );
        XmlTag.setParent( registIdm, registIdmMessage );
        XmlTag.setParent( registIdm, registIdmMessageUrl );
        XmlTag.setParent( registIdm, registIdmMemo );
        XmlTag.setParent( registIdm, registIdmMemoUrl );

        return;
    }

    public void setMethod(String method)
    {
        headerMethod = XmlTag.createXmlTag( TAG_HEADER_METHOD, method );
        return;
    }

    public void setName(String name)
    {
        headerName = XmlTag.createXmlTag( TAG_HEADER_NAME, name );
        return;
    }

    public void setCount(int count)
    {
        headerCount = XmlTag.createXmlTag( TAG_HEADER_COUNT, count );
        return;
    }

    public void setErrorCode(int code)
    {
        errorCode = XmlTag.createXmlTag( TAG_ERRORCODE, code );
        return;
    }

    public void setErrorMessage(String message)
    {
        errorMessage = XmlTag.createXmlTagNoCheck( TAG_ERRORMESSAGE, message );
        return;
    }

    public void setMessage(String message)
    {
        registIdmMessage = XmlTag.createXmlTag( TAG_MESSAGE, message );
        return;
    }

    public void setMessageUrl(String messageUrl)
    {
        registIdmMessageUrl = XmlTag.createXmlTag( TAG_MESSAGEURL, messageUrl );
        return;
    }

    public void setMemo(String memo)
    {
        registIdmMemo = XmlTag.createXmlTag( TAG_MEMO, memo );
        return;
    }

    public void setMemoUrl(String memoUrl)
    {
        registIdmMemoUrl = XmlTag.createXmlTag( TAG_MEMOURL, memoUrl );
        return;
    }
}
