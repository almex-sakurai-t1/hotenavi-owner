package jp.happyhotel.data;

public class DataHogeHotelBasic
{

    private int               id;
    private String            name;
    private String            nameKana;
    private int               rank;
    private String            zipCode;
    private int               jisCode;
    private int               prefId;
    private String            prefName;
    private String            address1;
    private String            address2;
    private String            address3;
    private String            tel;
    private String            fax;
    private int               roomCount;
    private int               halfway;
    private int               possibleOne;
    private int               possibleThree;
    private String            hotelLat;
    private String            hotelLon;
    private int               over18Flag;
    private int               isRoomService;
    private int               isCasher;


    public DataHogeHotelBasic()
    {
        this.id               =0;
        this.name             ="";
        this.nameKana         ="";
        this.rank             =0;
        this.zipCode          ="";
        this.jisCode          =0;
        this.prefId           =0;
        this.prefName         ="";
        this.address1         ="";
        this.address2         ="";
        this.address3         ="";
        this.tel              ="";
        this.fax              ="";
        this.roomCount        =0;
        this.halfway          =0;
        this.possibleOne      =0;
        this.possibleThree    =0;
        this.hotelLat         ="";
        this.hotelLon         ="";
        this.over18Flag       =0;
        this.isRoomService    =0;
        this.isCasher         =0;
    }

    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public String getAddress3()
    {
        return address3;
    }

    public String getFax()
    {
        return fax;
    }

    public int getHalfway()
    {
        return halfway;
    }

    public String getHotelLat()
    {
        return hotelLat;
    }

    public String getHotelLon()
    {
        return hotelLon;
    }

    public int getId()
    {
        return id;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public int getOver18Flag()
    {
        return over18Flag;
    }

    public int getPossibleOne()
    {
        return possibleOne;
    }

    public int getPossibleThree()
    {
        return possibleThree;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public int getRank()
    {
        return rank;
    }

    public int getRoomCount()
    {
        return roomCount;
    }

    public String getTel()
    {
        return tel;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public int getIsRoomService()
    {
        return isRoomService;
    }

    public int getIsCasher ()
    {
        return isCasher ;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public void setHalfway(int halfway)
    {
        this.halfway = halfway;
    }

    public void setHotelLat(String hotelLat)
    {
        this.hotelLat = hotelLat;
    }

    public void setHotelLon(String hotelLon)
    {
        this.hotelLon = hotelLon;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setOver18Flag(int over18Flag)
    {
        this.over18Flag = over18Flag;
    }

    public void setPossibleOne(int possibleOne)
    {
        this.possibleOne = possibleOne;
    }

    public void setPossibleThree(int possibleThree)
    {
        this.possibleThree = possibleThree;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public void setRoomCount(int roomCount)
    {
        this.roomCount = roomCount;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }
    public void setIsRoomService(int isRoomService)
    {
        this.isRoomService = isRoomService;
    }

    public void setIsCasher (int isCasher )
    {
        this.isCasher = isCasher ;
    }
}
