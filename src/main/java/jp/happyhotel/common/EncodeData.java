package jp.happyhotel.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.MimeUtility;

import org.apache.commons.codec.binary.Base64;

public class EncodeData
{

    /**
     * �Í�������(AES/CBC/PKCS5Padding�����ňÍ���)
     * 
     * @param key �Í��L�[
     * @param ivBytes �Í��x�N�^�[
     * @param encByteStr �Í������镶����
     * @return ���������ꂽ������
     **/
    public static String encodeString(byte[] key, byte[] ivBytes, String encByteStr)
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

    /**
     * �Í�������(AES/CBC/PKCS5Padding�����ňÍ���)
     * 
     * @param keyStr �Í��L�[
     * @param text �Í������镶����
     * @return ���������ꂽ������
     **/
    public static String encodeString(String keyStr, String text)
    {
        // �Í����L�[���n�b�V����
        byte[] keyBytes = hash( keyStr, 32 );

        // �Í���
        String result = encrypt( text, keyBytes );
        return result;
    }

    /**
     * �Í�������(AES/CBC/PKCS5Padding�����ňÍ���)
     * 
     * @param text �Í������镶����
     * @param keyBytes �Í��L�[
     * 
     * @return ���������ꂽ������
     **/
    public static String encrypt(String text, byte[] keyBytes)
    {
        // AES256���g�p
        Security.setProperty( "crypto.policy", "unlimited" );

        try
        {
            // iv�𐶐�
            byte[] ivBytes = new byte[16];
            SecureRandom random = SecureRandom.getInstance( "SHA1PRNG" );
            random.nextBytes( ivBytes );
            IvParameterSpec ivspec = new IvParameterSpec( ivBytes );

            // AES256/CBC/PKCS5�p�f�B���O�ňÍ���
            SecretKey secret = new SecretKeySpec( keyBytes, "AES" );
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
            cipher.init( Cipher.ENCRYPT_MODE, secret, ivspec );
            byte[] encryptedBytes = cipher.doFinal( text.getBytes() );

            // iv�f�[�^�ƈÍ����f�[�^������
            int length = ivBytes.length + encryptedBytes.length;
            ByteBuffer byteBuffer = ByteBuffer.allocate( length );
            byteBuffer.put( ivBytes );
            byteBuffer.put( encryptedBytes );
            byteBuffer.rewind();
            byte[] resultBytes = new byte[length];
            byteBuffer.get( resultBytes );

            // base64�G���R�[�h
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
     * SHA-256�Ńn�b�V����
     * 
     * @param value
     * @param length
     * @return byte[]
     * @throws Exception
     */
    private static byte[] hash(String value, int length)
    {
        byte[] hash = new byte[length];
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
            hash = digest.digest( value.getBytes( "UTF-8" ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return Arrays.copyOf( hash, 32 );
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
}