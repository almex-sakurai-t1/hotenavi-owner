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

    param_store = request.getParameter("Store");
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
      <option value="" selected>�I�����Ă�������</option>
      <option value="fine001">̧�ݶް��ݍ���</option>
      <option value="fine002">̧�ݶް��ݐ�k</option>
      <option value="fine005">̧�ݶް��ݕP�H</option>
      <option value="fine011">̧�ݍ�</option>
      <option value="fine012">̧�ݶް��݌K��</option>
      <option value="fine013">̧�ݶް��ݖL��</option>
      <option value="fine014-1">̧�ݕP�HI</option>
      <option value="fine014-2">̧�ݕP�HII</option>
      <option value="olive01">�ذ�ޔ��i��</option>
      <option value="olive02">�ذ�ލ�</option>
      <option value="fine016">������̧�݋��s��</option>
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

