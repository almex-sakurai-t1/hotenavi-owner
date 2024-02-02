package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchModifyCi extends WebApiResultBase
{
    // タグ名
    private static final String                              TAG_AMOUNT        = "ModifyCi";
    private static final String                              TAG_RESULT        = "Result";
    private static final String                              TAG_VISIT_RESULT  = "VisitResult";
    private static final String                              TAG_AMOUNT_RESULT = "AmountResult";
    private static final String                              TAG_USE_RESULT    = "UseResult";
    private static final String                              TAG_POINT         = "Point";
    private static final String                              TAG_MESSAGE       = "Message";
    private static final String                              TAG_REGIST_URL    = "RegistUrl";
    private static final String                              TAG_TIME          = "Time";
    private static final String                              TAG_FILE32        = "File32";
    private static final String                              TAG_FILE64        = "File64";
    private static final String                              TAG_EMPLOYEE      = "Employee";
    private static final String                              TAG_CI_STATUS     = "CiStatus";
    private static final String                              TAG_ERROR_CODE    = "ErrorCode";

    private String                                           tagName;
    private XmlTag                                           result;                                                                    // 結果用タグ
    private XmlTag                                           visitResult;                                                               // 来店ハピー結果用タグ（ハピー付与の場合のみ）
    private XmlTag                                           amountResult;                                                              // ハピー付与結果用タグ
    private XmlTag                                           useResult;                                                                 // ハピー使用結果用タグ
    private XmlTag                                           point;                                                                     // ポイント用タグ
    private XmlTag                                           messsage;                                                                  // 結果の応答メッセージ用タグ
    private XmlTag                                           registUrl;
    private XmlTag                                           ciStatus;
    private XmlTag                                           errorCode;
    private ArrayList<GenerateXmlHapiTouchHotelInfoEmployee> employeeList      = new ArrayList<GenerateXmlHapiTouchHotelInfoEmployee>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_AMOUNT );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, amountResult );
        XmlTag.setParent( root, useResult );
        XmlTag.setParent( root, point );
        XmlTag.setParent( root, messsage );
        XmlTag.setParent( root, registUrl );
        XmlTag.setParent( root, ciStatus );
        XmlTag.setParent( root, visitResult );
        XmlTag.setParent( root, errorCode );

        if ( employeeList != null )
        {
            for( int i = 0 ; i < employeeList.size() ; i++ )
            {
                this.employeeList.get( i ).setRootNode( root );
                this.employeeList.get( i ).initXmlNodeInfo();
            }
        }
        return;
    }

    public void setResult(String result)
    {
        this.result = XmlTag.createXmlTag( TAG_RESULT, result );
    }

    public void setVisitResult(String visitResult)
    {
        this.visitResult = XmlTag.createXmlTag( TAG_VISIT_RESULT, visitResult );
    }

    public void setAmountResult(String amountResult)
    {
        this.amountResult = XmlTag.createXmlTag( TAG_AMOUNT_RESULT, amountResult );
    }

    public void setUseResult(String useResult)
    {
        this.useResult = XmlTag.createXmlTag( TAG_USE_RESULT, useResult );
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

    public void setCiStatus(int status)
    {
        this.ciStatus = XmlTag.createXmlTag( TAG_CI_STATUS, status );
    }

    public void setErrorCpde(int errorCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errorCode );
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errorCode );
    }

    public void setEmployee(GenerateXmlHapiTouchHotelInfoEmployee employee)
    {
        this.employeeList.add( employee );
        return;
    }

}
