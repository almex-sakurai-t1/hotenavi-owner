<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    DateEdit  de   = new DateEdit();
    int  nowdate   = Integer.parseInt(de.getDate(2));
//    nowdate        = de.addDay(nowdate,-1);
    int  now_year  = nowdate / 10000;
    int  now_month = nowdate / 100 % 100;
    int  now_day   = nowdate % 100;
	boolean dateError = false;
    String StartYear  = ReplaceString.getParameter(request,"StartYear");
    if( StartYear != null && !CheckString.numCheck(StartYear))
	{
		dateError = true;
	}
    if (StartYear    == null)   StartYear  = "0";
    int year_from    = Integer.parseInt(StartYear);
    String StartMonth = ReplaceString.getParameter(request,"StartMonth");
    if( StartMonth != null && !CheckString.numCheck(StartMonth))
	{
		dateError = true;
	}
    if (StartMonth   == null)   StartMonth = "0";
    int month_from   = Integer.parseInt(StartMonth);
    String StartDay   = ReplaceString.getParameter(request,"StartDay");
    if( StartDay != null && !CheckString.numCheck(StartDay))
	{
		dateError = true;
	}
    if (StartDay     == null)   StartDay   = "0";
    int day_from     = Integer.parseInt(StartDay);
    String EndYear    = ReplaceString.getParameter(request,"EndYear");
    if( EndYear != null && !CheckString.numCheck(EndYear))
	{
		dateError = true;
	}
    if (EndYear      == null)   EndYear  = Integer.toString(now_year);
    int year_to      = Integer.parseInt(EndYear);
    String EndMonth   = ReplaceString.getParameter(request,"EndMonth");
    if( EndMonth != null && !CheckString.numCheck(EndMonth))
	{
		dateError = true;
	}
    if (EndMonth     == null)   EndMonth = Integer.toString(now_month);
    int month_to     = Integer.parseInt(EndMonth);
    String EndDay     = ReplaceString.getParameter(request,"EndDay");
    if( EndDay != null && !CheckString.numCheck(EndDay))
	{
		dateError = true;
	}
    if (EndDay       == null)   EndDay   = Integer.toString(now_day);
    int day_to       = Integer.parseInt(EndDay);

    int date_from    = year_from * 10000 + month_from * 100 + day_from;
    int date_to      = year_to   * 10000 + month_to   * 100 + day_to;
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
    String Id     = ReplaceString.getParameter(request,"Id");
    if (Id       == null)  Id   = "0";
    int    id     = Integer.parseInt(Id);
    String Qid    = ReplaceString.getParameter(request,"Qid");
    if (Qid      == null)  Qid  = "0";
    int    q_id   = Integer.parseInt(Qid);
    String Type   = ReplaceString.getParameter(request,"Type");
    if (Type     == null)  Type  = "0";
    if( !CheckString.numCheck(Type) )
    {
        Type  = "0";
%>
        <script type="text/javascript">
        <!--
        var dd = new Date();
        setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
        //-->
        </SCRIPT>
<%
    }
    int    type   = Integer.parseInt(Type);
    type          = type % 10;
    String[] Contents = new String[10];
    Contents[0]       = "list";
    Contents[1]       = "amount";
    Contents[2]       = "amount";
    Contents[3]       = "text";
    Contents[4]       = "amount";
    Contents[5]       = "";
    Contents[6]       = "";
    Contents[7]       = "";
    Contents[8]       = "";
    Contents[9]       = "";
%>
