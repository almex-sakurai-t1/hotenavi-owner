/*
 * @(#)CheckString.java 1.00 2007/07/19 Copyright (C) ALMEX Inc. 2007 文字列チェック汎用クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * ホテナビオーナーサイトログインIDチェック処理
     * 
     * @param loginId ホテナビオーナーサイト個人ユーザ名（ログインID）
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean loginIdCheck(String loginId)
    {
        boolean check = true;

        check = loginId.matches( "[0-9a-zA-Z.@,_-]+" );

        if ( loginId.length() > 10 ) // 10桁を超える場合は異常値
        {
            check = false;
        }
        if ( loginId.length() < 1 ) // 1桁未満は異常値
        {
            check = false;
        }

        return(check);
    }

    /**
     * ホテナビオーナーサイト名前チェック処理
     * 
     * @param name ホテナビ名前チェック）
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean nameCheck(String name)
    {
        boolean check = true;

        if ( name.length() > 10 ) // 10桁を超える場合は異常値
        {
            check = false;
        }
        if ( name.length() < 1 ) // 1桁未満は異常値
        {
            check = false;
        }

        return(check);
    }

    /**
     * ホテナビオーナーサイト携帯用パスワード　パスワードチェック処理
     * 
     * @param password ホテナビオーナーサイト携帯用パスワード
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
     * パスワードチェック用正規表現を取得
     * 
     * @return パスワードチェック用正規表現
     */
    public static String getPasswordRegex()
    {
        String regex_kigou = "{}\\[\\],.<>;:'\"?/|\\\\\\`~!@#$%^&*()_=+-";
        String pattern_kigou = "(?=.*[" + regex_kigou + "])";
        String regex_password = "^("
                + "(?=.*[0-9])(?=.*[a-z])" // 数字 英小文字
                + "|"
                + "(?=.*[0-9])(?=.*[A-Z])" // 数字 英大文字
                + "|"
                + "(?=.*[0-9])" + pattern_kigou // 数字 記号
                + "|"
                + "(?=.*[a-z])(?=.*[A-Z])" // 英小文字 英大文字
                + "|"
                + "(?=.*[a-z])" + pattern_kigou // 英小文字 記号
                + "|"
                + "(?=.*[A-Z])" + pattern_kigou // 英大文字 記号
                + ")"
                + "[a-zA-Z0-9" + regex_kigou + "]{" + Constants.MINIMUM_PASSWORD_LENGTH + ","
                + Constants.MAXIMUM_PASSWORD_LENGTH + "}$"; // 英大文字、英小文字、数字、記号（現状は）8文字以上
        return regex_password;
    }

    /**
     * パスワードチェック用正規表現を取得
     * 
     * @return パスワードチェック用正規表現
     */
    public static String getPasswordRegex3()
    {
        String regex_kigou = "{}\\[\\],.<>;:'\"?/|\\\\\\`~!@#$%^&*()_=+-";
        String pattern_kigou = "(?=.*[" + regex_kigou + "])";
        String regex_password = "^("
                + "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" // 数字 英小文字 英大文字
                + "|"
                + "(?=.*[0-9])(?=.*[a-z])" + pattern_kigou // 数字 英小文字 記号
                + "|"
                + "(?=.*[0-9])(?=.*[A-Z])" + pattern_kigou // 数字 英小文字 記号
                + "|"
                + "(?=.*[a-z])(?=.*[A-Z])" + pattern_kigou // 英小文字 英大文字
                + ")"
                + "[a-zA-Z0-9" + regex_kigou + "]{" + Constants.MINIMUM_PASSWORD_LENGTH + ","
                + Constants.MAXIMUM_PASSWORD_LENGTH + "}$"; // 英大文字、英小文字、数字、記号（現状は）8文字以上
        return regex_password;
    }

    /**
     * idパラメータチェック
     * 
     * @param HttpServletRequest
     * @return パラメータ値
     */
    public static String getParameterId(HttpServletRequest request)
    {

        String idstr = request.getParameter( "id" );
        int idLen = idstr.length();

        // 数値・桁数チェック
        if ( !numCheck( idstr ) || (idLen < 6 || idLen > 9) )
        {
            idstr = "0";
        }

        return(idstr);

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

    /*
     * @param 文字列
     * @return 処理結果（true:正常、false:異常）
     */
    public static boolean isXSSRuleCheck(String val)
    {
        Pattern pattern = Pattern.compile( "(?i)[\\s\"'`;/0-9=\\x0B\\x09\\x0C\\x3B\\x2C\\x28\\x3B]+on[a-zA-Z]+[\\s\\x0B\\x09\\x0C\\x3B\\x2C\\x28\\x3B]*?=" );
        Matcher matcher = pattern.matcher( val );

        String checkStr = "";
        while( matcher.find() )
        {
            checkStr = matcher.group();
            break;
        }
        return !checkStr.equals( "" );
    }

    /*
     * @param 文字列
     * @return 処理結果（true:正常、false:異常）
     */
    public static String isXSSRuleCheck(String val, String regex)
    {
        Pattern pattern = Pattern.compile( regex );
        Matcher matcher = pattern.matcher( val );
        String checkStr = "";
        while( matcher.find() )
        {
            checkStr = matcher.group();
            break;
        }
        return checkStr;
    }

    /**
     * 日付文字列が正しい日付かチェック
     * 
     * @param str ymd
     * @return true:正しい日付　false:不正な日付
     */
    public static boolean dateCheck(String ymd)
    {
        try
        {
            if ( ymd.equals( "99999999" ) )
                return true;

            // 日付チェック
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
            sdf.setLenient( false );
            sdf.parse( ymd );

            return true;

        }
        catch ( Exception e )
        {
            return false;
        }
    }

}
