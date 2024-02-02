/*
 * @(#)FileUploader.java 1.00 2007/08/14 Copyright (C) ALMEX Inc. 2007 ファイルアップロードクラス
 */
package jp.happyhotel.common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * ファイルアップロードクラス<br>
 * <li>multipart/form-dataで画像等アップする場合は、request.getParameterが利用できないので、 FileUploaderのgetParameterを利用する。
 * 
 * @author S.Shiiya
 * @version 1.00 2007/08/14
 */
public class FileUploader implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 404765730349992778L;

    private static final int  FILE_MAX_SIZE    = -1;

    private LogLib            log;

    private long[]            fileSize;
    private String[]          contentType;
    private String[]          inputDataName;
    private String[]          fileName;
    private byte[][]          inputData;
    private String[]          paramName;
    private String[]          paramValue;

    /**
     * ファイルアップロードクラスを初期化します。
     * 
     */
    public FileUploader()
    {
        log = new LogLib();
    }

    public String getContentType(String param)
    {
        int i;
        for( i = 0 ; i < contentType.length ; i++ )
        {
            if ( inputDataName[i].compareTo( param ) == 0 )
            {
                return(contentType[i]);
            }
        }
        return(null);
    }

    public long getFileSize(String param)
    {
        int i;
        for( i = 0 ; i < fileSize.length ; i++ )
        {
            if ( inputDataName[i].compareTo( param ) == 0 )
            {
                return(fileSize[i]);
            }
        }
        return(0);
    }

    public long getIndexOfFileSize(String param, int getindex)
    {
        int i;
        int index = 0;

        for( i = 0 ; i < fileSize.length ; i++ )
        {
            if ( inputDataName[i].compareTo( param ) == 0 )
            {
                if ( index == getindex )
                {
                    return(fileSize[i]);
                }
                else
                {
                    index++;
                }
            }
        }
        return(0);
    }

    public String getFileName(String param)
    {
        int i;
        for( i = 0 ; i < fileSize.length ; i++ )
        {
            if ( inputDataName[i] == null )
            {
                break;
            }
            if ( inputDataName[i].compareTo( param ) == 0 )
            {
                return(fileName[i]);
            }
        }
        return("");
    }

    public byte[] getInputData(String param)
    {
        int i;
        for( i = 0 ; i < inputData.length ; i++ )
        {
            if ( inputDataName[i].compareTo( param ) == 0 )
            {
                return(inputData[i]);
            }
        }
        return(null);
    }

    public byte[] getIndexOfInputData(String param, int getindex)
    {
        int i;
        int index = 0;

        for( i = 0 ; i < inputData.length ; i++ )
        {
            if ( inputDataName[i].compareTo( param ) == 0 )
            {
                if ( index == getindex )
                {
                    return(inputData[i]);
                }
                else
                {
                    index++;
                }
            }
        }
        return(null);
    }

    /**
     * フォームデータ（バイナリ部）の取得
     * 
     * @param request HTTPリクエスト
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(HttpServletRequest request)
    {
        int countData;
        int countBinary;
        boolean ret;

        ret = false;

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold( 1024 );

        ServletFileUpload uploader = new ServletFileUpload( factory );
        uploader.setSizeMax( FILE_MAX_SIZE );

        try
        {
            List items = uploader.parseRequest( request );
            Iterator iter = items.iterator();

            fileSize = new long[items.size()];
            contentType = new String[items.size()];
            inputDataName = new String[items.size()];
            fileName = new String[items.size()];
            inputData = new byte[items.size()][];
            paramName = new String[items.size()];
            paramValue = new String[items.size()];
            countData = 0;
            countBinary = 0;

            while( iter.hasNext() != false )
            {
                FileItem fitem = (FileItem)iter.next();
                if ( fitem.isFormField() == false )
                {
                    // 中身の取得
                    fileSize[countBinary] = fitem.getSize();
                    contentType[countBinary] = fitem.getContentType();
                    inputDataName[countBinary] = fitem.getFieldName();
                    fileName[countBinary] = fitem.getName();
                    inputData[countBinary] = fitem.get();
                    countBinary++;

                    ret = true;
                }
                else
                {
                    paramName[countData] = fitem.getFieldName();
                    // paramValue[countData] = fitem.getString();
                    paramValue[countData] = fitem.getString( request.getCharacterEncoding() );
                    countData++;

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            log.info( "[FileUploader.setData] Exception=" + e.toString() );
            ret = false;
        }

        return(ret);
    }

    /**
     * 同時に送信したパラメタを取得する（バイナリデータ以外）
     * 
     * @param param 取得したいパラメタ名
     * @return 取得結果(ない場合はnull)
     */
    public String getParameter(String param)
    {
        int i;
        if ( paramName != null )
        {
            for( i = 0 ; i < paramName.length ; i++ )
            {
                if ( paramName[i] == null )
                {
                    break;
                }
                if ( paramName[i].compareTo( param ) == 0 )
                {
                    return(paramValue[i]);
                }
            }
        }
        return(null);
    }

    /**
     * パラメタセット処理
     * 
     * @param setName カラム名
     * @param setValue 値
     * 
     */
    public void setParameter(String setName, String setValue)
    {
        int i;
        if ( paramName != null )
        {
            for( i = 0 ; i < paramName.length ; i++ )
            {
                if ( paramName[i] == null )
                {
                    break;
                }
                if ( paramName[i].compareTo( setName ) == 0 )
                {
                    paramValue[i] = setValue;
                }
            }
        }
        return;
    }

    /**
     * 同時に送信したパラメタを取得する（バイナリデータ以外の配列形式）
     * 
     * @param param 取得したいパラメタ名
     * @return 取得結果
     */
    public String[] getParameterValues(String param)
    {
        int filecount = 0;
        int fileIndex = 0;
        String[] ret = null;

        if ( paramName != null )
        {
            for( int i = 0 ; i < paramName.length ; i++ )
            {
                if ( paramName[i] != null && paramName[i].compareTo( param ) == 0 )
                {
                    filecount++;
                }
            }
            ret = new String[filecount];
            for( int i = 0 ; i < paramName.length ; i++ )
            {
                if ( paramName[i] != null && paramName[i].compareTo( param ) == 0 )
                {
                    ret[fileIndex] = paramValue[i];
                    fileIndex++;
                }
            }
        }

        return(ret);
    }

}
