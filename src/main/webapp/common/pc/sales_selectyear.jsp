<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/getCalendar.jsp" %>
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
    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;
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

    String paramYearMode = ReplaceString.getParameter(request,"YearMode");
    if (paramYearMode == null)
    {
        paramYearMode = "false";	
    }
    if (paramYearMode.equals("false"))
    {
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
            start_year  = start_date/10000;
            start_month = start_date/100%100;
            start_day   = start_date%100;
            paramDate   = Integer.toString(start_date);
        }
    }

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
          <a <%if (beforYearLink) {%>class="blue" href="#" onclick="jumpDate(<%=beforYearDate%>,<%=beforEndYearDate%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><<前年</a>&nbsp;
          <a <%if (beforMonthLink){%>class="blue" href="#" onclick="jumpDate(<%=beforMonthDate%>,<%=beforEndMonthDate%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>><前月</a>&nbsp;&nbsp;
          <a <%if (nextMonthLink) {%>class="blue" href="#" onclick="jumpDate(<%=nextMonthDate%>,<%=nextEndMonthDate%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌月></a>&nbsp;
          <a <%if (nextYearLink)  {%>class="blue" href="#" onclick="jumpDate(<%=nextYearDate%>,<%=nextEndYearDate%>,'<%=CompareSubmit%>','<%=CompareMode%>','<%=SubmitOK%>');"<%}else{%>class="gray"<%}%>>翌年>></a>
        </td>
      </tr>
    </table>
  </td>
</tr>
<tr>
<form name="selectday" action="salesdisp_select.jsp?Kind=<%=paramKind%>" method="post" target="mainFrame">
  <TD class="size10"  bgcolor="#BBBBBB" colspan=2 style="padding-left:30px">
    <input class="size12 ar" name="StartYear" id="StartYear" maxlength="4" size="4" value="<%=start_year%>" onclick="this.select();" onchange="checkStartYear(this.value);">年
    <input class="size12 ar" name="StartMonth" id="StartMonth" maxlength="2" size="2" value="<%=start_month%>" onclick="this.select();" onchange="checkStartMonth(this.value);">月
    ～<br>
    <input class="size12 ar" name="EndYear" id="EndYear" maxlength="4" size="4" value="<%=end_year%>" onclick="this.select();" onchange="checkEndYear(this.value);">年
    <input class="size12 ar" name="EndMonth" id="EndMonth" maxlength="2" size="2" value="<%=end_month%>" onclick="this.select();" onchange="checkEndMonth(this.value);">月
<%
    if (!CompareMode)
    {
%>
    <input name="CompareStartYear" id="CompareStartYear" type="hidden" value="<%=start_year%>">
    <input name="CompareStartMonth" id="CompareStartMonth" type="hidden" value="<%=start_month%>">
    <input name="CompareEndYear" id="CompareEndYear" type="hidden" value="<%=end_year%>">
    <input name="CompareEndMonth" id="CompareEndMonth" type="hidden" value="<%=end_month%>">
    <input name="Compare" type="hidden" value="<%=CompareMode%>">
    <INPUT type="button" class="size12" value="照会" onclick="if(checkDate()){document.getElementById('CompareSubmit').value='true';document.selectstore.submit();}">
<%
    }
%>
  </TD>
</tr>
<%
    if (CompareMode)
    {
%>
      <%-- 比較対象年月選択画面 --%>
      <jsp:include page="sales_compareyear.jsp" flush="true" />
<%
    }
    else
    {
%>
<tr>
  <TD class="size10" align="center" nowrap  bgcolor="#BBBBBB" colspan=2 height="100%" valign="top">
    <input name="CompareStart" type="button" class="inoutbtn" id="CompareStart" value="比較分析" onClick="compareSet();">
  </TD>
</tr>
<%
    }
%>
</form>

<script type="text/javascript">
<!--
//    originalSet();
    if (document.selectday.EndMonth.value == 12)
    {
       document.selectday.StartMonth.value = 1;
       document.selectday.StartYear.value = document.selectday.EndYear.value;
    }
    else
    {
       document.selectday.StartMonth.value = parseInt(document.selectday.EndMonth.value) + 1;
       document.selectday.StartYear.value = parseInt(document.selectday.EndYear.value) - 1;
    }
    rangeSet();

function jumpDate(paramDate,paramEndDate,CompareSubmit,CompareMode,SubmitOK){
    document.getElementById("YearMode").value = "true";
    document.getElementById("StartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
    document.getElementById("StartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
    document.getElementById("EndYearStore").value  = Math.floor(parseInt(paramEndDate)/10000);
    document.getElementById("EndMonthStore").value = Math.floor(parseInt(paramEndDate)/100)%100;
    if (CompareMode == "false")
    {
        document.getElementById("CompareStartYearStore").value  = Math.floor(parseInt(paramDate)/10000);
        document.getElementById("CompareStartMonthStore").value = Math.floor(parseInt(paramDate)/100)%100;
        document.getElementById("CompareEndYearStore").value  = Math.floor(parseInt(paramEndDate)/10000);
        document.getElementById("CompareEndMonthStore").value = Math.floor(parseInt(paramEndDate)/100)%100;
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
        document.selectday.StartYear.value = document.getElementById("StartYearStore").value;
        return false;
    }
    if (paramYear <  <%=DATE_MIN/10000%>)
    {
        document.selectday.StartYear.value = document.getElementById("StartYearStore").value;
        return false;
    }
    if (paramYear >  <%=cal_date/10000%>)
    {
        document.selectday.StartYear.value = document.getElementById("StartYearStore").value;
        return false;
    }
    if (document.selectday.StartMonth.value == 1)
    {
       document.selectday.EndYear.value = document.selectday.StartYear.value;
    }
    else
    {
       document.selectday.EndYear.value = parseInt(document.selectday.StartYear.value) + 1;
    }
    if(!checkDate())
    {
       originalSet();
       return false;
    }
    return true;
}
function checkStartMonth(paramMonth){
    if (isNaN(paramMonth))
    {
        document.selectday.StartMonth.value = document.getElementById("StartMonthStore").value;
        return false;
    }
    if (paramMonth <  1 || paramMonth > 12)
    {
        document.selectday.StartMonth.value = document.getElementById("StartMonthStore").value;
        return false;
    }
    if (document.selectday.StartMonth.value == 1)
    {
       document.selectday.EndYear.value = document.selectday.StartYear.value;
       document.selectday.EndMonth.value = 12;
    }
    else
    {
       document.selectday.EndMonth.value = parseInt(document.selectday.StartMonth.value) - 1;
       document.selectday.EndYear.value = parseInt(document.selectday.StartYear.value) + 1;
    }

    if(!checkDate())
    {
       originalSet();
       return false;
    }
    return true;
}
function checkEndYear(paramYear){
    if (isNaN(paramYear))
    {
        document.selectday.EndYear.value = document.getElementById("EndYearStore").value;
        return false;
    }
    if (paramYear <  <%=DATE_MIN/10000%>)
    {
        document.selectday.EndYear.value = document.getElementById("EndYearStore").value;
        return false;
    }
    if (paramYear >  <%=cal_date/10000%>)
    {
        document.selectday.EndYear.value = document.getElementById("EndYearStore").value;
        return false;
    }
    if (document.selectday.EndMonth.value == 12)
    {
       document.selectday.StartYear.value = document.selectday.EndYear.value;
    }
    else
    {
       document.selectday.StartYear.value = parseInt(document.selectday.EndYear.value) - 1;
    }
    if(!checkDate())
    {
       originalSet();
       return false;
    }
    return true;
}
function checkEndMonth(paramMonth){
    if (isNaN(paramMonth))
    {
        document.selectday.EndMonth.value = document.getElementById("EndMonthStore").value;
        return false;
    }
    if (paramMonth <  1 || paramMonth > 12)
    {
        document.selectday.EndMonth.value = document.getElementById("EndMonthStore").value;
        return false;
    }
    if (document.selectday.EndMonth.value == 12)
    {
       document.selectday.StartMonth.value = 1;
       document.selectday.StartYear.value = document.selectday.EndYear.value;
    }
    else
    {
       document.selectday.StartMonth.value = parseInt(document.selectday.EndMonth.value) + 1;
       document.selectday.StartYear.value = parseInt(document.selectday.EndYear.value) - 1;
    }
    if(!checkDate())
    {
       originalSet();
       return false;
    }
    return true;
}
function checkDate(){
    var years  = document.selectday.StartYear.value;
    var months = document.selectday.StartMonth.value;
    var dates  = parseInt(years)*10000 + parseInt(months)*100 + 1;

    var yeare  = document.selectday.EndYear.value;
    var monthe = document.selectday.EndMonth.value;
    var datee  = parseInt(yeare)*10000 + parseInt(monthe)*100 + 1;

    if (dates > <%=cal_date%>)
    {
        alert("先の日付は指定できません");
        return false;
    }
    if (dates < <%=DATE_MIN%>)
    {
        alert("<%=DATE_MIN/10000%>年<%=DATE_MIN/100%100%>月<%=DATE_MIN%100%>日（5年前）から後の日付を指定してください");
        return false;
    }
    if (datee > <%=cal_date%>)
    {
        alert("先の日付は指定できません");
        return false;
    }
    if (datee < <%=DATE_MIN%>)
    {
        alert("<%=DATE_MIN/10000%>年<%=DATE_MIN/100%100%>月<%=DATE_MIN%100%>日（5年前）から後の日付を指定してください");
        return false;
    }
    if (datee < dates)
    {
        alert("期間指定（開始日付≦終了日付）を正しく入力してください");
        return false;
    }
    if (datee == dates)
    {
        alert("年月範囲を指定してください");
        return false;
    }
    if (datee >= dates + 10000)
    {
        alert("期間指定は12ヶ月以内でお願いします");
        return false;
    }
    rangeSet();
    return true;
}
function rangeSet(){
    document.getElementById("YearMode").value = "true";
    document.getElementById("StartYearStore").value  = document.selectday.StartYear.value;
    document.getElementById("StartMonthStore").value = document.selectday.StartMonth.value;
    document.getElementById("EndYearStore").value    = document.selectday.EndYear.value;
    document.getElementById("EndMonthStore").value   = document.selectday.EndMonth.value;
}
function originalSet(){
    document.selectday.StartYear.value = document.getElementById("StartYearStore").value;
    document.selectday.StartMonth.value = document.getElementById("StartMonthStore").value;
    document.selectday.EndYear.value = document.getElementById("EndYearStore").value;
    document.selectday.EndMonth.value = document.getElementById("EndMonthStore").value;
}
function compareSet(){
    document.getElementById('Compare').value='true';
    document.getElementById('CompareSubmit').value='false';
    document.getElementById("CompareStartYearStore").value  = parseInt(document.selectday.StartYear.value) -1;
    document.getElementById("CompareStartMonthStore").value = document.selectday.StartMonth.value;
    document.getElementById("CompareEndYearStore").value    = parseInt(document.selectday.EndYear.value) -1;
    document.getElementById("CompareEndMonthStore").value   = document.selectday.EndMonth.value;
    document.selectstore.submit();
}
-->
</script>
