<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
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
    String paramHotelId = ReplaceString.getParameter(request,"LoginHotelId");
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String            query       = "";

    int          imedia_user        = 0;
    int          level              = 0;
     // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    boolean           acceptuser = false;

    // ファイル名を取得
    String filename = ReplaceString.getParameter(request,"fname");
    if( filename == null )
    {
        filename = "";
    }
    String ftype = ReplaceString.getParameter(request,"ftype");
    if( ftype == null )
    {
        ftype = "sales";
    }
    String num = ReplaceString.getParameter(request,"num");
    if( num == null )
    {
        num = "1";
    }

    String hotelid = (String)session.getAttribute("SelectHotel");
    String center_id = loginHotelId;

    Calendar cal=Calendar.getInstance();
    String       filepath;
    File checkFile = null;
    File renameFile = null;
    long lastModified_pdf = 0;
    long lastModified_xls = 0;
    boolean FileDel   = true;
    boolean errorflag = false;
    String  strMsg = "";

    if( hotelid == null)
    {
        hotelid = "";
    }

    if (imedia_user == 1 && level == 3)
    {
        acceptuser = true;
    }
    else if (hotelid.equals("all"))
    {
        // 対象ユーザの管理できる店舗かどうかの確認
        query = "SELECT * FROM owner_user_security WHERE hotelid=?";
        query = query + " AND owner_user_security.sec_level10=1";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            acceptuser = true;
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        // センターのホテルID
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            center_id = result.getString("center_id");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
        hotelid = center_id;
    }
    else
    {
        // 対象ユーザの管理できる店舗かどうかの確認
        query = "SELECT * FROM owner_user_hotel,owner_user_security WHERE owner_user_hotel.hotelid=?";
        query = query + " AND owner_user_security.hotelid=owner_user_hotel.hotelid";
        query = query + " AND owner_user_security.userid=owner_user_hotel.userid";
        query = query + " AND (owner_user_security.admin_flag=1 OR owner_user_security.sec_level03=1)";
        query = query + " AND owner_user_hotel.userid=?";
        query = query + " AND owner_user_hotel.accept_hotelid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        prestate.setString(3, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            acceptuser = true;
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    String ftp_password = "";
    String ftp_server = "mars.hotenavi.com";
    query = "SELECT * FROM hotel WHERE hotel_id =?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, hotelid);
    result      = prestate.executeQuery();
    if( result.next())
    {
        ftp_password = result.getString("ftp_passwd");
    }
    DBConnection.releaseResources(result,prestate,connection);

    String command = "";
    // 許可されたユーザのみ
    if( acceptuser)
    {
        if( filename != null )
        {
            if( filename.compareTo("") != 0 )
            {
                if( filename.indexOf("/") == -1 && filename.indexOf("\\") == -1 && filename.indexOf("..") == -1 )
                {
                    try
                    {
                        filepath = "/hotenavi/" + hotelid.toLowerCase() + "/pc/sales";
                        checkFile = new File(filepath + "/" + filename + ".xls");
                        lastModified_xls =checkFile.lastModified();
                        checkFile = new File(filepath + "/" + filename+ "-" + num + ".pdf");
                        lastModified_pdf =checkFile.lastModified();
%>checkFile.exists():<%=checkFile.exists()%><br>
lastModified_xls:<%=lastModified_xls%><br>
lastModified_pdf:<%=lastModified_pdf%><br>
<%
//                        if(checkFile.exists())
//                        {
//                            if (lastModified_xls > lastModified_pdf)
//                            {
%>
PDF形式に変換されていません。しばらく経った後にお試しください。<br>
<%
//                               deleteFile(ftp_server,hotelid,ftp_password, "/pc/sales/" + filename+ "-" + num + ".pdf");
//                            }
//                        }
//                        checkFile = new File(filepath + "/" + filename+ "-" + num + ".pdf");
                        if(checkFile.exists())
                        {
                            if (lastModified_xls > lastModified_pdf)
                            {
                                command = "/usr/local/etc/pdf_delete.sh " +  hotelid + " " + filename + "-" + num;
                                try
                                {
                                   ProcessBuilder b = new ProcessBuilder("/usr/local/etc/pdf_delete.sh",hotelid,filename + "-*");
                                   b.redirectErrorStream(true);
//                                   b.command(command+".pdf");
                                   Process process = b.start();
                                   InputStream is = process.getInputStream();
                                   try {
                                    while(is.read() >= 0); //標準出力だけ読み込めばよい
                                   } finally {
                                     is.close();
                                     process.destroy();
                                    }

//                                    try {
//                                           process.waitFor(); // プロセスの正常終了まで待機させる
//                                    } catch (InterruptedException e) {
//                                           Logging.info( "report_getpdf.jsp InterruptedException=" + e.toString() );
////                                    }
                                }
                                catch ( IOException e )
                                {
 //                                   errorflag = true;
 //                                   strMsg = e.toString();
                                    Logging.info( "report_getpdf.jsp IOException=" + e.toString() );
                                }
                            }
                        }
                        if(errorflag)
                        {
                            strMsg = "<font color=\"red\">" + strMsg + "</font>";
%>
<%=strMsg%>
<%
                        }
                        else
                        {
                            %><jsp:forward page="/servlet/LedgerSheet"/>
                            <%
                        }
                        return;
                    }
                    catch( Exception e )
                    {
                        if (loginHotelId.equals("demo"))
                        {
                            Logging.info("report_getpdf.jsp e=" + e.toString() + ",filename="+filename );
                            filename = "/demo/pc/sales/nofile.htm";
                        }
                        else
                        {
                            Logging.info("report_getpdf.jsp e=" + e.toString()+ ",filename="+filename );
                            filename = "/" + hotelid.toLowerCase() + "/pc/sales/nofile.htm";
                        }
                        response.sendRedirect( filename );
                        return;
                    }
                }
            }
        }
    }
%>
<%@ page import="org.apache.commons.net.ftp.FTPClient" %>
<%@ page import="org.apache.commons.net.ftp.FTPReply" %>
<%!
public static void deleteFile(String host,String user,String password,String fileName)
throws IOException {
    FTPClient fp = new FTPClient();
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            Logging.info("connection failed:"+host);
            System.out.println("connection failed");
            System.exit(1); // 異常終了

        }
        if (fp.login(user, password) == false) { // ログインできたか？
             Logging.info("login failed:"+user);
             System.out.println("login failed");
             System.exit(2); // 異常終了
        }

        // ファイル削除
        fp.deleteFile(fileName);
        Logging.info("deleteFile ファイル削除");
        fp.logout();
    }
    catch( Exception e )
    {
        Logging.info("deleteFile e=" + e.toString() );
    }
    finally
    {
        fp.disconnect();
        fp = null;
    }
}
%>
