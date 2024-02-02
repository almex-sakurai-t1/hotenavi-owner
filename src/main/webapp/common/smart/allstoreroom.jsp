<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ page errorPage="error.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>

<%
    String param_count = request.getParameter("count");
    if( param_count == null )
    {
        param_count = "3";
    }
    String param_tmcount = request.getParameter("tmcount");
    if( param_tmcount == null )
    {
        param_tmcount = "1";
    }
    String day   = request.getParameter("Day");
    String month = request.getParameter("Month");
    String year  = request.getParameter("Year");
    String[] dates = new String[]{ year, month, day, param_count, param_tmcount };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�Ǘ��X�܏��(����)<%if(day==null){%>�{����<%}else{%><%=year%>/<%=month%><%if(!day.equals("0")){%>/<%=day%><%}%>��<%}%></title>
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

<a name="Top"></a>
<h2>�Ǘ��X�܏��(����)
<font size="-1"><a href="#DateSelect">[���t�I��]</a></font></h2>
<hr class="border">
<%-- �Ǘ��X�ܕ����������擾 --%>
<jsp:include page="allstoreroomget.jsp" flush="true" />
<%-- �Ǘ��X�ܕ����������擾�����܂� --%>

<%
    if( ownerinfo.InOutGetStartDate == 0 )
    {
%>
�擾�ł��܂���ł���<br>
<%
    }
    else
    {
%>
<%-- �Ǘ��X�ܕ����������\�� --%>
<jsp:include page="allstoreroomdispdata.jsp" flush="true" />
<%-- �Ǘ��X�ܕ����������\�������܂� --%>
<%
	}
%>

<hr class="border">
<a name="DateSelect"></a>
<%-- ���t�I����ʕ\�� --%>
<jsp:include page="allstoreroomselectday.jsp" flush="true" />
<%-- ���t�I����ʕ\�������܂� --%>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
