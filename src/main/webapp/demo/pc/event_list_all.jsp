<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>

<%
    // ホテルID取得
    String data_type = request.getParameter("DataType");
    String disp_type = request.getParameter("DispType");
    String sort_type = request.getParameter("SortType");

if  (disp_type == null)
	{
	disp_type = "0";
	}

    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="error/error.html" />
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>イベント情報</title>
<link href="http://www.hotenavi.com/demo/contents.css" rel="stylesheet" type="text/css">
</head>

<script type="text/javascript">
<!--
function disp_find(id,obj) {
	target = document.all(id);
	if(target.style.display == "none"){
		target.style.display = "block";
	} else {
		target.style.display = "none";
	}
}
-->
</script>

<%
				int yy;
				int mm;
				int dd;
				int start_date;
				int end_date;
				int now_date;
				Calendar now = Calendar.getInstance();

				yy = now.get(Calendar.YEAR);
				mm = now.get(Calendar.MONTH);
				dd = now.get(Calendar.DATE);
				int today_year  = now.get(Calendar.YEAR);
				int today_month = now.get(Calendar.MONTH);
				int today_day   = now.get(Calendar.DATE);
				now_date = (yy)*10000 + (mm+1)*100 + dd;

			    String year      = request.getParameter("Year");
			    String month     = request.getParameter("Month");
			    String day       = request.getParameter("Day");
 				int    ymd;

 				if( year != null && month != null && day != null )
   				 {
        		 ymd = (Integer.valueOf(year).intValue() * 10000) + (Integer.valueOf(month).intValue() * 100) + Integer.valueOf(day).intValue();
  				  }
				else
				{
				ymd = now_date;
				}

       			yy  = ymd / 10000;
        		mm  = (ymd / 100 % 100) - 1;
		        dd  = ymd % 100;
       			int now_year   = ymd / 10000;
        		int now_month  = (ymd / 100 % 100) - 1;
		        int now_day    = ymd % 100;

			    int[]     last_day = new int[12];
    			last_day[0]  = 31;
    			last_day[1]  = 28;
				if (((yy % 4 == 0) && (yy % 100 != 0)) || (yy % 400 == 0))
					{
					last_day[1] = 29;
					}
					else
					{
					last_day[1] = 28;
					}
				last_day[2]  = 31;
				last_day[3]  = 30;
				last_day[4]  = 31;
				last_day[5]  = 30;
				last_day[6]  = 31;
				last_day[7]  = 31;
 				last_day[8]  = 30;
				last_day[9]  = 31;
				last_day[10]  = 30;
				last_day[11]  = 31;
				int span = 7;

    
				int  pre_year  = yy;
    			int  pre_month = mm;
    			int  pre_day   = dd;
			
				if (dd == 1) 
				{
					if (mm == 0)
					 {
		 				pre_year  = yy - 1;
		 				pre_month = 11;
		 				pre_day   = 31;
		 			 }
		 			else 
	     			 {
		 				pre_month = mm - 1;	        
		 				pre_day   = last_day[mm - 1];	        
	     			 }
		      	}
 				else
				{
					pre_day = dd - 1;
				}
				int pre_ymd = pre_year * 10000 + (pre_month + 1) * 100 + pre_day;
				
			    int  adv_year  = yy;
    			int  adv_month = mm;
    			int  adv_day   = dd;
				if (last_day[mm] == dd) 
				{
					if (mm == 11)
	     			{
		 				adv_year  = yy + 1;
		 				adv_month = 0;
		 				adv_day   = 1;
		 			}
					else 
	     			{
		 				adv_month = mm + 1;	        
		 				adv_day   = 1;	        
	     			}
				}
				else
	 			{
	 			adv_day = dd + 1;
				}
				int adv_ymd = adv_year * 10000 + (adv_month + 1) * 100 + adv_day;

%>



<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			

				<tr>
<%
if (disp_type.compareTo("0") == 0)
{
%>
                    <td style="font-size:12px;">
                      <form action="event_list_all.jsp?DataType=<%= data_type %>&DispType=1&SortType=<%= sort_type %>" method=POST>
                        <input name="submit01" type=submit value="アイメディア編集分も表示する" >現在はアイメディア編集分を表示していません。
					</td>
                      </form>
<%
}
else
{
%>
                    <td style="font-size:12px;">
                      <form action="event_list_all.jsp?DataType=<%= data_type %>&DispType=0&SortType=<%= sort_type %>" method=POST>
                        <input name="submit01" type=submit value="アイメディア編集分は表示しない" >現在はアイメディア編集分も表示しています。
					</td>
                      </form>
<%
}
%>
<%
if (sort_type.compareTo("0") == 0)
{
%>
                    <td style="font-size:12px;">
                      <form action="event_list_all.jsp?DataType=<%= data_type %>&DispType=<%= disp_type %>&SortType=1" method=POST>
                        <input name="submit02" type=submit value="最終更新日付順に表示する" >現在は作成日付順に表示されています。
					</td>
                      </form>
<%
}
else
{
%>
                    <td style="font-size:12px;">
                      <form action="event_list_all.jsp?DataType=<%= data_type %>&DispType=<%= disp_type %>&SortType=0" method=POST>
                        <input name="submit02" type=submit value="作成日付順に表示する" >現在は更新時刻順に表示されています。
					</td>
                      </form>
<%
}
%>
				</tr>

				<tr>
                    <td style="font-size:12px;">
                      <a href="event_list_all.jsp?HotelId=demo&DataType=<%= data_type %>&DispType=<%= disp_type %>&SortType=<%= sort_type %>&Year=<%= pre_year %>&Month=<%= pre_month + 1 %>&Day=<%= pre_day %>" >
                        <strong>前日←</strong></a>&nbsp;&nbsp;&nbsp;
						<%= pre_year %>年<%= pre_month + 1 %>月<%= pre_day %>日～<%= now_year %>年<%= now_month + 1 %>月<%= now_day %>日編集分&nbsp;&nbsp;&nbsp;
                      <a href="event_list_all.jsp?HotelId=demo&DataType=<%= data_type %>&DispType=<%= disp_type %>&SortType=<%= sort_type %>&Year=<%= adv_year %>&Month=<%= adv_month + 1 %>&Day=<%= adv_day %>" >
                        <strong>→翌日</strong></a>
					</td>
                    <td style="font-size:12px;">
                      <form action="event_list_all.jsp?HotelId=demo&DataType=<%= data_type %>&DispType=<%= disp_type %>&SortType=<%= sort_type %>&Year=<%= today_year %>&Month=<%= today_month + 1 %>&Day=<%= today_day %>" method=POST>
                        <input name="submit13" type=submit value="本日に戻る" >
					</td>
                      </form>
				</tr>


				<tr>
<% 
if (data_type.compareTo("1") == 0 || data_type.compareTo("2") == 0)
{ 
%>
				<td align="left" valign="middle" class="titlebar" colspan="2" style="font-size:12px;"><img src="http://www.hotenavi.com/demo/image/title_new.gif" width="79" height="14" alt="What's new">
<%
}
else if (data_type.compareTo("3") == 0 || data_type.compareTo("4") == 0)
{
%>
				<td align="left" valign="middle" class="titlebar" colspan="2" style="font-size:12px;"><img src="http://www.hotenavi.com/demo/image/title_event.gif" width="140" height="14" alt="イベント情報">
<%
}
%>
<% 
if (data_type.compareTo("2") == 0 || data_type.compareTo("4") == 0 || data_type.compareTo("6") == 0)
{ 
%>
				タイトルをクリックすると、明細が表示されます。
<%
}
%>
				</td>
				</tr>

<%

				String		query;
				String		query_user;
				java.sql.Date start;
				java.sql.Date end;

				int event_id;
				int last_update;
				int last_uptime;
				int i;
				String user_name = "";
				String msg_data;
				String msg_title;
				String msg_title_color;

				// MySQLへの接続を確立する
				DbAccess db = new DbAccess();
			    ResultSet result_user;
				query = "SELECT * FROM edit_event_info WHERE upd_hotelid<>''";

if (disp_type.compareTo("0") == 0 && sort_type.compareTo("0") == 0)
{
				query = query + " AND upd_hotelid<>'hotenavi'";
				query = query + " AND data_type=" + data_type;
				query = query + " AND last_update<=" + ymd;
				query = query + " AND last_update>=" + pre_ymd;
				query = query + " ORDER BY add_date DESC, add_time DESC";
}
else
if (disp_type.compareTo("1") == 0 && sort_type.compareTo("0") == 0)
{
				query = query + " AND data_type=" + data_type;
				query = query + " AND last_update<=" + ymd;
				query = query + " AND last_update>=" + pre_ymd;
				query = query + " ORDER BY add_date DESC, add_time DESC";
}
else
if (disp_type.compareTo("0") == 0 && sort_type.compareTo("1") == 0)
{
				query = query + " AND upd_hotelid<>'hotenavi'";
				query = query + " AND data_type=" + data_type;
				query = query + " AND last_update<=" + ymd;
				query = query + " AND last_update>=" + pre_ymd;
				query = query + " ORDER BY last_update DESC, last_uptime DESC";
}
else
if (disp_type.compareTo("1") == 0 && sort_type.compareTo("1") == 0)
{
				query = query + " AND data_type=" + data_type;
				query = query + " AND last_update<=" + ymd;
				query = query + " AND last_update>=" + pre_ymd;
				query = query + " ORDER BY last_update DESC, last_uptime DESC";
}


				// SQLクエリーの実行
				ResultSet result = db.execQuery(query);
				while( result.next() != false )
				{

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

						if(( disp_type.compareTo("1") != 0 && result.getInt("disp_flg") == 1 && now_date <= end_date ) || (disp_type.compareTo("1") == 0))
						{
							DbAccess db_user = new DbAccess();
 						   // ユーザ一覧・セキュリティ情報取得
  							query_user = "SELECT * FROM owner_user WHERE hotelid='" + result.getString("upd_hotelid") + "'";
    						query_user = query_user + " AND userid='" + result.getString("upd_userid") + "'";
    						result_user = db_user.execQuery(query_user);
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
							db_user.close();
%>

				<tr>
					<td  colspan="2">
						<!-- ############### サブタイトルバー ############### -->				
<% 
if (data_type.compareTo("2") == 0 || data_type.compareTo("4") == 0 || data_type.compareTo("6") == 0)
{ 
%>
						<table cellspacing="0" bgcolor="#ffffff"  border="1" cellpadding="0" bordercolor="#666666" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor" onclick="disp_find('honbun_demo_<%= result.getString("upd_hotelid")  %>_<%= event_id %>',this)" ><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="7" height="18"></td>
								<td width="3" onclick="disp_find('honbun_demo_<%= result.getString("upd_hotelid")  %>_<%= event_id %>',this)"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor" onclick="disp_find('honbun_demo_<%= result.getString("upd_hotelid")  %>_<%= event_id %>',this)"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="3" height="18"></td>
								<td width="370" onclick="disp_find('honbun_demo_<%= result.getString("upd_hotelid")  %>_<%= event_id %>',this)">
									<div><font color="<%= result.getString("title_color") %>"><%= result.getString("title") %></div>
								</td>
<%
}
else
{
%>
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="3" height="18"></td>
								<td width="400" style="font-size:12px;">
									<div><font color="<%= result.getString("title_color") %>"><%= result.getString("title") %></div>
								</td>
<%
}
%>
								<td  align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %>"><font color="<%= result.getString("title_color") %>" style="font-size:12px;">
				<form action="event_edit_form.jsp" method=POST>
			  <input type="hidden"   name="HotelId" value="<%= result.getString("hotelid") %>">
			  <input type="hidden"   name="DataType" value="<%= data_type %>">
			  <input type="hidden"   name="Id" value="<%= result.getInt("id") %>">
			  <input name="submit00" type="submit" value="編集する" class="size12">
<%
        if( result.getInt("member_only") == 1 )
        {
%>
      <b>メンバー専用</b>&nbsp;
<%
        }
%>
<%= result.getString("hotelid") %>&nbsp;
位置:<%= result.getInt("disp_idx") %>&nbsp;
期間:<%= start_yy+1900 %>年<%= start_mm+1 %>月<%= start_dd %>日～<%= end_yy+1900 %>年<%= end_mm+1 %>月<%= end_dd %>日&nbsp;
編集:<%= user_name %>&nbsp;<% if (user_name.compareTo("不明") != 0) { %><%=  result.getString("last_update") %> <% } %>
<%
        if( now_date < start_date || now_date > end_date || result.getInt("disp_flg") != 1)
        {
%>
      <br><b>表示していません。表示期間チェック！</b>
<%
        }
%>


								</td></form>
							</tr>
						</table>
						<!-- ############### サブタイトルバー ここまで ############### -->				

					</td>
				</tr>
<% 
if (data_type.compareTo("2") == 0 || data_type.compareTo("4") == 0 || data_type.compareTo("6") == 0)
{ 
%>
<tr id="honbun_demo_<%= result.getString("upd_hotelid")  %>_<%= event_id %>" style="display:none;"><td><table width="166" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" bordercolor="#666666"><tr><td width="160" nowrap>
<%
								for( i = 1 ; i <= 8 ; i++ )
								{
										msg_data	= "msg" + Integer.toString(i);
										msg_title = "msg" + Integer.toString(i) + "_title";
										msg_title_color = "msg" + Integer.toString(i) + "_title_color";

										if( result.getString(msg_title).length() != 0 )
										{
%>
										<font color="<%= result.getString(msg_title_color) %>"><%= result.getString(msg_title) %></font><br>
										<font color="#000000"><%=  result.getString(msg_data) %></font><br>

<%
										}
								}
%>
</td></tr></table></td></tr>
<%
}
else
{
%>

<%
								for( i = 1 ; i <= 8 ; i++ )
								{
										msg_data	= "msg" + Integer.toString(i);
										msg_title = "msg" + Integer.toString(i) + "_title";
										msg_title_color = "msg" + Integer.toString(i) + "_title_color";

										if( result.getString(msg_title).length() != 0 )
										{
%>

				<tr>
					<td  colspan="2"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="500" height="8"></td>
				</tr>

				<tr>
					<td align="left" valign="top"  colspan="2">

						<!-- ############### 本文タイトルバー ############### -->
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="honbun_margin">
							<tr>
								<td width="4" class="honbuntitlebar_left"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="4" height="16"></td>
								<td class="honbuntatitlebar_text"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="6" height="16" border="0" align="absmiddle"><font color="<%= result.getString(msg_title_color) %>"><%= result.getString(msg_title) %></td>
							</tr>
						</table>
						<!-- ############### 本文タイトルバー ここまで ############### -->

					</td>
				</tr>

				<tr>												
					<td  colspan="2"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="500" height="8"></td>
				</tr>

				<tr>
					<td align="left" valign="top" class="honbun"  colspan="2">

<%
      String firstString  = result.getString(msg_data);
	  String taisyoString = "src=\"image/";
	  String henkanString = "src=\"http://www.hotenavi.com/" + hotelid +  "/image/";
      String afterString = "";
      //対象文字列を判断しCRLFなどの改行コードを考慮してインプリメントするポインタ数を制御する。
      int plusPoint = taisyoString.length();
      int startPoint = 0;
      int endPoint   = firstString.indexOf(taisyoString, startPoint);

      //文字列に対象文字列がない場合、そのままの文字列を戻す
      //文字列に対象文字列がある間、以下の処理を繰り返す
      while (endPoint != -1){
        //文字列から対象文字列を元に検索行い変換文字列に置換する。
        afterString = afterString + firstString .substring(startPoint, endPoint) + henkanString;
        startPoint  = endPoint + plusPoint;
        endPoint    = firstString .indexOf(taisyoString, startPoint);
      }
      afterString = afterString + firstString .substring(startPoint);
	  
%>

						<div class="honbun_margin">
						<%= afterString %>
						</div>
					</td>
				</tr>
				
				<tr>
					<td  colspan="2" ><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="500" height="8"></td>
				</tr>

<%
										}
								}
%>

				<tr>
					<td  colspan="2"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="500" height="20"></td>
				</tr>


<%
}
						}
				}
				db.close();
%>

				<tr>
					<td  colspan="2"><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="500" height="50"></td>
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
					<td><img src="http://www.hotenavi.com/demo/image/spacer.gif" width="500" height="20"></td>
				</tr>
			</table>
		
		</td>
	</tr>
</table>

</body>
</html>


