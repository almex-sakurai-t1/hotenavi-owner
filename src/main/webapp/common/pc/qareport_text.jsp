<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.ReplaceString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/qareport_ini.jsp" %>
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

    int     pageno;
    int     pagecount;
    int     startcount;

    String pagestr = ReplaceString.getParameter(request,"page");
    if( pagestr != null )
    {
        pageno = Integer.valueOf(pagestr).intValue();
    }
    else
    {
        pageno = 0;
    }

    String  reqpage           = ReplaceString.getParameter(request,"reqpage");
    if     (reqpage          == null) { reqpage         = "";}

    java.sql.Date start;
    java.sql.Date end;
    int start_master_date = 0;
    int start_master_year = 0;
    int start_master_month = 0;
    int start_master_day = 0;
    int end_master_date = 0;
    int end_master_year = 0;
    int end_master_month = 0;
    int end_master_day = 0;
    int member_flag     = 0;
    // アンケート期間・メッセージ読み込み
    try
    {
        query = "SELECT * FROM question_master WHERE hotel_id=?";
        query = query + " AND id=?";
        prestate        = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setInt(2,id);
        result          = prestate.executeQuery();
        if( result.next() != false )
        {
            member_flag  = result.getInt("member_flag");
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
            msg              = result.getString("q" + q_id + "_msg");
            type             = result.getInt("q" + q_id );
            type             = type % 10;
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
    if  (msg.length() < 2)
    {
        try
        {
            query = "SELECT * FROM question_data WHERE hotel_id=?";
            query = query + " AND id=?";
            query = query + " AND q_id=?";
            prestate        = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, id);
            prestate.setString(3,"q"+q_id);
            result          = prestate.executeQuery();
            if( result.next() != false )
            {
                msg = result.getString("msg");
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
    }
    int     i     = 0;
    int     count = 0;
    int     count_record = 0;
    int     count_total  = 0;
    try
    {
        query = "SELECT count(*) FROM question_answer WHERE hotel_id=?";
        query = query + " AND id=?";
        query = query + " AND q_id=?";
        query = query + " AND ans_date>=?";
        query = query + " AND ans_date<=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, id);
        prestate.setString(3,"q"+q_id);
        prestate.setInt(4, date_from);
        prestate.setInt(5, date_to);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           count_total = result.getInt(1);
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
    try
    {
        query = "SELECT count(*) FROM question_answer WHERE hotel_id=?";
        query = query + " AND id=?";
        query = query + " AND q_id=?";
        query = query + " AND ans_date>=?";
        query = query + " AND ans_date<=?";
        query = query + " AND answer != ','";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, id);
        prestate.setString(3,"q"+q_id);
        prestate.setInt(4, date_from);
        prestate.setInt(5, date_to);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           count_record = result.getInt(1);
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
    query = "SELECT * FROM question_answer WHERE hotel_id=?";
    query = query + " AND id=?";
    query = query + " AND q_id=?";
    query = query + " AND ans_date>=?";
    query = query + " AND ans_date<=?";
    query = query + " AND answer != ','";
    query = query + " ORDER BY seq DESC";
    query = query + " LIMIT " + pageno * 10 + "," + 11;
    String link_data = "?Id=" + id + "&Qid=" + q_id + "&Type=" + type;
    link_data = link_data  + "&StartYear=" + year_from + "&StartMonth=" +  month_from + "&StartDay=" + day_from;
    link_data = link_data  + "&EndYear="   + year_to   + "&EndMonth="   +  month_to   + "&EndDay="   + day_to;
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
<table width="100%" height="80%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

<%@ include file="../../common/pc/qareport_title.jsp" %>

				<tr>
					<td valign="top">
						<table width="100%" border="1" cellspacing="0" cellpadding="0"  bgcolor="#333333">
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" style="text-align:center;color:#333333">回答内容</td>
								<td class="size12 honbun" align="center" width="80" style="text-align:center;color:#333333">日　付</td>
								<td class="size12 honbun" align="center" width="75" style="text-align:center;color:#333333">メンバー</td>
							</tr>
<%
    try
    {
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, id);
        prestate.setString(3,"q"+q_id);
        prestate.setInt(4, date_from);
        prestate.setInt(5, date_to);
        result      = prestate.executeQuery();
        while( result.next() != false )
        {
            count++;
            if( count > 10 )
               {
                   // 次ページあり
                   break;
               }
%>
							<tr bgcolor="<%if (count % 2 == 0){%>#EEEEFF<%}else{%>#FFFFEE<%}%>">
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333">
									<%=(result.getString("answer") + ",,,,").replace("\r\n","<br>").replace(",,,,,","")%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=result.getDate("ans_date")%><br><%=result.getTime("ans_time")%>
								</td>
								<td class="size12 honbun" style="text-align:right;color:#333333">
									<%=result.getString("custom_id")%>&nbsp;
								</td>
							</tr>
<%
		} // End of while( result.next() != false )
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
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" style="text-align:center;color:#333333" colspan="3">
<%
    int per    = 0;
    if (count_total != 0)
    {
        per = Math.round((float)count_record*1000 /(float)count_total);
    }
%>
									回答者総数：<%=Kanma.get(count_total)%>　回答数：<%=Kanma.get(count_record)%><%if (count_total != 0){%>(<%= per/10 %>.<%= per % 10 %>%)<%}%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table width="100%" border="0" cellspacing="0" cellpadding="0"  bgcolor="#333333">
							<tr bgcolor="#FFFFCC">
<%
    if( pageno != 0 )
    {
%>
								<td align="left" class="size12 honbun" width="150">
									<a href="qareport_text.jsp<%= link_data %>" class="link1"  style="color:#FF6666">←先頭へ</a>
									<a href="qareport_text.jsp<%= link_data %>&page=<%= pageno - 1 %>" class="link1"  style="color:#FF6666">←前ページへ</a>
								</td>
<%
    }
    else
    {
%>
								<td width="150"><img src="../../common/pc/image/spacer.gif" width="10" height="10"></td>
<%
    }
%>


								<td align="center" class="size12 honbun" style="color:#000000">
<%
    if( pageno - 5 > 0 )
    {
        startcount = pageno - 5;
    }
    else
    {
        startcount = 0;
    }
    pagecount = 0;
    for( i = startcount ; i <= ((count_record -1)/ 10) ; i++ )
    {
        if( pageno == i )
        {
%>
									&nbsp;&nbsp;<%= i + 1 %>&nbsp;&nbsp;
<%
        }
        else
        {
%>
									&nbsp;&nbsp;<a href="qareport_text.jsp<%= link_data %>&page=<%= i %>" class="link1"  style="color:#FF6666"><%= i + 1 %></a>&nbsp;&nbsp;
<%
        }
        pagecount++;
        if( pagecount >= 10 )
        {
            break;
        }
    }
%>
								</td>

<%
    if( count > 10 )
    {
%>
								<td align="right" class="size12 honbun" width="150">
									<a href="qareport_text.jsp<%= link_data %>&page=<%= pageno + 1 %>" class="link1"  style="color:#FF6666">次ページへ→</a>	
<%
        if( count_record/10 != pageno )
        {
%>
									<a href="qareport_text.jsp<%= link_data %>&page=<%= (count_record + 9)/10 -1 %>" class="link1"  style="color:#FF6666">最後へ→</a>	
<%
        }
%>
								</td>
<%
    }else{
%>
								<td width="150"><img src="../../common/pc/image/spacer.gif" width="10" height="10"></td>
<%
    }
%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="500" height="50"></td>
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


