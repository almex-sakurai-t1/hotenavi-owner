<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%!
    private static Calendar getCalendar(String currentDate) {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        if (currentDate == null || currentDate.length() != 8) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            int year = Integer.parseInt(currentDate.substring(0, 4));
            int month = Integer.parseInt(currentDate.substring(4, 6)) - 1;
            int date = Integer.parseInt(currentDate.substring(6, 8));
            calendar.set(year, month, date);
        }
        return calendar;
    }
%>
<%
    String    hotelid;
    String    hotelname;

    hotelid = ReplaceString.getParameter(request,"NowHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    hotelname = ReplaceString.getParameter(request,"NowHotelName");
    if( hotelname == null )
    {
        hotelname = "";
    }

    int    i;

    Calendar cal = Calendar.getInstance();
    int year = ownerinfo.CalGetDate / 100;
    int month = ownerinfo.CalGetDate % 100;
    int day = 1;
    cal = getCalendar(Integer.toString(year*10000+month*100+1));

    int max_month = cal.getActualMaximum(cal.DAY_OF_MONTH);
    int start = cal.get(cal.DAY_OF_WEEK);
%>
<!-- 店舗表示 -->
<a name="<%= hotelid %>"></a>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="20">
      <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="150" height="20" bgcolor="#22333F" class="tab" nowrap><font color="#FFFFFF"><%= hotelname %></font></td>
          <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
          <td height="20">
            <div><img src="../../common/pc/image/spacer.gif" width="12" height="16" align="absmiddle"><a href="#pagetop" class="navy10px">&gt;&gt;このページのトップへ</a></div>
          </td>
          <td height="20" align="right">
          </td>
        </tr>
      </table>
    </td>
  </tr>
<!-- ここから表 -->
  <tr>
  <%@ include file="../../common/pc/calendar_parts.jsp" %>
  </tr>
</table>
<!-- 1店舗目ここまで -->

