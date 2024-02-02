<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>

<%
    // ƒLƒƒƒŠƒAU‚è•ª‚¯
    UserAgent agent  = new UserAgent();
    String userAgent = request.getHeader("USER-AGENT");


    int type = agent.getUserAgentType(request);
    String url = "";

    if (userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("Android") != -1 || userAgent.indexOf("iPad") != -1 )
    {
        url = "smart/index.jsp";
    }
    else
    {
        url = "index.html";
    }
    response.sendRedirect( url );
%>
<%=userAgent%>