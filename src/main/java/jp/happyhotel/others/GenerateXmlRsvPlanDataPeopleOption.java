package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML PMS予約連携プランデータ人数オプション
 */
public class GenerateXmlRsvPlanDataPeopleOption extends WebApiResultBase
{
    // タグ名
    private static final String TAG_PEOPLE_OPTION = "PeopleOption";
    private static final String TAG_LABEL         = "Label";
    private static final String TAG_NUMBER_MIN    = "NumberMin";
    private static final String TAG_NUMBER_MAX    = "NumberMax";

    private XmlTag              peopleOption;
    private XmlTag              label;
    private XmlTag              numberMin;
    private XmlTag              numberMax;

    @Override
    protected void initXmlNodeInfo()
    {
        peopleOption = createRootChild( TAG_PEOPLE_OPTION );
        XmlTag.setParent( peopleOption, label );
        XmlTag.setParent( peopleOption, numberMin );
        XmlTag.setParent( peopleOption, numberMax );

        return;
    }

    public void setLabel(String label)
    {
        this.label = XmlTag.createXmlTag( TAG_LABEL, label );
    }

    public void setNumberMin(String numberMin)
    {
        this.numberMin = XmlTag.createXmlTag( TAG_NUMBER_MIN, numberMin );
    }

    public void setNumberMax(String numberMax)
    {
        this.numberMax = XmlTag.createXmlTag( TAG_NUMBER_MAX, numberMax );
    }

}
