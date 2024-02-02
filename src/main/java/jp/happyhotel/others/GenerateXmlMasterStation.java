package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * �w�}�X�^���XML�쐬�N���X
 */
public class GenerateXmlMasterStation extends WebApiResultBase
{
    // �^�O��
    private static final String                      TAG_ST         = "st";
    private static final String                      TAG_ST_PREF_ID = "prefId";
    private static final String                      TAG_ST_ROUTE   = "route";

    private XmlTag                                   st;                                                             // �w�^�O
    private XmlTag                                   prefId;                                                         // �s���{��ID
    private XmlTag                                   route;                                                          // �w���[�g�^�O
    private ArrayList<GenerateXmlMasterStationRoute> stRouteList    = new ArrayList<GenerateXmlMasterStationRoute>(); // �w�H���ڍ׃}�X�^

    @Override
    protected void initXmlNodeInfo()
    {
        st = createRootChild( TAG_ST );
        XmlTag.setParent( st, prefId );
        XmlTag.setParent( st, route );
        // �w�H���}�X�^��null�ȊO�ł���Βǉ�
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
