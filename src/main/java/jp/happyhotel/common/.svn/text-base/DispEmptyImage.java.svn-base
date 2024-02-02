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

import jp.happyhotel.data.DataUserMap;

/**
 * 
 * @author S.Tashiro
 * @version 1.00 2009/06/25
 */
public class DispEmptyImage extends HttpServlet implements javax.servlet.Servlet
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
        String paramUserId;
        OutputStream output;
        DataUserMap dum;

        ret = false;
        dum = new DataUserMap();
        paramUserId = request.getParameter( "user_id" );

        if ( paramUserId == null )
        {
            paramUserId = "";
        }

        ret = dum.getData( paramUserId );

        // 通信結果がTrueだったら
        if ( ret != false )
        {
            output = response.getOutputStream();

            // 表示する画像のコンテントタイプをセットする
            response.setContentType( dum.getContentType() );
            // キャッシュさせないように設定する
            response.setHeader( "pragma", "no-cache" );
            response.setHeader( "Cache-Control", "no-cache" );

            if ( dum.getImage() != null )
            {
                // 画像の出力
                output.write( dum.getImage() );
            }
            else if ( dum.getImage2() != null )
            {
                // 画像の出力
                output.write( dum.getImage2() );
            }
        }
    }
}
