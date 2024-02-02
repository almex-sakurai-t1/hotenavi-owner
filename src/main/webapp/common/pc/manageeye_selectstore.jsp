<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int          count;
    boolean      ret;
    String       query;
    String       hotelname = "";
    String       storecount;
    String       selecthotel;
    String       param_store;
    ResultSet    result;

    selecthotel = (String)session.getAttribute("SelectHotel");
    if( selecthotel == null )
    {
        selecthotel = "";
    }

    param_store = ReplaceString.getParameter(request,"Store");
    if( param_store != null )
    {
        selecthotel = param_store;
    }

    // �Z�b�V���������ɑI���z�e�����Z�b�g����
    session.setAttribute("SelectHotel", selecthotel);

    // �Ǘ��X�ܐ��̎擾
    storecount = (String)session.getAttribute("StoreCount");
    if( storecount != null )
    {
        count = Integer.valueOf(storecount).intValue();
    }
    else
    {
        count = 0;
    }

    // 2�X�܈ȏ�̏ꍇ�\������
    if( count > 1 )
    {
%>

<td width="80" align="center" nowrap bgcolor="#000000">
  <div class="white12" align="center">�X�ܑI��</div>
</td>
<form action="manageeye_main.jsp" method="get" name="selectstore" target="mainFrame">
  <td width="100%" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <select name="Store" onChange="document.selectstore.elements['submitstore'].click()">

<%
        if( selecthotel.compareTo("") == 0 || selecthotel.compareTo("all") == 0)
        {
%>
      <option value="" selected>�I�����Ă�������</option>
<%
        }
%>
<%
        DbAccess db_manage =  new DbAccess();

        // �Ǘ��X�ܐ������[�v
        ResultSet DbManageHotel = ownerinfo.getManageHotel(db_manage, ownerinfo.HotelId, ownerinfo.DbUserId);
        ret = DbManageHotel.first();
        while( ret != false )
        {
            DbAccess db =  new DbAccess();

            // �z�e�����̂̎擾
             query = "SELECT name FROM hotel WHERE hotel_id=?";
            result = db.execQuery(query,DbManageHotel.getString("accept_hotelid"));
            if( result != null )
            {
                result.next();
                hotelname = result.getString("name");
            }

            if( selecthotel.compareTo(DbManageHotel.getString("accept_hotelid")) == 0 )
            {
%>
      <option value="<%= DbManageHotel.getString("accept_hotelid") %>" selected><%= hotelname %></option>
<%
            }
            else
            {
%>
      <option value="<%= DbManageHotel.getString("accept_hotelid") %>"><%= hotelname %></option>
<%
            }

            db.close();

            ret = DbManageHotel.next();
        }

        db_manage.close();
%>

    </select>
    <input type="submit" value="�X�ܑI��" name="submitstore" onClick="return datacheck()">
  </td>
</form>

<%
    }
    else
    {
%>
<script type="text/javascript">
<!--
    setTimeout("window.open('manageeye_main.jsp','mainFrame')",0000);
//-->
</script>
<%
    }
%>

