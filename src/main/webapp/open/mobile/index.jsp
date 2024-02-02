<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="com.hotenavi2.common.*" %>

<%
    UserAgent agent = new UserAgent();
    String userAgent = request.getHeader("USER-AGENT");

    int type = agent.getUserAgentType(request);
    String url = "";

    if( type == agent.USERAGENT_DOCOMO )
    {
        url = "i/";
    }
    else if( type == agent.USERAGENT_VODAFONE )
    {
        url = "j/";
    }
    else if( type == agent.USERAGENT_AU )
    {
        url = "ez/";
    }
    else
    {
			if (userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("Android") != -1 || userAgent.indexOf("iPad") != -1 )
			{
				url = "smart/index.jsp";
			}
			else
			{
				url = "i/";
			}

    }

    url = url + "index.jsp";

    response.sendRedirect( response.encodeURL(url) );

%>
