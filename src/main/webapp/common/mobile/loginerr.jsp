<%@ page contentType="text/html; charset=Windows-31J" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>۸޲ݴװ</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<%
    int ret = Integer.parseInt(ReplaceString.getParameter(request,"result"));
    if( ret == 2 )
    {
%>
���O�C���ł��܂���ł����B
�[���ԍ����o�^����Ă��܂���B<br>
<%=ReplaceString.getParameter(request,"loginid")%>
<%
    }
    else if( ret == 3 )
    {
%>
���O�C���ł��܂���ł����B
�p�X���[�h���Ⴂ�܂��B<br>
<%
    }
    else
    {
%>
���O�C���ł��܂���ł����B
�G���[���������܂����B<br>
<%
    }
%>
<br>
<hr>
<div align="center">
<a href="<%= response.encodeURL("index.jsp") %>">TOP��</a><br>
</div>
<hr>
<div align="center">
Copyright(C) almex inc. All rights reserved.
</div>
</body>
</html>
