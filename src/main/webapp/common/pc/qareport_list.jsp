<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/qareport_ini.jsp" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");

    ReplaceString rs = new ReplaceString();

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query       = "";
    connection  = DBConnection.getConnection();

    //ホテル名の取得
    String            hname = "";
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            hname       = result.getString("name");
            hname       = rs.replaceKanaFull(hname);
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
	

    String     msg_subtitle    = "";
    String     msg             = "";
    String     msg_sub         = "";

    java.sql.Date start;
    java.sql.Date end;
    int start_master_date  = 0;
    int start_master_year  = 0;
    int start_master_month = 0;
    int start_master_day   = 0;
    int end_master_date    = 0;
    int end_master_year    = 0;
    int end_master_month   = 0;
    int end_master_day     = 0;
    int member_flag     = 0;
    int i               = 0;
    int count           = 0;
    int count_record    = 0;
    int count_total     = 0;
    int duplicate_check = 0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>アンケートレポート</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<%}%>

</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

<%@ include file="../../common/pc/qareport_title.jsp" %>

				<tr>
					<td>
						<table width="100%" border="1" cellspacing="0" cellpadding="0"  bgcolor="#333333">
							<tr bgcolor="#FFFFCC"  valign="middle">
								<td class="size12 honbun" align="center" valign="middle" width="80" style="text-align:center;color:#333333">アンケートNo.</td>
								<td class="size12 honbun" align="center" valign="middle" style="text-align:center;color:#333333">
									アンケート期間
								</td>
								<td class="size12 honbun" align="center" valign="middle" width="60" style="text-align:center;color:#333333">
									回答数
								</td>
								<td class="size12 honbun" align="center" valign="middle" width="60" style="text-align:center;color:#333333">
									複数投稿
								</td>
							</tr>

<%
    Connection        connection_sub  = null;
    PreparedStatement prestate_sub    = null;
    ResultSet         result_sub      = null;
    connection_sub  = DBConnection.getConnection();
    try
    {
        query = "SELECT * FROM question_master WHERE hotel_id=?";
        query = query + " ORDER BY id DESC";
        prestate        = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result          = prestate.executeQuery();
        while( result.next() != false )
        {
            member_flag        = result.getInt("member_flag");
            duplicate_check    = result.getInt("duplicate_check");
            msg_subtitle = result.getString("msg");
            start = result.getDate("start");
            start_master_year  = start.getYear() + 1900;
            start_master_month = start.getMonth() + 1;
            start_master_day   = start.getDate();
            start_master_date  = start_master_year*10000 + start_master_month*100 + start_master_day;
            end   = result.getDate("end");
            end_master_year  = end.getYear() + 1900;
            end_master_month = end.getMonth() + 1;
            end_master_day   = end.getDate();
            end_master_date  = end_master_year*10000 + end_master_month*100 + end_master_day;
            count++;
%>
							<tr bgcolor="<%if (count % 2 == 0){%>#EEEEFF<%}else{%>#FFFFEE<%}%>"  valign="middle" onclick="location.href='qareport_list.jsp?Id=' + document.getElementById('Id_<%=count%>').value;top.Main.mainFrame.menu.location.href='qareport_menu.jsp?Id=' + document.getElementById('Id_<%=count%>').value;">
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333" valign="middle">
<%  
    if(result.getInt("id") == id)
    {
%>
									（選択中）
<%
    }
%>
									<%=result.getInt("id")%>
									<input type="hidden" value="<%=result.getInt("id")%>" id="Id_<%=count%>">
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333" valign="middle">
									<%=start_master_year%>年<%=start_master_month%>月<%=start_master_day%>日
									～
<% 
            if (end_master_year != 9999)
            {
%>
									<%=end_master_year%>年<%=end_master_month%>月<%=end_master_day%>日
<%
            }
            if (member_flag == 1)
            {
%>
					(メンバー用）
<%
            }
            if (member_flag > 1)
            {
%>
					【非掲載】
<%
            }
%>

								</td>
								<td class="size12 honbun" style="text-align:right;color:#333333" valign="middle">
<%
            query = "SELECT COUNT(seq),MAX(seq),MIN(seq) FROM question_answer WHERE hotel_id=?";
            query = query + " AND id=" + result.getInt("id");
            prestate_sub    = connection_sub.prepareStatement(query);
            prestate_sub.setString(1, hotelid);
            result_sub      = prestate_sub.executeQuery();
            if( result_sub.next() != false )
            {
                if (result_sub.getInt(1) != 0)
                {
%>
								<%= result_sub.getInt(2)-result_sub.getInt(3)+1%>
<%
                }
            }
            DBConnection.releaseResources(result_sub);
            DBConnection.releaseResources(prestate_sub);
%>
								&nbsp;</td>
								<td class="size12 honbun" style="text-align:center;color:#333333" valign="middle">
<% 
            if (duplicate_check == 1)
            {
%>
									不可
<%
            }
            else
            {
%>
									可
<%
            }
%>

								</td>
							</tr>
<%
        } // End of while( result.next() != false )
        DBConnection.releaseResources(connection_sub);
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
			</table>
		</td>
	</tr>
	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="10" height="50"></td>
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


