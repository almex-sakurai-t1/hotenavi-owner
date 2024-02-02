/*
 * @(#)RandomString.java 1.00
 * 2009/07/15 Copyright (C) ALMEX Inc. 2009
 * ランダム文字列取得クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.user.UserLogin;

/**
 * ランダムな文字列を取得するクラス
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
     * ランダムな文字列を返す(半角大小英字および数字で構成)
     * 
     * @param digit ランダム文字列の桁数
     * @return 処理結果("":失敗)
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
     * ランダムな文字列を返す(半角大小英字および数字で構成)
     * 
     * @param digit ランダム文字列の桁数
     * @return 処理結果("":失敗)
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
     * ランダムな文字列を返す(数字で構成)
     * 
     * @param digit ランダム文字列の桁数
     * @return 処理結果("":失敗)
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
     * 英字を含む未登録の会員IDを返す
     * 
     * @return 処理結果("":失敗)
     */
    public static String getUserId()
    {
        final int USERID_NUM = 10;
        String userId;
        UserBasicInfo ubi;

        ubi = new UserBasicInfo();

        // 問題のない会員IDが生成されたらbreak
        while( true )
        {
            // userId = RandomString.getRandomString( USERID_NUM );
            userId = RandomString.getRandomString( USERID_NUM );
            // ユーザーID数値チェック(数字以外が含まれていればOK)
            if ( CheckString.numCheck( userId ) == false )
            {
                // 同じIDが登録されていなければOK
                if ( ubi.getUserBasicByAll( userId ) == false )
                {
                    break;
                }
            }
        }
        return(userId);
    }

    /***
     * 英字を含む未登録の会員IDを返す
     * 
     * @param strPrefix
     * @return 処理結果("":失敗)
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
        // 接頭語の桁数だけ、ランダム生成文字の桁数を減らす。
        nLength = USERID_NUM - strPrefix.length();

        // 問題のない会員IDが生成されたらbreak
        while( true )
        {
            userId = strPrefix + RandomString.getRandomString( nLength );
            // ユーザーID数値チェック(数字以外が含まれていればOK)
            if ( CheckString.numCheck( userId ) == false )
            {
                // 同じIDが登録されていなければOK
                if ( ubi.getUserBasicByAll( userId ) == false )
                {
                    break;
                }
            }
        }
        return(userId);
    }

    /**
     * 英字を含む未登録のセキュリティキーを返す
     * 
     * @return 処理結果("":失敗)
     */
    public static String getSecurityKey()
    {
        final int KEY_LENGTH = 48;
        String securityKey;
        UserLogin ul;

        ul = new UserLogin();
        // 問題のない会員IDが生成されたらbreak
        while( true )
        {
            securityKey = DateEdit.getDate( 2 ) + DateEdit.getTime( 1 ) + RandomString.getRandomString( KEY_LENGTH - 14 );
            // 同じIDが登録されていなければOK
            if ( ul.isSecurityKey( securityKey ) == true )
            {
                break;
            }
        }
        return(securityKey);
    }

    /**
     * ランダムな文字列を返す(半角大小英字および数字で構成)
     * 
     * @param digit ランダム文字列の桁数
     * @return 処理結果("":失敗)
     * @see 数字の1,0英数字のl,I,Oは使用しない
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
     * csrf用ワンタイムtokenを発行する
     * 
     * @param length ランダム文字列の桁数
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
