<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.hotenavi2.common.*" %>
<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
<%@ include file="../../common/pc/qaedit_message_ini.jsp" %>
<%
    String     loginHotelId  = (String)session.getAttribute("LoginHotelId");
    String disp_type = ReplaceString.getParameter(request,"DispType");
    if  (disp_type == null)
    {
         disp_type = "0";
    }
    String hotelid = (String)session.getAttribute("SelectHotel");
    if( hotelid == null )
    {
%>
        <jsp:forward page="error/error.html" />
<%
    }
    // セッションの確認
    if( ownerinfo.HotelId.compareTo("") == 0 )
    {
%>
        <jsp:forward page="timeout.html" />
<%
    }
%>
<%

    int     i  = 0;
    // ﾊﾟﾗﾒｰﾀ取得
    // ホテルID取得
    String paramId   = ReplaceString.getParameter(request,"Id");
    int    id  = 0;
    if     (paramId != null) id = Integer.parseInt(paramId);
    String query ="";
    DateEdit  de = new DateEdit();

    int       disp_flg   = 1;
    java.util.Date start = new java.util.Date();
    java.util.Date end = new java.util.Date();
    String    title      = "";
    String    title_color = "";
    int       nowdate     = Integer.parseInt(de.getDate(2));
    int       last_update = nowdate;
    boolean   EditFlag    = false;
    boolean   NewFlag     = false;

    String[]  msg_title       = new String[8];
    String[]  msg_title_color = new String[8];
    String[]  msg             = new String[8];

    for( i = 0 ; i < 8 ; i++ )
    {
        msg_title[i] = "";
        msg[i]       = "";
    }


    Connection        connection  = null;
    PreparedStatement prestate    = null;
    ResultSet         result      = null;
    connection  = DBConnection.getConnection();

    if( id == 0 )
    {
        NewFlag    = true;
    }
    else
    {
        try
        {
            query = "SELECT * FROM edit_event_info WHERE hotelid=?";
            query = query + " AND data_type=?";
            query = query + " AND id=?";
            prestate    = connection.prepareStatement(query);
            prestate.setString(1,hotelid );
            prestate.setInt(2,data_type);
            prestate.setInt(3,id);
            result      = prestate.executeQuery();
            if( result.next() != false )
            {
                disp_flg    = result.getInt("disp_flg");
                start       = result.getDate("start_date");
                end         = result.getDate("end_date");
                title       = result.getString("title");
                title_color = result.getString("title_color");
                last_update = result.getInt("last_update");

                for( i = 0 ; i < 8 ; i++ )
                {
                    if( result.getString("msg" + (i + 1) + "_title").length() != 0 || result.getString("msg" + (i + 1)).length() > 1)
                    {
                        msg_title[i]       = result.getString("msg" + (i + 1) + "_title");
                        msg_title_color[i] = result.getString("msg" + (i + 1) + "_title_color");
                        msg[i]             = result.getString("msg" + (i + 1));
                        msg[i] = msg[i].replace("<iframe","<span");
                        msg[i] = msg[i].replace("</iframe","</span");
                    }
                }
            }
            else
            {
                NewFlag    = true;
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
    }
    DBConnection.releaseResources(connection);


    int start_year  = start.getYear()+1900;
    int start_month = start.getMonth()+1;
    int start_day   = start.getDate();
    int start_date  = start_year * 10000 + start_month * 100 + start_day;

    int end_year    = end.getYear()+1900;
    int end_month   = end.getMonth()+1;
    int end_day     = end.getDate();
    int end_date    = end_year * 10000 + end_month * 100 + end_day;

//    if (NewFlag)
//    {
//        start_date    = de.addDay(start_date,1);
//    }
    start_year    = start_date / 10000;
    start_month   =(start_date / 100) % 100;
    start_day     = start_date % 100;

    if (last_update != nowdate) EditFlag = true;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
<title>アンケートメッセージ編集</title>
<% if (loginHotelId.compareTo("hotenavi") != 0){%>
<link href="../../<%= hotelid %>/pc/contents.css" rel="stylesheet" type="text/css">
<%}%>
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
        <td width="3">&nbsp;</td>
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
						<strong>アンケートメッセージ</strong>
						&nbsp;&nbsp;<strong><%if(NewFlag){%>新規作成<%}else{%>更新<%}%></strong>
					</td>
				</tr>
                  <tr>
                    <td class="red12"><strong>※このページを編集し終えたら、「下書き保存(非掲載）」もしくは「保存（掲載）」ボタンを必ず押してください。</strong></td>
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
                <td>
                  <table width="100%" border="0" cellspacing="0" cellpadding="2" >
                  <form name=form1 method=post>
                    <tr bgcolor="#FFFFFF">
                      <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="60%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
                          <input name="regsubmit1" type=button value="下書き保存（非掲載）" onClick="if (validation_range()){MM_openInput2('qaedit_message', '<%= hotelid %>', <%= data_type %>, <%= id %>,0)}">
                          <input name="regsubmit2" type=button value="保存（掲載）" onClick="if (validation_range()){MM_openInput2('qaedit_message', '<%= hotelid %>', <%= data_type %>, <%= id %>,1)}">
<%
    if (!NewFlag)
    {
%>
						  <input name="BackUp" type="checkbox" value="1">Backupを残す
<%
    }
%>
						  </td>
                          <td width="1%" align="center" valign="middle" class="size12" bgcolor="#969EAD">
                          </td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td  class="size12">
                      <div align="left" class="size12" style="CLEAR: both; FLOAT: left;color:black">
                        表示期間：
                      <input name="col_start_yy" type="text" size="4" maxlength="4" value="<%= start_year %>" onChange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      年
                      <input name="col_start_mm" type="text" size="2" maxlength="2" value="<%= start_month %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      月
                      <input name="col_start_dd" type="text" size="2" maxlength="2" value="<%= start_day %>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      日～
                      <input name="col_end_yy" type="text" size="4" maxlength="4"  value="<%if(NewFlag){%>2999<%}else{%><%= end_year %><%}%>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      年
                      <input name="col_end_mm" type="text" size="2" maxlength="2" value="<%if(NewFlag){%>12<%}else{%><%= end_month %><%}%>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      月
                      <input name="col_end_dd" type="text" size="2" maxlength="2" value="<%if(NewFlag){%>31<%}else{%><%= end_day %><%}%>"  onchange="setDayRange(this,'<%=start_date%>','<%=end_date%>');" style="text-align:right;">
                      日
                      </div>
                    </td>
                  </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>

<!--小見出しここから(1) -->
<%
    for( i = 0 ; i < 8 ; i++ )
    {
        if( Explain[0][i].length() != 0 )
        {
%>
				<tr>
					<td class="honbuntitlebar">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" height="16">
							<tr>
								<td class="honbuntatitlebar_text">
									<div id="msg_title_<%= i + 1 %>" style="color:<%= msg_title_color[i] %>">
										<%=Explain[0][i]%>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr align="left" bgcolor="#FFFFFF">
					<td class="size10"><img src="/common/pc/image/spacer.gif" width="200" height="3"></td>
                </tr>
				<tr bgcolor="#FFFFFF">
					<td align="left" valign="top">
						<div class="size12">
						<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="size12">
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
								<img border="0" style="background-color:white" src="/common/pc/image/color_btn.gif" name="color_btn_d<%= i + 1 %>" width="100" height="22" align="absmiddle" id="color_btn_d<%= i + 1 %>" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onClick="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);MM_openBrWindowDetail('../../common/pc/event_edit_select_color.html','色見本','width=240,height=180',<%= i + 1 %>)" onMouseOver="MM_swapImage('color_btn_d<%= i + 1 %>','','/common/pc/image/color_btn_o.gif',1)" onMouseOut="MM_swapImgRestore()">
							</td>
						</tr>
						</table>
						</div>
					</td>
				</tr>
				<tr align="left" bgcolor="#FFFFFF">
					<td class="honbuntitlebar_text">
						<div class="honbun_margin" style="float:left;clear:both;">
							<textarea id="col_msg<%= i + 1 %>" name=col_msg<%= i + 1 %> rows=10 cols=64 onchange="<%if(EditFlag){%>CheckEdit();<%}%>" onfocus="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onkeyup="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);get_pos(col_msg<%= i + 1 %>);" onmouseup="get_pos(col_msg<%= i + 1 %>);" onmousedown="ColFunc(<%= i + 1 %>,'<%=hotelid%>',<%=data_type%>);" onblur="dispArea<%= i + 1 %>.innerHTML='';"><%= msg[i].replace("\"","&quot;") %></textarea>
							<input name="old_msg<%= i + 1 %>" type="hidden" value="<%= msg[i].replace("\"","&quot;") %>" >
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
if( document.getElementById("col_msg<%= i + 1 %>").setSelectionRange ){
  bl=2;
} else if( document.selection.createRange ){
  bl=1;
}
</script>
				<tr align="left">
					<td >
						<div class="honbun" id="dispArea<%= i + 1 %>">
						</div>
					</td>
				</tr>
                <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="3"></td>
                </tr>
<%
        }
    }
%>
							<input type="hidden" name="hreflink" id="hreflink" value="http://">
							<input type="hidden" name="hrefblank" id="hrefblank" value="_self">
							<input type="hidden" name="imglink" id="imglink" value="image/">
<!-- 小見出しここまで -->

                  <tr align="left" bgcolor="#FFFFFF">
                    <td><img src="/common/pc/image/spacer.gif" width="200" height="8"></td>
                  </tr>
                  <tr align="left" bgcolor="#FFFFFF">
                    <td>
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td align="center" valign="middle" bgcolor="#969EAD">
                          <input name="regsubmit1" type=button value="下書き保存（非掲載）" onClick="if (validation_range()){MM_openInput2('qaedit_message', '<%= hotelid %>', <%= data_type %>, <%= id %>,0)}">
                          <input name="regsubmit2" type=button value="保存（掲載）" onClick="if (validation_range()){MM_openInput2('qaedit_message', '<%= hotelid %>', <%= data_type %>, <%= id %>,1)}">
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>

              </td>
              </form>

              </tr>
<%
    if(!NewFlag)
    {
%>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;</td>
                <td valign="top"></td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td valign="top">&nbsp;</td>
                <td height="30" align="center" valign="middle" bgcolor="#969EAD">
                  <form action="qaedit_message_delete.jsp?HotelId=<%= hotelid %>&Id=<%= id %>" method="POST">
                  <input name="submit_del" type=submit value="削除" >
                </td></form>
              </tr>
<%
    }
%>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td align="center">
                  <form action="qaedit_message.jsp?HotelId=<%= hotelid %>" method="POST">
                  <input name="submit_ret" type=submit value="戻る" >
                  </form>
                </td>
              </tr>
              <tr bgcolor="#FFFFFF">
                <td>&nbsp;</td>
                <td>&nbsp;</td>
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
