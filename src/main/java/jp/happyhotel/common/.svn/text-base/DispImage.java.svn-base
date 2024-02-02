/*
 * @(#)DispImage.java 1.00 2009/06/25 Copyright (C) ALMEX Inc. 2008 ホテル地図画像出力サーブレット
 */
package jp.happyhotel.common;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 画像表示クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2009/06/25
 */
public class DispImage extends HttpServlet implements javax.servlet.Servlet
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
        String paramUrl;
        OutputStream output;
        HttpConnection con;

        ret = false;
        paramUrl = request.getParameter( "url" );
        output = null;
        con = new HttpConnection();

        // &を他の文字に変換して、リクエスト時に情報が抜けないようにする
        if ( paramUrl != null )
        {
            paramUrl = paramUrl.replaceAll( "!", "&" );
        }

        ret = false;
        // URLを指定して、HTTP通信させる
        ret = con.urlConnection( request, response, paramUrl );

        // 通信結果がTrueだったら
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
    }
}
