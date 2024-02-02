<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

  <input disabled name="MailaddressCheck" type="checkbox" id="MailaddressCheck" value="1" onClick="setCondition(this,Mailaddress,Mailaddress);">
  メールアドレス：
    <input name="Mailaddress" type="text" id="Mailaddress" size=30 maxlength="100" onchange="setCheck(MailaddressCheck,Mailaddress,Mailaddress);">
 