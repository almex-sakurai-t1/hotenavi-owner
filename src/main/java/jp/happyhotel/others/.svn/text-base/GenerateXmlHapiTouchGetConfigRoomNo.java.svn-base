package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchGetConfigRoomNo extends WebApiResultBase
{
    // タグ名
    private static final String TAG_ROOM_NO = "RoomNo";

    private XmlTag              roomNo;

    @Override
    protected void initXmlNodeInfo()
    {
        XmlTag.setParent( root, roomNo );
        return;
    }

    public void setRoomNo(String roomno)
    {
        this.roomNo = XmlTag.createXmlTag( TAG_ROOM_NO, roomno );
    }

}
