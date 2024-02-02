<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.owner.OwnerLoginInfo" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo"/>
<%@ include file="../../common/pc/owner_ini.jsp" %>
<%
    String HotelIdbyURL =  paramRequestURI;
    HotelIdbyURL = HotelIdbyURL.replace("/pc/ownerlogin.jsp","");
    HotelIdbyURL = HotelIdbyURL.replace("/smartpc/ownerlogin.jsp","");
    HotelIdbyURL = HotelIdbyURL.replace("/_debug_/","");
    HotelIdbyURL = HotelIdbyURL.replace("/" + projectName+"/","");
    HotelIdbyURL = HotelIdbyURL.replace("/","");
//    String loginhotel = (String)session.getAttribute("LoginHotelId");
    if (HotelIdbyURL.indexOf(";jsessionid") != -1)
    {
        HotelIdbyURL = HotelIdbyURL.substring(0,HotelIdbyURL.indexOf(";jsessionid"));
    }


    int       result;
    int       count;
    boolean   ret;
    String    hotelid = "demo";
    String    loginid = "demo";
    String    password = "1111";

    hotelid  = HotelIdbyURL;

//    if(paramRequestURI.indexOf("demo") == -1 || paramRequestURI.indexOf("_debug_") != -1)
//    {
        loginid  = request.getParameter("LoginId");
        password = request.getParameter("Password");
//    }
//    else if(request.getParameter("LoginId").indexOf("demo") != -1)
//    {
//        loginid  = request.getParameter("LoginId");
//        password = request.getParameter("Password");
//    }

    String UserAgent = request.getHeader("user-agent");
    if (UserAgent.length() > 255)
    {
       UserAgent = UserAgent.substring(0,255);
    }

    // �A�J�E���g���b�N��Ԃ̊m�F
    OwnerLoginInfo ownerLoginInfo = new OwnerLoginInfo();
    if( !ownerLoginInfo.userLoginHistoryCheck(hotelid, loginid) )
    {
        //session�j��
        request.getSession().invalidate();
        // �A�J�E���g���b�N��
        DateEdit dateedit = new DateEdit();
        response.sendRedirect("../../common/pc/error/loginUserLock.html?"+dateedit.getTime(1));
        return;
    }

    // ���O�C���m�F
    result = ownerinfo.getUserDbInfo(hotelid, loginid, password, UserAgent, ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr() ), 1);

    // ���O�C���������͂�����
    if( result == 1 )
    {
        Connection        connection  = null;
        PreparedStatement prestate    = null;
        ResultSet         resultset   = null;
        String  query     = "";
        boolean  LoginExist = false;
        DateEdit dateedit = new DateEdit();
        try
        {
            connection        = DBConnection.getConnection();
            query = "SELECT * FROM owner_user_login WHERE hotelid=?";
            query = query + " AND userid = ?";
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
             Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
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
            query = query + "login_date = '" + dateedit.getDate(0) + "',";
            query = query + "login_time = '" + dateedit.getTime(0) + "',";
            query = query + "user_agent = '" + UserAgent + "',";
            query = query + "remote_ip  = '" + ReplaceString.SQLEscape(( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr() )) + "'";
            query = query + " WHERE hotelid='" + hotelid + "'";
            query = query + " AND userid = " + ownerinfo.DbUserId;
            query = query + " AND kind = 1";
        }
        else
        {
            query = "INSERT INTO owner_user_login SET ";
            query = query + "login_date = '" + dateedit.getDate(0) + "',";
            query = query + "login_time = '" + dateedit.getTime(0) + "',";
            query = query + "user_agent = '" + UserAgent + "',";
            query = query + "remote_ip  = '" + ReplaceString.SQLEscape(( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr() )) + "',";
            query = query + "hotelid='" + hotelid + "',";
            query = query + "userid = " + ownerinfo.DbUserId + ",";
            query = query + "kind = 1";
        }
        prestate    = connection.prepareStatement(query);
        prestate.executeUpdate();
        DBConnection.releaseResources(resultset,prestate,connection);

        // �Ǘ��X�ܐ����J�E���g����
        count = 0;
        DbAccess db_manage = new DbAccess();

        // �Ǘ��X�ܐ������[�v
        ResultSet DbManageHotel = ownerinfo.getManageHotel(db_manage, ownerinfo.HotelId, ownerinfo.DbUserId);
        if( DbManageHotel != null )
        {
            ret = DbManageHotel.first();
            while( ret != false )
            {
                count++;
                ret = DbManageHotel.next();
            }
        }
        else
        {
            db_manage.close();
            response.sendRedirect("../../common/pc/error/loginerr.html");
            return;
        }

        if (count == 0) count=2;

        // ���O�C���������ɌÂ��Z�b�V����ID��j��
        request.getSession(true).invalidate();
        // �V�����Z�b�V�������쐬
        session = request.getSession();
    
        // �Z�b�V���������ɂ��Z�b�g
        session.setAttribute("HotelId", hotelid);
        session.setAttribute("LoginHotelId", hotelid);
        session.setAttribute("LoginId", loginid);
        session.setAttribute("Password", password);

        // �Z�b�V���������ɊǗ��X�ܐ������Ă���
        session.setAttribute("StoreCount", Integer.toString(count) );

        // �p�����^�̃Z�b�g
        ownerinfo.HotelId     = hotelid;
        ownerinfo.DbLoginUser = loginid;
        ownerinfo.DbPassword  = password;
        session.setAttribute("ownerinfo", ownerinfo );

        // ���X�܂̃T�C�g�ɂĒP�X�ܐݒ�̏ꍇ
        if( count <= 1 )
        {
            // �z�e��ID���Z�b�g���Ȃ���
            DbManageHotel.first();

            ownerinfo.HotelId = DbManageHotel.getString("accept_hotelid");
            session.setAttribute("HotelId", ownerinfo.HotelId);
            session.setAttribute("SelectHotel", ownerinfo.HotelId);

        }

        db_manage.close();
        response.sendRedirect(response.encodeURL("ownerindex.jsp"));
    }
    // ���O�C�����s���͂�����(loginerr.jsp��)
    else
    {
        // session�j��
        request.getSession().invalidate();

        response.sendRedirect("../../common/pc/error/loginerr.html");

    }
%>

