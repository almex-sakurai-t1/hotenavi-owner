package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchGetConfigRoom extends WebApiResultBase
{
    // タグ名
    private static final String                            TAG_GET_CONFIG = "GetConfig";
    private static final String                            TAG_ROOM       = "Room";
    private static final String                            TAG_ROOMNO     = "RoomNo";

    private XmlTag                                         room;
    private XmlTag                                         roomNo;
    private ArrayList<GenerateXmlHapiTouchGetConfigRoomNo> roomNoList     = new ArrayList<GenerateXmlHapiTouchGetConfigRoomNo>();

    @Override
    protected void initXmlNodeInfo()
    {
        room = createRootChild( TAG_ROOM );
        if ( roomNoList != null )
        {
            for( int i = 0 ; i < roomNoList.size() ; i++ )
            {
                this.roomNoList.get( i ).setRootNode( room );
                this.roomNoList.get( i ).initXmlNodeInfo();
            }
        }
        return;
    }

    public void setRoomNo(GenerateXmlHapiTouchGetConfigRoomNo roomNo)
    {
        this.roomNoList.add( roomNo );
        return;
    }
}
