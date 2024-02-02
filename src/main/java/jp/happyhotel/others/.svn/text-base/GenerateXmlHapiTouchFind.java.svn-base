package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchFind extends WebApiResultBase
{
    // タグ名
    private static final String                              TAG_FIND       = "Find";
    private static final String                              TAG_RESULT     = "Result";
    private static final String                              TAG_POINT      = "Point";
    private static final String                              TAG_MESSAGE    = "Message";
    private static final String                              TAG_REGIST_URL = "RegistUrl";
    private static final String                              TAG_CI_CODE    = "CiCode";
    private static final String                              TAG_IDM        = "Idm";
    private static final String                              TAG_EMPLOYEE   = "Employee";
    private static final String                              TAG_USE_POINT  = "UsePoint";

    private XmlTag                                           result;                                                                 // 結果用タグ
    private XmlTag                                           point;                                                                  // ポイント用タグ
    private XmlTag                                           messsage;                                                               // 結果の応答メッセージ用タグ
    private XmlTag                                           registUrl;
    private XmlTag                                           ciCode;
    private XmlTag                                           idm;
    private ArrayList<GenerateXmlHapiTouchHotelInfoEmployee> employeeList   = new ArrayList<GenerateXmlHapiTouchHotelInfoEmployee>();
    private XmlTag                                           usePoint;

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_FIND );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, point );
        XmlTag.setParent( root, messsage );
        XmlTag.setParent( root, registUrl );
        XmlTag.setParent( root, ciCode );
        XmlTag.setParent( root, idm );
        if ( employeeList != null )
        {
            for( int i = 0 ; i < employeeList.size() ; i++ )
            {
                this.employeeList.get( i ).setRootNode( root );
                this.employeeList.get( i ).initXmlNodeInfo();
            }
        }
        XmlTag.setParent( root, usePoint );
        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setPoint(int point)
    {
        this.point = XmlTag.createXmlTag( TAG_POINT, point );
    }

    public void setMessage(String messsage)
    {
        this.messsage = XmlTag.createXmlTag( TAG_MESSAGE, messsage );
    }

    public void setRegistUrl(String registUrl)
    {
        this.registUrl = XmlTag.createXmlTag( TAG_REGIST_URL, registUrl );
    }

    public void setCiCode(int ciCode)
    {
        this.ciCode = XmlTag.createXmlTag( TAG_CI_CODE, ciCode );
    }

    public void setIdm(String idm)
    {
        this.idm = XmlTag.createXmlTag( TAG_IDM, idm );
    }

    public void setEmployee(GenerateXmlHapiTouchHotelInfoEmployee employee)
    {
        this.employeeList.add( employee );
        return;
    }

    public void setUsePoint(int usePoint)
    {
        this.idm = XmlTag.createXmlTag( TAG_USE_POINT, usePoint );
    }

}
