<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

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
	boolean dateError = false;
    param_year  = ReplaceString.getParameter(request,"StartYear");
    if( param_year != null && !CheckString.numCheck(param_year))
	{
		dateError = true;
	}
    param_month = ReplaceString.getParameter(request,"StartMonth");
    if( param_month != null && !CheckString.numCheck(param_month))
	{
		dateError = true;
	}
    param_day   = ReplaceString.getParameter(request,"StartDay");
	if( param_day != null && !CheckString.numCheck(param_day))
	{
		dateError = true;
	}
	if(dateError)
	{
%>
		<script type="text/javascript">
		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
    if( param_year != null && param_month != null && param_day != null )
    {
        ymd = (Integer.valueOf(param_year).intValue() * 10000) + (Integer.valueOf(param_month).intValue() * 100) + Integer.valueOf(param_day).intValue();
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
<script type="text/javascript">
function setDay(obj){
	obj = obj.form;
	var years = parseInt(obj.Year.options[obj.Year.selectedIndex].value,10);
	var months = parseInt(obj.Month.options[obj.Month.selectedIndex].value,10);
	var days = parseInt(obj.Day.options[obj.Day.selectedIndex].value,10);

	var lastday = monthday(years,months);

//入力されている月によって、最終日付を変更。ありえない日付の場合には、最終日付に強制的に変換。
	var itemnum = obj.Day.length;
	if (lastday - 1 < obj.Day.selectedIndex) {
		obj.Day.selectedIndex = lastday - 1;
		obj.Day.options[obj.Day.selectedIndex].value = lastday;
	}
	obj.Day.length = lastday;
	for (cnt = itemnum + 1;cnt <= lastday;cnt++) {
		obj.Day.options[cnt - 1].text = cnt;
		obj.Day.options[cnt - 1].value = cnt;
	}
}

function monthday(years,months){
	var lastday = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	if (((years % 4 == 0) && (years % 100 != 0)) || (years % 400 == 0)){
		lastday[1] = 29;
	}
	return lastday[months - 1];
}

function validation(){
	var input_year = parseInt(selectday.Year.options[selectday.Year.selectedIndex].value,10);
	var input_month = parseInt(selectday.Month.options[selectday.Month.selectedIndex].value,10);
	var input_day = parseInt(selectday.Day.options[selectday.Day.selectedIndex].value,10);

	if  (input_year == <%= now_year %>)
		{ 
		if (input_month > <%= now_month %>)
		     {
			 alert("先の日付は入力できません");
			 return false;
			 }
		else if  (input_month == <%= now_month %> && input_day > <%= now_day %>)
		     {
			 alert("先の日付は入力できません");
			 return false;
			 }
		}
}
</script>

<form name="selectday" action="roomhistory.jsp" method="post" target="historyFrame"  onsubmit="return validation();">
  <TD width="100%" class="size10" align="left" bgcolor="#FFFFFF">&nbsp;   
    <SELECT class="size12" name="Year" onChange="setDay(this);"> 
<%
    for( i = 0 ; i < 5 ; i++ )
    {
        if( now_year + (i-4) == year )
        {
%>
        <OPTION VALUE="<%= now_year + (i-4) %>" selected><%= now_year + (i-4) %></OPTION>
<%
        }
        else
        {
%>
        <OPTION VALUE="<%= now_year + (i-4) %>"><%= now_year + (i-4) %></OPTION>
<%
        }
%>
<%
    }
%>
      </SELECT>
      年 
      <SELECT class="size12" size=1 name="Month" onChange="setDay(this);">
<%
    for( i = 0 ; i < 12 ; i++ )
    {
        if( i == month )
        {
%>
            <OPTION VALUE="<%= i + 1 %>" selected><%= i + 1 %></OPTION>
<%
        }
        else
        {
%>
            <OPTION VALUE="<%= i + 1 %>"><%= i + 1 %></OPTION>
<%
        }
    }
%>
      </SELECT>
      月 
      <SELECT class="size12" name="Day" onChange="setDay(this);">
<%
    for( i = 0 ; i < 31 ; i++ )
    {
        if( (i + 1) == day )
        {
%>
            <OPTION VALUE="<%= i + 1 %>" selected><%= i + 1 %></OPTION>
<%
        }
        else
        {
%>
            <OPTION VALUE="<%= i + 1 %>"><%= i + 1 %></OPTION>
<%
        }
    }
%>
      </SELECT>
      日 
    <INPUT type="submit" class="size12" value="照会">
  </TD>
</form>
