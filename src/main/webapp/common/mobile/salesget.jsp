<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    String    year;
    String    month;
    String    day;
    String    endyear;
    String    endmonth;
    String    endday;
    int       ymd;
    int       endymd;
    int       today_year;
    int       today_month;
    int       today_day;
    int       today_ymd;
    int       int_startyear = 0;
    int       int_startmonth = 0;
    int       int_startday = 0;
    int       int_endyear = 0;
    int       int_endmonth = 0;
    int       int_endday = 0;
    int       err_flg = 0;
    boolean   rangeMode = false;
	boolean   dateError = false;
    int addup_date  = ownerinfo.Addupdate;

    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }
    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;

    String hotelid = request.getParameter("HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }

    if( hotelid != null )
    {
        ownerinfo.HotelId = hotelid;
    }

    Calendar    calnd;

    // �{���̓��t�擾
    calnd = Calendar.getInstance();
    today_year  = calnd.get(calnd.YEAR);
    today_month = calnd.get(calnd.MONTH) + 1;
    today_day   = calnd.get(calnd.DATE);
    today_ymd   = today_year * 10000 + today_month * 100 + today_day;

    DateEdit  de = new DateEdit();

    year     = ReplaceString.getParameter(request,"Year");
    month    = ReplaceString.getParameter(request,"Month");
    day      = ReplaceString.getParameter(request,"Day");
    endyear  = ReplaceString.getParameter(request,"EndYear");
    endmonth = ReplaceString.getParameter(request,"EndMonth");
    endday   = ReplaceString.getParameter(request,"EndDay");
    String[] dates = new String[]{ year, month, day, endyear, endmonth, endday };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    if( year != null && month != null && day != null )
    {
        ymd = (Integer.valueOf(year).intValue() * 10000) + (Integer.valueOf(month).intValue() * 100) + Integer.valueOf(day).intValue();
    }
    else
    {
        if(DemoMode)
        {
            ownerinfo.Addupdate = today_ymd;
        }
        else
        {
            ownerinfo.sendPacket0100(1,hotelid);
        }
        // ���t���ݒ肳��Ă��Ȃ��ꍇ�͌��݌v���
        ymd = ownerinfo.Addupdate;
    }

    if( endyear != null && endmonth != null && endday != null )
    {
        rangeMode=true;
        endymd = (Integer.valueOf(endyear).intValue() * 10000) + (Integer.valueOf(endmonth).intValue() * 100) + Integer.valueOf(endday).intValue();
    }
    else
    {
        endymd = ymd;
    }
%>
<%
    if( ymd != 0 )
    {
        int_startyear  = ymd / 10000;
        int_startmonth = (ymd / 100 % 100) - 1;
        int_startday   = ymd % 100;
    }

    if( endymd != 0 )
    {
        int_endyear  = endymd / 10000;
        int_endmonth = (endymd / 100 % 100) - 1;
        int_endday   = endymd % 100;
    }

    if (day != null)
    {
        if(day.equals("0"))
        {
            ymd = int_startyear * 10000 + (int_startmonth+1) * 100;
            endymd = ymd;
        }
        else if(day.equals("-1"))
        {
            ymd = de.addDay(ymd,-1);
            endymd = ymd;
        }
    }

    int[]     last_day_s = new int[12];
    last_day_s[0]  = 31;
    last_day_s[1]  = 28;
    if (((int_startyear % 4 == 0) && (int_startyear % 100 != 0)) || (int_startyear % 400 == 0))
        {
        last_day_s[1] = 29;
        }
    else
        {
        last_day_s[1] = 28;
        }
    last_day_s[2]  = 31;
    last_day_s[3]  = 30;
    last_day_s[4]  = 31;
    last_day_s[5]  = 30;
    last_day_s[6]  = 31;
    last_day_s[7]  = 31;
    last_day_s[8]  = 30;
    last_day_s[9]  = 31;
    last_day_s[10]  = 30;
    last_day_s[11]  = 31;
    int[]     last_day_e = new int[12];
    last_day_e[0]  = 31;
    last_day_e[1]  = 28;
    if (((int_endyear % 4 == 0) && (int_endyear % 100 != 0)) || (int_endyear % 400 == 0))
        {
        last_day_e[1] = 29;
        }
    else
        {
        last_day_e[1] = 28;
        }
    last_day_e[2]  = 31;
    last_day_e[3]  = 30;
    last_day_e[4]  = 31;
    last_day_e[5]  = 30;
    last_day_e[6]  = 31;
    last_day_e[7]  = 31;
    last_day_e[8]  = 30;
    last_day_e[9]  = 31;
    last_day_e[10]  = 30;
    last_day_e[11]  = 31;

    int min_date            = 20050101; // �ŏ����t
    min_date                = Integer.parseInt(getMinDateString(Integer.toString(addup_date))); //5�N�O�̓��t


    if( de.addMonth(ymd, 2) < endymd )
    {
        ownerinfo.SalesGetStartDate = 0;
        ownerinfo.SalesGetEndDate = 0;
%>
2�����ȓ��͈͎̔w��őI�����Ă�������<br>
�i�f�[�^���擾�j
<hr>
<%
    }
    else
     if( rangeMode && ymd == today_ymd )
    {
        ownerinfo.SalesGetStartDate = 0;
        ownerinfo.SalesGetEndDate = 0;
%>
���t�͈͂��w�肵�Ă�������<br>
�i�f�[�^���擾�j
<hr><%
    }
    else
     if( ymd < min_date )
    {
        ownerinfo.SalesGetStartDate = 0;
        ownerinfo.SalesGetEndDate = 0;
%>
5�N�O���O�̓��t�͓��͂ł��܂���<br>
�i�f�[�^���擾�j
<hr><%
    }
    else
     if( ymd > today_ymd )
    {
        ownerinfo.SalesGetStartDate = 0;
        ownerinfo.SalesGetEndDate = 0;
%>
��̓��t�͓��͂ł��܂���<br>
�i�f�[�^���擾�j
<hr><%
    }
    else
     if( endymd > today_ymd )
    {
        ownerinfo.SalesGetStartDate = 0;
        ownerinfo.SalesGetEndDate = 0;
%>
��̓��t�͓��͂ł��܂���<br>
�i�f�[�^���擾�j
<hr>
<%
    }
    else
     if( ymd > endymd )
    {
        ownerinfo.SalesGetStartDate = 0;
        ownerinfo.SalesGetEndDate = 0;
%>
�J�n���t���I�����t�œ��͂��Ă�������<br>
�i�f�[�^���擾�j
<hr>
<%
    }
    else
    if(int_startday > last_day_s[int_startmonth])
    {
        ownerinfo.SalesGetStartDate = 0;
        ownerinfo.SalesGetEndDate = 0;
%>
���t�𐳂������͂��Ă�������<br>
�i�f�[�^���擾�j
<hr>
<%
    }
    else
    if(int_endday > last_day_e[int_endmonth])
    {
        ownerinfo.SalesGetStartDate = 0;
        ownerinfo.SalesGetEndDate = 0;
%>
���t�𐳂������͂��Ă�������<br>
�i�f�[�^���擾�j
<hr>
<%
    }
    else
    {
        // ����擾���t�̃Z�b�g
        ownerinfo.SalesGetStartDate = ymd;
        ownerinfo.SalesGetEndDate = endymd;
        // ������擾
        ownerinfo.sendPacket0102();
    }
%>
