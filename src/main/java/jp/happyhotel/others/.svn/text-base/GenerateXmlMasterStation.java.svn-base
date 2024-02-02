package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * 駅マスタ情報XML作成クラス
 */
public class GenerateXmlMasterStation extends WebApiResultBase
{
    // タグ名
    private static final String                      TAG_ST         = "st";
    private static final String                      TAG_ST_PREF_ID = "prefId";
    private static final String                      TAG_ST_ROUTE   = "route";

    private XmlTag                                   st;                                                             // 駅タグ
    private XmlTag                                   prefId;                                                         // 都道府県ID
    private XmlTag                                   route;                                                          // 駅ルートタグ
    private ArrayList<GenerateXmlMasterStationRoute> stRouteList    = new ArrayList<GenerateXmlMasterStationRoute>(); // 駅路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        st = createRootChild( TAG_ST );
        XmlTag.setParent( st, prefId );
        XmlTag.setParent( st, route );
        // 駅路線マスタがnull以外であれば追加
        if ( stRouteList != null )
        {
            for( int i = 0 ; i < stRouteList.size() ; i++ )
            {
                stRouteList.get( i ).setRootNode( st );
                stRouteList.get( i ).initXmlNodeInfo();
            }
        }
        return;
    }

    public void setPrefId(int id)
    {
        prefId = XmlTag.createXmlTag( TAG_ST_PREF_ID, id );
        return;
    }

    public void setRoute(GenerateXmlMasterStationRoute route)
    {
        stRouteList.add( route );
        return;
    }

}
