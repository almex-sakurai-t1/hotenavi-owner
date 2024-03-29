package jp.happyhotel.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5変換に関する機能を提供するクラス
 * 
 * @author mitsuhashi-k1
 */
public class MD5
{
    /**
     * コンストラクタ
     */
    private MD5()
    {
    }

    /**
     * MD5変換（16進数）<br>
     * <br>
     * 指定した文字列をMD5値に変換します。<br>
     * 文字列がnullだった場合NullPointerExceptionが発生します。<br>
     * 
     * @param source MD5値に変換する文字列
     * @return MD5値
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
            messageDigest = MessageDigest.getInstance( "MD5" );
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new UnsupportedOperationException( e );
        }

        byte[] byteArray = source.getBytes();
        messageDigest.update( byteArray );
        byteArray = messageDigest.digest();

        StringBuilder strBuilder = new StringBuilder();
        for( int i = 0 ; i < byteArray.length ; i++ )
        {
            int val = byteArray[i] & 0xff;

            if ( val < 16 )
            {
                strBuilder.append( "0" );
            }

            // radixがNの場合、これらの文字の先頭からN個が、示した順に基数Nの桁として使用されます。
            // すなわち、16進数（基数は16）に使われる桁の数字は0123456789abcdefとなります。
            strBuilder.append( Integer.toString( val, 16 ) );
        }

        String md5Hash = strBuilder.toString();

        return md5Hash;
    }

    /**
     * MD5変換（16進数）<br>
     * <br>
     * 指定した文字列をMD5値に変換します。<br>
     * 文字列がnullだった場合NullPointerExceptionが発生します。<br>
     * 
     * @param source MD5値に変換する文字列
     * @param count 変換する回数
     * @return MD5値
     * @throws NullPointerException 文字列がnullだった場合。
     */

    public static String convert(String source, int count)
    {
        if ( source == null )
        {
            throw new NullPointerException();
        }

        String md5Hash = source;

        for( int i = 0 ; i < count ; i++ )
        {
            md5Hash = convert( md5Hash );
        }
        return md5Hash;
    }

}
