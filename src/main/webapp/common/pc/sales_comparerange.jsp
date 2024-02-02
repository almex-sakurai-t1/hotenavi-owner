<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/getCalendar.jsp" %>
<%@ include file="../../common/pc/getNationalHolidayName.jsp" %>
<%
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");

    String paramKind   = ReplaceString.getParameter(request,"Kind");
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

    //日付を変更して照会をクリックして日付が受け渡された場合
    String startYear  = ReplaceString.getParameter(request,"CompareStartYear");
    String startMonth = ReplaceString.getParameter(request,"CompareStartMonth");
    String startDay   = ReplaceString.getParameter(request,"CompareStartDay");
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

    String endYear  = ReplaceString.getParameter(request,"CompareEndYear");
    String endMonth = ReplaceString.getParameter(request,"CompareEndMonth");
    String endDay   = ReplaceString.getParameter(request,"CompareEndDay");
    if( endYear != null && endMonth != null && endDay != null )
    {
        end_date = (Integer.valueOf(endYear).intValue() * 10000) + (Integer.valueOf(endMonth).intValue() * 100) + Integer.valueOf(endDay).intValue();
    }
    if (end_date == 0)
    {
        end_date  = start_date;
    }

    String dateRange = ReplaceString.getParameter(request,"Range");
    if (dateRange!=null)
    {
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
        if (!dateRange.equals("0"))
        {
            paramEndDate = getAddDayDateString(paramDate,Integer.parseInt(dateRange)-1);
            end_date     = Integer.parseInt(paramEndDate);
            if (end_date > cal_date)
            {
               end_date       = cal_date;  //最終日が、計上日付より大きい場合は、開始日付を逆算で求める
               paramDate      = getAddDayDateString(Integer.toString(end_date),1-Integer.parseInt(dateRange));
               start_date     = Integer.parseInt(paramDate);
            }
        }
    }


    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;
    int end_year    = end_date/10000;
    int end_month   = end_date/100%100;
    int end_day     = end_date%100;
    paramEndDate    = Integer.toString(end_date);
    String maxStartDate = getAddDayDateString(Integer.toString(cal_date),1-Integer.parseInt(dateRange));

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
%>
<tr>
  <td colspan="2" bgcolor="#A8BEBC">
    <table>
      <tr>
        <td class="size10">
          ↑<small>比較対象の日付を指定して「照会」ボタンを押してください</small>
        </td>
      </tr>
      <tr>
        <td>
          <table width="100%">
            <tr>
              <th class="sunday">
                比較分析
              </th>
              <td width="10px">
                <a href="#"><image src="../../common/pc/image/closex.gif" onClick="document.getElementById('Compare').value='false';document.getElementById('CompareSubmit').value='false';document.selectstore.submit();" border="0"></a>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td class="size10" align="center">
          <a <%if (beforYearLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=beforYearDate%>,<%=date_range%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><<前年</a>&nbsp;
          <a <%if (beforMonthLink){%>class="blue" href="#" onclick="jumpCompareDate(<%=beforMonthDate%>,<%=date_range%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><前月</a>&nbsp;&nbsp;
          <a <%if (nextMonthLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextMonthDate%>,<%=date_range%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌月></a>&nbsp;
          <a <%if (nextYearLink)  {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextYearDate%>,<%=date_range%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌年>></a>
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
            if(SubmitOK&&CompareSubmit){ tdColor = tdColor + " pink";}
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
                <a href="#" class="ac<%=tdColor%>" onclick="jumpCompareDate(<%=(Integer.parseInt(paramDate)/100)*100+tdDetail%>,<%=date_range%>,'<%=SubmitOK%>');"><%=tdDetail%></a>
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
          <a <%if (beforWeekLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=beforWeekDate%>,<%=date_range%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><<前週</a>&nbsp;
          <a <%if (beforDayLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=beforDayDate%>,<%=date_range%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><前日</a>&nbsp;&nbsp;
          <a <%if (nextDayLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextDayDate%>,<%=date_range%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌日></a>&nbsp;
          <a <%if (nextWeekLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextWeekDate%>,<%=date_range%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌週>></a>
        </td>
      </tr>
    </table>
  </td>
</tr>
<tr>
  <TD class="size10" align="left" nowrap  bgcolor="#A8BEBC" colspan=2 style="padding-left:3px">
    <input class="size12 ar" name="CompareStartYear" id="CompareStartYear" maxlength="4" size="4" value="<%=start_year%>" onclick="this.select();" onchange="checkCompareStartYear(this.value);" onchange="checkYear(this.value);">年
    <input class="size12 ar" name="CompareStartMonth" id="CompareStartMonth" maxlength="2" size="2" value="<%=start_month%>" onclick="this.select();" onchange="checkCompareStartMonth(this.value);" onchange="checkMonth(this.value);">月
    <input class="size12 ar" name="CompareStartDay" id="CompareStartDay" maxlength="2" size="2" value="<%=start_day%>" onclick="this.select();" onchange="checkCompareStartDay(this.value);" onchange="checkDay(this.value);">日
    ～<br>
    <input class="size12 ar" id="ComparedisableEndYear" maxlength="4" size="4" value="<%=end_year%>" disabled>年
    <input class="size12 ar" id="ComparedisableEndMonth" maxlength="2" size="2" value="<%=end_month%>" disabled>月
    <input class="size12 ar" id="ComparedisableEndDay" maxlength="2" size="2" value="<%=end_day%>" disabled>日
    <input type="hidden" name="CompareEndYear"  id="CompareEndYear"  value="<%=end_year%>">
    <input type="hidden" name="CompareEndMonth" id="CompareEndMonth" value="<%=end_month%>">
    <input type="hidden" name="CompareEndDay"   id="CompareEndDay"   value="<%=end_day%>">
    <input name="Compare" type="hidden" value="<%=CompareMode%>">
    <INPUT type="button" class="size12" value="照会" onclick="if(checkDate(checkCompareDate())){submitCompareDate();}">
  </TD>
</tr>
<script type="text/javascript">
<!--
function jumpCompareDate(paramDate,Range,SubmitOK){
    document.getElementById("CompareStartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("CompareStartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("CompareStartDayStore").value   = parseInt(paramDate)%100;
    document.getElementById("Range").value   = Range;
    if (SubmitOK == "true")
    {
        top.Main.mainFrame.location.href="sales_wait.html";
    }
    else
    {
        top.Main.mainFrame.location.href="sales_page.html";
        document.selectstore.submit();
    }
}
function checkCompareStartYear(paramYear){
    if (isNaN(paramYear))
    {
        document.selectday.CompareStartYear.value =<%=start_year%>;
        return false;
    }
    if (paramYear <  <%=DATE_MIN/10000%>)
    {
        document.selectday.CompareStartYear.value =<%=start_year%>;
        return false;
    }
    if (paramYear >  <%=cal_date/10000%>)
    {
        document.selectday.CompareStartYear.value =<%=start_year%>;
        return false;
    }
    ComparedateSetfromRange();
    return true;
}
function checkCompareStartMonth(paramMonth){
    if (isNaN(paramMonth))
    {
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        return false;
    }
    if (paramMonth <  1 || paramMonth > 12)
    {
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        return false;
    }
    ComparedateSetfromRange();
    return true;
}
function checkCompareStartDay(paramDay){
    if (isNaN(paramDay))
    {
        document.selectday.CompareStartDay.value =<%=start_day%>;
        return false;
    }
    if (paramDay <  1 || paramDay > 31)
    {
        document.selectday.CompareStartDay.value =<%=start_day%>;
        return false;
    }
    ComparedateSetfromRange();
    return true;
}
function ComparedateSetfromRange(){
    var years  = document.selectday.CompareStartYear.value;
    var months = document.selectday.CompareStartMonth.value;
    var days   = document.selectday.CompareStartDay.value;
    var dates  = parseInt(years)*10000 + parseInt(months)*100 + parseInt(days);
    var lastday_s = monthday(years,months);
    if (lastday_s < document.selectday.CompareStartDay.value) 
    {
    }
    else
    if (dates > <%=maxStartDate%>)
    {
    }
    else
    if (dates < <%=DATE_MIN%>)
    {
    }
    else
    {
        document.getElementById("CompareStartYearStore").value  = document.selectday.CompareStartYear.value;
        document.getElementById("CompareStartMonthStore").value = document.selectday.CompareStartMonth.value;
        document.getElementById("CompareStartDayStore").value   = document.selectday.CompareStartDay.value;
        document.getElementById("SubmitOK").value="false";
        top.Main.mainFrame.location.href="sales_page.html";
        document.selectstore.submit();
    }
}
function checkCompareDate(){


//入力されている年月より、その月の最終日付を算出
    var years  = document.selectday.CompareStartYear.value;
    var months = document.selectday.CompareStartMonth.value;
    var days   = document.selectday.CompareStartDay.value;
    var dates  = parseInt(years)*10000 + parseInt(months)*100 + parseInt(days);
    var lastday_s = monthday(years,months);

//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
    if (lastday_s < document.selectday.CompareStartDay.value) 
    {
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        document.selectday.CompareStartDay.value =<%=start_day%>;
        return false;
    }
    if (dates > <%=maxStartDate%>)
    {
        alert("先の日付は指定できません");
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        document.selectday.CompareStartDay.value =<%=start_day%>;
        return false;
    }
    if (dates < <%=DATE_MIN%>)
    {
        alert("<%=DATE_MIN/10000%>年<%=DATE_MIN/100%100%>月<%=DATE_MIN%100%>日（5年前）から後の日付を指定してください");
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        document.selectday.CompareStartDay.value =<%=start_day%>;
        return false;
    }
    return true;
}
function submitCompareDate(){
    document.getElementById('CompareStartYearStore').value  =document.selectday.CompareStartYear.value;
    document.getElementById('CompareStartMonthStore').value =document.selectday.CompareStartMonth.value;
    document.getElementById('CompareStartDayStore').value   =document.selectday.CompareStartDay.value;
    document.getElementById("CompareEndYearStore").value    =document.selectday.CompareEndYear.value;
    document.getElementById("CompareEndMonthStore").value   =document.selectday.CompareEndMonth.value;
    document.getElementById("CompareEndDayStore").value     =document.selectday.CompareEndDay.value;
    document.getElementById("SubmitOK").value               ="true";
    document.getElementById("Compare").value                ="true";
    document.getElementById("CompareSubmit").value          ="true";
    top.Main.mainFrame.location.href="sales_wait.html";
}
-->
</script>
