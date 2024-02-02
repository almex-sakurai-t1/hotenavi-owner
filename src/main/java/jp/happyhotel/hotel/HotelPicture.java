/*
 * @(#)HotelPicture.java 1.00 2007/08/15 Copyright (C) ALMEX Inc. 2007 ホテルTOP画像出力サーブレット
 */
package jp.happyhotel.hotel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.UserAgent;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.user.UserTermInfo;

/**
 * ホテルTOP画像出力サーブレット<br>
 * パラメータにホテルID(id)をセットすること。
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/15
 */
public class HotelPicture extends HttpServlet implements javax.servlet.Servlet
{
    /**
     *
     */
    private static final long serialVersionUID = -2166674758389579073L;

    private static String     imageJpeg;
    private static String     imageGif;
    private static String     imagePng;
    private static String     imageLvjJpeg;
    private static String     imageLvjGif;
    private static String     imageLvjPng;
    private static String     hotelImageJpeg;
    private static String     hotelImageGif;
    private static String     hotelImagePng;

    /**
     * Servlet初期処理
     * 
     */
    public void init(ServletConfig config) throws ServletException
    {
        FileInputStream propfile = null;
        Properties propConfig = new Properties();

        super.init( config );

        try
        {
            propfile = new FileInputStream( "/etc/happyhotel/hotelimage.conf" );
            propConfig = new Properties();
            propConfig.load( propfile );

            imageJpeg = propConfig.getProperty( "hotel.image.jpeg" );
            imageGif = propConfig.getProperty( "hotel.image.gif" );
            imagePng = propConfig.getProperty( "hotel.image.png" );

            imageLvjJpeg = propConfig.getProperty( "hotel.image.lvj.jpeg" );
            imageLvjGif = propConfig.getProperty( "hotel.image.lvj.gif" );
            imageLvjPng = propConfig.getProperty( "hotel.image.lvj.png" );

            hotelImageJpeg = propConfig.getProperty( "hotelpicture.jpeg" );
            hotelImageGif = propConfig.getProperty( "hotelpicture.gif" );
            hotelImagePng = propConfig.getProperty( "hotelpicture.png" );
        }
        catch ( Exception e )
        {
        }
        finally
        {
            if ( propfile != null )
            {
                try
                {
                    propfile.close();
                }
                catch ( Exception e )
                {
                }
            }
        }
    }

    /**
     * 部屋画像の表示<br>
     * パラメータにホテルID(id)をセットすること。
     * 
     * @param id ホテルID
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int imageData;
        boolean ret;
        UserTermInfo uti;
        ServletOutputStream stream;
        BufferedInputStream inputData;
        File fileJpeg;
        File fileGif;
        File filePng;
        File fileReNew;
        String hImageJpeg;
        String hImageGif;
        String hImagePng;
        String hImageRenew;

        String image_Jpeg;
        String image_Gif;
        String image_Png;

        inputData = null;
        uti = new UserTermInfo();
        stream = response.getOutputStream();

        String hotelId = request.getParameter( "id" );
        String foreignFlag = request.getParameter( "foreign_flag" );

        image_Jpeg = imageJpeg;
        image_Gif = imageGif;
        image_Png = imagePng;

        if ( foreignFlag != null )
        {
            if ( foreignFlag.equals( "1" ) )
            {
                image_Jpeg = imageLvjJpeg;
                image_Gif = imageLvjGif;
                image_Png = imageLvjPng;
            }
        }
        if ( hotelId != null )
        {
            if ( CheckString.numCheck( hotelId ) != false )
            {
                hImageJpeg = hotelImageJpeg + hotelId + "jpg.jpg";
                hImageGif = hotelImageGif + hotelId + "gif.gif";
                hImagePng = hotelImagePng + hotelId + "png.png";
                hImageRenew = hotelImageJpeg + hotelId + "n.jpg";

                String paramType = request.getParameter( "type" );
                try
                {
                    if ( paramType == null )
                    {
                        Logging.info( "UserAgentType:" + UserAgent.getUserAgentType( request ) );
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
                                        fileGif = new File( hImageGif );
                                        if ( fileGif.exists() == false )
                                        {
                                            response.setContentType( "image/gif" );
                                            inputData = new BufferedInputStream( new FileInputStream( image_Gif ) );
                                        }
                                        else
                                        {
                                            response.setContentType( "image/gif" );
                                            inputData = new BufferedInputStream( new FileInputStream( hImageGif ) );
                                        }
                                        while( (imageData = inputData.read()) != -1 )
                                        {
                                            stream.write( imageData );
                                        }
                                        if ( fileGif != null )
                                        {
                                            fileGif = null;
                                        }
                                        break;

                                    case DataMasterUseragent.CARRIER_AU:
                                        filePng = new File( hImagePng );
                                        if ( filePng.exists() == false )
                                        {
                                            response.setContentType( "image/png" );
                                            inputData = new BufferedInputStream( new FileInputStream( image_Png ) );
                                        }
                                        else
                                        {
                                            response.setContentType( "image/png" );
                                            inputData = new BufferedInputStream( new FileInputStream( hImagePng ) );
                                        }
                                        while( (imageData = inputData.read()) != -1 )
                                        {
                                            stream.write( imageData );
                                        }
                                        if ( filePng != null )
                                        {
                                            filePng = null;
                                        }
                                        break;

                                    case DataMasterUseragent.CARRIER_SOFTBANK:
                                        filePng = new File( hImagePng );
                                        if ( filePng.exists() == false )
                                        {
                                            response.setContentType( "image/png" );
                                            inputData = new BufferedInputStream( new FileInputStream( image_Png ) );
                                        }
                                        else
                                        {
                                            response.setContentType( "image/png" );
                                            inputData = new BufferedInputStream( new FileInputStream( hImagePng ) );
                                        }
                                        while( (imageData = inputData.read()) != -1 )
                                        {
                                            stream.write( imageData );
                                        }
                                        if ( filePng != null )
                                        {
                                            filePng = null;
                                        }
                                        break;
                                }
                            }
                            else
                            {
                                fileJpeg = new File( hImageJpeg );

                                if ( fileJpeg.exists() == false )
                                {
                                    response.setContentType( "image/jpeg" );
                                    inputData = new BufferedInputStream( new FileInputStream( image_Jpeg ) );
                                }
                                else
                                {
                                    response.setContentType( "image/jpeg" );
                                    inputData = new BufferedInputStream( new FileInputStream( hImageJpeg ) );
                                }
                                while( (imageData = inputData.read()) != -1 )
                                {
                                    stream.write( imageData );
                                }

                                if ( fileJpeg != null )
                                {
                                    fileJpeg = null;
                                }
                            }
                        }
                        else
                        {
                            fileReNew = new File( hImageRenew );
                            fileJpeg = new File( hImageJpeg );

                            if ( fileReNew.exists() == false )
                            {
                                if ( fileJpeg.exists() == false )
                                {
                                    response.setContentType( "image/jpeg" );
                                    inputData = new BufferedInputStream( new FileInputStream( image_Jpeg ) );
                                }
                                else
                                {
                                    response.setContentType( "image/jpeg" );
                                    inputData = new BufferedInputStream( new FileInputStream( hImageJpeg ) );
                                }
                            }
                            else
                            {
                                response.setContentType( "image/jpeg" );
                                inputData = new BufferedInputStream( new FileInputStream( hImageRenew ) );
                            }

                            while( (imageData = inputData.read()) != -1 )
                            {
                                stream.write( imageData );
                            }

                            if ( fileJpeg != null )
                            {
                                fileJpeg = null;
                            }
                        }
                    }
                    else
                    {
                        if ( paramType.compareTo( "gif" ) == 0 )
                        {
                            fileGif = new File( hImageGif );
                            if ( fileGif.exists() == false )
                            {
                                response.setContentType( "image/gif" );
                                inputData = new BufferedInputStream( new FileInputStream( image_Gif ) );
                            }
                            else
                            {
                                response.setContentType( "image/gif" );
                                inputData = new BufferedInputStream( new FileInputStream( hImageGif ) );
                            }
                            while( (imageData = inputData.read()) != -1 )
                            {
                                stream.write( imageData );
                            }
                            if ( fileGif != null )
                            {
                                fileGif = null;
                            }
                        }
                        else if ( paramType.compareTo( "png" ) == 0 )
                        {
                            filePng = new File( hImagePng );
                            if ( filePng.exists() == false )
                            {
                                response.setContentType( "image/png" );
                                inputData = new BufferedInputStream( new FileInputStream( image_Png ) );
                            }
                            else
                            {
                                response.setContentType( "image/png" );
                                inputData = new BufferedInputStream( new FileInputStream( hImagePng ) );
                            }
                            while( (imageData = inputData.read()) != -1 )
                            {
                                stream.write( imageData );
                            }
                            if ( filePng != null )
                            {
                                filePng = null;
                            }
                        }
                        else if ( paramType.compareTo( "jpg" ) == 0 )
                        {
                            fileJpeg = new File( hImageJpeg );

                            if ( fileJpeg.exists() == false )
                            {
                                response.setContentType( "image/jpeg" );
                                inputData = new BufferedInputStream( new FileInputStream( image_Jpeg ) );
                            }
                            else
                            {
                                response.setContentType( "image/jpeg" );
                                inputData = new BufferedInputStream( new FileInputStream( hImageJpeg ) );
                            }
                            while( (imageData = inputData.read()) != -1 )
                            {
                                stream.write( imageData );
                            }

                            if ( fileJpeg != null )
                            {
                                fileJpeg = null;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {

                }
                finally
                {
                    if ( stream != null )
                    {
                        stream.close();
                    }
                    if ( inputData != null )
                    {
                        inputData.close();
                    }
                }
            }
        }
    }
}
