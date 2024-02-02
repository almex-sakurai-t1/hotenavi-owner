<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.kitchen.*" %>
<%@ page errorPage="error.jsp" %>
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
<%@ include file="checkHotelId.jsp" %>
<%
    String loginHotelId = (String)session.getAttribute("HotelId");
    String hotelid;

    boolean    HotelIdCheck = false;
    boolean    KitchenExist = false;
    KitchenInfo kitcheninfo = new KitchenInfo();

    // �z�e��ID�̃Z�b�g
    hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }

    int    storecount  = 1;
    String param_count = ReplaceString.getParameter(request,"count");
    if (param_count != null)
    {
        storecount = Integer.parseInt(param_count);
    }

    Connection        connection = null;
    PreparedStatement prestate   = null;
    ResultSet         result     = null;

    // �z�e����,�v�����擾
    String hotelname = "";
    int plan = 0;
    try
    {
        final String query = "SELECT name,plan FROM hotel WHERE hotel_id = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);

        result = prestate.executeQuery();
        if(result.next())
        {
            hotelname = result.getString("name");
            plan      = result.getInt("plan");
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

    // imedia_user �̃`�F�b�N
    int imedia_user = 0;
    try
    {
        final String query = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if(result.next())
        {
            imedia_user = result.getInt("imedia_user");
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


    int    userlevel;
    String username;

    // ��\�z�e���Ƀ��O�C���i�v����擾�j
    ownerinfo.sendPacket0100(1, hotelid);
    // ���[�U���x���̎擾
    userlevel = ownerinfo.DbUserLevel;
    // ���O�̎擾
    username = ownerinfo.DbUserName;
    // �ŏI���O�C�������擾
    String lastlogin = "";
    try
    {
        final String query = "SELECT * FROM owner_user_login WHERE hotelid = ? AND userid = ? AND kind = 1";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if(result.next())
        {
            lastlogin = result.getDate("last_login_date") +
                        "&nbsp;" +
                        result.getTime("last_login_time");
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
    if (lastlogin.equals(""))
    {
        try
        {
            final String query = "SELECT * FROM owner_user_log WHERE hotelid = ? "
                               + "AND userid = ? "
                               + "ORDER BY logid DESC "
                               + "LIMIT 1,1";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, loginHotelId);
            prestate.setInt(2, ownerinfo.DbUserId);

            result = prestate.executeQuery();
            if (result.next())
            {
                lastlogin = result.getDate("login_date") +
                            "&nbsp;" +
                            result.getTime("login_time");
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
    }
    if (lastlogin.equals(""))
    {
        lastlogin = "�Ȃ�";
    }

    //����Ǘ��E�������Ǘ��X�܌���
    int count = 0;
    try
    {
        final String query = "SELECT count(*) FROM hotel,owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                           + "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                           + "AND owner_user_hotel.userid = ? "
                           + "AND hotel.plan <= 2 "
                           + "AND hotel.plan >= 1 "
                           + "AND owner_user_hotel.hotelid = owner_user_security.hotelid "
                           + "AND owner_user_hotel.userid = owner_user_security.userid "
                           + "AND (owner_user_security.admin_flag = 1 OR owner_user_security.sec_level01 = 1 OR owner_user_security.sec_level02 = 1)";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
        if(result.next())
        {
            count = result.getInt(1);
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

    //�L�b�`���[���L�����f
    KitchenExist = kitcheninfo.sendPacket0000(1, hotelid);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=320px,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>�I�[�i�[�T�C�g���j���[</title>
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
<%
    if (!hotelname.equals(""))
    {
%>
<div align="center">
<%= hotelname %>
</div>

<hr class="border">

<%
    }
%>

<center>
<h2>�悤����<br><%= username %>�l</h2>
<font size="1">�O�񃍃O�C��:<%= lastlogin %></font>
</center>



<jsp:include page="info_message.jsp" flush="true" />

<hr class="border">

<ul class="link">
<%
    try
    {
        final String query = "SELECT * FROM owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                           + "AND owner_user_hotel.userid = ? "
                           + "AND owner_user_hotel.accept_hotelid = ? "
                           + "AND owner_user_hotel.hotelid = owner_user_security.hotelid "
                           + "AND owner_user_hotel.userid = owner_user_security.userid";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        prestate.setString(3, hotelid);

        result = prestate.executeQuery();
        if(result.next())
        {
            if( (result.getInt("sec_level01") == 1 || result.getInt("admin_flag") == 1 ) && (plan == 1 || plan == 2) )
            {
                HotelIdCheck = true;
%>
<%-- �ʃ��b�Z�[�W --%>

<li><a href="<%= response.encodeURL("salesmenu.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>">������</a></li>
<%
            }
            if( (result.getInt("sec_level02") == 1 || result.getInt("admin_flag") == 1 ) && (plan == 1 || plan == 2))
            {
                HotelIdCheck = true;
%>
<li><a href="<%= response.encodeURL("roominfo.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>">���ݕ������</a></li>
<%
                if(KitchenExist && imedia_user == 1 || hotelid.equals("tropicana"))
                {
%>
<li><a href="<%= response.encodeURL("kitcheninfo.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>">�L�b�`���[�����</a></li>
<%
                }
            }
            if( (result.getInt("sec_level04") == 1 || result.getInt("admin_flag") == 1 ) && (plan == 1 || plan == 3 || plan == 4) )
            {
                HotelIdCheck = true;
%>
<li><a href="<%= response.encodeURL("access.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>">�T�C�g�A�N�Z�X���</a></li>
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
        DBConnection.releaseResources(result, prestate, connection);
    }
    if(!HotelIdCheck)
    {
        if(storecount == 0)
        {
%>
<font color=red>�Ǘ��X�܂��o�^����Ă��܂���B</font><br>
�Ǘ��҂ɂ��⍇�����������B<br>
<%
        }
        else
        {
%>
<font color=red>�Ǘ��X�܂ł͂���܂���B</font><br>
<%
        }
    }
    if( count > 1 )
    {
%>
<li><a href="<%= response.encodeURL("allstoremenu.jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>">�Ǘ��X�܏��</a></li>
<%
    }
%>
</ul>

<jsp:include page="infoNew.jsp" flush="true" />
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
