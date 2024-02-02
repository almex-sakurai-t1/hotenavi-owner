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
    // �L�[
    private static byte[]       key         = null;
    // �Í��x�N�^�[�iInitialization Vector�F�������x�N�g���j
    private static byte[]       ivBytes     = null;

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
     * �Í�������(AES/CBC/PKCS5Padding�����ňÍ���)
     * 
     * @param key �Í��L�[
     * @param ivBytes �Í��x�N�^�[
     * @param encByteStr �Í������镶����
     * @return ���������ꂽ������
     **/
    public static String encode(String encByteStr)
    {
        String strOut = "";
        byte[] decodeWord = null;
        byte[] encByteByte = null;

        encByteByte = encByteStr.getBytes();
        // �閧���̍\�z
        Key skey = new SecretKeySpec( key, "AES" );

        try
        {
            // �������s���A���S���Y���A���[�h�A�p�f�B���O�������w��
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
            // ������
            cipher.init( Cipher.ENCRYPT_MODE, skey, new IvParameterSpec( ivBytes ) );
            // �Í���
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
     * base64�ϊ�����
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
     * ����������(AES/CBC/PKCS5Padding�����ŕ�����)
     * 
     * @param key �Í��L�[
     * @param ivBytes �Í��x�N�^�[
     * @param encByteStr �Í���������
     * @return ���������ꂽ������
     * @see �Í����E����:Java
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

            // �������s���A���S���Y���A���[�h�A�p�f�B���O�������w��
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
            // ������
            cipher.init( Cipher.DECRYPT_MODE, skey, new IvParameterSpec( ivBytes ) );
            // ����
            decodeWord = cipher.doFinal( encByteByte );
            if ( decodeWord != null )
            {
                // ������ϊ�
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
     * base64�ϊ�����
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
