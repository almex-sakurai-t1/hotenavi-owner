package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ予約確定情報
 */
public class GenerateXmlHapiTouchGetRsvRoomNo extends WebApiResultBase
{
    // タグ名
    private static final String TAG_ROOM     = "Room";
    private static final String TAG_ROOMNO   = "RoomNo";
    private static final String TAG_ROOMNAME = "RoomName";

    private XmlTag              room;                     // 結果用タグ
    private XmlTag              roomNo;                   // 部屋番号
    private XmlTag              roomName;                 // 部屋名称

    @Override
    protected void initXmlNodeInfo()
    {
        room = createRootChild( TAG_ROOM );
        XmlTag.setParent( room, roomNo );
        XmlTag.setParent( room, roomName );

        return;
    }

    public void setRoom(int roomNo)
    {
        this.roomNo = XmlTag.createXmlTag( TAG_ROOMNO, roomNo );
    }

    public void setRoomName(String roomName)
    {
        this.roomName = XmlTag.createXmlTag( TAG_ROOMNAME, roomName );
    }

}
