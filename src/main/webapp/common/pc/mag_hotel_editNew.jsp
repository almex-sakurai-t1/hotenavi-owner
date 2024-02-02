<%@ page contentType="text/html; charset=Windows-31J"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<%@ page import="com.hotenavi2.mailmagazine.*" %>
<%@ include file="../csrf/refererCheck.jsp" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/mag_edit_ini.jsp" %>
<%
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
    String loginHotelId = (String)session.getAttribute("LoginHotelId");
    String header_msg = "";
    boolean mag_project_flag = false;
    boolean mag_project_role_flag = false;
    String magazine_project_msg = "";
    String query = "";
    // ﾊﾟﾗﾒｰﾀ
    int update_flag       = 0;
    int update_week       = 0;
    int update_force      = 0;
    String message        = "";
    String key_word       = "";
    int last_update       = 0;
    int last_uptime       = 0;
    int last_hour         = 0;
    int last_minute       = 0;
    int last_second       = 0;
    String address        = "";
    String address_mailto    = "";
    String mag_address    = "";
    int member_only       = 0;
    int group_cancel_flag = 0;
    int group_add_flag    = 0;
    int change_flag       = 0;
    int delete_flag       = 0;
    int add_flag          = 0;
    int report_mail_flag  = 0;

    String hotelid = "";
    // ホテルID取得
    String selecthotel = (String)session.getAttribute("SelectHotel");
    if    (selecthotel.compareTo("all") == 0)
    {
           hotelid    = loginHotelId;
    }
    else
    {
           hotelid    = selecthotel;
    }
    String target_hotelid = "";
    String reply_to_address = "";
    String unsubscribe_url = "";

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
        Logging.error("foward Exception " + request.getRequestURI() + " e=" + e.toString(), e);
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
        query = "SELECT * FROM menu WHERE hotelid = ?";
        query = query + " AND contents='search.jsp'";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,selecthotel );
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            member_flag = true;
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
    try
    {
        query = "SELECT * FROM menu_config WHERE hotelid = ?";
        query = query + " AND data_type=2";
        query = query + " AND contents='mailmagazine'";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,selecthotel );
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            member_flag = true;
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
 //ホテル名の取得
    String            hname = "";
    try
    {
        query = "SELECT * FROM hotel WHERE hotel_id=?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid);
        result      = prestate.executeQuery();
        if( result.next() != false )
        {
            hname       = result.getString("name");
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
//
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
                key_word          = result.getString("key_word");
                last_update       = result.getInt("last_update");
                last_uptime       = result.getInt("last_uptime");
                address           = result.getString("address");
                mag_address       = result.getString("mag_address");
                address_mailto    = result.getString("address_mailto");
                member_only       = result.getInt("member_only");
                change_flag       = result.getInt("change_flag");
                group_cancel_flag = result.getInt("group_cancel_flag");
                delete_flag       = result.getInt("delete_flag");
                group_add_flag    = result.getInt("group_add_flag");
                add_flag          = result.getInt("add_flag");
                report_mail_flag  = result.getInt("report_mail_flag");
                target_hotelid    = result.getString("target_hotelid");
                reply_to_address    = result.getString("reply_to_address");
                unsubscribe_url    = result.getString("unsubscribe_url");
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
    int   data_type  = 62;
    int   i          = 0;
    String[]  msg             = new String[8];
    for( i = 0 ; i < 8 ; i++ )
    {
        msg[i]       = "";
    }

    try
    {
        query = "SELECT * FROM edit_event_info WHERE hotelid = ?";
        query = query + " AND data_type= ?";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1,hotelid );
        prestate.setInt(2,data_type);
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
        DBConnection.releaseResources(result);
        DBConnection.releaseResources(prestate);
    }

    String hotel_name    = "";
    String hotel_address = "";
    String hotel_tel     = "";
    String body          = "";

    query = "SELECT * FROM hh_hotel_basic WHERE hotenavi_id = ?";
    prestate    = connection.prepareStatement(query);
    prestate.setString(1,hotelid );
    result      = prestate.executeQuery();
    i = 0;
    if( result != null )
    {
        while( result.next() != false )
        {
            i++;
            hotel_name    = result.getString("hh_hotel_basic.name");
            hotel_address = result.getString("hh_hotel_basic.address_all");
            hotel_tel     = result.getString("hh_hotel_basic.tel1");
            if ( i == 1)
            {
                body = body + "====================" + "\r\n";
            }
            body = body + hotel_name    + "\r\n";
            body = body + hotel_address + "\r\n";
            body = body + hotel_tel     + "\r\n";
        }
    }
    DBConnection.releaseResources(result,prestate,connection);

%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>メールマガジン管理編集</title>
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<% if (loginHotelId.compareTo("demo") == 0){%>
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<%}%>
<script type="text/javascript" src="../../common/pc/scripts/mailmagazine_form.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/tohankaku.js"></script>

<script type="text/javascript">
function MM_openInput(hotelid){
  if (document.getElementById("col_msg1").value.indexOf("[unsubscribe]") == -1){
 //   alert("「メールの署名」に「解約用URL」をいれてください。");
 //   return;
  }
  document.form1.target = '_self';
  document.form1.action = 'mag_hotel_edit_input.jsp?HotelId='+hotelid;
  document.form1.submit();
}

function MM_Start(){
  if ("<%=unsubscribe_url%>" == ""){
    alert("「解約用URL」を更新してください。");
    return;
  }

  if (document.getElementById("col_msg1").value.indexOf("[unsubscribe]") == -1){
 //  alert("「メールの署名」に「解約用URL」をいれてください。");
 //   return;
  }
  document.getElementById("mailmagazineSystemArea").innerText = "プロジェクト更新中です。しばらくお待ちください"
  document.form1.target = '_self';
  document.form1.action='../../common/pc/mag_hotel_mailmagazine_start.jsp';
  document.form1.submit();
}

function MM_Delete(){
  if ( confirm("本当に削除しますか？") ){
    document.form1.target = '_self';
    document.form1.action='../../common/pc/mag_hotel_mailmagazine_delete.jsp';
    document.form1.submit();
  }
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
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF">メルマガ送信設定　更新</font></td>
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
}else{
%>
                  <tr>
                    <td colspan="2" class="size12"><font color="#CC0000"><strong>※このページを編集し終えたら、「<%= header_msg %>」ボタンを必ず押してください</strong></font></td>
                    <td align="right">&nbsp;</td>
                  </tr>
                 <form name=form1 method=post>
                  <tr class="honbun">
                  <td align="left" valign="middle" bgcolor="#969EAD" colspan="2">
					        <input name="regsubmit" type=button value="<%= header_msg %>" onClick="MM_openInput('<%= hotelid %>');">
                  <input name="hname" type="hidden" value="<%=hname%>">
                  <input type='hidden' name='csrf' value='<%=(String)session.getAttribute("csrf")%>'>
                </td>
                  </tr>
                  <tr class="honbun">
                    <td nowrap class="size12">メールマガジン用メールアドレス</td>
                    <td nowrap class="size12"><%= address %></td>
                  </tr>
                  <tr class="honbun">
                    <td nowrap class="size12">メールマガジン送信元メールアドレス</td>
                    <td nowrap class="size12"><%= mag_address %></td>
                  </tr>
                  <tr class="honbun">
                    <td nowrap class="size12">メールマガジン返信先メールアドレス</td>
<%
        if  (imedia_user == 1)
        {
%>        					<td nowrap class="size12"><input name="reply_to_address" type="text" size="50" value="<%=reply_to_address.equals("")?address:reply_to_address%>" style="ime-mode:disabled"></td>
<%
        }
        else
        {
%>        					<td nowrap class="size12"><input name="reply_to_address" type="text" size="50" value="<%=reply_to_address.equals("")?address:reply_to_address%>" style="ime-mode:disabled" readonly>変更の際は(株)アルメックスまでご連絡ください</td>
<%
        }
%>
                 </tr>
                 <tr class="honbun">
                  <td nowrap class="size12">メールマガジン解除URL</td>
<%
      if  (imedia_user == 1)
      {
%>                <td nowrap class="size12"><input name="unsubscribe_url" type="text" size="50" value="<%= unsubscribe_url.equals("") ? "https://www.hotenavi.com/" + hotelid + "/mailmagazine/cancel" : unsubscribe_url %>" style="ime-mode:disabled"></td>
<%
      }
      else
      {
%>                <td nowrap class="size12"><input name="unsubscribe_url" type="text" size="50" value="<%= unsubscribe_url.equals("") ? "https://www.hotenavi.com/" + hotelid + "/mailmagazine/cancel" : unsubscribe_url %>" style="ime-mode:disabled" readonly>変更の際は(株)アルメックスまでご連絡ください</td>
<%
      }
%>
               </tr>
<%
        if  (imedia_user == 1)
        {
%>             <tr class="honbun">
                    <td nowrap class="size12">ホテルへ一言専用メールアドレス</td>
            				<td nowrap class="size12"><input name="address_mailto" type="text" size="50" value="<%= address_mailto %>" style="ime-mode:disabled">（メールマガジン用と違うときのみ入力）</td>
                  </tr>
<%
        }
        else
        {
%>
					<input name="address_mailto" type="hidden" value="<%= address_mailto %>">
<%
        }
%>
 
                 <tr align="left">
                    <td><img src="../../common/pc/image/spacer.gif" width="20" height="10"></td>
                  </tr>
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
                <input name="key_word" type="hidden"  value="<%= key_word %>">

<!--小見出しここから(1) -->
<%
    for( i = 0 ; i < 8 ; i++ )
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
						<strong><%=Explain[disp_idx][i]%></strong>
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
							<textarea id="col_msg<%= i + 1 %>" name=col_msg<%= i + 1 %> rows=8 cols=64  onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onkeyup="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onmouseup="get_pos(col_msg<%= i + 1 %>);" onmousedown="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onblur="dispArea<%= i + 1 %>.innerHTML='';"><% 
    if (msg[i].compareTo("")!= 0)
    {%><%= msg[i].replace("\"","&quot;") %><%
    }
    else
    {
%><%= body %><%
    }
%></textarea>
<%
    if (Explain[disp_idx][i].equals("メールの署名"))
    {
%>
<input type="button" value="解約URLの挿入" id="insert_unsubscribe" onclick="insertUnSubscribe('<%= i + 1 %>')"> 
<%
    }
%>
							<input name="old_msg<%= i + 1 %>" type="hidden" value="<%= msg[i].replace("\"","&quot;") %>" >
						</div> 
						<div class="size12">
<%
            if (Decoration[disp_idx][i] == 1)
            {
%>
								装飾する場合は、マウス等で装飾したいテキスト範囲を指定してから各ボタンをクリックしてください。<br>
<%
            }
%>
<br/>
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
checkUnsubscribe(1);

function insertUnSubscribe(i){
  var textArea = document.getElementById('col_msg'+i);
  textArea.value = textArea.value.substr(0, textArea.selectionStart)
			+ "\r\n[unsubscribe]\r\n" 
			+ textArea.value.substr(textArea.selectionStart);
  checkUnsubscribe(i);
}
function checkUnsubscribe(i){
  var textArea = document.getElementById('col_msg'+i);
  if (textArea.value.indexOf("[unsubscribe]") != -1){
    document.getElementById("insert_unsubscribe").style.display = "none";
  }
  else{
    document.getElementById("insert_unsubscribe").style.display = "inline";
  }
}
</script>
				<tr align="left">
					<td colspan="2" >
						<div class="honbun" id="dispArea<%= i + 1 %>">
						</div>
					</td>
				</tr>
                <tr align="left" bgcolor="#FFFFFF">
                    <td colspan="2"><img src="/common/pc/image/spacer.gif" width="200" height="3"></td>
                </tr>
<%
        }
    }
    if  (imedia_user == 1)
    {
%>
<tr align="left" bgcolor="#FFFFFF">
  <td colspan="2" class="honbuntitlebar_text">
    <div class="honbun_margin" style="float:left;clear:both;">
      <tr>
        <td colspan="2" align="left" valign="middle" style="font-size:12px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
          <strong>メールマガジンシステムプロジェクト</strong>
        </td>
      </tr>
      <tr align="left" bgcolor="#FFFFFF">
        <td colspan="2" class="honbuntitlebar_text">
          <div class="honbun_margin" style="float:left;clear:both;" id="mailmagazineSystemArea">
<%  
    LogicProjectCheck logicProjectCheck = new LogicProjectCheck();
    if (!logicProjectCheck.check(hotelid))
    {
%>        <input type="button" onclick="MM_Start();" value="メールマガジンシステムプロジェクト更新"><br>
<%      
    }
%> 
<%=logicProjectCheck.getDispMssage()%><br>
<%
    if(logicProjectCheck.getResponseCode() == 200)
    {
%>        <input type="button" onclick="MM_Delete();" value="メールマガジンシステムプロジェクト削除"><br>
<%      
    }
%></div></td></tr>
    </div>
  </td>
</tr>
<%
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
