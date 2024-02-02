package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

public class GenerateXmlIssueId extends WebApiResultBase
{
    // À¸Þ–¼
    private static final String TAG_HEADER        = "header";
    private static final String TAG_HEADER_METHOD = "method";
    private static final String TAG_HEADER_NAME   = "name";
    private static final String TAG_HEADER_COUNT  = "count";
    private static final String TAG_ERRORCODE     = "errorCode";
    private static final String TAG_ERRORMESSAGE  = "errorMessage";
    private static final String TAG_USER          = "user";
    private static final String TAG_UUID          = "uuid";
    private static final String TAG_ID            = "id";
    private static final String TAG_PASSWORD      = "password";

    private XmlTag              header;
    private XmlTag              headerMethod;
    private XmlTag              headerName;
    private XmlTag              headerCount;
    private XmlTag              errorCode;
    private XmlTag              errorMessage;
    private XmlTag              user;
    private XmlTag              uuid;
    private XmlTag              userId;
    private XmlTag              userPassword;

    @Override
    protected void initXmlNodeInfo()
    {
        header = createRootChild( TAG_HEADER );
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        user = createRootChild( TAG_USER );
        XmlTag.setParent( user, userId );
        XmlTag.setParent( user, userPassword );
        XmlTag.setParent( user, uuid );

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

    public void setUserId(String id)
    {
        userId = XmlTag.createXmlTag( TAG_ID, id );
        return;
    }

    public void setUserPassword(String pass)
    {
        userPassword = XmlTag.createXmlTag( TAG_PASSWORD, pass );
        return;
    }

    public void setUuid(String strUuid)
    {
        uuid = XmlTag.createXmlTag( TAG_UUID, strUuid );
    }
}
