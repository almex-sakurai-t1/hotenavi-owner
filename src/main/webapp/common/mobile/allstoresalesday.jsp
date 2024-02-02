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
    NumberFormat    nf;
    nf = new DecimalFormat("00");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�Ǘ��X�܏��(����)���t�w��</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
���Ǘ��X�ܔ�����<br>
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
<a href="<%= response.encodeURL("allstoresales.jsp?Year="+(addup_date/10000)+"&Month="+(addup_date/100%100)+"&Day="+(addup_date%100)) %>">�{��</a>�i<%= addup_date / 10000 %>/<%= nf.format(addup_date / 100 % 100) %>/<%= nf.format(addup_date % 100) %>�j<br>
<%-- ���t�I����ʕ\�� --%>
<jsp:include page="allstoreselectday.jsp" flush="true" />
<%-- ���t�I����ʕ\�������܂� --%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
