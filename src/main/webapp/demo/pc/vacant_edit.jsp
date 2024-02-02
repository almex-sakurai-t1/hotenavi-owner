<%@ page contentType="text/html;charset=Windows-31J"%><%@ page errorPage="error.jsp" %><%@ page import="java.sql.*" %><%@ page import="java.net.*" %><%@ page import="java.util.*" %><%@ page import="com.hotenavi2.common.*" %><%@ page import="jp.happyhotel.common.HotelElement" %><jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%><%
    String header_msg = "";
    String query;

//空室情報用
    int    rec_flag           = 0;               //0:通常,1:メンバー専用
    String empty_title        = "空室情報";
    int    empty_flag         = 0;               //0:表示なし,1:表示あり,2:部屋ランク別,3:表示あり（"以上"なし）,4:表示あり部屋ランク別（"以上"なし）
    int    empty_method       = 0;               //0:有無表示,999:実数表示,1～998:指定数未満実数表示
    String no_vacancies       = "";              //満室時メッセージ
    String vacancies_message  = "";              //空室時メッセージ
    int    clean_flag         = 0;               //0:表示なし,1:表示あり,2:部屋ランク別,3:表示あり（"以上"なし）,4:表示あり部屋ランク別（"以上"なし）,5:表示あり（empty_method以上は表示なし）,6:表示あり部屋ランク別（empty_method以上は表示なし）
    int    clean_method       = 0;               //0:有無表示,999:実数表示,1～998:指定数未満実数表示
    int    empty_list_method  = 0;               //0:表示なし,999:すべて表示,1～998:未満表示
    int    clean_list_method  = 0;               //0:表示なし,999:すべて表示,1～998:未満表示
    int    allroom_flag       = 0;               //1:全部屋表示
    int    allroom_flag2      = 0;               //1:全部屋表示（2つめ）
    int    allroom_flag3      = 0;               //1:全部屋表示（3つめ）
    String room_link          = "<td width=\"150\" height=\"30\" align=\"center\" valign=\"middle\" nowrap class=\"roomlist\"><a href=\"roomdetail.jsp?RoomDetail=%ROOMNO%\" class=\"link1\">%ROOMNO%</a></td>";         //部屋リンク箇所
    int    line_count         = 4;               //列数
    String room_exclude       = "";              //全室表示時除外部屋
    String allroom_title      = "部屋一覧";      //全室表示時タイトル
    String room_exclude2      = "";              //全室表示時除外部屋2
    String allroom_title2     = "部屋一覧";      //全室表示時タイトル2
    String room_exclude3      = "";              //全室表示時除外部屋3
    String allroom_title3     = "部屋一覧";      //全室表示時タイトル4
    String allroom_empty_link       = "<td width=\"150\" height=\"40\" align=\"center\" valign=\"middle\" bgcolor=\"#ffffff\"><a href=\"room%ROOMNO%.jsp\" class=\"link1\">%ROOMNO%</a><br>空  室</td>"; //全室表示空室時リンク箇所
    String allroom_novacancies_link = "<td width=\"150\" height=\"40\" align=\"center\" valign=\"middle\" class=\"roomlist\"><a href=\"room%ROOMNO%.jsp\" class=\"link1\">%ROOMNO%</a><br>在  室</td>";  //全室表示在室時リンク箇所
    String allroom_clean_link       = "<td width=\"150\" height=\"40\" align=\"center\" valign=\"middle\" class=\"cleanlist\"><a href=\"room%ROOMNO%.jsp\" class=\"link1\">%ROOMNO%</a><br>準備中</td>"; //全室表示作業中時リンク箇所
    int    konzatsu_flag      = 0;               //1:混雑状況あり
    int    reserve_flag       = 0;               //1:ルーム予約リンクあり
    int    crosslink_flag     = 0;               //1:系列店空室状況リンクあり；2：系列店空室状況リンク都道府県別
    String hotel_id_sub1      = "";              //空満用ホテルID(1)（ホテルIDと違うときのみ入力）
    String hotel_id_sub2      = "";              //空満用ホテルID(2)（ホテルIDと違うときのみ入力。2ホテル目を表示する際に使用）
    String empty_message_pc   = "";              //PCメッセージ

//予約用
    String reserve_title      = "ルーム予約";
    String reserve_message    = "";              //予約メッセージ
    String reserve_conductor  = "ホテルに到着されましたら､フロントまで予約番号をお伝えください｡<br>";
           reserve_conductor  = reserve_conductor + "[注意]予約時刻を過ぎてもご来店の無い場合､予約はキャンセルされます｡<br>";
           reserve_conductor  = reserve_conductor + "3回キャンセルされますと､以降のご利用は出来ません。<br>";
           reserve_conductor  = reserve_conductor + "メンバーお1人様につき1室まで予約可能です｡";
    String reserve_conductor_mail = "ホテルに到着されましたら、フロントまで予約番号をお伝えください｡";          //予約導線メール内メッセージ
    String mail               = "";              //ホテルメールアドレス
    String mail_report        = "";              //日報メール送信元アドレス
    String mail_bbs           = "";              //掲示板投稿メール用アドレス
    String mail_reserve       = "";              //予約専用メールアドレス
    String mail_reserve_info  = "";              //予約お知らせメールアドレス
    String object_no          = "";              //物件番号
    int    reserve_mail_flag  = 0;               //1:メールアドレス入力必須
    int    reserve_update_flag= 1;               //1:予約変更可


//料金シミュレーション
    int    max_budget         = 50000;           //予算で調べるときの最大金額指定
    int    simulate24_flag    = 1;               //1:24時間を超えての計算をしない。2:実際に24時間を超えない

    String mode               = "NEW";           //NEW:新規 UPD:更新

    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    if  (hotelid.compareTo("all") == 0)
    {
        hotelid    = request.getParameter("HotelId");
    }
    String recflag = request.getParameter("Recflag");
    if  (recflag == null)
    {
        recflag  = "0";
    }

    String recflag_name       = "";
    if  (recflag.compareTo("0") == 0)
    {
        recflag_name = "通常";
    }
    else
    {
        recflag_name = "会員用";
    }

    DateEdit  de = new DateEdit();
    int       nowdate   = Integer.parseInt(de.getDate(2));
    int       trialDate = HotelElement.getTrialDate(hotelid);
    int       lastUpdate = 0;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();
    query = "SELECT * FROM hotel WHERE hotel_id='" + hotelid + "'";
    prestate    = connection.prepareStatement(query);
    result      = prestate.executeQuery();

    if (result != null)
    {
        if( result.next() != false )
        {
            mail              = result.getString("mail");
            mail_report       = result.getString("mail");
            if(mail_report.equals("")) mail_report = "imedia-info@hotenavi.com";
            mail_bbs          = result.getString("mail_bbs");
            mail_reserve      = result.getString("mail_reserve");
            mail_reserve_info = result.getString("mail_reserve_info");
            object_no         = result.getString("object_no");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    query = "SELECT * FROM hotel_config WHERE hotel_id='" + hotelid + "'";
    query = query + " AND rec_flag='" + recflag +"'";
    prestate    = connection.prepareStatement(query);
    result      = prestate.executeQuery();

    if (result != null)
    {
        if( result.next() != false )
        {
            header_msg = "更新";
            mode = "UPD";
            rec_flag               = result.getInt("rec_flag");
            if (!result.getString("empty_title").equals(""))
                empty_title        = result.getString("empty_title");
            empty_flag             = result.getInt("empty_flag");
            empty_method           = result.getInt("empty_method");
            no_vacancies           = result.getString("no_vacancies");
            vacancies_message      = result.getString("vacancies_message");
            clean_flag             = result.getInt("clean_flag");
            clean_method           = result.getInt("clean_method");
            empty_list_method      = result.getInt("empty_list_method");
            clean_list_method      = result.getInt("clean_list_method");
            if (!result.getString("room_link").equals(""))
                room_link          = result.getString("room_link");
            if (result.getInt("line_count")!=0) 
                line_count         = result.getInt("line_count");
            allroom_flag             = result.getInt("allroom_flag");
            if (!result.getString("allroom_title").equals(""))
                allroom_title        = result.getString("allroom_title");
            room_exclude             = result.getString("room_exclude");
            allroom_flag2             = result.getInt("allroom_flag2");
            if (!result.getString("allroom_title2").equals(""))
                allroom_title2        = result.getString("allroom_title2");
            room_exclude2             = result.getString("room_exclude2");
            allroom_flag3             = result.getInt("allroom_flag3");
            if (!result.getString("allroom_title3").equals(""))
                allroom_title3        = result.getString("allroom_title3");
            room_exclude3             = result.getString("room_exclude3");
            if (!result.getString("allroom_empty_link").equals(""))
                allroom_empty_link   = result.getString("allroom_empty_link");
            if (!result.getString("allroom_novacancies_link").equals(""))
                allroom_novacancies_link = result.getString("allroom_novacancies_link");
            if (!result.getString("allroom_clean_link").equals(""))
                allroom_clean_link   = result.getString("allroom_clean_link");
            konzatsu_flag          = result.getInt("konzatsu_flag");
            reserve_flag           = result.getInt("reserve_flag");
            crosslink_flag         = result.getInt("crosslink_flag");
            reserve_message        = result.getString("reserve_message");
            if (!result.getString("reserve_title").equals(""))
                reserve_title          = result.getString("reserve_title");
            if (!result.getString("reserve_conductor").equals(""))
                reserve_conductor      = result.getString("reserve_conductor");
            if (!result.getString("reserve_conductor_mail").equals(""))
                reserve_conductor_mail = result.getString("reserve_conductor_mail");
            reserve_mail_flag      = result.getInt("reserve_mail_flag");
            hotel_id_sub1          = result.getString("hotel_id_sub1");
            hotel_id_sub2          = result.getString("hotel_id_sub2");
            if (object_no.indexOf("n") == 0 || result.getInt("max_budget") != 0)
                max_budget         = result.getInt("max_budget");
            simulate24_flag        = result.getInt("simulate24_flag");
            empty_message_pc       = result.getString("empty_message_pc");
            reserve_update_flag    = result.getInt("reserve_update_flag");
            lastUpdate             = result.getInt("last_update");
        }
        else
        {
            header_msg = "新規作成";
            mode = "NEW";
        }
    }

    DBConnection.releaseResources(result,prestate,connection);

    //新ホテナビ対応
    if (nowdate >= trialDate)
    {
        if (room_link.indexOf("roomitem") == -1)
        {
            room_link = "<div class=\"oldSite\">" + room_link;
            room_link +="</div>\r\n<div class=\"roomitem\"><span class=\"roomlist\"><a href=\"room/detail/%ROOMNO%\">%ROOMNO%</a></span></div>";
        }
        if (allroom_empty_link.indexOf("roomitem") == -1)
        {
            allroom_empty_link = "<div class=\"oldSite\">" + allroom_empty_link;
            allroom_empty_link +="</div>\r\n<div class=\"roomitem\"><span class=\"roomempty\" id=\"room%ROOMNO%\"><a href=\"room/detail/%ROOMNO%\">%ROOMNO%</a><br>空  室</span></div>";
        }
        if (allroom_novacancies_link.indexOf("roomitem") == -1)
        {
            allroom_novacancies_link = "<div class=\"oldSite\">" + allroom_novacancies_link;
            allroom_novacancies_link +="</div>\r\n<div class=\"roomitem\"><span class=\"roomlist\" id=\"room%ROOMNO%\"><a href=\"room/detail/%ROOMNO%\">%ROOMNO%</a><br>在  室</span></div>";
        }
        if (allroom_clean_link.indexOf("roomitem") == -1)
        {
            allroom_clean_link = "<div class=\"oldSite\">" + allroom_clean_link;
            allroom_clean_link +="</div>\r\n<div class=\"roomitem\"><span class=\"roomclean\" id=\"room%ROOMNO%\"><a href=\"room/detail/%ROOMNO%\">%ROOMNO%</a><br>準備中</span></div>";
        }
    }

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>空満・予約設定</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
function MM_openPreview(input,hotelid,rec_flag){
  if( input == 'preview' )
  {
    document.form1.target = '_blank';
    document.form1.action = 'vacant_edit_preview.jsp?HotelId='+hotelid+'&Recflag='+rec_flag;
  }
  document.form1.submit();
}

function MM_openInput(input,hotelid,mode){
  if( input == 'input' )
  {
    document.form1.target = '_self';
    document.form1.action = 'vacant_edit_input.jsp?HotelId='+hotelid+'&Mode='+mode;
  }
  else if( input == 'edit' )
  {
    document.form1.target = '_self';
    document.form1.action = 'vacant_edit.jsp?HotelId='+hotelid+'&Recflag='+mode;
  }
  document.form1.submit();
}
</script>

</head>

<body bgcolor="#666666" background="../../common/pc/image/bg.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"><%= header_msg %></font></td>
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
        <td align="center" valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12">
              </td>
            </tr>
            <tr>
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td colspan="2" class="size12"><font color="#CC0000"><strong>※このページを編集し終えたら、「<%= header_msg %>」ボタンを必ず押してください</strong></font>
					<% if (lastUpdate<trialDate && trialDate!=99999999){%><br><font size="4" color="#FF0000"><strong>※新ホテナビリニューアル開始後は、必ず更新してください</strong></font><%}%>
					</td>
                  </tr>
                  <form name=form1 method=post>
                  <input name="rec_flag" type="hidden"  value="<%= recflag %>"> 
				  <tr align="left">
<% 
if  (mode.compareTo("NEW") == 0)
{
%>
                      <td align="left" colspan="2" valign="middle" bgcolor="#969EAD">&nbsp;<strong><%= recflag_name %></strong>&nbsp;<input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('input', '<%= hotelid %>', '<%= mode %>')"></td>
<%
}
else
   if (recflag.compareTo("0") == 0)  //通常用
	{
%>
                      <td align="left" valign="middle" bgcolor="#969EAD" class="size18">&nbsp;<strong><%= recflag_name %></strong>&nbsp;<input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('input', '<%= hotelid %>', '<%= mode %>')"></td>
                      <td align="left" valign="middle" bgcolor="#969EAD" class="size18"><input name="regsubmit" type=button value="会員専用はこちら" onClick="MM_openInput('edit', '<%= hotelid %>', '1')"></td>
<%
	}
	else  //会員専用
	{
%>
                      <td align="left" valign="middle" bgcolor="#969EAD" class="size18">&nbsp;<strong><%= recflag_name %></strong>&nbsp;<input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('input', '<%= hotelid %>', '<%= mode %>')"></td>
                      <td align="left" valign="middle" bgcolor="#969EAD" class="size18"><input name="regsubmit" type=button value="通常用はこちら" onClick="MM_openInput('edit', '<%= hotelid %>', '0')"></td>
<%
	}
%>
                   </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">空室情報タイトル名</td>
					<td nowrap class="size12">
						<input name="empty_title" type="text" size="30" value="<%= empty_title %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">空室情報表示</td>
					<td nowrap class="size12">
						<select name="empty_flag"> 
							<option value=0 <%if (empty_flag == 0){%>selected<%}%>>0.表示無し</option>
							<option value=1 <%if (empty_flag == 1){%>selected<%}%>>1.表示有（部屋一覧：標準）</option>
							<option value=2 <%if (empty_flag == 2){%>selected<%}%>>2.表示有（部屋一覧：ランク別）</option>
							<option value=3 <%if (empty_flag == 3){%>selected<%}%>>3.表示有（部屋一覧：標準,「以上」をつけない）</option>
							<option value=4 <%if (empty_flag == 4){%>selected<%}%>>4.表示有（部屋一覧：ランク別,「以上」をつけない）</option>
						</select>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">空室情報表示方法</td>
					<td nowrap class="size12">
						<input type="radio" name="empty_method" id="empty_method_radio1" value="0"    <%if (empty_method == 0)  {%>checked<%}%> onclick="document.getElementById('empty_method_text').value=this.value;">有無表示
						<input type="radio" name="empty_method" id="empty_method_radio2" value="999"  <%if (empty_method == 999){%>checked<%}%> onclick="document.getElementById('empty_method_text').value=this.value;">実数表示
						・指定数未満実数表示<input name="empty_method" id="empty_method_text" type="text" size="3" value="<%= empty_method %>" style="text-align:right;ime-mode:disabled" onchange="if(this.value==0){document.getElementById('empty_method_radio1').checked=true;}else{document.getElementById('empty_method_radio1').checked=false;};if(this.value==999){document.getElementById('empty_method_radio2').checked=true;}else{document.getElementById('empty_method_radio2').checked=false;}">←指定数を入力
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">&nbsp;</td>
                    <td nowrap class="size12">&nbsp;例）5室未満実数表示のときは「5」に設定</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">満室の場合の表示文</td>
					<td nowrap class="size12"><input name="no_vacancies" type="text" size="20" value="<%= no_vacancies %>"> 例）満員御礼</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">空室の場合の表示文</td>
					<td nowrap class="size12"><input name="vacancies_message" type="text" size="20" value="<%= vacancies_message %>"> 例）早くおいでよ～</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">準備中情報表示</td>
					<td nowrap class="size12">
						<select name="clean_flag"> 
							<option value=0 <%if (clean_flag == 0){%>selected<%}%>>0.表示無し</option>
							<option value=1 <%if (clean_flag == 1){%>selected<%}%>>1.表示有（部屋一覧：標準）</option>
							<option value=2 <%if (clean_flag == 2){%>selected<%}%>>2.表示有（部屋一覧：部屋ランク別）</option>
							<option value=3 <%if (clean_flag == 3){%>selected<%}%>>3.表示有（部屋一覧：標準,「以上」をつけない）</option>
							<option value=4 <%if (clean_flag == 4){%>selected<%}%>>4.表示有（部屋一覧：ランク別,「以上」をつけない）</option>
							<option value=5 <%if (clean_flag == 5){%>selected<%}%>>5.表示有（部屋一覧：標準，空室指定数以上は表示しない）</option>
							<option value=6 <%if (clean_flag == 6){%>selected<%}%>>6.表示有（部屋一覧：部屋ランク別，空室指定数以上は表示しない）</option>
						</select>
					</td>
                  </tr>
                 <tr align="left">
                    <td nowrap class="size12">準備中表示方法</td>
					<td nowrap class="size12">
						<input type="radio" name="clean_method" id="clean_method_radio1" value="0"   <%if (clean_method == 0)  {%>checked<%}%> onclick="document.getElementById('clean_method_text').value=this.value;">有無表示
						<input type="radio" name="clean_method" id="clean_method_radio2" value="999" <%if (clean_method == 999)  {%>checked<%}%>  onclick="document.getElementById('clean_method_text').value=this.value;">実数表示
						・指定数未満実数表示<input name="clean_method" id="clean_method_text" type="text" size="3" value="<%= clean_method %>" style="text-align:right;ime-mode:disabled" onchange="if(this.value==0){document.getElementById('clean_method_radio1').checked=true;}else{document.getElementById('clean_method_radio1').checked=false;};if(this.value==999){document.getElementById('clean_method_radio2').checked=true;}else{document.getElementById('clean_method_radio2').checked=false;}">←指定数を入力
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">空室一覧表示条件</td>
					<td nowrap class="size12">
						<input type="radio" name="empty_list_method" id="empty_list_method_radio1" value="0"   <%if (empty_list_method == 0)  {%>checked<%}%> onclick="document.getElementById('empty_list_method_text').value=this.value;">表示無し
						<input type="radio" name="empty_list_method" id="empty_list_method_radio2" value="999" <%if (empty_list_method == 999)  {%>checked<%}%> onclick="document.getElementById('empty_list_method_text').value=this.value;">全て表示
						・指定数未満の場合表示<input name="empty_list_method" id="empty_list_method_text" type="text" size="3" value="<%= empty_list_method %>" style="text-align:right;ime-mode:disabled"  onchange="if(this.value==0){document.getElementById('empty_list_method_radio1').checked=true;}else{document.getElementById('empty_list_method_radio1').checked=false;};if(this.value==999){document.getElementById('empty_list_method_radio2').checked=true;}else{document.getElementById('empty_list_method_radio2').checked=false;}">←指定数を入力
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">準備中一覧表示条件</td>
					<td nowrap class="size12">
						<input type="radio" name="clean_list_method" id="clean_list_method_radio1" value="0"   <%if (clean_list_method == 0)  {%>checked<%}%> onclick="document.getElementById('clean_list_method_text').value=this.value;">表示無し
						<input type="radio" name="clean_list_method" id="clean_list_method_radio2" value="999" <%if (clean_list_method == 999)  {%>checked<%}%> onclick="document.getElementById('clean_list_method_text').value=this.value;">全て表示
						・指定数未満の場合表示<input name="clean_list_method" id="clean_list_method_text" type="text" size="3" value="<%= clean_list_method %>" style="text-align:right;ime-mode:disabled"  onchange="if(this.value==0){document.getElementById('clean_list_method_radio1').checked=true;}else{document.getElementById('clean_list_method_radio1').checked=false;};if(this.value==999){document.getElementById('clean_list_method_radio2').checked=true;}else{document.getElementById('clean_list_method_radio2').checked=false;}">←指定数を入力
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">部屋リンク箇所</td>
					<td nowrap class="size12">
						<textarea name="room_link" rows="3" cols="80" style="ime-mode:active"><%=room_link%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">部屋一覧列数</td>
					<td nowrap class="size12">
						<input name="line_count" id="line_count" type="text" size="2" value="<%= line_count %>" style="text-align:right;ime-mode:disabled">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">全部屋表示あり</td>
					<td nowrap class="size12">
						<input type="checkbox" name="allroom_flag" value="1"   <%if (allroom_flag == 1) {%>checked<%}%>>
						全部屋一覧タイトル<input name="allroom_title" type="text" size="30" value="<%= allroom_title %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">全部屋表示の場合、除外部屋</td>
					<td nowrap class="size12">
						<input name="room_exclude" type="text" size="80" value="<%= room_exclude %>" style="ime-mode:disable"><font size=1>(コンマ区切り)</font>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">全部屋表示あり(2)</td>
					<td nowrap class="size12">
						<input type="checkbox" name="allroom_flag2" value="1"   <%if (allroom_flag2 == 1) {%>checked<%}%>>
						全部屋一覧タイトル(2)<input name="allroom_title2" type="text" size="30" value="<%= allroom_title2 %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">全部屋表示の場合、除外部屋(2)</td>
					<td nowrap class="size12">
						<input name="room_exclude2" type="text" size="80" value="<%= room_exclude2 %>" style="ime-mode:disable"><font size=1>(コンマ区切り)</font>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">全部屋表示あり(3)</td>
					<td nowrap class="size12">
						<input type="checkbox" name="allroom_flag3" value="1"   <%if (allroom_flag3 == 1) {%>checked<%}%>>
						全部屋一覧タイトル(3)<input name="allroom_title3" type="text" size="30" value="<%= allroom_title3 %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">全部屋表示の場合、除外部屋(3)</td>
					<td nowrap class="size12">
						<input name="room_exclude3" type="text" size="80" value="<%= room_exclude3 %>" style="ime-mode:disable"><font size=1>(コンマ区切り)</font>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">全部屋一覧リンク箇所</td>
					<td nowrap class="size12">
						<textarea name="allroom_empty_link" rows="3" cols="80" style="ime-mode:active"><%=allroom_empty_link%></textarea>（空　室）<br>
						<textarea name="allroom_novacancies_link" rows="3" cols="80" style="ime-mode:active"><%=allroom_novacancies_link%></textarea>（在　室）<br>
						<textarea name="allroom_clean_link" rows="3" cols="80" style="ime-mode:active"><%=allroom_clean_link%></textarea>（準備中）<br>
					</td>
                  </tr>
                  <tr align="left">
                    <td><img src="../../common/pc/image/spacer.gif" width="120" height="5"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">混雑状況リンクあり</td>
					<td nowrap class="size12">
						<input type="checkbox" name="konzatsu_flag" value="1"   <%if (konzatsu_flag == 1) {%>checked<%}%>>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">ルーム予約リンクあり</td>
					<td nowrap class="size12">
						<input type="checkbox" name="reserve_flag" value="1"   <%if (reserve_flag == 1) {%>checked<%}%>>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">系列店空室状況リンク</td>
					<td nowrap class="size12">
						<input type="radio" name="crosslink_flag" value="0"   <%if (crosslink_flag == 0) {%>checked<%}%>>なし
						<input type="radio" name="crosslink_flag" value="1"   <%if (crosslink_flag == 1) {%>checked<%}%>>あり
						<input type="radio" name="crosslink_flag" value="2"   <%if (crosslink_flag == 2) {%>checked<%}%>>あり（都道府県別）
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">空満用ホテルID</td>
					<td nowrap class="size12">
						(1)<input name="hotel_id_sub1" type="text" size="10" value="<%= hotel_id_sub1 %>" style="ime-mode:inactive">
						(2)<input name="hotel_id_sub2" type="text" size="10" value="<%= hotel_id_sub2 %>" style="ime-mode:inactive">
						（違うホテルID内容を表示する際のみ入力）
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">空室情報下部メッセージ</td>
					<td nowrap class="size12">
						<textarea name="empty_message_pc" rows="3" cols="80" style="ime-mode:active"><%= empty_message_pc.replace("\"","&quot;") %></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">【メールの設定】</td>
					<td nowrap class="size12">
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">日報メール送信元等</td>
					<td nowrap class="size12">
						<input name="mail" id="mail" type="text" size="64" maxlength="64" value="<%= mail %>" style="ime-mode:disabled">
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">掲示板投稿時</td>
					<td nowrap class="size12">
						<textarea name="mail_bbs" id="mail_bbs" type="text" rows=3 cols=80 style="ime-mode:disabled"><%= mail_bbs %></textarea>
					</td>
				  </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>

                  <tr align="left">
                    <td nowrap class="size12">【予約】</td>
					<td nowrap class="size12">
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">予約タイトル名</td>
					<td nowrap class="size12">
						<input name="reserve_title" type="text" size="30" value="<%= reserve_title %>" style="ime-mode:active">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">予約メッセージ</td>
					<td nowrap class="size12">
						<textarea name="reserve_message" rows="4" cols="80" style="ime-mode:active"><%=reserve_message%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">予約導線メッセージ</td>
					<td nowrap class="size12">
						<textarea name="reserve_conductor" rows="4" cols="80" style="ime-mode:active"><%=reserve_conductor%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">予約導線メール</td>
					<td nowrap class="size12">
						<textarea name="reserve_conductor_mail" rows="4" cols="80" style="ime-mode:active"><%=reserve_conductor_mail%></textarea>
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">確認メール送信元アドレス</td>
					<td nowrap class="size12">
						<input type=checkbox name="mail_check" <%if(mail_reserve.equals("")){%>checked<%}%> onclick="if(this.checked){document.form1.mail_reserve.value='';}else{document.form1.mail_reserve.value=document.form1.mail_report.value;}">日報メール送信元メール（<%=mail_report%>）と同じ<br>
						<input type=hidden name="mail_report" value="<%=mail_report%>"> 
						別途設定：<input type=text   name="mail_reserve" size="64" value="<%=mail_reserve%>" style="ime-mode:inactive" onchange="if(this.value==document.form1.mail.value||this.value==''){document.form1.mail_check.checked=true;}else{document.form1.mail_check.checked=false;}">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">オーナー向け確認メール</td>
					<td nowrap class="size12">
						設定時送信<input type=text   name="mail_reserve_info" size="64" value="<%=mail_reserve_info%>" style="ime-mode:inactive">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">メールアドレスの入力</td>
					<td nowrap class="size12">
						<input type="checkbox" name="reserve_mail_flag" value="1"   <%if (reserve_mail_flag == 1) {%>checked<%}%>>必須
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">予約後の部屋変更</td>
					<td nowrap class="size12">
						<input type="checkbox" name="reserve_update_flag" value="1"   <%if (reserve_update_flag == 1) {%>checked<%}%>>可
					</td>
                  </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">【料金シミュレーション】(<%=object_no%>)</td>
					<td nowrap class="size12">
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">予算で調べる最大金額</td>
					<td nowrap class="size12">
						<input name="max_budget" id="max_budget" type="text" size="5" maxlength="6" value="<%= max_budget %>" style="text-align:right;ime-mode:disabled" onchange="if (this.value<=50000 <%if (object_no.indexOf("n") == 0){%> && this.value != 0<%}%>){this.value=50000}">
						<%if (object_no.indexOf("n") == 0){%>（金額を0にすると、予算によるシミュレーションをしません）<%}%>
					</td>
				  </tr>
                  <tr align="left">
                    <td nowrap class="size12">24時間を超えての計算</td>
					<td nowrap class="size12">
						<input type="radio" name="simulate24_flag" value="0" <%if (simulate24_flag == 0) {%>checked<%}%>>する
						<input type="radio" name="simulate24_flag" value="1" <%if (simulate24_flag == 1) {%>checked<%}%>>しない
						<input type="radio" name="simulate24_flag" value="2" <%if (simulate24_flag == 2) {%>checked<%}%>>実際に超えない
					</td>
				  </tr>
                  <tr align="left">
                    <td width="150"><img src="../../common/pc/image/spacer.gif" width="120" height="14"></td>
                  </tr>
                </form>  
                </table>
              </td>
              </tr>
              <tr>
                <td valign="top">&nbsp;</td>
                <td valign="top"></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="new/image/tab_kado.gif" width="3" height="3"></td>
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
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18"><img src="../../common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; imedia
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="../../common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
