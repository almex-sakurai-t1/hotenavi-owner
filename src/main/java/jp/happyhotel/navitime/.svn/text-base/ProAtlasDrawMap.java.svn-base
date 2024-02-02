/*
 * @(#)ProAtlasDrawMap.java 1.00 2008/06/18 Copyright (C) ALMEX Inc. 2008 地図画像データ取得クラス
 */

package jp.happyhotel.navitime;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.servlet.http.*;
import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * 地図画像データ取得クラス(ProAtlas版) 地図画像データを取得する機能を提供する
 * 
 * @author S.Shiiya
 * @version 1.00 2008/06/18
 */
public class ProAtlasDrawMap implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -1970121841179145692L;

    private static final int  IMAGESIZE_MAX    = 100000;

    private String            mapType;
    private String            mapCenterX;
    private String            mapCenterY;
    private int               mapScale;
    private int               mapWidth;
    private int               mapHeight;

    private int               imageSize;
    private byte[]            image;

    /**
     * データを初期化します。
     */
    public ProAtlasDrawMap()
    {
    }

    public String getMapType()
    {
        return mapType;
    }

    public void setMapType(String mapType)
    {
        this.mapType = mapType;
    }

    public String getMapCenterX()
    {
        return mapCenterX;
    }

    public String getMapCenterY()
    {
        return mapCenterY;
    }

    public int getMapHeight()
    {
        return mapHeight;
    }

    public int getMapScale()
    {
        return mapScale;
    }

    public int getMapWidth()
    {
        return mapWidth;
    }

    public void setMapCenterX(String mapCenterX)
    {
        this.mapCenterX = mapCenterX;
    }

    public void setMapCenterY(String mapCenterY)
    {
        this.mapCenterY = mapCenterY;
    }

    public void setMapHeight(int mapHeight)
    {
        this.mapHeight = mapHeight;
    }

    public void setMapScale(int mapScale)
    {
        this.mapScale = mapScale;
    }

    public void setMapWidth(int mapWidth)
    {
        this.mapWidth = mapWidth;
    }

    public byte[] getImage()
    {
        return image;
    }

    public int getImageSize()
    {
        return imageSize;
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
        param = param + "appid=" + "o9_096x27";
        param = param + "&c=WGS84," + this.mapCenterX;
        param = param + "," + this.mapCenterY;
        param = param + "&scale=" + this.mapScale;
        param = param + "&pos=Ipin:PWGS84," + this.mapCenterX;
        param = param + "," + this.mapCenterY;
        param = param + "&imgtype=" + this.mapType;
        param = param + "&dataname=" + "ProAtlasClearCity";

        if ( carrierFlag != DataMasterUseragent.CARRIER_ETC )
        {
            param = param + "&wm=" + "true";
        }
        else
        {
            param = param + "&width=" + this.mapWidth;
            param = param + "&height=" + this.mapHeight;
        }

        readCount = 0;
        readData = new byte[IMAGESIZE_MAX];
        readBuff = new byte[1024];

        try
        {
            // ProAtlasサーバに接続する
            url = new URL( "http://api.pmx.proatlas.net/PESWebService/v1/drawMap?" + param );

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
            Logging.info( "[ProAtlasDrawMap.getImageData] Exception=" + e.toString() );
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
