<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
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
    hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,2);
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�����ڍ׏��(<%=hotelid%>)</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
%>
<%-- �����ŕ����ڍ׏����擾���܂� --%>
<jsp:include page="roominfodetailget.jsp" flush="true" />
<%-- �����ŕ����ڍ׏����擾���܂��i�����܂Łj --%>
�������ڍ׏��<br>
<%-- �����ڍ׏��f�[�^�̕\�� --%>
<jsp:include page="roominfodetaildispdata.jsp" flush="true" />
<%-- �����ڍ׏��f�[�^�̕\�������܂� --%>
<%
    }
    else
    {
%>
�Ǘ��X�܂ł͂���܂���B
<%
    }
%>
<jsp:include page="/common/mobile/footer.jsp" flush="true" />
</body>
</html>
