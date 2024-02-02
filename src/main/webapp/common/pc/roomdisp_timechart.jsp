<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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
    String param_cnt = request.getParameter("cnt");
    if(CheckString.isValidParameter(param_cnt) && !CheckString.numAlphaCheck(param_cnt))
    {
        response.sendError(400);
        return;
    }
    if( param_cnt == null )
    {
        param_cnt = "0";
    }
    int cnt = Integer.parseInt(param_cnt);

    String storeselect = (String)session.getAttribute("SelectHotel");
    String    interval;

    // �X�V�Ԋu�̎擾
    interval = request.getParameter("Interval");
    if(CheckString.isValidParameter(interval) && !CheckString.numAlphaCheck(interval))
    {
        response.sendError(400);
        return;
    }

    if( interval == null )
    {
        interval = (String)session.getAttribute("Interval");
        if( interval == null )
        {
            interval = "";
        }
    }
    else
    {
        session.setAttribute("Interval", interval);
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Refresh" content="<%= interval %>">
<meta http-equiv="Pragma" content="no-cache">
<title></title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/room.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
</head>

<BODY bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <a name="pagetop"></a>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">

        <tr>
          <%-- �ĕ\���Ԋu�w��p�[�c --%>
          <jsp:include page="roomdisp_refresh.jsp">
            <jsp:param name="reqpage" value="roomdisp_timechart.jsp" />
          </jsp:include>
        </tr>

        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
        </tr>
      </table>

      <%-- �������f�[�^�擾�\���p�[�c --%>
      <jsp:include page="roomdisp_timechart_getdata.jsp" flush="true" />

    </td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10">
      <!-- #BeginLibraryItem "/Library/footer.lbi" -->
      <IMG src="../../common/pc/image/imedia.gif" width="96" height="15" align="absmiddle">
      <IMG src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex inc . All Rights Reserved.
      <!-- #EndLibraryItem -->
    </td> 
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><IMG src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
<%
if( storeselect.compareTo("all") == 0 )
    {
    // �S�X�܂�I��
%>
<script>
   parent.selectFrame.location.href = "../../common/pc/roomdisp_timechart_jump.jsp?cnt=<%= cnt %>";
</script>

<%
     }
%>

</BODY>
</html>

