<%@ page contentType="text/html; charset=Windows-31J" %>
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�Ǘ��X�܏���ƭ�</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= loginHotelId %>" />
</jsp:include>
<div align="center">
�Ǘ��X�܏��
</div>
<%
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT * FROM owner_user_security WHERE hotelid = ?  AND userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result      = prestate.executeQuery();
        if (result != null)
        {
            if(result.next())
            {
                if( result.getInt("sec_level01") == 1 )
                {
%>
<hr>
��������<br>
<a href="<%= response.encodeURL("allstoresales.jsp") %>">�{����</a><br>
<a href="<%= response.encodeURL("allstoresales.jsp") %>?Day=-1">�O��v�����</a><br>
<a href="<%= response.encodeURL("allstoresalesday.jsp") %>">���t�w��</a><br>
<a href="<%= response.encodeURL("allstoresales.jsp") %>?Day=0">�{����</a><br>
<a href="<%= response.encodeURL("allstoresalesmonth.jsp") %>">�N���w��</a><br>
<a href="<%= response.encodeURL("allstoresalesrange.jsp") %>">���Ԏw��</a><br>
<%
                }
                if( result.getInt("sec_level02") == 1 )
                {
%>
<hr>
��������񗚗�<br>
<a href="<%= response.encodeURL("allstoreroomday.jsp") %>">���t�w��</a><br>
<br>
[�{����(1���Ԃ���)]<br>
<a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=3">3�X�ܒP��</a><br>
<a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=5">5�X�ܒP��</a><br>
<a href="<%= response.encodeURL("allstoreroom.jsp") %>?count=99">�S�X�ܕ\��</a><br>
(�X�ܐ�/���Ԏw��)<br>
<div align="center">
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
<input type="submit" value="�{�����w��\��">
</form>
</div>
<%
                }
            }
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
