<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %><jsp:useBean id="roominfo" scope="session" class="com.hotenavi2.room.RoomInfo" />
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // ホテルID取得
    String disp_type = request.getParameter("DispType");
if  (disp_type == null)
	{
	disp_type = "0";
	}
   
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
     hotelid = (String)session.getAttribute("SelectHotel");
    }
    if( hotelid == null )
    {
%>
 
        <jsp:forward page="error/error.html" />
<%
    }
				String		query;
				String		query_user;
				java.sql.Date start;
				java.sql.Date end;
				Calendar now = Calendar.getInstance();

				int event_id;
				int room_no_sv = 0;
				int yy;
				int mm;
				int dd;
				int start_date;
				int end_date;
				int now_date;
				now_date = now.get(now.YEAR)*10000 + (now.get(now.MONTH)+1)*100 + now.get(now.DATE);
                String InputDate = request.getParameter("InputDate");
                if    (InputDate != null)  now_date = Integer.parseInt(InputDate);
				int last_update;
				int last_uptime;
				int i;
				int seq = 0;
				boolean sync_flag = false;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>部屋情報</title>
<link href="http://www.hotenavi.com//<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
<!--
function dispAllFind(){
	var targets = document.getElementsByName("honbun");
	var array = targets.length;
	for(var i = 0; i < array ; i++){
		targets[i].style.display = "block";
	}
}

function dispAllClose(){
	var targets = document.getElementsByName("honbun");
	var array = targets.length;
	for(var i = 0; i < array ; i++){
		targets[i].style.display = "none";
	}
}

function disp_find(id,obj) {
	target = document.all(id);
	if(target.style.display == "none"){
		target.style.display = "block";
	} else {
		target.style.display = "none";
	}
}

function MM_Copy(hotelid,disptype) {
if  (hotelid=="<%=hotelid%>")
	{
	alert("IDと種類を変更していないので、コピーできません！！");
	}
	else
	{  
	location.href="room_copy_input2.jsp?HotelIdInput=<%=hotelid%>&HotelId=" + hotelid + "&DispType=" + disptype;
	}
}
-->
</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			

				<tr>
                    <td colspan="2">
                      <form action="room_edit_form2.jsp?HotelId=<%= hotelid %>&Id=0" method=POST>
                        <input name="submit00" type=submit value="新しく部屋を追加" >
                      </td></form>
                    <td colspan="2">
                      <form action="room_list2.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
                        <input name="InputDate" id="InputDate" type=text size=9 maxlength=8 style="ime-mode:disable" value="<%=now_date%>">
                        <input name="submit01" type=submit value="対象日付で表示する" >
                      </td></form>
                    <td colspan="1">
                      <form action="http://www.hotenavi.com/<%=hotelid%>/priceinfo.jsp" method=POST target="_blank">
                        <input name="date" id="websitedate" type=hidden value="<%=now_date%>">
                        <input name="hotel" id="websitehotel" type=hidden value="<%=hotelid%>">
                        <input name="submit02" type=submit value="WebSite" onclick="document.getElementById('websitedate').value=document.getElementById('InputDate').value;">
                      </td></form>
<%
if (disp_type.compareTo("0") == 0)
{
%>
                    <td colspan="3">
                      <form action="room_list2.jsp?HotelId=<%= hotelid %>&DispType=1" method=POST>
                        <input name="submit01" type=submit value="非表示分も表示する" >
                    </td></form>
<%
}else{
%>
                    <td colspan="5">
                      <form action="room_list2.jsp?HotelId=<%= hotelid %>&DispType=0" method=POST>
                        <input name="submit01" type=submit value="表示分のみ表示する" >
                    </td></form>
<%
}
%>
				</tr>
                  <tr>
                    <td colspan="8">
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
						  <form name=form1 method=post action="room_copy_input2.jsp">
						    <td>
						      編集ID    : <input name="HotelIdInput" type=hidden value="<%= hotelid %>" ><strong><%=hotelid%></strong>
							  <% if (!ownerinfo.DbLoginUser.equals("i-enter")){%>
							  （コピー先ID: <input name="hotelid" type=text value="<%= hotelid %>" size="10">
							  <input type="button" onClick="MM_Copy(document.form1.hotelid.value,<%=disp_type%>)" value="コピー">）
							  <% } %>
							  <input type="button" value="全て展開" onclick="dispAllFind()">
							  <input type="button" value="全て閉じる" onclick="dispAllClose()">
							</td>
						  </form>
    		              <td align="right">
							<a href="check_roominfo.jsp?id=<%=hotelid%>" target="_blank" style="color:#7200ff;text-decoration: none;" onClick="window.open('check_roominfo.jsp?id=<%=hotelid%>&ftp=DCGXU5AC','','scrollbars=no,toolbar=no,status=yes,width=800,resizable=yes');return(false)">コンテンツ参照</a>
						  </td>
                        </tr>
                      </table>
                    </td>
                  </tr>

<%
				String user_name = "";
				// MySQLへの接続を確立する
				Connection        connection  = null;
				PreparedStatement prestate    = null;
				ResultSet         result      = null;
				connection  = DBConnection.getConnection();

    int      roomCountHost = 0;
    int      roomCount     = 1;
    int      room_i;
    int[]    Seq;
    int[]    roomNo;
    String[] roomName;
    String[] roomNameHost;
    boolean[] roomNameCheck;
    boolean  hostCheck = true;
    roominfo.HotelId = hotelid;
    if (!roominfo.sendPacket0214())
    {
        roominfo.InfoRoomListTotalRooms =0;
    }
    query = "SELECT seq,room_no,room_name,room_name_host FROM hh_hotel_room_more WHERE id IN (SELECT id FROM hh_hotel_basic WHERE hotenavi_id = '" + hotelid + "')";
    query = query + " AND hh_hotel_room_more.seq !=0";
    query = query + " AND hh_hotel_room_more.disp_flag<=1";
    prestate    = connection.prepareStatement(query);
    result      = prestate.executeQuery();
    if (result != null)
    {
        if ( result.last() != false )
        {
            roomCount = result.getRow();
            result.beforeFirst();
        }
    }
    Seq      = new int[roomCount];
    roomNo   = new int[roomCount];
    roomName = new String[roomCount];
    roomNameHost = new String[roomCount];
    roomNameCheck = new boolean[roomCount];
    if (roomCount != 0 &&  roominfo.InfoRoomListTotalRooms != 0)
    {
        room_i   = -1;
        while( result.next() != false )
        {
            room_i++;
            Seq[room_i]    = result.getInt("seq");
            roomNo[room_i]    = result.getInt("room_no");
            roomName[room_i]  = result.getString("room_name");
            roomNameHost[room_i]  = result.getString("room_name_host");
            roomNameCheck[room_i] = false;
            for (int j=0; j < roominfo.InfoRoomListTotalRooms; j++ )
            {
                if (roomNameHost[room_i].equals(roominfo.InfoRoomListRoomName[j]))
                {
                    roomNameCheck[room_i] = true;
                    break;
                }
            }
            if (!roomNameCheck[room_i])
            {
                hostCheck = false;
            }
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);
%>
<%
				Connection        connection_user  = null;
				PreparedStatement prestate_user    = null;
				ResultSet         result_user      = null;
				connection_user  = DBConnection.getConnection();

				query = "SELECT * FROM room WHERE hotelid='" + hotelid + "'";
			if (disp_type.compareTo("1") != 0)
			{
				query = query + " AND start_date<=" + now_date;
				query = query + " AND end_date>=" + now_date;
				query = query + " AND disp_flg=1";
			}
				query = query + " ORDER BY room_no,id DESC";

				prestate = connection.prepareStatement(query);
				result   = prestate.executeQuery();
				while( result.next() != false )
				{
                    if(result.getInt("sync_flag") == 1)
                    {
							sync_flag = true;
                    }
					if(result.getInt("room_no") != room_no_sv)
					{
						room_no_sv = result.getInt("room_no");
						event_id =  result.getInt("id");
						// 時間判定の準備
						start = result.getDate("start_date");
						yy = start.getYear();
						mm = start.getMonth();
						dd = start.getDate();
						start_date = (yy+1900)*10000 + (mm+1)*100 + dd;
			            int start_yy = yy;
				        int start_mm = mm;
			            int start_dd = dd;

						end = result.getDate("end_date");
						yy = end.getYear();
						mm = end.getMonth();
						dd = end.getDate();
						end_date = (yy+1900)*10000 + (mm+1)*100 + dd;
			            int end_yy = yy;
			            int end_mm = mm;
			            int end_dd = dd;
						yy = now.get(Calendar.YEAR);
						mm = now.get(Calendar.MONTH);
						dd = now.get(Calendar.DATE);
						now_date = (yy)*10000 + (mm+1)*100 + dd;

						if(( disp_type.compareTo("1") != 0 && result.getInt("disp_flg") == 1 && now_date <= end_date ) || (disp_type.compareTo("1") == 0))
						{

 						   // ユーザ一覧・セキュリティ情報取得
  							query_user = "SELECT * FROM owner_user WHERE hotelid='" + result.getString("upd_hotelid") + "'";
    						query_user = query_user + " AND userid='" + result.getString("upd_userid") + "'";
							prestate_user = connection_user.prepareStatement(query_user);
							result_user   = prestate_user.executeQuery();
						    if( result_user != null )
   		 						{
       						 	if (result_user.next() != false )
									{
									user_name = result_user.getString("name");
									}
								else
									{
									user_name = "不明";
									} 
								}

						seq++;
%>
<% 
if (seq % 4 == 1)
{
%>
				<tr valign="top">
<%
}
%>
					<td  colspan="2">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)">
								<input type="hidden" name="id_<%= seq %>" value="<%= result.getString("id") %>">
								<td align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %>" class="subtitle_text">
									<div class="subtitle_text">
									<input name="room_no_<%= seq %>" type=hidden value="<%= result.getString("room_no") %>" ><%= result.getString("room_no") %>:
									<a href="room_edit_form2.jsp?HotelId=<%= hotelid %>&Id=<%= result.getInt("id") %>"><%= result.getString("room_name") %></a>ﾗﾝｸ:<%= result.getInt("rank") %>
									<br><%= start_yy+1900 %>/<%= start_mm+1 %>/<%= start_dd %>～<%= end_yy+1900 %>/<%= end_mm+1 %>/<%= end_dd %>
							<%
							String  image_mobile =  result.getString("image_mobile");
						    %><%if(image_mobile.compareTo("")== 0 || image_mobile.compareTo(" ")== 0){%><BR>部屋画像なし<%}%>

<%
        if( now_date < start_date || now_date > end_date || result.getInt("disp_flg") != 1)
        {
%>
      <br><b>表示していません</b>
<%
        }
%>
									</div>
								</td>
							</tr>
							<tr id="honbun_<%= hotelid %>_<%= event_id %>" name="honbun" style="display:none;">
								<td>
								<table width="166" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" bordercolor="#666666">
									<tr><td width="160" nowrap>
									<font color="#000000">
							<%
							String  equipment =  result.getString("equipment");
							equipment = equipment.replace("<br>","&lt;br&gt;");
							equipment = equipment.replace("\r","<BR>");
						    %>
							<%= equipment %>
									</font><br>
									</td></tr>
								</table>
							</td></tr>
						</table>
					</td>
<% 
if (seq % 4 == 0)
{
%>
				</tr><tr><td>&nbsp;</td></tr>
<%
}
%>

<%
						}
					}
				DBConnection.releaseResources(result_user);
				DBConnection.releaseResources(prestate_user);
				}
%>
<% 
if (seq % 4 != 0)
{
%>
				</tr>
<%
}
%>

<%
				DBConnection.releaseResources(connection_user);
				DBConnection.releaseResources(result);
				DBConnection.releaseResources(prestate);
%>
<% 
if (seq == 0)
{
%>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="1" height="20"></td>
				</tr>

				<tr valign="top">
					<td  colspan="1">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr id="" >
								<td>
                      <form action="room_ini_form.jsp?HotelId=<%= hotelid %>" method=POST>
                        <input name="submit99" type=submit value="ランクごと部屋情報一括作成" >
                      </td></form>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="1" height="20"></td>
				</tr>
<%
}
if (sync_flag)
{
                int hotenavi_count = 0;
                query  = "SELECT count(hh_hotel_basic.id) AS hotenavi_count FROM hh_hotel_basic ";
                query += " INNER JOIN hotel ON hotel.hotel_id = hh_hotel_basic.hotenavi_id";
                query += " AND hotel.plan IN (1,3,4)";
                query += " WHERE hh_hotel_basic.hotenavi_id='" + hotelid + "'";
                query += " GROUP BY hh_hotel_basic.hotenavi_id";
                prestate = connection.prepareStatement(query);
                result   = prestate.executeQuery();
                if( result.next() != false )
                {
                    hotenavi_count = result.getInt(1);
                }
                DBConnection.releaseResources(result);
                DBConnection.releaseResources(prestate);
%>
<script>alert("ハッピー・ホテル用データを作成してください！");</script>
<%
				query = "SELECT * FROM hh_hotel_basic WHERE hh_hotel_basic.hotenavi_id='" + hotelid + "'";
				prestate = connection.prepareStatement(query);
				result   = prestate.executeQuery();
				while( result.next() != false )
				{
%>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="1" height="20"></td>
				</tr>

				<tr valign="top">
					<td  colspan="8">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr id="" >
								<td>
<%
                    if (hotenavi_count == 1)
                    {
%>                      <form action="hh_room_input.jsp?HotelId=<%= hotelid %>&Id=<%=result.getInt("hh_hotel_basic.id")%>" method=POST>
                        <input name="submit99" type=submit value="<%=result.getString("hh_hotel_basic.name")%>（<%=result.getInt("hh_hotel_basic.id")%>）ハッピー・ホテル用データ作成" >
<%
                    }
                    else
                    {
%>						ホテナビ物件ホームページがある物件ではありません。ハッピーホテル用データ作成をして問題がないか確認してください。
<form action="hh_room_input.jsp?HotelId=<%= hotelid %>&Id=<%=result.getInt("hh_hotel_basic.id")%>" method=POST>
	<input name="submit99" type=submit value="<%=result.getString("hh_hotel_basic.name")%>（<%=result.getInt("hh_hotel_basic.id")%>）ハッピー・ホテル用データ作成" >

<%
                    }
%>
                      </td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="1" height="20"></td>
				</tr>
<%
				}
				DBConnection.releaseResources(result);
				DBConnection.releaseResources(prestate);
}
else
{
    String LinkMessage = "部屋名称参照";
    if (!hostCheck)
    {
        LinkMessage = "通信用部屋名称変更";
%>
<script>alert("ホストコンピュータと部屋名称が違います。ホスト通信用部屋名称を修正してください");</script>
<%
	}

				query = "SELECT * FROM hh_hotel_basic WHERE hh_hotel_basic.hotenavi_id='" + hotelid + "'";
				prestate = connection.prepareStatement(query);
				result   = prestate.executeQuery();
				while( result.next() != false )
				{
%>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="1" height="20"></td>
				</tr>

				<tr valign="top">
					<td  colspan="8">
						<!-- ############### サブタイトルバー ############### -->				
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="166">
							<tr id="" >
								<td>
							<a href="room_host_confirm.jsp?Id=<%=result.getInt("hh_hotel_basic.id")%>" style="color:#7200ff;text-decoration: none;">
								<%=LinkMessage%>
							</a><br>
							<a href="http://owner.hotenavi.com/happyhotel/test_room_confirm.jsp?id=<%=result.getInt("hh_hotel_basic.id")%>" target="_blank" style="color:#7200ff;text-decoration: none;" onClick="window.open('http://owner.hotenavi.com/happyhotel/test_room_confirm.jsp?id=<%=result.getInt("hh_hotel_basic.id")%>','','scrollbars=no,toolbar=no,status=yes,width=600,resizable=yes');return(false)">ハピホテ参照</a>

								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="1" height="20"></td>
				</tr>
<%
				}
				DBConnection.releaseResources(result);
				DBConnection.releaseResources(prestate);
}

				DBConnection.releaseResources(connection);
%>
                    <tr>
                      <td colspan="8">
                      <table border="0" cellspacing="0" cellpadding="0">
	                  <form name=form2 method=post action="room_str_change2.jsp?HotelId=<%=hotelid%>&DispType=<%=disp_type%>">
                        <tr>
							<td class="honbun">
							 設備
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_str rows=10 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">⇒</td>
                    		<td class="honbun">
								<textarea class=size name=new_str rows=10 cols=20 ></textarea><br>
							</td>
							<td width="120" class="honbun" rowspan=5><input name="BackUp" type="checkbox" value="1" checked>Backupを残す<br>
							<input type="submit" value="文字列変換">
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 開始日
							</td>
		                    <td class="honbun">
							<input name="moto_date" type="text" size="8" maxlength="8">(yyyymmdd)
							</td>
							<td width="20" class="honbun">⇒</td>
                    		<td class="honbun">
							  <input name="new_date" type="text" size="8" maxlength="8">(yyyymmdd)
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 ランク
							</td>
		                    <td class="honbun">
							<input name="moto_rank" type="text" size="2" maxlength="2">
							</td>
							<td width="20" class="honbun">⇒</td>
                    		<td class="honbun">
							  <input name="new_rank" type="text" size="2" maxlength="2">
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 部屋画像(PC)
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_img_pc rows=5 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">⇒</td>
                    		<td class="honbun">
								<textarea class=size name=new_img_pc rows=5 cols=20 ></textarea><br>
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 部屋画像(サムネイル)
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_thumb rows=5 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">⇒</td>
                    		<td class="honbun">
								<textarea class=size name=new_thumb rows=5 cols=20 ></textarea><br>
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 部屋画像(携帯)
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_img_m rows=5 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">⇒</td>
                    		<td class="honbun">
								<textarea class=size name=new_img_m rows=5 cols=20 ></textarea><br>
							</td>
                        </tr>
                        <tr>
							<td class="honbun">
							 部屋備考(PC)
							</td>
		                    <td class="honbun">
								<textarea class=size name=moto_memo_pc rows=5 cols=20></textarea><br>
							</td>
							<td width="20" class="honbun">⇒</td>
                    		<td class="honbun">
								<textarea class=size name=new_memo_pc rows=5 cols=20 ></textarea><br>
							</td>
                        </tr>
                      </table>
                      </td></form>
                    </tr>
			</table>
		</td>
	</tr>
	
	<tr valign="bottom">
		<td>
			<!-- copyright テーブル -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" valign="middle">
						<div class="copyright">
							Copy right &copy; imedia inc, All Rights Reserved
						</div>
					</td>
				</tr>
				<tr>
					<td><img src="http://www.hotenavi.com//<%= hotelid %>/image/spacer.gif" width="500" height="20"></td>
				</tr>
			</table>
		
		</td>
	</tr>
</table>

</body>
</html>


