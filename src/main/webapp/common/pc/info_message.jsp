<%@ page contentType="text/html; charset=Windows-31J"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.hotenavi2.common.*"%>
<jsp:useBean id="ownerinfo" scope="session"
	class="com.hotenavi2.owner.OwnerInfo" />
<%
    boolean UpdateMode = false;
    boolean PastMode   = false;
    String  loginHotelId = (String)session.getAttribute("LoginHotelId");
    String  param_id     = ReplaceString.getParameter(request,"id");
    String  param_seq    = ReplaceString.getParameter(request,"seq");
    if     (param_id != null && param_seq != null && loginHotelId != null)
    {
        UpdateMode = true;
    }
    else
    {
        param_id      = "";
        param_seq     = "";
    }
    String  param_past   = ReplaceString.getParameter(request,"past");
    if (param_past == null)
    {
        param_past = "";
    }
    else if(param_past.equals("y"))
    {
        PastMode = true;
    }
    String  param_confirm = ReplaceString.getParameter(request,"confirm");
    if (param_confirm == null)
    {
        param_confirm = "";
    }
    // 本日の日付取得
    Calendar calnd;
    calnd = Calendar.getInstance();
    int now_year   = calnd.get(calnd.YEAR);
    int now_month  = calnd.get(calnd.MONTH) + 1;
    int now_day    = calnd.get(calnd.DATE);
    int now_hour   = calnd.get(calnd.HOUR_OF_DAY);
    int now_minute = calnd.get(calnd.MINUTE);
    int now_second = calnd.get(calnd.SECOND);
    int now_date   = now_year * 10000 + now_month  * 100 + now_day;
    int now_time   = now_hour * 10000 + now_minute * 100 + now_second;
    NumberFormat  nf2;
    nf2    = new DecimalFormat("00");

    String            query       = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_msg  = null;
    PreparedStatement prestate_msg    = null;
    ResultSet         result_msg      = null;

    int  count_msg = 0;
    int  count_new = 0;
    int  ret_msg   = 0;
    connection  = DBConnection.getConnection();
    if (UpdateMode)
    {
        try
        {
            query = "UPDATE hh_owner_message SET";
            if (param_confirm.equals("NO"))
            {
                query = query + " msg_disp_flag  = 0";
            }
            else
            {
                query = query + " msg_disp_flag  = 1,";
                query = query + " msg_check_date = " + now_date + ",";
                query = query + " msg_check_time = " + now_time + " ";
            }
            query = query + " WHERE id=? AND seq = ?";
            prestate   = connection.prepareStatement(query);
            prestate.setInt(1, Integer.parseInt(param_id));
            prestate.setInt(1,  Integer.parseInt(param_seq));
            ret_msg    = prestate.executeUpdate();
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
    try
    {
        query = "SELECT count(*) FROM hh_owner_message";
        query = query + " WHERE  hotelid    =?";
        query = query + " AND    userid     = " + ownerinfo.DbUserId;
        query = query + " AND    start_date <=" + now_date;
        query = query + " AND    end_date   >=" + now_date;
        query = query + " AND    hotenavi_flag != 0";
        query = query + " AND    mobile_flag   != 2";
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        result   = prestate.executeQuery();
        if( result != null)
        {
            if( result.next() != false )
            {
                count_msg = result.getInt(1);
            }
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

%>
<tr valign="top">
	<td bgcolor="#E2D8CF" valign="top" class="size12"
		style="padding: 10px 0px 0px 10px">★アルメックスからのお知らせ</td>
	<td height="3" width="3"><img src="../../common/pc/image/grey.gif"
		width="3" height="26"></td>
</tr>
<tr valign="top">
	<td bgcolor="#E2D8CF" valign="top">
		<table width="98%" border="0" cellspacing="1" cellpadding="0"
			align="center">
			<%
    try
    {
        query = "SELECT * FROM hh_owner_message";
        query = query + " WHERE  hotelid    =?";
        query = query + " AND    userid     = " + ownerinfo.DbUserId;
        query = query + " AND    start_date <=" + now_date;
        query = query + " AND    end_date   >=" + now_date;
        query = query + " AND    hotenavi_flag != 0";
        query = query + " AND    mobile_flag   != 2";
        if (!PastMode)
        {
            query = query + " AND    msg_disp_flag = 0";
        }
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        result   = prestate.executeQuery();
        connection_msg  = DBConnection.getConnection();
        if( result != null)
        {
            while( result.next() != false )
            {
                count_new++;
                if (result.getInt("alert_disp_flag")==0)
                {
                    query = "UPDATE hh_owner_message SET";
                    query = query + " alert_disp_flag  = 1,";
                    query = query + " alert_check_date = " + now_date + ",";
                    query = query + " alert_check_time = " + now_time + " ";
                    query = query + " WHERE id=" + result.getInt("id") + " AND seq = " + result.getInt("seq");
                    prestate_msg = connection.prepareStatement(query);
                    ret_msg      = prestate_msg.executeUpdate();
                    DBConnection.releaseResources(result_msg);
                    DBConnection.releaseResources(prestate_msg);
                }
                if (count_new == 1)
                {
%>
			<tr>
				<th width="60" class="tableLN" align="center">日付</th>
				<th class="tableLN" align="center" colspan="2">メッセージ</th>
				<th width="130" class="tableRN" align="center">確認</th>
			</tr>
			<%
                }
%>
			<tr>
				<td class="tableLW" style="background-color: #FFFFFF"><%= result.getInt("start_date") / 10000 %>/<%= nf2.format(result.getInt("start_date") / 100 % 100) %>/<%= nf2.format(result.getInt("start_date") % 100) %><br />
				</td>
				<td class="tableLW" style="background-color: #FFFFFF"><%= new String(result.getString("title").getBytes("Shift_JIS"), "Windows-31J" ) %>
				</td>
				<td class="tableLW" style="background-color: #FFFFFF"><%= new String(result.getString("msg").getBytes("Shift_JIS"), "Windows-31J" ) %>
				</td>
				<td class="tableRW" style="background-color: #FFFFFF">
					<%
                if (PastMode)
                {
                    if( result.getInt("msg_disp_flag")==0)
                    {
%> <strong><font color=red>未確認</font></strong><br> <a
					href="page.jsp?id=<%= result.getInt("id") %>&seq=<%= result.getInt("seq") %>">確認済みにする</a>
					<%
                    }
                    else
                    {
%> <a
					href="page.jsp?id=<%= result.getInt("id") %>&seq=<%= result.getInt("seq") %>&confirm=NO">未確認状態に戻す</a>
					<%
                    }
                }else{
%> <a
					href="page.jsp?id=<%= result.getInt("id") %>&seq=<%= result.getInt("seq") %>">確認したので表示しない</a>
					<%
                }
%>
				</td>
			</tr>
			<%
            }
        }
        DBConnection.releaseResources(connection_msg);
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
%>
			<tr>
				<td class="size12" style="background-color: #FFFFFF; padding: 5px"
					colspan=4 align=center>
					<%
    if (count_new == 0)
    {
%> 新しいメッセージはありません。 <%
        if(count_msg > 0)
        {
%> <br>
				<br> <%
        }
    }
    if (count_msg > 0)
    {
        if (PastMode)
        {
%> <a href="page.jsp">未確認のメッセージのみを表示する。</a> <%
        }
        else
        {
%> <a href="page.jsp?past=y">確認済みのメッセージも表示する。</a> <%
        }
    }
%>
				</td>
			</tr>
		</table>
	</td>
	<td bgcolor="#666666"><img src="../../common/pc/image/grey.gif"
		width="3"></td>
</tr>
<tr valign="top">
	<td bgcolor="#E2D8CF" valign="top"><img
		src="../../common/pc/image/spacer.gif" width="3" height="3"></td>
	<td bgcolor="#666666"><img src="../../common/pc/image/grey.gif"
		width="3"></td>
</tr>
<%
    DBConnection.releaseResources(connection);
%>
