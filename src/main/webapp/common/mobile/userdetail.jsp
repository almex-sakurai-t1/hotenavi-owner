<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>

<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    int    user_id      = ownerinfo.DbUserId;
    String username     = ownerinfo.DbUserName;


    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    boolean           AdminFlag   = false;

    try
    {
        final String query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid = ? "
            + " AND owner_user_security.hotelid = owner_user.hotelid"
            + " AND owner_user_security.userid  = owner_user.userid"
            + " AND owner_user.userid = ? "
            + " AND owner_user_security.admin_flag = 1";

        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, user_id);

        result      = prestate.executeQuery();
        if( result.next())
        {
            AdminFlag = true;
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

    String param_user_id = ReplaceString.getParameter(request,"UserId");
    if    (param_user_id != null)
    {
        if(CheckString.numCheck(param_user_id))
        {
            user_id = Integer.parseInt(param_user_id);
        }
    }

    int     admin_flag          = 0;
    int     unknown_flag_pc     = 0;
    int     unknown_flag_mobile = 0;
    String  mailaddr_pc         = "";
    String  mailaddr_mobile     = "";
    int     sec_level01         = 0;
    int     sec_level02         = 0;
    int     sec_level03         = 0;
    int     sec_level04         = 0;
    int     sec_level05         = 0;
    int     sec_level06         = 0;
    int     sec_level07         = 0;
    int     sec_level15         = 0;
    int     sec_level16         = 0;

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>հ�ް�Ǘ�</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<div align="center">
հ�ް�Ǘ�
</div>
<hr>
<%
    if (AdminFlag)
    {
        try
        {
            final String query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid = ? "
                + " AND owner_user_security.hotelid = owner_user.hotelid"
                + " AND owner_user_security.userid  = owner_user.userid"
                + " AND owner_user.userid = ? "
                + " AND owner_user.imedia_user = 0";

            prestate    = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, user_id);

            result      = prestate.executeQuery();
            if( result.next())
            {
                username            = new String(result.getString("owner_user.name").getBytes("Shift_JIS"), "Windows-31J");
                admin_flag          = result.getInt("owner_user_security.admin_flag");
                unknown_flag_pc     = result.getInt("owner_user.unknown_flag_pc");
                unknown_flag_mobile = result.getInt("owner_user.unknown_flag_mobile");
                mailaddr_pc         = result.getString("owner_user.mailaddr_pc");
                mailaddr_mobile     = result.getString("owner_user.mailaddr_mobile");
                sec_level01         = result.getInt("owner_user_security.sec_level01");
                sec_level02         = result.getInt("owner_user_security.sec_level02");
                sec_level03         = result.getInt("owner_user_security.sec_level03");
                sec_level04         = result.getInt("owner_user_security.sec_level04");
                sec_level05         = result.getInt("owner_user_security.sec_level05");
                sec_level06         = result.getInt("owner_user_security.sec_level06");
                sec_level07         = result.getInt("owner_user_security.sec_level07");
                sec_level15         = result.getInt("owner_user_security.sec_level15");
                sec_level16         = result.getInt("owner_user_security.sec_level16");

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
%>
<%= username %>�l
<br>
<%
        try
        {
            final String query = "SELECT * FROM owner_user_login WHERE hotelid = ?  AND userid = ? ";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, user_id);

            result      = prestate.executeQuery();
            if( result != null )
            {
                if( result.next())
                {
%>
[�ŏI۸޲�]<br>
<%= result.getDate("login_date") %>
<%= result.getTime("login_time") %>
<a href="<%= response.encodeURL("loginhistory.jsp?UserId=" + user_id) %>">����</a><br>
<%
                }
                else
                {
%>
[۸޲ݗ����Ȃ�]<br>
<%
                }
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
%>
[Ұٱ��ڽ]<br>
<% if (mailaddr_pc.equals("") && mailaddr_mobile.equals("")){%>���o�^<br><%}%>
<% if (!mailaddr_pc.equals(""))    {%><%=mailaddr_pc%><% if(unknown_flag_pc ==1)       {%>�@<font color=red>PCҰٱ��ڽ�����m�F�肢�܂�</font><%}%><br><%}%>
<% if (!mailaddr_mobile.equals("")){%><%=mailaddr_mobile%><% if(unknown_flag_mobile ==1){%>�@<font color=red>�g��Ұٱ��ڽ�����m�F�肢�܂�</font><%}%><br><%}%>
[����]<br>
<font size=1>
<% if  (admin_flag  == 1) {%><strong>�Ǘ���</strong><%}%>
<% if  (sec_level01 == 1) {%>����Ǘ�<%}%>
<% if  (sec_level02 == 1) {%>�������<%}%>
<% if  (sec_level03 == 1) {%>���[�Ǘ�<%}%>
<% if  (sec_level04 == 1) {%>�g�o���|�[�g<%}%>
<% if  (sec_level05 == 1) {%>�����}�K�쐬<%}%>
<% if  (sec_level06 == 1) {%>�g�o�ҏW<%}%>
<% if  (sec_level07 == 1) {%>�ݒ胁�j���[<%}%>
<% if  (sec_level15 == 1) {%>�n�s�z�e�ҏW<%}%>
<% if  (sec_level16 == 1) {%>�N�`�R�~����<%}%><br>
</font>
[�Ǘ��X��]<br>
<%
        try
        {
            final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                + " AND owner_user_hotel.accept_hotelid = hotel.hotel_id"
                + " AND owner_user_hotel.userid = ? ";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, user_id);

            result      = prestate.executeQuery();
            if( result != null )
            {
                while( result.next())
                {
%>
<%= new String(result.getString("hotel.name").getBytes("Shift_JIS"), "Windows-31J") %><br>
<%
                }
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
    }
    DBConnection.releaseResources(connection);
%>
<jsp:include page="footer.jsp" flush="true" />
