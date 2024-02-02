package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * 市区町村マスタ情報
 */
public class GenerateXmlHapiTouchHotelInfoEmployee extends WebApiResultBase
{
    // タグ名
    private static final String TAG_EMPLOYEE = "Employee";
    private static final String TAG_CODE     = "Code";
    private static final String TAG_NAME     = "Name";

    private XmlTag              employee;
    private XmlTag              code;
    private XmlTag              name;

    @Override
    protected void initXmlNodeInfo()
    {
        employee = createRootChild( TAG_EMPLOYEE );
        XmlTag.setParent( employee, code );
        XmlTag.setParent( employee, name );
        return;
    }

    public void setCode(String code)
    {
        this.code = XmlTag.createXmlTag( TAG_CODE, code );
    }

    public void setName(String name)
    {
        this.name = XmlTag.createXmlTag( TAG_NAME, name );
    }

}
