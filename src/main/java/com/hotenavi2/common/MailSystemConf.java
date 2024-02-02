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
    // �L�[
    private static byte[]       key              = null;
    // �Í��x�N�^�[�iInitialization Vector�F�������x�N�g���j
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
            // �v���p�e�B�t�@�C����ǂݍ���
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
     * @param:���͒l
     * 
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )�ɂ��Í���������
     * @throws Exception
     */

    public static String encrypt(String inputStr) //
    {
        // �Í���

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
     * @param:���͒l�i�Í���������j
     * 
     * @return String EncodeData.encodeString( key, ivBytes, encrypt )�ɂ�镜��������
     * @throws Exception
     */
    public static String decrypt(String encrypt) //
    {
        // ����
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
