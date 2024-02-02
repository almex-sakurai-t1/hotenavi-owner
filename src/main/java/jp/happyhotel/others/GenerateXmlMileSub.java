package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * �}�C���T�uXML�쐬�N���X
 */
public class GenerateXmlMileSub extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_DATA      = "data";
    private static final String TAG_MONTH     = "month";
    private static final String TAG_LOSTPOINT = "lost_point";

    private XmlTag              data;                        //
    private XmlTag              month;                       // �����Ώی�
    private XmlTag              lostPoint;                   // �����}�C��

    @Override
    protected void initXmlNodeInfo()
    {
        data = createRootChild( TAG_DATA );
        XmlTag.setParent( data, month );
        XmlTag.setParent( data, lostPoint );
        return;
    }

    public void setMonth(String strMonth)
    {
        month = XmlTag.createXmlTag( TAG_MONTH, strMonth );
        return;
    }

    public void setLostPoint(String lostpoint)
    {
        lostPoint = XmlTag.createXmlTag( TAG_LOSTPOINT, lostpoint );
        return;
    }

}
