<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>
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

    int     i  = 0;
    // ﾊﾟﾗﾒｰﾀ取得
    // ホテルID取得
    String hotelid = (String)session.getAttribute("SelectHotel");
if (hotelid.compareTo("all") == 0)
{
    hotelid = ReplaceString.getParameter(request,"HotelId");
	if( hotelid != null && !CheckString.hotenaviIdCheck(hotelid))
	{
		hotelid="0";
%>
		<script type="text/javascript">
		<!--
		var dd = new Date();
		setTimeout("window.open('timeoutdisp.html?"+dd.getSeconds()+dd.getMinutes()+dd.getHours()+"','_top')",0000);
		//-->
		</script>
<%
	}
}
    String    seq = ReplaceString.getParameter(request,"Seq");
    String    CategoryId = ReplaceString.getParameter(request,"CategoryId");
    if        (CategoryId == null) CategoryId = "0";
    int       category_id = Integer.parseInt(CategoryId);
    DateEdit  de = new DateEdit();

    int       disp_idx    = 0;
    int       disp_flg    = 1;
    int       disp_from   = 0;
    int       disp_to     = 0;
    String    title       = "";
    String    title_color = "";
    String    msg         = "";
    int       nowdate     = Integer.parseInt(de.getDate(2));
    int       last_update = nowdate;
    boolean   EditFlag    = false;
    boolean   NewFlag     = false;

    int       goods_count = 0;
    int       member_only = 0;

    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    if( seq.compareTo("0") == 0 ) //新規作成
    {
        NewFlag    = true;
        try
        {
            final String query = "SELECT * FROM goods_category WHERE hotelid = ? "
        	                   + "ORDER BY category_id DESC";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                category_id  = result.getInt("category_id") + 1;
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
            final String query = "SELECT * FROM goods_category WHERE hotelid = ? "
                               + "ORDER BY disp_idx DESC";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                disp_idx  = result.getInt("disp_idx") + 1;
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
    }
    else
    {
        try
        {
            final String query = "SELECT * FROM goods_category WHERE hotelid = ? "
                               + "AND category_id = ? "
                               + "AND seq = ? ";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, category_id);
            prestate.setInt(3, Integer.parseInt(seq));
            result      = prestate.executeQuery();
            if( result.next() != false )
            {

                disp_idx    = result.getInt("disp_idx");
                disp_from   = result.getInt("disp_from");
                disp_to     = result.getInt("disp_to");
                title       = result.getString("title");
                title_color = result.getString("title_color");
                msg         = result.getString("msg");
                last_update = result.getInt("last_update");
                member_only = result.getInt("member_only");
            }
            else
            {
                NewFlag    = true;
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
    }

    if  (disp_idx == 0)
    {
        goods_count = 999;
    }
    else
    {
        try
        {       //景品マスタ登録件数の抽出
            final String query = "SELECT count(*) FROM goods WHERE hotelid = ? "
                               + "AND category_id = ? "
                               + "AND disp_from <= ? "
                               + "AND disp_to >= ?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1, hotelid);
            prestate.setInt(2, category_id);
            prestate.setInt(3, nowdate);
            prestate.setInt(4, nowdate);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                goods_count = result.getInt(1);
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
    }
    int               imedia_user = 0;
    int               level       = 0;
    // imedia_user のチェック
    try
    {
        final String query = "SELECT * FROM owner_user WHERE hotelid = ? "
        	               + "AND userid = ? ";
        prestate    = connection.prepareStatement(query);
        prestate.setString(1, loginhotel);
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

    DBConnection.releaseResources(connection);

    if (NewFlag)
    {
        disp_from     = de.addDay(nowdate,1);
        disp_to       = nowdate;
    }
    int start_year    = disp_from / 10000;
    int start_month   =(disp_from / 100) % 100;
    int start_day     = disp_from % 100;
    int end_year      = disp_to / 10000;
    int end_month     =(disp_to / 100) % 100;
    int end_day       = disp_to % 100;

    if (last_update != nowdate) EditFlag = true;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>景品交換メッセージ他設定</title>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<link href="http://www.hotenavi.com/<%= hotelid %>/contents.css" rel="stylesheet" type="text/css">
<link href="../../common/pc/style/contents.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../common/pc/scripts/edit_form.js"></script>
<script type="text/javascript" src="../../common/pc/scripts/tohankaku.js"></script>

</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="20">
          <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="180" height="20" nowrap bgcolor="#22333F" class="tab"><font color="#FFFFFF"> 編　集</font></td>
              <td width="15" height="20"><img src="/common/pc/image/tab1.gif" width="15" height="20"></td>
              <td height="20">
                <div><img src="/common/pc/image/spacer.gif" width="200" height="20"></div>
              </td>
            </tr>
          </table>
        </td>
        <td width="3"  colspan=2>&nbsp;</td>
      </tr>
      <!-- ここから表 -->
      <tr>
        <td align="center" valign="top" class="size10">
           <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="8" class="size10" bgcolor="#FFFFFF"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
                  <tr>
                    <td align="right" class="size10">&nbsp;</td>
                    <td align="right" class="size10">&nbsp;</td>
                    <td align="right" class="size10">&nbsp;</td>
                  </tr>
				<tr>
					<td align="left" colspan=3 valign="middle" style="font-size:14px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<strong>景品カテゴリー</strong>
						&nbsp;&nbsp;<strong><%if(NewFlag){%>新規作成<%}else{%>更新<%}%></strong>
					</td>
				</tr>
                  <tr>
                    <td class="red12"><strong>※このページを編集し終えたら、「このカテゴリーを保存」ボタンを必ず押してください</strong></td>
                    <td align="right" class="size12">&nbsp;</td>
                    <td align="right" class="size12">&nbsp;</td>
                  </tr>
                  <tr>
                    <td colspan="3"  class="size10"><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                </table>
              </div>
              </td>
            </tr>
          </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0" >
              <tr>
                <td  class="size12" bgcolor="#FFFFFF"><img src="/common/pc/image/spacer.gif" width="8" height="12"></td>
                <td  colspan=2>
                  <table width="100%" border="0" cellspacing="0" cellpadding="2" >
                  <form name=form1 method=post>
                    <tr bgcolor="#FFFFFF">
                      <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="60%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
						  </td>
                          <td width="40%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
                          </td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td  class="size12">
                      <div align="left" class="size12" style="CLEAR: both; FLOAT: left;color:black">
                        表示期間：<input name="col_disp_flg" type="hidden" value="1">
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start_year %>" onChange="setDayRange(this,'<%= disp_from %>','<%= disp_to %>');" style="text-align:right;">
                      年
                      <input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start_month %>"  onchange="setDayRange(this,'<%= disp_from %>','<%= disp_to %>');" style="text-align:right;">
                      月
                      <input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start_day %>"  onchange="setDayRange(this,'<%= disp_from %>','<%= disp_to %>');" style="text-align:right;">
                      日～
                      <input name="col_end_yy" type="text" size="4" maxlength="4"  value="<%if(NewFlag){%>2999<%}else{%><%= end_year %><%}%>"  onchange="setDayRange(this,'<%= disp_from %>','<%= disp_to %>');" style="text-align:right;">
                      年
                      <input name="col_end_mm" type="text" size="2" maxlength="2" value="<%= end_month %>"  onchange="setDayRange(this,'<%= disp_from %>','<%= disp_to %>');" style="text-align:right;">
                      月
                      <input name="col_end_dd" type="text" size="2" maxlength="2" value="<%= end_day %>"  onchange="setDayRange(this,'<%= disp_from %>','<%= disp_to %>');" style="text-align:right;">
                      日
						<img src="/common/pc/image/spacer.gif" width="50" height="8">
                        表示順番：
<%
    if (disp_idx != 0)
    {
%>
                        <input name="col_disp_idx" type="text" size="3" value="<%= disp_idx %>" style="text-align:right;ime-mode:disable">

<%
    }
    else
    {
%>
                        <input name="col_disp_idx" type="hidden" size="3" value="<%= disp_idx %>" style="text-align:right;"><%= disp_idx %>
						初期表示（おすすめ商品など）
<%
    }
%>
                      </div>
                    </td>
                  </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>

                  <tr align="left" >
					<td>
						<table cellspacing="0" cellpadding="0" class="subtitlebar_basecolor" width="100%">
							<tr valign="middle">
								<td width="7" class="subtitlebar_linecolor"><img src="../../common/pc/image/spacer.gif" width="7" height="18"></td>
								<td width="3" ><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td width="3" class="subtitlebar_linecolor" ><img src="../../common/pc/image/spacer.gif" width="3" height="18"></td>
								<td>
									<div class="subtitle_text" id="title_big"  style="color:<%= title_color %>">
										カテゴリー名：<input name="col_title" type="text" size="64" value="<%= title.replace("\"","&quot;") %>" onChange="<%if(EditFlag){%>if ((document.form1.old_title.value != document.form1.col_title.value)){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>">
										<input name="old_title" type="hidden" value="<%= title.replace("\"","&quot;") %>">
										<small><strong>文字色：</strong><input name="col_title_color" type="text" size="8" readonly value="<%= title_color %>" onchange="title_big.style.color=document.form1.col_title_color.value"></small>
										<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn1" width="100" height="22" align="absmiddle" id="color_btn1" onClick="MM_openBrWindow('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',0)" onMouseOver="MM_swapImage('color_btn1','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
									</div>
								</td>
							</tr>
						</table>
					</td>
                  </tr>
				<tr>
					<td align="left" valign="middle" style="font-size:12px;padding:3px;border:1px solid #663333;background-color:#FFFFFF;color:#663333;" colspan="3">
						<strong><%if (disp_idx == 0){%>初期表示商品を掲載しない場合の表示メッセージ<%}else{%>商品未登録時の表示メッセージ<%}%></strong>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td align="left" valign="top">
						<div class="size12">
						<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="size12">
								<strong>&nbsp;本文</strong>
								<select id="spansize1" name="spansize1" onfocus="ColFunc(1,'<%=hotelid%>',0);" onchange="if(spansize1.selectedIndex !=0){enclose('<font size=&quot;' + spansize1.options[spansize1.selectedIndex].value + '&quot;>', '</font>');spansize1.selectedIndex=0;return false;}">
									<option value="">文字サイズ</option><option value="1">小さく</option><option value="3">少し大きく</option><option value="4">大きく</option><option value="5">とても大きく</option></select>
								<input type="button" name="strong1" value="太" style="font-weight:bold" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<strong>', '</strong>');return false;">
								<input type="button" name="em1" value="斜" style="font-style:oblique" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<em>', '</em>');return false;">
								<input type="button" name="under1" value="線" style="text-decoration:underline" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<u>', '</u>');return false;">
							</td>
							<td class="size12">
<input type="image" border="0" style="margin:1px;margin-left:5px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#000000&quot;>', '</font>');return false;" src="/common/pc/image/000000.gif" alt="black"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#FF0000&quot;>', '</font>');return false;" src="/common/pc/image/FF0000.gif" alt="red"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#FF1493&quot;>', '</font>');return false;" src="/common/pc/image/FF1493.gif" alt="deeppink"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#800080&quot;>', '</font>');return false;" src="/common/pc/image/800080.gif" alt="purple"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#0000FF&quot;>', '</font>');return false;" src="/common/pc/image/0000FF.gif" alt="blue"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#008000&quot;>', '</font>');return false;" src="/common/pc/image/008000.gif" alt="green"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#808000&quot;>', '</font>');return false;" src="/common/pc/image/808000.gif" alt="olive"/><br/>
<input type="image" border="0" style="margin:1px;margin-left:5px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#FFFFFF&quot;>', '</font>');return false;" src="/common/pc/image/FFFFFF.gif" alt="white"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#FA8072&quot;>', '</font>');return false;" src="/common/pc/image/FA8072.gif" alt="salmon"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#EE82EE&quot;>', '</font>');return false;" src="/common/pc/image/EE82EE.gif" alt="violet"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#9370DB&quot;>', '</font>');return false;" src="/common/pc/image/9370DB.gif" alt="mediumpurple"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#00BFFF&quot;>', '</font>');return false;" src="/common/pc/image/00BFFF.gif" alt="deepskyblue"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#33CC33&quot;>', '</font>');return false;" src="/common/pc/image/33CC33.gif" alt="limegreen"/><input type="image" border="0" style="margin:1px;" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<font color=&quot;#FFD700&quot;>', '</font>');return false;" src="/common/pc/image/FFD700.gif" alt="gold"/>
							</td>
							<td>
								<input type="hidden" id="spancol1" name="spancol1" value="#" size=7 maxlength="7" onfocus="ColFunc(1,'<%=hotelid%>',0);" >
								<!--							<input type="button" name="spanbtn1" value="色" onfocus="ColFunc(1,'<%=hotelid%>',0);" onclick="enclose('<span style=&quot;color:' + document.getElementById('spancol1').value+'&quot;>', '</span>');return false;">-->
								<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn_d1" width="100" height="22" align="absmiddle" id="color_btn_d1" onfocus="ColFunc(1,'<%=hotelid%>',0);" onClick="ColFunc(1,'<%=hotelid%>',0);MM_openBrWindowDetail('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',1)" onMouseOver="MM_swapImage('color_btn_d1','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
							</td>
						</tr>
						</table>
						</div>
					</td>
				</tr>
				<tr align="left" bgcolor="#FFFFFF">
					<td class="honbuntitlebar_text">
						<div class="honbun_margin" style="float:left;clear:both;">
							<textarea id="col_msg1" name=col_msg1 rows=10 cols=64 onchange="<%if(EditFlag){%>if ((document.form1.old_title.value != document.form1.col_title.value)){document.form1.BackUp.checked=true;}else{document.form1.BackUp.checked=false;}<%}%>" onfocus="ColFunc(1,'<%=hotelid%>',0);get_pos(col_msg1);" onkeyup="ColFunc(1,'<%=hotelid%>',0);get_pos(col_msg1);" onmouseup="get_pos(col_msg1);" onmousedown="ColFunc(1,'<%=hotelid%>',0);" onblur="dispArea1.innerHTML='';"><%= msg.replace("\"","&quot;") %></textarea>
							<input name="old_msg" type="hidden" value="<%= msg.replace("\"","&quot;") %>" >
						</div>
						<div class="size12">
								装飾する場合は、マウス等で装飾したいテキスト範囲を指定してから各ボタンをクリックしてください。
						</div>
					</td>
				</tr>
<script language="javascript">
if( String("jadge") ){
 bl=3;
} else if( document.getElementById ){
 bl=4;
}
if( document.getElementById("col_msg1").setSelectionRange ){
  bl=2;
} else if( document.selection.createRange ){
  bl=1;
}
</script>
				<tr align="left">
					<td >
						<div class="honbun" id="dispArea1">
						</div>
					</td>
				</tr>
                <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="3"></td>
                </tr>
                </table>
              </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;</td>
                <td valign="top"  colspan=2></td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;
                </td>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
				     <input name="regsubmit" type=button value="このカテゴリーを保存" onClick="if (validation_range()){MM_openInput('goods_category', '<%= hotelid %>', <%= category_id %>, <%= seq %>)}">
<%
    if (!NewFlag)
    {
%>
						  <input name="BackUp" type="checkbox" value="1">Backupを残す
<%
    }
%>
				</td></form>
<%
    if(!NewFlag && (goods_count == 0 || imedia_user == 1))
    {
%>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
                  <form action="goods_category_delete.jsp?HotelId=<%= hotelid %>&CategoryId=<%= category_id %>&Seq=<%= seq %>" method="POST">
                  <input name="submit_del" type=submit value="削除">
                </td></form>
<%
    }
%>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td  colspan=2>&nbsp;</td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td align="center" colspan=2>
                  <form action="goods_list.jsp?HotelId=<%= hotelid %>" method="POST">
                  <input name="submit_ret" type=submit value="戻る" >
                  </form>
                </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td  colspan=2>&nbsp;</td>
              </tr>
            </table>
        </td>
        <td width="3" valign="top" align="left" height="100%" bgcolor="#FFFFFF">
          <table width="3" height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><img src="new/image/tab_kado.gif" width="3" height="3"></td>
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
  <tr bgcolor="#FFFFFF">
    <td><img src="/common/pc/image/spacer.gif" width="300" height="18"></td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <td align="center" valign="middle" class="size10"><!-- #BeginLibraryItem "/owner/Library/footer.lbi" --><img src="/common/pc/image/imedia.gif" width="63" height="18"><img src="/common/pc/image/spacer.gif" width="12" height="18" align="absmiddle">Copyrigtht&copy; almex
    inc . All Rights Reserved.<!-- #EndLibraryItem --></td>
  </tr>
  <tr>
    <td align="center" valign="middle"><img src="/common/pc/image/spacer.gif" width="300" height="12"></td>
  </tr>
</table>
</body>
</html>
