<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int pageno;
    String param_page = ReplaceString.getParameter(request,"page");
    if( param_page == null )
    {
        pageno = 0;
    }
    else
    {
        pageno = Integer.parseInt(param_page);
    }

    int    userid;
    int    count = 0;
    int    total_count = 0;
    String query;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    String hotelid = ownerinfo.HotelId;
    String loginhotel = (String)session.getAttribute("LoginHotelId");

    String param_userid = ReplaceString.getParameter(request,"UserId");
    if( param_userid == null )
    {
        userid  = ownerinfo.DbUserId;
    }
    else
    {
        userid  = Integer.parseInt(param_userid);
    }
    try
    {
        query = "SELECT * FROM owner_user_log WHERE hotelid=?";
        query = query + " AND userid=?";
        query = query + " ORDER BY logid DESC";
        query = query + " LIMIT " + pageno * 1000 + "," + 1000;
        connection        = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,loginhotel );
        prestate.setInt(2,userid );
        result      = prestate.executeQuery();
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="32" class="tableLN">&nbsp;</td>
    <td width="83" align="center" valign="middle" nowrap class="tableLN">日付</td>
    <td width="88" align="center" valign="middle" nowrap class="tableLN">時間</td>
    <td width="190" align="center" valign="middle" nowrap class="tableLN">ログイン結果</td>
    <td width="634" align="center" valign="middle" nowrap class="tableLN">ユーザエージェント</td>
  </tr>

<%
        if( result != null)
        {
            while( result.next() )
            {
                count++;
                total_count = pageno * 1000 + count;
%>
  <tr>
    <td height="21" align="center" valign="middle" nowrap class="grey1"><%= total_count %></td>
    <td width="83" align="center" valign="middle" nowrap class="tableLW"><%= result.getDate("login_date") %></td>
    <td width="88" align="center" valign="middle" nowrap class="tableLW"><%= result.getTime("login_time") %></td>
    <td valign="middle" nowrap class="tableLW"><%= result.getString("log_detail") %></td>
    <td valign="middle" nowrap class="tableLW"><%= result.getString("user_agent") %></td>
  </tr>
<%
            }
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

</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="grey1">
<%
    if( pageno != 0 )
    {
%>
<a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + userid + "&page=" + (pageno-1) ) %>"><font color="#FFFFFF">←前</font></a>
<%
    }
%>

<%
    if( count > 999 )
    {
%>

<a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + userid + "&page=" + (pageno+1) ) %>"><font color="#FFFFFF">次→</font></a>

<%
    }
%>
	</td>
	</tr>
</table>


