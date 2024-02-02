<%@ page contentType="text/html; charset=Windows-31J" %>
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
        // 期間指定されている場合
%>

<%= (ownerinfo.InOutGetStartDate / 10000) %>/<%= (ownerinfo.InOutGetStartDate / 100 % 100) %>/<%= (ownerinfo.InOutGetStartDate % 100) %>～<br>
<%= (ownerinfo.InOutGetEndDate / 10000) %>/<%= (ownerinfo.InOutGetEndDate / 100 % 100) %>/<%= (ownerinfo.InOutGetEndDate % 100) %>

<%
    }
    else
    {
        // それ以外
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
計上分<font size=1>[<a href="#DateSelect"><%if(MonthFlag){%>年月<%}else{%>日付<%}%>選択</a>]</font><br>
<%
    // ホテルIDのセット
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
<%-- IN/OUT組数表示 --%>
<pre>
 時間   I N  OUT
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
<pre>　 合計 　 <%= in_total %>　　<%= out_total %></pre><br />
<%-- IN/OUT組数表示ここまで --%>
<hr>
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

