<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.HotelElement" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �z�e��ID�擾
    String hotelid = (String)session.getAttribute("SelectHotel");
%>
<%
		if( hotelid == null )
		{
%>
				<jsp:forward page="error/error.html" />
<%
		}
%>
<%@ include file="html_old_design.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>�f����</title>
<link href="<%=cssFile%>" rel="stylesheet" type="text/css">
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

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('image/yajirushi.gif')">
<%
		// �n���h���l�[��
		String param_name    = ReplaceString.getParameter(request,"name");
		if (param_name       == null)                                   //20100910�ǉ�
		{
			param_name       = "";
		}
		String name          = ReplaceString.HTMLEscape(param_name);    //20100910�ǉ�
		// �薼
		String param_subject = ReplaceString.getParameter(request,"subject");
		if (param_subject    == null)                                   //20100910�ǉ�
		{
			param_subject    = "";
		}
		String subject       = ReplaceString.HTMLEscape(param_subject); //20100910�ǉ�
		// ���[���A�h���X
		String mail          = ""; //�K���󔒂ɂ���
		// �{��
		String param_body    = ReplaceString.getParameter(request,"body");
		if( param_body       == null )                                  //20100910�ǉ�
		{
			param_body       = "";
		}
		String body          = param_body;    //20100910�ǉ�
		// �폜�L�[
		String param_delkey  = ReplaceString.getParameter(request,"delkey");
		if (param_delkey     == null)                                   //20100910�ǉ�
		{
			param_delkey     = "";
		}
		String delkey        = ReplaceString.HTMLEscape(param_delkey);  //20100910�ǉ�

		try
		{
				String		query;
				DateEdit dateedit = new DateEdit();
				int    nowDate = Integer.parseInt(dateedit.getDate(2));
				int    startDate = HotelElement.getStartDate(hotelid);

				// MySQL�ւ̐ڑ����m������
				DbAccess db =  new DbAccess();
                String UserAgent = request.getHeader("user-agent");
                if (UserAgent.length() > 255)
                {
                    UserAgent = UserAgent.substring(0,255);
                }

				query = "INSERT INTO bbs VALUES (";
				query = query + "?,";
				query = query + "0,";
				query = query + "0,";
				query = query + "'" + dateedit.getDate(0) + " " + dateedit.getTime(0) + "',";
				query = query + "'" + ReplaceString.SQLEscape(name) + "',";
				query = query + "'" + ReplaceString.SQLEscape(mail) + "',";
				query = query + "'" + ReplaceString.SQLEscape(subject) + "',";
				query = query + "'" + ReplaceString.SQLEscape(body.replace("<br>","\r\n")) + "',";
				query = query + "?,";
				query = query + "0" + ",";
				query = query + "'" + ReplaceString.SQLEscape(UserAgent) + "',";
				query = query + "'" + ReplaceString.SQLEscape(( request.getHeader("X-FORWARDED-FOR") != null ? request.getHeader("X-FORWARDED-FOR").split( "," )[0] : request.getRemoteAddr() )) + "'";
				query = query + ")";

				List<Object> list = new ArrayList<Object>();
				//�p�����[�^�ǉ�
				list.add(hotelid);
				list.add(delkey);
				// SQL�N�G���[�̎��s
				int result = db.execUpdate(query,list);

				// ���[���̑��M
				if( result != 0 )
				{
						DbAccess db_msgid =  new DbAccess();
						DbAccess db_mail =  new DbAccess();

						String mailsubject = "�f���ɓ��e������܂���";

						// ���b�Z�[�W�h�c�̎擾
						query = "SELECT * FROM bbs WHERE hotel_id=?";
						query = query + " ORDER BY msg_id DESC";
						ResultSet bbs_rec = db_msgid.execQuery(query,hotelid);
						bbs_rec.next();

						int msgid = bbs_rec.getInt("msg_id");

						// ���M��̎擾
						query = "SELECT * FROM hotel WHERE hotel_id=?";
						ResultSet rec = db_mail.execQuery(query,hotelid);
						rec.next();

						String admin_mail = rec.getString( "mail" );

						String text = dateedit.getDate(0) + " " + dateedit.getTime(0) + "\r\n";
						text = text + param_name + "���񂩂�̓��e" + "\r\n";
						text = text + param_subject + "\r\n";
						text = text + param_body + "\r\n";
						text = text + "\r\n";
						if (startDate < nowDate)
						{
							text = text + "�X�}�[�g�t�H������̕ԐM�͂������URL���火" + "\r\n";
							text = text + " http://www.hotenavi.com/" + hotelid + "/smart/bbsinput.jsp?msg_id=" + msgid + "\r\n";
							text = text + "PC����̕ԐM�͂������URL���火" + "\r\n";
							text = text + " http://www.hotenavi.com/" + hotelid + "/bbsreturn.jsp?msg_id=" + msgid + "\r\n";
							text = text + "\r\n";
						}
						else
						{
							text = text + "�ԐM�͂������URL���火" + "\r\n";
							text = text + "https://www.hotenavi.com/" + hotelid + "/bbs/return/" + msgid + "\r\n";
							text = text + "\r\n";
						}
 					    text = text + "�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|\r\n";
						text = text + "�����̃��[���ɕԐM���Ă����e����܂���̂ł����ӂ��������B\r\n";
						text = text + "�����̃��[���͓��e���Ɏ����I�ɔz�M����Ă��܂��B\r\n";
						text = text + "�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|�|\r\n";

						SendMail sendmail = new SendMail();
						sendmail.send("webmaster@hotenavi.com", admin_mail, mailsubject, text);

						db_msgid.close();
						db_mail.close();
				}
%>


<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr valign="top">
		<td>


			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td align="left" valign="middle" class="titlebar"><img src="<%=imageTitleBbs%>" width="140" height="14" alt="�f����"></td>
				</tr>

				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="20"></td>
				</tr>

				<tr>
					<td class="honbun">

<%
				if( result != 0 )
				{
%>
												���e���������܂����B
<%
								}
								else
								{
%>
												���e�Ɏ��s���܂����B
<%
								}
%>

<%
			db.close();
		}
		catch( Exception e )
		{
%>
<%= e.toString() %>
<%
		}
%>
					</td>
				</tr>

				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="8"></td>
				</tr>

				<tr>
					<td><a href="javascript:;" onMouseOver="MM_swapImage('yajirushi','','image/yajirushi.gif',1)" onMouseOut="MM_swapImgRestore()"><img src="<%=imageYajirushi%>" name="yajirushi" width="15" height="13" border="0" id="yajirushi"></a><img src="<%=imageSpacer%>" width="10" height="12" align="absbottom"><A href="bbs.jsp?HotelId=<%= hotelid %>" onmouseover="MM_swapImage('yajirushi','','image/yajirushi.gif',1)" onmouseout="MM_swapImgRestore()" class="link1">�f���ɖ߂�</A></td>
				</tr>

				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="50"></td>
				</tr>
			</table>
		</td>
	</tr>

	<tr valign="bottom">
		<td>
			<!-- copyright �e�[�u�� -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" valign="middle">
						<div class="copyright">
							Copy right &copy; imedia inc, All Rights Reserved
						</div>
					</td>
				</tr>
				<tr>
					<td><img src="<%=imageSpacer%>" width="500" height="20"></td>
				</tr>
			</table>

		</td>
	</tr>
</table>

</body>
</html>
