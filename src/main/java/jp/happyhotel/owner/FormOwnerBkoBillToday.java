package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * ¿‹–¾× “ú•Ê¿‹–¾×‰æ–ÊFormƒNƒ‰ƒX
 */
public class FormOwnerBkoBillToday
{
    private int                selHotelID         = 0;
    private String             selHotelName       = "";
    private int                selYear            = 0;
    private int                selMonth           = 0;
    private String             errMsg             = "";
    private int                rsvKind            = 0;

    private ArrayList<Integer> slipNoList         = new ArrayList<Integer>(); // ”„Š|“`•[”Ô†
    private ArrayList<String>  usageDateList      = new ArrayList<String>(); // —˜—p“ú
    private ArrayList<Integer> usageDateIntList   = new ArrayList<Integer>();
    private ArrayList<String>  usageTimeList      = new ArrayList<String>(); // —˜—pŠÔ
    private ArrayList<Integer> usageTimeIntList   = new ArrayList<Integer>();
    private ArrayList<Integer> accountTitleCdList = new ArrayList<Integer>(); // ‰È–Ú
    private ArrayList<String>  huyoList           = new ArrayList<String>();
    private ArrayList<String>  siyouList          = new ArrayList<String>();
    private ArrayList<String>  reserveList        = new ArrayList<String>();
    private ArrayList<String>  acc200List         = new ArrayList<String>();
    private ArrayList<String>  personNmList       = new ArrayList<String>(); // ’S“––¼
    private ArrayList<Integer> customerIdList     = new ArrayList<Integer>(); // ŒÚ‹qID
    private ArrayList<String>  htSlipNoList       = new ArrayList<String>(); // “`•[No.(ƒnƒsƒ^ƒbƒ`)
    private ArrayList<String>  roomList           = new ArrayList<String>(); // •”‰®
    private ArrayList<String>  seisanAmountList   = new ArrayList<String>(); // ¸Z‹àŠz
    private ArrayList<String>  seisanFeeList      = new ArrayList<String>(); // ƒ^ƒbƒ`è”—¿
    private ArrayList<String>  rsvAmountList      = new ArrayList<String>(); // —\–ñ‹àŠz
    private ArrayList<String>  rsvFeeList         = new ArrayList<String>(); // —\–ñ‘—‹qè”—¿
    private ArrayList<String>  rsvBonusList       = new ArrayList<String>(); // —\–ñƒ{[ƒiƒXƒ}ƒCƒ‹
    private ArrayList<String>  billList           = new ArrayList<String>(); // ¿‹
    private ArrayList<String>  payList            = new ArrayList<String>(); // x•¥‚¢
    private ArrayList<Integer> billDate           = new ArrayList<Integer>(); // ¿‹’÷“ú
    private ArrayList<String>  userIdList         = new ArrayList<String>(); // ƒ†[ƒU[ID

    private String             sumBill            = "";
    private String             sumPay             = "";
    private String             sumSyusi           = "";
    private int                usageDate          = 0;
    private int                intBillDate        = 0;
    private boolean            blankCloseFlg      = false;

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public String getSelHotelName()
    {
        return selHotelName;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getSelYear()
    {
        return selYear;
    }

    public void setSelYear(int selYear)
    {
        this.selYear = selYear;
    }

    public int getSelMonth()
    {
        return selMonth;
    }

    public void setSelMonth(int selMonth)
    {
        this.selMonth = selMonth;
    }

    public int getRsvKind()
    {
        return rsvKind;
    }

    public void setRsvKind(int rsvKind)
    {
        this.rsvKind = rsvKind;
    }

    public ArrayList<Integer> getSlipNoList()
    {
        return slipNoList;
    }

    public void setSlipNoList(ArrayList<Integer> slipNoList)
    {
        this.slipNoList = slipNoList;
    }

    public ArrayList<String> getUsageDateList()
    {
        return usageDateList;
    }

    public void setUsageDateList(ArrayList<String> usageDateList)
    {
        this.usageDateList = usageDateList;
    }

    public ArrayList<String> getUsageTimeList()
    {
        return usageTimeList;
    }

    public void setUsageTimeList(ArrayList<String> usageTimeList)
    {
        this.usageTimeList = usageTimeList;
    }

    public ArrayList<Integer> getAccountTitleCdList()
    {
        return accountTitleCdList;
    }

    public void setAccountTitleCdList(ArrayList<Integer> accountTitleCdList)
    {
        this.accountTitleCdList = accountTitleCdList;
    }

    public ArrayList<String> getPersonNmList()
    {
        return personNmList;
    }

    public void setPersonNmList(ArrayList<String> personNmList)
    {
        this.personNmList = personNmList;
    }

    public ArrayList<Integer> getCustomerIdList()
    {
        return customerIdList;
    }

    public void setCustomerIdList(ArrayList<Integer> customerIdList)
    {
        this.customerIdList = customerIdList;
    }

    public ArrayList<String> getRoomList()
    {
        return roomList;
    }

    public void setRoomList(ArrayList<String> roomList)
    {
        this.roomList = roomList;
    }

    public ArrayList<String> getBillList()
    {
        return billList;
    }

    public void setBillList(ArrayList<String> billList)
    {
        this.billList = billList;
    }

    public ArrayList<String> getPayList()
    {
        return payList;
    }

    public void setPayList(ArrayList<String> payList)
    {
        this.payList = payList;
    }

    public ArrayList<String> getHtSlipNoList()
    {
        return htSlipNoList;
    }

    public void setHtSlipNoList(ArrayList<String> htSlipNoList)
    {
        this.htSlipNoList = htSlipNoList;
    }

    public ArrayList<Integer> getUsageDateIntList()
    {
        return usageDateIntList;
    }

    public void setUsageDateIntList(ArrayList<Integer> usageDateIntList)
    {
        this.usageDateIntList = usageDateIntList;
    }

    public ArrayList<Integer> getUsageTimeIntList()
    {
        return usageTimeIntList;
    }

    public void setUsageTimeIntList(ArrayList<Integer> usageTimeIntList)
    {
        this.usageTimeIntList = usageTimeIntList;
    }

    public String getSumBill()
    {
        return sumBill;
    }

    public void setSumBill(String sumBill)
    {
        this.sumBill = sumBill;
    }

    public String getSumPay()
    {
        return sumPay;
    }

    public void setSumPay(String sumPay)
    {
        this.sumPay = sumPay;
    }

    public ArrayList<String> getSeisanAmountList()
    {
        return seisanAmountList;
    }

    public void setSeisanAmountList(ArrayList<String> seisanAmountList)
    {
        this.seisanAmountList = seisanAmountList;
    }

    public ArrayList<String> getSeisanFeeList()
    {
        return seisanFeeList;
    }

    public void setSeisanFeeList(ArrayList<String> seisanFeeList)
    {
        this.seisanFeeList = seisanFeeList;
    }

    public ArrayList<String> getRsvAmountList()
    {
        return rsvAmountList;
    }

    public void setRsvAmountList(ArrayList<String> rsvAmountList)
    {
        this.rsvAmountList = rsvAmountList;
    }

    public ArrayList<String> getRsvFeeList()
    {
        return rsvFeeList;
    }

    public void setRsvFeeList(ArrayList<String> rsvFeeList)
    {
        this.rsvFeeList = rsvFeeList;
    }

    public ArrayList<String> getRsvBonusList()
    {
        return rsvBonusList;
    }

    public void setRsvBonusList(ArrayList<String> rsvBonusList)
    {
        this.rsvBonusList = rsvBonusList;
    }

    public int getUsageDate()
    {
        return usageDate;
    }

    public void setUsageDate(int usageDate)
    {
        this.usageDate = usageDate;
    }

    public ArrayList<String> getHuyoList()
    {
        return huyoList;
    }

    public void setHuyoList(ArrayList<String> huyoList)
    {
        this.huyoList = huyoList;
    }

    public ArrayList<String> getSiyouList()
    {
        return siyouList;
    }

    public void setSiyouList(ArrayList<String> siyouList)
    {
        this.siyouList = siyouList;
    }

    public ArrayList<String> getReserveList()
    {
        return reserveList;
    }

    public void setReserveList(ArrayList<String> reserveList)
    {
        this.reserveList = reserveList;
    }

    public ArrayList<String> getAcc200List()
    {
        return acc200List;
    }

    public void setAcc200List(ArrayList<String> acc200List)
    {
        this.acc200List = acc200List;
    }

    public String getSumSyusi()
    {
        return sumSyusi;
    }

    public void setSumSyusi(String sumSyusi)
    {
        this.sumSyusi = sumSyusi;
    }

    public ArrayList<Integer> getBillDate()
    {
        return billDate;
    }

    public void setBillDate(ArrayList<Integer> billDate)
    {
        this.billDate = billDate;
    }

    public int getIntBillDate()
    {
        return intBillDate;
    }

    public void setIntBillDate(int intBillDate)
    {
        this.intBillDate = intBillDate;
    }

    public ArrayList<String> getUserIdList()
    {
        return userIdList;
    }

    public void setUserIdList(ArrayList<String> userIdList)
    {
        this.userIdList = userIdList;
    }

    /**
     * BlankFlg‚ğİ’è‚µ‚Ü‚·B
     * 
     * @return BlankFlg
     */
    public void setBlankCloseFlg(boolean blankCloseFlg)
    {
        this.blankCloseFlg = blankCloseFlg;
    }

    /**
     * BlankFlg‚ğæ“¾‚µ‚Ü‚·B
     * 
     * @return BlankFlg
     */
    public boolean isBlankCloseFlg()
    {
        return blankCloseFlg;
    }
}
