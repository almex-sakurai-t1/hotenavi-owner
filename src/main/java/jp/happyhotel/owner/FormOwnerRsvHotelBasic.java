package jp.happyhotel.owner;

/**
 * 
 * �{�݊�{����� Form�N���X
 */
public class FormOwnerRsvHotelBasic
{
    private int    hotelId         = 0;
    private String hotelName       = "";
    private String zipCode         = ""; // �X�֔ԍ�
    private String address         = ""; // �s���{���� + �Z��1 + �Z��2 + �Z��3
    private String tel1            = "";
    private int    roomCnt         = 0;
    private int    over18Flag      = 0; // 18�փt���O
    private String over18Value     = ""; // 18�փt���O �\��
    private String reservePr       = ""; // �\��pPR
    private int    selHotelID      = 0;
    private int    userID          = 0;
    private String reservePrView   = ""; // �\���p�\��pPR
    private String errMsg          = "";
    private String ownerHotelID    = "";
    private String childCharge     = ""; // �q��������`
    private String childChargeView = ""; // �\���p�q��������`

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getHotelId()
    {
        return hotelId;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = hotelId;
    }

    public String getHotelName()
    {
        return hotelName;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zi��Code)
    {
        this.zipCode = zi��Code;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getTel1()
    {
        return tel1;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public int getRoomCnt()
    {
        return roomCnt;
    }

    public void setRoomCnt(int roomCnt)
    {
        this.roomCnt = roomCnt;
    }

    public int getOver18Flag()
    {
        return over18Flag;
    }

    public void setOver18Flag(int over18_flag)
    {
        this.over18Flag = over18_flag;
    }

    public String getReservePr()
    {
        return reservePr;
    }

    public void setReservePr(String reservePr)
    {
        this.reservePr = reservePr;
    }

    public String getOver18Value()
    {
        return over18Value;
    }

    public void setOver18Value(String over18Value)
    {
        this.over18Value = over18Value;
    }

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public String getReservePrView()
    {
        return reservePrView;
    }

    public void setReservePrView(String reservePrView)
    {
        this.reservePrView = reservePrView;
    }

    public String getOwnerHotelID()
    {
        return ownerHotelID;
    }

    public void setOwnerHotelID(String ownerHotelID)
    {
        this.ownerHotelID = ownerHotelID;
    }

    public void setChildCharge(String childCharge)
    {
        this.childCharge = childCharge;
    }

    public String getChildCharge()
    {
        return childCharge;
    }

    public void setChildChargeView(String childChargeView)
    {
        this.childChargeView = childChargeView;
    }

    public String getChildChargeView()
    {
        return childChargeView;
    }

}
