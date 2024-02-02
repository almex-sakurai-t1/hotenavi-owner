<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<%@ page import="jp.happyhotel.data.*" %>
<%@ page import="jp.happyhotel.hotel.*" %>
<%@ include file="owner_userinfo.jsp" %>
<%
    String paramId = ReplaceString.getParameter(request,"id");
%>
<%@ include file="owner_hotelcheck.jsp" %>
<%

	// 今日の日付
    Calendar cal = Calendar.getInstance();
    int now_year  = cal.get(cal.YEAR);
    int now_month = cal.get(cal.MONTH) + 1;
    int now_day   = cal.get(cal.DATE);
    int nowdate   = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime   = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);
    String  nowdate_s = Integer.toString(nowdate+100000000);
    String  nowtime_s = Integer.toString(nowtime+1000000);
    nowdate_s = nowdate_s.substring(1,5) + "/" + nowdate_s.substring(5,7) + "/" + nowdate_s.substring(7,9);
    nowtime_s = nowtime_s.substring(1,3) + ":" + nowtime_s.substring(3,5) + ":" + nowtime_s.substring(5,7);

    int wday;
    int year;
    int month;
    int mday;
    String[] arrWday = {"日", "月", "火", "水", "木", "金", "土"};

    String[] head_t  = new String[5];
    head_t[0] = "PC";
    head_t[1] = "i";
    head_t[2] = "au";
    head_t[3] = "y";
    head_t[4] = "合計";

    int[][]  count_t    = new int[24][5];
    int[]    total_t    = new int[5];
    int[]    date_t     = new int[24];

    //最終日
    int[]     last_day_t = new int[12];
    last_day_t[0]  = 31;
    last_day_t[1]  = 28;
	if (((now_year % 4 == 0) && (now_year % 100 != 0)) || (now_year % 400 == 0))
		{
		last_day_t[1] = 29;
		}
	else
		{
		last_day_t[1] = 28;
		}
    last_day_t[2]   = 31;
    last_day_t[3]   = 30;
    last_day_t[4]   = 31;
    last_day_t[5]   = 30;
    last_day_t[6]   = 31;
    last_day_t[7]   = 31;
    last_day_t[8]   = 30;
    last_day_t[9]   = 31;
    last_day_t[10]  = 30;
    last_day_t[11]  = 31;

//  前日の算出
    int pre_year  = now_year;
    int pre_month = now_month;
    int pre_day   = now_day;

	if(now_day != 0)
	{
		if (now_day == 1)
		{
			if (pre_month == 1)
			 {
			 pre_year = now_year - 1;
			 pre_month = 12;
			 pre_day  = 31;
			 }
			 else
		     {
			 pre_month = pre_month - 1;
			 pre_day   = last_day_t[pre_month - 2];
		     }
		}
	 	else
		{
		pre_day = now_day - 1;
		}
	}
	else
	if (pre_month == 1)
	    {
		pre_year = now_year - 1;
		pre_month = 12;
		}
		else
		{
		pre_month = pre_month - 1;
		}

	int predate     = pre_year * 10000 + pre_month * 100 + pre_day;

    int    start_year  = pre_year;
    int    start_month = pre_month;
    int    start_day   = pre_day;

    String input_year  = ReplaceString.getParameter(request,"YEAR");
    String input_month = ReplaceString.getParameter(request,"MONTH");
    String input_day   = ReplaceString.getParameter(request,"DAY");
    if (input_year   != null) {start_year  = Integer.parseInt(input_year);}
    if (input_month  != null) {start_month = Integer.parseInt(input_month);}
    if (input_day    != null) {start_day   = Integer.parseInt(input_day);}

	int last_day    = last_day_t[start_month - 1];
	if  (start_year == now_year && start_month == now_month)
	{
		last_day    = start_day;
	}

//    int from_ymd    = start_year * 10000 + start_month * 100;
//    int to_ymd      = start_year * 10000 + start_month * 100 + last_day;
    int from_ymd    = start_year * 10000 + start_month * 100 + start_day;
    int to_ymd      = start_year * 10000 + start_month * 100 + start_day;

	int       i            = 0;
	int       time_i       = 0;
	String    page_kind    = "";
	String    carrier_kind = "";
	int       access_count = 0;
    int       from_time    = 0;
    int       to_time      = 0;

    Connection db             = null;
    PreparedPreparedStatement  state  = null;
    ResultSet  result         = null;

//if(liteFlag == 0 || imediaFlag == 1)
if(imediaFlag == 2)
{
    db     = DBConnection.getConnection();
    query  = "SELECT disp_time,disp_useragent FROM hh_user_history WHERE id =?";
    query  = query + " AND disp_date >= ?";
    query  = query + " AND disp_date <= ?";
    state  = db.prepareStatement(query);
    state.setInt(1,Integer.parseInt(paramId));
    state.setInt(2,from_ymd);
    state.setInt(3,to_ymd);
    result = state.executeQuery();
    while( result.next() )
    {
        i  = result.getInt("disp_time") / 10000;
        if (result.getString("disp_useragent").compareTo("i") == 0)
        {
            count_t[i][1]++;
        }
        else
        if (result.getString("disp_useragent").compareTo("au") == 0)
        {
            count_t[i][2]++;
        }
        else
        if (result.getString("disp_useragent").compareTo("y") == 0)
        {
            count_t[i][3]++;
        }
        else
        {
            count_t[i][0]++;
        }
    }
    DBConnection.releaseResources(result,state,db);
}

    int count_max = 1;
    for( time_i = 0 ; time_i < 24 ; time_i++ )
    {
         count_t[time_i][4] = count_t[time_i][0] + count_t[time_i][1] + count_t[time_i][2] + count_t[time_i][3];
         if (count_max < count_t[time_i][4])
         count_max =  count_t[time_i][4];
    }
//    count_max = (count_max + 100)/100;
//    count_max = count_max * 100;

    int  total_myhotel = 0;
    int  total_myhotel_now = 0;
    int  total_history = 0;


    NumberFormat nf = new DecimalFormat("00");

    db     = DBConnection.getConnection();

    query = "SELECT count(*) FROM hh_user_history WHERE  id =?";
    state  = db.prepareStatement(query);
    state.setInt(1,Integer.parseInt(paramId));
    result = state.executeQuery();

    if( result.next() )
    {
        total_history = result.getInt(1);
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(state);

    query = "SELECT count(*) FROM hh_user_myhotel,hh_user_basic WHERE  id =?";
    query = query + " AND hh_user_myhotel.del_flag = 0";
    query = query + " AND hh_user_myhotel.user_id = hh_user_basic.user_id";
    query = query + " AND hh_user_basic.del_flag = 0";
    state  = db.prepareStatement(query);
    state.setInt(1,Integer.parseInt(paramId));
    result = state.executeQuery();
	if( result.next() )
	{
	    total_myhotel_now = result.getInt(1);
	}
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(state);


    query = "SELECT count(*) FROM hh_user_myhotel WHERE  id =?";
    state  = db.prepareStatement(query);
    state.setInt(1,Integer.parseInt(paramId));
    result = state.executeQuery();
    if( result.next() )
    {
        total_myhotel = result.getInt(1);
    }
    DBConnection.releaseResources(result,state,db);

    HotelBasicInfo   hbi;
    boolean          ret;
    {
        if( paramId.compareTo("null") != 0 && CheckString.numCheck( paramId ) != false )
        {
            hbi = new HotelBasicInfo( );
            ret = hbi.getHotel( Integer.parseInt(paramId) );
            if( ret != false )
            {

%>
<div id="main">
<%
if   (!memberFlag || !hotelcheckFlag)
{
%>
					<div class="hotelEntry lvStandard">
						<div class="basicInfo">
									<h3>ログアウトしてしますので、再度ログインしてください。</font></h3>
						</div>
					</div>
<%
}
else
{
//   if (liteFlag == 0 || imediaFlag == 1)
   if (imediaFlag == 2)
   {
%>

	<div id="box-intro">
		<div class="hotelEntry lvStandard">
			<div class="basicInfo">
					<div class="area"><%= hbi.getHotelInfo().getPrefName() %>・<%= hbi.getHotelInfo().getAddress1() %></div>
					<h3><%= hbi.getHotelInfo().getName() %></h3>
			</div>
		</div>
		<div id="txt-info"><p>　　　　　　　　　現在日時（<%= nowdate_s %> <%= nowtime_s %>）</p>
		</div>
	</div>
	<div>
		<td align="left">&nbsp;
			<select name="ReportYear">
			<%  for ( i = 2007 ; i <= now_year; i++){%>
					<option value="<%= i %>" <% if (i == start_year){%> selected <%}%>><%= i %>
			<%  }%>
				</select><small>年</small>
				<select name="ReportMonth">
			<%  for ( i = 1 ; i <= 12; i++){%>
					<option value="<%= i %>" <% if (i == start_month){%> selected <%}%>><%= i %>
			<%  }%>
				</select><small>月</small>
				<select name="ReportDay">
			<%  for ( i = 1 ; i <= 31; i++){%>
					<option value="<%= i %>" <% if (i == start_day){%> selected <%}%>><%= i %>
			<%  }%>
				</select><small>日</small>
				<input type="button" value="切替" onClick="OwnerSelectReportChangeTimeFunc('<%=loginHotelId%>','<%=loginUserId%>','<%=paramCache%>','1')">
		</td>
	</div>
	<div align="center">
	<tr>
		<td align="center">
			<table width="520" border="0" cellspacing="1" cellpadding="1" class="search_hoteldetail">
				<tr>
					<td class="hoteldetail_top" rowspan="2" width="90">時間帯<br></td>
					<td class="hoteldetail_top" rowspan="2" width="30">PC</td>
					<td class="hoteldetail_top" colspan="3" width="90">携帯</td>
					<td class="hoteldetail_top" rowspan="2" width="30">合計</td>
					<td class="hoteldetail_top" rowspan="2" ><!--今までの総表示件数:<%=total_history%><br>-->マイホテル登録会員数:<%=total_myhotel_now%></td>
				</tr>
				<tr>
					<td class="hoteldetail_top" align="center" colspan="1" width="30">i</td>
					<td class="hoteldetail_top" align="center" colspan="1" width="30">AU</td>
					<td class="hoteldetail_top" align="center" colspan="1" width="30">Y!</td>
				</tr>
<%
    for( time_i = 0 ; time_i < 24 ; time_i++ )
    {
     String disp_time = nf.format(time_i) + ":00 ～ " + nf.format(time_i) + ":59";
%>
				<tr>
					<td class="honbun_small" style="text-align:right"><%= disp_time %></td>
<%
		for( i = 0 ; i < 5 ; i++ )
		{
						total_t[i] = total_t[i] + count_t[time_i][i];
%>
					<td class="honbun_small" style="text-align:right">
						<%= count_t[time_i][i]%>&nbsp;
					</td>
<%
		}
%>
					<td class="honbun_small">
						<img src="images/graph.gif" width="<%=(200 * count_t[time_i][4])/count_max%>" height="5">
					</td>
<%
	}
%>
				</tr>
			</table>
			<table width="520" border="0" cellspacing="1" cellpadding="0" class="search_hoteldetail">
				<tr class="result">
					<td class="hoteldetail_top" nowrap width="90">
						合　計
					</td>
<%

		for( i = 0 ; i < 5 ; i++ )
		{
%>
					<td valign="middle"  class="hoteldetail_cel" style="text-align:right" width="30">
						<%= total_t[i]%>
					</td>
<%
		}
%>
					<td valign="middle"  class="hoteldetail_cel" style="text-align:right">
					&nbsp;
					</td>
				</tr>
			</table>
<!--
			<table width="520" border="0" cellspacing="1" cellpadding="0" class="search_hoteldetail">
				<tr>
					<td class="honbun_small" style="text-align:left">
					　ただ今、6月23日以前はご覧になれません。少々お待ちください。(6/30復旧予定）
					</td>
				</tr>
			</table>
-->
<%
}
else
  if (liteFlag == 0 || imediaFlag == 1)
{
%>
	<div id="box-intro">
		<div class="hotelEntry lvStandard">
			<div class="basicInfo">
					<div class="area"><%= hbi.getHotelInfo().getPrefName() %>・<%= hbi.getHotelInfo().getAddress1() %></div>
					<h3><%= hbi.getHotelInfo().getName() %></h3>
			</div>
		</div>
		<div id="txt-info"><p>　　　　　　　　　現在日時（<%= nowdate_s %> <%= nowtime_s %>）</p>
		</div>
	</div>
	<div>
		<td align="left">&nbsp;
			<select name="ReportYear">
			<%  for ( i = 2007 ; i <= now_year; i++){%>
					<option value="<%= i %>" <% if (i == start_year){%> selected <%}%>><%= i %>
			<%  }%>
				</select><small>年</small>
				<select name="ReportMonth">
			<%  for ( i = 1 ; i <= 12; i++){%>
					<option value="<%= i %>" <% if (i == start_month){%> selected <%}%>><%= i %>
			<%  }%>
				</select><small>月</small>
				<select name="ReportDay">
			<%  for ( i = 1 ; i <= 31; i++){%>
					<option value="<%= i %>" <% if (i == start_day){%> selected <%}%>><%= i %>
			<%  }%>
				</select><small>日</small>
				<input type="button" value="切替" onClick="OwnerSelectReportChangeTimeFunc('<%=loginHotelId%>','<%=loginUserId%>','<%=paramCache%>','1')">
		</td>
	</div>
	<div align="center">
	<tr>
		<td align="center">
			<table width="520" border="0" cellspacing="1" cellpadding="1" class="search_hoteldetail">
				<tr>
					<td class="hoteldetail_top" rowspan="2" width="90">時間帯<br></td>
					<td class="hoteldetail_top" rowspan="2" width="30">PC</td>
					<td class="hoteldetail_top" colspan="3" width="90">携帯</td>
					<td class="hoteldetail_top" rowspan="2" width="30">合計</td>
					<td class="hoteldetail_top" rowspan="2" ><!--今までの総表示件数:<%=total_history%><br>-->マイホテル登録会員数:<%=total_myhotel_now%></td>
				</tr>
				<tr>
					<td class="hoteldetail_top" align="center" colspan="1" width="30">i</td>
					<td class="hoteldetail_top" align="center" colspan="1" width="30">AU</td>
					<td class="hoteldetail_top" align="center" colspan="1" width="30">Y!</td>
				</tr>
					<td class="honbun_small" style="text-align:left" colspan="7">
						ただ今準備中です。しばらくお待ちください。
					</td>
				</tr>
			</table>

<%
}
else
{
%>
	<div id="box-intro">
		<div class="hotelEntry lvLight">
			<div class="basicInfo">
					<div class="area"><%= hbi.getHotelInfo().getPrefName() %>・<%= hbi.getHotelInfo().getAddress1() %></div>
					<h3><%= hbi.getHotelInfo().getName() %></h3>
			</div>
		</div>
		<div class="hotelEntry lvStandard">
			<div  class="specInfo" >
			今までの総表示件数:<%=total_history%><br>
			</div>
		</div>
		<div class="hotelEntry>
			<div class="basicInfo">
				<h3>「ライトコース」のため表示が制限されております。</h3>
			</div>
		</div>
		<div class="hotelEntry lvStandard">
			<div  class="specInfo" >
「スタンダードコース」にご変更いただいた場合は、時間帯別のページアクセス
状況がグラフで分かる時間帯別アクセス状況をご確認いただけます。<br>
<a href="../owner/owner_contactform.jsp?hid=<%=loginHotelId%>&id=<%=liteId%>&Case=2">⇒お問い合わせ</a><br>
<img src="images/hourpv.jpg">
			</div>
		</div>

<img src="images/spacer.gif" width="10" height="1">

	</div>

<%
}
%>

		</td>
	</tr>
	<tr>
	<tr>
			<td height="1" class="honbun_small" colspan="3"><img src="images/spacer.gif" width="10" height="1"></td>
	</tr>
	</div>
<%
}
%>
</div>
<%
}}}
%>

