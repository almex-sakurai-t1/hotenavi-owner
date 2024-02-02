package jp.happyhotel.owner;

/*
 * ó\ñÒäÓñ{èÓïÒFormÉNÉâÉX
 */
public class FormOwnerRsvReserveBasic
{
    private int    hotelId          = 0;
    private int    selHotelID       = 0;
    private String ownerHotelID     = "";
    private int    userID           = 0;
    private String errMsg           = "";
    private String cashDepositView  = "";
    private String cashDeposit      = "";
    private int    parking          = 1;
    private int    hiroof           = 1;
    private String deadlineTimeHH   = "";
    private String deadlineTimeMM   = "";
    private String deadlineTimeView = "";
    private int    noshowCreditFlag = 0;
    private int    equipDispFlag = 0;

    public int getHotelId()
    {
        return hotelId;
    }

    public void setHotelId(int hotelId)
    {
        this.hotelId = hotelId;
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

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public String getCashDeposit()
    {
        return cashDeposit;
    }

    public void setCashDeposit(String cashDeposit)
    {
        this.cashDeposit = cashDeposit;
    }

    public int getParking()
    {
        return parking;
    }

    public void setParking(int parking)
    {
        this.parking = parking;
    }

    public String getCashDepositView()
    {
        return cashDepositView;
    }

    public void setCashDepositView(String cashDepositView)
    {
        this.cashDepositView = cashDepositView;
    }

    public String getDeadlineTimeHH()
    {
        return deadlineTimeHH;
    }

    public void setDeadlineTimeHH(String deadlineTimeHH)
    {
        this.deadlineTimeHH = deadlineTimeHH;
    }

    public String getDeadlineTimeMM()
    {
        return deadlineTimeMM;
    }

    public void setDeadlineTimeMM(String deadlineTimeMM)
    {
        this.deadlineTimeMM = deadlineTimeMM;
    }

    public String getDeadlineTimeView()
    {
        return deadlineTimeView;
    }

    public void setDeadlineTimeView(String deadlineTimeView)
    {
        this.deadlineTimeView = deadlineTimeView;
    }

    public String getOwnerHotelID()
    {
        return ownerHotelID;
    }

    public void setOwnerHotelID(String ownerHotelID)
    {
        this.ownerHotelID = ownerHotelID;
    }

    public void setNoshowCreditFlag(int noshowCreditFlag)
    {
        this.noshowCreditFlag = noshowCreditFlag;
    }

    public int getNoshowCreditFlag()
    {
        return noshowCreditFlag;
    }

    public void setHiroof(int hiroof)
    {
        this.hiroof = hiroof;
    }

    public int getHiroof()
    {
        return hiroof;
    }

    public int getEquipDispFlag() {
        return equipDispFlag;
    }

    public void setEquipDispFlag(int equipDispFlag) {
        this.equipDispFlag = equipDispFlag;
    }

}
