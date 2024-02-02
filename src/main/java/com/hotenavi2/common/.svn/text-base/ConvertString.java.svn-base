/*
 * @(#)ConvertString.java 1.00
 * 2009/07/27 Copyright (C) ALMEX Inc. 2009
 * 文字列変換クラス
 */

package com.hotenavi2.common;

import java.io.Serializable;
import java.security.MessageDigest;

/**
 * 文字列変換クラス
 * 
 * @author N.ide
 * @version 1.00 2009/07/27
 */
public class ConvertString implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 7002044289771566075L;

    /**
     * 文字列をhash値(MD5)に変換する
     * 
     * @param input 変換対象文字列
     * @return 処理結果("":失敗)
     */

    public static String convert2md5(String input)
    {
        StringBuffer Buff;
        MessageDigest md;
        byte[] dat, digest;
        int i, nDigest;

        try
        {
            Buff = new StringBuffer();

            // インスタンス取得時にハッシュアルゴリズムをMD5指定
            md = MessageDigest.getInstance( "MD5" );

            // 入力された文字列からダイジェストを計算
            dat = input.getBytes();
            md.update( dat );
            digest = md.digest();

            for( i = 0 ; i < digest.length ; i++ )
            {
                nDigest = digest[i];
                // byte型では128～255が負になっているので補正
                if ( nDigest < 0 )
                {
                    nDigest += 256;
                }
                // 0～15は16進数で1桁なので2桁になるよう頭に0を追加
                if ( nDigest < 16 )
                {
                    Buff.append( "0" );
                }
                Buff.append( Integer.toString( nDigest, 16 ) );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "ConvertString.convert2md5 Exception=" + e.toString() );
            return("");
        }
        return(Buff.toString());
    }

    public static String convert2md5(String input, int loop)
    {
        loop = loop >= 512 ? 1 : 512 - loop;

        String passwd_pc_hashed = input;
        for( int i = 0 ; i < loop ; i++ )
        {
            passwd_pc_hashed = convert2md5( passwd_pc_hashed );
        }
        return passwd_pc_hashed;
    }
}
