<%@ page contentType="text/html; charset=Windows-31J" %><%
    String paramRequestURI = request.getRequestURI();
    String paramRemoteAddr = ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() );
    String paramServer     = request.getServerName();
    String paramAgent      = request.getHeader("USER-AGENT");
    String paramReferer    = request.getHeader("REFERER");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    String projectName = "hotenavi-owner";
%>
