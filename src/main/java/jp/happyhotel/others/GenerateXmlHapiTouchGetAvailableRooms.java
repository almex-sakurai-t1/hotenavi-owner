package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchGetAvailableRooms extends WebApiResultBase
{
    // タグ名
    private static final String                              TAG_GETAVAILABLEROOMS = "GetAvailableRooms";
    private static final String                              TAG_RESULT            = "Result";

    private String                                           tagName;
    private XmlTag                                           result;                                                                        // 結果用タグ
    private ArrayList<GenerateXmlHapiTouchHotelInfoEmployee> employeeList          = new ArrayList<GenerateXmlHapiTouchHotelInfoEmployee>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_GETAVAILABLEROOMS );
        XmlTag.setParent( root, result );
        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

}
