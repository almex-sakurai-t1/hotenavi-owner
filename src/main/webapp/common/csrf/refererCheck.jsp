<%@ page pageEncoding="Windows-31J" %><%
    String referer =  request.getHeader("referer");
    if (referer == null)
    {
        response.sendError(403);
        return;
    }
    if(!referer.startsWith("https://owner.hotenavi.com/") && !referer.startsWith("http://localhost:8080/"))
    {
        response.sendError(403);
        return;
    }
%>