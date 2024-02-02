<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

  <input disabled name="TotalCheck" type="checkbox" id="TotalCheck" value="1" onClick="setCondition(this,TotalStart,TotalEnd);">
  —˜—p‹àŠzF
    <input name="TotalStart" type="text" id="TotalStart" size=11 maxlength="9" style="text-align:right" onchange="setCheck(TotalCheck,TotalStart,TotalEnd);">
  `
    <input name="TotalEnd" type="text" id="TotalEnd" size=11 maxlength="9" style="text-align:right" onchange="setCheck(TotalCheck,TotalStart,TotalEnd);">
