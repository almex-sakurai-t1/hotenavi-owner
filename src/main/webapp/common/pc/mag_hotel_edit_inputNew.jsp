<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="java.sql.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/mag_edit_ini.jsp" %><%@ include file="../csrf/refererCheck.jsp" %><%@ include file="../csrf/checkCsrfForPost.jsp" %><%@ include file="../../common/pc/owner_ini.jsp" %>
<%
    // ÉZÉbÉVÉáÉìÇÃämîF
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int enddate = 29991231;
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    // ÉzÉeÉãIDéÊìæ
    String hotelid = (String)session.getAttribute("SelectHotel");
    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    int  ret = 0;
    try
    {
        query = "UPDATE mag_hotel SET";
        query = query + " address_mailto = ?";
        query = query + ",reply_to_address = ?";
        query = query + ",unsubscribe_url = ? ";
        query = query + " WHERE hotel_id = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,request.getParameter("address_mailto"));
        prestate.setString(2,request.getParameter("reply_to_address"));
        prestate.setString(3,request.getParameter("unsubscribe_url"));
        prestate.setString(4,hotelid );

        ret         = prestate.executeUpdate();
    }
    catch( Exception e )
    {
      %>
<%= e.toString() %>
      <%
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
%>
<%
    
    String[]  msg             = new String[8];
    for( int i = 0 ; i < 8 ; i++ )
    {
        msg[i] = ReplaceString.getParameter(request,"col_msg"+(i+1));
        if (Decoration[0][i] == 1)
        {
            msg[i] = msg[i].replace("<br>","<br/>");
            msg[i] = msg[i].replace("<BR>","<br/>");
            msg[i] = msg[i].replace("</strong>\r\n","</strong><br><br/>");
            msg[i] = msg[i].replace("</a>\r\n","</a><br><br/>");
            msg[i] = msg[i].replace("</font>\r\n","</font><br><br/>");
            msg[i] = msg[i].replace("</em>\r\n","</em><br><br/>");
            msg[i] = msg[i].replace("</u>\r\n","</u><br><br/>");
            msg[i] = msg[i].replace(">\r\n","><br/>");
            msg[i] = msg[i].replace("\r\n","<br/><br/>");
            msg[i] = msg[i].replace("><br/>",">\r\n");
        }
    }

    boolean    magazine_flag      = false;
    int  data_type = 62;
    try
    {
        query = "SELECT * FROM edit_event_info WHERE hotelid = ?";
        query = query + " AND data_type= ?";
        query = query + " AND id=1";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        prestate.setInt(2,data_type);
        result      = prestate.executeQuery();
        if (result != null)
        {
            if( result.next() != false )
            {
                magazine_flag = true;
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
    try
    {
        if(magazine_flag)
        {
            query = "UPDATE edit_event_info SET ";
            query = query + "disp_idx=0";
          }
        else
        {
            query = "INSERT INTO edit_event_info SET ";
            query = query + "hotelid = ?";
            query = query + ",data_type= ?";
            query = query + ",id=1 ";
            query = query + ",disp_idx=0";
          }
        query = query + ",disp_flg=1";
        query = query + ",start_date = " + nowdate;
        query = query + ",end_date = "   + enddate;
        query = query + ",msg1 = ?";
        query = query + ",msg2 = ?";
        query = query + ",msg3 = ?";
        query = query + ",msg4 = ?";
        query = query + ",msg5 = ?";
        query = query + ",msg6 = ?";
        query = query + ",msg7 = ?";
        query = query + ",msg8 = ?";
        query = query + ",upd_hotelid = ?";
        query = query + ",upd_userid = ?";
        query = query + ",last_update = " + nowdate;
        query = query + ",last_uptime = " + nowtime;
        if(magazine_flag)
        {
           query = query + " WHERE hotelid = ? AND data_type = ? AND id=1";
        }
        prestate    = connection.prepareStatement(query);
        if(magazine_flag)
        {
          prestate.setString(1,msg[0].replace("<br>","\r\n"));
          prestate.setString(2,msg[1]);
          prestate.setString(3,msg[2]);
          prestate.setString(4,msg[3]);
          prestate.setString(5,msg[4]);
          prestate.setString(6,msg[5]);
          prestate.setString(7,msg[6]);
          prestate.setString(8,msg[7]);
          prestate.setString(9,loginhotel);
          prestate.setInt(10,ownerinfo.DbUserId);
          prestate.setString(11,hotelid);
          prestate.setInt(12,data_type);
        }
        else
        {
          prestate.setString(1,hotelid);
          prestate.setInt(2,data_type);
          prestate.setString(3,msg[0].replace("<br>","\r\n"));
          prestate.setString(4,msg[1]);
          prestate.setString(5,msg[2]);
          prestate.setString(6,msg[3]);
          prestate.setString(7,msg[4]);
          prestate.setString(8,msg[5]);
          prestate.setString(9,msg[6]);
          prestate.setString(10,msg[7]);
          prestate.setString(11,loginhotel);
          prestate.setInt(12,ownerinfo.DbUserId);
        }
        ret         = prestate.executeUpdate();
    }
    catch( Exception e )
    {
      %>
<%= e.toString() %>
      <%
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>ÉÅÉãÉ}ÉKä«óù</title>
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
    if( ret == 1 )
    {
%>
ìoò^ÇµÇ‹ÇµÇΩÅB<br>

<%
    }
    else
    {
%>
<script> alert("ìoò^Ç…é∏îsÇµÇ‹ÇµÇΩÅB")</script>
<%
    }

%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="mag_hotel_edit.jsp?HotelId=<%= hotelid %>" method="POST">
                      <INPUT name="submit_ret" type=submit value=ñﬂÇÈ >
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
