<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.HotelElement" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
		String hotelid = ReplaceString.getParameter(request,"HotelId");
		String hotelid_multi = ReplaceString.getParameter(request,"HotelId_multi");
		if( hotelid == null || !CheckString.hotenaviIdCheck(hotelid))
		{
            hotelid = "";
%>
			<script type="text/javascript">
			<!--
			var dd = new Date();
			setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
			//-->
			</script>
<%
		}
		if( hotelid_multi == null || !CheckString.hotenaviIdCheck(hotelid_multi))
		{
            hotelid_multi = "";
%>
			<script type="text/javascript">
			<!--
			var dd = new Date();
			setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
			//-->
			</script>
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

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('image/yajirushiGrey_f2.gif')">

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
		String mail          = "";    //必ず空白にする
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
		// メッセージＩＤ
		String msgid = ReplaceString.getParameter(request,"msg_id");
		if (msgid != null)                               //20100910追加
		{
			 msgid   = ReplaceString.HTMLEscape(msgid);  //20100910追加
		}

		try
		{
				int 			subid;
				String		query;
				String		hotel;
				DateEdit dateedit = new DateEdit( );
				int    nowDate = Integer.parseInt(dateedit.getDate(2));
				int    startDate = HotelElement.getStartDate(hotelid);

				// MySQLへの接続を確立する
				DbAccess db =  new DbAccess();

				// 最新のsub_idを取得する
				query = "SELECT * FROM bbs_multi WHERE hotel_id=?";
				query = query + " AND msg_id=?";
				query = query + " ORDER BY sub_id DESC";
				List<Object> list = new ArrayList<Object>();
				list.add(hotelid_multi);
				list.add(msgid);

				// SQLクエリーの実行
				ResultSet result = db.execQuery(query,list);
				result.next();
				subid = result.getInt("sub_id");
				subid = subid + 1;
				hotel = result.getString("topic_hotel_id");

                String UserAgent = request.getHeader("user-agent");
                if (UserAgent.length() > 255)
                {
                    UserAgent = UserAgent.substring(0,255);
                }

				query = "INSERT INTO bbs_multi VALUES (";
				query = query + "?,";
				query = query + "?,";
				query = query + "?,";
				query = query + "'" + dateedit.getDate(0) + " " + dateedit.getTime(0) + "',";
				query = query + "'" + ReplaceString.SQLEscape(name) + "',";
				query = query + "'" + ReplaceString.SQLEscape(mail) + "',";
				query = query + "'" + ReplaceString.SQLEscape(subject) + "',";
				query = query + "'" + ReplaceString.SQLEscape(body.replace("<br>","\r\n")) + "',";
				query = query + delkey + ",";
				if (hotelid_multi.equals("555"))
				{
					query = query + "1" + ",";
				}
				else
				{
					query = query + "0" + ",";
				}
				query = query + "'" + ReplaceString.SQLEscape(UserAgent) + "',";
				query = query + "'" + ReplaceString.SQLEscape(( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() )) + "',";
				query = query + "'" + hotel + "'";
				query = query + ")";

				list.add(subid);
				// SQLクエリーの実行
				int ret = db.execUpdate(query,list);

				// メールの送信
				if( ret != 0 )
				{
						DbAccess db_msgid =  new DbAccess();
						DbAccess db_mail =  new DbAccess();

						String mailsubject = "掲示板に投稿がありました";

						if( hotel.compareTo("") != 0 )
						{
							String hotelname = "";
							DbAccess dbhotelname =  new DbAccess();
							query = "SELECT * FROM hotel WHERE hotel_id=?";
							ResultSet rethotelname = dbhotelname.execQuery(query,hotel);
							if( rethotelname != null )
							{
								if( rethotelname.next() != false )
								{
									mailsubject = mailsubject + "(" + rethotelname.getString("name") + ")";
								}
							}
							dbhotelname.close();
						}

						// メッセージＩＤの取得
						query = "SELECT * FROM bbs_multi WHERE hotel_id=?";
						query = query + " AND msg_id=?";
						query = query + " AND sub_id=?";
						list = new ArrayList<Object>();
						list.add(hotelid_multi);
						list.add(msgid);
						list.add(subid);
						ResultSet bbs_rec = db_msgid.execQuery(query,list);
						bbs_rec.next();

						int msg_id = bbs_rec.getInt("msg_id");

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
						if (hotelid_multi.equals("555"))
						{
							text = text + "携帯からの確認はこちらのURLから↓" + "\r\n";
							text = text + "[DOCOMO]   http://hotenavi.com/555/" + "i/ownerbbs.jsp?HotelId=555" + "\r\n";
							text = text + "[SOFTBANK] http://hotenavi.com/555/" + "j/ownerbbs.jsp?HotelId=555" + "\r\n";
							text = text + "[AU]       http://hotenavi.com/555/" + "ez/ownerbbs.jsp?HotelId=555" + "\r\n";
							text = text + "PCからの確認はこちらのURLから↓" + "\r\n";
							text = text + " http://owner.hotenavi.com/555/" + "\r\n";
							text = text + "\r\n";
							text = text + "−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−\r\n";
							text = text + "★このメールに返信しても投稿されませんのでご注意ください。\r\n";
							text = text + "★このメールは投稿時に自動的に配信されています。\r\n";
							text = text + "−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−\r\n";

							SendMail sendmail = new SendMail();
							sendmail.send("webmaster@hotenavi.com", "sakurai-t1@almex.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", admin_mail, mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "k.takahashi_555@ezweb.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "kan_cat@t.vodafone.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "s39tomo1224v@docomo.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "0ty313417260f6d@ezweb.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "0gu3265d1w3312n@ezweb.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "s-oku@innerspace.co.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "hiko.kei1990.nakayoshi@docomo.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "0xm3881639m2t0x@ezweb.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "0jr3881536w0p3v@ezweb.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "youichi.kawase@ezweb.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "cba.19.37@docomo.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "muu-family-12-3@t.vodafone.ne.jp", mailsubject, text);
							sendmail.send("webmaster@hotenavi.com", "t-yamada@innerspace.co.jp", mailsubject, text);
						}
						else
						{
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
							text = text + "−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−\r\n";
							text = text + "★このメールに返信しても投稿されませんのでご注意ください。\r\n";
							text = text + "★このメールは投稿時に自動的に配信されています。\r\n";
							text = text + "−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−\r\n";
						SendMail sendmail = new SendMail();
							sendmail.send("webmaster@hotenavi.com", admin_mail, mailsubject, text);
						}

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
				if( ret != 0 )
				{
%>
								<%if (hotelid_multi.equals("555")){%>仮<%}%>投稿が完了しました。
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
					<td><a href="javascript:;" onMouseOver="MM_swapImage('yajirushi','','image/yajirushiGrey_f2.gif',1)" onMouseOut="MM_swapImgRestore()"><img src="<%=imageYajirushiGrey%>" name="yajirushi" width="15" height="13" border="0" id="yajirushi"></a><img src="<%=imageSpacer%>" width="10" height="12" align="absbottom"><A href="bbs_multi.jsp?HotelId=<%= hotelid %>&HotelId_multi=<%= hotelid_multi %>" onmouseover="MM_swapImage('yajirushi','','image/yajirushiGrey_f2.gif',1)" onmouseout="MM_swapImgRestore()" class="link1">掲示板に戻る</A></td>
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
