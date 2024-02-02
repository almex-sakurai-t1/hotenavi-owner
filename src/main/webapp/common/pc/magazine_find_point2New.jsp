<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

  <input disabled name="Point2Check" type="checkbox" id="Point2Check" value="1" onClick="setCondition(this,Point2Start,Point2End);">
  ポイント2：
    <input name="Point2Start" type="text" id="Point2Start" size=11 maxlength="9" style="text-align:right"  onchange="setCheck(Point2Check,Point2Start,Point2End);">
  ～
    <input name="Point2End" type="text" id="Point2End" size=11 maxlength="9" style="text-align:right"  onchange="setCheck(Point2Check,Point2Start,Point2End);">
