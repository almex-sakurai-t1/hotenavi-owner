<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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
    String loginHotelId =  (String)session.getAttribute("HotelId");

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    int          userlevel;
    String       username;

    // ���[�U���x���̎擾
    userlevel = ownerinfo.DbUserLevel;
    // ���O�̎擾
    username = ownerinfo.DbUserName;

    // �ŏI���O�C�������擾
    String lastlogin = "";
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT * FROM owner_user_login WHERE hotelid = ? "
            + " AND userid = ? "
            + " AND kind = 1";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result      = prestate.executeQuery();
        if( result.next() )
        {
            lastlogin = result.getDate("last_login_date") +
                        "&nbsp;" +
                        result.getTime("last_login_time");
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
    if (lastlogin.equals(""))
    {
        try
        {
            final String query = "SELECT * FROM owner_user_log WHERE hotelid = ? "
                + " AND userid = ? "
                + " ORDER BY logid DESC LIMIT 1,1";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                lastlogin = result.getDate("login_date") +
                        "&nbsp;" +
                        result.getTime("login_time");
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
    if (lastlogin.equals(""))
    {
        lastlogin = "�Ȃ�";
    }

    DBConnection.releaseResources(connection);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�X�ܑI���ƭ�</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
�悤����<br>
<%= username %>�l<br>
�O��۸޲�<br>
<%= lastlogin %><br>
<br>
<%-- �ʃ��b�Z�[�W --%>
<jsp:include page="info_message.jsp" flush="true" />
<%-- �X�ܑI�� --%>
<jsp:include page="storeselectdispdata.jsp" flush="true" />
<br>
<%-- Information --%>
<jsp:include page="infoNew.jsp" flush="true" />
<hr>
<div align="center">
<a href="<%= response.encodeURL("index.jsp") %>">TOP��</a><br>
</div>
<hr>
<div align="center">
Copyright(C) almex inc. All rights reserved.
</div>
</body>
</html>
