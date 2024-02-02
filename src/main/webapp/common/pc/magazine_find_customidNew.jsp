<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

  <input disabled name="CustomidCheck" type="checkbox" id="CustomidCheck" value="1" onClick="setCondition(this,CustomidStart,CustomidEnd);">
  ‰ïˆõ”Ô†F
    <input name="CustomidStart" type="text" id="CustomidStart" size=11 maxlength="9" onchange="setCheck(CustomidCheck,CustomidStart,CustomidEnd);">
  `
    <input name="CustomidEnd" type="text" id="CustomidEnd" size=11 maxlength="9" onchange="setCheck(CustomidCheck,CustomidStart,CustomidEnd);">
