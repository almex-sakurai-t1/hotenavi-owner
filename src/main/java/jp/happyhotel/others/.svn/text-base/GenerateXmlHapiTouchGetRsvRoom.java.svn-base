package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約確定情報
 */
public class GenerateXmlHapiTouchGetRsvRoom extends WebApiResultBase
{
    // タグ名
    private static final String                         TAG_GET_RSV_ROOM = "GetRsvRoom";
    private static final String                         TAG_ROOM         = "Room";
    private static final String                         TAG_ROOMNO       = "RoomNo";
    private static final String                         TAG_RESERVE_ROOM = "ReserveRoom";

    private XmlTag                                      room;
    private ArrayList<GenerateXmlHapiTouchGetRsvRoomNo> roomNoList       = new ArrayList<GenerateXmlHapiTouchGetRsvRoomNo>();
    private XmlTag                                      reserveRoom;

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_GET_RSV_ROOM );
        XmlTag.setParent( root, room );
        XmlTag.setParent( root, reserveRoom );
        for( int i = 0 ; i < roomNoList.size() ; i++ )
        {
            this.roomNoList.get( i ).setRootNode( root );
            this.roomNoList.get( i ).initXmlNodeInfo();
        }

        return;
    }

    public void setRoom(int room)
    {
        this.room = XmlTag.createXmlTag( TAG_ROOMNO, room );
    }

    public void setReserveRoom(int rsvRoom)
    {
        this.reserveRoom = XmlTag.createXmlTag( TAG_RESERVE_ROOM, rsvRoom );
    }

    public void setData(GenerateXmlHapiTouchGetRsvRoomNo roomNo)
    {
        this.roomNoList.add( roomNo );
    }

    public void setErrorCode(int room)
    {
        this.room = XmlTag.createXmlTag( TAG_ROOMNO, room );
    }

}
