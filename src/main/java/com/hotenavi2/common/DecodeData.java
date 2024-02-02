package com.hotenavi2.common;

import java.io.Serializable;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecodeData implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 7242408736249276255L;

    /**
     * ����������(AES/CBC/PKCS5Padding�����ŕ�����)
     * 
     * @param encByteStr �Í���������
     * @return ��������(true,false)
     **/
    public static String decode(String encByteStr)
    {
        final String KEY = "h o t e n a v i ";
        final String IVKEY = "a l m e x 1 2 3 ";
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

        // �Í��L�[
        key = KEY.getBytes();

        // �Í��x�N�^�[�iInitialization Vector�F�������x�N�g���j
        ivBytes = IVKEY.getBytes();

        // �Í���������̕������̔��������o�C�g�z���p��
        encByteByte = new byte[encByteStr.length() / 2];
        // �Í�����������o�C�g�ɕϊ�
        for( i = 0 ; i < encByteStr.length() ; i += 2 )
        {
            nHead = ConvertHexToInt.HexToInt( String.valueOf( encByteStr.charAt( i ) ) );
            nTail = ConvertHexToInt.HexToInt( String.valueOf( encByteStr.charAt( i + 1 ) ) );

            encByteByte[i / 2] = (byte)(nHead * 16 + nTail);
        }

        Key skey = new SecretKeySpec( key, "AES" );

        // ������
        decodeWord = decodeWithVector( encByteByte, ivBytes, skey );

        if ( decodeWord != null )
        {
            // ������ϊ�
            strOut = new String( decodeWord );

        }

        return(strOut);
    }

    /**
     * ����������(AES/CBC/PKCS5Padding�����ŕ�����)
     * 
     * @param key �Í��L�[
     * @param ivBytes �Í��x�N�^�[
     * @param encByteStr �Í���������
     * @return ���������ꂽ������
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

        // �Í���������̕������̔��������o�C�g�z���p��
        encByteByte = new byte[encByteStr.length() / 2];
        // �Í�����������o�C�g�ɕϊ�
        for( i = 0 ; i < encByteStr.length() ; i += 2 )
        {
            nHead = ConvertHexToInt.HexToInt( String.valueOf( encByteStr.charAt( i ) ) );
            nTail = ConvertHexToInt.HexToInt( String.valueOf( encByteStr.charAt( i + 1 ) ) );

            encByteByte[i / 2] = (byte)(nHead * 16 + nTail);
        }

        Key skey = new SecretKeySpec( key, "AES" );

        // ������
        decodeWord = decodeWithVector( encByteByte, ivBytes, skey );

        if ( decodeWord != null )
        {
            // ������ϊ�
            strOut = new String( decodeWord );

        }

        return(strOut);
    }

    /***
     * ��������
     * 
     * @param src ��������Í�������
     * @param ivBytes �Í��x�N�^
     * @param skey �閧�L�[
     * @return ����������
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
            Logging.error( "[ActionHapiTouch.decodeWithVector()] Exception:" + e.toString() );
            return(null);
        }
    }

}
