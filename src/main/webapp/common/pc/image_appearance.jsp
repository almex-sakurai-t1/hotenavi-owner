<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%!
    private static final String IMG_DIR        = "upload_files/image"; // アップロードした画像を保存するフォルダ
    private static final int PAGE_MAX          = 50; // １ページ当りの最大表示親記事数
    private static final int DATA_TYPE         = 0;  //  upload_files.data_type: 0･･･画像,1･･･その他
%>
<%
//MySql用
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    String query = "";

    String loginHotelId  = (String)session.getAttribute("LoginHotelId");
    if( loginHotelId == null )
    {
        loginHotelId = "demo";
    }
    String hotelid       = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
        hotelid = "demo";
    }

    int imedia_user = 0;
    try
    {
        query = "SELECT imedia_user FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    if (ReplaceString.getParameter(request,"submit_upd") != null)
    {
        query = "UPDATE hotel SET appearance = '" + ReplaceString.getParameter(request,"appearance") + "'";
        query = query + " WHERE hotel_id=?";
        try
        {
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,hotelid);
            prestate.executeUpdate();
            DBConnection.releaseResources(prestate);
        }
        catch( Exception e )
        {
%>
	<%= e.toString() %>
<%
        }
    }
    if (ReplaceString.getParameter(request,"submit_del") != null)
    {
        query = "UPDATE hotel SET appearance = ''";
        query = query + " WHERE hotel_id=?";
        try
        {
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,hotelid);
            prestate.executeUpdate();
            DBConnection.releaseResources(prestate);
        }
        catch( Exception e )
        {
%>
	<%= e.toString() %>
<%
        }
    }

//  現在の外観画像
    String appearance = "";
    String imgwidth   = "";
    query = "SELECT appearance FROM hotel WHERE hotel_id=?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid);
    result      = prestate.executeQuery();
    if( result.next() != false )
    {
        appearance = result.getString("appearance");
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);
    if (!appearance.equals(""))
    {
        query = "SELECT imgwidth FROM upload_files WHERE hotel_id=?";
        query = query + " AND filename ='" + appearance + "'";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imgwidth = result.getString("imgwidth");
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

//現在日時
    DateEdit  de = new DateEdit();
    NumberFormat     nf2;
    nf2     = new DecimalFormat("00");
    int nowdate  = Integer.parseInt(de.getDate(2));
    int nowtime  = Integer.parseInt(de.getTime(1));
    String date_format = (nowdate / 10000) + "/" + nf2.format(nowdate / 100 % 100) + "/" + nf2.format(nowdate % 100) + " " + nf2.format(nowtime / 10000) + ":" + nf2.format(nowtime / 100 % 100) + ":" + nf2.format(nowtime % 100);


// パス関連
    String requestUri = request.getRequestURI();
    String contextPath = request.getContextPath();
    if (requestUri.indexOf(contextPath)==0)
    {
        requestUri = requestUri.substring(contextPath.length() + 1);
    }

    String JSP_PATH = application.getRealPath(requestUri);
    String JSP_FILE = JSP_PATH.substring(JSP_PATH.lastIndexOf(File.separator) + 1, JSP_PATH.length());
    String BASE_PATH     = JSP_PATH.substring(0, JSP_PATH.length()-requestUri.length());
    String UPLOAD_DIR_PATH =  BASE_PATH + IMG_DIR;
    String IMG_DIR_PATH =  "/" + IMG_DIR;
%>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>外観画像設定</title>
<% if (loginHotelId.compareTo("hotenavi") != 0){%>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<%}%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
</style>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
	<tr valign="top">
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
					<strong>外観画像設定</strong><br>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<form action="<%=JSP_FILE%>" method="POST">
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
<%
        if (!appearance.equals(""))
        {
%>
			<tr>
				<td class="honbun">
				【現在の掲載画像】<br>
				<a href="<%=IMG_DIR_PATH + "/" + appearance%>" target='_blank'><img src="<%=IMG_DIR_PATH + "/" + appearance%>" width="<%=imgwidth%>" border="0"></a>
				<br>
				<input name="submit_del"       type="submit" value="外観画像削除">
				<input name="appearance_del"   type="hidden" value="<%=appearance%>">
				<hr size="1" noshade>
				</td>
			</tr>
<%
        }
%>
			<tr>
				<td class="honbun">
				<div style="display:none" id="submit_appearance">
					<input name="submit_upd"   type="submit" value="外観画像設定">
				</div>
<%
    //投稿済み画像件数を取得
        int count = 0;
        query = "SELECT count(*) FROM upload_files";
        query = query + " WHERE target_hotelid =?";
        query = query + " AND data_type=" + DATA_TYPE;
        query = query + " AND del_flag=0";
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result   = prestate.executeQuery();
        if( result.next() != false )
        {
            count = result.getInt(1);
        }
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);

        query = "SELECT * FROM upload_files";
        query = query + " WHERE target_hotelid = ?";
        query = query + " AND data_type=" + DATA_TYPE;
        query = query + " AND del_flag=0";
        query = query + " ORDER BY add_date DESC,add_time DESC";
        prestate = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result   = prestate.executeQuery();

        int        id_image = 0;
        while( result.next() != false )
        {
            id_image = result.getInt("id");
        // 題名の長さ
            String subject = result.getString("subject");
        // 投稿日付
            String date_image = (result.getInt("add_date") / 10000) + "/" + nf2.format(result.getInt("add_date") / 100 % 100) + "/" + nf2.format(result.getInt("add_date") % 100) + " " + nf2.format(result.getInt("add_time") / 10000) + ":" + nf2.format(result.getInt("add_time") / 100 % 100) + ":" + nf2.format(result.getInt("add_time") % 100);
%>
			<hr size="1" noshade>
				[<%if(imedia_user == 1){%><%=result.getString("hotel_id")%>,<%}%><%=id_image%>]
<%
            if (imedia_user == 1 || !loginHotelId.equals("demo"))
            {
%>
				<small>画像名：<%=result.getString("filename")%></small>
<%
            }
%>
				<small>元画像名：<%=result.getString("originalfilename")%></small>
				<small>投稿日：<%=date_image%></small>
				<input type="radio" name="appearance" value="" onclick="this.value='<%=result.getString("filename")%>';document.getElementById('submit_appearance').style.display='block';" <%if (result.getString("filename").equals(appearance)){%>checked<%}%>>外観画像に設定
				<br>
				<blockquote>
<%
            if (result.getString("message")!=null)
            {
                if (result.getString("message").length() > 1)
                {
%>
				<small><%=result.getString("message").replace("\r","<br>")%></small> <br>
<%
                }
            }
%>
<%
            if (imedia_user == 1 || !loginHotelId.equals("demo"))
            {
%>
				<%if (result.getString("filename")!=null){%><a href="<%=IMG_DIR_PATH + "/" + (String)result.getString("filename")%>" target='_blank'><img src="<%=IMG_DIR_PATH + "/" + result.getString("filename")%>" width="<%=result.getString("imgwidth")%>" border="0"></a><%}%>
<%
            }
%>
				</blockquote>
<%
        }
        DBConnection.releaseResources(result,prestate,connection);
%>
				<hr size="1" noshade>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	</form>
</table>
</body>
</html>
