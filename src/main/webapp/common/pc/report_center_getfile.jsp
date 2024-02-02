<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }

    int       readsize = 0;
    byte      outbuff[] = new byte[4096];
    String    filepath = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query       = "";
    boolean      acceptuser = false;

    String requestUri = request.getRequestURI();
    if (requestUri.indexOf("/_debug_/") != -1)
    {
        acceptuser = true;
    }

    // ファイル名を取得
    String filename = ReplaceString.getParameter(request,"fname");
    if( filename == null )
    {
        filename = "";
    }
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if( hotelid != null )
    {
		if(!CheckString.hotenaviIdCheck(hotelid))
		{
			hotelid="0";
%>
			<script type="text/javascript">
			<!--
			var dd = new Date();
			setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
			//-->
			</script>
<%
		}
        // 対象ユーザの管理できる店舗かどうかの確認
        query = "SELECT * FROM hotel,owner_user_hotel,owner_user_security WHERE owner_user_hotel.hotelid=?";
        query = query + " AND owner_user_hotel.userid=?";
        query = query + " AND owner_user_security.hotelid=owner_user_hotel.hotelid";
        query = query + " AND owner_user_security.userid=owner_user_hotel.userid";
        if( filename.indexOf(".xls") >= 0 )
        {
            query = query + " AND owner_user_security.sec_level09=1"; //ダウンロード権限
        }
        query = query + " AND owner_user_security.sec_level10=1"; //多店舗選択権限
        query = query + " AND hotel.hotel_id=owner_user_hotel.accept_hotelid";
        query = query + " AND hotel.center_id = ?"; //管理店舗の中のセンター帳票に含まれている
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,(String)session.getAttribute("LoginHotelId") );
        prestate.setInt(2,ownerinfo.DbUserId);
        prestate.setString(3,hotelid);
        result      = prestate.executeQuery();
		try
		{
			if( result.next() )
			{
				acceptuser = true;
			}
		}
		catch( Exception e )
		{
			Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
		}
		finally
		{
			DBConnection.releaseResources(result, prestate, connection);
		}

        // 許可されたユーザのみ
        if( acceptuser != false )
        {
            if( filename != null )
            {
                if( filename.compareTo("") != 0 )
                {
                    if( filename.indexOf("/") == -1 && filename.indexOf("\\") == -1 && filename.indexOf("..") == -1 )
                    {
                        try
                        {
                            filepath = "/hotenavi/" + hotelid.toLowerCase() + "/pc/sales/";

                            FileInputStream fistream = new FileInputStream(filepath + filename);
                            ServletOutputStream outstream = response.getOutputStream();

                            if( filename.indexOf(".xls") >= 0 )
                            {
                                response.setContentType("application/vnd.ms-excel");
                                response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
                            }
                            else
                            {
                                response.setContentType("application/pdf");
                                response.setHeader("Content-disposition", "inline; filename=\"" + filename + "\"");
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
                            if (acceptuser != false)
                            {
                                filename = "/demo/pc/sales/nofile.htm";
                            }
                            else
                            {
                                filename = "/" + hotelid.toLowerCase() + "/pc/sales/nofile.htm";
                            }
                                response.sendRedirect( filename );
                                return;
                        }
                    }
                }
            }
        }
    }
%>
