<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="jp.happyhotel.common.CheckString" %><%@ page import="com.hotenavi2.common.MailAddressEncrypt" %>
<%
    boolean ErrorNumeric = false;

    int    rec_count = 0;
    int    condition_check = 0;
    int    condition       = 0;
    int    birthday_check  = 0;
    int    birthday_start  = 0;
    int    birthday_end    = 0;
    int    memorial_check  = 0;
    int    memorial_start  = 0;
    int    memorial_end    = 0;
    int    lastday_check   = 0;
    int    lastday_start   = 0;
    int    lastday_end     = 0;
    int    count_check     = 0;
    int    count_start     = 0;
    int    count_end       = 0;
    int    total_check     = 0;
    int    total_start     = 0;
    int    total_end       = 0;
    int    point_check     = 0;
    int    point_start     = 0;
    int    point_end       = 0;
    int    point2_check     = 0;
    int    point2_start     = 0;
    int    point2_end       = 0;
    int    customid_check  = 0;
    int    customid_start  = 0;
    int    customid_end     = 0;
    int    mailaddress_check = 0;
    String mailaddress = "";
    int    rank_check      = 0;
    int    rank_start      = 0;
    int    rank_end        = 0;
    int    condition_count = 0;
    int    lastsend_check   = 0;
    int    lastsend_start   = 0;
    int    lastsend_end     = 0;
    int    lastsend_start_hhmm = 0;
    int    lastsend_end_hhmm   = 0;
    String param_condition_value = "";
    String param_birthday_check = "";
    String param_birthday_startmonth = "";
    String param_birthday_startday = "";
    String param_birthday_endmonth = "";
    String param_birthday_endday = "";
    String param_memorial_check = "";
    String param_memorial_startmonth = "";
    String param_memorial_startday = "";
    String param_memorial_endmonth = "";
    String param_memorial_endday = "";
    String param_lastday_check = "";
    String param_lastday_startyear = "";
    String param_lastday_startmonth = "";
    String param_lastday_startday = "";
    String param_lastday_endyear = "";
    String param_lastday_endmonth = "";
    String param_lastday_endday = "";
    String param_count_check = "";
    String param_count_start = ReplaceString.getParameter(request,"CountStart");
    String param_count_end = ReplaceString.getParameter(request,"CountEnd");
    String param_total_check = "";
    String param_total_start = ReplaceString.getParameter(request,"TotalStart");
    String param_total_end = ReplaceString.getParameter(request,"TotalEnd");
    String param_point_check = "";
    String param_point_start = ReplaceString.getParameter(request,"PointStart");
    String param_point_end = ReplaceString.getParameter(request,"PointEnd");
    String param_point2_check = "";
    String param_point2_start = ReplaceString.getParameter(request,"Point2Start");;
    String param_point2_end = ReplaceString.getParameter(request,"Point2End");
    String param_customid_check = "";
    String param_customid_start = ReplaceString.getParameter(request,"CustomidStart");
    String param_customid_end = ReplaceString.getParameter(request,"CustomidEnd");
    String param_mailaddress_check = "";
    String param_mailaddress = ReplaceString.getParameter(request,"Mailaddress");
	if(param_mailaddress == null || !CheckString.mailaddrCheck(param_mailaddress))
	{
		param_mailaddress = "";
	}
    String param_rank_check = "";
    String param_rank_start = ReplaceString.getParameter(request,"RankStart");
    String param_rank_end = ReplaceString.getParameter(request,"RankEnd");
    String param_lastsend_check = "";
    String param_lastsend_startyear = "";
    String param_lastsend_startmonth = "";
    String param_lastsend_startday = "";
    String param_lastsend_starthour = "";
    String param_lastsend_startminute = "";
    String param_lastsend_endyear = "";
    String param_lastsend_endmonth = "";
    String param_lastsend_endday = "";
    String param_lastsend_endhour = "";
    String param_lastsend_endminute = "";
    String param_query;

    param_query = ReplaceString.getParameter(request,"Query");
    if( param_query == null )
    {
        param_query = "";
    }

    param_condition_value = ReplaceString.getParameter(request,"Condition");
    if( param_condition_value != null )
    {
        condition = Integer.parseInt(param_condition_value);
    }
%>
<%
    param_customid_check = ReplaceString.getParameter(request,"CustomidCheck");
    if( param_customid_check == null )
    {
        if(param_customid_start != null)
        {
            if(param_customid_start.compareTo("") != 0)
            {
                param_customid_check = "1";
            }
        }
        if(param_customid_end != null)
        {
            if(param_customid_end.compareTo("") != 0)
            {
                param_customid_check = "1";
            }
        }
    }
    if( param_customid_check != null )
    {
        customid_check = Integer.parseInt(param_customid_check);
        if( customid_check == 1 )
        {
            if( param_customid_start == null || param_customid_start.compareTo("") == 0 )
            {
                param_customid_start = "0";
            }
            customid_start = Integer.parseInt(param_customid_start);

            if( param_customid_end == null || param_customid_end.compareTo("") == 0 )
            {
                param_customid_end = "999999999";
            }
            customid_end = Integer.parseInt(param_customid_end);
        }
    }
    param_mailaddress_check = ReplaceString.getParameter(request,"MailaddressCheck");
    if( param_mailaddress_check == null )
    {
        if(param_mailaddress != null)
        {
            if(param_mailaddress.compareTo("") != 0)
            {
                param_mailaddress_check = "1";
            }
        }
    }
    if( param_mailaddress_check != null )
    {
        mailaddress_check = Integer.parseInt(param_mailaddress_check);
        if( mailaddress_check == 1 )
        {
            param_mailaddress = ReplaceString.getParameter(request,"Mailaddress");
            if( param_mailaddress == null || param_mailaddress.compareTo("") == 0 || !CheckString.mailaddrCheck(param_mailaddress))
            {
                param_mailaddress = "";
            }
            mailaddress = param_mailaddress;
        }
    }

    param_birthday_check = ReplaceString.getParameter(request,"BirthdayCheck");
    if( param_birthday_check != null )
    {
        birthday_check = Integer.parseInt(param_birthday_check);
        if( birthday_check == 1 )
        {
            param_birthday_startmonth = ReplaceString.getParameter(request,"BirthdayStartMonth");
            if( param_birthday_startmonth == null || param_birthday_startmonth.compareTo("") == 0 )
            {
                param_birthday_startmonth = "0";
            }
            param_birthday_startday = ReplaceString.getParameter(request,"BirthdayStartDay");
            if( param_birthday_startday == null || param_birthday_startday.compareTo("") == 0 )
            {
                param_birthday_startday = "0";
            }
            birthday_start = Integer.parseInt(param_birthday_startmonth) * 100 + Integer.parseInt(param_birthday_startday);

            param_birthday_endmonth = ReplaceString.getParameter(request,"BirthdayEndMonth");
            if( param_birthday_endmonth == null || param_birthday_endmonth.compareTo("") == 0 )
            {
                param_birthday_endmonth = "12";
            }
            param_birthday_endday = ReplaceString.getParameter(request,"BirthdayEndDay");
            if( param_birthday_endday == null || param_birthday_endday.compareTo("") == 0 )
            {
                param_birthday_endday = "31";
            }
            birthday_end = Integer.parseInt(param_birthday_endmonth) * 100 + Integer.parseInt(param_birthday_endday);
        }
    }
    param_memorial_check = ReplaceString.getParameter(request,"MemorialCheck");
    if( param_memorial_check != null )
    {
        memorial_check = Integer.parseInt(param_memorial_check);
        if( memorial_check == 1 )
        {
            param_memorial_startmonth = ReplaceString.getParameter(request,"MemorialStartMonth");
            if( param_memorial_startmonth == null || param_memorial_startmonth.compareTo("") == 0 )
            {
                param_memorial_startmonth = "0";
            }
            param_memorial_startday = ReplaceString.getParameter(request,"MemorialStartDay");
            if( param_memorial_startday == null || param_memorial_startday.compareTo("") == 0 )
            {
                param_memorial_startday = "0";
            }
            memorial_start = Integer.parseInt(param_memorial_startmonth) * 100 + Integer.parseInt(param_memorial_startday);

            param_memorial_endmonth = ReplaceString.getParameter(request,"MemorialEndMonth");
            if( param_memorial_endmonth == null || param_memorial_endmonth.compareTo("") == 0 )
            {
                param_memorial_endmonth = "12";
            }
            param_memorial_endday = ReplaceString.getParameter(request,"MemorialEndDay");
            if( param_memorial_endday == null || param_memorial_endday.compareTo("") == 0 )
            {
                param_memorial_endday = "31";
            }
            memorial_end = Integer.parseInt(param_memorial_endmonth) * 100 + Integer.parseInt(param_memorial_endday);
        }
    }
    param_lastday_check = ReplaceString.getParameter(request,"LastDayCheck");
    if( param_lastday_check != null )
    {
        lastday_check = Integer.parseInt(param_lastday_check);
        if( lastday_check == 1 )
        {
            param_lastday_startyear = ReplaceString.getParameter(request,"LastDayStartYear");
            if( param_lastday_startyear == null || param_lastday_startyear.compareTo("") == 0 )
            {
                param_lastday_startyear = "0";
            }
            param_lastday_startmonth = ReplaceString.getParameter(request,"LastDayStartMonth");
            if( param_lastday_startmonth == null || param_lastday_startmonth.compareTo("") == 0 )
            {
                param_lastday_startmonth = "0";
            }
            param_lastday_startday = ReplaceString.getParameter(request,"LastDayStartDay");
            if( param_lastday_startday == null || param_lastday_startday.compareTo("") == 0 )
            {
                param_lastday_startday = "0";
            }
            lastday_start = Integer.parseInt(param_lastday_startyear) * 10000 + Integer.parseInt(param_lastday_startmonth) * 100 + Integer.parseInt(param_lastday_startday);

            param_lastday_endyear = ReplaceString.getParameter(request,"LastDayEndYear");
            if( param_lastday_endyear == null || param_lastday_endyear.compareTo("") == 0 )
            {
                param_lastday_endyear = "9999";
            }
            param_lastday_endmonth = ReplaceString.getParameter(request,"LastDayEndMonth");
            if( param_lastday_endmonth == null || param_lastday_endmonth.compareTo("") == 0 )
            {
                param_lastday_endmonth = "12";
            }
            param_lastday_endday = ReplaceString.getParameter(request,"LastDayEndDay");
            if( param_lastday_endday == null || param_lastday_endday.compareTo("") == 0 )
            {
                param_lastday_endday = "31";
            }
            lastday_end = Integer.parseInt(param_lastday_endyear) * 10000 + Integer.parseInt(param_lastday_endmonth) * 100 + Integer.parseInt(param_lastday_endday);
        }
    }
    param_count_check = ReplaceString.getParameter(request,"CountCheck");
    if( param_count_check == null )
    {
        if(param_count_start != null)
        {
            if(param_count_start.compareTo("") != 0)
            {
                param_count_check = "1";
            }
        }
        if(param_count_end != null)
        {
            if(param_count_end.compareTo("") != 0)
            {
                param_count_check = "1";
            }
        }
    }
    if( param_count_check != null )
    {
        count_check = Integer.parseInt(param_count_check);
        if( count_check == 1 )
        {
            if( param_count_start == null || param_count_start.compareTo("") == 0 )
            {
                param_count_start = "0";
            }
            if (CheckString.numCheck(param_count_start))
            {
                count_start = Integer.parseInt(param_count_start);
            }
            else
            {
                ErrorNumeric = true;
            }
            if( param_count_end == null || param_count_end.compareTo("") == 0 )
            {
                param_count_end = "99999999";
            }
            if (CheckString.numCheck(param_count_end))
            {
                count_end = Integer.parseInt(param_count_end);
            }
            else
            {
                ErrorNumeric = true;
            }
        }
    }
    param_total_check = ReplaceString.getParameter(request,"TotalCheck");
    if( param_total_check == null )
    {
        if(param_total_start != null)
        {
            if(param_total_start.compareTo("") != 0)
            {
                param_total_check = "1";
            }
        }
        if(param_total_end != null)
        {
            if(param_total_end.compareTo("") != 0)
            {
                param_total_check = "1";
            }
        }
    }
    if( param_total_check != null )
    {
        total_check = Integer.parseInt(param_total_check);
        if( total_check == 1 )
        {
            if( param_total_start == null || param_total_start.compareTo("") == 0 )
            {
                param_total_start = "0";
            }
            if (CheckString.numCheck(param_total_start))
            {
                total_start = Integer.parseInt(param_total_start);
            }
            else
            {
                ErrorNumeric = true;
            }
            if( param_total_end == null || param_total_end.compareTo("") == 0 )
            {
                param_total_end = "99999999";
            }
            if (CheckString.numCheck(param_total_end))
            {
                total_end = Integer.parseInt(param_total_end);
            }
            else
            {
                ErrorNumeric = true;
            }
        }
    }
    param_point_check = ReplaceString.getParameter(request,"PointCheck");
    if( param_point_check == null )
    {
        if(param_point_start != null)
        {
            if(param_point_start.compareTo("") != 0)
            {
                param_point_check = "1";
            }
        }
        if(param_point_end != null)
        {
            if(param_point_end.compareTo("") != 0)
            {
                param_point_check = "1";
            }
        }
    }
    if( param_point_check != null )
    {
        point_check = Integer.parseInt(param_point_check);
        if( point_check == 1 )
        {
            if( param_point_start == null || param_point_start.compareTo("") == 0 )
            {
                param_point_start = "0";
            }
            if (CheckString.numCheck(param_point_start))
            {
               point_start = Integer.parseInt(param_point_start);
            }
            else
            {
                ErrorNumeric = true;
            }

            if( param_point_end == null || param_point_end.compareTo("") == 0 )
            {
                param_point_end = "99999999";
            }
            if (CheckString.numCheck(param_point_end))
            {
                point_end = Integer.parseInt(param_point_end);
            }
            else
            {
                ErrorNumeric = true;
            }
        }
    }
    param_point2_check = ReplaceString.getParameter(request,"Point2Check");
    if( param_point2_check == null )
    {
        if(param_point2_start != null)
        {
            if(param_point2_start.compareTo("") != 0)
            {
                param_point2_check = "1";
            }
        }
        if(param_point2_end != null)
        {
            if(param_point2_end.compareTo("") != 0)
            {
                param_point2_check = "1";
            }
        }
    }
    if( param_point2_check != null )
    {
        point2_check = Integer.parseInt(param_point2_check);
        if( point2_check == 1 )
        {
            if( param_point2_start == null || param_point2_start.compareTo("") == 0 )
            {
                param_point2_start = "0";
            }
            if (CheckString.numCheck(param_point2_start))
            {
                point2_start = Integer.parseInt(param_point2_start);
            }
            else
            {
                ErrorNumeric = true;
            }

            if( param_point2_end == null || param_point2_end.compareTo("") == 0 )
            {
                param_point2_end = "99999999";
            }
            if (CheckString.numCheck(param_point2_end))
            {
                point2_end = Integer.parseInt(param_point2_end);
            }
            else
            {
                ErrorNumeric = true;
            }
        }
    }
    param_rank_check = ReplaceString.getParameter(request,"RankCheck");
    if( param_rank_check == null )
    {
        if(param_rank_start != null)
        {
            if(param_rank_start.compareTo("") != 0)
            {
                param_rank_check = "1";
            }
        }
        if(param_rank_end != null)
        {
            if(param_rank_end.compareTo("") != 0)
            {
                param_rank_check = "1";
            }
        }
    }
    if( param_rank_check != null )
    {
        rank_check = Integer.parseInt(param_rank_check);
        if( rank_check == 1 )
        {
            if( param_rank_start == null || param_rank_start.compareTo("") == 0 )
            {
                param_rank_start = "0";
            }
            rank_start = Integer.parseInt(param_rank_start);

            if( param_rank_end == null || param_rank_end.compareTo("") == 0 )
            {
                param_rank_end = "99";
            }
            rank_end = Integer.parseInt(param_rank_end);
        }
    }

    param_lastsend_check = ReplaceString.getParameter(request,"LastSendCheck");
    if( param_lastsend_check != null )
    {
        lastsend_check = Integer.parseInt(param_lastsend_check);
        if( lastsend_check == 1 )
        {
            param_lastsend_startyear = ReplaceString.getParameter(request,"LastSendStartYear");
            if( param_lastsend_startyear == null || param_lastsend_startyear.compareTo("") == 0 )
            {
                param_lastsend_startyear = "0";
            }
            param_lastsend_startmonth = ReplaceString.getParameter(request,"LastSendStartMonth");
            if( param_lastsend_startmonth == null || param_lastsend_startmonth.compareTo("") == 0 )
            {
                param_lastsend_startmonth = "0";
            }
            param_lastsend_startday = ReplaceString.getParameter(request,"LastSendStartDay");
            if( param_lastsend_startday == null || param_lastsend_startday.compareTo("") == 0 )
            {
                param_lastsend_startday = "0";
            }
            lastsend_start = Integer.parseInt(param_lastsend_startyear) * 10000 + Integer.parseInt(param_lastsend_startmonth) * 100 + Integer.parseInt(param_lastsend_startday);

            param_lastsend_starthour = ReplaceString.getParameter(request,"LastSendStartHour");
            if( param_lastsend_starthour == null || param_lastsend_starthour.compareTo("") == 0 )
            {
                param_lastsend_starthour = "0";
            }
            param_lastsend_startminute = ReplaceString.getParameter(request,"LastSendStartMinute");
            if( param_lastsend_startminute == null || param_lastsend_startminute.compareTo("") == 0 )
            {
                param_lastsend_startminute = "0";
            }
            lastsend_start_hhmm = Integer.parseInt(param_lastsend_starthour) * 100 + Integer.parseInt(param_lastsend_startminute);

            param_lastsend_endyear = ReplaceString.getParameter(request,"LastSendEndYear");
            if( param_lastsend_endyear == null || param_lastsend_endyear.compareTo("") == 0 )
            {
                param_lastsend_endyear = "9999";
            }
            param_lastsend_endmonth = ReplaceString.getParameter(request,"LastSendEndMonth");
            if( param_lastsend_endmonth == null || param_lastsend_endmonth.compareTo("") == 0 )
            {
                param_lastsend_endmonth = "12";
            }
            param_lastsend_endday = ReplaceString.getParameter(request,"LastSendEndDay");
            if( param_lastsend_endday == null || param_lastsend_endday.compareTo("") == 0 )
            {
                param_lastsend_endday = "31";
            }
            lastsend_end = Integer.parseInt(param_lastsend_endyear) * 10000 + Integer.parseInt(param_lastsend_endmonth) * 100 + Integer.parseInt(param_lastsend_endday);

            param_lastsend_endhour = ReplaceString.getParameter(request,"LastSendEndHour");
            if( param_lastsend_endhour == null || param_lastsend_endhour.compareTo("") == 0 )
            {
                param_lastsend_endhour = "0";
            }
            param_lastsend_endminute = ReplaceString.getParameter(request,"LastSendEndMinute");
            if( param_lastsend_endminute == null || param_lastsend_endminute.compareTo("") == 0 )
            {
                param_lastsend_endminute = "0";
            }
            lastsend_end_hhmm = Integer.parseInt(param_lastsend_endhour) * 100 + Integer.parseInt(param_lastsend_endminute);
        }
    }
    if (!param_mailaddress.equals(""))
    {
        param_query = param_query.replace(param_mailaddress,MailAddressEncrypt.encrypt(param_mailaddress));
    }%>
