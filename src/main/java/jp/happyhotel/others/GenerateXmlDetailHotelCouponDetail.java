package jp.happyhotel.others;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * ホテル詳細クーポン情報xml生成クラス
 * 
 * @author N.Ide
 * @version 1.0 2011/04/25
 */

// ホテル詳細クーポン情報
public class GenerateXmlDetailHotelCouponDetail extends WebApiResultBase
{
    // タグ名
    private static final String TAG_DETAIL           = "detail";
    private static final String TAG_DETAIL_TEXT      = "text";
    private static final String TAG_DETAIL_CONDITION = "condition";

    private XmlTag              detail;                            // クーポン詳細情報格納タグ
    private XmlTag              detailText;                        // 特典内容
    private XmlTag              detailCondition;                   // 使用条件

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
