<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int             i;
    int             j;
    int             x;
    int             y;
    int             count;
    String          hotelid;
    String          hotelname;
    String          param_timechart_flag;
    int             timechart_flag = 0;
    String          roomtag[];
    String          now_date;
    String          now_time;
    DateEdit        df;
    NumberFormat    nf;
    String          amount;

    count     = ownerinfo.TimeChartRoomCount;
//    count     = 20;


    roomtag   = new String[count];
    df        = new DateEdit();
    now_date  = df.getDate(1);
    now_time  = df.getTime(2);
    nf        = new DecimalFormat("00");

    hotelid = ReplaceString.getParameter(request,"NowHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    hotelname = ReplaceString.getParameter(request,"NowHotelName");
    if( hotelname == null )
    {
        hotelname = "";
    }
    param_timechart_flag = ReplaceString.getParameter(request,"TimeChartFlag");
    if( param_timechart_flag != null )
    {
        timechart_flag = Integer.parseInt(param_timechart_flag);
    }
    // 部屋枠タグの初期化
    for( i = 0 ; i < count ; i++ )
    {
        roomtag[i] = "&nbsp;";
    }

    for( i = 0 ; i < count ; i++ )
    {
        roomtag[i] = "<TD align=center class=tableLG>";
        // 部屋名称
        roomtag[i] = roomtag[i] + "<a href='roomdetail.jsp?HotelId=" + hotelid + "&RoomCode=" + ownerinfo.TimeChartRoomCode[i] + "'>"+ ownerinfo.TimeChartRoomName[i];
        if (ownerinfo.TimeChartRoomFloor[i] != 0)
        {
            roomtag[i] = roomtag[i] + "["+ ownerinfo.TimeChartRoomFloor[i] +"]";
        }
        roomtag[i] = roomtag[i] + "</a></TD>";
        for ( j = 0; j < 144; j++)
        {
            if (ownerinfo.TimeChartRoomStatus[i][j] != 0)
            {
                roomtag[i] = roomtag[i] + "<TD width=4px title='"+ownerinfo.TimeChartStatusName[ownerinfo.TimeChartRoomStatus[i][j]-1]+"' align=center style='color:#" +  ownerinfo.TimeChartStatusForeColor[ownerinfo.TimeChartRoomStatus[i][j]-1] + ";background-color:#" + ownerinfo.TimeChartStatusColor[ownerinfo.TimeChartRoomStatus[i][j]-1] + ";";
            }
            else
            {
                roomtag[i] = roomtag[i] + "<TD width=4px title='' align=center style='color:#000000;background-color:#000000;";
            }
            if (j % 6 == 0)
            {
                roomtag[i] = roomtag[i] + "border-left:1px solid #000066;";
            }
            roomtag[i] = roomtag[i] + "border-bottom:1px solid #000066;font-size:5px;'>&#9608;</TD>";
        }
        roomtag[i] = roomtag[i] + "<TD align=center class=tableRG>";
        roomtag[i] = roomtag[i] + "<a href='roomdetail.jsp?HotelId=" + hotelid + "&RoomCode=" + ownerinfo.TimeChartRoomCode[i] + "'>"+ ownerinfo.TimeChartRoomName[i];
        if (ownerinfo.TimeChartRoomFloor[i] != 0)
        {
            roomtag[i] = roomtag[i] + "["+ ownerinfo.TimeChartRoomFloor[i] +"]";
        }
        roomtag[i] = roomtag[i] + "</a></TD>";
    }
%>

<!-- 店舗表示 --> 
<a name="<%= hotelid %>"></a> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="200" height="20" bgcolor="#22333F" class="tab"><font color="#FFFFFF"><%= hotelname %>&nbsp;利用状況</font></td>
          <td width="15" height="20" valign="bottom"><IMG src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td valign="bottom">
            <div class="navy10px">
            <img src="../../common/pc/image/spacer.gif" width="12" height="16" align="absmiddle"><a href="#pagetop" class="navy10px">&gt;&gt;このページのトップへ</a>
			</div>
          </td>
          <td height="20" valign="bottom">
            <div class="navy10px">
            </div>
          </td>
        </tr>
      </table>
    </td>
    <td width="3">&nbsp;</td>
  </tr>

<!-- ここから表 -->
  <tr>
    <td align="center" valign="top" bgcolor="#D0CED5">
      <table width="98%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="6" align="center" colspan=2><img src="../../common/pc/image/spacer.gif" width="300" height="6"></td>
        </tr>
        <tr>
          <td align="center" colspan=2>
            <table width="99%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="bar">タイムチャート</td>
                <td width="170" align="right" class="bar" nowrap style="font-size:12px">最終更新：<%=now_date%> <%=now_time%></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td colspan=2><img src="../../common/pc/image/spacer.gif" width="160" height="14"></td>
        </tr>
<%
if (count != 0)
{
%>
        <tr>
          <td align="center" valign="top">
            <table border="0" cellspacing="0" cellpadding="0">
              <tr align="center" valign="middle">
                <td class="tableLN" nowrap width="50px">部屋</td>
<%
    int time_title = ownerinfo.TimeChartStartTime;
    for( i = 0 ; i < 24 ; i++ )
    {
        if (time_title > 2400)
        {
            time_title = time_title - 2400;
        }
%>
                <td class="table<%if(i == 23){%>R<%}else{%>L<%}%>N" colspan=6 align=left style="font-size:5px;padding-left:0px"><%=nf.format(time_title/100)%>:<%=nf.format(time_title%100)%></td>
<%
        time_title = time_title+100;
    }
%>
                <td class="tableLN" nowrap width="50px">部屋</td>
              </tr>
<%
    for( i = 0 ; i < count ; i++ )
    {
%>
              <TR valign="middle">
                <%= roomtag[i] %>
              </TR>
<%
        if( (i+1)%5 == 0 && i+1 != count )
        {
%>
              <tr>
                <td colspan="1"   style="border-bottom:2px solid #000066;"></td>
                <td colspan="144" style="border-bottom:2px solid #000066;"></td>
                <td colspan="1"   style="border-bottom:2px solid #000066;"></td>
              </tr>
<%
        }
    }
%>
              <tr align="center" valign="middle">
                <td class="tableLN" nowrap width="50px"></td>
<%
    time_title = ownerinfo.TimeChartStartTime;
    for( i = 0 ; i < 24 ; i++ )
    {
        if (time_title > 2400)
        {
            time_title = time_title - 2400;
        }
%>
                <td class="table<%if(i == 23){%>R<%}else{%>L<%}%>N" colspan=6 align=left style="font-size:5px;padding-left:0px"><%=nf.format(time_title/100)%>:<%=nf.format(time_title%100)%></td>
<%
        time_title = time_title+100;
    }
%>
                <td class="tableLN" nowrap width="50px"></td>
              </tr>
<%
   if (request.getRequestURL().toString().indexOf("_debug_") != -1)
   {
%>
				<form name="roomdisp_timechart.jsp" method="post">
				<tr align="center" valign="middle">
					<td colspan="146" align="left"><input name="timechart_submit" type="submit" value="<%if (timechart_flag == 0){%>タイムチャート表示開始<%}else{%>タイムチャート表示済<%}%>"><br>
<%@ page import="java.io.File" %>
<%
        File checkFile = null;
        Connection        connection  = null;
        PreparedStatement prestate    = null;
        ResultSet         result      = null;
        String  query = "SELECT DISTINCT(hotel.hotel_id),hotel.name FROM hotel,owner_user_hotel WHERE owner_user_hotel.accept_hotelid =?";
        query = query + " AND hotel.hotel_id = owner_user_hotel.hotelid";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        while( result.next())
        {
             checkFile = new File("/hotenavi/" + result.getString("hotel.hotel_id") + "/pc/roomdisp_timechart.jsp");
%>
					<input type="radio" name="target_hotelid" value="<%=result.getString("hotel.hotel_id")%>"><%=result.getString("hotel.name")%>(<%=result.getString("hotel.hotel_id")%>)<%if(checkFile.exists()){%>*アップロード済み<%}%><br>
<%
        }
        DBConnection.releaseResources(result,prestate,connection);
%>
					</td>
				</tr>
				</form>
<%
   }
%>
            </table>
          </td>
          <td align="center" valign="top" nowrap>
            <table border="0" cellspacing="0" cellpadding="0" valign:top>
<!--ステータス名称の表示-->
<%
    for( i = 0; i < 52; i++)
    {
        if(!ownerinfo.TimeChartStatusName[i].equals(""))
        {
%>
              <tr align="left">
                <td nowrap bgcolor="#CCCCCC" height="20px">
					<div  style="margin:3 5 3 5;padding:5 0 5 0;font-size:10px;color:<%=ownerinfo.TimeChartStatusForeColor[i]%>;background-color:<%=ownerinfo.TimeChartStatusColor[i]%>">
                     &#9608;&#9608;
					</div>
                </td>
                <td nowrap bgcolor="#CCCCCC" height="20px">
					<div style="color:#000000;font-size:10px" >
                     <%=ownerinfo.TimeChartStatusName[i]%>
					</div>
                </td>
              </tr>
<%
        }
    }
%>
            </table>
          </td>
        </tr>
<%
}
else
{
%>
        <tr>
          <td align="left" valign="top" style="font-size:12px;padding-left:100px">
		  	タイムチャートを表示できません。<br><br>
			
			【原因】<br><br>
			　[タイムチャートのみ見られない場合]<br>
			　⇒ホテルコンピュータのバージョンUPが必要です。担当支店にご相談ください。<br><br>
			　[他の部屋情報も見られない場合（表示までに30秒経過）]<br>
			　⇒回線が接続されていません。接続機器や回線障害等をご確認ください。<br><br>
			　[他の部屋情報も見られない場合（すぐに表示）]<br>
			　⇒ホテルコンピュータの再起動等必要です。コールセンターもしくは担当支店に連絡してください。<br><br>
        </tr>
<%
}
%>
        <tr>
          <td colspan=2><img src="../../common/pc/image/spacer.gif" width="160" height="14"></td>
        </tr>
      </table>
    </td>
    <td width="3" valign="top" align="left" height="100%">
      <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
        </tr>
        <tr>
          <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="3" bgcolor="#999999">
      <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
        </tr>
      </table>
    </td>
    <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>

<!-- ここまで -->

