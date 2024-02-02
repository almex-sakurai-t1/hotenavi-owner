package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;
import jp.happyhotel.common.TimeCommon;

/*
 * 予約データ クラス
 */

public class DataRsvReserve implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -8421319553259356037L;

    private int               iD;
    private String            reserveNo;
    private int               reserveSubNo;
    private int               planId;
    private int               planSubId;
    private String            userId;
    private int               reserveDate;
    private int               seq;
    private int               estTimeArrival;
    private int               numAdult;
    private int               numChild;
    private int               numMan;
    private int               numWoman;
    private String            nameLast;
    private String            nameFirst;
    private String            nameLastKana;
    private String            nameFirstKana;
    private String            zipCode;
    private int               prefCode;
    private int               jisCode;
    private String            address1;
    private String            address2;
    private String            address3;
    private String            tel1;
    private String            tel2;
    private int               reminderFlag;
    private String            mailAddr;
    private String            demands;
    private String            remarks;
    private int               acceptDate;
    private int               acceptTime;
    private int               status;
    private int               basicChargeTotal;
    private int               optionChargeTotal;
    private int               chargeTotal;
    private int               addPoint;
    private int               comingFlag;
    private String            hotelName;
    private int               nowShowFlag;
    private int               parking;
    private int               parkingCount;
    private int               parkingHiRoofCount;
    private int               ciTimeFrom;
    private int               ciTimeTo;
    private int               coTime;
    private int               tempComingFlag;
    private int               noDispFlag;
    private String            tranid;
    private String            cancelTranid;
    private String            ownerHotelId;
    private int               ownerUserId;
    private int               coKind;
    private int               cancelKind;
    private String            mailAddr1;
    private String            mailAddr2;
    private String            userAgent;
    private int               payment;
    private int               paymentStatus;
    private String            consumerDemands;
    private int               addBonusMile;
    private int               usedMile;
    private String            reserveNoMain;
    private int               reserveDateTo;
    private int               extFlag;
    private int               cancelCharge;
    private int               basicChargeTotalAll;
    private int               optionChargeTotalAll;
    private int               chargeTotalAll;
    private int               cancelDate;
    private int               cancelCreditStatus;
    private int               roomHold;
    private int               otaCd;
    private String            otaBookingCode;
    private float             otaTotalAmountOfTaxes;
    private float             otaTotalAmountAfterTaxes;
    private String            otaCurrency;

    /**
     * データの初期化
     */
    public DataRsvReserve()
    {
        iD = 0;
        reserveNo = "";
        reserveSubNo = 0;
        planId = 0;
        planSubId = 0;
        userId = "";
        reserveDate = 0;
        seq = 0;
        estTimeArrival = 0;
        numAdult = 0;
        numChild = 0;
        numMan = 0;
        numWoman = 0;
        nameLast = "";
        nameFirst = "";
        nameLastKana = "";
        nameFirstKana = "";
        zipCode = "";
        prefCode = 0;
        jisCode = 0;
        address1 = "";
        address2 = "";
        address3 = "";
        tel1 = "";
        tel2 = "";
        reminderFlag = 0;
        mailAddr = "";
        demands = "";
        remarks = "";
        acceptDate = 0;
        acceptTime = 0;
        status = 0;
        basicChargeTotal = 0;
        optionChargeTotal = 0;
        chargeTotal = 0;
        addPoint = 0;
        comingFlag = 0;
        hotelName = "";
        nowShowFlag = 0;
        parking = 0;
        parkingCount = 0;
        parkingHiRoofCount = 0;
        ciTimeFrom = 0;
        ciTimeTo = 0;
        coTime = 0;
        tempComingFlag = 0;
        noDispFlag = 0;
        tranid = "";
        cancelTranid = "";
        ownerHotelId = "";
        ownerUserId = 0;
        coKind = 0;
        cancelKind = 0;
        mailAddr1 = "";
        mailAddr2 = "";
        userAgent = "";
        payment = 0;
        paymentStatus = 0;
        consumerDemands = "";
        addBonusMile = 0;
        usedMile = 0;
        reserveNoMain = "";
        reserveDateTo = 0;
        extFlag = 0;
        cancelCharge = 0;
        basicChargeTotalAll = 0;
        optionChargeTotalAll = 0;
        chargeTotalAll = 0;
        cancelDate = 0;
        cancelCreditStatus = 0;
        roomHold = 0;
        otaCd = 0;
        otaBookingCode = "";
        otaTotalAmountAfterTaxes = 0;
        otaTotalAmountOfTaxes = 0;
        otaCurrency = "";
    }

    /*****/

    public int getCancelKind()
    {
        return cancelKind;
    }

    public String getMailAddr1()
    {
        return mailAddr1;
    }

    public String getMailAddr2()
    {
        return mailAddr2;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setCancelKind(int cancelKind)
    {
        this.cancelKind = cancelKind;
    }

    public void setMailAddr1(String mailAddr1)
    {
        this.mailAddr1 = mailAddr1;
    }

    public void setMailAddr2(String mailAddr2)
    {
        this.mailAddr2 = mailAddr2;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    /******/

    /**
     * 
     * getter
     * 
     */
    public int getAcceptDate()
    {
        return this.acceptDate;
    }

    public int getAcceptTime()
    {
        return this.acceptTime;
    }

    public int getAddPoint()
    {
        return this.addPoint;
    }

    public String getAddress1()
    {
        return this.address1;
    }

    public String getAddress2()
    {
        return this.address2;
    }

    public String getAddress3()
    {
        return this.address3;
    }

    public int getBasicChargeTotal()
    {
        return this.basicChargeTotal;
    }

    public int getCiTimeFrom()
    {
        return this.ciTimeFrom;
    }

    public int getCiTimeTo()
    {
        return this.ciTimeTo;
    }

    public int getCoTime()
    {
        return this.coTime;
    }

    public int getCoKind()
    {
        return coKind;
    }

    public int getChargeTotal()
    {
        return this.chargeTotal;
    }

    public int getComingFlag()
    {
        return this.comingFlag;
    }

    public String getDemands()
    {
        return this.demands;
    }

    public int getEstTimeArrival()
    {
        return this.estTimeArrival;
    }

    public String getHotelName()
    {
        return this.hotelName;
    }

    public int getiD()
    {
        return iD;
    }

    public int getID()
    {
        return this.iD;
    }

    public int getJisCode()
    {
        return this.jisCode;
    }

    public String getMailAddr()
    {
        return this.mailAddr;
    }

    public String getNameFirst()
    {
        return this.nameFirst;
    }

    public String getNameFirstKana()
    {
        return this.nameFirstKana;
    }

    public String getNameLast()
    {
        return this.nameLast;
    }

    public String getNameLastKana()
    {
        return this.nameLastKana;
    }

    public int getNowShowFlag()
    {
        return this.nowShowFlag;
    }

    public int getNumAdult()
    {
        return this.numAdult;
    }

    public int getNumChild()
    {
        return this.numChild;
    }

    public int getOptionChargeTotal()
    {
        return this.optionChargeTotal;
    }

    public int getParking()
    {
        return this.parking;
    }

    public int getParkingCount()
    {
        return this.parkingCount;
    }

    public int getPlanId()
    {
        return this.planId;
    }

    public int getPlanSubId()
    {
        return this.planSubId;
    }

    public int getPrefCode()
    {
        return this.prefCode;
    }

    public String getRemarks()
    {
        return this.remarks;
    }

    public int getReminderFlag()
    {
        return this.reminderFlag;
    }

    public int getReserveDate()
    {
        return this.reserveDate;
    }

    public String getReserveNo()
    {
        return this.reserveNo;
    }

    public int getReserveSubNo()
    {
        return reserveSubNo;
    }

    public int getReserveNoSub()
    {
        return this.reserveSubNo;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public int getStatus()
    {
        return this.status;
    }

    public String getTel1()
    {
        return this.tel1;
    }

    public String getTel2()
    {
        return this.tel2;
    }

    public String getUserId()
    {
        return this.userId;
    }

    public String getZipCd()
    {
        return this.zipCode;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public int getTempComingFlag()
    {
        return this.tempComingFlag;
    }

    public int getNoDispFlag()
    {
        return noDispFlag;
    }

    public String getTranid()
    {
        return tranid;
    }

    public String getCancelTranid()
    {
        return cancelTranid;
    }

    public int getParkingHiRoofCount()
    {
        return parkingHiRoofCount;
    }

    public int getNumMan()
    {
        return numMan;
    }

    public int getNumWoman()
    {
        return numWoman;
    }

    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    public int getOwnerUserId()
    {
        return ownerUserId;
    }

    public int getPayment()
    {
        return payment;
    }

    public int getPaymentStatus()
    {
        return paymentStatus;
    }

    public String getConsumerDemands()
    {
        return consumerDemands;
    }

    public int getAddBonusMile()
    {
        return addBonusMile;
    }

    public int getUsedMile()
    {
        return usedMile;
    }

    public String getReserveNoMain()
    {
        return reserveNoMain;
    }

    public int getReserveDateTo()
    {
        return reserveDateTo;
    }

    public int getExtFlag()
    {
        return extFlag;
    }

    public int getCancelCharge()
    {
        return cancelCharge;
    }

    public int getBasicChargeTotalAll()
    {
        return basicChargeTotalAll;
    }

    public int getOptionChargeTotalAll()
    {
        return optionChargeTotalAll;
    }

    public int getChargeTotalAll()
    {
        return chargeTotalAll;
    }

    public int getCancelDate()
    {
        return cancelDate;
    }

    public int getCancelCreditStatus()
    {
        return cancelCreditStatus;
    }

    public int getRoomHold()
    {
        return roomHold;
    }

    public int getOtaCd()
    {
        return otaCd;
    }

    public String getOtaBookingCode()
    {
        return otaBookingCode;
    }

    public float getOtaTotalAmountAfterTaxes()
    {
        return otaTotalAmountAfterTaxes;
    }

    public float getOtaTotalAmountOfTaxes()
    {
        return otaTotalAmountOfTaxes;
    }

    public String getOtaCurrency()
    {
        return otaCurrency;
    }

    /**
     * 
     * setter
     * 
     */
    public void setAcceptDate(int acceptDate)
    {
        this.acceptDate = acceptDate;
    }

    public void setAcceptTime(int acceptTime)
    {
        this.acceptTime = acceptTime;
    }

    public void setAddPoint(int addPoint)
    {
        this.addPoint = addPoint;
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

    public void setBasicChargeTotal(int basicChargeTotal)
    {
        this.basicChargeTotal = basicChargeTotal;
    }

    public void setChargeTotal(int chargeTotal)
    {
        this.chargeTotal = chargeTotal;
    }

    public void setCiTimeFrom(int citimefrom)
    {
        this.ciTimeFrom = citimefrom;
    }

    public void setCiTimeTo(int citimeto)
    {
        this.ciTimeTo = citimeto;
    }

    public void setCoTime(int cotime)
    {
        this.coTime = cotime;
    }

    public void setCoKind(int coKind)
    {
        this.coKind = coKind;
    }

    public void setComingFlag(int comingFlag)
    {
        this.comingFlag = comingFlag;
    }

    public void setDemands(String demands)
    {
        this.demands = demands;
    }

    public void setEstTimeArrival(int estTimeArrival)
    {
        this.estTimeArrival = estTimeArrival;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public void setiD(int iD)
    {
        this.iD = iD;
    }

    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setNameFirst(String nameFirst)
    {
        this.nameFirst = nameFirst;
    }

    public void setNameFirstKana(String nameFirstKana)
    {
        this.nameFirstKana = nameFirstKana;
    }

    public void setNameLast(String nameLast)
    {
        this.nameLast = nameLast;
    }

    public void setNameLastKana(String nameLastKana)
    {
        this.nameLastKana = nameLastKana;
    }

    public void setNowShowFlag(int nowShowFlag)
    {
        this.nowShowFlag = nowShowFlag;
    }

    public void setNumAdult(int numAdult)
    {
        this.numAdult = numAdult;
    }

    public void setNumChild(int numChild)
    {
        this.numChild = numChild;
    }

    public void setOptionChargeTotal(int optionChargeTotal)
    {
        this.optionChargeTotal = optionChargeTotal;
    }

    public void setParking(int parking)
    {
        this.parking = parking;
    }

    public void setParkingCount(int parkingCount)
    {
        this.parkingCount = parkingCount;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public void setPlanSubId(int planSubId)
    {
        this.planSubId = planSubId;
    }

    public void setPrefCode(int prefCode)
    {
        this.prefCode = prefCode;
    }

    public void setRemaindFlag(int reminderFlag)
    {
        this.reminderFlag = reminderFlag;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public void setReminderFlag(int reminderFlag)
    {
        this.reminderFlag = reminderFlag;
    }

    public void setReserveDate(int reserveDate)
    {
        this.reserveDate = reserveDate;
    }

    public void setReserveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    public void setReserveSubNo(int reserveSubNo)
    {
        this.reserveSubNo = reserveSubNo;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public void setZipCd(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setTempComingFlag(int tempComingFlag)
    {
        this.tempComingFlag = tempComingFlag;
    }

    public void setNoDispFlag(int noDispFlag)
    {
        this.noDispFlag = noDispFlag;
    }

    public void setTranid(String tranid)
    {
        this.tranid = tranid;
    }

    public void setCancelTranid(String cancelTranid)
    {
        this.cancelTranid = cancelTranid;
    }

    public void setParkingHiRoofCount(int parkingHiRoofCount)
    {
        this.parkingHiRoofCount = parkingHiRoofCount;
    }

    public void setNumMan(int numMan)
    {
        this.numMan = numMan;
    }

    public void setNumWoman(int numWoman)
    {
        this.numWoman = numWoman;
    }

    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    public void setOwnerUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public void setPayment(int payment)
    {
        this.payment = payment;
    }

    public void setPaymentStatus(int paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    public void setConsumerDemands(String consumerDemands)
    {
        this.consumerDemands = consumerDemands;
    }

    public void setAddBonusMile(int addBonusMile)
    {
        this.addBonusMile = addBonusMile;
    }

    public void setUsedMile(int usedMile)
    {
        this.usedMile = usedMile;
    }

    public void setReserveNoMain(String reserveNoMain)
    {
        this.reserveNoMain = reserveNoMain;
    }

    public void setReserveDateTo(int reserveDateTo)
    {
        this.reserveDateTo = reserveDateTo;
    }

    public void setExtFlag(int extFlag)
    {
        this.extFlag = extFlag;
    }

    public void setCancelCharge(int cancelCharge)
    {
        this.cancelCharge = cancelCharge;
    }

    public void setBasicChargeTotalAll(int basicChargeTotalAll)
    {
        this.basicChargeTotalAll = basicChargeTotalAll;
    }

    public void setOptionChargeTotalAll(int optionChargeTotalAll)
    {
        this.optionChargeTotalAll = optionChargeTotalAll;
    }

    public void setChargeTotalAll(int chargeTotalAll)
    {
        this.chargeTotalAll = chargeTotalAll;
    }

    public void setCancelDate(int cancelDate)
    {
        this.cancelDate = cancelDate;
    }

    public void setCancelCreditStatus(int cancelCreditStatus)
    {
        this.cancelCreditStatus = cancelCreditStatus;
    }

    public void setRoomHold(int roomHold)
    {
        this.roomHold = roomHold;
    }

    public void setOtaCd(int otaCd)
    {
        this.otaCd = otaCd;
    }

    public void setOtaBookingCode(String otaBookingCode)
    {
        this.otaBookingCode = otaBookingCode;
    }

    public void setOtaTotalAmountAfterTaxes(float otaTotalAmountAfterTaxes)
    {
        this.otaTotalAmountAfterTaxes = otaTotalAmountAfterTaxes;
    }

    public void setOtaTotalAmountOfTaxes(float otaTotalAmountOfTaxes)
    {
        this.otaTotalAmountOfTaxes = otaTotalAmountOfTaxes;
    }

    public void setOtaCurrency(String otaCurrency)
    {
        this.otaCurrency = otaCurrency;
    }

    /**
     * 予約情報取得
     * Connection
     * 
     * @param iD ホテルID
     * @param reserveNo 予約番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, String reserveNo)
    {
        boolean ret = false;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            getReserve( connection, Id, reserveNo );
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 予約外部フラグ取得
     * 
     * @param id ホテルID
     * @param reserveNo 予約番号
     * @return
     */
    public static int getExtFlag(int id, String reserveNo)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int ext_flag = 0;
        query = "SELECT reserve.ext_flag FROM newRsvDB.hh_rsv_reserve reserve" +
                " WHERE reserve.id = ? AND reserve.reserve_no = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setString( 2, reserveNo );
            result = prestate.executeQuery();
            if ( result.next() != false )
            {
                ext_flag = result.getInt( "ext_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserve.getExtFlag] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ext_flag;
    }

    /**
     * 予約情報取得
     * Connection
     * 
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getDataByUserId(String userId)
    {
        boolean ret = false;
        Connection connection = null;

        try
        {
            connection = DBConnection.getConnection();
            getReserveByUserId( connection, userId );
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 予約情報取得
     * Connection
     * 
     * @param iD ホテルID
     * @param reserveNo 予約番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(Connection connection, int Id, String reserveNo)
    {
        boolean ret = false;

        try
        {
            getReserve( connection, Id, reserveNo );
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.getData Connection] Exception=" + e.toString() );
        }
        return(ret);
    }

    /**
     * 予約情報取得
     * 
     * @param connection Connection
     * @param userId ユーザID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    private void getReserveByUserId(Connection connection, String userId)
    {
        String query;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT id, reserve_no, reserve_sub_no, plan_id, user_id," +
                " reserve_date, seq, est_time_arrival, num_adult, num_child," +
                " num_man, num_woman, " +
                " name_last, name_first, name_last_kana, name_first_kana," +
                " zip_code, pref_code, jis_code, address1, address2, address3," +
                " tel1, tel2, reminder_flag,mail_addr, demands," +
                " remarks, accept_date, accept_time,status, basic_charge_total," +
                " option_charge_total, charge_total,add_point, coming_flag," +
                " hotel_name, noshow_flag, parking,parking_count, parking_high_roof_count, " +
                " ci_time_from, ci_time_to, co_time, temp_coming_flag, no_disp_flag," +
                " owner_hotel_id, owner_user_id, co_kind, cancel_kind, mail_addr1, mail_addr2, user_agent " +
                " FROM hh_rsv_reserve WHERE user_id = ? ORDER BY accept_date DESC, accept_time DESC" +
                " LIMIT 0,1";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.reserveNo = result.getString( "reserve_no" );
                    this.reserveSubNo = result.getInt( "reserve_sub_no" );
                    this.planId = result.getInt( "plan_id" );
                    this.userId = result.getString( "user_id" );
                    this.reserveDate = result.getInt( "reserve_date" );
                    this.seq = result.getInt( "seq" );
                    this.estTimeArrival = result.getInt( "est_time_arrival" );
                    this.numAdult = result.getInt( "num_adult" );
                    this.numChild = result.getInt( "num_child" );
                    this.numMan = result.getInt( "num_man" );
                    this.numWoman = result.getInt( "num_woman" );
                    this.nameLast = result.getString( "name_last" );
                    this.nameFirst = result.getString( "name_first" );
                    this.nameLastKana = result.getString( "name_last_kana" );
                    this.nameFirstKana = result.getString( "name_first_kana" );
                    this.zipCode = result.getString( "zip_code" );
                    this.prefCode = result.getInt( "pref_code" );
                    this.jisCode = result.getInt( "jis_code" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.address3 = result.getString( "address3" );
                    this.tel1 = result.getString( "tel1" );
                    this.tel2 = result.getString( "tel2" );
                    this.reminderFlag = result.getInt( "reminder_flag" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.demands = result.getString( "demands" );
                    this.remarks = result.getString( "remarks" );
                    this.acceptDate = result.getInt( "accept_date" );
                    this.acceptTime = result.getInt( "accept_time" );
                    this.status = result.getInt( "status" );
                    this.basicChargeTotal = result.getInt( "basic_charge_total" );
                    this.optionChargeTotal = result.getInt( "option_charge_total" );
                    this.chargeTotal = result.getInt( "charge_total" );
                    this.addPoint = result.getInt( "add_point" );
                    this.comingFlag = result.getInt( "coming_flag" );
                    this.hotelName = result.getString( "hotel_name" );
                    this.nowShowFlag = result.getInt( "noshow_flag" );
                    this.parking = result.getInt( "parking" );
                    this.parkingCount = result.getInt( "parking_count" );
                    this.parkingHiRoofCount = result.getInt( "parking_high_roof_count" );
                    this.ciTimeFrom = result.getInt( "ci_time_from" );
                    this.ciTimeTo = result.getInt( "ci_time_to" );
                    this.coTime = result.getInt( "co_time" );
                    this.tempComingFlag = result.getInt( "temp_coming_flag" );
                    this.noDispFlag = result.getInt( "no_disp_flag" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.coKind = result.getInt( "co_kind" );
                    this.cancelKind = result.getInt( "cancelKind" );
                    this.mailAddr1 = result.getString( "mail_addr1" );
                    this.mailAddr2 = result.getString( "mail_addr2" );
                    this.userAgent = result.getString( "user_agent" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 予約情報取得
     * 
     * @param connection Connection
     * @param iD ホテルID
     * @param reserveNo 予約番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    private void getReserve(Connection connection, int Id, String reserveNo)
    {
        String query;
        PreparedStatement prestate = null;
        ResultSet result = null;

        query = "SELECT id, reserve_no, reserve_sub_no, plan_id, plan_sub_id,user_id," +
                " reserve_date, seq, est_time_arrival, num_adult, num_child," +
                " num_man, num_woman, " +
                " name_last, name_first, name_last_kana, name_first_kana," +
                " zip_code, pref_code, jis_code, address1, address2, address3," +
                " tel1, tel2, reminder_flag,mail_addr, demands," +
                " remarks, accept_date, accept_time,status, basic_charge_total," +
                " option_charge_total, charge_total,add_point, coming_flag," +
                " hotel_name, noshow_flag, parking,parking_count, highroof_count AS parking_high_roof_count, " +
                " ci_time_from, ci_time_to, co_time, temp_coming_flag, no_disp_flag," +
                " owner_hotel_id, owner_user_id, co_kind, cancel_type AS cancel_kind,  mail_addr1, mail_addr2, user_agent, " +
                " payment,payment_status,consumer_demands,add_bonus_mile,used_mile, " +
                " reserve_no_main, reserve_date_to, ext_flag, cancel_charge, basic_charge_total_all, option_charge_total_all, charge_total_all, " +
                " cancel_date, cancel_credit_status,room_hold, " +
                " ota_cd, " +
                " ota_booking_code, " +
                " ota_total_amount_after_taxes, " +
                " ota_total_amount_of_taxes, " +
                " ota_currency " +
                " FROM newRsvDB.hh_rsv_reserve WHERE id = ? AND reserve_no = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setString( 2, reserveNo );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.iD = result.getInt( "id" );
                    this.reserveNo = result.getString( "reserve_no" );
                    this.reserveSubNo = result.getInt( "reserve_sub_no" );
                    this.planId = result.getInt( "plan_id" );
                    this.planSubId = result.getInt( "plan_sub_id" );
                    this.userId = result.getString( "user_id" );
                    this.reserveDate = result.getInt( "reserve_date" );
                    this.seq = result.getInt( "seq" );
                    this.estTimeArrival = result.getInt( "est_time_arrival" );
                    this.numAdult = result.getInt( "num_adult" );
                    this.numChild = result.getInt( "num_child" );
                    this.numMan = result.getInt( "num_man" );
                    this.numWoman = result.getInt( "num_woman" );
                    this.nameLast = result.getString( "name_last" );
                    this.nameFirst = result.getString( "name_first" );
                    this.nameLastKana = result.getString( "name_last_kana" );
                    this.nameFirstKana = result.getString( "name_first_kana" );
                    this.zipCode = result.getString( "zip_code" );
                    this.prefCode = result.getInt( "pref_code" );
                    this.jisCode = result.getInt( "jis_code" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.address3 = result.getString( "address3" );
                    this.tel1 = result.getString( "tel1" );
                    this.tel2 = result.getString( "tel2" );
                    this.reminderFlag = result.getInt( "reminder_flag" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.demands = result.getString( "demands" );
                    this.remarks = result.getString( "remarks" );
                    this.acceptDate = result.getInt( "accept_date" );
                    this.acceptTime = result.getInt( "accept_time" );
                    this.status = result.getInt( "status" );
                    this.basicChargeTotal = result.getInt( "basic_charge_total" );
                    this.optionChargeTotal = result.getInt( "option_charge_total" );
                    this.chargeTotal = result.getInt( "charge_total" );
                    this.addPoint = result.getInt( "add_point" );
                    this.comingFlag = result.getInt( "coming_flag" );
                    this.hotelName = result.getString( "hotel_name" );
                    this.nowShowFlag = result.getInt( "noshow_flag" );
                    this.parking = result.getInt( "parking" );
                    this.parkingCount = result.getInt( "parking_count" );
                    this.parkingHiRoofCount = result.getInt( "parking_high_roof_count" );
                    this.ciTimeFrom = result.getInt( "ci_time_from" );
                    this.ciTimeTo = result.getInt( "ci_time_to" );
                    this.coTime = result.getInt( "co_time" );
                    this.tempComingFlag = result.getInt( "temp_coming_flag" );
                    this.noDispFlag = result.getInt( "no_disp_flag" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.coKind = result.getInt( "co_kind" );
                    this.cancelKind = result.getInt( "cancel_kind" );
                    this.mailAddr1 = result.getString( "mail_addr1" );
                    this.mailAddr2 = result.getString( "mail_addr2" );
                    this.userAgent = result.getString( "user_agent" );
                    this.payment = result.getInt( "payment" );
                    this.paymentStatus = result.getInt( "payment_status" );
                    this.consumerDemands = result.getString( "consumer_demands" );
                    this.addBonusMile = result.getInt( "add_bonus_mile" );
                    this.usedMile = result.getInt( "used_mile" );
                    this.reserveNoMain = result.getString( "reserve_no_main" );
                    this.reserveDateTo = result.getInt( "reserve_date_to" );
                    this.extFlag = result.getInt( "ext_flag" );
                    this.cancelCharge = result.getInt( "cancel_charge" );
                    this.basicChargeTotalAll = result.getInt( "basic_charge_total_all" );
                    this.optionChargeTotalAll = result.getInt( "option_charge_total_all" );
                    this.chargeTotalAll = result.getInt( "charge_total_all" );
                    this.cancelDate = result.getInt( "cancel_date" );
                    this.cancelCreditStatus = result.getInt( "cancel_credit_status" );
                    this.roomHold = result.getInt( "room_hold" );
                    this.otaCd = result.getInt( "ota_cd" );
                    this.otaBookingCode = result.getString( "ota_booking_code" );
                    this.otaTotalAmountAfterTaxes = result.getFloat( "ota_total_amount_after_taxes" );
                    this.otaTotalAmountOfTaxes = result.getFloat( "ota_total_amount_of_taxes" );
                    this.otaCurrency = result.getString( "ota_currency" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.getReserve] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }

    /**
     * 予約データ設定
     * 
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        String strVal;
        String strVal2;

        try
        {
            if ( result != null )
            {
                this.iD = result.getInt( "id" );
                this.reserveNo = result.getString( "reserve_no" );
                this.reserveSubNo = result.getInt( "reserve_sub_no" );
                this.planId = result.getInt( "plan_id" );
                this.userId = result.getString( "user_id" );
                strVal = result.getString( "reserve_date" );
                strVal2 = strVal.substring( 1, 4 ) + strVal.substring( 6, 2 ) + strVal.substring( 10, 2 );
                this.reserveDate = Integer.valueOf( strVal2 );

                this.seq = result.getInt( "seq" );

                strVal = result.getString( "est_time_arrival" );
                strVal2 = strVal.substring( 1, 2 ) + strVal.substring( 4, 2 ) + "00";
                this.estTimeArrival = Integer.valueOf( strVal2 );

                this.numAdult = result.getInt( "num_adult" );
                this.numChild = result.getInt( "num_child" );
                this.numMan = result.getInt( "num_man" );
                this.numWoman = result.getInt( "num_woman" );
                this.nameLast = result.getString( "name_last" );
                this.nameFirst = result.getString( "name_first" );
                this.nameLastKana = result.getString( "name_last_kana" );
                this.nameFirstKana = result.getString( "name_first_kana" );
                this.zipCode = result.getString( "zip3" ) + result.getString( "zip4" );
                this.prefCode = result.getInt( "pref_code" );
                this.jisCode = result.getInt( "jis_code" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.address3 = result.getString( "address3" );
                this.tel1 = result.getString( "tel1" );
                this.tel2 = result.getString( "tel2" );
                this.reminderFlag = result.getInt( "remainder_flag" );
                this.mailAddr = result.getString( "mail_addr" );
                this.demands = result.getString( "demands" );
                this.remarks = result.getString( "remarks" );

                this.acceptDate = result.getInt( "accept_date" );
                this.acceptTime = result.getInt( "accept_time" );
                this.status = result.getInt( "status" );
                this.basicChargeTotal = result.getInt( "basic_charge_total" );
                this.optionChargeTotal = result.getInt( "option_charge_total" );
                this.chargeTotal = result.getInt( "charge_total" );
                this.addPoint = result.getInt( "add_point" );
                this.comingFlag = result.getInt( "coming_flag" );

                this.hotelName = result.getString( "hotel_name" );

                this.nowShowFlag = result.getInt( "now_show_flag" );
                this.parking = result.getInt( "parking" );
                this.parkingCount = result.getInt( "parkingCount" );
                this.parkingHiRoofCount = result.getInt( "parking_high_roof_count" );
                this.ciTimeFrom = result.getInt( "ci_time_from" );
                this.ciTimeTo = result.getInt( "ci_time_to" );
                this.coTime = result.getInt( "co_time" );
                this.comingFlag = result.getInt( "temp_coming_flag" );
                this.noDispFlag = result.getInt( "no_disp_flag" );
                this.ownerHotelId = result.getString( "owner_hotel_id" );
                this.ownerUserId = result.getInt( "owner_user_id" );
                this.coKind = result.getInt( "co_kind" );
                this.cancelKind = result.getInt( "cancel_kind" );
                this.mailAddr1 = result.getString( "mail_addr1" );
                this.mailAddr2 = result.getString( "mail_addr2" );
                this.userAgent = result.getString( "user_agent" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * 予約データ登録
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_reserve SET " +
                "  id = ?" +
                ", reserve_no = ?" +
                ", reserve_sub_no = ? " +
                ", plan_id = ? " +
                ", user_id = ? " +
                ", reserve_date = ? " +
                ", seq = ? " +
                ", est_time_arrival = ? " +
                ", num_adult = ? " +
                ", num_child = ? " +
                ", name_last = ? " +
                ", name_first = ? " +
                ", name_last_kana = ? " +
                ", name_first_kana = ? " +
                ", zip_code = ? " +
                ", pref_code = ? " +
                ", jis_code = ? " +
                ", address1 = ? " +
                ", address2 = ? " +
                ", address3 = ? " +
                ", tel1 = ? " +
                ", tel2 = ? " +
                ", reminder_flag = ? " +
                ", mail_addr = ? " +
                ", demands = ? " +
                ", remarks = ? " +
                ", accept_date = ? " +
                ", accept_time = ? " +
                ", status = ? " +
                ", basic_charge_total = ? " +
                ", option_charge_total = ? " +
                ", charge_total = ? " +
                ", add_point = ? " +
                ", coming_flag = ? " +
                ", hotel_name = ? " +
                ", noshow_flag = ? " +
                ", parking = ? " +
                ", parking_count = ? " +
                ", parking_high_roof_count = ? " +
                ", ci_time_from = ? " +
                ", ci_time_to = ? " +
                ", co_time = ? " +
                ", temp_coming_flag = ? " +
                ", no_disp_flag = ? " +
                ", num_man = ? " +
                ", num_woman = ? " +
                ", owner_hotel_id = ? " +
                ", owner_user_id = ? " +
                ", co_kind = ? " +
                ", cancel_kind = ? " +
                ", mail_addr1 = ?" +
                ", mail_addr2 = ?" +
                ", user_agent = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.iD );
            prestate.setString( 2, this.reserveNo );
            prestate.setInt( 3, this.reserveSubNo );
            prestate.setInt( 4, this.planId );
            prestate.setString( 5, this.userId );
            prestate.setInt( 6, this.reserveDate );
            prestate.setInt( 7, this.seq );
            prestate.setInt( 8, this.estTimeArrival );
            prestate.setInt( 9, this.numAdult );
            prestate.setInt( 10, this.numChild );
            prestate.setString( 11, this.nameLast );
            prestate.setString( 12, this.nameFirst );
            prestate.setString( 13, this.nameLastKana );
            prestate.setString( 14, this.nameFirstKana );
            prestate.setString( 15, this.zipCode );
            prestate.setInt( 16, this.prefCode );
            prestate.setInt( 17, this.jisCode );
            prestate.setString( 18, this.address1 );
            prestate.setString( 19, this.address2 );
            prestate.setString( 20, this.address3 );
            prestate.setString( 21, this.tel1 );
            prestate.setString( 22, this.tel2 );
            prestate.setInt( 23, this.reminderFlag );
            prestate.setString( 24, this.mailAddr );
            prestate.setString( 25, this.demands );
            prestate.setString( 26, this.remarks );
            prestate.setInt( 27, this.acceptDate );
            prestate.setInt( 28, this.acceptTime );
            prestate.setInt( 29, this.status );
            prestate.setInt( 30, this.basicChargeTotal );
            prestate.setInt( 31, this.optionChargeTotal );
            prestate.setInt( 32, this.chargeTotal );
            prestate.setInt( 33, this.addPoint );
            prestate.setInt( 34, this.comingFlag );
            prestate.setString( 35, this.hotelName );
            prestate.setInt( 36, this.nowShowFlag );
            prestate.setInt( 37, this.parking );
            prestate.setInt( 38, this.parkingCount );
            prestate.setInt( 39, this.parkingHiRoofCount );
            prestate.setInt( 40, this.ciTimeFrom );
            prestate.setInt( 41, this.ciTimeTo );
            prestate.setInt( 42, this.coTime );
            prestate.setInt( 43, this.tempComingFlag );
            prestate.setInt( 44, this.noDispFlag );
            prestate.setInt( 45, this.numMan );
            prestate.setInt( 46, this.numWoman );
            prestate.setString( 47, this.ownerHotelId );
            prestate.setInt( 48, this.ownerUserId );
            prestate.setInt( 49, this.coKind );
            prestate.setInt( 50, this.cancelKind );
            prestate.setString( 51, this.mailAddr1 );
            prestate.setString( 52, this.mailAddr2 );
            prestate.setString( 53, this.userAgent );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 予約データ登録
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection conn)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT INTO hh_rsv_reserve SET " +
                "  id = ?" +
                ", reserve_no = ?" +
                ", reserve_sub_no = ? " +
                ", plan_id = ? " +
                ", user_id = ? " +
                ", reserve_date = ? " +
                ", seq = ? " +
                ", est_time_arrival = ? " +
                ", num_adult = ? " +
                ", num_child = ? " +
                ", name_last = ? " +
                ", name_first = ? " +
                ", name_last_kana = ? " +
                ", name_first_kana = ? " +
                ", zip_code = ? " +
                ", pref_code = ? " +
                ", jis_code = ? " +
                ", address1 = ? " +
                ", address2 = ? " +
                ", address3 = ? " +
                ", tel1 = ? " +
                ", tel2 = ? " +
                ", reminder_flag = ? " +
                ", mail_addr = ? " +
                ", demands = ? " +
                ", remarks = ? " +
                ", accept_date = ? " +
                ", accept_time = ? " +
                ", status = ? " +
                ", basic_charge_total = ? " +
                ", option_charge_total = ? " +
                ", charge_total = ? " +
                ", add_point = ? " +
                ", coming_flag = ? " +
                ", hotel_name = ? " +
                ", noshow_flag = ? " +
                ", parking = ? " +
                ", parking_count = ? " +
                ", parking_high_roof_count = ? " +
                ", ci_time_from = ? " +
                ", ci_time_to = ? " +
                ", co_time = ? " +
                ", temp_coming_flag = ? " +
                ", no_disp_flag = ? " +
                ", tranid = ? " +
                ", cancel_tranid = ? " +
                ", num_man = ? " +
                ", num_woman = ? " +
                ", owner_hotel_id = ? " +
                ", owner_user_id = ? " +
                ", co_kind = ? " +
                ", cancel_kind = ? " +
                ", mail_addr1 = ?" +
                ", mail_addr2 = ?" +
                ", user_agent = ?";

        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.iD );
            prestate.setString( 2, this.reserveNo );
            prestate.setInt( 3, this.reserveSubNo );
            prestate.setInt( 4, this.planId );
            prestate.setString( 5, this.userId );
            prestate.setInt( 6, this.reserveDate );
            prestate.setInt( 7, this.seq );
            prestate.setInt( 8, this.estTimeArrival );
            prestate.setInt( 9, this.numAdult );
            prestate.setInt( 10, this.numChild );
            prestate.setString( 11, this.nameLast );
            prestate.setString( 12, this.nameFirst );
            prestate.setString( 13, this.nameLastKana );
            prestate.setString( 14, this.nameFirstKana );
            prestate.setString( 15, this.zipCode );
            prestate.setInt( 16, this.prefCode );
            prestate.setInt( 17, this.jisCode );
            prestate.setString( 18, this.address1 );
            prestate.setString( 19, this.address2 );
            prestate.setString( 20, this.address3 );
            prestate.setString( 21, this.tel1 );
            prestate.setString( 22, this.tel2 );
            prestate.setInt( 23, this.reminderFlag );
            prestate.setString( 24, this.mailAddr );
            prestate.setString( 25, this.demands );
            prestate.setString( 26, this.remarks );
            prestate.setInt( 27, this.acceptDate );
            prestate.setInt( 28, this.acceptTime );
            prestate.setInt( 29, this.status );
            prestate.setInt( 30, this.basicChargeTotal );
            prestate.setInt( 31, this.optionChargeTotal );
            prestate.setInt( 32, this.chargeTotal );
            prestate.setInt( 33, this.addPoint );
            prestate.setInt( 34, this.comingFlag );
            prestate.setString( 35, this.hotelName );
            prestate.setInt( 36, this.nowShowFlag );
            prestate.setInt( 37, this.parking );
            prestate.setInt( 38, this.parkingCount );
            prestate.setInt( 39, this.parkingHiRoofCount );
            prestate.setInt( 40, this.ciTimeFrom );
            prestate.setInt( 41, this.ciTimeTo );
            prestate.setInt( 42, this.coTime );
            prestate.setInt( 43, this.tempComingFlag );
            prestate.setInt( 44, this.noDispFlag );
            prestate.setString( 45, this.tranid );
            prestate.setString( 46, this.cancelTranid );
            prestate.setInt( 47, this.numMan );
            prestate.setInt( 48, this.numWoman );
            prestate.setString( 49, this.ownerHotelId );
            prestate.setInt( 50, this.ownerUserId );
            prestate.setInt( 51, this.coKind );
            prestate.setInt( 52, this.cancelKind );
            prestate.setString( 53, this.mailAddr1 );
            prestate.setString( 54, this.mailAddr2 );
            prestate.setString( 55, this.userAgent );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 予約データ更新
     * 
     * @param iD ホテルID
     * @param reserveNo 予約番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int Id, String reserveNo)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_reserve SET " +
                "  reserve_sub_no = ? " +
                ", plan_id = ? " +
                ", user_id = ? " +
                ", reserve_date = ? " +
                ", seq = ? " +
                ", est_time_arrival = ? " +
                ", num_adult = ? " +
                ", num_child = ? " +
                ", name_last = ? " +
                ", name_first = ? " +
                ", name_last_kana = ? " +
                ", name_first_kana = ? " +
                ", zip_code = ? " +
                ", pref_code = ? " +
                ", jis_code = ? " +
                ", address1 = ? " +
                ", address2 = ? " +
                ", address3 = ? " +
                ", tel1 = ? " +
                ", tel2 = ? " +
                ", reminder_flag = ? " +
                ", mail_addr = ? " +
                ", demands = ? " +
                ", remarks = ? " +
                ", accept_date = ? " +
                ", accept_time = ? " +
                ", status = ? " +
                ", basic_charge_total = ? " +
                ", option_charge_total = ? " +
                ", charge_total = ? " +
                ", add_point = ? " +
                ", coming_flag = ? " +
                ", hotel_name = ? " +
                ", noshow_flag = ? " +
                ", parking = ? " +
                ", parking_count = ? " +
                ", parking_high_roof_count = ? " +
                ", ci_time_from = ? " +
                ", ci_time_to = ? " +
                ", co_time = ? " +
                ", temp_coming_flag = ? " +
                ", no_disp_flag = ? " +
                ", tranid = ? " +
                ", cancel_tranid = ? " +
                ", num_man = ? " +
                ", num_woman = ? " +
                ", owner_hotel_id = ? " +
                ", owner_user_id = ? " +
                ", co_kind = ? " +
                ", cancel_kind = ? " +
                ", mail_addr1 = ?" +
                ", mail_addr2 = ?" +
                ", user_agent = ?";

        query = query + " WHERE id = ? AND reserve_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.reserveSubNo );
            prestate.setInt( 2, this.planId );
            prestate.setString( 3, this.userId );
            prestate.setInt( 4, this.reserveDate );
            prestate.setInt( 5, this.seq );
            prestate.setInt( 6, this.estTimeArrival );
            prestate.setInt( 7, this.numAdult );
            prestate.setInt( 8, this.numChild );
            prestate.setString( 9, this.nameLast );
            prestate.setString( 10, this.nameFirst );
            prestate.setString( 11, this.nameLastKana );
            prestate.setString( 12, this.nameFirstKana );
            prestate.setString( 13, this.zipCode );
            prestate.setInt( 14, this.prefCode );
            prestate.setInt( 15, this.jisCode );
            prestate.setString( 16, this.address1 );
            prestate.setString( 17, this.address2 );
            prestate.setString( 18, this.address3 );
            prestate.setString( 19, this.tel1 );
            prestate.setString( 20, this.tel2 );
            prestate.setInt( 21, this.reminderFlag );
            prestate.setString( 22, this.mailAddr );
            prestate.setString( 23, this.demands );
            prestate.setString( 24, this.remarks );
            prestate.setInt( 25, this.acceptDate );
            prestate.setInt( 26, this.acceptTime );
            prestate.setInt( 27, this.status );
            prestate.setInt( 28, this.basicChargeTotal );
            prestate.setInt( 29, this.optionChargeTotal );
            prestate.setInt( 30, this.chargeTotal );
            prestate.setInt( 31, this.addPoint );
            prestate.setInt( 32, this.comingFlag );
            prestate.setString( 33, this.hotelName );
            prestate.setInt( 34, this.nowShowFlag );
            prestate.setInt( 35, this.parking );
            prestate.setInt( 36, this.parkingCount );
            prestate.setInt( 37, this.parkingHiRoofCount );
            prestate.setInt( 38, this.ciTimeFrom );
            prestate.setInt( 39, this.ciTimeTo );
            prestate.setInt( 40, this.coTime );
            prestate.setInt( 41, this.tempComingFlag );
            prestate.setInt( 42, this.noDispFlag );
            prestate.setString( 43, this.tranid );
            prestate.setString( 44, this.cancelTranid );
            prestate.setInt( 45, this.numMan );
            prestate.setInt( 46, this.numWoman );
            prestate.setString( 47, this.ownerHotelId );
            prestate.setInt( 48, this.ownerUserId );
            prestate.setInt( 49, this.coKind );
            prestate.setInt( 50, this.cancelKind );
            prestate.setString( 51, this.mailAddr1 );
            prestate.setString( 52, this.mailAddr2 );
            prestate.setString( 53, this.userAgent );

            prestate.setInt( 54, Id );
            prestate.setString( 55, reserveNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 予約データ更新
     * 
     * @param iD ホテルID
     * @param reserveNo 予約番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateEstTimeArrivalData(int Id, String reserveNo, String Schema)
    {
        int result;
        boolean ret;
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;

        String reserveNoMain = "";
        ret = false;

        // 到着時刻変更可否判定
        int judgeCiFrom = this.ciTimeFrom;
        int judgeCiTo = this.ciTimeTo;
        TimeCommon timeCommon = new TimeCommon();
        if ( timeCommon.getRsvTime( Id, this.reserveDate, reserveNo ) )
        {
            judgeCiFrom = timeCommon.getCiTimeFrom();
            judgeCiTo = timeCommon.getCoKind() == 1 ? timeCommon.getCoTime() : timeCommon.getCiTimeTo() + timeCommon.getCoTime();
            Logging.info( "[DataRsvReserve.updateEstTimeArrivalData] timeCommon.getCiTimeFrom()=" + timeCommon.getCiTimeFrom() + ",timeCommon.getCiTimeTo():" + timeCommon.getCiTimeTo() + ",timeCommon.getCoKind():" + timeCommon.getCoKind()
                    + ",timeCommon.getCoTime():" + timeCommon.getCoTime()
                    + ",this.estTimeArrival:" + this.estTimeArrival );
        }
        if ( this.estTimeArrival < judgeCiFrom || this.estTimeArrival > judgeCiTo )
        {
            return false;
        }

        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {
            // 連泊か判定する
            query = "SELECT * FROM " + Schema + ".hh_rsv_reserve " +
                    "WHERE id = ? AND reserve_no = ?";
            ResultSet resultR = null;
            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, Id );
                prestate.setString( 2, reserveNo );
                resultR = prestate.executeQuery();
                if ( resultR.next() )
                {
                    reserveNoMain = resultR.getString( "reserve_no_main" );
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[DataRsvReserve.updateEstTimeArrivalData] Exception=" + e.toString() );
                ret = false;
            }
            finally
            {
                DBConnection.releaseResources( resultR, prestate, connection );
            }
        }

        if ( reserveNoMain.equals( "" ) )
        {
            // 一泊
            query = "UPDATE " + Schema + ".hh_rsv_reserve SET " +
                    " est_time_arrival = ? ";
            query = query + " WHERE id = ? AND reserve_no = ?";
        }
        else
        {
            // 連泊
            query = "UPDATE " + Schema + ".hh_rsv_reserve SET " +
                    " est_time_arrival = ? ";
            query = query + " WHERE id = ? AND reserve_no_main = ? ";
        }
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.estTimeArrival );
            prestate.setInt( 2, Id );
            if ( reserveNoMain.equals( "" ) )
            {
                prestate.setString( 3, reserveNo );
            }
            else
            {
                prestate.setString( 3, reserveNoMain );
            }
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.updateEstTimeArrivalData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * 予約データ更新
     * 
     * @param iD ホテルID
     * @param reserveNo 予約番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(Connection conn, int Id, String reserveNo)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_rsv_reserve SET " +
                "  reserve_sub_no = ? " +
                ", plan_id = ? " +
                ", user_id = ? " +
                ", reserve_date = ? " +
                ", seq = ? " +
                ", est_time_arrival = ? " +
                ", num_adult = ? " +
                ", num_child = ? " +
                ", name_last = ? " +
                ", name_first = ? " +
                ", name_last_kana = ? " +
                ", name_first_kana = ? " +
                ", zip_code = ? " +
                ", pref_code = ? " +
                ", jis_code = ? " +
                ", address1 = ? " +
                ", address2 = ? " +
                ", address3 = ? " +
                ", tel1 = ? " +
                ", tel2 = ? " +
                ", reminder_flag = ? " +
                ", mail_addr = ? " +
                ", demands = ? " +
                ", remarks = ? " +
                ", accept_date = ? " +
                ", accept_time = ? " +
                ", status = ? " +
                ", basic_charge_total = ? " +
                ", option_charge_total = ? " +
                ", charge_total = ? " +
                ", add_point = ? " +
                ", coming_flag = ? " +
                ", hotel_name = ? " +
                ", noshow_flag = ? " +
                ", parking = ? " +
                ", parking_count = ? " +
                ", parking_high_roof_count = ? " +
                ", ci_time_from = ? " +
                ", ci_time_to = ? " +
                ", co_time = ? " +
                ", temp_coming_flag = ? " +
                ", no_disp_flag = ? " +
                ", tranid = ? " +
                ", cancel_tranid = ? " +
                ", num_man = ? " +
                ", num_woman = ? " +
                ", owner_hotel_id = ? " +
                ", owner_user_id = ? " +
                ", co_kind = ? " +
                ", cancel_kind = ? " +
                ", mail_addr1 = ?" +
                ", mail_addr2 = ?" +
                ", user_agent = ?";

        query = query + " WHERE id = ? AND reserve_no = ?";

        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.reserveSubNo );
            prestate.setInt( 2, this.planId );
            prestate.setString( 3, this.userId );
            prestate.setInt( 4, this.reserveDate );
            prestate.setInt( 5, this.seq );
            prestate.setInt( 6, this.estTimeArrival );
            prestate.setInt( 7, this.numAdult );
            prestate.setInt( 8, this.numChild );
            prestate.setString( 9, this.nameLast );
            prestate.setString( 10, this.nameFirst );
            prestate.setString( 11, this.nameLastKana );
            prestate.setString( 12, this.nameFirstKana );
            prestate.setString( 13, this.zipCode );
            prestate.setInt( 14, this.prefCode );
            prestate.setInt( 15, this.jisCode );
            prestate.setString( 16, this.address1 );
            prestate.setString( 17, this.address2 );
            prestate.setString( 18, this.address3 );
            prestate.setString( 19, this.tel1 );
            prestate.setString( 20, this.tel2 );
            prestate.setInt( 21, this.reminderFlag );
            prestate.setString( 22, this.mailAddr );
            prestate.setString( 23, this.demands );
            prestate.setString( 24, this.remarks );
            prestate.setInt( 25, this.acceptDate );
            prestate.setInt( 26, this.acceptTime );
            prestate.setInt( 27, this.status );
            prestate.setInt( 28, this.basicChargeTotal );
            prestate.setInt( 29, this.optionChargeTotal );
            prestate.setInt( 30, this.chargeTotal );
            prestate.setInt( 31, this.addPoint );
            prestate.setInt( 32, this.comingFlag );
            prestate.setString( 33, this.hotelName );
            prestate.setInt( 34, this.nowShowFlag );
            prestate.setInt( 35, this.parking );
            prestate.setInt( 36, this.parkingCount );
            prestate.setInt( 37, this.parkingHiRoofCount );
            prestate.setInt( 38, this.ciTimeFrom );
            prestate.setInt( 39, this.ciTimeTo );
            prestate.setInt( 40, this.coTime );
            prestate.setInt( 41, this.tempComingFlag );
            prestate.setInt( 42, this.noDispFlag );
            prestate.setString( 43, this.tranid );
            prestate.setString( 44, this.cancelTranid );
            prestate.setInt( 45, this.numMan );
            prestate.setInt( 46, this.numWoman );
            prestate.setString( 47, this.ownerHotelId );
            prestate.setInt( 48, this.ownerUserId );
            prestate.setInt( 49, this.coKind );
            prestate.setInt( 50, this.cancelKind );
            prestate.setString( 51, this.mailAddr1 );
            prestate.setString( 52, this.mailAddr2 );
            prestate.setString( 53, this.userAgent );

            prestate.setInt( 54, Id );
            prestate.setString( 55, reserveNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 予約データ決済ステータス更新
     * 
     * @see "同じ予約親番号を持つデータの決済ステータス(payment_status)を一括更新する"
     * @connection コネクション
     * @param id ホテルID
     * @param reserveNo 予約親番号
     * @return
     */
    public boolean updatePaymentStatus(Connection connection, int id, String reserveNo)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_reserve SET ";
        query += "  payment_status=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE id=? AND reserve_no=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, ReserveCommon.PAYMENT_STATUS_SETTLED );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( i++, id );
            prestate.setString( i++, reserveNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.updatePaymentStatus] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 予約データ決済ステータス更新
     * 
     * @see "同じ予約親番号を持つデータの決済ステータス(payment_status)を一括更新する"
     * @connection コネクション
     * @param id ホテルID
     * @param reserveNoMain 予約親番号
     * @param paymentStatus 決済ステータス
     * @return
     */
    public boolean updatePaymentStatus(Connection connection, int id, String reserveNoMain, int paymentStatus)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_reserve SET ";
        query += "  payment_status=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE id=? AND reserve_no_main=? AND payment_status != ?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, paymentStatus );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );
            prestate.setInt( i++, id );
            prestate.setString( i++, reserveNoMain );
            prestate.setInt( i++, paymentStatus );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserve.updatePaymentStatus] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

}
