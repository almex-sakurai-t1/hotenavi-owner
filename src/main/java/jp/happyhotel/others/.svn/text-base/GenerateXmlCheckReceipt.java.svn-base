package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

public class GenerateXmlCheckReceipt extends WebApiResultBase
{
    // É^ÉOñº
    private static final String TAG_HEADER        = "header";
    private static final String TAG_HEADER_METHOD = "method";
    private static final String TAG_HEADER_NAME   = "name";
    private static final String TAG_HEADER_COUNT  = "count";
    private static final String TAG_ERRORCODE     = "errorCode";
    private static final String TAG_ERRORMESSAGE  = "errorMessage";

    private XmlTag              header;
    private XmlTag              headerMethod;
    private XmlTag              headerName;
    private XmlTag              headerCount;
    private XmlTag              errorCode;
    private XmlTag              errorMessage;

    @Override
    protected void initXmlNodeInfo()
    {
        header = createRootChild( TAG_HEADER );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        XmlTag.setParent( header, headerMethod );
        XmlTag.setParent( header, headerName );
        XmlTag.setParent( header, headerCount );

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
}
