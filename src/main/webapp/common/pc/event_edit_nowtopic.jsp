<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />


<%
    // �z�e��ID�擾
    String hotelid = (String)session.getAttribute("SelectHotel");
    String data_type = ReplaceString.getParameter(request,"DataType");
    String query;
    int    id;
    DbAccess db =  new DbAccess();

    String param_cnt = ReplaceString.getParameter(request,"cnt");
    if( param_cnt == null )
    {
        param_cnt = "0";
    }
    if(!CheckString.numCheck(param_cnt))
	{
	param_cnt ="0";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </script>
<%
	}
    int cnt = Integer.parseInt(param_cnt);

    query = "SELECT * FROM edit_event_info WHERE hotelid=?";
    query = query + " AND data_type=?";
    query = query + " ORDER BY id DESC";
    List<Object> list = new ArrayList<Object>();
	//�p�����[�^�ǉ�
	list.add(hotelid);
	list.add(Integer.parseInt(data_type));
    // SQL�N�G���[�̎��s
    ResultSet result = db.execQuery(query,list);
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
          <td><div align="left"><a href="event_edit.jsp?DataType=<%= data_type %>&cnt=<%= cnt - 1 %>">�O�̃g�s�b�N��</a><div></td>
<%
    }
%>
<%
    if( result.next() != false )
    {
%>
          <td><div align="right"><a href="event_edit.jsp?DataType=<%= data_type %>&cnt=<%= cnt + 1 %>">���̃g�s�b�N��</a><div></td>
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
        if( result.getInt("member_only") == 1 )
        {
%>
      �����o�[��p<br>
<%
        }
        else
        {
%>
      <br>
<%
        }
%>

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
      <td><strong><font color="#660000" size="4">���g�s�b�N��</font></strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="12"></td>
    </tr>
    <tr align="left">
      <td height="24" bgcolor="#666666"><strong>&nbsp;<font color="#FFFFFF">�匩�o��</font></strong></td>
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
      <td><strong>&nbsp;�匩�o���F</strong><%= result.getString("title") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="14"></td>
    </tr>
    <tr align="left">
      <td height="24">

<%
        if( result.getString("msg1_title").length() != 0 )
        {
%>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="24" bgcolor="#dddddd"><strong>&nbsp;�����o���i1�j</strong></td>
          </tr>
        </table>
      </td>
    </tr>
    <tr align="left">
      <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����F�F</strong><%= result.getString("msg1_title_color") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����o���F</strong><%= result.getString("msg1_title") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="14"></td>
    </tr>
    <tr align="left">
      <td height="24"><table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td bgcolor="#dddddd"><strong>&nbsp;�g�s�b�N�{��</strong></td>
        </tr>
      </table></td>
    </tr>
    <tr align="left">
      <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td><textarea name="textarea" cols=64 rows=10 readonly><%= result.getString("msg1") %></textarea></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="10" height="14">
<%
        }
%>
      </td>
    </tr>
    <tr align="left">
      <td height="24">

<%
        if( result.getString("msg2_title").length() != 0 )
        {
%>
        <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#dddddd"><strong>&nbsp;�����o���i2�j</strong></td>
          </tr>
        </table>
        <strong></strong>
      </td>
    </tr>
    <tr align="left">
      <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����F�F</strong><%= result.getString("msg2_title_color") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����o���F</strong><%= result.getString("msg2_title") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="14"></td>
    </tr>
    <tr align="left">
      <td height="24"><table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td bgcolor="#dddddd"><strong>&nbsp;�g�s�b�N�{��</strong></td>
        </tr>
      </table></td>
    </tr>
    <tr align="left">
      <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td><textarea name="textarea2" cols=64 rows=10 readonly><%= result.getString("msg2") %></textarea></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="10" height="12">
<%
        }
%>
      </td>
    </tr>
    <tr align="left">
      <td height="24">

<%
        if( result.getString("msg3_title").length() != 0 )
        {
%>
        <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#dddddd"><strong>&nbsp;�����o���i3�j</strong></td>
          </tr>
        </table>
      <strong>                              </strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����F�F</strong><%= result.getString("msg3_title_color") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����o���F</strong><%= result.getString("msg3_title") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="10"></td>
    </tr>
    <tr align="left">
      <td height="24"><table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td bgcolor="#dddddd"><strong>&nbsp;�g�s�b�N�{��</strong></td>
        </tr>
      </table></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td><textarea name="textarea3" cols=64 rows=10 readonly><%= result.getString("msg3") %></textarea></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="12">
<%
        }
%>
      </td>
    </tr>
    <tr align="left">
      <td height="24">

<%
        if( result.getString("msg4_title").length() != 0 )
        {
%>

        <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#dddddd"><strong> &nbsp;�����o���i4�j</strong></td>
          </tr>
        </table>
      <strong>                              </strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;�����F�F<%= result.getString("msg4_title_color") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;�����o���F<%= result.getString("msg4_title") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="10"></td>
    </tr>
    <tr align="left">
      <td height="24" bgcolor="#dddddd"><strong>&nbsp;�g�s�b�N�{��</strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td><textarea name="textarea4" cols=64 rows=10 readonly><%= result.getString("msg4") %></textarea></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="10">

<%
        }
%>
      </td>
    </tr>
    <tr align="left">
      <td height="24">

<%
        if( result.getString("msg5_title").length() != 0 )
        {
%>
        <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#dddddd"><strong>&nbsp;�����o���i5�j</strong></td>
          </tr>
        </table>
      <strong>                              </strong></td>
    </tr>
    <tr align="left">
      <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����F�F<%= result.getString("msg5_title_color") %></strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����o���F<%= result.getString("msg5_title") %></strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="14"></td>
    </tr>
    <tr align="left">
      <td height="24"><table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td bgcolor="#dddddd"><strong>&nbsp;�g�s�b�N�{���i5�j�F</strong></td>
        </tr>
      </table></td>
    </tr>
    <tr align="left">
      <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td><textarea name="textarea5" cols=64 rows=10 readonly><%= result.getString("msg5") %></textarea></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="14">

<%
        }
%>
      </td>
    </tr>
    <tr align="left">
      <td height="24">

<%
        if( result.getString("msg6_title").length() != 0 )
        {
%>

        <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#dddddd"><strong>&nbsp;�����o���i6�j</strong></td>
          </tr>
        </table>
      <strong>                              </strong></td>
    </tr>
    <tr align="left">
      <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����F�F<%= result.getString("msg6_title_color") %></strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����o���F<%= result.getString("msg6_title") %></strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="14"></td>
    </tr>
    <tr align="left">
      <td height="24" bgcolor="#dddddd"><strong>&nbsp;�g�s�b�N�{���i6�j</strong></td>
    </tr>
    <tr align="left">
      <td height="8"><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td><textarea name="textarea6" cols=64 rows=10 readonly><%= result.getString("msg6") %></textarea></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="12">

<%
        }
%>
      </td>
    </tr>
    <tr align="left">
      <td height="24">

<%
        if( result.getString("msg7_title").length() != 0 )
        {
%>

        <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#dddddd"><strong>&nbsp;�����o���i7�j</strong></td>
          </tr>
        </table>
      <strong>                                </strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����F�F<%= result.getString("msg7_title_color") %></strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;<strong>�����o���F<%= result.getString("msg7_title") %></strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="10"></td>
    </tr>
    <tr align="left">
      <td height="24"><table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td bgcolor="#dddddd"><strong>&nbsp;�g�s�b�N�{���i�V�j�F</strong></td>
        </tr>
      </table></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td><textarea name="textarea7" cols=64 rows=10 readonly><%= result.getString("msg7") %></textarea></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="12">

<%
        }
%>
      </td>
    </tr>
    <tr align="left">
      <td height="24"><strong>

<%
        if( result.getString("msg8_title").length() != 0 )
        {
%>
      </strong>
        <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#dddddd"><strong>&nbsp;�����o���i8�j</strong></td>
          </tr>
        </table>
      <strong>                                </strong></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;�����F�F<%= result.getString("msg8_title_color") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
    </tr>
    <tr align="left">
      <td>&nbsp;�����o���F<%= result.getString("msg8_title") %></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="10"></td>
    </tr>
    <tr align="left">
      <td height="24"><table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td bgcolor="#dddddd"><strong>&nbsp;�g�s�b�N�{���i8�j</strong></td>
        </tr>
      </table></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="1"></td>
    </tr>
    <tr align="left">
      <td><textarea name="textarea8" cols=64 rows=10 readonly><%= result.getString("msg8") %></textarea></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="10">

<%
        }
%>
        <br></td>
    </tr>
    <tr align="left">
      <td><img src="/common/pc/image/spacer.gif" width="200" height="10"></td>
    </tr>
    <tr align="left">
      <td height="35" valign="bottom" class="size12">                                <div align="center">
    <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" bgcolor="#969EAD">
            <tr>
              <td align="right" nowrap class="size12">���̃g�s�b�N��<img src="/common/pc/image/spacer.gif" width="20" height="10"></td>
              <td valign="bottom"><form action="event_edit_form.jsp?HotelId=<%= hotelid %>&DataType=<%= data_type %>&Id=<%= result.getInt("id") %>" method=POST>
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
