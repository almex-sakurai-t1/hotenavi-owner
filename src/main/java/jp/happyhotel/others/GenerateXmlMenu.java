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
public class GenerateXmlMenu extends WebApiResultBase
{
    // タグ名
    private static final String    TAG_ERROR_CODE    = "errorCode";
    private static final String    TAG_ERROR_MESSAGE = "errorMessage";
    private static final String    TAG_RESULT_COUNT  = "ResultCount";
    private static final String    TAG_LOGIN_STATUS  = "LoginStatus";
    private static final String    TAG_MESSAGE       = "Message";

    private XmlTag                 errorCode;
    private XmlTag                 errorMessage;
    private XmlTag                 resultCount;
    private XmlTag                 loginStatus;
    private XmlTag                 message;
    private GenerateXmlMenuTop     gmTop;
    private GenerateXmlMenuSpecial gmSpecial;
    private GenerateXmlTopics      gmTopics;
    private GenerateXmlMile        gmMile;
    private GenerateXmlTopMessage  gmTopMsg;

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        XmlTag.setParent( root, resultCount );
        XmlTag.setParent( root, loginStatus );
        XmlTag.setParent( root, message );

        if ( gmTop != null )
        {
            gmTop.setRootNode( root );
            gmTop.initXmlNodeInfo();
        }
        if ( gmSpecial != null )
        {
            gmSpecial.setRootNode( root );
            gmSpecial.initXmlNodeInfo();
        }

        if ( gmTopics != null )
        {
            gmTopics.setRootNode( root );
            gmTopics.initXmlNodeInfo();
        }
        if ( gmMile != null )
        {
            gmMile.setRootNode( root );
            gmMile.initXmlNodeInfo();
        }
        if ( gmTopMsg != null )
        {
            gmTopMsg.setRootNode( root );
            gmTopMsg.initXmlNodeInfo();
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

    public void setLoginStatus(int status)
    {
        loginStatus = XmlTag.createXmlTag( TAG_LOGIN_STATUS, status );
        return;
    }

    public void setMessage(String msg)
    {
        message = XmlTag.createXmlTag( TAG_MESSAGE, msg );
        return;
    }

    public void addMenuTop(GenerateXmlMenuTop addMenuTop)
    {
        gmTop = addMenuTop;
        return;
    }

    public void addMenuSpecial(GenerateXmlMenuSpecial addMenuSpecial)
    {
        gmSpecial = addMenuSpecial;
        return;
    }

    public void addTopics(GenerateXmlTopics addTopics)
    {
        gmTopics = addTopics;
        return;
    }

    public void addMile(GenerateXmlMile addMile)
    {
        gmMile = addMile;
        return;
    }

    public void addTopMessage(GenerateXmlTopMessage addTopMsg)
    {
        gmTopMsg = addTopMsg;
        return;
    }
}
