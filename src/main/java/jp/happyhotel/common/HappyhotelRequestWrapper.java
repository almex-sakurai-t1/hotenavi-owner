/*
 * @(#)HappyhotelRequestWrapper.java 2.00 2009/02/17 Copyright (C) ALMEX Inc. 2009 HTTPRequestラッパー
 */
package jp.happyhotel.common;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HappyhotelRequestWrapper extends HttpServletRequestWrapper
{

    private static final String SANITIZE_DATA[][]       = {
                                                        { "&", "&amp;" },
                                                        { "<", "&lt;" },
                                                        { ">", "&gt;" },
                                                        { "\"", "&quot;" },
                                                        { "'", "&#39;" }
                                                        };

    private static final String NONCHECK_URLOWNER       = Url.getSslUrl() + "/owner/";
    private static final String NONCHECK_URLOWNER_HTTP  = Url.getSslUrl() + "/owner/";
    private static final String NONCHECK_URLOWNER_DEBUG = Url.getUrl() + "/_debug_/owner/";
    private static final String NONCHECK_URLENTRY       = Url.getSslUrl() + "/entry/";
    private static final String NONCHECK_URLENTRY_HTTP  = Url.getSslUrl() + "/entry/";
    private static final String NONCHECK_URLMTONET      = "https://ssl.mto-net.jp/api/";
    private static final String NONCHECK_URLMTONET_HTTP = "http://mto-net.jp/api/";

    private String              queryString             = "";
    private String              urlString               = "";
    private boolean             noCheck                 = false;

    public HappyhotelRequestWrapper(HttpServletRequest request)
    {
        super( request );

        urlString = request.getRequestURL().toString();
        if ( urlString != null )
        {
            if ( urlString.indexOf( NONCHECK_URLOWNER ) != -1 )
            {
                noCheck = true;
            }
            if ( urlString.indexOf( NONCHECK_URLENTRY ) != -1 )
            {
                noCheck = true;
            }
            if ( urlString.indexOf( NONCHECK_URLOWNER_HTTP ) != -1 )
            {
                noCheck = true;
            }
            if ( urlString.indexOf( NONCHECK_URLENTRY_HTTP ) != -1 )
            {
                noCheck = true;
            }
            if ( urlString.indexOf( NONCHECK_URLOWNER_DEBUG ) != -1 )
            {
                noCheck = true;
            }
            if ( urlString.indexOf( NONCHECK_URLMTONET ) != -1 )
            {
                noCheck = true;
            }
            if ( urlString.indexOf( NONCHECK_URLMTONET_HTTP ) != -1 )
            {
                noCheck = true;
            }
        }
        queryString = request.getQueryString();
    }

    public String getParameter(String name)
    {
        if ( noCheck == false )
        {
            return(sanitize( getRequest().getParameter( name ) ));
        }
        else
        {
            return(getRequest().getParameter( name ));
        }
    }

    public String[] getParameterValues(String name)
    {
        String[] values = getRequest().getParameterValues( name );

        if ( noCheck == false )
        {
            if ( values != null )
            {
                for( int i = 0 ; i < values.length ; i++ )
                {
                    values[i] = sanitize( values[i] );
                }
            }
        }
        return(values);
    }

    public String getQueryString()
    {
        if ( noCheck == false )
        {
            return(sanitize( queryString ));
        }
        else
        {
            return(queryString);
        }
    }

    /**
     * サニタイズ処理
     * 
     * @param input 処理の対象の文字列
     * @return 置換処理後の文字列
     */
    static String sanitize(String string)
    {
        String result = string;
        if ( result != null && result.length() > 0 )
        {
            for( int i = 0 ; i < SANITIZE_DATA.length ; i++ )
            {
                result = Pattern.compile( SANITIZE_DATA[i][0] ).matcher( result ).replaceAll( SANITIZE_DATA[i][1] );
            }
        }
        return(result);
    }
}
