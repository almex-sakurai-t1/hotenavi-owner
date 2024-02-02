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
    NumberFormat    nf;
    nf = new DecimalFormat("00");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�Ǘ��X�܏��(����)�N���w��</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
function dateSelect(startDate,endDate){
    document.form1.Year.value = Math.floor(startDate / 10000);
    document.form1.Month.value = Math.floor(startDate / 100) % 100;
    document.form1.Day.value = startDate % 100;
    document.form1.submit();
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
</head>
<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">
<jsp:include page="header.jsp" flush="true" />
<h2>���Ǘ��X�ܔ�����</h2>
<jsp:include page="allstoresalesget.jsp" flush="true" />
<%-- �Ǘ��X�ܔ�����擾�����܂� --%>
<%
    // ���ݓ��t
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);

    // �v����t
    int addup_date  = ownerinfo.Addupdate;
    if (addup_date == 0)
    {
        addup_date = now_date;
    }
%>
<ul class="link">
<li><a href="<%= response.encodeURL("allstoresales.jsp?Year="+(addup_date/10000)+"&Month="+(addup_date/100%100)+"&Day=0") %>">�����i<%= addup_date / 10000 %>/<%= nf.format(addup_date / 100 % 100) %>)</a></li>
</ul>
<form name="form1" action="<%= response.encodeURL("allstoresales.jsp") %>" method="post">
<input type="hidden" name="Year" value="<%= addup_date/10000%>">
<input type="hidden" name="Month" value="<%= addup_date/100%100%>">
<input type="hidden" name="Day" value="<%= addup_date%100%>">
<%-- ���t�I����ʕ\�� --%>
<jsp:include page="allstoreselectmonth.jsp" flush="true" />
<%-- ���t�I����ʕ\�������܂� --%>
</form>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
