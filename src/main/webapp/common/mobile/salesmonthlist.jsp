<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
        HotelIdCheck = checkHotelId(loginHotelId,hotelid,ownerinfo.DbUserId,1);
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�������ʔ���(<%=hotelid%>)�N���w��</title>
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
<%-- �������ʔ���I�� --%>
<a name="monthly"></a>
���������ʔ���<br>
<a href="<%= response.encodeURL("salesmonthlistdisp.jsp?HotelId=" + hotelid) %>">�{��</a>�i<%= addup_date / 10000 %>/<%= nf.format(addup_date / 100 % 100) %>�j<br>
<%-- ���t�I���A�C�e����\�����܂� --%>
<jsp:include page="salesselectmonth.jsp" flush="true">
  <jsp:param name="Contents" value="salesmonthlistdisp" />
</jsp:include>
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
