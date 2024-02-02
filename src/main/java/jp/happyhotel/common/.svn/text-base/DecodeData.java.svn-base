package jp.happyhotel.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.MimeUtility;

public class DecodeData
{

    /**
     * 復号化処理(AES/CBC/PKCS5Padding方式で復号化)
     * 
     * @param encByteStr 暗号化文字列
     * @return 処理結果(true,false)
     * @see ホテナビ用（暗号化:C#、復号:Java）
     **/
    public static String decode(String encByteStr)
    {
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
        key = "h o t e n a v i ".getBytes();

        // 暗号ベクター（Initialization Vector：初期化ベクトル）
        ivBytes = "a l m e x 1 2 3 ".getBytes();

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
     * @see ハピホテタッチ用（暗号化:C#、復号:Java）
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
     * 復号処理(AES/CBC/PKCS5Padding方式で復号化)
     * 
     * @param src 復号する暗号文字列
     * @param ivBytes 暗号ベクタ
     * @param skey 秘密キー
     * @return 復号文字列
     * @see ハピホテタッチ用
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
            Logging.error( "[DecodeData.decodeWithVector()] Exception:" + e.toString() );
            return(null);
        }
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
    public static String decodeString(byte[] key, byte[] ivBytes, String encByteStr)
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
