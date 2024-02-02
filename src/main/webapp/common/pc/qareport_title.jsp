<%@ page contentType="text/html; charset=Windows-31J" %>
				<tr>
					<td align="left" valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;">
						<strong>【アンケートレポート】<%= hname %> </strong>
<%
    if (type != 0)
    {
        if (member_flag == 1)
        {
%>
					(メンバー用）
<%
        }
%>
					[<%=StartYear%>年<%=StartMonth%>月<%=StartDay%>日～<%=EndYear%>年<%=EndMonth%>月<%=EndDay%>日］
<%
    }
%>
					</td>
				</tr>

				<tr>
					<td ><img src="../../common/pc/image/spacer.gif" width="500" height="10"></td>
				</tr>
<%
    if (type != 0)
    {
%>
				<tr>
					<td >
						<!-- ############### サブタイトルバー ############### -->
						<div  id="disp" style="display:block">
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<form name="form_csv" id="form_csv" action="qareport_<%=Contents[type]%>_csv.jsp" method="post">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="10" height="18"></td>
								<td>
									<div class="size12 subtitle_text"><big><strong><%= msg %></strong></big></div>
								</td>
								<td width="120" class="size10 subtitle_text">
									<input type=button value="PRINT" onclick="document.getElementById('disp').style.display='none';document.getElementById('print').style.display='block';print();document.getElementById('print').style.display='none';document.getElementById('disp').style.display='block';">
									<input type=button value="CSV出力" onclick="document.form_csv.submit();">
									<input type="hidden" name="Id"         value="<%= id %>">
									<input type="hidden" name="Qid"        value="<%= q_id %>">
									<input type="hidden" name="StartYear"  value="<%= StartYear %>">
									<input type="hidden" name="StartMonth" value="<%= StartMonth %>">
									<input type="hidden" name="StartDay"   value="<%= StartDay %>">
									<input type="hidden" name="EndYear"    value="<%= EndYear %>">
									<input type="hidden" name="EndMonth"   value="<%= EndMonth %>">
									<input type="hidden" name="EndDay"     value="<%= EndDay %>">
									<input type="hidden" name="Type"       value="<%= type %>">
								</td>
							</tr></form>
						</table>
						</div>
						<div  id="print" style="display:none">
						<table cellpadding="0" cellspacing="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3"><img src="../../common/pc/image/spacer.gif" width="10" height="18"></td>
								<td>
									<div class="size12 subtitle_text"><big><strong><%= msg %></strong></big></div>
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
<%
    }
%>
				<tr>
					<td ><img src="../../common/pc/image/spacer.gif" width="500" height="10"></td>
				</tr>
