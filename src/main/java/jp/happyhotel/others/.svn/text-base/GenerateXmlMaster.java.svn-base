package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * マスターデータの共通情報xml生成クラス
 * 
 * @author S.Tashiro
 * @version 1.0 2011/05/06
 */

// コンテンツ共通
public class GenerateXmlMaster extends WebApiResultBase
{
    // タグ名
    private static final String TAG_ERROR_CODE    = "errorCode";
    private static final String TAG_ERROR_MESSAGE = "errorMessage";
    private static final String TAG_MASTER        = "master";
    private static final String TAG_VERSION       = "version";

    private XmlTag              errorCode;
    private XmlTag              errorMessage;
    private XmlTag              master;
    private XmlTag              version;

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        if ( master != null )
        {
            master = XmlTag.createXmlTag( TAG_MASTER );
            XmlTag.setParent( root, master );
            XmlTag.setParent( master, version );

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

    public void setMaster(String masterTag)
    {
        master = XmlTag.createXmlTag( TAG_MASTER, masterTag );
        return;
    }

    public void setVersion(String ver)
    {
        version = XmlTag.createXmlTag( TAG_VERSION, ver );
        return;
    }
}
