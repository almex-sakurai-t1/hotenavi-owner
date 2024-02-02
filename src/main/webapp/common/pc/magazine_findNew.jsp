<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelMailmagazine" %>
<%@ include file="../csrf/refererCheck.jsp" %><%@ include file="../../common/pc/owner_ini.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }

    String mag_address        = "";
    String hotel_name         = "";
    String hotelid            = "";
    String query              = "";
    String query_hotelid      = "";
    String sender_id          = "";

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String selecthotel  = (String)session.getAttribute("SelectHotel");
    if   (selecthotel.equals("all") || selecthotel.equals(""))
    {
           hotelid    = (String)session.getAttribute("HotelId");
           hotel_name = "�S�X����";
    }
    else
    {
           hotelid    = selecthotel;
    }
    HotelMailmagazine hotelMailmagazine = new HotelMailmagazine();

    int    data_send_date       = 0;
    int    data_send_time       = 0;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    int        imedia_user        = 0;
    int        level              = 0;
    int        store_count        = 0;
    String     group_id           = "";

     // imedia_user �̃`�F�b�N
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
    //  �J�X�^��or�G�N�X�g���X�ܐ��̃`�F�b�N
    if(imedia_user == 1 && level == 3)
    {
        try
        {
            connection  = DBConnection.getConnection();
            query = "SELECT * FROM search_hotel_find WHERE findstr8 =?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if  (result != null)
            {
                if( result.next() != false )
                {
                    group_id = result.getString("findstr7");
                }
            }
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
            if (group_id.compareTo("") == 0)
            {
                query = "SELECT count(DISTINCT hotel.hotel_id) FROM hotel,search_hotel_find WHERE search_hotel_find.findstr8=?";
            }
            else
            {
                query = "SELECT count(DISTINCT hotel.hotel_id) FROM hotel,search_hotel_find WHERE search_hotel_find.findstr7=?";
            }
            query = query + " AND hotel.hotel_id = search_hotel_find.findstr8";
            query = query + " AND (hotel.plan = 1 OR hotel.plan = 3 OR hotel.plan = 4)";
            prestate    = connection.prepareStatement(query);
            if (group_id.compareTo("") == 0)
            {
                prestate.setString(1, hotelid);
            }
            else
            {
                prestate.setString(1, group_id);
            }
            result      = prestate.executeQuery();
            if  (result != null)
            {
                if( result.next() != false )
                {
                    store_count   = result.getInt(1);
                }
            }
        }
        catch( Exception e )
        {
            Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
        }
        finally
        {
            DBConnection.releaseResources(result,prestate,connection);
        }
    }
    else
    {
     // �Ǘ��X�ܐ��̎擾
        if (hotelMailmagazine.getManageHotel(loginHotelId,ownerinfo.DbUserId))
        {
            store_count = hotelMailmagazine.getHotelCount();
            if (store_count == 1)
            {
                hotelid =  hotelMailmagazine.getHotelMailmagazine()[0].getHotelId();
                if (selecthotel.equals(""))
                {
                   selecthotel = hotelid;
                }
                hotel_name =  hotelMailmagazine.getHotelMailmagazine()[0].getName();
            }
        }
    }

    try
    {
        connection  = DBConnection.getConnection();
        query = "SELECT * FROM hotel,mag_hotel WHERE mag_hotel.hotel_id=?";
        query = query + " AND hotel.hotel_id = mag_hotel.hotel_id";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            hotel_name = result.getString("hotel.name");
            mag_address = result.getString("mag_hotel.address");
            sender_id = result.getString("mag_hotel.sender_id");
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
        query = "SELECT * FROM mag_data  WHERE hotel_id=?";
        query = query + " AND state = 1 ORDER BY send_date DESC,send_time DESC";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
           data_send_date = result.getInt("send_date");
           data_send_time = result.getInt("send_time");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

    if (hotelid.compareTo("demo") == 0 || hotelid.indexOf("happyhote") != -1 || hotelid.compareTo("service") == 0 || hotelid.compareTo("info") == 0)
    {
        store_count = 1;
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>�����}�K�쐬</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/<% if(store_count >= 1){%>magazine.js<%}else{%>magazine_offline.js<%}%>"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="disabledCheck();">
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
<noscript>
            <tr>
              <td>&nbsp;</td>
              <td class="size12"><font color="#CC0000"><strong>
				  <a href="/common/pc/set_javascript_on.html" target="_blank">�X�N���v�g�̐ݒ肪�L���ɂȂ��Ă��܂���̂ŁA����ɓ��삵�Ȃ��ꍇ������܂��B�X�N���v�g�̐ݒ��L���ɂ��Ă��������B</a>
					</strong></font></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
</noscript>
            <tr>
              <td>&nbsp;</td>
              <td class="size12"><font color="#CC0000"><strong>1.�z�M����Ώێ҂�I��ł�������</strong></font></td>
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
                <form action="magazine_find_result.jsp" method="post">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr valign="top">
                        <td width="8" align="left">&nbsp;</td>
                        <td align="left" class="size12">�z�M�����[���A�h���X�F
							<strong><% if (mag_address.compareTo("") == 0){%>�o�^����Ă��܂���B�K�v�ȏꍇ�̓A�����b�N�X�܂ŘA���肢�܂��B<%}else{%><%=mag_address%><%}%></strong>
							<% if(data_send_date != 0){%>�i�O��z�M�F<%=data_send_date/10000%>�N<%=data_send_date/100%100%>��<%=data_send_date%100%>���@<%=data_send_time/100%>:<%=data_send_time%100%>�j<%}%>
              <% if (sender_id.compareTo("") == 0){%>
                <div style="color:#CC0000;font-weight: bold;margin: 10px;">
                  ���[���}�K�W���ݒ肪�������Ă��܂���B
                  <br>�A�����b�N�X�܂Ŗ₢���킹�����肢���܂��B
                </div>
              <%}%>
            </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td width="8" align="left">&nbsp;</td>
                        <td align="left" class="size12">�o�^�ΏۓX�܁F</td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#FFFFFF" class="size12">&nbsp;
<%
    if(imedia_user == 1 && level == 3)
    {
        if (hotelid.compareTo("demo") == 0 || hotelid.indexOf("happyhote") != -1 || hotelid.compareTo("service") == 0 || hotelid.compareTo("info") == 0)
        {
%>
							<input type="checkbox" name="Store" value="<%=hotelid%>" checked><%=hotelid%><br>
<%
        }
        else
        {
            try
            {
                connection  = DBConnection.getConnection();
                if (group_id.compareTo("") == 0)
                {
                    query = "SELECT DISTINCT hotel.hotel_id,hotel.name FROM hotel,search_hotel_find WHERE search_hotel_find.findstr8=?";
                }
                else
                {
                    query = "SELECT DISTINCT hotel.hotel_id,hotel.name FROM hotel,search_hotel_find WHERE search_hotel_find.findstr7=?";
                }
                query = query + " AND  hotel.hotel_id = search_hotel_find.findstr8";
                query = query + " AND hotel.plan <= 4";
                query = query + " AND hotel.plan >= 1";
                query = query + " AND hotel.plan != 2";
                query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
                prestate    = connection.prepareStatement(query);
                if (group_id.compareTo("") == 0)
                {
                    prestate.setString(1, hotelid);
                }
                else
                {
                    prestate.setString(1, group_id);
                }
                result      = prestate.executeQuery();
                if  (result != null)
                {
                    while( result.next() != false )
                    {
%>							<input type="checkbox" name="Store" value="<%= result.getString("hotel.hotel_id") %>" <%if( hotelid.compareTo(result.getString("hotel.hotel_id")) == 0 || selecthotel.compareTo("all")==0){%> checked <%}%>><%= result.getString("hotel.name") %>&nbsp;
<%
                    }
                }
            }
            catch( Exception e )
            {
                Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
            }
            finally
            {
                DBConnection.releaseResources(result,prestate,connection);
            }
        }
    }
    else
    {
        if (!selecthotel.equals("all"))
        {
%>
			<input  type="hidden" name="Store" value="<%= selecthotel %>"><%=hotel_name%>
<%
        }
        else
        {
         // �Ǘ��X�ܐ��̎擾
            store_count = hotelMailmagazine.getHotelCount();
            for (int i = 0;  i  < store_count; i++)
            {
%>
						<input  type="checkbox" name="Store" value="<%=hotelMailmagazine.getHotelMailmagazine()[i].getHotelId()%>" <%if( selecthotel.equals(hotelMailmagazine.getHotelMailmagazine()[i].getHotelId()) || selecthotel.compareTo("all")==0){%> checked <%}%>><%= hotelMailmagazine.getHotelMailmagazine()[i].getName() %>&nbsp;
<%
            }
        }
    }
    if (store_count > 1)
    {
%>
	<br><br><br>�@�@<small>���z�e���̌ڋq�V�X�e���Ԃœ�������Ă���ꍇ�́A����̃��[���A�h���X�ɂ��ēX�܂ɂ��i�荞�݂��ł��܂���B�ڂ����͕��Ђ܂ł��⍇�����������B</small><br><br>
<%
    }
%>
                        </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left" class="size12"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
                      <tr valign="top">
                        <td width="8" align="left">&nbsp;</td>
                        <td align="left" class="size12">�z�M�����F</td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#FFFFFF" class="size12">&nbsp;

                          <%-- �����o�[�E�r�W�^�[�I�� --%>
                          <select name="Condition" id="Condition">
                              <option value="0">�S����
<%
    if (store_count >= 1)
    {
%>
                              <option value="1">�r�W�^�[�S��
                              <option value="2">�����o�[�S��
<%
     }
%>                              <option value="3">���L�̏����Ɉ�v����ꍇ
                          </select>
                        </td>
                      </tr>
<%
    if (store_count >= 1)
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#DDDDDD" class="size12">&nbsp;

                          <%-- ����R�[�h�I�� --%>
                          <jsp:include page="magazine_find_customidNew.jsp" />

                        </td>
                      </tr>
<%
    }
%>

                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#FFFFFF" class="size12">&nbsp;

                          <%-- ���[���A�h���X�I�� --%>
                          <jsp:include page="magazine_find_mailaddressNew.jsp" />

                        </td>
                      </tr>
<%
    if (store_count >= 1)
    {
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="26"></td>
                        <td align="left" valign="middle" bgcolor="#DDDDDD" class="size12">&nbsp;

                          <%-- �a�����I�� --%>
                          <jsp:include page="magazine_find_birthdayNew.jsp" />

                        </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#FFFFFF" class="size12">&nbsp;

                          <%-- �L�O���I�� --%>
                          <jsp:include page="magazine_find_memorialNew.jsp" />

                        </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#DDDDDD" class="size12">&nbsp;

                          <%-- �ŏI���p���I�� --%>
                          <jsp:include page="magazine_find_lastdayNew.jsp" />

                        </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#FFFFFF" class="size12">&nbsp;

                          <%-- ���p�񐔑I�� --%>
                          <jsp:include page="magazine_find_countNew.jsp" />

                        </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#DDDDDD" class="size12">&nbsp;

                          <%-- ���p���z�I�� --%>
                          <jsp:include page="magazine_find_totalNew.jsp" />

                        </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#FFFFFF" class="size12">&nbsp;

                          <%-- �|�C���g�I�� --%>
                          <jsp:include page="magazine_find_pointNew.jsp" />

                        </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#DDDDDD" class="size12">&nbsp;

                          <%-- �|�C���g2�I�� --%>
                          <jsp:include page="magazine_find_point2New.jsp" />

                        </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="28"></td>
                        <td align="left" valign="middle" bgcolor="#FFFFFF" class="size12">&nbsp;

                          <%-- �����N�I�� --%>
                          <jsp:include page="magazine_find_rankNew.jsp" />

                        </td>
                      </tr>
<%
    }
%>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="60"></td>
                        <td align="left" valign="middle" bgcolor="#DDDDDD" class="size12">&nbsp;

                          <%-- �ŏI�z�M�I�� --%>
                          <jsp:include page="magazine_find_lastsendNew.jsp" />

                        </td>
                      </tr>
                      <tr valign="top">
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                        <td align="left"><img src="../../common/pc/image/spacer.gif" width="8" height="8"></td>
                      </tr>
<% if (mag_address.compareTo("") != 0 && sender_id.compareTo("") !=0){%>
                      <tr valign="middle">
                        <td colspan="2" align="center">
                        <input name="Submit3" type="submit" value="�z�M�ґI��">
                        <img src="../../common/pc/image/spacer.gif" width="12" height="12">
                        <input type="reset" name="Submit2" value="�N���A"></td>
                      </tr>
<%}%>


</table>
                </form>
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
