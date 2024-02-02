<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="java.sql.*" %><%@ page import="com.hotenavi2.common.*" %>
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

    int        i                  = 0;
    int        imedia_user        = 0;
    String     query              = "";

    int        member_flag     = 0;
    String     msg_subtitle    = "";
    String     msg_table[]     = new String[30];
    String     msg_sub_table[] = new String[30];
    int        type_table[]    = new int[30];
    int        required[]      = new int[30];
    int        menu_count      = 0;

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

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection      = DBConnection.getConnection();

    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
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

    // アンケートのメッセージ読み込み
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

            Connection        connection_sub  = null;
            PreparedStatement prestate_sub    = null;
            ResultSet         result_sub      = null;
            connection_sub  = DBConnection.getConnection();

            for(i = 0 ; i < 30 ; i++ )
            {
                type_table[i] = result.getInt("q" + (i+1));
                required[i]   =type_table[i] / 10; 
                type_table[i] = type_table[i] % 10;
                if( type_table[i] == 0 )
                {
                    break;
                }
                msg_table[i]  = result.getString("q" + (i+1) + "_msg");
                if (type_table[i] == 3 && msg_table[i].length() < 2)
                {
                    query = "SELECT * FROM question_data WHERE hotel_id=?";
                    query = query + " AND id=?";
                    query = query + " AND q_id=?";
                    prestate_sub    = connection_sub.prepareStatement(query);
                    prestate_sub.setString(1, hotelid);
                    prestate_sub.setInt(2, id);
                    prestate_sub.setString(3,"q" + (i + 1));
                    result_sub      = prestate_sub.executeQuery();
                    if( result_sub != null )
                    {
                        if( result_sub.next() != false )
                        {
                            if (result_sub.getString("msg") != null)
                            {
                                if (result_sub.getString("msg").length() > 1)
                                {
                                    msg_sub_table[i] = result_sub.getString("msg");
                                }
                            }
                        }
                    }
                    DBConnection.releaseResources(result_sub);
                    DBConnection.releaseResources(prestate_sub);
                }
            }
            menu_count = i;
            DBConnection.releaseResources(connection_sub);
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

    if (date_from < start_master_date)
    {
        date_from  = start_master_date;
        year_from  = start_master_year;
        month_from = start_master_month;
        day_from   = start_master_day;
    }
    if (date_to > end_master_date)
    {
        date_to  = end_master_date;
        year_to  = end_master_year;
        month_to = end_master_month;
        day_to   = end_master_day;
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>メニュー</title>
<script type="text/javascript" src="../../common/pc/scripts/date_range_check.js"></script>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<%}%>

</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="270" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="7" height="10"></td>
	</tr>
	<tr>
		<td>
		<!-- ############### サブタイトルバー1 ############### -->				
			<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
				<tr valign="middle">
					<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
					<td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
					<td width="3" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
					<td>
						<div class="size18 subtitle_text">
							アンケートレポート
						</div></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="7" height="10"></td>
	</tr>
	<form name="selectrange" action="qareport_<%= Contents[type] %>.jsp" method="post" target="contents" onsubmit="return validation_range(<%=date_to%>)">
	<input type="hidden" name="Qid"        value="<%= q_id %>">
	<input type="hidden" name="Type"       value="<%= type %>">
	<tr>
		<td class="size12 honbun">
			アンケートNo.
			<select name="Id" id="Id" onchange="location.href='qareport_menu.jsp?Id=' + document.getElementById('Id').value;top.Main.mainFrame.contents.location.href='qareport_list.jsp?Id=' + document.getElementById('Id').value;">
<%
    // アンケートNo読み込み
    try
    {
        query = "SELECT * FROM question_master WHERE hotel_id=?";
        prestate        = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result          = prestate.executeQuery();
        while( result.next() != false )
        {
%>
				<option value="<%= result.getInt("id")%>" <%if (result.getInt("id") == id){%>selected<%}%>><%= result.getInt("id")%></option>
<%
        }
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
			</select>
<%
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
	</tr>
	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="7" height="5"></td>
	</tr>
	<tr>
		<td class="size12 honbun">
			<select name="StartYear" id="StartYear" onChange="setDayRange(this);" id=>
<%
    for( i = start_master_year; i <= year_to ; i++ )
    {
%>
				<option value="<%=i %>" <% if (i == year_from){%>selected<%}%>><%= i %></option>
<%
    }
%>
			</select>年 
			<select size=1 name="StartMonth" id="StartMonth" onChange="setDayRange(this);" >
<%
    for( i = 1; i <= 12 ; i++ )
    {
%>
				<option value="<%= i %>" <% if (i == month_from) {%>selected<%}%>><%= i %></option>
<%
    }
%>
			</select>月 
			<select size=1 name="StartDay" id="StartDay"  onChange="setDayRange(this);">
<%
    for( i = 1 ; i <= 31 ; i++ )
    {
%>
				<option value="<%= i %>" <% if(i== day_from){%>selected<%}%>><%= i %></option>
<%
    }
%>
			</select>日 ～<br>
			<select name="EndYear" id="EndYear" onChange="setDayRange(this);">
<%
    for( i = start_master_year; i <= year_to ; i++ )
    {
%>
				<option value="<%=i %>" <% if (i == year_to){%>selected<%}%>><%= i %></option>
<%
    }
%>
			</select>年 
			<select size=1 name="EndMonth" id="EndMonth" onChange="setDayRange(this);" >
<%
    for( i = 1; i <= 12 ; i++ )
    {
%>
				<option value="<%= i %>" <% if (i == month_to) {%>selected<%}%>><%= i %></option>
<%
    }
%>
			</select>月 
			<select size=1 name="EndDay" id="EndDay"  onChange="setDayRange(this);">
<%
    for( i = 1 ; i <= 31 ; i++ )
    {
%>
				<option value="<%= i %>" <% if(i== day_to){%>selected<%}%>><%= i %></option>
<%
    }
%>
			</select>日
			<input type="submit"  value="再検索">
		</td></from>
	</tr>
	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="7" height="10"></td>
	</tr>
	<tr>
		<td align="left" valign="top" class="size12 honbun">↓項目を選択してください</td>
	</tr>
	<tr valign="top">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left" valign="top" class="honbun">

						<table width="100%" border="1" cellspacing="0" cellpadding="0"  bgcolor="#333333">
					<!-- アンケートのテーブル -->
<%
    for(i = 0; i <  menu_count ; i++)
    {
%>
							<tr <% if(i == (q_id -1)){%>bgcolor="#FFEEEE"<%}else{%>bgcolor="#EEEEFF"<%}%>  valign="middle">
								<td class="size12 honbun" style="text-align:left;padding-right:3px;color:#333333" valign="middle">
<%
        if (type_table[i] != 9)
        {
%>
									<a href="#" onclick="location.href='qareport_menu.jsp?Id=<%= id %>&Qid=<%= i + 1 %>&Type=<%=type_table[i]%>&StartYear=' + document.getElementById('StartYear').value + '&StartMonth=' + document.getElementById('StartMonth').value + '&StartDay=' + document.getElementById('StartDay').value + '&EndYear=' + document.getElementById('EndYear').value + '&EndMonth=' + document.getElementById('EndMonth').value + '&EndDay=' + document.getElementById('EndDay').value;top.Main.mainFrame.contents.location.href='qareport_<%=Contents[type_table[i]]%>.jsp?Type=<%=type_table[i]%>&Id=<%= id %>&Qid=<%= i + 1 %>&StartYear=' + document.getElementById('StartYear').value + '&StartMonth=' + document.getElementById('StartMonth').value + '&StartDay=' + document.getElementById('StartDay').value + '&EndYear=' + document.getElementById('EndYear').value + '&EndMonth=' + document.getElementById('EndMonth').value + '&EndDay=' + document.getElementById('EndDay').value;" class="link1" style="color:#000000">
<%
        }
        if(msg_table[i].length() > 1 )
        {
%>
										<%= msg_table[i] %>
<%
        }
        else
        {
%>
										<%= msg_sub_table[i] %>
<%
        }
        if (type_table[i] != 9)
        {
%>
									</a>
<%
        }
%>
<%
        if(required[i] == 1)
        {
%>
										<font size=1>*必須</font>
<%
        }
%>

									</div>
								</td>
							</tr>
<%
    }
%>
						</table>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="7" height="20"></td>
				</tr>
			</table>
			
		</td>
	</tr>

</table>

</body>
</html>
