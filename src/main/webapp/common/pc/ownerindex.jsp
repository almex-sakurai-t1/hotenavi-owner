<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // �Z�b�V�����̊m�F
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
<script type="text/javascript">
<!--
    var dd = new Date();
    setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
//-->
</SCRIPT>
<%
    }
    // ��\�z�e���Ƀ��O�C���i�v����擾�j
    ownerinfo.sendPacket0100();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title>�z�e���I�[�i�[�T�C�g</title>
</head>
<FRAMESET rows="69,*" frameborder="NO" border="0" framespacing="0">
  <FRAME src="ownermenu.jsp" name="topFrame" frameborder="no" scrolling="NO" noresize marginwidth="0" marginheight="0">
  <FRAME src="page.html" name="Main" id="Main">
  <NOFRAMES>
  <BODY>
  <P>���̃y�[�W��\������ɂ́A�t���[�����T�|�[�g���Ă���u���E�U���K�v�ł��B</P>
  </BODY>
  </NOFRAMES>
  </FRAMESET>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-209661508-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-209661508-1');
</script>

</html>
