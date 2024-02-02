package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMapPoint;

/***
 * 駅ルートマスタ情報XML作成クラス
 */
public class GenerateXmlMasterStationRoute extends WebApiResultBase
{
    // タグ名
    private static final String                            TAG_ST_ROUTE        = "route";
    private static final String                            TAG_ST_ROUTE_ID     = "routeId";
    private static final String                            TAG_ST_ROUTE_DETAIL = "detail";

    private XmlTag                                         stRoute;                                                                   // 駅タグ
    private XmlTag                                         stRoutePrefId;                                                             // 都道府県ID
    private XmlTag                                         stRouteDetail;                                                             // 駅詳細タグ
    private ArrayList<GenerateXmlMasterStationRouteDetail> stDetailList        = new ArrayList<GenerateXmlMasterStationRouteDetail>(); // 駅路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        stRoute = createRootChild( TAG_ST_ROUTE );
        XmlTag.setParent( stRoute, stRoutePrefId );
        // 駅路線マスタがnull以外であれば追加
        if ( stDetailList != null )
        {
            for( int i = 0 ; i < stDetailList.size() ; i++ )
            {
                stDetailList.get( i ).setRootNode( stRoute );
                stDetailList.get( i ).initXmlNodeInfo();
            }
        }
        return;
    }

    public void setRouteId(String routeId)
    {
        stRoutePrefId = XmlTag.createXmlTag( TAG_ST_ROUTE_ID, routeId );
        return;
    }

    public void setDetail(String message)
    {
        stRouteDetail = XmlTag.createXmlTag( TAG_ST_ROUTE_DETAIL, message );
        return;
    }

    public void setDetail(GenerateXmlMasterStationRouteDetail detail)
    {
        stDetailList.add( detail );
        return;
    }

    public void addRouteStationInfo(ArrayList<DataMapPoint> dmp)
    {
        // 詳細データ追加
        for( int i = 0 ; i < dmp.size() ; i++ )
        {
            GenerateXmlMasterStationRouteDetail addRouteStDetail = new GenerateXmlMasterStationRouteDetail();
            addRouteStDetail.setId( dmp.get( i ).getId() );
            addRouteStDetail.setName( dmp.get( i ).getName() );
            this.setDetail( addRouteStDetail );
        }

    }

}
