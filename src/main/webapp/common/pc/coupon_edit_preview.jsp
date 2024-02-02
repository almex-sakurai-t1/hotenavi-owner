<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.text.*" %>
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
    NumberFormat nf2      = new DecimalFormat("00");
    NumberFormat nf3      = new DecimalFormat("000");
    NumberFormat nf4      = new DecimalFormat("0000");
    //現在の日付・時刻を取得
    DateEdit  de = new DateEdit();
    String nowDate         = de.getDate(2);
    int    nowdate        = Integer.parseInt(nowDate);
    int    nowtime        = Integer.parseInt(de.getTime(1));

    String hotelid = ReplaceString.getParameter(request,"HotelId");
	if( hotelid != null && !CheckString.hotenaviIdCheck(hotelid))
	{
        response.sendError(400);
        return;
	}
    String id = ReplaceString.getParameter(request,"Id");
    int    hapihote_id    = 0;
    String address_all    = "";
    String tel1           = "";
	String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    //ハピホテのIDを調べる
    try
    {
        query = "SELECT * FROM hh_hotel_basic WHERE hotenavi_id=?";
        //データコネクション開始
        connection  = DBConnection.getConnection();
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
    	DBConnection.releaseResources(result, prestate, connection);
    }

    int type = 0;
    String hotelname = "";
    String couponname = "";
    String contents1 = "";
    String contents2 = "";
    String restrict1 = "";
    String restrict2 = "";
    String teleno = "";
    String uselimit = "";

    type = Integer.parseInt( ReplaceString.getParameter(request,"CouponType") );
    hotelname =  ReplaceString.getParameter(request,"col_hotel_name");
    couponname = ReplaceString.getParameter(request,"col_coupon_name");
    contents1 =  ReplaceString.getParameter(request,"col_contents1");
    contents2 =  ReplaceString.getParameter(request,"col_contents2");
    restrict1 =  ReplaceString.getParameter(request,"col_restrict1");
    restrict1= restrict1.replace("\n", "<br>");
    restrict2 =  ReplaceString.getParameter(request,"col_restrict2");
	restrict2= restrict2.replace("\n", "<br>");
    teleno =     ReplaceString.getParameter(request,"col_teleno");
    uselimit =   ReplaceString.getParameter(request,"col_use_limit");

    String[] gifimage = new String[32];
    int[] contentstype = new int[32];

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
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>クーポンプレビュー</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/coupon.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/coupon.js"></script>
<script type="text/javascript">
	var asw = window.screen.width;
	var ash = window.screen.height;
	var width = 580;
	var heigth = 957;
	var leftDistance = 0;
	var w = 0;
	var userAgent = window.navigator.userAgent.toLowerCase();
	if (userAgent.indexOf('firefox') == -1) {
		var windowObj = window.opener;
		if (windowObj == undefined) {
			windowObj = window;
		}
		leftDistance = windowObj.screenLeft;
		w = (Math.floor(leftDistance / asw) * (leftDistance - (leftDistance - asw)));
	}
	w += (asw - width) / 2;
	var h = (ash - heigth) / 2;
	window.resizeTo(width, heigth);
	window.moveTo(w, h);
</script>
</head>

<body background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr valign="top">
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="10" height="20"><img src="../../common/pc/image/spacer.gif" width="10" height="20"></td>
          <td height="20"><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
          <td width="20" height="20"><img src="../../common/pc/image/spacer.gif" width="20" height="20"></td>
        </tr>

        <tr valign="top">
          <td width="10"><img src="../../common/pc/image/spacer.gif" width="10" height="40"></td>
          <td>
            <table width="100%" height="18" border="0" cellpadding="0" cellspacing="0" bgcolor="#666666">
              <tr valign="middle">
                <td width="7" align="left" bgcolor="#FF9933"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
                <td width="3"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
                <td width="3" bgcolor="#FF9933"><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
                <td><div class="white12">クーポンプレビュー</div></td>
              </tr>
            </table>

            <table width="100%" height="8" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td><img src="../../common/pc/image/spacer.gif" width="500" height="8"></td>
              </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="10"><img src="../../common/pc/image/spacer.gif" width="10" height="20"></td>

                <td valign="top">
<%  if (type == 99) {  //新フォーム%>
	<table width="500" border="0" cellspacing="0" cellpadding="0" style="border:1px #1E72AA solid; background:#ffffff;" align=center>
	<tr>
		<td>
			<div style="position:relative; with:489; height:59;"><img src="../../common/pc/image/h_coupon.gif" width="489" height="59">
			<div style="position:absolute; top:30px; left:25px; z-index:1; font-size:20px; font-weight:bold; width: 330px; height: 25px;"><%= hotelname %></div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="font-size:14px; margin:10px 10px 5px 35px;"><%= couponname %></div>
			<div style="color:#FF0000; font-size:20px; font-weight:bold; margin:10px 10px 10px 35px;"><%= contents1 %></div>
			<div style="color:#FF0000; font-size:20px; font-weight:bold; margin:10px 10px 10px 35px;"><%= contents2 %></div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="margin:10px; ">
			<table width="450" border="0" align="center" cellpadding="7" cellspacing="0" bgcolor="#e8e8e8" style="font-size:12px; border:1px solid #ccc;">
			<tr>
				<td>
					<table>
						<tr>
							<td rowspan="2" style="width: 50px; vertical-align: top;">
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
					<strong>
					<%= uselimit %>
					</strong>
				</td>
			</tr>
			<tr>
				<td align="right" style="border-top:1px #ccc dashed; ">
					発行日付：<%= nowdate / 10000 %>/<%= nf2.format( nowdate / 100 % 100 ) %>/<%= nf2.format( nowdate % 100 ) %>
				</td>
			</tr>
			<tr>
				<td align="right">
					クーポンNo：<b>XXX-XXXX</b><br>
				</td>
			</tr>
			</table>
			</div>
		</td>
	</tr>
	</table>
<%
}
else
{
%>
                  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td><img src="../../common/pc/image/spacer.gif" width="500" height="16"></td>
                    </tr>
                  </table>
                  <table border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="328">
                        <table width="328" height="198" border="0" cellpadding="6" cellspacing="0"  background="<%= gifimage[type] %>">
                          <tr>
                            <td align="left" valign="top">
                              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                  <td valign="top"><img src="../../common/pc/image/spacer.gif" width="100" height="12"></td>
                                </tr>
                                <tr>
                                  <td class="name"><%= hotelname %></td>
                                </tr>
                              </table>
                              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td align="left"><img src="../../common/pc/image/spacer.gif" width="100" height="6"></td>
                                </tr>
                                <tr>
                                  <td valign="top" class="coupon"><%= couponname %></td>
                                </tr>
                                <tr>
                                  <td valign="top"><img src="../../common/pc/image/spacer.gif" width="100" height="4"></td>
                                </tr>
<%
    if( contentstype[type] == 1 )
    {
%>
                                <tr>
                                  <td align="right" valign="middle" class="waribiki"><font color="#CC0000"><%= contents1 %></font></td>
                                  <td><img src="../../common/pc/image/spacer.gif" width="10" height="4" align="middle"><img src="../../common/pc/image/txt_red_off.gif" width="79" height="47" align="middle"></td>
                                </tr>
<%
    }
    else
    {
%>
                                <tr>
                                  <td class="service"><font color="#CC0000"><%= contents1 %></font></td>
                                </tr>
                                <tr>
                                  <td class="service"><font color="#CC0000"><%= contents2 %></font></td>
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
                                  <td width="66">条　件：</td>
                                  <td nowrap><%= restrict1 %></td>
                                </tr>
                                <tr>
                                  <td width="66"></td>
                                  <td nowrap><%= restrict2 %></td>
                                </tr>
                                <tr>
                                  <td width="66">TEL:</td>
                                  <td><%= teleno %></td>
                                </tr>
                                <tr>
                                  <td width="66">有効期限：</td>
                                  <td><%= uselimit %></td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                        </table>
                      </td>
                      <td><img src="../../common/pc/image/spacer.gif" width="12" height="16"></td>
                    </tr>

                    <tr>
                      <td><img src="../../common/pc/image/spacer.gif" width="328" height="16"></td>
                    </tr>
                  </table>

<%
}
%>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td valign="top">&nbsp;</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td valign="top">
                  <input name="submit_ret" type="button" value="戻る" onClick="history.back()">
<!--
                  <form action="coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=<%= type %>&Id=<%= id %>" method="POST">
                  <input name="submit_ret" type="submit" value="戻る" >
-->
                  </form>
                </td>
              </tr>
            </table>
          </td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td width="10">&nbsp;</td>
          <td><img src="../../common/pc/image/spacer.gif" width="500" height="50"></td>
          <td>&nbsp;</td>
        </tr>
      </table>
    </td>
    <td width="2"><img src="../../common/pc/image/spacer.gif" width="2" height="100"></td>
  </tr>

  <tr valign="bottom">
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="center" valign="middle"><div class="copyright"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18"><img src="../../common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyright&copy; almex
                inc . All Rights Reserved.<!-- #EndLibraryItem --></div></td>
        </tr>
        <tr>
          <td><img src="../../common/pc/image/spacer.gif" width="500" height="20"></td>
        </tr>
      </table>
    </td>
    <td width="2"><img src="../../common/pc/image/spacer.gif" width="2" height="30"></td>
  </tr>

</table>
</body>

</html>
