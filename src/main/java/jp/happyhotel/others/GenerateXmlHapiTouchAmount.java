package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML�n�s�^�b�`���
 */
public class GenerateXmlHapiTouchAmount extends WebApiResultBase
{
    // �^�O��
    private static final String                              TAG_AMOUNT       = "Amount";
    private static final String                              TAG_RESULT       = "Result";
    private static final String                              TAG_VISIT_RESULT = "Visit_Result";
    private static final String                              TAG_POINT        = "Point";
    private static final String                              TAG_MESSAGE      = "Message";
    private static final String                              TAG_REGIST_URL   = "RegistUrl";
    private static final String                              TAG_TIME         = "Time";
    private static final String                              TAG_FILE32       = "File32";
    private static final String                              TAG_FILE64       = "File64";
    private static final String                              TAG_EMPLOYEE     = "Employee";
    private static final String                              TAG_STATUS       = "Status";

    private String                                           tagName;
    private XmlTag                                           result;                                                                   // ���ʗp�^�O
    private XmlTag                                           visitResult;                                                              // ���X�n�s�[���ʗp�^�O�i�n�s�[�t�^�̏ꍇ�̂݁j
    private XmlTag                                           point;                                                                    // �|�C���g�p�^�O
    private XmlTag                                           messsage;                                                                 // ���ʂ̉������b�Z�[�W�p�^�O
    private XmlTag                                           registUrl;
    private XmlTag                                           time;                                                                     // ���݂̃T�[�o�[�����p�^�O
    private XmlTag                                           file32;                                                                   // 32�r�b�g�p�^�O
    private XmlTag                                           file64;                                                                   // 64�r�b�g�p�^�O
    private XmlTag                                           status;
    private ArrayList<GenerateXmlHapiTouchHotelInfoEmployee> employeeList     = new ArrayList<GenerateXmlHapiTouchHotelInfoEmployee>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_AMOUNT );
        XmlTag.setParent( root, result );
        XmlTag.setParent( root, visitResult );
        XmlTag.setParent( root, point );
        XmlTag.setParent( root, messsage );
        XmlTag.setParent( root, registUrl );
        XmlTag.setParent( root, time );
        XmlTag.setParent( root, file32 );
        XmlTag.setParent( root, file64 );
        XmlTag.setParent( root, status );

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

    public void setVisitResult(String result)
    {
        this.visitResult = XmlTag.createXmlTag( TAG_VISIT_RESULT, result );
    }

    public void setPoint(int point)
    {
        this.result = XmlTag.createXmlTag( TAG_POINT, point );
    }

    public void setMessage(String messsage)
    {
        this.messsage = XmlTag.createXmlTag( TAG_MESSAGE, messsage );
    }

    public void setRegistUrl(String registUrl)
    {
        this.messsage = XmlTag.createXmlTag( TAG_REGIST_URL, registUrl );
    }

    public void setTime(String time)
    {
        this.time = XmlTag.createXmlTag( TAG_TIME, time );
    }

    public void setFile32(String file32)
    {
        this.file32 = XmlTag.createXmlTag( TAG_FILE32, file32 );
    }

    public void setFile64(String file64)
    {
        this.file64 = XmlTag.createXmlTag( TAG_FILE64, file64 );
    }

    public void setStatus(int status)
    {
        this.status = XmlTag.createXmlTag( TAG_STATUS, status );
    }

    public void setEmployee(GenerateXmlHapiTouchHotelInfoEmployee employee)
    {
        this.employeeList.add( employee );
        return;
    }

}
