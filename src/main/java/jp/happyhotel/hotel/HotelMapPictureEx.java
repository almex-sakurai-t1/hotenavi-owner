/*
 * @(#)HotelMapPictureEx.java 1.00 2008/07/01 Copyright (C) ALMEX Inc. 2008 �z�e���n�}�摜�o�̓T�[�u���b�g(YahooMap�p)
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
 * �z�e���n�}�摜�o�̓T�[�u���b�g<br>
 * �p�����[�^�Ƀz�e��ID(id)���Z�b�g���邱�ƁB
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/15
 */
public class HotelMapPictureEx extends HttpServlet implements javax.servlet.Servlet
{
    /**
	 *
	 */
    private static final long serialVersionUID = 1725990473598991020L;

    /**
     * �z�e���n�}�摜�̕\��<br>
     * �p�����[�^�Ƀz�e��ID(id)���Z�b�g���邱�ƁB<br>
     * �܂��A�k�ڃT�C�Y(scale_size)���Z�b�g����Ək�ڂ̕ύX���\(�����l7)
     * 
     * @param id �z�e��ID
     * @param scale_size �k�ڃT�C�Y
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean ret;
        DataHotelBasic dhb;
        UserTermInfo uti;
        ProAtlasDrawMap padm;
        ServletOutputStream stream;

        dhb = new DataHotelBasic();
        uti = new UserTermInfo();
        padm = new ProAtlasDrawMap();
        stream = response.getOutputStream();

        String hotelId = request.getParameter( "id" );
        String scaleSize = request.getParameter( "scale_size" );
        if ( hotelId != null )
        {
            if ( CheckString.numCheck( hotelId ) != false )
            {
                dhb.getData( Integer.parseInt( hotelId ) );

                // ProAtlas�ɑ��M����p�����^�̕ҏW
                // �L�����A�̃`�F�b�N���s��
                ret = uti.getTermInfo( request );
                if ( ret != false )
                {
                    switch( uti.getTerm().getCarrierFlag() )
                    {
                        case DataMasterUseragent.CARRIER_DOCOMO:
                            padm.setMapType( "GIF" );
                            break;

                        case DataMasterUseragent.CARRIER_AU:
                            padm.setMapType( "PNG" );
                            break;

                        case DataMasterUseragent.CARRIER_SOFTBANK:
                            padm.setMapType( "PNG" );
                            break;
                    }
                }
                else
                {
                    padm.setMapType( "JPG" );
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

                padm.setMapCenterX( dhb.getHotelLat() );
                padm.setMapCenterY( dhb.getHotelLon() );

                switch( Integer.parseInt( scaleSize ) )
                {
                    case 1:
                        padm.setMapScale( 5000000 );
                        break;
                    case 2:
                        padm.setMapScale( 3000000 );
                        break;
                    case 3:
                        padm.setMapScale( 1000000 );
                        break;
                    case 4:
                        padm.setMapScale( 500000 );
                        break;
                    case 5:
                        padm.setMapScale( 250000 );
                        break;
                    case 6:
                        padm.setMapScale( 70000 );
                        break;
                    case 7:
                        padm.setMapScale( 25000 );
                        break;
                    case 8:
                        padm.setMapScale( 10000 );
                        break;
                    case 9:
                        padm.setMapScale( 5000 );
                        break;
                }
                padm.setMapWidth( 640 );
                padm.setMapHeight( 480 );

                padm.getImageData( request, response, uti.getTerm().getCarrierFlag() );
                if ( padm.getImageSize() > 0 )
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
                    // �摜�̏o��
                    stream.write( padm.getImage(), 0, padm.getImageSize() );
                }
            }
        }
    }
}
