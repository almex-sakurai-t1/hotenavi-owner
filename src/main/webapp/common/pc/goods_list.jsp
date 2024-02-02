<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>景品登録</title>
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
						<strong>景品登録</strong>
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
				</tr>
				<tr>
					<td>
						<form action="goods_category_form.jsp?HotelId=<%= hotelid %>&Seq=0" method=POST>
						<input name="submit00" type=submit value="新しくカテゴリーを追加" >
					</td></form>
<%
    if (disp_type.compareTo("0") == 0)
    {
%>
					<td>
					<form action="goods_list.jsp?HotelId=<%= hotelid %>&DispType=1" method=POST>
						<input name="submit01" type=submit value="非表示分も表示する" >
					</td></form>
<%
    }
%>
					<td>
						<form action="goods_category_order_change.jsp?HotelId=<%= hotelid %>" method=POST>
						<input name="submit02" type=submit value="表示順番変更" >
					</td>
				</tr>
				<tr>
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
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
    Calendar now = Calendar.getInstance();
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
    int goods_count = 0;
    int category_id = 0;
    int disp_idx    = 0;
    int category_sv = 99999;
    String user_name = "";
    String msg_data;
    String msg_title;
    String msg_title_color;
    yy = now.get(Calendar.YEAR);
    mm = now.get(Calendar.MONTH);
    dd = now.get(Calendar.DATE);
    now_date = (yy)*10000 + (mm+1)*100 + dd;

    connection  = DBConnection.getConnection();
    query = "SELECT * FROM goods_category WHERE hotelid= ? ";
    query = query + " ORDER BY disp_idx,category_id,seq DESC";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, hotelid);
    result      = prestate.executeQuery();

    // SQLクエリーの実行
    while( result.next() != false )
    {
        category_id  = result.getInt("category_id");
        disp_idx     = result.getInt("disp_idx");
        start_date   = result.getInt("disp_from");
        int start_yy = start_date / 10000;
        int start_mm = start_date / 100 % 100;
        int start_dd = start_date % 100;
        end_date     = result.getInt("disp_to");
        int end_yy   = end_date / 10000;
        int end_mm   = end_date / 100 % 100;
        int end_dd   = end_date % 100;

        if((category_sv != category_id) && (( disp_type.compareTo("1") != 0 && now_date <= end_date ) || (disp_type.compareTo("1") == 0)))
        {
            category_sv = category_id;
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
            DBConnection.releaseResources(result_user);
            DBConnection.releaseResources(prestate_user);

          //景品マスタ登録件数の抽出
            query_user = "SELECT count(*) FROM goods WHERE hotelid= ? ";
            if (disp_idx == 0)
            {
                query_user = query_user + " AND suggest_flag=1";
            }
            else
            {
                query_user = query_user + " AND category_id="+ category_id;
            }
            query_user = query_user + " AND disp_from<=" + now_date;
            query_user = query_user + " AND disp_to>="   + now_date;

            prestate_user    = connection_user.prepareStatement(query_user);
            prestate_user.setString(1, hotelid);
            result_user      = prestate_user.executeQuery();
            if( result_user.next() != false )
            {
                goods_count = result_user.getInt(1);
            }
            DBConnection.releaseResources(result_user,prestate_user,connection_user);
%>

				<tr>
					<td  colspan="3">
						<!-- ############### サブタイトルバー ############### -->
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="120">
									<div class="subtitle_text"><font color="<%= result.getString("title_color") %>"><%= result.getString("title") %></div>
								</td>
								<td width="180" align=left title="作成:<%=  result.getInt("add_date") %>&nbsp;<%=  result.getInt("add_time") %>&nbsp;<%=  result.getString("add_hotelid") %>&nbsp;<%=  result.getInt("add_userid") %>
編集:<%= user_name %>&nbsp;<% if (user_name.compareTo("不明") != 0) { %><%=  result.getInt("last_update") %>&nbsp;<%=  result.getInt("last_uptime") %>&nbsp;<%=  result.getString("upd_hotelid") %>&nbsp;<%=  result.getInt("upd_userid") %><% } %>"  class="subtitle_text"><font color="<%= result.getString("title_color") %>">
									<input name="Seq<%= count %>" type=hidden value="<%= result.getInt("seq") %>">
									<a href="goods_category_form.jsp?HotelId=<%= hotelid %>&CategoryId=<%= result.getInt("category_id") %>&Seq=<%= result.getInt("seq") %>">
									<input type="button" value="編集" class="size12" onclick="location.href='goods_category_form.jsp?HotelId=<%= hotelid %>&CategoryId=<%= category_id %>&Seq=<%= result.getInt("seq") %>'"></a>
<%
//    if (disp_idx != 0)
//    {
%>
									<input type="button" value="景品登録" class="size12" onclick="location.href='goods_list_detail.jsp?HotelId=<%= hotelid %>&CategoryId=<%= category_id %>'"></a>
								件数：<a href="goods_list_detail.jsp?HotelId=<%= hotelid %>&CategoryId=<%= category_id %>" class="link1"><%=goods_count%></a>
								</td>
								<td style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;">
								&nbsp;表示順：
								<input type="text" name="DispIdx<%=count%>" size=3 maxlength=3 value="<%=disp_idx%>" style="text-align:right;ime-mode:disabled">
								&nbsp;期間:<%= start_yy %>年<%= start_mm %>月<%= start_dd %>日～<%= end_yy %>年<%= end_mm %>月<%= end_dd %>日&nbsp;
<%
//    }
//    else
//    {
%><!--
								件数：<a href="goods_list_detail.jsp?HotelId=<%= hotelid %>&CategoryId=<%= category_id %>" class="link1"><%=goods_count%></a>
								</td>
								<td style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;">
								<input type="hidden" name="DispIdx<%=count%>" value="<%=disp_idx%>">
--><%
//    }
%>
								<input type="hidden" name="Category<%=count%>" value="<%=category_id%>">
<%
            if( now_date < start_date || now_date > end_date )
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
					<td  colspan="3"><img src="../../common/pc/image/spacer.gif" width="500" height="3"></td>
				</tr>
<%
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


