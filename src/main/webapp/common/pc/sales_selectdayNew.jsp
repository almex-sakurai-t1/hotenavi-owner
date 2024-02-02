<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/getCalendar.jsp" %>
<%@ include file="../../common/pc/getNationalHolidayName.jsp" %>
<%
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");

    // 現在日付
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;

    // 計上日付
    int cal_date  = ownerinfo.Addupdate;
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;
    if  (cal_date == 0) 
    {
        cal_date = now_date;
    }
    int   start_date = cal_date;

    //日付を変更して照会をクリックして日付が受け渡された場合
    String startYear  = ReplaceString.getParameter(request,"EndYear");
    String startMonth = ReplaceString.getParameter(request,"EndMonth");
    String startDay   = ReplaceString.getParameter(request,"EndDay");
    if  (startDay != null)
    {
        if(startDay.equals("0")) startDay = "1";
    }
    if( startYear != null && startMonth != null && startDay != null )
    {
        start_date = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    else if (start_date == 0)
    {
        start_date = now_date;
    }

    //前年・翌年・前月・翌月　をクリックして日付が受け渡された場合
    String paramDate = ReplaceString.getParameter(request,"paramDate");
    String nextMonthDate = "";
    String beforMonthDate = "";
    String nextYearDate = "";
    String beforYearDate = "";
    String nextDayDate = "";
    String beforDayDate = "";
    String nextWeekDate = "";
    String beforWeekDate = "";
    if (paramDate == null || paramDate.trim().length() == 0) {
        paramDate = Integer.toString(start_date);
    }
    else
    {
        start_date = Integer.parseInt(paramDate);
    }
    nextMonthDate   = getNextMonthDateString(paramDate);
    beforMonthDate  = getBeforeMonthDateString(paramDate);
    nextYearDate    = getNextYearDateString(paramDate);
    beforYearDate   = getBeforeYearDateString(paramDate);
    nextDayDate     = getNextDayDateString(paramDate);
    beforDayDate    = getBeforeDayDateString(paramDate);
    nextWeekDate    = getNextWeekDateString(paramDate);
    beforWeekDate   = getBeforeWeekDateString(paramDate);

    int DATE_MIN    = 20050101; // 最小日付
    DATE_MIN        = Integer.parseInt(getMinDateString(Integer.toString(cal_date)));
%>
<%
    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;

    //月
    String thisMonth = paramDate.substring(4, 6);
    ArrayList<String> list = getDateList(getCalendar(paramDate));

    //リンクを無効チェック
    boolean beforYearLink  = true;
    boolean beforMonthLink = true;
    boolean beforWeekLink  = true;
    boolean beforDayLink   = true;
    boolean nextYearLink   = true;
    boolean nextMonthLink  = true;
    boolean nextWeekLink   = true;
    boolean nextDayLink    = true;

    if (Integer.parseInt(beforYearDate) < DATE_MIN)
    {
       beforYearLink = false;
    }
    if (Integer.parseInt(beforMonthDate) <  DATE_MIN)
    {
       beforMonthLink = false;
    }
    if (Integer.parseInt(beforWeekDate) <  DATE_MIN)
    {
       beforWeekLink = false;
    }
    if (Integer.parseInt(beforDayDate) <  DATE_MIN)
    {
       beforDayLink = false;
    }
    if (Integer.parseInt(nextYearDate) > cal_date)
    {
       nextYearLink = false;
    }
    if (Integer.parseInt(nextMonthDate) > cal_date)
    {
       nextMonthLink = false;
    }
    if (Integer.parseInt(nextWeekDate) > cal_date)
    {
       nextWeekLink = false;
    }
    if (Integer.parseInt(nextDayDate) > cal_date)
    {
       nextDayLink = false;
    }
    String paramKind   = ReplaceString.getParameter(request,"Kind");
    if (paramKind == null)
    {
        paramKind = "DATE";
    }

    boolean CompareMode = true;
    String paramCompare = ReplaceString.getParameter(request,"Compare");
    if (paramCompare == null)
    {
        paramCompare = "false";
    }
    if  (paramCompare.equals("false"))
    {
        CompareMode = false;
    }
    boolean CompareSubmit = true;
    String paramCompareSubmit = ReplaceString.getParameter(request,"CompareSubmit");
    if (paramCompareSubmit == null)
    {
        paramCompareSubmit = "true";
    }
    if  (paramCompareSubmit.equals("false"))
    {
        CompareSubmit = false;
    }
%>
<tr>

  <td colspan="2" bgcolor="#BBBBBB">
    <table>
      <tr>
        <td class="size10" align="center">
          <a <%if (beforYearLink) {%>class="blue" href="#" onclick="jumpDate(<%=beforYearDate%>,'<%=CompareSubmit%>','<%=CompareMode%>');"<%}else{%>class="gray"<%}%>><<前年</a>&nbsp;
          <a <%if (beforMonthLink){%>class="blue" href="#" onclick="jumpDate(<%=beforMonthDate%>,'<%=CompareSubmit%>','<%=CompareMode%>');"<%}else{%>class="gray"<%}%>><前月</a>&nbsp;&nbsp;
          <a <%if (nextMonthLink) {%>class="blue" href="#" onclick="jumpDate(<%=nextMonthDate%>,'<%=CompareSubmit%>','<%=CompareMode%>');"<%}else{%>class="gray"<%}%>>翌月></a>&nbsp;
          <a <%if (nextYearLink)  {%>class="blue" href="#" onclick="jumpDate(<%=nextYearDate%>,'<%=CompareSubmit%>','<%=CompareMode%>');"<%}else{%>class="gray"<%}%>>翌年>></a>
        </td>
      </tr>
      <tr>
        <td class="cal_title">
          <%=paramDate.substring(0, 4)%>年 <%=Integer.parseInt(thisMonth)%>月
        </td>
      </tr>
      <tr>
        <td class="size10 ac">
          <table border="0" bordercolor="#000000">
            <tr>
<%
    String dayOfWeek = "日月火水木金土";
    for (int i = 0; i < dayOfWeek.length(); i++) {
%>
              <th class="<%if(i == 0){%>sunday<%}else{%>weekday<%}%>" width="28"><%=dayOfWeek.charAt(i)%></th>
<%
    }
%>
            </tr>
            <tr>
<%
    String dateStr  = null;
    String tdColor  = null;
    String tdTitle  = null;
    int    tdDetail = 0;
    String nationalHolidayName = null;
    for (int i = 0; i < list.size(); i++) 
    {
        dateStr = list.get(i);
%>
<%
        if (i != 0 && i % 7 == 0)
        {
%>
            </tr><tr>
<%
        }
        tdTitle = "";
        tdDetail = 0;
        tdColor = " black";
        if (thisMonth.equals(dateStr.substring(4, 6))) {
            nationalHolidayName = getNationalHolidayName(dateStr);
            if (nationalHolidayName.length() != 0) {
                tdColor = " red";
                String replaceStr = " title=\"★\" ";
                tdTitle = replaceStr.replace("★", nationalHolidayName);
            } else if (i % 7 == 0) {
                tdColor = " red";
            } else if ((i + 1) % 7 == 0) {
                tdColor = " blue";
            }
            tdDetail = Integer.parseInt(dateStr.substring(6, 8));
        }
        if (Integer.parseInt(dateStr) == start_date)
        {
            if(CompareSubmit){ tdColor = tdColor + " pink";}
            else             { tdColor = tdColor + " gold";}
        }
        if(tdDetail == 0)
        {
%>
            <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%>"<%=tdTitle%>>
              <span class="nglink"></span>
            </td>
<%
        } 
        else if(Integer.parseInt(dateStr) <= cal_date && Integer.parseInt(dateStr) >=DATE_MIN)
        {
%>
            <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%> border"<%=tdTitle%>>
              <a href="#" class="ac<%=tdColor%>" onclick="jumpDate(<%=(Integer.parseInt(paramDate)/100)*100+tdDetail%>,'<%=CompareSubmit%>','<%=CompareMode%>');"><%=tdDetail%></a>
            </td>
<%      }
        else
        {
%>
            <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%> border"<%=tdTitle%>>
              <span class="nglink"><%=tdDetail%></span>
            </td>
<%
        }
    }
%>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="size10" colspan="2"  align="center">
          <a <%if (beforWeekLink) {%>class="blue" href="#" onclick="jumpDate(<%=beforWeekDate%>,'<%=CompareSubmit%>','<%=CompareMode%>');"<%}else{%>class="gray"<%}%>><<前週</a>&nbsp;
          <a <%if (beforDayLink) {%>class="blue" href="#" onclick="jumpDate(<%=beforDayDate%>,'<%=CompareSubmit%>','<%=CompareMode%>');"<%}else{%>class="gray"<%}%>><前日</a>&nbsp;&nbsp;
          <a <%if (nextDayLink) {%>class="blue" href="#" onclick="jumpDate(<%=nextDayDate%>,'<%=CompareSubmit%>','<%=CompareMode%>');"<%}else{%>class="gray"<%}%>>翌日></a>&nbsp;
          <a <%if (nextWeekLink) {%>class="blue" href="#" onclick="jumpDate(<%=nextWeekDate%>,'<%=CompareSubmit%>','<%=CompareMode%>');"<%}else{%>class="gray"<%}%>>翌週>></a>
        </td>
      </tr>
    </table>
  </td>
</tr>
<tr>
<form name="selectday" action="salesdisp_select.jsp?Kind=<%=paramKind%>" method="post" target="mainFrame">
  <TD class="size10" align="left" nowrap  bgcolor="#BBBBBB" colspan=2>
    <input class="size12 ar" name="StartYear" id="StartYear" maxlength="4" size="4" value="<%=start_year%>" onclick="this.select();" onchange="checkYear(this.value);" onchange="checkYear(this.value);">年
    <input class="size12 ar" name="StartMonth" id="StartMonth" maxlength="2" size="2" value="<%=start_month%>" onclick="this.select();" onchange="checkMonth(this.value);" onchange="checkMonth(this.value);">月
    <input class="size12 ar" name="StartDay" id="StartDay" maxlength="2" size="2" value="<%=start_day%>" onclick="this.select();" onchange="checkDay(this.value);" onchange="checkDay(this.value);">日
<%
    if (!CompareMode)
    {
%>
    <input name="CompareStartYear" id="CompareStartYear" type="hidden" value="<%=start_year%>">
    <input name="CompareStartMonth" id="CompareStartMonth" type="hidden" value="<%=start_month%>">
    <input name="CompareStartDay" id="CompareStartDay" type="hidden" value="<%=start_day%>">
    <input name="Compare" type="hidden" value="<%=CompareMode%>">
    <INPUT type="button" class="size12" value="照会" onclick="if(checkDate()){rangeSet();}">
<%
    }
%>
  </TD>
</tr>
<%
    if (CompareMode)
    {
%>
      <%-- 比較対象日付選択画面 --%>
      <jsp:include page="sales_compareday.jsp" flush="true" />
<%
    }
    else
    {
%>
<tr>
  <TD class="size10" align="center" nowrap  bgcolor="#BBBBBB" colspan=2 height="100%" valign="top">
    <input name="CompareStart" type="button" class="inoutbtn" id="CompareStart" value="比較分析" onClick="document.getElementById('Compare').value='true';document.getElementById('CompareSubmit').value='false';document.selectstore.submit();">
  </TD>
</tr>
<%
    }
%>
</form>
<script type="text/javascript">
<!--
function jumpDate(paramDate,CompareSubmit,CompareMode){
    document.getElementById("StartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("StartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("StartDayStore").value   = parseInt(paramDate)%100;
    document.getElementById("EndYearStore").value    = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("EndMonthStore").value   = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("EndDayStore").value     = parseInt(paramDate)%100;
    document.getElementById("Range").value=1;
    if (CompareMode == "false")
    {
        document.getElementById("CompareStartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
        document.getElementById("CompareStartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
        document.getElementById("CompareStartDayStore").value   = parseInt(paramDate)%100;
        document.getElementById("CompareEndYearStore").value    = Math.floor(parseInt(paramDate)/10000);
        document.getElementById("CompareEndMonthStore").value   = Math.floor(parseInt(paramDate)/100)%100;
        document.getElementById("CompareEndDayStore").value     = parseInt(paramDate)%100;
    }
    if (CompareSubmit == "true" || CompareMode == "false")
    {
        document.getElementById("CompareSubmit").value ="true";
        top.Main.mainFrame.location.href="sales_wait.html";
    }
    else
    {
        document.selectstore.submit();
        top.Main.mainFrame.location.href="sales_page.html";
    }
}

function checkYear(paramYear){
    if (isNaN(paramYear))
    {
        document.selectday.StartYear.value =<%=start_year%>;
        return false;
    }
    if (paramYear <  <%=DATE_MIN/10000%>)
    {
        document.selectday.StartYear.value =<%=start_year%>;
        return false;
    }
    if (paramYear >  <%=cal_date/10000%>)
    {
        document.selectday.StartYear.value =<%=start_year%>;
        return false;
    }
    return true;
}
function checkMonth(paramMonth){
    if (isNaN(paramMonth))
    {
        document.selectday.StartMonth.value =<%=start_month%>;
        return false;
    }
    if (paramMonth <  1 || paramMonth > 12)
    {
        document.selectday.StartMonth.value =<%=start_month%>;
        return false;
    }
    return true;
}
function checkDay(paramDay){
    if (isNaN(paramDay))
    {
        document.selectday.StartDay.value =<%=start_day%>;
        return false;
    }
    if (paramDay <  1 || paramDay > 31)
    {
        document.selectday.StartDay.value =<%=start_day%>;
        return false;
    }
    return true;
}
function checkDate(){
//入力されている年月より、その月の最終日付を算出
    var years  = document.selectday.StartYear.value;
    var months = document.selectday.StartMonth.value;
    var days   = document.selectday.StartDay.value;
    var dates  = parseInt(years)*10000 + parseInt(months)*100 + parseInt(days);

    var lastday_s = monthday(years,months);

//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
    if (lastday_s < document.selectday.StartDay.value) 
    {
        document.selectday.StartYear.value =<%=start_year%>;
        document.selectday.StartMonth.value =<%=start_month%>;
        document.selectday.StartDay.value =<%=start_day%>;
        return false;
    }
    if (dates > <%=cal_date%>)
    {
        alert("先の日付は指定できません");
        document.selectday.StartYear.value =<%=start_year%>;
        document.selectday.StartMonth.value =<%=start_month%>;
        document.selectday.StartDay.value =<%=start_day%>;
        return false;
    }
    if (dates < <%=DATE_MIN%>)
    {
        alert("<%=DATE_MIN/10000%>年<%=DATE_MIN/100%100%>月<%=DATE_MIN%100%>日（5年前）から後の日付を指定してください");
        document.selectday.StartYear.value =<%=start_year%>;
        document.selectday.StartMonth.value =<%=start_month%>;
        document.selectday.StartDay.value =<%=start_day%>;
        return false;
    }
    return true;
}
function monthday(yearx,monthx){
    var lastday = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
        lastday[1] = 29;
    }
    return lastday[monthx - 1];
}
function rangeSet(){
    document.getElementById('StartYearStore').value=document.selectday.StartYear.value;
    document.getElementById('StartMonthStore').value=document.selectday.StartMonth.value;
    document.getElementById('StartDayStore').value=document.selectday.StartDay.value;
    document.getElementById('EndYearStore').value=document.selectday.StartYear.value;
    document.getElementById('EndMonthStore').value=document.selectday.StartMonth.value;
    document.getElementById('EndDayStore').value=document.selectday.StartDay.value;
    location.href='sales_select.jsp?EndYear=' + document.getElementById('StartYear').value + '&EndMonth=' + document.getElementById('StartMonth').value + '&EndDay=' + document.getElementById('StartDay').value;
}
-->
</script>
