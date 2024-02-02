<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />


<%
    // �z�e��ID�擾
    String hotelid = (String)session.getAttribute("SelectHotel");
    String data_type = request.getParameter("DataType");
    String query;
    int    id;
    DbAccess db = new DbAccess();

    String param_cnt = request.getParameter("cnt");
    if( param_cnt == null )
    {
        param_cnt = "0";
    }
    int cnt = Integer.parseInt(param_cnt);

    query = "SELECT * FROM menu WHERE hotelid='" + hotelid + "'";
    query = query + " AND data_type=" + data_type;
    query = query + " ORDER BY id DESC";

    // SQL�N�G���[�̎��s
    ResultSet result = db.execQuery(query);
    if( result != null )
    {
        result.relative(cnt);

        if( result.next() != false )
        {
            java.sql.Date date;

            date = result.getDate("start_date");
            int start_yy = date.getYear();
            int start_mm = date.getMonth();
            int start_dd = date.getDate();

            date = result.getDate("end_date");
            int end_yy = date.getYear();
            int end_mm = date.getMonth();
            int end_dd = date.getDate();
%>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="100" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">�ҁ@�W</font></td>
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
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="8"><img src="/common/pc/image/spacer.gif" width="100%" height="10"></td>
        </tr>
        <tr>
          <td width="8"><img src="/common/pc/image/spacer.gif" width="100%" height="5"></td>
<%
    if( cnt - 1 >= 0 )
    {
%>
          <td><div align="left"><a href="menu_edit.jsp?DataType=<%= data_type %>&cnt=<%= cnt - 1 %>">�O�̃g�s�b�N��</a><div></td>
<%
    }
%>
<%
    if( result.next() != false )
    {
%>
          <td><div align="right"><a href="menu_edit.jsp?DataType=<%= data_type %>&cnt=<%= cnt + 1 %>">���̃g�s�b�N��</a><div></td>
<%
    }

    // ���R�[�h��߂�
    result.previous();
%>
          <td width="8"><img src="/common/pc/image/spacer.gif" width="100%" height="5"></td>
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
                <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
                <td>

  <table width="100%" border="0" cellspacing="0" cellpadding="2">
    <tr align="left">
      <td>

<%
        if( result.getInt("disp_flg") == 1 )
        {
%>
      �\�����܂�<br>
<%
        }
        else
        {
%>
      �\�����Ȃ�<br>
<%
        }
%>

  �\���ʒu�F <%= result.getInt("disp_idx") %>
      </td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>


    <tr align="left">
      <td height="24" bgcolor="#666666"><strong>
        &nbsp;<font color="#FFFFFF">�\������</font></strong>
      </td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td>
        <%= start_yy+1900 %>�N<%= start_mm+1 %>��<%= start_dd %>���`<%= end_yy+1900 %>�N<%= end_mm+1 %>��<%= end_dd %>��
      </td>
    </tr>

    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>

    <tr align="left">
      <td height="24" bgcolor="#666666"><strong>&nbsp;<font color="#FFFFFF">�^�C�g��</font></strong></td>
    </tr>
    <tr align="left">
      <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td><strong>&nbsp;�����F�F</strong><%= result.getString("title_color") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td><strong>&nbsp;�^�C�g���F</strong><%= result.getString("title") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td><strong>&nbsp;�R���e���c�F</strong><%= result.getString("contents") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="14"></td>
    </tr>
    <tr align="left">
      <td height="35" valign="bottom" class="size12">                                <div align="center">
    <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" bgcolor="#969EAD">
            <tr>
              <td align="right" nowrap class="size12">���̃��j���[��<img src="/common/pc/image/spacer.gif" width="20" height="10"></td>
              <td valign="bottom"><form action="menu_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>" method=POST>
              <img src="/common/pc/image/spacer.gif" width="100" height="10"><br>                                        
              <input name="submit01" type="submit" value="�ҏW����" class="size12">
              </form></td>
            </tr>
        </table>
        </div></td>
    </tr>
  </table>


                </td>
              </tr>
            </table>
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
</table>

<%
    // 1�g�s�b�N�I��
        }
    }

    db.close();
%>
