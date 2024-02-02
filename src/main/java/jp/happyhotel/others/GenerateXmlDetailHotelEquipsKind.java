package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * �z�e���ڍאݔ����xml�����N���X
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// �z�e���ڍאݔ����
public class GenerateXmlDetailHotelEquipsKind extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_KIND         = "kind";
    private static final String TAG_KIND_NAME    = "name";
    private static final String TAG_KIND_MESSAGE = "message";

    private XmlTag              kind;                        // �ݔ���ʏ��i�[�^�O
    private XmlTag              kindName;                    // �ݔ���ʖ�
    private XmlTag              kindMessage;                 // �ݔ����

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
