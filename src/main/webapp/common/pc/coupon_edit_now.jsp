<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />

<%
    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String showExpiredCoupon = ReplaceString.getParameter(request,"showExpiredCoupon");
    if( hotelid.compareTo("all") == 0 )
    {
        hotelid = loginHotelId;
    }

    NumberFormat nf2      = new DecimalFormat("00");
    NumberFormat nf3      = new DecimalFormat("000");
    NumberFormat nf4      = new DecimalFormat("0000");
    //現在の日付・時刻を取得
    DateEdit  de = new DateEdit();
    String nowDate         = de.getDate(2);
    int    nowdate        = Integer.parseInt(nowDate);
    int    nowtime        = Integer.parseInt(de.getTime(1));

    int    hapihote_id    = 0;
    String address_all    = "";
    String tel1           = "";
	String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    //データコネクション開始
    connection  = DBConnection.getConnection();
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
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    java.sql.Date start;
    java.sql.Date end;
    int cnt = 0;
    int id;
    int start_yy;
    int start_mm;
    int start_dd;
    int start_ymd;
    int end_yy;
    int end_mm;
    int end_dd;
    int end_ymd;
    int coupon_type;

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

	try
	{
       	query = "SELECT * FROM edit_coupon,hh_master_coupon,hh_hotel_basic WHERE hh_hotel_basic.id=?";
        query = query + " AND hh_hotel_basic.hotenavi_id = edit_coupon.hotelid";
        query = query + " AND hh_hotel_basic.id = hh_master_coupon.id";
        query = query + " AND edit_coupon.id = hh_master_coupon.coupon_no";
        query = query + " AND hh_master_coupon.service_flag=2";
	    if ("0".equals(showExpiredCoupon))
	    {
	    	query = query + " AND (edit_coupon.end_date >= date_format(now(), '%Y-%m-%d'))";
	    }
        query = query + " ORDER BY edit_coupon.disp_idx";

        prestate    = connection.prepareStatement(query);
        prestate.setInt(1, hapihote_id);
        result      = prestate.executeQuery();

        while( result.next() )
        {
            id = result.getInt("edit_coupon.id");

            // 時間取得
            start = result.getDate("edit_coupon.start_date");
            start_yy = start.getYear();
            start_mm = start.getMonth();
            start_dd = start.getDate();
            start_ymd = (start_yy+1900) * 10000 + (start_mm+1) * 100 + start_dd;

            end = result.getDate("edit_coupon.end_date");
            end_yy = end.getYear();
            end_mm = end.getMonth();
            end_dd = end.getDate();
            end_ymd = (end_yy+1900) * 10000 + (end_mm+1) * 100 + end_dd;


            coupon_type = result.getInt("edit_coupon.coupon_type");
            if( coupon_type > 30 && coupon_type != 99)
            {
                coupon_type = 31;
            }
            cnt++;

%>

<%
            if( cnt % 2 == 1 )
            {
%>
                  <tr>
                    <td><img src="../../common/pc/image/spacer.gif" width="50" height="4"></td>
                    <td width="24"></td>
                  </tr>
                  <tr>
<%
            }
%>

                    <td valign="top" class="size12">
                      <table border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td>
                            表示順番： <%= result.getInt("edit_coupon.disp_idx") %>

<%
                if( result.getInt("edit_coupon.member_only") == 1 )
                {
%>
                            　　　　　　　　　　　メンバー専用

<%
                }
%>
                            <br>
                          </td>
                        </tr>
                        <tr>
                          <td><img src="../../common/pc/image/spacer.gif" width="200" height="8"></td>
                        </tr>
<%
	String restrict1 = result.getString("edit_coupon.restrict1");
	restrict1= restrict1.replace("\n", "<br>");
	String restrict2 = result.getString("edit_coupon.restrict2");
	restrict2= restrict2.replace("\n", "<br>");
%>
<%  if (coupon_type == 99) {  //新フォーム%>

	<table width="328" border="0" cellspacing="0" cellpadding="0" style="border:1px #1E72AA solid; background:#ffffff;" align=center>
	<tr>
		<td>
			<div style="position:relative; with:328; height:39;"><img src="../../common/pc/image/h_coupon.gif" width="328" height="39">
			<div style="position:absolute; top:30px; left:25px; z-index:1; font-size:20px; font-weight:bold; width: 320px; height: 25px;"><%= result.getString("edit_coupon.hotel_name") %></div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="font-size:14px; margin:10px 10px 1px 25px;"><%= result.getString("edit_coupon.coupon_name") %></div>
			<div style="color:#FF0000; font-size:20px; font-weight:bold; margin:1px 1px 1px 15px;"><%= result.getString("edit_coupon.contents1") %></div>
			<div style="color:#FF0000; font-size:20px; font-weight:bold; margin:1px 1px 1px 15px;"><%= result.getString("edit_coupon.contents2") %></div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="margin:5px; ">
			<table width="320" border="0" align="center" cellpadding="1" cellspacing="0" bgcolor="#e8e8e8" style="font-size:12px; border:1px solid #ccc;">
			<tr>
				<td>
					<table cellpadding="0" cellspacing="0" style="font-size: inherit;">
						<tr>
							<td rowspan="2" style="width: 40px; vertical-align: top;">
								条件：
							</td>
							<td style="padding-bottom: 3px; word-break: break-all;">
								<%= restrict1 %>
							</td>
						</tr>
						<% if (restrict2.compareTo("") != 0) {%>
						<tr>
							<td style="padding-top: 3px; word-break: break-all;">
								<%= restrict2 %>
							</td>
						</tr>
						<%}%>
					</table>
				</td>
			</tr>
			<tr>
				<td style="border-top:1px #ccc dashed; ">住所：<%= address_all %></td>
			</tr>
			<tr>
				<td style="border-top:1px #ccc dashed; ">TEL：<%= tel1 %></td>
			</tr>
			<tr>
				<td style="border-top:1px #ccc dashed; ">有効期限：
					<strong><%= result.getString("edit_coupon.use_limit") %></strong>
				</td>
			</tr>
			<tr>
				<td align="right" style="border-top:1px #ccc dashed; ">
					発行日付：<%= nowdate / 10000 %>/<%= nf2.format( nowdate / 100 % 100 ) %>/<%= nf2.format( nowdate % 100 ) %>
				</td>
			</tr>
			<tr>
				<td align="right">
					クーポンNo：<b><%= nf3.format( result.getInt("edit_coupon.all_seq") % 1000 ) %>-XXXX</b><br>
				</td>
			</tr>
			</table>

			</div>
		</td>
	</tr>
    <tr>
         <td class="size12" nowrap style="text-align:center">
              <%if (start_ymd>nowdate || end_ymd<nowdate){%><font color=red><strong>非表示</strong></font>　<%}%>
              表示期間：<%= start_yy+1900 %>年<%= start_mm+1 %>月<%= start_dd %>日～<%= end_yy+1900 %>年<%= end_mm+1 %>月<%= end_dd %>日
          </td>
   </tr>
<%}else{%>
                        <tr>
                          <td width="328">
                            <table width="328" height="198" border="0" cellpadding="6" cellspacing="0" background="<%= gifimage[coupon_type] %>">
                              <tr>
                                <td align="left" valign="top">
                                  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                      <td valign="top"><img src="../../common/pc/image/spacer.gif" width="100" height="12"></td>
                                    </tr>
                                    <tr>
                                      <td class="name"><%= result.getString("edit_coupon.hotel_name") %></td>
                                    </tr>
                                  </table>
                                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                      <td align="left"><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
                                    </tr>
                                    <tr>
                                      <td valign="top" class="coupon"><%= result.getString("edit_coupon.coupon_name") %></td>
                                    </tr>
                                    <tr>
                                      <td valign="top"><img src="../../common/pc/image/spacer.gif" width="100" height="4"></td>
                                    </tr>
<%
                    if( contentstype[coupon_type] == 1 )
                    {
%>
                                    <tr>
                                      <td align="right" valign="middle" class="waribiki"><font color="#CC0000"><%= result.getString("edit_coupon.contents1") %></font></td>
                                      <td><img src="../../common/pc/image/spacer.gif" width="10" height="4" align="middle"><img src="../../common/pc/image/txt_red_off.gif" width="79" height="47" align="middle"></td>
                                    </tr>
<%
                    }
                    else
                    {
%>
                                    <tr>
                                      <td class="service"><font color="#CC0000"><%= result.getString("edit_coupon.contents1") %></font></td>
                                    </tr>
                                    <tr>
                                      <td class="service"><font color="#CC0000"><%= result.getString("edit_coupon.contents2") %></font></td>
                                    </tr>
<%
                    }
%>
                                  </table>
                                  <table width="100%" border="0" cellspacing="0" cellpadding="1" class="syosai">
                                    <tr valign="top">
                                      <td colspan="2"><img src="../../common/pc/image/spacer.gif" width="100" height="4"></td>
                                    </tr>
                                    <tr>
                                      <td width="66" rowspan="2" style="vertical-align: top;">条　件：</td>
                                      <td style="padding-bottom: 3px; word-break: break-all;"><%= restrict1 %></td>
                                    </tr>
                                    <tr>
                                      <td style="padding-top: 3px; word-break: break-all;"><%= restrict2 %></td>
                                    </tr>
                                    <tr>
                                      <td width="66">TEL:</td>
                                      <td><%= result.getString("edit_coupon.teleno") %></td>
                                    </tr>
                                    <tr>
                                      <td width="66">有効期限：</td>
                                      <td><%= result.getString("edit_coupon.use_limit") %></td>
                                    </tr>
                                  </table>
                                </td>
                              </tr>
                            </table>
                          </td>
                        </tr>
                        <tr valign="top">
                          <td><img src="../../common/pc/image/spacer.gif" width="20" height="6"></td>
                        </tr>

                        <tr>
                          <td class="size12" nowrap>

                          表示期間：<%= start_yy+1900 %>年<%= start_mm+1 %>月<%= start_dd %>日～<%= end_yy+1900 %>年<%= end_mm+1 %>月<%= end_dd %>日


                          </td>
                        </tr>
                        <tr>
                          <td class="size12" nowrap>&nbsp;</td>
                        </tr>
<%}%>
                        <tr valign="middle">
                          <td align="center" nowrap class="size12">
                            <input name="submit00" type="button" id="submit00" onClick="openCouponEditWindow('<%= hotelid %>', <%= result.getInt("edit_coupon.coupon_type") %>, <%= id %>)" value="編集">
                          </td>
                        </tr>
                      </table>
                    </td>
<%
            if( cnt % 2 == 0 )
            {
%>
                  </tr>
<%
            }
        }
	}
	catch( Exception e )
	{
		Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
	}
	finally
	{
	    DBConnection.releaseResources(result, prestate, connection);
	}
%>
