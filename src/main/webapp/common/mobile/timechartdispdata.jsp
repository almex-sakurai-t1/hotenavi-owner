<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
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
    int            i = 0;
    int            j = 0;
    int            now_ymd;
    int            now_time;
    DateEdit        df;
    NumberFormat    nf;
    String          roomtag[];
    int count     = ownerinfo.TimeChartRoomCount;
    String responseUrl ="";
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

    int start_j  = 0;
    int cal_time = now_time/100 - ownerinfo.TimeChartStartTime;
    if (cal_time < 0)
    {
        cal_time = cal_time + 2400;
    }
    start_j = cal_time/100 - 5;
    String start_time = ReplaceString.getParameter(request,"StartTime");

    if (start_time != null)
    {
        if ( !CheckString.numCheck( start_time ) )
        {
            response.sendError(400);
            return;
        }
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
        roomtag[i] = "<td><font size=1>";
        // 部屋名称
        responseUrl = "roominfodetail.jsp?HotelId=" + hotelid + "&rc=" + ownerinfo.TimeChartRoomCode[i];
        roomtag[i] = roomtag[i] + "<a href='"+response.encodeURL(responseUrl)+"'>"+ ownerinfo.TimeChartRoomName[i];
        if (ownerinfo.TimeChartRoomFloor[i] != 0)
        {
            roomtag[i] = roomtag[i] + "["+ ownerinfo.TimeChartRoomFloor[i] +"]";
        }
        roomtag[i] = roomtag[i] + "</a></font></td>";
        for ( j = start_j*6; j < start_j*6 + 36; j++)
        {
            if (j%6 ==0)
            {
                roomtag[i] = roomtag[i] + "<td>";
            }
            if (ownerinfo.TimeChartRoomStatus[i][j] != 0)
            {
                roomtag[i] = roomtag[i] + "<span style='color:#" +  ownerinfo.TimeChartStatusForeColor[ownerinfo.TimeChartRoomStatus[i][j]-1] + ";background-color:#" + ownerinfo.TimeChartStatusColor[ownerinfo.TimeChartRoomStatus[i][j]-1] + ";";
            }
            else
            {
                roomtag[i] = roomtag[i] + "<span style='color:#000000;background-color:#000000;";
            }
            roomtag[i] = roomtag[i] + "'><font size=1>*</font></span>";
            if (j%6 ==5)
            {
                roomtag[i] = roomtag[i] + "</td>";
            }
        }
    }
%>
<%= now_ymd / 100 % 100 %>/<%= now_ymd % 100 %> <%= now_time / 10000 %>:<%= now_time / 100 % 100 %>現在<br>
<hr>
<div align=right>
<font size=-1><a href="#color_name">[色説明]</a></font>
</div>
<a name="time_chart"></a>
<%-- 現在タイムチャート情報表示 --%>
<div>
<%
    if (start_j != 0)
    {
%>
<a href="?HotelId=<%=hotelid%>&StartTime=<%=start_j-6%>">＜＜</a>　
<%
    }
%>
</div>
<div align=right>
<%
    if (start_j <  cal_time/100 - 5)
    {
        responseUrl = "?HotelId="+hotelid+"&StartTime="+(start_j+6);
%>
<a href="<%= response.encodeURL(responseUrl) %>" align=right>＞＞</a>
<%
    }
%>
</div>
<%-- 現在タイムチャート情報表示 --%>
<table cellspacing=0 cellpadding=0>
<tr>
<th><font size=-1>部屋</font></th>
<%
    int time_title = ownerinfo.TimeChartStartTime + start_j*100;
    for( i = start_j ; i < start_j+6 ; i++ )
    {
        if (time_title > 2400)
        {
            time_title = time_title - 2400;
        }
%>
<th align=left><font size=-1><%=nf.format(time_title/100)%>:<%=nf.format(time_title%100)%></font></th>
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
<a name="color_name"></a>
<div align=right>
<font size=-1><a href="#time_chart">[タイムチャートTOP]</a></font>
</div>
<table>
<%
    for( i = 0; i < 52; i++)
    {
        if(!ownerinfo.TimeChartStatusName[i].equals(""))
        {
%>
<tr align="center">
<td style="color:#<%=ownerinfo.TimeChartStatusForeColor[i]%>;background-color:#<%=ownerinfo.TimeChartStatusColor[i]%>">
*
</td>
<td>
<font size=-1><%=ownerinfo.TimeChartStatusName[i]%></font>
</td>
</tr>
<%
        }
    }
%>
</table>
<%-- 現在タイムチャート情報表示ここまで --%>
<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="timechart.jsp" />
  <jsp:param name="dispname" value="タイムチャート" />
</jsp:include>

