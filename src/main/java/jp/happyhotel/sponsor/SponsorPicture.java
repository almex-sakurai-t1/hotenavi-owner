/*
 * @(#)SponsorPicture.java 1.00 2007/09/01 Copyright (C) ALMEX Inc. 2007 スポンサー画像出力サーブレット
 */
package jp.happyhotel.sponsor;

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
import jp.happyhotel.data.DataMasterSponsor;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.user.UserTermInfo;

/**
 * スポンサー画像出力サーブレット<br>
 * パラメータにスポンサーID(sponsor_code)をセットすること。
 * 
 * @author S.Shiiya
 * @version 1.00 2007/09/01
 */
public class SponsorPicture extends HttpServlet implements javax.servlet.Servlet
{
    /**
     *
     */
    private static final long serialVersionUID = 6421219235921362990L;

    /**
     * スポンサー画像の表示<br>
     * パラメータにスポンサーID(sponsor_code)をセットすること。
     * 
     * @param sponsor_id スポンサーコード
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int imageData;
        boolean ret;
        String imageJpeg;
        String imageGif;
        String imagePng;
        DataMasterSponsor dms;
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

        dms = new DataMasterSponsor();
        uti = new UserTermInfo();
        stream = response.getOutputStream();

        String sponsorCode = request.getParameter( "sponsor_code" );
        if ( sponsorCode != null )
        {
            if ( CheckString.numCheck( sponsorCode ) != false )
            {
                dms.getData( Integer.parseInt( sponsorCode ) );

                String paramType = request.getParameter( "type" );
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
                                    if ( dms.getImageGif() == null )
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
                                        response.setContentLength( dms.getImageGif().length );
                                        stream.write( dms.getImageGif() );
                                    }
                                    break;

                                case DataMasterUseragent.CARRIER_AU:
                                    if ( dms.getImagePng() == null )
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
                                        response.setContentLength( dms.getImagePng().length );
                                        stream.write( dms.getImagePng() );
                                    }
                                    break;

                                case DataMasterUseragent.CARRIER_SOFTBANK:
                                    if ( dms.getImagePng() == null )
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
                                        response.setContentLength( dms.getImagePng().length );
                                        stream.write( dms.getImagePng() );
                                    }
                                    break;
                            }
                        }
                        else
                        {
                            if ( dms.getImage() == null )
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
                                response.setContentLength( dms.getImage().length );
                                stream.write( dms.getImage() );
                            }
                        }
                    }
                    else
                    {
                        if ( dms.getImage() == null )
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
                            response.setContentLength( dms.getImage().length );
                            stream.write( dms.getImage() );
                        }
                    }
                }
                else
                {
                    if ( CheckString.numCheck( sponsorCode ) != false )
                    {
                        dms.getData( Integer.parseInt( sponsorCode ) );
                    }
                    if ( paramType.compareTo( "gif" ) == 0 )
                    {
                        if ( dms.getImageGif() == null )
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
                            response.setContentLength( dms.getImageGif().length );
                            stream.write( dms.getImageGif() );
                        }
                    }
                    else if ( paramType.compareTo( "png" ) == 0 )
                    {
                        if ( dms.getImagePng() == null )
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
                            response.setContentLength( dms.getImagePng().length );
                            stream.write( dms.getImagePng() );
                        }
                    }
                    else if ( paramType.compareTo( "jpg" ) == 0 )
                    {
                        if ( dms.getImage() == null )
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
                            response.setContentLength( dms.getImage().length );
                            stream.write( dms.getImage() );
                        }
                    }
                }
            }
        }
    }
}
