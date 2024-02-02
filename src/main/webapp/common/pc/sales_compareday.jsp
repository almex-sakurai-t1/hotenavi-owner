<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/getCalendar.jsp" %>
<%@ include file="../../common/pc/getNationalHolidayName.jsp" %>
<%
    String loginHotelId  = (String)session.getAttribute("LoginHotelId");

    // ���ݓ��t
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;

    // �v����t
    int cal_date  = ownerinfo.Addupdate;
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;
    if  (cal_date == 0) 
    {
        cal_date = now_date;
    }
    int   start_date = cal_date;

    //���t��ύX���ďƉ���N���b�N���ē��t���󂯓n���ꂽ�ꍇ
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

    //�O�N�E���N�E�O���E�����@���N���b�N���ē��t���󂯓n���ꂽ�ꍇ
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

    int DATE_MIN    = 20050101; // �ŏ����t
    DATE_MIN        = Integer.parseInt(getMinDateString(Integer.toString(cal_date)));
%>
<%
    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;

    //��
    String thisMonth = paramDate.substring(4, 6);
    ArrayList<String> list = getDateList(getCalendar(paramDate));

    //�����N�𖳌��`�F�b�N
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
	String StartDay = ReplaceString.getParameter(request,"StartDay");
	if( StartDay != null && !CheckString.numCheck(StartDay))
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
          ��<small>��r�Ώۂ̓��t���w�肵�āu�Ɖ�v�{�^���������Ă�������</small>
        </td>
      </tr>
      <tr>
        <td>
          <table width="100%">
            <tr>
              <th class="sunday">
                ��r����
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
          <a <%if (beforYearLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=beforYearDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>><<�O�N</a>&nbsp;
          <a <%if (beforMonthLink){%>class="blue" href="#" onclick="jumpCompareDate(<%=beforMonthDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>><�O��</a>&nbsp;&nbsp;
          <a <%if (nextMonthLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextMonthDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>>����></a>&nbsp;
          <a <%if (nextYearLink)  {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextYearDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>>���N>></a>
        </td>
      </tr>
      <tr>
        <td class="cal_title">
           <%=paramDate.substring(0, 4)%>�N <%=Integer.parseInt(thisMonth)%>��
        </td>
      </tr>
      <tr>
        <td class="size10 ac">
          <table border="0" bordercolor="#000000">
            <tr>
<%
    String dayOfWeek = "�����ΐ��؋��y";
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
                String replaceStr = " title=\"��\" ";
                tdTitle = replaceStr.replace("��", nationalHolidayName);
            } else if (i % 7 == 0) {
                tdColor = " red";
            } else if ((i + 1) % 7 == 0) {
                tdColor = " blue";
            }
            tdDetail = Integer.parseInt(dateStr.substring(6, 8));
        }
        if (Integer.parseInt(dateStr) == start_date)
        {
            if (Integer.parseInt(dateStr) == start_date)
            {
                if(CompareSubmit){ tdColor = tdColor + " pink";}
                 else             { tdColor = tdColor + " gold";}
            }
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
                <a href="#" class="ac<%=tdColor%>" onclick="jumpCompareDate(<%=(Integer.parseInt(paramDate)/100)*100+tdDetail%>,'<%=CompareSubmit%>');"><%=tdDetail%></a>
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
          <a <%if (beforWeekLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=beforWeekDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>><<�O�T</a>&nbsp;
          <a <%if (beforDayLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=beforDayDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>><�O��</a>&nbsp;&nbsp;
          <a <%if (nextDayLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextDayDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>>����></a>&nbsp;
          <a <%if (nextWeekLink) {%>class="blue" href="#" onclick="jumpCompareDate(<%=nextWeekDate%>,'<%=CompareSubmit%>');"<%}else{%>class="gray"<%}%>>���T>></a>
        </td>
      </tr>
    </table>
  </td>
</tr>
<tr>
<form name="Compareselectday" action="salesdisp_select.jsp?Kind=<%=paramKind%>" method="post" target="mainFrame">
  <TD class="size10" align="left" nowrap  bgcolor="#A8BEBC" colspan=2>
    <input class="size12 ar" name="CompareStartYear"  id="CompareStartYear" maxlength="4" size="4" value="<%=start_year%>" onclick="this.select();" onchange="checkCompareYear(this.value);" onchange="checkCompareYear(this.value);">�N
    <input class="size12 ar" name="CompareStartMonth" id="CompareStartMonth" maxlength="2" size="2" value="<%=start_month%>" onclick="this.select();" onchange="checkCompareMonth(this.value);" onchange="checkCompareMonth(this.value);">��
    <input class="size12 ar" name="CompareStartDay"   id="CompareStartDay" maxlength="2" size="2" value="<%=start_day%>" onclick="this.select();" onchange="checkCompareDay(this.value);" onchange="checkCompareDay(this.value);">��
    <input name="Compare" type="hidden" value="<%=CompareMode%>">
    <INPUT type="button" class="size12" value="�Ɖ�"  onclick="if(checkCompareDate()){submitCompareDate();}">
  </TD>
</tr>

<script type="text/javascript">
<!--
function jumpCompareDate(paramDate,CompareSubmit){
    document.getElementById("CompareStartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("CompareStartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("CompareStartDayStore").value   = parseInt(paramDate)%100;
    document.getElementById("CompareEndYearStore").value    = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("CompareEndMonthStore").value   = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("CompareEndDayStore").value     = parseInt(paramDate)%100;
    document.getElementById("Range").value=1;
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
function submitCompareDate(){
    document.getElementById('StartYearStore').value  =document.selectday.StartYear.value;
    document.getElementById('StartMonthStore').value =document.selectday.StartMonth.value;
    document.getElementById('StartDayStore').value   =document.selectday.StartDay.value;
    document.getElementById("EndYearStore").value    =document.selectday.StartYear.value;
    document.getElementById("EndMonthStore").value   =document.selectday.StartMonth.value;
    document.getElementById("EndDayStore").value     =document.selectday.StartDay.value;
    document.getElementById('CompareStartYearStore').value  =document.selectday.CompareStartYear.value;
    document.getElementById('CompareStartMonthStore').value =document.selectday.CompareStartMonth.value;
    document.getElementById('CompareStartDayStore').value   =document.selectday.CompareStartDay.value;
    document.getElementById("CompareEndYearStore").value    =document.selectday.CompareStartYear.value;
    document.getElementById("CompareEndMonthStore").value   =document.selectday.CompareStartMonth.value;
    document.getElementById("CompareEndDayStore").value     =document.selectday.CompareStartDay.value;
    document.getElementById("SubmitOK").value               ="true";
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
function checkCompareDay(paramDay){
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
    return true;
}
function checkCompareDate(){
//���͂���Ă���N�����A���̌��̍ŏI���t���Z�o
    var years  = document.selectday.StartYear.value;
    var months = document.selectday.StartMonth.value;
    var days   = document.selectday.StartDay.value;
    var dates  = parseInt(years)*10000 + parseInt(months)*100 + parseInt(days);

    var lastday_s = monthday(years,months);

//���͂���Ă��錎�ɂ���āA�ŏI���t��ύX�B���肦�Ȃ����t�̏ꍇ�ɂ́A�ŏI���t�ɋ����I�ɕϊ��B
    if (lastday_s < document.selectday.StartDay.value) 
    {
        document.selectday.StartYear.value =<%=StartYear%>;
        document.selectday.StartMonth.value =<%=StartMonth%>;
        document.selectday.StartDay.value =<%=StartDay%>;
        return false;
    }
    if (dates > <%=cal_date%>)
    {
        alert("��̓��t�͎w��ł��܂���");
        document.selectday.StartYear.value =<%=StartYear%>;
        document.selectday.StartMonth.value =<%=StartMonth%>;
        document.selectday.StartDay.value =<%=StartDay%>;
        return false;
    }
    if (dates < <%=DATE_MIN%>)
    {
        alert("<%=DATE_MIN/10000%>�N<%=DATE_MIN/100%100%>��<%=DATE_MIN%100%>���i5�N�O�j�����̓��t���w�肵�Ă�������");
        document.selectday.StartYear.value =<%=StartYear%>;
        document.selectday.StartMonth.value =<%=StartMonth%>;
        document.selectday.StartDay.value =<%=StartDay%>;
        return false;
    }

//���͂���Ă���N�����A���̌��̍ŏI���t���Z�o
    years  = document.selectday.CompareStartYear.value;
    months = document.selectday.CompareStartMonth.value;
    days   = document.selectday.CompareStartDay.value;
    dates  = parseInt(years)*10000 + parseInt(months)*100 + parseInt(days);

    lastday_s = monthday(years,months);

//���͂���Ă��錎�ɂ���āA�ŏI���t��ύX�B���肦�Ȃ����t�̏ꍇ�ɂ́A�ŏI���t�ɋ����I�ɕϊ��B
    if (lastday_s < document.selectday.CompareStartDay.value) 
    {
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        document.selectday.CompareStartDay.value =<%=start_day%>;
        return false;
    }
    if (dates > <%=cal_date%>)
    {
        alert("��̓��t�͎w��ł��܂���");
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        document.selectday.CompareStartDay.value =<%=start_day%>;
        return false;
    }
    if (dates < <%=DATE_MIN%>)
    {
        alert("<%=DATE_MIN/10000%>�N<%=DATE_MIN/100%100%>��<%=DATE_MIN%100%>���i5�N�O�j�����̓��t���w�肵�Ă�������");
        document.selectday.CompareStartYear.value =<%=start_year%>;
        document.selectday.CompareStartMonth.value =<%=start_month%>;
        document.selectday.CompareStartDay.value =<%=start_day%>;
        return false;
    }
    return true;
}
-->
</script>
