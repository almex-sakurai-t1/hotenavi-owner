package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML PMS予約連携プランデータ 部屋別料金
 */
public class GenerateXmlRsvPlanDataRooms extends WebApiResultBase
{
    // タグ名
    private static final String TAG_ROOMS       = "Rooms";
    private static final String TAG_ROOM_NO     = "RoomNo";
    private static final String TAG_ROOM_NAME   = "RoomName";
    private static final String TAG_CHARGE_TEXT = "ChargeText";
    private static final String TAG_CHARGE      = "Charge";

    private XmlTag              rooms;
    private XmlTag              roomNo;
    private XmlTag              roomName;
    private XmlTag              chargeText;
    private XmlTag              charge;

    @Override
    protected void initXmlNodeInfo()
    {
        rooms = createRootChild( TAG_ROOMS );
        XmlTag.setParent( rooms, roomNo );
        XmlTag.setParent( rooms, roomName );
        XmlTag.setParent( rooms, chargeText );
        XmlTag.setParent( rooms, charge );

        return;
    }

    public void setRoomNo(String roomNo)
    {
        this.roomNo = XmlTag.createXmlTag( TAG_ROOM_NO, roomNo );
    }

    public void setRoomName(String roomName)
    {
        this.roomName = XmlTag.createXmlTag( TAG_ROOM_NAME, roomName );
    }

    public void setChargeText(String chargeText)
    {
        this.chargeText = XmlTag.createXmlTag( TAG_CHARGE_TEXT, chargeText );
    }

    public void setCharge(String charge)
    {
        this.charge = XmlTag.createXmlTag( TAG_CHARGE, charge );
    }
}
