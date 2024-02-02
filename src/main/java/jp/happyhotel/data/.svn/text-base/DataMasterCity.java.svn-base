/*
 * @(#)DataMasterCity.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 s‹æ’¬‘ºæ“¾ƒNƒ‰ƒX
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * s‹æ’¬‘ºæ“¾ƒNƒ‰ƒX
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMasterCity implements Serializable
{
    private static final long serialVersionUID = 2970715172377923788L;

    /** s‹æ’¬‘ºID **/
    private int               jisCode;
    /** s‹æ’¬‘º–¼Ì **/
    private String            name;
    /** s‹æ’¬‘º–¼ÌƒJƒi **/
    private String            nameKana;
    /** “s“¹•{Œ§ID **/
    private int               prefId;
    /** •\¦‡˜ **/
    private int               dispIndex;
    /** ŒŸõ•¶š—ñ‚P **/
    private String            findStr1;
    /** ŒŸõ•¶š—ñ‚Q **/
    private String            findStr2;
    /** ŒŸõ•¶š—ñ‚R **/
    private String            findStr3;
    /** ŒŸõ•¶š—ñ‚S **/
    private String            findStr4;
    /** ŒŸõ•¶š—ñ‚T **/
    private String            findStr5;
    /** ŒŸõ•¶š—ñ‚U **/
    private String            findStr6;
    /** ŒŸõ•¶š—ñ‚V **/
    private String            findStr7;
    /** ŒŸõ•¶š—ñ‚W **/
    private String            findStr8;
    /** ŒŸõ•¶š—ñ‚X **/
    private String            findStr9;
    /** ŒŸõ•¶š—ñ‚P‚O **/
    private String            findStr10;
    /** ƒXƒ|ƒ“ƒT[ƒGƒŠƒAƒR[ƒh **/
    private int               sponsorAreaCode;

    /**
     * ƒf[ƒ^‚ğ‰Šú‰»‚µ‚Ü‚·B
     */
    public DataMasterCity()
    {
        jisCode = 0;
        name = "";
        nameKana = "";
        prefId = 0;
        dispIndex = 0;
        findStr1 = "";
        findStr2 = "";
        findStr3 = "";
        findStr4 = "";
        findStr5 = "";
        findStr6 = "";
        findStr7 = "";
        findStr8 = "";
        findStr9 = "";
        findStr10 = "";
        sponsorAreaCode = 0;
    }

    public int getDispIndex()
    {
        return dispIndex;
    }

    public String getFindStr1()
    {
        return findStr1;
    }

    public String getFindStr10()
    {
        return findStr10;
    }

    public String getFindStr2()
    {
        return findStr2;
    }

    public String getFindStr3()
    {
        return findStr3;
    }

    public String getFindStr4()
    {
        return findStr4;
    }

    public String getFindStr5()
    {
        return findStr5;
    }

    public String getFindStr6()
    {
        return findStr6;
    }

    public String getFindStr7()
    {
        return findStr7;
    }

    public String getFindStr8()
    {
        return findStr8;
    }

    public String getFindStr9()
    {
        return findStr9;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public int getSponsorAreaCode()
    {
        return sponsorAreaCode;
    }

    public void setDispIndex(int dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public void setFindStr1(String findStr1)
    {
        this.findStr1 = findStr1;
    }

    public void setFindStr10(String findStr10)
    {
        this.findStr10 = findStr10;
    }

    public void setFindStr2(String findStr2)
    {
        this.findStr2 = findStr2;
    }

    public void setFindStr3(String findStr3)
    {
        this.findStr3 = findStr3;
    }

    public void setFindStr4(String findStr4)
    {
        this.findStr4 = findStr4;
    }

    public void setFindStr5(String findStr5)
    {
        this.findStr5 = findStr5;
    }

    public void setFindStr6(String findStr6)
    {
        this.findStr6 = findStr6;
    }

    public void setFindStr7(String findStr7)
    {
        this.findStr7 = findStr7;
    }

    public void setFindStr8(String findStr8)
    {
        this.findStr8 = findStr8;
    }

    public void setFindStr9(String findStr9)
    {
        this.findStr9 = findStr9;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setSponsorAreaCode(int sponsorAreaCode)
    {
        this.sponsorAreaCode = sponsorAreaCode;
    }

    /**
     * s‹æ’¬‘ºƒf[ƒ^æ“¾
     * 
     * @param jisCode s‹æ’¬‘ºƒR[ƒh
     * @return ˆ—Œ‹‰Ê(TRUE:³í,FALSE:ˆÙí)
     */
    public boolean getData(int jisCode)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_city WHERE jis_code = ?";
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, jisCode );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.jisCode = result.getInt( "jis_code" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.prefId = result.getInt( "pref_id" );
                    this.dispIndex = result.getInt( "disp_index" );
                    this.findStr1 = result.getString( "find_str1" );
                    this.findStr2 = result.getString( "find_str2" );
                    this.findStr3 = result.getString( "find_str3" );
                    this.findStr4 = result.getString( "find_str4" );
                    this.findStr5 = result.getString( "find_str5" );
                    this.findStr6 = result.getString( "find_str6" );
                    this.findStr7 = result.getString( "find_str7" );
                    this.findStr8 = result.getString( "find_str8" );
                    this.findStr9 = result.getString( "find_str9" );
                    this.findStr10 = result.getString( "find_str10" );
                    this.sponsorAreaCode = result.getInt( "sponsor_area_code" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterCity.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * s‹æ’¬‘ºƒf[ƒ^İ’è
     * 
     * @param result s‹æ’¬‘ºƒf[ƒ^ƒŒƒR[ƒh
     * @return ˆ—Œ‹‰Ê(TRUE:³í,FALSE:ˆÙí)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.jisCode = result.getInt( "jis_code" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.prefId = result.getInt( "pref_id" );
                this.dispIndex = result.getInt( "disp_index" );
                this.findStr1 = result.getString( "find_str1" );
                this.findStr2 = result.getString( "find_str2" );
                this.findStr3 = result.getString( "find_str3" );
                this.findStr4 = result.getString( "find_str4" );
                this.findStr5 = result.getString( "find_str5" );
                this.findStr6 = result.getString( "find_str6" );
                this.findStr7 = result.getString( "find_str7" );
                this.findStr8 = result.getString( "find_str8" );
                this.findStr9 = result.getString( "find_str9" );
                this.findStr10 = result.getString( "find_str10" );
                this.sponsorAreaCode = result.getInt( "sponsor_area_code" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterCity.setData] Exception=" + e.toString() );
        }
        return(true);
    }
}
