<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- 部屋ｽﾃｰﾀｽ遷移表示処理 --%>
<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }

    int             i;
    boolean         ret;
    NumberFormat    nf;
    StringFormat    sf;

    nf = new DecimalFormat("00");
    sf = new StringFormat( );
%>
<h2><%= ownerinfo.RoomHistoryDate / 10000 %>/<%= nf.format(ownerinfo.RoomHistoryDate / 100 % 100) %>/<%= nf.format(ownerinfo.RoomHistoryDate % 100) %>分<font size="-1"><a href="#DateSelect">[日付選択]</a></font></h2>
<hr class="border">
<%-- 部屋数表示 --%>
<table class="uriage">
<tr>
<th width="50%">部屋数</th><td class="uriage"><%= ownerinfo.RoomHistoryRoomCount %></td>
</tr>
</table>
<hr class="border" />
<table class="roomdetail2">
<tr>
<th class="roomdetail3">時間</th><th class="roomdetail3">空</th><th class="roomdetail3">在</th><th class="roomdetail3">準</th><th class="roomdetail3">止</th><th class="roomdetail3">IN</th><th class="roomdetail3">OT</th>
</tr>
<%
    int in_total = 0;
    int out_total = 0;
    for( i = 0 ; i < 24 ; i++ )
    {
		in_total = in_total + ownerinfo.InOutIn[i];
		out_total = out_total + ownerinfo.InOutOut[i];
%>
<tr>
<td class="roomdetail2"><%= nf.format(ownerinfo.RoomHistoryTime[i] / 100) %>時</td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryEmpty[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryExist[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryClean[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.RoomHistoryStop[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutIn[i]), 2) %></td><td class="roomdetail2"><%= sf.rightFitFormat(Integer.toString(ownerinfo.InOutOut[i]), 2) %></td></tr>
<%
        if( i == 11 )
        {
%>
<tr>
<th class="roomdetail3">時間</th><th class="roomdetail3">空</th><th class="roomdetail3">在</th><th class="roomdetail3">準</th><th class="roomdetail3">止</th><th class="roomdetail3">IN</th><th class="roomdetail3">OT</th>
</tr>
<%
        }
    }
%>
<tr>
<th class="roomdetail3">合計</th><th class="roomdetail3">&nbsp;</th>
<th class="roomdetail3">&nbsp;</th>
<th class="roomdetail3">&nbsp;</th>
<th class="roomdetail3">&nbsp;</th>
<th class="roomdetail4"><%= in_total %></th><th class="roomdetail4"><%= out_total %></th>
</tr>
</table>
<%-- 部屋数表示ここまで --%>
<hr class="border">
<a name="DateSelect"></a>
<%-- 日付選択画面表示 --%>
<jsp:include page="salesselectday.jsp" flush="true" >
  <jsp:param name="Contents" value="roomhistory" />
</jsp:include>
<%-- 日付選択画面表示ここまで --%>

<!-- 組数表示ここまで -->
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="jumpurl" value="roomhistory.jsp" />
  <jsp:param name="dispname" value="部屋ｽﾃｰﾀｽ遷移" />
</jsp:include>

