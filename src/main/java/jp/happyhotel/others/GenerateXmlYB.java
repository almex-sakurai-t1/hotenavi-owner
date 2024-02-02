package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlAttribute;
import jp.happyhotel.common.XmlTag;

// 検索結果ヘッダ
public class GenerateXmlYB extends WebApiResultBase
{
    // タグ名
    private static final String TAG_RESULT_SET                = "ResultSet";
    private static final String TAG_RESULT_SET_STATUS         = "Status";
    private static final String TAG_RESULT_SET_PURCHASE_SHIFT = "PurchaseShift";

    private XmlTag              resultSet;
    private XmlTag              status;
    private XmlTag              purchaseShift;
    private XmlAttribute        attribute;
    private XmlAttribute        attribute2;
    private XmlAttribute        attribute3;

    @Override
    protected void initXmlNodeInfo()
    {
        resultSet = createRootChild( TAG_RESULT_SET );

        attribute = new XmlAttribute( "xmlns", "urn:yahoo:jp:order:WalletGateway:NotifyAck" );
        attribute2 = new XmlAttribute( "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" );
        attribute3 = new XmlAttribute( "xsi:schemaLocation", "urn:yahoo:jp:order:WalletGateway:NotifyAck" +
                "http://ord.wallet.yahooapis.jp/WalletGateway/V1/notify_ack.xsd" );

        resultSet.addAttribute( attribute );
        resultSet.addAttribute( attribute2 );
        resultSet.addAttribute( attribute3 );

        XmlTag.setParent( resultSet, status );
        XmlTag.setParent( resultSet, purchaseShift );

        return;
    }

    public void setStatus(int nStatus)
    {
        status = XmlTag.createXmlTag( TAG_RESULT_SET_STATUS, nStatus );
        return;
    }

    public void setPurchase(int purchaseshift)
    {
        purchaseShift = XmlTag.createXmlTag( TAG_RESULT_SET_PURCHASE_SHIFT, purchaseshift );
        return;
    }

    public void setPurchase(String purchaseshift)
    {
        purchaseShift = XmlTag.createXmlTag( TAG_RESULT_SET_PURCHASE_SHIFT, purchaseshift );
        return;
    }

}
