package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * ホテル詳細料金情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// ホテル詳細料金情報
public class GenerateXmlDetailHotelPricesKind extends WebApiResultBase
{
    // タグ名
    private static final String TAG_KIND         = "kind";
    private static final String TAG_KIND_NAME    = "name";
    private static final String TAG_KIND_MESSAGE = "message";
    private static final String TAG_KIND_REMARKS = "remarks";

    private XmlTag              kind;                        // プラン種別情報格納タグ
    private XmlTag              kindName;                    // プラン種別名
    private XmlTag              kindMessage;                 // 料金情報
    private XmlTag              kindRemarks;                 // 備考

    @Override
    protected void initXmlNodeInfo()
    {
        kind = createRootChild( TAG_KIND );
        XmlTag.setParent( kind, kindName );
        XmlTag.setParent( kind, kindMessage );
        XmlTag.setParent( kind, kindRemarks );

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

    public void setRemarks(String remarks)
    {
        if ( remarks != null )
        {
            remarks = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( remarks ) );
        }
        kindRemarks = XmlTag.createXmlTag( TAG_KIND_REMARKS, remarks );
        return;
    }

}
