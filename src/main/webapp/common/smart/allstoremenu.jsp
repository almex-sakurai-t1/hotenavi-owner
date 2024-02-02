<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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
    String loginHotelId =  (String)session.getAttribute("HotelId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�Ǘ��X�܏���ƭ�</title>
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
<jsp:include page="header_hotelname.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= loginHotelId %>" />
</jsp:include>
<hr class="border">
<div align="center">
<h1 class="title">�Ǘ��X�܏��</h1>
</div>
<hr class="border">
<%
    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    try
    {
        final String query = "SELECT * FROM owner_user_security WHERE hotelid = ? AND userid = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if( result.next() )
        {
            if( result.getInt("sec_level01") == 1 )
            {
%>
<h1>��������</h1>
<ul class="link">
<li><a href="<%= response.encodeURL("allstoresales.jsp") %>">�{����</a></li>
<li><a href="<%= response.encodeURL("allstoresales.jsp") %>?Day=-1">�O��v�����</a></li>
<li><a href="<%= response.encodeURL("allstoresalesday.jsp") %>">���t�w��</a></li>
<li><a href="<%= response.encodeURL("allstoresales.jsp") %>?Day=0">�{����</a></li>
<li><a href="<%= response.encodeURL("allstoresalesmonth.jsp") %>">�N���w��</a></li>
<li><a href="<%= response.encodeURL("allstoresalesrange.jsp") %>">���Ԏw��</a></li>
<li><a href="<%= response.encodeURL("allstoresalesmonthlist.jsp?Month=0") %>">���ʔ���i�{�����j</a></li>
<li><a href="<%= response.encodeURL("allstoresalesmonthlist.jsp") %>">���ʔ���i�N���w��j</a></li>
<%
            }
            if( result.getInt("sec_level02") == 1 )
            {
%>
<br>
<li>
<h1><a href="<%= response.encodeURL("allstoreroomnow.jsp") %>" style="padding: 8px 15px 18px 0px;">�����ݕ������</a></h1>
</li>
<br>
<h1>��������񗚗�</h1>
<li><a href="<%= response.encodeURL("allstoreroomday.jsp") %>">���t�w��</a></li>

<h2>[�{����(1���Ԃ���)]</h2>
<li><a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=3">3�X�ܒP��</a></li>
<li><a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=5">5�X�ܒP��</a></li>
<li><a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=99">�S�X�ܕ\��</a></li>
<h2>(�X�ܐ�/���Ԏw��)</h2>
</ul>
<hr class="border">
<div class="form" align="center">
<form action="<%= response.encodeURL("allstoreroom.jsp") %>" method="post">
<select name="count">
<option value="3" selected>3
<option value="5">5
<option value="99">�S��
</select>�X�ܒP��<br>
<select name="tmcount">
<option value="1" selected>1
<option value="2">2
<option value="3">3
</select>���Ԃ���<br>
<input type="submit" value="�{�����w��\��" id="button2">
</form>
</div>
<%
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }
%>
</ul>
<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
