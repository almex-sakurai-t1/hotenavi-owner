<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

  <input disabled name="PointCheck" type="checkbox" id="PointCheck" value="1" onClick="setCondition(this,PointStart,PointEnd);">
  ポイント：
    <input name="PointStart" type="text" id="PointStart" size=11 maxlength="9" style="text-align:right"  onchange="setCheck(PointCheck,PointStart,PointEnd);">
  ～
    <input name="PointEnd" type="text" id="PointEnd" size=11 maxlength="9" style="text-align:right"  onchange="setCheck(PointCheck,PointStart,PointEnd);">
