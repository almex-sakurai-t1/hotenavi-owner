<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="com.hotenavi2.common.*" %>
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

    String     selecthotel   = "";
    selecthotel = ReplaceString.getParameter(request,"Store");
    if  (selecthotel == null)
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
    }
    if( !CheckString.hotenaviIdCheck(selecthotel) )
    {
        selecthotel = "";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    String    interval;

    // �X�V�Ԋu�̎擾
    interval = ReplaceString.getParameter(request,"Interval");
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
		if(!CheckString.numCheck(interval))
		{
                    interval ="0";  
%>
			<script type="text/javascript">
			<!--
			var dd = new Date();
			setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
			//-->
			</script>
<%
		}
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
<%
    if (ReplaceString.getParameter(request,"Store") == null)
    {
%>
        <tr>
          <%-- �ĕ\���Ԋu�w��p�[�c --%>
          <jsp:include page="roomdisp_refresh.jsp">
            <jsp:param name="reqpage" value="roomdisp_use.jsp" />
          </jsp:include>
        </tr>
<%
    }
    else
    {
%>
        <tr>
          <td><input type=button value="�߂�" onclick="history.back();" style="float: right;margin-top: 5px;margin-right: 5px;"></td>
<%
    }
%>
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
        </tr>
      </table>

      <%-- �������f�[�^�擾�\���p�[�c --%>
      <jsp:include page="roomdisp_use_getdata.jsp" flush="true" />

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
if( selecthotel.compareTo("all") == 0 )
    {
    // �S�X�܂�I��
%>
<script>
   parent.selectFrame.location.href = "../../common/pc/roomdisp_use_jump.jsp?cnt=<%= cnt %>";
</script>

<%
     }
%>

</BODY>
</html>

