/*
 * @(#)Cookie.java 1.00 2012/11/23 Copyright (C) ALMEX Inc. 2012 クッキー取得クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 任意のクッキーを取得するメソッド
 * </p>
 * 
 * @author S.Tashiro
 * @version 1.00 2012/11/23
 */
public class SelectCookie implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4340991519959174488L;

    /***
     * クッキー取得メソッド
     * 
     * @param request リクエスト
     * @param name クッキー名
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name)
    {
        Cookie[] cookies = null;
        Cookie ck = null;
        int i = 0;

        cookies = request.getCookies();
        if ( cookies != null )
        {
            for( i = 0 ; i < cookies.length ; i++ )
            {
                if ( cookies[i].getName().compareTo( name ) == 0 )
                {
                    ck = cookies[i];
                    break;
                }
            }
        }
        return ck;
    }

    /**
     * クッキの値取得メソッド
     * 
     * @param request リクエスト
     * @param name クッキー名
     * @see Cookieがnullの場合は、空白をセット
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name)
    {
        String ckValue = "";
        Cookie ck = null;
        ck = SelectCookie.getCookie( request, name );

        if ( ck != null )
        {
            ckValue = ck.getValue();
        }
        else
        {
            ckValue = "";
        }

        return ckValue;
    }
}
