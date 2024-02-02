<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page errorPage="error.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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
    NumberFormat nf = new DecimalFormat("00");
%>
<%@ include file="checkHotelId.jsp" %>
<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    String hotelid;

    boolean HotelIdCheck = true;

    // �z�e��ID�̃Z�b�g
    hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        HotelIdCheck = checkHotelId(loginHotelId, hotelid, ownerinfo.DbUserId, 1);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>���v����(<%= StringEscapeUtils.escapeHtml4(hotelid) %>)���t�w��</title>
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
<jsp:include page="header_hotelname.jsp" flush="true" />
<jsp:include page="salesget.jsp" flush="true" />
<hr class="border">
<%

    if (HotelIdCheck)
    {
        if (ownerinfo.Addupdate == 0)
        {
            ownerinfo.sendPacket0100(1, hotelid);
        }
        // ���ݓ��t
        String nowDate = getCurrentDateString();
        int now_date = Integer.parseInt(nowDate);
        // �v����t
        int addup_date = ownerinfo.Addupdate;
        if (addup_date == 0)
        {
            addup_date = now_date;
        }
%>
<%-- ���v����I�� --%>
<a name="daily"></a>
<h1>�����v����</h1>
<ul class="link">
�i<%= addup_date / 10000 %>/<%= nf.format(addup_date / 100 % 100) %>/<%= nf.format(addup_date % 100) %>�j
<li><a href="<%= response.encodeURL("salesdisp.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(addup_date/10000)+"&Month="+(addup_date/100%100)+"&Day="+(addup_date%100)) %>">�{��</a></li>
</ul>
<%-- ���t�I���A�C�e����\�����܂� --%>
<jsp:include page="salesselectday.jsp" flush="true" />
<%-- ���t�I���A�C�e����\�����܂��i�����܂Łj --%>
<%-- ���v����I��End --%>
<%
    }
    else
    {
%>
�Ǘ��X�܂ł͂���܂���B
<%
    }
%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
