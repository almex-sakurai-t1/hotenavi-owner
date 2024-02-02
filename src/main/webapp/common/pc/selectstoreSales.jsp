<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/owner_ini.jsp" %>
<%!
    private static Calendar getCalendar(String currentDate) {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        if (currentDate == null || currentDate.length() != 8) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            int year = Integer.parseInt(currentDate.substring(0, 4));
            int month = Integer.parseInt(currentDate.substring(4, 6)) - 1;
            int date = Integer.parseInt(currentDate.substring(6, 8));
            calendar.set(year, month, date);
        }
        return calendar;
    }
    private static String formatDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        return simpleDateFormat.format(calendar.getTime());
    }
    private static String getCurrentDateString() {
        return formatDate(getCalendar(null));
    }
%>
<%
    int          count = 0;
    boolean      ret;
    String       query     = "";
    String       query_sub = "";
    String       hotelname = "";
    int          storecount = 0;
    String       selecthotel;
    String       param_store;
    int          imedia_user        = 0;
    int          level              = 0;
    boolean      SelectExist = true;

    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("demo"))
    {
        DemoMode = true;
    }

    DbAccess db_security =  new DbAccess();
    // セキュリティ情報の取得
    ResultSet DbUserSecurity = ownerinfo.getUserSecurity(db_security, loginHotelId, ownerinfo.DbUserId);

    // セキュリティチェック
    if( DbUserSecurity == null )
    {
        db_security.close();
%>
        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    if( DbUserSecurity.first() == false )
    {
        db_security.close();
%>

        <jsp:forward page="../../common/pc/error/notlogin.html" />
<%
    }
    db_security.close();

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
 
    connection  = DBConnection.getConnection();
     // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }


    String paramTarget     = request.getParameter("Target");
//    if (paramTarget == null) paramTarget = "mainFrame";


    selecthotel = (String)session.getAttribute("SelectHotel");

    if( selecthotel == null )
    {
        selecthotel = "all_manage";
    }
    String paramStoreCount = request.getParameter("StoreCount");
    storecount             = Integer.parseInt(paramStoreCount);
    param_store            = request.getParameter("Store");
    if( param_store != null )
    {
        selecthotel = param_store;
    }

    if (imedia_user == 1 && level == 3)
    {
       query_sub = query_sub + " AND (hotel.plan = 1 OR hotel.plan = 2 OR hotel.plan=98) ";
    }
    else
    {
       query_sub = query_sub + " AND hotel.plan >= 1";
       query_sub = query_sub + " AND hotel.plan <= 2";
    }

    // セッション属性に選択ホテルをセットする
    session.setAttribute("SelectHotel", selecthotel);

    String paramKind   = request.getParameter("Kind");
    if (paramKind == null)
    {
        paramKind = "DATE";
    }
//    if( selecthotel.compareTo("all_manage") == 0 || selecthotel.compareTo("all") == 0)
//    {
//        paramKind = paramKind.replace("LIST","");
//    }
    if( selecthotel.compareTo("all") == 0)
    {
        paramKind = paramKind.replace("LIST","");
    }
    String dateRange = request.getParameter("Range");
    if (dateRange==null)
    {
        dateRange = "1";
    }
    boolean SubmitOK = true;
    if ((dateRange.equals("0") || dateRange.equals("1")) && paramKind.indexOf("RANGE") == 0)
    {
       SubmitOK = false;
    }
    String paramSubmitOK = request.getParameter("SubmitOK");
    if ( paramSubmitOK == null)
    {
         paramSubmitOK = "true";
    }
    if  ( paramSubmitOK.equals("false"))
    {
        SubmitOK = false;
    }
    boolean CompareMode = true;
    String paramCompare = request.getParameter("Compare");
    if (paramCompare == null)
    {
        paramCompare = "false";
    }
    if  (paramCompare.equals("false"))
    {
        CompareMode = false;
    }
    boolean CompareSubmit = true;
    String paramCompareSubmit = request.getParameter("CompareSubmit");
    if (paramCompareSubmit == null)
    {
        paramCompareSubmit = "true";
    }
    if  (paramCompareSubmit.equals("false"))
    {
        CompareSubmit = false;
    }
    String[] params = new String[]{ paramTarget, paramStoreCount, param_store, paramKind, dateRange, paramSubmitOK, paramCompare, paramCompareSubmit };
    for( String param : params )
    {
        if(CheckString.isValidParameter(param) && !CheckString.numAlphaCheck(param))
        {
            response.sendError(400);
            return;
        }
    }
    int  plan = 1;

    if  (storecount == 1)
    {  // 1件だけだった場合
        try
        {
            query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
            query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
            query = query + query_sub;
            prestate    = connection.prepareStatement(query);
            result      = prestate.executeQuery();
            if  (result != null)
            {
                if( result.next() != false )
                {
                    selecthotel = result.getString("hotel.hotel_id");
                    session.setAttribute("SelectHotel", selecthotel);
                }
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
    // セッション済みホテルが閲覧可能かどうかをチェックし、NGの場合はセッション内容を変更する。
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id =?";
        query = query + query_sub;
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, selecthotel);
        result      = prestate.executeQuery();
        if  (result != null)
        {
            if(!result.next())
            {
                if (storecount > 1)
                {
                    if (selecthotel.equals("all_manage")||selecthotel.equals("all"))
                    {
                        session.setAttribute("SelectHotel", selecthotel);
                    }
                    else
                    {
                        session.setAttribute("SelectHotel", "all");
                    }
                }
            }
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

    if( !selecthotel.equals("") && !selecthotel.equals("all") && !selecthotel.equals("all_manage"))  // 選択ﾎﾃﾙの計上日取得
    {
        ownerinfo.sendPacket0100(1,selecthotel);
    }
%>
<%
    if (storecount > 1 && ownerinfo.Addupdate == 0) //多店舗で計上日未取得のとき管理店舗1店舗目の計上日を取得する
    {
        try
        {
            query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
            query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
            query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
            query = query + query_sub;
            query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
            prestate    = connection.prepareStatement(query);
            result      = prestate.executeQuery();
            if  (result != null)
            {
                if(result.next())
                {
                    ownerinfo.sendPacket0100(1,result.getString("hotel.hotel_id"));
                }
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
%>
<%
//日付の受取り
    // 現在日付
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;
    
    if (DemoMode) //デモモードの場合は計上日を本日日付にする
    {
        ownerinfo.Addupdate = now_date;
    }

    int cal_date   = ownerinfo.Addupdate;
    int cal_year   = cal_date/10000;
    int cal_month  = cal_date/100%100;
    int cal_day    = cal_date%100;
    int start_date = cal_date;

    String startYear  = request.getParameter("StartYear");
    String startMonth = request.getParameter("StartMonth");
    String startDay   = request.getParameter("StartDay");
    if  (startDay != null)
    {
        if(startDay.equals("0")) startDay = "1";
    }
    if( startYear != null && startMonth != null && startDay != null )
    {
        start_date = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    if (start_date == 0)
    {
        start_date  = now_date;
    }
    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;

    int end_date    = start_date;
    String endYear  = request.getParameter("EndYear");
    String endMonth = request.getParameter("EndMonth");
    String endDay   = request.getParameter("EndDay");
    String[] endDates = new String[]{ startYear, startMonth, startDay, endYear, endMonth, endDay};
    for( String date : endDates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }

    if( endYear != null && endMonth != null && endDay != null )
    {
        end_date = (Integer.valueOf(endYear).intValue() * 10000) + (Integer.valueOf(endMonth).intValue() * 100) + Integer.valueOf(endDay).intValue();
    }
    if (end_date == 0)
    {
        end_date  = start_date;
    }
    int end_year  = end_date/10000;
    int end_month = end_date/100%100;
    int end_day   = end_date%100;

    String comparestartYear  = request.getParameter("CompareStartYear");
    String comparestartMonth = request.getParameter("CompareStartMonth");
    String comparestartDay   = request.getParameter("CompareStartDay");
    String compareendYear  = request.getParameter("CompareEndYear");
    String compareendMonth = request.getParameter("CompareEndMonth");
    String compareendDay   = request.getParameter("CompareEndDay");
    String[] comparedates = new String[]{ comparestartYear, comparestartMonth, comparestartDay, compareendYear, compareendMonth, compareendDay };
    for( String date : comparedates )
    {
        if ( date != null && !CheckString.numCheck( date ) )
        {
            response.sendError(400);
            return;
        }
    }
    int    compare_start_date = start_date;
    int    compare_end_date   = end_date;
    if( comparestartYear != null && comparestartMonth != null && comparestartDay != null )
    {
        compare_start_date = (Integer.valueOf(comparestartYear).intValue() * 10000) + (Integer.valueOf(comparestartMonth).intValue() * 100) + Integer.valueOf(comparestartDay).intValue();
    }
    int compare_start_year  = compare_start_date/10000;
    int compare_start_month = compare_start_date/100%100;
    int compare_start_day   = compare_start_date%100;
    if( compareendYear != null && compareendMonth != null && compareendDay != null )
    {
        compare_end_date = (Integer.valueOf(compareendYear).intValue() * 10000) + (Integer.valueOf(compareendMonth).intValue() * 100) + Integer.valueOf(compareendDay).intValue();
    }
    int compare_end_year  = compare_end_date/10000;
    int compare_end_month = compare_end_date/100%100;
    int compare_end_day   = compare_end_date%100;


%>
<%
    if (imedia_user == 1 && level == 3)
    {
%>
<tr>
<td height="20" width="60" align="left" valign="middle" nowrap bgcolor="#000000">
  <div class="white12" align="center">店舗選択</div>
</td>
<form action="sales_select.jsp" method="post" name="selectstore" <%if (paramTarget!=null){%>target="<%=paramTarget%>"<%}%>>
  <td  width="125" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <input name="StartYear"  type="hidden" id="StartYearStore"  value="<%=start_year%>">
    <input name="StartMonth" type="hidden" id="StartMonthStore" value="<%=start_month%>">
    <input name="StartDay"   type="hidden" id="StartDayStore"   value="<%=start_day%>">
    <input name="EndYear"    type="hidden" id="EndYearStore"    value="<%=end_year%>">
    <input name="EndMonth"   type="hidden" id="EndMonthStore"   value="<%=end_month%>">
    <input name="EndDay"     type="hidden" id="EndDayStore"     value="<%=end_day%>">
    <input name="CompareStartYear"  type="hidden" id="CompareStartYearStore"  value="<%=compare_start_year%>">
    <input name="CompareStartMonth" type="hidden" id="CompareStartMonthStore" value="<%=compare_start_month%>">
    <input name="CompareStartDay"   type="hidden" id="CompareStartDayStore"   value="<%=compare_start_day%>">
    <input name="CompareEndYear"    type="hidden" id="CompareEndYearStore"    value="<%=compare_end_year%>">
    <input name="CompareEndMonth"   type="hidden" id="CompareEndMonthStore"   value="<%=compare_end_month%>">
    <input name="CompareEndDay"     type="hidden" id="CompareEndDayStore"     value="<%=compare_end_day%>">
    <input name="SubmitOK"          type="hidden" id="SubmitOK"               value="<%=SubmitOK%>">
    <input name="Compare"           type="hidden" id="Compare"                value="<%=CompareMode%>">
    <input name="CompareSubmit"     type="hidden" id="CompareSubmit"          value="<%=CompareSubmit%>">
    <input name="Range"             type="hidden" id="Range"                  value="<%=dateRange%>">
    <%if (paramKind.equals("YEARLIST")) {%><input id="YearMode" name="YearMode" type="hidden" value="true"><%}%>
    <input name="Store" size="10" maxlength="10" onChange="<% if (SubmitOK&&CompareSubmit){%>top.Main.mainFrame.location.href='sales_wait.html';<%}else{%>top.Main.mainFrame.location.href='sales_page.html';<%}%>" value="<%if(!selecthotel.equals("all_manage")){%><%= selecthotel %><%}%>"><br>
  </td> 
</tr>
<%
    }
    else
    {
    // 2店舗以上の場合表示する
        if( storecount > 1 )
        {
%>
<tr>
<td height="20" width="60" align="left" valign="middle" nowrap bgcolor="#000000">
  <div class="white12" align="center">店舗選択</div>
</td>
<form action="sales_select.jsp" method="post" name="selectstore" <%if (paramTarget!=null){%>target="<%=paramTarget%>"<%}%>>
  <td  width="125" align="left" valign="middle" nowrap bgcolor="#FFFFFF">
    <input name="StartYear"  type="hidden" id="StartYearStore"  value="<%=start_year%>">
    <input name="StartMonth" type="hidden" id="StartMonthStore" value="<%=start_month%>">
    <input name="StartDay"   type="hidden" id="StartDayStore"   value="<%=start_day%>">
    <input name="EndYear"    type="hidden" id="EndYearStore"    value="<%=end_year%>">
    <input name="EndMonth"   type="hidden" id="EndMonthStore"   value="<%=end_month%>">
    <input name="EndDay"     type="hidden" id="EndDayStore"     value="<%=end_day%>">
    <input name="Range"      type="hidden" id="Range"           value="<%=dateRange%>">
    <input name="CompareStartYear"  type="hidden" id="CompareStartYearStore"  value="<%=compare_start_year%>">
    <input name="CompareStartMonth" type="hidden" id="CompareStartMonthStore" value="<%=compare_start_month%>">
    <input name="CompareStartDay"   type="hidden" id="CompareStartDayStore"   value="<%=compare_start_day%>">
    <input name="CompareEndYear"    type="hidden" id="CompareEndYearStore"    value="<%=compare_end_year%>">
    <input name="CompareEndMonth"   type="hidden" id="CompareEndMonthStore"   value="<%=compare_end_month%>">
    <input name="SubmitOK"          type="hidden" id="SubmitOK"               value="<%=SubmitOK%>">
    <input name="CompareEndDay"     type="hidden" id="CompareEndDayStore"     value="<%=compare_end_day%>">
    <input name="Compare"           type="hidden" id="Compare"                value="<%=CompareMode%>">
    <input name="CompareSubmit"     type="hidden" id="CompareSubmit"          value="<%=CompareSubmit%>">
    <%if (paramKind.equals("YEARLIST")) {%><input id="YearMode" name="YearMode" type="hidden" value="true"><%}%>
    <select name="Store" onChange="<% if (SubmitOK&&CompareSubmit){%>top.Main.mainFrame.location.href='sales_wait.html';<%}else{%>top.Main.mainFrame.location.href='sales_page.html';<%}%>" style="width:125px">
<%
            if( selecthotel.compareTo("") == 0)
            {
%>
		<option value="" selected>選択してください</option>
<%
            }
            if( selecthotel.compareTo("all_manage") == 0)
            {
%>
		<option value="all_manage" selected>管理店舗売上一覧</option>
<%
            }
            else
            {
%>
		<option value="all_manage">管理店舗売上一覧</option>
<%
            }
            if( selecthotel.compareTo("all") == 0 )
            {
%>
		<option value="all" selected>3店舗ごと</option>
<%
            }
            else
            {
%>
		<option value="all">3店舗ごと</option>
<%
            }
%>

<%
            try
            {
                query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid ='" + ownerinfo.HotelId + "'";
                query = query + " AND hotel.hotel_id = owner_user_hotel.accept_hotelid";
                query = query + " AND owner_user_hotel.userid = " + ownerinfo.DbUserId;
                query = query + query_sub;
                query = query + " ORDER BY hotel.sort_num,hotel.hotel_id";
                prestate    = connection.prepareStatement(query);
                result      = prestate.executeQuery();
                if  (result != null)
                {
                    while( result.next() != false )
                    {
                        
                        if( selecthotel.compareTo(result.getString("owner_user_hotel.accept_hotelid")) == 0 )
                        {
%>
		<option value="<%= result.getString("owner_user_hotel.accept_hotelid") %>" selected><%= result.getString("hotel.name") %></option>
<%
                        }
                        else
                        {
%>
		<option value="<%= result.getString("owner_user_hotel.accept_hotelid") %>"><%=result.getString("hotel.name") %></option>
<%
                        }
                    }
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
%>
    </select><br>
  </td> 
</tr>
<%
        }
        else
        { //1店舗の場合
%>
<form action="sales_select.jsp" method="post" name="selectstore" <%if (paramTarget!=null){%>target="<%=paramTarget%>"<%}%>>
    <input name="StartYear"  type="hidden" id="StartYearStore"  value="<%=start_year%>">
    <input name="StartMonth" type="hidden" id="StartMonthStore" value="<%=start_month%>">
    <input name="StartDay"   type="hidden" id="StartDayStore"   value="<%=start_day%>">
    <input name="EndYear"    type="hidden" id="EndYearStore"    value="<%=end_year%>">
    <input name="EndMonth"   type="hidden" id="EndMonthStore"   value="<%=end_month%>">
    <input name="EndDay"     type="hidden" id="EndDayStore"     value="<%=end_day%>">
    <input name="CompareStartYear"  type="hidden" id="CompareStartYearStore"  value="<%=compare_start_year%>">
    <input name="CompareStartMonth" type="hidden" id="CompareStartMonthStore" value="<%=compare_start_month%>">
    <input name="CompareStartDay"   type="hidden" id="CompareStartDayStore"   value="<%=compare_start_day%>">
    <input name="CompareEndYear"    type="hidden" id="CompareEndYearStore"    value="<%=compare_end_year%>">
    <input name="CompareEndMonth"   type="hidden" id="CompareEndMonthStore"   value="<%=compare_end_month%>">
    <input name="CompareEndDay"     type="hidden" id="CompareEndDayStore"     value="<%=compare_end_day%>">
    <input name="SubmitOK"          type="hidden" id="SubmitOK"               value="<%=SubmitOK%>">
    <input name="Compare"           type="hidden" id="Compare"                value="<%=CompareMode%>">
    <input name="CompareSubmit"     type="hidden" id="CompareSubmit"          value="<%=CompareSubmit%>">
    <input name="Range"             type="hidden" id="Range"                  value="<%=dateRange%>">
    <input name="Store" type="hidden" value="<%= selecthotel %>">
    <%if (paramKind.equals("YEARLIST")) {%><input id="YearMode" name="YearMode" type="hidden" value="true"><%}%>
<%
        }
    }
    DBConnection.releaseResources(connection);
%>
<tr>
  <td height="20" width="60" align="left" valign="middle" nowrap bgcolor="#000000">
    <div class="white12" align="center">表示形式</div>
  </td>
  <td align="center" class="white12" bgcolor="#000000">
      <select name="Kind" onChange="kindChange(this.value);" id="Kind" style="width:125px">
<%
    if( selecthotel.compareTo("all_manage") == 0 || selecthotel.compareTo("all") == 0)
    {
%>
         <option value="DATE"        <%if (paramKind.equals("DATE"))       {%>selected<%}%>>日計
         <option value="MONTH"       <%if (paramKind.equals("MONTH"))      {%>selected<%}%>>月計
         <option value="RANGE"       <%if (paramKind.equals("RANGE"))      {%>selected<%}%>>期間指定
         <option value="MONTHLIST"   <%if (paramKind.equals("MONTHLIST"))  {%>selected<%}%>>月次日別売上
<%
    }
    else
    {
%>
         <option value="DATE"        <%if (paramKind.equals("DATE"))       {%>selected<%}%>>日計
         <option value="MONTH"       <%if (paramKind.equals("MONTH"))      {%>selected<%}%>>月計
         <option value="MONTHLIST"   <%if (paramKind.equals("MONTHLIST"))  {%>selected<%}%>>月次日別売上
         <option value="RANGE"       <%if (paramKind.equals("RANGE"))      {%>selected<%}%>>期間指定
         <option value="RANGELIST"   <%if (paramKind.equals("RANGELIST"))  {%>selected<%}%>>期間指定日別売上
         <option value="CALENDAR"    <%if (paramKind.equals("CALENDAR"))   {%>selected<%}%>>カレンダー
         <option value="YEARLIST"    <%if (paramKind.equals("YEARLIST"))   {%>selected<%}%>>年次月別売上
<%
    }
%>
      </select>
  </td>
</form>
</tr>
<script type="text/javascript">
<!--
function kindChange(Kind){
    if  (document.getElementById("SubmitOK").value=="true")
    {
        if (Kind.indexOf('RANGE')!=0 || (document.selectstore.Range.value!='0' && document.selectstore.Range.value!='1'))
        {
            top.Main.mainFrame.location.href='sales_wait.html';
        }
        else
        {
            document.getElementById("SubmitOK").value="false";
            top.Main.mainFrame.location.href='sales_page.html';
            document.selectstore.submit();
        }
    }
    else
    {
        if (Kind.indexOf('RANGE')!=0 || (document.selectstore.Range.value!='0' && document.selectstore.Range.value!='1'))
        {
            document.getElementById("SubmitOK").value="true";
            top.Main.mainFrame.location.href='sales_wait.html';
        }
        else
        {
            document.getElementById("SubmitOK").value="false";
            top.Main.mainFrame.location.href='sales_page.html';
            document.selectstore.submit();
        }
    }
}
-->
</script>
