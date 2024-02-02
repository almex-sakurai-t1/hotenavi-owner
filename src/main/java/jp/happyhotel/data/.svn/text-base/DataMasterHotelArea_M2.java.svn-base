/*
 * @(#)DataMasterArea.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 エリア取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * エリア取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMasterHotelArea_M2 implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 2970715172377923788L;

    /** エリアID **/
    private int               areaId;
    /** エリア名称 **/
    private String            name;
    /** エリア名称カナ **/
    private String            nameKana;
    /** 都道府県ID **/
    private int               areaPrefId;
    private String            areaPrefName;
    private int               areaLocalId;
    private String            areaLocalName;
    private int               areaHotelCount;
    /** 市区町村ID **/
    private int               jisCode;
    /** 表示順序 **/
    private int               dispIndex;
    /** 検索文字列１ **/
    private String            findStr1;
    /** 検索文字列２ **/
    private String            findStr2;
    /** 検索文字列３ **/
    private String            findStr3;
    /** 検索文字列４ **/
    private String            findStr4;
    /** 検索文字列５ **/
    private String            findStr5;
    /** 検索文字列６ **/
    private String            findStr6;
    /** 検索文字列７ **/
    private String            findStr7;
    /** 検索文字列８ **/
    private String            findStr8;
    /** 検索文字列９ **/
    private String            findStr9;
    /** 検索文字列１０ **/
    private String            findStr10;
    /** スポンサーエリアコード **/
    private int               sponsorAreaCode;
    /** 中心点施設ID **/
    private String            pointId;

    /**
     * データを初期化します。
     */
    public DataMasterHotelArea_M2()
    {
        areaId = 0;
        name = "";
        nameKana = "";
        areaPrefId = 0;
        areaPrefName = "";
        areaLocalId = 0;
        areaLocalName = "";
        areaHotelCount = 0;
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

    public int getAreaLocalId()
    {
        return areaLocalId;
    }

    public void setAreaLocalId(int areaLocalId)
    {
        this.areaLocalId = areaLocalId;
    }

    public int getAreaPrefId()
    {
        return areaPrefId;
    }

    public void setAreaPrefId(int areaPrefId)
    {
        this.areaPrefId = areaPrefId;
    }

    public void setSponsorAreaCode(int sponsorAreaCode)
    {
        this.sponsorAreaCode = sponsorAreaCode;
    }

    /**
     * エリアデータ設定
     * 
     * @param result エリアデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
                this.areaPrefId = result.getInt( "pref_id" );
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

    public String getAreaLocalName()
    {
        return areaLocalName;
    }

    public void setAreaLocalName(String areaLocalName)
    {
        this.areaLocalName = areaLocalName;
    }

    public String getAreaPrefName()
    {
        return areaPrefName;
    }

    public void setAreaPrefName(String areaPrefName)
    {
        this.areaPrefName = areaPrefName;
    }

    public int getAreaHotelCount()
    {
        return areaHotelCount;
    }

    public void setAreaHotelCount(int areaHotelCount)
    {
        this.areaHotelCount = areaHotelCount;
    }
}
