<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.owner.OwnerLoginInfo" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="owner_ini.jsp" %>
<%@ include file="CheckCookie.jsp" %>

<%
    String HotelIdbyURL = paramRequestURI;
    HotelIdbyURL = HotelIdbyURL.replace("/smart/login.jsp", "");
    HotelIdbyURL = HotelIdbyURL.replace("/_debug_/", "");
    HotelIdbyURL = HotelIdbyURL.replace("/", "");
    if (HotelIdbyURL.indexOf(";jsessionid") != -1)
    {
        HotelIdbyURL = HotelIdbyURL.substring(0,HotelIdbyURL.indexOf(";jsessionid"));
    }

    String refererURL;
    // タイムアウトからリンクされたとき
    refererURL = ReplaceString.getParameter(request,"refererURL");
%>
<%= HotelIdbyURL %>

<%-- ログイン確認 --%>
<%
    int     count;
    boolean ret;
    String  hotelid;
    String  loginid;
    String  password;
    String  LidSave;
    String  Nid;
    String  NPasswd;
    String  NidSave;
    String  manage_hotelid = "";

    hotelid = HotelIdbyURL;

    Cookie owncookie;

    loginid = ReplaceString.getParameter(request,"LoginId");

try
{
if (!CheckString.numAlphaCheck(loginid))
{
  response.sendRedirect(response.encodeURL("login.jsp"));
}
else
{
    password = ReplaceString.getParameter(request,"Password");
    Nid = ReplaceString.getParameter(request,"nid");
    if (Nid == null) Nid = "";
    if (loginNidSave.equals("1"))
    {
        if (Nid.equals(""))
        {
            Nid = loginNid;
        }
    }
    NPasswd = ReplaceString.getParameter(request,"npasswd");
    if (NPasswd == null) NPasswd = "";
    NidSave = ReplaceString.getParameter(request,"nidsave");
    if (NidSave == null) NidSave = "";
    if (loginNidSave.equals("1"))
    {
        if (NidSave.equals(""))
        {
            NidSave = loginNidSave;
        }
    }
    LidSave = ReplaceString.getParameter(request,"lidsave");
    if (LidSave == null) LidSave = "";
    if (loginLidSave.equals("1"))
    {
        if (LidSave.equals(""))
        {
            LidSave = loginLidSave;
        }
    }

    // アカウントロック状態の確認
    OwnerLoginInfo ownerLoginInfo = new OwnerLoginInfo();
    if( !ownerLoginInfo.userLoginHistoryCheck(hotelid, loginid) )
    {
        //session破棄
        request.getSession().invalidate();
        // アカウントロック中
        response.sendRedirect("../../common/smart/loginerr.jsp?result=4");
        return;
    }

    if (!hotelid.equals("demo"))
    {
        // クッキーセット
        owncookie = new Cookie( "ownhid", hotelid );
        owncookie.setPath( "/" );
        owncookie.setDomain( ".hotenavi.com" );
        owncookie.setMaxAge( Integer.MAX_VALUE );
        response.addCookie( owncookie );

        if (NidSave.equals("1"))
        {
            owncookie = new Cookie( "ownnid", Nid );
            owncookie.setPath( "/" );
            owncookie.setDomain( ".hotenavi.com" );
            owncookie.setMaxAge( Integer.MAX_VALUE );
            response.addCookie( owncookie );
        }
        else
        {
            owncookie = new Cookie("ownnid", "");
            owncookie.setPath( "/" );
            owncookie.setDomain( ".hotenavi.com" );
            owncookie.setMaxAge( 1 );
            response.addCookie( owncookie );
        }
        if (LidSave.equals("1"))
        {
            owncookie = new Cookie( "ownlid", loginid );
            owncookie.setPath( "/" );
            owncookie.setDomain( ".hotenavi.com" );
            owncookie.setMaxAge( Integer.MAX_VALUE );
            response.addCookie( owncookie );
        }
        else
        {
            owncookie = new Cookie("ownlid", "");
            owncookie.setPath( "/" );
            owncookie.setDomain( ".hotenavi.com" );
            owncookie.setMaxAge( 1 );
            response.addCookie( owncookie );
        }
        owncookie = new Cookie( "ownnidsave", NidSave );
        owncookie.setPath( "/" );
        owncookie.setDomain( ".hotenavi.com" );
        owncookie.setMaxAge( Integer.MAX_VALUE );
        response.addCookie( owncookie );
        owncookie = new Cookie( "ownlidsave", LidSave );
        owncookie.setPath( "/" );
        owncookie.setDomain( ".hotenavi.com" );
        owncookie.setMaxAge( Integer.MAX_VALUE );
        response.addCookie( owncookie );
    }

    int passwd_pc_update = 0;
    int result           = 0;

    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet resultset = null;
    try
    {
        String query =  "SELECT * FROM hotel WHERE hotel_id = ? "
                     +  "AND hotel.owner_user = ? ";
        if (!NidSave.equals("1"))
            query    += "AND hotel.owner_password = ?";

        connection = DBConnection.getConnection();
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setString(2, Nid);
        if (!NidSave.equals("1"))
            prestate.setString(3, NPasswd);

        resultset = prestate.executeQuery();
        if( resultset.next())
        {
            result = 1;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(resultset, prestate, connection);
    }

    if (result == 1)
    {
        result = 0;
        // ログイン確認
        result = ownerinfo.getUserDbInfo(hotelid, loginid, password, request.getHeader("user-agent"), ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() ), 1);
    }
    if( result == 1 )
    {
        // --------------------------------------------
        // ログイン成功時はこっち
        // --------------------------------------------
        boolean  LoginExist = false;
        DateEdit dateedit = new DateEdit();
        try
        {
            connection = DBConnection.getConnection();

            try
            {
                final String query = "SELECT * FROM owner_user_login WHERE hotelid = ? "
                                   + "AND userid = ? "
                                   + "AND kind = 1";

                prestate = connection.prepareStatement(query);
                prestate.setString(1, hotelid);
                prestate.setInt(2, ownerinfo.DbUserId);

                resultset = prestate.executeQuery();
                if(resultset.next())
                {
                    LoginExist = true;
                }
            }
            catch( Exception e )
            {
                Logging.error("foward Exception e=" + e.toString(), e);
            }
            finally
            {
                DBConnection.releaseResources(resultset);
                DBConnection.releaseResources(prestate);
            }

            try
            {
                final String query;
                if (LoginExist)
                {
                    query = "UPDATE owner_user_login SET "
                          + "last_login_date = login_date, "
                          + "last_login_time = login_time, "
                          + "last_user_agent = user_agent, "
                          + "last_remote_ip  = remote_ip, "
                          + "login_date = ?, "
                          + "login_time = ?, "
                          + "user_agent = ?, "
                          + "remote_ip  = ? "
                          + "WHERE hotelid = ? "
                          + "AND userid = ? "
                          + "AND kind = 1";
                }
                else
                {
                    query = "INSERT INTO owner_user_login SET "
                          + "login_date = ?, "
                          + "login_time = ?, "
                          + "user_agent = ?, "
                          + "remote_ip  = ?, "
                          + "hotelid = ?, "
                          + "userid = ?, "
                          + "kind = 1";
                }
                prestate = connection.prepareStatement(query);
                prestate.setString(1, dateedit.getDate(0));
                prestate.setString(2, dateedit.getTime(0));
                prestate.setString(3, request.getHeader("user-agent") != null ? request.getHeader("user-agent") : "");
                prestate.setString(4, request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split(",")[0] : request.getRemoteAddr());
                prestate.setString(5, hotelid);
                prestate.setInt(6, ownerinfo.DbUserId);

                prestate.executeUpdate();
            }
            catch( Exception e )
            {
                Logging.error("foward Exception e=" + e.toString(), e);
            }
            finally
            {
                DBConnection.releaseResources(resultset);
                DBConnection.releaseResources(prestate);
            }
        }
        finally
        {
            DBConnection.releaseResources(connection);
        }

        // ログインした時に古いセッションIDを破棄
        request.getSession(true).invalidate();
        // 新しいセッションを作成
        session = request.getSession();

        // パラメータのセット
        ownerinfo.HotelId = hotelid;
        ownerinfo.DbLoginUser = loginid;
        ownerinfo.DbPassword  = password;
        session.setAttribute("ownerinfo", ownerinfo );

        // セッション属性にセット
        session.setAttribute("HotelId", hotelid);
        session.setAttribute("LoginHotelId", hotelid);
        session.setAttribute("LoginId", loginid);
        session.setAttribute("Password", password);

        manage_hotelid = hotelid;

        // 管理店舗数をカウントする
        count = 0;
        DbAccess db_manage = new DbAccess();

        // 管理店舗数分ループ
        ResultSet DbManageHotel = ownerinfo.getManageHotel(db_manage, hotelid, ownerinfo.DbUserId);
        if( DbManageHotel != null )
        {
            ret = DbManageHotel.first();
            while( ret != false )
            {
                manage_hotelid = DbManageHotel.getString("accept_hotelid");

                count++;
                ret = DbManageHotel.next();
            }
        }
        else
        {
            manage_hotelid = hotelid;
        }

        db_manage.close();

        // セッション属性に管理店舗数を入れておく
        session.setAttribute("StoreCount", Integer.toString(count) );

        // --------------------------------------------
        // 通常ログイン
        // --------------------------------------------
        try
        {
            final String query = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, ownerinfo.DbUserId);

            resultset = prestate.executeQuery();
            if( resultset.next() != false )
            {
                passwd_pc_update = resultset.getInt("passwd_pc_update");
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(resultset, prestate, connection);
        }

        if(passwd_pc_update == 0)
        {
            response.sendRedirect(response.encodeURL("passwdchange.jsp"));
        }
        else if(refererURL != null)
        {
            response.sendRedirect(response.encodeURL(refererURL));
        }
        else if(count > 1)
        {
            response.sendRedirect(response.encodeURL("storeselect.jsp"));
        }
        else
        {
            response.sendRedirect(response.encodeURL("ownerindex.jsp?HotelId="+manage_hotelid+"&count="+count));
        }
    }
    else
    {
        // session破棄
        request.getSession().invalidate();

        // --------------------------------------------
        // ログイン失敗時はこっち(loginerr.jspへ)
        // --------------------------------------------
%>
        <jsp:forward page="loginerr.jsp" >
          <jsp:param name="result"   value="<%= result %>" />
          <jsp:param name="hotelid"  value="<%= hotelid %>" />
          <jsp:param name="nid"      value="<%= Nid %>" />
          <jsp:param name="npasswd"  value="<%= NPasswd %>" />
          <jsp:param name="LoginId"  value="<%= loginid %>" />
          <jsp:param name="Password" value="<%= password %>" />
        </jsp:forward>
<%
    }
}
}
catch( Exception e )
{
    Logging.error("foward Exception e=" + e.toString(), e);
%>
        <jsp:forward page="timeout.jsp" />
<%
 }
%>
