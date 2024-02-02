package jp.happyhotel.others;

import java.util.ArrayList;

import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.common.Url;
import jp.happyhotel.common.WebApiResultBase;
import jp.happyhotel.common.XmlTag;

/**
 * �z�e���ڍ׏��xml�����N���X
 * 
 * @author N.Ide
 * @version 1.0 2011/04/27
 */

// �z�e���ڍ׏��
public class GenerateXmlDetailHotel extends WebApiResultBase
{
    // �^�O��
    private static final String                              TAG_HOTEL                         = "hotel";
    private static final String                              TAG_HOTEL_NAME                    = "name";
    private static final String                              TAG_HOTEL_ID                      = "id";
    private static final String                              TAG_HOTEL_RANK                    = "rank";
    private static final String                              TAG_HOTEL_RSV_FLAG                = "rsvFlag";
    private static final String                              TAG_HOTEL_MEMBER_IMG              = "memberImg";
    private static final String                              TAG_HOTEL_MEMBER_URL              = "memberUrl";
    private static final String                              TAG_HOTEL_MEMBER_TEXT             = "memberText";
    private static final String                              TAG_HOTEL_OVER18                  = "over18";
    private static final String                              TAG_HOTEL_IMAGE                   = "image";
    private static final String                              TAG_HOTEL_MESSAGE                 = "message";
    private static final String                              TAG_HOTEL_PR                      = "pr";
    private static final String                              TAG_HOTEL_HAPPIEPR                = "happiePr";
    private static final String                              TAG_HOTEL_KUCHIKOMIAVG            = "kuchikomiAvg";
    private static final String                              TAG_HOTEL_ADDRESS                 = "address";
    private static final String                              TAG_HOTEL_TEL                     = "tel";
    private static final String                              TAG_HOTEL_MAPCODE                 = "mapcode";
    private static final String                              TAG_HOTEL_EMPTY                   = "empty";
    private static final String                              TAG_HOTEL_EMPTY_STATUS            = "status";
    private static final String                              TAG_HOTEL_EMPTY_ROOM              = "room";
    private static final String                              TAG_HOTEL_EMPTY_UPDATE            = "upDate";
    private static final String                              TAG_HOTEL_EMPTY_UPTIME            = "upTime";
    private static final String                              TAG_HOTEL_EMPTY_NOMEMBER_TEXT     = "nomemberText";
    private static final String                              TAG_HOTEL_EMPTY_NOMEMBER_URL      = "nomemberUrl";
    private static final String                              TAG_HOTEL_ROOMALLCOUNT            = "roomAllCount";
    private static final String                              TAG_HOTEL_ROOM                    = "room";
    private static final String                              TAG_HOTEL_ROOM_COUNT              = "count";
    private static final String                              TAG_HOTEL_GALLERY                 = "gallery";
    private static final String                              TAG_HOTEL_PARKING                 = "parking";
    private static final String                              TAG_HOTEL_BLDGTYPE                = "bldgType";
    private static final String                              TAG_HOTEL_ACCESS                  = "access";
    private static final String                              TAG_HOTEL_MAP                     = "map";
    private static final String                              TAG_HOTEL_MAP_LAT                 = "lat";
    private static final String                              TAG_HOTEL_MAP_LON                 = "lon";
    private static final String                              TAG_HOTEL_PRICES                  = "hotelPrices";
    private static final String                              TAG_HOTEL_PRIVILEGES              = "privilege";
    private static final String                              TAG_HOTEL_ROOMSERVICE             = "roomservice";
    private static final String                              TAG_HOTEL_OTHERSERVICE            = "otherservice";
    private static final String                              TAG_HOTEL_CREDIT                  = "credit";
    private static final String                              TAG_HOTEL_HALFWAY                 = "halfway";
    private static final String                              TAG_HOTEL_HEADCOUNT               = "headcount";
    private static final String                              TAG_HOTEL_RESERVE                 = "reserve";
    private static final String                              TAG_HOTEL_EQUIPS                  = "hotelEquips";
    private static final String                              TAG_HOTEL_COUPON                  = "coupon";
    private static final String                              TAG_HOTEL_COUPON_IMAGE            = "image";
    private static final String                              TAG_HOTEL_COUPON_NO               = "no";
    private static final String                              TAG_HOTEL_COUPON_COMMONCONDITION  = "commonCondition";
    private static final String                              TAG_HOTEL_COUPON_PERIOD           = "period";
    private static final String                              TAG_HOTEL_COUPON_ISSUANCE         = "issuance";
    private static final String                              TAG_HOTEL_COUPON_MOBILE_CONDITION = "mobile_condition";
    private static final String                              TAG_HOTEL_CUSTOM_ID               = "customId";

    // �z�e�������N��ʒl
    public static final int                                  HOTEL_RANK_NONE                   = 0;                                                     // ���_��
    public static final int                                  HOTEL_RANK_LIGHT                  = 1;                                                     // ���C�g
    public static final int                                  HOTEL_RANK_STANDARD               = 2;                                                     // �X�^���_�[�h
    public static final int                                  HOTEL_RANK_HAPPIE                 = 3;                                                     // �n�s�[�����X
    public static final int                                  HOTEL_RANK_OTHER                  = 9;                                                     // ���̑�

    // �󖞏�Ԓl
    public static final int                                  HOTEL_EMPTY_EMPTY                 = 1;                                                     // ��
    public static final int                                  HOTEL_EMPTY_FULL                  = 2;                                                     // ����

    // 18�փt���O
    public static final int                                  HOTEL_OVER18_NONE                 = 0;                                                     // �ʏ�
    public static final int                                  HOTEL_OVER18_ABORT                = 1;                                                     // 18��

    private XmlTag                                           hotel;                                                                                     // �z�e�����i�[�^�O
    private XmlTag                                           hotelName;                                                                                 // �z�e����
    private XmlTag                                           hotelId;                                                                                   // �z�e��
    private XmlTag                                           hotelRank;                                                                                 // �z�e�������N
    private XmlTag                                           hotelRsvFlag;                                                                              // �z�e�������N
    private XmlTag                                           hotelMemberImg;                                                                            // �����X�摜
    private XmlTag                                           hotelMemberUrl;                                                                            // �����XURL
    private XmlTag                                           hotelMemberText;                                                                           // �����X�e�L�X�g
    private XmlTag                                           hotelOver18;                                                                               // 18�փt���O
    private XmlTag                                           hotelImage;                                                                                // �z�e���O�ω摜URL
    private XmlTag                                           hotelMessage;                                                                              // �z�e���ŐV���
    private XmlTag                                           hotelPr;                                                                                   // �z�e���Љ
    private XmlTag                                           hotelHappiePr;                                                                             // �n�s�[PR��
    private XmlTag                                           hotelKuchikomiAvg;                                                                         // �N�`�R�~����
    private XmlTag                                           hotelAddress;                                                                              // �Z��
    private XmlTag                                           hotelTel;                                                                                  // �d�b�ԍ�
    private XmlTag                                           hotelMapcode;                                                                              // �}�b�v�R�[�h
    private XmlTag                                           hotelEmpty;                                                                                // �󎺏��i�[�^�O
    private XmlTag                                           emptyStatus;                                                                               // �󖞏��
    private XmlTag                                           emptyRoom;                                                                                 // �󂫕�����
    private XmlTag                                           emptyUpDate;                                                                               // �󖞏��擾���t
    private XmlTag                                           emptyUpTime;                                                                               // �󖞏��擾����
    private XmlTag                                           emptyNomemberText;                                                                         // �����e�L�X�g
    private XmlTag                                           emptyNomemberUrl;                                                                          // �����e�L�X�gURL
    private XmlTag                                           hotelRoomAllCount;                                                                         // ��������
    private XmlTag                                           hotelRoom;                                                                                 // �������i�[�^�O
    private XmlTag                                           roomCount;                                                                                 // ������
    private XmlTag                                           hotelGallery;                                                                              // �摜�M�������[���i�[�^�O
    private XmlTag                                           hotelParking;                                                                              // ���ԏ�䐔
    private XmlTag                                           hotelBldgType;                                                                             // �����`��
    private XmlTag                                           hotelAccess;                                                                               // �Z��
    private XmlTag                                           hotelMap;                                                                                  // �n�}���i�[�^�O
    private XmlTag                                           mapLat;                                                                                    // �ܓx
    private XmlTag                                           mapLon;                                                                                    // �o�x
    private XmlTag                                           hotelPrices;                                                                               // �������i�[�^�O
    private XmlTag                                           hotelPrivileges;                                                                           // �����o�[���T
    private XmlTag                                           hotelRoomservice;                                                                          // ���[���T�[�r�X
    private XmlTag                                           hotelOtherservice;                                                                         // ���̑��T�[�r�X
    private XmlTag                                           hotelCredit;                                                                               // �x�����@
    private XmlTag                                           hotelHalfway;                                                                              // �r���O�o
    private XmlTag                                           hotelHeadcount;                                                                            // �l��
    private XmlTag                                           hotelReserve;                                                                              // �\����
    private XmlTag                                           hotelEquips;                                                                               // �ݔ����i�[�^�O
    private XmlTag                                           hotelCoupon;                                                                               // �N�[�|�����i�[�^�O
    private XmlTag                                           couponImage;                                                                               // �N�[�|���摜URL
    private XmlTag                                           couponNo;                                                                                  // �N�[�|���ԍ�
    private XmlTag                                           couponCommonCondition;                                                                     // ���ʎg�p����
    private XmlTag                                           couponPeriod;                                                                              // �L������
    private XmlTag                                           couponIssuance;                                                                            // ���s���t
    private XmlTag                                           couponMobileCondition;                                                                     // �g�уN�[�|������
    private XmlTag                                           customId;

    private ArrayList<GenerateXmlDetailHotelRoomDetail>      roomDateil                        = new ArrayList<GenerateXmlDetailHotelRoomDetail>();     // �����ڍ׏��
    private ArrayList<GenerateXmlDetailHotelGalleryCategory> galleryCategor                    = new ArrayList<GenerateXmlDetailHotelGalleryCategory>(); // �M�������[���
    private ArrayList<GenerateXmlDetailHotelSite>            site                              = new ArrayList<GenerateXmlDetailHotelSite>();           // �O���T�C�g���
    private ArrayList<GenerateXmlDetailHotelPricesKind>      pricesKind                        = new ArrayList<GenerateXmlDetailHotelPricesKind>();     // �������
    private ArrayList<GenerateXmlDetailHotelEquipsKind>      equipsKind                        = new ArrayList<GenerateXmlDetailHotelEquipsKind>();     // �ݔ����
    private ArrayList<GenerateXmlDetailHotelCouponDetail>    couponDetail                      = new ArrayList<GenerateXmlDetailHotelCouponDetail>();   // �N�[�|�����

    @Override
    protected void initXmlNodeInfo()
    {
        hotel = createRootChild( TAG_HOTEL );
        XmlTag.setParent( hotel, hotelName );
        XmlTag.setParent( hotel, hotelId );
        XmlTag.setParent( hotel, hotelRank );
        XmlTag.setParent( hotel, hotelRsvFlag );
        XmlTag.setParent( hotel, hotelMemberImg );
        XmlTag.setParent( hotel, hotelMemberUrl );
        XmlTag.setParent( hotel, hotelMemberText );
        XmlTag.setParent( hotel, hotelOver18 );
        XmlTag.setParent( hotel, hotelImage );
        XmlTag.setParent( hotel, hotelMessage );
        XmlTag.setParent( hotel, hotelPr );
        XmlTag.setParent( hotel, hotelHappiePr );
        XmlTag.setParent( hotel, hotelKuchikomiAvg );
        XmlTag.setParent( hotel, hotelAddress );
        XmlTag.setParent( hotel, hotelTel );
        XmlTag.setParent( hotel, hotelMapcode );
        // �󖞏�񂪂���ꍇ�̂ݒǉ�����
        if ( hotelEmpty != null )
        {
            hotelEmpty = XmlTag.createXmlTag( TAG_HOTEL_EMPTY );
            XmlTag.setParent( hotel, hotelEmpty );
            XmlTag.setParent( hotelEmpty, emptyStatus );
            XmlTag.setParent( hotelEmpty, emptyRoom );
            XmlTag.setParent( hotelEmpty, emptyUpDate );
            XmlTag.setParent( hotelEmpty, emptyUpTime );
            XmlTag.setParent( hotelEmpty, emptyNomemberText );
            XmlTag.setParent( hotelEmpty, emptyNomemberUrl );
        }

        XmlTag.setParent( hotel, hotelRoomAllCount );
        // ������񂪂���ꍇ�̂ݒǉ�����
        if ( hotelRoom != null )
        {
            hotelRoom = XmlTag.createXmlTag( TAG_HOTEL_ROOM );
            XmlTag.setParent( hotel, hotelRoom );
            XmlTag.setParent( hotelRoom, roomCount );
            if ( roomDateil != null )
            {
                for( int i = 0 ; i < roomDateil.size() ; i++ )
                {
                    roomDateil.get( i ).setRootNode( hotelRoom );
                    roomDateil.get( i ).initXmlNodeInfo();
                }
            }
        }
        // �M�������[��񂪂���ꍇ�̂ݒǉ�����
        if ( hotelGallery != null )
        {
            XmlTag.setParent( hotel, hotelGallery );

            if ( galleryCategor != null )
            {
                for( int i = 0 ; i < galleryCategor.size() ; i++ )
                {
                    galleryCategor.get( i ).setRootNode( hotelGallery );
                    galleryCategor.get( i ).initXmlNodeInfo();
                }
            }
        }
        XmlTag.setParent( hotel, hotelParking );
        XmlTag.setParent( hotel, hotelBldgType );
        XmlTag.setParent( hotel, hotelAccess );
        // �ܓx�o�x��񂪂���ꍇ�̂ݒǉ�����
        if ( hotelMap != null )
        {
            XmlTag.setParent( hotel, hotelMap );
            XmlTag.setParent( hotelMap, mapLat );
            XmlTag.setParent( hotelMap, mapLon );
        }
        // �O���T�C�g��񂪂���ꍇ�̂ݒǉ�����
        if ( site != null )
        {
            for( int i = 0 ; i < site.size() ; i++ )
            {
                site.get( i ).setRootNode( hotel );
                site.get( i ).initXmlNodeInfo();
            }
        }
        // ������񂪂���ꍇ�̂ݒǉ�����
        if ( hotelPrices != null )
        {
            XmlTag.setParent( hotel, hotelPrices );
            for( int i = 0 ; i < pricesKind.size() ; i++ )
            {
                pricesKind.get( i ).setRootNode( hotelPrices );
                pricesKind.get( i ).initXmlNodeInfo();
            }
        }
        XmlTag.setParent( hotel, hotelPrivileges );
        XmlTag.setParent( hotel, hotelRoomservice );
        XmlTag.setParent( hotel, hotelOtherservice );
        XmlTag.setParent( hotel, hotelCredit );
        XmlTag.setParent( hotel, hotelHalfway );
        XmlTag.setParent( hotel, hotelHeadcount );
        XmlTag.setParent( hotel, hotelReserve );
        // �ݔ���񂪂���ꍇ�̂ݒǉ�����
        if ( hotelEquips != null )
        {
            XmlTag.setParent( hotel, hotelEquips );
            for( int i = 0 ; i < equipsKind.size() ; i++ )
            {
                equipsKind.get( i ).setRootNode( hotelEquips );
                equipsKind.get( i ).initXmlNodeInfo();
            }
        }
        // �N�[�|����񂪂���ꍇ�̂ݒǉ�����
        if ( hotelCoupon != null )
        {
            XmlTag.setParent( hotel, hotelCoupon );
            XmlTag.setParent( hotelCoupon, couponImage );
            XmlTag.setParent( hotelCoupon, couponNo );
            for( int i = 0 ; i < couponDetail.size() ; i++ )
            {
                couponDetail.get( i ).setRootNode( hotelCoupon );
                couponDetail.get( i ).initXmlNodeInfo();
            }
            XmlTag.setParent( hotelCoupon, couponCommonCondition );
            XmlTag.setParent( hotelCoupon, couponPeriod );
            XmlTag.setParent( hotelCoupon, couponIssuance );
            XmlTag.setParent( hotelCoupon, couponMobileCondition );

        }
        XmlTag.setParent( hotel, customId );
        return;
    }

    public void setName(String name)
    {
        if ( name != null )
        {
            name = ReplaceString.replaceApiSpecial( name );
        }
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

    public void setRsvFlag(int rsvFlag)
    {
        hotelRsvFlag = XmlTag.createXmlTag( TAG_HOTEL_RSV_FLAG, rsvFlag );
        return;
    }

    public void setMemberImg(String memberImg)
    {
        hotelMemberImg = XmlTag.createXmlTag( TAG_HOTEL_MEMBER_IMG, memberImg );
        return;
    }

    public void setMemberUrl(String memberUrl)
    {
        hotelMemberUrl = XmlTag.createXmlTag( TAG_HOTEL_MEMBER_URL, memberUrl );
        return;
    }

    public void setMemberText(String memberText)
    {
        hotelMemberText = XmlTag.createXmlTag( TAG_HOTEL_MEMBER_TEXT, memberText );
        return;
    }

    public void setOver18(int over18)
    {
        hotelOver18 = XmlTag.createXmlTag( TAG_HOTEL_OVER18, over18 );
        return;
    }

    public void setImage(String image)
    {
        if ( image != null && image.equals( "" ) == false && image.indexOf( "http://" ) == -1 && image.indexOf( "https://" ) == -1 )
        {
            image = Url.getUrl() + image;
        }
        hotelImage = XmlTag.createXmlTag( TAG_HOTEL_IMAGE, image );
        return;
    }

    public void setMessage(String message)
    {
        if ( message != null )
        {
            message = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( message ) );
        }
        hotelMessage = XmlTag.createXmlTagNoCheck( TAG_HOTEL_MESSAGE, message );
        return;
    }

    public void setPr(String pr)
    {
        if ( pr != null )
        {
            pr = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( pr ) );
        }
        hotelPr = XmlTag.createXmlTag( TAG_HOTEL_PR, pr );
        return;
    }

    public void setHappiePr(String happiePr)
    {
        if ( happiePr != null )
        {
            happiePr = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( happiePr ) );
        }
        hotelHappiePr = XmlTag.createXmlTag( TAG_HOTEL_HAPPIEPR, happiePr );
        return;
    }

    public void setKuchikomiAvg(String avg)
    {
        hotelKuchikomiAvg = XmlTag.createXmlTag( TAG_HOTEL_KUCHIKOMIAVG, avg );
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

    public void setMapcode(String mapcode)
    {
        hotelMapcode = XmlTag.createXmlTag( TAG_HOTEL_MAPCODE, mapcode );
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

    public void setEmptyStatus(int status)
    {
        emptyStatus = XmlTag.createXmlTag( TAG_HOTEL_EMPTY_STATUS, status );
        return;
    }

    public void setEmptyRoom(String room)
    {
        emptyRoom = XmlTag.createXmlTag( TAG_HOTEL_EMPTY_ROOM, room );
        return;
    }

    public void setEmptyUpDate(int upDate)
    {
        emptyUpDate = XmlTag.createXmlTag( TAG_HOTEL_EMPTY_UPDATE, upDate );
        return;
    }

    public void setEmptyUpTime(String upTime)
    {
        emptyUpTime = XmlTag.createXmlTag( TAG_HOTEL_EMPTY_UPTIME, upTime );
        return;
    }

    public void setEmptyNoMemberText(String nomemberText)
    {
        emptyNomemberText = XmlTag.createXmlTag( TAG_HOTEL_EMPTY_NOMEMBER_TEXT, nomemberText );
        return;
    }

    public void setEmptyNoMemberUrl(String nomemberUrl)
    {
        emptyNomemberUrl = XmlTag.createXmlTag( TAG_HOTEL_EMPTY_NOMEMBER_URL, nomemberUrl );
        return;
    }

    public void setRoomAllCount(String roomAllCount)
    {
        hotelRoomAllCount = XmlTag.createXmlTag( TAG_HOTEL_ROOMALLCOUNT, roomAllCount );
        return;
    }

    public void setRoom(String room)
    {
        hotelRoom = XmlTag.createXmlTag( TAG_HOTEL_ROOM, room );
        return;
    }

    public void setRoomCount(int count)
    {
        roomCount = XmlTag.createXmlTag( TAG_HOTEL_ROOM_COUNT, count );
        return;
    }

    public void setGallery(String gallery)
    {
        if ( gallery != null )
        {
            gallery = ReplaceString.replaceApiSpecial( gallery );
        }
        hotelGallery = XmlTag.createXmlTag( TAG_HOTEL_GALLERY, gallery );
        return;
    }

    public void setParking(String parking)
    {
        if ( parking != null )
        {
            parking = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( parking ) );
        }
        hotelParking = XmlTag.createXmlTag( TAG_HOTEL_PARKING, parking );
        return;
    }

    public void setBldgType(String bldgType)
    {
        if ( bldgType != null )
        {
            bldgType = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( bldgType ) );
        }
        hotelBldgType = XmlTag.createXmlTag( TAG_HOTEL_BLDGTYPE, bldgType );
        return;
    }

    public void setAccess(String access)
    {
        if ( access != null )
        {
            access = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( access ) );
        }
        if ( access.equals( "" ) != false )
        {
            access += "�@";
        }
        hotelAccess = XmlTag.createXmlTag( TAG_HOTEL_ACCESS, access );
        return;
    }

    public void setMap(String map)
    {
        hotelMap = XmlTag.createXmlTag( TAG_HOTEL_MAP, map );
        return;
    }

    public void setMapLat(String lat)
    {
        mapLat = XmlTag.createXmlTag( TAG_HOTEL_MAP_LAT, lat );
        return;
    }

    public void setMapLon(String lon)
    {
        mapLon = XmlTag.createXmlTag( TAG_HOTEL_MAP_LON, lon );
        return;
    }

    public void setHotelPrices(String prices)
    {
        if ( prices != null )
        {
            prices = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( prices ) );
        }
        hotelPrices = XmlTag.createXmlTag( TAG_HOTEL_PRICES, prices );
        return;
    }

    public void setPrivileges(String privileges)
    {
        if ( privileges != null )
        {
            privileges = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( privileges ) );
        }
        hotelPrivileges = XmlTag.createXmlTag( TAG_HOTEL_PRIVILEGES, privileges );
        return;
    }

    public void setRoomservice(String roomservice)
    {
        if ( roomservice != null )
        {
            roomservice = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( roomservice ) );
        }
        hotelRoomservice = XmlTag.createXmlTag( TAG_HOTEL_ROOMSERVICE, roomservice );
        return;
    }

    public void setOtherservice(String otherservice)
    {
        if ( otherservice != null )
        {
            otherservice = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( otherservice ) );
        }
        hotelOtherservice = XmlTag.createXmlTag( TAG_HOTEL_OTHERSERVICE, otherservice );
        return;
    }

    public void setCredit(String credit)
    {
        if ( credit != null )
        {
            credit = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( credit ) );
        }
        hotelCredit = XmlTag.createXmlTag( TAG_HOTEL_CREDIT, credit );
        return;
    }

    public void setHalfway(String halfway)
    {
        if ( halfway != null )
        {
            halfway = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( halfway ) );
        }
        hotelHalfway = XmlTag.createXmlTag( TAG_HOTEL_HALFWAY, halfway );
        return;
    }

    public void setHeadcount(String headcount)
    {
        if ( headcount != null )
        {
            headcount = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( headcount ) );
        }
        hotelHeadcount = XmlTag.createXmlTag( TAG_HOTEL_HEADCOUNT, headcount );
        return;
    }

    public void setReserve(String reserve)
    {
        if ( reserve != null )
        {
            reserve = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( reserve ) );
        }
        hotelReserve = XmlTag.createXmlTag( TAG_HOTEL_RESERVE, reserve );
        return;
    }

    public void setEquips(String equips)
    {
        if ( equips != null )
        {
            equips = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( equips ) );
        }
        hotelEquips = XmlTag.createXmlTag( TAG_HOTEL_EQUIPS, equips );
        return;
    }

    public void setCoupon(String coupon)
    {
        hotelCoupon = XmlTag.createXmlTag( TAG_HOTEL_COUPON, coupon );
        return;
    }

    public void setCouponImage(String Image)
    {
        if ( Image != null && Image.equals( "" ) == false && Image.indexOf( "http://" ) == -1 && Image.indexOf( "https://" ) == -1 )
        {
            Image = Url.getUrl() + Image;
        }
        couponImage = XmlTag.createXmlTag( TAG_HOTEL_COUPON_IMAGE, Image );
        return;
    }

    public void setCouponNo(String No)
    {
        couponNo = XmlTag.createXmlTag( TAG_HOTEL_COUPON_NO, No );
        return;
    }

    public void setCouponCommonCondition(String CommonCondition)
    {
        if ( CommonCondition != null )
        {
            CommonCondition = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( CommonCondition ) );
        }
        couponCommonCondition = XmlTag.createXmlTag( TAG_HOTEL_COUPON_COMMONCONDITION, CommonCondition );
        return;
    }

    public void setCouponPeriod(String Period)
    {
        if ( Period != null )
        {
            Period = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( Period ) );
        }
        couponPeriod = XmlTag.createXmlTag( TAG_HOTEL_COUPON_PERIOD, Period );
        return;
    }

    public void setCouponIssuance(String Issuance)
    {
        if ( Issuance != null )
        {
            Issuance = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( Issuance ) );
        }
        couponIssuance = XmlTag.createXmlTag( TAG_HOTEL_COUPON_ISSUANCE, Issuance );
        return;
    }

    public void setCouponMobileCondition(String CouponMobileCondition)
    {
        if ( CouponMobileCondition != null )
        {
            CouponMobileCondition = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( CouponMobileCondition ) );
        }
        couponMobileCondition = XmlTag.createXmlTag( TAG_HOTEL_COUPON_MOBILE_CONDITION, CouponMobileCondition );
    }

    public void addRoomDateil(GenerateXmlDetailHotelRoomDetail addRoomDateil)
    {
        roomDateil.add( addRoomDateil );
        return;
    }

    public void addGalleryCategor(GenerateXmlDetailHotelGalleryCategory addGalleryCategor)
    {
        galleryCategor.add( addGalleryCategor );
        return;
    }

    public void addSite(GenerateXmlDetailHotelSite addSite)
    {
        site.add( addSite );
        return;
    }

    public void addPricesKind(GenerateXmlDetailHotelPricesKind addPricesKind)
    {
        pricesKind.add( addPricesKind );
        return;
    }

    public void addEquipsKind(GenerateXmlDetailHotelEquipsKind addEquipsKind)
    {
        equipsKind.add( addEquipsKind );
        return;
    }

    public void addCouponDetail(GenerateXmlDetailHotelCouponDetail addCouponDetail)
    {
        couponDetail.add( addCouponDetail );
        return;
    }

    public void setCustomId(String customid)
    {
        if ( customid != null )
        {
            customid = ReplaceString.replaceApiSpecial( ReplaceString.replaceApiBr( customid ) );
        }
        customId = XmlTag.createXmlTag( TAG_HOTEL_CUSTOM_ID, customid );
        return;
    }

}
