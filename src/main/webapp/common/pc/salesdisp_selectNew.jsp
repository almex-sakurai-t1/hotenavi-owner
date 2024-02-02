<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>

<%
    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    int       ymd;
    int       endymd;
    String    param_year;
    String    param_month;
    String    param_day;
    String    param_endyear;
    String    param_endmonth;
    String    param_endday;
    String    param_compare;
    String    param_compare_year;
    String    param_compare_month;
    String    param_compare_day;
    String    param_compare_endyear;
    String    param_compare_endmonth;
    String    param_compare_endday;
    String    storeselect;
    String    param_year_fromlist;
    String    param_month_fromlist;
    String    param_day_fromlist;
    String    param_hotelid;
    DateEdit  de = new DateEdit();

    // パラメタを取得し、Beanにセットしておく
    param_year     = request.getParameter("StartYear");
    param_month    = request.getParameter("StartMonth");
    param_day      = request.getParameter("StartDay");
    param_endyear  = request.getParameter("EndYear");
    param_endmonth = request.getParameter("EndMonth");
    param_endday   = request.getParameter("EndDay");
    param_compare          = request.getParameter("Compare");
    if (param_compare == null) 
    {
        param_compare = "false";
    }
    param_compare_year     = request.getParameter("CompareStartYear");
    param_compare_month    = request.getParameter("CompareStartMonth");
    param_compare_day      = request.getParameter("CompareStartDay");
    param_compare_endyear  = request.getParameter("CompareEndYear");
    param_compare_endmonth = request.getParameter("CompareEndMonth");
    param_compare_endday   = request.getParameter("CompareEndDay");

    param_year_fromlist     = request.getParameter("StartYearfromList");
    param_month_fromlist    = request.getParameter("StartMonthfromList");
    param_day_fromlist      = request.getParameter("StartDayfromList");
    String[] dates = new String[]{ param_year, param_month, param_day, param_endyear, param_endmonth, param_endday,
         param_compare_year, param_compare_month, param_compare_day, param_compare_endyear, param_compare_endmonth, param_compare_endday,
         param_year_fromlist, param_month_fromlist, param_day_fromlist };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    if(param_year_fromlist != null)  param_year  = param_year_fromlist;
    if(param_month_fromlist != null) param_month = param_month_fromlist;
    if(param_day_fromlist != null)   param_day   = param_day_fromlist;


    if( param_year != null && param_month != null && param_day != null )
    {
        ymd = (Integer.valueOf(param_year).intValue() * 10000) + (Integer.valueOf(param_month).intValue() * 100) + Integer.valueOf(param_day).intValue();
    }
    else
    {
        // 日付選択なしの場合は計上日
        ymd = ownerinfo.Addupdate;
    }
    if( param_endyear != null && param_endmonth != null && param_endday != null )
    {
        endymd = (Integer.valueOf(param_endyear).intValue() * 10000) + (Integer.valueOf(param_endmonth).intValue() * 100) + Integer.valueOf(param_endday).intValue();
    }
    else
    {
        // 日付選択なしの場合は計上日=0
        endymd = 0;
    }

    String paramKind           = request.getParameter("Kind");
    if(CheckString.isValidParameter(paramKind) && !CheckString.numAlphaCheck(paramKind))
    {
        response.sendError(400);
        return;
    }
    String paramKindfromList   = request.getParameter("KindfromList");
    if(CheckString.isValidParameter(paramKindfromList) && !CheckString.numAlphaCheck(paramKindfromList))
    {
        response.sendError(400);
        return;
    }
    if    (paramKindfromList != null)
    {
       paramKind = paramKindfromList;
       param_compare = "false";
    }

    if (paramKind == null)
    {
        paramKind = "DATE";
    }
    if (paramKind.equals("DATE"))
    {
        endymd = 0;
    }

    //管理店舗一覧からのリンク
    param_hotelid  = request.getParameter("HotelIdfromGroup");
    if(CheckString.isValidParameter(param_hotelid) && !CheckString.numAlphaCheck(param_hotelid))
    {
        response.sendError(400);
        return;
    }

    // 日付チェック（2ヶ月以内の場合有効）
    if( de.addMonth(ymd,2) >= endymd )
    {
        // パラメタセット
        ownerinfo.SalesGetStartDate       = ymd;
        ownerinfo.SalesGetEndDate         = endymd;
        ownerinfo.SalesDetailGetStartDate = ymd;
        ownerinfo.SalesDetailGetEndDate   = endymd;
        ownerinfo.ManualSalesDetailGetStartDate = ymd;
        ownerinfo.ManualSalesDetailGetEndDate   = endymd;
        ownerinfo.InOutGetStartDate       = ymd;
        ownerinfo.InOutGetEndDate         = endymd;
        ownerinfo.AddupInOutGetDate       = ymd;
        // セッション属性から選択した店舗を取得
        storeselect = (String)session.getAttribute("SelectHotel");
        if (param_hotelid != null)
        {
            storeselect = param_hotelid;
        }
    }
    else
    {
        storeselect = null;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<meta http-equiv="Pragma" content="no-cache">
<script>
<!--
    alert("期間指定は2ヶ月以内でお願いします");
//-->
</script>
</head>
<body background="../../common/pc/image/bg.gif">
</body>
</html>
<%
        return;
    }

    boolean GroupMode = false;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection        = DBConnection.getConnection();
    int          imedia_user      = 0;
    int          level            = 0;
 
     // imedia_user のチェック
    try
    {
    	final String query = "SELECT * FROM owner_user WHERE hotelid=?"
        				 + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    if (imedia_user == 1 && level == 3)
    {
        try
        {
        	final String query = "SELECT * FROM hotel WHERE hotel_id =?"
             					+ " AND  hotel.plan = 98";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,storeselect);
            result      = prestate.executeQuery();
            if(result.next())
            {
               GroupMode = true;
            }
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(result);
            DBConnection.releaseResources(prestate);
        }
    }
    DBConnection.releaseResources(connection);

    // 不明
    if( storeselect == null )
    {
%>
<jsp:forward page="../../common/pc/page.html" />
<%
    }

    if( storeselect.compareTo("all") == 0 )
    {
    // 全店舗を選択
%>
<jsp:forward page="salesdisp_f.html" />
<%
    }
    else
    if( storeselect.compareTo("all_manage") == 0 || GroupMode)
    {
        // 管理店舗合計を選択
        if(paramKind.equals("MONTHLIST") || paramKind.equals("MONTHLISTR"))
        {
            if (param_compare.equals("true"))
            {
%><jsp:forward page="salescomparedispgroup_monthlist.jsp" />
<%
            }
            else
            {
%><jsp:forward page="salesdispgroup_monthlist.jsp" />
<%
            }
        }
        else
        {
            if (param_compare.equals("true"))
            {
%>
<jsp:forward page="salescomparedispgroup.jsp" />
<%
            }
            else
            {
%>
<jsp:forward page="salesdispgroup.jsp" />
<%
            }
        }
    }
    else if(paramKind.equals("MONTHLIST") || paramKind.equals("RANGELIST") || paramKind.equals("MONTHLISTR") || paramKind.equals("RANGELISTR"))
    {
    // 日別売上を選択
        if (param_compare.equals("true"))
        {
%>
<jsp:forward page="salescomparedisp_monthlist.jsp" />
<%
        }
        else
        {
%>
<jsp:forward page="salesdisp_monthlist.jsp" />
<%
        }
    }
    else if(paramKind.equals("YEARLIST"))
    {
    // 年次月別売上を選択
        if (param_compare.equals("true"))
        {
%>
<jsp:forward page="salescomparedisp_yearlist.jsp" />
<%
        }
        else
        {
%>
<jsp:forward page="salesdisp_yearlist.jsp" />
<%
        }
    }
    else if(paramKind.equals("CALENDAR"))
    {
    // カレンダーを選択
        if (param_compare.equals("true"))
        {
%>
<jsp:forward page="salescomparedisp_calendar.jsp" />
<%
        }
        else
        {
%>
<jsp:forward page="salesdisp_calendar.jsp" />
<%
        }
    }
    else// 単店舗を選択
    {
        if (param_compare.equals("true"))
        {
%>
<jsp:forward page="salescomparedisp.jsp" />
<%
        }
        else
        {
%>
<jsp:forward page="salesdisp.jsp" />
<%
        }
    }
%>

