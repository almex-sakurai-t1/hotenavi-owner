/*
 * @(#)DataBkoPartyContract 1.00 2011/04/15 Copyright (C) ALMEX Inc. 2007 �_��҃}�X�^�擾�N���X
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * �_��҃}�X�^�擾�N���X
 *
 * @author J.Horie
 * @version 1.00 2011/04/20
 */
public class DataBkoPartyContract implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -5804133719367476160L;

    private int               partyContractCd;
    private String            partyContractName;
    private String            zipCode;
    private int               prefCode;
    private int               jisCode;
    private String            address1;
    private String            address2;
    private String            address3;
    private String            tel;

    /**
     * �f�[�^�����������܂��B
     */
    public DataBkoPartyContract()
    {
        partyContractCd = 0;
        partyContractName = "";
        zipCode = "";
        prefCode = 0;
        jisCode = 0;
        address1 = "";
        address2 = "";
        address3 = "";
        tel = "";
    }

    /**
     * �_��҃}�X�^�擾
     *
     * @param partyContractCd �_��҃R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int partyContractCd)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_bko_party_contract WHERE party_contract_cd = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, partyContractCd );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.partyContractCd = result.getInt( "party_contract_cd" );
                    this.partyContractName = result.getString( "party_contract_name" );
                    this.zipCode = result.getString( "zip_code" );
                    this.prefCode = result.getInt( "pref_code" );
                    this.jisCode = result.getInt( "jis_code" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.address3 = result.getString( "address3" );
                    this.tel = result.getString( "tel" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataBkoPartyContract.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * partyContractCd���擾���܂��B
     *
     * @return partyContractCd
     */
    public int getPartyContractCd()
    {
        return partyContractCd;
    }

    /**
     * partyContractCd��ݒ肵�܂��B
     *
     * @param partyContractCd partyContractCd
     */
    public void setPartyContractCd(int partyContractCd)
    {
        this.partyContractCd = partyContractCd;
    }

    /**
     * partyContractName���擾���܂��B
     *
     * @return partyContractName
     */
    public String getPartyContractName()
    {
        return partyContractName;
    }

    /**
     * partyContractName��ݒ肵�܂��B
     *
     * @param partyContractName partyContractName
     */
    public void setPartyContractName(String partyContractName)
    {
        this.partyContractName = partyContractName;
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
}
