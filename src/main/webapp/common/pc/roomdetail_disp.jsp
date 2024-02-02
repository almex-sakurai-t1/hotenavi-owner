<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    int         now_date;
    int         now_time;
    DateEdit    df;

    df        = new DateEdit();
    now_date  = Integer.valueOf(df.getDate(2)).intValue();
    now_time  = Integer.valueOf(df.getTime(1)).intValue();

    // �z�X�g��ʎ擾
    int host_kind = 0;
    String object_no = "";
    String hotelid = (String)session.getAttribute("SelectHotel");
    if (hotelid.equals("all"))
    {
        hotelid    = request.getParameter("HotelId");
        if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
        {
            response.sendError(400);
            return;
        }
    }
    String query = "SELECT * FROM hotel WHERE hotel_id=?";
    DbAccess db =  new DbAccess();

    ResultSet result = db.execQuery(query,hotelid);
    if( result != null )
    {
        if( result.next() != false )
        {
            host_kind       = result.getInt("host_kind");
            object_no  = result.getString("object_no");
        }
    }
    if (ownerinfo.sendPacket0100(1,hotelid))
    {
        if (ownerinfo.SystemKind.equals(ownerinfo.SYSTEM_KIND_NEO))
        {
            if (ownerinfo.SystemVer1 >= ownerinfo.SYSTEM_VER1_MIN  && ownerinfo.SystemVer2 >= ownerinfo.SYSTEM_VER2_NEO_TO_SIRIUS)
            {
                host_kind = 1; 
            }
        } 
    }
    db.close();

    String TexExist  = request.getParameter("TexExist");
    String RoomCount = request.getParameter("RoomCount");
%>

<td align="center" valign="top" bgcolor="#D0CED5">
  <table width="98%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td height="6" align="center"><img src="../../common/pc/image/spacer.gif" width="300" height="6"></td>
    </tr>
    <tr>
      <td align="center">
        <table width="99%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="100" class="bar"><%= ownerinfo.ModeName %></td>
            <td class="bar">�]�ƈ��F<%= ownerinfo.EmployeeName %>�@</td>
            <td width="170" align="right" class="bar" nowrap><div class="size12">�ŏI�X�V�F<%= now_date / 10000 %>/<%= now_date / 100 % 100 %>/<%= now_date % 100 %>&nbsp;<%= now_time / 10000 %>�F<%= now_time / 100 % 100 %></div>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td valign="top"><img src="../../common/pc/image/spacer.gif" width="160" height="6"></td>
    </tr>
    <tr>
      <td align="right" valign="top">

        <%-- �y�[�W����p�[�c --%>
          <jsp:include page="roomdetail_page.jsp" flush="true" >
            <jsp:param name="RoomCount"    value="<%= RoomCount %>" />
          </jsp:include>

      </td>
    </tr>
    <tr>
      <td valign="top"><img src="../../common/pc/image/spacer.gif" width="160" height="12"></td>
    </tr>
    <tr>
      <td align="left">
        <table width="99%" border="0" cellspacing="3" cellpadding="0">
          <tr valign="top">
            <td width="144" align="left">
              <%-- �����ԍ��E�X�e�[�^�X�\���p�[�c --%>
              <jsp:include page="roomdetail_status.jsp" >
                 <jsp:param name="TexExist"     value="<%= TexExist %>" />
              </jsp:include>
            </td>
            <td valign="top">
              <%-- ���p�ҏ󋵕\���p�[�c --%>
              <jsp:include page="roomdetail_detail.jsp" />
<%
    // �z�X�g��AMF�ȊO�̏ꍇ�̂ݕ\��
    if( host_kind != 1 )
    {
%>
              <%-- �����\���p�[�c --%>
              <jsp:include page="roomdetail_message.jsp" />
<%
    }
%>
            </td>
            <td valign="right">
              <%-- �����o�[���\���p�[�c --%>
              <jsp:include page="roomdetail_member.jsp" />
            </td>
          </tr>
        </table>
      </td>
    </tr>

<%
    // �z�X�g��AMF�̏ꍇ�̂ݕ\��
    if( host_kind == 1)
    {
%>
    <tr>
      <td align="left">
        <table width="99%" border="0" cellspacing="3" cellpadding="0">
          <tr>
            <td width="30%" valign="top">
              <%-- �x�����󋵁E�����\���p�[�c --%>
              <jsp:include page="roomdetail_pay.jsp" />
            </td>
            <td width="35%" valign="top">
              <%-- ���p���ו\���p�[�c --%>
              <jsp:include page="roomdetail_use.jsp" />
            </td>
            <td width="35%" valign="right">
              <%-- ���i���ו\���p�[�c --%>
              <jsp:include page="roomdetail_goods.jsp" />
            </td>
          </tr>
        </table>
      </td>
    </tr>
<%
    }
%>
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


