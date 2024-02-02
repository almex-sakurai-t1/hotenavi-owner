package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMapRoute;

/***
 * 路線マスタ情報XML作成クラス
 */
public class GenerateXmlMasterRouteStation extends WebApiResultBase
{
    // タグ名
    private static final String                            TAG_ROUTEST         = "routeSt";
    private static final String                            TAG_ROUTEST_PREF_ID = "prefId";
    private static final String                            TAG_ROUTEST_DETAIL  = "detail";

    private XmlTag                                         routeSt;                                                                   // 駅路線タグ
    private XmlTag                                         prefId;                                                                    // 都道府県ID
    private ArrayList<GenerateXmlMasterRouteStationDetail> detailList          = new ArrayList<GenerateXmlMasterRouteStationDetail>(); // 駅路線詳細マスタ

    @Override
    protected void initXmlNodeInfo()
    {
        routeSt = createRootChild( TAG_ROUTEST );
        XmlTag.setParent( routeSt, prefId );
        // 駅路線マスタがnull以外であれば追加
        if ( detailList != null )
        {
            for( int i = 0 ; i < detailList.size() ; i++ )
            {
                detailList.get( i ).setRootNode( routeSt );
                detailList.get( i ).initXmlNodeInfo();
            }
        }
        return;
    }

    public void setPrefId(int id)
    {
        prefId = XmlTag.createXmlTag( TAG_ROUTEST_PREF_ID, id );
        return;
    }

    public void setDetail(GenerateXmlMasterRouteStationDetail detail)
    {
        detailList.add( detail );
        return;
    }

    public void addRouteStationInfo(ArrayList<DataMapRoute> dmr, int prefId)
    {
        setPrefId( prefId );
        // 詳細データ追加
        for( int i = 0 ; i < dmr.size() ; i++ )
        {
            GenerateXmlMasterRouteStationDetail addRouteStDetail = new GenerateXmlMasterRouteStationDetail();
            addRouteStDetail.setId( dmr.get( i ).getRouteId() );
            addRouteStDetail.setName( dmr.get( i ).getName() );
            this.setDetail( addRouteStDetail );
        }

    }
}
