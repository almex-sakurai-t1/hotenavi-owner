/*
 * @(#)CheckString.java 1.00 2007/07/19 Copyright (C) ALMEX Inc. 2007 文字列チェック汎用クラス
 */

package com.hotenavi2.common;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 文字列のチェックを行うメソッド群です。
 * </p>
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/19
 */
public class CheckString implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -4856108172406548350L;

    /**
     * 半角数字チェック処理
     * 
     * @param orgNum チェック対象文字列
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean numCheck(String orgNum)
    {
        char cutData;
        if ( orgNum != null && orgNum.trim().length() > 0 )
        {
            for( int i = 0 ; i < orgNum.trim().length() ; i++ )
            {
                cutData = orgNum.charAt( i );
                if ( (cutData < '0' || cutData > '9') && cutData != '-' )
                {
                    return(false);
                }
            }
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ホテナビIDチェック処理
     * 
     * @param hotenaviId ホテナビID
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean hotenaviIdCheck(String hotenaviId)
    {
        boolean check = true;

        check = numAlphaCheck( hotenaviId ); // 英数と_-以外は異常値

        if ( check )
        {
            if ( hotenaviId.length() > 10 ) // 10桁を超える場合は異常値
            {
                check = false;
            }
        }

        return(check);
    }

    /**
     * ログインIDチェック処理
     * 
     * @param hotenaviId ホテナビID
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean loginIdCheck(String loginId)
    {
        boolean check = true;

        check = loginId.matches( "[0-9a-zA-Z.@,_-]+" );

        return(check);
    }

    /**
     * パスワードチェック処理
     * 
     * @param hotenaviId ホテナビID
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean passwordCheck(String password)
    {
        boolean check = true;

        check = password.matches( "[0-9a-zA-Z!.@,_-]+" );
        if ( check )
        {
            if ( password.length() > 8 ) // 8桁を超える場合は異常値
            {
                check = false;
            }
        }
        if ( check )
        {
            if ( password.length() < 4 ) // 4桁未満は異常値
            {
                check = false;
            }
        }

        return(check);
    }

    /**
     * 半角英字チェック処理
     * 
     * @param orgAlphabet チェック対象文字列
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean alphabetCheck(String orgAlphabet)
    {
        int i;
        char cutData;

        for( i = 0 ; i < orgAlphabet.length() ; i++ )
        {
            cutData = orgAlphabet.charAt( i );
            if ( (cutData < 'A' || cutData > 'Z') && (cutData < 'a' || cutData > 'z') )
            {
                return(false);
            }
        }

        return(true);
    }

    /**
     * 半角英数字チェック処理
     * 
     * @param orgData チェック対象文字列
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean numAlphaCheck(String orgData)
    {
        return(orgData.matches( "[0-9a-zA-Z._-]+" ));
    }

    /**
     * 全角ひらがなチェック処理
     * 
     * @param orgHiragana チェック対象文字列
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean hiraganaCheck(String orgHiragana)
    {
        int i;
        char cutData;

        for( i = 0 ; i < orgHiragana.length() ; i++ )
        {
            cutData = orgHiragana.charAt( i );
            if ( cutData < 'ぁ' || cutData > 'ん' )
            {
                return(false);
            }
        }

        return(true);
    }

    /**
     * 全角カタカナチェック処理
     * 
     * @param orgKatakana チェック対象文字列
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean katakanaCheck(String orgKatakana)
    {
        int i;
        char cutData;

        for( i = 0 ; i < orgKatakana.length() ; i++ )
        {
            cutData = orgKatakana.charAt( i );
            if ( (cutData < 'ァ' || cutData > 'ヶ') && cutData != 'ー' )
            {
                return(false);
            }
        }

        return(true);
    }

    /**
     * 空白チェック処理
     * 空白（半角・全角）のみの場合true
     * 
     * @param orgOnlySpace チェック対象文字列
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean onlySpaceCheck(String orgOnlySpace)
    {
        int i;
        char cutData;
        boolean ret = true;

        for( i = 0 ; i < orgOnlySpace.length() ; i++ )
        {
            cutData = orgOnlySpace.charAt( i );
            if ( cutData != '　' && cutData != ' ' )
            {
                ret = false;
                break;
            }
        }

        return(ret);
    }

    /**
     * nullチェック
     * 
     * @param objString 文字列
     * @return string(nullの場合、""に置き換える)
     */
    public static String checkStringForNull(String objString)
    {
        if ( objString == null )
        {
            objString = "";
        }
        return objString.trim();
    }

    /**
     * パラメータのnull、空白チェック（null、空白の場合false）
     * 
     * @param objString
     * @return 処理結果(true:正常,false:異常)
     */

    public static boolean isValidParameter(String objString)
    {
        if ( objString == null || objString.trim().length() <= 0 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * null、空白チェック（null、空白の場合false）
     * 
     * @param objString
     * @return 処理結果(true:正常,false:異常)
     */

    public static boolean isvalidString(String objString)
    {
        if ( objString == null || objString.trim().length() <= 0 || objString.compareTo( "null" ) == 0 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * メールアドレスチェックメソッド
     * 
     * @param mailaddr
     * @return 処理結果（true:正常、false:異常）
     */
    public static boolean mailaddrCheck(String mailaddr)
    {
        if ( mailaddr == null )
        {
            return false;
        }
        else
        {
            /*
             * PCでも、'..'や@の前の'.'をOKとして扱う
             * 2016/10/06（#15020対応）
             * 携帯用のメアドとして（vodafone.ne.jpで）'/ ? +'が含まれたものが登録されていたので、それらの記号を含むメアドにも対応
             */
            if ( mailaddr.matches( "^[\\w\\d\\?\\+/._-]+\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$" ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * 漢字+ひらがなチェック
     * 
     * @param str
     * @return 処理結果（true:正常、false:異常）
     */
    public static boolean kanjiCheck(String str)
    {
        if ( str == null )
        {
            return false;
        }

        Pattern p = Pattern.compile( "^[一-龠ぁ-んー]*$" );
        Matcher m = p.matcher( str );
        if ( m.find() )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
