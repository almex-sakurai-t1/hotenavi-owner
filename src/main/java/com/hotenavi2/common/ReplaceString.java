/*
 * @(#)ReplaceString.java 2.00 2004/12/02
 * Copyright (C) ALMEX Inc. 2004
 * 文字列置換クラス
 */

package com.hotenavi2.common;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

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
    private static final long serialVersionUID = -6389335370791566001L;

    /**
     * 文字列の置換を行う
     * 
     * @param input 処理の対象の文字列
     * @param pattern 置換前の文字列
     * @oaram replacement 置換後の文字列
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
     * @oaram replacement 置換後の文字列
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
     * SQLクエリー文字列の置換を行う
     * ('→'')
     * (\→\\)
     * 
     * @param input 処理の対象の文字列
     * @param pattern 置換前の文字列
     * @oaram replacement 置換後の文字列
     * @return 置換処理後の文字列
     */
    static public String SQLEscape(String input)
    {
        boolean escapecode;

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
            if ( input.lastIndexOf( "Ⅸ" ) == input.length() - 1 )
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

            // TMLエスケープされているものを元に戻す
            input = replace( input, "&amp;", "&" );
            input = replace( input, "&lt;", "<" );
            input = replace( input, "&gt;", ">" );
            input = replace( input, "&quot;", "\"" );
            input = replace( input, "&#39;", "'" );
            input = replace( input, "&#160;", " " );
        }

        return(input);
    }

    /**
     * HTML文字列の置換を行う
     * (&→&amp;)
     * (<→&lt;)
     * (>→&gt;)
     * ("→&quot;)
     * 
     * @param input 処理の対象の文字列
     * @param pattern 置換前の文字列
     * @oaram replacement 置換後の文字列
     * @return 置換処理後の文字列
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
