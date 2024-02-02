package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMapPoint;

/***
 * ICルートマスタ情報XML作成クラス
 */
public class GenerateXmlMasterIcRoute extends WebApiResultBase
{
    // タグ名
    private static final String                       TAG_IC_ROUTE    = "route";
    private static final String                       TAG_IC_ROUTE_ID = "routeId";
    private static final String                       TAG_IC_DETAIL   = "route";

    private XmlTag                                    route;                                                            // ICルートタグ
    private XmlTag                                    routeId;                                                          // ICルートID
    private XmlTag                                    detail;                                                           // IC詳細タグ
    private ArrayList<GenerateXmlMasterIcRouteDetail> icRouteDetail   = new ArrayList<GenerateXmlMasterIcRouteDetail>(); // 駅路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        route = createRootChild( TAG_IC_ROUTE );
        XmlTag.setParent( route, routeId );
        // XmlTag.setParent( route, detail );
        // ICマスタがnull以外であれば追加
        if ( icRouteDetail != null )
        {
            for( int i = 0 ; i < icRouteDetail.size() ; i++ )
            {
                icRouteDetail.get( i ).setRootNode( route );
                icRouteDetail.get( i ).initXmlNodeInfo();
            }
            return;
        }

    }

    public void setRouteId(String id)
    {
        routeId = XmlTag.createXmlTag( TAG_IC_ROUTE_ID, id );
        return;
    }

    public void setDetail(String message)
    {
        detail = XmlTag.createXmlTag( TAG_IC_DETAIL, message );
        return;
    }

    public void setDetail(GenerateXmlMasterIcRouteDetail detail)
    {
        icRouteDetail.add( detail );
        return;
    }

    public void addRouteIcInfo(ArrayList<DataMapPoint> dmp)
    {
        // 詳細データ追加
        for( int i = 0 ; i < dmp.size() ; i++ )
        {
            GenerateXmlMasterIcRouteDetail addRouteStDetail = new GenerateXmlMasterIcRouteDetail();
            addRouteStDetail.setId( dmp.get( i ).getId() );
            addRouteStDetail.setName( dmp.get( i ).getName() );
            this.setDetail( addRouteStDetail );
        }

    }

}
