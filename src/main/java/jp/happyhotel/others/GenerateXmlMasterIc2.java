package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * ICマスタ情報XML作成クラス
 */
public class GenerateXmlMasterIc2 extends WebApiResultBase
{
    // タグ名
    private static final String                  TAG_IC         = "ic2";
    private static final String                  TAG_IC_LOCALID = "localId";
    private static final String                  TAG_IC_ROUTE   = "route";

    private XmlTag                               ic;                                                         // ICタグ
    private XmlTag                               icLocalId;                                                  // 地方ID
    private ArrayList<GenerateXmlMasterIc2Route> icRouteList    = new ArrayList<GenerateXmlMasterIc2Route>(); // IC路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        ic = createRootChild( TAG_IC );
        XmlTag.setParent( ic, icLocalId );
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

    public void setLocalId(int localid)
    {
        icLocalId = XmlTag.createXmlTag( TAG_IC_LOCALID, localid );
        return;
    }

    public void setRoute(GenerateXmlMasterIc2Route icRoute)
    {
        icRouteList.add( icRoute );
        return;
    }

}
