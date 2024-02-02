<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    String loginHotelId =  (String)session.getAttribute("LoginHotelId");
    String hotelid = (String)session.getAttribute("SelectHotel");
    if (hotelid.compareTo("all")==0)
    {
       hotelid = loginHotelId;
    }
    String limit_flag = ReplaceString.getParameter(request,"limit_flag");
    if(limit_flag == null) limit_flag = "false";
    if(limit_flag.compareTo("") == 0) limit_flag ="false";
    String coupon_no  = ReplaceString.getParameter(request,"coupon_no");
    if    (coupon_no == null) coupon_no = "";
    if   (!CheckString.numCheck(coupon_no)) coupon_no = "";
    String coupon_id  = ReplaceString.getParameter(request,"coupon_id");
    if    (coupon_id == null) coupon_id = "0";
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

    int    start_year  = now_year;
    int    start_month = now_month;

    String input_year  = ReplaceString.getParameter(request,"YEAR");
    String input_month = ReplaceString.getParameter(request,"MONTH");
    if (input_year   != null) {start_year  = Integer.parseInt(input_year);}
    if (input_month  != null) {start_month = Integer.parseInt(input_month);}
    int pageno;
    String param_page = ReplaceString.getParameter(request,"page");
    if( param_page == null )
    {
        pageno = 0;
    }
    else
    {
        pageno = Integer.parseInt(param_page);
    }
    int    hapihote_id = 0;
    int    count        = 0;
    int    total_count  = 0;
    String input_date_s = "";
    String input_time_s = "";
    String start_date_s = "";
    String end_date_s = "";
    String coupon_no_s = "";
    String user_status_s = "";
	String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection = DBConnection.getConnection();
    //ハピホテのIDを調べる
    try
    {
        query = "SELECT id FROM hh_hotel_basic WHERE hotenavi_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
           hapihote_id = result.getInt("id");
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

    try
    {
        query = "SELECT * FROM hh_user_coupon";
        query += " INNER JOIN hh_master_coupon";
        query += " ON hh_user_coupon.id = hh_master_coupon.id";
        query += " AND hh_user_coupon.seq = hh_master_coupon.seq";
        query += " AND hh_master_coupon.service_flag=2";
        query += " INNER JOIN edit_coupon";
        query += " ON edit_coupon.id = hh_master_coupon.coupon_no";
        query += " AND edit_coupon.hotelid =?";
        if(coupon_id.compareTo("0") != 0)
        {
            query = query + " AND edit_coupon.id =?";
        }
        query += " WHERE hh_user_coupon.id =?";
        if(limit_flag.equals("true"))
        {
            query += " AND hh_user_coupon.end_date >= "   + nowdate;
        }
        query += " AND hh_user_coupon.hotenavi_flag = 1";
        if(coupon_no.compareTo("") != 0)
        {
            query += " AND (hh_user_coupon.coupon_no%10000)  =" + coupon_no;
        }
        query += " ORDER BY hh_user_coupon.coupon_no DESC";
        query += " LIMIT " + pageno * 40 + "," + 41;
        int a = 1;
        prestate    = connection.prepareStatement(query);
        prestate.setString(a++, hotelid);
        if(coupon_id.compareTo("0") != 0)
        {
            prestate.setInt(a++, Integer.parseInt(coupon_id));
        }
        prestate.setInt(a++, hapihote_id);
        result      = prestate.executeQuery();
%>
	<div align="center">
	<tr>
		<td align="center">
		<table width="98%" border="0" cellspacing="1" cellpadding="0" >
			<tr>
				<td width="60" class="tableLN" align="center">表示日付</td>
				<td width="50" class="tableLN" align="center">時　刻</td>
				<td            class="tableLN" align="center" colspan="2">クーポン内容</td>
				<td width="80" class="tableLN" align="center">クーポンNo</td>
				<td width="90" class="tableLN" align="center">利用者</td>
				<td width="60" class="tableLN" align="center">利用期限</td>
				<td width="40" class="tableRN" align="center">ご来店</td>
			</tr>
<%
           while( result.next() )
           {
               count++;
               if (count > 40) break;
               total_count  = pageno * 40 + count;
               input_date_s = Integer.toString(result.getInt("hh_user_coupon.print_date")+100000000);
               input_time_s = Integer.toString(result.getInt("hh_user_coupon.print_time")+1000000);
               start_date_s = Integer.toString(result.getInt("hh_user_coupon.start_date")+100000000);
               end_date_s   = Integer.toString(result.getInt("hh_user_coupon.end_date")+100000000);
               coupon_no_s  = Integer.toString(result.getInt("edit_coupon.all_seq")+1000000).substring(4,7);
               coupon_no_s  = coupon_no_s + "-";
               coupon_no_s  = coupon_no_s + Integer.toString(result.getInt("hh_user_coupon.coupon_no")+1000000).substring(3,7);
               user_status_s = "";
               if (result.getString("hh_user_coupon.user_status").indexOf("U") != -1) user_status_s = user_status_s + result.getString("hh_user_coupon.user_id") + ",";
               if (result.getString("hh_user_coupon.user_status").indexOf("M") != -1) user_status_s = user_status_s + "携帯";
               if (result.getString("hh_user_coupon.user_status").indexOf("C") != -1) user_status_s = user_status_s + "PC";
               if (result.getString("hh_user_coupon.user_status").indexOf("A") != -1) user_status_s = user_status_s + "ｱﾌﾟﾘ";
               if (result.getString("hh_user_coupon.user_status").indexOf("S") != -1) user_status_s = user_status_s + "ｽﾏﾎ";
%>
		<tr class="tableLW" >
			<td align="center" class="tableLW" id="line1<%=result.getInt("hh_user_coupon.coupon_no")%>" <% if (result.getInt("hh_user_coupon.used_flag") == 1 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
				<%= input_date_s.substring(1,5) %>/<%= input_date_s.substring(5,7) %>/<%= input_date_s.substring(7,9) %>
			</td>
			<td align="center" class="tableLW" id="line2<%=result.getInt("hh_user_coupon.coupon_no")%>" <% if (result.getInt("hh_user_coupon.used_flag") == 1 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
				<%= input_time_s.substring(1,3) %>:<%= input_time_s.substring(3,5) %>:<%= input_time_s.substring(5,7) %>
			</td>
			<td align="center" class="tableLW" width="10" id="line3<%=result.getInt("hh_user_coupon.coupon_no")%>" <% if (result.getInt("hh_user_coupon.used_flag") == 1 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
				<%= result.getInt("edit_coupon.id") %>
			</td>
			<td align="center" class="tableLW" id="line4<%=result.getInt("hh_user_coupon.coupon_no")%>" <% if (result.getInt("hh_user_coupon.used_flag") == 1 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
				<%= result.getString("edit_coupon.coupon_name") %>&nbsp;<%= result.getString("edit_coupon.contents1") %>&nbsp;<%= result.getString("edit_coupon.contents2") %>
<% if(result.getInt("hh_user_coupon.start_date") > nowdate ) {%><font color=red><small><%= start_date_s.substring(5,7) %>/<%= start_date_s.substring(7,9) %>
から利用できます</small><%}%>
			</td>
			<td align="center" class="tableLW" id="line5<%=result.getInt("hh_user_coupon.coupon_no")%>" <% if (result.getInt("hh_user_coupon.used_flag") == 1 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
				<% if(result.getInt("hh_user_coupon.start_date") > 0) {%><%= start_date_s.substring(5,7) %><%= start_date_s.substring(7,9) %>-<%}%><%= coupon_no_s %>
			</td>
			<td align="center" class="tableLW" id="line6<%=result.getInt("hh_user_coupon.coupon_no")%>" <% if (result.getInt("hh_user_coupon.used_flag") == 1 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
				<%= user_status_s %>
			</td>
			<td align="center" class="tableLW" id="line7<%=result.getInt("hh_user_coupon.coupon_no")%>" <% if (result.getInt("hh_user_coupon.used_flag") == 1 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
<%
                if (result.getInt("hh_user_coupon.end_date") == 99999999)
                {
                    if(result.getString("edit_coupon.use_limit").compareTo("") == 0)
                    {
%>
				&nbsp;
<%
                    }else{
%>
				<%= result.getString("edit_coupon.use_limit") %>
<%
                    }
                }
                else
                {
%>
				<%= end_date_s.substring(1,5) %>/<%= end_date_s.substring(5,7) %>/<%= end_date_s.substring(7,9) %>
<%
                }
%>
			</td>
			<td align="center" class="tableRW" id="line8<%=result.getInt("hh_user_coupon.coupon_no")%>" <% if (result.getInt("hh_user_coupon.used_flag") == 1 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
				<input id="used<%=result.getInt("hh_user_coupon.coupon_no")%>" name="used<%=result.getInt("hh_user_coupon.coupon_no")%>" type="checkbox" <% if (result.getInt("hh_user_coupon.used_flag") == 1 ){%>checked<%}%>
				 onclick="if(this.checked)
				 			{
								document.getElementById('line1<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FF8888';
								document.getElementById('line2<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FF8888';
								document.getElementById('line3<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FF8888';
								document.getElementById('line4<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FF8888';
								document.getElementById('line5<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FF8888';
								document.getElementById('line6<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FF8888';
								document.getElementById('line7<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FF8888';
								document.getElementById('line8<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FF8888';
							}
							else
							{
								document.getElementById('line1<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FFFFFF';
								document.getElementById('line2<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FFFFFF';
								document.getElementById('line3<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FFFFFF';
								document.getElementById('line4<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FFFFFF';
								document.getElementById('line5<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FFFFFF';
								document.getElementById('line6<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FFFFFF';
								document.getElementById('line7<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FFFFFF';
								document.getElementById('line8<%=result.getInt("hh_user_coupon.coupon_no")%>').style.backgroundColor='#FFFFFF';
							}
							parent.updFrame.location.href='../../common/pc/coupon_used_check.jsp?coupon_no=<%=result.getInt("hh_user_coupon.coupon_no")%>&seq=<%=result.getInt("hh_user_coupon.seq")%>&used='+this.checked">
			</td>
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
%>
		</table>
		<table width="520" border="0" cellspacing="0" cellpadding="0" id="disp2" style="display:block">
			<tr>
				<td class="honbun size12" width="480">
<%
            if( pageno != 0 )
            {
%>
					<a href="coupon_report.jsp?page=<%=pageno-1%>&limit_flag=<%=limit_flag%>">←前</a>
<%
            }
%>
				</td>
				<td class = "honbun size12">
<%
            if( count > 40 )
            {
%>
					<a href="coupon_report.jsp?page=<%=pageno+1%>&limit_flag=<%=limit_flag%>">次→</a>
<%
            }
%>				</td>
			</tr>
		</table>
<%
   if(limit_flag.compareTo("true") == 0)
   {
%>
		<table width="520" border="0" cellspacing="0" cellpadding="0" id="disp3" style="display:block">
			<tr>
				<td class="size12" width="480" style="text-align:left">
					※利用期間が「有効期限まで」と設定されている場合はすべての履歴が表示されます。
				</td>
			</tr>
		</table>
<%
    }
%>
		</td>
	</tr>
	</div>
