<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.happyhotel.common.*" %>
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
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    String mode = request.getParameter("Mode");
	
	//hotel_element
	String trial_date        = request.getParameter("trial_date");          	//リニューアル開始日付
    if    (trial_date        == null)   trial_date = "99999999";
	String start_date        = request.getParameter("start_date");          	//新ホテナビ稼動開始日付
    if    (start_date        == null)   trial_date = "99999999";
	String chk_age_flg       = request.getParameter("chk_age_flg");          	//年齢確認フラグ （1:クッションページあり）
    if    (chk_age_flg       == null)   chk_age_flg = "0";
	String html_head         = request.getParameter("html_head");          		//HTMLヘッダ情報
    if    (html_head         == null)   html_head = "";
	String html_login_form   = request.getParameter("html_login_form");    		//ログインフォーム
    if    (html_login_form   == null)   html_login_form = "";
	String offlineflg        = request.getParameter("offlineflg");          	//ログイン有無フラグ（0:メンバーページなし 1:メンバーパージあり）
    if    (offlineflg        == null)   offlineflg = "";
	String mailmagazineflg   = request.getParameter("mailmagazineflg");         //メルマガ有無フラグ（0：無し 1:有）
    if    (mailmagazineflg   == null)   mailmagazineflg = "1";
	String mailtoflg         = request.getParameter("mailtoflg");          		//ホテルへ一言有無フラグ（0：無し 1:有）
    if    (mailtoflg          == null)   mailtoflg = "1";
	String mailnameflg        = request.getParameter("mailnameflg");         	//ホテルへ一言ホテル件名有無（0：件名にホテル名無し(default) 1:件名にホテル名有）
    if    (mailnameflg        == null)   mailnameflg = "0";
	String mailname        = request.getParameter("mailname");         			//ホテルへ一言ホテル名
    if    (mailname        == null)   mailname = "";
	String viewflg        = request.getParameter("viewflg");         			//0:通常　1:参照バージョン 2:参照バージョン（メンバーも） 9:休止中
    if    (viewflg        == null)   viewflg = "0";
	String bbsgroupflg        = request.getParameter("bbsgroupflg");         	//多店舗掲示板の使用(0:通常　1:多店舗バージョン)
    if    (bbsgroupflg        == null)   bbsgroupflg = "0";
	String prize_hotelid        = request.getParameter("prize_hotelid");         //商品交換用ホテルID（未入力の場合はhotel_idを使用）
    if    (prize_hotelid        == null)   prize_hotelid = hotelid;
	if    (prize_hotelid.equals(""))   prize_hotelid = hotelid;
	String coupon_map_flg        = request.getParameter("coupon_map_flg");       //0:Yahoo!MAPを使用,1:画像ファイルを使用
    if    (coupon_map_flg        == null)   coupon_map_flg = "0";
	String coupon_map_img1        = request.getParameter("coupon_map_img1");     //クーポン画像1
    if    (coupon_map_img1        == null)   coupon_map_img1 = "";
	String coupon_map_img2        = request.getParameter("coupon_map_img2");     //クーポン画像2
    if    (coupon_map_img2        == null)   coupon_map_img2 = "";
	String bbs_temp_flg        = request.getParameter("bbs_temp_flg");           //0:通常掲示板,1:仮投稿掲示板（掲示板追加時にdel_flagに1をセット）
    if    (bbs_temp_flg        == null)   bbs_temp_flg = "0";
	String ranking_hidden_flg        = request.getParameter("ranking_hidden_flg");     //0:通常,1:ランキング情報を出力しない
    if    (ranking_hidden_flg        == null)   ranking_hidden_flg = "0";
	String ownercount        	= request.getParameter("ownercount");     //オーナーズルーム達成回数
    if    (ownercount        	== null)   ownercount = "0";
    String count_whatsnew_for_hoteltop        	= request.getParameter("count_whatsnew_for_hoteltop");     //TOPページのWhatsNew表示件数
    if    (count_whatsnew_for_hoteltop        	== null)  count_whatsnew_for_hoteltop = "3";
    if (!CheckString.numCheck(count_whatsnew_for_hoteltop)) count_whatsnew_for_hoteltop = "3";

    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    connection      = DBConnection.getConnection();
    if( mode.compareTo("NEW") == 0 )
    {
        query = "INSERT INTO hotenavi.hotel_element SET ";
        query = query + "hotel_id='"    + hotelid  + "', ";
    }
    else
    {
        query = "UPDATE hotenavi.hotel_element SET ";
    }

	query = query + "trial_date='"             + trial_date + "', ";
	query = query + "start_date='"             + start_date + "', ";
	query = query + "chk_age_flg='"            + chk_age_flg + "', ";
    query = query + "html_head='"              + ReplaceString.SQLEscape(html_head) + "', ";
    query = query + "html_login_form='"        + ReplaceString.SQLEscape(html_login_form) + "', ";
    query = query + "offlineflg='"             + offlineflg + "', ";
    query = query + "mailmagazineflg='"        + mailmagazineflg + "', ";
    query = query + "mailtoflg='"              + mailtoflg + "', ";
    query = query + "mailnameflg='"            + mailnameflg + "', ";
    query = query + "mailname='"      		   + ReplaceString.SQLEscape(mailname) + "', ";
    query = query + "viewflg='"                + viewflg + "', ";
    query = query + "bbsgroupflg='"            + bbsgroupflg + "', ";
    query = query + "prize_hotelid='"          + prize_hotelid + "', ";
    query = query + "coupon_map_flg='"         + coupon_map_flg + "', ";
    query = query + "coupon_map_img1='"        + coupon_map_img1 + "', ";
    query = query + "coupon_map_img2='"        + coupon_map_img2 + "', ";
    query = query + "bbs_temp_flg='"           + bbs_temp_flg + "', ";
    query = query + "ranking_hidden_flg='"     + ranking_hidden_flg + "', ";
    query = query + "ownercount='"             + ownercount + "', ";
    query = query + "count_whatsnew_for_hoteltop='"  + count_whatsnew_for_hoteltop + "' ";

    if( mode.compareTo("UPD") == 0 )
    {
        query = query + " WHERE hotel_id='" + hotelid + "'";
    }

    // SQLクエリーの実行
    prestate = connection.prepareStatement(query);
    int ret  = prestate.executeUpdate();
    DBSync.publish(query);
    DBSync.publish(query,true);
    DBConnection.releaseResources(result,prestate,connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>初期設定</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">登録確認</font></td>
              <td width="15" height="20" valign="bottom"><img src="../../common/pc/image/tab1.gif" width="15" height="20"></td>
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
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="size12">
<%
    if( ret != 0 )
    {
%>
登録しました。<br>
<%
    }
    else
    {
%>
登録に失敗しました。
<%
    }
%>
                    </td>
                  </tr>
                  <tr>
                    <td class="size12"><form action="etc_edit.jsp?HotelId=<%= hotelid %>" method="POST">
                      <INPUT name="submit_ret" type=submit value=戻る >
                    </form></td>
                  </tr>
                 </table>
              </div>
            </td>
          </tr>
        </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td></td>
              </tr>
            </table>
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
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="../../common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; imedia
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
