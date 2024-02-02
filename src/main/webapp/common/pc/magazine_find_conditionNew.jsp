<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

  <select name="Condition" id="Condition">
    <option value="0">全員に
    <option value="1">ビジター全員
    <option value="2">メンバー全員
    <option value="3">下記の条件に一致する場合
  </select>

