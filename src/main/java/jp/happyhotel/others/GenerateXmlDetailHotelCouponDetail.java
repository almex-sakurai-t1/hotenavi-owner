package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * �z�e���ڍ׃N�[�|�����xml�����N���X
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// �z�e���ڍ׃N�[�|�����
public class GenerateXmlDetailHotelCouponDetail extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_DETAIL           = "detail";
    private static final String TAG_DETAIL_TEXT      = "text";
    private static final String TAG_DETAIL_CONDITION = "condition";

    private XmlTag              detail;                            // �N�[�|���ڍ׏��i�[�^�O
    private XmlTag              detailText;                        // ���T���e
    private XmlTag              detailCondition;                   // �g�p����

    @Override
    protected void initXmlNodeInfo()
    {
        detail = createRootChild( TAG_DETAIL );
        XmlTag.setParent( detail, detailText );
        if ( detailCondition != null )
        {
            XmlTag.setParent( detail, detailCondition );
        }
        return;
    }

    public void setText(String text)
    {
        if ( text != null )
        {
            text = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( text ) );
        }
        detailText = XmlTag.createXmlTag( TAG_DETAIL_TEXT, text );
        return;
    }

    public void setCondition(String condition)
    {
        if ( condition != null && condition.equals( "" ) == false )
        {
            condition = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( condition ) );
        }
        else
        {
            condition = " ";
        }
        detailCondition = XmlTag.createXmlTag( TAG_DETAIL_CONDITION, condition );
        return;
    }
}
