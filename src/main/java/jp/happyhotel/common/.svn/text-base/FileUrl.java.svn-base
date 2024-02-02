/*
 * @(#)FileUrl.java 1.00 2011/02/08
 * Copyright (C) ALMEX Inc. 2011
 * ファイルURLクラス
 */

package jp.happyhotel.common;

import java.io.File;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

;

/**
 * ファイルURLクラス
 * 
 * @author S.Tashiro
 * @version 1.00 2011/02/08
 */
public class FileUrl implements Serializable
{
    private static final long serialVersionUID = -4011051532142347226L;

    /****
     * ファイルURL取得クラス
     * 
     * @param path パス
     * @param fileName ファイル名
     * @return 処理結果：文字列、nullだったらエラー
     */
    public static String getFileUrl(String path, String fileName)
    {
        String fileNameUrl = "";
        // バージョンアップ用のファイルを取得する
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
            // ファイル名を返すy
            return(fileNameUrl);
        }
        catch ( Exception e )
        {
            Logging.info( "[FileUrl.getFileUrl()]Exception:" + e.toString() );
            return(null);
        }

    }

    /****
     * ファイル名取得クラス
     * 
     * @param request
     * @return 処理結果：ファイル名
     */
    public static String getFileName(HttpServletRequest request)
    {
        String requestUri = ""; // URI exp. /happyhotel/tool/request.jsp
        String contextPath = "";
        String fileName = "";

        // 自分のURLのうち、ファイル名を取得する
        try
        {
            requestUri = request.getRequestURI();
            contextPath = request.getContextPath();
            if ( requestUri.indexOf( contextPath ) == 0 )
            {
                requestUri = requestUri.substring( contextPath.length() + 1 );
            }

            fileName = requestUri.substring( requestUri.lastIndexOf( File.separator ) + 1, requestUri.length() );

            // ファイル名を返す
            return(fileName);
        }
        catch ( Exception e )
        {
            Logging.info( "[FileUrl.getFileName(request)]Exception:" + e.toString() );
            return(null);
        }

    }

    /****
     * パス名取得クラス
     * 
     * @param request
     * @return 処理結果：パス名
     */
    public static String getPathName(HttpServletRequest request)
    {
        String requestUri = ""; // URI exp. /happyhotel/tool/request.jsp
        String fileName = "";
        String pathName = "";

        // 自分のURLのうち、パス名を取得する
        try
        {
            requestUri = request.getRequestURI();
            fileName = getFileName( request );
            pathName = requestUri.replace( fileName, "" );
            // ファイル名を返す
            return(pathName);
        }
        catch ( Exception e )
        {
            Logging.info( "[FileUrl.getPathName(request)]Exception:" + e.toString() );
            return(null);
        }

    }
}
