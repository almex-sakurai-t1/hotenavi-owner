package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * IC�}�X�^���XML�쐬�N���X
 */
public class GenerateXmlMasterIc extends WebApiResultBase
{
    // �^�O��
    private static final String                 TAG_IC        = "ic";
    private static final String                 TAG_IC_PREFID = "prefId";
    private static final String                 TAG_IC_ROUTE  = "route";

    private XmlTag                              ic;                                                       // IC�^�O
    private XmlTag                              icPrefId;                                                 // �s���{��ID
    private ArrayList<GenerateXmlMasterIcRoute> icRouteList   = new ArrayList<GenerateXmlMasterIcRoute>(); // �w�H���ڍ׃}�X�^

    @Override
    protected void initXmlNodeInfo()
    {
        ic = createRootChild( TAG_IC );
        XmlTag.setParent( ic, icPrefId );
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

    public void setPrefId(int id)
    {
        icPrefId = XmlTag.createXmlTag( TAG_IC_PREFID, id );
        return;
    }

    public void setRoute(GenerateXmlMasterIcRoute icRoute)
    {
        icRouteList.add( icRoute );
        return;
    }

}
