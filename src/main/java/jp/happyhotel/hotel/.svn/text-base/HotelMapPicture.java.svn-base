/*
 * @(#)HotelMapPicture.java 1.00 2007/08/15 Copyright (C) ALMEX Inc. 2007 ホテル地図画像出力サーブレット
 */
package jp.happyhotel.hotel;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.happyhotel.common.*;
import jp.happyhotel.data.*;
import jp.happyhotel.navitime.*;
import jp.happyhotel.user.*;

/**
 * ホテル地図画像出力サーブレット<br>
 * パラメータにホテルID(id)をセットすること。
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/15
 */
public class HotelMapPicture extends HttpServlet implements javax.servlet.Servlet
{
    /**
	 *
	 */
    private static final long serialVersionUID = -2166674758389579073L;

    /**
     * ホテル地図画像の表示<br>
     * パラメータにホテルID(id)をセットすること。<br>
     * また、縮尺サイズ(scale_size)をセットすると縮尺の変更が可能(初期値7)
     * 
     * @param id ホテルID
     * @param scale_size 縮尺サイズ
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean ret;
        DataHotelBasic dhb;
        UserTermInfo uti;
        NavitimeImageRaster nvir;
        ServletOutputStream stream;

        dhb = new DataHotelBasic();
        uti = new UserTermInfo();
        nvir = new NavitimeImageRaster();
        stream = response.getOutputStream();

        String hotelId = request.getParameter( "id" );
        String scaleSize = request.getParameter( "scale_size" );
        if ( hotelId != null )
        {
            if ( CheckString.numCheck( hotelId ) != false )
            {
                dhb.getData( Integer.parseInt( hotelId ) );

                // NAVITIMEに送信するパラメタの編集
                // キャリアのチェックを行う
                ret = uti.getTermInfo( request );
                if ( ret != false )
                {
                    switch( uti.getTerm().getCarrierFlag() )
                    {
                        case DataMasterUseragent.CARRIER_DOCOMO:
                            nvir.setMapType( "GIF" );
                            break;

                        case DataMasterUseragent.CARRIER_AU:
                            nvir.setMapType( "PNG" );
                            break;

                        case DataMasterUseragent.CARRIER_SOFTBANK:
                            nvir.setMapType( "PNG" );
                            break;
                    }
                }
                else
                {
                    nvir.setMapType( "JPG" );
                }
                if ( scaleSize != null )
                {
                    if ( scaleSize.compareTo( "" ) == 0 )
                        scaleSize = "7";
                    if ( CheckString.numCheck( scaleSize ) == false )
                        scaleSize = "7";
                }
                else
                {
                    scaleSize = "7";
                }
                ConvertGeodesic cg = new ConvertGeodesic();
                cg.Wgs2Tokyo( Double.parseDouble( dhb.getHotelLat() ), Double.parseDouble( dhb.getHotelLon() ) );
                cg.Tokyo2TokyoNum( cg.getLatTOKYO(), cg.getLonTOKYO() );

                nvir.setMapCenterX( cg.getLonTOKYONum() );
                nvir.setMapCenterY( cg.getLatTOKYONum() );
                switch( Integer.parseInt( scaleSize ) )
                {
                    case 1:
                        nvir.setMapScale( 1 );
                        nvir.setMapZoom( 13 );
                        break;
                    case 2:
                        nvir.setMapScale( 1 );
                        nvir.setMapZoom( 23 );
                        break;
                    case 3:
                        nvir.setMapScale( 2 );
                        nvir.setMapZoom( 4 );
                        break;
                    case 4:
                        nvir.setMapScale( 2 );
                        nvir.setMapZoom( 6 );
                        break;
                    case 5:
                        nvir.setMapScale( 3 );
                        nvir.setMapZoom( 3 );
                        break;
                    case 6:
                        nvir.setMapScale( 3 );
                        nvir.setMapZoom( 5 );
                        break;
                    case 7:
                        nvir.setMapScale( 4 );
                        nvir.setMapZoom( 3 );
                        break;
                    case 8:
                        nvir.setMapScale( 4 );
                        nvir.setMapZoom( 5 );
                        break;
                    case 9:
                        nvir.setMapScale( 4 );
                        nvir.setMapZoom( 7 );
                        break;
                }
                nvir.setMapWidth( 640 );
                nvir.setMapHeight( 480 );
                nvir.setDrawScale( 0 );
                nvir.setDrawDirection( 0 );
                nvir.setDrawTarget( 0 );
                if ( ret != false )
                    nvir.setMapMobile( 1 );
                else
                    nvir.setMapMobile( 0 );

                nvir.setIconCount( 1 );
                nvir.setIconId( 0, 10004 );
                nvir.setIconLat( 0, cg.getLatTOKYONum() );
                nvir.setIconLon( 0, cg.getLonTOKYONum() );
                nvir.setIconName( 0, "" );
                nvir.setIconURL( 0, "" );
                nvir.setIconDisp( 0, 1 );
                nvir.setIconNameDisp( 0, 0 );
                nvir.setIconDispPos( 0, 4 );

                nvir.getImageDataEx( request, response, uti.getTerm().getCarrierFlag() );
                if ( nvir.getImageSize() > 0 )
                {
                    switch( uti.getTerm().getCarrierFlag() )
                    {
                        case DataMasterUseragent.CARRIER_DOCOMO:
                            response.setContentType( "image/gif" );
                            break;

                        case DataMasterUseragent.CARRIER_AU:
                            response.setContentType( "image/png" );
                            break;

                        case DataMasterUseragent.CARRIER_SOFTBANK:
                            response.setContentType( "image/png" );
                            break;

                        default:
                            response.setContentType( "image/jpeg" );
                            break;
                    }
                    // 画像の出力
                    stream.write( nvir.getImage(), 0, nvir.getImageSize() );
                }
            }
        }
    }
}
