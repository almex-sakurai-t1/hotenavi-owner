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
	boolean    dateError = false;
    // �z�e��ID�̃Z�b�g
    hotelid = ReplaceString.getParameter(request,"HotelId");
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
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,1);
    }
    String day   = ReplaceString.getParameter(request,"Day");
    String month = ReplaceString.getParameter(request,"Month");
    String year  = ReplaceString.getParameter(request,"Year");
    String endyear  = ReplaceString.getParameter(request,"EndYear");
    String endmonth = ReplaceString.getParameter(request,"EndMonth");
    String endday   = ReplaceString.getParameter(request,"EndDay");
    String[] dates = new String[]{ year, month, day, endyear, endmonth, endday };
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
<title>���Ԏw����ʈꗗ(<%=hotelid%>)<%=year%>/<%=month%>/<%=day%>�`<%=endyear%>/<%=endmonth%>/<%=endday%></title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
        if (ownerinfo.Addupdate == 0)
        {
            ownerinfo.sendPacket0100(1,hotelid);
        }
%>
<%-- �����Ŕ�����擾���܂��i�Ƃ肠�������v�E���t�͈͂��擾���邽�߁j--%>
<jsp:include page="salesget.jsp" flush="true" />
<%-- �����Ŕ�����擾���܂��i�����܂Łj --%>

�����Ԏw����ʔ���<br>
<%
        if( ownerinfo.SalesGetStartDate == 0 )
        {
%>
�擾�ł��܂���ł���<br>
<%
        }
        else
        {
%>
<%-- ���ʔ���ꗗ���f�[�^�̕\�� --%>
<jsp:include page="salesrangelistdispdata.jsp" flush="true" />
<%-- ���ʔ���ꗗ���f�[�^�̕\�������܂� --%>
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
