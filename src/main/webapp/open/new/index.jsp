<%@ page contentType="text/html; charset=Windows-31J" %><%@ page errorPage="error.jsp" %><%
    String url = request.getRequestURL().toString();
    url = url.replace( "http://", "https://" );
    url = url.replace( "index.jsp", "" );
    response.sendRedirect( url );
%>