<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- IN/OUT�g���\������ --%>
<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    StringFormat    sf;
    sf = new StringFormat();
    NumberFormat    nf;
    nf = new DecimalFormat("00");

    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String day   = ReplaceString.getParameter(request,"Day");
    if (day != null)
    {
        if ( !CheckString.numCheck( day ) )
        {
            response.sendError(400);
            return;
        }
        if(day.equals("0")) 
        {
            MonthFlag = true; 
        }
    }

    if( ownerinfo.InOutGetStartDate != 0 && ownerinfo.InOutGetEndDate != 0 && ownerinfo.InOutGetEndDate != ownerinfo.InOutGetStartDate)
    {
        RangeFlag = true; 
        // ���Ԏw�肳��Ă���ꍇ
%>

<%= (ownerinfo.InOutGetStartDate / 10000) %>/<%= (ownerinfo.InOutGetStartDate / 100 % 100) %>/<%= (ownerinfo.InOutGetStartDate % 100) %>�`<br>
<%= (ownerinfo.InOutGetEndDate / 10000) %>/<%= (ownerinfo.InOutGetEndDate / 100 % 100) %>/<%= (ownerinfo.InOutGetEndDate % 100) %>

<%
    }
    else
    {
        // ����ȊO
        if( (ownerinfo.InOutGetStartDate % 100) != 0 )
        {
%>
<%= (ownerinfo.InOutGetStartDate / 10000) %>/<%= (ownerinfo.InOutGetStartDate / 100 % 100) %>/<%= (ownerinfo.InOutGetStartDate % 100) %>
<%
        }
        else
        {
          MonthFlag = true; 
%>
<%= (ownerinfo.InOutGetStartDate / 10000) %>/<%= (ownerinfo.InOutGetStartDate / 100 % 100) %>
<%
        }
    }
%>
�v�㕪<font size=1>[<a href="#DateSelect"><%if(MonthFlag){%>�N��<%}else{%>���t<%}%>�I��</a>]</font><br>
<%
    // �z�e��ID�̃Z�b�g
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
%>
<hr>
<%-- IN/OUT�g���\�� --%>
<pre>
 ����   I N  OUT
</pre>
<%
    int in_total=0;
    int out_total=0;
    for(int i = 0 ; i < 24 ; i++ )
    {
	    in_total = in_total + ownerinfo.InOutIn[i];
		out_total = out_total + ownerinfo.InOutOut[i];
%>
<pre>  <%= nf.format(ownerinfo.InOutTime[i] / 100) %>:<%= nf.format(ownerinfo.InOutTime[i] % 100) %> <%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutIn[i]), 4) %> <%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutOut[i]), 4) %></pre>
<%
    }
%>
<pre>�@ ���v �@ <%= in_total %>�@�@<%= out_total %></pre><br />
<%-- IN/OUT�g���\�������܂� --%>
<hr>
<a name="DateSelect"></a>
<% 
    if(MonthFlag)
    {
%>
<%-- �N���I����ʕ\�� --%>
<jsp:include page="salesselectmonth.jsp" flush="true" >
  <jsp:param name="Contents" value="salesinout" />
</jsp:include>

<%-- �N���I����ʕ\�������܂� --%>
<%
    }
    else if (RangeFlag)
    {
%>
<%-- ���t�͈͑I����ʕ\�� --%>
<jsp:include page="salesselectrange.jsp" flush="true" >
  <jsp:param name="Contents" value="salesinout" />
</jsp:include>
<%-- ���t�͈͑I����ʕ\�������܂� --%>
<%
    }
    else
    {
%>
<%-- ���t�I����ʕ\�� --%>
<jsp:include page="salesselectday.jsp" flush="true" >
  <jsp:param name="Contents" value="salesinout" />
</jsp:include>
<%-- ���t�I����ʕ\�������܂� --%>
<%
    }
%>
<!-- �g���\�������܂� -->
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="jumpurl" value="salesinout.jsp" />
  <jsp:param name="dispname" value="IN/OUT�g��" />
</jsp:include>

