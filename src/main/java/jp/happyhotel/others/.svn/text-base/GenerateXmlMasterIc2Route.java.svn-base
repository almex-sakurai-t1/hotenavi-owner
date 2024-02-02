package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMapPoint;

/***
 * ICルートマスタ情報XML作成クラス
 */
public class GenerateXmlMasterIc2Route extends WebApiResultBase
{
    // タグ名
    private static final String                        TAG_IC_ROUTE      = "route";
    private static final String                        TAG_IC_ROUTE_ID   = "routeId";
    private static final String                        TAG_IC_ROUTE_NAME = "routeName";
    private static final String                        TAG_IC_DETAIL     = "route";

    private XmlTag                                     route;                                                               // ICルートタグ
    private XmlTag                                     routeId;                                                             // ICルートID
    private XmlTag                                     routeName;                                                           // ICルート名
    private XmlTag                                     detail;                                                              // IC詳細タグ
    private ArrayList<GenerateXmlMasterIc2RouteDetail> icRouteDetail     = new ArrayList<GenerateXmlMasterIc2RouteDetail>(); // IC路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        route = createRootChild( TAG_IC_ROUTE );
        XmlTag.setParent( route, routeId );
        XmlTag.setParent( route, routeName );
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

    public void setRouteName(String name)
    {
        routeName = XmlTag.createXmlTag( TAG_IC_ROUTE_NAME, name );
        return;
    }

    public void setDetail(String message)
    {
        detail = XmlTag.createXmlTag( TAG_IC_DETAIL, message );
        return;
    }

    public void setDetail(GenerateXmlMasterIc2RouteDetail detail)
    {
        icRouteDetail.add( detail );
        return;
    }

    public void addRouteIcInfo(ArrayList<DataMapPoint> dmp, ArrayList<Integer> hotelCount)
    {
        // 詳細データ追加
        for( int i = 0 ; i < dmp.size() ; i++ )
        {
            GenerateXmlMasterIc2RouteDetail addRouteIcDetail = new GenerateXmlMasterIc2RouteDetail();
            addRouteIcDetail.setId( dmp.get( i ).getOption4() );
            addRouteIcDetail.setName( dmp.get( i ).getName() );
            addRouteIcDetail.setCount( hotelCount.get( i ) );
            this.setDetail( addRouteIcDetail );
        }

    }
}
