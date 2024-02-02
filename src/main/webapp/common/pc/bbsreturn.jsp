<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
%>
<%
		if( hotelid == null )
		{
%>
				<jsp:forward page="error/error.html" />
<%
		}
%>
<%@ include file="html_old_design.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>掲示板</title>
<script language="JavaScript">
<!--
var bbs00="bbsguide.htm"
				function bbs00W(){
								window.open(bbs00,"new1","resizable=0,scrollbars=1,width=450,height=550")}
//-->
</SCRIPT>
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
	var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
		var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
		if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
	var p,i,x;	if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
		d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	 if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
<link href="<%=cssFile%>" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%
		int 	 key;

		String msg_id = request.getParameter("msg_id");
		if (msg_id != null)                               //20100910追加
		{
			 msg_id   = ReplaceString.HTMLEscape(msg_id); //20100910追加
		}
		// 削除キーの生成
		Random rnd = new Random();

		while( true )
		{
				key = rnd.nextInt(9999);
				if( key >= 1000 )
				{
						break;
				}
		}
%>

<script language="JavaScript">
		function datacheck( )
		{
				if( document.forms[0].name.value == "" )
				{
						alert('ハンドルネームを入力してください。');
						return false;
				}

				if( document.forms[0].subject.value == "" )
				{
						alert('題名を入力してください');
						return false;
				}

				if( document.forms[0].body.value == "" )
				{
						alert('本文を入力してください');
						return false;
				}

				if( document.forms[0].delkey.value == "" )
				{
						alert('削除キーを入力してください');
						return false;
				}

				if( isNaN(document.forms[0].delkey.value) != false )
				{
						alert('削除キーは数値で入力してください');
						return false;
				}

//				body = document.forms[0].body.value;
//				if( body.length > 250 )
//				{
//						alert('本文は２５０文字以内でお願いします');
//						return false;
//				}
				return true;
		}
</script>


<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>


			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left" valign="middle" class="titlebar"><img src="<%=imageTitleBbs%>" width="140" height="14" alt="掲示板"></td>
				</tr>
				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="20"></td>
				</tr>

				<tr>
					<td class="honbun">下記投稿への返信</td>
				</tr>

				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="8"></td>
				</tr>

				<tr>
					<td>
            <form action="bbsreturn2.jsp?HotelId=<%= hotelid %>&msg_id=<%= msg_id %>" method="POST">

						<table width="100%" border="0" cellspacing="0" cellpadding="0" class="bbs">
							<tr>
								<tr>
									<td height="17" align="right" valign="middle" nowrap class="link1" style="padding:3px;">ハンドルネーム:</td>
									<td class="link1"><input name="name" type="text" id="name" size="32" maxlength="64">※必須</td>
									<td rowspan="5" width="1" background="<%=imageDot1%>" style="padding: 0px;"><img src="<%=imageSpacer%>" width="1" height="10"></td>
									<td rowspan="5" align="center" valign="middle"><img src="<%=imageBbsImage%>" width="167" height="128"><br><br>
										<input  name="submit" type="image" id="submit" onclick="return datacheck()" src="<%=imageBtnToukou%>" alt="投稿する" align="middle" width="91" height="24" border="0">
									</td>
								</tr>
								<tr>
									<td align="right" valign="middle" nowrap class="link1" style="padding:3px;">題　　名:</td>
									<td class="link1"><input name="subject" type="text" id="subject" size="32" maxlength="64">※必須</td>
								</tr>
								<tr>
									<td align="right" valign="top" nowrap class="link1" style="padding:3px;">本文:</td>
									<td class="link1"><textarea class=size name=body rows=6 cols=64></textarea><BR>※必須</td>
								</tr>

								<tr>
									<td align="right" valign="top" nowrap class="link1" style="padding:3px;">削除キー:</td>
									<td><input type="text" class="size" maxLength="4" size="6" value="<%= key %>" name="delkey"></td>
								</tr>
						</table>

						</form>
						<!-- 掲示板書き込み入力用テーブル ここまで -->

					</td>
				</tr>

				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="8"></td>
				</tr>

<%
		try
		{
				int 			msgid;
				boolean 	nulldata;
				String		query;
				String		data;
				String		subject = "";
				String		input_date = "";
				String		name = "";
				String		mail = "";
				String		body = "";

				// MySQLへの接続を確立する
				DbAccess db =  new DbAccess();

				query = "SELECT * FROM bbs WHERE hotel_id=?";
				query = query + " AND msg_id=?";
				query = query + " AND sub_id=0";
				query = query + " AND del_flag=0";
				query = query + " ORDER BY msg_id DESC";
				query = query + " LIMIT 100";
				List<Object> list = new ArrayList<Object>();
				list.add(hotelid);
				list.add(msg_id);
				// SQLクエリーの実行
				ResultSet result = db.execQuery(query,list);
				while( result.next() != false )
				{
						msgid = result.getInt("msg_id");

						data = ReplaceString.HTMLEscape(result.getString("subject"));
						nulldata = result.wasNull();
						if( nulldata == false )
						{
								subject = data;
						}

						data = result.getString("input_date");
						nulldata = result.wasNull();
						if( nulldata == false )
						{
								input_date = data;
						}

						data = ReplaceString.HTMLEscape(result.getString("name"));
						nulldata = result.wasNull();
						if( nulldata == false )
						{
								name = data;
						}
						mail = "";

						data = ReplaceString.HTMLEscape(result.getString("body"));
						nulldata = result.wasNull();
						if( nulldata == false )
						{
								body = data;
						}
						body = body.replace("&amp;","&");
						body = body.replace("\r\n","<br>");

%>

				<tr>
					<td class="honbun">

						<!-- 書き込まれた内容表示するテーブル -->

						<!-- メンバー名表示 -->
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="bbs_main_name" nowrap style="padding:3x;">
												<%= name %> さん
								</td>
								<td align="right" class="bbs_main_date"><%= input_date %></td>
							</tr>
						</table>

						<!-- ここから本文表示 -->
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="bbs">
							<tr>
								<td class="bbs_main_title" style="padding:3px;" width="80%">■<%= subject %></td>
								<form action="bbsdelete.jsp?HotelId=<%= hotelid %>&msg_id=<%= msgid %>" method=POST>
									<td width="10%" align="center" nowrap class="bbs_main_delete"><input class="honbun" type=password maxLength=11 size=4 name=del_key>&nbsp;<input name="submit2" type=submit value=削除 class="inputbtnGrey"></td>
								</form>
								<td width="10%" align="center" valign="middle" nowrap class="bbs_main_res"><a href="bbsreturn.jsp?HotelId=<%= hotelid %>&msg_id=<%= msgid %>" class="link1">返信する</a></td>
							</tr>
							<tr>
								<td colspan="3" class="bbs_main_body" style="padding:3px;"><%= body %></td>
							</tr>
							<tr>
								<td colspan="3" align="center"><img src="<%=imageSpacer%>" width="100" height="16"></td>
							</tr>

<%
	 // 返信チェック
				int 			subid;
				boolean 	res_nulldata;
				String		res_query;
				String		res_data;
				String		res_subject = "";
				String		res_input_date = "";
				String		res_name = "";
				String		res_mail = "";
				String		res_body = "";

				DbAccess db_res =  new DbAccess();

				res_query = "SELECT * FROM bbs WHERE hotel_id=?";
				res_query = res_query + " AND msg_id=" + result.getInt("msg_id");
				res_query = res_query + " AND sub_id <> 0";
				res_query = res_query + " AND del_flag=0";
				res_query = res_query + " ORDER BY sub_id";

				// SQLクエリーの実行
				ResultSet res_result = db_res.execQuery(res_query,hotelid);
				while( res_result.next() != false )
				{
						subid = res_result.getInt("sub_id");

						res_data = ReplaceString.HTMLEscape(res_result.getString("subject"));
						res_nulldata = res_result.wasNull();
						if( res_nulldata == false )
						{
								res_subject = res_data;
						}

						res_data = res_result.getString("input_date");
						res_nulldata = res_result.wasNull();
						if( res_nulldata == false )
						{
								res_input_date = res_data;
						}

						res_data = ReplaceString.HTMLEscape(res_result.getString("name"));
						res_nulldata = res_result.wasNull();
						if( res_nulldata == false )
						{
								res_name = res_data;
						}
						res_mail = "";
						res_data = ReplaceString.HTMLEscape(res_result.getString("body"));
						res_nulldata = res_result.wasNull();
						if( res_nulldata == false )
						{
								res_body = res_data;
						}
						res_body = res_body.replace("&amp;","&");
						res_body = res_body.replace("\r\n","<br>");
%>
							<tr>
								<td align="center" valign="middle" colspan="3">

									<!-- 返信用テーブル -->

									<!-- メンバー名表示 -->
									<table width="95%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td class="bbs_res_name" nowrap>
																		<%= res_name %> さん
											</td>
											<td align="right" class="bbs_res_date"><%= res_input_date %> </td>
										</tr>
									</table>

									<!-- ここから　返信の本文表示 -->
									<table width="95%" cellpadding="0" cellspacing="0" class="bbs_res">
										<tr>
											<td width="85%" class="bbs_res_title"><img src="<%=imageSpacer%>" width="4" height="12" align="absmiddle"><img src="<%=imageResyajirushi%>" width="10" height="11" align="absmiddle"><img src="<%=imageSpacer%>" width="4" height="12" align="absmiddle">■<%= res_subject %></td>
											<form action="bbsdelete.jsp?HotelId=<%= hotelid %>&msg_id=<%= msgid %>&sub_id=<%= subid %>" method="POST">
											<td width="10%" align="center" nowrap class="bbs_res_delete"><input class="honbun" type=password maxLength=11 size=4 name=del_key>&nbsp;<input name="submit22" type=submit value=削除 class="inputbtnGrey"></td>
											</form>
										</tr>

										<tr>
											<td colspan="2" class="bbs_res_body" style="padding:3px;"><%= res_body %></td>
										</tr>
										<tr>
											<td colspan="2">&nbsp;</td>
										</tr>
									</table>
									<!-- 返信用テーブル　ここまで -->
								</td>
							</tr>
							<tr>
								<td colspan="3" align="center"><img src="<%=imageSpacer%>" width="100" height="8"></td>
							</tr>
<%
				} // End of while( res_result.next() != false )

				db_res.close();
%>
						</table>
					</td>
				</tr>



				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="8"></td>
				</tr>

<%
				} // End of while( result.next() != false )
%>

<%
			db.close();
		}
		catch( Exception e )
		{
%>
<%= e.toString() %>
<%
		}
%>

				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="8"></td>
				</tr>

				<tr>
					<td><a href="javascript:;" onMouseOver="MM_swapImage('yajirushi','','image/yajirushi.gif',1)" onMouseOut="MM_swapImgRestore()"><img src="<%=imageYajirushi%>" name="yajirushi" width="15" height="13" border="0" id="yajirushi"></a><img src="<%=imageSpacer%>" width="10" height="12" align="absbottom"><A href="bbs.jsp?HotelId=<%= hotelid %>" onmouseover="MM_swapImage('yajirushi','','image/yajirushi.gif',1)" onmouseout="MM_swapImgRestore()" class="link1">掲示板に戻る</A></td>
				</tr>

				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="50"></td>
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
					<td><img src="<%=imageSpacer%>" width="500" height="20"></td>
				</tr>
			</table>

		</td>
	</tr>
</table>

</body>
</html>
