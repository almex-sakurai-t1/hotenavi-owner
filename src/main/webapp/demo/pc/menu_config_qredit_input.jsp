<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    
    String hotelid = request.getParameter("HotelId");
    
    if  (hotelid.compareTo("all") == 0)
    {
        hotelid = request.getParameter("HotelId");
    }
    
    int data_type = 15;
    String id = request.getParameter("Id");
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    String query = "";
    int result = 0;
        // ���t�ް��̕ҏW
    int start_ymd = 0;
    int end_ymd = 0;
    
    int start_yy = Integer.parseInt(request.getParameter("col_start_yy"));
    int start_mm = Integer.parseInt(request.getParameter("col_start_mm"));
    int start_dd = Integer.parseInt(request.getParameter("col_start_dd"));
    start_ymd = start_yy*10000 + start_mm*100 + start_dd;
    
    int end_yy = Integer.parseInt(request.getParameter("col_end_yy"));
    int end_mm = Integer.parseInt(request.getParameter("col_end_mm"));
    int end_dd = Integer.parseInt(request.getParameter("col_end_dd"));
    end_ymd = end_yy*10000 + end_mm*100 + end_dd;
    
    String    col_title = request.getParameter("col_title");
    String    col_msg1 = request.getParameter("col_msg1");
    String    col_decoration = request.getParameter("col_decoration");
    
    try
    {
        connection      = DBConnection.getConnection();
        if( id.compareTo("0") == 0 )
        {
            query = "INSERT INTO hotenavi.menu_config SET ";
            query = query + "hotelid = ?, ";
            query = query + "id=1, ";
            query = query + "data_type = ?, ";
            query = query + "add_hotelid = ?, ";
            query = query + "add_date = ?, ";
            query = query + "add_time = ?, ";
            query = query + "add_userid = ?, ";
        }
        else
        {
            query = "UPDATE hotenavi.menu_config SET ";
        }
        query = query + "disp_idx = ?,";
        query = query + "disp_flg =1, ";
        query = query + "start_date = ?, ";
        query = query + "end_date = ?, ";
        query = query + "title = ?, ";
        query = query + "msg = ?, ";
        query = query + "decoration = ?, ";
        query = query + "upd_hotelid = ?, ";
        query = query + "upd_userid = ?, ";
        query = query + "last_update = ?, ";
        query = query + "last_uptime = ?";
        if( id.compareTo("0") != 0 )
        {
            query = query + " WHERE hotelid = ? AND data_type = ? AND id= ?";
        }

        prestate    = connection.prepareStatement(query);
        if( id.compareTo("0") == 0 )
        {
            prestate.setString(1,hotelid);
            prestate.setInt(2,data_type);
            prestate.setString(3,loginhotel);
            prestate.setInt(4,nowdate);
            prestate.setInt(5,nowtime);
            prestate.setInt(6,ownerinfo.DbUserId);
            prestate.setInt(7,Integer.parseInt(request.getParameter("disp_idx")));
            prestate.setInt(8,start_ymd);
            prestate.setInt(9,end_ymd);
            prestate.setString(10,col_title);
            prestate.setString(11,col_msg1);
            prestate.setString(12,col_decoration);
            prestate.setString(13,loginhotel);
            prestate.setInt(14,ownerinfo.DbUserId);
            prestate.setInt(15,nowdate);
            prestate.setInt(16,nowtime);
        }
        else
        {
            prestate.setInt(1,Integer.parseInt(request.getParameter("disp_idx")));
            prestate.setInt(2,start_ymd);
            prestate.setInt(3,end_ymd);
            prestate.setString(4,col_title);
            prestate.setString(5,col_msg1);
            prestate.setString(6,col_decoration);
            prestate.setString(7,loginhotel);
            prestate.setInt(8,ownerinfo.DbUserId);
            prestate.setInt(9,nowdate);
            prestate.setInt(10,nowtime);
            prestate.setString(11,hotelid);
            prestate.setInt(12,data_type);
            prestate.setInt(13,Integer.parseInt(id));
        }
        // SQL�N�G���[�̎��s
        result = prestate.executeUpdate();
        DBSync.publish(prestate.toString().split(":",2)[1]);
        DBSync.publish(prestate.toString().split(":",2)[1],true);
    }
    catch( Exception e )
    {
        Logging.error("menu_config_qredit_input.jsp Exception e=" + e.toString() );
%>
loginhotel:<%=loginhotel%><br>
Exception e=<%= e.toString() %>
<%
    }
    finally
    {
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(connection);
    }
%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�V�z�e�i�r�Ή��@PC�t���[�G���A��</title>
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
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">�o�^�m�F</font></td>
              <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
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
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="/common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if( result != 0 )
    {
%>
�o�^���܂����B<br>
<%
    }
    else
    {
%>
�o�^�Ɏ��s���܂����B
<%
    }
%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12">&nbsp;</td>
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
              <td><img src="/common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
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
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="/common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; imedia
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
