<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.kitchen.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
%>
<%@ include file="checkHotelId.jsp" %>
<%
    String loginHotelId =  (String)session.getAttribute("HotelId");
    String hotelid;

    boolean    HotelIdCheck = false;
    boolean    KitchenExist = false;
    KitchenInfo kitcheninfo = new KitchenInfo();

    // ホテルIDのセット
    hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    } 
    if( hotelid == null )
    {
        hotelid = "";
        HotelIdCheck = false;
    }

    int    storecount  = 1;
    String param_count = request.getParameter("count");
    if(param_count != null)
    {
        if ( !CheckString.numCheck( param_count ) )
        {
            response.sendError(400);
            return;
        }
        storecount  = Integer.parseInt(param_count);
    }

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    // ホテル名,プラン取得
    String  hotelname = "";
    int     plan = 0;
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT name,plan FROM hotel WHERE hotel_id= ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if(result.next())
        {
            hotelname = result.getString("name");
            plan      = result.getInt("plan");
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

    int          userlevel;
    String       username;

    // 代表ホテルにログイン（計上日取得）
    ownerinfo.sendPacket0100(1,hotelid);
    // ユーザレベルの取得
    userlevel = ownerinfo.DbUserLevel;
    // 名前の取得
    username = ownerinfo.DbUserName;
    // 最終ログイン日時取得
    String lastlogin = "";
    try
    {
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
        lastlogin = "なし";
    }

    //売上管理・部屋情報管理店舗件数
    int count = 0;
    try
    {
        final String query = "SELECT  count(*) FROM hotel,owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
            + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid"
            + " AND owner_user_hotel.userid = ? "
            + " AND hotel.plan <= 2"
            + " AND hotel.plan >= 1"
            + " AND owner_user_hotel.hotelid = owner_user_security.hotelid"
            + " AND owner_user_hotel.userid = owner_user_security.userid"
            + " AND (owner_user_security.admin_flag = 1 OR owner_user_security.sec_level01 = 1 OR owner_user_security.sec_level02 = 1)";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if(result.next())
        {
            count = result.getInt(1);
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


    int               imedia_user = 0;

    // imedia_user のチェック
    try
    {
        final String query = "SELECT * FROM owner_user WHERE hotelid = ? "
            + " AND userid = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user      = result.getInt("imedia_user");
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

    //キッチン端末有無判断
    KitchenExist = kitcheninfo.sendPacket0000(1,hotelid);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>ｵｰﾅｰｻｲﾄﾒﾆｭｰ</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />
<%
    if (!hotelname.equals(""))
    {
%>
<div align="center">
<%= hotelname %>
</div>
<hr>
<%
    }
%>
ようこそ<br>
<%= username %>様<br>
前回ﾛｸﾞｲﾝ<br>
<%= lastlogin %><br>
<%-- 個別メッセージ --%>
<jsp:include page="info_message.jsp" flush="true" />
<%
    try
    {
        final String query = "SELECT * FROM owner_user_security,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
            + " AND owner_user_hotel.userid = ? "
            + " AND owner_user_hotel.accept_hotelid = ? "
            + " AND owner_user_hotel.hotelid = owner_user_security.hotelid"
            + " AND owner_user_hotel.userid = owner_user_security.userid";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        prestate.setString(3, hotelid);
        result      = prestate.executeQuery();
        if(result.next())
        {
            if( result.getInt("sec_level01") == 1 && (plan == 1 || plan == 2) )
            {
                HotelIdCheck = true;
%>
<a href="<%= response.encodeURL("salesmenu.jsp?HotelId=" + hotelid) %>" accesskey="1">売上情報</a><br>
<%
            }
            if( result.getInt("sec_level02") == 1 && (plan == 1 || plan == 2))
            {
                HotelIdCheck = true;
%>
<a href="<%= response.encodeURL("roominfo.jsp?HotelId=" + hotelid) %>" accesskey="2">現在部屋情報</a><br>
<%
                if(KitchenExist && imedia_user == 1 || hotelid.equals("tropicana"))
                {
%>
<a href="<%= response.encodeURL("kitcheninfo.jsp?HotelId=" + hotelid) %>" accesskey="2">ｷｯﾁﾝ端末情報</a><br>
<%
                }
            }
            if( result.getInt("sec_level04") == 1 && (plan == 1 || plan == 3 || plan == 4) )
            {
                HotelIdCheck = true;
%>
<a href="<%= response.encodeURL("access.jsp?HotelId=" + hotelid) %>" accesskey="4">ｻｲﾄｱｸｾｽ情報</a><br>
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
        DBConnection.releaseResources(result,prestate,connection);
    }
    if(!HotelIdCheck)
    {
        if(storecount == 0)
        {
%>
<font color=red>管理店舗が登録されていません。</font><br>
管理者にお問合せください。<br>
<%
        }
        else
        {
%>
<font color=red>管理店舗ではありません。</font><br>
<%
        }
    }
    if( count > 1 )
    {
%>
<a href="<%= response.encodeURL("allstoremenu.jsp?HotelId=" + hotelid) %>" accesskey="3">管理店舗情報</a><br>
<%
    }
%>
<br>
<jsp:include page="infoNew.jsp" flush="true" />
<jsp:include page="footer.jsp" flush="true" />
</body>
</html>
