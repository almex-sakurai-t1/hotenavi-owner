/*
 * @(#)DispImage.java 1.00 2009/06/25 Copyright (C) ALMEX Inc. 2008 ホテル地図画像出力サーブレット
 */
package jp.happyhotel.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.user.UserTermInfo;

/**
 * ルート検索画像表示クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/06/30
 */
public class DispRouteImage extends HttpServlet implements javax.servlet.Servlet
{

    /**
     *
     */
    private static final long serialVersionUID = 7196606982794598620L;

    /**
     * 
     * @param id ホテルID
     * @param scale_size 縮尺サイズ
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean ret;
        int scale;
        int dispScale;
        int carrierFlag;
        String paramUrl;
        InputStream input;
        OutputStream output;
        HttpConnection con;
        CreateUrl cu;
        String strPoint;
        String strKind;
        String strPos;
        String paramDx;
        String paramDy;
        String paramScale;
        String paramDscale;
        String coordinate;
        ReadXml readXml;
        UserTermInfo uti;
        String filePath = "";
        String contentType = "";

        ret = false;
        paramUrl = request.getParameter( "url" );
        input = null;
        output = null;
        con = new HttpConnection();
        cu = new CreateUrl();

        scale = 0;
        strPoint = request.getParameter( "point" );
        strKind = request.getParameter( "kind" );
        strPos = request.getParameter( "pos" );
        paramDx = request.getParameter( "dx" );
        paramDy = request.getParameter( "dy" );
        paramScale = request.getParameter( "scale" );
        paramDscale = request.getParameter( "dscale" );

        uti = new UserTermInfo();
        ret = uti.getTermInfo( request );
        if ( ret != false )
        {
            carrierFlag = uti.getTerm().getCarrierFlag();
        }
        else
        {
            carrierFlag = 4;
        }

        if ( strPoint == null )
        {
            strPoint = "";
        }
        if ( (strKind == null) || (strKind.compareTo( "" ) == 0) || (CheckString.numCheck( strKind ) == false) )
        {
            strKind = "0";
        }
        if ( strPos == null )
        {
            strPos = "";
        }
        if ( (paramDx == null) || (paramDx.compareTo( "" ) == 0) || (CheckString.numCheck( paramDx ) == false) )
        {
            paramDx = "0";
        }
        if ( (paramDy == null) || (paramDy.compareTo( "" ) == 0) || (CheckString.numCheck( paramDy ) == false) )
        {
            paramDy = "0";
        }
        if ( (paramScale == null) || (paramScale.compareTo( "" ) == 0) || (CheckString.numCheck( paramScale ) == false) )
        {
            paramScale = "0";
        }
        if ( (paramDscale == null) || (paramDscale.compareTo( "" ) == 0) || (CheckString.numCheck( paramDscale ) == false) )
        {
            paramDscale = "0";
        }

        if ( strPoint.compareTo( "" ) != 0 )
        {
            cu.setPoint( strPoint );
        }
        cu.setOutdatum( "WGS84" );
        cu.setOutput( "xml" );
        // cu.setCostPriority( 2 );
        if ( Integer.parseInt( strKind ) == 0 || Integer.parseInt( strKind ) == 1 )
        {
            cu.setTransport( 2 );
        }
        else
        {
            cu.setUseAroundTollRoad( 1 );
        }
        // cu.setSpeedWalk( 4 );
        // cu.setSpeedHighWay( 80 );
        // cu.setSpeedCityHighWay( 70 );
        // cu.setSpeedNationRoute( 100 );
        // cu.setSpeedMainRoad( 90 );
        // cu.setSpeedPrefRoad( 60 );
        // cu.setSpeedOtherRoad( 40 );
        // cu.setSpeedTollRoad( 50 );
        cu.setTollPriority( 2 );
        cu.setDetailInfo( 1 );
        paramUrl = cu.getRouting();

        cu = null;
        cu = new CreateUrl();
        readXml = new ReadXml( paramUrl );
        if ( readXml.getElementValue() != false )
        {
            if ( readXml.getResultCode() == 0 )
            {

                // 基準となる縮尺
                scale = this.getScale( Integer.parseInt( paramScale ) );
                // 移動または、拡大縮小後の縮尺
                dispScale = this.getScale( Integer.parseInt( paramDscale ) );

                // cu.setWidth( 500 );
                // cu.setHeight( 500 );
                // cu.setScale( 25000 );
                cu.setWm( true );
                cu.setHm( true );

                // 移動量が0の場合、scaleパラメータに表示縮尺(dispScale)をセットする。それ以外だと、dscaleパラメータに表示縮尺をセットする
                if ( (Integer.parseInt( paramDx ) == 0) && (Integer.parseInt( paramDy ) == 0) )
                {
                    cu.setScale( dispScale );
                    System.out.println( "[DispRouteImage]dispScale:" + dispScale );
                }
                else
                {
                    // scaleパラメータに基準スケール(scale)をセット、それぞれの移動後のパラメータもセット
                    // cu.setScale( scale );
                    cu.setScale( dispScale );
                    cu.setDx( Integer.parseInt( paramDx ) * 100 );
                    cu.setDy( Integer.parseInt( paramDy ) * 100 );
                    cu.setDscale( dispScale );
                    System.out.println( "[DispRouteImage]dx:" + paramDx + ", dy:" + paramDy );
                }

                cu.setDataName( "ProAtlasClearCity" );
                if ( strPos.compareTo( "" ) != 0 )
                {
                    cu.setPos( strPos );
                }
                if ( readXml.getDistanceList() > 5000 )
                {
                    if ( readXml.getNodeList().compareTo( "" ) != 0 )
                    {
                        cu.setLine( "CFF0000:W3:PWGS84," + readXml.getNodeList() );
                        // cu.setPointList( "WGS84," + readXml.getNodeList() );
                    }
                    else
                    {
                        cu.setLine( "CFF0000:W3:PWGS84," + readXml.getRouteList() );
                        // cu.setPointList( "WGS84," + readXml.getRouteList() );
                    }
                }
                else
                {
                    cu.setLine( "CFF0000:W3:PWGS84," + readXml.getRouteList() );
                    // cu.setPointList( "WGS84," + readXml.getRouteList() );
                }

                // キャリアによって出す画像タイプを変更
                if ( (carrierFlag == DataMasterUseragent.CARRIER_AU) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
                {
                    cu.setImgType( "PNG" );
                    Logging.info( "[DispRouteImage]:PNGセット" );
                }
                else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
                {
                    cu.setImgType( "GIF" );
                    Logging.info( "[DispRouteImage]:GIFセット" );
                }

                paramUrl = cu.getDrawMap();

                // URLを指定して、HTTP通信させる
                ret = con.urlConnection( request, response, paramUrl );
                // 通信結果がTrueだったら
                if ( ret != false )
                {

                    // 中心地を取得する
                    coordinate = con.getCoordinate();
                    // 基準のスケール
                    paramScale = con.getSclale();
                    // 中心地をセット
                    cu.setC( coordinate );

                    if ( (Integer.parseInt( paramDx ) == 0) && (Integer.parseInt( paramDy ) == 0) )
                    {
                        // とってきた縮尺にDscaleを足す
                        cu.setScale( this.getScale( Integer.parseInt( paramDscale ) ) );
                    }
                    else
                    {
                        // scaleパラメータに基準スケール(scale)をセット、それぞれの移動後のパラメータもセット
                        // cu.setScale( scale );
                        cu.setScale( this.getScale( Integer.parseInt( paramDscale ) ) );
                        cu.setDscale( this.getScale( Integer.parseInt( paramDscale ) ) );
                    }

                    paramUrl = cu.getDrawMap();
                    // URLを指定して、HTTP通信させる
                    ret = con.urlConnection( request, response, paramUrl );
                    if ( ret != false )
                    {
                        output = response.getOutputStream();
                        // キャッシュさせないように設定する
                        response.setHeader( "pragma", "no-cache" );
                        response.setHeader( "Cache-Control", "no-cache" );
                        // 表示する画像のコンテントタイプをセットする
                        response.setContentType( con.getContentType() );
                        // 画像の出力
                        output.write( con.getImage(), 0, con.getImageSize() );
                    }

                    // }
                }
                else
                {
                    // ルート検索失敗の画像などを表示する
                    int imageData;

                    if ( (carrierFlag == DataMasterUseragent.CARRIER_AU) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
                    {
                        filePath = "/happyhotel/common/image/noimage.png";
                        contentType = "image/png";
                    }
                    else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
                    {
                        filePath = "/happyhotel/common/image/noimage.gif";
                        contentType = "image/gif";
                    }
                    else
                    {
                        filePath = "/happyhotel/common/images/noimage.jpg";
                        contentType = "image/jpg";
                    }

                    File file = new File( filePath );
                    output = response.getOutputStream();

                    if ( file.exists() != false )
                    {
                        // キャッシュさせないように設定する
                        response.setHeader( "pragma", "no-cache" );
                        response.setHeader( "Cache-Control", "no-cache" );
                        response.setContentType( contentType );
                        input = new BufferedInputStream( new FileInputStream( file ) );
                        while( (imageData = input.read()) != -1 )
                        {
                            output.write( imageData );
                        }
                    }
                }
            }
            else
            {
                // ルート検索失敗の画像などを表示する
                int imageData;

                if ( (carrierFlag == DataMasterUseragent.CARRIER_AU) || (carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK) )
                {
                    filePath = "/happyhotel/common/image/noimage.png";
                    contentType = "image/png";
                }
                else if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
                {
                    filePath = "/happyhotel/common/image/noimage.gif";
                    contentType = "image/gif";
                }
                else
                {
                    filePath = "/happyhotel/common/images/noimage.jpg";
                    contentType = "image/jpg";
                }

                File file = new File( filePath );
                output = response.getOutputStream();

                if ( file.exists() != false )
                {
                    // キャッシュさせないように設定する
                    response.setHeader( "pragma", "no-cache" );
                    response.setHeader( "Cache-Control", "no-cache" );
                    response.setContentType( contentType );
                    input = new BufferedInputStream( new FileInputStream( file ) );
                    while( (imageData = input.read()) != -1 )
                    {
                        output.write( imageData );
                    }
                }
            }
        }
        else
        {
            Logging.info( "readXml.getElementValue()" );
        }

    }

    /**
     * 縮尺を取得する
     * 
     * @param scale スケール。縮尺
     * @return 処理結果(5000～1000000までの縮尺)
     * 
     */
    private int getScale(int scale)
    {
        int dispScale;

        switch( scale )
        {
            case -4:
                dispScale = 10000;
                break;
            case -3:
                dispScale = 10000;
                break;
            case -2:
                dispScale = 10000;
                break;
            case -1:
                dispScale = 25000;
                break;
            case 0:
                dispScale = 70000;
                break;
            case 1:
                dispScale = 250000;
                break;
            case 2:
                dispScale = 500000;
                break;
            case 3:
                dispScale = 500000;
                break;
            case 4:
                dispScale = 500000;
                break;
            default:
                dispScale = 70000;
        }
        return(dispScale);
    }

    /**
     * 縮尺から今の段階を取得する
     * 
     * @param scale スケール。縮尺
     * @return 処理結果(5000～1000000までの縮尺)
     * 
     */
    private int getScaleTo(int scale)
    {
        int dispScale;

        switch( scale )
        {
            case 10000:
                dispScale = -2;
                break;
            case 25000:
                dispScale = -1;
                break;
            case 70000:
                dispScale = 0;
                break;
            case 250000:
                dispScale = 1;
                break;
            case 500000:
                dispScale = 2;
                break;
            default:
                dispScale = 0;
        }
        return(dispScale);
    }
}
