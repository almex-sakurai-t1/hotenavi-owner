/*
 * @(#)LedgerSheet.java 1.00 2009/03/31
 * Copyright (C) ALMEX Inc. 2009
 * クラス
 */

package com.hotenavi2.owner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotenavi2.common.DbAccess;

/**
 * 帳票pdfファイル取得サーブレット
 * 
 * @author N.Ide
 * @version 1.00 2009/03/31
 */

public class LedgerSheet extends HttpServlet
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 6026762883860107487L;

    /**
     * pdfファイルの取得
     * 
     * @param HotelId ホテルID
     * @param user ユーザID
     * @param fname ファイル名
     * @param ftype ファイルタイプ(sales or access)
     * @param num シート番号
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {

        final String confFilePass = "/etc/hotenavi/ledgersheet.conf";
        String hotelId = null;
        String fileName = null;
        String fileType = null;
        String serverAddress = null;
        String spec = null;
        String loginHotelId = null;
        String sheetNumStr = null;
        FileInputStream propfile = null;
        Properties config = null;
        ServletOutputStream stream;
        int sheetNum = -1;
        int count;
        byte[] readBuff;

        hotelId = (String)request.getSession().getAttribute( "SelectHotel" );
        fileName = request.getParameter( "fname" );
        fileType = request.getParameter( "ftype" );
        sheetNumStr = request.getParameter( "num" );
        if ( sheetNumStr != null )
        {
            sheetNum = Integer.parseInt( sheetNumStr );
        }

        if ( hotelId != null && fileName != null && fileType != null && sheetNum > 0 )
        {
            loginHotelId = (String)request.getSession().getAttribute( "LoginHotelId" );

            // 多店舗帳票の場合、SelectHotel=allなので、集計ホテルIDを取得する
            if ( hotelId.compareTo( "all" ) == 0 )
            {
                DbAccess db = new DbAccess();
                OwnerInfo ownerinfo = new OwnerInfo();
                ResultSet resultHotel = ownerinfo.getHotelInfo( db, loginHotelId );
                if ( resultHotel != null )
                {
                    try
                    {
                        if ( resultHotel.first() != false )
                        {
                            hotelId = resultHotel.getString( "hotel.center_id" );
                            if ( hotelId.compareTo( "" ) == 0 )
                            {
                                hotelId = loginHotelId;
                            }
                        }
                        else
                        {
                            hotelId = null;
                        }
                        resultHotel.close();
                    }
                    catch ( SQLException e )
                    {
                        hotelId = null;
                    }
                }
                db.close();
            }

            if ( loginHotelId != null && hotelId != null )
            {
                String existFileName = "/hotenavi/" + hotelId + "/pc/" + fileType + "/" + fileName + "-" + sheetNum + ".pdf";
                File existFile = new File( existFileName );
                if ( existFile.exists() == false )
                {
                    try
                    {
                        propfile = new FileInputStream( confFilePass );
                        config = new Properties();
                        config.load( propfile );
                        propfile.close();
                    }
                    catch ( Exception e )
                    {
                        e.printStackTrace();
                    }

                    // PDF変換先URLの取得
                    serverAddress = config.getProperty( "server.address" );

                    if ( serverAddress != null )
                    {
                        // 接続先URLの生成
                        spec = serverAddress
                                + "?HotelId=" + hotelId
                                + "&fname=" + fileName
                                + "&num=" + sheetNum
                                + "&ftype=" + fileType;

                        try
                        {
                            URL pdfURL = new URL( spec );
                            URLConnection pdfConnect = (HttpURLConnection)pdfURL.openConnection();

                            pdfConnect.connect();

                            stream = response.getOutputStream();
                            response.setContentType( pdfConnect.getContentType() );
                            // response.setContentLength( pdfConnect.getContentLength() );
                            System.out.println( "pdfConnect.getContentLength()=" + pdfConnect.getContentLength() );
                            response.setHeader( "Content-disposition", "inline; filename=\"" + fileName + ".pdf\"" );

                            readBuff = new byte[1024];
                            // 戻ってきたイメージをByte配列に格納
                            while( true )
                            {
                                count = pdfConnect.getInputStream().read( readBuff );
                                if ( count == -1 )
                                {
                                    break;
                                }
                                for( int i = 0 ; i < count ; i++ )
                                {
                                    stream.write( readBuff[i] );
                                }
                            }
                        }
                        catch ( Exception e )
                        {
                            // ファイルなし
                            try
                            {
                                response.sendError( HttpServletResponse.SC_NOT_FOUND );
                            }
                            catch ( IOException ioe )
                            {
                                ioe.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        // ファイルなし
                        try
                        {
                            response.sendError( HttpServletResponse.SC_NOT_FOUND );
                        }
                        catch ( IOException e )
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    // すでにPDFファイルがある場合
                    try
                    {
                        stream = response.getOutputStream();
                        FileInputStream fistream = new FileInputStream( existFile );
                        response.setContentType( "application/pdf" );

                        readBuff = new byte[1024];

                        while( true )
                        {
                            count = fistream.read( readBuff );
                            if ( count == -1 )
                            {
                                break;
                            }
                            for( int i = 0 ; i < count ; i++ )
                            {
                                stream.write( readBuff[i] );
                            }
                        }
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }

                }
            }
            else
            {
                try
                {
                    response.sendError( HttpServletResponse.SC_NOT_FOUND );
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            try
            {
                response.sendError( HttpServletResponse.SC_NOT_FOUND );
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }
}
