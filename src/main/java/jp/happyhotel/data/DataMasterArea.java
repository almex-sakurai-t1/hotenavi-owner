/*
 * @(#)DataMasterArea.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 �G���A�擾�N���X
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * �G���A�擾�N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMasterArea implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 2970715172377923788L;

    /** �G���AID **/
    private int               areaId;
    /** �G���A���� **/
    private String            name;
    /** �G���A���̃J�i **/
    private String            nameKana;
    /** �s���{��ID **/
    private int               prefId;
    /** �s�撬��ID **/
    private int               jisCode;
    /** �\������ **/
    private int               dispIndex;
    /** ����������P **/
    private String            findStr1;
    /** ����������Q **/
    private String            findStr2;
    /** ����������R **/
    private String            findStr3;
    /** ����������S **/
    private String            findStr4;
    /** ����������T **/
    private String            findStr5;
    /** ����������U **/
    private String            findStr6;
    /** ����������V **/
    private String            findStr7;
    /** ����������W **/
    private String            findStr8;
    /** ����������X **/
    private String            findStr9;
    /** ����������P�O **/
    private String            findStr10;
    /** �X�|���T�[�G���A�R�[�h **/
    private int               sponsorAreaCode;
    /** ���S�_�{��ID **/
    private String            pointId;

    /**
     * �f�[�^�����������܂��B
     */
    public DataMasterArea()
    {
        areaId = 0;
        name = "";
        nameKana = "";
        prefId = 0;
        jisCode = 0;
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
        pointId = "";
    }

    public int getAreaId()
    {
        return areaId;
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

    public String getPointId()
    {
        return pointId;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public int getSponsorAreaCode()
    {
        return sponsorAreaCode;
    }

    public void setAreaId(int areaId)
    {
        this.areaId = areaId;
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

    public void setPointId(String pointId)
    {
        this.pointId = pointId;
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
     * �G���A�f�[�^�擾
     * 
     * @param areaCode �G���A�R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int areaCode)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_area WHERE area_id = ?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, areaCode );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.areaId = result.getInt( "area_id" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.prefId = result.getInt( "pref_id" );
                    this.jisCode = result.getInt( "jis_code" );
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
                    this.pointId = result.getString( "point_id" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterArea.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * �G���A�f�[�^�ݒ�
     * 
     * @param result �G���A�f�[�^���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.areaId = result.getInt( "area_id" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.prefId = result.getInt( "pref_id" );
                this.jisCode = result.getInt( "jis_code" );
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
                this.pointId = result.getString( "point_id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterArea.setData] Exception=" + e.toString() );
        }

        return(true);
    }
}
