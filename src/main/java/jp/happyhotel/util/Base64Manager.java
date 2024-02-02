package jp.happyhotel.util;

import sun.nio.cs.StandardCharsets;

/**
 * base64文字列encode/decode用クラス
 * 
 * @author paku-k1
 */
public class Base64Manager
{
    /**
     * コンストラクタ
     */
    private Base64Manager()
    {
    }

    /**
     * Base64文字列へエンコード
     * 
     * @param src Base64でエンコードする文字列
     * @return エンコードされた文字列
     */
    @SuppressWarnings("restriction")
    public static String encode(String src)
    {
        byte[] bytes = src.getBytes( StandardCharsets.UTF_8 );
        return Base64.getEncoder().encodeToString( bytes );
    }

    /**
     * Base64文字列のデコード
     * 
     * @param src Base64でエンコードされた文字列
     * @return デコードされた文字列
     * @throws IllegalArgumentException {@code src} が有効なBase64スキームになっていない場合
     */
    public static String decode(String src)
    {
        byte[] bytes = Base64.getDecoder().decode( src );
        return new String( bytes, StandardCharsets.UTF_8 );
    }
}
