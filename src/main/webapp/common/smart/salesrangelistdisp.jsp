<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    String loginHotelId = (String)session.getAttribute("HotelId");
    String hotelid;

    boolean HotelIdCheck = true;

    // �z�e��ID�̃Z�b�g
    hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }
    else
    {
        HotelIdCheck = checkHotelId(loginHotelId, hotelid, ownerinfo.DbUserId, 1);
    }
    String day   = request.getParameter("Day");
    String month = request.getParameter("Month");
    String year  = request.getParameter("Year");
    String endyear  = request.getParameter("EndYear");
    String endmonth = request.getParameter("EndMonth");
    String endday   = request.getParameter("EndDay");
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>���Ԏw����ʈꗗ(<%= StringEscapeUtils.escapeHtml4(hotelid) %>)<%= StringEscapeUtils.escapeHtml4(year) %>/<%= StringEscapeUtils.escapeHtml4(month) %>/<%= StringEscapeUtils.escapeHtml4(day) %>~<%= StringEscapeUtils.escapeHtml4(endyear) %>/<%= StringEscapeUtils.escapeHtml4(endmonth) %>/<%= StringEscapeUtils.escapeHtml4(endday) %></title>
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
<hr class="border">
<%
    if (HotelIdCheck)
    {
        if (ownerinfo.Addupdate == 0)
        {
            ownerinfo.sendPacket0100(1, hotelid);
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
