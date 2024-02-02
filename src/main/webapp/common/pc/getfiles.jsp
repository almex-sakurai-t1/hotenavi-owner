<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    DateEdit  de = new DateEdit();
    int nowdate  = Integer.parseInt(de.getDate(2));
    int nowtime  = Integer.parseInt(de.getTime(1));

    String Referer    = request.getHeader("Referer");
    if    (Referer== null) Referer = "";
    String requestUri = request.getRequestURI();
    String hotelid    = requestUri.replace("_debug_","");
    hotelid           = hotelid.replace("pc/getfiles.jsp","");
    hotelid           = hotelid.replace("/","");

    int          readsize = 0;
    byte         outbuff[] = new byte[4096];
    String       filepath;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query       = "";
    boolean      acceptuser = false;

    // ファイル名を取得
    String filename = ReplaceString.getParameter(request,"fname");
    if( filename == null )
    {
        filename = "";
    }

    Referer = Referer.replace("_debug_/","");
    if (Referer.equals("http://owner.hotenavi.com/" + hotelid + "/pc/index.html") || Referer.equals("https://owner.hotenavi.com/" + hotelid + "/pc/index.html") || Referer.indexOf("https://owner.hotenavi.com/happyhotel/")==0)
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            acceptuser = true;
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        String UserAgent = request.getHeader("user-agent");
        if (UserAgent.length() > 255)
        {
           UserAgent = UserAgent.substring(0,255);
        }

        query = "INSERT INTO hh_owner_user_log SET ";
        query = query + "hotel_id='"         + hotelid            + "', ";
        query = query + "user_id=0,";
        query = query + "login_date="        + nowdate            + ", ";
        query = query + "login_time="        + nowtime            + ", ";
        query = query + "log_level=999, ";
        query = query + "log_detail='"       + filename           + "', ";
        query = query + "user_agent='"       + UserAgent  + "', ";
        query = query + "remote_ip='"        + ( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() )          + "' ";
        prestate    = connection.prepareStatement(query);
        int retresult   = prestate.executeUpdate();
        DBConnection.releaseResources(result,prestate,connection);

        // 許可されたユーザのみ
        if( acceptuser != false )
        {
            if( filename != null )
            {
                if( filename.compareTo("") != 0 )
                {
                    try
                    {
                        filepath = "/hotenavi/upload_files/others/";
                        FileInputStream fistream = new FileInputStream(filepath + filename);
                        ServletOutputStream outstream = response.getOutputStream();
                        if( filename.indexOf(".xls") >= 0 )
                        {
                            response.setContentType("application/vnd.ms-excel");
                            response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
                        }
                        else if( filename.indexOf(".pdf") >= 0 )
                        {
                            response.setContentType("application/pdf");
                            response.setHeader("Content-disposition", "inline; filename=\"" + filename + "\"");
                        }
                        else
                        {
                            response.setContentType("application/octetstream");
                            response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
                        }
                        while( (readsize = fistream.read(outbuff)) != -1 )
                        {
                            outstream.write(outbuff, 0, readsize);
                        }
                        fistream.close();
                        outstream.close();
                        return;
                    }
                    catch( Exception e )
                    {
                        filename = "/" + hotelid.toLowerCase() + "/pc/sales/nofile.htm";
                        response.sendRedirect( filename );
                        return;
                    }
                }
            }
        }
    }
%>
<jsp:forward page="../../common/pc/getfiles_error.jsp" />
