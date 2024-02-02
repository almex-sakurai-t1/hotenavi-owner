<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if    (selecthotel == null) selecthotel = "all_manage";

    int start_year  = ownerinfo.SalesGetStartDate / 10000;
    int start_month = ownerinfo.SalesGetStartDate / 100 % 100;
    int start_day   = ownerinfo.SalesGetStartDate % 100;
    int end_year  = 0;
    int end_month = 0;
    int end_day   = 0;
//    if( ownerinfo.SalesGetEndDate != 0 )
//    {
//        end_year  = ownerinfo.SalesGetEndDate / 10000;
//        end_month = ownerinfo.SalesGetEndDate / 100 % 100;
//        end_day   = ownerinfo.SalesGetEndDate % 100;
//    }
	boolean dateError = false;
    String param_year     = ReplaceString.getParameter(request,"StartYear");
	if( param_year != null && !CheckString.numCheck(param_year))
	{
		dateError = true;
	}
    String param_month    = ReplaceString.getParameter(request,"StartMonth");
	if( param_month != null && !CheckString.numCheck(param_month))
	{
		dateError = true;
	}
    String param_day      = ReplaceString.getParameter(request,"StartDay");
	if( param_day != null && !CheckString.numCheck(param_day))
	{
		dateError = true;
	}
    String param_endyear  = ReplaceString.getParameter(request,"EndYear");
	if( param_endyear != null && !CheckString.numCheck(param_endyear))
	{
		dateError = true;
	}
    String param_endmonth = ReplaceString.getParameter(request,"EndMonth");
	if( param_endmonth != null && !CheckString.numCheck(param_endmonth))
	{
		dateError = true;
	}
    String param_endday   = ReplaceString.getParameter(request,"EndDay");
	if( param_endday != null && !CheckString.numCheck(param_endday))
	{
		dateError = true;
	}
    String param_year_fromlist  = ReplaceString.getParameter(request,"StartYearfromList");
	if( param_year_fromlist != null && !CheckString.numCheck(param_year_fromlist))
	{
		dateError = true;
	}
    String param_month_fromlist = ReplaceString.getParameter(request,"StartMonthfromList");
	if( param_month_fromlist != null && !CheckString.numCheck(param_month_fromlist))
	{
		dateError = true;
	}
    String param_day_fromlist   = ReplaceString.getParameter(request,"StartDayfromList");
	if( param_day_fromlist != null && !CheckString.numCheck(param_day_fromlist))
	{
		dateError = true;
	}
	if(dateError)
	{
        param_year="0";
        param_month="0";
        param_day="0";
        param_endyear="0";
        param_endmonth="0";
        param_endday="0";
        param_year_fromlist="0";
        param_month_fromlist="0";
        param_day_fromlist="0";
%>
		<script type="text/javascript">
		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
    if(param_year_fromlist != null)  param_year  = param_year_fromlist;
    if(param_month_fromlist != null) param_month = param_month_fromlist;
    if(param_day_fromlist != null)   param_day   = param_day_fromlist;

    if (param_year != null)
        start_year  = Integer.parseInt(param_year);
    if (param_month != null)
        start_month = Integer.parseInt(param_month);
    if (param_day  != null)
        start_day   = Integer.parseInt(param_day);
    if (param_endyear != null)
        end_year    = Integer.parseInt(param_endyear);
    if (param_endmonth != null)
        end_month   = Integer.parseInt(param_endmonth);
    if (param_endday != null)
        end_day     = Integer.parseInt(param_endday);


    String paramKind   = ReplaceString.getParameter(request,"Kind");
    String paramKindfromList   = ReplaceString.getParameter(request,"KindfromList");
    if    (paramKindfromList != null) paramKind = paramKindfromList;
    if (paramKind == null)
    {
        paramKind = "DATE";
    }
    if (paramKind.equals("DATE"))
    {
        end_day = 0;
    }
%>
<%= start_year %>年<%= start_month %>月<%if( start_day != 0 ){%><%= start_day %>日
<%
    }
    if( end_day != 0 )
    {
%>
～
<%= end_year %>年<%= end_month %>月<%= end_day %>日
<%
    }
%>
計上分
<%
    if ( start_day != 0  && end_day == 0 && !selecthotel.equals("all_manage"))
    {
        //料金モードの取得
        ownerinfo.CalGetDate = start_year * 10000 + start_month*100 + start_day;
        if (ReplaceString.getParameter(request,"HotelIdfromGroup")!=null)
        {
            ownerinfo.sendPacket0142(1, ReplaceString.getParameter(request,"HotelIdfromGroup"));
        }
        else
        {
            ownerinfo.sendPacket0142(1, selecthotel);
        }
        if (ownerinfo.CalDayModeName[start_day-1] != null)
        {
%>
<span style="position:relative;left:150px;">[料金モード：<span style="margin-right:5px"><%=ownerinfo.CalDayModeName[start_day-1]%>]</span>
<%
        }
    }
%>

<%
    if( start_day == 0 && paramKind.indexOf("LIST") == -1 && selecthotel.compareTo("all") != 0 && selecthotel.compareTo("all_manage") != 0)
    {
%>
<!--<input name="monthlist" type="button" class="inoutbtn" style="background-color: #000066;" onClick="location.href='salesdisp_monthlist.jsp?HotelId=<%= selecthotel %>'" value="日別売上">-->
<%
    }
%>
