package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * ホテル詳細設備情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// ホテル詳細設備情報
public class GenerateXmlDetailHotelEquipsKind extends WebApiResultBase
{
    // タグ名
    private static final String TAG_KIND         = "kind";
    private static final String TAG_KIND_NAME    = "name";
    private static final String TAG_KIND_MESSAGE = "message";

    private XmlTag              kind;                        // 設備種別情報格納タグ
    private XmlTag              kindName;                    // 設備種別名
    private XmlTag              kindMessage;                 // 設備情報

    @Override
    protected void initXmlNodeInfo()
    {
        kind = createRootChild( TAG_KIND );
        XmlTag.setParent( kind, kindName );
        XmlTag.setParent( kind, kindMessage );

        return;
    }

    public void setName(String name)
    {
        if ( name != null )
        {
            name = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( name ) );
        }
        kindName = XmlTag.createXmlTag( TAG_KIND_NAME, name );
        return;
    }

    public void setMessage(String message)
    {
        if ( message != null )
        {
            message = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( message ) );
        }
        kindMessage = XmlTag.createXmlTag( TAG_KIND_MESSAGE, message );
        return;
    }

}
