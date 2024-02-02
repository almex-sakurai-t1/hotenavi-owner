<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String requestUri = request.getRequestURI();
    String hotelid    = requestUri.replace("_debug_","");
    hotelid           = hotelid.replace("i/passwdreminder.jsp","");
    hotelid           = hotelid.replace("ez/passwdreminder.jsp","");
    hotelid           = hotelid.replace("j/passwdreminder.jsp","");
    hotelid           = hotelid.replace("/","");
    String loginid              = ReplaceString.getParameter(request,"loginid");
    if    (loginid == null)     loginid = "";
    String mailaddr_mobile      = ReplaceString.getParameter(request,"mailaddr_mobile");
    if    (mailaddr_mobile     == null) mailaddr_mobile = "";

    boolean RemindMode = false;
    if (!loginid.equals("") && !mailaddr_mobile.equals(""))
    {
        RemindMode = true;
    }

    Calendar cal = Calendar.getInstance();
    int now_year  = cal.get(cal.YEAR);
    int now_month = cal.get(cal.MONTH) + 1;
    int now_day   = cal.get(cal.DATE);
    int nowdate   = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime   = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    int     userid    = 0;

    if (RemindMode)
    {
        String  name       = "";
        String  group_name = "";
        String  from_mail = "";
        String  passwd_mobile = "";
        Connection        connection  = null;
        PreparedStatement prestate    = null;
        ResultSet         result      = null;
        connection        = DBConnection.getConnection();
        try
        {
            final String query = "SELECT * FROM hotel WHERE hotel_id = ? ";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                from_mail      = result.getString("mail");
                if (from_mail.equals("")) from_mail = "imedia-info@hotenavi.com";
                group_name     = result.getString("name");
            }
        }
        catch( Exception e )
        {
            Logging.error(e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }

        try
        {
            final String query = "SELECT * FROM owner_user WHERE hotelid = ? "
                + " AND loginid = ? "
                + " AND mailaddr_mobile = ? ";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setString(2, ReplaceString.HTMLEscape(loginid));
            prestate.setString(3, ReplaceString.HTMLEscape(mailaddr_mobile));

            result      = prestate.executeQuery();
            if( result.next() )
            {
                userid       = result.getInt("userid");
                name         = result.getString("name");
            }
        }
        catch( Exception e )
        {
            Logging.error(e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result,prestate,connection);
        }

        if (userid != 0)
        {
            passwd_mobile = RandomString.getRandomNumber(4);

            try
            {
                final String query = "UPDATE owner_user SET passwd_mobile_update=0,passwd_mobile = ? WHERE hotelid = ?  AND userid = ? ";
                connection  = DBConnection.getConnection();
                prestate    = connection.prepareStatement(query);
                prestate.setString(1, passwd_mobile);
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
            text = text +  "�y�g�їp�p�X���[�h�z" + "\r\n";
            text = text +  passwd_mobile + "\r\n";
            text = text +  "\r\n";
            text = text +  "------------------\r\n";
            text = text +  "�I�[�i�[�T�C�g�Ƀ��O�C�����A�p�X���[�h��ύX���Ă��������B" + "\r\n";
            text = text +  "------------------\r\n";
            SendMail sendmail = new SendMail();
            sendmail.send(from_mail, mailaddr_mobile, title_mail, text);
            sendmail.send(from_mail, "sakurai-t1@almex.jp", title_mail, text);

            String UserAgent = request.getHeader("user-agent");
            if (UserAgent.length() > 255)
            {
                UserAgent = UserAgent.substring(0,255);
            }
            try
            {
                final String query = "INSERT INTO hh_owner_user_log SET "
                    + "hotel_id = ? , "
                    + "user_id = ? , "
                    + "login_date = ? , "
                    + "login_time = ? , "
                    + "login_name = ? , "
                    + "passwd = ? , "
                    + "log_level=230,"
                    + "log_detail='�g�сi���j�p�X���[�h���s',"
                    + "user_agent = ? , "
                    + "remote_ip= ?  ";

                connection = DBConnection.getConnection();
                prestate    = connection.prepareStatement(query);
                prestate.setString(1, ReplaceString.SQLEscape(hotelid));
                prestate.setInt(2, userid);
                prestate.setInt(3, nowdate);
                prestate.setInt(4, nowtime);
                prestate.setString(5, ReplaceString.SQLEscape(loginid));
                prestate.setString(6, passwd_mobile);
                prestate.setString(7, UserAgent);
                prestate.setString(8, ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() ) );

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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�߽ܰ���ϲ��ް</title>
</head>
<body>

<jsp:include page="header.jsp" flush="true" />
���߽ܰ�ލĔ��s<br>
	  </tr>
<%
    if (RemindMode)
    {
        if (userid != 0)
        {
%>
<font color=red><strong>���߽ܰ�ނ����s����܂����</strong></font><br>
Ұق����M����Ă��܂��̂Ť��Ű��Ă�۸޲݂���߽ܰ�ނ�ύX���Ă��������<br>
<%
        }
        else
        {
%>
<font color=red><strong>���͂����lհ�ް���������͌g��Ұٱ��ڽ���Ⴂ�܂��</strong></font><br>
<br>
<a href="passwdreminder.jsp">�ē��͂���</a>
<%
        }
    }
    else
    {
%>
<font color=red><strong>�߽ܰ�ނ��Ĕ��s���܂��̂Ť�lհ�ް���ƌg��Ұٱ��ڽ����͂��Ă��������</strong></font><br>
<form action="<%= response.encodeURL("passwdreminder.jsp")%>" method="post" name="userform" id="userform">
[�lհ�ް��]<br>
<input name="loginid" type="text" size="10" maxlength="10" istyle="3"><br>
[�g��Ұٱ��ڽ]<br>
<input name="mailaddr_mobile" type="text" size="20" maxlength="60" istyle="3"><br>
<input type="submit" value="�߽ܰ�ލĔ��s">
<hr>
����lհ�ް������킩��Ȃ��ꍇ���g��Ұٱ��ڽ��̓o�^���Ȃ��ꍇ�ͤ�Ǘ��҂܂ł��₢���킹���������<br>
����g��Ұٱ��ڽ��ɉ��߽ܰ�ނ����M����܂��̂Ť۸޲݌��߽ܰ�ނ�ύX���Ă��������<br>
<%
    }
%>
<hr>
<div align="center">
Copyright(C) almex inc. All rights reserved.
</div>

</body>
</html>

