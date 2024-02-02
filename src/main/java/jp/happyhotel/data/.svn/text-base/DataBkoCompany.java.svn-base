/*
 * @(#)DataBkoCompany 1.00 2011/04/15 Copyright (C) ALMEX Inc. 2007 自社マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * 自社マスタ取得クラス
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
     * データを初期化します。
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
     * 自社マスタ取得
     *
     * @param companyCd 会社コード
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
     * companyCdを取得します。
     *
     * @return companyCd
     */
    public int getCompanyCd()
    {
        return companyCd;
    }

    /**
     * companyCdを設定します。
     *
     * @param companyCd companyCd
     */
    public void setCompanyCd(int companyCd)
    {
        this.companyCd = companyCd;
    }

    /**
     * companyNameを取得します。
     *
     * @return companyName
     */
    public String getCompanyName()
    {
        return companyName;
    }

    /**
     * companyNameを設定します。
     *
     * @param companyName companyName
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    /**
     * departmentNameを取得します。
     *
     * @return departmentName
     */
    public String getDepartmentName()
    {
        return departmentName;
    }

    /**
     * departmentNameを設定します。
     *
     * @param departmentName departmentName
     */
    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    /**
     * zipCodeを取得します。
     *
     * @return zipCode
     */
    public String getZipCode()
    {
        return zipCode;
    }

    /**
     * zipCodeを設定します。
     *
     * @param zipCode zipCode
     */
    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    /**
     * prefCodeを取得します。
     *
     * @return prefCode
     */
    public int getPrefCode()
    {
        return prefCode;
    }

    /**
     * prefCodeを設定します。
     *
     * @param prefCode prefCode
     */
    public void setPrefCode(int prefCode)
    {
        this.prefCode = prefCode;
    }

    /**
     * jisCodeを取得します。
     *
     * @return jisCode
     */
    public int getJisCode()
    {
        return jisCode;
    }

    /**
     * jisCodeを設定します。
     *
     * @param jisCode jisCode
     */
    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    /**
     * address1を取得します。
     *
     * @return address1
     */
    public String getAddress1()
    {
        return address1;
    }

    /**
     * address1を設定します。
     *
     * @param address1 address1
     */
    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    /**
     * address2を取得します。
     *
     * @return address2
     */
    public String getAddress2()
    {
        return address2;
    }

    /**
     * address2を設定します。
     *
     * @param address2 address2
     */
    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    /**
     * address3を取得します。
     *
     * @return address3
     */
    public String getAddress3()
    {
        return address3;
    }

    /**
     * address3を設定します。
     *
     * @param address3 address3
     */
    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    /**
     * telを取得します。
     *
     * @return tel
     */
    public String getTel()
    {
        return tel;
    }

    /**
     * telを設定します。
     *
     * @param tel tel
     */
    public void setTel(String tel)
    {
        this.tel = tel;
    }

    /**
     * faxを取得します。
     * @return fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * faxを設定します。
     * @param fax fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * bankForTransferを取得します。
     *
     * @return bankForTransfer
     */
    public String getBankForTransfer()
    {
        return bankForTransfer;
    }

    /**
     * bankForTransferを設定します。
     *
     * @param bankForTransfer bankForTransfer
     */
    public void setBankForTransfer(String bankForTransfer)
    {
        this.bankForTransfer = bankForTransfer;
    }

    /**
     * accountNameを取得します。
     *
     * @return accountName
     */
    public String getAccountName()
    {
        return accountName;
    }

    /**
     * accountNameを設定します。
     *
     * @param accountName accountName
     */
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

}
