package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReserveCommon;

/*
 * 予約履歴データ クラス
 */

public class DataRsvReserveHistory implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -7816920467176786204L;

    private int               iD;
    private String            reserveNo;
    private int               reserveSubNo;
    private int               updateKind;
    private int               planId;
    private int               planSubId;
    private String            userId;
    private int               reserveDate;
    private int               seq;
    private int               estTimeArrival;
    private int               numAdult;
    private int               numChild;
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
    private int               highRoofCount;
    private int               ciTimeFrom;
    private int               ciTimeTo;
    private int               coTime;
    private int               tempComingFlag;
    private int               numMan;
    private int               numWoman;
    private String            ownerHotelId;
    private int               ownerUserId;
    private int               coKind;
    private int               cancelKind;
    private String            mailAddr1;
    private String            mailAddr2;
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
    private float             otaTotalAmountAfterTaxes;
    private float             otaTotalAmountOfTaxes;
    private String            otaCurrency;

    /**
     * データの初期化
     */
    public DataRsvReserveHistory()
    {
        iD = 0;
        reserveNo = "";
        reserveSubNo = 0;
        updateKind = 0;
        planId = 0;
        planSubId = 0;
        userId = "";
        reserveDate = 0;
        seq = 0;
        estTimeArrival = 0;
        numAdult = 0;
        numChild = 0;
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
        highRoofCount = 0;
        ciTimeFrom = 0;
        ciTimeTo = 0;
        coTime = 0;
        tempComingFlag = 0;
        numMan = 0;
        numWoman = 0;
        ownerHotelId = "";
        ownerUserId = 0;
        coKind = 0;
        cancelKind = 0;
        mailAddr1 = "";
        mailAddr2 = "";
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

    public String getMailAddr1()
    {
        return mailAddr1;
    }

    public String getMailAddr2()
    {
        return mailAddr2;
    }

    public void setMailAddr1(String mailAddr1)
    {
        this.mailAddr1 = mailAddr1;
    }

    public void setMailAddr2(String mailAddr2)
    {
        this.mailAddr2 = mailAddr2;
    }

    /****/

    public int getiD()
    {
        return iD;
    }

    public int getReserveSubNo()
    {
        return reserveSubNo;
    }

    public String getZipCode()
    {
        return zipCode;
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

    public int getAcceptTime()
    {
        return acceptTime;
    }

    public int getHighRoofCount()
    {
        return highRoofCount;
    }

    public int getCancelKind()
    {
        return cancelKind;
    }

    public void setiD(int iD)
    {
        this.iD = iD;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setReminderFlag(int reminderFlag)
    {
        this.reminderFlag = reminderFlag;
    }

    public void setHighRoofCount(int highRoofCount)
    {
        this.highRoofCount = highRoofCount;
    }

    public void setCancelKind(int cancelKind)
    {
        this.cancelKind = cancelKind;
    }

    // getter
    public int getID()
    {
        return this.iD;
    }

    public String getReserveNo()
    {
        return this.reserveNo;
    }

    public int getReserveNoSub()
    {
        return this.reserveSubNo;
    }

    public int getUpdateKind()
    {
        return this.updateKind;
    }

    public int getPlanId()
    {
        return this.planId;
    }

    public int getPlanSubId()
    {
        return this.planSubId;
    }

    public String getUserId()
    {
        return this.userId;
    }

    public int getReserveDate()
    {
        return this.reserveDate;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public int getEstTimeArrival()
    {
        return this.estTimeArrival;
    }

    public int getNumAdult()
    {
        return this.numAdult;
    }

    public int getNumChild()
    {
        return this.numChild;
    }

    public String getNameLast()
    {
        return this.nameLast;
    }

    public String getNameFirst()
    {
        return this.nameFirst;
    }

    public String getNameLastKana()
    {
        return this.nameLastKana;
    }

    public String getNameFirstKana()
    {
        return this.nameFirstKana;
    }

    public String getZipCd()
    {
        return this.zipCode;
    }

    public int getPrefCode()
    {
        return this.prefCode;
    }

    public int getJisCode()
    {
        return this.jisCode;
    }

    public String getAddres1()
    {
        return this.address1;
    }

    public String getAddres2()
    {
        return this.address2;
    }

    public String getAddres3()
    {
        return this.address3;
    }

    public String getTel1()
    {
        return this.tel1;
    }

    public String getTel2()
    {
        return this.tel2;
    }

    public int getReminderFlag()
    {
        return this.reminderFlag;
    }

    public String getMailAddr()
    {
        return this.mailAddr;
    }

    public String getDemands()
    {
        return this.demands;
    }

    public String getRemarks()
    {
        return this.remarks;
    }

    public int getAcceptDate()
    {
        return this.acceptDate;
    }

    public int getacceptTime()
    {
        return this.acceptTime;
    }

    public int getStatus()
    {
        return this.status;
    }

    public int getBasicChargeTotal()
    {
        return this.basicChargeTotal;
    }

    public int getOptionChargeTotal()
    {
        return this.optionChargeTotal;
    }

    public int getChargeTotal()
    {
        return this.chargeTotal;
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

    public int getAddPoint()
    {
        return this.addPoint;
    }

    public int getComingFlag()
    {
        return this.comingFlag;
    }

    public String getHotelName()
    {
        return this.hotelName;
    }

    public int getNowShowFlag()
    {
        return this.nowShowFlag;
    }

    public int getParking()
    {
        return this.parking;
    }

    public int getParkingCount()
    {
        return this.parkingCount;
    }

    public int getHiRoofCount()
    {
        return this.highRoofCount;
    }

    public int getTempComingFlag()
    {
        return tempComingFlag;
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
    public void setId(int iD)
    {
        this.iD = iD;
    }

    public void setReserveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    public void setReserveSubNo(int reserveSubNo)
    {
        this.reserveSubNo = reserveSubNo;
    }

    public void setUpdateKind(int updateKind)
    {
        this.updateKind = updateKind;
    }

    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    public void setPlanSubId(int planSubId)
    {
        this.planSubId = planSubId;
    }

    public void steUserId(String userId)
    {
        this.userId = userId;
    }

    public void setReserveDate(int reserveDate)
    {
        this.reserveDate = reserveDate;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setEstTimeArrival(int estTimeArrival)
    {
        this.estTimeArrival = estTimeArrival;
    }

    public void setNumAdult(int numAdult)
    {
        this.numAdult = numAdult;
    }

    public void setNumChild(int numChild)
    {
        this.numChild = numChild;
    }

    public void setNameLast(String nameLast)
    {
        this.nameLast = nameLast;
    }

    public void setNameFirst(String nameFirst)
    {
        this.nameFirst = nameFirst;
    }

    public void setNameLastKana(String nameLastKana)
    {
        this.nameLastKana = nameLastKana;
    }

    public void setNameFirstKana(String nameFirstKana)
    {
        this.nameFirstKana = nameFirstKana;
    }

    public void setZipCd(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setPrefCode(int prefCode)
    {
        this.prefCode = prefCode;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
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

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public void setRemaindFlag(int reminderFlag)
    {
        this.reminderFlag = reminderFlag;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setDemands(String demands)
    {
        this.demands = demands;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public void setAcceptDate(int acceptDate)
    {
        this.acceptDate = acceptDate;
    }

    public void setAcceptTime(int acceptTime)
    {
        this.acceptTime = acceptTime;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setBasicChargeTotal(int basicChargeTotal)
    {
        this.basicChargeTotal = basicChargeTotal;
    }

    public void setOptionChargeTotal(int optionChargeTotal)
    {
        this.optionChargeTotal = optionChargeTotal;
    }

    public void setChargeTotal(int chargeTotal)
    {
        this.chargeTotal = chargeTotal;
    }

    public void setAddPoint(int addPoint)
    {
        this.addPoint = addPoint;
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

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public void setNowShowFlag(int nowShowFlag)
    {
        this.nowShowFlag = nowShowFlag;
    }

    public void setParking(int parking)
    {
        this.parking = parking;
    }

    public void setParkingCount(int parkingCount)
    {
        this.parkingCount = parkingCount;
    }

    public void setHiRoofCount(int highRoofCount)
    {
        this.highRoofCount = highRoofCount;
    }

    public void setTempComingFlag(int tempComingFlag)
    {
        this.tempComingFlag = tempComingFlag;
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
     * 予約履歴情報取得
     * 
     * @param iD ホテルID
     * @param reserveNo 予約番号
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getData(int Id, String reserveNo)
    {
        // 変数定義
        boolean ret; // 戻り値
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT id, reserve_no, reserve_sub_no, update_kind, " +
                " plan_id, user_id, reserve_date, seq," +
                " est_time_arrival, num_adult, num_child, num_man, num_woman," +
                " name_last, name_first, name_last_kana, name_first_kana," +
                " zip_code, address1, address2, tel1, tel2, reminder_flag," +
                " mail_addr, demands, remarks, accept_date, accept_time," +
                " status, basic_charge_total, option_charge_total, charge_total," +
                " add_point, coming_flag, hotel_name, noshow_flag, parking," +
                " parking_count, parking_high_roof_count, ci_time_from, ci_time_to, co_time, temp_coming_flag," +
                " owner_hotel_id, owner_user_id, co_kind, cancel_kind, mail_addr1, mail_addr2" +
                " FROM hh_rsv_reserve_history WHERE id = ? AND reserve_no = ?";

        try
        {
            connection = DBConnection.getConnection();
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
                    this.updateKind = result.getInt( "update_kind" );
                    this.planId = result.getInt( "plan_id" );
                    this.userId = result.getString( "user_id" );
                    this.reserveDate = result.getInt( "reserve_date" );
                    this.seq = result.getInt( "seq" );
                    this.estTimeArrival = result.getInt( "est_time_arrival" );
                    this.numAdult = result.getInt( "num_adult" );
                    this.numChild = result.getInt( "num_child" );
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
                    this.ciTimeFrom = result.getInt( "ci_time_from" );
                    this.ciTimeTo = result.getInt( "ci_time_to" );
                    this.coTime = result.getInt( "co_time" );
                    this.tempComingFlag = result.getInt( "temp_coming_flag" );
                    this.highRoofCount = result.getInt( "parking_high_roof_count" );
                    this.numMan = result.getInt( "num_man" );
                    this.numWoman = result.getInt( "num_woman" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.coKind = result.getInt( "co_kind" );
                    this.cancelKind = result.getInt( "cancel_kind" );
                    this.mailAddr1 = result.getString( "mail_addr1" );
                    this.mailAddr2 = result.getString( "mail_addr2" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserveHistory.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 予約履歴データ設定
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

                this.updateKind = result.getInt( "update_kind" );

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
                this.ciTimeFrom = result.getInt( "ci_time_from" );
                this.ciTimeTo = result.getInt( "ci_time_to" );
                this.coTime = result.getInt( "co_time" );
                this.tempComingFlag = result.getInt( "temp_coming_flag" );
                this.highRoofCount = result.getInt( "parking_high_roof_count" );
                this.numMan = result.getInt( "num_man" );
                this.numWoman = result.getInt( "num_woman" );
                this.ownerHotelId = result.getString( "owner_hotel_id" );
                this.ownerUserId = result.getInt( "owner_user_id" );
                this.coKind = result.getInt( "co_kind" );
                this.cancelKind = result.getInt( "cancel_kind" );
                this.mailAddr1 = result.getString( "mail_addr1" );
                this.mailAddr2 = result.getString( "mail_addr2" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserveHistory.setData] Exception=" + e.toString() );
        }
        return(ret);
    }

    /**
     * 予約履歴データ登録
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

        query = "INSERT INTO hh_rsv_reserve_history SET " +
                "  id = ?" +
                ", reserve_no = ?" +
                ", reserve_sub_no = ? " +
                ", update_kind = ? " +
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
                ", ci_time_from = ? " +
                ", ci_time_to = ? " +
                ", co_time = ? " +
                ", temp_coming_flag = ? " +
                ", parking_high_roof_count = ? " +
                ", num_man = ? " +
                ", num_woman = ? " +
                ", owner_hotel_id = ? " +
                ", owner_user_id = ? " +
                ", co_kind = ? " +
                ", cancel_kind = ? " +
                ", mail_addr1 = ? " +
                ", mail_addr2 = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.iD );
            prestate.setString( 2, this.reserveNo );
            prestate.setInt( 3, this.reserveSubNo );
            prestate.setInt( 4, this.updateKind );
            prestate.setInt( 5, this.planId );
            prestate.setString( 6, this.userId );
            prestate.setInt( 7, this.reserveDate );
            prestate.setInt( 8, this.seq );
            prestate.setInt( 9, this.estTimeArrival );
            prestate.setInt( 10, this.numAdult );
            prestate.setInt( 11, this.numChild );
            prestate.setString( 12, this.nameLast );
            prestate.setString( 13, this.nameFirst );
            prestate.setString( 14, this.nameLastKana );
            prestate.setString( 15, this.nameFirstKana );
            prestate.setString( 16, this.zipCode );
            prestate.setInt( 17, this.prefCode );
            prestate.setInt( 18, this.jisCode );
            prestate.setString( 19, this.address1 );
            prestate.setString( 20, this.address2 );
            prestate.setString( 21, this.address3 );
            prestate.setString( 22, this.tel1 );
            prestate.setString( 23, this.tel2 );
            prestate.setInt( 24, this.reminderFlag );
            prestate.setString( 25, this.mailAddr );
            prestate.setString( 26, this.demands );
            prestate.setString( 27, this.remarks );
            prestate.setInt( 28, this.acceptDate );
            prestate.setInt( 29, this.acceptTime );
            prestate.setInt( 30, this.status );
            prestate.setInt( 31, this.basicChargeTotal );
            prestate.setInt( 32, this.optionChargeTotal );
            prestate.setInt( 33, this.chargeTotal );
            prestate.setInt( 34, this.addPoint );
            prestate.setInt( 35, this.comingFlag );
            prestate.setString( 36, this.hotelName );
            prestate.setInt( 37, this.nowShowFlag );
            prestate.setInt( 38, this.parking );
            prestate.setInt( 39, this.parkingCount );
            prestate.setInt( 40, this.ciTimeFrom );
            prestate.setInt( 41, this.ciTimeTo );
            prestate.setInt( 42, this.coTime );
            prestate.setInt( 43, this.tempComingFlag );
            prestate.setInt( 44, this.highRoofCount );
            prestate.setInt( 45, this.numMan );
            prestate.setInt( 46, this.numWoman );
            prestate.setString( 47, this.ownerHotelId );
            prestate.setInt( 48, this.ownerUserId );
            prestate.setInt( 49, this.coKind );
            prestate.setInt( 50, this.cancelKind );
            prestate.setString( 51, this.mailAddr1 );
            prestate.setString( 52, this.mailAddr2 );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserveHistory.insertData] Exception=" + e.toString() );
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
     * 予約履歴データ登録
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

        query = "INSERT INTO hh_rsv_reserve_history SET " +
                "  id = ?" +
                ", reserve_no = ?" +
                ", reserve_sub_no = ? " +
                ", update_kind = ? " +
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
                ", ci_time_from = ? " +
                ", ci_time_to = ? " +
                ", co_time = ? " +
                ", temp_coming_flag = ? " +
                ", parking_high_roof_count = ? " +
                ", num_man = ? " +
                ", num_woman = ? " +
                ", owner_hotel_id = ? " +
                ", owner_user_id = ? " +
                ", co_kind = ? " +
                ", cancel_kind = ? " +
                ", mail_addr1 = ? " +
                ", mail_addr2 = ? ";

        try
        {
            prestate = conn.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.iD );
            prestate.setString( 2, this.reserveNo );
            prestate.setInt( 3, this.reserveSubNo );
            prestate.setInt( 4, this.updateKind );
            prestate.setInt( 5, this.planId );
            prestate.setString( 6, this.userId );
            prestate.setInt( 7, this.reserveDate );
            prestate.setInt( 8, this.seq );
            prestate.setInt( 9, this.estTimeArrival );
            prestate.setInt( 10, this.numAdult );
            prestate.setInt( 11, this.numChild );
            prestate.setString( 12, this.nameLast );
            prestate.setString( 13, this.nameFirst );
            prestate.setString( 14, this.nameLastKana );
            prestate.setString( 15, this.nameFirstKana );
            prestate.setString( 16, this.zipCode );
            prestate.setInt( 17, this.prefCode );
            prestate.setInt( 18, this.jisCode );
            prestate.setString( 19, this.address1 );
            prestate.setString( 20, this.address2 );
            prestate.setString( 21, this.address3 );
            prestate.setString( 22, this.tel1 );
            prestate.setString( 23, this.tel2 );
            prestate.setInt( 24, this.reminderFlag );
            prestate.setString( 25, this.mailAddr );
            prestate.setString( 26, this.demands );
            prestate.setString( 27, this.remarks );
            prestate.setInt( 28, this.acceptDate );
            prestate.setInt( 29, this.acceptTime );
            prestate.setInt( 30, this.status );
            prestate.setInt( 31, this.basicChargeTotal );
            prestate.setInt( 32, this.optionChargeTotal );
            prestate.setInt( 33, this.chargeTotal );
            prestate.setInt( 34, this.addPoint );
            prestate.setInt( 35, this.comingFlag );
            prestate.setString( 36, this.hotelName );
            prestate.setInt( 37, this.nowShowFlag );
            prestate.setInt( 38, this.parking );
            prestate.setInt( 39, this.parkingCount );
            prestate.setInt( 40, this.ciTimeFrom );
            prestate.setInt( 41, this.ciTimeTo );
            prestate.setInt( 42, this.coTime );
            prestate.setInt( 43, this.tempComingFlag );
            prestate.setInt( 44, this.highRoofCount );
            prestate.setInt( 45, this.numMan );
            prestate.setInt( 46, this.numWoman );
            prestate.setString( 47, this.ownerHotelId );
            prestate.setInt( 48, this.ownerUserId );
            prestate.setInt( 49, this.coKind );
            prestate.setInt( 50, this.cancelKind );
            prestate.setString( 51, this.mailAddr1 );
            prestate.setString( 52, this.mailAddr2 );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserveHistory.insertData] Exception=" + e.toString() );
            ret = false;
        }
        return(ret);
    }

    /**
     * 予約履歴データ登録
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection conn, String Schema)
    {
        boolean ret;
        if ( Schema.equals( ReserveCommon.SCHEMA_NEWRSV ) )
        {
            int result;
            String query;
            PreparedStatement prestate = null;

            ret = false;

            query = "INSERT INTO  " + ReserveCommon.SCHEMA_NEWRSV + ".hh_rsv_reserve_history SET " +
                    "  id = ?" +
                    ", reserve_no = ?" +
                    ", reserve_sub_no = ? " +
                    ", update_kind = ? " +
                    ", plan_id = ? " +
                    ", plan_sub_id = ? " +
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
                    ", ci_time_from = ? " +
                    ", ci_time_to = ? " +
                    ", co_time = ? " +
                    ", temp_coming_flag = ? " +
                    ", highroof_count = ? " +
                    ", num_man = ? " +
                    ", num_woman = ? " +
                    ", owner_hotel_id = ? " +
                    ", owner_user_id = ? " +
                    ", co_kind = ? " +
                    ", cancel_type = ? " +
                    ", mail_addr1 = ? " +
                    ", mail_addr2 = ? " +
                    ", payment = ? " +
                    ", payment_status = ? " +
                    ", consumer_demands = ? " +
                    ", add_bonus_mile = ? " +
                    ", used_mile = ? " +
                    ", reserve_no_main = ? " +
                    ", reserve_date_to = ? " +
                    ", ext_flag = ? " +
                    ", cancel_charge = ? " +
                    ", basic_charge_total_all = ? " +
                    ", option_charge_total_all= ? " +
                    ", charge_total_all = ? " +
                    ", cancel_date = ? " +
                    ", cancel_credit_status = ? " +
                    ", room_hold = ? " +
                    ", ota_cd = ? " +
                    ", ota_booking_code = ? " +
                    ", ota_total_amount_after_taxes = ? " +
                    ", ota_total_amount_of_taxes = ? " +
                    ", ota_currency = ? " +
                    ", last_update = " + DateEdit.getDate( 2 ) +
                    ", last_uptime = " + DateEdit.getTime( 1 );

            try
            {
                prestate = conn.prepareStatement( query );
                // 更新対象の値をセットする
                prestate.setInt( 1, this.iD );
                prestate.setString( 2, this.reserveNo );
                prestate.setInt( 3, this.reserveSubNo );
                prestate.setInt( 4, this.updateKind );
                prestate.setInt( 5, this.planId );
                prestate.setInt( 6, this.planSubId );
                prestate.setString( 7, this.userId );
                prestate.setInt( 8, this.reserveDate );
                prestate.setInt( 9, this.seq );
                prestate.setInt( 10, this.estTimeArrival );
                prestate.setInt( 11, this.numAdult );
                prestate.setInt( 12, this.numChild );
                prestate.setString( 13, this.nameLast );
                prestate.setString( 14, this.nameFirst );
                prestate.setString( 15, this.nameLastKana );
                prestate.setString( 16, this.nameFirstKana );
                prestate.setString( 17, this.zipCode );
                prestate.setInt( 18, this.prefCode );
                prestate.setInt( 19, this.jisCode );
                prestate.setString( 20, this.address1 );
                prestate.setString( 21, this.address2 );
                prestate.setString( 22, this.address3 );
                prestate.setString( 23, this.tel1 );
                prestate.setString( 24, this.tel2 );
                prestate.setInt( 25, this.reminderFlag );
                prestate.setString( 26, this.mailAddr );
                prestate.setString( 27, this.demands );
                prestate.setString( 28, this.remarks );
                prestate.setInt( 29, this.acceptDate );
                prestate.setInt( 30, this.acceptTime );
                prestate.setInt( 31, this.status );
                prestate.setInt( 32, this.basicChargeTotal );
                prestate.setInt( 33, this.optionChargeTotal );
                prestate.setInt( 34, this.chargeTotal );
                prestate.setInt( 35, this.addPoint );
                prestate.setInt( 36, this.comingFlag );
                prestate.setString( 37, this.hotelName );
                prestate.setInt( 38, this.nowShowFlag );
                prestate.setInt( 39, this.parking );
                prestate.setInt( 40, this.parkingCount );
                prestate.setInt( 41, this.ciTimeFrom );
                prestate.setInt( 42, this.ciTimeTo );
                prestate.setInt( 43, this.coTime );
                prestate.setInt( 44, this.tempComingFlag );
                prestate.setInt( 45, this.highRoofCount );
                prestate.setInt( 46, this.numMan );
                prestate.setInt( 47, this.numWoman );
                prestate.setString( 48, this.ownerHotelId );
                prestate.setInt( 49, this.ownerUserId );
                prestate.setInt( 50, this.coKind );
                prestate.setInt( 51, this.cancelKind );
                prestate.setString( 52, this.mailAddr1 );
                prestate.setString( 53, this.mailAddr2 );
                prestate.setInt( 54, this.payment );
                prestate.setInt( 55, this.paymentStatus );
                prestate.setString( 56, this.consumerDemands );
                prestate.setInt( 57, this.addBonusMile );
                prestate.setInt( 58, this.usedMile );
                prestate.setString( 59, this.reserveNoMain );
                prestate.setInt( 60, this.reserveDateTo );
                prestate.setInt( 61, this.extFlag );
                prestate.setInt( 62, this.cancelCharge );
                prestate.setInt( 63, this.basicChargeTotalAll );
                prestate.setInt( 64, this.optionChargeTotalAll );
                prestate.setInt( 65, this.chargeTotalAll );
                prestate.setInt( 66, this.cancelDate );
                prestate.setInt( 67, this.cancelCreditStatus );
                prestate.setInt( 68, this.roomHold );
                prestate.setInt( 69, this.otaCd );
                prestate.setString( 70, this.otaBookingCode );
                prestate.setFloat( 71, this.otaTotalAmountAfterTaxes );
                prestate.setFloat( 72, this.otaTotalAmountOfTaxes );
                prestate.setString( 73, this.otaCurrency );

                result = prestate.executeUpdate();
                if ( result > 0 )
                {
                    ret = true;
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[DataRsvReserveHistory.insertData] Exception=" + e.toString() );
                ret = false;
            }
        }
        else
        {
            ret = insertData( conn );
        }
        return(ret);
    }
}
