<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- ログイン確認 --%>
<%
    String requestUri = request.getRequestURI();

    int       result;
    int       count;
    boolean   ret;
    boolean   DebugMode = false;
    if  (requestUri.indexOf("_debug_") != -1)
    {
       DebugMode = true;
    }
    String    hotelid;
    String    loginid;
    String    password;
    String    manage_hotelid = "";
    UserAgent    agent;
    String    refererURL = "";

    hotelid  = ReplaceString.getParameter(request,"HotelId");
    password = ReplaceString.getParameter(request,"Password");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    } 

    if (hotelid == null)
    {
        hotelid="";
    }
    if (password== null)
    {
        password="";
    }

    // 端末番号取得
    agent = new UserAgent();
    loginid  = agent.getSerialNo(request);

    if(!DebugMode && hotelid.equals("demo"))
    {
        loginid = "000000000000000";
    }
    else if(DebugMode && password.equals("1111") && hotelid.equals("demo"))
    {
        loginid = "000000000000000";
    }

    //ﾀｲﾑｱｳﾄからﾘﾝｸされたとき

    if (request.getParameter("refererURL") != null)
    {
        refererURL = ReplaceString.getParameter(request,"refererURL");
    }

    // パラメータのセット
    ownerinfo.HotelId = hotelid;
    ownerinfo.DbLoginUser = loginid;
    ownerinfo.DbPassword  = password;

    // セッション属性にもセット
    session.setAttribute("HotelId", hotelid);
    session.setAttribute("LoginId", loginid);
    session.setAttribute("Password", password);

    String            query       = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         resultset   = null;

    // ログイン確認
    result = ownerinfo.getUserDbInfo(hotelid, loginid, password, request.getHeader("user-agent"), ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() ), 2);
    if( result == 1 )
    {
// --------------------------------------------
// ログイン成功時はこっち
// --------------------------------------------
        boolean  LoginExist = false;
        DateEdit dateedit = new DateEdit();
        try
        {
            connection        = DBConnection.getConnection();
            query = "SELECT * FROM owner_user_login WHERE hotelid= ? ";
            query = query + " AND userid = ? ";
            query = query + " AND kind = 1";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, ownerinfo.DbUserId);

            resultset      = prestate.executeQuery();
            if (resultset != null)
            {
                if(resultset.next())
                {
                    LoginExist = true;
                }
            }
        }
        catch( Exception e )
        {
            Logging.error(e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(resultset);
            DBConnection.releaseResources(prestate);
        }
        if (LoginExist)
        {
            query = "UPDATE owner_user_login SET ";
            query = query + "last_login_date = login_date,";
            query = query + "last_login_time = login_time,";
            query = query + "last_user_agent = user_agent,";
            query = query + "last_remote_ip  = remote_ip,";
            query = query + "login_date = ? , ";
            query = query + "login_time = ? , ";
            query = query + "user_agent = ? , ";
            query = query + "remote_ip  = ? ";
            query = query + " WHERE hotelid = ? ";
            query = query + " AND userid = ? ";
            query = query + " AND kind = 1";
        }
        else
        {
            query = "INSERT INTO owner_user_login SET ";
            query = query + "login_date = ? , ";
            query = query + "login_time = ? , ";
            query = query + "user_agent = ? , ";
            query = query + "remote_ip  = ? , ";
            query = query + "last_login_date = ? , ";
            query = query + "last_login_time = ? , ";
            query = query + "last_user_agent = '',";
            query = query + "last_remote_ip  = '',";
            query = query + "hotelid = ? , ";
            query = query + "userid = ? , ";
            query = query + "kind = 1";
        }
        prestate    = connection.prepareStatement(query);
        if (LoginExist)
        {
            prestate.setString(1, dateedit.getDate(0));
            prestate.setString(2, dateedit.getTime(0));
            prestate.setString(3, ReplaceString.SQLEscape(request.getHeader("user-agent")));
            prestate.setString(4, ReplaceString.SQLEscape(( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() )));
            prestate.setString(5, hotelid);
            prestate.setInt(6, ownerinfo.DbUserId);
        }
        else
        {
            prestate.setString(1, dateedit.getDate(0));
            prestate.setString(2, dateedit.getTime(0));
            prestate.setString(3, ReplaceString.SQLEscape(request.getHeader("user-agent")) );
            prestate.setString(4, ReplaceString.SQLEscape(( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() )) );
            prestate.setString(5, dateedit.getDate(0));
            prestate.setString(6, dateedit.getTime(0));
            prestate.setString(7, hotelid);
            prestate.setInt(8, ownerinfo.DbUserId);
        }

        prestate.executeUpdate();
        DBConnection.releaseResources(resultset,prestate,connection);



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

        int               passwd_mobile_update= 0;
        try
        {
            query = "SELECT * FROM owner_user WHERE hotelid= ? ";
            query = query + " AND userid = ? ";
            connection  = DBConnection.getConnection();
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, ownerinfo.DbUserId);
            resultset   = prestate.executeQuery();
            if( resultset.next() != false )
            {
                passwd_mobile_update = resultset.getInt("passwd_mobile_update");
            }
        }
        catch( Exception e )
        {
            Logging.error(e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(resultset,prestate,connection);
        }

        if(passwd_mobile_update ==0)
        {
            response.sendRedirect(response.encodeURL("passwdchange.jsp"));
        }
        else if(!refererURL.equals(""))
        {
            response.sendRedirect(response.encodeURL(refererURL));
        }
        else if( count > 1 )
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
// --------------------------------------------
// ログイン失敗時はこっち(loginerr.jspへ)
// --------------------------------------------
%>
        <jsp:forward page="loginerr.jsp" >
          <jsp:param name="result" value="<%= result %>" />
          <jsp:param name="loginid" value="<%= loginid %>" />
        </jsp:forward>
<%
    }
%>

