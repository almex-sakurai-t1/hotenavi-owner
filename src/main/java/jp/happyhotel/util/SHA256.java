package jp.happyhotel.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA-256変換に関する機能を提供するクラス
 * 
 * @author mitsuhashi-k1
 */
public class SHA256
{
    /**
     * コンストラクタ
     */
    private SHA256()
    {
    }

    /**
     * SHA-256変換<br>
     * <br>
     * 指定した文字列を SHA-256値に変換します。<br>
     * 文字列がnullだった場合NullPointerExceptionが発生します。<br>
     * 
     * @param source SHA-256値に変換する文字列
     * @return SHA-256値
     * @throws NullPointerException 文字列がnullだった場合。
     */
    public static String convert(String source)
    {
        if ( source == null )
        {
            throw new NullPointerException();
        }

        MessageDigest messageDigest;
        try
        {
            messageDigest = MessageDigest.getInstance( "SHA-256" );
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new UnsupportedOperationException( e );
        }

        byte[] cipher_byte;
        messageDigest.update( source.getBytes() );
        cipher_byte = messageDigest.digest();

        StringBuilder sb = new StringBuilder( 2 * cipher_byte.length );
        for( byte b : cipher_byte )
        {
            sb.append( String.format( "%02x", b & 0xff ) );
        }

        String sha256Hash = sb.toString();

        return sha256Hash;

    }
}
