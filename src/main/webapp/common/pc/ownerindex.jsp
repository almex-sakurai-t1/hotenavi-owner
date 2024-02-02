<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
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
    // 代表ホテルにログイン（計上日取得）
    ownerinfo.sendPacket0100();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title>ホテルオーナーサイト</title>
</head>
<FRAMESET rows="69,*" frameborder="NO" border="0" framespacing="0">
  <FRAME src="ownermenu.jsp" name="topFrame" frameborder="no" scrolling="NO" noresize marginwidth="0" marginheight="0">
  <FRAME src="page.html" name="Main" id="Main">
  <NOFRAMES>
  <BODY>
  <P>このページを表示するには、フレームをサポートしているブラウザが必要です。</P>
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
