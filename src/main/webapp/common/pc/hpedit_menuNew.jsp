<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    DateEdit  de          = new DateEdit();
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="timeout.html?<%=de.getDate(2)%><%=de.getTime(1)%>" />
<%
    }
    String requestUri = request.getRequestURI();
    boolean DebugMode = false;
    if (requestUri.indexOf("_debug_/demo/") != -1)
    {
       DebugMode = true;
    }

    boolean    TargetAll  = false;
    if  (hotelid.compareTo("all") == 0)
    {
         TargetAll = true;
         hotelid   = "demo";
    }


    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");

    String Type      = request.getParameter("Type");
    if(CheckString.isValidParameter(Type) && !CheckString.numAlphaCheck(Type))
    {
        response.sendError(400);
        return;
    }

    if    (Type     == null)
    {
           Type      = "pc";
    }
    int        i                  = 0;
    int        type               = 0; //0:PC 1:�g��
    if (Type.compareTo("pc") != 0)
    {
               type               = 1; //�g��
    }

    int        imedia_user        = 0;
    String     query              = "";
    boolean    ExistFlag          = false;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    // imedia_user �̃`�F�b�N
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user = result.getInt("imedia_user");
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
%>
<%@ include file="../../common/pc/menu_ini.jsp" %>
<%
    int       nowdate     = Integer.parseInt(de.getDate(2));
    // HP�ҏW���j���[���̂̏�������
    try
    {
        query = "SELECT * FROM menu WHERE hotelid=?";
        query = query + " AND data_type=?";
        query = query + " AND start_date <=" + nowdate;
        query = query + " AND end_date >=" + nowdate;
        query = query + " AND disp_flg =1";
        query = query + " AND hpedit_id>0";
        prestate    = connection.prepareStatement(query);
        prestate.setString( 1, hotelid);
        prestate.setInt( 2, (type*2+1));
        result      = prestate.executeQuery();
        while( result.next() )
        {
            Title[type][result.getInt("hpedit_id")-1]=result.getString("title");
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

    String image_hotelid = "demo";
    String appearance    = "";
    String ftp_password = "";

    //  �X�܂̃`�F�b�N
    try
    {
        query = "SELECT * FROM hotel WHERE hotel.hotel_id =?";
        query = query + " AND hotel.plan <= 4";
        query = query + " AND hotel.plan >= 1";
        query = query + " AND hotel.plan != 2";
        prestate    = connection.prepareStatement(query);
        prestate.setString( 1, hotelid);
        result      = prestate.executeQuery();
        if  (result != null)
        {
            if( result.next() )
            {
                image_hotelid = result.getString("hotel_id");
                appearance    = result.getString("appearance");
                ftp_password = result.getString("ftp_passwd");
            }
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



//�V�z�e�i�r�Ή�
    int trial_date = 99999999;
    int start_date = 99999999;


    query = "SELECT * FROM hotel_element WHERE hotel_id= ?";
    prestate    = connection.prepareStatement(query);
    prestate.setString( 1, hotelid);
    result      = prestate.executeQuery();

    if (result != null)
    {
        if( result.next() )
        {
            trial_date              	= result.getInt("trial_date");
            start_date              	= result.getInt("start_date");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>���j���[</title>
<link href="../../<%= image_hotelid %>/pc/menu.css" rel="stylesheet" type="text/css">
<link href="http://www.hotenavi.com/<%= image_hotelid %>/menu.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
	var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
		var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
		if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
	var p,i,x;	if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
		d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	 if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('image/ueyajirushi_o.gif')">

<table width="156" border="0" cellspacing="0" cellpadding="0">

	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="7" height="20"></td>
	</tr>
	<tr>
		<td><div class="grouptop"><%= Type %>��HP�ҏW</div></td>
	</tr>
	<tr>
		<td><img src="../../common/pc/image/spacer.gif" width="7" height="8"></td>
	</tr>
	<tr valign="top">
		<td>
			<table width="156" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top">
						<!-- ���z�e�i�r���j���[�{�b�N�X -->
						<table width="156" cellspacing="0" cellpadding="0" class="menubox" id="oldMenu" <%if(type==0 && (nowdate >= start_date || (nowdate >= trial_date && imedia_user == 1))){%>style="display:none;"<%}%>>
<% if(type==0 && nowdate < start_date && nowdate >= trial_date){%><tr><td><input type="button" value="�V���j���[��" onclick="document.getElementById('oldMenu').style.display='none';document.getElementById('newMenu').style.display='block';"></td></tr><%}%>
	<tr>
		<td><div class="name">���ҏW���ڂ�I�����Ă�������</div></td>
	</tr>
<%
if (type==1 || nowdate < start_date)
{
    for(i = 0; i < 100 ; i++)
    {
        if (MenuNo[type][i] == 0) break;

        ExistFlag     = false;
        // edit_event_info �L���`�F�b�N
        try
        {
            query = "SELECT * FROM edit_event_info WHERE hotelid=?";
            query = query + " AND data_type=" + MenuNo[type][i];
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                ExistFlag = true;
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
        if (ExistFlag || imedia_user == 1)
        {
            if(TargetAll)
            {
%>
							<tr>
								<td><a href="event_list_all.jsp?DataType=<%= MenuNo[type][i] %>&DispType=0&SortType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle"><%= Title[type][i] %>
								</a></td>
							</tr>
<%
            }
            else
            {
%>
							<tr>
								<td><a href="event_list.jsp?Type=<%= Type %>&DataType=<%= MenuNo[type][i] %>&disp_type=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle"><%= Title[type][i] %>
								<% if (!ExistFlag){%>&nbsp;(��)<%}%></a></td>
							</tr>
<%
            }
        }
    }
}
%>
						</table>
						<!--���z�e�i�r���j���[�R���e���c�����܂�-->
						<!--�V���z�e�i�r���j���[�R���e���c -->
						<table width="156" cellspacing="0" cellpadding="0" class="menubox" id="newMenu" <%if(type==1 || (nowdate < trial_date || (nowdate < start_date && imedia_user == 0))){%>style="display:none;"<%}%>>
<% if(type==0 && nowdate < start_date && nowdate >= trial_date){%><tr><td><input type="button" value="�����j���[��" onclick="document.getElementById('oldMenu').style.display='block';document.getElementById('newMenu').style.display='none';"></td></tr><%}%>
	<tr>
		<td><div class="name">���ҏW���ڂ�I�����Ă�������</div></td>
	</tr>

<%
    if(type==0 && nowdate >= trial_date){
        boolean setubi_exist = false;
        try
        {
            query = "SELECT * FROM menu_config WHERE hotelid=?";
            query = query + " AND data_type IN (1,2)";
            query = query + " AND contents='setubi'";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() )
            {
                setubi_exist = true;
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
        try
        {
            query = "SELECT * FROM menu_config WHERE hotelid=?";
            query = query + " AND data_type IN (1,2)";
            query = query + " AND (event_data_type<>0 OR contents='service' OR contents='setubi')";
            query = query + " AND event_data_type<>63";
            query = query + " GROUP BY event_data_type";
            query = query + " ORDER BY data_type,disp_idx";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            while( result.next() )
            {
                int eventDataType = result.getInt("event_data_type");
                String contents   = result.getString("contents");
                if (contents.equals("service"))
                {
                    if (setubi_exist)
                    {
                        eventDataType = 31;
                    }
                    else
                    {
                        eventDataType = 15;
                    }
                }
                if (contents.equals("setubi"))
                {
                    eventDataType = 15;
                }
%>
							<tr <%if(eventDataType==5 && imedia_user==0){%>style="display:none"<%}%>>
								<td><a href="event_list.jsp?Type=<%= Type %>&DataType=<%= eventDataType %>&disp_type=0" target="contents"><img src="https://www.hotenavi.com/contents/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">
								<%if(eventDataType==1){%>TOP(<%}%><%= result.getString("page_title") %><%if (eventDataType==1){%>)<%}%>
								</a></td>
							</tr>
<%
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
    }
%>
						</table>
						<!--�V�z�e�i�r���j���[�R���e���c�����܂�-->
<%
    DBConnection.releaseResources(connection);
%>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="7" height="10"></td>
				</tr>
<%
    if (imedia_user != 1)
    {
%>
				<tr>
					<td class="name">�ҏW�������e���z�[���y�[�W�ɔ��f����Ȃ��ꍇ��ҏW�\���j���[�𑝂₵�����ꍇ�́A�A�����b�N�X�܂ł��⍇�����������B</td>
				</tr>
<%
    }
%>

				<tr>
					<td valign="top">
						<table width="156" cellspacing="0" cellpadding="0" class="menubox">
							<tr>
								<td><a href="upload_image.jsp" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�摜�A�b�v���[�h</a>
							</tr>
							<tr>
								<td><a href="upload_file.jsp" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">��e�ʃt�@�C���A�b�v���[�h</a>
							</tr>
<%
//    if (imedia_user == 1 || !appearance.equals(""))
    if (imedia_user == 1)
    {
%>
							<tr>
								<td><a href="image_appearance.jsp" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�O�ω摜�ݒ�</a>
							</tr>
<%
    }
%>
						</table>
					</td>
				</tr>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="7" height="10"></td>
				</tr>

<%
    if (imedia_user == 1 && DebugMode)
    {
        if(!TargetAll)
        {
%>
				<tr>
					<td class="name">���ȉ��A�����b�N�X��p�ҏW����</td>
				</tr>
				<tr>
					<td valign="top">
						<table width="156" cellspacing="0" cellpadding="0" class="menubox">
							<tr>
								<td><a href="tool_ftptrans.jsp?DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�X�}�z�Ή�</a>
							</tr>
							<tr>
								<td><a href="room_list2.jsp?DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�������</a>
							</tr>
							<tr>
								<td><a href="service_list2.jsp?DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�ݔ����</a>
							</tr>
							<tr>
								<td><a href="price_list2.jsp?DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�����V�X�e��</a>
							</tr>
							<tr>
								<td><a href="roomrank_list2.jsp?DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�����N�ʗ���</a>
							</tr>
<%
			if(nowdate < start_date)
			{
				if (type == 0)
				{
%>
							<tr>
								<td><a href="menu_list.jsp?DataType=1&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�r�W�^�[���j���[</a>
							</tr>
							<tr>
								<td><a href="menu_list.jsp?DataType=2&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�����o�[���j���[</a>
							</tr>
							<tr>
								<td><a href="menu_list.jsp?DataType=11&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�O���[�v�����N</a>
							</tr>
							<tr>
								<td><a href="menu_list.jsp?DataType=12&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�I�t�B�V���������N</a>
							</tr>
							<tr>
								<td><a href="menu_qredit_form.jsp?HotelId=<%=hotelid%>" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">PC�t���[�G���A��</a>
							</tr>
<%
				}
				if (type == 1)
				{
%>
							<tr>
								<td><a href="menu_list.jsp?DataType=3&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�޼����ƭ�</a>
							</tr>
							<tr>
								<td><a href="menu_list.jsp?DataType=4&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">���ް�ƭ�</a>
							</tr>
							<tr>
								<td><a href="menu_list.jsp?DataType=13&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">��ٰ���ݸ</a>
							</tr>
							<tr>
								<td><a href="menu_list.jsp?DataType=14&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�̨����ݸ</a>
							</tr>
							<tr>
								<td><a href="menu_list.jsp?DataType=16&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">���m�点</a>
							</tr>
							<tr>
								<td><a href="menu_mobilefree_form.jsp?HotelId=<%=hotelid%>" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�g�уt���[�G���A</a>
							</tr>
<%
				}
%>
							<tr>
								<td><a href="accesscount_edit.jsp?<%=de.getDate(2)%><%=de.getTime(1)%>" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�A�N�Z�X�J�E���g</a>
							</tr>
							<tr>
								<td><a href="menu_notice_form.jsp?HotelId=<%=hotelid%>" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">���m��</a>
							</tr>
						</table>
					</td>
				</tr>

<%
				if (type == 0)
				{
%>
				<tr>
					<td><img src="../../common/pc/image/spacer.gif" width="7" height="10"></td>
				</tr>
				<tr>
					<td class="name">�V�z�e�i�r�Ή�</td>
				</tr>
				<tr>
					<td valign="top">
						<table width="156" cellspacing="0" cellpadding="0" class="menubox">
							<tr>
								<td><a href="tool_convertHtml.jsp?<%=de.getDate(2)%><%=de.getTime(1)%>" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�V�T�C�g�ϊ�</a>
							</tr>
<%
				}
			}
			if(type == 0)
			{
%>
							<tr>
								<td><a href="menu_config_list.jsp?DataType=1&DispType=0&<%=de.getDate(2)%><%=de.getTime(1)%>" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�r�W�^�[���j���[</a>
							</tr>
							<tr>
								<td><a href="menu_config_list.jsp?DataType=2&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�����o�[���j���[</a>
							</tr>
							<tr>
								<td><a href="menu_config_list.jsp?DataType=11&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�O���[�v�����N</a>
							</tr>
							<tr>
								<td><a href="menu_config_list.jsp?DataType=12&DispType=0" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�I�t�B�V���������N</a>
							</tr>
							<tr>
								<td><a href="menu_config_qredit_form.jsp?HotelId=<%=hotelid%>" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">PC�t���[�G���A��</a>
							</tr>
							<!--tr>
								<td><a href="accesscount_edit.jsp?<%=de.getDate(2)%><%=de.getTime(1)%>" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">�A�N�Z�X�J�E���g</a>
							</tr-->
							<tr>
								<td><a href="menu_config_notice_form.jsp?HotelId=<%=hotelid%>" target="contents"><img src="http://www.hotenavi.com/<%= image_hotelid %>/image/sankaku.gif" width="8" height="8" border="0" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="2" height="12" border="0" align="absmiddle">���m��</a>
							</tr>

						</table>
					</td>
				</tr>
<%
			}
%>
<%
        }
    }
%>
			</table>
		</td>
	</tr>
</table>

</body>
</html>
