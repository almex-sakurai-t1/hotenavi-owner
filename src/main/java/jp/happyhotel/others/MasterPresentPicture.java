/*
 * @(#)MaseterSamplePicture.java 1.00 2007/08/15 Copyright (C) ALMEX Inc. 2007 賞品画像出力サーブレット
 */
package jp.happyhotel.others;

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
import jp.happyhotel.data.DataMasterPresent;
import jp.happyhotel.data.DataMasterUseragent;
import jp.happyhotel.user.UserTermInfo;

/**
 * 賞品画像出力サーブレット<br>
 * パラメータに管理番号をセットすること。
 * 
 * @author S.Tashiro
 * @version 1.00 2008/04/23
 */
public class MasterPresentPicture extends HttpServlet implements javax.servlet.Servlet
{
    /**
     *
     */
    private static final long serialVersionUID = -2166674758389579073L;

    /**
     * 賞品画像の表示<br>
     * パラメータに管理番号(seq)をセットすること。
     * 
     * @param id ホテルID
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int imageData;
        boolean ret;
        String imageJpeg;
        String imageGif;
        String imagePng;
        DataMasterPresent dmp;
        UserTermInfo uti;
        FileInputStream propfile = null;
        Properties config = new Properties();
        ServletOutputStream stream;
        BufferedInputStream inputData;

        propfile = new FileInputStream( "/etc/happyhotel/presentimage.conf" );
        config = new Properties();
        config.load( propfile );

        imageJpeg = config.getProperty( "present.image.jpeg" );
        imageGif = config.getProperty( "present.image.gif" );
        imagePng = config.getProperty( "present.image.png" );

        propfile.close();

        dmp = new DataMasterPresent();
        uti = new UserTermInfo();
        stream = response.getOutputStream();

        String seq = request.getParameter( "seq" );
        if ( seq != null )
        {
            if ( CheckString.numCheck( seq ) != false )
            {
                dmp.getData( Integer.parseInt( seq ) );

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
                                    if ( dmp.getPresentPictureGif() == null )
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
                                        response.setContentLength( dmp.getPresentPictureGif().length );
                                        stream.write( dmp.getPresentPictureGif() );
                                    }
                                    break;

                                case DataMasterUseragent.CARRIER_AU:
                                    if ( dmp.getPresentPicturePng() == null )
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
                                        response.setContentLength( dmp.getPresentPicturePng().length );
                                        stream.write( dmp.getPresentPicturePng() );
                                    }
                                    break;

                                case DataMasterUseragent.CARRIER_SOFTBANK:
                                    if ( dmp.getPresentPicturePng() == null )
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
                                        response.setContentLength( dmp.getPresentPicturePng().length );
                                        stream.write( dmp.getPresentPicturePng() );
                                    }
                                    break;
                            }
                        }
                        else
                        {
                            if ( dmp.getPresentPicturePc() == null )
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
                                response.setContentLength( dmp.getPresentPicturePc().length );
                                stream.write( dmp.getPresentPicturePc() );
                            }
                        }
                    }
                    else
                    {
                        if ( dmp.getPresentPicturePc() == null )
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
                            response.setContentLength( dmp.getPresentPicturePc().length );
                            stream.write( dmp.getPresentPicturePc() );
                        }
                    }
                }
                else
                {
                    if ( CheckString.numCheck( seq ) != false )
                    {
                        dmp.getData( Integer.parseInt( seq ) );
                    }

                    if ( paramType.compareTo( "gif" ) == 0 )
                    {
                        if ( dmp.getPresentPictureGif() == null )
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
                            response.setContentLength( dmp.getPresentPictureGif().length );
                            stream.write( dmp.getPresentPictureGif() );
                        }
                    }
                    else if ( paramType.compareTo( "png" ) == 0 )
                    {
                        if ( dmp.getPresentPicturePng() == null )
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
                            response.setContentLength( dmp.getPresentPicturePng().length );
                            stream.write( dmp.getPresentPicturePng() );
                        }
                    }
                    else if ( paramType.compareTo( "jpg" ) == 0 )
                    {
                        if ( dmp.getPresentPicturePc() == null )
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
                            response.setContentLength( dmp.getPresentPicturePc().length );
                            stream.write( dmp.getPresentPicturePc() );
                        }
                    }
                }
            }
        }
    }
}
