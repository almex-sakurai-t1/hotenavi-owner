<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // ホテルID取得
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

		DateEdit dateedit = new DateEdit();

		String msgid = ReplaceString.getParameter(request,"msg_id");
		if( msgid == null )
		{
%>
				<jsp:forward page="error/error.html" />
<%
		}
        msgid        = ReplaceString.HTMLEscape(msgid);           //20100910追加
%>
<%@ include file="html_old_design.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html>
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
					<td class="honbun link1">

<%

		String subid = ReplaceString.getParameter(request,"sub_id");
		String delkey = ReplaceString.getParameter(request,"del_key");
		if (subid != null)                                 //20100910追加
		{
			 subid     = ReplaceString.HTMLEscape(subid);  //20100910追加
		}
		if (delkey != null)                                //20100910追加
		{
			 delkey    = ReplaceString.HTMLEscape(delkey); //20100910追加
		}

		try
		{
				int 			key;
				String		query;

				if( delkey == null || delkey.compareTo("") == 0 )
				{
						key = 0;
				}
				else
				{
						key = Integer.valueOf(delkey).intValue();
				}

				// MySQLへの接続を確立する
				DbAccess db =  new DbAccess();

				// データの取得
				query = "SELECT bbs_multi.del_key,bbs_multi.body,hotel.password FROM bbs_multi,hotel WHERE bbs_multi.hotel_id=?";
				query = query + " AND hotel.hotel_id=bbs_multi.hotel_id";
				query = query + " AND msg_id=?";

				if( subid != null )
				{
						query = query + " AND sub_id=?";
				}
				else
				{
						query = query + " AND sub_id=0";
				}
				List<Object> list = new ArrayList<Object>();
				//パラメータ追加
				list.add(hotelid_multi);
				list.add(msgid);
				if( subid != null )
				{
				    list.add(Integer.parseInt(subid));
				}
				else
				{
				    list.add(0);
				}
				// SQLクエリーの実行
				ResultSet result = db.execQuery(query,list);
				if( result.next() != false )
				{
						// 削除キーのチェック
						if( key == result.getInt("bbs_multi.del_key") || key == result.getInt("hotel.password") )
						{
							String memo = "【削除】" + dateedit.getDate(0) + " " + dateedit.getTime(0);
							if (key == result.getInt("bbs_multi.del_key"))
							{
								memo = memo + " ユーザーにより削除（オーナーサイト）";
							}
							else
							{
								memo = memo + " オーナーにより削除（オーナーサイト）";
							}
							memo = memo + "[" + request.getHeader("user-agent") + "]";
							memo = memo + "[" + ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() ) + " " + "]";
							memo = result.getString("body") + memo;
							DbAccess db_del =  new DbAccess();
							// 削除キーＯＫ
							if (hotelid_multi.equals("555"))
							{
								query = "UPDATE bbs_multi SET del_flag=9 WHERE hotel_id=?";
							}
							else
							{
								query = "UPDATE bbs_multi SET del_flag=1,body= ? WHERE hotel_id=?";
							}
							query = query + " AND msg_id=?";
							if( subid != null )
							{
								// 特定の返信データのみ更新
								query = query + " AND sub_id=?";
							}
							List<Object> list_del = new ArrayList<Object>();
							//パラメータ追加
							if (!hotelid_multi.equals("555"))
							{
 								list_del.add(memo);
							}
							list_del.add(hotelid_multi);
							list_del.add(msgid);
							if( subid != null )
							{
				    			list_del.add(Integer.parseInt(subid));
							}
							else
							{
				    			list_del.add(0);
							}
							// SQLクエリーの実行
							int ret;
							ret = db_del.execUpdate(query,list_del);
							db_del.close();
%>
		削除しました。
<%
		}
		else
		{
%>
		削除キーが違います。
<%
		}
%>
<%
				}
				else
				{
%>
		対象のレコードなし
<%
				}

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
