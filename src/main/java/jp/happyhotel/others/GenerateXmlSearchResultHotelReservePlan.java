package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

// �z�e���\����
public class GenerateXmlSearchResultHotelReservePlan extends WebApiResultBase
{
    // �^�O��
    private static final String TAG_PLAN      = "plan";
    private static final String TAG_PLAN_NO   = "no";
    private static final String TAG_PLAN_NAME = "name";

    private XmlTag              plan;                  // �\��v�������i�[�^�O
    private XmlTag              planNo;                // �\��v�����ԍ�
    private XmlTag              planName;              // �\��v������

    @Override
    protected void initXmlNodeInfo()
    {
        plan = createRootChild( TAG_PLAN );
        XmlTag.setParent( plan, planNo );
        XmlTag.setParent( plan, planName );
        return;
    }

    public void setNo(int no)
    {
        planNo = XmlTag.createXmlTag( TAG_PLAN_NO, no );
        return;
    }

    public void setName(String name)
    {
        planName = XmlTag.createXmlTag( TAG_PLAN_NAME, name );
        return;
    }
}
