<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ﾛｸﾞｲﾝｴﾗｰ</title>
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

<table class="roomdetail2">
<tr>
<th>ﾛｸﾞｲﾝｴﾗｰ</th></tr>
<%
    String hotelid;
    String loginid;
    String password;
    String Nid;
    String NPasswd;
    hotelid  = ReplaceString.getParameter(request,"hotelid");
    Nid      = ReplaceString.getParameter(request,"nid");
    NPasswd  = ReplaceString.getParameter(request,"npasswd");
    loginid  = ReplaceString.getParameter(request,"LoginId");
    password = ReplaceString.getParameter(request,"Password");

    int ret = Integer.parseInt(ReplaceString.getParameter(request,"result"));
    if( ret == 2 )
    {
%>
<tr><td>ログインできませんでした。<br>
ユーザー名が登録されていません。</td></tr>
<%
    }
    else if( ret == 3 )
    {
%>
<tr><td>ログインできませんでした。<br>
パスワードが違います。</td></tr>
<%
    }
    else
    {
%>
<tr><td>ログインできませんでした。<br>
エラーが発生しました。<br><br>

必ず「再入力」をクリックし、お手数ですが最初から入力してください。<br>
</td></tr>
<%
        DateEdit dateedit = new DateEdit();
        int nowdate = Integer.parseInt(dateedit.getDate(2));
        int nowtime = Integer.parseInt(dateedit.getTime(1));

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            final String query = "INSERT INTO owner_user_log SET "
                               + "hotelid = ?, "
                               + "login_date = ?, "
                               + "login_time = ?, "
                               + "login_name = ?, "
                               + "password = ?, "
                               + "log_level = ?, "
                               + "log_detail = ?, "
                               + "user_agent = ?, "
                               + "remote_ip = ?";

            prestate = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, nowdate);
            prestate.setInt(3, nowtime);
            prestate.setString(4, loginid);
            prestate.setString(5, password);
            prestate.setInt(6, ret);
            prestate.setString(7, "ログインエラー[" + Nid + "," + NPasswd + "]");
            prestate.setString(8, request.getHeader("user-agent") != null ? request.getHeader("user-agent") : "");
            prestate.setString(9, request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split(",")[0] : request.getRemoteAddr());

            prestate.executeUpdate();
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(connection);
        }
    }
%>
</table>
<hr class="border">
<div align="center">
<%
    if( ret == 0 )
    {
%>
<a href="logout.jsp">再入力</a><br>
<%
    }
    else
    {
%>
<a href="index.jsp">TOPへ</a><br>
<%
    }
%>
</div>
<hr class="border">
<div align="center" class="copyright">
Copyright(C) almex inc. All rights reserved.
</div>
</body>
</html>
