<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%-- ���ʔ���ꗗ�\������ --%>
<%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%>
<jsp:forward page="timeout.jsp" />
<%
    }
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }

    int wday;
    int year;
    int month;
    int mday;
    String[] arrWday = {"��", "��", "��", "��", "��", "��", "�y"};

    int             i;
    StringFormat    sf;
    NumberFormat    nf;

    sf = new StringFormat();
    nf = new DecimalFormat("00");

//�����v
    int      salestotal = 0;
    int      salestotalcount =0 ;

    String   sday;

    int       ymd;

    int       today_year;
    int       today_month;
    int       today_day;
    int       today_ymd;
    int       now_addup;
    int       addup_year;
    int       addup_month;
    int       addup_day;

    int       start_year  = ownerinfo.SalesGetStartDate / 10000;
    int       start_month = ownerinfo.SalesGetStartDate / 100 % 100;
    int       start_day   = ownerinfo.SalesGetStartDate % 100;
    int       start_ymd   = start_year * 10000 + start_month * 100 + start_day;

    int[]     last_day = new int[12];
    last_day[0]  = 31;
    last_day[1]  = 28;
    if (((start_year % 4 == 0) && (start_year % 100 != 0)) || (start_year % 400 == 0))
    {
        last_day[1] = 29;
    }
    else
    {
        last_day[1] = 28;
    }
    last_day[2]  = 31;
    last_day[3]  = 30;
    last_day[4]  = 31;
    last_day[5]  = 30;
    last_day[6]  = 31;
    last_day[7]  = 31;
    last_day[8]  = 30;
    last_day[9]  = 31;
    last_day[10]  = 30;
    last_day[11]  = 31;

    Calendar    calnd;

    // �{���̓��t�擾
    calnd = Calendar.getInstance();
    today_year  = calnd.get(calnd.YEAR);
    today_month = calnd.get(calnd.MONTH) + 1;
    today_day   = calnd.get(calnd.DATE);
    today_ymd   = today_year * 10000 + today_month * 100 + today_day;
    // ���݌v����擾
    now_addup = ownerinfo.Addupdate;
    addup_year  = now_addup / 10000;
    addup_month = now_addup / 100 % 100;
    addup_day   = now_addup % 100;

    //�����̏ꍇ�́A���̍ŏI��
    if (today_year == start_year && today_month == start_month)
    {
        last_day[today_month - 1] = addup_day;
    }

%>
<%= (ownerinfo.SalesGetStartDate / 10000) %>/<%= (ownerinfo.SalesGetStartDate / 100 % 100) %>
�v�㕪<font size=1>[<a href="#DateSelect">�N���I��</a>]</font><br>
<%
    // �z�e��ID�̃Z�b�g
    String hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid == null )
    {
        hotelid = ownerinfo.HotelId;
    }
%>
<hr>
<%-- ���v���ʔ���ꗗ�\�� --%>
<pre>
�@���@�@������z�@ �g��</pre>
<%
    for( i = 1 ; i <= last_day[start_month - 1] ; i++ )
    {
        // �j���̎Z�o        
        month = start_month;
        year  = start_year;
        mday  = i;

        if(month == 1 || month == 2) {
            year--;
            month += 12;
        }
        wday = (year + (int)(year/4) - (int)(year/100) + (int)(year/400) + (int)((13*month+8)/5) + mday) % 7;

        // ����擾���t�̃Z�b�g
        ymd = (start_year * 10000) + (start_month * 100) + i;
        ownerinfo.SalesGetStartDate = ymd;
        ownerinfo.SalesGetEndDate = ymd;

        if (DemoMode)
        {
            float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;
            ownerinfo.SalesTotal      = (int)(525000*per);
            ownerinfo.SalesTotalCount = (int)(60*per);
        }
        else
        {
           // ������擾
           ownerinfo.sendPacket0102();
        }

        salestotal       += ownerinfo.SalesTotal;
        salestotalcount  += ownerinfo.SalesTotalCount;
%>
<pre><a href="<%= response.encodeURL("salesdisp.jsp?HotelId=" + hotelid + "&Year=" + start_year + "&Month=" + start_month + "&Day=" + i) %>"><%= sf.rightFitFormat( Integer.toString(i), 2) %>(<%= arrWday[wday] %>)</a><%= sf.rightFitFormat( Kanma.get(ownerinfo.SalesTotal), 10 ) %><%= sf.rightFitFormat( Integer.toString(ownerinfo.SalesTotalCount), 5) %><font size=1>�g</font></pre>
<%
    }
%>
<pre>
���v<%= sf.rightFitFormat( Kanma.get(salestotal), 12) %><%= sf.rightFitFormat( Integer.toString(salestotalcount), 5) %><font size=1>�g</font>
</pre>
<%-- �\�������܂� --%>
<hr>
<a name="DateSelect"></a>
<%-- �N���I����ʕ\�� --%>
<jsp:include page="salesselectmonth.jsp" flush="true" >
  <jsp:param name="Contents" value="salesmonthlistdisp" />
</jsp:include>

<jsp:include page="jumpanother.jsp" flush="true" >
  <jsp:param name="HotelId" value="<%= hotelid %>" />
  <jsp:param name="jumpurl" value="salesmonthlistdisp.jsp" />
  <jsp:param name="dispname" value="�������ʔ���" />
</jsp:include>
