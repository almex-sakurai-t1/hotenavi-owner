<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

  <input disabled name="CountCheck" type="checkbox" id="CountCheck" value="1" onClick="setCondition(this,CountStart,CountEnd);">
  —˜—p‰ñ”F
    <input name="CountStart" type="text" id="CountStart" size=11 maxlength="9" style="text-align:right"  onchange="setCheck(CountCheck,CountStart,CountEnd);">
  `
    <input name="CountEnd" type="text" id="CountEnd" size=11 maxlength="9" style="text-align:right"  onchange="setCheck(CountCheck,CountStart,CountEnd);">
