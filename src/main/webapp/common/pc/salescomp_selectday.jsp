<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.util.*" %>
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

<form name="selectday" action="salescomp.jsp" method="post" target="compFrame">
  <TD width="100%" class="size10" align="left" bgcolor="#FFFFFF">&nbsp;   
    <SELECT class="size12" name="Comp1Year">
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
      <SELECT class="size12" size=1 name="Comp1Month">
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
      月と
    <SELECT class="size12" name="Comp2Year">
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
      <SELECT class="size12" size=1 name="Comp2Month">
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
    <INPUT type="submit" class="size12" value="比較">
  </TD>
</form>
