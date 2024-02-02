<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%!
    private static final String IMG_DIR        = "upload_files/image"; // �A�b�v���[�h�����摜��ۑ�����t�H���_
    private static final int PAGE_MAX          = 50; // �P�y�[�W����̍ő�\���e�L����
    private static final int DATA_TYPE         = 0;  //  upload_files.data_type: 0����摜,1������̑�
%>
<%
//MySql�p
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

//  ���݂̊O�ω摜
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

//���ݓ���
    DateEdit  de = new DateEdit();
    NumberFormat     nf2;
    nf2     = new DecimalFormat("00");
    int nowdate  = Integer.parseInt(de.getDate(2));
    int nowtime  = Integer.parseInt(de.getTime(1));
    String date_format = (nowdate / 10000) + "/" + nf2.format(nowdate / 100 % 100) + "/" + nf2.format(nowdate % 100) + " " + nf2.format(nowtime / 10000) + ":" + nf2.format(nowtime / 100 % 100) + ":" + nf2.format(nowtime % 100);


// �p�X�֘A
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
<title>�O�ω摜�ݒ�</title>
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
					<strong>�O�ω摜�ݒ�</strong><br>
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
				�y���݂̌f�ډ摜�z<br>
				<a href="<%=IMG_DIR_PATH + "/" + appearance%>" target='_blank'><img src="<%=IMG_DIR_PATH + "/" + appearance%>" width="<%=imgwidth%>" border="0"></a>
				<br>
				<input name="submit_del"       type="submit" value="�O�ω摜�폜">
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
					<input name="submit_upd"   type="submit" value="�O�ω摜�ݒ�">
				</div>
<%
    //���e�ς݉摜�������擾
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
        // �薼�̒���
            String subject = result.getString("subject");
        // ���e���t
            String date_image = (result.getInt("add_date") / 10000) + "/" + nf2.format(result.getInt("add_date") / 100 % 100) + "/" + nf2.format(result.getInt("add_date") % 100) + " " + nf2.format(result.getInt("add_time") / 10000) + ":" + nf2.format(result.getInt("add_time") / 100 % 100) + ":" + nf2.format(result.getInt("add_time") % 100);
%>
			<hr size="1" noshade>
				[<%if(imedia_user == 1){%><%=result.getString("hotel_id")%>,<%}%><%=id_image%>]
<%
            if (imedia_user == 1 || !loginHotelId.equals("demo"))
            {
%>
				<small>�摜���F<%=result.getString("filename")%></small>
<%
            }
%>
				<small>���摜���F<%=result.getString("originalfilename")%></small>
				<small>���e���F<%=date_image%></small>
				<input type="radio" name="appearance" value="" onclick="this.value='<%=result.getString("filename")%>';document.getElementById('submit_appearance').style.display='block';" <%if (result.getString("filename").equals(appearance)){%>checked<%}%>>�O�ω摜�ɐݒ�
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
