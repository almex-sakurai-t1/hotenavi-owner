package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * �}�C��XML�쐬�N���X
 */
public class GenerateXmlMile extends WebApiResultBase
{
    // �^�O��
    private static final String           TAG_MILE  = "mile";
    private static final String           TAG_COUNT = "count";
    private static final String           TAG_POINT = "point";

    private XmlTag                        mile;                                           // �}�C���^�u
    private XmlTag                        point;                                          // �|�C���g�^�u
    private XmlTag                        count;                                          // ����
    private ArrayList<GenerateXmlMileSub> mileSub   = new ArrayList<GenerateXmlMileSub>(); // �}�C���T�u

    @Override
    protected void initXmlNodeInfo()
    {
        mile = createRootChild( TAG_MILE );
        XmlTag.setParent( mile, count );
        XmlTag.setParent( mile, point );
        if ( mileSub != null )
        {
            for( int i = 0 ; i < mileSub.size() ; i++ )
            {
                mileSub.get( i ).setRootNode( mile );
                mileSub.get( i ).initXmlNodeInfo();
            }
        }
        return;
    }

    public void setCount(int cnt)
    {
        count = XmlTag.createXmlTag( TAG_COUNT, cnt );
        return;
    }

    public void setPoint(String pnt)
    {
        point = XmlTag.createXmlTag( TAG_POINT, pnt );
        return;
    }

    public void setMileSub(GenerateXmlMileSub sub)
    {
        mileSub.add( sub );
        return;
    }

}
