<%@ page contentType="text/html; charset=Windows-31J" %>
<%
    int send_mail_flag = 0;
//���̃z�e���ň�l�ł��z�M�`�F�b�N���Ă���l������΁A��
    query = "SELECT * FROM owner_user_hotel WHERE accept_hotelid= ?";
    query = query + " AND ( report_daily_pc = 1 OR report_daily_mobile = 1 OR report_month_pc = 1 OR report_month_mobile = 1)";
    dbc   = DBConnection.getConnection();
    stc   = dbc.prepareStatement(query);
    stc.setString(1, list_accept_hotelid);
    retc  = stc.executeQuery();
    if( retc.next() != false )
    {
        send_mail_flag = 1;
    }
    DBConnection.releaseResources(retc,stc,dbc);
%>

							<tr>
								<td valign="middle" width="20" class="tableWhite" align="center" width="80">
<% if (input_admin_flag == 1 && admin_userid != userid){%>
									<input name="hotel<%= count %>"   type="checkbox" id="hotel<%= count %>"   value="<%= list_accept_hotelid %>" <%if (checked_accept_hotelid){%> checked <%}%>  <% if (admin_report_flag == 1){%>onclick="return Change_mail<%=count%>();"<%}%>>
<%}else{%>
									<%if (checked_accept_hotelid){%><input name="hotel<%= count %>"   type="hidden" id="hotel<%= count %>"   value="<%= list_accept_hotelid %>>"><%}%> <%if (checked_accept_hotelid){%>��<%}else{%>-<%}%>
<%}%>
									<input name="hotelid<%= count %>" type="hidden"   id="hotelid<%= count %>" value="<%= list_accept_hotelid %>">
								</td>
								<td class="tableWhite" ><%= list_name %></td>
								<td class="tableWhite" width="20" style="text-align:center"><% if (list_admin){%>��<%}else{%>�~<%}%></td>
<% if (admin_report_flag == 1){%>
								<td class="tableWhite" width="400">
	<% if (send_mail_flag == 1){%>
		<% if (input_admin_flag == 1){%>
									����i<input name="report_daily_pc<%= count %>"     type="checkbox" id="report_daily_pc<%= count %>"     value="1" <% if( list_report_daily_pc == 1 )     {%> checked <%}%> onclick="return Check_mail<%=count%>();">PC<input name="report_daily_mobile<%= count %>" type="checkbox" id="report_daily_mobile<%= count %>" value="1" <% if( list_report_daily_mobile == 1 ) {%> checked <%}%> onclick="return Check_mail<%=count%>();">�g��)
									����i<input name="report_month_pc<%= count %>"     type="checkbox" id="report_month_pc<%= count %>"     value="1" <% if( list_report_month_pc == 1 )     {%> checked <%}%> onclick="return Check_mail<%=count%>();">PC<input name="report_month_mobile<%= count %>" type="checkbox" id="report_month_mobile<%= count %>" value="1" <% if( list_report_month_mobile == 1 ) {%> checked <%}%> onclick="return Check_mail<%=count%>();">�g��)
									����<input name="report_times<%= count %>" type="text" class="tableWhite" id="report_times<%= count %>" value="<%=list_report_times%>" size="4" maxlength="4" style="ime-mode: disabled;text-align:right" autocomplete="off" onchange="return Check_mail<%=count%>();">�`<input name="report_timee<%= count %>" type="text" class="tableWhite" id="report_timee<%= count %>" value="<%=list_report_timee%>" size="4" maxlength="4" style="ime-mode: disabled;text-align:right" autocomplete="off" onchange="return Check_mail<%=count%>();">
		<%}else{%>
			<% if (list_report_daily_pc == 1 || list_report_daily_mobile == 1 || list_report_month_pc == 1 || list_report_month_mobile == 1){%>
									����i<input name="report_daily_pc<%= count %>"     type="checkbox" id="report_daily_pc<%= count %>"     value="1" <% if( list_report_daily_pc == 1 )     {%> checked <%}%> onclick="return Check_mail<%=count%>();">PC<input name="report_daily_mobile<%= count %>" type="checkbox" id="report_daily_mobile<%= count %>" value="1" <% if( list_report_daily_mobile == 1 ) {%> checked <%}%> onclick="return Check_mail<%=count%>();">�g��)
									����i<input name="report_month_pc<%= count %>"     type="checkbox" id="report_month_pc<%= count %>"     value="1" <% if( list_report_month_pc == 1 )     {%> checked <%}%> onclick="return Check_mail<%=count%>();">PC<input name="report_month_mobile<%= count %>" type="checkbox" id="report_month_mobile<%= count %>" value="1" <% if( list_report_month_mobile == 1 ) {%> checked <%}%> onclick="return Check_mail<%=count%>();">�g��)
									����<input name="report_times<%= count %>" type="text" class="tableWhite" id="report_times<%= count %>" value="<%=list_report_times%>" size="4" maxlength="4" style="ime-mode: disabled;text-align:right" autocomplete="off" onchange="return Check_mail<%=count%>();">�`<input name="report_timee<%= count %>" type="text" class="tableWhite" id="report_timee<%= count %>" value="<%=list_report_timee%>" size="4" maxlength="4" style="ime-mode: disabled;text-align:right" autocomplete="off" onchange="return Check_mail<%=count%>();">
			<%}else{%>
									����i<input name="report_daily_pc<%= count %>"     type="hidden" id="report_daily_pc<%= count %>"     value="<%=list_report_daily_pc%>">�ݒ�Ȃ�<input name="report_daily_mobile<%= count %>" type="hidden" id="report_daily_mobile<%= count %>" value="<%=list_report_daily_mobile%>">)
									����i<input name="report_month_pc<%= count %>"     type="hidden" id="report_month_pc<%= count %>"     value="<%=list_report_month_pc%>">�ݒ�Ȃ�<input name="report_month_mobile<%= count %>" type="hidden" id="report_month_mobile<%= count %>" value="<%=list_report_daily_mobile%>">)
									<input name="report_times<%= count %>" type="hidden" class="tableWhite" id="report_times<%= count %>" value="<%=list_report_times%>" size="4" maxlength="4" style="ime-mode: disabled;text-align:right" autocomplete="off" ><input name="report_timee<%= count %>" type="hidden" class="tableWhite" id="report_timee<%= count %>" value="<%=list_report_timee%>" size="4" maxlength="4" style="ime-mode: disabled;text-align:right" autocomplete="off" >
			<%}%>
		<%}%>
	<% }else{%>
									<input name="report_daily_pc<%= count %>"     type="hidden" id="report_daily_pc<%= count %>"     value="<%=list_report_daily_pc%>"><input name="report_daily_mobile<%= count %>" type="hidden" id="report_daily_mobile<%= count %>" value="<%=list_report_daily_mobile%>">
									<input name="report_month_pc<%= count %>"     type="hidden" id="report_month_pc<%= count %>"     value="<%=list_report_month_pc%>"><input name="report_month_mobile<%= count %>" type="hidden" id="report_month_mobile<%= count %>" value="<%=list_report_daily_mobile%>">
									<input name="report_times<%= count %>" type="hidden" class="tableWhite" id="report_times<%= count %>" value="<%=list_report_times%>" size="4" maxlength="4" style="ime-mode: disabled;text-align:right" autocomplete="off" ><input name="report_timee<%= count %>" type="hidden" class="tableWhite" id="report_timee<%= count %>" value="<%=list_report_timee%>" size="4" maxlength="4" style="ime-mode: disabled;text-align:right" autocomplete="off" >
									���チ�[���̐ݒ�ΏۊO�X�܂ł�
	<% } %>
								</td>
								<td class="tableWhite" width="20" style="text-align:center"><% if( send_mail_flag == 1){%>��<%}else{%>�~<%}%>
								</td>

<script type="text/javascript">
<!--
function Change_mail<%= count %>()
{
   if (!document.userform.hotel<%= count %>.checked)
   {
       document.userform.report_daily_pc<%= count %>.checked = false;
       document.userform.report_daily_mobile<%= count %>.checked = false;
       document.userform.report_month_pc<%= count %>.checked = false;
       document.userform.report_month_mobile<%= count %>.checked = false;
       document.userform.report_times<%= count %>.value = 0;
       document.userform.report_timee<%= count %>.value = 0;
   }
   else if(( document.userform.report_daily_pc<%= count %>.checked == true      ||
             document.userform.report_daily_mobile<%= count %>.checked == true  ||
             document.userform.report_month_pc<%= count %>.checked == true      ||
             document.userform.report_month_mobile<%= count %>.checked == true  )&&
           ( document.userform.report_timee<%= count %>.value == 0))
        {
             document.userform.report_timee<%= count %>.value = 2400;
        }
SalesMailCheck();
}
function Check_mail<%= count %>()
{
   if ((document.userform.report_daily_pc<%= count %>.checked)||
       (document.userform.report_daily_mobile<%= count %>.checked)||
       (document.userform.report_month_pc<%= count %>.checked)||
       (document.userform.report_month_mobile<%= count %>.checked)||
       (document.userform.report_times<%= count %>.value > 0)||
       (document.userform.report_timee<%= count %>.value > 0))
   {
        document.userform.hotel<%= count %>.checked = true;
   }
   if(( document.userform.report_daily_pc<%= count %>.checked == true      ||
        document.userform.report_daily_mobile<%= count %>.checked == true  ||
        document.userform.report_month_pc<%= count %>.checked == true      ||
        document.userform.report_month_mobile<%= count %>.checked == true  )&&
      ( document.userform.report_timee<%= count %>.value == 0))
   {
        document.userform.report_timee<%= count %>.value = 2400;
   }
SalesMailCheck();
}

-->
</script>
<%}%>
							</tr>

