/*
 * @(#)DataMasterZip.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 —X•Ö”Ô†ƒf[ƒ^æ“¾ƒNƒ‰ƒX
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * —X•Ö”Ô†ƒf[ƒ^æ“¾ƒNƒ‰ƒX
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 */
public class DataMasterZip implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -2329952243466258909L;

    /** s‹æ’¬‘ºID **/
    private int               jisCode;
    /** ‹Œ—X•Ö”Ô† **/
    private String            zipOld;
    /** —X•Ö”Ô† **/
    private String            zipCode;
    /** “s“¹•{Œ§–¼ÌƒJƒi **/
    private String            prefKana;
    /** s‹æ’¬‘º–¼ÌƒJƒi **/
    private String            address1Kana;
    /** ’¬ˆæ–¼ƒJƒi **/
    private String            address2Kana;
    /** “s“¹•{Œ§–¼Ì **/
    private String            prefName;
    /** s‹æ’¬‘º–¼Ì **/
    private String            address1Name;
    /** ’¬ˆæ–¼ **/
    private String            address2Name;
    /** ƒtƒ‰ƒO‚P **/
    private int               flag1;
    /** ƒtƒ‰ƒO‚Q **/
    private int               flag2;
    /** ƒtƒ‰ƒO‚R **/
    private int               flag3;
    /** ƒtƒ‰ƒO‚S **/
    private int               flag4;
    /** ƒtƒ‰ƒO‚T **/
    private int               flag5;
    /** ƒtƒ‰ƒO‚U **/
    private int               flag6;

    /**
     * ƒf[ƒ^‚ğ‰Šú‰»‚µ‚Ü‚·B
     */
    public DataMasterZip()
    {
        jisCode = 0;
        zipOld = "";
        zipCode = "";
        prefKana = "";
        address1Kana = "";
        address2Kana = "";
        prefName = "";
        address1Name = "";
        address2Name = "";
        flag1 = 0;
        flag2 = 0;
        flag3 = 0;
        flag4 = 0;
        flag5 = 0;
        flag6 = 0;
    }

    public String getAddress1Kana()
    {
        return address1Kana;
    }

    public String getAddress1Name()
    {
        return address1Name;
    }

    public String getAddress2Kana()
    {
        return address2Kana;
    }

    public String getAddress2Name()
    {
        return address2Name;
    }

    public int getFlag1()
    {
        return flag1;
    }

    public int getFlag2()
    {
        return flag2;
    }

    public int getFlag3()
    {
        return flag3;
    }

    public int getFlag4()
    {
        return flag4;
    }

    public int getFlag5()
    {
        return flag5;
    }

    public int getFlag6()
    {
        return flag6;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public String getPrefKana()
    {
        return prefKana;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public String getZipOld()
    {
        return zipOld;
    }

    public void setAddress1Kana(String address1Kana)
    {
        this.address1Kana = address1Kana;
    }

    public void setAddress1Name(String address1Name)
    {
        this.address1Name = address1Name;
    }

    public void setAddress2Kana(String address2Kana)
    {
        this.address2Kana = address2Kana;
    }

    public void setAddress2Name(String address2Name)
    {
        this.address2Name = address2Name;
    }

    public void setFlag1(int flag1)
    {
        this.flag1 = flag1;
    }

    public void setFlag2(int flag2)
    {
        this.flag2 = flag2;
    }

    public void setFlag3(int flag3)
    {
        this.flag3 = flag3;
    }

    public void setFlag4(int flag4)
    {
        this.flag4 = flag4;
    }

    public void setFlag5(int flag5)
    {
        this.flag5 = flag5;
    }

    public void setFlag6(int flag6)
    {
        this.flag6 = flag6;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public void setPrefKana(String prefKana)
    {
        this.prefKana = prefKana;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setZipOld(String zipOld)
    {
        this.zipOld = zipOld;
    }

    /**
     * —X•Ö”Ô†ƒf[ƒ^æ“¾
     * 
     * @param zipCode ƒ|ƒCƒ“ƒgƒR[ƒh
     * @return ˆ—Œ‹‰Ê(TRUE:³í,FALSE:ˆÙí)
     */
    public boolean getData(String zipCode)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_zip WHERE zip_code = ?";

        count = 0;
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, zipCode );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.jisCode = result.getInt( "jis_code" );
                    this.zipOld = result.getString( "zip_old" );
                    this.zipCode = result.getString( "zip_code" );
                    this.prefKana = result.getString( "pref_kana" );
                    this.address1Kana = result.getString( "address1_kana" );
                    this.address2Kana = result.getString( "address2_kana" );
                    this.prefName = result.getString( "pref_name" );
                    this.address1Name = result.getString( "address1_name" );
                    this.address2Name = result.getString( "address2_name" );
                    this.flag1 = result.getInt( "flag1" );
                    this.flag2 = result.getInt( "flag2" );
                    this.flag3 = result.getInt( "flag3" );
                    this.flag4 = result.getInt( "flag4" );
                    this.flag5 = result.getInt( "flag5" );
                    this.flag6 = result.getInt( "flag6" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterZip.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
            return(true);
        else
            return(false);
    }
}
