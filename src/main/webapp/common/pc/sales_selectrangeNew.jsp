<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/getCalendar.jsp" %>
<%@ include file="../../common/pc/getNationalHolidayName.jsp" %>
<%
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");

    String paramKind   = ReplaceString.getParameter(request,"Kind");
    if(CheckString.isValidParameter(paramKind) && !CheckString.numAlphaCheck(paramKind))
    {
        response.sendError(400);
        return;
    }
    if (paramKind == null)
    {
        paramKind = "RANGE";
    }

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
	boolean dateError = false;
    //日付を変更して照会をクリックして日付が受け渡された場合
    String startYear  = ReplaceString.getParameter(request,"StartYear");
    String startMonth = ReplaceString.getParameter(request,"StartMonth");
    String startDay   = ReplaceString.getParameter(request,"StartDay");
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
    String nextDayDate = "";
    String beforDayDate = "";
    String nextWeekDate = "";
    String beforWeekDate = "";
    String nextEndYearDate = "";
    String nextEndMonthDate = "";
    String nextEndWeekDate = "";
    String nextEndDayDate = "";
    String next1MDate = "";
    String next2MDate = "";
    if (paramDate == null || paramDate.trim().length() == 0) {
        paramDate = Integer.toString(start_date);
    }
    else
    {
        start_date = Integer.parseInt(paramDate);
    }
    int end_date    = start_date;

    String paramEndDate = paramDate;

    String endYear  = ReplaceString.getParameter(request,"EndYear");
    String endMonth = ReplaceString.getParameter(request,"EndMonth");
    String endDay   = ReplaceString.getParameter(request,"EndDay");
    String[] endDates = new String[]{ endYear, endMonth, endDay };
    for( String date : endDates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }
    if( endYear != null && endMonth != null && endDay != null )
    {
        end_date = (Integer.valueOf(endYear).intValue() * 10000) + (Integer.valueOf(endMonth).intValue() * 100) + Integer.valueOf(endDay).intValue();
    }
    if (end_date == 0)
    {
        end_date  = start_date;
    }
    paramEndDate    = Integer.toString(end_date);
    String dateRange = ReplaceString.getParameter(request,"Range");
    if (dateRange!=null)
    {
        if( !CheckString.numCheck(dateRange) )
        {
            response.sendError(400);
            return;
        }
        if (!dateRange.equals("0"))
        {
            paramEndDate = getAddDayDateString(paramDate,Integer.parseInt(dateRange)-1);
            end_date     = Integer.parseInt(paramEndDate);
        }
    }

    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;
    int end_year    = end_date/10000;
    int end_month   = end_date/100%100;
    int end_day     = end_date%100;

    int date_range  = getDateRange(paramDate,paramEndDate);
    int date_range_max = date_range;

    nextMonthDate   = getNextMonthDateString(paramDate);
    beforMonthDate  = getBeforeMonthDateString(paramDate);
    nextYearDate    = getNextYearDateString(paramDate);
    beforYearDate   = getBeforeYearDateString(paramDate);
    nextDayDate     = getNextDayDateString(paramDate);
    beforDayDate    = getBeforeDayDateString(paramDate);
    nextWeekDate    = getNextWeekDateString(paramDate);
    beforWeekDate   = getBeforeWeekDateString(paramDate);
    nextEndMonthDate   = getNextMonthDateString(paramEndDate);
    nextEndYearDate    = getNextYearDateString(paramEndDate);
    nextEndDayDate     = getNextDayDateString(paramEndDate);
    nextEndWeekDate    = getNextWeekDateString(paramEndDate);

    next1MDate   = getNext1MDateString(paramDate);
    int date_range_1m = getDateRange(paramDate,next1MDate)-1;
    next2MDate   = getNext2MDateString(paramDate);
    int date_range_2m = getDateRange(paramDate,next2MDate)-1;
    if  (date_range_2m > date_range_max)
    {
        date_range_max = date_range_2m; //期間指定の最大日数
    }

    int DATE_MIN    = 20050101; // 最小日付
    DATE_MIN        = Integer.parseInt(getMinDateString(Integer.toString(cal_date)));
%>
<%

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
    if (Integer.parseInt(nextEndYearDate) > cal_date)
    {
       nextYearLink = false;
    }
    if (Integer.parseInt(nextEndMonthDate) > cal_date)
    {
       nextMonthLink = false;
    }
    if (Integer.parseInt(nextEndWeekDate) > cal_date)
    {
       nextWeekLink = false;
    }
    if (Integer.parseInt(nextEndDayDate) > cal_date)
    {
       nextDayLink = false;
    }

    boolean SubmitOK = true;
    String paramSubmitOK = ReplaceString.getParameter(request,"SubmitOK");
    if ( paramSubmitOK == null)
    {
         paramSubmitOK = "true";
    }
    if  ( paramSubmitOK.equals("false"))
    {
        SubmitOK = false;
    }
    if ((dateRange.equals("0") || dateRange.equals("1")) && paramKind.indexOf("RANGE") == 0)
    {
        SubmitOK = false;
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

    if (!CompareSubmit)
    {
        SubmitOK = false;
    }
%>
<tr>
  <td colspan="2" bgcolor="#BBBBBB">
    <table>
      <tr>
        <td class="size10" align="center">
          <a <%if (beforYearLink) {%>class="blue" href="#" onclick="jumpDate(<%=beforYearDate%>,<%=date_range%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><<前年</a>&nbsp;
          <a <%if (beforMonthLink){%>class="blue" href="#" onclick="jumpDate(<%=beforMonthDate%>,<%=date_range%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><前月</a>&nbsp;&nbsp;
          <a <%if (nextMonthLink) {%>class="blue" href="#" onclick="jumpDate(<%=nextMonthDate%>,<%=date_range%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌月></a>&nbsp;
          <a <%if (nextYearLink)  {%>class="blue" href="#" onclick="jumpDate(<%=nextYearDate%>,<%=date_range%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌年>></a>
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
    for (int i = 0; i < dayOfWeek.length(); i++)
    {
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
        if (Integer.parseInt(dateStr) >= start_date && Integer.parseInt(dateStr) <= end_date )
        {
            if(SubmitOK){ tdColor = tdColor + " pink";}
            else        { tdColor = tdColor + " gold";}
        }
        if(tdDetail == 0)
        {
%>
              <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%>"<%=tdTitle%>>
                <span class="nglink"></span>
              </td>
<%
        }
        else if(Integer.parseInt(dateStr) > cal_date || Integer.parseInt(dateStr) < DATE_MIN)
        {
%>
              <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%> border"<%=tdTitle%>>
                <span class="nglink"><%=tdDetail%></span>
              </td>
<%
         }
        else if(Integer.parseInt(getAddDayDateString(dateStr,Integer.parseInt(dateRange)-1)) > cal_date)
        {
%>
              <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%> border"<%=tdTitle%>>
                <%=tdDetail%>
              </td>
<%
        }
        else if(Integer.parseInt(dateStr) <= cal_date && Integer.parseInt(dateStr) >=DATE_MIN)
        {
              %>
              <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%> border"<%=tdTitle%>>
                <a href="#" class="ac<%=tdColor%>" onclick="jumpDate(<%=(Integer.parseInt(paramDate)/100)*100+tdDetail%>,<%=date_range%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"><%=tdDetail%></a>
              </td>
<%
        }
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
          <a <%if (beforWeekLink) {%>class="blue" href="#" onclick="jumpDate(<%=beforWeekDate%>,<%=date_range%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><<前週</a>&nbsp;
          <a <%if (beforDayLink) {%>class="blue" href="#" onclick="jumpDate(<%=beforDayDate%>,<%=date_range%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><前日</a>&nbsp;&nbsp;
          <a <%if (nextDayLink) {%>class="blue" href="#" onclick="jumpDate(<%=nextDayDate%>,<%=date_range%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌日></a>&nbsp;
          <a <%if (nextWeekLink) {%>class="blue" href="#" onclick="jumpDate(<%=nextWeekDate%>,<%=date_range%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌週>></a>
        </td>
      </tr>
    </table>
  </td>
</tr>
<tr>
<form name="selectday" action="salesdisp_select.jsp?Kind=<%=paramKind%>" method="post" target="mainFrame">
  <TD class="size10" align="left" nowrap  bgcolor="#BBBBBB" colspan=2 style="padding-left:3px">
    <input class="size12 ar" name="StartYear" id="StartYear" maxlength="4" size="4" value="<%=start_year%>" onclick="this.select();" onchange="checkStartYear(this.value);">年
    <input class="size12 ar" name="StartMonth" id="StartMonth" maxlength="2" size="2" value="<%=start_month%>" onclick="this.select();" onchange="checkStartMonth(this.value);">月
    <input class="size12 ar" name="StartDay" id="StartDay" maxlength="2" size="2" value="<%=start_day%>" onclick="this.select();" onchange="checkStartDay(this.value);">日
    ～<br>
    <select name="Range" onChange="dateSetfromRange('<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');">
<%
    for (int i = 0; i < date_range_max; i++)
    {
%>
       <option value="<%=i+1%>"<%if(date_range==i+1){%>selected<%}%>><%if(date_range_1m==i+1){%>1ヶ月間<%}else if(date_range_2m==i+1){%>2ヶ月間<%}else{%><%=i+1%>日間<%}%>
<%
    }
%>
    </select><br>
    <input class="size12 ar" name="EndYear" id="EndYear" maxlength="4" size="4" value="<%=end_year%>" onclick="this.select();" onchange="checkEndYear(this.value);">年
    <input class="size12 ar" name="EndMonth" id="EndMonth" maxlength="2" size="2" value="<%=end_month%>" onclick="this.select();" onchange="checkEndMonth(this.value);">月
    <input class="size12 ar" name="EndDay" id="EndDay" maxlength="2" size="2" value="<%=end_day%>" onclick="this.select();" onchange="checkEndDay(this.value);">日
<%
    if (!CompareMode)
    {
%>
    <input name="CompareStartYear" id="CompareStartYear" type="hidden" value="<%=start_year%>">
    <input name="CompareStartMonth" id="CompareStartMonth" type="hidden" value="<%=start_month%>">
    <input name="CompareStartDay" id="CompareStartDay" type="hidden" value="<%=start_day%>">
    <input name="CompareEndYear" id="CompareEndYear" type="hidden" value="<%=end_year%>">
    <input name="CompareEndMonth" id="CompareEndMonth" type="hidden" value="<%=end_month%>">
    <input name="CompareEndDay" id="CompareEndDay" type="hidden" value="<%=end_day%>">
    <input name="Compare" type="hidden" value="<%=CompareMode%>">
    <INPUT type="button" class="size12" value="照会" onclick="checkDate()">
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
      <jsp:include page="sales_comparerange.jsp" flush="true" />
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
function jumpDate(paramDate,Range,CompareSubmit,CompareMode,SubmitOK){
    document.getElementById("StartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("StartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("StartDayStore").value   = parseInt(paramDate)%100;
    document.getElementById("Range").value=Range;
    if (CompareMode == "false")
    {
        document.getElementById("CompareStartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
        document.getElementById("CompareStartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
        document.getElementById("CompareStartDayStore").value   = parseInt(paramDate)%100;
    }
    if (SubmitOK == "true" && (CompareSubmit == "true" || CompareMode == "false"))
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
function checkStartYear(paramYear){
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
    rangeSet();
    return true;
}
function checkStartMonth(paramMonth){
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
    rangeSet();
    return true;
}
function checkStartDay(paramDay){
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
    rangeSet();
    return true;
}
function checkEndYear(paramYear){
    if (isNaN(paramYear))
    {
        document.selectday.EndYear.value =<%=end_year%>;
        return false;
    }
    if (paramYear <  <%=DATE_MIN/10000%>)
    {
        document.selectday.EndYear.value =<%=end_year%>;
        return false;
    }
    if (paramYear >  <%=cal_date/10000%>)
    {
        document.selectday.EndYear.value =<%=end_year%>;
        return false;
    }
    rangeSet();
    return true;
}
function checkEndMonth(paramMonth){
    if (isNaN(paramMonth))
    {
        document.selectday.EndMonth.value =<%=end_month%>;
        return false;
    }
    if (paramMonth <  1 || paramMonth > 12)
    {
        document.selectday.EndMonth.value =<%=end_month%>;
        return false;
    }
    rangeSet();
    return true;
}
function checkEndDay(paramDay){
    if (isNaN(paramDay))
    {
        document.selectday.EndDay.value =<%=end_day%>;
        return false;
    }
    if (paramDay <  1 || paramDay > 31)
    {
        document.selectday.EndDay.value =<%=end_day%>;
        return false;
    }
    rangeSet();
    return true;
}
function checkDate(){
//入力されている年月より、その月の最終日付を算出
    var years  = document.selectday.StartYear.value;
    var months = document.selectday.StartMonth.value;
    var days   = document.selectday.StartDay.value;
    var dates  = parseInt(years)*10000 + parseInt(months)*100 + parseInt(days);

    var lastday_s = monthday(years,months);

    var yeare  = document.selectday.EndYear.value;
    var monthe = document.selectday.EndMonth.value;
    var daye   = document.selectday.EndDay.value;
    var datee  = parseInt(yeare)*10000 + parseInt(monthe)*100 + parseInt(daye);

    var lastday_e = monthday(yeare,monthe);

//開始日の2ヵ月後
    var date2e  = parseInt(years)*10000 + parseInt(months)*100 + parseInt(days) + 200;
    if  (date2e/100%100 > 12)
    {
        date2e = date2e + 8800;
    }

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
    if (lastday_e < document.selectday.EndDay.value) 
    {
        document.selectday.EndYear.value =<%=end_year%>;
        document.selectday.EndMonth.value =<%=end_month%>;
        document.selectday.EndDay.value =<%=end_day%>;
        return false;
    }
    if (datee > <%=cal_date%>)
    {
        alert("先の日付は指定できません");
        document.selectday.EndYear.value =<%=end_year%>;
        document.selectday.EndMonth.value =<%=end_month%>;
        document.selectday.EndDay.value =<%=end_day%>;
        return false;
    }
    if (datee < <%=DATE_MIN%>)
    {
        alert("<%=DATE_MIN/10000%>年<%=DATE_MIN/100%100%>月<%=DATE_MIN%100%>日（5年前）から後の日付を指定してください");
        document.selectday.EndYear.value =<%=end_year%>;
        document.selectday.EndMonth.value =<%=end_month%>;
        document.selectday.EndDay.value =<%=end_day%>;
        return false;
    }
    if (datee < dates)
    {
        alert("期間指定（開始日付≦終了日付）を正しく入力してください");
        document.selectday.StartYear.value =<%=start_year%>;
        document.selectday.StartMonth.value =<%=start_month%>;
        document.selectday.StartDay.value =<%=start_day%>;
        document.selectday.EndYear.value =<%=end_year%>;
        document.selectday.EndMonth.value =<%=end_month%>;
        document.selectday.EndDay.value =<%=end_day%>;
        return false;
    }
    if (datee == dates)
    {
        alert("日付範囲を指定してください");
        document.selectday.StartYear.value =<%=start_year%>;
        document.selectday.StartMonth.value =<%=start_month%>;
        document.selectday.StartDay.value =<%=start_day%>;
        document.selectday.EndYear.value =<%=end_year%>;
        document.selectday.EndMonth.value =<%=end_month%>;
        document.selectday.EndDay.value =<%=end_day%>;
        return false;
    }
    if (datee > date2e)
    {
        alert("期間指定は2ヶ月以内でお願いします");
        document.selectday.StartYear.value =<%=start_year%>;
        document.selectday.StartMonth.value =<%=start_month%>;
        document.selectday.StartDay.value =<%=start_day%>;
        document.selectday.EndYear.value =<%=end_year%>;
        document.selectday.EndMonth.value =<%=end_month%>;
        document.selectday.EndDay.value =<%=end_day%>;
        return false;
    }
    rangeSet("true");
    return true;
}
function monthday(yearx,monthx){
    var lastday = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    if (((yearx % 4 == 0) && (yearx % 100 != 0)) || (yearx % 400 == 0)){
        lastday[1] = 29;
    }
    return lastday[monthx - 1];
}
function rangeSet(SubmitOK){
//入力されている年月より、その月の最終日付を算出
    var years  = document.selectday.StartYear.value;
    var months = document.selectday.StartMonth.value;
    var days   = document.selectday.StartDay.value;
    var dates  = parseInt(years)*10000 + parseInt(months)*100 + parseInt(days);

    var lastday_s = monthday(years,months);

    var yeare  = document.selectday.EndYear.value;
    var monthe = document.selectday.EndMonth.value;
    var daye   = document.selectday.EndDay.value;
    var datee  = parseInt(yeare)*10000 + parseInt(monthe)*100 + parseInt(daye);

    var lastday_e = monthday(yeare,monthe);
    var date2e  = parseInt(years)*10000 + parseInt(months)*100 + parseInt(days) + 200;

    if  (date2e/100%100 > 12)
    {
        date2e = date2e + 8800;
    }
    if (lastday_s < document.selectday.StartDay.value) 
    {
    }
    else
    if (dates > <%=cal_date%>)
    {
    }
    else
    if (dates < <%=DATE_MIN%>)
    {
    }
    else
    if (lastday_e < document.selectday.EndDay.value) 
    {
    }
    else
    if (datee > <%=cal_date%>)
    {
    }
    else
    if (datee < <%=DATE_MIN%>)
    {
    }
    else
    if (datee < dates)
    {
    }
    else
    if (datee > date2e)
    {
    }
    else
    {
        secStart = new Date(document.selectday.StartYear.value,document.selectday.StartMonth.value-1,document.selectday.StartDay.value);
        secEnd   = new Date(document.selectday.EndYear.value,document.selectday.EndMonth.value-1,document.selectday.EndDay.value);
        secRange = secEnd.getTime()-secStart.getTime();
        document.selectday.Range.selectedIndex = Math.floor(secRange/(1000*60*60*24));
        document.getElementById("StartYearStore").value  = document.selectday.StartYear.value;
        document.getElementById("StartMonthStore").value = document.selectday.StartMonth.value;
        document.getElementById("StartDayStore").value   = document.selectday.StartDay.value;
        document.getElementById("EndYearStore").value    = document.selectday.EndYear.value;
        document.getElementById("EndMonthStore").value   = document.selectday.EndMonth.value;
        document.getElementById("EndDayStore").value     = document.selectday.EndDay.value;
        document.getElementById("Range").value=document.selectday.Range.selectedIndex+1;
        if (SubmitOK == "true")
        {
            document.getElementById("SubmitOK").value="true";
            document.getElementById("CompareSubmit").value ="true";
            top.Main.mainFrame.location.href="sales_wait.html";
        }
        else
        {
            document.getElementById("SubmitOK").value="false";
            top.Main.mainFrame.location.href="sales_page.html";
            document.selectstore.submit();
        }
    }
    return true;
}

function dateSetfromRange(CompareSubmit,CompareMode,SubmitOK){
    var years  = document.selectday.StartYear.value;
    var months = document.selectday.StartMonth.value;
    var days   = document.selectday.StartDay.value;
    var msec  = (new Date(parseInt(years)+"/"+parseInt(months)+"/"+parseInt(days))).getTime();
    var n     = eval(document.selectday.Range.selectedIndex);
    var nmsec = n * 1000 * 60 * 60 * 24;//　１日のミリ秒
    var dt    =  new Date(msec + nmsec );
    var year_result  = dt.getYear();
    if (year_result < 1900)
    {
        year_result  = year_result + 1900;
    }
    var month_result = dt.getMonth() + 1;
    var day_result   = dt.getDate();
    document.selectday.EndYear.value  = year_result;
    document.selectday.EndMonth.value = month_result;
    document.selectday.EndDay.value   = day_result;

    
//  もし、計上日付より大きい場合は、最終日を計上日にして逆算する。
    var date_result = parseInt(year_result)*10000 + parseInt(month_result)*100 + parseInt(day_result);
    if(date_result><%=cal_date%>)
    {
        document.selectday.EndYear.value  = <%=cal_date/10000%>;
        document.selectday.EndMonth.value = <%=cal_date/100%100%>;
        document.selectday.EndDay.value   = <%=cal_date%100%>;
        msec  = (new Date("<%=cal_date/10000%>/<%=cal_date/100%100%>/<%=cal_date%100%>")).getTime();
        dt  =  new Date(msec - nmsec );
        year_result  = dt.getYear();
        if (year_result < 1900)
        {
            year_result  = year_result + 1900;
        }
        month_result = dt.getMonth() + 1;
        day_result   = dt.getDate();
        document.selectday.StartYear.value  = year_result;
        document.selectday.StartMonth.value = month_result;
        document.selectday.StartDay.value   = day_result;
    }

    document.getElementById("StartYearStore").value  = document.selectday.StartYear.value;
    document.getElementById("StartMonthStore").value = document.selectday.StartMonth.value;
    document.getElementById("StartDayStore").value   = document.selectday.StartDay.value;
    document.getElementById("EndYearStore").value    = document.selectday.EndYear.value;
    document.getElementById("EndMonthStore").value   = document.selectday.EndMonth.value;
    document.getElementById("EndDayStore").value     = document.selectday.EndDay.value;
    document.getElementById("Range").value=document.selectday.Range.value;
//    if (SubmitOK == "true" && (CompareSubmit == "true" || CompareMode == "false"))
//    {
//        document.getElementById("CompareSubmit").value ="true";
//        top.Main.mainFrame.location.href="sales_wait.html";
//    }
//    else
//    {
        document.getElementById("SubmitOK").value="false";
        top.Main.mainFrame.location.href="sales_page.html";
        document.selectstore.submit();
//    }
}
-->
</script>
