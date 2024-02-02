<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.HotelElement" %>
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
<link href="<%=cssFile%>" rel="stylesheet" type="text/css">
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
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('image/yajirushi.gif')">
<%
		// ハンドルネーム
		String param_name    = ReplaceString.getParameter(request,"name");
		if (param_name       == null)                                   //20100910追加
		{
			param_name       = "";
		}
		String name          = ReplaceString.HTMLEscape(param_name);    //20100910追加
		// 題名
		String param_subject = ReplaceString.getParameter(request,"subject");
		if (param_subject    == null)                                   //20100910追加
		{
			param_subject    = "";
		}
		String subject       = ReplaceString.HTMLEscape(param_subject); //20100910追加
		// メールアドレス
		String mail          = ""; //必ず空白にする
		// 本文
		String param_body    = ReplaceString.getParameter(request,"body");
		if( param_body       == null )                                  //20100910追加
		{
			param_body       = "";
		}
		String body          = param_body;    //20100910追加
		// 削除キー
		String param_delkey  = ReplaceString.getParameter(request,"delkey");
		if (param_delkey     == null)                                   //20100910追加
		{
			param_delkey     = "";
		}
		String delkey        = ReplaceString.HTMLEscape(param_delkey);  //20100910追加

		try
		{
				String		query;
				DateEdit dateedit = new DateEdit();
				int    nowDate = Integer.parseInt(dateedit.getDate(2));
				int    startDate = HotelElement.getStartDate(hotelid);

				// MySQLへの接続を確立する
				DbAccess db =  new DbAccess();
                String UserAgent = request.getHeader("user-agent");
                if (UserAgent.length() > 255)
                {
                    UserAgent = UserAgent.substring(0,255);
                }

				query = "INSERT INTO bbs VALUES (";
				query = query + "?,";
				query = query + "0,";
				query = query + "0,";
				query = query + "'" + dateedit.getDate(0) + " " + dateedit.getTime(0) + "',";
				query = query + "'" + ReplaceString.SQLEscape(name) + "',";
				query = query + "'" + ReplaceString.SQLEscape(mail) + "',";
				query = query + "'" + ReplaceString.SQLEscape(subject) + "',";
				query = query + "'" + ReplaceString.SQLEscape(body.replace("<br>","\r\n")) + "',";
				query = query + "?,";
				query = query + "0" + ",";
				query = query + "'" + ReplaceString.SQLEscape(UserAgent) + "',";
				query = query + "'" + ReplaceString.SQLEscape(( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() )) + "'";
				query = query + ")";

				List<Object> list = new ArrayList<Object>();
				//パラメータ追加
				list.add(hotelid);
				list.add(delkey);
				// SQLクエリーの実行
				int result = db.execUpdate(query,list);

				// メールの送信
				if( result != 0 )
				{
						DbAccess db_msgid =  new DbAccess();
						DbAccess db_mail =  new DbAccess();

						String mailsubject = "掲示板に投稿がありました";

						// メッセージＩＤの取得
						query = "SELECT * FROM bbs WHERE hotel_id=?";
						query = query + " ORDER BY msg_id DESC";
						ResultSet bbs_rec = db_msgid.execQuery(query,hotelid);
						bbs_rec.next();

						int msgid = bbs_rec.getInt("msg_id");

						// 送信先の取得
						query = "SELECT * FROM hotel WHERE hotel_id=?";
						ResultSet rec = db_mail.execQuery(query,hotelid);
						rec.next();

						String admin_mail = rec.getString( "mail" );

						String text = dateedit.getDate(0) + " " + dateedit.getTime(0) + "\r\n";
						text = text + param_name + "さんからの投稿" + "\r\n";
						text = text + param_subject + "\r\n";
						text = text + param_body + "\r\n";
						text = text + "\r\n";
						if (startDate < nowDate)
						{
							text = text + "スマートフォンからの返信はこちらのURLから↓" + "\r\n";
							text = text + " http://www.hotenavi.com/" + hotelid + "/smart/bbsinput.jsp?msg_id=" + msgid + "\r\n";
							text = text + "PCからの返信はこちらのURLから↓" + "\r\n";
							text = text + " http://www.hotenavi.com/" + hotelid + "/bbsreturn.jsp?msg_id=" + msgid + "\r\n";
							text = text + "\r\n";
						}
						else
						{
							text = text + "返信はこちらのURLから↓" + "\r\n";
							text = text + "https://www.hotenavi.com/" + hotelid + "/bbs/return/" + msgid + "\r\n";
							text = text + "\r\n";
						}
 					    text = text + "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－\r\n";
						text = text + "★このメールに返信しても投稿されませんのでご注意ください。\r\n";
						text = text + "★このメールは投稿時に自動的に配信されています。\r\n";
						text = text + "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－\r\n";

						SendMail sendmail = new SendMail();
						sendmail.send("webmaster@hotenavi.com", admin_mail, mailsubject, text);

						db_msgid.close();
						db_mail.close();
				}
%>


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
					<td class="honbun">

<%
				if( result != 0 )
				{
%>
												投稿が完了しました。
<%
								}
								else
								{
%>
												投稿に失敗しました。
<%
								}
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
					</td>
				</tr>

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
