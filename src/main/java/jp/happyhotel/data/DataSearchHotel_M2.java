package jp.happyhotel.data;

public class DataSearchHotel_M2
{

    private int      id                  = 0;
    private int      rank                = 0;
    private String   name                = "";
    private String   nameMobile          = "";
    private String   prefId              = "";
    private String   prefName            = "";
    private String   address1            = "";
    private String   addressAll          = "";
    private String   tel1                = "";
    private String   lat                 = "";
    private String   lon                 = "";
    private String   url                 = "";
    private String   urlOfficial1        = "";
    private String   urlOfficial2        = "";
    private String   pr                  = "";
    private String   imagePath           = "";
    private String   hotelMessage        = "";
    private String   statusMessage       = "";
    private int      reserve             = 0;
    private int      reserveTel          = 0;
    private int      reserveMail         = 0;
    private int      reserveWeb          = 0;
    private int      emptyStatus         = 0;
    private int      lastUpDate          = 0;
    private int      lastUpTime          = 0;
    private int      couponCount         = 0;
    private int      bbsConfig           = 0;
    private int      bbsAllCount         = 0;
    private int      points              = 0;
    private int      stars               = 0;
    private int      over18Flag          = 0;
    private int      companyType         = 0;
    private boolean  isReserveFlag       = false;
    private int      reserveSalesFlag    = 0;
    private int      reservePlanCount    = 0;
    private int[]    reservePlanId       = null;
    private String[] reservePlan         = null; // 予約プラン名（各種検索結果で使用）
    private String   reservePlanPr       = null; // 予約のPR（予約ハピー加盟店検索で使用）
    private String   reservePlanImagePc  = null; // 予約の画像（予約ハピー加盟店検索で使用）
    private String   reservePlanImageGif = null; // 予約の画像（予約ハピー加盟店検索で使用）
    private String   reservePlanImagePng = null; // 予約の画像（予約ハピー加盟店検索で使用）
    private String   happieName          = "";
    private int      roomCount           = 0;
    private int[]    roomSeq             = null;
    private int[]    hotelCount          = null;
    private int      touchEquipFlag      = 0;    // タッチ機器フラグ

    public int getId()
    {
        return id;
    }

    public int getRank()
    {
        return rank;
    }

    public String getName()
    {
        return name;
    }

    public String getNameMobile()
    {
        return nameMobile;
    }

    public String getPrefId()
    {
        return prefId;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public String getAddress1()
    {
        return address1;
    }

    public String getAddressAll()
    {
        return addressAll;
    }

    public String getTel1()
    {
        return tel1;
    }

    public String getLat()
    {
        return lat;
    }

    public String getLon()
    {
        return lon;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUrlOfficial1()
    {
        return urlOfficial1;
    }

    public String getUrlOfficial2()
    {
        return urlOfficial2;
    }

    public String getPr()
    {
        return pr;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public String getHotelMessage()
    {
        return hotelMessage;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public int getReserve()
    {
        return reserve;
    }

    public int getReserveTel()
    {
        return reserveTel;
    }

    public int getReserveMail()
    {
        return reserveMail;
    }

    public int getReserveWeb()
    {
        return reserveWeb;
    }

    public int getEmptyStatus()
    {
        return emptyStatus;
    }

    public int getLastUpDate()
    {
        return lastUpDate;
    }

    public int getLastUpTime()
    {
        return lastUpTime;
    }

    public int getCouponCount()
    {
        return couponCount;
    }

    public int getBbsConfig()
    {
        return bbsConfig;
    }

    public int getBbsAllCount()
    {
        return bbsAllCount;
    }

    public int getPoints()
    {
        return points;
    }

    public int getStars()
    {
        return stars;
    }

    public int getOver18Flag()
    {
        return over18Flag;
    }

    public int getCompanyType()
    {
        return companyType;
    }

    public boolean getIsReserveFlag()
    {
        return isReserveFlag;
    }

    public int getReserveSalesFlag()
    {
        return reserveSalesFlag;
    }

    public int getReservePlanCount()
    {
        return reservePlanCount;
    }

    public int[] getReservePlanId()
    {
        return reservePlanId;
    }

    public String[] getReservePlan()
    {
        return reservePlan;
    }

    public String getReservePlanPr()
    {
        return reservePlanPr;
    }

    public String getReservePlanImagePc()
    {
        return reservePlanImagePc;
    }

    public String getReservePlanImageGif()
    {
        return reservePlanImageGif;
    }

    public String getReservePlanImagePng()
    {
        return reservePlanImagePng;
    }

    public String getHappieName()
    {
        return happieName;
    }

    public int getRoomCount()
    {
        return roomCount;
    }

    public int[] getRoomSeq()
    {
        return roomSeq;
    }

    public int[] getHotelCount()
    {
        return hotelCount;
    }

    public int getTouchEquipFlag()
    {
        return touchEquipFlag;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameMobile(String nameMobile)
    {
        this.nameMobile = nameMobile;
    }

    public void setPrefId(String prefId)
    {
        this.prefId = prefId;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddressAll(String addressAll)
    {
        this.addressAll = addressAll;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public void setLon(String lon)
    {
        this.lon = lon;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setUrlOfficial1(String urlOfficial1)
    {
        this.urlOfficial1 = urlOfficial1;
    }

    public void setUrlOfficial2(String urlOfficial2)
    {
        this.urlOfficial2 = urlOfficial2;
    }

    public void setPr(String pr)
    {
        this.pr = pr;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public void setHotelMessage(String hotelMessage)
    {
        this.hotelMessage = hotelMessage;
    }

    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    public void setReserve(int reserve)
    {
        this.reserve = reserve;
    }

    public void setReserveTel(int reserveTel)
    {
        this.reserveTel = reserveTel;
    }

    public void setReserveMail(int reserveMail)
    {
        this.reserveMail = reserveMail;
    }

    public void setReserveWeb(int reserveWeb)
    {
        this.reserveWeb = reserveWeb;
    }

    public void setEmptyStatus(int emptyStatus)
    {
        this.emptyStatus = emptyStatus;
    }

    public void setLastUpDate(int lastUpDate)
    {
        this.lastUpDate = lastUpDate;
    }

    public void setLastUpTime(int lastUpTime)
    {
        this.lastUpTime = lastUpTime;
    }

    public void setCouponCount(int couponCount)
    {
        this.couponCount = couponCount;
    }

    public void setBbsConfig(int bbsConfig)
    {
        this.bbsConfig = bbsConfig;
    }

    public void setBbsAllCount(int bbsAllCount)
    {
        this.bbsAllCount = bbsAllCount;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public void setStars(int stars)
    {
        this.stars = stars;
    }

    public void setOver18Flag(int over18Flag)
    {
        this.over18Flag = over18Flag;
    }

    public void setCompanyType(int companyType)
    {
        this.companyType = companyType;
    }

    public void setIsReserveFlag(boolean isReserveFlag)
    {
        this.isReserveFlag = isReserveFlag;
    }

    public void setReserveSalesFlag(int reserveSalesFlag)
    {
        this.reserveSalesFlag = reserveSalesFlag;
    }

    public void setReservePlanCount(int reservePlanCount)
    {
        this.reservePlanCount = reservePlanCount;
    }

    public void setReservePlanId(int[] reservePlanId)
    {
        this.reservePlanId = reservePlanId;
    }

    public void setReservePlan(String[] reservePlan)
    {
        this.reservePlan = reservePlan;
    }

    public void setReservePlanPr(String reservePlanPr)
    {
        this.reservePlanPr = reservePlanPr;
    }

    public void setReservePlanImagePc(String reservePlanImagePc)
    {
        this.reservePlanImagePc = reservePlanImagePc;
    }

    public void setReservePlanImageGif(String reservePlanImageGif)
    {
        this.reservePlanImageGif = reservePlanImageGif;
    }

    public void setReservePlanImagePng(String reservePlanImagePng)
    {
        this.reservePlanImagePng = reservePlanImagePng;
    }

    public void setHappieName(String happieName)
    {
        this.happieName = happieName;
    }

    public void setRoomCount(int roomCount)
    {
        this.roomCount = roomCount;
    }

    public void setRoomSeq(int[] roomSeq)
    {
        this.roomSeq = roomSeq;
    }

    public void setHotelCount(int[] hotelCount)
    {
        this.hotelCount = hotelCount;
    }

    public void setTouchEquipFlag(int hotelEquipFlag)
    {
        this.touchEquipFlag = hotelEquipFlag;
    }

}
