package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

public class GenerateXmlLoginCheck extends WebApiResultBase
{
    // À¸Þ–¼
    private static final String TAG_HEADER        = "header";
    private static final String TAG_HEADER_METHOD = "method";
    private static final String TAG_HEADER_NAME   = "name";
    private static final String TAG_HEADER_COUNT  = "count";
    private static final String TAG_ERRORCODE     = "errorCode";
    private static final String TAG_ERRORMESSAGE  = "errorMessage";
    private static final String TAG_CHECKUSER     = "checkUser";
    private static final String TAG_KIND          = "kind";
    private static final String TAG_KIND_MESSAGE  = "kindMessage";
    private static final String TAG_MESSAGE       = "message";
    private static final String TAG_MESSAGEURL    = "messageUrl";

    private XmlTag              header;
    private XmlTag              headerMethod;
    private XmlTag              headerName;
    private XmlTag              headerCount;
    private XmlTag              errorCode;
    private XmlTag              errorMessage;
    private XmlTag              checkUser;
    private XmlTag              checkUserKind;
    private XmlTag              checkUserKindMessage;
    private XmlTag              checkUserMessage;
    private XmlTag              checkUserMessageUrl;

    @Override
    protected void initXmlNodeInfo()
    {
        header = createRootChild( TAG_HEADER );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        checkUser = createRootChild( TAG_CHECKUSER );

        XmlTag.setParent( checkUser, checkUserKind );
        XmlTag.setParent( checkUser, checkUserKindMessage );
        XmlTag.setParent( checkUser, checkUserMessage );
        XmlTag.setParent( checkUser, checkUserMessageUrl );

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

    public void setKind(int kind)
    {
        checkUserKind = XmlTag.createXmlTag( TAG_KIND, kind );
        return;
    }

    public void setKindMessage(String kindMessage)
    {
        checkUserKindMessage = XmlTag.createXmlTag( TAG_KIND_MESSAGE, kindMessage );
        return;
    }

    public void setMessage(String message)
    {
        checkUserMessage = XmlTag.createXmlTag( TAG_MESSAGE, message );
        return;
    }

    public void setMessageUrl(String messageUrl)
    {
        checkUserMessageUrl = XmlTag.createXmlTag( TAG_MESSAGEURL, messageUrl );
        return;
    }

}
