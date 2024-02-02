<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- IN/OUT組数表示処理 --%>
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
    String day   = request.getParameter("Day");
    if (day != null)
    {
        if (!CheckString.numCheck(day))
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
        // 期間指定されている場合
%>

<h2><%= (ownerinfo.InOutGetStartDate / 10000) %>/<%= (ownerinfo.InOutGetStartDate / 100 % 100) %>/<%= (ownerinfo.InOutGetStartDate % 100) %>~<br>
<%= (ownerinfo.InOutGetEndDate / 10000) %>/<%= (ownerinfo.InOutGetEndDate / 100 % 100) %>/<%= (ownerinfo.InOutGetEndDate % 100) %></h2>

<%
    }
    else
    {
        // それ以外
        if( (ownerinfo.InOutGetStartDate % 100) != 0 )
        {
%>
<h2><%= (ownerinfo.InOutGetStartDate / 10000) %>/<%= (ownerinfo.InOutGetStartDate / 100 % 100) %>/<%= (ownerinfo.InOutGetStartDate % 100) %></h2>
<%
        }
        else
        {
          MonthFlag = true; 
%>
<h2><%= (ownerinfo.InOutGetStartDate / 10000) %>/<%= (ownerinfo.InOutGetStartDate / 100 % 100) %></h2>
<%
        }
    }
%>
<h1>計上分<font size="-1"><a href="#DateSelect">[<%if(MonthFlag){%>年月<%}else{%>日付<%}%>選択]</a></font></h1>
<%
    // ホテルIDのセット
    String hotelid = request.getParameter("HotelId");
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
<hr class="border">
<%-- IN/OUT組数表示 --%>
<table class="uriage">
<tr>
<th width="50%">時間</th><th width="25%">IN</th><th width="25%">OUT</th>
</tr>
<%
    int in_total =0;
    int out_total = 0;
    for(int i = 0 ; i < 24 ; i++ )
    {
        in_total = in_total + ownerinfo.InOutIn[i];
		out_total = out_total + ownerinfo.InOutOut[i];
%>
<tr>
<td><%= nf.format(ownerinfo.InOutTime[i] / 100) %>:<%= nf.format(ownerinfo.InOutTime[i] % 100) %></td><td class="uriage"><%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutIn[i]), 4) %></td><td class="uriage"><%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutOut[i]), 4) %></td>
</tr>
<%
    }
%>
<tr>
<th>合計</th><td bgcolor="#000066" class="uriage"><font color="#ffffff"><%= in_total %></font></td><td bgcolor="#000066" class="uriage"><font color="#ffffff"><%= out_total %></font></td>
</tr>
</table>
<%-- IN/OUT組数表示ここまで --%>
<hr class="border">
<a name="DateSelect"></a>
<% 
    if(MonthFlag)
    {
%>
<%-- 年月選択画面表示 --%>
<jsp:include page="salesselectmonth.jsp" flush="true" >
  <jsp:param name="Contents" value="salesinout" />
</jsp:include>

<%-- 年月選択画面表示ここまで --%>
<%
    }
    else if (RangeFlag)
    {
%>
<%-- 日付範囲選択画面表示 --%>
<jsp:include page="salesselectrange.jsp" flush="true" >
  <jsp:param name="Contents" value="salesinout" />
</jsp:include>
<%-- 日付範囲選択画面表示ここまで --%>
<%
    }
    else
    {
%>
<%-- 日付選択画面表示 --%>
<jsp:include page="salesselectday.jsp" flush="true" >
  <jsp:param name="Contents" value="salesinout" />
</jsp:include>
<%-- 日付選択画面表示ここまで --%>
<%
    }
%>
<!-- 組数表示ここまで -->
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="jumpurl" value="salesinout.jsp" />
  <jsp:param name="dispname" value="IN/OUT組数" />
</jsp:include>

