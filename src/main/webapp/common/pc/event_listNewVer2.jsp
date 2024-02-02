<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%
    boolean useNewHotenaviFlg = ((Boolean) session.getAttribute("useNewHotenaviFlg")).booleanValue();
	if (useNewHotenaviFlg) {
%>
	<jsp:include page="event_list_new.jsp" flush="true"/>
<% } else { %>
	<jsp:include page="event_list_old.jsp" flush="true"/>
<% } %>