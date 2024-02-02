<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // ÉZÉbÉVÉáÉìÇÃämîF
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
    int    i;
    int    ret;
    int    count;
    String query;
    ResultSet result_hotel;
    ResultSet result_admin;
    ResultSet result_count;
    DbAccess db;
    DbAccess db_update;
    DbAccess db_store;

    // owner_userÉfÅ[É^ÉxÅ[ÉXÇÃìoò^
    String hotelid = (String)session.getAttribute("LoginHotelId");
    String userid = ReplaceString.getParameter(request,"userid");
    String loginid = ReplaceString.getParameter(request,"loginid");
    String name = ReplaceString.getParameter(request,"name");
    String passwd_pc = ReplaceString.getParameter(request,"passwd_pc");
    String passwd_mobile = ReplaceString.getParameter(request,"passwd_mobile");
    String mailaddr_pc = ReplaceString.getParameter(request,"mailaddr_pc");
    String mailaddr_mobile = ReplaceString.getParameter(request,"mailaddr_mobile");
    if (loginid!=null)
    {
        if( !CheckString.loginIdCheck(loginid) )
        {
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
        }
    }

    db_update =  new DbAccess();

    if( userid.compareTo("0") == 0 )
    {
        query = "INSERT INTO owner_user ( hotelid, userid, loginid, machineid, name, passwd_pc, passwd_mobile, mailaddr_pc, mailaddr_mobile, level ";
        query = query + ") VALUES ( ";
        query = query + "'" + hotelid + "',";
        query = query + "null" + ",";
        query = query + "'" + ReplaceString.SQLEscape(loginid) + "',";
        query = query + "'" + ReplaceString.SQLEscape(name) + "',";
        query = query + "'" + ReplaceString.SQLEscape(passwd_pc) + "',";
        query = query + "'" + ReplaceString.SQLEscape(passwd_mobile) + "',";
        query = query + "'" + ReplaceString.SQLEscape(mailaddr_pc) + "',";
        query = query + "'" + ReplaceString.SQLEscape(mailaddr_mobile) + "',";
        query = query + "1";
        query = query + " )";
    }
    else
    {
        query = "UPDATE owner_user SET ";
        query = query + "hotelid='" + hotelid + "', ";
        query = query + "loginid='" + ReplaceString.SQLEscape(loginid) + "', ";
        query = query + "name='" + ReplaceString.SQLEscape(name) + "', ";
        query = query + "passwd_pc='" + ReplaceString.SQLEscape(passwd_pc) + "', ";
        query = query + "passwd_mobile='" + ReplaceString.SQLEscape(passwd_mobile) + "', ";
        query = query + "mailaddr_pc='" + ReplaceString.SQLEscape(mailaddr_pc) + "', ";
        query = query + "mailaddr_mobile='" + ReplaceString.SQLEscape(mailaddr_mobile) + "', ";
        query = query + "level=" + "1" + " ";
        query = query + " WHERE hotelid='" + hotelid + "' AND userid=?";
    }
    if( userid.compareTo("0") == 0 )
    {
    ret = db_update.execUpdate(query);
    }
    else
    {
    ret = db_update.execUpdate(query,userid);
    }
    db_update.close();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>ÉÜÅ[ÉUä«óù</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">ìoò^ämîF</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3">&nbsp;</td>
      </tr>
      <!-- Ç±Ç±Ç©ÇÁï\ -->
      <tr>
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if( ret != 0 )
    {
%>
ìoò^ÇµÇ‹ÇµÇΩÅB<br>
<%
    }
    else
    {
%>
ìoò^Ç…é∏îsÇµÇ‹ÇµÇΩÅB
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="size12">
                    <form action="../../common/pc/user_form_self.jsp?UserId=<%= userid %>" method="POST">
                      <input name="submit_ret" type="submit" value="ñﬂÇÈ" >
                    </form></td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
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
      <!-- Ç±Ç±Ç‹Ç≈ -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
