<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String loginHotelId =  (String)session.getAttribute("LoginHotelId");

    String[]  color1_t = new String[4];
    color1_t[0]        = "#FFFFFF";
    color1_t[1]        = "#FFFFFF";
    color1_t[2]        = "#FFFFFF";
    color1_t[3]        = "#FFFFFF";

    NumberFormat  nf2;
    nf2              = new DecimalFormat("00");
    String     query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connectionh = null;
    PreparedStatement prestateh   = null;
    ResultSet         resulth     = null;
    Connection        connectionx = null;
    PreparedStatement prestatex   = null;
    ResultSet         resultx     = null;
    Connection        connectionu = null;
    PreparedStatement prestateu   = null;
    ResultSet         resultu     = null;

    int      admin_flag          = 0;
    int      unknown_flag_pc     = 0;
    int      unknown_flag_mobile = 0;
    String   unknown_mailaddr_pc     = "";
    String   unknown_mailaddr_mobile = "";
    query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid=?";
    query = query + " AND owner_user_security.hotelid = owner_user.hotelid";
    query = query + " AND owner_user_security.userid  = owner_user.userid";
    query = query + " AND owner_user.userid  = ?";
    try
    {
        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1,loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result   = prestate.executeQuery();
        if( result != null )
        {
            if( result.next() != false )
            {
                admin_flag          = result.getInt("owner_user_security.admin_flag");
                unknown_flag_pc     = result.getInt("owner_user.unknown_flag_pc");
                unknown_flag_mobile = result.getInt("owner_user.unknown_flag_mobile");
                unknown_mailaddr_pc     = result.getString("owner_user.mailaddr_pc");
                unknown_mailaddr_mobile = result.getString("owner_user.mailaddr_mobile");
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

    if (admin_flag == 1)
    {
        // ユーザ一覧・セキュリティ情報取得
        query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid=?";
        query = query + " AND owner_user_security.hotelid = owner_user.hotelid";
        query = query + " AND owner_user_security.userid = owner_user.userid";
        query = query + " AND owner_user.imedia_user = 0";
        try
        {
            prestate = connection.prepareStatement(query);
            prestate.setString(1,loginHotelId);
            result   = prestate.executeQuery();
            connectionh = DBConnection.getConnection();
            connectionx = DBConnection.getConnection();
            connectionu = DBConnection.getConnection();

%>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:10px 0px 0px 10px">
			★登録ユーザーのログイン状況をご確認ください
		</td>
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="26"></td>
	  </tr>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top">
		<table width="98%" border="0" cellspacing="1" cellpadding="0" align="center">
		  <tr>
			<th width="180" align="center" valign="middle" nowrap class="tableLN">ユーザー名</th>
			<th width="120" align="center" valign="middle" nowrap class="tableLN">最終ログイン</th>
			<th class="tableRN">閲覧権限 他</th>
		  </tr>
		</table>
		</td>
		<td width="3"><img src="../../common/pc/image/grey.gif" width="3"  height="100%"></td>
	  </tr>
<%
            if( result != null )
            {
                while( result.next() != false )
                {
%>
	  <tr id="disp">
		<td bgcolor="#E2D8CF" valign="top">
		<table width="98%" border="0" cellspacing="1" cellpadding="0" align="center">
		  <tr>
			<td  width="175" valign="middle" class="tableLW" style="text-align:left;padding-left:5px;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">
				<a href="user_form.jsp?UserId=<%= result.getInt("owner_user.userid") %>">
				<%=  new String(result.getString("owner_user.name").getBytes("Shift_JIS"), "Windows-31J") %>
				</a>
				<% if  (result.getInt("owner_user_security.admin_flag") == 1)  {%>【管理者】<%}%><br>
			</td>
			<td  width="120" valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">
<%
                    query = "SELECT * FROM owner_user_login WHERE hotelid=?";
                    query = query + " AND userid=" + result.getInt("owner_user.userid");
                    prestateh = connection.prepareStatement(query);
                    prestateh.setString(1, loginHotelId);
                    resulth   = prestateh.executeQuery();
                    if (resulth.next())
                    {
%>
				<a href="owner_user_loginhistory.jsp?UserId=<%= result.getInt("owner_user.userid") %>&Type=hotenavi">
					<%= resulth.getDate("login_date") %>
					<%= resulth.getTime("login_time") %>
				</a>
<%
                    }
                    else if (result.getInt("passwd_pc_update") != 0)
                    {
                        query = "SELECT * FROM owner_user_log WHERE hotelid=?";
                        query = query + " AND userid=" + result.getInt("owner_user.userid");
                        query = query + " AND log_level = 1";
                        query = query + " ORDER BY logid DESC";
                        prestatex = connectionx.prepareStatement(query);
                        prestatex.setString(1, loginHotelId);
                        resultx   = prestatex.executeQuery();
                        if (resultx.next())
                        {
                            query = "INSERT INTO owner_user_login SET ";
                            query = query + "login_date = '" + resultx.getDate("login_date") + "',";
                            query = query + "login_time = '" + resultx.getTime("login_time") + "',";
                            query = query + "user_agent = '" + ReplaceString.SQLEscape(resultx.getString("user_agent")) + "',";
                            query = query + "remote_ip  = '" + ReplaceString.SQLEscape(resultx.getString("remote_ip"))  + "',";
                            query = query + "hotelid='" + loginHotelId + "',";
                            query = query + "userid = " +  result.getInt("owner_user.userid") + ",";
                            query = query + "kind = 1";
                            connectionu  = DBConnection.getConnection();
                            prestateu    = connectionu.prepareStatement(query);
                            prestateu.executeUpdate();
                            DBConnection.releaseResources(prestateu);
%>
				<a href="owner_user_loginhistory.jsp?UserId=<%= result.getInt("owner_user.userid") %>&Type=hotenavi">
					<%= resultx.getDate("login_date") %>
					<%= resultx.getTime("login_time") %>
				</a>
<%
                        }
                        else
                        {
%>
				ログイン履歴なし
<%
                        }
                        DBConnection.releaseResources(resultx);
                        DBConnection.releaseResources(prestatex);
%>
<%
                    }
                    DBConnection.releaseResources(resulth);
                    DBConnection.releaseResources(prestateh);
%>
			</td>
			<td valign="middle" class="tableRW" style="text-align:left;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">
				<font size=1>
				<% if  (result.getInt("owner_user_security.sec_level01") == 1) {%>【売上管理】<%}%>
				<% if  (result.getInt("owner_user_security.sec_level02") == 1) {%>【部屋情報】<%}%>
				<% if  (result.getInt("owner_user_security.sec_level03") == 1) {%>【帳票管理】<%}%>
				<% if  (result.getInt("owner_user_security.sec_level04") == 1) {%>【ＨＰレポート】<%}%>
				<% if  (result.getInt("owner_user_security.sec_level05") == 1) {%>【メルマガ作成】<%}%>
				<% if  (result.getInt("owner_user_security.sec_level06") == 1) {%>【ＨＰ編集】<%}%>
				<% if  (result.getInt("owner_user_security.sec_level07") == 1) {%>【設定メニュー】<%}%>
				<% if  (result.getInt("owner_user_security.sec_level15") == 1) {%>【ハピホテ編集】<%}%>
				<% if  (result.getInt("owner_user_security.sec_level16") == 1) {%>【クチコミ権限】<%}%><br>
				<% if (result.getInt("owner_user.unknown_flag_pc") ==1){%><font color=red>メールアドレスをご確認願います</font>:<a href="user_form.jsp?UserId=<%= result.getInt("owner_user.userid") %>"><%=result.getString("owner_user.mailaddr_pc")%></a><br><%}%>
				<% if (result.getInt("owner_user.unknown_flag_mobile") ==1){%><font color=red>メールアドレスをご確認願います</font>:<a href="user_form.jsp?UserId=<%= result.getInt("owner_user.userid") %>"><%=result.getString("owner_user.mailaddr_mobile")%></a><br><%}%>
				</font>
			</td>
		  </tr>
		</table>
		</td>
		<td width="3"><img src="../../common/pc/image/grey.gif" width="3"  height="100%"></td>
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
            DBConnection.releaseResources(connectionu);
            DBConnection.releaseResources(connectionx);
            DBConnection.releaseResources(connectionh);
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
%>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top">
			<img src="../../common/pc/image/spacer.gif" width="3" height="3">
		</td>
		<td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td>
	  </tr>
<%
    }
    else if (unknown_flag_pc == 1 || unknown_flag_mobile == 1)
    {
%>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top" class="size12" style="padding:10px 0px 0px 10px">
			★登録メールアドレスを確認してください
		</td>
		<td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="26"></td>
	  </tr>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top">
		<table width="98%" border="0" cellspacing="1" cellpadding="0" align="center">
		  <tr>
			<td bgcolor="#FFFFFF" valign="top" class="size12" style="padding:10px 50px 10px 10px">
			<%if (unknown_flag_pc     == 1){%><a href="user_form.jsp?UserId=<%= ownerinfo.DbUserId %>"><%=unknown_mailaddr_pc%></a><br><%}%>
			<%if (unknown_flag_mobile == 1){%><a href="user_form.jsp?UserId=<%= ownerinfo.DbUserId %>"><%=unknown_mailaddr_mobile%></a><%}%>
			</td>
		  </tr>
		</table>
		</td>
		<td width="3"><img src="../../common/pc/image/grey.gif" width="3" height="100%"></td>
	  </tr>
	  <tr valign="top">
		<td bgcolor="#E2D8CF" valign="top">
			<img src="../../common/pc/image/spacer.gif" width="3" height="3">
		</td>
		<td bgcolor="#666666"><img src="../../common/pc/image/grey.gif" width="3"></td>
	  </tr>
<%
    }
    DBConnection.releaseResources(connection);
%>
