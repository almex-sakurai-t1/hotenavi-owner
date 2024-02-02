<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/getCalendar.jsp" %>
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
    String nextEndYearDate = "";
    String nextEndMonthDate = "";
    String beforEndYearDate = "";
    String beforEndMonthDate = "";
    if (paramDate == null || paramDate.trim().length() == 0) {
        paramDate = Integer.toString(start_date);
    }
    else
    {
        start_date = Integer.parseInt(paramDate);
    }
    int end_date    = start_date;
    if (end_date == cal_date)
    {
        end_date = Integer.parseInt(getBefore1MDateString(Integer.toString(end_date))); 
        if (end_date/100%100 == 12)
        {
            start_date = end_date - 1100;
        }
        else
        {
            start_date = end_date - 10000+100;
        }
        paramDate   = Integer.toString(start_date);
    }
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

    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;
    int end_year    = end_date/10000;
    int end_month   = end_date/100%100;
    int end_day     = end_date%100;
    paramEndDate    = Integer.toString(end_date);

    nextMonthDate   = getNextMonthDateString(paramDate);
    beforMonthDate  = getBeforeMonthDateString(paramDate);
    nextYearDate    = getNextYearDateString(paramDate);
    beforYearDate   = getBeforeYearDateString(paramDate);
    nextEndMonthDate   = getNextMonthDateString(paramEndDate);
    nextEndYearDate    = getNextYearDateString(paramEndDate);
    beforEndMonthDate   = getBeforeMonthDateString(paramEndDate);
    beforEndYearDate    = getBeforeYearDateString(paramEndDate);

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
    boolean nextYearLink   = true;
    boolean nextMonthLink  = true;

    if (Integer.parseInt(beforYearDate) < DATE_MIN)
    {
       beforYearLink = false;
    }
    if (Integer.parseInt(beforMonthDate) <  DATE_MIN)
    {
       beforMonthLink = false;
    }
    if (Integer.parseInt(nextEndYearDate) > cal_date)
    {
       nextYearLink = false;
    }
    if (Integer.parseInt(nextEndMonthDate) > cal_date)
    {
       nextMonthLink = false;
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
          <a <%if (beforYearLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=beforYearDate%>,<%=beforEndYearDate%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><<前年</a>&nbsp;
          <a <%if (beforMonthLink){%>class="blue" href="#" onclick="jumpCompareDate(<%=beforMonthDate%>,<%=beforEndMonthDate%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><前月</a>&nbsp;&nbsp;
          <a <%if (nextMonthLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextMonthDate%>,<%=nextEndMonthDate%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌月></a>&nbsp;
          <a <%if (nextYearLink)  {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextYearDate%>,<%=nextEndYearDate%>,'<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌年>></a>
        </td>
      </tr>
  </td>
</tr>
<tr>
  <TD class="size10" bgcolor="#A8BEBC" colspan=2 style="padding-left:30px">
    <input class="size12 ar" name="CompareStartYear" id="CompareStartYear" maxlength="4" size="4" value="<%=start_year%>" onclick="this.select();" onchange="checkCompareStartYear(this.value);" onchange="checkYear(this.value);">年
    <input class="size12 ar" name="CompareStartMonth" id="CompareStartMonth" maxlength="2" size="2" value="<%=start_month%>" onclick="this.select();" onchange="checkCompareStartMonth(this.value);" onchange="checkMonth(this.value);">月
    ～<br>
    <input class="size12 ar" id="ComparedisableEndYear" maxlength="4" size="4" value="<%=end_year%>" disabled>年
    <input class="size12 ar" id="ComparedisableEndMonth" maxlength="2" size="2" value="<%=end_month%>" disabled>月
    <input type="hidden" name="CompareEndYear"  id="CompareEndYear"  value="<%=end_year%>">
    <input type="hidden" name="CompareEndMonth" id="CompareEndMonth" value="<%=end_month%>">
    <input name="Compare" type="hidden" value="<%=CompareMode%>">
    <INPUT type="button" class="size12" value="照会" onclick="if(checkDate(checkCompareDate())){submitCompareDate();}">
  </TD>
</tr>
<script type="text/javascript">
<!--
var s_y = document.selectday.StartYear.value;
var s_m = document.selectday.StartMonth.value;
var e_y = document.selectday.EndYear.value;
var e_m = document.selectday.EndMonth.value;
var range = 0;
if (e_y == s_y)
{
   range = parseInt(e_m) - parseInt(s_m);
}
else
{
   range = parseInt(e_m)+12 - parseInt(s_m);
}
if (parseInt(document.selectday.CompareStartMonth.value) + range > 12)
{
   document.selectday.CompareEndYear.value = parseInt(document.selectday.CompareStartYear.value)  + 1;
   document.selectday.CompareEndMonth.value = parseInt(document.selectday.CompareStartMonth.value) - 12 + range;
}
else
{
   document.selectday.CompareEndMonth.value = parseInt(document.selectday.CompareStartMonth.value)  + range;
   document.selectday.CompareEndYear.value = parseInt(document.selectday.CompareStartYear.value);
}

document.getElementById("CompareStartYearStore").value  = document.selectday.CompareStartYear.value;
document.getElementById("CompareStartMonthStore").value = document.selectday.CompareStartMonth.value;
document.getElementById("CompareEndYearStore").value  = document.selectday.CompareEndYear.value;
document.getElementById("CompareEndMonthStore").value = document.selectday.CompareEndMonth.value;
document.getElementById("ComparedisableEndYear").value  = document.selectday.CompareEndYear.value;
document.getElementById("ComparedisableEndMonth").value = document.selectday.CompareEndMonth.value;



function jumpCompareDate(paramDate,paramEndDate,SubmitOK){
    document.getElementById("CompareStartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("CompareStartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("CompareEndYearStore").value  = Math.floor(parseInt(paramEndDate)/10000);
    document.getElementById("CompareEndMonthStore").value = Math.floor(parseInt(paramEndDate)/100)%100;
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
    var dates  = parseInt(years)*10000 + parseInt(months)*100 +1;

    if (dates > <%=cal_date%>)
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
        document.getElementById("CompareEndYearStore").value  = document.selectday.CompareEndYear.value;
        document.getElementById("CompareEndMonthStore").value = document.selectday.CompareEndMonth.value;
        document.getElementById("SubmitOK").value="false";
        top.Main.mainFrame.location.href="sales_page.html";
        document.selectstore.submit();
    }
}
function checkCompareDate(){


//入力されている年月より、その月の最終日付を算出
    var years  = document.selectday.CompareStartYear.value;
    var months = document.selectday.CompareStartMonth.value;
    var dates  = parseInt(years)*10000 + parseInt(months)*100 + 1;

    if (dates > <%=cal_date%>)
    {
        alert("先の日付は指定できません");
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        return false;
    }
    if (dates < <%=DATE_MIN%>)
    {
        alert("<%=DATE_MIN/10000%>年<%=DATE_MIN/100%100%>月<%=DATE_MIN%100%>日（5年前）から後の年月を指定してください");
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        return false;
    }
    return true;
}
function submitCompareDate(){
    document.getElementById('CompareStartYearStore').value  =document.selectday.CompareStartYear.value;
    document.getElementById('CompareStartMonthStore').value =document.selectday.CompareStartMonth.value;
    document.getElementById("CompareEndYearStore").value    =document.selectday.CompareEndYear.value;
    document.getElementById("CompareEndMonthStore").value   =document.selectday.CompareEndMonth.value;
    document.getElementById("SubmitOK").value               ="true";
    document.getElementById("Compare").value                ="true";
    document.getElementById("CompareSubmit").value          ="true";
    top.Main.mainFrame.location.href="sales_wait.html";
}
-->
</script>
