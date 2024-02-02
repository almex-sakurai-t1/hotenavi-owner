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
    String loginhotel = (String)session.getAttribute("LoginHotelId");
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
    int nowtime = cal.get(cal.HOUR_OF_DAY)*10000 + cal.get(cal.MINUTE)*100 + cal.get(cal.SECOND);

    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    String mode = request.getParameter("Mode");

//空室情報用
    String rec_flag           = request.getParameter("rec_flag");          //0:通常,1:メンバー専用
    if    (rec_flag          == null)   rec_flag = "0";
    String empty_title        = request.getParameter("empty_title");       //空室情報タイトル
    if    (empty_title       == null)   empty_title = "空室情報";
    String empty_flag         = request.getParameter("empty_flag");        //0:表示なし,1:表示あり,2:部屋ランク別,3:表示あり（"以上"なし）,4:表示あり部屋ランク別（"以上"なし）
    if    (empty_flag        == null)   empty_flag = "0";
    String empty_method       = request.getParameter("empty_method");      //0:有無表示,999:実数表示,1～998:指定数未満実数表示
    if    (empty_method      == null)   empty_method = "0";
    String no_vacancies       = request.getParameter("no_vacancies");      //満室時メッセージ
    if    (no_vacancies      == null)   no_vacancies = "";
    String vacancies_message  = request.getParameter("vacancies_message"); //空室時メッセージ
    if    (vacancies_message == null)   vacancies_message = "";
    String clean_flag         = request.getParameter("clean_flag");        //0:表示なし,1:表示あり,2:部屋ランク別,3:表示あり（"以上"なし）,4:表示あり部屋ランク別（"以上"なし）,5:表示あり（empty_method以上は表示なし）,6:表示あり部屋ランク別（empty_method以上は表示なし）
    if    (clean_flag        == null)   clean_flag = "0";
    String clean_method       = request.getParameter("clean_method");      //0:有無表示,999:実数表示,1～998:指定数未満実数表示
    if    (clean_method      == null)   clean_method = "0";
    String empty_list_method  = request.getParameter("empty_list_method"); //0:表示なし,999:すべて表示,1～998:未満表示
    if    (empty_list_method == null)   empty_list_method = "0";
    String clean_list_method  = request.getParameter("clean_list_method"); //0:表示なし,999:すべて表示,1～998:未満表示
    if    (clean_list_method == null)   clean_list_method = "0";
    String room_link          = request.getParameter("room_link");         //部屋リンク箇所
    if    (room_link         == null)   room_link = "";
    String line_count         = request.getParameter("line_count");        //一覧列数
    if    (line_count        == null)   line_count = "4";
    String allroom_flag       = request.getParameter("allroom_flag");      //1:全部屋表示
    if    (allroom_flag      == null)   allroom_flag = "0";
    String allroom_title      = request.getParameter("allroom_title");    //全部屋表示タイトル
    if    (allroom_title     == null)   allroom_title = "0";
    String room_exclude       = request.getParameter("room_exclude");      //全室表示時除外部屋
    if    (room_exclude      == null)   room_exclude = "";
    String allroom_flag2      = request.getParameter("allroom_flag2");      //1:全部屋表示2
    if    (allroom_flag2     == null)   allroom_flag2 = "0";
    String allroom_title2     = request.getParameter("allroom_title2");    //全部屋表示タイトル2
    if    (allroom_title2    == null)   allroom_title2 = "0";
    String room_exclude2      = request.getParameter("room_exclude2");      //全室表示時除外部屋2
    if    (room_exclude2     == null)   room_exclude2 = "";
    String allroom_flag3      = request.getParameter("allroom_flag3");      //1:全部屋表示3
    if    (allroom_flag3     == null)   allroom_flag3 = "0";
    String allroom_title3     = request.getParameter("allroom_title3");    //全部屋表示タイトル3
    if    (allroom_title3    == null)   allroom_title3 = "0";
    String room_exclude3      = request.getParameter("room_exclude3");      //全室表示時除外部屋3
    if    (room_exclude3     == null)   room_exclude3 = "";
    String allroom_empty_link        = request.getParameter("allroom_empty_link");        //全室表示空室時リンク箇所
    if    (allroom_empty_link       == null)   allroom_empty_link = "0";
    String allroom_novacancies_link  = request.getParameter("allroom_novacancies_link");  //全室表示在室時リンク箇所
    if    (allroom_novacancies_link == null)   allroom_novacancies_link = "0";
    String allroom_clean_link        = request.getParameter("allroom_clean_link");        //全室表示準備中時リンク箇所
    if    (allroom_clean_link       == null)   allroom_clean_link = "0";
    String konzatsu_flag      = request.getParameter("konzatsu_flag");     //1:混雑状況あり
    if    (konzatsu_flag     == null)   konzatsu_flag = "0";
    String reserve_flag       = request.getParameter("reserve_flag");      //1:ルーム予約リンクあり
    if    (reserve_flag      == null)   reserve_flag = "0";
    String crosslink_flag     = request.getParameter("crosslink_flag");    //1:系列店空室状況リンクあり；2：系列店空室状況リンク都道府県別
    if    (crosslink_flag    == null)   crosslink_flag = "0";
    String hotel_id_sub1      = request.getParameter("hotel_id_sub1");      //空満用ホテルID(1)（ホテルIDと違うときのみ入力）
    if    (hotel_id_sub1     == null)   hotel_id_sub1 = "";
    if    (hotel_id_sub1.compareTo(hotelid) == 0) hotel_id_sub1 = "";
    String hotel_id_sub2      = request.getParameter("hotel_id_sub2");      //空満用ホテルID(2)（ホテルIDと違うときのみ入力。2ホテル目を表示する際に使用）
    if    (hotel_id_sub2     == null)   hotel_id_sub2 = "";
    if    (hotel_id_sub2.compareTo(hotelid) == 0) hotel_id_sub2 = "";
    String empty_message_pc   = request.getParameter("empty_message_pc");   //PC空室情報下部表示メッセージ
    if    (empty_message_pc  == null)   empty_message_pc = "";

//予約用
    String reserve_title          = request.getParameter("reserve_title");          //ルーム予約タイトル
    if    (reserve_title         == null)   reserve_title = "ルーム予約";
    String reserve_message        = request.getParameter("reserve_message");        //予約メッセージ
    if    (reserve_message       == null)   reserve_message = "";
    String reserve_conductor      = request.getParameter("reserve_conductor");      //予約導線メッセージ
    if    (reserve_conductor     == null)   reserve_conductor = "";
    String reserve_conductor_mail = request.getParameter("reserve_conductor_mail"); //予約導線メール内メッセージ
    if    (reserve_conductor_mail == null)  reserve_conductor_mail = "";
    String mail                   = request.getParameter("mail");                   //日報送信元メールアドレス
    if    (mail == null)                    mail = "";
    String mail_bbs               = request.getParameter("mail_bbs");                //掲示板投稿時メールアドレス
    if    (mail_bbs == null)                mail_bbs = "";
    String mail_reserve           = request.getParameter("mail_reserve");           //予約専用メールアドレス
    if    (mail_reserve == null)            mail_reserve = "";
    if    (mail_reserve.equals(mail))       mail_reserve = "";
    String mail_reserve_info      = request.getParameter("mail_reserve_info");       //予約お知らせメールアドレス
    String reserve_mail_flag      =  request.getParameter("reserve_mail_flag");      //1:メールアドレス入力必須
    if    (reserve_mail_flag      == null)  reserve_mail_flag = "0";
    String reserve_update_flag    =  request.getParameter("reserve_update_flag");    //1:予約後の部屋変更可
    if    (reserve_update_flag    == null)  reserve_update_flag = "0";


//料金シミュレーション用
    String max_budget            = request.getParameter("max_budget");           //予算で調べるときの最大金額指定
    if    (max_budget           == null)   max_budget = "50000";
    String simulate24_flag       = request.getParameter("simulate24_flag");      //1:24時間を超えての計算をしない。2:実際に超えない
    if    (simulate24_flag      == null)   simulate24_flag = "1";

    String query = "";
    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;

    connection      = DBConnection.getConnection();
    if( mode.compareTo("NEW") == 0 )
    {
        query = "INSERT INTO hotel_config SET ";
        query = query + "hotel_id='"    + hotelid  + "', ";
        query = query + "rec_flag='"    + rec_flag + "', ";
        query = query + "add_hotelid='" + loginhotel + "', ";
        query = query + "add_userid='"  + ownerinfo.DbUserId + "', ";
        query = query + "add_date='"    + nowdate + "', ";
        query = query + "add_time='"    + nowtime + "', ";
    }
    else
    {
        query = "UPDATE hotel_config SET ";
    }
    query = query + "empty_title='"            + empty_title + "', ";
    query = query + "empty_flag='"             + empty_flag  + "', ";
    query = query + "empty_method='"           + empty_method + "', ";
    query = query + "no_vacancies='"           + ReplaceString.SQLEscape(no_vacancies) + "', ";
    query = query + "vacancies_message='"      + ReplaceString.SQLEscape(vacancies_message) + "', ";
    query = query + "clean_flag='"             + clean_flag + "', ";
    query = query + "clean_method='"           + clean_method + "', ";
    query = query + "empty_list_method='"      + empty_list_method + "', ";
    query = query + "clean_list_method='"      + clean_list_method + "', ";
    query = query + "room_link='"              + ReplaceString.SQLEscape(room_link) + "', ";
    query = query + "line_count='"             + line_count + "', ";
    query = query + "allroom_flag='"           + allroom_flag + "', ";
    query = query + "allroom_title='"          + ReplaceString.SQLEscape(allroom_title) + "', ";
    query = query + "room_exclude='"           + ReplaceString.SQLEscape(room_exclude) + "', ";
    query = query + "allroom_flag2='"           + allroom_flag2 + "', ";
    query = query + "allroom_title2='"          + ReplaceString.SQLEscape(allroom_title2) + "', ";
    query = query + "room_exclude2='"           + ReplaceString.SQLEscape(room_exclude2) + "', ";
    query = query + "allroom_flag3='"           + allroom_flag3 + "', ";
    query = query + "allroom_title3='"          + ReplaceString.SQLEscape(allroom_title3) + "', ";
    query = query + "room_exclude3='"           + ReplaceString.SQLEscape(room_exclude3) + "', ";
    query = query + "allroom_empty_link='"      + ReplaceString.SQLEscape(allroom_empty_link) + "', ";
    query = query + "allroom_novacancies_link='"+ ReplaceString.SQLEscape(allroom_novacancies_link) + "', ";
    query = query + "allroom_clean_link='"      + ReplaceString.SQLEscape(allroom_clean_link) + "', ";
    query = query + "konzatsu_flag='"          + konzatsu_flag + "', ";
    query = query + "reserve_flag='"           + reserve_flag + "', ";
    query = query + "crosslink_flag='"         + crosslink_flag + "', ";
    query = query + "reserve_title='"          + ReplaceString.SQLEscape(reserve_title) + "', ";
    query = query + "reserve_message='"        + ReplaceString.SQLEscape(reserve_message) + "', ";
    query = query + "reserve_conductor='"      + ReplaceString.SQLEscape(reserve_conductor) + "', ";
    query = query + "reserve_conductor_mail='" + ReplaceString.SQLEscape(reserve_conductor_mail) + "', ";
    query = query + "reserve_mail_flag='"      + reserve_mail_flag + "', ";
    query = query + "reserve_update_flag='"    + reserve_update_flag + "', ";
    query = query + "hotel_id_sub1='"          + ReplaceString.SQLEscape(hotel_id_sub1) + "', ";
    query = query + "hotel_id_sub2='"          + ReplaceString.SQLEscape(hotel_id_sub2) + "', ";
    query = query + "empty_message_pc='"       + ReplaceString.SQLEscape(empty_message_pc) + "', ";
    query = query + "max_budget='"             + max_budget + "', ";
    query = query + "simulate24_flag='"        + simulate24_flag + "', ";
    if (rec_flag.equals("1"))
    {
    query = query + "order_crosslink='999',order_bbs='999', ";
    }
    query = query + "upd_hotelid='"     + loginhotel + "', ";
    query = query + "upd_userid='"      + ownerinfo.DbUserId + "', ";
    query = query + "last_update='"     + nowdate + "', ";
    query = query + "last_uptime='"     + nowtime + "' ";

    if( mode.compareTo("UPD") == 0 )
    {
        query = query + " WHERE hotel_id='" + hotelid + "' AND rec_flag='" + request.getParameter("rec_flag") + "'";
    }

    // SQLクエリーの実行
    prestate = connection.prepareStatement(query);
    int ret  = prestate.executeUpdate();
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);


    query = "UPDATE hotel SET ";
    query = query + "mail='"               + mail              + "', ";
    query = query + "mail_bbs='"           + mail_bbs          + "', ";
    query = query + "mail_reserve='"       + mail_reserve      + "', ";
    query = query + "mail_reserve_info='"  + mail_reserve_info + "' ";
    query = query + " WHERE hotel_id='" + hotelid + "'";

    // SQLクエリーの実行
    prestate = connection.prepareStatement(query);
    int rethotel  = prestate.executeUpdate();
    DBConnection.releaseResources(result,prestate,connection);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>空満・予約設定</title>
<link href="/common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="/common/pc/style/access.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="scripts/main.js"></script>
</head>

<body bgcolor="#666666" background="/common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100" height="20" nowrap bgcolor="#22333F" class="tab">
                <font color="#FFFFFF">登録確認</font></td>
              <td width="15" height="20" valign="bottom"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
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
            <td><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
            <td><img src="/common/pc/image/spacer.gif" width="400" height="12"></td>
          </tr>
          <tr>
            <td width="8"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
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
                    <td class="size12"><form action="vacant_edit.jsp?HotelId=<%= hotelid %>&Recflag=<%= rec_flag %>" method="POST">
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
              <td><img src="/common/pc/image/tab_kado.gif" width="3" height="3"></td>
            </tr>
            <tr>
              <td bgcolor="#666666" height="100%"><img src="/common/pc/image/spacer.gif" width="3" height="100"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="3" bgcolor="#999999">
          <table width="100%" height="3" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="3"><img src="/common/pc/image/tab_kado2.gif" width="3" height="3"></td>
              <td bgcolor="#666666"><img src="/common/pc/image/spacer.gif" width="100" height="3"></td>
            </tr>
          </table>
        </td>
        <td height="3" width="3"><img src="/common/pc/image/grey.gif" width="3" height="3"></td>
      </tr>
      <!-- ここまで -->
    </table></td>
  </tr>
  <tr>
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18" align="absmiddle"><img src="/common/pc/image/spacer.gif" width="12" height="10" align="absmiddle">Copyrigtht&copy; imedia
      inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
