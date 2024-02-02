<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="getCalendar.jsp" %>
<%
    String contents = request.getParameter("Contents");
    if (contents == null)
    {
        contents = "salesdisp";
    }

    // �z�e��ID�̃Z�b�g
    String hotelid = request.getParameter("HotelId");
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }

    // ���ݓ��t
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;

    // �v����t
    int addup_date = ownerinfo.Addupdate;
    if (addup_date == 0)
    {
        addup_date = now_date;
    }
    int addup_year  = addup_date / 10000;
    int addup_month = addup_date / 100 % 100;
    int addup_day   = addup_date % 100;

    // �擾���t
    int cal_date = ownerinfo.SalesGetStartDate;
    if (contents.equals("roomhistory") || contents.equals("salesinout"))
    {
        cal_date = ownerinfo.InOutGetStartDate;
    }
    if (cal_date == 0)
    {
        cal_date = addup_date;
    }
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;
    cal_date = (cal_date / 100)*100+1;

    String paramDate       = Integer.toString(cal_date);
    String nextMonthDate   = getNext1MDateString(paramDate);
    String beforeMonthDate = getBefore1MDateString(paramDate);
    String nextYearDate    = getNext1YDateString(paramDate);
    String beforeYearDate  = getBefore1YDateString(paramDate);

    int min_date = 20050101; // �ŏ����t
    min_date = Integer.parseInt(getMinDateString(Integer.toString(addup_date))); //5�N�O�̓��t

    //�����N�𖳌��`�F�b�N
    boolean beforeYearLink  = true;
    boolean beforeMonthLink = true;
    boolean nextYearLink    = true;
    boolean nextMonthLink   = true;

    if (Integer.parseInt(beforeYearDate)/100 < min_date/100)
    {
        beforeYearLink = false;
    }
    if (Integer.parseInt(beforeMonthDate)/100 <  min_date/100)
    {
        beforeMonthLink = false;
    }
    if (Integer.parseInt(nextYearDate)/100 > addup_date/100)
    {
        nextYearLink = false;
    }
    if (Integer.parseInt(nextMonthDate)/100 > addup_date/100)
    {
        nextMonthLink = false;
    }

    int target_date = 0;

    //���t�I���G���A�̍쐬
    int    i;
    String tagyear;
    String tagmonth;
    String tagday;

    tagyear = "<select id=\"year\" name=\"Year\">";
    for( i = -4 ; i < 2 ; i++ )
    {
        if( addup_year + (i-1) == cal_year )
        {
            tagyear = tagyear + "<option value=\"" + (addup_year + (i-1)) + "\" selected>" + (addup_year + (i-1));
        }
        else
        {
            tagyear = tagyear + "<option value=\"" + (addup_year + (i-1)) + "\">" + (addup_year + (i-1));
        }
    }
    tagyear = tagyear + "</select>�N";

    tagmonth = "<select id=\"month\" name=\"Month\">";
    for( i = 0 ; i < 12 ; i++ )
    {
        if( (i + 1) == cal_month )
        {
            tagmonth = tagmonth + "<option value=\"" + (i + 1) + "\" selected>" + (i + 1);
        }
        else
        {
            tagmonth = tagmonth + "<option value=\"" + (i + 1) + "\">" + (i + 1);
        }
    }
    tagmonth = tagmonth + "</select>��";

    tagday = "<input type=\"hidden\" name=\"Day\" value=\"0\">";

    if (beforeYearLink)
    {
        target_date = Integer.parseInt(beforeYearDate);
%>
<ul class="link_detail">
<li class="some"><a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day=0") %>">�O�N</a>
<%
    }
%>
<%
    if (beforeMonthLink)
    {
        target_date = Integer.parseInt(beforeMonthDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day=0") %>">�O��</a>
<%
    }
%>
<%
    if (nextMonthLink)
    {
        target_date = Integer.parseInt(nextMonthDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day=0") %>">����</a>
<%
    }
%>
<%
    if (nextYearLink)
    {
        target_date = Integer.parseInt(nextYearDate);
%>
<a href="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J") + "&Year="+(target_date/10000)+"&Month="+(target_date/100%100)+"&Day=0") %>">���N</a></li>
<%
    }
%>
</ul>
<div class="form" align="center">
<form action="<%= response.encodeURL(URLEncoder.encode(contents, "Windows-31J") + ".jsp?HotelId=" + URLEncoder.encode(hotelid, "Windows-31J")) %>" method="post">
���t��I�����Ă��������B<br>
<%= tagyear %><br>
<%= tagmonth %><br>
<%= tagday %><br>
<input type="submit" value="����" id="button">
</form>
</div>
<hr class="border" />
