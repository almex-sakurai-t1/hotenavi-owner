<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�Ǘ��X�܏��(��������)���t�w��</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
</head>
<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">
<jsp:include page="header.jsp" flush="true" />
<h2>�Ǘ��X�ܕ����������</h2>
<jsp:include page="allstoreroomget.jsp" flush="true" />
<%-- �Ǘ��X�ܕ����������擾�����܂� --%>
<%-- ���t�I����ʕ\�� --%>
<jsp:include page="allstoreroomselectday.jsp" flush="true" />
<%-- ���t�I����ʕ\�������܂� --%>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
