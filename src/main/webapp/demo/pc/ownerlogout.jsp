<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // ƒZƒbƒVƒ‡ƒ“‘®«‚Ìíœ
    session.invalidate();
%>

<% response.sendRedirect("index.jsp"); %>

