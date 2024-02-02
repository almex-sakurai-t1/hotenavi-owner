<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%
    String paramRequestURI = request.getRequestURI();
    String paramRemoteAddr = ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() );
    String paramServer     = request.getServerName();
    String paramAgent      = request.getHeader("USER-AGENT");
    String paramReferer    = request.getHeader("REFERER");
%>
