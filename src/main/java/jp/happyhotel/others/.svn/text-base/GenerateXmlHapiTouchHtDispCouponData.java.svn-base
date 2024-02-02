package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチクーポン表示情報
 */
public class GenerateXmlHapiTouchHtDispCouponData extends WebApiResultBase
{
    // タグ名
    private static final String TAG_COUPON_DATA = "CouponData";
    private static final String TAG_SUB_SEQ     = "SubSeq";
    private static final String TAG_TITLE       = "Title";
    private static final String TAG_CONDITION   = "Condition";
    private static final String TAG_USED_FLAG   = "UsedFlag";

    private XmlTag              couponData;
    private XmlTag              subSeq;                        // エラーコード用タグ
    private XmlTag              title;                         //
    private XmlTag              condition;                     //
    private XmlTag              usedFlag;                      //

    @Override
    protected void initXmlNodeInfo()
    {
        couponData = createRootChild( TAG_COUPON_DATA );

        XmlTag.setParent( couponData, subSeq );
        XmlTag.setParent( couponData, title );
        XmlTag.setParent( couponData, condition );
        XmlTag.setParent( couponData, usedFlag );
        return;
    }

    public void setSubSeq(int subseq)
    {
        this.subSeq = XmlTag.createXmlTag( TAG_SUB_SEQ, subseq );
    }

    public void setTitle(String title)
    {
        this.title = XmlTag.createXmlTag( TAG_TITLE, title );
    }

    public void setCondition(String condition)
    {
        this.condition = XmlTag.createXmlTag( TAG_CONDITION, condition );
    }

    public void setUsedFlag(int usedflag)
    {
        this.usedFlag = XmlTag.createXmlTag( TAG_USED_FLAG, usedflag );
    }

}
