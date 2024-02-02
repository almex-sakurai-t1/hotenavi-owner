<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    NumberFormat    nf;
    nf = new DecimalFormat("00");
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
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,1);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>���v����(<%=hotelid%>)�N���w��</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<jsp:include page="header_hotelname.jsp" flush="true" />
<jsp:include page="salesget.jsp" flush="true" />
<hr>
<%
    if (HotelIdCheck)
    {
        if (ownerinfo.Addupdate == 0)
        {
            ownerinfo.sendPacket0100(1,hotelid);
        }
        // ���ݓ��t
        String nowDate = getCurrentDateString();
        int now_date   = Integer.parseInt(nowDate);
        // �v����t
        int addup_date  = ownerinfo.Addupdate;
        if (addup_date == 0)
        {
            addup_date = now_date;
        }
%>
<%-- ���v����I�� --%>
<a name="monthly"></a>
�����v����<br>
<a href="<%= response.encodeURL("salesdisp.jsp?HotelId=" + hotelid + "&Day=0") %>">�{��</a>�i<%= addup_date / 10000 %>/<%= nf.format(addup_date / 100 % 100) %>�j<br>
<%-- ���t�I���A�C�e����\�����܂� --%>
<jsp:include page="salesselectmonth.jsp" flush="true" />
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
