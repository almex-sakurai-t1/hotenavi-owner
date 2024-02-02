package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataMapRoute;

/***
 * �H���}�X�^���XML�쐬�N���X
 */
public class GenerateXmlMasterRouteStation extends WebApiResultBase
{
    // �^�O��
    private static final String                            TAG_ROUTEST         = "routeSt";
    private static final String                            TAG_ROUTEST_PREF_ID = "prefId";
    private static final String                            TAG_ROUTEST_DETAIL  = "detail";

    private XmlTag                                         routeSt;                                                                   // �w�H���^�O
    private XmlTag                                         prefId;                                                                    // �s���{��ID
    private ArrayList<GenerateXmlMasterRouteStationDetail> detailList          = new ArrayList<GenerateXmlMasterRouteStationDetail>(); // �w�H���ڍ׃}�X�^

    @Override
    protected void initXmlNodeInfo()
    {
        routeSt = createRootChild( TAG_ROUTEST );
        XmlTag.setParent( routeSt, prefId );
        // �w�H���}�X�^��null�ȊO�ł���Βǉ�
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
        // �ڍ׃f�[�^�ǉ�
        for( int i = 0 ; i < dmr.size() ; i++ )
        {
            GenerateXmlMasterRouteStationDetail addRouteStDetail = new GenerateXmlMasterRouteStationDetail();
            addRouteStDetail.setId( dmr.get( i ).getRouteId() );
            addRouteStDetail.setName( dmr.get( i ).getName() );
            this.setDetail( addRouteStDetail );
        }

    }
}
