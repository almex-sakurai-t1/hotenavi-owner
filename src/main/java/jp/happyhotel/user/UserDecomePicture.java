/*
 * @(#)UserDecomePicture.java 1.00 2008/01/30 Copyright (C) ALMEX Inc. 2008 �f�R���摜�o�̓T�[�u���b�g
 */
package jp.happyhotel.user;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.happyhotel.common.*;
import jp.happyhotel.data.*;

/**
 * �f�R���摜�o�̓T�[�u���b�g<br>
 * �p�����[�^�ɊǗ��ԍ�(seq)���Z�b�g���邱�ƁB
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/15
 */
public class UserDecomePicture extends HttpServlet implements javax.servlet.Servlet
{

    /**
	 *
	 */
    private static final long serialVersionUID = 8490379212918480510L;

    /**
     * �����摜�̕\��<br>
     * �p�����[�^�ɊǗ��ԍ�(seq)���Z�b�g���邱�ƁB
     * 
     * @param seq �Ǘ��ԍ�
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
            if ( dmd.getDecomePicture() != null )
            {
                response.setContentType( "image/gif" );
                response.setContentLength( dmd.getDecomePicture().length );
                stream.write( dmd.getDecomePicture() );
            }
        }
    }
}
