/*
 * @(#)ConvertGeodesic.java 1.00 2007/08/06 Copyright (C) ALMEX Inc. 2007 測地系コンバートクラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

/**
 * 測地系コンバートクラス。
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/06
 */
public class ConvertGeodesic implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -4640465451166545686L;

    private double            m_latWGS;
    private double            m_lonWGS;
    private int               m_latWGSNum;
    private int               m_lonWGSNum;
    private double            m_latTOKYO;
    private double            m_lonTOKYO;
    private int               m_latTOKYONum;
    private int               m_lonTOKYONum;

    /**
     * 測地系コンバートクラスを初期化します。
     * 
     */
    public ConvertGeodesic()
    {
        m_latWGS = 0;
        m_lonWGS = 0;
        m_latWGSNum = 0;
        m_lonWGSNum = 0;
        m_latTOKYO = 0;
        m_lonTOKYO = 0;
        m_latTOKYONum = 0;
        m_lonTOKYONum = 0;
    }

    /**
     * @return m_latTOKYO
     */
    public double getLatTOKYO()
    {
        return(m_latTOKYO);
    }

    /**
     * @return m_latWGS
     */
    public double getLatWGS()
    {
        return(m_latWGS);
    }

    /**
     * @return m_lonTOKYO
     */
    public double getLonTOKYO()
    {
        return(m_lonTOKYO);
    }

    /**
     * @return m_lonWGS
     */
    public double getLonWGS()
    {
        return(m_lonWGS);
    }

    /**
     * @return m_latTOKYONum
     */
    public int getLatTOKYONum()
    {
        return(m_latTOKYONum);
    }

    /**
     * @return m_latWGSNum
     */
    public int getLatWGSNum()
    {
        return(m_latWGSNum);
    }

    /**
     * @return m_lonTOKYONum
     */
    public int getLonTOKYONum()
    {
        return(m_lonTOKYONum);
    }

    /**
     * @return m_lonWGSNum
     */
    public int getLonWGSNum()
    {
        return(m_lonWGSNum);
    }

    /**
     * 世界測地系⇒日本測地系変換
     * 
     * @param latW 緯度（世界測地系）
     * @param lonW 経度（世界測地系）
     */
    public void Wgs2Tokyo(double latW, double lonW)
    {
        m_latTOKYO = latW + (latW * 0.00010695 - lonW * 0.000017464 - 0.0046017);
        m_lonTOKYO = lonW + (latW * 0.000046038 + lonW * 0.000083043 - 0.010040);
    }

    /**
     * 世界測地系⇒世界測地系10進表記
     * 
     * @param latW 緯度（世界測地系）
     * @param lonW 経度（世界測地系）
     */
    public void Wgs2WgsNum(double latW, double lonW)
    {
        m_latWGSNum = (int)(latW * 3600000);
        m_lonWGSNum = (int)(lonW * 3600000);
    }

    /**
     * 世界測地系10進表記⇒世界測地系
     * 
     * @param latWNum 緯度（世界測地系10進表記）
     * @param lonWNum 経度（世界測地系10進表記）
     */
    public void WgsNum2Wgs(int latWNum, int lonWNum)
    {
        m_latWGS = (double)((double)latWNum / (double)3600000);
        m_lonWGS = (double)((double)lonWNum / (double)3600000);
    }

    /**
     * 
     * 日本測地系⇒世界測地系変換
     * 
     * @param latT 緯度（日本測地系）
     * @param lonT 経度（日本測地系）
     */
    public void Tokyo2Wgs(double latT, double lonT)
    {
        m_latWGS = latT - latT * 0.00010695 + lonT * 0.000017464 + 0.0046017;
        m_lonWGS = lonT - latT * 0.000046038 - lonT * 0.000083043 + 0.010040;
    }

    /**
     * 
     * 日本測地系⇒日本測地系10進表記
     * 
     * @param latT 緯度（日本測地系）
     * @param lonT 経度（日本測地系）
     */
    public void Tokyo2TokyoNum(double latT, double lonT)
    {
        m_latTOKYONum = (int)(latT * 3600000);
        m_lonTOKYONum = (int)(lonT * 3600000);
    }

    /**
     * 
     * 日本測地系10進表記⇒日本測地系
     * 
     * @param latTNum 緯度（日本測地系10進表記）
     * @param lonTNum 経度（日本測地系10進表記）
     */
    public void TokyoNum2Tokyo(int latTNum, int lonTNum)
    {
        m_latTOKYO = (double)((double)latTNum / (double)3600000);
        m_lonTOKYO = (double)((double)lonTNum / (double)3600000);
    }

    /**
     * Dms⇒degreeコンバート
     * 
     * @param lat 緯度(999.99.99.99)
     * @param lon 経度(99.99.99.99)
     */
    public void convertDms2Degree(String lat, String lon)
    {
        int i;
        double[] cutLat;
        double[] cutLon;
        Double cutLatD = 0.0;
        Double cutLonD = 0.0;
        String[] cutData;
        ConvertGeodesic cg;

        cg = new ConvertGeodesic();

        // dms→degree変換
        cutData = lat.split( "\\.", 3 );
        cutLat = new double[cutData.length];
        for( i = 0 ; i < cutData.length ; i++ )
        {
            cutLat[i] = Double.valueOf( cutData[i] );
        }
        if ( cutData.length > 2 )
        {
            cutLatD = (double)(cutLat[0]) + (double)(cutLat[1] / 60) + (double)(cutLat[2] / 3600);
        }

        cutData = lon.split( "\\.", 3 );
        cutLon = new double[cutData.length];
        for( i = 0 ; i < cutData.length ; i++ )
        {
            cutLon[i] = Double.valueOf( cutData[i] );
        }
        if ( cutData.length > 2 )
        {
            cutLonD = (double)(cutLon[0]) + (double)(cutLon[1] / 60) + (double)(cutLon[2] / 3600);
        }

        // 世界測地系degree
        m_latWGS = cutLatD;
        m_lonWGS = cutLonD;
        // 世界測地系num変換
        cg.Wgs2WgsNum( m_latWGS, m_lonWGS );
        m_latWGSNum = cg.getLatWGSNum();
        m_lonWGSNum = cg.getLonWGSNum();

        // WGS->Tokyo
        cg.Wgs2Tokyo( cutLatD, cutLonD );

        // 日本測地系degree
        m_latTOKYO = cg.getLatTOKYO();
        m_lonTOKYO = cg.getLonTOKYO();
        // 日本測地系num変換
        cg.Tokyo2TokyoNum( m_latTOKYO, m_lonTOKYO );
        m_latTOKYONum = cg.getLatTOKYONum();
        m_lonTOKYONum = cg.getLonTOKYONum();
    }

    /**
     */
    public void convertDegree(String lat, String lon)
    {
        ConvertGeodesic cg;
        cg = new ConvertGeodesic();

        m_latTOKYO = Double.parseDouble( lat );
        m_lonTOKYO = Double.parseDouble( lon );

        // 日本測地系num変換
        cg.Tokyo2TokyoNum( m_latTOKYO, m_lonTOKYO );
        m_latTOKYONum = cg.getLatTOKYONum();
        m_lonTOKYONum = cg.getLonTOKYONum();
    }

}
