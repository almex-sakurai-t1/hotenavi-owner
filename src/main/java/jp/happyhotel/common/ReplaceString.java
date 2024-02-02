/*
 * @(#)ReplaceString.java 2.00 2004/12/02 Copyright (C) ALMEX Inc. 2004 文字列置換クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.data.DataMasterUseragent;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 文字列の置換を行うクラス。
 * 
 * @author S.Shiiya
 * @version 2.00 2004/12/02
 */
public class ReplaceString implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -6441263467307426582L;

    /**
     * 文字列の置換を行う
     * 
     * @param input 処理の対象の文字列
     * @param pattern 置換前の文字列
     * @param replacement 置換後の文字列
     * @return 置換処理後の文字列
     */
    static public String replace(String input, String pattern, String replacement)
    {
        // 置換対象文字列が存在する場所を取得
        int index = input.indexOf( pattern );

        // 置換対象文字列が存在しなければ終了
        if ( index == -1 )
        {
            return(input);
        }

        // 処理を行うための StringBuffer
        StringBuffer buffer = new StringBuffer();

        buffer.append( input.substring( 0, index ) + replacement );

        if ( index + pattern.length() < input.length() )
        {
            // 残りの文字列を再帰的に置換
            String rest = input.substring( index + pattern.length(), input.length() );
            buffer.append( replace( rest, pattern, replacement ) );
        }

        return(buffer.toString());
    }

    /**
     * 文字列の置換を行う
     * 
     * @param input 処理の対象の文字列
     * @param pattern 置換前の文字列
     * @param replacement 置換後の文字列
     * @return 置換処理後の文字列
     */
    static public String replace(String input, int pattern, String replacement)
    {
        // 置換対象文字列が存在する場所を取得
        int index = input.indexOf( pattern );

        // 置換対象文字列が存在しなければ終了
        if ( index == -1 )
        {
            return(input);
        }

        // 処理を行うための StringBuffer
        StringBuffer buffer = new StringBuffer();

        buffer.append( input.substring( 0, index ) + replacement );

        if ( index + 1 < input.length() )
        {
            // 残りの文字列を再帰的に置換
            String rest = input.substring( index + 1, input.length() );
            buffer.append( replace( rest, pattern, replacement ) );
        }

        return(buffer.toString());
    }

    /**
     * SQLクエリー文字列の置換を行う ('→'') (\→\\)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String SQLEscape(String input)
    {
        boolean escapecode;

        if ( input != null )
        {
            input = replace( input, "'", "''" );
            input = replace( input, "\\", "\\\\" );

            escapecode = false;
            if ( input.length() > 0 )
            {
                // ２バイト目0x5cの文字を判別
                if ( input.lastIndexOf( "予" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "表" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "―" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "ソ" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "Ы" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "�\" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "噂" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "浬" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "欺" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "圭" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "構" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "蚕" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "十" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "申" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "曾" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "箪" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "貼" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "能" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "表" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "暴" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "予" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "禄" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "兔" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "喀" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "媾" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "彌" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "拿" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "杤" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "歃" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "濬" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "畚" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "秉" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "綵" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "臀" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "藹" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "觸" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "軆" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "鐔" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "饅" ) == input.length() - 1 )
                    escapecode = true;
                if ( input.lastIndexOf( "鷭" ) == input.length() - 1 )
                    escapecode = true;

                if ( escapecode != false )
                {
                    input = input + " ";
                }
            }
        }
        return(input);
    }

    /**
     * HTML文字列の置換を行う (&→&amp;) (<→&lt;) (>→&gt;) ("→&quot;)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     * @see "変換対象（&、<、>、&quot;、'、改行コード）"
     */
    static public String HTMLEscape(String input)
    {
        if ( input != null )
        {
            // サニタイズされた&を元に戻す
            input = replace( input, "&amp;amp;", "&" );
            // すでにHTMLエスケープされているものを元に戻す(二重エスケープを防ぐため）
            input = replace( input, "&amp;", "&" );
            input = replace( input, "&lt;", "<" );
            input = replace( input, "&gt;", ">" );
            input = replace( input, "&quot;", "\"" );
            input = replace( input, "&#39;", "'" );
            input = replace( input, "&#160;", " " );
            // HTMLエスケープ処理
            input = replace( input, "&", "&amp;" );
            input = replace( input, "<", "&lt;" );
            input = replace( input, ">", "&gt;" );
            input = replace( input, "\"", "&quot;" );
            input = replace( input, "'", "&#39;" );
            input = replace( input, " ", "&#160;" );
            input = replace( input, "\r\n", "<br>" );
            input = replace( input, "\r", "<br>" );
            input = replace( input, "\n", "<br>" );
        }

        return(input);
    }

    static public String getParameter(HttpServletRequest request, String input)
    {
        String inputString = request.getParameter( input );
        if ( inputString != null )
        {
            inputString = StringEscapeUtils.escapeHtml4( inputString );
        }
        return(inputString);
    }

    /**
     * HTML文字列の置換を行う (&→&amp;) (<→&lt;) (>→&gt;) ("→&quot;) Javascript内に文字列をセットする場合
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     * @see "変換対象（&、<、>、&quot;、'、改行コード）"
     */
    static public String HTMLScriptEscape(String input)
    {

        // サニタイズされた&を元に戻す
        input = replace( input, "&amp;amp;", "&" );
        // すでにHTMLエスケープされているものを元に戻す(二重エスケープを防ぐため）
        input = replace( input, "&amp;", "&" );
        input = replace( input, "&lt;", "<" );
        input = replace( input, "&gt;", ">" );
        input = replace( input, "&quot;", "\"" );
        input = replace( input, "&#39;", "'" );
        input = replace( input, "&#160;", " " );
        // HTMLエスケープ処理
        input = replace( input, "&", "&amp;" );
        input = replace( input, "<", "&lt;" );
        input = replace( input, ">", "&gt;" );
        input = replace( input, "\"", "\\&quot;" );
        input = replace( input, "'", "\\&#39;" );
        input = replace( input, " ", "&#160;" );
        input = replace( input, "\r\n", "<br>" );
        input = replace( input, "\r", "<br>" );
        input = replace( input, "\n", "<br>" );

        return(input);
    }

    /**
     * HTML文字列の置換を行う (&→&amp;) (<→&lt;) (>→&gt;) ("→&quot;)のみ
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     * @see "変換対象（&、<、>、&quot;）"
     */
    static public String HTMLTagEscape(String input)
    {

        // サニタイズされた&を元に戻す
        input = replace( input, "&amp;amp;", "&" );
        // すでにHTMLエスケープされているものを元に戻す(二重エスケープを防ぐため）
        input = replace( input, "&amp;", "&" );
        input = replace( input, "&lt;", "<" );
        input = replace( input, "&gt;", ">" );
        input = replace( input, "&quot;", "\"" );
        // HTMLエスケープ処理
        input = replace( input, "&", "&amp;" );
        input = replace( input, "<", "&lt;" );
        input = replace( input, ">", "&gt;" );
        input = replace( input, "\"", "&quot;" );

        return(input);
    }

    /**
     * HTML文字列をDB登録用に置換を行う (&amp;→&) (&lt;→<) (&gt;→>) (&quot;→")
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */

    static public String unEscape(String inputString)
    {
        if ( inputString != null )
        {
            inputString = StringEscapeUtils.unescapeHtml4( inputString );
        }
        return(inputString);
    }

    /**
     * HTML文字列をDB登録用に置換を行う (&amp;→&) (&lt;→<) (&gt;→>) (&quot;→")
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String DBEscape(String input)
    {

        // サニタイズされた文字列を元に戻す
        input = replace( input, "&amp;amp;", "&" );
        input = replace( input, "&#160;", " " );
        input = replace( input, "&#39;", "'" );
        input = replace( input, "&quot;", "\"" );
        input = replace( input, "&lt;", "<" );
        input = replace( input, "&gt;", ">" );
        input = replace( input, "&amp;", "&" );

        return(input);
    }

    /**
     * 携帯向けに文字列の置換を行う (全角カナ→半角カナ) (全角記号→半角記号) (全角数字→半角数字) (全角アルファベット→半角アルファベット)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceMobile(String input)
    {
        String strReturn;

        strReturn = "";
        strReturn = replaceNumAlphaHalf( input );
        strReturn = replaceKanaHalf( strReturn );
        return(strReturn);
    }

    /**
     * 検索ワードの重複登録防止のために文字列の置換を行う (半角カナ→全角カナ) (全角数字→半角数字) (全角アルファベット→半角アルファベット) (小文字アルファベット→大文字アルファベット)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceSearchWord(String input)
    {
        String strReturn;

        strReturn = "";
        strReturn = replaceAlphaLarge( input );
        strReturn = replaceNumAlphaHalf( strReturn );
        strReturn = replaceKanaFull( strReturn );
        return(strReturn);
    }

    /**
     * 文字列の置換を行う (全角カナ→半角カナ)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceKanaHalf(String input)
    {
        String strHankaku;
        String strZenkaku;
        String strHankakuDaku;
        String strZenkakuDaku;
        String strHankakuHanDaku;
        String strZenkakuHanDaku;
        String strReturn;
        int i;
        int nIndex;
        int nType;
        char cChange;

        nType = 0;
        // 普通のカタカナ変換用データ
        strHankaku = "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｦﾝｧｨｩｪｫｬｭｮｯ､｡ｰ｢｣ﾞﾟ";
        strZenkaku = "アイウエオカキクケコサシスセソタチツテトナニヌネノ";
        strZenkaku += "ハヒフヘホマミムメモヤユヨラリルレロワヲンァィゥェォャュョッ、。ー「」　　";

        // 濁音のカタカナ変換用データ
        strHankakuDaku = "ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ";
        strZenkakuDaku = "ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ";

        // 半濁音のカタカナ変換用データ
        strHankakuHanDaku = "ﾊﾋﾌﾍﾎ";
        strZenkakuHanDaku = "パピプペポ";

        strReturn = "";

        for( i = 0 ; i < input.length() ; i++ )
        {
            nType = 0;
            cChange = input.charAt( i );
            nIndex = strZenkaku.indexOf( cChange, 0 );
            if ( nIndex >= 0 )
            {
                cChange = strHankaku.charAt( nIndex );
                nType = 0;
            }
            else
            {
                nIndex = strZenkakuDaku.indexOf( cChange, 0 );
                if ( nIndex >= 0 )
                {
                    cChange = strHankakuDaku.charAt( nIndex );
                    nType = 1;
                }
                else
                {
                    nIndex = strZenkakuHanDaku.indexOf( cChange, 0 );
                    if ( nIndex >= 0 )
                    {
                        cChange = strHankakuHanDaku.charAt( nIndex );
                        nType = 2;
                    }
                }
            }
            // 変換した文字列を結合
            strReturn += cChange;

            // ﾞやﾟを追加する
            if ( nType == 1 )
            {
                strReturn += 'ﾞ';
            }
            if ( nType == 2 )
            {
                strReturn += 'ﾟ';
            }
        }
        return(strReturn);
    }

    /**
     * 文字列の置換を行う (全角記号→半角記号) (全角数字→半角数字) (全角アルファベット→半角アルファベット)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceNumAlphaHalf(String input)
    {
        String strZenMark;
        String strHanMark;
        StringBuffer strbfNumAlpha;
        strbfNumAlpha = new StringBuffer();
        char change;
        int nIndex;
        int i;

        strZenMark = "＋−＊／＝｜！？”＃＠＄％＆’｀（）［］，．；：＿＜＞＾・";
        strHanMark = "+-*/=|!?\"#@$%&'`()[],.;:_<>^･";

        for( i = 0 ; i < input.length() ; i++ )
        {
            change = input.charAt( i );
            if ( change >= 'ａ' && change <= 'ｚ' )
            {
                change += 'a' - 'ａ';
            }
            else if ( change >= 'Ａ' && change <= 'Ｚ' )
            {
                change += 'A' - 'Ａ';
            }
            else if ( change >= '０' && change <= '９' )
            {
                change += '0' - '０';
            }
            else if ( change == '　' )
            {
                change = ' ';
            }
            else if ( (nIndex = strZenMark.indexOf( change )) >= 0 )
            {
                change = strHanMark.charAt( nIndex );
            }
            strbfNumAlpha.append( change );
        }
        return strbfNumAlpha.toString();
    }

    /**
     * 文字列の置換を行う (半角カナ→全角カナ)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceKanaFull(String input)
    {
        String strHankaku;
        String strZenkaku;
        String strHankakuDakuten;
        String strHankakuHanDakuten;
        String strHankakuDaku;
        String strZenkakuDaku;
        String strHankakuHanDaku;
        String strZenkakuHanDaku;
        String strReturn;
        int i;
        int nIndex;
        int nIndex2;
        char cChange;
        char cChange2;
        boolean changedFlag;

        // 普通のカタカナ変換用データ
        strHankaku = "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｦﾝｧｨｩｪｫｬｭｮｯ､｡ｰ｢｣";
        strZenkaku = "アイウエオカキクケコサシスセソタチツテトナニヌネノ";
        strZenkaku += "ハヒフヘホマミムメモヤユヨラリルレロワヲンァィゥェォャュョッ、。ー「」";

        // 濁点、半濁点識別用データ
        strHankakuDakuten = "ﾞ";
        strHankakuHanDakuten = "ﾟ";

        // 濁音のカタカナ変換用データ
        strHankakuDaku = "ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ";
        strZenkakuDaku = "ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ";

        // 半濁音のカタカナ変換用データ
        strHankakuHanDaku = "ﾊﾋﾌﾍﾎ";
        strZenkakuHanDaku = "パピプペポ";

        strReturn = "";

        for( i = 0 ; i < input.length() ; i++ )
        {
            changedFlag = false;
            cChange = input.charAt( i );
            nIndex = strHankaku.indexOf( cChange, 0 );
            if ( nIndex >= 0 )
            {
                nIndex2 = strHankakuHanDaku.indexOf( cChange, 0 );
                // 半濁音がありうる文字の場合、次の文字が半濁点か判別
                if ( nIndex2 >= 0 && i < input.length() - 1 )
                {
                    cChange2 = input.charAt( i + 1 );
                    // 次の文字が半濁点だった場合、半濁音文字で確定
                    if ( strHankakuHanDakuten.indexOf( cChange2, 0 ) >= 0 )
                    {
                        changedFlag = true;
                        cChange = strZenkakuHanDaku.charAt( nIndex2 );
                    }
                }
                nIndex2 = strHankakuDaku.indexOf( cChange, 0 );
                // 濁音がありうる文字の場合、次の文字が濁点か判別
                if ( nIndex2 >= 0 && i < input.length() - 1 )
                {
                    cChange2 = input.charAt( i + 1 );
                    // 次の文字が濁点だった場合、濁音文字で確定
                    if ( strHankakuDakuten.indexOf( cChange2, 0 ) >= 0 )
                    {
                        changedFlag = true;
                        cChange = strZenkakuDaku.charAt( nIndex2 );
                    }
                }
                // 濁音文字、半濁音文字でなければ普通のカタカナに変換
                if ( changedFlag != true )
                {
                    cChange = strZenkaku.charAt( nIndex );
                }
            }
            // 変換した文字列を結合
            // 濁点、半濁点の場合は結合しない
            if ( cChange != strHankakuDakuten.charAt( 0 ) && cChange != strHankakuHanDakuten.charAt( 0 ) )
            {
                strReturn += cChange;
            }
        }
        return(strReturn);
    }

    /**
     * 文字列の置換を行う (半角数字→全角数字) (半角アルファベット→全角アルファベット)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceNumAlphaFull(String input)
    {
        StringBuffer strbfNumAlpha;
        strbfNumAlpha = new StringBuffer();
        char change;
        int i;

        for( i = 0 ; i < input.length() ; i++ )
        {
            change = input.charAt( i );
            if ( change >= 'a' && change <= 'z' )
            {
                change -= 'a' - 'ａ';
            }
            else if ( change >= 'A' && change <= 'Z' )
            {
                change -= 'A' - 'Ａ';
            }
            else if ( change >= '0' && change <= '9' )
            {
                change -= '0' - '０';
            }
            else if ( change == '　' )
            {
                change = ' ';
            }

            strbfNumAlpha.append( change );
        }
        return strbfNumAlpha.toString();
    }

    /**
     * 文字列の置換を行う (小文字アルファベット→大文字アルファベット)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceAlphaLarge(String input)
    {
        StringBuffer strbfNumAlpha;
        strbfNumAlpha = new StringBuffer();
        char change;
        int i;

        for( i = 0 ; i < input.length() ; i++ )
        {
            change = input.charAt( i );
            if ( change >= 'a' && change <= 'z' )
            {
                change -= 'a' - 'A';
            }
            else if ( change >= 'ａ' && change <= 'ｚ' )
            {
                change -= 'ａ' - 'Ａ';
            }
            strbfNumAlpha.append( change );
        }
        return strbfNumAlpha.toString();
    }

    /**
     * 文字列の置換を行う (大文字アルファベット→小文字アルファベット)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceAlphaSmall(String input)
    {
        StringBuffer strbfNumAlpha;
        strbfNumAlpha = new StringBuffer();
        char change;
        int i;

        for( i = 0 ; i < input.length() ; i++ )
        {
            change = input.charAt( i );
            if ( change >= 'A' && change <= 'Z' )
            {
                change += 'a' - 'A';
            }
            else if ( change >= 'Ａ' && change <= 'Ｚ' )
            {
                change += 'ａ' - 'Ａ';
            }
            strbfNumAlpha.append( change );
        }
        return strbfNumAlpha.toString();
    }

    /**
     * 文字列の置換を行う （文字列→絵文字）
     * 
     * @param input 処理の対象の文字列
     * @param carrierFlag キャリアフラグ
     * @return 置換処理後の文字列
     */
    static public String replaceEm(String input, int carrierFlag)
    {
        if ( carrierFlag == DataMasterUseragent.CARRIER_DOCOMO )
        {
            input = replace( input, "<%=em1%>", "&#xE63E;" );
            input = replace( input, "<%=em2%>", "&#xE63F;" );
            input = replace( input, "<%=em3%>", "&#xE640;" );
            input = replace( input, "<%=em4%>", "&#xE641;" );
            input = replace( input, "<%=em5%>", "&#xE642;" );
            input = replace( input, "<%=em6%>", "&#xE643;" );
            input = replace( input, "<%=em7%>", "&#xE644;" );
            input = replace( input, "<%=em8%>", "&#xE645;" );
            input = replace( input, "<%=em9%>", "&#xE646;" );
            input = replace( input, "<%=em10%>", "&#xE647;" );
            input = replace( input, "<%=em11%>", "&#xE648;" );
            input = replace( input, "<%=em12%>", "&#xE649;" );
            input = replace( input, "<%=em13%>", "&#xE64A;" );
            input = replace( input, "<%=em14%>", "&#xE64B;" );
            input = replace( input, "<%=em15%>", "&#xE64C;" );
            input = replace( input, "<%=em16%>", "&#xE64D;" );
            input = replace( input, "<%=em17%>", "&#xE64E;" );
            input = replace( input, "<%=em18%>", "&#xE64F;" );
            input = replace( input, "<%=em19%>", "&#xE650;" );
            input = replace( input, "<%=em20%>", "&#xE651;" );
            input = replace( input, "<%=em21%>", "&#xE652;" );
            input = replace( input, "<%=em22%>", "&#xE653;" );
            input = replace( input, "<%=em23%>", "&#xE654;" );
            input = replace( input, "<%=em24%>", "&#xE655;" );
            input = replace( input, "<%=em25%>", "&#xE656;" );
            input = replace( input, "<%=em26%>", "&#xE657;" );
            input = replace( input, "<%=em27%>", "&#xE658;" );
            input = replace( input, "<%=em28%>", "&#xE659;" );
            input = replace( input, "<%=em29%>", "&#xE65A;" );
            input = replace( input, "<%=em30%>", "&#xE65B;" );
            input = replace( input, "<%=em31%>", "&#xE65C;" );
            input = replace( input, "<%=em32%>", "&#xE65D;" );
            input = replace( input, "<%=em33%>", "&#xE65E;" );
            input = replace( input, "<%=em34%>", "&#xE65F;" );
            input = replace( input, "<%=em35%>", "&#xE660;" );
            input = replace( input, "<%=em36%>", "&#xE661;" );
            input = replace( input, "<%=em37%>", "&#xE662;" );
            input = replace( input, "<%=em38%>", "&#xE663;" );
            input = replace( input, "<%=em39%>", "&#xE664;" );
            input = replace( input, "<%=em40%>", "&#xE665;" );
            input = replace( input, "<%=em41%>", "&#xE666;" );
            input = replace( input, "<%=em42%>", "&#xE667;" );
            input = replace( input, "<%=em43%>", "&#xE668;" );
            input = replace( input, "<%=em44%>", "&#xE669;" );
            input = replace( input, "<%=em45%>", "&#xE66A;" );
            input = replace( input, "<%=em46%>", "&#xE66B;" );
            input = replace( input, "<%=em47%>", "&#xE66C;" );
            input = replace( input, "<%=em48%>", "&#xE66D;" );
            input = replace( input, "<%=em49%>", "&#xE66E;" );
            input = replace( input, "<%=em50%>", "&#xE66F;" );
            input = replace( input, "<%=em51%>", "&#xE670;" );
            input = replace( input, "<%=em52%>", "&#xE671;" );
            input = replace( input, "<%=em53%>", "&#xE672;" );
            input = replace( input, "<%=em54%>", "&#xE673;" );
            input = replace( input, "<%=em55%>", "&#xE674;" );
            input = replace( input, "<%=em56%>", "&#xE675;" );
            input = replace( input, "<%=em57%>", "&#xE676;" );
            input = replace( input, "<%=em58%>", "&#xE677;" );
            input = replace( input, "<%=em59%>", "&#xE678;" );
            input = replace( input, "<%=em60%>", "&#xE679;" );
            input = replace( input, "<%=em61%>", "&#xE67A;" );
            input = replace( input, "<%=em62%>", "&#xE67B;" );
            input = replace( input, "<%=em63%>", "&#xE67C;" );
            input = replace( input, "<%=em64%>", "&#xE67D;" );
            input = replace( input, "<%=em65%>", "&#xE67E;" );
            input = replace( input, "<%=em66%>", "&#xE67F;" );
            input = replace( input, "<%=em67%>", "&#xE680;" );
            input = replace( input, "<%=em68%>", "&#xE681;" );
            input = replace( input, "<%=em69%>", "&#xE682;" );
            input = replace( input, "<%=em70%>", "&#xE683;" );
            input = replace( input, "<%=em71%>", "&#xE684;" );
            input = replace( input, "<%=em72%>", "&#xE685;" );
            input = replace( input, "<%=em73%>", "&#xE686;" );
            input = replace( input, "<%=em74%>", "&#xE687;" );
            input = replace( input, "<%=em75%>", "&#xE688;" );
            input = replace( input, "<%=em76%>", "&#xE689;" );
            input = replace( input, "<%=em77%>", "&#xE68A;" );
            input = replace( input, "<%=em78%>", "&#xE68B;" );
            input = replace( input, "<%=em79%>", "&#xE68C;" );
            input = replace( input, "<%=em80%>", "&#xE68D;" );
            input = replace( input, "<%=em81%>", "&#xE68E;" );
            input = replace( input, "<%=em82%>", "&#xE68F;" );
            input = replace( input, "<%=em83%>", "&#xE690;" );
            input = replace( input, "<%=em84%>", "&#xE691;" );
            input = replace( input, "<%=em85%>", "&#xE692;" );
            input = replace( input, "<%=em86%>", "&#xE693;" );
            input = replace( input, "<%=em87%>", "&#xE694;" );
            input = replace( input, "<%=em88%>", "&#xE695;" );
            input = replace( input, "<%=em89%>", "&#xE696;" );
            input = replace( input, "<%=em90%>", "&#xE697;" );
            input = replace( input, "<%=em91%>", "&#xE698;" );
            input = replace( input, "<%=em92%>", "&#xE699;" );
            input = replace( input, "<%=em93%>", "&#xE69A;" );
            input = replace( input, "<%=em94%>", "&#xE69B;" );
            input = replace( input, "<%=em95%>", "&#xE69C;" );
            input = replace( input, "<%=em96%>", "&#xE69D;" );
            input = replace( input, "<%=em97%>", "&#xE69E;" );
            input = replace( input, "<%=em98%>", "&#xE69F;" );
            input = replace( input, "<%=em99%>", "&#xE6A0;" );
            input = replace( input, "<%=em100%>", "&#xE6A1;" );
            input = replace( input, "<%=em101%>", "&#xE6A2;" );
            input = replace( input, "<%=em102%>", "&#xE6A3;" );
            input = replace( input, "<%=em103%>", "&#xE6A4;" );
            input = replace( input, "<%=em104%>", "&#xE6A5;" );
            input = replace( input, "<%=em105%>", "&#xE6CE;" );
            input = replace( input, "<%=em106%>", "&#xE6CF;" );
            input = replace( input, "<%=em107%>", "&#xE6D0;" );
            input = replace( input, "<%=em108%>", "&#xE6D1;" );
            input = replace( input, "<%=em109%>", "&#xE6D2;" );
            input = replace( input, "<%=em110%>", "&#xE6D3;" );
            input = replace( input, "<%=em111%>", "&#xE6D4;" );
            input = replace( input, "<%=em112%>", "&#xE6D5;" );
            input = replace( input, "<%=em113%>", "&#xE6D6;" );
            input = replace( input, "<%=em114%>", "&#xE6D7;" );
            input = replace( input, "<%=em115%>", "&#xE6D8;" );
            input = replace( input, "<%=em116%>", "&#xE6D9;" );
            input = replace( input, "<%=em117%>", "&#xE6DA;" );
            input = replace( input, "<%=em118%>", "&#xE6DB;" );
            input = replace( input, "<%=em119%>", "&#xE6DC;" );
            input = replace( input, "<%=em120%>", "&#xE6DD;" );
            input = replace( input, "<%=em121%>", "&#xE6DE;" );
            input = replace( input, "<%=em122%>", "&#xE6DF;" );
            input = replace( input, "<%=em123%>", "&#xE6E0;" );
            input = replace( input, "<%=em124%>", "&#xE6E1;" );
            input = replace( input, "<%=em125%>", "&#xE6E2;" );
            input = replace( input, "<%=em126%>", "&#xE6E3;" );
            input = replace( input, "<%=em127%>", "&#xE6E4;" );
            input = replace( input, "<%=em128%>", "&#xE6E5;" );
            input = replace( input, "<%=em129%>", "&#xE6E6;" );
            input = replace( input, "<%=em130%>", "&#xE6E7;" );
            input = replace( input, "<%=em131%>", "&#xE6E8;" );
            input = replace( input, "<%=em132%>", "&#xE6E9;" );
            input = replace( input, "<%=em133%>", "&#xE6EA;" );
            input = replace( input, "<%=em134%>", "&#xE6EB;" );
            input = replace( input, "<%=em135%>", "&#xE70B;" );
            input = replace( input, "<%=em136%>", "&#xE6EC;" );
            input = replace( input, "<%=em137%>", "&#xE6ED;" );
            input = replace( input, "<%=em138%>", "&#xE6EE;" );
            input = replace( input, "<%=em139%>", "&#xE6EF;" );
            input = replace( input, "<%=em140%>", "&#xE6F0;" );
            input = replace( input, "<%=em141%>", "&#xE6F1;" );
            input = replace( input, "<%=em142%>", "&#xE6F2;" );
            input = replace( input, "<%=em143%>", "&#xE6F3;" );
            input = replace( input, "<%=em144%>", "&#xE6F4;" );
            input = replace( input, "<%=em145%>", "&#xE6F5;" );
            input = replace( input, "<%=em146%>", "&#xE6F6;" );
            input = replace( input, "<%=em147%>", "&#xE6F7;" );
            input = replace( input, "<%=em148%>", "&#xE6F8;" );
            input = replace( input, "<%=em149%>", "&#xE6F9;" );
            input = replace( input, "<%=em150%>", "&#xE6FA;" );
            input = replace( input, "<%=em151%>", "&#xE6FB;" );
            input = replace( input, "<%=em152%>", "&#xE6FC;" );
            input = replace( input, "<%=em153%>", "&#xE6FD;" );
            input = replace( input, "<%=em154%>", "&#xE6FE;" );
            input = replace( input, "<%=em155%>", "&#xE6FF;" );
            input = replace( input, "<%=em156%>", "&#xE700;" );
            input = replace( input, "<%=em157%>", "&#xE701;" );
            input = replace( input, "<%=em158%>", "&#xE702;" );
            input = replace( input, "<%=em159%>", "&#xE703;" );
            input = replace( input, "<%=em160%>", "&#xE704;" );
            input = replace( input, "<%=em161%>", "&#xE705;" );
            input = replace( input, "<%=em162%>", "&#xE706;" );
            input = replace( input, "<%=em163%>", "&#xE707;" );
            input = replace( input, "<%=em164%>", "&#xE708;" );
            input = replace( input, "<%=em165%>", "&#xE709;" );
            input = replace( input, "<%=em166%>", "&#xE70A;" );
            input = replace( input, "<%=em167%>", "&#xE6AC;" );
            input = replace( input, "<%=em168%>", "&#xE6AD;" );
            input = replace( input, "<%=em169%>", "&#xE6AE;" );
            input = replace( input, "<%=em170%>", "&#xE6B1;" );
            input = replace( input, "<%=em171%>", "&#xE6B2;" );
            input = replace( input, "<%=em172%>", "&#xE6B3;" );
            input = replace( input, "<%=em173%>", "&#xE6B7;" );
            input = replace( input, "<%=em174%>", "&#xE6B8;" );
            input = replace( input, "<%=em175%>", "&#xE6B9;" );
            input = replace( input, "<%=em176%>", "&#xE6BA;" );
            input = replace( input, "<%=em177%>", "&#xE70C;" );
            input = replace( input, "<%=em178%>", "&#xE70D;" );
            input = replace( input, "<%=em179%>", "&#xE70E;" );
            input = replace( input, "<%=em180%>", "&#xE71F;" );
            input = replace( input, "<%=em181%>", "&#xE710;" );
            input = replace( input, "<%=em182%>", "&#xE711;" );
            input = replace( input, "<%=em183%>", "&#xE712;" );
            input = replace( input, "<%=em184%>", "&#xE713;" );
            input = replace( input, "<%=em185%>", "&#xE714;" );
            input = replace( input, "<%=em186%>", "&#xE715;" );
            input = replace( input, "<%=em187%>", "&#xE716;" );
            input = replace( input, "<%=em188%>", "&#xE717;" );
            input = replace( input, "<%=em189%>", "&#xE718;" );
            input = replace( input, "<%=em190%>", "&#xE719;" );
            input = replace( input, "<%=em191%>", "&#xE71A;" );
            input = replace( input, "<%=em192%>", "&#xE71B;" );
            input = replace( input, "<%=em193%>", "&#xE71C;" );
            input = replace( input, "<%=em194%>", "&#xE71D;" );
            input = replace( input, "<%=em195%>", "&#xE71E;" );
            input = replace( input, "<%=em196%>", "&#xE71F;" );
            input = replace( input, "<%=em197%>", "&#xE720;" );
            input = replace( input, "<%=em198%>", "&#xE721;" );
            input = replace( input, "<%=em199%>", "&#xE722;" );
            input = replace( input, "<%=em200%>", "&#xE723;" );
            input = replace( input, "<%=em201%>", "&#xE724;" );
            input = replace( input, "<%=em202%>", "&#xE725;" );
            input = replace( input, "<%=em203%>", "&#xE726;" );
            input = replace( input, "<%=em204%>", "&#xE727;" );
            input = replace( input, "<%=em205%>", "&#xE728;" );
            input = replace( input, "<%=em206%>", "&#xE729;" );
            input = replace( input, "<%=em207%>", "&#xE72A;" );
            input = replace( input, "<%=em208%>", "&#xE72B;" );
            input = replace( input, "<%=em209%>", "&#xE72C;" );
            input = replace( input, "<%=em210%>", "&#xE72D;" );
            input = replace( input, "<%=em211%>", "&#xE72E;" );
            input = replace( input, "<%=em212%>", "&#xE72F;" );
            input = replace( input, "<%=em213%>", "&#xE730;" );
            input = replace( input, "<%=em214%>", "&#xE731;" );
            input = replace( input, "<%=em215%>", "&#xE732;" );
            input = replace( input, "<%=em216%>", "&#xE733;" );
            input = replace( input, "<%=em217%>", "&#xE734;" );
            input = replace( input, "<%=em218%>", "&#xE735;" );
            input = replace( input, "<%=em219%>", "&#xE736;" );
            input = replace( input, "<%=em220%>", "&#xE737;" );
            input = replace( input, "<%=em221%>", "&#xE738;" );
            input = replace( input, "<%=em222%>", "&#xE739;" );
            input = replace( input, "<%=em223%>", "&#xE73A;" );
            input = replace( input, "<%=em224%>", "&#xE73B;" );
            input = replace( input, "<%=em225%>", "&#xE73C;" );
            input = replace( input, "<%=em226%>", "&#xE73D;" );
            input = replace( input, "<%=em227%>", "&#xE73E;" );
            input = replace( input, "<%=em228%>", "&#xE73F;" );
            input = replace( input, "<%=em229%>", "&#xE740;" );
            input = replace( input, "<%=em230%>", "&#xE741;" );
            input = replace( input, "<%=em231%>", "&#xE742;" );
            input = replace( input, "<%=em232%>", "&#xE743;" );
            input = replace( input, "<%=em233%>", "&#xE744;" );
            input = replace( input, "<%=em234%>", "&#xE745;" );
            input = replace( input, "<%=em235%>", "&#xE746;" );
            input = replace( input, "<%=em236%>", "&#xE747;" );
            input = replace( input, "<%=em237%>", "&#xE748;" );
            input = replace( input, "<%=em238%>", "&#xE749;" );
            input = replace( input, "<%=em239%>", "&#xE74A;" );
            input = replace( input, "<%=em240%>", "&#xE74B;" );
            input = replace( input, "<%=em241%>", "&#xE74C;" );
            input = replace( input, "<%=em242%>", "&#xE74D;" );
            input = replace( input, "<%=em243%>", "&#xE74E;" );
            input = replace( input, "<%=em244%>", "&#xE74F;" );
            input = replace( input, "<%=em245%>", "&#xE750;" );
            input = replace( input, "<%=em246%>", "&#xE751;" );
            input = replace( input, "<%=em247%>", "&#xE752;" );
            input = replace( input, "<%=em248%>", "&#xE753;" );
            input = replace( input, "<%=em249%>", "&#xE754;" );
            input = replace( input, "<%=em250%>", "&#xE755;" );
            input = replace( input, "<%=em251%>", "&#xE756;" );
            input = replace( input, "<%=em252%>", "&#xE757;" );
        }
        else if ( carrierFlag == DataMasterUseragent.CARRIER_AU )
        {
            input = replace( input, "<%=em1%>", "<img localsrc='44'>" );
            input = replace( input, "<%=em2%>", "<img localsrc='107'>" );
            input = replace( input, "<%=em3%>", "<img localsrc='95'>" );
            input = replace( input, "<%=em4%>", "<img localsrc='191'>" );
            input = replace( input, "<%=em5%>", "<img localsrc='16'>" );
            input = replace( input, "<%=em6%>", "<img localsrc='190'>" );
            input = replace( input, "<%=em7%>", "<img localsrc='305'>" );
            input = replace( input, "<%=em8%>", "<img localsrc='481'>" );
            input = replace( input, "<%=em9%>", "<img localsrc='192'>" );
            input = replace( input, "<%=em10%>", "<img localsrc='193'>" );
            input = replace( input, "<%=em11%>", "<img localsrc='194'>" );
            input = replace( input, "<%=em12%>", "<img localsrc='392'>" );
            input = replace( input, "<%=em13%>", "<img localsrc='196'>" );
            input = replace( input, "<%=em14%>", "<img localsrc='197'>" );
            input = replace( input, "<%=em15%>", "<img localsrc='198'>" );
            input = replace( input, "<%=em16%>", "<img localsrc='199'>" );
            input = replace( input, "<%=em17%>", "<img localsrc='200'>" );
            input = replace( input, "<%=em18%>", "<img localsrc='201'>" );
            input = replace( input, "<%=em19%>", "<img localsrc='202'>" );
            input = replace( input, "<%=em20%>", "<img localsrc='203'>" );
            input = replace( input, "<%=em21%>", "[ｽﾎﾟｰﾂ]" );
            input = replace( input, "<%=em22%>", "<img localsrc='45'>" );
            input = replace( input, "<%=em23%>", "<img localsrc='306'>" );
            input = replace( input, "<%=em24%>", "<img localsrc='220'>" );
            input = replace( input, "<%=em25%>", "<img localsrc='219'>" );
            input = replace( input, "<%=em26%>", "<img localsrc='421'>" );
            input = replace( input, "<%=em27%>", "<img localsrc='307'>" );
            input = replace( input, "<%=em28%>", "<img localsrc='222'>" );
            input = replace( input, "<%=em29%>", "<img localsrc='308'>" );
            input = replace( input, "<%=em30%>", "<img localsrc='172'>" );
            input = replace( input, "<%=em31%>", "<img localsrc='341'>" );
            input = replace( input, "<%=em32%>", "<img localsrc='217'>" );
            input = replace( input, "<%=em33%>", "<img localsrc='125'>" );
            input = replace( input, "<%=em34%>", "<img localsrc='125'>" );
            input = replace( input, "<%=em35%>", "<img localsrc='216'>" );
            input = replace( input, "<%=em36%>", "<img localsrc='379'>" );
            input = replace( input, "<%=em37%>", "<img localsrc='168'>" );
            input = replace( input, "<%=em38%>", "<img localsrc='112'>" );
            input = replace( input, "<%=em39%>", "<img localsrc='156'>" );
            input = replace( input, "<%=em40%>", "<img localsrc='375'>" );
            input = replace( input, "<%=em41%>", "<img localsrc='376'>" );
            input = replace( input, "<%=em42%>", "<img localsrc='212'>" );
            input = replace( input, "<%=em43%>", "<img localsrc='205'>" );
            input = replace( input, "<%=em44%>", "<img localsrc='378'>" );
            input = replace( input, "<%=em45%>", "<img localsrc='206'>" );
            input = replace( input, "<%=em46%>", "<img localsrc='213'>" );
            input = replace( input, "<%=em47%>", "<img localsrc='208'>" );
            input = replace( input, "<%=em48%>", "<img localsrc='99'>" );
            input = replace( input, "<%=em49%>", "<img localsrc='207'>" );
            input = replace( input, "<%=em50%>", "<img localsrc='146'>" );
            input = replace( input, "<%=em51%>", "<img localsrc='93'>" );
            input = replace( input, "<%=em52%>", "<img localsrc='52'>" );
            input = replace( input, "<%=em53%>", "<img localsrc='65'>" );
            input = replace( input, "<%=em54%>", "<img localsrc='245'>" );
            input = replace( input, "<%=em55%>", "<img localsrc='124'>" );
            input = replace( input, "<%=em56%>", "<img localsrc='104'>" );
            input = replace( input, "<%=em57%>", "<img localsrc='289'>" );
            input = replace( input, "<%=em58%>", "<img localsrc='110'>" );
            input = replace( input, "<%=em59%>", "<img localsrc='70'>" );
            input = replace( input, "<%=em60%>", "<img localsrc='223'>" );
            input = replace( input, "<%=em61%>", "<img localsrc='294'>" );
            input = replace( input, "<%=em62%>", "<img localsrc='309'>" );
            input = replace( input, "<%=em63%>", "<img localsrc='494'>" );
            input = replace( input, "<%=em64%>", "<img localsrc='311'>" );
            input = replace( input, "<%=em65%>", "<img localsrc='106'>" );
            input = replace( input, "<%=em66%>", "<img localsrc='176'>" );
            input = replace( input, "<%=em67%>", "<img localsrc='177'>" );
            input = replace( input, "<%=em68%>", "<img localsrc='94'>" );
            input = replace( input, "<%=em69%>", "<img localsrc='83'>" );
            input = replace( input, "<%=em70%>", "<img localsrc='122'>" );
            input = replace( input, "<%=em71%>", "<img localsrc='312'>" );
            input = replace( input, "<%=em72%>", "<img localsrc='144'>" );
            input = replace( input, "<%=em73%>", "<img localsrc='313'>" );
            input = replace( input, "<%=em74%>", "<img localsrc='85'>" );
            input = replace( input, "<%=em75%>", "<img localsrc='161'>" );
            input = replace( input, "<%=em76%>", "<img localsrc='395'>" );
            input = replace( input, "<%=em77%>", "<img localsrc='288'>" );
            input = replace( input, "<%=em78%>", "<img localsrc='232'>" );
            input = replace( input, "<%=em79%>", "<img localsrc='300'>" );
            input = replace( input, "<%=em80%>", "<img localsrc='414'>" );
            input = replace( input, "<%=em81%>", "<img localsrc='314'>" );
            input = replace( input, "<%=em82%>", "<img localsrc='315'>" );
            input = replace( input, "<%=em83%>", "<img localsrc='316'>" );
            input = replace( input, "<%=em84%>", "<img localsrc='317'>" );
            input = replace( input, "<%=em85%>", "<img localsrc='318'>" );
            input = replace( input, "<%=em86%>", "<img localsrc='817'>" );
            input = replace( input, "<%=em87%>", "<img localsrc='319'>" );
            input = replace( input, "<%=em88%>", "<img localsrc='320'>" );
            input = replace( input, "<%=em89%>", "<img localsrc='42'>" );
            input = replace( input, "<%=em90%>", "<img localsrc='42'>" );
            input = replace( input, "<%=em91%>", "<img localsrc='728'>" );
            input = replace( input, "<%=em92%>", "<img localsrc='729'>" );
            input = replace( input, "<%=em93%>", "<img localsrc='116'>" );
            input = replace( input, "<%=em94%>", "<img localsrc='178'>" );
            input = replace( input, "<%=em95%>", "<img localsrc='321'>" );
            input = replace( input, "<%=em96%>", "<img localsrc='322'>" );
            input = replace( input, "<%=em97%>", "<img localsrc='323'>" );
            input = replace( input, "<%=em98%>", "<img localsrc='15'>" );
            input = replace( input, "<%=em99%>", "<img localsrc='422'>" );
            input = replace( input, "<%=em100%>", "<img localsrc='134'>" );
            input = replace( input, "<%=em101%>", "<img localsrc='251'>" );
            input = replace( input, "<%=em102%>", "<img localsrc='169'>" );
            input = replace( input, "<%=em103%>", "<img localsrc='234'>" );
            input = replace( input, "<%=em104%>", "<img localsrc='71'>" );
            input = replace( input, "<%=em105%>", "<img localsrc='513'>" );
            input = replace( input, "<%=em106%>", "<img localsrc='784'>" );
            input = replace( input, "<%=em107%>", "<img localsrc='166'>" );
            input = replace( input, "<%=em108%>", "[iﾓｰﾄﾞ]" );
            input = replace( input, "<%=em109%>", "[iﾓｰﾄﾞ]" );
            input = replace( input, "<%=em110%>", "<img localsrc='108'>" );
            input = replace( input, "<%=em111%>", "[ﾄﾞｺﾓ]" );
            input = replace( input, "<%=em112%>", "[ﾄﾞｺﾓﾎﾟｲﾝﾄ]" );
            input = replace( input, "<%=em113%>", "<img localsrc='109'>" );
            input = replace( input, "<%=em114%>", "<img localsrc='299'>" );
            input = replace( input, "<%=em115%>", "<img localsrc='385'>" );
            input = replace( input, "<%=em116%>", "<img localsrc='120'>" );
            input = replace( input, "<%=em117%>", "<img localsrc='118'>" );
            input = replace( input, "<%=em118%>", "<img localsrc='324'>" );
            input = replace( input, "<%=em119%>", "<img localsrc='119'>" );
            input = replace( input, "<%=em120%>", "<img localsrc='334'>" );
            input = replace( input, "<%=em121%>", "<img localsrc='730'>" );
            input = replace( input, "<%=em122%>", "<img localsrc='299'>" );
            input = replace( input, "<%=em123%>", "<img localsrc='818'>" );
            input = replace( input, "<%=em124%>", "<img localsrc='4'>" );
            input = replace( input, "<%=em125%>", "<img localsrc='180'>" );
            input = replace( input, "<%=em126%>", "<img localsrc='181'>" );
            input = replace( input, "<%=em127%>", "<img localsrc='182'>" );
            input = replace( input, "<%=em128%>", "<img localsrc='183'>" );
            input = replace( input, "<%=em129%>", "<img localsrc='184'>" );
            input = replace( input, "<%=em130%>", "<img localsrc='185'>" );
            input = replace( input, "<%=em131%>", "<img localsrc='186'>" );
            input = replace( input, "<%=em132%>", "<img localsrc='187'>" );
            input = replace( input, "<%=em133%>", "<img localsrc='188'>" );
            input = replace( input, "<%=em134%>", "<img localsrc='325'>" );
            input = replace( input, "<%=em135%>", "<img localsrc='326'>" );
            input = replace( input, "<%=em136%>", "<img localsrc='51'>" );
            input = replace( input, "<%=em137%>", "<img localsrc='803'>" );
            input = replace( input, "<%=em138%>", "<img localsrc='265'>" );
            input = replace( input, "<%=em139%>", "<img localsrc='266'>" );
            input = replace( input, "<%=em140%>", "<img localsrc='257'>" );
            input = replace( input, "<%=em141%>", "<img localsrc='258'>" );
            input = replace( input, "<%=em142%>", "<img localsrc='442'>" );
            input = replace( input, "<%=em143%>", "<img localsrc='441'>" );
            input = replace( input, "<%=em144%>", "<img localsrc='327'>" );
            input = replace( input, "<%=em145%>", "<img localsrc='731'>" );
            input = replace( input, "<%=em146%>", "<img localsrc='343'>" );
            input = replace( input, "<%=em147%>", "<img localsrc='224'>" );
            input = replace( input, "<%=em148%>", "<img localsrc='397'>" );
            input = replace( input, "<%=em149%>", "<img localsrc='273'>" );
            input = replace( input, "<%=em150%>", "<img localsrc='420'>" );
            input = replace( input, "<%=em151%>", "<img localsrc='77'>" );
            input = replace( input, "<%=em152%>", "<img localsrc='262'>" );
            input = replace( input, "<%=em153%>", "<img localsrc='281'>" );
            input = replace( input, "<%=em154%>", "<img localsrc='268'>" );
            input = replace( input, "<%=em155%>", "<img localsrc='291'>" );
            input = replace( input, "<%=em156%>", "<img localsrc='732'>" );
            input = replace( input, "<%=em157%>", "<img localsrc='261'>" );
            input = replace( input, "<%=em158%>", "<img localsrc='2'>" );
            input = replace( input, "<%=em159%>", "<img localsrc='733'>" );
            input = replace( input, "<%=em160%>", "<img localsrc='734'>" );
            input = replace( input, "<%=em161%>", "<img localsrc='329'>" );
            input = replace( input, "<%=em162%>", "<img localsrc='330'>" );
            input = replace( input, "<%=em163%>", "<img localsrc='263'>" );
            input = replace( input, "<%=em164%>", "<img localsrc='282'>" );
            input = replace( input, "<%=em165%>", "-" );
            input = replace( input, "<%=em166%>", "<img localsrc='735'>" );
            input = replace( input, "<%=em167%>", "<img localsrc='226'>" );
            input = replace( input, "<%=em168%>", "ふくろ" );
            input = replace( input, "<%=em169%>", "<img localsrc='508'>" );
            input = replace( input, "<%=em170%>", "人形" );
            input = replace( input, "<%=em171%>", "いす" );
            input = replace( input, "<%=em172%>", "<img localsrc='490'>" );
            input = replace( input, "<%=em173%>", "[SOON]" );
            input = replace( input, "<%=em174%>", "[ON]" );
            input = replace( input, "<%=em175%>", "[end]" );
            input = replace( input, "<%=em176%>", "<img localsrc='46'>" );
            input = replace( input, "<%=em177%>", "iｱﾌﾟﾘ" );
            input = replace( input, "<%=em178%>", "iｱﾌﾟﾘ" );
            input = replace( input, "<%=em179%>", "<img localsrc='335'>" );
            input = replace( input, "<%=em180%>", "<img localsrc='290'>" );
            input = replace( input, "<%=em181%>", "<img localsrc='295'>" );
            input = replace( input, "<%=em182%>", "<img localsrc='805'>" );
            input = replace( input, "<%=em183%>", "<img localsrc='221'>" );
            input = replace( input, "<%=em184%>", "<img localsrc='48'>" );
            input = replace( input, "<%=em185%>", "ﾄﾞｱ" );
            input = replace( input, "<%=em186%>", "<img localsrc='233'>" );
            input = replace( input, "<%=em187%>", "<img localsrc='337'>" );
            input = replace( input, "<%=em188%>", "<img localsrc='806'>" );
            input = replace( input, "<%=em189%>", "<img localsrc='152'>" );
            input = replace( input, "<%=em190%>", "<img localsrc='149'>" );
            input = replace( input, "<%=em191%>", "<img localsrc='354'>" );
            input = replace( input, "<%=em192%>", "<img localsrc='72'>" );
            input = replace( input, "<%=em193%>", "<img localsrc='58'>" );
            input = replace( input, "<%=em194%>", "<img localsrc='215'>" );
            input = replace( input, "<%=em195%>", "<img localsrc='423'>" );
            input = replace( input, "<%=em196%>", "<img localsrc='25'>" );
            input = replace( input, "<%=em197%>", "<img localsrc='441'>" );
            input = replace( input, "<%=em198%>", "<img localsrc='446'>" );
            input = replace( input, "<%=em199%>", "<img localsrc='257'><img localsrc='330'>" );
            input = replace( input, "<%=em200%>", "<img localsrc='351'>" );
            input = replace( input, "<%=em201%>", "<img localsrc='779'>" );
            input = replace( input, "<%=em202%>", "<img localsrc='450'>" );
            input = replace( input, "<%=em203%>", "<img localsrc='349'>" );
            input = replace( input, "<%=em204%>", "<img localsrc='287'>" );
            input = replace( input, "<%=em206%>", "<img localsrc='348'>" );
            input = replace( input, "<%=em207%>", "<img localsrc='446'>" );
            input = replace( input, "<%=em208%>", "<img localsrc='443'>" );
            input = replace( input, "<%=em209%>", "<img localsrc='440'>" );
            input = replace( input, "<%=em210%>", "<img localsrc='259'>" );
            input = replace( input, "<%=em211%>", "<img localsrc='791'>" );
            input = replace( input, "<%=em212%>", "NG" );
            input = replace( input, "<%=em213%>", "<img localsrc='143'>" );
            input = replace( input, "<%=em214%>", "<img localsrc='81'>" );
            input = replace( input, "<%=em215%>", "<img localsrc='54'>" );
            input = replace( input, "<%=em216%>", "<img localsrc='218'>" );
            input = replace( input, "<%=em217%>", "<img localsrc='279'>" );
            input = replace( input, "<%=em218%>", "<img localsrc='807'>" );
            input = replace( input, "<%=em219%>", "<img localsrc='82'>" );
            input = replace( input, "<%=em220%>", "<img localsrc='1'>" );
            input = replace( input, "<%=em221%>", "[禁]" );
            input = replace( input, "<%=em222%>", "<img localsrc='387'>" );
            input = replace( input, "<%=em223%>", "[合]" );
            input = replace( input, "<%=em224%>", "<img localsrc='386'>" );
            input = replace( input, "<%=em225%>", "<img localsrc='808'>" );
            input = replace( input, "<%=em226%>", "<img localsrc='809'>" );
            input = replace( input, "<%=em227%>", "<img localsrc='377'>" );
            input = replace( input, "<%=em228%>", "<img localsrc='810'>" );
            input = replace( input, "<%=em229%>", "<img localsrc='342'>" );
            input = replace( input, "<%=em230%>", "<img localsrc='53'>" );
            input = replace( input, "<%=em231%>", "<img localsrc='241'>" );
            input = replace( input, "<%=em232%>", "<img localsrc='113'>" );
            input = replace( input, "<%=em233%>", "<img localsrc='739'>" );
            input = replace( input, "<%=em234%>", "<img localsrc='434'>" );
            input = replace( input, "<%=em235%>", "<img localsrc='811'>" );
            input = replace( input, "<%=em236%>", "<img localsrc='133'>" );
            input = replace( input, "<%=em237%>", "<img localsrc='235'>" );
            input = replace( input, "<%=em238%>", "<img localsrc='244'>" );
            input = replace( input, "<%=em239%>", "<img localsrc='239'>" );
            input = replace( input, "<%=em240%>", "<img localsrc='400'>" );
            input = replace( input, "<%=em241%>", "<img localsrc='333'>" );
            input = replace( input, "<%=em242%>", "<img localsrc='424'>" );
            input = replace( input, "<%=em243%>", "<img localsrc='812'>" );
            input = replace( input, "<%=em244%>", "<img localsrc='78'>" );
            input = replace( input, "<%=em245%>", "<img localsrc='252'>" );
            input = replace( input, "<%=em246%>", "<img localsrc='203'>" );
            input = replace( input, "<%=em247%>", "<img localsrc='454'>" );
            input = replace( input, "<%=em248%>", "<img localsrc='814'>" );
            input = replace( input, "<%=em249%>", "<img localsrc='248'>" );
            input = replace( input, "<%=em250%>", "<img localsrc='254'>" );
            input = replace( input, "<%=em251%>", "<img localsrc='12'>" );
            input = replace( input, "<%=em252%>", "<img localsrc='350'>" );
        }
        else if ( carrierFlag == DataMasterUseragent.CARRIER_SOFTBANK )
        {
            input = replace( input, "<%=em1%>", "$Gj" );
            input = replace( input, "<%=em2%>", "$Gi" );
            input = replace( input, "<%=em3%>", "$Gk" );
            input = replace( input, "<%=em4%>", "$Gh" );
            input = replace( input, "<%=em5%>", "$E]" );
            input = replace( input, "<%=em6%>", "$Pc" );
            input = replace( input, "<%=em7%>", "[霧]" );
            input = replace( input, "<%=em8%>", "$P\\" );
            input = replace( input, "<%=em9%>", "$F_" );
            input = replace( input, "<%=em10%>", "$F`" );
            input = replace( input, "<%=em11%>", "$Fa" );
            input = replace( input, "<%=em12%>", "$Fb" );
            input = replace( input, "<%=em13%>", "$Fc" );
            input = replace( input, "<%=em14%>", "$Fd" );
            input = replace( input, "<%=em15%>", "$Fe" );
            input = replace( input, "<%=em16%>", "$Ff" );
            input = replace( input, "<%=em17%>", "$Fg" );
            input = replace( input, "<%=em18%>", "$Fh" );
            input = replace( input, "<%=em19%>", "$Fi" );
            input = replace( input, "<%=em20%>", "$Fj" );
            input = replace( input, "<%=em21%>", "[ｽﾎﾟｰﾂ]" );
            input = replace( input, "<%=em22%>", "$G6" );
            input = replace( input, "<%=em23%>", "$G4" );
            input = replace( input, "<%=em24%>", "$G5" );
            input = replace( input, "<%=em25%>", "$G8" );
            input = replace( input, "<%=em26%>", "$G3" );
            input = replace( input, "<%=em27%>", "$PJ" );
            input = replace( input, "<%=em28%>", "$ER" );
            input = replace( input, "<%=em29%>", "[ﾎﾟｹｯﾄﾍﾞﾙ]" );
            input = replace( input, "<%=em30%>", "$G>" );
            input = replace( input, "<%=em31%>", "$PT" );
            input = replace( input, "<%=em32%>", "$PU" );
            input = replace( input, "<%=em33%>", "$G;" );
            input = replace( input, "<%=em34%>", "$PN" );
            input = replace( input, "<%=em35%>", "$Ey" );
            input = replace( input, "<%=em36%>", "$F\"" );
            input = replace( input, "<%=em37%>", "$G=" );
            input = replace( input, "<%=em38%>", "$GV" );
            input = replace( input, "<%=em39%>", "$GX" );
            input = replace( input, "<%=em40%>", "$Es" );
            input = replace( input, "<%=em41%>", "$Eu" );
            input = replace( input, "<%=em42%>", "$Em" );
            input = replace( input, "<%=em43%>", "$Et" );
            input = replace( input, "<%=em44%>", "$Ex" );
            input = replace( input, "<%=em45%>", "$Ev" );
            input = replace( input, "<%=em46%>", "$GZ" );
            input = replace( input, "<%=em47%>", "$Eo" );
            input = replace( input, "<%=em48%>", "$En" );
            input = replace( input, "<%=em49%>", "$Eq" );
            input = replace( input, "<%=em50%>", "$Gc" );
            input = replace( input, "<%=em51%>", "$Ge" );
            input = replace( input, "<%=em52%>", "$Gd" );
            input = replace( input, "<%=em53%>", "$Gg" );
            input = replace( input, "<%=em54%>", "$E@" );
            input = replace( input, "<%=em55%>", "$O:" );
            input = replace( input, "<%=em56%>", "$O3" );
            input = replace( input, "<%=em57%>", "$G\\" );
            input = replace( input, "<%=em58%>", "$G]" );
            input = replace( input, "<%=em59%>", "$FV" );
            input = replace( input, "<%=em60%>", "$ED" );
            input = replace( input, "<%=em61%>", "$O*" );
            input = replace( input, "<%=em62%>", "$Q\"" );
            input = replace( input, "<%=em63%>", "$Q#" );
            input = replace( input, "<%=em64%>", "[ｲﾍﾞﾝﾄ]" );
            input = replace( input, "<%=em65%>", "$EE" );
            input = replace( input, "<%=em66%>", "$O." );
            input = replace( input, "<%=em67%>", "$F(" );
            input = replace( input, "<%=em68%>", "$G(" );
            input = replace( input, "<%=em69%>", "$OC" );
            input = replace( input, "<%=em70%>", "$Eh" );
            input = replace( input, "<%=em71%>", "$O4" );
            input = replace( input, "<%=em72%>", "$E2" );
            input = replace( input, "<%=em73%>", "$Ok" );
            input = replace( input, "<%=em74%>", "$G)" );
            input = replace( input, "<%=em75%>", "$G*" );
            input = replace( input, "<%=em76%>", "$O!" );
            input = replace( input, "<%=em77%>", "$EJ" );
            input = replace( input, "<%=em78%>", "$G," );
            input = replace( input, "<%=em79%>", "[ｹﾞｰﾑ]" );
            input = replace( input, "<%=em80%>", "$OM" );
            input = replace( input, "<%=em81%>", "$F." );
            input = replace( input, "<%=em82%>", "$F-" );
            input = replace( input, "<%=em83%>", "$F/" );
            input = replace( input, "<%=em84%>", "$P9" );
            input = replace( input, "<%=em85%>", "$P;" );
            input = replace( input, "<%=em86%>", "$G0" );
            input = replace( input, "<%=em87%>", "$G1" );
            input = replace( input, "<%=em88%>", "$G2" );
            input = replace( input, "<%=em89%>", "$FX" );
            input = replace( input, "<%=em90%>", "$FW" );
            input = replace( input, "<%=em91%>", "$QV" );
            input = replace( input, "<%=em92%>", "$G'" );
            input = replace( input, "<%=em93%>", "[ﾒｶﾞﾈ]" );
            input = replace( input, "<%=em94%>", "$F*" );
            input = replace( input, "<%=em95%>", "●" );
            input = replace( input, "<%=em96%>", "$Gl" );
            input = replace( input, "<%=em97%>", "$Gl" );
            input = replace( input, "<%=em98%>", "$Gl" );
            input = replace( input, "<%=em99%>", "$OR" );
            input = replace( input, "<%=em100%>", "$Gr" );
            input = replace( input, "<%=em101%>", "$Go" );
            input = replace( input, "<%=em102%>", "$G<" );
            input = replace( input, "<%=em103%>", "$GS" );
            input = replace( input, "<%=em104%>", "$FY" );
            input = replace( input, "<%=em105%>", "$E$" );
            input = replace( input, "<%=em106%>", "$E#" );
            input = replace( input, "<%=em107%>", "$G+" );
            input = replace( input, "<%=em108%>", "[iﾓｰﾄﾞ]" );
            input = replace( input, "<%=em109%>", "[iﾓｰﾄﾞ]" );
            input = replace( input, "<%=em110%>", "$E#" );
            input = replace( input, "<%=em111%>", "[ﾄﾞｺﾓ]" );
            input = replace( input, "<%=em112%>", "[ﾄﾞｺﾓﾎﾟｲﾝﾄ]" );
            input = replace( input, "<%=em113%>", "$F5" );
            input = replace( input, "<%=em114%>", "$F6" );
            input = replace( input, "<%=em115%>", "$FI" );
            input = replace( input, "<%=em116%>", "$G_" );
            input = replace( input, "<%=em117%>", "$FU" );
            input = replace( input, "<%=em118%>", "[CL]" );
            input = replace( input, "<%=em119%>", "$E4" );
            input = replace( input, "<%=em120%>", "$F2" );
            input = replace( input, "<%=em121%>", "[位置情報]" );
            input = replace( input, "<%=em122%>", "$F1" );
            input = replace( input, "<%=em123%>", "$F0" );
            input = replace( input, "<%=em124%>", "[Q]" );
            input = replace( input, "<%=em125%>", "$F<" );
            input = replace( input, "<%=em126%>", "$F=" );
            input = replace( input, "<%=em127%>", "$F>" );
            input = replace( input, "<%=em128%>", "$F?" );
            input = replace( input, "<%=em129%>", "$F@" );
            input = replace( input, "<%=em130%>", "$FA" );
            input = replace( input, "<%=em131%>", "$FB" );
            input = replace( input, "<%=em132%>", "$FC" );
            input = replace( input, "<%=em133%>", "$FD" );
            input = replace( input, "<%=em134%>", "$FE" );
            input = replace( input, "<%=em135%>", "$Fm" );
            input = replace( input, "<%=em136%>", "$GB" );
            input = replace( input, "<%=em137%>", "$OG" );
            input = replace( input, "<%=em138%>", "$GC" );
            input = replace( input, "<%=em139%>", "$OG" );
            input = replace( input, "<%=em140%>", "$Gw" );
            input = replace( input, "<%=em141%>", "$Gy" );
            input = replace( input, "<%=em142%>", "$Gx" );
            input = replace( input, "<%=em143%>", "$P'" );
            input = replace( input, "<%=em144%>", "$P&" );
            input = replace( input, "<%=em145%>", "$FV" );
            input = replace( input, "<%=em146%>", "$G^" );
            input = replace( input, "<%=em147%>", "$EC" );
            input = replace( input, "<%=em148%>", "$O#" );
            input = replace( input, "<%=em149%>", "$G#" );
            input = replace( input, "<%=em150%>", "$ON" );
            input = replace( input, "<%=em151%>", "$E/" );
            input = replace( input, "<%=em152%>", "$OT" );
            input = replace( input, "<%=em153%>", "$G-" );
            input = replace( input, "<%=em154%>", "$O1" );
            input = replace( input, "<%=em155%>", "$OF" );
            input = replace( input, "<%=em156%>", "$FX" );
            input = replace( input, "<%=em157%>", "$E\\" );
            input = replace( input, "<%=em158%>", "$OW" );
            input = replace( input, "<%=em159%>", "!?" );
            input = replace( input, "<%=em160%>", "!!" );
            input = replace( input, "<%=em161%>", "$OU" );
            input = replace( input, "<%=em162%>", "$OQ" );
            input = replace( input, "<%=em163%>", "$OQ" );
            input = replace( input, "<%=em164%>", "$OP" );
            input = replace( input, "<%=em165%>", "-" );
            input = replace( input, "<%=em166%>", "-" );
            input = replace( input, "<%=em167%>", "$OD" );
            input = replace( input, "<%=em168%>", "ふくろ" );
            input = replace( input, "<%=em169%>", "ﾍﾟﾝ" );
            input = replace( input, "<%=em170%>", "人影" );
            input = replace( input, "<%=em171%>", "$E?" );
            input = replace( input, "<%=em172%>", "$Pk" );
            input = replace( input, "<%=em173%>", "[SOON]" );
            input = replace( input, "<%=em174%>", "[ON]" );
            input = replace( input, "<%=em175%>", "[end]" );
            input = replace( input, "<%=em176%>", "$GM" );
            input = replace( input, "<%=em177%>", "iｱﾌﾟﾘ" );
            input = replace( input, "<%=em178%>", "iｱﾌﾟﾘ" );
            input = replace( input, "<%=em179%>", "$G&" );
            input = replace( input, "<%=em180%>", "$OE" );
            input = replace( input, "<%=em181%>", "$O<" );
            input = replace( input, "<%=em182%>", "[ｼﾞｰﾝｽﾞ]" );
            input = replace( input, "<%=em183%>", "[ｽﾉﾎﾞ]" );
            input = replace( input, "<%=em184%>", "$OE" );
            input = replace( input, "<%=em185%>", "[ﾄﾞｱ]" );
            input = replace( input, "<%=em186%>", "$EO" );
            input = replace( input, "<%=em187%>", "$G," );
            input = replace( input, "<%=em188%>", "$E#" + "$OH" );
            input = replace( input, "<%=em189%>", "$E!" );
            input = replace( input, "<%=em190%>", "$O!" );
            input = replace( input, "<%=em191%>", "$E." );
            input = replace( input, "<%=em192%>", "$GT" );
            input = replace( input, "<%=em193%>", "[砂時計]" );
            input = replace( input, "<%=em194%>", "$EV" );
            input = replace( input, "<%=em195%>", "$OX" );
            input = replace( input, "<%=em196%>", "[腕時計]" );
            input = replace( input, "<%=em197%>", "$P#" );
            input = replace( input, "<%=em198%>", "$P*" );
            input = replace( input, "<%=em199%>", "$P5$OQ" );
            input = replace( input, "<%=em200%>", "$E(" );
            input = replace( input, "<%=em201%>", "$P6" );
            input = replace( input, "<%=em202%>", "$P." );
            input = replace( input, "<%=em203%>", "$E&" );
            input = replace( input, "<%=em204%>", "$G." );
            input = replace( input, "<%=em205%>", "$E%" );
            input = replace( input, "<%=em206%>", "$P%" );
            input = replace( input, "<%=em207%>", "$P*" );
            input = replace( input, "<%=em208%>", "$P&" );
            input = replace( input, "<%=em209%>", "$P\"" );
            input = replace( input, "<%=em210%>", "$P1" );
            input = replace( input, "<%=em211%>", "$P3" );
            input = replace( input, "<%=em212%>", "[ＮＧ]" );
            input = replace( input, "<%=em213%>", "[ｸﾘｯﾌﾟ]" );
            input = replace( input, "<%=em214%>", "$Fn" );
            input = replace( input, "<%=em215%>", "$QW" );
            input = replace( input, "<%=em216%>", "$E5" );
            input = replace( input, "<%=em217%>", "$O5" );
            input = replace( input, "<%=em218%>", "[ﾘｻｲｸﾙ]" );
            input = replace( input, "<%=em219%>", "$Fo" );
            input = replace( input, "<%=em220%>", "$Fr" );
            input = replace( input, "<%=em221%>", "[禁]" );
            input = replace( input, "<%=em222%>", "$FK" );
            input = replace( input, "<%=em223%>", "[合]" );
            input = replace( input, "<%=em224%>", "$FJ" );
            input = replace( input, "<%=em225%>", "⇔" );
            input = replace( input, "<%=em226%>", "↑↓" );
            input = replace( input, "<%=em227%>", "$Ew" );
            input = replace( input, "<%=em228%>", "$P^" );
            input = replace( input, "<%=em229%>", "$G[" );
            input = replace( input, "<%=em230%>", "$E0" );
            input = replace( input, "<%=em231%>", "[ﾁｪﾘｰ]" );
            input = replace( input, "<%=em232%>", "$O$" );
            input = replace( input, "<%=em233%>", "[ﾊﾞﾅﾅ]" );
            input = replace( input, "<%=em234%>", "$Oe" );
            input = replace( input, "<%=em235%>", "$E0" );
            input = replace( input, "<%=em236%>", "$E8" );
            input = replace( input, "<%=em237%>", "$GP" );
            input = replace( input, "<%=em238%>", "$Ob" );
            input = replace( input, "<%=em239%>", "$Gf" );
            input = replace( input, "<%=em240%>", "$O+" );
            input = replace( input, "<%=em241%>", "$O`" );
            input = replace( input, "<%=em242%>", "$OY" );
            input = replace( input, "<%=em243%>", "[ｶﾀﾂﾑﾘ]" );
            input = replace( input, "<%=em244%>", "$QC" );
            input = replace( input, "<%=em245%>", "$Gu" );
            input = replace( input, "<%=em246%>", "$G9" );
            input = replace( input, "<%=em247%>", "$Gv" );
            input = replace( input, "<%=em248%>", "$P$" );
            input = replace( input, "<%=em249%>", "$G:" );
            input = replace( input, "<%=em250%>", "$E+" );
            input = replace( input, "<%=em251%>", "$Gd" );
            input = replace( input, "<%=em252%>", "$E'" );
        }
        return(input);
    }

    /**
     * 文字列の置換を行う (API向けに改行コードを変更)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceApiBr(String input)
    {
        // HTMLエスケープされたものを戻す
        input = DBEscape( input );
        // それぞれを改行コードに変更
        input = input.replace( "\r\n", "" );
        input = input.replace( "<br/>", "\n" );
        input = input.replace( "<Br/>", "\n" );
        input = input.replace( "<BR/>", "\n" );
        input = input.replace( "<br>", "\n" );
        input = input.replace( "<Br>", "\n" );
        input = input.replace( "<BR>", "\n" );

        return input;
    }

    /**
     * 文字列の置換を行う (API向けに改行コードを変更)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceApiSpecial(String input)
    {
        // HTMLエスケープされたものを戻す
        input = DBEscape( input );
        // それぞれを変更
        input = input.replace( "&", "&amp;amp;amp;" );
        input = input.replace( "<", "&lt;" );
        input = input.replace( ">", "&gt;" );
        input = input.replace( "\"", "&quot;" );
        input = input.replace( "\'", "&apos;" );
        input = input.replace( "&amp;amp;amp;#", "&#" );
        input = input.replace( "&#165;", "￥" );
        input = input.replace( "\\", "￥" );

        return input;
    }

    /**
     * 文字列の置換を行う (ハピホテタッチ向けにエスケープ文字を全角に)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceApiFull(String input)
    {
        // HTMLエスケープされたものを戻す
        input = DBEscape( input );
        // それぞれを変更
        input = input.replace( "<", "＜" );
        input = input.replace( ">", "＞" );
        input = input.replace( "\"", "”" );
        input = input.replace( "\'", "’" );
        input = input.replace( "&amp;amp;amp;#", "&#" );
        input = input.replace( "&#165;", "￥" );
        input = input.replace( "\\", "￥" );
        input = input.replace( "&", "＆" );

        return input;
    }

    /**
     * 文字列の置換を行う (API向けに改行コードを変更)
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static public String replaceApiBr2Space(String input)
    {
        // HTMLエスケープされたものを戻す
        input = DBEscape( input );
        // それぞれを改行コードに変更
        input = input.replace( "\r\n", "" );
        input = input.replace( "<br/>", "\n" );
        input = input.replace( "<Br/>", "\n" );
        input = input.replace( "<BR/>", "\n" );
        input = input.replace( "<br>", "\n" );
        input = input.replace( "<Br>", "\n" );
        input = input.replace( "<BR>", "\n" );
        input = input.replace( "\n", " " );

        return input;
    }

    /**
     * MD5変換処理
     * 
     * @param message メッセージ
     * @return 処理結果(null：MD5変換失敗)
     **/
    static public String replaceMd5(String message)
    {
        byte[] bytePass;
        String strReturn = null;

        try
        {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            bytePass = message.getBytes();
            md.update( bytePass );
            bytePass = md.digest();

            StringBuffer buff = new StringBuffer();
            for( int i = 0 ; i < bytePass.length ; i++ )
            {
                int val = bytePass[i] & 0xff;
                if ( val < 16 )
                {
                    buff.append( "0" );
                }
                buff.append( Integer.toString( val, 16 ) );
            }
            strReturn = buff.toString();
        }
        catch ( Exception e )
        {
            Logging.error( "[ReplaceString.replaceMd5()]Exception:" + e.toString() );
        }

        return(strReturn);
    }

    /***
     * カタカナをひらがなに変換する
     * 
     * @param input カタカナの文字列
     * @return
     */
    public static String replaceKana2Hira(String input)
    {
        StringBuffer sb = new StringBuffer( input );
        for( int i = 0 ; i < sb.length() ; i++ )
        {
            char c = sb.charAt( i );
            if ( c >= 'ァ' && c <= 'ン' )
            {
                sb.setCharAt( i, (char)(c - 'ァ' + 'ぁ') );
            }
            else if ( c == 'ヵ' )
            {
                sb.setCharAt( i, 'か' );
            }
            else if ( c == 'ヶ' )
            {
                sb.setCharAt( i, 'け' );
            }
            else if ( c == 'ヴ' )
            {
                sb.setCharAt( i, 'う' );
                sb.insert( i + 1, '゛' );
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * IPアドレス変換 IPV4のアドレス（zz9.zz9.zz9.zz9）を16進表記(FFFFFFFF)に変換
     * 
     * @param ip_address
     * @return 処理結果(FFFFFFFF)
     */
    public static String replaceHexIp(String ipAddress)
    {
        String hexIp = "";
        Logging.info( ipAddress );
        String[] ip = ipAddress.split( "\\.", 4 );
        Logging.info( ip.length + "" );

        for( int i = 0 ; i < ip.length ; i++ )
        {
            Logging.info( ip[i] );
            if ( CheckString.numCheck( ip[i] ) )
            {
                String temp = Integer.toHexString( Integer.parseInt( ip[i] ) );
                if ( temp.length() == 1 )
                {
                    temp = "0" + temp;
                }
                hexIp += temp;
            }
        }
        hexIp = hexIp.toUpperCase(); // 大文字に変換
        Logging.info( hexIp );
        return(hexIp);
    }

    public static String[] arrayShuffle(String array[])
    {
        for( int i = (array.length - 1) ; 0 < i ; i-- )
        {

            // 0?(i+1)の範囲で値を取得
            int r = (int)(Math.random() * (i + 1));

            // 要素の並び替えを実行
            String tmp = array[i];
            array[i] = array[r];
            array[r] = tmp;
        }
        return array;
    }

    /**
     * メールアドレスのマスキング処理（XXXXXXXX@XXXXXXXXX）をXXX****@*******XXXに変換
     * 
     * @param mailAddress
     * @return 処理結果(String)
     */
    public static String maskedMailAddress(String mailAddress)
    {
        StringBuilder sb = new StringBuilder( mailAddress );

        for( int i = 0 ; i < sb.length() ; i++ )
        {
            if ( i < 3 || i >= sb.length() - 3 || sb.charAt( i ) == '@' )
            {
                sb.setCharAt( i, sb.charAt( i ) );
            }
            else
            {
                sb.setCharAt( i, '*' );
            }
        }
        return sb.toString();
    }
}
