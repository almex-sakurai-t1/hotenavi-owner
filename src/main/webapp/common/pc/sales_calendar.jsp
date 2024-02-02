<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/getCalendar.jsp" %>
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
    cal_date = Integer.parseInt(getNext1MDateString(nowDate));
    int   start_date = cal_date;


    //日付を変更して照会をクリックして日付が受け渡された場合
    String startYear  = ReplaceString.getParameter(request,"EndYear");
    String startMonth = ReplaceString.getParameter(request,"EndMonth");
    String startDay   = ReplaceString.getParameter(request,"EndDay");
    String[] dates = new String[]{ startYear, startMonth, startDay };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }
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
    if (paramDate == null || paramDate.trim().length() == 0) {
        paramDate = Integer.toString(start_date);
    }
    else
    {
        start_date = Integer.parseInt(paramDate);
    }
    nextMonthDate   = getNext1MDateString(paramDate);
    beforMonthDate  = getBefore1MDateString(paramDate);
    nextYearDate    = getNext1YDateString(paramDate);
    beforYearDate   = getBefore1YDateString(paramDate);

    int thisMonthLastDay  = getCalendar(paramDate).getActualMaximum(Calendar.DATE);
    if  (Integer.parseInt(paramDate)/100 == cal_date/100)
    {
        thisMonthLastDay = cal_day;
    }
    int nextMonthLastDay  = getCalendar(nextMonthDate).getActualMaximum(Calendar.DATE);
    if  (Integer.parseInt(nextMonthDate)/100 == cal_date/100)
    {
        nextMonthLastDay = cal_day;
    }
    int beforMonthLastDay = getCalendar(beforMonthDate).getActualMaximum(Calendar.DATE);
    int nextYearLastDay   = getCalendar(nextYearDate).getActualMaximum(Calendar.DATE);
    int beforYearLastDay  = getCalendar(beforYearDate).getActualMaximum(Calendar.DATE);
    if  (Integer.parseInt(nextYearDate)/100 == cal_date/100)
    {
        nextYearLastDay = cal_day;
    }

    int nextMonthLastDate  = (Integer.parseInt(nextMonthDate)/100)*100  + nextMonthLastDay;
    int beforMonthLastDate = (Integer.parseInt(beforMonthDate)/100)*100 + beforMonthLastDay;
    int nextYearLastDate   = (Integer.parseInt(nextYearDate)/100)*100   + nextYearLastDay;
    int beforYearLastDate  = (Integer.parseInt(beforYearDate)/100)*100  + beforYearLastDay;

    int DATE_MIN    = 20050101; // 最小日付
    DATE_MIN        = Integer.parseInt(getMinDateString(Integer.toString(cal_date)));
%>
<%
    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;

    //月
    String thisMonth = paramDate.substring(4, 6);

    //リンクを無効チェック
    boolean beforYearLink  = true;
    boolean beforMonthLink = true;
    boolean nextYearLink   = true;
    boolean nextMonthLink  = true;

    if (Integer.parseInt(beforYearDate)/100 < DATE_MIN/100)
    {
       beforYearLink = false;
    }
    if (Integer.parseInt(beforMonthDate)/100 <  DATE_MIN/100)
    {
       beforMonthLink = false;
    }
    if (Integer.parseInt(nextYearDate)/100 > cal_date/100)
    {
       nextYearLink = false;
    }
    if (Integer.parseInt(nextMonthDate)/100 > cal_date/100)
    {
       nextMonthLink = false;
    }

    String paramKind   = ReplaceString.getParameter(request,"Kind");
    if(CheckString.isValidParameter(paramKind) && !CheckString.numAlphaCheck(paramKind))
    {
        response.sendError(400);
        return;
    }
    if (paramKind == null)
    {
        paramKind = "MONTH";
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
        <td class="size10" align="center" nowrap>
          <a <%if (beforYearLink) {%>class="blue" href="#" onclick="jumpMonth(<%=beforYearLastDate%>,<%=beforYearLastDay%>,'<%=CompareSubmit%>','<%=CompareMode%>')"<%}else{%>class="gray"<%}%>><<前年</a>&nbsp;
          <a <%if (beforMonthLink){%>class="blue" href="#" onclick="jumpMonth(<%=beforMonthLastDate%>,<%=beforMonthLastDay%>,'<%=CompareSubmit%>','<%=CompareMode%>')"<%}else{%>class="gray"<%}%>><前月</a>&nbsp;&nbsp;
          <a <%if (nextMonthLink) {%>class="blue" href="#" onclick="jumpMonth(<%=nextMonthLastDate%>,<%=nextMonthLastDay%>,'<%=CompareSubmit%>','<%=CompareMode%>')"<%}else{%>class="gray"<%}%>>翌月></a>&nbsp;
          <a <%if (nextYearLink)  {%>class="blue" href="#" onclick="jumpMonth(<%=nextYearLastDate%>,<%=beforYearLastDay%>,'<%=CompareSubmit%>','<%=CompareMode%>')"<%}else{%>class="gray"<%}%>>翌年>></a>
        </td>
      </tr>
      <tr>
        <td class="cal_title">
          <%=start_year%>&nbsp;年
        </td>
      </tr>
      <tr class="size10 ac">
        <td class="size10 ac">
          <table border="0" bordercolor="#000000">
            <tr>
<%
    String tdColor  = null;
    int    tdDetail = 0;
    for (int i = 0; i < 12; i++) 
    {
        if (i != 0 && i % 4 == 0) 
        {
%>
            </tr><tr>
<%
        }
        tdColor = " black";
        tdDetail = i+1;
        String thisMonthDate = Integer.toString(start_year * 10000 + tdDetail * 100 + 1);
        thisMonthLastDay  = getCalendar(thisMonthDate).getActualMaximum(Calendar.DATE);
        int thisMonthLastDate  = (Integer.parseInt(thisMonthDate)/100)*100  + thisMonthLastDay;
        if ((start_year * 100 + tdDetail) == start_date/100)
        {
            if(CompareSubmit){ tdColor = tdColor + " pink";}
            else             { tdColor = tdColor + " gold";}
        }
        if((start_year * 100 + tdDetail) <= cal_date/100 && (start_year * 100 + tdDetail) >=DATE_MIN/100)
        {
%>
              <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%> border" width="40px">
                <a href="#" class="ac<%=tdColor%>" onclick="jumpMonth(<%=thisMonthLastDate%>,<%=thisMonthLastDay%>,'<%=CompareSubmit%>','<%=CompareMode%>')"><%=tdDetail%>月</a>
              </td>
<%
        }
        else
        {
%>
              <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%> border" width="40px">
                <span class="nglink"><%=tdDetail%>月</span>
              </td>
<%
        }
    }
%>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </td>
</tr>
<tr>
<form name="selectday" action="salesdisp_select.jsp?Kind=<%=paramKind%>" method="post" target="mainFrame">
  <TD class="size10" align="center" nowrap  bgcolor="#BBBBBB" colspan=2>
    <input class="size12 ar" name="StartYear"  id="StartYear" maxlength="4" size="4" value="<%=start_year%>" onclick="this.select();" onchange="checkYear(this.value);" onchange="checkYear(this.value);">年
    <input class="size12 ar" name="StartMonth" id="StartMonth" maxlength="2" size="2" value="<%=start_month%>" onclick="this.select();" onchange="checkMonth(this.value);" onchange="checkMonth(this.value);">月
    <input class="size12 ar" name="StartDay" type="hidden" id="StartDay" value="0">
<%
    if (!CompareMode)
    {
%>
    <input name="CompareStartYear" id="CompareStartYear" type="hidden" value="<%=start_year%>">
    <input name="CompareStartMonth" id="CompareStartMonth" type="hidden" value="<%=start_month%>">
    <input name="CompareStartDay" id="CompareStartDay" type="hidden" value="0">
    <input name="Compare" type="hidden" value="<%=CompareMode%>">
    <INPUT type="button" class="size12" value="照会" onclick="if(checkDate()){document.getElementById('StartYearStore').value=document.selectday.StartYear.value;document.getElementById('StartMonthStore').value=document.selectday.StartMonth.value;document.getElementById('StartDayStore').value='0';location.href='sales_select.jsp?Kind=<%=paramKind%>&EndYear=' + document.getElementById('StartYear').value + '&EndMonth=' + document.getElementById('StartMonth').value + '&EndDay=0';}">
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
      <jsp:include page="sales_comparecalendar.jsp" flush="true" />
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
function jumpMonth(paramDate,Range,CompareSubmit,CompareMode){
    document.getElementById("StartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("StartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("StartDayStore").value   = 1;
    document.getElementById("EndYearStore").value    = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("EndMonthStore").value   = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("EndDayStore").value     = parseInt(paramDate)%100;
    document.getElementById("Range").value = Range;
    if (CompareMode == "false")
    {
        document.getElementById("CompareStartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
        document.getElementById("CompareStartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
        document.getElementById("CompareStartDayStore").value   = 1;
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
function checkDate(){
//入力されている年月より、その月の最終日付を算出
    var years  = document.selectday.StartYear.value;
    var months = document.selectday.StartMonth.value;
    var dates  = parseInt(years)*100 + parseInt(months);

//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
    if (dates > <%=cal_date/100%>)
    {
        document.selectday.StartYear.value =<%=start_year%>;
        document.selectday.StartMonth.value =<%=start_month%>;
        return false;
    }
    if (dates < <%=DATE_MIN/100%>)
    {
        document.selectday.StartYear.value =<%=start_year%>;
        document.selectday.StartMonth.value =<%=start_month%>;
        return false;
    }
    return true;
}
-->
</script>
