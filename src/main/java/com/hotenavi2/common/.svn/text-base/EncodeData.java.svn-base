package com.hotenavi2.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.MimeUtility;

import org.apache.commons.codec.binary.Base64;

public class EncodeData
{

    /**
     * 暗号化処理(AES/CBC/PKCS5Padding方式で暗号化)
     * 
     * @param key 暗号キー
     * @param ivBytes 暗号ベクター
     * @param encByteStr 暗号化する文字列
     * @return 復号化された文字列
     **/
    public static String encodeString(byte[] key, byte[] ivBytes, String encByteStr)
    {
        String strOut = "";
        byte[] decodeWord = null;
        byte[] encByteByte = null;

        encByteByte = encByteStr.getBytes();
        // 秘密鍵の構築
        Key skey = new SecretKeySpec( key, "AES" );

        try
        {
            // 復号を行うアルゴリズム、モード、パディング方式を指定
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
            // 初期化
            cipher.init( Cipher.ENCRYPT_MODE, skey, new IvParameterSpec( ivBytes ) );
            // 暗号化
            decodeWord = cipher.doFinal( encByteByte );

            if ( decodeWord != null )
            {
                strOut = encodeBase64( decodeWord );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[EncodeData.encodeString()] Exception:" + e.toString() );
            return(strOut);
        }
        return(strOut);
    }

    /**
     * 暗号化処理(AES/CBC/PKCS5Padding方式で暗号化)
     * 
     * @param text 暗号化する文字列
     * @param keyBytes 暗号キー
     * 
     * @return 復号化された文字列
     **/
    public static String encrypt(String text, byte[] keyBytes)
    {
        // AES256を使用
        Security.setProperty( "crypto.policy", "unlimited" );

        try
        {
            // ivを生成
            byte[] ivBytes = new byte[16];
            SecureRandom random = SecureRandom.getInstance( "SHA1PRNG" );
            random.nextBytes( ivBytes );
            IvParameterSpec ivspec = new IvParameterSpec( ivBytes );

            // AES256/CBC/PKCS5パディングで暗号化
            SecretKey secret = new SecretKeySpec( keyBytes, "AES" );
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
            cipher.init( Cipher.ENCRYPT_MODE, secret, ivspec );
            byte[] encryptedBytes = cipher.doFinal( text.getBytes() );

            // ivデータと暗号化データを結合
            int length = ivBytes.length + encryptedBytes.length;
            ByteBuffer byteBuffer = ByteBuffer.allocate( length );
            byteBuffer.put( ivBytes );
            byteBuffer.put( encryptedBytes );
            byteBuffer.rewind();
            byte[] resultBytes = new byte[length];
            byteBuffer.get( resultBytes );

            // base64エンコード
            String result = new String( Base64.encodeBase64( resultBytes ) );

            return result;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * base64変換処理
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static String encodeBase64(byte[] data) throws Exception
    {

        ByteArrayOutputStream forEncode = new ByteArrayOutputStream();

        OutputStream toBase64 = MimeUtility.encode( forEncode, "base64" );
        toBase64.write( data );
        toBase64.close();

        return forEncode.toString( "iso-8859-1" );
    }

    /**
     * 復号処理(AES/CBC/PKCS5Padding方式で復号化)
     * 
     * @param key 暗号キー
     * @param ivBytes 暗号ベクター
     * @param encByteStr 暗号化文字列
     * @return 復号された文字列
     * @see 暗号化・復号:Java
     **/
    public static String decodeString(byte[] key, byte[] ivBytes, String encByteStr)
    {
        String strOut = "";
        byte[] decodeWord = null;
        byte[] encByteByte = null;

        Key skey = new SecretKeySpec( key, "AES" );

        try
        {
            //
            encByteByte = decodeBase64( encByteStr );

            // 復号を行うアルゴリズム、モード、パディング方式を指定
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
            // 初期化
            cipher.init( Cipher.DECRYPT_MODE, skey, new IvParameterSpec( ivBytes ) );
            // 復号
            decodeWord = cipher.doFinal( encByteByte );
            if ( decodeWord != null )
            {
                // 文字列変換
                strOut = new String( decodeWord );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DecodeData.decodeString()] Exception:" + e.toString() );
            return(strOut);
        }
        return(strOut);
    }

    /*****
     * base64変換処理
     * 
     * @param strBase64
     * @return
     * @throws Exception
     */
    public static byte[] decodeBase64(String strBase64) throws Exception
    {

        InputStream inputBase64 = MimeUtility.decode( new ByteArrayInputStream( strBase64.getBytes() ), "base64" );

        byte[] buf = new byte[1024];
        ByteArrayOutputStream toByteArray = new ByteArrayOutputStream();

        for( int len = -1 ; (len = inputBase64.read( buf )) != -1 ; )
        {
            toByteArray.write( buf, 0, len );
        }
        return toByteArray.toByteArray();
    }
}
