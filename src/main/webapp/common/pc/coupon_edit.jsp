<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
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
    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    String loginHotelId = (String)session.getAttribute("LoginHotelId");

    String showExpiredCoupon = request.getParameter("showExpiredCoupon");
	if (showExpiredCoupon == null) {
		showExpiredCoupon = "0";
	}

    if( hotelid.compareTo("all") == 0 )
    {
        hotelid = loginHotelId;
    }

    DateEdit  de      = new DateEdit();
    int       nowdate = Integer.parseInt(de.getDate(2));
    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    //旧タイプのクーポンを使用しているかいなかを調べる
    boolean   OldCouponExist = false;
    try
    {
        query = "SELECT * FROM edit_coupon WHERE hotelid=?";
        query = query + " AND coupon_type<>99";
        query = query + " AND start_date <=?";
        query = query + " AND end_date >=?";
        connection  = DBConnection.getConnection();
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, hotelid);
        prestate.setInt(2, nowdate);
        prestate.setInt(3, nowdate);
        result      = prestate.executeQuery();
        if( result.next() )
        {
           OldCouponExist = true;
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

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>クーポン作成</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/coupon.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/main.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/coupon.js"></script>
<script type="text/javascript">
function openCouponEditWindow(hotelId, couponType, Id) {
	MM_openBrWindow(
			'coupon_edit_form.jsp?HotelId=' + hotelId + '&CouponType=' + couponType + '&Id=' + Id,
			'coupon',
			'menubar=no,scrollbars=yes,resizable=no');
}
</script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20">
            <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="100" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">クーポン作成</font></td>
                <td width="15" height="20"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
                <td height="20">
                  <div><img src="../../common/pc/image/spacer.gif" width="200" height="20"></div>
                </td>
              </tr>
            </table>
          </td>
          <td width="3">&nbsp;</td>
        </tr>
        <!-- ここから表 -->
        <tr>
          <td align="center" valign="top" bgcolor="#FFFFFF">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
                <td><img src="../../common/pc/image/spacer.gif" width="400" height="4"></td>
              </tr>
              <tr>
                <td valign="top" class="size12" colspan="2">
                <div class="size12" style="margin-left:10px">
<%
  if (OldCouponExist)
  {
%>
					<br><font color=red><strong>2008年8月13日よりクーポンがシリアル番号に対応したものになっています。</strong></font><br>
					※クーポンが表示されると「HPレポート」⇒「クーポンレポート」に反映されます。<br>
					※ホームページを確認していただき、シリアル番号が表示されていない場合は、<br>
					<font color=blue><strong>ホームページ側の調整が必要</strong></font>ですので、お手数ですがアルメックスまで連絡願います。<br>
					&nbsp;<input name="submitnew" type="button" id="submitnew" style="width:300; height:30; border:5px outset; color:blue;font-size:15px;font-weight:bold;" onClick="openCouponEditWindow('<%= hotelid %>', 99, 0)" value="クーポン（PC版地図付）新規作成 [推奨]">
					<br/><br/><br/>
<%
   }
   else
   {
%>
					&nbsp;<input name="submitnew" type="button" id="submitnew" onClick="openCouponEditWindow('<%= hotelid %>', 99, 0)" value="新規作成">
<%
   }
%>
				<input id="showExpiredCoupon" type="checkbox" onclick="setCouponShow(this);"<% if ("1".equals(showExpiredCoupon)) { out.print(" checked"); } %>/><label for="showExpiredCoupon">期間切れのクーポンを表示</label>
              </div>
              </td>
            </tr>
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="4"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="4"></td>
            </tr>
            <tr valign="top">
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td>
                <div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="4">

                  <tr bgcolor="#666666">
                    <td colspan="2"><strong><font color="#FFFFFF">現在のクーポン</font></strong><img src="../../common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                    <%-- 現在登録されているクーポン表示 --%>
                    <jsp:include page="coupon_edit_now.jsp" flush="true" >
                    	<jsp:param name="showExpiredCoupon" value="<%=showExpiredCoupon%>" />
                    </jsp:include>
                    <%-- 現在登録されているクーポン表示ここまで --%>

                    <td valign="top" class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td valign="top" class="size12"><img src="../../common/pc/image/spacer.gif" width="200" height="4"></td>
                    <td valign="top" class="size12">&nbsp;</td>
                  </tr>
                </table>
                </div>
              </td>
            </tr>
          </table>

          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="4">
                  <tr bgcolor="#DDDDDD">
                    <td colspan="2"><strong>注意事項</strong><img src="../../common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                  <tr>
                    <td colspan="2" align=center>
                    <%-- 現在登録されている注意事項表示 --%>
                    <jsp:include page="coupon_edit_nowattention.jsp" flush="true" />
                      <input name="submit00" type="button" id="submit00" onClick="MM_openBrWindow('coupon_attention.jsp?HotelId=<%= hotelid %>','注意事項編集','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="注意事項編集">
                    <%-- 現在登録されている注意事項表示ここまで --%>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>

<%
  if (OldCouponExist)
  {
%>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="28"></td>
            </tr>
            <tr>
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="4">
                  <tr bgcolor="#DDDDDD">
                    <td colspan="2"><strong>クーポン見本</strong><img src="../../common/pc/image/spacer.gif" width="20" height="12"></td>
                  </tr>
                  <tr>
                    <td height="14" colspan="2" valign="top"><img src="../../common/pc/image/spacer.gif" width="200" height="1"></td>
                  </tr>
                  <tr>
                    <td class="size12">作成したいクーポンを選んでください。（見本は65%に縮小された大きさです。）<br>
                      <br>
                      <a href="#all">・ビジター／メンバー問わず</a>　　<a href="#member">・メンバー向け</a>　　<a href="#event">・イベント向け</a>　　<a href="#season">・季節ごと</a>
					  &nbsp;<input name="submit01" type="button" id="submit01" style="width:300; height:30; border:5px outset; color:blue;font-size:15px;font-weight:bold;" onClick="openCouponEditWindow('<%= hotelid %>', 99, 0)" value="クーポン（PC版地図付）新規作成"></td>
                    <td width="20" align="right">&nbsp;</td>
                  </tr>
                </table>
                </div>
              </td>
            </tr>
            <tr>
              <td colspan="2"><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
          </table>

          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td></td>
            </tr>
            <tr>
              <td valign="top">
                <table width="100%" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                    <td valign="top"><table border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td valign="top" class="size12"><a name="all"></a><strong>ビジター／メンバー問わず</strong></td>
                        <td valign="top">&nbsp;</td>
                        <td align="left" valign="top" nowrap>&nbsp;</td>
                        <td width="24" align="left" valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top" nowrap>&nbsp;</td>
                        <td valign="top" nowrap>&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_n_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top" nowrap>
                          <div class="size12"><strong>ベーシック・多目的タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様、一般のお客様問わず使え、<br>
                            サービス内容を変えられるので、<br>
                            様々な場面に対応できます。 <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=1&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_n_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top" nowrap>
                          <div class="size12"><strong>ベーシック・割引タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様、一般のお客様問わず、<br>
                            割り引き用に使えます。<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=2&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td valign="top" nowrap><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>

                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_opn_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>オープン記念用・<br>多目的タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様、一般のお客様問わず使え、<br>
                            オープン時に、サービス内容を変えて使うことができます。<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=3&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_opn_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>オープン記念用・<br>割引タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様、一般のお客様問わず、オープン時の、割引用に使用します。<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=4&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>

                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_rn_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>リニューアル記念用・<br>多目的タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様、一般のお客様問わず使え、リニューアル時にサービス内容を変えて使用できます。<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=5&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_rn_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>リニューアル記念用・<br>割引タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様、一般のお客様問わず、リニューアル時の割引用に使用します。 <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=6&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>
                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td valign="top" class="size12"><a name="member" class="size12"></a><strong>メンバー向け</strong></td>
                        <td valign="top">&nbsp;</td>
                        <td align="left" valign="top">&nbsp;</td>
                        <td width="24" align="left" valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_mb_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>メンバー様用・多目的タイプ</strong><br></div>
                          <div class="size12">
                            メンバーの方に対してのみ使用できます。<br>
                            サービス内容を変えられ、様々な場面に対応できます。 <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=7&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_mb_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>メンバー様用・割引タイプ</strong><br></div>
                          <div class="size12">
                            メンバーの方に対してのみ、割引用に使用します。<br>
                            <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=8&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>
                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_md_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>記念日用・多目的タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様の記念日に使えます。<br>
                            サービス内容を変えられます。 <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=9&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td width="213" valign="top"><img src="../../common/pc/image/cpn_md_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>記念日用・割引タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様の記念日に、割引用に使用します。<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=10&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>
                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td valign="top"><img src="../../common/pc/image/cpn_bd_f_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" align="left" valign="top">
                          <div class="size12"><strong>誕生日用・多目的タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様の誕生日に使えます。<br>
                            サービス内容を変えられます。<br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=11&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                        <td valign="top"><img src="../../common/pc/image/cpn_bd_off_prv.gif" width="213" height="117"></td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                        <td width="213" valign="top">
                          <div class="size12"><strong>誕生日用・割引タイプ</strong><br></div>
                          <div class="size12">
                            メンバー様の誕生日に、割引用に使用します。 <br>
                            <br>
                            <br>
                            <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=12&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                          </div>
                        </td>
                        <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                      </tr>
                      <tr>
                        <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                      </tr>
                      <tr>
                        <td valign="top" class="size12"><a name="event"></a><strong>イベント向け</strong></td>
                        <td valign="top">&nbsp;</td>
                        <td align="left" valign="top">&nbsp;</td>
                        <td width="24" align="left" valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_ny_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top">
                              <div class="size12"><strong>お正月用・多目的タイプ</strong><br></div>
                              <div class="size12">
                                お正月の時に、使用します。<br>
                                サービス内容を変えらます。 <br>
                                <br>
                                <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=13&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                                </div>
                            </td>
                            <td width="24" align="left" valign="top">&nbsp;</td>
                            <td valign="top"><img src="../../common/pc/image/cpn_ny_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>お正月用・割引タイプ</strong><br>
                            </div>
                              <div class="size12">お正月の時に、割引用に使用します。 <br>
                                  <br>
                                  <br>
                                  <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=14&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td colspan="7" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_vd_f_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" align="left" valign="top"><div class="size12"><strong>バレンタイン用・多目的タイプ</strong><br>
                                                                </div>
                                                          <div class="size12">バレンタインの時に、使用します。<br>
                                                                  サービス内容を変えらます。 <br>
                                                                  <br>
                                                                  <br>
                                                                    <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=15&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                                                          </div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top"><img src="../../common/pc/image/cpn_vd_off_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>バレンタイン用・割引タイプ</strong><br>
                                                                </div>
                                                          <div class="size12">バレンタインの時に、割引用に使用します。 <br>
                                                                  <br>
                                                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=16&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                                                          </div></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                          </tr>
                          <tr>
                            <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                          </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_wd_f_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" align="left" valign="top"><div class="size12"><strong>ホワイトデイ用・多目的タイプ</strong><br>
                                                                </div>
                                                          <div class="size12">ホワイトデイの時に、使用します。<br>
                                                                  サービス内容を変えられます。 <br>
                                                                  <br>
                                                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=17&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                                                          </div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top"><img src="../../common/pc/image/cpn_wd_off_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>ホワイトデイ用・割引タイプ</strong><br>
                                                                </div>
                                                          <div class="size12">ホワイトデイの時に、割引用に使用します。 <br>
                                                                  <br>
                                                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=18&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                                                          </div></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                          </tr>
                          <tr>
                            <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                          </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_hw_f_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>ハロウィン用・多目的タイプ</strong><br>
                                                                </div>
                                                          <div class="size12">ホワイトデイの時に、使用します。<br>
                                                                  サービス内容を変えられます。 <br>
<br>
<br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=19&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
</div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top"><img src="../../common/pc/image/cpn_hw_off_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>ハロウィン用・割引タイプ</strong><br>
                                                                </div>
                                                          <div class="size12">ハロウィンの時に、割引用に使用します。 <br>
                                                                  <br>
                                                              <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=20&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                                                          </div></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                          </tr>
                          <tr>
                            <td colspan="8"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                          </tr>
                          <tr>
                            <td width="213" valign="top"><img src="../../common/pc/image/cpn_xms_f_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" align="left" valign="top"><div class="size12"><strong>クリスマス用・多目的タイプ</strong><br>
                                                                </div>
                                                          <div class="size12">クリスマスの時に、使用します。<br>
                                                                  サービス内容を変えられます。 <br>
                                                                  <br>
                                                              <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=21&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                                                          </div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td width="213" valign="top"><img src="../../common/pc/image/cpn_xms_off_prv.gif" width="213" height="117"></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
                            <td width="213" valign="top"><div class="size12"><strong>クリスマス用・割引タイプ</strong><br>
                                                                </div>
                                                          <div class="size12">クリスマスの時に、割引用に使用します。<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=22&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                                                          </div></td>
                            <td valign="top"><img src="../../common/pc/image/spacer.gif" width="12" height="12"></td>
                          </tr>
                          <tr>
                            <td colspan="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            </tr>
                          <tr>
                            <td valign="top" class="size12"><a name="season"></a><strong>季節ごと</strong></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top">&nbsp;</td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_sp_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top"><div class="size12"><strong>春用・多目的タイプ</strong><br>
                            </div>
                              <div class="size12">春に使用します。<br>
  サービス内容を変えられます。 <br>
  <br>
  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=23&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                              </div></td>
                            <td width="24" align="left" valign="top"><img src="../../common/pc/image/spacer.gif" width="24" height="12"></td>
                            <td valign="top"><img src="../../common/pc/image/cpn_sp_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>春用・割引タイプ</strong><br>
                            </div>
                              <div class="size12">春に、割引用に使用します。<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=24&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td colspan="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_sm_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top"><div class="size12"><strong>夏用・多目的タイプ</strong><br>
                            </div>
                              <div class="size12">夏に使用します。<br>
  サービス内容を変えられます。 <br>
  <br>
  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=25&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
  </div></td>
                            <td width="24" align="left" valign="top">&nbsp;</td>
                            <td valign="top"><img src="../../common/pc/image/cpn_sm_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>夏用・割引タイプ</strong><br>
                            </div>
                              <div class="size12">夏に、割引用に使用します。<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=26&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td colspan="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_au_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top"><div class="size12"><strong>秋用・多目的タイプ</strong><br>
                            </div>
                              <div class="size12">秋に使用します。<br>
  サービス内容を変えられます。 <br>
  <br>
  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=27&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                              </div></td>
                            <td align="left" valign="top">&nbsp;</td>
                            <td valign="top"><img src="../../common/pc/image/cpn_au_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>秋用・割引タイプ</strong><br>
                            </div>
                              <div class="size12">秋に、割引用に使用します。<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=28&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                          <tr>
                            <td colspan="8" valign="top"><img src="../../common/pc/image/spacer.gif" width="400" height="20"></td>
                            </tr>
                          <tr>
                            <td valign="top"><img src="../../common/pc/image/cpn_wn_f_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td align="left" valign="top"><div class="size12"><strong>冬用・多目的タイプ</strong><br>
                            </div>
                              <div class="size12">冬に使用します。<br>
  サービス内容を変えられます。 <br>
  <br>
  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=29&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
</div></td>
                            <td width="24" align="left" valign="top">&nbsp;</td>
                            <td valign="top"><img src="../../common/pc/image/cpn_wn_off_prv.gif" width="213" height="117"></td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top"><div class="size12"><strong>冬用・割引タイプ</strong><br>
                            </div>
                              <div class="size12">冬に、割引用に使用します。<br>
                                  <br>
                                  <br>
                                <input name="submit01" type="button" id="submit01" onClick="MM_openBrWindow('../../common/pc/coupon_edit_form.jsp?HotelId=<%= hotelid %>&CouponType=30&Id=0','coupon','menubar=no,scrollbars=no,resizable=no,width=640,height=480')" value="このクーポンを作成">
                              </div></td>
                            <td valign="top">&nbsp;</td>
                          </tr>
                        </table>
                    </td>
                   </tr>
                    </table></td>
              </tr>
           </table>
<%
    }
%>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="../../common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="../../common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="../../common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="../../common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="../../common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="../../common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyright&copy; almex
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
