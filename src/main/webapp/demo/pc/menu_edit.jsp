<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
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
    // �z�e��ID�擾
    String hotelid = (String)session.getAttribute("SelectHotel");
    String data_type = request.getParameter("DataType");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>PC�ŃC�x���g���@�ҏW</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
<script type="text/javascript" src="/common/pc/scripts/coupon.js"></script>

</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">�V�K�쐬</font></td>
              <td width="15" height="20"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- ��������\ -->
      <tr>
        <td valign="top" bgcolor="#FFFFFF">
          <table width="100%" height="20" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="/common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
            <tr>
              <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td>              <div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <form action="menu_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=0" method=POST>
                        <strong>�V�������j���[��ǉ�����</strong>�@
                        <input name="submit00" type=submit value="�ǉ�" >
                      </form>
                    </td>
                    <td width="20" align="right"><img src="/common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                </table>
              </div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="/common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="10"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="/common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- �����܂� -->
    </table>
  </td>
  </tr>
  <tr>
    <td valign="top">

      <jsp:include page="menu_edit_nowtopic.jsp" flush="true" />

    </td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18">
    </td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><img src="/common/pc/image/imedia.gif" width="63" height="18"><img src="/common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; imedia
    inc . All Rights Reserved.</td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
