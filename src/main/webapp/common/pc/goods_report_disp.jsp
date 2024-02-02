<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%

    NumberFormat nf2      = new DecimalFormat("00");
    StringFormat sf;
    sf = new StringFormat();

    String loginHotelId =  (String)session.getAttribute("LoginHotelId");
    String hotelid = (String)session.getAttribute("SelectHotel");
    if (hotelid.compareTo("all")==0)
    {
       hotelid = loginHotelId;
    }
    String limit_flag = ReplaceString.getParameter(request,"limit_flag");
    if(limit_flag   == null) limit_flag = "true";
    if(limit_flag.compareTo("") == 0) limit_flag ="true";
    String custom_id = ReplaceString.getParameter(request,"custom_id");

    DateEdit dateedit = new DateEdit();

    int nowdate   =  Integer.parseInt(dateedit.getDate(2));
    int nowtime   =  Integer.parseInt(dateedit.getTime(1));
    String nowdate_s = nf2.format(nowdate / 10000 ) + "/" + nf2.format(nowdate / 100 % 100 ) + "/" + nf2.format(nowdate % 100 );
    String nowtime_s = nf2.format(nowtime / 10000 ) + "/" + nf2.format(nowtime / 100 % 100 ) + "/" + nf2.format(nowtime % 100 );

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
    int    count        = 0;
    int    total_count  = 0;

    String            query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connection_sub  = null;
    PreparedStatement prestate_sub    = null;
    ResultSet         result_sub      = null;
    connection     = DBConnection.getConnection();
    connection_sub = DBConnection.getConnection();

    try
    {
        query = "SELECT COUNT(*),SUM(goods_entry.use_total),goods_entry.* FROM goods_entry,goods WHERE goods_entry.hotelid=?";
        query = query + " AND goods_entry.category_id=goods.category_id";
        query = query + " AND goods_entry.seq=goods.seq";
        query = query + " AND goods_entry.input_date  >= goods.disp_from";
        query = query + " AND goods_entry.input_date  <= goods.disp_to";
        if (custom_id != null)
        {
            query = query + " AND goods_entry.custom_id=?";
        }
        query = query + " AND goods_entry.count!=0";
        query = query + " AND goods_entry.status>0";
        if (limit_flag.compareTo("true") == 0)
        {
            query = query + " AND goods_entry.status<3";
        }
        query = query + " GROUP BY goods_entry.entry_id";
        query = query + " ORDER BY goods_entry.entry_id DESC";
        query = query + " LIMIT " + pageno * 40 + "," + 41;
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        if (custom_id != null)
        {
            prestate.setString(2,custom_id);
        }
        result      = prestate.executeQuery();
%>
	<div align="center">
	<tr>
		<td align="center">
		<table width="98%" border="0" cellspacing="1" cellpadding="0" >
			<tr>
				<td width="50"  class="tableLN" align="center">番号</td>
				<td width="60"  class="tableLN" align="center">会員No</td>
				<td width="" class="tableLN" align="center">お名前・ニックネーム</td>
				<td width="125" class="tableLN" align="center">入力日時</td>
				<td width="90"  class="tableLN" align="center">申込時残POINT</td>
				<td width="60"  class="tableLN" align="center">申込件数</td>
				<td width="70"  class="tableLN" align="center">使用POINT</td>
				<td width="110" class="tableLN" align="center">受け渡し・発送先</td>
				<td width="100" class="tableRN" align="center">状況</td>
			</tr>
<%
        int  input_date = 0;
        int  input_time = 0;
        if( result != null )
        {
            while( result.next() != false )
            {
                count++;
                if (count > 40) break;
                total_count  = pageno * 40 + count;
                input_date = result.getInt("input_date");
                input_time = result.getInt("input_time");
%>
			<tr class="tableLW" >
				<td align="center" class="tableLW" id="line1<%=result.getInt("goods_entry.entry_id")%>" <% if (result.getInt("goods_entry.status") > 2 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
					<a href="goods_report_detail.jsp?<% if(custom_id != null){%>custom_id=<%=custom_id%>&<%}%>EntryId=<%= result.getInt("goods_entry.entry_id")%>&page=<%=pageno%>&limit_flag=<%=limit_flag%>">
						<strong><%= result.getInt("goods_entry.entry_id")%></strong>
					</a>
				</td>
				<td align="center" class="tableLW" id="line2<%=result.getInt("goods_entry.entry_id")%>" <% if (result.getInt("goods_entry.status") > 2 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
					<a href="goods_report.jsp?custom_id=<%= result.getString("goods_entry.custom_id")%>&limit_flag=<%=limit_flag%>">
						<%= result.getString("goods_entry.custom_id") %>
					</a>
				</td>
				<td align="left" class="tableLW" style="padding-left:5px" id="line3<%=result.getInt("goods_entry.entry_id")%>" <% if (result.getInt("goods_entry.status") > 2 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
					<% if(result.getString("goods_entry.name").compareTo("")!= 0){%>
						<%= result.getString("goods_entry.name") %>
						<%if(result.getString("goods_entry.nick_name").compareTo("")!= 0){%>
						／<%= result.getString("goods_entry.nick_name") %>
						<%}%>
					<%}else{%>
						<%= result.getString("goods_entry.nick_name") %>
					<%}%>
				</td>
				<td align="center" class="tableLW" id="line4<%=result.getInt("goods_entry.entry_id")%>" <% if (result.getInt("goods_entry.status") > 2 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
					<%=nf2.format(input_date / 10000 )%>/<%=nf2.format(input_date / 100 % 100 )%>/<%=nf2.format(input_date % 100 )%>
					<%=nf2.format(input_time / 10000 )%>:<%=nf2.format(input_time / 100 % 100 )%>:<%=nf2.format(input_time % 100 )%>
				</td>
				<td align="right" class="tableLW" style="padding-right:5px" id="line5<%=result.getInt("goods_entry.entry_id")%>" <% if (result.getInt("goods_entry.status") > 2 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
					<%= sf.rightFitFormat( Kanma.get(result.getInt("point")), 10 )%>
				</td>
				<td align="right" class="tableLW" style="padding-right:5px" id="line6<%=result.getInt("goods_entry.entry_id")%>" <% if (result.getInt("goods_entry.status") > 2 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
					<%= result.getInt(1)%>
				</td>
				<td align="right" class="tableLW" style="padding-right:5px" id="line7<%=result.getInt("goods_entry.entry_id")%>" <% if (result.getInt("goods_entry.status") > 2 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
					<%= sf.rightFitFormat( Kanma.get(result.getInt(2)), 10 ) %>
				</td>
				<td align="center" class="tableLW" style="padding-left:5px" style="padding-left:5px" id="line8<%=result.getInt("goods_entry.entry_id")%>" <% if (result.getInt("goods_entry.status") > 2 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
					<% if (result.getInt("method") == 1){%>
						<% if(result.getString("hotelid").compareTo(result.getString("topic_hotelid")) == 0){%> 
						フロント受け渡し
						<%}else{
                                   query = "SELECT name FROM hotel WHERE hotel_id ='" + result.getString("topic_hotelid") + "'";
                                   prestate_sub = connection_sub.prepareStatement(query);
                                   result_sub   = prestate_sub.executeQuery(query);
                                   if( result_sub.next() != false )
                                   {%>
						<%=result_sub.getString("name")%>
                                   <%}
                                   DBConnection.releaseResources(result_sub);
                                   DBConnection.releaseResources(prestate_sub);
						  }%>
					<%}else if (result.getInt("method") == 2){%>指定住所へ発送<%}%>
				</td>
				<td align="center" class="tableRW" id="line9<%=result.getInt("goods_entry.entry_id")%>" <% if (result.getInt("goods_entry.status") > 2 ){%>style="background-color:#FF8888"<%}else{%>style="background-color:#FFFFFF"<%}%>>
					<a href="goods_report_detail.jsp?<% if(custom_id != null){%>custom_id=<%=custom_id%>&<%}%>EntryId=<%= result.getInt("goods_entry.entry_id")%>&page=<%=pageno%>&limit_flag=<%=limit_flag%>">
					<% if (result.getInt("status") == 1){%><font color=red><strong>未確認</strong></font><%}else if(result.getInt("status") == 2){%>確認済<%}else if(result.getInt("status") == 3){if (result.getInt("method") == 1){%>準備完了<%}else if (result.getInt("method") == 2){%>発送済<%}}else if (result.getInt("status") == 9){%>取消済<%}%>
					</a>
				</td>
			</tr>
<%
           }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(connection_sub);
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
		</table>
		<table width="520" border="0" cellspacing="0" cellpadding="0" id="disp2" style="display:block">
			<tr>
				<td class="honbun" width="480">
<%
            if( pageno != 0 )
            {
%>
					<a href="goods_report.jsp?<% if(custom_id != null){%>custom_id=<%=custom_id%>&<%}%>page=<%=pageno-1%>&limit_flag=<%=limit_flag%>">←前</a>
<%
            }
%>
				</td>
				<td class = "honbun">
<%
            if( count > 40 )
            {
%>
					<a href="goods_report.jsp?<% if(custom_id != null){%>custom_id=<%=custom_id%>&<%}%>page=<%=pageno+1%>&limit_flag=<%=limit_flag%>">次→</a>
<%
            }
%>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	</div>
