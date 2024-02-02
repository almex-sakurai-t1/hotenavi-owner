<%@ page contentType="text/html; charset=Windows-31J" %>
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
<%@ include file="checkHotelId.jsp" %>
<%
    String loginHotelId =  (String)session.getAttribute("HotelId");
    String hotelid;

    boolean    HotelIdCheck = true;

    // �z�e��ID�̃Z�b�g
    hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,1);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�������ƭ�</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
%>
��������<br>
<a href="<%= response.encodeURL("salesdisp.jsp?HotelId="+ hotelid) %>">�{����</a><br>
<a href="<%= response.encodeURL("salesdisp.jsp?HotelId="+ hotelid + "&Day=-1") %>">�O��v�����</a><br>
<a href="<%= response.encodeURL("salesday.jsp?HotelId="+ hotelid) %>">���t�w��</a><br>
<a href="<%= response.encodeURL("salesdisp.jsp?HotelId="+ hotelid + "&Day=0") %>">�{����</a><br>
<a href="<%= response.encodeURL("salesmonth.jsp?HotelId="+ hotelid) %>">�N���w��</a><br>
<a href="<%= response.encodeURL("salesrange.jsp?HotelId=" + hotelid) %>">���Ԏw��</a><br>
�����ʔ�����<br>
<a href="<%= response.encodeURL("salesmonthlistdisp.jsp?HotelId=" + hotelid) %>">�����{����</a><br>
<a href="<%= response.encodeURL("salesmonthlist.jsp?HotelId=" + hotelid) %>">�����N���w��</a><br>
<a href="<%= response.encodeURL("salesrangelist.jsp?HotelId=" + hotelid) %>">���Ԏw��</a><br>
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
