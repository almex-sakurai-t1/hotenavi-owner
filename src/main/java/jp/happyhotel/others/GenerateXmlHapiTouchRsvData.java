package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約データ取得
 */
public class GenerateXmlHapiTouchRsvData extends WebApiResultBase
{
    // タグ名
    private static final String                       TAG_RSV_DATA   = "RsvData";
    private static final String                       TAG_DATA       = "Data";
    private static final String                       TAG_ERROR_CODE = "ErrorCode";

    private XmlTag                                    rsvData;
    private XmlTag                                    data;
    private XmlTag                                    errorCode;

    private ArrayList<GenerateXmlHapiTouchRsvDataSub> dataList       = new ArrayList<GenerateXmlHapiTouchRsvDataSub>();

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

    public void addData(GenerateXmlHapiTouchRsvDataSub data)
    {
        this.dataList.add( data );
    }

    public void setErrorCode(int errCode)
    {
        this.errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, errCode );
    }
}
