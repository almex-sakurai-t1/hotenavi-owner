<%@ page contentType="text/html; charset=Windows-31J" %>
				<a name="a_<%=t_hotelid[i]%>">
				<tr>
					<td >
						<!-- ############### サブタイトルバー ############### -->
						<div  id="disp_<%=t_hotelid[i]%>" style="display:block" class="disp">
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<form name="form_csv_<%=t_hotelid[i]%>" id="form_csv_<%=t_hotelid[i]%>" action="<%=Contents[type].replace(".jsp","_csv.jsp")%>" method="post">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="10" height="18"></td>
								<td width="100">
									<div class="size12 subtitle_text"><strong>
									<%=year%>年
<%
    if (month != 0)
    {
%>
									<%=month%>月
<%
    }
    if (day  != 0)
    {
%>
									<%=day%>日
<%
    }
%>
									</strong></div>
								</td>
								<td>
									<div class="subtitle_text"><big><strong><%= rs.replaceKanaFull(t_name[i]) %></strong></big></div>
								</td>
								<td width="120" class="size10 subtitle_text">
									<input type=button value="PRINT" onclick="funcPrint('<%=t_hotelid[i]%>');">
									<input type=button value="CSV出力" onclick="document.getElementById('form_csv_<%=t_hotelid[i]%>').submit();">
									<input type="hidden" name="hotelid"  value="<%= t_hotelid[i] %>">
									<input type="hidden" name="Year"  value="<%= Year %>">
									<input type="hidden" name="Month" value="<%= Month %>">
									<input type="hidden" name="Day"   value="<%= Day %>">
									<input type="hidden" name="Type"  value="<%= Type %>">
									<input type="hidden" name="Member"  value="<% if(type ==4){%>mem_<%}%>">
								</td>
							</tr></form>
						</table>
						</div>
						<div  id="print_<%=t_hotelid[i]%>" style="display:none" class="print">
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="10" height="18"></td>
								<td width="100">
									<div class="size12  subtitle_text"><strong>
									<%=year%>年
<%
    if (month != 0)
    {
%>
									<%=month%>月
<%
    }
    if (day  != 0)
    {
%>
									<%=day%>日
<%
    }
%>
									</strong></div>
								</td>
								<td>
									<div class="subtitle_text"><strong><big><%= rs.replaceKanaFull(t_name[i]) %></strong></big></div>
								</td>
								<td width="80" class="size10 subtitle_text">
									<%= de.getDate(1) %>&nbsp;<%=de.getTime(0)%>
								</td>
							</tr>
						</table>
						</div>
						<!-- ############### サブタイトルバー ここまで ############### -->
					</td>
				</tr>
				<tr>
					<td ><img src="../../common/pc/image/spacer.gif" width="500" height="10"></td>
				</tr>
