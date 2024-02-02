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


    Connection        connection_sub  = null;
    PreparedStatement prestate_sub    = null;
    ResultSet         result_sub      = null;
    connection_sub  = DBConnection.getConnection();

    int     i     = 0;
    int     count = 0;
    int     per   = 0;
    String[]t_msg    = new String[30];
    int[]   t_count  = new int[30];
    int     max   = 0;
    int     total = 0;
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
           count = result.getInt(1);
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
        query = "SELECT * FROM question_data WHERE hotel_id=?";
        query = query + " AND id=?";
        query = query + " AND q_id=?";
        prestate        = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, id);
        prestate.setString(3,"q"+q_id);
        result          = prestate.executeQuery();
        connection_sub  = DBConnection.getConnection();
        while( result.next() != false )
        {
            t_msg[(result.getInt("sub_id")-1)] = result.getString("msg");
            query = "SELECT count(*) FROM question_answer WHERE hotel_id=?";
            query = query + " AND id=?";
            query = query + " AND q_id=?";
            query = query + " AND ans_date>=?";
            query = query + " AND ans_date<=?";
            query = query + " AND concat(answer,',') LIKE '" + result.getInt("value") + ",%'";
            prestate_sub    = connection_sub.prepareStatement(query);
            prestate_sub.setString(1, hotelid);
            prestate_sub.setInt(2, id);
            prestate_sub.setString(3,"q"+q_id);
            prestate_sub.setInt(4, date_from);
            prestate_sub.setInt(5, date_to);
            result_sub      = prestate_sub.executeQuery();
            if( result_sub.next() != false )
            {
                t_count[(result.getInt("sub_id")-1)] = result_sub.getInt(1);
                total = total + t_count[(result.getInt("sub_id")-1)];
                if (t_count[(result.getInt("sub_id")-1)]>max)
                {
                    max = t_count[(result.getInt("sub_id")-1)];
                }
            }
            DBConnection.releaseResources(result_sub);
            DBConnection.releaseResources(prestate_sub);
        }
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
    if (msg.length() < 2)
    {
        msg = t_msg[0];
    }
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
					<td valign="top">
						<table width="660" border="1" cellspacing="0" cellpadding="0"  bgcolor="#333333">
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" width="200" style="text-align:center;color:#333333">選択肢</td>
								<td class="size12 honbun" align="center" colspan="2" style="text-align:center;color:#333333">票数</td>
								<td class="size12 honbun" align="center" style="text-align:center;color:#333333">
									<img src="../../common/pc/image/spacer.gif" width="1" height="3">
								</td>
							</tr>
<%
    for (i = 0;i < 30; i++)
    {
        if(t_msg[i]!= null)
        {
%>
							<tr bgcolor="<%if (i % 2 == 0){%>#EEEEFF<%}else{%>#FFFFEE<%}%>">
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333">
									<%=t_msg[i]%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
									<%=Kanma.get(t_count[i])%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333">
<%
            per        = 0;
            if (count != 0)
            {
                per = Math.round((float)t_count[i]*1000 /(float)count);
            }
%>
									&nbsp;<%if (count != 0){%><%= per/10 %>.<%= per % 10 %>%<%}%>
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333">
<%
            if (t_count[i] != 0 && max != 0)
            {
%>
									<img src="../../common/pc/image/bar_aqua.gif" width="<%= per*330 / 1000%>" height="12" border="0">
<%
            }
%>&nbsp;
								</td>
							</tr>
<%
        }
    }
%>
							<tr bgcolor="#FFFFCC">
								<td class="size12 honbun" align="center" width="200" style="text-align:center;color:#333333">
									合　計（回答数：<%=count%>）
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333" width="45">
									<%=Kanma.get(total)%>
								</td>
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333" width="50">
<%
    per        = 0;
    if (count != 0)
    {
        per = Math.round((float)total*1000 /(float)count);
    }
%>
									&nbsp;<%if (count != 0){%><%= per/10 %>.<%= per % 10 %>%<%}%>
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333">
									<img src="../../common/pc/image/spacer.gif" width="1" height="3">
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


