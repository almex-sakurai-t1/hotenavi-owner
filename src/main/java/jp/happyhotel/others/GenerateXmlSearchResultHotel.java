package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;
import jp.happyhotel.data.DataSearchHotel_M2;

// �������ʃz�e�����
public class GenerateXmlSearchResultHotel extends WebApiResultBase
{
    private static final String                                MEMBER_IMG_HAPPIE       = "�n�s�^�b�`";
    private static final String                                MEMBER_IMG_RSV          = "�\��";
    // �^�O��
    private static final String                                TAG_HOTEL               = "hotel";
    private static final String                                TAG_HOTEL_NAME          = "name";
    private static final String                                TAG_HOTEL_ID            = "id";
    private static final String                                TAG_HOTEL_RANK          = "rank";
    private static final String                                TAG_HOTEL_RSV_FLAG      = "rsvFlag";
    private static final String                                TAG_HOTEL_MEMBER_IMG    = "memberImg";
    private static final String                                TAG_HOTEL_EMPTY         = "empty";
    private static final String                                TAG_HOTEL_UPDATE        = "update";
    private static final String                                TAG_HOTEL_UPTIME        = "upTime";
    private static final String                                TAG_HOTEL_OVER18        = "over18";
    private static final String                                TAG_HOTEL_IMAGE         = "image";
    private static final String                                TAG_HOTEL_ADDRESS       = "address";
    private static final String                                TAG_HOTEL_TEL           = "tel";
    private static final String                                TAG_HOTEL_LAT           = "lat";
    private static final String                                TAG_HOTEL_LON           = "lon";
    private static final String                                TAG_HOTEL_DISTANCE      = "distance";
    private static final String                                TAG_HOTEL_KUCHIKOMIAVG  = "kuchikomiAvg";
    private static final String                                TAG_HOTEL_PR            = "pr";
    private static final String                                TAG_HOTEL_MESSAGE       = "message";
    private static final String                                TAG_HOTEL_HAPPIEPR      = "happiePr";
    private static final String                                TAG_HOTEL_RESERVE       = "reserve";
    private static final String                                TAG_HOTEL_RESERVE_COUNT = "count";
    private static final String                                TAG_HOTEL_CUSTOM_ID     = "customId";
    private static final String                                TAG_HOTEL_COUPON        = "coupon";

    // �z�e�������N��ʒl
    public static final int                                    HOTEL_RANK_NONE         = 0;                                                       // ���_��
    public static final int                                    HOTEL_RANK_LIGHT        = 1;                                                       // ���C�g
    public static final int                                    HOTEL_RANK_STANDARD     = 2;                                                       // �X�^���_�[�h
    public static final int                                    HOTEL_RANK_HAPPIE       = 3;                                                       // �n�s�[�����X
    public static final int                                    HOTEL_RANK_OTHER        = 9;                                                       // ���̑�

    // �󖞏�Ԓl
    public static final int                                    HOTEL_EMPTY_EMPTY       = 1;                                                       // ��
    public static final int                                    HOTEL_EMPTY_FULL        = 2;                                                       // ����

    // 18�փt���O
    public static final int                                    HOTEL_OVER18_NONE       = 0;                                                       // �ʏ�
    public static final int                                    HOTEL_OVER18_ABORT      = 1;                                                       // 18��

    private XmlTag                                             hotel;                                                                             // �z�e�����i�[�^�O
    private XmlTag                                             hotelName;                                                                         // �z�e����
    private XmlTag                                             hotelId;                                                                           // �z�e��ID
    private XmlTag                                             hotelRank;                                                                         // �z�e�������N
    private XmlTag                                             hotelRsvFlag;                                                                      // �\��Ή�
    private XmlTag                                             hotelMemberImg;                                                                    // �}�C�������X�摜
    private XmlTag                                             hotelEmpty;                                                                        // �󖞏��
    private XmlTag                                             hotelUpdate;                                                                       // �󖞏�Ԏ擾���t
    private XmlTag                                             hotelUpTime;                                                                       // �󖞏�Ԏ擾����
    private XmlTag                                             hotelOver18;                                                                       // 18�փt���O
    private XmlTag                                             hotelImage;                                                                        // �z�e���O�ω摜URL
    private XmlTag                                             hotelAddress;                                                                      // �Z��
    private XmlTag                                             hotelTel;                                                                          // �d�b�ԍ�
    private XmlTag                                             hotelLat;                                                                          // �ܓx
    private XmlTag                                             hotelLon;                                                                          // �o�x
    private XmlTag                                             hotelDistance;                                                                     // ����
    private XmlTag                                             hotelKuchikomiAvg;                                                                 // �N�`�R�~���ϓ_
    private XmlTag                                             hotelPr;                                                                           // �z�e���Љ
    private XmlTag                                             hotelMessage;                                                                      // �z�e���ŐV���
    private XmlTag                                             hotelHappiePr;                                                                     // �n�s�[PR��
    private XmlTag                                             hotelReserve;                                                                      // �\��i�[�^�O
    private XmlTag                                             hotelReserveCount;                                                                 // �\�񌏐�
    private ArrayList<GenerateXmlSearchResultHotelReservePlan> reservePlan             = new ArrayList<GenerateXmlSearchResultHotelReservePlan>(); // �\��v����
    private XmlTag                                             hotelCustomId;                                                                     // �ڋq�����o�[ID
    private XmlTag                                             hotelCoupon;                                                                       // �N�[�|��

    @Override
    protected void initXmlNodeInfo()
    {
        hotel = createRootChild( TAG_HOTEL );
        XmlTag.setParent( hotel, hotelName );
        XmlTag.setParent( hotel, hotelId );
        XmlTag.setParent( hotel, hotelRank );
        XmlTag.setParent( hotel, hotelRsvFlag );
        XmlTag.setParent( hotel, hotelMemberImg );
        XmlTag.setParent( hotel, hotelEmpty );
        XmlTag.setParent( hotel, hotelUpdate );
        XmlTag.setParent( hotel, hotelUpTime );
        XmlTag.setParent( hotel, hotelOver18 );
        XmlTag.setParent( hotel, hotelImage );
        XmlTag.setParent( hotel, hotelAddress );
        XmlTag.setParent( hotel, hotelTel );
        XmlTag.setParent( hotel, hotelLat );
        XmlTag.setParent( hotel, hotelLon );
        XmlTag.setParent( hotel, hotelDistance );
        XmlTag.setParent( hotel, hotelKuchikomiAvg );
        XmlTag.setParent( hotel, hotelPr );
        XmlTag.setParent( hotel, hotelMessage );
        XmlTag.setParent( hotel, hotelHappiePr );
        XmlTag.setParent( hotel, hotelCustomId );
        XmlTag.setParent( hotel, hotelCoupon );

        if ( hotelReserveCount != null )
        {
            hotelReserve = XmlTag.createXmlTag( TAG_HOTEL_RESERVE );

            XmlTag.setParent( hotel, hotelReserve );
            XmlTag.setParent( hotelReserve, hotelReserveCount );
        }

        if ( reservePlan != null )
        {
            for( int i = 0 ; i < reservePlan.size() ; i++ )
            {
                reservePlan.get( i ).setRootNode( hotelReserve );
                reservePlan.get( i ).initXmlNodeInfo();

            }
        }
        return;
    }

    public void setName(String name)
    {
        hotelName = XmlTag.createXmlTag( TAG_HOTEL_NAME, name );
        return;
    }

    public void setId(int id)
    {
        hotelId = XmlTag.createXmlTag( TAG_HOTEL_ID, id );
        return;
    }

    public void setRank(int rank)
    {
        hotelRank = XmlTag.createXmlTag( TAG_HOTEL_RANK, rank );
        return;
    }

    public void setRsvFlag(int rsvflag)
    {
        hotelRsvFlag = XmlTag.createXmlTag( TAG_HOTEL_RSV_FLAG, rsvflag );
        return;
    }

    public void setMemberImg(String memberImg)
    {
        hotelMemberImg = XmlTag.createXmlTag( TAG_HOTEL_MEMBER_IMG, memberImg );
        return;
    }

    public void setEmpty(int empty)
    {
        if ( empty == 2 )
        {
            empty = 0;
        }
        hotelEmpty = XmlTag.createXmlTag( TAG_HOTEL_EMPTY, empty );
        return;
    }

    public void setUpdate(int update)
    {
        hotelUpdate = XmlTag.createXmlTag( TAG_HOTEL_UPDATE, update );
        return;
    }

    public void setUpTime(String upTime)
    {
        hotelUpTime = XmlTag.createXmlTag( TAG_HOTEL_UPTIME, upTime );
        return;
    }

    public void setOver18(int over18)
    {
        hotelOver18 = XmlTag.createXmlTag( TAG_HOTEL_OVER18, over18 );
        return;
    }

    public void setImage(String image)
    {
        if ( image != null && image.indexOf( "http://" ) == -1 && image.indexOf( "https://" ) == -1 )
        {
            image = Url.getUrl() + image;
        }
        hotelImage = XmlTag.createXmlTag( TAG_HOTEL_IMAGE, image );
        return;
    }

    public void setAddress(String address)
    {
        hotelAddress = XmlTag.createXmlTag( TAG_HOTEL_ADDRESS, address );
        return;
    }

    public void setTel(String tel)
    {
        hotelTel = XmlTag.createXmlTag( TAG_HOTEL_TEL, tel );
        return;
    }

    public void setLat(String lat)
    {
        hotelLat = XmlTag.createXmlTag( TAG_HOTEL_LAT, lat );
        return;
    }

    public void setLon(String lon)
    {
        hotelLon = XmlTag.createXmlTag( TAG_HOTEL_LON, lon );
        return;
    }

    public void setDistance(int distance)
    {
        hotelDistance = XmlTag.createXmlTag( TAG_HOTEL_DISTANCE, distance );
        return;
    }

    public void setKuchikomiAvg(float avg)
    {
        hotelKuchikomiAvg = XmlTag.createXmlTag( TAG_HOTEL_KUCHIKOMIAVG, Float.toString( avg ) );
        return;
    }

    public void setPr(String pr)
    {
        hotelPr = XmlTag.createXmlTag( TAG_HOTEL_PR, pr );
        return;
    }

    public void setMessage(String message)
    {
        hotelMessage = XmlTag.createXmlTag( TAG_HOTEL_MESSAGE, message );
        return;
    }

    public void setHappiePr(String happiePr)
    {
        hotelHappiePr = XmlTag.createXmlTag( TAG_HOTEL_HAPPIEPR, happiePr );
        return;
    }

    public void setReserveCount(int count)
    {
        hotelReserveCount = XmlTag.createXmlTag( TAG_HOTEL_RESERVE_COUNT, count );
        return;
    }

    public void addPlan(GenerateXmlSearchResultHotelReservePlan add)
    {
        reservePlan.add( add );
        return;
    }

    public void setCustomId(String customId)
    {
        hotelCustomId = XmlTag.createXmlTag( TAG_HOTEL_CUSTOM_ID, customId );
    }

    public void setCoupon(int coupon)
    {
        hotelCoupon = XmlTag.createXmlTag( TAG_HOTEL_COUPON, coupon );
    }

    /***
     * �z�e�����ǉ�
     * 
     * @param dsh �z�e�����
     * @param paymemberFlag �L������t���O
     * @param distance ����
     */
    public void addHotelInfo(DataSearchHotel_M2 dsh, boolean paymemberFlag, int distance)
    {
        addHotelInfoExpand( dsh, paymemberFlag, distance, "" );
    }

    /***
     * �z�e�����ǉ�
     * 
     * @param dsh �z�e�����
     * @param paymemberFlag �L������t���O
     * @param distance ����
     */
    public void addMyHotelInfo(DataSearchHotel_M2 dsh, boolean paymemberFlag, int distance, String customId)
    {
        addHotelInfoExpand( dsh, paymemberFlag, distance, customId );
    }

    /***
     * �z�e�����ǉ�
     * 
     * @param dsh �z�e�����
     * @param paymemberFlag �L������t���O
     * @param distance ����
     */
    public void addHotelInfoExpand(DataSearchHotel_M2 dsh, boolean paymemberFlag, int distance, String customId)
    {
        this.setName( ReplaceString.replaceApiSpecial( dsh.getName() ) );
        this.setId( dsh.getId() );
        this.setRank( dsh.getRank() );

        // �󖞏��͗L������̂�
        if ( paymemberFlag != false )
        {
            // �����͔�\�������̂��߁A�󎺂̂ݕ\������
            if ( dsh.getEmptyStatus() == 1 )
            {
                this.setEmpty( dsh.getEmptyStatus() );
                this.setUpdate( dsh.getLastUpDate() );
                this.setUpTime( String.format( "%1$04d", dsh.getLastUpTime() / 100 ) );
            }
        }
        // 18�֕\���͔�����X�������ʂŃZ�b�g���Ȃ�
        if ( dsh.getRank() >= 1 )
        {
            this.setOver18( dsh.getOver18Flag() );
        }
        if ( dsh.getRank() >= 2 )
        {
            this.setImage( dsh.getImagePath() );
        }
        this.setAddress( dsh.getAddressAll() );
        this.setTel( dsh.getTel1() );
        this.setLat( dsh.getLat() );
        this.setLon( dsh.getLon() );
        this.setDistance( distance );
        if ( dsh.getRank() >= 2 )
        {
            if ( dsh.getBbsConfig() == 1 )
            {
                if ( dsh.getStars() > 0 )
                {
                    this.setKuchikomiAvg( dsh.getStars() );
                }
            }
        }
        // ���C�g�R�[�X�ȏ�ŕ\������B
        if ( dsh.getRank() > 0 )
        {
            this.setPr( ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( dsh.getPr() ) ) );
            this.setMessage( ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( dsh.getHotelMessage() ) ) );
        }
        // TODO �n�s�[PR
        if ( dsh.getRank() >= 3 )
        {
            if ( dsh.getHappieName() != null )
            {
                this.setHappiePr( ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( dsh.getHappieName() ) ) );
            }
            this.setMemberImg( MEMBER_IMG_HAPPIE );
        }

        // TODO �\��
        this.setRsvFlag( dsh.getReserveSalesFlag() );
        if ( dsh.getRank() >= 3 )
        {
            this.setReserveCount( dsh.getReservePlanCount() );
            if ( dsh.getReservePlanCount() > 0 )
            {
                for( int i = 0 ; i < dsh.getReservePlanCount() ; i++ )
                {
                    GenerateXmlSearchResultHotelReservePlan addReservePlan = new GenerateXmlSearchResultHotelReservePlan();
                    addReservePlan.setNo( dsh.getReservePlanId()[i] );
                    addReservePlan.setName( ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( dsh.getReservePlan()[i] ) ) );
                    // �z�e�����m�[�h�ɗ\��m�[�h��ǉ�
                    this.addPlan( addReservePlan );
                }
                this.setMemberImg( MEMBER_IMG_RSV );
            }
        }
        // �ڋqID�֘A
        if ( customId.equals( "" ) == false )
        {
            this.setCustomId( customId );
        }
        // �N�[�|���L
        if ( dsh.getCouponCount() > 0 )
        {
            this.setCoupon( 1 );
        }
        else
        {
            this.setCoupon( 0 );
        }

    }

}
