<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>
<%
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    String     session_selecthotel = (String)session.getAttribute("SelectHotel");
    String     selecthotel   = "";
    selecthotel = ReplaceString.getParameter(request,"Store");
    if  (selecthotel == null)
    {
        selecthotel = (String)session.getAttribute("SelectHotel");
    }
    if  (selecthotel == null)
    {
        selecthotel = "all";
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

    String     query              = "";
    String paramKind   = ReplaceString.getParameter(request,"Kind");
    if (paramKind == null)
    {
        paramKind = "DATE";
    }
    String dateRange = ReplaceString.getParameter(request,"Range");
    if (dateRange==null)
    {
        dateRange = "0";
    }
    if( !CheckString.numCheck (dateRange) )
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


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title>比較分析用</title>
<script type="text/javascript" src="../../common/pc/scripts/sales_datacheck.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/date_check.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/date_range_check.js"></script>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table  width="100%" border="0" cellpadding="0" cellspacing="0">
<%
    if (paramKind.equals("DATE"))
    {
%>
          <!-- 日計選択 -->
          <jsp:include page="../../common/pc/sales_compareday.jsp" flush="true" />
          <!-- 日計選択ここまで -->
<%
    }
    else if (paramKind.equals("MONTH") || paramKind.equals("MONTHLIST"))
    {
%>
          <!-- 月計選択 -->
          <jsp:include page="../../common/pc/sales_comparemonth.jsp" flush="true" />
          <!-- 月計選択ここまで -->
<%
    }
    else if (paramKind.equals("RANGE")||paramKind.equals("RANGELIST"))
    {
%>
          <!-- 期間指定選択 -->
          <jsp:include page="../../common/pc/sales_comparerange.jsp" flush="true" />
          <!-- 期間指定選択ここまで -->
<%
    }
%>

      </table>
    </td>
  </tr>
</table>
</body>
</html>
