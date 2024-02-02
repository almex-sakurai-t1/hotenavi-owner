<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %><%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %><%@ page import="com.hotenavi2.mailmagazine.*" %>
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
    int id = 0;
    int ret = -1;
    int historyid = 0;
    int error_flg = 1;
    String error_message = "";
    String query;
    DbAccess db;
    ResultSet address_list;

    // �Z�b�V���������ɃZ�b�g����Ă���f�[�^���擾
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid.compareTo("all") == 0 )
    {
        hotelid = (String)session.getAttribute("HotelId");
    }

    String param_historyid = ReplaceString.getParameter(request,"history");
    List<Object> list = new ArrayList<Object>();

    if( param_historyid != null )
    {
        db =  new DbAccess();
	      query = "SELECT * FROM mag_data WHERE hotel_id=?";
	      query = query + " AND history_id=?";
        list = new ArrayList<Object>();
        list.add(hotelid);
        list.add(param_historyid);
	      ResultSet result = db.execQuery(query,list);
	      if( result.next() != false )
	      {
  	      if( result.getInt("state") == 0 )
		      {
		        error_flg = 0;
		      }
	      }
        db.close();
        if (error_flg == 0)
        {
          LogicMagazineSend logicMagazineSend = new LogicMagazineSend();
          if (!logicMagazineSend.delete(hotelid,Integer.parseInt(param_historyid)))
          {
            error_message = logicMagazineSend.getErrorMessage();
            error_flg = 1;
          }
        }

	      if (error_flg == 0)
	      {
 	        db =  new DbAccess();
	        query = "UPDATE mag_data SET ";
	        query = query + "state=2 ";
	        query = query + ",recipient_id='' ";
	        query = query + ",mail_id=''";
	        query = query + " WHERE hotel_id=?";
	        query = query + " AND history_id=?";

	        ret = db.execUpdate(query,list);
	        db.close();

	        db =  new DbAccess();

	        query = "UPDATE mag_data_list SET ";
	        query = query + "send_flag=2 ";
	        query = query + " WHERE hotel_id=?";
	        query = query + " AND history_id=?";

	        ret = db.execUpdate(query,list);
	        db.close();
	      }
    }

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�����}�K�쐬</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
      </tr>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">�����}�K�쐬</font></td>
              <td width="15" height="20"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
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
        <td align="center" valign="top" bgcolor="#FFF5EE"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td class="size12"><font color="#CC0000"><strong>
<%
	if (error_flg == 0)
	{
%>
					���[���}�K�W���f�[�^���폜���܂����B<br>
<%
	}
else
	{
%>
					���[���}�K�W���f�[�^�̍폜�����s���܂����B<br>
					<%=error_message%>
<%
	}
%>

              </strong></font></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
              <tr>
                <td valign="top">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="200" height="14"></td>
                      </tr>
                  </table>
                </td>
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
      <!-- �����܂� -->
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
