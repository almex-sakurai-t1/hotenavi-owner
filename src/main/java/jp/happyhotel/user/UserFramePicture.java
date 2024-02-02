/*
 * @(#)UserDecomePicture.java 1.00 2008/01/30 Copyright (C) ALMEX Inc. 2008 デコメ画像出力サーブレット
 */
package jp.happyhotel.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.data.DataMasterDecome;
import jp.happyhotel.data.DataMasterUseragent;

/**
 * フレーム画像出力サーブレット<br>
 * パラメータに管理番号(seq)をセットすること。
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/15
 */
public class UserFramePicture extends HttpServlet implements javax.servlet.Servlet
{

    /**
     *
     */
    private static final long serialVersionUID = 8490379212918480510L;

    /**
     * 部屋画像の表示<br>
     * パラメータに管理番号(seq)をセットすること。
     * 
     * @param seq 管理番号
     * @param carrierType キャリアタイプ（0:DoCoMo,1:au,2:SoftBnak）
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        DataMasterDecome dmd;
        ServletOutputStream stream;

        dmd = new DataMasterDecome();
        stream = response.getOutputStream();

        String seq = request.getParameter( "seq" );
        String carrierType = request.getParameter( "carrier" );

        if ( (carrierType == null) || (carrierType.compareTo( "" ) == 0) || (CheckString.numCheck( carrierType ) == false) )
        {
            carrierType = "0";
        }
        if ( seq != null )
        {
            if ( CheckString.numCheck( seq ) != false )
            {
                dmd.getData( Integer.parseInt( seq ) );
            }

            // キャリアに応じてcontentTypeをセットし出力
            if ( Integer.parseInt( carrierType ) == DataMasterUseragent.CARRIER_AU )
            {
                if ( dmd.getPictureAu() != null )
                {
                    response.setContentType( "image/png" );
                    response.setContentLength( dmd.getPictureAu().length );
                    stream.write( dmd.getPictureAu() );
                }
            }
            else if ( Integer.parseInt( carrierType ) == DataMasterUseragent.CARRIER_SOFTBANK )
            {
                if ( dmd.getPictureSoftbank() != null )
                {
                    response.setContentType( "image/png" );
                    response.setContentLength( dmd.getPictureSoftbank().length );
                    stream.write( dmd.getPictureSoftbank() );
                }
            }
            else
            {
                if ( dmd.getPictureDocomo() != null )
                {
                    response.setContentType( "image/gif" );
                    response.setContentLength( dmd.getPictureDocomo().length );
                    stream.write( dmd.getPictureDocomo() );
                }
            }
        }
    }
}
