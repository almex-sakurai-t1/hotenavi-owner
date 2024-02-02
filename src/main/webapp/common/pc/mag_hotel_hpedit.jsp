<%@ page contentType="text/html; charset=Windows-31J"%>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="jp.happyhotel.common.CheckString" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/mag_hpedit_ini.jsp" %>
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
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String header_msg = "";
    String query = "";
    // ﾊﾟﾗﾒｰﾀ
    int update_flag       = 0;
    int update_week       = 0;
    int update_force      = 0;
    String key_word       = "";
    int last_update       = 0;
    int last_uptime       = 0;
    int last_hour         = 0;
    int last_minute       = 0;
    int last_second       = 0;
    String address        = "";
    String mag_address    = "";
    String username       = "";
    String password       = "";
    String message        = "";
    String reg_address    = "";
    String reg_username   = "";
    String reg_password   = "";
    String reg_message    = "";
    int member_only       = 0;
    int group_cancel_flag = 0;
    int group_add_flag    = 0;
    int change_flag       = 0;
    int delete_flag       = 0;
    int add_flag          = 0;
    int report_mail_flag  = 0;
    String qr_identifier  = "";
    int input_send_flag   = 0;
    String input_send_message = "";
    String request_message = "";
    String subject_hotelname= "";

    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
    if(hotelid != null && !CheckString.numAlphaCheck(hotelid))
    {
        response.sendError(400);
        return;
    }
    mag_address = hotelid + "_mag@hotenavi.com";
    username    = hotelid + "_mag";
    password    = hotelid + "_mag";

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();


    int               imedia_user = 0;
    int               level       = 0;
    // imedia_user のチェック
    try
    {
        query = "SELECT * FROM owner_user WHERE hotelid=?";
        query = query + " AND userid=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginHotelId);
        prestate.setInt(2, ownerinfo.DbUserId);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            imedia_user = result.getInt("imedia_user");
            level       = result.getInt("level");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    // メンバーページ有無のチェック
    boolean member_flag = false;
    try
    {
        query = "SELECT * FROM menu WHERE hotelid='" + hotelid + "'";
        query = query + " AND contents='search.jsp'";
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            member_flag = true;
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    //ホテル名/FTPパスワードの取得
    String ftp_password = "";
    String hname = "";
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            ftp_password = result.getString("ftp_passwd");
            hname        = result.getString("name");
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
	
    Calendar cal = Calendar.getInstance();
    int nowdate = cal.get(cal.YEAR)*10000 + (cal.get(cal.MONTH)+1)*100 + cal.get(cal.DATE);
//新ホテナビ対応
    int trial_date = 99999999;
    int start_date = 99999999;


    query = "SELECT * FROM hotel_element WHERE hotel_id= ?";
    prestate    = connection.prepareStatement(query);
    prestate.setString( 1, hotelid);
    result      = prestate.executeQuery();

    if (result != null)
    {
        if( result.next() )
        {
            trial_date              	= result.getInt("trial_date");
            start_date              	= result.getInt("start_date");
        }
    }
    DBConnection.releaseResources(result);
    DBConnection.releaseResources(prestate);

    try
    {
        query = "SELECT * FROM mag_hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if (result != null)
        {
            if( result.next() != false )
            {
                header_msg = "更新";
                update_flag       = result.getInt("update_flag");
                update_week       = result.getInt("update_week");
                update_force      = result.getInt("update_force");
                message           = result.getString("message");
                reg_message       = result.getString("reg_message");
                key_word          = result.getString("key_word");
                last_update       = result.getInt("last_update");
                last_uptime       = result.getInt("last_uptime");
                address           = result.getString("address");
                mag_address       = result.getString("mag_address");
                username          = result.getString("username");
                password          = result.getString("password");
                reg_address       = result.getString("reg_address");
                reg_username      = result.getString("reg_username");
                reg_password      = result.getString("reg_password");
                member_only       = result.getInt("member_only");
                change_flag       = result.getInt("change_flag");
                group_cancel_flag = result.getInt("group_cancel_flag");
                delete_flag       = result.getInt("delete_flag");
                group_add_flag    = result.getInt("group_add_flag");
                add_flag          = result.getInt("add_flag");
                report_mail_flag  = result.getInt("report_mail_flag");
                qr_identifier     = result.getString("qr_identifier");
                input_send_flag   = result.getInt("input_send_flag");
                input_send_message= result.getString("input_send_message");
                request_message   = result.getString("request_message");
                subject_hotelname = result.getString("subject_hotelname");
            }
            else
            {
                header_msg = "メールマガジン設定がされていません。";
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }
    last_hour   = last_uptime / 10000;
    last_minute = (last_uptime - (last_hour * 10000)) / 100;
    last_second = last_uptime - (last_hour * 10000) - (last_minute * 100);


    int   disp_idx   = 0;
    int   data_type  = 61;
    int   i          = 0;
    String[]  msg             = new String[8];
    String[]  msg_title       = new String[8];
    for( i = 0 ; i < 8 ; i++ )
    {
        msg[i]       = "";
        msg_title[i] = "";
    }

    try
    {
        query = "SELECT * FROM edit_event_info WHERE hotelid='" + hotelid + "'";
        query = query + " AND data_type=" + data_type;
        prestate    = connection.prepareStatement(query);
        result      = prestate.executeQuery();
        if (result != null)
        {
            if( result.next() != false )
            {
                for( i = 0 ; i < 8 ; i++ )
                {
                    if( result.getString("msg" + (i + 1) + "_title").length() != 0 || result.getString("msg" + (i + 1)).length() > 1)
                    {
                        msg[i]             = result.getString("msg" + (i + 1));
                        msg_title[i]       = result.getString("msg" + (i + 1) + "_title");
                    }
                }
            }
        }
    }
    catch( Exception e )
    {
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);;
    }
    finally
    {
        DBConnection.releaseResources(result,prestate,connection);
    }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>メールマガジン管理HP編集</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<%}%>
<script type="text/javascript" src="../../common/pc/scripts/mailmagazine_form.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/tohankaku.js"></script>

<script type="text/javascript">

function MM_openInput(input,hotelid){
  document.form1.target = '_self';
  document.form1.action = 'mag_hotel_hpedit_input.jsp?HotelId='+hotelid;
  document.form1.submit();
}
</script>

</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">メルマガHP編集　更新</font></td>
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
        <td align="center" valign="top" >
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr bgcolor="#FFFFFF">
              <td><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><img src="../../common/pc/image/spacer.gif" width="400" height="12"></td>
            </tr>
            <tr bgcolor="#FFFFFF">
              <td width="8"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
<% 
if  (header_msg.compareTo("更新") != 0)
{
%>
				  <tr align="left">
                      <td align="left" valign="middle" bgcolor="#969EAD" colspan="2"><strong><%= header_msg %></strong></td>
                   </tr>

<%
}
else
{
%>
                  <tr>
                    <td colspan="2" class="size12">
						<font color="#CC0000"><strong>※このページを編集し終えたら、「<%= header_msg %>」ボタンを必ず押してください</strong></font><br>
					※HP編集はホームページ側が最新でないと反映されません。<strong><font color="#0000FF">ホームページ側の調整が必要</font></strong>となりますのでお手数ですが株式会社アルメックスまで連絡願います。<br>
<%
     if (imedia_user == 1 && start_date > nowdate)
     {
%><%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="org.apache.commons.net.ftp.FTPClient" %>
<%@ page import="org.apache.commons.net.ftp.FTPReply" %>
<%
        File checkFile = null;
        boolean checkMode = true;
        boolean existBtn  = true;
//        boolean existMagCancel  = true;
        boolean existContents = false;
        boolean existCommon = true;
        boolean existLength = false;
        boolean existTenpoId = false;
        int     ContentsSize = 0;
        String ftp_server = "";
        String mailmagazine_check = ReplaceString.getParameter(request,"mailmagazine_check");
        if (mailmagazine_check == null)
        {
            checkMode = false;
            mailmagazine_check = "check";
        }
        String uploadBtn = ReplaceString.getParameter(request,"uploadBtn");
        if (uploadBtn != null)
        {
            sendBtn(hotelid,ftp_password);
            checkMode = true;
        }
        String uploadContents = ReplaceString.getParameter(request,"uploadContents");
        if (uploadContents != null)
        {
            sendContents(hotelid,ftp_password);
            checkMode = true;
        }
        if (mailmagazine_check.equals("check"))
        {
            checkFile = new File("/hotenavi/demo/upd_mailmagazine/temp/pc/image/btn_kakunin.gif");
            checkFile.delete();
            ftp_server = "jupiter.hotenavi.com";
            recieveTrans(ftp_server,hotelid,ftp_password,"/hotenavi/demo/upd_mailmagazine/temp/pc/image/","image/","btn_kakunin.gif");
            if (checkFile.length() == 0)
            {
                checkFile.delete();
                existBtn = false;
            }
//            checkFile = new File("/hotenavi/demo/upd_mailmagazine/temp/magcancel.ini");
//            checkFile.delete();
//            ftp_server = "neptune.hotenavi.com";
//            recieveTrans(ftp_server,hotelid,ftp_password,"/hotenavi/demo/upd_mailmagazine/temp/","","magcancel.ini");
//            if (checkFile.length() == 0)
//            {
//                checkFile.delete();
//                existMagCancel = false;
//            }
            checkFile = new File("/hotenavi/demo/upd_mailmagazine/temp/mailmagazine.jsp");
            checkFile.delete();
            ftp_server = "jupiter.hotenavi.com";
            recieveTrans(ftp_server,hotelid,ftp_password,"/hotenavi/demo/upd_mailmagazine/temp/","","mailmagazine.jsp");
            ContentsSize = (int)checkFile.length();
            if (checkFile.length() == 0)
            {
                checkFile.delete();
                existContents = false;
            }
            else
            {
                BufferedReader br = new BufferedReader(new FileReader("/hotenavi/demo/upd_mailmagazine/temp/mailmagazine.jsp")); 
                String line = null;
                while((line = br.readLine()) != null){
                if (line.indexOf("common/mailmagazine/mailmagazine.jsp") != -1){
                existContents = true;
                }
                }
                br.close();
            }
            checkFile = new File("/hotenavi/demo/upd_mailmagazine/temp/common.ini");
            checkFile.delete();
            ftp_server = "jupiter.hotenavi.com";
            recieveTrans(ftp_server,hotelid,ftp_password,"/hotenavi/demo/upd_mailmagazine/temp/","","common.ini");
            ContentsSize = (int)checkFile.length();
            if (checkFile.length() == 0)
            {
                checkFile.delete();
                existCommon = false;
            }
            else
            {
                BufferedReader br = new BufferedReader(new FileReader("/hotenavi/demo/upd_mailmagazine/temp/common.ini")); 
                String line = null;
                while((line = br.readLine()) != null){
                if (line.indexOf("m_length") != -1){
                existLength = true;
                }
                if (line.indexOf("tenpo_id") != -1){
                existTenpoId = true;
                }
                }
                br.close();
            }
        }
%>						<form action="mag_hotel_hpedit.jsp" method="post"><input name="mailmagazine_check" type="submit" value="<%=mailmagazine_check%>">
<%
    if(checkMode)
    {
        {if (!existContents){%><br>新コンテンツではありません。mailmagazine.jsp のサイズ:<%=ContentsSize%>。<input name="uploadContents" type="submit" value="upload"><%}}
        {if (!existBtn){%><br>PC用ボタンがUPされていません。<input name="uploadBtn" type="submit" value="upload"><%}}
        {if (!existLength){%><br>m_length が定義されていません。common.iniを編集してUPしてください。<%}}
        {if (!existTenpoId){%><br>tenpo_id が定義されていません。common.iniを編集してUPしてください。<%}}
        if (existContents && existBtn && existLength && existTenpoId){%>コンテンツは最新です<%}
    }
%>
						</form>
<%
    }
%>                    </td>
					<td align="right">&nbsp;</td>
                  </tr>
                  <form name=form1 method=post>
				  <tr align="left">
                      <td align="left" valign="middle" bgcolor="#969EAD" colspan="2">
					  	<input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('input', '<%= hotelid %>')">
						<input name="hname" type="hidden" value="<%=hname%>">
					  </td>
                   </tr>
                  <tr align="left">
                    <td nowrap class="size12">メールマガジンの受付制限</td>
					<td nowrap class="size12">
						<input name="member_only" type="radio" value="0" <% if (member_only%10 == 0){%>checked<%}%>>すべて受付
<%
    if (member_flag)
    {
%>
						<input name="member_only" type="radio" value="1" <% if (member_only%10 == 1){%>checked<%}%>>メンバーのみ
<%
    }
%>
						<input name="member_only" type="radio" value="9" <% if (member_only%10 == 9){%>checked<%}%>>受付停止
					</td>
                  </tr>
<%
    if  (imedia_user == 1)
    {
%>
                  <tr align="left">
                    <td nowrap class="size12"><em>QRメンバー登録受付制限(i)</em></td>
					<td nowrap class="size12">
						<input name="reg_member_only" type="radio" value="0" <% if (member_only/10%10 == 0){%>checked<%}%>>受付
						<input name="reg_member_only" type="radio" value="9" <% if (member_only/10%10 == 9){%>checked<%}%>>受付停止
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12"><em>QRメンバー店舗識別コード(i)</em></td>
					<td nowrap class="size12">
						<input name="qr_identifier" type="text" size="5" maxlength="5" value="<%= qr_identifier %>" style="ime-mode:disabled"><br>
					</td>
				  </tr>
                  <tr align="left" style="display:none">
                    <td nowrap class="size12"><em>PC版QRコード表示(i)</em></td>
					<td nowrap class="size12">
						<input name="qr_member_only" type="hidden" value="9">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12"><em>PC版メンバー入力(i)</em></td>
					<td nowrap class="size12">
						<input name="search_member_only" type="radio" value="0" <% if (member_only/1000%10 == 0){%>checked<%}%>>通常
						<input name="search_member_only" type="radio" value="9" <% if (member_only/1000%10 == 9){%>checked<%}%>>なし
					</td>
                  </tr>
<%
    }
    else
    {
%>
						<input name="reg_member_only" type="hidden" value="<%=member_only/10%10%>">
						<input name="qr_member_only" type="hidden" value="<%=member_only/100%10%>">
						<input name="search_member_only" type="hidden" value="<%=member_only/1000%10%>">
						<input name="qr_identifier" type="hidden" value="<%= qr_identifier %>"><br>
<%
    }
%>

                  <tr align="left">
                    <td nowrap class="size12">メールアドレス変更機能</td>
					<td nowrap class="size12">
						<input name="change_flag" type="radio" value="0" <% if (change_flag == 0){%>checked<%}%>>なし
						<input name="change_flag" type="radio" value="1" <% if (change_flag == 1){%>checked<%}%>>あり
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">メールアドレス削除機能（メンバーのみ）</td>
					<td nowrap class="size12">
						<input name="delete_flag" type="radio" value="0" <% if (delete_flag == 0){%>checked<%}%>>なし
						<input name="delete_flag" type="radio" value="1" <% if (delete_flag == 1){%>checked<%}%>>あり
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">登録済会員のメールアドレス追加機能</td>
					<td nowrap class="size12">
						<input name="add_flag" type="radio" value="0" <% if (add_flag == 0){%>checked<%}%>>あり
						<input name="add_flag" type="radio" value="1" <% if (add_flag == 1){%>checked<%}%>>なし
					</td>
                  </tr>
<!--
                  <tr align="left">
                    <td nowrap class="size12">メルマガ登録時の系列店対応</td>
					<td nowrap class="size12">
						<input name="group_add_flag" type="radio" value="0" <% if (group_add_flag == 0){%>checked<%}%>>操作店舗のみ
						<input name="group_add_flag" type="radio" value="1" <% if (group_add_flag == 1){%>checked<%}%>>系列店一括
						<input name="group_add_flag" type="radio" value="2" <% if (group_add_flag == 2){%>checked<%}%>>系列店選択
					</td>
                  </tr>
-->
						<input name="group_add_flag" type="hidden" value="<%=group_add_flag%>"
                  <tr align="left">
                    <td nowrap class="size12">メルマガ解除時の系列店対応</td>
					<td nowrap class="size12">
						<input name="group_cancel_flag" type="radio" value="0" <% if (group_cancel_flag == 0){%>checked<%}%>>操作店舗のみ
						<input name="group_cancel_flag" type="radio" value="1" <% if (group_cancel_flag == 1){%>checked<%}%>>系列店一括
						<input name="group_cancel_flag" type="radio" value="2" <% if (group_cancel_flag == 2){%>checked<%}%>>系列店選択
					</td>
                  </tr>
                  <tr align="left">
<%
    if  (imedia_user == 1)
    {
%>
                    <td nowrap class="size12" valign=top><em>空メール送信先アドレス(i)</em></td>
					<td nowrap class="size12">
						<input name="mag_address" type="text" size="50" value="<%= mag_address %>" style="ime-mode:disabled"><br>
						ユーザー名：<input name="username" type="text" size="20" value="<%= username %>" style="ime-mode:disabled">
						パスワード：<input name="password" type="text" size="20" value="<%= password %>" style="ime-mode:disabled">
					</td>
<%
    }
    else
    {
%>
                    <td nowrap class="size12" valign=top>空メール送信先アドレス</td>
					<td nowrap class="size12"><input name="mag_address" type="text" size="50" value="<%= mag_address%>" readonly>（修正できません）</td>
<%
    }
%>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12">メルマガ登録時（空メールの返信）<br>のメール内メッセージ</td>
					<td nowrap class="size12"><textarea type="text" id="message" name="message" cols="60" rows="4"><%= message %></textarea></td>
                  </tr>
<%
    if  (imedia_user == 1)
    {
%>                  <tr align="left">
                    <td nowrap class="size12"><em>登録メールアドレス　テキスト入力有無(i)</em></td>
					<td nowrap class="size12">
						<input name="input_send_flag" type="radio" value="1" <% if (input_send_flag == 1){%>checked<%}%> onclick="setInputSendMessage(1);">あり
						<input name="input_send_flag" type="radio" value="0" <% if (input_send_flag == 0){%>checked<%}%> onclick="setInputSendMessage(0);">なし
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12"><em>メールアドレス入力時（返信）の<br>登録メール内メッセージ(i)</em></td>
					<td nowrap class="size12"><textarea type="text" id="input_send_message" name="input_send_message" cols="60" rows="4"><%= input_send_message %></textarea></td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12"><em>メール配信画面内のメッセージ(i)</em></td>
					<td nowrap class="size12"><textarea type="text" id="request_message" name="request_message" cols="60" rows="4"><%= request_message %></textarea></td>
                  </tr>
<script type="text/javascript">

function setInputSendMessage(input_send_flag){
  if(document.getElementById("input_send_message").value == "" && input_send_flag == 1)
  {
    document.getElementById("input_send_message").value = document.getElementById("message").value + "\r\n";
    document.getElementById("input_send_message").value = document.getElementById("input_send_message").value + "%ADDRESS%" + "\r\n\r\n";
    document.getElementById("input_send_message").value = document.getElementById("input_send_message").value + "本メールにお心あたりがない場合は、お手数ですが削除していただくか、そのまま無視していただきますようお願いいたします。";
  }
  else if(input_send_flag == 0)
  {
    document.getElementById("input_send_message").value = "";
  }
  var DefaultMessage1 = "ありがとうございます。<br/>ホテルからお得な情報をお届けします。<br/><br/>";
  var DefaultMessage2 = "※「<b>登録する</b>」ボタンを押しても送信メールが立ち上がらない場合は<br/>下記のフォームにメールアドレスを入力の上「<b>確認して登録する</b>」を押してください。<br/>";
  if(input_send_flag == 1)
  {
    if(document.getElementById("request_message").value == "" || document.getElementById("request_message").value == DefaultMessage1)
    {
      document.getElementById("request_message").value = DefaultMessage1;
      document.getElementById("request_message").value = document.getElementById("request_message").value + DefaultMessage2;
    }
  }
  else
  {
    if(document.getElementById("request_message").value == (DefaultMessage1 + DefaultMessage2))
    {
      document.getElementById("request_message").value = DefaultMessage1;
    }
  }
}
</script>

<%
    }
%>
<%
    if  (imedia_user == 1)
    {
%>
                  <tr align="left">
                    <td nowrap class="size12" valign=top><em>QRメンバー登録<br>空メール送信先アドレス(i)</em></td>
					<td nowrap class="size12">
						<input name="reg_address" type="text" size="50" value="<%= reg_address %>" style="ime-mode:disabled"><br>
						ユーザー名：<input name="reg_username" type="text" size="20" value="<%= reg_username %>" style="ime-mode:disabled">
						パスワード：<input name="reg_password" type="text" size="20" value="<%= reg_password %>" style="ime-mode:disabled">
					</td>
                  </tr>
                  <tr align="left">
                    <td nowrap class="size12"><em>QRメンバー登録時（空メールの返信）<br>のメール内メッセージ(i)</em></td>
					<td nowrap class="size12"><textarea type="text" name="reg_message" cols="60" rows="4"><%= reg_message %></textarea></td>
                  </tr>
<%
    }
%>
                  <tr align="left">
                    <td nowrap class="size12">合言葉</td>
					<td nowrap class="size12"><input name="key_word" type="text" size="20" value="<%= key_word %>"> 例）ホテナビ</td>
                  </tr>
<%  
    if (member_flag)
    {
%>
                  <tr align="left">
                    <td nowrap class="size12">メンバーが登録完了時にホテルにメール送信</td>
					<td nowrap class="size12">
						<input name="report_mail_flag" type="radio" value="0" <% if (report_mail_flag == 0){%>checked<%}%>>なし
						<input name="report_mail_flag" type="radio" value="1" <% if (report_mail_flag == 1){%>checked<%}%>>あり
					</td>
                  </tr>
<%
    }
    else
    {
%>
					<input type="hidden" name="report_mail_flag"  value="0">
<%
    }
%><%
    if  (imedia_user == 1)
    {
%>
                  <tr align="left">
                    <td nowrap class="size12" valign=middle><em>登録完了時メール 件名に出力する店舗名(i)</em></td>
					<td nowrap class="size12">
						<input name="subject_hotelname" type="text" size="32" maxlength="255" value="<%= subject_hotelname %>"><br>
					</td>
                  </tr>
<%
    }
%>

                  <tr align="left" bgcolor="#FFFFFF">
                    <td colspan="2"><img src="/common/pc/image/spacer.gif" width="200" height="20"></td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td width="8" bgcolor="#FFFFFF"><img src="../../common/pc/image/spacer.gif" width="8" height="12"></td>
              <td><div class="size12">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">

<!--小見出しここから(1) -->
<%
    for( i = 0 ; i < 8 ; i++ )
    {
      if(imedia_user== 1 || ImediaOnly[disp_idx][i]==0)
      {
        if (Explain[disp_idx][i].compareTo("") == 0)
        {
%>
				<input name="col_msg<%= i + 1 %>"             type="hidden" value="">
<%
        }
        else
        {
%>
				<tr>
					<td colspan="2" align="left" valign="middle" style="font-size:12px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<% if(ImediaOnly[disp_idx][i]==1){%><em><%=Explain[disp_idx][i]%>(i)</em><%}else{%><strong><%=Explain[disp_idx][i]%></strong><%}%>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="2" align="left" valign="top">
						<div class="size12">
						<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="size12">
<%
            if (Decoration[disp_idx][i] == 1)
            {
%>
								<select id="spansize<%= i + 1 %>" name="spansize<%= i + 1 %>" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onchange="if(spansize<%= i + 1 %>.selectedIndex !=0){enclose('<font size=&quot;' + spansize<%= i + 1 %>.options[spansize<%= i + 1 %>.selectedIndex].value + '&quot;>', '</font>');spansize<%= i + 1 %>.selectedIndex=0;return false;}">
									<option value="">文字サイズ</option><option value="1">小さく</option><option value="3">少し大きく</option><option value="4">大きく</option><option value="5">とても大きく</option></select>
								<input type="button" name="strong<%= i + 1 %>" value="太" style="font-weight:bold" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<strong>', '</strong>');return false;">
								<input type="button" name="em<%= i + 1 %>" value="斜" style="font-style:oblique" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<em>', '</em>');return false;">
								<input type="button" name="under<%= i + 1 %>" value="線" style="text-decoration:underline" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<u>', '</u>');return false;">
							</td>
							<td class="size12">
<input type="image" border="0" style="margin:1px;margin-left:5px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#000000&quot;>', '</font>');return false;" src="/common/pc/image/000000.gif" alt="black"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#FF0000&quot;>', '</font>');return false;" src="/common/pc/image/FF0000.gif" alt="red"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#FF1493&quot;>', '</font>');return false;" src="/common/pc/image/FF1493.gif" alt="deeppink"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#800080&quot;>', '</font>');return false;" src="/common/pc/image/800080.gif" alt="purple"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#0000FF&quot;>', '</font>');return false;" src="/common/pc/image/0000FF.gif" alt="blue"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#008000&quot;>', '</font>');return false;" src="/common/pc/image/008000.gif" alt="green"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#808000&quot;>', '</font>');return false;" src="/common/pc/image/808000.gif" alt="olive"/><br/>
<input type="image" border="0" style="margin:1px;margin-left:5px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#FFFFFF&quot;>', '</font>');return false;" src="/common/pc/image/FFFFFF.gif" alt="white"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#FA8072&quot;>', '</font>');return false;" src="/common/pc/image/FA8072.gif" alt="salmon"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#EE82EE&quot;>', '</font>');return false;" src="/common/pc/image/EE82EE.gif" alt="violet"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#9370DB&quot;>', '</font>');return false;" src="/common/pc/image/9370DB.gif" alt="mediumpurple"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#00BFFF&quot;>', '</font>');return false;" src="/common/pc/image/00BFFF.gif" alt="deepskyblue"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#33CC33&quot;>', '</font>');return false;" src="/common/pc/image/33CC33.gif" alt="limegreen"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<font color=&quot;#FFD700&quot;>', '</font>');return false;" src="/common/pc/image/FFD700.gif" alt="gold"/>
							</td>
							<td>
								<input type="hidden" id="spancol<%= i + 1 %>" name="spancol<%= i + 1 %>" value="#" size=7 maxlength="7" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" >
								<!--							<input type="button" name="spanbtn<%= i + 1 %>" value="色" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('<span style=&quot;color:' + document.getElementById('spancol<%= i + 1 %>').value+'&quot;>', '</span>');return false;">-->
								<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn_d<%= i + 1 %>" width="100" height="22" align="absmiddle" id="color_btn_d<%= i + 1 %>" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onClick="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);MM_openBrWindowDetail('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',<%= i + 1 %>)" onMouseOver="MM_swapImage('color_btn_d<%= i + 1 %>','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
<%
            }
%>
							</td>
						</tr>
						</table>
						</div>
					</td>
				</tr>
				<tr align="left" bgcolor="#FFFFFF">
					<td colspan="2" class="honbuntitlebar_text">
						<div class="honbun_margin" style="float:left;clear:both;">
							<textarea id="col_msg<%= i + 1 %>" name=col_msg<%= i + 1 %> rows=8 cols=64  onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onkeyup="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onmouseup="get_pos(col_msg<%= i + 1 %>);" onmousedown="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onblur="dispArea<%= i + 1 %>.innerHTML='';"><%if (msg[i].length() > 1){%><%= msg[i].replace("\"","&quot;") %><%}else{%><%=MsgStandard[disp_idx][i]%><%}%></textarea>
							<input name="old_msg<%= i + 1 %>" type="hidden" value="<%= msg[i].replace("\"","&quot;") %>" >
						</div> 
						<div class="size12">
								<input type="button" name="keyword<%= i + 1 %>" value="合言葉"  onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onclick="enclose('%KEYWORD%', '');return false;"><br>
<%
            if (Decoration[disp_idx][i] == 1)
            {
%>
								装飾する場合は、マウス等で装飾したいテキスト範囲を指定してから各ボタンをクリックしてください。<br>
<%
            }
%>
<br/>
								ホテル名挿入箇所は、%NAME% と入力してください。<br/>
								合言葉挿入箇所は、%KEYWORD% と入力してください。<br/>
			<%if(i==1){%>		メンバーズNo.挿入箇所は、%CustomId% と入力してください。<%}%>

						</div>
					</td>
				</tr>
<script language="javascript">
if( String("jadge") ){
 bl=3;
} else if( document.getElementById ){
 bl=4;
}
if( document.getElementById("col_msg<%= i + 1 %>").setSelectionRange ){
  bl=2;
} else if( document.selection.createRange ){
  bl=1;
}
</script>
				<tr align="left">
					<td colspan="2" >
						<div class="honbun" id="dispArea<%= i + 1 %>">
						</div>
					</td>
				</tr>
<%
            if (FooterTitle[disp_idx][i] != null)
            {
%>
				<tr align="left">
					<td colspan="2" >
						<div class="honbun size12">
						－－－－－－－－－－－－－－－－－<br>
						<%=FooterTitle[disp_idx][i]%><br>
						<input name="col_msg<%= i + 1 %>_title"     type="text"   size=75 maxlength=128 value="<% if(msg_title[i].equals("null")){%><%}else if(msg_title[i].equals("")){%><%=FooterDefault[disp_idx][i]%><%}else{%><%=msg_title[i]%><%}%>">
						<input name="old_msg<%= i + 1 %>_title" type="hidden" value="<%= msg_title[i] %>" ><br>
						－－－－－－－－－－－－－－－－－<br>
						</div>
					</td>
				</tr>
<%
            }
%>
                <tr align="left" bgcolor="#FFFFFF">
                    <td colspan="2"><img src="/common/pc/image/spacer.gif" width="200" height="3"></td>
                </tr>
<%
        }
      }
      else
      {
%>
					<input type=hidden name=col_msg<%= i + 1 %> value="<%= msg[i]%>">
<%
      }
    }
%>
							<input type="hidden" name="hreflink" id="hreflink" value="http://">
							<input type="hidden" name="hrefblank" id="hrefblank" value="_self">
							<input type="hidden" name="imglink" id="imglink" value="image/">
<!-- 小見出しここまで -->

                  <tr align="left" bgcolor="#FFFFFF">
                    <td colspan="2"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>

              </form>
<%  
}
%>

                </table>
              </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;</td>
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="../../common/pc/image/imedia.gif" width="63" height="18" border="0"><img src="../../common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyright&copy; almex
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
              </tr>
              <tr bgcolor="#FFFFFF">
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
      <tr bgcolor="#FFFFFF">
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
</table>
</body>
</html>
<%!
public static int recieveTrans(String host,String user,String password,String path,String server_path,String filename)
throws IOException {
    FTPClient fp = new FTPClient();
    FileOutputStream os = null;
    
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return 1;
        }
        if (fp.login(user, password) == false) { // ログインできたか？
            return 2;
        }
// ファイル受信
        os = new FileOutputStream(path+filename);// クライアント側
        if(!fp.retrieveFile(server_path+filename, os))// サーバー側
        {
           return 3;
        }
        os.close();
    }
    finally
    {
        fp.disconnect();
    }
    return 0;
}%>
<%!
public static int sendBtn(String user,String password)
throws IOException {
    FTPClient fp = new FTPClient();
    FileInputStream is = null;
    String host = "jupiter.hotenavi.com";
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return 1;
        }

        if (fp.login(user, password) == false) { // ログインできたか？
            return 2;
        }
// ファイル送信
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/image/btn_kakunin.gif");// クライアント側
        if (!fp.storeFile("/image/btn_kakunin.gif", is))// サーバー側
        {
           return 3;
        }
        is.close();
    }
    finally
    {
        fp.disconnect();
    }
    return 0;
}
%>
<%!
public static int sendContents(String user,String password)
throws IOException {
    FTPClient fp = new FTPClient();
    FileInputStream is = null;
    String host;
    host = "jupiter.hotenavi.com";
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return 1;
        }

        if (fp.login(user, password) == false) { // ログインできたか？
            return 2;
        }
// ファイル送信
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazine.jsp");// クライアント側
        fp.storeFile("/mailmagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/magazine_qr.jsp");// クライアント側
        fp.storeFile("/magazine_qr.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazinecancel.jsp");// クライアント側
        fp.storeFile("/mailmagazinecancel.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazinecancellist.jsp");// クライアント側
        fp.storeFile("/mailmagazinecancellist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazinecancelupdate.jsp");// クライアント側
        fp.storeFile("/mailmagazinecancelupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazinemember.jsp");// クライアント側
        fp.storeFile("/mailmagazinemember.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazinememberuserid.jsp");// クライアント側
        fp.storeFile("/mailmagazinememberuserid.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazineregist.jsp");// クライアント側
        fp.storeFile("/mailmagazineregist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazinereplace.jsp");// クライアント側
        fp.storeFile("/mailmagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/mailmagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazinerequest.jsp");// クライアント側
        fp.storeFile("/mailmagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/mailmagazinevisitor.jsp");// クライアント側
        fp.storeFile("/mailmagazinevisitor.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/memberinfoget.jsp");// クライアント側
        fp.storeFile("/memberinfoget.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazine.jsp");// クライアント側
        fp.storeFile("/membermagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinecancel.jsp");// クライアント側
        fp.storeFile("/membermagazinecancel.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinecancelupdate.jsp");// クライアント側
        fp.storeFile("/membermagazinecancelupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinedelete.jsp");// クライアント側
        fp.storeFile("/membermagazinedelete.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinedeleteupdate.jsp");// クライアント側
        fp.storeFile("/membermagazinedeleteupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinememberreplace.jsp");// クライアント側
        fp.storeFile("/membermagazinememberreplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinememberreplaceupdate.jsp");// クライアント側
        fp.storeFile("/membermagazinememberreplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinereplace.jsp");// クライアント側
        fp.storeFile("/membermagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/membermagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinerequest.jsp");// クライアント側
        fp.storeFile("/membermagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinestate.jsp");// クライアント側
        fp.storeFile("/membermagazinestate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermagazinestateupdate.jsp");// クライアント側
        fp.storeFile("/membermagazinestateupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermailmagazineindex.jsp");// クライアント側
        fp.storeFile("/membermailmagazineindex.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/pc/membermailmagazinelogin.jsp");// クライアント側
        fp.storeFile("/membermailmagazinelogin.jsp", is);// サーバー側
        is.close();

        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazine.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazinecancel.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazinecancel.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazinecancellist.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazinecancellist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazinecancelupdate.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazinecancelupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazinemember.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazinemember.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazinememberuserid.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazinememberuserid.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazineregist.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazineregist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazinereplace.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazinerequest.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/mailmagazinevisitor.jsp");// クライアント側
        fp.storeFile("/smart/mailmagazinevisitor.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazine.jsp");// クライアント側
        fp.storeFile("/smart/membermagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazinedelete.jsp");// クライアント側
        fp.storeFile("/smart/membermagazinedelete.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazinedeleteupdate.jsp");// クライアント側
        fp.storeFile("/smart/membermagazinedeleteupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazinememberreplace.jsp");// クライアント側
        fp.storeFile("/smart/membermagazinememberreplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazinememberreplaceupdate.jsp");// クライアント側
        fp.storeFile("/smart/membermagazinememberreplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazinereplace.jsp");// クライアント側
        fp.storeFile("/smart/membermagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/smart/membermagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazinerequest.jsp");// クライアント側
        fp.storeFile("/smart/membermagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazinestate.jsp");// クライアント側
        fp.storeFile("/smart/membermagazinestate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermagazinestateupdate.jsp");// クライアント側
        fp.storeFile("/smart/membermagazinestateupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/smart/membermailmagazinelogin.jsp");// クライアント側
        fp.storeFile("/smart/membermailmagazinelogin.jsp", is);// サーバー側
        is.close();
    }
    finally
    {
        fp.disconnect();
    }

    host = "neptune.hotenavi.com";
    try
    {
        fp.connect(host);
        if (!FTPReply.isPositiveCompletion(fp.getReplyCode())) { // コネクトできたか？
            return 1;
        }

        if (fp.login(user, password) == false) { // ログインできたか？
            return 2;
        }
// ファイル送信
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazine.jsp");// クライアント側
        fp.storeFile("/i/mailmagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazine.jsp");// クライアント側
        fp.storeFile("/j/mailmagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazine.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinecancel.jsp");// クライアント側
        fp.storeFile("/i/mailmagazinecancel.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinecancel.jsp");// クライアント側
        fp.storeFile("/j/mailmagazinecancel.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinecancel.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazinecancel.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinecancellist.jsp");// クライアント側
        fp.storeFile("/i/mailmagazinecancellist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinecancellist.jsp");// クライアント側
        fp.storeFile("/j/mailmagazinecancellist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinecancellist.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazinecancellist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinecancelupdate.jsp");// クライアント側
        fp.storeFile("/i/mailmagazinecancelupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinecancelupdate.jsp");// クライアント側
        fp.storeFile("/j/mailmagazinecancelupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinecancelupdate.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazinecancelupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinemember.jsp");// クライアント側
        fp.storeFile("/i/mailmagazinemember.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinemember.jsp");// クライアント側
        fp.storeFile("/j/mailmagazinemember.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinemember.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazinemember.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinememberuserid.jsp");// クライアント側
        fp.storeFile("/i/mailmagazinememberuserid.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinememberuserid.jsp");// クライアント側
        fp.storeFile("/j/mailmagazinememberuserid.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinememberuserid.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazinememberuserid.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazineregist.jsp");// クライアント側
        fp.storeFile("/i/mailmagazineregist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazineregist.jsp");// クライアント側
        fp.storeFile("/j/mailmagazineregist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazineregist.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazineregist.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinereplace.jsp");// クライアント側
        fp.storeFile("/i/mailmagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinereplace.jsp");// クライアント側
        fp.storeFile("/j/mailmagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinereplace.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/i/mailmagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/j/mailmagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinerequest.jsp");// クライアント側
        fp.storeFile("/i/mailmagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinerequest.jsp");// クライアント側
        fp.storeFile("/j/mailmagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinerequest.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinevisitor.jsp");// クライアント側
        fp.storeFile("/i/mailmagazinevisitor.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinevisitor.jsp");// クライアント側
        fp.storeFile("/j/mailmagazinevisitor.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/mailmagazinevisitor.jsp");// クライアント側
        fp.storeFile("/ez/mailmagazinevisitor.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazine.jsp");// クライアント側
        fp.storeFile("/i/membermagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazine.jsp");// クライアント側
        fp.storeFile("/j/membermagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazine.jsp");// クライアント側
        fp.storeFile("/ez/membermagazine.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinedelete.jsp");// クライアント側
        fp.storeFile("/i/membermagazinedelete.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinedelete.jsp");// クライアント側
        fp.storeFile("/j/membermagazinedelete.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinedelete.jsp");// クライアント側
        fp.storeFile("/ez/membermagazinedelete.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinedeleteupdate.jsp");// クライアント側
        fp.storeFile("/i/membermagazinedeleteupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinedeleteupdate.jsp");// クライアント側
        fp.storeFile("/j/membermagazinedeleteupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinedeleteupdate.jsp");// クライアント側
        fp.storeFile("/ez/membermagazinedeleteupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinememberreplace.jsp");// クライアント側
        fp.storeFile("/i/membermagazinememberreplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinememberreplace.jsp");// クライアント側
        fp.storeFile("/j/membermagazinememberreplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinememberreplace.jsp");// クライアント側
        fp.storeFile("/ez/membermagazinememberreplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinememberreplaceupdate.jsp");// クライアント側
        fp.storeFile("/i/membermagazinememberreplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinememberreplaceupdate.jsp");// クライアント側
        fp.storeFile("/j/membermagazinememberreplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinememberreplaceupdate.jsp");// クライアント側
        fp.storeFile("/ez/membermagazinememberreplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinereplace.jsp");// クライアント側
        fp.storeFile("/i/membermagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinereplace.jsp");// クライアント側
        fp.storeFile("/j/membermagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinereplace.jsp");// クライアント側
        fp.storeFile("/ez/membermagazinereplace.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/i/membermagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/j/membermagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinereplaceupdate.jsp");// クライアント側
        fp.storeFile("/ez/membermagazinereplaceupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinerequest.jsp");// クライアント側
        fp.storeFile("/i/membermagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinerequest.jsp");// クライアント側
        fp.storeFile("/j/membermagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinerequest.jsp");// クライアント側
        fp.storeFile("/ez/membermagazinerequest.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinestate.jsp");// クライアント側
        fp.storeFile("/i/membermagazinestate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinestate.jsp");// クライアント側
        fp.storeFile("/j/membermagazinestate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinestate.jsp");// クライアント側
        fp.storeFile("/ez/membermagazinestate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinestateupdate.jsp");// クライアント側
        fp.storeFile("/i/membermagazinestateupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinestateupdate.jsp");// クライアント側
        fp.storeFile("/j/membermagazinestateupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermagazinestateupdate.jsp");// クライアント側
        fp.storeFile("/ez/membermagazinestateupdate.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermailmagazinelogin.jsp");// クライアント側
        fp.storeFile("/i/membermailmagazinelogin.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermailmagazinelogin.jsp");// クライアント側
        fp.storeFile("/j/membermailmagazinelogin.jsp", is);// サーバー側
        is.close();
        is = new FileInputStream("/hotenavi/demo/upd_mailmagazine/mobile/membermailmagazinelogin.jsp");// クライアント側
        fp.storeFile("/ez/membermailmagazinelogin.jsp", is);// サーバー側
        is.close();

    }
    finally
    {
        fp.disconnect();
    }
    return 0;
}
%>
