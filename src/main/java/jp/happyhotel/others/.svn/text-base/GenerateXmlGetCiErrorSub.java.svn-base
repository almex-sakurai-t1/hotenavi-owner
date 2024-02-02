package jp.happyhotel.others;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XML PMS予約連携 予約プランデータ
 */
public class GenerateXmlGetCiErrorSub extends WebApiResultBase
{
    // タグ名
    private static final String TAG_CIDATA  = "CiData";
    private static final String TAG_FRONTIP = "FrontIp";
    private static final String TAG_HOTELID = "HotelId";
    private static final String TAG_CICODE  = "CiCode";
    private static final String TAG_CIDATE  = "CiDate";
    private static final String TAG_CITIME  = "CiTime";
    private static final String TAG_ROOM_NO = "RoomNo";
    private static final String TAG_RSV_NO  = "RsvNo";

    private XmlTag              ciData;
    private XmlTag              frontIp;
    private XmlTag              hotelId;
    private XmlTag              ciCode;
    private XmlTag              ciDate;
    private XmlTag              ciTime;
    private XmlTag              roomNo;
    private XmlTag              rsvNo;

    @Override
    protected void initXmlNodeInfo()
    {
        ciData = createRootChild( TAG_CIDATA );
        XmlTag.setParent( ciData, frontIp );
        XmlTag.setParent( ciData, hotelId );
        XmlTag.setParent( ciData, ciCode );
        XmlTag.setParent( ciData, ciDate );
        XmlTag.setParent( ciData, ciTime );
        XmlTag.setParent( ciData, roomNo );
        XmlTag.setParent( ciData, rsvNo );
        return;
    }

    public void setData(String ciData)
    {
        this.ciData = XmlTag.createXmlTag( TAG_CIDATA, ciData );
    }

    public void setFrontIp(String frontIp)
    {
        this.frontIp = XmlTag.createXmlTag( TAG_FRONTIP, frontIp );
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = XmlTag.createXmlTag( TAG_HOTELID, hotelId );
    }

    public void setCiCode(int ciCode)
    {
        this.ciCode = XmlTag.createXmlTag( TAG_CICODE, ciCode );
    }

    public void setCiDate(int ciDate)
    {
        this.ciDate = XmlTag.createXmlTag( TAG_CIDATE, ciDate );
    }

    public void setCiTime(int ciTime)
    {
        this.ciTime = XmlTag.createXmlTag( TAG_CITIME, ciTime );
    }

    public void setRoomNo(String roomNo)
    {
        this.roomNo = XmlTag.createXmlTag( TAG_ROOM_NO, roomNo );
    }

    public void setRsvNo(String rsvNo)
    {
        this.rsvNo = XmlTag.createXmlTag( TAG_RSV_NO, rsvNo );
    }

}
