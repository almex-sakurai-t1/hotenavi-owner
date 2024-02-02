<%@ page contentType="text/html; charset=Windows-31J" %><%
    String paramRequestURI = request.getRequestURI();
    String paramServerName = request.getServerName();
    paramServerName = paramServerName.equals("localhost") ? "http://localhost:8080" : "https://" + paramServerName; 
    String URL = paramServerName + paramRequestURI;
    URL = URL.replace("index.jsp","index.html");
    response.sendRedirect(URL);
%>
