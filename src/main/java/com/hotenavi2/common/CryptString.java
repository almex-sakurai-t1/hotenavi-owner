/*
 * @(#)CryptString.java  2.00 2005/09/14
 *
 * Copyright (C) ALMEX Inc. 2005
 *
 * 文字列暗号化処理
 */

package com.hotenavi2.common;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.text.*;
import java.security.*;
import javax.crypto.*;
import com.hotenavi2.common.*;


/**
 * 文字列操作をおこなうクラス。
 *
 * @author  S.Shiiya
 * @version 2.00 2004/12/02
 */
public class CryptString implements Serializable
{
    /**
     * トリップ文字列取得
     *
     * @param text 処理の対象の文字列
     * @return 処理後の文字列
     */
    public static String bbstrip(String text)
    {
        int       tripidx;
        String    cryptdata;
        String    namedata;

        // ◆は◇に変換
        text = ReplaceString.replace(text, "◆", "◇");

        // #文字列を探す
        tripidx = text.indexOf("#");
        if( tripidx < 0 )
        {
            // ない場合はそのまま返す
            return( text );
        }
        else
        {
            cryptdata = text.substring(tripidx+1);
            namedata  = text.substring(0, tripidx);

            try
            {
                cryptdata = encrypt(cryptdata, cryptdata);
                namedata = namedata + "◆" + cryptdata;
            }
            catch( Exception e )
            {
            }

        }

        return( namedata );
    }

    private static String encrypt(String key, String text) throws
                javax.crypto.IllegalBlockSizeException,
                java.security.InvalidKeyException,
                java.security.NoSuchAlgorithmException,
                java.io.UnsupportedEncodingException,
                javax.crypto.BadPaddingException,
                javax.crypto.NoSuchPaddingException
    {
        String cryptdata = "";

        MessageDigest md = MessageDigest.getInstance("MD5");
	md.update(text.getBytes());
        byte hash[]=md.digest();

        for(int j=0; j<hash.length; j++)
        {
            int r = (hash[j] >= 0) ? hash[j]:(256+hash[j]);
            if(r < 0x10) {
                System.out.print("0");
            }
            cryptdata = cryptdata + Integer.toString(r, 16);
        }

        return( cryptdata.substring( 1, 10 ) );
    }
}

