<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
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
    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String Range   = request.getParameter("Range");
    if (Range != null)
    {
        RangeFlag = true; 
    }
    String day   = request.getParameter("Day");
    if (day != null)
    {
      if(day.equals("0")) 
      {
          MonthFlag = true; 
      }
    }
    String month   = request.getParameter("Month");
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�Ǘ��X�܏��(����)<%if(day==null){%>�{����<%}else if(day.equals("-1")){%>�O��v�����<%}else if(month == null){%>�{����<%}else{%><%=year%>/<%=month%><%if(!day.equals("0")){%>/<%=day%><%}%><%if( endyear != null && endmonth != null && endday != null){%>�`<%=endyear%>/<%=endmonth%>/<%=endday%><%}%>��<%}%></title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
�Ǘ��X�ܔ�����<%if(day==null){%><font size=1>�i�{�����j</font><%}else if(day.equals("-1")){%><font size=1>�i�O��v������j</font><%}else if(month == null){%><font size=1>�i�{�����j</font><%}%><br>
<%-- �Ǘ��X�ܔ�����擾 --%>
<jsp:include page="allstoresalesget.jsp" flush="true" />
<%-- �Ǘ��X�ܔ�����擾�����܂� --%>
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
<%= ownerinfo.SalesGetStartDate / 10000 %>/<%= nf.format(ownerinfo.SalesGetStartDate / 100 % 100) %><%if(!MonthFlag){%>/<%= nf.format(ownerinfo.SalesGetStartDate % 100) %><%}%><%
        if (RangeFlag && ownerinfo.SalesGetEndDate != ownerinfo.SalesGetStartDate)
        {
%>�`<%= ownerinfo.SalesGetEndDate / 10000 %>/<%= nf.format(ownerinfo.SalesGetEndDate / 100 % 100) %>/<%= nf.format(ownerinfo.SalesGetEndDate % 100) %>
<%
        }
%>�v�㕪<font size=1>[<a href="#DateSelect"><%if(MonthFlag){%>�N��<%}else{%>���t<%}%>�I��</a>]</font><br>
<hr>
<%-- �Ǘ��X�ܔ�����\�� --%>
<jsp:include page="allstoresalesdispdata.jsp" flush="true" />
<%-- �Ǘ��X�ܔ�����\�������܂� --%>
<hr>
<a name="DateSelect"></a>
<% 
        if(MonthFlag)
        {
%>
<%-- �N���I����ʕ\�� --%>
<jsp:include page="allstoreselectmonth.jsp" flush="true" />
<%-- �N���I����ʕ\�������܂� --%>
<%
        }
        else if (RangeFlag)
        {
%>
<%-- ���t�͈͑I����ʕ\�� --%>
<jsp:include page="allstoreselectrange.jsp" flush="true" />
<%-- ���t�͈͑I����ʕ\�������܂� --%>
<%
        }
        else
        {
%>
<%-- ���t�I����ʕ\�� --%>
<jsp:include page="allstoreselectday.jsp" flush="true" />
<%-- ���t�I����ʕ\�������܂� --%>
<%
        }
%>
<%
    }
%>
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
