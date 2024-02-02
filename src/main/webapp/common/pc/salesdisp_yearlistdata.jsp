<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="jp.happyhotel.common.DateEdit" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String loginId      = (String)session.getAttribute("LoginId");
    boolean DemoMode = false;
    if (loginHotelId.equals("demo") && loginId.equals("demo"))
    {
        DemoMode = true;
    }
%>
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
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
        hotelid = "";
    }
    String param_compare          = ReplaceString.getParameter(request,"Compare");
    if (param_compare == null)
    {
        param_compare = "false";
    }
    String paramKind   = ReplaceString.getParameter(request,"Kind");
    if (paramKind == null)
    {
        paramKind = "YEARLIST";
    }

    boolean TotalMode = false;
    if (paramKind.indexOf("LISTR") != -1)
    {
        TotalMode = true;
    }

%>
<%-- 月別売上一覧表示処理 --%>
<%

    int             i;
    StringFormat    sf;
    NumberFormat    nf;

    sf = new StringFormat();
    nf = new DecimalFormat("00");

    int       target_date = 0;
    int       target_day;

    // 現在日付
    String nowDate = getCurrentDateString();
    int now_date   = Integer.parseInt(nowDate);
    int now_year   = now_date/10000;
    int now_month  = now_date/100%100;
    int now_day    = now_date%100;

    // 計上日付
    int cal_date  = ownerinfo.Addupdate;
    int cal_year  = cal_date/10000;
    int cal_month = cal_date/100%100;
    int cal_day   = cal_date%100;
    if  (cal_date == 0)
    {
        cal_date = now_date;
    }
    int   start_date = cal_date;

    String startYear  = ReplaceString.getParameter(request,"StartYear");
    String startMonth = ReplaceString.getParameter(request,"StartMonth");
    String startDay   = "1";
    if( startYear != null && startMonth != null && startDay != null )
    {
        start_date = (Integer.valueOf(startYear).intValue() * 10000) + (Integer.valueOf(startMonth).intValue() * 100) + Integer.valueOf(startDay).intValue();
    }
    else if (start_date == 0)
    {
        start_date = now_date;
    }
    String paramDate = Integer.toString(start_date);

    int start_year  = start_date/10000;
    int start_month = start_date/100%100;
    int start_day   = start_date%100;

    int end_date    = 0;
    String paramEndDate = paramDate;

    String endYear  = ReplaceString.getParameter(request,"EndYear");
    String endMonth = ReplaceString.getParameter(request,"EndMonth");
    if( endYear != null && endMonth != null  )
    {
        end_date = (Integer.valueOf(endYear).intValue() * 10000) + (Integer.valueOf(endMonth).intValue() * 100) + 1;
    }
    if (end_date == 0)
    {
        if (cal_date/100 == start_date/100)
        {
            end_date  = start_date + cal_day - 1;//当月なので計上日付
        }
        else
        {
            end_date  = start_date + getCalendar(paramDate).getActualMaximum(Calendar.DATE) - 1;//月末日付
        }
    }
    int end_year    = end_date/10000;
    int end_month   = end_date/100%100;
    int end_day     = end_date%100;
    paramEndDate    = Integer.toString(end_date);
    int      imedia_user = 0;
    int      level = 0;
    int      sales_cache_flag = 0;


    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
     // imedia_user のチェック
    try
    {
        connection  = DBConnection.getConnection();
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
        DBConnection.releaseResources(result,prestate,connection);
    }

%>
<style>
  *.holiday {background-color: #FFDDDD!important;}
  *.saturday {background-color: #DDDDFF!important;}
#arrow_box {
  display: none;
  position: absolute;
  padding: 16px;
  -webkit-border-radius: 8px;
  -moz-border-radius: 8px;  
  border-radius: 8px;
  background: #333;
  color: #fff;
  left: 45%;
  top: 50px;
  opacity: 80%;
}

#arrow_box:after {
  position: absolute;
  bottom: 100%;
  left: 50%;
  width: 0;
  height: 0;
  margin-left: -10px;
  border: solid transparent;
  border-color: rgba(51, 51, 51, 0);
  border-bottom-color: #333;
  border-width: 10px;
  pointer-events: none;
  content: " ";
}
</style>
<script type="text/javascript" src="../../common/pc/scripts/prototype.js"></script>
<div style="text-align:right;margin: -20px 0 5px 0;" class="size14">
<input id="sales_cache"  type="checkbox" onclick="salesCacheChange(this);" value="1" onMouseover="document.getElementById('arrow_box').style.display='block';" onMouseout="document.getElementById('arrow_box').style.display='none';" <%if (sales_cache_flag == 1){%> checked <%}%>>キャッシュ
<input type="button" onclick="document.form1.action='../../common/pc/salesdisp_yearlist_csv.jsp';document.form1.submit();" value="CSV" style="float:right">
<p id="arrow_box">チェックをはずすとホテルに接続して売上情報を取得します</p>
</div>
            <tr>
              <td align="center" valign="middle" nowrap class="size14 tableLN">年月</td>
<%
    if (TotalMode)
    {
%>
              <td align="center" valign="middle" nowrap class="size14 tableLN">売上金額</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">（累計）</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">組数</td>
              <td align="center" valign="middle" nowrap class="size14 tableRN">（累計）</td>
<%
    }
    else
    {
%>
              <td align="center" valign="middle" nowrap class="size14 tableLN">売上金額</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">組数</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">回転数</td>
              <td align="center" valign="middle" nowrap class="size14 tableLN">客単価</td>
              <td align="center" valign="middle" nowrap class="size14 tableRN">部屋単価</td>
<%
    }
%>
            </tr>
<%
    //再取得時用
    int[]    t_target_date     = new int[12];
    int[]    t_SalesTotal      = new int[12];
    int[]    t_SalesTotalCount = new int[12];
    int[]    t_SalesTotalRate  = new int[12];
    int[]    t_SalesTotalPrice = new int[12];
    int[]    t_SalesRoomPrice  = new int[12];
    int[]    t_SalesRoomTotalPrice  = new int[12];
    String   paramData         = "";
    for( i = 0 ; i < 12 ; i++ )
    {
        paramData =  ReplaceString.getParameter(request,"target_date_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_target_date[i]       = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotal_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotal[i]        = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalCount_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalCount[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalRate_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalRate[i]    = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesTotalPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesTotalPrice[i]   = Integer.parseInt(paramData);
        paramData =  ReplaceString.getParameter(request,"SalesRoomTotalPrice_" + param_compare + "_" + i);
        if (paramData == null)   paramData = "0";
        t_SalesRoomTotalPrice[i]   = Integer.parseInt(paramData);
    }

    //累計
    int      sales_total = 0;
    int      sales_total_count =0 ;
    int      room_count  = 0;
    int      date_count = 0 ;
    int      month_count = 0 ;
    int      total_rate = 0;

    boolean      getresult = true;

    try
    {
        for( i = 0 ; i < 12; i++ )
        {
            target_date = start_date + i*100;
            if(target_date/100%100 > 12)
            {
                target_date = target_date + 8800;
            }
            if (target_date > end_date)
            {
                break;
            }
            // 売上取得日付のセット
            ownerinfo.SalesGetStartDate = target_date/100 * 100;
            ownerinfo.SalesGetEndDate  = 0;

            getresult = true;
            if (t_SalesTotal[i] == 0 && t_SalesTotalCount[i]==0)
            {
                if (DemoMode)
                {
                    float per = ((float)ownerinfo.SalesGetStartDate-20050325)/(float)50000;
                    t_SalesTotal[i]      = (int)(Math.round(2525000*per));
                    t_SalesTotalCount[i] = (int)(Math.round(260*per));
                    t_SalesTotalRate[i]  = 300;
                    t_SalesTotalPrice[i] = (int)(Math.round(28750*per));
                    t_SalesRoomPrice[i]  = (int)(Math.round(226250*per));
                    t_SalesRoomTotalPrice[i]  = (int)(Math.round(226250*per));
                }
                else
                {
                    if (ownerinfo.SalesGetStartDate < (cal_date/100)*100)
                    {
                        getresult = ownerinfo.sendPacket0102(1, hotelid,sales_cache_flag);
                    }
                    else
                    {
                        getresult = ownerinfo.sendPacket0102(1, hotelid);
                    }
                    if (getresult)
                    {
                        t_SalesTotal[i]      = ownerinfo.SalesTotal;
                        t_SalesTotalCount[i] = ownerinfo.SalesTotalCount;
                        t_SalesTotalRate[i]  = ownerinfo.SalesTotalRate;
                        t_SalesTotalPrice[i] = ownerinfo.SalesTotalPrice;
                        t_SalesRoomPrice[i]  = ownerinfo.SalesRoomPrice;
                        t_SalesRoomTotalPrice[i]  = ownerinfo.SalesRoomTotalPrice;
                    }
                }
            }

            sales_total        += t_SalesTotal[i];
            sales_total_count  += t_SalesTotalCount[i];

            room_count = 0;
            if (t_SalesRoomTotalPrice[i] != 0)
            {
                room_count    = Math.round((float)t_SalesTotal[i] / (float)t_SalesRoomTotalPrice[i]);
            }
            if(getresult)
            {
                if (t_SalesTotal[i] != 0)
                {
                    month_count++;
                    if (target_date/100 == cal_date/100)
                    {
                        date_count = date_count + cal_date % 100;
                    }
                    else  if (target_date/100 < cal_date/100)
                    {
                        date_count = date_count + DateEdit.getLastDayOfMonth(target_date) % 100;
                    }
                }
            }
            total_rate = 0;
            if (date_count != 0)
            {
               total_rate = Math.round((float)sales_total_count*100 / ((float)date_count * (float)room_count));
            }
            String tdColor  = "";
            String tdTitle  = "";
            String backgroundColor  = "";
%>
                <tr height="24px">
                  <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="target_date_<%=param_compare%>_<%=i%>" id="target_date_<%=param_compare%>_<%=i%>" value="<%=target_date%>">
                      <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();">
                        <%if(i == 0 || target_date/100%100 == 1){%><%= target_date/10000%>年&nbsp;<%}%><%= target_date/100%100 %>月
                      </a>
                  </td>
<%
            if (!getresult)//取得できなかった
            {
%>
                  <td  colspan="5" align="middle" valign="middle" class="size14 tableRW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>>　取得できませんでした。<input type="button" value="再取得" onclick="document.form1.submit();" style="padding:0px 6px"></td>
<%
            }
            else
            {
                if (TotalMode)
                {
%>                <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotal_<%=param_compare%>_<%=i%>"           id="SalesTotal_<%=param_compare%>_<%=i%>"           value="<%=t_SalesTotal[i]%>">     <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotal[i]) %></a></td>
                  <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalTotal_<%=param_compare%>_<%=i%>"      id="SalesTotalTotal_<%=param_compare%>_<%=i%>"      value="<%=sales_total%>">         <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();"><%= Kanma.get(sales_total) %></a></td>
                  <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalCount_<%=param_compare%>_<%=i%>"      id="SalesTotalCount_<%=param_compare%>_<%=i%>"      value="<%=t_SalesTotalCount[i]%>"><a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotalCount[i]) %></a></td>
                  <td align="right" valign="middle" class="size14 tableRW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalTotalCount_<%=param_compare%>_<%=i%>" id="SalesTotalTotalCount_<%=param_compare%>_<%=i%>" value="<%=sales_total_count%>">   <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();"><%= Kanma.get(sales_total_count) %></a></td>
                  <input type="hidden" name="SalesTotalRate_<%=param_compare%>_<%=i%>"  id="SalesTotalRate_<%=param_compare%>_<%=i%>"  value="<%=t_SalesTotalRate[i]%>">
                  <input type="hidden" name="SalesTotalPrice_<%=param_compare%>_<%=i%>" id="SalesTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalPrice[i]%>">
                  <input type="hidden" name="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>"  id="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>"  value="<%=t_SalesRoomTotalPrice[i]%>">
<%
                }
                else
                {
%>                <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotal_<%=param_compare%>_<%=i%>"      id="SalesTotal_<%=param_compare%>_<%=i%>"      value="<%=t_SalesTotal[i]%>">     <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotal[i]) %></a></td>
                  <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalCount_<%=param_compare%>_<%=i%>" id="SalesTotalCount_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalCount[i]%>"><a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotalCount[i]) %></a></td>
                  <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalRate_<%=param_compare%>_<%=i%>"  id="SalesTotalRate_<%=param_compare%>_<%=i%>"  value="<%=t_SalesTotalRate[i]%>"> <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();"><%= (float)t_SalesTotalRate[i] / (float)100 %><% if((float)t_SalesTotalRate[i]%10==0){%>0<%}%></a></td>
                  <td align="right" valign="middle" class="size14 tableLW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesTotalPrice_<%=param_compare%>_<%=i%>" id="SalesTotalPrice_<%=param_compare%>_<%=i%>" value="<%=t_SalesTotalPrice[i]%>"><a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();"><%= Kanma.get(t_SalesTotalPrice[i]) %></a></td>
                  <td align="right" valign="middle" class="size14 tableRW<%=tdColor%>" title="<%=tdTitle%>" nowrap <%=backgroundColor%>><input type="hidden" name="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>"  id="SalesRoomTotalPrice_<%=param_compare%>_<%=i%>"  value="<%=t_SalesRoomTotalPrice[i]%>"> <a href="#" class="size14" onclick="document.form1.action='salesdisp_select.jsp';document.form1.StartYearfromList.value=<%=target_date/10000%>;document.form1.StartMonthfromList.value=<%=target_date/100%100%>;document.form1.submit();"><%= Kanma.get(t_SalesRoomTotalPrice[i]) %></a></td>
<%
                }
            }
%>
                </tr>
<%
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }

    if(!TotalMode)
    {
%>
            <tr>
              <td align="center" valign="middle" class="size14 tableLB" nowrap>合計</td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(sales_total) %></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(sales_total_count) %></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= (float)total_rate / (float)100 %><% if((float)total_rate%10==0){%>0<%}%></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><% if(sales_total_count!=0){%><%=Kanma.get(Math.round((float)sales_total /(float)sales_total_count))%><%}else{%>－<%}%></td>
              <td align="right" valign="middle" class="size14 tableRB" nowrap><% if(room_count!=0){%><%=Kanma.get(Math.round((float)sales_total/(float)room_count))%><%}else{%>－<%}%></td>
            </tr>
            <tr>
              <td align="center" valign="middle" class="size14 tableLB" nowrap>平均</td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(Math.round((float)sales_total/(float)month_count)) %></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap><%= Kanma.get(Math.round((float)sales_total_count/(float)month_count)) %></td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap>－</td>
              <td align="right" valign="middle" class="size14 tableLB" nowrap>－</td>
              <td align="right" valign="middle" class="size14 tableRB" nowrap><% if(room_count!=0){%><%=Kanma.get(Math.round((float)sales_total/((float)month_count*(float)room_count)))%><%}else{%>－<%}%></td>
            </tr>
<%
    }
%>
            <tr height="24px">
              <td align="left" valign="middle"  class="size14"  colspan=3>
                <input name="Kind"         type="hidden" value="<%=paramKind%>">
                <input name="KindfromList" type="hidden" value="DATE">
                <input name="Compare"      type="hidden" value="<%=param_compare%>">
                <input name="StartYear"    type="hidden" value="<%=startYear%>">
                <input name="StartMonth"   type="hidden" value="<%=startMonth%>">
                <input name="StartDay"   type="hidden" value="0">
                <input name="StartYearfromList"    type="hidden" value="<%=startYear%>">
                <input name="StartMonthfromList"   type="hidden" value="<%=startMonth%>">
                <input name="StartDayfromList"   type="hidden" value="0">
                <input name="EndYear"    type="hidden" value="<%=endYear%>">
                <input name="EndMonth"   type="hidden" value="<%=endMonth%>">
                <input name="Max"        type="hidden" value="<%=i%>">
              </td>
<%
    if(TotalMode)
    {
%>
              <td align="left" valign="middle"  class="size14"  colspan=2>
                <input type="button" onclick="document.form1.Kind.value='<%=paramKind.replace("LISTR","LIST")%>';document.form1.submit();" value="通常表示">
<%
    }
    else
    {
%>
              <td align="left" valign="middle"  class="size14"  colspan=3>
                <input type="button" onclick="document.form1.Kind.value='<%=paramKind%>R';document.form1.submit();" value="月別累計表示">
<%
    }
%>
              </td>
            </tr>
<script>
function salesCacheChange(obj)
{
    var flag = obj.checked ? 1 : 0;
    Event.observe(
        'hotel_id',
        '',
        new Ajax.Request( '../../common/pc/salesCacheChange.jsp',
            {
                method: 'get',
                parameters: 'flag=' +flag
            }
        )
    );
}
</script>