<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    int         i;
    Cookie      hhCookie;
    // クッキー削除
    hhCookie = new Cookie("ownnid", "");
    hhCookie.setPath( "/" );
    hhCookie.setDomain( ".hotenavi.com" );
    response.addCookie( hhCookie );
%>

