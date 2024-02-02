/*
 * @(#)DataBkoAccountRecv.java 1.00 2011/04/15 Copyright (C) ALMEX Inc. 2007 売掛データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 売掛データ取得クラス
 * 
 * @author J.Horie
 * @version 1.00 2011/04/15
 */
public class DataBkoAccountRecv implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4988594767260119168L;

    private int               accrecvSlipNo;
    private String            hotelId;
    private int               id;
    private int               addUpDate;
    private int               slipUpdate;
    private int               billCd;
    private String            billName;
    private String            divName;
    private String            personName;
    private int               userManagementNo;
    private int               usageDate;
    private int               usageTime;
    private int               htSlipNo;
    private String            htRoomNo;
    private int               usageCharge;
    private int               receiveCharge;
    private int               happyBalance;
    private int               accrecvAmount;
    private int               reconcileAmount;
    private int               accrecvBalance;
    private String            remarks;
    private String            correction;
    private int               tempSlipNo;
    private int               firstAccrecvSlipNo;
    private int               creditNoteFlag;
    private int               invalidFlag;
    private int               registFlag;
    private int               closingKind;
    private String            ownerHotelId;
    private int               ownerUserId;
    private int               lastUpdate;
    private int               lastUptime;
    private int               ciSeq;

    /**
     * データを初期化します。
     */
    public DataBkoAccountRecv()
    {
        accrecvSlipNo = 0;
        hotelId = "";
        id = 0;
        addUpDate = 0;
        slipUpdate = 0;
        billCd = 0;
        billName = "";
        divName = "";
        personName = "";
        userManagementNo = 0;
        usageDate = 0;
        usageTime = 0;
        htSlipNo = 0;
        htRoomNo = "";
        usageCharge = 0;
        receiveCharge = 0;
        happyBalance = 0;
        accrecvAmount = 0;
        reconcileAmount = 0;
        accrecvBalance = 0;
        remarks = "";
        correction = "";
        tempSlipNo = 0;
        firstAccrecvSlipNo = 0;
        creditNoteFlag = 0;
        invalidFlag = 0;
        registFlag = 0;
        closingKind = 0;
        ownerHotelId = "";
        ownerUserId = 0;
        lastUpdate = 0;
        lastUptime = 0;
        ciSeq = 0;
    }

    /**
     * 売掛データ取得
     * 
     * @param accrecvSlipNo 売掛伝票No
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int accrecvSlipNo)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_bko_account_recv WHERE accrecv_slip_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, accrecvSlipNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.accrecvSlipNo = result.getInt( "accrecv_slip_no" );
                    this.hotelId = result.getString( "hotel_id" );
                    this.id = result.getInt( "id" );
                    this.addUpDate = result.getInt( "add_up_date" );
                    this.slipUpdate = result.getInt( "slip_update" );
                    this.billCd = result.getInt( "bill_cd" );
                    this.billName = result.getString( "bill_name" );
                    this.divName = result.getString( "div_name" );
                    this.personName = result.getString( "person_name" );
                    this.userManagementNo = result.getInt( "user_management_no" );
                    this.usageDate = result.getInt( "usage_date" );
                    this.usageTime = result.getInt( "usage_time" );
                    this.htSlipNo = result.getInt( "ht_slip_no" );
                    this.htRoomNo = result.getString( "ht_room_no" );
                    this.usageCharge = result.getInt( "usage_charge" );
                    this.receiveCharge = result.getInt( "receive_charge" );
                    this.happyBalance = result.getInt( "happy_balance" );
                    this.accrecvAmount = result.getInt( "accrecv_amount" );
                    this.reconcileAmount = result.getInt( "reconcile_amount" );
                    this.accrecvBalance = result.getInt( "accrecv_balance" );
                    this.remarks = result.getString( "remarks" );
                    this.correction = result.getString( "correction" );
                    this.tempSlipNo = result.getInt( "temp_slip_no" );
                    this.firstAccrecvSlipNo = result.getInt( "first_accrecv_slip_no" );
                    this.creditNoteFlag = result.getInt( "credit_note_flag" );
                    this.invalidFlag = result.getInt( "invalid_flag" );
                    this.registFlag = result.getInt( "regist_flag" );
                    this.closingKind = result.getInt( "closing_kind" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.ciSeq = result.getInt( "ci_seq" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecv.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 売掛データ取得
     * 
     * @param id ホテルID
     * @param ciSeq チェックインNO
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int ciSeq)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_bko_account_recv WHERE id = ? AND ci_seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, ciSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.accrecvSlipNo = result.getInt( "accrecv_slip_no" );
                    this.hotelId = result.getString( "hotel_id" );
                    this.id = result.getInt( "id" );
                    this.addUpDate = result.getInt( "add_up_date" );
                    this.slipUpdate = result.getInt( "slip_update" );
                    this.billCd = result.getInt( "bill_cd" );
                    this.billName = result.getString( "bill_name" );
                    this.divName = result.getString( "div_name" );
                    this.personName = result.getString( "person_name" );
                    this.userManagementNo = result.getInt( "user_management_no" );
                    this.usageDate = result.getInt( "usage_date" );
                    this.usageTime = result.getInt( "usage_time" );
                    this.htSlipNo = result.getInt( "ht_slip_no" );
                    this.htRoomNo = result.getString( "ht_room_no" );
                    this.usageCharge = result.getInt( "usage_charge" );
                    this.receiveCharge = result.getInt( "receive_charge" );
                    this.happyBalance = result.getInt( "happy_balance" );
                    this.accrecvAmount = result.getInt( "accrecv_amount" );
                    this.reconcileAmount = result.getInt( "reconcile_amount" );
                    this.accrecvBalance = result.getInt( "accrecv_balance" );
                    this.remarks = result.getString( "remarks" );
                    this.correction = result.getString( "correction" );
                    this.tempSlipNo = result.getInt( "temp_slip_no" );
                    this.firstAccrecvSlipNo = result.getInt( "first_accrecv_slip_no" );
                    this.creditNoteFlag = result.getInt( "credit_note_flag" );
                    this.invalidFlag = result.getInt( "invalid_flag" );
                    this.registFlag = result.getInt( "regist_flag" );
                    this.closingKind = result.getInt( "closing_kind" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.ciSeq = result.getInt( "ci_seq" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecv.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 売掛データ取得
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.accrecvSlipNo = result.getInt( "accrecv_slip_no" );
                this.hotelId = result.getString( "hotel_id" );
                this.id = result.getInt( "id" );
                this.addUpDate = result.getInt( "add_up_date" );
                this.slipUpdate = result.getInt( "slip_update" );
                this.billCd = result.getInt( "bill_cd" );
                this.billName = result.getString( "bill_name" );
                this.divName = result.getString( "div_name" );
                this.personName = result.getString( "person_name" );
                this.userManagementNo = result.getInt( "user_management_no" );
                this.usageDate = result.getInt( "usage_date" );
                this.usageTime = result.getInt( "usage_time" );
                this.htSlipNo = result.getInt( "ht_slip_no" );
                this.htRoomNo = result.getString( "ht_room_no" );
                this.usageCharge = result.getInt( "usage_charge" );
                this.receiveCharge = result.getInt( "receive_charge" );
                this.happyBalance = result.getInt( "happy_balance" );
                this.accrecvAmount = result.getInt( "accrecv_amount" );
                this.reconcileAmount = result.getInt( "reconcile_amount" );
                this.accrecvBalance = result.getInt( "accrecv_balance" );
                this.remarks = result.getString( "remarks" );
                this.correction = result.getString( "correction" );
                this.tempSlipNo = result.getInt( "temp_slip_no" );
                this.firstAccrecvSlipNo = result.getInt( "first_accrecv_slip_no" );
                this.creditNoteFlag = result.getInt( "credit_note_flag" );
                this.invalidFlag = result.getInt( "invalid_flag" );
                this.registFlag = result.getInt( "regist_flag" );
                this.closingKind = result.getInt( "closing_kind" );
                this.ownerHotelId = result.getString( "owner_hotel_id" );
                this.ownerUserId = result.getInt( "owner_user_id" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.ciSeq = result.getInt( "ci_seq" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecv.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * 売掛データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection connection)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_bko_account_recv SET ";
        query = query + " hotel_id = ?,";
        query = query + " id = ?,";
        query = query + " add_up_date = ?,";
        query = query + " slip_update = ?,";
        query = query + " bill_cd = ?,";
        query = query + " bill_name = ?,";
        query = query + " div_name = ?,";
        query = query + " person_name = ?,";
        query = query + " user_management_no = ?,";
        query = query + " usage_date = ?,";
        query = query + " usage_time = ?,";
        query = query + " ht_slip_no = ?,";
        query = query + " ht_room_no = ?,";
        query = query + " usage_charge = ?,";
        query = query + " receive_charge = ?,";
        query = query + " happy_balance = ?,";
        query = query + " accrecv_amount = ?,";
        query = query + " reconcile_amount = ?,";
        query = query + " accrecv_balance = ?,";
        query = query + " remarks = ?,";
        query = query + " correction = ?,";
        query = query + " temp_slip_no = ?,";
        query = query + " first_accrecv_slip_no = ?,";
        query = query + " credit_note_flag = ?,";
        query = query + " invalid_flag = ?,";
        query = query + " regist_flag = ?,";
        query = query + " closing_kind = ?,";
        query = query + " owner_hotel_id = ?,";
        query = query + " owner_user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " ci_seq = ?,";
        query = query + " accrecv_slip_no = ?";

        try
        {
            // connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.hotelId );
            prestate.setInt( 2, this.id );
            prestate.setInt( 3, this.addUpDate );
            prestate.setInt( 4, this.slipUpdate );
            prestate.setInt( 5, this.billCd );
            prestate.setString( 6, this.billName );
            prestate.setString( 7, this.divName );
            prestate.setString( 8, this.personName );
            prestate.setInt( 9, this.userManagementNo );
            prestate.setInt( 10, this.usageDate );
            prestate.setInt( 11, this.usageTime );
            prestate.setInt( 12, this.htSlipNo );
            prestate.setString( 13, this.htRoomNo );
            prestate.setInt( 14, this.usageCharge );
            prestate.setInt( 15, this.receiveCharge );
            prestate.setInt( 16, this.happyBalance );
            prestate.setInt( 17, this.accrecvAmount );
            prestate.setInt( 18, this.reconcileAmount );
            prestate.setInt( 19, this.accrecvBalance );
            prestate.setString( 20, this.remarks );
            prestate.setString( 21, this.correction );
            prestate.setInt( 22, this.tempSlipNo );
            prestate.setInt( 23, this.firstAccrecvSlipNo );
            prestate.setInt( 24, this.creditNoteFlag );
            prestate.setInt( 25, this.invalidFlag );
            prestate.setInt( 26, this.registFlag );
            prestate.setInt( 27, this.closingKind );
            prestate.setString( 28, this.ownerHotelId );
            prestate.setInt( 29, this.ownerUserId );
            prestate.setInt( 30, this.lastUpdate );
            prestate.setInt( 31, this.lastUptime );
            prestate.setInt( 32, this.ciSeq );
            prestate.setInt( 33, this.accrecvSlipNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecv.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * 売掛データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(Connection connection)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;

        query = "UPDATE hh_bko_account_recv SET";
        query = query + " hotel_id = ?,";
        query = query + " id = ?,";
        query = query + " add_up_date = ?,";
        query = query + " slip_update = ?,";
        query = query + " bill_cd = ?,";
        query = query + " bill_name = ?,";
        query = query + " div_name = ?,";
        query = query + " person_name = ?,";
        query = query + " user_management_no = ?,";
        query = query + " usage_date = ?,";
        query = query + " usage_time = ?,";
        query = query + " ht_slip_no = ?,";
        query = query + " ht_room_no = ?,";
        query = query + " usage_charge = ?,";
        query = query + " receive_charge = ?,";
        query = query + " happy_balance = ?,";
        query = query + " accrecv_amount = ?,";
        query = query + " reconcile_amount = ?,";
        query = query + " accrecv_balance = ?,";
        query = query + " remarks = ?,";
        query = query + " correction = ?,";
        query = query + " temp_slip_no = ?,";
        // query = query + " first_accrecv_slip_no = ?,";
        query = query + " credit_note_flag = ?,";
        query = query + " invalid_flag = ?,";
        query = query + " regist_flag = ?,";
        query = query + " closing_kind = ?,";
        query = query + " owner_hotel_id = ?,";
        query = query + " owner_user_id = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " ci_seq = ?";

        query = query + " WHERE accrecv_slip_no = ?";

        ret = false;
        try
        {
            // connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, this.hotelId );
            prestate.setInt( 2, this.id );
            prestate.setInt( 3, this.addUpDate );
            prestate.setInt( 4, this.slipUpdate );
            prestate.setInt( 5, this.billCd );
            prestate.setString( 6, this.billName );
            prestate.setString( 7, this.divName );
            prestate.setString( 8, this.personName );
            prestate.setInt( 9, this.userManagementNo );
            prestate.setInt( 10, this.usageDate );
            prestate.setInt( 11, this.usageTime );
            prestate.setInt( 12, this.htSlipNo );
            prestate.setString( 13, this.htRoomNo );
            prestate.setInt( 14, this.usageCharge );
            prestate.setInt( 15, this.receiveCharge );
            prestate.setInt( 16, this.happyBalance );
            prestate.setInt( 17, this.accrecvAmount );
            prestate.setInt( 18, this.reconcileAmount );
            prestate.setInt( 19, this.accrecvBalance );
            prestate.setString( 20, this.remarks );
            prestate.setString( 21, this.correction );
            prestate.setInt( 22, this.tempSlipNo );
            // prestate.setInt(23, this.firstAccrecvSlipNo );
            prestate.setInt( 23, this.creditNoteFlag );
            prestate.setInt( 24, this.invalidFlag );
            prestate.setInt( 25, this.registFlag );
            prestate.setInt( 26, this.closingKind );
            prestate.setString( 27, this.ownerHotelId );
            prestate.setInt( 28, this.ownerUserId );
            prestate.setInt( 29, this.lastUpdate );
            prestate.setInt( 30, this.lastUptime );
            prestate.setInt( 31, this.ciSeq );
            prestate.setInt( 32, this.accrecvSlipNo );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoAccountRecv.updateData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            // DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * accrecvSlipNoを取得します。
     * 
     * @return accrecvSlipNo
     */
    public int getAccrecvSlipNo()
    {
        return accrecvSlipNo;
    }

    /**
     * accrecvSlipNoを設定します。
     * 
     * @param accrecvSlipNo accrecvSlipNo
     */
    public void setAccrecvSlipNo(int accrecvSlipNo)
    {
        this.accrecvSlipNo = accrecvSlipNo;
    }

    /**
     * hotelIdを取得します。
     * 
     * @return hotelId
     */
    public String getHotelId()
    {
        return hotelId;
    }

    /**
     * hotelIdを設定します。
     * 
     * @param hotelId hotelId
     */
    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    /**
     * idを取得します。
     * 
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * idを設定します。
     * 
     * @param id id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * addUpDateを取得します。
     * 
     * @return addUpDate
     */
    public int getAddUpDate()
    {
        return addUpDate;
    }

    /**
     * addUpDateを設定します。
     * 
     * @param addUpDate addUpDate
     */
    public void setAddUpDate(int addUpDate)
    {
        this.addUpDate = addUpDate;
    }

    /**
     * slipUpdateを取得します。
     * 
     * @return slipUpdate
     */
    public int getSlipUpdate()
    {
        return slipUpdate;
    }

    /**
     * slipUpdateを設定します。
     * 
     * @param slipUpdate slipUpdate
     */
    public void setSlipUpdate(int slipUpdate)
    {
        this.slipUpdate = slipUpdate;
    }

    /**
     * billCdを取得します。
     * 
     * @return billCd
     */
    public int getBillCd()
    {
        return billCd;
    }

    /**
     * billCdを設定します。
     * 
     * @param billCd billCd
     */
    public void setBillCd(int billCd)
    {
        this.billCd = billCd;
    }

    /**
     * billNameを取得します。
     * 
     * @return billName
     */
    public String getBillName()
    {
        return billName;
    }

    /**
     * billNameを設定します。
     * 
     * @param billName billName
     */
    public void setBillName(String billName)
    {
        this.billName = billName;
    }

    /**
     * divNameを取得します。
     * 
     * @return divName
     */
    public String getDivName()
    {
        return divName;
    }

    /**
     * divNameを設定します。
     * 
     * @param divName divName
     */
    public void setDivName(String divName)
    {
        this.divName = divName;
    }

    /**
     * personNameを取得します。
     * 
     * @return personName
     */
    public String getPersonName()
    {
        return personName;
    }

    /**
     * personNameを設定します。
     * 
     * @param personName personName
     */
    public void setPersonName(String personName)
    {
        this.personName = personName;
    }

    /**
     * userManagementNoを取得します。
     * 
     * @return userManagementNo
     */
    public int getUserManagementNo()
    {
        return userManagementNo;
    }

    /**
     * userManagementNoを設定します。
     * 
     * @param userManagementNo userManagementNo
     */
    public void setUserManagementNo(int userManagementNo)
    {
        this.userManagementNo = userManagementNo;
    }

    /**
     * usageDateを取得します。
     * 
     * @return usageDate
     */
    public int getUsageDate()
    {
        return usageDate;
    }

    /**
     * usageDateを設定します。
     * 
     * @param usageDate usageDate
     */
    public void setUsageDate(int usageDate)
    {
        this.usageDate = usageDate;
    }

    /**
     * usageTimeを取得します。
     * 
     * @return usageTime
     */
    public int getUsageTime()
    {
        return usageTime;
    }

    /**
     * usageTimeを設定します。
     * 
     * @param usageTime usageTime
     */
    public void setUsageTime(int usageTime)
    {
        this.usageTime = usageTime;
    }

    /**
     * htSlipNoを取得します。
     * 
     * @return htSlipNo
     */
    public int getHtSlipNo()
    {
        return htSlipNo;
    }

    /**
     * htSlipNoを設定します。
     * 
     * @param htSlipNo htSlipNo
     */
    public void setHtSlipNo(int htSlipNo)
    {
        this.htSlipNo = htSlipNo;
    }

    /**
     * htRoomNoを取得します。
     * 
     * @return htRoomNo
     */
    public String getHtRoomNo()
    {
        return htRoomNo;
    }

    /**
     * htRoomNoを設定します。
     * 
     * @param htRoomNo htRoomNo
     */
    public void setHtRoomNo(String htRoomNo)
    {
        this.htRoomNo = htRoomNo;
    }

    /**
     * usageChargeを取得します。
     * 
     * @return usageCharge
     */
    public int getUsageCharge()
    {
        return usageCharge;
    }

    /**
     * usageChargeを設定します。
     * 
     * @param usageCharge usageCharge
     */
    public void setUsageCharge(int usageCharge)
    {
        this.usageCharge = usageCharge;
    }

    /**
     * receiveChargeを取得します。
     * 
     * @return receiveCharge
     */
    public int getReceiveCharge()
    {
        return receiveCharge;
    }

    /**
     * receiveChargeを設定します。
     * 
     * @param receiveCharge receiveCharge
     */
    public void setReceiveCharge(int receiveCharge)
    {
        this.receiveCharge = receiveCharge;
    }

    /**
     * happyBalanceを取得します。
     * 
     * @return happyBalance
     */
    public int getHappyBalance()
    {
        return happyBalance;
    }

    /**
     * happyBalanceを設定します。
     * 
     * @param happyBalance happyBalance
     */
    public void setHappyBalance(int happyBalance)
    {
        this.happyBalance = happyBalance;
    }

    /**
     * accrecvAmountを取得します。
     * 
     * @return accrecvAmount
     */
    public int getAccrecvAmount()
    {
        return accrecvAmount;
    }

    /**
     * accrecvAmountを設定します。
     * 
     * @param accrecvAmount accrecvAmount
     */
    public void setAccrecvAmount(int accrecvAmount)
    {
        this.accrecvAmount = accrecvAmount;
    }

    /**
     * reconcileAmountを取得します。
     * 
     * @return reconcileAmount
     */
    public int getReconcileAmount()
    {
        return reconcileAmount;
    }

    /**
     * reconcileAmountを設定します。
     * 
     * @param reconcileAmount reconcileAmount
     */
    public void setReconcileAmount(int reconcileAmount)
    {
        this.reconcileAmount = reconcileAmount;
    }

    /**
     * accrecvBalanceを取得します。
     * 
     * @return accrecvBalance
     */
    public int getAccrecvBalance()
    {
        return accrecvBalance;
    }

    /**
     * accrecvBalanceを設定します。
     * 
     * @param accrecvBalance accrecvBalance
     */
    public void setAccrecvBalance(int accrecvBalance)
    {
        this.accrecvBalance = accrecvBalance;
    }

    /**
     * remarksを取得します。
     * 
     * @return remarks
     */
    public String getRemarks()
    {
        return remarks;
    }

    /**
     * remarksを設定します。
     * 
     * @param remarks remarks
     */
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    /**
     * correctionを取得します。
     * 
     * @return correction
     */
    public String getCorrection()
    {
        return correction;
    }

    /**
     * correctionを設定します。
     * 
     * @param correction correction
     */
    public void setCorrection(String correction)
    {
        this.correction = correction;
    }

    /**
     * tempSlipNoを取得します。
     * 
     * @return tempSlipNo
     */
    public int getTempSlipNo()
    {
        return tempSlipNo;
    }

    /**
     * tempSlipNoを設定します。
     * 
     * @param tempSlipNo tempSlipNo
     */
    public void setTempSlipNo(int tempSlipNo)
    {
        this.tempSlipNo = tempSlipNo;
    }

    /**
     * firstAccrecvSlipNoを取得します。
     * 
     * @return firstAccrecvSlipNo
     */
    public int getFirstAccrecvSlipNo()
    {
        return firstAccrecvSlipNo;
    }

    /**
     * firstAccrecvSlipNoを設定します。
     * 
     * @param firstAccrecvSlipNo firstAccrecvSlipNo
     */
    public void setFirstAccrecvSlipNo(int firstAccrecvSlipNo)
    {
        this.firstAccrecvSlipNo = firstAccrecvSlipNo;
    }

    /**
     * creditNoteFlagを取得します。
     * 
     * @return creditNoteFlag
     */
    public int getCreditNoteFlag()
    {
        return creditNoteFlag;
    }

    /**
     * creditNoteFlagを設定します。
     * 
     * @param creditNoteFlag creditNoteFlag
     */
    public void setCreditNoteFlag(int creditNoteFlag)
    {
        this.creditNoteFlag = creditNoteFlag;
    }

    /**
     * invalidFlagを取得します。
     * 
     * @return invalidFlag
     */
    public int getInvalidFlag()
    {
        return invalidFlag;
    }

    /**
     * invalidFlagを設定します。
     * 
     * @param invalidFlag invalidFlag
     */
    public void setInvalidFlag(int invalidFlag)
    {
        this.invalidFlag = invalidFlag;
    }

    /**
     * registFlagを取得します。
     * 
     * @return registFlag
     */
    public int getRegistFlag()
    {
        return registFlag;
    }

    /**
     * registFlagを設定します。
     * 
     * @param registFlag registFlag
     */
    public void setRegistFlag(int registFlag)
    {
        this.registFlag = registFlag;
    }

    /**
     * closingKindを取得します。
     * 
     * @return closingKind
     */
    public int getClosingKind()
    {
        return closingKind;
    }

    /**
     * closingKindを設定します。
     * 
     * @param closingKind closingKind
     */
    public void setClosingKind(int closingKind)
    {
        this.closingKind = closingKind;
    }

    /**
     * ownerHotelIdを取得します。
     * 
     * @return ownerHotelId
     */
    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    /**
     * ownerHotelIdを設定します。
     * 
     * @param ownerHotelId ownerHotelId
     */
    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    /**
     * ownerUserIdを取得します。
     * 
     * @return ownerUserId
     */
    public int getOwnerUserId()
    {
        return ownerUserId;
    }

    /**
     * ownerUserIdを設定します。
     * 
     * @param ownerUserId ownerUserId
     */
    public void setOwnerUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    /**
     * lastUpdateを取得します。
     * 
     * @return lastUpdate
     */
    public int getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * lastUpdateを設定します。
     * 
     * @param lastUpdate lastUpdate
     */
    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    /**
     * lastUptimeを取得します。
     * 
     * @return lastUptime
     */
    public int getLastUptime()
    {
        return lastUptime;
    }

    /**
     * lastUptimeを設定します。
     * 
     * @param lastUptime lastUptime
     */
    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /**
     * ciSeqを取得します。
     * 
     * @return ciSeq
     */
    public int getCiSeq()
    {
        return ciSeq;
    }

    /**
     * ciSeqを設定します。
     * 
     * @param ciSeq ciSeq
     */
    public void setCiSeq(int ciSeq)
    {
        this.ciSeq = ciSeq;
    }

}
