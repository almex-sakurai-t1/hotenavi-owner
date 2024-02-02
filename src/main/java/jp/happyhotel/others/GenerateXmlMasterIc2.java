package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * IC�}�X�^���XML�쐬�N���X
 */
public class GenerateXmlMasterIc2 extends WebApiResultBase
{
    // �^�O��
    private static final String                  TAG_IC         = "ic2";
    private static final String                  TAG_IC_LOCALID = "localId";
    private static final String                  TAG_IC_ROUTE   = "route";

    private XmlTag                               ic;                                                         // IC�^�O
    private XmlTag                               icLocalId;                                                  // �n��ID
    private ArrayList<GenerateXmlMasterIc2Route> icRouteList    = new ArrayList<GenerateXmlMasterIc2Route>(); // IC�H���ڍ׃}�X�^

    @Override
    protected void initXmlNodeInfo()
    {
        ic = createRootChild( TAG_IC );
        XmlTag.setParent( ic, icLocalId );
        // IC�H���}�X�^��null�ȊO�ł���Βǉ�
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
