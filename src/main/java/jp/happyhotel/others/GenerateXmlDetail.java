package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * ホテル詳細情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/2
 */

// コンテンツ共通
public class GenerateXmlDetail extends WebApiResultBase
{
    // タグ名
    private static final String    TAG_ERROR_CODE    = "errorCode";
    private static final String    TAG_ERROR_MESSAGE = "errorMessage";

    private XmlTag                 detail;                            // ヘッダ情報格納タグ
    private XmlTag                 errorCode;
    private XmlTag                 errorMessage;
    private GenerateXmlDetailHotel hotel;

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        if ( hotel != null )
        {
            hotel.setRootNode( root );
            hotel.initXmlNodeInfo();
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

    public void addHotel(GenerateXmlDetailHotel addHotel)
    {
        hotel = addHotel;
        return;
    }
}
