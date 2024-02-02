<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String requestUri = request.getRequestURI();
%>
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/index_datacheck.js"></script>
<link rel="stylesheet" href="../../common/pc/style/index.css" type="text/css">
  <jsp:include page="infoNew.jsp" flush="true" />
