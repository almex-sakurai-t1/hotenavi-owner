<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/qaedit_message_ini.jsp" %>
<%
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    String disp_type = ReplaceString.getParameter(request,"DispType");
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
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>アンケートメッセージ設定</title>
<% if (loginHotelId.compareTo("hotenavi") != 0){%>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<%}%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
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

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="2">
						<strong>アンケートメッセージ</strong>
					</td>
				</tr>
				<tr>
					<td  colspan="2"><img src="../../common/pc/image/spacer.gif" width="500" height="3"></td>
				</tr>
				<tr>
					<td>
						<form action="qaedit_message_form.jsp?HotelId=<%= hotelid %>&Id=0" method=POST>
						<input name="submit00" type=submit value="新しく設定を追加" >
					</td></form>
<%
    if (disp_type.compareTo("0") == 0)
    {
%>
					<td>
					<form action="qaedit_message.jsp?HotelId=<%= hotelid %>&DispType=1" method=POST>
						<input name="submit01" type=submit value="非表示分も表示する" >
					</td></form>
<%
    }
%>
				</tr>
				<tr>
					<td  colspan="2"><img src="../../common/pc/image/spacer.gif" width="500" height="3"></td>
				</tr>

<%
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_user  = null;
    PreparedStatement prestate_user    = null;
    ResultSet         result_user      = null;
    String        query;
    String        query_user;
    java.sql.Date start;
    java.sql.Date end;
    Calendar now = Calendar.getInstance();
    int event_id;
    int yy;
    int mm;
    int dd;
    int start_date;
    int end_date;
    int now_date;
    int last_update;
    int last_uptime;
    int i;
    int count = 0;
    String user_name = "";
    String msg_data;
    String msg_title;
    String msg_title_color;
    boolean TargetExist = false;

    connection  = DBConnection.getConnection();
    query = "SELECT * FROM edit_event_info WHERE hotelid=?";
    query = query + " AND data_type=?";
    query = query + " ORDER BY id DESC";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid );
    prestate.setInt(2,data_type);
    result      = prestate.executeQuery();

    // SQLクエリーの実行
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
        yy = now.get(Calendar.YEAR);
        mm = now.get(Calendar.MONTH);
        dd = now.get(Calendar.DATE);
        now_date = (yy)*10000 + (mm+1)*100 + dd;

        if(( disp_type.compareTo("1") != 0 && now_date <= end_date ) || (disp_type.compareTo("1") == 0))
        {
            count++;
            // ユーザ一覧・セキュリティ情報取得
            connection_user  = DBConnection.getConnection();
            query_user = "SELECT * FROM owner_user WHERE hotelid=?";
            query_user = query_user + " AND userid=?";
            prestate_user = connection_user.prepareStatement(query_user);
            prestate_user.setString(1,result.getString("upd_hotelid") );
            prestate_user.setInt(2,result.getInt("upd_userid"));
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
            DBConnection.releaseResources(result_user,prestate_user,connection_user);
%>

				<tr>
					<td  colspan="2">
						<!-- ############### サブタイトルバー ############### -->
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor" onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)" ><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3" onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor" onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="220" onclick="disp_find('honbun_<%= hotelid %>_<%= event_id %>',this)" class="subtitle_text">
									<div>期間:<%= start_yy+1900 %>年<%= start_mm+1 %>月<%= start_dd %>日～<%= end_yy+1900 %>年<%= end_mm+1 %>月<%= end_dd %>日
<%
            if( now_date < start_date || now_date > end_date )
            {
%>
								<br><b>＊表示していません</b>
<%
            }
%>
									</div>
								</td>
								<td nowrap align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%= user_name %>&nbsp;<% if (user_name.compareTo("不明") != 0) { %><%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %><% } %>"  class="subtitle_text">
									<div>
									<input name="Id<%= count %>" type=hidden value="<%= result.getInt("id") %>">
									<input type="button" value="編集" class="size12" onclick="location.href='qaedit_message_form.jsp?HotelId=<%= hotelid %>&Id=<%= result.getInt("id") %>'">
<%
            if( result.getInt("disp_flg") != 1)
            {
%>
								<b>＊現在、下書き保存（非掲載）です</b>⇒
									<input type="button" value="掲載" class="size12" onclick="location.href='qaedit_message_open.jsp?HotelId=<%= hotelid %>&Id=<%= result.getInt("id") %>'">
<%
            }
            if(now_date <= end_date && now_date >= start_date &&  result.getInt("disp_flg") == 1)
            {
                if (!TargetExist)
                {
                    TargetExist = true;
%>
								<b>*掲載中</b>
<%
                }
                else
                {
                }
            }
%>

									</div>
								</td>
							</tr>
						</table>
						<!-- ############### サブタイトルバー ここまで ############### -->
					</td>
				</tr>
				<tr id="honbun_<%= hotelid %>_<%= event_id %>" style="<% if (count == 1){%>display:block;<%}else{%>display:none;<%}%>">
					<td colspan="2">
						<table>
<%
                for( i = 1 ; i <= 8 ; i++ )
                {
                    msg_data  = "msg" + Integer.toString(i);
                    msg_data  = msg_data.replace("<iframe","<span");
                    msg_data  = msg_data.replace("</iframe","</span");
                    msg_title = "msg" + Integer.toString(i) + "_title";
                    msg_title_color = "msg" + Integer.toString(i) + "_title_color";
                    if( result.getString(msg_title).length() != 0 || result.getString(msg_data).length() > 1 )
                    {
%>
						<tr>
							<td  colspan="2"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
						</tr>
<%
                        if( Explain[0][i-1].length() != 0 )
                        {
%>
						<tr>
							<td align="left" valign="top"  colspan="2">
								<!-- ############### 本文タイトルバー ############### -->
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="honbun_margin">
									<tr>
										<td width="4" class="honbuntitlebar_left"><img src="../../common/pc/image/spacer.gif" width="4" height="16"></td>
										<td class="honbuntatitlebar_text"><img src="../../common/pc/image/spacer.gif" width="6" height="16" border="0" align="absmiddle"><%= Explain[0][i-1] %></td>
									</tr>
								</table>
						<!-- ############### 本文タイトルバー ここまで ############### -->
							</td>
						</tr>
						<tr>
							<td  colspan="2"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
						</tr>
<%
                        }
%>
						<tr>
							<td align="left" valign="top" class="honbun"  colspan="2">
								<div class="honbun_margin">
								<%=  result.getString(msg_data) %>
								</div>
							</td>
						</tr>

						<tr>
							<td  colspan="2" ><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
						</tr>
<%
                     }
                 }
%>
				</table></td></tr>

				<tr>
					<td  colspan="2"><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>


<%
        }
    }
    DBConnection.releaseResources(result,prestate,connection);
%>
				<tr>
					<td  colspan="2"><img src="../../common/pc/image/spacer.gif" width="500" height="50"></td>
				</tr>
<%
    if ( count > 1)
    {
%>
				<tr valign="middle" class="subtitlebar_basecolor">
					<td colspan="2">
						<div class="subtitle_text">クリックすると設定内容が表示されます。</div>
					</td>
				</tr>
<%
    }
%>
			</table>
		</td><input name="LineCount" type=hidden value="<%= count %>"></form>
	</tr>

	<tr valign="bottom">
		<td>
			<!-- copyright テーブル -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" valign="middle">
						<div class="copyright">
							Copyright &copy; almex inc, All Rights Reserved
						</div>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>
			</table>

		</td>
	</tr>
</table>

</body>
</html>


