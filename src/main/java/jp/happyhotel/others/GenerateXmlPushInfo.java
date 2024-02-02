package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * PUSH�ʒm�ꗗXML�쐬�N���X
 */
public class GenerateXmlPushInfo extends WebApiResultBase
{
    // �^�O��
    private static final String               TAG_PUSH_INFO = "pushInfo";

    private XmlTag                            pushInfo;                                               //
    private ArrayList<GenerateXmlPushInfoSub> pushSub       = new ArrayList<GenerateXmlPushInfoSub>(); // �}�C���T�u

    @Override
    protected void initXmlNodeInfo()
    {
        pushInfo = createRootChild( TAG_PUSH_INFO );
        if ( pushSub != null )
        {
            for( int i = 0 ; i < pushSub.size() ; i++ )
            {
                pushSub.get( i ).setRootNode( pushInfo );
                pushSub.get( i ).initXmlNodeInfo();
            }
        }

    }

    public void setPushInfoSub(GenerateXmlPushInfoSub sub)
    {
        pushSub.add( sub );
        return;
    }

}
