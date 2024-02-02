<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
    String hotelid     = (String)session.getAttribute("SelectHotel");
    String param_year  = ReplaceString.getParameter(request,"Year");
    String param_month = ReplaceString.getParameter(request,"Month");
    if( !CheckString.numCheck(param_year) || !CheckString.numCheck(param_month) )
    {
        param_year = "0";
        param_month = "0";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    NumberFormat nf = new DecimalFormat("0000");
    String year  = nf.format(Integer.valueOf(param_year).intValue());

    nf = new DecimalFormat("00");
    String month = nf.format(Integer.valueOf(param_month).intValue());

    String loginhotel = (String)session.getAttribute("LoginHotelId");
    DbAccess db_sec =  new DbAccess();
    // �Z�L�����e�B���̎擾
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db_sec, loginhotel, ownerinfo.DbUserId);
    // �Z�L�����e�B�`�F�b�N
    if( DbUserSecurity == null )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�A�N�Z�X���|�[�g�_�E�����[�h</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/room.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/click_check.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="140" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF">�A�N�Z�X���|�[�g</font></td>
          <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td height="20">
            <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
          </td>
        </tr>
      </table>
    </td>
    <td width="3">&nbsp;</td>
  </tr>
  <!-- ��������\ -->
  <tr>
    <td align="center" valign="top" bgcolor="#D5D8CB"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="8" height="6"><img src="../../common/pc/image/spacer.gif" width="400" height="6"></td>
        </tr>
      </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td></td>
          </tr>
          <tr>
            <td valign="top"><table border="0" cellspacing="0" cellpadding="1">
                <tr valign="top">
                  <td width="4"><img src="../../common/pc/image/spacer.gif" width="4" height="10"></td>
                  <td height="30"><table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
                      <tr valign="middle">
                        <td align="center" bgcolor="#000066"><div class="size12"><font color="#FFFFFF"><%= year %>�N<%= month %>���@�A�N�Z�X���|�[�g</font></div>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="4" height="20"></td>
                  <td><table border="0" cellpadding="2" cellspacing="0" class="access">
                      <tr>
                        <td align="center" valign="middle" nowrap><div class="size12"><font class="space4">���[��I�����Ă���������</font> <font class="space4">&nbsp;</font></div>
                        </td>
                        <td align="center" valign="middle" class="size12" nowrap> 
                              <select name="menu1" onchange="MM_jumpMenu('parent.frames[\'mainFrame\']',this,1)" onmouseover="parent.mainFrame.location.href='page.html';">
                              <option selected>�I�����Ă�������</option>
                              <option value="../../common/pc/hpreport_getfile.jsp?HotelId=<%= hotelid %>&fname=<%= year %><%= month %>-<%= hotelid %>.xls#���ʃA�N�Z�X��!a1">1.���ʃA�N�Z�X��</option>
                              <option value="../../common/pc/hpreport_getfile.jsp?HotelId=<%= hotelid %>&fname=<%= year %><%= month %>-<%= hotelid %>.xls#�j���ʃA�N�Z�X��!a1">2.�j���ʃA�N�Z�X��</option>
                              <option value="../../common/pc/hpreport_getfile.jsp?HotelId=<%= hotelid %>&fname=<%= year %><%= month %>-<%= hotelid %>.xls#���ԑѕʃA�N�Z�X��!a1">3.���ԑѕʃA�N�Z�X��</option>
                              <option value="../../common/pc/hpreport_getfile.jsp?HotelId=<%= hotelid %>&fname=<%= year %><%= month %>-<%= hotelid %>.xls#�L�����A�ʃA�N�Z�X��!a1">4.�L�����A�ʃA�N�Z�X��</option>
                              <option value="../../common/pc/hpreport_getfile.jsp?HotelId=<%= hotelid %>&fname=<%= year %><%= month %>-<%= hotelid %>.xls#�r�W�^�[���j���[!a1">5.�r�W�^�[���j���[</option>
                              <option value="../../common/pc/hpreport_getfile.jsp?HotelId=<%= hotelid %>&fname=<%= year %><%= month %>-<%= hotelid %>.xls#�����o�[���j���[!a1">6.�����o�[���j���[</option>
                              </select>
                        </td>
<%
if( DbUserSecurity.getInt("sec_level09") == 1)
{
%>
                        <td align="center" valign="middle" nowrap class="size12"><a href="../../common/pc/hpreport_getfile.jsp?HotelId=<%= hotelid %>&fname=<%= year %><%= month %>-<%= hotelid %>.xls#���ʃA�N�Z�X��!a1" target="mainFrame" ><img src="../../common/pc/image/sales_d.gif" alt="���[���_�E�����[�h" width="130" height="20" border="0" align="absmiddle"></a>&nbsp;<small>�i�_�E�����[�h����ꍇ�͉E�N���b�N���Ώۃt�@�C����ۑ��j</small></td>
<%
}
%>
                      </tr>
                    </table>
                  </td>
                  	<td>
					<table border="0" cellpadding="2" cellspacing="0" class="access">
                      <tr>
                        <td align="center" valign="middle" class="size10" nowrap> 
						<input type="button" value="PRINT" onClick="parent.mainFrame.focus();parent.mainFrame.print();"> ��������̓y�[�W�ݒ��A4���ɂ��Ă��������B�i�S�V�[�g�o�́j<br>
						</td>
					</tr>
					</table>
                </tr>
              </table>
                <img src="../../common/pc/image/spacer.gif" width="8" height="1"></td>
          </tr>
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="4"></td>
          </tr>
        </table>
    </td>
    <td width="3" valign="top" align="left" height="100%">
      <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
        </tr>
        <tr>
          <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="20"></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="3" bgcolor="#999999">
      <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
          <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
        </tr>
      </table>
    </td>
    <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
  </tr>
  <!-- �����܂� -->
</table>
</body>
</html>
