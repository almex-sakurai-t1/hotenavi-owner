/*
 * @(#)DispImage.java 1.00 2009/06/25 Copyright (C) ALMEX Inc. 2008 �z�e���n�}�摜�o�̓T�[�u���b�g
 */
package jp.happyhotel.common;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * �摜�\���N���X
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
     * @param id �z�e��ID
     * @param scale_size �k�ڃT�C�Y
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

        // &�𑼂̕����ɕϊ����āA���N�G�X�g���ɏ�񂪔����Ȃ��悤�ɂ���
        if ( paramUrl != null )
        {
            paramUrl = paramUrl.replaceAll( "!", "&" );
        }

        ret = false;
        // URL���w�肵�āAHTTP�ʐM������
        ret = con.urlConnection( request, response, paramUrl );

        // �ʐM���ʂ�True��������
        if ( ret != false )
        {
            output = response.getOutputStream();

            // �L���b�V�������Ȃ��悤�ɐݒ肷��
            response.setHeader( "pragma", "no-cache" );
            response.setHeader( "Cache-Control", "no-cache" );
            // �\������摜�̃R���e���g�^�C�v���Z�b�g����
            response.setContentType( con.getContentType() );

            // �摜�̏o��
            output.write( con.getImage(), 0, con.getImageSize() );
        }
    }
}
