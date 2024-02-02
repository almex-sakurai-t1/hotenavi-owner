package jp.happyhotel.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.MimeUtility;

public class EncodeString
{

    private final static String CONF_PATH   = "/etc/hotenavi/aes.conf";
    private final static String KEY_KEY     = "key";
    private final static String IVBYTES_KEY = "ivBytes";
    // キー
    private static byte[]       key         = null;
    // 暗号ベクター（Initialization Vector：初期化ベクトル）
    private static byte[]       ivBytes     = null;

    static
    {
        try
        {
            Properties prop = new Properties();
            FileInputStream propfile = new FileInputStream( CONF_PATH );
            prop = new Properties();
            // プロパティファイルを読み込む
            prop.load( propfile );

            String confKey = prop.getProperty( KEY_KEY );
            String confIbytes = prop.getProperty( IVBYTES_KEY );
            key = confKey.getBytes();
            ivBytes = confIbytes.getBytes();
            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "EncodeString Static Block Error=" + e.toString() );
        }
    }

    /**
     * 暗号化処理(AES/CBC/PKCS5Padding方式で暗号化)
     * 
     * @param key 暗号キー
     * @param ivBytes 暗号ベクター
     * @param encByteStr 暗号化する文字列
     * @return 復号化された文字列
     **/
    public static String encode(String encByteStr)
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
     * 復号化処理(AES/CBC/PKCS5Padding方式で復号化)
     * 
     * @param key 暗号キー
     * @param ivBytes 暗号ベクター
     * @param encByteStr 暗号化文字列
     * @return 復号化された文字列
     * @see 暗号化・復号:Java
     **/
    public static String decode(String encByteStr)
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
