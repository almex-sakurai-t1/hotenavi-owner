package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/*
 * 予約データ クラス
 */

public class DataRsvReserveWork implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -8421319553259356037L;

    private int               workid;
    private int               iD;
    private String            reserveNo;
    private int               reserveSubNo;
    private int               planId;
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
    private int               ciTimeFrom;
    private int               ciTimeTo;
    private int               coTime;
    private int               tempComingFlag;
    private int               highRoofCount;
    private int               numMan;
    private int               numWoman;
    private int               coKind;

    /**
     * データの初期化
     */
    public DataRsvReserveWork()
    {
        workid = 0;
        iD = 0;
        reserveNo = "";
        reserveSubNo = 0;
        planId = 0;
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
        ciTimeFrom = 0;
        ciTimeTo = 0;
        coTime = 0;
        tempComingFlag = 0;
        highRoofCount = 0;
        setNumMan( 0 );
        setNumWoman( 0 );
        coKind = 0;
    }

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
        return this.coKind;
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

    public int getTempComingFlag()
    {
        return this.tempComingFlag;
    }

    public int getWorkid()
    {
        return workid;
    }

    public int getHiRoofCount()
    {
        return highRoofCount;
    }

    public int getNumMan()
    {
        return numMan;
    }

    public int getNumWoman()
    {
        return numWoman;
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

    public void setTempComingFlag(int tempComingFlag)
    {
        this.tempComingFlag = tempComingFlag;
    }

    public void setWorkid(int workid)
    {
        this.workid = workid;
    }

    public void setHiRoofCount(int highRoofCount)
    {
        this.highRoofCount = highRoofCount;
    }

    public void setNumMan(int numMan)
    {
        this.numMan = numMan;
    }

    public void setNumWoman(int numWoman)
    {
        this.numWoman = numWoman;
    }

    /**
     * 仮予約情報取得
     * 
     * @param iD ホテルID
     * @param workid ワークID
     * @return 処理結果(TRUE:正常,False:異常)
     */
    public boolean getReserveWork(int Id, int workid)
    {
        String query;
        PreparedStatement prestate = null;
        ResultSet result = null;
        Connection connection = null;
        boolean ret = false;

        query = "SELECT work_id, id, reserve_no, reserve_sub_no, plan_id, user_id," +
                " reserve_date, seq, est_time_arrival, num_adult, num_child, num_man, num_woman," +
                " name_last, name_first, name_last_kana, name_first_kana," +
                " zip_code, pref_code, jis_code, address1, address2, address3," +
                " tel1, tel2, reminder_flag,mail_addr, demands," +
                " remarks, accept_date, accept_time,status, basic_charge_total," +
                " option_charge_total, charge_total,add_point, coming_flag," +
                " hotel_name, noshow_flag, parking,parking_count, parking_high_roof_count," +
                " ci_time_from, ci_time_to, co_time, temp_coming_flag, co_kind " +
                " FROM hh_rsv_reserve_work WHERE id = ? AND work_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, Id );
            prestate.setInt( 2, workid );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.workid = result.getInt( "work_id" );
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
                    this.coKind = result.getInt( "co_kind" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataRsvReserveWork.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }
}
