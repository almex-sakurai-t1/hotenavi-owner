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

    String requestUri    = request.getRequestURI();
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");

    int       readsize = 0;
    byte         outbuff[] = new byte[4096];
    String       filepath;
    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    boolean      acceptuser = false;

    // ファイル名を取得
    String filename = ReplaceString.getParameter(request,"fname");
    if( filename == null )
    {
        filename = "";
    }
    String hotelid = ReplaceString.getParameter(request,"HotelId");
	boolean ret = false;
    if( hotelid != null)
    {
		if(!CheckString.hotenaviIdCheck(hotelid))
		{
			hotelid = "0";
%>
			<script type="text/javascript">
			<!--
			var dd = new Date();
			setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
			//-->
			</script>
<%
		}
        if (requestUri.equals("/_debug_/demo/pc/report_getfile.jsp") && loginHotelId.equals("demo"))
        {
            query = "SELECT * FROM owner_user WHERE hotelid=?";
            query = query + " AND userid=?";
            query = query + " AND imedia_user=1";
        }
        else
        {
            // 対象ユーザの管理できる店舗かどうかの確認
            query = "SELECT * FROM owner_user_hotel,owner_user_security WHERE owner_user_hotel.hotelid=?";
            query = query + " AND owner_user_security.hotelid=owner_user_hotel.hotelid";
            query = query + " AND owner_user_security.userid=owner_user_hotel.userid";
            if( filename.indexOf(".xls") >= 0 )
            {
                query = query + " AND owner_user_security.sec_level09=1"; //ダウンロード権限
            }
            query = query + " AND owner_user_hotel.userid=?";
            query = query + " AND owner_user_hotel.accept_hotelid=?";
            ret = true;
        }
        try
        {
	        connection  = DBConnection.getConnection();
	        prestate    = connection.prepareStatement(query);
	        prestate.setString(1, loginHotelId);
	        prestate.setInt(2, ownerinfo.DbUserId);
	        if(ret)
	        {
	        	prestate.setString(3, hotelid);
	        }
	        result      = prestate.executeQuery();
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
                                if (hotelid.compareTo("jump") == 0)
                                {
                                    response.setHeader("Content-disposition", "inline; filename=\"" + filename + "\"");
                                }
                                else
                                {
                                    response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
                                }
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
    }
%>
