<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String requestUri = request.getRequestURI();
    String hotelid    = requestUri.replace("_debug_","");
    hotelid           = hotelid.replace("i/passwdreminder.jsp","");
    hotelid           = hotelid.replace("ez/passwdreminder.jsp","");
    hotelid           = hotelid.replace("j/passwdreminder.jsp","");
    hotelid           = hotelid.replace("smart/passwdreminder.jsp","");
    hotelid           = hotelid.replace("/","");
    String loginid    = request.getParameter("loginid");
    if (loginid == null) loginid = "";
    String mailaddr_mobile = request.getParameter("mailaddr_mobile");
    if (mailaddr_mobile == null) mailaddr_mobile = "";
    
    boolean remindMode = false;
    if (!loginid.equals("") && !mailaddr_mobile.equals(""))
    {
        remindMode = true;
    }

    Calendar cal  = Calendar.getInstance();
    int now_year  = cal.get(cal.YEAR);
    int now_month = cal.get(cal.MONTH) + 1;
    int now_day   = cal.get(cal.DATE);
    int nowdate   = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime   = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    int userid = 0;

    if (remindMode)
    {
        String  name       = "";
        String  group_name = "";
        String  from_mail  = "";
        String  passwd_pc  = "";

        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            final String query = "SELECT * FROM hotel WHERE hotel_id = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
 
            result = prestate.executeQuery();
            if(result.next())
            {
                from_mail = result.getString("mail");
                if (from_mail.equals(""))
                    from_mail = "imedia-info@hotenavi.com"; 

                group_name = result.getString("name");
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
            DBConnection.releaseResources(connection);
        }

        try
        {
            final String query = "SELECT * FROM owner_user WHERE hotelid = ? "
                               + "AND loginid = ? "
                               + "AND (mailaddr_mobile = ? OR mailaddr_pc = ?)";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setString(2, loginid);
            prestate.setString(3, mailaddr_mobile);
            prestate.setString(4, mailaddr_mobile);

            result = prestate.executeQuery();
            if(result.next())
            {
                userid = result.getInt("userid");
                name   = result.getString("name");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result, prestate, connection);
        }

        if (userid != 0)
        {
            passwd_pc = RandomString.getRandomNumber(4);

            try
            {
                final String query = "UPDATE owner_user SET passwd_pc_update = 0, passwd_pc = ? WHERE hotelid = ? AND userid = ?";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement(query);
                prestate.setString(1, passwd_pc);
                prestate.setString(2, hotelid);
                prestate.setInt(3, userid);

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

            // ���[���̑��M�i�p�X���[�h�j
            String title_mail = "";
            String text = "";
            title_mail  = "�y�z�e�i�r�z[�I�[�i�[�T�C�g�p�X���[�h]" + group_name + "�i" + hotelid + "�j";
            text =         "���z�e�i�r�I�[�i�[�T�C�g�̉��p�X���[�h�����s����܂����B" + "\r\n";
            text = text +  "\r\n";
            text = text +  "�y�z�e�i�r�I�[�i�[�T�C�g�z" + "\r\n";
            text = text +  "https://owner.hotenavi.com/" + hotelid + "/" + "\r\n";
            text = text +  "�y���O�z" + "\r\n";
            text = text +  name + "\r\n";
            text = text +  "�y�l�p�X���[�h�z" + "\r\n";
            text = text +  passwd_pc + "\r\n";
            text = text +  "\r\n";
            text = text +  "------------------\r\n";
            text = text +  "�I�[�i�[�T�C�g�Ƀ��O�C�����A�l�p�X���[�h��ύX���Ă��������B" + "\r\n";
            text = text +  "------------------\r\n";
            SendMail sendmail = new SendMail();
            sendmail.send(from_mail, mailaddr_mobile, title_mail, text);
            sendmail.send(from_mail, "sakurai-t1@almex.jp", title_mail, text);

            String userAgent = request.getHeader("user-agent");
            if (userAgent.length() > 255)
            {
                userAgent = userAgent.substring(0,255);
            }

            try
            {
                final String query = "INSERT INTO hh_owner_user_log SET "
                                   + "hotel_id = ?, "
                                   + "user_id = ?, "
                                   + "login_date = ?, "
                                   + "login_time = ?, "
                                   + "login_name = ?, "
                                   + "passwd = ?, "
                                   + "log_level = 230, "
                                   + "log_detail = '�X�}�z�i���j�p�X���[�h���s', "
                                   + "user_agent = ?, "
                                   + "remote_ip = ?";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement(query);
                prestate.setString(1, hotelid);
                prestate.setInt(2, userid);
                prestate.setInt(3, nowdate);
                prestate.setInt(4, nowtime);
                prestate.setString(5, loginid);
                prestate.setString(6, passwd_pc);
                prestate.setString(7, userAgent);
                prestate.setString(8, request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr());

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
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�p�X���[�h���}�C���_�[</title>
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
<h1 class="title">�p�X���[�h�Ĕ��s</h1>
<ul class="link">
<%
    if (remindMode)
    {
        if (userid != 0)
        {
%>
<font color=red><strong>���p�X���[�h�����s����܂����B</strong></font><br>
���[�������M����Ă��܂��̂ŁA�I�[�i�[�T�C�g�Ƀ��O�C�����A�l�p�X���[�h��ύX���Ă��������B<br>
<hr class="border" align="center">
<li><a href="index.jsp"><span class="title">TOP��</span></a></li>
<%
        }
        else
        {
%>
<font color=red><strong>���͂����l���[�U�[���������̓��[���A�h���X���Ⴂ�܂��B</strong></font><br>
<hr class="border">

<li><a href="passwdreminder.jsp"><span class="title">�ē��͂���</span></a></li>

<%
        }
    }
    else
    {
%>
<font color=red><strong>�u�l�p�X���[�h�i���j�v���Ĕ��s���܂��̂ŁA�l���[�U�[���ƃ��[���A�h���X����͂��Ă��������B</strong></font><br>
<div class="form" align="center">
<form action="<%= response.encodeURL("passwdreminder.jsp")%>" method="post" name="userform" id="userform"> 
�l���[�U�[��<br>
<input name="loginid" id="text" type="text" size="20" maxlength="20" istyle="3"><br>
���[���A�h���X<br>
<input name="mailaddr_mobile" id="text2" type="text" size="20" maxlength="60" istyle="3"><br>
<input name="image" type="submit" value="�p�X���[�h���s" id="button2">
<hr class="border">
<div align="left">
���u�l���[�U�[���v���킩��Ȃ��ꍇ�A�u���[���A�h���X�v�̓o�^���Ȃ��ꍇ�́A�Ǘ��҂܂ł��₢���킹���������B<br>
���u���[���A�h���X�v�ɉ��p�X���[�h�����M����܂��̂ŁA���O�C����u�l�p�X���[�h�v��ύX���Ă��������B<br>
</div>
<hr class="border">
<%
    }
%> 
</form>
</div>
</ul>
<div align="center" class="copyright">
Copyright(C) almex inc. All rights reserved.
</div>

</body>
</html>
