<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�Ǘ��X�܏��(��������)���t�w��</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
�Ǘ��X�ܕ����������<br>
<jsp:include page="allstoreroomget.jsp" flush="true" />
<%-- �Ǘ��X�ܕ����������擾�����܂� --%>
<%-- ���t�I����ʕ\�� --%>
<jsp:include page="allstoreroomselectday.jsp" flush="true" />
<%-- ���t�I����ʕ\�������܂� --%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
