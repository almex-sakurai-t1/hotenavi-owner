<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    int         i;
    Cookie      hhCookie;
    // �N�b�L�[�폜
    hhCookie = new Cookie("ownnid", "");
    hhCookie.setPath( "/" );
    hhCookie.setDomain( ".hotenavi.com" );
    response.addCookie( hhCookie );
%>

