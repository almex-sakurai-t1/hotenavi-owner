package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * ICマスタ情報XML作成クラス
 */
public class GenerateXmlMasterIc extends WebApiResultBase
{
    // タグ名
    private static final String                 TAG_IC        = "ic";
    private static final String                 TAG_IC_PREFID = "prefId";
    private static final String                 TAG_IC_ROUTE  = "route";

    private XmlTag                              ic;                                                       // ICタグ
    private XmlTag                              icPrefId;                                                 // 都道府県ID
    private ArrayList<GenerateXmlMasterIcRoute> icRouteList   = new ArrayList<GenerateXmlMasterIcRoute>(); // 駅路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        ic = createRootChild( TAG_IC );
        XmlTag.setParent( ic, icPrefId );
        // IC路線マスタがnull以外であれば追加
        if ( icRouteList != null )
        {
            for( int i = 0 ; i < icRouteList.size() ; i++ )
            {
                icRouteList.get( i ).setRootNode( ic );
                icRouteList.get( i ).initXmlNodeInfo();
            }
            return;
        }
    }

    public void setPrefId(int id)
    {
        icPrefId = XmlTag.createXmlTag( TAG_IC_PREFID, id );
        return;
    }

    public void setRoute(GenerateXmlMasterIcRoute icRoute)
    {
        icRouteList.add( icRoute );
        return;
    }

}
