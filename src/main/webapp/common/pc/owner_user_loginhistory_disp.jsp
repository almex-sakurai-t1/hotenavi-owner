<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String loginHotelId =  (String)session.getAttribute("LoginHotelId");
    String userid = ReplaceString.getParameter(request,"UserId");
    if( userid   == null )
    {
        userid = "0";
    }
    String Type = ReplaceString.getParameter(request,"Type");
    if( Type   == null )
    {
        Type = "hotenavi";
    }
    if( !CheckString.alphabetCheck(Type) )
    {
        Type="";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    String EDIT = ReplaceString.getParameter(request,"EDIT");

    String query = "";
    String group_name = "";
    String user_name    = "";
    String user_loginId = "";
    Connection db   = null;
    PreparedStatement  st   = null;
    ResultSet  ret  = null;
    Connection db_user   = null;
    PreparedStatement  st_user   = null;
    ResultSet  ret_user  = null;

    if(Integer.parseInt(userid) != 0)
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        db = DBConnection.getConnection();
        st =db.prepareStatement(query);
        st.setString(1, loginHotelId);
        st.setInt(2, Integer.parseInt(userid));
        ret = st.executeQuery();
        if( ret != null )
        {
            if( ret.next() != false )
            {
            user_loginId  = ret.getString("loginid");
            user_name     = ret.getString("name");
            }
        }
        DBConnection.releaseResources(ret,st,db);
    }

    query = "SELECT * FROM hotel WHERE hotel_id=?";
    db = DBConnection.getConnection();
    st =db.prepareStatement(query);
    st.setString(1, loginHotelId);
    ret = st.executeQuery();
    if( ret != null )
    {
        if( ret.next() != false )
        {
            group_name  = ret.getString("name");
        }
    }
    DBConnection.releaseResources(ret,st,db);

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

    String login_date_s = "";
    String login_time_s = "";
    int    count = 0;
    int    total_count = 0;

if  (Type.compareTo("hotenavi") == 0)
{
    query = "SELECT * FROM owner_user_log WHERE hotelid=?";
    query = query + " AND userid=" + Integer.parseInt(userid);
    query = query + " ORDER BY logid DESC";
}
else
{
    query = "SELECT * FROM hh_owner_user_log WHERE hotel_id=?";
    if (EDIT != null)
    {
        query = query + " AND log_level>=200";
        query = query + " AND log_level<300";
        if (userid.compareTo("0") != 0)  query = query + " AND user_id=" + Integer.parseInt(userid);
    }
    else
    {
        query = query + " AND user_id=" + Integer.parseInt(userid);
    }
    query = query + " ORDER BY login_date DESC,login_time DESC,seq";
}
    query = query + " LIMIT " + pageno * 20 + "," + 21;

    db = DBConnection.getConnection();
    st =db.prepareStatement(query);
    st.setString(1, loginHotelId);
    ret = st.executeQuery();
%>
			<tr>
				<td colspan=3>

<table width="100%" border="0" cellspacing="1" cellpadding="0" class="search_hoteldetail">
  <tr>
     <td colspan="6" class="tableLN" align="left"><% if(Integer.parseInt(userid) != 0){%><%=user_name%>さん（<%=user_loginId%>）の<%}%>&nbsp;<b><%=Type%></b>&nbsp;<%=group_name%>(<%=loginHotelId%>)オーナーサイト<% if(EDIT != null){%>　ユーザー管理修正<%}else{%>へのログイン<%}%>履歴</td>
  </tr>
  <tr>
    <td width="20" class="tableLN">&nbsp;</td>
    <td width="64" class="tableLN">日付</td>
    <td width="48" class="tableLN">時間</td>
<% if(EDIT == null){%>
    <td width="190" class="tableLN">ログイン結果</td>
    <td width="80"  class="tableLN">IPアドレス</td>
    <td  class="tableLN">ユーザエージェント</td>
<%}else{%>
    <td width="100" class="tableLN">ユーザーID</td>
    <td width="130" class="tableLN">名前</td>
    <td  class="tableLN">修正内容</td>
<%}%>

  </tr>

<%
    while( ret.next() != false )
    {
        count++;
        if (count > 20) break;
        total_count  = pageno * 20 + count;
%>
  <tr>
<%
if  (Type.compareTo("hotenavi") == 0)
{
%>
    <td  class="tableWhite" style="text-align:right" nowrap><%= total_count %></td>
    <td  class="tableWhite" nowrap><%= ret.getDate("login_date") %></td>
    <td  class="tableWhite" nowrap><%= ret.getTime("login_time") %></td>
    <td  class="tableWhite"><%= ReplaceString.HTMLEscape(ret.getString("log_detail")) %></td>
    <td  class="tableWhite"><%= ret.getString("remote_ip") %></td>
    <td  class="tableWhite"><%= ret.getString("user_agent") %></td>
<%
}
else
{
        login_date_s = Integer.toString(ret.getInt("login_date")+100000000);
        login_time_s = Integer.toString(ret.getInt("login_time")+1000000);

%>
    <td  class="tableWhite" style="text-align:right" nowrap><%= total_count %></td>
    <td  class="tableWhite"><%= login_date_s.substring(1,5) %>/<%= login_date_s.substring(5,7) %>/<%= login_date_s.substring(7,9) %></td>
    <td  class="tableWhite"><%= login_time_s.substring(1,3) %>:<%= login_time_s.substring(3,5) %>:<%= login_time_s.substring(5,7) %></td>
    <% if(EDIT == null){%>
    <td  class="tableWhite"><%= ReplaceString.HTMLEscape(ret.getString("log_detail")) %></td>
    <td  class="tableWhite"><%= ret.getString("remote_ip") %></td>
    <td  class="tableWhite"><%= ret.getString("user_agent") %></td>
    <%}else{
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid= ? ";
        db_user = DBConnection.getConnection();
        st_user = db_user.prepareStatement(query);
        st_user.setString(1, loginHotelId );
        st_user.setInt(2,ret.getInt("user_id"));
        ret_user = st_user.executeQuery();
        if( ret_user != null )
        {
            if( ret_user.next() != false )
            {
%>
        <td  class="tableWhite">(<%=ret.getInt("user_id")%>)<%= ret_user.getString("loginid") %></td>
        <td  class="tableWhite"><%= ret_user.getString("name") %></td>
        <td  class="tableWhite"><%= ReplaceString.HTMLEscape(ret.getString("log_detail")) %></td>
<%
            }
            else
            {
%>
        <td  class="tableWhite">(<%=ret.getInt("user_id")%>)</td>
        <td  class="tableWhite">&nbsp;</td>
        <td  class="tableWhite"><%= ReplaceString.HTMLEscape(ret.getString("log_detail")) %></td>
<%
            }
        }
        DBConnection.releaseResources(ret_user,st_user,db_user);
    }%>

<%
}
%>
  </tr>
<%

    }

    DBConnection.releaseResources(ret,st,db);
%>

</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="honbun" width="90%">
<% if(EDIT == null){%>
<%
    if( pageno != 0 )
    {
%>
<a href="#" onclick="postUrl('<%= response.encodeURL("owner_user_loginhistory.jsp?UserId=" + userid + "&page=" + (pageno-1) + "&Type=" + Type) %>');">←前</a>
<%
    }
%>
	<td class = "honbun">
<%
    if( count > 20 )
    {
%>

<a href="#" onclick="postUrl('<%= response.encodeURL("owner_user_loginhistory.jsp?UserId=" + userid + "&page=" + (pageno+1) + "&Type=" + Type) %>');">次→</a>

<%
    }
%>
<%}else{%>
<%
    if( pageno != 0 )
    {
%>
<a href="#" onclick="postUrl('<%= response.encodeURL("owner_user_loginhistory.jsp?UserId=" + userid + "&page=" + (pageno-1) + "&Type=" + Type + "&EDIT") %>');">←前</a>
<%
    }
%>
	<td class = "honbun">
<%
    if( count > 20 )
    {
%>

<a href="#" onclick="postUrl('<%= response.encodeURL("owner_user_loginhistory.jsp?UserId=" + userid + "&page=" + (pageno+1) + "&Type=" + Type + "&EDIT") %>');">次→</a>

<%
    }
%>

<%}%>

	</td>
	</tr>
</table>

				</td>
			</tr>
<%@ include file="../csrf/csrf.jsp" %>


