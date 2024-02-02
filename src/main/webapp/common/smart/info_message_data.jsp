<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
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

    String loginHotelId =  (String)session.getAttribute("HotelId");

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_msg  = null;
    PreparedStatement prestate_msg    = null;
    ResultSet         result_msg      = null;

    int          userlevel;
    String       username;
    // ユーザレベルの取得
    userlevel = ownerinfo.DbUserLevel;
    // 名前の取得
    username = ownerinfo.DbUserName;

    boolean UpdateMode = false;
    boolean PastMode   = false;
    String  param_past   = request.getParameter("past");
    if (param_past == null)
    {
        param_past = "";
    }
    else if(param_past.equals("y"))
    {
        PastMode = true;
    }
    String  param_id     = request.getParameter("id");
    String  param_seq    = request.getParameter("seq");
    String  param_confirm = request.getParameter("confirm");

    if (param_id != null && param_seq != null && loginHotelId != null &&param_confirm != null)
    {
        UpdateMode = true;
    }

    int  count_msg = 0;
    int  count_new = 0;
    int  ret_msg   = 0;

    try
    {
        connection = DBConnection.getConnection();

        if (UpdateMode)
        {
            try
            {
                final String query;
                if (param_confirm.equals("NO"))
                {
                    query = "UPDATE hh_owner_message SET msg_disp_flag = 0 "
                          + "WHERE hotelid = ? AND id = ? AND seq = ? AND userid = ?";
                    prestate = connection.prepareStatement(query);
                    prestate.setString(1, loginHotelId);
                    prestate.setString(2, param_id);
                    prestate.setString(3, param_seq);
                    prestate.setInt(4, ownerinfo.DbUserId);
                }
                else
                {
                    query = "UPDATE hh_owner_message SET msg_disp_flag = 1, msg_check_date = ?, msg_check_time = ? "
                          + "WHERE hotelid = ? AND id = ? AND seq = ? AND userid = ?";
                    prestate = connection.prepareStatement(query);
                    prestate.setInt(1, now_date);
                    prestate.setInt(2, now_time);
                    prestate.setString(3, loginHotelId);
                    prestate.setString(4, param_id);
                    prestate.setString(5, param_seq);
                    prestate.setInt(6, ownerinfo.DbUserId);
                }
                ret_msg = prestate.executeUpdate();
            }
            catch( Exception e )
            {
                Logging.error("foward Exception e=" + e.toString(), e);
            }
            finally
            {
                DBConnection.releaseResources(result);
                DBConnection.releaseResources(prestate);
            }
        }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>個別ﾒｯｾｰｼﾞ</title>
<script type="text/javascript">
function hideAdBar(){
setTimeout("scrollTo(0,1)", 100);
if (window.orientation ==0){document.body.className="portrait";}else{document.body.className="landscape";}
}
</script>
<link rel="stylesheet" type="text/css" href="../../common/smart/iphone_index.css">
</head>
<body class="portrait" text="#555555" onLoad="hideAdBar()" onOrientationChange="hideAdBar();">
<jsp:include page="header.jsp" flush="true" />
<h2><%= username %>様へのメッセージ</h2>
<hr class="border">
<ul class="link">
<%-- 個別メッセージ --%>
<%
        try
        {
            final String query = "SELECT * FROM hh_owner_message "
                               + "WHERE  hotelid = ? "
                               + "AND    id = ? "
                               + "AND    seq = ? "
                               + "AND    userid = ? "
                               + "AND    start_date <= ? "
                               + "AND    end_date   >= ? "
                               + "AND    hotenavi_flag != 0 "
                               + "AND    mobile_flag   != 0";

            prestate = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setString(2, param_id);
            prestate.setString(3, param_seq);
            prestate.setInt(4, ownerinfo.DbUserId);
            prestate.setInt(5, now_date);
            prestate.setInt(6, now_date);

            result = prestate.executeQuery();
            if( result.next() )
            {
                if (result.getInt("alert_disp_flag") == 0)
                {
                    try
                    {
                        final String updateQuery = "UPDATE hh_owner_message SET "
                                                 + "alert_disp_flag  = 1, "
                                                 + "alert_check_date = ?, "
                                                 + "alert_check_time = ? "
                                                 + "WHERE id = ? AND seq = ?";

                        connection_msg = DBConnection.getConnection();
                        prestate_msg = connection.prepareStatement(updateQuery);
                        prestate_msg.setInt(1, now_date);
                        prestate_msg.setInt(2, now_time);
                        prestate_msg.setInt(3, result.getInt("id"));
                        prestate_msg.setInt(4, result.getInt("seq"));

                        ret_msg = prestate_msg.executeUpdate();
                    }
                    catch( Exception e )
                    {
                        Logging.error("foward Exception e=" + e.toString(), e);
                    }
                    finally
                    {
                        DBConnection.releaseResources(result_msg, prestate_msg, connection_msg);
                    }
                }
%>
<h2>[<%= result.getInt("start_date") / 10000 %>/<%= nf2.format(result.getInt("start_date") / 100 % 100) %>/<%= nf2.format(result.getInt("start_date") % 100) %>]</h2>
<table class="roomdetail2">
<tr>
<th><strong><%= result.getString("title") %></strong></th>
</tr>
<tr>
<td><%=result.getString("msg").replace("\r\n","<br>") %></td>
</tr>
</table>
<hr class="border">
<%
                if( result.getInt("msg_disp_flag")==0)
                {
%>
<strong><font color=red>未確認</font></strong>　
<a href="<%= response.encodeURL("info_message_data.jsp?id="+result.getInt("id")+"&seq=" +result.getInt("seq") +"&confirm=YES")%>">確認済にする</a>

<%
                }
                else
                {
%>
<font color=green>確認済</font>　
<a href="<%= response.encodeURL("info_message_data.jsp?id="+result.getInt("id")+"&seq=" +result.getInt("seq") +"&confirm=NO")%>">未確認状態に戻す</a>

<%
                }
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }
    finally
    {
        DBConnection.releaseResources(connection);
    }
%>
</ul>

<%-- 個別メッセージ --%>
<jsp:include page="info_message.jsp" flush="true">
  <jsp:param name="DispMessage" value="メッセージ一覧" />
</jsp:include>

<hr class="border">
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
