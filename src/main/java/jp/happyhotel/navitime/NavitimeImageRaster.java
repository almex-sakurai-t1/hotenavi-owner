/*
 * @(#)NavitimeImageRaster.java 1.00 2007/08/28 Copyright (C) ALMEX Inc. 2007 地図画像データ取得クラス
 */

package jp.happyhotel.navitime;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.servlet.http.*;
import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * 地図画像データ取得クラス 地図画像データを取得する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/28
 */
public class NavitimeImageRaster implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 5562853544319831123L;

    private static final int  IMAGESIZE_MAX    = 100000;

    private LogLib            log;

    private String            mapColor;
    private String            mapType;
    private int               mapCenterX;
    private int               mapCenterY;
    private int               mapScale;
    private int               mapZoom;
    private int               mapWidth;
    private int               mapHeight;
    private int               mapAngle;
    private int               maxBytes;
    private int               mapMoveH;
    private int               mapMoveV;
    private int               mapX;
    private int               mapY;
    private int               drawScale;
    private int               drawDirection;
    private int               drawTarget;
    private int               mapMobile;
    private int               mapMobileX;
    private int               mapMobileY;
    private String            palette;
    private int               iconCount;
    private int[]             iconId;
    private int[]             iconLat;
    private int[]             iconLon;
    private String[]          iconName;
    private String[]          iconURL;
    private int[]             iconDisp;
    private int[]             iconNameDisp;
    private int[]             iconDispPos;
    private int               routeCount;
    private int[]             routeStartIconIdx;
    private String[]          routeStartNode;
    private String[]          routeStartTime;
    private int[]             routeEndIconIdx;
    private String[]          routeEndNode;
    private String[]          routeEndTime;
    private String[]          routeLineId;
    private String[]          routeLineName;
    private String[]          routeTrainId;
    private String[]          routeTrainName;
    private String[]          routeTrainKindId;
    private String[]          routeTrainKindName;
    private int[]             routeRed;
    private int[]             routeGreen;
    private int[]             routeBlue;

    private int               imageSize;
    private byte[]            image;

    /**
     * データを初期化します。
     */
    public NavitimeImageRaster()
    {
        log = new LogLib();

        mapColor = "";
        mapType = "JPG";
        mapCenterX = 0;
        mapCenterY = 0;
        mapScale = 0;
        mapZoom = 0;
        mapWidth = 0;
        mapHeight = 0;
        mapAngle = 0;
        maxBytes = 0;
        mapMoveH = 0;
        mapMoveV = 0;
        mapX = 0;
        mapY = 0;
        drawScale = 0;
        drawDirection = 0;
        drawTarget = 0;
        mapMobile = 0;
        mapMobileX = 0;
        mapMobileY = 0;
        palette = "";
    }

    public byte[] getImage()
    {
        return image;
    }

    public int getImageSize()
    {
        return imageSize;
    }

    public void setDrawDirection(int drawDirection)
    {
        this.drawDirection = drawDirection;
    }

    public void setDrawScale(int drawScale)
    {
        this.drawScale = drawScale;
    }

    public void setDrawTarget(int drawTarget)
    {
        this.drawTarget = drawTarget;
    }

    public void setIconCount(int iconCount)
    {
        this.iconCount = iconCount;
        this.iconDisp = new int[iconCount];
        this.iconDispPos = new int[iconCount];
        this.iconId = new int[iconCount];
        this.iconLat = new int[iconCount];
        this.iconLon = new int[iconCount];
        this.iconName = new String[iconCount];
        this.iconNameDisp = new int[iconCount];
        this.iconURL = new String[iconCount];
    }

    public void setIconDisp(int idx, int iconDisp)
    {
        this.iconDisp[idx] = iconDisp;
    }

    public void setIconDispPos(int idx, int iconDispPos)
    {
        this.iconDispPos[idx] = iconDispPos;
    }

    public void setIconId(int idx, int iconId)
    {
        this.iconId[idx] = iconId;
    }

    public void setIconLat(int idx, int iconLat)
    {
        this.iconLat[idx] = iconLat;
    }

    public void setIconLon(int idx, int iconLon)
    {
        this.iconLon[idx] = iconLon;
    }

    public void setIconName(int idx, String iconName)
    {
        this.iconName[idx] = iconName;
    }

    public void setIconNameDisp(int idx, int iconNameDisp)
    {
        this.iconNameDisp[idx] = iconNameDisp;
    }

    public void setIconURL(int idx, String iconURL)
    {
        this.iconURL[idx] = iconURL;
    }

    public void setLog(LogLib log)
    {
        this.log = log;
    }

    public void setMapAngle(int mapAngle)
    {
        this.mapAngle = mapAngle;
    }

    public void setMapCenterX(int mapCenterX)
    {
        this.mapCenterX = mapCenterX;
    }

    public void setMapCenterY(int mapCenterY)
    {
        this.mapCenterY = mapCenterY;
    }

    public void setMapColor(String mapColor)
    {
        this.mapColor = mapColor;
    }

    public void setMapHeight(int mapHeight)
    {
        this.mapHeight = mapHeight;
    }

    public void setMapMobile(int mapMobile)
    {
        this.mapMobile = mapMobile;
    }

    public void setMapMobileX(int mapMobileX)
    {
        this.mapMobileX = mapMobileX;
    }

    public void setMapMobileY(int mapMobileY)
    {
        this.mapMobileY = mapMobileY;
    }

    public void setMapMoveH(int mapMoveH)
    {
        this.mapMoveH = mapMoveH;
    }

    public void setMapMoveV(int mapMoveV)
    {
        this.mapMoveV = mapMoveV;
    }

    public void setMapScale(int mapScale)
    {
        this.mapScale = mapScale;
    }

    public void setMapType(String mapType)
    {
        this.mapType = mapType;
    }

    public void setMapWidth(int mapWidth)
    {
        this.mapWidth = mapWidth;
    }

    public void setMapX(int mapX)
    {
        this.mapX = mapX;
    }

    public void setMapY(int mapY)
    {
        this.mapY = mapY;
    }

    public void setMapZoom(int mapZoom)
    {
        this.mapZoom = mapZoom;
    }

    public void setMaxBytes(int maxBytes)
    {
        this.maxBytes = maxBytes;
    }

    public void setPalette(String palette)
    {
        this.palette = palette;
    }

    public void setRouteBlue(int[] routeBlue)
    {
        this.routeBlue = routeBlue;
    }

    public void setRouteCount(int routeCount)
    {
        this.routeCount = routeCount;
    }

    public void setRouteEndIconIdx(int[] routeEndIconIdx)
    {
        this.routeEndIconIdx = routeEndIconIdx;
    }

    public void setRouteEndNode(String[] routeEndNode)
    {
        this.routeEndNode = routeEndNode;
    }

    public void setRouteEndTime(String[] routeEndTime)
    {
        this.routeEndTime = routeEndTime;
    }

    public void setRouteGreen(int[] routeGreen)
    {
        this.routeGreen = routeGreen;
    }

    public void setRouteLineId(String[] routeLineId)
    {
        this.routeLineId = routeLineId;
    }

    public void setRouteLineName(String[] routeLineName)
    {
        this.routeLineName = routeLineName;
    }

    public void setRouteRed(int[] routeRed)
    {
        this.routeRed = routeRed;
    }

    public void setRouteStartIconIdx(int[] routeStartIconIdx)
    {
        this.routeStartIconIdx = routeStartIconIdx;
    }

    public void setRouteStartNode(String[] routeStartNode)
    {
        this.routeStartNode = routeStartNode;
    }

    public void setRouteStartTime(String[] routeStartTime)
    {
        this.routeStartTime = routeStartTime;
    }

    public void setRouteTrainId(String[] routeTrainId)
    {
        this.routeTrainId = routeTrainId;
    }

    public void setRouteTrainKindId(String[] routeTrainKindId)
    {
        this.routeTrainKindId = routeTrainKindId;
    }

    public void setRouteTrainKindName(String[] routeTrainKindName)
    {
        this.routeTrainKindName = routeTrainKindName;
    }

    public void setRouteTrainName(String[] routeTrainName)
    {
        this.routeTrainName = routeTrainName;
    }

    /**
     * 地図画像データ取得処理
     * 
     * @param request HTTPリクエスト
     * @param response HTTPレスポンス
     * @param carrierFlag キャリアフラグ
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getImageData(HttpServletRequest request, HttpServletResponse response, int carrierFlag)
    {
        int i;
        int count;
        int readCount;
        byte[] readBuff;
        byte[] readData;
        String param;
        String headerName;
        String headerValue;
        URL url;
        HttpURLConnection urlConn;
        Enumeration headerNamesEnum;
        Enumeration headerValueEnum;

        param = "";
        param = param + "MapType=" + this.mapType;
        param = param + "&MapCenterX=" + this.mapCenterX;
        param = param + "&MapCenterY=" + this.mapCenterY;
        param = param + "&MapScale=" + this.mapScale;
        param = param + "&MapZoom=" + this.mapZoom;
        param = param + "&MapWidth=" + this.mapWidth;
        param = param + "&MapHeight=" + this.mapHeight;
        param = param + "&DrawScale=" + this.drawScale;
        param = param + "&DrawDirection=" + this.drawDirection;
        param = param + "&DrawTarget=" + this.drawTarget;
        param = param + "&MapMobile=" + this.mapMobile;

        for( i = 0 ; i < this.iconCount ; i++ )
        {
            param = param + "&Icon" + i + "=" +
                    this.iconId[i] + "," +
                    this.iconLon[i] + "," +
                    this.iconLat[i] + "," +
                    this.iconName[i] + "," +
                    this.iconURL[i] + "," +
                    this.iconDisp[i] + "," +
                    this.iconNameDisp[i] + "," +
                    this.iconDispPos[i];
        }
        for( i = 0 ; i < this.routeCount ; i++ )
        {
            param = param + "&Route" + i + "=" +
                    this.routeStartIconIdx[i] + "," +
                    this.routeStartNode[i] + "," +
                    this.routeStartTime[i] + "," +
                    this.routeEndIconIdx[i] + "," +
                    this.routeEndNode[i] + "," +
                    this.routeEndTime[i] + "," +
                    this.routeLineId[i] + "," +
                    this.routeLineName[i] + "," +
                    this.routeTrainId[i] + "," +
                    this.routeTrainName[i] + "," +
                    this.routeTrainKindId[i] + "," +
                    this.routeTrainKindName[i] + "," +
                    this.routeRed[i] + "," +
                    this.routeGreen[i] + "," +
                    this.routeBlue[i];
        }

        readCount = 0;
        readData = new byte[IMAGESIZE_MAX];
        readBuff = new byte[1024];

        try
        {
            // NAVITIMEサーバに接続する
            url = new URL( "http://services.navitime.jp/pages/10067001/ImageRasterNV.jsp?" + param );

            System.out.println( "param= " + param );

            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setRequestMethod( "GET" );

            // キャリア別にヘッダを追加する。
            switch( carrierFlag )
            {
                case DataMasterUseragent.CARRIER_DOCOMO:
                    urlConn.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );
                    break;

                case DataMasterUseragent.CARRIER_AU:
                    headerNamesEnum = request.getHeaderNames();

                    urlConn.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );

                    while( headerNamesEnum.hasMoreElements() != false )
                    {
                        headerName = (String)headerNamesEnum.nextElement();
                        headerValueEnum = request.getHeaders( headerName );
                        while( headerValueEnum.hasMoreElements() != false )
                        {
                            headerValue = (String)headerValueEnum.nextElement();

                            if ( headerName.compareTo( "x-up-subno" ) != 0 )
                            {
                                if ( headerName.indexOf( "x-up-" ) >= 0 )
                                {
                                    System.out.println( "urlConn.setRequestProperty= " + headerName + ";" + headerValue );

                                    urlConn.setRequestProperty( headerName, headerValue );
                                }
                            }
                        }
                    }
                    break;

                case DataMasterUseragent.CARRIER_SOFTBANK:
                    headerNamesEnum = request.getHeaderNames();

                    urlConn.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );

                    while( headerNamesEnum.hasMoreElements() != false )
                    {
                        headerName = (String)headerNamesEnum.nextElement();
                        headerValueEnum = request.getHeaders( headerName );
                        while( headerValueEnum.hasMoreElements() != false )
                        {
                            headerValue = (String)headerValueEnum.nextElement();

                            if ( headerName.compareTo( "x-jphone-uid" ) != 0 )
                            {
                                if ( headerName.indexOf( "x-jphone-" ) >= 0 )
                                {
                                    urlConn.setRequestProperty( headerName, headerValue );
                                }
                            }
                        }
                    }
                    break;
            }

            urlConn.connect();

            // 戻ってきたイメージをByte配列に格納
            while( true )
            {
                count = urlConn.getInputStream().read( readBuff );
                if ( count == -1 )
                {
                    break;
                }
                for( i = 0 ; i < count ; i++ )
                {
                    readData[readCount++] = readBuff[i];
                }
            }
        }
        catch ( Exception e )
        {
            log.info( "[NavitimeImageRaster.getImage] Exception=" + e.toString() );
        }

        if ( readCount > 0 )
        {
            // 画像の出力
            this.image = readData;
            this.imageSize = readCount;

            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 地図画像データ取得処理(ゼンリン用)
     * 
     * @param request HTTPリクエスト
     * @param response HTTPレスポンス
     * @param carrierFlag キャリアフラグ
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getImageDataEx(HttpServletRequest request, HttpServletResponse response, int carrierFlag)
    {
        int i;
        int count;
        int readCount;
        byte[] readBuff;
        byte[] readData;
        String param;
        String headerName;
        String headerValue;
        URL url;
        HttpURLConnection urlConn;
        Enumeration headerNamesEnum;
        Enumeration headerValueEnum;

        param = "";
        param = param + "MapType=" + this.mapType;
        param = param + "&MapCenterX=" + this.mapCenterX;
        param = param + "&MapCenterY=" + this.mapCenterY;
        param = param + "&MapScale=" + this.mapScale;
        param = param + "&MapZoom=" + this.mapZoom;
        param = param + "&MapWidth=" + this.mapWidth;
        param = param + "&MapHeight=" + this.mapHeight;
        param = param + "&DrawScale=" + this.drawScale;
        param = param + "&DrawDirection=" + this.drawDirection;
        param = param + "&DrawTarget=" + this.drawTarget;
        param = param + "&MapMobile=" + this.mapMobile;

        for( i = 0 ; i < this.iconCount ; i++ )
        {
            param = param + "&Icon" + i + "=" +
                    this.iconId[i] + "," +
                    this.iconLon[i] + "," +
                    this.iconLat[i] + "," +
                    this.iconName[i] + "," +
                    this.iconURL[i] + "," +
                    this.iconDisp[i] + "," +
                    this.iconNameDisp[i] + "," +
                    this.iconDispPos[i];
        }
        for( i = 0 ; i < this.routeCount ; i++ )
        {
            param = param + "&Route" + i + "=" +
                    this.routeStartIconIdx[i] + "," +
                    this.routeStartNode[i] + "," +
                    this.routeStartTime[i] + "," +
                    this.routeEndIconIdx[i] + "," +
                    this.routeEndNode[i] + "," +
                    this.routeEndTime[i] + "," +
                    this.routeLineId[i] + "," +
                    this.routeLineName[i] + "," +
                    this.routeTrainId[i] + "," +
                    this.routeTrainName[i] + "," +
                    this.routeTrainKindId[i] + "," +
                    this.routeTrainKindName[i] + "," +
                    this.routeRed[i] + "," +
                    this.routeGreen[i] + "," +
                    this.routeBlue[i];
        }

        readCount = 0;
        readData = new byte[IMAGESIZE_MAX];
        readBuff = new byte[1024];

        try
        {
            // NAVITIMEサーバに接続する
            url = new URL( "http://services.navitime.biz/pages/10067001/ImageRasterNV.jsp?" + param );
            System.out.println( "Acccess to services.navitime.biz" );

            System.out.println( "param= " + param );

            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setRequestMethod( "GET" );

            // キャリア別にヘッダを追加する。
            switch( carrierFlag )
            {
                case DataMasterUseragent.CARRIER_DOCOMO:
                    urlConn.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );
                    break;

                case DataMasterUseragent.CARRIER_AU:
                    headerNamesEnum = request.getHeaderNames();

                    urlConn.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );

                    while( headerNamesEnum.hasMoreElements() != false )
                    {
                        headerName = (String)headerNamesEnum.nextElement();
                        headerValueEnum = request.getHeaders( headerName );
                        while( headerValueEnum.hasMoreElements() != false )
                        {
                            headerValue = (String)headerValueEnum.nextElement();

                            if ( headerName.compareTo( "x-up-subno" ) != 0 )
                            {
                                if ( headerName.indexOf( "x-up-" ) >= 0 )
                                {
                                    System.out.println( "urlConn.setRequestProperty= " + headerName + ";" + headerValue );

                                    urlConn.setRequestProperty( headerName, headerValue );
                                }
                            }
                        }
                    }
                    break;

                case DataMasterUseragent.CARRIER_SOFTBANK:
                    headerNamesEnum = request.getHeaderNames();

                    urlConn.setRequestProperty( "User-Agent", request.getHeader( "User-Agent" ) );

                    while( headerNamesEnum.hasMoreElements() != false )
                    {
                        headerName = (String)headerNamesEnum.nextElement();
                        headerValueEnum = request.getHeaders( headerName );
                        while( headerValueEnum.hasMoreElements() != false )
                        {
                            headerValue = (String)headerValueEnum.nextElement();

                            if ( headerName.compareTo( "x-jphone-uid" ) != 0 )
                            {
                                if ( headerName.indexOf( "x-jphone-" ) >= 0 )
                                {
                                    urlConn.setRequestProperty( headerName, headerValue );
                                }
                            }
                        }
                    }
                    break;
            }

            urlConn.connect();

            // 戻ってきたイメージをByte配列に格納
            while( true )
            {
                count = urlConn.getInputStream().read( readBuff );
                if ( count == -1 )
                {
                    break;
                }
                for( i = 0 ; i < count ; i++ )
                {
                    readData[readCount++] = readBuff[i];
                }
            }
        }
        catch ( Exception e )
        {
            log.info( "[NavitimeImageRaster.getImageDataEx] Exception=" + e.toString() );
        }

        if ( readCount > 0 )
        {
            // 画像の出力
            this.image = readData;
            this.imageSize = readCount;

            return(true);
        }
        else
        {
            return(false);
        }
    }
}
