/*
 * @(#)UserDecomePicture.java 1.00 2008/01/30 Copyright (C) ALMEX Inc. 2008 デコメ画像出力サーブレット
 */
package jp.happyhotel.user;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * デコメ画像出力サーブレット<br>
 * パラメータに管理番号(seq)をセットすること。
 * 
 * @author S.Tashiro
 * @version 1.00 2008/02/13
 */
public class UserDecomeSamplePicture extends HttpServlet implements javax.servlet.Servlet
{

    /**
	 *
	 */
    private static final long serialVersionUID = 5723842342929850235L;

    /**
     * 部屋画像の表示<br>
     * パラメータに管理番号(seq)をセットすること。
     * 
     * @param seq 管理番号
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        DataMasterDecome dmd;
        ServletOutputStream stream;

        dmd = new DataMasterDecome();
        stream = response.getOutputStream();

        String seq = request.getParameter( "seq" );
        if ( seq != null )
        {
            if ( CheckString.numCheck( seq ) != false )
            {
                dmd.getData( Integer.parseInt( seq ) );
            }
            if ( dmd.getDecomeSamplePicture() != null )
            {
                response.setContentType( "image/gif" );
                response.setContentLength( dmd.getDecomeSamplePicture().length );
                stream.write( dmd.getDecomeSamplePicture() );
            }
        }
    }
}
