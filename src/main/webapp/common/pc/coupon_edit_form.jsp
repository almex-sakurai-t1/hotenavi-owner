<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
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
%>

<%
    String            loginHotelId = (String)session.getAttribute("LoginHotelId");
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    String    query   = "";
    int   imedia_user = 0;
    connection  = DBConnection.getConnection();
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query += " AND userid = ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() )
        {
           imedia_user = result.getInt("imedia_user");
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
    	DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(result);
    }
    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    String param_coupon_type = ReplaceString.getParameter(request,"CouponType");
    if( param_coupon_type == null )
    {
        param_coupon_type = "0";
    }
    int coupon_type = Integer.parseInt(param_coupon_type);
    if (coupon_type > 30 && coupon_type != 99)
    {
        coupon_type = 31;
    }
    String id = ReplaceString.getParameter(request,"Id");

    DateEdit  de = new DateEdit();

    int       disp_idx;
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();
    String    hotel_name;
    String    coupon_name;
    String    contents1;
    String    contents2;
    String    restrict1;
    String    restrict2;
    String    teleno;
    String    use_limit;
    int       member_only;
    int    available           = 0;
    int    use_count           = 0;
    int    use_start_day       = 0;
    int    use_end_day         = 0;
    int    disp_mobile         = 1;
    String disp_mobile_message = "ｸｰﾎﾟﾝをご利用の際には発行された[ｸｰﾎﾟﾝ画面]をﾌﾛﾝﾄにて提示し、ｸｰﾎﾟﾝ番号をお伝えください｡";
    int    hapihote_id    = 0;
    String address_all    = "";
    String tel1           = "";

    //ハピホテのIDを調べる
    try
    {
        query = "SELECT * FROM hh_hotel_basic WHERE hotenavi_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        result      = prestate.executeQuery();
        if( result.next() )
        {
           hapihote_id = result.getInt("id");
           address_all = result.getString("address_all");
           tel1        = result.getString("tel1");
        }
    }
    catch( Exception e )
    {
    	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
    }
    finally
    {
        DBConnection.releaseResources(prestate);
        DBConnection.releaseResources(result);
    }

    disp_idx = 0;
    hotel_name = "";
    coupon_name = "";
    contents1 = "";
    contents2 = "";
    restrict1 = "";
    restrict2 = "";
    teleno = "";
    use_limit = "";
    member_only = 0;

    int[] contentstype = new int[32];
    String[] gifimage = new String[32];

    gifimage[0]  = "../../common/pc/image/cpn_n_bg.gif";
    gifimage[1]  = "../../common/pc/image/cpn_n_bg.gif";
    gifimage[2]  = "../../common/pc/image/cpn_n_bg.gif";
    gifimage[3]  = "../../common/pc/image/cpn_n_bg.gif";
    gifimage[4]  = "../../common/pc/image/cpn_n_bg.gif";
    gifimage[5]  = "../../common/pc/image/cpn_n_bg.gif";
    gifimage[6]  = "../../common/pc/image/cpn_n_bg.gif";
    gifimage[7]  = "../../common/pc/image/cpn_mb_bg.gif";
    gifimage[8]  = "../../common/pc/image/cpn_mb_bg.gif";
    gifimage[9]  = "../../common/pc/image/cpn_mb_bg.gif";
    gifimage[10] = "../../common/pc/image/cpn_mb_bg.gif";
    gifimage[11] = "../../common/pc/image/cpn_mb_bg.gif";
    gifimage[12] = "../../common/pc/image/cpn_mb_bg.gif";
    gifimage[13] = "../../common/pc/image/cpn_ny_bg.gif";
    gifimage[14] = "../../common/pc/image/cpn_ny_bg.gif";
    gifimage[15] = "../../common/pc/image/cpn_vd_bg.gif";
    gifimage[16] = "../../common/pc/image/cpn_vd_bg.gif";
    gifimage[17] = "../../common/pc/image/cpn_wd_bg.gif";
    gifimage[18] = "../../common/pc/image/cpn_wd_bg.gif";
    gifimage[19] = "../../common/pc/image/cpn_hw_bg.gif";
    gifimage[20] = "../../common/pc/image/cpn_hw_bg.gif";
    gifimage[21] = "../../common/pc/image/cpn_xms_bg.gif";
    gifimage[22] = "../../common/pc/image/cpn_xms_bg.gif";
    gifimage[23] = "../../common/pc/image/cpn_sp_bg.gif";
    gifimage[24] = "../../common/pc/image/cpn_sp_bg.gif";
    gifimage[25] = "../../common/pc/image/cpn_sm_bg.gif";
    gifimage[26] = "../../common/pc/image/cpn_sm_bg.gif";
    gifimage[27] = "../../common/pc/image/cpn_au_bg.gif";
    gifimage[28] = "../../common/pc/image/cpn_au_bg.gif";
    gifimage[29] = "../../common/pc/image/cpn_wn_bg.gif";
    gifimage[30] = "../../common/pc/image/cpn_wn_bg.gif";
    gifimage[31] = "../../common/pc/image/cpn_n_bg.gif";

    contentstype[0]  = 1;
    contentstype[1]  = 2;
    contentstype[2]  = 1;
    contentstype[3]  = 2;
    contentstype[4]  = 1;
    contentstype[5]  = 2;
    contentstype[6]  = 1;
    contentstype[7]  = 2;
    contentstype[8]  = 1;
    contentstype[9]  = 2;
    contentstype[10] = 1;
    contentstype[11] = 2;
    contentstype[12] = 1;
    contentstype[13] = 2;
    contentstype[14] = 1;
    contentstype[15] = 2;
    contentstype[16] = 1;
    contentstype[17] = 2;
    contentstype[18] = 1;
    contentstype[19] = 2;
    contentstype[20] = 1;
    contentstype[21] = 2;
    contentstype[22] = 1;
    contentstype[23] = 2;
    contentstype[24] = 1;
    contentstype[25] = 2;
    contentstype[26] = 1;
    contentstype[27] = 2;
    contentstype[28] = 1;
    contentstype[29] = 2;
    contentstype[30] = 1;
    contentstype[31] = 2;

%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=Windows-31J">
<meta http-equiv="Content-Style-Type" content="text/css">
<title>クーポン編集</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/coupon.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/edit_coupon.js"></script>
<script type="text/javascript">
var asw = window.screen.width;
var ash = window.screen.height;
var width = 440;
var heigth = 957;
var leftDistance = 0;
var w = 0;
window.moveTo(0, 0);
var userAgent = window.navigator.userAgent.toLowerCase();
if (userAgent.indexOf('firefox') == -1) {
	var windowObj = window.opener;
	if (windowObj == undefined) {
		windowObj = window;
	}
	leftDistance = windowObj.screen.availLeft;
	w = (Math.floor(leftDistance / asw) * (leftDistance - (leftDistance - asw)));
}
w += (asw - width) / 2;
var h = (ash - heigth) / 2;
window.resizeTo(width, heigth);
window.moveTo(w, h);
</script>
<style type="text/css">
html {
	overflow: auto;
}
.fullWidth {
	width: 100%;
}
</style>
</head>

<body background="../../common/pc/image/bg.gif">
<div align=center>
	<form name="form1" method="post">
		<table style="width: 395px;">
                  <tr>
                    <td>
                     <table width="100%"  border="0" cellpadding="0" cellspacing="0" >
                        <tr>
                          <td align="left" valign="top" bgcolor="#22333F" class="tab" colspan=2><font color="#FFFFFF">

<%

    if( id.compareTo("0") == 0 )
    {
        String paramdata = ReplaceString.getParameter(request,"MemberOnly");
        if( paramdata == null )
        {
            member_only = 0;
        }
        else
        {
            member_only = Integer.parseInt( paramdata );
        }

%>
新規<br>
<%
    }
    else
    {
        try
        {
            query = "SELECT * FROM edit_coupon,hh_master_coupon,hh_hotel_basic WHERE edit_coupon.hotelid=?";
            query = query + " AND edit_coupon.id = ?";
            query = query + " AND hh_hotel_basic.hotenavi_id = edit_coupon.hotelid";
            query = query + " AND hh_hotel_basic.id = hh_master_coupon.id";
            query = query + " AND edit_coupon.id = hh_master_coupon.coupon_no";
            query = query + " AND hh_master_coupon.service_flag=2";

//          query = "SELECT * FROM edit_coupon WHERE hotelid='" + hotelid + "' AND id=" + id;
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, Integer.parseInt(id));
            result      = prestate.executeQuery();
            if( result.next() )
            {
	            disp_idx = result.getInt("edit_coupon.disp_idx");
	            start = result.getDate("edit_coupon.start_date");
	            end = result.getDate("edit_coupon.end_date");
	            hotel_name = result.getString("edit_coupon.hotel_name");
	            coupon_name = result.getString("edit_coupon.coupon_name");
	            contents1 = result.getString("edit_coupon.contents1");
	            contents2 = result.getString("edit_coupon.contents2");
	            restrict1 = result.getString("edit_coupon.restrict1");
	            restrict1 = restrict1.replace("<br>", "\r\n");
	            restrict2 = result.getString("edit_coupon.restrict2");
	            restrict2 = restrict2.replace("<br>", "\r\n");
	            teleno = result.getString("edit_coupon.teleno");
	            use_limit = result.getString("edit_coupon.use_limit");
	            member_only = result.getInt("edit_coupon.member_only");
	            disp_mobile = result.getInt("edit_coupon.disp_mobile");
	            disp_mobile_message = result.getString("edit_coupon.disp_mobile_message");
	            use_count           = result.getInt("hh_master_coupon.use_count");
	            use_start_day       = result.getInt("hh_master_coupon.start_day");
	            use_end_day         = result.getInt("hh_master_coupon.end_day");
            }
        }
        catch( Exception e )
        {
        	Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
        }
        finally
        {
            DBConnection.releaseResources(prestate);
            DBConnection.releaseResources(result);
        }

        DBConnection.releaseResources(connection);
%>
更新<br>
<%
    }


    int start_year  = start.getYear()+1900;
    int start_month = start.getMonth()+1;
    int start_day   = start.getDate();
    int start_date  = start_year * 10000 + start_month * 100 + start_day;

    int end_year    = end.getYear()+1900;
    int end_month   = end.getMonth()+1;
    int end_day     = end.getDate();
    int end_date    = end_year * 10000 + end_month * 100 + end_day;
    if( id.compareTo("0") == 0 )
    {
//      start_date    = de.addDay(start_date,1);
        end_date      = de.addDay(end_date,1);
    }
    start_year    = start_date / 10000;
    start_month   =(start_date / 100) % 100;
    start_day     = start_date % 100;
    end_year      = end_date / 10000;
    end_month     =(end_date / 100) % 100;
    end_day       = end_date % 100;

    if( id.compareTo("0") == 0 )
    {
        if (end_month == 12)
        {
            end_year  = end_year + 1;
            end_month = 1;
        }
        else
        {
            end_month = end_month + 1;
        }
        end_day   = 1;
        end_date  = end_year * 10000 + end_month * 100 + end_day;
        end_date  = de.addDay(end_date,-1);
        end_year  = end_date / 10000;
        end_month =(end_date / 100) % 100;
        end_day   = end_date % 100;
    }

%>
                          </font></td>
                       </tr>
                        <tr valign="top">
                          <td colspan=2><img src="../../common/pc/image/spacer.gif" width="20" height="10"></td>
                        </tr>
                        <tr>
                          <td align="left" valign="top" class="size12" colspan=2>
<div>
	表示順番　　 ： <input name="col_disp_idx" type="text" size="8" maxlength="4" value="<%= disp_idx %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right">
</div>
<div>
	表示開始日　： <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start_year %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right">年
			<input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start_month %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right">月
			<input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start_day %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right">日
</div>
<div>
	表示終了日　： <input name="col_end_yy" type="text" size="4" maxlength="4" value="<%= end_year %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right">年
			<input name="col_end_mm" type="text" size="2" maxlength="2" value="<%= end_month %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right">月
			<input name="col_end_dd" type="text" size="2" maxlength="2" value="<%= end_day %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right">日
</div>
<div>
	<label>
		<input name="col_member_only" type="checkbox" size="8" value=1 <%if ( member_only == 1 ) { %>checked<% } %>>メンバー専用
	</label>
</div>
	<br>
                          </td>
                       </tr>
                       <tr>
                          <td>
                            <table width="100%" height="145" border="0" cellpadding="0" cellspacing="0">
                              <tr>
                                <td align="left" valign="top">
                                  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td align="left" valign="top" bgcolor="#22333F" class="tab" colspan=2><font color="#FFFFFF">クーポン表示制御</td></tr>
                                    </tr>
									<tr>
										<td colspan="1" class="tableLN"  align="left" >携帯条件</td>
										<td colspan="1" class="tableWhite"  align="left" >
											<input type="hidden" id="disp_mobile" name="disp_mobile" value="1">
                      <textarea name="disp_mobile_message" class="fullWidth" rows=4><%= disp_mobile_message %></textarea>
										</td>
									</tr>
									<tr>
										<td colspan="1" class="tableLN"  align="left" >利用回数</td>
										<td colspan="1" class="tableWhite"  align="left" >
											<select name="use_count" class="fullWidth">
												<option value="0" <% if (use_count == 0) {%> selected <%}%>>制限なし
												<option value="1" <% if (use_count == 1) {%> selected <%}%>>1回
												<option value="2" <% if (use_count == 2) {%> selected <%}%>>2回
												<option value="3" <% if (use_count == 3) {%> selected <%}%>>3回
												<option value="4" <% if (use_count == 4) {%> selected <%}%>>4回
												<option value="5" <% if (use_count == 5) {%> selected <%}%>>5回
												<option value="6" <% if (use_count == 6) {%> selected <%}%>>6回
												<option value="7" <% if (use_count == 7) {%> selected <%}%>>7回
												<option value="8" <% if (use_count == 8) {%> selected <%}%>>8回
												<option value="9" <% if (use_count == 9) {%> selected <%}%>>9回
												<option value="10" <% if (use_count == 10) {%> selected <%}%>>10回
											</select>
										</td>
									</tr>
									<tr>
										<td colspan="1" class="tableLN"  align="left" >利用可能日</td>
										<td colspan="1" class="tableWhite"  align="left" >
											<select name="use_start_day" class="fullWidth" style="margin-bottom: 1px;">
												<option value="0" <% if (use_start_day == 0) {%> selected <%}%>>発行当日から
												<option value="1" <% if (use_start_day == 1) {%> selected <%}%>>発行翌日から
												<option value="2" <% if (use_start_day == 2) {%> selected <%}%>>発行翌々日から
											</select>
											<select name="use_end_day" class="fullWidth">
												<option value="0"  <% if (use_end_day ==  0) {%> selected <%}%>>有効期限まで
												<option value="1"  <% if (use_end_day ==  1) {%> selected <%}%>>1日後まで
												<option value="2"  <% if (use_end_day ==  2) {%> selected <%}%>>2日後まで
												<option value="3"  <% if (use_end_day ==  3) {%> selected <%}%>>3日後まで
												<option value="4"  <% if (use_end_day ==  4) {%> selected <%}%>>4日後まで
												<option value="5"  <% if (use_end_day ==  5) {%> selected <%}%>>5日後まで
												<option value="6"  <% if (use_end_day ==  6) {%> selected <%}%>>6日後まで
												<option value="7"  <% if (use_end_day ==  7) {%> selected <%}%>>1週間後まで
												<option value="10" <% if (use_end_day == 10) {%> selected <%}%>>10日後まで
												<option value="14" <% if (use_end_day == 14) {%> selected <%}%>>2週間後まで
												<option value="20" <% if (use_end_day == 20) {%> selected <%}%>>20日後まで
												<option value="21" <% if (use_end_day == 21) {%> selected <%}%>>3週間後まで
												<option value="30" <% if (use_end_day == 30) {%> selected <%}%>>30日後まで
												<option value="60" <% if (use_end_day == 60) {%> selected <%}%>>60日後まで
												<option value="90" <% if (use_end_day == 90) {%> selected <%}%>>90日後まで
											</select>
										</td>
									</tr>
                                  </table>
                                </td>
                              </tr>
                            </table>
                          </td>
                       </tr>
                       <tr>
<%
if (coupon_type == 99)
{
%>
                          <td align="left" valign="top">

	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="border:1px #1E72AA solid; background:#ffffff;" align=center>
	<tr>
		<td>
			<div style="font-size:14px; margin:1px 1px 1px 15px;">
				<input type="text" name="col_hotel_name" class="fullWidth" maxlength="32" value="<%= new ReplaceString().HTMLEscape(hotel_name) %>" placeholder="ホテル名">
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="font-size:14px; margin:1px 1px 1px 15px;">
				<input name="col_coupon_name" type="text" class="fullWidth" maxlength="32" value="<%= new ReplaceString().HTMLEscape(coupon_name) %>" placeholder="クーポン名">
			</div>
			<div style="color:#FF0000; font-size:20px; font-weight:bold; margin:1px 1px 1px 35px;">
				<input name="col_contents1" type="text" class="fullWidth" maxlength="32" value="<%= new ReplaceString().HTMLEscape(contents1) %>" placeholder="内容1"><br>
				<input name="col_contents2" type="text" class="fullWidth" maxlength="32" value="<%= new ReplaceString().HTMLEscape(contents2) %>" placeholder="内容2">
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="margin:5px; ">
			<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" bgcolor="#e8e8e8" style="font-size:12px; border:1px solid #ccc;">
			<tr>
				<td width="66" rowspan="2" style="vertical-align: top;">
					条件：
				</td>
				<td>
					<textarea name="col_restrict1" type="text" rows="6" class="fullWidth" maxlength="200"><%= restrict1 %></textarea>
				</td>
			</tr>
			<tr>
				<td>
					<textarea name="col_restrict2" type="text" rows="6" class="fullWidth" maxlength="200"><%= restrict2 %></textarea>
				</td>
			</tr>
			<tr>
				<td style="border-top:1px #ccc dashed; ">住所：</td>
				<td style="border-top:1px #ccc dashed; "><%= address_all %></td>
			</tr>
			<tr>
				<td style="border-top:1px #ccc dashed; ">TEL：</td>
				<td style="border-top:1px #ccc dashed; ">
					<%= tel1 %>
					<input name="col_teleno" type="hidden" value="<%= tel1 %>" >
				</td>
			</tr>
			<tr>
				<td style="border-top:1px #ccc dashed; ">有効期限：</td>
				<td style="border-top:1px #ccc dashed; ">
					<input name="col_use_limit" type="text" class="fullWidth" maxlength="32" value="<%= new ReplaceString().HTMLEscape(use_limit) %>" >
				</td>
			</tr>
			</table>
			</div>
		</td>
	</tr>
	</table>
                          </td>
<%
}
else
{
%>
                          <td align="left" valign="top">
                            <table width="100%" height="198" border="0" cellpadding="6" cellspacing="0" background="<%= gifimage[coupon_type] %>">
                              <tr>
                                <td align="left" valign="top">
                                  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                      <td valign="top"><img src="../../common/pc/image/spacer.gif" width="100" height="5"></td>
                                    </tr>
                                    <tr>
                                      <td class="name"><input type="text" name="col_hotel_name" size="32" maxlength="15" value="<%= new ReplaceString().HTMLEscape(hotel_name) %>"></td>
                                    </tr>
                                  </table>
                                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                      <td align="left"><img src="../../common/pc/image/spacer.gif" width="100" height="1"></td>
                                    </tr>
                                    <tr>
                                      <td valign="top" class="coupon"><input name="col_coupon_name" type="text" size="32" maxlength="32" value="<%= new ReplaceString().HTMLEscape(coupon_name) %>"></td>
                                    </tr>
                                    <tr>
                                      <td valign="top"><img src="../../common/pc/image/spacer.gif" width="100" height="1"></td>
                                    </tr>
<%
                    if( contentstype[coupon_type] == 1 )
                    {
%>
                                    <tr>
                                      <td align="right" valign="middle" class="waribiki">
                                        <font color="#CC0000">
                                          <input name="col_contents1" type="text" size="32" maxlength="32" value="<%= new ReplaceString().HTMLEscape(contents1) %>" >
                                          <input name="col_contents2" type="hidden" value="<%= new ReplaceString().HTMLEscape(contents2) %>" >
                                        </font>
                                      </td>
                                      <td><img src="../../common/pc/image/spacer.gif" width="10" height="4" align="middle"><img src="../../common/pc/image/txt_red_off.gif" width="79" height="47" align="middle"></td>
                                    </tr>
<%
                    }
                    else
                    {
%>
                                    <tr>
                                      <td class="service"><font color="#CC0000"><input name="col_contents1" type="text" size="32" maxlength="32" value="<%= new ReplaceString().HTMLEscape(contents1) %>" ></font></td>
                                    </tr>
                                    <tr>
                                      <td class="service"><font color="#CC0000"><input name="col_contents2" type="text" size="32" maxlength="32" value="<%= new ReplaceString().HTMLEscape(contents2) %>" ></font></td>
                                    </tr>
<%
                    }
%>
                                  </table>
                                  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="syosai">
                                    <tr>
                                      <td width="66" rowspan="2" style="vertical-align: top;">条　件：</td>
                                      <td nowrap><textarea name="col_restrict1" type="text" size="32" maxlength="200"><%= restrict1 %></textarea></td>
                                    </tr>
                                    <tr>
                                      <td nowrap><textarea name="col_restrict2" type="text" size="32" maxlength="200"><%= restrict2 %></textarea></td>
                                    </tr>
                                    <tr>
                                      <td width="66">TEL:</td>
                                      <td><input name="col_teleno" type="text" class="fullWidth" maxlength="16" value="<%= teleno %>" ></td>
                                    </tr>
                                    <tr>
                                      <td width="66">有効期限：</td>
                                      <td><input name="col_use_limit" type="text" class="fullWidth" maxlength="32" value="<%= new ReplaceString().HTMLEscape(use_limit) %>" ></td>
                                    </tr>
                                  </table>
                                </td>
                              </tr>
                            </table>
                          </td>

<%
}
%>
                        </tr>
                        <tr valign="top">
                          <td colspan=2><img src="../../common/pc/image/spacer.gif" width="20" height="10"></td>
                        </tr>
<%if( id.compareTo("0") != 0 ){%>
                        <tr>
                          <td  colspan=2>
                            <div align="left" class="size10" style="background-color:white; padding: 5px 0;">
<font color=red>　【注意】</font>「更新」の場合、過去に表示されたクーポンの内容も変更されます。<br>　編集する場合は「修正前のクーポンを残して追加」のご利用を推奨します。<br>
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <td valign="top" colspan=2><img src="../../common/pc/image/spacer.gif" width="100" height="10"></td>
                        </tr>
<%}%>
		<tr>
			<td align="center">
				<input name="regsubmit" type="button" value="<%if( id.compareTo("0") == 0 ){%>作成<%}else{%>更新<%}%>" onClick="if (validation_range()){MM_openInput('input', '<%= hotelid %>', <%= coupon_type %>, '<%= id %>' )}">
				<%if( id.compareTo("0") != 0 ){%><input name="regcopy"   type="button" value="修正前のクーポンを残して追加" onClick="if (validation_range()){MM_openInput('input', '<%= hotelid %>', <%= coupon_type %>, '0' )}"><%}%>
				<input name="presubmit" type="button" value="プレビュー" onClick="MM_openPreview('preview', '<%= hotelid %>', <%= coupon_type %>, '<%= id %>' )">
			</td>
		</tr>
	</table>
</form>
</div>
<div align="center">
<%if( id.compareTo("0") != 0 ){%>
<form action="coupon_edit_delete.jsp?HotelId=<%= hotelid %>&Id=<%= id %>" method="POST" style="display: inline-block;">
<input name="submit_del" type="submit" value="削除" onclick="return confirm('削除します。よろしいですか？')">
</form>
<%}%>
<input name="submit_ret" type="button" value="閉じる" onClick="window.close()">
</div>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle"><div class="copyright"><!-- #BeginLibraryItem "Library/footer.lbi" -->
    Copyright&copy; almex
	inc . All Rights Reserved.<!-- #EndLibraryItem --></div></td>
  </tr>
</table>

</body>
</html>
