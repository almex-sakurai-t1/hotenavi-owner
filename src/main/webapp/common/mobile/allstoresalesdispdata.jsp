<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
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
    int             i;
    int             count = 0;
    boolean         dataExist = false;
    boolean         getresult = true;
    NumberFormat    nf;
    StringFormat    sf;

    nf = new DecimalFormat("00");
    sf = new StringFormat( );

    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String param_day   = ReplaceString.getParameter(request,"Day");
    if (param_day != null)
    {
       if(param_day.equals("0"))
       {
           MonthFlag = true;
       }
    }
    String param_month = ReplaceString.getParameter(request,"Month");
    String[] dates = new String[]{ param_day, param_month };
    for( String date : dates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    //再取得用
    int[]    t_SalesTotal           = new int[50];
    int[]    t_SalesTotalCount      = new int[50];
    //合計用
    int sales_total = 0;
    int sales_total_count = 0;
    boolean SplitFlag = false;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    try
    {
        connection        = DBConnection.getConnection();
        final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ?"
            + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid"
            + " AND hotel.plan <= 2"
            + " AND hotel.plan >= 1"
            + " AND owner_user_hotel.userid = ? "
            + " ORDER BY hotel.sort_num,hotel.hotel_id";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result      = prestate.executeQuery();
        if (result != null)
        {
%>
<pre>
店舗名 売上金額 　 組数</pre>
<%
            while(result.next()!= false)
            {
                ownerinfo.HotelId = result.getString("owner_user_hotel.accept_hotelid");
                if (DemoMode)
                {
                    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;
                    ownerinfo.SalesTotal      = (int)(525000*per);
                    ownerinfo.SalesTotalCount = (int)(60*per);
                }
                else
                {
                    getresult = ownerinfo.sendPacket0102();
                }
                int year     = ownerinfo.SalesGetStartDate / 10000;
                int month    = ownerinfo.SalesGetStartDate / 100 % 100;
                int day      = ownerinfo.SalesGetStartDate % 100;
                int endyear  = ownerinfo.SalesGetEndDate / 10000;
                int endmonth = ownerinfo.SalesGetEndDate / 100 % 100;
                int endday   = ownerinfo.SalesGetEndDate % 100;
                String param = "?HotelId=" + result.getString("hotel.hotel_id");
                if( ownerinfo.SalesGetStartDate != 0 && ownerinfo.SalesGetEndDate != 0  && ownerinfo.SalesGetEndDate != ownerinfo.SalesGetStartDate)
                {
                    RangeFlag = true;
                }
                if( ownerinfo.SalesGetStartDate % 100 == 0 )
                {
                    MonthFlag = true;
                }
                if (param_month != null)
                {
                    param = param + "&Year=" + year + "&Month=" + month;
                }
                if (MonthFlag)
                {
                    param = param + "&Day=0";
                }
                else
                {
                    param = param + "&Day=" + day;
                }
                if (RangeFlag)
                {
                    param = param + "&EndYear=" + endyear + "&EndMonth=" + endmonth + "&EndDay=" + endday + "&Range";
                }
%>
<%-- ホテル名称表示 --%>
<pre><a href="<%= response.encodeURL("salesdisp.jsp" + param) %>"><%= result.getString("hotel.name") %></a><%if(result.getInt("split_flag") == 1){%><font size=1>(*)</font><%}%><%
                if( getresult == false )
                {
%>
取得できません<br>
<%
                }
                else
                {
                    dataExist         = true;
                    if(result.getInt("split_flag") == 0)
                    {
                        sales_total       = sales_total       + ownerinfo.SalesTotal;
                        sales_total_count = sales_total_count + ownerinfo.SalesTotalCount;
                    }
                    else
                    {
                        SplitFlag = true;
                    }
%>
<%= sf.rightFitFormat(Kanma.get(ownerinfo.SalesTotal), 15) %><%= sf.rightFitFormat(Integer.toString(ownerinfo.SalesTotalCount),6) %>組<%
                }

                if (ownerinfo.SalesTotal!= 0 && ownerinfo.SalesTotalCount != 0 && result.getInt("split_flag") == 0)
                {
                    count++;
                }
%></pre>
<%
            }
        }
    }
    catch( Exception e )
    {
        Logging.error(e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
    if( !dataExist )
    {
%>
管理するホテルがありません<br>
<%
    }
    else
    {
%>
<pre><%=count%>店舗合計
<%= sf.rightFitFormat(Kanma.get(sales_total), 15) %><%= sf.rightFitFormat(Integer.toString(sales_total_count),6) %>組</pre>
<%      if(SplitFlag)
        {
%>
<font size=1>(*)の店舗は合計に加算されません。</font>
<%
        }
%>
<%
    }
%>
