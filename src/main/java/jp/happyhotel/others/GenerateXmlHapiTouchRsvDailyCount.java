package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML�n�s�^�b�`�\��f�[�^�擾
 */
public class GenerateXmlHapiTouchRsvDailyCount extends WebApiResultBase
{
    // �^�O��
    private static final String                             TAG_RSV_DATA   = "RsvDailyCount";
    private static final String                             TAG_DATA       = "Data";
    private static final String                             TAG_ERROR_CODE = "ErrorCode";

    private XmlTag                                          rsvData;
    private XmlTag                                          data;
    private XmlTag                                          errorCode;

    private ArrayList<GenerateXmlHapiTouchRsvDailyCountSub> dataList       = new ArrayList<GenerateXmlHapiTouchRsvDailyCountSub>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_RSV_DATA );
        for( int i = 0 ; i < dataList.size() ; i++ )
        {
            this.dataList.get( i ).setRootNode( root );
            this.dataList.get( i ).initXmlNodeInfo();
        }
        XmlTag.setParent( root, errorCode );

        return;
    }

    public void addData(GenerateXmlHapiTouchRsvDailyCountSub data)
    {
        this.dataList.add( data );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }
}
