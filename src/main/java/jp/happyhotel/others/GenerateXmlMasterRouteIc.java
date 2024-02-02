package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMapRoute;

/***
 * 高速道路マスタ情報XML作成クラス
 */
public class GenerateXmlMasterRouteIc extends WebApiResultBase
{
    // タグ名
    private static final String                       TAG_ROUTE_IC     = "routeIc";
    private static final String                       TAG_ROUTE_PREFID = "prefId";
    private static final String                       TAG_ROUTE_DETAIL = "detail";

    private XmlTag                                    routeIc;                                                           // ICルートタグ
    private XmlTag                                    routePrefId;                                                       // 都道府県ID
    private ArrayList<GenerateXmlMasterRouteIcDetail> detailList       = new ArrayList<GenerateXmlMasterRouteIcDetail>(); // 駅路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        routeIc = createRootChild( TAG_ROUTE_IC );
        XmlTag.setParent( routeIc, routePrefId );
        // IC路線マスタがnull以外であれば追加
        if ( detailList != null )
        {
            for( int i = 0 ; i < detailList.size() ; i++ )
            {
                detailList.get( i ).setRootNode( routeIc );
                detailList.get( i ).initXmlNodeInfo();
            }

        }
    }

    public void setPrefId(int id)
    {
        routePrefId = XmlTag.createXmlTag( TAG_ROUTE_PREFID, id );
        return;
    }

    public void setDetail(GenerateXmlMasterRouteIcDetail detail)
    {
        detailList.add( detail );
        return;
    }

    public void addRouteIcInfo(ArrayList<DataMapRoute> dmr, int prefId)
    {
        setPrefId( prefId );
        // 詳細データ追加
        for( int i = 0 ; i < dmr.size() ; i++ )
        {
            GenerateXmlMasterRouteIcDetail addRouteIcDetail = new GenerateXmlMasterRouteIcDetail();
            addRouteIcDetail.setId( dmr.get( i ).getRouteId() );
            addRouteIcDetail.setName( dmr.get( i ).getName() );
            this.setDetail( addRouteIcDetail );
        }
    }

}
