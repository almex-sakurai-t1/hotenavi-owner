/*
 * @(#)DataBkoBill 1.00 2011/04/15 Copyright (C) ALMEX Inc. 2007 �����f�[�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �����f�[�^�擾�N���X
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
     * �f�[�^�����������܂��B
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
     * �����f�[�^�擾
     * 
     * @param billSlipNo �����`�[No
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
     * �����f�[�^�擾
     * 
     * @param id �z�e��ID
     * @param billDate �����N��
     * @return ��������(TRUE:����,FALSE:�ُ�)
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
            // �������v���z�N���A
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
     * billSlipMo���擾���܂��B
     * 
     * @return billSlipMo
     */
    public int getBillSlipMo()
    {
        return billSlipMo;
    }

    /**
     * billSlipMo��ݒ肵�܂��B
     * 
     * @param billSlipMo billSlipMo
     */
    public void setBillSlipMo(int billSlipMo)
    {
        this.billSlipMo = billSlipMo;
    }

    /**
     * billNo���擾���܂��B
     * 
     * @return billNo
     */
    public String getBillNo()
    {
        return billNo;
    }

    /**
     * billNo��ݒ肵�܂��B
     * 
     * @param billNo billNo
     */
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }

    /**
     * billCd���擾���܂��B
     * 
     * @return billCd
     */
    public int getBillCd()
    {
        return billCd;
    }

    /**
     * billCd��ݒ肵�܂��B
     * 
     * @param billCd billCd
     */
    public void setBillCd(int billCd)
    {
        this.billCd = billCd;
    }

    /**
     * billName���擾���܂��B
     * 
     * @return billName
     */
    public String getBillName()
    {
        return billName;
    }

    /**
     * billName��ݒ肵�܂��B
     * 
     * @param billName billName
     */
    public void setBillName(String billName)
    {
        this.billName = billName;
    }

    /**
     * billNameKana���擾���܂��B
     * 
     * @return billNameKana
     */
    public String getBillNameKana()
    {
        return billNameKana;
    }

    /**
     * billNameKana��ݒ肵�܂��B
     * 
     * @param billNameKana billNameKana
     */
    public void setBillNameKana(String billNameKana)
    {
        this.billNameKana = billNameKana;
    }

    /**
     * billDate���擾���܂��B
     * 
     * @return billDate
     */
    public int getBillDate()
    {
        return billDate;
    }

    /**
     * billDate��ݒ肵�܂��B
     * 
     * @param billDate billDate
     */
    public void setBillDate(int billDate)
    {
        this.billDate = billDate;
    }

    /**
     * chargeIncTax���擾���܂��B
     * 
     * @return chargeIncTax
     */
    public int getChargeIncTax()
    {
        return chargeIncTax;
    }

    /**
     * chargeIncTax��ݒ肵�܂��B
     * 
     * @param chargeIncTax chargeIncTax
     */
    public void setChargeIncTax(int chargeIncTax)
    {
        this.chargeIncTax = chargeIncTax;
    }

    /**
     * chargeNotIncTax���擾���܂��B
     * 
     * @return chargeNotIncTax
     */
    public int getChargeNotIncTax()
    {
        return chargeNotIncTax;
    }

    /**
     * chargeNotIncTax��ݒ肵�܂��B
     * 
     * @param chargeNotIncTax chargeNotIncTax
     */
    public void setChargeNotIncTax(int chargeNotIncTax)
    {
        this.chargeNotIncTax = chargeNotIncTax;
    }

    /**
     * tax���擾���܂��B
     * 
     * @return tax
     */
    public int getTax()
    {
        return tax;
    }

    /**
     * tax��ݒ肵�܂��B
     * 
     * @param tax tax
     */
    public void setTax(int tax)
    {
        this.tax = tax;
    }

    /**
     * billIssueDate���擾���܂��B
     * 
     * @return billIssueDate
     */
    public int getBillIssueDate()
    {
        return billIssueDate;
    }

    /**
     * billIssueDate��ݒ肵�܂��B
     * 
     * @param billIssueDate billIssueDate
     */
    public void setBillIssueDate(int billIssueDate)
    {
        this.billIssueDate = billIssueDate;
    }

    /**
     * paymentDate���擾���܂��B
     * 
     * @return paymentDate
     */
    public int getPaymentDate()
    {
        return paymentDate;
    }

    /**
     * paymentDate��ݒ肵�܂��B
     * 
     * @param paymentDate paymentDate
     */
    public void setPaymentDate(int paymentDate)
    {
        this.paymentDate = paymentDate;
    }

    /**
     * depositDate���擾���܂��B
     * 
     * @return depositDate
     */
    public int getDepositDate()
    {
        return depositDate;
    }

    /**
     * depositDate��ݒ肵�܂��B
     * 
     * @param depositDate depositDate
     */
    public void setDepositDate(int depositDate)
    {
        this.depositDate = depositDate;
    }

    /**
     * id���擾���܂��B
     * 
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * id��ݒ肵�܂��B
     * 
     * @param id id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * hotelName���擾���܂��B
     * 
     * @return hotelName
     */
    public String getHotelName()
    {
        return hotelName;
    }

    /**
     * hotelName��ݒ肵�܂��B
     * 
     * @param hotelName hotelName
     */
    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    /**
     * issueFlag���擾���܂��B
     * 
     * @return issueFlag
     */
    public int getIssueFlag()
    {
        return issueFlag;
    }

    /**
     * issueFlag��ݒ肵�܂��B
     * 
     * @param issueFlag issueFlag
     */
    public void setIssueFlag(int issueFlag)
    {
        this.issueFlag = issueFlag;
    }

    /**
     * reissueFlag���擾���܂��B
     * 
     * @return reissueFlag
     */
    public int getReissueFlag()
    {
        return reissueFlag;
    }

    /**
     * reissueFlag��ݒ肵�܂��B
     * 
     * @param reissueFlag reissueFlag
     */
    public void setReissueFlag(int reissueFlag)
    {
        this.reissueFlag = reissueFlag;
    }

    /**
     * billZipCode���擾���܂��B
     * 
     * @return billZipCode
     */
    public String getBillZipCode()
    {
        return billZipCode;
    }

    /**
     * billZipCode��ݒ肵�܂��B
     * 
     * @param billZipCode billZipCode
     */
    public void setBillZipCode(String billZipCode)
    {
        this.billZipCode = billZipCode;
    }

    /**
     * billPrefCode���擾���܂��B
     * 
     * @return billPrefCode
     */
    public int getBillPrefCode()
    {
        return billPrefCode;
    }

    /**
     * billPrefCode��ݒ肵�܂��B
     * 
     * @param billPrefCode billPrefCode
     */
    public void setBillPrefCode(int billPrefCode)
    {
        this.billPrefCode = billPrefCode;
    }

    /**
     * billJisCode���擾���܂��B
     * 
     * @return billJisCode
     */
    public int getBillJisCode()
    {
        return billJisCode;
    }

    /**
     * billJisCode��ݒ肵�܂��B
     * 
     * @param billJisCode billJisCode
     */
    public void setBillJisCode(int billJisCode)
    {
        this.billJisCode = billJisCode;
    }

    /**
     * billAddress1���擾���܂��B
     * 
     * @return billAddress1
     */
    public String getBillAddress1()
    {
        return billAddress1;
    }

    /**
     * billAddress1��ݒ肵�܂��B
     * 
     * @param billAddress1 billAddress1
     */
    public void setBillAddress1(String billAddress1)
    {
        this.billAddress1 = billAddress1;
    }

    /**
     * billAddress2���擾���܂��B
     * 
     * @return billAddress2
     */
    public String getBillAddress2()
    {
        return billAddress2;
    }

    /**
     * billAddress2��ݒ肵�܂��B
     * 
     * @param billAddress2 billAddress2
     */
    public void setBillAddress2(String billAddress2)
    {
        this.billAddress2 = billAddress2;
    }

    /**
     * billAddress3���擾���܂��B
     * 
     * @return billAddress3
     */
    public String getBillAddress3()
    {
        return billAddress3;
    }

    /**
     * billAddress3��ݒ肵�܂��B
     * 
     * @param billAddress3 billAddress3
     */
    public void setBillAddress3(String billAddress3)
    {
        this.billAddress3 = billAddress3;
    }

    /**
     * billTel���擾���܂��B
     * 
     * @return billTel
     */
    public String getBillTel()
    {
        return billTel;
    }

    /**
     * billTel��ݒ肵�܂��B
     * 
     * @param billTel billTel
     */
    public void setBillTel(String billTel)
    {
        this.billTel = billTel;
    }

    /**
     * billDivName���擾���܂��B
     * 
     * @return billDivName
     */
    public String getBillDivName()
    {
        return billDivName;
    }

    /**
     * billDivName��ݒ肵�܂��B
     * 
     * @param billDivName billDivName
     */
    public void setBillDivName(String billDivName)
    {
        this.billDivName = billDivName;
    }

    /**
     * billPositionTitle���擾���܂��B
     * 
     * @return billPositionTitle
     */
    public String getBillPositionTitle()
    {
        return billPositionTitle;
    }

    /**
     * billPositionTitle��ݒ肵�܂��B
     * 
     * @param billPositionTitle billPositionTitle
     */
    public void setBillPositionTitle(String billPositionTitle)
    {
        this.billPositionTitle = billPositionTitle;
    }

    /**
     * billPersonName���擾���܂��B
     * 
     * @return billPersonName
     */
    public String getBillPersonName()
    {
        return billPersonName;
    }

    /**
     * billPersonName��ݒ肵�܂��B
     * 
     * @param billPersonName billPersonName
     */
    public void setBillPersonName(String billPersonName)
    {
        this.billPersonName = billPersonName;
    }

    /**
     * closingKind���擾���܂��B
     * 
     * @return closingKind
     */
    public int getClosingKind()
    {
        return closingKind;
    }

    /**
     * closingKind��ݒ肵�܂��B
     * 
     * @param closingKind closingKind
     */
    public void setClosingKind(int closingKind)
    {
        this.closingKind = closingKind;
    }

    /**
     * ownerHotelId���擾���܂��B
     * 
     * @return ownerHotelId
     */
    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    /**
     * ownerHotelId��ݒ肵�܂��B
     * 
     * @param ownerHotelId ownerHotelId
     */
    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    /**
     * ownerUserId���擾���܂��B
     * 
     * @return ownerUserId
     */
    public int getOwnerUserId()
    {
        return ownerUserId;
    }

    /**
     * ownerUserId��ݒ肵�܂��B
     * 
     * @param ownerUserId ownerUserId
     */
    public void setOwnerUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    /**
     * lastUpdate���擾���܂��B
     * 
     * @return lastUpdate
     */
    public int getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * lastUpdate��ݒ肵�܂��B
     * 
     * @param lastUpdate lastUpdate
     */
    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    /**
     * lastUptime���擾���܂��B
     * 
     * @return lastUptime
     */
    public int getLastUptime()
    {
        return lastUptime;
    }

    /**
     * lastUptime��ݒ肵�܂��B
     * 
     * @param lastUptime lastUptime
     */
    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }
}
