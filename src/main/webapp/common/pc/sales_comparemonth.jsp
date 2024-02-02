<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
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
	boolean dateError = false;
	String StartYear = ReplaceString.getParameter(request,"StartYear");
	if( StartYear != null && !CheckString.numCheck(StartYear))
	{
		dateError = true;
	}
	String StartMonth = ReplaceString.getParameter(request,"StartMonth");
	if( StartMonth != null && !CheckString.numCheck(StartMonth))
	{
		dateError = true;
	}
	if(dateError)
	{
		StartYear="0";
		StartMonth="0";
%>
		<script type="text/javascript">
		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
%>
<tr>
  <td colspan="2" bgcolor="#A8BEBC">
    <table>
      <tr>
        <td class="size10">
          ↑<small>比較対象の年月を指定して「照会」ボタンを押してください</small>
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
          <a <%if (beforYearLink) {%>class="blue" href="#" onclick="jumpCompareMonth(<%=beforYearDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>><<前年</a>&nbsp;
          <a <%if (beforMonthLink){%>class="blue" href="#" onclick="jumpCompareMonth(<%=beforMonthDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>><前月</a>&nbsp;&nbsp;
          <a <%if (nextMonthLink) {%>class="blue" href="#" onclick="jumpCompareMonth(<%=nextMonthDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>>翌月></a>&nbsp;
          <a <%if (nextYearLink)  {%>class="blue" href="#" onclick="jumpCompareMonth(<%=nextYearDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>>翌年>></a>
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
    String thisMonthDate = "";
    int thisMonthLastDate  = 0;
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
        thisMonthDate = Integer.toString(start_year * 10000 + tdDetail * 100 + 1);
        thisMonthLastDay  = getCalendar(thisMonthDate).getActualMaximum(Calendar.DATE);
        thisMonthLastDate  = (Integer.parseInt(thisMonthDate)/100)*100  + thisMonthLastDay;
        if ((start_year * 100 + tdDetail) == start_date/100)
        {
            if(CompareSubmit){ tdColor = tdColor + " pink";}
            else             { tdColor = tdColor + " gold";}
        }
        if((start_year * 100 + tdDetail) <= cal_date/100 && (start_year * 100 + tdDetail) >=DATE_MIN/100)
        {
%>
              <td id="Calender<%=tdDetail%>" class="ac<%=tdColor%> border" width="40px">
                <a href="#" class="ac<%=tdColor%>" onclick="jumpCompareMonth(<%=thisMonthLastDate%>,'<%=CompareSubmit%>')"><%=tdDetail%>月</a>
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
  <TD class="size10" align="center" nowrap  bgcolor="#A8BEBC" colspan=2>
    <input class="size12 ar" name="CompareStartYear"  id="CompareStartYear" maxlength="4" size="4" value="<%=start_year%>" onclick="this.select();" onchange="checkCompareYear(this.value);" onchange="checkCompareYear(this.value);">年
    <input class="size12 ar" name="CompareStartMonth" id="CompareStartMonth" maxlength="2" size="2" value="<%=start_month%>" onclick="this.select();" onchange="checkCompareMonth(this.value);" onchange="checkCompareMonth(this.value);">月
    <input class="size12 ar" name="CompareStartDay" type="hidden" id="CompareStartDay" value="0">
    <input name="Compare" type="hidden" value="<%=CompareMode%>">
    <INPUT type="button" class="size12" value="照会"  onclick="if(checkCompareDate()){submitCompareMonth();}">
  </TD>
</tr>
<script type="text/javascript">
<!--
function jumpCompareMonth(paramDate,CompareSubmit){
    document.getElementById('StartYearStore').value  =document.selectday.StartYear.value;
    document.getElementById('StartMonthStore').value =document.selectday.StartMonth.value;
    document.getElementById('StartDayStore').value   =document.selectday.StartDay.value;
    document.getElementById("EndYearStore").value    =document.selectday.StartYear.value;
    document.getElementById("EndMonthStore").value   =document.selectday.StartMonth.value;
    document.getElementById("EndDayStore").value     =new Date(document.selectday.StartYear.value, document.selectday.StartMonth.value, 0).getDate();
    document.getElementById("CompareStartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("CompareStartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("CompareStartDayStore").value   = 1;
    document.getElementById("CompareEndYearStore").value    = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("CompareEndMonthStore").value   = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("CompareEndDayStore").value     = parseInt(paramDate)%100;
    if (CompareSubmit == "true")
    {
        top.Main.mainFrame.location.href="sales_wait.html";
    }
    else
    {
        document.selectstore.submit();
        top.Main.mainFrame.location.href="sales_page.html";
    }
}
function submitCompareMonth(){
    document.getElementById('StartYearStore').value  =document.selectday.StartYear.value;
    document.getElementById('StartMonthStore').value =document.selectday.StartMonth.value;
    document.getElementById('StartDayStore').value   =document.selectday.StartDay.value;
    document.getElementById("EndYearStore").value    =document.selectday.StartYear.value;
    document.getElementById("EndMonthStore").value   =document.selectday.StartMonth.value;
    document.getElementById("EndDayStore").value     =new Date(document.selectday.StartYear.value, document.selectday.StartMonth.value, 0).getDate();
    document.getElementById('CompareStartYearStore').value  =document.selectday.CompareStartYear.value;
    document.getElementById('CompareStartMonthStore').value =document.selectday.CompareStartMonth.value;
    document.getElementById('CompareStartDayStore').value   =document.selectday.CompareStartDay.value;
    document.getElementById("CompareEndYearStore").value    =document.selectday.CompareStartYear.value;
    document.getElementById("CompareEndMonthStore").value   =document.selectday.CompareStartMonth.value;
    document.getElementById("CompareEndDayStore").value     =new Date(document.selectday.CompareStartYear.value, document.selectday.CompareStartMonth.value, 0).getDate();
    document.getElementById("Compare").value                ="true";
    document.getElementById("CompareSubmit").value          ="true";
    top.Main.mainFrame.location.href="sales_wait.html";
}
function checkCompareYear(paramYear){
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
    return true;
}
function checkCompareMonth(paramMonth){
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
    return true;
}
function checkCompareDate(){
//入力されている年月より、その月の最終日付を算出
    var years  = document.selectday.StartYear.value;
    var months = document.selectday.StartMonth.value;
    var dates  = parseInt(years)*100 + parseInt(months);

    if (dates > <%=cal_date/100%>)
    {
        document.selectday.StartYear.value =<%=StartYear%>;
        document.selectday.StartMonth.value =<%=StartMonth%>;
        return false;
    }
    if (dates < <%=DATE_MIN/100%>)
    {
        document.selectday.StartYear.value =<%=StartYear%>;
        document.selectday.StartMonth.value =<%=StartMonth%>;
        return false;
    }

//入力されている年月より、その月の最終日付を算出
    years  = document.selectday.CompareStartYear.value;
    months = document.selectday.CompareStartMonth.value;
    dates  = parseInt(years)*100 + parseInt(months);

//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
    if (dates > <%=cal_date/100%>)
    {
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        return false;
    }
    if (dates < <%=DATE_MIN/100%>)
    {
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        return false;
    }
    return true;
}
-->
</script>
