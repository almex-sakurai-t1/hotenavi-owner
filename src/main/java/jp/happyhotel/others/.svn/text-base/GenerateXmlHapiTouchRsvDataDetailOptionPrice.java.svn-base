package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約データデータ
 */
public class GenerateXmlHapiTouchRsvDataDetailOptionPrice extends WebApiResultBase
{
    // タグ名
    private static final String TAG_OPTION_PRICE        = "OptionPrice";
    private static final String TAG_OPTION_NAME         = "OptionName";
    private static final String TAG_OPTION_UNIT_PRICE   = "OptionUnitPrice";
    private static final String TAG_OPTION_MAX_QUANTITY = "OptionMaxQuantity";
    private static final String TAG_OPTION_TOTAL        = "OptionTotal";
    private static final String TAG_OPTION_REMARKS      = "OptionRemarks";

    private XmlTag              optionPrice;
    private XmlTag              optionName;
    private XmlTag              optionUnitPrice;
    private XmlTag              optionMaxQuantity;
    private XmlTag              optionTotal;
    private XmlTag              optionRemarks;

    @Override
    protected void initXmlNodeInfo()
    {
        optionPrice = createRootChild( TAG_OPTION_PRICE );
        XmlTag.setParent( optionPrice, optionName );
        XmlTag.setParent( optionPrice, optionUnitPrice );
        XmlTag.setParent( optionPrice, optionMaxQuantity );
        XmlTag.setParent( optionPrice, optionTotal );
        XmlTag.setParent( optionPrice, optionRemarks );

        return;
    }

    public void setOptionName(String optionName)
    {
        this.optionName = XmlTag.createXmlTag( TAG_OPTION_NAME, optionName );
    }

    public void setOptionUnitPrice(String optionUnitPrice)
    {
        this.optionUnitPrice = XmlTag.createXmlTag( TAG_OPTION_UNIT_PRICE, optionUnitPrice );
    }

    public void setOptionMaxQuantity(int optionMaxQuantity)
    {
        this.optionMaxQuantity = XmlTag.createXmlTag( TAG_OPTION_MAX_QUANTITY, optionMaxQuantity );
    }

    public void setOptionTotal(String optionTotal)
    {
        this.optionTotal = XmlTag.createXmlTag( TAG_OPTION_TOTAL, optionTotal );
    }

    public void setOptionRemarks(String optionRemarks)
    {
        this.optionPrice = XmlTag.createXmlTag( TAG_OPTION_REMARKS, optionRemarks );
    }

}
