/*
 * @(#)SetSecurityFilter.java 2.00 2009/02/17 Copyright (C) ALMEX Inc. 2009 セキュリティ対策フィルタ
 */
package jp.happyhotel.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SetSecurityFilter implements Filter
{

    protected FilterConfig filterConfig = null;
    protected boolean      ignore       = true;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {

        ServletRequest sanitizedRequest = request;

        // リクエストに何か入っていた場合はサニタイジングする。
        if ( ignore && request instanceof HttpServletRequest && request.getParameterMap().size() > 0 )
        {
            sanitizedRequest = new HappyhotelRequestWrapper( (HttpServletRequest)request );
        }

        // 別のフィルタに値を継承
        chain.doFilter( sanitizedRequest, response );
    }

    public void init(FilterConfig config) throws ServletException
    {
        this.filterConfig = config;

        String value = filterConfig.getInitParameter( "ignore" );
        if ( value == null )
        {
            this.ignore = true;
        }
        else if ( value.equalsIgnoreCase( "true" ) )
        {
            this.ignore = true;
        }
        else if ( value.equalsIgnoreCase( "yes" ) )
        {
            this.ignore = true;
        }
        else
        {
            this.ignore = false;
        }
    }

    public void destroy()
    {
        this.filterConfig = null;
    }
}
