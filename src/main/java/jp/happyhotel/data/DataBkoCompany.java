/*
 * @(#)DataBkoCompany 1.00 2011/04/15 Copyright (C) ALMEX Inc. 2007 ���Ѓ}�X�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ���Ѓ}�X�^�擾�N���X
 *
 * @author J.Horie
 * @version 1.00 2011/04/20
 */
public class DataBkoCompany implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -5804133719367476160L;

    private int               companyCd;
    private String            companyName;
    private String            departmentName;
    private String            zipCode;
    private int               prefCode;
    private int               jisCode;
    private String            address1;
    private String            address2;
    private String            address3;
    private String            tel;
    private String            fax;
    private String            bankForTransfer;
    private String            accountName;

    /**
     * �f�[�^�����������܂��B
     */
    public DataBkoCompany()
    {
        companyCd = 0;
        companyName = "";
        departmentName = "";
        zipCode = "";
        prefCode = 0;
        jisCode = 0;
        address1 = "";
        address2 = "";
        address3 = "";
        tel = "";
        fax = "";
        bankForTransfer = "";
        accountName = "";
    }

    /**
     * ���Ѓ}�X�^�擾
     *
     * @param companyCd ��ЃR�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int companyCd)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_bko_company WHERE company_cd = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, companyCd );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.companyCd = result.getInt( "company_cd" );
                    this.companyName = result.getString( "company_name" );
                    this.departmentName = result.getString( "department_name" );
                    this.zipCode = result.getString( "zip_code" );
                    this.prefCode = result.getInt( "pref_code" );
                    this.jisCode = result.getInt( "jis_code" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.address3 = result.getString( "address3" );
                    this.tel = result.getString( "tel" );
                    this.fax = result.getString( "fax" );
                    this.bankForTransfer = result.getString( "bank_for_transfer" );
                    this.accountName = result.getString( "account_name" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoCompany.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * companyCd���擾���܂��B
     *
     * @return companyCd
     */
    public int getCompanyCd()
    {
        return companyCd;
    }

    /**
     * companyCd��ݒ肵�܂��B
     *
     * @param companyCd companyCd
     */
    public void setCompanyCd(int companyCd)
    {
        this.companyCd = companyCd;
    }

    /**
     * companyName���擾���܂��B
     *
     * @return companyName
     */
    public String getCompanyName()
    {
        return companyName;
    }

    /**
     * companyName��ݒ肵�܂��B
     *
     * @param companyName companyName
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    /**
     * departmentName���擾���܂��B
     *
     * @return departmentName
     */
    public String getDepartmentName()
    {
        return departmentName;
    }

    /**
     * departmentName��ݒ肵�܂��B
     *
     * @param departmentName departmentName
     */
    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    /**
     * zipCode���擾���܂��B
     *
     * @return zipCode
     */
    public String getZipCode()
    {
        return zipCode;
    }

    /**
     * zipCode��ݒ肵�܂��B
     *
     * @param zipCode zipCode
     */
    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    /**
     * prefCode���擾���܂��B
     *
     * @return prefCode
     */
    public int getPrefCode()
    {
        return prefCode;
    }

    /**
     * prefCode��ݒ肵�܂��B
     *
     * @param prefCode prefCode
     */
    public void setPrefCode(int prefCode)
    {
        this.prefCode = prefCode;
    }

    /**
     * jisCode���擾���܂��B
     *
     * @return jisCode
     */
    public int getJisCode()
    {
        return jisCode;
    }

    /**
     * jisCode��ݒ肵�܂��B
     *
     * @param jisCode jisCode
     */
    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    /**
     * address1���擾���܂��B
     *
     * @return address1
     */
    public String getAddress1()
    {
        return address1;
    }

    /**
     * address1��ݒ肵�܂��B
     *
     * @param address1 address1
     */
    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    /**
     * address2���擾���܂��B
     *
     * @return address2
     */
    public String getAddress2()
    {
        return address2;
    }

    /**
     * address2��ݒ肵�܂��B
     *
     * @param address2 address2
     */
    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    /**
     * address3���擾���܂��B
     *
     * @return address3
     */
    public String getAddress3()
    {
        return address3;
    }

    /**
     * address3��ݒ肵�܂��B
     *
     * @param address3 address3
     */
    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    /**
     * tel���擾���܂��B
     *
     * @return tel
     */
    public String getTel()
    {
        return tel;
    }

    /**
     * tel��ݒ肵�܂��B
     *
     * @param tel tel
     */
    public void setTel(String tel)
    {
        this.tel = tel;
    }

    /**
     * fax���擾���܂��B
     * @return fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * fax��ݒ肵�܂��B
     * @param fax fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * bankForTransfer���擾���܂��B
     *
     * @return bankForTransfer
     */
    public String getBankForTransfer()
    {
        return bankForTransfer;
    }

    /**
     * bankForTransfer��ݒ肵�܂��B
     *
     * @param bankForTransfer bankForTransfer
     */
    public void setBankForTransfer(String bankForTransfer)
    {
        this.bankForTransfer = bankForTransfer;
    }

    /**
     * accountName���擾���܂��B
     *
     * @return accountName
     */
    public String getAccountName()
    {
        return accountName;
    }

    /**
     * accountName��ݒ肵�܂��B
     *
     * @param accountName accountName
     */
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

}
