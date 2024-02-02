/*
 * @(#)DataMasterCity.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 地図ポイントマスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;

/**
 * 地図ポイントマスタ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/16
 */
public class DataMapPoint_M2 implements Serializable
{
    private static final long serialVersionUID = -7317009974893855435L;

    /** ID **/
    private String            id;
    /** 属性分類コード **/
    private String            classCode;
    /** JISコード **/
    private int               jisCode;
    /** 緯度 **/
    private int               lat;
    /** 経度 **/
    private int               lon;
    /** HotelCount **/
    private int               mapPointHotelCount;
    /** 座標精度 **/
    private String            mapPrecision;
    /** 目標物名称 **/
    private String            name;
    /** 目標物名称カナ **/
    private String            nameKana;
    /** 付属１ **/
    private String            option1;
    /** 付属２ **/
    private String            option2;
    /** 付属３ **/
    private String            option3;
    /** 付属４ **/
    private String            option4;
    /** 付属５ **/
    private String            option5;
    /** 付属６ **/
    private String            option6;
    /** 付属７ **/
    private String            option7;
    /** 付属８ **/
    private String            option8;
    /** 付属９ **/
    private String            option9;

    /**
     * データを初期化します。
     */
    public DataMapPoint_M2()
    {
        id = "";
        classCode = "";
        jisCode = 0;
        lat = 0;
        lon = 0;
        mapPrecision = "";
        name = "";
        nameKana = "";
        option1 = "";
        option2 = "";
        option3 = "";
        option4 = "";
        option5 = "";
        option6 = "";
        option7 = "";
        option8 = "";
        option9 = "";
    }

    public String getClassCode()
    {
        return classCode;
    }

    public String getId()
    {
        return id;
    }

    public int getJisCode()
    {
        return jisCode;
    }

    public int getLat()
    {
        return lat;
    }

    public int getLon()
    {
        return lon;
    }

    public String getMapPrecision()
    {
        return mapPrecision;
    }

    public String getName()
    {
        int cutIdx;

        name = name.replaceAll( "インターチェンジ", "" );
        name = name.replaceAll( "ジャンクション", "" );
        name = name.replaceAll( "入口", "" );
        name = name.replaceAll( "出口", "" );

        cutIdx = name.indexOf( "（" );
        if ( cutIdx > 0 )
        {
            name = name.substring( 0, cutIdx );
        }
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public String getOption1()
    {
        return option1;
    }

    public String getOption2()
    {
        return option2;
    }

    public String getOption3()
    {
        return option3;
    }

    public String getOption4()
    {
        return option4;
    }

    public String getOption5()
    {
        return option5;
    }

    public String getOption6()
    {
        return option6;
    }

    public String getOption7()
    {
        return option7;
    }

    public String getOption8()
    {
        return option8;
    }

    public String getOption9()
    {
        return option9;
    }

    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setJisCode(int jisCode)
    {
        this.jisCode = jisCode;
    }

    public void setLat(int lat)
    {
        this.lat = lat;
    }

    public void setLon(int lon)
    {
        this.lon = lon;
    }

    public void setMapPrecision(String mapPrecision)
    {
        this.mapPrecision = mapPrecision;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setOption1(String option1)
    {
        this.option1 = option1;
    }

    public void setOption2(String option2)
    {
        this.option2 = option2;
    }

    public void setOption3(String option3)
    {
        this.option3 = option3;
    }

    public void setOption4(String option4)
    {
        this.option4 = option4;
    }

    public void setOption5(String option5)
    {
        this.option5 = option5;
    }

    public void setOption6(String option6)
    {
        this.option6 = option6;
    }

    public void setOption7(String option7)
    {
        this.option7 = option7;
    }

    public void setOption8(String option8)
    {
        this.option8 = option8;
    }

    public void setOption9(String option9)
    {
        this.option9 = option9;
    }

    public int getMapPointHotelCount()
    {
        return mapPointHotelCount;
    }

    public void setMapPointHotelCount(int mapPointHotelCount)
    {
        this.mapPointHotelCount = mapPointHotelCount;
    }

}
