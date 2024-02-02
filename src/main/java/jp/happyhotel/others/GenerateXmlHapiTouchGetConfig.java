package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/***
 * XMLハピタッチ情報
 */
public class GenerateXmlHapiTouchGetConfig extends WebApiResultBase
{
    // タグ名
    private static final String                           TAG_GET_CONFIG    = "GetConfig";
    private static final String                           TAG_ROOM          = "Room";
    private static final String                           TAG_AMOUNT_RATE   = "AmountRate";
    private static final String                           TAG_VISIT_RATE    = "VisitRate";
    private static final String                           TAG_IS_VALID_RSV  = "IsValidRsv";
    private static final String                           TAG_DEADLINE_TIME = "DeadlineTime";
    private static final String                           TAG_HOTEL_NAME    = "HotelName";
    private static final String                           TAG_IS_COUPON     = "IsCoupon";
    private static final String                           TAG_FRONT_IP      = "FrontIp";

    private XmlTag                                        room;
    private XmlTag                                        amontRate;
    private XmlTag                                        visitRate;
    private GenerateXmlHapiTouchGetConfigRoom             roomList          = new GenerateXmlHapiTouchGetConfigRoom();
    private ArrayList<GenerateXmlHapiTouchGetConfigPoint> pointList         = new ArrayList<GenerateXmlHapiTouchGetConfigPoint>();
    private XmlTag                                        isValidRsv;
    private XmlTag                                        deadlineTime;
    private XmlTag                                        hotelName;
    private XmlTag                                        isCoupon;
    private XmlTag                                        frontIp;

    @Override
    protected void initXmlNodeInfo()
    {
        setRootNode( TAG_GET_CONFIG );
        XmlTag.setParent( root, hotelName );

        if ( roomList != null )
        {
            this.roomList.setRootNode( root );
            this.roomList.initXmlNodeInfo();

        }
        if ( pointList != null )
        {
            for( int i = 0 ; i < pointList.size() ; i++ )
            {
                this.pointList.get( i ).setRootNode( root );
                this.pointList.get( i ).initXmlNodeInfo();
            }
        }

        XmlTag.setParent( root, isValidRsv );
        XmlTag.setParent( root, deadlineTime );
        XmlTag.setParent( root, isCoupon );
        XmlTag.setParent( root, frontIp );
        return;
    }

    public void setRoom(GenerateXmlHapiTouchGetConfigRoom room)
    {
        this.roomList = room;
    }

    public void setPoint(GenerateXmlHapiTouchGetConfigPoint point)
    {
        this.pointList.add( point );
        return;
    }

    public void setIsValidRsv(int rsv)
    {
        this.isValidRsv = XmlTag.createXmlTag( TAG_IS_VALID_RSV, rsv );
        return;
    }

    public void setDeadlineTime(int deadlinetime)
    {
        this.deadlineTime = XmlTag.createXmlTag( TAG_DEADLINE_TIME, deadlinetime );
        return;
    }

    public void setHotelName(String name)
    {
        this.hotelName = XmlTag.createXmlTag( TAG_HOTEL_NAME, name );
        return;
    }

    public void setIsCoupon(int coupon)
    {
        this.isCoupon = XmlTag.createXmlTag( TAG_IS_COUPON, coupon );
        return;
    }

    public void setFrontIp(String frontIp)
    {
        this.frontIp = XmlTag.createXmlTag( TAG_FRONT_IP, frontIp );
        return;
    }

}
