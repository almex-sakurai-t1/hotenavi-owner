package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchSyncCi extends WebApiResultBase
{
    // タグ名
    private static final String                       TAG_SYNC_CI = "SyncCi";

    private String                                    tagName;

    private ArrayList<GenerateXmlHapiTouchSyncCiData> ciDataList  = new ArrayList<GenerateXmlHapiTouchSyncCiData>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_SYNC_CI );
        if ( ciDataList != null )
        {
            for( int i = 0 ; i < ciDataList.size() ; i++ )
            {
                this.ciDataList.get( i ).setRootNode( root );
                this.ciDataList.get( i ).initXmlNodeInfo();
            }
        }

        return;
    }

    public void setCiData(GenerateXmlHapiTouchSyncCiData ciData)
    {
        this.ciDataList.add( ciData );
        return;
    }
}
