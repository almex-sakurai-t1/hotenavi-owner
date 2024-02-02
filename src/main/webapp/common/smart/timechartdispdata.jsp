<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- 現在タイムチャート情報表示処理 --%>

<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    int i = 0;
    int j = 0;
    int now_ymd;
    int now_time;
    DateEdit df;
    NumberFormat nf;
    String roomtag[];
    int count = ownerinfo.TimeChartRoomCount;

    // ホテルIDのセット
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }

    roomtag = new String[count];
    df      = new DateEdit();
    nf      = new DecimalFormat("00");

    // 現在日付・時刻取得(YYYYMMDD / HHMMSS)
    now_ymd  = Integer.parseInt(df.getDate(2));
    now_time = Integer.parseInt(df.getTime(1));

    // 部屋枠タグの初期化
    for( i = 0 ; i < count ; i++ )
    {
        roomtag[i] = "&nbsp;";
    }

    int start_j = 0;
    int cal_time = now_time/100 - ownerinfo.TimeChartStartTime;
    if (cal_time < 0)
    {
        cal_time = cal_time + 2400;
    }
    start_j = cal_time/100 - 5;
    String start_time = request.getParameter("StartTime");
    if (start_time != null)
    {
        start_j = Integer.parseInt(start_time);
    }
    if (start_j > cal_time/100 - 5)
    {
        start_j = cal_time/100 - 5;
    }
    if (start_j < 0)
    {
        start_j = 0;
    }

    for( i = 0 ; i < count ; i++ )
    {
        roomtag[i] = "<td align=center nowrap>";
        // 部屋名称
        roomtag[i] = roomtag[i] + "<a href='roominfodetail.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&rc=" + ownerinfo.TimeChartRoomCode[i] + "'>"+ ownerinfo.TimeChartRoomName[i];
        if (ownerinfo.TimeChartRoomFloor[i] != 0)
        {
            roomtag[i] = roomtag[i] + "["+ ownerinfo.TimeChartRoomFloor[i] +"]";
        }
        roomtag[i] = roomtag[i] + "</a></td>";
        for ( j = start_j*6; j < start_j*6 + 36; j++)
        {
            if (ownerinfo.TimeChartRoomStatus[i][j] != 0)
            {
                roomtag[i] = roomtag[i] + "<td width=10px title='"+ownerinfo.TimeChartStatusName[ownerinfo.TimeChartRoomStatus[i][j]-1]+"' align=center style='color:#" +  ownerinfo.TimeChartStatusForeColor[ownerinfo.TimeChartRoomStatus[i][j]-1] + ";background-color:#" + ownerinfo.TimeChartStatusColor[ownerinfo.TimeChartRoomStatus[i][j]-1] + ";";
            }
            else
            {
                roomtag[i] = roomtag[i] + "<td width=10px title='' align=center style='color:#000000;background-color:#000000;";
            }
            if (j % 6 == 0)
            {
                roomtag[i] = roomtag[i] + "border-left:1px solid #000066;";
            }
            roomtag[i] = roomtag[i] + "'>&#9608;</td>";
        }
    }
%>

<h2><%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>現在
<font size=-1><a href="#color_name" style="text-decoration:none;float:right;clear:both">[色説明]</a></font></h2>
<a name="time_chart"></a>
<hr class="border">
<div>
<%
    if (start_j != 0)
    {
%>
<a href="?HotelId=<%= URLEncoder.encode(hotelid, "Windows-31J") %>&StartTime=<%= start_j - 6 %>" style="text-decoration:none;">＜＜</a>　
<%
    }
%>
<%
    if (start_j <  cal_time/100 - 5)
    {
%>
<a href="?HotelId=<%= URLEncoder.encode(hotelid, "Windows-31J") %>&StartTime=<%= start_j + 6 %>"  style="text-decoration:none;float:right;clear:both">＞＞</a>
<%
    }
%>
</div>
<%-- 現在タイムチャート情報表示 --%>
<table class="timechart" >
<tr>
<th nowrap style="text-align:center">部屋</th>
<%
    int time_title = ownerinfo.TimeChartStartTime + start_j*100;
    for( i = start_j ; i < start_j+6 ; i++ )
    {
        if (time_title > 2400)
        {
            time_title = time_title - 2400;
        }
%>
<th colspan=6><%=nf.format(time_title/100)%>:<%=nf.format(time_title%100)%></th>
<%
        time_title = time_title+100;
    }
%>
</tr>
<%
    for( i = 0 ; i < count ; i++ )
    {
%>
              <tr valign="middle">
                <%= roomtag[i] %>
              </tr>
<%
    }
%>
</table>
<hr class="border">
<table>
<a name="color_name"></a>

<%
    for( i = 0; i < 52; i++)
    {
        if(!ownerinfo.TimeChartStatusName[i].equals(""))
        {
%>
<tr align="center">
<td style="color:#<%= ownerinfo.TimeChartStatusForeColor[i] %>;background-color:#<%= ownerinfo.TimeChartStatusColor[i] %>">
<font size=-1>&#9608;&#9608;</font>
</td>
<td>
<font size=-1><%= ownerinfo.TimeChartStatusName[i] %></font>
</td>
</tr>
<%
        }
    }
%>
<font size=-1><a href="#time_chart" style="text-decoration:none;float:right;clear:both">[タイムチャートTOP]</a></font>
</table>
<hr class="border">
<%-- 現在タイムチャート情報表示ここまで --%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="timechart.jsp" />
  <jsp:param name="dispname" value="タイムチャート" />
</jsp:include>
