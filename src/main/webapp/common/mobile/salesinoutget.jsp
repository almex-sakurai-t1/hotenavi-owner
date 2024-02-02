<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
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

    String    year;
    String    month;
    String    day;
    int       ymd;
    int       endymd;
    int       today_year;
    int       today_month;
    int       today_day;
    int       today_ymd;

    Calendar    calnd;

    // 本日の日付取得
    calnd = Calendar.getInstance();
    today_year  = calnd.get(calnd.YEAR);
    today_month = calnd.get(calnd.MONTH) + 1;
    today_day   = calnd.get(calnd.DATE);
    today_ymd   = today_year * 10000 + today_month * 100 + today_day;

    year     = ReplaceString.getParameter(request,"Year");
    month    = ReplaceString.getParameter(request,"Month");
    day      = ReplaceString.getParameter(request,"Day");
    String[] dates = new String[]{ year, month, day };
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
        endymd = ymd;
        ownerinfo.SalesGetStartDate = ymd;
        ownerinfo.SalesGetEndDate = endymd;
    }
    else
    {
        if(DemoMode)
        {
            ymd     = today_ymd;
            endymd  = today_ymd;
            ownerinfo.SalesGetStartDate = ymd;
            ownerinfo.SalesGetEndDate = endymd;
        }
        else
        {
            ymd = ownerinfo.SalesGetStartDate ;
            endymd = ownerinfo.SalesGetEndDate ;
        }
	}


    String    hotelid;
    String    param_ymd;

    hotelid = ReplaceString.getParameter(request,"HotelId");
    if(CheckString.isValidParameter(hotelid) && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    if( hotelid != null )
    {
        ownerinfo.HotelId = hotelid;
    }

    param_ymd = ReplaceString.getParameter(request,"Ymd");
    if( param_ymd != null )
    {
        if ( !CheckString.numCheck( param_ymd ) )
        {
            response.sendError(400);
            return;
        }
        // パラメタセット（部屋情報からのリクエスト）
        ownerinfo.InOutGetStartDate = Integer.parseInt(param_ymd);
        ownerinfo.InOutGetEndDate = 0;
    }
    else
    {
        // パラメタセット（売上情報で取得した日付をセットする）
        ownerinfo.InOutGetStartDate = ymd;
        ownerinfo.InOutGetEndDate = endymd;
    }

    // IN/OUT組数取得
    if(!ownerinfo.sendPacket0106())
    {
%>
取得できませんでした。<br>
<%
    }
%>
