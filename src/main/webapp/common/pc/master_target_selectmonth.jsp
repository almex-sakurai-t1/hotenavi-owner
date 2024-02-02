<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %>
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
    int         i;
    int         year;
    int         month;
    int         day;
    int         ymd;
    int         now_year;
    int         now_month;
    int         now_day;
    String      param_year;
    String      param_month;
    String      param_day;
    Calendar    cal;

    cal       = Calendar.getInstance();
    now_year  = cal.get(cal.YEAR);
    now_month = cal.get(cal.MONTH) + 1;
    now_day   = cal.get(cal.DATE);

    param_year  = ReplaceString.getParameter(request,"Year");
    param_month = ReplaceString.getParameter(request,"Month");

    if( param_year != null && param_month != null )
    {
        if( !CheckString.numCheck(param_year) || !CheckString.numCheck(param_month) )
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
        ymd = (Integer.valueOf(param_year).intValue() * 10000) + (Integer.valueOf(param_month).intValue() * 100);
    }
    else
    {
        // 日付選択なしの場合は計上日
        ymd = ownerinfo.Addupdate;
    }

    if( ymd != 0 )
    {
        year  = ymd / 10000;
        month = (ymd / 100 % 100) - 1;
        day   = ymd % 100;
    }
    else
    {
        cal   = Calendar.getInstance();
        year  = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH);
        day   = cal.get(cal.DATE);
    }

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<title>売上目標月選択</title>
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#663333" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form name="selectmonth" action="master_target.jsp?New=1&SelectGroup=3&SelectMoney=3" method="post" target="targetFrame">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
        <tr valign="middle">

          <td width="80" align="center" nowrap bgcolor="#000000">
            <div class="white12" align="center">設定年月</div>
          </td>
    <td align="left" bgcolor="#FFFFFF">
      <div class="size10">
	<!-- 設定年月表示 -->
        <select class="size12" name="Year">
<%
    for( i = 0 ; i < 3 ; i++ )
    {
        if( now_year + i == year )
        {
%>
        <option VALUE="<%= now_year + i %>" selected><%= now_year + i %></option>
<%
        }
        else
        {
%>
        <option VALUE="<%= now_year + i %>"><%= now_year + i %></option>
<%
        }
%>
<%
    }
%>
        </select>
        年  
        <select class=size12 size=1 name="Month">
<%
    for( i = 0 ; i < 12 ; i++ )
    {
        if( i == month )
        {
%>
            <option VALUE="<%= i + 1 %>" selected><%= i + 1 %></option>
<%
        }
        else
        {
%>
            <option VALUE="<%= i + 1 %>"><%= i + 1 %></option>
<%
        }
    }
%>
        </select>
        月  
        <input type="submit" class="size12" value="選択">
	<!-- 設定年月表示ここまで -->
      </div>
    </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</form>
</body>
</html>
