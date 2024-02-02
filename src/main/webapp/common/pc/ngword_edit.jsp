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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>ホテルオーナーサイト</title>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">

</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="mainframe">

	<tr valign="top">
		<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left">
						<form action="ngword_edit_form.jsp?HotelId=<%= hotelid %>" method=POST>
							<input name="submit00" type=submit value="新しくNG WORDを追加" class="submit_button">
						</form>
					</td>

				</tr>


				<tr>
					<td><img src="/hotenavi../../common/pc/image/spacer.gif" width="10" height="8"></td>
				</tr>

<%

				String		query;
				int i;
				// MySQLへの接続を確立する
				DbAccess db = new DbAccess();

				query = "SELECT * FROM bbs_ngword WHERE hotel_id=?";
				query = query + " ORDER BY id";

				// SQLクエリーの実行
				ResultSet result = db.execQuery(query,hotelid);
				while( result.next() != false )
				{
%>
				<tr>
					<td  colspan="2">
						<!-- ############### サブタイトルバー ############### -->
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="80%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="500">
									<div class="subtitle_text"><%= ReplaceString.HTMLEscape(result.getString("ng_word")) %></div>
								</td>
								<form action="ngword_edit_form.jsp?HotelId=<%= hotelid %>&Id=<%= result.getString("id") %>" method=POST>
								<td align="left">
									<table cellpadding="0" cellspacing="0">
										<tr>
											<td>&nbsp;</td>
											<td><input name="submit01" type="submit" value="編集する"></td>
										</tr>
									</table>
								</td>
								</form>
							<tr>
						</table>
					</td>
				</tr>
				<tr>
					<td  colspan="2"><img src="/hotenavi../../common/pc/image/spacer.gif" width="500" height="0"></td>
				</tr>
<%
				}
				db.close();
%>

				<tr>
					<td  colspan="2"><img src="/hotenavi../../common/pc/image/spacer.gif" width="500" height="50"></td>
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
							Copy right &copy; almex inc, All Rights Reserved
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


