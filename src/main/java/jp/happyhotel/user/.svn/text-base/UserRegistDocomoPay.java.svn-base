/*
 * @(#)HotelRoomPicture.java 1.00 2007/08/15 Copyright (C) ALMEX Inc. 2007 部屋画像出力サーブレット
 */
package jp.happyhotel.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DoCoMoマイメニュー登録・削除サーブレット<br>
 * 
 * @author S.Tashiro
 * @version 1.00 2008/08/24
 */
public class UserRegistDocomoPay extends HttpServlet implements javax.servlet.Servlet
{
    /**
     *
     */
    private static final long serialVersionUID = 4162399202458231603L;

    /**
     * DoCoMo有料マイメニュー登録・削除処理<br>
     * 
     * @param request リクエスト
     * @param response レスポンス
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean ret;
        UserRegistPay urg;
        ServletOutputStream stream;

        stream = response.getOutputStream();

        urg = new UserRegistPay();
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
