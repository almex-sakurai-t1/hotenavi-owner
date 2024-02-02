/*
 * @(#)HotelRoomPicture.java 1.00 2007/08/15 Copyright (C) ALMEX Inc. 2007 部屋画像出力サーブレット
 */
package jp.happyhotel.hotel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataHotelRoom;
import jp.happyhotel.data.DataHotelRoomMore;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.user.UserTermInfo;

/**
 * 部屋画像出力サーブレット<br>
 * パラメータにホテルID(id)と部屋番号(room_name)または管理番号(seq)をセットすること。
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/15
 */
public class HotelRoomPicture extends HttpServlet implements javax.servlet.Servlet
{
    /**
     *
     */
    private static final long serialVersionUID = -8364712364333014289L;

    /**
     * 部屋画像の表示<br>
     * パラメータにホテルID(id)と部屋番号(room_name)をセットすること。
     * 
     * @param id ホテルID
     * @param room_name 部屋番号
     * @param seq 管理番号
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int imageData;
        boolean ret;
        boolean moreFlag = false; // 部屋画像数拡大版判別用（有料限定）
        String imageJpeg;
        String imageGif;
        String imagePng;
        DataHotelRoom dhr;
        DataHotelRoomMore dhrm;
        UserTermInfo uti;
        FileInputStream propfile = null;
        Properties config = new Properties();
        ServletOutputStream stream;
        BufferedInputStream inputData;

        propfile = new FileInputStream( "/etc/happyhotel/hotelimage.conf" );
        config = new Properties();
        config.load( propfile );

        imageJpeg = config.getProperty( "hotel.image.jpeg" );
        imageGif = config.getProperty( "hotel.image.gif" );
        imagePng = config.getProperty( "hotel.image.png" );

        propfile.close();

        dhr = new DataHotelRoom();
        dhrm = new DataHotelRoomMore();
        uti = new UserTermInfo();
        stream = response.getOutputStream();

        String hotelId = request.getParameter( "id" );
        String roomName = request.getParameter( "room_name" );
        String seq = request.getParameter( "seq" );
        String strMoreFlag = request.getParameter( "more" );
        if ( strMoreFlag != null && strMoreFlag.compareTo( "" ) != 0 )
        {
            if ( strMoreFlag.compareTo( "true" ) == 0 )
            {
                moreFlag = true;
            }
        }

        if ( hotelId != null && (roomName != null || seq != null) )
        {
            if ( CheckString.numCheck( hotelId ) != false )
            {
                if ( roomName != null )
                {
                    if ( moreFlag == false )
                    {
                        dhr.getData( Integer.parseInt( hotelId ), roomName );
                    }
                    else
                    {
                        dhrm.getData( Integer.parseInt( hotelId ), roomName );
                    }
                }
                else if ( seq != null )
                {
                    if ( CheckString.numCheck( seq ) != false )
                    {
                        if ( moreFlag == false )
                        {
                            dhr.getData( Integer.parseInt( hotelId ), Integer.parseInt( seq ) );
                        }
                        else
                        {
                            dhrm.getData( Integer.parseInt( hotelId ), Integer.parseInt( seq ) );
                        }
                    }
                }

                String paramType = request.getParameter( "type" );
                if ( moreFlag == false )
                {
                    if ( paramType == null )
                    {
                        if ( UserAgent.getUserAgentType( request ) != UserAgent.USERAGENT_PC &&
                                UserAgent.getUserAgentType( request ) != UserAgent.USERAGENT_SMARTPHONE )
                        {
                            // キャリアのチェックを行う
                            ret = uti.getTermInfo( request );
                            if ( ret != false )
                            {
                                switch( uti.getTerm().getCarrierFlag() )
                                {
                                    case DataMasterUseragent.CARRIER_DOCOMO:
                                        if ( dhr.getRoomPictureGif() == null )
                                        {
                                            response.setContentType( "image/gif" );
                                            inputData = new BufferedInputStream( new FileInputStream( imageGif ) );

                                            while( (imageData = inputData.read()) != -1 )
                                            {
                                                stream.write( imageData );
                                            }
                                        }
                                        else
                                        {
                                            response.setContentType( "image/gif" );
                                            response.setContentLength( dhr.getRoomPictureGif().length );
                                            stream.write( dhr.getRoomPictureGif() );
                                        }
                                        break;

                                    case DataMasterUseragent.CARRIER_AU:
                                        if ( dhr.getRoomPicturePng() == null )
                                        {
                                            response.setContentType( "image/png" );
                                            inputData = new BufferedInputStream( new FileInputStream( imagePng ) );

                                            while( (imageData = inputData.read()) != -1 )
                                            {
                                                stream.write( imageData );
                                            }
                                        }
                                        else
                                        {
                                            response.setContentType( "image/png" );
                                            response.setContentLength( dhr.getRoomPicturePng().length );
                                            stream.write( dhr.getRoomPicturePng() );
                                        }
                                        break;

                                    case DataMasterUseragent.CARRIER_SOFTBANK:
                                        if ( dhr.getRoomPicturePng() == null )
                                        {
                                            response.setContentType( "image/png" );
                                            inputData = new BufferedInputStream( new FileInputStream( imagePng ) );

                                            while( (imageData = inputData.read()) != -1 )
                                            {
                                                stream.write( imageData );
                                            }
                                        }
                                        else
                                        {
                                            response.setContentType( "image/png" );
                                            response.setContentLength( dhr.getRoomPicturePng().length );
                                            stream.write( dhr.getRoomPicturePng() );
                                        }
                                        break;
                                }
                            }
                            else
                            {
                                response.setContentType( "image/jpeg" );
                                response.setContentLength( dhr.getRoomPicturePc().length );
                                stream.write( dhr.getRoomPicturePc() );
                            }
                        }
                        else
                        {
                            response.setContentType( "image/jpeg" );
                            response.setContentLength( dhr.getRoomPicturePc().length );
                            stream.write( dhr.getRoomPicturePc() );
                        }
                    }
                    else
                    {
                        if ( paramType.compareTo( "gif" ) == 0 )
                        {
                            if ( dhr.getRoomPictureGif() == null )
                            {
                                response.setContentType( "image/gif" );
                                inputData = new BufferedInputStream( new FileInputStream( imageGif ) );

                                while( (imageData = inputData.read()) != -1 )
                                {
                                    stream.write( imageData );
                                }
                            }
                            else
                            {
                                response.setContentType( "image/gif" );
                                response.setContentLength( dhr.getRoomPictureGif().length );
                                stream.write( dhr.getRoomPictureGif() );
                            }
                        }
                        else if ( paramType.compareTo( "png" ) == 0 )
                        {
                            if ( dhr.getRoomPicturePng() == null )
                            {
                                response.setContentType( "image/png" );
                                inputData = new BufferedInputStream( new FileInputStream( imagePng ) );

                                while( (imageData = inputData.read()) != -1 )
                                {
                                    stream.write( imageData );
                                }
                            }
                            else
                            {
                                response.setContentType( "image/png" );
                                response.setContentLength( dhr.getRoomPicturePng().length );
                                stream.write( dhr.getRoomPicturePng() );
                            }
                        }
                        else if ( paramType.compareTo( "jpg" ) == 0 )
                        {
                            if ( dhr.getRoomPicturePc() == null )
                            {
                                response.setContentType( "image/jpeg" );
                                inputData = new BufferedInputStream( new FileInputStream( imageJpeg ) );

                                while( (imageData = inputData.read()) != -1 )
                                {
                                    stream.write( imageData );
                                }
                            }
                            else
                            {
                                response.setContentType( "image/jpeg" );
                                response.setContentLength( dhr.getRoomPicturePc().length );
                                stream.write( dhr.getRoomPicturePc() );
                            }
                        }
                    }
                }
                // 部屋画像数拡張版
                else
                {
                    if ( paramType == null )
                    {
                        if ( UserAgent.getUserAgentType( request ) != UserAgent.USERAGENT_PC )
                        {
                            // キャリアのチェックを行う
                            ret = uti.getTermInfo( request );
                            if ( ret != false )
                            {
                                switch( uti.getTerm().getCarrierFlag() )
                                {
                                    case DataMasterUseragent.CARRIER_DOCOMO:
                                        if ( dhrm.getRoomPictureGif() == null )
                                        {
                                            response.setContentType( "image/gif" );
                                            inputData = new BufferedInputStream( new FileInputStream( imageGif ) );

                                            while( (imageData = inputData.read()) != -1 )
                                            {
                                                stream.write( imageData );
                                            }
                                        }
                                        else
                                        {
                                            response.setContentType( "image/gif" );
                                            response.setContentLength( dhrm.getRoomPictureGif().length );
                                            stream.write( dhrm.getRoomPictureGif() );
                                        }
                                        break;

                                    case DataMasterUseragent.CARRIER_AU:
                                        if ( dhrm.getRoomPicturePng() == null )
                                        {
                                            response.setContentType( "image/png" );
                                            inputData = new BufferedInputStream( new FileInputStream( imagePng ) );

                                            while( (imageData = inputData.read()) != -1 )
                                            {
                                                stream.write( imageData );
                                            }
                                        }
                                        else
                                        {
                                            response.setContentType( "image/png" );
                                            response.setContentLength( dhrm.getRoomPicturePng().length );
                                            stream.write( dhrm.getRoomPicturePng() );
                                        }
                                        break;

                                    case DataMasterUseragent.CARRIER_SOFTBANK:
                                        if ( dhrm.getRoomPicturePng() == null )
                                        {
                                            response.setContentType( "image/png" );
                                            inputData = new BufferedInputStream( new FileInputStream( imagePng ) );

                                            while( (imageData = inputData.read()) != -1 )
                                            {
                                                stream.write( imageData );
                                            }
                                        }
                                        else
                                        {
                                            response.setContentType( "image/png" );
                                            response.setContentLength( dhrm.getRoomPicturePng().length );
                                            stream.write( dhrm.getRoomPicturePng() );
                                        }
                                        break;
                                }
                            }
                            else
                            {
                                response.setContentType( "image/jpeg" );
                                response.setContentLength( dhrm.getRoomPicturePc().length );
                                stream.write( dhrm.getRoomPicturePc() );
                            }
                        }
                        else
                        {
                            response.setContentType( "image/jpeg" );
                            response.setContentLength( dhrm.getRoomPicturePc().length );
                            stream.write( dhrm.getRoomPicturePc() );
                        }
                    }
                    else
                    {
                        if ( paramType.compareTo( "gif" ) == 0 )
                        {
                            if ( dhrm.getRoomPictureGif() == null )
                            {
                                response.setContentType( "image/gif" );
                                inputData = new BufferedInputStream( new FileInputStream( imageGif ) );

                                while( (imageData = inputData.read()) != -1 )
                                {
                                    stream.write( imageData );
                                }
                            }
                            else
                            {
                                response.setContentType( "image/gif" );
                                response.setContentLength( dhrm.getRoomPictureGif().length );
                                stream.write( dhrm.getRoomPictureGif() );
                            }
                        }
                        else if ( paramType.compareTo( "png" ) == 0 )
                        {
                            if ( dhrm.getRoomPicturePng() == null )
                            {
                                response.setContentType( "image/png" );
                                inputData = new BufferedInputStream( new FileInputStream( imagePng ) );

                                while( (imageData = inputData.read()) != -1 )
                                {
                                    stream.write( imageData );
                                }
                            }
                            else
                            {
                                response.setContentType( "image/png" );
                                response.setContentLength( dhrm.getRoomPicturePng().length );
                                stream.write( dhrm.getRoomPicturePng() );
                            }
                        }
                        else if ( paramType.compareTo( "jpg" ) == 0 )
                        {
                            if ( dhrm.getRoomPicturePc() == null )
                            {
                                response.setContentType( "image/jpeg" );
                                inputData = new BufferedInputStream( new FileInputStream( imageJpeg ) );

                                while( (imageData = inputData.read()) != -1 )
                                {
                                    stream.write( imageData );
                                }
                            }
                            else
                            {
                                response.setContentType( "image/jpeg" );
                                response.setContentLength( dhrm.getRoomPicturePc().length );
                                stream.write( dhrm.getRoomPicturePc() );
                            }
                        }
                    }
                }
            }
        }
    }
}
