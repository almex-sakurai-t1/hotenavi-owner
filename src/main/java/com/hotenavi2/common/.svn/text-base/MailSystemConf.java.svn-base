package com.hotenavi2.common;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * 
 * 
 * @author S.Sakurai
 * @version 1.00 2023/10/10
 */
public class MailSystemConf implements Serializable
{
    private final static String CONF_PATH        = "/etc/hotenavi/mailSystem.conf";
    private final static String KEY_KEY          = "key";
    private final static String IVBYTES_KEY      = "ivBytes";
    private final static String API_KEY          = "apiKey";
    private final static String WEB_API_ENDPOINT = "apiEndpoint";
    // キー
    private static byte[]       key              = null;
    // 暗号ベクター（Initialization Vector：初期化ベクトル）
    private static byte[]       ivBytes          = null;

    private static String       api_key          = null;
    private static String       api_endpoint     = null;
    private static final long   serialVersionUID = -3924291538252750667L;

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
            api_endpoint = prop.getProperty( WEB_API_ENDPOINT );
            api_key = prop.getProperty( API_KEY );
            key = confKey.getBytes();
            ivBytes = confIbytes.getBytes();
            prop = null;
            propfile.close();
        }
        catch ( Exception e )
        {
            Logging.error( "mailAddressEncrypt Static Block Error=" + e.toString() );
        }
    }

    /**
     * @param:入力値
     * 
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )による暗号化文字列
     * @throws Exception
     */

    public static String encrypt(String inputStr) //
    {
        // 暗号化

        String encrypt = "";
        try
        {
            encrypt = EncodeData.encodeString( key, ivBytes, inputStr );
        }
        catch ( Exception e )
        {
            Logging.error( "[MailSystem.encrypt] Exception:" + e.toString() );
        }
        return(encrypt);
    }

    /**
     * @param:入力値（暗号化文字列）
     * 
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )による復号文字列
     * @throws Exception
     */
    public static String decrypt(String encrypt) //
    {
        // 復号
        String decrypt = "";
        try
        {
            decrypt = EncodeData.decodeString( key, ivBytes, encrypt );
        }
        catch ( Exception e )
        {
            Logging.error( "[MailSystem.decrypt] Exception:" + e.toString() );
        }
        return(decrypt);
    }

    public static String getApiEndpoint()
    {
        return(api_endpoint);
    }

    public static String getApiKey()
    {
        return(api_key);
    }
}
