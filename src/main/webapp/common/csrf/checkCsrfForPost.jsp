<%@ page pageEncoding="Windows-31J" %>
<%@ page import="com.hotenavi2.common.*" %>
<%
    // Check csrf parameter
    String csrfToken = request.getParameter("csrf");
    String currentCsrf = (String)session.getAttribute("csrf");
    if (csrfToken != null)
    {
        if (currentCsrf != null && currentCsrf.equals(csrfToken))
        {
        }
        else
        {
//            response.sendError(400);
            return;
        }
    }
 %><!-- csrfToken:<%= csrfToken %><br>
 currentCsrf:<%= currentCsrf %>-->