package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;

/***
 * XML チェックインエラーデータ取得
 */
public class GenerateXmlGetCiError extends WebApiResultBase
{
    // タグ名
    private static final String                 TAG_GET_CI_ERROR = "GetCiError";

    private ArrayList<GenerateXmlGetCiErrorSub> dataList         = new ArrayList<GenerateXmlGetCiErrorSub>();

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_GET_CI_ERROR );
        for( int i = 0 ; i < dataList.size() ; i++ )
        {
            this.dataList.get( i ).setRootNode( root );
            this.dataList.get( i ).initXmlNodeInfo();
        }
        return;
    }

    public void addData(GenerateXmlGetCiErrorSub data)
    {
        this.dataList.add( data );
    }

}
