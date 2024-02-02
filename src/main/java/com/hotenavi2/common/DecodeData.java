package com.hotenavi2.common;

import java.io.Serializable;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecodeData implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 7242408736249276255L;

    /**
     * 復号化処理(AES/CBC/PKCS5Padding方式で復号化)
     * 
     * @param encByteStr 暗号化文字列
     * @return 処理結果(true,false)
     **/
    public static String decode(String encByteStr)
    {
        final String KEY = "h o t e n a v i ";
        final String IVKEY = "a l m e x 1 2 3 ";
        int nHead;
        int nTail;
        int i = 0;
        boolean ret = false;
        String strDate = "";
        String strTime = "";
        String strOut = "";
        byte[] decodeWord = null;
        byte[] key = null;
        byte[] ivBytes = null;
        byte[] encByteByte = null;

        // 暗号キー
        key = KEY.getBytes();

        // 暗号ベクター（Initialization Vector：初期化ベクトル）
        ivBytes = IVKEY.getBytes();

        // 暗号化文字列の文字数の半分だけバイト配列を用意
        encByteByte = new byte[encByteStr.length() / 2];
        // 暗号化文字列をバイトに変換
        for( i = 0 ; i < encByteStr.length() ; i += 2 )
        {
            nHead = ConvertHexToInt.HexToInt( String.valueOf( encByteStr.charAt( i ) ) );
            nTail = ConvertHexToInt.HexToInt( String.valueOf( encByteStr.charAt( i + 1 ) ) );

            encByteByte[i / 2] = (byte)(nHead * 16 + nTail);
        }

        Key skey = new SecretKeySpec( key, "AES" );

        // 復号化
        decodeWord = decodeWithVector( encByteByte, ivBytes, skey );

        if ( decodeWord != null )
        {
            // 文字列変換
            strOut = new String( decodeWord );

        }

        return(strOut);
    }

    /**
     * 復号化処理(AES/CBC/PKCS5Padding方式で復号化)
     * 
     * @param key 暗号キー
     * @param ivBytes 暗号ベクター
     * @param encByteStr 暗号化文字列
     * @return 復号化された文字列
     **/
    public static String decode(byte[] key, byte[] ivBytes, String encByteStr)
    {
        int nHead;
        int nTail;
        int i = 0;
        boolean ret = false;
        String strDate = "";
        String strTime = "";
        String strOut = "";
        byte[] decodeWord = null;
        byte[] encByteByte = null;

        // 暗号化文字列の文字数の半分だけバイト配列を用意
        encByteByte = new byte[encByteStr.length() / 2];
        // 暗号化文字列をバイトに変換
        for( i = 0 ; i < encByteStr.length() ; i += 2 )
        {
            nHead = ConvertHexToInt.HexToInt( String.valueOf( encByteStr.charAt( i ) ) );
            nTail = ConvertHexToInt.HexToInt( String.valueOf( encByteStr.charAt( i + 1 ) ) );

            encByteByte[i / 2] = (byte)(nHead * 16 + nTail);
        }

        Key skey = new SecretKeySpec( key, "AES" );

        // 復号化
        decodeWord = decodeWithVector( encByteByte, ivBytes, skey );

        if ( decodeWord != null )
        {
            // 文字列変換
            strOut = new String( decodeWord );

        }

        return(strOut);
    }

    /***
     * 復号処理
     * 
     * @param src 復号する暗号文字列
     * @param ivBytes 暗号ベクタ
     * @param skey 秘密キー
     * @return 復号文字列
     */
    public static byte[] decodeWithVector(byte[] src, byte[] ivBytes, Key skey)
    {
        try
        {
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
            cipher.init( Cipher.DECRYPT_MODE, skey, new IvParameterSpec( ivBytes ) );
            return cipher.doFinal( src );
        }
        catch ( Exception e )
        {
            Logging.error( "[ActionHapiTouch.decodeWithVector()] Exception:" + e.toString() );
            return(null);
        }
    }

}
