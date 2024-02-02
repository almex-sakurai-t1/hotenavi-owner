/*
 * @(#)HotelRoomPicture.java 1.00 2007/08/15 Copyright (C) ALMEX Inc. 2007 部屋画像出力サーブレット
 */
package jp.happyhotel.user;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * DoCoMoマイメニュー登録・削除サーブレット<br>
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/15
 */
public class UserRegistDocomo extends HttpServlet implements javax.servlet.Servlet
{
    /**
	 *
	 */
    private static final long serialVersionUID = -5186785324124494508L;

    /**
     * DoCoMoマイメニュー登録・削除処理<br>
     * 
     * @param id ホテルID
     * @param room_name 部屋番号
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean ret;
        UserRegist urg;
        ServletOutputStream stream;

        stream = response.getOutputStream();

        urg = new UserRegist();
        ret = urg.setTermInfoDoCoMo( request );
        if ( ret != false )
        {
            // 登録完了
            response.setContentType( "text/plain" );
            // OK<LF>を送信
            stream.print( "OK" );
            stream.print( 0x0a );
        }
        else
        {
            // 登録失敗
            response.setContentType( "text/plain" );
            // NG<LF>を送信
            stream.print( "NG" );
            stream.print( 0x0a );
        }
    }
}
