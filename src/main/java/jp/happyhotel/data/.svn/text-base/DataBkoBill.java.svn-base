/*
 * @(#)DataBkoBill 1.00 2011/04/15 Copyright (C) ALMEX Inc. 2007 請求データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 請求データ取得クラス
 * 
 * @author J.Horie
 * @version 1.00 2011/04/15
 */
public class DataBkoBill implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 6057034387863888498L;

    private int               billSlipMo;
    private String            billNo;
    private int               billCd;
    private String            billName;
    private String            billNameKana;
    private int               billDate;
    private int               chargeIncTax;
    private int               chargeNotIncTax;
    private int               tax;
    private int               billIssueDate;
    private int               paymentDate;
    private int               depositDate;
    private int               id;
    private String            hotelName;
    private int               issueFlag;
    private int               reissueFlag;
    private String            billZipCode;
    private int               billPrefCode;
    private int               billJisCode;
    private String            billAddress1;
    private String            billAddress2;
    private String            billAddress3;
    private String            billTel;
    private String            billDivName;
    private String            billPositionTitle;
    private String            billPersonName;
    private int               closingKind;
    private String            ownerHotelId;
    private int               ownerUserId;
    private int               lastUpdate;
    private int               lastUptime;

    /**
     * データを初期化します。
     */
    public DataBkoBill()
    {
        billSlipMo = 0;
        billNo = "";
        billCd = 0;
        billName = "";
        billNameKana = "";
        billDate = 0;
        chargeIncTax = 0;
        chargeNotIncTax = 0;
        tax = 0;
        billIssueDate = 0;
        paymentDate = 0;
        depositDate = 0;
        id = 0;
        hotelName = "";
        issueFlag = 0;
        reissueFlag = 0;
        billZipCode = "";
        billPrefCode = 0;
        billJisCode = 0;
        billAddress1 = "";
        billAddress2 = "";
        billAddress3 = "";
        billTel = "";
        billDivName = "";
        billPositionTitle = "";
        billPersonName = "";
        closingKind = 0;
        ownerHotelId = "";
        ownerUserId = 0;
        lastUpdate = 0;
        lastUptime = 0;
    }

    /**
     * 請求データ取得
     * 
     * @param billSlipNo 請求伝票No
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int billSlipNo)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_bko_bill WHERE bill_slip_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, billSlipNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.billSlipMo = result.getInt( "bill_slip_no" );
                    this.billNo = result.getString( "bill_no" );
                    this.billCd = result.getInt( "bill_cd" );
                    this.billName = result.getString( "bill_name" );
                    this.billNameKana = result.getString( "bill_name_kana" );
                    this.billDate = result.getInt( "bill_date" );
                    this.chargeIncTax = result.getInt( "charge_inc_tax" );
                    this.chargeNotIncTax = result.getInt( "charge_not_inc_tax" );
                    this.tax = result.getInt( "tax" );
                    this.billIssueDate = result.getInt( "bill_issue_date" );
                    this.paymentDate = result.getInt( "payment_date" );
                    this.depositDate = result.getInt( "deposit_date" );
                    this.id = result.getInt( "id" );
                    this.hotelName = result.getString( "hotel_name" );
                    this.issueFlag = result.getInt( "issue_flag" );
                    this.reissueFlag = result.getInt( "reissue_flag" );
                    this.billZipCode = result.getString( "bill_zip_code" );
                    this.billPrefCode = result.getInt( "bill_pref_code" );
                    this.billJisCode = result.getInt( "bill_jis_code" );
                    this.billAddress1 = result.getString( "bill_address1" );
                    this.billAddress2 = result.getString( "bill_address2" );
                    this.billAddress3 = result.getString( "bill_address3" );
                    this.billTel = result.getString( "bill_tel" );
                    this.billDivName = result.getString( "bill_div_name" );
                    this.billPositionTitle = result.getString( "bill_position_title" );
                    this.billPersonName = result.getString( "bill_person_name" );
                    this.closingKind = result.getInt( "closing_kind" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoBill.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 請求データ取得
     * 
     * @param id ホテルID
     * @param billDate 請求年月
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int id, int billDate)
    {
        boolean ret = false;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * ";
        query = query + "FROM hh_bko_bill bko ";
        query = query + " INNER JOIN hh_hotel_newhappie happie ON bko.id = happie.id AND bko.bill_issue_date >= happie.bko_date_start ";
        query = query + "WHERE bko.id = ? ";
        query = query + "AND bko.bill_issue_date > ? AND bko.bill_issue_date < ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, id );
            prestate.setInt( 2, billDate * 100 );
            prestate.setInt( 3, (billDate + 1) * 100 );
            result = prestate.executeQuery();
            // 請求合計金額クリア
            this.chargeIncTax = 0;
            this.chargeNotIncTax = 0;
            if ( result != null )
            {
                while( result.next() != false )
                {
                    this.billSlipMo = result.getInt( "bill_slip_no" );
                    this.billNo = result.getString( "bill_no" );
                    this.billCd = result.getInt( "bill_cd" );
                    this.billName = result.getString( "bill_name" );
                    this.billNameKana = result.getString( "bill_name_kana" );
                    this.billDate = result.getInt( "bill_date" );
                    this.chargeIncTax += result.getInt( "charge_inc_tax" );
                    this.chargeNotIncTax += result.getInt( "charge_not_inc_tax" );
                    this.tax = result.getInt( "tax" );
                    this.billIssueDate = result.getInt( "bill_issue_date" );
                    this.paymentDate = result.getInt( "payment_date" );
                    this.depositDate = result.getInt( "deposit_date" );
                    this.id = result.getInt( "id" );
                    this.hotelName = result.getString( "hotel_name" );
                    this.issueFlag = result.getInt( "issue_flag" );
                    this.reissueFlag = result.getInt( "reissue_flag" );
                    this.billZipCode = result.getString( "bill_zip_code" );
                    this.billPrefCode = result.getInt( "bill_pref_code" );
                    this.billJisCode = result.getInt( "bill_jis_code" );
                    this.billAddress1 = result.getString( "bill_address1" );
                    this.billAddress2 = result.getString( "bill_address2" );
                    this.billAddress3 = result.getString( "bill_address3" );
                    this.billTel = result.getString( "bill_tel" );
                    this.billDivName = result.getString( "bill_div_name" );
                    this.billPositionTitle = result.getString( "bill_position_title" );
                    this.billPersonName = result.getString( "bill_person_name" );
                    this.closingKind = result.getInt( "closing_kind" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoBill.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * billSlipMoを取得します。
     * 
     * @return billSlipMo
     */
    public int getBillSlipMo()
    {
        return billSlipMo;
    }

    /**
     * billSlipMoを設定します。
     * 
     * @param billSlipMo billSlipMo
     */
    public void setBillSlipMo(int billSlipMo)
    {
        this.billSlipMo = billSlipMo;
    }

    /**
     * billNoを取得します。
     * 
     * @return billNo
     */
    public String getBillNo()
    {
        return billNo;
    }

    /**
     * billNoを設定します。
     * 
     * @param billNo billNo
     */
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
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
     * billNameKanaを取得します。
     * 
     * @return billNameKana
     */
    public String getBillNameKana()
    {
        return billNameKana;
    }

    /**
     * billNameKanaを設定します。
     * 
     * @param billNameKana billNameKana
     */
    public void setBillNameKana(String billNameKana)
    {
        this.billNameKana = billNameKana;
    }

    /**
     * billDateを取得します。
     * 
     * @return billDate
     */
    public int getBillDate()
    {
        return billDate;
    }

    /**
     * billDateを設定します。
     * 
     * @param billDate billDate
     */
    public void setBillDate(int billDate)
    {
        this.billDate = billDate;
    }

    /**
     * chargeIncTaxを取得します。
     * 
     * @return chargeIncTax
     */
    public int getChargeIncTax()
    {
        return chargeIncTax;
    }

    /**
     * chargeIncTaxを設定します。
     * 
     * @param chargeIncTax chargeIncTax
     */
    public void setChargeIncTax(int chargeIncTax)
    {
        this.chargeIncTax = chargeIncTax;
    }

    /**
     * chargeNotIncTaxを取得します。
     * 
     * @return chargeNotIncTax
     */
    public int getChargeNotIncTax()
    {
        return chargeNotIncTax;
    }

    /**
     * chargeNotIncTaxを設定します。
     * 
     * @param chargeNotIncTax chargeNotIncTax
     */
    public void setChargeNotIncTax(int chargeNotIncTax)
    {
        this.chargeNotIncTax = chargeNotIncTax;
    }

    /**
     * taxを取得します。
     * 
     * @return tax
     */
    public int getTax()
    {
        return tax;
    }

    /**
     * taxを設定します。
     * 
     * @param tax tax
     */
    public void setTax(int tax)
    {
        this.tax = tax;
    }

    /**
     * billIssueDateを取得します。
     * 
     * @return billIssueDate
     */
    public int getBillIssueDate()
    {
        return billIssueDate;
    }

    /**
     * billIssueDateを設定します。
     * 
     * @param billIssueDate billIssueDate
     */
    public void setBillIssueDate(int billIssueDate)
    {
        this.billIssueDate = billIssueDate;
    }

    /**
     * paymentDateを取得します。
     * 
     * @return paymentDate
     */
    public int getPaymentDate()
    {
        return paymentDate;
    }

    /**
     * paymentDateを設定します。
     * 
     * @param paymentDate paymentDate
     */
    public void setPaymentDate(int paymentDate)
    {
        this.paymentDate = paymentDate;
    }

    /**
     * depositDateを取得します。
     * 
     * @return depositDate
     */
    public int getDepositDate()
    {
        return depositDate;
    }

    /**
     * depositDateを設定します。
     * 
     * @param depositDate depositDate
     */
    public void setDepositDate(int depositDate)
    {
        this.depositDate = depositDate;
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
     * hotelNameを取得します。
     * 
     * @return hotelName
     */
    public String getHotelName()
    {
        return hotelName;
    }

    /**
     * hotelNameを設定します。
     * 
     * @param hotelName hotelName
     */
    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    /**
     * issueFlagを取得します。
     * 
     * @return issueFlag
     */
    public int getIssueFlag()
    {
        return issueFlag;
    }

    /**
     * issueFlagを設定します。
     * 
     * @param issueFlag issueFlag
     */
    public void setIssueFlag(int issueFlag)
    {
        this.issueFlag = issueFlag;
    }

    /**
     * reissueFlagを取得します。
     * 
     * @return reissueFlag
     */
    public int getReissueFlag()
    {
        return reissueFlag;
    }

    /**
     * reissueFlagを設定します。
     * 
     * @param reissueFlag reissueFlag
     */
    public void setReissueFlag(int reissueFlag)
    {
        this.reissueFlag = reissueFlag;
    }

    /**
     * billZipCodeを取得します。
     * 
     * @return billZipCode
     */
    public String getBillZipCode()
    {
        return billZipCode;
    }

    /**
     * billZipCodeを設定します。
     * 
     * @param billZipCode billZipCode
     */
    public void setBillZipCode(String billZipCode)
    {
        this.billZipCode = billZipCode;
    }

    /**
     * billPrefCodeを取得します。
     * 
     * @return billPrefCode
     */
    public int getBillPrefCode()
    {
        return billPrefCode;
    }

    /**
     * billPrefCodeを設定します。
     * 
     * @param billPrefCode billPrefCode
     */
    public void setBillPrefCode(int billPrefCode)
    {
        this.billPrefCode = billPrefCode;
    }

    /**
     * billJisCodeを取得します。
     * 
     * @return billJisCode
     */
    public int getBillJisCode()
    {
        return billJisCode;
    }

    /**
     * billJisCodeを設定します。
     * 
     * @param billJisCode billJisCode
     */
    public void setBillJisCode(int billJisCode)
    {
        this.billJisCode = billJisCode;
    }

    /**
     * billAddress1を取得します。
     * 
     * @return billAddress1
     */
    public String getBillAddress1()
    {
        return billAddress1;
    }

    /**
     * billAddress1を設定します。
     * 
     * @param billAddress1 billAddress1
     */
    public void setBillAddress1(String billAddress1)
    {
        this.billAddress1 = billAddress1;
    }

    /**
     * billAddress2を取得します。
     * 
     * @return billAddress2
     */
    public String getBillAddress2()
    {
        return billAddress2;
    }

    /**
     * billAddress2を設定します。
     * 
     * @param billAddress2 billAddress2
     */
    public void setBillAddress2(String billAddress2)
    {
        this.billAddress2 = billAddress2;
    }

    /**
     * billAddress3を取得します。
     * 
     * @return billAddress3
     */
    public String getBillAddress3()
    {
        return billAddress3;
    }

    /**
     * billAddress3を設定します。
     * 
     * @param billAddress3 billAddress3
     */
    public void setBillAddress3(String billAddress3)
    {
        this.billAddress3 = billAddress3;
    }

    /**
     * billTelを取得します。
     * 
     * @return billTel
     */
    public String getBillTel()
    {
        return billTel;
    }

    /**
     * billTelを設定します。
     * 
     * @param billTel billTel
     */
    public void setBillTel(String billTel)
    {
        this.billTel = billTel;
    }

    /**
     * billDivNameを取得します。
     * 
     * @return billDivName
     */
    public String getBillDivName()
    {
        return billDivName;
    }

    /**
     * billDivNameを設定します。
     * 
     * @param billDivName billDivName
     */
    public void setBillDivName(String billDivName)
    {
        this.billDivName = billDivName;
    }

    /**
     * billPositionTitleを取得します。
     * 
     * @return billPositionTitle
     */
    public String getBillPositionTitle()
    {
        return billPositionTitle;
    }

    /**
     * billPositionTitleを設定します。
     * 
     * @param billPositionTitle billPositionTitle
     */
    public void setBillPositionTitle(String billPositionTitle)
    {
        this.billPositionTitle = billPositionTitle;
    }

    /**
     * billPersonNameを取得します。
     * 
     * @return billPersonName
     */
    public String getBillPersonName()
    {
        return billPersonName;
    }

    /**
     * billPersonNameを設定します。
     * 
     * @param billPersonName billPersonName
     */
    public void setBillPersonName(String billPersonName)
    {
        this.billPersonName = billPersonName;
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
}
