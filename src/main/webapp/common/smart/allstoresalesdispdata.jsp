<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" /><%
    String requestUri = request.getRequestURI();
    if( requestUri.indexOf("/mobile/") > 0 )
    {
%><jsp:forward page="timeout.jsp" />
<%
    }
    String loginHotelId = (String)session.getAttribute("HotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("000000000000000"))
    {
        DemoMode = true;
    }
    int             i = -1;
    int             count = 0;
    boolean         dataExist = false;
    boolean         getresult = true;
    NumberFormat    nf;
    StringFormat    sf;

    nf = new DecimalFormat("00");
    sf = new StringFormat( );

    boolean MonthFlag = false;
    boolean RangeFlag = false;
    String param_day   = request.getParameter("Day");
    if (param_day != null)
    {
       if(param_day.equals("0"))
       {
           MonthFlag = true;
       }
    }
    String param_month = request.getParameter("Month");

    String selectStore = request.getParameter("selectStore");
    if (selectStore == null)
    {
        selectStore = "false";
    }

    //再取得用
    int[]    t_room_count           = new int[50];
    int[]    t_SalesTotal           = new int[50];
    int[]    t_SalesTotalCount      = new int[50];
    int[]    t_SalesTotalRate       = new int[50];
    int[]    t_SalesTotalPrice      = new int[50];
    int[]    t_SalesRoomPrice       = new int[50];
    int[]    t_SalesRoomTotalPrice  = new int[50];
    int[]    t_SalesRestTotal       = new int[50];
    int[]    t_SalesRestCount      = new int[50];
    int[]    t_SalesRestRate       = new int[50];
    int[]    t_SalesRestPrice      = new int[50];
    int[]    t_RestRoomPrice       = new int[50];
    int[]    t_SalesStayTotal      = new int[50];
    int[]    t_SalesStayCount      = new int[50];
    int[]    t_SalesStayRate       = new int[50];
    int[]    t_SalesStayPrice      = new int[50];
    int[]    t_StayRoomPrice       = new int[50];

    //合計用
    int date_count  = 0;
    int date_count_total = 0;
    int store_count = 0;
    int line_count  = 0;
    int sales_total = 0;
    int sales_total_count = 0;
    int room_count_total = 0;
    int sales_rest_total = 0;
    int sales_rest_total_count = 0;
    int sales_stay_total = 0;
    int sales_stay_total_count = 0;
    int          imedia_user      = 0;
    int          level            = 0;
    int          sales_cache_flag = 0;
    Connection connection = null;
    PreparedStatement prestate = null;
    ResultSet result = null;
    connection  = DBConnection.getConnection();
    try
    {
        final String query = "SELECT owner_user.imedia_user,owner_user.level,owner_user_security.sales_cache_flag"
                           + " FROM owner_user"
                           + " INNER JOIN owner_user_security ON owner_user.hotelid = owner_user_security.hotelid AND owner_user.userid = owner_user_security.userid"
                           + " WHERE owner_user.hotelid = ? AND owner_user.userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
            sales_cache_flag = result.getInt("sales_cache_flag");
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

    boolean SplitFlag = false;

    try
    {
        final String query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid = ? "
                           + "AND hotel.hotel_id = owner_user_hotel.accept_hotelid "
                           + "AND hotel.plan <= 2 "
                           + "AND hotel.plan >= 1 "
                           + "AND owner_user_hotel.userid = ? "
                           + "ORDER BY hotel.sort_num,hotel.hotel_id";

        prestate = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);

        result = prestate.executeQuery();
%>
<script type="text/javascript" src="../../common/pc/scripts/prototype.js"></script>
<div style="float:right;clear:both;">
<span style="font-size:8px"><input id="sales_cache"  type="checkbox" onclick="salesCacheChange(this);" value="1" <%if (sales_cache_flag == 1){%> checked <%}%>>キャッシュ</span>

<input type="button" id="reloadStore" value="店舗選択終了" class="changeButton" onclick="selStore();" style="display:none">
<input type="button" value="表示項目変更" id="tableChange" class="changeButton" onclick="tableChange();" >
</div>
<table class="uriage">
<thead>
<tr>
<th label="num">店舗名</th><th label="num" style="display:none"></th>
<th label="num" class="theadTh1">売上金額<select style="display:none" class="Selects" onchange="titleChange(1,0,this);"><option selected>売上金額<option>休憩のみ<option>宿泊のみ</select></th>
<th label="num" class="theadTh1" style="display:none">休憩のみ<select style="display:none" class="Selects" onchange="titleChange(1,1,this);"><option>売上金額<option selected>休憩のみ<option>宿泊のみ</select></th>
<th label="num" class="theadTh1" style="display:none">宿泊のみ<select style="display:none" class="Selects" onchange="titleChange(1,2,this);"><option>売上金額<option>休憩のみ<option selected>宿泊のみ</select></th>

<th label="num" class="theadTh2">組数<select style="display:none" class="Selects" onchange="titleChange(2,0,this);"><option selected>組数<option>回転数<option>客単価<option>部屋単価</select></th>
<th label="num" class="theadTh2" style="display:none">回転数<select style="display:none" class="Selects" onchange="titleChange(2,1,this);"><option>組数<option selected>回転数<option>客単価<option>部屋単価</select></th>
<th label="num" class="theadTh2" style="display:none">客単価<select style="display:none" class="Selects" onchange="titleChange(2,2,this);"><option>組数<option>回転数<option selected>客単価<option>部屋単価</select></th>
<th label="num" class="theadTh2" style="display:none">部屋単価<select style="display:none" class="Selects"  onchange="titleChange(2,3,this);"><option>組数<option>回転数<option>客単価<option selected>部屋単価</select></th>
</tr>
</thead>
<tbody>
<%
        Object lock = request.getSession();
        synchronized(lock)
        {
            while(result.next())
            {
                i++;
                getresult =true;
                String hotelid = result.getString("owner_user_hotel.accept_hotelid");

                if (DemoMode)
                {
                    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;
                    ownerinfo.SalesTotal      = (int)(525000*per);
                    ownerinfo.SalesTotalCount = (int)(60*per);
                    ownerinfo.SalesTotalRate  = 300;
                    ownerinfo.SalesTotalPrice = (int)Math.round(8750*per);
                    ownerinfo.SalesRoomPrice  = (int)Math.round(26250*per);
                    ownerinfo.SalesRoomTotalPrice = (int)Math.round(525000*per*30);
                    ownerinfo.SalesRestTotal = (int)(525000*per);
                    ownerinfo.SalesRestCount = (int)(60*per);
                    ownerinfo.SalesRestRate  = 300;
                    ownerinfo.SalesRestPrice = (int)Math.round(8750*per);
                    ownerinfo.SalesStayTotal = (int)(525000*per);
                    ownerinfo.SalesStayCount = (int)(60*per);
                    ownerinfo.SalesStayRate  = 300;
                    ownerinfo.SalesStayPrice = (int)Math.round(8750*per);
                }
                else
                {
                    ownerinfo.sendPacket0100(1, hotelid);
                    if (ownerinfo.SalesGetStartDate == ownerinfo.SalesGetEndDate || ownerinfo.SalesGetEndDate == 0)
                    {
                        getresult = ownerinfo.sendPacket0102(1,hotelid,sales_cache_flag);
                    }
                    else
                    {
                        getresult = ownerinfo.sendPacket0102(1,hotelid);
                    }
                    ownerinfo.sendPacket0114(1,hotelid);
                }
                if (getresult)
                {
                    t_room_count[i]          = ownerinfo.StateRoomCount;
                    t_SalesTotal[i]          = ownerinfo.SalesTotal;
                    t_SalesTotalCount[i]     = ownerinfo.SalesTotalCount;
                    t_SalesTotalRate[i]      = ownerinfo.SalesTotalRate;
                    t_SalesTotalPrice[i]     = ownerinfo.SalesTotalPrice;
                    t_SalesRoomPrice[i]      = ownerinfo.SalesRoomPrice;
                    if (ownerinfo.SalesGetStartDate%100 != 0 && ownerinfo.SalesGetEndDate==0)
                    {
                        t_SalesRoomTotalPrice[i] = ownerinfo.SalesRoomPrice;
                    }
                    else
                    {
                        t_SalesRoomTotalPrice[i] = ownerinfo.SalesRoomTotalPrice;
                    }

                    t_SalesRestTotal[i]     = ownerinfo.SalesRestTotal;
                    t_SalesRestCount[i]     = ownerinfo.SalesRestCount;
                    t_SalesRestRate[i]      = ownerinfo.SalesRestRate;
                    t_SalesRestPrice[i]     = ownerinfo.SalesRestPrice;
                    if (t_room_count[i] != 0)
                    {
                        t_RestRoomPrice[i] = Math.round((float)t_SalesRestTotal[i] / (float)t_room_count[i]);
                    }

                    t_SalesStayTotal[i]     = ownerinfo.SalesStayTotal;
                    t_SalesStayCount[i]     = ownerinfo.SalesStayCount;
                    t_SalesStayRate[i]      = ownerinfo.SalesStayRate;
                    t_SalesStayPrice[i]     = ownerinfo.SalesStayPrice;
                    if (t_room_count[i] != 0)
                    {
                        t_StayRoomPrice[i] = Math.round((float)t_SalesStayTotal[i] / (float)t_room_count[i]);
                    }

                    date_count = 0;
                    if (t_SalesRoomPrice[i] != 0)
                    {
                        date_count    = Math.round((float)t_SalesRoomTotalPrice[i] / (float)t_SalesRoomPrice[i]);
                    }
                }
                if(result.getInt("split_flag")==1)
                {
                    SplitFlag = true;
                }
                else if (result.getInt("owner_user_hotel.sales_disp_flag")== 1)
                {
                    sales_total       = sales_total       + t_SalesTotal[i];
                    sales_total_count = sales_total_count + t_SalesTotalCount[i];
                    sales_rest_total       = sales_rest_total       + t_SalesRestTotal[i];
                    sales_rest_total_count = sales_rest_total_count + t_SalesRestCount[i];
                    sales_stay_total       = sales_stay_total       + t_SalesStayTotal[i];
                    sales_stay_total_count = sales_stay_total_count + t_SalesStayCount[i];
                    room_count_total  = room_count_total + t_room_count[i];
                    date_count_total  = date_count_total + date_count*t_room_count[i];
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
%><%-- ホテル名称表示 --%>
<tr class="storeDetail" <%if (result.getInt("owner_user_hotel.sales_disp_flag")== 0){%>style="display:none"<%}%>>
<td colspan="1" style="display:none"><%=i%></td>
<td>
<a href="<%= response.encodeURL("salesdisp.jsp" + param) %>"><%= result.getString("hotel.name") %></a><%if(result.getInt("split_flag") == 1){%><font size=1>(*)</font><%}%>
<input type="checkbox" class="storeCheck" onchange="storeEdit('<%=hotelid%>',this);" <% if (result.getInt("owner_user_hotel.sales_disp_flag") == 1){%>checked<%}%> style="<%if (selectStore.equals("true")){%>display:inline<%}else{%>display:none<%}%>;">
</td>
<%
                if( getresult == false )
                {
%><td colspan="2">取得できません</td></tr>
<%
                }
                else
                {
                    line_count++;
                    dataExist         = true;
%><td class="uriage tbodyTd10"><%= Kanma.get(t_SalesTotal[i])%></td>
<td class="uriage tbodyTd11" style="display:none"><%= Kanma.get(t_SalesRestTotal[i])%></td>
<td class="uriage tbodyTd12" style="display:none"><%= Kanma.get(t_SalesStayTotal[i])%></td>
<td class="uriage tbodyTd200"><%= t_SalesTotalCount[i] %></td>
<td class="uriage tbodyTd201" style="display:none"><%= (float)t_SalesTotalRate[i] / (float)100 %><% if((float)t_SalesTotalRate[i]%10==0){%>0<%}%></td>
<td class="uriage tbodyTd202" style="display:none"><%= Kanma.get(t_SalesTotalPrice[i])%></td>
<td class="uriage tbodyTd203" style="display:none"><%= Kanma.get(t_SalesRoomPrice[i])%></td>
<td class="uriage tbodyTd210" style="display:none"><%= t_SalesRestCount[i] %></td>
<td class="uriage tbodyTd211" style="display:none"><%= (float)t_SalesRestRate[i] / (float)100 %><% if((float)t_SalesRestRate[i]%10==0){%>0<%}%></td>
<td class="uriage tbodyTd212" style="display:none"><%= Kanma.get(t_SalesRestPrice[i])%></td>
<td class="uriage tbodyTd213" style="display:none"><%= Kanma.get(t_RestRoomPrice[i])%></td>
<td class="uriage tbodyTd220" style="display:none"><%= t_SalesStayCount[i] %></td>
<td class="uriage tbodyTd221" style="display:none"><%= (float)t_SalesStayRate[i] / (float)100 %><% if((float)t_SalesStayRate[i]%10==0){%>0<%}%></td>
<td class="uriage tbodyTd222" style="display:none"><%= Kanma.get(t_SalesStayPrice[i])%></td>
<td class="uriage tbodyTd223" style="display:none"><%= Kanma.get(t_StayRoomPrice[i])%></td>
</tr>
<%
                }
                if (ownerinfo.SalesTotal!= 0 && ownerinfo.SalesTotalCount != 0 && result.getInt("split_flag") == 0 && result.getInt("owner_user_hotel.sales_disp_flag")== 1)
                {
                    count++;
                }
%><%
            }
        }
%></tbody>
<%
    }
    catch( Exception e )
    {
        Logging.error("foward Exception e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result, prestate, connection);
    }

    if( !dataExist )
    {
%>管理するホテルがありません<br>
<%
    }
    else
    {
        int sales_total_rate = 0;
        if (date_count_total != 0)
        {
           sales_total_rate = Math.round((float)sales_total_count*100 / (float)date_count_total);
        }
        int sales_rest_total_rate = 0;
        if (date_count_total != 0)
        {
           sales_rest_total_rate = Math.round((float)sales_rest_total_count*100 / (float)date_count_total);
        }
        int sales_stay_total_rate = 0;
        if (date_count_total != 0)
        {
           sales_stay_total_rate = Math.round((float)sales_stay_total_count*100 / (float)date_count_total);
        }
%><tr><th><%=count%>店舗合計</th>
<td class="uriage tbodyTd10"><%= Kanma.get(sales_total) %></td>
<td class="uriage tbodyTd11" style="display:none"><%= Kanma.get(sales_rest_total) %></td>
<td class="uriage tbodyTd12" style="display:none"><%= Kanma.get(sales_stay_total) %></td>
<td class="uriage tbodyTd200"><%= Kanma.get(sales_total_count) %></td>
<td class="uriage tbodyTd201" style="display:none"><%= (float)sales_total_rate / (float)100 %><% if((float)sales_total_rate%10==0){%>0<%}%></td>
<td class="uriage tbodyTd202" style="display:none"><% if(sales_total_count!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)sales_total_count))%><%}%></td>
<td class="uriage tbodyTd203" style="display:none"><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)room_count_total))%><%}%></td>
<td class="uriage tbodyTd210" style="display:none"><%= Kanma.get(sales_rest_total_count) %></td>
<td class="uriage tbodyTd211" style="display:none"><%= (float)sales_rest_total_rate / (float)100 %><% if((float)sales_rest_total_rate%10==0){%>0<%}%></td>
<td class="uriage tbodyTd212" style="display:none"><% if(sales_rest_total_count!=0){%><%=Kanma.get(Math.round((float)sales_rest_total /(float)sales_rest_total_count))%><%}%></td>
<td class="uriage tbodyTd213" style="display:none"><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_rest_total /(float)room_count_total))%><%}%></td>
<td class="uriage tbodyTd220" style="display:none"><%= Kanma.get(sales_stay_total_count) %></td>
<td class="uriage tbodyTd221" style="display:none"><%= (float)sales_stay_total_rate / (float)100 %><% if((float)sales_stay_total_rate%10==0){%>0<%}%></td>
<td class="uriage tbodyTd222" style="display:none"><% if(sales_stay_total_count!=0){%><%=Kanma.get(Math.round((float)sales_stay_total /(float)sales_stay_total_count))%><%}%></td>
<td class="uriage tbodyTd223" style="display:none"><% if(room_count_total!=0){%><%=Kanma.get(Math.round((float)sales_stay_total /(float)room_count_total))%><%}%></td>
</tr>
</table>
<%      if(SplitFlag)
        {
%>
<font size=1>(*)の店舗は合計に加算されません。</font>
<%
        }
%><%
    }
%>
<script>
function salesCacheChange(obj)
{
    var flag = obj.checked ? 1 : 0;
    Event.observe(
        'hotel_id',
        '',
        new Ajax.Request( '../../common/smart/salesCacheChange.jsp',
            {
                method: 'get',
                parameters: 'flag=' +flag
            }
        )
    );
}
</script>