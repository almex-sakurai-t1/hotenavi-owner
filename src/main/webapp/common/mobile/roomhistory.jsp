<%@ page contentType="text/html; charset=Windows-31J" %>
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
        if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
        {
            response.sendError(400);
            return;
        }
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,2);
    }
    String day   = request.getParameter("Day");
    String month = request.getParameter("Month");
    String year  = request.getParameter("Year");
    String[] dates = new String[]{ year, month, day };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�����ð���J��(<%=hotelid%>)<%if(day==null){%>�{����<%}else if(day.equals("-1")){%>�O��v�����<%}else{%><%=year%>/<%=month%>/<%=day%>��<%}%></title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
%>
<%-- �����ð���J�ڏ��擾 --%>
<jsp:include page="roomhistoryget.jsp" flush="true"/>
<%-- �����ð���J�ڏ��擾�����܂� --%>
�������ð���J��<%if(day==null){%><font size=1>�i�{�����j</font><%}else if(day.equals("-1")){%><font size=1>�i�O��v������j</font><%}%><br>
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
<%-- �����ð���J�ڏ��\�� --%>
<jsp:include page="roomhistorydispdata.jsp" flush="true" />
<%-- �����ð���J�ڏ��\�������܂� --%>
<%
        }
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
