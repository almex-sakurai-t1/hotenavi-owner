<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../../common/pc/goods_message_ini.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
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
    if  (hotelid.compareTo("all") == 0)
    {
        hotelid = ReplaceString.getParameter(request,"HotelId");
		if( hotelid != null && !CheckString.hotenaviIdCheck(hotelid))
		{
			hotelid="0";
%>
			<script type="text/javascript">
			<!--
			var dd = new Date();
			setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
			//-->
			</script>
<%
		}
    }

    String loginhotel = (String)session.getAttribute("LoginHotelId");

    String  query;
    String  query_user;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_user  = null;
    PreparedStatement prestate_user    = null;
    ResultSet         result_user      = null;

    connection  = DBConnection.getConnection();
    int               imedia_user = 0;
    int               level       = 0;
    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginhotel);
        prestate.setInt(2,ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

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
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>景品交換メッセージ他設定登録</title>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
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
					<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<strong>景品交換メッセージ他設定登録</strong>
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
				<tr>
<%
    if (imedia_user == 1)
    {
%>
					<td>
						<form action="goods_message_form.jsp?HotelId=<%= hotelid %>&Id=0" method=POST>
						<input name="submit00" type=submit value="新しくメッセージ・設定内容を追加" >
					</td></form>
<%
    }
    else
    {
%>
					<td>&nbsp;
					</td>
<%
    }
%>

<%
    if (disp_type.compareTo("0") == 0)
    {
%>
					<td>
					<form action="goods_message.jsp?HotelId=<%= hotelid %>&DispType=1" method=POST>
						<input name="submit01" type=submit value="非表示分も表示する" >
					</td></form>
<%
    }
%>
					<td>
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
<%
    query = "SELECT * FROM edit_event_info WHERE hotelid=?";
    query = query + " AND data_type>=81";
    query = query + " AND data_type<=82";
    query = query + " ORDER BY id DESC,data_type";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, hotelid);
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

        if(( disp_type.compareTo("1") != 0 && result.getInt("disp_flg") == 1 && now_date <= end_date ) || (disp_type.compareTo("1") == 0))
        {
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
<%
            if (result.getInt("data_type") == 81)
            {
%>
				<tr>
					<td  colspan="3">
						<!-- ############### サブタイトルバー ############### -->
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="400">
									<div class="subtitle_text"><font color="<%= result.getString("title_color") %>"><%= result.getString("title") %></div>
								</td>
								<td align=left title="作成:<%=  result.getString("add_date") %>&nbsp;<%=  result.getString("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getString("add_userid") %>
編集:<%= user_name %>&nbsp;<% if (user_name.compareTo("不明") != 0) { %><%=  result.getString("last_update") %>&nbsp;<%=  result.getString("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getString("upd_userid") %><% } %>"  class="subtitle_text"><font color="<%= result.getString("title_color") %>">
								<input name="Id<%= count %>" type=hidden value="<%= result.getInt("id") %>">
								<a href="goods_message_form.jsp?HotelId=<%= hotelid %>&Id=<%= result.getInt("id") %>"><input type="button" value="編集" class="size12" onclick="location.href='goods_message_form.jsp?HotelId=<%= hotelid %>&Id=<%= result.getInt("id") %>'"></a>
								<br>期間:<%= start_yy+1900 %>年<%= start_mm+1 %>月<%= start_dd %>日～<%= end_yy+1900 %>年<%= end_mm+1 %>月<%= end_dd %>日&nbsp;
<%
                if( now_date < start_date || now_date > end_date || result.getInt("disp_flg") != 1)
                {
%>
								<br><b>表示していません。表示期間チェック！</b>
<%
                }
%>
								</td>
							</tr>
						</table>
						<!-- ############### サブタイトルバー ここまで ############### -->
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>

<%
            }
            for( i = 1 ; i <= 8 ; i++ )
            {
                msg_data    = "msg" + Integer.toString(i);
                msg_title = "msg" + Integer.toString(i) + "_title";
                msg_title_color = "msg" + Integer.toString(i) + "_title_color";
                if( result.getString(msg_title).length() != 0 || result.getString(msg_data).length() > 1)
                {
%>
				<tr>
					<td align="left" valign="middle" style="font-size:12px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<strong><%=Explain[result.getInt("disp_idx")][i-1]%></strong>
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
<%
                    if( result.getString(msg_title).length() != 0 )
                    {
%>
				<tr>
					<td align="left" valign="top"  colspan="3">
						<!-- ############### 本文タイトルバー ############### -->
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="honbun_margin">
							<tr>
								<td width="4" class="honbuntitlebar_left"><img src="../../common/pc/image/spacer.gif" width="4" height="16"></td>
								<td class="honbuntatitlebar_text"><img src="../../common/pc/image/spacer.gif" width="6" height="16" border="0" align="absmiddle"><font color="<%= result.getString(msg_title_color) %>"><%= result.getString(msg_title) %></td>
							</tr>
						</table>
						<!-- ############### 本文タイトルバー ここまで ############### -->
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
<%
                    }
%>
				<tr>
					<td align="left" valign="top" class="honbun"  colspan="3">

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
                        while (endPoint != -1)
                        {
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
					<td  colspan="3" ><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
				</tr>

<%
                }
%>
<%
            }
        }
        if (disp_type.compareTo("0") == 0)
        {
            if (count > 1)break;
        }
    }
    DBConnection.releaseResources(result,prestate,connection);
%>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="50"></td>
				</tr>
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


