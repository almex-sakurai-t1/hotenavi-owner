/*
 * @(#)PictureString.java  2.00 2004/10/29
 *
 * Copyright (C) ALMEX Inc. 2004
 *
 * 絵文字関連編集
 */

package com.hotenavi2.common;

import java.io.*;
import java.lang.*;

/**
 *  絵文字関連編集クラス
 *
 *  @version 2.00
 */
public class PictureString implements Serializable
{
    /**
     *  絵文字をカットする(InputStream)
     *
     *  @param inputorg 編集元ストリーム
     *  @return 編集後文字列
     *
     */
    public static String cut(InputStream inputorg)
    {
        String newstr = "";

        try
        {
            while( true )
            {
                int read = inputorg.read();
                if( read == -1 )
                {
                    break;
                }

                newstr  = newstr  + (char)read;
            }

            return( new String(newstr.getBytes("8859_1"), "Windows-31J") );
        }
        catch( Exception e )
        {
            return( newstr );
        }

/**
        try
        {
            while( true )
            {
                int read = inputorg.read();
                if( read == -1 )
                {
                    break;
                }

                // 絵文字判定
                if( read > 0xf6 )
                {
                    // 絵文字(i-mode,au用)
                    // 次の文字コードを読み飛ばす
                    inputorg.read();
                }
                else if( read == 0x1b )
                {
                    // 絵文字(vodafone用)
                    // 0x0Fが出るまで読み飛ばし
                    while( true )
                    {
                        read = inputorg.read();
                        if( read == 0x0f || read == -1 )
                        {
                            break;
                        }
                    }
                }
                else
                {
                    newstr  = newstr  + (char)read;
                }
            }

            return( new String(newstr.getBytes("8859_1"), "Windows-31J") );
        }
        catch(Exception e)
        {
            return( newstr );
        }
**/
    }


    /**
     *  絵文字をカットする(String)
     *
     *  @param inputorg 編集元ストリーム
     *  @return 編集後文字列
     *
     */
    public static String cut(String inputorg)
    {
        int i;
        boolean vodastart = false;
        String newstr = "";

        return( inputorg );
/**
        try
        {
            char read[] = new char[inputorg.length()];
            read = inputorg.toCharArray();

            for( i = 0 ; i < inputorg.length() ; i++ )
            {
                // VODAFONE用(開始コード)
                if( read[i] == 0x1b )
                {
                    vodastart = true;
                }

                if( vodastart == false )
                {
                    if( read[i] < 0xE000 || read[i] > 0xF8FF )
                    {
                        newstr = newstr + read[i];
                    }
                }

                // VODAFONE用(終了コード)
                if( read[i] == 0x0f )
                {
                    vodastart = false;
                }

            }

            return( newstr );
        }
        catch(Exception e)
        {
            return( newstr );
        }
**/
    }
}

