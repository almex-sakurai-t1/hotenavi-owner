<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.ReplaceString" %>
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
    String paramId   = ReplaceString.getParameter(request,"Id");
    if    (paramId  == null) paramId="0";
    int qid         = Integer.parseInt(paramId);

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
    boolean    MemberExist  = false;
    boolean    VisitorExist = false;
    NumberFormat nf2       = new DecimalFormat("00");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>アンケート編集</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<%}%>

</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="270px" border="0" cellpadding="0" cellspacing="0">
	<tr valign="top">
		<td>
			<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
				<tr>
					<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
					<td width="3"><img src="../../common/pc/image/spacer.gif" width="10" height="18"></td>
					<td><div class="size14 subtitle_text">アンケート編集</div></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr valign="top">
		<td class="size10">
			<img src="../../common/pc/image/spacer.gif" width="10" height="1">
		</td>
	</tr>
	<tr >
		<td class="size14 subtitle_text">
			<input type="button" value="メッセージ設定" onclick="location.href='qaedit_menu.jsp?Id=0';top.Main.mainFrame.contents.location.href='qaedit_message.jsp';">
		</td>
	</tr>
	<tr >
		<td class="size14 subtitle_text">
			<input type="button" value="アンケート新規追加" onclick="location.href='qaedit_menu.jsp?Id=0';top.Main.mainFrame.contents.location.href='qaedit_form.jsp?Id=0';">
		</td>
	</tr>

	<tr valign="top">
		<td class="size10">
			<img src="../../common/pc/image/spacer.gif" width="10" height="1">
		</td>
	</tr>
	<tr >
		<td>
			<table width="100%" border="1" cellspacing="0" cellpadding="0"  bgcolor="#333333">

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
            if (count == 1)
            {
%>
							<tr bgcolor="#FFFFCC"  valign="middle">
								<td class="size12 honbun" align="center" valign="middle" width="10" style="text-align:center;color:#333333">No.</td>
								<td class="size12 honbun" align="center" valign="middle" style="text-align:center;color:#333333">
									アンケート期間
								</td>
								<td class="size12 honbun" align="center" valign="middle" width="35" style="text-align:center;color:#333333">
									回答
								</td>
								<td class="size12 honbun" align="center" valign="middle" width="40" style="text-align:center;color:#333333">
									編集
								</td>
							</tr>
<%
            }
%>
							<tr <% if(result.getInt("id") == qid){%>bgcolor="#FFEEEE"<%}else{%>bgcolor="#EEEEFF"<%}%>  valign="middle">
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333" valign="middle"  onclick="location.href='qaedit_menu.jsp?Id=' + document.getElementById('Id_<%=count%>').value;top.Main.mainFrame.contents.location.href='qaedit_view.jsp?Id=' + document.getElementById('Id_<%=count%>').value;">
									<%= result.getInt("id")%>
									<input type="hidden" value="<%=result.getInt("id")%>" id="Id_<%=count%>">
								</td>
								<td class="size12 honbun" style="text-align:left;padding-left:3px;color:#333333" valign="middle"  onclick="location.href='qaedit_menu.jsp?Id=' + document.getElementById('Id_<%=count%>').value;top.Main.mainFrame.contents.location.href='qaedit_view.jsp?Id=' + document.getElementById('Id_<%=count%>').value;">
									<%=start_master_year%>/<%=nf2.format(start_master_month)%>/<%=nf2.format(start_master_day)%>
									～
<% 
            if (end_master_year != 9999)
            {
%>
									<%=end_master_year%>/<%=nf2.format(end_master_month)%>/<%=nf2.format(end_master_day)%>
<%
            }
%>
									<br>
<%
            if (start_master_date <= nowdate && end_master_date >= nowdate)
            {
                if(member_flag == 1)
                {
                    if(!MemberExist)
                    {
                        MemberExist = true;
%>
									*掲載中
<%
                    }
                }
                if(member_flag == 0)
                {
                    if(!VisitorExist)
                    {
                        VisitorExist = true;
%>
									*掲載中
<%
                    }
                }
            }
%>
<%
            if (member_flag == 1)
            {
%>
									【メンバー用】
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
								<td class="size12 honbun" style="text-align:right;padding-right:3px;color:#333333" valign="middle" onclick="location.href='qaedit_menu.jsp?Id=' + document.getElementById('Id_<%=count%>').value;top.Main.mainFrame.contents.location.href='qaedit_view.jsp?Id=' + document.getElementById('Id_<%=count%>').value;">
<%
            count_record = 0;
            query = "SELECT COUNT(seq),MAX(seq),MIN(seq) FROM question_answer WHERE hotel_id=?";
            query = query + " AND id=" + result.getInt("id");
            prestate_sub    = connection_sub.prepareStatement(query);
            prestate_sub.setString(1, hotelid);
            result_sub      = prestate_sub.executeQuery();
            if( result_sub.next() != false )
            {
                if (result_sub.getInt(1) != 0)
                {
                    count_record = result_sub.getInt(2)-result_sub.getInt(3)+1;
                }
            }
            DBConnection.releaseResources(result_sub);
            DBConnection.releaseResources(prestate_sub);
%>
									<%= count_record %>
								</td>
								<td class="size12 honbun" style="text-align:center;color:#333333" valign="middle">
<%
            if (count_record == 0)
            {
%>
									<input type="button" value="編集" onclick="location.href='qaedit_menu.jsp?Id=' + document.getElementById('Id_<%=count%>').value;top.Main.mainFrame.contents.location.href='qaedit_form.jsp?Id=' + document.getElementById('Id_<%=count%>').value;">
<%
            }
            else
            {
%>
									<input type="button" value="編集" onclick="location.href='qaedit_menu.jsp?Id=' + document.getElementById('Id_<%=count%>').value;top.Main.mainFrame.contents.location.href='qaedit_form.jsp?Id=' + document.getElementById('Id_<%=count%>').value;">
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
	<tr >
		<td>
			<table width="100%" border="1" cellspacing="0" cellpadding="0"  bgcolor="#333333">
				<tr bgcolor="#FFFFCC"  valign="middle">
					<td class="size10 honbun" align="center" valign="middle" style="text-align:center;color:#333333">
						※新規に追加・編集する場合は、「下書き保存（非掲載）」をご利用ください。対象Noの行をクリックすると掲載イメージをご覧いただけます。
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

</body>
</html>


