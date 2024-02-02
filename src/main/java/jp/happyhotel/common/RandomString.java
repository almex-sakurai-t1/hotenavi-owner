/*
 * @(#)RandomString.java 1.00
 * 2009/07/15 Copyright (C) ALMEX Inc. 2009
 * �����_��������擾�N���X
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserLogin;

/**
 * �����_���ȕ�������擾����N���X
 * 
 * @author N.ide
 * @version 1.00 2009/07/15
 */
public class RandomString implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -7816851658604741981L;

    /**
     * �����_���ȕ������Ԃ�(���p�召�p������ѐ����ō\��)
     * 
     * @param digit �����_��������̌���
     * @return ��������("":���s)
     */

    static public String getOwnerPassword()
    {

        int digit = 10;
        String strRandom = "";
        String chars = "2345678abcdefghijkmnprstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
        String num_chars = "23456789";
        String alpha_chars = "abcdefghijkmnprstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";

        int nRandom;
        int i = 0;
        int i_num = 0;
        int i_char = 0;

        Random rand;
        rand = new Random();
        i_num = rand.nextInt( 10 );
        i_char = (i_num + rand.nextInt( 9 ) + 1) % 10;

        if ( digit <= 0 )
        {
            return("");
        }
        else
        {
            for( i = 0 ; i < digit ; i++ )
            {
                rand = new Random();
                if ( i == i_num )
                {
                    nRandom = rand.nextInt( num_chars.length() );
                    strRandom = strRandom + num_chars.substring( nRandom, nRandom + 1 );
                }
                if ( i == i_char )
                {
                    nRandom = rand.nextInt( alpha_chars.length() );
                    strRandom = strRandom + alpha_chars.substring( nRandom, nRandom + 1 );
                }
                else
                {
                    nRandom = rand.nextInt( chars.length() );
                    strRandom = strRandom + chars.substring( nRandom, nRandom + 1 );
                }
            }
            return(strRandom);
        }
    }

    /**
     * �����_���ȕ������Ԃ�(���p�召�p������ѐ����ō\��)
     * 
     * @param digit �����_��������̌���
     * @return ��������("":���s)
     */
    static public String getRandomString(int digit)
    {
        String strRandom = "";
        String chars = "2345678abcdefghijkmnprstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
        int nRandom;
        int i;
        Random rand;

        if ( digit <= 0 )
        {
            return("");
        }
        else
        {
            for( i = 0 ; i < digit ; i++ )
            {
                rand = new Random();
                nRandom = rand.nextInt( chars.length() );
                strRandom = strRandom + chars.substring( nRandom, nRandom + 1 );
            }
            return(strRandom);
        }
    }

    /**
     * �����_���ȕ������Ԃ�(�����ō\��)
     * 
     * @param digit �����_��������̌���
     * @return ��������("":���s)
     */
    static public String getRandomNumber(int digit)
    {
        String strRandom = "";
        String chars = "1234567890";
        int nRandom;
        int i;
        Random rand;

        if ( digit <= 0 )
        {
            return("");
        }
        else
        {
            for( i = 0 ; i < digit ; i++ )
            {
                rand = new Random();
                nRandom = rand.nextInt( chars.length() );
                strRandom = strRandom + chars.substring( nRandom, nRandom + 1 );
            }
            return(strRandom);
        }
    }

    /**
     * �p�����܂ޖ��o�^�̉��ID��Ԃ�
     * 
     * @return ��������("":���s)
     */
    public static String getUserId()
    {
        final int USERID_NUM = 10;
        String userId;
        UserBasicInfo ubi;

        ubi = new UserBasicInfo();

        // ���̂Ȃ����ID���������ꂽ��break
        while( true )
        {
            // userId = RandomString.getRandomString( USERID_NUM );
            userId = RandomString.getRandomString( USERID_NUM );
            // ���[�U�[ID���l�`�F�b�N(�����ȊO���܂܂�Ă����OK)
            if ( CheckString.numCheck( userId ) == false )
            {
                // ����ID���o�^����Ă��Ȃ����OK
                if ( ubi.getUserBasicByAll( userId ) == false )
                {
                    break;
                }
            }
        }
        return(userId);
    }

    /***
     * �p�����܂ޖ��o�^�̉��ID��Ԃ�
     * 
     * @param strPrefix
     * @return ��������("":���s)
     */
    public static String getUserId(String strPrefix)
    {
        final int USERID_NUM = 10;
        String userId;
        UserBasicInfo ubi;
        int nLength = 0;

        ubi = new UserBasicInfo();
        if ( strPrefix == null )
        {
            strPrefix = "";
        }
        // �ړ���̌��������A�����_�����������̌��������炷�B
        nLength = USERID_NUM - strPrefix.length();

        // ���̂Ȃ����ID���������ꂽ��break
        while( true )
        {
            userId = strPrefix + RandomString.getRandomString( nLength );
            // ���[�U�[ID���l�`�F�b�N(�����ȊO���܂܂�Ă����OK)
            if ( CheckString.numCheck( userId ) == false )
            {
                // ����ID���o�^����Ă��Ȃ����OK
                if ( ubi.getUserBasicByAll( userId ) == false )
                {
                    break;
                }
            }
        }
        return(userId);
    }

    /**
     * �p�����܂ޖ��o�^�̃Z�L�����e�B�L�[��Ԃ�
     * 
     * @return ��������("":���s)
     */
    public static String getSecurityKey()
    {
        final int KEY_LENGTH = 48;
        String securityKey;
        UserLogin ul;

        ul = new UserLogin();
        // ���̂Ȃ����ID���������ꂽ��break
        while( true )
        {
            securityKey = DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) + RandomString.getRandomString( KEY_LENGTH - 14 );
            // ����ID���o�^����Ă��Ȃ����OK
            if ( ul.isSecurityKey( securityKey ) == true )
            {
                break;
            }
        }
        return(securityKey);
    }

    /**
     * �����_���ȕ������Ԃ�(���p�召�p������ѐ����ō\��)
     * 
     * @param digit �����_��������̌���
     * @return ��������("":���s)
     * @see ������1,0�p������l,I,O�͎g�p���Ȃ�
     */
    static public String getLicenceKey(int digit)
    {
        String strRandom = "";
        String chars = "2345678abcdefghijkmnoprstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
        int nRandom;
        int i;
        Random rand;

        if ( digit <= 0 )
        {
            return("");
        }
        else
        {
            for( i = 0 ; i < digit ; i++ )
            {
                rand = new Random();
                nRandom = rand.nextInt( chars.length() );
                strRandom = strRandom + chars.substring( nRandom, nRandom + 1 );
            }
            return(strRandom);
        }
    }

    /**
     * csrf�p�����^�C��token�𔭍s����
     * 
     * @param length �����_��������̌���
     * @return token
     */
    public static String generateRandomString(int length)
    {
        Random random = new SecureRandom();
        if ( length < 1 )
            throw new IllegalArgumentException();

        final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        final String NUMBER = "0123456789";
        final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;

        StringBuilder sb = new StringBuilder( length );
        for( int i = 0 ; i < length ; i++ )
        {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt( DATA_FOR_RANDOM_STRING.length() );
            char rndChar = DATA_FOR_RANDOM_STRING.charAt( rndCharAt );
            sb.append( rndChar );
        }

        return sb.toString();
    }
}
