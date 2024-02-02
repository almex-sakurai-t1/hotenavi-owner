package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * PUSH通知一覧XML作成クラス
 */
public class GenerateXmlRsvData extends WebApiResultBase
{
    // タグ名
    private static final String              TAG_RSV_DATA      = "RsvData";
    private static final String              TAG_ERROR_CODE    = "errorCode";
    private static final String              TAG_ERROR_MESSAGE = "errorMessage";
    private static final String              TAG_RESULT_COUNT  = "ResultCount";

    private XmlTag                           errorCode;
    private XmlTag                           errorMessage;
    private XmlTag                           resultCount;
    private XmlTag                           rsvData;                                                   //
    private ArrayList<GenerateXmlRsvDataSub> rsvDataSub        = new ArrayList<GenerateXmlRsvDataSub>(); // 予約サブ

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, errorCode );
        XmlTag.setParent( root, errorMessage );
        XmlTag.setParent( root, resultCount );

        rsvData = createRootChild( TAG_RSV_DATA );
        if ( rsvDataSub != null )
        {
            for( int i = 0 ; i < rsvDataSub.size() ; i++ )
            {
                rsvDataSub.get( i ).setRootNode( root );
                rsvDataSub.get( i ).initXmlNodeInfo();
            }
        }

    }

    public void setErrorCode(int code)
    {
        errorCode = XmlTag.createXmlTag( TAG_ERROR_CODE, code );
        return;
    }

    public void setError(String message)
    {
        errorMessage = XmlTag.createXmlTag( TAG_ERROR_MESSAGE, message );
        return;
    }

    public void setResultCount(int count)
    {
        resultCount = XmlTag.createXmlTag( TAG_RESULT_COUNT, count );
        return;
    }

    public void setRsvDataSub(GenerateXmlRsvDataSub sub)
    {
        rsvDataSub.add( sub );
        return;
    }

}
