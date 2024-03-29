/*
 * @(#)DataMasterPref.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 s¹{§æ¾NX
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * s¹{§æ¾NX
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMasterPref implements Serializable
{
    private static final long serialVersionUID = -6185468558334380773L;

    /** s¹{§ID **/
    private int               prefId;
    /** s¹{§¼Ì **/
    private String            name;
    /** s¹{§¼ÌJi **/
    private String            nameKana;
    /** nûID **/
    private int               localId;
    /** \¦ **/
    private int               dispIndex;
    /** õ¶ñP **/
    private String            findStr1;
    /** õ¶ñQ **/
    private String            findStr2;
    /** õ¶ñR **/
    private String            findStr3;
    /** õ¶ñS **/
    private String            findStr4;
    /** õ¶ñT **/
    private String            findStr5;
    /** õ¶ñU **/
    private String            findStr6;
    /** õ¶ñV **/
    private String            findStr7;
    /** õ¶ñW **/
    private String            findStr8;
    /** õ¶ñX **/
    private String            findStr9;
    /** õ¶ñPO **/
    private String            findStr10;
    /** X|T[GAR[h **/
    private int               sponsorAreaCode;

    /**
     * f[^ðú»µÜ·B
     */
    public DataMasterPref()
    {
        prefId = 0;
        name = "";
        nameKana = "";
        localId = 0;
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

    public int getLocalId()
    {
        return localId;
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

    public void setLocalId(int localId)
    {
        this.localId = localId;
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
     * s¹{§f[^æ¾
     * 
     * @param prefCode s¹{§R[h
     * @return Ê(TRUE:³í,FALSE:Ùí)
     */
    public boolean getData(int prefCode)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_pref WHERE pref_id = ?";
        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, prefCode );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.prefId = result.getInt( "pref_id" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.localId = result.getInt( "local_id" );
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
            Logging.error( "[DataMasterPref.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * s¹{§f[^Ýè
     * 
     * @param result s¹{§f[^R[h
     * @return Ê(TRUE:³í,FALSE:Ùí)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.prefId = result.getInt( "pref_id" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.localId = result.getInt( "local_id" );
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
            Logging.error( "[DataMasterPref.setData] Exception=" + e.toString() );
        }

        return(true);
    }
}
