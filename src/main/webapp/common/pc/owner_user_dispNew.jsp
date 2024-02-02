<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
	String loginHotelId =  (String)session.getAttribute("LoginHotelId");
    String  userid = ReplaceString.getParameter(request,"UserId");
    if( userid == null )
    {
        userid = "";
    }

    String[]  color1_t = new String[4];
    String[]  color2_t = new String[4];
    color1_t[0]        = "#EEEEFF";
    color1_t[1]        = "#FFFFFF";
    color1_t[2]        = "#FFFFFF";
    color1_t[3]        = "#FFFFFF";
    color2_t[0]        = "#FFEEEE";
    color2_t[1]        = "#FFFFFF";
    color2_t[2]        = "#FFFFFF";
    color2_t[3]        = "#FFFFFF";
    int  report_daily_pc     = 0;
    int  report_daily_mobile = 0;
    int  report_month_pc     = 0;
    int  report_month_mobile = 0;

    String     query = "";
    int count = 1;
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    Connection        connectionh  = null;
    PreparedStatement prestateh    = null;
    ResultSet         resulth      = null;
    connection        = DBConnection.getConnection();
//ハピホテマイル加盟店有無
    boolean  NewHappieExist = false;
    query = "SELECT * FROM owner_user,owner_user_hotel,hh_hotel_basic,hh_hotel_newhappie WHERE owner_user.hotelid=?";
    query = query + " AND owner_user.hotelid=owner_user_hotel.hotelid";
    query = query + " AND owner_user.userid=owner_user_hotel.userid";
    query = query + " AND owner_user_hotel.accept_hotelid=hh_hotel_basic.hotenavi_id";
    query = query + " AND hh_hotel_basic.id = hh_hotel_newhappie.id";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, loginHotelId);
    result      = prestate.executeQuery();
    if( result != null )
    {
        if( result.next() != false )
        {
            NewHappieExist        = true;
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

//第一アドミニストレータの状態
    int      admin_userid      = 0;
    int      admin_admin_flag  = 0;
    int      admin_report_flag = 0;
    String   admin_loginid     = "";
    String   admin_name        = "";
    query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid=?";
    query = query + " AND owner_user_security.hotelid = owner_user.hotelid";
    query = query + " AND owner_user_security.userid = owner_user.userid";
    query = query + " AND owner_user.imedia_user=0";
    query = query + " AND owner_user_security.admin_flag=1";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, loginHotelId);
    result      = prestate.executeQuery();
    if( result != null )
    {
        if( result.next() != false )
        {
            admin_userid          = result.getInt("owner_user.userid");
            admin_loginid         = result.getString("owner_user.loginid");
            admin_name            = result.getString("owner_user.name");
            admin_report_flag     = result.getInt("owner_user.report_flag");
            admin_admin_flag      = result.getInt("owner_user_security.admin_flag");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    // ユーザ一覧・セキュリティ情報取得
    query = "SELECT * FROM owner_user,owner_user_security WHERE owner_user.hotelid=?";
    query = query + " AND owner_user_security.hotelid = owner_user.hotelid";
    query = query + " AND owner_user_security.userid = owner_user.userid";
    query = query + " AND (owner_user.imedia_user = 0  OR owner_user.userid=?)";
    if (!userid.equals(""))
    {
    query = query + " AND owner_user.userid=?";
    }
    prestate    = connection.prepareStatement(query);
    prestate.setString(1, loginHotelId);
    prestate.setInt(2, ownerinfo.DbUserId);
    if (!userid.equals(""))
    {
        prestate.setInt(3, Integer.parseInt(userid));
    }
    result      = prestate.executeQuery();
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th width="40"  class="tableLN"></th>
    <th width="180" align="center" valign="middle" nowrap class="tableLN" colspan="2">ユーザー名</th>
 <% if (admin_report_flag == 1){%>
	<th align="center" nowrap class="tableLN" >メールアドレス</th>
	<th width="36"  align="center" nowrap class="tableLN" >日報</th>
	<th width="36"  align="center" nowrap class="tableLN" >月報</th>
<%}else{%>
	<th  align="center" class="tableLN" colspan=3>メールアドレス</th>
<%}%>
<%
    if (NewHappieExist)
    {
%>
    <th width="36"  class="tableLN">ｸﾁｺﾐ</th>
	<th width="36"  class="tableRN">予約</th>
<%
    }else{
%>
    <th width="36"  class="tableRN">ｸﾁｺﾐ</th>
<%
    }
%>
  </tr>
<%
    if( result != null )
    {
        while( result.next() != false )
        {

                report_daily_pc     = 0;
                report_daily_mobile = 0;
                report_month_pc     = 0;
                report_month_mobile = 0;

            if (loginHotelId.compareTo("") != 0)
            {
                query = "SELECT * FROM hotel,owner_user_hotel WHERE owner_user_hotel.hotelid= ? " ;
                query = query + " AND owner_user_hotel.accept_hotelid = hotel.hotel_id";
                query = query + " AND owner_user_hotel.userid = ?";
            }
            else
            {
                if  (result.getInt("owner_user.report_flag") == 1 && result.getInt("owner_user_hotel.report_daily_pc")     == 1) report_daily_pc     =1;
                if  (result.getInt("owner_user.report_flag") == 1 && result.getInt("owner_user_hotel.report_daily_mobile") == 1) report_daily_mobile =1;
                if  (result.getInt("owner_user.report_flag") == 1 && result.getInt("owner_user_hotel.report_month_pc")     == 1) report_month_pc     =1;
                if  (result.getInt("owner_user.report_flag") == 1 && result.getInt("owner_user_hotel.report_month_mobile") == 1) report_month_mobile =1;
            }
%>
  <tr onclick="document.getElementById('hoteldisp<%= result.getString("owner_user.userid") %>').style.display='block';">
	<td width="40px" rowspan="3" align="center" valign="middle"  class="tableLW" style="background-color:#FFFFFF">
<%
if (result.getString("owner_user.userid").compareTo(userid) == 0)
{
%>
	<%= count %><br>
		&nbsp;<br>
		(<%= result.getInt("userid") %>)<br>
<%
}
else
{
%>
		<%= count %>(<%= result.getInt("userid") %>)<br>
		<input name="Submit" type="submit" onClick="postUrl('user_form.jsp?UserId=<%= result.getInt("userid") %>');" value="変更" ><br>
	<% if (result.getInt("owner_user_security.admin_flag") != 1){%>
		<input name="Submit" type="submit" onClick="postUrl('user_delete.jsp?UserId=<%= result.getInt("userid") %>');" value="削除" ><br>
	<%}%>
<%
}
%>
	</td>
	<td  width="30"  valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">PC</td>
	<td  width="150" valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">
		&nbsp;<%= result.getString("owner_user.loginid") %>&nbsp;
	</td>
<% if (admin_report_flag == 1){%>
	<td  valign="middle" class="tableLW" style="background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>" >&nbsp;<%= result.getString("owner_user.mailaddr_pc") %><% if(result.getInt("owner_user.unknown_flag_pc")==1){%><font color="#FF0000">&nbsp;Unknown</font><%}%></td>
	<td  width="40" valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>" id="disp_report_daily_pc<%= result.getString("owner_user.userid") %>" ><% if (report_daily_pc == 1) {%>●<%}else{%>×<%}%></td>
	<td  width="40" valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>" id="disp_report_month_pc<%= result.getString("owner_user.userid") %>" ><% if (report_month_pc == 1) {%>●<%}else{%>×<%}%></td>
<%}else{%>
	<td colspan="3" valign="middle" class="tableLW" style="background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">&nbsp;<%= result.getString("owner_user.mailaddr_pc") %><% if(result.getInt("owner_user.unknown_flag_pc")==1){%><font color="#FF0000">&nbsp;Unknown</font><%}%></td>
<%}%>
<%
    if (NewHappieExist)
    {
%>
	<td width="40" valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>"><% if  (result.getInt("owner_user_security.sec_level17") == 1) {%>●<%}else{%>×<%}%></td>
	<td width="40" valign="middle" class="tableRW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>"><% if  (result.getInt("owner_user_security.sec_level21") == 1) {%>●<%}else{%>×<%}%></td>
<%
    }else{
%>
	<td width="40" valign="middle" class="tableRW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>"><% if  (result.getInt("owner_user_security.sec_level17") == 1) {%>●<%}else{%>×<%}%></td>
<%
    }
%>

  </tr>
  <tr onclick="document.getElementById('hoteldisp<%= result.getString("owner_user.userid") %>').style.display='block';">
	<td width="30"  valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">携帯</td>
	<td width="150" valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">&nbsp;
	<% if(result.getString("owner_user.machineid").length() > 17){%><%=result.getString("owner_user.machineid").substring(0,17)%>...<%}else{%><%= result.getString("owner_user.machineid") %><%}%>&nbsp;
	</td>
<% if (admin_report_flag == 1){%>
	<td             valign="middle" class="tableLW" style="background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">&nbsp;<%= result.getString("mailaddr_mobile") %><% if(result.getInt("owner_user.unknown_flag_mobile")==1){%><font color="#FF0000">&nbsp;Unknown</font><%}%></td>
	<td width="40"  valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>" id="disp_report_daily_mobile<%= result.getString("owner_user.userid") %>" ><% if (report_daily_mobile == 1) {%>●<%}else{%>×<%}%></td>
	<td width="40"  valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>" id="disp_report_month_mobile<%= result.getString("owner_user.userid") %>" ><% if (report_month_mobile == 1) {%>●<%}else{%>×<%}%></td>
<%}else{%>
	<td colspan="3  valign="middle" class="tableLW" style="background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>">&nbsp;<%= result.getString("mailaddr_mobile") %><% if(result.getInt("owner_user.unknown_flag_mobile")==1){%><font color="#FF0000">&nbsp;Unknown</font><%}%></td>
<%}%>
<%
    if (NewHappieExist)
    {
%>
	<td width="40" valign="middle" class="tableLW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>"><% if  (result.getInt("owner_user_security.sec_level18") == 1) {%>●<%}else{%>×<%}%></td>
	<td width="40" valign="middle" class="tableRW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>"><% if  (result.getInt("owner_user_security.sec_level22") == 1) {%>●<%}else{%>×<%}%></td>
<%
    }else{
%>
	<td width="40" valign="middle" class="tableRW" style="text-align:center;background-color:<%=color1_t[result.getInt("owner_user.imedia_user")]%>"><% if  (result.getInt("owner_user_security.sec_level18") == 1) {%>●<%}else{%>×<%}%></td>
<%
    }
%>
  </tr>
  <tr onclick="document.getElementById('hoteldisp<%= result.getString("owner_user.userid") %>').style.display='block';">
	<td  width="180" colspan="1" id="disp_owner_user" align="center" valign="middle"  class="tableLW" style="background-color:<%=color2_t[result.getInt("owner_user.imedia_user")]%>">
		<%=  new String(result.getString("owner_user.name").getBytes("Shift_JIS"), "Windows-31J") %><% if  (result.getInt("owner_user_security.admin_flag") == 1)  {%>【管理者】<%}%><br>
		<small>（<a href="#" onclick="postUrl('owner_user_loginhistory.jsp?UserId=<%= result.getInt("owner_user.userid") %>&Type=hotenavi')">ログイン履歴</a>
		<a href="#" onclick="postUrl('owner_user_loginhistory.jsp?UserId=<%= result.getInt("owner_user.userid") %>&Type=hapihote&EDIT')">修正履歴</a>
		)
		</small>
	</td>
	<td nowrap align="center" valign="middle" class="tableRW" colspan=<%if (NewHappieExist){%>"7"<%}else{%>"6"<%}%> style="background-color:<%=color2_t[result.getInt("owner_user.imedia_user")]%>" >
			<% if  (result.getInt("owner_user_security.sec_level01") == 1) {%>■<%}else{%>□<%}%>【売上管理】　　
			<% if  (result.getInt("owner_user_security.sec_level02") == 1) {%>■<%}else{%>□<%}%>【部屋情報】　　
			<% if  (result.getInt("owner_user_security.sec_level03") == 1) {%>■<%}else{%>□<%}%>【帳票管理】　　
			<% if  (result.getInt("owner_user_security.sec_level04") == 1) {%>■<%}else{%>□<%}%>【ＨＰレポート】
			<% if  (result.getInt("owner_user_security.sec_level05") == 1) {%>■<%}else{%>□<%}%>【メルマガ作成】<br>
			<% if  (result.getInt("owner_user_security.sec_level06") == 1) {%>■<%}else{%>□<%}%>【ＨＰ編集】　　
			<% if  (result.getInt("owner_user_security.sec_level07") == 1) {%>■<%}else{%>□<%}%>【設定メニュー】
			<% if  (result.getInt("owner_user_security.sec_level09") == 1) {%>■<%}else{%>□<%}%>【ダウンロード】
			<% if  (result.getInt("owner_user_security.sec_level15") == 1) {%>■<%}else{%>□<%}%>【ハピホテ編集】
			<% if  (result.getInt("owner_user_security.sec_level16") == 1) {%>■<%}else{%>□<%}%>【クチコミ権限】<br>
<%
    if (NewHappieExist)
    {
%>
			<% if  (result.getInt("owner_user_security.sec_level19") == 1) {%>■<%}else{%>□<%}%>【予約編集】
			<% if  (result.getInt("owner_user_security.sec_level19") == 2) {%>■<%}else{%>□<%}%>【予約フロント専用】
			<% if  (result.getInt("owner_user_security.sec_level20") == 1) {%>■<%}else{%>□<%}%>【請求明細表示】
			<% if  (result.getInt("owner_user_security.sec_level23") == 1) {%>■<%}else{%>□<%}%>【ﾊﾋﾟﾎﾃﾀｯﾁ未接続ﾒｰﾙ】<br>
<%
    }
%>
	</td>
  </tr>
  <tr onmouseover="document.getElementById('hoteldisp<%= result.getString("owner_user.userid") %>').style.display='block';" onmouseout="document.getElementById('hoteldisp<%= result.getString("owner_user.userid") %>').style.display='none';">
	<td colspan=<%if (NewHappieExist){%>"9"<%}else{%>"8"<%}%>>
		<table width="100%" cellpadding="0" cellspacing="1" border="0" id="hoteldisp<%= result.getString("owner_user.userid") %>" style="display:none;">
		  <tr>
			<td width="40"  colspan="1" align="center" valign="middle" class="tableLW" style="background-color:<%=color2_t[result.getInt("owner_user.imedia_user")]%>">
				店舗
			</td>
			<td  align="left" valign="middle"  class="tableRW" colspan="7" style="padding-left:20px;background-color:<%=color2_t[result.getInt("owner_user.imedia_user")]%>">
				<img style="float:right;clear:both" border=0 src="../../common/pc/image/close.gif" onclick="document.getElementById('hoteldisp<%= result.getString("owner_user.userid") %>').style.display='none';">
<%
            if (loginHotelId.compareTo("") != 0)
            {
                connectionh        = DBConnection.getConnection();
                prestateh   = connectionh.prepareStatement(query);
                prestateh.setString(1, loginHotelId);
                prestateh.setInt(2, result.getInt("owner_user.userid"));
                resulth  = prestateh.executeQuery();
                if( resulth != null )
                {
                    while( resulth.next() != false )
                    {
%>
						<%=resulth.getString("hotel.name")%>
						[&nbsp;<%=resulth.getString("hotel.hotel_id")%>
						<% if  (result.getInt("owner_user.report_flag") == 1){%>
						<% if  (resulth.getInt("owner_user_hotel.report_times") !=0 || resulth.getInt("owner_user_hotel.report_timee") !=0) {%>（メール：<%=resulth.getInt("owner_user_hotel.report_times")%>-<%=resulth.getInt("owner_user_hotel.report_timee")%>
							<% if  (resulth.getInt("owner_user_hotel.report_daily_pc") == 1 ) {report_daily_pc=1;%>,PC日報<%}%>
							<% if  (resulth.getInt("owner_user_hotel.report_month_pc") == 1 ) {report_month_pc=1;%>,PC月報<%}%>
							<% if  (resulth.getInt("owner_user_hotel.report_daily_mobile") == 1 ) {report_daily_mobile=1;%>,携帯日報<%}%>
							<% if  (resulth.getInt("owner_user_hotel.report_month_mobile") == 1 ) {report_month_mobile=1;%>,携帯月報<%}%>
						）<%}%>
						<%}%>]<br>
<%
                    }
                }
                DBConnection.releaseResources(resulth,prestateh,connectionh);
              }
%>
<% if (admin_report_flag == 1){%>
<script type="text/javascript">
<!--
                <%if  (result.getInt("owner_user.report_flag") == 1 && report_daily_pc     == 1){%>document.getElementById('disp_report_daily_pc<%= result.getString("owner_user.userid") %>').innerHTML="●";<%}%>
                <%if  (result.getInt("owner_user.report_flag") == 1 && report_daily_mobile == 1){%>document.getElementById('disp_report_daily_mobile<%= result.getString("owner_user.userid") %>').innerHTML="●";<%}%>
                <%if  (result.getInt("owner_user.report_flag") == 1 && report_month_pc     == 1){%>document.getElementById('disp_report_month_pc<%= result.getString("owner_user.userid") %>').innerHTML="●";<%}%>
                <%if  (result.getInt("owner_user.report_flag") == 1 && report_month_mobile == 1){%>document.getElementById('disp_report_month_mobile<%= result.getString("owner_user.userid") %>').innerHTML="●";<%}%>
-->
</script>
<%}%>
			</td>
		  </tr>
		</table>
	  </td>
	</tr>
<%
            count++;
        }
    }
    DBConnection.releaseResources( result,prestate,connection);
%>
</table>
<%@ include file="../csrf/csrf.jsp" %>