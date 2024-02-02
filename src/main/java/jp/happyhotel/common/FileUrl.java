/*
 * @(#)FileUrl.java 1.00 2011/02/08
 * Copyright (C) ALMEX Inc. 2011
 * �t�@�C��URL�N���X
 */

package jp.happyhotel.common;

import java.io.File;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

;

/**
 * �t�@�C��URL�N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2011/02/08
 */
public class FileUrl implements Serializable
{
    private static final long serialVersionUID = -4011051532142347226L;

    /****
     * �t�@�C��URL�擾�N���X
     * 
     * @param path �p�X
     * @param fileName �t�@�C����
     * @return �������ʁF������Anull��������G���[
     */
    public static String getFileUrl(String path, String fileName)
    {
        String fileNameUrl = "";
        // �o�[�W�����A�b�v�p�̃t�@�C�����擾����
        try
        {
            File file = new File( path );
            File[] listFile = file.listFiles();

            if ( listFile.length > 0 )
            {
                for( int i = 0 ; i < listFile.length ; i++ )
                {
                    if ( listFile[i].toString().indexOf( fileName ) > -1 )
                    {
                        fileNameUrl = listFile[i].toString();
                        break;
                    }
                }
            }
            if ( fileNameUrl.compareTo( "" ) != 0 )
            {
                fileNameUrl = fileNameUrl.substring( fileNameUrl.indexOf( fileName ) );
            }
            // �t�@�C������Ԃ�y
            return(fileNameUrl);
        }
        catch ( Exception e )
        {
            Logging.info( "[FileUrl.getFileUrl()]Exception:" + e.toString() );
            return(null);
        }

    }

    /****
     * �t�@�C�����擾�N���X
     * 
     * @param request
     * @return �������ʁF�t�@�C����
     */
    public static String getFileName(HttpServletRequest request)
    {
        String requestUri = ""; // URI exp. /happyhotel/tool/request.jsp
        String contextPath = "";
        String fileName = "";

        // ������URL�̂����A�t�@�C�������擾����
        try
        {
            requestUri = request.getRequestURI();
            contextPath = request.getContextPath();
            if ( requestUri.indexOf( contextPath ) == 0 )
            {
                requestUri = requestUri.substring( contextPath.length() + 1 );
            }

            fileName = requestUri.substring( requestUri.lastIndexOf( File.separator ) + 1, requestUri.length() );

            // �t�@�C������Ԃ�
            return(fileName);
        }
        catch ( Exception e )
        {
            Logging.info( "[FileUrl.getFileName(request)]Exception:" + e.toString() );
            return(null);
        }

    }

    /****
     * �p�X���擾�N���X
     * 
     * @param request
     * @return �������ʁF�p�X��
     */
    public static String getPathName(HttpServletRequest request)
    {
        String requestUri = ""; // URI exp. /happyhotel/tool/request.jsp
        String fileName = "";
        String pathName = "";

        // ������URL�̂����A�p�X�����擾����
        try
        {
            requestUri = request.getRequestURI();
            fileName = getFileName( request );
            pathName = requestUri.replace( fileName, "" );
            // �t�@�C������Ԃ�
            return(pathName);
        }
        catch ( Exception e )
        {
            Logging.info( "[FileUrl.getPathName(request)]Exception:" + e.toString() );
            return(null);
        }

    }
}
